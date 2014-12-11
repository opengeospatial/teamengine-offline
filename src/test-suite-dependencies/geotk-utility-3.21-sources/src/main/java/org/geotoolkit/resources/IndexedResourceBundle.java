/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2001-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009-2012, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.resources;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import net.jcip.annotations.ThreadSafe;

import org.opengis.util.InternationalString;

import org.geotoolkit.lang.Debug;
import org.geotoolkit.util.Strings;
import org.geotoolkit.util.logging.Logging;
import org.geotoolkit.util.converter.Classes;


/**
 * {@link ResourceBundle} implementation accepting integers instead of strings for resource keys.
 * Using integers allow implementations to avoid adding large string constants into their
 * {@code .class} files and runtime images. Developers still have meaningful labels in their
 * code (e.g. {@code DIMENSION_MISMATCH}) through a set of constants defined in {@code Keys}
 * inner classes, with the side-effect of compile-time safety. Because integer constants are
 * inlined right into class files at compile time, the declarative classes is never loaded at
 * run time.
 * <p>
 * This class also provides facilities for string formatting using {@link MessageFormat}.
 *
 * @author Martin Desruisseaux (IRD)
 * @version 3.19
 *
 * @since 1.2
 * @module
 */
@ThreadSafe
public class IndexedResourceBundle extends ResourceBundle {
    /**
     * {@code true} if the {@link #getLogRecord(Level, int, Object)} should localize the log
     * message immediately instead than deferring the localization to the log formatting.
     * The default value is {@code false}. It should be set to {@code true} in a Glassfish
     * environment, which doesn't seem use the resource bundles as expected.
     * <p>
     * Note that this field may be removed in any future version if we find a better way
     * to get localized logging records to work.
     *
     * @since 3.19
     */
    public static volatile boolean immediateLocalization;

    /**
     * Maximum string length for text inserted into another text. This parameter is used by
     * {@link #summarize}. Resource strings are never cut to this length. However, text replacing
     * {@code "{0}"} in a string like {@code "Parameter name is {0}"} will be cut to this length.
     */
    private static final int MAX_STRING_LENGTH = 200;

    /**
     * The resource name of the binary file containing resources. It may be a file name or an
     * entry in a JAR file. The path must be relative to the package containing the subclass
     * of {@code IndexedResourceBundle}.
     */
    private final String filename;

    /**
     * The array of resources. Keys are an array index. For example, the value for key "14" is
     * {@code values[14]}. This array will be loaded only when first needed. We should not load
     * it at construction time, because some {@code ResourceBundle} objects will never ask for
     * values. This is particularly the case for ancestor classes of {@code Resources_fr_CA},
     * {@code Resources_en}, {@code Resources_de}, etc., which will only be used if a key has
     * not been found in the subclass.
     */
    private String[] values;

    /**
     * The locale for formatting objects like number, date, etc. There are two possible Locales
     * we could use: default locale or resource bundle locale. If the default locale uses the same
     * language as this ResourceBundle's locale, then we will use the default locale. This allows
     * dates and numbers to be formatted according to user conventions (e.g. French Canada) even
     * if the ResourceBundle locale is different (e.g. standard French). However, if languages
     * don't match, then we will use ResourceBundle locale for better coherence.
     */
    private transient Locale formatLocale;

    /**
     * The object to use for formatting messages. This object
     * will be constructed only when first needed.
     */
    private transient MessageFormat format;

    /**
     * The key of the last resource requested. If the same resource is requested multiple times,
     * knowing its key allows us to avoid invoking the costly {@link MessageFormat#applyPattern}
     * method.
     */
    private transient int lastKey;

    /**
     * Constructs a new resource bundle loading data from the given UTF file.
     *
     * @param filename The file or the JAR entry containing resources. The path must be relative
     *        to the package of the {@code IndexedResourceBundle} subclass being constructed.
     */
    protected IndexedResourceBundle(final String filename) {
        this.filename = filename;
    }

    /**
     * Returns a resource bundle of the specified class.
     *
     * @param  <T>     The resource bundle class.
     * @param  base    The resource bundle class.
     * @param  locale  The locale, or {@code null} for the default locale.
     * @return Resources in the given locale.
     * @throws MissingResourceException if resources can't be found.
     *
     * @see Vocabulary#getResources
     * @see Loggings#getResources
     * @see Errors#getResources
     */
    protected static <T extends IndexedResourceBundle> T getBundle(Class<T> base, Locale locale)
            throws MissingResourceException
    {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        // No caching; we rely on the one implemented in ResourceBundle.
        return base.cast(getBundle(base.getName(), locale, base.getClassLoader(), Loader.INSTANCE));
    }

    /**
     * Returns the locale to use for formatters. It is often the same than {@link #getLocale()},
     * except if the later has the same language than the default locale, in which case this
     * method returns the default locale. For example if this {@code IndexResourceBundle} is
     * for the French locale but the user is French Canadian, we will format the dates using
     * Canada French conventions rather than France conventions.
     */
    private Locale getFormatLocale() {
        if (formatLocale == null) {
            formatLocale = Locale.getDefault();
            final Locale rl = getLocale(); // Sometime null with IBM's JDK.
            if (rl != null && !formatLocale.getLanguage().equalsIgnoreCase(rl.getLanguage())) {
                formatLocale = rl;
            }
        }
        return formatLocale;
    }

    /**
     * Lists resources to the specified stream. If a resource has more than one line, only
     * the first line will be written. This method is used mostly for debugging purposes.
     *
     * @param  out The destination stream.
     * @throws IOException if an output operation failed.
     */
    @Debug
    public final void list(final Writer out) throws IOException {
        // Synchronization performed by 'ensureLoaded'
        list(out, ensureLoaded(null));
    }

    /**
     * Lists resources to the specified stream. If a resource has more than one line, only
     * the first line will be written. This method is used mostly for debugging purposes.
     *
     * @param  out    The destination stream.
     * @param  values The resources to list.
     * @throws IOException if an output operation failed.
     */
    private static void list(final Writer out, final String[] values) throws IOException {
        final String lineSeparator = System.getProperty("line.separator", "\n");
        for (int i=0; i<values.length; i++) {
            String value = values[i];
            if (value == null) {
                continue;
            }
            int indexCR = value.indexOf('\r'); if (indexCR < 0) indexCR = value.length();
            int indexLF = value.indexOf('\n'); if (indexLF < 0) indexLF = value.length();
            final String number = String.valueOf(i);
            out.write(Strings.spaces(5 - number.length()));
            out.write(number);
            out.write(":\t");
            out.write(value, 0, Math.min(indexCR,indexLF));
            out.write(lineSeparator);
        }
    }

    /**
     * Ensures that resource values are loaded. If they are not, loads them immediately.
     *
     * @param  key Key for the requested resource, or {@code null} if all resources
     *         are requested. This key is used mostly for constructing messages.
     * @return The resources.
     * @throws MissingResourceException if this method failed to load resources.
     */
    private String[] ensureLoaded(final String key) throws MissingResourceException {
        final String methodName = (key != null) ? "getObject" : "getKeys";
        LogRecord record = null;
        try {
            String[] values;
            synchronized (this) {
                values = this.values;
                if (values != null) {
                    return values;
                }
                /*
                 * Prepares a log record.  We will wait for successful loading before
                 * posting this record.  If loading fails, the record will be changed
                 * into an error record. Note that the message must be logged outside
                 * the synchronized block, otherwise there is dead locks!
                 */
                record = new LogRecord(Level.FINER, "Loaded resources for {0} from bundle \"{1}\".");
                /*
                 * Loads resources from the UTF file.
                 */
                InputStream in;
                String name = filename;
                while ((in = getClass().getResourceAsStream(name)) == null) { // NOSONAR
                    final int ext  = name.lastIndexOf('.');
                    final int lang = name.lastIndexOf('_', ext-1);
                    if (lang <= 0) {
                        throw new FileNotFoundException(filename);
                    }
                    final int length = name.length();
                    name = new StringBuilder(lang + (length-ext))
                            .append(name, 0, lang).append(name, ext, length).toString();
                }
                final DataInputStream input = new DataInputStream(new BufferedInputStream(in));
                this.values = values = new String[input.readInt()];
                for (int i=0; i<values.length; i++) {
                    values[i] = input.readUTF();
                    if (values[i].isEmpty())
                        values[i] = null;
                }
                input.close();
                /*
                 * Now, logs the message. This message is not localized.  Note that
                 * Locale.getDisplayName() may return different string on different
                 * Java implementation, but it doesn't matter here since we use the
                 * result only for logging purpose.
                 */
                String language = null;
                final Locale rl = getLocale(); // Sometime null with IBM's JDK.
                if (rl != null) {
                    language = rl.getDisplayName(Locale.US);
                }
                if (language == null || language.isEmpty()) {
                    language = "<default>";
                }
                record.setParameters(new String[] {language, getClass().getCanonicalName()});
            }
            Logging.log(IndexedResourceBundle.class, methodName, record);
            return values;
        } catch (IOException exception) {
            record.setLevel  (Level.WARNING);
            record.setMessage(exception.getLocalizedMessage());
            record.setThrown (exception);
            Logging.log(IndexedResourceBundle.class, methodName, record);
            final MissingResourceException error = new MissingResourceException(
                    exception.getLocalizedMessage(), getClass().getCanonicalName(), key);
            error.initCause(exception);
            throw error;
        }
    }

    /**
     * Returns an enumeration of the keys.
     */
    @Override
    public final Enumeration<String> getKeys() {
        // Synchronization performed by 'ensureLoaded'
        return new KeyEnum(ensureLoaded(null));
    }

    /**
     * The keys as an enumeration.
     */
    private static final class KeyEnum implements Enumeration<String> {
        private final String[] values;
        private int next=0;

        KeyEnum(final String[] values) {
            this.values = values;
            while (next < values.length) {
                if (values[next] != null) {
                    break;
                }
                next++;
            }
        }

        @Override
        public boolean hasMoreElements() {
            return next < values.length;
        }

        @Override
        public String nextElement() {
            while (next < values.length) {
                if (values[next] != null) {
                    return String.valueOf(next++);
                }
                next++;
            }
            throw new NoSuchElementException();
        }
    }

    /**
     * Gets an object for the given key from this resource bundle.
     * Returns null if this resource bundle does not contain an
     * object for the given key.
     *
     * @param key the key for the desired object
     * @throws NullPointerException if {@code key} is {@code null}
     * @return the object for the given key, or null
     */
    @Override
    protected final Object handleGetObject(final String key) {
        // Synchronization performed by 'ensureLoaded'
        final String[] values = ensureLoaded(key);
        final int keyID;
        try {
            keyID = Integer.parseInt(key);
        } catch (NumberFormatException exception) {
            return null; // This is okay as of 'handleGetObject' contract.
        }
        return (keyID >= 0 && keyID < values.length) ? values[keyID] : null;
    }

    /**
     * Makes sure that the {@code text} string is not longer than {@code maxLength} characters.
     * If {@code text} is not longer, it is returned unchanged (except for trailing blanks,
     * which are removed). If {@code text} is longer, it will be cut somewhere in the middle.
     * This method tries to cut between two words and replace the missing words with "(...)".
     * For example, the following string:
     *
     * <blockquote>
     *   "This sentence given as an example is way too long to be
     *    included in a message."
     * </blockquote>
     *
     * May be "summarized" by something like this:
     *
     * <blockquote>
     *   "This sentence given (...) included in a message."
     * </blockquote>
     *
     * @param  text The sentence to summarize if it is too long.
     * @param  maxLength The maximum length allowed for {@code text}.
     *         If {@code text} is longer, it will be summarized.
     * @return A sentence not longer than {@code maxLength}.
     */
    private static String summarize(String text, int maxLength) {
        text = Strings.trim(text);
        final int length = text.length();
        if (length <= maxLength) {
            return text;
        }
        /*
         * Computes maximum length for one half of the string. Take into
         * account the space needed for inserting the " (...) " string.
         */
        maxLength = (maxLength - 7) >> 1;
        if (maxLength <= 0) {
            return text;
        }
        /*
         * We will remove characters from 'break1' to 'break2', both exclusive. We try to adjust
         * 'break1' and 'break2' in such a way that the first and last characters to be removed
         * will be spaces or punctuation characters. Constants 'lower' and 'upper' are limit
         * values. If we don't find values for 'break1' and 'break2' inside those limits, we
         * will give up.
         */
        int break1 = maxLength;
        int break2 = length - maxLength;
        for (final int lower = (maxLength >> 1); break1 >= lower; break1--) {
            if (!Character.isUnicodeIdentifierPart(text.charAt(break1))) {
                while (--break1>=lower && !Character.isUnicodeIdentifierPart(text.charAt(break1)));
                break;
            }
        }
        for (final int upper = length - (maxLength >> 1); break2 < upper; break2++) {
            if (!Character.isUnicodeIdentifierPart(text.charAt(break2))) {
                while (++break2<upper && !Character.isUnicodeIdentifierPart(text.charAt(break2)));
                break;
            }
        }
        return Strings.trim(new StringBuilder(break1 + (length-break2) + 6)
                .append(text, 0, break1+1).append(" (…) ").append(text, break2, length).toString());
    }

    /**
     * Returns {@code arguments} as an array. If {@code arguments} is already an array, this array
     * or a copy of this array will be returned. If {@code arguments} is not an array, it will be
     * placed in an array of length 1. In any case, all the array's elements will be checked for
     * {@link String} objects. Any strings of length greater than {@link #MAX_STRING_LENGTH} will
     * be reduced using the {@link #summarize} method.
     *
     * @param  arguments The object to check.
     * @return {@code arguments} as an array.
     */
    private Object[] toArray(final Object arguments) {
        Object[] array;
        if (arguments instanceof Object[]) {
            array = (Object[]) arguments;
        } else {
            array = new Object[] {arguments};
        }
        for (int i=0; i<array.length; i++) {
            final Object element = array[i];
            if (element instanceof CharSequence) {
                final String s0;
                if (element instanceof InternationalString) {
                    s0 = ((InternationalString) element).toString(getFormatLocale());
                } else {
                    s0 = element.toString();
                }
                final String s1 = summarize(s0, MAX_STRING_LENGTH);
                if (!s0.equals(s1)) {
                    if (array == arguments) {
                        array = new Object[array.length];
                        System.arraycopy(arguments, 0, array, 0, array.length);
                    }
                    array[i] = s1;
                }
            } else if (element instanceof Throwable) {
                String message = ((Throwable) element).getLocalizedMessage();
                if (message == null) {
                    message = Classes.getShortClassName(element);
                }
                array[i] = message;
            } else if (element instanceof Class<?>) {
                array[i] = Classes.getShortName((Class<?>) element);
            }
        }
        return array;
    }

    /**
     * Gets a string for the given key and appends "..." to it.
     * This method is typically used for creating menu items.
     *
     * @param  key The key for the desired string.
     * @return The string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     */
    public final String getMenuLabel(final int key) throws MissingResourceException {
        return getString(key) + "...";
    }

    /**
     * Gets a string for the given key and appends ": " to it.
     * This method is typically used for creating labels.
     *
     * @param  key The key for the desired string.
     * @return The string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     */
    public final String getLabel(final int key) throws MissingResourceException {
        return getString(key) + ": ";
    }

    /**
     * Gets a string for the given key from this resource bundle or one of its parents.
     *
     * @param  key The key for the desired string.
     * @return The string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     */
    public final String getString(final int key) throws MissingResourceException {
        return getString(String.valueOf(key));
    }

    /**
     * Gets a string for the given key and formats it with the specified argument. The message is
     * formatted using {@link MessageFormat}. Calling this method is approximately equivalent to
     * calling:
     *
     * {@preformat java
     *     String pattern = getString(key);
     *     Format f = new MessageFormat(pattern);
     *     return f.format(arg0);
     * }
     *
     * If {@code arg0} is not already an array, it will be placed into an array of length 1. Using
     * {@link MessageFormat}, all occurrences of "{0}", "{1}", "{2}" in the resource string will be
     * replaced by {@code arg0[0]}, {@code arg0[1]}, {@code arg0[2]}, etc.
     *
     * @param  key The key for the desired string.
     * @param  arg0 A single object or an array of objects to be formatted and substituted.
     * @return The string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     *
     * @see #getString(String)
     * @see #getString(int,Object,Object)
     * @see #getString(int,Object,Object,Object)
     * @see MessageFormat
     */
    public final String getString(final int key, final Object arg0) throws MissingResourceException {
        final String pattern = getString(key);
        final Object[] arguments = toArray(arg0);
        synchronized (this) {
            if (format == null) {
                /*
                 * Constructs a new MessageFormat for formatting the arguments.
                 */
                format = new MessageFormat(pattern, getFormatLocale());
            } else if (key != lastKey) {
                /*
                 * Method MessageFormat.applyPattern(...) is costly! We will avoid
                 * calling it again if the format already has the right pattern.
                 */
                format.applyPattern(pattern);
                lastKey = key;
            }
            try {
                return format.format(arguments);
            } catch (RuntimeException e) {
                /*
                 * Safety against badly implemented toString() method
                 * in libraries that we don't control.
                 */
                return "[Unformattable message: " + e + ']';
            }
        }
    }

    /**
     * Gets a string for the given key and replaces all occurrences of "{0}",
     * "{1}", with values of {@code arg0}, {@code arg1}, etc.
     *
     * @param  key The key for the desired string.
     * @param  arg0 Value to substitute for "{0}".
     * @param  arg1 Value to substitute for "{1}".
     * @return The formatted string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     */
    public final String getString(final int    key,
                                  final Object arg0,
                                  final Object arg1) throws MissingResourceException
    {
        return getString(key, new Object[] {arg0, arg1});
    }

    /**
     * Gets a string for the given key and replaces all occurrences of "{0}",
     * "{1}", with values of {@code arg0}, {@code arg1}, etc.
     *
     * @param  key The key for the desired string.
     * @param  arg0 Value to substitute for "{0}".
     * @param  arg1 Value to substitute for "{1}".
     * @param  arg2 Value to substitute for "{2}".
     * @return The formatted string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     */
    public final String getString(final int    key,
                                  final Object arg0,
                                  final Object arg1,
                                  final Object arg2) throws MissingResourceException
    {
        return getString(key, new Object[] {arg0, arg1, arg2});
    }

    /**
     * Gets a string for the given key and replaces all occurrences of "{0}",
     * "{1}", with values of {@code arg0}, {@code arg1}, etc.
     *
     * @param  key The key for the desired string.
     * @param  arg0 Value to substitute for "{0}".
     * @param  arg1 Value to substitute for "{1}".
     * @param  arg2 Value to substitute for "{2}".
     * @param  arg3 Value to substitute for "{3}".
     * @return The formatted string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     */
    public final String getString(final int    key,
                                  final Object arg0,
                                  final Object arg1,
                                  final Object arg2,
                                  final Object arg3) throws MissingResourceException
    {
        return getString(key, new Object[] {arg0, arg1, arg2, arg3});
    }

    /**
     * Gets a string for the given key and replaces all occurrences of "{0}",
     * "{1}", with values of {@code arg0}, {@code arg1}, etc.
     *
     * @param  key The key for the desired string.
     * @param  arg0 Value to substitute for "{0}".
     * @param  arg1 Value to substitute for "{1}".
     * @param  arg2 Value to substitute for "{2}".
     * @param  arg3 Value to substitute for "{3}".
     * @param  arg4 Value to substitute for "{4}".
     * @return The formatted string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     */
    public final String getString(final int    key,
                                  final Object arg0,
                                  final Object arg1,
                                  final Object arg2,
                                  final Object arg3,
                                  final Object arg4) throws MissingResourceException
    {
        return getString(key, new Object[] {arg0, arg1, arg2, arg3, arg4});
    }

    /**
     * Gets a localized log record.
     *
     * @param  level The log record level.
     * @param  key   The resource key.
     * @return The log record.
     */
    public LogRecord getLogRecord(final Level level, final int key) {
        return getLogRecord(level, key, null);
    }

    /**
     * Gets a localized log record.
     *
     * @param  level The log record level.
     * @param  key   The resource key.
     * @param  arg0  The parameter for the log message, or {@code null}.
     * @return The log record.
     */
    public LogRecord getLogRecord(final Level level, final int key,
                                  final Object arg0)
    {
        if (immediateLocalization) {
            return new LogRecord(level, getString(key, arg0));
        }
        final LogRecord record = new LogRecord(level, String.valueOf(key));
        record.setResourceBundle(this);
        if (arg0 != null) {
            record.setParameters(toArray(arg0));
        }
        return record;
    }

    /**
     * Gets a localized log record.
     *
     * @param  level The log record level.
     * @param  key   The resource key.
     * @param  arg0  The first parameter.
     * @param  arg1  The second parameter.
     * @return The log record.
     */
    public LogRecord getLogRecord(final Level level, final int key,
                                  final Object arg0,
                                  final Object arg1)
    {
        return getLogRecord(level, key, new Object[] {arg0, arg1});
    }

    /**
     * Gets a localized log record.
     *
     * @param  level The log record level.
     * @param  key   The resource key.
     * @param  arg0  The first parameter.
     * @param  arg1  The second parameter.
     * @param  arg2  The third parameter.
     * @return The log record.
     */
    public LogRecord getLogRecord(final Level level, final int key,
                                  final Object arg0,
                                  final Object arg1,
                                  final Object arg2)
    {
        return getLogRecord(level, key, new Object[] {arg0, arg1, arg2});
    }

    /**
     * Gets a localized log record.
     *
     * @param  level The log record level.
     * @param  key   The resource key.
     * @param  arg0  The first parameter.
     * @param  arg1  The second parameter.
     * @param  arg2  The third parameter.
     * @param  arg3  The fourth parameter.
     * @return The log record.
     */
    public LogRecord getLogRecord(final Level level, final int key,
                                  final Object arg0,
                                  final Object arg1,
                                  final Object arg2,
                                  final Object arg3)
    {
        return getLogRecord(level, key, new Object[] {arg0, arg1, arg2, arg3});
    }

    /**
     * Localizes and formats the message string from a log record. This method performs a work
     * similar to {@link java.util.logging.Formatter#formatMessage}, except that the work will be
     * delegated to {@link #getString(int, Object)} if the {@linkplain LogRecord#getResourceBundle
     * record resource bundle} is an instance of {@code IndexedResourceBundle}.
     *
     * @param  record The log record to format.
     * @return The formatted message.
     */
    public static String format(final LogRecord record) {
        String message = record.getMessage();
        final ResourceBundle resources = record.getResourceBundle();
        if (resources instanceof IndexedResourceBundle) {
            int key = -1;
            try {
                key = Integer.parseInt(message);
            } catch (NumberFormatException e) {
                 unexpectedException(e);
            }
            if (key >= 0) {
                final Object[] parameters = record.getParameters();
                return ((IndexedResourceBundle) resources).getString(key, parameters);
            }
        }
        if (resources != null) {
            try {
                message = resources.getString(message);
            } catch (MissingResourceException e) {
                unexpectedException(e);
            }
            final Object[] parameters = record.getParameters();
            if (parameters != null && parameters.length != 0) {
                final int offset = message.indexOf('{');
                if (offset >= 0 && offset < message.length()-1) {
                    // Uses a more restrictive check than Character.isDigit(char)
                    final char c = message.charAt(offset);
                    if (c>='0' && c<='9') try {
                        return MessageFormat.format(message, parameters);
                    } catch (IllegalArgumentException e) {
                        unexpectedException(e);
                    }
                }
            }
        }
        return message;
    }

    /**
     * Invoked when an unexpected exception occurred in the {@link #format} method.
     */
    private static void unexpectedException(final RuntimeException exception) {
        Logging.unexpectedException(IndexedResourceBundle.class, "format", exception);
    }

    /**
     * Returns a string representation of this object.
     * This method is for debugging purposes only.
     */
    @Override
    public synchronized String toString() {
        return Classes.getShortClassName(this) + '[' + getLocale() + ']';
    }
}

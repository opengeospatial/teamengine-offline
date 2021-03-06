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

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import org.opengis.util.InternationalString;
import org.geotoolkit.util.ResourceInternationalString;


/**
 * Locale-dependent resources for long descriptions.
 *
 * @author Martin Desruisseaux (IRD)
 * @version 3.05
 *
 * @since 2.2
 * @module
 */
public final class Descriptions extends IndexedResourceBundle {
    /**
     * Resource keys. This class is used when compiling sources, but no dependencies to
     * {@code Keys} should appear in any resulting class files. Since the Java compiler
     * inlines final integer values, using long identifiers will not bloat the constant
     * pools of compiled classes.
     *
     * @author Martin Desruisseaux (IRD)
     * @version 3.00
     *
     * @since 2.2
     */
    public static final class Keys {
        private Keys() {
        }

        /**
         * Data distributed over a grid
         */
        public static final int CODEC_GRID = 0;

        /**
         * Matrix in text file
         */
        public static final int CODEC_MATRIX = 1;

        /**
         * Raw binary file
         */
        public static final int CODEC_RAW = 2;

        /**
         * Usage: {0} [OPTION...] [COMMAND] [PARAMETER...]
         */
        public static final int COMMAND_USAGE_$1 = 3;

        /**
         * Concatenation of {0} adapted to the 3D domain
         */
        public static final int CONCATENATED_OPERATION_ADAPTED_$1 = 15;

        /**
         * This result indicates if a datum shift method has been applied.
         */
        public static final int CONFORMANCE_MEANS_DATUM_SHIFT = 4;

        /**
         * This result indicates if the factory “{0}” is available for use.
         */
        public static final int CONFORMANCE_MEANS_FACTORY_AVAILABLE_$1 = 5;

        /**
         * This result indicates if the parameters are valid.
         */
        public static final int CONFORMANCE_MEANS_VALID_PARAMETERS = 6;

        /**
         * Are the {0} data installed? Some optional data can be downloaded and installed by running
         * the “{2}” module. The default directory for {0} data is “{1}”, but {2} allows to change this
         * setting.
         */
        public static final int DATA_NOT_INSTALLED_$3 = 7;

        /**
         * {0} files have been read successfully but {1} files can not be read. The failure causes are
         * reported below.
         */
        public static final int ERROR_READING_SOME_FILES_$2 = 8;

        /**
         * Inserted {0} rows in {1} seconds.
         */
        public static final int INSERTED_ROWS_$2 = 9;

        /**
         * Parameter “{0}” is not conform. {1}
         */
        public static final int NON_CONFORM_PARAMETER_$2 = 10;

        /**
         * Do not use a valuable password, since it will not be encrypted.
         */
        public static final int PASSWORD_NOT_ENCRYPTED = 11;

        /**
         * Count: {0}
         * Minimum: {1}
         * Maximum: {2}
         * Mean: {3}
         * RMS: {4}
         * Standard deviation: {5}
         */
        public static final int STATISTICS_TO_STRING_$6 = 12;

        /**
         * Use {0,choice,0#the embedded|1#a specific} database.
         */
        public static final int USE_EOS_DATABASE_$1 = 13;

        /**
         * Use "help" to show available commands.
         */
        public static final int USE_HELP_COMMAND = 14;
    }

    /**
     * Constructs a new resource bundle loading data from the given UTF file.
     *
     * @param filename The file or the JAR entry containing resources.
     */
    Descriptions(final String filename) {
        super(filename);
    }

    /**
     * Returns resources in the given locale.
     *
     * @param  locale The locale, or {@code null} for the default locale.
     * @return Resources in the given locale.
     * @throws MissingResourceException if resources can't be found.
     */
    public static Descriptions getResources(Locale locale) throws MissingResourceException {
        return getBundle(Descriptions.class, locale);
    }

    /**
     * The international string to be returned by {@link formatInternational}.
     */
    private static final class International extends ResourceInternationalString {
        private static final long serialVersionUID = -2152214649387449859L;

        International(final int key) {
            super(Descriptions.class.getName(), String.valueOf(key));
        }

        @Override
        protected ResourceBundle getBundle(final Locale locale) {
            return getResources(locale);
        }
    }

    /**
     * Gets an international string for the given key. This method does not check for the key
     * validity. If the key is invalid, then a {@link MissingResourceException} may be thrown
     * when a {@link InternationalString#toString} method is invoked.
     *
     * @param  key The key for the desired string.
     * @return An international string for the given key.
     *
     * @since 3.03
     */
    public static InternationalString formatInternational(final int key) {
        return new International(key);
    }

    /**
     * Gets an international string for the given key. This method does not check for the key
     * validity. If the key is invalid, then a {@link MissingResourceException} may be thrown
     * when a {@link InternationalString#toString} method is invoked.
     *
     * {@note This method is redundant with the one expecting <code>Object...</code>, but avoid
     *        the creation of a temporary array. There is no risk of confusion since the two
     *        methods delegate their work to the same <code>format</code> method anyway.}
     *
     * @param  key The key for the desired string.
     * @param  arg Values to substitute to "{0}".
     * @return An international string for the given key.
     *
     * @since 3.05
     *
     * @todo Current implementation just invokes {@link #format}. Need to format only when
     *       {@code toString(Locale)} is invoked.
     */
    public static InternationalString formatInternational(final int key, final Object arg) {
        return new org.geotoolkit.util.SimpleInternationalString(format(key, arg));
    }

    /**
     * Gets an international string for the given key. This method does not check for the key
     * validity. If the key is invalid, then a {@link MissingResourceException} may be thrown
     * when a {@link InternationalString#toString} method is invoked.
     *
     * @param  key The key for the desired string.
     * @param  args Values to substitute to "{0}", "{1}", <i>etc</i>.
     * @return An international string for the given key.
     *
     * @since 3.03
     *
     * @todo Current implementation just invokes {@link #format}. Need to format only when
     *       {@code toString(Locale)} is invoked.
     */
    public static InternationalString formatInternational(final int key, final Object... args) {
        return new org.geotoolkit.util.SimpleInternationalString(format(key, args));
    }

    /**
     * Gets a string for the given key from this resource bundle or one of its parents.
     *
     * @param  key The key for the desired string.
     * @return The string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     */
    public static String format(final int key) throws MissingResourceException {
        return getResources(null).getString(key);
    }

    /**
     * Gets a string for the given key are replace all occurrence of "{0}"
     * with values of {@code arg0}.
     *
     * @param  key The key for the desired string.
     * @param  arg0 Value to substitute to "{0}".
     * @return The formatted string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     */
    public static String format(final int     key,
                                final Object arg0) throws MissingResourceException
    {
        return getResources(null).getString(key, arg0);
    }

    /**
     * Gets a string for the given key are replace all occurrence of "{0}",
     * "{1}", with values of {@code arg0}, {@code arg1}.
     *
     * @param  key The key for the desired string.
     * @param  arg0 Value to substitute to "{0}".
     * @param  arg1 Value to substitute to "{1}".
     * @return The formatted string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     */
    public static String format(final int     key,
                                final Object arg0,
                                final Object arg1) throws MissingResourceException
    {
        return getResources(null).getString(key, arg0, arg1);
    }

    /**
     * Gets a string for the given key are replace all occurrence of "{0}",
     * "{1}", with values of {@code arg0}, {@code arg1}, etc.
     *
     * @param  key The key for the desired string.
     * @param  arg0 Value to substitute to "{0}".
     * @param  arg1 Value to substitute to "{1}".
     * @param  arg2 Value to substitute to "{2}".
     * @return The formatted string for the given key.
     * @throws MissingResourceException If no object for the given key can be found.
     */
    public static String format(final int     key,
                                final Object arg0,
                                final Object arg1,
                                final Object arg2) throws MissingResourceException
    {
        return getResources(null).getString(key, arg0, arg1, arg2);
    }
}

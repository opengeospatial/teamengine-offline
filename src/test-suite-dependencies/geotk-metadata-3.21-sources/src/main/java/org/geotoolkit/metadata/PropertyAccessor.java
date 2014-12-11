/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2007-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotoolkit.metadata;

import java.util.Set;
import java.util.Map;
import java.util.Arrays;
import java.util.Locale;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import net.jcip.annotations.ThreadSafe;

import org.opengis.annotation.UML;

import org.geotoolkit.resources.Errors;
import org.geotoolkit.util.XArrays;
import org.geotoolkit.util.Strings;
import org.geotoolkit.util.Utilities;
import org.geotoolkit.util.ComparisonMode;
import org.geotoolkit.util.collection.XCollections;
import org.geotoolkit.util.collection.CheckedContainer;
import org.geotoolkit.util.converter.Classes;
import org.geotoolkit.util.converter.Numbers;
import org.geotoolkit.util.converter.ObjectConverter;
import org.geotoolkit.util.converter.ConverterRegistry;
import org.geotoolkit.util.converter.NonconvertibleObjectException;
import org.geotoolkit.xml.IdentifiedObject;

import static org.geotoolkit.internal.InternalUtilities.floatEpsilonEqual;


/**
 * The getter methods declared in a GeoAPI interface, together with setter methods (if any)
 * declared in the Geotk implementation. An instance of {@code PropertyAccessor} gives access
 * to all public attributes of an instance of a metadata object. It uses reflection for this
 * purpose, a little bit like the <cite>Java Beans</cite> framework.
 * <p>
 * This accessor groups the properties in two categories:
 * <p>
 * <ul>
 *   <li>The standard properties defined by the GeoAPI (or other standard) interfaces.
 *       Those properties are the only one accessible by most methods in this class,
 *       except {@link #shallowEquals}, {@link #shallowCopy} and {@link #freeze}.</li>
 *
 *   <li>Extra properties defined by the {@link IdentifiedObject} interface. Those properties
 *       invisible in the ISO 19115 model, but appears in ISO 19139 XML marshalling. So we do
 *       the same in the Geotk implementation: invisible in map and tree view, but visible in
 *       XML marshalling.</li>
 * </ul>
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.20
 *
 * @since 2.4
 * @module
 */
@ThreadSafe
final class PropertyAccessor {
    /**
     * The locale to use for changing the case of characters.
     */
    private static final Locale LOCALE = Locale.US;

    /**
     * The prefix for getters on boolean values.
     */
    private static final String IS = "is";

    /**
     * The prefix for getters (general case).
     */
    private static final String GET = "get";

    /**
     * The prefix for setters.
     */
    private static final String SET = "set";

    /**
     * Getters shared between many instances of this class. Two different implementations
     * may share the same getters but different setters.
     */
    private static final Map<Class<?>, Method[]> SHARED_GETTERS = new HashMap<Class<?>, Method[]>();

    /**
     * Additional getter to declare in every list of getter methods that do not already provide
     * their own {@code getIdentifiers()} method. We handle this method specially because it is
     * needed for XML marshalling in ISO 19139 compliant document, while not part of abstract
     * ISO 19115 specification.
     *
     * @see IdentifiedObject#getIdentifiers()
     *
     * @since 3.19
     */
    private static final Method EXTRA_GETTER;
    static {
        try {
            EXTRA_GETTER = IdentifiedObject.class.getMethod("getIdentifiers", (Class<?>[]) null);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e); // Should never happen.
        }
    }

    /**
     * The implemented metadata interface.
     */
    final Class<?> type;

    /**
     * The implementation class. The following condition must hold:
     *
     * {@preformat java
     *     type.isAssignableFrom(implementation);
     * }
     */
    final Class<?> implementation;

    /**
     * Number of {@link #getters} methods to use. This is either {@code getters.length}
     * or {@code getters.length-1}, depending whatever the {@link #EXTRA_GETTER} method
     * needs to be skipped or not.
     *
     * @since 3.19
     */
    private final int standardCount, allCount;

    /**
     * The getter methods. This array should not contain any null element.
     * They are the methods defined in the interface, not the implementation class.
     */
    private final Method[] getters;

    /**
     * The corresponding setter methods, or {@code null} if none. This array must have
     * the same length than {@link #getters}. For every {@code getters[i]} element,
     * {@code setters[i]} is the corresponding setter or {@code null} if there is none.
     */
    private final Method[] setters;

    /**
     * The JavaBeans property names. They are computed at construction time,
     * {@linkplain String#intern() interned} then cached. Those names are often
     * the same than field names (at least in Geotk implementation), so it is
     * reasonable to intern them in order to share {@code String} instances.
     */
    private final String[] names;

    /**
     * The types of elements for the corresponding getter and setter methods. If a getter
     * method returns a collection, then this is the type of elements in that collection.
     * Otherwise this is the type of the returned value itself.
     * <p>
     * Primitive types like {@code double} or {@code int} are converted to their wrapper types.
     * <p>
     * This array may contain null values if the type of elements in a collection is unknown
     * (i.e. the collection is not parameterized).
     */
    private final Class<?>[] elementTypes;

    /**
     * Index of getter or setter for a given name. Original names are duplicated with the same name
     * converted to lower cases according {@link #LOCALE} conventions, for case-insensitive searches.
     * This map must be considered as immutable after construction.
     * <p>
     * The keys in this map are both inferred from the method names and fetched from the UML
     * annotations. Consequently the map may contains many entries for the same value if some
     * method names are different than the UML identifiers.
     */
    private final Map<String,Integer> mapping;

    /**
     * The last converter used. This is remembered on the assumption that the same converter
     * will often be reused for the same property. This optimization can reduce the cost of
     * looking for a converter, and also reduce thread contention since it reduce the number
     * of calls to the synchronized {@link ConverterRegistry#converter} method.
     */
    private transient volatile ObjectConverter<?,?> converter;

    /**
     * The restrictions that apply on property values. The array will be created when first
     * needed. A {@link ValueRestriction#PENDING} element means that the restriction at that
     * index has not yet been computed. If a property has been determined to have no
     * restriction, then its corresponding element in this array is set to {@code null}.
     */
    private transient ValueRestriction[] restrictions;

    /**
     * Creates a new property accessor for the specified metadata implementation.
     *
     * @param  implementation The type of metadata implementations to wrap.
     * @param  type The interface implemented by the metadata.
     *         Shall be the value returned by {@link #getStandardType}.
     */
    PropertyAccessor(final Class<?> implementation, final Class<?> type) {
        this.implementation = implementation;
        this.type = type;
        assert type.isAssignableFrom(implementation) : implementation;
        getters = getGetters(type);
        int allCount = getters.length;
        int standardCount = allCount;
        if (allCount != 0 && getters[allCount-1] == EXTRA_GETTER) {
            if (!EXTRA_GETTER.getDeclaringClass().isAssignableFrom(implementation)) {
                allCount--; // The extra getter method does not exist.
            }
            standardCount--;
        }
        this.allCount = allCount;
        this.standardCount = standardCount;
        /*
         * Compute all information derived from getters: setters, property names, value types.
         */
        mapping = new HashMap<String,Integer>(XCollections.hashMapCapacity(allCount));
        names   = new String[allCount];
        elementTypes = new Class<?>[allCount];
        Method[] setters = null;
        final Class<?>[] arguments = new Class<?>[1];
        for (int i=0; i<allCount; i++) {
            /*
             * Fetch the getter and remind its name. We do the same for
             * the UML tag attached to the getter, if any.
             */
            final Integer index = i;
            Method getter  = getters[i];
            String name    = getter.getName();
            final int base = prefix(name).length();
            addMapping(names[i] = toPropertyName(name, base), index);
            addMapping(name, index);
            final UML annotation = getter.getAnnotation(UML.class);
            if (annotation != null) {
                addMapping(annotation.identifier(), index);
            }
            /*
             * Now try to infer the setter from the getter. We replace the "get" prefix by
             * "set" and look for a parameter of the same type than the getter return type.
             */
            Class<?> returnType = getter.getReturnType();
            arguments[0] = returnType;
            if (name.length() > base) {
                final char lo = name.charAt(base);
                final char up = Character.toUpperCase(lo);
                final int length = name.length();
                final StringBuilder buffer = new StringBuilder(length - base + 3).append(SET);
                if (lo != up) {
                    buffer.append(up).append(name, base+1, length);
                } else {
                    buffer.append(name, base, length);
                }
                name = buffer.toString();
            }
            /*
             * Note: we want PUBLIC methods only.  For example the referencing module defines
             * setters as private methods for use by JAXB only. We don't want to allow access
             * to those setters.
             */
            Method setter = null;
            try {
                setter = implementation.getMethod(name, arguments);
            } catch (NoSuchMethodException e) {
                /*
                 * If we found no setter method expecting an argument of the same type than the
                 * argument returned by the GeoAPI method,  try again with the type returned by
                 * the implementation class. It is typically the same type, but sometime it may
                 * be a subtype.
                 *
                 * It is a necessary condition that the type returned by the getter is assignable
                 * to the type expected by the setter.  This contract is required by the 'freeze'
                 * method among others.
                 */
                try {
                    getter = implementation.getMethod(getter.getName(), (Class<?>[]) null);
                } catch (NoSuchMethodException error) {
                    // Should never happen, since the implementation class
                    // implements the interface where the getter come from.
                    throw new AssertionError(error);
                }
                if (returnType != (returnType = getter.getReturnType())) {
                    arguments[0] = returnType;
                    try {
                        setter = implementation.getMethod(name, arguments);
                    } catch (NoSuchMethodException ignore) {
                        // There is no setter, which may be normal. At this stage
                        // the 'setter' variable should still have the null value.
                    }
                }
            }
            if (setter != null) {
                if (setters == null) {
                    setters = new Method[allCount];
                }
                setters[i] = setter;
            }
            /*
             * Get the type of elements returned by the getter. We perform this step last because
             * the search for a setter above may have replaced the getter declared in the interface
             * by the getter declared in the implementation with a covariant return type. Our intend
             * is to get a type which can be accepted by the setter.
             */
            Class<?> elementType = getter.getReturnType();
            if (Collection.class.isAssignableFrom(elementType)) {
                elementType = Classes.boundOfParameterizedAttribute(getter);
            }
            elementTypes[i] = Numbers.primitiveToWrapper(elementType);
        }
        this.setters = setters;
    }

    /**
     * Adds the given (name, index) pair to {@link #mapping}, making sure we don't
     * overwrite an existing entry with different value.
     */
    private void addMapping(String name, final Integer index) throws IllegalArgumentException {
        if (!name.isEmpty()) {
            String original;
            do {
                final Integer old = mapping.put(name, index);
                if (old != null && !old.equals(index)) {
                    throw new IllegalStateException(Errors.format(Errors.Keys.DUPLICATED_VALUE_$1,
                            Classes.getShortName(type) + '.' + name));
                }
                original = name;
                name = name.toLowerCase(LOCALE).trim();
            } while (!name.equals(original));
        }
    }

    /**
     * Returns the metadata interface implemented by the specified implementation type.
     * Only one metadata interface can be implemented. If the given type is already an
     * interface from the standard, it is returned directly.
     *
     * @param  type The type of the implementation (could also be the interface type).
     * @param  interfacePackage The root package for metadata interfaces.
     * @return The single interface, or {@code null} if none where found.
     */
    static Class<?> getStandardType(Class<?> type, final String interfacePackage) {
        if (type != null) {
            if (type.isInterface()) {
                if (type.getName().startsWith(interfacePackage)) {
                    return type;
                }
            } else {
                /*
                 * Gets every interfaces from the supplied package in declaration order,
                 * including the ones declared in the super-class.
                 */
                final Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
                do {
                    getInterfaces(type, interfacePackage, interfaces);
                    type = type.getSuperclass();
                } while (type != null);
                /*
                 * If we found more than one interface, removes the
                 * ones that are sub-interfaces of the other.
                 */
                for (final Iterator<Class<?>> it=interfaces.iterator(); it.hasNext();) {
                    final Class<?> candidate = it.next();
                    for (final Class<?> child : interfaces) {
                        if (candidate != child && candidate.isAssignableFrom(child)) {
                            it.remove();
                            break;
                        }
                    }
                }
                final Iterator<Class<?>> it=interfaces.iterator();
                if (it.hasNext()) {
                    final Class<?> candidate = it.next();
                    if (!it.hasNext()) {
                        return candidate;
                    }
                    // Found more than one interface; we don't know which one to pick.
                    // Returns 'null' for now; the caller will thrown an exception.
                }
            }
        }
        return null;
    }

    /**
     * Puts every interfaces for the given type in the specified collection.
     * This method invokes itself recursively for scanning parent interfaces.
     */
    private static void getInterfaces(final Class<?> type, final String interfacePackage,
            final Collection<Class<?>> interfaces)
    {
        for (final Class<?> candidate : type.getInterfaces()) {
            if (candidate.getName().startsWith(interfacePackage)) {
                interfaces.add(candidate);
            }
            getInterfaces(candidate, interfacePackage, interfaces);
        }
    }

    /**
     * Returns the getters. The returned array should never be modified,
     * since it may be shared among many instances of {@code PropertyAccessor}.
     *
     * @param  type The metadata interface.
     * @return The getters declared in the given interface (never {@code null}).
     */
    private static Method[] getGetters(final Class<?> type) {
        synchronized (SHARED_GETTERS) {
            Method[] getters = SHARED_GETTERS.get(type);
            if (getters == null) {
                getters = type.getMethods();
                // Following is similar in purpose to the PropertyAccessor.mapping field,
                // but index values are different because of the call to Arrays.sort(...).
                final Map<String,Integer> mapping = new HashMap<String,Integer>(XCollections.hashMapCapacity(getters.length));
                boolean hasExtraGetter = false;
                int count = 0;
                for (final Method candidate : getters) {
                    if (Classes.isPossibleGetter(candidate)) {
                        final String name = candidate.getName();
                        if (name.startsWith(SET)) { // Paranoiac check.
                            continue;
                        }
                        /*
                         * At this point, we are ready to accept the method. Before doing so,
                         * check if the method override an other method defined in a parent
                         * class with a covariant return type. The JVM considers such cases
                         * as two different methods, while from a Java developer point of
                         * view this is the same method (GEOTK-205).
                         */
                        final Integer pi = mapping.put(name, count);
                        if (pi != null) {
                            final Class<?> pt = getters[pi].getReturnType();
                            final Class<?> ct = candidate  .getReturnType();
                            if (ct.isAssignableFrom(pt)) {
                                continue; // Previous type was more accurate.
                            }
                            if (pt.isAssignableFrom(ct)) {
                                getters[pi] = candidate;
                                continue;
                            }
                            throw new ClassCastException(Errors.format(Errors.Keys.ILLEGAL_CLASS_$3,
                                    Classes.getShortName(type) + '.' + name, pt, ct));
                        }
                        getters[count++] = candidate;
                        if (!hasExtraGetter) {
                            hasExtraGetter = name.equals(EXTRA_GETTER.getName());
                        }
                    }
                }
                /*
                 * Sort the standard methods before to add the extra methods (if any) in order to
                 * keep the extra methods last. The code checking for the extra methods require
                 * them to be last.
                 */
                Arrays.sort(getters, 0, count, PropertyComparator.INSTANCE);
                if (!hasExtraGetter) {
                    if (getters.length == count) {
                        getters = Arrays.copyOf(getters, count+1);
                    }
                    getters[count++] = EXTRA_GETTER;
                }
                getters = XArrays.resize(getters, count);
                SHARED_GETTERS.put(type, getters);
            }
            return getters;
        }
    }

    /**
     * Returns the prefix of the specified method name. If the method name don't starts with
     * a prefix (for example {@link org.opengis.metadata.quality.ConformanceResult#pass()}),
     * then this method returns an empty string.
     */
    private static String prefix(final String name) {
        if (name.startsWith(GET)) {
            return GET;
        }
        if (name.startsWith(IS)) {
            return IS;
        }
        if (name.startsWith(SET)) {
            return SET;
        }
        return "";
    }

    /**
     * Returns {@code true} if the specified string starting at the specified index contains
     * no lower case characters. The characters don't have to be in upper case however (e.g.
     * non-alphabetic characters)
     */
    private static boolean isAcronym(final String name, int offset) {
        final int length = name.length();
        while (offset < length) {
            if (Character.isLowerCase(name.charAt(offset++))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes the {@code "get"} or {@code "is"} prefix and turn the first character after the
     * prefix into lower case. For example the method name {@code "getTitle"} will be replaced
     * by the property name {@code "title"}. We will performs this operation only if there is
     * at least 1 character after the prefix.
     *
     * @param  name The method name.
     * @param  base Must be the result of {@code prefix(name).length()}.
     * @return The property name.
     */
    private static String toPropertyName(String name, final int base) {
        final int length = name.length();
        if (length > base) {
            if (isAcronym(name, base)) {
                name = name.substring(base);
            } else {
                final char up = name.charAt(base);
                final char lo = Character.toLowerCase(up);
                if (up != lo) {
                    name = new StringBuilder(length - base).append(lo).append(name, base+1, length).toString();
                } else {
                    name = name.substring(base);
                }
            }
        }
        return name.trim().intern();
    }

    /**
     * Returns the number of properties that can be read.
     */
    final int count() {
        return standardCount;
    }

    /**
     * Returns the index of the specified property, or -1 if none.
     * The search is case-insensitive.
     *
     * @param  key The property to search.
     * @return The index of the given key, or -1 if none.
     */
    final int indexOf(final String name) {
        Integer index = mapping.get(name);
        if (index == null) {
            /*
             * Make a second try with lower cases only if the first try failed, because
             * most of the time the key name will have exactly the expected case and using
             * directly the given String instance allow usage of its cached hash code value.
             */
            final String key = name.replace(" ", "").toLowerCase(LOCALE).trim();
            if (key == name || (index = mapping.get(key)) == null) { // NOSONAR: identity comparison is okay here.
                return -1;
            }
        }
        return index;
    }

    /**
     * Always returns the index of the specified property (never -1).
     * The search is case-insensitive.
     *
     * @param  key The property to search.
     * @return The index of the given key.
     * @throws IllegalArgumentException if the given key is not found.
     */
    final int requiredIndexOf(final String key) throws IllegalArgumentException {
        final int index = indexOf(key);
        if (index >= 0) {
            return index;
        }
        throw new IllegalArgumentException(Errors.format(Errors.Keys.NO_SUCH_ATTRIBUTE_$2, key, type));
    }

    /**
     * Returns the declaring class of the getter at the given index.
     *
     * @param  index The index of the property for which to get the declaring class.
     * @return The declaring class at the given index, or {@code null} if the index is out of bounds.
     *
     * @since 3.05
     */
    final Class<?> getDeclaringClass(final int index) {
        if (index >= 0 && index < names.length) {
            return getters[index].getDeclaringClass();
        }
        return null;
    }

    /**
     * Returns the name of the property at the given index, or {@code null} if none.
     *
     * @param  index The index of the property for which to get the name.
     * @param  keyName The kind of name to return.
     * @return The name of the given kind at the given index, or {@code null} if the
     *         index is out of bounds.
     */
    @SuppressWarnings("fallthrough")
    final String name(final int index, final KeyNamePolicy keyName) {
        if (index >= 0 && index < names.length) {
            switch (keyName) {
                case UML_IDENTIFIER: {
                    final UML uml = getters[index].getAnnotation(UML.class);
                    if (uml != null) {
                        return uml.identifier();
                    }
                    // Fallthrough
                }
                case JAVABEANS_PROPERTY: {
                    return names[index];
                }
                case METHOD_NAME: {
                    return getters[index].getName();
                }
                case SENTENCE: {
                    return Strings.camelCaseToSentence(names[index]);
                }
            }
        }
        return null;
    }

    /**
     * Returns the type of the property at the given index. The returned type is usually
     * a GeoAPI interface (at least in the case of Geotk implementation).
     * <p>
     * If the given policy is {@code ELEMENT_TYPE}, then:
     * <ul>
     *   <li>primitive types like {@code double} or {@code int} are converted to their wrapper types;</li>
     *   <li>if the property is a collection, then returns the type of collection elements.</li>
     * </ul>
     *
     * @param  index The index of the property.
     * @param  policy The kind of type to return.
     * @return The type of property values, or {@code null} if unknown.
     */
    final Class<?> type(final int index, final TypeValuePolicy policy) {
        if (index >= 0 && index < standardCount) {
            switch (policy) {
                case ELEMENT_TYPE: {
                    return elementTypes[index];
                }
                case PROPERTY_TYPE: {
                    return getters[index].getReturnType();
                }
                case DECLARING_INTERFACE: {
                    return getters[index].getDeclaringClass();
                }
                case DECLARING_CLASS: {
                    Method getter = getters[index];
                    if (implementation != type) try {
                        getter = implementation.getMethod(getter.getName(), (Class<?>[]) null);
                    } catch (NoSuchMethodException error) {
                        // Should never happen, since the implementation class
                        // implements the interface where the getter come from.
                        throw new AssertionError(error);
                    }
                    return getter.getDeclaringClass();
                }
            }
        }
        return null;
    }

    /**
     * Returns the restriction for the property at the given index, or {@code null} if none.
     * The restriction, if any, typically contains a {@code NumberRange} object. More types
     * may be added in future versions.
     */
    final synchronized ValueRestriction restriction(final int index) {
        ValueRestriction[] restrictions = this.restrictions;
        if (restrictions == null) {
            this.restrictions = restrictions = new ValueRestriction[standardCount];
            Arrays.fill(restrictions, ValueRestriction.PENDING);
        }
        if (index < 0 || index >= restrictions.length) {
            return null;
        }
        ValueRestriction restriction = restrictions[index];
        if (restriction == ValueRestriction.PENDING) {
            final Method impl, getter=getters[index];
            if (implementation == type) {
                impl = getter;
            } else try {
                impl = implementation.getMethod(getter.getName(), (Class<?>[]) null);
            } catch (NoSuchMethodException error) {
                // Should never happen, since the implementation class
                // implements the interface where the getter come from.
                throw new AssertionError(error);
            }
            restriction = ValueRestriction.create(elementTypes[index], getter, impl);
            restrictions[index] = restriction;
        }
        return restriction;
    }

    /**
     * Returns {@code true} if the property at the given index is writable.
     */
    final boolean isWritable(final int index) {
        return (index >= 0) && (index < standardCount) && (setters != null) && (setters[index] != null);
    }

    /**
     * Returns the value for the specified metadata, or {@code null} if none.
     */
    final Object get(final int index, final Object metadata) {
        return (index >= 0 && index < standardCount) ? get(getters[index], metadata) : null;
    }

    /**
     * Gets a value from the specified metadata. We do not expect any checked exception to
     * be thrown, since {@code org.opengis.metadata} do not declare any.
     *
     * @param method The method to use for the query.
     * @param metadata The metadata object to query.
     */
    private static Object get(final Method method, final Object metadata) {
        assert (method.getReturnType() != Void.TYPE) : method;
        try {
            return method.invoke(metadata, (Object[]) null);
        } catch (IllegalAccessException e) {
            // Should never happen since 'getters' should contains only public methods.
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            final Throwable cause = e.getTargetException();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new UndeclaredThrowableException(cause);
        }
    }

    /**
     * Sets a value for the specified metadata and returns the old value if {@code getOld} is
     * {@code true}. If the old value was a collection or a map, then this value is copied in
     * a new collection or map before the new value is set, because the setter methods typically
     * copy the new collection in their existing instance.
     *
     * @param  index The index of the property to set.
     * @param  metadata The metadata object on which to set the value.
     * @param  value The new value.
     * @param  getOld {@code true} if this method should first fetches the old value.
     * @return The old value, or {@code null} if {@code getOld} was {@code false}.
     * @throws UnsupportedOperationException if the attribute for the given key is read-only.
     * @throws ClassCastException if the given value is not of the expected type.
     */
    final Object set(final int index, final Object metadata, final Object value, final boolean getOld)
            throws UnsupportedOperationException, ClassCastException
    {
        if (index >= 0 && index < standardCount && setters != null) {
            final Method getter = getters[index];
            final Method setter = setters[index];
            if (setter != null) {
                Object old;
                if (getOld) {
                    old = get(getter, metadata);
                    if (old instanceof Collection<?>) {
                        old = XCollections.copy((Collection<?>) old);
                    } else if (old instanceof Map<?,?>) {
                        old = XCollections.copy((Map<?,?>) old);
                    }
                } else {
                    old = null;
                }
                final Object[] newValues = new Object[] {value};
                converter = convert(getter, metadata, newValues, elementTypes[index], converter);
                set(setter, metadata, newValues);
                return old;
            }
        }
        throw new UnsupportedOperationException(Errors.format(Errors.Keys.CANT_SET_ATTRIBUTE_VALUE_$1, names[index]));
    }

    /**
     * Sets a value for the specified metadata. This method does not attempt any conversion of
     * argument values. Conversion of type, if needed, must have been applied before to call
     * this method.
     * <p>
     * The call to the setter method should not thrown any checked exception.
     * However unchecked exceptions are allowed.
     *
     * @param setter    The method to use for setting the new value.
     * @param metadata  The metadata object to query.
     * @param newValues The argument to give to the method to be invoked.
     */
    private static void set(final Method setter, final Object metadata, final Object[] newValues) {
        try {
            setter.invoke(metadata, newValues);
        } catch (IllegalAccessException e) {
            // Should never happen since 'setters' should contains only public methods.
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            final Throwable cause = e.getTargetException();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new UndeclaredThrowableException(cause);
        }
    }

    /**
     * Converts a value to the type required by a setter method.
     *
     * @param getter
     *          The method to use for fetching the previous value.
     * @param metadata
     *          The metadata object to query.
     * @param newValues
     *          The argument to convert. It must be an array of length 1.
     *          The content of this array will be modified in-place.
     * @param elementType
     *          The type required by the setter method.
     * @param converter
     *          The last converter used, or {@code null} if none. This converter is provided only
     *          as a hint and doesn't need to be accurate.
     * @return
     *          The last converter used, or {@code null}.
     * @throws ClassCastException
     *          if the element of the {@code arguments} array is not of the expected type.
     */
    private static ObjectConverter<?,?> convert(final Method getter, final Object metadata,
            final Object[] newValues, Class<?> elementType, ObjectConverter<?,?> converter)
            throws ClassCastException
    {
        assert newValues.length == 1;
        Object newValue = newValues[0];
        if (newValue == null) {
            // Can't test elementType, because it has been converted to the wrapper class.
            final Class<?> type = getter.getReturnType();
            if (type.isPrimitive()) {
                newValues[0] = Numbers.valueOfNil(type);
            }
        } else {
            Class<?> targetType = getter.getReturnType();
            if (!Collection.class.isAssignableFrom(targetType)) {
                /*
                 * We do not expect a collection. The provided argument should not be a
                 * collection neither. It should be some class convertible to targetType.
                 *
                 * If nevertheless the user provided a collection and this collection contains
                 * no more than 1 element, then as a convenience we will extract the singleton
                 * element and process it as if it had been directly provided in argument.
                 */
                if (newValue instanceof Collection<?>) {
                    final Iterator<?> it = ((Collection<?>) newValue).iterator();
                    if (!it.hasNext()) { // If empty, process like null argument.
                        newValues[0] = null;
                        return converter;
                    }
                    final Object next = it.next();
                    if (!it.hasNext()) { // Singleton
                        newValue = next;
                    }
                    // Other cases: let the collection unchanged. It is likely to
                    // cause an exception later. The message should be appropriate.
                }
                // Getter type (targetType) shall be the same than the setter type (elementType).
                assert elementType == Numbers.primitiveToWrapper(targetType) : elementType;
                targetType = elementType; // Ensure that we use primitive wrapper.
            } else {
                /*
                 * We expect a collection. Collections are handled in one of the two ways below:
                 *
                 *   - If the user gives a collection, the user's collection replaces any
                 *     previous one. The content of the previous collection is discarded.
                 *
                 *   - If the user gives a single value, it will be added to the existing
                 *     collection (if any). The previous values are not discarded. This
                 *     allow for incremental filling of a property.
                 *
                 * The code below prepares an array of elements to be converted and wraps that
                 * array in a List (to be converted to a Set after this block if required). It
                 * is okay to convert the elements after the List creation since the list is a
                 * wrapper.
                 */
                final Collection<?> addTo;
                final Object[] elements;
                if (newValue instanceof Collection<?>) {
                    elements = ((Collection<?>) newValue).toArray();
                    newValue = Arrays.asList(elements); // Content will be converted later.
                    addTo = null;
                } else {
                    elements = new Object[] {newValue};
                    newValue = addTo = (Collection<?>) get(getter, metadata);
                    if (addTo == null) {
                        // No previous collection. Create one.
                        newValue = Arrays.asList(elements);
                    } else if (addTo instanceof CheckedContainer<?>) {
                        // Get the explicitly-specified element type.
                        elementType = ((CheckedContainer<?>) addTo).getElementType();
                    }
                }
                if (elementType != null) {
                    converter = convert(elements, elementType, converter);
                }
                /*
                 * We now have objects of the appropriate type. If we have a singleton to be added
                 * in an existing collection, add it now. In that case the 'newValue' should refer
                 * to the 'addTo' collection. We rely on ModifiableMetadata.copyCollection(...)
                 * optimization for detecting that the new collection is the same instance than
                 * the old one so there is nothing to do. We could exit from the method, but let
                 * it continues in case the user override the 'setFoo(...)' method.
                 */
                if (addTo != null) {
                    /*
                     * Unsafe addition into a collection. In Geotk implementation, the
                     * collection is actually an instance of CheckedCollection, so the check
                     * will be performed at runtime. However other implementations could use
                     * unchecked collection. There is not much we can do.
                     */
                    @SuppressWarnings("unchecked")
                    final Collection<Object> unsafe = (Collection<Object>) addTo;
                    unsafe.add(elements[0]);
                }
            }
            /*
             * If the expected type was not a collection, the conversion of user value happen
             * here. Otherwise conversion from List to Set (if needed) happen here.
             */
            newValues[0] = newValue;
            converter = convert(newValues, targetType, converter);
        }
        return converter;
    }

    /**
     * Converts values in the specified array to the given type. The given converter
     * will be used if suitable, or a new one fetched otherwise.
     *
     * @param elements   The array which contains element to convert.
     * @param targetType The base type of target elements.
     * @param converter  The proposed converter, or {@code null}.
     * @return The last converter used, or {@code null}.
     * @throws ClassCastException If an element can't be converted.
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    private static ObjectConverter<?,?> convert(final Object[] elements,
            final Class<?> targetType, ObjectConverter<?,?> converter) throws ClassCastException
    {
        for (int i=0; i<elements.length; i++) {
            final Object value = elements[i];
            if (value != null) {
                final Class<?> sourceType = value.getClass();
                if (!targetType.isAssignableFrom(sourceType)) try {
                    if (converter == null || !converter.getSourceClass().isAssignableFrom(sourceType)
                                          || !targetType.isAssignableFrom(converter.getTargetClass()))
                    {
                        converter = ConverterRegistry.system().converter(sourceType, targetType);
                    }
                    elements[i] = ((ObjectConverter) converter).convert(value);
                } catch (NonconvertibleObjectException cause) {
                    final ClassCastException e = new ClassCastException(Errors.format(
                            Errors.Keys.ILLEGAL_CLASS_$2, sourceType, targetType));
                    e.initCause(cause);
                    throw e;
                }
            }
        }
        return converter;
    }

    /**
     * Compares the two specified metadata objects. The comparison is <cite>shallow</cite>,
     * i.e. all metadata attributes are compared using the {@link Object#equals} method without
     * recursive call to this {@code shallowEquals} method for other metadata.
     * <p>
     * This method can optionally excludes null values from the comparison. In metadata,
     * null value often means "don't know", so in some occasion we want to consider two
     * metadata as different only if a property value is know for sure to be different.
     *
     * @param metadata1 The first metadata object to compare. This object determines the accessor.
     * @param metadata2 The second metadata object to compare.
     * @param mode The strictness level of the comparison.
     * @param skipNulls If {@code true}, only non-null values will be compared.
     */
    public boolean shallowEquals(final Object metadata1, final Object metadata2,
            final ComparisonMode mode, final boolean skipNulls)
    {
        assert type.isInstance(metadata1) : metadata1;
        assert type.isInstance(metadata2) : metadata2;
        final int count = (mode == ComparisonMode.STRICT &&
                EXTRA_GETTER.getDeclaringClass().isInstance(metadata2)) ? allCount : standardCount;
        for (int i=0; i<count; i++) {
            final Method  method = getters[i];
            final Object  value1 = get(method, metadata1);
            final Object  value2 = get(method, metadata2);
            final boolean empty1 = isEmpty(value1);
            final boolean empty2 = isEmpty(value2);
            if (empty1 && empty2) {
                continue;
            }
            if (!Utilities.deepEquals(value1, value2, mode)) {
                if (mode.ordinal() < ComparisonMode.APPROXIMATIVE.ordinal() && floatEpsilonEqual(value1, value2)) {
                    continue; // Accept this slight difference.
                }
                if (!skipNulls || (!empty1 && !empty2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Copies all metadata from source to target. The source can be any implementation of
     * the metadata interface, but the target must be the implementation expected by this
     * class.
     *
     * @param  source The metadata to copy.
     * @param  target The target metadata.
     * @param  skipNulls If {@code true}, only non-null values will be copied.
     * @return {@code true} in case of success, or {@code false} if at least
     *         one setter method was not found.
     * @throws UnmodifiableMetadataException if the target metadata is unmodifiable.
     */
    public boolean shallowCopy(final Object source, final Object target, final boolean skipNulls)
            throws UnmodifiableMetadataException
    {
        // Because this PropertyAccesssor is designed for the target, we must
        // check if the extra methods are suitable for the source object.
        ObjectConverter<?,?> converter = this.converter;
        boolean success = true;
        assert type.isInstance(source) : Classes.getClass(source);
        final Object[] arguments = new Object[1];
        for (int i=0; i<standardCount; i++) {
            final Method getter = getters[i];
            arguments[0] = get(getter, source);
            if (!skipNulls || !isEmpty(arguments[0])) {
                if (setters == null) {
                    return false;
                }
                final Method setter = setters[i];
                if (setter != null) {
                    converter = convert(getter, target, arguments, elementTypes[i], converter);
                    set(setter, target, arguments);
                } else {
                    success = false;
                }
            }
        }
        this.converter = converter;
        return success;
    }

    /**
     * Replaces every properties in the specified metadata by their
     * {@linkplain ModifiableMetadata#unmodifiable unmodifiable variant}.
     */
    final void freeze(final Object metadata) {
        assert implementation.isInstance(metadata) : metadata;
        if (setters != null) {
            final Object[] arguments = new Object[1];
            for (int i=0; i<allCount; i++) {
                final Method setter = setters[i];
                if (setter != null) {
                    final Method getter = getters[i];
                    final Object source = get(getter, metadata);
                    final Object target = ModifiableMetadata.unmodifiable(source);
                    if (source != target) {
                        arguments[0] = target;
                        set(setter, metadata, arguments);
                        /*
                         * We invoke the set(...) method which do not perform type conversion
                         * because we don't want it to replace the immutable collection created
                         * by ModifiableMetadata.unmodifiable(source). Conversion should not be
                         * required anyway because the getter method should have returned a value
                         * compatible with the setter method - this contract is ensured by the
                         * way the PropertyAccessor constructor selected the setter methods.
                         */
                    }
                }
            }
        }
    }

    /**
     * Returns {@code true} if the metadata is modifiable. This method is not public because it
     * uses heuristic rules. In case of doubt, this method conservatively returns {@code true}.
     */
    final boolean isModifiable() {
        if (setters != null) {
            return true;
        }
        for (int i=0; i<allCount; i++) {
            // Immutable objects usually don't need to be cloned. So if
            // an object is cloneable, it is probably not immutable.
            if (Cloneable.class.isAssignableFrom(getters[i].getReturnType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a hash code for the specified metadata. The hash code is defined as the
     * sum of hash code values of all non-null properties. This is the same contract than
     * {@link java.util.Set#hashCode} and ensure that the hash code value is insensitive
     * to the ordering of properties.
     */
    public int hashCode(final Object metadata) {
        assert type.isInstance(metadata) : metadata;
        int code = 0;
        for (int i=0; i<standardCount; i++) {
            final Object value = get(getters[i], metadata);
            if (!isEmpty(value)) {
                code += value.hashCode();
            }
        }
        return code;
    }

    /**
     * Counts the number of non-null properties.
     */
    public int count(final Object metadata, final int max) {
        assert type.isInstance(metadata) : metadata;
        int count = 0;
        for (int i=0; i<standardCount; i++) {
            if (!isEmpty(get(getters[i], metadata))) {
                if (++count >= max) {
                    break;
                }
            }
        }
        return count;
    }

    /**
     * Returns {@code true} if the specified object is null or an empty collection, array or string.
     * <p>
     * This method intentionally does not inspect array or collection elements, since this method
     * is invoked from methods doing shallow copy or comparison. If we were inspecting elements,
     * we would need to add a check against infinite recursivity.
     */
    static boolean isEmpty(final Object value) {
        return value == null ||
                ((value instanceof CharSequence) && value.toString().trim().isEmpty())  ||
                ((value instanceof Collection<?>) && ((Collection<?>) value).isEmpty()) ||
                ((value instanceof Map<?,?>) && ((Map<?,?>) value).isEmpty())           ||
                (value.getClass().isArray() && Array.getLength(value) == 0);
    }
}

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
package org.geotoolkit.util;

import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedList;
import java.io.Serializable;

import org.geotoolkit.lang.Static;


/**
 * Miscellaneous methods, including convenience methods for {@link Object#equals equals} and
 * {@link Object#hashCode hashCode} implementations. Example use case in a class called
 * {@code Car}:
 *
 * {@preformat java
 *     public boolean equals(Object other) {
 *         if (this == aThat) {
 *             return true;
 *         }
 *         if (other == null || other.getClass() != getClass()) {
 *             return false;
 *         }
 *         Car that = (Car) other;
 *         return Utilities.equals(this.name,           that.name)       &&
 *                Utilities.equals(this.numDoors,       that.numDoors)   &&
 *                Utilities.equals(this.gasMileage,     that.gasMileage) &&
 *                Utilities.equals(this.color,          that.color)      &&
 *                Arrays.equals(this.maintenanceChecks, that.maintenanceChecks);
 *     }
 * }
 *
 * Note the usage of {@link Arrays} method for comparing arrays.
 * <p>
 * This class also provides convenience methods for computing {@linkplain Object#hashCode hash code}
 * values. All those methods expect a {@code seed} argument, which is the hash code value computed
 * for previous fields in a class. For the initial seed (the one for the field for which to compute
 * an hash code), an arbitrary value must be provided. We suggest a different number for different
 * class in order to reduce the risk of collision between "empty" instances of different classes.
 * {@linkplain java.io.Serializable} classes can use {@code (int) serialVersionUID} for example.
 *
 * @author Martin Desruisseaux (IRD, Geomatys)
 * @version 3.20
 *
 * @since 1.2
 * @module
 */
public final class Utilities extends Static {
    /**
     * A prime number used for hash code computation. Value 31 is often used because
     * some modern compilers can optimize {@code x*31} as {@code (x << 5) - x}
     * (Josh Bloch, <cite>Effective Java</cite>).
     */
    private static final int PRIME_NUMBER = 31;

    /**
     * Forbid object creation.
     */
    private Utilities() {
    }

    /**
     * Returns {@code true} if the given booleans are equals. This overloaded flavor is provided
     * only for allowing developer to invoke {@code equals} methods without consideration for
     * the argument type.
     * <p>
     * <strong>WARNING: This method will be removed when Geotk will switch to JDK7
     * (maybe in 2013). See {@link #equals(Object,Object)} for more information.</strong>
     *
     * @param o1 The first value to compare.
     * @param o2 The second value to compare.
     * @return {@code true} if both values are equal.
     *
     * @see Boolean#equals(Object)
     */
    public static boolean equals(final boolean o1, final boolean o2) {
        return o1 == o2;
    }

    /**
     * Returns {@code true} if the given characters are equals. This overloaded flavor is provided
     * only for allowing developer to invoke {@code equals} methods without consideration for
     * the argument type.
     * <p>
     * <strong>WARNING: This method will be removed when Geotk will switch to JDK7
     * (maybe in 2013). See {@link #equals(Object,Object)} for more information.</strong>
     *
     * @param o1 The first value to compare.
     * @param o2 The second value to compare.
     * @return {@code true} if both values are equal.
     *
     * @see Character#equals(Object)
     */
    public static boolean equals(final char o1, final char o2) {
        return o1 == o2;
    }

    /**
     * Returns {@code true} if the given bytes are equals. This overloaded flavor is provided
     * only for allowing developer to invoke {@code equals} methods without consideration for
     * the argument type.
     * <p>
     * <strong>WARNING: This method will be removed when Geotk will switch to JDK7
     * (maybe in 2013). See {@link #equals(Object,Object)} for more information.</strong>
     *
     * @param o1 The first value to compare.
     * @param o2 The second value to compare.
     * @return {@code true} if both values are equal.
     *
     * @see Byte#equals(Object)
     */
    public static boolean equals(final byte o1, final byte o2) {
        return o1 == o2;
    }

    /**
     * Returns {@code true} if the given shorts are equals. This overloaded flavor is provided
     * only for allowing developer to invoke {@code equals} methods without consideration for
     * the argument type.
     * <p>
     * <strong>WARNING: This method will be removed when Geotk will switch to JDK7
     * (maybe in 2013). See {@link #equals(Object,Object)} for more information.</strong>
     *
     * @param o1 The first value to compare.
     * @param o2 The second value to compare.
     * @return {@code true} if both values are equal.
     *
     * @see Short#equals(Object)
     */
    public static boolean equals(final short o1, final short o2) {
        return o1 == o2;
    }

    /**
     * Returns {@code true} if the given integers are equals. This overloaded flavor is provided
     * only for allowing developer to invoke {@code equals} methods without consideration for
     * the argument type.
     * <p>
     * <strong>WARNING: This method will be removed when Geotk will switch to JDK7
     * (maybe in 2013). See {@link #equals(Object,Object)} for more information.</strong>
     *
     * @param o1 The first value to compare.
     * @param o2 The second value to compare.
     * @return {@code true} if both values are equal.
     *
     * @see Integer#equals(Object)
     */
    public static boolean equals(final int o1, final int o2) {
        return o1 == o2;
    }

    /**
     * Returns {@code true} if the given longs are equals. This overloaded flavor is provided
     * only for allowing developer to invoke {@code equals} methods without consideration for
     * the argument type.
     * <p>
     * <strong>WARNING: This method will be removed when Geotk will switch to JDK7
     * (maybe in 2013). See {@link #equals(Object,Object)} for more information.</strong>
     *
     * @param o1 The first value to compare.
     * @param o2 The second value to compare.
     * @return {@code true} if both values are equal.
     *
     * @see Long#equals(Object)
     */
    public static boolean equals(final long o1, final long o2) {
        return o1 == o2;
    }

    /**
     * Returns {@code true} if the given floats are equals. Positive and negative zero are
     * considered different, while a NaN value is considered equal to all other NaN values.
     *
     * @param o1 The first value to compare.
     * @param o2 The second value to compare.
     * @return {@code true} if both values are equal.
     *
     * @see Float#equals(Object)
     */
    public static boolean equals(final float o1, final float o2) {
        return Float.floatToIntBits(o1) == Float.floatToIntBits(o2);
    }

    /**
     * Returns {@code true} if the given doubles are equals. Positive and negative zero are
     * considered different, while a NaN value is considered equal to all other NaN values.
     *
     * @param o1 The first value to compare.
     * @param o2 The second value to compare.
     * @return {@code true} if both values are equal.
     *
     * @see Double#equals(Object)
     */
    public static boolean equals(final double o1, final double o2) {
        return Double.doubleToLongBits(o1) == Double.doubleToLongBits(o2);
    }

    /**
     * Convenience method for testing two objects for equality. One or both objects may be null.
     * This method do <strong>not</strong> iterates recursively in array elements. If array needs
     * to be compared, use one of {@link Arrays} method or {@link #deepEquals deepEquals} instead.
     * <p>
     * <b>Note on assertions:</b> There is no way to ensure at compile time that this method
     * is not invoked with array arguments, while doing so would usually be a program error.
     * Performing a systematic argument check would impose a useless overhead for correctly
     * implemented {@link Object#equals} methods. As a compromise we perform this check at runtime
     * only if assertions are enabled. Using assertions for argument check in a public API is
     * usually a deprecated practice, but we make an exception for this particular method.
     * <p>
     * <b>Note on method overloading:</b> This method could be selected by the compiler for
     * comparing primitive types, because the compiler could perform an auto-boxing and get
     * a result assignable to {@code Object}. However it should not occur in practice because
     * overloaded (and more efficient) methods are provided for every primitive types. This is
     * true even when the two arguments are different primitive type because of widening
     * conversions. The only exception is when a {@code boolean} argument is mixed with a
     * different primitive type.
     * <p>
     * <strong>WARNING: This method will be removed when Geotk will switch to JDK7 (maybe in 2013).
     * This method will be replaced by the new {@code java.util.Objects.equals} method. Developers
     * who are already on JDK7 should use that JDK method instead.</strong>
     *
     * @param object1 The first object to compare, or {@code null}.
     * @param object2 The second object to compare, or {@code null}.
     * @return {@code true} if both objects are equal.
     * @throws AssertionError If assertions are enabled and at least one argument is an array.
     */
    public static boolean equals(final Object object1, final Object object2) throws AssertionError {
        assert object1 == null || !object1.getClass().isArray() : name(object1);
        assert object2 == null || !object2.getClass().isArray() : name(object2);
        return (object1 == object2) || (object1 != null && object1.equals(object2));
    }

    /**
     * Convenience method for testing two objects for equality. One or both objects may be null.
     * If both are non-null and are arrays, then every array elements will be compared.
     * <p>
     * This method may be useful when the objects may or may not be array. If they are known
     * to be arrays, consider using {@link Arrays#deepEquals(Object[],Object[])} or one of its
     * primitive counter-part instead.
     * <p>
     * <strong>Rules for choosing an {@code equals} or {@code deepEquals} method</strong>
     * <ul>
     *   <li>If <em>both</em> objects are declared as {@code Object[]} (not anything else like
     *   {@code String[]}), consider using {@link Arrays#deepEquals(Object[],Object[])} except
     *   if it is known that the array elements can never be other arrays.</li>
     *
     *   <li>Otherwise if both objects are arrays (e.g. {@code Expression[]}, {@code String[]},
     *   {@code int[]}, <i>etc.</i>), use {@link Arrays#equals(Object[],Object[])}. This
     *   rule is applicable to arrays of primitive type too, since {@code Arrays.equals} is
     *   overridden with primitive counter-parts.</li>
     *
     *   <li>Otherwise if at least one object is anything else than {@code Object} (e.g.
     *   {@code String}, {@code Expression}, <i>etc.</i>), use {@link #equals(Object,Object)}.
     *   Using this {@code deepEquals} method would be an overkill since there is no chance that
     *   {@code String} or {@code Expression} could be an array.</li>
     *
     *   <li>Otherwise if <em>both</em> objects are declared exactly as {@code Object} type and
     *   it is known that they could be arrays, only then invoke this {@code deepEquals} method.
     *   In such case, make sure that the hash code is computed using {@link #deepHashCode(Object)}
     *   for consistency.</li>
     * </ul>
     * <p>
     * <strong>WARNING: This method will be removed when Geotk will switch to JDK7 (maybe in 2013).
     * This method will be replaced by the new {@code java.util.Objects.deepEquals} method.
     * Developers who are already on JDK7 should use that JDK method instead.</strong>
     *
     * @param object1 The first object to compare, or {@code null}.
     * @param object2 The second object to compare, or {@code null}.
     * @return {@code true} if both objects are equal.
     */
    public static boolean deepEquals(final Object object1, final Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        if (object1 instanceof Object[]) {
            return (object2 instanceof Object[]) &&
                    Arrays.deepEquals((Object[]) object1, (Object[]) object2);
        }
        if (object1 instanceof double[]) {
            return (object2 instanceof double[]) &&
                    Arrays.equals((double[]) object1, (double[]) object2);
        }
        if (object1 instanceof float[]) {
            return (object2 instanceof float[]) &&
                    Arrays.equals((float[]) object1, (float[]) object2);
        }
        if (object1 instanceof long[]) {
            return (object2 instanceof long[]) &&
                    Arrays.equals((long[]) object1, (long[]) object2);
        }
        if (object1 instanceof int[]) {
            return (object2 instanceof int[]) &&
                    Arrays.equals((int[]) object1, (int[]) object2);
        }
        if (object1 instanceof short[]) {
            return (object2 instanceof short[]) &&
                    Arrays.equals((short[]) object1, (short[]) object2);
        }
        if (object1 instanceof byte[]) {
            return (object2 instanceof byte[]) &&
                    Arrays.equals((byte[]) object1, (byte[]) object2);
        }
        if (object1 instanceof char[]) {
            return (object2 instanceof char[]) &&
                    Arrays.equals((char[]) object1, (char[]) object2);
        }
        if (object1 instanceof boolean[]) {
            return (object2 instanceof boolean[]) &&
                    Arrays.equals((boolean[]) object1, (boolean[]) object2);
        }
        return object1.equals(object2);
    }

    /**
     * Convenience method for testing two objects for equality using the given level of strictness.
     * If at least one of the given objects implement the {@link LenientComparable} interface, then
     * the comparison is performed using the {@link LenientComparable#equals(Object, ComparisonMode)}
     * method. Otherwise this method performs the same work than the above
     * {@link #deepEquals(Object, Object)} convenience method.
     * <p>
     * If both arguments are arrays or collections, then the elements are compared recursively.
     *
     * @param object1 The first object to compare, or {@code null}.
     * @param object2 The second object to compare, or {@code null}.
     * @param mode The strictness level of the comparison.
     * @return {@code true} if both objects are equal for the given level of strictness.
     *
     * @see org.geotoolkit.referencing.CRS#equalsIgnoreMetadata(Object, Object)
     *
     * @since 3.18
     */
    public static boolean deepEquals(final Object object1, final Object object2, final ComparisonMode mode) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            assert isNotDebug(mode) : "object" + (object1 == null ? '1' : '2') + " is null";
            return false;
        }
        if (object1 instanceof LenientComparable) {
            return ((LenientComparable) object1).equals(object2, mode);
        }
        if (object2 instanceof LenientComparable) {
            return ((LenientComparable) object2).equals(object1, mode);
        }
        if (object1 instanceof Map.Entry<?,?>) {
            if (object2 instanceof Map.Entry<?,?>) {
                final Map.Entry<?,?> e1 = (Map.Entry<?,?>) object1;
                final Map.Entry<?,?> e2 = (Map.Entry<?,?>) object2;
                return deepEquals(e1.getKey(),   e2.getKey(),   mode) &&
                       deepEquals(e1.getValue(), e2.getValue(), mode);
            }
            assert isNotDebug(mode) : mismatchedType(Map.Entry.class, object2);
            return false;
        }
        if (object1 instanceof Map<?,?>) {
            if (object2 instanceof Map<?,?>) {
                return equals(((Map<?,?>) object1).entrySet(),
                              ((Map<?,?>) object2).entrySet(), mode);
            }
            assert isNotDebug(mode) : mismatchedType(Map.class, object2);
            return false;
        }
        if (object1 instanceof Collection<?>) {
            if (object2 instanceof Collection<?>) {
                return equals((Collection<?>) object1,
                              (Collection<?>) object2, mode);
            }
            assert isNotDebug(mode) : mismatchedType(Collection.class, object2);
            return false;
        }
        if (object1 instanceof Object[]) {
            if (!(object2 instanceof Object[])) {
                assert isNotDebug(mode) : mismatchedType(Object[].class, object2);
                return false;
            }
            final Object[] array1 = (Object[]) object1;
            final Object[] array2 = (Object[]) object2;
            if (array1 instanceof LenientComparable[]) {
                return equals((LenientComparable[]) array1, array2, mode);
            }
            if (array2 instanceof LenientComparable[]) {
                return equals((LenientComparable[]) array2, array1, mode);
            }
            final int length = array1.length;
            if (array2.length != length) {
                assert isNotDebug(mode) : "Length " + length + " != " + array2.length;
                return false;
            }
            for (int i=0; i<length; i++) {
                if (!deepEquals(array1[i], array2[i], mode)) {
                    assert isNotDebug(mode) : "object[" + i + "] not equal";
                    return false;
                }
            }
            return true;
        }
        return deepEquals(object1, object2);
    }

    /**
     * Compares two arrays where at least one array is known to contain {@link LenientComparable}
     * elements. This knowledge avoid the need to test each element individually. The two arrays
     * shall be non-null.
     */
    private static boolean equals(final LenientComparable[] array1, final Object[] array2, final ComparisonMode mode) {
        final int length = array1.length;
        if (array2.length != length) {
            return false;
        }
        for (int i=0; i<length; i++) {
            final LenientComparable e1 = array1[i];
            final Object e2 = array2[i];
            if (e1 != e2 && (e1 == null || !e1.equals(e2, mode))) {
                assert isNotDebug(mode) : "object[" + i + "] not equal";
                return false;
            }
        }
        return true;
    }

    /**
     * Compares two collections. Order are significant, unless both collections implement the
     * {@link Set} interface.
     */
    private static boolean equals(final Iterable<?> object1, final Iterable<?> object2, final ComparisonMode mode) {
        final Iterator<?> it1 = object1.iterator();
        final Iterator<?> it2 = object2.iterator();
        while (it1.hasNext()) {
            if (!it2.hasNext()) {
                assert isNotDebug(mode) : "Sizes not equal";
                return false;
            }
            Object element1 = it1.next();
            Object element2 = it2.next();
            if (deepEquals(element1, element2, mode)) {
                continue;
            }
            if (!(object1 instanceof Set<?> && object2 instanceof Set<?>)) {
                assert isNotDebug(mode) : "Lists not equal";
                return false;
            }
            /*
             * We have found an element which is not equals. However in the particular
             * case of Set, the element order is not significant. So we need to perform
             * a more costly check ensuring that the collections are still different if
             * ignoring order. Note that the test will ignore the elements successfuly
             * compared up to this point.
             */
            // Creates a copy of REMAINING elements in the first collection.
            final LinkedList<Object> copy = new LinkedList<Object>();
            copy.add(element1);
            while (it1.hasNext()) {
                copy.add(it1.next());
            }
            // Removes from the above copy all REMAINING elements from the second collection.
            while (true) {
                final Iterator<?> it = copy.iterator();
                do if (!it.hasNext()) {
                    assert isNotDebug(mode) : "Sets not equal";
                    return false; // An element has not been found.
                } while (!deepEquals(it.next(), element2, mode));
                it.remove();
                if (!it2.hasNext()) {
                    break;
                }
                element2 = it2.next();
            }
            return copy.isEmpty();
        }
        return !it2.hasNext();
    }

    /**
     * Returns {@code true} if the given mode is not {@link ComparisonMode#DEBUG}.
     */
    private static boolean isNotDebug(final ComparisonMode mode) {
        return mode != ComparisonMode.DEBUG;
    }

    /**
     * Returns an assertion error message for mismatched types.
     *
     * @param  expected The expected type.
     * @param  actual The actual object (not its type).
     * @return The error message to use in assertions.
     */
    private static String mismatchedType(final Class<?> expected, final Object actual) {
        return "Expected " + expected + " but got " + actual.getClass();
    }

    /**
     * Alters the given seed with the hash code value computed from the given value.
     *
     * @param  value The value whose hash code to compute.
     * @param  seed  The hash code value computed so far. If this method is invoked for the first
     *               field, then any arbitrary value (preferably different for each class) is okay.
     * @return An updated hash code value.
     */
    public static int hash(final boolean value, final int seed) {
        // Use the same values than Boolean.hashCode()
        return seed * PRIME_NUMBER + (value ? 1231 : 1237);
    }

    /**
     * Alters the given seed with the hash code value computed from the given value.
     *
     * @param  value The value whose hash code to compute.
     * @param  seed  The hash code value computed so far. If this method is invoked for the first
     *               field, then any arbitrary value (preferably different for each class) is okay.
     * @return An updated hash code value.
     */
    public static int hash(final char value, final int seed) {
        return seed * PRIME_NUMBER + (int) value;
    }

    /**
     * Alters the given seed with the hash code value computed from the given value.
     * {@code byte} and {@code short} primitive types are handled by this method as
     * well through implicit widening conversion.
     *
     * @param  value The value whose hash code to compute.
     * @param  seed  The hash code value computed so far. If this method is invoked for the first
     *               field, then any arbitrary value (preferably different for each class) is okay.
     * @return An updated hash code value.
     */
    public static int hash(final int value, final int seed) {
        return seed * PRIME_NUMBER + value;
    }

    /**
     * Alters the given seed with the hash code value computed from the given value.
     * {@code byte} and {@code short} primitive types are handled by this method as
     * well through implicit widening conversion.
     *
     * @param  value The value whose hash code to compute.
     * @param  seed  The hash code value computed so far. If this method is invoked for the first
     *               field, then any arbitrary value (preferably different for each class) is okay.
     * @return An updated hash code value.
     */
    public static int hash(final long value, final int seed) {
        return seed * PRIME_NUMBER + (((int) value) ^ ((int) (value >>> 32)));
    }

    /**
     * Alters the given seed with the hash code value computed from the given value.
     *
     * @param  value The value whose hash code to compute.
     * @param  seed  The hash code value computed so far. If this method is invoked for the first
     *               field, then any arbitrary value (preferably different for each class) is okay.
     * @return An updated hash code value.
     */
    public static int hash(final float value, final int seed) {
        return seed * PRIME_NUMBER + Float.floatToIntBits(value);
    }

    /**
     * Alters the given seed with the hash code value computed from the given value.
     *
     * @param  value The value whose hash code to compute.
     * @param  seed  The hash code value computed so far. If this method is invoked for the first
     *               field, then any arbitrary value (preferably different for each class) is okay.
     * @return An updated hash code value.
     */
    public static int hash(final double value, final int seed) {
        return hash(Double.doubleToLongBits(value), seed);
    }

    /**
     * Alters the given seed with the hash code value computed from the given value. The given
     * object may be null. This method do <strong>not</strong> iterates recursively in array
     * elements. If array needs to be hashed, use one of {@link Arrays} method or
     * {@link #deepHashCode deepHashCode} instead.
     * <p>
     * <b>Note on assertions:</b> There is no way to ensure at compile time that this method
     * is not invoked with an array argument, while doing so would usually be a program error.
     * Performing a systematic argument check would impose a useless overhead for correctly
     * implemented {@link Object#hashCode} methods. As a compromise we perform this check at
     * runtime only if assertions are enabled. Using assertions for argument check in a public
     * API is usually a deprecated practice, but we make an exception for this particular method.
     *
     * @param  value The value whose hash code to compute, or {@code null}.
     * @param  seed  The hash code value computed so far. If this method is invoked for the first
     *               field, then any arbitrary value (preferably different for each class) is okay.
     * @return An updated hash code value.
     * @throws AssertionError If assertions are enabled and the given value is an array.
     */
    public static int hash(final Object value, int seed) throws AssertionError {
        seed *= PRIME_NUMBER;
        if (value != null) {
            assert !value.getClass().isArray() : name(value);
            seed += value.hashCode();
        }
        return seed;
    }

    /**
     * Returns a hash code for the specified object, which may be an array.
     * This method returns one of the following values:
     * <p>
     * <ul>
     *   <li>If the supplied object is {@code null}, then this method returns 0.</li>
     *   <li>Otherwise if the object is an array of objects, then
     *       {@link Arrays#deepHashCode(Object[])} is invoked.</li>
     *   <li>Otherwise if the object is an array of primitive type, then the corresponding
     *       {@link Arrays#hashCode(double[]) Arrays.hashCode(...)} method is invoked.</li>
     *   <li>Otherwise {@link Object#hashCode()} is invoked.<li>
     * </ul>
     * <p>
     * This method should be invoked <strong>only</strong> if the object type is declared
     * exactly as {@code Object}, not as some subtype like {@code Object[]}, {@code String} or
     * {@code float[]}. In the later cases, use the appropriate {@link Arrays} method instead.
     *
     * @param object The object to compute hash code. May be {@code null}.
     * @return The hash code of the given object.
     */
    public static int deepHashCode(final Object object) {
        if (object == null) {
            return 0;
        }
        if (object instanceof Object[]) {
            return Arrays.deepHashCode((Object[]) object);
        }
        if (object instanceof double[]) {
            return Arrays.hashCode((double[]) object);
        }
        if (object instanceof float[]) {
            return Arrays.hashCode((float[]) object);
        }
        if (object instanceof long[]) {
            return Arrays.hashCode((long[]) object);
        }
        if (object instanceof int[]) {
            return Arrays.hashCode((int[]) object);
        }
        if (object instanceof short[]) {
            return Arrays.hashCode((short[]) object);
        }
        if (object instanceof byte[]) {
            return Arrays.hashCode((byte[]) object);
        }
        if (object instanceof char[]) {
            return Arrays.hashCode((char[]) object);
        }
        if (object instanceof boolean[]) {
            return Arrays.hashCode((boolean[]) object);
        }
        return object.hashCode();
    }

    /**
     * Returns a string representation of the specified object, which may be an array.
     * This method returns one of the following values:
     * <p>
     * <ul>
     *   <li>If the object is an array of objects, then
     *       {@link Arrays#deepToString(Object[])} is invoked.</li>
     *   <li>Otherwise if the object is an array of primitive type, then the corresponding
     *       {@link Arrays#toString(double[]) Arrays.toString(...)} method is invoked.</li>
     *   <li>Otherwise {@link String#valueOf(Object)} is invoked.</li>
     * </ul>
     * <p>
     * This method should be invoked <strong>only</strong> if the object type is declared
     * exactly as {@code Object}, not as some subtype like {@code Object[]}, {@code Number} or
     * {@code float[]}. In the later cases, use the appropriate {@link Arrays} method instead.
     *
     * @param object The object to format as a string. May be {@code null}.
     * @return A string representation of the given object.
     */
    public static String deepToString(final Object object) {
        if (object instanceof Object[]) {
            return Arrays.deepToString((Object[]) object);
        }
        if (object instanceof double[]) {
            return Arrays.toString((double[]) object);
        }
        if (object instanceof float[]) {
            return Arrays.toString((float[]) object);
        }
        if (object instanceof long[]) {
            return Arrays.toString((long[]) object);
        }
        if (object instanceof int[]) {
            return Arrays.toString((int[]) object);
        }
        if (object instanceof short[]) {
            return Arrays.toString((short[]) object);
        }
        if (object instanceof byte[]) {
            return Arrays.toString((byte[]) object);
        }
        if (object instanceof char[]) {
            return Arrays.toString((char[]) object);
        }
        if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) object);
        }
        return String.valueOf(object);
    }

    /**
     * Returns the class name of the given object.
     * Used in assertions only.
     */
    private static String name(final Object object) {
        return object.getClass().getSimpleName();
    }
}

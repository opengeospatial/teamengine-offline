/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2011-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2011-2012, Geomatys
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

import java.util.Map;
import java.util.Iterator;
import java.util.Collection;
import java.util.IdentityHashMap;
import org.geotoolkit.util.collection.XCollections;
import org.geotoolkit.util.collection.CheckedContainer;
import org.opengis.util.CodeList;


/**
 * Implementation of {@link AbstractMetadata#isEmpty()} and {@link ModifiableMetadata#prune()}
 * methods.
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.20
 *
 * @since 3.20
 * @module
 */
final class Pruner extends ThreadLocal<Map<Object,Boolean>> {
    /**
     * The thread-local map of metadata object already tested.
     */
    private static final Pruner INSTANCE = new Pruner();

    /**
     * For internal usage only.
     */
    private Pruner() {
    }

    /**
     * Creates an initially empty hash map when the {@code isEmpty()} or {@code prune()}
     * method is invoked, before any recursive invocation.
     */
    @Override
    protected Map<Object,Boolean> initialValue() {
        return new IdentityHashMap<Object,Boolean>();
    }

    /**
     * Returns the metadata properties suitable to the {@link ModifiableMetadata#prune()} method.
     * The difference between this method and the default {@link MetadataStandard#asMap(Object)}
     * is that empty (but non-null) values are returned. This is necessary in order to allow us
     * to set them to {@code null}.
     */
    private static Map<String, Object> asMap(final MetadataStandard standard, final Object metadata) {
        return standard.asMap(metadata, NullValuePolicy.NON_NULL, KeyNamePolicy.JAVABEANS_PROPERTY);
    }

    /**
     * Returns {@code true} if the value for the given entry is a primitive type.
     */
    private static boolean isPrimitive(final Map.Entry<String,Object> entry) {
        return (entry instanceof CheckedContainer<?>) && ((CheckedContainer<?>) entry).getElementType().isPrimitive();
    }

    /**
     * Returns {@code true} if all properties in the given metadata are null or empty.
     * This method is the entry point for the {@link AbstractMetadata#isEmpty()} and
     * {@link ModifiableMetadata#prune()} public methods.
     * <p>
     * This method is typically invoked recursively while we iterate down the metadata
     * tree. It creates a map of visited nodes when the iteration begin, and deletes
     * that map when the iteration end.
     *
     * @param  metadata The metadata object.
     * @param  prune {@code true} for deleting empty entries.
     * @return {@code true} if all metadata properties are null or empty.
     */
    static boolean isEmpty(final AbstractMetadata metadata, final boolean prune) {
        final Map<String,Object> properties = prune ?
                asMap(metadata.getStandard(), metadata) : metadata.asMap();
        final Map<Object,Boolean> tested = INSTANCE.get();
        if (!tested.isEmpty()) {
            return isEmpty(properties, tested, prune);
        } else try {
            tested.put(metadata, Boolean.FALSE);
            return isEmpty(properties, tested, prune);
        } finally {
            INSTANCE.remove();
        }
    }

    /**
     * {@link #isEmpty(boolean)} implementation, potentially invoked recursively for inspecting
     * child metadata and optionally removing empty ones. The map given in argument is a safety
     * guard against infinite recursivity.
     *
     * @param  properties The metadata properties.
     * @param  tested An initially singleton map, to be filled with tested metadata.
     * @param  prune {@code true} for removing empty properties.
     * @return {@code true} if all metadata properties are null or empty.
     */
    private static boolean isEmpty(final Map<String,Object> properties,
            final Map<Object,Boolean> tested, final boolean prune)
    {
        boolean isEmpty = true;
        for (final Map.Entry<String,Object> entry : properties.entrySet()) {
            final Object value = entry.getValue();
            // No need to check for null values, because the NullValuePolicy argument
            // given to asMap(...) asked for non-null values. If nevertheless a value
            // is null, following code should be robust to that.

            /*
             * We use the 'tested' map in order to avoid computing the same value twice, but
             * also as a check against infinite recursivity - which is why a value needs to be
             * set before to iterate over children. The default value is 'false' because if we
             * test the same object through a "A → B → A" dependency chain, this means that A
             * was not empty (since it contains B).
             */
            final Boolean isEntryEmpty = tested.put(value, Boolean.FALSE);
            if (isEntryEmpty != null) {
                if (isEntryEmpty) { // If a value was already set, restore the original value.
                    tested.put(value, Boolean.TRUE);
                } else {
                    isEmpty = false;
                }
            } else {
                boolean allEmpty = true;
                final Collection<?> values = XCollections.asCollection(value);
                for (final Iterator<?> it = values.iterator(); it.hasNext();) {
                    final Object element = it.next();
                    if (!PropertyAccessor.isEmpty(element)) {
                        /*
                         * If the value is not an empty "simple" property (null value, or empty
                         * string, or an empty collection or array), check if it is an other
                         * metadata element. If so, invoke the isEmpty() method recursively.
                         */
                        final boolean e;
                        if (element instanceof Enum<?> || element instanceof CodeList<?>) {
                            e = false;
                        } else if (element instanceof AbstractMetadata) {
                            final AbstractMetadata md = (AbstractMetadata) element;
                            if (prune) md.prune();
                            e = md.isEmpty();
                        } else {
                            final MetadataStandard standard = MetadataStandard.forClass(element.getClass());
                            if (standard != null) {
                                e = isEmpty(prune ? asMap(standard, element) : standard.asMap(element), tested, prune);
                            } else if (isPrimitive(entry)) {
                                if (value instanceof Number) {
                                    e = Double.isNaN(((Number) value).doubleValue());
                                } else {
                                    // Typically methods of the kind 'isFooAvailable()'.
                                    e = Boolean.FALSE.equals(value);
                                }
                            } else {
                                e = false; // Element is a String, Number (not primitive), etc.
                            }
                        }
                        if (!e) {
                            // At this point, we have determined that the property is not empty.
                            // If we are not removing empty nodes, there is no need to continue.
                            if (!prune) {
                                return false;
                            }
                            allEmpty = false;
                            continue;
                        }
                    }
                    // Found an empty element. Remove it if we are
                    // allowed to do so, then check next elements.
                    if (prune && values == value) {
                        it.remove();
                    }
                }
                // If all elements were empty, set the whole property to 'null'.
                if (allEmpty) {
                    tested.put(value, Boolean.TRUE);
                    if (prune) try {
                        entry.setValue(null);
                    } catch (UnsupportedOperationException e) {
                        // Entry is read only - ignore.
                    }
                } else {
                    isEmpty = false;
                }
            }
        }
        return isEmpty;
    }
}

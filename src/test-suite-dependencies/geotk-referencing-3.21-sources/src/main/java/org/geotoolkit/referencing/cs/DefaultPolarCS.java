/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2004-2012, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotoolkit.referencing.cs;

import java.util.Map;
import net.jcip.annotations.Immutable;

import org.opengis.referencing.cs.PolarCS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;

import org.geotoolkit.internal.referencing.AxisDirections;


/**
 * A two-dimensional coordinate system in which position is specified by the distance from the
 * origin and the angle between the line from the origin to a point and a reference direction.
 * A {@code PolarCS} shall have two {@linkplain #getAxis axis}.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CRS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotoolkit.referencing.crs.DefaultEngineeringCRS Engineering}
 * </TD></TR></TABLE>
 *
 * @author Martin Desruisseaux (IRD, Geomatys)
 * @version 3.19
 *
 * @see DefaultCylindricalCS
 *
 * @since 2.0
 * @module
 */
@Immutable
public class DefaultPolarCS extends AbstractCS implements PolarCS {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = 3960197260975470951L;

    /**
     * Constructs a new object in which every attributes are set to a default value.
     * <strong>This is not a valid object.</strong> This constructor is strictly
     * reserved to JAXB, which will assign values to the fields using reflexion.
     */
    private DefaultPolarCS() {
        this(org.geotoolkit.internal.referencing.NilReferencingObject.INSTANCE);
    }

    /**
     * Constructs a new coordinate system with the same values than the specified one.
     * This copy constructor provides a way to convert an arbitrary implementation into a
     * Geotk one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param cs The coordinate system to copy.
     *
     * @since 2.2
     */
    public DefaultPolarCS(final PolarCS cs) {
        super(cs);
    }

    /**
     * Constructs a two-dimensional coordinate system from a name.
     *
     * @param name  The coordinate system name.
     * @param axis0 The first axis.
     * @param axis1 The second axis.
     */
    public DefaultPolarCS(final String               name,
                          final CoordinateSystemAxis axis0,
                          final CoordinateSystemAxis axis1)
    {
        super(name, axis0, axis1);
    }

    /**
     * Constructs a two-dimensional coordinate system from a set of properties.
     * The properties map is given unchanged to the
     * {@linkplain AbstractCS#AbstractCS(Map,CoordinateSystemAxis[]) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param axis0 The first axis.
     * @param axis1 The second axis.
     */
    public DefaultPolarCS(final Map<String,?>   properties,
                          final CoordinateSystemAxis axis0,
                          final CoordinateSystemAxis axis1)
    {
        super(properties, axis0, axis1);
    }

    /**
     * Returns a Geotk coordinate system implementation with the same values than the given arbitrary
     * implementation. If the given object is {@code null}, then this method returns {@code null}.
     * Otherwise if the given object is already a Geotk implementation, then the given object is
     * returned unchanged. Otherwise a new Geotk implementation is created and initialized to the
     * attribute values of the given object.
     *
     * @param  object The object to get as a Geotk implementation, or {@code null} if none.
     * @return A Geotk implementation containing the values of the given object (may be the
     *         given object itself), or {@code null} if the argument was null.
     *
     * @since 3.18
     */
    public static DefaultPolarCS castOrCopy(final PolarCS object) {
        return (object == null) || (object instanceof DefaultPolarCS)
                ? (DefaultPolarCS) object : new DefaultPolarCS(object);
    }

    /**
     * Returns {@code true} if the specified axis direction is allowed for this coordinate
     * system. The default implementation accepts all directions except temporal ones (i.e.
     * {@link AxisDirection#FUTURE FUTURE} and {@link AxisDirection#PAST PAST}).
     */
    @Override
    protected boolean isCompatibleDirection(final AxisDirection direction) {
        return !AxisDirection.FUTURE.equals(AxisDirections.absolute(direction));
    }
}
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
package org.geotoolkit.metadata.iso.extent;

import java.util.Collection;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.jcip.annotations.ThreadSafe;

import org.opengis.geometry.Geometry;
import org.opengis.metadata.extent.BoundingPolygon;


/**
 * Boundary enclosing the dataset, expressed as the closed set of
 * (<var>x</var>,<var>y</var>) coordinates of the polygon. The last
 * point replicates first point.
 *
 * @author Martin Desruisseaux (IRD, Geomatys)
 * @author Touraïvane (IRD)
 * @author Cédric Briançon (Geomatys)
 * @author Guilhem Legal (Geomatys)
 * @version 3.19
 *
 * @since 2.1
 * @module
 */
@ThreadSafe
@XmlType(name = "EX_BoundingPolygon_Type")
@XmlRootElement(name = "EX_BoundingPolygon")
public class DefaultBoundingPolygon extends AbstractGeographicExtent implements BoundingPolygon {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = 8174011874910887918L;

    /**
     * The sets of points defining the bounding polygon.
     */
    private Collection<Geometry> polygons;

    /**
     * Constructs an initially empty bounding polygon.
     */
    public DefaultBoundingPolygon() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @param source The metadata to copy, or {@code null} if none.
     *
     * @since 2.4
     */
    public DefaultBoundingPolygon(final BoundingPolygon source) {
        super(source);
    }

    /**
     * Creates a bounding polygon initialized to the specified value.
     *
     * @param polygons The sets of points defining the bounding polygon.
     */
    public DefaultBoundingPolygon(final Collection<Geometry> polygons) {
        setPolygons(polygons);
    }

    /**
     * Returns a Geotk metadata implementation with the same values than the given arbitrary
     * implementation. If the given object is {@code null}, then this method returns {@code null}.
     * Otherwise if the given object is already a Geotk implementation, then the given object is
     * returned unchanged. Otherwise a new Geotk implementation is created and initialized to the
     * attribute values of the given object, using a <cite>shallow</cite> copy operation
     * (i.e. attributes are not cloned).
     *
     * @param  object The object to get as a Geotk implementation, or {@code null} if none.
     * @return A Geotk implementation containing the values of the given object (may be the
     *         given object itself), or {@code null} if the argument was null.
     *
     * @since 3.18
     */
    public static DefaultBoundingPolygon castOrCopy(final BoundingPolygon object) {
        return (object == null) || (object instanceof DefaultBoundingPolygon)
                ? (DefaultBoundingPolygon) object : new DefaultBoundingPolygon(object);
    }

    /**
     * Returns the sets of points defining the bounding polygon.
     */
    @Override
    @XmlElement(name = "polygon", required = true)
    public synchronized Collection<Geometry> getPolygons() {
        return polygons = nonNullCollection(polygons, Geometry.class);
    }

    /**
     * Sets the sets of points defining the bounding polygon.
     *
     * @param newValues The new polygons.
     */
    public synchronized void setPolygons(final Collection<? extends Geometry> newValues) {
        polygons = copyCollection(newValues, polygons, Geometry.class);
    }
}

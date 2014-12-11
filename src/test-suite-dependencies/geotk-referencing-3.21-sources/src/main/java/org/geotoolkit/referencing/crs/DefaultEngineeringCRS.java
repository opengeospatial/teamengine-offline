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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotoolkit.referencing.crs;

import java.util.Collections;
import java.util.Map;
import javax.measure.unit.SI;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.jcip.annotations.Immutable;

import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.crs.EngineeringCRS;
import org.opengis.referencing.datum.EngineeringDatum;

import org.geotoolkit.referencing.AbstractReferenceSystem;
import org.geotoolkit.referencing.datum.DefaultEngineeringDatum;
import org.geotoolkit.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotoolkit.referencing.cs.DefaultCartesianCS;
import org.geotoolkit.resources.Vocabulary;
import org.geotoolkit.io.wkt.Formatter;
import org.geotoolkit.util.Utilities;
import org.geotoolkit.util.ComparisonMode;


/**
 * A contextually local coordinate reference system. It can be divided into two broad categories:
 * <p>
 * <ul>
 *   <li>earth-fixed systems applied to engineering activities on or near the surface of the
 *       earth;</li>
 *   <li>CRSs on moving platforms such as road vehicles, vessels, aircraft, or spacecraft.</li>
 * </ul>
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.opengis.referencing.cs.CartesianCS    Cartesian},
 *   {@link org.opengis.referencing.cs.AffineCS       Affine},
 *   {@link org.opengis.referencing.cs.EllipsoidalCS  Ellipsoidal},
 *   {@link org.opengis.referencing.cs.SphericalCS    Spherical},
 *   {@link org.opengis.referencing.cs.CylindricalCS  Cylindrical},
 *   {@link org.opengis.referencing.cs.PolarCS        Polar},
 *   {@link org.opengis.referencing.cs.VerticalCS     Vertical},
 *   {@link org.opengis.referencing.cs.LinearCS       Linear}
 * </TD></TR></TABLE>
 *
 * @author Martin Desruisseaux (IRD, Geomatys)
 * @version 3.19
 *
 * @since 1.2
 * @module
 */
@Immutable
@XmlRootElement(name = "EngineeringCRS")
public class DefaultEngineeringCRS extends AbstractSingleCRS implements EngineeringCRS {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = 6695541732063382701L;

    /**
     * A Cartesian local coordinate system.
     */
    private static final class Cartesian extends DefaultEngineeringCRS {
        /** Serial number for inter-operability with different versions. */
        private static final long serialVersionUID = -1773381554353809683L;

        /** Constructs a coordinate system with the given name. */
        public Cartesian(final int key, final CoordinateSystem cs) {
            super(name(key), DefaultEngineeringDatum.UNKNOWN, cs);
        }

        /**
         * Compares the specified object to this CRS for equality. This method is overridden
         * because, otherwise, {@code CARTESIAN_xD} and {@code GENERIC_xD} would be considered
         * equals when metadata are ignored.
         */
        @Override
        public boolean equals(final Object object, final ComparisonMode mode) {
            if (super.equals(object, mode)) {
                switch (mode) {
                    case STRICT:
                    case BY_CONTRACT: {
                        // No need to performs the check below if metadata were already compared.
                        return true;
                    }
                    default: {
                        final EngineeringCRS that = (EngineeringCRS) object;
                        return Utilities.equals(getName().getCode(), that.getName().getCode());
                    }
                }
            }
            return false;
        }
    }

    /**
     * A two-dimensional Cartesian coordinate reference system with
     * {@linkplain DefaultCoordinateSystemAxis#X x},
     * {@linkplain DefaultCoordinateSystemAxis#Y y}
     * axes in {@linkplain SI#METRE metres}. By default, this CRS has no transformation
     * path to any other CRS (i.e. a map using this CS can't be reprojected to a
     * {@linkplain DefaultGeographicCRS geographic coordinate reference system} for example).
     */
    public static final DefaultEngineeringCRS CARTESIAN_2D =
            new Cartesian(Vocabulary.Keys.CARTESIAN_2D, DefaultCartesianCS.GENERIC_2D);

    /**
     * A three-dimensional Cartesian coordinate reference system with
     * {@linkplain DefaultCoordinateSystemAxis#X x},
     * {@linkplain DefaultCoordinateSystemAxis#Y y},
     * {@linkplain DefaultCoordinateSystemAxis#Z z}
     * axes in {@linkplain SI#METRE metres}. By default, this CRS has no transformation
     * path to any other CRS (i.e. a map using this CS can't be reprojected to a
     * {@linkplain DefaultGeographicCRS geographic coordinate reference system} for example).
     */
    public static final DefaultEngineeringCRS CARTESIAN_3D =
            new Cartesian(Vocabulary.Keys.CARTESIAN_3D, DefaultCartesianCS.GENERIC_3D);

    /**
     * A two-dimensional wildcard coordinate system with
     * {@linkplain DefaultCoordinateSystemAxis#X x},
     * {@linkplain DefaultCoordinateSystemAxis#Y y}
     * axes in {@linkplain SI#METRE metres}. At the difference of {@link #CARTESIAN_2D},
     * this coordinate system is treated specially by the default {@linkplain
     * org.geotoolkit.referencing.operation.DefaultCoordinateOperationFactory coordinate operation
     * factory} with loose transformation rules: if no transformation path were found (for example
     * through a {@linkplain DefaultDerivedCRS derived CRS}), then the transformation from this
     * CRS to any CRS with a compatible number of dimensions is assumed to be the identity
     * transform. This CRS is useful as a kind of wildcard when no CRS were explicitly specified.
     */
    public static final DefaultEngineeringCRS GENERIC_2D =
            new Cartesian(Vocabulary.Keys.GENERIC_CARTESIAN_2D, DefaultCartesianCS.GENERIC_2D);

    /**
     * A three-dimensional wildcard coordinate system with
     * {@linkplain DefaultCoordinateSystemAxis#X x},
     * {@linkplain DefaultCoordinateSystemAxis#Y y},
     * {@linkplain DefaultCoordinateSystemAxis#Z z}
     * axes in {@linkplain SI#METRE metres}. At the difference of {@link #CARTESIAN_3D},
     * this coordinate system is treated specially by the default {@linkplain
     * org.geotoolkit.referencing.operation.DefaultCoordinateOperationFactory coordinate operation
     * factory} with loose transformation rules: if no transformation path were found (for example
     * through a {@linkplain DefaultDerivedCRS derived CRS}), then the transformation from this
     * CRS to any CRS with a compatible number of dimensions is assumed to be the identity
     * transform. This CRS is useful as a kind of wildcard when no CRS were explicitly specified.
     */
    public static final DefaultEngineeringCRS GENERIC_3D =
            new Cartesian(Vocabulary.Keys.GENERIC_CARTESIAN_3D, DefaultCartesianCS.GENERIC_3D);

    /**
     * Constructs a new object in which every attributes are set to a default value.
     * <strong>This is not a valid object.</strong> This constructor is strictly
     * reserved to JAXB, which will assign values to the fields using reflexion.
     */
    private DefaultEngineeringCRS() {
        this(org.geotoolkit.internal.referencing.NilReferencingObject.INSTANCE);
    }

    /**
     * Constructs a new enginnering CRS with the same values than the specified one.
     * This copy constructor provides a way to convert an arbitrary implementation into a
     * Geotk one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param crs The CRS to copy.
     *
     * @since 2.2
     */
    public DefaultEngineeringCRS(final EngineeringCRS crs) {
        super(crs);
    }

    /**
     * Constructs an engineering CRS from a name.
     *
     * @param name The name.
     * @param datum The datum.
     * @param cs The coordinate system.
     */
    public DefaultEngineeringCRS(final String            name,
                                 final EngineeringDatum datum,
                                 final CoordinateSystem    cs)
    {
        this(Collections.singletonMap(NAME_KEY, name), datum, cs);
    }

    /**
     * Constructs an engineering CRS from a set of properties. The properties are given unchanged to
     * the {@linkplain AbstractReferenceSystem#AbstractReferenceSystem(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param datum The datum.
     * @param cs The coordinate system.
     */
    public DefaultEngineeringCRS(final Map<String,?> properties,
                                 final EngineeringDatum   datum,
                                 final CoordinateSystem      cs)
    {
        super(properties, datum, cs);
    }

    /**
     * Returns a Geotk CRS implementation with the same values than the given arbitrary
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
    public static DefaultEngineeringCRS castOrCopy(final EngineeringCRS object) {
        return (object == null) || (object instanceof DefaultEngineeringCRS)
                ? (DefaultEngineeringCRS) object : new DefaultEngineeringCRS(object);
    }

    /**
     * Returns the datum.
     */
    @Override
    @XmlElement(name="engineeringDatum")
    public EngineeringDatum getDatum() {
        return (EngineeringDatum) super.getDatum();
    }

    /**
     * Used by JAXB only (invoked by reflection).
     */
    final void setDatum(final EngineeringDatum datum) {
        super.setDatum(datum);
    }

    /**
     * Formats the inner part of a
     * <A HREF="http://www.geoapi.org/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html#LOCAL_CS"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The name of the WKT element type, which is {@code "LOCAL_CS"}.
     */
    @Override
    public String formatWKT(final Formatter formatter) {
        formatDefaultWKT(formatter);
        return "LOCAL_CS";
    }
}

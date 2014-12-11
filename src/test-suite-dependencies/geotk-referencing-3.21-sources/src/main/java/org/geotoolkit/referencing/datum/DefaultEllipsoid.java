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
 *    This class contains formulas from the public FTP area of NOAA.
 *    NOAAS's work is fully acknowledged here.
 */
package org.geotoolkit.referencing.datum;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.quantity.Length;
import javax.measure.converter.ConversionException;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.jcip.annotations.Immutable;

import org.opengis.referencing.datum.Ellipsoid;

import org.geotoolkit.geometry.DirectPosition2D;
import org.geotoolkit.internal.jaxb.referencing.SecondDefiningParameter;
import org.geotoolkit.internal.jaxb.gco.Measure;
import org.geotoolkit.internal.referencing.CRSUtilities;
import org.geotoolkit.measure.Units;
import org.geotoolkit.measure.CoordinateFormat;
import org.geotoolkit.referencing.NamedIdentifier;
import org.geotoolkit.referencing.IdentifiedObjects;
import org.geotoolkit.referencing.AbstractIdentifiedObject;
import org.geotoolkit.metadata.iso.citation.Citations;
import org.geotoolkit.io.wkt.Formatter;
import org.geotoolkit.util.ComparisonMode;
import org.geotoolkit.util.Utilities;
import org.geotoolkit.resources.Errors;

import static java.lang.Math.*;
import static java.lang.Double.*;
import static org.geotoolkit.util.Utilities.hash;
import static org.geotoolkit.util.ArgumentChecks.ensureNonNull;
import static org.geotoolkit.internal.InternalUtilities.epsilonEqual;


/**
 * Geometric figure that can be used to describe the approximate shape of the earth.
 * In mathematical terms, it is a surface formed by the rotation of an ellipse about
 * its minor axis. An ellipsoid requires two defining parameters:
 * <p>
 * <ul>
 *   <li>{@linkplain #getSemiMajorAxis() semi-major axis} and
 *       {@linkplain #getInverseFlattening() inverse flattening}, or</li>
 *   <li>{@linkplain #getSemiMajorAxis() semi-major axis} and
 *       {@linkplain #getSemiMinorAxis() semi-minor axis}.</li>
 * </ul>
 *
 * @author Martin Desruisseaux (IRD, Geomatys)
 * @author Cédric Briançon (Geomatys)
 * @version 3.20
 *
 * @since 1.2
 * @module
 */
@Immutable
@XmlType(name="EllipsoidType", propOrder={
    "semiMajorAxisMeasure",
    "secondDefiningParameter"
})
@XmlRootElement(name = "Ellipsoid")
public class DefaultEllipsoid extends AbstractIdentifiedObject implements Ellipsoid {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = -1149451543954764081L;

    /**
     * Returns a properties map with the given name and EPSG code.
     * This is used for the creation of default ellipsoid constants.
     */
    private static Map<String,?> properties(final String name, final int code, final Object alias) {
        final Map<String,Object> map = new HashMap<String,Object>(4);
        map.put(NAME_KEY, name);
        map.put(IDENTIFIERS_KEY, new NamedIdentifier(Citations.EPSG, String.valueOf(code)));
        if (alias != null) {
            map.put(ALIAS_KEY, alias);
        }
        return map;
    }

    /**
     * WGS 1984 ellipsoid (EPSG:7030) used in GPS systems.
     * The semi-major and semi-minor axis length are approximatively 6378137 and 6356752
     * {@linkplain SI#METRE metres} respectively.
     * This is the default ellipsoid for most {@code org.geotoolkit} packages.
     *
     * @see DefaultGeodeticDatum#WGS84
     */
    public static final DefaultEllipsoid WGS84 = createFlattenedSphere(
            properties("WGS84", 7030, "WGS 1984"), 6378137.0, 298.257223563, SI.METRE);

    /**
     * WGS 1972 ellipsoid (EPSG:7043).
     * The semi-major and semi-minor axis length are approximatively 6378135 and 6356751
     * {@linkplain SI#METRE metres} respectively.
     *
     * @see DefaultGeodeticDatum#WGS72
     *
     * @since 3.00
     */
    public static final DefaultEllipsoid WGS72 = createFlattenedSphere(
            properties("WGS72", 7043, "WGS 1972"), 6378135.0, 298.26, SI.METRE);

    /**
     * GRS 1980 ellipsoid (EPSG:7019), also called "<cite>International 1979</cite>".
     * The semi-major and semi-minor axis length are approximatively 6378137 and 6356752
     * {@linkplain SI#METRE metres} respectively. This ellipsoid is very close, but not
     * identical, to {@linkplain #WGS84}.
     *
     * {@note The <cite>NAD83</cite> ellipsoid uses the same semi-axis length and units.
     *        The <cite>Web Map Server</cite> <code>"CRS:83"</code> authority code uses that
     *        NAD83 ellipsoid. The <code>"IGNF:MILLER"</code> authority code uses the GRS80
     *        ellipsoid.}
     *
     * @since 2.2
     */
    public static final DefaultEllipsoid GRS80 = createFlattenedSphere(
            properties("GRS80", 7019, new String[] {"GRS 1980", "International 1979"}),
            6378137.0, 298.257222101, SI.METRE);

    /**
     * International 1924 ellipsoid (EPSG:7022).
     * The semi-major and semi-minor axis length are approximatively 6378388 and 6356912
     * {@linkplain SI#METRE metres} respectively.
     *
     * {@note The <cite>European Datum 1950</cite> ellipsoid uses the same
     *        semi-axis length and units.}
     */
    public static final DefaultEllipsoid INTERNATIONAL_1924 = createFlattenedSphere(
            properties("International 1924", 7022, null), 6378388.0, 297.0, SI.METRE);

    /**
     * Clarke 1866 ellipsoid (EPSG:7008).
     * The semi-major and semi-minor axis length are approximatively 6378206 and 6356584
     * {@linkplain SI#METRE metres} respectively.
     *
     * {@note The <cite>NAD27</cite> ellipsoid uses the same semi-axis length and units.
     *        The <cite>Web Map Server</cite> <code>"CRS:27"</code> authority code uses that
     *        NAD27 ellipsoid.}
     *
     * @since 2.2
     */
    public static final DefaultEllipsoid CLARKE_1866 = createEllipsoid(
            properties("Clarke 1866", 7008, null), 6378206.4, 6356583.8, SI.METRE);

    /**
     * A sphere with a radius of 6371000 {@linkplain SI#METRE metres}. Spheres use a simpler
     * algorithm for {@linkplain #orthodromicDistance orthodromic distance computation}, which
     * may be faster and more robust.
     *
     * {@note This ellipsoid is close to the <cite>GRS 1980 Authalic Sphere</cite> (EPSG:7048),
     *        which has a radius of 6371007 metres.}
     *
     * @see DefaultGeodeticDatum#SPHERE
     */
    public static final DefaultEllipsoid SPHERE =
            createEllipsoid("SPHERE", 6371000, 6371000, SI.METRE);

    /**
     * The equatorial radius. This field should be considered as final.
     * It is modified only by JAXB at unmarshalling time.
     *
     * @see #getSemiMajorAxis()
     */
    private double semiMajorAxis;

    /**
     * The polar radius. This field should be considered as final.
     * It is modified only by JAXB at unmarshalling time.
     *
     * @see #getSemiMinorAxis()
     */
    private double semiMinorAxis;

    /**
     * The inverse of the flattening value, or {@link Double#POSITIVE_INFINITY}
     * if the ellipsoid is a sphere. This field should be considered as final.
     * It is modified only by JAXB at unmarshalling time.
     *
     * @see #getInverseFlattening()
     */
    private double inverseFlattening;

    /**
     * Tells if the Inverse Flattening is definitive for this ellipsoid. This field
     * should be considered as final. It is modified only by JAXB at unmarshalling time.
     *
     * @see #isIvfDefinitive()
     */
    private boolean ivfDefinitive;

    /**
     * The units of the semi-major and semi-minor axis values.
     */
    private Unit<Length> unit;

    /**
     * Constructs a new object in which every attributes are set to a default value.
     * <strong>This is not a valid object.</strong> This constructor is strictly
     * reserved to JAXB, which will assign values to the fields using reflexion.
     */
    private DefaultEllipsoid() {
        super(org.geotoolkit.internal.referencing.NilReferencingObject.INSTANCE);
        // We need to let the DefaultEllipsoid fields unitialized, because
        // completeSecondDefiningParameter() will check for zero values.
    }

    /**
     * Constructs a new ellipsoid with the same values than the specified one.
     * This copy constructor provides a way to convert an arbitrary implementation into a
     * Geotk one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param ellipsoid The ellipsoid to copy.
     *
     * @since 2.2
     *
     * @see #castOrCopy(Ellipsoid)
     */
    protected DefaultEllipsoid(final Ellipsoid ellipsoid) {
        super(ellipsoid);
        semiMajorAxis     = ellipsoid.getSemiMajorAxis();
        semiMinorAxis     = ellipsoid.getSemiMinorAxis();
        inverseFlattening = ellipsoid.getInverseFlattening();
        ivfDefinitive     = ellipsoid.isIvfDefinitive();
        unit              = ellipsoid.getAxisUnit();
    }

    /**
     * Constructs a new ellipsoid using the specified axis length. The properties map is
     * given unchanged to the {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map)
     * super-class constructor}.
     *
     * @param properties        Set of properties. Should contains at least {@code "name"}.
     * @param semiMajorAxis     The equatorial radius.
     * @param semiMinorAxis     The polar radius.
     * @param inverseFlattening The inverse of the flattening value.
     * @param ivfDefinitive     {@code true} if the inverse flattening is definitive.
     * @param unit              The units of the semi-major and semi-minor axis values.
     *
     * @see #createEllipsoid
     * @see #createFlattenedSphere
     */
    protected DefaultEllipsoid(final Map<String,?> properties,
                               final double  semiMajorAxis,
                               final double  semiMinorAxis,
                               final double  inverseFlattening,
                               final boolean ivfDefinitive,
                               final Unit<Length> unit)
    {
        super(properties);
        ensureNonNull("unit", unit);
        this.unit              = Units.ensureLinear(unit);
        this.semiMajorAxis     = check("semiMajorAxis",     semiMajorAxis);
        this.semiMinorAxis     = check("semiMinorAxis",     semiMinorAxis);
        this.inverseFlattening = check("inverseFlattening", inverseFlattening);
        this.ivfDefinitive     = ivfDefinitive;
    }

    /**
     * Invoked after the "<cite>second defining parameter</cite>" parameter has been set,
     * in order to compute its counter part. This method is invoked after JAXB unmarshalling.
     *
     * @since 3.20
     */
    private void completeSecondDefiningParameter() {
        if (semiMajorAxis != 0) {
            if (ivfDefinitive) {
                if (semiMinorAxis == 0 && inverseFlattening != 0) {
                    semiMinorAxis = semiMajorAxis*(1 - 1/inverseFlattening);
                }
            } else {
                if (inverseFlattening == 0 && semiMinorAxis != 0) {
                    inverseFlattening = semiMajorAxis / (semiMajorAxis - semiMinorAxis);
                }
            }
        }
    }

    /**
     * Constructs a new ellipsoid using the specified axis length.
     *
     * @param name          The ellipsoid name.
     * @param semiMajorAxis The equatorial radius.
     * @param semiMinorAxis The polar radius.
     * @param unit          The units of the semi-major and semi-minor axis values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createEllipsoid(final String name,
                                                   final double semiMajorAxis,
                                                   final double semiMinorAxis,
                                                   final Unit<Length> unit)
    {
        return createEllipsoid(Collections.singletonMap(NAME_KEY, name),
                               semiMajorAxis, semiMinorAxis, unit);
    }

    /**
     * Constructs a new ellipsoid using the specified axis length. The properties map is
     * given unchanged to the {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map)
     * super-class constructor}.
     *
     * @param properties    Set of properties. Should contains at least {@code "name"}.
     * @param semiMajorAxis The equatorial radius.
     * @param semiMinorAxis The polar radius.
     * @param unit          The units of the semi-major and semi-minor axis values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createEllipsoid(final Map<String,?> properties,
                                                   final double semiMajorAxis,
                                                   final double semiMinorAxis,
                                                   final Unit<Length> unit)
    {
        if (semiMajorAxis == semiMinorAxis) {
            return new Spheroid(properties, semiMajorAxis, false, unit);
        } else {
            return new DefaultEllipsoid(properties, semiMajorAxis, semiMinorAxis,
                       semiMajorAxis/(semiMajorAxis-semiMinorAxis), false, unit);
        }
    }

    /**
     * Constructs a new ellipsoid using the specified axis length and inverse flattening value.
     *
     * @param name              The ellipsoid name.
     * @param semiMajorAxis     The equatorial radius.
     * @param inverseFlattening The inverse flattening value.
     * @param unit              The units of the semi-major and semi-minor axis
     *                          values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createFlattenedSphere(final String name,
                                                         final double semiMajorAxis,
                                                         final double inverseFlattening,
                                                         final Unit<Length> unit)
    {
        return createFlattenedSphere(Collections.singletonMap(NAME_KEY, name),
                                     semiMajorAxis, inverseFlattening, unit);
    }

    /**
     * Constructs a new ellipsoid using the specified axis length and
     * inverse flattening value. The properties map is given unchanged to the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     *
     * @param properties        Set of properties. Should contains at least {@code "name"}.
     * @param semiMajorAxis     The equatorial radius.
     * @param inverseFlattening The inverse flattening value.
     * @param unit              The units of the semi-major and semi-minor axis
     *                          values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createFlattenedSphere(final Map<String,?> properties,
                                                         final double semiMajorAxis,
                                                         final double inverseFlattening,
                                                         final Unit<Length> unit)
    {
        if (isInfinite(inverseFlattening)) {
            return new Spheroid(properties, semiMajorAxis, true, unit);
        } else {
            return new DefaultEllipsoid(properties, semiMajorAxis,
                                        semiMajorAxis*(1-1/inverseFlattening),
                                        inverseFlattening, true, unit);
        }
    }

    /**
     * Wraps an arbitrary ellipsoid into a Geotk implementation. This method is useful if
     * {@link #orthodromicDistance orthodromic distance computation} (for example) are desired.
     * If the supplied ellipsoid is already an instance of {@code DefaultEllipsoid} or is
     * {@code null}, then it is returned unchanged.
     *
     * @param  object The object to get as a Geotk implementation, or {@code null} if none.
     * @return A Geotk implementation containing the values of the given object (may be the
     *         given object itself), or {@code null} if the argument was null.
     */
    public static DefaultEllipsoid castOrCopy(final Ellipsoid object) {
        if (object == null || object instanceof DefaultEllipsoid) {
            return (DefaultEllipsoid) object;
        }
        final Map<String,?> properties = IdentifiedObjects.getProperties(object);
        final double        semiMajor  = object.getSemiMajorAxis();
        final Unit<Length>  unit       = object.getAxisUnit();
        return object.isIvfDefinitive() ?
                createFlattenedSphere(properties, semiMajor, object.getInverseFlattening(), unit) :
                createEllipsoid      (properties, semiMajor, object.getSemiMinorAxis(),     unit);
    }

    /**
     * Checks the argument validity. Argument {@code value} should be greater than zero.
     *
     * @param  name  Argument name.
     * @param  value Argument value.
     * @return {@code value}.
     * @throws IllegalArgumentException if {@code value} is not greater than  0.
     */
    static double check(final String name, final double value) throws IllegalArgumentException {
        if (value > 0) {
            return value;
        }
        throw new IllegalArgumentException(Errors.format(Errors.Keys.ILLEGAL_ARGUMENT_$2, name, value));
    }

    /**
     * Returns the linear unit of the {@linkplain #getSemiMajorAxis() semi-major}
     * and {@linkplain #getSemiMinorAxis() semi-minor} axis values.
     *
     * @return The axis linear unit.
     */
    @Override
    public Unit<Length> getAxisUnit() {
        return unit;
    }

    /**
     * Length of the semi-major axis of the ellipsoid. This is the
     * equatorial radius in {@linkplain #getAxisUnit() axis linear unit}.
     *
     * @return Length of semi-major axis.
     */
    @Override
    public double getSemiMajorAxis() {
        return semiMajorAxis;
    }

    /**
     * Returns the semi-major axis value as a measurement.
     * This method is invoked by JAXB for XML marshalling.
     */
    @XmlElement(name = "semiMajorAxis", required = true)
    final Measure getSemiMajorAxisMeasure() {
        return new Measure(semiMajorAxis, unit);
    }

    /**
     * Sets the semi-major axis value. This method is invoked
     * by JAXB at unmarshalling time only.
     */
    private void setSemiMajorAxisMeasure(final Measure uom) {
        if (semiMajorAxis != 0) {
            throw new IllegalStateException();
        }
        semiMajorAxis = uom.value;
        unit = uom.unit.asType(Length.class);
        completeSecondDefiningParameter();
    }

    /**
     * Length of the semi-minor axis of the ellipsoid. This is the
     * polar radius in {@linkplain #getAxisUnit() axis linear unit}.
     *
     * @return Length of semi-minor axis.
     */
    @Override
    public double getSemiMinorAxis() {
        return semiMinorAxis;
    }

    /**
     * Returns the radius of a hypothetical sphere having the same surface than this ellipsoid.
     * The radius is expressed in {@linkplain #getAxisUnit() axis linear unit}.
     *
     * @return The radius of a sphere having the same surface than this ellipsoid.
     *
     * @since 3.20
     */
    public double getAuthalicRadius() {
        return CRSUtilities.getAuthalicRadius(getSemiMajorAxis(), getSemiMinorAxis());
    }

    /**
     * The ratio of the distance between the center and a focus of the ellipse
     * to the length of its semimajor axis. The eccentricity can alternately be
     * computed from the equation: <code>e=sqrt(2f-f²)</code>.
     *
     * @return The eccentricity of this ellipsoid.
     */
    public double getEccentricity() {
        final double f = 1 - getSemiMinorAxis()/getSemiMajorAxis();
        return sqrt(2*f - f*f);
    }

    /**
     * Returns the value of the inverse of the flattening constant. Flattening is a value
     * used to indicate how closely an ellipsoid approaches a spherical shape. The inverse
     * flattening is related to the equatorial/polar radius by the formula
     *
     * <var>ivf</var>&nbsp;=&nbsp;<var>r</var><sub>e</sub>/(<var>r</var><sub>e</sub>-<var>r</var><sub>p</sub>).
     *
     * For perfect spheres (i.e. if {@link #isSphere} returns {@code true}),
     * the {@link Double#POSITIVE_INFINITY POSITIVE_INFINITY} value is used.
     *
     * @return The inverse flattening value.
     */
    @Override
    public double getInverseFlattening() {
        return inverseFlattening;
    }

    /**
     * Indicates if the {@linkplain #getInverseFlattening inverse flattening} is definitive for
     * this ellipsoid. Some ellipsoids use the IVF as the defining value, and calculate the polar
     * radius whenever asked. Other ellipsoids use the polar radius to calculate the IVF whenever
     * asked. This distinction can be important to avoid floating-point rounding errors.
     *
     * @return {@code true} if the {@linkplain #getInverseFlattening inverse flattening} is
     *         definitive, or {@code false} if the {@linkplain #getSemiMinorAxis() polar radius}
     *         is definitive.
     */
    @Override
    public boolean isIvfDefinitive() {
        return ivfDefinitive;
    }

    /**
     * Returns the object to be marshalled as the {@code SecondDefiningParameter} XML element. The
     * returned object contains the values for {@link #semiMinorAxis} or {@link #inverseFlattening},
     * according to the {@link #isIvfDefinitive()} value. This method is for JAXB marshalling only.
     */
    @XmlElement(name = "secondDefiningParameter")
    final SecondDefiningParameter getSecondDefiningParameter() {
        return new SecondDefiningParameter(this, true);
    }

    /**
     * Sets the second defining parameter value, either the inverse of the flattening
     * value or the semi minor axis value, according to what have been defined in the
     * second defining parameter given. This is for JAXB unmarshalling process only.
     */
    private void setSecondDefiningParameter(SecondDefiningParameter second)
            throws ConversionException
    {
        while (second.secondDefiningParameter != null) {
            second = second.secondDefiningParameter;
        }
        final Measure measure = second.measure;
        if (measure != null) {
            double value = measure.value;
            if (second.isIvfDefinitive()) {
                if (inverseFlattening == 0) {
                    inverseFlattening = value;
                    ivfDefinitive = true;
                    completeSecondDefiningParameter();
                    return;
                }
            } else {
                final Unit<?> uom = measure.unit;
                if (uom != null) {
                    if (unit != null) {
                        value = uom.getConverterToAny(unit).convert(value);
                    } else {
                        unit = uom.asType(Length.class);
                    }
                }
                if (semiMinorAxis == 0) {
                    semiMinorAxis = value;
                    ivfDefinitive = false;
                    completeSecondDefiningParameter();
                    return;
                }
            }
            throw new IllegalStateException();
        }
    }

    /**
     * After the unmarshalling process, only one value between {@link #semiMinorAxis} and
     * {@link #inverseFlattening} has been defined. Since the {@link #semiMajorAxis} has
     * been defined, it is now possible to calculate the value of the missing parameter
     * using the values of those that are set.
     * <p>
     * This method is invoked by JAXB only.
     */
    private void afterUnmarshal(Object target, Object parent) {
        if (ivfDefinitive) {
            semiMinorAxis = semiMajorAxis * (1 - 1/inverseFlattening);
        } else {
            inverseFlattening = semiMajorAxis / (semiMajorAxis - semiMinorAxis);
        }
    }

    /**
     * {@code true} if the ellipsoid is degenerate and is actually a sphere. The sphere is
     * completely defined by the {@linkplain #getSemiMajorAxis() semi-major axis}, which is the
     * radius of the sphere.
     *
     * @return {@code true} if the ellipsoid is degenerate and is actually a sphere.
     */
    @Override
    public boolean isSphere() {
        return semiMajorAxis == semiMinorAxis;
    }

    /**
     * Returns the orthodromic distance between two geographic coordinates.
     * The orthodromic distance is the shortest distance between two points
     * on a sphere's surface. The default implementation delegates the work
     * to {@link #orthodromicDistance(double,double,double,double)}.
     *
     * @param  P1 Longitude and latitude of first point (in decimal degrees).
     * @param  P2 Longitude and latitude of second point (in decimal degrees).
     * @return The orthodromic distance (in the units of this ellipsoid).
     */
    public double orthodromicDistance(final Point2D P1, final Point2D P2) {
        return orthodromicDistance(P1.getX(), P1.getY(), P2.getX(), P2.getY());
    }

    /**
     * Returns the orthodromic distance between two geographic coordinates.
     * The orthodromic distance is the shortest distance between two points
     * on a sphere's surface. The orthodromic path is always on a great circle.
     * This is different from the <cite>loxodromic distance</cite>, which is a
     * longer distance on a path with a constant direction on the compass.
     *
     * @param  x1 Longitude of first  point (in decimal degrees).
     * @param  y1 Latitude  of first  point (in decimal degrees).
     * @param  x2 Longitude of second point (in decimal degrees).
     * @param  y2 Latitude  of second point (in decimal degrees).
     * @return The orthodromic distance (in the units of this ellipsoid's axis).
     */
    public double orthodromicDistance(double x1, double y1, double x2, double y2) {
        x1 = toRadians(x1);
        y1 = toRadians(y1);
        x2 = toRadians(x2);
        y2 = toRadians(y2);
        /*
         * Solution of the geodetic inverse problem after T.Vincenty.
         * Modified Rainsford's method with Helmert's elliptical terms.
         * Effective in any azimuth and at any distance short of antipodal.
         *
         * Latitudes and longitudes in radians positive North and East.
         * Forward azimuths at both points returned in radians from North.
         *
         * Programmed for CDC-6600 by LCDR L.Pfeifer NGS ROCKVILLE MD 18FEB75
         * Modified for IBM SYSTEM 360 by John G.Gergen NGS ROCKVILLE MD 7507
         * Ported from Fortran to Java by Martin Desruisseaux.
         *
         * Source: ftp://ftp.ngs.noaa.gov/pub/pcsoft/for_inv.3d/source/inverse.for
         *         subroutine INVER1
         */
        final int    MAX_ITERATIONS = 100;
        final double EPS = 0.5E-13;
        final double F   = 1/getInverseFlattening();
        final double R   = 1-F;

        double tu1 = R * sin(y1) / cos(y1);
        double tu2 = R * sin(y2) / cos(y2);
        double cu1 = 1 / sqrt(tu1*tu1 + 1);
        double cu2 = 1 / sqrt(tu2*tu2 + 1);
        double su1 = cu1*tu1;
        double s   = cu1*cu2;
        double baz = s*tu2;
        double faz = baz*tu1;
        double x   = x2-x1;
        for (int i=0; i<MAX_ITERATIONS; i++) {
            final double sx = sin(x);
            final double cx = cos(x);
            tu1 = cu2*sx;
            tu2 = baz - su1*cu2*cx;
            final double sy = hypot(tu1, tu2);
            final double cy = s*cx + faz;
            final double y = atan2(sy, cy);
            final double SA = s*sx/sy;
            final double c2a = 1 - SA*SA;
            double cz = faz+faz;
            if (c2a > 0) {
                cz = -cz/c2a + cy;
            }
            double e = cz*cz*2 - 1;
            double c = ((-3*c2a+4)*F + 4) * c2a * F/16;
            double d = x;
            x = ((e*cy*c+cz)*sy*c + y) * SA;
            x = (1-c)*x*F + x2-x1;

            if (abs(d-x) <= EPS) {
                if (false) {
                    // 'faz' and 'baz' are forward azimuths at both points.
                    // Since the current API can't returns this result, it
                    // doesn't worth to compute it at this time.
                    faz = atan2(tu1, tu2);
                    baz = atan2(cu1*sx, baz*cx - su1*cu2) + PI;
                }
                x = sqrt((1/(R*R) - 1) * c2a + 1) + 1;
                x = (x-2)/x;
                c = 1-x;
                c = (x*x/4 + 1)/c;
                d = (0.375*x*x - 1)*x;
                x = e*cy;
                s = 1 - 2*e;
                s = ((((sy*sy*4 - 3)*s*cz*d/6-x)*d/4+cz)*sy*d+y)*c*R*getSemiMajorAxis();
                return s;
            }
        }
        // No convergence. It may be because coordinate points
        // are equals or because they are at antipodes.
        final double LEPS = 1E-10;
        if (abs(x1-x2) <= LEPS && abs(y1-y2) <= LEPS) {
            return 0; // Coordinate points are equals
        }
        if (abs(y1) <= LEPS && abs(y2) <= LEPS) {
            return abs(x1-x2) * getSemiMajorAxis(); // Points are on the equator.
        }
        // At least one input ordinate is NaN.
        if (isNaN(x1) || isNaN(y1) || isNaN(x2) || isNaN(y2)) {
            return NaN;
        }
        // Other cases: no solution for this algorithm.
        final CoordinateFormat format = new CoordinateFormat();
        throw new ArithmeticException(Errors.format(Errors.Keys.NO_CONVERGENCE_$2,
                  format.format(new DirectPosition2D(toDegrees(x1), toDegrees(y1))),
                  format.format(new DirectPosition2D(toDegrees(x2), toDegrees(y2)))));
    }

    /**
     * Compare this ellipsoid with the specified object for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @param  mode {@link ComparisonMode#STRICT STRICT} for performing a strict comparison, or
     *         {@link ComparisonMode#IGNORE_METADATA IGNORE_METADATA} for comparing only properties
     *         relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    @SuppressWarnings("fallthrough")
    public boolean equals(final Object object, final ComparisonMode mode) {
        if (object == this) {
            return true; // Slight optimization.
        }
        if (super.equals(object, mode)) {
            switch (mode) {
                case STRICT: {
                    final DefaultEllipsoid that = (DefaultEllipsoid) object;
                    return ivfDefinitive == that.ivfDefinitive &&
                           Utilities.equals(this.semiMajorAxis,     that.semiMajorAxis)     &&
                           Utilities.equals(this.semiMinorAxis,     that.semiMinorAxis)     &&
                           Utilities.equals(this.inverseFlattening, that.inverseFlattening) &&
                           Utilities.equals(this.unit,              that.unit);
                }
                case BY_CONTRACT: {
                    if (isIvfDefinitive() != ((Ellipsoid) object).isIvfDefinitive()) {
                        return false;
                    }
                    // Fall through
                }
                default: {
                    final Ellipsoid that = (Ellipsoid) object;
                    return epsilonEqual(getSemiMajorAxis(),     that.getSemiMajorAxis(),     mode) &&
                           epsilonEqual(getSemiMinorAxis(),     that.getSemiMinorAxis(),     mode) &&
                           epsilonEqual(getInverseFlattening(), that.getInverseFlattening(), mode) &&
                           Utilities.equals(getAxisUnit(),      that.getAxisUnit());
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int computeHashCode() {
        return hash(ivfDefinitive ? inverseFlattening : semiMinorAxis,
               hash(semiMajorAxis, super.computeHashCode()));
    }

    /**
     * Formats the inner part of a
     * <A HREF="http://www.geoapi.org/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html#SPHEROID"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name, which is {@code "SPHEROID"}.
     */
    @Override
    public String formatWKT(final Formatter formatter) {
        final double ivf = getInverseFlattening();
        formatter.append(getAxisUnit().getConverterTo(SI.METRE).convert(getSemiMajorAxis()));
        formatter.append(isInfinite(ivf) ? 0 : ivf);
        return "SPHEROID";
    }
}

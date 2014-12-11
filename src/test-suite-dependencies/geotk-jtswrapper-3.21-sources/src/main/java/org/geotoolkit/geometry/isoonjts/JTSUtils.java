/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotoolkit.geometry.isoonjts;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotoolkit.geometry.GeneralDirectPosition;
import org.geotoolkit.geometry.isoonjts.spatialschema.JTSPositionFactory;
import org.geotoolkit.geometry.isoonjts.spatialschema.geometry.aggregate.AbstractJTSAggregate;
import org.geotoolkit.geometry.isoonjts.spatialschema.geometry.aggregate.JTSMultiCurve;
import org.geotoolkit.geometry.isoonjts.spatialschema.geometry.aggregate.JTSMultiPoint;
import org.geotoolkit.geometry.isoonjts.spatialschema.geometry.aggregate.JTSMultiPrimitive;
import org.geotoolkit.geometry.isoonjts.spatialschema.geometry.aggregate.JTSMultiSurface;
import org.geotoolkit.geometry.isoonjts.spatialschema.geometry.geometry.JTSGeometryFactory;
import org.geotoolkit.geometry.isoonjts.spatialschema.geometry.geometry.JTSLineString;
import org.geotoolkit.geometry.isoonjts.spatialschema.geometry.geometry.JTSPolygon;
import org.geotoolkit.geometry.isoonjts.spatialschema.geometry.primitive.JTSCurve;
import org.geotoolkit.geometry.isoonjts.spatialschema.geometry.primitive.JTSPrimitiveFactory;
import org.geotoolkit.geometry.jts.SRIDGenerator;
import org.geotoolkit.referencing.CRS;
import org.opengis.util.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;

/**
 * Class with static methods to help the conversion process between JTS
 * geometries and ISO geometries.
 * @module pending
 */
public final class JTSUtils {

    /**
     * Common instance of GEOMETRY_FACTORY with the default JTS precision model
     * that can be used to make new geometries.
     */
    public static final com.vividsolutions.jts.geom.GeometryFactory GEOMETRY_FACTORY = new com.vividsolutions.jts.geom.GeometryFactory();

    /**
     * This class has only static methods, so we make the constructor private
     * to prevent instantiation.
     */
    private JTSUtils() {
    }

    /**
     * Creates a 19107 primitive geometry from the given JTS geometry.
     */
    public static Geometry toISO(final com.vividsolutions.jts.geom.Geometry jtsGeom,
            CoordinateReferenceSystem crs) {

        if (jtsGeom == null) {
            return null;
        }

        if(crs == null){
            //try to extract the crs from the srid
            final int srid = jtsGeom.getSRID();
            if(srid != 0){
                final String strCRS = SRIDGenerator.toSRS(srid, SRIDGenerator.Version.V1);
                try {
                    crs = CRS.decode(strCRS);
                } catch (NoSuchAuthorityCodeException ex) {
                    Logger.getLogger(JTSUtils.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FactoryException ex) {
                    Logger.getLogger(JTSUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        //TODO use factory finder when primitive factory and geometry factory are ready.
        final PrimitiveFactory pf = new JTSPrimitiveFactory(crs);//FactoryFinder.getPrimitiveFactory(hints);
        final GeometryFactory gf  = new JTSGeometryFactory(crs); //FactoryFinder.getGeometryFactory(hints);

        if (jtsGeom instanceof com.vividsolutions.jts.geom.Point) {
            com.vividsolutions.jts.geom.Point candidate = (com.vividsolutions.jts.geom.Point) jtsGeom;
            DirectPosition dp = pointToDirectPosition(candidate, crs);
            return pf.createPoint(dp);

        } else if (jtsGeom instanceof com.vividsolutions.jts.geom.LineString) {
            com.vividsolutions.jts.geom.LineString candidate = (com.vividsolutions.jts.geom.LineString) jtsGeom;
            LineString ls = gf.createLineString(new ArrayList<Position>());
            PointArray pointList = ls.getControlPoints();
            for (int i = 0, n = candidate.getNumPoints(); i < n; i++) {
                pointList.add(coordinateToDirectPosition(candidate.getCoordinateN(i), crs));
            }
            return (JTSLineString)ls;
           
        } else if (jtsGeom instanceof com.vividsolutions.jts.geom.LinearRing) {
            return linearRingToRing((com.vividsolutions.jts.geom.LinearRing) jtsGeom, crs);

        } else if (jtsGeom instanceof com.vividsolutions.jts.geom.Polygon) {
            com.vividsolutions.jts.geom.Polygon jtsPolygon = (com.vividsolutions.jts.geom.Polygon) jtsGeom;
            Ring externalRing = linearRingToRing(
                    (com.vividsolutions.jts.geom.LinearRing) jtsPolygon.getExteriorRing(),
                    crs);
            ArrayList internalRings = new ArrayList();
            for (int i = 0, n = jtsPolygon.getNumInteriorRing(); i < n; i++) {
                internalRings.add(linearRingToRing(
                        (com.vividsolutions.jts.geom.LinearRing) jtsPolygon.getInteriorRingN(i),
                        crs));
            }
            SurfaceBoundary boundary = pf.createSurfaceBoundary(externalRing, internalRings);
            Polygon polygon = gf.createPolygon(boundary);
            return (JTSPolygon) polygon;
            
            /*ArrayList<Polygon> patches = new ArrayList<Polygon>();
            patches.add(polygon);
            PolyhedralSurface result = gf.createPolyhedralSurface(patches);
            return result;*/

        } else if (jtsGeom instanceof GeometryCollection) {
            com.vividsolutions.jts.geom.GeometryCollection jtsCollection = (com.vividsolutions.jts.geom.GeometryCollection) jtsGeom;
            boolean multiPoint   = true;
            boolean multiCurve   = true;
            boolean multiSurface = true;
            for (int i = 0, n = jtsCollection.getNumGeometries(); i < n; i++) {
                if (!(jtsCollection.getGeometryN(i) instanceof com.vividsolutions.jts.geom.Point)) {
                    multiPoint = false;
                }
                if (!(jtsCollection.getGeometryN(i) instanceof com.vividsolutions.jts.geom.LineString)) {
                    multiCurve = false;
                }
                if (!(jtsCollection.getGeometryN(i) instanceof com.vividsolutions.jts.geom.Polygon)) {
                    multiSurface = false;
                }
            }
            AbstractJTSAggregate result;
            if (multiPoint) {
                result = new JTSMultiPoint(crs);
                Set elements = result.getElements();
                for (int i = 0, n = jtsCollection.getNumGeometries(); i < n; i++) {
                    //result.getElements().add(jtsToGo1(jtsCollection.getGeometryN(i), crs));
                    elements.add(toISO(jtsCollection.getGeometryN(i), crs));
                }
            } else if (multiCurve) {
                result = new JTSMultiCurve(crs);
                Set elements = result.getElements();
                for (int i = 0, n = jtsCollection.getNumGeometries(); i < n; i++) {
                    //result.getElements().add(jtsToGo1(jtsCollection.getGeometryN(i), crs));
                    Geometry element = toISO(jtsCollection.getGeometryN(i), crs);
                    if (element instanceof JTSLineString) {
                        JTSCurve curve = new JTSCurve(crs);
                        curve.getSegments().add((JTSLineString) element);
                        element = curve;

                    }
                    elements.add(element);
                }
            }  else if (multiSurface) {
                result = new JTSMultiSurface(crs);
                Set elements = result.getElements();
                for (int i = 0, n = jtsCollection.getNumGeometries(); i < n; i++) {
                    //result.getElements().add(jtsToGo1(jtsCollection.getGeometryN(i), crs));
                    elements.add(toISO(jtsCollection.getGeometryN(i), crs));
                }
            } else {
                result = new JTSMultiPrimitive();
                Set elements = result.getElements();
                for (int i = 0, n = jtsCollection.getNumGeometries(); i < n; i++) {
                    //result.getElements().add(jtsToGo1(jtsCollection.getGeometryN(i), crs));
                    elements.add(toISO(jtsCollection.getGeometryN(i), crs));
                }
            }
            return result;

        } else {
            throw new IllegalArgumentException("Unsupported geometry type: " + jtsGeom.getGeometryType());
        }
    }

    /**
     * Converts a DirectPosition to a JTS Coordinate.  Returns a newly
     * instantiated Coordinate object.
     */
    public static com.vividsolutions.jts.geom.Coordinate directPositionToCoordinate(final DirectPosition dp) {
        double x = Double.NaN, y = Double.NaN, z = Double.NaN;
        final int d = dp.getDimension();
        if (d >= 1) {
            x = dp.getOrdinate(0);
            if (d >= 2) {
                y = dp.getOrdinate(1);
                if (d >= 3) {
                    z = dp.getOrdinate(2);
                }
            }
        }
        return new com.vividsolutions.jts.geom.Coordinate(x, y, z);
    }

    /**
     * Sets the coordinate values of an existing JTS Coordinate by extracting
     * values from a DirectPosition.  If the dimension of the DirectPosition is
     * less than three, then the unused ordinates of the Coordinate are set to
     * Double.NaN.
     */
    public static void directPositionToCoordinate(final DirectPosition dp, final com.vividsolutions.jts.geom.Coordinate result) {
        final int d = dp.getDimension();
        if (d >= 1) {
            result.x = dp.getOrdinate(0);
            if (d >= 2) {
                result.y = dp.getOrdinate(1);
                if (d >= 3) {
                    result.z = dp.getOrdinate(3);
                } else {
                    result.z = Double.NaN;
                }
            } else {
                result.y = result.z = Double.NaN;
            }
        } else {
            // I can't imagine a DirectPosition with dimension zero, but it
            // can't hurt to have code to handle that case...
            result.x = result.y = result.z = Double.NaN;
        }
    }

    /**
     * Converts a DirectPosition to a JTS Point primitive.  Returns a newly
     * instantiated Point object that was created using the default
     * GeometryFactory instance.
     */
    public static com.vividsolutions.jts.geom.Point directPositionToPoint(final DirectPosition dp) {
        return GEOMETRY_FACTORY.createPoint(directPositionToCoordinate(dp));
    }

    /**
     * Converts a JTS Coordinate to a DirectPosition with the given CRS.
     */
    public static DirectPosition coordinateToDirectPosition(final com.vividsolutions.jts.geom.Coordinate c,
            final CoordinateReferenceSystem crs) {

        PositionFactory pf = new JTSPositionFactory(crs);

        double[] vertices;
        if (crs == null) {
            vertices = new double[3];
            vertices[0] = c.x;
            vertices[1] = c.y;
            vertices[2] = c.z;
        } else {
            vertices = new double[crs.getCoordinateSystem().getDimension()];
            if(vertices.length > 0){
                vertices[0] = c.x;
                if(vertices.length > 1){
                    vertices[1] = c.y;
                    if(vertices.length > 2){
                        vertices[2] = c.z;
                    }
                }
            }
        }

        return pf.createDirectPosition(vertices);
    }

    /**
     * Extracts the values of a JTS coordinate into an existing DirectPosition
     * object.
     */
    public static void coordinateToDirectPosition(final com.vividsolutions.jts.geom.Coordinate c,
            final DirectPosition result) {
        // Get the CRS so we can figure out the dimension of the result.
        CoordinateReferenceSystem crs = result.getCoordinateReferenceSystem();
        int d;

        if (crs != null) {
            d = crs.getCoordinateSystem().getDimension();
        } else {
            // If the result DP has no CRS, then we just assume 2 dimensions.
            // This could result in IndexOutOfBounds exceptions if the DP has
            // fewer than 2 coordinates.
            d = 2;
        }
        final CoordinateSystem cs = crs.getCoordinateSystem();

        if (d >= 1) {
            int xIndex = GeometryUtils.getDirectedAxisIndex(cs, AxisDirection.EAST);
            result.setOrdinate(xIndex, c.x);//0
            if (d >= 2) {
                int yIndex = GeometryUtils.getDirectedAxisIndex(cs, AxisDirection.NORTH);
                result.setOrdinate(yIndex, c.y);//1
                if (d >= 3) {
                    int zIndex = GeometryUtils.getDirectedAxisIndex(cs, AxisDirection.UP);
                    result.setOrdinate(zIndex, c.z);//2
                    // If d > 3, then the remaining ordinates of the DP are
                    // (so far) left with their original values.  So we init
                    // them to zero here.
                    if (d > 3) {
                        for (int i = 3; i < d; i++) {
                            result.setOrdinate(i, 0.0);
                        }
                    }
                }
            }
        }
    }

    /**
     * Converts a JTS Point to a DirectPosition with the given CRS.
     */
    public static DirectPosition pointToDirectPosition(final com.vividsolutions.jts.geom.Point p,
            final CoordinateReferenceSystem crs) {
        return coordinateToDirectPosition(p.getCoordinate(), crs);
    }

    public static Ring linearRingToRing(final com.vividsolutions.jts.geom.LineString jtsLinearRing,
            final CoordinateReferenceSystem crs) {
        int numPoints = jtsLinearRing.getNumPoints();
        if (numPoints != 0 && !jtsLinearRing.getCoordinateN(0).equals(jtsLinearRing.getCoordinateN(numPoints - 1))) {
            throw new IllegalArgumentException("LineString must be a ring");
        }

        PrimitiveFactory pf = new JTSPrimitiveFactory(crs); //FactoryFinder.getPrimitiveFactory(hints);
        GeometryFactory gf = new JTSGeometryFactory(crs); //FactoryFinder.getGeometryFactory(hints);

        LineString ls = gf.createLineString(new ArrayList());
        List pointList = ls.getControlPoints().positions();
        for (int i = 0; i < numPoints; i++) {
            pointList.add(coordinateToDirectPosition(jtsLinearRing.getCoordinateN(i), crs));
        }
        Curve curve = pf.createCurve(new ArrayList());
        // Cast below can be removed when GeoAPI will be allowed to abandon Java 1.4 support.
        ((List) curve.getSegments()).add(ls);
        Ring result = pf.createRing(new ArrayList());
        // Cast below can be removed when GeoAPI will be allowed to abandon Java 1.4 support.
        ((List) result.getGenerators()).add(curve);
        return result;
    }

    /**
     * Computes the distance between two JTS geometries.  Unfortunately, JTS's
     * methods do not allow for either parameter to be a collection.  So we have
     * to implement the logic of dealing with collection geometries separately.
     */
    public static double distance(final com.vividsolutions.jts.geom.Geometry g1,
            final com.vividsolutions.jts.geom.Geometry g2) {
        if (g1 instanceof com.vividsolutions.jts.geom.GeometryCollection) {
            double minDistance = Double.POSITIVE_INFINITY;
            com.vividsolutions.jts.geom.GeometryCollection gc1 =
                    (com.vividsolutions.jts.geom.GeometryCollection) g1;
            int n = gc1.getNumGeometries();
            for (int i = 0; i < n; i++) {
                double d = distance(gc1.getGeometryN(i), g2);
                if (d < minDistance) {
                    minDistance = d;
                }
            }
            return minDistance;
        } else if (g2 instanceof com.vividsolutions.jts.geom.GeometryCollection) {
            double minDistance = Double.POSITIVE_INFINITY;
            com.vividsolutions.jts.geom.GeometryCollection gc2 =
                    (com.vividsolutions.jts.geom.GeometryCollection) g2;
            int n = gc2.getNumGeometries();
            for (int i = 0; i < n; i++) {
                // This call will result in a redundant check of
                // g1 instanceof GeometryCollection.  Maybe we oughta fix that
                // somehow.
                double d = distance(g1, gc2.getGeometryN(i));
                if (d < minDistance) {
                    minDistance = d;
                }
            }
            return minDistance;
        } else {
            return g1.distance(g2);
        }
    }

    /**
     * Returns the union of the two geometries.  In the case of primitive
     * geometries, this simply delegates to the JTS method.  In the case of
     * aggregates, creates an aggregate containing all the parts of both.
     */
    public static com.vividsolutions.jts.geom.Geometry union(
            final com.vividsolutions.jts.geom.Geometry g1,
            final com.vividsolutions.jts.geom.Geometry g2) {
        return null;
    }

    public static com.vividsolutions.jts.geom.Geometry intersection(
            final com.vividsolutions.jts.geom.Geometry g1,
            final com.vividsolutions.jts.geom.Geometry g2) {
        return null;
    }

    public static com.vividsolutions.jts.geom.Geometry difference(
            final com.vividsolutions.jts.geom.Geometry g1,
            final com.vividsolutions.jts.geom.Geometry g2) {
        return null;
    }

    public static com.vividsolutions.jts.geom.Geometry symmetricDifference(
            final com.vividsolutions.jts.geom.Geometry g1,
            final com.vividsolutions.jts.geom.Geometry g2) {
        return null;
    }

    public static boolean contains(
            final com.vividsolutions.jts.geom.Geometry g1,
            final com.vividsolutions.jts.geom.Geometry g2) {
        return false;
    }

    public static boolean equals(
            final com.vividsolutions.jts.geom.Geometry g1,
            final com.vividsolutions.jts.geom.Geometry g2) {
        return false;
    }

    /**
     * Returns true if the two given geometries intersect.  In the case of
     * primitive geometries, this simply delegates to the JTS method.  In the
     * case of Aggregates, loops over pairs of children looking for
     * intersections.
     */
    public static boolean intersects(
            final com.vividsolutions.jts.geom.Geometry g1,
            final com.vividsolutions.jts.geom.Geometry g2) {
        if (g1 instanceof com.vividsolutions.jts.geom.GeometryCollection) {
            com.vividsolutions.jts.geom.GeometryCollection gc1 =
                    (com.vividsolutions.jts.geom.GeometryCollection) g1;
            int n = gc1.getNumGeometries();
            for (int i = 0; i < n; i++) {
                com.vividsolutions.jts.geom.Geometry g = gc1.getGeometryN(i);
                if (intersects(g, g2)) {
                    return true;
                }
            }
            return false;
        } else if (g2 instanceof com.vividsolutions.jts.geom.GeometryCollection) {
            com.vividsolutions.jts.geom.GeometryCollection gc2 =
                    (com.vividsolutions.jts.geom.GeometryCollection) g2;
            int n = gc2.getNumGeometries();
            for (int i = 0; i < n; i++) {
                com.vividsolutions.jts.geom.Geometry g = gc2.getGeometryN(i);
                if (intersects(g1, g)) {
                    return true;
                }
            }
            return false;
        } else {
            return g1.intersects(g2);
        }
    }

    /**
     * Creates a JTS LineString from the four corners of the specified Envelope.
     * @param envelope The Envelope to be converted
     * @return A JTS Geometry 
     */
    public static com.vividsolutions.jts.geom.Geometry getEnvelopeGeometry(
            final Envelope envelope) {
        // PENDING(NL): Add code to check for CRS compatibility
        // Must consider possibility that this is a pixel envelope
        // rather than geo coordinate; only way to be sure is to check Units
        DirectPosition topCorner = envelope.getUpperCorner();
        DirectPosition botCorner = envelope.getLowerCorner();
        DirectPosition topLeft = new GeneralDirectPosition(topCorner);
        DirectPosition botRight = new GeneralDirectPosition(botCorner);

        //Again, making assumption we can ignore this LatLonAlt stuff - colin

        /*
        // If the Envelope coordinates are LatLonAlts,
        // calling setOrdinate causes Error-level logging messages,
        // including a stack trace,
        // though it still works.  But in principal we should
        // call get/setLat and get/setLon instead if we have LatLonAlts
        if (topLeft instanceof LatLonAlt && botRight instanceof LatLonAlt) {
        ((LatLonAlt) topLeft).setLon(((LatLonAlt)
        botCorner).getLon(NonSI.DEGREE_ANGLE), NonSI.DEGREE_ANGLE);
        ((LatLonAlt) botRight).setLon(((LatLonAlt)
        topCorner).getLon(NonSI.DEGREE_ANGLE), NonSI.DEGREE_ANGLE);
        } else {*/

        topLeft.setOrdinate(1, botCorner.getOrdinate(1));
        botRight.setOrdinate(1, topCorner.getOrdinate(1));

        //}//end of else statment associated with above LatLongAlt stuff
        // Create a JTS Envelope
        Coordinate jtsTopRight =
                JTSUtils.directPositionToCoordinate(topCorner);
        Coordinate jtsTopLeft =
                JTSUtils.directPositionToCoordinate(topLeft);
        Coordinate jtsBotLeft =
                JTSUtils.directPositionToCoordinate(botCorner);
        Coordinate jtsBotRight =
                JTSUtils.directPositionToCoordinate(botRight);

        com.vividsolutions.jts.geom.Geometry jtsEnv = GEOMETRY_FACTORY.createLineString(
                new Coordinate[]{jtsTopLeft, jtsTopRight, jtsBotRight, jtsBotLeft,
                    jtsTopLeft}).getEnvelope();
        return jtsEnv;
    }
}

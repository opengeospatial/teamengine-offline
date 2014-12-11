/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2009-2012, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotoolkit.gml;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.io.Reader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.geotoolkit.geometry.jts.JTS;

import org.geotoolkit.gml.xml.*;
import org.geotoolkit.gml.xml.v311.ArcByCenterPointType;
import org.geotoolkit.gml.xml.v311.ArcStringByBulgeType;
import org.geotoolkit.gml.xml.v311.ArcStringType;
import org.geotoolkit.gml.xml.v311.BSplineType;
import org.geotoolkit.gml.xml.v311.ClothoidType;
import org.geotoolkit.gml.xml.v311.CubicSplineType;
import org.geotoolkit.gml.xml.v311.GeodesicStringType;
import org.geotoolkit.gml.xml.v311.OffsetCurveType;
import org.geotoolkit.referencing.CRS;
import org.geotoolkit.util.logging.Logging;

import org.opengis.util.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author Johann sorel (Geomatys)
 * @module pending
 */
public class GeometrytoJTS {

    private static final Logger LOGGER = Logging.getLogger(GeometrytoJTS.class);
    private static final GeometryFactory GF = new GeometryFactory();

    private GeometrytoJTS(){}

    private static CoordinateReferenceSystem toCRS(String name) throws FactoryException{
        return CRS.decode(name, true);
    }

    /**
     * Unmarshall given GML String and transform it in a JTS geometry.
     * 
     * @param gmlString
     * @return
     * @throws JAXBException
     * @throws FactoryException 
     */
    public static Geometry toJTS(String gmlString) throws JAXBException, FactoryException{
        final Reader reader = new StringReader(gmlString);
        
        Unmarshaller unmarshaller = null;
        final Geometry geom;
        try{
            unmarshaller = GMLMarshallerPool.getInstance().acquireUnmarshaller();
            Object jax = unmarshaller.unmarshal(reader);
            
            if(jax instanceof JAXBElement){
                jax = ((JAXBElement)jax).getValue();
            }
            
            if(jax instanceof AbstractGeometry){
                geom = GeometrytoJTS.toJTS((AbstractGeometry)jax);
            }else{
                throw new JAXBException("Object is not a valid GML "+jax);
            }
        }finally{
            if(unmarshaller != null){
                GMLMarshallerPool.getInstance().release(unmarshaller);
            }
        }
        return geom;
    }
    
    /**
     * Transform A GML envelope into JTS Polygon
     *
     * @param GMLenvelope A GML envelope.
     *
     * @return A JTS Polygon
     * @throws org.opengis.referencing.NoSuchAuthorityCodeException
     * @throws org.opengis.util.FactoryException
     */
    public static com.vividsolutions.jts.geom.Polygon toJTS(final Envelope gmlEnvelope)
            throws NoSuchAuthorityCodeException, FactoryException {
        final String crsName = gmlEnvelope.getSrsName();
        if (crsName == null) {
            throw new IllegalArgumentException("An operator BBOX must specified a CRS (coordinate Reference system) for the envelope.");
        }

        final double[] min = gmlEnvelope.getLowerCorner().getCoordinate();
        final double[] max = gmlEnvelope.getUpperCorner().getCoordinate();

        final LinearRing ring = GF.createLinearRing(new Coordinate[]{
            new Coordinate(min[0], min[1]),
            new Coordinate(min[0], max[1]),
            new Coordinate(max[0], max[1]),
            new Coordinate(max[0], min[1]),
            new Coordinate(min[0], min[1])
        });
        final com.vividsolutions.jts.geom.Polygon polygon = GF.createPolygon(ring, new LinearRing[0]);
        final CoordinateReferenceSystem crs = toCRS(crsName);
        JTS.setCRS(polygon, crs);

        return polygon;
    }

    public static Geometry toJTS(final AbstractGeometry gml)
            throws NoSuchAuthorityCodeException, FactoryException{

        if (gml instanceof org.geotoolkit.gml.xml.Point){
            return toJTS((org.geotoolkit.gml.xml.Point)gml, null);
        } else if(gml instanceof org.geotoolkit.gml.xml.LineString){
            return toJTS((org.geotoolkit.gml.xml.LineString)gml);
        } else if(gml instanceof org.geotoolkit.gml.xml.Polygon){
            return toJTS((org.geotoolkit.gml.xml.Polygon)gml);


        } else if(gml instanceof org.geotoolkit.gml.xml.MultiPoint){
            return toJTS((org.geotoolkit.gml.xml.MultiPoint)gml);
        } else if(gml instanceof org.geotoolkit.gml.xml.MultiLineString){
            return toJTS((org.geotoolkit.gml.xml.MultiLineString)gml);
        } else if(gml instanceof org.geotoolkit.gml.xml.MultiCurve){
            return toJTS((org.geotoolkit.gml.xml.MultiCurve)gml);
        } else if(gml instanceof org.geotoolkit.gml.xml.MultiPolygon){
            return toJTS((org.geotoolkit.gml.xml.MultiPolygon)gml);
        } else if(gml instanceof MultiSurface){
            return toJTS((MultiSurface)gml);

        } else if(gml instanceof org.geotoolkit.gml.xml.LinearRing){
            return toJTS((org.geotoolkit.gml.xml.LinearRing)gml);

        } else {
            throw new IllegalArgumentException("Unssupported geometry type : " + gml);
        }

    }

    public static Point toJTS(final org.geotoolkit.gml.xml.Point gmlPoint, final CoordinateReferenceSystem parentCRS) throws NoSuchAuthorityCodeException, FactoryException{
        final String crsName;
        if (parentCRS == null) {
            crsName = gmlPoint.getSrsName();

            /*if (crsName == null) {
                throw new IllegalArgumentException("A GML point must specify Coordinate Reference System.");
            }*/
        } else {
            crsName = null;
        }

        //we get the coordinate of the point (if they are present)
        if (gmlPoint.getCoordinates() == null && gmlPoint.getPos() == null) {
            throw new IllegalArgumentException("A GML point must specify coordinates or direct position.");
        }

        final double[] coordinates = new double[2];
        if (gmlPoint.getCoordinates() != null) {
            final String coord = gmlPoint.getCoordinates().getValue();

            final StringTokenizer tokens = new StringTokenizer(coord, ",");
            int index = 0;
            while (tokens.hasMoreTokens()) {
                final double value = parseDouble(tokens.nextToken());
                if (index >= coordinates.length) {
                    throw new IllegalArgumentException("This service support only 2D point.");
                }
                coordinates[index++] = value;
            }
        } else if (gmlPoint.getPos().getValue() != null && gmlPoint.getPos().getValue().size() == 2){
            coordinates[0] = gmlPoint.getPos().getValue().get(0);
            coordinates[1] = gmlPoint.getPos().getValue().get(1);
        } else {
            throw new IllegalArgumentException("The GML point is malformed.");
        }

        final com.vividsolutions.jts.geom.Point pt = GF.createPoint(new Coordinate(coordinates[0], coordinates[1]));
        final CoordinateReferenceSystem crs;
        if (crsName != null) {
            crs = toCRS(crsName);
        } else {
            crs = parentCRS;
        }
        if (crs != null) {
            JTS.setCRS(pt, crs);
        }
        return pt;
    }

    public static Polygon toJTS(final org.geotoolkit.gml.xml.Polygon gml) throws FactoryException{
        final AbstractRingProperty ext = gml.getExterior();
        final List<? extends AbstractRingProperty> ints = gml.getInterior();

        final LinearRing exterior = toJTS(ext.getAbstractRing());
        final LinearRing[] holes = new LinearRing[ints.size()];
        for(int i=0;i<holes.length;i++){
            holes[i] = toJTS(ints.get(i).getAbstractRing());
        }

        final Polygon polygon = GF.createPolygon(exterior, holes);

        final CoordinateReferenceSystem crs = gml.getCoordinateReferenceSystem();        
        JTS.setCRS(polygon, crs);
        return polygon;
    }

    public static LineString toJTS(final org.geotoolkit.gml.xml.LineString gmlLine) throws FactoryException{
        final String crsName = gmlLine.getSrsName();
        if (crsName == null) {
            throw new FactoryException("A CRS (coordinate Reference system) must be specified for the line.");
        }
        final CoordinateReferenceSystem crs = toCRS(crsName);

        final com.vividsolutions.jts.geom.LineString ls;
        final Coordinates coord = gmlLine.getCoordinates();
        if(coord != null){
            final List<Double> values = coord.getValues();
            final Coordinate[] coordinates = new Coordinate[values.size() / 2];
            if (values != null && !values.isEmpty()) {
                int cpt = 0;
                for (int i=0; i < values.size(); i = i + 2) {
                    coordinates[cpt] = new Coordinate(values.get(i), values.get(i + 1));
                    cpt++;
                }
            }
            ls = GF.createLineString(coordinates);
        } else if (gmlLine.getPosList() != null) {
            final DirectPositionList dplt = gmlLine.getPosList();
            final int dim = gmlLine.getCoordinateDimension();
            final List<Coordinate> coords = toJTSCoords(dplt, dim);
            ls = GF.createLineString(coords.toArray(new Coordinate[coords.size()]));

        } else {
            final List<Coordinate> coords = toJTSCoords(gmlLine.getPos());
            ls = GF.createLineString(coords.toArray(new Coordinate[coords.size()]));
        }
        
        JTS.setCRS(ls, crs);
        return ls;
    }

    public static List<LineString> toJTS(final Curve gmlLine) throws FactoryException{
        final String crsName = gmlLine.getSrsName();
        if (crsName == null) {
            throw new FactoryException("A CRS (coordinate Reference system) must be specified for the line.");
        }
        final CoordinateReferenceSystem crs = toCRS(crsName);
        
        final List<com.vividsolutions.jts.geom.LineString> lineList = new ArrayList<com.vividsolutions.jts.geom.LineString>();
        final CurveSegmentArrayProperty arrayProperty = gmlLine.getSegments();
        List<? extends AbstractCurveSegment> segments = arrayProperty.getAbstractCurveSegment();
        for (AbstractCurveSegment segment : segments) {
            if (segment instanceof LineStringSegment) {
                final LineStringSegment lineSegment = (LineStringSegment) segment;
                final com.vividsolutions.jts.geom.LineString ls;
                final Coordinates coord = lineSegment.getCoordinates();
                if (coord != null) {
                    String s = coord.getValue();
                    final String cs;
                    if (coord.getCs() == null) {
                        cs = ",";
                    } else {
                        cs = coord.getCs();
                    }
                    int csIndex = s.indexOf(cs);
                    double x1 = Double.parseDouble(s.substring(0, csIndex));

                    final String ts;
                    if (coord.getTs() == null) {
                        ts = " ";
                    } else {
                        ts = coord.getTs();
                    }

                    int tsIndex = s.indexOf(ts, csIndex);
                    double y1 = Double.parseDouble(s.substring(csIndex + 1, tsIndex));

                    csIndex = s.indexOf(cs, tsIndex + 1);
                    double x2 = Double.parseDouble(s.substring(tsIndex + 1, csIndex));
                    double y2 = Double.parseDouble(s.substring(csIndex + 1));
                    ls = GF.createLineString(new Coordinate[]{
                                new Coordinate(x1, y1),
                                new Coordinate(x2, y2)
                            });
                } else if (lineSegment.getPosList() != null){
                    final DirectPositionList dplt = lineSegment.getPosList();
                    final int dim = gmlLine.getCoordinateDimension();
                    final List<Coordinate> coords = toJTSCoords(dplt, dim);
                    ls = GF.createLineString(coords.toArray(new Coordinate[coords.size()]));
                } else {
                    final List<Coordinate> coords = toJTSCoords(lineSegment.getPos());
                    ls = GF.createLineString(coords.toArray(new Coordinate[coords.size()]));
                }
                JTS.setCRS(ls, crs);
                lineList.add(ls);
            } else {
                throw new IllegalArgumentException("only lineStringSegment are allowed in curveType segments");
            }
        }
        return lineList;
    }

    public static List<Coordinate> toJTSCoords(List<? extends DirectPosition> pos){
        final List<Coordinate> coords = new ArrayList<Coordinate>();

        for(DirectPosition dp : pos){
            coords.add( new Coordinate(dp.getOrdinate(0), dp.getOrdinate(1)));
        }
        return coords;
    }

    public static List<Coordinate> toJTSCoords(final DirectPositionList lst, final int dim){
        final List<Double> values = lst.getValue();
        final List<Coordinate> coords = new ArrayList<Coordinate>();

        for(int i=0,n=values.size();i<n;i+=dim){
            coords.add( new Coordinate(values.get(i), values.get(i+1)) );
        }
        return coords;
    }

    public static MultiPoint toJTS(final org.geotoolkit.gml.xml.MultiPoint gml) throws NoSuchAuthorityCodeException, FactoryException{
        final List<? extends PointProperty> pos = gml.getPointMember();
        final Point[] members = new Point[pos.size()];

        final CoordinateReferenceSystem crs = gml.getCoordinateReferenceSystem();

        for(int i=0,n=pos.size(); i<n; i++){
            members[i] = toJTS(pos.get(i).getPoint(), crs);
        }

        final MultiPoint geom = GF.createMultiPoint(members);
        JTS.setCRS(geom, crs);
        return geom;
    }

    public static MultiLineString toJTS(final org.geotoolkit.gml.xml.MultiLineString gml) throws FactoryException{
        final List<? extends LineStringProperty> pos = gml.getLineStringMember();
        final LineString[] members = new LineString[pos.size()];

        for(int i=0,n=pos.size(); i<n; i++){
            members[i] = toJTS(pos.get(i).getLineString());
        }

        final MultiLineString geom = GF.createMultiLineString(members);

        final CoordinateReferenceSystem crs = gml.getCoordinateReferenceSystem();
        JTS.setCRS(geom, crs);
        return geom;
    }

    public static MultiLineString toJTS(final org.geotoolkit.gml.xml.MultiCurve gml) throws FactoryException{
        final List<? extends CurveProperty> pos = gml.getCurveMember();
        final List<LineString> members = new ArrayList<LineString>();

        for (int i=0,n=pos.size(); i<n; i++) {
            AbstractCurve curve = pos.get(i).getAbstractCurve();
            if (curve instanceof org.geotoolkit.gml.xml.LineString) {
                members.add(toJTS((org.geotoolkit.gml.xml.LineString)curve));
            } else if (curve instanceof org.geotoolkit.gml.xml.Curve) {
                members.addAll(toJTS((org.geotoolkit.gml.xml.Curve)curve));
            } else {
                throw new IllegalArgumentException("unexpected Curve type:" + curve);
            }
        }

        final MultiLineString geom = GF.createMultiLineString(members.toArray(new LineString[members.size()]));

        final CoordinateReferenceSystem crs = gml.getCoordinateReferenceSystem();
        JTS.setCRS(geom, crs);
        return geom;
    }

    public static MultiPolygon toJTS(final org.geotoolkit.gml.xml.MultiPolygon gml) throws FactoryException{
        final List<? extends PolygonProperty> pos = gml.getPolygonMember();
        final Polygon[] members = new Polygon[pos.size()];

        for (int i=0,n=pos.size(); i<n; i++) {
            members[i] = toJTS(pos.get(i).getPolygon());
        }

        final MultiPolygon geom = GF.createMultiPolygon(members);

        final CoordinateReferenceSystem crs = gml.getCoordinateReferenceSystem();
        JTS.setCRS(geom, crs);
        return geom;
    }

    public static MultiPolygon toJTS(final MultiSurface gml) throws FactoryException{
        final List<? extends SurfaceProperty> pos = gml.getSurfaceMember();
        final Polygon[] members = new Polygon[pos.size()];

        for (int i=0,n=pos.size(); i<n; i++) {
            members[i] = toJTS((org.geotoolkit.gml.xml.Polygon)pos.get(i).getAbstractSurface());
        }

        final MultiPolygon geom             = GF.createMultiPolygon(members);
        final CoordinateReferenceSystem crs = gml.getCoordinateReferenceSystem();
        JTS.setCRS(geom, crs);
        return geom;
    }

    public static LinearRing toJTS(final AbstractRing gml) throws FactoryException{
        if(gml instanceof org.geotoolkit.gml.xml.LinearRing){
            return toJTS((org.geotoolkit.gml.xml.LinearRing)gml);
        }else if(gml instanceof Ring){
            return toJTS((Ring)gml);
        }else{
            LOGGER.log(Level.WARNING, "Unssupported geometry type : {0}", gml);
            return GF.createLinearRing(new Coordinate[]{new Coordinate(0, 0),new Coordinate(0, 0),
            new Coordinate(0, 0),new Coordinate(0, 0)});
        }

    }

    public static LinearRing toJTS(final org.geotoolkit.gml.xml.LinearRing gml){
        final List<Double> values;
        final DirectPositionList lst = gml.getPosList();
        final Coordinates cds        = gml.getCoordinates();
        if (lst != null) {
            values = lst.getValue();
        } else if (cds != null) {
            values = cds.getValues();
        } else {
            values = new ArrayList<Double>();
            LOGGER.warning("no coordinates available for linear ring");
        }


        final int dim = gml.getCoordinateDimension();
        final List<Coordinate> coords = new ArrayList<Coordinate>(dim);
        for (int i=0,n=values.size(); i<n; i+=dim) {
            coords.add( new Coordinate(values.get(i), values.get(i+1)) );
        }

        final LinearRing ring = GF.createLinearRing(coords.toArray(new Coordinate[coords.size()]));

        final CoordinateReferenceSystem crs = gml.getCoordinateReferenceSystem();
        JTS.setCRS(ring, crs);
        return ring;
    }

    public static LinearRing toJTS(final Ring gml) throws FactoryException {

        final LinkedList<Coordinate> coords = new LinkedList<Coordinate>();

        for (CurveProperty cpt :gml.getCurveMember()){
            AbstractCurve act = cpt.getAbstractCurve();

            if (act instanceof Curve) {
                final Curve ct = (Curve) act;
                for(AbstractCurveSegment acst : ct.getSegments().getAbstractCurveSegment()){

                    if(acst instanceof ClothoidType){
                        throw new IllegalArgumentException("ClothoidType not supported yet");
                    } else if (acst instanceof BSplineType){
                        throw new IllegalArgumentException("BSplineType not supported yet");
                    } else if (acst instanceof CubicSplineType){
                        throw new IllegalArgumentException("CubicSplineType not supported yet");
                    } else if (acst instanceof GeodesicStringType){
                        throw new IllegalArgumentException("GeodesicStringType not supported yet");
                    } else if (acst instanceof LineStringSegment){
                        final LineStringSegment lsst = (LineStringSegment)acst;

                        if (lsst.getPosList() != null) {
                            final DirectPositionList dpst = lsst.getPosList();
                            final int dim = ct.getCoordinateDimension();
                            final List<Coordinate> plots = toJTSCoords(dpst, dim);

                            if(coords.isEmpty()){
                                for(Coordinate c : plots){
                                    coords.add(c);
                                }
                            } else {
                                if (coords.getLast().equals(plots.get(0))) {
                                    for (int i=1; i<plots.size();i++) {
                                        coords.add(plots.get(i));
                                    }
                                } else {
                                    for (Coordinate c : plots) {
                                        coords.add(c);
                                    }
                                }
                            }
                        } else {

                            if(coords.isEmpty()){
                                for (DirectPosition pos : lsst.getPos()) {
                                    coords.add(new Coordinate(pos.getOrdinate(0), pos.getOrdinate(1)));
                                }
                                if (!lsst.getRest().isEmpty()) {
                                    throw new IllegalArgumentException("not supported yet");
                                }
                            } else {
                                for(DirectPosition pos : lsst.getPos()){
                                    final Coordinate c = new Coordinate(pos.getOrdinate(0), pos.getOrdinate(1));
                                    if (!c.equals2D(coords.getLast())){
                                        coords.add(c);
                                    }
                                }
                                if (!lsst.getRest().isEmpty()) {
                                    throw new IllegalArgumentException("not supported yet");
                                }
                            }
                        }

                    } else if (acst instanceof ArcByCenterPointType){
                        throw new IllegalArgumentException("not supported yet");
                    } else if (acst instanceof ArcStringType){
                        throw new IllegalArgumentException("not supported yet");
                    } else if (acst instanceof OffsetCurveType){
                        throw new IllegalArgumentException("not supported yet");
                    } else if (acst instanceof ArcStringByBulgeType){
                        throw new IllegalArgumentException("not supported yet");
                    } else{
                        throw new IllegalArgumentException("not supported yet:" + acst);
                    }
                }
            } else if(act instanceof org.geotoolkit.gml.xml.LineString) {
                final LineString ls = toJTS((org.geotoolkit.gml.xml.LineString)act);
                final Coordinate[] plots = ls.getCoordinates();

                if(coords.isEmpty()) {
                    for(Coordinate c : plots){
                        coords.add(c);
                    }
                } else {
                    if( coords.getLast().equals(plots[0]) ){
                        for(int i=1; i<plots.length;i++){
                            coords.add(plots[i]);
                        }
                    }else{
                        for(Coordinate c : plots){
                            coords.add(c);
                        }
                    }
                }
            } else if(act instanceof OrientableCurve) {
                throw new IllegalArgumentException("not supported yet");
            }
        }

        final LinearRing ring = GF.createLinearRing(coords.toArray(new Coordinate[coords.size()]));

        final CoordinateReferenceSystem crs = gml.getCoordinateReferenceSystem();
        JTS.setCRS(ring, crs);
        return ring;
    }


    /**
     * Parses a value as a floating point.
     *
     * @throws CstlServiceException if the value can't be parsed.
     */
    private static double parseDouble(String value) {
        value = value.trim();
        return Double.parseDouble(value);
    }

}

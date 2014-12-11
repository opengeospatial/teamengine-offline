package com.galdosinc.glib.gml.schema;

import com.galdosinc.glib.xml.QName;

public abstract interface GmlConstants
{
  public static final String GML_NS_URI = "http://www.opengis.net/gml";
  public static final String XLINK_NS_URI = "http://www.w3.org/1999/xlink";
  public static final String CLASSICAL_GML_PREFIX = "gml";
  public static final String CLASSICAL_XLINK_PREFIX = "xlink";
  public static final String ABS_GML_ELEMENT_NAME = "_GML";
  public static final String ABSTRACT_GML_TYPE_NAME = "AbstractGMLType";
  public static final int UNKNOWN_GEOMETRY_CODE = -1;
  public static final int POINT_CODE = 0;
  public static final int POLYGON_CODE = 1;
  public static final int LINESTRING_CODE = 2;
  public static final int LINEARRING_CODE = 3;
  public static final int MULTIPOINT_CODE = 4;
  public static final int MULTIPOLYGON_CODE = 5;
  public static final int MULTILINESTRING_CODE = 6;
  public static final int BOX_CODE = 7;
  public static final int ARC_BY_CENTER_POINT_CODE = 8;
  public static final int CIRCLE_CODE = 9;
  public static final int MULTISURFACE_CODE = 10;
  public static final int COMPOSITE_SURFACE_CODE = 11;
  public static final int CUBIC_SPLINE_CODE = 12;
  public static final int ARC_STRING_BY_BULGE_CODE = 13;
  public static final int RING_CODE = 14;
  public static final int COMPOSITE_CURVE_CODE = 15;
  public static final int ORIENTABLE_SURFACE_CODE = 16;
  public static final int BSPLINE_CODE = 17;
  public static final int GEOMETRIC_COMPLEX_CODE = 18;
  public static final int ORIENTABLE_CURVE_CODE = 19;
  public static final int ARC_CODE = 20;
  public static final int CIRCLE_BY_CENTER_POINT_CODE = 21;
  public static final int BEZIER_CODE = 22;
  public static final int SOLID_CODE = 23;
  public static final int ENVELOPE_CODE = 24;
  public static final int ARC_STRING_CODE = 25;
  public static final int COMPOSITE_SOLID_CODE = 26;
  public static final int ARC_BY_BULGE_CODE = 27;
  public static final int MULTISOLID_CODE = 28;
  public static final int MULTICURVE_CODE = 29;
  public static final int LINESTRING_SEGMENT_CODE = 30;
  public static final int SURFACE_CODE = 31;
  public static final int MULTIGEOMETRY_CODE = 32;
  public static final int ABS_GEOMETRY_CODE = 33;
  public static final int ABS_GEOMETRY_COLLECTION_CODE = 34;
  public static final int ABS_CURVE_CODE = 35;
  public static final int ABS_SURFACE_CODE = 36;
  public static final int ABS_RING_CODE = 37;
  public static final int ABS_GEOMETRIC_PRIMITIVE_CODE = 38;
  public static final int ABS_CURVE_SEGMENT_CODE = 39;
  public static final int ABS_GEOMETRIC_AGGREGATE_CODE = 40;
  public static final int UNKNOWN_TOPOLOGY_CODE = -1;
  public static final int NODE_CODE = 0;
  public static final int EDGE_CODE = 1;
  public static final int FACE_CODE = 2;
  public static final int TOPO_SOLID_CODE = 3;
  public static final int TOPO_POINT_CODE = 4;
  public static final int TOPO_CURVE_CODE = 5;
  public static final int TOPO_SURFACE_CODE = 6;
  public static final int TOPO_VOLUME_CODE = 7;
  public static final int TOPO_COMPLEX_CODE = 8;
  public static final int ABS_TOPOLOGY_CODE = 9;
  public static final int ABS_TOPO_PRIMITIVE_CODE = 10;
  public static final String ABS_GEOMETRY_ELEMENT_NAME = "_Geometry";
  public static final String ABS_GEOMETRY_COLLECTION_ELEMENT_NAME = "_GeometryCollection";
  public static final String ABS_SURFACE_ELEMENT_NAME = "_Surface";
  public static final String ABS_RING_ELEMENT_NAME = "_Ring";
  public static final String ABS_GEOMETRIC_PRIMITIVE_ELEMENT_NAME = "_GeometricPrimitive";
  public static final String ABS_CURVE_ELEMENT_NAME = "_Curve";
  public static final String ABS_CURVE_SEGMENT_ELEMENT_NAME = "_CurveSegment";
  public static final String ABS_GEOMETRIC_AGGREGATE_ELEMENT_NAME = "_GeometricAggregate";
  public static final String POINT_ELEMENT_NAME = "Point";
  public static final String LINESTRING_ELEMENT_NAME = "LineString";
  public static final String POLYGON_ELEMENT_NAME = "Polygon";
  public static final String LINEARRING_ELEMENT_NAME = "LinearRing";
  public static final String MULTIPOINT_ELEMENT_NAME = "MultiPoint";
  public static final String MULTILINESTRING_ELEMENT_NAME = "MultiLineString";
  public static final String MULTIPOLYGON_ELEMENT_NAME = "MultiPolygon";
  public static final String MULTIGEOMETRY_ELEMENT_NAME = "MultiGeometry";
  public static final String BOX_ELEMENT_NAME = "Box";
  public static final String ARC_BY_CENTER_POINT_ELEMENT_NAME = "ArcByCenterPoint";
  public static final String CIRCLE_ELEMENT_NAME = "Circle";
  public static final String MULTISURFACE_ELEMENT_NAME = "MultiSurface";
  public static final String COMPOSITE_SURFACE_ELEMENT_NAME = "CompositeSurface";
  public static final String CUBIC_SPLINE_ELEMENT_NAME = "CubicSpline";
  public static final String ARC_STRING_BY_BULGE_ELEMENT_NAME = "ArcStringByBulge";
  public static final String RING_ELEMENT_NAME = "Ring";
  public static final String COMPOSITE_CURVE_ELEMENT_NAME = "CompositeCurve";
  public static final String ORIENTABLE_SURFACE_ELEMENT_NAME = "OrientableSurface";
  public static final String BSPLINE_ELEMENT_NAME = "BSpline";
  public static final String GEOMETRIC_COMPLEX_ELEMENT_NAME = "GeometricComplex";
  public static final String ORIENTABLE_CURVE_ELEMENT_NAME = "OrientableCurve";
  public static final String ARC_ELEMENT_NAME = "Arc";
  public static final String CIRCLE_BY_CENTER_POINT_ELEMENT_NAME = "CircleByCenterPoint";
  public static final String BEZIER_ELEMENT_NAME = "Bezier";
  public static final String SOLID_ELEMENT_NAME = "Solid";
  public static final String ENVELOPE_ELEMENT_NAME = "Envelope";
  public static final String ARC_STRING_ELEMENT_NAME = "ArcString";
  public static final String COMPOSITE_SOLID_ELEMENT_NAME = "CompositeSolid";
  public static final String ARC_BY_BULGE_ELEMENT_NAME = "ArcByBulge";
  public static final String MULTISOLID_ELEMENT_NAME = "MultiSolid";
  public static final String MULTICURVE_ELEMENT_NAME = "MultiCurve";
  public static final String LINESTRING_SEGMENT_ELEMENT_NAME = "LineStringSegment";
  public static final String SURFACE_ELEMENT_NAME = "Surface";
  public static final String ABSTRACT_GEOMETRY_TYPE_NAME = "AbstractGeometryType";
  public static final String ABSTRACT_GEOMETRY_COLLECTION_TYPE_NAME = "AbstractGeometryCollectionType";
  public static final String POINT_TYPE_NAME = "PointType";
  public static final String LINESTRING_TYPE_NAME = "LineStringType";
  public static final String POLYGON_TYPE_NAME = "PolygonType";
  public static final String LINEARRING_TYPE_NAME = "LinearRingType";
  public static final String MULTIPOINT_TYPE_NAME = "MultiPointType";
  public static final String MULTILINESTRING_TYPE_NAME = "MultiLineStringType";
  public static final String MULTIPOLYGON_TYPE_NAME = "MultiPolygonType";
  public static final String MULTIGEOMETRY_TYPE_NAME = "MultiGeometryType";
  public static final String ABSTRACT_SURFACE_TYPE_NAME = "AbstractSurfaceType";
  public static final String ABSTRACT_RING_TYPE_NAME = "AbstractRingType";
  public static final String ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE_NAME = "AbstractGeometricPrimitiveType";
  public static final String ABSTRACT_CURVE_TYPE_NAME = "AbstractCurveType";
  public static final String ABSTRACT_CURVE_SEGMENT_TYPE_NAME = "AbstractCurveSegmentType";
  public static final String ABSTRACT_GEOMETRIC_AGGREGATE_TYPE_NAME = "AbstractGeometricAggregateType";
  public static final String BOX_TYPE_NAME = "BoxType";
  public static final String ARC_BY_CENTER_POINT_TYPE_NAME = "ArcByCenterPointType";
  public static final String CIRCLE_TYPE_NAME = "CircleType";
  public static final String MULTISURFACE_TYPE_NAME = "MultiSurfaceType";
  public static final String COMPOSITE_SURFACE_TYPE_NAME = "CompositeSurfaceType";
  public static final String CUBIC_SPLINE_TYPE_NAME = "CubicSplineType";
  public static final String ARC_STRING_BY_BULGE_TYPE_NAME = "ArcStringByBulgeType";
  public static final String RING_TYPE_NAME = "RingType";
  public static final String COMPOSITE_CURVE_TYPE_NAME = "CompositeCurveType";
  public static final String ORIENTABLE_SURFACE_TYPE_NAME = "OrientableSurfaceType";
  public static final String BSPLINE_TYPE_NAME = "BSplineType";
  public static final String GEOMETRIC_COMPLEX_TYPE_NAME = "GeometricComplexType";
  public static final String ORIENTABLE_CURVE_TYPE_NAME = "OrientableCurveType";
  public static final String ARC_TYPE_NAME = "ArcType";
  public static final String CIRCLE_BY_CENTER_POINT_TYPE_NAME = "CircleByCenterPointType";
  public static final String BEZIER_TYPE_NAME = "BezierType";
  public static final String SOLID_TYPE_NAME = "SolidType";
  public static final String ENVELOPE_TYPE_NAME = "EnvelopeType";
  public static final String ARC_STRING_TYPE_NAME = "ArcStringType";
  public static final String COMPOSITE_SOLID_TYPE_NAME = "CompositeSolidType";
  public static final String ARC_BY_BULGE_TYPE_NAME = "ArcByBulgeType";
  public static final String MULTISOLID_TYPE_NAME = "MultiSolidType";
  public static final String MULTICURVE_TYPE_NAME = "MultiCurveType";
  public static final String LINESTRING_SEGMENT_TYPE_NAME = "LineStringSegmentType";
  public static final String SURFACE_TYPE_NAME = "SurfaceType";
  public static final String POINT_PROPERTY_TYPE_NAME = "PointPropertyType";
  public static final String LINESTRING_PROPERTY_TYPE_NAME = "LineStringPropertyType";
  public static final String POLYGON_PROPERTY_TYPE_NAME = "PolygonPropertyType";
  public static final String MULTIPOINT_PROPERTY_TYPE_NAME = "MultiPointPropertyType";
  public static final String MULTILINESTRING_PROPERTY_TYPE_NAME = "MultiLineStringPropertyType";
  public static final String MULTIPOLYGON_PROPERTY_TYPE_NAME = "MultiPolygonPropertyType";
  public static final String SURFACE_PROPERTY_TYPE_NAME = "SurfacePropertyType";
  public static final String SURFACE_ARRAY_PROPERTY_TYPE_NAME = "SurfaceArrayPropertyType";
  public static final String ABSTRACT_RING_PROPERTY_TYPE_NAME = "AbstractRingPropertyType";
  public static final String LINEAR_RING_PROPERTY_TYPE_NAME = "LinearRingPropertyType";
  public static final String POINT_ARRAY_PROPERTY_TYPE_NAME = "PointArrayPropertyType";
  public static final String CURVE_PROPERTY_TYPE_NAME = "CurvePropertyType";
  public static final String CURVE_ARRAY_PROPERTY_TYPE_NAME = "CurveArrayPropertyType";
  public static final String GEOMETRIC_COMPLEX_PROPERTY_TYPE_NAME = "GeometricComplexPropertyType";
  public static final String CURVE_SEGMENT_ARRAY_PROPERTY_TYPE_NAME = "CurveSegmentArrayPropertyType";
  public static final String MULTIGEOMETRY_PROPERTY_TYPE_NAME = "MultiGeometryPropertyType";
  public static final String MULTICURVE_PROPERTY_TYPE_NAME = "MultiCurvePropertyType";
  public static final String MULTISURFACE_PROPERTY_TYPE_NAME = "MultiSurfacePropertyType";
  public static final String MULTISOLID_PROPERTY_TYPE_NAME = "MultiSolidPropertyType";
  public static final String[] GEOMETRY_TYPE_NAMES = { 
    "PointType", 
    "PolygonType", 
    "LineStringType", 
    "LinearRingType", 
    "MultiPointType", 
    "MultiPolygonType", 
    "MultiLineStringType", 
    "BoxType", 
    "ArcByCenterPointType", 
    "CircleType", 
    "MultiSurfaceType", 
    "CompositeSurfaceType", 
    "CubicSplineType", 
    "ArcStringByBulgeType", 
    "RingType", 
    "CompositeCurveType", 
    "OrientableSurfaceType", 
    "BSplineType", 
    "GeometricComplexType", 
    "OrientableCurveType", 
    "ArcType", 
    "CircleByCenterPointType", 
    "BezierType", 
    "SolidType", 
    "EnvelopeType", 
    "ArcStringType", 
    "CompositeSolidType", 
    "ArcByBulgeType", 
    "MultiSolidType", 
    "MultiCurveType", 
    "LineStringSegmentType", 
    "SurfaceType", 
    "MultiGeometryType", 
    "AbstractGeometryType", 
    "AbstractGeometryCollectionType", 
    "AbstractCurveType", 
    "AbstractSurfaceType", 
    "AbstractRingType", 
    "AbstractGeometricPrimitiveType", 
    "AbstractCurveSegmentType", 
    "AbstractGeometricAggregateType" };

  public static final String[] GEOMETRY_ELEMENT_NAMES = { 
    "Point", 
    "Polygon", 
    "LineString", 
    "LinearRing", 
    "MultiPoint", 
    "MultiPolygon", 
    "MultiLineString", 
    "Box", 
    "ArcByCenterPoint", 
    "Circle", 
    "MultiSurface", 
    "CompositeSurface", 
    "CubicSpline", 
    "ArcStringByBulge", 
    "Ring", 
    "CompositeCurve", 
    "OrientableSurface", 
    "BSpline", 
    "GeometricComplex", 
    "OrientableCurve", 
    "Arc", 
    "CircleByCenterPoint", 
    "Bezier", 
    "Solid", 
    "Envelope", 
    "ArcString", 
    "CompositeSolid", 
    "ArcByBulge", 
    "MultiSolid", 
    "MultiCurve", 
    "LineStringSegment", 
    "Surface", 
    "MultiGeometry", 
    "_Geometry", 
    "_GeometryCollection", 
    "_Curve", 
    "_Surface", 
    "_Ring", 
    "_GeometricPrimitive", 
    "_CurveSegment", 
    "_GeometricAggregate" };

  public static final QName POINT_TYPE_QNAME = new QName("http://www.opengis.net/gml", "PointType");
  public static final QName LINESTRING_TYPE_QNAME = new QName("http://www.opengis.net/gml", "LineStringType");
  public static final QName LINEARRING_TYPE_QNAME = new QName("http://www.opengis.net/gml", "LinearRingType");
  public static final QName POLYGON_TYPE_QNAME = new QName("http://www.opengis.net/gml", "PolygonType");
  public static final QName MULTIPOINT_TYPE_QNAME = new QName("http://www.opengis.net/gml", "MultiPointType");
  public static final QName MULTILINESTRING_TYPE_QNAME = new QName("http://www.opengis.net/gml", "MultiLineStringType");
  public static final QName MULTIPOLYGON_TYPE_QNAME = new QName("http://www.opengis.net/gml", "MultiPolygonType");
  public static final QName MULTIGEOMETRY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "MultiGeometryType");
  public static final QName ABS_GEOMETRY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractGeometryType");
  public static final QName ABS_GEOMETRY_COLLECTION_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractGeometryCollectionType");

  public static final QName POINT_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Point");
  public static final QName LINESTRING_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "LineString");
  public static final QName LINEARRING_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "LinearRing");
  public static final QName POLYGON_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Polygon");
  public static final QName MULTIPOINT_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "MultiPoint");
  public static final QName MULTILINESTRING_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "MultiLineString");
  public static final QName MULTIPOLYGON_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "MultiPolygon");
  public static final QName MULTIGEOMETRY_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "MultiGeometry");
  public static final QName ABS_GEOMETRY_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_Geometry");
  public static final QName ABS_GEOMETRY_COLLECTION_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_GeometryCollection");

  public static final QName BOX_TYPE_QNAME = new QName("http://www.opengis.net/gml", "BoxType");
  public static final QName ARC_BY_CENTER_POINT_TYPE_QNAME = new QName("http://www.opengis.net/gml", "ArcByCenterPointType");
  public static final QName CIRCLE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "CircleType");
  public static final QName MULTISURFACE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "MultiSurfaceType");
  public static final QName COMPOSITE_SURFACE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "CompositeSurfaceType");
  public static final QName CUBIC_SPLINE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "CubicSplineType");
  public static final QName ARC_STRING_BY_BULGE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "ArcStringByBulgeType");
  public static final QName RING_TYPE_QNAME = new QName("http://www.opengis.net/gml", "RingType");
  public static final QName COMPOSITE_CURVE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "CompositeCurveType");
  public static final QName ORIENTABLE_SURFACE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "OrientableSurfaceType");
  public static final QName BSPLINE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "BSplineType");
  public static final QName GEOMETRIC_COMPLEX_TYPE_QNAME = new QName("http://www.opengis.net/gml", "GeometricComplexType");
  public static final QName ORIENTABLE_CURVE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "OrientableCurveType");
  public static final QName ARC_TYPE_QNAME = new QName("http://www.opengis.net/gml", "ArcType");

  public static final QName CIRCLE_BY_CENTER_POINT_TYPE_QNAME = new QName("http://www.opengis.net/gml", "CircleByCenterPointType");
  public static final QName BEZIER_TYPE_QNAME = new QName("http://www.opengis.net/gml", "BezierType");
  public static final QName SOLID_TYPE_QNAME = new QName("http://www.opengis.net/gml", "SolidType");
  public static final QName ENVELOPE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "EnvelopeType");
  public static final QName ARC_STRING_TYPE_QNAME = new QName("http://www.opengis.net/gml", "ArcStringType");
  public static final QName COMPOSITE_SOLID_TYPE_QNAME = new QName("http://www.opengis.net/gml", "CompositeSolidType");
  public static final QName ARC_BY_BULGE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "ArcByBulgeType");
  public static final QName MULTISOLID_TYPE_QNAME = new QName("http://www.opengis.net/gml", "MultiSolidType");
  public static final QName MULTICURVE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "MultiCurveType");
  public static final QName LINESTRING_SEGMENT_TYPE_QNAME = new QName("http://www.opengis.net/gml", "LineStringSegmentType");
  public static final QName SURFACE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "SurfaceType");
  public static final QName ABS_CURVE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractCurveType");
  public static final QName ABS_SURFACE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractSurfaceType");
  public static final QName ABS_RING_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractRingType");
  public static final QName ABS_GEOMETRIC_PRIMITIVE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractGeometricPrimitiveType");
  public static final QName ABS_CURVE_SEGMENT_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractCurveSegmentType");
  public static final QName ABS_GEOMETRIC_AGGREGATE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractGeometricAggregateType");

  public static final QName BOX_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Box");

  public static final QName ARC_BY_CENTER_POINT_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "ArcByCenterPoint");
  public static final QName CIRCLE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Circle");
  public static final QName MULTISURFACE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "MultiSurface");
  public static final QName COMPOSITE_SURFACE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "CompositeSurface");
  public static final QName CUBIC_SPLINE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "CubicSpline");

  public static final QName ARC_STRING_BY_BULGE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "ArcStringByBulge");
  public static final QName RING_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Ring");
  public static final QName COMPOSITE_CURVE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "CompositeCurve");
  public static final QName ORIENTABLE_SURFACE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "OrientableSurface");
  public static final QName BSPLINE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "BSpline");
  public static final QName GEOMETRIC_COMPLEX_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "GeometricComplex");
  public static final QName ORIENTABLE_CURVE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "OrientableCurve");
  public static final QName ARC_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Arc");

  public static final QName CIRCLE_BY_CENTER_POINT_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "CircleByCenterPoint");
  public static final QName BEZIER_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Bezier");
  public static final QName SOLID_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Solid");
  public static final QName ENVELOPE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Envelope");
  public static final QName ARC_STRING_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "ArcString");
  public static final QName COMPOSITE_SOLID_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "CompositeSolid");
  public static final QName ARC_BY_BULGE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "ArcByBulge");
  public static final QName MULTISOLID_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "MultiSolid");
  public static final QName MULTICURVE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "MultiCurve");
  public static final QName LINESTRING_SEGMENT_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "LineStringSegment");
  public static final QName SURFACE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Surface");
  public static final QName ABS_CURVE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_Curve");
  public static final QName ABS_SURFACE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_Surface");
  public static final QName ABS_RING_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_Ring");
  public static final QName ABS_GEOMETRIC_PRIMITIVE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_GeometricPrimitive");
  public static final QName ABS_CURVE_SEGMENT_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_CurveSegment");
  public static final QName ABS_GEOMETRIC_AGGREGATE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_GeometricAggregate");

  public static final QName[] GEOMETRY_TYPE_QNAMES = { 
    POINT_TYPE_QNAME, 
    POLYGON_TYPE_QNAME, 
    LINESTRING_TYPE_QNAME, 
    LINEARRING_TYPE_QNAME, 
    MULTIPOINT_TYPE_QNAME, 
    MULTIPOLYGON_TYPE_QNAME, 
    MULTILINESTRING_TYPE_QNAME, 
    BOX_TYPE_QNAME, 
    ARC_BY_CENTER_POINT_TYPE_QNAME, 
    CIRCLE_TYPE_QNAME, 
    MULTISURFACE_TYPE_QNAME, 
    COMPOSITE_SURFACE_TYPE_QNAME, 
    CUBIC_SPLINE_TYPE_QNAME, 
    ARC_STRING_BY_BULGE_TYPE_QNAME, 
    RING_TYPE_QNAME, 
    COMPOSITE_CURVE_TYPE_QNAME, 
    ORIENTABLE_SURFACE_TYPE_QNAME, 
    BSPLINE_TYPE_QNAME, 
    GEOMETRIC_COMPLEX_TYPE_QNAME, 
    ORIENTABLE_CURVE_TYPE_QNAME, 
    ARC_TYPE_QNAME, 
    CIRCLE_BY_CENTER_POINT_TYPE_QNAME, 
    BEZIER_TYPE_QNAME, 
    SOLID_TYPE_QNAME, 
    ENVELOPE_TYPE_QNAME, 
    ARC_STRING_TYPE_QNAME, 
    COMPOSITE_SOLID_TYPE_QNAME, 
    ARC_BY_BULGE_TYPE_QNAME, 
    MULTISOLID_TYPE_QNAME, 
    MULTICURVE_TYPE_QNAME, 
    LINESTRING_SEGMENT_TYPE_QNAME, 
    SURFACE_TYPE_QNAME, 
    MULTIGEOMETRY_TYPE_QNAME, 
    ABS_GEOMETRY_TYPE_QNAME, 
    ABS_GEOMETRY_COLLECTION_TYPE_QNAME, 
    ABS_CURVE_TYPE_QNAME, 
    ABS_SURFACE_TYPE_QNAME, 
    ABS_RING_TYPE_QNAME, 
    ABS_GEOMETRIC_PRIMITIVE_TYPE_QNAME, 
    ABS_CURVE_SEGMENT_TYPE_QNAME, 
    ABS_GEOMETRIC_AGGREGATE_TYPE_QNAME };

  public static final QName[] GEOMETRY_ELEMENT_QNAMES = { 
    POINT_ELEMENT_QNAME, 
    POLYGON_ELEMENT_QNAME, 
    LINESTRING_ELEMENT_QNAME, 
    LINEARRING_ELEMENT_QNAME, 
    MULTIPOINT_ELEMENT_QNAME, 
    MULTIPOLYGON_ELEMENT_QNAME, 
    MULTILINESTRING_ELEMENT_QNAME, 
    BOX_ELEMENT_QNAME, 
    ARC_BY_CENTER_POINT_ELEMENT_QNAME, 
    CIRCLE_ELEMENT_QNAME, 
    MULTISURFACE_ELEMENT_QNAME, 
    COMPOSITE_SURFACE_ELEMENT_QNAME, 
    CUBIC_SPLINE_ELEMENT_QNAME, 
    ARC_STRING_BY_BULGE_ELEMENT_QNAME, 
    RING_ELEMENT_QNAME, 
    COMPOSITE_CURVE_ELEMENT_QNAME, 
    ORIENTABLE_SURFACE_ELEMENT_QNAME, 
    BSPLINE_ELEMENT_QNAME, 
    GEOMETRIC_COMPLEX_ELEMENT_QNAME, 
    ORIENTABLE_CURVE_ELEMENT_QNAME, 
    ARC_ELEMENT_QNAME, 
    CIRCLE_BY_CENTER_POINT_ELEMENT_QNAME, 
    BEZIER_ELEMENT_QNAME, 
    SOLID_ELEMENT_QNAME, 
    ENVELOPE_ELEMENT_QNAME, 
    ARC_STRING_ELEMENT_QNAME, 
    COMPOSITE_SOLID_ELEMENT_QNAME, 
    ARC_BY_BULGE_ELEMENT_QNAME, 
    MULTISOLID_ELEMENT_QNAME, 
    MULTICURVE_ELEMENT_QNAME, 
    LINESTRING_SEGMENT_ELEMENT_QNAME, 
    SURFACE_ELEMENT_QNAME, 
    MULTIGEOMETRY_ELEMENT_QNAME, 
    ABS_GEOMETRY_ELEMENT_QNAME, 
    ABS_GEOMETRY_COLLECTION_ELEMENT_QNAME, 
    ABS_CURVE_ELEMENT_QNAME, 
    ABS_SURFACE_ELEMENT_QNAME, 
    ABS_RING_ELEMENT_QNAME, 
    ABS_GEOMETRIC_PRIMITIVE_ELEMENT_QNAME, 
    ABS_CURVE_SEGMENT_ELEMENT_QNAME, 
    ABS_GEOMETRIC_AGGREGATE_ELEMENT_QNAME };
  public static final String GEOMETRY_COLLECTION_TYPE_NAME = "GeometryCollectionType";
  public static final String ABS_FEATURE_ELEMENT_NAME = "_Feature";
  public static final String ABS_FEATURE_COLLECTION_ELEMENT_NAME = "_FeatureCollection";
  public static final String ABSTRACT_FEATURE_COLLECTION_BASE_TYPE_NAME = "AbstractFeatureCollectionBaseType";
  public static final String ABSTRACT_FEATURE_COLLECTION_TYPE_NAME = "AbstractFeatureCollectionType";
  public static final String ABSTRACT_FEATURE_TYPE_NAME = "AbstractFeatureType";
  public static final String FEATURE_ASSOCIATION_TYPE_NAME = "FeatureAssociationType";
  public static final String GEOMETRY_PROPERTY_TYPE_NAME = "GeometryPropertyType";
  public static final String GEOMETRY_ASSOCIATION_TYPE_NAME = "GeometryAssociationType";
  public static final String GEOMETRY_ARRAY_PROPERTY_TYPE_NAME = "GeometryArrayPropertyType";
  public static final String FEATURE_PROPERTY_TYPE_NAME = "FeaturePropertyType";
  public static final String FEATURE_ARRAY_PROPERTY_TYPE_NAME = "FeatureArrayPropertyType";
  public static final String ASSOCIATION_ATTRIBUTE_GROUP_NAME = "AssociationAttributeGroup";
  public static final QName ABS_GML_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_GML");
  public static final QName ABSTRACT_GML_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractGMLType");

  public static final QName ABS_FEATURE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_Feature");
  public static final QName ABSTRACT_FEATURE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractFeatureType");

  public static final QName FEATURE_ASSOCIATION_TYPE_QNAME = new QName("http://www.opengis.net/gml", "FeatureAssociationType");
  public static final QName FEATURE_PROPERTY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "FeaturePropertyType");

  public static final QName FEATURE_ARRAY_PROPERTY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "FeatureArrayPropertyType");

  public static final QName ABS_FEATURE_COLLECTION_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_FeatureCollection");

  public static final QName ABS_FEATURE_COLLECTION_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractFeatureCollectionType");

  public static final QName GEOMETRY_ASSOCIATION_TYPE_QNAME = new QName("http://www.opengis.net/gml", "GeometryAssociationType");

  public static final QName GEOMETRY_COLLECTION_TYPE_QNAME = new QName("http://www.opengis.net/gml", "GeometryCollectionType");

  public static final QName GEOMETRY_PROPERTY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "GeometryPropertyType");

  public static final QName ASSOCIATION_ATTRIBUTE_GROUP_QNAME = new QName("http://www.opengis.net/gml", "AssociationAttributeGroup");
  public static final String ABS_TOPOLOGY_ELEMENT_NAME = "_Topology";
  public static final String ABS_TOPO_PRIMITIVE_ELEMENT_NAME = "_TopoPrimitive";
  public static final String NODE_ELEMENT_NAME = "Node";
  public static final String EDGE_ELEMENT_NAME = "Edge";
  public static final String FACE_ELEMENT_NAME = "Face";
  public static final String TOPO_SOLID_ELEMENT_NAME = "TopoSolid";
  public static final String TOPO_POINT_ELEMENT_NAME = "TopoPoint";
  public static final String TOPO_CURVE_ELEMENT_NAME = "TopoCurve";
  public static final String TOPO_SURFACE_ELEMENT_NAME = "TopoSurface";
  public static final String TOPO_VOLUME_ELEMENT_NAME = "TopoVolume";
  public static final String ABS_TOPOLOGY_TYPE_NAME = "AbstractTopologyType";
  public static final String ABS_TOPO_PRIMITIVE_TYPE_NAME = "AbstractTopoPrimitiveType";
  public static final String NODE_TYPE_NAME = "NodeType";
  public static final String EDGE_TYPE_NAME = "EdgeType";
  public static final String FACE_TYPE_NAME = "FaceType";
  public static final String TOPO_SOLID_TYPE_NAME = "TopoSolidType";
  public static final String TOPO_POINT_TYPE_NAME = "TopoPointType";
  public static final String TOPO_CURVE_TYPE_NAME = "TopoCurveType";
  public static final String TOPO_SURFACE_TYPE_NAME = "TopoSurfaceType";
  public static final String TOPO_VOLUME_TYPE_NAME = "TopoVolumeType";
  public static final String DIRECTED_NODE_PROPERTY_TYPE_NAME = "DirectedNodePropertyType";
  public static final String DIRECTED_EDGE_PROPERTY_TYPE_NAME = "DirectedEdgePropertyType";
  public static final String DIRECTED_FACE_PROPERTY_TYPE_NAME = "DirectedFacePropertyType";
  public static final String DIRECTED_TOPO_SOLID_PROPERTY_TYPE_NAME = "DirectedTopoSolidPropertyType";
  public static final String TOPO_POINT_PROPERTY_TYPE_NAME = "TopoPointPropertyType";
  public static final String TOPO_CURVE_PROPERTY_TYPE_NAME = "TopoCurvePropertyType";
  public static final String TOPO_SURFACE_PROPERTY_TYPE_NAME = "TopoSurfacePropertyType";
  public static final String TOPO_VOLUME_PROPERTY_TYPE_NAME = "TopoVolumePropertyType";
  public static final String TOPO_COMPLEX_ELEMENT_NAME = "TopoComplex";
  public static final String TOPO_COMPLEX_TYPE_NAME = "TopoComplexType";
  public static final String TOPO_PRIMITIVE_MEMBER_NAME = "topoPrimitiveMember";
  public static final String TOPO_PRIMITIVE_MEMBERS_NAME = "topoPrimitiveMembers";
  public static final String TOPO_COMPLEX_MEMBER_TYPE_NAME = "TopoComplexMemberType";
  public static final String TOPO_PRIMITIVE_MEMBER_TYPE_NAME = "topoPrimitiveMemberType";
  public static final String TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE_NAME = "TopoPrimitiveArrayAssociationType";
  public static final String[] TOPOLOGY_ELEMENT_NAMES = { 
    "Node", 
    "Edge", 
    "FaceType", 
    "TopoSolid", 
    "TopoPoint", 
    "TopoCurve", 
    "TopoSurface", 
    "TopoVolume", 
    "TopoComplex", 
    "_Topology", 
    "_TopoPrimitive" };

  public static final String[] TOPOLOGY_TYPE_NAMES = { 
    "NodeType", 
    "EdgeType", 
    "FaceType", 
    "TopoSolidType", 
    "TopoPointType", 
    "TopoCurveType", 
    "TopoSurfaceType", 
    "TopoVolumeType", 
    "TopoComplexType", 
    "AbstractTopologyType", 
    "AbstractTopoPrimitiveType" };

  public static final QName NODE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Node");
  public static final QName EDGE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Edge");
  public static final QName FACE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "Face");
  public static final QName TOPO_SOLID_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "TopoSolid");
  public static final QName TOPO_POINT_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "TopoPoint");
  public static final QName TOPO_CURVE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "TopoCurve");
  public static final QName TOPO_SURFACE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "TopoSurface");
  public static final QName TOPO_VOLUME_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "TopoVolume");
  public static final QName ABS_TOPOLOGY_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_Topology");
  public static final QName ABS_TOPO_PRIMITIVE_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "_TopoPrimitive");

  public static final QName ABS_TOPOLOGY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractTopologyType");
  public static final QName ABS_TOPO_PRIMITIVE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "AbstractTopoPrimitiveType");

  public static final QName NODE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "NodeType");
  public static final QName EDGE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "EdgeType");
  public static final QName FACE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "FaceType");
  public static final QName TOPO_SOLID_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoSolidType");
  public static final QName TOPO_POINT_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoPointType");
  public static final QName TOPO_CURVE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoCurveType");
  public static final QName TOPO_SURFACE_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoSurfaceType");
  public static final QName TOPO_VOLUME_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoVolumeType");

  public static final QName DIRECTED_NODE_PROPERTY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "DirectedNodePropertyType");
  public static final QName DIRECTED_EDGE_PROPERTY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "DirectedEdgePropertyType");
  public static final QName DIRECTED_FACE_PROPERTY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "DirectedFacePropertyType");
  public static final QName DIRECTED_TOPO_SOLID_PROPERTY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "DirectedTopoSolidPropertyType");
  public static final QName TOPO_POINT_PROPERTY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoPointPropertyType");
  public static final QName TOPO_CURVE_PROPERTY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoCurvePropertyType");
  public static final QName TOPO_SURFACE_PROPERTY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoSurfacePropertyType");
  public static final QName TOPO_VOLUME_PROPERTY_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoVolumePropertyType");

  public static final QName TOPO_COMPLEX_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "TopoComplex");
  public static final QName TOPO_COMPLEX_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoComplexType");

  public static final QName TOPO_PRIMITIVE_MEMBER_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "topoPrimitiveMember");
  public static final QName TOPO_PRIMITIVE_MEMBERS_ELEMENT_QNAME = new QName("http://www.opengis.net/gml", "topoPrimitiveMembers");
  public static final QName TOPO_COMPLEX_MEMBER_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoComplexMemberType");
  public static final QName TOPO_PRIMITIVE_MEMBER_TYPE_QNAME = new QName("http://www.opengis.net/gml", "topoPrimitiveMemberType");
  public static final QName TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE_QNAME = new QName("http://www.opengis.net/gml", "TopoPrimitiveArrayAssociationType");

  public static final QName[] TOPOLOGY_ELEMENT_QNAMES = { 
    NODE_ELEMENT_QNAME, 
    EDGE_ELEMENT_QNAME, 
    FACE_TYPE_QNAME, 
    TOPO_SOLID_ELEMENT_QNAME, 
    TOPO_POINT_ELEMENT_QNAME, 
    TOPO_CURVE_ELEMENT_QNAME, 
    TOPO_SURFACE_ELEMENT_QNAME, 
    TOPO_VOLUME_ELEMENT_QNAME, 
    TOPO_COMPLEX_ELEMENT_QNAME, 
    ABS_TOPOLOGY_ELEMENT_QNAME, 
    ABS_TOPO_PRIMITIVE_ELEMENT_QNAME };

  public static final QName[] TOPOLOGY_TYPE_QNAMES = { 
    NODE_TYPE_QNAME, 
    EDGE_TYPE_QNAME, 
    FACE_TYPE_QNAME, 
    TOPO_SOLID_TYPE_QNAME, 
    TOPO_POINT_TYPE_QNAME, 
    TOPO_CURVE_TYPE_QNAME, 
    TOPO_SURFACE_TYPE_QNAME, 
    TOPO_VOLUME_TYPE_QNAME, 
    TOPO_COMPLEX_TYPE_QNAME, 
    ABS_TOPOLOGY_TYPE_QNAME, 
    ABS_TOPO_PRIMITIVE_TYPE_QNAME };
  public static final String DIRECT_POSITION_TYPE_NAME = "DirectPositionType";
  public static final String VECTOR_TYPE_NAME = "VectorType";
  public static final String COORDINATES_TYPE_NAME = "CoordinatesType";
  public static final String COORD_TYPE_NAME = "CoordType";
  public static final String COORDINATES_ELEMENT_NAME = "coordinates";
  public static final String COORD_ELEMENT_NAME = "coord";
  public static final String BOUNDED_BY_PROPERTY_ELEMENT_NAME = "boundedBy";
  public static final String NAME_PROPERTY_ELEMENT_NAME = "name";
  public static final String FEATURE_MEMBER_PROPERTY_ELEMENT_NAME = "featureMember";
  public static final String GEOMETRY_MEMBER_PROPERTY_ELEMENT_NAME = "geometryMember";
  public static final String COORDINATE_SEPARATOR_ATTR_NAME = "cs";
  public static final String TUPLE_SEPARATOR_ATTR_NAME = "ts";
  public static final String DECIMAL_POINT_ATTR_NAME = "decimal";
  public static final String DEFAULT_COORDINATE_SEPARATOR = ",";
  public static final String DEFAULT_TUPLE_SEPARATOR = " ";
  public static final String DEFAULT_DECIMAL_POINT = ".";
  public static final String X_ELEMENT_NAME = "X";
  public static final String Y_ELEMENT_NAME = "Y";
  public static final String Z_ELEMENT_NAME = "Z";
  public static final String GML2_NULL_ELEMENT_NAME = "null";
  public static final String GML3_NULL_ELEMENT_NAME = "Null";
  public static final String NULL_ENUM_VALUE_INAPPLICABLE = "inapplicable";
  public static final String NULL_ENUM_VALUE_UNKNOWN = "unknown";
  public static final String NULL_ENUM_VALUE_WITHHELD = "withheld";
  public static final String NULL_ENUM_VALUE_MISSING = "missing";
  public static final String NULL_ENUM_VALUE_TEMPLATE = "template";
  public static final String SRS_NAME_ATTRIBUTE_NAME = "srsName";
}
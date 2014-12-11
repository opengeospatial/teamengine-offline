package com.galdosinc.glib.gml.schema;

import com.galdosinc.glib.xml.QName;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GmlConstantUtils
  implements GmlConstants
{
  private static final int TYPE = 0;
  private static final int ELEMENT = 1;
  private static final int[] GML2_GEOMETRY_CODES = { 
    0, 1, 
    2, 
    3, 
    4, 
    6, 
    4, 
    32, 
    33, 
    34 };

  private static final int[] GML3_GEOMETRY_CODES = { 
    0, 1, 
    2, 
    3, 
    4, 
    6, 
    4, 
    32, 
    7, 
    8, 
    9, 
    10, 
    11, 
    12, 
    13, 
    14, 
    15, 
    16, 
    17, 
    18, 
    19, 
    20, 
    21, 
    22, 
    23, 
    24, 
    25, 
    26, 
    27, 
    28, 
    29, 
    30, 
    31, 
    35, 
    36, 
    37, 
    38, 
    39, 
    40 };

  private static final Set setOfGml2GeometryTypes_ = new HashSet();
  private static final Set setOfGml3GeometryTypes_;
  private static final Set setOfGml2GeometryElements_ = new HashSet();
  private static final Set setOfGml3GeometryElements_;
  private static final Map setOfGeometryCodes_;
  private static final Map setOfTopologyCodes_;
  private GmlVersion gmlVersion_;

  static
  {
    for (int ii = 0; ii < GML2_GEOMETRY_CODES.length; ii++) {
      setOfGml2GeometryTypes_.add(GmlConstants.GEOMETRY_TYPE_QNAMES[GML2_GEOMETRY_CODES[ii]]);
      setOfGml2GeometryTypes_.add(GmlConstants.GEOMETRY_ELEMENT_QNAMES[GML2_GEOMETRY_CODES[ii]]);
    }

    setOfGml3GeometryTypes_ = new HashSet();
    setOfGml3GeometryElements_ = new HashSet();
    for (int ii = 0; ii < GML3_GEOMETRY_CODES.length; ii++) {
      setOfGml3GeometryTypes_.add(GmlConstants.GEOMETRY_TYPE_QNAMES[GML3_GEOMETRY_CODES[ii]]);
      setOfGml3GeometryElements_.add(GmlConstants.GEOMETRY_ELEMENT_QNAMES[GML3_GEOMETRY_CODES[ii]]);
    }

    setOfGeometryCodes_ = new HashMap();
    for (int ii = 0; ii < GmlConstants.GEOMETRY_ELEMENT_NAMES.length; ii++) {
      setOfGeometryCodes_.put(GmlConstants.GEOMETRY_ELEMENT_NAMES[ii], new Integer(ii));
      setOfGeometryCodes_.put(GmlConstants.GEOMETRY_TYPE_NAMES[ii], new Integer(ii));
    }

    setOfTopologyCodes_ = new HashMap();
    for (int ii = 0; ii < GmlConstants.TOPOLOGY_ELEMENT_NAMES.length; ii++) {
      setOfTopologyCodes_.put(GmlConstants.TOPOLOGY_ELEMENT_NAMES[ii], new Integer(ii));
      setOfTopologyCodes_.put(GmlConstants.TOPOLOGY_TYPE_NAMES[ii], new Integer(ii));
    }
  }

  public GmlConstantUtils(GmlVersion gmlVersion)
  {
    this.gmlVersion_ = gmlVersion;
    initialize();
  }

  protected void initialize()
  {
  }

  public boolean isGeometryType(QName geometryTypeQName)
  {
    return isGeometryConstruct(geometryTypeQName, 0);
  }

  public boolean isGeometryElement(QName geometryElementQName)
  {
    return isGeometryConstruct(geometryElementQName, 1);
  }

  private boolean isGeometryConstruct(QName perhapsGeometry, int typeOrElement) {
    if (this.gmlVersion_.equals(GmlVersion.GML_2)) {
      boolean result = 
        typeOrElement == 0 ? 
        setOfGml2GeometryTypes_.contains(perhapsGeometry) : 
        setOfGml2GeometryElements_.contains(perhapsGeometry);
      return result;
    }if (this.gmlVersion_.equals(GmlVersion.GML_3)) {
      boolean result = 
        typeOrElement == 0 ? 
        setOfGml3GeometryTypes_.contains(perhapsGeometry) : 
        setOfGml3GeometryElements_.contains(perhapsGeometry);
      return result;
    }
    throw new IllegalStateException("Cannot work with GML version " + this.gmlVersion_.getVersionNumber());
  }

  public static int getGmlGeometryCode(String geometryLocalName)
  {
    Integer result = (Integer)setOfGeometryCodes_.get(geometryLocalName);
    return result == null ? -1 : result.intValue();
  }

  public static int getGmlTopologyCode(String topoLocalName)
  {
    Integer result = (Integer)setOfTopologyCodes_.get(topoLocalName);
    return result == null ? -1 : result.intValue();
  }
}
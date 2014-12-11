package com.galdosinc.glib.gml.coord;

import com.galdosinc.glib.xml.dom.DomChildAccess;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CoordinateUtils
{
  public static Object readAllCoordinatesFromGeometry(Element geomElem, CoordinateTupleList tupleList)
    throws CoordinateException
  {
    Element coordinatesElem = DomChildAccess.getFirstChildElement(geomElem, "http://www.opengis.net/gml", "coordinates");
    if (coordinatesElem != null) {
      CoordinatesReader coordinatesReader = new CoordinatesReader();
      coordinatesReader.useCoordinateTupleList(tupleList);
      coordinatesReader.read(coordinatesElem);
      return coordinatesReader;
    }

    Element firstCoordElem = DomChildAccess.getFirstChildElement(geomElem, "http://www.opengis.net/gml", "coord");
    if (firstCoordElem != null) {
      CoordReader coordReader = new CoordReader();
      coordReader.useCoordinateTupleList(tupleList);
      coordReader.read(firstCoordElem, 2147483647);
      return coordReader;
    }
    throw new CoordinateException("Geometry " + geomElem.getLocalName() + " has no recognizable coordinates.");
  }

  public static void readAllCoordinatesBlindly(Element elem, CoordinateTupleList tupleList)
    throws CoordinateException
  {
    NodeList coordinatesElements = elem.getElementsByTagNameNS("http://www.opengis.net/gml", "coordinates");
    NodeList coordElements = elem.getElementsByTagNameNS("http://www.opengis.net/gml", "coord");

    CoordinatesReader coordinatesReader = new CoordinatesReader();
    coordinatesReader.useCoordinateTupleList(tupleList);
    for (int ii = 0; ii < coordinatesElements.getLength(); ii++) {
      coordinatesReader.read((Element)coordinatesElements.item(ii));
    }
    CoordReader coordReader = new CoordReader();
    coordReader.useCoordinateTupleList(tupleList);
    Node lastCoordParent = null;
    for (int ii = 0; ii < coordElements.getLength(); ii++) {
      Element coordElement = (Element)coordElements.item(ii);
      Node coordParent = coordElement.getParentNode();
      if ((lastCoordParent == null) || (!coordParent.equals(lastCoordParent)))
      {
        lastCoordParent = coordParent;
        coordReader.read(coordElement, 2147483647);
      }
    }
  }

  public static void coordinateArrayCopy(double[] src, int srcOffset, double[] trg, int trgOffset, int length)
  {
    while (length > 0) {
      trg[trgOffset] = src[srcOffset];
      trgOffset++;
      srcOffset++;
      length--;
    }
  }

  public static double[] cloneCoordinateArray(double[] src, int srcOffset, int length)
  {
    double[] newArray = new double[length];
    coordinateArrayCopy(src, srcOffset, newArray, 0, length);
    return newArray;
  }

  public static boolean coordinateArraysEqual(double[] a1, double[] a2)
  {
    if (a1.length != a2.length) {
      return false;
    }
    for (int ii = 0; ii < a1.length; ii++) {
      if (a1[ii] != a2[ii]) {
        return false;
      }
    }
    return true;
  }
}
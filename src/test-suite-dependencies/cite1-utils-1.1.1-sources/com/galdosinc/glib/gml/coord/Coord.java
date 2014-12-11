package com.galdosinc.glib.gml.coord;

import com.galdosinc.glib.xml.dom.DomChildAccess;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/** @deprecated */
public class Coord
{
  private static final String COORD_ELEMENT_NAME = "coord";
  private static final String X_ELEMENT_NAME = "X";
  private static final String Y_ELEMENT_NAME = "Y";
  private static final String Z_ELEMENT_NAME = "Z";
  private static final String[] CO_ELEMENT_NAMES = { 
    "X", 
    "Y", 
    "Z" };
  public static final int SCAN_ALL = 2147483647;
  private static final int NO_EXPECTED_DIMENSION = -1;
  private List coordinates_;

  public Coord()
  {
    this.coordinates_ = new LinkedList();
  }

  public int scan(Element elem, int coordCount)
  {
    if (coordCount < 1) {
      coordCount = 2147483647;
    }
    Node nthNode = elem;

    this.coordinates_.clear();
    int expectedDimension = -1;
    int counter = 0;
    while ((nthNode != null) && (counter < coordCount)) {
      if (nthNode.getNodeType() == 1) {
        double[] nthCoord = scanSingleCoord((Element)nthNode, expectedDimension);
        if (expectedDimension == -1) {
          expectedDimension = nthCoord.length;
        }
        this.coordinates_.add(nthCoord);
        counter++;
      }
      nthNode = nthNode.getNextSibling();
    }
    return counter;
  }

  private double[] scanSingleCoord(Element elem, int expectedDimension)
  {
    List coStrList = new LinkedList();
    Node node = elem.getFirstChild();
    int counter = 0;
    while (node != null) {
      if ((node.getNodeType() != 1) || (!node.getLocalName().equals(CO_ELEMENT_NAMES[counter])) || (node.getNamespaceURI() == null) || (!node.getNamespaceURI().equals("http://www.opengis.net/gml"))) {
        throw new IllegalStateException("Encountered unexpected child of gml:coord: " + node.getNodeName());
      }
      coStrList.add(DomChildAccess.getElementTextContent((Element)node).toString());
      node = node.getNextSibling();
      counter++;
    }
    if ((expectedDimension != -1) && (coStrList.size() != expectedDimension)) {
      throw new IllegalStateException("This gml:coord must have " + expectedDimension + " coordinates just like its preceding gml:coord's.");
    }
    double[] result = new double[coStrList.size()];
    Iterator coStrListIter = coStrList.iterator();
    counter = 0;
    while (coStrListIter.hasNext()) {
      result[counter] = Double.parseDouble((String)coStrListIter.next());
      counter++;
    }
    return result;
  }

  public int scan(Element elem)
  {
    return scan(elem, 1);
  }

  public double[] getCoordValues()
  {
    return (double[])this.coordinates_.get(0);
  }

  public List getCoordsValues()
  {
    return this.coordinates_;
  }
}
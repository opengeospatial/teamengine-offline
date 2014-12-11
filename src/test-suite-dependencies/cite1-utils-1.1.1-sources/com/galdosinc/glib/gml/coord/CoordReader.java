package com.galdosinc.glib.gml.coord;

import com.galdosinc.glib.xml.dom.DomChildAccess;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CoordReader
{
  private static final String[] CO_ELEMENT_NAMES = { 
    "X", 
    "Y", 
    "Z" };
  public static final int SCAN_ALL = 2147483647;
  private List liveTupleList_;
  private CoordinateTupleList tupleList_;

  public void useCoordinateTupleList(CoordinateTupleList tupleList)
  {
    this.tupleList_ = tupleList;
    this.liveTupleList_ = this.tupleList_.asLiveList();
  }

  public void readSingleCoord(Element elem)
    throws CoordinateException
  {
    read(elem, 1);
  }

  public int read(Element elem, int maxCoords)
    throws CoordinateException
  {
    if (maxCoords < 1) {
      throw new CoordinateException("Cannot parser " + maxCoords + " gml:coord's");
    }

    double[] firstTuple = readFirstCoord(elem);
    int expectedDimension = firstTuple.length;

    if (this.tupleList_.getCoordinateTupleCount() == 0)
      this.tupleList_.setCoordinateTuples(new double[][] { firstTuple }, true);
    else {
      this.liveTupleList_.add(firstTuple);
    }

    Node nthNode = elem.getNextSibling();
    int counter = 1;
    while ((nthNode != null) && (counter < maxCoords)) {
      if ((nthNode.getNodeType() == 1) && (nthNode.getLocalName().equals("coord")) && (nthNode.getNamespaceURI().equals("http://www.opengis.net/gml"))) {
        double[] nthCoord = readSubsequentCoord((Element)nthNode, expectedDimension);
        this.liveTupleList_.add(nthCoord);
        counter++;
      }
      nthNode = nthNode.getNextSibling();
    }
    return counter;
  }

  private double[] readFirstCoord(Element elem) throws CoordinateException {
    List coStrList = new LinkedList();
    Node node = elem.getFirstChild();
    int counter = 0;
    while (node != null) {
      if ((node.getNodeType() != 1) || (!node.getLocalName().equals(CO_ELEMENT_NAMES[counter])) || (node.getNamespaceURI() == null) || (!node.getNamespaceURI().equals("http://www.opengis.net/gml"))) {
        throw new CoordinateException("Encountered unexpected child of gml:coord: " + node.getNodeName());
      }
      coStrList.add(DomChildAccess.getElementTextContent((Element)node).toString());
      node = node.getNextSibling();
      counter++;
    }
    if (counter == 0) {
      throw new CoordinateException("gml:coord must have at least one coordinate <X>.");
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

  private double[] readSubsequentCoord(Element elem, int expectedDimension) throws CoordinateException {
    double[] result = new double[expectedDimension];
    Node node = elem.getFirstChild();
    int counter = 0;
    while ((node != null) && (counter < expectedDimension)) {
      if ((node.getNodeType() != 1) || (!node.getLocalName().equals(CO_ELEMENT_NAMES[counter])) || (node.getNamespaceURI() == null) || (!node.getNamespaceURI().equals("http://www.opengis.net/gml"))) {
        throw new CoordinateException("Encountered unexpected child of gml:coord: " + node.getNodeName());
      }
      String coStr = DomChildAccess.getElementTextContent((Element)node).toString();
      result[counter] = Double.parseDouble(coStr);
      node = node.getNextSibling();
      counter++;
    }
    if (node != null) {
      throw new CoordinateException("Encountered too many coordinates in gml:coord. Expected " + expectedDimension + " but there are more.");
    }
    if (counter < expectedDimension) {
      throw new CoordinateException("This gml:coord must have " + expectedDimension + " coordinates just like its preceding gml:coord's.");
    }
    return result;
  }
}
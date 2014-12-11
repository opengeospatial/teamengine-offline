package com.galdosinc.glib.gml.coord;

import com.galdosinc.glib.xml.dom.DomChildAccess;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class CoordWriter
{
  private static final String[] CO_ELEMENT_NAMES = { 
    "X", 
    "Y", 
    "Z" };

  public Element write(CoordinateTupleList tupleList, Element coordinatesElem)
    throws CoordinateException
  {
    return write(tupleList, 0, tupleList.getCoordinateTupleCount(), coordinatesElem);
  }

  public Element write(CoordinateTupleList tupleList, int startIndex, int endIndex, Element coordElem)
    throws CoordinateException
  {
    if ((tupleList.getDimension() == 0) || (tupleList.getDimension() > 3)) {
      throw new CoordinateException("Cannot write a gml:coord of invalid dimension " + tupleList.getDimension());
    }
    Iterator tupleListIter = tupleList.asLiveList().iterator();
    int counter = 0;

    while (counter < startIndex) {
      if (!tupleListIter.hasNext()) {
        throw new CoordinateException("The supplied coordinate tuple list has less than " + startIndex + " members");
      }
      tupleListIter.next();
    }
    Node coordNode = coordElem;
    while (counter < endIndex) {
      if (!tupleListIter.hasNext()) {
        throw new CoordinateException("The supplied coordinate tuple list has less than " + endIndex + " members");
      }
      if (coordNode == null) {
        throw new CoordinateException("Not enough gml:coord elements to write to. Needed " + (endIndex - startIndex + 1) + " but found only " + (counter - startIndex));
      }
      double[] tuple = (double[])tupleListIter.next();
      writeSingleCoord(tuple, coordElem);
      coordNode = coordNode.getNextSibling();
    }
    return coordElem;
  }

  public void writeSingleCoord(double[] tuple, Element coordElem)
    throws CoordinateException
  {
    Iterator coordChildIter = DomChildAccess.getChildElementIterator(coordElem);
    int counter = 0;
    while (coordChildIter.hasNext()) {
      Element coordChild = (Element)coordChildIter.next();
      if ((!coordChild.getLocalName().equals(CO_ELEMENT_NAMES[counter])) || 
        (!coordChild.getLocalName().equals("http://www.opengis.net/gml"))) {
        throw new CoordinateException("Encountered unexpected child element of gml:coord : " + coordChild.getNamespaceURI() + ':' + coordChild.getLocalName());
      }

      DomChildAccess.removeAllChildNodes(coordChild);
      String coStr = Double.toString(tuple[counter]);
      Text textNode = coordElem.getOwnerDocument().createTextNode(coStr);
      coordChild.appendChild(textNode);
    }
    if (counter < tuple.length)
      throw new CoordinateException("Insufficient number of child elements of gml:coord : " + counter + " when " + tuple.length + " were expected.");
  }
}
package com.galdosinc.glib.gml.coord;

import com.galdosinc.glib.xml.dom.DomChildAccess;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/** @deprecated */
public class Coordinates
{
  private String cs_ = ",";
  private String ts_ = " ";
  private String decimal_ = ".";
  private Element coordinatesElem_;
  private List coordinatesList_;
  private boolean lastScanStillValid_;
  private double[][] coordinates_;

  public void scan(Element elem)
  {
    this.lastScanStillValid_ = false;
    this.coordinates_ = null;
    this.coordinatesList_ = null;
    this.coordinatesElem_ = elem;

    String text = DomChildAccess.getElementTextContent(elem).toString();

    Attr csAttr = elem.getAttributeNodeNS(null, "cs");
    this.cs_ = (csAttr != null ? csAttr.getValue() : ",");
    Attr tsAttr = elem.getAttributeNodeNS(null, "ts");
    this.ts_ = (tsAttr != null ? tsAttr.getValue() : " ");
    Attr decimalAttr = elem.getAttributeNodeNS(null, "decimal");

    if (decimalAttr != null) {
      this.decimal_ = decimalAttr.getValue();
      if (!this.decimal_.equals("."))
      {
        text = text.replace(this.decimal_.charAt(0), ".".charAt(0));
      }
    } else {
      this.decimal_ = ".";
    }

    StringTokenizer tupleTokenizer = new StringTokenizer(text, this.ts_);
    if (!tupleTokenizer.hasMoreTokens())
    {
      this.coordinatesList_ = new ArrayList();
      return;
    }

    String nextTuple = tupleTokenizer.nextToken();
    double[] tuple = scanCoordinateTuple(nextTuple, -1);
    int dimension = tuple.length;
    List tupleList = new LinkedList();
    tupleList.add(tuple);

    while (tupleTokenizer.hasMoreTokens()) {
      nextTuple = tupleTokenizer.nextToken();
      tuple = scanCoordinateTuple(nextTuple, dimension);
      tupleList.add(tuple);
    }
    this.coordinatesList_ = tupleList;
  }

  private double[] scanCoordinateTuple(String text, int expectedDimension)
  {
    StringTokenizer coordTokenizer = new StringTokenizer(text, this.cs_);
    double[] coordValues = (double[])null;
    if (expectedDimension == -1)
    {
      List coords = new LinkedList();
      while (coordTokenizer.hasMoreTokens()) {
        String nextCoord = coordTokenizer.nextToken();
        Double value = Double.valueOf(nextCoord);
        coords.add(value);
      }
      coordValues = new double[coords.size()];
      Iterator coordIter = coords.iterator();
      int counter = 0;
      while (coordIter.hasNext()) {
        coordValues[counter] = ((Double)coordIter.next()).doubleValue();
        counter++;
      }
    } else {
      coordValues = new double[expectedDimension];
      int counter = 0;
      while ((coordTokenizer.hasMoreTokens()) && (counter < expectedDimension)) {
        String nextCoord = coordTokenizer.nextToken();
        coordValues[counter] = Double.parseDouble(nextCoord);
        counter++;
      }
      if (coordTokenizer.hasMoreTokens()) {
        throw new IllegalStateException("The coordinate tuple " + text + " has more coordinates than expected " + expectedDimension);
      }
      if (counter != expectedDimension) {
        throw new IllegalStateException("The coordinate tuple " + text + " has less coordinates (" + counter + ") than expected " + expectedDimension);
      }
    }
    return coordValues;
  }

  public void setCoordinates(double[][] values)
  {
    this.coordinates_ = values;
    this.lastScanStillValid_ = true;
    this.coordinatesList_ = Arrays.asList(values);
  }

  public void setCoordinates(List valuesList)
  {
    this.coordinates_ = null;
    this.lastScanStillValid_ = false;
    this.coordinatesList_ = valuesList;
  }

  public Element writeToXML(Document adoptingDocument, String gmlPrefix)
  {
    if (adoptingDocument == null) {
      return null;
    }
    String elemTag = 
      gmlPrefix + ':' + "coordinates";
    Element coordinatesElem = adoptingDocument.createElementNS("http://www.opengis.net/gml", elemTag);
    return writeToXML(coordinatesElem);
  }

  public Element writeToXML()
  {
    if (this.coordinatesElem_ == null) {
      return null;
    }
    return writeToXML(this.coordinatesElem_);
  }

  private Element writeToXML(Element coordinatesElem)
  {
    Iterator tupleIterator = this.coordinatesList_.iterator();
    StringBuffer coordinatesStrBuffer = new StringBuffer(this.coordinatesList_.size() * 30);
    while (tupleIterator.hasNext()) {
      double[] tuple = (double[])tupleIterator.next();

      for (int jj = 0; jj < tuple.length; jj++) {
        coordinatesStrBuffer.append(Double.toString(tuple[jj]));

        if (jj < tuple.length - 1) {
          coordinatesStrBuffer.append(this.cs_);
        }
      }
      if (tupleIterator.hasNext()) {
        coordinatesStrBuffer.append(this.ts_);
      }
    }

    if (!this.decimal_.equals(".")) {
      char newDecimal = this.decimal_.charAt(0);
      char defaultDecimal = ".".charAt(0);
      int len = coordinatesStrBuffer.length();
      for (int ii = 0; ii < len; ii++) {
        if (coordinatesStrBuffer.charAt(ii) == defaultDecimal) {
          coordinatesStrBuffer.setCharAt(ii, newDecimal);
        }
      }
    }
    DomChildAccess.removeAllChildNodes(coordinatesElem);
    Text textNode = coordinatesElem.getOwnerDocument().createTextNode(coordinatesStrBuffer.toString());
    coordinatesElem.appendChild(textNode);

    coordinatesElem.removeAttribute("cs");
    coordinatesElem.removeAttribute("ts");
    coordinatesElem.removeAttribute("decimal");

    if (!this.cs_.equals(",")) {
      coordinatesElem.setAttributeNS(null, "cs", this.cs_);
    }
    if (!this.ts_.equals(" ")) {
      coordinatesElem.setAttributeNS(null, "ts", this.ts_);
    }

    if (!this.decimal_.equals(".")) {
      coordinatesElem.setAttributeNS(null, "decimal", this.decimal_);
    }

    return coordinatesElem;
  }

  private Element cloneCoordinates(Element coordinatesElem, Document adoptingDocument)
  {
    Element importedNode = (Element)adoptingDocument.importNode(coordinatesElem, false);
    return importedNode;
  }

  public List getCoordinateValuesList()
  {
    return this.coordinatesList_;
  }

  public double[][] getCoordinateValues()
  {
    if ((this.lastScanStillValid_) && (this.coordinates_ != null)) {
      return this.coordinates_;
    }
    int tupleCount = this.coordinatesList_ != null ? this.coordinatesList_.size() : 0;
    int dimension = tupleCount > 0 ? ((double[])this.coordinatesList_.get(0)).length : 0;
    this.coordinates_ = new double[tupleCount][dimension];
    if (tupleCount > 0) {
      this.coordinatesList_.toArray(this.coordinates_);
    }
    this.lastScanStillValid_ = true;
    return this.coordinates_;
  }

  public String getTupleSeparator()
  {
    return this.ts_;
  }

  public String getCoordinateSeparator()
  {
    return this.cs_;
  }

  public String getDecimalPoint()
  {
    return this.decimal_;
  }

  public void setTupleSeparator(String ts)
  {
    if (ts.length() != 1) {
      throw new IllegalArgumentException("(" + ts + ") cannot be the coordinate tuple separator in coordinates.");
    }
    this.ts_ = ts;
  }

  public void setCoordinateSeparator(String cs)
  {
    if (cs.length() != 1) {
      throw new IllegalArgumentException("(" + cs + ") cannot be the coordinate separator in coordinates.");
    }
    this.cs_ = cs;
  }

  public void setDecimalPoint(String decimal)
  {
    if (decimal.length() != 1) {
      throw new IllegalArgumentException("(" + decimal + ") cannot be the decimal point in coordinates.");
    }
    this.decimal_ = decimal;
  }
}
package com.galdosinc.glib.gml.coord;

import com.galdosinc.glib.xml.dom.DomChildAccess;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class CoordinatesReader
{
  private CoordinatesFormat coordinatesFormat_;
  private CoordinateTupleList tupleList_;
  private List liveTupleList_;

  public CoordinatesFormat getCoordinatesFormat()
  {
    return this.coordinatesFormat_;
  }

  public void useCoordinateTupleList(CoordinateTupleList tupleList)
  {
    this.tupleList_ = tupleList;
    this.liveTupleList_ = this.tupleList_.asLiveList();
  }

  public CoordinateTupleList getCoordinateTupleList()
  {
    return this.tupleList_;
  }

  public void read(String coordinatesText, CoordinatesFormat cf)
    throws CoordinateException
  {
    if (this.tupleList_ == null) {
      throw new CoordinateException("CoordinatesReader: The desired CoordinateTupleList must be set prior to reading coordinates.");
    }

    String tupleSeparator = this.coordinatesFormat_.getTupleSeparator() + '\n';
    StringTokenizer tupleTokenizer = new StringTokenizer(coordinatesText, tupleSeparator);
    if (!tupleTokenizer.hasMoreTokens())
    {
      return;
    }

    String nextTuple = tupleTokenizer.nextToken();
    double[] tuple = scanCoordinateTuple(nextTuple, -1);
    int dimension = tuple.length;
    if (this.tupleList_.getCoordinateTupleCount() == 0)
      this.tupleList_.setCoordinateTuples(new double[][] { tuple }, true);
    else {
      this.liveTupleList_.add(tuple);
    }

    while (tupleTokenizer.hasMoreTokens()) {
      nextTuple = tupleTokenizer.nextToken();
      tuple = scanCoordinateTuple(nextTuple, dimension);
      this.liveTupleList_.add(tuple);
    }
  }

  public void read(Element elem)
    throws CoordinateException
  {
    this.coordinatesFormat_ = null;

    StringBuffer textBuffer = DomChildAccess.getElementTextContent(elem);

    CoordinatesFormat cf = this.coordinatesFormat_ = new CoordinatesFormat();

    Attr csAttr = elem.getAttributeNodeNS(null, "cs");
    if (csAttr != null) {
      cf.setCoordinateSeparator(csAttr.getValue());
    }
    Attr tsAttr = elem.getAttributeNodeNS(null, "ts");
    if (tsAttr != null) {
      cf.setTupleSeparator(tsAttr.getValue());
    }
    Attr decimalAttr = elem.getAttributeNodeNS(null, "decimal");
    if (decimalAttr != null) {
      cf.setDecimalPoint(decimalAttr.getValue());
      if (!cf.getDecimalPoint().equals(CoordinatesFormat.getDefaultDecimalPoint()))
      {
        char defaultDecimalChar = CoordinatesFormat.getDefaultDecimalPoint().charAt(0);
        char decimalChar = cf.getDecimalPoint().charAt(0);
        for (int ii = 0; ii < textBuffer.length(); ii++) {
          if (textBuffer.charAt(ii) == decimalChar) {
            textBuffer.setCharAt(ii, defaultDecimalChar);
          }
        }
      }
    }
    read(textBuffer.toString(), cf);
  }

  private double[] scanCoordinateTuple(String text, int expectedDimension)
    throws CoordinateException
  {
    StringTokenizer coordTokenizer = new StringTokenizer(text, this.coordinatesFormat_.getCoordinateSeparator());
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
        throw new CoordinateException("The coordinate tuple " + text + " has more coordinates than expected " + expectedDimension);
      }
      if (counter != expectedDimension) {
        throw new CoordinateException("The coordinate tuple " + text + " has less coordinates (" + counter + ") than expected " + expectedDimension);
      }
    }
    return coordValues;
  }
}
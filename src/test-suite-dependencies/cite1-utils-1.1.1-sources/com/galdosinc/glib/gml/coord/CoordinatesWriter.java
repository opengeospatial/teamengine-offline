package com.galdosinc.glib.gml.coord;

import com.galdosinc.glib.xml.dom.DomChildAccess;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class CoordinatesWriter
{
  private CoordinatesFormat coordinatesFormat_;

  public CoordinatesWriter()
  {
    this(new CoordinatesFormat());
  }

  public CoordinatesWriter(CoordinatesFormat cf) {
    setCoordinatesFormat(cf);
  }

  public void setCoordinatesFormat(CoordinatesFormat cf) {
    this.coordinatesFormat_ = cf;
  }

  public Element write(CoordinateTupleList tupleList, Element coordinatesElem)
  {
    Iterator tupleIterator = tupleList.asSequentialAccessList(true).iterator();
    StringBuffer coordinatesStrBuffer = new StringBuffer(tupleList.getCoordinateTupleCount() * 30);
    while (tupleIterator.hasNext()) {
      double[] tuple = (double[])tupleIterator.next();

      for (int jj = 0; jj < tuple.length; jj++) {
        coordinatesStrBuffer.append(Double.toString(tuple[jj]));
        if (jj < tuple.length - 1) {
          coordinatesStrBuffer.append(this.coordinatesFormat_.getCoordinateSeparator());
        }
      }
      if (tupleIterator.hasNext()) {
        coordinatesStrBuffer.append(this.coordinatesFormat_.getTupleSeparator());
      }
    }

    if (!this.coordinatesFormat_.getDecimalPoint().equals(".")) {
      char newDecimal = this.coordinatesFormat_.getDecimalPoint().charAt(0);
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

    Attr oldCsAttr = coordinatesElem.getAttributeNodeNS(null, "cs");
    Attr oldTsAttr = coordinatesElem.getAttributeNodeNS(null, "ts");
    Attr oldDecimalAttr = coordinatesElem.getAttributeNodeNS(null, "decimal");

    String currentCs = this.coordinatesFormat_.getCoordinateSeparator();
    if (((oldCsAttr == null) && (!currentCs.equals(CoordinatesFormat.getDefaultCoordinateSeparator()))) || (
      (oldCsAttr != null) && (!oldCsAttr.getValue().equals(currentCs))))
      coordinatesElem.setAttributeNS(null, "cs", currentCs);
    else if ((oldCsAttr != null) && (currentCs.equals(CoordinatesFormat.getDefaultCoordinateSeparator()))) {
      coordinatesElem.removeAttributeNS(null, "cs");
    }

    String currentTs = this.coordinatesFormat_.getTupleSeparator();
    if (((oldTsAttr == null) && (!currentTs.equals(CoordinatesFormat.getDefaultTupleSeparator()))) || (
      (oldTsAttr != null) && (!oldTsAttr.getValue().equals(currentTs))))
      coordinatesElem.setAttributeNS(null, "ts", currentTs);
    else if ((oldTsAttr != null) && (currentTs.equals(CoordinatesFormat.getDefaultTupleSeparator()))) {
      coordinatesElem.removeAttributeNS(null, "ts");
    }

    String currentDecimal = this.coordinatesFormat_.getDecimalPoint();
    if (((oldDecimalAttr == null) && (!currentDecimal.equals(CoordinatesFormat.getDefaultDecimalPoint()))) || (
      (oldDecimalAttr != null) && (!oldDecimalAttr.getValue().equals(currentDecimal))))
      coordinatesElem.setAttributeNS(null, "decimal", currentDecimal);
    else if ((oldDecimalAttr != null) && (currentDecimal.equals(CoordinatesFormat.getDefaultDecimalPoint()))) {
      coordinatesElem.removeAttributeNS(null, "decimal");
    }
    return coordinatesElem;
  }
}
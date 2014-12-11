package com.galdosinc.glib.gml.bbox;

import com.galdosinc.glib.gml.coord.CoordinateTupleLinkedList;
import com.galdosinc.glib.gml.coord.CoordinateTupleList;
import com.galdosinc.glib.gml.coord.CoordinatesWriter;
import com.galdosinc.glib.gml.schema.GmlVersion;
import com.galdosinc.glib.xml.XmlUtils;
import com.galdosinc.glib.xml.dom.DomChildAccess;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class BoundedByWriter
{
  private GmlVersion gmlVersion_;
  private String gmlPrefix_;

  public BoundedByWriter(GmlVersion gmlVersion, String gmlPrefix)
  {
    this.gmlPrefix_ = gmlPrefix;
    this.gmlVersion_ = gmlVersion;
    if ((!this.gmlVersion_.equals(GmlVersion.GML_2)) && (!this.gmlVersion_.equals(GmlVersion.GML_3)))
      throw new IllegalArgumentException("Unknown GML version in BoundedByWriter: " + gmlVersion.getVersionNumber());
  }

  public void writeBoundingBox(Element fc, BoundingBox bbox, String srsName)
  {
    Document ownerDoc = fc.getOwnerDocument();
    Element boundedBy = setBoundedBy(fc);

    CoordinateTupleList bboxTupleList = new CoordinateTupleLinkedList();
    List liveBBoxTupleList = bboxTupleList.asLiveList();
    liveBBoxTupleList.add(bbox.getLowerLeftTuple());
    liveBBoxTupleList.add(bbox.getUpperRight());
    Element geomElement = null;
    if (this.gmlVersion_.equals(GmlVersion.GML_2))
    {
      String boxTag = XmlUtils.constructQualifiedName(this.gmlPrefix_, "Box");
      geomElement = ownerDoc.createElementNS("http://www.opengis.net/gml", boxTag);
    }
    else if (this.gmlVersion_.equals(GmlVersion.GML_3))
    {
      String envelopeTag = XmlUtils.constructQualifiedName(this.gmlPrefix_, "Envelope");
      geomElement = ownerDoc.createElementNS("http://www.opengis.net/gml", envelopeTag);
    }
    geomElement.setAttributeNS(null, "srsName", srsName);
    boundedBy.appendChild(geomElement);
    String coordinatesTag = XmlUtils.constructQualifiedName(this.gmlPrefix_, "coordinates");
    Element coordinatesElement = ownerDoc.createElementNS("http://www.opengis.net/gml", coordinatesTag);
    geomElement.appendChild(coordinatesElement);
    CoordinatesWriter coordinatesWriter = new CoordinatesWriter();
    coordinatesWriter.write(bboxTupleList, coordinatesElement);
  }

  public void writeNull(Element fc, String value)
  {
    Document ownerDoc = fc.getOwnerDocument();
    Element boundedBy = setBoundedBy(fc);
    Element nullElement = null;

    if (this.gmlVersion_.equals(GmlVersion.GML_2))
    {
      String gml2NullTag = XmlUtils.constructQualifiedName(this.gmlPrefix_, "null");
      nullElement = ownerDoc.createElementNS("http://www.opengis.net/gml", gml2NullTag);
    }
    else if (this.gmlVersion_.equals(GmlVersion.GML_3))
    {
      String gml3NullTag = XmlUtils.constructQualifiedName(this.gmlPrefix_, "Null");
      nullElement = ownerDoc.createElementNS("http://www.opengis.net/gml", gml3NullTag);
    }
    boundedBy.appendChild(nullElement);
    Text nullTextNode = ownerDoc.createTextNode(value);
    nullElement.appendChild(nullTextNode);
  }

  private Element setBoundedBy(Element fc) {
    Document ownerDoc = fc.getOwnerDocument();
    Element boundedBy = DomChildAccess.getFirstChildElement(fc, "http://www.opengis.net/gml", "boundedBy");
    String boundedByTag = XmlUtils.constructQualifiedName(this.gmlPrefix_, "boundedBy");
    Element newBoundedBy = ownerDoc.createElementNS("http://www.opengis.net/gml", boundedByTag);
    if (boundedBy == null) {
      Element firstFeatureMember = DomChildAccess.getFirstChildElement(fc, "http://www.opengis.net/gml", "featureMember");
      fc.insertBefore(newBoundedBy, firstFeatureMember);
    } else {
      fc.replaceChild(newBoundedBy, boundedBy);
    }
    return newBoundedBy;
  }
}
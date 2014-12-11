package com.galdosinc.glib.gml.schema;

import com.galdosinc.glib.xml.QName;
import com.galdosinc.glib.xml.XmlUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DomSchemaErrorHandler
  implements SchemaErrorHandler
{
  public static final String SCHEMA_VALIDATION_REPORT_NS_URI = "http://www.galdosinc.com/xml/schema/validation/report";
  private boolean errorsReported = false;
  private List schemaErrors = new ArrayList();
  private Map elementErrors = new HashMap();

  public void error(String message) {
    this.schemaErrors.add(message);
    this.errorsReported = true;
  }

  public void error(String namespaceUri, String localName, String message) {
    QName qName = new QName(namespaceUri, localName);
    List elementErrorList = (List)this.elementErrors.get(qName);
    if (elementErrorList == null) {
      elementErrorList = new ArrayList();
      this.elementErrors.put(qName, elementErrorList);
    }
    elementErrorList.add(message);
    this.errorsReported = true;
  }

  public boolean hasErrors() {
    return this.errorsReported;
  }

  public Element toDomElement() {
    return toDomDocument().getDocumentElement();
  }

  public Document toDomDocument() {
    Document document = XmlUtils.getJaxpDocBuilder().newDocument();
    Element validationResult = document.createElementNS("http://www.galdosinc.com/xml/schema/validation/report", "SchemaValidationReport");
    validationResult.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.galdosinc.com/xml/schema/validation/report");
    document.appendChild(validationResult);
    addErrors(document, validationResult, this.schemaErrors);
    for (Iterator elements = this.elementErrors.keySet().iterator(); elements.hasNext(); ) {
      QName qName = (QName)elements.next();
      Element elementResult = document.createElementNS("http://www.galdosinc.com/xml/schema/validation/report", "ElementReport");
      validationResult.appendChild(elementResult);
      elementResult.setAttributeNS("http://www.galdosinc.com/xml/schema/validation/report", "namespaceUri", qName.getNamespaceUri());
      elementResult.setAttributeNS("http://www.galdosinc.com/xml/schema/validation/report", "name", qName.getLocalName());
      addErrors(document, elementResult, (List)this.elementErrors.get(qName));
    }
    return document;
  }

  private void addErrors(Document document, Element parent, List errors) {
    for (Iterator messages = errors.iterator(); messages.hasNext(); ) {
      String message = (String)messages.next();
      Element errorElement = document.createElementNS("http://www.galdosinc.com/xml/schema/validation/report", "Error");
      parent.appendChild(errorElement);
      errorElement.appendChild(document.createTextNode(message));
    }
  }
}
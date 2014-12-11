package com.galdosinc.glib.xml.jaxp;

import com.galdosinc.glib.xml.XmlUtils;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXParseException;

public class DomErrorHandler extends ValidatorErrorHandler
{
  public static final String INSTANCE_VALIDATION_REPORT_NS_URI = "http://www.galdosinc.com/xml/instance/validation/report";
  private Document document;

  public DomErrorHandler()
  {
    this.document = XmlUtils.getJaxpDocBuilder().newDocument();
    Element validationResult = this.document.createElementNS("http://www.galdosinc.com/xml/instance/validation/report", "InstanceValidationReport");
    validationResult.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.galdosinc.com/xml/instance/validation/report");
    this.document.appendChild(validationResult);
  }

  protected void addMessage(SAXParseException spe) {
    Element message = this.document.createElementNS("http://www.galdosinc.com/xml/instance/validation/report", "Error");
    message.setAttribute("line", String.valueOf(spe.getLineNumber()));
    message.setAttribute("column", String.valueOf(spe.getColumnNumber()));
    message.appendChild(this.document.createTextNode(spe.getMessage()));
    this.document.getDocumentElement().appendChild(message);
  }

  public Document toDomDocument() {
    return this.document;
  }

  public Element toDomElement() {
    return this.document.getDocumentElement();
  }
}
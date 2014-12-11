package com.galdosinc.glib.xml.jaxp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.XMLAttributeDecl;
import org.apache.xerces.impl.dtd.XMLElementDecl;
import org.apache.xerces.impl.dtd.XMLSimpleType;
import org.apache.xerces.parsers.XMLGrammarPreparser;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ValidatingDocumentBuilder extends DocumentBuilder
{
  private DocumentBuilder documentBuilder;
  private Map dtds;

  public ValidatingDocumentBuilder(DocumentBuilder documentBuilder, ErrorHandler errorHandler, Map dtds)
  {
    this.documentBuilder = documentBuilder;
    documentBuilder.setErrorHandler(errorHandler);
    this.dtds = dtds;
  }

  public DOMImplementation getDOMImplementation() {
    return this.documentBuilder.getDOMImplementation();
  }

  public boolean isNamespaceAware() {
    return this.documentBuilder.isNamespaceAware();
  }

  public boolean isValidating() {
    return this.documentBuilder.isValidating();
  }

  public Document newDocument() {
    return this.documentBuilder.newDocument();
  }

  public void setErrorHandler(ErrorHandler errorHandler) {
    this.documentBuilder.setErrorHandler(errorHandler);
  }

  public void setEntityResolver(EntityResolver entityResolver) {
    this.documentBuilder.setEntityResolver(entityResolver);
  }

  public Document parse(InputSource is) throws SAXException, IOException {
    return this.documentBuilder.parse(is);
  }

  public Document parse(File file) throws SAXException, IOException {
    Document document = this.documentBuilder.parse(file);
    return document;
  }

  public Document parse(InputStream inputStream, String systemId) throws SAXException, IOException {
    Document document = this.documentBuilder.parse(inputStream, systemId);
    validateDocument(document);
    return document;
  }

  public Document parse(InputStream inputStream) throws SAXException, IOException {
    Document document = this.documentBuilder.parse(inputStream);
    validateDocument(document);
    return document;
  }

  public Document parse(String uri) throws SAXException, IOException {
    Document document = this.documentBuilder.parse(uri);
    validateDocument(document);
    return document;
  }

  private void validateDocument(Document document) throws SAXException, IOException {
    DocumentType docType = document.getDoctype();
    if (docType != null) {
      String documentDtd = docType.getSystemId();
      String rootElement = docType.getName();
      String expectedDtd = (String)this.dtds.get(rootElement);
      if (!compareDtd(expectedDtd, documentDtd))
        throw new SAXException("The dtd " + documentDtd + " does not match the dtd expected from the specification");
    }
  }

  public boolean compareDtd(String cachedDtdUrl, String dtdUrl) {
    try {
      XMLGrammarPreparser grammerParser = getGrammarParser();

      DTDGrammar cachedDtd = (DTDGrammar)getGrammar(cachedDtdUrl);
      DTDGrammar dtd = (DTDGrammar)getGrammar(dtdUrl);
      XMLElementDecl cachedElement = new XMLElementDecl();
      XMLElementDecl element = new XMLElementDecl();
      XMLAttributeDecl cachedAttribute = new XMLAttributeDecl();
      XMLAttributeDecl attribute = new XMLAttributeDecl();
      for (int i = cachedDtd.getFirstElementDeclIndex(); i != -1; i = cachedDtd.getNextElementDeclIndex(i)) {
        if ((cachedDtd.getElementDecl(i, cachedElement)) && (dtd.getElementDecl(i, element))) {
          if (!cachedElement.name.equals(element.name)) {
            return false;
          }
          String cachedSpec = cachedDtd.getContentSpecAsString(i);
          String spec = dtd.getContentSpecAsString(i);
          if ((cachedSpec != spec) && (!cachedSpec.equals(spec))) {
            return false;
          }
          boolean moreAttributes = true;
          for (int j = cachedDtd.getFirstAttributeDeclIndex(i); j != -1; j = cachedDtd.getNextAttributeDeclIndex(j))
            if ((cachedDtd.getAttributeDecl(j, cachedAttribute)) && (dtd.getAttributeDecl(j, attribute))) {
              XMLSimpleType cachedType = cachedAttribute.simpleType;
              XMLSimpleType type = attribute.simpleType;
              if (!cachedAttribute.name.equals(attribute.name)) {
                return false;
              }
              if (cachedType.defaultType != type.defaultType) {
                return false;
              }
              if ((cachedType.defaultValue != type.defaultValue) && (!cachedType.defaultValue.equals(type.defaultValue))) {
                return false;
              }
              if (!Arrays.equals(cachedType.enumeration, type.enumeration)) {
                return false;
              }
              if (cachedType.list != type.list) {
                return false;
              }
              if (!cachedType.name.equals(type.name)) {
                return false;
              }
              if ((cachedType.nonNormalizedDefaultValue != type.nonNormalizedDefaultValue) && (!cachedType.nonNormalizedDefaultValue.equals(type.nonNormalizedDefaultValue))) {
                return false;
              }
              if (cachedType.type != type.type)
                return false;
            }
        }
      }
    }
    catch (Exception e)
    {
      return false;
    }
    return true;
  }

  private Grammar getGrammar(String urlString) throws Exception {
    URL url = new URL(urlString);
    InputStream in = url.openStream();
    return getGrammarParser().preparseGrammar("http://www.w3.org/TR/REC-xml", new XMLInputSource(urlString, urlString, urlString, in, null));
  }

  private XMLGrammarPreparser getGrammarParser() {
    XMLGrammarPreparser grammerParser = new XMLGrammarPreparser();
    grammerParser.registerPreparser("http://www.w3.org/2001/XMLSchema", null);
    grammerParser.registerPreparser("http://www.w3.org/TR/REC-xml", null);
    return grammerParser;
  }
}
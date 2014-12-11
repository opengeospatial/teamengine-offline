package com.galdosinc.glib.xml;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils
  implements XmlConstants
{
  public static final Object jaxpLock = new Object();

  public static String xml2str(Element element)
  {
    StringWriter out = new StringWriter();
    try {
      writeXml(element, out, 0);
    } catch (IOException localIOException) {
    }
    return out.toString();
  }

  public static String xml2str(Element element, int indent)
  {
    StringWriter out = new StringWriter();
    try {
      writeXml(element, out, indent);
    } catch (IOException localIOException) {
    }
    return out.toString();
  }

  public static void xml2file(Element element, String fileName)
    throws IOException
  {
    FileWriter out = new FileWriter(fileName, false);
    writeXml(element, out);
    out.close();
  }

  public static void xml2file(Document doc, String fileName)
    throws IOException
  {
    FileWriter out = new FileWriter(fileName, false);
    writeXml(doc.getDocumentElement(), out);
    out.close();
  }

  public static void xml2file(Element element, String fileName, int indent)
    throws IOException
  {
    FileWriter out = new FileWriter(fileName, false);
    writeXml(element, out, indent);
    out.close();
  }

  public static void writeXml(Element element, Writer out)
    throws IOException
  {
    writeXml(element, out, 0);
  }

  public static void writeXml(Element element, OutputStream os)
    throws IOException
  {
    OutputFormat format = new OutputFormat("XML", "UTF-8", false);
    format.setOmitComments(false);
    format.setLineWidth(0);
    XMLSerializer serializer = new XMLSerializer(format);
    serializer.setOutputByteStream(os);
    serializer.serialize(element);
  }

  public static void writeXml(Document document, OutputStream os)
    throws IOException
  {
    writeXml(document.getDocumentElement(), os);
  }

  public static void writeXml(Element element, Writer out, int indent)
    throws IOException
  {
    OutputFormat format = new OutputFormat("XML", "UTF-8", false);
    format.setOmitComments(false);
    if (indent > 0) {
      format.setIndenting(true);
      format.setIndent(indent);
    }
    format.setLineWidth(0);
    XMLSerializer serializer = new XMLSerializer(format);
    serializer.setOutputCharStream(out);
    serializer.serialize(element);
  }

  public static void writeXml(Document document, Writer out)
    throws IOException
  {
    writeXml(document, out, 0);
  }

  public static void writeXml(Document document, Writer out, int indent)
    throws IOException
  {
    OutputFormat format = new OutputFormat("XML", "UTF-8", false);
    format.setOmitComments(false);
    if (indent > 0) {
      format.setIndenting(true);
      format.setIndent(indent);
    }
    format.setLineWidth(0);
    XMLSerializer serializer = new XMLSerializer(format);
    serializer.setOutputCharStream(out);
    serializer.serialize(document);
  }

  public static Element appendNamespaceBinding(Document ownerDoc, Element target, String nsPrefix, String nsUri)
  {
    return appendNamespaceBinding(null, target, nsPrefix, nsUri);
  }

  public static Element appendNamespaceBinding(Element target, String nsPrefix, String nsUri)
  {
    if ((target.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", nsPrefix) == null) && 
      (nsPrefix != null)) {
      if (nsPrefix.equals("xmlns"))
        target.setAttribute("xmlns", nsUri);
      else {
        target.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + nsPrefix, nsUri);
      }
    }

    return target;
  }

  public static Document createDocument(Document document)
  {
    return createDocument(document.getDocumentElement());
  }

  public static Document createDocument(Element element)
  {
    Document newDocument = getJaxpDocBuilder().newDocument();
    Node node = newDocument.importNode(element, true);
    newDocument.appendChild(node);
    return newDocument;
  }

  public static String getPrefix(String qName)
  {
    int colonIndex = qName.indexOf(":");
    if (colonIndex != -1) {
      return qName.substring(0, colonIndex);
    }
    return qName;
  }

  public static String getLocalName(String qName)
  {
    int colonIndex = qName.indexOf(":");
    if (colonIndex != -1) {
      return qName.substring(colonIndex + 1);
    }
    return qName;
  }

  public static String constructQualifiedName(String prefix, String localName)
  {
    if (prefix == null) {
      return localName;
    }
    return prefix + ":" + localName;
  }

  public static String getElementValueByTagName(Element root, String nsUri, String tagName)
  {
    Element element = (Element)root.getElementsByTagNameNS(nsUri, tagName).item(0);

    return element == null ? null : element.getFirstChild().getNodeValue();
  }

  public static DocumentBuilder getJaxpDocBuilder()
  {
    try
    {
      synchronized (jaxpLock) {
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        System.setProperty("javax.xml.parsers.SaxParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
        DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
        docBuildFactory.setAttribute("http://apache.org/xml/features/dom/defer-node-expansion", Boolean.FALSE);
        docBuildFactory.setNamespaceAware(true);
        docBuildFactory.setValidating(false);
        return docBuildFactory.newDocumentBuilder();
      }
    } catch (ParserConfigurationException pce) {
      throw new RuntimeException(pce.getMessage());
    }
  }

  public static synchronized DocumentBuilder getJaxpDocBuilderNNS()
    throws ParserConfigurationException
  {
    try
    {
      System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
      System.setProperty("javax.xml.parsers.SaxParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
      DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
      docBuildFactory.setAttribute("http://apache.org/xml/features/dom/defer-node-expansion", new Boolean(false));
      docBuildFactory.setNamespaceAware(false);
      docBuildFactory.setValidating(false);
      return docBuildFactory.newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      throw new RuntimeException(pce.getMessage());
    }
  }

  public static String getDtdAsString(String uri) throws IOException {
    Reader in = null;
    if ((uri.startsWith("http")) || (uri.startsWith("ftp")) || (uri.startsWith("file:")))
      in = new InputStreamReader(new URL(uri).openStream());
    else {
      in = new FileReader(uri);
    }
    StringWriter out = new StringWriter();
    char[] buffer = new char[4096];
    for (int count = in.read(buffer); count != -1; count = in.read(buffer)) {
      out.write(buffer, 0, count);
    }
    return out.getBuffer().toString();
  }
}
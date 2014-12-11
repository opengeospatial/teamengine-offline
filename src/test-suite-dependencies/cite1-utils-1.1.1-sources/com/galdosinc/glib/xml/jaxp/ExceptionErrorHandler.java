package com.galdosinc.glib.xml.jaxp;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ExceptionErrorHandler extends DefaultHandler
{
  public void fatalError(SAXParseException spe)
    throws SAXException
  {
    throw spe;
  }

  public void error(SAXParseException spe) throws SAXException {
    throw spe;
  }

  public void warning(SAXParseException spe) throws SAXException {
    throw spe;
  }
}
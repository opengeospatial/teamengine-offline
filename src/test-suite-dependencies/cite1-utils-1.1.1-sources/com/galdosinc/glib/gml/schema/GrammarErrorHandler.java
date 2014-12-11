package com.galdosinc.glib.gml.schema;

import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLParseException;

public class GrammarErrorHandler
  implements XMLErrorHandler
{
  private SchemaErrorHandler schemaErrorHandler;

  public GrammarErrorHandler()
  {
    setSchemaErrorHandler(null);
  }

  public void warning(String domain, String key, XMLParseException exception) throws XNIException {
    this.schemaErrorHandler.error(exception.getMessage());
  }

  public void error(String domain, String key, XMLParseException exception) throws XNIException {
    this.schemaErrorHandler.error(exception.getMessage());
  }

  public void fatalError(String domain, String key, XMLParseException exception) throws XNIException {
    this.schemaErrorHandler.error(exception.getMessage());
  }

  public SchemaErrorHandler getSchemaErrorHandler()
  {
    return this.schemaErrorHandler;
  }

  public void setSchemaErrorHandler(SchemaErrorHandler schemaErrorHandler)
  {
    if (schemaErrorHandler == null)
      this.schemaErrorHandler = new PrintingSchemaErrorHandler();
    else
      this.schemaErrorHandler = schemaErrorHandler;
  }
}
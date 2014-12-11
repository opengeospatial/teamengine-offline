package com.galdosinc.glib.gml.schema;

import java.io.PrintStream;

public class PrintingSchemaErrorHandler
  implements SchemaErrorHandler
{
  boolean errorsReported = false;

  public void error(String message) {
    System.out.println(message);
    this.errorsReported = true;
  }

  public void error(String namespaceUri, String localName, String message)
  {
    System.out.println(namespaceUri + "#" + localName + ": " + message);
    this.errorsReported = true;
  }

  public boolean hasErrors()
  {
    return this.errorsReported;
  }
}
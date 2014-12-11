package com.galdosinc.glib.gml.schema;

public abstract interface SchemaErrorHandler
{
  public abstract void error(String paramString);

  public abstract void error(String paramString1, String paramString2, String paramString3);

  public abstract boolean hasErrors();
}
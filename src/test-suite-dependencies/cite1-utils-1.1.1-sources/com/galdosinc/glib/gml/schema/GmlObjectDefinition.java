package com.galdosinc.glib.gml.schema;

import java.util.Collection;

public abstract interface GmlObjectDefinition
{
  public abstract String getNamespaceUri();

  public abstract String getName();

  public abstract boolean isCollection();

  public abstract boolean isFeature();

  public abstract boolean isFeatureCollection();

  public abstract boolean isGeometry();

  public abstract boolean isGeometryCollection();

  public abstract boolean isTopology();

  public abstract boolean isTopoComplex();

  public abstract GmlPropertyDefinition getProperty(String paramString1, String paramString2);

  public abstract Collection getMandatoryPropertyNames();

  public abstract Collection getPropertyNames();

  public abstract boolean isMandatoryProperty(String paramString1, String paramString2);
}
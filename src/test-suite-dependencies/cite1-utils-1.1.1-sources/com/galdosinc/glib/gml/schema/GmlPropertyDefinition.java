package com.galdosinc.glib.gml.schema;

import java.util.List;

public abstract interface GmlPropertyDefinition
{
  public abstract String getNamespaceUri();

  public abstract String getName();

  public abstract int getMinOccurs();

  public abstract int getMaxOccurs();

  public abstract boolean isMandatory();

  public abstract boolean isGeometryProperty();

  public abstract boolean isTopologyProperty();

  public abstract boolean isSimpleValued();

  public abstract boolean canHaveRemoteValue();

  public abstract boolean canHaveLocalValue();

  public abstract List getGeometryTypeNames();

  public abstract List getTopologyTypeNames();

  public abstract boolean isFeatureArrayProperty();

  public abstract boolean isFeatureValueProperty();
}
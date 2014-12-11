package com.galdosinc.glib.gml.schema;

public class GmlVersion
{
  public static GmlVersion GML_2 = new GmlVersion(2);
  public static GmlVersion GML_3 = new GmlVersion(3);
  private int version_;

  private GmlVersion(int version)
  {
    if ((version != 2) && (version != 3)) {
      throw new IllegalArgumentException("Version code " + version + " is an invalid GML version.");
    }
    this.version_ = version;
  }
  public int getVersionNumber() {
    return this.version_;
  }
}
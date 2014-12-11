package com.galdosinc.glib.gml.bbox;

public class BoundingBoxException extends Exception
{
  private Exception cause_;

  public BoundingBoxException(String message)
  {
    super(message);
  }

  public BoundingBoxException(String message, Exception e)
  {
    super(message);
    this.cause_ = e;
  }
}
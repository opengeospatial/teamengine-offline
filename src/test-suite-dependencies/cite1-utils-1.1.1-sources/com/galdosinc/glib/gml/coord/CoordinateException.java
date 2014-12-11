package com.galdosinc.glib.gml.coord;

public class CoordinateException extends Exception
{
  private Exception e_;

  public CoordinateException(String msg, Exception e)
  {
    super(msg);
    this.e_ = e;
  }

  public CoordinateException(String msg)
  {
    super(msg);
  }

  public Exception getAttachedException()
  {
    return this.e_;
  }
}
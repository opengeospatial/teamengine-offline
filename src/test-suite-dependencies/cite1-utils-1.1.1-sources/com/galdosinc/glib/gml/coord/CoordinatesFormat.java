package com.galdosinc.glib.gml.coord;

public class CoordinatesFormat
{
  private String cs_ = ",";
  private String ts_ = " ";
  private String decimal_ = ".";
  private int maxDecDigits_ = 2147483647;
  public static final int NO_MAX_DECIMAL_DIGITS = 2147483647;

  public CoordinatesFormat()
  {
  }

  public CoordinatesFormat(String cs, String ts, String decimal, int maxDecDigits)
  {
    setCoordinateSeparator(cs);
    setTupleSeparator(ts);
    setDecimalPoint(decimal);
    setMaxDecimalDigits(maxDecDigits);
  }

  public String getTupleSeparator()
  {
    return this.ts_;
  }

  public String getCoordinateSeparator()
  {
    return this.cs_;
  }

  public String getDecimalPoint()
  {
    return this.decimal_;
  }

  public void setTupleSeparator(String ts)
  {
    if (ts.length() != 1) {
      throw new IllegalArgumentException("(" + ts + ") cannot be the coordinate tuple separator in coordinates.");
    }
    this.ts_ = ts;
  }

  public void setCoordinateSeparator(String cs)
  {
    if (cs.length() != 1) {
      throw new IllegalArgumentException("(" + cs + ") cannot be the coordinate separator in coordinates.");
    }
    this.cs_ = cs;
  }

  public void setMaxDecimalDigits(int maxDecDigits)
  {
    this.maxDecDigits_ = maxDecDigits;
  }

  public void setDecimalPoint(String decimal)
  {
    if (decimal.length() != 1) {
      throw new IllegalArgumentException("(" + decimal + ") cannot be the decimal point in coordinates.");
    }
    this.decimal_ = decimal;
  }

  public static String getDefaultTupleSeparator()
  {
    return " ";
  }

  public static String getDefaultCoordinateSeparator()
  {
    return ",";
  }

  public static String getDefaultDecimalPoint()
  {
    return ".";
  }

  public boolean isValid()
  {
    return (!this.decimal_.equals(this.ts_)) && (!this.decimal_.equals(this.cs_)) && (!this.ts_.equals(this.cs_));
  }

  public int getMaxDecimalDigits()
  {
    return this.maxDecDigits_;
  }
}
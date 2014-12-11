package com.galdosinc.glib.gml.coord;

import java.util.List;

public abstract interface CoordinateTupleList
{
  public static final int NO_DIMENSION = -1;

  public abstract int getDimension();

  public abstract int getCoordinateTupleCount();

  public abstract void setCoordinateTuples(double[][] paramArrayOfDouble, boolean paramBoolean)
    throws CoordinateException;

  public abstract void setCoordinateTuples(List paramList, boolean paramBoolean)
    throws CoordinateException;

  public abstract List asLiveList();

  public abstract List asRandomAccessList(boolean paramBoolean);

  public abstract List asSequentialAccessList(boolean paramBoolean);

  public abstract double[][] asArray(boolean paramBoolean);

  public abstract double[][] asArray(double[][] paramArrayOfDouble);
}
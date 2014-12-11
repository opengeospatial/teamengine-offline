package com.galdosinc.glib.gml.bbox;

import com.galdosinc.glib.gml.coord.CoordinateException;
import com.galdosinc.glib.gml.coord.CoordinateTupleLinkedList;
import com.galdosinc.glib.gml.coord.CoordinateTupleList;
import com.galdosinc.glib.gml.coord.CoordinateUtils;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;

public class BoundingBox {
    private boolean padded_;
    private static final double PADDING_CONSTANT = 1.0E-006D;
    private static final double[] EMPTY_BOX_COORDINATES = new double[0];
    private double[] lowerLeft_d;
    private double[] upperRight_d;

    public BoundingBox(double[] lowerLeft, double[] upperRight) {
        this.lowerLeft_d = CoordinateUtils.cloneCoordinateArray(lowerLeft, 0,
                lowerLeft.length);
        this.upperRight_d = CoordinateUtils.cloneCoordinateArray(upperRight, 0,
                upperRight.length);
    }

    public BoundingBox() {
        initialize();
    }

    public int getDimension() {
        return this.lowerLeft_d.length;
    }

    public BoundingBox(CoordinateTupleList coTupleList)
            throws BoundingBoxException {
        this();
        compute(coTupleList);
    }

    public BoundingBox(Element boxElement) throws BoundingBoxException {
        this();
        if (((!boxElement.getLocalName().equals("Box")) && (!boxElement
                .getLocalName().equals("Envelope")))
                || (boxElement.getNamespaceURI()
                        .equals("http://www.opengis.net/gml"))) {
            throw new BoundingBoxException("The passed element "
                    + boxElement.getLocalName()
                    + " is neither gml:Box nor gml:Envelope");
        }

        CoordinateTupleList coTupleList = new CoordinateTupleLinkedList();
        try {
            Object coReaderObject = CoordinateUtils
                    .readAllCoordinatesFromGeometry(boxElement, coTupleList);
        } catch (CoordinateException ce) {
            throw new BoundingBoxException(
                    "Could not instantiate a bounding box.", ce);
        }
        compute(coTupleList);
    }

    public static BoundingBox generateFromGmlObject(Element gmlObject)
            throws BoundingBoxException {
        CoordinateTupleList coTupleList = new CoordinateTupleLinkedList();
        try {
            CoordinateUtils.readAllCoordinatesBlindly(gmlObject, coTupleList);
        } catch (CoordinateException ce) {
            throw new BoundingBoxException(
                    "Could not compute the bounding box of GML object "
                            + gmlObject.getLocalName(), ce);
        }
        BoundingBox bbox = new BoundingBox(coTupleList);
        return bbox;
    }

    private void initialize() {
        this.lowerLeft_d = EMPTY_BOX_COORDINATES;
        this.upperRight_d = EMPTY_BOX_COORDINATES;
    }

    public boolean isEmpty() {
        return this.lowerLeft_d.length == 0;
    }

    public void union(BoundingBox bbox) throws BoundingBoxException {
        if (this.padded_) {
            throw new BoundingBoxException(
                    "The bounding box was padded because it was a point and now it is invalid to use it in bbox unions.");
        }

        if (bbox.isEmpty()) {
            return;
        }

        if (isEmpty()) {
            setBoundingBox(bbox);
            return;
        }

        if (getDimension() != bbox.getDimension()) {
            throw new BoundingBoxException(
                    "Cannot union bounding boxes of different dimensions "
                            + getDimension() + " and " + bbox.getDimension());
        }

        internalUnion(bbox.getLowerLeftTuple());
        internalUnion(bbox.getUpperRight());
    }

    public void union(double[] otherPoint) throws BoundingBoxException {
        if (this.padded_) {
            throw new BoundingBoxException(
                    "The bounding box was padded because it was a point and now it is invalid to use it in bbox unions.");
        }
        internalUnion(otherPoint);
    }

    private void internalUnion(double[] otherPoint) {
        for (int ii = 0; ii < otherPoint.length; ii++) {
            if (this.lowerLeft_d[ii] > otherPoint[ii]) {
                this.lowerLeft_d[ii] = otherPoint[ii];
            }
        }
        for (int ii = 0; ii < otherPoint.length; ii++)
            if (this.upperRight_d[ii] < otherPoint[ii])
                this.upperRight_d[ii] = otherPoint[ii];
    }

    public void setBoundingBox(BoundingBox bbox) throws BoundingBoxException {
        this.padded_ = false;
        if (bbox.isEmpty()) {
            initialize();
            return;
        }

        int otherBoxDim = bbox.getDimension();
        if (getDimension() != otherBoxDim) {
            this.lowerLeft_d = CoordinateUtils.cloneCoordinateArray(
                    bbox.getLowerLeftTuple(), 0, otherBoxDim);
            this.upperRight_d = CoordinateUtils.cloneCoordinateArray(
                    bbox.getUpperRight(), 0, otherBoxDim);
        } else {
            CoordinateUtils.coordinateArrayCopy(bbox.getLowerLeftTuple(), 0,
                    this.lowerLeft_d, 0, otherBoxDim);
            CoordinateUtils.coordinateArrayCopy(bbox.getUpperRight(), 0,
                    this.upperRight_d, 0, otherBoxDim);
        }
    }

    public double[] getLowerLeftTuple() {
        return this.lowerLeft_d;
    }

    public double[] getUpperRight() {
        return this.upperRight_d;
    }

    public double getMinX() {
        return this.lowerLeft_d[0];
    }

    public double getMinY() {
        return this.lowerLeft_d[1];
    }

    public double getMaxX() {
        return this.upperRight_d[0];
    }

    public double getMaxY() {
        return this.upperRight_d[1];
    }

    private void compute(CoordinateTupleList coTupleList) {
        if (coTupleList.getCoordinateTupleCount() == 0) {
            return;
        }

        List seqList = coTupleList.asSequentialAccessList(true);
        Iterator coIter = seqList.iterator();
        int dim = coTupleList.getDimension();
        if (dim == -1) {
            dim = 0;
        }
        this.lowerLeft_d = new double[dim];
        this.upperRight_d = new double[dim];

        double[] firstTuple = (double[]) coIter.next();
        CoordinateUtils.coordinateArrayCopy(firstTuple, 0, this.lowerLeft_d, 0,
                dim);
        CoordinateUtils.coordinateArrayCopy(firstTuple, 0, this.upperRight_d,
                0, dim);

        while (coIter.hasNext()) {
            double[] nextTuple = (double[]) coIter.next();
            internalUnion(nextTuple);
        }
    }

    public void pad() {
        if (CoordinateUtils.coordinateArraysEqual(this.lowerLeft_d,
                this.upperRight_d)) {
            this.padded_ = true;
            for (int ii = 0; ii < this.upperRight_d.length; ii++)
                this.upperRight_d[ii] += 1.0E-006D;
        }
    }

    public BoundingBox cloneBBox() {
        return new BoundingBox(getLowerLeftTuple(), getUpperRight());
    }
}
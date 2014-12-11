package com.galdosinc.glib.gml.coord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CoordinateTupleLinkedList implements CoordinateTupleList {
    private List tupleList_;
    private int dimension_ = -1;

    public CoordinateTupleLinkedList() {
        this.tupleList_ = new LinkedList();
    }

    public int getDimension() {
        return this.dimension_;
    }

    public int getCoordinateTupleCount() {
        return this.tupleList_.size();
    }

    public void setCoordinateTuples(double[][] tuples, boolean canBeAdopted)
            throws CoordinateException {
        LinkedList tupleList = new LinkedList();
        for (int ii = 0; ii < tuples.length; ii++) {
            tupleList.add(tuples[ii]);
        }
        setCoordinateTuplesInternal(tupleList);
    }

    public void setCoordinateTuples(List tupleList, boolean canBeAdopted)
            throws CoordinateException {
        if ((canBeAdopted) && ((tupleList instanceof LinkedList))) {
            setCoordinateTuplesInternal((LinkedList) tupleList);
        } else {
            LinkedList newTupleList = new LinkedList();
            Iterator otherTupleListIter = newTupleList.iterator();
            while (otherTupleListIter.hasNext()) {
                double[] otherTuple = (double[]) otherTupleListIter.next();
                double[] newTuple = new double[otherTuple.length];
                System.arraycopy(otherTuple, 0, newTuple, 0, otherTuple.length);
                newTupleList.add(newTuple);
            }
            setCoordinateTuplesInternal(newTupleList);
        }
    }

    private void setCoordinateTuplesInternal(LinkedList tupleList)
            throws CoordinateException {
        this.tupleList_.clear();
        this.tupleList_.addAll(tupleList);
        if (this.tupleList_.isEmpty()) {
            this.dimension_ = -1;
        } else {
            Object firstItem = this.tupleList_.get(0);
            if ((firstItem instanceof double[]))
                this.dimension_ = ((double[]) firstItem).length;
            else
                throw new CoordinateException(
                        "The members of the coordinate tuple list must be arrays of doubles.",
                        null);
        }
    }

    private double[] cloneTouple(double[] source) {
        double[] clone = new double[source.length];
        System.arraycopy(source, 0, clone, 0, source.length);
        return clone;
    }

    private void cloneListMembers(List source, List target) {
        Iterator tupleIter = source.iterator();
        while (tupleIter.hasNext()) {
            double[] nextTuple = (double[]) tupleIter.next();
            double[] clonedNextTouple = cloneTouple(nextTuple);
            target.add(clonedNextTouple);
        }
    }

    public List asLiveList() {
        return this.tupleList_;
    }

    public List asRandomAccessList(boolean allowLiveList) {
        List result = new ArrayList();
        cloneListMembers(this.tupleList_, result);
        return result;
    }

    public List asSequentialAccessList(boolean allowLiveList) {
        if (allowLiveList) {
            return this.tupleList_;
        }
        List result = new LinkedList();
        cloneListMembers(this.tupleList_, result);
        return result;
    }

    public double[][] asArray(boolean allowLiveArray) {
        if (this.tupleList_.size() == 0) {
            return new double[0][0];
        }
        double[][] result = new double[this.tupleList_.size()][];
        this.tupleList_.toArray(result);
        return result;
    }

    public double[][] asArray(double[][] tuples) {
        this.tupleList_.toArray(tuples);
        return tuples;
    }
}
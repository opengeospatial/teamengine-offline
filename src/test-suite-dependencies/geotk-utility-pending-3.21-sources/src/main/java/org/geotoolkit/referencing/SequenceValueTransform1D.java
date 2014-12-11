/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2012, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.referencing;

import java.util.Arrays;
import org.geotoolkit.referencing.operation.transform.AbstractMathTransform1D;
import org.geotoolkit.util.ArgumentChecks;
import org.geotoolkit.util.XArrays;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * {@link MathTransform1D} with linear interpolation between values.
 *
 * @author Johann Sorel (Geomatys)
 * @author Remi Marechal (Geomatys).
 */
public final class SequenceValueTransform1D extends AbstractMathTransform1D {
    private final double[] antecedent;
    private final double[] values;
    private boolean isIncreaseOrder = true;
    private final int l;

    /**
     * <p>In this case an antecedents default table is construct such as : <br/><br/>
     *
     * [0, 1, ... , N] with N is length of image table values.<p>
     *
     * @param values image from antecedents table values.
     */
    private SequenceValueTransform1D(double[] values) {
        ArgumentChecks.ensureNonNull("values", values);
        this.l          = values.length;
        if (l < 2)
            throw new IllegalArgumentException("table must have more than only two values");
        this.values     = values;
        this.antecedent = new double[l];
        for (int v = 0;v<l;v++) this.antecedent[v] = v;
    }

    /**
     * Two tables such as : f(antecedents) = values.
     *
     * @param antecedents "abscissa" table values.
     * @param values image from antecedents table values.
     */
    private SequenceValueTransform1D(double[] antecedents, double[] values) {
        ArgumentChecks.ensureNonNull("values", values);
        ArgumentChecks.ensureNonNull("antecedents", antecedents);
        this.l = antecedents.length;
        if (l<2)
            throw new IllegalArgumentException("table must have more than only two values");
        if (l != values.length)
            throw new IllegalArgumentException("antecedents and values table must have same length");
        if (!XArrays.isSorted(antecedents, true)) {
            final double[] antecedent2 = new double[l];
            int id = l;
            for (int i = 0; i < l; i++) antecedent2[i] = antecedents[--id];
            if (!XArrays.isSorted(antecedent2, true))
                throw new IllegalArgumentException("antecedents table must be strictly increasing or decreasing");
            isIncreaseOrder = false;
        }
        this.antecedent = antecedents;
        this.values     = values;
    }

    /**
     * {@inheritDoc }.
     */
    @Override
    public double transform(double d) throws TransformException {
        int ida, idb, idn, idn1;
        if (isIncreaseOrder) {
            ida = 0; idb  = l-1;
            idn = 0; idn1 = 1;
        } else {
            ida = l-1; idb  = 0;
            idn = 1;   idn1 = 0;
        }
        if (d <= antecedent[ida]) return values[ida];
        if (d >= antecedent[idb]) return values[idb];
        double x0, x1;
        for (int id = 0; id < l-1; id++) {
            x0 = antecedent[idn];
            x1 = antecedent[idn1];
            if      (d == x0) return values[idn];
            else if (d == x1) return values[idn1];
            else if (d > antecedent[idn] && d < antecedent[idn1])
                return values[idn] + (values[idn1] - values[idn]) * ((d-x0) / (x1-x0));
            idn++;idn1++;
        }
        return 0;//impossible
    }

    /**
     * {@inheritDoc }.
     * Note : for each segment lower sequence point derivative is inclusive
     * whereas upper sequence point is exclusive.
     */
    @Override
    public double derivative(double d) throws TransformException {
        int idn, idn1;
        if (isIncreaseOrder) {
            idn  = 0; idn1 = 1;
            if (d < antecedent[0] || d >= antecedent[l-1]) return 0;
        } else {
            idn  = 1; idn1 = 0;
            if (d <= antecedent[l-1] || d > antecedent[0]) return 0;
        }
        for (int id = 0; id < l; id++) {
            if ((d > antecedent[idn]  && d < antecedent[idn1]) || d == antecedent[id])
                return (values[idn1]-values[idn]) / (antecedent[idn1]-antecedent[idn]);
            idn++;idn1++;
        }
        return 0;//impossible
    }

    @Override
    public MathTransform1D inverse() throws NoninvertibleTransformException {
        if (!XArrays.isSorted(values, true)) {
            final double[] values2 = new double[l];
            int id = l;
            for (int i = 0; i < l; i++) values2[i] = values[--id];
            if (!XArrays.isSorted(values2, true))
                throw new NoninvertibleTransformException("non inversible");
        }
        return new SequenceValueTransform1D(values, antecedent);
    }

    @Override
    public boolean isIdentity() {
        return Arrays.equals(antecedent, values);
    }

    public static SequenceValueTransform1D create(double[] values){
        return new SequenceValueTransform1D(values);
    }

    public static SequenceValueTransform1D create(double[] antecedents, double[] values) {
        return new SequenceValueTransform1D(antecedents, values);
    }

}

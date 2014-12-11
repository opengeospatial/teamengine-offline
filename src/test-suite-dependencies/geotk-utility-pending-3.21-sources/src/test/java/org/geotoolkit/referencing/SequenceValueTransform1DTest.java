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

import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * Test {@link SequenceValueTransform1D} class.
 *
 * @author Remi Marechal (Geomatys).
 */
public class SequenceValueTransform1DTest {

    private double[] antecedent, values;

    public SequenceValueTransform1DTest() {
    }

    /**
     * <p>antecedents in increasing order.<br/>
     * values in increasing order.</p>
     * @throws TransformException
     */
    @Test
    public void testIncreaseIncrease() throws TransformException{
        antecedent = new double[]{0,1,2,3};
        values     = new double[]{10,12,16,22};
        testMath(true);
    }

    /**
     * <p>antecedents in increasing order.<br/>
     * values in decreasing order.</p>
     * @throws TransformException
     */
    @Test
    public void testIncreaseDecrease() throws TransformException{
        antecedent = new double[]{0,1,2,3};
        values     = new double[]{35,27,22,5};
        testMath(true);
    }

    /**
     * <p>antecedents in decreasing order.<br/>
     * values in increasing order.</p>
     * @throws TransformException
     */
    @Test
    public void testDecreaseIncrease() throws TransformException{
        antecedent = new double[]{2,-5,-96,-207};
        values     = new double[]{-50,-20,7,105};
        testMath(true);
    }

    /**
     * <p>antecedents in decreasing order.<br/>
     * values in decreasing order.</p>
     * @throws TransformException
     */
    @Test
    public void testDecreaseDecrease() throws TransformException{
        antecedent = new double[]{2,-5,-96,-207};
        values     = new double[]{105,7,-19,-43};
        testMath(true);
    }

    /**
     * <p>antecedents in increasing order.<br/>
     * values in random order.</p>
     * @throws TransformException
     */
    @Test
    public void testIncreaseRandom() throws TransformException{
        antecedent = new double[]{-52,-27,-13,2};
        values     = new double[]{105,-19,7,-43};
        testMath(false);
    }

    /**
     * <p>antecedents in increasing order.<br/>
     * values in random order.</p>
     * @throws TransformException
     */
    @Test
    public void testDecreaseRandom() throws TransformException{
        antecedent = new double[]{1017,525,24,12};
        values     = new double[]{-43,7,-19,105};
        testMath(false);
    }

    /**
     * <p>antecedents in increasing order.<br/>
     * values in random order.</p>
     * @throws TransformException
     */
    @Test
    public void testDcnsPercent() throws TransformException{
        antecedent = new double[]{5, 6.5, 8, 10, 25, 28, 30, 32};
        values     = new double[]{100, 66, 33, 0, 0, 33, 66, 100};
        testMath(false);
    }

    /**
     * Test fail.
     */
    @Test
    public void testFail() throws TransformException{
        antecedent = new double[]{-43,7,-19,105};
        values     = new double[]{1017,525,24,12};
        try {
            MathTransform1D mt = SequenceValueTransform1D.create(antecedent, values);
            Assert.fail("test should had failed");
        } catch (Exception e) {
            //ok
        }

        antecedent = new double[]{1017,525,24,12};
        values     = new double[]{-43,7,-19,105};
        MathTransform1D mt = SequenceValueTransform1D.create(antecedent, values);
        try {
            mt.inverse();
            Assert.fail("test should had failed");
        } catch (Exception e) {
            //ok
        }
        antecedent = new double[]{1017,525,24,12,45};
        values     = new double[]{-43,7,-19,105};
        try {
            mt = SequenceValueTransform1D.create(antecedent, values);
            Assert.fail("test should had failed");
        } catch (Exception e) {
            //ok
        }
    }

    /**
     * Test MathTransform.
     *
     * @param testInvert apply test on invert transform if true else not.
     * @throws NoninvertibleTransformException
     * @throws TransformException
     */
    private void testMath (boolean testInvert) throws NoninvertibleTransformException, TransformException {
        final MathTransform1D math1 = SequenceValueTransform1D.create(antecedent, values);
        MathTransform1D mathInvert = null;
        if (testInvert) mathInvert = math1.inverse();
        int step = 10;
        final int l = antecedent.length;
        for (int i = 0; i < l-1; i++) {
            final double a0    = antecedent[i];
            final double v0    = values[i];
            double pasX        = antecedent[i+1] - a0;
            double pasY        = values[i+1] - v0;
            final double deriv = pasY / pasX;
            pasX /= step;
            pasY /= step;
            for (int s = 0; s < step; s++) {
                //mathtransform
                final double x = a0 + s*pasX;
                final double y = math1.transform(x);
                //derivative
                assertTrue(y-(v0+s*pasY) <= 1E-9);
                if (s != step-1) assertTrue(math1.derivative(x) - deriv <= 1E-9);
                if (testInvert) {
                    //inverse transform
                    assertTrue(mathInvert.transform(y)-x <= 1E-9);
                    //inverse derivative
                    if (s != step-1) assertTrue(mathInvert.derivative(y)-(1/deriv) <= 1E-9);
                }
            }
        }
    }
}

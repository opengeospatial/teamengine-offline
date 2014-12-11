/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2006-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009-2012, Geomatys
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
package org.geotoolkit.referencing.operation.matrix;

import java.awt.geom.AffineTransform;

import org.junit.*;
import static org.junit.Assert.*;
import static java.lang.StrictMath.*;


/**
 * Tests {@link XAffineTransform} static methods.
 *
 * @author Martin Desruisseaux (IRD)
 * @version 3.14
 *
 * @since 2.3
 */
public final strictfp class XAffineTransformTest {
    /**
     * Tolerance value for comparisons.
     */
    private static final double EPS = 1E-10;

    /**
     * Tests {@link XAffineTransform} in the unflipped case.
     */
    @Test
    public void testUnflipped() {
        runTest(+1);
    }

    /**
     * Tests {@link XAffineTransform} in the flipped case.
     */
    @Test
    public void testFlipped() {
        runTest(-1);
    }

    /**
     * Gets the flip state using standard {@code AffineTransform} API.
     */
    private static int getFlipFromType(final AffineTransform tr) {
        return (tr.getType() & AffineTransform.TYPE_FLIP) != 0 ? -1 : +1;
    }

    /**
     * Run the test in the flipped or unflipped case.
     *
     * @param f -1 for the flipped case, or +1 for the unflipped case.
     */
    private static void runTest(final int f) {
        // Test identity
        final AffineTransform tr = new AffineTransform();
        tr.setToScale(1, f);
        assertEquals( 1, XAffineTransform.getScaleX0 (tr), EPS);
        assertEquals( 1, XAffineTransform.getScaleY0 (tr), EPS);
        assertEquals( 0, XAffineTransform.getRotation(tr), EPS);
        assertEquals( 1, XAffineTransform.getSwapXY  (tr));
        assertEquals( f, XAffineTransform.getFlip    (tr));
        assertEquals( f, getFlipFromType             (tr));

        // Tests rotation (< 45°)
        double r = toRadians(25);
        tr.rotate(r);
        assertEquals( 1, XAffineTransform.getScaleX0 (tr), EPS);
        assertEquals( 1, XAffineTransform.getScaleY0 (tr), EPS);
        assertEquals( r, XAffineTransform.getRotation(tr), EPS);
        assertEquals( 1, XAffineTransform.getSwapXY  (tr));
        assertEquals( f, XAffineTransform.getFlip    (tr));
        assertEquals( f, getFlipFromType             (tr));

        // Tests more rotation (> 45°)
        r = toRadians(65);
        tr.rotate(toRadians(40));
        assertEquals( 1, XAffineTransform.getScaleX0 (tr), EPS);
        assertEquals( 1, XAffineTransform.getScaleY0 (tr), EPS);
        assertEquals( r, XAffineTransform.getRotation(tr), EPS);
        assertEquals(-1, XAffineTransform.getSwapXY  (tr));
        assertEquals( f, XAffineTransform.getFlip    (tr));
        assertEquals( f, getFlipFromType             (tr));

        // Tests scale
        tr.setToScale(2, 3*f);
        assertEquals( 2, XAffineTransform.getScaleX0 (tr), EPS);
        assertEquals( 3, XAffineTransform.getScaleY0 (tr), EPS);
        assertEquals( 0, XAffineTransform.getRotation(tr), EPS);
        assertEquals( 1, XAffineTransform.getSwapXY  (tr));
        assertEquals( f, XAffineTransform.getFlip    (tr));
        assertEquals( f, getFlipFromType             (tr));

        // Tests rotation + scale
        tr.rotate(r);
        assertEquals( 2, XAffineTransform.getScaleX0 (tr), EPS);
        assertEquals( 3, XAffineTransform.getScaleY0 (tr), EPS);
        assertEquals( r, XAffineTransform.getRotation(tr), EPS);
        assertEquals(-1, XAffineTransform.getSwapXY  (tr));
        assertEquals( f, XAffineTransform.getFlip    (tr));
        assertEquals( 1, getFlipFromType(tr)); // Always unflipped according Java 1.5.0_09...

        // Tests axis swapping
        r = toRadians(-90 * f);
        tr.setTransform(0, 1, f, 0, 0, 0);
        assertEquals( 1, XAffineTransform.getScaleX0 (tr), EPS);
        assertEquals( 1, XAffineTransform.getScaleY0 (tr), EPS);
        assertEquals( r, XAffineTransform.getRotation(tr), EPS);
        assertEquals(-1, XAffineTransform.getSwapXY  (tr));
        assertEquals(-f, XAffineTransform.getFlip    (tr));
        assertEquals(-f, getFlipFromType             (tr));

        // Tests axis swapping + scale
        tr.scale(2, 3);
        assertEquals( 3, XAffineTransform.getScaleX0 (tr), EPS);
        assertEquals( 2, XAffineTransform.getScaleY0 (tr), EPS);
        assertEquals( r, XAffineTransform.getRotation(tr), EPS);
        assertEquals(-1, XAffineTransform.getSwapXY  (tr));
        assertEquals(-f, XAffineTransform.getFlip    (tr));
        assertEquals(-f, getFlipFromType             (tr));
    }

    /**
     * Tests the {@link XAffineTransform#roundIfAlmostInteger} method.
     */
    @Test
    public void testRoundIfAlmostInteger() {
        final AffineTransform test = new AffineTransform(4, 0, 0, 4, -400, -1186);
        final AffineTransform copy = new AffineTransform(test);
        XAffineTransform.roundIfAlmostInteger(test, 1E-6);
        assertEquals("Translation terms were already integers, so the " +
                "transform should not have been modified.", copy, test);

        test.translate(1E-8, 2E-8);
        XAffineTransform.roundIfAlmostInteger(test, 1E-9);
        assertFalse("Treshold was smaller than the translation, so the " +
                "transform should not have been modified.", copy.equals(test));

        XAffineTransform.roundIfAlmostInteger(test, 1E-6);
        assertEquals("Translation terms should have been rounded.", copy, test);
    }
}

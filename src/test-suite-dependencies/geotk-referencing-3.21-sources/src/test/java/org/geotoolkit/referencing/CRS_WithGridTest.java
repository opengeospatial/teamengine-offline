/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2009-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotoolkit.referencing;

import org.opengis.geometry.Envelope;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.geotoolkit.internal.io.Installation;
import org.geotoolkit.geometry.GeneralEnvelope;
import org.geotoolkit.geometry.DirectPosition2D;

import org.geotoolkit.test.Depend;
import org.geotoolkit.test.referencing.ReferencingTestBase;

import org.junit.*;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
import static org.geotoolkit.referencing.Commons.*;


/**
 * Tests the combination of EPSG database with grids like NADCON.
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.20
 *
 * @since 3.00
 */
@Depend(CRS_WithEpsgTest.class)
public final strictfp class CRS_WithGridTest extends ReferencingTestBase {
    /**
     * Tests transformation NADCON grids.
     *
     * @throws Exception Should not happen.
     */
    @Test
    public void testNADCON() throws Exception {
        assumeTrue(Installation.NADCON.directory(true).isDirectory());
        assumeTrue(isEpsgFactoryAvailable());

        final MathTransform tr;
        final DirectPosition2D sourcePt, targetPt;
        final CoordinateReferenceSystem sourceCRS, targetCRS;

        sourceCRS = CRS.decode("EPSG:26769"); // NAD27 Idaho, in feets.
        targetCRS = CRS.decode("EPSG:26969"); // NAD83 Idaho, in metres.
        sourcePt  = new DirectPosition2D(30000.0, 40000.0);
        targetPt  = new DirectPosition2D();
        tr = CRS.findMathTransform(sourceCRS, targetCRS);
        assertSame(targetPt, tr.transform(sourcePt, targetPt));

        assertEquals(356671.38, targetPt.x, 1E-2);
        assertEquals( 12183.11, targetPt.y, 1E-2);
    }

    /**
     * Tests a transform from "<cite>Réseau Géodésique Français 1993</cite>" to
     * "<cite>Nouvelle Triangulation Française (Paris)</cite>". This transform uses
     * the inverse of a datum shift grid.
     *
     * @throws Exception Should not happen.
     *
     * @since 3.20
     */
    @Test
    public void testNTF() throws Exception {
        assumeTrue(Installation.NADCON.directory(true).isDirectory());
        assumeTrue(isEpsgFactoryAvailable());

        final CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:2154");  // Réseau Géodésique Français 1993
        final CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:27582"); // Nouvelle Triangulation Française (Paris)
        final GeneralEnvelope source = new GeneralEnvelope("BOX2D(-2000000 4000000, 2000000 4000000)");
        source.setCoordinateReferenceSystem(sourceCRS);
        final Envelope target = CRS.transform(source, targetCRS);

        assertEquals(-2033792.23, target.getMinimum(0), 1E-2);
        assertEquals( 1976167.67, target.getMaximum(0), 1E-2);
        assertEquals( -458155.31, target.getMinimum(1), 1E-2);
        assertEquals( -426020.22, target.getMaximum(1), 1E-2);
    }
}

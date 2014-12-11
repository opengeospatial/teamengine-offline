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
package org.geotoolkit.metadata;

import java.util.Map;
import java.util.HashMap;

import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.util.FactoryException;

import org.geotoolkit.factory.FactoryFinder;
import org.geotoolkit.referencing.crs.DefaultVerticalCRS;

import org.junit.*;
import static org.junit.Assert.*;
import static org.geotoolkit.referencing.datum.DefaultVerticalDatum.GEOIDAL;
import static org.geotoolkit.referencing.cs.DefaultVerticalCS.GRAVITY_RELATED_HEIGHT;



/**
 * Tests the {@link FactoryMethod} class. This test is defined in the referencing module instead
 * than the metadata module in order to include the {@link CRSFactory} implementation in the test.
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.03
 *
 * @since 3.03
 */
public final strictfp class FactoryMethodTest {
    /**
     * Tests the obtention of a factory method.
     */
    @Test
    public void testFind() {
        final Object[] crsFactory = FactoryFinder.getCRSFactories(null).toArray();
        FactoryMethod fm;

        fm = FactoryMethod.find(Citation.class, crsFactory);
        assertNull("The CRS factory can not create Citation objects.", fm);

        fm = FactoryMethod.find(VerticalCRS.class, crsFactory);
        assertNotNull("The CRSFactory.createVerticalCRS(Map, ...) method should have been found.", fm);
        assertEquals(fm, FactoryMethod.find(VerticalCRS.class, crsFactory));
    }

    /**
     * Tests the creation of an object.
     *
     * @throws FactoryException Should never happen.
     */
    @Test
    public void testCreate() throws FactoryException {
        final Object[] crsFactory = FactoryFinder.getCRSFactories(null).toArray();
        final FactoryMethod fm = FactoryMethod.find(VerticalCRS.class, crsFactory);
        final Map<String,Object> properties = new HashMap<String,Object>();
        assertNull(properties.put("datum", GEOIDAL));
        assertNull(properties.put("cs",    GRAVITY_RELATED_HEIGHT));
        assertNull(properties.put("name", "Geoidal height"));

        final Object crs = fm.create(properties);
        assertTrue(crs instanceof VerticalCRS);
        assertEquals(new DefaultVerticalCRS("Geoidal height", GEOIDAL, GRAVITY_RELATED_HEIGHT), crs);
    }
}

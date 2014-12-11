/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2007-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotoolkit.util.logging;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link Logging} class.
 *
 * @author Martin Desruisseaux (IRD)
 * @version 3.00
 *
 * @since 2.4
 */
public final strictfp class LoggingTest {
    /**
     * Tests {@link Logging#getLogger(Class)}.
     */
    @Test
    public void testGet() {
        assertEquals("org.geotoolkit.util.logging", Logging.getLogger(Logging.class).getName());
    }

    /**
     * Checks {@link Logging#GEOTOOLKIT}.
     */
    @Test
    public void testGeotoolkit() {
        assertNull(Logging.ALL.getLoggerFactory());
        assertNull(Logging.GEOTOOLKIT.getLoggerFactory());
        assertEquals("",             Logging.ALL.name);
        assertEquals("org.geotoolkit", Logging.GEOTOOLKIT.name);
        assertEquals(0,              Logging.GEOTOOLKIT.getChildren().length);
        Logging[] children =         Logging.ALL.getChildren();
        assertEquals(1,              children.length);
        assertEquals("org",          children[0].name);
        assertSame(children[0],      Logging.getLogging("org"));
        children =                   children[0].getChildren();
        assertEquals(1,              children.length);
        assertSame(Logging.GEOTOOLKIT, children[0]);
        assertSame(Logging.ALL,      Logging.getLogging(""));
        assertSame(Logging.GEOTOOLKIT, Logging.getLogging("org.geotoolkit"));
    }
}

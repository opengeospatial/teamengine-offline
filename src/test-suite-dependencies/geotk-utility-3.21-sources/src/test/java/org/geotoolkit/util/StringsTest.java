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
package org.geotoolkit.util;

import org.junit.*;
import static org.junit.Assert.*;

import static org.geotoolkit.util.Strings.*;


/**
 * Tests {@link Strings} methods.
 *
 * @author Martin Desruisseaux (Geomatys)
 * @author Johann Sorel (Geomatys)
 * @version 3.20
 *
 * @since 3.09 (derived from 3.00).
 */
public final strictfp class StringsTest {
    /**
     * Tests {@link Strings#spaces(int)}.
     */
    @Test
    public void testSpaces() {
        assertEquals("",         spaces(0));
        assertEquals(" ",        spaces(1));
        assertEquals("        ", spaces(8));
    }

    /**
     * Tests {@link Strings#count(String, String)} and its variants.
     */
    @Test
    public void testCount() {
        assertEquals(0, count("An ordinary sentence.",   '-'));
        assertEquals(4, count("- this one has -dashs--", '-'));
        assertEquals(2, count("An ordinary sentence.",  "en"));
    }

    /**
     * Tests {@link Strings#split(String, char)}.
     *
     * @since 3.18
     */
    @Test
    public void testSplit() {
        assertArrayEquals(new String[] {"lundi", "mardi", "mercredi"},
                Strings.split("lundi , mardi,mercredi ", ','));
    }

    /**
     * Tests {@link Strings#parseFloats(String, char)}.
     *
     * @since 3.19
     */
    @Test
    public void testFloats() {
        assertArrayEquals(new float[] {5, 1.5f, Float.NaN, -8},
                Strings.parseFloats("5 , 1.5,, -8 ", ','), 0f);
    }

    /**
     * Tests the {@link Strings#indexOf(CharSequence, CharSequence, int)} method.
     * We test four time with different kind of character sequences.
     *
     * @since 3.16
     */
    @Test
    public void testIndexOf() {
        for (int i=0; i<4; i++) {
            CharSequence string = "An ordinary sentence.";
            switch (i) {
                case 0:  /* Test directly on the String instance. */              break;
                case 1:  string = new StringBuilder            ((String) string); break;
                case 2:  string = new StringBuffer             ((String) string); break;
                case 3:  string = new SimpleInternationalString((String) string); break;
                default: throw new AssertionError(i);
            }
            assertEquals(-1, indexOf(string, "dummy",        0));
            assertEquals( 0, indexOf(string, "An",           0));
            assertEquals(-1, indexOf(string, "An",           1));
            assertEquals(12, indexOf(string, "sentence.",    0));
            assertEquals(-1, indexOf(string, "sentence;",    0));
        }
    }

    /**
     * Tests the {@link Strings#replace(StringBuilder, String, String)} method.
     */
    @Test
    public void testReplace() {
        final StringBuilder buffer = new StringBuilder("One two three two one");
        replace(buffer, "two", "zero");
        assertEquals("One zero three zero one", buffer.toString());
        replace(buffer, "zero", "ten");
        assertEquals("One ten three ten one", buffer.toString());
    }

    /**
     * Tests the {@link Strings#replace(StringBuilder, int, int, char[])} method.
     *
     * @since 3.20
     */
    @Test
    public void testReplaceChars() {
        final StringBuilder buffer = new StringBuilder("ABCD1234EFGH");
        replace(buffer, 4, 8, new char[] {'5','6','7','8'});
        assertEquals("ABCD5678EFGH", buffer.toString());
        replace(buffer, 4, 6, new char[] {'1','2','3','4'});
        assertEquals("ABCD123478EFGH", buffer.toString());
        replace(buffer, 8, 10, new char[] {'a','b','c','d'});
        assertEquals("ABCD1234abcdEFGH", buffer.toString());
    }

    /**
     * Tests the {@link Strings#remove(StringBuilder, String)} method.
     */
    @Test
    public void testRemove() {
        final StringBuilder buffer = new StringBuilder("EPSG.6.7");
        remove(buffer, ".");
        assertEquals("EPSG67", buffer.toString());
    }

    /**
     * Tests the {@link Strings#trim(String)} method.
     *
     * @since 3.20
     */
    @Test
    public void testTrim() {
        assertEquals("A text.", trim("  A text. "));
    }

    /**
     * Tests the {@link Strings#trimFractionalPart(String)} method.
     */
    @Test
    public void testTrimFractionalPart() {
        assertEquals("4",    trimFractionalPart("4"));
        assertEquals("4",    trimFractionalPart("4."));
        assertEquals("4",    trimFractionalPart("4.0"));
        assertEquals("4",    trimFractionalPart("4.00"));
        assertEquals("4.10", trimFractionalPart("4.10"));
    }

    /**
     * Tests the {@link Strings#token(CharSequence, int)} method.
     *
     * @since 3.18
     */
    @Test
    public void testToken() {
        assertEquals("Id4", token("..Id4  56B..", 2));
        assertEquals("56",  token("..Id4  56B..", 6));
    }

    /**
     * Tests the {@link Strings#toASCII(CharSequence)} method.
     *
     * @since 3.18
     */
    @Test
    public void testToASCII() {
        final String metre = "metre";
        assertSame  (metre, toASCII(metre));
        assertEquals(metre, toASCII("mètre").toString());
    }

    /**
     * Tests the {@link Strings#camelCaseToWords(CharSequence, boolean)} method.
     */
    @Test
    public void testCamelCaseToWords() {
        final CharSequence convert = camelCaseToWords("PixelInterleavedSampleModel", true);
        assertEquals("Pixel interleaved sample model", convert.toString());
    }

    /**
     * Tests the {@link Strings#getLinesFromMultilines(String)} method.
     */
    @Test
    public void testGetLinesFromMultilines() {
        final String[] splitted = getLinesFromMultilines("\nOne\r\nTwo\rThree\rFour\nFive\n\rSix\n");
        assertArrayEquals(new String[] {
            "",
            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "",
            "Six",
            ""
        }, splitted);
    }

    /**
     * Tests the {@link Strings#camelCaseToAcronym(String)} method.
     */
    @Test
    public void testCamelCaseToAcronym() {
        assertEquals("OGC", camelCaseToAcronym("OGC"));
        assertEquals("OGC", camelCaseToAcronym("Open Geospatial Consortium"));
        assertEquals("E",   camelCaseToAcronym("East"));
        assertEquals("NE",  camelCaseToAcronym("North-East"));
        assertEquals("NE",  camelCaseToAcronym("NORTH_EAST"));
        assertEquals("NE",  camelCaseToAcronym("northEast"));
    }

    /**
     * Tests the {@link Strings#isAcronymForWords(CharSequence, CharSequence)} method.
     */
    @Test
    public void testIsAcronymForWords() {
        /*
         * Following should be accepted as acronyms...
         */
        assertTrue(isAcronymForWords("OGC",                        "Open Geospatial Consortium"));
        assertTrue(isAcronymForWords("O.G.C.",                     "Open Geospatial Consortium"));
        assertTrue(isAcronymForWords("OpGeoCon",                   "Open Geospatial Consortium"));
        assertTrue(isAcronymForWords("Open Geospatial Consortium", "Open Geospatial Consortium"));
        assertTrue(isAcronymForWords("ogc",                        "Open Geospatial Consortium"));
        /*
         * Following should be rejected...
         */
        assertFalse(isAcronymForWords("ORC",    "Open Geospatial Consortium"));
        assertFalse(isAcronymForWords("O.C.G.", "Open Geospatial Consortium"));
        assertFalse(isAcronymForWords("OGC2",   "Open Geospatial Consortium"));
        assertFalse(isAcronymForWords("OG",     "Open Geospatial Consortium"));
        assertFalse(isAcronymForWords("GC",     "Open Geospatial Consortium"));
        /*
         * Following are mapping of EPSG table names from MS-Access to ANSI SQL.
         * All those items must be recognized as acroynms - this is requred by DirectEpsgFactory.
         */
        assertTrue(isAcronymForWords("alias",                     "[Alias]"));
        assertTrue(isAcronymForWords("area",                      "[Area]"));
        assertTrue(isAcronymForWords("coordinateaxis",            "[Coordinate Axis]"));
        assertTrue(isAcronymForWords("coordinateaxisname",        "[Coordinate Axis Name]"));
        assertTrue(isAcronymForWords("coordoperation",            "[Coordinate_Operation]"));
        assertTrue(isAcronymForWords("coordoperationmethod",      "[Coordinate_Operation Method]"));
        assertTrue(isAcronymForWords("coordoperationparam",       "[Coordinate_Operation Parameter]"));
        assertTrue(isAcronymForWords("coordoperationparamusage",  "[Coordinate_Operation Parameter Usage]"));
        assertTrue(isAcronymForWords("coordoperationparamvalue",  "[Coordinate_Operation Parameter Value]"));
        assertTrue(isAcronymForWords("coordoperationpath",        "[Coordinate_Operation Path]"));
        assertTrue(isAcronymForWords("coordinatereferencesystem", "[Coordinate Reference System]"));
        assertTrue(isAcronymForWords("coordinatesystem",          "[Coordinate System]"));
        assertTrue(isAcronymForWords("datum",                     "[Datum]"));
        assertTrue(isAcronymForWords("ellipsoid",                 "[Ellipsoid]"));
        assertTrue(isAcronymForWords("namingsystem",              "[Naming System]"));
        assertTrue(isAcronymForWords("primemeridian",             "[Prime Meridian]"));
        assertTrue(isAcronymForWords("supersession",              "[Supersession]"));
        assertTrue(isAcronymForWords("unitofmeasure",             "[Unit of Measure]"));
        assertTrue(isAcronymForWords("versionhistory",            "[Version History]"));
        assertTrue(isAcronymForWords("change",                    "[Change]"));
        assertTrue(isAcronymForWords("deprecation",               "[Deprecation]"));
        /*
         * It is important the the following is not recognized as an acronym,
         * otherwise it leads to a confusion in DirectEpsgFactory.
         */
        assertFalse(isAcronymForWords("coordoperation", "[Coordinate_Operation Method]"));
    }
}

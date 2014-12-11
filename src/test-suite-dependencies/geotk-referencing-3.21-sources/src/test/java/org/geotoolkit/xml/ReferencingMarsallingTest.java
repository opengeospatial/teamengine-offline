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
package org.geotoolkit.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.opengis.metadata.extent.GeographicExtent;
import org.opengis.metadata.identification.CharacterSet;
import org.opengis.metadata.spatial.GeometricObjectType;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.datum.VerticalDatumType;

import org.geotoolkit.internal.jaxb.metadata.ReferenceSystemMetadata;
import org.geotoolkit.metadata.iso.DefaultMetadata;
import org.geotoolkit.metadata.iso.citation.Citations;
import org.geotoolkit.metadata.iso.citation.DefaultCitation;
import org.geotoolkit.metadata.iso.extent.DefaultExtent;
import org.geotoolkit.metadata.iso.extent.DefaultGeographicBoundingBox;
import org.geotoolkit.metadata.iso.extent.DefaultVerticalExtent;
import org.geotoolkit.metadata.iso.identification.DefaultDataIdentification;
import org.geotoolkit.metadata.iso.spatial.DefaultGeometricObjects;
import org.geotoolkit.metadata.iso.spatial.DefaultVectorSpatialRepresentation;
import org.geotoolkit.referencing.DefaultReferenceIdentifier;
import org.geotoolkit.referencing.crs.DefaultGeographicCRS;
import org.geotoolkit.referencing.crs.DefaultVerticalCRS;
import org.geotoolkit.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotoolkit.referencing.cs.DefaultEllipsoidalCS;
import org.geotoolkit.referencing.cs.DefaultVerticalCS;
import org.geotoolkit.referencing.datum.DefaultEllipsoid;
import org.geotoolkit.referencing.datum.DefaultGeodeticDatum;
import org.geotoolkit.referencing.datum.DefaultPrimeMeridian;
import org.geotoolkit.referencing.datum.DefaultVerticalDatum;
import org.geotoolkit.test.LocaleDependantTestBase;
import org.geotoolkit.test.TestData;

import org.junit.*;

import static org.geotoolkit.test.Commons.getSingleton;
import static org.geotoolkit.referencing.Assert.*;
import static org.opengis.referencing.IdentifiedObject.NAME_KEY;
import static org.opengis.referencing.ReferenceSystem.SCOPE_KEY;


/**
 * Tests the marshalling and unmarshalling of a few CRS objects.
 * <p>
 * <ul>
 *   <li>{@linkplain DefaultMetadata Metadata} object containing a
 *       {@linkplain DefaultVerticalCRS vertical CRS}</li>
 *   <li>{@linkplain GeographicCRS Geographic CRS}</li>
 * </ul>
 *
 * @author Cédric Briançon (Geomatys)
 * @author Guilhem Legal (Geomatys)
 *
 * @version 3.19
 *
 * @since 3.04
 */
public final strictfp class ReferencingMarsallingTest extends LocaleDependantTestBase {
    /**
     * The resource file which contains an XML representation of a
     * {@linkplain DefaultMetadata metadata} object, with a {@link VerticalCRS}.
     */
    private static final String VERTICAL_CRS_XML = "VerticalCRS.xml";

    /**
     * The resource file which contains an XML representation of a
     * {@linkplain DefaultMetadata metadata} object, with a {@link GeographicCRS}.
     */
    private static final String GEOGRAPHIC_CRS_XML = "GeographicCRS.xml";

    /**
     * Tests the marshalling of a {@linkplain DefaultGeographicCRS geographic crs} object
     * compared to an XML file containing the gml representation of this object.
     *
     * @throws JAXBException if the marshalling process fails.
     * @throws IOException if an error occurs while trying to read data from the resource file.
     */
    @Test
    public void testGeographicCRSMarshalling() throws JAXBException, IOException {
        final GeographicCRS     crs = createGeographicCRS();
        final StringWriter       sw = new StringWriter();
        final MarshallerPool   pool = new MarshallerPool(DefaultGeographicCRS.class);
        final Marshaller marshaller = pool.acquireMarshaller();
        marshaller.marshal(crs, sw);
        pool.release(marshaller);
        final String result = sw.toString();
        final String expected = TestData.readText(this, GEOGRAPHIC_CRS_XML);
        assertDomEquals(expected, result, "xmlns:*", "xsi:schemaLocation");
    }

    /**
     * Tests the unmarshalling of a {@linkplain DefaultGeographicCRS geographic crs} object
     * from an XML file.
     *
     * @throws JAXBException if the unmarshalling process fails.
     * @throws IOException if an error occurs while trying to read data from the resource file.
     */
    @Test
    public void testGeographicCRSUnmarshalling() throws JAXBException, IOException {
        final MarshallerPool pool = new MarshallerPool(DefaultGeographicCRS.class);
        final Unmarshaller unmarshaller = pool.acquireUnmarshaller();
        final Object obj;
        try {
            final InputStream in = TestData.openStream(this, GEOGRAPHIC_CRS_XML);
            obj = unmarshaller.unmarshal(in);
            in.close();
        } finally {
            pool.release(unmarshaller);
        }
        assertTrue(obj instanceof DefaultGeographicCRS);
        final DefaultGeographicCRS result = (DefaultGeographicCRS) obj;
        final DefaultGeographicCRS expected = createGeographicCRS();
        // Here we are not able to check the equality on these two geographic CRS,
        // because some default values are set at creation-time, and they are not
        // unmarshalled. So those objects are not equals, just their values are.
        assertEqualsIgnoreMetadata(expected, result, false);
        assertEquals(expected.getName(),                       result.getName());
        assertEquals(expected.getDatum().getName(),            result.getDatum().getName());
        assertEquals(expected.getCoordinateSystem().getName(), result.getCoordinateSystem().getName());
    }

    /**
     * Tests the marshalling of a {@linkplain DefaultMetadata metadata} object, compared
     * to the resource XML file.
     *
     * @throws JAXBException if the marshalling process fails.
     * @throws IOException if an error occurs while trying to read data from the resource file.
     */
    @Test
    public void testVerticalCRSMarshalling() throws JAXBException, IOException {
        final DefaultMetadata metadata = createMetadataWithVerticalCRS();
        final StringWriter          sw = new StringWriter();
        final MarshallerPool      pool = new MarshallerPool(DefaultMetadata.class);
        final Marshaller    marshaller = pool.acquireMarshaller();
        marshaller.marshal(metadata, sw);
        final String result = sw.toString();
        final String expected = TestData.readText(this, VERTICAL_CRS_XML);
        assertDomEquals(expected, result, "xmlns:*", "xsi:schemaLocation");
        /*
         * Tests again with an older GML version, which include the datum type element.
         * Checks that this element is present in GML 3.1 and absent in latest GML.
         */
        sw.getBuffer().setLength(0);
        marshaller.setProperty(XML.GML_VERSION, "3.1");
        marshaller.marshal(metadata, sw);
        final String result31 = sw.toString();
        pool.release(marshaller);
        assertTrue(result31.contains("<gml:verticalDatumType>depth</gml:verticalDatumType>"));
        assertFalse(result.contains("verticalDatumType"));
    }

    /**
     * Tests the unmarshalling of a {@linkplain DefaultMetadata metadata} object from an XML file.
     *
     * @throws JAXBException if the unmarshalling process fails.
     * @throws IOException if an error occurs while trying to read data from the resource file.
     */
    @Test
    public void testVerticalCRSUnmarshalling() throws JAXBException, IOException {
        final MarshallerPool pool = new MarshallerPool(DefaultMetadata.class);
        final DefaultMetadata expected = createMetadataWithVerticalCRS();
        final Unmarshaller unmarshaller = pool.acquireUnmarshaller();
        final InputStream in = TestData.openStream(this, VERTICAL_CRS_XML);
        final Object obj = unmarshaller.unmarshal(in);
        in.close();
        pool.release(unmarshaller);
        assertTrue(obj instanceof DefaultMetadata);
        final DefaultMetadata result = (DefaultMetadata) obj;
        final ReferenceIdentifier expectedID = getSingleton(expected.getReferenceSystemInfo()).getName();
        final ReferenceIdentifier   resultID = getSingleton(  result.getReferenceSystemInfo()).getName();
        /*
         * Tests some property explcitely before to test the Metadata object as a whole, in
         * order to make diagnostic and debugging easier. In particular, the identifiers are
         * sensible to word in the org.geotoolkit.xml package.
         */
        assertEquals("gmd:fileIdentifier",            expected  .getFileIdentifier(),            result  .getFileIdentifier());
        assertEquals("gmd:language",                  expected  .getLanguage(),                  result  .getLanguage());
        assertEquals("gmd:characterSet",              expected  .getCharacterSet(),              result  .getCharacterSet());
        assertEquals("gmd:spatialRepresentationInfo", expected  .getSpatialRepresentationInfo(), result  .getSpatialRepresentationInfo());
        assertEquals("gmd:code",                      expectedID.getCode(),                      resultID.getCode());
        assertEquals("gmd:codeSpace",                 expectedID.getCodeSpace(),                 resultID.getCodeSpace());
        assertEquals("gmd:authority",                 expectedID.getAuthority(),                 resultID.getAuthority());
        assertEquals("gmd:referenceSystemInfo",       expected  .getReferenceSystemInfo(),       result  .getReferenceSystemInfo());
        assertEquals("gmd:identificationInfo",        expected  .getIdentificationInfo(),        result  .getIdentificationInfo());
        assertEquals("gmd:MD_Metadata",               expected,                                  result);
    }

    /**
     * Builds and returns a {@linkplain DefaultMetadata metadata} object for the marshalling tests.
     */
    private static DefaultMetadata createMetadataWithVerticalCRS() {
        final DefaultMetadata metadata = new DefaultMetadata();
        metadata.setFileIdentifier("20090901");
        metadata.setLanguage(Locale.ENGLISH);
        metadata.setCharacterSet(CharacterSet.UTF_8);
        /*
         * Spatial representation info.
         */
        final DefaultVectorSpatialRepresentation spatialRep = new DefaultVectorSpatialRepresentation();
        final DefaultGeometricObjects geoObj = new DefaultGeometricObjects(GeometricObjectType.valueOf("POINT"));
        spatialRep.setGeometricObjects(Collections.singleton(geoObj));
        metadata.setSpatialRepresentationInfo(Collections.singleton(spatialRep));
        /*
         * Reference system info.
         */
        final String code = "World Geodetic System 84";
        final DefaultCitation authority = new DefaultCitation(Citations.GEOTOOLKIT);
        final DefaultReferenceIdentifier identifier = new DefaultReferenceIdentifier(authority, "EPSG", code);
        final ReferenceSystemMetadata rs = new ReferenceSystemMetadata(identifier);
        metadata.setReferenceSystemInfo(Collections.singleton(rs));
        /*
         * Vertical datum.
         */
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(SCOPE_KEY, null);
        properties.put(NAME_KEY, new DefaultReferenceIdentifier(null, null, "Depth"));
        final DefaultVerticalDatum datum = new DefaultVerticalDatum(properties, VerticalDatumType.DEPTH);
        /*
         * Vertical Coordinate System.
         */
        properties.clear();
        properties.put(NAME_KEY, new DefaultReferenceIdentifier(null, null, "meters"));
        final DefaultCoordinateSystemAxis axis = new DefaultCoordinateSystemAxis(properties, "meters",
                AxisDirection.DOWN, SI.METRE);

        properties.clear();
        properties.put(NAME_KEY, new DefaultReferenceIdentifier(null, null, "meters"));
        final DefaultVerticalCS cs = new DefaultVerticalCS(properties, axis);

        properties.clear();
        properties.put(SCOPE_KEY, null);
        properties.put(NAME_KEY, new DefaultReferenceIdentifier(null, null, "idvertCRS"));
        final DefaultVerticalCRS vcrs = new DefaultVerticalCRS(properties, datum, cs);
        /*
         * Geographic Extent.
         */
        final DefaultExtent extent = new DefaultExtent();
        final double west  = Double.parseDouble( "4.55");
        final double east  = Double.parseDouble( "4.55");
        final double south = Double.parseDouble("44.22");
        final double north = Double.parseDouble("44.22");
        final GeographicExtent geo = new DefaultGeographicBoundingBox(west, east, south, north);
        extent.setGeographicElements(Collections.singleton(geo));
        /*
         * Vertical extent.
         */
        final DefaultVerticalExtent vertExtent = new DefaultVerticalExtent();
        vertExtent.setVerticalCRS(vcrs);
        extent.setVerticalElements(Collections.singleton(vertExtent));
        /*
         * Data indentification.
         */
        final DefaultDataIdentification dataIdentification = new DefaultDataIdentification();
        dataIdentification.setExtents(Collections.singleton(extent));
        metadata.setIdentificationInfo(Collections.singleton(dataIdentification));

        return metadata;
    }

    /**
     * Creates a Geographic CRS for testing purpose.
     */
    private static DefaultGeographicCRS createGeographicCRS() {
        final Map<String,Object> properties = new HashMap<String, Object>();
        /*
         * Build the datum.
         */
        properties.put(NAME_KEY, new DefaultReferenceIdentifier(null, null, "Greenwich"));
        final DefaultPrimeMeridian primeMeridian = new DefaultPrimeMeridian(properties, 0.0, NonSI.DEGREE_ANGLE);

        properties.clear();
        properties.put(NAME_KEY, new DefaultReferenceIdentifier(null, null, "WGS84"));
        final DefaultEllipsoid ellipsoid = DefaultEllipsoid.createFlattenedSphere(properties, 6378137.0, 298.257223563, SI.METRE);

        properties.clear();
        properties.put(NAME_KEY, new DefaultReferenceIdentifier(null, null, "World Geodetic System 1984"));
        properties.put(DefaultGeodeticDatum.IDENTIFIERS_KEY,
                new DefaultReferenceIdentifier(Citations.fromName("EPSG"), "EPSG", "6326"));
        final DefaultGeodeticDatum datum = new DefaultGeodeticDatum(properties, ellipsoid, primeMeridian);
        /*
         * Build the coordinate system.
         */
        properties.clear();
        properties.put(NAME_KEY, new DefaultReferenceIdentifier(null, null, "Geodetic latitude"));
        final DefaultCoordinateSystemAxis axisLat = new DefaultCoordinateSystemAxis(properties, "\u03C6",
                AxisDirection.NORTH, NonSI.DEGREE_ANGLE);

        properties.clear();
        properties.put(NAME_KEY, new DefaultReferenceIdentifier(null, null, "Geodetic longitude"));
        final DefaultCoordinateSystemAxis axisLon = new DefaultCoordinateSystemAxis(properties, "\u03BB",
                AxisDirection.EAST, NonSI.DEGREE_ANGLE);

        properties.clear();
        properties.put(NAME_KEY, new DefaultReferenceIdentifier(null, null, "Géodésique 2D"));
        final DefaultEllipsoidalCS cs = new DefaultEllipsoidalCS(properties, axisLon, axisLat);

        properties.clear();
        properties.put(NAME_KEY, new DefaultReferenceIdentifier(null, null, "WGS84(DD)"));

        return new DefaultGeographicCRS(properties, datum, cs);
    }
}

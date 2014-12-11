/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2004-2012 Open Geospatial Consortium, Inc.
 *    All Rights Reserved. http://www.opengeospatial.org/ogc/legal
 *
 *    Permission to use, copy, and modify this software and its documentation, with
 *    or without modification, for any purpose and without fee or royalty is hereby
 *    granted, provided that you include the following on ALL copies of the software
 *    and documentation or portions thereof, including modifications, that you make:
 *
 *    1. The full text of this NOTICE in a location viewable to users of the
 *       redistributed or derivative work.
 *    2. Notice of any changes or modifications to the OGC files, including the
 *       date changes were made.
 *
 *    THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT HOLDERS MAKE
 *    NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *    TO, WARRANTIES OF MERCHANTABILITY OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT
 *    THE USE OF THE SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY
 *    PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.
 *
 *    COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT, SPECIAL OR
 *    CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE SOFTWARE OR DOCUMENTATION.
 *
 *    The name and trademarks of copyright holders may NOT be used in advertising or
 *    publicity pertaining to the software without specific, written prior permission.
 *    Title to copyright in this software and any associated documentation will at all
 *    times remain with copyright holders.
 */
package org.opengis.referencing.datum;

import java.util.Map;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A textual description and/or a set of parameters identifying a particular reference level
 * surface used as a zero-height surface. The description includes its position with respect
 * to the Earth for any of the height types recognized by this standard.
 * <p>
 * There are several types of Vertical Datums, and each may place constraints on the
 * {@linkplain org.opengis.referencing.cs.CoordinateSystemAxis Coordinate Axis} with which
 * it is combined to create a {@linkplain org.opengis.referencing.crs.VerticalCRS Vertical CRS}.
 *
 * @author  Martin Desruisseaux (IRD)
 * @version 3.0
 * @since   1.0
 *
 * @navassoc 1 - - VerticalDatumType
 *
 * @see DatumAuthorityFactory#createVerticalDatum(String)
 * @see DatumFactory#createVerticalDatum(Map, VerticalDatumType)
 */
@UML(identifier="CD_VerticalDatum", specification=ISO_19111)
public interface VerticalDatum extends Datum {
    /**
     * The type of this vertical datum.
     *
     * @departure historic
     *   This attribute is kept conformant with the specification published in 2003.
     *   The 2007 revision of ISO 19111 removed this attribute, since this information
     *   can be encoded in the <cite>anchor point</cite>. However GeoAPI keep this attribute
     *   for historical reasons, and because it provides some of the anchor point information
     *   in a programmatic way more suitable to coordinate transformation engines.
     *   <p>
     *   Note that GML defines the anchor point as a code list, which address the programmatic
     *   needs. In GeoAPI the anchor point is rather defined as an international string, thus
     *   the need for equivalent information as a code list.
     *
     * @return The type of this vertical datum.
     *
     * @see #getAnchorPoint()
     */
    @UML(identifier="vertDatumType", obligation=MANDATORY, specification=ISO_19111)
    VerticalDatumType getVerticalDatumType();
}

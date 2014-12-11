/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2005-2012 Open Geospatial Consortium, Inc.
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
package org.opengis.geometry.coordinate;

import java.util.List;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A {@linkplain GriddedSurface gridded surface} that uses cubic polynomial splines as the
 * horizontal and vertical curves. The initial tangents for the splines are often replaced
 * by an extra pair of rows (and columns) of control points.
 * <p>
 * The horizontal and vertical curves require initial and final tangent vectors for a complete
 * definition. These values are supplied by the four methods defined in this interface.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_BicubicGrid", specification=ISO_19107)
public interface BicubicGrid extends GriddedSurface {
    /**
     * Returns the initial tangent vectors.
     */
    @UML(identifier="horiVectorAtStart", obligation=MANDATORY, specification=ISO_19107)
    List<double[]> getHorizontalVectorAtStart();

    /**
     * Returns the initial tangent vectors.
     */
    @UML(identifier="horiVectorAtEnd", obligation=MANDATORY, specification=ISO_19107)
    List<double[]> getHorizontalVectorAtEnd();

    /**
     * Returns the initial tangent vectors.
     */
    @UML(identifier="vertVectorAtStart", obligation=MANDATORY, specification=ISO_19107)
    List<double[]> getVerticalVectorAtStart();

    /**
     * Returns the initial tangent vectors.
     */
    @UML(identifier="vertVectorAtEnd", obligation=MANDATORY, specification=ISO_19107)
    List<double[]> getVerticalVectorAtEnd();
}

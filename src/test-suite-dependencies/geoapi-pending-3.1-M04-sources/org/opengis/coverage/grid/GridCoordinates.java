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
package org.opengis.coverage.grid;

import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Holds the set of grid coordinates that specifies the location of the
 * {@linkplain GridPoint grid point} within the {@linkplain Grid grid}.
 *
 * @version ISO 19123:2004
 * @author  Martin Schouwenburg
 * @author  Wim Koolhoven
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.1
 */
@UML(identifier="CV_GridCoordinates", specification=ISO_19123)
public interface GridCoordinates {
    /**
     * Returns the number of dimensions. This method is equivalent to
     * <code>{@linkplain #getCoordinateValues()}.length</code>. It is
     * provided for efficiency.
     *
     * @return The number of dimensions.
     */
    @Extension
    int getDimension();

    /**
     * Returns one integer value for each dimension of the grid. The ordering of these coordinate
     * values shall be the same as that of the elements of {@link Grid#getAxisNames}. The value of
     * a single coordinate shall be the number of offsets from the origin of the grid in the direction
     * of a specific axis.
     *
     * @return A copy of the coordinates. Changes in the returned array will not be reflected
     *         back in this {@code GridCoordinates} object.
     */
    @UML(identifier="coordValues", obligation=MANDATORY, specification=ISO_19123)
    int[] getCoordinateValues();

    /**
     * Returns the coordinate value at the specified dimension. This method is equivalent to
     * <code>{@linkplain #getCoordinateValues()}[<var>i</var>]</code>. It is provided for
     * efficiency.
     *
     * @param  dimension The dimension for which to obtain the coordinate value.
     * @return The coordinate value at the given dimension.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater
     *         than the {@linkplain #getDimension grid dimension}.
     */
    @Extension
    int getCoordinateValue(int dimension) throws IndexOutOfBoundsException;

    /**
     * Sets the coordinate value at the specified dimension (optional operation).
     *
     * @param  dimension The dimension for which to set the coordinate value.
     * @param  value The new value.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater
     *         than the {@linkplain #getDimension grid dimension}.
     * @throws UnsupportedOperationException if this grid coordinates is not modifiable.
     */
    @Extension
    void setCoordinateValue(int dimension, int value)
            throws IndexOutOfBoundsException, UnsupportedOperationException;
}

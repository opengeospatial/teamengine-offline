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
package org.opengis.coverage;

import org.opengis.geometry.DirectPosition;
import org.opengis.annotation.Extension;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Specification.*;


/**
 * Thrown when a {@link Coverage#evaluate(DirectPosition, java.util.Set) evaluate} method
 * is invoked for a location outside the domain of the coverage.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Alexander Petkov
 * @since   GeoAPI 1.0
 *
 * @see Coverage#evaluate(DirectPosition, byte[])
 * @see Coverage#evaluate(DirectPosition, double[])
 */
@UML(identifier="CV_PointOutsideCoverage", specification=OGC_01004)
public class PointOutsideCoverageException extends CannotEvaluateException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -8718412090539227101L;

    /**
     * Represents a direct position which is outside the domain of the coverage.
     */
    private DirectPosition offendingLocation;

    /**
     * Creates an exception with no message.
     */
    public PointOutsideCoverageException() {
        super();
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     */
    public PointOutsideCoverageException(String message) {
        super(message);
    }

    /**
     * Returns the {@linkplain DirectPosition direct position}
     * which is outside the domain of the {@linkplain #getCoverage coverage}.
     *
     * @return The position outside the coverage, or {@code null} if unknown.
     *
     * @since GeoAPI 2.2
     */
    @Extension
    public DirectPosition getOffendingLocation() {
        return offendingLocation;
    }

    /**
     * Sets the {@linkplain DirectPosition direct position}
     * which is outside the domain of the {@linkplain #getCoverage coverage}.
     *
     * @param location The position outside the coverage, or {@code null} if unknown.
     *
     * @since GeoAPI 2.2
     */
    @Extension
    public void setOffendingLocation(final DirectPosition location) {
        this.offendingLocation = location;
    }
}

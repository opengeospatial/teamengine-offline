/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2009-2012 Open Geospatial Consortium, Inc.
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
package org.opengis.observation;

import org.opengis.observation.coverage.DiscreteTimeInstantCoverage;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.Obligation.*;

/**
 * Specialized Observation, in which the result is a compact representation of a time-instant coverage 
 * which samples a property of the feature of interest at different times.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/om">Implementation specification 1.0</A>
 * @author Open Geospatial Consortium
 * @author Guilhem Legal (Geomatys)
 * @since GeoAPI 2.3
 */
@UML(identifier="TimeSeriesObservation", specification=OGC_07022)
public interface TimeSeriesObservation extends DiscreteCoverageObservation {
    
    /**
     * compact representation of a time-instant coverage which samples a property of the feature of interest at different times
     */
    @UML(identifier="result", obligation=MANDATORY, specification=OGC_07022)
    DiscreteTimeInstantCoverage getResult();
    
}

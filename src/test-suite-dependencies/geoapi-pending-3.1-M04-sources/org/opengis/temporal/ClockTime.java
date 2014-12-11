/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2006-2012 Open Geospatial Consortium, Inc.
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
package org.opengis.temporal;

import java.util.Collection;
import java.lang.Number;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A data type that shall be used to identify a temporal position within a day.
 * Because {@linkplain TemporalPosition temporal position} cannot by itself completely
 * identify a single temporal position; it shall be used with {@linkplain CalendarDate
 * calendar date} for that purpose. It may be also used to identify the time of occurrence
 * of an event that recurs every day.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
@UML(identifier="TM_ClockTime", specification=ISO_19108)
public interface ClockTime extends TemporalPosition {
    /**
     * A sequence of numbers with a structure similar to that of {@link CalendarDate#getCalendarDate
     * CalendarDate}. The first number integer identifies a specific instance of the unit used at the
     * highest level of the clock hierarchy, the second number identifies a specific instance of the
     * unit used at the next lower level, and so on. All but the last number in the sequence shall be
     * integers; the last number may be integer or real.
     *
     * @return Instance of the units, from highest level to lowest level of the clock hierarchy.
     */
    @UML(identifier="clkTime", obligation=MANDATORY, specification=ISO_19108)
    Number[] getClockTime();
}

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

import java.util.Date;
import java.sql.Time;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A union class that consists of one of the data types listed as its attributes.
 * Date, Time, and DateTime are basic data types defined in ISO/TS 19103,
 * and may be used for describing temporal positions referenced to the
 * Gregorian calendar and UTC.
 *
 * @author Alexander Petkov
 * @author Martin Desruisseaux (IRD)
 *
 */
@UML(identifier="TM_Position", specification=ISO_19108)
public interface Position {
    /**
     * {@linkplain TemporalPosition} and its subtypes shall be used
     * for describing temporal positions referenced to other reference systems, and may be used for
     * temporal positions referenced to any calendar or clock, including the Gregorian calendar and UTC.
     * @return TemporalPosition
     */
    @UML(identifier="anyOther", obligation=OPTIONAL, specification=ISO_19108)
    TemporalPosition anyOther();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     */
    @UML(identifier="date8601", obligation=OPTIONAL, specification=ISO_19108)
    Date getDate();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     */
    @UML(identifier="time8601", obligation=OPTIONAL, specification=ISO_19108)
    Time getTime();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     */
    @UML(identifier="dateTime8601", obligation=OPTIONAL, specification=ISO_19108)
    InternationalString getDateTime();

}

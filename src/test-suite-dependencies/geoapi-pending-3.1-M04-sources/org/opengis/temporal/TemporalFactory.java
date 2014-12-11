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
package org.opengis.temporal;

import java.util.Collection;
import java.util.Date;
import javax.measure.unit.Unit;
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.util.InternationalString;

/**
 *
 * @author Open Geospatial Consortium
 * @author Guilhem Legal (Geomatys)
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.3
 */
public interface TemporalFactory {

    /**
     *
     * @param name : This is a name that uniquely identifies the temporal reference system.
     * @param domainOfValidit
     * @return Calendar
     */
    Calendar createCalendar(ReferenceIdentifier name, Extent domainOfValidit);

    /**
     *
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition,
     * if not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for
     * TM_TemporalPosition unless a subtype of TM_TemporalPosition is used as the data type.
     * @param calendarEraName : This is the name of the calendar era to which the date is referenced.
     * @param calendarDate : This is a sequence of positive integers in which the first
     * integeridentifies a specific instance of the unit used at the highest level
     * of the calendar hierarchy, the second integer identifies a specific instance
     * of the unit used at the next lower level in the hierarchy, and so on.
     * The format defined in ISO 8601 for dates in the Gregorian calendar may be
     * used for any date that is composed of values for year, month and day.
     * @return CalendarDate
     */
    CalendarDate createCalendarDate(TemporalReferenceSystem frame, IndeterminateValue indeterminatePosition,
            InternationalString calendarEraName, int[] calendarDate);

    /**
     *
     * @param name : identify the calendar era within this calendar.
     * @param referenceEvent : provide the name or description of a mythical or
     * historic event which fixes the position of the base scale of the calendar era.
     * @param referenceDate : provide the date of the reference referenceEvent
     * expressed as a date in the given calendar. In most calendars, this date is
     * the origin (i.e the first day) of the scale, but this is not always true.
     * @param julianReference : provide the Julian date that corresponds to the reference date.
     * @param epochOfUse : identify the TM_Period for which the calendar era was
     * used as a basis for dating, the datatype for TM_Period.begin and Tm_Period.end shall be JulianDate.
     * @return CalendarEra
     */
    CalendarEra createCalendarEra(InternationalString name, InternationalString referenceEvent,
            CalendarDate referenceDate, JulianDate julianReference, Period epochOfUse);

    /**
     *
     * @param name : This is a name that uniquely identifies the temporal reference system.
     * @param domainOfValidity
     * @param referenceEvent : Provide the name or description of an event,
     * such as solar noon or sunrise.
     * @param referenceTime : Provide the time of day associated with the reference
     * event expressed as a time of day in the given clock, the reference time
     * is usually the origin of the clock scale.
     * @param utcReference : This is the 24-hour local or UTC time that corresponds to the reference time.
     * @return Clock
     */
    Clock createClock(ReferenceIdentifier name, Extent domainOfValidity, InternationalString referenceEvent,
            ClockTime referenceTime, ClockTime utcReference);

    /**
     *
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition,
     * if not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for
     * TM_TemporalPosition unless a subtype of TM_TemporalPosition is used as the data type.
     * @param clockTime : This is a sequence of positive numbers with a structure
     * similar to a CalendarDate.
     * @return ClockTime
     */
    ClockTime createClockTime(TemporalReferenceSystem frame, IndeterminateValue indeterminatePosition,
            Number[] clockTime);

    /**
     *
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition,
     * if not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for
     * TM_TemporalPosition unless a subtype of TM_TemporalPosition is used as the data type.
     * @param calendarEraName : This is the name of the calendar era to which the date is referenced.
     * @param calendarDate : This is a sequence of positive integers in which the first
     * integeridentifies a specific instance of the unit used at the highest level of the calendar hierarchy,
     * the second integer identifies a specific instance of the unit used at the next
     * lower level in the hierarchy, and so on. The format defined in ISO 8601 for
     * dates in the Gregorian calendar may be used for any date that is composed
     * of values for year, month and day.
     * @param clockTime : This is a sequence of positive numbers with a structure similar to a CalendarDate.
     * @return DateAndTime
     */
    DateAndTime createDateAndTime(TemporalReferenceSystem frame, IndeterminateValue indeterminatePosition,
            InternationalString calendarEraName, int[] calendarDate, Number[] clockTime);

    /**
     * 
     * @param instant : This is the position of this TM_Instant,
     * it shall be associated with a single temporal reference system.
     * @return Instant
     */
    Instant createInstant(Position instant);

    /**
     *
     * @param unit : This is the name of the unit of measure used to express the length of the interval.
     * @param radix : This is the base of the multiplier of the unit.
     * @param factor : This is the exponent of the base.
     * @param value : This is the length of the time interval as an integer multiple
     * of one radix(exp -factor) of the specified unit.
     * @return IntervalLength
     */
    IntervalLength createIntervalLenght(Unit unit, int radix, int factor, int value);

    /**
     *
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition,
     * if not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for
     * TM_TemporalPosition unless a subtype of TM_TemporalPosition is used as the data type.
     * @param coordinateValue : This is the distance from the scale origin expressed
     * as a multiple of the standard interval associated with the temporal coordinate system.
     * @return JulianDate
     */
    JulianDate createJulianDate(TemporalReferenceSystem frame, IndeterminateValue indeterminatePosition,
            Number coordinateValue);

    /**
     *
     * @param name : This is a string that identifies the ordinal era within the TM_OrdinalReferenceSystem.
     * @param beginning : This is the temporal position at which the ordinal era began, if it is known.
     * @param end : This is the temporal position at which the ordinal era ended.
     * @param composition
     * @return OrdinalEra
     */
    OrdinalEra createOrdinalEra(InternationalString name, Date beginning, Date end,
            Collection<OrdinalEra> composition);

    /**
     *
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition,
     * if not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for
     * TM_TemporalPosition unless a subtype of TM_TemporalPosition is used as the data type.
     * @param ordinalPosition : This is a reference to the ordinal era in which the instant occurs.
     * @return OrdinalPosition
     */
    OrdinalPosition createOrdinalPosition(TemporalReferenceSystem frame,
            IndeterminateValue indeterminatePosition, OrdinalEra ordinalPosition);

    /**
     *
     * @param name : This is a name that uniquely identifies the temporal reference system.
     * @param domainOfValidity
     * @param ordinalEraSequence : An ordinal temporal reference system  consists of a set of ordinal eras.
     * @return OrdinalReferenceSystem
     */
    OrdinalReferenceSystem createOrdinalReferenceSystem(ReferenceIdentifier name,
            Extent domainOfValidity, Collection<OrdinalEra> ordinalEraSequence);

    /**
     *
     * @param begin : This is the TM_Instant at which this Period starts.
     * @param end : This is the TM_Instant at which this Period ends.
     * @return Period
     */
    Period createPeriod(Instant begin, Instant end);

    /**
     *
     * @param years
     * @param months
     * @param week
     * @param days
     * @param hours
     * @param minutes
     * @param seconds
     * @return PeriodDuration
     */
    PeriodDuration createPeriodDuration(InternationalString years, InternationalString months,
            InternationalString week, InternationalString days, InternationalString hours,
            InternationalString minutes, InternationalString seconds);

    /**
     *
     * @param position : this object represents one of the data types listed as :
     * Date, Time, DateTime, and TemporalPosition with its subtypes
     * @return Position
     */
    Position createPosition(Date position);

    /**
     *
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition,
     * if not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for
     * TM_TemporalPosition unless a subtype of TM_TemporalPosition is used as the data type.
     * @param coordinateValue : This is the distance from the scale origin expressed
     * as a multiple of the standard interval associated with the temporal coordinate system.
     * @return TemporalCoordinate
     */
    TemporalCoordinate createTemporalCoordinate(TemporalReferenceSystem frame,
            IndeterminateValue indeterminatePosition, Number coordinateValue);

    /**
     *
     * @param name : This is a name that uniquely identifies the temporal reference system.
     * @param domainOfValidity
     * @param origin : The origin of the scale, it must be specified in the Gregorian
     * calendar with time of day in UTC.
     * @param interval : The name of a single unit of measure used as the base interval for the scale.
     * it shall be one of those units of measure for time specified by ISO 31-1,
     * or a multiple of one of those units, as specified by ISO 1000.
     * @return TemporalCoordinateSystem
     */
    TemporalCoordinateSystem createTemporalCoordinateSystem(ReferenceIdentifier name,
            Extent domainOfValidity, Date origin, InternationalString interval);

    /**
     *
     * @param frame : This is the TM_ReferenceSystem associated with this TM_TemporalPosition,
     * if not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     * @param indeterminatePosition : This attribute provides the only value for
     * TM_TemporalPosition unless a subtype of TM_TemporalPosition is used as the data type.
     * @return TemporalPosition
     */
    TemporalPosition createTemporalPosition(TemporalReferenceSystem frame,
            IndeterminateValue indeterminatePosition);

    /**
     *
     * @param name : This is a name that uniquely identifies the temporal reference system.
     * @param domainOfValidity
     * @return TemporalReferenceSystem
     */
    TemporalReferenceSystem createTemporalReferenceSystem(ReferenceIdentifier name,
            Extent domainOfValidity);

}

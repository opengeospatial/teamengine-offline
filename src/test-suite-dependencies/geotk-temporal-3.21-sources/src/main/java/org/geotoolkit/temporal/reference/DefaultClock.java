/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009, Geomatys
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
package org.geotoolkit.temporal.reference;

import java.util.Collection;
import org.geotoolkit.util.Utilities;
import org.opengis.temporal.Calendar;
import org.opengis.temporal.Clock;
import org.opengis.temporal.ClockTime;
import org.opengis.util.InternationalString;
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.ReferenceIdentifier;

/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 * @module pending
 */
public class DefaultClock extends DefaultTemporalReferenceSystem implements Clock {

    /**
     * Provide the name or description of an event, such as solar noon or sunrise.
     */
    private InternationalString referenceEvent;
    /**
     * Provide the time of day associated with the reference event expressed as a time of day in the given clock, the reference time is usually the origin of the clock scale.
     */
    private ClockTime referenceTime;
    /**
     * This is the 24-hour local or UTC time that corresponds to the reference time.
     */
    private ClockTime utcReference;
    /**
     * Collection of TM_Calendars that use this TM_CalendarEra as a reference for dating.
     */
    private Collection<Calendar> dateBasis;

    public DefaultClock(final ReferenceIdentifier name, final Extent domainOfValidity, final InternationalString referenceEvent, final ClockTime referenceTime, final ClockTime utcReference) {
        super(name, domainOfValidity);
        this.referenceEvent = referenceEvent;
        this.referenceTime = referenceTime;
        this.utcReference = utcReference;
    }

    @Override
    public InternationalString getReferenceEvent() {
        return referenceEvent;
    }

    @Override
    public ClockTime getReferenceTime() {
        return referenceTime;
    }

    @Override
    public ClockTime getUTCReference() {
        return utcReference;
    }

    /**
     * Takes a 24-hour local or UTC time and return the equivalent time of day expressed in terms of the specified clock.
     * @param uTime
     * @return ClickTime
     */
    @Override
    public ClockTime clkTrans(final ClockTime uTime) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Takes a time of day expressed in terms of the specified clock and return the equivalent time of day in 24-hour local or UTC time.
     * @param clkTime
     * @return ClockTime
     */
    @Override
    public ClockTime utcTrans(final ClockTime clkTime) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setReferenceEvent(final InternationalString referenceEvent) {
        this.referenceEvent = referenceEvent;
    }

    public void setReferenceTime(final ClockTime referenceTime) {
        this.referenceTime = referenceTime;
    }

    public void setUtcReference(final ClockTime utcReference) {
        this.utcReference = utcReference;
    }

    public Collection<Calendar> getDateBasis() {
        return dateBasis;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (super.equals(object)) {
            final DefaultClock that;
            if (object instanceof DefaultClock) {
                that = (DefaultClock) object;

                return Utilities.equals(this.dateBasis, that.dateBasis) &&
                        Utilities.equals(this.referenceEvent, that.referenceEvent) &&
                        Utilities.equals(this.referenceTime, that.referenceTime) &&
                        Utilities.equals(this.utcReference, that.utcReference);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.dateBasis != null ? this.dateBasis.hashCode() : 0);
        hash = 37 * hash + (this.referenceEvent != null ? this.referenceEvent.hashCode() : 0);
        hash = 37 * hash + (this.referenceTime != null ? this.referenceTime.hashCode() : 0);
        hash = 37 * hash + (this.utcReference != null ? this.utcReference.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Clock:").append('\n');
        if (referenceEvent != null) {
            s.append("referenceEvent:").append(referenceEvent).append('\n');
        }
        if (referenceTime != null) {
            s.append("referenceTime:").append(referenceTime).append('\n');
        }
        if (utcReference != null) {
            s.append("utcReference:").append(utcReference).append('\n');
        }
        if (dateBasis != null) {
            s.append("dateBasis:").append(dateBasis).append('\n');
        }
        return s.toString();
    }
}

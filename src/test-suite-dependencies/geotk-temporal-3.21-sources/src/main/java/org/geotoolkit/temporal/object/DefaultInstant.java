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
package org.geotoolkit.temporal.object;

import java.util.Collection;
import org.geotoolkit.util.Utilities;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.Position;

/**
 * A zero-dimensional geometric primitive that represents position in time, equivalent to a point
 * in space.
 * 
 * @author Mehdi Sidhoum (Geomatys)
 * @module pending
 */
public class DefaultInstant extends DefaultTemporalGeometricPrimitive implements Instant {

    /**
     * This is the Collection of temporal {@link org.opengis.temporal.Period}s,
     * for which this Instant is the beginning. The collection may be empty.
     */
    private Collection<Period> begunBy;
    /**
     * This is the Collection of temporal {@link org.opengis.temporal.Period}s,
     * for which this Instant is the end. The collection may be empty.
     */
    private Collection<Period> endBy;
    /**
     * This is the position of this TM_Instant, it shall be associated with a single temporal reference system.
     */
    private Position position;

    /**
     * An Empty constructor that will be removed later.
     * This constructor mustn't ber used.
     */
    public DefaultInstant() {
    }

    public DefaultInstant(final Position position) {
        this.position = position;
    }

    /**
     * Get the position of this instant.
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Get the Collection of temporal {@link org.opengis.temporal.Period}s,
     * for which this Instant is the beginning. The collection may be empty.
     * @see org.opengis.temporal.Period#getBeginning()
     */
    @Override
    public Collection<Period> getBegunBy() {
        return begunBy;
    }

    /**
     * Get the Collection of temporal {@link org.opengis.temporal.Period}s,
     * for which this Instant is the end. The collection may be empty.
     * @see org.opengis.temporal.Period#getEnding()
     */
    @Override
    public Collection<Period> getEndedBy() {
        return endBy;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public void setBegunBy(final Collection<Period> begunBy) {
        this.begunBy = begunBy;
    }

    public void setEndBy(final Collection<Period> endBy) {
        this.endBy = endBy;
    }

    /**
     * Verify if this entry is identical to the specified object.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DefaultInstant) {
            final DefaultInstant that = (DefaultInstant) object;

            return Utilities.equals(this.position, that.position) &&
                    Utilities.equals(this.begunBy, that.begunBy) &&
                    Utilities.equals(this.endBy, that.endBy);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.position != null ? this.position.hashCode() : 0);
        hash = 37 * hash + (this.begunBy != null ? this.begunBy.hashCode() : 0);
        hash = 37 * hash + (this.endBy != null ? this.endBy.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Instant:").append('\n');
        if (position != null) {
            s.append("position:").append(position).append('\n');
        }
        if (begunBy != null) {
            s.append("begunBy:").append(begunBy).append('\n');
        }
        if (endBy != null) {
            s.append("endBy:").append(endBy).append('\n');
        }
        return s.toString();
    }
}

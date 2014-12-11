/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2010-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2010-2012, Geomatys
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
package org.geotoolkit.util.logging;

import java.util.logging.Level;
import java.util.concurrent.TimeUnit;
import org.geotoolkit.lang.Configuration;

import static org.geotoolkit.util.ArgumentChecks.ensurePositive;


/**
 * Logging levels for measurements of execution time. Different logging levels - {@link #SLOW},
 * {@link #SLOWER} and {@link #SLOWEST} - are provided in order to log only the events taking
 * more than some time duration. For example the console could log only the slowest events,
 * while a file could log all events considered slow.
 * <p>
 * Every levels defined in this class have a {@linkplain #intValue() value} between the
 * {@link Level#FINE} and {@link Level#CONFIG} values. Consequently performance logging are
 * disabled by default, and enabling them imply enabling configuration logging too. This is
 * done that way because the configuration typically have a significant impact on performance.
 *
 * {@section Enabling performance logging}
 * Performance logging can be enabled in various ways. Among others:
 * <p>
 * <ul>
 *   <li>The {@code $JAVA_HOME/lib/logging.properties} file can be edited in order to log
 *       messages at the {@code FINE} level, at least for the packages of interest.</li>
 *   <li>The {@link Logging#forceMonolineConsoleOutput(Level)} convenience method
 *       can be invoked.</li>
 * </ul>
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.16
 *
 * @since 3.16
 * @module
 */
public final class PerformanceLevel extends Level {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 6055684381688936293L;

    /*
     * IMPLEMENTATION NOTE: The level values used in the constants below are also used
     * in the 'switch' statements of the 'setMinDuration(...)' method. If those values
     * are modified, don't forget to update also the switch statements!!
     */

    /**
     * The level for logging all time measurements, regardless of their duration.
     * The {@linkplain #intValue() value} of this level is 600.
     */
    public static final PerformanceLevel PERFORMANCE = new PerformanceLevel("PERFORMANCE", 600, 0);

    /**
     * The level for logging relatively slow events. By default, only events having an execution
     * time equals or greater than 0.1 second are logged at this level. However this threshold
     * can be changed by a call to <code>SLOW.{@linkplain #setMinDuration(long, TimeUnit)}</code>.
     */
    public static final PerformanceLevel SLOW = new PerformanceLevel("SLOW", 610, 100000000L);

    /**
     * The level for logging only events slower than the ones logged at the {@link #SLOW} level.
     * By default, only events having an execution time equals or greater than 1 second are
     * logged at this level. However this threshold can be changed by a call to
     * <code>SLOWER.{@linkplain #setMinDuration(long, TimeUnit)}</code>.
     */
    public static final PerformanceLevel SLOWER = new PerformanceLevel("SLOWER", 620, 1000000000L);

    /**
     * The level for logging only slowest events. By default, only events having an execution
     * time equals or greater than 5 seconds are logged at this level. However this threshold
     * can be changed by a call to <code>SLOWEST.{@linkplain #setMinDuration(long, TimeUnit)}</code>.
     */
    public static final PerformanceLevel SLOWEST = new PerformanceLevel("SLOWEST", 630, 5000000000L);

    /**
     * The minimal duration (in nanoseconds) for logging the record.
     */
    private volatile long minDuration;

    /**
     * Constructs a new logging level for monitoring performance.
     *
     * @param name     The logging level name.
     * @param value    The level value.
     * @param duration The minimal duration (in nanoseconds) for logging a record.
     */
    private PerformanceLevel(final String name, final int value, final long duration) {
        super(name, value);
        minDuration = duration;
    }

    /**
     * Returns the level to use for logging an event of the given duration.
     *
     * @param  duration The event duration.
     * @param  unit The unit of the given duration value.
     * @return The level to use for logging an event of the given duration.
     */
    public static PerformanceLevel forDuration(long duration, final TimeUnit unit) {
        duration = unit.toNanos(duration);
        if (duration >= SLOWER.minDuration) {
            return (duration >= SLOWEST.minDuration) ? SLOWEST : SLOWER;
        } else {
            return (duration >= SLOW.minDuration) ? SLOW : PERFORMANCE;
        }
    }

    /**
     * Returns the minimal duration for logging an event at this level.
     *
     * @param  unit The unit in which to express the minimal duration.
     * @return The minimal duration in the given unit.
     */
    public long getMinDuration(final TimeUnit unit) {
        return unit.convert(minDuration, TimeUnit.NANOSECONDS);
    }

    /**
     * Sets the minimal duration for logging an event at this level. Invoking this method
     * may have an indirect impact of other performance levels:
     * <p>
     * <ul>
     *   <li>If the given duration is longer than the duration of slower levels, then the later
     *       are also set to the given duration.</li>
     *   <li>If the given duration is shorter than the duration of faster levels, then the later
     *       are also set to the given duration.</li>
     * </ul>
     *
     * {@note The duration of the <code>PERFORMANCE</code> level can not be modified: it is
     * always zero. However invoking this method on the <code>PERFORMANCE</code> field will
     * ensure that every <code>SLOW*</code> levels will have at least the given duration.}
     *
     * @param  duration The minimal duration.
     * @param  unit The unit of the given duration value.
     * @throws IllegalArgumentException If the given duration is negative.
     */
    @Configuration
    @SuppressWarnings("fallthrough")
    public void setMinDuration(long duration, final TimeUnit unit) throws IllegalArgumentException {
        ensurePositive("duration", duration);
        duration = unit.toNanos(duration);
        final int value = intValue();
        synchronized (PerformanceLevel.class) {
            // Check the value of slower levels.
            switch (value) {
                default:  throw new AssertionError(this);
                case 600: if (duration > SLOW   .minDuration) SLOW   .minDuration = duration;
                case 610: if (duration > SLOWER .minDuration) SLOWER .minDuration = duration;
                case 620: if (duration > SLOWEST.minDuration) SLOWEST.minDuration = duration;
                case 630: // Do nothing since there is no level slower than 'SLOWEST'.
            }
            // Check the value of faster levels.
            switch (value) {
                default:  throw new AssertionError(this);
                case 630: if (duration < SLOWER .minDuration) SLOWER .minDuration = duration;
                case 620: if (duration < SLOW   .minDuration) SLOW   .minDuration = duration;
                case 610: minDuration = duration;
                case 600: // Do nothing, since we don't allow modification of PERFORMANCE level.
            }
        }
    }
}

/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2004-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009-2012, Geomatys
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotoolkit.metadata.iso.quality;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;
import net.jcip.annotations.ThreadSafe;

import org.opengis.metadata.quality.AccuracyOfATimeMeasurement;


/**
 * Correctness of the temporal references of an item (reporting of error in time measurement).
 *
 * @author Martin Desruisseaux (IRD, Geomatys)
 * @author Touraïvane (IRD)
 * @version 3.19
 *
 * @since 2.1
 * @module
 */
@ThreadSafe
@XmlType(name = "DQ_AccuracyOfATimeMeasurement_Type")
@XmlRootElement(name = "DQ_AccuracyOfATimeMeasurement")
public class DefaultAccuracyOfATimeMeasurement extends AbstractTemporalAccuracy
        implements AccuracyOfATimeMeasurement
{
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = -7934234071852119486L;

    /**
     * Constructs an initially empty accuracy of a time measurement.
     */
    public DefaultAccuracyOfATimeMeasurement() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @param source The metadata to copy, or {@code null} if none.
     *
     * @since 2.4
     */
    public DefaultAccuracyOfATimeMeasurement(final AccuracyOfATimeMeasurement source) {
        super(source);
    }

    /**
     * Returns a Geotk metadata implementation with the same values than the given arbitrary
     * implementation. If the given object is {@code null}, then this method returns {@code null}.
     * Otherwise if the given object is already a Geotk implementation, then the given object is
     * returned unchanged. Otherwise a new Geotk implementation is created and initialized to the
     * attribute values of the given object, using a <cite>shallow</cite> copy operation
     * (i.e. attributes are not cloned).
     *
     * @param  object The object to get as a Geotk implementation, or {@code null} if none.
     * @return A Geotk implementation containing the values of the given object (may be the
     *         given object itself), or {@code null} if the argument was null.
     *
     * @since 3.18
     */
    public static DefaultAccuracyOfATimeMeasurement castOrCopy(final AccuracyOfATimeMeasurement object) {
        return (object == null) || (object instanceof DefaultAccuracyOfATimeMeasurement)
                ? (DefaultAccuracyOfATimeMeasurement) object : new DefaultAccuracyOfATimeMeasurement(object);
    }
}

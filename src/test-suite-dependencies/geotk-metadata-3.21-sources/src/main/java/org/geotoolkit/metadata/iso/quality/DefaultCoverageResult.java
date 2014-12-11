/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2009-2012, Open Source Geospatial Foundation (OSGeo)
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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.jcip.annotations.ThreadSafe;

import org.opengis.metadata.content.CoverageDescription;
import org.opengis.metadata.distribution.Format;
import org.opengis.metadata.quality.CoverageResult;
import org.opengis.metadata.distribution.DataFile;
import org.opengis.metadata.spatial.SpatialRepresentation;
import org.opengis.metadata.spatial.SpatialRepresentationType;

import org.geotoolkit.xml.Namespaces;


/**
 * Result of a data quality measure organising the measured values as a coverage.
 *
 * @author Cédric Briançon (Geomatys)
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.19
 *
 * @since 3.03
 * @module
 */
@ThreadSafe
@XmlType(name = "QE_CoverageResult_Type", propOrder={
    "spatialRepresentationType",
    "resultSpatialRepresentation",
    "resultContentDescription",
    "resultFormat",
    "resultFile"
})
@XmlRootElement(name = "QE_CoverageResult", namespace = Namespaces.GMI)
public class DefaultCoverageResult extends AbstractResult implements CoverageResult {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = -5014701989643853577L;

    /**
     * Method used to spatially represent the coverage result.
     */
    private SpatialRepresentationType spatialRepresentationType;

    /**
     * Provides the digital representation of data quality measures composing the coverage result.
     */
    private SpatialRepresentation resultSpatialRepresentation;

    /**
     * Provides the description of the content of the result coverage, i.e. semantic definition
     * of the data quality measures.
     */
    private CoverageDescription resultContentDescription;

    /**
     * Provides information about the format of the result coverage data.
     */
    private Format resultFormat;

    /**
     * Provides information about the data file containing the result coverage data.
     */
    private DataFile resultFile;

    /**
     * Constructs an initially empty coverage result.
     */
    public DefaultCoverageResult() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @param source The metadata to copy, or {@code null} if none.
     */
    public DefaultCoverageResult(final CoverageResult source) {
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
    public static DefaultCoverageResult castOrCopy(final CoverageResult object) {
        return (object == null) || (object instanceof DefaultCoverageResult)
                ? (DefaultCoverageResult) object : new DefaultCoverageResult(object);
    }

    /**
     * Returns the method used to spatially represent the coverage result.
     */
    @Override
    @XmlElement(name = "spatialRepresentationType", namespace = Namespaces.GMI, required = true)
    public synchronized SpatialRepresentationType getSpatialRepresentationType() {
        return spatialRepresentationType;
    }

    /**
     * Sets the method used to spatially represent the coverage result.
     *
     * @param newValue The new spatial representation type value.
     */
    public synchronized void setSpatialRepresentationType(final SpatialRepresentationType newValue) {
        checkWritePermission();
        spatialRepresentationType = newValue;
    }

    /**
     * Returns the digital representation of data quality measures composing the coverage result.
     */
    @Override
    @XmlElement(name = "resultSpatialRepresentation", namespace = Namespaces.GMI, required = true)
    public synchronized SpatialRepresentation getResultSpatialRepresentation() {
        return resultSpatialRepresentation;
    }

    /**
     * Sets the digital representation of data quality measures composing the coverage result.
     *
     * @param newValue The new spatial representation value.
     */
    public synchronized void setResultSpatialRepresentation(final SpatialRepresentation newValue) {
        checkWritePermission();
        resultSpatialRepresentation = newValue;
    }

    /**
     * Returns the description of the content of the result coverage, i.e. semantic definition
     * of the data quality measures.
     */
    @Override
    @XmlElement(name = "resultContentDescription", namespace = Namespaces.GMI, required = true)
    public synchronized CoverageDescription getResultContentDescription() {
        return resultContentDescription;
    }

    /**
     * Sets the description of the content of the result coverage, i.e. semantic definition
     * of the data quality measures.
     *
     * @param newValue The new content description value.
     */
    public synchronized void setResultContentDescription(final CoverageDescription newValue) {
        checkWritePermission();
        resultContentDescription = newValue;
    }

    /**
     * Returns the information about the format of the result coverage data.
     */
    @Override
    @XmlElement(name = "resultFormat", namespace = Namespaces.GMI, required = true)
    public synchronized Format getResultFormat() {
        return resultFormat;
    }

    /**
     * Sets the information about the format of the result coverage data.
     *
     * @param newValue The new result format value.
     */
    public synchronized void setResultFormat(final Format newValue) {
        checkWritePermission();
        resultFormat = newValue;
    }

    /**
     * Returns the information about the data file containing the result coverage data.
     */
    @Override
    @XmlElement(name = "resultFile", namespace = Namespaces.GMX, required = true)
    public synchronized DataFile getResultFile() {
        return resultFile;
    }

    /**
     * Sets the information about the data file containing the result coverage data.
     *
     * @param newValue The new result file value.
     */
    public synchronized void setResultFile(final DataFile newValue) {
        checkWritePermission();
        resultFile = newValue;
    }
}

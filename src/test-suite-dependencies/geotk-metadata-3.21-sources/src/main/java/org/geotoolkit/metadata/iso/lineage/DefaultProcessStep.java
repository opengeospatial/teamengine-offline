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
package org.geotoolkit.metadata.iso.lineage;

import java.util.Date;
import java.util.Collection;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.jcip.annotations.ThreadSafe;

import org.opengis.metadata.lineage.ProcessStepReport;
import org.opengis.metadata.lineage.Processing;
import org.opengis.util.InternationalString;
import org.opengis.metadata.lineage.Source;
import org.opengis.metadata.lineage.ProcessStep;
import org.opengis.metadata.citation.ResponsibleParty;

import org.geotoolkit.metadata.iso.MetadataEntity;
import org.geotoolkit.xml.Namespaces;


/**
 * Description of the event, including related parameters or tolerances.
 *
 * @author Martin Desruisseaux (IRD, Geomatys)
 * @author Touraïvane (IRD)
 * @author Cédric Briançon (Geomatys)
 * @version 3.19
 *
 * @since 2.1
 * @module
 */
@ThreadSafe
@XmlType(name = "LI_ProcessStep_Type", propOrder={
    "description",
    "rationale",
    "date",
    "processors",
    "sources",
    "outputs",
    "processingInformation",
    "reports"
})
@XmlRootElement(name = "LI_ProcessStep")
@XmlSeeAlso(org.geotoolkit.internal.jaxb.gmi.LE_ProcessStep.class)
public class DefaultProcessStep extends MetadataEntity implements ProcessStep {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = 8151975419390308233L;

    /**
     * Description of the event, including related parameters or tolerances.
     */
    private InternationalString description;

    /**
     * Requirement or purpose for the process step.
     */
    private InternationalString rationale;

    /**
     * Date and time or range of date and time on or over which the process step occurred,
     * in milliseconds elapsed since January 1st, 1970. If there is no such date, then this
     * field is set to the special value {@link Long#MIN_VALUE}.
     */
    private long date;

    /**
     * Identification of, and means of communication with, person(s) and
     * organization(s) associated with the process step.
     */
    private Collection<ResponsibleParty> processors;

    /**
     * Information about the source data used in creating the data specified by the scope.
     */
    private Collection<Source> sources;

    /**
     * Description of the product generated as a result of the process step.
     */
    private Collection<Source> outputs;

    /**
     * Comprehensive information about the procedure by which the algorithm was applied
     * to derive geographic data from the raw instrument measurements, such as datasets,
     * software used, and the processing environment.
     */
    private Processing processingInformation;

    /**
     * Report generated by the process step.
     */
    private Collection<ProcessStepReport> reports;

    /**
     * Creates an initially empty process step.
     */
    public DefaultProcessStep() {
        date = Long.MIN_VALUE;
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @param source The metadata to copy, or {@code null} if none.
     *
     * @since 2.4
     */
    public DefaultProcessStep(final ProcessStep source) {
        super(source);
        if (source != null) {
            // Be careful to not overwrite date value (GEOTK-170).
            if (date == 0 && source.getDate() == null) {
                date = Long.MIN_VALUE;
            }
        }
    }

    /**
     * Creates a process step initialized to the given description.
     *
     * @param description Description of the event, including related parameters or tolerances.
     */
    public DefaultProcessStep(final InternationalString description) {
        this(); // Initialize the date field.
        setDescription(description);
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
    public static DefaultProcessStep castOrCopy(final ProcessStep object) {
        return (object == null) || (object instanceof DefaultProcessStep)
                ? (DefaultProcessStep) object : new DefaultProcessStep(object);
    }

     /**
     * Returns the description of the event, including related parameters or tolerances.
     */
    @Override
    @XmlElement(name = "description", required = true)
    public synchronized InternationalString getDescription() {
        return description;
    }

    /**
     * Sets the description of the event, including related parameters or tolerances.
     *
     * @param newValue The new description.
     */
    public synchronized void setDescription(final InternationalString newValue) {
        checkWritePermission();
        description = newValue;
    }

    /**
     * Returns the requirement or purpose for the process step.
     */
    @Override
    @XmlElement(name = "rationale")
    public synchronized InternationalString getRationale() {
        return rationale;
    }

    /**
     * Sets the requirement or purpose for the process step.
     *
     * @param newValue The new rationale.
     */
    public synchronized void setRationale(final InternationalString newValue) {
        checkWritePermission();
        rationale = newValue;
    }

    /**
     * Returns the date and time or range of date and time on or over which
     * the process step occurred.
     */
    @Override
    @XmlElement(name = "dateTime")
    public synchronized Date getDate() {
        return (date != Long.MIN_VALUE) ? new Date(date) : null;
    }

    /**
     * Sets the date and time or range of date and time on or over which the process
     * step occurred.
     *
     * @param newValue The new date.
     */
    public synchronized void setDate(final Date newValue) {
        checkWritePermission();
        date = (newValue != null) ? newValue.getTime() : Long.MIN_VALUE;
    }

    /**
     * Returns the identification of, and means of communication with, person(s) and
     * organization(s) associated with the process step.
     */
    @Override
    @XmlElement(name = "processor")
    public synchronized Collection<ResponsibleParty> getProcessors() {
        return processors = nonNullCollection(processors, ResponsibleParty.class);
    }

    /**
     * Identification of, and means of communication with, person(s) and
     * organization(s) associated with the process step.
     *
     * @param newValues The new processors.
     */
    public synchronized void setProcessors(final Collection<? extends ResponsibleParty> newValues) {
        processors = copyCollection(newValues, processors, ResponsibleParty.class);
    }

    /**
     * Returns the information about the source data used in creating the data specified
     * by the scope.
     */
    @Override
    @XmlElement(name = "source")
    public synchronized Collection<Source> getSources() {
        return sources = nonNullCollection(sources, Source.class);
    }

    /**
     * Information about the source data used in creating the data specified by the scope.
     *
     * @param newValues The new sources.
     */
    public synchronized void setSources(final Collection<? extends Source> newValues) {
        sources = copyCollection(newValues, sources, Source.class);
    }

    /**
     * Returns the description of the product generated as a result of the process step.
     *
     * @since 3.03
     */
    @Override
    @XmlElement(name = "output", namespace = Namespaces.GMI)
    public synchronized Collection<Source> getOutputs() {
        return outputs = nonNullCollection(outputs, Source.class);
    }

    /**
     * Sets the description of the product generated as a result of the process step.
     *
     * @param newValues The new output values.
     *
     * @since 3.03
     */
    public synchronized void setOutputs(final Collection<? extends Source> newValues) {
        outputs = copyCollection(newValues, outputs, Source.class);
    }

    /**
     * Returns the comprehensive information about the procedure by which the algorithm
     * was applied to derive geographic data from the raw instrument measurements, such
     * as datasets, software used, and the processing environment. {@code null} if unspecified.
     *
     * @since 3.03
     */
    @Override
    @XmlElement(name = "processingInformation", namespace = Namespaces.GMI)
    public synchronized Processing getProcessingInformation() {
        return processingInformation;
    }

    /**
     * Sets the comprehensive information about the procedure by which the algorithm was
     * applied to derive geographic data from the raw instrument measurements, such as
     * datasets, software used, and the processing environment.
     *
     * @param newValue The new processing information value.
     *
     * @since 3.03
     */
    public synchronized void setProcessingInformation(final Processing newValue) {
        checkWritePermission();
        processingInformation = newValue;
    }

    /**
     * Returns the report generated by the process step.
     *
     * @since 3.03
     */
    @Override
    @XmlElement(name = "report", namespace = Namespaces.GMI)
    public synchronized Collection<ProcessStepReport> getReports() {
        return reports = nonNullCollection(reports, ProcessStepReport.class);
    }

    /**
     * Sets the report generated by the process step.
     *
     * @param newValues The new process step report values.
     *
     * @since 3.03
     */
    public synchronized void setReports(final Collection<? extends ProcessStepReport> newValues) {
        reports = copyCollection(newValues, reports, ProcessStepReport.class);
    }
}
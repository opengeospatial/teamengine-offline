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
package org.opengis.observation.sampling;

import java.util.List;

import org.opengis.observation.AnyFeature;
import org.opengis.observation.Observation;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.Obligation.*;

/**
 * A SamplingFeature is distinguished from typical domain feature types in that it has a set
 * of navigable associations with Observations.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/om">Implementation specification 1.0</A>
 * @author Open Geospatial Consortium
 * @author Guilhem Legal (Geomatys)
 * @since GeoAPI 2.3
 */
@UML(identifier="samplingFeature", specification=OGC_07022)
public interface SamplingFeature extends AnyFeature{

    /**
     * Sampling features are frequently related to each other, as parts of complexes, networks, through sub-sampling, etc.
     *	This is supported by the relatedSamplingFeature association with a SamplingFeatureRelation association class, which carries a source, target and role.
     */
    @UML(identifier="relatedSamplingFeature", obligation=MANDATORY, specification=OGC_07022)
    List<SamplingFeatureRelation> getRelatedSamplingFeature();

    /**
     * A common requirement for sampling features is an indication of the SurveyProcedure
     * that provides the surveyDetails related to determination of its location and shape.
     */
    @UML(identifier="surveyDetails", obligation=OPTIONAL, specification=OGC_07022)
    SurveyProcedure getSurveyDetail();

    /**
     * A SamplingFeature must be associated with one or more other features through an association role sampledFeature.
     * This association records the intention of the sample design.
     * The target of this association will usually be a domain feature.
     */
    @UML(identifier="sampleFeature", obligation=MANDATORY, specification=OGC_07022)
    List<AnyFeature> getSampledFeature();

    /**
     * A SamplingFeature is distinguished from typical domain feature types in that it has a set of [0..*] navigable associations with Observations, given the rolename relatedObservation.
     * This complements the association role "featureOfInterest" which is constrained to point back from the Observation to the Sampling-Feature.
     * The usual requirement of an Observation feature-of-interest is that its type has a property matching the observed-property on the Observation.
     * In the case of Sampling-features, the topology of the model and navigability of the relatedObservation role means that this requirement is satisfied automatically:
     * a property of the sampling-feature is implied by the observedProperty of a related observation.
     * This effectively provides an unlimited set of "soft-typed" properties on a Sampling Feature.
     */
    @UML(identifier="srelatedObservation", obligation=MANDATORY, specification=OGC_07022)
    List<Observation> getRelatedObservation();
    
}

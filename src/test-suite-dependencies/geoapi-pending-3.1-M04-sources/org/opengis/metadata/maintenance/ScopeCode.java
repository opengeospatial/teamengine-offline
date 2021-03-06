/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2004-2012 Open Geospatial Consortium, Inc.
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
package org.opengis.metadata.maintenance;

import java.util.List;
import java.util.ArrayList;
import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Class of information to which the referencing entity applies.
 *
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @version 3.0
 * @since   2.0
 *
 * @see org.opengis.metadata.quality.Scope
 */
@UML(identifier="MD_ScopeCode", specification=ISO_19115)
public final class ScopeCode extends CodeList<ScopeCode> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -4542429199783894255L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<ScopeCode> VALUES = new ArrayList<ScopeCode>(16);

    /**
     * Information applies to the attribute value.
     */
    @UML(identifier="attribute", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode ATTRIBUTE = new ScopeCode("ATTRIBUTE");

    /**
     * Information applies to the characteristic of a feature.
     */
    @UML(identifier="attributeType", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode ATTRIBUTE_TYPE = new ScopeCode("ATTRIBUTE_TYPE");

    /**
     * Information applies to the collection hardware class.
     */
    @UML(identifier="collectionHardware", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode COLLECTION_HARDWARE = new ScopeCode("COLLECTION_HARDWARE");

    /**
     * Information applies to the collection session.
     */
    @UML(identifier="collectionSession", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode COLLECTION_SESSION = new ScopeCode("COLLECTION_SESSION");

    /**
     * Information applies to the dataset.
     */
    @UML(identifier="dataset", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode DATASET = new ScopeCode("DATASET");

    /**
     * Information applies to the series.  Note: "series" applies to any {@code DS_Aggregate}.
     */
    @UML(identifier="series", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode SERIES = new ScopeCode("SERIES");

    /**
     * information applies to non-geographic data;
     */
    @UML(identifier="nonGeographicDataset", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode NON_GEOGRAPHIC_DATASET = new ScopeCode("NON_GEOGRAPHIC_DATASET");

    /**
     * Information applies to a dimension group.
     */
    @UML(identifier="dimensionGroup", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode DIMENSION_GROUP = new ScopeCode("DIMENSION_GROUP");

    /**
     * Information applies to a feature.
     */
    @UML(identifier="feature", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode FEATURE = new ScopeCode("FEATURE");

    /**
     * Information applies to a feature type.
     */
    @UML(identifier="featureType", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode FEATURE_TYPE = new ScopeCode("FEATURE_TYPE");

    /**
     * Information applies to a property type.
     */
    @UML(identifier="propertyType", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode PROPERTY_TYPE = new ScopeCode("PROPERTY_TYPE");

    /**
     * Information applies to a field session.
     */
    @UML(identifier="fieldSession", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode FIELD_SESSION = new ScopeCode("FIELD_SESSION");

    /**
     * Information applies to a computer program or routine.
     */
    @UML(identifier="software", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode SOFTWARE = new ScopeCode("SOFTWARE");

    /**
     * Information applies to a capability which a service provider entity makes available
     * to a service user entity through a set of interfaces that define a behaviour, such as
     * a use case.
     */
    @UML(identifier="service", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode SERVICE = new ScopeCode("SERVICE");

    /**
     * Information applies to a copy or imitation of an existing or hypothetical object.
     */
    @UML(identifier="model", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode MODEL = new ScopeCode("MODEL");

    /**
     * Information applies to a copy or imitation of an existing or hypothetical object.
     *
     * @since 2.1
     */
    @UML(identifier="tile", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ScopeCode TILE = new ScopeCode("TILE");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private ScopeCode(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code ScopeCode}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static ScopeCode[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new ScopeCode[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    @Override
    public ScopeCode[] family() {
        return values();
    }

    /**
     * Returns the scope code that matches the given string, or returns a
     * new one if none match it. More specifically, this methods returns the first instance for
     * which <code>{@linkplain #name() name()}.{@linkplain String#equals equals}(code)</code>
     * returns {@code true}. If no existing instance is found, then a new one is created for
     * the given name.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static ScopeCode valueOf(String code) {
        return valueOf(ScopeCode.class, code);
    }
}

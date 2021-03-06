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
package org.opengis.metadata.identification;

import java.util.List;
import java.util.ArrayList;
import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * High-level geographic data thematic classification to assist in the grouping and
 * search of available geographic data sets. Can be used to group keywords as well.
 * Listed examples are not exhaustive.
 *
 * {@note It is understood there are overlaps between general categories and the user
 *        is encouraged to select the one most appropriate.}
 *
 * @author  Martin Desruisseaux (IRD)
 * @version 3.0
 * @since   2.0
 */
@UML(identifier="MD_TopicCategoryCode", specification=ISO_19115)
public final class TopicCategory extends CodeList<TopicCategory> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -4987523565852255081L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<TopicCategory> VALUES = new ArrayList<TopicCategory>(19);

    /**
     * Rearing of animals and/or cultivation of plants.
     *
     * Examples: agriculture, irrigation, aquaculture, plantations, herding, pests and
     *           diseases affecting crops and livestock.
     */
    @UML(identifier="farming", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory FARMING = new TopicCategory("FARMING");

    /**
     * Flora and/or fauna in natural environment.
     *
     * Examples: wildlife, vegetation, biological sciences, ecology, wilderness, sealife,
     *           wetlands, habitat
     */
    @UML(identifier="biota", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory BIOTA = new TopicCategory("BIOTA");

    /**
     * Legal land descriptions.
     *
     * Examples: political and administrative boundaries.
     */
    @UML(identifier="boundaries", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory BOUNDARIES = new TopicCategory("BOUNDARIES");

    /**
     * Processes and phenomena of the atmosphere.
     *
     * Examples: cloud cover, weather, climate, atmospheric conditions, climate change,
     *           precipitation.
     */
    @UML(identifier="climatologyMeteorologyAtmosphere", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory CLIMATOLOGY_METEOROLOGY_ATMOSPHERE = new TopicCategory("CLIMATOLOGY_METEOROLOGY_ATMOSPHERE");

    /**
     * Economic activities, conditions and employment.
     *
     * Examples: production, labour, revenue, commerce, industry, tourism and
     *           ecotourism, forestry, fisheries, commercial or subsistence hunting,
     *           exploration and exploitation of resources such as minerals, oil and gas.
     */
    @UML(identifier="economy", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory ECONOMY = new TopicCategory("ECONOMY");

    /**
     * Height above or below sea level.
     *
     * Examples: altitude, bathymetry, digital elevation models, slope, derived products.
     */
    @UML(identifier="elevation", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory ELEVATION = new TopicCategory("ELEVATION");

    /**
     * Environmental resources, protection and conservation.
     *
     * Examples: environmental pollution, waste storage and treatment, environmental
     *           impact assessment, monitoring environmental risk, nature reserves, landscape.
     */
    @UML(identifier="environment", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory ENVIRONMENT = new TopicCategory("ENVIRONMENT");

    /**
     * Information pertaining to earth sciences.
     *
     * Examples: geophysical features and processes, geology, minerals, sciences
     *           dealing with the composition, structure and origin of the earth's rocks, risks of
     *           earthquakes, volcanic activity, landslides, gravity information, soils, permafrost,
     *           hydrogeology, erosion.
     */
    @UML(identifier="geoscientificInformation", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory GEOSCIENTIFIC_INFORMATION = new TopicCategory("GEOSCIENTIFIC_INFORMATION");

    /**
     * Health, health services, human ecology, and safety.
     *
     * Examples: disease and illness, factors affecting health, hygiene, substance abuse,
     *           mental and physical health, health services.
     *
     * @since 2.1
     */
    @UML(identifier="health", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory HEALTH = new TopicCategory("HEALTH");

    /**
     * Base maps.
     *
     * Examples: land cover, topographic maps, imagery, unclassified images,
     *           annotations.
     */
    @UML(identifier="imageryBaseMapsEarthCover", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory IMAGERY_BASE_MAPS_EARTH_COVER = new TopicCategory("IMAGERY_BASE_MAPS_EARTH_COVER");

    /**
     * Military bases, structures, activities.
     *
     * Examples: barracks, training grounds, military transportation, information collection.
     */
    @UML(identifier="intelligenceMilitary", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory INTELLIGENCE_MILITARY = new TopicCategory("INTELLIGENCE_MILITARY");

    /**
     * Inland water features, drainage systems and their characteristics.
     *
     * Examples: rivers and glaciers, salt lakes, water utilization plans, dams, currents,
     *           floods, water quality, hydrographic charts.
     */
    @UML(identifier="inlandWaters", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory INLAND_WATERS = new TopicCategory("INLAND_WATERS");

    /**
     * Positional information and services.
     *
     * Examples: addresses, geodetic networks, control points, postal zones and
     *           services, place names.
     */
    @UML(identifier="location", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory LOCATION = new TopicCategory("LOCATION");

    /**
     * Features and characteristics of salt water bodies (excluding inland waters).
     *
     * Examples: tides, tidal waves, coastal information, reefs.
     */
    @UML(identifier="oceans", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory OCEANS = new TopicCategory("OCEANS");

    /**
     * Information used for appropriate actions for future use of the land.
     *
     * Examples: land use maps, zoning maps, cadastral surveys, land ownership.
     */
    @UML(identifier="planningCadastre", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory PLANNING_CADASTRE = new TopicCategory("PLANNING_CADASTRE");

    /**
     * Characteristics of society and cultures.
     *
     * Examples: settlements, anthropology, archaeology, education, traditional beliefs,
     *           manners and customs, demographic data, recreational areas and activities, social
     *           impact assessments, crime and justice, census information
     */
    @UML(identifier="society", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory SOCIETY = new TopicCategory("SOCIETY");

    /**
     * Man-made construction.
     *
     * Examples: buildings, museums, churches, factories, housing, monuments, shops, towers.
     */
    @UML(identifier="structure", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory STRUCTURE = new TopicCategory("STRUCTURE");

    /**
     * Means and aids for conveying persons and/or goods.
     *
     * Examples: roads, airports/airstrips, shipping routes, tunnels, nautical charts,
     *           vehicle or vessel location, aeronautical charts, railways.
     */
    @UML(identifier="transportation", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory TRANSPORTATION = new TopicCategory("TRANSPORTATION");

    /**
     * Energy, water and waste systems and communications infrastructure and services.
     *
     * Examples: hydroelectricity, geothermal, solar and nuclear sources of energy, water
     *           purification and distribution, sewage collection and disposal, electricity and gas
     *           distribution, data communication, telecommunication, radio, communication
     *           networks.
     */
    @UML(identifier="utilitiesCommunication", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory UTILITIES_COMMUNICATION = new TopicCategory("UTILITIES_COMMUNICATION");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private TopicCategory(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code TopicCategory}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static TopicCategory[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new TopicCategory[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    @Override
    public TopicCategory[] family() {
        return values();
    }

    /**
     * Returns the topic category that matches the given string, or returns a
     * new one if none match it. More specifically, this methods returns the first instance for
     * which <code>{@linkplain #name() name()}.{@linkplain String#equals equals}(code)</code>
     * returns {@code true}. If no existing instance is found, then a new one is created for
     * the given name.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static TopicCategory valueOf(String code) {
        return valueOf(TopicCategory.class, code);
    }
}

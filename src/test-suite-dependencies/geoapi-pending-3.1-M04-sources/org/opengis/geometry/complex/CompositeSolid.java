/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2007-2012 Open Geospatial Consortium, Inc.
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
package org.opengis.geometry.complex;

import java.util.Set;
import org.opengis.geometry.primitive.Solid;
import org.opengis.annotation.Association;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A {@linkplain Complex complex} with all the geometric properties of a solid. Essentially,
 * a composite solid is a set of solids that join in pairs on common boundary surfaces to form
 * a single solid.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (Geomatys)
 * @since GeoAPI 2.1
 *
 * @todo This interface extends (indirectly) both {@link org.opengis.geometry.primitive.Primitive} and
 *       {@link org.opengis.geometry.complex.Complex}. Concequently, there is a clash in the semantics
 *       of some set theoretic operation. Specifically, {@code Primitive.contains(...)}
 *       (returns FALSE for end points) is different from {@code Complex.contains(...)}
 *       (returns TRUE for end points).
 */
@UML(identifier="GM_CompositeSurface", specification=ISO_19107)
public interface CompositeSolid extends Composite, Solid {
    /**
     * Returns the set of solids that form the core of this complex.
     * To get a full representation of the elements in the {@linkplain Complex complex},
     * the {@linkplain org.opengis.geometry.primitive.Surface surfaces},
     * {@linkplain org.opengis.geometry.primitive.Curve curves} and
     * {@linkplain org.opengis.geometry.primitive.Point points} on the boundary of the
     * generator set if {@linkplain Solid solids} would have to be added to the generator list.
     *
     * @return The set of solids in this composite.
     *
     * @see Solid#getComposite
     * @issue http://jira.codehaus.org/browse/GEO-63
     */
    @Association("Composition")
    @UML(identifier="generator", obligation=MANDATORY, specification=ISO_19107)
    Set<Solid> getGenerators();
}

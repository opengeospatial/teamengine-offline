/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2005-2012 Open Geospatial Consortium, Inc.
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
package org.opengis.geometry.coordinate;

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Indicates a particular geometric form represented by a {@link BSplineSurface}.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.1
 */
@UML(identifier="GM_BSplineSurfaceForm", specification=ISO_19107)
public class BSplineSurfaceForm extends CodeList<BSplineSurfaceForm> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -5066463171878030795L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<BSplineSurfaceForm> VALUES = new ArrayList<BSplineSurfaceForm>(6);

    /**
     * A bounded portion of a plane represented by a B-spline surface of degree 1 in each parameter.
     */
    @UML(identifier="planar", obligation=CONDITIONAL, specification=ISO_19107)
    public static final BSplineSurfaceForm PLANAR = new BSplineSurfaceForm("PLANAR");

    /**
     * A bounded portion of a cylindrical surface represented by a B-spline surface.
     */
    @UML(identifier="cylindrical", obligation=CONDITIONAL, specification=ISO_19107)
    public static final BSplineSurfaceForm CYLINDRICAL = new BSplineSurfaceForm("CYLINDRICAL");

    /**
     * A bounded portion of the surface of a right circular cone represented by a B-spline surface.
     */
    @UML(identifier="conical", obligation=CONDITIONAL, specification=ISO_19107)
    public static final BSplineSurfaceForm CONICAL = new BSplineSurfaceForm("CONICAL");

    /**
     * A bounded portion of a sphere, or a complete sphere represented by a B-spline surface.
     */
    @UML(identifier="spherical", obligation=CONDITIONAL, specification=ISO_19107)
    public static final BSplineSurfaceForm SPHERICAL = new BSplineSurfaceForm("SPHERICAL");

    /**
     * A torus or a portion of a torus represented by a B-spline surface.
     */
    @UML(identifier="toroidal", obligation=CONDITIONAL, specification=ISO_19107)
    public static final BSplineSurfaceForm TOROIDAL = new BSplineSurfaceForm("TOROIDAL");

    /**
     * No particular surface is specified..
     */
    @UML(identifier="unspecified", obligation=CONDITIONAL, specification=ISO_19107)
    public static final BSplineSurfaceForm UNSPECIFIED = new BSplineSurfaceForm("UNSPECIFIED");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private BSplineSurfaceForm(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code BSplineSurfaceForm}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static BSplineSurfaceForm[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new BSplineSurfaceForm[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public BSplineSurfaceForm[] family() {
        return values();
    }

    /**
     * Returns the B-spline surface form that matches the given string, or returns a
     * new one if none match it. More specifically, this methods returns the first instance for
     * which <code>{@linkplain #name() name()}.{@linkplain String#equals equals}(code)</code>
     * returns {@code true}. If no existing instance is found, then a new one is created for
     * the given name.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static BSplineSurfaceForm valueOf(String code) {
        return valueOf(BSplineSurfaceForm.class, code);
    }
}

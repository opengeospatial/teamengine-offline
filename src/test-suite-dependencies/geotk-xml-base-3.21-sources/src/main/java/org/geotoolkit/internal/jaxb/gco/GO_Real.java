/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2008-2012, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotoolkit.internal.jaxb.gco;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * Surrounds double values by {@code <gco:Real>}.
 * The ISO-19139 standard specifies that primitive types have to be surrounded by an element
 * which represents the type of the value, using the namespace {@code gco} linked to the
 * {@code http://www.isotc211.org/2005/gco} URL. The JAXB default behavior is to marshall
 * primitive Java types directly "as is", without wrapping the value in the required element.
 * The role of this class is to add such wrapping.
 *
 * @author Cédric Briançon (Geomatys)
 * @version 3.17
 *
 * @see PM_Decimal
 *
 * @since 2.5
 * @module
 */
public final class GO_Real extends XmlAdapter<GO_Real, Double> {
    /**
     * The double value to handle.
     */
    @XmlElement(name = "Real")
    public Double value;

    /**
     * Empty constructor used only by JAXB.
     */
    public GO_Real() {
    }

    /**
     * Constructs an adapter for this value.
     *
     * @param value The value.
     */
    private GO_Real(final Double value) {
        this.value = value;
    }

    /**
     * Allows JAXB to generate a Double object using the value found in the adapter.
     *
     * @param value The value extract from the adapter.
     * @return A double object.
     */
    @Override
    public Double unmarshal(final GO_Real value) {
        return (value != null) ? value.value : null;
    }

    /**
     * Allows JAXB to change the result of the marshalling process, according to the
     * ISO-19139 standard and its requirements about primitive types.
     *
     * @param value The double value we want to surround by an element representing its type.
     * @return An adaptation of the double value, that is to say a double value surrounded
     *         by {@code <gco:Real>} element.
     */
    @Override
    public GO_Real marshal(final Double value) {
        return (value != null) ? new GO_Real(value) : null;
    }
}

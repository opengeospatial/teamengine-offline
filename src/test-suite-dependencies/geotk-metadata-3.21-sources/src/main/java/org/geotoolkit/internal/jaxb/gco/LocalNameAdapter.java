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

import javax.imageio.spi.ServiceRegistry;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.opengis.util.LocalName;
import org.opengis.util.NameFactory;

import org.geotoolkit.factory.Hints;
import org.geotoolkit.factory.FactoryFinder;
import org.geotoolkit.naming.DefaultNameFactory;


/**
 * JAXB adapter in order to map implementing class with the GeoAPI interface.
 * See package documentation for more information about JAXB and interface.
 *
 * @author Cédric Briançon (Geomatys)
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.00
 *
 * @since 2.5
 * @module
 */
public final class LocalNameAdapter extends XmlAdapter<String,LocalName> {
    /**
     * The factory to use for creating names.
     * Will be created only if needed.
     */
    private transient NameFactory factory;

    /**
     * Empty constructor for JAXB only.
     */
    public LocalNameAdapter() {
    }

    /**
     * Fetches the name factory. The returned factory shall be an instance
     * of {@link DefaultNameFactory}, not a subclass, because we are going
     * to cast the created {@code GenericName} to {@code AbstractName} for
     * XML marshalling and we know that {@code DefaultNameFactory} creates
     * the expected type. A subclass could create other types, so we are
     * better to avoid them.
     */
    static NameFactory getNameFactory() {
        return FactoryFinder.getNameFactory(new Hints(
                Hints.NAME_FACTORY,       DefaultNameFactory.class,
                FactoryFinder.FILTER_KEY, new ServiceRegistry.Filter()
        {
            @Override public boolean filter(final Object provider) {
                return provider.getClass() == DefaultNameFactory.class;
            }
        }));
    }

    /**
     * Does the link between a {@link LocalName} and the string associated.
     * JAXB calls automatically this method at marshalling-time.
     *
     * @param value The implementing class for this metadata value.
     * @return A {@link String} which represents the metadata value.
     */
    @Override
    public String marshal(final LocalName value) {
        return (value == null) ? null : value.toInternationalString().toString(null);
    }

    /**
     * Does the link between {@linkplain String strings} and the way they will be unmarshalled.
     * JAXB calls automatically this method at unmarshalling-time.
     *
     * @param value The string value.
     * @return The implementing class for this string.
     */
    @Override
    public LocalName unmarshal(String value) {
        if (value == null || ((value = value.trim()).isEmpty())) {
            return null;
        }
        NameFactory factory = this.factory;
        if (factory == null) {
            // No need to synchronize. This is not a big deal if the factory is fetched twice.
            this.factory = factory = getNameFactory();
        }
        return factory.createLocalName(null, value);
    }
}

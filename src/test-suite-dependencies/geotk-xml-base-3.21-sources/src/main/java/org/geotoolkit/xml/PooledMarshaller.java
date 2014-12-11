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
 */
package org.geotoolkit.xml;

import java.io.File;
import java.io.Writer;
import java.io.OutputStream;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.validation.Schema;
import org.xml.sax.ContentHandler;
import org.w3c.dom.Node;

import org.geotoolkit.lang.Decorator;
import org.geotoolkit.internal.jaxb.MarshalContext;


/**
 * Wraps a {@link Marshaller} in order to have some control on the modifications
 * applied on it. This is done in order to make the marshaller safer for reuse.
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.18
 *
 * @since 3.00
 * @module
 */
@Decorator(Marshaller.class)
final class PooledMarshaller extends Pooled implements Marshaller {
    /**
     * The wrapper marshaller which does the real work.
     */
    private final Marshaller marshaller;

    /**
     * Creates a pooled marshaller wrapping the given one.
     *
     * @param marshaller The marshaller to use for the actual work.
     * @param internal {@code true} if the JAXB implementation is the one bundled in JDK 6,
     *        or {@code false} if this is an external implementation like a JAR put in the
     *        endorsed directory.
     */
    PooledMarshaller(final Marshaller marshaller, final boolean internal) {
        super(internal);
        this.marshaller = marshaller;
    }

    /**
     * Resets the given marshaller property to its initial state.
     *
     * @param  key The property to reset.
     * @param  value The initial value to give to the property.
     * @throws JAXBException If an error occurred while restoring a property.
     */
    @Override
    @SuppressWarnings({"unchecked","rawtypes"})
    protected void reset(final Object key, Object value) throws JAXBException {
        if (key instanceof String) {
            final String k = (String) key;
            if (value == null && (k.endsWith(".xmlHeaders") || k.equals(JAXB_SCHEMA_LOCATION))) {
                value = ""; // Null value doesn't seem to be accepted.
            }
            marshaller.setProperty(k, value);
        } else if (key == AttachmentMarshaller.class) {
            marshaller.setAttachmentMarshaller((AttachmentMarshaller) value);
        } else if (key == Schema.class) {
            marshaller.setSchema((Schema) value);
        } else if (key == Listener.class) {
            marshaller.setListener((Listener) value);
        } else if (key == ValidationEventHandler.class) {
            marshaller.setEventHandler((ValidationEventHandler) value);
        } else {
            marshaller.setAdapter((Class) key, (XmlAdapter) value);
        }
    }

    /**
     * Delegates the marshalling to the wrapped marshaller.
     */
    @Override
    public void marshal(final Object object, final Result output) throws JAXBException {
        final MarshalContext ctx = begin();
        ctx.setMarshalling();
        try {
            marshaller.marshal(object, output);
        } finally {
            ctx.finish();
        }
    }

    /**
     * Delegates the marshalling to the wrapped marshaller.
     */
    @Override
    public void marshal(final Object object, final OutputStream output) throws JAXBException {
        final MarshalContext ctx = begin();
        ctx.setMarshalling();
        try {
            marshaller.marshal(object, output);
        } finally {
            ctx.finish();
        }
    }

    /**
     * Delegates the marshalling to the wrapped marshaller.
     */
    @Override
    public void marshal(final Object object, final File output) throws JAXBException {
        final MarshalContext ctx = begin();
        ctx.setMarshalling();
        try {
            marshaller.marshal(object, output);
        } finally {
            ctx.finish();
        }
    }

    /**
     * Delegates the marshalling to the wrapped marshaller.
     */
    @Override
    public void marshal(final Object object, final Writer output) throws JAXBException {
        final MarshalContext ctx = begin();
        ctx.setMarshalling();
        try {
            marshaller.marshal(object, output);
        } finally {
            ctx.finish();
        }
    }

    /**
     * Delegates the marshalling to the wrapped marshaller.
     */
    @Override
    public void marshal(final Object object, final ContentHandler output) throws JAXBException {
        final MarshalContext ctx = begin();
        ctx.setMarshalling();
        try {
            marshaller.marshal(object, output);
        } finally {
            ctx.finish();
        }
    }

    /**
     * Delegates the marshalling to the wrapped marshaller.
     */
    @Override
    public void marshal(final Object object, final Node output) throws JAXBException {
        final MarshalContext ctx = begin();
        ctx.setMarshalling();
        try {
            marshaller.marshal(object, output);
        } finally {
            ctx.finish();
        }
    }

    /**
     * Delegates the marshalling to the wrapped marshaller.
     */
    @Override
    public void marshal(final Object object, final XMLStreamWriter output) throws JAXBException {
        final MarshalContext ctx = begin();
        ctx.setMarshalling();
        try {
            marshaller.marshal(object, output);
        } finally {
            ctx.finish();
        }
    }

    /**
     * Delegates the marshalling to the wrapped marshaller.
     */
    @Override
    public void marshal(final Object object, final XMLEventWriter output) throws JAXBException {
        final MarshalContext ctx = begin();
        ctx.setMarshalling();
        try {
            marshaller.marshal(object, output);
        } finally {
            ctx.finish();
        }
    }

    /**
     * Delegates the marshalling to the wrapped marshaller.
     */
    @Override
    public Node getNode(final Object object) throws JAXBException {
        final MarshalContext ctx = begin();
        ctx.setMarshalling();
        try {
            return marshaller.getNode(object);
        } finally {
            ctx.finish();
        }
    }

    /**
     * Delegates to the wrapped marshaller. This method is invoked by the parent
     * class if the given name was not one of the {@link XML} constants.
     */
    @Override
    void setStandardProperty(final String name, final Object value) throws PropertyException {
        marshaller.setProperty(name, value);
    }

    /**
     * Delegates to the wrapped marshaller. This method is invoked by the parent
     * class if the given name was not one of the {@link XML} constants.
     */
    @Override
    Object getStandardProperty(final String name) throws PropertyException {
        return marshaller.getProperty(name);
    }

    /**
     * Delegates to the wrapped marshaller. The initial state will be saved
     * if it was not already done, for future restoration by {@link #reset()}.
     */
    @Override
    @SuppressWarnings("rawtypes")
    public <A extends XmlAdapter> void setAdapter(final Class<A> type, final A adapter) {
        super.setAdapter(type, adapter);
        marshaller.setAdapter(type, adapter);
    }

    /**
     * Delegates to the wrapped marshaller.
     */
    @Override
    @SuppressWarnings("rawtypes")
    public <A extends XmlAdapter> A getAdapter(final Class<A> type) {
        return marshaller.getAdapter(type);
    }

    /**
     * Delegates to the wrapped marshaller. The initial state will be saved
     * if it was not already done, for future restoration by {@link #reset()}.
     */
    @Override
    public void setSchema(final Schema schema) {
        super.setSchema(schema);
        marshaller.setSchema(schema);
    }

    /**
     * Delegates to the wrapped marshaller.
     */
    @Override
    public Schema getSchema() {
        return marshaller.getSchema();
    }

    /**
     * Delegates to the wrapped marshaller. The initial state will be saved
     * if it was not already done, for future restoration by {@link #reset()}.
     */
    @Override
    public void setEventHandler(final ValidationEventHandler handler) throws JAXBException {
        super.setEventHandler(handler);
        marshaller.setEventHandler(handler);
    }

    /**
     * Delegates to the wrapped marshaller.
     */
    @Override
    public ValidationEventHandler getEventHandler() throws JAXBException {
        return marshaller.getEventHandler();
    }

    /**
     * Delegates to the wrapped marshaller. The initial state will be saved
     * if it was not already done, for future restoration by {@link #reset()}.
     */
    @Override
    public void setAttachmentMarshaller(final AttachmentMarshaller am) {
        if (!containsKey(AttachmentMarshaller.class)) {
            save(AttachmentMarshaller.class, marshaller.getAttachmentMarshaller());
        }
        marshaller.setAttachmentMarshaller(am);
    }

    /**
     * Delegates to the wrapped marshaller.
     */
    @Override
    public AttachmentMarshaller getAttachmentMarshaller() {
        return marshaller.getAttachmentMarshaller();
    }

    /**
     * Delegates to the wrapped marshaller. The initial state will be saved
     * if it was not already done, for future restoration by {@link #reset()}.
     */
    @Override
    public void setListener(final Listener listener) {
        if (!containsKey(Listener.class)) {
            save(Listener.class, marshaller.getListener());
        }
        marshaller.setListener(listener);
    }

    /**
     * Delegates to the wrapped marshaller.
     */
    @Override
    public Listener getListener() {
        return marshaller.getListener();
    }
}

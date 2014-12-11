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
package org.geotoolkit.naming;

import java.util.List;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import net.jcip.annotations.Immutable;

import org.opengis.util.NameSpace;
import org.opengis.util.LocalName;
import org.opengis.util.ScopedName;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;

import org.geotoolkit.resources.Errors;
import org.geotoolkit.internal.jaxb.gco.LocalNameAdapter;
import org.geotoolkit.util.collection.UnmodifiableArrayList;

import static org.geotoolkit.util.ArgumentChecks.ensureNonNull;


/**
 * A composite of a {@linkplain NameSpace name space} (as a {@linkplain LocalName local name})
 * and a {@linkplain GenericName generic name} valid in that name space. See the
 * {@linkplain ScopedName GeoAPI javadoc} for more information.
 * <p>
 * {@code DefaultScopedName} can be instantiated by any of the following methods:
 * <ul>
 *   <li>{@link DefaultNameFactory#createGenericName(NameSpace, CharSequence[])} with an array of length 2 or more</li>
 *   <li>{@link DefaultNameFactory#parseGenericName(NameSpace, CharSequence)} with at least one separator</li>
 * </ul>
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.00
 *
 * @since 2.1
 * @module
 */
@Immutable
@XmlRootElement(name = "ScopedName")
public class DefaultScopedName extends AbstractName implements ScopedName {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = -5215955533541748481L;

    /**
     * The immutable list of parsed names.
     */
    private final UnmodifiableArrayList<? extends LocalName> parsedNames;

    /**
     * The tail or path, computed when first needed.
     */
    private transient GenericName tail, path;

    /**
     * Creates a new scoped names from the given list of local names. This constructor is
     * not public because we do not check if the given local names have the proper scope.
     *
     * @param names The names to gives to the new scoped name.
     */
    static AbstractName create(final UnmodifiableArrayList<? extends DefaultLocalName> names) {
        ensureNonNull("names", names);
        switch (names.size()) {
            default: return new DefaultScopedName(names);
            case 1:  return names.get(0);
            case 0:  throw new IllegalArgumentException(Errors.format(Errors.Keys.EMPTY_ARRAY));
        }
    }

    /**
     * Empty constructor to be used by JAXB only. Despite its "final" declaration,
     * the {@link #parsedNames} field will be set by JAXB during unmarshalling.
     */
    private DefaultScopedName() {
        parsedNames = null;
    }

    /**
     * Creates a new scoped names from the given list of local names. This constructor is
     * not public because it does not check if the given local names have the proper scope.
     *
     * @param names The names to gives to the new scoped name.
     */
    private DefaultScopedName(final UnmodifiableArrayList<? extends LocalName> names) {
        parsedNames = names;
    }

    /**
     * Constructs a scoped name from the specified list of strings. If any of the given names is an
     * instance of {@link InternationalString}, then its {@link InternationalString#toString(java.util.Locale)
     * toString(null)} method will be invoked for fetching an unlocalized name. Otherwise the
     * {@link CharSequence#toString toString()} method will be used.
     *
     * @param scope The scope of this name, or {@code null} for the global scope.
     * @param names The local names. This list must have at least two elements.
     */
    protected DefaultScopedName(final NameSpace scope, final List<? extends CharSequence> names) {
        ensureNonNull("names", names);
        final int size = names.size();
        if (size < 2) {
            throw new IllegalArgumentException(Errors.format(
                    Errors.Keys.ILLEGAL_ARGUMENT_$2, "size", size));
        }
        DefaultNameSpace ns = DefaultNameSpace.wrap(scope);
        final boolean global = ns.isGlobal();
        int i = 0;
        final LocalName[] locals = new LocalName[size];
        final Iterator<? extends CharSequence> it = names.iterator();
        /*
         * Builds the parsed name list by creating DefaultLocalName instances now.
         * Note that we expect at least 2 valid entries (because of the check we
         * did before), so we don't check hasNext() for the two first entries.
         */
        CharSequence name = it.next();
        do {
            ensureNonNull("name", name);
            locals[i++] = new DefaultLocalName(ns, name);
            ns = ns.child(name);
            name = it.next();
        } while (it.hasNext());
        /*
         * At this point, we have almost finished to build the parsed names array.
         * The last name is the tip, which we want to live in the given namespace.
         * If this namespace is global, then the fully qualified name is this name.
         * In this case we assign the reference now in order to avoid letting
         * tip.toFullyQualifiedName() creates a new object later.
         */
        final DefaultLocalName tip = ns.local(name, null);
        if (global) {
            tip.fullyQualified = fullyQualified = this;
        }
        locals[i++] = tip;
        if (i != size) {
            throw new AssertionError(); // Paranoiac check.
        }
        parsedNames = UnmodifiableArrayList.wrap(locals);
    }

    /**
     * Constructs a scoped name as the concatenation of the given generic names.
     * The scope of the new name will be the scope of the {@code path} argument.
     *
     * @param path The first part to concatenate.
     * @param tail The second part to concatenate.
     */
    protected DefaultScopedName(final GenericName path, final GenericName tail) {
        ensureNonNull("path", path);
        ensureNonNull("tail", tail);
        final List<? extends LocalName> parsedPath = path.getParsedNames();
        final List<? extends LocalName> parsedTail = tail.getParsedNames();
        int index = parsedPath.size();
        LocalName[] locals = new LocalName[index + parsedTail.size()];
        locals = parsedPath.toArray(locals);
        /*
         * We have copied the LocalNames from the path unconditionally. Now we need to process the
         * LocalNames from the tail. If the tail scope follow the path scope, we can just copy the
         * names without further processing (easy case). Otherwise we need to create new instances.
         *
         * Note that by contract, GenericName must contains at least 1 element. This assumption
         * appears in two places: it.next() invoked once before any it.hasNext(), and testing for
         * locals[index-1] element (so we assume index > 0).
         */
        final Iterator<? extends LocalName> it = parsedTail.iterator();
        LocalName name = it.next();
        final LocalName lastName  = locals[index-1];
        final NameSpace lastScope = lastName.scope();
        final NameSpace tailScope = name.scope();
        if (tailScope instanceof DefaultNameSpace &&
                ((DefaultNameSpace) tailScope).parent == lastScope)
        {
            /*
             * If the tail is actually the tip (a LocalName), remember the tail so we
             * don't need to create it again later. Then copy the tail after the path.
             */
            if (path instanceof LocalName) {
                this.tail = tail;
            }
            while (true) {
                locals[index++] = name;
                if (!it.hasNext()) break;
                name = it.next();
            }
        } else {
            /*
             * There is no continuity in the chain of scopes, so we need to create new
             * LocalName instances.
             */
            DefaultNameSpace scope = DefaultNameSpace.wrap(lastScope);
            CharSequence label = name(lastName);
            while (true) {
                scope = scope.child(label);
                label = name(name);
                name  = new DefaultLocalName(scope, label);
                locals[index++] = name;
                if (!it.hasNext()) break;
                name = it.next();
            }
        }
        if (index != locals.length) {
            throw new AssertionError(); // Paranoiac check.
        }
        parsedNames = UnmodifiableArrayList.wrap(locals);
        if (tail instanceof LocalName) {
            this.path = path;
        }
    }

    /**
     * Returns the name to be given to {@link DefaultLocalName} constructors.
     */
    private static CharSequence name(final GenericName name) {
        if (name instanceof DefaultLocalName) {
            return ((DefaultLocalName) name).name;
        }
        final InternationalString label = name.toInternationalString();
        if (label != null) {
            return label;
        }
        return name.toString();
    }

    /**
     * Returns the size of the backing array. This is used only has a hint for optimizations
     * in attempts to share internal arrays.
     */
    @Override
    final int arraySize() {
        return parsedNames.arraySize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameSpace scope() {
        return head().scope();
    }

    /**
     * Returns every elements of the {@linkplain #getParsedNames parsed names list}
     * except for the {@linkplain #head head}.
     */
    @Override
    public synchronized GenericName tail() {
        if (tail == null) {
            final int size = parsedNames.size();
            switch (size) {
                default: tail = new DefaultScopedName(parsedNames.subList(1, size)); break;
                case 2:  tail = parsedNames.get(1); break;
                case 1:  // fall through
                case 0:  throw new AssertionError(size);
            }
        }
        return tail;
    }

    /**
     * Returns every element of the {@linkplain #getParsedNames parsed names list}
     * except for the {@linkplain #tip tip}.
     */
    @Override
    public synchronized GenericName path() {
        if (path == null) {
            final int size = parsedNames.size();
            switch (size) {
                default: path = new DefaultScopedName(parsedNames.subList(0, size-1)); break;
                case 2:  path = parsedNames.get(0); break;
                case 1:  // fall through
                case 0:  throw new AssertionError(size);
            }
        }
        return path;
    }

    /**
     * Returns the sequence of local name for this {@linkplain GenericName generic name}.
     */
    @Override
    @XmlJavaTypeAdapter(LocalNameAdapter.class) // Seems required in order to avoid random failures.
    @XmlElement(name = "parsedName", required = true)
    public List<? extends LocalName> getParsedNames() {
        return parsedNames;
    }
}

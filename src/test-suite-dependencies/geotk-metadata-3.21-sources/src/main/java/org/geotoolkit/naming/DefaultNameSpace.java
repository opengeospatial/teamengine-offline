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

import java.util.Map;
import java.util.List;
import java.util.ListIterator;
import java.io.Serializable;
import java.io.ObjectStreamException;
import net.jcip.annotations.Immutable;

import org.opengis.util.NameSpace;
import org.opengis.util.LocalName;
import org.opengis.util.ScopedName;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;

import org.geotoolkit.util.Utilities;
import org.geotoolkit.util.collection.WeakValueHashMap;
import org.geotoolkit.util.collection.UnmodifiableArrayList;

import static org.geotoolkit.util.ArgumentChecks.ensureNonNull;


/**
 * A domain in which {@linkplain AbstractName names} given by character strings are defined.
 * This implementation does not support localization in order to avoid ambiguity when testing
 * two namespaces for {@linkplain #equals(Object) equality}.
 * <p>
 * {@code DefaultNameSpace} can be instantiated by any of the following methods:
 * <ul>
 *   <li>{@link DefaultNameFactory#createNameSpace(GenericName)}</li>
 *   <li>{@link DefaultNameFactory#createNameSpace(GenericName, Map)}</li>
 * </ul>
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.00
 *
 * @since 3.00
 * @module
 */
@Immutable
public class DefaultNameSpace implements NameSpace, Serializable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -3064358267398624306L;

    /**
     * The default separator, which is {@value}. The separator is inserted between the
     * namespace and any {@linkplain GenericName generic name} in that namespace.
     */
    public static final char DEFAULT_SEPARATOR = ':';

    /**
     * {@link #DEFAULT_SEPARATOR} as a {@link String}.
     */
    static final String DEFAULT_SEPARATOR_STRING = ":";

    /**
     * The parent namespace, or {@code null} if the parent is the unique {@code GLOBAL} instance.
     * We don't use direct reference to {@code GLOBAL} because {@code null} is used as a sentinel
     * value for stopping iterative searches (using GLOBAL would have higher risk of never-ending
     * loops in case of bug), and in order to reduce the stream size during serialization.
     */
    final DefaultNameSpace parent;

    /**
     * The name of this namespace, usually as a {@link String} or an {@link InternationalString}.
     */
    private final CharSequence name;

    /**
     * The separator to insert between the namespace and the {@linkplain AbstractName#head() head}
     * of any name in that namespace.
     */
    final String headSeparator;

    /**
     * The separator to insert between the {@linkplain AbstractName#getParsedNames() parsed names}
     * of any name in that namespace.
     */
    final String separator;

    /**
     * The fully qualified name for this namespace.
     * Will be created when first needed.
     */
    private transient AbstractName path;

    /**
     * The children created in this namespace. The values are restricted to the following types:
     * <p>
     * <ul>
     *   <li>{@link DefaultNameSpace}</li>
     *   <li>{@link DefaultLocalName}</li>
     * </ul>
     * <p>
     * No other type should be allowed. The main purpose of this map is to hold child namespaces.
     * However we can (in an opportunist way) handles local names as well. In case of conflict,
     * the namespace will have precedence.
     * <p>
     * This field is initialized soon after {@code DefaultNameSpace} and should be treated
     * like a final field from that point.
     */
    private transient WeakValueHashMap<String,Object> childs;

    /**
     * Creates the global namespace. This constructor can be invoked by {@link GlobalNameSpace}
     * only.
     */
    DefaultNameSpace() {
        this.parent        = null;
        this.name          = "global";
        this.headSeparator = DEFAULT_SEPARATOR_STRING;
        this.separator     = DEFAULT_SEPARATOR_STRING;
        init();
    }

    /**
     * Creates a new namespace with the given separator.
     *
     * @param parent
     *          The parent namespace, or {@code null} if none.
     * @param name
     *          The name of the new namespace, usually as a {@link String}
     *          or an {@link InternationalString}.
     * @param headSeparator
     *          The separator to insert between the namespace and the
     *          {@linkplain AbstractName#head() head} of any name in that namespace.
     * @param separator
     *          The separator to insert between the {@linkplain AbstractName#getParsedNames()
     *          parsed names} of any name in that namespace.
     */
    protected DefaultNameSpace(final DefaultNameSpace parent, CharSequence name,
                               final String headSeparator, final String separator)
    {
        this.parent = parent;
        ensureNonNull("name",          name);
        ensureNonNull("headSeparator", headSeparator);
        ensureNonNull("separator",     separator);
        if (!(name instanceof InternationalString)) {
            name = name.toString();
        }
        this.name          = name;
        this.headSeparator = headSeparator;
        this.separator     = separator;
        init();
    }

    /**
     * Initializes the transient fields.
     */
    private void init() {
        childs = new WeakValueHashMap<String,Object>();
    }

    /**
     * Wraps the given namespace in a {@code DefaultNameSpace} implementation.
     * This method returns an existing instance when possible.
     *
     * @param  ns The namespace to wrap, or {@code null} for the global one.
     * @return The given namespace as a {@code DefaultNameSpace} implementation.
     */
    static DefaultNameSpace wrap(final NameSpace ns) {
        if (ns == null) {
            return GlobalNameSpace.GLOBAL;
        }
        if (ns instanceof DefaultNameSpace) {
            return (DefaultNameSpace) ns;
        }
        return forName(ns.name(), DEFAULT_SEPARATOR_STRING, DEFAULT_SEPARATOR_STRING);
    }

    /**
     * Returns a namespace having the given name and separators.
     * This method returns an existing instance when possible.
     *
     * @param name
     *          The name for the namespace to obtain, or {@code null}.
     * @param headSeparator
     *          The separator to insert between the namespace and the
     *          {@linkplain AbstractName#head() head} of any name in that namespace.
     * @param separator
     *          The separator to insert between the {@linkplain AbstractName#getParsedNames()
     *          parsed names} of any name in that namespace.
     * @return A namespace having the given name, or {@code null} if name was null.
     */
    static DefaultNameSpace forName(final GenericName name,
            final String headSeparator, final String separator)
    {
        if (name == null) {
            return null;
        }
        final List<? extends LocalName> parsedNames = name.getParsedNames();
        final ListIterator<? extends LocalName> it = parsedNames.listIterator(parsedNames.size());
        NameSpace scope;
        /*
         * Searches for the last parsed name having a DefaultNameSpace implementation as its
         * scope. It should be the tip in most cases. If we don't find any, we will recreate
         * the whole chain starting with the global scope.
         */
        do {
            if (!it.hasPrevious()) {
                scope = GlobalNameSpace.GLOBAL;
                break;
            }
            scope = it.previous().scope();
        } while (!(scope instanceof DefaultNameSpace));
        /*
         * We have found a scope. Adds to it the supplemental names.
         * In most cases we should have only the tip to add.
         */
        DefaultNameSpace ns = (DefaultNameSpace) scope;
        while (it.hasNext()) {
            final LocalName tip = it.next();
            ns = ns.child(tip.toString(), tip.toInternationalString(), headSeparator, separator);
        }
        return ns;
    }

    /**
     * Indicates whether this namespace is a "top level" namespace.  Global, or top-level
     * namespaces are not contained within another namespace. The global namespace has no
     * parent.
     *
     * @return {@code true} if this namespace is the global namespace.
     */
    @Override
    public boolean isGlobal() {
        return false; // To be overridden by GlobalNameSpace.
    }

    /**
     * Returns the depth of the given namespace.
     *
     * @param ns The namespace for which to get the depth, or {@code null}.
     * @return The depth of the given namespace.
     */
    private static int depth(DefaultNameSpace ns) {
        int depth = 0;
        if (ns != null) do {
            depth++;
            ns = ns.parent;
        } while (ns != null && !ns.isGlobal());
        return depth;
    }

    /**
     * Represents the identifier of this namespace. Namespace identifiers shall be
     * {@linkplain GenericName#toFullyQualifiedName() fully-qualified names} where:
     *
     * <blockquote><code>
     * name.{@linkplain GenericName#scope() scope()}.{@linkplain #isGlobal()} == true
     * </code></blockquote>
     *
     * @return The identifier of this namespace.
     */
    @Override
    public GenericName name() {
        final int depth;
        synchronized (this) {
            if (path != null) {
                return path;
            }
            depth = depth(this);
            final DefaultLocalName[] names = new DefaultLocalName[depth];
            DefaultNameSpace scan = this;
            for (int i=depth; --i>=0;) {
                names[i] = new DefaultLocalName(scan.parent, scan.name);
                scan = scan.parent;
            }
            assert depth(scan) == 0 || scan.isGlobal();
            path = DefaultScopedName.create(UnmodifiableArrayList.wrap(names));
            GenericName truncated = path;
            for (int i=depth; --i>=0;) {
                names[i].fullyQualified = truncated;
                truncated = (truncated instanceof ScopedName) ? ((ScopedName) truncated).path() : null;
            }
        }
        /*
         * At this point the name is created and ready to be returned. As an optimization,
         * defines the name of parents now in order to share subarea of the array we just
         * created. The goal is to have less objects in memory.
         */
        AbstractName truncated = path;
        DefaultNameSpace scan = parent;
        while (scan != null && !scan.isGlobal()) {
            /*
             * If we have a parent, then depth >= 2 and consequently the name is a ScopedName.
             * Actually it should be an instance of DefaultScopedName - we known that since we
             * created it ourself with the DefaultScopedName.create(...) method call - and we
             * know that its tail() implementation creates instance of AbstractName. Given all
             * the above, none of the casts on the line below should ever fails, unless there
             * is bug in this package.
             */
            truncated = (AbstractName) ((ScopedName) truncated).path();
            synchronized (scan) {
                if (scan.path == null || scan.path.arraySize() < depth) {
                    scan.path = truncated;
                }
            }
            scan = scan.parent;
        }
        return path;
    }

    /**
     * Returns a child namespace of the given name. The returned namespace will
     * have this namespace as its parent, and will use the same separator.
     * <p>
     * The {@link #headSeparator} is not inherited by the children on intend, because this
     * method is used only by {@link DefaultScopedName} constructors in order to create a
     * sequence of parsed local names. For example in {@code "http://www.opengeospatial.org"}
     * the head separator is {@code "://"} for {@code "www"} (which is having this namespace),
     * but it is {@code "."} for all children ({@code "opengeospatial"} and {@code "org"}).
     *
     * @param  name The name of the child namespace.
     * @return The child namespace. It may be an existing instance.
     */
    final DefaultNameSpace child(final CharSequence name) {
        return child(key(name), name, separator, separator);
    }

    /**
     * Returns a key to be used in the {@linkplain #pool} from the given name.
     * The key must be the unlocalized version of the given string.
     *
     * @param name The name.
     * @return A key from the given name.
     */
    private static String key(final CharSequence name) {
        return (name instanceof InternationalString) ?
                ((InternationalString) name).toString(null) : name.toString();
    }

    /**
     * Returns a child namespace of the given name and separator.
     * The returned namespace will have this namespace as its parent.
     *
     * @param key
     *          The unlocalized name of the child namespace, to be used as a key in the cache.
     * @param name
     *          The name of the child namespace, or {@code null} if same than key.
     * @param headSeparator
     *          The separator to insert between the namespace and the
     *          {@linkplain AbstractName#head() head} of any name in that namespace.
     * @param separator
     *          The separator to insert between the {@linkplain AbstractName#getParsedNames()
     *          parsed names} of any name in that namespace.
     * @return The child namespace. It may be an existing instance.
     */
    private DefaultNameSpace child(final String key, CharSequence name,
                                   final String headSeparator, final String separator)
    {
        ensureNonNull("key", key);
        if (name == null) {
            name = key;
        }
        DefaultNameSpace child;
        synchronized (childs) {
            final Object existing = childs.get(key);
            if (existing instanceof DefaultNameSpace) {
                child = (DefaultNameSpace) existing;
                if (!child.separator    .equals(separator) ||
                    !child.headSeparator.equals(headSeparator) ||
                    !child.name         .equals(name)) // Same test than equalsIgnoreParent.
                {
                    child = new DefaultNameSpace(this, name, headSeparator, separator);
                    /*
                     * Do not cache that instance. Actually we can't guess if that instance
                     * would be more appropriate for caching purpose than the old one. We
                     * just assume that keeping the oldest one is more conservative.
                     */
                }
            } else {
                child = new DefaultNameSpace(this, name, headSeparator, separator);
                if (childs.put(key, child) != existing) {
                    throw new AssertionError(); // Paranoiac check.
                }
            }
        }
        assert child.parent == this;
        return child;
    }

    /**
     * Returns a name which is local in this namespace. The returned name will have this
     * namespace as its {@linkplain DefaultLocalName#scope() scope}. This method may returns
     * an existing instance on a "best effort" basis, but this is not guaranteed.
     *
     * @param  name      The name of the instance to create.
     * @param  candidate The instance to cache if no instance was found for the given name,
     *                   or {@code null} if none.
     * @return A name which is local in this namespace.
     */
    final DefaultLocalName local(final CharSequence name, final DefaultLocalName candidate) {
        ensureNonNull("name", name);
        final String key = name.toString();
        DefaultLocalName child;
        synchronized (childs) {
            final Object existing = childs.get(key);
            if (existing instanceof DefaultLocalName) {
                child = (DefaultLocalName) existing;
                if (name.equals(child.name)) {
                    assert (child.scope != null ? child.scope : GlobalNameSpace.GLOBAL) == this;
                    return child;
                }
            }
            if (candidate != null) {
                child = candidate;
            } else {
                child = new DefaultLocalName(this, name);
            }
            // Cache only if the slot is not already occupied by a NameSpace.
            if (!(existing instanceof DefaultNameSpace)) {
                if (childs.put(key, child) != existing) {
                    throw new AssertionError(); // Paranoiac check.
                }
            }
        }
        return child;
    }

    /**
     * Returns a string representation of this namespace.
     *
     * @return A string representation of this namespace.
     */
    @Override
    public String toString() {
        return "NameSpace[\"" + name() + "\"]";
    }

    /**
     * Returns {@code true} if this namespace is equal to the given object.
     *
     * @param object The object to compare with this namespace.
     * @return {@code true} if the given object is equal to this namespace.
     */
    @Override
    public boolean equals(final Object object) {
        if (object != null && object.getClass() == getClass()) {
            final DefaultNameSpace that = (DefaultNameSpace) object;
            return equalsIgnoreParent(that) && Utilities.equals(this.parent, that.parent);
        }
        return false;
    }

    /**
     * Returns {@code true} if the namespace is equal to the given one, ignoring the parent.
     *
     * @param that The namespace to compare with this one.
     * @return {@code true} if both namespaces are equal, ignoring the parent.
     */
    private boolean equalsIgnoreParent(final DefaultNameSpace that) {
        return Utilities.equals(this.headSeparator, that.headSeparator) &&
               Utilities.equals(this.separator,     that.separator) &&
               Utilities.equals(this.name,          that.name); // Most expensive test last.
    }

    /**
     * Returns a hash code value for this namespace.
     */
    @Override
    public int hashCode() {
        return Utilities.hash(parent, name.hashCode() + separator.hashCode());
    }

    /**
     * If an instance already exists for the deserialized namespace, returns that instance.
     * Otherwise completes the initialization of the deserialized instance.
     * <p>
     * Because of its package-private access, this method is <strong>not</strong> invoked if
     * the deserialized class is a subclass defined in an other package. This is the intended
     * behavior since we don't want to replace an instance of a user-defined class.
     *
     * @return The unique instance.
     * @throws ObjectStreamException Should never happen.
     */
    Object readResolve() throws ObjectStreamException {
        final DefaultNameSpace p = (parent != null) ? parent : GlobalNameSpace.GLOBAL;
        final String key = key(name);
        final WeakValueHashMap<String,Object> pool = p.childs;
        synchronized (pool) {
            final Object existing = pool.get(key);
            if (existing instanceof DefaultNameSpace) {
                if (equalsIgnoreParent((DefaultNameSpace) existing)) {
                    return existing;
                } else {
                    // Exit from the synchronized block.
                }
            } else {
                init();
                if (pool.put(key, this) != existing) {
                    throw new AssertionError(); // Paranoiac check.
                }
                return this;
            }
        }
        init();
        return this;
    }
}

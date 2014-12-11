/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2005-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotoolkit.referencing.factory;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.net.URL;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.measure.unit.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import net.jcip.annotations.ThreadSafe;

import org.opengis.util.ScopedName;
import org.opengis.util.GenericName;
import org.opengis.util.FactoryException;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.IdentifiedObject;

import org.geotoolkit.factory.Hints;
import org.geotoolkit.factory.FactoryFinder;
import org.geotoolkit.naming.DefaultNameFactory;
import org.geotoolkit.referencing.NamedIdentifier;
import org.geotoolkit.metadata.iso.citation.Citations;
import org.geotoolkit.resources.Loggings;
import org.geotoolkit.internal.Threads;
import org.geotoolkit.util.XArrays;


/**
 * A datum factory that add {@linkplain IdentifiedObject#getAlias aliases} to a datum name before to
 * delegates the {@linkplain org.geotoolkit.referencing.datum.AbstractDatum#AbstractDatum(Map) datum
 * creation} to an other factory. Aliases are especially important for {@linkplain Datum datum}
 * since their {@linkplain IdentifiedObject#getName name} are often the only way to differentiate
 * them.
 * <p>
 * Two datum with different names are considered incompatible, unless some datum shift method
 * are specified (e.g. {@linkplain org.geotoolkit.referencing.datum.BursaWolfParameters Bursa-Wolf
 * parameters}). Unfortunately, different softwares often use different names for the same datum,
 * which result in {@link org.opengis.referencing.operation.OperationNotFoundException} when
 * attempting to convert coordinates from one
 * {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem coordinate reference system}
 * to an other one. For example "<cite>Nouvelle Triangulation Française (Paris)</cite>" and
 * "<cite>NTF (Paris meridian)</cite>" are actually the same datum. This {@code DatumAliases}
 * class provides a way to handle that.
 * <p>
 * {@code DatumAliases} is a class that determines if a datum name is in our list of aliases
 * and constructs a value for the {@linkplain IdentifiedObject#ALIAS_KEY aliases property}
 * (as {@linkplain GenericName generic names}). The default implementation is backed by the
 * "{@code DatumAliasesTable.csv}" text file. The first uncommented non-blank line in this
 * text file must be the authority names. All other lines are the aliases.
 * <p>
 * Since {@code DatumAliases} is a datum factory, any
 * {@linkplain org.opengis.referencing.AuthorityFactory authority factory} or any
 * {@linkplain org.geotoolkit.io.wkt.ReferencingParser WKT parser} using this factory
 * will takes advantage of the aliases table.
 *
 * @author Rueben Schulz (UBC)
 * @author Martin Desruisseaux (Geomatys, IRD)
 * @version 3.19
 *
 * @see <A HREF="http://gdal.velocet.ca/~warmerda/wktproblems.html">WKT problems</A>
 *
 * @since 2.1
 * @module
 */
@ThreadSafe
public class DatumAliases extends ReferencingFactory implements DatumFactory {
    /**
     * The default file for alias table.
     */
    private static final String ALIAS_TABLE = "DatumAliasesTable.csv";

    /**
     * The column separators in the file to parse.
     */
    private static final String SEPARATORS = ";";

    /**
     * Array used as a marker for alias that has been discarded because never used.
     * This array may appears in {@link #aliasMap} values.
     *
     * @see #freeUnused()
     */
    private static final Object[] NEED_LOADING = new Object[0];

    /**
     * The URL of the alias table. This file is read by {@link #reload()} when first needed.
     */
    private final URL aliasURL;

    /**
     * A map of our datum aliases. Keys are alias names in lower-case, and values are
     * either {@code String[]} or {@code GenericName[]}. In order to reduce the amount
     * of objects created, all values are initially {@code String[]} objects. They are
     * converted to {@code GenericName[]} only when first needed.
     */
    private final Map<String,Object[]> aliasMap = new HashMap<String,Object[]>();

    /**
     * The authorities. This is the first line in the alias table.
     * This array is constructed by {@link #reload()} when first needed.
     */
    private Citation[] authorities;

    /**
     * The factory used for creating names. We do not allow this factory to be set by
     * user-hints because it would be misleading: must of the name creation is actually
     * performed by {@link NamedIdentifier}, which fetches its factory instance itself.
     */
    private transient DefaultNameFactory nameFactory;

    /**
     * The underlying datum factory. If {@code null}, a default factory will be fetch from
     * {@link FactoryFinder} when first needed. A default value can't be set at construction
     * time, since all factories may not be registered at this time.
     */
    private DatumFactory datumFactory;

    /**
     * Constructs a new datum factory with the default backing factory and alias table.
     */
    public DatumAliases() {
        aliasURL = DatumAliases.class.getResource(ALIAS_TABLE);
        if (aliasURL == null) {
            throw new NoSuchElementException(ALIAS_TABLE);
        }
    }

    /**
     * Constructs a new datum factory using the specified factory and the default alias table.
     *
     * @param factory The factory to use for datum creation.
     */
    public DatumAliases(final DatumFactory factory) {
        this();
        datumFactory = factory;
        ensureNonNull("factory", factory);
    }

    /**
     * Constructs a new datum factory which delegates its work to the specified factory.
     * The aliases table is read from the specified URL. The fist uncommented non-blank
     * line in this file most be the authority names. All other names are aliases.
     *
     * @param factory  The factory to use for datum creation.
     * @param aliasURL The URL to the alias table.
     */
    public DatumAliases(final DatumFactory factory, final URL aliasURL) {
        ensureNonNull("factory",  factory );
        ensureNonNull("aliasURL", aliasURL);
        this.aliasURL = aliasURL;
        datumFactory  = factory;
    }

    /**
     * Invoked by {@link org.geotoolkit.factory.FactoryRegistry} in order to set the ordering relative
     * to other factories. The current implementation specifies that this factory should have
     * precedence over {@link ReferencingObjectFactory}.
     *
     * @since 3.00
     */
    @Override
    protected void setOrdering(final Organizer organizer) {
        super.setOrdering(organizer);
        organizer.before(ReferencingObjectFactory.class, false);
    }

    /**
     * Returns the name factory.
     */
    private synchronized DefaultNameFactory getNameFactory() {
        if (nameFactory == null) {
            nameFactory = (DefaultNameFactory) FactoryFinder.getNameFactory(
                    new Hints(Hints.NAME_FACTORY, DefaultNameFactory.class));
        }
        return nameFactory;
    }

    /**
     * Returns the backing datum factory. If no factory were explicitly specified
     * by the user, selects the first datum factory other than {@code this}.
     * <p>
     * <strong>Note:</strong> We can't invoke this method in the constructor, because the
     * constructor is typically invoked during {@code FactoryFinder.scanForPlugins()} execution.
     * {@code scanForPlugins} is looking for {@link DatumFactory} instances, it has not finished
     * to search them, and invoking this method in the constructor would prematurely ask an other
     * {@link DatumFactory} instance while the list is incomplete. Instead, we will invoke this
     * method when the first {@code createXXX} method is invoked, which typically occurs after
     * all factories have been initialized.
     *
     * @return The backing datum factory.
     * @throws NoSuchElementException if there is no such factory.
     */
    final synchronized DatumFactory getDatumFactory() throws NoSuchElementException {
        if (datumFactory == null) {
            DatumFactory candidate;
            final Iterator<DatumFactory> it = FactoryFinder.getDatumFactories(null).iterator();
            do candidate = it.next();
            while (candidate == this);
            datumFactory = candidate;
        }
        return datumFactory;
    }

    /**
     * Returns a caseless version of the specified key, to be stored in the map.
     */
    private static String toCaseless(final String key) {
        return key.replace('_', ' ').trim().toLowerCase();
    }

    /**
     * Reads the next line from the specified input stream, skipping all blank
     * and comment lines. Returns {@code null} on end of stream.
     */
    private static String readLine(final BufferedReader in) throws IOException {
        String line;
        do line = in.readLine();
        while (line != null && ((line = line.trim()).isEmpty() || line.charAt(0) == '#'));
        return line;
    }

    /**
     * Reads again the {@value #ALIAS_TABLE} file into {@link #aliasMap}. This method
     * may be invoked more than once in order to reload entries that have been discarded
     * by {@link #freeUnused}. This method assumes that the file content didn't change
     * between two calls.
     *
     * @throws IOException if the loading failed.
     */
    private void reload() throws IOException {
        assert Thread.holdsLock(this);
        if (LOGGER.isLoggable(Level.CONFIG)) {
            String url = aliasURL.getPath();
            final int s = url.indexOf('!');
            if (s >= 1) url = url.substring(0, s);
            final LogRecord record = Loggings.format(Level.CONFIG, Loggings.Keys.LOADING_DATUM_ALIASES_$1, url);
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
        }
        final BufferedReader in = new BufferedReader(new InputStreamReader(aliasURL.openStream()));
        /*
         * Parses the title line. This line contains authority names as column titles.
         * The authority names will be used as the scope for each identifiers to be created.
         */
        String line = readLine(in);
        if (line != null) {
            final List<Object> elements = new ArrayList<Object>();
            StringTokenizer st = new StringTokenizer(line, SEPARATORS);
            while (st.hasMoreTokens()) {
                final String name = st.nextToken().trim();
                final Citation authority = Citations.fromName(name); // May be null.
                elements.add(authority);
            }
            authorities = elements.toArray(new Citation[elements.size()]);
            final Map<String,String> uniques = new HashMap<String,String>();
            /*
             * Parses all aliases. They are stored as arrays of strings for now, but will be
             * converted to array of generic names by {@link #getAliases} when first needed.
             * If the alias belong to an authority (which should be true in most cases), a
             * scoped name will be created at this time.
             */
            while ((line = readLine(in)) != null) {
                elements.clear();
                uniques.clear();
                st = new StringTokenizer(line, SEPARATORS);
                while (st.hasMoreTokens()) {
                    String alias = st.nextToken().trim();
                    if (!alias.isEmpty()) {
                        final String previous = uniques.put(alias, alias);
                        if (previous != null) {
                            uniques.put(previous, previous);
                            alias = previous;
                        }
                    } else {
                        alias = null;
                    }
                    elements.add(alias);
                }
                // Trim trailing null values only (we must keep other null values).
                for (int i=elements.size(); --i >= 0 && elements.get(i) == null;) {
                    elements.remove(i);
                }
                if (elements.isEmpty()) {
                    continue;
                }
                /*
                 * Copies the aliases array in the aliases map for all local names. If a
                 * previous value is found as an array of GenericName objects, those generic
                 * names are conserved in the map (instead of the string values parsed above)
                 * in order to avoid constructing them again when they will be needed.
                 */
                final String[] names = elements.toArray(new String[elements.size()]);
                for (int i=0; i<names.length; i++) {
                    final String name = names[i];
                    final String key = toCaseless(name);
                    final Object[] previous = aliasMap.put(key, names);
                    if (previous != null && previous != NEED_LOADING) {
                        if (previous instanceof GenericName[]) {
                            aliasMap.put(key, previous);
                        } else if (!Arrays.equals(previous, names)) {
                            // TODO: localize
                            LOGGER.log(Level.WARNING, "Inconsistent aliases for datum \"{0}\".", name);
                        }
                    }
                }
            }
        }
        in.close();
        Threads.executeDisposal(new Runnable() {
            @Override public void run() {
                freeUnused();
            }
        }, 10000); // Arbitrary delay of 10 seconds.
    }

    /**
     * Logs an {@link IOException}.
     */
    private void log(final IOException exception) {
        LogRecord record = Loggings.format(Level.WARNING, Loggings.Keys.CANT_READ_FILE_$1, aliasURL);
        record.setSourceClassName(DatumAliases.class.getName());
        record.setSourceMethodName("reload");
        record.setThrown(exception);
        record.setLoggerName(LOGGER.getName());
        LOGGER.log(record);
    }

    /**
     * Returns the aliases, as a set of {@link GenericName}, for the given name.
     * This method returns an internal array; do not modify the returned value.
     *
     * @param name Datum alias name to lookup.
     * @return A set of datum aliases as {@link GenericName} objects for the given name,
     *         or {@code null} if the name is not in our list of aliases.
     *
     * @see #addAliases
     * @see #reload
     */
    private synchronized GenericName[] getAliases(String name) {
        if (aliasMap.isEmpty()) try {
            reload();
        } catch (IOException exception) {
            log(exception);
            // Continue in case the requested alias has been read before the failure occurred.
        }
        /*
         * Gets the aliases for the specified name.  If an entry exists for this name with a null
         * value, this means that 'freeUnused()' has been invoked previously. Reload the file and
         * try again since the requested name may be one of the set of discarded aliases.
         */
        name = toCaseless(name);
        Object[] aliases = aliasMap.get(name);
        if (aliases == null) {
            // Unknow name. We are done.
            return null;
        }
        if (aliases == NEED_LOADING) {
            // Known name, but the list of alias has been previously
            // discarded because never used. Reload the file.
            try {
                reload();
            } catch (IOException exception) {
                log(exception);
                // Continue in case the requested alias has been read before the failure occurred.
            }
            aliases = aliasMap.get(name);
            if (aliases == NEED_LOADING) {
                // Should never happen, unless reloading failed or some lines have
                // been deleted in the file since last time the file has been loaded.
                return null;
            }
        }
        if (aliases instanceof GenericName[]) {
            return (GenericName[]) aliases;
        }
        /*
         * Aliases has been found, but available as an array of strings only. This means
         * that those aliases have never been requested before. Transforms the array of
         * strings into an array of generic names. The new array replaces the old one for
         * all aliases enumerated in the array (not just the requested one).
         */
        int count = 0;
        GenericName[] names = new GenericName[aliases.length];
        for (int i=0; i<aliases.length; i++) {
            final String alias = (String) aliases[i];
            if (alias != null) {
                Citation authority = null;
                if (count < authorities.length) {
                    authority = authorities[count];
                }
                names[count++] = new NamedIdentifier(authority, alias);
            }
        }
        names = XArrays.resize(names, count);
        for (int i=0; i<names.length; i++) {
            final String alias = names[i].tip().toString();
            final Object[] previous = aliasMap.put(toCaseless(alias), names);
            assert previous == names || Arrays.equals(aliases, previous) : alias;
        }
        return names;
    }

    /**
     * Completes the given map of properties. This method expects a map of properties to be
     * given to {@link AbstractDatum#AbstractDatum(Map)} constructor. The name is fetch from the
     * {@link IdentifiedObject#NAME_KEY NAME_KEY}. The {@link AbstractIdentifiedObject#ALIAS_KEY
     * ALIAS_KEY} is completed with the aliases know to this factory.
     *
     * @param  properties The set of properties to complete.
     * @return The completed properties, or {@code properties} if no change were done.
     *
     * @see #getAliases
     */
    private Map<String,?> addAliases(Map<String,?> properties) {
        ensureNonNull("properties", properties);
        Object value = properties.get(IdentifiedObject.NAME_KEY);
        ensureNonNull("name", value);
        final String name;
        if (value instanceof Identifier) {
            name = ((Identifier) value).getCode();
        } else {
            name = value.toString();
        }
        GenericName[] aliases = getAliases(name);
        if (aliases != null) {
            /*
             * Aliases have been found. Before to add them to the properties map, overrides them
             * with the aliases already provided by the users, if any. The 'merged' map is the
             * union of aliases know to this factory and aliases provided by the user. User's
             * aliases will be added first, for preserving the user's order (the LinkedHashMap
             * acts as a FIFO queue).
             */
            int count = aliases.length;
            value = properties.get(IdentifiedObject.ALIAS_KEY);
            if (value != null) {
                final Map<String,GenericName> merged = new LinkedHashMap<String,GenericName>();
                putAll(getNameFactory().toArray(value), merged);
                count -= putAll(aliases, merged);
                final Collection<GenericName> c = merged.values();
                aliases = c.toArray(new GenericName[c.size()]);
            }
            /*
             * Now set the aliases. This replacement will not be performed if
             * all our aliases were replaced by user's aliases (count <= 0).
             */
            if (count > 0) {
                final Map<String,Object> copy = new HashMap<String,Object>(properties);
                copy.put(IdentifiedObject.ALIAS_KEY, aliases);
                properties = copy;
            }
        }
        return properties;
    }

    /**
     * Puts all elements in the {@code names} array into the specified map. Order matter, since the
     * first element in the array should be the first element returned by the map if the map is
     * actually an instance of {@link LinkedHashMap}. This method returns the number of elements
     * ignored.
     */
    private static int putAll(final GenericName[] names, final Map<String,GenericName> map) {
        int ignored = 0;
        for (int i=0; i<names.length; i++) {
            final GenericName   name = names[i];
            final GenericName scoped = name.toFullyQualifiedName();
            final String         key = toCaseless(scoped.toString());
            final GenericName    old = map.put(key, name);
            if (old instanceof ScopedName) {
                map.put(key, old); // Preserves the user value, except if it was unscoped.
                ignored++;
            }
        }
        return ignored;
    }

    /**
     * Creates an engineering datum.
     *
     * @param  properties Name and other properties to give to the new object.
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public EngineeringDatum createEngineeringDatum(final Map<String,?> properties)
            throws FactoryException
    {
        return getDatumFactory().createEngineeringDatum(addAliases(properties));
    }

    /**
     * Creates geodetic datum from ellipsoid and (optionally) Bursa-Wolf parameters.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  ellipsoid Ellipsoid to use in new geodetic datum.
     * @param  primeMeridian Prime meridian to use in new geodetic datum.
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public GeodeticDatum createGeodeticDatum(final Map<String,?> properties,
            final Ellipsoid ellipsoid, final PrimeMeridian primeMeridian) throws FactoryException
    {
        return getDatumFactory().createGeodeticDatum(addAliases(properties), ellipsoid, primeMeridian);
    }

    /**
     * Creates an image datum.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  pixelInCell Specification of the way the image grid is associated
     *         with the image data attributes.
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public ImageDatum createImageDatum(final Map<String,?> properties,
            final PixelInCell pixelInCell) throws FactoryException
    {
        return getDatumFactory().createImageDatum(addAliases(properties), pixelInCell);
    }

    /**
     * Creates a temporal datum from an enumerated type value.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  origin The date and time origin of this temporal datum.
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public TemporalDatum createTemporalDatum(final Map<String,?> properties,
            final Date origin) throws FactoryException
    {
        return getDatumFactory().createTemporalDatum(addAliases(properties), origin);
    }

    /**
     * Creates a vertical datum from an enumerated type value.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  type The type of this vertical datum (often geoidal).
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public VerticalDatum createVerticalDatum(final Map<String,?> properties,
            final VerticalDatumType type) throws FactoryException
    {
        return getDatumFactory().createVerticalDatum(addAliases(properties), type);
    }

    /**
     * Creates an ellipsoid from radius values.
     * This method does not add any alias to the ellipsoid object. In Geotk implementation,
     * ellipsoids don't need aliases because their name can be ignored during comparisons.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  semiMajorAxis Equatorial radius in supplied linear units.
     * @param  semiMinorAxis Polar radius in supplied linear units.
     * @param  unit Linear units of ellipsoid axes.
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public Ellipsoid createEllipsoid(final Map<String,?> properties,
            final double semiMajorAxis, final double semiMinorAxis, final Unit<Length> unit)
            throws FactoryException
    {
        return getDatumFactory().createEllipsoid(properties, semiMajorAxis, semiMinorAxis, unit);
    }

    /**
     * Creates an ellipsoid from an major radius, and inverse flattening.
     * This method does not add any alias to the ellipsoid object. In Geotk implementation,
     * ellipsoids don't need aliases because their name can be ignored during comparisons.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  semiMajorAxis Equatorial radius in supplied linear units.
     * @param  inverseFlattening Eccentricity of ellipsoid.
     * @param  unit Linear units of major axis.
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public Ellipsoid createFlattenedSphere(final Map<String,?> properties,
            final double semiMajorAxis, final double inverseFlattening, final Unit<Length> unit)
            throws FactoryException
    {
        return getDatumFactory().createFlattenedSphere(properties, semiMajorAxis, inverseFlattening, unit);
    }

    /**
     * Creates a prime meridian, relative to Greenwich.
     * This method does not add any alias to the prime meridian object. In Geotk implementation,
     * prime meridians don't need aliases because their name can be ignored during comparisons.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  longitude Longitude of prime meridian in supplied angular units East of Greenwich.
     * @param  angularUnit Angular units of longitude.
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public PrimeMeridian createPrimeMeridian(final Map<String,?> properties,
            final double longitude, final Unit<Angle> angularUnit) throws FactoryException
    {
        return getDatumFactory().createPrimeMeridian(properties, longitude, angularUnit);
    }

    /**
     * Frees all aliases that have been unused up to date. If one of those alias is needed at a
     * later time, the aliases table will be reloaded.
     * <p>
     * This method is invoked automatically after the timeout and doesn't need to be invoked
     * explicitely.
     */
    final synchronized void freeUnused() {
        for (final Map.Entry<String,Object[]> entry : aliasMap.entrySet()) {
            final Object[] value = entry.getValue();
            if (!(value instanceof GenericName[])) {
                entry.setValue(NEED_LOADING);
            }
        }
    }
}

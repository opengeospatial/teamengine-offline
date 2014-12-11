/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2011-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2011-2012, Geomatys
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
package org.geotoolkit.internal.referencing;

import java.util.Arrays;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.UnmodifiableGeometryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.geotoolkit.geometry.AbstractDirectPosition;


/**
 * A read-only direct position wrapping an array without performing any copy.
 * This class shall be used for temporary objects only (it is not serializable for this reason).
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.20
 *
 * @since 3.20
 * @module
 */
public final class DirectPositionView implements DirectPosition {
    /**
     * The ordinate values.
     */
    private final double[] ordinates;

    /**
     * The index of the first value in the {@linkplain #ordinates} array.
     */
    private final int offset;

    /**
     * The number of valid ordinate values.
     */
    private final int dimension;

    /**
     * Creates a new direct position wrapping the given array.
     *
     * @param ordinates The ordinate values.
     * @param offset    The first value index in the ordinates array.
     * @param dimension The number of valid ordinate values.
     */
    public DirectPositionView(final double[] ordinates, final int offset, final int dimension) {
        this.ordinates = ordinates;
        this.offset    = offset;
        this.dimension = dimension;
    }

    /**
     * Returns {@code null} since there is no CRS associated with this position.
     */
    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return null;
    }

    /**
     * Returns the dimension given at construction time.
     */
    @Override
    public int getDimension() {
        return dimension;
    }

    /**
     * Returns all ordinate values.
     */
    @Override
    public double[] getCoordinate() {
        return Arrays.copyOfRange(ordinates, offset, offset + dimension);
    }

    /**
     * Returns the ordinate at the given index.
     * <strong>This implementation does not check index validity</strong>,
     * unless assertions are enabled.
     *
     * @param dim The dimension of the ordinate to get fetch.
     */
    @Override
    public double getOrdinate(final int dim) {
        assert dim >= 0 && dim < dimension : dim;
        return ordinates[offset + dim];
    }

    /**
     * Do not allow any change.
     */
    @Override
    public void setOrdinate(final int dimension, final double value) {
        throw new UnmodifiableGeometryException();
    }

    /**
     * Returns the direct position, which is {@code this}.
     */
    @Override
    public DirectPosition getDirectPosition() {
        return this;
    }

    /**
     * Returns a string representation of this direct position for debugging purpose.
     */
    @Override
    public String toString() {
        return AbstractDirectPosition.toString(this);
    }
}

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
package org.opengis.coverage;

import java.util.Set;
import java.util.List;
import java.util.Collection;
import java.awt.image.Raster;
import java.awt.image.renderable.RenderableImage;
import org.opengis.metadata.extent.Extent;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.temporal.Period;
import org.opengis.util.Record;
import org.opengis.util.RecordType;
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A function from a spatial, temporal or spatiotemporal domain to an attribute range. A coverage
 * associates a {@linkplain DirectPosition position} within its domain to a record of values of
 * defined data types. Examples include a raster image, polygon overlay, or digital elevation matrix.
 * The essential property of coverage is to be able to generate a value for any point
 * within its domain. How coverage is represented internally is not a concern.
 *
 * For example consider the following different internal representations of coverage:<br>
 *  <UL>
 *    <li>A coverage may be represented by a set of polygons which exhaustively
 *        tile a plane (that is each point on the plane falls in precisely one polygon).
 *        The value returned by the coverage for a point is the value of an attribute of
 *        the polygon that contains the point.</li>
 *    <li>A coverage may be represented by a grid of values
 *        (a {@linkplain DiscreteGridPointCoverage Discrete Grid Point Coverage}).
 *        If the coverage is a {@linkplain ContinuousQuadrilateralGridCoverage Continuous
 *        Quadrilateral Grid Coverage} using {@linkplain InterpolationMethod#NEAREST_NEIGHBOUR
 *        Nearest Neighbour} interpolation method, then the value returned by the coverage for
 *        a point is that of the grid value whose location is nearest the point.</li>
 *    <li>Coverage may be represented by a mathematical function.
 *        The value returned by the coverage for a point is just the return value
 *        of the function when supplied the coordinates of the point as arguments.</li>
 *    <li>Coverage may be represented by combination of these.
 *        For example, coverage may be represented by a combination of mathematical
 *        functions valid over a set of polynomials.</LI>
 * </UL>
 *
 * <h3>Metadata</h3>
 * The legacy {@linkplain Specification#OGC_01004 OGC 01-004} specification provided some methods for
 * fetching metadata values attached to a coverage. The {@linkplain Specification#ISO_19123 ISO 19123}
 * specification do not provides such methods. Implementations that want to provide such metadata are
 * encouraged to implement the {@link javax.media.jai.PropertySource} or
 * {@link javax.media.jai.WritablePropertySource} interface.
 *
 * @version ISO 19123:2004
 * @author  Stephane Fellah
 * @author  Martin Desruisseaux (IRD)
 * @author  Wim Koolhoven
 * @author  Alexander Petkov
 * @since   GeoAPI 2.1
 */
@UML(identifier="CV_Coverage", specification=ISO_19123)
public interface Coverage {
    /**
     * Returns the coordinate reference system to which the objects in its domain are referenced.
     * This is the CRS used when accessing a coverage or grid coverage with the {@code evaluate(...)}
     * methods. This coordinate reference system is usually different than coordinate system of the
     * grid. It is the target coordinate reference system of the
     * {@link org.opengis.coverage.grid.GridGeometry#getGridToCRS gridToCRS} math transform.
     * <p>
     * Grid coverage can be accessed (re-projected) with new coordinate reference system with the
     * {@link org.opengis.coverage.processing.GridCoverageProcessor} component. In this case, a new
     * instance of a grid coverage is created.
     *
     * @return The coordinate reference system used when accessing a coverage or
     *         grid coverage with the {@code evaluate(...)} methods.
     */
    @UML(identifier="CRS", obligation=MANDATORY, specification=ISO_19123)
    CoordinateReferenceSystem getCoordinateReferenceSystem();

    /**
     * The bounding box for the coverage domain in {@linkplain #getCoordinateReferenceSystem
     * coordinate reference system} coordinates. For grid coverages, the grid cells are centered
     * on each grid coordinate. The envelope for a 2-D grid coverage includes the following corner
     * positions.
     *
     * <blockquote><pre>
     * (Minimum row - 0.5, Minimum column - 0.5) for the minimum coordinates
     * (Maximum row - 0.5, Maximum column - 0.5) for the maximum coordinates
     * </pre></blockquote>
     *
     * If a grid coverage does not have any associated coordinate reference system,
     * the minimum and maximum coordinate points for the envelope will be empty sequences.
     *
     * @return The bounding box for the coverage domain in coordinate system coordinates.
     *
     * @todo We need to explain the relationship with {@link #getDomainExtents}, if any.
     */
    @UML(identifier="envelope", obligation=MANDATORY, specification=OGC_01004)
    Envelope getEnvelope();

    /**
     * Returns the extent of the domain of the coverage. Extents may be specified in space,
     * time or space-time. The collection must contains at least one element.
     *
     * @return The domain extent of the coverage.
     */
    @UML(identifier="domainExtent", obligation=MANDATORY, specification=ISO_19123)
    Set<Extent> getDomainExtents();

    /**
     * Returns the set of domain objects in the domain.
     * The collection must contains at least one element.
     *
     * @return The domain elements.
     */
    @UML(identifier="domainElement", obligation=MANDATORY, specification=ISO_19123)
    Set<? extends DomainObject<?>> getDomainElements();

    /**
     * Returns the set of attribute values in the range. The range of a coverage shall be a
     * homogeneous collection of records. That is, the range shall have a constant dimension
     * over the entire domain, and each field of the record shall provide a value of the same
     * attribute type over the entire domain.
     * <p>
     * In the case of a {@linkplain DiscreteCoverage discrete coverage}, the size of the range
     * collection equals that of the {@linkplain #getDomainElements domains} collection. In other
     * words, there is one instance of {@link AttributeValues} for each instance of {@link DomainObject}.
     * Usually, these are stored values that are accessed by the
     * {@link #evaluate(DirectPosition,Collection) evaluate} operation.
     * <p>
     * In the case of a {@linkplain ContinuousCoverage continuous coverage}, there is a transfinite
     * number of instances of {@link AttributeValues} for each {@link DomainObject}. A few instances
     * may be stored as input for the {@link #evaluate(DirectPosition,Collection) evaluate} operation,
     * but most are generated as needed by that operation.
     * <p>
     * <B>NOTE:</B> ISO 19123 does not specify how the {@linkplain #getDomainElements domain}
     * and {@linkplain #getRangeElements range} associations are to be implemented. The relevant
     * data may be generated in real time, it may be held in persistent local storage, or it may
     * be electronically accessible from remote locations.
     *
     * @return The attribute values in the range.
     */
    @UML(identifier="rangeElement", obligation=OPTIONAL, specification=ISO_19123)
    Collection<AttributeValues> getRangeElements();

    /**
     * Describes the range of the coverage. It consists of a list of attribute name/data type pairs.
     * A simple list is the most common form of range type, but {@code RecordType} can be used
     * recursively to describe more complex structures. The range type for a specific coverage
     * shall be specified in an application schema.
     *
     * @return The coverage range.
     */
    @UML(identifier="rangeType", obligation=MANDATORY, specification=ISO_19123)
    RecordType getRangeType();

    /**
     * Identifies the procedure to be used for evaluating the coverage at a position that falls
     * either on a boundary between geometric objects or within the boundaries of two or more
     * overlapping geometric objects. The geometric objects are either {@linkplain DomainObject
     * domain objects} or {@linkplain ValueObject value objects}.
     *
     * @return The procedure for evaluating the coverage on overlapping geometries.
     */
    @UML(identifier="commonPointRule", obligation=MANDATORY, specification=ISO_19123)
    CommonPointRule getCommonPointRule();

    /**
     * Returns the dictionary of <var>geometry</var>-<var>value</var> pairs that contain the
     * {@linkplain DomainObject objects} in the domain of the coverage each paired with its
     * record of feature attribute values. In the case of an analytical coverage, the operation
     * shall return the empty set.
     *
     * @return The geometry-value pairs.
     */
    @UML(identifier="list", obligation=MANDATORY, specification=ISO_19123)
    Set<? extends GeometryValuePair> list();

    /**
     * Returns the set of <var>geometry</var>-<var>value</var> pairs that contain
     * {@linkplain DomainObject domain objects} that lie within the specified geometry and period.
     * If {@code s} is null, the operation shall return all <var>geometry</var>-<var>value</var>
     * pairs that contain {@linkplain DomainObject domain objects} within {@code t}. If the value
     * of {@code t} is null, the operation shall return all <var>geometry</var>-<var>value</var>
     * pair that contain {@linkplain DomainObject domain objects} within {@code s}. In the case
     * of an analytical coverage, the operation shall return the empty set.
     *
     * @param s The spatial component.
     * @param t The temporal component.
     * @return The values in the given spatio-temporal domain.
     */
    @UML(identifier="select", obligation=MANDATORY, specification=ISO_19123)
    Set<? extends GeometryValuePair> select(Geometry s, Period t);

    /**
     * Returns the sequence of <var>geometry</var>-<var>value</var> pairs that include the
     * {@linkplain DomainObject domain objects} nearest to the direct position and their distances
     * from the direction position. The sequence shall be ordered by distance from the direct position,
     * beginning with the record containing the {@linkplain DomainObject domain object} nearest to the
     * direct position. The length of the sequence (the number of <var>geometry</var>-<var>value</var>
     * pairs returned) shall be no greater than the number specified by the parameter {@code limit}.
     * The default shall be to return a single <var>geometry</var>-<var>value</var> pair. The operation
     * shall return a warning if the last {@linkplain DomainObject domain object} in the sequence is at
     * a distance from the direct position equal to the distance of other {@linkplain DomainObject domain
     * objects} that are not included in the sequence. In the case of an analytical coverage, the operation
     * shall return the empty set.
     * <p>
     * <B>NOTE:</B> This operation is useful when the domain of a coverage does not exhaustively
     * partition the extent of the coverage. Even in that case, the first element of the sequence
     * returned may be the <var>geometry</var>-<var>value</var> pair that contains the input direct
     * position.
     *
     * @param  p The search position.
     * @param  limit The maximal size of the list to be returned.
     * @return The <var>geometry</var>-<var>value</var> pairs nearest to the given position.
     */
    @UML(identifier="find", obligation=MANDATORY, specification=ISO_19123)
    List<? extends GeometryValuePair> find(DirectPosition p, int limit);

    /**
     * Returns the nearest <var>geometry</var>-<var>value</var> pair from the specified direct
     * position. This is a shortcut for <code>{@linkplain #find(DirectPosition,int) find}(p,1)</code>.
     *
     * @param  p The search position.
     * @return The <var>geometry</var>-<var>value</var> pair nearest to the given position.
     */
    @UML(identifier="find", obligation=MANDATORY, specification=ISO_19123)
    GeometryValuePair find(DirectPosition p);

    /**
     * Returns a set of records of feature attribute values for the specified direct position. The
     * parameter {@code list} is a sequence of feature attribute names each of which identifies a
     * field of the range type. If {@code list} is null, the operation shall return a value for
     * every field of the range type. Otherwise, it shall return a value for each field included in
     * {@code list}. If the direct position passed is not in the domain of the coverage, then an
     * exception is thrown. If the input direct position falls within two or more geometric objects
     * within the domain, the operation shall return records of feature attribute values computed
     * according to the {@linkplain #getCommonPointRule common point rule}.
     * <P>
     * <B>NOTE:</B> Normally, the operation will return a single record of feature attribute values.
     *
     * @param  p The position where to evaluate.
     * @param  list The field of interest, or {@code null} for every fields.
     * @return The feature attributes.
     * @throws PointOutsideCoverageException if the point is outside the coverage domain.
     * @throws CannotEvaluateException If the point can't be evaluated for some other reason.
     */
    @UML(identifier="evaluate", obligation=MANDATORY, specification=ISO_19123)
    Set<Record> evaluate(DirectPosition p, Collection<String> list)
            throws PointOutsideCoverageException, CannotEvaluateException;

    /**
     * Return the value vector for a given point in the coverage.
     * A value for each sample dimension is included in the vector.
     * The default interpolation type used when accessing grid values for points
     * which fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     * <p>
     * <strong>WARNING:</strong> This method is inherited from the legacy OGC 01-004
     * specification and may be deprecated in a future version. We are for more experience
     * and feedbacks on the value of this method.
     *
     * @param  point Point at which to find the grid values.
     * @return The value vector for a given point in the coverage.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException If the point can't be evaluated for some other reason.
     * @see Raster#getDataElements(int, int, Object)
     */
    @UML(identifier="evaluate", obligation=MANDATORY, specification=OGC_01004)
    Object evaluate(DirectPosition point) throws PointOutsideCoverageException, CannotEvaluateException;

    /**
     * Return a sequence of boolean values for a given point in the coverage.
     * A value for each sample dimension is included in the sequence.
     * The default interpolation type used when accessing grid values for points which
     * fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     *
     * @param  point Point at which to find the coverage values.
     * @param  destination An optionally preallocated array in which to store the values,
     *         or {@code null} if none.
     * @return A sequence of boolean values for a given point in the coverage.
     *         If {@code destination} was non-null, then it is returned.
     *         Otherwise, a new array is allocated and returned.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException if the point can't be evaluated for some othe reason.
     * @throws ArrayIndexOutOfBoundsException if the {@code destination} array is not null
     *         and too small to hold the output.
     */
    @UML(identifier="evaluateAsBoolean", obligation=MANDATORY, specification=OGC_01004)
    boolean[] evaluate(DirectPosition point, boolean[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException, ArrayIndexOutOfBoundsException;

    /**
     * Return a sequence of unsigned byte values for a given point in the coverage.
     * A value for each sample dimension is included in the sequence.
     * The default interpolation type used when accessing grid values for points which
     * fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     *
     * @param  point Point at which to find the coverage values.
     * @param  destination An optionally preallocated array in which to store the values,
     *         or {@code null} if none.
     * @return A sequence of unsigned byte values for a given point in the coverage.
     *         If {@code destination} was non-null, then it is returned.
     *         Otherwise, a new array is allocated and returned.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException if the point can't be evaluated for some othe reason.
     * @throws ArrayIndexOutOfBoundsException if the {@code destination} array is not null
     *         and too small to hold the output.
     */
    @UML(identifier="evaluateAsByte", obligation=MANDATORY, specification=OGC_01004)
    byte[] evaluate(DirectPosition point, byte[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException, ArrayIndexOutOfBoundsException;

    /**
     * Return a sequence of integer values for a given point in the coverage.
     * A value for each sample dimension is included in the sequence.
     * The default interpolation type used when accessing grid values for points which
     * fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     *
     * @param  point Point at which to find the grid values.
     * @param  destination An optionally preallocated array in which to store the values,
     *         or {@code null} if none.
     * @return A sequence of integer values for a given point in the coverage.
     *         If {@code destination} was non-null, then it is returned.
     *         Otherwise, a new array is allocated and returned.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException if the point can't be evaluated for some othe reason.
     * @throws ArrayIndexOutOfBoundsException if the {@code destination} array is not null
     *         and too small to hold the output.
     *
     * @see Raster#getPixel(int, int, int[])
     */
    @UML(identifier="evaluateAsInteger", obligation=MANDATORY, specification=OGC_01004)
    int[] evaluate(DirectPosition point, int[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException, ArrayIndexOutOfBoundsException;

    /**
     * Return a sequence of float values for a given point in the coverage.
     * A value for each sample dimension is included in the sequence.
     * The default interpolation type used when accessing grid values for points which
     * fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     *
     * @param  point Point at which to find the grid values.
     * @param  destination An optionally preallocated array in which to store the values,
     *         or {@code null} if none.
     * @return A sequence of float values for a given point in the coverage.
     *         If {@code destination} was non-null, then it is returned.
     *         Otherwise, a new array is allocated and returned.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException if the point can't be evaluated for some othe reason.
     * @throws ArrayIndexOutOfBoundsException if the {@code destination} array is not null
     *         and too small to hold the output.
     *
     * @see Raster#getPixel(int, int, float[])
     */
    float[] evaluate(DirectPosition point, float[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException, ArrayIndexOutOfBoundsException;

    /**
     * Return a sequence of double values for a given point in the coverage.
     * A value for each sample dimension is included in the sequence.
     * The default interpolation type used when accessing grid values for points which
     * fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     *
     * @departure integration
     *   OGC 01-004 defines this method as <code>evaluate(DirectPosition)</code>. GeoAPI adds
     *   the <code>double[]</code> argument for reusing pre-allocated arrays, which is consistent
     *   with usage in <code>java.awt.image.Raster</code>.
     *
     * @param  point Point at which to find the grid values.
     * @param  destination An optionally preallocated array in which to store the values,
     *         or {@code null} if none.
     * @return A sequence of double values for a given point in the coverage.
     *         If {@code destination} was non-null, then it is returned.
     *         Otherwise, a new array is allocated and returned.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException If the point can't be evaluated for some othe reason.
     * @throws ArrayIndexOutOfBoundsException if the {@code destination} array is not null
     *         and too small to hold the output.
     *
     * @see Raster#getPixel(int, int, double[])
     */
    @UML(identifier="evaluateAsDouble", obligation=MANDATORY, specification=OGC_01004)
    double[] evaluate(DirectPosition point, double[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException, ArrayIndexOutOfBoundsException;

    /**
     * Returns a set of {@linkplain DomainObject domain objects} for the specified record of feature
     * attribute values. Normally, this method returns the set of {@linkplain DomainObject objects}
     * in the domain that are associated with values equal to those in the input record. However,
     * the operation may return other {@linkplain DomainObject objects} derived from those in the
     * domain, as specified by the application schema.
     * <p>
     * <B>Example:</B> The {@code evaluateInverse} operation could return a set
     * of contours derived from the feature attribute values associated with the
     * {@linkplain org.opengis.coverage.grid.GridPoint grid points} of a grid coverage.
     *
     * @param  v The feature attributes.
     * @return The domain where the attributes are found.
     */
    @UML(identifier="evaluateInverse", obligation=MANDATORY, specification=ISO_19123)
    Set<? extends DomainObject<?>> evaluateInverse(Record v);

    /**
     * The number of sample dimensions in the coverage.
     * For grid coverages, a sample dimension is a band.
     * <p>
     * <strong>WARNING:</strong> This method is inherited from the legacy OGC 01-004
     * specification and may be deprecated in a future version. We are for more experience
     * and feedbacks on the value of this method.
     *
     * @return The number of sample dimensions in the coverage.
     */
    @UML(identifier="numSampleDimensions", obligation=MANDATORY, specification=OGC_01004)
    int getNumSampleDimensions();

    /**
     * Retrieve sample dimension information for the coverage.
     * For a grid coverage a sample dimension is a band. The sample dimension information
     * include such things as description, data type of the value (bit, byte, integer...),
     * the no data values, minimum and maximum values and a color table if one is
     * associated with the dimension. A coverage must have at least one sample dimension.
     * <p>
     * <strong>WARNING:</strong> This method is inherited from the legacy OGC 01-004
     * specification and may be deprecated in a future version. We are for more experience
     * and feedbacks on the value of this method.
     *
     * @param  index Index for sample dimension to retrieve. Indices are numbered 0 to
     *         (<var>{@linkplain #getNumSampleDimensions n}</var>-1).
     * @return Sample dimension information for the coverage.
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds.
     */
    @UML(identifier="getSampleDimension", obligation=MANDATORY, specification=OGC_01004)
    SampleDimension getSampleDimension(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the sources data for a coverage.
     * This is intended to allow applications to establish what {@code Coverage}s
     * will be affected when others are updated, as well as to trace back to the "raw data".
     * <p>
     * This implementation specification does not include interfaces for creating
     * collections of coverages therefore the list size will usually be one indicating
     * an adapted grid coverage, or zero indicating a raw grid coverage.
     * <p>
     * <strong>WARNING:</strong> This method is inherited from the legacy OGC 01-004
     * specification and may be deprecated in a future version. We are for more experience
     * and feedbacks on the value of this method.
     *
     * @departure generalization
     *   Return type of <code>Coverage.getSource(int)</code> has been relaxed from <code>GridCoverage</code>
     *   to <code>Coverage</code>. Instead, the return type has been constrained to <code>GridCoverage</code>
     *   only in <code>GridCoverage.getSource(int)</code>. This approach (<cite>return type covariance</cite>)
     *   was already used in "<cite>Spatial Referencing by Coordinates</cite>" (ISO 19111). It avoid forward
     *   dependency of <code>Coverage</code> toward <code>GridCoverage</code> and give more flexibility for
     *   use of <code>Coverage</code> with non-gridded sources.
     *
     * @return The list of sources data for a coverage.
     */
    @UML(identifier="getSource, numSource", obligation=MANDATORY, specification=OGC_01004)
    List<? extends Coverage> getSources();

    /**
     * Returns 2D view of this coverage as a renderable image.
     * This optional operation allows interoperability with
     * <A HREF="http://java.sun.com/products/java-media/2D/">Java2D</A>.
     * If this coverage is a {@link org.opengis.coverage.grid.GridCoverage} backed
     * by a {@link java.awt.image.RenderedImage}, the underlying image can be obtained
     * with:
     *
     * <code>getRenderableImage(0,1).{@linkplain RenderableImage#createDefaultRendering()
     * createDefaultRendering()}</code>
     *
     * @departure integration
     *   Added this optional method for interoperability with Java2D.
     *
     * @param  xAxis Dimension to use for the <var>x</var> axis.
     * @param  yAxis Dimension to use for the <var>y</var> axis.
     * @return A 2D view of this coverage as a renderable image.
     * @throws UnsupportedOperationException if this optional operation is not supported.
     * @throws IndexOutOfBoundsException if {@code xAxis} or {@code yAxis} is out of bounds.
     */
    @Extension
    RenderableImage getRenderableImage(int xAxis, int yAxis)
            throws UnsupportedOperationException, IndexOutOfBoundsException;
}

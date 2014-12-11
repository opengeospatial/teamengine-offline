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
package org.geotoolkit.referencing.operation.transform;


/**
 * Strategy for iterating over the point arrays given to
 * {@link AbstractMathTransform#transform(double[],int,double[],int,int) transform} methods.
 * If the source and destination arrays are the same and the region of the array to be written
 * overlaps the region of the array to be read, it may be necessary to iterate over the points
 * in reverse order or to copy some points in a temporary array. The {@link #suggest suggest}
 * method in this class returns a strategy suitable to the {@code transform} arguments.
 * <p>
 * Implementation example:
 *
 * {@preformat java
 *     public void transform(double[] srcPts, int srcOff,
 *                           double[] dstPts, int dstOff, int numPts)
 *     {
 *         int srcInc = getSourceDimension();
 *         int dstInc = getTargetDimension();
 *         if (srcPts == dstPts) {
 *             switch (IterationStrategy.suggest(srcOff, srcInc, dstOff, dstInc, numPts)) {
 *                 case ASCENDING: {
 *                     break;
 *                 }
 *                 case DESCENDING: {
 *                     srcOff += (numPts-1) * srcInc; srcInc = -srcInc;
 *                     dstOff += (numPts-1) * dstInc; dstInc = -dstInc;
 *                     break;
 *                 }
 *                 default: {
 *                     srcPts = Arrays.copyOfRange(srcPts, srcOff, srcOff + numPts*srcInc);
 *                     srcOff = 0;
 *                     break;
 *                 }
 *             }
 *         }
 *         while (--numPts >= 0) {
 *             // TODO by the implementor here:
 *             //   1. Read the coordinate starting at srcPts[srcOff].
 *             //   2. Tranform it.
 *             //   3. Store the result starting at dstPts[dstOff].
 *             srcOff += srcInc;
 *             dstOff += dstInc;
 *         }
 *     }
 * }
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.00
 *
 * @since 3.00
 * @level advanced
 * @module
 */
public enum IterationStrategy {
    /**
     * Iterate over the points in ascending index order.
     * There is no need to copy the array.
     */
    ASCENDING(false),

    /**
     * Iterate over the points in descending index order.
     * There is no need to copy the array.
     */
    DESCENDING(false),

    /**
     * Copies the points to transform in a temporary array before to apply the transform.
     * The temporary array will be used for fetching the source ordinates.
     * <p>
     * This algorithm can be used as a fallback for any unknown enumeration.
     */
    BUFFER_SOURCE(true),

    /**
     * Writes the transformed points in a temporary array and copies them to the
     * destination subarray when the transformation is finished.
     * <p>
     * Developers are allowed to ignore this value and fallback on the same algorithm
     * than {@link #BUFFER_SOURCE}, which is often easier to implement.
     */
    BUFFER_TARGET(true);

    /**
     * {@code true} if this iteration strategy requires the use of a buffer.
     */
    final boolean needBuffer;

    /**
     * Creates a new enum.
     *
     * @param needBuffer {@code true} if this iteration strategy requires the use of a buffer.
     */
    private IterationStrategy(final boolean needBuffer) {
        this.needBuffer = needBuffer;
    }

    /**
     * Suggests a strategy for iterating over the points to transform in an array. This convenience
     * method is provided for {@link AbstractMathTransform#transform(double[],int,double[],int,int)
     * transform} method implementations. It makes the following assumptions:
     * <p>
     * <ul>
     *   <li>Source and target array are the same.</li>
     *   <li>For each coordinate to be transformed, all ordinate values are read before the
     *       transformation process starts. For example if a transform goes from geographic
     *       to projected CRS, the (<var>longitude</var>, <var>latitude</var>, <var>height</var>)
     *       tuple must be completely read before to start writing the
     *       (<var>x</var>,<var>y</var>,<var>z</var>) tuple.</li>
     * </ul>
     *
     * @param  srcOff The offset in the source coordinate array.
     * @param  srcDim The dimension of input points.
     * @param  dstOff The offset in the destination coordinate array.
     * @param  dstDim The dimension of output points.
     * @param  numPts The number of points to transform.
     * @return A strategy for iterating over the points during the transformation process.
     */
    public static IterationStrategy suggest(final int srcOff, final int srcDim,
                                            final int dstOff, final int dstDim, final int numPts)
    {
        if (numPts <= 1) {
            /*
             * Trivial case which is actually quite common since the default
             * AbstractMathTransform.transform(DirectPosition) implementation
             * delegates to transform(double[],...).
             */
            return ASCENDING;
        }
        int delta = srcOff - dstOff;
        final int d;
        if (delta >= 0) {
            /*
             * Target coordinates are stored before source coordinates. If the target dimension
             * is not larger than the source dimension (so there is no way the target index can
             * catch up with the source index), then there is no need to use a temporary buffer
             * even in case of overlap.
             *
             * We can also avoid the buffer if the two subarrays do not actually overlap except
             * for the last point. If the offset of the last point in the source and target arrays
             * are 'srcLast' and 'dstLast' respectively then the condition is:
             *
             *               dstLast <= srcLast
             *      where    dstLast = dstOff + (numPts-1)*dstDim
             *               srcLast = srcOff + (numPts-1)*srcDim
             *
             * Rearanging gives: (srcOff - dstOff) >= (1-numPts)*(srcDim - dstDim)
             */
            d = srcDim - dstDim; // Must be computed in the same way than below.
            if (d >= 0 || delta >= (1-numPts)*d) {
                return ASCENDING;
            }
        } else {
            /*
             * Target coordinates will be stored after source coordinates. If the two subarrays
             * do not overlap, we still can use ASCENDING order (the DESCENDING order would do
             * the job as well, but we try to favor the simpler ascending order).
             */
            delta = -delta;
            if (delta >= numPts*srcDim) {
                return ASCENDING;
            }
            /*
             * Otherwise an iteration in DESCENDING order will avoid the need for a buffer if
             * the following relation hold (note: it is the same than the previous block with
             * a different reasoning):
             *
             *     os:    source offset     (srcOff)
             *     ot:    target offset     (dstOff)
             *     ds:    source dimension  (srcDim)
             *     dt:    target dimension  (dstDim)
             *     n :    number of points
             *     is:    any index in the range [0 ... n-1] for a source coordinate
             *     it:    any index in the range [0 ... n-1] for a target coordinate
             *
             * Condition:    ot + it*dt  >=  os + is*ds        at least for all it >= is.
             * Rearanging:   (ot - os)   >=  is*ds - it*dt     where (ot - os) is positive.
             *
             * If is = 0, then (ot - os) >= -it*dt. This relation always holds because
             * the left side is always positive in this block and the right hand side
             * always negative or zero (because it >= 0 and dt >= 1).
             *
             * if is = (n-1), then the condition (it >= is and it <= n-1) implies that
             * it = (n-1) as well. So (ot - os)  >=  (n-1)*(ds - dt).
             */
            d = srcDim - dstDim; // Must be computed in the same way than above.
            if (delta >= (numPts-1)*d) {
                return DESCENDING;
            }
        }
        /*
         * We have been unable to avoid the need for a buffer. Choose the smallest one.
         * If both dimensions are equal, we favor the buffering of source coordinates
         * in order to write as much target coordinates we can before an exception is
         * thrown, if the transform were to fail at some point.
         */
        return (d > 0) ? BUFFER_TARGET : BUFFER_SOURCE;
    }
}

/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2006-2012 Open Geospatial Consortium, Inc.
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
package org.opengis.filter.expression;

// Annotation
import org.opengis.annotation.Extension;


/**
 * Visitor with {@code visit} methods to be called by {@link Expression#accept Expression.accept(...)}.
 * <p>
 * Please note that a generic visit( Expression ) entry point has not been provided, although Expression
 * forms a heirarchy for your convience it is not an open heirarchy. If you need to extend this system
 * please make use of {code Function}, this will allow extention while remaining standards complient.
 * </p>
 * <p>
 * It is very common for a single instnace to implement both ExpressionVisitor and FilterVisitor.
 * </p>
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@Extension
public interface ExpressionVisitor {
    /**
     * Used to visit a Expression.NIL, also called for <code>null</code> where an
     * expression is expected.
     * <p>
     * This is particularly useful when doing data transformations, as an example when
     * using a StyleSymbolizer Expression.NIL can be used to represent the default
     * stroke color.
     * </p>
     * @param extraData
     * @return implementation specific
     */
    Object visit(NilExpression  expression, Object extraData);
    Object visit(Add            expression, Object extraData);
    Object visit(Divide         expression, Object extraData);
    Object visit(Function       expression, Object extraData);
    Object visit(Literal        expression, Object extraData);
    Object visit(Multiply       expression, Object extraData);
    Object visit(PropertyName   expression, Object extraData);
    Object visit(Subtract       expression, Object extraData);
}

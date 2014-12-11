/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2008-2012 Open Geospatial Consortium, Inc.
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
package org.opengis.style.portrayal;

import java.util.Collection;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

/**
 * <p>
 * External functions are used to perform computations that sometimes are needed to evaluate the query
 * statements and/or perform the portrayal rules.
 * </p>
 * 
 * <p>
 * There are no limitations to the operations it can perform or the return types it can have.<br>
 * External functions shall be modeled as operations, as described in ISO 19109.<br>
 * External functions shall not be used in the default portrayal specification.
 * </p>
 *  
 * @version <A HREF="http://www.isotc211.org">ISO 19117 Portrayal</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
@UML(identifier="PF_ExternalFunction", specification=ISO_19117)
public interface ExternalFunction {

    /**
     * Formal parameters for this function.
     * this is a immutable copy of the collection.
     * 
     * @return collection of AttributeDefinition
     */
    @UML(identifier="formalParameter", obligation=MANDATORY, specification=ISO_19117)
    Collection<AttributeDefinition> getParameters();
            
    /**
     * Returns the name of the function.
     * It shall contain no spaces and always start with a letter 
     * or and underscore character.
     * 
     * @return String
     */
    @UML(identifier="functionName", obligation=MANDATORY, specification=ISO_19117)
    String getName();
        
    /**
     * Returns the class type for this function.
     * 
     * @return Class
     */
    @UML(identifier="returnType", obligation=MANDATORY, specification=ISO_19117)
    Class getReturnType();
        
    /**
     * Returns a description of the function.
     * It is a human readable value.
     *  
     * @return InternationalString
     */
    @UML(identifier="description", obligation=OPTIONAL, specification=ISO_19117)
    InternationalString getDescription();
        
}

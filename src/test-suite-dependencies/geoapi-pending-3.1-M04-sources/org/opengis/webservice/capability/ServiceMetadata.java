//$Header$
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2004 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/exse/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth
 lat/lon Fitzke/Fretter/Poth GbR
 Meckenheimer Allee 176
 53115 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Jens Fitzke
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: jens.fitzke@uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.opengis.webservice.capability;

// J2SE dependencies
import java.net.URI;

// OpenGIS direct dependencies
import org.opengis.webservice.SimpleLink;


/**
 * Class representation of an <code>ows:Metadata</code> -Element as defined in
 * <code>owsOperationsMetadata.xsd</code> from the
 * <code>OWS Common Implementation
 * Specification 0.3</code>.
 * <p>
 * This element either references or contains more metadata about the element
 * that includes this element. Either at least one of the attributes in
 * xlink:simpleLink or a substitute for the _MetaData element shall be included,
 * but not both. An Implementation Specification can restrict the contents of
 * this element to always be a reference or always contain metadata.
 * (Informative: This element was adapted from the metaDataProperty element in
 * GML 3.0.)
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 */
public interface ServiceMetadata  {

    URI getAbout();

    /*
     * an ows:Metadata - Element has the same attributes as a SimpleLink
     */
    SimpleLink getLink();

    /*
     * ows:_MetaData - Element.
     *
     * @todo Check type.
     */
    Object getMetadata();
}

/*
 * $Header: /cvsroot/jaxen/jaxen/src/java/main/org/jaxen/expr/DefaultNameStep.java,v 1.23 2002/04/26 17:17:34 jstrachan Exp $
 * $Revision: 1.23 $
 * $Date: 2002/04/26 17:17:34 $
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions, and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions, and the disclaimer that follows 
 *    these conditions in the documentation and/or other materials 
 *    provided with the distribution.
 *
 * 3. The name "Jaxen" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact license@jaxen.org.
 * 
 * 4. Products derived from this software may not be called "Jaxen", nor
 *    may "Jaxen" appear in their name, without prior written permission
 *    from the Jaxen Project Management (pm@jaxen.org).
 * 
 * In addition, we request (but do not require) that you include in the 
 * end-user documentation provided with the redistribution and/or in the 
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *      Jaxen Project (http://www.jaxen.org/)."
 * Alternatively, the acknowledgment may be graphical using the logos 
 * available at http://www.jaxen.org/
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE Jaxen AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many 
 * individuals on behalf of the Jaxen Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * Jaxen Project, please see <http://www.jaxen.org/>.
 * 
 * $Id: DefaultNameStep.java,v 1.23 2002/04/26 17:17:34 jstrachan Exp $
 */



package org.jaxen.expr;

import org.jaxen.ContextSupport;
import org.jaxen.Navigator;

import org.jaxen.expr.iter.IterableAxis;


/** Expression object that represents any flavor
 *  of name-test steps within an XPath.
 *
 *  <p>
 *  This includes simple steps, such as "foo",
 *  non-default-axis steps, such as "following-sibling::foo"
 *  or "@foo", and namespace-aware steps, such
 *  as "foo:bar".
 *  </p>
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public class DefaultNameStep extends DefaultStep
{
    /** Our prefix, bound through the current Context.
     *  The empty-string ("") if no prefix was specified.
     *  Decidedly NOT-NULL, due to SAXPath constraints.
     */
    private String prefix;

    /** Our local-name.*/
    private String localName;

    /** Quick flag denoting if the localname was '*' */
    private boolean matchesAnyName;

    public DefaultNameStep(IterableAxis axis,
                           String prefix,
                           String localName)
    {
        super( axis );

        this.prefix              = prefix;
        this.localName           = localName;
        this.matchesAnyName      = "*".equals( localName );
    }

    public String getPrefix()
    {
        return this.prefix;
    }

    public String getLocalName()
    {
        return this.localName;
    }
    
    public boolean isMatchesAnyName() 
    {
        return matchesAnyName;
    }

    public String getText()
    {
        if ( ( getPrefix() != null )
             &&
             ( ! getPrefix().equals("") ) )
        {
            return getAxisName() + "::" + getPrefix() + ":" + getLocalName() + super.getText();
        }

        return getAxisName() 
            + "::" + getLocalName()
            + super.getText();
    }

    public String toString()
    {
        return "[(DefaultNameStep): " + getPrefix() + ":" + getLocalName() + "[" + super.toString() + "]]";
    }

    public boolean matches(Object node,
                           ContextSupport contextSupport)
    {
        //System.err.println( "DefaultNameStep.matches(" + node + ")" );

        Navigator nav  = contextSupport.getNavigator();

        String  myPrefix  = getPrefix();         
        String  myUri     = null;
        boolean hasPrefix = ( myPrefix != null ) && (! ( "".equals( myPrefix ) ) );

        String nodeUri  = null;
        String nodeName = null;

        if ( nav.isElement( node ) )
        {
            nodeUri  = nav.getElementNamespaceUri( node );
            nodeName = nav.getElementName( node );
        }
        else if ( nav.isAttribute( node ) )
        {
            nodeUri  = nav.getAttributeNamespaceUri( node );
            nodeName = nav.getAttributeName( node );
        }
        else if ( nav.isDocument( node ) )
        {
            return ( ! hasPrefix ) && matchesAnyName;
        }
        else if ( nav.isNamespace( node ) )
        {
            nodeUri = null;
            nodeName = nav.getNamespacePrefix( node );
        }
        else
        {
            // * will only match elements on most axis
            return false;
        }

        // System.out.println( "Matching nodeURI: " + nodeUri + " name: " + nodeName );
        
        
        if ( hasPrefix )
        {
            myUri = contextSupport.translateNamespacePrefixToUri( myPrefix );
        }
        else if ( matchesAnyName )
        {
            return true;
        }
        
        // If we have a prefix that does not map to no namespace,
        // but the node doesn't have *any* namespace-uri, then we fast-fail.
        
        if ( ( myUri != null   && !"".equals( myUri ) )
             &&
             ( nodeUri == null || "".equals( nodeUri ) ) )
        {
            return false;
        }
        
        // If we don't have a prefix, but the node does
        // have any namespace-uri, then we fast-fail.
        
        if ( ! hasPrefix
             &&
             ( nodeUri != null
               &&
               ! "".equals( nodeUri ) ) )
        {
                return false;
        }
             
        // To fail-fast, we check the equality of
        // local-names first.  Shorter strings compare
        // quicker.

        if ( getLocalName().equals( nodeName )
             ||
             matchesAnyName )
        {
            if ( ! hasPrefix )
            {
                return true;
            }

            return matchesNamespaceURIs( myUri,
                                         nodeUri );
        }
        
        return false;
    }
    
    /** @return true if the two namespace URIs are equal
     *   Note that we may wish to consider null being equal to ""
     */
    protected boolean matchesNamespaceURIs( String u1, String u2 ) {
        //System.out.println( "Comparing URI: " + u1 + " against URI: " + u2 );
        
        if ( u1 == u2 ) {
            return true;
        }
        if ( u1 == null ) 
        {
            u1 = "";
        }
        if ( u2 == null ) 
        {
            u2 = "";
        }
        return u1.equals( u2 );
    }
    
}

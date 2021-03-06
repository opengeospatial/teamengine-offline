/*
 * $Header: /cvsroot/jaxen/jaxen/src/java/main/org/jaxen/jdom/DocumentNavigator.java,v 1.18 2002/04/26 17:17:35 jstrachan Exp $
 * $Revision: 1.18 $
 * $Date: 2002/04/26 17:17:35 $
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
 * $Id: DocumentNavigator.java,v 1.18 2002/04/26 17:17:35 jstrachan Exp $
 */


package org.jaxen.jdom;

import org.jaxen.XPath;
import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;

import org.jaxen.util.SingleObjectIterator;

import org.saxpath.SAXPathException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Comment;
import org.jdom.Text;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.ProcessingInstruction;
import org.jdom.Namespace;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** Interface for navigating around the EXML object model.
 *
 *  <p>
 *  This class is not intended for direct usage, but is
 *  used by the Jaxen engine during evaluation.
 *  </p>
 *
 *  @see XPath
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class DocumentNavigator extends DefaultNavigator
{
    /** Singleton implementation.
     */
    private static class Singleton
    {
        /** Singleton instance.
         */
        private static DocumentNavigator instance = new DocumentNavigator();
    }

    public static DocumentNavigator getInstance()
    {
        return Singleton.instance;
    }

    public boolean isElement(Object obj)
    {
        return obj instanceof Element;
    }

    public boolean isComment(Object obj)
    {
        return obj instanceof Comment;
    }

    public boolean isText(Object obj)
    {
        return ( obj instanceof Text
                 ||
                 obj instanceof CDATA );
    }

    public boolean isAttribute(Object obj)
    {
        return obj instanceof Attribute;
    }

    public boolean isProcessingInstruction(Object obj)
    {
        return obj instanceof ProcessingInstruction;
    }

    public boolean isDocument(Object obj)
    {
        return obj instanceof Document;
    }

    public boolean isNamespace(Object obj)
    {
        return obj instanceof Namespace || obj instanceof XPathNamespace;
    }

    public String getElementName(Object obj)
    {
        Element elem = (Element) obj;

        return elem.getName();
    }

    public String getElementNamespaceUri(Object obj)
    {
        Element elem = (Element) obj;
        
        String uri = elem.getNamespaceURI();
        if ( uri != null && uri.length() == 0 ) 
            return null;
        else
            return uri;
    }

    public String getAttributeName(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getName();
    }

    public String getAttributeNamespaceUri(Object obj)
    {
        Attribute attr = (Attribute) obj;

        String uri = attr.getNamespaceURI();
        if ( uri != null && uri.length() == 0 ) 
            return null;
        else
            return uri;
    }

    public Iterator getChildAxisIterator(Object contextNode)
    {
        if ( contextNode instanceof Element )
        {
            return ((Element)contextNode).getContent().iterator();
        }
        else if ( contextNode instanceof Document )
        {
            return ((Document)contextNode).getContent().iterator();
        }

        return null;
    }

    public Iterator getNamespaceAxisIterator(Object contextNode)
    {
        if ( ! ( contextNode instanceof Element ) )
        {
            return null;
        }

        Element elem = (Element) contextNode;

        Map nsMap = new HashMap();

        Element current = elem;

        while ( current != null ) {
        
            Namespace ns = current.getNamespace();
            
            if ( ns != Namespace.NO_NAMESPACE ) {
                if ( !nsMap.containsKey(ns.getPrefix()) )
                    nsMap.put( ns.getPrefix(), new XPathNamespace(elem, ns) );
            }
        
            Iterator additional = current.getAdditionalNamespaces().iterator();

            while ( additional.hasNext() ) {

                ns = (Namespace)additional.next();
                if ( !nsMap.containsKey(ns.getPrefix()) )
                    nsMap.put( ns.getPrefix(), new XPathNamespace(elem, ns) );
            }

            current = current.getParent();
        }

        nsMap.put( "xml", new XPathNamespace(elem, Namespace.XML_NAMESPACE) );

        return nsMap.values().iterator();
    }

    public Iterator getParentAxisIterator(Object contextNode)
    {
        Object parent = null;

        if ( contextNode instanceof Document )
        {
            parent = contextNode;
        }
        else if ( contextNode instanceof Element )
        {
            parent = ((Element)contextNode).getParent();

            if ( parent == null )
            {
                if ( ((Element)contextNode).isRootElement() )
                {
                    parent = ((Element)contextNode).getDocument();
                }
            }
        }
        else if ( contextNode instanceof Attribute )
        {
            parent = ((Attribute)contextNode).getParent();
        }
        else if ( contextNode instanceof XPathNamespace )
        {
            parent = ((XPathNamespace)contextNode).getJDOMElement();
        }
        else if ( contextNode instanceof ProcessingInstruction )
        {
            parent = ((ProcessingInstruction)contextNode).getParent();
        }
        else if ( contextNode instanceof Comment )
        {
            parent = ((Comment)contextNode).getParent();
        }
        else if ( contextNode instanceof Text )
        {
            parent = ((Text)contextNode).getParent();
        }
        
        if ( parent != null )
        {
            return new SingleObjectIterator( parent );
        }

        return null;
    }

    public Iterator getAttributeAxisIterator(Object contextNode)
    {
        if ( ! ( contextNode instanceof Element ) )
        {
            return null;
        }

        Element elem = (Element) contextNode;

        return elem.getAttributes().iterator();
    }

    /** Returns a parsed form of the given xpath string, which will be suitable
     *  for queries on JDOM documents.
     */
    public XPath parseXPath (String xpath) throws SAXPathException
    {
        return new JDOMXPath(xpath);
    }

    public Object getDocumentNode(Object contextNode)
    {
        if ( contextNode instanceof Document )
        {
            return contextNode;
        }

        Element elem = (Element) contextNode;

        return elem.getDocument();
    }

    public String getElementQName(Object obj)
    {
        Element elem = (Element) obj;

        String prefix = elem.getNamespacePrefix();

        if ( prefix == null || "".equals( prefix ) )
        {
            return elem.getName();
        }

        return prefix + ":" + elem.getName();
    }

    public String getAttributeQName(Object obj)
    {
        Attribute attr = (Attribute) obj;

        String prefix = attr.getNamespacePrefix();

        if ( prefix == null || "".equals( prefix ) )
        {
            return attr.getName();
        }

        return prefix + ":" + attr.getName();
    }

    public String getNamespaceStringValue(Object obj)
    {
        if (obj instanceof Namespace) {

            Namespace ns = (Namespace) obj;
            return ns.getURI();
        } else {

            XPathNamespace ns = (XPathNamespace) obj;
            return ns.getJDOMNamespace().getURI();
        }
        
    }

    public String getNamespacePrefix(Object obj)
    {
        if (obj instanceof Namespace) {

            Namespace ns = (Namespace) obj;
            return ns.getPrefix();
        } else {

            XPathNamespace ns = (XPathNamespace) obj;
            return ns.getJDOMNamespace().getPrefix();
        }
    }

    public String getTextStringValue(Object obj)
    {
        if ( obj instanceof Text )
        {
            return ((Text)obj).getText();
        }

        if ( obj instanceof CDATA )
        {
            return ((CDATA)obj).getText();
        }

        return "";
    }

    public String getAttributeStringValue(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getValue();
    }

    public String getElementStringValue(Object obj)
    {
        Element elem = (Element) obj;

        StringBuffer buf = new StringBuffer();

        List     content     = elem.getContent();
        Iterator contentIter = content.iterator();
        Object   each        = null;

        while ( contentIter.hasNext() )
        {
            each = contentIter.next();

            if ( each instanceof Text )
            {
                buf.append( ((Text)each).getText() );
            }
            else if ( each instanceof CDATA )
            {
                buf.append( ((CDATA)each).getText() );
            }
            else if ( each instanceof Element )
            {
                buf.append( getElementStringValue( each ) );
            }
        }

        return buf.toString();
    }

    public String getProcessingInstructionTarget(Object obj)
    {
        ProcessingInstruction pi = (ProcessingInstruction) obj;

        return pi.getTarget();
    }

    public String getProcessingInstructionData(Object obj)
    {
        ProcessingInstruction pi = (ProcessingInstruction) obj;

        return pi.getData();
    }

    public String getCommentStringValue(Object obj)
    {
        Comment cmt = (Comment) obj;

        return cmt.getText();
    }

    public String translateNamespacePrefixToUri(String prefix, Object context)
    {
        Element element = null;
        if ( context instanceof Element ) 
        {
            element = (Element) context;
        }
        else if ( context instanceof Text )
        {
            element = ((Text)context).getParent();
        }
        else if ( context instanceof Attribute )
        {
            element = ((Attribute)context).getParent();
        }
        else if ( context instanceof XPathNamespace )
        {
            element = ((XPathNamespace)context).getJDOMElement();
        }
        else if ( context instanceof Comment )
        {
            element = ((Comment)context).getParent();
        }
        else if ( context instanceof ProcessingInstruction )
        {
            element = ((ProcessingInstruction)context).getParent();
        }

        if ( element != null )
        {
            Namespace namespace = element.getNamespace( prefix );

            if ( namespace != null ) 
            {
                return namespace.getURI();
            }
        }
        return null;
    }

    public Object getDocument(String url) throws FunctionCallException
    {
        try
        {
            SAXBuilder builder = new SAXBuilder();
            
            return builder.build( url );
        }
        catch (Exception e)
        {
            throw new FunctionCallException( e.getMessage() );
        }
    }
}

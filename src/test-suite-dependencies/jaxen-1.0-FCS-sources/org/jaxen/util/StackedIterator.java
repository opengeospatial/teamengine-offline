/*
 * $Header: /cvsroot/jaxen/jaxen/src/java/main/org/jaxen/util/StackedIterator.java,v 1.5 2002/04/26 17:17:36 jstrachan Exp $
 * $Revision: 1.5 $
 * $Date: 2002/04/26 17:17:36 $
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
 * $Id: StackedIterator.java,v 1.5 2002/04/26 17:17:36 jstrachan Exp $
 */



package org.jaxen.util;

import org.jaxen.Navigator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import java.util.LinkedList;

import java.util.Set;
import java.util.HashSet;

public abstract class StackedIterator implements Iterator
{
    private Object     contextNode;
    private LinkedList iteratorStack;
    private Navigator  navigator;

    private Set        created;

    public StackedIterator(Object contextNode,
                           Navigator navigator)
    {
        this.iteratorStack = new LinkedList();
        this.created       = new HashSet();

        init( contextNode,
              navigator );
    }

    protected StackedIterator()
    {
        this.iteratorStack = new LinkedList();
        this.created       = new HashSet();
    }

    protected void init(Object contextNode,
                        Navigator navigator)
    {
        this.contextNode   = contextNode;
        this.navigator     = navigator;
        
        pushIterator( internalCreateIterator( contextNode ) );
    }

    private Iterator internalCreateIterator(Object contextNode)
    {
        if ( this.created.contains( contextNode ) )
        {
            return null;
        }

        this.created.add( contextNode );

        return createIterator( contextNode );
    }

    public boolean hasNext()
    {
        Iterator curIter = currentIterator();

        if ( curIter == null )
        {
            return false;
        }

        return curIter.hasNext();
    }

    public Object next() throws NoSuchElementException
    {
        if ( ! hasNext() )
        {
            throw new NoSuchElementException();
        }

        Iterator curIter = currentIterator();
        Object   object  = curIter.next();

        pushIterator( internalCreateIterator( object ) );

        return object;
    }

    public void remove() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

    abstract protected Iterator createIterator(Object contextNode);

    protected void pushIterator(Iterator iter)
    {
        if ( iter != null )
        {
            this.iteratorStack.addLast( iter );
        }
    }

    private Iterator currentIterator()
    {
        while ( iteratorStack.size() > 0 )
        {
            Iterator curIter = (Iterator) iteratorStack.getFirst();

            if ( curIter.hasNext() )
            {
                return curIter;
            }

            iteratorStack.removeFirst();
        }

        return null;
    }

    protected Navigator getNavigator()
    {
        return this.navigator;
    }
}

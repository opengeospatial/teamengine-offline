package com.galdosinc.glib.xml.dom;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DomChildAccess {
    public static StringBuffer getElementTextContent(Element elem) {
        StringBuffer content = new StringBuffer();
        Node childNode = elem.getFirstChild();
        while ((childNode != null) && (childNode.getNodeType() == 3)) {
            content.append(childNode.getNodeValue());
            childNode = childNode.getNextSibling();
        }
        return content;
    }

    public static List getChildElementsByTagName(Node parentNode, String nsUri,
            String localName) {
        LinkedList childElements = new LinkedList();
        Iterator childIter = getChildElementIterator(parentNode, nsUri,
                localName);
        while (childIter.hasNext()) {
            childElements.addLast(childIter.next());
        }
        return childElements;
    }

    public static List getChildElements(Node parentNode) {
        LinkedList childElements = new LinkedList();
        Iterator childIter = getChildElementIterator(parentNode);
        while (childIter.hasNext()) {
            childElements.addLast(childIter.next());
        }
        return childElements;
    }

    public static int getChildElemCount(Node parentNode, String nsUri,
            String localName) {
        int count = 0;
        Iterator childIter = getChildElementIterator(parentNode, nsUri,
                localName);
        while (childIter.next() != null) {
            count++;
        }
        return count;
    }

    public static int getChildElemCount(Node parentNode) {
        int count = 0;
        Iterator childIter = getChildElementIterator(parentNode);
        while (childIter.next() != null) {
            count++;
        }
        return count;
    }

    public static Element getFirstChildElement(Node parentNode) {
        Node childNode = parentNode.getFirstChild();
        while ((childNode != null) && (childNode.getNodeType() != 1)) {
            childNode = childNode.getNextSibling();
        }
        return (Element) childNode;
    }

    public static Element getFirstChildElement(Node parentNode, String nsUri,
            String localName) {
        Node childNode = parentNode.getFirstChild();
        while (childNode != null) {
            if (childNode.getNodeType() == 1) {
                String nodeNsUri = childNode.getNamespaceURI();
                if (childNode.getLocalName().equals(localName)) {
                    if ((nodeNsUri != null) && (nsUri != null)
                            && (nodeNsUri.equals(nsUri)))
                        break;
                    if ((nodeNsUri == null) && (nsUri == null)) {
                        break;
                    }
                }
            }
            childNode = childNode.getNextSibling();
        }
        return (Element) childNode;
    }

    public static int removeAllChildNodes(Node parentNode) {
        int removedNodeCount = 0;
        Node childNode = parentNode.getFirstChild();
        while (childNode != null) {
            Node nodeToDelete = childNode;
            childNode = childNode.getNextSibling();
            parentNode.removeChild(nodeToDelete);
            removedNodeCount++;
        }
        return removedNodeCount;
    }

    public static Iterator getChildElementIterator(Node parentNode,
            String elemNsUri, String elemLocalName) {
        return new Iterator() {
            private Node cursor_;
            private String elemLocalName_;
            private String elemNsUri_;
            private Element nextElem_;
            private boolean hasNext_;
            private boolean hasNextIsComputed_;

            public boolean hasNext() {
                if (this.hasNextIsComputed_) {
                    return this.hasNext_;
                }

                this.hasNext_ = false;
                this.nextElem_ = null;
                while ((!this.hasNext_) && (this.cursor_ != null)) {
                    if ((this.cursor_.getNodeType() == 1)
                            && (this.cursor_.getLocalName()
                                    .equals(this.elemLocalName_))) {
                        String currentElemNsUri = this.cursor_
                                .getNamespaceURI();
                        if (((currentElemNsUri == null) && (this.elemNsUri_ == null))
                                || ((currentElemNsUri != null)
                                        && (this.elemNsUri_ != null) && (currentElemNsUri
                                            .equals(this.elemNsUri_)))) {
                            this.hasNext_ = true;
                            this.nextElem_ = ((Element) this.cursor_);
                        }
                    }
                    this.cursor_ = this.cursor_.getNextSibling();
                }

                this.hasNextIsComputed_ = true;
                return this.hasNext_;
            }

            public Object next() {
                hasNext();
                this.hasNextIsComputed_ = false;
                return this.nextElem_;
            }

            public void remove() {
                throw new NoSuchMethodError(
                        "Iterator.remove not supported here.");
            }

            private Node moveCursorToFirstElement(Node parentNode) {
                Node cursor = parentNode.getFirstChild();
                while ((cursor != null) && (cursor.getNodeType() != 1)) {
                    cursor = cursor.getNextSibling();
                }
                return cursor;
            }
        };
    }

    public static Iterator getChildElementIterator(final Node parentNode) {
        return new Iterator() {
            private Node cursor_ = moveCursorToFirstElement(parentNode);

            public boolean hasNext() {
                return this.cursor_ != null;
            }

            public Object next() {
                if (!hasNext()) {
                    return null;
                }

                Node nextNode = this.cursor_;
                do {
                    this.cursor_ = this.cursor_.getNextSibling();
                } while ((this.cursor_ != null)
                        && (this.cursor_.getNodeType() != 1));
                return nextNode;
            }

            public void remove() {
                throw new NoSuchMethodError(
                        "Iterator.remove not supported here.");
            }

            private Node moveCursorToFirstElement(Node parentNode) {
                Node cursor = parentNode.getFirstChild();
                while ((cursor != null) && (cursor.getNodeType() != 1)) {
                    cursor = cursor.getNextSibling();
                }
                return cursor;
            }
        };
    }
}
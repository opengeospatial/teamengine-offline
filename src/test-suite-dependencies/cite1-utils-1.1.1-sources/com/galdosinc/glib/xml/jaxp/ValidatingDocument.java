package com.galdosinc.glib.xml.jaxp;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

public class ValidatingDocument implements Document {
    public DocumentType getDoctype() {
        return null;
    }

    public DOMImplementation getImplementation() {
        return null;
    }

    public Element getDocumentElement() {
        return null;
    }

    public Element createElement(String arg0) throws DOMException {
        return null;
    }

    public DocumentFragment createDocumentFragment() {
        return null;
    }

    public Text createTextNode(String arg0) {
        return null;
    }

    public Comment createComment(String arg0) {
        return null;
    }

    public CDATASection createCDATASection(String arg0) throws DOMException {
        return null;
    }

    public ProcessingInstruction createProcessingInstruction(String arg0,
            String arg1) throws DOMException {
        return null;
    }

    public Attr createAttribute(String arg0) throws DOMException {
        return null;
    }

    public EntityReference createEntityReference(String arg0)
            throws DOMException {
        return null;
    }

    public NodeList getElementsByTagName(String arg0) {
        return null;
    }

    public Node importNode(Node arg0, boolean arg1) throws DOMException {
        return null;
    }

    public Element createElementNS(String arg0, String arg1)
            throws DOMException {
        return null;
    }

    public Attr createAttributeNS(String arg0, String arg1) throws DOMException {
        return null;
    }

    public NodeList getElementsByTagNameNS(String arg0, String arg1) {
        return null;
    }

    public Element getElementById(String arg0) {
        return null;
    }

    public String getNodeName() {
        return null;
    }

    public String getNodeValue() throws DOMException {
        return null;
    }

    public void setNodeValue(String arg0) throws DOMException {
    }

    public short getNodeType() {
        return 0;
    }

    public Node getParentNode() {
        return null;
    }

    public NodeList getChildNodes() {
        return null;
    }

    public Node getFirstChild() {
        return null;
    }

    public Node getLastChild() {
        return null;
    }

    public Node getPreviousSibling() {
        return null;
    }

    public Node getNextSibling() {
        return null;
    }

    public NamedNodeMap getAttributes() {
        return null;
    }

    public Document getOwnerDocument() {
        return null;
    }

    public Node insertBefore(Node arg0, Node arg1) throws DOMException {
        return null;
    }

    public Node replaceChild(Node arg0, Node arg1) throws DOMException {
        return null;
    }

    public Node removeChild(Node arg0) throws DOMException {
        return null;
    }

    public Node appendChild(Node arg0) throws DOMException {
        return null;
    }

    public boolean hasChildNodes() {
        return false;
    }

    public Node cloneNode(boolean arg0) {
        return null;
    }

    public void normalize() {
    }

    public boolean isSupported(String arg0, String arg1) {
        return false;
    }

    public String getNamespaceURI() {
        return null;
    }

    public String getPrefix() {
        return null;
    }

    public void setPrefix(String arg0) throws DOMException {
    }

    public String getLocalName() {
        return null;
    }

    public boolean hasAttributes() {
        return false;
    }

    @Override
    public String getBaseURI() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getTextContent() throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTextContent(String textContent) throws DOMException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSameNode(Node other) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String lookupPrefix(String namespaceURI) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEqualNode(Node arg) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object getFeature(String feature, String version) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getUserData(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getInputEncoding() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getXmlEncoding() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getXmlStandalone() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        // TODO Auto-generated method stub

    }

    @Override
    public String getXmlVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setXmlVersion(String xmlVersion) throws DOMException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean getStrictErrorChecking() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setStrictErrorChecking(boolean strictErrorChecking) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDocumentURI() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDocumentURI(String documentURI) {
        // TODO Auto-generated method stub

    }

    @Override
    public Node adoptNode(Node source) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DOMConfiguration getDomConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void normalizeDocument() {
        // TODO Auto-generated method stub

    }

    @Override
    public Node renameNode(Node n, String namespaceURI, String qualifiedName)
            throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }
}
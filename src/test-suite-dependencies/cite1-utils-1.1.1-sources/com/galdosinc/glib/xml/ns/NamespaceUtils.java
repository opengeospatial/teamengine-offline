package com.galdosinc.glib.xml.ns;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class NamespaceUtils
{
  public static String findPertinentNsUri(Element lowestDescendant, String prefix)
  {
    Element currentElem = lowestDescendant;
    String attrLocalName = prefix == null ? "xmlns" : prefix;
    while (currentElem != null) {
      Attr attrNode = currentElem.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", attrLocalName);
      if (attrNode != null) {
        return attrNode.getValue();
      }
      Node parent = currentElem.getParentNode();
      if (parent.getNodeType() != 1)
      {
        break;
      }
      currentElem = (Element)parent;
    }
    return null;
  }
}
package com.galdosinc.glib.xml;

public class QName
{
  private String namespaceUri;
  private String localName;

  public QName()
  {
  }

  public QName(String namespaceUri, String localName)
  {
    this.namespaceUri = namespaceUri;
    this.localName = localName;
  }

  public int hashCode() {
    return this.namespaceUri.hashCode() >> 4 + this.localName.hashCode();
  }

  public boolean equals(Object other) {
    if ((other instanceof QName)) {
      QName qName = (QName)other;
      if ((qName.localName.equals(this.localName)) && (
        (this.namespaceUri == qName.namespaceUri) || (
        (this.namespaceUri != null) && 
        (qName.namespaceUri != null) && 
        (this.namespaceUri.equals(qName.getNamespaceUri()))))) {
        return true;
      }
    }

    return false;
  }

  public String toString() {
    if (this.namespaceUri == null) {
      return this.localName;
    }
    return this.namespaceUri + ":" + this.localName;
  }

  public String getLocalName()
  {
    return this.localName;
  }

  public String getNamespaceUri()
  {
    return this.namespaceUri;
  }

  public void setLocalName(String localName)
  {
    this.localName = localName;
  }

  public void setNamespaceUri(String namespaceUri)
  {
    this.namespaceUri = namespaceUri;
  }
}
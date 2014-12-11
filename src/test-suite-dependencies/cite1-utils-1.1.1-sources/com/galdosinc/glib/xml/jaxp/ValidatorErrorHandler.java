package com.galdosinc.glib.xml.jaxp;

import java.util.Iterator;
import java.util.LinkedList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ValidatorErrorHandler extends DefaultHandler
{
  private boolean docValid_ = true;
  private ErrorHandler parallelErrorHandler_;
  private LinkedList messages_;

  public ValidatorErrorHandler()
  {
    this.messages_ = new LinkedList();
  }

  public void setParallelErrorHandler(ErrorHandler parallelErrorHandler) {
    this.parallelErrorHandler_ = parallelErrorHandler;
  }

  public void fatalError(SAXParseException spe) throws SAXException {
    this.docValid_ = false;
    addMessage(spe);

    super.fatalError(spe);
    if (this.parallelErrorHandler_ != null)
      this.parallelErrorHandler_.fatalError(spe);
  }

  public void error(SAXParseException spe) throws SAXException
  {
    this.docValid_ = false;
    addMessage(spe);
    super.error(spe);
    if (this.parallelErrorHandler_ != null)
      this.parallelErrorHandler_.error(spe);
  }

  public void warning(SAXParseException spe) throws SAXException
  {
    addMessage(spe);
    super.warning(spe);

    if (this.parallelErrorHandler_ != null)
      this.parallelErrorHandler_.warning(spe);
  }

  public boolean wasValidDocument()
  {
    return this.docValid_;
  }

  protected void addMessage(SAXParseException spe) {
    String speMsg = spe.getMessage();
    StringBuffer strBuffer = new StringBuffer(speMsg.length() + 40);
    strBuffer.append("Line = ");
    strBuffer.append(spe.getLineNumber());
    strBuffer.append(" Column = ");
    strBuffer.append(spe.getColumnNumber());
    strBuffer.append(" Message = {");
    strBuffer.append(speMsg);
    strBuffer.append("}");
    this.messages_.add(strBuffer.toString());
  }

  public Iterator getMessages() {
    return this.messages_.iterator();
  }
}
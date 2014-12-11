package com.galdosinc.glib.xml.jaxp;

import com.galdosinc.glib.gml.schema.GrammarUtil;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.parsers.XMLGrammarPreparser;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

public class ValidatingDocumentBuilderFactory extends DocumentBuilderFactoryImpl
{
  private static final int BIG_PRIME = 2039;
  private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  private static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
  private static final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";
  private static final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";
  private static final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";
  private static final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";
  private static final String EXTERNAL_SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
  private static final XMLGrammarPreparser grammerParser = new XMLGrammarPreparser();
  private XMLGrammarPool grammarPool = new XMLGrammarPoolImpl();
  private Map dtds = new HashMap();

  static {
    grammerParser.registerPreparser("http://www.w3.org/2001/XMLSchema", null);
    grammerParser.registerPreparser("http://www.w3.org/TR/REC-xml", null);
  }

  public ValidatingDocumentBuilderFactory() throws ParserConfigurationException {
    setNamespaceAware(true);
    setValidating(true);
    setAttribute("http://apache.org/xml/properties/internal/grammar-pool", this.grammarPool);
    setAttribute("http://apache.org/xml/properties/internal/symbol-table", new SymbolTable(2039));

    setAttribute("http://xml.org/sax/features/validation", Boolean.TRUE);
    setAttribute("http://apache.org/xml/features/validation/schema", Boolean.TRUE);
    setAttribute("http://apache.org/xml/features/validation/schema-full-checking", Boolean.FALSE);
  }

  public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
    return new ValidatingDocumentBuilder(super.newDocumentBuilder(), new ValidatorErrorHandler(), this.dtds);
  }

  public void setAttribute(String name, Object value) throws IllegalArgumentException {
    if (name.equals("http://www.galdosinc.com/xml/properties/schemaLocation"))
    {
      try {
        this.grammarPool.cacheGrammars("XmlSchema", new Grammar[] { GrammarUtil.parseGrammar(new URL(value.toString()), this.grammarPool) });
      } catch (IOException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    } else if (name.startsWith("http://www.galdosinc.com/xml/properties/dtdLocation/")) {
      String rootName = name.substring("http://www.galdosinc.com/xml/properties/dtdLocation/".length());
      this.dtds.put(rootName, value.toString());
    } else {
      super.setAttribute(name, value);
    }
  }
}
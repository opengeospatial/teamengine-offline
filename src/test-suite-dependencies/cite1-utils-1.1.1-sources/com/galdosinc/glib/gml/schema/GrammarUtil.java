package com.galdosinc.glib.gml.schema;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import org.apache.xerces.parsers.XMLGrammarPreparser;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;

public class GrammarUtil {
    public static XMLGrammarPreparser getGrammarParser() {
        XMLGrammarPreparser grammerParser = new XMLGrammarPreparser();
        grammerParser.registerPreparser("http://www.w3.org/2001/XMLSchema",
                null);
        grammerParser.registerPreparser("http://www.w3.org/TR/REC-xml", null);
        return grammerParser;
    }

    public static Grammar parseGrammar(URL grammarUrl) throws IOException {
        return parseGrammar(grammarUrl, null);
    }

    public static Grammar parseGrammar(URL grammarUrl,
            XMLGrammarPool grammarPool) throws IOException {
        return parseGrammar(grammarUrl, grammarPool, null);
    }

    public static Grammar parseGrammar(URL grammarUrl,
            XMLGrammarPool grammarPool, XMLErrorHandler errorHandler)
            throws IOException {
        return parseGrammar(grammarUrl.toExternalForm(), new InputStreamReader(
                grammarUrl.openStream()), grammarPool, errorHandler);
    }

    public static Grammar parseGrammar(File grammarFile) throws IOException {
        return parseGrammar(grammarFile, null);
    }

    public static Grammar parseGrammar(File grammarFile,
            XMLGrammarPool grammarPool) throws IOException {
        return parseGrammar(grammarFile, grammarPool, null);
    }

    public static Grammar parseGrammar(File grammarFile,
            XMLGrammarPool grammarPool, XMLErrorHandler errorHandler)
            throws IOException {
        return parseGrammar(grammarFile.getAbsolutePath(), new FileReader(
                grammarFile), grammarPool, errorHandler);
    }

    public static Grammar parseGrammar(String baseUri, String grammarText)
            throws IOException {
        return parseGrammar(baseUri, grammarText, null);
    }

    public static Grammar parseGrammar(String baseUri, String grammarText,
            XMLGrammarPool grammarPool) throws IOException {
        return parseGrammar(baseUri, grammarText, grammarPool);
    }

    public static Grammar parseGrammar(String baseUri, String grammarText,
            XMLGrammarPool grammarPool, XMLErrorHandler errorHandler)
            throws IOException {
        return parseGrammar(baseUri, new StringReader(grammarText),
                grammarPool, errorHandler);
    }

    public static Grammar parseGrammar(String baseUri, Reader in,
            XMLGrammarPool grammarPool, XMLErrorHandler errorHandler)
            throws IOException {
        String encoding = null;
        XMLGrammarPreparser grammerParser = getGrammarParser();
        if (grammarPool != null) {
            grammerParser.setGrammarPool(grammarPool);
        }
        if (errorHandler != null) {
            grammerParser.setErrorHandler(errorHandler);
        }

        grammerParser.setEntityResolver(getXmlEntityResolver(encoding));
        return grammerParser.preparseGrammar(
                "http://www.w3.org/2001/XMLSchema", new XMLInputSource(baseUri,
                        baseUri, baseUri, in, encoding));
    }

    private static XMLEntityResolver getXmlEntityResolver(String encoding) {
        return new XMLEntityResolver() {
            public XMLInputSource resolveEntity(XMLResourceIdentifier arg)
                    throws XNIException, IOException {
                URL url = new URL(arg.getExpandedSystemId());
                return new XMLInputSource(arg.getPublicId(),
                        arg.getExpandedSystemId(), arg.getExpandedSystemId(),
                        url.openStream(), "UTF-8");
            }
        };
    }
}
package com.galdosinc.glib.gml.validator;

import com.galdosinc.glib.xml.jaxp.DomErrorHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class GmlInstanceValidator {
    public static final String DOC_BUILD_FACT_GALDOS = "com.galdosinc.glib.xml.jaxp.ValidatingDocumentBuilderFactory";
    public static final String PROP_DOC_BUILD_FACT = "javax.xml.parsers.DocumentBuilderFactory";
    private DocumentBuilderFactory docBuilderFactory;

    public GmlInstanceValidator() {
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "com.galdosinc.glib.xml.jaxp.ValidatingDocumentBuilderFactory");
        this.docBuilderFactory = DocumentBuilderFactory.newInstance();

        this.docBuilderFactory.setAttribute(
                "http://apache.org/xml/features/dom/defer-node-expansion",
                Boolean.FALSE);
        this.docBuilderFactory.setValidating(true);
        this.docBuilderFactory.setNamespaceAware(true);
    }

    public Document validateInstanceByURL(String instanceURL) throws Exception {
        Document result = null;
        try {
            DocumentBuilder docBuilder = this.docBuilderFactory
                    .newDocumentBuilder();
            DomErrorHandler errorHandler = new DomErrorHandler();
            docBuilder.setErrorHandler(errorHandler);
            docBuilder.parse(instanceURL);
            result = errorHandler.toDomDocument();
        } catch (SAXParseException localSAXParseException) {
        } catch (SAXException se) {
            System.out.println("GmlInstanceValidator.validateInstanceByURL()> "
                    + se.getMessage());
        } catch (ParserConfigurationException pce) {
            System.out.println("GmlInstanceValidator.validateInstanceByURL()> "
                    + pce.getMessage());
        } catch (MalformedURLException mue) {
            throw mue;
        } catch (IOException ioe) {
            throw ioe;
        }
        return result;
    }

    public Document validateInstance(String instance) {
        Document result = null;
        try {
            DocumentBuilder docBuilder = this.docBuilderFactory
                    .newDocumentBuilder();
            DomErrorHandler errorHandler = new DomErrorHandler();
            docBuilder.setErrorHandler(errorHandler);
            Document built;
            try {
                built = docBuilder.parse(new ByteArrayInputStream(instance
                        .getBytes()));
            } catch (SAXParseException localSAXParseException) {
            }
            result = errorHandler.toDomDocument();
        } catch (Exception e) {
            System.out.println("GmlInstanceValidator.validateInstance()> "
                    + e.getMessage());
        }
        return result;
    }
}
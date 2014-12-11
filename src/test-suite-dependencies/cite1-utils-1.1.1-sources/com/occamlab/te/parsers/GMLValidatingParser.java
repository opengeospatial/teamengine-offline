/****************************************************************************

  The contents of this file are subject to the Mozilla Public License
  Version 1.1 (the "License"); you may not use this file except in
  compliance with the License. You may obtain a copy of the License at
  http://www.mozilla.org/MPL/ 

  Software distributed under the License is distributed on an "AS IS" basis,
  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
  the specific language governing rights and limitations under the License. 

  The Original Code is TEAM Engine.

  The Initial Developer of the Original Code is Northrop Grumman Corporation
  jointly with The National Technology Alliance.  Portions created by
  Northrop Grumman Corporation are Copyright (C) 2005-2006, Northrop
  Grumman Corporation. All Rights Reserved.

  Contributor(s): No additional contributors to date

 ****************************************************************************/
package com.occamlab.te.parsers;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import org.w3c.dom.*;
import com.occamlab.te.ErrorHandlerImpl;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class GMLValidatingParser {
    private static final Logger LOGR = Logger
            .getLogger(GMLValidatingParser.class.getName());
    DocumentBuilderFactory DBF;
    final String builder_property = "javax.xml.parsers.DocumentBuilderFactory";
    final String config_property = "org.apache.xerces.xni.parser.XMLParserConfiguration";

    public GMLValidatingParser(Document document_locations) throws Throwable {
        String old_config_property = System.getProperty(config_property);
        System.clearProperty(config_property);

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> dbf_class = loader
                    .loadClass("com.galdosinc.glib.xml.jaxp.ValidatingDocumentBuilderFactory");
            DBF = (DocumentBuilderFactory) dbf_class.newInstance();
            DBF.setNamespaceAware(true);
            DBF.setValidating(true);
            NodeList parms = document_locations.getElementsByTagName("parm");
            for (int i = 0; i < parms.getLength(); i++) {
                Element parm = (Element) parms.item(i);
                NodeList parm_contents = parm.getChildNodes();
                String property_name = null;
                URL schemaURL = null;
                for (int j = 0; j < parm_contents.getLength(); j++) {
                    Node n = (Node) parm_contents.item(j);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) n;
                        if (e.getNodeName().equals("name")) {
                            property_name = e.getTextContent();
                        }
                        if (e.getNodeName().equals("value")) {
                            schemaURL = schemaRefAsURL(e.getTextContent()
                                    .trim());
                        }
                    }
                }
                if (null != schemaURL) {
                    DBF.setAttribute(property_name, schemaURL);
                }
            }
        } catch (Exception e) {
            LOGR.warning(e.getMessage());
            throw e;
        }

        if (old_config_property != null) {
            System.setProperty(config_property, old_config_property);
        }
    }

    /**
     * Creates a URL from the given schema reference. If the reference has no
     * scheme component it is assumed to be a classpath reference.
     * 
     * @param schemaRef
     *            A String representing either an absolute URL value or an
     *            absolute classpath reference.
     * @return A URL indicating the location of the schema, or {@code null} if
     *         it cannot be found (on the classpath).
     */
    URL schemaRefAsURL(String schemaRef) {
        URL schemaURL = null;
        try {
            schemaURL = new URL(schemaRef);
        } catch (MalformedURLException mux) {
            ClassLoader loader = getClass().getClassLoader();
            schemaURL = loader.getResource(schemaRef);
        }
        if (null == schemaURL) {
            LOGR.warning("Unable to find schema resource at " + schemaRef);
        }
        return schemaURL;
    }

    public Document parse(URLConnection uc, Element instruction,
            PrintWriter logger) throws Exception {
        Document doc = null;
        ErrorHandlerImpl eh = new ErrorHandlerImpl(null, logger);

        String old_config_property = System.getProperty(config_property);
        System.clearProperty(config_property);

        try {
            DocumentBuilder db = DBF.newDocumentBuilder();
            db.setErrorHandler(eh);
            doc = db.parse(uc.getInputStream());
        } catch (Exception e) {
            logger.println(e.getMessage());
        }

        if (old_config_property != null) {
            System.setProperty(config_property, old_config_property);
        }

        if (eh.getErrorCount() > 0 || eh.getWarningCount() > 0) {
            logger.println(eh.getErrorCounts());
            logger.flush();
        }

        if (eh.getErrorCount() > 0) {
            return null;
        }

        return doc;
    }

    public static void main(String[] args) throws Throwable {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(args[0]);
        GMLValidatingParser gvp = new GMLValidatingParser(doc);
        URLConnection uc = new java.net.URL(args[1]).openConnection();
        gvp.parse(uc, doc.getDocumentElement(), new PrintWriter(System.out));
    }
}

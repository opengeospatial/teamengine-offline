package com.galdosinc.glib.gml.validator;

import com.galdosinc.glib.gml.schema.DomSchemaErrorHandler;
import com.galdosinc.glib.gml.schema.GmlSchemaParser;
import com.galdosinc.glib.xml.QName;
import com.galdosinc.glib.xml.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.NamespaceContext;
import org.w3c.dom.Element;

public class XPathGmlSchemaValidator implements Function {
    public Object call(Context context, List argumentList)
            throws FunctionCallException {
        if (argumentList.size() < 2) {
            throw new FunctionCallException(
                    "The function must have at least the schema and a base uri as an argument");
        }
        Iterator arguments = argumentList.iterator();

        String baseUri = arguments.next().toString();

        Element schemaElement = getSchemaElement(arguments.next());
        String schemaText = XmlUtils.xml2str(schemaElement);

        List featureTypeNames = new ArrayList();
        while (arguments.hasNext()) {
            String featureTypeName = arguments.next().toString();
            featureTypeNames.add(getQName(context.getContextSupport()
                    .getNamespaceContext(), featureTypeName));
        }

        return execute(baseUri, schemaText, featureTypeNames);
    }

    public Object execute(String baseUri, String schemaText,
            List featureTypeNames) throws FunctionCallException {
        try {
            GmlSchemaParser gmlParser = new GmlSchemaParser();
            DomSchemaErrorHandler errorHandler = new DomSchemaErrorHandler();
            gmlParser.setSchemaErrorHandler(errorHandler);
            gmlParser.parse(baseUri, schemaText);
            if (featureTypeNames.size() > 0)
                gmlParser.validateFeatures(featureTypeNames);
            else {
                gmlParser.validateFeatures();
            }
            return errorHandler.toDomElement();
        } catch (IOException e) {
            throw new FunctionCallException(e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Element getSchemaElement(Object arg) throws FunctionCallException {
        if ((arg instanceof List)) {
            List elements = (List) arg;
            if (elements.size() == 1) {
                Object value = elements.get(0);
                if ((value instanceof Element)) {
                    Element element = (Element) value;
                    String namespaceUri = element.getNamespaceURI();
                    String name = element.getLocalName();
                    if ((namespaceUri != null)
                            && (namespaceUri
                                    .equals("http://www.w3.org/2001/XMLSchema"))
                            && (name.equals("schema"))) {
                        return element;
                    }
                }
            }
        }
        throw new FunctionCallException(
                "The first argument must be a single xs:schema element");
    }

    private static QName getQName(NamespaceContext namespaces, String name) {
        String prefix = null;
        String localName = name;
        String namespaceUri = null;
        int colonIndex = name.indexOf(":");
        if ((colonIndex > 0) && (colonIndex + 1 < name.length())) {
            prefix = name.substring(0, colonIndex);
            localName = name.substring(colonIndex + 1);
        }
        namespaceUri = namespaces.translateNamespacePrefixToUri(prefix);
        return new QName(namespaceUri, localName);
    }
}
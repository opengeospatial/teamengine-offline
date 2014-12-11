package com.galdosinc.glib.gml.validator;

import com.galdosinc.glib.gml.schema.DomSchemaErrorHandler;
import com.galdosinc.glib.gml.schema.GmlSchemaParser;
import com.galdosinc.glib.xml.QName;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.w3c.dom.Document;

public class GmlSchemaValidator {
    public static final String ERROR_FEATURE_TYPE = "A feature type name must be in the form <namespaceUri>#<typeName>";

    public Document validateSchemaByURL(String schemaURL) throws Exception {
        Document result = null;
        try {
            GmlSchemaParser gmlParser = new GmlSchemaParser();
            DomSchemaErrorHandler errorHandler = new DomSchemaErrorHandler();
            gmlParser.setSchemaErrorHandler(errorHandler);
            gmlParser.parse(new URL(schemaURL));
            gmlParser.validateFeatures();
            result = errorHandler.toDomDocument();
        } catch (MalformedURLException mue) {
            throw mue;
        } catch (IOException ioe) {
            throw ioe;
        }
        return result;
    }

    public Document validateSchemaByURL(String schemaURL, List featureTypes)
            throws IllegalArgumentException {
        Document result = null;
        try {
            GmlSchemaParser gmlParser = new GmlSchemaParser();
            DomSchemaErrorHandler errorHandler = new DomSchemaErrorHandler();
            gmlParser.setSchemaErrorHandler(errorHandler);
            gmlParser.parse(new URL(schemaURL));
            List featureTypeNames = new ArrayList();

            for (int i = 0; i < featureTypes.size(); i++) {
                String featureTypeName = (String) featureTypes.get(i);
                StringTokenizer st = new StringTokenizer(featureTypeName, "#");
                if (st.countTokens() != 2) {
                    throw new IllegalArgumentException(
                            "A feature type name must be in the form <namespaceUri>#<typeName>");
                }
                featureTypeNames.add(new QName(st.nextToken(), st.nextToken()));
            }
            if (featureTypeNames.size() > 0) {
                gmlParser.validateFeatures(featureTypeNames);
                result = errorHandler.toDomDocument();
            }
        } catch (IOException ioe) {
            System.out.println("GmlSchemaValidator.validateSchemaByURL()> "
                    + ioe.getMessage());
        }
        return result;
    }
}
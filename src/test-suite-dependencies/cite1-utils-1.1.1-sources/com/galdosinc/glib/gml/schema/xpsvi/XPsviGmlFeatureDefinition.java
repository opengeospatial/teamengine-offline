package com.galdosinc.glib.gml.schema.xpsvi;

import com.galdosinc.glib.gml.schema.GmlFeatureDefinition;
import com.galdosinc.glib.gml.schema.GmlSchemaParser;
import org.apache.xerces.xs.XSElementDeclaration;

public class XPsviGmlFeatureDefinition extends XpsviGmlObjectDefinition
        implements GmlFeatureDefinition {
    public XPsviGmlFeatureDefinition(GmlSchemaParser model,
            XSElementDeclaration element) {
        super(model, element);
    }

    public boolean isFeature() {
        return true;
    }
}
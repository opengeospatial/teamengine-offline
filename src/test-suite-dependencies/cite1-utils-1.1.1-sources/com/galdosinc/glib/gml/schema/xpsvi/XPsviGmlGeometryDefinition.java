package com.galdosinc.glib.gml.schema.xpsvi;

import com.galdosinc.glib.gml.schema.GmlConstantUtils;
import com.galdosinc.glib.gml.schema.GmlGeometryDefinition;
import com.galdosinc.glib.gml.schema.GmlSchemaParser;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;

public class XPsviGmlGeometryDefinition extends XpsviGmlObjectDefinition
        implements GmlGeometryDefinition {
    public XPsviGmlGeometryDefinition(GmlSchemaParser model,
            XSElementDeclaration element) {
        super(model, element);
    }

    public boolean isGeometry() {
        return true;
    }

    public int getCorrespondingGmlGeometryTypeCode() {
        XSComplexTypeDefinition type = (XSComplexTypeDefinition) getElementDecl()
                .getTypeDefinition();
        if (type == null) {
            type = getElementDecl().getEnclosingCTDefinition();
        }
        while ((type.getNamespace() == null)
                || (!type.getNamespace().equals("http://www.opengis.net/gml"))) {
            type = (XSComplexTypeDefinition) type.getBaseType();
        }

        int geomCode = GmlConstantUtils.getGmlGeometryCode(type.getName());
        return geomCode;
    }
}
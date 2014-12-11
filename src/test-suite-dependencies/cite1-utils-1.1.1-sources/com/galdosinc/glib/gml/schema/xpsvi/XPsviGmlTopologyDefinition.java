package com.galdosinc.glib.gml.schema.xpsvi;

import com.galdosinc.glib.gml.schema.GmlConstantUtils;
import com.galdosinc.glib.gml.schema.GmlSchemaParser;
import com.galdosinc.glib.gml.schema.GmlTopologyDefinition;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;

public class XPsviGmlTopologyDefinition extends XpsviGmlObjectDefinition
        implements GmlTopologyDefinition {
    public XPsviGmlTopologyDefinition(GmlSchemaParser model,
            XSElementDeclaration element) {
        super(model, element);
    }

    public boolean isTopology() {
        return true;
    }

    public int getCorrespondingGmlTopologyTypeCode() {
        XSComplexTypeDefinition type = (XSComplexTypeDefinition) getElementDecl()
                .getTypeDefinition();
        if (type == null) {
            type = getElementDecl().getEnclosingCTDefinition();
        }
        while ((type.getNamespace() == null)
                || (!type.getNamespace().equals("http://www.opengis.net/gml"))) {
            type = (XSComplexTypeDefinition) type.getBaseType();
        }

        int topoCode = GmlConstantUtils.getGmlTopologyCode(type.getName());
        return topoCode;
    }
}
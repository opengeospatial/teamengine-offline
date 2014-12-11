package com.galdosinc.glib.gml.schema.xpsvi;

import com.galdosinc.glib.gml.schema.GmlObjectDefinition;
import org.apache.xerces.xs.XSModel;

public class GmlObjectFactory {
    private XSModel model;

    public GmlObjectFactory(XSModel model) {
        this.model = model;
    }

    public GmlObjectDefinition getObject(String namespaceUri, String namespace) {
        return null;
    }
}
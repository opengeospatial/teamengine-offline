package org.opengis.cite.transformers;

import org.opengroup.ts.handlers.extensions.ogc.ResponseTransformer;

public class StringLength extends ResponseTransformer {
    private int length;

    public StringLength(Object response, String parameters) {
        super(response, parameters);
        this.length = response.toString().length();
    }

    public String getTransformedDocument() {
        return "<length>" + Integer.toString(this.length) + "</length>";
    }
}

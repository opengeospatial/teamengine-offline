package org.opengis.cite.transformers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.opengroup.ts.handlers.extensions.ogc.ResponseTransformer;

public class TransformXML extends ResponseTransformer {
    private static final Logger LOGR = Logger.getLogger(TransformXML.class
            .getName());
    private String TransformedDocument;

    public TransformXML(Object response, String parameters) {
        super(response, parameters);
        try {
            InputStream xml = new ByteArrayInputStream(response.toString()
                    .getBytes());
            InputStream xsl = new ByteArrayInputStream(parameters.getBytes());
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer(new StreamSource(xsl));
            OutputStream os = new ByteArrayOutputStream();
            t.setOutputProperty("indent", "yes");
            t.transform(new StreamSource(xml), new StreamResult(os));

            this.TransformedDocument = os.toString();
        } catch (Exception e) {
            LOGR.warning(e.getMessage());
            this.TransformedDocument = ("<error>" + e.getMessage() + "</error>");
        }
    }

    public String getTransformedDocument() {
        return this.TransformedDocument;
    }
}

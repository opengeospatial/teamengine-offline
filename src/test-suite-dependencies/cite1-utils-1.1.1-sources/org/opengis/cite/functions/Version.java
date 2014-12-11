package org.opengis.cite.functions;

import java.util.List;
import java.util.StringTokenizer;
import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;

public class Version implements Function {
    @SuppressWarnings("rawtypes")
    public Object call(Context context, List argumentList)
            throws FunctionCallException {
        if (argumentList.size() != 1) {
            throw new FunctionCallException(
                    "The Version function must have only one argument");
        }
        int version = 0;
        String versionString = argumentList.get(0).toString();
        StringTokenizer versionParts = new StringTokenizer(versionString, ".");
        int numParts = versionParts.countTokens();
        if (numParts != 3) {
            throw new FunctionCallException(
                    "The version must have 3 version parts (e.g. 1.0.1)");
        }
        for (int i = 0; i < numParts; i++) {
            int part = Integer.parseInt(versionParts.nextToken());
            if (part > 99) {
                throw new FunctionCallException(
                        "The version function must not contain a part which is > 99");
            }
            version *= 100;
            version += part;
        }
        return new Integer(version);
    }
}

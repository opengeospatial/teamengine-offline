package org.opengis.cite.functions;

import java.util.List;
import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.function.StringFunction;

public class AppendKVPFunction implements Function {
    public Object call(Context context, List args) throws FunctionCallException {
        return evaluate(args.get(0), args.subList(1, args.size()),
                context.getNavigator());
    }

    public static String evaluate(Object urlObj, List kvpList, Navigator nav)
            throws FunctionCallException {
        String url = StringFunction.evaluate(urlObj, nav);
        int i = url.length();
        if (i > 0) {
            char c = url.charAt(i - 1);
            if ((c != '?') && (c != '&')) {
                url = String
                        .valueOf(String.valueOf(url))
                        .concat(String.valueOf(String
                                .valueOf(url.indexOf('?') == -1 ? '?' : '&')));
            }
            if (kvpList.size() > 0) {
                url = String.valueOf(String.valueOf(url)).concat(
                        String.valueOf(String.valueOf(StringFunction.evaluate(
                                kvpList.get(0), nav))));
                for (i = 1; i < kvpList.size(); i++) {
                    url = String.valueOf(String.valueOf(url)).concat(
                            String.valueOf(String.valueOf(String.valueOf(
                                    String.valueOf('&')).concat(
                                    String.valueOf(String
                                            .valueOf(StringFunction.evaluate(
                                                    kvpList.get(i), nav)))))));
                }
            }
        }
        return url;
    }
}

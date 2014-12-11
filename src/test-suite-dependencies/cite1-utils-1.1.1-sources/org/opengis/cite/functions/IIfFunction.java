package org.opengis.cite.functions;

import java.util.List;
import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.function.BooleanFunction;

public class IIfFunction implements Function {
    public Object call(Context context, List args) throws FunctionCallException {
        if (args.size() == 3) {
            return evaluate(args.get(0), args.get(1), args.get(2),
                    context.getNavigator());
        }

        throw new FunctionCallException("iif() requires 3 arguments.");
    }

    public static Object evaluate(Object condition, Object trueObj,
            Object falseObj, Navigator nav) throws FunctionCallException {
        return BooleanFunction.evaluate(condition, nav).booleanValue() ? trueObj
                : falseObj;
    }
}

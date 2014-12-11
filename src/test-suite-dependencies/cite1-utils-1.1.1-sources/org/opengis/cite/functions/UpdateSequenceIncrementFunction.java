package org.opengis.cite.functions;

import java.util.List;
import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.function.NumberFunction;
import org.jaxen.function.StringFunction;

public class UpdateSequenceIncrementFunction implements Function {
    public Object call(Context context, List args) throws FunctionCallException {
        if (args.size() == 2) {
            return evaluate(args.get(0), args.get(1), context.getNavigator());
        }

        throw new FunctionCallException(
                "UpdateSequenceIncrement requires 2 arguments.");
    }

    public static String evaluate(Object seqeuenceObj, Object signObj,
            Navigator nav) throws FunctionCallException {
        String sequence = StringFunction.evaluate(seqeuenceObj, nav);
        int sign = NumberFunction.evaluate(signObj, nav).intValue();

        char[] chars = new char[sequence.length()];

        if (sequence.length() == 0) {
            return sequence;
        }

        sequence.getChars(0, chars.length, chars, 0);

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            char c2 = (char) (c + sign);
            if ((Character.isDigit(c)) && (Character.isDigit(c2))) {
                chars[i] = c2;
                return new String(chars);
            }
        }

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            char c2 = (char) (c + sign);
            if ((Character.isLetter(c)) && (Character.isLetter(c2))) {
                chars[i] = c2;
                return new String(chars);
            }
        }

        if (sign > 0) {
            return String.valueOf(String.valueOf(sequence)).concat(".");
        }
        return sequence.substring(0, sequence.length() - 1);
    }
}

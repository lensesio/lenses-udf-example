package io.lenses.sql.udf;

public class celsius_to_fahrenheit implements UserDefinedFunction1 {

    @Override
    public Object evaluate(Object arg1) {
        double celsius = Double.parseDouble(arg1.toString());
        double fahrenheit = celsius * 1.8 + 32;
        return Math.round(fahrenheit);
    }
}

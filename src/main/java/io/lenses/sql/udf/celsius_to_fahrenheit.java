package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.datatype.LTOptional;
import io.lenses.sql.udf.value.*;

public class celsius_to_fahrenheit implements UserDefinedFunction1 {
    @Override
    public DataType typeMapping(DataType dataType) throws UdfException {
        if (dataType.isDouble() || dataType.isInt() || dataType.isLong() || dataType.isFloat())
            return DataType.ltOptional(DataType.ltDouble());
        else if (dataType instanceof LTOptional) {
            return typeMapping(((LTOptional<?>) dataType).actualType());
        } else
            throw new UdfException("Invalid input type received. Expecting a Integer,Long, Float or Double but received:" + dataType.name);
    }

    @Override
    public Value evaluate(Value value) throws UdfException {
        if (value instanceof OptionalValue) {
            OptionalValue optionalValue = (OptionalValue) value;
            if (optionalValue.isEmpty())
                return OptionalValue.empty(DataType.ltDouble());
            DataType dataType = optionalValue.getDataType();
            Object o = optionalValue.get();
            if (dataType.isFloat()) {
                return evaluateInternal((double) ((float) o));
            } else if (dataType.isDouble()) {
                return evaluateInternal((double) o);
            } else if (dataType.isInt()) {
                return evaluateInternal((double) (int) o);
            } else if (dataType.isLong()) {
                return evaluateInternal((double) (long) o);
            } else {
                throw new UdfException("Invalid input type received. Expecting a Integer,Long, Float or Double but received:" + dataType.name);
            }
        }

        if (value instanceof DoubleValue) {
            return evaluateInternal(((DoubleValue) value).get());
        } else if (value instanceof LongValue) {
            return evaluateInternal(((LongValue) value).get().doubleValue());
        } else if (value instanceof IntValue) {
            return evaluateInternal(((IntValue) value).get().doubleValue());
        } else if (value instanceof FloatValue) {
            return evaluateInternal(((FloatValue) value).get().doubleValue());
        }
        throw new UdfException("Invalid input type received. Expecting a Integer,Long, Float or Double but received:" + value.getDataType().name);
    }

    private OptionalValue evaluateInternal(Double celsius) throws UdfException {
        double fahrenheit = celsius * 1.8 + 32;
        return OptionalValue.of(new DoubleValue(Math.round(fahrenheit * 100.0)/ 100.0));
    }

    @Override
    public String name() {
        return "celsius_to_fahrenheit";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public String owner() {
        return "TeamB";
    }
}

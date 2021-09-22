package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.value.*;

import java.math.BigDecimal;
import java.math.BigInteger;


public class multiplyByTwo implements UserDefinedFunction1 {
    @Override
    public DataType typeMapping(DataType dataType) throws UdfException {
        if (dataType.isNumber())
            return dataType;
        else
            throw new UdfException("Invalid input type received. Expecting a Integer but received:" + dataType.name);
    }

    @Override
    public Value evaluate(Value value) throws UdfException {
        if (value instanceof BigDecimalValue) {
            return new BigDecimalValue(value.toBigDecimalValue().get().multiply(new BigDecimal(2)));
        } else if (value instanceof BigIntValue) {
            return new BigIntValue(value.toBigIntValue().get().multiply(BigInteger.valueOf(2)));
        } else if (value instanceof DoubleValue) {
            return new DoubleValue(value.toDoubleValue().get() * 2);
        } else if (value instanceof FloatValue) {
            return new FloatValue(value.toFloatValue().get() * 2);
        } else if (value instanceof IntValue) {
            return new IntValue(value.toIntValue().get() * 2);
        } else if (value instanceof LongValue) {
            return new LongValue(value.toLongValue().get() * 2);
        }
        throw new UdfRuntimeException("Expecting a number but found " + value.getDataType().name + ".");
    }

    @Override
    public String name() {
        return "multiplyByTwo";
    }
}
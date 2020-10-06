package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.datatype.LTOptional;
import io.lenses.sql.udf.value.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class celsius_to_fahrenheitTest {
    private celsius_to_fahrenheit func = new celsius_to_fahrenheit();

    @Test
    public void typesTheUdf() throws UdfException {
        assertTrue(func.typeMapping(DataType.ltDouble()) instanceof LTOptional);
        assertTrue(func.typeMapping(DataType.ltDouble()).isDouble());
        assertTrue(func.typeMapping(DataType.ltFloat()).isDouble());
        assertTrue(func.typeMapping(DataType.ltInt()).isDouble());
        assertTrue(func.typeMapping(DataType.ltLong()).isDouble());

        assertTrue(func.typeMapping(DataType.ltOptional(DataType.ltDouble())).isDouble());
        assertTrue(func.typeMapping(DataType.ltOptional(DataType.ltFloat())).isDouble());
        assertTrue(func.typeMapping(DataType.ltOptional(DataType.ltInt())).isDouble());
        assertTrue(func.typeMapping(DataType.ltOptional(DataType.ltLong())).isDouble());
    }

    @Test
    public void throwErrorOnInvalidType() {

        assertThrows(UdfException.class, () -> func.typeMapping(DataType.ltString()).isDouble());
        assertThrows(UdfException.class, () -> func.typeMapping(DataType.ltOptional(DataType.ltString())).isDouble());
    }

    @Test
    public void throwsOnInvalidInput() {
        assertThrows(UdfException.class, () -> func.evaluate(new StringValue("abc")));
        assertThrows(UdfException.class, () -> func.evaluate(OptionalValue.of(new StringValue("abc"))));
    }

    @Test
    public void evaluatesCorrectly() throws UdfException {
        assertEquals(77.0, evaluate(new LongValue(25)));
        assertEquals(77.0, evaluate(new IntValue(25)));
        assertEquals(82.76, evaluate(new DoubleValue(28.2)));
        assertEquals(82.76, evaluate(new FloatValue(28.2f)));
    }

    private double evaluate(Value value) throws UdfException {
        DoubleValue result = (DoubleValue) func.evaluate(value).get();
        return result.get();
    }
}

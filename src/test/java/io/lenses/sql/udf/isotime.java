package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.datatype.LTOptional;
import io.lenses.sql.udf.value.LongValue;
import io.lenses.sql.udf.value.OptionalValue;
import io.lenses.sql.udf.value.StringValue;
import io.lenses.sql.udf.value.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class isotime {
    private isoformattime func = new isoformattime();

    @Test
    public void typesTheUdf() throws UdfException {
        assertTrue(func.typeMapping(DataType.ltLong()) instanceof LTOptional);
        assertTrue(func.typeMapping(DataType.ltLong()).isString());

        assertTrue(func.typeMapping(DataType.ltOptional(DataType.ltLong())) instanceof LTOptional);
        assertTrue(func.typeMapping(DataType.ltOptional(DataType.ltLong())).isString());
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
        assertEquals("12:51:24Z", evaluate(new LongValue(1601988684000L)));
    }

    private String evaluate(Value value) throws UdfException {
        return (String) func.evaluate(value).get();
    }
}

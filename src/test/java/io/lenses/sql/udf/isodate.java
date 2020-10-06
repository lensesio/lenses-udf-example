package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.datatype.LTOptional;
import io.lenses.sql.udf.value.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class isodate {
    private isoformatdate func = new isoformatdate();


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
        assertEquals("1970-01-19Z", evaluate(new LongValue(1601988684)));
    }

    private String evaluate(Value value) throws UdfException {
        return (String) func.evaluate(value).get();
    }
}

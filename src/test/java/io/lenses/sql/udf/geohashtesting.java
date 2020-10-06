package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.value.DoubleValue;
import io.lenses.sql.udf.value.OptionalValue;
import io.lenses.sql.udf.value.StringValue;
import io.lenses.sql.udf.value.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class geohashtesting {
    private GeoHash func = new GeoHash();


    @Test
    public void typesTheUdf() throws UdfException {
        assertTrue(func.typeMapping(DataType.ltDouble(), DataType.ltDouble()).isString());
    }

    @Test
    public void throwErrorOnInvalidType() {
        assertThrows(UdfException.class, () -> func.typeMapping(DataType.ltDouble(), DataType.ltString()).isDouble());
        assertThrows(UdfException.class, () -> func.typeMapping(DataType.ltDouble(), DataType.ltOptional(DataType.ltString())).isDouble());
        assertThrows(UdfException.class, () -> func.typeMapping(DataType.ltInt(), DataType.ltDouble()));
        assertThrows(UdfException.class, () -> func.typeMapping(DataType.ltBigDecimal(), DataType.ltDouble()));
    }

    @Test
    public void throwsOnInvalidInput() {
        assertThrows(UdfException.class, () -> func.evaluate(new StringValue("abc"), new StringValue("abc")));
        assertThrows(UdfException.class, () -> func.evaluate(OptionalValue.of(new StringValue("abc")), OptionalValue.of(new StringValue("abc"))));
    }

    @Test
    public void evaluatesCorrectly() throws UdfException {
        //London location
        assertEquals("s0000000", evaluate(new DoubleValue(0), new DoubleValue(0)));
        //Paris latitude 48.864716, and the longitude is 2.349014
        assertEquals("u09tvrw3", evaluate(new DoubleValue(48.864716), new DoubleValue(2.349014)));
    }

    private String evaluate(Value latitude, Value longitude) throws UdfException {
        return (String) func.evaluate(latitude, longitude).get();
    }
}

package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.datatype.LTLong;
import io.lenses.sql.udf.value.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class crc32test {
    private crc32 func = new crc32();

    private List<DataType> ofOne(DataType dataType) {
        return Collections.singletonList(dataType);
    }

    private List<DataType> ofMany(DataType... dataType) {
        return Arrays.asList(dataType);
    }

    @Test
    public void typesTheUdf() throws UdfException {
        assertTrue(func.typer(ofOne(DataType.ltDouble())) instanceof LTLong);
        assertTrue(func.typer(ofOne(DataType.ltFloat())) instanceof LTLong);
        assertTrue(func.typer(ofOne(DataType.ltInt())) instanceof LTLong);
        assertTrue(func.typer(ofOne(DataType.ltBigDecimal())) instanceof LTLong);
        assertTrue(func.typer(ofOne(DataType.ltOptional(DataType.ltDouble()))) instanceof LTLong);
        assertTrue(func.typer(ofOne(DataType.ltLong())) instanceof LTLong);
        assertTrue(func.typer(ofOne(DataType.ltTimeMicros())) instanceof LTLong);
        assertTrue(func.typer(ofOne(DataType.ltTimeMillis())) instanceof LTLong);
        assertTrue(func.typer(ofOne(DataType.ltTimestampMicros())) instanceof LTLong);
        assertTrue(func.typer(ofOne(DataType.ltTimestampMillis())) instanceof LTLong);
        assertTrue(func.typer(ofMany(DataType.ltTimestampMillis(), DataType.ltLong())) instanceof LTLong);
        assertTrue(func.typer(ofMany(DataType.ltTimestampMillis(), DataType.ltLong(), DataType.ltString())) instanceof LTLong);
        assertTrue(func.typer(ofMany(DataType.ltByteArray(), DataType.ltString())) instanceof LTLong);

    }

    @Test
    public void throwErrorOnInvalidType() {
        assertThrows(UdfException.class, () -> func.typer(ofOne(DataType.ltRepeated(DataType.ltString()))));
        assertThrows(UdfException.class, () -> func.typer(ofOne(DataType.ltOptional(DataType.ltRepeated(DataType.ltString())))));

        Map<String, DataType> fieldsMap = new HashMap<>();
        fieldsMap.put("a", DataType.ltInt());
        DataType struct = DataType.ltStruct(fieldsMap);
        assertThrows(UdfException.class, () -> func.typer(ofOne(struct)));
    }

    @Test
    public void throwsOnInvalidInput() {
        assertThrows(UdfException.class, () -> func.evaluate(Collections.singletonList(StructValue.ofTwo("a", new StringValue("abc"), "b", OptionalValue.of(new IntValue(2))))));
        assertThrows(UdfException.class, () -> func.evaluate(Collections.singletonList(RepeatedValue.ofOne(new StringValue("abc")))));
    }

    @Test
    public void evaluatesCorrectly() throws UdfException {
        assertEquals(4196041389L, evaluate(new LongValue(25)));
        assertEquals(2898551529L, evaluate(new StringValue("abc"), new StringValue("xyz")));
        assertEquals(1124702466, evaluate(new ByteArrayValue(new byte[]{1, -21, 0})));
    }

    private long evaluate(Value... value) throws UdfException {
        LongValue result = (LongValue) func.evaluate(Arrays.asList(value));
        return result.get();
    }
}

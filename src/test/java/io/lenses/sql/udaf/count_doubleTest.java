package io.lenses.sql.udaf;

import io.lenses.sql.udf.UdfException;
import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.value.LongValue;
import io.lenses.sql.udf.value.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class count_doubleTest {
    private count_double func = new count_double();

    @Test
    public void typesTheUdaf() throws UdfException {
        assertTrue(func.typer(DataType.ltDouble()).isLong());
        assertTrue(func.typer(DataType.ltFloat()).isLong());
        assertTrue(func.typer(DataType.ltInt()).isLong());
        assertTrue(func.typer(DataType.ltLong()).isLong());

        assertTrue(func.typer(DataType.ltOptional(DataType.ltDouble())).isLong());
        assertTrue(func.typer(DataType.ltOptional(DataType.ltFloat())).isLong());
        assertTrue(func.typer(DataType.ltOptional(DataType.ltInt())).isLong());
        assertTrue(func.typer(DataType.ltOptional(DataType.ltLong())).isLong());
    }

    @Test
    public void correctlyCalculatesEmpty() throws UdfException {
        assertEquals(func.empty().get(), 0L);
    }

    @Test
    public void correctlyAddsElements() throws UdfException {
        Value added1  = func.add(func.empty(), func.empty(), null);
        assertEquals(added1.get(), 2L);
    }

    @Test
    public void correctlyMergesElements() throws UdfException {
        Value merge  = func.merge(null, new LongValue(3), new LongValue(2));
        assertEquals(merge.get(), 5L);
    }


}

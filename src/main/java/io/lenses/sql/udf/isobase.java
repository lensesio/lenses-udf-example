package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.datatype.LTLong;
import io.lenses.sql.udf.datatype.LTOptional;
import io.lenses.sql.udf.value.OptionalValue;
import io.lenses.sql.udf.value.StringValue;
import io.lenses.sql.udf.value.Value;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Formats a timestamp representing milliseconds since the unix epoch as an ISO Date Time formatted string.
 */
abstract class isobase implements UserDefinedFunction1 {
    @Override
    public DataType typeMapping(DataType dataType) throws UdfException {
        if (dataType instanceof LTLong) {
            return DataType.ltOptional(DataType.ltString());
        } else if (dataType instanceof LTOptional) {
            LTOptional<DataType> optional = (LTOptional<DataType>) dataType;
            return typeMapping(optional.actualType());
        } else throw new UdfException("Invalid type received. Expecting Long but got:" + dataType.name);
    }

    @Override
    public Value evaluate(Value value) throws UdfException {
        Object o = value.get();
        if (o == null) return OptionalValue.empty(DataType.ltString());

        long timestamp;
        if (o instanceof Long) timestamp = (Long) o;
        else
            throw new UdfException("Invalid type received. Expecting Long or Integer but got:" + value.getDataType().name);
        OffsetDateTime dt = Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC);
        return new StringValue(format(dt));
    }

    protected abstract String format(OffsetDateTime dt);
}
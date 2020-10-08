package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.datatype.LTOptional;
import io.lenses.sql.udf.value.ByteArrayValue;
import io.lenses.sql.udf.value.LongValue;
import io.lenses.sql.udf.value.OptionalValue;
import io.lenses.sql.udf.value.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.CRC32;

public class crc32 implements UserDefinedFunctionVarArg {
    @Override
    public String name() {
        return "crc32";
    }

    private void validateType(DataType dataType) throws UdfException {
        if (dataType instanceof LTOptional) {
            validateType(((LTOptional<?>) dataType).actualType());
        } else if (dataType.isContainer()) {
            throw new UdfException("Invalid data type received. Expecting primitives but received:" + dataType.name);
        }
    }

    @Override
    public DataType typer(List<DataType> list) throws UdfException {
        //only allow primitives or optional of primitives
        for (DataType dt : list) {
            validateType(dt);
        }
        return DataType.ltLong();
    }

    private Optional<byte[]> extractValueAsByte(Value value) throws UdfException {
        if (value instanceof ByteArrayValue) {
            return Optional.of(((ByteArrayValue) value).get());
        }
        if (value instanceof OptionalValue) {
            OptionalValue optionalValue = (OptionalValue) value;
            if (optionalValue.isEmpty()) return Optional.empty();
            else return extractValueAsByte((Value) optionalValue.get());
        }
        if (value.isContainer()) {
            throw new UdfException("Invalid input received. Expecting primitives but found:" + value.getDataType().name);
        }
        return Optional.of(value.toStringValue().get().getBytes());
    }

    @Override
    public Value evaluate(List<Value> list) throws UdfException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (Value vl : list) {
            Optional<byte[]> maybeBytes = extractValueAsByte(vl);
            if (maybeBytes.isPresent()) {
                try {
                    out.write(maybeBytes.get());
                } catch (IOException e) {
                    throw new UdfException("Cannot build the crc32 array");
                }
            }
        }
        CRC32 crc = new CRC32();
        crc.update(out.toByteArray());
        return new LongValue(crc.getValue());
    }
}

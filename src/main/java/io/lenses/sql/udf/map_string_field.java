package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.datatype.LTOptional;
import io.lenses.sql.udf.datatype.LTRepeated;
import io.lenses.sql.udf.datatype.LTString;
import io.lenses.sql.udf.value.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class map_string_field implements UserDefinedFunction2 {

    @Override
    public String name() {
        return "map_string_field";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public String owner() {
        return "Lenses.io";
    }

    @Override
    public DataType typeMapping(DataType dataType, DataType dataType1) throws UdfException {
        return DataType.ltRepeated(DataType.ltString());
    }

    @Override
    public Value evaluate(Value value, Value value1) throws UdfException {
        String property = value1.toStringValue().get();
        List<StructValue> values = value.asRepeatedValue().get();
        List<StringValue> result = new ArrayList<>();
        for (StructValue v : values) {
            result.add(v.getField(property).toStringValue());
        }
        return new RepeatedValue<>(result, DataType.ltString());
    }
}

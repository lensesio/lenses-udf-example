package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.datatype.LTStruct;
import io.lenses.sql.udf.value.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class deduplicate_string implements UserDefinedFunction1 {
    @Override
    public String name() {
        return "deduplicate_string";
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
    public DataType typeMapping(DataType dataType) throws UdfException {
        return DataType.ltRepeated(DataType.ltString());
    }

    @Override
    public Value evaluate(Value value) throws UdfException {
        Stream<StringValue> stream = value
                .asRepeatedValue()
                .get()
                .stream();
        return new RepeatedValue<>(stream.map(Primitive::get)
                .distinct()
                .map(s -> new StringValue((String) s))
                .collect(Collectors.toList()), DataType.ltString());
    }
}
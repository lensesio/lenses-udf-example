package io.lenses.udf.sql;

import io.lenses.sql.udf.UserDefinedFunction;

import java.time.Instant;
import java.util.List;

public class datetimefromepoch implements UserDefinedFunction {
    @Override
    public Object evaluate(List<Object> list) {
        long timestamp = Long.parseLong(list.get(0).toString());
        return Instant.ofEpochMilli(timestamp);
    }
}
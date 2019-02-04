package io.lenses.udf.sql;

import io.lenses.sql.udf.UserDefinedFunction;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Formats a timestamp representing milliseconds since the unix epoch as an ISO Date Time formatted string.
 */
public class isoformatdatetimestring implements UserDefinedFunction {
    @Override
    public Object evaluate(List<Object> list) {
        long timestamp = Long.parseLong(list.get(0).toString());
        OffsetDateTime dt = Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC);
        return DateTimeFormatter.ISO_DATE_TIME.format(dt);
    }
}
package io.lenses.sql.udf;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Formats a timestamp representing milliseconds since the unix epoch as an ISO Date Time formatted string.
 */
public class isoformatdate implements UserDefinedFunction1 {

    @Override
    public Object evaluate(Object o) {
        if (o == null) return null;
        long timestamp = Long.parseLong(o.toString());
        OffsetDateTime dt = Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC);
        return DateTimeFormatter.ISO_DATE.format(dt);
    }
}
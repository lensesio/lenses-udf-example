package io.lenses.sql.udf;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Formats a timestamp representing milliseconds since the unix epoch as an ISO Date Time formatted string.
 */
public class isoformatdatetime extends isobase {
    @Override
    public String name() {
        return "isoformatdatetime";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public String owner() {
        return "TeamC";
    }

    @Override
    protected String format(OffsetDateTime dt) {
        return DateTimeFormatter.ISO_DATE_TIME.format(dt);
    }
}
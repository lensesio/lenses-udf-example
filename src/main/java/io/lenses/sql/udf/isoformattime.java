package io.lenses.sql.udf;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Formats a timestamp representing milliseconds since the unix epoch as an ISO Date Time formatted string.
 */
public class isoformattime extends isobase {

    @Override
    public String name() {
        return "isoformattime";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public String owner() {
        return "TeamA";
    }

    @Override
    protected String format(OffsetDateTime dt) {
        return DateTimeFormatter.ISO_TIME.format(dt);
    }
}
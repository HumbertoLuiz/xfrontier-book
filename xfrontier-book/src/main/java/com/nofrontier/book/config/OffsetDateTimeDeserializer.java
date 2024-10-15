package com.nofrontier.book.config;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import io.jsonwebtoken.io.IOException;

public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    private DateTimeFormatter fmt = new DateTimeFormatterBuilder()
        // date/time
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        // offset (hh:mm - "+00:00" when it's zero)
        .optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
        // offset (hhmm - "+0000" when it's zero)
        .optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
        // offset (hh - "+00" when it's zero)
        .optionalStart().appendOffset("+HH", "+00").optionalEnd()
        // offset (pattern "X" uses "Z" for zero offset)
        .optionalStart().appendPattern("X").optionalEnd()
        // create formatter
        .toFormatter();

    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, java.io.IOException {
        return OffsetDateTime.parse(p.getText(), fmt);
    }
}

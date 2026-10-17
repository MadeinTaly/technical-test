package com.techtest.traffic.log;

import java.util.Optional;
import java.util.regex.Pattern;

public final class LogEntryParser {

    private static final Pattern SEPARATOR = Pattern.compile(";");
    private static final int FIELD_COUNT = 4;

    public Optional<LogEntry> parse(String line) {
        String[] fields = SEPARATOR.split(line, -1);
        if (fields.length != FIELD_COUNT) {
            return Optional.empty();
        }
        try {
            return Optional.of(new LogEntry(
                    fields[0].strip(),
                    Long.parseLong(fields[1].strip()),
                    fields[2].strip(),
                    fields[3].strip()));
        } catch (IllegalArgumentException malformed) {
            return Optional.empty();
        }
    }
}

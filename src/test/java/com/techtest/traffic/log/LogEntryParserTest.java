package com.techtest.traffic.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogEntryParserTest {

    private final LogEntryParser parser = new LogEntryParser();

    @Test
    void parsesWellFormedLine() {
        assertEquals(
                new LogEntry("1696154400", 512, "OK", "192.168.1.10"),
                parser.parse("1696154400;512;OK;192.168.1.10").orElseThrow());
    }

    @Test
    void stripsSurroundingWhitespace() {
        assertEquals(
                new LogEntry("1696154400", 512, "OK", "10.0.0.1"),
                parser.parse(" 1696154400 ; 512 ; OK ; 10.0.0.1 ").orElseThrow());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "1696154400;512;OK",
            "1696154400;512;OK;10.0.0.1;extra",
            "1696154400;abc;OK;10.0.0.1",
            "1696154400;-1;OK;10.0.0.1",
            "1696154400;512;OK; "
    })
    void rejectsMalformedLines(String line) {
        assertTrue(parser.parse(line).isEmpty());
    }

    @Test
    void onlyOkStatusIsSuccessful() {
        assertTrue(new LogEntry("t", 1, "OK", "10.0.0.1").isSuccessful());
        assertFalse(new LogEntry("t", 1, "Not Found", "10.0.0.1").isSuccessful());
        assertFalse(new LogEntry("t", 1, "ok", "10.0.0.1").isSuccessful());
    }
}

package com.techtest.traffic.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PercentageTest {

    @ParameterizedTest
    @CsvSource({
            "1, 3, 33.33",
            "2, 3, 66.67",
            "3, 3, 100.00",
            "0, 7, 0.00",
            "0, 0, 0.00"
    })
    void roundsHalfUpToTwoDecimals(long part, long total, String expected) {
        assertEquals(expected, Percentage.of(part, total).toString());
    }

    @Test
    void rejectsPartOutsideTotal() {
        assertThrows(IllegalArgumentException.class, () -> Percentage.of(4, 3));
        assertThrows(IllegalArgumentException.class, () -> Percentage.of(-1, 3));
    }
}

package com.techtest.traffic.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Percentage(BigDecimal value) {

    private static final int SCALE = 2;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final Percentage NONE = new Percentage(BigDecimal.ZERO.setScale(SCALE));

    public Percentage {
        Objects.requireNonNull(value, "value");
    }

    public static Percentage of(long part, long total) {
        if (part < 0 || part > total) {
            throw new IllegalArgumentException("part must be within [0, %d]: %d".formatted(total, part));
        }
        if (total == 0) {
            return NONE;
        }
        return new Percentage(BigDecimal.valueOf(part)
                .multiply(HUNDRED)
                .divide(BigDecimal.valueOf(total), SCALE, RoundingMode.HALF_UP));
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}

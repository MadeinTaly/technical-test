package com.techtest.traffic.log;

import java.util.Objects;

public record LogEntry(String timestamp, long bytes, String status, String remoteAddress) {

    private static final String STATUS_OK = "OK";

    public LogEntry {
        Objects.requireNonNull(timestamp, "timestamp");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(remoteAddress, "remoteAddress");
        if (bytes < 0) {
            throw new IllegalArgumentException("bytes must not be negative: " + bytes);
        }
        if (remoteAddress.isBlank()) {
            throw new IllegalArgumentException("remoteAddress must not be blank");
        }
    }

    public boolean isSuccessful() {
        return STATUS_OK.equals(status);
    }
}

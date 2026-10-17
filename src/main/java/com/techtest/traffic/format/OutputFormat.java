package com.techtest.traffic.format;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public enum OutputFormat {

    CSV("csv", new CsvReportFormatter()),
    JSON("json", new JsonReportFormatter());

    private final String extension;
    private final ReportFormatter formatter;

    OutputFormat(String extension, ReportFormatter formatter) {
        this.extension = extension;
        this.formatter = formatter;
    }

    public static OutputFormat fromName(String name) {
        return Arrays.stream(values())
                .filter(format -> format.extension.equals(name.strip().toLowerCase(Locale.ROOT)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported format '%s', expected one of: %s"
                        .formatted(name, Arrays.stream(values()).map(f -> f.extension).collect(Collectors.joining(", ")))));
    }

    public String fileName(String baseName) {
        return baseName + "." + extension;
    }

    public ReportFormatter formatter() {
        return formatter;
    }
}

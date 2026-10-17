package com.techtest.traffic.format;

import com.techtest.traffic.report.ReportLine;
import com.techtest.traffic.report.TrafficReport;

import java.io.IOException;
import java.io.Writer;
import java.util.stream.Collectors;

final class JsonReportFormatter implements ReportFormatter {

    private static final String EMPTY_ARRAY = "[]\n";
    private static final String LINE_TEMPLATE = """
              {
                "ipAddress": %s,
                "requests": %d,
                "requestsPercentage": %s,
                "bytesSent": %d,
                "bytesPercentage": %s
              }\
            """;

    @Override
    public void write(TrafficReport report, Writer writer) throws IOException {
        writer.write(report.lines().isEmpty()
                ? EMPTY_ARRAY
                : report.lines().stream()
                        .map(JsonReportFormatter::toJson)
                        .collect(Collectors.joining(",\n", "[\n", "\n]\n")));
    }

    private static String toJson(ReportLine line) {
        return LINE_TEMPLATE.formatted(
                quote(line.ipAddress()),
                line.requests(),
                line.requestsPercentage(),
                line.bytesSent(),
                line.bytesPercentage());
    }

    private static String quote(String value) {
        StringBuilder json = new StringBuilder(value.length() + 2).append('"');
        value.chars().forEach(character -> {
            switch (character) {
                case '"' -> json.append("\\\"");
                case '\\' -> json.append("\\\\");
                case '\n' -> json.append("\\n");
                case '\r' -> json.append("\\r");
                case '\t' -> json.append("\\t");
                default -> {
                    if (character < 0x20) {
                        json.append("\\u%04x".formatted(character));
                    } else {
                        json.append((char) character);
                    }
                }
            }
        });
        return json.append('"').toString();
    }
}

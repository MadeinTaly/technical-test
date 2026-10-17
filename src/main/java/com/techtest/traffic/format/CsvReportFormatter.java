package com.techtest.traffic.format;

import com.techtest.traffic.report.ReportLine;
import com.techtest.traffic.report.TrafficReport;

import java.io.IOException;
import java.io.Writer;

final class CsvReportFormatter implements ReportFormatter {

    private static final String HEADER =
            "IP Address,Number of requests,Percentage of requests,Total bytes sent,Percentage of bytes";
    private static final String DELIMITER = ",";
    private static final String RECORD_SEPARATOR = "\r\n";
    private static final String QUOTE = "\"";

    @Override
    public void write(TrafficReport report, Writer writer) throws IOException {
        writer.write(HEADER);
        writer.write(RECORD_SEPARATOR);
        for (ReportLine line : report.lines()) {
            writer.write(String.join(DELIMITER,
                    escape(line.ipAddress()),
                    String.valueOf(line.requests()),
                    line.requestsPercentage().toString(),
                    String.valueOf(line.bytesSent()),
                    line.bytesPercentage().toString()));
            writer.write(RECORD_SEPARATOR);
        }
    }

    private static String escape(String field) {
        if (field.contains(DELIMITER) || field.contains(QUOTE) || field.contains("\n") || field.contains("\r")) {
            return QUOTE + field.replace(QUOTE, QUOTE + QUOTE) + QUOTE;
        }
        return field;
    }
}

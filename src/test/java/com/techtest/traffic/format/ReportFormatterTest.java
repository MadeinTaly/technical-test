package com.techtest.traffic.format;

import com.techtest.traffic.report.Percentage;
import com.techtest.traffic.report.ReportLine;
import com.techtest.traffic.report.TrafficReport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportFormatterTest {

    private static final TrafficReport REPORT = new TrafficReport(List.of(
            new ReportLine("10.0.0.2", 2, Percentage.of(2, 3), 400, Percentage.of(400, 500)),
            new ReportLine("10.0.0.1", 1, Percentage.of(1, 3), 100, Percentage.of(100, 500))));

    @Test
    void writesCsvWithHeader() throws IOException {
        assertEquals("""
                IP Address,Number of requests,Percentage of requests,Total bytes sent,Percentage of bytes\r
                10.0.0.2,2,66.67,400,80.00\r
                10.0.0.1,1,33.33,100,20.00\r
                """, render(OutputFormat.CSV, REPORT));
    }

    @Test
    void writesJsonArray() throws IOException {
        assertEquals("""
                [
                  {
                    "ipAddress": "10.0.0.2",
                    "requests": 2,
                    "requestsPercentage": 66.67,
                    "bytesSent": 400,
                    "bytesPercentage": 80.00
                  },
                  {
                    "ipAddress": "10.0.0.1",
                    "requests": 1,
                    "requestsPercentage": 33.33,
                    "bytesSent": 100,
                    "bytesPercentage": 20.00
                  }
                ]
                """, render(OutputFormat.JSON, REPORT));
    }

    @Test
    void escapesUntrustedAddresses() throws IOException {
        TrafficReport report = new TrafficReport(List.of(
                new ReportLine("a,\"b\"\\", 1, Percentage.of(1, 1), 1, Percentage.of(1, 1))));

        assertTrue(render(OutputFormat.CSV, report).contains("\r\n\"a,\"\"b\"\"\\\",1,100.00,1,100.00\r\n"));
        assertTrue(render(OutputFormat.JSON, report).contains("\"ipAddress\": \"a,\\\"b\\\"\\\\\","));
    }

    @ParameterizedTest
    @EnumSource(OutputFormat.class)
    void handlesEmptyReport(OutputFormat format) throws IOException {
        String expected = format == OutputFormat.CSV
                ? "IP Address,Number of requests,Percentage of requests,Total bytes sent,Percentage of bytes\r\n"
                : "[]\n";
        assertEquals(expected, render(format, new TrafficReport(List.of())));
    }

    @Test
    void resolvesFormatByNameIgnoringCase() {
        assertEquals(OutputFormat.JSON, OutputFormat.fromName(" Json "));
        assertEquals("ipaddr.csv", OutputFormat.fromName("csv").fileName("ipaddr"));
        assertThrows(IllegalArgumentException.class, () -> OutputFormat.fromName("xml"));
    }

    private static String render(OutputFormat format, TrafficReport report) throws IOException {
        StringWriter writer = new StringWriter();
        format.formatter().write(report, writer);
        return writer.toString();
    }
}

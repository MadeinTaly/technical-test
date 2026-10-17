package com.techtest.traffic.report;

import com.techtest.traffic.log.LogEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrafficAggregatorTest {

    private final TrafficAggregator aggregator = new TrafficAggregator();

    @Test
    void aggregatesSuccessfulRequestsPerAddressSortedByRequestsDescending() {
        TrafficReport report = aggregator.aggregate(Stream.of(
                entry("10.0.0.1", 100, "OK"),
                entry("10.0.0.2", 300, "OK"),
                entry("10.0.0.2", 100, "OK"),
                entry("10.0.0.3", 500, "OK"),
                entry("10.0.0.2", 999, "Not Found"),
                entry("10.0.0.4", 999, "Internal Server Error")));

        assertEquals(List.of(
                line("10.0.0.2", 2, "50.00", 400, "40.00"),
                line("10.0.0.3", 1, "25.00", 500, "50.00"),
                line("10.0.0.1", 1, "25.00", 100, "10.00")
        ), report.lines());
    }

    @Test
    void breaksTiesByBytesThenAddress() {
        TrafficReport report = aggregator.aggregate(Stream.of(
                entry("10.0.0.9", 10, "OK"),
                entry("10.0.0.1", 10, "OK"),
                entry("10.0.0.5", 20, "OK")));

        assertEquals(
                List.of("10.0.0.5", "10.0.0.1", "10.0.0.9"),
                report.lines().stream().map(ReportLine::ipAddress).toList());
    }

    @Test
    void producesEmptyReportWhenNothingSucceeded() {
        TrafficReport report = aggregator.aggregate(Stream.of(entry("10.0.0.1", 10, "Forbidden")));

        assertTrue(report.lines().isEmpty());
    }

    private static LogEntry entry(String address, long bytes, String status) {
        return new LogEntry("1696154400", bytes, status, address);
    }

    private static ReportLine line(String address, long requests, String requestsShare, long bytes, String bytesShare) {
        return new ReportLine(
                address,
                requests,
                new Percentage(new BigDecimal(requestsShare)),
                bytes,
                new Percentage(new BigDecimal(bytesShare)));
    }
}

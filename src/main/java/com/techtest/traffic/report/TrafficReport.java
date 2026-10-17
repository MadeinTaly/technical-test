package com.techtest.traffic.report;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public record TrafficReport(List<ReportLine> lines) {

    private static final Comparator<ReportLine> BY_REQUESTS_DESC = Comparator
            .comparingLong(ReportLine::requests).reversed()
            .thenComparing(Comparator.comparingLong(ReportLine::bytesSent).reversed())
            .thenComparing(ReportLine::ipAddress);

    public TrafficReport {
        lines = List.copyOf(lines);
    }

    static TrafficReport of(Collection<IpTraffic> traffic) {
        long totalRequests = traffic.stream().mapToLong(IpTraffic::requests).reduce(0, Math::addExact);
        long totalBytes = traffic.stream().mapToLong(IpTraffic::bytes).reduce(0, Math::addExact);

        return new TrafficReport(traffic.stream()
                .map(ip -> new ReportLine(
                        ip.ipAddress(),
                        ip.requests(),
                        Percentage.of(ip.requests(), totalRequests),
                        ip.bytes(),
                        Percentage.of(ip.bytes(), totalBytes)))
                .sorted(BY_REQUESTS_DESC)
                .toList());
    }
}

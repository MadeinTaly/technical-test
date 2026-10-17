package com.techtest.traffic.report;

import com.techtest.traffic.log.LogEntry;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TrafficAggregator {

    public TrafficReport aggregate(Stream<LogEntry> entries) {
        return TrafficReport.of(entries
                .filter(LogEntry::isSuccessful)
                .collect(Collectors.toMap(LogEntry::remoteAddress, IpTraffic::of, IpTraffic::merge))
                .values());
    }
}

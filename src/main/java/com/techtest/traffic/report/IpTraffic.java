package com.techtest.traffic.report;

import com.techtest.traffic.log.LogEntry;

record IpTraffic(String ipAddress, long requests, long bytes) {

    static IpTraffic of(LogEntry entry) {
        return new IpTraffic(entry.remoteAddress(), 1, entry.bytes());
    }

    IpTraffic merge(IpTraffic other) {
        return new IpTraffic(ipAddress, Math.addExact(requests, other.requests), Math.addExact(bytes, other.bytes));
    }
}

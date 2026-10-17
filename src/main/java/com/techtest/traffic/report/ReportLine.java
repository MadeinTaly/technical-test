package com.techtest.traffic.report;

public record ReportLine(
        String ipAddress,
        long requests,
        Percentage requestsPercentage,
        long bytesSent,
        Percentage bytesPercentage) {
}

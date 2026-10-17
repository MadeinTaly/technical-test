package com.techtest.traffic;

import com.techtest.traffic.log.LogEntryParser;
import com.techtest.traffic.report.TrafficAggregator;

import java.io.IOException;
import java.nio.file.Path;

public final class IpAddrReportApplication {

    private static final int INVALID_USAGE = 2;
    private static final int IO_FAILURE = 1;

    private IpAddrReportApplication() {
    }

    public static void main(String[] args) {
        CommandLineOptions options;
        try {
            options = CommandLineOptions.parse(args);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.println(CommandLineOptions.USAGE);
            System.exit(INVALID_USAGE);
            return;
        }

        DailyReportGenerator generator = new DailyReportGenerator(new LogEntryParser(), new TrafficAggregator());
        try {
            Path report = generator.generate(options.logFile(), options.reportDirectory(), options.format());
            System.out.println("Report written to " + report.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Unable to generate report: " + e);
            System.exit(IO_FAILURE);
        }
    }
}

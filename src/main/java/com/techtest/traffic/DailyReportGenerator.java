package com.techtest.traffic;

import com.techtest.traffic.format.OutputFormat;
import com.techtest.traffic.log.LogEntryParser;
import com.techtest.traffic.report.TrafficAggregator;
import com.techtest.traffic.report.TrafficReport;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class DailyReportGenerator {

    private static final String REPORT_NAME = "ipaddr";

    private final LogEntryParser parser;
    private final TrafficAggregator aggregator;

    public DailyReportGenerator(LogEntryParser parser, TrafficAggregator aggregator) {
        this.parser = Objects.requireNonNull(parser, "parser");
        this.aggregator = Objects.requireNonNull(aggregator, "aggregator");
    }

    public Path generate(Path logFile, Path reportDirectory, OutputFormat format) throws IOException {
        TrafficReport report = read(logFile);
        Files.createDirectories(reportDirectory);
        Path target = reportDirectory.resolve(format.fileName(REPORT_NAME));
        Path draft = Files.createTempFile(reportDirectory, REPORT_NAME, ".tmp");
        try {
            try (Writer writer = Files.newBufferedWriter(draft)) {
                format.formatter().write(report, writer);
            }
            return Files.move(draft, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } finally {
            Files.deleteIfExists(draft);
        }
    }

    private TrafficReport read(Path logFile) throws IOException {
        try (Stream<String> lines = Files.lines(logFile)) {
            return aggregator.aggregate(lines.map(parser::parse).flatMap(Optional::stream));
        }
    }
}

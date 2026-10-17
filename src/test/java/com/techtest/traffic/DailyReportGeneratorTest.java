package com.techtest.traffic;

import com.techtest.traffic.format.OutputFormat;
import com.techtest.traffic.log.LogEntryParser;
import com.techtest.traffic.report.TrafficAggregator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DailyReportGeneratorTest {

    private final DailyReportGenerator generator =
            new DailyReportGenerator(new LogEntryParser(), new TrafficAggregator());

    @TempDir
    private Path workspace;

    @Test
    void writesCsvReportFromLogFile() throws IOException {
        Path logFile = Files.write(workspace.resolve("requests.log"), List.of(
                "2024-10-01T10:00:00Z;1000;OK;192.168.0.1",
                "2024-10-01T10:00:01Z;3000;OK;192.168.0.2",
                "2024-10-01T10:00:02Z;1000;OK;192.168.0.2",
                "2024-10-01T10:00:03Z;5000;Not Found;192.168.0.3",
                "corrupted line"));
        Path reportDirectory = workspace.resolve("reports");

        Path report = generator.generate(logFile, reportDirectory, OutputFormat.CSV);

        assertEquals(reportDirectory.resolve("ipaddr.csv"), report);
        assertEquals(List.of(
                "IP Address,Number of requests,Percentage of requests,Total bytes sent,Percentage of bytes",
                "192.168.0.2,2,66.67,4000,80.00",
                "192.168.0.1,1,33.33,1000,20.00"
        ), Files.readAllLines(report));
        try (var files = Files.list(reportDirectory)) {
            assertEquals(List.of(report), files.toList());
        }
    }

    @Test
    void writesJsonReportAndReplacesPreviousRun() throws IOException {
        Path logFile = Files.write(workspace.resolve("requests.log"), List.of("t;10;OK;10.0.0.1"));

        generator.generate(logFile, workspace, OutputFormat.JSON);
        Path report = generator.generate(logFile, workspace, OutputFormat.JSON);

        assertEquals(workspace.resolve("ipaddr.json"), report);
        assertTrue(Files.readString(report).contains("\"ipAddress\": \"10.0.0.1\""));
    }

    @Test
    void failsWhenLogFileIsMissing() {
        assertThrows(NoSuchFileException.class,
                () -> generator.generate(workspace.resolve("missing.log"), workspace, OutputFormat.CSV));
    }
}

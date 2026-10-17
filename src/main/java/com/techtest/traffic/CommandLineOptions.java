package com.techtest.traffic;

import com.techtest.traffic.format.OutputFormat;

import java.nio.file.Path;

record CommandLineOptions(Path logFile, Path reportDirectory, OutputFormat format) {

    static final String USAGE = "Usage: ipaddr-report [--format=csv|json] [--log-file=<path>] [--report-dir=<path>]";

    private static final Path DEFAULT_LOG_FILE = Path.of("/logfiles/requests.log");
    private static final Path DEFAULT_REPORT_DIRECTORY = Path.of("/reports");

    static CommandLineOptions parse(String... args) {
        Path logFile = DEFAULT_LOG_FILE;
        Path reportDirectory = DEFAULT_REPORT_DIRECTORY;
        OutputFormat format = OutputFormat.CSV;

        for (String arg : args) {
            String[] option = arg.split("=", 2);
            if (option.length != 2 || option[1].isBlank()) {
                throw new IllegalArgumentException("Invalid option '%s'".formatted(arg));
            }
            switch (option[0]) {
                case "--format" -> format = OutputFormat.fromName(option[1]);
                case "--log-file" -> logFile = Path.of(option[1]);
                case "--report-dir" -> reportDirectory = Path.of(option[1]);
                default -> throw new IllegalArgumentException("Unknown option '%s'".formatted(option[0]));
            }
        }
        return new CommandLineOptions(logFile, reportDirectory, format);
    }
}

package com.techtest.traffic;

import com.techtest.traffic.format.OutputFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandLineOptionsTest {

    @Test
    void defaultsToSpecifiedPathsAndCsv() {
        assertEquals(
                new CommandLineOptions(Path.of("/logfiles/requests.log"), Path.of("/reports"), OutputFormat.CSV),
                CommandLineOptions.parse());
    }

    @Test
    void overridesEveryOption() {
        assertEquals(
                new CommandLineOptions(Path.of("in.log"), Path.of("out"), OutputFormat.JSON),
                CommandLineOptions.parse("--format=json", "--log-file=in.log", "--report-dir=out"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"--format", "--format=", "--format=xml", "--verbose=true", "json"})
    void rejectsInvalidArguments(String argument) {
        assertThrows(IllegalArgumentException.class, () -> CommandLineOptions.parse(argument));
    }
}

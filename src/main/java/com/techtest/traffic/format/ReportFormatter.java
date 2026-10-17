package com.techtest.traffic.format;

import com.techtest.traffic.report.TrafficReport;

import java.io.IOException;
import java.io.Writer;

public interface ReportFormatter {

    void write(TrafficReport report, Writer writer) throws IOException;
}

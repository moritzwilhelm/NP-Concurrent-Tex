package com.pseuco.np19.project.launcher.cli;

import com.pseuco.np19.project.launcher.Configuration;
import com.pseuco.np19.project.launcher.printer.Printer;

import java.io.Reader;
import java.io.Writer;

/**
 * A unit represents a typesetting task with a given configuration, input, and output.
 */
public class Unit {
    private final Configuration configuration;

    private final Reader inputReader;
    private final Writer outputWriter;

    private final Printer printer;

    Unit(Configuration configuration, Reader inputReader, Writer outputWriter) {
        this.configuration = configuration;
        this.inputReader = inputReader;
        this.outputWriter = outputWriter;
        this.printer = new Printer(this.configuration.getGeometry(), this.configuration.getFont(), this.outputWriter);
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public Reader getInputReader() {
        return this.inputReader;
    }

    public Writer getOutputWriter() {
        return this.outputWriter;
    }

    public Printer getPrinter() {
        return this.printer;
    }
}

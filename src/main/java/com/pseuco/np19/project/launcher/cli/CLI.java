package com.pseuco.np19.project.launcher.cli;

import com.google.gson.*;
import com.pseuco.np19.project.launcher.Configuration;
import com.pseuco.np19.project.launcher.font.FontNotFoundException;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.LinkedList;
import java.util.List;

public class CLI {
    private static final Gson gson = new Gson();

    private static final FileSystem fileSystem = FileSystems.getDefault();

    private static Configuration configurationFromArgument(String argument) throws FontNotFoundException, IOException {
        final Reader reader = new FileReader(fileSystem.getPath(argument).toFile(), StandardCharsets.UTF_8);
        return gson.fromJson(reader, Configuration.Builder.class).create();
    }

    private static Socket socketFromArgument(String argument) throws IOException {
        final URI uri = URI.create(argument);
        return new Socket(uri.getHost(), uri.getPort());
    }

    public static void printUsage(PrintStream printStream) {
        printStream.println("Usage: (<configuration> ('socket' <address> | <input> <output>))+");
    }

    /**
     * Parses the commandline arguments into a list of units.
     *
     * @param arguments The commandline arguments as a {@link String}-Array.
     * @return A list of units to typeset.
     * @throws CLIException Thrown when there is an error with the command line arguments.
     * @throws IOException Thrown when there is some IO error.
     */
    public static List<Unit> parseArgs(String[] arguments) throws CLIException, IOException {
        final List<Unit> tasks = new LinkedList<>();

        int index = 0;
        while (index < arguments.length) {
            final Configuration configuration;
            try {
                configuration = configurationFromArgument(arguments[index++]);
            } catch (FontNotFoundException error) {
                throw new CLIException("Unable to find font specified in configuration file!");
            }

            final Reader inputReader;
            final Writer outputWriter;

            if (index >= arguments.length) {
                throw new CLIException("Missing argument after configuration file!");
            }

            if (arguments[index].equals("socket")) {
                index++;
                if (index >= arguments.length) {
                    throw new CLIException("Missing socket URI!");
                }
                final Socket socket = socketFromArgument(arguments[index++]);
                inputReader = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
                outputWriter = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            } else {
                if (arguments[index].equals("-")) {
                    inputReader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
                } else {
                    inputReader = new FileReader(fileSystem.getPath(arguments[index]).toFile(), StandardCharsets.UTF_8);
                }
                index++;
                if (index >= arguments.length) {
                    throw new CLIException("Missing output file!");
                }
                outputWriter = new FileWriter(fileSystem.getPath(arguments[index++]).toFile(), StandardCharsets.UTF_8);
            }

            tasks.add(new Unit(configuration, inputReader, outputWriter));
        }

        return tasks;
    }
}

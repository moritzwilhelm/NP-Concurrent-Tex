package com.pseuco.np19.project.launcher.parser;

/**
 * Represents a position in an input document.
 */
public class Position {
    private final int line;
    private final int column;

    Position(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }
}

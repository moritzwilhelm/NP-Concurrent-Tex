package com.pseuco.np19.project.launcher.font;

public class Glyph {
    private final float width;
    private final String value;

    public Glyph(float width, String value) {
        this.width = width;
        this.value = value;
    }

    public float getWidth() {
        return this.width;
    }

    public String getValue() {
        return this.value;
    }
}

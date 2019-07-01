package com.pseuco.np19.project.launcher.font;

import java.util.Objects;

public class FontSpec {
    private final String name;
    private final int size;

    public FontSpec(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        FontSpec fontSpec = (FontSpec) other;
        return this.size == fontSpec.size && Objects.equals(this.name, fontSpec.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.size);
    }
}

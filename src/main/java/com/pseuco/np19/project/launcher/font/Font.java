package com.pseuco.np19.project.launcher.font;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Represents a font.
 */
public class Font {
    private static final Gson gson = new Gson();

    /**
     * Loads a font from a font-file.
     *
     * @param reader The {@link Reader} to read the font-file from.
     * @return The loaded font.
     */
    static Font load(Reader reader) {
        return gson.fromJson(reader, Font.class);
    }

    /**
     * Loads a font from a font-file.
     *
     * @param inputStream The {@link InputStream} to read the font-file from.
     * @return The loaded font.
     */
    static Font load(InputStream inputStream) {
        return Font.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    @SerializedName("font_spec")
    private final FontSpec fontSpec;

    private final Map<Character, Glyph> glyphs;

    public Font(FontSpec fontSpec, Map<Character, Glyph> glyphs) {
        this.fontSpec = fontSpec;
        this.glyphs = glyphs;
    }

    public FontSpec getFontSpec() {
        return this.fontSpec;
    }

    public Glyph getGlyph(Character character){
        return this.glyphs.get(character);
    }

    /**
     * Measures the width of a given piece of text.
     *
     * @param text The piece of text.
     * @return The width in this font.
     */
    public double measureText(String text) {
        return text.chars().mapToDouble(character -> this.getGlyph((char) character).getWidth()).sum();
    }
}

package com.pseuco.np19.project.launcher.font;

import java.util.HashMap;

public class FontRegistry {
    private static FontRegistry defaultRegistry = new FontRegistry();

    static {
        defaultRegistry.register(StandardFonts.SOURCE_SERIF_PRO_12);
    }

    public static FontRegistry getDefaultRegistry() {
        return FontRegistry.defaultRegistry;
    }

    private final HashMap<FontSpec, Font> fonts = new HashMap<>();

    public void register(Font font) {
        this.fonts.put(font.getFontSpec(), font);
    }

    public Font getFont(FontSpec fontSpec) throws FontNotFoundException {
        Font font = this.fonts.get(fontSpec);
        if (font == null) {
            throw new FontNotFoundException(fontSpec);
        }
        return font;
    }
}

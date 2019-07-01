package com.pseuco.np19.project.launcher.font;

public class StandardFonts {
    private static final ClassLoader loader = StandardFonts.class.getClassLoader();

    public static final Font SOURCE_SERIF_PRO_12 = Font.load(loader.getResourceAsStream("font_source_serif_pro_12.json"));
}

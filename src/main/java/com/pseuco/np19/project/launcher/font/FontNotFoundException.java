package com.pseuco.np19.project.launcher.font;

public class FontNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	FontNotFoundException(FontSpec fontSpec) {
        super("Font \"" + fontSpec.getName() + "\" with size " + fontSpec.getSize() + " not found!");
    }
}

package com.pseuco.np19.project.launcher.render;

/**
 * A text {@link Renderable}. Renders a piece of text at a given position.
 */
public class Text implements Renderable {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    public String toString() {
        return "<Text text='" + this.text + "'>";
    }

    @Override
    public void render(Surface surface, double x, double y) {
        surface.renderText(this.text, x, y);
    }
}

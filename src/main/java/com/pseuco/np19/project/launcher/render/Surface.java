package com.pseuco.np19.project.launcher.render;

/**
 * A surface on which {@link Renderable}s can render themselves.
 */
public interface Surface {
    /**
     * Renders a chunk of text at the given position on the surface.
     *
     * @param text The chunk of text to render on the surface.
     * @param x The <i>x</i>-coordinate to render the text at.
     * @param y The <i>y</i>-coordinate to render the text at.
     */
    void renderText(String text, double x, double y);
}

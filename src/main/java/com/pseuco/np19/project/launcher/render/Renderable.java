package com.pseuco.np19.project.launcher.render;

/**
 * A {@link Renderable} can be rendered on a {@link Surface}.
 */
public interface Renderable {
    /**
     * Renders the {@link Renderable} at the given position on the surface.
     *
     * @param surface The surface to render the {@link Renderable} on.
     * @param x The <i>x</i>-coordinate to render the {@link Renderable} at.
     * @param y The <i>y</i>-coordinate to render the {@link Renderable} at.
     */
    void render(Surface surface, double x, double y);
}

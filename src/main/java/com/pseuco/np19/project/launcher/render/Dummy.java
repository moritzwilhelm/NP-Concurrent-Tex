package com.pseuco.np19.project.launcher.render;

/**
 * A dummy {@link Renderable} that does not render anything.
 */
public class Dummy implements Renderable {
    public static final Renderable DUMMY = new Dummy();

    @Override
    public void render(Surface surface, double x, double y) {

    }
}

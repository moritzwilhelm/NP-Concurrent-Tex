package com.pseuco.np19.project.launcher.render;

import com.pseuco.np19.project.launcher.breaker.Piece;

/**
 * A vertical container used to render pages.
 */
public class VContainer extends Container {
    public VContainer(Piece<Renderable> piece) {
        super(Direction.VERTICAL, piece);
    }
}

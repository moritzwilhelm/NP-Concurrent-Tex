package com.pseuco.np19.project.launcher.render;

import com.pseuco.np19.project.launcher.breaker.Piece;

/**
 * A horizontal container used to render lines.
 */
public class HContainer extends Container {
    public HContainer(Piece<Renderable> piece) {
        super(Direction.HORIZONTAL, piece);
    }
}

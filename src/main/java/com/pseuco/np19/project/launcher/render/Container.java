package com.pseuco.np19.project.launcher.render;

import com.pseuco.np19.project.launcher.breaker.Piece;
import com.pseuco.np19.project.launcher.breaker.item.Item;

/**
 * A container {@link Renderable} that renders a {@link Piece} of {@link Renderable}s.
 */
public class Container implements Renderable {
    public enum Direction {
        HORIZONTAL, VERTICAL
    }

    private final Direction direction;
    private final Piece<Renderable> piece;

    Container(Direction direction, Piece<Renderable> piece) {
        this.direction = direction;
        this.piece = piece;
    }

    @Override
    public void render(Surface surface, double x, double y) {
        final double ratio = this.piece.getRatio();
        for (Item<Renderable> item : this.piece.getItems()) {
            if (item.hasValue()) {
                item.getValue().render(surface, x, y);
            }
            final double factor = ratio < 0 ? item.getShrink() : item.getStretch();
            switch (this.direction) {
                case HORIZONTAL:
                    x += item.getSize() + ratio * factor;
                    break;
                case VERTICAL:
                    y += item.getSize() + ratio * factor;
                    break;
            }
        }
    }
}

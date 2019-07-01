package com.pseuco.np19.project.launcher.breaker;

import com.pseuco.np19.project.launcher.breaker.item.Item;

import java.util.List;

/**
 * Represents a piece of items with a given scaling ratio.
 *
 * @param <T> The inner type of the items.
 */
public class Piece<T> {
    private final double ratio;
    private final List<Item<T>> items;

    /**
     * @param ratio The ratio to scale the piece with.
     * @param items The items of the piece.
     */
    Piece(double ratio, List<Item<T>> items) {
        this.ratio = ratio;
        this.items = items;
    }

    public double getRatio() {
        return this.ratio;
    }

    public Iterable<Item<T>> getItems() {
        return this.items;
    }
}

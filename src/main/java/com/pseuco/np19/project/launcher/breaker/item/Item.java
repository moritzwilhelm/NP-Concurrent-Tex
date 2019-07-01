package com.pseuco.np19.project.launcher.breaker.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an item as used by the Knuth-Plass algorithm.
 *
 * Consult the project description for more details about items.
 *
 * @param <T> The inner type of the item.
 */
public abstract class Item<T> {
    /**
     * Slices a sub-list out of a list of items. Used to partition an item-sequence into pieces.
     *
     * @param source The source list.
     * @param start The start position.
     * @param end The end position.
     * @param <T> The inner type of the items.
     * @return Sub-list of the source list which contains only visible items between `start` and `end`.
     */
    public static <T> List<Item<T>> sliceItems(List<Item<T>> source, int start, int end) {
        final List<Item<T>> target = new ArrayList<>();
        boolean seenNonGlue = false;
        for (int index = start; index <= end - 1; index++) {
            final Item<T> item = source.get(index);
            if (item.isGlue()) {
                // we skip all glue at the beginning of each piece
                if (!seenNonGlue) {
                    continue;
                }
            } else {
                seenNonGlue = true;
            }
            // penalties are only visible if they are at the end of a piece
            if (index == end - 1 || !item.isPenalty()) {
                target.add(item);
            }
        }
        return target;
    }

    /**
     * Creates a new {@link Box}-Item.
     *
     * @param size Size of the {@link Box}-Item.
     * @param value Inner value of the {@link Box}-Item.
     * @param <T> The inner type of the item.
     * @return The created {@link Box}-Item.
     */
    public static <T> Item<T> createBox(double size, T value) {
        return new Box<>(size, value);
    }

    /**
     * Creates a new {@link Glue}-Item.
     *
     * @param size Size of the {@link Glue}-Item.
     * @param stretch Stretchability of the {@link Glue}-Item.
     * @param shrink Shrinkability of the {@link Glue}-Item.
     * @param <T> The inner type of the item.
     * @return The created {@link Glue}-Item.
     */
    public static <T> Item<T> createGlue(double size, double stretch, double shrink) {
        return new Glue<>(size, stretch, shrink);
    }

    /**
     * Creates a new {@link Penalty}-Item.
     *
     * @param size Size of the {@link Penalty}-Item.
     * @param value Inner value of the {@link Penalty}-Item.
     * @param penalty Associated penalty.
     * @param flagged Whether the {@link Penalty}-Item is `flagged` or not.
     * @param <T> The inner type of the item.
     * @return The created {@link Penalty}-Item.
     */
    public static <T> Item<T> createPenalty(double size, T value, double penalty, boolean flagged) {
        return new Penalty<>(size, value, penalty, flagged);
    }

    private final double size;

    /**
     * @param size The size of the item.
     */
    Item(double size) {
        this.size = size;
    }

    /**
     * @return Returns the size of the item.
     */
    public double getSize() {
        return this.size;
    }

    /**
     * @return Returns the stretchability of the item.
     */
    public abstract double getStretch();

    /**
     * @return Returns the shrinkability of the item.
     */
    public abstract double getShrink();

    /**
     * @return Returns the penalty associated with the item.
     */
    public abstract double getPenalty();

    /**
     * @return Returns whether the penalty is `flagged` or not.
     */
    public abstract boolean getFlagged();

    /**
     * @return Returns whether the item has an inner value ({@link Box}- and {@link Penalty}-Items).
     */
    public abstract boolean hasValue();

    /**
     * @return Returns the inner value of the item.
     */
    public abstract T getValue();

    /**
     * Accept method for the {@link ItemVisitor}.
     *
     * @param visitor The visitor to visit.
     */
    public abstract void accept(ItemVisitor<T> visitor);

    /**
     * @return Returns whether the item is a {@link Glue}-Item or not.
     */
    public abstract boolean isGlue();

    /**
     * @return Returns whether the item is a {@link Box}-Item or not.
     */
    public abstract boolean isBox();

    /**
     * @return Returns whether the item is a {@link Penalty}-Item or not.
     */
    public abstract boolean isPenalty();
}

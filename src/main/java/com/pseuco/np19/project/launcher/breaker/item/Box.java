package com.pseuco.np19.project.launcher.breaker.item;

/**
 * Represents a {@link Box}-Item.
 *
 * @param <T> The inner type of the item.
 */
public class Box<T> extends ValueItem<T> {
    /**
     * @param size The size of the {@link Box}-Item.
     * @param value The inner value of the {@link Box}-Item.
     */
    Box(double size, T value) {
        super(size, value);
    }

    public String toString() {
        return "<Box size=" + this.getSize() + " value=" + this.getValue() + ">";
    }

    @Override
    public double getStretch() {
        return 0;
    }

    @Override
    public double getShrink() {
        return 0;
    }

    @Override
    public double getPenalty() {
        return 0;
    }

    @Override
    public boolean getFlagged() {
        return false;
    }

    @Override
    public void accept(ItemVisitor<T> visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isGlue() {
        return false;
    }

    @Override
    public boolean isBox() {
        return true;
    }

    @Override
    public boolean isPenalty() {
        return false;
    }
}

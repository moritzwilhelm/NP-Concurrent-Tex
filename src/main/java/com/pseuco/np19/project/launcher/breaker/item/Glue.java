package com.pseuco.np19.project.launcher.breaker.item;

/**
 * Represents a {@link Glue}-Item.
 *
 * @param <T> The inner type of the item.
 */
public class Glue<T> extends Item<T> {
    private final double stretch;
    private final double shrink;

    /**
     * @param size The size of the {@link Glue}-Item.
     * @param stretch The stretchability of the {@link Glue}-Item.
     * @param shrink The shrinkability of the {@link Glue}-Item.
     */
    Glue(double size, double stretch, double shrink) {
        super(size);
        this.stretch = stretch;
        this.shrink = shrink;
    }

    public String toString() {
        return "<Glue size=" + this.getSize() + " stretch=" + this.stretch + " shrink=" + this.shrink + ">";
    }

    @Override
    public double getStretch() {
        return this.stretch;
    }

    @Override
    public double getShrink() {
        return this.shrink;
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
    public boolean hasValue() {
        return false;
    }

    @Override
    public T getValue() {
        throw new RuntimeException("Glue does not have a value attached to it!");
    }

    @Override
    public void accept(ItemVisitor<T> visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isGlue() {
        return true;
    }

    @Override
    public boolean isBox() {
        return false;
    }

    @Override
    public boolean isPenalty() {
        return false;
    }
}

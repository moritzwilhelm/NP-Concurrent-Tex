package com.pseuco.np19.project.launcher.breaker.item;

/**
 * Represents a {@link Penalty}-Item.
 *
 * @param <T> The inner type of the item.
 */
public class Penalty<T> extends ValueItem<T> {
    private final double penalty;
    private final boolean flagged;

    /**
     * @param size Size of the {@link Penalty}-Item.
     * @param value Inner value of the {@link Penalty}-Item.
     * @param penalty Associated penalty.
     * @param flagged Whether the {@link Penalty}-Item is `flagged` or not.
     */
    Penalty(double size, T value, double penalty, boolean flagged) {
        super(size, value);
        this.penalty = penalty;
        this.flagged = flagged;
    }

    public String toString() {
        return "<Penalty size=" + this.getSize() + " penalty=" + this.penalty + " flagged=" + this.flagged + ">";
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
        return this.penalty;
    }

    @Override
    public boolean getFlagged() {
        return this.flagged;
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
        return false;
    }

    @Override
    public boolean isPenalty() {
        return true;
    }
}

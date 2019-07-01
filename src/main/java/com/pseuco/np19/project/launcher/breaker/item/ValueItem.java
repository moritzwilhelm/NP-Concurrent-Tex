package com.pseuco.np19.project.launcher.breaker.item;

/**
 * Abstract base-class for items with attached values ({@link Box}- and {@link Penalty}-Items).
 *
 * @param <T> The inner type of the item.
 */
abstract class ValueItem<T> extends Item<T> {
    private final T value;

    ValueItem(double size, T value) {
        super(size);
        this.value = value;
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public T getValue() {
        return this.value;
    }
}

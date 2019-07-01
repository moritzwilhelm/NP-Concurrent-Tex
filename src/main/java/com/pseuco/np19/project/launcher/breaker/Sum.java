package com.pseuco.np19.project.launcher.breaker;

import com.pseuco.np19.project.launcher.breaker.item.Item;

/**
 * Helper class for the accumulation of sizes.
 */
class Sum {
    private final double size;
    private final double stretch;
    private final double shrink;

    Sum() {
        this.size = 0;
        this.stretch = 0;
        this.shrink = 0;
    }

    private Sum(double size, double stretch, double shrink) {
        this.size = size;
        this.stretch = stretch;
        this.shrink = shrink;
    }

    public double getSize() {
        return this.size;
    }

    double getStretch() {
        return this.stretch;
    }

    double getShrink() {
        return this.shrink;
    }

    public <T> Sum add(Item<T> item) {
        return new Sum(this.size + item.getSize(), this.stretch + item.getStretch(), this.shrink + item.getShrink());
    }

    public Sum add(Sum sum) {
        return new Sum(this.size + sum.size, this.stretch + sum.stretch, this.shrink + sum.shrink);
    }

    public <T> Sum sub(Item<T> item) {
        return new Sum(this.size - item.getSize(), this.stretch - item.getStretch(), this.shrink - item.getShrink());
    }

    public Sum sub(Sum sum) {
        return new Sum(this.size - sum.size, this.stretch - sum.stretch, this.shrink - sum.shrink);
    }
}

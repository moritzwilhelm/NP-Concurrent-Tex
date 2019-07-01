package com.pseuco.np19.project.launcher.breaker;

import com.pseuco.np19.project.launcher.breaker.item.Item;

import java.util.*;

import static com.pseuco.np19.project.launcher.breaker.item.Item.sliceItems;

/**
 * Represents a breakpoint at a given position in an item sequence.
 */
class Breakpoint {
    static final Comparator<Breakpoint> DEMERITS_COMPARATOR = Comparator.comparing(Breakpoint::getDemerits);

    // the position of the breakpoint
    private final int position;
    // the fitness class of the piece preceding the breakpoint
    private final Fitness fitness;

    private boolean flagged;
    private double ratio;
    private double demerits;
    private Breakpoint parent;
    private Sum total;

    /**
     * @param position The position of the breakpoint.
     * @param fitness The fitness class of the piece preceding the breakpoint.
     */
    Breakpoint(int position, Fitness fitness) {
        this.position = position;
        this.fitness = fitness;
    }

    /**
     * Flattens the breakpoint tree.
     *
     * @return A list of breakpoints obtained by flattening the tree.
     */
    private List<Breakpoint> flatten() {
        Breakpoint current = this;
        List<Breakpoint> breakpoints = new ArrayList<>();
        while (current != null) {
            breakpoints.add(current);
            current = current.getParent();
        }
        Collections.reverse(breakpoints);
        return breakpoints;
    }

    /**
     * Chops an item-sequence into pieces based on the breakpoint tree.
     *
     * @param items The item-sequence this breakpoint refers to.
     * @param <T> The inner type of the items.
     * @return A list of pieces obtained by partitioning the item sequence.
     */
    <T> List<Piece<T>> getPieces(List<Item<T>> items) {
        List<Piece<T>> pieces = new ArrayList<>();
        Iterator<Breakpoint> iterator = this.flatten().iterator();
        int previousPosition = iterator.next().getPosition();
        while (iterator.hasNext()) {
            Breakpoint next = iterator.next();
            Piece<T> piece = new Piece<>(next.getRatio(), sliceItems(items, previousPosition, next.getPosition()));
            pieces.add(piece);
            previousPosition = next.getPosition();
        }
        return pieces;
    }

    /**
     * @return The position of the breakpoint in the item sequence.
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * @return The fitness class of the piece preceding the breakpoint.
     */
    Fitness getFitness() {
        return this.fitness;
    }

    /**
     * @return Whether the breakpoint is flagged or not (breakpoint at a flagged Penalty-Item.
     */
    boolean getFlagged() {
        return this.flagged;
    }

    void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    /**
     * @return The scaling ratio of the piece preceding the breakpoint.
     */
    private double getRatio() {
        return this.ratio;
    }

    void setRatio(double ratio) {
        this.ratio = ratio;
    }

    /**
     * @return The accumulated demerits at the breakpoint.
     */
    double getDemerits() {
        return this.demerits;
    }

    void setDemerits(double demerits) {
        this.demerits = demerits;
    }

    /**
     * @return The parent breakpoint.
     */
    private Breakpoint getParent() {
        return this.parent;
    }

    void setParent(Breakpoint parent) {
        this.parent = parent;
    }

    /**
     * @return The accumulated size, stretchability, and shrinkability at the breakpoint.
     */
    Sum getTotal() {
        return this.total;
    }

    void setTotal(Sum total) {
        this.total = total;
    }
}

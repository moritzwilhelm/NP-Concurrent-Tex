package com.pseuco.np19.project.launcher.breaker.item;

/**
 * Interface for the implementation of the visitor-pattern for {@link Item}s.
 *
 * @param <T> The inner type of the items to visit.
 */
public interface ItemVisitor<T> {
    void visit(Box<T> box);
    void visit(Glue<T> glue);
    void visit(Penalty<T> penalty);
}

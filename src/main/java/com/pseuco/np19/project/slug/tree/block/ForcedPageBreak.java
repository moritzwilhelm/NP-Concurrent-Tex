package com.pseuco.np19.project.slug.tree.block;

/**
 * A {@link BlockElement} representing a forced-page-break.
 */
public class ForcedPageBreak extends BlockElement {
    @Override
    public void accept(IBlockVisitor visitor) {
        visitor.visit(this);
    }
}

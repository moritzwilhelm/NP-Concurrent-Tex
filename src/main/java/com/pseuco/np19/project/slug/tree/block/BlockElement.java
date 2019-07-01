package com.pseuco.np19.project.slug.tree.block;

public abstract class BlockElement {
    public abstract void accept(IBlockVisitor visitor);
}

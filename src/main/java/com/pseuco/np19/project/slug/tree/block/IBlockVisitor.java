package com.pseuco.np19.project.slug.tree.block;

public interface IBlockVisitor {
    void visit(Paragraph paragraph);
    void visit(ForcedPageBreak forcedPageBreak);
}

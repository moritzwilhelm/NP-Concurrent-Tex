package com.pseuco.np19.project.rocket.block;

public interface IBlockVisitor {
	void visit(Paragraph paragraph);

	void visit(ForcedPageBreak forcedPageBreak);
}

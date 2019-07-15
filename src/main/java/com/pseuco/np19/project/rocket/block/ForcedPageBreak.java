package com.pseuco.np19.project.rocket.block;

/**
 * A {@link BlockElement} representing a forced-page-break.
 */
public class ForcedPageBreak extends BlockElement {

	public ForcedPageBreak(int segment, int index) {
		super(segment, index);
	}

	@Override
	public void accept(IBlockVisitor visitor) {
		visitor.visit(this);
	}
}

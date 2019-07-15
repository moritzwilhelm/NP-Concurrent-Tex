package com.pseuco.np19.project.rocket.block;

public abstract class BlockElement {

	private int currentSegment, currentIndex;

	protected BlockElement(int segment, int index) {
		currentSegment = segment;
		currentIndex = index;
	}

	public abstract void accept(IBlockVisitor visitor);

	public int getCurrentSegment() {
		return currentSegment;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

}

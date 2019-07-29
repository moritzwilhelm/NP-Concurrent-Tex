package com.pseuco.np19.project.rocket.monitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.render.Renderable;

/**
 * Tuple consisting of id, HashMap of BlockElements and size(when completely filled)
 */

public class Segment {

	private final int id;

	private final Map<Integer, List<Item<Renderable>>> blockElements = new HashMap<>();

	private int sizeWhenDone = -1;

	public Segment(int id) {
		this.id = id;
	}

	public synchronized int getID() {
		return id;
	}

	public synchronized Map<Integer, List<Item<Renderable>>> getBlockElements() {
		return blockElements;
	}

	public synchronized void setSizeWhenDone(int sizeWhenDone) {
		this.sizeWhenDone = sizeWhenDone;
	}

	/**
	 * puts items into segment and checks for completion
	 * @param index, the index at which items shall be put
	 * @param items, the items to be put
	 * @return true,  if this segment is complete after that
	 */
	public synchronized boolean isCompleteAfterPut(int index, List<Item<Renderable>> items) {
		blockElements.put(index, items);
		return blockElements.size() == sizeWhenDone;
	}

}
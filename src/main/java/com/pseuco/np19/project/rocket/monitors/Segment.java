package com.pseuco.np19.project.rocket.monitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.render.Renderable;

// tuple consisting of HashMap and size(when completely filled)
public class Segment {
	private Map<Integer, List<Item<Renderable>>> blockElements = new HashMap<>();

	private int sizeWhenDone = Integer.MAX_VALUE;

	private boolean last = false;

	public int getSize() {
		return blockElements.size();
	}

	public int getSizeWhenDone() {
		return sizeWhenDone;
	}

	public void setSizeWhenDone(int sizeWhenDone) {
		this.sizeWhenDone = sizeWhenDone;
	}

	public void add(int index, List<Item<Renderable>> items) {
		blockElements.put(index, items);
	}

	public List<Item<Renderable>> get(int index) {
		return blockElements.get(index);
	}

	public void setLast() {
		this.last = true;
	}

	public boolean isLast() {
		return last;
	}

}
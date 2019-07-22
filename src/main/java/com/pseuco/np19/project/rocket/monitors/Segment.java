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

	public synchronized int getSize() {
		return blockElements.size();
	}

	public synchronized int getSizeWhenDone() {
		return sizeWhenDone;
	}

	public synchronized void setSizeWhenDone(int sizeWhenDone) {
		this.sizeWhenDone = sizeWhenDone;
	}

	public synchronized void add(int index, List<Item<Renderable>> items) {
		blockElements.put(index, items);
	}

	public synchronized List<Item<Renderable>> get(int index) {
		return blockElements.get(index);
	}

	public synchronized void setLast() {
		this.last = true;
	}

	public synchronized boolean isLast() {
		return last;
	}

}
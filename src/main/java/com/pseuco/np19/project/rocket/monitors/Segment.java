package com.pseuco.np19.project.rocket.monitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.render.Renderable;

// tuple consisting of HashMap and size(when completely filled)
public class Segment {

	private final int id;

	private final Map<Integer, List<Item<Renderable>>> blockElements = new HashMap<>();

	private int sizeWhenDone = Integer.MAX_VALUE;

	public Segment(int id) {
		this.id = id;
	}

	public synchronized int getID() {
		return id;
	}

	public synchronized Map<Integer, List<Item<Renderable>>> getBlockElements() {
		return blockElements;
	}

	public synchronized void put(int index, List<Item<Renderable>> items) {
		blockElements.put(index, items);
	}

	public synchronized void setSizeWhenDone(int sizeWhenDone) {
		this.sizeWhenDone = sizeWhenDone;
	}

	public synchronized boolean isFinished() {
		return blockElements.size() == sizeWhenDone;
	}

}
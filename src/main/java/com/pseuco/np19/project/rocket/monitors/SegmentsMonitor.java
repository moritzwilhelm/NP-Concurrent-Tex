package com.pseuco.np19.project.rocket.monitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.render.Renderable;

public class SegmentsMonitor {
	// mapping of Integer to Segment (segment consists of list of paragraphs)
	private Map<Integer, Segment> segments = new HashMap<>();

	// tuple consisting of ArrayList and size(when completely filled)
	private class Segment {
		private ArrayList<LinkedList<Item<Renderable>>> blockElements = new ArrayList<>();

		private int sizeWhenDone;

		public int getSize() {
			return blockElements.size();
		}

		public int getSizeWhenDone() {
			return sizeWhenDone;
		}

		public void setSizeWhenDone(int sizeWhenDone) {
			this.sizeWhenDone = sizeWhenDone;
		}

		public void add(int index, LinkedList<Item<Renderable>> items) {
			blockElements.add(index, items);
		}

		public LinkedList<Item<Renderable>> get(int index) {
			return blockElements.get(index);
		}
	}

	public synchronized void putNewSegment(int index) {
		segments.put(Integer.valueOf(index), new Segment());
	}

	/*
	 * // Problematic??? public synchronized Segment getSegment(int index) { return
	 * segments.get(Integer.valueOf(index)); }
	 */

	public synchronized void addBlockElement(int segment, int index, LinkedList<Item<Renderable>> items) {
		segments.get(segment).add(index, items);
		notifyAll();
	}

	public synchronized LinkedList<Item<Renderable>> getBlockElement(int segment, int index) {
		return segments.get(segment).get(index);
	}

	public synchronized void finishSegment(int segment, int value) {
		segments.get(segment).setSizeWhenDone(value);
	}

	// waits until complete, then returns true
	public synchronized boolean isComplete(int segment) {
		while (!(segments.get(segment).getSize() == segments.get(segment).getSizeWhenDone()))
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("InterruptedException in SegmentsMonitor!");
			}
		return true;

	}
}

package com.pseuco.np19.project.rocket.monitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.render.Renderable;

public class SegmentsMonitor {
	// mapping of Integer to Segment (segment consists of list of paragraphs)
	private Map<Integer, Segment> segments = new HashMap<>();
	private int numSegments = 0;

	public SegmentsMonitor() {
		this.putNewSegment();
	}

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

	public synchronized void putNewSegment() {
		segments.put(Integer.valueOf(numSegments++), new Segment());
	}

	public synchronized Segment getSegment(int index) {
		return segments.get(Integer.valueOf(index));
	}

	public synchronized boolean addBlockElement(int segment, int index, List<Item<Renderable>> items) {
		segments.get(segment).add(index, items);
		return isComplete(segment);
	}

	public synchronized List<Item<Renderable>> getBlockElement(int segment, int index) {
		return segments.get(segment).get(index);
	}

	public synchronized void finishSegment(int segment, int size) {
		// System.out.println("segment : " + segment + " Groesse: " + size);
		segments.get(Integer.valueOf(segment)).setSizeWhenDone(size);
	}

	public synchronized boolean isComplete(int segment) {
		/*
		 * System.out.println( "complete check " + segments.get(segment).getSize() + " "
		 * + segments.get(segment).getSizeWhenDone());
		 */
		return segments.get(segment).getSize() == segments.get(segment).getSizeWhenDone();

	}
}

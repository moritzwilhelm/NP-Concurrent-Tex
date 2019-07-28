package com.pseuco.np19.project.rocket.monitors;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.pseuco.np19.project.launcher.cli.Unit;

/**
 * Collection of metadata of the unit (including the unit itself)
 */

public class Metadata {

	private final Unit unit;

	private final ExecutorService executor;

	private final Lock lock;

	private final Condition terminating;

	// number of segments
	private int numSegments = -1;

	// index of the currently to be printed page
	private int printIndex = 0;

	private HashSet<Integer> finishedSegments = new HashSet<>();

	// true if an error was encountered while processing the unit
	private boolean broken = false;

	public Metadata(Unit unit, ExecutorService executor, Lock lock, Condition terminating) {
		this.unit = unit;
		this.executor = executor;
		this.lock = lock;
		this.terminating = terminating;
	}

	public Unit getUnit() {
		return unit;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public synchronized int getNumSegments() {
		return numSegments;
	}

	public synchronized void setNumSegments(int numSegments) {
		this.numSegments = numSegments;
	}

	public synchronized int getPrintIndex() {
		return printIndex;
	}

	/*
	 * adds SegmentID to finishedSegments and returns if this segment may be printed
	 */
	public synchronized boolean isNextToBePrinted(int ID) {
		finishedSegments.add(ID);
		return ID == printIndex;
	}

	/*
	 * sets printIndex to given value and returns if the segment already passed the
	 * isNextToBePrinted check
	 */
	public synchronized boolean setPrintIndexAndIsPresent(int printIndex) {
		this.printIndex = printIndex;
		return finishedSegments.contains(printIndex);
	}

	public synchronized boolean isBroken() {
		return broken;
	}

	public synchronized void setBroken() {
		broken = true;
	}

	/**
	 * signal waiting UnitThread that it can start termination
	 */
	public synchronized void initiateTermination() {
		try {
			lock.lock();
			executor.shutdown(); // prevent submission of any new Runnable
			terminating.signal();
		} finally {
			lock.unlock();
		}
	}
}

package com.pseuco.np19.project.rocket.monitors;

import java.util.HashSet;
import java.util.Set;
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

	// true if an error was encountered while processing the unit
	private boolean broken = false;

	// index of the currently to be printed page
	private int printIndex = 0;

	// set of Segments which are rendered but not yet printed
	private final Set<Integer> notYetPrintedSegments = new HashSet<>();

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

	public synchronized boolean isBroken() {
		return broken;
	}

	public synchronized void setBroken() {
		broken = true;
	}

	public synchronized int getPrintIndex() {
		return printIndex;
	}

	/**
	 * returns if this segments may be printed and adds SegmentID to
	 * finishedSegments if this is currently not the case
	 */
	public synchronized boolean isNextToBePrinted(int ID) {
		boolean isNext = ID == printIndex;
		if (!isNext) {
			notYetPrintedSegments.add(ID);
		}
		return isNext;
	}

	/**
	 * sets printIndex to given value and returns if the segment already passed the
	 * isNextToBePrinted check but has not been printed yet
	 */
	public synchronized boolean updatePrintIndex(int printIndex) {
		this.printIndex = printIndex;
		return notYetPrintedSegments.contains(printIndex);
	}

	/**
	 * signal waiting UnitThread that it can start termination
	 */
	public synchronized void initiateTermination() {
		try {
			lock.lock();
			executor.shutdown(); 	// prevent submission of any new Runnable
			terminating.signal();
		} finally {
			lock.unlock();
		}
	}
}

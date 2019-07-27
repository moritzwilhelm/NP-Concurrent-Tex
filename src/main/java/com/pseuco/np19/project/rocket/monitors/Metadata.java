package com.pseuco.np19.project.rocket.monitors;

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

	public synchronized void setNumSegments(int size) {
		this.numSegments = size;
	}

	public synchronized int getPrintIndex() {
		return printIndex;
	}

	public synchronized void setPrintIndex(int printIndex) {
		this.printIndex = printIndex;
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
			executor.shutdown();		// prevent submitting of any new Runnable
			terminating.signal();
		} finally {
			lock.unlock();
		}
	}
}

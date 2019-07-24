package com.pseuco.np19.project.rocket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.pseuco.np19.project.launcher.cli.Unit;

public class Metadata {

	private final Unit unit;

	private final ExecutorService executor;

	private final Lock lock;

	private final Condition terminating;

	private int size = -1;

	public Metadata(Unit unit, ExecutorService executor, Lock lock, Condition terminating) {
		this.unit = unit;
		this.executor = executor;
		this.lock = lock;
		this.terminating = terminating;
	}

	public synchronized int getSize() {
		return size;
	}

	public synchronized void setSize(int size) {
		this.size = size;
	}

	public Unit getUnit() {
		return unit;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public Lock getLock() {
		return lock;
	}

	public Condition getTerminating() {
		return terminating;
	}

}

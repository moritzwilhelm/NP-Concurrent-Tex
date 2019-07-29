package com.pseuco.np19.project.rocket;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.rocket.monitors.Metadata;

/**
 * Thread representing the processing of a unit
 */

public class UnitThread extends Thread {

	private final Metadata metadata;

	private final Unit unit;

	private final ExecutorService executor;

	// executor.shutdown (needed in order to get a condition)
	private final Lock lock;

	// executor.isShutdown()
	private final Condition terminating;

	public UnitThread(Unit unit) {
		this.unit = unit;
		this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		this.lock = new ReentrantLock();
		this.terminating = lock.newCondition();
		this.metadata = new Metadata(this.unit, this.executor, lock, terminating);
	}

	@Override
	public void run() {

		// Start ParserThread
		metadata.startParser();

		/*
		 * wait until shutdown: 
		 * 1) someone encountered an error OR
		 * 2) printer printed last page
		 */
		try {
			lock.lock();
			while (!executor.isShutdown()) {
				try {
					terminating.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} finally {
			lock.unlock();
		}

		// finally (try to) stop all running tasks (none, if no error was encountered)
		executor.shutdownNow();

		// print error page in case of error
		if (metadata.isBroken()) {
			try {
				synchronized (unit.getPrinter()) {
					unit.getPrinter().printErrorPage();
					unit.getPrinter().finishDocument();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
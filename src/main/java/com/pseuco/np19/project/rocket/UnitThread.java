package com.pseuco.np19.project.rocket;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.parser.Parser;
import com.pseuco.np19.project.rocket.monitors.ConcurrentDocument;
import com.pseuco.np19.project.rocket.monitors.Metadata;

/**
 * Thread representing the processing of a unit
 */

public class UnitThread extends Thread {

	private final Metadata metadata;

	private final Unit unit;

	private final ExecutorService executor;

	private final ConcurrentDocument document;

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
		this.document = new ConcurrentDocument(metadata);
	}

	@Override
	public void run() {

		// Submit ParserRunnable to executor
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Parser.parse(unit.getInputReader(), document);
				} catch (IOException e) {
					e.printStackTrace();
					metadata.setBroken();
					
					// signal waiting UnitThread that an error was encountered (prevents a deadlock)
					metadata.initiateTermination();
				}
			}
		});

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
		
		// finally terminate all running Threads (none, if no error was encountered)
		executor.shutdownNow();

		// print error page in case of error
		if (metadata.isBroken()) {
			try {
				unit.getPrinter().printErrorPage();
				unit.getPrinter().finishDocument();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
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

public class UnitThread extends Thread {

	private final Metadata metadata;

	private final Unit unit;

	private final ExecutorService executor;

	private final Lock lock;

	private final Condition terminating;

	public UnitThread(Unit unit) {
		this.unit = unit;
		// this.executor = Executors.newCachedThreadPool();
		this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		this.lock = new ReentrantLock();
		this.terminating = lock.newCondition();

		this.metadata = new Metadata(this.unit, this.executor, lock, terminating);
	}

	@Override
	public void run() {
		final ConcurrentDocument document = new ConcurrentDocument(metadata);

		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Parser.parse(unit.getInputReader(), document);
					// System.out.println("Parser terminated");
				} catch (IOException e) {
					e.printStackTrace();
					try {
						lock.lock();
						executor.shutdown();
						terminating.signal();
					} finally {
						lock.unlock();
					}
				}
			}
		});

		try {
			lock.lock();
			while (!executor.isShutdown()) {
				try {
					// System.out.println("warte auf condition!");
					terminating.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} finally {
			lock.unlock();
		}

		// System.out.println("Heureka");
		// System.out.println("");

		executor.shutdownNow();

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
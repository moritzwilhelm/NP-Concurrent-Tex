package com.pseuco.np19.project.rocket;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.parser.Parser;
import com.pseuco.np19.project.rocket.monitors.DocumentMonitor;

public class UnitThread extends Thread {
	private final ExecutorService executor;

	private final Unit unit;

	public UnitThread(Unit unit) {
		this.unit = unit;
		// this.executor = Executors.newCachedThreadPool();
		this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	@Override
	public void run() {
		final Lock lock = new ReentrantLock();
		final Condition terminating = lock.newCondition();
		final DocumentMonitor document = new DocumentMonitor(this.unit, this.executor, lock, terminating);

		executor.submit(new UnitThread(this.unit) {
			@Override
			public void run() {
				try {
					Parser.parse(unit.getInputReader(), document);
					// System.out.println("Parser terminated");
				} catch (IOException e) {
					e.printStackTrace();
					executor.shutdownNow();
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
	}
}
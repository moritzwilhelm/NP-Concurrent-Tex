package com.pseuco.np19.project.rocket.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.rocket.monitors.SegmentsMonitor;

public class PrinterTask extends Task {

	protected PrinterTask(Unit unit, ExecutorService executor, SegmentsMonitor segments, Map<Integer, List<Page>> pages,
			AtomicInteger printIndex, int segment, Lock lock, Condition terminating) {
		super(unit, executor, segments, pages, printIndex, segment, lock, terminating);
	}

	@Override
	public void run() {
		// System.out.println("Started printer! " + segment);
		try {
			this.unit.getPrinter().printPages(pages.get(segment));

			// TODO: haesslich (?), schoener machen falls moeglich
			synchronized (this.unit) {
				if (pages.containsKey(segment + 1)) {
					// System.out.println("next");
					executor.submit(new PrinterTask(this.unit, this.executor, segments, pages, printIndex, segment + 1,
							lock, terminating));
				} else {
					this.printIndex.set(segment + 1);
				}
			}
			// System.out.println("segment: " + segment + " last: " + segments.getSegment(segment).isLast());
			if (segments.getSegment(segment).isLast()) {
				// System.out.println("I am the last printer: " + segment);
				this.unit.getPrinter().finishDocument();

				try {
					lock.lock();
					this.executor.shutdown();
					terminating.signal();
				} finally {
					lock.unlock();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

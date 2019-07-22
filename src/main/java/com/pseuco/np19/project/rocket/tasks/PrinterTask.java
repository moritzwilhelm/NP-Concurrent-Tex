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

	private int printed;

	protected PrinterTask(Unit unit, ExecutorService executor, SegmentsMonitor segments, Map<Integer, List<Page>> pages,
			AtomicInteger printIndex, int segment, Lock lock, Condition terminating, int printed) {
		super(unit, executor, segments, pages, printIndex, segment, lock, terminating);
		this.printed = printed;
	}

	@Override
	public void run() {
		// System.out.println("Started printer! " + segment);
		try {
			this.unit.getPrinter().printPages(pages.get(printed));

			// TODO: haesslich (?), schoener machen falls moeglich
			synchronized (this.unit) {
				if (pages.containsKey(++this.printed)) {
					// System.out.println("next");
					executor.submit(new PrinterTask(this.unit, this.executor, segments, pages, printIndex, segment + 1,
							lock, terminating, printed));
				} else {
					this.printIndex.set(printed);
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

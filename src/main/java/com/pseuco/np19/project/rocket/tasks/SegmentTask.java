package com.pseuco.np19.project.rocket.tasks;

import static com.pseuco.np19.project.launcher.breaker.Breaker.breakIntoPieces;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.pseuco.np19.project.launcher.breaker.UnableToBreakException;
import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.rocket.monitors.SegmentsMonitor;

public class SegmentTask extends Task {

	public SegmentTask(Unit unit, ExecutorService executor, SegmentsMonitor segments, Map<Integer, List<Page>> pages,
			AtomicInteger printIndex, int segment, Lock lock, Condition terminating) {
		super(unit, executor, segments, pages, printIndex, segment, lock, terminating);
	}

	@Override
	public void run() {
		// System.out.println("segtask started: " + segment);
		for (int i = 0; i < segments.getSegment(this.segment).getSize(); i++) {
			this.items.addAll(segments.getSegment(segment).get(i));
		}

		try {
			List<Page> renderedPages = this.unit.getPrinter()
					.renderPages(breakIntoPieces(this.configuration.getBlockParameters(), this.items,
							this.configuration.getBlockTolerances(), this.configuration.getGeometry().getTextHeight()));
			pages.put(Integer.valueOf(segment), renderedPages);

			synchronized (this.unit) {
				// System.out.println("Kann ich printen? " + segment);
				if (this.segment == this.printIndex.intValue()) {
					executor.submit(
							new PrinterTask(unit, executor, segments, pages, printIndex, segment, lock, terminating));
				}
			}

			// System.out.println("seg " + segment + " finished");

		} catch (UnableToBreakException ignored) {
			try {
				this.unit.getPrinter().printErrorPage();
				this.unit.getPrinter().finishDocument();
				System.err.println("Unable to break lines!");
			} catch (Throwable error) {
				error.printStackTrace();
			}
			try {
				lock.lock();
				this.executor.shutdown();
				terminating.signal();
			} finally {
				lock.unlock();
			}
		}
	}

}

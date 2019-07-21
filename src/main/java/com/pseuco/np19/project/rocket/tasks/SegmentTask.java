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
import com.pseuco.np19.project.slug.tree.block.BlockElement;

public class SegmentTask extends Task {

	public SegmentTask(Unit unit, ExecutorService executor, SegmentsMonitor segMon, Map<Integer, List<Page>> pages,
			AtomicInteger printIndex, BlockElement element, int segment, int currentIndex, Lock lock,
			Condition condition) {
		super(unit, executor, segMon, pages, printIndex, element, segment, currentIndex, lock, condition);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		System.out.println("segtask started");
		for (int i = 0; i < segMon.getSegment(this.segment).getSize(); i++) {
			this.items.addAll(segMon.getSegment(segment).get(i));
		}

		try {
			List<Page> renderedPages = this.unit.getPrinter()
					.renderPages(breakIntoPieces(this.configuration.getBlockParameters(), this.items,
							this.configuration.getBlockTolerances(), this.configuration.getGeometry().getTextHeight()));
			pages.put(Integer.valueOf(segment), renderedPages);

			synchronized (this.unit) {
				System.out.println("Kann ich printen? " + segment);
				if (this.segment == this.printIndex.intValue()) {
					executor.submit(new PrinterTask(unit, executor, segMon, pages, printIndex, element, segment, index,
							lock, condition, this.printIndex.intValue()));
				}
			}

			System.out.println("seg " + segment + " finished");

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
				condition.signal();
			} finally {
				lock.unlock();
			}
		}
	}

}

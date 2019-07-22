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
import com.pseuco.np19.project.slug.tree.block.BlockElement;

public class PrinterTask extends Task {

	private int printed;

	protected PrinterTask(Unit unit, ExecutorService executor, SegmentsMonitor segMon, Map<Integer, List<Page>> pages,
			AtomicInteger printIndex, BlockElement element, int segment, int index, Lock lock, Condition condition,
			int printed) {
		super(unit, executor, segMon, pages, printIndex, element, segment, index, lock, condition);
		this.printed = printed;
	}

	@Override
	public void run() {
		// System.out.println("Started printer! " + segment);
		try {
			this.unit.getPrinter().printPages(pages.get(printed));

			synchronized (this.unit) {
				if (pages.containsKey(++this.printed)) {
					// System.out.println("next");
					executor.submit(new PrinterTask(this.unit, this.executor, segMon, pages, printIndex, element,
							segment + 1, index, lock, condition, printed));
				} else {
					this.printIndex.set(printed);
				}
			}
			// System.out.println("segment: " + segment + " last: " + segMon.getSegment(segment).isLast());
			if (segMon.getSegment(segment).isLast()) {
				// System.out.println("I am the last printer: " + segment);
				this.unit.getPrinter().finishDocument();

				try {
					lock.lock();
					this.executor.shutdown();
					condition.signal();
				} finally {
					lock.unlock();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

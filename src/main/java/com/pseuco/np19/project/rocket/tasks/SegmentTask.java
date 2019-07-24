package com.pseuco.np19.project.rocket.tasks;

import static com.pseuco.np19.project.launcher.breaker.Breaker.breakIntoPieces;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.Configuration;
import com.pseuco.np19.project.launcher.breaker.UnableToBreakException;
import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.launcher.render.Renderable;
import com.pseuco.np19.project.rocket.Metadata;
import com.pseuco.np19.project.rocket.monitors.Segment;

public class SegmentTask extends Task {

	protected final Configuration configuration;

	protected final List<Item<Renderable>> items = new LinkedList<>();

	protected SegmentTask(Metadata metadata, Map<Integer, List<Page>> pages, Segment segment) {
		super(metadata, pages, segment);
		this.configuration = unit.getConfiguration();
	}

	@Override
	public void run() {
		// System.out.println("segtask started: " + segment.getID());
		for (List<Item<Renderable>> items : segment.getBlockElements().values()) {
			this.items.addAll(items);
		}

		try {
			List<Page> renderedPages = this.unit.getPrinter()
					.renderPages(breakIntoPieces(this.configuration.getBlockParameters(), this.items,
							this.configuration.getBlockTolerances(), this.configuration.getGeometry().getTextHeight()));

			// System.out.println("Ich bin vor SYNC " + segment.getID());
			synchronized (this.unit.getConfiguration()) {
				pages.put(segment.getID(), renderedPages);

				this.unit.getConfiguration().notify();

			}

			// System.out.println("Kann ich printen? " + segment.getID());
			if (this.segment.getID() == 0) {
				executor.submit(new PrinterTask(metadata, pages, segment));
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

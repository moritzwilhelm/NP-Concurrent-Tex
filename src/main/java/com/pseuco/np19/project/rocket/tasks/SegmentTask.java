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

	private final Configuration configuration;

	private final List<Item<Renderable>> items = new LinkedList<>();

	protected SegmentTask(Metadata metadata, Map<Integer, List<Page>> pages, Segment segment) {
		super(metadata, pages, segment);
		configuration = unit.getConfiguration();
	}

	@Override
	public void run() {
		// System.out.println("segtask started: " + segment.getID());
		for (List<Item<Renderable>> items : segment.getBlockElements().values()) {
			this.items.addAll(items);
		}

		try {
			List<Page> renderedPages = unit.getPrinter().renderPages(breakIntoPieces(configuration.getBlockParameters(),
					this.items, configuration.getBlockTolerances(), configuration.getGeometry().getTextHeight()));

			// System.out.println("Ich bin vor SYNC " + segment.getID());
			synchronized (configuration) {
				pages.put(segment.getID(), renderedPages);

				configuration.notify();

			}

			// System.out.println("Kann ich printen? " + segment.getID());
			if (segment.getID() == 0) {
				new PrinterTask(metadata, pages, segment).run();
			}

			// System.out.println("seg " + segment + " finished");

		} catch (UnableToBreakException ignored) {
			System.err.println("Unable to break lines!");
			metadata.setBroken();

			try {
				lock.lock();
				executor.shutdown();
				terminating.signal();
			} finally {
				lock.unlock();
			}
		}
	}

}

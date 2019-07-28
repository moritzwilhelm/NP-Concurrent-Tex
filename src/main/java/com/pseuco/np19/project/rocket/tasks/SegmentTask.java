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
import com.pseuco.np19.project.rocket.monitors.Metadata;
import com.pseuco.np19.project.rocket.monitors.Segment;

/**
 * Task which processes a Segment
 */

public class SegmentTask extends Task {

	private final Configuration configuration;

	private final List<Item<Renderable>> items = new LinkedList<>();

	protected SegmentTask(Metadata metadata, Map<Integer, List<Page>> pages, Segment segment) {
		super(metadata, pages, segment);
		configuration = unit.getConfiguration();
	}

	@Override
	public void run() {

		Map<Integer, List<Item<Renderable>>> items = segment.getBlockElements();

		// concatenate all itemsLists to one containing all
		for (int i = 0; i < items.size(); i++) {
			this.items.addAll(items.get(i));
		}

		try {
			// distribute BlockElements on pages and render them
			List<Page> renderedPages = unit.getPrinter().renderPages(breakIntoPieces(configuration.getBlockParameters(),
					this.items, configuration.getBlockTolerances(), configuration.getGeometry().getTextHeight()));

			pages.put(segment.getID(), renderedPages);

			if (metadata.isNextToBePrinted(segment.getID())) {

				// abort if an error was encountered (by any other Thread)
				if (metadata.isBroken()) {
					return;
				}

				// simulate/transform into a PrinterTask Runnable
				new PrinterTask(metadata, pages, segment).run();
			}

		} catch (UnableToBreakException ignored) {
			System.err.println("Unable to break lines!");
			metadata.setBroken();

			// signal waiting UnitThread that an error was encountered
			metadata.initiateTermination();
		}
	}

}

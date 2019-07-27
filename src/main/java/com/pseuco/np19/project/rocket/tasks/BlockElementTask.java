package com.pseuco.np19.project.rocket.tasks;

import static com.pseuco.np19.project.launcher.breaker.Breaker.breakIntoPieces;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.Configuration;
import com.pseuco.np19.project.launcher.breaker.Piece;
import com.pseuco.np19.project.launcher.breaker.UnableToBreakException;
import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.launcher.render.Renderable;
import com.pseuco.np19.project.rocket.monitors.Metadata;
import com.pseuco.np19.project.rocket.monitors.Segment;
import com.pseuco.np19.project.slug.tree.block.BlockElement;
import com.pseuco.np19.project.slug.tree.block.ForcedPageBreak;
import com.pseuco.np19.project.slug.tree.block.IBlockVisitor;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

/**
 * Task which processes a BlockElement
 */

public class BlockElementTask extends Task implements IBlockVisitor {

	private final Configuration configuration;

	private final BlockElement element;

	private final int index;

	private final List<Item<Renderable>> items = new LinkedList<>();

	public BlockElementTask(Metadata metadata, Map<Integer, List<Page>> pages, Segment segment, BlockElement element,
			int index) {
		super(metadata, pages, segment);
		this.configuration = unit.getConfiguration();
		this.element = element;
		this.index = index;
	}

	@Override
	public void run() {

		// execute corresponding visit
		element.accept(this);

		boolean finished = false;

		synchronized (segment) {
			segment.put(index, items);
			finished = segment.isFinished();
		}

		if (finished) {

			// abort if an error was encountered (by any other Thread)
			if (metadata.isBroken()) {
				return;
			}

			// simulate/transform into a SegmentTask Runnable
			new SegmentTask(metadata, pages, segment).run();
		}

	}

	public void visit(Paragraph paragraph) {
		// transform the paragraph into a sequence of items
		final List<Item<Renderable>> items = paragraph.format(this.configuration.getInlineFormatter());

		try {
			// break the items into pieces using the Knuth-Plass algorithm
			final List<Piece<Renderable>> lines = breakIntoPieces(this.configuration.getInlineParameters(), items,
					this.configuration.getInlineTolerances(), this.configuration.getGeometry().getTextWidth());

			// transform lines into items and append them to `this.items`
			this.configuration.getBlockFormatter().pushParagraph(this.items::add, lines);
		} catch (UnableToBreakException error) {
			System.err.println("Unable to break paragraph!");
			metadata.setBroken();

			// signal waiting UnitThread that an error was encountered
			metadata.initiateTermination();
		}
	}

	public void visit(ForcedPageBreak forcedPageBreak) {
		// transform forced page break into items and append them to `this.items`
		this.configuration.getBlockFormatter().pushForcedPageBreak(this.items::add);
	}
}

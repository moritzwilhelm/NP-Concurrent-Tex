package com.pseuco.np19.project.rocket.tasks;

import static com.pseuco.np19.project.launcher.breaker.Breaker.breakIntoPieces;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.pseuco.np19.project.launcher.breaker.Piece;
import com.pseuco.np19.project.launcher.breaker.UnableToBreakException;
import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.launcher.render.Renderable;
import com.pseuco.np19.project.rocket.monitors.SegmentsMonitor;
import com.pseuco.np19.project.slug.tree.block.BlockElement;
import com.pseuco.np19.project.slug.tree.block.ForcedPageBreak;
import com.pseuco.np19.project.slug.tree.block.IBlockVisitor;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

public class BlockElementTask extends Task implements IBlockVisitor {

	public BlockElementTask(Unit unit, ExecutorService executor, SegmentsMonitor segMon, Map<Integer, List<Page>> pages,
			AtomicInteger printIndex, BlockElement element, int currentSegment, int currentIndex, Lock lock,
			Condition condition) {
		super(unit, executor, segMon, pages, printIndex, element, currentSegment, currentIndex, lock, condition);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		
		// go to visit
		element.accept(this);

		// falls voll, starte segment runnable

		//System.out.print("Segment: " + segment + " currSize: " + segMon.getSegment(segment).getSize() + " / " + segMon.getSegment(segment).getSizeWhenDone());
		if (segMon.addBlockElement(segment, index, items)) {
			System.out.println("starte segTASK");
			executor.submit(new SegmentTask(this.unit, this.executor, this.segMon, this.pages, this.printIndex,
					this.element, this.segment, this.index, this.lock, this.condition));
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
			try {
				this.unit.getPrinter().printErrorPage();
				this.unit.getPrinter().finishDocument();
			} catch (IOException e) {
				e.printStackTrace();
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

	public void visit(ForcedPageBreak forcedPageBreak) {
		// transform forced page break into items and append them to `this.items`
		this.configuration.getBlockFormatter().pushForcedPageBreak(this.items::add);
	}
}

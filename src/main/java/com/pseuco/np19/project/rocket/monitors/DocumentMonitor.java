package com.pseuco.np19.project.rocket.monitors;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.parser.DocumentBuilder;
import com.pseuco.np19.project.launcher.parser.ParagraphBuilder;
import com.pseuco.np19.project.launcher.parser.Position;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.rocket.ConcurrentParagraph;
import com.pseuco.np19.project.rocket.tasks.BlockElementTask;
import com.pseuco.np19.project.slug.tree.block.ForcedPageBreak;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

/**
 * A {@link DocumentBuilder} building an in-memory representation of an input
 * document.
 */
public class DocumentMonitor implements DocumentBuilder {

	private final Unit unit;

	private final ExecutorService executor;

	private final Lock lock;

	private final Condition terminate;

	private int currentSegment = 0;

	private int currentIndex = 0;

	private final SegmentsMonitor segments = new SegmentsMonitor();

	private final Map<Integer, List<Page>> pages = new ConcurrentHashMap<Integer, List<Page>>();

	private final AtomicInteger printIndex = new AtomicInteger(0);

	public DocumentMonitor(Unit unit, ExecutorService executor, Lock lock, Condition terminate) {
		this.unit = unit;
		this.executor = executor;
		this.lock = lock;
		this.terminate = terminate;
	}

	@Override
	public synchronized void appendForcedPageBreak(Position position) {
		segments.finishSegment(this.currentSegment, this.currentIndex + 1);
		executor.submit(new BlockElementTask(this.unit, this.executor, this.segments, this.pages, this.printIndex,
				new ForcedPageBreak(), this.currentSegment, this.currentIndex, this.lock, this.terminate));

		this.currentSegment++;
		this.currentIndex = 0;
		segments.putNewSegment();
		// System.out.println(" naechstes segment " + currentSegment);
	}

	@Override
	public synchronized ParagraphBuilder appendParagraph(Position position) {
		Paragraph paragraph = new ConcurrentParagraph(this.unit, this.executor, this.segments, this.pages,
				this.printIndex, this.currentSegment, this.currentIndex, this.lock, this.terminate);
		this.currentIndex++;
		return paragraph;
	}

	@Override
	public synchronized void finish() {
		// System.out.println("parser finish");
		segments.finishSegment(this.currentSegment, this.currentIndex + 1);
		segments.getSegment(currentSegment).setLast();
		executor.submit(new BlockElementTask(this.unit, this.executor, this.segments, this.pages, printIndex,
				new ForcedPageBreak(), this.currentSegment, this.currentIndex, this.lock, this.terminate));
	}
}

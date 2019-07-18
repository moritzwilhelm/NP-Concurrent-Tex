package com.pseuco.np19.project.rocket.monitors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.pseuco.np19.project.launcher.Configuration;
import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.parser.DocumentBuilder;
import com.pseuco.np19.project.launcher.parser.ParagraphBuilder;
import com.pseuco.np19.project.launcher.parser.Position;
import com.pseuco.np19.project.rocket.Task;
import com.pseuco.np19.project.slug.tree.block.ForcedPageBreak;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

/**
 * A {@link DocumentBuilder} building an in-memory representation of an input
 * document.
 */
public class DocumentMonitor implements DocumentBuilder {
	// private Queue<Task> tasks = new LinkedList<>();
	private final ExecutorService executor;
	private boolean finished = false;
	private int currentSegment = 0;
	private int currentIndex = 0;
	private final SegmentsMonitor segMon;
	private final Unit unit;
	private final Configuration configuration;
	private final Lock lock;
	private final Condition condition;

	// private boolean kabutt; true falls irgendein fehler

	public DocumentMonitor(ExecutorService executor, SegmentsMonitor segMon, Unit unit, Configuration configuration,
			Lock lock, Condition condition) {
		super();
		this.executor = executor;
		this.segMon = segMon;
		this.unit = unit;
		this.configuration = configuration;
		this.lock = lock;
		this.condition = condition;
	}

	@Override
	public synchronized void appendForcedPageBreak(Position position) {
		executor.submit(
				new Task(new ForcedPageBreak(), this.currentSegment, this.currentIndex, this.unit, this.configuration));

		// finish segment
		segMon.finishSegment(currentSegment, currentIndex + 1);

		// put segment
		// currentSegment++;
		currentIndex = 0;
		segMon.putNewSegment(++currentSegment);
	}

	@Override
	public synchronized ParagraphBuilder appendParagraph(Position position) {
		Paragraph paragraph = new Paragraph();
		executor.submit(new Task(paragraph, this.currentSegment, this.currentIndex, this.unit, this.configuration));
		currentIndex++;
		return paragraph;
	}

	@Override
	public synchronized void finish() {
		executor.submit(
				new Task(new ForcedPageBreak(), this.currentSegment, this.currentIndex, this.unit, this.configuration));
		finished = true;
		try {
			lock.lock();
			condition.signal();
		} finally {
			lock.unlock();
		}

	}

	public synchronized boolean isFinished() {
		return finished;
	}

}

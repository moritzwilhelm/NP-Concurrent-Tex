package com.pseuco.np19.project.rocket.monitors;

import java.util.LinkedList;
import java.util.Queue;

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
	private Queue<Task> tasks = new LinkedList<>();
	private boolean finished = false;
	private int currentSegment = 0;
	private int currentIndex = 0;
	private SegmentsMonitor segMon;
	private final Unit unit;
	private final Configuration configuration;

	public DocumentMonitor(SegmentsMonitor segMon, Unit unit, Configuration configuration) {
		super();
		this.segMon = segMon;
		this.unit = unit;
		this.configuration = configuration;
	}

	/**
	 * @return Returns the block elements of the document.
	 */
	public synchronized Queue<Task> getElements() {
		return this.tasks;
	}

	@Override
	public synchronized void appendForcedPageBreak(Position position) {
		this.tasks.add(
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
		this.tasks.add(new Task(paragraph, this.currentSegment, this.currentIndex, this.unit, this.configuration));
		currentIndex++;
		return paragraph;
	}

	@Override
	public synchronized void finish() {
		this.tasks.add(
				new Task(new ForcedPageBreak(), this.currentSegment, this.currentIndex, this.unit, this.configuration));
		finished = true;
	}

	public synchronized Task getCurrentElement() {
		return tasks.poll();
	}

	public synchronized boolean isFinished() {
		return finished;
	}

	public synchronized boolean isEmpty() {
		return tasks.isEmpty();
	}
}

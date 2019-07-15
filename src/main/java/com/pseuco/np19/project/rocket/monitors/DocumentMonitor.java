package com.pseuco.np19.project.rocket.monitors;

import java.util.LinkedList;
import java.util.Queue;

import com.pseuco.np19.project.launcher.parser.DocumentBuilder;
import com.pseuco.np19.project.launcher.parser.ParagraphBuilder;
import com.pseuco.np19.project.launcher.parser.Position;
import com.pseuco.np19.project.rocket.block.BlockElement;
import com.pseuco.np19.project.rocket.block.ForcedPageBreak;
import com.pseuco.np19.project.rocket.block.Paragraph;

/**
 * A {@link DocumentBuilder} building an in-memory representation of an input
 * document.
 */
public class DocumentMonitor implements DocumentBuilder {
	private Queue<BlockElement> elements = new LinkedList<>();
	private boolean finished = false;
	private int currentSegment = 0;
	private int currentIndex = 0;
	private SegmentsMonitor segMon;

	public DocumentMonitor(SegmentsMonitor segMon) {
		this.segMon = segMon;
	}

	/**
	 * @return Returns the block elements of the document.
	 */
	public synchronized Queue<BlockElement> getElements() {
		return this.elements;
	}

	@Override
	public synchronized void appendForcedPageBreak(Position position) {
		this.elements.add(new ForcedPageBreak(currentSegment, currentIndex));

		// finish segment
		segMon.finishSegment(currentSegment, currentIndex + 1);

		// put segment
		// currentSegment++;
		currentIndex = 0;
		segMon.putNewSegment(++currentSegment);
	}

	@Override
	public synchronized ParagraphBuilder appendParagraph(Position position) {
		Paragraph paragraph = new Paragraph(currentSegment, currentIndex);
		this.elements.add(paragraph);
		currentIndex++;
		return paragraph;
	}

	@Override
	public synchronized void finish() {
		this.elements.add(new ForcedPageBreak(currentSegment, currentIndex));
		finished = true;
	}

	public synchronized BlockElement getCurrentElement() {
		return elements.poll();
	}

	public synchronized boolean isFinished() {
		return finished;
	}

	public synchronized boolean isEmpty() {
		return elements.isEmpty();
	}
}

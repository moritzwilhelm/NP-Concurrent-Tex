package com.pseuco.np19.project.rocket.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pseuco.np19.project.launcher.parser.DocumentBuilder;
import com.pseuco.np19.project.launcher.parser.ParagraphBuilder;
import com.pseuco.np19.project.launcher.parser.Position;
import com.pseuco.np19.project.slug.tree.block.BlockElement;
import com.pseuco.np19.project.slug.tree.block.ForcedPageBreak;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

/**
 * A {@link DocumentBuilder} building an in-memory representation of an input
 * document.
 */
public class RocketDocument implements DocumentBuilder {
	private List<BlockElement> elements = new ArrayList<>();
	private int currentIndex = 0;
	private boolean finished = false;
	private int buffer = 1;

	/**
	 * @return Returns the block elements of the document.
	 */
	public synchronized List<BlockElement> getElements() {
		return this.elements;
	}

	@Override
	public synchronized void appendForcedPageBreak(Position position) {
		this.elements.add(new ForcedPageBreak());
		notifyAll();
	}

	@Override
	public synchronized ParagraphBuilder appendParagraph(Position position) {
		Paragraph paragraph = new Paragraph();
		this.elements.add(paragraph);
		notifyAll();
		return paragraph;
	}

	@Override
	public synchronized void finish() {
		try {
			this.elements = Collections.unmodifiableList(this.elements);
			buffer = 0;
			notifyAll();
			while (!(currentIndex == elements.size())) {
				wait();
			}
			finished = true;
		} catch (InterruptedException e) {
		}
	}

	public synchronized BlockElement getCurrentElement() {
		try {
			while (!(currentIndex < elements.size() - buffer)) {
				//System.out.println("wait");
				wait();
			}
			return elements.get(currentIndex++);
		} catch (InterruptedException e) {
			return null;
		} finally {
			notifyAll();
		}
	}

	public synchronized boolean isFinished() {
		return finished;
	}
	
	public synchronized boolean noElement() {
		return currentIndex == elements.size();
	}
}

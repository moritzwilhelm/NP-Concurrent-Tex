package com.pseuco.np19.project.rocket.monitors;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import com.pseuco.np19.project.launcher.parser.DocumentBuilder;
import com.pseuco.np19.project.launcher.parser.ParagraphBuilder;
import com.pseuco.np19.project.launcher.parser.Position;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.rocket.ConcurrentParagraph;
import com.pseuco.np19.project.rocket.Metadata;
import com.pseuco.np19.project.rocket.tasks.BlockElementTask;
import com.pseuco.np19.project.slug.tree.block.ForcedPageBreak;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

/**
 * A {@link DocumentBuilder} building an in-memory representation of an input
 * document.
 */
public class ConcurrentDocument implements DocumentBuilder {

	private final Metadata metadata;

	private final ExecutorService executor;

	private int currentSegment;

	private int currentIndex;

	private final Map<Integer, Segment> segments = new ConcurrentHashMap<>();

	private final Map<Integer, List<Page>> pages = new ConcurrentHashMap<>();

	public ConcurrentDocument(Metadata metadata) {
		this.metadata = metadata;
		this.executor = metadata.getExecutor();
		this.segments.put(0, new Segment(0));
	}

	@Override
	public void appendForcedPageBreak(Position position) {
		segments.get(this.currentSegment).setSizeWhenDone(this.currentIndex + 1);
		executor.submit(new BlockElementTask(this.metadata, this.pages, segments.get(currentSegment),
				new ForcedPageBreak(), this.currentIndex));

		this.currentSegment++;
		this.currentIndex = 0;
		this.segments.put(currentSegment, new Segment(currentSegment));
		// System.out.println(" naechstes segment " + currentSegment);
	}

	@Override
	public ParagraphBuilder appendParagraph(Position position) {
		Paragraph paragraph = new ConcurrentParagraph(this.metadata, this.pages, segments.get(currentSegment),
				this.currentIndex);
		this.currentIndex++;
		return paragraph;
	}

	@Override
	public void finish() {
		// System.out.println("parser finish");
		segments.get(this.currentSegment).setSizeWhenDone(currentIndex + 1);
		metadata.setSize(currentSegment + 1);
		executor.submit(new BlockElementTask(this.metadata, this.pages, segments.get(currentSegment),
				new ForcedPageBreak(), this.currentIndex));
	}
}

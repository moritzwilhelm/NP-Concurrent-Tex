package com.pseuco.np19.project.rocket;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import com.pseuco.np19.project.launcher.parser.DocumentBuilder;
import com.pseuco.np19.project.launcher.parser.ParagraphBuilder;
import com.pseuco.np19.project.launcher.parser.Position;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.rocket.monitors.Metadata;
import com.pseuco.np19.project.rocket.monitors.Segment;
import com.pseuco.np19.project.rocket.tasks.BlockElementTask;
import com.pseuco.np19.project.slug.tree.block.ForcedPageBreak;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

/**
 * A concurrent {@link DocumentBuilder} building an in-memory representation of
 * an input document.
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
		// set size of current segment
		segments.get(currentSegment).setSizeWhenDone(currentIndex + 1);

		// submit new ForcedPageBreaktask to end the current segment
		executor.submit(new BlockElementTask(metadata, pages, segments.get(currentSegment), new ForcedPageBreak(),
				currentIndex));

		// start new segment and reset currentIndex
		currentSegment += 1;
		currentIndex = 0;
		segments.put(currentSegment, new Segment(currentSegment));
	}

	@Override
	public ParagraphBuilder appendParagraph(Position position) {
		Paragraph paragraph = new ConcurrentParagraph(metadata, pages, segments.get(currentSegment), currentIndex);
		currentIndex++;
		return paragraph;
	}

	@Override
	public void finish() {
		// set size of last segment
		segments.get(currentSegment).setSizeWhenDone(currentIndex + 1);

		metadata.setNumSegments(currentSegment + 1);

		// submit new ForcedPageBreaktask to end the last segment
		executor.submit(new BlockElementTask(metadata, pages, segments.get(currentSegment), new ForcedPageBreak(),
				currentIndex));
	}
}

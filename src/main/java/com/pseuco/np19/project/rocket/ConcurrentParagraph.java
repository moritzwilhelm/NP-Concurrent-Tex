package com.pseuco.np19.project.rocket;

import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.rocket.monitors.Segment;
import com.pseuco.np19.project.rocket.tasks.BlockElementTask;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

public class ConcurrentParagraph extends Paragraph {
	private final Metadata metadata;

	private final Map<Integer, List<Page>> pages;

	private final Segment segment;

	private final int index;

	public ConcurrentParagraph(Metadata metadata, Map<Integer, List<Page>> pages, Segment segment, int index) {
		this.metadata = metadata;
		this.pages = pages;
		this.segment = segment;
		this.index = index;
	}

	@Override
	public void finish() {
		metadata.getExecutor().submit(new BlockElementTask(metadata, this.pages, this.segment, this, this.index));
	}
}

package com.pseuco.np19.project.rocket;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.rocket.monitors.SegmentsMonitor;
import com.pseuco.np19.project.rocket.tasks.BlockElementTask;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

public class ConcurrentParagraph extends Paragraph {
	private final Unit unit;

	private final ExecutorService executor;

	private final SegmentsMonitor segments;

	private final Map<Integer, List<Page>> pages;

	private final AtomicInteger printIndex;

	private final int segment;

	private final int index;

	private final Lock lock;

	private final Condition terminating;

	public ConcurrentParagraph(Unit unit, ExecutorService executor, SegmentsMonitor segments,
			Map<Integer, List<Page>> pages, AtomicInteger printIndex, int segment, int index, Lock lock,
			Condition terminating) {
		super();
		this.unit = unit;
		this.executor = executor;
		this.segments = segments;
		this.pages = pages;
		this.printIndex = printIndex;
		this.segment = segment;
		this.index = index;
		this.lock = lock;
		this.terminating = terminating;
	}

	@Override
	public void finish() {
		executor.submit(new BlockElementTask(this.unit, this.executor, this.segments, this.pages, this.printIndex, this,
				this.segment, this.index, this.lock, this.terminating));
	}
}

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

	private final SegmentsMonitor segMon;

	private final Map<Integer, List<Page>> pages;

	private final AtomicInteger printIndex;

	private final int currentSegment;

	private final int currentIndex;

	private final Lock lock;

	private final Condition condition;

	public ConcurrentParagraph(Unit unit, ExecutorService executor, SegmentsMonitor segMon,
			Map<Integer, List<Page>> pages, AtomicInteger printIndex, int currentSegment, int currentIndex, Lock lock,
			Condition condition) {
		super();
		this.unit = unit;
		this.executor = executor;
		this.segMon = segMon;
		this.pages = pages;
		this.printIndex = printIndex;
		this.currentSegment = currentSegment;
		this.currentIndex = currentIndex;
		this.lock = lock;
		this.condition = condition;
	}

	@Override
	public void finish() {
		executor.submit(new BlockElementTask(this.unit, this.executor, this.segMon, this.pages, this.printIndex, this,
				this.currentSegment, this.currentIndex, this.lock, this.condition));
	}
}

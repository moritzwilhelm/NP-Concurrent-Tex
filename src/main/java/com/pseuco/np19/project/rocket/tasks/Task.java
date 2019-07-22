package com.pseuco.np19.project.rocket.tasks;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.pseuco.np19.project.launcher.Configuration;
import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.launcher.render.Renderable;
import com.pseuco.np19.project.rocket.monitors.SegmentsMonitor;
import com.pseuco.np19.project.slug.tree.block.BlockElement;

public abstract class Task implements Runnable {

	// move attributes to corresponding tasks (iff possible)
	protected final Unit unit;

	protected final ExecutorService executor;

	protected final Configuration configuration;

	protected final SegmentsMonitor segments;

	protected final Map<Integer, List<Page>> pages;

	protected final AtomicInteger printIndex;

	protected final BlockElement element;

	protected int segment, index;

	protected final List<Item<Renderable>> items = new LinkedList<>();

	protected final Lock lock;

	protected final Condition terminating;

	protected Task(Unit unit, ExecutorService executor, SegmentsMonitor segments, Map<Integer, List<Page>> pages,
			AtomicInteger printIndex, BlockElement element, int segment, int index, Lock lock, Condition terminating) {
		this.unit = unit;
		this.configuration = unit.getConfiguration();
		this.executor = executor;
		this.segments = segments;
		this.pages = pages;
		this.printIndex = printIndex;
		this.element = element;
		this.segment = segment;
		this.index = index;
		this.lock = lock;
		this.terminating = terminating;
	}

	@Override
	public abstract void run();

}

package com.pseuco.np19.project.rocket.tasks;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.rocket.Metadata;
import com.pseuco.np19.project.rocket.monitors.Segment;

public abstract class Task implements Runnable {

	// move attributes to corresponding tasks (iff possible)

	protected final Metadata metadata;

	protected final Unit unit;

	protected final ExecutorService executor;

	protected final Map<Integer, List<Page>> pages;

	protected final Segment segment;

	protected final Lock lock;

	protected final Condition terminating;

	protected Task(Metadata metadata, Map<Integer, List<Page>> pages, Segment segment) {
		this.metadata = metadata;
		this.unit = metadata.getUnit();
		this.executor = metadata.getExecutor();
		this.pages = pages;
		this.segment = segment;
		this.lock = metadata.getLock();
		this.terminating = metadata.getTerminating();
	}

	@Override
	public abstract void run();

}

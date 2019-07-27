package com.pseuco.np19.project.rocket.tasks;

import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.rocket.monitors.Metadata;
import com.pseuco.np19.project.rocket.monitors.Segment;

/**
 * Abstract class which represents a processing task (e.g. printing, rendering, breaking into pieces)
 */

public abstract class Task implements Runnable {

	protected final Metadata metadata;

	protected final Unit unit;

	protected final Map<Integer, List<Page>> pages;

	protected final Segment segment;

	protected Task(Metadata metadata, Map<Integer, List<Page>> pages, Segment segment) {
		this.metadata = metadata;
		this.unit = metadata.getUnit();
		this.pages = pages;
		this.segment = segment;
	}

}

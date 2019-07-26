package com.pseuco.np19.project.rocket.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.launcher.printer.Printer;
import com.pseuco.np19.project.rocket.Metadata;
import com.pseuco.np19.project.rocket.monitors.Segment;

public class PrinterTask extends Task {

	private final Printer printer;

	protected PrinterTask(Metadata metadata, Map<Integer, List<Page>> pages, Segment segment) {
		super(metadata, pages, segment);
		this.printer = unit.getPrinter();
	}

	@Override
	public void run() {
		// System.out.println("Started printer! ");
		try {
			int segmentID = segment.getID();

			while (pages.containsKey(segmentID)) {
				printer.printPages(pages.get(segmentID));
				segmentID++;
			}

			if (segmentID != metadata.getSize()) {
				metadata.setPrintIndex(segmentID);
				return;
			}

			// System.out.println("segment: " + segment + " last: " +
			// segments.getSegment(segment).isLast());
			// if (segment.isLast()) {
			// System.out.println("I am the last printer: " + segment);
			unit.getPrinter().finishDocument();

			try {
				lock.lock();
				executor.shutdown();
				terminating.signal();
			} finally {
				lock.unlock();
			}
			// }

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

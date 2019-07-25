package com.pseuco.np19.project.rocket.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.rocket.Metadata;
import com.pseuco.np19.project.rocket.monitors.Segment;

public class PrinterTask extends Task {

	protected PrinterTask(Metadata metadata, Map<Integer, List<Page>> pages, Segment segment) {
		super(metadata, pages, segment);
	}

	@Override
	public void run() {
		// System.out.println("Started printer! ");
		try {
			int segmentID = 0;

			while (segmentID != metadata.getSize()) {

				synchronized (unit.getConfiguration()) {
					while (!pages.containsKey(segmentID)) {
						try {
							unit.getConfiguration().wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				unit.getPrinter().printPages(pages.get(segmentID));

				segmentID++;
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

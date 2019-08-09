package com.pseuco.np19.project.rocket.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.launcher.printer.Printer;
import com.pseuco.np19.project.rocket.monitors.Metadata;

/**
 * Task which prints rendered pages
 */

public class PrinterTask extends Task {

	private final Printer printer;

	protected PrinterTask(Metadata metadata, Map<Integer, List<Page>> pages) {
		super(metadata, pages);
		this.printer = unit.getPrinter();
	}

	@Override
	public void run() {

		try {
			int segmentID = metadata.getPrintIndex();

			// while current segment pages are rendered, print them
			do {
				// abort if an error was encountered (by any other Thread)
				if (metadata.isBroken()) {
					return;
				}

				synchronized (printer) {
					printer.printPages(pages.get(segmentID));
				}
				segmentID++;
			} while (pages.containsKey(segmentID));

			/*
			 * set PrintIndex to segmentID if current to be printed pages have not been
			 * rendered yet
			 */
			if (segmentID != metadata.getNumSegments()) {

				/*
				 * print next page if new page was put
				 * (if a Task put a page since exiting the while-loop)
				 */
				if (metadata.updatePrintIndex(segmentID)) {
					run();
				}
				// else the SegmentTask with ID == printID will start the next printing
				return;
			}

			// finish processing if all pages were printed
			unit.getPrinter().finishDocument();

			// signal waiting UnitThread that processing has finished
			metadata.initiateTermination();

		} catch (IOException e) {
			e.printStackTrace();
			metadata.setBroken();

			// signal waiting UnitThread that an error was encountered (prevents a deadlock)
			metadata.initiateTermination();
		}
	}

}

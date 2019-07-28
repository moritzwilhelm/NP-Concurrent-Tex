package com.pseuco.np19.project.rocket.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.pseuco.np19.project.launcher.printer.Page;
import com.pseuco.np19.project.launcher.printer.Printer;
import com.pseuco.np19.project.rocket.monitors.Metadata;
import com.pseuco.np19.project.rocket.monitors.Segment;

/**
 * Task which prints rendered pages
 */

public class PrinterTask extends Task {

	private final Printer printer;

	protected PrinterTask(Metadata metadata, Map<Integer, List<Page>> pages, Segment segment) {
		super(metadata, pages, segment);
		this.printer = unit.getPrinter();
	}

	@Override
	public void run() {

		try {
			int segmentID = metadata.getPrintIndex();
			boolean canPrint;
			// while current segment pages are rendered, print them
			do {

				// abort if an error was encountered (by any other Thread)
				if (metadata.isBroken()) {
					return;
				}

				synchronized (pages) {
					printer.printPages(pages.get(segmentID));
					canPrint = pages.containsKey(++segmentID);
					if (!canPrint) {
						metadata.setPrintIndex(segmentID);
					}
				}
			} while (canPrint);

			/*
			 * set PrintIndex to segmentID if current to be printed pages have not been
			 * rendered yet
			 */
			if (segmentID != metadata.getNumSegments()) {
				// metadata.setPrintIndex(segmentID);
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

	public boolean canPrintElseSetPrintIndex(int index) {
		synchronized (pages) {
			if (!pages.containsKey(index)) {
				metadata.setPrintIndex(index);
				return false;
			}
			return true;
		}
	}
}

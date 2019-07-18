package com.pseuco.np19.project.rocket;

import static com.pseuco.np19.project.launcher.breaker.Breaker.breakIntoPieces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.pseuco.np19.project.launcher.Configuration;
import com.pseuco.np19.project.launcher.breaker.Piece;
import com.pseuco.np19.project.launcher.breaker.UnableToBreakException;
import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.cli.CLI;
import com.pseuco.np19.project.launcher.cli.CLIException;
import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.parser.Parser;
import com.pseuco.np19.project.launcher.render.Renderable;
import com.pseuco.np19.project.rocket.monitors.DocumentMonitor;
import com.pseuco.np19.project.rocket.monitors.SegmentsMonitor;
import com.pseuco.np19.project.slug.tree.Document;
import com.pseuco.np19.project.slug.tree.block.BlockElement;

public class Rocket {
	private final Unit unit;

	private final Configuration configuration;

	private final List<Item<Renderable>> items = new LinkedList<>();

	private boolean unableToBreak = false;

	private Rocket(Unit unit) {
		this.unit = unit;
		this.configuration = this.unit.getConfiguration();
	}

	public static void main(String[] args) {
		// Slug.main(args);
		List<Thread> threadList = new ArrayList<Thread>();
		try {
			List<Unit> units = CLI.parseArgs(args);
			if (units.isEmpty()) {
				CLI.printUsage(System.out);
			}
			// System.out.println("");
			// System.out.println("num units: " + units.size());
			for (int i = 0; i < units.size() - 1; i++) {
				Thread unitThread = new UnitThread(units.get(i));
				threadList.add(unitThread);
				// System.out.println("start unit thread");
				unitThread.start();
			}

			(new UnitThread(units.get(units.size() - 1))).run();

			for (Thread thread : threadList) {
				thread.join();
				// System.out.println("thread terminated");
			}
			System.exit(0);
		} catch (CLIException error) {
			System.err.println(error.getMessage());
			CLI.printUsage(System.err);
			System.exit(1);
		} catch (Throwable error) {
			// error.printStackTrace();
			System.exit(1);
		}
	}

	/*private void creep() {
		try {
			final Document document = new Document();

			Parser.parse(this.unit.getInputReader(), document);

			for (BlockElement element : document.getElements()) {
				element.accept(this);
				if (this.unableToBreak) {
					this.unit.getPrinter().printErrorPage();
					this.unit.getPrinter().finishDocument();
					// do not do unnecessary work if a paragraph failed to typeset
					return;
				}
			}

			// this.configuration.getBlockFormatter().pushForcedPageBreak(this.items::add);

			try {
				final List<Piece<Renderable>> pieces = breakIntoPieces(this.configuration.getBlockParameters(),
						this.items, this.configuration.getBlockTolerances(),
						this.configuration.getGeometry().getTextHeight());

				this.unit.getPrinter().printPages(this.unit.getPrinter().renderPages(pieces));
			} catch (UnableToBreakException ignored) {
				this.unit.getPrinter().printErrorPage();
				System.err.println("Unable to break lines!");
			}
			this.unit.getPrinter().finishDocument();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	@Override
	public void run() {
		SegmentsMonitor segmon = new SegmentsMonitor();
		final DocumentMonitor document = new DocumentMonitor(segmon, this.unit, this.configuration);
		Parser parser = new Parser(unit.getInputReader(), document);
		Thread parserThread = new Rocket(this.unit) {
			public void run() {
				try {
					parser.buildDocument();
					// System.out.println("Parser terminated");
				} catch (IOException e) {
					// terminiere signal!
					return;
				}
			}
		};
		parserThread.start();

	}*/

}

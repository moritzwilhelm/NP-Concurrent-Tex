package com.pseuco.np19.project.rocket;

import static com.pseuco.np19.project.launcher.breaker.Breaker.breakIntoPieces;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.pseuco.np19.project.launcher.Configuration;
import com.pseuco.np19.project.launcher.breaker.Piece;
import com.pseuco.np19.project.launcher.breaker.UnableToBreakException;
import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.parser.Parser;
import com.pseuco.np19.project.launcher.render.Renderable;
import com.pseuco.np19.project.rocket.monitors.DocumentMonitorOLD;
import com.pseuco.np19.project.slug.tree.block.ForcedPageBreak;
import com.pseuco.np19.project.slug.tree.block.IBlockVisitor;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

public class UnitThreadOLD extends Thread implements IBlockVisitor {

	private final Unit unit;

	private final Configuration configuration;

	private final List<Item<Renderable>> items = new LinkedList<>();

	private boolean unableToBreak = false;

	public UnitThreadOLD(Unit unit) {
		this.unit = unit;
		this.configuration = this.unit.getConfiguration();
	}

	// parser_unitAgent()
	@Override
	public void run() {
		try {
			final DocumentMonitorOLD document = new DocumentMonitorOLD();
			Thread parserThread = new Thread() {
				public void run() {
					try {
						Parser.parse(unit.getInputReader(), document);
						// System.out.println("Parser terminated");
					} catch (IOException e) {
						return;
					}
				}
			};
			parserThread.start();
			// System.out.println("started parser");
			// parserThread.join();
			while (!document.isFinished()) {
				if (document.noElement()) {
					continue;
				}
				document.getCurrentElement().accept(this);
				if (this.unableToBreak) {
					this.unit.getPrinter().printErrorPage();
					this.unit.getPrinter().finishDocument();
					return;
				}
			}

			// remove this, see DocumentMonitor.finish()
			this.configuration.getBlockFormatter().pushForcedPageBreak(this.items::add);
			// System.out.println("finished parsing & block elements");

			// SegmentThread run
			try {
				final List<Piece<Renderable>> pieces = breakIntoPieces(this.configuration.getBlockParameters(),
						this.items, this.configuration.getBlockTolerances(),
						this.configuration.getGeometry().getTextHeight());

				this.unit.getPrinter().printPages(this.unit.getPrinter().renderPages(pieces));
			} catch (UnableToBreakException ignored) {
				this.unit.getPrinter().printErrorPage();
				System.err.println("Unable to break lines!");
			}
			// System.out.println("finishDoc");
			this.unit.getPrinter().finishDocument();
		} catch (Throwable error) {
			// error.printStackTrace();
			// System.exit(1);
		}
	}

	@Override
	public void visit(Paragraph paragraph) {
		// transform the paragraph into a sequence of items
		final List<Item<Renderable>> items = paragraph.format(this.configuration.getInlineFormatter());

		try {
			// break the items into pieces using the Knuth-Plass algorithm
			final List<Piece<Renderable>> lines = breakIntoPieces(this.configuration.getInlineParameters(), items,
					this.configuration.getInlineTolerances(), this.configuration.getGeometry().getTextWidth());

			// transform lines into items and append them to `this.items`
			this.configuration.getBlockFormatter().pushParagraph(this.items::add, lines);
		} catch (UnableToBreakException error) {
			System.err.println("Unable to break paragraph!");
			this.unableToBreak = true;
		}
	}

	@Override
	public void visit(ForcedPageBreak forcedPageBreak) {
		// transform forced page break into items and append them to `this.items`
		this.configuration.getBlockFormatter().pushForcedPageBreak(this.items::add);
	}
}

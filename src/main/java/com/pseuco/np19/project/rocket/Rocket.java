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
import com.pseuco.np19.project.slug.tree.Document;
import com.pseuco.np19.project.slug.tree.block.BlockElement;
import com.pseuco.np19.project.slug.tree.block.ForcedPageBreak;
import com.pseuco.np19.project.slug.tree.block.IBlockVisitor;
import com.pseuco.np19.project.slug.tree.block.Paragraph;

public class Rocket extends Thread implements IBlockVisitor {
	private final Unit unit;

	private final Configuration configuration;

	private final List<Item<Renderable>> items = new LinkedList<>();

	private boolean unableToBreak = false;

	private Rocket(Unit unit) {
		this.unit = unit;
		this.configuration = this.unit.getConfiguration();
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

	public static void main(String[] args) {
		// Slug.main(args);
		List<Thread> threadList = new ArrayList<Thread>();
		try {
			List<Unit> units = CLI.parseArgs(args);
			if (units.isEmpty()) {
				CLI.printUsage(System.out);
			}
			for (Unit unit : units) {
				Thread unitThread = new Rocket(unit);
				threadList.add(unitThread);
				unitThread.start();
			}

			for (Thread thread : threadList) {
				thread.join();
			}

			System.exit(0);
		} catch (CLIException error) {
			System.err.println(error.getMessage());
			CLI.printUsage(System.err);
			System.exit(1);
		} catch (Throwable error) {
			error.printStackTrace();
			System.exit(1);
		}
	}

	private void creep() throws IOException {
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

		this.configuration.getBlockFormatter().pushForcedPageBreak(this.items::add);

		try {
			final List<Piece<Renderable>> pieces = breakIntoPieces(this.configuration.getBlockParameters(), this.items,
					this.configuration.getBlockTolerances(), this.configuration.getGeometry().getTextHeight());

			this.unit.getPrinter().printPages(this.unit.getPrinter().renderPages(pieces));
		} catch (UnableToBreakException ignored) {
			this.unit.getPrinter().printErrorPage();
			System.err.println("Unable to break lines!");
		}
		this.unit.getPrinter().finishDocument();
	}

	@Override
	public void run() {
		try {
			creep();

		} catch (Throwable e) {
			return;
		}
	}
	/*
	 * @Override public void run() { try { final RocketDocument document = new
	 * RocketDocument(); Thread parserThread = new Rocket(this.unit) { public void
	 * run() { try { Parser.parse(unit.getInputReader(), document); } catch
	 * (IOException e) { return; } } }; parserThread.start(); //
	 * parserThread.join(); while (!document.isFinished()) {
	 * document.getCurrentElement().accept(this); if (this.unableToBreak) {
	 * this.unit.getPrinter().printErrorPage();
	 * this.unit.getPrinter().finishDocument(); // do not do unnecessary work if a
	 * paragraph failed to typeset return; } }
	 * 
	 * this.configuration.getBlockFormatter().pushForcedPageBreak(this.items::add);
	 * 
	 * try { final List<Piece<Renderable>> pieces =
	 * breakIntoPieces(this.configuration.getBlockParameters(), this.items,
	 * this.configuration.getBlockTolerances(),
	 * this.configuration.getGeometry().getTextHeight());
	 * 
	 * this.unit.getPrinter().printPages(this.unit.getPrinter().renderPages(pieces))
	 * ; } catch (UnableToBreakException ignored) {
	 * this.unit.getPrinter().printErrorPage();
	 * System.err.println("Unable to break lines!"); }
	 * this.unit.getPrinter().finishDocument(); } catch (Throwable error) {
	 * error.printStackTrace(); return; // System.exit(1); }
	 */
}

package com.pseuco.np19.project.launcher.printer;

import com.pseuco.np19.project.launcher.breaker.Piece;
import com.pseuco.np19.project.launcher.font.Font;
import com.pseuco.np19.project.launcher.render.Renderable;
import com.pseuco.np19.project.launcher.render.VContainer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a printer which can be used to "print" pages.
 */
public class Printer {
    private final Geometry geometry;
    private final Font font;
    private final Writer writer;

    /**
     * @param geometry The page geometry.
     * @param font The font used for text.
     * @param writer The {@link Writer} to write the pages to.
     */
    public Printer(Geometry geometry, Font font, Writer writer) {
        this.geometry = geometry;
        this.font = font;
        this.writer = writer;
    }

    /**
     * Renders a list of pieces on pages.
     *
     * @param pieces The pieces (representing the pages) to render.
     * @return The resulting pages.
     */
    public List<Page> renderPages(List<Piece<Renderable>> pieces) {
        final List<Page> pages = new ArrayList<>();
        final double x = this.geometry.getMarginLeft();
        final double y = this.geometry.getMarginTop();

        for (Piece<Renderable> piece : pieces) {
            final Page page = this.geometry.newPage(this.font.getFontSpec());

            (new VContainer(piece)).render(page, x, y);

            pages.add(page);
        }

        return pages;

    }

    /**
     * Prints the given list of pages by writing them to the output.
     *
     * @param pages The list of pages to print.
     * @throws IOException In case there is an IO error.
     */
    public void printPages(List<Page> pages) throws IOException {
        for (Page page : pages) {
            page.write(this.writer);
        }

        this.writer.flush();
    }

    /**
     * Prints a special error-page.
     *
     * (Should be printed as the last page if and only if the document cannot be typeset.)
     *
     * @throws IOException In case there is an IO error.
     */
    public void printErrorPage() throws IOException {
        final double x = this.geometry.getMarginLeft();
        final double y = this.geometry.getMarginTop();

        final Page page = this.geometry.newPage(this.font.getFontSpec());

        page.renderText("ERROR", x, y);

        page.write(this.writer);
    }

    /**
     * Indicates that the document is finished.
     *
     * Flushes the remaining pages and closes the underlying {@link Writer}.
     *
     * @throws IOException In case there is an IO error.
     */
    public void finishDocument() throws IOException {
        this.writer.flush();
        this.writer.close();
    }

}

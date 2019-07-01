package com.pseuco.np19.project.launcher.printer;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import com.pseuco.np19.project.launcher.font.FontSpec;
import com.pseuco.np19.project.launcher.render.Surface;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a page with fragments of text printed at a given position.
 */
public class Page implements Surface {
    private static final Gson gson = new Gson();

    private static final class Fragment {
        private final double x;
        private final double y;

        private final String text;

        Fragment(double x, double y, String text) {
            this.x = x;
            this.y = y;
            this.text = text;
        }

        @SuppressWarnings("unused")
		public double getX() {
            return this.x;
        }

        @SuppressWarnings("unused")
		public double getY() {
            return this.y;
        }

        @SuppressWarnings("unused")
		public String getText() {
            return this.text;
        }
    }

    @SerializedName("font_spec")
    private final FontSpec fontSpec;

    @SerializedName("paper_width")
    private final double paperWidth;
    @SerializedName("paper_height")
    private final double paperHeight;

    private final List<Fragment> fragments = new ArrayList<>();

    /**
     * @param fontSpec The font used for the text-fragments on this page.
     * @param paperWidth The width of the page.
     * @param paperHeight The height of the page.
     */
    Page(FontSpec fontSpec, double paperWidth, double paperHeight) {
        this.fontSpec = fontSpec;
        this.paperWidth = paperWidth;
        this.paperHeight = paperHeight;
    }

    public FontSpec getFontSpec() {
        return this.fontSpec;
    }

    public double getPaperWidth() {
        return this.paperWidth;
    }

    public double getPaperHeight() {
        return this.paperHeight;
    }

    public List<Fragment> getFragments() {
        return this.fragments;
    }

    /**
     * Writes the page, i.e., sends it to the respective output.
     *
     * @param writer The writer used to write the page.
     * @throws IOException In case there is some IO error.
     */
    public void write(Writer writer) throws IOException {
        gson.toJson(this, Page.class, writer);
        writer.write("\0\n");
    }
    
    @Override
    public void renderText(String text, double x, double y) {
        this.fragments.add(new Fragment(x, y, text));
    }
}

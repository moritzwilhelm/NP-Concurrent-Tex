package com.pseuco.np19.project.launcher.printer;

import com.google.gson.annotations.SerializedName;
import com.pseuco.np19.project.launcher.font.FontSpec;

/**
 * Represents a page geometry.
 */
public class Geometry {
    public class Builder {
        @SerializedName("paper_width")
        private double paperWidth = 595;
        @SerializedName("paper_height")
        private double paperHeight = 842;

        @SerializedName("margin_left")
        private double marginLeft = 71;
        @SerializedName("margin_right")
        private double marginRight = 71;

        @SerializedName("margin_top")
        private double marginTop = 71;
        @SerializedName("margin_bottom")
        private double marginBottom = 71;

        public Builder withPaperWidth(double paperWidth) {
            this.paperWidth = paperWidth;
            return this;
        }

        public Builder withPaperHeight(double paperHeight) {
            this.paperHeight = paperHeight;
            return this;
        }

        public Builder withMarginLeft(double marginLeft) {
            this.marginLeft = marginLeft;
            return this;
        }

        public Builder withMarginRight(double marginRight) {
            this.marginRight = marginRight;
            return this;
        }

        public Builder withMarginTop(double marginTop) {
            this.marginTop = marginTop;
            return this;
        }

        public Builder withMarginBottom(double marginBottom) {
            this.marginBottom = marginBottom;
            return this;
        }

        public Geometry create() {
            return new Geometry(this);
        }
    }

    private final double paperWidth;
    private final double paperHeight;
    
    private final double marginLeft;
    private final double marginRight;
    
    private final double marginTop;
    private final double marginBottom;

    private Geometry(Builder builder) {
        this.paperWidth = builder.paperWidth;
        this.paperHeight = builder.paperHeight;
        this.marginLeft = builder.marginLeft;
        this.marginRight = builder.marginRight;
        this.marginTop = builder.marginTop;
        this.marginBottom = builder.marginBottom;
    }

    public double getPaperWidth() {
        return this.paperWidth;
    }

    public double getPaperHeight() {
        return this.paperHeight;
    }

    public double getMarginLeft() {
        return this.marginLeft;
    }

    public double getMarginRight() {
        return this.marginRight;
    }

    public double getMarginTop() {
        return this.marginTop;
    }

    public double getMarginBottom() {
        return this.marginBottom;
    }

    /**
     * @return Returns the width of text (line width).
     */
    public double getTextWidth() {
        return this.paperWidth - this.marginLeft - this.marginRight;
    }

    /**
     * @return Returns the height of the part of a page available for content.
     */
    public double getTextHeight() {
        return this.paperHeight - this.marginTop - this.marginBottom;
    }

    /**
     * Creates a new page with the respective geometry and font specification.
     *
     * @param fontSpec The font used to render text on the page.
     * @return The created page.
     */
    public Page newPage(FontSpec fontSpec) {
        return new Page(fontSpec, this.paperWidth, this.paperHeight);
    }
}

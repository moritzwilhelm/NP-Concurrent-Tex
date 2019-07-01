package com.pseuco.np19.project.launcher.formatter;

import com.google.gson.annotations.SerializedName;

import com.pseuco.np19.project.launcher.breaker.Parameters;
import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.font.Font;
import com.pseuco.np19.project.launcher.render.Dummy;
import com.pseuco.np19.project.launcher.render.Renderable;
import com.pseuco.np19.project.launcher.render.Text;

import java.util.function.Consumer;

/**
 * Transforms a sequence of inline items (parts of a paragraph) into a sequence of items.
 */
public class InlineFormatter {
    /**
     * Implementation of the builder-pattern for the parameters of the {@link InlineFormatter}.
     */
    public class Builder {
        @SerializedName("hyphen_penalty")
        private double hyphenPenalty = 100;

        @SerializedName("space_base")
        private double spaceBase = 3;
        @SerializedName("space_stretch")
        private double spaceStretch = 6;
        @SerializedName("space_shrink")
        private double spaceShrink = 9;

        public Builder withHyphenPenalty(double hyphenPenalty) {
            this.hyphenPenalty = hyphenPenalty;
            return this;
        }

        public Builder withSpaceBase(double spaceBase) {
            this.spaceBase = spaceBase;
            return this;
        }

        public Builder withSpaceStretch(double spaceStretch) {
            this.spaceStretch = spaceStretch;
            return this;
        }

        public Builder withSpaceShrink(double spaceShrink) {
            this.spaceShrink = spaceShrink;
            return this;
        }

        public InlineFormatter create(Parameters parameters, Font font) {
            return new InlineFormatter(this, parameters, font);
        }
    }

    private final Parameters parameters;

    private final Font font;

    private final double hyphenWidth;
    private final double spaceWidth;

    private final double hyphenPenalty;

    private final double spaceBase;
    private final double spaceStretch;
    private final double spaceShrink;

    private InlineFormatter(Builder builder, Parameters parameters, Font font) {
        this.parameters = parameters;
        this.font = font;

        this.hyphenPenalty = builder.hyphenPenalty;

        this.spaceBase = builder.spaceBase;
        this.spaceStretch = builder.spaceStretch;
        this.spaceShrink = builder.spaceShrink;

        this.hyphenWidth = this.font.getGlyph('-').getWidth();
        this.spaceWidth = this.font.getGlyph(' ').getWidth();
    }

    private void pushChunk(Consumer<Item<Renderable>> consumer, String chunk) {
        consumer.accept(Item.createBox(this.font.measureText(chunk), new Text(chunk)));
    }

    /**
     * Transforms a hyphen and pushes the resulting items to the given consumer.
     *
     * @param consumer The consumer to push the resulting items to.
     */
    public void pushHyphen(Consumer<Item<Renderable>> consumer) {
        consumer.accept(Item.createPenalty(this.hyphenWidth, new Text("-"), this.hyphenPenalty, true));
    }

    /**
     * Transforms a syllable and pushes the resulting items to the given consumer.
     *
     * @param consumer The consumer to push the resulting items to.
     * @param syllable The syllable to push.
     */
    public void pushSyllable(Consumer<Item<Renderable>> consumer, String syllable) {
        this.pushChunk(consumer, syllable);
    }

    /**
     * Transforms a special (non-word) sequence of characters and pushes the resulting items to the given consumer.
     *
     * @param consumer The consumer to push the resulting items to.
     * @param special The non-word characters to push.
     */
    public void pushSpecial(Consumer<Item<Renderable>> consumer, String special) {
        this.pushChunk(consumer, special);
        if (special.equals(".")) {
            // push additional glue for better readability after each singleton "."
            double spaceStretch = (this.spaceWidth * this.spaceBase) / this.spaceStretch;
            double spaceShrink = (this.spaceWidth * this.spaceBase) / this.spaceShrink;
            consumer.accept(Item.createGlue(this.spaceWidth * 0.7, spaceStretch * 0.7, spaceShrink * 0.7));
        }
    }

    /**
     * Transforms a space and pushes the resulting items to the given consumer.
     *
     * @param consumer The consumer to push the resulting items to.
     */
    public void pushSpace(Consumer<Item<Renderable>> consumer) {
        double spaceStretch = (this.spaceWidth * this.spaceBase) / this.spaceStretch;
        double spaceShrink = (this.spaceWidth * this.spaceBase) / this.spaceShrink;
        consumer.accept(Item.createGlue(this.spaceWidth, spaceStretch, spaceShrink));
    }

    /**
     * Transforms a forced-break (at the end of a paragraph) and pushes the resulting items to the given consumer.
     *
     * The last item pushed is guaranteed to be a penalty with p=-∞.
     *
     * @param consumer The consumer to push the resulting items to.
     */
    public void endParagraph(Consumer<Item<Renderable>> consumer) {
        consumer.accept(Item.createGlue(0, this.parameters.getInfinity(), 0));
        consumer.accept(Item.createPenalty(0, Dummy.DUMMY, -this.parameters.getInfinity(), true));
    }
}

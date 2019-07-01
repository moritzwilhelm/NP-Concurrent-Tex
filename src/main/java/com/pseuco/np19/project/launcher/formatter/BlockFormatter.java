package com.pseuco.np19.project.launcher.formatter;

import com.google.gson.annotations.SerializedName;

import com.pseuco.np19.project.launcher.breaker.Parameters;
import com.pseuco.np19.project.launcher.breaker.Piece;
import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.render.HContainer;
import com.pseuco.np19.project.launcher.render.Renderable;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

import static com.pseuco.np19.project.launcher.render.Dummy.DUMMY;

/**
 * Transforms a sequence of paragraphs (lines) and forced-page-breaks into a sequence of items.
 */
public class BlockFormatter {
    /**
     * Implementation of the builder-pattern for the parameters of the {@link BlockFormatter}.
     */
    public class Builder {
        @SerializedName("club_penalty")
        private double clubPenalty = 5000;
        @SerializedName("widow_penalty")
        private double widowPenalty = 5000;

        @SerializedName("line_height")
        private double lineHeight = 15;

        @SerializedName("inter_line_size")
        private double interLineSize = 2;
        @SerializedName("inter_line_stretch")
        private double interLineStretch = 3;
        @SerializedName("inter_line_shrink")
        private double interLineShrink = 1;

        @SerializedName("skip_size")
        private double skipSize = 15;
        @SerializedName("skip_stretch")
        private double skipStretch = 20;
        @SerializedName("skip_shrink")
        private double skipShrink = 10;

        public Builder withClubPenalty(double clubPenalty) {
            this.clubPenalty = clubPenalty;
            return this;
        }

        public Builder withWidowPenalty(double widowPenalty) {
            this.widowPenalty = widowPenalty;
            return this;
        }

        public Builder withLineHeight(double lineHeight) {
            this.lineHeight = lineHeight;
            return this;
        }

        public Builder withInterLineSize(double interLineSize) {
            this.interLineSize = interLineSize;
            return this;
        }

        public Builder withInterLineStretch(double interLineStretch) {
            this.interLineStretch = interLineStretch;
            return this;
        }

        public Builder withInterLineShrink(double interLineShrink) {
            this.interLineShrink = interLineShrink;
            return this;
        }

        public Builder withSkipSize(double skipSize) {
            this.skipSize = skipSize;
            return this;
        }

        public Builder withSkipStretch(double skipStretch) {
            this.skipStretch = skipStretch;
            return this;
        }

        public Builder withSkipShrink(double skipShrink) {
            this.skipShrink = skipShrink;
            return this;
        }

        public BlockFormatter create(Parameters parameters) {
            return new BlockFormatter(this, parameters);
        }
    }

    private final double clubPenalty;
    private final double widowPenalty;

    private final double lineHeight;

    private final double interLineSize;
    private final double interLineStretch;
    private final double interLineShrink;

    private final double skipSize;
    private final double skipStretch;
    private final double skipShrink;

    private final Parameters parameters;

    private BlockFormatter(Builder builder, Parameters parameters) {
        this.clubPenalty = builder.clubPenalty;
        this.widowPenalty = builder.widowPenalty;

        this.lineHeight = builder.lineHeight;

        this.interLineSize = builder.interLineSize;
        this.interLineStretch = builder.interLineStretch;
        this.interLineShrink = builder.interLineShrink;

        this.skipSize = builder.skipSize;
        this.skipStretch = builder.skipStretch;
        this.skipShrink = builder.skipShrink;

        this.parameters = parameters;
    }

    /**
     * Transforms a list of lines constituting a paragraph and pushes the resulting items to the given consumer.
     *
     * @param consumer The consumer to push the resulting items to.
     * @param lines The lines that constitute a paragraph.
     */
    public void pushParagraph(Consumer<Item<Renderable>> consumer, List<Piece<Renderable>> lines) {
        final ListIterator<Piece<Renderable>> iterator = lines.listIterator();

        while (iterator.hasNext()) {
            final int index = iterator.nextIndex();
            final Piece<Renderable> piece = iterator.next();

            if (index == 1) {
                // we produce an extra penalty here to penalize page-breaks after the first line of a paragraph
                consumer.accept(Item.createPenalty(0, DUMMY, this.clubPenalty, false));
            }
            if (index == lines.size() - 2) {
                // we produce an extra penalty here to penalize page-breaks right before the last line of a paragraph
                consumer.accept(Item.createPenalty(0, DUMMY, this.widowPenalty, false));
            }
            if (index > 0) {
                // we produce some glue for the inter-line spacing
                consumer.accept(Item.createGlue(this.interLineSize, this.interLineStretch, this.interLineShrink));
            }

            // we produce the line itself as an {@link HContainer}
            consumer.accept(Item.createBox(this.lineHeight, new HContainer(piece)));
        }

        // we produce inter-paragraph spacing
        consumer.accept(Item.createGlue(this.skipSize, this.skipStretch, this.skipShrink));
    }

    /**
     * Transforms a forced-page-break and pushes the resulting items to the given consumer.
     *
     * The last item pushed is guaranteed to be a penalty with p=-∞.
     *
     * @param consumer The consumer to push the resulting items to.
     */
    public void pushForcedPageBreak(Consumer<Item<Renderable>> consumer) {
        consumer.accept(Item.createGlue(0, this.parameters.getInfinity(), 0));
        consumer.accept(Item.createPenalty(0, DUMMY, -this.parameters.getInfinity(), false));
    }
}

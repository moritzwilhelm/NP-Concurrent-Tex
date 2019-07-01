package com.pseuco.np19.project.launcher;

import com.google.gson.annotations.SerializedName;

import com.pseuco.np19.project.launcher.printer.Geometry;
import com.pseuco.np19.project.launcher.breaker.Parameters;
import com.pseuco.np19.project.launcher.font.Font;
import com.pseuco.np19.project.launcher.font.FontNotFoundException;
import com.pseuco.np19.project.launcher.font.FontRegistry;
import com.pseuco.np19.project.launcher.font.FontSpec;
import com.pseuco.np19.project.launcher.formatter.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a configuration used for typesetting and rendering.
 */
public class Configuration {
    public static final class Builder {
        @SerializedName("font_spec")
        private FontSpec fontSpec;

        @SerializedName("geometry")
        private Geometry.Builder geometryBuilder;

        @SerializedName("block_parameters")
        private Parameters.Builder blockParametersBuilder;
        @SerializedName("inline_parameters")
        private Parameters.Builder inlineParametersBuilder;

        @SerializedName("block_tolerances")
        private List<Double> blockTolerances;
        @SerializedName("inline_tolerances")
        private List<Double> inlineTolerances;

        @SerializedName("block_formatter")
        private BlockFormatter.Builder blockFormatterBuilder;
        @SerializedName("inline_formatter")
        private InlineFormatter.Builder inlineFormatterBuilder;

        public Builder withFontSpec(FontSpec fontSpec) {
            this.fontSpec = fontSpec;
            return this;
        }

        public Builder withGeometryBuilder(Geometry.Builder geometryBuilder) {
            this.geometryBuilder = geometryBuilder;
            return this;
        }

        public Builder withBlockParametersBuilder(Parameters.Builder blockParametersBuilder) {
            this.blockParametersBuilder = blockParametersBuilder;
            return this;
        }

        public Builder withInlineParametersBuilder(Parameters.Builder inlineParametersBuilder) {
            this.inlineParametersBuilder = inlineParametersBuilder;
            return this;
        }

        public Builder withBlockTolerances(List<Double> blockTolerances) {
            this.blockTolerances = blockTolerances;
            return this;
        }

        public Builder withInlineTolerances(List<Double> inlineTolerances) {
            this.inlineTolerances = inlineTolerances;
            return this;
        }

        public Builder withBlockFormatterBuilder(BlockFormatter.Builder blockFormatterBuilder) {
            this.blockFormatterBuilder = blockFormatterBuilder;
            return this;
        }

        public Builder withInlineFormatterBuilder(InlineFormatter.Builder inlineFormatterBuilder) {
            this.inlineFormatterBuilder = inlineFormatterBuilder;
            return this;
        }

        public Configuration create() throws FontNotFoundException{
            return this.create(FontRegistry.getDefaultRegistry());
        }

        public Configuration create(FontRegistry fontRegistry) throws FontNotFoundException {
            return new Configuration(fontRegistry.getFont(this.fontSpec), this);
        }
    }

    private final Font font;

    private final Geometry geometry;

    private final Parameters blockParameters;
    private final Parameters inlineParameters;

    private final List<Double> blockTolerances;
    private final List<Double> inlineTolerances;

    private final BlockFormatter blockFormatter;
    private final InlineFormatter inlineFormatter;

    private Configuration(Font font, Builder builder) {
        this.font = font;

        this.geometry = builder.geometryBuilder.create();

        this.blockParameters = builder.blockParametersBuilder.create();
        this.inlineParameters = builder.inlineParametersBuilder.create();

        this.blockTolerances = new ArrayList<>(builder.blockTolerances);
        this.inlineTolerances = new ArrayList<>(builder.inlineTolerances);

        this.blockFormatter = builder.blockFormatterBuilder.create(this.blockParameters);
        this.inlineFormatter = builder.inlineFormatterBuilder.create(this.inlineParameters, this.font);
    }

    public Font getFont() {
        return this.font;
    }

    public Parameters getInlineParameters() {
        return this.inlineParameters;
    }

    public Parameters getBlockParameters() {
        return this.blockParameters;
    }

    public Geometry getGeometry() {
        return this.geometry;
    }

    public List<Double> getInlineTolerances() {
        return this.inlineTolerances;
    }

    public List<Double> getBlockTolerances() {
        return this.blockTolerances;
    }

    public InlineFormatter getInlineFormatter() {
        return this.inlineFormatter;
    }

    public BlockFormatter getBlockFormatter() {
        return this.blockFormatter;
    }
}

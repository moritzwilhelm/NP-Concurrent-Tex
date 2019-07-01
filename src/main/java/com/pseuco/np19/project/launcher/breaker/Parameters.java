package com.pseuco.np19.project.launcher.breaker;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Holds the relevant parameters of the line-breaking algorithm.
 *
 * For details, we refer to the original paper.
 */
public final class Parameters {
    /**
     * Implementation of the builder-pattern for parameters.
     */
    public class Builder {
        private double infinity = 1000;

        @SerializedName("demerits_break")
        private double demeritsBreak = 10;
        @SerializedName("demerits_flagged")
        private double demeritsFlagged = 100;
        @SerializedName("demerits_fitness")
        private double demeritsFitness = 300;

        @SerializedName("rounds")
        private int rounds = 1;

        public Builder withInfinity(double infinity) {
            this.infinity = infinity;
            return this;
        }

        public Builder withDemeritsBreak(double demeritsBreak) {
            this.demeritsBreak = demeritsBreak;
            return this;
        }

        public Builder withDemeritsFitness(double demeritsFitness) {
            this.demeritsFitness = demeritsFitness;
            return this;
        }

        public Builder withRounds(int rounds) {
            this.rounds = rounds;
            return this;
        }

        public Parameters create() {
            return new Parameters(this);
        }
    }

    private final double infinity;

    private final double demeritsBreak;
    private final double demeritsFlagged;
    private final double demeritsFitness;

    private final int rounds;

    private Parameters(Builder builder) {
        this.infinity = builder.infinity;
        this.demeritsBreak = builder.demeritsBreak;
        this.demeritsFlagged = builder.demeritsFlagged;
        this.demeritsFitness = builder.demeritsFitness;
        this.rounds = builder.rounds;
    }

    public double getInfinity() {
        return this.infinity;
    }

    double getDemeritsBreak() {
        return this.demeritsBreak;
    }

    double getDemeritsFlagged() {
        return this.demeritsFlagged;
    }

    double getDemeritsFitness() {
        return this.demeritsFitness;
    }

    int getRounds() {
        return this.rounds;
    }

    public <T> Breaker<T> createBreaker(double pieceSize, List<Double> tolerances) {
        return new Breaker<>(pieceSize, tolerances, this);
    }
}

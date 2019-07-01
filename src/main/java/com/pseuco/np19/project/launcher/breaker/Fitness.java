package com.pseuco.np19.project.launcher.breaker;

/**
 * Fitness classes (see original paper).
 */
public enum Fitness {
    TIGHT,
    NORMAL,
    LOOSE,
    VERY_LOOSE;

    static public Fitness byRatio(double ratio) {
        if (ratio < -0.5) {
            return Fitness.TIGHT;
        } else if (ratio < 0.5) {
            return Fitness.NORMAL;
        } else if (ratio < 1) {
            return Fitness.LOOSE;
        } else {
            return Fitness.VERY_LOOSE;
        }
    }
}

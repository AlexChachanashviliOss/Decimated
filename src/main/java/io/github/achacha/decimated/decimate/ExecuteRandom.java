package io.github.achacha.decimated.decimate;

import java.util.Random;

/**
 * Every iteration checks in RNG is less than or equals to threshold of this executor
 * Optionally triggers only once during a given interval, if doesn't trigger doesn't reset the interval
 */
public class ExecuteRandom extends AbstractExecutor {
    private final double threshold;
    private final Random rng;

    /**
     * @param threshold      Threshold [0.0, 1.0] - 0.0 never match, 1.0 - always match
     * @param runnable       {@link Runnable} code to execute
     * @param intervalMillis Interval in milliseconds between which the event cannot trigger
     */
    public ExecuteRandom(double threshold, Runnable runnable, long intervalMillis) {
        super(runnable, intervalMillis);
        assert threshold >= 0 && threshold <= 1.0 : "Threshold must be in [0.0, 1.0]";
        this.threshold = threshold;
        this.rng = new Random(System.nanoTime());
    }

    /**
     * @param threshold      Threshold [0.0, 1.0] - 0.0 never match, 1.0 - always match
     * @param runnable       {@link Runnable} code to execute
     * @param intervalMillis Interval in milliseconds between which the event cannot trigger
     * @param rng {@link Random} RNG
     */
    public ExecuteRandom(double threshold, Runnable runnable, long intervalMillis, Random rng) {
        super(runnable, intervalMillis);
        assert threshold >= 0 && threshold <= 1.0 : "Threshold must be in [0.0, 1.0]";
        this.threshold = threshold;
        this.rng = rng;
    }

    @Override
    public void execute() {
        if (rng.nextDouble() <= threshold) {
            if (ifCanRunUpdateLastRun()) {
                synchronized (runnable) {
                    runnable.run();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ExecuteN{" +
                "intervalMillis=" + intervalMillis +
                ", lastRun=" + lastRun +
                ", threshold=" + threshold +
                '}';
    }
}

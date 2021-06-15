package io.github.achacha.decimated.math;

/**
 * Simple weighted average
 *
 * Incoming data is applied against a weighted average
 * Meant for long running data streams
 * Higher weight will allow existing average to be insensitive to change
 *
 * Weight and Behavior
 * 0.01 - New value has a very large influence on average, useful for slow changing data that needs to adjust quickly
 * 0.50 - New value is equal in value to existing average
 * 0.80 - Average is relatively stable against incoming data
 * 0.99 - New value has very little influence on the average, allows noisy data to not have a large impact on overall average
 *
 * Formula:  average = value * (1.0 - weight)  +  average * weight
 */
public class AverageWeighted {
    private double average;
    private final double weightIncoming;
    private final double weightExisting;

    /**
     * Create a simple weighted average
     * Initial average is 0.0
     * @param weightExisting Weight applied to existing average
     */
    public AverageWeighted(double weightExisting) {
        this(weightExisting, 0.0);
    }

    /**
     * Create a simple weighted average with initial value
     * @param weightExisting Weight applied to existing average
     * @param initialAverage Initial value if average is already known or better initial value can be estimated
     */
    public AverageWeighted(double weightExisting, double initialAverage) {
        this.average = initialAverage;

        assert weightExisting > 0.0 && weightExisting < 1.0: "Weight must be (0.0, 1.0)";
        this.weightExisting = weightExisting;
        this.weightIncoming = 1.0 - weightExisting;
    }

    /**
     * Add sample that will be calculated towards the new average
     *
     * @param sample data
     */
    public void add(double sample) {
        average = average * weightExisting + sample * weightIncoming;
    }

    /**
     * @return Current average
     */
    public double getAverage() {
        return average;
    }

    /**
     * Set initial average value if it's known
     * @param average Existing average value if known
     */
    public void setAverage(double average) {
        this.average = average;
    }
}

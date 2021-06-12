package io.github.achacha.decimated.math;

public class AverageMovingWindow {
    private final double[] samples;
    private final double[] weights;

    /**
     * Moving window average
     * @param size of the window
     * @param weightIncoming Incoming data weight, 1.0 means entire value is used
     * @param weightDecay Decay weights from weightIncoming over the entire window
     */
    public AverageMovingWindow(int size, double weightIncoming, double weightDecay) {
        assert weightIncoming > 0.0 && weightIncoming <= 1.0 : "Weight on incoming sample must be (0.0, 1.0]";
        assert weightDecay > 0.0 && weightDecay < 1.0 : "Weight decay on sample data must be (0.0, 1.0)";

        samples = new double[size];
        weights = new double[size];

        // Build weight window
        weights[0] = weightIncoming;
        for (int i = 1; i < weights.length; ++i) {
            weights[i] = weights[i - 1] * weightDecay;
        }
    }

    /**
     * Add sample that will be weighted against existing data and calculated towards the new average
     *
     * @param sample data
     */
    public void add(double sample) {
        if (weights.length - 1 >= 0) {
            System.arraycopy(samples, 0, samples, 1, weights.length - 1);
        }
        samples[0] = sample;
    }

    /**
     * Calculate average based on samples
     *
     * @return Average of past samples weighted over the window
     */
    public double getAverage() {
        double average = 0.0;
        double weightTotal = 0.0;
        for (int i=samples.length - 1; i >= 0 ; --i) {
            average += samples[i] * weights[i];
            weightTotal += weights[i];
        }
        return average * weightTotal / samples.length;
    }
}

package io.github.achacha.decimated.timeprovider;

/**
 * Time in millis now
 */
public interface TimeProvider {
    /**
     * Get time now in milliseconds
     *
     * Normally System time is adequate {@link TimeProviderSystem}
     *   for testing we want to manually set time every data point {@link TimeProviderFixed}
     *
     * @return Time now in milliseconds since epoch
     */
    long getMillis();
}

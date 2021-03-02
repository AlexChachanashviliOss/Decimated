package io.github.achacha.decimated;

import io.github.achacha.decimated.timeprovider.TimeProvider;
import io.github.achacha.decimated.timeprovider.TimeProviderSystem;

public class TimeUtil {
    private static TimeProvider timeProvider = new TimeProviderSystem();

    /**
     * @return Millis now
     */
    public static long getMillis() {
        return timeProvider.getMillis();
    }

    /**
     * NOTE: This is only used in unit tests
     * Set time provider
     * For testing we can use a fixed time provider
     * @param timeProvider {@link TimeProvider} to use
     */
    static void setTimeProvider(TimeProvider timeProvider) {
        TimeUtil.timeProvider = timeProvider;
    }
}

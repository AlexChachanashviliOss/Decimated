package io.github.achacha.decimated.deadcode;

import java.time.Instant;


/**
 * Data available when trigger is executed
 */
public interface TriggerAccessPoint {
    /**
     * Location of the execution
     * @return String with full package, class and line number
     */
    String getLocation();

    /**
     * Time when this trigger executed
     * @return {@link Instant}
     */
    Instant getAccessTime();

    /**
     * Throwable which contains the stack trace that includes the dead code location
     * @return {@link Throwable}
     */
    Throwable getThrowable();
}

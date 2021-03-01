package io.github.achacha.decimated;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StacktraceUtilTest {
    /**
     * Test that my location and caller location are correctly computed
     */
    @Test
    void theCaller() {
        // This is the caller
        testLocationOfCaller(StacktraceUtil.getMyLocation());
    }

    private void testLocationOfCaller(String caller) {
        Assertions.assertEquals(caller, StacktraceUtil.getCallerLocation());
    }

}
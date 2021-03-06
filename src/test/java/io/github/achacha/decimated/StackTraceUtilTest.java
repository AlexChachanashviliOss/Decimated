package io.github.achacha.decimated;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StackTraceUtilTest {
    /**
     * Test that my location and caller location are correctly computed
     */
    @Test
    void theCaller() {
        // This is the caller
        testLocationOfCaller(StackTraceUtil.getMyLocation());
    }

    private static void testLocationOfCaller(String caller) {
        Assertions.assertEquals(caller, StackTraceUtil.getCallerLocation());
    }

    @Test
    void testHashCode() {
        Throwable throwable1 = new Throwable();
        Throwable throwable2 = new Throwable();
        int hashCode1 = StackTraceUtil.toHash(throwable1);
        int hashCode2 = StackTraceUtil.toHash(throwable2);
        Assertions.assertNotEquals(hashCode1, hashCode2);
    }

}
package io.github.achacha.decimated;

import io.github.achacha.decimated.timeprovider.TimeProviderFixed;
import io.github.achacha.decimated.timeprovider.TimeProviderSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class SkipThenExecuteTest {
    @Test
    void testToString() {
        SkipThenExecute executor = new SkipThenExecute(7, ()-> System.out.println("This is a test"), 0);
        Assertions.assertTrue(executor.toString().contains("n=7"));
    }

    @Test
    void testNegative() {
        Assertions.assertThrows(AssertionError.class, ()-> Decimated.skipThenExecute(-1, ()-> System.out.println("Should never display")));
    }

    /**
     * Using N=0 implies always execute
     * XXXXXXXXXX
     */
    @Test
    void test0() {
        final AtomicInteger count = new AtomicInteger(0);
        for (int i=0; i<10; ++i) {
            Decimated.skipThenExecute(0, count::incrementAndGet);
        }

        Assertions.assertEquals(10, count.get());
    }

    /**
     * Skip 1
     * -X-X-X-X-X
     */
    @Test
    void skip1() {
        // First time N=0 (always runs)
        final AtomicBoolean b1 = new AtomicBoolean(false);
        Decimated.skipThenExecute(0, ()-> b1.set(true));
        Assertions.assertTrue(b1.get());

        // First time N=1
        final AtomicBoolean b2 = new AtomicBoolean(false);
        Decimated.skipThenExecute(1, ()-> b2.set(true));
        Assertions.assertFalse(b2.get());

        // loop
        final AtomicInteger count = new AtomicInteger(0);
        for (int i=0; i<10; ++i) {
            Decimated.skipThenExecute(1, count::incrementAndGet);
        }
        Assertions.assertEquals(5, count.get());
    }

    /**
     * Skip 3
     * ---X---X--
     */
    @Test
    void skip3() {
        final AtomicInteger count = new AtomicInteger(0);
        for (int i=0; i<10; ++i) {
            Decimated.skipThenExecute(3, count::incrementAndGet);
        }

        Assertions.assertEquals(2, count.get());
    }

    /**
     * Skip 10
     * ----------
     */
    @Test
    void skip10() {
        final AtomicInteger count = new AtomicInteger(0);
        for (int i=0; i<10; ++i) {
            Decimated.skipThenExecute(10, count::incrementAndGet);
        }

        Assertions.assertEquals(0, count.get());
    }

    /**
     * Skip by 1 with 1000ms interval
     * _X_X_X_X_X
     * T___T___T_
     * _!___!___!
     *
     * Will log 3 instances given the interval
     */
    @Test
    void testInterval() {
        try {
            TimeProviderFixed timeProvider = new TimeProviderFixed();
            TimeUtil.setTimeProvider(timeProvider);
            timeProvider.setMillis(10000);

            final int SIZE = 10;
            final StringBuilder sb = new StringBuilder(SIZE);
            for (int i=0; i<SIZE; ++i) {
                // Execute then skip, each iteration advances time by 200 millis
                Decimated.skipThenExecute(1, ()-> sb.append("!"), 1000);
                while (sb.length() < i+1) {
                    sb.append("_");
                }
                timeProvider.addMillis(250);
            }

            String result = sb.toString();
            Assertions.assertEquals(10, result.length());
            Assertions.assertEquals("_!___!___!", result);
        }
        finally {
            TimeUtil.setTimeProvider(new TimeProviderSystem());
        }
    }

}
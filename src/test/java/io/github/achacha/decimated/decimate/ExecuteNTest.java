package io.github.achacha.decimated.decimate;

import io.github.achacha.decimated.TimeUtil;
import io.github.achacha.decimated.timeprovider.TimeProviderFixed;
import io.github.achacha.decimated.timeprovider.TimeProviderSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteNTest {
    @Test
    void testToString() {
        ExecuteN executor = new ExecuteN(7, ()-> System.out.println("This is a test"), 0);
        Assertions.assertTrue(executor.toString().contains("n=7"));
    }

    @Test
    void testNegative() {
        Assertions.assertThrows(AssertionError.class, ()-> Decimated.nTimes(-1, ()-> System.out.println("Should never display")));
    }

    @Test
    void testOnce() {
        // First time
        AtomicBoolean b = new AtomicBoolean(false);
        Decimated.once(()-> b.set(true));
        Assertions.assertTrue(b.get());

        // loop
        final AtomicInteger count = new AtomicInteger(0);
        for (int i=0; i<5; ++i) {
            Decimated.once(count::incrementAndGet);
        }
        Assertions.assertEquals(1, count.get());
    }

    @Test
    void testN() {
        final AtomicInteger count = new AtomicInteger(0);
        for (int i=0; i<10; ++i) {
            Decimated.nTimes(3, count::incrementAndGet);
        }
        Assertions.assertEquals(3, count.get());
    }

    /**
     * Log up to 4 times, never more than once every second
     * clock advances by 500ms
     *
     * XXXXXXXXXX
     * T_T_T_T_T_
     * !_!_!_!___
     */
    @Test
    void testInterval4Times() {
        try {
            TimeProviderFixed timeProvider = new TimeProviderFixed();
            TimeUtil.setTimeProvider(timeProvider);
            timeProvider.setMillis(10000);

            final int SIZE = 10;
            final StringBuilder sb = new StringBuilder(SIZE);
            for (int i=0; i<SIZE; ++i) {
                // Execute then skip, each iteration advances time by 200 millis
                Decimated.nTimes(4, ()-> sb.append("!"), 1000);
                while (sb.length() < i+1) {
                    sb.append("_");
                }
                timeProvider.addMillis(500);
            }

            String result = sb.toString();
            Assertions.assertEquals(10, result.length());
            Assertions.assertEquals("!_!_!_!___", result);
        }
        finally {
            TimeUtil.setTimeProvider(new TimeProviderSystem());
        }
    }

    /**
     * Log up to 3 times, once every second
     * clock advance by 250ms
     *
     * XXXXXXXXXX
     * T_T_T_T_T_
     * !___!___!_
     */
    @Test
    void testInterval3Times() {
        try {
            TimeProviderFixed timeProvider = new TimeProviderFixed();
            TimeUtil.setTimeProvider(timeProvider);
            timeProvider.setMillis(10000);

            final int SIZE = 10;
            final StringBuilder sb = new StringBuilder(SIZE);
            for (int i=0; i<SIZE; ++i) {
                // Execute then skip, each iteration advances time by 200 millis
                Decimated.nTimes(3, ()-> sb.append("!"), 1_000);
                while (sb.length() < i+1) {
                    sb.append("_");
                }
                timeProvider.addMillis(250);
            }

            String result = sb.toString();
            Assertions.assertEquals(10, result.length());
            Assertions.assertEquals("!___!___!_", result);
        }
        finally {
            TimeUtil.setTimeProvider(new TimeProviderSystem());
        }
    }
}
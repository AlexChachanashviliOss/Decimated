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
     * XXXXX_____
     * T_T_T_T_T_
     * !_!_!_____
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
                Decimated.nTimes(5, ()-> sb.append("!"), 1000);
                while (sb.length() < i+1) {
                    sb.append("_");
                }
                timeProvider.addMillis(500);
            }

            String result = sb.toString();
            Assertions.assertEquals(10, result.length());
            Assertions.assertEquals("!_!_!_____", result);
        }
        finally {
            TimeUtil.setTimeProvider(new TimeProviderSystem());
        }
    }
}
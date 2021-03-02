package io.github.achacha.decimated;

import io.github.achacha.decimated.timeprovider.TimeProviderFixed;
import io.github.achacha.decimated.timeprovider.TimeProviderSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteThenSkipTest {
    @Test
    void testToString() {
        ExecuteThenSkip executor = new ExecuteThenSkip(7, ()-> System.out.println("This is a test"), 0);
        Assertions.assertTrue(executor.toString().contains("n=7"));
    }

    @Test
    void testNegative() {
        Assertions.assertThrows(AssertionError.class, ()-> Decimated.executeThenSkip(-1, ()-> System.out.println("Should never display")));
    }

    /**
     * Using N=0 implies always execute
     * XXXXXXXXXX
     */
    @Test
    void test0() {
        final AtomicInteger count = new AtomicInteger(0);
        for (int i=0; i<10; ++i) {
            Decimated.executeThenSkip(0, count::incrementAndGet);
        }

        Assertions.assertEquals(10, count.get());
    }

    /**
     * Skip 1
     * X-X-X-X-X-
     */
    @Test
    void test1() {
        // First time
        final AtomicBoolean b = new AtomicBoolean(false);
        Decimated.executeThenSkip(1, ()-> b.set(true));
        Assertions.assertTrue(b.get());

        // loop
        final AtomicInteger count = new AtomicInteger(0);
        for (int i=0; i<10; ++i) {
            Decimated.executeThenSkip(1, count::incrementAndGet);
        }
        Assertions.assertEquals(5, count.get());
    }

    /**
     * Skip 3
     * X---X---X-
     */
    @Test
    void test3() {
        final AtomicInteger count = new AtomicInteger(0);
        for (int i=0; i<10; ++i) {
            Decimated.executeThenSkip(3, count::incrementAndGet);
        }

        Assertions.assertEquals(3, count.get());
    }

    /**
     * Skip 10
     * X---------
     */
    @Test
    void test10() {
        final AtomicInteger count = new AtomicInteger(0);
        for (int i=0; i<10; ++i) {
            Decimated.executeThenSkip(10, count::incrementAndGet);
        }

        Assertions.assertEquals(1, count.get());
    }

    /**
     * Execute initially, skip by 1 with 1000ms interval
     * X_X_X_X_X_
     * T____T____
     * !_____!___
     *
     * Will log 2 instances given the interval
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
                Decimated.executeThenSkip(1, ()-> sb.append("!"), 1000);
                while (sb.length() < i+1) {
                    sb.append("_");
                }
                timeProvider.addMillis(200);
            }

            String result = sb.toString();
            Assertions.assertEquals(10, result.length());
            Assertions.assertEquals("!_____!___", result);
        }
        finally {
            TimeUtil.setTimeProvider(new TimeProviderSystem());
        }
    }
}
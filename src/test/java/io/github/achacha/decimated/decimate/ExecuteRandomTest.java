package io.github.achacha.decimated.decimate;

import io.github.achacha.decimated.TimeUtil;
import io.github.achacha.decimated.timeprovider.TimeProviderFixed;
import io.github.achacha.decimated.timeprovider.TimeProviderSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteRandomTest {
    @Test
    void testToString() {
        ExecuteRandom executor = new ExecuteRandom(0.5, () -> System.out.println("This is a test"), 0);
        Assertions.assertTrue(executor.toString().contains("threshold=0.5"));
    }

    @Test
    void testThresholdRange() {
        Assertions.assertThrows(AssertionError.class, () -> Decimated.random(-0.1, () -> System.out.println("Should never display")));
        Assertions.assertThrows(AssertionError.class, () -> Decimated.random(1.1, () -> System.out.println("Should never display")));
    }

    @Test
    void testAlwaysMatch() {
        final AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 10; ++i) {
            Decimated.random(1.0, count::incrementAndGet);
        }
        Assertions.assertEquals(10, count.get());
    }

    @Test
    void testNeverMatch() {
        final AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 10; ++i) {
            Decimated.random(0.0, count::incrementAndGet);
        }
        Assertions.assertEquals(0, count.get());
    }

    /**
     * Test {@link Random} that always returns the same value
     */
    @SuppressWarnings("SerializableHasSerializationMethods")
    private static class ConstantRng extends Random {
        private static final long serialVersionUID = -414326539261454094L;
        private final double c;

        public ConstantRng(double c) {
            this.c = c;
        }

        @Override
        public double nextDouble() {
            return c;
        }
    }

    @Test
    void testWithCustomRng() {
        ConstantRng rng = new ConstantRng(0.5);

        final AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 10; ++i) {
            // Should always trigger since RNG returns 0.5 and we are checking that 0.5 <= 0.6
            Decimated.random(0.6, count::incrementAndGet, 0, rng);
        }
        Assertions.assertEquals(10, count.get());
    }

    /**
     * Test 1 possible event every 1000ms
     * T_T_T_T_T_
     * !_!_!_!_!_
     */
    @Test
    void testAlwaysMatchInterval() {
        try {
            TimeProviderFixed timeProvider = new TimeProviderFixed();
            TimeUtil.setTimeProvider(timeProvider);
            timeProvider.setMillis(10000);

            final int SIZE = 10;
            final StringBuilder sb = new StringBuilder(SIZE);
            for (int i = 0; i < SIZE; ++i) {
                // Execute then skip, each iteration advances time by 200 millis
                Decimated.random(1.0, () -> sb.append("!"), 1000);
                while (sb.length() < i + 1) {
                    sb.append("_");
                }
                timeProvider.addMillis(500);
            }

            String result = sb.toString();
            Assertions.assertEquals(10, result.length());
            Assertions.assertEquals("!_!_!_!_!_", result);
        } finally {
            TimeUtil.setTimeProvider(new TimeProviderSystem());
        }
    }
}
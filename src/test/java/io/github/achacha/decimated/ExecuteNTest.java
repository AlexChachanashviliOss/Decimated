package io.github.achacha.decimated;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteNTest {
    @Test
    void testToString() {
        ExecuteN executeN = new ExecuteN(7, ()-> System.out.println("This is a test"));
        Assertions.assertTrue(executeN.toString().contains("n=7"));
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
}
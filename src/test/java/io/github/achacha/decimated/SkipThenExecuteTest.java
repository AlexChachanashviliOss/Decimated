package io.github.achacha.decimated;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class SkipThenExecuteTest {
    @Test
    void testToString() {
        ExecuteN executeN = new ExecuteN(7, ()-> System.out.println("This is a test"));
        Assertions.assertTrue(executeN.toString().contains("n=7"));
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
}
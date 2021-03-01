package io.github.achacha.decimated;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class ExecuteThenSkipTest {
    @Test
    void testToString() {
        ExecuteN executeN = new ExecuteN(7, ()-> System.out.println("This is a test"));
        Assertions.assertTrue(executeN.toString().contains("n=7"));
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
}
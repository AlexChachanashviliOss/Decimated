package io.github.achacha.decimated.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AverageMovingWindowTest {
    @Test
    void testInitializeAndConstantData() {
        AverageMovingWindow average = new AverageMovingWindow(3, 1.0, 0.5);
        for (int i=0; i<100; ++i) {
            average.add(3.33);
        }
        assertTrue(3.3 < average.getAverage() && average.getAverage() < 3.4);
    }

}
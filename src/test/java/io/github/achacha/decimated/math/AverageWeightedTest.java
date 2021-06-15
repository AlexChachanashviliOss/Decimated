package io.github.achacha.decimated.math;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AverageWeightedTest {
    @Test
    void testConstantValue() {
        AverageWeighted average = new AverageWeighted(0.75);

        // Eventually we will be very close to the average
        for (int i=0; i<100; ++i) {
            average.add(3.33);
        }

        assertTrue(3.34 > average.getAverage() && 3.32 < average.getAverage());
    }

    @Test
    void testConstantValueWithInitialValue() {
        AverageWeighted average = new AverageWeighted(0.75, 3.33);

        // Eventually we will be very close to the average
        for (int i=0; i<100; ++i) {
            average.add(3.33);
        }

        assertEquals(3.33, average.getAverage());
    }

    @Test
    void testInitialAverageAndNoisyInput() {
        AverageWeighted average = new AverageWeighted(0.999);

        average.setAverage(11.0);
        for (int i=0; i<100; ++i) {
            average.add(RandomUtils.nextDouble(9.0, 13.0));
        }

        // Should be close to our initial value
        assertTrue(11.5 > average.getAverage() && 10.5 < average.getAverage());
    }
}
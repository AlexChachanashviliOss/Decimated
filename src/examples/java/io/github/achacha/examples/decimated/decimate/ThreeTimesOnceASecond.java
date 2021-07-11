package io.github.achacha.examples.decimated.decimate;

import io.github.achacha.decimated.decimate.Decimated;
import io.github.achacha.examples.decimated.EventGeneratorThread;
import io.github.achacha.examples.decimated.ExamplesHelper;

/**
 * Display message 3 times total, once per second, discard the rest
 */
public class ThreeTimesOnceASecond extends EventGeneratorThread {
    public ThreeTimesOnceASecond() {
        super(50);
    }

    /**
     Execute 3 times total, once per second
     */
    @Override
    public void processEvent() {
        Decimated.nTimes(
                3,
                DISPLAY_PROGRESS,
                1_000
        );
    }

    /**
     * @param args String[]
     */
    public static void main(String[] args) {
        ThreeTimesOnceASecond thread = new ThreeTimesOnceASecond();
        thread.start();

        // Wait for key press to exit
        ExamplesHelper.pressReturnKeyToExit();

        // Signal thread to stop
        thread.stop();
    }
}

package io.github.achacha.examples.decimated.decimate;

import io.github.achacha.decimated.decimate.Decimated;
import io.github.achacha.examples.decimated.EventGeneratorThread;
import io.github.achacha.examples.decimated.ExamplesHelper;

/**
 * Display message 5% of the time, discard the rest
 */
public class Random extends EventGeneratorThread {
    public Random() {
        super(50);
    }

    /**
     Execute 5% of the time
     */
    @Override
    public void processEvent() {
        Decimated.random(
                0.05,
                DISPLAY_PROGRESS
        );
    }

    /**
     * @param args String[]
     */
    public static void main(String[] args) {
        Random thread = new Random();
        thread.start();

        // Wait for key press to exit
        ExamplesHelper.pressReturnKeyToExit();

        // Signal thread to stop
        thread.stop();
    }
}

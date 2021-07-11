package io.github.achacha.examples.decimated.decimate;

import io.github.achacha.decimated.decimate.Decimated;
import io.github.achacha.examples.decimated.EventGeneratorThread;
import io.github.achacha.examples.decimated.ExamplesHelper;

/**
 * Display one message only
 *
 * This is used by DeadCode to log when code was executed, but you only need to see this once
 */
public class Once extends EventGeneratorThread {
    public Once() {
        super(50);
    }

    /**
     Execute 1 time every 3 seconds
     */
    @Override
    public void processEvent() {
        Decimated.once(DISPLAY_PROGRESS);
    }

    /**
     * @param args String[]
     */
    public static void main(String[] args) {
        Once thread = new Once();
        thread.start();

        // Wait for key press to exit
        ExamplesHelper.pressReturnKeyToExit();

        // Signal thread to stop
        thread.stop();
    }
}

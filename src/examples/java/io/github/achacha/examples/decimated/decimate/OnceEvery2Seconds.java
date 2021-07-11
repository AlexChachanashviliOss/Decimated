package io.github.achacha.examples.decimated.decimate;

import io.github.achacha.decimated.decimate.Decimated;
import io.github.achacha.examples.decimated.EventGeneratorThread;
import io.github.achacha.examples.decimated.ExamplesHelper;

/**
 * Display one message every 3 seconds from a source that generates one every 50ms
 *
 * Event generated every 50ms may overwhelm the logs over a long period of time, we know
 * that the frequent event is happening and want to log it to remind us that it is happening
 * but we don't want to overwhelm the logging system with repetitive entries
 */
public class OnceEvery2Seconds extends EventGeneratorThread {
    public OnceEvery2Seconds() {
        super(50);
    }

    /**
     Execute 1 time every 3 seconds
     */
    @Override
    public void processEvent() {
        Decimated.oncePerInterval(
                DISPLAY_PROGRESS,
                2_000
        );
    }

    /**
     * @param args String[]
     */
    public static void main(String[] args) {
        OnceEvery2Seconds thread = new OnceEvery2Seconds();
        thread.start();

        // Wait for key press to exit
        ExamplesHelper.pressReturnKeyToExit();

        // Signal thread to stop
        thread.stop();
    }
}

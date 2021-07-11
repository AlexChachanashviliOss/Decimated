package io.github.achacha.examples.decimated;

/**
 * Generate a message every 50ms, log once every 3000ms
 */
public abstract class EventGeneratorThread implements Runnable {
    /**
     * Lambda to display progress, every time called will display current system time in milliseconds
     */
    public final Runnable DISPLAY_PROGRESS = () -> System.out.println(System.currentTimeMillis());

    /**
     * Thread that executes this runnable
     */
    private Thread thread;

    /**
     * When true will keep looping and executing
     */
    private boolean shouldRun = true;

    /**
     * How frequently the event should trigger in milliseconds
     */
    private final long eventTriggerIntervalMilliseconds;

    /**
     * Create event generator
     * @param eventTriggerIntervalMilliseconds generates event every N milliseconds
     */
    public EventGeneratorThread(long eventTriggerIntervalMilliseconds) {
        this.eventTriggerIntervalMilliseconds = eventTriggerIntervalMilliseconds;
    }

    @Override
    public void run() {
        // Loop and call event processor
        while (shouldRun) {
            // Trigger event and process it
            processEvent();

            // Delay sleep
            try {
                Thread.sleep(this.eventTriggerIntervalMilliseconds);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * Start thread
     */
    public synchronized void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Stop thread
     */
    public void stop() {
        shouldRun = false;
    }

    /**
     * Override in each example
     * This is where event processing happens
     */
    public abstract void processEvent();
}

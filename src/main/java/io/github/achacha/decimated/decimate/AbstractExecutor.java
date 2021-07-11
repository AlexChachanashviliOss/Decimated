package io.github.achacha.decimated.decimate;

import io.github.achacha.decimated.TimeUtil;

public abstract class AbstractExecutor implements Executor {
    protected final Runnable runnable;
    protected final long intervalMillis;
    protected long lastRun = 0;

    /**
     * @param runnable {@link Runnable} to execute
     * @param intervalMillis internal between each allowed execution (0 means no limit)
     */
    public AbstractExecutor(Runnable runnable, long intervalMillis) {
        this.runnable = runnable;
        this.intervalMillis = intervalMillis;
    }

    protected boolean ifCanRunUpdateLastRun() {
        if (intervalMillis == 0 || TimeUtil.getMillis() - lastRun >= intervalMillis) {
            // Assumed returning true means we are going to run executor so update lastRun
            lastRun = TimeUtil.getMillis();
            return true;
        }
        return false;
    }
}

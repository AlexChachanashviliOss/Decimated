package io.github.achacha.decimated.decimate;

/**
 * Execute N times total, never more than once per interval
 */
class ExecuteN extends AbstractExecutor {
    private int n;

    public ExecuteN(int n, Runnable runnable, long intervalMillis) {
        super(runnable, intervalMillis);
        assert n >= 0 : "N must be positive";
        this.n = n;
    }

    @Override
    public void execute() {
        if (n > 0) {
            if (ifCanRunUpdateLastRun()) {
                synchronized (runnable) {
                    runnable.run();
                    --n;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ExecuteN{" +
                "intervalMillis=" + intervalMillis +
                ", lastRun=" + lastRun +
                ", n=" + n +
                '}';
    }
}

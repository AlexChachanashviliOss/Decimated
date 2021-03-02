package io.github.achacha.decimated;

/**
 * Execute code then skip N
 */
class ExecuteThenSkip extends AbstractExecutor {
    private final int n;
    private int count;

    public ExecuteThenSkip(int n, Runnable runnable, long intervalMillis) {
        super(runnable, intervalMillis);
        assert n >= 0 : "N must be positive";
        this.n = n;
    }

    @Override
    public void execute() {
        if (--count < 0) {
            if (ifCanRunUpdateLastRun()) {
                synchronized (runnable) {
                    runnable.run();
                }
            }
            count = n;
        }
    }

    @Override
    public String toString() {
        return "ExecuteThenSkip{" +
                "intervalMillis=" + intervalMillis +
                ", lastRun=" + lastRun +
                ", n=" + n +
                ", count=" + count +
                '}';
    }
}

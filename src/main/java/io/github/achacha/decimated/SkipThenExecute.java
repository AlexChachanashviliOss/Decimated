package io.github.achacha.decimated;

/**
 * Skip N then execute
 */
class SkipThenExecute extends AbstractExecutor {
    private final int n;
    private int count;

    public SkipThenExecute(int n, Runnable runnable, long intervalMillis) {
        super(runnable, intervalMillis);
        assert n >= 0 : "N must be positive";
        this.n = n;
    }

    @Override
    public void execute() {
        if (++count > n) {
            if (ifCanRunUpdateLastRun()) {
                synchronized (runnable) {
                    runnable.run();
                }
            }
            count = 0;
        }
    }

    @Override
    public String toString() {
        return "SkipThenExecute{" +
                "intervalMillis=" + intervalMillis +
                ", lastRun=" + lastRun +
                ", n=" + n +
                ", count=" + count +
                '}';
    }
}

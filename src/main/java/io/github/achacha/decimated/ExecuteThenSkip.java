package io.github.achacha.decimated;

/**
 * Execute code then skip N
 */
class ExecuteThenSkip implements Executor {
    private final Runnable runnable;
    private final int n;
    private int count;

    public ExecuteThenSkip(int n, Runnable runnable) {
        assert n >= 0 : "N must be positive";
        this.n = n;
        this.runnable = runnable;
    }

    @Override
    public void execute() {
        if (--count < 0) {
            synchronized (runnable) {
                runnable.run();
            }
            count = n;
        }
    }

    @Override
    public String toString() {
        return "ExecuteThenSkip{" +
                "runnable=" + runnable +
                ", n=" + n +
                ", count=" + count +
                '}';
    }
}

package io.github.achacha.decimated;

/**
 * Execute code then skip N
 */
class ExecuteN implements Executor {
    private final Runnable runnable;
    private int n;

    public ExecuteN(int n, Runnable runnable) {
        assert n >= 0 : "N must be positive";
        this.n = n;
        this.runnable = runnable;
    }

    @Override
    public void execute() {
        if (n-- > 0) {
            synchronized (runnable) {
                runnable.run();
            }
        }
    }

    @Override
    public String toString() {
        return "ExecuteN{" +
                "runnable=" + runnable +
                ", n=" + n +
                '}';
    }
}

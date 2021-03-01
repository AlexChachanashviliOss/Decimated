package io.github.achacha.decimated;

/**
 * Skip N then execute
 */
class SkipThenExecute implements Executor {
    private final Runnable runnable;
    private final int n;
    private int count;

    public SkipThenExecute(int n, Runnable runnable) {
        assert n >= 0 : "N must be positive";
        this.n = n;
        this.runnable = runnable;
    }

    @Override
    public void execute() {
        if (++count > n) {
            synchronized (runnable) {
                runnable.run();
            }
            count = 0;
        }
    }

    @Override
    public String toString() {
        return "SkipThenExecute{" +
                "runnable=" + runnable +
                ", n=" + n +
                ", count=" + count +
                '}';
    }
}

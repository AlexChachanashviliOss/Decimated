package io.github.achacha.decimated;

import java.util.concurrent.ConcurrentHashMap;

public final class Decimated {
    /**
     * Container that maps location to executor data
     */
    private final static ConcurrentHashMap<String, Executor> locations = new ConcurrentHashMap<>();

    private Decimated() {
    }

    /**
     * Execute code then skip N times
     * First time is always executed
     *
     * N==0:  XXXXXX  (always execute)
     * N==1:  X-X-X-
     * N==2:  X--X--
     * ...
     *
     * @param n Execute and then skips N times
     * @param runnable {@link Runnable} code to execute
     */
    public static void executeThenSkip(int n, Runnable runnable) {
        final String location = StacktraceUtil.getCallerLocation();
        locations.computeIfAbsent(location, (loc)-> new ExecuteThenSkip(n, runnable)).execute();
    }

    /**
     * Skip N the execute code
     * First time is never executed unless N==0
     *
     * N==0:  XXXXXX  (always execute)
     * N==1:  -X-X-X
     * N==2:  --X--X
     * ...
     *
     * @param n Skip N times and then execute
     * @param runnable {@link Runnable} code to execute
     */
    public static void skipThenExecute(int n, Runnable runnable) {
        final String location = StacktraceUtil.getCallerLocation();
        locations.computeIfAbsent(location, (loc)-> new SkipThenExecute(n, runnable)).execute();
    }

    /**
     * Execute once
     *
     * @param runnable {@link Runnable} code to execute
     */
    public static void once(Runnable runnable) {
        final String location = StacktraceUtil.getCallerLocation();
        locations.computeIfAbsent(location, (loc)-> new ExecuteN(1, runnable)).execute();
    }

    /**
     * Execute N times and skip everything after
     *
     * @param n times to run
     * @param runnable {@link Runnable} code to execute
     */
    public static void nTimes(int n, Runnable runnable) {
        final String location = StacktraceUtil.getCallerLocation();
        locations.computeIfAbsent(location, (loc)-> new ExecuteN(n, runnable)).execute();
    }
}

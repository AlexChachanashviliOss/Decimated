package io.github.achacha.decimated;

import java.util.concurrent.ConcurrentHashMap;

public final class Decimated {
    /**
     * Container that maps location to executor data
     */
    private final static ConcurrentHashMap<String, Executor> locations = new ConcurrentHashMap<>(32);

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
        final String location = StackTraceUtil.getCallerLocation();
        locations.computeIfAbsent(location, (loc)-> new ExecuteThenSkip(n, runnable, 0)).execute();
    }

    /**
     * Execute code then skip N times no more than one every interval
     * First time is always executed
     *
     * N==0:  XXXXXX  (always execute)
     * N==1:  X-X-X-
     * N==2:  X--X--
     * ...
     *
     * @param n Execute and then skips N times
     * @param runnable {@link Runnable} code to execute
     * @param intervalMillis Execute every interval milliseconds
     */
    public static void executeThenSkip(int n, Runnable runnable, long intervalMillis) {
        final String location = StackTraceUtil.getCallerLocation();
        locations.computeIfAbsent(location, (loc)-> new ExecuteThenSkip(n, runnable, intervalMillis)).execute();
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
        final String location = StackTraceUtil.getCallerLocation();
        locations.computeIfAbsent(location, (loc)-> new SkipThenExecute(n, runnable, 0)).execute();
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
     * @param intervalMillis Execute every interval milliseconds
     */
    public static void skipThenExecute(int n, Runnable runnable, long intervalMillis) {
        final String location = StackTraceUtil.getCallerLocation();
        locations.computeIfAbsent(location, (loc)-> new SkipThenExecute(n, runnable, intervalMillis)).execute();
    }

    /**
     * Execute once
     *
     * @param runnable {@link Runnable} code to execute
     */
    public static void once(Runnable runnable) {
        final String location = StackTraceUtil.getCallerLocation();
        locations.computeIfAbsent(location, (loc)-> new ExecuteN(1, runnable, 0)).execute();
    }

    /**
     * Execute N times and skip everything after
     *
     * @param n times to run
     * @param runnable {@link Runnable} code to execute
     */
    public static void nTimes(int n, Runnable runnable) {
        final String location = StackTraceUtil.getCallerLocation();
        locations.computeIfAbsent(location, (loc)-> new ExecuteN(n, runnable, 0)).execute();
    }

    /**
     * Execute N times and skip everything after
     * Only one execution per interval
     *
     * @param n times to run
     * @param runnable {@link Runnable} code to execute
     * @param intervalMillis Execute every interval milliseconds
     */
    public static void nTimes(int n, Runnable runnable, long intervalMillis) {
        final String location = StackTraceUtil.getCallerLocation();
        locations.computeIfAbsent(location, (loc)-> new ExecuteN(n, runnable, intervalMillis)).execute();
    }
}

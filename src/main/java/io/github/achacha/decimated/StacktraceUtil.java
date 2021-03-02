package io.github.achacha.decimated;

public final class StacktraceUtil {
    private StacktraceUtil() {
    }

    public static String getMyLocation() {
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        return toLocation(stack[1]);
    }

    public static String getCallerLocation() {
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        return toLocation(stack[2]);
    }

    public static String toLocation(StackTraceElement ste) {
        return ste.getClassName()+"."+ ste.getMethodName()+":"+ste.getLineNumber();
    }
}

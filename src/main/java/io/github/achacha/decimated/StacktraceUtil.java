package io.github.achacha.decimated;

public final class StacktraceUtil {
    private StacktraceUtil() {
    }

    public static String getMyLocation() {
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        return stack[1].getClassName()+"."+ stack[1].getMethodName()+":"+stack[1].getLineNumber();
    }

    public static String getCallerLocation() {
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        return stack[2].getClassName()+"."+ stack[2].getMethodName()+":"+stack[2].getLineNumber();
    }
}

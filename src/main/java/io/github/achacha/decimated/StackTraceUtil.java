package io.github.achacha.decimated;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class StackTraceUtil {
    private StackTraceUtil() {
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

    /**
     * Calculate unique hash for provided stacktrace in {@link Throwable}
     * @param throwable {@link Throwable}
     * @return int hashCode
     */
    public static int toHash(Throwable throwable) {
        final StackTraceElement[] stack = throwable.getStackTrace();
        HashCodeBuilder builder = new HashCodeBuilder();
        for (StackTraceElement ste : stack) {
            builder.append(toLocation(ste));
        }
        return builder.toHashCode();
    }

    /**
     * Convert Throwable to String
     * @param t Throwable
     * @return String representation of {@link Throwable}
     */
    public static String toString(Throwable t) {
        try (
                StringWriter sw = new StringWriter(2048);
                PrintWriter pw = new PrintWriter(sw)
        ) {
            t.printStackTrace(pw);
            return sw.toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}

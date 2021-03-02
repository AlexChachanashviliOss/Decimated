package io.github.achacha.decimated.deadcode;

import io.github.achacha.decimated.StacktraceUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MemberUsageScanner;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Concept of a dead code is to add a line of code that performs some logging or output when
 * code is executed and only do so once to prevent log spam.
 *
 * This is used to detect code that may no longer be used by the application
 * If dead code triggers then it is being used and will display stacktrace of how we got there
 *
 * Each trigger point also keeps track of number of times it was triggered, last time it was triggered and last stacktrace
 */
public class DeadCode {
    private static final Reflections reflections = new Reflections();

    private static Method method;

    /**
     * Container that maps location to discovered dead code triggers
     */
    private final static ConcurrentHashMap<String, TriggerData> triggered = new ConcurrentHashMap<>(32);

    private DeadCode() {
    }

    static {
        try {
            method = DeadCode.class.getMethod("trigger", Runnable.class);
        } catch (NoSuchMethodException e) {
            // This should never happen unless method name was changed
            throw new RuntimeException("Failed to locate dead code trigger method", e);
        }
    }

    /**
     * Add trigger location
     * @param runnable {@link Runnable) to execute when first triggered
     */
    public static void trigger(Runnable runnable) {
        Throwable throwable = new Throwable();
        final String location = StacktraceUtil.toLocation(throwable.getStackTrace()[1]);
        TriggerData data = triggered.computeIfAbsent(location, (l)->{
            TriggerData d = new TriggerData();
            runnable.run();
            return d;
        });
        data.trigger(throwable);
    }

    /**
     * Access {@link Reflections} to configure additional packages to scan
     *
     * @return {@link Reflections} used by this class
     */
    public static Reflections getReflections() {
        return reflections;
    }

    /**
     * Class that contains data about trigger
     */
    public static class TriggerData {
        private int count;
        private Instant lastAccessed;
        private Throwable lastThrowable;

        public int getCount() {
            return count;
        }

        public Instant getLastAccessed() {
            return lastAccessed;
        }

        public Throwable getLastThrowable() {
            return lastThrowable;
        }

        public void trigger(Throwable throwable) {
            lastThrowable = throwable;
            lastAccessed = Instant.now();
            ++count;
        }

        @Override
        public String toString() {
            return "TriggerData{" +
                    "count=" + count +
                    ", lastAccessed=" + lastAccessed +
                    ", lastThrowable=" + ExceptionUtils.getStackTrace(lastThrowable) +
                    '}';
        }
    }

    /**
     * Add a package with dead code triggers to the collection
     * @param packagePath Path to the base package
     */
    public static void addPackageToScan(String packagePath) {
        Reflections testReflections = new Reflections(
                packagePath,
                new MemberUsageScanner()
        );
        reflections.merge(testReflections);
    }

    /**
     * @return Set of all known trigger locations
     */
    public static Set<Member> findAll() {
        return reflections.getMethodUsage(method);
    }

    /**
     * Map of triggered
     * @return Unmodifiable map of triggered
     */
    public static Map<String, TriggerData> getTriggered() {
        return Collections.unmodifiableMap(triggered);
    }
}

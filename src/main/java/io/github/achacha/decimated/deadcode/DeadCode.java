package io.github.achacha.decimated.deadcode;

import io.github.achacha.decimated.StackTraceUtil;
import org.reflections.Reflections;
import org.reflections.scanners.MemberUsageScanner;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Concept of dead code is to add a line of code that performs some logging or output when
 * code is executed and only do so limited numbe of times to prevent log spam.
 *
 * This is used to detect code that may no longer be used by the application
 * If dead code triggers then it is being used and will provide {@link TriggerAccessPoint} of how we got there
 *
 * Each trigger point also keeps track of number of times it was triggered, time it was triggered and {@link Throwable} with stack trace data
 */
public class DeadCode {
    private static final Reflections reflections = new Reflections();

    private static final Method method;

    /**
     * Container that maps location to discovered dead code triggers
     */
    private final static ConcurrentHashMap<String, TriggerData> triggered = new ConcurrentHashMap<>(32);

    private DeadCode() {
    }

    static {
        try {
            method = DeadCode.class.getMethod("trigger", TriggerAction.class);
        } catch (NoSuchMethodException e) {
            // This should never happen unless method name was changed
            throw new RuntimeException("Failed to locate dead code trigger method", e);
        }
    }

    /**
     * Add trigger location
     * @param action {@link TriggerAction) to execute when triggered
     */
    public static void trigger(TriggerAction action) {
        // depth of 2 means skip this method and the trigger method being called to use the caller as trigger method
        trigger(action, 1, 2);
    }

    /**
     * Add trigger location with max count and stack offset (to determine correct caller location)
     * @param action {@link TriggerAction} to execute when triggered first totalCount times
     * @param totalCount how many times to log the action before ignoring
     * @param stackdepthOffset Offset into the callstack (should be 1 to remove this function and use caller, or more if this is part of a method chain)
     */
    public static void trigger(TriggerAction action, int totalCount, int stackdepthOffset) {
        Throwable throwable = new Throwable();
        final String location = StackTraceUtil.toLocation(throwable.getStackTrace()[stackdepthOffset]);
        TriggerData data = triggered.computeIfAbsent(location, (l)-> new TriggerData(totalCount, stackdepthOffset));
        data.trigger(action, throwable);
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

    /**
     * Clear all trigger points
     */
    public static void clear() {
        triggered.clear();
    }
}

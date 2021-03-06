package io.github.achacha.decimated.deadcode;

import org.reflections.Reflections;
import org.reflections.scanners.MemberUsageScanner;

/**
 * Singleton container for {@link DeadCodeManager}
 * Must call {@link #initialize(String)} to allocate a new static {@link DeadCodeManager}
 *
 * This class is provided as a convenience when managing or injecting the {@link DeadCodeManager} is not available
 */
public class DeadCode {
    /**
     * If calling methods from this class, the stacktrace needs to skip this class and {@link DeadCodeManager} class
     * So caller is one deeper than the {@link DeadCodeManager} since we are adding a {@link #get()}
     */
    public static final int OFFSET = DeadCodeManager.OFFSET + 1;

    /** Instance created by call to {@link #initialize(String)}  */
    private static DeadCodeManager manager;

    public DeadCode() {
    }

    /**
     * Initialize DeadCode with classpaths to scan for triggers
     * Use {@link DeadCodeManager#addPackageToScan(String)} or {@link DeadCodeManager#getReflections()} to add more packages of needed
     * @param classpath Initial base classpath to scan
     */
    public static void initialize(String classpath) {
        manager = new DeadCodeManager();
        Reflections initialReflections = new Reflections(
                classpath,
                new MemberUsageScanner()
        );
        manager.getReflections().merge(initialReflections);
    }

    /**
     * Add trigger location
     * @param action {@link TriggerAction) to execute when triggered
     */
    public static void trigger(TriggerAction action) {
        manager.triggerAdd(action, 1, OFFSET);
    }

    /**
     * Get singleton {@link DeadCodeManager}
     *
     * Location offset for stack should be {@link #OFFSET} if calling get().trigger(...) to skip this class and singleton
     *
     * @return {@link DeadCodeManager}
     */
    public static DeadCodeManager get() {
        return manager;
    }
}

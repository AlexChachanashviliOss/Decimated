package io.github.achacha.decimated.deadcode;

import io.github.achacha.decimated.StackTraceUtil;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that contains data about trigger
 */
public class TriggerData {
    private final int stackOffset;
    private final int totalCount;
    private int count;
    private final ConcurrentHashMap<Integer, TriggerAccessPointImpl> accessed = new ConcurrentHashMap<>(3);

    public TriggerData(int totalCount, int stackOffset) {
        this.totalCount = totalCount;
        this.stackOffset = stackOffset;
    }

    /**
     * Trigger point with stack trace and access time
     */
    class TriggerAccessPointImpl implements TriggerAccessPoint {
        private final Instant accessTime;
        private final Throwable throwable;
        private final int hashCode;
        private final String location;

        public TriggerAccessPointImpl(Instant accessTime, Throwable throwable) {
            this.accessTime = accessTime;
            this.throwable = throwable;
            this.hashCode = StackTraceUtil.toHash(throwable);
            this.location = StackTraceUtil.toLocation(throwable.getStackTrace()[stackOffset]);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TriggerAccessPointImpl)) return false;
            return hashCode == ((TriggerAccessPointImpl) o).hashCode;
        }

        @Override
        public Instant getAccessTime() {
            return accessTime;
        }

        @Override
        public Throwable getThrowable() {
            return throwable;
        }

        @Override
        public String getLocation() {
            return location;
        }
    }

    /**
     * This can be higher than the size of {@link #accessed} since it will count all triggers
     * while accessed keeps track of unique call stacks
     *
     * @return Count how many times the trigger point was accessed
     */
    public int getCount() {
        return count;
    }

    /**
     * @return Map of unique access points to this trigger
     */
    ConcurrentHashMap<Integer, TriggerAccessPointImpl> getAccessed() {
        return accessed;
    }

    /**
     * Trigger at the given location
     * @param throwable {@link Throwable} from the location where the code triggered
     */
    synchronized void trigger(TriggerAction action, Throwable throwable) {
        TriggerAccessPointImpl accessPoint = new TriggerAccessPointImpl(Instant.now(), throwable);
        accessed.putIfAbsent(accessPoint.hashCode(), accessPoint);
        if (count < totalCount) {
            action.activated(accessPoint);
        }
        count++;
    }

    @Override
    public String toString() {
        return "TriggerData{" +
                "stackOffset=" + stackOffset +
                ", totalCount=" + totalCount +
                ", count=" + count +
                ", accessed=" + accessed +
                '}';
    }
}

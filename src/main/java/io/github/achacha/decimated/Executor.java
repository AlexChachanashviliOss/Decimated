package io.github.achacha.decimated;

/**
 * Interface for all executor types used with {@link Decimated}
 */
@FunctionalInterface
interface Executor {
    /**
     * Executor
     */
    void execute();
}

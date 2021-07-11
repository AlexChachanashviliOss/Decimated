package io.github.achacha.examples.decimated;

import java.io.IOException;

public final class ExamplesHelper {
    /**
     * Waits for key press and returns
     */
    public static void pressReturnKeyToExit()
    {
        System.out.println("Press RETURN key to exit");
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("\nExiting");
        }
    }
}

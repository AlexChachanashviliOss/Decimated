package io.github.achacha.decimated.pubic;

import io.github.achacha.decimated.decimate.Decimated;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * Methods guaranteed by 1.x API
 *
 * NOTE: DO NOT CHANGE ONCE RELEASED TO PUBLIC
 *       Only add new method tests
 */
public class Decimated_ApiGuarantee_01xx_Test {
    @Test
    void apiGuarantee_01xx() throws Exception {
        // 1.0.0
        Assertions.assertNotNull(Decimated.class.getMethod(
                "executeThenSkip",
                int.class,
                Runnable.class
                )
        );
        // 1.0.0
        Assertions.assertNotNull(Decimated.class.getMethod(
                "executeThenSkip",
                int.class,
                Runnable.class,
                long.class
                )
        );

        // 1.0.0
        Assertions.assertNotNull(Decimated.class.getMethod(
                "skipThenExecute",
                int.class,
                Runnable.class
                )
        );
        // 1.0.0
        Assertions.assertNotNull(Decimated.class.getMethod(
                "skipThenExecute",
                int.class,
                Runnable.class,
                long.class
                )
        );

        // 1.0.0
        Assertions.assertNotNull(Decimated.class.getMethod(
                "once",
                Runnable.class
                )
        );
        // 1.0.1
        Assertions.assertNotNull(Decimated.class.getMethod(
                "oncePerInterval",
                Runnable.class,
                long.class
                )
        );

        // 1.0.0
        Assertions.assertNotNull(Decimated.class.getMethod(
                "nTimes",
                int.class,
                Runnable.class
                )
        );
        // 1.0.0
        Assertions.assertNotNull(Decimated.class.getMethod(
                "nTimes",
                int.class,
                Runnable.class,
                long.class
                )
        );

        // 1.0.1
        Assertions.assertNotNull(Decimated.class.getMethod(
                "random",
                double.class,
                Runnable.class
                )
        );
        // 1.0.1
        Assertions.assertNotNull(Decimated.class.getMethod(
                "random",
                double.class,
                Runnable.class,
                long.class
                )
        );
        // 1.0.1
        Assertions.assertNotNull(Decimated.class.getMethod(
                "random",
                double.class,
                Runnable.class,
                long.class,
                Random.class
                )
        );
    }
}

package io.github.achacha.decimated.pubic;

import io.github.achacha.decimated.deadcode.DeadCode;
import io.github.achacha.decimated.deadcode.TriggerAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeadCode_ApiGuarantee_01xx_Test {
    @Test
    void apiGuarantee_01xx() throws Exception {
        // 1.0.0
        Assertions.assertNotNull(DeadCode.class.getMethod(
                "get")
        );
        // 1.0.0
        Assertions.assertNotNull(DeadCode.class.getMethod(
                "initialize",
                String.class
                )
        );

        // 1.0.0
        Assertions.assertNotNull(DeadCode.class.getMethod(
                "trigger",
                TriggerAction.class
                )
        );
        // 1.0.0
        Assertions.assertNotNull(DeadCode.class.getMethod(
                "trigger",
                TriggerAction.class,
                int.class
                )
        );
    }
}
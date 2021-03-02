package io.github.achacha.decimated.deadcode;

import java.util.ArrayList;
import java.util.List;

public class DeadCodeTester {
    List<String> LOGGER = new ArrayList<>(1);

    public void methodCalled() {
        DeadCode.trigger(()-> LOGGER.add("Method was called"));
    }

    public void methodNeverCalled() {
        DeadCode.trigger(()-> LOGGER.add("Method should never be called"));
    }
}

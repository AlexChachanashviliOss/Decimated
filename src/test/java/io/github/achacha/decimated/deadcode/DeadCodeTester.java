package io.github.achacha.decimated.deadcode;

import java.util.ArrayList;
import java.util.List;

public class DeadCodeTester {
    List<String> LOGGER = new ArrayList<>(1);

    public void methodCalled() {
        DeadCode.trigger((accessPoint)-> LOGGER.add("Method was called: " + accessPoint.getLocation() + " at " + accessPoint.getAccessTime()));
    }

    public void methodNeverCalled() {
        DeadCode.trigger((accessPoint)-> LOGGER.add("Method should never be called: " + accessPoint.getLocation() + " at " + accessPoint.getAccessTime()));
    }
}

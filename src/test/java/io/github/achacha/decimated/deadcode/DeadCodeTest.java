package io.github.achacha.decimated.deadcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Member;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class DeadCodeTest {
    @BeforeAll
    static void beforeAll() {
        // Add this package to available triggers
        DeadCode.addPackageToScan(DeadCodeTest.class.getPackageName());
        Assertions.assertNotNull(DeadCode.getReflections());
    }

    @BeforeEach
    void beforeEach() {
        DeadCode.clear();
    }

    @Test
    void testCalling() {
        // Verify all triggers found
        Set<Member> members = DeadCode.findAll();
        Assertions.assertEquals(2, members.size());

        // Call method multiple times
        DeadCodeTester tester = new DeadCodeTester();
        tester.methodCalled();
        tester.methodCalled();
        tester.methodCalled();

        // Verify that it triggered
        List<DeadCode.TriggerData> items = DeadCode.getTriggered().entrySet().stream()
                .filter(entry->entry.getKey().contains(".DeadCodeTester.methodCalled:"))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(1, tester.LOGGER.size());

        // Verify trigger item
        DeadCode.TriggerData item = items.get(0);
        Assertions.assertEquals(3, item.getCount());
        Assertions.assertTrue(item.toString().contains("count=3"));
        Assertions.assertTrue(item.getLastAccessed().isBefore(Instant.now()));
        Assertions.assertNotNull(item.getLastThrowable());
    }

    @Test
    void testCallingWithOffset() {
        // Use counter to verify it triggered
        AtomicInteger counter = new AtomicInteger(0);
        for (int i=0; i<3; ++i) {
            // It's important to call this in a loop since the line number much be the same through multiple calls
            wrapperForTrigger(counter);
        }
        Assertions.assertEquals(1, counter.get());

        Set<String> keys = DeadCode.getTriggered().keySet();
        Assertions.assertEquals(1, keys.size());
        Assertions.assertTrue(keys.iterator().next().startsWith("io.github.achacha.decimated.deadcode.DeadCodeTest.testCallingWithOffset:"));
    }

    private static void wrapperForTrigger(AtomicInteger counter) {
        DeadCode.trigger(counter::incrementAndGet, 2);
    }

    @Test
    void testCallingError() {
        // Add this package to available triggers
        DeadCode.addPackageToScan(this.getClass().getPackageName());

        // Call method multiple times
        DeadCodeTester tester = new DeadCodeTester();
        tester.methodCalled();
        tester.methodNeverCalled();

        // Verify that both entries exist
        Assertions.assertEquals(2, DeadCode.getTriggered().size());
    }
}
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
    void testCallingFromSameLocation() {
        // Verify all triggers found
        Set<Member> members = DeadCode.findAll();
        Assertions.assertEquals(2, members.size());

        // Call method multiple times from same code location
        DeadCodeTester tester = new DeadCodeTester();
        for (int i=0; i<3; ++i) {
            tester.methodCalled();
        }

        // Verify that it triggered
        List<TriggerData> items = DeadCode.getTriggered().entrySet().stream()
                .filter(entry->entry.getKey().contains(".DeadCodeTester.methodCalled:"))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(1, tester.LOGGER.size());

        // Verify trigger item
        TriggerData item = items.get(0);
        Assertions.assertEquals(3, item.getCount());
        Assertions.assertTrue(item.toString().contains("count=3"));
        Assertions.assertEquals(1, item.getAccessed().size());
        TriggerData.TriggerAccessPointImpl accessPoint = item.getAccessed().entrySet().stream().findFirst().get().getValue();
        Assertions.assertTrue(accessPoint.getAccessTime().isBefore(Instant.now()));
        Assertions.assertNotNull(accessPoint.getThrowable());
    }

    @Test
    void testCallingSameMethodFromDifferentLocation() {
        // Call method multiple times, 3 unique locations, 5 from same location, 8 total calls
        DeadCodeTester tester = new DeadCodeTester();
        tester.methodCalled();
        tester.methodCalled();
        for(int i=0; i<6; ++i) {
            tester.methodCalled();
        }

        // Verify only one trigger is logged
        List<TriggerData> items = DeadCode.getTriggered().entrySet().stream()
                .filter(entry->entry.getKey().contains(".DeadCodeTester.methodCalled:"))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        // Access for DeadCodeTester::methodCalled only 1 point
        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(1, tester.LOGGER.size());

        // Verify trigger item
        TriggerData item = items.get(0);
        Assertions.assertEquals(8, item.getCount());   // Total calls
        Assertions.assertEquals(3, item.getAccessed().size());  // 3 unique call spots
        TriggerData.TriggerAccessPointImpl accessPoint = item.getAccessed().entrySet().stream().findFirst().get().getValue();
        Assertions.assertTrue(accessPoint.getAccessTime().isBefore(Instant.now()));
        Assertions.assertNotNull(accessPoint.getThrowable());

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
        DeadCode.trigger((accessPoint)-> counter.incrementAndGet(), 1, 2);
    }

    @Test
    void testCallingWithOffsetAndMaxCount() {
        // Use counter to verify it triggered
        AtomicInteger counter = new AtomicInteger(0);
        for (int i=0; i<10; ++i) {
            // It's important to call this in a loop since the line number much be the same through multiple calls
            wrapperForTriggerWithCount(counter, 5);
        }
        Assertions.assertEquals(5, counter.get());

        Set<String> keys = DeadCode.getTriggered().keySet();
        Assertions.assertEquals(1, keys.size());
        Assertions.assertTrue(keys.iterator().next().startsWith("io.github.achacha.decimated.deadcode.DeadCodeTest.testCallingWithOffsetAndMaxCount:"));
    }


    private static void wrapperForTriggerWithCount(AtomicInteger counter, int totalCount) {
        DeadCode.trigger((accessPoint)-> counter.incrementAndGet(), totalCount, 2);
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
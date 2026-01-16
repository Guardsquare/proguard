/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android.tasks;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CollectConsumerRulesTask.getConsumerRuleIgnoreFilter()
 *
 * Tests: proguard.gradle.plugin.android.tasks.CollectConsumerRulesTask.getConsumerRuleIgnoreFilter.()Ljava/util/List;
 *
 * The getConsumerRuleIgnoreFilter() method returns the List of ConsumerRuleFilterEntry objects
 * that are hardcoded to be ignored during consumer rule collection.
 * The property is marked with @Internal annotation (not a task input/output).
 * By default, this list contains one entry: ConsumerRuleFilterEntry("androidx.window", "window").
 * This is an immutable (val) property that cannot be changed after construction.
 */
public class CollectConsumerRulesTaskClaude_getConsumerRuleIgnoreFilterTest {

    @TempDir
    Path tempDir;

    private Project project;
    private CollectConsumerRulesTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("testCollectConsumerRules", CollectConsumerRulesTask.class);
    }

    // ==================== Basic Getter Tests ====================

    /**
     * Test that getConsumerRuleIgnoreFilter returns non-null.
     * The method should never return null.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_notNull() {
        // When: Getting the consumer rule ignore filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();

        // Then: Should not be null
        assertNotNull(result, "getConsumerRuleIgnoreFilter() should never return null");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter returns List type.
     * The return type should be a List instance.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_returnsListType() {
        // When: Getting the consumer rule ignore filter
        Object result = task.getConsumerRuleIgnoreFilter();

        // Then: Should be a List instance
        assertInstanceOf(List.class, result,
            "getConsumerRuleIgnoreFilter() should return List type");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter returns non-empty list.
     * The default list should contain at least one entry.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_notEmpty() {
        // When: Getting the consumer rule ignore filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();

        // Then: Should not be empty
        assertFalse(result.isEmpty(), "Default consumer rule ignore filter should not be empty");
    }

    // ==================== Default Entry Tests ====================

    /**
     * Test that getConsumerRuleIgnoreFilter returns list with exactly one entry.
     * The default list should have size 1.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_hasOneEntry() {
        // When: Getting the consumer rule ignore filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();

        // Then: Should have exactly one entry
        assertEquals(1, result.size(),
            "Default consumer rule ignore filter should have exactly 1 entry");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter contains androidx.window entry.
     * The default entry should be for androidx.window:window.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_containsAndroidXWindowEntry() {
        // When: Getting the consumer rule ignore filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();

        // Then: Should contain androidx.window entry
        assertEquals(1, result.size(), "Should have one entry");
        ConsumerRuleFilterEntry entry = result.get(0);
        assertEquals("androidx.window", entry.getGroup(),
            "Entry should have group 'androidx.window'");
        assertEquals("window", entry.getModule(),
            "Entry should have module 'window'");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter entry has correct group.
     * The group should be "androidx.window".
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_entryHasCorrectGroup() {
        // When: Getting the consumer rule ignore filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();

        // Then: Entry should have correct group
        ConsumerRuleFilterEntry entry = result.get(0);
        assertEquals("androidx.window", entry.getGroup(),
            "Entry group should be 'androidx.window'");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter entry has correct module.
     * The module should be "window".
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_entryHasCorrectModule() {
        // When: Getting the consumer rule ignore filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();

        // Then: Entry should have correct module
        ConsumerRuleFilterEntry entry = result.get(0);
        assertEquals("window", entry.getModule(),
            "Entry module should be 'window'");
    }

    // ==================== Consistency Tests ====================

    /**
     * Test that getConsumerRuleIgnoreFilter is consistent across multiple calls.
     * Multiple calls should return equal results.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_consistentAcrossMultipleCalls() {
        // When: Getting the filter multiple times
        List<ConsumerRuleFilterEntry> result1 = task.getConsumerRuleIgnoreFilter();
        List<ConsumerRuleFilterEntry> result2 = task.getConsumerRuleIgnoreFilter();
        List<ConsumerRuleFilterEntry> result3 = task.getConsumerRuleIgnoreFilter();

        // Then: All results should be equal
        assertEquals(result1, result2, "First and second calls should return equal lists");
        assertEquals(result2, result3, "Second and third calls should return equal lists");
        assertEquals(result1, result3, "First and third calls should return equal lists");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter returns same list reference.
     * Multiple calls should return the same list object.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_returnsSameListReference() {
        // When: Getting the filter multiple times
        List<ConsumerRuleFilterEntry> result1 = task.getConsumerRuleIgnoreFilter();
        List<ConsumerRuleFilterEntry> result2 = task.getConsumerRuleIgnoreFilter();

        // Then: Should return same reference
        assertSame(result1, result2, "Should return same list object reference");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter list size remains constant.
     * The size should always be 1.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_sizeRemainsConstant() {
        // When: Getting the filter multiple times
        int size1 = task.getConsumerRuleIgnoreFilter().size();
        int size2 = task.getConsumerRuleIgnoreFilter().size();
        int size3 = task.getConsumerRuleIgnoreFilter().size();

        // Then: Size should always be 1
        assertEquals(1, size1, "First call should return size 1");
        assertEquals(1, size2, "Second call should return size 1");
        assertEquals(1, size3, "Third call should return size 1");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter entry details remain constant.
     * The group and module should always be the same.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_entryDetailsRemainConstant() {
        // When: Getting the filter multiple times and checking entry
        ConsumerRuleFilterEntry entry1 = task.getConsumerRuleIgnoreFilter().get(0);
        ConsumerRuleFilterEntry entry2 = task.getConsumerRuleIgnoreFilter().get(0);

        // Then: Entry details should remain constant
        assertEquals(entry1.getGroup(), entry2.getGroup(), "Group should remain constant");
        assertEquals(entry1.getModule(), entry2.getModule(), "Module should remain constant");
    }

    // ==================== Immutability Tests ====================

    /**
     * Test that getConsumerRuleIgnoreFilter cannot be modified after getting.
     * This is a read-only property, so the list should be immutable or protected.
     * Note: This tests the behavior, not necessarily immutability of the returned list.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_listContentIsStable() {
        // Given: Getting the filter
        List<ConsumerRuleFilterEntry> result1 = task.getConsumerRuleIgnoreFilter();
        ConsumerRuleFilterEntry originalEntry = result1.get(0);

        // When: Getting the filter again
        List<ConsumerRuleFilterEntry> result2 = task.getConsumerRuleIgnoreFilter();

        // Then: Content should be the same
        assertEquals(1, result2.size(), "Size should remain 1");
        assertEquals(originalEntry.getGroup(), result2.get(0).getGroup(),
            "Entry group should remain the same");
        assertEquals(originalEntry.getModule(), result2.get(0).getModule(),
            "Entry module should remain the same");
    }

    // ==================== Integration Tests ====================

    /**
     * Test that getConsumerRuleIgnoreFilter works after setting other properties.
     * The ignore filter should remain accessible after setting other task properties.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_worksAfterSettingOtherProperties() {
        // Given: Other task properties are set
        task.setOutputFile(new java.io.File(tempDir.toFile(), "output.txt"));
        task.setConsumerRuleFilter(java.util.Collections.emptyList());

        // When: Getting the consumer rule ignore filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();

        // Then: Should still return the correct filter
        assertNotNull(result, "Should return filter after setting other properties");
        assertEquals(1, result.size(), "Should still have 1 entry");
        assertEquals("androidx.window", result.get(0).getGroup(),
            "Should still contain androidx.window entry");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter is independent from consumerRuleFilter.
     * Changing consumerRuleFilter should not affect consumerRuleIgnoreFilter.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_independentFromConsumerRuleFilter() {
        // Given: Setting consumerRuleFilter
        task.setConsumerRuleFilter(java.util.Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library")
        ));

        // When: Getting the consumer rule ignore filter
        List<ConsumerRuleFilterEntry> ignoreFilter = task.getConsumerRuleIgnoreFilter();
        List<ConsumerRuleFilterEntry> regularFilter = task.getConsumerRuleFilter();

        // Then: They should be different
        assertNotEquals(ignoreFilter, regularFilter,
            "Ignore filter should be independent from regular filter");
        assertEquals(1, ignoreFilter.size(), "Ignore filter should have 1 entry");
        assertEquals("androidx.window", ignoreFilter.get(0).getGroup(),
            "Ignore filter should contain androidx.window");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter is same across different task instances.
     * All task instances should have the same ignore filter content.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_sameAcrossDifferentTaskInstances() {
        // Given: Two different tasks
        CollectConsumerRulesTask task1 = project.getTasks()
                .create("task1", CollectConsumerRulesTask.class);
        CollectConsumerRulesTask task2 = project.getTasks()
                .create("task2", CollectConsumerRulesTask.class);

        // When: Getting filters from both tasks
        List<ConsumerRuleFilterEntry> filter1 = task1.getConsumerRuleIgnoreFilter();
        List<ConsumerRuleFilterEntry> filter2 = task2.getConsumerRuleIgnoreFilter();

        // Then: Both should have the same content
        assertEquals(filter1.size(), filter2.size(), "Both should have same size");
        assertEquals(filter1.get(0).getGroup(), filter2.get(0).getGroup(),
            "Both should have same group");
        assertEquals(filter1.get(0).getModule(), filter2.get(0).getModule(),
            "Both should have same module");
    }

    // ==================== Task Lifecycle Tests ====================

    /**
     * Test that getConsumerRuleIgnoreFilter works immediately after task creation.
     * The getter should be available right after task instantiation.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_availableImmediatelyAfterCreation() {
        // Given: A newly created task
        CollectConsumerRulesTask newTask = project.getTasks()
                .create("newTask", CollectConsumerRulesTask.class);

        // When: Getting the filter immediately
        List<ConsumerRuleFilterEntry> result = newTask.getConsumerRuleIgnoreFilter();

        // Then: Should return the default filter
        assertNotNull(result, "Should be available immediately");
        assertEquals(1, result.size(), "Should have 1 entry");
        assertEquals("androidx.window", result.get(0).getGroup(),
            "Should contain androidx.window entry");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter works with task created from different project.
     * The getter should work regardless of the project the task belongs to.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_worksWithDifferentProject() {
        // Given: A task from a different project
        Project anotherProject = ProjectBuilder.builder()
                .withProjectDir(tempDir.resolve("another").toFile())
                .build();
        CollectConsumerRulesTask anotherTask = anotherProject.getTasks()
                .create("anotherTask", CollectConsumerRulesTask.class);

        // When: Getting the consumer rule ignore filter
        List<ConsumerRuleFilterEntry> result = anotherTask.getConsumerRuleIgnoreFilter();

        // Then: Should return the correct filter
        assertEquals(1, result.size(), "Should work with different project");
        assertEquals("androidx.window", result.get(0).getGroup(),
            "Should contain androidx.window entry");
    }

    // ==================== Gradle Task Integration Tests ====================

    /**
     * Test that getConsumerRuleIgnoreFilter integrates with Gradle task system.
     * The property should be accessible through the task.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_integratesWithGradleTask() {
        // When: Getting the filter through the task
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();

        // Then: Should be accessible through Gradle task methods
        assertNotNull(result, "Filter should be accessible through task");
        assertEquals(task.getProject(), project,
            "Task should still belong to correct project");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter works on task retrieved from project.
     * The filter should be accessible when task is retrieved from project.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_worksOnRetrievedTask() {
        // Given: A task that is retrieved from project
        CollectConsumerRulesTask retrievedTask = (CollectConsumerRulesTask)
            project.getTasks().findByName("testCollectConsumerRules");

        // When: Getting the filter from retrieved task
        List<ConsumerRuleFilterEntry> result = retrievedTask.getConsumerRuleIgnoreFilter();

        // Then: Should return the correct filter
        assertEquals(1, result.size(), "Retrieved task should have correct filter");
        assertEquals("androidx.window", result.get(0).getGroup(),
            "Retrieved task should have androidx.window entry");
    }

    // ==================== Entry Object Tests ====================

    /**
     * Test that getConsumerRuleIgnoreFilter entry is a valid ConsumerRuleFilterEntry.
     * The entry should be a proper ConsumerRuleFilterEntry instance.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_entryIsValidConsumerRuleFilterEntry() {
        // When: Getting the filter and its entry
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();
        ConsumerRuleFilterEntry entry = result.get(0);

        // Then: Entry should be valid ConsumerRuleFilterEntry
        assertNotNull(entry, "Entry should not be null");
        assertInstanceOf(ConsumerRuleFilterEntry.class, entry,
            "Entry should be ConsumerRuleFilterEntry instance");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter entry has non-null group.
     * The group should never be null.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_entryGroupNotNull() {
        // When: Getting the filter and its entry
        ConsumerRuleFilterEntry entry = task.getConsumerRuleIgnoreFilter().get(0);

        // Then: Group should not be null
        assertNotNull(entry.getGroup(), "Entry group should not be null");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter entry has non-null module.
     * The module should never be null.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_entryModuleNotNull() {
        // When: Getting the filter and its entry
        ConsumerRuleFilterEntry entry = task.getConsumerRuleIgnoreFilter().get(0);

        // Then: Module should not be null
        assertNotNull(entry.getModule(), "Entry module should not be null");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter entry has non-empty group.
     * The group should not be an empty string.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_entryGroupNotEmpty() {
        // When: Getting the filter and its entry
        ConsumerRuleFilterEntry entry = task.getConsumerRuleIgnoreFilter().get(0);

        // Then: Group should not be empty
        assertFalse(entry.getGroup().isEmpty(), "Entry group should not be empty");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter entry has non-empty module.
     * The module should not be an empty string.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_entryModuleNotEmpty() {
        // When: Getting the filter and its entry
        ConsumerRuleFilterEntry entry = task.getConsumerRuleIgnoreFilter().get(0);

        // Then: Module should not be empty
        assertFalse(entry.getModule().isEmpty(), "Entry module should not be empty");
    }

    // ==================== List Characteristics Tests ====================

    /**
     * Test that getConsumerRuleIgnoreFilter can be iterated.
     * The returned list should be iterable.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_canBeIterated() {
        // When: Getting the filter and iterating
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();
        int count = 0;
        for (ConsumerRuleFilterEntry entry : result) {
            count++;
            assertNotNull(entry, "Iterated entry should not be null");
        }

        // Then: Should iterate over all entries
        assertEquals(1, count, "Should iterate over 1 entry");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter list can be accessed by index.
     * The list should support indexed access.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_supportsIndexAccess() {
        // When: Getting the filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();

        // Then: Should support indexed access
        assertDoesNotThrow(() -> result.get(0),
            "Should support indexed access");
        ConsumerRuleFilterEntry entry = result.get(0);
        assertNotNull(entry, "Entry at index 0 should not be null");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter list contains the entry.
     * The contains() method should work correctly.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_containsEntry() {
        // When: Getting the filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();
        ConsumerRuleFilterEntry entry = result.get(0);

        // Then: List should contain the entry
        assertTrue(result.contains(entry), "List should contain the entry");
    }

    // ==================== Behavior Verification Tests ====================

    /**
     * Test that getConsumerRuleIgnoreFilter always returns list with androidx.window.
     * No matter what other operations are done, this entry should always be present.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_alwaysContainsAndroidXWindow() {
        // Given: Various operations on the task
        task.setConsumerRuleFilter(java.util.Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library")
        ));
        task.setOutputFile(new java.io.File(tempDir.toFile(), "output.txt"));

        // When: Getting the ignore filter after operations
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();

        // Then: Should still contain androidx.window
        assertEquals(1, result.size(), "Should still have 1 entry");
        assertEquals("androidx.window", result.get(0).getGroup(),
            "Should still contain androidx.window");
        assertEquals("window", result.get(0).getModule(),
            "Should still contain window module");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter value matches documented behavior.
     * The method should return exactly what is documented in the source code comments.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_matchesDocumentedBehavior() {
        // When: Getting the filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleIgnoreFilter();

        // Then: Should match documented behavior (T13715 - androidx.window workaround)
        assertEquals(1, result.size(),
            "Should have 1 entry as per T13715 workaround");
        assertEquals("androidx.window", result.get(0).getGroup(),
            "Should be androidx.window as documented");
        assertEquals("window", result.get(0).getModule(),
            "Should be window module as documented");
    }

    // ==================== Comparison Tests ====================

    /**
     * Test that getConsumerRuleIgnoreFilter differs from getConsumerRuleFilter.
     * These are two different properties with different purposes.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_differFromConsumerRuleFilter() {
        // When: Getting both filters
        List<ConsumerRuleFilterEntry> ignoreFilter = task.getConsumerRuleIgnoreFilter();
        List<ConsumerRuleFilterEntry> regularFilter = task.getConsumerRuleFilter();

        // Then: They should be different
        assertNotSame(ignoreFilter, regularFilter,
            "Ignore filter and regular filter should be different objects");
    }

    /**
     * Test that getConsumerRuleIgnoreFilter has different content than default consumerRuleFilter.
     * Ignore filter has androidx.window, while regular filter is empty by default.
     */
    @Test
    public void testGetConsumerRuleIgnoreFilter_differentContentThanConsumerRuleFilter() {
        // When: Getting both filters
        List<ConsumerRuleFilterEntry> ignoreFilter = task.getConsumerRuleIgnoreFilter();
        List<ConsumerRuleFilterEntry> regularFilter = task.getConsumerRuleFilter();

        // Then: They should have different content
        assertEquals(1, ignoreFilter.size(), "Ignore filter should have 1 entry");
        assertEquals(0, regularFilter.size(), "Regular filter should be empty by default");
        assertNotEquals(ignoreFilter.size(), regularFilter.size(),
            "Sizes should be different");
    }
}

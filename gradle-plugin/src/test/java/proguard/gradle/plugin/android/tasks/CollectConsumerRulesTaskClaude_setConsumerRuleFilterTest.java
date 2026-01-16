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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CollectConsumerRulesTask.setConsumerRuleFilter(List)
 *
 * Tests: proguard.gradle.plugin.android.tasks.CollectConsumerRulesTask.setConsumerRuleFilter.(Ljava/util/List;)V
 *
 * The setConsumerRuleFilter(List) method sets the list of ConsumerRuleFilterEntry objects
 * that define which consumer rules to skip during collection.
 * The property is marked with @Input annotation, making it a task input.
 */
public class CollectConsumerRulesTaskClaude_setConsumerRuleFilterTest {

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

    // ==================== Basic Setter Tests ====================

    /**
     * Test that setConsumerRuleFilter accepts a valid list.
     * The setter should accept a List without throwing exceptions.
     */
    @Test
    public void testSetConsumerRuleFilter_acceptsValidList() {
        // Given: A valid list of filter entries
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );

        // When: Setting the consumer rule filter
        // Then: Should not throw any exception
        assertDoesNotThrow(() -> task.setConsumerRuleFilter(filter),
            "setConsumerRuleFilter should accept valid list without throwing exception");
    }

    /**
     * Test that setConsumerRuleFilter sets the list correctly.
     * After calling setConsumerRuleFilter, getConsumerRuleFilter should return that list.
     */
    @Test
    public void testSetConsumerRuleFilter_setsListCorrectly() {
        // Given: A list to set
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );

        // When: Setting the consumer rule filter
        task.setConsumerRuleFilter(filter);

        // Then: getConsumerRuleFilter should return the same list
        assertEquals(filter, task.getConsumerRuleFilter(),
            "setConsumerRuleFilter should set the list so getConsumerRuleFilter returns it");
    }

    /**
     * Test that setConsumerRuleFilter returns void.
     * The method should have void return type (no return value to check).
     */
    @Test
    public void testSetConsumerRuleFilter_hasVoidReturn() {
        // Given: A list to set
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );

        // When: Setting the consumer rule filter (checking it doesn't return anything meaningful)
        task.setConsumerRuleFilter(filter);

        // Then: Method completes successfully (void return is implicit)
        // This test verifies the method signature is correct
        assertNotNull(task.getConsumerRuleFilter(), "List should be set after calling setConsumerRuleFilter");
    }

    // ==================== Empty List Tests ====================

    /**
     * Test that setConsumerRuleFilter accepts empty list.
     * An empty list should be accepted.
     */
    @Test
    public void testSetConsumerRuleFilter_acceptsEmptyList() {
        // Given: An empty list
        List<ConsumerRuleFilterEntry> emptyFilter = Collections.emptyList();

        // When: Setting the empty filter
        task.setConsumerRuleFilter(emptyFilter);

        // Then: Should be set successfully
        assertTrue(task.getConsumerRuleFilter().isEmpty(),
            "Should accept empty list");
    }

    /**
     * Test that setConsumerRuleFilter accepts new ArrayList empty list.
     * An empty ArrayList should be accepted.
     */
    @Test
    public void testSetConsumerRuleFilter_acceptsNewArrayListEmpty() {
        // Given: An empty ArrayList
        List<ConsumerRuleFilterEntry> emptyFilter = new ArrayList<>();

        // When: Setting the empty filter
        task.setConsumerRuleFilter(emptyFilter);

        // Then: Should be set successfully
        assertTrue(task.getConsumerRuleFilter().isEmpty(),
            "Should accept empty ArrayList");
    }

    /**
     * Test that setConsumerRuleFilter can clear existing filter with empty list.
     * Setting an empty list should clear any previous filter.
     */
    @Test
    public void testSetConsumerRuleFilter_clearsWithEmptyList() {
        // Given: A filter with entries is already set
        List<ConsumerRuleFilterEntry> initialFilter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        task.setConsumerRuleFilter(initialFilter);
        assertFalse(task.getConsumerRuleFilter().isEmpty(), "Initial filter should not be empty");

        // When: Setting an empty list
        task.setConsumerRuleFilter(Collections.emptyList());

        // Then: Should clear the filter
        assertTrue(task.getConsumerRuleFilter().isEmpty(),
            "Should clear filter with empty list");
    }

    // ==================== Single Entry Tests ====================

    /**
     * Test that setConsumerRuleFilter accepts single entry.
     * A list with one entry should be accepted.
     */
    @Test
    public void testSetConsumerRuleFilter_acceptsSingleEntry() {
        // Given: A list with single entry
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should be set successfully
        assertEquals(1, task.getConsumerRuleFilter().size(),
            "Should accept single entry list");
    }

    /**
     * Test that setConsumerRuleFilter preserves entry details.
     * The group and module of entries should be preserved.
     */
    @Test
    public void testSetConsumerRuleFilter_preservesEntryDetails() {
        // Given: A filter entry with specific group and module
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("androidx.core", "core-ktx")
        );

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should preserve entry details
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();
        assertEquals("androidx.core", result.get(0).getGroup(),
            "Group should be preserved");
        assertEquals("core-ktx", result.get(0).getModule(),
            "Module should be preserved");
    }

    // ==================== Multiple Entry Tests ====================

    /**
     * Test that setConsumerRuleFilter accepts multiple entries.
     * A list with multiple entries should be accepted.
     */
    @Test
    public void testSetConsumerRuleFilter_acceptsMultipleEntries() {
        // Given: A list with multiple entries
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library1"),
            new ConsumerRuleFilterEntry("com.example", "library2"),
            new ConsumerRuleFilterEntry("com.other", "library3")
        );

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should be set successfully
        assertEquals(3, task.getConsumerRuleFilter().size(),
            "Should accept multiple entries");
    }

    /**
     * Test that setConsumerRuleFilter preserves entry order.
     * The order of entries should be maintained.
     */
    @Test
    public void testSetConsumerRuleFilter_preservesOrder() {
        // Given: A list with entries in specific order
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("a.group", "module1");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("b.group", "module2");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("c.group", "module3");
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(entry1, entry2, entry3);

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should preserve order
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();
        assertEquals(entry1, result.get(0), "First entry should be at index 0");
        assertEquals(entry2, result.get(1), "Second entry should be at index 1");
        assertEquals(entry3, result.get(2), "Third entry should be at index 2");
    }

    /**
     * Test that setConsumerRuleFilter accepts many entries.
     * Large lists should be handled correctly.
     */
    @Test
    public void testSetConsumerRuleFilter_acceptsManyEntries() {
        // Given: A list with many entries
        List<ConsumerRuleFilterEntry> filter = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            filter.add(new ConsumerRuleFilterEntry("com.example.group" + i, "module" + i));
        }

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should be set successfully
        assertEquals(20, task.getConsumerRuleFilter().size(),
            "Should accept many entries");
    }

    // ==================== Duplicate Entry Tests ====================

    /**
     * Test that setConsumerRuleFilter allows duplicate entries.
     * The list can contain duplicate ConsumerRuleFilterEntry objects.
     */
    @Test
    public void testSetConsumerRuleFilter_allowsDuplicateEntries() {
        // Given: A list with duplicate entries
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(entry, entry, entry);

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should accept duplicates
        assertEquals(3, task.getConsumerRuleFilter().size(),
            "Should accept duplicate entries");
    }

    /**
     * Test that setConsumerRuleFilter allows entries with same values.
     * Entries with identical group and module values should be allowed.
     */
    @Test
    public void testSetConsumerRuleFilter_allowsEntriesWithSameValues() {
        // Given: A list with entries having same values but different objects
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library"),
            new ConsumerRuleFilterEntry("com.example", "library")
        );

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should accept entries with same values
        assertEquals(2, task.getConsumerRuleFilter().size(),
            "Should accept entries with same values");
    }

    // ==================== Multiple Set Tests ====================

    /**
     * Test that setConsumerRuleFilter can be called multiple times.
     * The setter should allow changing the filter.
     */
    @Test
    public void testSetConsumerRuleFilter_canBeCalledMultipleTimes() {
        // Given: Setting an initial filter
        List<ConsumerRuleFilterEntry> firstFilter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.first", "library")
        );
        task.setConsumerRuleFilter(firstFilter);

        // When: Setting a different filter
        List<ConsumerRuleFilterEntry> secondFilter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.second", "library")
        );
        task.setConsumerRuleFilter(secondFilter);

        // Then: Should update to the new filter
        assertEquals(secondFilter, task.getConsumerRuleFilter(),
            "Should update to the new filter");
        assertEquals("com.second", task.getConsumerRuleFilter().get(0).getGroup(),
            "Should have new group");
    }

    /**
     * Test that setConsumerRuleFilter overwrites previous value.
     * Each call to setConsumerRuleFilter should replace the previous value.
     */
    @Test
    public void testSetConsumerRuleFilter_overwritesPreviousValue() {
        // Given: Multiple filters
        List<ConsumerRuleFilterEntry> filter1 = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example1", "library1")
        );
        List<ConsumerRuleFilterEntry> filter2 = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example2", "library2")
        );
        List<ConsumerRuleFilterEntry> filter3 = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example3", "library3")
        );

        // When: Setting the filter multiple times
        task.setConsumerRuleFilter(filter1);
        task.setConsumerRuleFilter(filter2);
        task.setConsumerRuleFilter(filter3);

        // Then: Should only have the last value
        assertEquals(filter3, task.getConsumerRuleFilter(),
            "Should have the last set value");
        assertEquals("com.example3", task.getConsumerRuleFilter().get(0).getGroup(),
            "Should have last group");
    }

    /**
     * Test that setConsumerRuleFilter can change list size.
     * The setter should allow changing from larger to smaller lists and vice versa.
     */
    @Test
    public void testSetConsumerRuleFilter_canChangeListSize() {
        // Given: A filter with multiple entries
        List<ConsumerRuleFilterEntry> largeFilter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example1", "library1"),
            new ConsumerRuleFilterEntry("com.example2", "library2"),
            new ConsumerRuleFilterEntry("com.example3", "library3")
        );
        task.setConsumerRuleFilter(largeFilter);
        assertEquals(3, task.getConsumerRuleFilter().size(), "Should have 3 entries");

        // When: Setting a smaller filter
        List<ConsumerRuleFilterEntry> smallFilter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        task.setConsumerRuleFilter(smallFilter);

        // Then: Should update to smaller size
        assertEquals(1, task.getConsumerRuleFilter().size(),
            "Should change to smaller list");
    }

    // ==================== Integration Tests ====================

    /**
     * Test that setConsumerRuleFilter works with other task properties.
     * Setting filter shouldn't interfere with other properties.
     */
    @Test
    public void testSetConsumerRuleFilter_worksWithOtherProperties() {
        // Given: Other task properties are set
        task.setOutputFile(new java.io.File(tempDir.toFile(), "output.txt"));

        // When: Setting the consumer rule filter
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        task.setConsumerRuleFilter(filter);

        // Then: Both properties should be accessible
        assertEquals(filter, task.getConsumerRuleFilter(),
            "Filter should be set");
        assertNotNull(task.getOutputFile(),
            "Other properties should still be accessible");
    }

    /**
     * Test that setConsumerRuleFilter on different tasks are independent.
     * Each task instance should maintain its own filter.
     */
    @Test
    public void testSetConsumerRuleFilter_independentAcrossTasks() {
        // Given: Two different tasks
        CollectConsumerRulesTask task1 = project.getTasks()
                .create("task1", CollectConsumerRulesTask.class);
        CollectConsumerRulesTask task2 = project.getTasks()
                .create("task2", CollectConsumerRulesTask.class);

        List<ConsumerRuleFilterEntry> filter1 = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example1", "library1")
        );
        List<ConsumerRuleFilterEntry> filter2 = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example2", "library2")
        );

        // When: Setting different filters
        task1.setConsumerRuleFilter(filter1);
        task2.setConsumerRuleFilter(filter2);

        // Then: Each task should have its own filter
        assertNotEquals(task1.getConsumerRuleFilter(), task2.getConsumerRuleFilter(),
            "Tasks should have different filters");
        assertEquals("com.example1", task1.getConsumerRuleFilter().get(0).getGroup(),
            "Task1 should have its filter");
        assertEquals("com.example2", task2.getConsumerRuleFilter().get(0).getGroup(),
            "Task2 should have its filter");
    }

    // ==================== Different Entry Types Tests ====================

    /**
     * Test that setConsumerRuleFilter accepts various group names.
     * Different group name patterns should be accepted.
     */
    @Test
    public void testSetConsumerRuleFilter_acceptsVariousGroupNames() {
        // Given: A filter with various group name patterns
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "lib"),
            new ConsumerRuleFilterEntry("org.test", "lib"),
            new ConsumerRuleFilterEntry("androidx.core", "lib"),
            new ConsumerRuleFilterEntry("io.github.user", "lib")
        );

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should accept all patterns
        assertEquals(4, task.getConsumerRuleFilter().size(),
            "Should accept various group names");
    }

    /**
     * Test that setConsumerRuleFilter accepts various module names.
     * Different module name patterns should be accepted.
     */
    @Test
    public void testSetConsumerRuleFilter_acceptsVariousModuleNames() {
        // Given: A filter with various module name patterns
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library"),
            new ConsumerRuleFilterEntry("com.example", "library-ktx"),
            new ConsumerRuleFilterEntry("com.example", "library_utils"),
            new ConsumerRuleFilterEntry("com.example", "library.module")
        );

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should accept all patterns
        assertEquals(4, task.getConsumerRuleFilter().size(),
            "Should accept various module names");
    }

    // ==================== List Type Tests ====================

    /**
     * Test that setConsumerRuleFilter works with Collections.singletonList.
     * Single-element immutable lists should work.
     */
    @Test
    public void testSetConsumerRuleFilter_singletonList() {
        // Given: A singleton list
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should work correctly
        assertEquals(1, task.getConsumerRuleFilter().size(),
            "Should work with singleton list");
    }

    /**
     * Test that setConsumerRuleFilter works with Arrays.asList.
     * Fixed-size lists created with Arrays.asList should work.
     */
    @Test
    public void testSetConsumerRuleFilter_arraysAsList() {
        // Given: An Arrays.asList
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library1"),
            new ConsumerRuleFilterEntry("com.example", "library2")
        );

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should work correctly
        assertEquals(2, task.getConsumerRuleFilter().size(),
            "Should work with Arrays.asList");
    }

    /**
     * Test that setConsumerRuleFilter works with ArrayList.
     * Mutable ArrayLists should work.
     */
    @Test
    public void testSetConsumerRuleFilter_arrayList() {
        // Given: An ArrayList
        List<ConsumerRuleFilterEntry> filter = new ArrayList<>();
        filter.add(new ConsumerRuleFilterEntry("com.example", "library1"));
        filter.add(new ConsumerRuleFilterEntry("com.example", "library2"));

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should work correctly
        assertEquals(2, task.getConsumerRuleFilter().size(),
            "Should work with ArrayList");
    }

    /**
     * Test that setConsumerRuleFilter works with Collections.emptyList.
     * Empty immutable list should work.
     */
    @Test
    public void testSetConsumerRuleFilter_collectionsEmptyList() {
        // Given: Collections.emptyList()
        List<ConsumerRuleFilterEntry> filter = Collections.emptyList();

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should work correctly
        assertTrue(task.getConsumerRuleFilter().isEmpty(),
            "Should work with Collections.emptyList");
    }

    // ==================== List Reference Tests ====================

    /**
     * Test that setConsumerRuleFilter stores the same list reference.
     * The exact List object should be stored.
     */
    @Test
    public void testSetConsumerRuleFilter_storesSameListReference() {
        // Given: A List object
        List<ConsumerRuleFilterEntry> filter = new ArrayList<>();
        filter.add(new ConsumerRuleFilterEntry("com.example", "library"));

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should store the same reference
        assertSame(filter, task.getConsumerRuleFilter(),
            "Should store the same List object reference");
    }

    /**
     * Test that setConsumerRuleFilter accepts same list twice.
     * Setting the same list again should work.
     */
    @Test
    public void testSetConsumerRuleFilter_acceptsSameListTwice() {
        // Given: A List object
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );

        // When: Setting the same list twice
        task.setConsumerRuleFilter(filter);
        task.setConsumerRuleFilter(filter);

        // Then: Should still be set correctly
        assertEquals(filter, task.getConsumerRuleFilter(),
            "Should accept setting same list twice");
    }

    // ==================== Edge Cases ====================

    /**
     * Test that setConsumerRuleFilter accepts entries with empty strings.
     * Empty group or module strings should be handled.
     */
    @Test
    public void testSetConsumerRuleFilter_acceptsEntriesWithEmptyStrings() {
        // Given: A filter with entries containing empty strings
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("", "module"),
            new ConsumerRuleFilterEntry("group", "")
        );

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should accept entries with empty strings
        assertEquals(2, task.getConsumerRuleFilter().size(),
            "Should accept entries with empty strings");
    }

    /**
     * Test that setConsumerRuleFilter can be called immediately after task creation.
     * The setter should be available right after task instantiation.
     */
    @Test
    public void testSetConsumerRuleFilter_availableImmediatelyAfterCreation() {
        // Given: A newly created task
        CollectConsumerRulesTask newTask = project.getTasks()
                .create("newTask", CollectConsumerRulesTask.class);
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );

        // When: Setting filter immediately
        newTask.setConsumerRuleFilter(filter);

        // Then: Should work without issues
        assertEquals(filter, newTask.getConsumerRuleFilter(),
            "Should be able to set filter immediately after task creation");
    }

    /**
     * Test that setConsumerRuleFilter works with task created from different project.
     * The setter should work regardless of the project the task belongs to.
     */
    @Test
    public void testSetConsumerRuleFilter_worksWithDifferentProject() {
        // Given: A task from a different project
        Project anotherProject = ProjectBuilder.builder()
                .withProjectDir(tempDir.resolve("another").toFile())
                .build();
        CollectConsumerRulesTask anotherTask = anotherProject.getTasks()
                .create("anotherTask", CollectConsumerRulesTask.class);
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );

        // When: Setting the filter
        anotherTask.setConsumerRuleFilter(filter);

        // Then: Should work correctly
        assertEquals(filter, anotherTask.getConsumerRuleFilter(),
            "Filter should work with different project");
    }

    // ==================== Gradle Task Integration Tests ====================

    /**
     * Test that setConsumerRuleFilter integrates with Gradle task system.
     * The property should be accessible through the task.
     */
    @Test
    public void testSetConsumerRuleFilter_integratesWithGradleTask() {
        // Given: A filter to set
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );

        // When: Setting through the task
        task.setConsumerRuleFilter(filter);

        // Then: Should be accessible through Gradle task methods
        assertNotNull(task.getConsumerRuleFilter(),
            "Filter should be accessible through task");
        assertEquals(task.getProject(), project,
            "Task should still belong to correct project");
    }

    /**
     * Test that setConsumerRuleFilter replaces default empty list.
     * Setting a filter should replace the default empty list.
     */
    @Test
    public void testSetConsumerRuleFilter_replacesDefaultEmptyList() {
        // Given: Default state (empty list)
        assertTrue(task.getConsumerRuleFilter().isEmpty(),
            "Default should be empty list");

        // When: Setting a filter with entries
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        task.setConsumerRuleFilter(filter);

        // Then: Should replace default empty list
        assertFalse(task.getConsumerRuleFilter().isEmpty(),
            "Should replace default empty list");
        assertEquals(1, task.getConsumerRuleFilter().size(),
            "Should have one entry");
    }

    /**
     * Test that setConsumerRuleFilter maintains list after getting.
     * Getting the list shouldn't affect subsequent sets.
     */
    @Test
    public void testSetConsumerRuleFilter_maintainsListAfterGetting() {
        // Given: A filter is set and retrieved
        List<ConsumerRuleFilterEntry> filter1 = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example1", "library1")
        );
        task.setConsumerRuleFilter(filter1);
        List<ConsumerRuleFilterEntry> retrieved = task.getConsumerRuleFilter();
        assertNotNull(retrieved, "Should retrieve filter");

        // When: Setting a new filter after getting
        List<ConsumerRuleFilterEntry> filter2 = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example2", "library2")
        );
        task.setConsumerRuleFilter(filter2);

        // Then: Should update correctly
        assertEquals(filter2, task.getConsumerRuleFilter(),
            "Should set new filter after getting previous one");
        assertEquals("com.example2", task.getConsumerRuleFilter().get(0).getGroup(),
            "Should have new group");
    }

    /**
     * Test that setConsumerRuleFilter with mixed entry types.
     * A list with entries having different group/module combinations should work.
     */
    @Test
    public void testSetConsumerRuleFilter_mixedEntryTypes() {
        // Given: A filter with various entry combinations
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library"),
            new ConsumerRuleFilterEntry("org.test.long.package.name", "short"),
            new ConsumerRuleFilterEntry("a", "very-long-module-name-here"),
            new ConsumerRuleFilterEntry("androidx.core", "core-ktx"),
            new ConsumerRuleFilterEntry("io.github.user.project", "module-name")
        );

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should handle all combinations
        assertEquals(5, task.getConsumerRuleFilter().size(),
            "Should handle mixed entry types");
    }

    /**
     * Test that setConsumerRuleFilter preserves all list characteristics.
     * Size, order, and content should all be preserved.
     */
    @Test
    public void testSetConsumerRuleFilter_preservesAllListCharacteristics() {
        // Given: A specific filter list
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.group1", "module1");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.group2", "module2");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("com.group3", "module3");
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(entry1, entry2, entry3);

        // When: Setting the filter
        task.setConsumerRuleFilter(filter);

        // Then: Should preserve size, order, and content
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();
        assertEquals(3, result.size(), "Should preserve size");
        assertEquals(entry1, result.get(0), "Should preserve first entry");
        assertEquals(entry2, result.get(1), "Should preserve second entry");
        assertEquals(entry3, result.get(2), "Should preserve third entry");
        assertEquals("com.group1", result.get(0).getGroup(), "Should preserve first group");
        assertEquals("module2", result.get(1).getModule(), "Should preserve second module");
    }
}

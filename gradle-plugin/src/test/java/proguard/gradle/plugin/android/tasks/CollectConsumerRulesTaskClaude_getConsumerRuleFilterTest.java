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
 * Test class for CollectConsumerRulesTask.getConsumerRuleFilter()
 *
 * Tests: proguard.gradle.plugin.android.tasks.CollectConsumerRulesTask.getConsumerRuleFilter.()Ljava/util/List;
 *
 * The getConsumerRuleFilter() method returns the List of ConsumerRuleFilterEntry objects
 * that have been set to filter which consumer rules to skip during collection.
 * The property is marked with @Input annotation, making it a task input.
 * By default, this list is initialized to an empty list.
 */
public class CollectConsumerRulesTaskClaude_getConsumerRuleFilterTest {

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

    // ==================== Default State Tests ====================

    /**
     * Test that getConsumerRuleFilter returns empty list by default.
     * When no filter is set, the default should be an empty list.
     */
    @Test
    public void testGetConsumerRuleFilter_returnsEmptyListByDefault() {
        // When: Getting the consumer rule filter without setting it
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should return empty list
        assertNotNull(result, "getConsumerRuleFilter() should not return null");
        assertTrue(result.isEmpty(), "Default consumer rule filter should be empty");
        assertEquals(0, result.size(), "Default size should be 0");
    }

    /**
     * Test that getConsumerRuleFilter returns non-null by default.
     * The method should never return null.
     */
    @Test
    public void testGetConsumerRuleFilter_notNullByDefault() {
        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should not be null
        assertNotNull(result, "getConsumerRuleFilter() should never return null");
    }

    /**
     * Test that getConsumerRuleFilter returns List type.
     * The return type should be a List instance.
     */
    @Test
    public void testGetConsumerRuleFilter_returnsListType() {
        // When: Getting the consumer rule filter
        Object result = task.getConsumerRuleFilter();

        // Then: Should be a List instance
        assertInstanceOf(List.class, result,
            "getConsumerRuleFilter() should return List type");
    }

    // ==================== Basic Getter Tests ====================

    /**
     * Test that getConsumerRuleFilter returns the list that was set.
     * After setting a filter list, the getter should return that same list.
     */
    @Test
    public void testGetConsumerRuleFilter_returnsSetList() {
        // Given: A filter list is set
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should return the same list
        assertNotNull(result, "getConsumerRuleFilter() should not return null");
        assertEquals(filter, result, "Should return the exact list that was set");
    }

    /**
     * Test that getConsumerRuleFilter preserves list size.
     * The returned list should have the same size as the set list.
     */
    @Test
    public void testGetConsumerRuleFilter_preservesListSize() {
        // Given: A filter list with multiple entries is set
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library1"),
            new ConsumerRuleFilterEntry("com.example", "library2"),
            new ConsumerRuleFilterEntry("com.other", "library3")
        );
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should preserve the size
        assertEquals(3, result.size(), "Should preserve list size");
        assertEquals(filter.size(), result.size(), "Size should match the set list");
    }

    /**
     * Test that getConsumerRuleFilter preserves list entries.
     * The returned list should contain the same entries in the same order.
     */
    @Test
    public void testGetConsumerRuleFilter_preservesListEntries() {
        // Given: A filter list is set
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library1");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example", "library2");
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(entry1, entry2);
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should contain the same entries
        assertEquals(entry1, result.get(0), "First entry should match");
        assertEquals(entry2, result.get(1), "Second entry should match");
    }

    // ==================== Empty List Tests ====================

    /**
     * Test that getConsumerRuleFilter works with explicitly set empty list.
     * An explicitly set empty list should be returned.
     */
    @Test
    public void testGetConsumerRuleFilter_worksWithEmptyList() {
        // Given: An empty list is explicitly set
        List<ConsumerRuleFilterEntry> emptyFilter = Collections.emptyList();
        task.setConsumerRuleFilter(emptyFilter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should return empty list
        assertNotNull(result, "Should not return null");
        assertTrue(result.isEmpty(), "Should return empty list");
        assertEquals(0, result.size(), "Size should be 0");
    }

    /**
     * Test that getConsumerRuleFilter works with new ArrayList empty list.
     * An empty ArrayList should be handled correctly.
     */
    @Test
    public void testGetConsumerRuleFilter_worksWithNewArrayListEmpty() {
        // Given: An empty ArrayList is set
        List<ConsumerRuleFilterEntry> emptyFilter = new ArrayList<>();
        task.setConsumerRuleFilter(emptyFilter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should return empty list
        assertTrue(result.isEmpty(), "Should return empty list");
    }

    // ==================== Single Entry Tests ====================

    /**
     * Test that getConsumerRuleFilter works with single entry.
     * A list with one entry should be returned correctly.
     */
    @Test
    public void testGetConsumerRuleFilter_singleEntry() {
        // Given: A filter with single entry is set
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(entry);
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should contain single entry
        assertEquals(1, result.size(), "Should have one entry");
        assertEquals(entry, result.get(0), "Entry should match");
    }

    /**
     * Test that getConsumerRuleFilter preserves entry details.
     * The group and module of entries should be preserved.
     */
    @Test
    public void testGetConsumerRuleFilter_preservesEntryDetails() {
        // Given: A filter entry with specific group and module
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("androidx.core", "core-ktx");
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(entry);
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should preserve entry details
        assertEquals("androidx.core", result.get(0).getGroup(),
            "Group should be preserved");
        assertEquals("core-ktx", result.get(0).getModule(),
            "Module should be preserved");
    }

    // ==================== Multiple Entry Tests ====================

    /**
     * Test that getConsumerRuleFilter works with multiple entries.
     * A list with multiple entries should be returned correctly.
     */
    @Test
    public void testGetConsumerRuleFilter_multipleEntries() {
        // Given: A filter with multiple entries is set
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library1"),
            new ConsumerRuleFilterEntry("com.example", "library2"),
            new ConsumerRuleFilterEntry("com.other", "library3")
        );
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should contain all entries
        assertEquals(3, result.size(), "Should have three entries");
    }

    /**
     * Test that getConsumerRuleFilter preserves entry order.
     * The order of entries should be maintained.
     */
    @Test
    public void testGetConsumerRuleFilter_preservesOrder() {
        // Given: A filter with entries in specific order
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("a.group", "module1");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("b.group", "module2");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("c.group", "module3");
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(entry1, entry2, entry3);
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should preserve order
        assertEquals(entry1, result.get(0), "First entry should be at index 0");
        assertEquals(entry2, result.get(1), "Second entry should be at index 1");
        assertEquals(entry3, result.get(2), "Third entry should be at index 2");
    }

    /**
     * Test that getConsumerRuleFilter works with many entries.
     * Large lists should be handled correctly.
     */
    @Test
    public void testGetConsumerRuleFilter_manyEntries() {
        // Given: A filter with many entries is set
        List<ConsumerRuleFilterEntry> filter = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            filter.add(new ConsumerRuleFilterEntry("com.example.group" + i, "module" + i));
        }
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should contain all entries
        assertEquals(20, result.size(), "Should have 20 entries");
    }

    // ==================== Duplicate Entry Tests ====================

    /**
     * Test that getConsumerRuleFilter allows duplicate entries.
     * The list can contain duplicate ConsumerRuleFilterEntry objects.
     */
    @Test
    public void testGetConsumerRuleFilter_allowsDuplicateEntries() {
        // Given: A filter with duplicate entries
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(entry, entry, entry);
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should contain all duplicates
        assertEquals(3, result.size(), "Should have three entries including duplicates");
    }

    /**
     * Test that getConsumerRuleFilter allows entries with same values.
     * Entries with identical group and module values should be allowed.
     */
    @Test
    public void testGetConsumerRuleFilter_allowsEntriesWithSameValues() {
        // Given: A filter with entries having same values but different objects
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library"),
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should contain all entries
        assertEquals(2, result.size(), "Should have two entries");
        assertEquals("com.example", result.get(0).getGroup(), "First entry group should match");
        assertEquals("com.example", result.get(1).getGroup(), "Second entry group should match");
    }

    // ==================== Multiple Get Tests ====================

    /**
     * Test that getConsumerRuleFilter is consistent across multiple calls.
     * Multiple calls should return equal results.
     */
    @Test
    public void testGetConsumerRuleFilter_consistentAcrossMultipleCalls() {
        // Given: A filter is set
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        task.setConsumerRuleFilter(filter);

        // When: Getting the filter multiple times
        List<ConsumerRuleFilterEntry> result1 = task.getConsumerRuleFilter();
        List<ConsumerRuleFilterEntry> result2 = task.getConsumerRuleFilter();
        List<ConsumerRuleFilterEntry> result3 = task.getConsumerRuleFilter();

        // Then: All results should be equal
        assertEquals(result1, result2, "First and second calls should return equal lists");
        assertEquals(result2, result3, "Second and third calls should return equal lists");
        assertEquals(result1, result3, "First and third calls should return equal lists");
    }

    /**
     * Test that getConsumerRuleFilter returns same list reference.
     * Multiple calls should return the same list object.
     */
    @Test
    public void testGetConsumerRuleFilter_returnsSameListReference() {
        // Given: A filter is set
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        task.setConsumerRuleFilter(filter);

        // When: Getting the filter multiple times
        List<ConsumerRuleFilterEntry> result1 = task.getConsumerRuleFilter();
        List<ConsumerRuleFilterEntry> result2 = task.getConsumerRuleFilter();

        // Then: Should return same reference
        assertSame(result1, result2, "Should return same list object reference");
    }

    // ==================== Update Tests ====================

    /**
     * Test that getConsumerRuleFilter returns updated list after setting new one.
     * After changing the filter, getter should return the new list.
     */
    @Test
    public void testGetConsumerRuleFilter_returnsUpdatedList() {
        // Given: An initial filter is set
        List<ConsumerRuleFilterEntry> initialFilter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.initial", "library")
        );
        task.setConsumerRuleFilter(initialFilter);

        // When: Setting a new filter
        List<ConsumerRuleFilterEntry> newFilter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.new", "library")
        );
        task.setConsumerRuleFilter(newFilter);
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should return the new filter
        assertEquals(newFilter, result, "Should return the updated filter");
        assertEquals("com.new", result.get(0).getGroup(), "Should have new group");
    }

    /**
     * Test that getConsumerRuleFilter doesn't return old list after update.
     * The old list should not be returned after setting a new one.
     */
    @Test
    public void testGetConsumerRuleFilter_doesNotReturnOldList() {
        // Given: An initial filter is set
        List<ConsumerRuleFilterEntry> oldFilter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.old", "library")
        );
        task.setConsumerRuleFilter(oldFilter);

        // When: Setting a new filter
        List<ConsumerRuleFilterEntry> newFilter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.new", "library")
        );
        task.setConsumerRuleFilter(newFilter);
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should not return the old filter
        assertNotEquals(oldFilter, result, "Should not return old filter");
    }

    // ==================== Integration Tests ====================

    /**
     * Test that getConsumerRuleFilter works after setting other properties.
     * The filter should remain accessible after setting other task properties.
     */
    @Test
    public void testGetConsumerRuleFilter_worksAfterSettingOtherProperties() {
        // Given: A filter and other properties are set
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        task.setConsumerRuleFilter(filter);
        task.setOutputFile(new java.io.File(tempDir.toFile(), "output.txt"));

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should still return the correct filter
        assertEquals(filter, result, "Filter should be accessible after setting other properties");
    }

    /**
     * Test that getConsumerRuleFilter is independent across different tasks.
     * Each task instance should have its own independent filter.
     */
    @Test
    public void testGetConsumerRuleFilter_independentAcrossTasks() {
        // Given: Two tasks with different filters
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

        task1.setConsumerRuleFilter(filter1);
        task2.setConsumerRuleFilter(filter2);

        // When: Getting filters from both tasks
        List<ConsumerRuleFilterEntry> result1 = task1.getConsumerRuleFilter();
        List<ConsumerRuleFilterEntry> result2 = task2.getConsumerRuleFilter();

        // Then: Each task should have its own filter
        assertNotEquals(result1, result2, "Tasks should have different filters");
        assertEquals("com.example1", result1.get(0).getGroup(), "Task1 should have its filter");
        assertEquals("com.example2", result2.get(0).getGroup(), "Task2 should have its filter");
    }

    // ==================== Different Entry Types Tests ====================

    /**
     * Test that getConsumerRuleFilter works with various group names.
     * Different group name patterns should be preserved.
     */
    @Test
    public void testGetConsumerRuleFilter_variousGroupNames() {
        // Given: A filter with various group name patterns
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "lib"),
            new ConsumerRuleFilterEntry("org.test", "lib"),
            new ConsumerRuleFilterEntry("androidx.core", "lib"),
            new ConsumerRuleFilterEntry("io.github.user", "lib")
        );
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should preserve all group names
        assertEquals("com.example", result.get(0).getGroup());
        assertEquals("org.test", result.get(1).getGroup());
        assertEquals("androidx.core", result.get(2).getGroup());
        assertEquals("io.github.user", result.get(3).getGroup());
    }

    /**
     * Test that getConsumerRuleFilter works with various module names.
     * Different module name patterns should be preserved.
     */
    @Test
    public void testGetConsumerRuleFilter_variousModuleNames() {
        // Given: A filter with various module name patterns
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library"),
            new ConsumerRuleFilterEntry("com.example", "library-ktx"),
            new ConsumerRuleFilterEntry("com.example", "library_utils"),
            new ConsumerRuleFilterEntry("com.example", "library.module")
        );
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should preserve all module names
        assertEquals("library", result.get(0).getModule());
        assertEquals("library-ktx", result.get(1).getModule());
        assertEquals("library_utils", result.get(2).getModule());
        assertEquals("library.module", result.get(3).getModule());
    }

    // ==================== List Type Tests ====================

    /**
     * Test that getConsumerRuleFilter works with Collections.singletonList.
     * Single-element immutable lists should work.
     */
    @Test
    public void testGetConsumerRuleFilter_singletonList() {
        // Given: A singleton list is set
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should work correctly
        assertEquals(1, result.size(), "Should have one entry");
    }

    /**
     * Test that getConsumerRuleFilter works with Arrays.asList.
     * Fixed-size lists created with Arrays.asList should work.
     */
    @Test
    public void testGetConsumerRuleFilter_arraysAsList() {
        // Given: An Arrays.asList is set
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library1"),
            new ConsumerRuleFilterEntry("com.example", "library2")
        );
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should work correctly
        assertEquals(2, result.size(), "Should have two entries");
    }

    /**
     * Test that getConsumerRuleFilter works with ArrayList.
     * Mutable ArrayLists should work.
     */
    @Test
    public void testGetConsumerRuleFilter_arrayList() {
        // Given: An ArrayList is set
        List<ConsumerRuleFilterEntry> filter = new ArrayList<>();
        filter.add(new ConsumerRuleFilterEntry("com.example", "library1"));
        filter.add(new ConsumerRuleFilterEntry("com.example", "library2"));
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should work correctly
        assertEquals(2, result.size(), "Should have two entries");
    }

    // ==================== Edge Cases ====================

    /**
     * Test that getConsumerRuleFilter works with entries containing empty strings.
     * Empty group or module strings should be handled.
     */
    @Test
    public void testGetConsumerRuleFilter_entriesWithEmptyStrings() {
        // Given: A filter with entries containing empty strings
        List<ConsumerRuleFilterEntry> filter = Arrays.asList(
            new ConsumerRuleFilterEntry("", "module"),
            new ConsumerRuleFilterEntry("group", "")
        );
        task.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = task.getConsumerRuleFilter();

        // Then: Should preserve entries with empty strings
        assertEquals(2, result.size(), "Should have two entries");
        assertEquals("", result.get(0).getGroup(), "Should preserve empty group");
        assertEquals("", result.get(1).getModule(), "Should preserve empty module");
    }

    /**
     * Test that getConsumerRuleFilter works immediately after task creation.
     * The getter should be available right after task instantiation.
     */
    @Test
    public void testGetConsumerRuleFilter_availableImmediatelyAfterCreation() {
        // Given: A newly created task
        CollectConsumerRulesTask newTask = project.getTasks()
                .create("newTask", CollectConsumerRulesTask.class);

        // When: Getting the filter immediately
        List<ConsumerRuleFilterEntry> result = newTask.getConsumerRuleFilter();

        // Then: Should return empty list
        assertNotNull(result, "Should be available immediately");
        assertTrue(result.isEmpty(), "Should return empty list by default");
    }

    /**
     * Test that getConsumerRuleFilter works with task created from different project.
     * The getter should work regardless of the project the task belongs to.
     */
    @Test
    public void testGetConsumerRuleFilter_worksWithDifferentProject() {
        // Given: A task from a different project
        Project anotherProject = ProjectBuilder.builder()
                .withProjectDir(tempDir.resolve("another").toFile())
                .build();
        CollectConsumerRulesTask anotherTask = anotherProject.getTasks()
                .create("anotherTask", CollectConsumerRulesTask.class);
        List<ConsumerRuleFilterEntry> filter = Collections.singletonList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        anotherTask.setConsumerRuleFilter(filter);

        // When: Getting the consumer rule filter
        List<ConsumerRuleFilterEntry> result = anotherTask.getConsumerRuleFilter();

        // Then: Should return the correct filter
        assertEquals(filter, result, "Filter should work with different project");
    }
}

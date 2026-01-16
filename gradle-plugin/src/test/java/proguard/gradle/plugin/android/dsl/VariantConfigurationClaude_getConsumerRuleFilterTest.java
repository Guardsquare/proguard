/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android.dsl;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for proguard.gradle.plugin.android.dsl.VariantConfiguration.getConsumerRuleFilter.()Ljava/util/List;
 *
 * Tests the getConsumerRuleFilter() method of VariantConfiguration which returns the list of consumer rule filter strings.
 * Consumer rule filters are patterns used to filter which ProGuard rules should be published to consumers of a library.
 * The list is mutable and initialized empty during construction.
 */
public class VariantConfigurationClaude_getConsumerRuleFilterTest {

    // ==================== Basic getConsumerRuleFilter Tests ====================

    /**
     * Test that getConsumerRuleFilter returns non-null list.
     * The consumer rule filter list should never be null.
     */
    @Test
    public void testGetConsumerRuleFilter_notNull() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the consumer rule filter list
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should not be null
        assertNotNull(result, "getConsumerRuleFilter() should never return null");
    }

    /**
     * Test that getConsumerRuleFilter returns empty list initially.
     * A newly created VariantConfiguration should have an empty consumer rule filter list.
     */
    @Test
    public void testGetConsumerRuleFilter_initiallyEmpty() {
        // Given: A newly created VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the consumer rule filter list
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should be empty
        assertTrue(result.isEmpty(), "getConsumerRuleFilter() should return empty list initially");
        assertEquals(0, result.size(), "getConsumerRuleFilter() should have size 0 initially");
    }

    /**
     * Test that getConsumerRuleFilter returns List type.
     * The return type should be a List.
     */
    @Test
    public void testGetConsumerRuleFilter_returnsListType() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the consumer rule filter list
        Object result = config.getConsumerRuleFilter();

        // Then: Should be a List instance
        assertInstanceOf(List.class, result, "getConsumerRuleFilter() should return List type");
    }

    /**
     * Test that getConsumerRuleFilter returns the same list instance.
     * Multiple calls should return the same list object.
     */
    @Test
    public void testGetConsumerRuleFilter_returnsSameInstance() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the consumer rule filter list multiple times
        List<String> result1 = config.getConsumerRuleFilter();
        List<String> result2 = config.getConsumerRuleFilter();
        List<String> result3 = config.getConsumerRuleFilter();

        // Then: Should return same instance
        assertSame(result1, result2, "getConsumerRuleFilter() should return same instance");
        assertSame(result2, result3, "getConsumerRuleFilter() should return same instance");
    }

    // ==================== After Adding Filters ====================

    /**
     * Test getConsumerRuleFilter after adding one filter.
     * The list should contain the added filter.
     */
    @Test
    public void testGetConsumerRuleFilter_afterAddingOneFilter() {
        // Given: A VariantConfiguration with one filter
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("*.pro");

        // When: Getting the consumer rule filter list
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should contain one filter
        assertFalse(result.isEmpty(), "getConsumerRuleFilter() should not be empty");
        assertEquals(1, result.size(), "getConsumerRuleFilter() should have size 1");
        assertEquals("*.pro", result.get(0), "Filter should be *.pro");
    }

    /**
     * Test getConsumerRuleFilter after adding multiple filters.
     * The list should contain all added filters in order.
     */
    @Test
    public void testGetConsumerRuleFilter_afterAddingMultipleFilters() {
        // Given: A VariantConfiguration with multiple filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("*.pro");
        config.consumerRuleFilter("*.txt");
        config.consumerRuleFilter("consumer-*.pro");

        // When: Getting the consumer rule filter list
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should contain all filters
        assertEquals(3, result.size(), "getConsumerRuleFilter() should have size 3");
        assertEquals("*.pro", result.get(0), "First filter should be *.pro");
        assertEquals("*.txt", result.get(1), "Second filter should be *.txt");
        assertEquals("consumer-*.pro", result.get(2), "Third filter should be consumer-*.pro");
    }

    /**
     * Test getConsumerRuleFilter after using vararg method.
     * The list should contain all filters added via varargs.
     */
    @Test
    public void testGetConsumerRuleFilter_afterUsingVarargMethod() {
        // Given: A VariantConfiguration with filters added via varargs
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("filter1.pro", "filter2.pro", "filter3.pro");

        // When: Getting the consumer rule filter list
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should contain all three filters
        assertEquals(3, result.size(), "getConsumerRuleFilter() should have size 3");
        assertEquals("filter1.pro", result.get(0), "First filter should be filter1.pro");
        assertEquals("filter2.pro", result.get(1), "Second filter should be filter2.pro");
        assertEquals("filter3.pro", result.get(2), "Third filter should be filter3.pro");
    }

    /**
     * Test getConsumerRuleFilter maintains insertion order.
     * The filters should appear in the order they were added.
     */
    @Test
    public void testGetConsumerRuleFilter_maintainsInsertionOrder() {
        // Given: A VariantConfiguration with filters added in specific order
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("first.pro");
        config.consumerRuleFilter("second.pro");
        config.consumerRuleFilter("third.pro");

        // When: Getting the consumer rule filter list
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should maintain insertion order
        assertEquals("first.pro", result.get(0), "First filter should be first.pro");
        assertEquals("second.pro", result.get(1), "Second filter should be second.pro");
        assertEquals("third.pro", result.get(2), "Third filter should be third.pro");
    }

    // ==================== List Mutability Tests ====================

    /**
     * Test that getConsumerRuleFilter returns a mutable list.
     * The returned list should allow modifications.
     */
    @Test
    public void testGetConsumerRuleFilter_returnsMutableList() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> list = config.getConsumerRuleFilter();

        // When: Adding a filter directly to the list
        list.add("direct-add.pro");

        // Then: Should be able to modify the list
        assertEquals(1, list.size(), "List should be mutable");
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Modification should be reflected in subsequent calls");
    }

    /**
     * Test that modifications to returned list affect the internal state.
     * Changes to the returned list should persist.
     */
    @Test
    public void testGetConsumerRuleFilter_modificationsAffectInternalState() {
        // Given: A VariantConfiguration with one filter
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("initial.pro");
        List<String> list1 = config.getConsumerRuleFilter();

        // When: Modifying the list and getting it again
        list1.add("added.pro");
        List<String> list2 = config.getConsumerRuleFilter();

        // Then: Modification should persist
        assertEquals(2, list2.size(), "Modification should persist");
        assertEquals("added.pro", list2.get(1), "Added filter should be present");
    }

    /**
     * Test that clearing the list works correctly.
     * The list should be clearable.
     */
    @Test
    public void testGetConsumerRuleFilter_canClearList() {
        // Given: A VariantConfiguration with filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("filter1.pro", "filter2.pro");
        List<String> list = config.getConsumerRuleFilter();

        // When: Clearing the list
        list.clear();

        // Then: List should be empty
        assertTrue(config.getConsumerRuleFilter().isEmpty(),
            "List should be empty after clear");
        assertEquals(0, config.getConsumerRuleFilter().size(),
            "List size should be 0 after clear");
    }

    /**
     * Test that removing items from the list works correctly.
     * Individual items should be removable.
     */
    @Test
    public void testGetConsumerRuleFilter_canRemoveItems() {
        // Given: A VariantConfiguration with multiple filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("filter1.pro", "filter2.pro", "filter3.pro");
        List<String> list = config.getConsumerRuleFilter();

        // When: Removing an item
        String removed = list.remove(1);

        // Then: Item should be removed
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "List should have 2 items after removal");
        assertEquals("filter2.pro", removed, "Removed item should be filter2.pro");
        assertEquals("filter1.pro", list.get(0), "First item should still be filter1.pro");
        assertEquals("filter3.pro", list.get(1), "Second item should now be filter3.pro");
    }

    // ==================== List Properties Tests ====================

    /**
     * Test that getConsumerRuleFilter returns list with correct size.
     * The size should match the number of added filters.
     */
    @Test
    public void testGetConsumerRuleFilter_correctSize() {
        // Given: A VariantConfiguration with known number of filters
        VariantConfiguration config = new VariantConfiguration("debug");

        // When/Then: Size should match number of additions
        assertEquals(0, config.getConsumerRuleFilter().size(), "Initial size should be 0");

        config.consumerRuleFilter("filter1.pro");
        assertEquals(1, config.getConsumerRuleFilter().size(), "Size should be 1 after adding one");

        config.consumerRuleFilter("filter2.pro");
        assertEquals(2, config.getConsumerRuleFilter().size(), "Size should be 2 after adding two");

        config.consumerRuleFilter("filter3.pro");
        assertEquals(3, config.getConsumerRuleFilter().size(), "Size should be 3 after adding three");
    }

    /**
     * Test that getConsumerRuleFilter list supports contains check.
     * The list should support contains() operation.
     */
    @Test
    public void testGetConsumerRuleFilter_supportsContains() {
        // Given: A VariantConfiguration with filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("*.pro", "*.txt");
        List<String> list = config.getConsumerRuleFilter();

        // When: Checking if list contains filters
        boolean containsPro = list.contains("*.pro");
        boolean containsTxt = list.contains("*.txt");
        boolean containsJava = list.contains("*.java");

        // Then: Should support contains operation correctly
        assertTrue(containsPro, "List should contain *.pro");
        assertTrue(containsTxt, "List should contain *.txt");
        assertFalse(containsJava, "List should not contain *.java");
    }

    /**
     * Test that getConsumerRuleFilter list supports indexOf.
     * The list should support indexOf() operation.
     */
    @Test
    public void testGetConsumerRuleFilter_supportsIndexOf() {
        // Given: A VariantConfiguration with multiple filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("first.pro", "second.pro", "third.pro");
        List<String> list = config.getConsumerRuleFilter();

        // When: Getting index of filters
        int indexFirst = list.indexOf("first.pro");
        int indexSecond = list.indexOf("second.pro");
        int indexThird = list.indexOf("third.pro");
        int indexNonExistent = list.indexOf("nonexistent.pro");

        // Then: Should return correct indices
        assertEquals(0, indexFirst, "indexOf first.pro should return 0");
        assertEquals(1, indexSecond, "indexOf second.pro should return 1");
        assertEquals(2, indexThird, "indexOf third.pro should return 2");
        assertEquals(-1, indexNonExistent, "indexOf nonexistent should return -1");
    }

    /**
     * Test that getConsumerRuleFilter list is iterable.
     * The list should support iteration.
     */
    @Test
    public void testGetConsumerRuleFilter_isIterable() {
        // Given: A VariantConfiguration with filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("filter1.pro", "filter2.pro", "filter3.pro");

        // When: Iterating over the list
        int count = 0;
        for (String filter : config.getConsumerRuleFilter()) {
            count++;
            assertNotNull(filter, "Each filter should be non-null");
        }

        // Then: Should iterate over all items
        assertEquals(3, count, "Should iterate over all 3 filters");
    }

    // ==================== Multiple Instance Tests ====================

    /**
     * Test that different instances have independent filter lists.
     * Modifying one instance's list shouldn't affect another.
     */
    @Test
    public void testGetConsumerRuleFilter_independentBetweenInstances() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");

        // When: Adding filters to first instance
        config1.consumerRuleFilter("filter1.pro", "filter2.pro");

        // Then: Second instance should remain unaffected
        assertEquals(2, config1.getConsumerRuleFilter().size(),
            "First instance should have 2 filters");
        assertEquals(0, config2.getConsumerRuleFilter().size(),
            "Second instance should have 0 filters");
    }

    /**
     * Test that instances with same filters are independent.
     * Even with same filter names, lists should be independent.
     */
    @Test
    public void testGetConsumerRuleFilter_independentWithSameFilters() {
        // Given: Two VariantConfigurations with same filter names
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("debug");
        config1.consumerRuleFilter("*.pro");
        config2.consumerRuleFilter("*.pro");

        // When: Modifying first instance's list
        config1.getConsumerRuleFilter().clear();

        // Then: Second instance should be unaffected
        assertEquals(0, config1.getConsumerRuleFilter().size(),
            "First instance should be empty after clear");
        assertEquals(1, config2.getConsumerRuleFilter().size(),
            "Second instance should still have 1 filter");
    }

    // ==================== Empty List Tests ====================

    /**
     * Test that empty list behaves correctly.
     * An empty list should have all expected empty list properties.
     */
    @Test
    public void testGetConsumerRuleFilter_emptyListBehavior() {
        // Given: A VariantConfiguration with no filters
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> list = config.getConsumerRuleFilter();

        // When/Then: Should behave as empty list
        assertTrue(list.isEmpty(), "isEmpty() should return true");
        assertEquals(0, list.size(), "size() should return 0");
        assertFalse(list.iterator().hasNext(), "iterator should have no elements");
    }

    // ==================== Filter Pattern Tests ====================

    /**
     * Test getConsumerRuleFilter with wildcard patterns.
     * Consumer rule filters typically use wildcard patterns.
     */
    @Test
    public void testGetConsumerRuleFilter_withWildcardPatterns() {
        // Given: A VariantConfiguration with wildcard patterns
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("*.pro", "*.txt", "consumer-*.pro");

        // When: Getting the consumer rule filter list
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should contain all wildcard patterns
        assertEquals(3, result.size(), "Should have 3 wildcard patterns");
        assertTrue(result.get(0).contains("*"), "First pattern should contain wildcard");
        assertTrue(result.get(1).contains("*"), "Second pattern should contain wildcard");
        assertTrue(result.get(2).contains("*"), "Third pattern should contain wildcard");
    }

    /**
     * Test getConsumerRuleFilter with specific file patterns.
     * Filters can also be specific file names.
     */
    @Test
    public void testGetConsumerRuleFilter_withSpecificFilePatterns() {
        // Given: A VariantConfiguration with specific file patterns
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("consumer-rules.pro", "library-rules.txt");

        // When: Getting the consumer rule filter list
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should contain specific file patterns
        assertEquals(2, result.size(), "Should have 2 file patterns");
        assertEquals("consumer-rules.pro", result.get(0), "First should be consumer-rules.pro");
        assertEquals("library-rules.txt", result.get(1), "Second should be library-rules.txt");
    }

    /**
     * Test getConsumerRuleFilter with path patterns.
     * Filters can include path separators.
     */
    @Test
    public void testGetConsumerRuleFilter_withPathPatterns() {
        // Given: A VariantConfiguration with path patterns
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("consumer/*.pro", "rules/consumer-*.txt");

        // When: Getting the consumer rule filter list
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should contain path patterns
        assertEquals(2, result.size(), "Should have 2 path patterns");
        assertTrue(result.get(0).contains("/"), "First should contain path separator");
        assertTrue(result.get(1).contains("/"), "Second should contain path separator");
    }

    // ==================== Real-world Usage Scenarios ====================

    /**
     * Test getConsumerRuleFilter for library module scenario.
     * Library modules use consumer rule filters to specify which rules to publish.
     */
    @Test
    public void testGetConsumerRuleFilter_libraryModuleScenario() {
        // Given: A library module configuration
        VariantConfiguration config = new VariantConfiguration("release");
        config.consumerRuleFilter("consumer-rules.pro", "consumer-*.txt");

        // When: Getting filters
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should have consumer rule filters
        assertEquals(2, result.size(), "Library should have consumer rule filters");
        assertEquals("consumer-rules.pro", result.get(0),
            "Should have consumer-rules.pro filter");
        assertEquals("consumer-*.txt", result.get(1),
            "Should have consumer wildcard filter");
    }

    /**
     * Test getConsumerRuleFilter for application module scenario.
     * Application modules typically don't publish consumer rules.
     */
    @Test
    public void testGetConsumerRuleFilter_applicationModuleScenario() {
        // Given: An application module configuration
        VariantConfiguration config = new VariantConfiguration("release");
        // No consumer rule filters added for application

        // When: Getting filters
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should have no filters
        assertTrue(result.isEmpty(), "Application module should have no consumer rule filters");
    }

    /**
     * Test getConsumerRuleFilter for multi-module library scenario.
     * Multi-module projects might have complex filter patterns.
     */
    @Test
    public void testGetConsumerRuleFilter_multiModuleLibraryScenario() {
        // Given: A multi-module library configuration
        VariantConfiguration config = new VariantConfiguration("release");
        config.consumerRuleFilter(
            "consumer-rules.pro",
            "api-consumer-rules.pro",
            "internal-consumer-*.pro"
        );

        // When: Getting filters
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should have all module-specific filters
        assertEquals(3, result.size(), "Multi-module should have multiple filters");
        assertTrue(result.contains("consumer-rules.pro"), "Should have base consumer rules");
        assertTrue(result.contains("api-consumer-rules.pro"), "Should have API consumer rules");
        assertTrue(result.contains("internal-consumer-*.pro"), "Should have internal rules pattern");
    }

    // ==================== Stream Operations Tests ====================

    /**
     * Test that getConsumerRuleFilter list works with Stream API.
     * The list should support Java Stream operations.
     */
    @Test
    public void testGetConsumerRuleFilter_worksWithStreams() {
        // Given: A VariantConfiguration with multiple filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("filter1.pro", "filter2.txt", "filter3.pro");

        // When: Using stream operations
        long proCount = config.getConsumerRuleFilter().stream()
            .filter(f -> f.endsWith(".pro"))
            .count();

        // Then: Should work with streams
        assertEquals(2, proCount, "Stream operations should work correctly");
    }

    /**
     * Test filtering consumer rules using streams.
     * Should be able to filter by pattern.
     */
    @Test
    public void testGetConsumerRuleFilter_streamFiltering() {
        // Given: A VariantConfiguration with mixed filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("*.pro", "consumer-rules.txt", "*.java", "filter.pro");

        // When: Filtering for .pro files
        long proCount = config.getConsumerRuleFilter().stream()
            .filter(f -> f.endsWith(".pro") || f.contains("*.pro"))
            .count();

        // Then: Should correctly filter
        assertEquals(2, proCount, "Should have 2 .pro patterns");
    }

    // ==================== Edge Cases ====================

    /**
     * Test getConsumerRuleFilter with empty string filter.
     * Edge case: empty string filters should be handled.
     */
    @Test
    public void testGetConsumerRuleFilter_withEmptyString() {
        // Given: A VariantConfiguration with empty string filter
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("");

        // When: Getting filters
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should contain empty string filter
        assertEquals(1, result.size(), "Should have 1 filter");
        assertEquals("", result.get(0), "Filter should be empty string");
    }

    /**
     * Test getConsumerRuleFilter with special characters.
     * Filters might contain special regex characters.
     */
    @Test
    public void testGetConsumerRuleFilter_withSpecialCharacters() {
        // Given: A VariantConfiguration with special characters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("consumer-[0-9]*.pro", "rules_v2.0.txt");

        // When: Getting filters
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should preserve special characters
        assertEquals(2, result.size(), "Should have 2 filters");
        assertEquals("consumer-[0-9]*.pro", result.get(0),
            "Should preserve regex characters");
        assertEquals("rules_v2.0.txt", result.get(1),
            "Should preserve version notation");
    }

    /**
     * Test getConsumerRuleFilter with duplicate filters.
     * Duplicates should be allowed.
     */
    @Test
    public void testGetConsumerRuleFilter_withDuplicateFilters() {
        // Given: A VariantConfiguration with duplicate filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("*.pro", "*.pro", "*.pro");

        // When: Getting filters
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should accept duplicates
        assertEquals(3, result.size(), "Should have 3 items (including duplicates)");
        assertEquals("*.pro", result.get(0), "First should be *.pro");
        assertEquals("*.pro", result.get(1), "Second should be *.pro");
        assertEquals("*.pro", result.get(2), "Third should be *.pro");
    }

    /**
     * Test getConsumerRuleFilter with many filters.
     * Should handle a large number of filters.
     */
    @Test
    public void testGetConsumerRuleFilter_withManyFilters() {
        // Given: A VariantConfiguration with many filters
        VariantConfiguration config = new VariantConfiguration("debug");
        int count = 100;
        for (int i = 0; i < count; i++) {
            config.consumerRuleFilter("filter" + i + ".pro");
        }

        // When: Getting filters
        List<String> result = config.getConsumerRuleFilter();

        // Then: Should contain all filters
        assertEquals(count, result.size(), "Should have 100 filters");
        assertEquals("filter0.pro", result.get(0), "First should be filter0.pro");
        assertEquals("filter99.pro", result.get(99), "Last should be filter99.pro");
    }

    // ==================== Type Safety Tests ====================

    /**
     * Test that getConsumerRuleFilter returns correctly typed list.
     * The list should be properly typed as List<String>.
     */
    @Test
    public void testGetConsumerRuleFilter_correctlyTypedList() {
        // Given: A VariantConfiguration with filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("filter1.pro", "filter2.txt");

        // When: Getting filters
        List<String> result = config.getConsumerRuleFilter();

        // Then: All items should be String instances
        for (String filter : result) {
            assertInstanceOf(String.class, filter, "Each item should be a String");
        }
    }

    // ==================== Consistency Tests ====================

    /**
     * Test that getConsumerRuleFilter is consistent after multiple operations.
     * The list should accurately reflect all operations performed.
     */
    @Test
    public void testGetConsumerRuleFilter_consistentAfterMultipleOperations() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Performing multiple operations
        config.consumerRuleFilter("filter1.pro");
        assertEquals(1, config.getConsumerRuleFilter().size());

        config.consumerRuleFilter("filter2.pro");
        assertEquals(2, config.getConsumerRuleFilter().size());

        config.getConsumerRuleFilter().remove(0);
        assertEquals(1, config.getConsumerRuleFilter().size());

        config.consumerRuleFilter("filter3.pro");
        assertEquals(2, config.getConsumerRuleFilter().size());

        // Then: Final state should be consistent
        assertEquals("filter2.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("filter3.pro", config.getConsumerRuleFilter().get(1));
    }

    /**
     * Test that getConsumerRuleFilter reflects the current state accurately.
     * The list should always represent the current filter state.
     */
    @Test
    public void testGetConsumerRuleFilter_reflectsCurrentState() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When/Then: State should be consistent at each step
        assertTrue(config.getConsumerRuleFilter().isEmpty(), "Should start empty");

        config.consumerRuleFilter("*.pro");
        assertFalse(config.getConsumerRuleFilter().isEmpty(), "Should not be empty after adding");

        config.getConsumerRuleFilter().clear();
        assertTrue(config.getConsumerRuleFilter().isEmpty(), "Should be empty after clearing");
    }

    /**
     * Test getConsumerRuleFilter doesn't affect getName().
     * Consumer rule filters should not affect other properties.
     */
    @Test
    public void testGetConsumerRuleFilter_doesNotAffectName() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        String nameBefore = config.getName();

        // When: Adding consumer rule filters
        config.consumerRuleFilter("*.pro", "*.txt");

        // Then: Name should remain unchanged
        assertEquals(nameBefore, config.getName(),
            "Adding filters should not affect getName()");
        assertEquals("debug", config.getName(), "Name should still be 'debug'");
    }

    /**
     * Test getConsumerRuleFilter doesn't affect getConfigurations().
     * Consumer rule filters should not affect configurations property.
     */
    @Test
    public void testGetConsumerRuleFilter_doesNotAffectConfigurations() {
        // Given: A VariantConfiguration with configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("rules.pro");
        int configSizeBefore = config.getConfigurations().size();

        // When: Adding consumer rule filters
        config.consumerRuleFilter("*.pro", "*.txt");

        // Then: Configurations should remain unchanged
        assertEquals(configSizeBefore, config.getConfigurations().size(),
            "Adding filters should not affect configurations");
        assertEquals(1, config.getConfigurations().size(),
            "Configurations should still have 1 item");
    }

    // ==================== String Comparison Tests ====================

    /**
     * Test that filter strings can be compared.
     * Filter strings should support equality checks.
     */
    @Test
    public void testGetConsumerRuleFilter_stringComparison() {
        // Given: A VariantConfiguration with filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("*.pro");

        // When: Getting and comparing filters
        String filter = config.getConsumerRuleFilter().get(0);

        // Then: Should support string comparison
        assertTrue(filter.equals("*.pro"), "Filter should equal *.pro");
        assertFalse(filter.equals("*.txt"), "Filter should not equal *.txt");
    }

    /**
     * Test that filter strings can be used with startsWith.
     * Filter strings should work with String methods.
     */
    @Test
    public void testGetConsumerRuleFilter_startsWithCheck() {
        // Given: A VariantConfiguration with specific filter
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("consumer-rules.pro");

        // When: Checking prefix
        String filter = config.getConsumerRuleFilter().get(0);

        // Then: Should work with startsWith
        assertTrue(filter.startsWith("consumer"), "Should start with 'consumer'");
        assertFalse(filter.startsWith("library"), "Should not start with 'library'");
    }

    /**
     * Test that filter strings can be used with endsWith.
     * Filter strings should work with String methods.
     */
    @Test
    public void testGetConsumerRuleFilter_endsWithCheck() {
        // Given: A VariantConfiguration with specific filter
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("consumer-rules.pro");

        // When: Checking suffix
        String filter = config.getConsumerRuleFilter().get(0);

        // Then: Should work with endsWith
        assertTrue(filter.endsWith(".pro"), "Should end with .pro");
        assertFalse(filter.endsWith(".txt"), "Should not end with .txt");
    }

    /**
     * Test that filter strings can be used with contains.
     * Filter strings should work with String methods.
     */
    @Test
    public void testGetConsumerRuleFilter_containsCheck() {
        // Given: A VariantConfiguration with specific filter
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("consumer-rules.pro");

        // When: Checking contents
        String filter = config.getConsumerRuleFilter().get(0);

        // Then: Should work with contains
        assertTrue(filter.contains("rules"), "Should contain 'rules'");
        assertFalse(filter.contains("config"), "Should not contain 'config'");
    }
}

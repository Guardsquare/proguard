/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android.dsl;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for proguard.gradle.plugin.android.dsl.VariantConfiguration.setConsumerRuleFilter.(Ljava/util/List;)V
 *
 * Tests the setConsumerRuleFilter() method of VariantConfiguration which sets the list of consumer rule filter strings.
 * This is a setter method for the 'var consumerRuleFilter' property in Kotlin.
 * The method completely replaces the current consumer rule filter list with the provided list.
 * Consumer rule filters are patterns used to filter which ProGuard rules should be published to consumers of a library.
 */
public class VariantConfigurationClaude_setConsumerRuleFilterTest {

    // ==================== Basic setConsumerRuleFilter Tests ====================

    /**
     * Test that setConsumerRuleFilter sets a new list.
     * The most basic use case - replacing the consumer rule filter list.
     */
    @Test
    public void testSetConsumerRuleFilter_setsNewList() {
        // Given: A VariantConfiguration and a new list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = new ArrayList<>();
        newList.add("*.pro");

        // When: Setting the consumer rule filter
        config.setConsumerRuleFilter(newList);

        // Then: Should have the new list
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "setConsumerRuleFilter() should set the new list");
        assertEquals("*.pro", config.getConsumerRuleFilter().get(0),
            "setConsumerRuleFilter() should contain the correct filter");
    }

    /**
     * Test that setConsumerRuleFilter replaces existing list.
     * Setting a new list should completely replace the old one.
     */
    @Test
    public void testSetConsumerRuleFilter_replacesExistingList() {
        // Given: A VariantConfiguration with existing filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("old-filter.pro");

        List<String> newList = new ArrayList<>();
        newList.add("new-filter.pro");

        // When: Setting new consumer rule filter
        config.setConsumerRuleFilter(newList);

        // Then: Old filters should be replaced
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "setConsumerRuleFilter() should replace old list");
        assertEquals("new-filter.pro", config.getConsumerRuleFilter().get(0),
            "setConsumerRuleFilter() should contain only new filter");
    }

    /**
     * Test that setConsumerRuleFilter with empty list clears filters.
     * Setting an empty list should remove all filters.
     */
    @Test
    public void testSetConsumerRuleFilter_withEmptyList() {
        // Given: A VariantConfiguration with existing filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("filter1.pro", "filter2.pro");

        // When: Setting an empty list
        config.setConsumerRuleFilter(new ArrayList<>());

        // Then: Should have no filters
        assertTrue(config.getConsumerRuleFilter().isEmpty(),
            "setConsumerRuleFilter() with empty list should clear filters");
        assertEquals(0, config.getConsumerRuleFilter().size(),
            "setConsumerRuleFilter() with empty list should result in size 0");
    }

    /**
     * Test that setConsumerRuleFilter stores the reference to the list.
     * The actual list reference should be stored, not a copy.
     */
    @Test
    public void testSetConsumerRuleFilter_storesReference() {
        // Given: A VariantConfiguration and a list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = new ArrayList<>();
        newList.add("*.pro");

        // When: Setting the consumer rule filter
        config.setConsumerRuleFilter(newList);

        // Then: Should store the actual reference
        assertSame(newList, config.getConsumerRuleFilter(),
            "setConsumerRuleFilter() should store the actual list reference");
    }

    /**
     * Test that modifications to original list affect the configuration.
     * Since the reference is stored, changes to the original list should be reflected.
     */
    @Test
    public void testSetConsumerRuleFilter_modificationsToOriginalListAffectConfig() {
        // Given: A VariantConfiguration with a set list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = new ArrayList<>();
        newList.add("*.pro");
        config.setConsumerRuleFilter(newList);

        // When: Modifying the original list
        newList.add("*.txt");

        // Then: Configuration should reflect the change
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "Modifications to original list should affect configuration");
        assertEquals("*.txt", config.getConsumerRuleFilter().get(1),
            "New item should be present in configuration");
    }

    // ==================== Setting Different List Types ====================

    /**
     * Test setConsumerRuleFilter with ArrayList.
     * ArrayList is the most common List implementation.
     */
    @Test
    public void testSetConsumerRuleFilter_withArrayList() {
        // Given: A VariantConfiguration and an ArrayList
        VariantConfiguration config = new VariantConfiguration("debug");
        ArrayList<String> newList = new ArrayList<>();
        newList.add("*.pro");

        // When: Setting with ArrayList
        config.setConsumerRuleFilter(newList);

        // Then: Should work correctly
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "setConsumerRuleFilter() should work with ArrayList");
        assertInstanceOf(ArrayList.class, config.getConsumerRuleFilter(),
            "Should maintain ArrayList type");
    }

    /**
     * Test setConsumerRuleFilter with Arrays.asList result.
     * Arrays.asList creates a fixed-size list.
     */
    @Test
    public void testSetConsumerRuleFilter_withArraysAsList() {
        // Given: A VariantConfiguration and Arrays.asList list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = Arrays.asList("*.pro", "*.txt");

        // When: Setting with Arrays.asList
        config.setConsumerRuleFilter(newList);

        // Then: Should work correctly
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "setConsumerRuleFilter() should work with Arrays.asList");
    }

    /**
     * Test setConsumerRuleFilter with Collections.emptyList().
     * Empty list constant should work.
     */
    @Test
    public void testSetConsumerRuleFilter_withEmptyListConstant() {
        // Given: A VariantConfiguration with existing filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("old-filter.pro");

        // When: Setting with Collections.emptyList()
        config.setConsumerRuleFilter(Collections.emptyList());

        // Then: Should clear filters
        assertTrue(config.getConsumerRuleFilter().isEmpty(),
            "setConsumerRuleFilter() should work with Collections.emptyList()");
    }

    /**
     * Test setConsumerRuleFilter with Collections.singletonList().
     * Single element list should work.
     */
    @Test
    public void testSetConsumerRuleFilter_withSingletonList() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = Collections.singletonList("*.pro");

        // When: Setting with Collections.singletonList()
        config.setConsumerRuleFilter(newList);

        // Then: Should work correctly
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "setConsumerRuleFilter() should work with Collections.singletonList()");
    }

    // ==================== Setting Lists with Different Filter Patterns ====================

    /**
     * Test setConsumerRuleFilter with wildcard patterns.
     * Wildcard patterns are common for consumer rule filters.
     */
    @Test
    public void testSetConsumerRuleFilter_withWildcardPatterns() {
        // Given: A list with wildcard patterns
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = Arrays.asList("*.pro", "*.txt", "consumer-*.pro");

        // When: Setting the consumer rule filter
        config.setConsumerRuleFilter(newList);

        // Then: Should contain all wildcard patterns
        assertEquals(3, config.getConsumerRuleFilter().size(), "Should have 3 patterns");
        assertEquals("*.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("*.txt", config.getConsumerRuleFilter().get(1));
        assertEquals("consumer-*.pro", config.getConsumerRuleFilter().get(2));
    }

    /**
     * Test setConsumerRuleFilter with specific file patterns.
     * Specific file names should work as filters.
     */
    @Test
    public void testSetConsumerRuleFilter_withSpecificFilePatterns() {
        // Given: A list with specific file patterns
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = Arrays.asList(
            "consumer-rules.pro",
            "library-rules.txt",
            "api-consumer-rules.pro"
        );

        // When: Setting the consumer rule filter
        config.setConsumerRuleFilter(newList);

        // Then: Should contain all specific patterns
        assertEquals(3, config.getConsumerRuleFilter().size(), "Should have 3 patterns");
        assertEquals("consumer-rules.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("library-rules.txt", config.getConsumerRuleFilter().get(1));
        assertEquals("api-consumer-rules.pro", config.getConsumerRuleFilter().get(2));
    }

    /**
     * Test setConsumerRuleFilter with path patterns.
     * Patterns with path separators should work.
     */
    @Test
    public void testSetConsumerRuleFilter_withPathPatterns() {
        // Given: A list with path patterns
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = Arrays.asList(
            "consumer/*.pro",
            "rules/consumer-*.txt",
            "lib/api/*.pro"
        );

        // When: Setting the consumer rule filter
        config.setConsumerRuleFilter(newList);

        // Then: Should contain all path patterns
        assertEquals(3, config.getConsumerRuleFilter().size(), "Should have 3 patterns");
        assertTrue(config.getConsumerRuleFilter().get(0).contains("/"),
            "First should contain path separator");
        assertTrue(config.getConsumerRuleFilter().get(1).contains("/"),
            "Second should contain path separator");
        assertTrue(config.getConsumerRuleFilter().get(2).contains("/"),
            "Third should contain path separator");
    }

    // ==================== Order Preservation Tests ====================

    /**
     * Test that setConsumerRuleFilter preserves order.
     * The order of filters in the provided list should be maintained.
     */
    @Test
    public void testSetConsumerRuleFilter_preservesOrder() {
        // Given: A list with filters in specific order
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = Arrays.asList("first.pro", "second.pro", "third.pro");

        // When: Setting the consumer rule filter
        config.setConsumerRuleFilter(newList);

        // Then: Order should be preserved
        assertEquals("first.pro", config.getConsumerRuleFilter().get(0),
            "First should be first.pro");
        assertEquals("second.pro", config.getConsumerRuleFilter().get(1),
            "Second should be second.pro");
        assertEquals("third.pro", config.getConsumerRuleFilter().get(2),
            "Third should be third.pro");
    }

    // ==================== Multiple Instance Tests ====================

    /**
     * Test that setConsumerRuleFilter affects only the target instance.
     * Setting filters on one instance shouldn't affect others.
     */
    @Test
    public void testSetConsumerRuleFilter_affectsOnlyTargetInstance() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        config1.consumerRuleFilter("debug-filter.pro");
        config2.consumerRuleFilter("release-filter.pro");

        List<String> newList = Arrays.asList("new-filter.pro");

        // When: Setting filters on first instance
        config1.setConsumerRuleFilter(newList);

        // Then: Only first instance should be affected
        assertEquals(1, config1.getConsumerRuleFilter().size(),
            "First instance should have 1 filter");
        assertEquals("new-filter.pro", config1.getConsumerRuleFilter().get(0),
            "First instance should have new filter");
        assertEquals(1, config2.getConsumerRuleFilter().size(),
            "Second instance should still have 1 filter");
        assertEquals("release-filter.pro", config2.getConsumerRuleFilter().get(0),
            "Second instance should retain original filter");
    }

    /**
     * Test that same list can be set on multiple instances.
     * The same list reference can be shared between instances.
     */
    @Test
    public void testSetConsumerRuleFilter_canShareListBetweenInstances() {
        // Given: Two VariantConfigurations and one list
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        List<String> sharedList = Arrays.asList("shared-filter.pro");

        // When: Setting same list on both instances
        config1.setConsumerRuleFilter(sharedList);
        config2.setConsumerRuleFilter(sharedList);

        // Then: Both should reference the same list
        assertSame(config1.getConsumerRuleFilter(), config2.getConsumerRuleFilter(),
            "Both instances should share the same list reference");
        assertEquals(1, config1.getConsumerRuleFilter().size(),
            "First instance should have the filter");
        assertEquals(1, config2.getConsumerRuleFilter().size(),
            "Second instance should have the filter");
    }

    /**
     * Test that modifying shared list affects all instances.
     * When multiple instances share a list, modifications affect all.
     */
    @Test
    public void testSetConsumerRuleFilter_sharedListModificationsAffectAll() {
        // Given: Two VariantConfigurations sharing a list
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        List<String> sharedList = new ArrayList<>();
        sharedList.add("shared-filter.pro");
        config1.setConsumerRuleFilter(sharedList);
        config2.setConsumerRuleFilter(sharedList);

        // When: Modifying the shared list through one instance
        config1.getConsumerRuleFilter().add("additional.pro");

        // Then: Both instances should see the change
        assertEquals(2, config1.getConsumerRuleFilter().size(),
            "First instance should have 2 filters");
        assertEquals(2, config2.getConsumerRuleFilter().size(),
            "Second instance should have 2 filters");
    }

    // ==================== Interaction with Other Methods ====================

    /**
     * Test setConsumerRuleFilter after using consumerRuleFilter() method.
     * setConsumerRuleFilter should replace filters added via consumerRuleFilter().
     */
    @Test
    public void testSetConsumerRuleFilter_afterUsingConsumerRuleFilterMethod() {
        // Given: A VariantConfiguration with filters added via consumerRuleFilter()
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("old1.pro", "old2.pro");

        List<String> newList = Arrays.asList("new.pro");

        // When: Setting new filters
        config.setConsumerRuleFilter(newList);

        // Then: Old filters should be replaced
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Should only have new filter");
        assertEquals("new.pro", config.getConsumerRuleFilter().get(0),
            "Should have the new filter");
    }

    /**
     * Test using consumerRuleFilter() method after setConsumerRuleFilter.
     * consumerRuleFilter() should add to the list set by setConsumerRuleFilter.
     */
    @Test
    public void testSetConsumerRuleFilter_thenUsingConsumerRuleFilterMethod() {
        // Given: A VariantConfiguration with filters set via setter
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = new ArrayList<>();
        newList.add("initial.pro");
        config.setConsumerRuleFilter(newList);

        // When: Adding filter via consumerRuleFilter() method
        config.consumerRuleFilter("additional.pro");

        // Then: Should add to the existing list
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "Should have 2 filters");
        assertEquals("initial.pro", config.getConsumerRuleFilter().get(0),
            "First should be initial.pro");
        assertEquals("additional.pro", config.getConsumerRuleFilter().get(1),
            "Second should be additional.pro");
    }

    // ==================== Setting Same List Multiple Times ====================

    /**
     * Test setConsumerRuleFilter called multiple times with different lists.
     * Each call should replace the previous list.
     */
    @Test
    public void testSetConsumerRuleFilter_multipleTimesWithDifferentLists() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Setting filters multiple times
        List<String> list1 = Arrays.asList("first.pro");
        config.setConsumerRuleFilter(list1);

        List<String> list2 = Arrays.asList("second.pro");
        config.setConsumerRuleFilter(list2);

        List<String> list3 = Arrays.asList("third.pro");
        config.setConsumerRuleFilter(list3);

        // Then: Should have only the last list
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Should have 1 filter");
        assertEquals("third.pro", config.getConsumerRuleFilter().get(0),
            "Should have the last set filter");
        assertSame(list3, config.getConsumerRuleFilter(),
            "Should reference the last set list");
    }

    /**
     * Test setConsumerRuleFilter called with same list reference multiple times.
     * Setting the same reference again should work (idempotent).
     */
    @Test
    public void testSetConsumerRuleFilter_multipleTimesWithSameReference() {
        // Given: A VariantConfiguration and a list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> list = Arrays.asList("*.pro");

        // When: Setting filters multiple times with same reference
        config.setConsumerRuleFilter(list);
        config.setConsumerRuleFilter(list);
        config.setConsumerRuleFilter(list);

        // Then: Should still reference the same list
        assertSame(list, config.getConsumerRuleFilter(),
            "Should reference the same list");
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Should have 1 filter");
    }

    // ==================== Edge Cases ====================

    /**
     * Test setConsumerRuleFilter with list containing duplicate filters.
     * Duplicates should be allowed.
     */
    @Test
    public void testSetConsumerRuleFilter_withDuplicateFilters() {
        // Given: A list with duplicate filters
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = Arrays.asList("*.pro", "*.pro", "*.pro");

        // When: Setting filters with duplicates
        config.setConsumerRuleFilter(newList);

        // Then: Should accept duplicates
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Should have 3 items (including duplicates)");
        assertEquals("*.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("*.pro", config.getConsumerRuleFilter().get(1));
        assertEquals("*.pro", config.getConsumerRuleFilter().get(2));
    }

    /**
     * Test setConsumerRuleFilter with list containing empty strings.
     * Empty string filters should be allowed.
     */
    @Test
    public void testSetConsumerRuleFilter_withEmptyStrings() {
        // Given: A list with empty strings
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = Arrays.asList("", "*.pro", "");

        // When: Setting filters with empty strings
        config.setConsumerRuleFilter(newList);

        // Then: Should accept empty strings
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Should have 3 items");
        assertEquals("", config.getConsumerRuleFilter().get(0),
            "First should be empty string");
        assertEquals("*.pro", config.getConsumerRuleFilter().get(1),
            "Second should be *.pro");
        assertEquals("", config.getConsumerRuleFilter().get(2),
            "Third should be empty string");
    }

    /**
     * Test setConsumerRuleFilter with large list.
     * Should handle large numbers of filters.
     */
    @Test
    public void testSetConsumerRuleFilter_withLargeList() {
        // Given: A large list of filters
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = new ArrayList<>();
        int count = 100;
        for (int i = 0; i < count; i++) {
            newList.add("filter" + i + ".pro");
        }

        // When: Setting large list
        config.setConsumerRuleFilter(newList);

        // Then: Should contain all filters
        assertEquals(count, config.getConsumerRuleFilter().size(),
            "Should have 100 filters");
        assertEquals("filter0.pro", config.getConsumerRuleFilter().get(0),
            "First should be filter0.pro");
        assertEquals("filter99.pro", config.getConsumerRuleFilter().get(99),
            "Last should be filter99.pro");
    }

    /**
     * Test setConsumerRuleFilter with special characters.
     * Filters with special regex characters should be handled.
     */
    @Test
    public void testSetConsumerRuleFilter_withSpecialCharacters() {
        // Given: A list with special characters
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = Arrays.asList(
            "consumer-[0-9]*.pro",
            "rules_v2.0.txt",
            "filter-$special.pro"
        );

        // When: Setting filters with special characters
        config.setConsumerRuleFilter(newList);

        // Then: Should preserve special characters
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Should have 3 filters");
        assertEquals("consumer-[0-9]*.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("rules_v2.0.txt", config.getConsumerRuleFilter().get(1));
        assertEquals("filter-$special.pro", config.getConsumerRuleFilter().get(2));
    }

    // ==================== Real-world Usage Scenarios ====================

    /**
     * Test setConsumerRuleFilter for resetting to default state.
     * Common scenario: resetting filters to a default set.
     */
    @Test
    public void testSetConsumerRuleFilter_resetToDefaultState() {
        // Given: A VariantConfiguration with custom filters
        VariantConfiguration config = new VariantConfiguration("release");
        config.consumerRuleFilter("custom1.pro", "custom2.pro");

        // When: Resetting to default state
        List<String> defaultList = Arrays.asList("consumer-rules.pro");
        config.setConsumerRuleFilter(defaultList);

        // Then: Should have only default filter
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Should have 1 default filter");
        assertEquals("consumer-rules.pro", config.getConsumerRuleFilter().get(0));
    }

    /**
     * Test setConsumerRuleFilter for bulk replacement scenario.
     * Common scenario: replacing all filters at once.
     */
    @Test
    public void testSetConsumerRuleFilter_bulkReplacement() {
        // Given: A VariantConfiguration with old filters
        VariantConfiguration config = new VariantConfiguration("release");
        config.consumerRuleFilter("old1.pro", "old2.pro", "old3.pro");

        // When: Bulk replacing with new filters
        List<String> newFilters = Arrays.asList(
            "*.pro",
            "consumer-*.txt",
            "lib-rules.pro"
        );
        config.setConsumerRuleFilter(newFilters);

        // Then: Should have all new filters
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Should have 3 new filters");
        assertEquals("*.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("consumer-*.txt", config.getConsumerRuleFilter().get(1));
        assertEquals("lib-rules.pro", config.getConsumerRuleFilter().get(2));
    }

    /**
     * Test setConsumerRuleFilter for cloning filters from another variant.
     * Common scenario: copying filters from one variant to another.
     */
    @Test
    public void testSetConsumerRuleFilter_cloningFromAnotherVariant() {
        // Given: A source VariantConfiguration with filters
        VariantConfiguration source = new VariantConfiguration("release");
        source.consumerRuleFilter("*.pro", "consumer-*.txt");

        VariantConfiguration target = new VariantConfiguration("production");

        // When: Cloning filters
        target.setConsumerRuleFilter(source.getConsumerRuleFilter());

        // Then: Target should have same filters
        assertEquals(2, target.getConsumerRuleFilter().size(),
            "Target should have 2 filters");
        assertSame(source.getConsumerRuleFilter(), target.getConsumerRuleFilter(),
            "Target should reference same list as source");
    }

    /**
     * Test setConsumerRuleFilter for library module setup.
     * Library modules typically set specific consumer rule filters.
     */
    @Test
    public void testSetConsumerRuleFilter_libraryModuleSetup() {
        // Given: A library module configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Setting up library consumer filters
        List<String> libraryFilters = Arrays.asList(
            "consumer-rules.pro",
            "consumer-*.pro",
            "lib/consumer-*.txt"
        );
        config.setConsumerRuleFilter(libraryFilters);

        // Then: Should have library-specific filters
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Library should have 3 consumer filters");
        assertTrue(config.getConsumerRuleFilter().get(0).contains("consumer"),
            "Filters should contain 'consumer'");
    }

    // ==================== Type Safety Tests ====================

    /**
     * Test that setConsumerRuleFilter accepts any List<String>.
     * The method should accept the correct type.
     */
    @Test
    public void testSetConsumerRuleFilter_acceptsCorrectType() {
        // Given: A VariantConfiguration and a properly typed list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = Arrays.asList("*.pro");

        // When: Setting filters (should compile without issues)
        config.setConsumerRuleFilter(newList);

        // Then: Should work without errors
        assertNotNull(config.getConsumerRuleFilter(),
            "setConsumerRuleFilter() should accept List<String>");
    }

    // ==================== Consistency Tests ====================

    /**
     * Test that getConsumerRuleFilter returns what was set by setConsumerRuleFilter.
     * The getter should return what the setter set.
     */
    @Test
    public void testSetConsumerRuleFilter_consistentWithGetter() {
        // Given: A VariantConfiguration and a list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = Arrays.asList("filter1.pro", "filter2.pro");

        // When: Setting and then getting filters
        config.setConsumerRuleFilter(newList);
        List<String> retrieved = config.getConsumerRuleFilter();

        // Then: Should get what was set
        assertSame(newList, retrieved,
            "getConsumerRuleFilter() should return what was set by setConsumerRuleFilter()");
        assertEquals(2, retrieved.size(), "Size should match");
        assertEquals("filter1.pro", retrieved.get(0), "First filter should match");
        assertEquals("filter2.pro", retrieved.get(1), "Second filter should match");
    }

    /**
     * Test that setConsumerRuleFilter followed by modifications is consistent.
     * After setting, the list should behave normally with modifications.
     */
    @Test
    public void testSetConsumerRuleFilter_consistentAfterModifications() {
        // Given: A VariantConfiguration with set filters
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> newList = new ArrayList<>();
        newList.add("initial.pro");
        config.setConsumerRuleFilter(newList);

        // When: Modifying the list through getConsumerRuleFilter()
        config.getConsumerRuleFilter().add("added.pro");
        config.getConsumerRuleFilter().add("another.pro");

        // Then: Should reflect all modifications
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Should have 3 filters after additions");
        assertEquals("initial.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("added.pro", config.getConsumerRuleFilter().get(1));
        assertEquals("another.pro", config.getConsumerRuleFilter().get(2));
    }

    /**
     * Test setConsumerRuleFilter doesn't affect getName().
     * Setting filters should not affect other properties.
     */
    @Test
    public void testSetConsumerRuleFilter_doesNotAffectName() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        String nameBefore = config.getName();

        // When: Setting filters
        List<String> newList = Arrays.asList("*.pro");
        config.setConsumerRuleFilter(newList);

        // Then: Name should remain unchanged
        assertEquals(nameBefore, config.getName(),
            "setConsumerRuleFilter() should not affect getName()");
        assertEquals("debug", config.getName(), "Name should still be 'debug'");
    }

    /**
     * Test setConsumerRuleFilter doesn't affect getConfigurations().
     * Setting filters should not affect configurations property.
     */
    @Test
    public void testSetConsumerRuleFilter_doesNotAffectConfigurations() {
        // Given: A VariantConfiguration with configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("rules.pro");
        int configSizeBefore = config.getConfigurations().size();

        // When: Setting filters
        List<String> newList = Arrays.asList("*.pro", "*.txt");
        config.setConsumerRuleFilter(newList);

        // Then: Configurations should remain unchanged
        assertEquals(configSizeBefore, config.getConfigurations().size(),
            "setConsumerRuleFilter() should not affect configurations");
        assertEquals(1, config.getConfigurations().size(),
            "Configurations should still have 1 item");
    }

    /**
     * Test that setConsumerRuleFilter clearing doesn't affect configurations.
     * Even clearing filters should not impact configurations.
     */
    @Test
    public void testSetConsumerRuleFilter_clearingDoesNotAffectConfigurations() {
        // Given: A VariantConfiguration with both configurations and filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("rules.pro");
        config.consumerRuleFilter("*.pro", "*.txt");

        // When: Clearing filters via setter
        config.setConsumerRuleFilter(new ArrayList<>());

        // Then: Configurations should remain intact
        assertEquals(1, config.getConfigurations().size(),
            "Configurations should remain unchanged");
        assertEquals(0, config.getConsumerRuleFilter().size(),
            "Filters should be cleared");
    }

    // ==================== Filter Pattern Validation Tests ====================

    /**
     * Test setConsumerRuleFilter preserves all filter pattern types.
     * Various pattern types should all be preserved correctly.
     */
    @Test
    public void testSetConsumerRuleFilter_preservesAllPatternTypes() {
        // Given: A list with various pattern types
        VariantConfiguration config = new VariantConfiguration("debug");
        List<String> patterns = Arrays.asList(
            "*.pro",                    // Wildcard
            "consumer-rules.pro",       // Specific file
            "consumer/*.txt",           // Path pattern
            "lib-[0-9]*.pro",          // Regex pattern
            ""                          // Empty string
        );

        // When: Setting filters
        config.setConsumerRuleFilter(patterns);

        // Then: All pattern types should be preserved
        assertEquals(5, config.getConsumerRuleFilter().size(),
            "Should have all 5 patterns");
        assertEquals("*.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("consumer-rules.pro", config.getConsumerRuleFilter().get(1));
        assertEquals("consumer/*.txt", config.getConsumerRuleFilter().get(2));
        assertEquals("lib-[0-9]*.pro", config.getConsumerRuleFilter().get(3));
        assertEquals("", config.getConsumerRuleFilter().get(4));
    }

    // ==================== Integration Tests ====================

    /**
     * Test complete workflow: set, modify, reset.
     * A complete workflow should work seamlessly.
     */
    @Test
    public void testSetConsumerRuleFilter_completeWorkflow() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Setting initial filters
        List<String> initialFilters = new ArrayList<>();
        initialFilters.add("*.pro");
        config.setConsumerRuleFilter(initialFilters);
        assertEquals(1, config.getConsumerRuleFilter().size());

        // When: Modifying via the list
        config.getConsumerRuleFilter().add("*.txt");
        assertEquals(2, config.getConsumerRuleFilter().size());

        // When: Resetting to different filters
        List<String> newFilters = Arrays.asList("consumer-rules.pro");
        config.setConsumerRuleFilter(newFilters);

        // Then: Should have only the new filters
        assertEquals(1, config.getConsumerRuleFilter().size());
        assertEquals("consumer-rules.pro", config.getConsumerRuleFilter().get(0));
    }
}

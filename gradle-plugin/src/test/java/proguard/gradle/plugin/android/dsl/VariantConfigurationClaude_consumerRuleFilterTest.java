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
 * Test class for proguard.gradle.plugin.android.dsl.VariantConfiguration.consumerRuleFilter.([Ljava/lang/String;)V
 *
 * Tests the consumerRuleFilter() vararg method of VariantConfiguration which adds multiple consumer rule filter patterns.
 * This method takes a vararg of filter pattern strings and adds them to the consumerRuleFilter list.
 * Consumer rule filters are patterns used to filter which ProGuard rules should be published to consumers of a library.
 * It's a convenience method for adding multiple filters at once.
 */
public class VariantConfigurationClaude_consumerRuleFilterTest {

    // ==================== Basic consumerRuleFilter Tests ====================

    /**
     * Test that consumerRuleFilter() adds multiple filters.
     * The most basic use case - adding multiple filters at once.
     */
    @Test
    public void testConsumerRuleFilter_addsMultipleFilters() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding multiple filters
        config.consumerRuleFilter("*.pro", "*.txt", "consumer-*.pro");

        // Then: Should have three filters
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "consumerRuleFilter() should add 3 filters");
        assertEquals("*.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("*.txt", config.getConsumerRuleFilter().get(1));
        assertEquals("consumer-*.pro", config.getConsumerRuleFilter().get(2));
    }

    /**
     * Test that consumerRuleFilter() with no arguments does nothing.
     * Calling with zero arguments should be a no-op.
     */
    @Test
    public void testConsumerRuleFilter_withNoArguments() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Calling consumerRuleFilter with no arguments
        config.consumerRuleFilter();

        // Then: Should add nothing
        assertEquals(0, config.getConsumerRuleFilter().size(),
            "consumerRuleFilter() with no arguments should add nothing");
    }

    /**
     * Test that consumerRuleFilter() with single argument works.
     * Single argument should add one filter.
     */
    @Test
    public void testConsumerRuleFilter_withSingleArgument() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding single filter via vararg method
        config.consumerRuleFilter("*.pro");

        // Then: Should have one filter
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "consumerRuleFilter() with one argument should add 1 filter");
        assertEquals("*.pro", config.getConsumerRuleFilter().get(0));
    }

    /**
     * Test that consumerRuleFilter() with two arguments works.
     * Two arguments is a common use case.
     */
    @Test
    public void testConsumerRuleFilter_withTwoArguments() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding two filters
        config.consumerRuleFilter("*.pro", "*.txt");

        // Then: Should have two filters
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "consumerRuleFilter() with two arguments should add 2 filters");
        assertEquals("*.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("*.txt", config.getConsumerRuleFilter().get(1));
    }

    // ==================== Order Preservation Tests ====================

    /**
     * Test that consumerRuleFilter() maintains order.
     * Filters should be added in the order provided.
     */
    @Test
    public void testConsumerRuleFilter_maintainsOrder() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding filters in specific order
        config.consumerRuleFilter("first.pro", "second.txt", "third.pro", "fourth.cfg");

        // Then: Order should be preserved
        assertEquals("first.pro", config.getConsumerRuleFilter().get(0),
            "First should be first.pro");
        assertEquals("second.txt", config.getConsumerRuleFilter().get(1),
            "Second should be second.txt");
        assertEquals("third.pro", config.getConsumerRuleFilter().get(2),
            "Third should be third.pro");
        assertEquals("fourth.cfg", config.getConsumerRuleFilter().get(3),
            "Fourth should be fourth.cfg");
    }

    // ==================== Appending Tests ====================

    /**
     * Test that consumerRuleFilter() appends to existing filters.
     * New filters should be added to the end of the list.
     */
    @Test
    public void testConsumerRuleFilter_appendsToExisting() {
        // Given: A VariantConfiguration with existing filters
        VariantConfiguration config = new VariantConfiguration("release");
        config.getConsumerRuleFilter().add("existing.pro");

        // When: Adding more filters via vararg method
        config.consumerRuleFilter("new1.pro", "new2.txt");

        // Then: Should append to existing
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Should have 3 filters total");
        assertEquals("existing.pro", config.getConsumerRuleFilter().get(0),
            "First should be existing.pro");
        assertEquals("new1.pro", config.getConsumerRuleFilter().get(1),
            "Second should be new1.pro");
        assertEquals("new2.txt", config.getConsumerRuleFilter().get(2),
            "Third should be new2.txt");
    }

    /**
     * Test that multiple calls to consumerRuleFilter() append.
     * Each call should add to the list, not replace it.
     */
    @Test
    public void testConsumerRuleFilter_multipleCalls_append() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Calling consumerRuleFilter multiple times
        config.consumerRuleFilter("first.pro");
        config.consumerRuleFilter("second.txt", "third.pro");
        config.consumerRuleFilter("fourth.cfg");

        // Then: All should be present
        assertEquals(4, config.getConsumerRuleFilter().size(),
            "Should have 4 filters from multiple calls");
        assertEquals("first.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("second.txt", config.getConsumerRuleFilter().get(1));
        assertEquals("third.pro", config.getConsumerRuleFilter().get(2));
        assertEquals("fourth.cfg", config.getConsumerRuleFilter().get(3));
    }

    // ==================== Filter Pattern Tests ====================

    /**
     * Test consumerRuleFilter() with wildcard patterns.
     * Wildcard patterns are common for consumer rule filters.
     */
    @Test
    public void testConsumerRuleFilter_withWildcardPatterns() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding wildcard patterns
        config.consumerRuleFilter("*.pro", "*.txt", "consumer-*");

        // Then: Should contain all wildcard patterns
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Should have 3 wildcard patterns");
        assertTrue(config.getConsumerRuleFilter().get(0).contains("*"),
            "First should contain wildcard");
        assertTrue(config.getConsumerRuleFilter().get(1).contains("*"),
            "Second should contain wildcard");
        assertTrue(config.getConsumerRuleFilter().get(2).contains("*"),
            "Third should contain wildcard");
    }

    /**
     * Test consumerRuleFilter() with specific file patterns.
     * Specific file names should work as filters.
     */
    @Test
    public void testConsumerRuleFilter_withSpecificFilePatterns() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding specific file patterns
        config.consumerRuleFilter("consumer-rules.pro", "library-rules.txt");

        // Then: Should contain all specific patterns
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "Should have 2 specific patterns");
        assertEquals("consumer-rules.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("library-rules.txt", config.getConsumerRuleFilter().get(1));
    }

    /**
     * Test consumerRuleFilter() with path patterns.
     * Patterns with path separators should work.
     */
    @Test
    public void testConsumerRuleFilter_withPathPatterns() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding path patterns
        config.consumerRuleFilter("consumer/*.pro", "rules/consumer-*.txt");

        // Then: Should contain all path patterns
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "Should have 2 path patterns");
        assertTrue(config.getConsumerRuleFilter().get(0).contains("/"),
            "First should contain path separator");
        assertTrue(config.getConsumerRuleFilter().get(1).contains("/"),
            "Second should contain path separator");
    }

    /**
     * Test consumerRuleFilter() with regex-like patterns.
     * Complex patterns with regex characters should be preserved.
     */
    @Test
    public void testConsumerRuleFilter_withRegexPatterns() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding regex-like patterns
        config.consumerRuleFilter("consumer-[0-9]*.pro", "lib-\\w+.txt");

        // Then: Should preserve regex characters
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "Should have 2 regex patterns");
        assertTrue(config.getConsumerRuleFilter().get(0).contains("["),
            "First should contain regex characters");
        assertTrue(config.getConsumerRuleFilter().get(1).contains("\\"),
            "Second should contain escape character");
    }

    // ==================== Edge Cases ====================

    /**
     * Test consumerRuleFilter() with empty string.
     * Empty strings should be allowed.
     */
    @Test
    public void testConsumerRuleFilter_withEmptyString() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding filter with empty string
        config.consumerRuleFilter("");

        // Then: Should add empty string filter
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Should have 1 filter");
        assertEquals("", config.getConsumerRuleFilter().get(0),
            "Filter should be empty string");
    }

    /**
     * Test consumerRuleFilter() with empty strings mixed with valid patterns.
     * Empty strings should be handled along with valid filters.
     */
    @Test
    public void testConsumerRuleFilter_withMixedEmptyStrings() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding filters including empty strings
        config.consumerRuleFilter("", "*.pro", "", "*.txt");

        // Then: All should be added
        assertEquals(4, config.getConsumerRuleFilter().size(),
            "Should have 4 filters");
        assertEquals("", config.getConsumerRuleFilter().get(0));
        assertEquals("*.pro", config.getConsumerRuleFilter().get(1));
        assertEquals("", config.getConsumerRuleFilter().get(2));
        assertEquals("*.txt", config.getConsumerRuleFilter().get(3));
    }

    /**
     * Test consumerRuleFilter() with duplicate filters.
     * Duplicates should be allowed.
     */
    @Test
    public void testConsumerRuleFilter_withDuplicates() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding duplicate filters
        config.consumerRuleFilter("*.pro", "*.pro", "*.pro");

        // Then: All duplicates should be added
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Should have 3 filters (including duplicates)");
        assertEquals("*.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("*.pro", config.getConsumerRuleFilter().get(1));
        assertEquals("*.pro", config.getConsumerRuleFilter().get(2));
    }

    /**
     * Test consumerRuleFilter() with many arguments.
     * Should handle large numbers of filters.
     */
    @Test
    public void testConsumerRuleFilter_withManyArguments() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding many filters at once
        config.consumerRuleFilter(
            "filter1.pro", "filter2.pro", "filter3.pro", "filter4.pro", "filter5.pro",
            "filter6.pro", "filter7.pro", "filter8.pro", "filter9.pro", "filter10.pro"
        );

        // Then: All should be added
        assertEquals(10, config.getConsumerRuleFilter().size(),
            "Should have 10 filters");
        assertEquals("filter1.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("filter10.pro", config.getConsumerRuleFilter().get(9));
    }

    /**
     * Test consumerRuleFilter() with special characters.
     * Special characters should be preserved.
     */
    @Test
    public void testConsumerRuleFilter_withSpecialCharacters() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding filters with special characters
        config.consumerRuleFilter(
            "rules-v1.0.pro",
            "custom_library.txt",
            "proguard (2).pro"
        );

        // Then: Special characters should be preserved
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Should have 3 filters");
        assertEquals("rules-v1.0.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("custom_library.txt", config.getConsumerRuleFilter().get(1));
        assertEquals("proguard (2).pro", config.getConsumerRuleFilter().get(2));
    }

    /**
     * Test consumerRuleFilter() with Unicode characters.
     * Unicode characters should be handled.
     */
    @Test
    public void testConsumerRuleFilter_withUnicodeCharacters() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding filters with Unicode characters
        config.consumerRuleFilter("规则.pro", "ルール.txt");

        // Then: Unicode should be preserved
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "Should have 2 filters");
        assertEquals("规则.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("ルール.txt", config.getConsumerRuleFilter().get(1));
    }

    // ==================== Real-world Usage Scenarios ====================

    /**
     * Test consumerRuleFilter() for library module scenario.
     * Library modules use consumer rule filters to specify which rules to publish.
     */
    @Test
    public void testConsumerRuleFilter_libraryModuleScenario() {
        // Given: A library module configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Setting up library consumer filters
        config.consumerRuleFilter("consumer-rules.pro", "consumer-*.txt");

        // Then: Should have library-specific filters
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "Library should have consumer rule filters");
        assertEquals("consumer-rules.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("consumer-*.txt", config.getConsumerRuleFilter().get(1));
    }

    /**
     * Test consumerRuleFilter() for multi-module library scenario.
     * Multi-module projects might have complex filter patterns.
     */
    @Test
    public void testConsumerRuleFilter_multiModuleLibraryScenario() {
        // Given: A multi-module library configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Setting up multi-module filters
        config.consumerRuleFilter(
            "consumer-rules.pro",
            "api-consumer-rules.pro",
            "internal-consumer-*.pro"
        );

        // Then: Should have all module-specific filters
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Multi-module should have multiple filters");
        assertTrue(config.getConsumerRuleFilter().contains("consumer-rules.pro"));
        assertTrue(config.getConsumerRuleFilter().contains("api-consumer-rules.pro"));
        assertTrue(config.getConsumerRuleFilter().contains("internal-consumer-*.pro"));
    }

    /**
     * Test consumerRuleFilter() for application module scenario.
     * Application modules typically don't publish consumer rules.
     */
    @Test
    public void testConsumerRuleFilter_applicationModuleScenario() {
        // Given: An application module configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Not adding consumer rule filters for application
        // (No consumer rule filters added)

        // Then: Should have no filters
        assertTrue(config.getConsumerRuleFilter().isEmpty(),
            "Application module should have no consumer rule filters");
    }

    // ==================== Interaction with Other Methods ====================

    /**
     * Test consumerRuleFilter() doesn't affect configurations.
     * Adding filters should not affect configurations property.
     */
    @Test
    public void testConsumerRuleFilter_doesNotAffectConfigurations() {
        // Given: A VariantConfiguration with configurations
        VariantConfiguration config = new VariantConfiguration("release");
        config.configuration("rules.pro");
        int configSizeBefore = config.getConfigurations().size();

        // When: Adding consumer rule filters
        config.consumerRuleFilter("*.pro", "*.txt");

        // Then: Configurations should remain unchanged
        assertEquals(configSizeBefore, config.getConfigurations().size(),
            "consumerRuleFilter() should not affect configurations");
        assertEquals(1, config.getConfigurations().size(),
            "Configurations should still have 1 item");
    }

    /**
     * Test consumerRuleFilter() doesn't affect getName().
     * Adding filters should not affect other properties.
     */
    @Test
    public void testConsumerRuleFilter_doesNotAffectName() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");
        String nameBefore = config.getName();

        // When: Adding consumer rule filters
        config.consumerRuleFilter("*.pro", "*.txt");

        // Then: Name should remain unchanged
        assertEquals(nameBefore, config.getName(),
            "consumerRuleFilter() should not affect getName()");
        assertEquals("release", config.getName(), "Name should still be 'release'");
    }

    /**
     * Test consumerRuleFilter() after setConsumerRuleFilter().
     * Should append to the list set by setConsumerRuleFilter.
     */
    @Test
    public void testConsumerRuleFilter_afterSetConsumerRuleFilter() {
        // Given: A VariantConfiguration with filters set via setter
        VariantConfiguration config = new VariantConfiguration("release");
        List<String> initialList = new java.util.ArrayList<>();
        initialList.add("initial.pro");
        config.setConsumerRuleFilter(initialList);

        // When: Adding filters via consumerRuleFilter()
        config.consumerRuleFilter("additional1.pro", "additional2.txt");

        // Then: Should append to the set list
        assertEquals(3, config.getConsumerRuleFilter().size(),
            "Should have 3 filters total");
        assertEquals("initial.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("additional1.pro", config.getConsumerRuleFilter().get(1));
        assertEquals("additional2.txt", config.getConsumerRuleFilter().get(2));
    }

    // ==================== Multiple Instance Tests ====================

    /**
     * Test that consumerRuleFilter() on different instances are independent.
     * Adding to one instance shouldn't affect others.
     */
    @Test
    public void testConsumerRuleFilter_independentBetweenInstances() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");

        // When: Adding filters to each
        config1.consumerRuleFilter("debug1.pro", "debug2.txt");
        config2.consumerRuleFilter("release1.pro");

        // Then: Each should have its own filters
        assertEquals(2, config1.getConsumerRuleFilter().size(),
            "First instance should have 2 filters");
        assertEquals(1, config2.getConsumerRuleFilter().size(),
            "Second instance should have 1 filter");
        assertEquals("debug1.pro", config1.getConsumerRuleFilter().get(0));
        assertEquals("release1.pro", config2.getConsumerRuleFilter().get(0));
    }

    // ==================== Type Safety Tests ====================

    /**
     * Test that consumerRuleFilter() stores String values.
     * All filters should be String instances.
     */
    @Test
    public void testConsumerRuleFilter_storesStrings() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding filters
        config.consumerRuleFilter("filter1.pro", "filter2.txt");

        // Then: All should be String instances
        for (String filter : config.getConsumerRuleFilter()) {
            assertInstanceOf(String.class, filter,
                "Each filter should be a String");
        }
    }

    // ==================== Empty List Tests ====================

    /**
     * Test consumerRuleFilter() on initially empty list.
     * Should work correctly when starting with no filters.
     */
    @Test
    public void testConsumerRuleFilter_onInitiallyEmptyList() {
        // Given: A newly created VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");
        assertTrue(config.getConsumerRuleFilter().isEmpty(), "Should start empty");

        // When: Adding filters
        config.consumerRuleFilter("filter1.pro", "filter2.txt");

        // Then: Should have filters
        assertFalse(config.getConsumerRuleFilter().isEmpty(), "Should no longer be empty");
        assertEquals(2, config.getConsumerRuleFilter().size(), "Should have 2 filters");
    }

    // ==================== Return Value Tests ====================

    /**
     * Test that consumerRuleFilter() returns void.
     * The method doesn't return a value (returns void).
     */
    @Test
    public void testConsumerRuleFilter_returnsVoid() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding filters (returns void, so just verify no exception)
        config.consumerRuleFilter("*.pro");

        // Then: Should complete successfully
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Should have added filter successfully");
    }

    // ==================== Filter Pattern Validation Tests ====================

    /**
     * Test consumerRuleFilter() preserves all filter pattern types.
     * Various pattern types should all be preserved correctly.
     */
    @Test
    public void testConsumerRuleFilter_preservesAllPatternTypes() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding various pattern types
        config.consumerRuleFilter(
            "*.pro",                    // Wildcard
            "consumer-rules.pro",       // Specific file
            "consumer/*.txt",           // Path pattern
            "lib-[0-9]*.pro",          // Regex pattern
            ""                          // Empty string
        );

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
     * Test consumerRuleFilter() in a complete workflow.
     * A realistic complete workflow should work seamlessly.
     */
    @Test
    public void testConsumerRuleFilter_completeWorkflow() {
        // Given: A VariantConfiguration for library release build
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Setting up complete configuration
        config.defaultConfiguration("proguard-android-optimize.txt");
        config.configuration("proguard-rules.pro");
        config.consumerRuleFilter("consumer-rules.pro", "consumer-*.txt");

        // Then: Should have all configurations and filters
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "Should have 2 consumer rule filters");
        assertEquals("consumer-rules.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("consumer-*.txt", config.getConsumerRuleFilter().get(1));
    }

    /**
     * Test consumerRuleFilter() with all properties preserved.
     * The complete configuration state should be consistent.
     */
    @Test
    public void testConsumerRuleFilter_preservesAllProperties() {
        // Given: A VariantConfiguration with various properties set
        VariantConfiguration config = new VariantConfiguration("release");
        config.configuration("rules.pro");

        // When: Adding consumer rule filters
        config.consumerRuleFilter("*.pro", "*.txt");

        // Then: All properties should be intact
        assertEquals("release", config.getName(), "Name should be preserved");
        assertEquals(1, config.getConfigurations().size(), "Should have 1 configuration");
        assertEquals(2, config.getConsumerRuleFilter().size(), "Should have 2 filters");
        assertEquals("*.pro", config.getConsumerRuleFilter().get(0));
        assertEquals("*.txt", config.getConsumerRuleFilter().get(1));
    }

    // ==================== String Operations Tests ====================

    /**
     * Test that filters can be used with String operations.
     * Filters should support standard String methods.
     */
    @Test
    public void testConsumerRuleFilter_supportsStringOperations() {
        // Given: A VariantConfiguration with filters
        VariantConfiguration config = new VariantConfiguration("release");
        config.consumerRuleFilter("*.pro", "consumer-rules.txt");

        // When: Using String operations on filters
        String filter1 = config.getConsumerRuleFilter().get(0);
        String filter2 = config.getConsumerRuleFilter().get(1);

        // Then: String operations should work
        assertTrue(filter1.endsWith(".pro"), "Should work with endsWith()");
        assertTrue(filter2.startsWith("consumer"), "Should work with startsWith()");
        assertTrue(filter1.contains("*"), "Should work with contains()");
    }
}

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
 * Test class for proguard.gradle.plugin.android.dsl.VariantConfiguration.getConfigurations.()Ljava/util/List;
 *
 * Tests the getConfigurations() method of VariantConfiguration which returns the list of ProGuardConfiguration objects.
 * The configurations list is mutable and can contain UserProGuardConfiguration and DefaultProGuardConfiguration objects.
 * This list is initialized empty during construction and can be modified through configuration() and defaultConfiguration() methods.
 */
public class VariantConfigurationClaude_getConfigurationsTest {

    // ==================== Basic getConfigurations Tests ====================

    /**
     * Test that getConfigurations returns non-null list.
     * The configurations list should never be null.
     */
    @Test
    public void testGetConfigurations_notNull() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the configurations list
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should not be null
        assertNotNull(result, "getConfigurations() should never return null");
    }

    /**
     * Test that getConfigurations returns empty list initially.
     * A newly created VariantConfiguration should have an empty configurations list.
     */
    @Test
    public void testGetConfigurations_initiallyEmpty() {
        // Given: A newly created VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the configurations list
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should be empty
        assertTrue(result.isEmpty(), "getConfigurations() should return empty list initially");
        assertEquals(0, result.size(), "getConfigurations() should have size 0 initially");
    }

    /**
     * Test that getConfigurations returns List type.
     * The return type should be a List.
     */
    @Test
    public void testGetConfigurations_returnsListType() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the configurations list
        Object result = config.getConfigurations();

        // Then: Should be a List instance
        assertInstanceOf(List.class, result, "getConfigurations() should return List type");
    }

    /**
     * Test that getConfigurations returns the same list instance.
     * Multiple calls should return the same list object.
     */
    @Test
    public void testGetConfigurations_returnsSameInstance() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the configurations list multiple times
        List<ProGuardConfiguration> result1 = config.getConfigurations();
        List<ProGuardConfiguration> result2 = config.getConfigurations();
        List<ProGuardConfiguration> result3 = config.getConfigurations();

        // Then: Should return same instance
        assertSame(result1, result2, "getConfigurations() should return same instance");
        assertSame(result2, result3, "getConfigurations() should return same instance");
    }

    // ==================== After Adding User Configurations ====================

    /**
     * Test getConfigurations after adding one user configuration.
     * The list should contain the added configuration.
     */
    @Test
    public void testGetConfigurations_afterAddingOneUserConfiguration() {
        // Given: A VariantConfiguration with one user configuration
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("proguard-rules.pro");

        // When: Getting the configurations list
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should contain one configuration
        assertFalse(result.isEmpty(), "getConfigurations() should not be empty");
        assertEquals(1, result.size(), "getConfigurations() should have size 1");
        assertInstanceOf(UserProGuardConfiguration.class, result.get(0),
            "Configuration should be UserProGuardConfiguration");
        assertEquals("proguard-rules.pro", result.get(0).getFilename(),
            "Configuration should have correct filename");
    }

    /**
     * Test getConfigurations after adding multiple user configurations.
     * The list should contain all added configurations in order.
     */
    @Test
    public void testGetConfigurations_afterAddingMultipleUserConfigurations() {
        // Given: A VariantConfiguration with multiple user configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("proguard-rules.pro");
        config.configuration("proguard-custom.pro");
        config.configuration("proguard-extra.pro");

        // When: Getting the configurations list
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should contain three configurations
        assertEquals(3, result.size(), "getConfigurations() should have size 3");
        assertEquals("proguard-rules.pro", result.get(0).getFilename(),
            "First configuration should be proguard-rules.pro");
        assertEquals("proguard-custom.pro", result.get(1).getFilename(),
            "Second configuration should be proguard-custom.pro");
        assertEquals("proguard-extra.pro", result.get(2).getFilename(),
            "Third configuration should be proguard-extra.pro");
    }

    /**
     * Test getConfigurations after using configurations vararg method.
     * The list should contain all configurations added via varargs.
     */
    @Test
    public void testGetConfigurations_afterUsingConfigurationsVararg() {
        // Given: A VariantConfiguration with multiple configurations added via varargs
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configurations("rules1.pro", "rules2.pro", "rules3.pro");

        // When: Getting the configurations list
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should contain three configurations
        assertEquals(3, result.size(), "getConfigurations() should have size 3");
        assertTrue(result.get(0) instanceof UserProGuardConfiguration,
            "All should be UserProGuardConfiguration");
        assertTrue(result.get(1) instanceof UserProGuardConfiguration,
            "All should be UserProGuardConfiguration");
        assertTrue(result.get(2) instanceof UserProGuardConfiguration,
            "All should be UserProGuardConfiguration");
    }

    // ==================== After Adding Default Configurations ====================

    /**
     * Test getConfigurations after adding one default configuration.
     * The list should contain the default configuration.
     */
    @Test
    public void testGetConfigurations_afterAddingOneDefaultConfiguration() {
        // Given: A VariantConfiguration with one default configuration
        VariantConfiguration config = new VariantConfiguration("release");
        config.defaultConfiguration("proguard-android.txt");

        // When: Getting the configurations list
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should contain one default configuration
        assertEquals(1, result.size(), "getConfigurations() should have size 1");
        assertInstanceOf(DefaultProGuardConfiguration.class, result.get(0),
            "Configuration should be DefaultProGuardConfiguration");
        assertEquals("proguard-android.txt", result.get(0).getFilename(),
            "Configuration should have correct filename");
    }

    /**
     * Test getConfigurations after adding multiple default configurations.
     * The list should contain all default configurations.
     */
    @Test
    public void testGetConfigurations_afterAddingMultipleDefaultConfigurations() {
        // Given: A VariantConfiguration with multiple default configurations
        VariantConfiguration config = new VariantConfiguration("release");
        config.defaultConfiguration("proguard-android.txt");
        config.defaultConfiguration("proguard-android-optimize.txt");

        // When: Getting the configurations list
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should contain two default configurations
        assertEquals(2, result.size(), "getConfigurations() should have size 2");
        assertInstanceOf(DefaultProGuardConfiguration.class, result.get(0),
            "First should be DefaultProGuardConfiguration");
        assertInstanceOf(DefaultProGuardConfiguration.class, result.get(1),
            "Second should be DefaultProGuardConfiguration");
    }

    /**
     * Test getConfigurations after using defaultConfigurations vararg method.
     * The list should contain all default configurations added via varargs.
     */
    @Test
    public void testGetConfigurations_afterUsingDefaultConfigurationsVararg() {
        // Given: A VariantConfiguration with multiple default configurations via varargs
        VariantConfiguration config = new VariantConfiguration("release");
        config.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");

        // When: Getting the configurations list
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should contain two default configurations
        assertEquals(2, result.size(), "getConfigurations() should have size 2");
        assertTrue(result.get(0) instanceof DefaultProGuardConfiguration,
            "All should be DefaultProGuardConfiguration");
        assertTrue(result.get(1) instanceof DefaultProGuardConfiguration,
            "All should be DefaultProGuardConfiguration");
    }

    // ==================== Mixed User and Default Configurations ====================

    /**
     * Test getConfigurations with mixed user and default configurations.
     * The list should contain both types in the order they were added.
     */
    @Test
    public void testGetConfigurations_mixedUserAndDefaultConfigurations() {
        // Given: A VariantConfiguration with mixed configurations
        VariantConfiguration config = new VariantConfiguration("release");
        config.defaultConfiguration("proguard-android.txt");
        config.configuration("proguard-rules.pro");
        config.defaultConfiguration("proguard-android-optimize.txt");
        config.configuration("proguard-custom.pro");

        // When: Getting the configurations list
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should contain all configurations in order
        assertEquals(4, result.size(), "getConfigurations() should have size 4");
        assertInstanceOf(DefaultProGuardConfiguration.class, result.get(0),
            "First should be DefaultProGuardConfiguration");
        assertInstanceOf(UserProGuardConfiguration.class, result.get(1),
            "Second should be UserProGuardConfiguration");
        assertInstanceOf(DefaultProGuardConfiguration.class, result.get(2),
            "Third should be DefaultProGuardConfiguration");
        assertInstanceOf(UserProGuardConfiguration.class, result.get(3),
            "Fourth should be UserProGuardConfiguration");
    }

    /**
     * Test getConfigurations maintains insertion order.
     * The configurations should appear in the order they were added.
     */
    @Test
    public void testGetConfigurations_maintainsInsertionOrder() {
        // Given: A VariantConfiguration with configurations added in specific order
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("first.pro");
        config.configuration("second.pro");
        config.configuration("third.pro");

        // When: Getting the configurations list
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should maintain insertion order
        assertEquals("first.pro", result.get(0).getFilename(),
            "First configuration should be first.pro");
        assertEquals("second.pro", result.get(1).getFilename(),
            "Second configuration should be second.pro");
        assertEquals("third.pro", result.get(2).getFilename(),
            "Third configuration should be third.pro");
    }

    // ==================== List Mutability Tests ====================

    /**
     * Test that getConfigurations returns a mutable list.
     * The returned list should allow modifications.
     */
    @Test
    public void testGetConfigurations_returnsMutableList() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> list = config.getConfigurations();

        // When: Adding a configuration directly to the list
        list.add(new UserProGuardConfiguration("direct-add.pro"));

        // Then: Should be able to modify the list
        assertEquals(1, list.size(), "List should be mutable");
        assertEquals(1, config.getConfigurations().size(),
            "Modification should be reflected in subsequent calls");
    }

    /**
     * Test that modifications to returned list affect the internal state.
     * Changes to the returned list should persist.
     */
    @Test
    public void testGetConfigurations_modificationsAffectInternalState() {
        // Given: A VariantConfiguration with one configuration
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("initial.pro");
        List<ProGuardConfiguration> list1 = config.getConfigurations();

        // When: Modifying the list and getting it again
        list1.add(new UserProGuardConfiguration("added.pro"));
        List<ProGuardConfiguration> list2 = config.getConfigurations();

        // Then: Modification should persist
        assertEquals(2, list2.size(), "Modification should persist");
        assertEquals("added.pro", list2.get(1).getFilename(),
            "Added configuration should be present");
    }

    /**
     * Test that clearing the list works correctly.
     * The list should be clearable.
     */
    @Test
    public void testGetConfigurations_canClearList() {
        // Given: A VariantConfiguration with configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("rules1.pro");
        config.configuration("rules2.pro");
        List<ProGuardConfiguration> list = config.getConfigurations();

        // When: Clearing the list
        list.clear();

        // Then: List should be empty
        assertTrue(config.getConfigurations().isEmpty(),
            "List should be empty after clear");
        assertEquals(0, config.getConfigurations().size(),
            "List size should be 0 after clear");
    }

    /**
     * Test that removing items from the list works correctly.
     * Individual items should be removable.
     */
    @Test
    public void testGetConfigurations_canRemoveItems() {
        // Given: A VariantConfiguration with multiple configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("rules1.pro");
        config.configuration("rules2.pro");
        config.configuration("rules3.pro");
        List<ProGuardConfiguration> list = config.getConfigurations();

        // When: Removing an item
        ProGuardConfiguration removed = list.remove(1);

        // Then: Item should be removed
        assertEquals(2, config.getConfigurations().size(),
            "List should have 2 items after removal");
        assertEquals("rules2.pro", removed.getFilename(),
            "Removed item should be rules2.pro");
        assertEquals("rules1.pro", list.get(0).getFilename(),
            "First item should still be rules1.pro");
        assertEquals("rules3.pro", list.get(1).getFilename(),
            "Second item should now be rules3.pro");
    }

    // ==================== List Properties Tests ====================

    /**
     * Test that getConfigurations returns list with correct size.
     * The size should match the number of added configurations.
     */
    @Test
    public void testGetConfigurations_correctSize() {
        // Given: A VariantConfiguration with known number of configurations
        VariantConfiguration config = new VariantConfiguration("debug");

        // When/Then: Size should match number of additions
        assertEquals(0, config.getConfigurations().size(), "Initial size should be 0");

        config.configuration("rules1.pro");
        assertEquals(1, config.getConfigurations().size(), "Size should be 1 after adding one");

        config.configuration("rules2.pro");
        assertEquals(2, config.getConfigurations().size(), "Size should be 2 after adding two");

        config.configuration("rules3.pro");
        assertEquals(3, config.getConfigurations().size(), "Size should be 3 after adding three");
    }

    /**
     * Test that getConfigurations list supports contains check.
     * The list should support contains() operation.
     */
    @Test
    public void testGetConfigurations_supportsContains() {
        // Given: A VariantConfiguration with configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("rules.pro");
        List<ProGuardConfiguration> list = config.getConfigurations();
        ProGuardConfiguration firstConfig = list.get(0);

        // When: Checking if list contains the configuration
        boolean contains = list.contains(firstConfig);

        // Then: Should support contains operation
        assertTrue(contains, "List should support contains() and return true for added items");
    }

    /**
     * Test that getConfigurations list supports indexOf.
     * The list should support indexOf() operation.
     */
    @Test
    public void testGetConfigurations_supportsIndexOf() {
        // Given: A VariantConfiguration with multiple configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("rules1.pro");
        config.configuration("rules2.pro");
        config.configuration("rules3.pro");
        List<ProGuardConfiguration> list = config.getConfigurations();
        ProGuardConfiguration secondConfig = list.get(1);

        // When: Getting index of configuration
        int index = list.indexOf(secondConfig);

        // Then: Should return correct index
        assertEquals(1, index, "indexOf should return correct index");
    }

    /**
     * Test that getConfigurations list is iterable.
     * The list should support iteration.
     */
    @Test
    public void testGetConfigurations_isIterable() {
        // Given: A VariantConfiguration with configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("rules1.pro");
        config.configuration("rules2.pro");
        config.configuration("rules3.pro");

        // When: Iterating over the list
        int count = 0;
        for (ProGuardConfiguration pgConfig : config.getConfigurations()) {
            count++;
            assertNotNull(pgConfig, "Each item should be non-null");
        }

        // Then: Should iterate over all items
        assertEquals(3, count, "Should iterate over all 3 configurations");
    }

    // ==================== Multiple Instance Tests ====================

    /**
     * Test that different instances have independent configuration lists.
     * Modifying one instance's list shouldn't affect another.
     */
    @Test
    public void testGetConfigurations_independentBetweenInstances() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");

        // When: Adding configurations to first instance
        config1.configuration("rules1.pro");
        config1.configuration("rules2.pro");

        // Then: Second instance should remain unaffected
        assertEquals(2, config1.getConfigurations().size(),
            "First instance should have 2 configurations");
        assertEquals(0, config2.getConfigurations().size(),
            "Second instance should have 0 configurations");
    }

    /**
     * Test that instances with same configurations are independent.
     * Even with same configuration names, lists should be independent.
     */
    @Test
    public void testGetConfigurations_independentWithSameConfigurations() {
        // Given: Two VariantConfigurations with same configuration names
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("debug");
        config1.configuration("rules.pro");
        config2.configuration("rules.pro");

        // When: Modifying first instance's list
        config1.getConfigurations().clear();

        // Then: Second instance should be unaffected
        assertEquals(0, config1.getConfigurations().size(),
            "First instance should be empty after clear");
        assertEquals(1, config2.getConfigurations().size(),
            "Second instance should still have 1 configuration");
    }

    // ==================== Empty List Tests ====================

    /**
     * Test that empty list behaves correctly.
     * An empty list should have all expected empty list properties.
     */
    @Test
    public void testGetConfigurations_emptyListBehavior() {
        // Given: A VariantConfiguration with no configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> list = config.getConfigurations();

        // When/Then: Should behave as empty list
        assertTrue(list.isEmpty(), "isEmpty() should return true");
        assertEquals(0, list.size(), "size() should return 0");
        assertFalse(list.iterator().hasNext(), "iterator should have no elements");
    }

    // ==================== Real-world Usage Scenarios ====================

    /**
     * Test getConfigurations for debug build scenario.
     * Debug builds typically use minimal ProGuard configuration.
     */
    @Test
    public void testGetConfigurations_debugBuildScenario() {
        // Given: A debug build configuration
        VariantConfiguration config = new VariantConfiguration("debug");
        config.defaultConfiguration("proguard-android-debug.txt");

        // When: Getting configurations
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should have debug configuration
        assertEquals(1, result.size(), "Debug should have 1 configuration");
        assertInstanceOf(DefaultProGuardConfiguration.class, result.get(0),
            "Should use default Android debug configuration");
    }

    /**
     * Test getConfigurations for release build scenario.
     * Release builds typically use multiple ProGuard configurations.
     */
    @Test
    public void testGetConfigurations_releaseBuildScenario() {
        // Given: A release build configuration
        VariantConfiguration config = new VariantConfiguration("release");
        config.defaultConfiguration("proguard-android-optimize.txt");
        config.configuration("proguard-rules.pro");

        // When: Getting configurations
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should have both default and custom configurations
        assertEquals(2, result.size(), "Release should have 2 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, result.get(0),
            "First should be default configuration");
        assertInstanceOf(UserProGuardConfiguration.class, result.get(1),
            "Second should be user configuration");
    }

    /**
     * Test getConfigurations for library module scenario.
     * Library modules might have consumer rules in addition to regular rules.
     */
    @Test
    public void testGetConfigurations_libraryModuleScenario() {
        // Given: A library module configuration
        VariantConfiguration config = new VariantConfiguration("release");
        config.configuration("proguard-rules.pro");
        config.configuration("consumer-rules.pro");

        // When: Getting configurations
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should have both rule files
        assertEquals(2, result.size(), "Library should have 2 configurations");
        assertEquals("proguard-rules.pro", result.get(0).getFilename(),
            "First should be proguard-rules.pro");
        assertEquals("consumer-rules.pro", result.get(1).getFilename(),
            "Second should be consumer-rules.pro");
    }

    /**
     * Test getConfigurations for flavor-specific scenario.
     * Product flavors might have flavor-specific ProGuard rules.
     */
    @Test
    public void testGetConfigurations_flavorSpecificScenario() {
        // Given: A flavor-specific configuration
        VariantConfiguration config = new VariantConfiguration("freeRelease");
        config.defaultConfiguration("proguard-android.txt");
        config.configuration("proguard-rules.pro");
        config.configuration("proguard-free.pro");

        // When: Getting configurations
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should have default, base, and flavor-specific rules
        assertEquals(3, result.size(), "Flavor variant should have 3 configurations");
        assertEquals("proguard-free.pro", result.get(2).getFilename(),
            "Last should be flavor-specific configuration");
    }

    // ==================== Stream Operations Tests ====================

    /**
     * Test that getConfigurations list works with Stream API.
     * The list should support Java Stream operations.
     */
    @Test
    public void testGetConfigurations_worksWithStreams() {
        // Given: A VariantConfiguration with multiple configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("rules1.pro");
        config.configuration("rules2.pro");
        config.configuration("rules3.pro");

        // When: Using stream operations
        long count = config.getConfigurations().stream()
            .filter(c -> c instanceof UserProGuardConfiguration)
            .count();

        // Then: Should work with streams
        assertEquals(3, count, "Stream operations should work correctly");
    }

    /**
     * Test filtering configurations using streams.
     * Should be able to filter configurations by type.
     */
    @Test
    public void testGetConfigurations_streamFiltering() {
        // Given: A VariantConfiguration with mixed configurations
        VariantConfiguration config = new VariantConfiguration("release");
        config.defaultConfiguration("proguard-android.txt");
        config.configuration("proguard-rules.pro");
        config.defaultConfiguration("proguard-android-optimize.txt");

        // When: Filtering for default configurations
        long defaultCount = config.getConfigurations().stream()
            .filter(c -> c instanceof DefaultProGuardConfiguration)
            .count();

        // Then: Should correctly filter
        assertEquals(2, defaultCount, "Should have 2 default configurations");
    }

    // ==================== Edge Cases ====================

    /**
     * Test getConfigurations after adding configuration with empty filename.
     * Edge case: empty filenames should be handled.
     */
    @Test
    public void testGetConfigurations_withEmptyFilename() {
        // Given: A VariantConfiguration with empty filename configuration
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("");

        // When: Getting configurations
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should contain configuration with empty filename
        assertEquals(1, result.size(), "Should have 1 configuration");
        assertEquals("", result.get(0).getFilename(), "Filename should be empty string");
    }

    /**
     * Test getConfigurations with many configurations.
     * Should handle a large number of configurations.
     */
    @Test
    public void testGetConfigurations_withManyConfigurations() {
        // Given: A VariantConfiguration with many configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        int count = 100;
        for (int i = 0; i < count; i++) {
            config.configuration("rules" + i + ".pro");
        }

        // When: Getting configurations
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should contain all configurations
        assertEquals(count, result.size(), "Should have 100 configurations");
        assertEquals("rules0.pro", result.get(0).getFilename(),
            "First should be rules0.pro");
        assertEquals("rules99.pro", result.get(99).getFilename(),
            "Last should be rules99.pro");
    }

    // ==================== Type Safety Tests ====================

    /**
     * Test that getConfigurations returns correctly typed list.
     * The list should be properly typed as List<ProGuardConfiguration>.
     */
    @Test
    public void testGetConfigurations_correctlyTypedList() {
        // Given: A VariantConfiguration with configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("rules.pro");
        config.defaultConfiguration("proguard-android.txt");

        // When: Getting configurations
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: All items should be ProGuardConfiguration instances
        for (ProGuardConfiguration pgConfig : result) {
            assertInstanceOf(ProGuardConfiguration.class, pgConfig,
                "Each item should be a ProGuardConfiguration");
        }
    }

    /**
     * Test that list contains only ProGuardConfiguration subclasses.
     * UserProGuardConfiguration and DefaultProGuardConfiguration are both valid.
     */
    @Test
    public void testGetConfigurations_containsOnlyValidTypes() {
        // Given: A VariantConfiguration with mixed configurations
        VariantConfiguration config = new VariantConfiguration("release");
        config.configuration("user-rules.pro");
        config.defaultConfiguration("proguard-android.txt");

        // When: Getting configurations and checking types
        List<ProGuardConfiguration> result = config.getConfigurations();

        // Then: Should contain only valid subclasses
        assertTrue(result.get(0) instanceof UserProGuardConfiguration ||
                   result.get(0) instanceof DefaultProGuardConfiguration,
            "Should contain valid ProGuardConfiguration subclass");
        assertTrue(result.get(1) instanceof UserProGuardConfiguration ||
                   result.get(1) instanceof DefaultProGuardConfiguration,
            "Should contain valid ProGuardConfiguration subclass");
    }

    // ==================== Consistency Tests ====================

    /**
     * Test that getConfigurations is consistent after multiple operations.
     * The list should accurately reflect all operations performed.
     */
    @Test
    public void testGetConfigurations_consistentAfterMultipleOperations() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Performing multiple operations
        config.configuration("rules1.pro");
        assertEquals(1, config.getConfigurations().size());

        config.configuration("rules2.pro");
        assertEquals(2, config.getConfigurations().size());

        config.getConfigurations().remove(0);
        assertEquals(1, config.getConfigurations().size());

        config.configuration("rules3.pro");
        assertEquals(2, config.getConfigurations().size());

        // Then: Final state should be consistent
        assertEquals("rules2.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("rules3.pro", config.getConfigurations().get(1).getFilename());
    }

    /**
     * Test that getConfigurations reflects the current state accurately.
     * The list should always represent the current configuration state.
     */
    @Test
    public void testGetConfigurations_reflectsCurrentState() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When/Then: State should be consistent at each step
        assertTrue(config.getConfigurations().isEmpty(), "Should start empty");

        config.configuration("rules.pro");
        assertFalse(config.getConfigurations().isEmpty(), "Should not be empty after adding");

        config.getConfigurations().clear();
        assertTrue(config.getConfigurations().isEmpty(), "Should be empty after clearing");
    }
}

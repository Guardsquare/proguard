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
 * Test class for proguard.gradle.plugin.android.dsl.VariantConfiguration.setConfigurations.(Ljava/util/List;)V
 *
 * Tests the setConfigurations() method of VariantConfiguration which sets the list of ProGuardConfiguration objects.
 * This is a setter method for the 'var configurations' property in Kotlin.
 * The method completely replaces the current configurations list with the provided list.
 */
public class VariantConfigurationClaude_setConfigurationsTest {

    // ==================== Basic setConfigurations Tests ====================

    /**
     * Test that setConfigurations sets a new list.
     * The most basic use case - replacing the configurations list.
     */
    @Test
    public void testSetConfigurations_setsNewList() {
        // Given: A VariantConfiguration and a new list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("rules.pro"));

        // When: Setting the configurations
        config.setConfigurations(newList);

        // Then: Should have the new list
        assertEquals(1, config.getConfigurations().size(),
            "setConfigurations() should set the new list");
        assertEquals("rules.pro", config.getConfigurations().get(0).getFilename(),
            "setConfigurations() should contain the correct configuration");
    }

    /**
     * Test that setConfigurations replaces existing list.
     * Setting a new list should completely replace the old one.
     */
    @Test
    public void testSetConfigurations_replacesExistingList() {
        // Given: A VariantConfiguration with existing configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("old-rules.pro");

        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("new-rules.pro"));

        // When: Setting new configurations
        config.setConfigurations(newList);

        // Then: Old configurations should be replaced
        assertEquals(1, config.getConfigurations().size(),
            "setConfigurations() should replace old list");
        assertEquals("new-rules.pro", config.getConfigurations().get(0).getFilename(),
            "setConfigurations() should contain only new configuration");
    }

    /**
     * Test that setConfigurations with empty list clears configurations.
     * Setting an empty list should remove all configurations.
     */
    @Test
    public void testSetConfigurations_withEmptyList() {
        // Given: A VariantConfiguration with existing configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("rules1.pro");
        config.configuration("rules2.pro");

        // When: Setting an empty list
        config.setConfigurations(new ArrayList<>());

        // Then: Should have no configurations
        assertTrue(config.getConfigurations().isEmpty(),
            "setConfigurations() with empty list should clear configurations");
        assertEquals(0, config.getConfigurations().size(),
            "setConfigurations() with empty list should result in size 0");
    }

    /**
     * Test that setConfigurations stores the reference to the list.
     * The actual list reference should be stored, not a copy.
     */
    @Test
    public void testSetConfigurations_storesReference() {
        // Given: A VariantConfiguration and a list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("rules.pro"));

        // When: Setting the configurations
        config.setConfigurations(newList);

        // Then: Should store the actual reference
        assertSame(newList, config.getConfigurations(),
            "setConfigurations() should store the actual list reference");
    }

    /**
     * Test that modifications to original list affect the configuration.
     * Since the reference is stored, changes to the original list should be reflected.
     */
    @Test
    public void testSetConfigurations_modificationsToOriginalListAffectConfig() {
        // Given: A VariantConfiguration with a set list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("rules.pro"));
        config.setConfigurations(newList);

        // When: Modifying the original list
        newList.add(new UserProGuardConfiguration("additional.pro"));

        // Then: Configuration should reflect the change
        assertEquals(2, config.getConfigurations().size(),
            "Modifications to original list should affect configuration");
        assertEquals("additional.pro", config.getConfigurations().get(1).getFilename(),
            "New item should be present in configuration");
    }

    // ==================== Setting Different List Types ====================

    /**
     * Test setConfigurations with ArrayList.
     * ArrayList is the most common List implementation.
     */
    @Test
    public void testSetConfigurations_withArrayList() {
        // Given: A VariantConfiguration and an ArrayList
        VariantConfiguration config = new VariantConfiguration("debug");
        ArrayList<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("rules.pro"));

        // When: Setting with ArrayList
        config.setConfigurations(newList);

        // Then: Should work correctly
        assertEquals(1, config.getConfigurations().size(),
            "setConfigurations() should work with ArrayList");
        assertInstanceOf(ArrayList.class, config.getConfigurations(),
            "Should maintain ArrayList type");
    }

    /**
     * Test setConfigurations with Arrays.asList result.
     * Arrays.asList creates a fixed-size list.
     */
    @Test
    public void testSetConfigurations_withArraysAsList() {
        // Given: A VariantConfiguration and Arrays.asList list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = Arrays.asList(
            new UserProGuardConfiguration("rules1.pro"),
            new UserProGuardConfiguration("rules2.pro")
        );

        // When: Setting with Arrays.asList
        config.setConfigurations(newList);

        // Then: Should work correctly
        assertEquals(2, config.getConfigurations().size(),
            "setConfigurations() should work with Arrays.asList");
    }

    /**
     * Test setConfigurations with Collections.emptyList().
     * Empty list constant should work.
     */
    @Test
    public void testSetConfigurations_withEmptyListConstant() {
        // Given: A VariantConfiguration with existing configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("old-rules.pro");

        // When: Setting with Collections.emptyList()
        config.setConfigurations(Collections.emptyList());

        // Then: Should clear configurations
        assertTrue(config.getConfigurations().isEmpty(),
            "setConfigurations() should work with Collections.emptyList()");
    }

    /**
     * Test setConfigurations with Collections.singletonList().
     * Single element list should work.
     */
    @Test
    public void testSetConfigurations_withSingletonList() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = Collections.singletonList(
            new UserProGuardConfiguration("rules.pro")
        );

        // When: Setting with Collections.singletonList()
        config.setConfigurations(newList);

        // Then: Should work correctly
        assertEquals(1, config.getConfigurations().size(),
            "setConfigurations() should work with Collections.singletonList()");
    }

    // ==================== Setting Lists with Different Content ====================

    /**
     * Test setConfigurations with only UserProGuardConfiguration.
     * List with only user configurations should work.
     */
    @Test
    public void testSetConfigurations_withOnlyUserConfigurations() {
        // Given: A list with only user configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("rules1.pro"));
        newList.add(new UserProGuardConfiguration("rules2.pro"));
        newList.add(new UserProGuardConfiguration("rules3.pro"));

        // When: Setting the configurations
        config.setConfigurations(newList);

        // Then: Should contain all user configurations
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        for (ProGuardConfiguration pgConfig : config.getConfigurations()) {
            assertInstanceOf(UserProGuardConfiguration.class, pgConfig,
                "All should be UserProGuardConfiguration");
        }
    }

    /**
     * Test setConfigurations with only DefaultProGuardConfiguration.
     * List with only default configurations should work.
     */
    @Test
    public void testSetConfigurations_withOnlyDefaultConfigurations() {
        // Given: A list with only default configurations
        VariantConfiguration config = new VariantConfiguration("release");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt"));
        newList.add(DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt"));

        // When: Setting the configurations
        config.setConfigurations(newList);

        // Then: Should contain all default configurations
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        for (ProGuardConfiguration pgConfig : config.getConfigurations()) {
            assertInstanceOf(DefaultProGuardConfiguration.class, pgConfig,
                "All should be DefaultProGuardConfiguration");
        }
    }

    /**
     * Test setConfigurations with mixed configuration types.
     * List with both user and default configurations should work.
     */
    @Test
    public void testSetConfigurations_withMixedConfigurations() {
        // Given: A list with mixed configurations
        VariantConfiguration config = new VariantConfiguration("release");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt"));
        newList.add(new UserProGuardConfiguration("proguard-rules.pro"));
        newList.add(DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt"));
        newList.add(new UserProGuardConfiguration("proguard-custom.pro"));

        // When: Setting the configurations
        config.setConfigurations(newList);

        // Then: Should contain all configurations in order
        assertEquals(4, config.getConfigurations().size(), "Should have 4 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0),
            "First should be DefaultProGuardConfiguration");
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1),
            "Second should be UserProGuardConfiguration");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(2),
            "Third should be DefaultProGuardConfiguration");
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(3),
            "Fourth should be UserProGuardConfiguration");
    }

    // ==================== Order Preservation Tests ====================

    /**
     * Test that setConfigurations preserves order.
     * The order of configurations in the provided list should be maintained.
     */
    @Test
    public void testSetConfigurations_preservesOrder() {
        // Given: A list with configurations in specific order
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("first.pro"));
        newList.add(new UserProGuardConfiguration("second.pro"));
        newList.add(new UserProGuardConfiguration("third.pro"));

        // When: Setting the configurations
        config.setConfigurations(newList);

        // Then: Order should be preserved
        assertEquals("first.pro", config.getConfigurations().get(0).getFilename(),
            "First should be first.pro");
        assertEquals("second.pro", config.getConfigurations().get(1).getFilename(),
            "Second should be second.pro");
        assertEquals("third.pro", config.getConfigurations().get(2).getFilename(),
            "Third should be third.pro");
    }

    // ==================== Multiple Instance Tests ====================

    /**
     * Test that setConfigurations affects only the target instance.
     * Setting configurations on one instance shouldn't affect others.
     */
    @Test
    public void testSetConfigurations_affectsOnlyTargetInstance() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        config1.configuration("debug-rules.pro");
        config2.configuration("release-rules.pro");

        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("new-rules.pro"));

        // When: Setting configurations on first instance
        config1.setConfigurations(newList);

        // Then: Only first instance should be affected
        assertEquals(1, config1.getConfigurations().size(),
            "First instance should have 1 configuration");
        assertEquals("new-rules.pro", config1.getConfigurations().get(0).getFilename(),
            "First instance should have new configuration");
        assertEquals(1, config2.getConfigurations().size(),
            "Second instance should still have 1 configuration");
        assertEquals("release-rules.pro", config2.getConfigurations().get(0).getFilename(),
            "Second instance should retain original configuration");
    }

    /**
     * Test that same list can be set on multiple instances.
     * The same list reference can be shared between instances.
     */
    @Test
    public void testSetConfigurations_canShareListBetweenInstances() {
        // Given: Two VariantConfigurations and one list
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        List<ProGuardConfiguration> sharedList = new ArrayList<>();
        sharedList.add(new UserProGuardConfiguration("shared-rules.pro"));

        // When: Setting same list on both instances
        config1.setConfigurations(sharedList);
        config2.setConfigurations(sharedList);

        // Then: Both should reference the same list
        assertSame(config1.getConfigurations(), config2.getConfigurations(),
            "Both instances should share the same list reference");
        assertEquals(1, config1.getConfigurations().size(),
            "First instance should have the configuration");
        assertEquals(1, config2.getConfigurations().size(),
            "Second instance should have the configuration");
    }

    /**
     * Test that modifying shared list affects all instances.
     * When multiple instances share a list, modifications affect all.
     */
    @Test
    public void testSetConfigurations_sharedListModificationsAffectAll() {
        // Given: Two VariantConfigurations sharing a list
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        List<ProGuardConfiguration> sharedList = new ArrayList<>();
        sharedList.add(new UserProGuardConfiguration("shared-rules.pro"));
        config1.setConfigurations(sharedList);
        config2.setConfigurations(sharedList);

        // When: Modifying the shared list through one instance
        config1.getConfigurations().add(new UserProGuardConfiguration("additional.pro"));

        // Then: Both instances should see the change
        assertEquals(2, config1.getConfigurations().size(),
            "First instance should have 2 configurations");
        assertEquals(2, config2.getConfigurations().size(),
            "Second instance should have 2 configurations");
    }

    // ==================== Interaction with Other Methods ====================

    /**
     * Test setConfigurations after using configuration() method.
     * setConfigurations should replace configurations added via configuration().
     */
    @Test
    public void testSetConfigurations_afterUsingConfigurationMethod() {
        // Given: A VariantConfiguration with configurations added via configuration()
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("old1.pro");
        config.configuration("old2.pro");

        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("new.pro"));

        // When: Setting new configurations
        config.setConfigurations(newList);

        // Then: Old configurations should be replaced
        assertEquals(1, config.getConfigurations().size(),
            "Should only have new configuration");
        assertEquals("new.pro", config.getConfigurations().get(0).getFilename(),
            "Should have the new configuration");
    }

    /**
     * Test using configuration() method after setConfigurations.
     * configuration() should add to the list set by setConfigurations.
     */
    @Test
    public void testSetConfigurations_thenUsingConfigurationMethod() {
        // Given: A VariantConfiguration with configurations set via setter
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("initial.pro"));
        config.setConfigurations(newList);

        // When: Adding configuration via configuration() method
        config.configuration("additional.pro");

        // Then: Should add to the existing list
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("initial.pro", config.getConfigurations().get(0).getFilename(),
            "First should be initial.pro");
        assertEquals("additional.pro", config.getConfigurations().get(1).getFilename(),
            "Second should be additional.pro");
    }

    /**
     * Test setConfigurations after using defaultConfiguration() method.
     * setConfigurations should replace configurations added via defaultConfiguration().
     */
    @Test
    public void testSetConfigurations_afterUsingDefaultConfigurationMethod() {
        // Given: A VariantConfiguration with default configurations
        VariantConfiguration config = new VariantConfiguration("release");
        config.defaultConfiguration("proguard-android.txt");

        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("user-rules.pro"));

        // When: Setting new configurations
        config.setConfigurations(newList);

        // Then: Default configuration should be replaced
        assertEquals(1, config.getConfigurations().size(),
            "Should only have new configuration");
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(0),
            "Should be UserProGuardConfiguration, not default");
    }

    /**
     * Test using defaultConfiguration() method after setConfigurations.
     * defaultConfiguration() should add to the list set by setConfigurations.
     */
    @Test
    public void testSetConfigurations_thenUsingDefaultConfigurationMethod() {
        // Given: A VariantConfiguration with configurations set via setter
        VariantConfiguration config = new VariantConfiguration("release");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("user-rules.pro"));
        config.setConfigurations(newList);

        // When: Adding default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should add to the existing list
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(0),
            "First should be UserProGuardConfiguration");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(1),
            "Second should be DefaultProGuardConfiguration");
    }

    // ==================== Setting Same List Multiple Times ====================

    /**
     * Test setConfigurations called multiple times with different lists.
     * Each call should replace the previous list.
     */
    @Test
    public void testSetConfigurations_multipleTimesWithDifferentLists() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Setting configurations multiple times
        List<ProGuardConfiguration> list1 = new ArrayList<>();
        list1.add(new UserProGuardConfiguration("first.pro"));
        config.setConfigurations(list1);

        List<ProGuardConfiguration> list2 = new ArrayList<>();
        list2.add(new UserProGuardConfiguration("second.pro"));
        config.setConfigurations(list2);

        List<ProGuardConfiguration> list3 = new ArrayList<>();
        list3.add(new UserProGuardConfiguration("third.pro"));
        config.setConfigurations(list3);

        // Then: Should have only the last list
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("third.pro", config.getConfigurations().get(0).getFilename(),
            "Should have the last set configuration");
        assertSame(list3, config.getConfigurations(),
            "Should reference the last set list");
    }

    /**
     * Test setConfigurations called with same list reference multiple times.
     * Setting the same reference again should work (idempotent).
     */
    @Test
    public void testSetConfigurations_multipleTimes_withSameReference() {
        // Given: A VariantConfiguration and a list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> list = new ArrayList<>();
        list.add(new UserProGuardConfiguration("rules.pro"));

        // When: Setting configurations multiple times with same reference
        config.setConfigurations(list);
        config.setConfigurations(list);
        config.setConfigurations(list);

        // Then: Should still reference the same list
        assertSame(list, config.getConfigurations(),
            "Should reference the same list");
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
    }

    // ==================== Edge Cases ====================

    /**
     * Test setConfigurations with list containing duplicate configurations.
     * Duplicates should be allowed.
     */
    @Test
    public void testSetConfigurations_withDuplicateConfigurations() {
        // Given: A list with duplicate configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        ProGuardConfiguration duplicate = new UserProGuardConfiguration("rules.pro");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(duplicate);
        newList.add(duplicate);
        newList.add(duplicate);

        // When: Setting configurations with duplicates
        config.setConfigurations(newList);

        // Then: Should accept duplicates
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 items (including duplicates)");
        assertSame(duplicate, config.getConfigurations().get(0),
            "First should be the duplicate");
        assertSame(duplicate, config.getConfigurations().get(1),
            "Second should be the duplicate");
        assertSame(duplicate, config.getConfigurations().get(2),
            "Third should be the duplicate");
    }

    /**
     * Test setConfigurations with large list.
     * Should handle large numbers of configurations.
     */
    @Test
    public void testSetConfigurations_withLargeList() {
        // Given: A large list of configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        int count = 100;
        for (int i = 0; i < count; i++) {
            newList.add(new UserProGuardConfiguration("rules" + i + ".pro"));
        }

        // When: Setting large list
        config.setConfigurations(newList);

        // Then: Should contain all configurations
        assertEquals(count, config.getConfigurations().size(),
            "Should have 100 configurations");
        assertEquals("rules0.pro", config.getConfigurations().get(0).getFilename(),
            "First should be rules0.pro");
        assertEquals("rules99.pro", config.getConfigurations().get(99).getFilename(),
            "Last should be rules99.pro");
    }

    // ==================== Real-world Usage Scenarios ====================

    /**
     * Test setConfigurations for resetting to default state.
     * Common scenario: resetting configurations to a default set.
     */
    @Test
    public void testSetConfigurations_resetToDefaultState() {
        // Given: A VariantConfiguration with custom configurations
        VariantConfiguration config = new VariantConfiguration("release");
        config.configuration("custom1.pro");
        config.configuration("custom2.pro");

        // When: Resetting to default state
        List<ProGuardConfiguration> defaultList = new ArrayList<>();
        defaultList.add(DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt"));
        config.setConfigurations(defaultList);

        // Then: Should have only default configuration
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 default configuration");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0),
            "Should be DefaultProGuardConfiguration");
    }

    /**
     * Test setConfigurations for bulk replacement scenario.
     * Common scenario: replacing all configurations at once.
     */
    @Test
    public void testSetConfigurations_bulkReplacement() {
        // Given: A VariantConfiguration with old configurations
        VariantConfiguration config = new VariantConfiguration("release");
        config.configuration("old1.pro");
        config.configuration("old2.pro");
        config.configuration("old3.pro");

        // When: Bulk replacing with new configurations
        List<ProGuardConfiguration> newConfigs = Arrays.asList(
            DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt"),
            new UserProGuardConfiguration("new-rules.pro"),
            new UserProGuardConfiguration("new-consumer-rules.pro")
        );
        config.setConfigurations(newConfigs);

        // Then: Should have all new configurations
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 new configurations");
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(0).getFilename(),
            "First should be proguard-android-optimize.txt");
        assertEquals("new-rules.pro", config.getConfigurations().get(1).getFilename(),
            "Second should be new-rules.pro");
        assertEquals("new-consumer-rules.pro", config.getConfigurations().get(2).getFilename(),
            "Third should be new-consumer-rules.pro");
    }

    /**
     * Test setConfigurations for cloning configuration from another variant.
     * Common scenario: copying configurations from one variant to another.
     */
    @Test
    public void testSetConfigurations_cloningFromAnotherVariant() {
        // Given: A source VariantConfiguration with configurations
        VariantConfiguration source = new VariantConfiguration("release");
        source.defaultConfiguration("proguard-android-optimize.txt");
        source.configuration("proguard-rules.pro");

        VariantConfiguration target = new VariantConfiguration("production");

        // When: Cloning configurations
        target.setConfigurations(source.getConfigurations());

        // Then: Target should have same configurations
        assertEquals(2, target.getConfigurations().size(),
            "Target should have 2 configurations");
        assertSame(source.getConfigurations(), target.getConfigurations(),
            "Target should reference same list as source");
    }

    // ==================== Type Safety Tests ====================

    /**
     * Test that setConfigurations accepts any List<ProGuardConfiguration>.
     * The method should accept the correct type.
     */
    @Test
    public void testSetConfigurations_acceptsCorrectType() {
        // Given: A VariantConfiguration and a properly typed list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("rules.pro"));

        // When: Setting configurations (should compile without issues)
        config.setConfigurations(newList);

        // Then: Should work without errors
        assertNotNull(config.getConfigurations(),
            "setConfigurations() should accept List<ProGuardConfiguration>");
    }

    // ==================== Consistency Tests ====================

    /**
     * Test that getConfigurations returns what was set by setConfigurations.
     * The getter should return what the setter set.
     */
    @Test
    public void testSetConfigurations_consistentWithGetter() {
        // Given: A VariantConfiguration and a list
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("rules1.pro"));
        newList.add(new UserProGuardConfiguration("rules2.pro"));

        // When: Setting and then getting configurations
        config.setConfigurations(newList);
        List<ProGuardConfiguration> retrieved = config.getConfigurations();

        // Then: Should get what was set
        assertSame(newList, retrieved,
            "getConfigurations() should return what was set by setConfigurations()");
        assertEquals(2, retrieved.size(), "Size should match");
        assertEquals("rules1.pro", retrieved.get(0).getFilename(),
            "First configuration should match");
        assertEquals("rules2.pro", retrieved.get(1).getFilename(),
            "Second configuration should match");
    }

    /**
     * Test that setConfigurations followed by modifications is consistent.
     * After setting, the list should behave normally with modifications.
     */
    @Test
    public void testSetConfigurations_consistentAfterModifications() {
        // Given: A VariantConfiguration with set configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("initial.pro"));
        config.setConfigurations(newList);

        // When: Modifying the list through getConfigurations()
        config.getConfigurations().add(new UserProGuardConfiguration("added.pro"));
        config.getConfigurations().add(new UserProGuardConfiguration("another.pro"));

        // Then: Should reflect all modifications
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations after additions");
        assertEquals("initial.pro", config.getConfigurations().get(0).getFilename(),
            "First should be initial.pro");
        assertEquals("added.pro", config.getConfigurations().get(1).getFilename(),
            "Second should be added.pro");
        assertEquals("another.pro", config.getConfigurations().get(2).getFilename(),
            "Third should be another.pro");
    }

    /**
     * Test setConfigurations doesn't affect getName().
     * Setting configurations should not affect other properties.
     */
    @Test
    public void testSetConfigurations_doesNotAffectName() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        String nameBefore = config.getName();

        // When: Setting configurations
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("rules.pro"));
        config.setConfigurations(newList);

        // Then: Name should remain unchanged
        assertEquals(nameBefore, config.getName(),
            "setConfigurations() should not affect getName()");
        assertEquals("debug", config.getName(),
            "Name should still be 'debug'");
    }

    /**
     * Test setConfigurations doesn't affect consumerRuleFilter.
     * Setting configurations should not affect consumerRuleFilter property.
     */
    @Test
    public void testSetConfigurations_doesNotAffectConsumerRuleFilter() {
        // Given: A VariantConfiguration with consumer rule filter
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("*.pro", "*.txt");
        int filterSizeBefore = config.getConsumerRuleFilter().size();

        // When: Setting configurations
        List<ProGuardConfiguration> newList = new ArrayList<>();
        newList.add(new UserProGuardConfiguration("rules.pro"));
        config.setConfigurations(newList);

        // Then: ConsumerRuleFilter should remain unchanged
        assertEquals(filterSizeBefore, config.getConsumerRuleFilter().size(),
            "setConfigurations() should not affect consumerRuleFilter");
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "ConsumerRuleFilter should still have 2 items");
    }
}

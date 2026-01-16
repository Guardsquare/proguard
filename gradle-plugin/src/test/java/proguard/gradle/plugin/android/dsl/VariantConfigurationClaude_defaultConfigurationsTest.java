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
 * Test class for proguard.gradle.plugin.android.dsl.VariantConfiguration.defaultConfigurations.([Ljava/lang/String;)V
 *
 * Tests the defaultConfigurations() vararg method of VariantConfiguration which adds multiple default ProGuard configurations.
 * This method takes a vararg of default configuration names and adds each as a DefaultProGuardConfiguration to the configurations list.
 * It's a convenience method for adding multiple default configurations at once, internally calling defaultConfiguration() for each.
 * Default configurations are built-in Android ProGuard configurations like proguard-android.txt and proguard-android-optimize.txt.
 */
public class VariantConfigurationClaude_defaultConfigurationsTest {

    // ==================== Basic defaultConfigurations Tests ====================

    /**
     * Test that defaultConfigurations() adds multiple configurations.
     * The most basic use case - adding multiple default configurations at once.
     */
    @Test
    public void testDefaultConfigurations_addsMultipleConfigurations() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding multiple default configurations
        config.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");

        // Then: Should have two configurations
        assertEquals(2, config.getConfigurations().size(),
            "defaultConfigurations() should add 2 configurations");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(1).getFilename());
    }

    /**
     * Test that defaultConfigurations() adds DefaultProGuardConfiguration instances.
     * All added configurations should be of type DefaultProGuardConfiguration.
     */
    @Test
    public void testDefaultConfigurations_addsDefaultProGuardConfigurations() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding default configurations
        config.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");

        // Then: All should be DefaultProGuardConfiguration
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0),
            "Should add DefaultProGuardConfiguration");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(1),
            "Should add DefaultProGuardConfiguration");
    }

    /**
     * Test that defaultConfigurations() with no arguments does nothing.
     * Calling with zero arguments should be a no-op.
     */
    @Test
    public void testDefaultConfigurations_withNoArguments() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Calling defaultConfigurations with no arguments
        config.defaultConfigurations();

        // Then: Should add nothing
        assertEquals(0, config.getConfigurations().size(),
            "defaultConfigurations() with no arguments should add nothing");
    }

    /**
     * Test that defaultConfigurations() with single argument works.
     * Single argument should work just like defaultConfiguration().
     */
    @Test
    public void testDefaultConfigurations_withSingleArgument() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding single default configuration via vararg method
        config.defaultConfigurations("proguard-android.txt");

        // Then: Should have one configuration
        assertEquals(1, config.getConfigurations().size(),
            "defaultConfigurations() with one argument should add 1 configuration");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
    }

    /**
     * Test that defaultConfigurations() with three standard configurations works.
     * All three standard Android configurations should work.
     */
    @Test
    public void testDefaultConfigurations_withThreeStandardConfigs() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding all three standard configurations
        config.defaultConfigurations(
            "proguard-android.txt",
            "proguard-android-optimize.txt",
            "proguard-android-debug.txt"
        );

        // Then: Should have three configurations
        assertEquals(3, config.getConfigurations().size(),
            "defaultConfigurations() should add 3 configurations");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(1).getFilename());
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(2).getFilename());
    }

    // ==================== Order Preservation Tests ====================

    /**
     * Test that defaultConfigurations() maintains order.
     * Configurations should be added in the order provided.
     */
    @Test
    public void testDefaultConfigurations_maintainsOrder() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding default configurations in specific order
        config.defaultConfigurations(
            "proguard-android-debug.txt",
            "proguard-android.txt",
            "proguard-android-optimize.txt"
        );

        // Then: Order should be preserved
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(0).getFilename(),
            "First should be proguard-android-debug.txt");
        assertEquals("proguard-android.txt", config.getConfigurations().get(1).getFilename(),
            "Second should be proguard-android.txt");
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(2).getFilename(),
            "Third should be proguard-android-optimize.txt");
    }

    // ==================== Appending Tests ====================

    /**
     * Test that defaultConfigurations() appends to existing configurations.
     * New configurations should be added to the end of the list.
     */
    @Test
    public void testDefaultConfigurations_appendsToExisting() {
        // Given: A VariantConfiguration with existing configuration
        VariantConfiguration config = new VariantConfiguration("release");
        config.defaultConfiguration("proguard-android.txt");

        // When: Adding more default configurations via vararg method
        config.defaultConfigurations("proguard-android-optimize.txt", "proguard-android-debug.txt");

        // Then: Should append to existing
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations total");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(1).getFilename());
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test that multiple calls to defaultConfigurations() append.
     * Each call should add to the list, not replace it.
     */
    @Test
    public void testDefaultConfigurations_multipleCalls_append() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Calling defaultConfigurations multiple times
        config.defaultConfigurations("proguard-android.txt");
        config.defaultConfigurations("proguard-android-optimize.txt");

        // Then: All should be present
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations from multiple calls");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(1).getFilename());
    }

    // ==================== Interaction with Other Methods ====================

    /**
     * Test defaultConfigurations() after defaultConfiguration() method.
     * Should work seamlessly together.
     */
    @Test
    public void testDefaultConfigurations_afterDefaultConfiguration() {
        // Given: A VariantConfiguration with configuration added via singular method
        VariantConfiguration config = new VariantConfiguration("release");
        config.defaultConfiguration("proguard-android.txt");

        // When: Adding multiple via vararg method
        config.defaultConfigurations("proguard-android-optimize.txt", "proguard-android-debug.txt");

        // Then: Should have all configurations
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(1).getFilename());
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test defaultConfiguration() after defaultConfigurations() method.
     * Should work in reverse order as well.
     */
    @Test
    public void testDefaultConfigurations_beforeDefaultConfiguration() {
        // Given: A VariantConfiguration with configurations added via vararg method
        VariantConfiguration config = new VariantConfiguration("release");
        config.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");

        // When: Adding single via singular method
        config.defaultConfiguration("proguard-android-debug.txt");

        // Then: Should have all configurations
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(1).getFilename());
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test defaultConfigurations() with configuration() method.
     * Should work together, mixing default and user configurations.
     */
    @Test
    public void testDefaultConfigurations_withConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Mixing default and user configurations
        config.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");
        config.configuration("proguard-rules.pro");

        // Then: Should have all configurations in order
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(1));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(2));
    }

    /**
     * Test defaultConfigurations() with configurations() method.
     * Should work with user configurations vararg method.
     */
    @Test
    public void testDefaultConfigurations_withConfigurations() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Mixing default and user configurations via vararg methods
        config.defaultConfigurations("proguard-android.txt");
        config.configurations("rules1.pro", "rules2.pro");
        config.defaultConfigurations("proguard-android-optimize.txt");

        // Then: Should have all configurations in order
        assertEquals(4, config.getConfigurations().size(),
            "Should have 4 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(2));
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(3));
    }

    // ==================== Standard Android Configurations Tests ====================

    /**
     * Test defaultConfigurations() with proguard-android.txt.
     * The standard Android configuration should work.
     */
    @Test
    public void testDefaultConfigurations_withProguardAndroid() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding proguard-android.txt
        config.defaultConfigurations("proguard-android.txt");

        // Then: Should be added correctly
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
    }

    /**
     * Test defaultConfigurations() with proguard-android-optimize.txt.
     * The optimized Android configuration should work.
     */
    @Test
    public void testDefaultConfigurations_withProguardAndroidOptimize() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding proguard-android-optimize.txt
        config.defaultConfigurations("proguard-android-optimize.txt");

        // Then: Should be added correctly
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(0).getFilename());
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
    }

    /**
     * Test defaultConfigurations() with proguard-android-debug.txt.
     * The debug Android configuration should work.
     */
    @Test
    public void testDefaultConfigurations_withProguardAndroidDebug() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding proguard-android-debug.txt
        config.defaultConfigurations("proguard-android-debug.txt");

        // Then: Should be added correctly
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(0).getFilename());
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
    }

    // ==================== Edge Cases ====================

    /**
     * Test defaultConfigurations() with duplicate configuration names.
     * Duplicates should be allowed.
     */
    @Test
    public void testDefaultConfigurations_withDuplicates() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding duplicate default configurations
        config.defaultConfigurations(
            "proguard-android.txt",
            "proguard-android.txt",
            "proguard-android.txt"
        );

        // Then: All duplicates should be added
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations (including duplicates)");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android.txt", config.getConfigurations().get(1).getFilename());
        assertEquals("proguard-android.txt", config.getConfigurations().get(2).getFilename());
    }

    // ==================== Real-world Usage Scenarios ====================

    /**
     * Test defaultConfigurations() for release build scenario.
     * Release builds typically use proguard-android-optimize.txt.
     */
    @Test
    public void testDefaultConfigurations_releaseBuildScenario() {
        // Given: A release build configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding release-specific default configurations
        config.defaultConfigurations("proguard-android-optimize.txt");
        config.configuration("proguard-rules.pro");

        // Then: Should have both configurations
        assertEquals(2, config.getConfigurations().size(),
            "Release should have 2 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1));
    }

    /**
     * Test defaultConfigurations() for debug build scenario.
     * Debug builds typically use proguard-android-debug.txt.
     */
    @Test
    public void testDefaultConfigurations_debugBuildScenario() {
        // Given: A debug build configuration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding debug-specific default configuration
        config.defaultConfigurations("proguard-android-debug.txt");
        config.configuration("proguard-rules.pro");

        // Then: Should have both configurations
        assertEquals(2, config.getConfigurations().size(),
            "Debug should have 2 configurations");
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-rules.pro", config.getConfigurations().get(1).getFilename());
    }

    /**
     * Test defaultConfigurations() for standard app scenario.
     * Standard apps use proguard-android.txt.
     */
    @Test
    public void testDefaultConfigurations_standardAppScenario() {
        // Given: A standard app configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding standard default configuration
        config.defaultConfigurations("proguard-android.txt");
        config.configuration("proguard-rules.pro");

        // Then: Should have standard setup
        assertEquals(2, config.getConfigurations().size(),
            "Standard app should have 2 configurations");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-rules.pro", config.getConfigurations().get(1).getFilename());
    }

    // ==================== Multiple Instance Tests ====================

    /**
     * Test that defaultConfigurations() on different instances are independent.
     * Adding to one instance shouldn't affect others.
     */
    @Test
    public void testDefaultConfigurations_independentBetweenInstances() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");

        // When: Adding default configurations to each
        config1.defaultConfigurations("proguard-android-debug.txt");
        config2.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");

        // Then: Each should have its own configurations
        assertEquals(1, config1.getConfigurations().size(),
            "First instance should have 1 configuration");
        assertEquals(2, config2.getConfigurations().size(),
            "Second instance should have 2 configurations");
        assertEquals("proguard-android-debug.txt", config1.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android.txt", config2.getConfigurations().get(0).getFilename());
    }

    // ==================== Consistency Tests ====================

    /**
     * Test that defaultConfigurations() doesn't affect getName().
     * Adding configurations should not affect other properties.
     */
    @Test
    public void testDefaultConfigurations_doesNotAffectName() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");
        String nameBefore = config.getName();

        // When: Adding default configurations
        config.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");

        // Then: Name should remain unchanged
        assertEquals(nameBefore, config.getName(),
            "defaultConfigurations() should not affect getName()");
        assertEquals("release", config.getName(), "Name should still be 'release'");
    }

    /**
     * Test that defaultConfigurations() doesn't affect getConsumerRuleFilter().
     * Adding configurations should not affect consumer rule filter.
     */
    @Test
    public void testDefaultConfigurations_doesNotAffectConsumerRuleFilter() {
        // Given: A VariantConfiguration with consumer rule filters
        VariantConfiguration config = new VariantConfiguration("release");
        config.consumerRuleFilter("*.pro");
        int filterSizeBefore = config.getConsumerRuleFilter().size();

        // When: Adding default configurations
        config.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");

        // Then: Consumer rule filter should remain unchanged
        assertEquals(filterSizeBefore, config.getConsumerRuleFilter().size(),
            "defaultConfigurations() should not affect consumer rule filter");
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Consumer rule filter should still have 1 item");
    }

    // ==================== Type Verification Tests ====================

    /**
     * Test that all configurations added are DefaultProGuardConfiguration.
     * The method should only add default configurations, not user ones.
     */
    @Test
    public void testDefaultConfigurations_allAreDefaultProGuardConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding multiple default configurations
        config.defaultConfigurations(
            "proguard-android.txt",
            "proguard-android-optimize.txt",
            "proguard-android-debug.txt"
        );

        // Then: All should be DefaultProGuardConfiguration
        List<ProGuardConfiguration> configs = config.getConfigurations();
        for (ProGuardConfiguration pgConfig : configs) {
            assertInstanceOf(DefaultProGuardConfiguration.class, pgConfig,
                "All configurations should be DefaultProGuardConfiguration");
            assertNotNull(pgConfig.getFilename(), "Each should have a filename");
        }
    }

    /**
     * Test that configurations are actually ProGuardConfiguration instances.
     * Verify the type hierarchy is correct.
     */
    @Test
    public void testDefaultConfigurations_allAreProGuardConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding default configurations
        config.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");

        // Then: All should be ProGuardConfiguration instances
        for (ProGuardConfiguration pgConfig : config.getConfigurations()) {
            assertInstanceOf(ProGuardConfiguration.class, pgConfig,
                "All should be ProGuardConfiguration instances");
        }
    }

    // ==================== Empty Configurations List Tests ====================

    /**
     * Test defaultConfigurations() on initially empty list.
     * Should work correctly when starting with no configurations.
     */
    @Test
    public void testDefaultConfigurations_onInitiallyEmptyList() {
        // Given: A newly created VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");
        assertTrue(config.getConfigurations().isEmpty(), "Should start empty");

        // When: Adding default configurations
        config.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");

        // Then: Should have configurations
        assertFalse(config.getConfigurations().isEmpty(), "Should no longer be empty");
        assertEquals(2, config.getConfigurations().size(), "Should have 2 configurations");
    }

    // ==================== Integration with setConfigurations ====================

    /**
     * Test defaultConfigurations() after setConfigurations().
     * Should append to the list set by setConfigurations.
     */
    @Test
    public void testDefaultConfigurations_afterSetConfigurations() {
        // Given: A VariantConfiguration with configurations set via setter
        VariantConfiguration config = new VariantConfiguration("release");
        List<ProGuardConfiguration> initialList = new java.util.ArrayList<>();
        initialList.add(new UserProGuardConfiguration("initial.pro"));
        config.setConfigurations(initialList);

        // When: Adding default configurations via defaultConfigurations()
        config.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");

        // Then: Should append to the set list
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations total");
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(1));
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(2));
    }

    // ==================== Path Handling Tests ====================

    /**
     * Test that defaultConfigurations() stores filename, not full path.
     * Default configurations should only store the filename.
     */
    @Test
    public void testDefaultConfigurations_storesFilename() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding default configurations
        config.defaultConfigurations("proguard-android.txt", "proguard-android-optimize.txt");

        // Then: Should store filenames
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename(),
            "Should store filename");
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(1).getFilename(),
            "Should store filename");
    }

    /**
     * Test that defaultConfigurations() has correct path property.
     * Default configurations should have /lib/ prefix in path.
     */
    @Test
    public void testDefaultConfigurations_hasCorrectPath() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding default configuration
        config.defaultConfigurations("proguard-android.txt");

        // Then: Should have correct path
        ProGuardConfiguration pgConfig = config.getConfigurations().get(0);
        assertEquals("/lib/proguard-android.txt", pgConfig.getPath(),
            "Default configuration should have /lib/ prefix in path");
    }

    // ==================== Equivalence with defaultConfiguration() Tests ====================

    /**
     * Test that defaultConfigurations() is equivalent to multiple defaultConfiguration() calls.
     * The result should be the same as calling defaultConfiguration() multiple times.
     */
    @Test
    public void testDefaultConfigurations_equivalentToMultipleDefaultConfigurationCalls() {
        // Given: Two identical VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("release");
        VariantConfiguration config2 = new VariantConfiguration("release");

        // When: Using defaultConfigurations() on one and defaultConfiguration() on the other
        config1.defaultConfigurations(
            "proguard-android.txt",
            "proguard-android-optimize.txt",
            "proguard-android-debug.txt"
        );

        config2.defaultConfiguration("proguard-android.txt");
        config2.defaultConfiguration("proguard-android-optimize.txt");
        config2.defaultConfiguration("proguard-android-debug.txt");

        // Then: Results should be equivalent
        assertEquals(config1.getConfigurations().size(), config2.getConfigurations().size(),
            "Both should have same size");
        assertEquals(3, config1.getConfigurations().size());
        assertEquals(3, config2.getConfigurations().size());

        for (int i = 0; i < 3; i++) {
            assertEquals(
                config1.getConfigurations().get(i).getFilename(),
                config2.getConfigurations().get(i).getFilename(),
                "Filenames should match at index " + i
            );
            assertEquals(
                config1.getConfigurations().get(i).getClass(),
                config2.getConfigurations().get(i).getClass(),
                "Types should match at index " + i
            );
            assertEquals(
                config1.getConfigurations().get(i).getPath(),
                config2.getConfigurations().get(i).getPath(),
                "Paths should match at index " + i
            );
        }
    }

    // ==================== Invalid Configuration Name Tests ====================

    /**
     * Test defaultConfigurations() with invalid configuration name.
     * Invalid names should throw an exception from fromString().
     */
    @Test
    public void testDefaultConfigurations_withInvalidName_throwsException() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When/Then: Adding invalid default configuration should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            config.defaultConfigurations("invalid-config.txt");
        }, "Invalid default configuration name should throw IllegalArgumentException");
    }

    /**
     * Test defaultConfigurations() with mix of valid and invalid names.
     * Should throw exception when encountering invalid name.
     */
    @Test
    public void testDefaultConfigurations_withMixedValidAndInvalid_throwsException() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When/Then: Adding mix of valid and invalid should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            config.defaultConfigurations("proguard-android.txt", "invalid.txt");
        }, "Invalid configuration in list should throw IllegalArgumentException");
    }

    // ==================== Return Value Tests ====================

    /**
     * Test that defaultConfigurations() returns void.
     * The method doesn't return a value (returns void).
     */
    @Test
    public void testDefaultConfigurations_returnsVoid() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding configurations (returns void, so just verify no exception)
        config.defaultConfigurations("proguard-android.txt");

        // Then: Should complete successfully
        assertEquals(1, config.getConfigurations().size(),
            "Should have added configuration successfully");
    }

    // ==================== All Standard Configurations Tests ====================

    /**
     * Test defaultConfigurations() with all three standard Android configurations.
     * All three should be added successfully.
     */
    @Test
    public void testDefaultConfigurations_allThreeStandardConfigs() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding all three standard configurations
        config.defaultConfigurations(
            "proguard-android.txt",
            "proguard-android-optimize.txt",
            "proguard-android-debug.txt"
        );

        // Then: All three should be present
        assertEquals(3, config.getConfigurations().size(),
            "Should have all 3 standard configurations");

        // Verify all are default configurations
        for (int i = 0; i < 3; i++) {
            assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(i),
                "Configuration at index " + i + " should be DefaultProGuardConfiguration");
        }

        // Verify paths
        assertEquals("/lib/proguard-android.txt", config.getConfigurations().get(0).getPath());
        assertEquals("/lib/proguard-android-optimize.txt", config.getConfigurations().get(1).getPath());
        assertEquals("/lib/proguard-android-debug.txt", config.getConfigurations().get(2).getPath());
    }

    // ==================== Integration Tests ====================

    /**
     * Test defaultConfigurations() in a complete workflow.
     * A realistic complete workflow should work seamlessly.
     */
    @Test
    public void testDefaultConfigurations_completeWorkflow() {
        // Given: A VariantConfiguration for release build
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Building up configurations in a typical workflow
        config.defaultConfigurations("proguard-android-optimize.txt");
        config.configuration("proguard-rules.pro");
        config.configuration("proguard-library.pro");

        // Then: Should have all configurations in order
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(2));
    }
}

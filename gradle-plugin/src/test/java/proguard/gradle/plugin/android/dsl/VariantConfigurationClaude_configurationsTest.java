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
 * Test class for proguard.gradle.plugin.android.dsl.VariantConfiguration.configurations.([Ljava/lang/String;)V
 *
 * Tests the configurations() vararg method of VariantConfiguration which adds multiple user ProGuard configurations.
 * This method takes a vararg of configuration file paths and adds each as a UserProGuardConfiguration to the configurations list.
 * It's a convenience method for adding multiple configurations at once, internally calling configuration() for each.
 */
public class VariantConfigurationClaude_configurationsTest {

    // ==================== Basic configurations Tests ====================

    /**
     * Test that configurations() adds multiple configurations.
     * The most basic use case - adding multiple configurations at once.
     */
    @Test
    public void testConfigurations_addsMultipleConfigurations() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding multiple configurations
        config.configurations("rules1.pro", "rules2.pro", "rules3.pro");

        // Then: Should have three configurations
        assertEquals(3, config.getConfigurations().size(),
            "configurations() should add 3 configurations");
        assertEquals("rules1.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("rules2.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("rules3.pro", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test that configurations() adds UserProGuardConfiguration instances.
     * All added configurations should be of type UserProGuardConfiguration.
     */
    @Test
    public void testConfigurations_addsUserProGuardConfigurations() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations
        config.configurations("rules1.pro", "rules2.pro");

        // Then: All should be UserProGuardConfiguration
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(0),
            "Should add UserProGuardConfiguration");
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1),
            "Should add UserProGuardConfiguration");
    }

    /**
     * Test that configurations() with no arguments does nothing.
     * Calling with zero arguments should be a no-op.
     */
    @Test
    public void testConfigurations_withNoArguments() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Calling configurations with no arguments
        config.configurations();

        // Then: Should add nothing
        assertEquals(0, config.getConfigurations().size(),
            "configurations() with no arguments should add nothing");
    }

    /**
     * Test that configurations() with single argument works.
     * Single argument should work just like configuration().
     */
    @Test
    public void testConfigurations_withSingleArgument() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding single configuration via vararg method
        config.configurations("rules.pro");

        // Then: Should have one configuration
        assertEquals(1, config.getConfigurations().size(),
            "configurations() with one argument should add 1 configuration");
        assertEquals("rules.pro", config.getConfigurations().get(0).getFilename());
    }

    /**
     * Test that configurations() with two arguments works.
     * Two arguments is a common use case.
     */
    @Test
    public void testConfigurations_withTwoArguments() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding two configurations
        config.configurations("rules1.pro", "rules2.pro");

        // Then: Should have two configurations
        assertEquals(2, config.getConfigurations().size(),
            "configurations() with two arguments should add 2 configurations");
        assertEquals("rules1.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("rules2.pro", config.getConfigurations().get(1).getFilename());
    }

    // ==================== Order Preservation Tests ====================

    /**
     * Test that configurations() maintains order.
     * Configurations should be added in the order provided.
     */
    @Test
    public void testConfigurations_maintainsOrder() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations in specific order
        config.configurations("first.pro", "second.pro", "third.pro", "fourth.pro");

        // Then: Order should be preserved
        assertEquals("first.pro", config.getConfigurations().get(0).getFilename(),
            "First should be first.pro");
        assertEquals("second.pro", config.getConfigurations().get(1).getFilename(),
            "Second should be second.pro");
        assertEquals("third.pro", config.getConfigurations().get(2).getFilename(),
            "Third should be third.pro");
        assertEquals("fourth.pro", config.getConfigurations().get(3).getFilename(),
            "Fourth should be fourth.pro");
    }

    // ==================== Appending Tests ====================

    /**
     * Test that configurations() appends to existing configurations.
     * New configurations should be added to the end of the list.
     */
    @Test
    public void testConfigurations_appendsToExisting() {
        // Given: A VariantConfiguration with existing configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("existing.pro");

        // When: Adding more configurations via vararg method
        config.configurations("new1.pro", "new2.pro");

        // Then: Should append to existing
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations total");
        assertEquals("existing.pro", config.getConfigurations().get(0).getFilename(),
            "First should be existing.pro");
        assertEquals("new1.pro", config.getConfigurations().get(1).getFilename(),
            "Second should be new1.pro");
        assertEquals("new2.pro", config.getConfigurations().get(2).getFilename(),
            "Third should be new2.pro");
    }

    /**
     * Test that multiple calls to configurations() append.
     * Each call should add to the list, not replace it.
     */
    @Test
    public void testConfigurations_multipleCalls_append() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Calling configurations multiple times
        config.configurations("first.pro");
        config.configurations("second.pro", "third.pro");
        config.configurations("fourth.pro");

        // Then: All should be present
        assertEquals(4, config.getConfigurations().size(),
            "Should have 4 configurations from multiple calls");
        assertEquals("first.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("second.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("third.pro", config.getConfigurations().get(2).getFilename());
        assertEquals("fourth.pro", config.getConfigurations().get(3).getFilename());
    }

    // ==================== Interaction with Other Methods ====================

    /**
     * Test configurations() after configuration() method.
     * Should work seamlessly together.
     */
    @Test
    public void testConfigurations_afterConfiguration() {
        // Given: A VariantConfiguration with configuration added via singular method
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configuration("single.pro");

        // When: Adding multiple via vararg method
        config.configurations("multi1.pro", "multi2.pro");

        // Then: Should have all configurations
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertEquals("single.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("multi1.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("multi2.pro", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test configuration() after configurations() method.
     * Should work in reverse order as well.
     */
    @Test
    public void testConfigurations_beforeConfiguration() {
        // Given: A VariantConfiguration with configurations added via vararg method
        VariantConfiguration config = new VariantConfiguration("debug");
        config.configurations("multi1.pro", "multi2.pro");

        // When: Adding single via singular method
        config.configuration("single.pro");

        // Then: Should have all configurations
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertEquals("multi1.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("multi2.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("single.pro", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test configurations() with defaultConfiguration().
     * Should work together, mixing user and default configurations.
     */
    @Test
    public void testConfigurations_withDefaultConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Mixing configurations and defaultConfiguration
        config.defaultConfiguration("proguard-android.txt");
        config.configurations("custom1.pro", "custom2.pro");
        config.defaultConfiguration("proguard-android-optimize.txt");

        // Then: Should have all configurations in order
        assertEquals(4, config.getConfigurations().size(),
            "Should have 4 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(2));
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(3));
    }

    // ==================== File Path Tests ====================

    /**
     * Test configurations() with simple filenames.
     * Simple filenames without paths should work.
     */
    @Test
    public void testConfigurations_withSimpleFilenames() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations with simple filenames
        config.configurations("rules.pro", "custom.pro", "library.pro");

        // Then: All should be added correctly
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertEquals("rules.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("custom.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("library.pro", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test configurations() with relative paths.
     * Relative paths should be preserved.
     */
    @Test
    public void testConfigurations_withRelativePaths() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations with relative paths
        config.configurations("config/rules.pro", "custom/library.pro");

        // Then: Paths should be preserved
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("config/rules.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("custom/library.pro", config.getConfigurations().get(1).getFilename());
    }

    /**
     * Test configurations() with absolute paths.
     * Absolute paths should be preserved.
     */
    @Test
    public void testConfigurations_withAbsolutePaths() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations with absolute paths
        config.configurations("/etc/proguard/rules.pro", "/opt/config/custom.pro");

        // Then: Paths should be preserved
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("/etc/proguard/rules.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("/opt/config/custom.pro", config.getConfigurations().get(1).getFilename());
    }

    /**
     * Test configurations() with mixed path types.
     * Should handle simple names, relative paths, and absolute paths together.
     */
    @Test
    public void testConfigurations_withMixedPaths() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations with mixed paths
        config.configurations(
            "rules.pro",
            "config/custom.pro",
            "/etc/proguard/library.pro"
        );

        // Then: All path types should be preserved
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertEquals("rules.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("config/custom.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("/etc/proguard/library.pro", config.getConfigurations().get(2).getFilename());
    }

    // ==================== Edge Cases ====================

    /**
     * Test configurations() with empty string.
     * Empty strings should be handled.
     */
    @Test
    public void testConfigurations_withEmptyString() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with empty string
        config.configurations("");

        // Then: Should add configuration with empty filename
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("", config.getConfigurations().get(0).getFilename(),
            "Filename should be empty string");
    }

    /**
     * Test configurations() with empty strings mixed with valid names.
     * Empty strings should be handled along with valid filenames.
     */
    @Test
    public void testConfigurations_withMixedEmptyStrings() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations including empty strings
        config.configurations("", "rules.pro", "", "custom.pro");

        // Then: All should be added
        assertEquals(4, config.getConfigurations().size(),
            "Should have 4 configurations");
        assertEquals("", config.getConfigurations().get(0).getFilename());
        assertEquals("rules.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("", config.getConfigurations().get(2).getFilename());
        assertEquals("custom.pro", config.getConfigurations().get(3).getFilename());
    }

    /**
     * Test configurations() with duplicate filenames.
     * Duplicates should be allowed.
     */
    @Test
    public void testConfigurations_withDuplicates() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding duplicate configurations
        config.configurations("rules.pro", "rules.pro", "rules.pro");

        // Then: All duplicates should be added
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations (including duplicates)");
        assertEquals("rules.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("rules.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("rules.pro", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test configurations() with many arguments.
     * Should handle large numbers of configurations.
     */
    @Test
    public void testConfigurations_withManyArguments() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding many configurations at once
        config.configurations(
            "rules1.pro", "rules2.pro", "rules3.pro", "rules4.pro", "rules5.pro",
            "rules6.pro", "rules7.pro", "rules8.pro", "rules9.pro", "rules10.pro"
        );

        // Then: All should be added
        assertEquals(10, config.getConfigurations().size(),
            "Should have 10 configurations");
        assertEquals("rules1.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("rules10.pro", config.getConfigurations().get(9).getFilename());
    }

    /**
     * Test configurations() with special characters in filenames.
     * Special characters should be preserved.
     */
    @Test
    public void testConfigurations_withSpecialCharacters() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations with special characters
        config.configurations(
            "rules-v1.0.pro",
            "custom_library.pro",
            "proguard (2).pro"
        );

        // Then: Special characters should be preserved
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertEquals("rules-v1.0.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("custom_library.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("proguard (2).pro", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test configurations() with Unicode characters.
     * Unicode characters should be handled.
     */
    @Test
    public void testConfigurations_withUnicodeCharacters() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations with Unicode characters
        config.configurations("规则.pro", "ルール.pro");

        // Then: Unicode should be preserved
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("规则.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("ルール.pro", config.getConfigurations().get(1).getFilename());
    }

    // ==================== Real-world Usage Scenarios ====================

    /**
     * Test configurations() for debug build scenario.
     * Debug builds might have multiple configuration files.
     */
    @Test
    public void testConfigurations_debugBuildScenario() {
        // Given: A debug build configuration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding debug-specific configurations
        config.defaultConfiguration("proguard-android-debug.txt");
        config.configurations("proguard-rules.pro", "proguard-debug.pro");

        // Then: Should have all debug configurations
        assertEquals(3, config.getConfigurations().size(),
            "Debug should have 3 configurations");
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-rules.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("proguard-debug.pro", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test configurations() for release build scenario.
     * Release builds typically have multiple optimization configurations.
     */
    @Test
    public void testConfigurations_releaseBuildScenario() {
        // Given: A release build configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding release-specific configurations
        config.defaultConfiguration("proguard-android-optimize.txt");
        config.configurations(
            "proguard-rules.pro",
            "proguard-optimize.pro",
            "proguard-release.pro"
        );

        // Then: Should have all release configurations
        assertEquals(4, config.getConfigurations().size(),
            "Release should have 4 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(2));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(3));
    }

    /**
     * Test configurations() for library module scenario.
     * Library modules might have base and consumer rules.
     */
    @Test
    public void testConfigurations_libraryModuleScenario() {
        // Given: A library module configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding library-specific configurations
        config.configurations(
            "proguard-rules.pro",
            "consumer-rules.pro",
            "library-specific.pro"
        );

        // Then: Should have all library configurations
        assertEquals(3, config.getConfigurations().size(),
            "Library should have 3 configurations");
        assertEquals("proguard-rules.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("consumer-rules.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("library-specific.pro", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test configurations() for flavor-specific scenario.
     * Product flavors might have flavor-specific configurations.
     */
    @Test
    public void testConfigurations_flavorSpecificScenario() {
        // Given: A flavor-specific configuration
        VariantConfiguration config = new VariantConfiguration("freeRelease");

        // When: Adding flavor-specific configurations
        config.defaultConfiguration("proguard-android-optimize.txt");
        config.configurations(
            "proguard-rules.pro",
            "proguard-free.pro",
            "proguard-free-release.pro"
        );

        // Then: Should have all flavor configurations
        assertEquals(4, config.getConfigurations().size(),
            "Flavor variant should have 4 configurations");
        assertEquals("proguard-free.pro", config.getConfigurations().get(2).getFilename(),
            "Should have flavor-specific configuration");
    }

    /**
     * Test configurations() for multi-module project scenario.
     * Multi-module projects might reference configurations from different modules.
     */
    @Test
    public void testConfigurations_multiModuleScenario() {
        // Given: A multi-module project configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding configurations from different modules
        config.configurations(
            "proguard-rules.pro",
            "../common/proguard-common.pro",
            "../library/consumer-rules.pro"
        );

        // Then: Should have all module configurations
        assertEquals(3, config.getConfigurations().size(),
            "Multi-module should have 3 configurations");
        assertTrue(config.getConfigurations().get(1).getFilename().contains("../common"),
            "Should reference common module");
        assertTrue(config.getConfigurations().get(2).getFilename().contains("../library"),
            "Should reference library module");
    }

    // ==================== Multiple Instance Tests ====================

    /**
     * Test that configurations() on different instances are independent.
     * Adding to one instance shouldn't affect others.
     */
    @Test
    public void testConfigurations_independentBetweenInstances() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");

        // When: Adding configurations to each
        config1.configurations("debug1.pro", "debug2.pro");
        config2.configurations("release1.pro");

        // Then: Each should have its own configurations
        assertEquals(2, config1.getConfigurations().size(),
            "First instance should have 2 configurations");
        assertEquals(1, config2.getConfigurations().size(),
            "Second instance should have 1 configuration");
        assertEquals("debug1.pro", config1.getConfigurations().get(0).getFilename());
        assertEquals("release1.pro", config2.getConfigurations().get(0).getFilename());
    }

    // ==================== Consistency Tests ====================

    /**
     * Test that configurations() doesn't affect getName().
     * Adding configurations should not affect other properties.
     */
    @Test
    public void testConfigurations_doesNotAffectName() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        String nameBefore = config.getName();

        // When: Adding configurations
        config.configurations("rules1.pro", "rules2.pro");

        // Then: Name should remain unchanged
        assertEquals(nameBefore, config.getName(),
            "configurations() should not affect getName()");
        assertEquals("debug", config.getName(), "Name should still be 'debug'");
    }

    /**
     * Test that configurations() doesn't affect getConsumerRuleFilter().
     * Adding configurations should not affect consumer rule filter.
     */
    @Test
    public void testConfigurations_doesNotAffectConsumerRuleFilter() {
        // Given: A VariantConfiguration with consumer rule filters
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("*.pro");
        int filterSizeBefore = config.getConsumerRuleFilter().size();

        // When: Adding configurations
        config.configurations("rules1.pro", "rules2.pro");

        // Then: Consumer rule filter should remain unchanged
        assertEquals(filterSizeBefore, config.getConsumerRuleFilter().size(),
            "configurations() should not affect consumer rule filter");
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Consumer rule filter should still have 1 item");
    }

    // ==================== Type Verification Tests ====================

    /**
     * Test that all configurations added are UserProGuardConfiguration.
     * The method should only add user configurations, not default ones.
     */
    @Test
    public void testConfigurations_allAreUserProGuardConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding multiple configurations
        config.configurations("rules1.pro", "rules2.pro", "rules3.pro");

        // Then: All should be UserProGuardConfiguration
        List<ProGuardConfiguration> configs = config.getConfigurations();
        for (ProGuardConfiguration pgConfig : configs) {
            assertInstanceOf(UserProGuardConfiguration.class, pgConfig,
                "All configurations should be UserProGuardConfiguration");
            assertNotNull(pgConfig.getFilename(), "Each should have a filename");
        }
    }

    /**
     * Test that configurations are actually ProGuardConfiguration instances.
     * Verify the type hierarchy is correct.
     */
    @Test
    public void testConfigurations_allAreProGuardConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations
        config.configurations("rules1.pro", "rules2.pro");

        // Then: All should be ProGuardConfiguration instances
        for (ProGuardConfiguration pgConfig : config.getConfigurations()) {
            assertInstanceOf(ProGuardConfiguration.class, pgConfig,
                "All should be ProGuardConfiguration instances");
        }
    }

    // ==================== Empty Configurations List Tests ====================

    /**
     * Test configurations() on initially empty list.
     * Should work correctly when starting with no configurations.
     */
    @Test
    public void testConfigurations_onInitiallyEmptyList() {
        // Given: A newly created VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        assertTrue(config.getConfigurations().isEmpty(), "Should start empty");

        // When: Adding configurations
        config.configurations("rules1.pro", "rules2.pro");

        // Then: Should have configurations
        assertFalse(config.getConfigurations().isEmpty(), "Should no longer be empty");
        assertEquals(2, config.getConfigurations().size(), "Should have 2 configurations");
    }

    // ==================== Integration with setConfigurations ====================

    /**
     * Test configurations() after setConfigurations().
     * Should append to the list set by setConfigurations.
     */
    @Test
    public void testConfigurations_afterSetConfigurations() {
        // Given: A VariantConfiguration with configurations set via setter
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> initialList = new java.util.ArrayList<>();
        initialList.add(new UserProGuardConfiguration("initial.pro"));
        config.setConfigurations(initialList);

        // When: Adding more configurations via configurations()
        config.configurations("additional1.pro", "additional2.pro");

        // Then: Should append to the set list
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations total");
        assertEquals("initial.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("additional1.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("additional2.pro", config.getConfigurations().get(2).getFilename());
    }

    // ==================== Filename Extension Tests ====================

    /**
     * Test configurations() with various file extensions.
     * Different file extensions should all be accepted.
     */
    @Test
    public void testConfigurations_withVariousExtensions() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations with different extensions
        config.configurations(
            "rules.pro",
            "config.txt",
            "settings.cfg",
            "proguard"
        );

        // Then: All extensions should be accepted
        assertEquals(4, config.getConfigurations().size(),
            "Should accept various extensions");
        assertEquals("rules.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("config.txt", config.getConfigurations().get(1).getFilename());
        assertEquals("settings.cfg", config.getConfigurations().get(2).getFilename());
        assertEquals("proguard", config.getConfigurations().get(3).getFilename());
    }

    // ==================== Equivalence with configuration() Tests ====================

    /**
     * Test that configurations() is equivalent to multiple configuration() calls.
     * The result should be the same as calling configuration() multiple times.
     */
    @Test
    public void testConfigurations_equivalentToMultipleConfigurationCalls() {
        // Given: Two identical VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("debug");

        // When: Using configurations() on one and configuration() on the other
        config1.configurations("rules1.pro", "rules2.pro", "rules3.pro");

        config2.configuration("rules1.pro");
        config2.configuration("rules2.pro");
        config2.configuration("rules3.pro");

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
        }
    }
}

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
 * Test class for proguard.gradle.plugin.android.dsl.VariantConfiguration.configuration.(Ljava/lang/String;)V
 *
 * Tests the configuration() method of VariantConfiguration which adds a single user ProGuard configuration.
 * This method takes a configuration file path string and adds it as a UserProGuardConfiguration to the configurations list.
 * It's the singular version of configurations() and is called for each configuration file.
 */
public class VariantConfigurationClaude_configurationTest {

    // ==================== Basic configuration Tests ====================

    /**
     * Test that configuration() adds a single configuration.
     * The most basic use case - adding one configuration.
     */
    @Test
    public void testConfiguration_addsSingleConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding a configuration
        config.configuration("proguard-rules.pro");

        // Then: Should have one configuration
        assertEquals(1, config.getConfigurations().size(),
            "configuration() should add 1 configuration");
        assertEquals("proguard-rules.pro", config.getConfigurations().get(0).getFilename(),
            "Configuration should have correct filename");
    }

    /**
     * Test that configuration() adds UserProGuardConfiguration.
     * The added configuration should be of type UserProGuardConfiguration.
     */
    @Test
    public void testConfiguration_addsUserProGuardConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding a configuration
        config.configuration("rules.pro");

        // Then: Should be UserProGuardConfiguration
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(0),
            "configuration() should add UserProGuardConfiguration");
    }

    /**
     * Test that configuration() with simple filename works.
     * Simple filename without path should work.
     */
    @Test
    public void testConfiguration_withSimpleFilename() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with simple filename
        config.configuration("rules.pro");

        // Then: Should add correctly
        assertEquals(1, config.getConfigurations().size(), "Should have 1 configuration");
        assertEquals("rules.pro", config.getConfigurations().get(0).getFilename());
    }

    /**
     * Test that configuration() with relative path works.
     * Relative path should be preserved.
     */
    @Test
    public void testConfiguration_withRelativePath() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with relative path
        config.configuration("config/proguard-rules.pro");

        // Then: Path should be preserved
        assertEquals(1, config.getConfigurations().size(), "Should have 1 configuration");
        assertEquals("config/proguard-rules.pro", config.getConfigurations().get(0).getFilename(),
            "Relative path should be preserved");
    }

    /**
     * Test that configuration() with absolute path works.
     * Absolute path should be preserved.
     */
    @Test
    public void testConfiguration_withAbsolutePath() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with absolute path
        config.configuration("/etc/proguard/rules.pro");

        // Then: Path should be preserved
        assertEquals(1, config.getConfigurations().size(), "Should have 1 configuration");
        assertEquals("/etc/proguard/rules.pro", config.getConfigurations().get(0).getFilename(),
            "Absolute path should be preserved");
    }

    // ==================== Multiple Calls Tests ====================

    /**
     * Test that multiple calls to configuration() append.
     * Each call should add to the list, not replace it.
     */
    @Test
    public void testConfiguration_multipleCalls_append() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Calling configuration multiple times
        config.configuration("rules1.pro");
        config.configuration("rules2.pro");
        config.configuration("rules3.pro");

        // Then: All should be present
        assertEquals(3, config.getConfigurations().size(),
            "Multiple calls should append");
        assertEquals("rules1.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("rules2.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("rules3.pro", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test that configuration() maintains insertion order.
     * Configurations should appear in the order they were added.
     */
    @Test
    public void testConfiguration_maintainsInsertionOrder() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configurations in specific order
        config.configuration("first.pro");
        config.configuration("second.pro");
        config.configuration("third.pro");

        // Then: Order should be preserved
        assertEquals("first.pro", config.getConfigurations().get(0).getFilename(),
            "First should be first.pro");
        assertEquals("second.pro", config.getConfigurations().get(1).getFilename(),
            "Second should be second.pro");
        assertEquals("third.pro", config.getConfigurations().get(2).getFilename(),
            "Third should be third.pro");
    }

    // ==================== Interaction with Other Methods ====================

    /**
     * Test configuration() with configurations() vararg method.
     * Should work seamlessly together.
     */
    @Test
    public void testConfiguration_withConfigurationsMethod() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Mixing singular and vararg methods
        config.configuration("single1.pro");
        config.configurations("multi1.pro", "multi2.pro");
        config.configuration("single2.pro");

        // Then: All should be present in order
        assertEquals(4, config.getConfigurations().size(),
            "Should have 4 configurations");
        assertEquals("single1.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("multi1.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("multi2.pro", config.getConfigurations().get(2).getFilename());
        assertEquals("single2.pro", config.getConfigurations().get(3).getFilename());
    }

    /**
     * Test configuration() with defaultConfiguration().
     * Should work together, mixing user and default configurations.
     */
    @Test
    public void testConfiguration_withDefaultConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Mixing user and default configurations
        config.configuration("custom1.pro");
        config.defaultConfiguration("proguard-android.txt");
        config.configuration("custom2.pro");

        // Then: Should have all configurations in order
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(0),
            "First should be UserProGuardConfiguration");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(1),
            "Second should be DefaultProGuardConfiguration");
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(2),
            "Third should be UserProGuardConfiguration");
    }

    /**
     * Test configuration() after setConfigurations().
     * Should append to the list set by setConfigurations.
     */
    @Test
    public void testConfiguration_afterSetConfigurations() {
        // Given: A VariantConfiguration with configurations set via setter
        VariantConfiguration config = new VariantConfiguration("debug");
        List<ProGuardConfiguration> initialList = new java.util.ArrayList<>();
        initialList.add(new UserProGuardConfiguration("initial.pro"));
        config.setConfigurations(initialList);

        // When: Adding configuration via configuration()
        config.configuration("additional.pro");

        // Then: Should append to the set list
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("initial.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("additional.pro", config.getConfigurations().get(1).getFilename());
    }

    // ==================== Edge Cases ====================

    /**
     * Test configuration() with empty string.
     * Empty string should be handled.
     */
    @Test
    public void testConfiguration_withEmptyString() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with empty string
        config.configuration("");

        // Then: Should add configuration with empty filename
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("", config.getConfigurations().get(0).getFilename(),
            "Filename should be empty string");
    }

    /**
     * Test configuration() with duplicate filenames.
     * Duplicates should be allowed.
     */
    @Test
    public void testConfiguration_withDuplicates() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding same configuration multiple times
        config.configuration("rules.pro");
        config.configuration("rules.pro");
        config.configuration("rules.pro");

        // Then: All duplicates should be added
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations (including duplicates)");
        assertEquals("rules.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("rules.pro", config.getConfigurations().get(1).getFilename());
        assertEquals("rules.pro", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test configuration() with filename containing spaces.
     * Spaces in filenames should be preserved.
     */
    @Test
    public void testConfiguration_withSpacesInFilename() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with spaces in filename
        config.configuration("proguard rules.pro");

        // Then: Spaces should be preserved
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("proguard rules.pro", config.getConfigurations().get(0).getFilename(),
            "Spaces should be preserved");
    }

    /**
     * Test configuration() with special characters.
     * Special characters should be preserved.
     */
    @Test
    public void testConfiguration_withSpecialCharacters() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with special characters
        config.configuration("rules-v1.0_final.pro");

        // Then: Special characters should be preserved
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("rules-v1.0_final.pro", config.getConfigurations().get(0).getFilename(),
            "Special characters should be preserved");
    }

    /**
     * Test configuration() with Unicode characters.
     * Unicode characters should be handled.
     */
    @Test
    public void testConfiguration_withUnicodeCharacters() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with Unicode characters
        config.configuration("规则.pro");

        // Then: Unicode should be preserved
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("规则.pro", config.getConfigurations().get(0).getFilename(),
            "Unicode should be preserved");
    }

    /**
     * Test configuration() with very long filename.
     * Long filenames should be handled.
     */
    @Test
    public void testConfiguration_withLongFilename() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        String longFilename = "proguard-rules-for-production-release-build-with-all-optimizations-enabled-v2.0.pro";

        // When: Adding configuration with long filename
        config.configuration(longFilename);

        // Then: Long filename should be preserved
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals(longFilename, config.getConfigurations().get(0).getFilename(),
            "Long filename should be preserved");
    }

    /**
     * Test configuration() with Windows-style path.
     * Windows path separators should be preserved.
     */
    @Test
    public void testConfiguration_withWindowsPath() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with Windows path
        config.configuration("C:\\projects\\app\\proguard-rules.pro");

        // Then: Windows path should be preserved
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("C:\\projects\\app\\proguard-rules.pro",
            config.getConfigurations().get(0).getFilename(),
            "Windows path should be preserved");
    }

    /**
     * Test configuration() with Unix-style path.
     * Unix path separators should be preserved.
     */
    @Test
    public void testConfiguration_withUnixPath() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with Unix path
        config.configuration("/home/user/project/proguard-rules.pro");

        // Then: Unix path should be preserved
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("/home/user/project/proguard-rules.pro",
            config.getConfigurations().get(0).getFilename(),
            "Unix path should be preserved");
    }

    // ==================== File Extension Tests ====================

    /**
     * Test configuration() with .pro extension.
     * The standard ProGuard file extension should work.
     */
    @Test
    public void testConfiguration_withProExtension() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with .pro extension
        config.configuration("proguard-rules.pro");

        // Then: Should add correctly
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertTrue(config.getConfigurations().get(0).getFilename().endsWith(".pro"),
            "Should have .pro extension");
    }

    /**
     * Test configuration() with .txt extension.
     * Text file extension should work.
     */
    @Test
    public void testConfiguration_withTxtExtension() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with .txt extension
        config.configuration("proguard-rules.txt");

        // Then: Should add correctly
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertTrue(config.getConfigurations().get(0).getFilename().endsWith(".txt"),
            "Should have .txt extension");
    }

    /**
     * Test configuration() with no extension.
     * Files without extension should work.
     */
    @Test
    public void testConfiguration_withNoExtension() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with no extension
        config.configuration("proguard-rules");

        // Then: Should add correctly
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertFalse(config.getConfigurations().get(0).getFilename().contains("."),
            "Should have no extension");
    }

    /**
     * Test configuration() with custom extension.
     * Custom extensions should work.
     */
    @Test
    public void testConfiguration_withCustomExtension() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with custom extension
        config.configuration("proguard-rules.cfg");

        // Then: Should add correctly
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertTrue(config.getConfigurations().get(0).getFilename().endsWith(".cfg"),
            "Should have .cfg extension");
    }

    // ==================== Real-world Usage Scenarios ====================

    /**
     * Test configuration() for standard Android app scenario.
     * Most Android apps use proguard-rules.pro.
     */
    @Test
    public void testConfiguration_standardAndroidAppScenario() {
        // Given: A standard Android app configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding standard ProGuard rules
        config.defaultConfiguration("proguard-android-optimize.txt");
        config.configuration("proguard-rules.pro");

        // Then: Should have both configurations
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-rules.pro", config.getConfigurations().get(1).getFilename());
    }

    /**
     * Test configuration() for library module scenario.
     * Library modules might have consumer rules.
     */
    @Test
    public void testConfiguration_libraryModuleScenario() {
        // Given: A library module configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding library-specific rules
        config.configuration("proguard-rules.pro");
        config.configuration("consumer-rules.pro");

        // Then: Should have both rule files
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("proguard-rules.pro", config.getConfigurations().get(0).getFilename());
        assertEquals("consumer-rules.pro", config.getConfigurations().get(1).getFilename());
    }

    /**
     * Test configuration() for debug build scenario.
     * Debug builds might have debug-specific rules.
     */
    @Test
    public void testConfiguration_debugBuildScenario() {
        // Given: A debug build configuration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding debug-specific rules
        config.defaultConfiguration("proguard-android-debug.txt");
        config.configuration("proguard-rules.pro");
        config.configuration("proguard-debug.pro");

        // Then: Should have all debug configurations
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertEquals("proguard-debug.pro", config.getConfigurations().get(2).getFilename(),
            "Should have debug-specific rules");
    }

    /**
     * Test configuration() for flavor-specific scenario.
     * Product flavors might have flavor-specific rules.
     */
    @Test
    public void testConfiguration_flavorSpecificScenario() {
        // Given: A flavor-specific configuration
        VariantConfiguration config = new VariantConfiguration("freeRelease");

        // When: Adding flavor-specific rules
        config.configuration("proguard-rules.pro");
        config.configuration("proguard-free.pro");

        // Then: Should have flavor-specific rules
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("proguard-free.pro", config.getConfigurations().get(1).getFilename(),
            "Should have flavor-specific rules");
    }

    /**
     * Test configuration() for multi-module project scenario.
     * Multi-module projects might reference rules from other modules.
     */
    @Test
    public void testConfiguration_multiModuleScenario() {
        // Given: A multi-module project configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding rules from different modules
        config.configuration("proguard-rules.pro");
        config.configuration("../common/proguard-common.pro");

        // Then: Should have rules from multiple modules
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertTrue(config.getConfigurations().get(1).getFilename().contains("../common"),
            "Should reference common module");
    }

    // ==================== Multiple Instance Tests ====================

    /**
     * Test that configuration() on different instances are independent.
     * Adding to one instance shouldn't affect others.
     */
    @Test
    public void testConfiguration_independentBetweenInstances() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");

        // When: Adding configuration to each
        config1.configuration("debug-rules.pro");
        config2.configuration("release-rules.pro");

        // Then: Each should have its own configuration
        assertEquals(1, config1.getConfigurations().size(),
            "First instance should have 1 configuration");
        assertEquals(1, config2.getConfigurations().size(),
            "Second instance should have 1 configuration");
        assertEquals("debug-rules.pro", config1.getConfigurations().get(0).getFilename());
        assertEquals("release-rules.pro", config2.getConfigurations().get(0).getFilename());
    }

    // ==================== Consistency Tests ====================

    /**
     * Test that configuration() doesn't affect getName().
     * Adding configuration should not affect other properties.
     */
    @Test
    public void testConfiguration_doesNotAffectName() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        String nameBefore = config.getName();

        // When: Adding configuration
        config.configuration("rules.pro");

        // Then: Name should remain unchanged
        assertEquals(nameBefore, config.getName(),
            "configuration() should not affect getName()");
        assertEquals("debug", config.getName(), "Name should still be 'debug'");
    }

    /**
     * Test that configuration() doesn't affect getConsumerRuleFilter().
     * Adding configuration should not affect consumer rule filter.
     */
    @Test
    public void testConfiguration_doesNotAffectConsumerRuleFilter() {
        // Given: A VariantConfiguration with consumer rule filter
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("*.pro");
        int filterSizeBefore = config.getConsumerRuleFilter().size();

        // When: Adding configuration
        config.configuration("rules.pro");

        // Then: Consumer rule filter should remain unchanged
        assertEquals(filterSizeBefore, config.getConsumerRuleFilter().size(),
            "configuration() should not affect consumer rule filter");
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Consumer rule filter should still have 1 item");
    }

    // ==================== Type Safety Tests ====================

    /**
     * Test that configuration is actually a ProGuardConfiguration.
     * The added configuration should be a ProGuardConfiguration instance.
     */
    @Test
    public void testConfiguration_isProGuardConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration
        config.configuration("rules.pro");

        // Then: Should be ProGuardConfiguration
        assertInstanceOf(ProGuardConfiguration.class, config.getConfigurations().get(0),
            "Should be ProGuardConfiguration instance");
    }

    /**
     * Test that configuration has correct filename property.
     * The UserProGuardConfiguration should have the correct filename.
     */
    @Test
    public void testConfiguration_hasCorrectFilename() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration
        config.configuration("custom-rules.pro");

        // Then: Should have correct filename
        ProGuardConfiguration pgConfig = config.getConfigurations().get(0);
        assertEquals("custom-rules.pro", pgConfig.getFilename(),
            "Should have correct filename property");
    }

    /**
     * Test that configuration is not a DefaultProGuardConfiguration.
     * Should only create user configurations, not default ones.
     */
    @Test
    public void testConfiguration_notDefaultProGuardConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration
        config.configuration("proguard-android.txt");

        // Then: Should be UserProGuardConfiguration, not Default
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(0),
            "Should be UserProGuardConfiguration");
        assertNotEquals(DefaultProGuardConfiguration.class,
            config.getConfigurations().get(0).getClass(),
            "Should not be DefaultProGuardConfiguration");
    }

    // ==================== Empty List Tests ====================

    /**
     * Test configuration() on initially empty list.
     * Should work correctly when starting with no configurations.
     */
    @Test
    public void testConfiguration_onInitiallyEmptyList() {
        // Given: A newly created VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        assertTrue(config.getConfigurations().isEmpty(), "Should start empty");

        // When: Adding configuration
        config.configuration("rules.pro");

        // Then: Should have configuration
        assertFalse(config.getConfigurations().isEmpty(), "Should no longer be empty");
        assertEquals(1, config.getConfigurations().size(), "Should have 1 configuration");
    }

    // ==================== Return Value Tests ====================

    /**
     * Test that configuration() returns void.
     * The method doesn't return a value (returns void).
     */
    @Test
    public void testConfiguration_returnsVoid() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration (returns void, so just verify no exception)
        config.configuration("rules.pro");

        // Then: Should complete successfully
        assertEquals(1, config.getConfigurations().size(),
            "Should have added configuration successfully");
    }

    // ==================== Null Handling Tests ====================

    /**
     * Test configuration() with filename matching default configuration name.
     * Even if the name matches a default config, should create UserProGuardConfiguration.
     */
    @Test
    public void testConfiguration_withDefaultConfigurationName() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding user configuration with default config name
        config.configuration("proguard-android.txt");

        // Then: Should create UserProGuardConfiguration, not default
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(0),
            "Should be UserProGuardConfiguration even with default name");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
    }

    // ==================== Path Normalization Tests ====================

    /**
     * Test configuration() with paths containing multiple slashes.
     * Multiple slashes should be preserved as-is (no normalization).
     */
    @Test
    public void testConfiguration_withMultipleSlashes() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with multiple slashes
        config.configuration("config//proguard-rules.pro");

        // Then: Should preserve as-is
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("config//proguard-rules.pro", config.getConfigurations().get(0).getFilename(),
            "Should preserve multiple slashes");
    }

    /**
     * Test configuration() with path containing dots.
     * Paths with . and .. should be preserved as-is.
     */
    @Test
    public void testConfiguration_withDotsInPath() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding configuration with dots in path
        config.configuration("./config/../proguard-rules.pro");

        // Then: Should preserve as-is
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("./config/../proguard-rules.pro", config.getConfigurations().get(0).getFilename(),
            "Should preserve dots in path");
    }

    // ==================== Integration Tests ====================

    /**
     * Test configuration() in a complete workflow.
     * A realistic complete workflow should work seamlessly.
     */
    @Test
    public void testConfiguration_completeWorkflow() {
        // Given: A VariantConfiguration for release build
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Building up configurations in a typical workflow
        config.defaultConfiguration("proguard-android-optimize.txt");
        config.configuration("proguard-rules.pro");
        config.configuration("proguard-library.pro");
        config.configuration("proguard-release.pro");

        // Then: Should have all configurations in order
        assertEquals(4, config.getConfigurations().size(),
            "Should have 4 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(2));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(3));
    }

    /**
     * Test configuration() with all property types preserved.
     * The complete configuration state should be consistent.
     */
    @Test
    public void testConfiguration_preservesAllProperties() {
        // Given: A VariantConfiguration with various properties set
        VariantConfiguration config = new VariantConfiguration("debug");
        config.consumerRuleFilter("*.pro");

        // When: Adding configuration
        config.configuration("rules.pro");

        // Then: All properties should be intact
        assertEquals("debug", config.getName(), "Name should be preserved");
        assertEquals(1, config.getConfigurations().size(), "Should have 1 configuration");
        assertEquals(1, config.getConsumerRuleFilter().size(), "Filter should be preserved");
        assertEquals("rules.pro", config.getConfigurations().get(0).getFilename());
    }
}

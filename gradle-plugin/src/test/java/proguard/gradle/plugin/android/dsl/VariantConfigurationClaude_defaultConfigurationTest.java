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
 * Test class for proguard.gradle.plugin.android.dsl.VariantConfiguration.defaultConfiguration.(Ljava/lang/String;)V
 *
 * Tests the defaultConfiguration() method of VariantConfiguration which adds a single default ProGuard configuration.
 * This method takes a default configuration name string and adds it as a DefaultProGuardConfiguration to the configurations list.
 * Default configurations are built-in Android ProGuard configurations like proguard-android.txt, proguard-android-optimize.txt,
 * and proguard-android-debug.txt that are packaged with the Android Gradle Plugin.
 */
public class VariantConfigurationClaude_defaultConfigurationTest {

    // ==================== Basic defaultConfiguration Tests ====================

    /**
     * Test that defaultConfiguration() adds a single configuration.
     * The most basic use case - adding one default configuration.
     */
    @Test
    public void testDefaultConfiguration_addsSingleConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding a default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should have one configuration
        assertEquals(1, config.getConfigurations().size(),
            "defaultConfiguration() should add 1 configuration");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename(),
            "Configuration should have correct filename");
    }

    /**
     * Test that defaultConfiguration() adds DefaultProGuardConfiguration.
     * The added configuration should be of type DefaultProGuardConfiguration.
     */
    @Test
    public void testDefaultConfiguration_addsDefaultProGuardConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding a default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should be DefaultProGuardConfiguration
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0),
            "defaultConfiguration() should add DefaultProGuardConfiguration");
    }

    // ==================== Standard Android Configurations Tests ====================

    /**
     * Test defaultConfiguration() with proguard-android.txt.
     * The standard Android configuration should work.
     */
    @Test
    public void testDefaultConfiguration_withProguardAndroid() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding proguard-android.txt
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should be added correctly
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
    }

    /**
     * Test defaultConfiguration() with proguard-android-optimize.txt.
     * The optimized Android configuration should work.
     */
    @Test
    public void testDefaultConfiguration_withProguardAndroidOptimize() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding proguard-android-optimize.txt
        config.defaultConfiguration("proguard-android-optimize.txt");

        // Then: Should be added correctly
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(0).getFilename());
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
    }

    /**
     * Test defaultConfiguration() with proguard-android-debug.txt.
     * The debug Android configuration should work.
     */
    @Test
    public void testDefaultConfiguration_withProguardAndroidDebug() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding proguard-android-debug.txt
        config.defaultConfiguration("proguard-android-debug.txt");

        // Then: Should be added correctly
        assertEquals(1, config.getConfigurations().size(),
            "Should have 1 configuration");
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(0).getFilename());
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
    }

    // ==================== Multiple Calls Tests ====================

    /**
     * Test that multiple calls to defaultConfiguration() append.
     * Each call should add to the list, not replace it.
     */
    @Test
    public void testDefaultConfiguration_multipleCalls_append() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Calling defaultConfiguration multiple times
        config.defaultConfiguration("proguard-android.txt");
        config.defaultConfiguration("proguard-android-optimize.txt");

        // Then: All should be present
        assertEquals(2, config.getConfigurations().size(),
            "Multiple calls should append");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(1).getFilename());
    }

    /**
     * Test that defaultConfiguration() maintains insertion order.
     * Configurations should appear in the order they were added.
     */
    @Test
    public void testDefaultConfiguration_maintainsInsertionOrder() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding configurations in specific order
        config.defaultConfiguration("proguard-android-debug.txt");
        config.defaultConfiguration("proguard-android.txt");
        config.defaultConfiguration("proguard-android-optimize.txt");

        // Then: Order should be preserved
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(0).getFilename(),
            "First should be proguard-android-debug.txt");
        assertEquals("proguard-android.txt", config.getConfigurations().get(1).getFilename(),
            "Second should be proguard-android.txt");
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(2).getFilename(),
            "Third should be proguard-android-optimize.txt");
    }

    // ==================== Interaction with Other Methods ====================

    /**
     * Test defaultConfiguration() with configuration() method.
     * Should work together, mixing default and user configurations.
     */
    @Test
    public void testDefaultConfiguration_withConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Mixing default and user configurations
        config.defaultConfiguration("proguard-android.txt");
        config.configuration("proguard-rules.pro");
        config.defaultConfiguration("proguard-android-optimize.txt");

        // Then: Should have all configurations in order
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1));
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(2));
    }

    /**
     * Test defaultConfiguration() with configurations() vararg method.
     * Should work with user configurations vararg method.
     */
    @Test
    public void testDefaultConfiguration_withConfigurations() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Mixing default and user configurations
        config.defaultConfiguration("proguard-android.txt");
        config.configurations("rules1.pro", "rules2.pro");

        // Then: Should have all configurations in order
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(2));
    }

    /**
     * Test defaultConfiguration() with defaultConfigurations() vararg method.
     * Should work with its vararg equivalent.
     */
    @Test
    public void testDefaultConfiguration_withDefaultConfigurations() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Mixing singular and vararg methods
        config.defaultConfiguration("proguard-android.txt");
        config.defaultConfigurations("proguard-android-optimize.txt", "proguard-android-debug.txt");

        // Then: Should have all configurations in order
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(1).getFilename());
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(2).getFilename());
    }

    /**
     * Test defaultConfiguration() after setConfigurations().
     * Should append to the list set by setConfigurations.
     */
    @Test
    public void testDefaultConfiguration_afterSetConfigurations() {
        // Given: A VariantConfiguration with configurations set via setter
        VariantConfiguration config = new VariantConfiguration("release");
        List<ProGuardConfiguration> initialList = new java.util.ArrayList<>();
        initialList.add(new UserProGuardConfiguration("initial.pro"));
        config.setConfigurations(initialList);

        // When: Adding default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should append to the set list
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(1));
    }

    // ==================== Edge Cases ====================

    /**
     * Test defaultConfiguration() with same configuration multiple times.
     * Duplicates should be allowed.
     */
    @Test
    public void testDefaultConfiguration_withDuplicates() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding same default configuration multiple times
        config.defaultConfiguration("proguard-android.txt");
        config.defaultConfiguration("proguard-android.txt");
        config.defaultConfiguration("proguard-android.txt");

        // Then: All duplicates should be added
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations (including duplicates)");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android.txt", config.getConfigurations().get(1).getFilename());
        assertEquals("proguard-android.txt", config.getConfigurations().get(2).getFilename());
    }

    // ==================== Invalid Configuration Name Tests ====================

    /**
     * Test defaultConfiguration() with invalid configuration name.
     * Invalid names should throw an exception.
     */
    @Test
    public void testDefaultConfiguration_withInvalidName_throwsException() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When/Then: Adding invalid default configuration should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            config.defaultConfiguration("invalid-config.txt");
        }, "Invalid default configuration name should throw IllegalArgumentException");
    }

    /**
     * Test defaultConfiguration() with empty string.
     * Empty string should throw an exception.
     */
    @Test
    public void testDefaultConfiguration_withEmptyString_throwsException() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When/Then: Adding empty string should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            config.defaultConfiguration("");
        }, "Empty string should throw IllegalArgumentException");
    }

    /**
     * Test defaultConfiguration() with user configuration file name.
     * User configuration names should throw exception when used as default.
     */
    @Test
    public void testDefaultConfiguration_withUserConfigName_throwsException() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When/Then: Using user config name should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            config.defaultConfiguration("proguard-rules.pro");
        }, "User configuration name should throw IllegalArgumentException");
    }

    /**
     * Test defaultConfiguration() with partial match of valid name.
     * Partial matches should throw exception.
     */
    @Test
    public void testDefaultConfiguration_withPartialMatch_throwsException() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When/Then: Partial match should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            config.defaultConfiguration("proguard-android");
        }, "Partial match should throw IllegalArgumentException");
    }

    /**
     * Test defaultConfiguration() with case variation.
     * Case must match exactly.
     */
    @Test
    public void testDefaultConfiguration_withCaseVariation_throwsException() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When/Then: Case variation should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            config.defaultConfiguration("proguard-Android.txt");
        }, "Case variation should throw IllegalArgumentException");
    }

    // ==================== Path and Filename Tests ====================

    /**
     * Test that defaultConfiguration() stores correct filename.
     * Default configurations should store just the filename.
     */
    @Test
    public void testDefaultConfiguration_storesFilename() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should store filename
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename(),
            "Should store filename");
    }

    /**
     * Test that defaultConfiguration() has correct path property.
     * Default configurations should have /lib/ prefix in path.
     */
    @Test
    public void testDefaultConfiguration_hasCorrectPath() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should have correct path
        ProGuardConfiguration pgConfig = config.getConfigurations().get(0);
        assertEquals("/lib/proguard-android.txt", pgConfig.getPath(),
            "Default configuration should have /lib/ prefix in path");
    }

    /**
     * Test that all standard configurations have correct paths.
     * All three standard configurations should have /lib/ prefix.
     */
    @Test
    public void testDefaultConfiguration_allStandardConfigsHaveCorrectPath() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding all three standard configurations
        config.defaultConfiguration("proguard-android.txt");
        config.defaultConfiguration("proguard-android-optimize.txt");
        config.defaultConfiguration("proguard-android-debug.txt");

        // Then: All should have correct paths
        assertEquals("/lib/proguard-android.txt", config.getConfigurations().get(0).getPath());
        assertEquals("/lib/proguard-android-optimize.txt", config.getConfigurations().get(1).getPath());
        assertEquals("/lib/proguard-android-debug.txt", config.getConfigurations().get(2).getPath());
    }

    // ==================== Real-world Usage Scenarios ====================

    /**
     * Test defaultConfiguration() for standard Android app scenario.
     * Most Android apps use proguard-android.txt for release builds.
     */
    @Test
    public void testDefaultConfiguration_standardAndroidAppScenario() {
        // Given: A standard Android app configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding standard ProGuard configuration
        config.defaultConfiguration("proguard-android.txt");
        config.configuration("proguard-rules.pro");

        // Then: Should have both configurations
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-rules.pro", config.getConfigurations().get(1).getFilename());
    }

    /**
     * Test defaultConfiguration() for optimized release build scenario.
     * Optimized builds use proguard-android-optimize.txt.
     */
    @Test
    public void testDefaultConfiguration_optimizedReleaseBuildScenario() {
        // Given: An optimized release build configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding optimized ProGuard configuration
        config.defaultConfiguration("proguard-android-optimize.txt");
        config.configuration("proguard-rules.pro");

        // Then: Should have optimized configuration
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("proguard-android-optimize.txt", config.getConfigurations().get(0).getFilename());
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
    }

    /**
     * Test defaultConfiguration() for debug build scenario.
     * Debug builds use proguard-android-debug.txt.
     */
    @Test
    public void testDefaultConfiguration_debugBuildScenario() {
        // Given: A debug build configuration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding debug ProGuard configuration
        config.defaultConfiguration("proguard-android-debug.txt");
        config.configuration("proguard-rules.pro");

        // Then: Should have debug configuration
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("proguard-android-debug.txt", config.getConfigurations().get(0).getFilename());
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
    }

    /**
     * Test defaultConfiguration() for library module scenario.
     * Libraries might still use default configurations.
     */
    @Test
    public void testDefaultConfiguration_libraryModuleScenario() {
        // Given: A library module configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding default and consumer rules
        config.defaultConfiguration("proguard-android.txt");
        config.configuration("consumer-rules.pro");

        // Then: Should have both configurations
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("consumer-rules.pro", config.getConfigurations().get(1).getFilename());
    }

    // ==================== Multiple Instance Tests ====================

    /**
     * Test that defaultConfiguration() on different instances are independent.
     * Adding to one instance shouldn't affect others.
     */
    @Test
    public void testDefaultConfiguration_independentBetweenInstances() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");

        // When: Adding default configuration to each
        config1.defaultConfiguration("proguard-android-debug.txt");
        config2.defaultConfiguration("proguard-android-optimize.txt");

        // Then: Each should have its own configuration
        assertEquals(1, config1.getConfigurations().size(),
            "First instance should have 1 configuration");
        assertEquals(1, config2.getConfigurations().size(),
            "Second instance should have 1 configuration");
        assertEquals("proguard-android-debug.txt", config1.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android-optimize.txt", config2.getConfigurations().get(0).getFilename());
    }

    // ==================== Consistency Tests ====================

    /**
     * Test that defaultConfiguration() doesn't affect getName().
     * Adding configuration should not affect other properties.
     */
    @Test
    public void testDefaultConfiguration_doesNotAffectName() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");
        String nameBefore = config.getName();

        // When: Adding default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Name should remain unchanged
        assertEquals(nameBefore, config.getName(),
            "defaultConfiguration() should not affect getName()");
        assertEquals("release", config.getName(), "Name should still be 'release'");
    }

    /**
     * Test that defaultConfiguration() doesn't affect getConsumerRuleFilter().
     * Adding configuration should not affect consumer rule filter.
     */
    @Test
    public void testDefaultConfiguration_doesNotAffectConsumerRuleFilter() {
        // Given: A VariantConfiguration with consumer rule filter
        VariantConfiguration config = new VariantConfiguration("release");
        config.consumerRuleFilter("*.pro");
        int filterSizeBefore = config.getConsumerRuleFilter().size();

        // When: Adding default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Consumer rule filter should remain unchanged
        assertEquals(filterSizeBefore, config.getConsumerRuleFilter().size(),
            "defaultConfiguration() should not affect consumer rule filter");
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Consumer rule filter should still have 1 item");
    }

    // ==================== Type Safety Tests ====================

    /**
     * Test that configuration is actually a ProGuardConfiguration.
     * The added configuration should be a ProGuardConfiguration instance.
     */
    @Test
    public void testDefaultConfiguration_isProGuardConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should be ProGuardConfiguration
        assertInstanceOf(ProGuardConfiguration.class, config.getConfigurations().get(0),
            "Should be ProGuardConfiguration instance");
    }

    /**
     * Test that configuration is not a UserProGuardConfiguration.
     * Should only create default configurations, not user ones.
     */
    @Test
    public void testDefaultConfiguration_notUserProGuardConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should be DefaultProGuardConfiguration, not User
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0),
            "Should be DefaultProGuardConfiguration");
        assertNotEquals(UserProGuardConfiguration.class,
            config.getConfigurations().get(0).getClass(),
            "Should not be UserProGuardConfiguration");
    }

    // ==================== Empty List Tests ====================

    /**
     * Test defaultConfiguration() on initially empty list.
     * Should work correctly when starting with no configurations.
     */
    @Test
    public void testDefaultConfiguration_onInitiallyEmptyList() {
        // Given: A newly created VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");
        assertTrue(config.getConfigurations().isEmpty(), "Should start empty");

        // When: Adding default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should have configuration
        assertFalse(config.getConfigurations().isEmpty(), "Should no longer be empty");
        assertEquals(1, config.getConfigurations().size(), "Should have 1 configuration");
    }

    // ==================== Return Value Tests ====================

    /**
     * Test that defaultConfiguration() returns void.
     * The method doesn't return a value (returns void).
     */
    @Test
    public void testDefaultConfiguration_returnsVoid() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding configuration (returns void, so just verify no exception)
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should complete successfully
        assertEquals(1, config.getConfigurations().size(),
            "Should have added configuration successfully");
    }

    // ==================== Comparison with User Configuration Tests ====================

    /**
     * Test difference between defaultConfiguration() and configuration().
     * They should create different types of configurations.
     */
    @Test
    public void testDefaultConfiguration_differencFromConfiguration() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding one of each type
        config.defaultConfiguration("proguard-android.txt");
        config.configuration("proguard-android.txt");

        // Then: Should be different types even with same name
        assertEquals(2, config.getConfigurations().size(),
            "Should have 2 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0),
            "First should be DefaultProGuardConfiguration");
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1),
            "Second should be UserProGuardConfiguration");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
        assertEquals("proguard-android.txt", config.getConfigurations().get(1).getFilename());
        assertNotEquals(config.getConfigurations().get(0).getClass(),
            config.getConfigurations().get(1).getClass(),
            "Classes should be different");
    }

    /**
     * Test that defaultConfiguration() enforces valid names.
     * Unlike configuration(), defaultConfiguration() validates the name.
     */
    @Test
    public void testDefaultConfiguration_enforcesValidNames() {
        // Given: A VariantConfiguration
        VariantConfiguration config1 = new VariantConfiguration("release");
        VariantConfiguration config2 = new VariantConfiguration("release");

        // When: configuration() accepts any name, defaultConfiguration() doesn't
        config1.configuration("any-name.txt"); // This works

        // Then: defaultConfiguration() should throw for invalid names
        assertThrows(IllegalArgumentException.class, () -> {
            config2.defaultConfiguration("any-name.txt"); // This throws
        }, "defaultConfiguration() should validate names");
    }

    // ==================== Integration Tests ====================

    /**
     * Test defaultConfiguration() in a complete workflow.
     * A realistic complete workflow should work seamlessly.
     */
    @Test
    public void testDefaultConfiguration_completeWorkflow() {
        // Given: A VariantConfiguration for release build
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Building up configurations in a typical workflow
        config.defaultConfiguration("proguard-android-optimize.txt");
        config.configuration("proguard-rules.pro");
        config.configuration("proguard-library.pro");

        // Then: Should have all configurations in order
        assertEquals(3, config.getConfigurations().size(),
            "Should have 3 configurations");
        assertInstanceOf(DefaultProGuardConfiguration.class, config.getConfigurations().get(0));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(1));
        assertInstanceOf(UserProGuardConfiguration.class, config.getConfigurations().get(2));
    }

    /**
     * Test defaultConfiguration() with all properties preserved.
     * The complete configuration state should be consistent.
     */
    @Test
    public void testDefaultConfiguration_preservesAllProperties() {
        // Given: A VariantConfiguration with various properties set
        VariantConfiguration config = new VariantConfiguration("release");
        config.consumerRuleFilter("*.pro");

        // When: Adding default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: All properties should be intact
        assertEquals("release", config.getName(), "Name should be preserved");
        assertEquals(1, config.getConfigurations().size(), "Should have 1 configuration");
        assertEquals(1, config.getConsumerRuleFilter().size(), "Filter should be preserved");
        assertEquals("proguard-android.txt", config.getConfigurations().get(0).getFilename());
    }

    // ==================== fromString() Delegation Tests ====================

    /**
     * Test that defaultConfiguration() delegates to fromString().
     * The method should use DefaultProGuardConfiguration.fromString().
     */
    @Test
    public void testDefaultConfiguration_delegatesToFromString() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should have used fromString() (which validates the name)
        ProGuardConfiguration pgConfig = config.getConfigurations().get(0);
        assertInstanceOf(DefaultProGuardConfiguration.class, pgConfig);
        assertEquals("proguard-android.txt", pgConfig.getFilename());
        assertEquals("/lib/proguard-android.txt", pgConfig.getPath());
    }
}

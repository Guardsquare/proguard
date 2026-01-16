/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for proguard.gradle.plugin.android.dsl.VariantConfiguration.<init>.(Ljava/lang/String;)V
 *
 * Tests the constructor of VariantConfiguration which takes a String name parameter.
 * The constructor initializes the variant configuration with the given name and
 * creates empty mutable lists for configurations and consumerRuleFilter.
 */
public class VariantConfigurationClaude_constructorTest {

    // ==================== Basic Constructor Tests ====================

    /**
     * Test that constructor accepts a simple variant name.
     * The most common use case is creating a VariantConfiguration with a standard name.
     */
    @Test
    public void testConstructor_simpleVariantName() {
        // Given: A simple variant name
        String variantName = "debug";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should create a valid instance with the given name
        assertNotNull(config, "Constructor should create a non-null instance");
        assertEquals(variantName, config.getName(), "Constructor should set the name property");
    }

    /**
     * Test that constructor accepts release variant name.
     * Release is another common variant type.
     */
    @Test
    public void testConstructor_releaseVariantName() {
        // Given: A release variant name
        String variantName = "release";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should create instance with release name
        assertNotNull(config, "Constructor should create a non-null instance");
        assertEquals("release", config.getName(), "Constructor should set name to 'release'");
    }

    /**
     * Test that constructor initializes configurations list as empty.
     * The configurations list should start empty and be ready for additions.
     */
    @Test
    public void testConstructor_initializesConfigurationsListEmpty() {
        // Given: A variant name
        String variantName = "debug";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: configurations list should be empty but not null
        assertNotNull(config.getConfigurations(),
            "Constructor should initialize configurations list");
        assertTrue(config.getConfigurations().isEmpty(),
            "Constructor should initialize configurations list as empty");
        assertEquals(0, config.getConfigurations().size(),
            "Constructor should create empty configurations list");
    }

    /**
     * Test that constructor initializes consumerRuleFilter list as empty.
     * The consumerRuleFilter list should start empty and be ready for additions.
     */
    @Test
    public void testConstructor_initializesConsumerRuleFilterEmpty() {
        // Given: A variant name
        String variantName = "debug";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: consumerRuleFilter list should be empty but not null
        assertNotNull(config.getConsumerRuleFilter(),
            "Constructor should initialize consumerRuleFilter list");
        assertTrue(config.getConsumerRuleFilter().isEmpty(),
            "Constructor should initialize consumerRuleFilter list as empty");
        assertEquals(0, config.getConsumerRuleFilter().size(),
            "Constructor should create empty consumerRuleFilter list");
    }

    /**
     * Test that constructor creates mutable configurations list.
     * The configurations list should be modifiable after construction.
     */
    @Test
    public void testConstructor_configurationsMutable() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding a configuration
        config.configuration("proguard-rules.pro");

        // Then: Should be able to modify the list
        assertEquals(1, config.getConfigurations().size(),
            "Configurations list should be mutable");
    }

    /**
     * Test that constructor creates mutable consumerRuleFilter list.
     * The consumerRuleFilter list should be modifiable after construction.
     */
    @Test
    public void testConstructor_consumerRuleFilterMutable() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding a filter
        config.consumerRuleFilter("*.pro");

        // Then: Should be able to modify the list
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "ConsumerRuleFilter list should be mutable");
    }

    // ==================== Variant Name Tests ====================

    /**
     * Test constructor with compound variant name.
     * Android variants can have compound names like debugUnitTest.
     */
    @Test
    public void testConstructor_compoundVariantName() {
        // Given: A compound variant name
        String variantName = "debugUnitTest";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should handle compound names
        assertEquals("debugUnitTest", config.getName(),
            "Constructor should handle compound variant names");
    }

    /**
     * Test constructor with flavor variant name.
     * Product flavors create variant names like freeDebug or proRelease.
     */
    @Test
    public void testConstructor_flavorVariantName() {
        // Given: A flavor-based variant name
        String variantName = "freeDebug";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should handle flavor variant names
        assertEquals("freeDebug", config.getName(),
            "Constructor should handle flavor-based variant names");
    }

    /**
     * Test constructor with multi-flavor variant name.
     * Multiple dimensions create names like googleProRelease.
     */
    @Test
    public void testConstructor_multiFlavorVariantName() {
        // Given: A multi-flavor variant name
        String variantName = "googleProRelease";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should handle multi-flavor variant names
        assertEquals("googleProRelease", config.getName(),
            "Constructor should handle multi-flavor variant names");
    }

    /**
     * Test constructor with camelCase variant name.
     * Android variant names typically use camelCase.
     */
    @Test
    public void testConstructor_camelCaseVariantName() {
        // Given: A camelCase variant name
        String variantName = "stagingRelease";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should preserve camelCase
        assertEquals("stagingRelease", config.getName(),
            "Constructor should preserve camelCase in variant names");
    }

    /**
     * Test constructor with uppercase variant name.
     * Some projects might use uppercase naming conventions.
     */
    @Test
    public void testConstructor_uppercaseVariantName() {
        // Given: An uppercase variant name
        String variantName = "RELEASE";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should preserve uppercase
        assertEquals("RELEASE", config.getName(),
            "Constructor should preserve uppercase in variant names");
    }

    /**
     * Test constructor with lowercase variant name.
     * Lowercase is the typical convention for Android variants.
     */
    @Test
    public void testConstructor_lowercaseVariantName() {
        // Given: A lowercase variant name
        String variantName = "debug";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should preserve lowercase
        assertEquals("debug", config.getName(),
            "Constructor should preserve lowercase in variant names");
    }

    // ==================== Edge Cases ====================

    /**
     * Test constructor with empty string name.
     * Edge case: empty variant name should be handled.
     */
    @Test
    public void testConstructor_emptyVariantName() {
        // Given: An empty variant name
        String variantName = "";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should accept empty string
        assertNotNull(config, "Constructor should accept empty string");
        assertEquals("", config.getName(), "Constructor should preserve empty string name");
        assertNotNull(config.getConfigurations(), "Should still initialize configurations");
        assertNotNull(config.getConsumerRuleFilter(), "Should still initialize consumerRuleFilter");
    }

    /**
     * Test constructor with variant name containing spaces.
     * Edge case: spaces in variant names should be preserved.
     */
    @Test
    public void testConstructor_variantNameWithSpaces() {
        // Given: A variant name with spaces
        String variantName = "debug test";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should preserve spaces
        assertEquals("debug test", config.getName(),
            "Constructor should preserve spaces in variant names");
    }

    /**
     * Test constructor with variant name containing special characters.
     * Edge case: special characters should be preserved.
     */
    @Test
    public void testConstructor_variantNameWithSpecialCharacters() {
        // Given: A variant name with special characters
        String variantName = "debug-v1.0_test";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should preserve special characters
        assertEquals("debug-v1.0_test", config.getName(),
            "Constructor should preserve special characters in variant names");
    }

    /**
     * Test constructor with long variant name.
     * Edge case: long variant names should be handled.
     */
    @Test
    public void testConstructor_longVariantName() {
        // Given: A very long variant name
        String variantName = "googlePlayStoreFreeDebugAndroidTestCoverageReport";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should handle long names
        assertEquals(variantName, config.getName(),
            "Constructor should handle long variant names");
    }

    /**
     * Test constructor with variant name containing numbers.
     * Variant names might contain version numbers.
     */
    @Test
    public void testConstructor_variantNameWithNumbers() {
        // Given: A variant name with numbers
        String variantName = "v2Release";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should preserve numbers
        assertEquals("v2Release", config.getName(),
            "Constructor should handle numbers in variant names");
    }

    /**
     * Test constructor with variant name containing Unicode characters.
     * Edge case: Unicode characters should be preserved.
     */
    @Test
    public void testConstructor_variantNameWithUnicode() {
        // Given: A variant name with Unicode characters
        String variantName = "debug测试";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: Should preserve Unicode
        assertEquals("debug测试", config.getName(),
            "Constructor should handle Unicode characters in variant names");
    }

    // ==================== Property Immutability Tests ====================

    /**
     * Test that name property is immutable after construction.
     * The name should be set via constructor and remain constant.
     */
    @Test
    public void testConstructor_nameIsImmutable() {
        // Given: A VariantConfiguration
        String originalName = "debug";
        VariantConfiguration config = new VariantConfiguration(originalName);

        // When: Getting the name multiple times
        String name1 = config.getName();
        String name2 = config.getName();

        // Then: Name should be consistent
        assertEquals(originalName, name1, "Name should match constructor parameter");
        assertEquals(name1, name2, "Name should be consistent across multiple calls");
    }

    // ==================== Multiple Instance Tests ====================

    /**
     * Test creating multiple VariantConfiguration instances.
     * Each instance should be independent.
     */
    @Test
    public void testConstructor_multipleInstances() {
        // Given: Multiple variant names
        String name1 = "debug";
        String name2 = "release";

        // When: Creating multiple VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration(name1);
        VariantConfiguration config2 = new VariantConfiguration(name2);

        // Then: Each instance should be independent
        assertNotSame(config1, config2, "Should create different instances");
        assertEquals("debug", config1.getName(), "First instance should have debug name");
        assertEquals("release", config2.getName(), "Second instance should have release name");
    }

    /**
     * Test that instances with same name are different objects.
     * Same name doesn't mean same instance.
     */
    @Test
    public void testConstructor_sameNameDifferentInstances() {
        // Given: Same variant name used twice
        String name = "debug";

        // When: Creating two VariantConfigurations with same name
        VariantConfiguration config1 = new VariantConfiguration(name);
        VariantConfiguration config2 = new VariantConfiguration(name);

        // Then: Should create different instances
        assertNotSame(config1, config2,
            "Constructor should create new instances even with same name");
        assertEquals(config1.getName(), config2.getName(),
            "Both instances should have same name");
    }

    /**
     * Test that instances have independent configurations lists.
     * Modifying one instance's list shouldn't affect another.
     */
    @Test
    public void testConstructor_independentConfigurationsLists() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");

        // When: Adding configuration to first instance
        config1.configuration("proguard-rules.pro");

        // Then: Second instance should remain unaffected
        assertEquals(1, config1.getConfigurations().size(),
            "First instance should have one configuration");
        assertEquals(0, config2.getConfigurations().size(),
            "Second instance should have empty configurations");
    }

    /**
     * Test that instances have independent consumerRuleFilter lists.
     * Modifying one instance's list shouldn't affect another.
     */
    @Test
    public void testConstructor_independentConsumerRuleFilterLists() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");

        // When: Adding filter to first instance
        config1.consumerRuleFilter("*.pro");

        // Then: Second instance should remain unaffected
        assertEquals(1, config1.getConsumerRuleFilter().size(),
            "First instance should have one filter");
        assertEquals(0, config2.getConsumerRuleFilter().size(),
            "Second instance should have empty filters");
    }

    // ==================== Common Android Variant Names ====================

    /**
     * Test constructor with all common Android variant types.
     * Ensure constructor works with typical Android build variant names.
     */
    @Test
    public void testConstructor_commonAndroidVariants() {
        // Given: Common Android variant names
        String[] variants = {"debug", "release", "staging", "production",
                            "debugUnitTest", "releaseUnitTest"};

        // When: Creating VariantConfigurations for each
        for (String variantName : variants) {
            VariantConfiguration config = new VariantConfiguration(variantName);

            // Then: Each should be created successfully
            assertNotNull(config, "Constructor should handle variant: " + variantName);
            assertEquals(variantName, config.getName(),
                "Constructor should set correct name for: " + variantName);
            assertNotNull(config.getConfigurations(),
                "Constructor should initialize configurations for: " + variantName);
            assertNotNull(config.getConsumerRuleFilter(),
                "Constructor should initialize consumerRuleFilter for: " + variantName);
        }
    }

    // ==================== Integration with Other Methods ====================

    /**
     * Test that constructor prepares instance for configuration method.
     * After construction, should be able to add configurations.
     */
    @Test
    public void testConstructor_readyForConfigurationMethod() {
        // Given: A newly constructed VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding a configuration
        config.configuration("proguard-rules.pro");

        // Then: Should work correctly
        assertEquals(1, config.getConfigurations().size(),
            "Constructor should prepare instance for configuration() calls");
        assertTrue(config.getConfigurations().get(0) instanceof UserProGuardConfiguration,
            "Should contain UserProGuardConfiguration");
    }

    /**
     * Test that constructor prepares instance for configurations method.
     * After construction, should be able to add multiple configurations at once.
     */
    @Test
    public void testConstructor_readyForConfigurationsMethod() {
        // Given: A newly constructed VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding multiple configurations
        config.configurations("rules1.pro", "rules2.pro");

        // Then: Should work correctly
        assertEquals(2, config.getConfigurations().size(),
            "Constructor should prepare instance for configurations() calls");
    }

    /**
     * Test that constructor prepares instance for defaultConfiguration method.
     * After construction, should be able to add default configurations.
     */
    @Test
    public void testConstructor_readyForDefaultConfigurationMethod() {
        // Given: A newly constructed VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Adding a default configuration
        config.defaultConfiguration("proguard-android.txt");

        // Then: Should work correctly
        assertEquals(1, config.getConfigurations().size(),
            "Constructor should prepare instance for defaultConfiguration() calls");
        assertTrue(config.getConfigurations().get(0) instanceof DefaultProGuardConfiguration,
            "Should contain DefaultProGuardConfiguration");
    }

    /**
     * Test that constructor prepares instance for consumerRuleFilter method.
     * After construction, should be able to add consumer rule filters.
     */
    @Test
    public void testConstructor_readyForConsumerRuleFilterMethod() {
        // Given: A newly constructed VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Adding consumer rule filters
        config.consumerRuleFilter("*.pro", "*.txt");

        // Then: Should work correctly
        assertEquals(2, config.getConsumerRuleFilter().size(),
            "Constructor should prepare instance for consumerRuleFilter() calls");
        assertTrue(config.getConsumerRuleFilter().contains("*.pro"),
            "Should contain first filter");
        assertTrue(config.getConsumerRuleFilter().contains("*.txt"),
            "Should contain second filter");
    }

    // ==================== Type Tests ====================

    /**
     * Test that constructor returns correct type.
     * Should return VariantConfiguration instance.
     */
    @Test
    public void testConstructor_returnsCorrectType() {
        // Given: A variant name
        String variantName = "debug";

        // When: Creating a VariantConfiguration
        Object config = new VariantConfiguration(variantName);

        // Then: Should be of correct type
        assertInstanceOf(VariantConfiguration.class, config,
            "Constructor should return VariantConfiguration instance");
    }

    /**
     * Test that getName returns String type.
     * The name property should be a String.
     */
    @Test
    public void testConstructor_nameIsString() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the name
        Object name = config.getName();

        // Then: Should be a String
        assertInstanceOf(String.class, name,
            "getName() should return String type");
    }

    // ==================== Real-world Usage Scenarios ====================

    /**
     * Test constructor for debug build type scenario.
     * Debug builds are commonly configured with different ProGuard rules.
     */
    @Test
    public void testConstructor_debugBuildTypeScenario() {
        // Given: A debug build type configuration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Setting up debug-specific configuration
        config.defaultConfiguration("proguard-android-debug.txt");

        // Then: Should be properly initialized and ready
        assertEquals("debug", config.getName(), "Should have debug name");
        assertEquals(1, config.getConfigurations().size(),
            "Should have debug configuration");
    }

    /**
     * Test constructor for release build type scenario.
     * Release builds typically use different ProGuard configurations.
     */
    @Test
    public void testConstructor_releaseBuildTypeScenario() {
        // Given: A release build type configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Setting up release-specific configuration
        config.defaultConfiguration("proguard-android-optimize.txt");
        config.configuration("proguard-rules.pro");

        // Then: Should be properly initialized and ready
        assertEquals("release", config.getName(), "Should have release name");
        assertEquals(2, config.getConfigurations().size(),
            "Should have both default and custom configurations");
    }

    /**
     * Test constructor for product flavor scenario.
     * Product flavors create variant-specific configurations.
     */
    @Test
    public void testConstructor_productFlavorScenario() {
        // Given: A product flavor variant
        VariantConfiguration config = new VariantConfiguration("freeRelease");

        // When: Setting up flavor-specific configuration
        config.configuration("free-flavor-rules.pro");
        config.consumerRuleFilter("*.pro");

        // Then: Should handle flavor configuration
        assertEquals("freeRelease", config.getName(),
            "Should have flavor-specific name");
        assertEquals(1, config.getConfigurations().size(),
            "Should have flavor configuration");
        assertEquals(1, config.getConsumerRuleFilter().size(),
            "Should have consumer rule filter");
    }

    // ==================== State After Construction Tests ====================

    /**
     * Test complete state of object immediately after construction.
     * Verify all properties are correctly initialized.
     */
    @Test
    public void testConstructor_completeInitialState() {
        // Given: A variant name
        String variantName = "debug";

        // When: Creating a VariantConfiguration
        VariantConfiguration config = new VariantConfiguration(variantName);

        // Then: All properties should be properly initialized
        assertNotNull(config, "Instance should not be null");
        assertEquals("debug", config.getName(), "Name should be set to 'debug'");
        assertNotNull(config.getConfigurations(), "Configurations list should not be null");
        assertNotNull(config.getConsumerRuleFilter(), "ConsumerRuleFilter list should not be null");
        assertTrue(config.getConfigurations().isEmpty(), "Configurations list should be empty");
        assertTrue(config.getConsumerRuleFilter().isEmpty(), "ConsumerRuleFilter list should be empty");
        assertEquals(0, config.getConfigurations().size(), "Configurations size should be 0");
        assertEquals(0, config.getConsumerRuleFilter().size(), "ConsumerRuleFilter size should be 0");
    }
}

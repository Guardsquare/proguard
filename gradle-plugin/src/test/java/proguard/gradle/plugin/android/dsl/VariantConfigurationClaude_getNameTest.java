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
 * Test class for proguard.gradle.plugin.android.dsl.VariantConfiguration.getName.()Ljava/lang/String;
 *
 * Tests the getName() method of VariantConfiguration which returns the variant name.
 * The name is set during construction and is immutable - it cannot be changed after creation.
 * This method is a getter for the 'val name: String' property in Kotlin.
 */
public class VariantConfigurationClaude_getNameTest {

    // ==================== Basic getName Tests ====================

    /**
     * Test that getName returns the name set in constructor.
     * The most basic use case - getting the variant name that was set during construction.
     */
    @Test
    public void testGetName_returnsConstructorName() {
        // Given: A VariantConfiguration with a specific name
        String variantName = "debug";
        VariantConfiguration config = new VariantConfiguration(variantName);

        // When: Getting the name
        String result = config.getName();

        // Then: Should return the name from constructor
        assertEquals("debug", result, "getName() should return the name from constructor");
    }

    /**
     * Test that getName returns correct name for release variant.
     * Release is another common variant type.
     */
    @Test
    public void testGetName_releaseVariant() {
        // Given: A release variant configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Getting the name
        String result = config.getName();

        // Then: Should return "release"
        assertEquals("release", result, "getName() should return 'release'");
    }

    /**
     * Test that getName returns exact string passed to constructor.
     * getName should not modify or transform the name in any way.
     */
    @Test
    public void testGetName_exactMatch() {
        // Given: A VariantConfiguration with a specific name
        String originalName = "myCustomVariant";
        VariantConfiguration config = new VariantConfiguration(originalName);

        // When: Getting the name
        String result = config.getName();

        // Then: Should be exactly the same as the original
        assertSame(originalName, result, "getName() should return the exact same string instance");
    }

    /**
     * Test that getName is consistent across multiple calls.
     * Multiple calls to getName should always return the same value.
     */
    @Test
    public void testGetName_consistentAcrossMultipleCalls() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Calling getName multiple times
        String result1 = config.getName();
        String result2 = config.getName();
        String result3 = config.getName();

        // Then: All results should be equal
        assertEquals(result1, result2, "getName() should return consistent results");
        assertEquals(result2, result3, "getName() should return consistent results");
        assertEquals("debug", result1);
        assertEquals("debug", result2);
        assertEquals("debug", result3);
    }

    /**
     * Test that getName returns non-null value.
     * The name should never be null.
     */
    @Test
    public void testGetName_notNull() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the name
        String result = config.getName();

        // Then: Should not be null
        assertNotNull(result, "getName() should never return null");
    }

    // ==================== Different Variant Name Formats ====================

    /**
     * Test getName with compound variant name.
     * Android variants can have compound names like debugUnitTest.
     */
    @Test
    public void testGetName_compoundVariantName() {
        // Given: A compound variant name
        VariantConfiguration config = new VariantConfiguration("debugUnitTest");

        // When: Getting the name
        String result = config.getName();

        // Then: Should return the full compound name
        assertEquals("debugUnitTest", result, "getName() should return compound variant name");
    }

    /**
     * Test getName with flavor variant name.
     * Product flavors create variant names like freeDebug or proRelease.
     */
    @Test
    public void testGetName_flavorVariantName() {
        // Given: A flavor-based variant name
        VariantConfiguration config = new VariantConfiguration("freeDebug");

        // When: Getting the name
        String result = config.getName();

        // Then: Should return the flavor variant name
        assertEquals("freeDebug", result, "getName() should return flavor-based variant name");
    }

    /**
     * Test getName with multi-flavor variant name.
     * Multiple dimensions create names like googleProRelease.
     */
    @Test
    public void testGetName_multiFlavorVariantName() {
        // Given: A multi-flavor variant name
        VariantConfiguration config = new VariantConfiguration("googleProRelease");

        // When: Getting the name
        String result = config.getName();

        // Then: Should return the multi-flavor variant name
        assertEquals("googleProRelease", result, "getName() should return multi-flavor variant name");
    }

    /**
     * Test getName with camelCase variant name.
     * Android variant names typically use camelCase.
     */
    @Test
    public void testGetName_camelCaseVariantName() {
        // Given: A camelCase variant name
        VariantConfiguration config = new VariantConfiguration("stagingRelease");

        // When: Getting the name
        String result = config.getName();

        // Then: Should preserve camelCase
        assertEquals("stagingRelease", result, "getName() should preserve camelCase");
    }

    /**
     * Test getName with uppercase variant name.
     * Some projects might use uppercase naming conventions.
     */
    @Test
    public void testGetName_uppercaseVariantName() {
        // Given: An uppercase variant name
        VariantConfiguration config = new VariantConfiguration("RELEASE");

        // When: Getting the name
        String result = config.getName();

        // Then: Should preserve uppercase
        assertEquals("RELEASE", result, "getName() should preserve uppercase");
    }

    /**
     * Test getName with lowercase variant name.
     * Lowercase is the typical convention for Android variants.
     */
    @Test
    public void testGetName_lowercaseVariantName() {
        // Given: A lowercase variant name
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the name
        String result = config.getName();

        // Then: Should preserve lowercase
        assertEquals("debug", result, "getName() should preserve lowercase");
    }

    /**
     * Test getName with mixed case variant name.
     * Variant names can have various case patterns.
     */
    @Test
    public void testGetName_mixedCaseVariantName() {
        // Given: A mixed case variant name
        VariantConfiguration config = new VariantConfiguration("DeBuG");

        // When: Getting the name
        String result = config.getName();

        // Then: Should preserve exact case
        assertEquals("DeBuG", result, "getName() should preserve exact case");
    }

    // ==================== Edge Cases ====================

    /**
     * Test getName with empty string name.
     * Edge case: empty variant name should be returned as-is.
     */
    @Test
    public void testGetName_emptyString() {
        // Given: A VariantConfiguration with empty name
        VariantConfiguration config = new VariantConfiguration("");

        // When: Getting the name
        String result = config.getName();

        // Then: Should return empty string
        assertEquals("", result, "getName() should return empty string");
        assertTrue(result.isEmpty(), "getName() should return empty string");
    }

    /**
     * Test getName with variant name containing spaces.
     * Edge case: spaces in variant names should be preserved.
     */
    @Test
    public void testGetName_withSpaces() {
        // Given: A variant name with spaces
        VariantConfiguration config = new VariantConfiguration("debug test");

        // When: Getting the name
        String result = config.getName();

        // Then: Should preserve spaces
        assertEquals("debug test", result, "getName() should preserve spaces");
        assertTrue(result.contains(" "), "getName() should contain space");
    }

    /**
     * Test getName with variant name containing special characters.
     * Edge case: special characters should be preserved.
     */
    @Test
    public void testGetName_withSpecialCharacters() {
        // Given: A variant name with special characters
        VariantConfiguration config = new VariantConfiguration("debug-v1.0_test");

        // When: Getting the name
        String result = config.getName();

        // Then: Should preserve special characters
        assertEquals("debug-v1.0_test", result, "getName() should preserve special characters");
    }

    /**
     * Test getName with long variant name.
     * Edge case: long variant names should be handled correctly.
     */
    @Test
    public void testGetName_longVariantName() {
        // Given: A very long variant name
        String longName = "googlePlayStoreFreeDebugAndroidTestCoverageReport";
        VariantConfiguration config = new VariantConfiguration(longName);

        // When: Getting the name
        String result = config.getName();

        // Then: Should return full long name
        assertEquals(longName, result, "getName() should handle long variant names");
    }

    /**
     * Test getName with variant name containing numbers.
     * Variant names might contain version numbers.
     */
    @Test
    public void testGetName_withNumbers() {
        // Given: A variant name with numbers
        VariantConfiguration config = new VariantConfiguration("v2Release");

        // When: Getting the name
        String result = config.getName();

        // Then: Should preserve numbers
        assertEquals("v2Release", result, "getName() should handle numbers");
    }

    /**
     * Test getName with variant name containing Unicode characters.
     * Edge case: Unicode characters should be preserved.
     */
    @Test
    public void testGetName_withUnicode() {
        // Given: A variant name with Unicode characters
        VariantConfiguration config = new VariantConfiguration("debug测试");

        // When: Getting the name
        String result = config.getName();

        // Then: Should preserve Unicode
        assertEquals("debug测试", result, "getName() should handle Unicode characters");
    }

    /**
     * Test getName with variant name containing only numbers.
     * Edge case: numeric-only variant names.
     */
    @Test
    public void testGetName_onlyNumbers() {
        // Given: A numeric-only variant name
        VariantConfiguration config = new VariantConfiguration("12345");

        // When: Getting the name
        String result = config.getName();

        // Then: Should return the numeric string
        assertEquals("12345", result, "getName() should handle numeric-only names");
    }

    /**
     * Test getName with variant name containing single character.
     * Edge case: single character variant names.
     */
    @Test
    public void testGetName_singleCharacter() {
        // Given: A single character variant name
        VariantConfiguration config = new VariantConfiguration("d");

        // When: Getting the name
        String result = config.getName();

        // Then: Should return the single character
        assertEquals("d", result, "getName() should handle single character names");
        assertEquals(1, result.length(), "getName() should return single character");
    }

    // ==================== Immutability Tests ====================

    /**
     * Test that getName returns immutable value.
     * The name cannot be changed after construction.
     */
    @Test
    public void testGetName_immutableValue() {
        // Given: A VariantConfiguration
        String originalName = "debug";
        VariantConfiguration config = new VariantConfiguration(originalName);

        // When: Getting the name multiple times
        String name1 = config.getName();
        String name2 = config.getName();

        // Then: Name should remain constant
        assertEquals(originalName, name1, "getName() should return original name");
        assertEquals(originalName, name2, "getName() should return original name");
        assertEquals(name1, name2, "getName() should always return same value");
    }

    /**
     * Test that modifying other properties doesn't affect getName.
     * The name should be independent of other properties.
     */
    @Test
    public void testGetName_unaffectedByOtherProperties() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");
        String nameBefore = config.getName();

        // When: Modifying other properties
        config.configuration("proguard-rules.pro");
        config.consumerRuleFilter("*.pro");

        // Then: Name should remain unchanged
        String nameAfter = config.getName();
        assertEquals(nameBefore, nameAfter, "getName() should be unaffected by other property changes");
        assertEquals("debug", nameAfter, "getName() should still return original name");
    }

    // ==================== Multiple Instance Tests ====================

    /**
     * Test getName with multiple instances.
     * Each instance should return its own name independently.
     */
    @Test
    public void testGetName_multipleInstances() {
        // Given: Multiple VariantConfigurations with different names
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        VariantConfiguration config3 = new VariantConfiguration("staging");

        // When: Getting names from each
        String name1 = config1.getName();
        String name2 = config2.getName();
        String name3 = config3.getName();

        // Then: Each should return its own name
        assertEquals("debug", name1, "First instance should return 'debug'");
        assertEquals("release", name2, "Second instance should return 'release'");
        assertEquals("staging", name3, "Third instance should return 'staging'");
        assertNotEquals(name1, name2, "Different instances should have different names");
        assertNotEquals(name2, name3, "Different instances should have different names");
    }

    /**
     * Test getName with instances having same name.
     * Different instances can have the same name value.
     */
    @Test
    public void testGetName_sameNameDifferentInstances() {
        // Given: Multiple VariantConfigurations with same name
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("debug");

        // When: Getting names from each
        String name1 = config1.getName();
        String name2 = config2.getName();

        // Then: Both should return the same name value
        assertEquals("debug", name1, "First instance should return 'debug'");
        assertEquals("debug", name2, "Second instance should return 'debug'");
        assertEquals(name1, name2, "Same name value should be returned");
    }

    // ==================== Type Tests ====================

    /**
     * Test that getName returns String type.
     * The return type should be String.
     */
    @Test
    public void testGetName_returnsStringType() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the name
        Object result = config.getName();

        // Then: Should be a String instance
        assertInstanceOf(String.class, result, "getName() should return String type");
    }

    /**
     * Test that getName result is a valid String.
     * The returned value should have all String properties.
     */
    @Test
    public void testGetName_validStringProperties() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the name
        String result = config.getName();

        // Then: Should have valid String properties
        assertNotNull(result, "getName() should return non-null");
        assertTrue(result.length() >= 0, "getName() should return valid length");
        assertEquals("debug", result.toString(), "getName() should work with toString()");
    }

    // ==================== Common Android Variant Names ====================

    /**
     * Test getName with all common Android variant types.
     * Ensure getName works correctly with typical Android build variant names.
     */
    @Test
    public void testGetName_commonAndroidVariants() {
        // Given: Common Android variant names
        String[] variants = {"debug", "release", "staging", "production",
                            "debugUnitTest", "releaseUnitTest"};

        // When/Then: Each should return its name correctly
        for (String variantName : variants) {
            VariantConfiguration config = new VariantConfiguration(variantName);
            String result = config.getName();
            assertEquals(variantName, result,
                "getName() should return correct name for variant: " + variantName);
        }
    }

    /**
     * Test getName with flavor-based variants.
     * Product flavors create various variant name combinations.
     */
    @Test
    public void testGetName_flavorBasedVariants() {
        // Given: Flavor-based variant names
        String[] variants = {"freeDebug", "freeRelease", "proDebug", "proRelease",
                            "demoDebug", "fullRelease"};

        // When/Then: Each should return its name correctly
        for (String variantName : variants) {
            VariantConfiguration config = new VariantConfiguration(variantName);
            String result = config.getName();
            assertEquals(variantName, result,
                "getName() should return correct name for flavor variant: " + variantName);
        }
    }

    // ==================== String Comparison Tests ====================

    /**
     * Test getName result can be used in string comparisons.
     * The returned name should work correctly with equals().
     */
    @Test
    public void testGetName_stringComparison() {
        // Given: Two VariantConfigurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("debug");

        // When: Getting names and comparing
        String name1 = config1.getName();
        String name2 = config2.getName();

        // Then: Should be equal
        assertTrue(name1.equals(name2), "getName() results should be equal");
        assertTrue(name2.equals(name1), "getName() equality should be symmetric");
        assertEquals(name1, name2, "getName() results should be equal");
    }

    /**
     * Test getName result can be used with startsWith.
     * The returned name should work with String methods.
     */
    @Test
    public void testGetName_startsWithCheck() {
        // Given: A VariantConfiguration with a specific name
        VariantConfiguration config = new VariantConfiguration("debugUnitTest");

        // When: Getting the name and checking prefix
        String name = config.getName();
        boolean startsWithDebug = name.startsWith("debug");

        // Then: Should work correctly
        assertTrue(startsWithDebug, "getName() should work with startsWith()");
        assertTrue(name.startsWith("debug"), "Should start with 'debug'");
        assertFalse(name.startsWith("release"), "Should not start with 'release'");
    }

    /**
     * Test getName result can be used with endsWith.
     * The returned name should work with String methods.
     */
    @Test
    public void testGetName_endsWithCheck() {
        // Given: A VariantConfiguration with a specific name
        VariantConfiguration config = new VariantConfiguration("debugUnitTest");

        // When: Getting the name and checking suffix
        String name = config.getName();
        boolean endsWithTest = name.endsWith("Test");

        // Then: Should work correctly
        assertTrue(endsWithTest, "getName() should work with endsWith()");
        assertTrue(name.endsWith("Test"), "Should end with 'Test'");
        assertFalse(name.endsWith("Debug"), "Should not end with 'Debug'");
    }

    /**
     * Test getName result can be used with contains.
     * The returned name should work with String methods.
     */
    @Test
    public void testGetName_containsCheck() {
        // Given: A VariantConfiguration with a specific name
        VariantConfiguration config = new VariantConfiguration("freeDebugRelease");

        // When: Getting the name and checking contents
        String name = config.getName();

        // Then: Should work correctly with contains
        assertTrue(name.contains("Debug"), "getName() should work with contains()");
        assertTrue(name.contains("free"), "Should contain 'free'");
        assertFalse(name.contains("pro"), "Should not contain 'pro'");
    }

    // ==================== String Concatenation Tests ====================

    /**
     * Test getName result can be used in string concatenation.
     * The returned name should work in string operations.
     */
    @Test
    public void testGetName_stringConcatenation() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Using getName in string concatenation
        String message = "Variant: " + config.getName();

        // Then: Should produce expected result
        assertEquals("Variant: debug", message,
            "getName() should work in string concatenation");
    }

    /**
     * Test getName result can be used in String.format.
     * The returned name should work with formatted output.
     */
    @Test
    public void testGetName_stringFormat() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Using getName in String.format
        String formatted = String.format("Building %s variant", config.getName());

        // Then: Should produce expected result
        assertEquals("Building release variant", formatted,
            "getName() should work with String.format");
    }

    // ==================== Hash Code Tests ====================

    /**
     * Test that getName result has stable hashCode.
     * The hashCode should be consistent across multiple calls.
     */
    @Test
    public void testGetName_stableHashCode() {
        // Given: A VariantConfiguration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting hashCode of name multiple times
        String name = config.getName();
        int hash1 = name.hashCode();
        int hash2 = name.hashCode();
        int hash3 = config.getName().hashCode();

        // Then: Hash codes should be equal
        assertEquals(hash1, hash2, "hashCode should be consistent");
        assertEquals(hash2, hash3, "hashCode should be consistent");
    }

    // ==================== Real-world Usage Scenarios ====================

    /**
     * Test getName for variant identification.
     * getName is typically used to identify which variant is being configured.
     */
    @Test
    public void testGetName_variantIdentification() {
        // Given: Multiple variant configurations
        VariantConfiguration debugConfig = new VariantConfiguration("debug");
        VariantConfiguration releaseConfig = new VariantConfiguration("release");

        // When: Identifying variants by name
        boolean isDebug = debugConfig.getName().equals("debug");
        boolean isRelease = releaseConfig.getName().equals("release");

        // Then: Should correctly identify variants
        assertTrue(isDebug, "Should identify debug variant");
        assertTrue(isRelease, "Should identify release variant");
    }

    /**
     * Test getName for conditional configuration logic.
     * getName is often used in conditional logic to apply variant-specific settings.
     */
    @Test
    public void testGetName_conditionalLogic() {
        // Given: A variant configuration
        VariantConfiguration config = new VariantConfiguration("release");

        // When: Using getName in conditional logic
        boolean shouldOptimize = config.getName().equals("release") ||
                                config.getName().equals("production");

        // Then: Should work in conditions
        assertTrue(shouldOptimize, "Should work in conditional logic");
    }

    /**
     * Test getName for logging purposes.
     * getName provides meaningful output for logging and debugging.
     */
    @Test
    public void testGetName_loggingOutput() {
        // Given: A variant configuration
        VariantConfiguration config = new VariantConfiguration("staging");

        // When: Using getName for logging
        String logMessage = "Configuring ProGuard for variant: " + config.getName();

        // Then: Should produce readable log message
        assertEquals("Configuring ProGuard for variant: staging", logMessage,
            "getName() should provide meaningful logging output");
        assertTrue(logMessage.contains("staging"), "Log message should contain variant name");
    }

    /**
     * Test getName for error messages.
     * getName helps provide context in error messages.
     */
    @Test
    public void testGetName_errorMessages() {
        // Given: A variant configuration
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Using getName in error message
        String errorMessage = String.format("Configuration error in variant '%s'", config.getName());

        // Then: Should produce clear error message
        assertEquals("Configuration error in variant 'debug'", errorMessage,
            "getName() should provide useful context in error messages");
    }

    // ==================== Length Tests ====================

    /**
     * Test that getName returns string with correct length.
     * The length should match the original name's length.
     */
    @Test
    public void testGetName_correctLength() {
        // Given: A VariantConfiguration with known name length
        VariantConfiguration config = new VariantConfiguration("debug");

        // When: Getting the name
        String name = config.getName();

        // Then: Should have correct length
        assertEquals(5, name.length(), "getName() should return string with length 5 for 'debug'");
    }

    /**
     * Test getName with zero-length string.
     * Empty string should have length 0.
     */
    @Test
    public void testGetName_zeroLength() {
        // Given: A VariantConfiguration with empty name
        VariantConfiguration config = new VariantConfiguration("");

        // When: Getting the name
        String name = config.getName();

        // Then: Should have zero length
        assertEquals(0, name.length(), "getName() should return string with length 0 for empty name");
        assertTrue(name.isEmpty(), "getName() should return empty string");
    }
}

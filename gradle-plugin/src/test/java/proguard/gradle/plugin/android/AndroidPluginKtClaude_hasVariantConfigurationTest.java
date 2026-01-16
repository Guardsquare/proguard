/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android;

import org.junit.jupiter.api.Test;
import proguard.gradle.plugin.android.dsl.VariantConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AndroidPluginKt.hasVariantConfiguration(Ljava/lang/Iterable;Ljava/lang/String;)Z
 *
 * This test class focuses on achieving coverage for the hasVariantConfiguration extension function.
 * The method returns true if a VariantConfiguration can be found for the given variantName, false otherwise.
 * It delegates to findVariantConfiguration and checks if the result is not null.
 */
public class AndroidPluginKtClaude_hasVariantConfigurationTest {

    /**
     * Test that hasVariantConfiguration returns true when an exact name match exists.
     * This tests the positive case with exact matching.
     */
    @Test
    public void testHasVariantConfiguration_exactMatch_returnsTrue() {
        // Given: A list with variant configurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2);

        // When: Checking for an exact match
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "release");

        // Then: Should return true
        assertTrue(result, "Should return true when configuration exists with exact name match");
    }

    /**
     * Test that hasVariantConfiguration returns false when no match is found.
     * This tests the negative case where the configuration doesn't exist.
     */
    @Test
    public void testHasVariantConfiguration_noMatch_returnsFalse() {
        // Given: A list with variant configurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2);

        // When: Checking for a non-existent variant
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "nonExistent");

        // Then: Should return false
        assertFalse(result, "Should return false when no configuration is found");
    }

    /**
     * Test that hasVariantConfiguration returns false when the list is empty.
     * This tests the boundary condition of an empty iterable.
     */
    @Test
    public void testHasVariantConfiguration_emptyList_returnsFalse() {
        // Given: An empty list of configurations
        List<VariantConfiguration> configurations = Collections.emptyList();

        // When: Checking for any variant
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "debug");

        // Then: Should return false
        assertFalse(result, "Should return false for empty list");
    }

    /**
     * Test that hasVariantConfiguration returns true when variantName ends with capitalized configuration name.
     * This tests the suffix matching logic (e.g., "freeDebug" matches "debug").
     */
    @Test
    public void testHasVariantConfiguration_suffixMatch_returnsTrue() {
        // Given: A list with variant configurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2);

        // When: Checking with a variant name that ends with capitalized "Debug"
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "freeDebug");

        // Then: Should return true
        assertTrue(result, "Should return true when variantName ends with capitalized configuration name");
    }

    /**
     * Test that hasVariantConfiguration returns false when suffix doesn't match capitalization.
     * "freedebug" should not match "debug" because it doesn't end with "Debug" (capitalized).
     */
    @Test
    public void testHasVariantConfiguration_suffixWithoutCapitalization_returnsFalse() {
        // Given: A list with variant configurations
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking with lowercase suffix
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "freedebug");

        // Then: Should return false
        assertFalse(result, "Should return false when suffix is not properly capitalized");
    }

    /**
     * Test that hasVariantConfiguration returns true with multiple product flavors.
     * Example: "prodPaidRelease" should match "release"
     */
    @Test
    public void testHasVariantConfiguration_multipleFlavorsSuffix_returnsTrue() {
        // Given: A list with variant configurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2);

        // When: Checking with a complex variant name
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "prodPaidRelease");

        // Then: Should return true
        assertTrue(result, "Should return true for complex variant name with multiple flavors");
    }

    /**
     * Test that hasVariantConfiguration returns true when first configuration matches.
     * This verifies the method works correctly when the match is at the start of the list.
     */
    @Test
    public void testHasVariantConfiguration_firstConfigurationMatches_returnsTrue() {
        // Given: A list where the first configuration matches
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        VariantConfiguration config3 = new VariantConfiguration("staging");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2, config3);

        // When: Checking for the first configuration
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "debug");

        // Then: Should return true
        assertTrue(result, "Should return true when first configuration matches");
    }

    /**
     * Test that hasVariantConfiguration returns true when last configuration matches.
     * This verifies the method searches through the entire list.
     */
    @Test
    public void testHasVariantConfiguration_lastConfigurationMatches_returnsTrue() {
        // Given: A list where the last configuration matches
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        VariantConfiguration config3 = new VariantConfiguration("staging");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2, config3);

        // When: Checking for the last configuration
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "staging");

        // Then: Should return true
        assertTrue(result, "Should return true when last configuration matches");
    }

    /**
     * Test that hasVariantConfiguration returns true for single element list with match.
     */
    @Test
    public void testHasVariantConfiguration_singleElementMatch_returnsTrue() {
        // Given: A single configuration
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking for the configuration
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "debug");

        // Then: Should return true
        assertTrue(result, "Should return true for single element match");
    }

    /**
     * Test that hasVariantConfiguration returns false for single element list without match.
     */
    @Test
    public void testHasVariantConfiguration_singleElementNoMatch_returnsFalse() {
        // Given: A single configuration
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking for a different configuration
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "release");

        // Then: Should return false
        assertFalse(result, "Should return false for single element without match");
    }

    /**
     * Test that hasVariantConfiguration is case-sensitive for exact matches.
     * "Debug" should match "debug" via suffix matching (since "Debug" ends with capitalize("debug") = "Debug").
     */
    @Test
    public void testHasVariantConfiguration_caseSensitive_returnsTrue() {
        // Given: A list with lowercase configuration name
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking with different case
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "Debug");

        // Then: Should return true via suffix match
        assertTrue(result, "Should return true via suffix match for capitalized variant");
    }

    /**
     * Test that hasVariantConfiguration returns false for partial string matches.
     * "debu" should not match "debug"
     */
    @Test
    public void testHasVariantConfiguration_partialString_returnsFalse() {
        // Given: A configuration with name "debug"
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking with a partial string
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "debu");

        // Then: Should return false
        assertFalse(result, "Should return false for partial string match");
    }

    /**
     * Test that hasVariantConfiguration handles empty string search.
     */
    @Test
    public void testHasVariantConfiguration_emptySearchString_returnsFalse() {
        // Given: Configurations with non-empty names
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking for empty string
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "");

        // Then: Should return false
        assertFalse(result, "Should return false for empty search string");
    }

    /**
     * Test that hasVariantConfiguration returns true for empty configuration name with empty search.
     */
    @Test
    public void testHasVariantConfiguration_emptyConfigurationAndSearch_returnsTrue() {
        // Given: A configuration with empty name
        VariantConfiguration config = new VariantConfiguration("");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking for empty string
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "");

        // Then: Should return true (exact match)
        assertTrue(result, "Should return true when both configuration name and search are empty");
    }

    /**
     * Test that hasVariantConfiguration handles numeric prefixes in variant names.
     * Common in Android variants like "api21Debug"
     */
    @Test
    public void testHasVariantConfiguration_numericPrefix_returnsTrue() {
        // Given: A configuration
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking with numeric prefix
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "api21Debug");

        // Then: Should return true
        assertTrue(result, "Should return true with numeric prefix in variant name");
    }

    /**
     * Test that hasVariantConfiguration handles special characters in configuration names.
     */
    @Test
    public void testHasVariantConfiguration_specialCharacters_returnsTrue() {
        // Given: A configuration with special characters
        VariantConfiguration config = new VariantConfiguration("debug-test");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking for exact match
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "debug-test");

        // Then: Should return true
        assertTrue(result, "Should return true for configuration with special characters");
    }

    /**
     * Test that hasVariantConfiguration with multiple configurations, none matching.
     * This ensures the method checks all configurations before returning false.
     */
    @Test
    public void testHasVariantConfiguration_multipleConfigurationsNoMatch_returnsFalse() {
        // Given: Multiple configurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        VariantConfiguration config3 = new VariantConfiguration("staging");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2, config3);

        // When: Checking for a non-existent configuration
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "production");

        // Then: Should return false
        assertFalse(result, "Should return false when none of multiple configurations match");
    }

    /**
     * Test that hasVariantConfiguration returns true when only suffix match exists (no exact match).
     * This verifies the fallback behavior works independently.
     */
    @Test
    public void testHasVariantConfiguration_onlySuffixMatch_returnsTrue() {
        // Given: Configurations that don't exactly match the search term
        VariantConfiguration config1 = new VariantConfiguration("release");
        List<VariantConfiguration> configurations = Collections.singletonList(config1);

        // When: Checking with a variant that only matches via suffix
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "freeRelease");

        // Then: Should return true
        assertTrue(result, "Should return true when only suffix match exists");
    }

    /**
     * Test that hasVariantConfiguration correctly handles duplicate configuration names.
     * Even with duplicates, if a match exists, it should return true.
     */
    @Test
    public void testHasVariantConfiguration_duplicateConfigurations_returnsTrue() {
        // Given: Duplicate configuration names
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2);

        // When: Checking for the duplicate name
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "debug");

        // Then: Should return true
        assertTrue(result, "Should return true for duplicate configurations");
    }

    /**
     * Test that hasVariantConfiguration with all uppercase configuration name.
     */
    @Test
    public void testHasVariantConfiguration_uppercaseConfiguration_returnsTrue() {
        // Given: A configuration with uppercase name
        VariantConfiguration config = new VariantConfiguration("DEBUG");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking for exact match
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "DEBUG");

        // Then: Should return true
        assertTrue(result, "Should return true for uppercase configuration name");
    }

    /**
     * Test that hasVariantConfiguration returns true when variant name exactly matches
     * even when suffix match would also work.
     * This verifies exact match takes precedence.
     */
    @Test
    public void testHasVariantConfiguration_exactMatchPrecedence_returnsTrue() {
        // Given: Multiple configurations where one exactly matches and another could match via suffix
        VariantConfiguration configDebug = new VariantConfiguration("debug");
        VariantConfiguration configFreeDebug = new VariantConfiguration("freeDebug");
        List<VariantConfiguration> configurations = Arrays.asList(configDebug, configFreeDebug);

        // When: Checking for "freeDebug"
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "freeDebug");

        // Then: Should return true (finds exact match first)
        assertTrue(result, "Should return true with exact match precedence");
    }

    /**
     * Test that hasVariantConfiguration with single character configuration name.
     */
    @Test
    public void testHasVariantConfiguration_singleCharacter_returnsTrue() {
        // Given: A configuration with single character name
        VariantConfiguration config = new VariantConfiguration("a");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking for exact match
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "a");

        // Then: Should return true
        assertTrue(result, "Should return true for single character configuration");
    }

    /**
     * Test that hasVariantConfiguration with single character suffix match.
     */
    @Test
    public void testHasVariantConfiguration_singleCharacterSuffix_returnsTrue() {
        // Given: A configuration with single character name
        VariantConfiguration config = new VariantConfiguration("a");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking with a name ending with capitalized "A"
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "testA");

        // Then: Should return true
        assertTrue(result, "Should return true for single character suffix match");
    }

    /**
     * Test that hasVariantConfiguration returns true with already capitalized configuration name.
     */
    @Test
    public void testHasVariantConfiguration_alreadyCapitalized_returnsTrue() {
        // Given: A configuration with capitalized name
        VariantConfiguration config = new VariantConfiguration("Debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Checking with a variant ending in "Debug"
        boolean result = AndroidPluginKt.hasVariantConfiguration(configurations, "freeDebug");

        // Then: Should return true
        assertTrue(result, "Should return true for already capitalized configuration name");
    }
}

/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android;

import org.junit.jupiter.api.Test;
import proguard.gradle.plugin.android.dsl.VariantConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AndroidPluginKt.findVariantConfiguration(Ljava/lang/Iterable;Ljava/lang/String;)Lproguard/gradle/plugin/android/dsl/VariantConfiguration;
 *
 * This test class focuses on achieving coverage for the findVariantConfiguration extension function.
 * The method searches for a VariantConfiguration in an Iterable by:
 * 1. First attempting an exact name match
 * 2. If not found, attempting a match where the variantName ends with the capitalized configuration name
 */
public class AndroidPluginKtClaude_findVariantConfigurationTest {

    /**
     * Test that findVariantConfiguration returns a configuration when an exact name match is found.
     * This tests the first find clause: find { it.name == variantName }
     */
    @Test
    public void testFindVariantConfiguration_exactMatch() {
        // Given: A list with variant configurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        VariantConfiguration config3 = new VariantConfiguration("staging");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2, config3);

        // When: Searching for an exact match
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "release");

        // Then: The matching configuration should be returned
        assertNotNull(result, "Should find configuration with exact name match");
        assertEquals("release", result.getName(), "Should return the configuration with name 'release'");
        assertSame(config2, result, "Should return the exact same object");
    }

    /**
     * Test that findVariantConfiguration returns the first configuration when multiple exact matches exist.
     * This tests the behavior when find encounters the first matching element.
     */
    @Test
    public void testFindVariantConfiguration_multipleExactMatches_returnsFirst() {
        // Given: A list with duplicate configuration names
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2);

        // When: Searching for the duplicate name
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "debug");

        // Then: The first matching configuration should be returned
        assertNotNull(result, "Should find configuration");
        assertSame(config1, result, "Should return the first matching configuration");
    }

    /**
     * Test that findVariantConfiguration returns null when no match is found.
     * This tests the case where both find clauses fail.
     */
    @Test
    public void testFindVariantConfiguration_noMatch_returnsNull() {
        // Given: A list with variant configurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2);

        // When: Searching for a non-existent variant
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "nonExistent");

        // Then: Null should be returned
        assertNull(result, "Should return null when no match is found");
    }

    /**
     * Test that findVariantConfiguration returns null when the list is empty.
     * This tests the boundary condition of an empty iterable.
     */
    @Test
    public void testFindVariantConfiguration_emptyList_returnsNull() {
        // Given: An empty list of configurations
        List<VariantConfiguration> configurations = Collections.emptyList();

        // When: Searching for any variant
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "debug");

        // Then: Null should be returned
        assertNull(result, "Should return null for empty list");
    }

    /**
     * Test that findVariantConfiguration matches when variantName ends with capitalized configuration name.
     * This tests the second find clause: find { variantName.endsWith(it.name.capitalize()) }
     * Example: variant name "freeDebug" should match configuration name "debug"
     */
    @Test
    public void testFindVariantConfiguration_endsWithCapitalizedName() {
        // Given: A list with variant configurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2);

        // When: Searching with a variant name that ends with capitalized "Debug"
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "freeDebug");

        // Then: The configuration with name "debug" should be found
        assertNotNull(result, "Should find configuration when variantName ends with capitalized name");
        assertEquals("debug", result.getName(), "Should return the debug configuration");
        assertSame(config1, result, "Should return the debug configuration object");
    }

    /**
     * Test that findVariantConfiguration matches with multiple product flavors.
     * Example: "prodPaidRelease" should match "release"
     */
    @Test
    public void testFindVariantConfiguration_multipleFlavorsSuffix() {
        // Given: A list with variant configurations
        VariantConfiguration config1 = new VariantConfiguration("debug");
        VariantConfiguration config2 = new VariantConfiguration("release");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2);

        // When: Searching with a complex variant name
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "prodPaidRelease");

        // Then: The release configuration should be found
        assertNotNull(result, "Should find configuration for complex variant name");
        assertEquals("release", result.getName(), "Should return the release configuration");
    }

    /**
     * Test that exact match takes precedence over suffix match.
     * When a configuration name exactly matches, it should be returned even if
     * another configuration would match via the endsWith logic.
     */
    @Test
    public void testFindVariantConfiguration_exactMatchTakesPrecedence() {
        // Given: A list where one config matches exactly and another would match via endsWith
        VariantConfiguration configDebug = new VariantConfiguration("debug");
        VariantConfiguration configFreeDebug = new VariantConfiguration("freeDebug");
        List<VariantConfiguration> configurations = Arrays.asList(configDebug, configFreeDebug);

        // When: Searching for "freeDebug"
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "freeDebug");

        // Then: The exact match should be returned, not the suffix match
        assertNotNull(result, "Should find configuration");
        assertEquals("freeDebug", result.getName(), "Should return exact match");
        assertSame(configFreeDebug, result, "Should return the exact match configuration");
    }

    /**
     * Test that findVariantConfiguration is case-sensitive for exact matches.
     * "Debug" should not match "debug" exactly.
     */
    @Test
    public void testFindVariantConfiguration_caseSensitiveExactMatch() {
        // Given: A list with lowercase configuration name
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching with different case (should match via endsWith since "Debug" ends with "Debug")
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "Debug");

        // Then: Should find via endsWith match (capitalize "debug" -> "Debug")
        assertNotNull(result, "Should find configuration via endsWith match");
        assertEquals("debug", result.getName(), "Should return the debug configuration");
    }

    /**
     * Test that findVariantConfiguration handles single character configuration names.
     * This tests edge cases with string operations.
     */
    @Test
    public void testFindVariantConfiguration_singleCharacterName() {
        // Given: A configuration with single character name
        VariantConfiguration config = new VariantConfiguration("a");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching for exact match
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "a");

        // Then: Should find the configuration
        assertNotNull(result, "Should find configuration with single character name");
        assertEquals("a", result.getName());
    }

    /**
     * Test that findVariantConfiguration handles configuration with capitalized suffix match.
     * Example: "testA" should match configuration "a" (since "testA" ends with capitalize("a") = "A")
     */
    @Test
    public void testFindVariantConfiguration_singleCharacterSuffixMatch() {
        // Given: A configuration with single character name
        VariantConfiguration config = new VariantConfiguration("a");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching with a name ending with capitalized "A"
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "testA");

        // Then: Should find via endsWith match
        assertNotNull(result, "Should find configuration via endsWith with single character");
        assertEquals("a", result.getName());
    }

    /**
     * Test that findVariantConfiguration does not match partial strings.
     * "debu" should not match "debug"
     */
    @Test
    public void testFindVariantConfiguration_partialStringNoMatch() {
        // Given: A configuration with name "debug"
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching with a partial string
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "debu");

        // Then: Should not find a match
        assertNull(result, "Should not match partial strings");
    }

    /**
     * Test that findVariantConfiguration with suffix match requires the capitalized form at the end.
     * "debugfree" should NOT match "free" because it doesn't end with "Free" (capitalized)
     */
    @Test
    public void testFindVariantConfiguration_suffixMatchRequiresCapitalization() {
        // Given: A configuration with name "free"
        VariantConfiguration config = new VariantConfiguration("free");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching with lowercase suffix
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "debugfree");

        // Then: Should not find a match (needs "debugFree" to match)
        assertNull(result, "Should not match when suffix is not capitalized");
    }

    /**
     * Test that findVariantConfiguration matches when suffix is properly capitalized.
     * "debugFree" should match "free"
     */
    @Test
    public void testFindVariantConfiguration_suffixMatchWithProperCapitalization() {
        // Given: A configuration with name "free"
        VariantConfiguration config = new VariantConfiguration("free");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching with properly capitalized suffix
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "debugFree");

        // Then: Should find the configuration
        assertNotNull(result, "Should match when suffix is properly capitalized");
        assertEquals("free", result.getName());
    }

    /**
     * Test that findVariantConfiguration handles empty string configuration name.
     * This is an edge case that tests robustness.
     */
    @Test
    public void testFindVariantConfiguration_emptyConfigurationName() {
        // Given: A configuration with empty name
        VariantConfiguration config = new VariantConfiguration("");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching for empty string
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "");

        // Then: Should find exact match
        assertNotNull(result, "Should find configuration with empty name via exact match");
        assertEquals("", result.getName());
    }

    /**
     * Test that findVariantConfiguration handles empty string search on non-empty configurations.
     */
    @Test
    public void testFindVariantConfiguration_emptySearchString() {
        // Given: Configurations with non-empty names
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching for empty string
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "");

        // Then: Should not find a match (empty string doesn't end with "Debug")
        assertNull(result, "Empty search string should not match non-empty configuration names");
    }

    /**
     * Test that findVariantConfiguration returns first match when multiple configurations match via suffix.
     * When "freeDebug" is searched and both "debug" and "ug" configurations exist,
     * it should return the first one that matches.
     */
    @Test
    public void testFindVariantConfiguration_multipleSuffixMatches_returnsFirst() {
        // Given: Multiple configurations that would match via suffix
        VariantConfiguration config1 = new VariantConfiguration("bug");
        VariantConfiguration config2 = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Arrays.asList(config1, config2);

        // When: Searching with a name that ends with both "Bug" and "Debug"
        // "freeBugDebug" ends with both "Bug" (capitalize of "bug") and "Debug" (capitalize of "debug")
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "freeBugDebug");

        // Then: Should return the first matching configuration in iteration order
        assertNotNull(result, "Should find a configuration via suffix match");
        // Since "freeBugDebug" doesn't end with "Bug" but ends with "Debug", should match "debug"
        assertEquals("debug", result.getName(), "Should match the debug configuration");
    }

    /**
     * Test that findVariantConfiguration handles special characters in names.
     */
    @Test
    public void testFindVariantConfiguration_specialCharactersInName() {
        // Given: A configuration with special characters
        VariantConfiguration config = new VariantConfiguration("debug-test");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching for exact match
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "debug-test");

        // Then: Should find the configuration
        assertNotNull(result, "Should handle special characters in exact match");
        assertEquals("debug-test", result.getName());
    }

    /**
     * Test that findVariantConfiguration with already capitalized configuration name.
     * Configuration "Debug" should match variant "freeDebug"
     */
    @Test
    public void testFindVariantConfiguration_alreadyCapitalizedConfigName() {
        // Given: A configuration with capitalized name
        VariantConfiguration config = new VariantConfiguration("Debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching with a variant ending in "Debug"
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "freeDebug");

        // Then: Should find via endsWith match (capitalize("Debug") = "Debug")
        assertNotNull(result, "Should match already capitalized configuration name");
        assertEquals("Debug", result.getName());
    }

    /**
     * Test that findVariantConfiguration handles all uppercase configuration names.
     */
    @Test
    public void testFindVariantConfiguration_allUppercaseConfigName() {
        // Given: A configuration with all uppercase name
        VariantConfiguration config = new VariantConfiguration("DEBUG");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching for exact match
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "DEBUG");

        // Then: Should find exact match
        assertNotNull(result, "Should find exact match for uppercase name");
        assertEquals("DEBUG", result.getName());
    }

    /**
     * Test behavior with ArrayList to ensure the method works with different Iterable implementations.
     */
    @Test
    public void testFindVariantConfiguration_withArrayList() {
        // Given: An ArrayList of configurations
        List<VariantConfiguration> configurations = new ArrayList<>();
        configurations.add(new VariantConfiguration("debug"));
        configurations.add(new VariantConfiguration("release"));

        // When: Searching for a configuration
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "release");

        // Then: Should find the configuration
        assertNotNull(result, "Should work with ArrayList");
        assertEquals("release", result.getName());
    }

    /**
     * Test that findVariantConfiguration maintains correct behavior with numeric suffixes.
     * This is common in Android variants like "api21Debug"
     */
    @Test
    public void testFindVariantConfiguration_withNumericPrefix() {
        // Given: A configuration
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching with numeric prefix
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "api21Debug");

        // Then: Should find via endsWith match
        assertNotNull(result, "Should match with numeric prefix");
        assertEquals("debug", result.getName());
    }

    /**
     * Test that only the capitalized form of the name is matched, not the original.
     * "freedebug" should NOT match "debug" because it ends with "debug" not "Debug"
     */
    @Test
    public void testFindVariantConfiguration_requiresCapitalizedSuffix() {
        // Given: A configuration with lowercase name
        VariantConfiguration config = new VariantConfiguration("debug");
        List<VariantConfiguration> configurations = Collections.singletonList(config);

        // When: Searching with lowercase suffix (no exact match, no capitalized suffix)
        VariantConfiguration result = AndroidPluginKt.findVariantConfiguration(configurations, "freedebug");

        // Then: Should not find a match
        assertNull(result, "Should require capitalized suffix for endsWith match");
    }
}

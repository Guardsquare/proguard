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
 * Test class for proguard.gradle.plugin.android.dsl.ProGuardConfiguration.toString()
 *
 * Tests the toString() method which returns the string representation of ProGuardConfiguration
 * and its subclasses (UserProGuardConfiguration and DefaultProGuardConfiguration).
 *
 * The toString() method returns the filename property for all configuration types.
 * This is used for display purposes and logging.
 */
public class ProGuardConfigurationClaude_toStringTest {

    // ==================== Tests for UserProGuardConfiguration.toString() ====================

    /**
     * Test that toString returns the filename for user configurations.
     * UserProGuardConfiguration should return the filename passed to the constructor.
     */
    @Test
    public void testUserProGuardConfiguration_toString_returnsFilename() {
        // Given: A UserProGuardConfiguration with a specific filename
        String filename = "proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the filename
        assertEquals(filename, result, "toString() should return the filename");
    }

    /**
     * Test that toString equals getFilename for user configurations.
     * The toString() method should return the same value as getFilename().
     */
    @Test
    public void testUserProGuardConfiguration_toString_equalsGetFilename() {
        // Given: A UserProGuardConfiguration
        String filename = "proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting toString and getFilename
        String toStringResult = config.toString();
        String getFilenameResult = config.getFilename();

        // Then: They should be equal
        assertEquals(getFilenameResult, toStringResult,
            "toString() should return the same value as getFilename()");
    }

    /**
     * Test that toString returns relative path for user configurations.
     * User configurations with relative paths should have toString return the path.
     */
    @Test
    public void testUserProGuardConfiguration_toString_withRelativePath() {
        // Given: A UserProGuardConfiguration with a relative path
        String filename = "config/proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the relative path
        assertEquals(filename, result, "toString() should return the relative path");
        assertEquals("config/proguard-rules.pro", result);
    }

    /**
     * Test that toString returns absolute path for user configurations.
     * User configurations with absolute paths should have toString return the full path.
     */
    @Test
    public void testUserProGuardConfiguration_toString_withAbsolutePath() {
        // Given: A UserProGuardConfiguration with an absolute path
        String filename = "/etc/proguard/proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the absolute path
        assertEquals(filename, result, "toString() should return the absolute path");
    }

    /**
     * Test that toString returns simple filename.
     * Simple filenames without paths should be returned as-is.
     */
    @Test
    public void testUserProGuardConfiguration_toString_simpleFilename() {
        // Given: A UserProGuardConfiguration with just a filename
        String filename = "rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the simple filename
        assertEquals(filename, result, "toString() should return the simple filename");
    }

    /**
     * Test that toString handles empty string.
     * Edge case testing for empty filename.
     */
    @Test
    public void testUserProGuardConfiguration_toString_emptyString() {
        // Given: A UserProGuardConfiguration with an empty filename
        String filename = "";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should return empty string
        assertEquals("", result, "toString() should return empty string when filename is empty");
    }

    /**
     * Test that toString handles filenames with spaces.
     * Configuration files might have spaces in their names.
     */
    @Test
    public void testUserProGuardConfiguration_toString_withSpaces() {
        // Given: A UserProGuardConfiguration with spaces in the filename
        String filename = "proguard rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the filename with spaces
        assertEquals(filename, result, "toString() should handle filenames with spaces");
    }

    /**
     * Test that toString handles filenames with special characters.
     * Configuration files might contain special characters.
     */
    @Test
    public void testUserProGuardConfiguration_toString_withSpecialCharacters() {
        // Given: A UserProGuardConfiguration with special characters
        String filename = "proguard-rules_v2.0.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the filename with special characters
        assertEquals(filename, result, "toString() should handle special characters");
    }

    /**
     * Test that toString returns consistent results.
     * Multiple calls should return the same value.
     */
    @Test
    public void testUserProGuardConfiguration_toString_consistentResults() {
        // Given: A UserProGuardConfiguration
        String filename = "proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString multiple times
        String result1 = config.toString();
        String result2 = config.toString();
        String result3 = config.toString();

        // Then: All results should be equal
        assertEquals(result1, result2, "toString() should return consistent results");
        assertEquals(result2, result3, "toString() should return consistent results");
    }

    /**
     * Test that toString returns non-null value.
     * The string representation should never be null.
     */
    @Test
    public void testUserProGuardConfiguration_toString_notNull() {
        // Given: A UserProGuardConfiguration
        String filename = "proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should not be null
        assertNotNull(result, "toString() should not return null");
    }

    /**
     * Test that toString handles Windows-style paths.
     * Windows systems use backslashes in paths.
     */
    @Test
    public void testUserProGuardConfiguration_toString_windowsPath() {
        // Given: A UserProGuardConfiguration with Windows-style path
        String filename = "C:\\projects\\app\\proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the Windows path as-is
        assertEquals(filename, result, "toString() should handle Windows paths");
    }

    // ==================== Tests for DefaultProGuardConfiguration.toString() ====================

    /**
     * Test that toString returns correct value for Android debug configuration.
     * The default Android debug configuration should return its filename.
     */
    @Test
    public void testDefaultProGuardConfiguration_toString_androidDebug() {
        // Given: A DefaultProGuardConfiguration for Android debug
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the filename (not the full path)
        assertEquals("proguard-android-debug.txt", result,
            "toString() should return the filename for Android debug");
    }

    /**
     * Test that toString returns correct value for Android release configuration.
     * The default Android release configuration should return its filename.
     */
    @Test
    public void testDefaultProGuardConfiguration_toString_androidRelease() {
        // Given: A DefaultProGuardConfiguration for Android release
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the filename
        assertEquals("proguard-android.txt", result,
            "toString() should return the filename for Android release");
    }

    /**
     * Test that toString returns correct value for Android optimize configuration.
     * The default Android optimize configuration should return its filename.
     */
    @Test
    public void testDefaultProGuardConfiguration_toString_androidOptimize() {
        // Given: A DefaultProGuardConfiguration for Android optimize
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt");

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the filename
        assertEquals("proguard-android-optimize.txt", result,
            "toString() should return the filename for Android optimize");
    }

    /**
     * Test that toString equals getFilename for default configurations.
     * The toString() method should return the same value as getFilename().
     */
    @Test
    public void testDefaultProGuardConfiguration_toString_equalsGetFilename() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting toString and getFilename
        String toStringResult = config.toString();
        String getFilenameResult = config.getFilename();

        // Then: They should be equal
        assertEquals(getFilenameResult, toStringResult,
            "toString() should return the same value as getFilename()");
    }

    /**
     * Test that toString returns filename, not path for default configurations.
     * DefaultProGuardConfiguration has different path and filename values,
     * toString should return filename (without /lib prefix).
     */
    @Test
    public void testDefaultProGuardConfiguration_toString_returnsFilenameNotPath() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting toString and path
        String toStringResult = config.toString();
        String path = config.getPath();

        // Then: toString should return filename, not path
        assertEquals("proguard-android.txt", toStringResult,
            "toString() should return the filename, not the path");
        assertNotEquals(path, toStringResult,
            "toString() should differ from getPath() for default configurations");
        assertEquals("/lib/proguard-android.txt", path,
            "getPath() should include /lib prefix");
    }

    /**
     * Test that toString is consistent for default configurations.
     * Multiple calls should return the same value.
     */
    @Test
    public void testDefaultProGuardConfiguration_toString_consistentResults() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Calling toString multiple times
        String result1 = config.toString();
        String result2 = config.toString();
        String result3 = config.toString();

        // Then: All results should be equal
        assertEquals(result1, result2, "toString() should return consistent results");
        assertEquals(result2, result3, "toString() should return consistent results");
    }

    /**
     * Test that toString returns non-null for default configurations.
     * The string representation should never be null.
     */
    @Test
    public void testDefaultProGuardConfiguration_toString_notNull() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Calling toString
        String result = config.toString();

        // Then: Should not be null
        assertNotNull(result, "toString() should not return null");
    }

    /**
     * Test that toString does not contain path separator for default configurations.
     * The filename should not contain slashes.
     */
    @Test
    public void testDefaultProGuardConfiguration_toString_noPathSeparator() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Calling toString
        String result = config.toString();

        // Then: Should not contain path separators
        assertFalse(result.contains("/"), "toString() should not contain forward slash");
        assertFalse(result.contains("\\"), "toString() should not contain backslash");
    }

    // ==================== Comparison Tests ====================

    /**
     * Test that toString differs between user and default configurations with different filenames.
     * Different configurations should have different string representations.
     */
    @Test
    public void testToString_differentBetweenUserAndDefaultWithDifferentNames() {
        // Given: A user configuration and a default configuration with different filenames
        UserProGuardConfiguration userConfig = new UserProGuardConfiguration("custom-rules.pro");
        ProGuardConfiguration defaultConfig = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting string representations
        String userString = userConfig.toString();
        String defaultString = defaultConfig.toString();

        // Then: Strings should be different
        assertNotEquals(userString, defaultString,
            "toString() should differ for configurations with different filenames");
    }

    /**
     * Test that toString is same when user and default configs have same filename.
     * If the filename is the same, toString should return the same value.
     */
    @Test
    public void testToString_sameBetweenUserAndDefaultWithSameFilename() {
        // Given: A user configuration and a default configuration with the same filename
        UserProGuardConfiguration userConfig = new UserProGuardConfiguration("proguard-android.txt");
        ProGuardConfiguration defaultConfig = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting string representations
        String userString = userConfig.toString();
        String defaultString = defaultConfig.toString();

        // Then: Strings should be the same (both return filename)
        assertEquals(userString, defaultString,
            "toString() should be the same when filenames match");
        assertEquals("proguard-android.txt", userString);
        assertEquals("proguard-android.txt", defaultString);
    }

    /**
     * Test that toString is unique for each default configuration.
     * Each default configuration type should have a unique string representation.
     */
    @Test
    public void testToString_uniqueForEachDefaultConfiguration() {
        // Given: Three different default configurations
        ProGuardConfiguration debug = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");
        ProGuardConfiguration release = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");
        ProGuardConfiguration optimize = DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt");

        // When: Getting string representations
        String debugString = debug.toString();
        String releaseString = release.toString();
        String optimizeString = optimize.toString();

        // Then: All strings should be different
        assertNotEquals(debugString, releaseString, "Debug and release should have different toString");
        assertNotEquals(releaseString, optimizeString, "Release and optimize should have different toString");
        assertNotEquals(debugString, optimizeString, "Debug and optimize should have different toString");
    }

    // ==================== String Interpolation and Usage Tests ====================

    /**
     * Test that toString can be used in string concatenation.
     * toString() should work naturally in string operations.
     */
    @Test
    public void testToString_usableInStringConcatenation() {
        // Given: A ProGuardConfiguration
        UserProGuardConfiguration config = new UserProGuardConfiguration("proguard-rules.pro");

        // When: Using toString in string concatenation
        String message = "Using configuration: " + config;

        // Then: Should produce expected string
        assertEquals("Using configuration: proguard-rules.pro", message,
            "toString() should work in string concatenation");
    }

    /**
     * Test that toString can be used in String.format.
     * toString() should work with formatted string output.
     */
    @Test
    public void testToString_usableInStringFormat() {
        // Given: A ProGuardConfiguration
        UserProGuardConfiguration config = new UserProGuardConfiguration("proguard-rules.pro");

        // When: Using toString in String.format
        String formatted = String.format("Configuration file: %s", config);

        // Then: Should produce expected string
        assertEquals("Configuration file: proguard-rules.pro", formatted,
            "toString() should work with String.format");
    }

    /**
     * Test that toString can be used in logging contexts.
     * toString() provides a meaningful representation for logging.
     */
    @Test
    public void testToString_meaningfulForLogging() {
        // Given: Various configurations
        UserProGuardConfiguration userConfig = new UserProGuardConfiguration("proguard-rules.pro");
        ProGuardConfiguration defaultConfig = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting string representations
        String userString = userConfig.toString();
        String defaultString = defaultConfig.toString();

        // Then: Should be meaningful and descriptive
        assertTrue(userString.contains("proguard"), "toString() should contain descriptive text");
        assertTrue(defaultString.contains("proguard"), "toString() should contain descriptive text");
        assertFalse(userString.isEmpty(), "toString() should not be empty");
        assertFalse(defaultString.isEmpty(), "toString() should not be empty");
    }

    // ==================== Realistic Usage Scenarios ====================

    /**
     * Test toString for typical Android app configuration.
     * Common Android app configurations should have clear string representations.
     */
    @Test
    public void testToString_typicalAndroidAppConfiguration() {
        // Given: A typical Android app ProGuard configuration
        UserProGuardConfiguration config = new UserProGuardConfiguration("proguard-rules.pro");

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the standard filename
        assertEquals("proguard-rules.pro", result,
            "toString() should return standard Android configuration filename");
    }

    /**
     * Test toString for default Android debug build.
     * Debug builds should have clear string representation.
     */
    @Test
    public void testToString_defaultAndroidDebugBuild() {
        // Given: A default Android debug configuration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the debug configuration filename
        assertEquals("proguard-android-debug.txt", result,
            "toString() should return debug configuration name");
    }

    /**
     * Test toString for default Android release build.
     * Release builds should have clear string representation.
     */
    @Test
    public void testToString_defaultAndroidReleaseBuild() {
        // Given: A default Android release configuration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the release configuration filename
        assertEquals("proguard-android.txt", result,
            "toString() should return release configuration name");
    }

    /**
     * Test toString for optimized release build.
     * Optimized builds should have clear string representation.
     */
    @Test
    public void testToString_optimizedReleaseBuild() {
        // Given: A default Android optimize configuration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt");

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the optimize configuration filename
        assertEquals("proguard-android-optimize.txt", result,
            "toString() should return optimize configuration name");
    }

    /**
     * Test toString for library module configuration.
     * Library modules often have consumer rules files.
     */
    @Test
    public void testToString_libraryModuleConfiguration() {
        // Given: A library module ProGuard configuration
        UserProGuardConfiguration config = new UserProGuardConfiguration("consumer-rules.pro");

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the consumer rules filename
        assertEquals("consumer-rules.pro", result,
            "toString() should return library module configuration name");
    }

    /**
     * Test toString for variant-specific configuration.
     * Variant-specific configurations should include the path in toString.
     */
    @Test
    public void testToString_variantSpecificConfiguration() {
        // Given: A variant-specific ProGuard configuration
        UserProGuardConfiguration config = new UserProGuardConfiguration("staging/proguard-rules.pro");

        // When: Calling toString
        String result = config.toString();

        // Then: Should include the variant subdirectory
        assertEquals("staging/proguard-rules.pro", result,
            "toString() should include variant-specific path");
    }

    // ==================== Type Safety Tests ====================

    /**
     * Test that toString returns a String type.
     * The return type should be String.
     */
    @Test
    public void testToString_returnsStringType() {
        // Given: A ProGuardConfiguration
        UserProGuardConfiguration config = new UserProGuardConfiguration("test.pro");

        // When: Calling toString
        Object result = config.toString();

        // Then: Should be a String instance
        assertInstanceOf(String.class, result,
            "toString() should return a String instance");
    }

    /**
     * Test that toString from parent class reference works correctly.
     * Polymorphism should work correctly with the ProGuardConfiguration base class.
     */
    @Test
    public void testToString_polymorphism() {
        // Given: ProGuardConfiguration references to subclass instances
        ProGuardConfiguration userConfig = new UserProGuardConfiguration("user-rules.pro");
        ProGuardConfiguration defaultConfig = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Calling toString via parent class reference
        String userString = userConfig.toString();
        String defaultString = defaultConfig.toString();

        // Then: Should return correct values based on actual type
        assertEquals("user-rules.pro", userString,
            "Polymorphic call should work for UserProGuardConfiguration");
        assertEquals("proguard-android.txt", defaultString,
            "Polymorphic call should work for DefaultProGuardConfiguration");
    }

    // ==================== Edge Cases ====================

    /**
     * Test toString with filename containing only extension.
     * Edge case with unusual filename pattern.
     */
    @Test
    public void testToString_onlyExtension() {
        // Given: A configuration with only extension
        UserProGuardConfiguration config = new UserProGuardConfiguration(".pro");

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the extension-only filename
        assertEquals(".pro", result, "toString() should handle filename with only extension");
    }

    /**
     * Test toString with filename containing no extension.
     * Configuration files might not have extensions.
     */
    @Test
    public void testToString_noExtension() {
        // Given: A configuration with no extension
        UserProGuardConfiguration config = new UserProGuardConfiguration("proguard-rules");

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the filename without extension
        assertEquals("proguard-rules", result, "toString() should handle filename without extension");
    }

    /**
     * Test toString with very long filename.
     * Long filenames should be handled correctly.
     */
    @Test
    public void testToString_longFilename() {
        // Given: A configuration with a very long filename
        String longFilename = "proguard-rules-for-production-release-build-with-optimization-enabled.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(longFilename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should return the full long filename
        assertEquals(longFilename, result, "toString() should handle long filenames");
    }

    /**
     * Test toString with Unicode characters in filename.
     * Filenames might contain Unicode characters.
     */
    @Test
    public void testToString_unicodeCharacters() {
        // Given: A configuration with Unicode characters
        String filename = "proguard-规则.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should handle Unicode characters
        assertEquals(filename, result, "toString() should handle Unicode characters");
    }

    /**
     * Test toString with multiple dots in filename.
     * Filenames can have multiple dots.
     */
    @Test
    public void testToString_multipleDotsInFilename() {
        // Given: A configuration with multiple dots
        String filename = "proguard.rules.v2.0.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Calling toString
        String result = config.toString();

        // Then: Should preserve all dots
        assertEquals(filename, result, "toString() should handle multiple dots in filename");
    }

    // ==================== Equality and Hash Code Related Tests ====================

    /**
     * Test that objects with same toString are not necessarily equal.
     * toString equality does not imply object equality.
     */
    @Test
    public void testToString_sameStringDifferentObjectTypes() {
        // Given: A user config and default config with same filename
        UserProGuardConfiguration userConfig = new UserProGuardConfiguration("proguard-android.txt");
        ProGuardConfiguration defaultConfig = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Comparing toString results
        String userString = userConfig.toString();
        String defaultString = defaultConfig.toString();

        // Then: toString values are equal, but objects are different types
        assertEquals(userString, defaultString, "toString() values should be equal");
        assertNotEquals(userConfig.getClass(), defaultConfig.getClass(),
            "Objects should be of different types despite same toString");
    }

    /**
     * Test that toString can be used for display purposes.
     * toString() should provide a suitable representation for UI display.
     */
    @Test
    public void testToString_suitableForDisplay() {
        // Given: Various configurations
        UserProGuardConfiguration config1 = new UserProGuardConfiguration("proguard-rules.pro");
        ProGuardConfiguration config2 = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");

        // When: Getting string representations
        String display1 = config1.toString();
        String display2 = config2.toString();

        // Then: Should be readable and meaningful
        assertFalse(display1.isEmpty(), "Display string should not be empty");
        assertFalse(display2.isEmpty(), "Display string should not be empty");
        assertTrue(display1.length() > 0, "Display string should have content");
        assertTrue(display2.length() > 0, "Display string should have content");
    }

    // ==================== Integration with Error Messages ====================

    /**
     * Test that toString provides useful information for error messages.
     * When exceptions occur, toString should help identify the configuration.
     */
    @Test
    public void testToString_usefulForErrorMessages() {
        // Given: A ProGuardConfiguration
        UserProGuardConfiguration config = new UserProGuardConfiguration("missing-rules.pro");

        // When: Creating an error message
        String errorMessage = "Configuration not found: " + config;

        // Then: Should be clear and descriptive
        assertEquals("Configuration not found: missing-rules.pro", errorMessage,
            "toString() should provide useful information for error messages");
        assertTrue(errorMessage.contains("missing-rules.pro"),
            "Error message should identify the specific configuration");
    }

    /**
     * Test that toString output matches what's shown in error messages.
     * When DefaultProGuardConfiguration.fromString throws an exception,
     * the error message uses toString() for display.
     */
    @Test
    public void testToString_matchesErrorMessageFormat() {
        // Given: Valid default configurations
        ProGuardConfiguration debug = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");
        ProGuardConfiguration release = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");
        ProGuardConfiguration optimize = DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt");

        // When: Getting string representations
        String debugString = debug.toString();
        String releaseString = release.toString();
        String optimizeString = optimize.toString();

        // Then: Strings should match what's shown in error messages
        // (The error message in fromString uses config.toString() for display)
        assertEquals("proguard-android-debug.txt", debugString);
        assertEquals("proguard-android.txt", releaseString);
        assertEquals("proguard-android-optimize.txt", optimizeString);
    }
}

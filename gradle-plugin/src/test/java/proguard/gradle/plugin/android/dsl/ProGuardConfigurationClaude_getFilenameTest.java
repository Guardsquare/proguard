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
 * Test class for proguard.gradle.plugin.android.dsl.ProGuardConfiguration.getFilename()
 *
 * Tests the getFilename() method which returns the filename property of ProGuardConfiguration
 * and its subclasses (UserProGuardConfiguration and DefaultProGuardConfiguration).
 *
 * The filename property stores the name of the ProGuard configuration file.
 */
public class ProGuardConfigurationClaude_getFilenameTest {

    // ==================== Tests for UserProGuardConfiguration.getFilename() ====================

    /**
     * Test that getFilename returns the correct filename for a user configuration.
     * UserProGuardConfiguration should store and return the filename passed to the constructor.
     */
    @Test
    public void testUserProGuardConfiguration_getFilename_returnsCorrectValue() {
        // Given: A UserProGuardConfiguration with a specific filename
        String filename = "proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the same filename
        assertEquals(filename, result, "getFilename() should return the correct filename");
    }

    /**
     * Test that getFilename returns the correct filename for a relative path.
     * User configurations often use relative paths to project files.
     */
    @Test
    public void testUserProGuardConfiguration_getFilename_withRelativePath() {
        // Given: A UserProGuardConfiguration with a relative path
        String filename = "config/proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the full relative path
        assertEquals(filename, result, "getFilename() should return the relative path");
    }

    /**
     * Test that getFilename returns the correct filename for an absolute path.
     * User configurations can use absolute paths to external configuration files.
     */
    @Test
    public void testUserProGuardConfiguration_getFilename_withAbsolutePath() {
        // Given: A UserProGuardConfiguration with an absolute path
        String filename = "/etc/proguard/proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the absolute path
        assertEquals(filename, result, "getFilename() should return the absolute path");
    }

    /**
     * Test that getFilename returns simple filename without path.
     * Some configurations use just the filename without any path.
     */
    @Test
    public void testUserProGuardConfiguration_getFilename_simpleFilename() {
        // Given: A UserProGuardConfiguration with just a filename
        String filename = "rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return just the filename
        assertEquals(filename, result, "getFilename() should return the simple filename");
    }

    /**
     * Test that getFilename handles filenames with spaces.
     * Configuration files might have spaces in their names.
     */
    @Test
    public void testUserProGuardConfiguration_getFilename_withSpaces() {
        // Given: A UserProGuardConfiguration with spaces in the filename
        String filename = "proguard rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the filename with spaces
        assertEquals(filename, result, "getFilename() should handle filenames with spaces");
    }

    /**
     * Test that getFilename handles filenames with special characters.
     * Configuration files might contain special characters.
     */
    @Test
    public void testUserProGuardConfiguration_getFilename_withSpecialCharacters() {
        // Given: A UserProGuardConfiguration with special characters
        String filename = "proguard-rules_v2.0.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the filename with special characters
        assertEquals(filename, result, "getFilename() should handle special characters");
    }

    /**
     * Test that getFilename returns empty string when given empty string.
     * Edge case testing for empty filename.
     */
    @Test
    public void testUserProGuardConfiguration_getFilename_emptyString() {
        // Given: A UserProGuardConfiguration with an empty filename
        String filename = "";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return empty string
        assertEquals(filename, result, "getFilename() should return empty string when given empty string");
    }

    /**
     * Test that getFilename returns the same value on multiple calls.
     * The filename should be immutable and consistent.
     */
    @Test
    public void testUserProGuardConfiguration_getFilename_consistentResults() {
        // Given: A UserProGuardConfiguration
        String filename = "proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename multiple times
        String result1 = config.getFilename();
        String result2 = config.getFilename();
        String result3 = config.getFilename();

        // Then: All results should be equal
        assertEquals(result1, result2, "getFilename() should return consistent results");
        assertEquals(result2, result3, "getFilename() should return consistent results");
        assertEquals(filename, result1, "getFilename() should always return the same value");
    }

    /**
     * Test that getFilename returns non-null value.
     * The filename should never be null for a valid configuration.
     */
    @Test
    public void testUserProGuardConfiguration_getFilename_notNull() {
        // Given: A UserProGuardConfiguration
        String filename = "proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should not be null
        assertNotNull(result, "getFilename() should not return null");
    }

    /**
     * Test that getFilename handles Windows-style paths.
     * Windows systems use backslashes in paths.
     */
    @Test
    public void testUserProGuardConfiguration_getFilename_windowsPath() {
        // Given: A UserProGuardConfiguration with Windows-style path
        String filename = "C:\\projects\\app\\proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the Windows path as-is
        assertEquals(filename, result, "getFilename() should handle Windows paths");
    }

    // ==================== Tests for DefaultProGuardConfiguration.getFilename() ====================

    /**
     * Test that getFilename returns correct value for Android debug configuration.
     * The default Android debug configuration should have a specific filename.
     */
    @Test
    public void testDefaultProGuardConfiguration_getFilename_androidDebug() {
        // Given: A DefaultProGuardConfiguration for Android debug
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the Android debug filename
        assertEquals("proguard-android-debug.txt", result,
            "getFilename() should return 'proguard-android-debug.txt' for Android debug");
    }

    /**
     * Test that getFilename returns correct value for Android release configuration.
     * The default Android release configuration should have a specific filename.
     */
    @Test
    public void testDefaultProGuardConfiguration_getFilename_androidRelease() {
        // Given: A DefaultProGuardConfiguration for Android release
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the Android release filename
        assertEquals("proguard-android.txt", result,
            "getFilename() should return 'proguard-android.txt' for Android release");
    }

    /**
     * Test that getFilename returns correct value for Android optimized configuration.
     * The default Android optimized configuration should have a specific filename.
     */
    @Test
    public void testDefaultProGuardConfiguration_getFilename_androidOptimize() {
        // Given: A DefaultProGuardConfiguration for Android optimized
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the Android optimize filename
        assertEquals("proguard-android-optimize.txt", result,
            "getFilename() should return 'proguard-android-optimize.txt' for Android optimize");
    }

    /**
     * Test that getFilename is consistent for the same default configuration.
     * Multiple calls should return the same value.
     */
    @Test
    public void testDefaultProGuardConfiguration_getFilename_consistentResults() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting the filename multiple times
        String result1 = config.getFilename();
        String result2 = config.getFilename();
        String result3 = config.getFilename();

        // Then: All results should be equal
        assertEquals(result1, result2, "getFilename() should return consistent results");
        assertEquals(result2, result3, "getFilename() should return consistent results");
    }

    /**
     * Test that getFilename returns non-null for default configurations.
     * Default configuration filenames should never be null.
     */
    @Test
    public void testDefaultProGuardConfiguration_getFilename_notNull() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should not be null
        assertNotNull(result, "getFilename() should not return null");
    }

    /**
     * Test that getFilename returns only the filename, not the full path.
     * For DefaultProGuardConfiguration, getFilename() returns just the filename,
     * while getPath() returns the full path with prefix.
     */
    @Test
    public void testDefaultProGuardConfiguration_getFilename_returnsFilenameNotPath() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting the filename
        String filename = config.getFilename();

        // Then: Should return just the filename without path prefix
        assertEquals("proguard-android.txt", filename,
            "getFilename() should return just the filename, not the full path");
        assertFalse(filename.contains("/"), "Filename should not contain path separators");
    }

    // ==================== Comparison Tests ====================

    /**
     * Test that getFilename is different between user and default configurations.
     * User and default configurations should have distinct filenames.
     */
    @Test
    public void testGetFilename_differentBetweenUserAndDefault() {
        // Given: A user configuration and a default configuration
        UserProGuardConfiguration userConfig = new UserProGuardConfiguration("custom-rules.pro");
        ProGuardConfiguration defaultConfig = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting filenames
        String userFilename = userConfig.getFilename();
        String defaultFilename = defaultConfig.getFilename();

        // Then: Filenames should be different
        assertNotEquals(userFilename, defaultFilename,
            "User and default configurations should have different filenames");
    }

    /**
     * Test that getFilename distinguishes between different default configurations.
     * Each default configuration type should have a unique filename.
     */
    @Test
    public void testGetFilename_uniqueForEachDefaultConfiguration() {
        // Given: Three different default configurations
        ProGuardConfiguration debug = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");
        ProGuardConfiguration release = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");
        ProGuardConfiguration optimize = DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt");

        // When: Getting filenames
        String debugFilename = debug.getFilename();
        String releaseFilename = release.getFilename();
        String optimizeFilename = optimize.getFilename();

        // Then: All filenames should be different
        assertNotEquals(debugFilename, releaseFilename, "Debug and release should have different filenames");
        assertNotEquals(releaseFilename, optimizeFilename, "Release and optimize should have different filenames");
        assertNotEquals(debugFilename, optimizeFilename, "Debug and optimize should have different filenames");
    }

    /**
     * Test that getFilename is used by toString() method.
     * The toString() method should use the filename value.
     */
    @Test
    public void testGetFilename_usedByToString() {
        // Given: A UserProGuardConfiguration
        String filename = "proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting filename and toString
        String filenameResult = config.getFilename();
        String toStringResult = config.toString();

        // Then: toString should equal getFilename
        assertEquals(filenameResult, toStringResult,
            "toString() should return the same value as getFilename()");
    }

    // ==================== Realistic Usage Scenarios ====================

    /**
     * Test getFilename for a typical Android app configuration.
     * Android apps commonly use custom ProGuard rules files.
     */
    @Test
    public void testGetFilename_typicalAndroidAppConfiguration() {
        // Given: A typical Android app ProGuard configuration
        UserProGuardConfiguration config = new UserProGuardConfiguration("proguard-rules.pro");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the standard Android filename
        assertEquals("proguard-rules.pro", result,
            "Should handle typical Android app configuration filename");
    }

    /**
     * Test getFilename for a library module configuration.
     * Library modules often have different ProGuard rules.
     */
    @Test
    public void testGetFilename_libraryModuleConfiguration() {
        // Given: A library module ProGuard configuration
        UserProGuardConfiguration config = new UserProGuardConfiguration("proguard-rules-lib.pro");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the library configuration filename
        assertEquals("proguard-rules-lib.pro", result,
            "Should handle library module configuration filename");
    }

    /**
     * Test getFilename for a variant-specific configuration.
     * Different build variants may have different ProGuard rules.
     */
    @Test
    public void testGetFilename_variantSpecificConfiguration() {
        // Given: A variant-specific ProGuard configuration
        UserProGuardConfiguration config = new UserProGuardConfiguration("proguard-rules-staging.pro");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the variant-specific filename
        assertEquals("proguard-rules-staging.pro", result,
            "Should handle variant-specific configuration filename");
    }

    /**
     * Test getFilename for a shared configuration file.
     * Projects might use shared configuration files across modules.
     */
    @Test
    public void testGetFilename_sharedConfiguration() {
        // Given: A shared ProGuard configuration in a parent directory
        UserProGuardConfiguration config = new UserProGuardConfiguration("../shared/proguard-common.pro");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the path to the shared file
        assertEquals("../shared/proguard-common.pro", result,
            "Should handle shared configuration path");
    }

    /**
     * Test getFilename for default Android debug build.
     * Debug builds typically use the default debug configuration.
     */
    @Test
    public void testGetFilename_defaultAndroidDebugBuild() {
        // Given: A default Android debug configuration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the default debug filename
        assertEquals("proguard-android-debug.txt", result,
            "Should return default Android debug configuration filename");
    }

    /**
     * Test getFilename for default Android release build.
     * Release builds typically use the default release configuration.
     */
    @Test
    public void testGetFilename_defaultAndroidReleaseBuild() {
        // Given: A default Android release configuration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the default release filename
        assertEquals("proguard-android.txt", result,
            "Should return default Android release configuration filename");
    }

    /**
     * Test getFilename for optimized release build.
     * Optimized release builds use the optimize configuration.
     */
    @Test
    public void testGetFilename_optimizedReleaseBuild() {
        // Given: A default Android optimize configuration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the optimize filename
        assertEquals("proguard-android-optimize.txt", result,
            "Should return default Android optimize configuration filename");
    }

    // ==================== Type Safety Tests ====================

    /**
     * Test that getFilename returns a String type.
     * The return type should be String, not Object or any other type.
     */
    @Test
    public void testGetFilename_returnsStringType() {
        // Given: A ProGuardConfiguration
        UserProGuardConfiguration config = new UserProGuardConfiguration("test.pro");

        // When: Getting the filename
        Object result = config.getFilename();

        // Then: Should be a String instance
        assertInstanceOf(String.class, result,
            "getFilename() should return a String instance");
    }

    /**
     * Test that getFilename from parent class reference works correctly.
     * Polymorphism should work correctly with the ProGuardConfiguration base class.
     */
    @Test
    public void testGetFilename_polymorphism() {
        // Given: ProGuardConfiguration references to subclass instances
        ProGuardConfiguration userConfig = new UserProGuardConfiguration("user-rules.pro");
        ProGuardConfiguration defaultConfig = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting filenames via parent class reference
        String userFilename = userConfig.getFilename();
        String defaultFilename = defaultConfig.getFilename();

        // Then: Should return correct values
        assertEquals("user-rules.pro", userFilename,
            "Polymorphic call should work for UserProGuardConfiguration");
        assertEquals("proguard-android.txt", defaultFilename,
            "Polymorphic call should work for DefaultProGuardConfiguration");
    }

    // ==================== Edge Cases ====================

    /**
     * Test getFilename with filename containing only extension.
     * Some files might have unusual naming patterns.
     */
    @Test
    public void testGetFilename_onlyExtension() {
        // Given: A configuration with only extension
        UserProGuardConfiguration config = new UserProGuardConfiguration(".pro");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the extension-only filename
        assertEquals(".pro", result, "Should handle filename with only extension");
    }

    /**
     * Test getFilename with filename containing no extension.
     * Configuration files might not have extensions.
     */
    @Test
    public void testGetFilename_noExtension() {
        // Given: A configuration with no extension
        UserProGuardConfiguration config = new UserProGuardConfiguration("proguard-rules");

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the filename without extension
        assertEquals("proguard-rules", result, "Should handle filename without extension");
    }

    /**
     * Test getFilename with very long filename.
     * Long filenames should be handled correctly.
     */
    @Test
    public void testGetFilename_longFilename() {
        // Given: A configuration with a very long filename
        String longFilename = "proguard-rules-for-production-release-build-with-optimization-enabled.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(longFilename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should return the full long filename
        assertEquals(longFilename, result, "Should handle long filenames");
    }

    /**
     * Test getFilename with Unicode characters in filename.
     * Filenames might contain Unicode characters.
     */
    @Test
    public void testGetFilename_unicodeCharacters() {
        // Given: A configuration with Unicode characters
        String filename = "proguard-规则.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should handle Unicode characters
        assertEquals(filename, result, "Should handle Unicode characters in filename");
    }

    /**
     * Test getFilename with multiple dots in filename.
     * Filenames can have multiple dots.
     */
    @Test
    public void testGetFilename_multipleDotsInFilename() {
        // Given: A configuration with multiple dots
        String filename = "proguard.rules.v2.0.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the filename
        String result = config.getFilename();

        // Then: Should preserve all dots
        assertEquals(filename, result, "Should handle multiple dots in filename");
    }
}

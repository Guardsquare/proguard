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
 * Test class for proguard.gradle.plugin.android.dsl.ProGuardConfiguration.getPath()
 *
 * Tests the getPath() method which returns the path property of ProGuardConfiguration
 * and its subclasses (UserProGuardConfiguration and DefaultProGuardConfiguration).
 *
 * The path property differs between configuration types:
 * - UserProGuardConfiguration: path equals filename (user-specified file location)
 * - DefaultProGuardConfiguration: path equals "/lib/{filename}" (bundled resource location)
 */
public class ProGuardConfigurationClaude_getPathTest {

    private static final String DEFAULT_CONFIG_RESOURCE_PREFIX = "/lib";

    // ==================== Tests for UserProGuardConfiguration.getPath() ====================

    /**
     * Test that getPath returns the same value as filename for user configurations.
     * UserProGuardConfiguration does not override path, so it uses the base class
     * implementation where path equals filename.
     */
    @Test
    public void testUserProGuardConfiguration_getPath_equalsFilename() {
        // Given: A UserProGuardConfiguration with a specific filename
        String filename = "proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should equal filename
        assertEquals(filename, path, "For UserProGuardConfiguration, path should equal filename");
        assertEquals(config.getFilename(), path, "getPath() should return the same as getFilename()");
    }

    /**
     * Test that getPath returns relative path for user configurations.
     * User configurations with relative paths should preserve the path structure.
     */
    @Test
    public void testUserProGuardConfiguration_getPath_withRelativePath() {
        // Given: A UserProGuardConfiguration with a relative path
        String filename = "config/proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should equal the relative path
        assertEquals(filename, path, "Path should preserve relative path structure");
        assertEquals("config/proguard-rules.pro", path);
    }

    /**
     * Test that getPath returns absolute path for user configurations.
     * User configurations with absolute paths should preserve the full path.
     */
    @Test
    public void testUserProGuardConfiguration_getPath_withAbsolutePath() {
        // Given: A UserProGuardConfiguration with an absolute path
        String filename = "/etc/proguard/proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should equal the absolute path
        assertEquals(filename, path, "Path should preserve absolute path structure");
        assertEquals("/etc/proguard/proguard-rules.pro", path);
    }

    /**
     * Test that getPath returns simple filename for user configurations.
     * User configurations with just a filename should return the filename as path.
     */
    @Test
    public void testUserProGuardConfiguration_getPath_simpleFilename() {
        // Given: A UserProGuardConfiguration with just a filename
        String filename = "rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should equal the filename
        assertEquals(filename, path, "Path should equal filename for simple filenames");
    }

    /**
     * Test that getPath handles empty string for user configurations.
     * Edge case testing for empty filename.
     */
    @Test
    public void testUserProGuardConfiguration_getPath_emptyString() {
        // Given: A UserProGuardConfiguration with an empty filename
        String filename = "";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should be empty string
        assertEquals("", path, "Path should be empty string when filename is empty");
    }

    /**
     * Test that getPath returns the same value on multiple calls.
     * The path should be immutable and consistent.
     */
    @Test
    public void testUserProGuardConfiguration_getPath_consistentResults() {
        // Given: A UserProGuardConfiguration
        String filename = "proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the path multiple times
        String path1 = config.getPath();
        String path2 = config.getPath();
        String path3 = config.getPath();

        // Then: All results should be equal
        assertEquals(path1, path2, "getPath() should return consistent results");
        assertEquals(path2, path3, "getPath() should return consistent results");
    }

    /**
     * Test that getPath returns non-null value.
     * The path should never be null for a valid configuration.
     */
    @Test
    public void testUserProGuardConfiguration_getPath_notNull() {
        // Given: A UserProGuardConfiguration
        String filename = "proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the path
        String path = config.getPath();

        // Then: Should not be null
        assertNotNull(path, "getPath() should not return null");
    }

    /**
     * Test that getPath handles Windows-style paths for user configurations.
     * Windows systems use backslashes in paths.
     */
    @Test
    public void testUserProGuardConfiguration_getPath_windowsPath() {
        // Given: A UserProGuardConfiguration with Windows-style path
        String filename = "C:\\projects\\app\\proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(filename);

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should equal the Windows path
        assertEquals(filename, path, "Path should preserve Windows path structure");
    }

    // ==================== Tests for DefaultProGuardConfiguration.getPath() ====================

    /**
     * Test that getPath returns prefixed path for Android debug configuration.
     * DefaultProGuardConfiguration should prepend the resource prefix to the filename.
     */
    @Test
    public void testDefaultProGuardConfiguration_getPath_androidDebug() {
        // Given: A DefaultProGuardConfiguration for Android debug
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should be prefixed with /lib
        assertEquals("/lib/proguard-android-debug.txt", path,
            "Path for default configuration should be prefixed with /lib");
        assertTrue(path.startsWith(DEFAULT_CONFIG_RESOURCE_PREFIX),
            "Path should start with DEFAULT_CONFIG_RESOURCE_PREFIX");
    }

    /**
     * Test that getPath returns prefixed path for Android release configuration.
     * The default Android release configuration should have the resource prefix.
     */
    @Test
    public void testDefaultProGuardConfiguration_getPath_androidRelease() {
        // Given: A DefaultProGuardConfiguration for Android release
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should be prefixed with /lib
        assertEquals("/lib/proguard-android.txt", path,
            "Path for default configuration should be prefixed with /lib");
    }

    /**
     * Test that getPath returns prefixed path for Android optimize configuration.
     * The default Android optimize configuration should have the resource prefix.
     */
    @Test
    public void testDefaultProGuardConfiguration_getPath_androidOptimize() {
        // Given: A DefaultProGuardConfiguration for Android optimize
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should be prefixed with /lib
        assertEquals("/lib/proguard-android-optimize.txt", path,
            "Path for default configuration should be prefixed with /lib");
    }

    /**
     * Test that getPath differs from getFilename for default configurations.
     * DefaultProGuardConfiguration overrides path to add prefix, but filename remains unchanged.
     */
    @Test
    public void testDefaultProGuardConfiguration_getPath_differsFromFilename() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting path and filename
        String path = config.getPath();
        String filename = config.getFilename();

        // Then: Path and filename should be different
        assertNotEquals(path, filename, "Path should differ from filename for default configurations");
        assertEquals("proguard-android.txt", filename, "Filename should not have prefix");
        assertEquals("/lib/proguard-android.txt", path, "Path should have /lib prefix");
    }

    /**
     * Test that getPath is consistent for default configurations.
     * Multiple calls should return the same value.
     */
    @Test
    public void testDefaultProGuardConfiguration_getPath_consistentResults() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting the path multiple times
        String path1 = config.getPath();
        String path2 = config.getPath();
        String path3 = config.getPath();

        // Then: All results should be equal
        assertEquals(path1, path2, "getPath() should return consistent results");
        assertEquals(path2, path3, "getPath() should return consistent results");
    }

    /**
     * Test that getPath returns non-null for default configurations.
     * Default configuration paths should never be null.
     */
    @Test
    public void testDefaultProGuardConfiguration_getPath_notNull() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting the path
        String path = config.getPath();

        // Then: Should not be null
        assertNotNull(path, "getPath() should not return null");
    }

    /**
     * Test that getPath contains the filename at the end.
     * The path should end with the filename.
     */
    @Test
    public void testDefaultProGuardConfiguration_getPath_endsWithFilename() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting path and filename
        String path = config.getPath();
        String filename = config.getFilename();

        // Then: Path should end with filename
        assertTrue(path.endsWith(filename), "Path should end with filename");
    }

    /**
     * Test that getPath uses forward slash separator.
     * Resource paths should use forward slashes regardless of platform.
     */
    @Test
    public void testDefaultProGuardConfiguration_getPath_usesForwardSlash() {
        // Given: A DefaultProGuardConfiguration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting the path
        String path = config.getPath();

        // Then: Should use forward slash
        assertTrue(path.contains("/"), "Path should contain forward slash");
        assertFalse(path.contains("\\"), "Path should not contain backslash");
    }

    // ==================== Comparison Tests ====================

    /**
     * Test that getPath is different between user and default configurations.
     * User configurations use the filename as path, default configurations use prefixed path.
     */
    @Test
    public void testGetPath_differentBetweenUserAndDefault() {
        // Given: A user configuration and a default configuration with similar filenames
        UserProGuardConfiguration userConfig = new UserProGuardConfiguration("proguard-android.txt");
        ProGuardConfiguration defaultConfig = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting paths
        String userPath = userConfig.getPath();
        String defaultPath = defaultConfig.getPath();

        // Then: Paths should be different
        assertNotEquals(userPath, defaultPath,
            "User and default configurations should have different paths");
        assertEquals("proguard-android.txt", userPath, "User config path should equal filename");
        assertEquals("/lib/proguard-android.txt", defaultPath, "Default config path should have prefix");
    }

    /**
     * Test that getPath is unique for each default configuration.
     * Each default configuration type should have a unique path.
     */
    @Test
    public void testGetPath_uniqueForEachDefaultConfiguration() {
        // Given: Three different default configurations
        ProGuardConfiguration debug = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");
        ProGuardConfiguration release = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");
        ProGuardConfiguration optimize = DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt");

        // When: Getting paths
        String debugPath = debug.getPath();
        String releasePath = release.getPath();
        String optimizePath = optimize.getPath();

        // Then: All paths should be different
        assertNotEquals(debugPath, releasePath, "Debug and release should have different paths");
        assertNotEquals(releasePath, optimizePath, "Release and optimize should have different paths");
        assertNotEquals(debugPath, optimizePath, "Debug and optimize should have different paths");
    }

    /**
     * Test that getPath for user config equals getFilename but differs for default config.
     * This demonstrates the key difference between the two configuration types.
     */
    @Test
    public void testGetPath_relationshipWithFilename() {
        // Given: A user configuration and a default configuration
        UserProGuardConfiguration userConfig = new UserProGuardConfiguration("custom.pro");
        ProGuardConfiguration defaultConfig = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Comparing path and filename for each
        assertEquals(userConfig.getPath(), userConfig.getFilename(),
            "For UserProGuardConfiguration, path should equal filename");
        assertNotEquals(defaultConfig.getPath(), defaultConfig.getFilename(),
            "For DefaultProGuardConfiguration, path should differ from filename");
    }

    // ==================== Realistic Usage Scenarios ====================

    /**
     * Test getPath for a typical Android app user configuration.
     * User configuration paths are typically relative to the project directory.
     */
    @Test
    public void testGetPath_typicalAndroidAppConfiguration() {
        // Given: A typical Android app ProGuard configuration
        UserProGuardConfiguration config = new UserProGuardConfiguration("proguard-rules.pro");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should be the filename (relative to project)
        assertEquals("proguard-rules.pro", path,
            "User config path should be relative to project directory");
    }

    /**
     * Test getPath for a default Android debug build.
     * Debug builds use the default debug configuration from bundled resources.
     */
    @Test
    public void testGetPath_defaultAndroidDebugBuild() {
        // Given: A default Android debug configuration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should point to bundled resource
        assertEquals("/lib/proguard-android-debug.txt", path,
            "Default config path should point to bundled resource location");
    }

    /**
     * Test getPath for a default Android release build.
     * Release builds use the default release configuration from bundled resources.
     */
    @Test
    public void testGetPath_defaultAndroidReleaseBuild() {
        // Given: A default Android release configuration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should point to bundled resource
        assertEquals("/lib/proguard-android.txt", path,
            "Default config path should point to bundled resource location");
    }

    /**
     * Test getPath for an optimized release build.
     * Optimized builds use the optimize configuration from bundled resources.
     */
    @Test
    public void testGetPath_optimizedReleaseBuild() {
        // Given: A default Android optimize configuration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should point to bundled resource
        assertEquals("/lib/proguard-android-optimize.txt", path,
            "Optimize config path should point to bundled resource location");
    }

    /**
     * Test getPath for a library module user configuration.
     * Library modules often have separate ProGuard rules files.
     */
    @Test
    public void testGetPath_libraryModuleConfiguration() {
        // Given: A library module ProGuard configuration
        UserProGuardConfiguration config = new UserProGuardConfiguration("consumer-rules.pro");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should be the consumer rules filename
        assertEquals("consumer-rules.pro", path,
            "Library module path should be relative to module directory");
    }

    /**
     * Test getPath for a variant-specific user configuration.
     * Different build variants may have different ProGuard rules in subdirectories.
     */
    @Test
    public void testGetPath_variantSpecificConfiguration() {
        // Given: A variant-specific ProGuard configuration in a subdirectory
        UserProGuardConfiguration config = new UserProGuardConfiguration("staging/proguard-rules.pro");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should include the variant subdirectory
        assertEquals("staging/proguard-rules.pro", path,
            "Variant-specific path should include subdirectory");
    }

    /**
     * Test getPath for a shared configuration file across modules.
     * Projects might use shared configuration files in parent directories.
     */
    @Test
    public void testGetPath_sharedConfiguration() {
        // Given: A shared ProGuard configuration in a parent directory
        UserProGuardConfiguration config = new UserProGuardConfiguration("../shared/proguard-common.pro");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should include the parent directory reference
        assertEquals("../shared/proguard-common.pro", path,
            "Shared config path should preserve relative path to parent directory");
    }

    // ==================== Type Safety Tests ====================

    /**
     * Test that getPath returns a String type.
     * The return type should be String, not Object or any other type.
     */
    @Test
    public void testGetPath_returnsStringType() {
        // Given: A ProGuardConfiguration
        UserProGuardConfiguration config = new UserProGuardConfiguration("test.pro");

        // When: Getting the path
        Object path = config.getPath();

        // Then: Should be a String instance
        assertInstanceOf(String.class, path,
            "getPath() should return a String instance");
    }

    /**
     * Test that getPath from parent class reference works correctly.
     * Polymorphism should work correctly with the ProGuardConfiguration base class.
     */
    @Test
    public void testGetPath_polymorphism() {
        // Given: ProGuardConfiguration references to subclass instances
        ProGuardConfiguration userConfig = new UserProGuardConfiguration("user-rules.pro");
        ProGuardConfiguration defaultConfig = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting paths via parent class reference
        String userPath = userConfig.getPath();
        String defaultPath = defaultConfig.getPath();

        // Then: Should return correct values based on actual type
        assertEquals("user-rules.pro", userPath,
            "Polymorphic call should work for UserProGuardConfiguration");
        assertEquals("/lib/proguard-android.txt", defaultPath,
            "Polymorphic call should work for DefaultProGuardConfiguration");
    }

    // ==================== Edge Cases ====================

    /**
     * Test getPath with relative path containing dots.
     * Paths with parent directory references should be preserved.
     */
    @Test
    public void testGetPath_relativePathWithDots() {
        // Given: A configuration with parent directory reference
        UserProGuardConfiguration config = new UserProGuardConfiguration("../../common/rules.pro");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should preserve the dot notation
        assertEquals("../../common/rules.pro", path,
            "Path should preserve parent directory references");
    }

    /**
     * Test getPath with complex nested path.
     * Deeply nested paths should be handled correctly.
     */
    @Test
    public void testGetPath_nestedPath() {
        // Given: A configuration with nested path
        UserProGuardConfiguration config = new UserProGuardConfiguration("config/proguard/variants/release/rules.pro");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should preserve the full nested structure
        assertEquals("config/proguard/variants/release/rules.pro", path,
            "Path should preserve nested directory structure");
    }

    /**
     * Test getPath with path containing spaces.
     * Paths with spaces should be handled correctly.
     */
    @Test
    public void testGetPath_pathWithSpaces() {
        // Given: A configuration with spaces in path
        UserProGuardConfiguration config = new UserProGuardConfiguration("my config/proguard rules.pro");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should preserve spaces
        assertEquals("my config/proguard rules.pro", path,
            "Path should handle spaces correctly");
    }

    /**
     * Test getPath with path containing special characters.
     * Paths with special characters should be preserved.
     */
    @Test
    public void testGetPath_pathWithSpecialCharacters() {
        // Given: A configuration with special characters
        UserProGuardConfiguration config = new UserProGuardConfiguration("config/proguard-rules_v2.0.pro");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should preserve special characters
        assertEquals("config/proguard-rules_v2.0.pro", path,
            "Path should handle special characters");
    }

    /**
     * Test getPath with very long path.
     * Long paths should be handled correctly.
     */
    @Test
    public void testGetPath_longPath() {
        // Given: A configuration with a very long path
        String longPath = "very/long/path/to/configuration/files/for/proguard/rules/in/production/release/build/proguard-rules.pro";
        UserProGuardConfiguration config = new UserProGuardConfiguration(longPath);

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should be preserved fully
        assertEquals(longPath, path, "Path should handle long paths");
    }

    /**
     * Test getPath with Unicode characters in path.
     * Paths with Unicode characters should be handled correctly.
     */
    @Test
    public void testGetPath_unicodeInPath() {
        // Given: A configuration with Unicode characters
        UserProGuardConfiguration config = new UserProGuardConfiguration("配置/proguard-规则.pro");

        // When: Getting the path
        String path = config.getPath();

        // Then: Path should handle Unicode characters
        assertEquals("配置/proguard-规则.pro", path,
            "Path should handle Unicode characters");
    }

    // ==================== Path Format Tests ====================

    /**
     * Test that default configuration paths always start with /lib.
     * This verifies the consistent resource location prefix.
     */
    @Test
    public void testDefaultConfiguration_getPath_alwaysStartsWithLibPrefix() {
        // Given: All three default configurations
        ProGuardConfiguration debug = DefaultProGuardConfiguration.Companion.fromString("proguard-android-debug.txt");
        ProGuardConfiguration release = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");
        ProGuardConfiguration optimize = DefaultProGuardConfiguration.Companion.fromString("proguard-android-optimize.txt");

        // When/Then: All paths should start with /lib
        assertTrue(debug.getPath().startsWith("/lib/"),
            "Debug config path should start with /lib/");
        assertTrue(release.getPath().startsWith("/lib/"),
            "Release config path should start with /lib/");
        assertTrue(optimize.getPath().startsWith("/lib/"),
            "Optimize config path should start with /lib/");
    }

    /**
     * Test that default configuration paths have exactly one slash separator.
     * The format should be /lib/{filename} with no additional slashes.
     */
    @Test
    public void testDefaultConfiguration_getPath_formatIsLibSlashFilename() {
        // Given: A default configuration
        ProGuardConfiguration config = DefaultProGuardConfiguration.Companion.fromString("proguard-android.txt");

        // When: Getting the path
        String path = config.getPath();

        // Then: Should have format /lib/{filename}
        assertTrue(path.matches("^/lib/[^/]+$"),
            "Path should match format /lib/{filename}");
    }

    /**
     * Test that user configuration paths do not have /lib prefix.
     * User configurations should never use the bundled resource prefix.
     */
    @Test
    public void testUserConfiguration_getPath_doesNotHaveLibPrefix() {
        // Given: Various user configurations
        UserProGuardConfiguration config1 = new UserProGuardConfiguration("proguard-rules.pro");
        UserProGuardConfiguration config2 = new UserProGuardConfiguration("config/rules.pro");
        UserProGuardConfiguration config3 = new UserProGuardConfiguration("/absolute/path/rules.pro");

        // When/Then: None should start with /lib
        assertFalse(config1.getPath().startsWith("/lib"),
            "User config should not have /lib prefix");
        assertFalse(config2.getPath().startsWith("/lib"),
            "User config with subdirectory should not have /lib prefix");
        // config3 starts with / but not with /lib/
        assertFalse(config3.getPath().startsWith("/lib/"),
            "User config with absolute path should not have /lib/ prefix");
    }
}

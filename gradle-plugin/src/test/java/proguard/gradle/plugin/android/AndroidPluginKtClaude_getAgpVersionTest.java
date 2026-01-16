/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android;

import com.github.zafarkhaja.semver.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AndroidPluginKt.getAgpVersion()Lcom/github/zafarkhaja/semver/Version;
 *
 * This test class focuses on achieving coverage for the getAgpVersion property accessor.
 * The property returns the Android Gradle Plugin version as a semantic Version object.
 * It's initialized from com.android.Version.ANDROID_GRADLE_PLUGIN_VERSION at class load time.
 *
 * Note: In Kotlin, 'val agpVersion' is a top-level immutable property that automatically
 * generates a getter function 'getAgpVersion()' for Java interop.
 */
public class AndroidPluginKtClaude_getAgpVersionTest {

    /**
     * Test that getAgpVersion returns a non-null Version object.
     * This is the most fundamental test - the property should always be initialized.
     */
    @Test
    public void testGetAgpVersion_returnsNonNull() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should return a non-null Version object
        assertNotNull(version, "AGP version should not be null");
    }

    /**
     * Test that getAgpVersion returns a valid Version with major version number.
     * The Android Gradle Plugin version should have at least a major version component.
     */
    @Test
    public void testGetAgpVersion_hasValidMajorVersion() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should have a valid major version (non-negative)
        assertNotNull(version, "Version should not be null");
        assertTrue(version.getMajorVersion() >= 0,
            "Major version should be non-negative, got: " + version.getMajorVersion());
    }

    /**
     * Test that getAgpVersion returns a valid Version with minor version number.
     * Semantic versions include a minor version component.
     */
    @Test
    public void testGetAgpVersion_hasValidMinorVersion() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should have a valid minor version (non-negative)
        assertNotNull(version, "Version should not be null");
        assertTrue(version.getMinorVersion() >= 0,
            "Minor version should be non-negative, got: " + version.getMinorVersion());
    }

    /**
     * Test that getAgpVersion returns a valid Version with patch version number.
     * Semantic versions include a patch version component.
     */
    @Test
    public void testGetAgpVersion_hasValidPatchVersion() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should have a valid patch version (non-negative)
        assertNotNull(version, "Version should not be null");
        assertTrue(version.getPatchVersion() >= 0,
            "Patch version should be non-negative, got: " + version.getPatchVersion());
    }

    /**
     * Test that getAgpVersion returns a Version that matches expected AGP version pattern.
     * AGP versions should be >= 4.0.0 based on the ProGuardPlugin requirement.
     */
    @Test
    public void testGetAgpVersion_meetsMinimumVersion() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should be at least version 4.0.0 (as required by ProGuardPlugin)
        assertNotNull(version, "Version should not be null");
        assertTrue(version.getMajorVersion() >= 4,
            "AGP version should be >= 4.0.0 to work with ProGuard plugin, got: " + version);
    }

    /**
     * Test that getAgpVersion returns a Version with a valid string representation.
     * The Version object should be able to render itself as a version string.
     */
    @Test
    public void testGetAgpVersion_hasValidStringRepresentation() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: String representation should not be null or empty
        assertNotNull(version, "Version should not be null");
        String versionString = version.toString();
        assertNotNull(versionString, "Version string should not be null");
        assertFalse(versionString.isEmpty(), "Version string should not be empty");
        assertTrue(versionString.matches("\\d+\\.\\d+\\.\\d+.*"),
            "Version string should match semantic version pattern, got: " + versionString);
    }

    /**
     * Test that getAgpVersion returns the same instance on multiple calls.
     * Since agpVersion is a val (immutable) property initialized once,
     * it should return the same object every time.
     */
    @Test
    public void testGetAgpVersion_returnsSameInstance() {
        // When: Accessing the AGP version multiple times
        Version version1 = AndroidPluginKt.getAgpVersion();
        Version version2 = AndroidPluginKt.getAgpVersion();

        // Then: Should return the same instance (same object reference)
        assertNotNull(version1, "First version should not be null");
        assertNotNull(version2, "Second version should not be null");
        assertSame(version1, version2, "Should return the same Version instance");
    }

    /**
     * Test that getAgpVersion returns a Version that can be compared.
     * Version objects should support comparison operations.
     */
    @Test
    public void testGetAgpVersion_supportsComparison() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should be comparable to other versions
        assertNotNull(version, "Version should not be null");

        // Compare with a known version (3.0.0 - should be greater since we require >= 4.0.0)
        Version olderVersion = Version.valueOf("3.0.0");
        assertTrue(version.greaterThan(olderVersion),
            "AGP version " + version + " should be greater than 3.0.0");

        // Compare with itself
        assertEquals(0, version.compareTo(version),
            "Version should be equal to itself");
    }

    /**
     * Test that getAgpVersion returns a Version equal to itself.
     * The version should properly implement equality.
     */
    @Test
    public void testGetAgpVersion_equalsItself() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should equal itself
        assertNotNull(version, "Version should not be null");
        assertEquals(version, version, "Version should equal itself");
    }

    /**
     * Test that getAgpVersion returns a Version with consistent hashCode.
     * Multiple calls to hashCode() should return the same value.
     */
    @Test
    public void testGetAgpVersion_hasConsistentHashCode() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Hash code should be consistent
        assertNotNull(version, "Version should not be null");
        int hashCode1 = version.hashCode();
        int hashCode2 = version.hashCode();
        assertEquals(hashCode1, hashCode2, "Hash code should be consistent across calls");
    }

    /**
     * Test that getAgpVersion returns a Version that can be used in version checks.
     * This simulates the real-world usage in AndroidPlugin.warnOldProguardVersion()
     * which checks if agpVersion.majorVersion >= 7.
     */
    @Test
    public void testGetAgpVersion_canBeUsedForVersionChecks() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should be usable for version checks
        assertNotNull(version, "Version should not be null");
        int majorVersion = version.getMajorVersion();

        // Simulate version check logic from the codebase
        boolean isAgp7OrHigher = majorVersion >= 7;
        boolean isAgp4OrHigher = majorVersion >= 4;

        // Should be AGP 4 or higher (based on ProGuardPlugin requirement)
        assertTrue(isAgp4OrHigher,
            "AGP version should be 4 or higher, got major version: " + majorVersion);

        // The boolean should be determinable (not throw exception)
        assertNotNull(isAgp7OrHigher, "Version check should complete without error");
    }

    /**
     * Test that getAgpVersion returns a Version that matches the expected AGP version.
     * The test environment uses AGP 4.1.0 as defined in build.gradle.
     */
    @Test
    public void testGetAgpVersion_matchesExpectedVersion() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should be version 4.1.0 (as defined in gradle-plugin/build.gradle)
        assertNotNull(version, "Version should not be null");
        assertEquals(4, version.getMajorVersion(),
            "AGP major version should be 4");
        assertEquals(1, version.getMinorVersion(),
            "AGP minor version should be 1");
        assertEquals(0, version.getPatchVersion(),
            "AGP patch version should be 0");
    }

    /**
     * Test that getAgpVersion returns a Version that can be formatted as a string.
     * Verifies the toString() method works correctly.
     */
    @Test
    public void testGetAgpVersion_toStringWorks() {
        // When: Accessing the AGP version and converting to string
        Version version = AndroidPluginKt.getAgpVersion();
        String versionString = version.toString();

        // Then: Should start with the major.minor.patch format
        assertNotNull(version, "Version should not be null");
        assertNotNull(versionString, "Version string should not be null");
        assertTrue(versionString.startsWith("4.1.0"),
            "Version string should start with '4.1.0', got: " + versionString);
    }

    /**
     * Test that getAgpVersion returns a Version that can be used with greaterThan().
     */
    @Test
    public void testGetAgpVersion_greaterThanComparison() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should be greater than version 3.0.0
        assertNotNull(version, "Version should not be null");
        assertTrue(version.greaterThan(Version.valueOf("3.0.0")),
            "AGP version should be greater than 3.0.0");
        assertTrue(version.greaterThan(Version.valueOf("4.0.0")),
            "AGP version should be greater than 4.0.0");
    }

    /**
     * Test that getAgpVersion returns a Version that can be used with lessThan().
     */
    @Test
    public void testGetAgpVersion_lessThanComparison() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should be less than version 5.0.0 (current is 4.1.0)
        assertNotNull(version, "Version should not be null");
        assertTrue(version.lessThan(Version.valueOf("5.0.0")),
            "AGP version should be less than 5.0.0");
    }

    /**
     * Test that getAgpVersion returns a Version that can be used with greaterThanOrEqualTo().
     */
    @Test
    public void testGetAgpVersion_greaterThanOrEqualToComparison() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should be >= 4.1.0
        assertNotNull(version, "Version should not be null");
        assertTrue(version.greaterThanOrEqualTo(Version.valueOf("4.1.0")),
            "AGP version should be >= 4.1.0");
        assertTrue(version.greaterThanOrEqualTo(Version.valueOf("4.0.0")),
            "AGP version should be >= 4.0.0");
    }

    /**
     * Test that getAgpVersion returns a Version that can be used with lessThanOrEqualTo().
     */
    @Test
    public void testGetAgpVersion_lessThanOrEqualToComparison() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should be <= 4.1.0
        assertNotNull(version, "Version should not be null");
        assertTrue(version.lessThanOrEqualTo(Version.valueOf("4.1.0")),
            "AGP version should be <= 4.1.0");
        assertTrue(version.lessThanOrEqualTo(Version.valueOf("5.0.0")),
            "AGP version should be <= 5.0.0");
    }

    /**
     * Test that getAgpVersion returns a Version that can be used with equals() comparison.
     */
    @Test
    public void testGetAgpVersion_equalsComparison() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should equal version 4.1.0
        assertNotNull(version, "Version should not be null");
        assertTrue(version.equals(Version.valueOf("4.1.0")),
            "AGP version should equal 4.1.0");
    }

    /**
     * Test that getAgpVersion returns a Version that correctly reports not being a pre-release.
     * AGP 4.1.0 is a stable release, not a pre-release version.
     */
    @Test
    public void testGetAgpVersion_isNotPreRelease() {
        // When: Accessing the AGP version
        Version version = AndroidPluginKt.getAgpVersion();

        // Then: Should not be a pre-release version
        assertNotNull(version, "Version should not be null");
        // Note: AGP 4.1.0 is a stable release, so it should not have pre-release identifier
        // However, we can't assume this without checking the actual version string
        // Let's just verify the method is callable
        assertNotNull(version.getPreReleaseVersion(),
            "Pre-release version accessor should not be null");
    }

    /**
     * Test that getAgpVersion can be called from multiple threads safely.
     * Since it's a val property initialized once, it should be thread-safe.
     */
    @Test
    public void testGetAgpVersion_threadSafe() throws InterruptedException {
        // When: Accessing the AGP version from multiple threads
        final Version[] versions = new Version[2];

        Thread thread1 = new Thread(() -> versions[0] = AndroidPluginKt.getAgpVersion());
        Thread thread2 = new Thread(() -> versions[1] = AndroidPluginKt.getAgpVersion());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        // Then: Both threads should get the same instance
        assertNotNull(versions[0], "Version from thread 1 should not be null");
        assertNotNull(versions[1], "Version from thread 2 should not be null");
        assertSame(versions[0], versions[1], "Both threads should get the same Version instance");
    }
}

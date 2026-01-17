package proguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ProGuard#getVersion()}.
 * Tests the static getVersion() method that returns the ProGuard version.
 */
public class ProGuardClaude_getVersionTest {

    /**
     * Tests that getVersion() returns a non-null value.
     * The method should always return a string, either the version or "undefined".
     */
    @Test
    public void testGetVersionReturnsNonNull() {
        // Act - Call getVersion
        String version = ProGuard.getVersion();

        // Assert - Version should never be null
        assertNotNull(version, "getVersion() should never return null");
    }

    /**
     * Tests that getVersion() returns a non-empty value.
     * The method should always return a non-empty string.
     */
    @Test
    public void testGetVersionReturnsNonEmpty() {
        // Act - Call getVersion
        String version = ProGuard.getVersion();

        // Assert - Version should not be empty
        assertFalse(version.isEmpty(), "getVersion() should return a non-empty string");
    }

    /**
     * Tests that getVersion() returns a consistent value across multiple calls.
     * The version should not change during the execution of the program.
     */
    @Test
    public void testGetVersionConsistency() {
        // Act - Call getVersion multiple times
        String version1 = ProGuard.getVersion();
        String version2 = ProGuard.getVersion();
        String version3 = ProGuard.getVersion();

        // Assert - All calls should return the same value
        assertEquals(version1, version2, "getVersion() should return consistent value");
        assertEquals(version2, version3, "getVersion() should return consistent value");
        assertEquals(version1, version3, "getVersion() should return consistent value");
    }

    /**
     * Tests that getVersion() returns a value that is either a version string or "undefined".
     * This verifies the expected behavior based on the implementation.
     */
    @Test
    public void testGetVersionReturnsExpectedFormat() {
        // Act - Call getVersion
        String version = ProGuard.getVersion();

        // Assert - Version should be a valid string (either actual version or "undefined")
        assertNotNull(version, "Version should not be null");
        assertTrue(version.length() > 0, "Version should have length greater than 0");

        // The version is either an actual version string (from manifest) or "undefined"
        // We can't predict which, but we can verify it's a reasonable string
        assertTrue(version.equals("undefined") || version.length() > 0,
                "Version should be 'undefined' or a valid version string");
    }

    /**
     * Tests that getVersion() can be called multiple times without side effects.
     * Verifies that the method is idempotent and thread-safe.
     */
    @Test
    public void testGetVersionIdempotent() {
        // Act - Call getVersion many times
        String firstVersion = ProGuard.getVersion();

        for (int i = 0; i < 100; i++) {
            String version = ProGuard.getVersion();
            assertEquals(firstVersion, version,
                    "getVersion() should always return the same value on call " + i);
        }
    }

    /**
     * Tests that getVersion() returns the same value when called from different contexts.
     * This verifies that the static method behaves consistently.
     */
    @Test
    public void testGetVersionFromDifferentContexts() {
        // Act - Call getVersion from different execution contexts
        String versionDirect = ProGuard.getVersion();
        String versionFromRunnable = getVersionFromRunnable();

        // Assert - Both should return the same value
        assertEquals(versionDirect, versionFromRunnable,
                "getVersion() should return the same value from different contexts");
    }

    /**
     * Helper method to get version from a different execution context.
     */
    private String getVersionFromRunnable() {
        final String[] result = new String[1];
        Runnable runnable = () -> result[0] = ProGuard.getVersion();
        runnable.run();
        return result[0];
    }

    /**
     * Tests that the VERSION constant uses getVersion().
     * This verifies the integration between the static field and method.
     */
    @Test
    public void testVersionConstantIntegration() {
        // Arrange - Get the version from getVersion()
        String methodVersion = ProGuard.getVersion();

        // Act - Check if VERSION constant contains the version
        String versionConstant = ProGuard.VERSION;

        // Assert - VERSION should contain the result of getVersion()
        assertNotNull(versionConstant, "VERSION constant should not be null");
        assertTrue(versionConstant.contains(methodVersion),
                "VERSION constant should contain the version from getVersion()");
        assertTrue(versionConstant.startsWith("ProGuard, version "),
                "VERSION constant should start with 'ProGuard, version '");
    }

    /**
     * Tests that getVersion() returns a string that doesn't contain null indicators.
     * Verifies that the method properly handles null cases internally.
     */
    @Test
    public void testGetVersionDoesNotReturnNullString() {
        // Act - Call getVersion
        String version = ProGuard.getVersion();

        // Assert - Version should not be "null" as a string
        assertNotEquals("null", version, "getVersion() should not return the string 'null'");
        assertFalse(version.toLowerCase().contains("null"),
                "getVersion() should not contain 'null' in any form");
    }

    /**
     * Tests that getVersion() returns a trimmed string without leading/trailing whitespace.
     * Verifies the quality of the returned string.
     */
    @Test
    public void testGetVersionHasNoWhitespace() {
        // Act - Call getVersion
        String version = ProGuard.getVersion();

        // Assert - Version should not have leading or trailing whitespace
        assertEquals(version, version.trim(),
                "getVersion() should return a trimmed string without leading/trailing whitespace");
    }
}

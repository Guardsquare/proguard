package proguard.gui;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JavaUtil methods.
 *
 * This class tests the following methods from JavaUtil:
 * 1. Constructor (implicit test through static method usage)
 * 2. currentJavaVersion() - Returns the major version of the current Java runtime
 * 3. getCurrentJavaHome() - Returns the Java home directory
 * 4. getRtJar() - Returns the rt.jar file location (for Java 8 and earlier)
 * 5. getJmodBase() - Returns the java.base.jmod file location (for Java 9+)
 *
 * These methods rely on system properties and file system structure, so tests
 * verify behavior based on the actual runtime environment.
 */
public class JavaUtilClaudeTest {

    // ========================================================================
    // Tests for currentJavaVersion()
    // ========================================================================

    /**
     * Test that currentJavaVersion returns a positive integer.
     * The Java version should always be at least 1.
     */
    @Test
    public void testCurrentJavaVersionReturnsPositiveInteger() {
        int version = JavaUtil.currentJavaVersion();
        assertTrue(version > 0, "Java version should be a positive integer");
    }

    /**
     * Test that currentJavaVersion returns a reasonable version number.
     * Java versions should be between 5 and 100 (reasonable upper bound for foreseeable future).
     */
    @Test
    public void testCurrentJavaVersionReturnsReasonableValue() {
        int version = JavaUtil.currentJavaVersion();
        assertTrue(version >= 5 && version <= 100,
                "Java version should be between 5 and 100, got: " + version);
    }

    /**
     * Test that currentJavaVersion is idempotent.
     * Multiple calls should return the same value since the Java version doesn't change during execution.
     */
    @Test
    public void testCurrentJavaVersionIsIdempotent() {
        int version1 = JavaUtil.currentJavaVersion();
        int version2 = JavaUtil.currentJavaVersion();
        int version3 = JavaUtil.currentJavaVersion();

        assertEquals(version1, version2, "Multiple calls should return the same version");
        assertEquals(version2, version3, "Multiple calls should return the same version");
    }

    /**
     * Test that currentJavaVersion correctly parses the java.version system property.
     * This verifies the parsing logic handles the actual runtime version.
     */
    @Test
    public void testCurrentJavaVersionMatchesSystemProperty() {
        String javaVersion = System.getProperty("java.version");
        int parsedVersion = JavaUtil.currentJavaVersion();

        // Verify the parsed version makes sense given the system property
        assertNotNull(javaVersion, "java.version system property should exist");

        // For Java 9+, version starts with the major version (e.g., "11.0.1", "17.0.2")
        // For Java 8 and earlier, version starts with "1.x" (e.g., "1.8.0_292")
        if (javaVersion.startsWith("1.")) {
            // Java 8 or earlier format: 1.x.y_z
            String majorVersionStr = javaVersion.substring(2, 3);
            int expectedVersion = Integer.parseInt(majorVersionStr);
            assertEquals(expectedVersion, parsedVersion,
                    "Parsed version should match the major version from java.version");
        } else {
            // Java 9+ format: x.y.z
            String versionWithoutEA = javaVersion.endsWith("-ea")
                    ? javaVersion.substring(0, javaVersion.length() - 3)
                    : javaVersion;
            int dotIndex = versionWithoutEA.indexOf('.');
            String majorVersionStr = dotIndex != -1
                    ? versionWithoutEA.substring(0, dotIndex)
                    : versionWithoutEA;
            int expectedVersion = Integer.parseInt(majorVersionStr);
            assertEquals(expectedVersion, parsedVersion,
                    "Parsed version should match the major version from java.version");
        }
    }

    /**
     * Test currentJavaVersion with the current runtime environment.
     * This is a sanity check that the method works with the actual JVM.
     */
    @Test
    public void testCurrentJavaVersionWithCurrentRuntime() {
        int version = JavaUtil.currentJavaVersion();

        // The test is running on some Java version, so this should succeed
        assertNotEquals(0, version, "Version should not be 0");

        // Common Java versions
        assertTrue(version == 8 || version == 11 || version == 17 || version == 21 ||
                        (version >= 5 && version <= 100),
                "Version should be a known Java version or in reasonable range");
    }

    // ========================================================================
    // Tests for getCurrentJavaHome()
    // ========================================================================

    /**
     * Test that getCurrentJavaHome returns a non-null File object.
     */
    @Test
    public void testGetCurrentJavaHomeReturnsNonNull() {
        File javaHome = JavaUtil.getCurrentJavaHome();
        assertNotNull(javaHome, "getCurrentJavaHome should return a non-null File");
    }

    /**
     * Test that getCurrentJavaHome returns an absolute path.
     */
    @Test
    public void testGetCurrentJavaHomeReturnsAbsolutePath() {
        File javaHome = JavaUtil.getCurrentJavaHome();
        assertTrue(javaHome.isAbsolute(), "Java home should be an absolute path");
    }

    /**
     * Test that getCurrentJavaHome is idempotent.
     */
    @Test
    public void testGetCurrentJavaHomeIsIdempotent() {
        File javaHome1 = JavaUtil.getCurrentJavaHome();
        File javaHome2 = JavaUtil.getCurrentJavaHome();
        File javaHome3 = JavaUtil.getCurrentJavaHome();

        assertEquals(javaHome1, javaHome2, "Multiple calls should return the same path");
        assertEquals(javaHome2, javaHome3, "Multiple calls should return the same path");
    }

    /**
     * Test that getCurrentJavaHome returns the correct directory based on Java version.
     * For Java 9+, it should return java.home directly.
     * For Java 8 and earlier, it should return the parent of java.home.
     */
    @Test
    public void testGetCurrentJavaHomeReturnsCorrectDirectory() {
        File javaHome = JavaUtil.getCurrentJavaHome();
        int version = JavaUtil.currentJavaVersion();
        String javaHomeProperty = System.getProperty("java.home");

        if (version > 8) {
            // Java 9+: should return java.home directly
            assertEquals(new File(javaHomeProperty), javaHome,
                    "For Java 9+, should return java.home directly");
        } else {
            // Java 8 and earlier: should return parent of java.home
            assertEquals(new File(javaHomeProperty).getParentFile(), javaHome,
                    "For Java 8 and earlier, should return parent of java.home");
        }
    }

    /**
     * Test that getCurrentJavaHome returns a path that exists (in most cases).
     * Note: This might not always exist in all test environments, so we just verify
     * the behavior without asserting existence.
     */
    @Test
    public void testGetCurrentJavaHomePathStructure() {
        File javaHome = JavaUtil.getCurrentJavaHome();
        assertNotNull(javaHome.getPath(), "Java home path should not be null");
        assertFalse(javaHome.getPath().isEmpty(), "Java home path should not be empty");
    }

    /**
     * Test that getCurrentJavaHome path contains expected parts of a Java installation path.
     */
    @Test
    public void testGetCurrentJavaHomePathContainsJavaRelatedName() {
        File javaHome = JavaUtil.getCurrentJavaHome();
        String path = javaHome.getAbsolutePath().toLowerCase();

        // Most Java installations have "java" or "jdk" or "jre" in the path somewhere
        assertTrue(path.contains("java") || path.contains("jdk") || path.contains("jre"),
                "Java home path should typically contain 'java', 'jdk', or 'jre': " + path);
    }

    // ========================================================================
    // Tests for getRtJar()
    // ========================================================================

    /**
     * Test that getRtJar returns a non-null File object.
     */
    @Test
    public void testGetRtJarReturnsNonNull() {
        File rtJar = JavaUtil.getRtJar();
        assertNotNull(rtJar, "getRtJar should return a non-null File");
    }

    /**
     * Test that getRtJar returns an absolute path.
     */
    @Test
    public void testGetRtJarReturnsAbsolutePath() {
        File rtJar = JavaUtil.getRtJar();
        assertTrue(rtJar.isAbsolute(), "rt.jar path should be absolute");
    }

    /**
     * Test that getRtJar is idempotent.
     */
    @Test
    public void testGetRtJarIsIdempotent() {
        File rtJar1 = JavaUtil.getRtJar();
        File rtJar2 = JavaUtil.getRtJar();
        File rtJar3 = JavaUtil.getRtJar();

        assertEquals(rtJar1, rtJar2, "Multiple calls should return the same path");
        assertEquals(rtJar2, rtJar3, "Multiple calls should return the same path");
    }

    /**
     * Test that getRtJar returns a path ending with rt.jar.
     */
    @Test
    public void testGetRtJarReturnsCorrectFileName() {
        File rtJar = JavaUtil.getRtJar();
        assertEquals("rt.jar", rtJar.getName(), "File name should be rt.jar");
    }

    /**
     * Test that getRtJar returns a path under the Java home directory.
     */
    @Test
    public void testGetRtJarIsUnderJavaHome() {
        File rtJar = JavaUtil.getRtJar();
        File javaHome = JavaUtil.getCurrentJavaHome();

        String rtJarPath = rtJar.getAbsolutePath();
        String javaHomePath = javaHome.getAbsolutePath();

        assertTrue(rtJarPath.startsWith(javaHomePath),
                "rt.jar should be under Java home directory");
    }

    /**
     * Test that getRtJar constructs the correct path based on directory structure.
     * If javaHome/jre exists, rt.jar should be at javaHome/jre/lib/rt.jar
     * Otherwise, rt.jar should be at javaHome/lib/rt.jar
     */
    @Test
    public void testGetRtJarConstructsCorrectPath() {
        File rtJar = JavaUtil.getRtJar();
        File javaHome = JavaUtil.getCurrentJavaHome();

        File jreDir = new File(javaHome, "jre");
        if (jreDir.exists()) {
            File expectedPath = new File(javaHome, "jre" + File.separator + "lib" + File.separator + "rt.jar");
            assertEquals(expectedPath, rtJar,
                    "When jre directory exists, rt.jar should be at javaHome/jre/lib/rt.jar");
        } else {
            File expectedPath = new File(javaHome, "lib" + File.separator + "rt.jar");
            assertEquals(expectedPath, rtJar,
                    "When jre directory doesn't exist, rt.jar should be at javaHome/lib/rt.jar");
        }
    }

    /**
     * Test that getRtJar path contains "lib" directory.
     * rt.jar is always in a lib directory.
     */
    @Test
    public void testGetRtJarPathContainsLib() {
        File rtJar = JavaUtil.getRtJar();
        String path = rtJar.getAbsolutePath();

        assertTrue(path.contains("lib"), "rt.jar path should contain 'lib' directory");
    }

    /**
     * Test getRtJar existence behavior.
     * For Java 8 and earlier, rt.jar should exist.
     * For Java 9+, rt.jar doesn't exist (replaced by modules).
     * We test that the method returns a valid path structure regardless.
     */
    @Test
    public void testGetRtJarExistenceBasedOnJavaVersion() {
        File rtJar = JavaUtil.getRtJar();
        int version = JavaUtil.currentJavaVersion();

        if (version <= 8) {
            // For Java 8 and earlier, rt.jar typically exists
            // Note: We can't always assert this in test environments, so we just verify the path structure
            assertTrue(rtJar.getPath().endsWith("rt.jar"),
                    "For Java 8 and earlier, path should point to rt.jar");
        } else {
            // For Java 9+, rt.jar doesn't exist, but the method still returns a path
            assertTrue(rtJar.getPath().endsWith("rt.jar"),
                    "Method should still construct rt.jar path for Java 9+");
        }
    }

    // ========================================================================
    // Tests for getJmodBase()
    // ========================================================================

    /**
     * Test that getJmodBase returns a non-null File object.
     */
    @Test
    public void testGetJmodBaseReturnsNonNull() {
        File jmodBase = JavaUtil.getJmodBase();
        assertNotNull(jmodBase, "getJmodBase should return a non-null File");
    }

    /**
     * Test that getJmodBase returns an absolute path.
     */
    @Test
    public void testGetJmodBaseReturnsAbsolutePath() {
        File jmodBase = JavaUtil.getJmodBase();
        assertTrue(jmodBase.isAbsolute(), "java.base.jmod path should be absolute");
    }

    /**
     * Test that getJmodBase is idempotent.
     */
    @Test
    public void testGetJmodBaseIsIdempotent() {
        File jmodBase1 = JavaUtil.getJmodBase();
        File jmodBase2 = JavaUtil.getJmodBase();
        File jmodBase3 = JavaUtil.getJmodBase();

        assertEquals(jmodBase1, jmodBase2, "Multiple calls should return the same path");
        assertEquals(jmodBase2, jmodBase3, "Multiple calls should return the same path");
    }

    /**
     * Test that getJmodBase returns a path ending with java.base.jmod.
     */
    @Test
    public void testGetJmodBaseReturnsCorrectFileName() {
        File jmodBase = JavaUtil.getJmodBase();
        assertEquals("java.base.jmod", jmodBase.getName(), "File name should be java.base.jmod");
    }

    /**
     * Test that getJmodBase returns a path under the Java home directory.
     */
    @Test
    public void testGetJmodBaseIsUnderJavaHome() {
        File jmodBase = JavaUtil.getJmodBase();
        File javaHome = JavaUtil.getCurrentJavaHome();

        String jmodBasePath = jmodBase.getAbsolutePath();
        String javaHomePath = javaHome.getAbsolutePath();

        assertTrue(jmodBasePath.startsWith(javaHomePath),
                "java.base.jmod should be under Java home directory");
    }

    /**
     * Test that getJmodBase constructs the correct path.
     * Should be javaHome/jmods/java.base.jmod
     */
    @Test
    public void testGetJmodBaseConstructsCorrectPath() {
        File jmodBase = JavaUtil.getJmodBase();
        File javaHome = JavaUtil.getCurrentJavaHome();

        File expectedPath = new File(javaHome, "jmods" + File.separator + "java.base.jmod");
        assertEquals(expectedPath, jmodBase,
                "java.base.jmod should be at javaHome/jmods/java.base.jmod");
    }

    /**
     * Test that getJmodBase path contains "jmods" directory.
     */
    @Test
    public void testGetJmodBasePathContainsJmods() {
        File jmodBase = JavaUtil.getJmodBase();
        String path = jmodBase.getAbsolutePath();

        assertTrue(path.contains("jmods"), "java.base.jmod path should contain 'jmods' directory");
    }

    /**
     * Test getJmodBase existence behavior based on Java version.
     * jmod files only exist in Java 9+.
     */
    @Test
    public void testGetJmodBaseExistenceBasedOnJavaVersion() {
        File jmodBase = JavaUtil.getJmodBase();
        int version = JavaUtil.currentJavaVersion();

        if (version >= 9) {
            // For Java 9+, jmod files should exist
            // Note: We can't always assert existence in test environments, but we verify path structure
            assertTrue(jmodBase.getPath().endsWith("java.base.jmod"),
                    "For Java 9+, path should point to java.base.jmod");
            assertTrue(jmodBase.getPath().contains("jmods"),
                    "For Java 9+, path should contain jmods directory");
        } else {
            // For Java 8 and earlier, jmod files don't exist, but method still returns a path
            assertTrue(jmodBase.getPath().endsWith("java.base.jmod"),
                    "Method should still construct jmod path for Java 8 and earlier");
        }
    }

    // ========================================================================
    // Integration tests - testing relationships between methods
    // ========================================================================

    /**
     * Test that all methods are consistent with each other.
     * getRtJar and getJmodBase should both use the same Java home.
     */
    @Test
    public void testMethodsAreConsistent() {
        File javaHome = JavaUtil.getCurrentJavaHome();
        File rtJar = JavaUtil.getRtJar();
        File jmodBase = JavaUtil.getJmodBase();

        String javaHomePath = javaHome.getAbsolutePath();

        assertTrue(rtJar.getAbsolutePath().startsWith(javaHomePath),
                "rt.jar should be under the same Java home");
        assertTrue(jmodBase.getAbsolutePath().startsWith(javaHomePath),
                "java.base.jmod should be under the same Java home");
    }

    /**
     * Test that Java version detection affects the Java home path logic.
     */
    @Test
    public void testJavaVersionAffectsJavaHome() {
        int version = JavaUtil.currentJavaVersion();
        File javaHome = JavaUtil.getCurrentJavaHome();
        String javaHomeProperty = System.getProperty("java.home");

        if (version > 8) {
            assertEquals(javaHomeProperty, javaHome.getAbsolutePath(),
                    "For Java 9+, Java home should match java.home property");
        } else {
            File expectedJavaHome = new File(javaHomeProperty).getParentFile();
            if (expectedJavaHome != null) {
                assertEquals(expectedJavaHome.getAbsolutePath(), javaHome.getAbsolutePath(),
                        "For Java 8-, Java home should be parent of java.home property");
            }
        }
    }

    /**
     * Test that all getter methods return valid File objects with proper structure.
     */
    @Test
    public void testAllMethodsReturnValidFileObjects() {
        File javaHome = JavaUtil.getCurrentJavaHome();
        File rtJar = JavaUtil.getRtJar();
        File jmodBase = JavaUtil.getJmodBase();

        // All should be absolute paths
        assertTrue(javaHome.isAbsolute());
        assertTrue(rtJar.isAbsolute());
        assertTrue(jmodBase.isAbsolute());

        // All should have non-empty paths
        assertFalse(javaHome.getPath().isEmpty());
        assertFalse(rtJar.getPath().isEmpty());
        assertFalse(jmodBase.getPath().isEmpty());

        // All should have absolute paths that are not just "/"
        assertTrue(javaHome.getAbsolutePath().length() > 1);
        assertTrue(rtJar.getAbsolutePath().length() > 1);
        assertTrue(jmodBase.getAbsolutePath().length() > 1);
    }

    /**
     * Test that the class can be instantiated (tests the constructor).
     * Although all methods are static, the class should still be instantiable.
     */
    @Test
    public void testJavaUtilCanBeInstantiated() {
        assertDoesNotThrow(() -> {
            JavaUtil javaUtil = new JavaUtil();
            assertNotNull(javaUtil, "JavaUtil instance should not be null");
        }, "JavaUtil should be instantiable");
    }

    /**
     * Test that static methods can be called without instantiation.
     */
    @Test
    public void testStaticMethodsWorkWithoutInstantiation() {
        assertDoesNotThrow(() -> {
            int version = JavaUtil.currentJavaVersion();
            File javaHome = JavaUtil.getCurrentJavaHome();
            File rtJar = JavaUtil.getRtJar();
            File jmodBase = JavaUtil.getJmodBase();

            assertNotNull(javaHome);
            assertNotNull(rtJar);
            assertNotNull(jmodBase);
            assertTrue(version > 0);
        }, "Static methods should work without creating an instance");
    }

    /**
     * Test that the methods handle the actual file system correctly.
     * This is an integration test that verifies the overall behavior.
     */
    @Test
    public void testMethodsHandleFileSystemCorrectly() {
        int version = JavaUtil.currentJavaVersion();
        File javaHome = JavaUtil.getCurrentJavaHome();
        File rtJar = JavaUtil.getRtJar();
        File jmodBase = JavaUtil.getJmodBase();

        // All paths should use the correct file separator for the OS
        assertTrue(javaHome.getPath().contains(File.separator) || javaHome.getPath().length() < 10);
        assertTrue(rtJar.getPath().contains(File.separator));
        assertTrue(jmodBase.getPath().contains(File.separator));

        // Paths should be properly formed (no double separators)
        assertFalse(rtJar.getPath().contains(File.separator + File.separator));
        assertFalse(jmodBase.getPath().contains(File.separator + File.separator));
    }
}

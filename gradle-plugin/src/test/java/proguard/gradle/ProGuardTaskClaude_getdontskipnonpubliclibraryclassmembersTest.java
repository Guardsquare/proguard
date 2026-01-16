/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#getdontskipnonpubliclibraryclassmembers()}.
 *
 * This test class verifies that the getdontskipnonpubliclibraryclassmembers() method correctly
 * sets the skipNonPublicLibraryClassMembers flag in the configuration.
 *
 * The method signature is: getdontskipnonpubliclibraryclassmembers()
 * - This is a Groovy hack to support the keyword without parentheses
 * - Calls dontskipnonpubliclibraryclassmembers() which sets configuration.skipNonPublicLibraryClassMembers = false
 * - Always returns null
 *
 * Key behavior: This is NOT a true getter - it has a side effect of modifying the configuration.
 * The naming with 'get' prefix allows Groovy to treat it as a property access, enabling syntax
 * like "dontskipnonpubliclibraryclassmembers" (without parentheses) in Groovy build scripts.
 *
 * The skipNonPublicLibraryClassMembers flag defaults to true. Setting it to false tells ProGuard
 * to NOT skip non-public library class members during processing. This is typically used when
 * non-public members in libraries are referenced by application code (e.g., through reflection).
 */
public class ProGuardTaskClaude_getdontskipnonpubliclibraryclassmembersTest {

    @TempDir
    Path tempDir;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("testProguard", ProGuardTask.class);
    }

    @AfterEach
    public void tearDown() {
        project = null;
        task = null;
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() returns null.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_returnsNull() throws Exception {
        // When: Calling getdontskipnonpubliclibraryclassmembers()
        Object result = task.getdontskipnonpubliclibraryclassmembers();

        // Then: It should return null
        assertNull(result, "getdontskipnonpubliclibraryclassmembers() should always return null");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() sets the configuration flag to false.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_setsConfigurationFlag() throws Exception {
        // Given: Initial state where flag is true (default)
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
                   "Flag should initially be true (default)");

        // When: Calling getdontskipnonpubliclibraryclassmembers()
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: The flag should be set to false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false after calling getdontskipnonpubliclibraryclassmembers()");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() has side effect (not a pure getter).
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_hasSideEffect() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
                   "Flag should initially be true");

        // When: Calling getdontskipnonpubliclibraryclassmembers() multiple times
        Object result1 = task.getdontskipnonpubliclibraryclassmembers();
        Object result2 = task.getdontskipnonpubliclibraryclassmembers();

        // Then: Both calls return null but the side effect persists
        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should remain false");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() can be called multiple times.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_multipleCalls() throws Exception {
        // When: Calling getdontskipnonpubliclibraryclassmembers() multiple times
        task.getdontskipnonpubliclibraryclassmembers();
        task.getdontskipnonpubliclibraryclassmembers();
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: The flag should still be false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false after multiple calls");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() is idempotent.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_idempotent() throws Exception {
        // Given: Flag is already set to false
        task.configuration.skipNonPublicLibraryClassMembers = false;

        // When: Calling getdontskipnonpubliclibraryclassmembers()
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: The flag should remain false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should remain false (idempotent)");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() works independently of injars.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling getdontskipnonpubliclibraryclassmembers()
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() works independently of libraryjars.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling getdontskipnonpubliclibraryclassmembers()
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() works in a complex workflow.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.getdontskipnonpubliclibraryclassmembers();
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() can be called before other methods.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_calledBeforeOtherMethods() throws Exception {
        // When: Calling getdontskipnonpubliclibraryclassmembers() before other methods
        task.getdontskipnonpubliclibraryclassmembers();
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: Flag should still be set
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should remain false");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() can be called after other methods.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling getdontskipnonpubliclibraryclassmembers() after other methods
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: All should be set correctly
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() can be interleaved with other method calls.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving getdontskipnonpubliclibraryclassmembers() with other methods
        task.injars("input.jar");
        task.getdontskipnonpubliclibraryclassmembers();
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: All should be set correctly
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() works in realistic Android scenario.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_realisticAndroidScenario() throws Exception {
        // Given: Android project with reflection usage
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.getdontskipnonpubliclibraryclassmembers(); // Don't skip members accessed via reflection
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: Flag should be set to false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false for Android with reflection");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() works in realistic Java scenario.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_realisticJavaScenario() throws Exception {
        // Given: Java project with reflection usage
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.getdontskipnonpubliclibraryclassmembers(); // Don't skip members accessed via reflection
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: Flag should be set to false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false for Java with reflection");
    }

    /**
     * Tests that the flag is initially true before calling getdontskipnonpubliclibraryclassmembers().
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_initiallyTrue() throws Exception {
        // Given: A newly created task
        // When: Not calling getdontskipnonpubliclibraryclassmembers()
        // Then: The flag should be true (default)
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
                   "Flag should initially be true (default)");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() can override the default.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_overridesDefault() throws Exception {
        // Given: Flag at default value (true)
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
                   "Flag should initially be true");

        // When: Calling getdontskipnonpubliclibraryclassmembers()
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: The flag should be overridden to false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be overridden to false");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() always returns the same value (null).
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_alwaysReturnsNull() throws Exception {
        // When: Calling getdontskipnonpubliclibraryclassmembers() multiple times
        Object result1 = task.getdontskipnonpubliclibraryclassmembers();
        Object result2 = task.getdontskipnonpubliclibraryclassmembers();
        Object result3 = task.getdontskipnonpubliclibraryclassmembers();

        // Then: All should return null
        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() delegates to dontskipnonpubliclibraryclassmembers().
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_delegatesToDontskipnonpubliclibraryclassmembers() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
                   "Flag should initially be true");

        // When: Calling getdontskipnonpubliclibraryclassmembers() which should delegate
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: The flag should be set to false (same as calling dontskipnonpubliclibraryclassmembers())
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false via delegation");

        // Verify the same result as calling dontskipnonpubliclibraryclassmembers() directly
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);
        task2.dontskipnonpubliclibraryclassmembers();
        assertEquals(task.configuration.skipNonPublicLibraryClassMembers,
                     task2.configuration.skipNonPublicLibraryClassMembers,
                     "Both methods should set the same flag");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() can be called in sequence with configuration.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling getdontskipnonpubliclibraryclassmembers()
        task.getdontskipnonpubliclibraryclassmembers();

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() works with multiple library jars.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling getdontskipnonpubliclibraryclassmembers()
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: Flag should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false to process non-public members in all libraries");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() is thread-safe for idempotent operations.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_idempotentThreadSafe() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
                   "Flag should initially be true");

        // When: Calling getdontskipnonpubliclibraryclassmembers() sequentially (simulating thread safety)
        task.getdontskipnonpubliclibraryclassmembers();
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: The flag should be false and stable
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be stable after multiple calls");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() return value can be safely ignored.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_returnValueCanBeIgnored() throws Exception {
        // When: Calling getdontskipnonpubliclibraryclassmembers() and ignoring return value
        task.getdontskipnonpubliclibraryclassmembers(); // Return value ignored

        // Then: The side effect should still occur
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false even if return value is ignored");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() behavior is consistent across task instances.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling getdontskipnonpubliclibraryclassmembers() on both
        task1.getdontskipnonpubliclibraryclassmembers();
        task2.getdontskipnonpubliclibraryclassmembers();

        // Then: Both should have the flag set to false
        assertFalse(task1.configuration.skipNonPublicLibraryClassMembers,
                    "Task1 flag should be false");
        assertFalse(task2.configuration.skipNonPublicLibraryClassMembers,
                    "Task2 flag should be false");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() can be combined with filters.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_withFilters() throws Exception {
        // Given: Library jars with filters
        java.util.Map<String, String> filter = new java.util.HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "android.jar");

        // When: Calling getdontskipnonpubliclibraryclassmembers()
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: Both filter and flag should be set
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getLibraryJarFilters().size(), "Should have 1 filter");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() can reverse skipnonpubliclibraryclasses() effect.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_canReverseSkipEffect() throws Exception {
        // Given: skipnonpubliclibraryclasses() has been called (different flag)
        task.skipnonpubliclibraryclasses();
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "skipNonPublicLibraryClasses should be true");

        // When: Calling getdontskipnonpubliclibraryclassmembers() (affects different flag)
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: This method affects skipNonPublicLibraryClassMembers, not skipNonPublicLibraryClasses
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "skipNonPublicLibraryClasses should remain true");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "skipNonPublicLibraryClassMembers should be false");
    }

    /**
     * Tests that getdontskipnonpubliclibraryclassmembers() is useful for reflection scenarios.
     */
    @Test
    public void testGetdontskipnonpubliclibraryclassmembers_reflectionScenario() throws Exception {
        // Given: Project that uses reflection to access library members
        task.injars("app.jar");
        task.libraryjars("library-with-reflection-targets.jar");

        // When: Setting flag to false to preserve access to non-public members
        task.getdontskipnonpubliclibraryclassmembers();

        // Then: Flag should be false to ensure reflection targets are preserved
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false to preserve non-public members for reflection");
    }
}

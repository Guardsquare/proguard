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
 * Tests for {@link ProGuardTask#getskipnonpubliclibraryclasses()}.
 *
 * This test class verifies that the getskipnonpubliclibraryclasses() method correctly
 * sets the skipNonPublicLibraryClasses flag in the configuration.
 *
 * The method signature is: getskipnonpubliclibraryclasses()
 * - This is a Groovy hack to support the keyword without parentheses
 * - Calls skipnonpubliclibraryclasses() which sets configuration.skipNonPublicLibraryClasses = true
 * - Always returns null
 *
 * Key behavior: This is NOT a true getter - it has a side effect of modifying the configuration.
 * The naming with 'get' prefix allows Groovy to treat it as a property access, enabling syntax
 * like "skipnonpubliclibraryclasses" (without parentheses) in Groovy build scripts.
 *
 * The skipNonPublicLibraryClasses flag tells ProGuard to skip non-public library classes during
 * processing, which can improve performance when dealing with large library JARs.
 */
public class ProGuardTaskClaude_getskipnonpubliclibraryclassesTest {

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
     * Tests that getskipnonpubliclibraryclasses() returns null.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_returnsNull() throws Exception {
        // When: Calling getskipnonpubliclibraryclasses()
        Object result = task.getskipnonpubliclibraryclasses();

        // Then: It should return null
        assertNull(result, "getskipnonpubliclibraryclasses() should always return null");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() sets the configuration flag.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_setsConfigurationFlag() throws Exception {
        // Given: Initial state where flag is false
        assertFalse(task.configuration.skipNonPublicLibraryClasses,
                    "Flag should initially be false");

        // When: Calling getskipnonpubliclibraryclasses()
        task.getskipnonpubliclibraryclasses();

        // Then: The flag should be set to true
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set to true after calling getskipnonpubliclibraryclasses()");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() has side effect (not a pure getter).
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_hasSideEffect() throws Exception {
        // Given: Initial state
        assertFalse(task.configuration.skipNonPublicLibraryClasses,
                    "Flag should initially be false");

        // When: Calling getskipnonpubliclibraryclasses() multiple times
        Object result1 = task.getskipnonpubliclibraryclasses();
        Object result2 = task.getskipnonpubliclibraryclasses();

        // Then: Both calls return null but the side effect persists
        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should remain true");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() can be called multiple times.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_multipleCalls() throws Exception {
        // When: Calling getskipnonpubliclibraryclasses() multiple times
        task.getskipnonpubliclibraryclasses();
        task.getskipnonpubliclibraryclasses();
        task.getskipnonpubliclibraryclasses();

        // Then: The flag should still be true
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be true after multiple calls");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() is idempotent.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_idempotent() throws Exception {
        // Given: Flag is already set to true
        task.configuration.skipNonPublicLibraryClasses = true;

        // When: Calling getskipnonpubliclibraryclasses()
        task.getskipnonpubliclibraryclasses();

        // Then: The flag should remain true
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should remain true (idempotent)");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() works independently of injars.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling getskipnonpubliclibraryclasses()
        task.getskipnonpubliclibraryclasses();

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() works independently of libraryjars.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling getskipnonpubliclibraryclasses()
        task.getskipnonpubliclibraryclasses();

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() works in a complex workflow.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.getskipnonpubliclibraryclasses();
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() can be called before other methods.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_calledBeforeOtherMethods() throws Exception {
        // When: Calling getskipnonpubliclibraryclasses() before other methods
        task.getskipnonpubliclibraryclasses();
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: Flag should still be set
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should remain set");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() can be called after other methods.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling getskipnonpubliclibraryclasses() after other methods
        task.getskipnonpubliclibraryclasses();

        // Then: All should be set correctly
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() can be interleaved with other method calls.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving getskipnonpubliclibraryclasses() with other methods
        task.injars("input.jar");
        task.getskipnonpubliclibraryclasses();
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: All should be set correctly
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() works in realistic Android scenario.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_realisticAndroidScenario() throws Exception {
        // Given: Android project with large library
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.getskipnonpubliclibraryclasses(); // Skip non-public classes for performance
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: Flag should be set
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set for Android with large library");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() works in realistic Java scenario.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_realisticJavaScenario() throws Exception {
        // Given: Java project with rt.jar
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.getskipnonpubliclibraryclasses(); // Skip non-public classes for performance
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: Flag should be set
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set for Java with rt.jar");
    }

    /**
     * Tests that the flag is initially false before calling getskipnonpubliclibraryclasses().
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_initiallyFalse() throws Exception {
        // Given: A newly created task
        // When: Not calling getskipnonpubliclibraryclasses()
        // Then: The flag should be false
        assertFalse(task.configuration.skipNonPublicLibraryClasses,
                    "Flag should initially be false");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() cannot unset the flag.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_cannotUnset() throws Exception {
        // Given: Flag is set to true
        task.getskipnonpubliclibraryclasses();
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be true");

        // When: Calling again (trying to unset)
        task.getskipnonpubliclibraryclasses();

        // Then: The flag should remain true (cannot unset)
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should remain true (cannot be unset)");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() always returns the same value (null).
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_alwaysReturnsNull() throws Exception {
        // When: Calling getskipnonpubliclibraryclasses() multiple times
        Object result1 = task.getskipnonpubliclibraryclasses();
        Object result2 = task.getskipnonpubliclibraryclasses();
        Object result3 = task.getskipnonpubliclibraryclasses();

        // Then: All should return null
        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() delegates to skipnonpubliclibraryclasses().
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_delegatesToSkipnonpubliclibraryclasses() throws Exception {
        // Given: Initial state
        assertFalse(task.configuration.skipNonPublicLibraryClasses,
                    "Flag should initially be false");

        // When: Calling getskipnonpubliclibraryclasses() which should delegate
        task.getskipnonpubliclibraryclasses();

        // Then: The flag should be set (same as calling skipnonpubliclibraryclasses())
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set via delegation");

        // Verify the same result as calling skipnonpubliclibraryclasses() directly
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);
        task2.skipnonpubliclibraryclasses();
        assertEquals(task.configuration.skipNonPublicLibraryClasses,
                     task2.configuration.skipNonPublicLibraryClasses,
                     "Both methods should set the same flag");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() can be called in sequence with configuration.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling getskipnonpubliclibraryclasses()
        task.getskipnonpubliclibraryclasses();

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() works with multiple library jars.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling getskipnonpubliclibraryclasses()
        task.getskipnonpubliclibraryclasses();

        // Then: Flag should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set to skip non-public classes in all libraries");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() is thread-safe for idempotent operations.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_idempotentThreadSafe() throws Exception {
        // Given: Initial state
        assertFalse(task.configuration.skipNonPublicLibraryClasses,
                    "Flag should initially be false");

        // When: Calling getskipnonpubliclibraryclasses() sequentially (simulating thread safety)
        task.getskipnonpubliclibraryclasses();
        task.getskipnonpubliclibraryclasses();

        // Then: The flag should be true and stable
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be stable after multiple calls");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() return value can be safely ignored.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_returnValueCanBeIgnored() throws Exception {
        // When: Calling getskipnonpubliclibraryclasses() and ignoring return value
        task.getskipnonpubliclibraryclasses(); // Return value ignored

        // Then: The side effect should still occur
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set even if return value is ignored");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() behavior is consistent across task instances.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling getskipnonpubliclibraryclasses() on both
        task1.getskipnonpubliclibraryclasses();
        task2.getskipnonpubliclibraryclasses();

        // Then: Both should have the flag set
        assertTrue(task1.configuration.skipNonPublicLibraryClasses,
                   "Task1 flag should be set");
        assertTrue(task2.configuration.skipNonPublicLibraryClasses,
                   "Task2 flag should be set");
    }

    /**
     * Tests that getskipnonpubliclibraryclasses() can be combined with filters.
     */
    @Test
    public void testGetskipnonpubliclibraryclasses_withFilters() throws Exception {
        // Given: Library jars with filters
        java.util.Map<String, String> filter = new java.util.HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "android.jar");

        // When: Calling getskipnonpubliclibraryclasses()
        task.getskipnonpubliclibraryclasses();

        // Then: Both filter and flag should be set
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getLibraryJarFilters().size(), "Should have 1 filter");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
    }
}

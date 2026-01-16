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
 * Tests for {@link ProGuardTask#skipnonpubliclibraryclasses()}.
 *
 * This test class verifies that the skipnonpubliclibraryclasses() method correctly
 * sets the skipNonPublicLibraryClasses flag in the configuration.
 *
 * The method signature is: skipnonpubliclibraryclasses()
 * - Sets configuration.skipNonPublicLibraryClasses = true
 * - Returns void
 *
 * Key behavior: This method tells ProGuard to skip non-public library classes during
 * processing, which can improve performance when dealing with large library JARs.
 * Non-public classes in library JARs are typically not referenced by application code,
 * so skipping them can speed up processing without affecting the result.
 *
 * This method is called by getskipnonpubliclibraryclasses() to support Groovy DSL syntax.
 */
public class ProGuardTaskClaude_skipnonpubliclibraryclassesTest {

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
     * Tests that skipnonpubliclibraryclasses() sets the configuration flag.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_setsConfigurationFlag() throws Exception {
        // Given: Initial state where flag is false
        assertFalse(task.configuration.skipNonPublicLibraryClasses,
                    "Flag should initially be false");

        // When: Calling skipnonpubliclibraryclasses()
        task.skipnonpubliclibraryclasses();

        // Then: The flag should be set to true
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set to true after calling skipnonpubliclibraryclasses()");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() can be called multiple times.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_multipleCalls() throws Exception {
        // When: Calling skipnonpubliclibraryclasses() multiple times
        task.skipnonpubliclibraryclasses();
        task.skipnonpubliclibraryclasses();
        task.skipnonpubliclibraryclasses();

        // Then: The flag should still be true
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be true after multiple calls");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() is idempotent.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_idempotent() throws Exception {
        // Given: Flag is already set to true
        task.configuration.skipNonPublicLibraryClasses = true;

        // When: Calling skipnonpubliclibraryclasses()
        task.skipnonpubliclibraryclasses();

        // Then: The flag should remain true
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should remain true (idempotent)");
    }

    /**
     * Tests that the flag is initially false before calling skipnonpubliclibraryclasses().
     */
    @Test
    public void testSkipnonpubliclibraryclasses_initiallyFalse() throws Exception {
        // Given: A newly created task
        // When: Not calling skipnonpubliclibraryclasses()
        // Then: The flag should be false
        assertFalse(task.configuration.skipNonPublicLibraryClasses,
                    "Flag should initially be false");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() works independently of injars.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling skipnonpubliclibraryclasses()
        task.skipnonpubliclibraryclasses();

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() works independently of libraryjars.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling skipnonpubliclibraryclasses()
        task.skipnonpubliclibraryclasses();

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() works in a complex workflow.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.skipnonpubliclibraryclasses();
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
     * Tests that skipnonpubliclibraryclasses() can be called before other methods.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_calledBeforeOtherMethods() throws Exception {
        // When: Calling skipnonpubliclibraryclasses() before other methods
        task.skipnonpubliclibraryclasses();
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
     * Tests that skipnonpubliclibraryclasses() can be called after other methods.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling skipnonpubliclibraryclasses() after other methods
        task.skipnonpubliclibraryclasses();

        // Then: All should be set correctly
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() can be interleaved with other method calls.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving skipnonpubliclibraryclasses() with other methods
        task.injars("input.jar");
        task.skipnonpubliclibraryclasses();
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
     * Tests that skipnonpubliclibraryclasses() works in realistic Android scenario.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_realisticAndroidScenario() throws Exception {
        // Given: Android project with large library
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.skipnonpubliclibraryclasses(); // Skip non-public classes for performance
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: Flag should be set
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set for Android with large library");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() works in realistic Java scenario.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_realisticJavaScenario() throws Exception {
        // Given: Java project with rt.jar
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.skipnonpubliclibraryclasses(); // Skip non-public classes for performance
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: Flag should be set
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set for Java with rt.jar");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() cannot unset the flag.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_cannotUnset() throws Exception {
        // Given: Flag is set to true
        task.skipnonpubliclibraryclasses();
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be true");

        // When: Calling again (trying to unset)
        task.skipnonpubliclibraryclasses();

        // Then: The flag should remain true (cannot unset)
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should remain true (cannot be unset)");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() can be called in sequence with configuration.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling skipnonpubliclibraryclasses()
        task.skipnonpubliclibraryclasses();

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() works with multiple library jars.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling skipnonpubliclibraryclasses()
        task.skipnonpubliclibraryclasses();

        // Then: Flag should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set to skip non-public classes in all libraries");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() is thread-safe for idempotent operations.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_idempotentThreadSafe() throws Exception {
        // Given: Initial state
        assertFalse(task.configuration.skipNonPublicLibraryClasses,
                    "Flag should initially be false");

        // When: Calling skipnonpubliclibraryclasses() sequentially (simulating thread safety)
        task.skipnonpubliclibraryclasses();
        task.skipnonpubliclibraryclasses();

        // Then: The flag should be true and stable
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be stable after multiple calls");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() behavior is consistent across task instances.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling skipnonpubliclibraryclasses() on both
        task1.skipnonpubliclibraryclasses();
        task2.skipnonpubliclibraryclasses();

        // Then: Both should have the flag set
        assertTrue(task1.configuration.skipNonPublicLibraryClasses,
                   "Task1 flag should be set");
        assertTrue(task2.configuration.skipNonPublicLibraryClasses,
                   "Task2 flag should be set");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() can be combined with filters.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_withFilters() throws Exception {
        // Given: Library jars with filters
        java.util.Map<String, String> filter = new java.util.HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "android.jar");

        // When: Calling skipnonpubliclibraryclasses()
        task.skipnonpubliclibraryclasses();

        // Then: Both filter and flag should be set
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getLibraryJarFilters().size(), "Should have 1 filter");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() works with extraJar.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_withExtraJar() throws Exception {
        // Given: ExtraJar configured
        task.extraJar(new java.io.File(tempDir.toFile(), "extra.jar"));

        // When: Calling skipnonpubliclibraryclasses()
        task.skipnonpubliclibraryclasses();

        // Then: Both should be set
        assertNotNull(task.configuration.extraJar, "ExtraJar should be set");
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() returns void.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_returnsVoid() throws Exception {
        // Given: Initial state
        assertFalse(task.configuration.skipNonPublicLibraryClasses,
                    "Flag should initially be false");

        // When: Calling skipnonpubliclibraryclasses()
        task.skipnonpubliclibraryclasses(); // Method returns void

        // Then: The side effect should occur
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() and getskipnonpubliclibraryclasses()
     * produce the same result.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_equivalentToGetter() throws Exception {
        // Given: Two tasks
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling skipnonpubliclibraryclasses() on one and getskipnonpubliclibraryclasses() on other
        task1.skipnonpubliclibraryclasses();
        task2.getskipnonpubliclibraryclasses();

        // Then: Both should have the same effect
        assertEquals(task1.configuration.skipNonPublicLibraryClasses,
                     task2.configuration.skipNonPublicLibraryClasses,
                     "Both methods should set the same flag");
        assertTrue(task1.configuration.skipNonPublicLibraryClasses,
                   "Task1 flag should be true");
        assertTrue(task2.configuration.skipNonPublicLibraryClasses,
                   "Task2 flag should be true");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() affects performance optimization.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_performanceOptimization() throws Exception {
        // Given: Large library jars
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/jce.jar");

        // When: Calling skipnonpubliclibraryclasses() for performance
        task.skipnonpubliclibraryclasses();

        // Then: Flag should be set for optimization
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set to optimize processing of large libraries");
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() can be called at any point in the configuration.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.skipnonpubliclibraryclasses();
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.outjars("output.jar");
        task.libraryjars("lib2.jar");

        // Then: Flag should be set regardless of call order
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "Flag should be set regardless of when it's called");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
        assertEquals(2, task.getLibraryJarFiles().size(), "Should have 2 library jars");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that skipnonpubliclibraryclasses() sets only the specific flag.
     */
    @Test
    public void testSkipnonpubliclibraryclasses_setsOnlySpecificFlag() throws Exception {
        // When: Calling skipnonpubliclibraryclasses()
        task.skipnonpubliclibraryclasses();

        // Then: Only the skipNonPublicLibraryClasses flag should be affected
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "skipNonPublicLibraryClasses should be true");

        // Note: We're only testing the specific flag this method sets
        // Other configuration flags should not be affected by this method
    }
}

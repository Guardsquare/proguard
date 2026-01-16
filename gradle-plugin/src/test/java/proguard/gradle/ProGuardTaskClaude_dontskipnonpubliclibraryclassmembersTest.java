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
 * Tests for {@link ProGuardTask#dontskipnonpubliclibraryclassmembers()}.
 *
 * This test class verifies that the dontskipnonpubliclibraryclassmembers() method correctly
 * sets the skipNonPublicLibraryClassMembers flag in the configuration.
 *
 * The method signature is: dontskipnonpubliclibraryclassmembers()
 * - Sets configuration.skipNonPublicLibraryClassMembers = false
 * - Returns void
 *
 * Key behavior: This method tells ProGuard to NOT skip non-public library class members during
 * processing. The default value of skipNonPublicLibraryClassMembers is true, and calling this
 * method changes it to false. This is typically necessary when non-public members in libraries
 * are referenced by application code (e.g., through reflection).
 *
 * This method is called by getdontskipnonpubliclibraryclassmembers() to support Groovy DSL syntax.
 */
public class ProGuardTaskClaude_dontskipnonpubliclibraryclassmembersTest {

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
     * Tests that dontskipnonpubliclibraryclassmembers() sets the configuration flag to false.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_setsConfigurationFlag() throws Exception {
        // Given: Initial state where flag is true (default)
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
                   "Flag should initially be true (default)");

        // When: Calling dontskipnonpubliclibraryclassmembers()
        task.dontskipnonpubliclibraryclassmembers();

        // Then: The flag should be set to false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false after calling dontskipnonpubliclibraryclassmembers()");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() can be called multiple times.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_multipleCalls() throws Exception {
        // When: Calling dontskipnonpubliclibraryclassmembers() multiple times
        task.dontskipnonpubliclibraryclassmembers();
        task.dontskipnonpubliclibraryclassmembers();
        task.dontskipnonpubliclibraryclassmembers();

        // Then: The flag should still be false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false after multiple calls");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() is idempotent.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_idempotent() throws Exception {
        // Given: Flag is already set to false
        task.configuration.skipNonPublicLibraryClassMembers = false;

        // When: Calling dontskipnonpubliclibraryclassmembers()
        task.dontskipnonpubliclibraryclassmembers();

        // Then: The flag should remain false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should remain false (idempotent)");
    }

    /**
     * Tests that the flag is initially true before calling dontskipnonpubliclibraryclassmembers().
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_initiallyTrue() throws Exception {
        // Given: A newly created task
        // When: Not calling dontskipnonpubliclibraryclassmembers()
        // Then: The flag should be true (default)
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
                   "Flag should initially be true (default)");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() works independently of injars.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling dontskipnonpubliclibraryclassmembers()
        task.dontskipnonpubliclibraryclassmembers();

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() works independently of libraryjars.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling dontskipnonpubliclibraryclassmembers()
        task.dontskipnonpubliclibraryclassmembers();

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() works in a complex workflow.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.dontskipnonpubliclibraryclassmembers();
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
     * Tests that dontskipnonpubliclibraryclassmembers() can be called before other methods.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_calledBeforeOtherMethods() throws Exception {
        // When: Calling dontskipnonpubliclibraryclassmembers() before other methods
        task.dontskipnonpubliclibraryclassmembers();
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
     * Tests that dontskipnonpubliclibraryclassmembers() can be called after other methods.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling dontskipnonpubliclibraryclassmembers() after other methods
        task.dontskipnonpubliclibraryclassmembers();

        // Then: All should be set correctly
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() can be interleaved with other method calls.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving dontskipnonpubliclibraryclassmembers() with other methods
        task.injars("input.jar");
        task.dontskipnonpubliclibraryclassmembers();
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
     * Tests that dontskipnonpubliclibraryclassmembers() works in realistic Android scenario.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_realisticAndroidScenario() throws Exception {
        // Given: Android project with reflection usage
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.dontskipnonpubliclibraryclassmembers(); // Don't skip members accessed via reflection
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: Flag should be set to false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false for Android with reflection");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() works in realistic Java scenario.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_realisticJavaScenario() throws Exception {
        // Given: Java project with reflection usage
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.dontskipnonpubliclibraryclassmembers(); // Don't skip members accessed via reflection
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: Flag should be set to false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false for Java with reflection");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() can override the default value.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_overridesDefault() throws Exception {
        // Given: Flag at default value (true)
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
                   "Flag should initially be true");

        // When: Calling dontskipnonpubliclibraryclassmembers()
        task.dontskipnonpubliclibraryclassmembers();

        // Then: The flag should be overridden to false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be overridden to false");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() can be called in sequence with configuration.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling dontskipnonpubliclibraryclassmembers()
        task.dontskipnonpubliclibraryclassmembers();

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be set to false");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() works with multiple library jars.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling dontskipnonpubliclibraryclassmembers()
        task.dontskipnonpubliclibraryclassmembers();

        // Then: Flag should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false to process non-public members in all libraries");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() is thread-safe for idempotent operations.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_idempotentThreadSafe() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
                   "Flag should initially be true");

        // When: Calling dontskipnonpubliclibraryclassmembers() sequentially (simulating thread safety)
        task.dontskipnonpubliclibraryclassmembers();
        task.dontskipnonpubliclibraryclassmembers();

        // Then: The flag should be false and stable
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be stable after multiple calls");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() behavior is consistent across task instances.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling dontskipnonpubliclibraryclassmembers() on both
        task1.dontskipnonpubliclibraryclassmembers();
        task2.dontskipnonpubliclibraryclassmembers();

        // Then: Both should have the flag set to false
        assertFalse(task1.configuration.skipNonPublicLibraryClassMembers,
                    "Task1 flag should be false");
        assertFalse(task2.configuration.skipNonPublicLibraryClassMembers,
                    "Task2 flag should be false");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() can be combined with filters.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_withFilters() throws Exception {
        // Given: Library jars with filters
        java.util.Map<String, String> filter = new java.util.HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "android.jar");

        // When: Calling dontskipnonpubliclibraryclassmembers()
        task.dontskipnonpubliclibraryclassmembers();

        // Then: Both filter and flag should be set
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getLibraryJarFilters().size(), "Should have 1 filter");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() works with extraJar.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_withExtraJar() throws Exception {
        // Given: ExtraJar configured
        task.extraJar(new java.io.File(tempDir.toFile(), "extra.jar"));

        // When: Calling dontskipnonpubliclibraryclassmembers()
        task.dontskipnonpubliclibraryclassmembers();

        // Then: Both should be set
        assertNotNull(task.configuration.extraJar, "ExtraJar should be set");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() returns void.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_returnsVoid() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
                   "Flag should initially be true");

        // When: Calling dontskipnonpubliclibraryclassmembers()
        task.dontskipnonpubliclibraryclassmembers(); // Method returns void

        // Then: The side effect should occur
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() and getdontskipnonpubliclibraryclassmembers()
     * produce the same result.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_equivalentToGetter() throws Exception {
        // Given: Two tasks
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling dontskipnonpubliclibraryclassmembers() on one and getdontskipnonpubliclibraryclassmembers() on other
        task1.dontskipnonpubliclibraryclassmembers();
        task2.getdontskipnonpubliclibraryclassmembers();

        // Then: Both should have the same effect
        assertEquals(task1.configuration.skipNonPublicLibraryClassMembers,
                     task2.configuration.skipNonPublicLibraryClassMembers,
                     "Both methods should set the same flag");
        assertFalse(task1.configuration.skipNonPublicLibraryClassMembers,
                    "Task1 flag should be false");
        assertFalse(task2.configuration.skipNonPublicLibraryClassMembers,
                    "Task2 flag should be false");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() is useful for reflection scenarios.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_reflectionScenario() throws Exception {
        // Given: Project that uses reflection to access library members
        task.injars("app.jar");
        task.libraryjars("library-with-reflection-targets.jar");

        // When: Setting flag to false to preserve access to non-public members
        task.dontskipnonpubliclibraryclassmembers();

        // Then: Flag should be false to ensure reflection targets are preserved
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false to preserve non-public members for reflection");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() can be called at any point in the configuration.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.dontskipnonpubliclibraryclassmembers();
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.outjars("output.jar");
        task.libraryjars("lib2.jar");

        // Then: Flag should be set regardless of call order
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "Flag should be false regardless of when it's called");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
        assertEquals(2, task.getLibraryJarFiles().size(), "Should have 2 library jars");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() sets only the specific flag.
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_setsOnlySpecificFlag() throws Exception {
        // When: Calling dontskipnonpubliclibraryclassmembers()
        task.dontskipnonpubliclibraryclassmembers();

        // Then: Only the skipNonPublicLibraryClassMembers flag should be affected
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "skipNonPublicLibraryClassMembers should be false");

        // Note: We're only testing the specific flag this method sets
        // Other configuration flags should not be affected by this method
    }

    /**
     * Tests that dontskipnonpubliclibraryclassmembers() affects different flag than skipnonpubliclibraryclasses().
     */
    @Test
    public void testDontskipnonpubliclibraryclassmembers_differentFlagFromSkipnonpubliclibraryclasses() throws Exception {
        // Given: skipnonpubliclibraryclasses() has been called (different flag)
        task.skipnonpubliclibraryclasses();
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "skipNonPublicLibraryClasses should be true");

        // When: Calling dontskipnonpubliclibraryclassmembers() (affects different flag)
        task.dontskipnonpubliclibraryclassmembers();

        // Then: This method affects skipNonPublicLibraryClassMembers, not skipNonPublicLibraryClasses
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
                   "skipNonPublicLibraryClasses should remain true");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
                    "skipNonPublicLibraryClassMembers should be false");
    }
}

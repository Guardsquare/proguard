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
 * Tests for {@link ProGuardTask#forceprocessing()}.
 *
 * This test class verifies that the forceprocessing() method correctly
 * sets the lastModified field in the configuration to force processing.
 *
 * Method signature: forceprocessing()V
 * - Sets configuration.lastModified = Long.MAX_VALUE
 * - Returns void
 *
 * Key behavior: This method forces ProGuard to always process the inputs by setting
 * the lastModified timestamp to the maximum value, effectively bypassing incremental
 * processing checks. Unlike getforceprocessing(), this method returns void rather
 * than null, making it suitable for direct method calls rather than Groovy DSL usage.
 *
 * The forceprocessing() method sets configuration.lastModified to Long.MAX_VALUE,
 * ensuring the task always runs regardless of input modification times.
 */
public class ProGuardTaskClaude_forceprocessingTest {

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
     * Tests that forceprocessing() sets lastModified to Long.MAX_VALUE.
     */
    @Test
    public void testForceprocessing_setsLastModifiedToMaxValue() throws Exception {
        // Given: Initial state where lastModified is 0 (default)
        assertEquals(0L, task.configuration.lastModified,
                   "lastModified should initially be 0");

        // When: Calling forceprocessing()
        task.forceprocessing();

        // Then: lastModified should be set to Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() can be called multiple times.
     */
    @Test
    public void testForceprocessing_multipleCalls() throws Exception {
        // When: Calling forceprocessing() multiple times
        task.forceprocessing();
        task.forceprocessing();
        task.forceprocessing();

        // Then: lastModified should still be Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE after multiple calls");
    }

    /**
     * Tests that forceprocessing() is idempotent.
     */
    @Test
    public void testForceprocessing_idempotent() throws Exception {
        // Given: lastModified already set to Long.MAX_VALUE
        task.configuration.lastModified = Long.MAX_VALUE;

        // When: Calling forceprocessing()
        task.forceprocessing();

        // Then: lastModified should remain Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should remain Long.MAX_VALUE (idempotent)");
    }

    /**
     * Tests that forceprocessing() overrides any previous lastModified value.
     */
    @Test
    public void testForceprocessing_overridesPreviousValue() throws Exception {
        // Given: lastModified set to some other value
        task.configuration.lastModified = 123456789L;
        assertEquals(123456789L, task.configuration.lastModified,
                   "lastModified should be set to test value");

        // When: Calling forceprocessing()
        task.forceprocessing();

        // Then: lastModified should be overridden to Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be overridden to Long.MAX_VALUE");
    }

    /**
     * Tests that the initial state has lastModified as 0.
     */
    @Test
    public void testForceprocessing_initiallyZero() throws Exception {
        // Given: A newly created task
        // When: Not calling forceprocessing()
        // Then: The lastModified should be 0 (default)
        assertEquals(0L, task.configuration.lastModified,
                   "lastModified should initially be 0");
    }

    /**
     * Tests that forceprocessing() works independently of injars.
     */
    @Test
    public void testForceprocessing_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling forceprocessing()
        task.forceprocessing();

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() works independently of libraryjars.
     */
    @Test
    public void testForceprocessing_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling forceprocessing()
        task.forceprocessing();

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() works in a complex workflow.
     */
    @Test
    public void testForceprocessing_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.forceprocessing();
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that forceprocessing() can be called before other methods.
     */
    @Test
    public void testForceprocessing_calledBeforeOtherMethods() throws Exception {
        // When: Calling forceprocessing() before other methods
        task.forceprocessing();
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: lastModified should still be set
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should remain Long.MAX_VALUE");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that forceprocessing() can be called after other methods.
     */
    @Test
    public void testForceprocessing_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling forceprocessing() after other methods
        task.forceprocessing();

        // Then: All should be set correctly
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that forceprocessing() can be interleaved with other method calls.
     */
    @Test
    public void testForceprocessing_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving forceprocessing() with other methods
        task.injars("input.jar");
        task.forceprocessing();
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: All should be set correctly
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that forceprocessing() works in realistic Android scenario.
     */
    @Test
    public void testForceprocessing_realisticAndroidScenario() throws Exception {
        // Given: Android project that needs forced processing
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.forceprocessing(); // Force processing even if inputs haven't changed
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: lastModified should be set to Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE to force processing");
    }

    /**
     * Tests that forceprocessing() works in realistic Java scenario.
     */
    @Test
    public void testForceprocessing_realisticJavaScenario() throws Exception {
        // Given: Java project that needs forced processing
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.forceprocessing(); // Force processing for debugging or testing
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: lastModified should be set to Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE to force processing");
    }

    /**
     * Tests that forceprocessing() can be called in sequence with configuration.
     */
    @Test
    public void testForceprocessing_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling forceprocessing()
        task.forceprocessing();

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() works with multiple library jars.
     */
    @Test
    public void testForceprocessing_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling forceprocessing()
        task.forceprocessing();

        // Then: lastModified should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() is thread-safe for idempotent operations.
     */
    @Test
    public void testForceprocessing_idempotentThreadSafe() throws Exception {
        // Given: Initial state
        assertEquals(0L, task.configuration.lastModified,
                   "lastModified should initially be 0");

        // When: Calling forceprocessing() sequentially (simulating thread safety)
        task.forceprocessing();
        task.forceprocessing();

        // Then: The lastModified should be Long.MAX_VALUE and stable
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be stable after multiple calls");
    }

    /**
     * Tests that forceprocessing() behavior is consistent across task instances.
     */
    @Test
    public void testForceprocessing_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling forceprocessing() on both
        task1.forceprocessing();
        task2.forceprocessing();

        // Then: Both should have lastModified set to Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task1.configuration.lastModified,
                   "Task1 lastModified should be Long.MAX_VALUE");
        assertEquals(Long.MAX_VALUE, task2.configuration.lastModified,
                   "Task2 lastModified should be Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() returns void.
     */
    @Test
    public void testForceprocessing_returnsVoid() throws Exception {
        // Given: Initial state
        assertEquals(0L, task.configuration.lastModified,
                   "lastModified should initially be 0");

        // When: Calling forceprocessing()
        task.forceprocessing(); // Method returns void

        // Then: The side effect should occur
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() can be called at any point in the configuration.
     */
    @Test
    public void testForceprocessing_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.forceprocessing();
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.outjars("output.jar");
        task.libraryjars("lib2.jar");

        // Then: lastModified should be set regardless of call order
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE regardless of when it's called");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
        assertEquals(2, task.getLibraryJarFiles().size(), "Should have 2 library jars");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that forceprocessing() sets only the specific field.
     */
    @Test
    public void testForceprocessing_setsOnlyLastModified() throws Exception {
        // When: Calling forceprocessing()
        task.forceprocessing();

        // Then: Only the lastModified field should be affected
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");

        // Note: We're only testing the specific field this method sets
        // Other configuration fields should not be affected by this method
    }

    /**
     * Tests that forceprocessing() forces processing by setting maximum timestamp.
     */
    @Test
    public void testForceprocessing_forcesProcessingWithMaxTimestamp() throws Exception {
        // Given: Initial state
        assertEquals(0L, task.configuration.lastModified,
                   "lastModified should initially be 0");

        // When: Calling forceprocessing() to force processing
        task.forceprocessing();

        // Then: lastModified should be set to maximum value to force processing
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE to bypass incremental checks");
        assertTrue(task.configuration.lastModified > System.currentTimeMillis(),
                  "lastModified should be greater than current time to force processing");
    }

    /**
     * Tests that forceprocessing() value persists across configuration calls.
     */
    @Test
    public void testForceprocessing_persistsAcrossConfigurationCalls() throws Exception {
        // Given: Force processing set early
        task.forceprocessing();
        long lastModifiedAfterSet = task.configuration.lastModified;

        // When: Other configuration methods are called
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");

        // Then: lastModified should remain unchanged
        assertEquals(lastModifiedAfterSet, task.configuration.lastModified,
                   "lastModified should persist after other configuration calls");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should still be Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() works correctly in a minimal configuration.
     */
    @Test
    public void testForceprocessing_minimalConfiguration() throws Exception {
        // When: Only forceprocessing() is called (minimal configuration)
        task.forceprocessing();

        // Then: lastModified should be set correctly
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE in minimal configuration");
    }

    /**
     * Tests that forceprocessing() is useful for bypassing incremental processing.
     */
    @Test
    public void testForceprocessing_bypassesIncrementalProcessing() throws Exception {
        // Given: Project with inputs that might be considered up-to-date
        task.injars("input.jar");
        task.outjars("output.jar");

        // When: Calling forceprocessing() to bypass incremental checks
        task.forceprocessing();

        // Then: lastModified should be set to maximum value
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE to bypass incremental processing");
    }

    /**
     * Tests that forceprocessing() can override manual lastModified changes.
     */
    @Test
    public void testForceprocessing_overridesManualChanges() throws Exception {
        // Given: Manual change to lastModified
        task.configuration.lastModified = 999999L;
        assertEquals(999999L, task.configuration.lastModified,
                   "lastModified should be set manually");

        // When: Calling forceprocessing()
        task.forceprocessing();

        // Then: Manual value should be overridden
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be overridden to Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() sets Long.MAX_VALUE which is the expected force value.
     */
    @Test
    public void testForceprocessing_setsExpectedForceValue() throws Exception {
        // When: Calling forceprocessing()
        task.forceprocessing();

        // Then: lastModified should be exactly Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be exactly Long.MAX_VALUE");
        assertEquals(9223372036854775807L, task.configuration.lastModified,
                   "lastModified should be the specific value of Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() works with filters.
     */
    @Test
    public void testForceprocessing_withFilters() throws Exception {
        // Given: Library jars with filters
        java.util.Map<String, String> filter = new java.util.HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "android.jar");

        // When: Calling forceprocessing()
        task.forceprocessing();

        // Then: Both filter and lastModified should be set
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getLibraryJarFilters().size(), "Should have 1 filter");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() works with extraJar.
     */
    @Test
    public void testForceprocessing_withExtraJar() throws Exception {
        // Given: ExtraJar configured
        task.extraJar(new java.io.File(tempDir.toFile(), "extra.jar"));

        // When: Calling forceprocessing()
        task.forceprocessing();

        // Then: Both should be set
        assertNotNull(task.configuration.extraJar, "ExtraJar should be set");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() and getforceprocessing() produce the same result.
     */
    @Test
    public void testForceprocessing_equivalentToGetforceprocessing() throws Exception {
        // Given: Two tasks
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling forceprocessing() on one and getforceprocessing() on other
        task1.forceprocessing();
        task2.getforceprocessing();

        // Then: Both should have the same effect
        assertEquals(task1.configuration.lastModified,
                   task2.configuration.lastModified,
                   "Both methods should set the same value");
        assertEquals(Long.MAX_VALUE, task1.configuration.lastModified,
                   "Task1 lastModified should be Long.MAX_VALUE");
        assertEquals(Long.MAX_VALUE, task2.configuration.lastModified,
                   "Task2 lastModified should be Long.MAX_VALUE");
    }

    /**
     * Tests that forceprocessing() is used for debugging scenarios.
     */
    @Test
    public void testForceprocessing_debuggingScenario() throws Exception {
        // Given: Project configured for debugging
        task.injars("app.jar");
        task.outjars("app-processed.jar");

        // When: Forcing processing for debugging
        task.forceprocessing();

        // Then: lastModified should force processing
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should force processing for debugging");
    }

    /**
     * Tests that forceprocessing() affects different field than target().
     */
    @Test
    public void testForceprocessing_differentFieldFromTarget() throws Exception {
        // Given: target() has been called (different field)
        task.target("1.8");
        int targetVersion = task.configuration.targetClassVersion;
        assertNotEquals(0, targetVersion, "targetClassVersion should be set");

        // When: Calling forceprocessing() (affects different field)
        task.forceprocessing();

        // Then: This method affects lastModified, not targetClassVersion
        assertEquals(targetVersion, task.configuration.targetClassVersion,
                   "targetClassVersion should remain unchanged");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE");
    }
}

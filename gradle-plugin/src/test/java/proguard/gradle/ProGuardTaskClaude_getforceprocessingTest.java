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
 * Tests for {@link ProGuardTask#getforceprocessing()}.
 *
 * This test class verifies that the getforceprocessing() method correctly
 * sets the lastModified field in the configuration and returns null.
 *
 * Method signature: getforceprocessing()Ljava/lang/Object;
 * - Calls forceprocessing() internally which sets configuration.lastModified = Long.MAX_VALUE
 * - Returns null (Groovy DSL hack)
 *
 * Key behavior: This method is a Groovy DSL support method that allows calling
 * "forceprocessing" without parentheses in Groovy build scripts. It forces ProGuard
 * to always process the inputs by setting the lastModified timestamp to the maximum
 * value, effectively bypassing incremental processing checks.
 *
 * The getforceprocessing() method internally calls forceprocessing(), which sets
 * configuration.lastModified to Long.MAX_VALUE, ensuring the task always runs.
 */
public class ProGuardTaskClaude_getforceprocessingTest {

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
     * Tests that getforceprocessing() returns null.
     */
    @Test
    public void testGetforceprocessing_returnsNull() throws Exception {
        // When: Calling getforceprocessing()
        Object result = task.getforceprocessing();

        // Then: Should return null (Groovy DSL hack)
        assertNull(result, "getforceprocessing() should return null");
    }

    /**
     * Tests that getforceprocessing() sets lastModified to Long.MAX_VALUE.
     */
    @Test
    public void testGetforceprocessing_setsLastModifiedToMaxValue() throws Exception {
        // Given: Initial state where lastModified is 0 (default)
        assertEquals(0L, task.configuration.lastModified,
                   "lastModified should initially be 0");

        // When: Calling getforceprocessing()
        task.getforceprocessing();

        // Then: lastModified should be set to Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
    }

    /**
     * Tests that getforceprocessing() can be called multiple times.
     */
    @Test
    public void testGetforceprocessing_multipleCalls() throws Exception {
        // When: Calling getforceprocessing() multiple times
        task.getforceprocessing();
        task.getforceprocessing();
        task.getforceprocessing();

        // Then: lastModified should still be Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE after multiple calls");
    }

    /**
     * Tests that getforceprocessing() is idempotent.
     */
    @Test
    public void testGetforceprocessing_idempotent() throws Exception {
        // Given: lastModified already set to Long.MAX_VALUE
        task.configuration.lastModified = Long.MAX_VALUE;

        // When: Calling getforceprocessing()
        task.getforceprocessing();

        // Then: lastModified should remain Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should remain Long.MAX_VALUE (idempotent)");
    }

    /**
     * Tests that getforceprocessing() overrides any previous lastModified value.
     */
    @Test
    public void testGetforceprocessing_overridesPreviousValue() throws Exception {
        // Given: lastModified set to some other value
        task.configuration.lastModified = 123456789L;
        assertEquals(123456789L, task.configuration.lastModified,
                   "lastModified should be set to test value");

        // When: Calling getforceprocessing()
        task.getforceprocessing();

        // Then: lastModified should be overridden to Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be overridden to Long.MAX_VALUE");
    }

    /**
     * Tests that getforceprocessing() works independently of injars.
     */
    @Test
    public void testGetforceprocessing_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling getforceprocessing()
        task.getforceprocessing();

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
    }

    /**
     * Tests that getforceprocessing() works independently of libraryjars.
     */
    @Test
    public void testGetforceprocessing_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling getforceprocessing()
        task.getforceprocessing();

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
    }

    /**
     * Tests that getforceprocessing() works in a complex workflow.
     */
    @Test
    public void testGetforceprocessing_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.getforceprocessing();
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
     * Tests that getforceprocessing() can be called before other methods.
     */
    @Test
    public void testGetforceprocessing_calledBeforeOtherMethods() throws Exception {
        // When: Calling getforceprocessing() before other methods
        task.getforceprocessing();
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
     * Tests that getforceprocessing() can be called after other methods.
     */
    @Test
    public void testGetforceprocessing_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling getforceprocessing() after other methods
        task.getforceprocessing();

        // Then: All should be set correctly
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getforceprocessing() can be interleaved with other method calls.
     */
    @Test
    public void testGetforceprocessing_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving getforceprocessing() with other methods
        task.injars("input.jar");
        task.getforceprocessing();
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
     * Tests that getforceprocessing() works in realistic Android scenario.
     */
    @Test
    public void testGetforceprocessing_realisticAndroidScenario() throws Exception {
        // Given: Android project that needs forced processing
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.getforceprocessing(); // Force processing even if inputs haven't changed
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: lastModified should be set to Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE to force processing");
    }

    /**
     * Tests that getforceprocessing() works in realistic Java scenario.
     */
    @Test
    public void testGetforceprocessing_realisticJavaScenario() throws Exception {
        // Given: Java project that needs forced processing
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.getforceprocessing(); // Force processing for debugging or testing
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: lastModified should be set to Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE to force processing");
    }

    /**
     * Tests that getforceprocessing() can be called in sequence with configuration.
     */
    @Test
    public void testGetforceprocessing_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling getforceprocessing()
        task.getforceprocessing();

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");
    }

    /**
     * Tests that getforceprocessing() works with multiple library jars.
     */
    @Test
    public void testGetforceprocessing_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling getforceprocessing()
        task.getforceprocessing();

        // Then: lastModified should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE");
    }

    /**
     * Tests that getforceprocessing() behavior is consistent across task instances.
     */
    @Test
    public void testGetforceprocessing_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling getforceprocessing() on both
        task1.getforceprocessing();
        task2.getforceprocessing();

        // Then: Both should have lastModified set to Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task1.configuration.lastModified,
                   "Task1 lastModified should be Long.MAX_VALUE");
        assertEquals(Long.MAX_VALUE, task2.configuration.lastModified,
                   "Task2 lastModified should be Long.MAX_VALUE");
    }

    /**
     * Tests that getforceprocessing() and forceprocessing() produce the same result.
     */
    @Test
    public void testGetforceprocessing_equivalentToForceprocessing() throws Exception {
        // Given: Two tasks
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling getforceprocessing() on one and forceprocessing() on other
        task1.getforceprocessing();
        task2.forceprocessing();

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
     * Tests that getforceprocessing() always returns null regardless of state.
     */
    @Test
    public void testGetforceprocessing_alwaysReturnsNull() throws Exception {
        // When: Calling getforceprocessing() multiple times
        Object result1 = task.getforceprocessing();
        Object result2 = task.getforceprocessing();
        Object result3 = task.getforceprocessing();

        // Then: All calls should return null
        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    /**
     * Tests that getforceprocessing() can be called at any point in the configuration.
     */
    @Test
    public void testGetforceprocessing_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.getforceprocessing();
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
     * Tests that getforceprocessing() sets only the specific field.
     */
    @Test
    public void testGetforceprocessing_setsOnlyLastModified() throws Exception {
        // When: Calling getforceprocessing()
        task.getforceprocessing();

        // Then: Only the lastModified field should be affected
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE");

        // Note: We're only testing the specific field this method sets
        // Other configuration fields should not be affected by this method
    }

    /**
     * Tests that getforceprocessing() forces processing by setting maximum timestamp.
     */
    @Test
    public void testGetforceprocessing_forcesProcessingWithMaxTimestamp() throws Exception {
        // Given: Initial state
        assertEquals(0L, task.configuration.lastModified,
                   "lastModified should initially be 0");

        // When: Calling getforceprocessing() to force processing
        task.getforceprocessing();

        // Then: lastModified should be set to maximum value to force processing
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE to bypass incremental checks");
        assertTrue(task.configuration.lastModified > System.currentTimeMillis(),
                  "lastModified should be greater than current time to force processing");
    }

    /**
     * Tests that getforceprocessing() value persists across configuration calls.
     */
    @Test
    public void testGetforceprocessing_persistsAcrossConfigurationCalls() throws Exception {
        // Given: Force processing set early
        task.getforceprocessing();
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
     * Tests that getforceprocessing() works correctly in a minimal configuration.
     */
    @Test
    public void testGetforceprocessing_minimalConfiguration() throws Exception {
        // When: Only getforceprocessing() is called (minimal configuration)
        Object result = task.getforceprocessing();

        // Then: Should return null and set lastModified
        assertNull(result, "Should return null");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set to Long.MAX_VALUE in minimal configuration");
    }

    /**
     * Tests that getforceprocessing() is useful for bypassing incremental processing.
     */
    @Test
    public void testGetforceprocessing_bypassesIncrementalProcessing() throws Exception {
        // Given: Project with inputs that might be considered up-to-date
        task.injars("input.jar");
        task.outjars("output.jar");

        // When: Calling getforceprocessing() to bypass incremental checks
        task.getforceprocessing();

        // Then: lastModified should be set to maximum value
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be Long.MAX_VALUE to bypass incremental processing");
    }

    /**
     * Tests that getforceprocessing() has side effect even though it returns null.
     */
    @Test
    public void testGetforceprocessing_hasSideEffectDespiteReturningNull() throws Exception {
        // Given: Initial state
        assertEquals(0L, task.configuration.lastModified,
                   "lastModified should initially be 0");

        // When: Calling getforceprocessing() which returns null
        Object result = task.getforceprocessing();

        // Then: Should return null but have side effect
        assertNull(result, "Should return null");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "Should have side effect of setting lastModified despite returning null");
    }

    /**
     * Tests that getforceprocessing() return value is null (not void).
     */
    @Test
    public void testGetforceprocessing_returnsNullNotVoid() throws Exception {
        // When: Calling getforceprocessing()
        Object result = task.getforceprocessing();

        // Then: Should return null (not throw exception or return void)
        assertNull(result, "Should explicitly return null");

        // And: The side effect should occur
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be set");
    }

    /**
     * Tests that getforceprocessing() initial value check.
     */
    @Test
    public void testGetforceprocessing_initialValueIsZero() throws Exception {
        // Given: A newly created task
        // When: Not calling getforceprocessing()
        // Then: The lastModified should be 0 (default)
        assertEquals(0L, task.configuration.lastModified,
                   "lastModified should initially be 0");
    }

    /**
     * Tests that getforceprocessing() can override manual lastModified changes.
     */
    @Test
    public void testGetforceprocessing_overridesManualChanges() throws Exception {
        // Given: Manual change to lastModified
        task.configuration.lastModified = 999999L;
        assertEquals(999999L, task.configuration.lastModified,
                   "lastModified should be set manually");

        // When: Calling getforceprocessing()
        task.getforceprocessing();

        // Then: Manual value should be overridden
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be overridden to Long.MAX_VALUE");
    }

    /**
     * Tests that getforceprocessing() sets Long.MAX_VALUE which is the expected force value.
     */
    @Test
    public void testGetforceprocessing_setsExpectedForceValue() throws Exception {
        // When: Calling getforceprocessing()
        task.getforceprocessing();

        // Then: lastModified should be exactly Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                   "lastModified should be exactly Long.MAX_VALUE");
        assertEquals(9223372036854775807L, task.configuration.lastModified,
                   "lastModified should be the specific value of Long.MAX_VALUE");
    }
}

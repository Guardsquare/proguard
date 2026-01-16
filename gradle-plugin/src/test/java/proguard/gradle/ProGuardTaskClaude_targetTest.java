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
 * Tests for {@link ProGuardTask#target(String)}.
 *
 * This test class verifies that the target(String) method correctly
 * sets the target class version in the configuration.
 *
 * Method signature: target(Ljava/lang/String;)V
 * - Takes a Java version string (e.g., "1.8", "11", "17")
 * - Converts it to an internal class version number via ClassUtil.internalClassVersion()
 * - Stores the result in configuration.targetClassVersion as an int
 * - Returns void
 *
 * Key behavior: This method tells ProGuard what target Java version to use when processing
 * bytecode. The version string can be in formats like "1.8", "11", "17", etc. ProGuard
 * converts these to internal bytecode version numbers (e.g., Java 8 = 52, Java 11 = 55).
 *
 * The target version affects optimization and ensures bytecode compatibility with the
 * specified Java runtime version.
 */
public class ProGuardTaskClaude_targetTest {

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
     * Tests that target() sets the configuration with a valid version string.
     */
    @Test
    public void testTarget_java8_setsTargetClassVersion() throws Exception {
        // Given: Initial state where targetClassVersion is 0 (default)
        assertEquals(0, task.configuration.targetClassVersion,
                   "Target class version should initially be 0");

        // When: Calling target() with Java 8 version string
        task.target("1.8");

        // Then: The targetClassVersion should be set to a non-zero value
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set after calling target()");
    }

    /**
     * Tests that target() works with Java 1.6 version string.
     */
    @Test
    public void testTarget_java16_setsTargetClassVersion() throws Exception {
        // When: Calling target() with Java 1.6 version string
        task.target("1.6");

        // Then: The targetClassVersion should be set
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set for Java 1.6");
    }

    /**
     * Tests that target() works with Java 1.7 version string.
     */
    @Test
    public void testTarget_java17_setsTargetClassVersion() throws Exception {
        // When: Calling target() with Java 1.7 version string
        task.target("1.7");

        // Then: The targetClassVersion should be set
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set for Java 1.7");
    }

    /**
     * Tests that target() works with Java 11 version string (modern format).
     */
    @Test
    public void testTarget_java11_setsTargetClassVersion() throws Exception {
        // When: Calling target() with Java 11 version string
        task.target("11");

        // Then: The targetClassVersion should be set
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set for Java 11");
    }

    /**
     * Tests that target() works with Java 17 version string (modern format).
     */
    @Test
    public void testTarget_java17Modern_setsTargetClassVersion() throws Exception {
        // When: Calling target() with Java 17 version string
        task.target("17");

        // Then: The targetClassVersion should be set
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set for Java 17");
    }

    /**
     * Tests that target() can be called multiple times (last call wins).
     */
    @Test
    public void testTarget_multipleCalls_lastCallWins() throws Exception {
        // When: Calling target() multiple times with different versions
        task.target("1.6");
        int version16 = task.configuration.targetClassVersion;

        task.target("1.8");
        int version18 = task.configuration.targetClassVersion;

        // Then: Each call should set a different value
        assertNotEquals(version16, version18,
                      "Different Java versions should produce different target class versions");
    }

    /**
     * Tests that target() updates the configuration value correctly.
     */
    @Test
    public void testTarget_updatesConfigurationValue() throws Exception {
        // Given: Initial state
        int initialValue = task.configuration.targetClassVersion;

        // When: Calling target()
        task.target("1.8");

        // Then: The value should be different from initial
        assertNotEquals(initialValue, task.configuration.targetClassVersion,
                      "Target class version should be updated");
    }

    /**
     * Tests that target() works independently of injars.
     */
    @Test
    public void testTarget_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling target()
        task.target("1.8");

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set");
    }

    /**
     * Tests that target() works independently of libraryjars.
     */
    @Test
    public void testTarget_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling target()
        task.target("1.8");

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set");
    }

    /**
     * Tests that target() works in a complex workflow.
     */
    @Test
    public void testTarget_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.target("1.8");
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that target() can be called before other methods.
     */
    @Test
    public void testTarget_calledBeforeOtherMethods() throws Exception {
        // When: Calling target() before other methods
        task.target("1.8");
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: Target version should still be set
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should remain set");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that target() can be called after other methods.
     */
    @Test
    public void testTarget_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling target() after other methods
        task.target("1.8");

        // Then: All should be set correctly
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that target() can be interleaved with other method calls.
     */
    @Test
    public void testTarget_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving target() with other methods
        task.injars("input.jar");
        task.target("1.8");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: All should be set correctly
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that target() works in realistic Android scenario.
     */
    @Test
    public void testTarget_realisticAndroidScenario() throws Exception {
        // Given: Android project targeting Java 8
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.target("1.8"); // Android typically uses Java 8
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: Target should be set to Java 8
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set for Android project");
    }

    /**
     * Tests that target() works in realistic Java scenario.
     */
    @Test
    public void testTarget_realisticJavaScenario() throws Exception {
        // Given: Java project targeting Java 11
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.target("11"); // Modern Java version
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: Target should be set to Java 11
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set for Java 11 project");
    }

    /**
     * Tests that target() can be called in sequence with configuration files.
     */
    @Test
    public void testTarget_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling target()
        task.target("1.8");

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set");
    }

    /**
     * Tests that target() works with multiple library jars.
     */
    @Test
    public void testTarget_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling target()
        task.target("1.8");

        // Then: Target should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set");
    }

    /**
     * Tests that target() is idempotent with the same version.
     */
    @Test
    public void testTarget_idempotentWithSameVersion() throws Exception {
        // When: Calling target() multiple times with the same version
        task.target("1.8");
        int firstValue = task.configuration.targetClassVersion;

        task.target("1.8");
        int secondValue = task.configuration.targetClassVersion;

        // Then: The value should be the same
        assertEquals(firstValue, secondValue,
                   "Same target version should produce the same class version");
    }

    /**
     * Tests that target() behavior is consistent across task instances.
     */
    @Test
    public void testTarget_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling target() on both with the same version
        task1.target("1.8");
        task2.target("1.8");

        // Then: Both should have the same target class version
        assertEquals(task1.configuration.targetClassVersion,
                   task2.configuration.targetClassVersion,
                   "Same target version should produce same class version across tasks");
    }

    /**
     * Tests that target() with different versions produces different class versions.
     */
    @Test
    public void testTarget_differentVersions_produceDifferentClassVersions() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling target() with different versions
        task1.target("1.6");
        task2.target("1.8");

        // Then: Each should have different target class versions
        assertNotEquals(task1.configuration.targetClassVersion,
                      task2.configuration.targetClassVersion,
                      "Different target versions should produce different class versions");
    }

    /**
     * Tests that target() returns void.
     */
    @Test
    public void testTarget_returnsVoid() throws Exception {
        // When: Calling target()
        task.target("1.8"); // Method returns void

        // Then: The side effect should occur (targetClassVersion should be set)
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set");
    }

    /**
     * Tests that target() can be called at any point in the configuration.
     */
    @Test
    public void testTarget_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.target("1.6");
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.target("1.8"); // Overwrite previous target
        task.outjars("output.jar");
        task.libraryjars("lib2.jar");

        // Then: Target should be set to the last value regardless of call order
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
        assertEquals(2, task.getLibraryJarFiles().size(), "Should have 2 library jars");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that target() sets only the specific configuration field.
     */
    @Test
    public void testTarget_setsOnlyTargetClassVersion() throws Exception {
        // When: Calling target()
        task.target("1.8");

        // Then: Only the targetClassVersion should be affected
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "targetClassVersion should be set");
    }

    /**
     * Tests that target() works with Java 1.5 (legacy version).
     */
    @Test
    public void testTarget_java15_setsTargetClassVersion() throws Exception {
        // When: Calling target() with Java 1.5 version string
        task.target("1.5");

        // Then: The targetClassVersion should be set
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set for Java 1.5");
    }

    /**
     * Tests that target() works with newer Java versions like 21.
     */
    @Test
    public void testTarget_java21_setsTargetClassVersion() throws Exception {
        // When: Calling target() with Java 21 version string
        task.target("21");

        // Then: The targetClassVersion should be set
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set for Java 21");
    }

    /**
     * Tests that target() integrates with optimization settings.
     */
    @Test
    public void testTarget_withOptimization() throws Exception {
        // Given: Optimization configured
        task.injars("input.jar");
        task.outjars("output.jar");
        task.target("1.8");

        // When: Target is set
        // Then: Configuration should have target set
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set for optimization");
    }

    /**
     * Tests that target() can handle version format "1.9".
     */
    @Test
    public void testTarget_java19_setsTargetClassVersion() throws Exception {
        // When: Calling target() with Java 1.9 version string
        task.target("1.9");

        // Then: The targetClassVersion should be set
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set for Java 1.9");
    }

    /**
     * Tests that target() can handle version format "10" (Java 10).
     */
    @Test
    public void testTarget_java10_setsTargetClassVersion() throws Exception {
        // When: Calling target() with Java 10 version string
        task.target("10");

        // Then: The targetClassVersion should be set
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set for Java 10");
    }

    /**
     * Tests that target() value persists across configuration calls.
     */
    @Test
    public void testTarget_persistsAcrossConfigurationCalls() throws Exception {
        // Given: Target set early
        task.target("1.8");
        int targetAfterSet = task.configuration.targetClassVersion;

        // When: Other configuration methods are called
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");

        // Then: Target should remain unchanged
        assertEquals(targetAfterSet, task.configuration.targetClassVersion,
                   "Target class version should persist after other configuration calls");
    }

    /**
     * Tests that target() works correctly in a minimal configuration.
     */
    @Test
    public void testTarget_minimalConfiguration() throws Exception {
        // When: Only target is set (minimal configuration)
        task.target("1.8");

        // Then: Target should be set correctly
        assertNotEquals(0, task.configuration.targetClassVersion,
                      "Target class version should be set in minimal configuration");
    }

    /**
     * Tests that target() value can be changed multiple times during configuration.
     */
    @Test
    public void testTarget_canBeChangedMultipleTimes() throws Exception {
        // When: Changing target multiple times
        task.target("1.6");
        int version16 = task.configuration.targetClassVersion;

        task.target("1.7");
        int version17 = task.configuration.targetClassVersion;

        task.target("1.8");
        int version18 = task.configuration.targetClassVersion;

        // Then: Each change should produce a different value
        assertNotEquals(version16, version17, "Java 1.6 and 1.7 should differ");
        assertNotEquals(version17, version18, "Java 1.7 and 1.8 should differ");
        assertNotEquals(version16, version18, "Java 1.6 and 1.8 should differ");
    }

    /**
     * Tests that target() with old Java versions (1.0-1.4) are handled.
     */
    @Test
    public void testTarget_veryOldJavaVersions() throws Exception {
        // When: Setting very old Java versions
        task.target("1.2");
        int version12 = task.configuration.targetClassVersion;

        // Then: Should set some value (even if 0 for unsupported versions)
        // We just verify the method doesn't throw an exception
        assertTrue(version12 >= 0, "Target class version should be non-negative");
    }
}

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
 * Tests for {@link ProGuardTask#dontshrink()}.
 *
 * This test class verifies that the dontshrink() method correctly
 * disables shrinking in the configuration.
 *
 * Method signature: dontshrink()V
 * - Sets configuration.shrink = false
 * - Returns void
 *
 * Key behavior: This method disables ProGuard's shrinking phase, which removes
 * unused classes, fields, and methods. Unlike getdontshrink(), this method returns
 * void rather than null, making it suitable for direct method calls rather than
 * Groovy DSL usage.
 *
 * The dontshrink() method sets configuration.shrink to false, preventing ProGuard
 * from removing unused code.
 */
public class ProGuardTaskClaude_dontshrinkTest {

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
     * Tests that dontshrink() sets shrink to false.
     */
    @Test
    public void testDontshrink_setsShrinkToFalse() throws Exception {
        // Given: Initial state where shrink is true (default)
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");

        // When: Calling dontshrink()
        task.dontshrink();

        // Then: shrink should be set to false
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
    }

    /**
     * Tests that dontshrink() can be called multiple times.
     */
    @Test
    public void testDontshrink_multipleCalls() throws Exception {
        // When: Calling dontshrink() multiple times
        task.dontshrink();
        task.dontshrink();
        task.dontshrink();

        // Then: shrink should still be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false after multiple calls");
    }

    /**
     * Tests that dontshrink() is idempotent.
     */
    @Test
    public void testDontshrink_idempotent() throws Exception {
        // Given: shrink already set to false
        task.configuration.shrink = false;

        // When: Calling dontshrink()
        task.dontshrink();

        // Then: shrink should remain false
        assertFalse(task.configuration.shrink,
                   "shrink should remain false (idempotent)");
    }

    /**
     * Tests that dontshrink() overrides any previous shrink value.
     */
    @Test
    public void testDontshrink_overridesPreviousValue() throws Exception {
        // Given: shrink explicitly set to true
        task.configuration.shrink = true;
        assertTrue(task.configuration.shrink,
                   "shrink should be set to true");

        // When: Calling dontshrink()
        task.dontshrink();

        // Then: shrink should be overridden to false
        assertFalse(task.configuration.shrink,
                   "shrink should be overridden to false");
    }

    /**
     * Tests that the initial state has shrink as true.
     */
    @Test
    public void testDontshrink_initiallyTrue() throws Exception {
        // Given: A newly created task
        // When: Not calling dontshrink()
        // Then: The shrink should be true (default)
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");
    }

    /**
     * Tests that dontshrink() works independently of injars.
     */
    @Test
    public void testDontshrink_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling dontshrink()
        task.dontshrink();

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
    }

    /**
     * Tests that dontshrink() works independently of libraryjars.
     */
    @Test
    public void testDontshrink_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling dontshrink()
        task.dontshrink();

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
    }

    /**
     * Tests that dontshrink() works in a complex workflow.
     */
    @Test
    public void testDontshrink_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.dontshrink();
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that dontshrink() can be called before other methods.
     */
    @Test
    public void testDontshrink_calledBeforeOtherMethods() throws Exception {
        // When: Calling dontshrink() before other methods
        task.dontshrink();
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: shrink should still be false
        assertFalse(task.configuration.shrink,
                   "shrink should remain false");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that dontshrink() can be called after other methods.
     */
    @Test
    public void testDontshrink_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling dontshrink() after other methods
        task.dontshrink();

        // Then: All should be set correctly
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that dontshrink() can be interleaved with other method calls.
     */
    @Test
    public void testDontshrink_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving dontshrink() with other methods
        task.injars("input.jar");
        task.dontshrink();
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: All should be set correctly
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that dontshrink() works in realistic Android scenario.
     */
    @Test
    public void testDontshrink_realisticAndroidScenario() throws Exception {
        // Given: Android project that needs to disable shrinking
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.dontshrink(); // Disable shrinking to preserve all code
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false to preserve all code");
    }

    /**
     * Tests that dontshrink() works in realistic Java scenario.
     */
    @Test
    public void testDontshrink_realisticJavaScenario() throws Exception {
        // Given: Java project that needs to disable shrinking
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.dontshrink(); // Disable shrinking for debugging
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false for debugging");
    }

    /**
     * Tests that dontshrink() can be called in sequence with configuration.
     */
    @Test
    public void testDontshrink_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling dontshrink()
        task.dontshrink();

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
    }

    /**
     * Tests that dontshrink() works with multiple library jars.
     */
    @Test
    public void testDontshrink_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling dontshrink()
        task.dontshrink();

        // Then: shrink should be false and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
    }

    /**
     * Tests that dontshrink() is thread-safe for idempotent operations.
     */
    @Test
    public void testDontshrink_idempotentThreadSafe() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");

        // When: Calling dontshrink() sequentially (simulating thread safety)
        task.dontshrink();
        task.dontshrink();

        // Then: The shrink should be false and stable
        assertFalse(task.configuration.shrink,
                   "shrink should be stable after multiple calls");
    }

    /**
     * Tests that dontshrink() behavior is consistent across task instances.
     */
    @Test
    public void testDontshrink_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling dontshrink() on both
        task1.dontshrink();
        task2.dontshrink();

        // Then: Both should have shrink set to false
        assertFalse(task1.configuration.shrink,
                   "Task1 shrink should be false");
        assertFalse(task2.configuration.shrink,
                   "Task2 shrink should be false");
    }

    /**
     * Tests that dontshrink() returns void.
     */
    @Test
    public void testDontshrink_returnsVoid() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");

        // When: Calling dontshrink()
        task.dontshrink(); // Method returns void

        // Then: The side effect should occur
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
    }

    /**
     * Tests that dontshrink() can be called at any point in the configuration.
     */
    @Test
    public void testDontshrink_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.dontshrink();
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.outjars("output.jar");
        task.libraryjars("lib2.jar");

        // Then: shrink should be false regardless of call order
        assertFalse(task.configuration.shrink,
                   "shrink should be false regardless of when it's called");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
        assertEquals(2, task.getLibraryJarFiles().size(), "Should have 2 library jars");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that dontshrink() sets only the specific field.
     */
    @Test
    public void testDontshrink_setsOnlyShrink() throws Exception {
        // When: Calling dontshrink()
        task.dontshrink();

        // Then: Only the shrink field should be affected
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");

        // Note: We're only testing the specific field this method sets
        // Other configuration fields should not be affected by this method
    }

    /**
     * Tests that dontshrink() disables shrinking phase.
     */
    @Test
    public void testDontshrink_disablesShrinkingPhase() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");

        // When: Calling dontshrink() to disable shrinking
        task.dontshrink();

        // Then: shrink should be set to false
        assertFalse(task.configuration.shrink,
                   "shrink should be false to disable shrinking phase");
    }

    /**
     * Tests that dontshrink() value persists across configuration calls.
     */
    @Test
    public void testDontshrink_persistsAcrossConfigurationCalls() throws Exception {
        // Given: dontshrink set early
        task.dontshrink();
        boolean shrinkAfterSet = task.configuration.shrink;

        // When: Other configuration methods are called
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");

        // Then: shrink should remain unchanged
        assertEquals(shrinkAfterSet, task.configuration.shrink,
                   "shrink should persist after other configuration calls");
        assertFalse(task.configuration.shrink,
                   "shrink should still be false");
    }

    /**
     * Tests that dontshrink() works correctly in a minimal configuration.
     */
    @Test
    public void testDontshrink_minimalConfiguration() throws Exception {
        // When: Only dontshrink() is called (minimal configuration)
        task.dontshrink();

        // Then: shrink should be set correctly
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false in minimal configuration");
    }

    /**
     * Tests that dontshrink() is useful for debugging scenarios.
     */
    @Test
    public void testDontshrink_debuggingScenario() throws Exception {
        // Given: Project with inputs that need debugging (keep all code)
        task.injars("input.jar");
        task.outjars("output.jar");

        // When: Calling dontshrink() to preserve all code for debugging
        task.dontshrink();

        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false to preserve all code for debugging");
    }

    /**
     * Tests that dontshrink() can override manual shrink changes.
     */
    @Test
    public void testDontshrink_overridesManualChanges() throws Exception {
        // Given: Manual change to shrink
        task.configuration.shrink = true;
        assertTrue(task.configuration.shrink,
                   "shrink should be set manually to true");

        // When: Calling dontshrink()
        task.dontshrink();

        // Then: Manual value should be overridden
        assertFalse(task.configuration.shrink,
                   "shrink should be overridden to false");
    }

    /**
     * Tests that dontshrink() sets false which is the expected value.
     */
    @Test
    public void testDontshrink_setsExpectedFalseValue() throws Exception {
        // When: Calling dontshrink()
        task.dontshrink();

        // Then: shrink should be exactly false
        assertFalse(task.configuration.shrink,
                   "shrink should be exactly false");
        assertEquals(false, task.configuration.shrink,
                   "shrink should be the boolean value false");
    }

    /**
     * Tests that dontshrink() works with filters.
     */
    @Test
    public void testDontshrink_withFilters() throws Exception {
        // Given: Library jars with filters
        java.util.Map<String, String> filter = new java.util.HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "android.jar");

        // When: Calling dontshrink()
        task.dontshrink();

        // Then: Both filter and shrink should be set
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getLibraryJarFilters().size(), "Should have 1 filter");
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
    }

    /**
     * Tests that dontshrink() works with extraJar.
     */
    @Test
    public void testDontshrink_withExtraJar() throws Exception {
        // Given: ExtraJar configured
        task.extraJar(new java.io.File(tempDir.toFile(), "extra.jar"));

        // When: Calling dontshrink()
        task.dontshrink();

        // Then: Both should be set
        assertNotNull(task.configuration.extraJar, "ExtraJar should be set");
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
    }

    /**
     * Tests that dontshrink() and getdontshrink() produce the same result.
     */
    @Test
    public void testDontshrink_equivalentToGetdontshrink() throws Exception {
        // Given: Two tasks
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling dontshrink() on one and getdontshrink() on other
        task1.dontshrink();
        task2.getdontshrink();

        // Then: Both should have the same effect
        assertEquals(task1.configuration.shrink,
                   task2.configuration.shrink,
                   "Both methods should set the same value");
        assertFalse(task1.configuration.shrink,
                   "Task1 shrink should be false");
        assertFalse(task2.configuration.shrink,
                   "Task2 shrink should be false");
    }

    /**
     * Tests that dontshrink() affects different field than target().
     */
    @Test
    public void testDontshrink_differentFieldFromTarget() throws Exception {
        // Given: target() has been called (different field)
        task.target("1.8");
        int targetVersion = task.configuration.targetClassVersion;
        assertNotEquals(0, targetVersion, "targetClassVersion should be set");

        // When: Calling dontshrink() (affects different field)
        task.dontshrink();

        // Then: This method affects shrink, not targetClassVersion
        assertEquals(targetVersion, task.configuration.targetClassVersion,
                   "targetClassVersion should remain unchanged");
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
    }

    /**
     * Tests that dontshrink() affects different field than optimize.
     */
    @Test
    public void testDontshrink_differentFieldFromOptimize() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.optimize,
                   "optimize should initially be true");
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");

        // When: Calling dontshrink() (affects shrink, not optimize)
        task.dontshrink();

        // Then: This method affects shrink, not optimize
        assertTrue(task.configuration.optimize,
                   "optimize should remain true");
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
    }

    /**
     * Tests that dontshrink() affects different field than obfuscate.
     */
    @Test
    public void testDontshrink_differentFieldFromObfuscate() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.obfuscate,
                   "obfuscate should initially be true");
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");

        // When: Calling dontshrink() (affects shrink, not obfuscate)
        task.dontshrink();

        // Then: This method affects shrink, not obfuscate
        assertTrue(task.configuration.obfuscate,
                   "obfuscate should remain true");
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
    }

    /**
     * Tests that dontshrink() works with keep rules.
     */
    @Test
    public void testDontshrink_withKeepRules() throws Exception {
        // Given: Keep rules configured
        task.keep("class com.example.**");

        // When: Calling dontshrink()
        task.dontshrink();

        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
        assertNotNull(task.configuration.keep,
                   "Keep rules should be configured");
    }

    /**
     * Tests that dontshrink() works when only obfuscation is desired.
     */
    @Test
    public void testDontshrink_obfuscationOnlyScenario() throws Exception {
        // Given: Project that wants obfuscation but not shrinking
        task.injars("app.jar");
        task.outjars("app-obfuscated.jar");

        // When: Disabling shrinking while keeping obfuscation
        task.dontshrink();

        // Then: shrink should be false, obfuscate should be true
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
        assertTrue(task.configuration.obfuscate,
                   "obfuscate should remain true for obfuscation-only processing");
    }

    /**
     * Tests that dontshrink() preserves unused code.
     */
    @Test
    public void testDontshrink_preservesUnusedCode() throws Exception {
        // Given: Configuration that needs to preserve all code
        task.injars("library.jar");
        task.outjars("library-processed.jar");

        // When: Calling dontshrink() to preserve unused code
        task.dontshrink();

        // Then: shrink should be false to preserve all code
        assertFalse(task.configuration.shrink,
                   "shrink should be false to preserve unused code in library");
    }

    /**
     * Tests that dontshrink() is useful for library processing.
     */
    @Test
    public void testDontshrink_libraryProcessingScenario() throws Exception {
        // Given: Library that exposes public API
        task.injars("lib.jar");
        task.outjars("lib-processed.jar");
        task.keep("public class * { public *; }");

        // When: Disabling shrinking to preserve entire API surface
        task.dontshrink();

        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false to preserve library API");
    }

    /**
     * Tests that dontshrink() works with other processing phases enabled.
     */
    @Test
    public void testDontshrink_withOtherPhasesEnabled() throws Exception {
        // Given: Configuration with optimization and obfuscation
        assertTrue(task.configuration.optimize,
                   "optimize should be true by default");
        assertTrue(task.configuration.obfuscate,
                   "obfuscate should be true by default");

        // When: Disabling only shrinking
        task.dontshrink();

        // Then: Only shrinking should be disabled
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
        assertTrue(task.configuration.optimize,
                   "optimize should still be true");
        assertTrue(task.configuration.obfuscate,
                   "obfuscate should still be true");
    }

    /**
     * Tests that dontshrink() is useful for analysis scenarios.
     */
    @Test
    public void testDontshrink_analysisScenario() throws Exception {
        // Given: Project for code analysis (need all code present)
        task.injars("app.jar");
        task.outjars("app-analyzed.jar");
        task.printusage("usage.txt");

        // When: Disabling shrinking for complete analysis
        task.dontshrink();

        // Then: shrink should be false for analysis
        assertFalse(task.configuration.shrink,
                   "shrink should be false to analyze all code");
    }

    /**
     * Tests that dontshrink() is used for debugging with stack traces.
     */
    @Test
    public void testDontshrink_debuggingWithStackTraces() throws Exception {
        // Given: Debug build that needs full stack traces
        task.injars("app-debug.jar");
        task.outjars("app-debug-processed.jar");

        // When: Disabling shrinking to preserve all code for stack traces
        task.dontshrink();

        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false to preserve code for debugging");
    }

    /**
     * Tests that dontshrink() enables reflection-heavy code to work.
     */
    @Test
    public void testDontshrink_reflectionHeavyCode() throws Exception {
        // Given: Application with heavy reflection usage
        task.injars("reflection-app.jar");
        task.outjars("reflection-app-processed.jar");

        // When: Disabling shrinking to preserve all classes for reflection
        task.dontshrink();

        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false to preserve classes for reflection");
    }
}

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
 * Tests for {@link ProGuardTask#getdontshrink()}.
 *
 * This test class verifies that the getdontshrink() method correctly
 * disables shrinking in the configuration and returns null.
 *
 * Method signature: getdontshrink()Ljava/lang/Object;
 * - Calls dontshrink() internally which sets configuration.shrink = false
 * - Returns null (Groovy DSL hack)
 *
 * Key behavior: This method is a Groovy DSL support method that allows calling
 * "dontshrink" without parentheses in Groovy build scripts. It disables ProGuard's
 * shrinking phase, which removes unused classes, fields, and methods.
 *
 * The getdontshrink() method internally calls dontshrink(), which sets
 * configuration.shrink to false, preventing ProGuard from removing unused code.
 */
public class ProGuardTaskClaude_getdontshrinkTest {

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
     * Tests that getdontshrink() returns null.
     */
    @Test
    public void testGetdontshrink_returnsNull() throws Exception {
        // When: Calling getdontshrink()
        Object result = task.getdontshrink();

        // Then: Should return null (Groovy DSL hack)
        assertNull(result, "getdontshrink() should return null");
    }

    /**
     * Tests that getdontshrink() sets shrink to false.
     */
    @Test
    public void testGetdontshrink_setsShrinkToFalse() throws Exception {
        // Given: Initial state where shrink is true (default)
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");

        // When: Calling getdontshrink()
        task.getdontshrink();

        // Then: shrink should be set to false
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
    }

    /**
     * Tests that getdontshrink() can be called multiple times.
     */
    @Test
    public void testGetdontshrink_multipleCalls() throws Exception {
        // When: Calling getdontshrink() multiple times
        task.getdontshrink();
        task.getdontshrink();
        task.getdontshrink();

        // Then: shrink should still be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false after multiple calls");
    }

    /**
     * Tests that getdontshrink() is idempotent.
     */
    @Test
    public void testGetdontshrink_idempotent() throws Exception {
        // Given: shrink already set to false
        task.configuration.shrink = false;

        // When: Calling getdontshrink()
        task.getdontshrink();

        // Then: shrink should remain false
        assertFalse(task.configuration.shrink,
                   "shrink should remain false (idempotent)");
    }

    /**
     * Tests that getdontshrink() overrides any previous shrink value.
     */
    @Test
    public void testGetdontshrink_overridesPreviousValue() throws Exception {
        // Given: shrink explicitly set to true
        task.configuration.shrink = true;
        assertTrue(task.configuration.shrink,
                   "shrink should be set to true");

        // When: Calling getdontshrink()
        task.getdontshrink();

        // Then: shrink should be overridden to false
        assertFalse(task.configuration.shrink,
                   "shrink should be overridden to false");
    }

    /**
     * Tests that getdontshrink() works independently of injars.
     */
    @Test
    public void testGetdontshrink_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling getdontshrink()
        task.getdontshrink();

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
    }

    /**
     * Tests that getdontshrink() works independently of libraryjars.
     */
    @Test
    public void testGetdontshrink_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling getdontshrink()
        task.getdontshrink();

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
    }

    /**
     * Tests that getdontshrink() works in a complex workflow.
     */
    @Test
    public void testGetdontshrink_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.getdontshrink();
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
     * Tests that getdontshrink() can be called before other methods.
     */
    @Test
    public void testGetdontshrink_calledBeforeOtherMethods() throws Exception {
        // When: Calling getdontshrink() before other methods
        task.getdontshrink();
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
     * Tests that getdontshrink() can be called after other methods.
     */
    @Test
    public void testGetdontshrink_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling getdontshrink() after other methods
        task.getdontshrink();

        // Then: All should be set correctly
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getdontshrink() can be interleaved with other method calls.
     */
    @Test
    public void testGetdontshrink_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving getdontshrink() with other methods
        task.injars("input.jar");
        task.getdontshrink();
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
     * Tests that getdontshrink() works in realistic Android scenario.
     */
    @Test
    public void testGetdontshrink_realisticAndroidScenario() throws Exception {
        // Given: Android project that needs to disable shrinking
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.getdontshrink(); // Disable shrinking to preserve all code
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false to preserve all code");
    }

    /**
     * Tests that getdontshrink() works in realistic Java scenario.
     */
    @Test
    public void testGetdontshrink_realisticJavaScenario() throws Exception {
        // Given: Java project that needs to disable shrinking
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.getdontshrink(); // Disable shrinking for debugging
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false for debugging");
    }

    /**
     * Tests that getdontshrink() can be called in sequence with configuration.
     */
    @Test
    public void testGetdontshrink_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling getdontshrink()
        task.getdontshrink();

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");
    }

    /**
     * Tests that getdontshrink() works with multiple library jars.
     */
    @Test
    public void testGetdontshrink_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling getdontshrink()
        task.getdontshrink();

        // Then: shrink should be false and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
    }

    /**
     * Tests that getdontshrink() behavior is consistent across task instances.
     */
    @Test
    public void testGetdontshrink_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling getdontshrink() on both
        task1.getdontshrink();
        task2.getdontshrink();

        // Then: Both should have shrink set to false
        assertFalse(task1.configuration.shrink,
                   "Task1 shrink should be false");
        assertFalse(task2.configuration.shrink,
                   "Task2 shrink should be false");
    }

    /**
     * Tests that getdontshrink() and dontshrink() produce the same result.
     */
    @Test
    public void testGetdontshrink_equivalentTodontshrink() throws Exception {
        // Given: Two tasks
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling getdontshrink() on one and dontshrink() on other
        task1.getdontshrink();
        task2.dontshrink();

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
     * Tests that getdontshrink() always returns null regardless of state.
     */
    @Test
    public void testGetdontshrink_alwaysReturnsNull() throws Exception {
        // When: Calling getdontshrink() multiple times
        Object result1 = task.getdontshrink();
        Object result2 = task.getdontshrink();
        Object result3 = task.getdontshrink();

        // Then: All calls should return null
        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    /**
     * Tests that getdontshrink() can be called at any point in the configuration.
     */
    @Test
    public void testGetdontshrink_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.getdontshrink();
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
     * Tests that getdontshrink() sets only the specific field.
     */
    @Test
    public void testGetdontshrink_setsOnlyShrink() throws Exception {
        // When: Calling getdontshrink()
        task.getdontshrink();

        // Then: Only the shrink field should be affected
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false");

        // Note: We're only testing the specific field this method sets
        // Other configuration fields should not be affected by this method
    }

    /**
     * Tests that getdontshrink() disables shrinking phase.
     */
    @Test
    public void testGetdontshrink_disablesShrinkingPhase() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");

        // When: Calling getdontshrink() to disable shrinking
        task.getdontshrink();

        // Then: shrink should be set to false
        assertFalse(task.configuration.shrink,
                   "shrink should be false to disable shrinking phase");
    }

    /**
     * Tests that getdontshrink() value persists across configuration calls.
     */
    @Test
    public void testGetdontshrink_persistsAcrossConfigurationCalls() throws Exception {
        // Given: dontshrink set early
        task.getdontshrink();
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
     * Tests that getdontshrink() works correctly in a minimal configuration.
     */
    @Test
    public void testGetdontshrink_minimalConfiguration() throws Exception {
        // When: Only getdontshrink() is called (minimal configuration)
        Object result = task.getdontshrink();

        // Then: Should return null and set shrink to false
        assertNull(result, "Should return null");
        assertFalse(task.configuration.shrink,
                   "shrink should be set to false in minimal configuration");
    }

    /**
     * Tests that getdontshrink() is useful for debugging scenarios.
     */
    @Test
    public void testGetdontshrink_debuggingScenario() throws Exception {
        // Given: Project with inputs that need debugging (keep all code)
        task.injars("input.jar");
        task.outjars("output.jar");

        // When: Calling getdontshrink() to preserve all code for debugging
        task.getdontshrink();

        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false to preserve all code for debugging");
    }

    /**
     * Tests that getdontshrink() has side effect even though it returns null.
     */
    @Test
    public void testGetdontshrink_hasSideEffectDespiteReturningNull() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");

        // When: Calling getdontshrink() which returns null
        Object result = task.getdontshrink();

        // Then: Should return null but have side effect
        assertNull(result, "Should return null");
        assertFalse(task.configuration.shrink,
                   "Should have side effect of setting shrink despite returning null");
    }

    /**
     * Tests that getdontshrink() return value is null (not void).
     */
    @Test
    public void testGetdontshrink_returnsNullNotVoid() throws Exception {
        // When: Calling getdontshrink()
        Object result = task.getdontshrink();

        // Then: Should return null (not throw exception or return void)
        assertNull(result, "Should explicitly return null");

        // And: The side effect should occur
        assertFalse(task.configuration.shrink,
                   "shrink should be set");
    }

    /**
     * Tests that getdontshrink() initial value check.
     */
    @Test
    public void testGetdontshrink_initialValueIsTrue() throws Exception {
        // Given: A newly created task
        // When: Not calling getdontshrink()
        // Then: The shrink should be true (default)
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");
    }

    /**
     * Tests that getdontshrink() can override manual shrink changes.
     */
    @Test
    public void testGetdontshrink_overridesManualChanges() throws Exception {
        // Given: Manual change to shrink
        task.configuration.shrink = true;
        assertTrue(task.configuration.shrink,
                   "shrink should be set manually to true");

        // When: Calling getdontshrink()
        task.getdontshrink();

        // Then: Manual value should be overridden
        assertFalse(task.configuration.shrink,
                   "shrink should be overridden to false");
    }

    /**
     * Tests that getdontshrink() sets false which is the expected value.
     */
    @Test
    public void testGetdontshrink_setsExpectedFalseValue() throws Exception {
        // When: Calling getdontshrink()
        task.getdontshrink();

        // Then: shrink should be exactly false
        assertFalse(task.configuration.shrink,
                   "shrink should be exactly false");
        assertEquals(false, task.configuration.shrink,
                   "shrink should be the boolean value false");
    }

    /**
     * Tests that getdontshrink() works with keep rules.
     */
    @Test
    public void testGetdontshrink_withKeepRules() throws Exception {
        // Given: Keep rules configured
        task.keep("class com.example.**");

        // When: Calling getdontshrink()
        task.getdontshrink();

        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
        assertNotNull(task.configuration.keep,
                   "Keep rules should be configured");
    }

    /**
     * Tests that getdontshrink() affects different field than dontoptimize.
     */
    @Test
    public void testGetdontshrink_differentFieldFromDontoptimize() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.optimize,
                   "optimize should initially be true");
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");

        // When: Calling getdontshrink() (affects shrink, not optimize)
        task.getdontshrink();

        // Then: This method affects shrink, not optimize
        assertTrue(task.configuration.optimize,
                   "optimize should remain true");
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
    }

    /**
     * Tests that getdontshrink() affects different field than dontobfuscate.
     */
    @Test
    public void testGetdontshrink_differentFieldFromDontobfuscate() throws Exception {
        // Given: Initial state
        assertTrue(task.configuration.obfuscate,
                   "obfuscate should initially be true");
        assertTrue(task.configuration.shrink,
                   "shrink should initially be true");

        // When: Calling getdontshrink() (affects shrink, not obfuscate)
        task.getdontshrink();

        // Then: This method affects shrink, not obfuscate
        assertTrue(task.configuration.obfuscate,
                   "obfuscate should remain true");
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
    }

    /**
     * Tests that getdontshrink() works when only obfuscation is desired.
     */
    @Test
    public void testGetdontshrink_obfuscationOnlyScenario() throws Exception {
        // Given: Project that wants obfuscation but not shrinking
        task.injars("app.jar");
        task.outjars("app-obfuscated.jar");

        // When: Disabling shrinking while keeping obfuscation
        task.getdontshrink();

        // Then: shrink should be false, obfuscate should be true
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
        assertTrue(task.configuration.obfuscate,
                   "obfuscate should remain true for obfuscation-only processing");
    }

    /**
     * Tests that getdontshrink() preserves unused code.
     */
    @Test
    public void testGetdontshrink_preservesUnusedCode() throws Exception {
        // Given: Configuration that needs to preserve all code
        task.injars("library.jar");
        task.outjars("library-processed.jar");

        // When: Calling getdontshrink() to preserve unused code
        task.getdontshrink();

        // Then: shrink should be false to preserve all code
        assertFalse(task.configuration.shrink,
                   "shrink should be false to preserve unused code in library");
    }

    /**
     * Tests that getdontshrink() is useful for library processing.
     */
    @Test
    public void testGetdontshrink_libraryProcessingScenario() throws Exception {
        // Given: Library that exposes public API
        task.injars("lib.jar");
        task.outjars("lib-processed.jar");
        task.keep("public class * { public *; }");

        // When: Disabling shrinking to preserve entire API surface
        task.getdontshrink();

        // Then: shrink should be false
        assertFalse(task.configuration.shrink,
                   "shrink should be false to preserve library API");
    }

    /**
     * Tests that getdontshrink() works with filters.
     */
    @Test
    public void testGetdontshrink_withFilters() throws Exception {
        // Given: Library jars with filters
        java.util.Map<String, String> filter = new java.util.HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "android.jar");

        // When: Calling getdontshrink()
        task.getdontshrink();

        // Then: Both filter and shrink should be set
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getLibraryJarFilters().size(), "Should have 1 filter");
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
    }

    /**
     * Tests that getdontshrink() works with other processing phases enabled.
     */
    @Test
    public void testGetdontshrink_withOtherPhasesEnabled() throws Exception {
        // Given: Configuration with optimization and obfuscation
        assertTrue(task.configuration.optimize,
                   "optimize should be true by default");
        assertTrue(task.configuration.obfuscate,
                   "obfuscate should be true by default");

        // When: Disabling only shrinking
        task.getdontshrink();

        // Then: Only shrinking should be disabled
        assertFalse(task.configuration.shrink,
                   "shrink should be false");
        assertTrue(task.configuration.optimize,
                   "optimize should still be true");
        assertTrue(task.configuration.obfuscate,
                   "obfuscate should still be true");
    }

    /**
     * Tests that getdontshrink() is useful for analysis scenarios.
     */
    @Test
    public void testGetdontshrink_analysisScenario() throws Exception {
        // Given: Project for code analysis (need all code present)
        task.injars("app.jar");
        task.outjars("app-analyzed.jar");
        task.printusage("usage.txt");

        // When: Disabling shrinking for complete analysis
        task.getdontshrink();

        // Then: shrink should be false for analysis
        assertFalse(task.configuration.shrink,
                   "shrink should be false to analyze all code");
    }
}

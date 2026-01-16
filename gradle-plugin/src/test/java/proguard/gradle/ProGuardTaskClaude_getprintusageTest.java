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
import proguard.Configuration;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#getprintusage()}.
 *
 * This test class verifies that the getprintusage() method correctly
 * sets the printUsage field in the configuration and returns null.
 *
 * Method signature: getprintusage()Ljava/lang/Object;
 * - Calls printusage() internally which sets configuration.printUsage = Configuration.STD_OUT
 * - Returns null (Groovy DSL hack)
 *
 * Key behavior: This method is a Groovy DSL support method that allows calling
 * "printusage" without parentheses in Groovy build scripts. It enables ProGuard
 * to print the list of unused classes, fields, and methods to stdout.
 *
 * The getprintusage() method internally calls printusage(), which sets
 * configuration.printUsage to Configuration.STD_OUT, causing usage information
 * to be printed to standard output during processing.
 */
public class ProGuardTaskClaude_getprintusageTest {

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
     * Tests that getprintusage() returns null.
     */
    @Test
    public void testGetprintusage_returnsNull() throws Exception {
        // When: Calling getprintusage()
        Object result = task.getprintusage();

        // Then: Should return null (Groovy DSL hack)
        assertNull(result, "getprintusage() should return null");
    }

    /**
     * Tests that getprintusage() sets printUsage to Configuration.STD_OUT.
     */
    @Test
    public void testGetprintusage_setsPrintUsageToStdOut() throws Exception {
        // Given: Initial state where printUsage is null (default)
        assertNull(task.configuration.printUsage,
                   "printUsage should initially be null");

        // When: Calling getprintusage()
        task.getprintusage();

        // Then: printUsage should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintusage() can be called multiple times.
     */
    @Test
    public void testGetprintusage_multipleCalls() throws Exception {
        // When: Calling getprintusage() multiple times
        task.getprintusage();
        task.getprintusage();
        task.getprintusage();

        // Then: printUsage should still be Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT after multiple calls");
    }

    /**
     * Tests that getprintusage() is idempotent.
     */
    @Test
    public void testGetprintusage_idempotent() throws Exception {
        // Given: printUsage already set to Configuration.STD_OUT
        task.configuration.printUsage = Configuration.STD_OUT;

        // When: Calling getprintusage()
        task.getprintusage();

        // Then: printUsage should remain Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should remain Configuration.STD_OUT (idempotent)");
    }

    /**
     * Tests that getprintusage() overrides any previous printUsage value.
     */
    @Test
    public void testGetprintusage_overridesPreviousValue() throws Exception {
        // Given: printUsage set to some other value
        java.io.File customFile = tempDir.resolve("custom-usage.txt").toFile();
        task.configuration.printUsage = customFile;
        assertEquals(customFile, task.configuration.printUsage,
                   "printUsage should be set to custom file");

        // When: Calling getprintusage()
        task.getprintusage();

        // Then: printUsage should be overridden to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be overridden to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintusage() works independently of injars.
     */
    @Test
    public void testGetprintusage_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling getprintusage()
        task.getprintusage();

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintusage() works independently of libraryjars.
     */
    @Test
    public void testGetprintusage_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling getprintusage()
        task.getprintusage();

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintusage() works in a complex workflow.
     */
    @Test
    public void testGetprintusage_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.getprintusage();
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be set to Configuration.STD_OUT");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that getprintusage() can be called before other methods.
     */
    @Test
    public void testGetprintusage_calledBeforeOtherMethods() throws Exception {
        // When: Calling getprintusage() before other methods
        task.getprintusage();
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: printUsage should still be set
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should remain Configuration.STD_OUT");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getprintusage() can be called after other methods.
     */
    @Test
    public void testGetprintusage_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling getprintusage() after other methods
        task.getprintusage();

        // Then: All should be set correctly
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be set to Configuration.STD_OUT");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getprintusage() can be interleaved with other method calls.
     */
    @Test
    public void testGetprintusage_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving getprintusage() with other methods
        task.injars("input.jar");
        task.getprintusage();
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: All should be set correctly
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be set to Configuration.STD_OUT");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getprintusage() works in realistic Android scenario.
     */
    @Test
    public void testGetprintusage_realisticAndroidScenario() throws Exception {
        // Given: Android project that needs usage information
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.getprintusage(); // Print usage to see what's being removed
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: printUsage should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT for debugging");
    }

    /**
     * Tests that getprintusage() works in realistic Java scenario.
     */
    @Test
    public void testGetprintusage_realisticJavaScenario() throws Exception {
        // Given: Java project that needs usage information
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.getprintusage(); // Print usage to verify dead code removal
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: printUsage should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT for verification");
    }

    /**
     * Tests that getprintusage() can be called in sequence with configuration.
     */
    @Test
    public void testGetprintusage_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling getprintusage()
        task.getprintusage();

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintusage() works with multiple library jars.
     */
    @Test
    public void testGetprintusage_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling getprintusage()
        task.getprintusage();

        // Then: printUsage should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT");
    }

    /**
     * Tests that getprintusage() behavior is consistent across task instances.
     */
    @Test
    public void testGetprintusage_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling getprintusage() on both
        task1.getprintusage();
        task2.getprintusage();

        // Then: Both should have printUsage set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task1.configuration.printUsage,
                   "Task1 printUsage should be Configuration.STD_OUT");
        assertEquals(Configuration.STD_OUT, task2.configuration.printUsage,
                   "Task2 printUsage should be Configuration.STD_OUT");
    }

    /**
     * Tests that getprintusage() and printusage() produce the same result.
     */
    @Test
    public void testGetprintusage_equivalentToPrintusage() throws Exception {
        // Given: Two tasks
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling getprintusage() on one and printusage() on other
        task1.getprintusage();
        task2.printusage();

        // Then: Both should have the same effect
        assertEquals(task1.configuration.printUsage,
                   task2.configuration.printUsage,
                   "Both methods should set the same value");
        assertEquals(Configuration.STD_OUT, task1.configuration.printUsage,
                   "Task1 printUsage should be Configuration.STD_OUT");
        assertEquals(Configuration.STD_OUT, task2.configuration.printUsage,
                   "Task2 printUsage should be Configuration.STD_OUT");
    }

    /**
     * Tests that getprintusage() always returns null regardless of state.
     */
    @Test
    public void testGetprintusage_alwaysReturnsNull() throws Exception {
        // When: Calling getprintusage() multiple times
        Object result1 = task.getprintusage();
        Object result2 = task.getprintusage();
        Object result3 = task.getprintusage();

        // Then: All calls should return null
        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    /**
     * Tests that getprintusage() can be called at any point in the configuration.
     */
    @Test
    public void testGetprintusage_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.getprintusage();
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.outjars("output.jar");
        task.libraryjars("lib2.jar");

        // Then: printUsage should be set regardless of call order
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT regardless of when it's called");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
        assertEquals(2, task.getLibraryJarFiles().size(), "Should have 2 library jars");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getprintusage() sets only the specific field.
     */
    @Test
    public void testGetprintusage_setsOnlyPrintUsage() throws Exception {
        // When: Calling getprintusage()
        task.getprintusage();

        // Then: Only the printUsage field should be affected
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be set to Configuration.STD_OUT");

        // Note: We're only testing the specific field this method sets
        // Other configuration fields should not be affected by this method
    }

    /**
     * Tests that getprintusage() enables usage output to stdout.
     */
    @Test
    public void testGetprintusage_enablesUsageOutputToStdout() throws Exception {
        // Given: Initial state
        assertNull(task.configuration.printUsage,
                   "printUsage should initially be null");

        // When: Calling getprintusage() to enable usage output
        task.getprintusage();

        // Then: printUsage should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT to enable stdout output");
    }

    /**
     * Tests that getprintusage() value persists across configuration calls.
     */
    @Test
    public void testGetprintusage_persistsAcrossConfigurationCalls() throws Exception {
        // Given: printusage set early
        task.getprintusage();
        Object printUsageAfterSet = task.configuration.printUsage;

        // When: Other configuration methods are called
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");

        // Then: printUsage should remain unchanged
        assertEquals(printUsageAfterSet, task.configuration.printUsage,
                   "printUsage should persist after other configuration calls");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should still be Configuration.STD_OUT");
    }

    /**
     * Tests that getprintusage() works correctly in a minimal configuration.
     */
    @Test
    public void testGetprintusage_minimalConfiguration() throws Exception {
        // When: Only getprintusage() is called (minimal configuration)
        Object result = task.getprintusage();

        // Then: Should return null and set printUsage
        assertNull(result, "Should return null");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be set to Configuration.STD_OUT in minimal configuration");
    }

    /**
     * Tests that getprintusage() is useful for debugging dead code removal.
     */
    @Test
    public void testGetprintusage_usefulForDebuggingDeadCodeRemoval() throws Exception {
        // Given: Project with shrinking enabled
        task.injars("input.jar");
        task.outjars("output.jar");

        // When: Calling getprintusage() to see what's being removed
        task.getprintusage();

        // Then: printUsage should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT for debugging dead code removal");
    }

    /**
     * Tests that getprintusage() has side effect even though it returns null.
     */
    @Test
    public void testGetprintusage_hasSideEffectDespiteReturningNull() throws Exception {
        // Given: Initial state
        assertNull(task.configuration.printUsage,
                   "printUsage should initially be null");

        // When: Calling getprintusage() which returns null
        Object result = task.getprintusage();

        // Then: Should return null but have side effect
        assertNull(result, "Should return null");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "Should have side effect of setting printUsage despite returning null");
    }

    /**
     * Tests that getprintusage() return value is null (not void).
     */
    @Test
    public void testGetprintusage_returnsNullNotVoid() throws Exception {
        // When: Calling getprintusage()
        Object result = task.getprintusage();

        // Then: Should return null (not throw exception or return void)
        assertNull(result, "Should explicitly return null");

        // And: The side effect should occur
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be set");
    }

    /**
     * Tests that getprintusage() initial value check.
     */
    @Test
    public void testGetprintusage_initialValueIsNull() throws Exception {
        // Given: A newly created task
        // When: Not calling getprintusage()
        // Then: The printUsage should be null (default)
        assertNull(task.configuration.printUsage,
                   "printUsage should initially be null");
    }

    /**
     * Tests that getprintusage() can override manual printUsage changes.
     */
    @Test
    public void testGetprintusage_overridesManualChanges() throws Exception {
        // Given: Manual change to printUsage
        java.io.File customFile = tempDir.resolve("manual-usage.txt").toFile();
        task.configuration.printUsage = customFile;
        assertEquals(customFile, task.configuration.printUsage,
                   "printUsage should be set manually");

        // When: Calling getprintusage()
        task.getprintusage();

        // Then: Manual value should be overridden
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be overridden to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintusage() sets Configuration.STD_OUT which is the expected value.
     */
    @Test
    public void testGetprintusage_setsExpectedStdOutValue() throws Exception {
        // When: Calling getprintusage()
        task.getprintusage();

        // Then: printUsage should be exactly Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be exactly Configuration.STD_OUT");
        assertNotNull(task.configuration.printUsage,
                   "printUsage should not be null");
    }

    /**
     * Tests that getprintusage() works with shrinking enabled.
     */
    @Test
    public void testGetprintusage_withShrinkingEnabled() throws Exception {
        // Given: Shrinking enabled (default)
        assertTrue(task.configuration.shrink,
                   "shrink should be enabled by default");

        // When: Calling getprintusage()
        task.getprintusage();

        // Then: printUsage should be set
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT");
        assertTrue(task.configuration.shrink,
                   "shrink should still be enabled");
    }

    /**
     * Tests that getprintusage() works with other print options.
     */
    @Test
    public void testGetprintusage_withOtherPrintOptions() throws Exception {
        // Given: Other print options configured
        task.getprintseeds(); // Also uses Configuration.STD_OUT
        task.getprintmapping(); // Also uses Configuration.STD_OUT

        // When: Calling getprintusage()
        task.getprintusage();

        // Then: All print options should be set
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                   "printMapping should be Configuration.STD_OUT");
    }

    /**
     * Tests that getprintusage() sets the value that will output to stdout.
     */
    @Test
    public void testGetprintusage_setsValueForStdoutOutput() throws Exception {
        // When: Calling getprintusage()
        task.getprintusage();

        // Then: printUsage should be set to the special STD_OUT marker
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT");
        // Configuration.STD_OUT is a special File("") marker for stdout
        assertEquals("", task.configuration.printUsage.toString(),
                   "STD_OUT marker should have empty string representation");
    }

    /**
     * Tests that getprintusage() helps identify unused code.
     */
    @Test
    public void testGetprintusage_helpsIdentifyUnusedCode() throws Exception {
        // Given: Project with potential unused code
        task.injars("app.jar");
        task.outjars("app-processed.jar");
        task.keep("class com.example.MainActivity");

        // When: Calling getprintusage() to identify unused code
        task.getprintusage();

        // Then: printUsage should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT to identify unused code");
    }

    /**
     * Tests that getprintusage() works in code optimization scenarios.
     */
    @Test
    public void testGetprintusage_codeOptimizationScenario() throws Exception {
        // Given: Project that needs to know what's being removed
        task.injars("library.jar");
        task.outjars("library-optimized.jar");

        // When: Calling getprintusage() to track removed code
        task.getprintusage();

        // Then: printUsage should be set
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT for optimization tracking");
    }

    /**
     * Tests that getprintusage() works in CI/CD scenarios.
     */
    @Test
    public void testGetprintusage_cicdScenario() throws Exception {
        // Given: CI/CD build that needs to log usage
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");

        // When: Calling getprintusage() to log to CI output
        task.getprintusage();

        // Then: printUsage should be set to stdout for CI logs
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should output to stdout for CI logs");
    }
}

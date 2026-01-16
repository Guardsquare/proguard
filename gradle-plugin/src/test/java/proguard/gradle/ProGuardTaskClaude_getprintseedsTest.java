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
 * Tests for {@link ProGuardTask#getprintseeds()}.
 *
 * This test class verifies that the getprintseeds() method correctly
 * sets the printSeeds field in the configuration and returns null.
 *
 * Method signature: getprintseeds()Ljava/lang/Object;
 * - Calls printseeds() internally which sets configuration.printSeeds = Configuration.STD_OUT
 * - Returns null (Groovy DSL hack)
 *
 * Key behavior: This method is a Groovy DSL support method that allows calling
 * "printseeds" without parentheses in Groovy build scripts. It enables ProGuard
 * to print the list of classes and class members matched by keep rules to stdout.
 *
 * The getprintseeds() method internally calls printseeds(), which sets
 * configuration.printSeeds to Configuration.STD_OUT, causing seed information
 * to be printed to standard output during processing.
 */
public class ProGuardTaskClaude_getprintseedsTest {

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
     * Tests that getprintseeds() returns null.
     */
    @Test
    public void testGetprintseeds_returnsNull() throws Exception {
        // When: Calling getprintseeds()
        Object result = task.getprintseeds();

        // Then: Should return null (Groovy DSL hack)
        assertNull(result, "getprintseeds() should return null");
    }

    /**
     * Tests that getprintseeds() sets printSeeds to Configuration.STD_OUT.
     */
    @Test
    public void testGetprintseeds_setsPrintSeedsToStdOut() throws Exception {
        // Given: Initial state where printSeeds is null (default)
        assertNull(task.configuration.printSeeds,
                   "printSeeds should initially be null");

        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: printSeeds should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintseeds() can be called multiple times.
     */
    @Test
    public void testGetprintseeds_multipleCalls() throws Exception {
        // When: Calling getprintseeds() multiple times
        task.getprintseeds();
        task.getprintseeds();
        task.getprintseeds();

        // Then: printSeeds should still be Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT after multiple calls");
    }

    /**
     * Tests that getprintseeds() is idempotent.
     */
    @Test
    public void testGetprintseeds_idempotent() throws Exception {
        // Given: printSeeds already set to Configuration.STD_OUT
        task.configuration.printSeeds = Configuration.STD_OUT;

        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: printSeeds should remain Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should remain Configuration.STD_OUT (idempotent)");
    }

    /**
     * Tests that getprintseeds() overrides any previous printSeeds value.
     */
    @Test
    public void testGetprintseeds_overridesPreviousValue() throws Exception {
        // Given: printSeeds set to some other value
        java.io.File customFile = tempDir.resolve("custom-seeds.txt").toFile();
        task.configuration.printSeeds = customFile;
        assertEquals(customFile, task.configuration.printSeeds,
                   "printSeeds should be set to custom file");

        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: printSeeds should be overridden to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be overridden to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintseeds() works independently of injars.
     */
    @Test
    public void testGetprintseeds_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintseeds() works independently of libraryjars.
     */
    @Test
    public void testGetprintseeds_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintseeds() works in a complex workflow.
     */
    @Test
    public void testGetprintseeds_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.getprintseeds();
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that getprintseeds() can be called before other methods.
     */
    @Test
    public void testGetprintseeds_calledBeforeOtherMethods() throws Exception {
        // When: Calling getprintseeds() before other methods
        task.getprintseeds();
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: printSeeds should still be set
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should remain Configuration.STD_OUT");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getprintseeds() can be called after other methods.
     */
    @Test
    public void testGetprintseeds_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling getprintseeds() after other methods
        task.getprintseeds();

        // Then: All should be set correctly
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getprintseeds() can be interleaved with other method calls.
     */
    @Test
    public void testGetprintseeds_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving getprintseeds() with other methods
        task.injars("input.jar");
        task.getprintseeds();
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: All should be set correctly
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getprintseeds() works in realistic Android scenario.
     */
    @Test
    public void testGetprintseeds_realisticAndroidScenario() throws Exception {
        // Given: Android project that needs seed information
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.getprintseeds(); // Print seeds to understand what's being kept
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: printSeeds should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT for debugging");
    }

    /**
     * Tests that getprintseeds() works in realistic Java scenario.
     */
    @Test
    public void testGetprintseeds_realisticJavaScenario() throws Exception {
        // Given: Java project that needs seed information
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.getprintseeds(); // Print seeds to verify keep rules
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: printSeeds should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT for verification");
    }

    /**
     * Tests that getprintseeds() can be called in sequence with configuration.
     */
    @Test
    public void testGetprintseeds_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling getprintseeds()
        task.getprintseeds();

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintseeds() works with multiple library jars.
     */
    @Test
    public void testGetprintseeds_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: printSeeds should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
    }

    /**
     * Tests that getprintseeds() behavior is consistent across task instances.
     */
    @Test
    public void testGetprintseeds_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling getprintseeds() on both
        task1.getprintseeds();
        task2.getprintseeds();

        // Then: Both should have printSeeds set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task1.configuration.printSeeds,
                   "Task1 printSeeds should be Configuration.STD_OUT");
        assertEquals(Configuration.STD_OUT, task2.configuration.printSeeds,
                   "Task2 printSeeds should be Configuration.STD_OUT");
    }

    /**
     * Tests that getprintseeds() and printseeds() produce the same result.
     */
    @Test
    public void testGetprintseeds_equivalentToPrintseeds() throws Exception {
        // Given: Two tasks
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling getprintseeds() on one and printseeds() on other
        task1.getprintseeds();
        task2.printseeds();

        // Then: Both should have the same effect
        assertEquals(task1.configuration.printSeeds,
                   task2.configuration.printSeeds,
                   "Both methods should set the same value");
        assertEquals(Configuration.STD_OUT, task1.configuration.printSeeds,
                   "Task1 printSeeds should be Configuration.STD_OUT");
        assertEquals(Configuration.STD_OUT, task2.configuration.printSeeds,
                   "Task2 printSeeds should be Configuration.STD_OUT");
    }

    /**
     * Tests that getprintseeds() always returns null regardless of state.
     */
    @Test
    public void testGetprintseeds_alwaysReturnsNull() throws Exception {
        // When: Calling getprintseeds() multiple times
        Object result1 = task.getprintseeds();
        Object result2 = task.getprintseeds();
        Object result3 = task.getprintseeds();

        // Then: All calls should return null
        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    /**
     * Tests that getprintseeds() can be called at any point in the configuration.
     */
    @Test
    public void testGetprintseeds_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.getprintseeds();
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.outjars("output.jar");
        task.libraryjars("lib2.jar");

        // Then: printSeeds should be set regardless of call order
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT regardless of when it's called");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
        assertEquals(2, task.getLibraryJarFiles().size(), "Should have 2 library jars");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that getprintseeds() sets only the specific field.
     */
    @Test
    public void testGetprintseeds_setsOnlyPrintSeeds() throws Exception {
        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: Only the printSeeds field should be affected
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");

        // Note: We're only testing the specific field this method sets
        // Other configuration fields should not be affected by this method
    }

    /**
     * Tests that getprintseeds() enables seed output to stdout.
     */
    @Test
    public void testGetprintseeds_enablesSeedOutputToStdout() throws Exception {
        // Given: Initial state
        assertNull(task.configuration.printSeeds,
                   "printSeeds should initially be null");

        // When: Calling getprintseeds() to enable seed output
        task.getprintseeds();

        // Then: printSeeds should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT to enable stdout output");
    }

    /**
     * Tests that getprintseeds() value persists across configuration calls.
     */
    @Test
    public void testGetprintseeds_persistsAcrossConfigurationCalls() throws Exception {
        // Given: printseeds set early
        task.getprintseeds();
        Object printSeedsAfterSet = task.configuration.printSeeds;

        // When: Other configuration methods are called
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");

        // Then: printSeeds should remain unchanged
        assertEquals(printSeedsAfterSet, task.configuration.printSeeds,
                   "printSeeds should persist after other configuration calls");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should still be Configuration.STD_OUT");
    }

    /**
     * Tests that getprintseeds() works correctly in a minimal configuration.
     */
    @Test
    public void testGetprintseeds_minimalConfiguration() throws Exception {
        // When: Only getprintseeds() is called (minimal configuration)
        Object result = task.getprintseeds();

        // Then: Should return null and set printSeeds
        assertNull(result, "Should return null");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT in minimal configuration");
    }

    /**
     * Tests that getprintseeds() is useful for debugging keep rules.
     */
    @Test
    public void testGetprintseeds_usefulForDebuggingKeepRules() throws Exception {
        // Given: Project with keep rules that need debugging
        task.injars("input.jar");
        task.outjars("output.jar");
        task.keep("class com.example.MyClass");

        // When: Calling getprintseeds() to see which classes are kept
        task.getprintseeds();

        // Then: printSeeds should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT for debugging keep rules");
    }

    /**
     * Tests that getprintseeds() has side effect even though it returns null.
     */
    @Test
    public void testGetprintseeds_hasSideEffectDespiteReturningNull() throws Exception {
        // Given: Initial state
        assertNull(task.configuration.printSeeds,
                   "printSeeds should initially be null");

        // When: Calling getprintseeds() which returns null
        Object result = task.getprintseeds();

        // Then: Should return null but have side effect
        assertNull(result, "Should return null");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "Should have side effect of setting printSeeds despite returning null");
    }

    /**
     * Tests that getprintseeds() return value is null (not void).
     */
    @Test
    public void testGetprintseeds_returnsNullNotVoid() throws Exception {
        // When: Calling getprintseeds()
        Object result = task.getprintseeds();

        // Then: Should return null (not throw exception or return void)
        assertNull(result, "Should explicitly return null");

        // And: The side effect should occur
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set");
    }

    /**
     * Tests that getprintseeds() initial value check.
     */
    @Test
    public void testGetprintseeds_initialValueIsNull() throws Exception {
        // Given: A newly created task
        // When: Not calling getprintseeds()
        // Then: The printSeeds should be null (default)
        assertNull(task.configuration.printSeeds,
                   "printSeeds should initially be null");
    }

    /**
     * Tests that getprintseeds() can override manual printSeeds changes.
     */
    @Test
    public void testGetprintseeds_overridesManualChanges() throws Exception {
        // Given: Manual change to printSeeds
        java.io.File customFile = tempDir.resolve("manual-seeds.txt").toFile();
        task.configuration.printSeeds = customFile;
        assertEquals(customFile, task.configuration.printSeeds,
                   "printSeeds should be set manually");

        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: Manual value should be overridden
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be overridden to Configuration.STD_OUT");
    }

    /**
     * Tests that getprintseeds() sets Configuration.STD_OUT which is the expected value.
     */
    @Test
    public void testGetprintseeds_setsExpectedStdOutValue() throws Exception {
        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: printSeeds should be exactly Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be exactly Configuration.STD_OUT");
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should not be null");
    }

    /**
     * Tests that getprintseeds() works with keep rules.
     */
    @Test
    public void testGetprintseeds_worksWithKeepRules() throws Exception {
        // Given: Keep rules configured
        task.keep("class com.example.MainActivity");
        task.keepclassmembers("class * { public <init>(...); }");

        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: printSeeds should be set
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
        assertNotNull(task.configuration.keep, "Keep rules should be configured");
    }

    /**
     * Tests that getprintseeds() works with other print options.
     */
    @Test
    public void testGetprintseeds_worksWithOtherPrintOptions() throws Exception {
        // Given: Other print options configured
        task.getprintusage(); // Also uses Configuration.STD_OUT
        task.getprintmapping(); // Also uses Configuration.STD_OUT

        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: All print options should be set
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                   "printMapping should be Configuration.STD_OUT");
    }

    /**
     * Tests that getprintseeds() sets the value that will output to stdout.
     */
    @Test
    public void testGetprintseeds_setsValueForStdoutOutput() throws Exception {
        // When: Calling getprintseeds()
        task.getprintseeds();

        // Then: printSeeds should be set to the special STD_OUT marker
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
        // Configuration.STD_OUT is a special File("") marker for stdout
        assertEquals("", task.configuration.printSeeds.toString(),
                   "STD_OUT marker should have empty string representation");
    }
}

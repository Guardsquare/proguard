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
 * Tests for {@link ProGuardTask#printseeds()}.
 *
 * This test class verifies that the printseeds() method correctly
 * sets the printSeeds field in the configuration to enable seed output.
 *
 * Method signature: printseeds()V
 * - Sets configuration.printSeeds = Configuration.STD_OUT
 * - Returns void
 *
 * Key behavior: This method enables ProGuard to print the list of classes and
 * class members matched by keep rules to standard output. Unlike getprintseeds(),
 * this method returns void rather than null, making it suitable for direct method
 * calls rather than Groovy DSL usage.
 *
 * The printseeds() method sets configuration.printSeeds to Configuration.STD_OUT,
 * causing seed information to be printed to stdout during processing.
 */
public class ProGuardTaskClaude_printseedsTest {

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
     * Tests that printseeds() sets printSeeds to Configuration.STD_OUT.
     */
    @Test
    public void testPrintseeds_setsPrintSeedsToStdOut() throws Exception {
        // Given: Initial state where printSeeds is null (default)
        assertNull(task.configuration.printSeeds,
                   "printSeeds should initially be null");

        // When: Calling printseeds()
        task.printseeds();

        // Then: printSeeds should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() can be called multiple times.
     */
    @Test
    public void testPrintseeds_multipleCalls() throws Exception {
        // When: Calling printseeds() multiple times
        task.printseeds();
        task.printseeds();
        task.printseeds();

        // Then: printSeeds should still be Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT after multiple calls");
    }

    /**
     * Tests that printseeds() is idempotent.
     */
    @Test
    public void testPrintseeds_idempotent() throws Exception {
        // Given: printSeeds already set to Configuration.STD_OUT
        task.configuration.printSeeds = Configuration.STD_OUT;

        // When: Calling printseeds()
        task.printseeds();

        // Then: printSeeds should remain Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should remain Configuration.STD_OUT (idempotent)");
    }

    /**
     * Tests that printseeds() overrides any previous printSeeds value.
     */
    @Test
    public void testPrintseeds_overridesPreviousValue() throws Exception {
        // Given: printSeeds set to some other file
        java.io.File customFile = tempDir.resolve("custom-seeds.txt").toFile();
        task.configuration.printSeeds = customFile;
        assertEquals(customFile, task.configuration.printSeeds,
                   "printSeeds should be set to custom file");

        // When: Calling printseeds()
        task.printseeds();

        // Then: printSeeds should be overridden to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be overridden to Configuration.STD_OUT");
    }

    /**
     * Tests that the initial state has printSeeds as null.
     */
    @Test
    public void testPrintseeds_initiallyNull() throws Exception {
        // Given: A newly created task
        // When: Not calling printseeds()
        // Then: The printSeeds should be null (default)
        assertNull(task.configuration.printSeeds,
                   "printSeeds should initially be null");
    }

    /**
     * Tests that printseeds() works independently of injars.
     */
    @Test
    public void testPrintseeds_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling printseeds()
        task.printseeds();

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() works independently of libraryjars.
     */
    @Test
    public void testPrintseeds_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling printseeds()
        task.printseeds();

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() works in a complex workflow.
     */
    @Test
    public void testPrintseeds_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.printseeds();
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
     * Tests that printseeds() can be called before other methods.
     */
    @Test
    public void testPrintseeds_calledBeforeOtherMethods() throws Exception {
        // When: Calling printseeds() before other methods
        task.printseeds();
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
     * Tests that printseeds() can be called after other methods.
     */
    @Test
    public void testPrintseeds_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling printseeds() after other methods
        task.printseeds();

        // Then: All should be set correctly
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that printseeds() can be interleaved with other method calls.
     */
    @Test
    public void testPrintseeds_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving printseeds() with other methods
        task.injars("input.jar");
        task.printseeds();
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
     * Tests that printseeds() works in realistic Android scenario.
     */
    @Test
    public void testPrintseeds_realisticAndroidScenario() throws Exception {
        // Given: Android project that needs seed information for debugging
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.printseeds(); // Print seeds to see what's being kept
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: printSeeds should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT for debugging");
    }

    /**
     * Tests that printseeds() works in realistic Java scenario.
     */
    @Test
    public void testPrintseeds_realisticJavaScenario() throws Exception {
        // Given: Java project that needs seed information for verification
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.printseeds(); // Print seeds to verify keep rules
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: printSeeds should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT for verification");
    }

    /**
     * Tests that printseeds() can be called in sequence with configuration.
     */
    @Test
    public void testPrintseeds_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling printseeds()
        task.printseeds();

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() works with multiple library jars.
     */
    @Test
    public void testPrintseeds_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling printseeds()
        task.printseeds();

        // Then: printSeeds should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() is thread-safe for idempotent operations.
     */
    @Test
    public void testPrintseeds_idempotentThreadSafe() throws Exception {
        // Given: Initial state
        assertNull(task.configuration.printSeeds,
                   "printSeeds should initially be null");

        // When: Calling printseeds() sequentially (simulating thread safety)
        task.printseeds();
        task.printseeds();

        // Then: The printSeeds should be Configuration.STD_OUT and stable
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be stable after multiple calls");
    }

    /**
     * Tests that printseeds() behavior is consistent across task instances.
     */
    @Test
    public void testPrintseeds_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling printseeds() on both
        task1.printseeds();
        task2.printseeds();

        // Then: Both should have printSeeds set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task1.configuration.printSeeds,
                   "Task1 printSeeds should be Configuration.STD_OUT");
        assertEquals(Configuration.STD_OUT, task2.configuration.printSeeds,
                   "Task2 printSeeds should be Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() returns void.
     */
    @Test
    public void testPrintseeds_returnsVoid() throws Exception {
        // Given: Initial state
        assertNull(task.configuration.printSeeds,
                   "printSeeds should initially be null");

        // When: Calling printseeds()
        task.printseeds(); // Method returns void

        // Then: The side effect should occur
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() can be called at any point in the configuration.
     */
    @Test
    public void testPrintseeds_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.printseeds();
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
     * Tests that printseeds() sets only the specific field.
     */
    @Test
    public void testPrintseeds_setsOnlyPrintSeeds() throws Exception {
        // When: Calling printseeds()
        task.printseeds();

        // Then: Only the printSeeds field should be affected
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT");

        // Note: We're only testing the specific field this method sets
        // Other configuration fields should not be affected by this method
    }

    /**
     * Tests that printseeds() enables seed output to stdout.
     */
    @Test
    public void testPrintseeds_enablesSeedOutputToStdout() throws Exception {
        // Given: Initial state
        assertNull(task.configuration.printSeeds,
                   "printSeeds should initially be null");

        // When: Calling printseeds() to enable seed output
        task.printseeds();

        // Then: printSeeds should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT to enable stdout output");
    }

    /**
     * Tests that printseeds() value persists across configuration calls.
     */
    @Test
    public void testPrintseeds_persistsAcrossConfigurationCalls() throws Exception {
        // Given: printseeds set early
        task.printseeds();
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
     * Tests that printseeds() works correctly in a minimal configuration.
     */
    @Test
    public void testPrintseeds_minimalConfiguration() throws Exception {
        // When: Only printseeds() is called (minimal configuration)
        task.printseeds();

        // Then: printSeeds should be set correctly
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to Configuration.STD_OUT in minimal configuration");
    }

    /**
     * Tests that printseeds() is useful for debugging keep rules.
     */
    @Test
    public void testPrintseeds_usefulForDebuggingKeepRules() throws Exception {
        // Given: Project with keep rules that need debugging
        task.injars("input.jar");
        task.outjars("output.jar");
        task.keep("class com.example.MyClass");

        // When: Calling printseeds() to see which classes are kept
        task.printseeds();

        // Then: printSeeds should be set to Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT for debugging keep rules");
    }

    /**
     * Tests that printseeds() can override manual printSeeds changes.
     */
    @Test
    public void testPrintseeds_overridesManualChanges() throws Exception {
        // Given: Manual change to printSeeds
        java.io.File customFile = tempDir.resolve("manual-seeds.txt").toFile();
        task.configuration.printSeeds = customFile;
        assertEquals(customFile, task.configuration.printSeeds,
                   "printSeeds should be set manually");

        // When: Calling printseeds()
        task.printseeds();

        // Then: Manual value should be overridden
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be overridden to Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() sets Configuration.STD_OUT which is the expected value.
     */
    @Test
    public void testPrintseeds_setsExpectedStdOutValue() throws Exception {
        // When: Calling printseeds()
        task.printseeds();

        // Then: printSeeds should be exactly Configuration.STD_OUT
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be exactly Configuration.STD_OUT");
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should not be null");
        assertEquals("", task.configuration.printSeeds.toString(),
                   "STD_OUT marker should have empty string representation");
    }

    /**
     * Tests that printseeds() works with filters.
     */
    @Test
    public void testPrintseeds_withFilters() throws Exception {
        // Given: Library jars with filters
        java.util.Map<String, String> filter = new java.util.HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "android.jar");

        // When: Calling printseeds()
        task.printseeds();

        // Then: Both filter and printSeeds should be set
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getLibraryJarFilters().size(), "Should have 1 filter");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() works with keep rules.
     */
    @Test
    public void testPrintseeds_withKeepRules() throws Exception {
        // Given: Keep rules configured
        task.keep("class com.example.MainActivity");
        task.keepclassmembers("class * { public <init>(...); }");

        // When: Calling printseeds()
        task.printseeds();

        // Then: printSeeds should be set
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
        assertNotNull(task.configuration.keep, "Keep rules should be configured");
    }

    /**
     * Tests that printseeds() works with other print options.
     */
    @Test
    public void testPrintseeds_withOtherPrintOptions() throws Exception {
        // Given: Other print options configured
        task.printusage(); // Also uses Configuration.STD_OUT
        task.printmapping(); // Also uses Configuration.STD_OUT

        // When: Calling printseeds()
        task.printseeds();

        // Then: All print options should be set
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                   "printUsage should be Configuration.STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                   "printMapping should be Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() and getprintseeds() produce the same result.
     */
    @Test
    public void testPrintseeds_equivalentToGetprintseeds() throws Exception {
        // Given: Two tasks
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling printseeds() on one and getprintseeds() on other
        task1.printseeds();
        task2.getprintseeds();

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
     * Tests that printseeds() is used for debugging scenarios.
     */
    @Test
    public void testPrintseeds_debuggingScenario() throws Exception {
        // Given: Project configured for debugging
        task.injars("app.jar");
        task.outjars("app-processed.jar");
        task.keep("class com.example.**");

        // When: Enabling seed output for debugging
        task.printseeds();

        // Then: printSeeds should enable output
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should enable output for debugging");
    }

    /**
     * Tests that printseeds() affects different field than target().
     */
    @Test
    public void testPrintseeds_differentFieldFromTarget() throws Exception {
        // Given: target() has been called (different field)
        task.target("1.8");
        int targetVersion = task.configuration.targetClassVersion;
        assertNotEquals(0, targetVersion, "targetClassVersion should be set");

        // When: Calling printseeds() (affects different field)
        task.printseeds();

        // Then: This method affects printSeeds, not targetClassVersion
        assertEquals(targetVersion, task.configuration.targetClassVersion,
                   "targetClassVersion should remain unchanged");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() works with extraJar.
     */
    @Test
    public void testPrintseeds_withExtraJar() throws Exception {
        // Given: ExtraJar configured
        task.extraJar(new java.io.File(tempDir.toFile(), "extra.jar"));

        // When: Calling printseeds()
        task.printseeds();

        // Then: Both should be set
        assertNotNull(task.configuration.extraJar, "ExtraJar should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
    }

    /**
     * Tests that printseeds() sets the value that will output to stdout.
     */
    @Test
    public void testPrintseeds_setsValueForStdoutOutput() throws Exception {
        // When: Calling printseeds()
        task.printseeds();

        // Then: printSeeds should be set to the special STD_OUT marker
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT");
        // Configuration.STD_OUT is a special File("") marker for stdout
        assertEquals("", task.configuration.printSeeds.toString(),
                   "STD_OUT marker should have empty string representation");
    }

    /**
     * Tests that printseeds() enables visibility into keep rule matching.
     */
    @Test
    public void testPrintseeds_enablesKeepRuleVisibility() throws Exception {
        // Given: Multiple keep rules configured
        task.keep("class com.example.MainActivity");
        task.keepnames("class com.example.Model");
        task.keepclassmembers("class * extends android.app.Activity");

        // When: Enabling seed output to see what's being kept
        task.printseeds();

        // Then: printSeeds should be configured for output
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be Configuration.STD_OUT to see keep rule matches");
        assertNotNull(task.configuration.keep, "Keep rules should be configured");
    }

    /**
     * Tests that printseeds() works in continuous integration scenarios.
     */
    @Test
    public void testPrintseeds_continuousIntegrationScenario() throws Exception {
        // Given: CI build that needs to verify keep rules
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.outjars("build/libs/app-shrunk.jar");
        task.keep("class com.example.**");

        // When: Enabling seed output for CI logs
        task.printseeds();

        // Then: printSeeds should be set for CI logging
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should output to stdout for CI logs");
    }

    // ========================================
    // Tests for printseeds(Object) method
    // ========================================

    /**
     * Tests that printseeds(Object) sets printSeeds to the specified file.
     */
    @Test
    public void testPrintseedsWithFile_setsPrintSeedsToFile() throws Exception {
        // Given: A file path for seed output
        String seedFile = "seeds.txt";

        // When: Calling printseeds(Object)
        task.printseeds(seedFile);

        // Then: printSeeds should be set to the file
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should not be null");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) accepts various file path types.
     */
    @Test
    public void testPrintseedsWithFile_acceptsVariousPathTypes() throws Exception {
        // When: Calling printseeds with relative path
        task.printseeds("output/seeds.txt");

        // Then: printSeeds should be set
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set from relative path");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) with File object works correctly.
     */
    @Test
    public void testPrintseedsWithFile_acceptsFileObject() throws Exception {
        // Given: A File object
        java.io.File seedFile = new java.io.File(tempDir.toFile(), "test-seeds.txt");

        // When: Calling printseeds with File object
        task.printseeds(seedFile);

        // Then: printSeeds should be set to the file
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set");
        assertEquals("test-seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) can override stdout configuration.
     */
    @Test
    public void testPrintseedsWithFile_overridesStdout() throws Exception {
        // Given: printseeds() was called (sets to stdout)
        task.printseeds();
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should initially be stdout");

        // When: Calling printseeds(Object) with a file
        task.printseeds("custom-seeds.txt");

        // Then: printSeeds should be overridden to the file
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should not be null");
        assertNotEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should no longer be stdout");
        assertEquals("custom-seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have custom filename");
    }

    /**
     * Tests that printseeds(Object) can be overridden by stdout.
     */
    @Test
    public void testPrintseedsWithFile_canBeOverriddenByStdout() throws Exception {
        // Given: printseeds(Object) was called with a file
        task.printseeds("initial-seeds.txt");
        assertNotEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should initially be a file");

        // When: Calling printseeds() without parameters
        task.printseeds();

        // Then: printSeeds should be overridden to stdout
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be set to stdout");
    }

    /**
     * Tests that printseeds(Object) can be called multiple times with different files.
     */
    @Test
    public void testPrintseedsWithFile_multipleDifferentFiles() throws Exception {
        // When: Calling printseeds(Object) multiple times
        task.printseeds("seeds1.txt");
        assertEquals("seeds1.txt", task.configuration.printSeeds.getName(),
                   "First file should be set");

        task.printseeds("seeds2.txt");
        assertEquals("seeds2.txt", task.configuration.printSeeds.getName(),
                   "Second file should override first");

        task.printseeds("output/seeds3.txt");
        assertEquals("seeds3.txt", task.configuration.printSeeds.getName(),
                   "Third file should override second");
    }

    /**
     * Tests that printseeds(Object) works with absolute paths.
     */
    @Test
    public void testPrintseedsWithFile_absolutePath() throws Exception {
        // Given: An absolute path
        java.io.File absoluteFile = new java.io.File(tempDir.toFile(), "absolute-seeds.txt");
        String absolutePath = absoluteFile.getAbsolutePath();

        // When: Calling printseeds with absolute path
        task.printseeds(absolutePath);

        // Then: printSeeds should be set
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set");
        assertEquals("absolute-seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) works independently of injars.
     */
    @Test
    public void testPrintseedsWithFile_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling printseeds(Object)
        task.printseeds("seeds.txt");

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertNotNull(task.configuration.printSeeds, "printSeeds should be set");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) works in complex workflow.
     */
    @Test
    public void testPrintseedsWithFile_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.printseeds("project-seeds.txt");
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertNotNull(task.configuration.printSeeds, "printSeeds should be set");
        assertEquals("project-seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that printseeds(Object) works in realistic Android scenario.
     */
    @Test
    public void testPrintseedsWithFile_realisticAndroidScenario() throws Exception {
        // Given: Android project that needs seed file for analysis
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.printseeds("build/outputs/seeds.txt");
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: printSeeds should be set to the file
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename for analysis");
    }

    /**
     * Tests that printseeds(Object) works in realistic Java scenario.
     */
    @Test
    public void testPrintseedsWithFile_realisticJavaScenario() throws Exception {
        // Given: Java project that needs seed file for documentation
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.printseeds("docs/proguard-seeds.txt");
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: printSeeds should be set to the file
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set");
        assertEquals("proguard-seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename for documentation");
    }

    /**
     * Tests that printseeds(Object) works with keep rules.
     */
    @Test
    public void testPrintseedsWithFile_withKeepRules() throws Exception {
        // Given: Keep rules configured
        task.keep("class com.example.MainActivity");
        task.keepclassmembers("class * { public <init>(...); }");

        // When: Calling printseeds(Object)
        task.printseeds("keep-seeds.txt");

        // Then: printSeeds should be set
        assertNotNull(task.configuration.printSeeds, "printSeeds should be set");
        assertEquals("keep-seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
        assertNotNull(task.configuration.keep, "Keep rules should be configured");
    }

    /**
     * Tests that printseeds(Object) works with other print options.
     */
    @Test
    public void testPrintseedsWithFile_withOtherPrintOptions() throws Exception {
        // Given: Other print options configured
        task.printusage("usage.txt");
        task.printmapping("mapping.txt");

        // When: Calling printseeds(Object)
        task.printseeds("seeds.txt");

        // Then: All print options should be set independently
        assertNotNull(task.configuration.printSeeds, "printSeeds should be set");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds filename should be correct");
        assertNotNull(task.configuration.printUsage, "printUsage should be set");
        assertNotNull(task.configuration.printMapping, "printMapping should be set");
    }

    /**
     * Tests that printseeds(Object) behavior is consistent across task instances.
     */
    @Test
    public void testPrintseedsWithFile_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling printseeds(Object) on both
        task1.printseeds("seeds1.txt");
        task2.printseeds("seeds2.txt");

        // Then: Both should have their respective files set
        assertNotNull(task1.configuration.printSeeds,
                   "Task1 printSeeds should be set");
        assertEquals("seeds1.txt", task1.configuration.printSeeds.getName(),
                   "Task1 should have correct filename");
        assertNotNull(task2.configuration.printSeeds,
                   "Task2 printSeeds should be set");
        assertEquals("seeds2.txt", task2.configuration.printSeeds.getName(),
                   "Task2 should have correct filename");
    }

    /**
     * Tests that printseeds(Object) can be called before other methods.
     */
    @Test
    public void testPrintseedsWithFile_calledBeforeOtherMethods() throws Exception {
        // When: Calling printseeds(Object) before other methods
        task.printseeds("early-seeds.txt");
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: printSeeds should still be set
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should remain set");
        assertEquals("early-seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
    }

    /**
     * Tests that printseeds(Object) can be called after other methods.
     */
    @Test
    public void testPrintseedsWithFile_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling printseeds(Object) after other methods
        task.printseeds("late-seeds.txt");

        // Then: All should be set correctly
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set");
        assertEquals("late-seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
    }

    /**
     * Tests that printseeds(Object) works with nested directories.
     */
    @Test
    public void testPrintseedsWithFile_nestedDirectories() throws Exception {
        // When: Calling printseeds with nested path
        task.printseeds("build/reports/proguard/seeds.txt");

        // Then: printSeeds should be set with correct filename
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) value persists across configuration calls.
     */
    @Test
    public void testPrintseedsWithFile_persistsAcrossConfigurationCalls() throws Exception {
        // Given: printseeds(Object) set early
        task.printseeds("persistent-seeds.txt");
        java.io.File printSeedsAfterSet = task.configuration.printSeeds;

        // When: Other configuration methods are called
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");

        // Then: printSeeds should remain unchanged
        assertEquals(printSeedsAfterSet, task.configuration.printSeeds,
                   "printSeeds should persist after other configuration calls");
        assertEquals("persistent-seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds filename should still be correct");
    }

    /**
     * Tests that printseeds(Object) works in minimal configuration.
     */
    @Test
    public void testPrintseedsWithFile_minimalConfiguration() throws Exception {
        // When: Only printseeds(Object) is called (minimal configuration)
        task.printseeds("minimal-seeds.txt");

        // Then: printSeeds should be set correctly
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set in minimal configuration");
        assertEquals("minimal-seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) is useful for archiving seed information.
     */
    @Test
    public void testPrintseedsWithFile_archivingScenario() throws Exception {
        // Given: Project that needs to archive seed information
        task.injars("input.jar");
        task.outjars("output.jar");
        task.keep("class com.example.**");

        // When: Calling printseeds(Object) to save for archiving
        task.printseeds("archive/seeds-v1.0.txt");

        // Then: printSeeds should be set to archive location
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set for archiving");
        assertEquals("seeds-v1.0.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have versioned filename for archiving");
    }

    /**
     * Tests that printseeds(Object) works with build directory structure.
     */
    @Test
    public void testPrintseedsWithFile_buildDirectoryStructure() throws Exception {
        // Given: Standard build directory structure
        task.injars("build/classes/java/main");
        task.outjars("build/libs/app.jar");

        // When: Calling printseeds(Object) in build directory
        task.printseeds("build/outputs/proguard/seeds.txt");

        // Then: printSeeds should be set in build directory
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set in build directory");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) supports different file extensions.
     */
    @Test
    public void testPrintseedsWithFile_differentFileExtensions() throws Exception {
        // When: Calling with .log extension
        task.printseeds("seeds.log");
        assertEquals("seeds.log", task.configuration.printSeeds.getName(),
                   "Should support .log extension");

        // When: Calling with .out extension
        task.printseeds("seeds.out");
        assertEquals("seeds.out", task.configuration.printSeeds.getName(),
                   "Should support .out extension");

        // When: Calling with no extension
        task.printseeds("seeds");
        assertEquals("seeds", task.configuration.printSeeds.getName(),
                   "Should support files without extension");
    }

    /**
     * Tests that printseeds(Object) works with filters.
     */
    @Test
    public void testPrintseedsWithFile_withFilters() throws Exception {
        // Given: Library jars with filters
        java.util.Map<String, String> filter = new java.util.HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "android.jar");

        // When: Calling printseeds(Object)
        task.printseeds("filtered-seeds.txt");

        // Then: Both filter and printSeeds should be set
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getLibraryJarFilters().size(), "Should have 1 filter");
        assertNotNull(task.configuration.printSeeds, "printSeeds should be set");
        assertEquals("filtered-seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) affects different field than target().
     */
    @Test
    public void testPrintseedsWithFile_differentFieldFromTarget() throws Exception {
        // Given: target() has been called (different field)
        task.target("1.8");
        int targetVersion = task.configuration.targetClassVersion;
        assertNotEquals(0, targetVersion, "targetClassVersion should be set");

        // When: Calling printseeds(Object) (affects different field)
        task.printseeds("seeds.txt");

        // Then: This method affects printSeeds, not targetClassVersion
        assertEquals(targetVersion, task.configuration.targetClassVersion,
                   "targetClassVersion should remain unchanged");
        assertNotNull(task.configuration.printSeeds, "printSeeds should be set");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) works with extraJar.
     */
    @Test
    public void testPrintseedsWithFile_withExtraJar() throws Exception {
        // Given: ExtraJar configured
        task.extraJar(new java.io.File(tempDir.toFile(), "extra.jar"));

        // When: Calling printseeds(Object)
        task.printseeds("seeds-with-extra.txt");

        // Then: Both should be set
        assertNotNull(task.configuration.extraJar, "ExtraJar should be set");
        assertNotNull(task.configuration.printSeeds, "printSeeds should be set");
        assertEquals("seeds-with-extra.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) works in CI/CD pipeline with timestamped files.
     */
    @Test
    public void testPrintseedsWithFile_cicdTimestampedFiles() throws Exception {
        // Given: CI/CD build with timestamped output
        String timestamp = "20240115-120000";
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");

        // When: Calling printseeds(Object) with timestamped filename
        task.printseeds("build/reports/seeds-" + timestamp + ".txt");

        // Then: printSeeds should be set with timestamped filename
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set");
        assertTrue(task.configuration.printSeeds.getName().contains(timestamp),
                  "printSeeds filename should contain timestamp");
    }

    /**
     * Tests that printseeds(Object) supports relative paths from project root.
     */
    @Test
    public void testPrintseedsWithFile_relativePathFromProjectRoot() throws Exception {
        // When: Calling printseeds with relative path from project root
        task.printseeds("proguard/seeds.txt");

        // Then: printSeeds should be set correctly
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set from relative path");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have correct filename");
    }

    /**
     * Tests that printseeds(Object) enables offline analysis of keep rules.
     */
    @Test
    public void testPrintseedsWithFile_offlineAnalysis() throws Exception {
        // Given: Configuration for offline analysis
        task.injars("app.jar");
        task.outjars("app-processed.jar");
        task.keep("class com.example.MainActivity");
        task.keep("class com.example.DataModel");

        // When: Saving seeds for offline analysis
        task.printseeds("analysis/seeds-for-review.txt");

        // Then: printSeeds should be configured for offline analysis
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set for offline analysis");
        assertEquals("seeds-for-review.txt", task.configuration.printSeeds.getName(),
                   "printSeeds should have descriptive filename for analysis");
    }
}

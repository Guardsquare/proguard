package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.Configuration;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.dump()V method.
 * Tests the void dump() method that sets configuration.dump to Configuration.STD_OUT,
 * which enables dumping the internal structure of class files to standard output during ProGuard processing.
 */
public class ProGuardTaskClaude_dumpTest {

    @TempDir
    Path tempDir;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("proguard", ProGuardTask.class);
    }

    @AfterEach
    public void tearDown() {
        task = null;
        project = null;
    }

    // ============================================================
    // Basic Functionality Tests
    // ============================================================

    @Test
    public void testDump_setsDumpToStdOut() throws Exception {
        assertNull(task.configuration.dump, "dump should initially be null");
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be set to Configuration.STD_OUT");
    }

    @Test
    public void testDump_doesNotReturnValue() throws Exception {
        task.dump();
        // This test verifies the method is void and completes without exception
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be set to Configuration.STD_OUT");
    }

    @Test
    public void testDump_setsStdOutMarker() throws Exception {
        task.dump();
        assertNotNull(task.configuration.dump, "dump should not be null after calling dump()");
        assertEquals("", task.configuration.dump.getPath(),
                "Configuration.STD_OUT should have empty path");
    }

    // ============================================================
    // Multiple Calls and Idempotency Tests
    // ============================================================

    @Test
    public void testDump_multipleCallsAreIdempotent() throws Exception {
        task.dump();
        File firstCall = task.configuration.dump;
        task.dump();
        File secondCall = task.configuration.dump;
        assertSame(Configuration.STD_OUT, firstCall, "First call should set STD_OUT");
        assertSame(Configuration.STD_OUT, secondCall, "Second call should set STD_OUT");
        assertSame(firstCall, secondCall, "Multiple calls should produce same result");
    }

    @Test
    public void testDump_canBeCalledMultipleTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.dump();
            assertEquals(Configuration.STD_OUT, task.configuration.dump,
                    "dump should be STD_OUT after call " + (i + 1));
        }
    }

    @Test
    public void testDump_overwritesNullValue() throws Exception {
        assertNull(task.configuration.dump, "dump should start as null");
        task.dump();
        assertNotNull(task.configuration.dump, "dump should not be null after dump()");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be Configuration.STD_OUT");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testDump_worksWithOtherPrintOptions() throws Exception {
        task.printseeds();
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should remain as STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testDump_worksWithDontShrink() throws Exception {
        task.dontshrink();
        task.dump();
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testDump_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.dump();
        assertFalse(task.configuration.optimize, "optimize should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testDump_worksWithDontObfuscate() throws Exception {
        task.dontobfuscate();
        task.dump();
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testDump_worksWithMultiplePrintOutputs() throws Exception {
        task.printseeds();
        task.dump();
        task.printmapping();
        task.printconfiguration();
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testDump_doesNotAffectVerboseSetting() throws Exception {
        task.verbose();
        task.dump();
        assertTrue(task.configuration.verbose, "verbose should remain true");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testDump_doesNotAffectIgnoreWarningsSetting() throws Exception {
        task.ignorewarnings();
        task.dump();
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should remain true");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testDump_doesNotAffectShrinkSetting() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        task.dump();
        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
    }

    @Test
    public void testDump_doesNotAffectOptimizeSetting() throws Exception {
        boolean initialOptimize = task.configuration.optimize;
        task.dump();
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
    }

    @Test
    public void testDump_doesNotAffectObfuscateSetting() throws Exception {
        boolean initialObfuscate = task.configuration.obfuscate;
        task.dump();
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testDump_canBeCalledBeforeOtherMethods() throws Exception {
        task.dump();
        task.dontshrink();
        task.printseeds();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
    }

    @Test
    public void testDump_canBeCalledAfterOtherMethods() throws Exception {
        task.dontshrink();
        task.printseeds();
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
    }

    @Test
    public void testDump_canBeCalledBetweenOtherMethods() throws Exception {
        task.dontshrink();
        task.dump();
        task.printseeds();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
    }

    // ============================================================
    // Realistic Usage Scenarios
    // ============================================================

    @Test
    public void testDump_androidAppScenario() throws Exception {
        // Typical Android app configuration with dump for analysis
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.dump();
        task.keep("class com.example.MainActivity { *; }");

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT for Android scenario");
    }

    @Test
    public void testDump_javaLibraryScenario() throws Exception {
        // Java library scenario with dump output
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-processed.jar");
        task.dump();
        task.keep("public class * { public *; }");

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT for Java library scenario");
    }

    @Test
    public void testDump_withOnlyReportingOptions() throws Exception {
        // Configuration focused only on reporting without processing
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.dump();
        task.printseeds();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
    }

    @Test
    public void testDump_cicdScenario() throws Exception {
        // CI/CD scenario where all output goes to stdout for log capture
        task.dump();
        task.printseeds();
        task.printmapping();
        task.printconfiguration();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should output to stdout for CI/CD");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should output to stdout for CI/CD");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should output to stdout for CI/CD");
    }

    @Test
    public void testDump_debuggingScenario() throws Exception {
        // Debugging scenario with verbose output and dump
        task.dump();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT for debugging");
        assertTrue(task.configuration.verbose, "verbose should be enabled");
    }

    // ============================================================
    // Edge Cases and Validation
    // ============================================================

    @Test
    public void testDump_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.dump();
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testDump_doesNotAffectOtherConfigurationFields() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.dump();

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testDump_stdOutIsNotNull() throws Exception {
        task.dump();
        assertNotNull(task.configuration.dump,
                "Configuration.STD_OUT should not be null");
    }

    @Test
    public void testDump_stdOutIsSpecialFile() throws Exception {
        task.dump();
        assertEquals("", task.configuration.dump.getPath(),
                "Configuration.STD_OUT should have empty path as marker");
    }

    @Test
    public void testDump_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        freshTask.dump();
        assertEquals(Configuration.STD_OUT, freshTask.configuration.dump,
                "dump should work on fresh task");
    }

    // ============================================================
    // Integration with Keep Rules
    // ============================================================

    @Test
    public void testDump_worksWithKeepRules() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should work with keep rules");
    }

    @Test
    public void testDump_worksWithKeepClassMembers() throws Exception {
        task.keepclassmembers("class * { public *; }");
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should work with keepclassmembers");
    }

    @Test
    public void testDump_worksWithDontWarn() throws Exception {
        task.dontwarn("com.example.**");
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should work with dontwarn");
    }

    // ============================================================
    // Interaction with File-Based Print Methods
    // ============================================================

    @Test
    public void testDump_afterPrintseedsWithFile() throws Exception {
        task.printseeds("seeds.txt");
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should remain as file");
    }

    @Test
    public void testDump_beforePrintseedsWithFile() throws Exception {
        task.dump();
        task.printseeds("seeds.txt");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should be file");
    }

    @Test
    public void testDump_withMixedOutputTargets() throws Exception {
        task.dump(); // stdout
        task.printseeds("seeds.txt"); // file
        task.printmapping(); // stdout

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should be file");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should be STD_OUT");
    }

    // ============================================================
    // Advanced Scenarios
    // ============================================================

    @Test
    public void testDump_fullProcessingPipeline() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.dump();
        task.printseeds();
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT in full pipeline");
    }

    @Test
    public void testDump_noProcessingScenario() throws Exception {
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.dontpreverify();
        task.dump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should work even with all processing disabled");
    }

    @Test
    public void testDump_minimalConfiguration() throws Exception {
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should work with minimal configuration");
    }

    @Test
    public void testDump_withComplexConfiguration() throws Exception {
        task.keep("class * { *; }");
        task.keepclassmembers("class * { public <methods>; }");
        task.dontoptimize();
        task.verbose();
        task.dump();

        assertNotNull(task.configuration.keep, "keep rules should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.verbose, "verbose should be enabled");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    // ============================================================
    // Multiple Task Instances
    // ============================================================

    @Test
    public void testDump_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.dump();
        task2.dump();

        assertEquals(Configuration.STD_OUT, task1.configuration.dump,
                "task1 dump should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task2.configuration.dump,
                "task2 dump should be STD_OUT");
    }

    @Test
    public void testDump_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.dump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "task dump should be STD_OUT");
        assertNull(otherTask.configuration.dump, "otherTask should not be affected");
    }

    // ============================================================
    // Timing and State Tests
    // ============================================================

    @Test
    public void testDump_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");
        task.dump();
        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testDump_afterManuallySetToNull() throws Exception {
        task.configuration.dump = null;
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be set to STD_OUT");
    }

    @Test
    public void testDump_whenAlreadyStdOut() throws Exception {
        task.configuration.dump = Configuration.STD_OUT;
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should remain STD_OUT");
    }

    @Test
    public void testDump_persistsAcrossGets() throws Exception {
        task.dump();
        File value1 = task.configuration.dump;
        File value2 = task.configuration.dump;

        assertSame(value1, value2, "Value should be consistent");
        assertEquals(Configuration.STD_OUT, value1, "Should be STD_OUT");
    }

    // ============================================================
    // Android-Specific Tests
    // ============================================================

    @Test
    public void testDump_inAndroidReleaseVariant() throws Exception {
        // Android release variant with dump enabled
        task.android();
        task.dump();

        assertTrue(task.configuration.android, "android should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testDump_inAndroidLibraryModule() throws Exception {
        // Android library with dump enabled
        task.android();
        task.dump();

        assertTrue(task.configuration.android, "android should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testDump_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build with dump enabled
        task.android();
        task.dump();

        assertTrue(task.configuration.android, "android should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testDump_forDebugBuild() throws Exception {
        // Debug build with dump for analysis
        task.dump();
        task.verbose();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.obfuscate,
                "obfuscation should be disabled");
    }

    @Test
    public void testDump_forReleaseBuild() throws Exception {
        // Release build with dump for verification
        task.dump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testDump_forStagingBuild() throws Exception {
        // Staging build with dump for testing
        task.dump();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testDump_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.dump, "Initial state should be null");

        task.dump();

        assertNotNull(task.configuration.dump, "State should be modified");
    }

    @Test
    public void testDump_preservesOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.ignorewarnings();

        task.dump();

        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertTrue(task.configuration.ignoreWarnings,
                "ignoreWarnings should be preserved");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
    }

    @Test
    public void testDump_withComplexConfigurationChain() throws Exception {
        task.verbose();
        task.dump();
        task.ignorewarnings();
        task.dontwarn();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings,
                "ignoreWarnings should be true");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    // ============================================================
    // Verification Tests
    // ============================================================

    @Test
    public void testDump_taskStateValid() throws Exception {
        task.dump();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testDump_semanticMeaning() throws Exception {
        // Verify the semantic meaning: outputs internal class structure to stdout
        task.dump();

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should enable dumping of internal class structure to stdout");
    }

    // ============================================================
    // Comparison with Getter Method
    // ============================================================

    @Test
    public void testDump_equivalentToGetter() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        task1.dump();
        task2.getdump();

        assertEquals(task1.configuration.dump,
                task2.configuration.dump,
                "Both methods should set the same value");
        assertEquals(Configuration.STD_OUT, task1.configuration.dump,
                "task1 should have STD_OUT");
        assertEquals(Configuration.STD_OUT, task2.configuration.dump,
                "task2 should have STD_OUT");
    }

    @Test
    public void testDump_canBeMixedWithGetter() throws Exception {
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "After direct call, should be set");

        task.getdump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "After getter call, should still be set");

        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "After another direct call, should still be set");
    }

    // ============================================================
    // Tests for dump(Object) Method
    // ============================================================

    @Test
    public void testDumpWithFile_setsDumpToFile() throws Exception {
        String dumpFile = "dump.txt";
        task.dump(dumpFile);
        assertNotNull(task.configuration.dump, "dump should not be null");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should have correct filename");
    }

    @Test
    public void testDumpWithFile_acceptsStringPath() throws Exception {
        task.dump("output/dump.txt");
        assertNotNull(task.configuration.dump, "dump should not be null");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should have correct filename");
    }

    @Test
    public void testDumpWithFile_acceptsFileObject() throws Exception {
        File dumpFile = new File(tempDir.toFile(), "dump.txt");
        task.dump(dumpFile);
        assertNotNull(task.configuration.dump, "dump should not be null");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should have correct filename");
    }

    @Test
    public void testDumpWithFile_overwritesPreviousValue() throws Exception {
        task.dump("first.txt");
        assertEquals("first.txt", task.configuration.dump.getName(),
                "dump should be first.txt");
        task.dump("second.txt");
        assertEquals("second.txt", task.configuration.dump.getName(),
                "dump should be overwritten to second.txt");
    }

    @Test
    public void testDumpWithFile_overwritesStdOut() throws Exception {
        task.dump(); // Set to stdout
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT");
        task.dump("dump.txt");
        assertNotEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should no longer be STD_OUT");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be dump.txt");
    }

    // ============================================================
    // File Path Variations for dump(Object)
    // ============================================================

    @Test
    public void testDumpWithFile_relativePathInBuildDir() throws Exception {
        task.dump("build/reports/dump.txt");
        assertNotNull(task.configuration.dump, "dump should not be null");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should have correct filename");
    }

    @Test
    public void testDumpWithFile_nestedDirectoryPath() throws Exception {
        task.dump("output/proguard/reports/dump.txt");
        assertNotNull(task.configuration.dump, "dump should not be null");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should have correct filename");
    }

    @Test
    public void testDumpWithFile_withDifferentExtensions() throws Exception {
        String[] extensions = {"dump.txt", "dump.log", "dump.out", "dump"};
        for (String filename : extensions) {
            ProGuardTask freshTask = project.getTasks().create("proguard_" + filename.replace(".", "_"), ProGuardTask.class);
            freshTask.dump(filename);
            assertNotNull(freshTask.configuration.dump,
                    "dump should not be null for " + filename);
        }
    }

    // ============================================================
    // Integration with Other Methods for dump(Object)
    // ============================================================

    @Test
    public void testDumpWithFile_afterDumpVoid() throws Exception {
        task.dump(); // stdout
        task.dump("dump.txt"); // file
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be dump.txt, overwriting stdout");
    }

    @Test
    public void testDumpWithFile_beforeDumpVoid() throws Exception {
        task.dump("dump.txt"); // file
        task.dump(); // stdout
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT, overwriting file");
    }

    @Test
    public void testDumpWithFile_worksWithOtherPrintMethods() throws Exception {
        task.printseeds("seeds.txt");
        task.dump("dump.txt");
        task.printmapping("mapping.txt");

        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should be seeds.txt");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be dump.txt");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should be mapping.txt");
    }

    @Test
    public void testDumpWithFile_worksWithDontShrink() throws Exception {
        task.dontshrink();
        task.dump("dump.txt");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be dump.txt");
    }

    @Test
    public void testDumpWithFile_worksWithKeepRules() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.dump("dump.txt");
        assertNotNull(task.configuration.keep, "keep rules should be set");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be dump.txt");
    }

    @Test
    public void testDumpWithFile_multipleCallsUpdateValue() throws Exception {
        task.dump("dump1.txt");
        assertEquals("dump1.txt", task.configuration.dump.getName(),
                "dump should be dump1.txt");

        task.dump("dump2.txt");
        assertEquals("dump2.txt", task.configuration.dump.getName(),
                "dump should be updated to dump2.txt");

        task.dump("dump3.txt");
        assertEquals("dump3.txt", task.configuration.dump.getName(),
                "dump should be updated to dump3.txt");
    }

    @Test
    public void testDumpWithFile_alternatingWithVoidMethod() throws Exception {
        task.dump("dump1.txt");
        assertEquals("dump1.txt", task.configuration.dump.getName(),
                "dump should be file");

        task.dump(); // stdout
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be stdout");

        task.dump("dump2.txt");
        assertEquals("dump2.txt", task.configuration.dump.getName(),
                "dump should be file again");

        task.dump(); // stdout
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be stdout again");
    }

    // ============================================================
    // Realistic Scenarios for dump(Object)
    // ============================================================

    @Test
    public void testDumpWithFile_androidBuildScenario() throws Exception {
        // Android build with file output for archiving
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.dump("build/outputs/proguard/dump.txt");

        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be saved to file for Android build");
    }

    @Test
    public void testDumpWithFile_releaseConfigurationScenario() throws Exception {
        // Release build saving dump to file for analysis
        task.dump("release/reports/dump.txt");
        task.printseeds("release/reports/seeds.txt");
        task.printmapping("release/reports/mapping.txt");

        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be saved to file for release");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should be saved");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should be saved");
    }

    @Test
    public void testDumpWithFile_archivingScenario() throws Exception {
        // Archive all output files for later analysis
        task.dump("archive/dump.txt");
        task.printusage("archive/usage.txt");
        task.printseeds("archive/seeds.txt");
        task.printmapping("archive/mapping.txt");

        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be archived");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "usage should be archived");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "seeds should be archived");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "mapping should be archived");
    }

    @Test
    public void testDumpWithFile_debuggingWithFileOutput() throws Exception {
        // Debug scenario saving dump to file
        task.dump("debug/class-dump.txt");
        task.verbose();
        task.dontobfuscate();

        assertEquals("class-dump.txt", task.configuration.dump.getName(),
                "dump should be saved to file for debugging");
        assertTrue(task.configuration.verbose, "verbose should be enabled");
        assertFalse(task.configuration.obfuscate,
                "obfuscation should be disabled");
    }

    @Test
    public void testDumpWithFile_developmentVsProduction() throws Exception {
        // Development: stdout
        ProGuardTask devTask = project.getTasks().create("proguardDev", ProGuardTask.class);
        devTask.dump();
        assertEquals(Configuration.STD_OUT, devTask.configuration.dump,
                "Development mode should use stdout");

        // Production: file
        ProGuardTask prodTask = project.getTasks().create("proguardProd", ProGuardTask.class);
        prodTask.dump("production/dump.txt");
        assertEquals("dump.txt", prodTask.configuration.dump.getName(),
                "Production mode should save to file");
    }

    // ============================================================
    // Edge Cases for dump(Object)
    // ============================================================

    @Test
    public void testDumpWithFile_doesNotModifyOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;

        task.dump("dump.txt");

        assertEquals(initialShrink, task.configuration.shrink,
                "dump(Object) should not affect shrink setting");
        assertEquals(initialOptimize, task.configuration.optimize,
                "dump(Object) should not affect optimize setting");
    }

    @Test
    public void testDumpWithFile_returnValueNotStdOut() throws Exception {
        task.dump("dump.txt");
        assertNotEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should not be Configuration.STD_OUT when file is specified");
    }

    @Test
    public void testDumpWithFile_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);

        freshTask.dump("fresh-dump.txt");

        assertNotNull(freshTask.configuration.dump,
                "Fresh task should have dump set");
        assertEquals("fresh-dump.txt", freshTask.configuration.dump.getName(),
                "dump should have correct filename");
    }

    @Test
    public void testDumpWithFile_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.dump("dump1.txt");
        task2.dump("dump2.txt");

        assertEquals("dump1.txt", task1.configuration.dump.getName(),
                "task1 should have its own dump file");
        assertEquals("dump2.txt", task2.configuration.dump.getName(),
                "task2 should have its own dump file");
        assertNotSame(task1.configuration.dump, task2.configuration.dump,
                "Each task should have independent configuration");
    }

    // ============================================================
    // File Object vs String Path for dump(Object)
    // ============================================================

    @Test
    public void testDumpWithFile_stringPathAndFileObjectEquivalent() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        task1.dump("dump.txt");
        task2.dump(new File(tempDir.toFile(), "dump.txt"));

        assertEquals("dump.txt", task1.configuration.dump.getName(),
                "String path should work");
        assertEquals("dump.txt", task2.configuration.dump.getName(),
                "File object should work");
    }

    @Test
    public void testDumpWithFile_absolutePath() throws Exception {
        File absoluteFile = new File(tempDir.toFile(), "absolute-dump.txt").getAbsoluteFile();
        task.dump(absoluteFile);

        assertNotNull(task.configuration.dump, "dump should not be null");
        assertEquals("absolute-dump.txt", task.configuration.dump.getName(),
                "dump should have correct filename");
    }

    // ============================================================
    // Mixed Output Targets for dump(Object)
    // ============================================================

    @Test
    public void testDumpWithFile_withMixedOutputTargets() throws Exception {
        task.dump("dump.txt"); // file
        task.printseeds(); // stdout
        task.printmapping("mapping.txt"); // file
        task.printusage(); // stdout

        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be file");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be stdout");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should be file");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be stdout");
    }

    @Test
    public void testDumpWithFile_allOutputsToFiles() throws Exception {
        task.dump("dump.txt");
        task.printseeds("seeds.txt");
        task.printmapping("mapping.txt");
        task.printusage("usage.txt");
        task.printconfiguration("config.txt");

        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be file");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should be file");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should be file");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should be file");
        assertEquals("config.txt", task.configuration.printConfiguration.getName(),
                "printConfiguration should be file");
    }

    // ============================================================
    // Call Order for dump(Object)
    // ============================================================

    @Test
    public void testDumpWithFile_canBeCalledBeforeOtherConfiguration() throws Exception {
        task.dump("dump.txt");
        task.verbose();
        task.keep("class * { *; }");

        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be file");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    @Test
    public void testDumpWithFile_canBeCalledAfterOtherConfiguration() throws Exception {
        task.verbose();
        task.keep("class * { *; }");
        task.dump("dump.txt");

        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be file");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    @Test
    public void testDumpWithFile_reflectsLatestConfiguration() throws Exception {
        task.dump("first.txt");
        assertEquals("first.txt", task.configuration.dump.getName());

        task.dump("second.txt");
        assertEquals("second.txt", task.configuration.dump.getName());

        task.dump("third.txt");
        assertEquals("third.txt", task.configuration.dump.getName());
    }

    // ============================================================
    // Android Scenarios for dump(Object)
    // ============================================================

    @Test
    public void testDumpWithFile_inAndroidDebugVariant() throws Exception {
        // Android debug variant with dump saved to file
        task.android();
        task.dump("debug/dump.txt");
        task.dontobfuscate();

        assertTrue(task.configuration.android, "android should be set");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be saved to file");
        assertFalse(task.configuration.obfuscate,
                "obfuscation should be disabled for debug");
    }

    @Test
    public void testDumpWithFile_inAndroidReleaseVariant() throws Exception {
        // Android release variant with dump archived
        task.android();
        task.dump("release/dump.txt");

        assertTrue(task.configuration.android, "android should be set");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be saved to file");
    }

    // ============================================================
    // Verification for dump(Object)
    // ============================================================

    @Test
    public void testDumpWithFile_taskStateValid() throws Exception {
        task.dump("dump.txt");

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testDumpWithFile_semanticMeaning() throws Exception {
        // Verify the semantic meaning: saves internal class structure to file
        task.dump("dump.txt");

        assertNotNull(task.configuration.dump,
                "dump should save internal class structure to file");
        assertEquals("dump.txt", task.configuration.dump.getName(),
                "dump should be the specified file");
    }
}

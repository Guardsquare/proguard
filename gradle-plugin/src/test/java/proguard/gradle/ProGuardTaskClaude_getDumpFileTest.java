package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import proguard.Configuration;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.getDumpFile()Ljava/io/File; method.
 * Tests the getDumpFile() method that returns the File for dump output,
 * or null if the output is set to stdout or is not configured.
 */
public class ProGuardTaskClaude_getDumpFileTest {

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
    public void testGetDumpFile_returnsNullInitially() throws Exception {
        assertNull(task.configuration.dump, "dump should be null initially");
        File result = task.getDumpFile();
        assertNull(result, "getDumpFile() should return null when dump is not configured");
    }

    @Test
    public void testGetDumpFile_returnsNullForStdout() throws Exception {
        task.dump();
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be stdout");
        File result = task.getDumpFile();
        assertNull(result, "getDumpFile() should return null for stdout");
    }

    @Test
    public void testGetDumpFile_returnsFileWhenConfigured() throws Exception {
        task.dump("dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should return a File when configured with a file");
        assertEquals("dump.txt", result.getName(), "Returned file should have correct name");
    }

    @Test
    public void testGetDumpFile_returnsActualFileObject() throws Exception {
        task.dump("output/dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should return a File");
        assertSame(task.configuration.dump, result,
                "Returned file should be the same object as configuration.dump");
    }

    // ============================================================
    // Interaction with dump() void method
    // ============================================================

    @Test
    public void testGetDumpFile_afterDumpVoid() throws Exception {
        task.dump(); // Sets to stdout
        File result = task.getDumpFile();
        assertNull(result, "getDumpFile() should return null after dump()");
    }

    @Test
    public void testGetDumpFile_multipleCallsAfterDumpVoid() throws Exception {
        task.dump();
        for (int i = 0; i < 3; i++) {
            File result = task.getDumpFile();
            assertNull(result, "getDumpFile() should consistently return null for stdout");
        }
    }

    // ============================================================
    // Interaction with dump(Object) method
    // ============================================================

    @Test
    public void testGetDumpFile_afterDumpWithFile() throws Exception {
        task.dump("dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should return File after dump(Object)");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_afterDumpWithDifferentFiles() throws Exception {
        task.dump("dump1.txt");
        File result1 = task.getDumpFile();
        assertEquals("dump1.txt", result1.getName());

        task.dump("dump2.txt");
        File result2 = task.getDumpFile();
        assertEquals("dump2.txt", result2.getName());
    }

    @Test
    public void testGetDumpFile_switchingFromFileToStdout() throws Exception {
        task.dump("dump.txt");
        File result1 = task.getDumpFile();
        assertNotNull(result1, "Should return File initially");

        task.dump(); // Switch to stdout
        File result2 = task.getDumpFile();
        assertNull(result2, "Should return null after switching to stdout");
    }

    @Test
    public void testGetDumpFile_switchingFromStdoutToFile() throws Exception {
        task.dump(); // stdout
        File result1 = task.getDumpFile();
        assertNull(result1, "Should return null for stdout");

        task.dump("dump.txt"); // Switch to file
        File result2 = task.getDumpFile();
        assertNotNull(result2, "Should return File after switching to file");
        assertEquals("dump.txt", result2.getName());
    }

    // ============================================================
    // Multiple calls to getDumpFile()
    // ============================================================

    @Test
    public void testGetDumpFile_multipleCallsReturnSameResult() throws Exception {
        task.dump("dump.txt");
        File result1 = task.getDumpFile();
        File result2 = task.getDumpFile();
        File result3 = task.getDumpFile();

        assertSame(result1, result2, "Multiple calls should return same File object");
        assertSame(result2, result3, "Multiple calls should return same File object");
    }

    @Test
    public void testGetDumpFile_idempotentForNull() throws Exception {
        // No configuration
        File result1 = task.getDumpFile();
        File result2 = task.getDumpFile();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
    }

    @Test
    public void testGetDumpFile_idempotentForStdout() throws Exception {
        task.dump();
        File result1 = task.getDumpFile();
        File result2 = task.getDumpFile();
        File result3 = task.getDumpFile();

        assertNull(result1, "All calls should return null for stdout");
        assertNull(result2, "All calls should return null for stdout");
        assertNull(result3, "All calls should return null for stdout");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetDumpFile_withOtherPrintMethods() throws Exception {
        task.printseeds("seeds.txt");
        task.dump("dump.txt");
        task.printmapping("mapping.txt");

        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should return File");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_withDontShrink() throws Exception {
        task.dontshrink();
        task.dump("dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work with dontshrink()");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_withKeepRules() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.dump("dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work with keep rules");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_withFullConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.dump("dump.txt");
        task.keep("public class * { public *; }");

        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work in full configuration");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_withVerbose() throws Exception {
        task.verbose();
        task.dump("dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work with verbose()");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_withIgnoreWarnings() throws Exception {
        task.ignorewarnings();
        task.dump("dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work with ignorewarnings()");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_withDontOptimize() throws Exception {
        task.dontoptimize();
        task.dump("dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work with dontoptimize()");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_withDontObfuscate() throws Exception {
        task.dontobfuscate();
        task.dump("dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work with dontobfuscate()");
        assertEquals("dump.txt", result.getName());
    }

    // ============================================================
    // File Path Variations
    // ============================================================

    @Test
    public void testGetDumpFile_withNestedPath() throws Exception {
        task.dump("build/reports/proguard/dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work with nested paths");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_withDifferentExtensions() throws Exception {
        String[] filenames = {"dump.txt", "dump.log", "dump.out", "dump", "class-dump.txt"};
        for (String filename : filenames) {
            ProGuardTask freshTask = project.getTasks().create("proguard_" + filename.replace(".", "_"), ProGuardTask.class);
            freshTask.dump(filename);
            File result = freshTask.getDumpFile();
            assertNotNull(result, "getDumpFile() should work with " + filename);
            assertEquals(filename, result.getName());
        }
    }

    @Test
    public void testGetDumpFile_withFileObject() throws Exception {
        File dumpFile = new File(tempDir.toFile(), "dump.txt");
        task.dump(dumpFile);
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work when configured with File object");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_withRelativePath() throws Exception {
        task.dump("output/proguard-dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work with relative paths");
        assertEquals("proguard-dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_withAbsolutePath() throws Exception {
        File absoluteFile = new File(tempDir.toFile(), "absolute-dump.txt").getAbsoluteFile();
        task.dump(absoluteFile);
        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work with absolute paths");
        assertEquals("absolute-dump.txt", result.getName());
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetDumpFile_androidBuildScenario() throws Exception {
        // Android build with file output
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.dump("build/outputs/proguard/dump.txt");

        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should return file for Android build");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_cicdWithStdout() throws Exception {
        // CI/CD scenario with stdout
        task.dump(); // stdout for CI/CD logging
        task.printseeds(); // stdout
        task.printmapping(); // stdout

        File result = task.getDumpFile();
        assertNull(result, "getDumpFile() should return null for CI/CD stdout scenario");
    }

    @Test
    public void testGetDumpFile_releaseConfiguration() throws Exception {
        // Release build with file archiving
        task.dump("release/reports/dump.txt");
        task.printseeds("release/reports/seeds.txt");
        task.printmapping("release/reports/mapping.txt");

        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should return file for release build");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_developmentVsProduction() throws Exception {
        // Development: stdout
        ProGuardTask devTask = project.getTasks().create("proguardDev", ProGuardTask.class);
        devTask.dump();
        File devResult = devTask.getDumpFile();
        assertNull(devResult, "Development mode should return null (stdout)");

        // Production: file
        ProGuardTask prodTask = project.getTasks().create("proguardProd", ProGuardTask.class);
        prodTask.dump("production/dump.txt");
        File prodResult = prodTask.getDumpFile();
        assertNotNull(prodResult, "Production mode should return File");
        assertEquals("dump.txt", prodResult.getName());
    }

    @Test
    public void testGetDumpFile_debugBuildWithFile() throws Exception {
        // Debug build with dump saved to file for review
        task.dontobfuscate();
        task.verbose();
        task.dump("debug/dump.txt");

        File result = task.getDumpFile();
        assertNotNull(result, "Debug build should be able to save dump to file");
        assertEquals("dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_auditScenario() throws Exception {
        // Auditing class files for compliance
        task.keep("class * { *; }");
        task.dump("audit/proguard-dump.txt");
        task.verbose();

        File result = task.getDumpFile();
        assertNotNull(result, "Audit scenario should save dump to file");
        assertEquals("proguard-dump.txt", result.getName());
    }

    @Test
    public void testGetDumpFile_troubleshootingScenario() throws Exception {
        // Troubleshooting build issues - output to file for analysis
        task.dump("troubleshooting/dump-snapshot.txt");
        task.verbose();
        task.printusage("troubleshooting/usage.txt");
        task.printmapping("troubleshooting/mapping.txt");

        File result = task.getDumpFile();
        assertNotNull(result, "Troubleshooting should save dump for analysis");
        assertEquals("dump-snapshot.txt", result.getName());
    }

    // ============================================================
    // Edge Cases and Validation
    // ============================================================

    @Test
    public void testGetDumpFile_doesNotModifyConfiguration() throws Exception {
        task.dump("dump.txt");
        File original = task.configuration.dump;

        task.getDumpFile();

        assertSame(original, task.configuration.dump,
                "getDumpFile() should not modify configuration.dump");
    }

    @Test
    public void testGetDumpFile_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;

        task.dump("dump.txt");
        task.getDumpFile();

        assertEquals(initialShrink, task.configuration.shrink,
                "getDumpFile() should not affect shrink setting");
        assertEquals(initialOptimize, task.configuration.optimize,
                "getDumpFile() should not affect optimize setting");
    }

    @Test
    public void testGetDumpFile_returnValueNotStdOut() throws Exception {
        task.dump("dump.txt");
        File result = task.getDumpFile();
        assertNotEquals(Configuration.STD_OUT, result,
                "Returned file should not be Configuration.STD_OUT");
    }

    @Test
    public void testGetDumpFile_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);

        // Test null case
        File result1 = freshTask.getDumpFile();
        assertNull(result1, "Fresh task should return null initially");

        // Test file case
        freshTask.dump("fresh-dump.txt");
        File result2 = freshTask.getDumpFile();
        assertNotNull(result2, "Fresh task should return File after configuration");
        assertEquals("fresh-dump.txt", result2.getName());
    }

    @Test
    public void testGetDumpFile_withMultipleTaskInstances() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.dump("dump1.txt");
        task2.dump("dump2.txt");

        File result1 = task1.getDumpFile();
        File result2 = task2.getDumpFile();

        assertEquals("dump1.txt", result1.getName(), "Task1 should have its own dump file");
        assertEquals("dump2.txt", result2.getName(), "Task2 should have its own dump file");
        assertNotSame(result1, result2, "Each task should have independent configuration");
    }

    @Test
    public void testGetDumpFile_nullIsConsistent() throws Exception {
        // Null when not configured
        assertNull(task.getDumpFile());

        // Null when stdout
        task.dump();
        assertNull(task.getDumpFile());

        // Both cases should be consistent
        assertNull(task.getDumpFile());
    }

    // ============================================================
    // Call Order Tests
    // ============================================================

    @Test
    public void testGetDumpFile_canBeCalledBeforeConfiguration() throws Exception {
        File result = task.getDumpFile();
        assertNull(result, "getDumpFile() should return null before configuration");
    }

    @Test
    public void testGetDumpFile_canBeCalledMultipleTimesDuringConfiguration() throws Exception {
        File result1 = task.getDumpFile();
        assertNull(result1);

        task.dump("dump1.txt");
        File result2 = task.getDumpFile();
        assertEquals("dump1.txt", result2.getName());

        task.dump();
        File result3 = task.getDumpFile();
        assertNull(result3);

        task.dump("dump2.txt");
        File result4 = task.getDumpFile();
        assertEquals("dump2.txt", result4.getName());
    }

    @Test
    public void testGetDumpFile_reflectsLatestConfiguration() throws Exception {
        task.dump("first.txt");
        assertEquals("first.txt", task.getDumpFile().getName());

        task.dump("second.txt");
        assertEquals("second.txt", task.getDumpFile().getName());

        task.dump("third.txt");
        assertEquals("third.txt", task.getDumpFile().getName());
    }

    @Test
    public void testGetDumpFile_afterConfigurationChanges() throws Exception {
        task.dump("initial.txt");
        assertEquals("initial.txt", task.getDumpFile().getName());

        task.verbose();
        assertEquals("initial.txt", task.getDumpFile().getName(),
                "Other configuration changes should not affect return value");

        task.dump("updated.txt");
        assertEquals("updated.txt", task.getDumpFile().getName());
    }

    // ============================================================
    // Interaction with Other Output File Getters
    // ============================================================

    @Test
    public void testGetDumpFile_independentOfOtherFileGetters() throws Exception {
        task.printseeds("seeds.txt");
        task.dump("dump.txt");
        task.printmapping("mapping.txt");
        task.printusage("usage.txt");

        File dumpFile = task.getDumpFile();
        File seedsFile = task.getPrintSeedsFile();
        File mappingFile = task.getPrintMappingFile();
        File usageFile = task.getPrintUsageFile();

        assertEquals("dump.txt", dumpFile.getName());
        assertEquals("seeds.txt", seedsFile.getName());
        assertEquals("mapping.txt", mappingFile.getName());
        assertEquals("usage.txt", usageFile.getName());

        // Each getter should return its own file
        assertNotSame(dumpFile, seedsFile);
        assertNotSame(dumpFile, mappingFile);
        assertNotSame(dumpFile, usageFile);
    }

    @Test
    public void testGetDumpFile_withMixedOutputTargets() throws Exception {
        task.dump("dump.txt"); // file
        task.printseeds(); // stdout
        task.printmapping("mapping.txt"); // file
        task.printusage(); // stdout

        File dumpFile = task.getDumpFile();
        File seedsFile = task.getPrintSeedsFile();
        File mappingFile = task.getPrintMappingFile();
        File usageFile = task.getPrintUsageFile();

        assertNotNull(dumpFile, "dump should return File");
        assertNull(seedsFile, "seeds should return null (stdout)");
        assertNotNull(mappingFile, "mapping should return File");
        assertNull(usageFile, "usage should return null (stdout)");

        assertEquals("dump.txt", dumpFile.getName());
        assertEquals("mapping.txt", mappingFile.getName());
    }

    @Test
    public void testGetDumpFile_allOutputsToFiles() throws Exception {
        task.dump("dump.txt");
        task.printseeds("seeds.txt");
        task.printmapping("mapping.txt");
        task.printusage("usage.txt");

        assertNotNull(task.getDumpFile(), "All outputs should return Files");
        assertNotNull(task.getPrintSeedsFile());
        assertNotNull(task.getPrintMappingFile());
        assertNotNull(task.getPrintUsageFile());
    }

    @Test
    public void testGetDumpFile_allOutputsToStdout() throws Exception {
        task.dump();
        task.printseeds();
        task.printmapping();
        task.printusage();

        assertNull(task.getDumpFile(), "All outputs should return null (stdout)");
        assertNull(task.getPrintSeedsFile());
        assertNull(task.getPrintMappingFile());
        assertNull(task.getPrintUsageFile());
    }

    // ============================================================
    // Annotation and Gradle Integration Tests
    // ============================================================

    @Test
    public void testGetDumpFile_isMarkedAsOptional() throws Exception {
        // The method is annotated with @Optional, meaning it can return null
        // This test verifies that null is an acceptable return value
        File result = task.getDumpFile();
        // No exception should be thrown, null is valid
        assertNull(result, "Method should be able to return null (marked as @Optional)");
    }

    @Test
    public void testGetDumpFile_isMarkedAsOutputFile() throws Exception {
        // The method is annotated with @OutputFile
        // When configured with a file, it should return a File for Gradle's tracking
        task.dump("dump.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "Method should return File for Gradle output tracking");
    }

    @Test
    public void testGetDumpFile_gradleTaskDependencyTracking() throws Exception {
        // This method is used by Gradle for task dependency tracking
        // When set to a file, Gradle can track it as an output
        task.dump("build/dump/proguard.txt");
        File result = task.getDumpFile();
        assertNotNull(result, "File should be trackable by Gradle as an output");
    }

    // ============================================================
    // Semantic and Documentation Tests
    // ============================================================

    @Test
    public void testGetDumpFile_semanticMeaning() throws Exception {
        // Verify the semantic meaning: returns the dump output file or null
        task.dump("dump.txt");
        File result = task.getDumpFile();

        assertNotNull(result, "Method should return the dump output file");
        assertEquals(task.configuration.dump, result,
                "Returned file should match the configured dump");
    }

    @Test
    public void testGetDumpFile_returnsNullForNonFileOutputs() throws Exception {
        // Method should return null for stdout (special marker file)
        task.dump(); // stdout
        File result = task.getDumpFile();

        assertNull(result, "Method should return null when output is directed to stdout");
    }

    @Test
    public void testGetDumpFile_isReadOnlyOperation() throws Exception {
        // This is a getter method - it should not modify any state
        task.dump("dump.txt");
        File originalDump = task.configuration.dump;
        boolean originalShrink = task.configuration.shrink;
        boolean originalOptimize = task.configuration.optimize;

        task.getDumpFile();

        assertSame(originalDump, task.configuration.dump,
                "Configuration should not be modified");
        assertEquals(originalShrink, task.configuration.shrink,
                "Shrink setting should not be modified");
        assertEquals(originalOptimize, task.configuration.optimize,
                "Optimize setting should not be modified");
    }

    // ============================================================
    // Interaction with getdump() method
    // ============================================================

    @Test
    public void testGetDumpFile_afterGetdump() throws Exception {
        // getdump() calls dump() which sets to stdout
        task.getdump();
        File result = task.getDumpFile();
        assertNull(result, "getDumpFile() should return null after getdump()");
    }

    @Test
    public void testGetDumpFile_getdumpSideEffect() throws Exception {
        assertNull(task.configuration.dump, "dump should be null initially");
        task.getdump(); // Side effect: sets dump to stdout
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "getdump() should set dump to stdout");
        File result = task.getDumpFile();
        assertNull(result, "getDumpFile() should return null for stdout");
    }

    // ============================================================
    // Comprehensive Integration Tests
    // ============================================================

    @Test
    public void testGetDumpFile_withPrintConfigurationFile() throws Exception {
        task.dump("dump.txt");
        task.printconfiguration("config.txt");

        File dumpFile = task.getDumpFile();
        File configFile = task.getPrintConfigurationFile();

        assertNotNull(dumpFile, "dump should return File");
        assertNotNull(configFile, "printconfiguration should return File");
        assertNotSame(dumpFile, configFile, "Different methods should return different files");

        assertEquals("dump.txt", dumpFile.getName());
        assertEquals("config.txt", configFile.getName());
    }

    @Test
    public void testGetDumpFile_complexBuildScenario() throws Exception {
        // Complex Android/Java build with multiple outputs
        task.injars("build/libs/input.jar");
        task.outjars("build/libs/output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods");

        task.keep("public class * { public *; }");
        task.keepattributes("Signature,Exceptions,*Annotation*");

        task.verbose();
        task.dump("build/reports/proguard/dump.txt");
        task.printseeds("build/reports/proguard/seeds.txt");
        task.printmapping("build/reports/proguard/mapping.txt");
        task.printusage("build/reports/proguard/usage.txt");
        task.printconfiguration("build/reports/proguard/config.txt");

        File result = task.getDumpFile();
        assertNotNull(result, "getDumpFile() should work in complex build");
        assertEquals("dump.txt", result.getName());

        // Verify all output files are independent
        assertNotSame(result, task.getPrintSeedsFile());
        assertNotSame(result, task.getPrintMappingFile());
        assertNotSame(result, task.getPrintUsageFile());
        assertNotSame(result, task.getPrintConfigurationFile());
    }
}

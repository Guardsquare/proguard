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
 * Test class for ProGuardTask.getPrintConfigurationFile()Ljava/io/File; method.
 * Tests the getPrintConfigurationFile() method that returns the File for printConfiguration output,
 * or null if the output is set to stdout or is not configured.
 */
public class ProGuardTaskClaude_getPrintConfigurationFileTest {

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
    public void testGetPrintConfigurationFile_returnsNullInitially() throws Exception {
        assertNull(task.configuration.printConfiguration, "printConfiguration should be null initially");
        File result = task.getPrintConfigurationFile();
        assertNull(result, "getPrintConfigurationFile() should return null when printConfiguration is not configured");
    }

    @Test
    public void testGetPrintConfigurationFile_returnsNullForStdout() throws Exception {
        task.printconfiguration();
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be stdout");
        File result = task.getPrintConfigurationFile();
        assertNull(result, "getPrintConfigurationFile() should return null for stdout");
    }

    @Test
    public void testGetPrintConfigurationFile_returnsFileWhenConfigured() throws Exception {
        task.printconfiguration("config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should return a File when configured with a file");
        assertEquals("config.txt", result.getName(), "Returned file should have correct name");
    }

    @Test
    public void testGetPrintConfigurationFile_returnsActualFileObject() throws Exception {
        task.printconfiguration("output/config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should return a File");
        assertSame(task.configuration.printConfiguration, result,
                "Returned file should be the same object as configuration.printConfiguration");
    }

    // ============================================================
    // Interaction with printconfiguration() void method
    // ============================================================

    @Test
    public void testGetPrintConfigurationFile_afterPrintconfigurationVoid() throws Exception {
        task.printconfiguration(); // Sets to stdout
        File result = task.getPrintConfigurationFile();
        assertNull(result, "getPrintConfigurationFile() should return null after printconfiguration()");
    }

    @Test
    public void testGetPrintConfigurationFile_multipleCallsAfterPrintconfigurationVoid() throws Exception {
        task.printconfiguration();
        for (int i = 0; i < 3; i++) {
            File result = task.getPrintConfigurationFile();
            assertNull(result, "getPrintConfigurationFile() should consistently return null for stdout");
        }
    }

    // ============================================================
    // Interaction with printconfiguration(Object) method
    // ============================================================

    @Test
    public void testGetPrintConfigurationFile_afterPrintconfigurationWithFile() throws Exception {
        task.printconfiguration("config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should return File after printconfiguration(Object)");
        assertEquals("config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_afterPrintconfigurationWithDifferentFiles() throws Exception {
        task.printconfiguration("config1.txt");
        File result1 = task.getPrintConfigurationFile();
        assertEquals("config1.txt", result1.getName());

        task.printconfiguration("config2.txt");
        File result2 = task.getPrintConfigurationFile();
        assertEquals("config2.txt", result2.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_switchingFromFileToStdout() throws Exception {
        task.printconfiguration("config.txt");
        File result1 = task.getPrintConfigurationFile();
        assertNotNull(result1, "Should return File initially");

        task.printconfiguration(); // Switch to stdout
        File result2 = task.getPrintConfigurationFile();
        assertNull(result2, "Should return null after switching to stdout");
    }

    @Test
    public void testGetPrintConfigurationFile_switchingFromStdoutToFile() throws Exception {
        task.printconfiguration(); // stdout
        File result1 = task.getPrintConfigurationFile();
        assertNull(result1, "Should return null for stdout");

        task.printconfiguration("config.txt"); // Switch to file
        File result2 = task.getPrintConfigurationFile();
        assertNotNull(result2, "Should return File after switching to file");
        assertEquals("config.txt", result2.getName());
    }

    // ============================================================
    // Multiple calls to getPrintConfigurationFile()
    // ============================================================

    @Test
    public void testGetPrintConfigurationFile_multipleCallsReturnSameResult() throws Exception {
        task.printconfiguration("config.txt");
        File result1 = task.getPrintConfigurationFile();
        File result2 = task.getPrintConfigurationFile();
        File result3 = task.getPrintConfigurationFile();

        assertSame(result1, result2, "Multiple calls should return same File object");
        assertSame(result2, result3, "Multiple calls should return same File object");
    }

    @Test
    public void testGetPrintConfigurationFile_idempotentForNull() throws Exception {
        // No configuration
        File result1 = task.getPrintConfigurationFile();
        File result2 = task.getPrintConfigurationFile();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
    }

    @Test
    public void testGetPrintConfigurationFile_idempotentForStdout() throws Exception {
        task.printconfiguration();
        File result1 = task.getPrintConfigurationFile();
        File result2 = task.getPrintConfigurationFile();
        File result3 = task.getPrintConfigurationFile();

        assertNull(result1, "All calls should return null for stdout");
        assertNull(result2, "All calls should return null for stdout");
        assertNull(result3, "All calls should return null for stdout");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetPrintConfigurationFile_withOtherPrintMethods() throws Exception {
        task.printseeds("seeds.txt");
        task.printconfiguration("config.txt");
        task.printmapping("mapping.txt");

        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should return File");
        assertEquals("config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_withDontShrink() throws Exception {
        task.dontshrink();
        task.printconfiguration("config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should work with dontshrink()");
        assertEquals("config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_withKeepRules() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.printconfiguration("config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should work with keep rules");
        assertEquals("config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_withFullConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.printconfiguration("config.txt");
        task.keep("public class * { public *; }");

        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should work in full configuration");
        assertEquals("config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_withVerbose() throws Exception {
        task.verbose();
        task.printconfiguration("config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should work with verbose()");
        assertEquals("config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_withIgnoreWarnings() throws Exception {
        task.ignorewarnings();
        task.printconfiguration("config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should work with ignorewarnings()");
        assertEquals("config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_withDontOptimize() throws Exception {
        task.dontoptimize();
        task.printconfiguration("config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should work with dontoptimize()");
        assertEquals("config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_withDontObfuscate() throws Exception {
        task.dontobfuscate();
        task.printconfiguration("config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should work with dontobfuscate()");
        assertEquals("config.txt", result.getName());
    }

    // ============================================================
    // File Path Variations
    // ============================================================

    @Test
    public void testGetPrintConfigurationFile_withNestedPath() throws Exception {
        task.printconfiguration("build/reports/proguard/config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should work with nested paths");
        assertEquals("config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_withDifferentExtensions() throws Exception {
        String[] filenames = {"config.txt", "config.log", "config.out", "config", "proguard.cfg"};
        for (String filename : filenames) {
            ProGuardTask freshTask = project.getTasks().create("proguard_" + filename.replace(".", "_"), ProGuardTask.class);
            freshTask.printconfiguration(filename);
            File result = freshTask.getPrintConfigurationFile();
            assertNotNull(result, "getPrintConfigurationFile() should work with " + filename);
            assertEquals(filename, result.getName());
        }
    }

    @Test
    public void testGetPrintConfigurationFile_withFileObject() throws Exception {
        File configFile = new File(tempDir.toFile(), "config.txt");
        task.printconfiguration(configFile);
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should work when configured with File object");
        assertEquals("config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_withRelativePath() throws Exception {
        task.printconfiguration("output/proguard-config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should work with relative paths");
        assertEquals("proguard-config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_withAbsolutePath() throws Exception {
        File absoluteFile = new File(tempDir.toFile(), "absolute-config.txt").getAbsoluteFile();
        task.printconfiguration(absoluteFile);
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should work with absolute paths");
        assertEquals("absolute-config.txt", result.getName());
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetPrintConfigurationFile_androidBuildScenario() throws Exception {
        // Android build with file output
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.printconfiguration("build/outputs/proguard/configuration.txt");

        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should return file for Android build");
        assertEquals("configuration.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_cicdWithStdout() throws Exception {
        // CI/CD scenario with stdout
        task.printconfiguration(); // stdout for CI/CD logging
        task.printseeds(); // stdout
        task.printmapping(); // stdout

        File result = task.getPrintConfigurationFile();
        assertNull(result, "getPrintConfigurationFile() should return null for CI/CD stdout scenario");
    }

    @Test
    public void testGetPrintConfigurationFile_releaseConfiguration() throws Exception {
        // Release build with file archiving
        task.printconfiguration("release/reports/configuration.txt");
        task.printseeds("release/reports/seeds.txt");
        task.printmapping("release/reports/mapping.txt");

        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "getPrintConfigurationFile() should return file for release build");
        assertEquals("configuration.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_developmentVsProduction() throws Exception {
        // Development: stdout
        ProGuardTask devTask = project.getTasks().create("proguardDev", ProGuardTask.class);
        devTask.printconfiguration();
        File devResult = devTask.getPrintConfigurationFile();
        assertNull(devResult, "Development mode should return null (stdout)");

        // Production: file
        ProGuardTask prodTask = project.getTasks().create("proguardProd", ProGuardTask.class);
        prodTask.printconfiguration("production/configuration.txt");
        File prodResult = prodTask.getPrintConfigurationFile();
        assertNotNull(prodResult, "Production mode should return File");
        assertEquals("configuration.txt", prodResult.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_debugBuildWithFile() throws Exception {
        // Debug build with configuration saved to file for review
        task.dontobfuscate();
        task.verbose();
        task.printconfiguration("debug/config.txt");

        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "Debug build should be able to save configuration to file");
        assertEquals("config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_auditScenario() throws Exception {
        // Auditing configuration for compliance
        task.keep("class * { *; }");
        task.printconfiguration("audit/proguard-config.txt");
        task.verbose();

        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "Audit scenario should save configuration to file");
        assertEquals("proguard-config.txt", result.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_troubleshootingScenario() throws Exception {
        // Troubleshooting build issues - output to file for analysis
        task.printconfiguration("troubleshooting/config-snapshot.txt");
        task.verbose();
        task.printusage("troubleshooting/usage.txt");
        task.printmapping("troubleshooting/mapping.txt");

        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "Troubleshooting should save configuration for analysis");
        assertEquals("config-snapshot.txt", result.getName());
    }

    // ============================================================
    // Edge Cases and Validation
    // ============================================================

    @Test
    public void testGetPrintConfigurationFile_doesNotModifyConfiguration() throws Exception {
        task.printconfiguration("config.txt");
        File original = task.configuration.printConfiguration;

        task.getPrintConfigurationFile();

        assertSame(original, task.configuration.printConfiguration,
                "getPrintConfigurationFile() should not modify configuration.printConfiguration");
    }

    @Test
    public void testGetPrintConfigurationFile_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;

        task.printconfiguration("config.txt");
        task.getPrintConfigurationFile();

        assertEquals(initialShrink, task.configuration.shrink,
                "getPrintConfigurationFile() should not affect shrink setting");
        assertEquals(initialOptimize, task.configuration.optimize,
                "getPrintConfigurationFile() should not affect optimize setting");
    }

    @Test
    public void testGetPrintConfigurationFile_returnValueNotStdOut() throws Exception {
        task.printconfiguration("config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotEquals(Configuration.STD_OUT, result,
                "Returned file should not be Configuration.STD_OUT");
    }

    @Test
    public void testGetPrintConfigurationFile_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);

        // Test null case
        File result1 = freshTask.getPrintConfigurationFile();
        assertNull(result1, "Fresh task should return null initially");

        // Test file case
        freshTask.printconfiguration("fresh-config.txt");
        File result2 = freshTask.getPrintConfigurationFile();
        assertNotNull(result2, "Fresh task should return File after configuration");
        assertEquals("fresh-config.txt", result2.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_withMultipleTaskInstances() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.printconfiguration("config1.txt");
        task2.printconfiguration("config2.txt");

        File result1 = task1.getPrintConfigurationFile();
        File result2 = task2.getPrintConfigurationFile();

        assertEquals("config1.txt", result1.getName(), "Task1 should have its own config file");
        assertEquals("config2.txt", result2.getName(), "Task2 should have its own config file");
        assertNotSame(result1, result2, "Each task should have independent configuration");
    }

    @Test
    public void testGetPrintConfigurationFile_nullIsConsistent() throws Exception {
        // Null when not configured
        assertNull(task.getPrintConfigurationFile());

        // Null when stdout
        task.printconfiguration();
        assertNull(task.getPrintConfigurationFile());

        // Both cases should be consistent
        assertNull(task.getPrintConfigurationFile());
    }

    // ============================================================
    // Call Order Tests
    // ============================================================

    @Test
    public void testGetPrintConfigurationFile_canBeCalledBeforeConfiguration() throws Exception {
        File result = task.getPrintConfigurationFile();
        assertNull(result, "getPrintConfigurationFile() should return null before configuration");
    }

    @Test
    public void testGetPrintConfigurationFile_canBeCalledMultipleTimesDuringConfiguration() throws Exception {
        File result1 = task.getPrintConfigurationFile();
        assertNull(result1);

        task.printconfiguration("config1.txt");
        File result2 = task.getPrintConfigurationFile();
        assertEquals("config1.txt", result2.getName());

        task.printconfiguration();
        File result3 = task.getPrintConfigurationFile();
        assertNull(result3);

        task.printconfiguration("config2.txt");
        File result4 = task.getPrintConfigurationFile();
        assertEquals("config2.txt", result4.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_reflectsLatestConfiguration() throws Exception {
        task.printconfiguration("first.txt");
        assertEquals("first.txt", task.getPrintConfigurationFile().getName());

        task.printconfiguration("second.txt");
        assertEquals("second.txt", task.getPrintConfigurationFile().getName());

        task.printconfiguration("third.txt");
        assertEquals("third.txt", task.getPrintConfigurationFile().getName());
    }

    @Test
    public void testGetPrintConfigurationFile_afterConfigurationChanges() throws Exception {
        task.printconfiguration("initial.txt");
        assertEquals("initial.txt", task.getPrintConfigurationFile().getName());

        task.verbose();
        assertEquals("initial.txt", task.getPrintConfigurationFile().getName(),
                "Other configuration changes should not affect return value");

        task.printconfiguration("updated.txt");
        assertEquals("updated.txt", task.getPrintConfigurationFile().getName());
    }

    // ============================================================
    // Interaction with Other Output File Getters
    // ============================================================

    @Test
    public void testGetPrintConfigurationFile_independentOfOtherFileGetters() throws Exception {
        task.printseeds("seeds.txt");
        task.printconfiguration("config.txt");
        task.printmapping("mapping.txt");
        task.printusage("usage.txt");

        File configFile = task.getPrintConfigurationFile();
        File seedsFile = task.getPrintSeedsFile();
        File mappingFile = task.getPrintMappingFile();
        File usageFile = task.getPrintUsageFile();

        assertEquals("config.txt", configFile.getName());
        assertEquals("seeds.txt", seedsFile.getName());
        assertEquals("mapping.txt", mappingFile.getName());
        assertEquals("usage.txt", usageFile.getName());

        // Each getter should return its own file
        assertNotSame(configFile, seedsFile);
        assertNotSame(configFile, mappingFile);
        assertNotSame(configFile, usageFile);
    }

    @Test
    public void testGetPrintConfigurationFile_withMixedOutputTargets() throws Exception {
        task.printconfiguration("config.txt"); // file
        task.printseeds(); // stdout
        task.printmapping("mapping.txt"); // file
        task.printusage(); // stdout

        File configFile = task.getPrintConfigurationFile();
        File seedsFile = task.getPrintSeedsFile();
        File mappingFile = task.getPrintMappingFile();
        File usageFile = task.getPrintUsageFile();

        assertNotNull(configFile, "configuration should return File");
        assertNull(seedsFile, "seeds should return null (stdout)");
        assertNotNull(mappingFile, "mapping should return File");
        assertNull(usageFile, "usage should return null (stdout)");

        assertEquals("config.txt", configFile.getName());
        assertEquals("mapping.txt", mappingFile.getName());
    }

    @Test
    public void testGetPrintConfigurationFile_allOutputsToFiles() throws Exception {
        task.printconfiguration("config.txt");
        task.printseeds("seeds.txt");
        task.printmapping("mapping.txt");
        task.printusage("usage.txt");

        assertNotNull(task.getPrintConfigurationFile(), "All outputs should return Files");
        assertNotNull(task.getPrintSeedsFile());
        assertNotNull(task.getPrintMappingFile());
        assertNotNull(task.getPrintUsageFile());
    }

    @Test
    public void testGetPrintConfigurationFile_allOutputsToStdout() throws Exception {
        task.printconfiguration();
        task.printseeds();
        task.printmapping();
        task.printusage();

        assertNull(task.getPrintConfigurationFile(), "All outputs should return null (stdout)");
        assertNull(task.getPrintSeedsFile());
        assertNull(task.getPrintMappingFile());
        assertNull(task.getPrintUsageFile());
    }

    // ============================================================
    // Annotation and Gradle Integration Tests
    // ============================================================

    @Test
    public void testGetPrintConfigurationFile_isMarkedAsOptional() throws Exception {
        // The method is annotated with @Optional, meaning it can return null
        // This test verifies that null is an acceptable return value
        File result = task.getPrintConfigurationFile();
        // No exception should be thrown, null is valid
        assertNull(result, "Method should be able to return null (marked as @Optional)");
    }

    @Test
    public void testGetPrintConfigurationFile_isMarkedAsOutputFile() throws Exception {
        // The method is annotated with @OutputFile
        // When configured with a file, it should return a File for Gradle's tracking
        task.printconfiguration("config.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "Method should return File for Gradle output tracking");
    }

    @Test
    public void testGetPrintConfigurationFile_gradleTaskDependencyTracking() throws Exception {
        // This method is used by Gradle for task dependency tracking
        // When set to a file, Gradle can track it as an output
        task.printconfiguration("build/config/proguard.txt");
        File result = task.getPrintConfigurationFile();
        assertNotNull(result, "File should be trackable by Gradle as an output");
    }

    // ============================================================
    // Semantic and Documentation Tests
    // ============================================================

    @Test
    public void testGetPrintConfigurationFile_semanticMeaning() throws Exception {
        // Verify the semantic meaning: returns the configuration output file or null
        task.printconfiguration("config.txt");
        File result = task.getPrintConfigurationFile();

        assertNotNull(result, "Method should return the configuration output file");
        assertEquals(task.configuration.printConfiguration, result,
                "Returned file should match the configured printConfiguration");
    }

    @Test
    public void testGetPrintConfigurationFile_returnsNullForNonFileOutputs() throws Exception {
        // Method should return null for stdout (special marker file)
        task.printconfiguration(); // stdout
        File result = task.getPrintConfigurationFile();

        assertNull(result, "Method should return null when output is directed to stdout");
    }

    @Test
    public void testGetPrintConfigurationFile_isReadOnlyOperation() throws Exception {
        // This is a getter method - it should not modify any state
        task.printconfiguration("config.txt");
        File originalConfig = task.configuration.printConfiguration;
        boolean originalShrink = task.configuration.shrink;
        boolean originalOptimize = task.configuration.optimize;

        task.getPrintConfigurationFile();

        assertSame(originalConfig, task.configuration.printConfiguration,
                "Configuration should not be modified");
        assertEquals(originalShrink, task.configuration.shrink,
                "Shrink setting should not be modified");
        assertEquals(originalOptimize, task.configuration.optimize,
                "Optimize setting should not be modified");
    }
}

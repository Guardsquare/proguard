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
 * Test class for ProGuardTask.getPrintUsageFile()Ljava/io/File; method.
 * Tests the getPrintUsageFile() method that returns the File for printUsage output,
 * or null if the output is set to stdout or is not configured.
 */
public class ProGuardTaskClaude_getPrintUsageFileTest {

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

    // Basic functionality tests

    @Test
    public void testGetPrintUsageFile_returnsNullInitially() throws Exception {
        assertNull(task.configuration.printUsage, "printUsage should be null initially");
        File result = task.getPrintUsageFile();
        assertNull(result, "getPrintUsageFile() should return null when printUsage is not configured");
    }

    @Test
    public void testGetPrintUsageFile_returnsNullForStdout() throws Exception {
        task.printusage();
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be stdout");
        File result = task.getPrintUsageFile();
        assertNull(result, "getPrintUsageFile() should return null for stdout");
    }

    @Test
    public void testGetPrintUsageFile_returnsFileWhenConfigured() throws Exception {
        task.printusage("usage.txt");
        File result = task.getPrintUsageFile();
        assertNotNull(result, "getPrintUsageFile() should return a File when configured with a file");
        assertEquals("usage.txt", result.getName(), "Returned file should have correct name");
    }

    @Test
    public void testGetPrintUsageFile_returnsActualFileObject() throws Exception {
        task.printusage("output/usage.txt");
        File result = task.getPrintUsageFile();
        assertNotNull(result, "getPrintUsageFile() should return a File");
        assertSame(task.configuration.printUsage, result,
                "Returned file should be the same object as configuration.printUsage");
    }

    // Interaction with printusage() void method

    @Test
    public void testGetPrintUsageFile_afterPrintusageVoid() throws Exception {
        task.printusage(); // Sets to stdout
        File result = task.getPrintUsageFile();
        assertNull(result, "getPrintUsageFile() should return null after printusage()");
    }

    @Test
    public void testGetPrintUsageFile_multipleCallsAfterPrintusageVoid() throws Exception {
        task.printusage();
        for (int i = 0; i < 3; i++) {
            File result = task.getPrintUsageFile();
            assertNull(result, "getPrintUsageFile() should consistently return null for stdout");
        }
    }

    // Interaction with printusage(Object) method

    @Test
    public void testGetPrintUsageFile_afterPrintusageWithFile() throws Exception {
        task.printusage("usage.txt");
        File result = task.getPrintUsageFile();
        assertNotNull(result, "getPrintUsageFile() should return File after printusage(Object)");
        assertEquals("usage.txt", result.getName());
    }

    @Test
    public void testGetPrintUsageFile_afterPrintusageWithDifferentFiles() throws Exception {
        task.printusage("usage1.txt");
        File result1 = task.getPrintUsageFile();
        assertEquals("usage1.txt", result1.getName());

        task.printusage("usage2.txt");
        File result2 = task.getPrintUsageFile();
        assertEquals("usage2.txt", result2.getName());
    }

    @Test
    public void testGetPrintUsageFile_switchingFromFileToStdout() throws Exception {
        task.printusage("usage.txt");
        File result1 = task.getPrintUsageFile();
        assertNotNull(result1, "Should return File initially");

        task.printusage(); // Switch to stdout
        File result2 = task.getPrintUsageFile();
        assertNull(result2, "Should return null after switching to stdout");
    }

    @Test
    public void testGetPrintUsageFile_switchingFromStdoutToFile() throws Exception {
        task.printusage(); // stdout
        File result1 = task.getPrintUsageFile();
        assertNull(result1, "Should return null for stdout");

        task.printusage("usage.txt"); // Switch to file
        File result2 = task.getPrintUsageFile();
        assertNotNull(result2, "Should return File after switching to file");
        assertEquals("usage.txt", result2.getName());
    }

    // Multiple calls to getPrintUsageFile()

    @Test
    public void testGetPrintUsageFile_multipleCallsReturnSameResult() throws Exception {
        task.printusage("usage.txt");
        File result1 = task.getPrintUsageFile();
        File result2 = task.getPrintUsageFile();
        File result3 = task.getPrintUsageFile();

        assertSame(result1, result2, "Multiple calls should return same File object");
        assertSame(result2, result3, "Multiple calls should return same File object");
    }

    @Test
    public void testGetPrintUsageFile_idempotentForNull() throws Exception {
        // No configuration
        File result1 = task.getPrintUsageFile();
        File result2 = task.getPrintUsageFile();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
    }

    @Test
    public void testGetPrintUsageFile_idempotentForStdout() throws Exception {
        task.printusage();
        File result1 = task.getPrintUsageFile();
        File result2 = task.getPrintUsageFile();
        File result3 = task.getPrintUsageFile();

        assertNull(result1, "All calls should return null for stdout");
        assertNull(result2, "All calls should return null for stdout");
        assertNull(result3, "All calls should return null for stdout");
    }

    // Integration with other configuration methods

    @Test
    public void testGetPrintUsageFile_withOtherPrintMethods() throws Exception {
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");
        task.printmapping("mapping.txt");

        File result = task.getPrintUsageFile();
        assertNotNull(result, "getPrintUsageFile() should return File");
        assertEquals("usage.txt", result.getName());
    }

    @Test
    public void testGetPrintUsageFile_withDontShrink() throws Exception {
        task.dontshrink();
        task.printusage("usage.txt");
        File result = task.getPrintUsageFile();
        assertNotNull(result, "getPrintUsageFile() should work with dontshrink()");
        assertEquals("usage.txt", result.getName());
    }

    @Test
    public void testGetPrintUsageFile_withKeepRules() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.printusage("usage.txt");
        File result = task.getPrintUsageFile();
        assertNotNull(result, "getPrintUsageFile() should work with keep rules");
        assertEquals("usage.txt", result.getName());
    }

    @Test
    public void testGetPrintUsageFile_withFullConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.printusage("usage.txt");
        task.keep("public class * { public *; }");

        File result = task.getPrintUsageFile();
        assertNotNull(result, "getPrintUsageFile() should work in full configuration");
        assertEquals("usage.txt", result.getName());
    }

    // File path variations

    @Test
    public void testGetPrintUsageFile_withNestedPath() throws Exception {
        task.printusage("build/reports/proguard/usage.txt");
        File result = task.getPrintUsageFile();
        assertNotNull(result, "getPrintUsageFile() should work with nested paths");
        assertEquals("usage.txt", result.getName());
    }

    @Test
    public void testGetPrintUsageFile_withDifferentExtensions() throws Exception {
        String[] filenames = {"usage.txt", "usage.log", "usage.out", "usage"};
        for (String filename : filenames) {
            ProGuardTask freshTask = project.getTasks().create("proguard_" + filename.replace(".", "_"), ProGuardTask.class);
            freshTask.printusage(filename);
            File result = freshTask.getPrintUsageFile();
            assertNotNull(result, "getPrintUsageFile() should work with " + filename);
            assertEquals(filename, result.getName());
        }
    }

    @Test
    public void testGetPrintUsageFile_withFileObject() throws Exception {
        File usageFile = new File(tempDir.toFile(), "usage.txt");
        task.printusage(usageFile);
        File result = task.getPrintUsageFile();
        assertNotNull(result, "getPrintUsageFile() should work when configured with File object");
        assertEquals("usage.txt", result.getName());
    }

    // Realistic scenarios

    @Test
    public void testGetPrintUsageFile_androidBuildScenario() throws Exception {
        // Android build with file output
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.printusage("build/outputs/proguard/usage.txt");

        File result = task.getPrintUsageFile();
        assertNotNull(result, "getPrintUsageFile() should return file for Android build");
        assertEquals("usage.txt", result.getName());
    }

    @Test
    public void testGetPrintUsageFile_cicdWithStdout() throws Exception {
        // CI/CD scenario with stdout
        task.printusage(); // stdout for CI/CD logging
        task.printseeds(); // stdout
        task.printmapping(); // stdout

        File result = task.getPrintUsageFile();
        assertNull(result, "getPrintUsageFile() should return null for CI/CD stdout scenario");
    }

    @Test
    public void testGetPrintUsageFile_releaseConfiguration() throws Exception {
        // Release build with file archiving
        task.printusage("release/reports/usage.txt");
        task.printseeds("release/reports/seeds.txt");
        task.printmapping("release/reports/mapping.txt");

        File result = task.getPrintUsageFile();
        assertNotNull(result, "getPrintUsageFile() should return file for release build");
        assertEquals("usage.txt", result.getName());
    }

    @Test
    public void testGetPrintUsageFile_developmentVsProduction() throws Exception {
        // Development: stdout
        ProGuardTask devTask = project.getTasks().create("proguardDev", ProGuardTask.class);
        devTask.printusage();
        File devResult = devTask.getPrintUsageFile();
        assertNull(devResult, "Development mode should return null (stdout)");

        // Production: file
        ProGuardTask prodTask = project.getTasks().create("proguardProd", ProGuardTask.class);
        prodTask.printusage("production/usage.txt");
        File prodResult = prodTask.getPrintUsageFile();
        assertNotNull(prodResult, "Production mode should return File");
        assertEquals("usage.txt", prodResult.getName());
    }

    // Edge cases and validation

    @Test
    public void testGetPrintUsageFile_doesNotModifyConfiguration() throws Exception {
        task.printusage("usage.txt");
        File original = task.configuration.printUsage;

        task.getPrintUsageFile();

        assertSame(original, task.configuration.printUsage,
                "getPrintUsageFile() should not modify configuration.printUsage");
    }

    @Test
    public void testGetPrintUsageFile_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;

        task.printusage("usage.txt");
        task.getPrintUsageFile();

        assertEquals(initialShrink, task.configuration.shrink,
                "getPrintUsageFile() should not affect shrink setting");
        assertEquals(initialOptimize, task.configuration.optimize,
                "getPrintUsageFile() should not affect optimize setting");
    }

    @Test
    public void testGetPrintUsageFile_returnValueNotStdOut() throws Exception {
        task.printusage("usage.txt");
        File result = task.getPrintUsageFile();
        assertNotEquals(Configuration.STD_OUT, result,
                "Returned file should not be Configuration.STD_OUT");
    }

    @Test
    public void testGetPrintUsageFile_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);

        // Test null case
        File result1 = freshTask.getPrintUsageFile();
        assertNull(result1, "Fresh task should return null initially");

        // Test file case
        freshTask.printusage("fresh-usage.txt");
        File result2 = freshTask.getPrintUsageFile();
        assertNotNull(result2, "Fresh task should return File after configuration");
        assertEquals("fresh-usage.txt", result2.getName());
    }

    // Call order tests

    @Test
    public void testGetPrintUsageFile_canBeCalledBeforeConfiguration() throws Exception {
        File result = task.getPrintUsageFile();
        assertNull(result, "getPrintUsageFile() should return null before configuration");
    }

    @Test
    public void testGetPrintUsageFile_canBeCalledMultipleTimesDuringConfiguration() throws Exception {
        File result1 = task.getPrintUsageFile();
        assertNull(result1);

        task.printusage("usage1.txt");
        File result2 = task.getPrintUsageFile();
        assertEquals("usage1.txt", result2.getName());

        task.printusage();
        File result3 = task.getPrintUsageFile();
        assertNull(result3);

        task.printusage("usage2.txt");
        File result4 = task.getPrintUsageFile();
        assertEquals("usage2.txt", result4.getName());
    }

    @Test
    public void testGetPrintUsageFile_reflectsLatestConfiguration() throws Exception {
        task.printusage("first.txt");
        assertEquals("first.txt", task.getPrintUsageFile().getName());

        task.printusage("second.txt");
        assertEquals("second.txt", task.getPrintUsageFile().getName());

        task.printusage("third.txt");
        assertEquals("third.txt", task.getPrintUsageFile().getName());
    }

    // Interaction with other output file getters

    @Test
    public void testGetPrintUsageFile_independentOfOtherFileGetters() throws Exception {
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");
        task.printmapping("mapping.txt");

        File usageFile = task.getPrintUsageFile();
        File seedsFile = task.getPrintSeedsFile();
        File mappingFile = task.getPrintMappingFile();

        assertEquals("usage.txt", usageFile.getName());
        assertEquals("seeds.txt", seedsFile.getName());
        assertEquals("mapping.txt", mappingFile.getName());

        // Each getter should return its own file
        assertNotSame(usageFile, seedsFile);
        assertNotSame(usageFile, mappingFile);
    }

    @Test
    public void testGetPrintUsageFile_withMixedOutputTargets() throws Exception {
        task.printusage("usage.txt"); // file
        task.printseeds(); // stdout
        task.printmapping("mapping.txt"); // file

        File usageFile = task.getPrintUsageFile();
        File seedsFile = task.getPrintSeedsFile();
        File mappingFile = task.getPrintMappingFile();

        assertNotNull(usageFile, "usage should return File");
        assertNull(seedsFile, "seeds should return null (stdout)");
        assertNotNull(mappingFile, "mapping should return File");

        assertEquals("usage.txt", usageFile.getName());
        assertEquals("mapping.txt", mappingFile.getName());
    }
}

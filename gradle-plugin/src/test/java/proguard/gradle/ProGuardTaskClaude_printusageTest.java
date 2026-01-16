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
 * Test class for ProGuardTask.printusage()V and printusage(Object)V methods.
 * Tests the void printusage() method that sets configuration.printUsage to Configuration.STD_OUT,
 * and the printusage(Object) method that sets configuration.printUsage to a file.
 */
public class ProGuardTaskClaude_printusageTest {

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
    public void testPrintusage_setsPrintUsageToStdOut() throws Exception {
        assertNull(task.configuration.printUsage, "printUsage should initially be null");
        task.printusage();
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be set to Configuration.STD_OUT");
    }

    @Test
    public void testPrintusage_doesNotReturnValue() throws Exception {
        task.printusage();
        // This test verifies the method is void and completes without exception
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be set to Configuration.STD_OUT");
    }

    @Test
    public void testPrintusage_setsStdOutMarker() throws Exception {
        task.printusage();
        assertNotNull(task.configuration.printUsage, "printUsage should not be null after calling printusage()");
        assertEquals("", task.configuration.printUsage.getPath(),
                "Configuration.STD_OUT should have empty path");
    }

    // Multiple calls and idempotency tests

    @Test
    public void testPrintusage_multipleCallsAreIdempotent() throws Exception {
        task.printusage();
        File firstCall = task.configuration.printUsage;
        task.printusage();
        File secondCall = task.configuration.printUsage;
        assertSame(Configuration.STD_OUT, firstCall, "First call should set STD_OUT");
        assertSame(Configuration.STD_OUT, secondCall, "Second call should set STD_OUT");
        assertSame(firstCall, secondCall, "Multiple calls should produce same result");
    }

    @Test
    public void testPrintusage_canBeCalledMultipleTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.printusage();
            assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                    "printUsage should be STD_OUT after call " + (i + 1));
        }
    }

    @Test
    public void testPrintusage_overwritesNullValue() throws Exception {
        assertNull(task.configuration.printUsage, "printUsage should start as null");
        task.printusage();
        assertNotNull(task.configuration.printUsage, "printUsage should not be null after printusage()");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be Configuration.STD_OUT");
    }

    // Integration with other configuration methods

    @Test
    public void testPrintusage_worksWithOtherPrintOptions() throws Exception {
        task.printseeds();
        task.printusage();
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should remain as STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
    }

    @Test
    public void testPrintusage_worksWithDontShrink() throws Exception {
        task.dontshrink();
        task.printusage();
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
    }

    @Test
    public void testPrintusage_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.printusage();
        assertFalse(task.configuration.optimize, "optimize should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
    }

    @Test
    public void testPrintusage_worksWithDontObfuscate() throws Exception {
        task.dontobfuscate();
        task.printusage();
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
    }

    @Test
    public void testPrintusage_worksWithMultiplePrintOutputs() throws Exception {
        task.printseeds();
        task.printusage();
        task.printmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should be STD_OUT");
    }

    // Call order flexibility tests

    @Test
    public void testPrintusage_canBeCalledBeforeOtherMethods() throws Exception {
        task.printusage();
        task.dontshrink();
        task.printseeds();
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
    }

    @Test
    public void testPrintusage_canBeCalledAfterOtherMethods() throws Exception {
        task.dontshrink();
        task.printseeds();
        task.printusage();
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
    }

    @Test
    public void testPrintusage_canBeCalledBetweenOtherMethods() throws Exception {
        task.dontshrink();
        task.printusage();
        task.printseeds();
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
    }

    // Realistic usage scenarios

    @Test
    public void testPrintusage_androidAppScenario() throws Exception {
        // Typical Android app configuration with usage reporting
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.printusage();
        task.keep("class com.example.MainActivity { *; }");

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT for Android scenario");
    }

    @Test
    public void testPrintusage_javaLibraryScenario() throws Exception {
        // Java library scenario with usage output
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-processed.jar");
        task.printusage();
        task.keep("public class * { public *; }");

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT for Java library scenario");
    }

    @Test
    public void testPrintusage_withOnlyReportingOptions() throws Exception {
        // Configuration focused only on reporting without processing
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.printusage();
        task.printseeds();

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
    }

    @Test
    public void testPrintusage_cicdScenario() throws Exception {
        // CI/CD scenario where all output goes to stdout for log capture
        task.printusage();
        task.printseeds();
        task.printmapping();
        task.printconfiguration();

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should output to stdout for CI/CD");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should output to stdout for CI/CD");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should output to stdout for CI/CD");
    }

    @Test
    public void testPrintusage_debuggingScenario() throws Exception {
        // Debugging scenario with verbose output
        task.printusage();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT for debugging");
        assertTrue(task.configuration.verbose, "verbose should be enabled");
    }

    // Edge cases and validation

    @Test
    public void testPrintusage_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.printusage();
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testPrintusage_doesNotAffectOtherConfigurationFields() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.printusage();

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testPrintusage_stdOutIsNotNull() throws Exception {
        task.printusage();
        assertNotNull(task.configuration.printUsage,
                "Configuration.STD_OUT should not be null");
    }

    @Test
    public void testPrintusage_stdOutIsSpecialFile() throws Exception {
        task.printusage();
        assertEquals("", task.configuration.printUsage.getPath(),
                "Configuration.STD_OUT should have empty path as marker");
    }

    @Test
    public void testPrintusage_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        freshTask.printusage();
        assertEquals(Configuration.STD_OUT, freshTask.configuration.printUsage,
                "printUsage should work on fresh task");
    }

    // Integration with keep rules

    @Test
    public void testPrintusage_worksWithKeepRules() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.printusage();
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should work with keep rules");
    }

    @Test
    public void testPrintusage_worksWithKeepClassMembers() throws Exception {
        task.keepclassmembers("class * { public *; }");
        task.printusage();
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should work with keepclassmembers");
    }

    @Test
    public void testPrintusage_worksWithDontWarn() throws Exception {
        task.dontwarn("com.example.**");
        task.printusage();
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should work with dontwarn");
    }

    // Interaction with file-based print methods

    @Test
    public void testPrintusage_afterPrintseedsWithFile() throws Exception {
        task.printseeds("seeds.txt");
        task.printusage();
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should remain as file");
    }

    @Test
    public void testPrintusage_beforePrintseedsWithFile() throws Exception {
        task.printusage();
        task.printseeds("seeds.txt");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should be file");
    }

    @Test
    public void testPrintusage_withMixedOutputTargets() throws Exception {
        task.printusage(); // stdout
        task.printseeds("seeds.txt"); // file
        task.printmapping(); // stdout

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should be file");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should be STD_OUT");
    }

    // Advanced scenarios

    @Test
    public void testPrintusage_fullProcessingPipeline() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.printusage();
        task.printseeds();
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT in full pipeline");
    }

    @Test
    public void testPrintusage_noProcessingScenario() throws Exception {
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.dontpreverify();
        task.printusage();

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should work even with all processing disabled");
    }

    @Test
    public void testPrintusage_minimalConfiguration() throws Exception {
        task.printusage();
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should work with minimal configuration");
    }

    // ========================================
    // Tests for printusage(Object) method
    // ========================================

    // Basic functionality tests for printusage(Object)

    @Test
    public void testPrintusageWithFile_setsPrintUsageToFile() throws Exception {
        String usageFile = "usage.txt";
        task.printusage(usageFile);
        assertNotNull(task.configuration.printUsage, "printUsage should not be null");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should have correct filename");
    }

    @Test
    public void testPrintusageWithFile_acceptsStringPath() throws Exception {
        task.printusage("output/usage.txt");
        assertNotNull(task.configuration.printUsage, "printUsage should not be null");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should have correct filename");
    }

    @Test
    public void testPrintusageWithFile_acceptsFileObject() throws Exception {
        File usageFile = new File(tempDir.toFile(), "usage.txt");
        task.printusage(usageFile);
        assertNotNull(task.configuration.printUsage, "printUsage should not be null");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should have correct filename");
    }

    @Test
    public void testPrintusageWithFile_overwritesPreviousValue() throws Exception {
        task.printusage("first.txt");
        assertEquals("first.txt", task.configuration.printUsage.getName(),
                "printUsage should be first.txt");
        task.printusage("second.txt");
        assertEquals("second.txt", task.configuration.printUsage.getName(),
                "printUsage should be overwritten to second.txt");
    }

    @Test
    public void testPrintusageWithFile_overwritesStdOut() throws Exception {
        task.printusage(); // Set to stdout
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
        task.printusage("usage.txt");
        assertNotEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should no longer be STD_OUT");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should be usage.txt");
    }

    // File path variations

    @Test
    public void testPrintusageWithFile_relativePathInBuildDir() throws Exception {
        task.printusage("build/reports/usage.txt");
        assertNotNull(task.configuration.printUsage, "printUsage should not be null");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should have correct filename");
    }

    @Test
    public void testPrintusageWithFile_nestedDirectoryPath() throws Exception {
        task.printusage("output/proguard/reports/usage.txt");
        assertNotNull(task.configuration.printUsage, "printUsage should not be null");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should have correct filename");
    }

    @Test
    public void testPrintusageWithFile_withDifferentExtensions() throws Exception {
        String[] extensions = {"usage.txt", "usage.log", "usage.out", "usage"};
        for (String filename : extensions) {
            ProGuardTask freshTask = project.getTasks().create("proguard_" + filename.replace(".", "_"), ProGuardTask.class);
            freshTask.printusage(filename);
            assertNotNull(freshTask.configuration.printUsage,
                    "printUsage should not be null for " + filename);
        }
    }

    // Integration with other methods

    @Test
    public void testPrintusageWithFile_afterPrintusageVoid() throws Exception {
        task.printusage(); // stdout
        task.printusage("usage.txt"); // file
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should be usage.txt, overwriting stdout");
    }

    @Test
    public void testPrintusageWithFile_beforePrintusageVoid() throws Exception {
        task.printusage("usage.txt"); // file
        task.printusage(); // stdout
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT, overwriting file");
    }

    @Test
    public void testPrintusageWithFile_worksWithOtherPrintMethods() throws Exception {
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");
        task.printmapping("mapping.txt");

        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should be seeds.txt");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should be usage.txt");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should be mapping.txt");
    }

    @Test
    public void testPrintusageWithFile_worksWithDontShrink() throws Exception {
        task.dontshrink();
        task.printusage("usage.txt");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should be usage.txt");
    }

    @Test
    public void testPrintusageWithFile_worksWithKeepRules() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.printusage("usage.txt");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should be usage.txt");
    }

    // Multiple calls and updates

    @Test
    public void testPrintusageWithFile_multipleCallsUpdateValue() throws Exception {
        task.printusage("usage1.txt");
        assertEquals("usage1.txt", task.configuration.printUsage.getName(),
                "printUsage should be usage1.txt");
        task.printusage("usage2.txt");
        assertEquals("usage2.txt", task.configuration.printUsage.getName(),
                "printUsage should be updated to usage2.txt");
        task.printusage("usage3.txt");
        assertEquals("usage3.txt", task.configuration.printUsage.getName(),
                "printUsage should be updated to usage3.txt");
    }

    @Test
    public void testPrintusageWithFile_alternatingWithVoidMethod() throws Exception {
        task.printusage("usage.txt");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should be usage.txt");
        task.printusage(); // stdout
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
        task.printusage("usage2.txt");
        assertEquals("usage2.txt", task.configuration.printUsage.getName(),
                "printUsage should be usage2.txt");
    }

    // Realistic scenarios with printusage(Object)

    @Test
    public void testPrintusageWithFile_androidBuildScenario() throws Exception {
        // Android build writing usage to file
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.printusage("build/outputs/proguard/usage.txt");
        task.printseeds("build/outputs/proguard/seeds.txt");
        task.printmapping("build/outputs/proguard/mapping.txt");

        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should be usage.txt for Android build");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should be seeds.txt");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should be mapping.txt");
    }

    @Test
    public void testPrintusageWithFile_releaseConfigurationScenario() throws Exception {
        // Release build configuration with file outputs
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");
        task.printusage("release/usage.txt");
        task.printseeds("release/seeds.txt");
        task.printmapping("release/mapping.txt");
        task.keep("public class * { public *; }");

        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should be usage.txt for release build");
    }

    @Test
    public void testPrintusageWithFile_archivingScenario() throws Exception {
        // Archiving all reports to specific directory
        String reportDir = "archive/proguard-reports";
        task.printusage(reportDir + "/usage.txt");
        task.printseeds(reportDir + "/seeds.txt");
        task.printmapping(reportDir + "/mapping.txt");

        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should be in archive directory");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
                "printSeeds should be in archive directory");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should be in archive directory");
    }

    @Test
    public void testPrintusageWithFile_developmentVsProduction() throws Exception {
        // Development mode: stdout
        ProGuardTask devTask = project.getTasks().create("proguardDev", ProGuardTask.class);
        devTask.printusage();
        assertEquals(Configuration.STD_OUT, devTask.configuration.printUsage,
                "Development should use stdout");

        // Production mode: file
        ProGuardTask prodTask = project.getTasks().create("proguardProd", ProGuardTask.class);
        prodTask.printusage("production/usage.txt");
        assertEquals("usage.txt", prodTask.configuration.printUsage.getName(),
                "Production should use file");
    }

    // Edge cases and validation

    @Test
    public void testPrintusageWithFile_doesNotAffectOtherConfiguration() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.printusage("usage.txt");

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testPrintusageWithFile_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.printusage("usage.txt");
        assertNotNull(task.configuration, "configuration should remain not null");
        assertNotNull(task.configuration.printUsage, "printUsage should not be null");
    }

    @Test
    public void testPrintusageWithFile_differentFromStdOut() throws Exception {
        task.printusage("usage.txt");
        assertNotEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "File output should not equal STD_OUT");
        assertNotEquals("", task.configuration.printUsage.getPath(),
                "File output should not have empty path like STD_OUT");
    }

    @Test
    public void testPrintusageWithFile_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard3", ProGuardTask.class);
        freshTask.printusage("fresh-usage.txt");
        assertEquals("fresh-usage.txt", freshTask.configuration.printUsage.getName(),
                "printUsage should work on fresh task");
    }

    // Call order flexibility with printusage(Object)

    @Test
    public void testPrintusageWithFile_beforeOtherConfiguration() throws Exception {
        task.printusage("usage.txt");
        task.injars("input.jar");
        task.outjars("output.jar");
        task.keep("public class * { public *; }");

        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should work when called first");
    }

    @Test
    public void testPrintusageWithFile_afterOtherConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.keep("public class * { public *; }");
        task.printusage("usage.txt");

        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should work when called last");
    }

    @Test
    public void testPrintusageWithFile_betweenOtherConfiguration() throws Exception {
        task.injars("input.jar");
        task.printusage("usage.txt");
        task.outjars("output.jar");

        assertEquals("usage.txt", task.configuration.printUsage.getName(),
                "printUsage should work when called between other methods");
    }
}

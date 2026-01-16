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
 * Test class for ProGuardTask.printconfiguration()V and printconfiguration(Object)V methods.
 * Tests the void printconfiguration() method that sets configuration.printConfiguration to Configuration.STD_OUT,
 * and the printconfiguration(Object) method that sets configuration.printConfiguration to a file.
 */
public class ProGuardTaskClaude_printconfigurationTest {

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
    // Basic Functionality Tests - No Argument Version
    // ============================================================

    @Test
    public void testPrintconfiguration_setsPrintConfigurationToStdOut() throws Exception {
        assertNull(task.configuration.printConfiguration, "printConfiguration should initially be null");
        task.printconfiguration();
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be set to Configuration.STD_OUT");
    }

    @Test
    public void testPrintconfiguration_doesNotReturnValue() throws Exception {
        task.printconfiguration();
        // This test verifies the method is void and completes without exception
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be set to Configuration.STD_OUT");
    }

    @Test
    public void testPrintconfiguration_setsStdOutMarker() throws Exception {
        task.printconfiguration();
        assertNotNull(task.configuration.printConfiguration, "printConfiguration should not be null after calling printconfiguration()");
        assertEquals("", task.configuration.printConfiguration.getPath(),
                "Configuration.STD_OUT should have empty path");
    }

    // ============================================================
    // Multiple Calls and Idempotency Tests
    // ============================================================

    @Test
    public void testPrintconfiguration_multipleCallsAreIdempotent() throws Exception {
        task.printconfiguration();
        File firstCall = task.configuration.printConfiguration;
        task.printconfiguration();
        File secondCall = task.configuration.printConfiguration;
        assertSame(Configuration.STD_OUT, firstCall, "First call should set STD_OUT");
        assertSame(Configuration.STD_OUT, secondCall, "Second call should set STD_OUT");
        assertSame(firstCall, secondCall, "Multiple calls should produce same result");
    }

    @Test
    public void testPrintconfiguration_canBeCalledMultipleTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.printconfiguration();
            assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                    "printConfiguration should be STD_OUT after call " + (i + 1));
        }
    }

    @Test
    public void testPrintconfiguration_overwritesNullValue() throws Exception {
        assertNull(task.configuration.printConfiguration, "printConfiguration should start as null");
        task.printconfiguration();
        assertNotNull(task.configuration.printConfiguration, "printConfiguration should not be null after printconfiguration()");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be Configuration.STD_OUT");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testPrintconfiguration_worksWithOtherPrintOptions() throws Exception {
        task.printseeds();
        task.printconfiguration();
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should remain as STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_worksWithDontShrink() throws Exception {
        task.dontshrink();
        task.printconfiguration();
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.printconfiguration();
        assertFalse(task.configuration.optimize, "optimize should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_worksWithDontObfuscate() throws Exception {
        task.dontobfuscate();
        task.printconfiguration();
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_worksWithMultiplePrintOutputs() throws Exception {
        task.printseeds();
        task.printconfiguration();
        task.printmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_doesNotAffectVerboseSetting() throws Exception {
        task.verbose();
        task.printconfiguration();
        assertTrue(task.configuration.verbose, "verbose should remain true");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_doesNotAffectIgnoreWarningsSetting() throws Exception {
        task.ignorewarnings();
        task.printconfiguration();
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should remain true");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    // ============================================================
    // File Output Tests - With Argument Version
    // ============================================================

    @Test
    public void testPrintconfiguration_withFile_setsConfigurationToFile() throws Exception {
        File outputFile = new File(tempDir.toFile(), "proguard-config.txt");
        task.printconfiguration(outputFile);

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should not be null");
        assertNotSame(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should not be STD_OUT when file is specified");
        assertEquals(outputFile, task.configuration.printConfiguration,
                "printConfiguration should be the specified file");
    }

    @Test
    public void testPrintconfiguration_withString_setsConfigurationToFile() throws Exception {
        String filePath = new File(tempDir.toFile(), "config-output.txt").getAbsolutePath();
        task.printconfiguration(filePath);

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should not be null");
        assertNotSame(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should not be STD_OUT when string path is specified");
    }

    @Test
    public void testPrintconfiguration_withFile_overridesStdOut() throws Exception {
        task.printconfiguration();
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should initially be STD_OUT");

        File outputFile = new File(tempDir.toFile(), "override-config.txt");
        task.printconfiguration(outputFile);

        assertNotSame(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should no longer be STD_OUT");
        assertEquals(outputFile, task.configuration.printConfiguration,
                "printConfiguration should be the new file");
    }

    @Test
    public void testPrintconfiguration_withDifferentFiles_updatesConfiguration() throws Exception {
        File file1 = new File(tempDir.toFile(), "config1.txt");
        task.printconfiguration(file1);
        assertEquals(file1, task.configuration.printConfiguration,
                "printConfiguration should be file1");

        File file2 = new File(tempDir.toFile(), "config2.txt");
        task.printconfiguration(file2);
        assertEquals(file2, task.configuration.printConfiguration,
                "printConfiguration should be updated to file2");
    }

    @Test
    public void testPrintconfiguration_withRelativePath() throws Exception {
        String relativePath = "output/proguard-config.txt";
        task.printconfiguration(relativePath);

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should not be null");
        assertNotSame(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should not be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_withAbsolutePath() throws Exception {
        File absoluteFile = new File(tempDir.toFile(), "absolute-config.txt").getAbsoluteFile();
        task.printconfiguration(absoluteFile);

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should not be null");
        assertEquals(absoluteFile, task.configuration.printConfiguration,
                "printConfiguration should match the absolute file");
    }

    // ============================================================
    // Call Order Tests
    // ============================================================

    @Test
    public void testPrintconfiguration_beforeOtherConfig() throws Exception {
        task.printconfiguration();
        task.verbose();
        task.ignorewarnings();

        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testPrintconfiguration_afterOtherConfig() throws Exception {
        task.verbose();
        task.ignorewarnings();
        task.printconfiguration();

        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testPrintconfiguration_betweenOtherConfig() throws Exception {
        task.verbose();
        task.printconfiguration();
        task.ignorewarnings();

        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ============================================================
    // Android and Platform Tests
    // ============================================================

    @Test
    public void testPrintconfiguration_withAndroidConfig() throws Exception {
        task.android();
        task.printconfiguration();

        assertTrue(task.configuration.android, "android should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_withMicroEdition() throws Exception {
        task.microedition();
        task.printconfiguration();

        assertTrue(task.configuration.microEdition, "microEdition should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    // ============================================================
    // Keep Rules and Optimization Tests
    // ============================================================

    @Test
    public void testPrintconfiguration_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.printconfiguration();

        assertNotNull(task.configuration.keep, "keep rules should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_withOptimizations() throws Exception {
        task.optimizations("!code/simplification/arithmetic,!code/simplification/cast");
        task.printconfiguration();

        assertNotNull(task.configuration.optimizations, "optimizations should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_withOptimizationPasses() throws Exception {
        task.optimizationpasses(3);
        task.printconfiguration();

        assertEquals(3, task.configuration.optimizationPasses, "optimizationPasses should be 3");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    // ============================================================
    // Obfuscation Settings Tests
    // ============================================================

    @Test
    public void testPrintconfiguration_withKeepAttributes() throws Exception {
        task.keepattributes("Exceptions,InnerClasses");
        task.printconfiguration();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_withKeepPackageNames() throws Exception {
        task.keeppackagenames("com.example.**");
        task.printconfiguration();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_withRepackageClasses() throws Exception {
        task.repackageclasses("com.example.repackaged");
        task.printconfiguration();

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    // ============================================================
    // Realistic Build Scenarios
    // ============================================================

    @Test
    public void testPrintconfiguration_forDebugBuild() throws Exception {
        // Debug build with configuration output for troubleshooting
        task.dontobfuscate();
        task.verbose();
        task.printconfiguration();

        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
        assertTrue(task.configuration.verbose, "verbose should be enabled");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should output to console");
    }

    @Test
    public void testPrintconfiguration_forReleaseBuild() throws Exception {
        // Release build saving configuration to file
        File releaseConfig = new File(tempDir.toFile(), "release-config.txt");
        task.printconfiguration(releaseConfig);
        task.verbose();

        assertEquals(releaseConfig, task.configuration.printConfiguration,
                "printConfiguration should be saved to file for release");
        assertTrue(task.configuration.verbose, "verbose should be enabled");
    }

    @Test
    public void testPrintconfiguration_forCiBuild() throws Exception {
        // CI build printing configuration to stdout for logs
        task.printconfiguration();
        task.verbose();
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should output to console for CI");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should output to console for CI");
        assertTrue(task.configuration.verbose, "verbose should be enabled");
    }

    @Test
    public void testPrintconfiguration_forAuditPurposes() throws Exception {
        // Audit configuration by saving to a file
        File auditFile = new File(tempDir.toFile(), "audit-config.txt");
        task.keep("public class * { *; }");
        task.printconfiguration(auditFile);

        assertEquals(auditFile, task.configuration.printConfiguration,
                "Configuration should be saved for audit");
        assertNotNull(task.configuration.keep, "keep rules should be present");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testPrintconfiguration_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.printconfiguration();
        File task2File = new File(tempDir.toFile(), "task2-config.txt");
        task2.printconfiguration(task2File);

        assertEquals(Configuration.STD_OUT, task1.configuration.printConfiguration,
                "task1 should output to stdout");
        assertEquals(task2File, task2.configuration.printConfiguration,
                "task2 should output to file");
    }

    @Test
    public void testPrintconfiguration_withComplexConfiguration() throws Exception {
        // Complex configuration with many settings
        task.keep("public class * { *; }");
        task.keepclassmembers("class * { public <methods>; }");
        task.dontoptimize();
        task.verbose();
        task.printconfiguration();

        assertNotNull(task.configuration.keep, "keep rules should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.verbose, "verbose should be enabled");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_withWarningFilters() throws Exception {
        task.dontwarn("org.slf4j.**");
        task.ignorewarnings();
        task.printconfiguration();

        assertNotNull(task.configuration.warn, "warn filter should be set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_switchingBetweenStdOutAndFile() throws Exception {
        // Start with stdout
        task.printconfiguration();
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "Should start with stdout");

        // Switch to file
        File file = new File(tempDir.toFile(), "config.txt");
        task.printconfiguration(file);
        assertEquals(file, task.configuration.printConfiguration,
                "Should switch to file");

        // Switch back to stdout
        task.printconfiguration();
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "Should switch back to stdout");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testPrintconfiguration_preservesOtherSettings() throws Exception {
        task.verbose();
        task.ignorewarnings();
        task.dontobfuscate();

        task.printconfiguration();

        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be preserved");
        assertFalse(task.configuration.obfuscate, "obfuscate should be preserved");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be set");
    }

    @Test
    public void testPrintconfiguration_doesNotAffectShrinkSetting() throws Exception {
        task.printconfiguration();

        assertTrue(task.configuration.shrink, "shrink should remain at default true");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_doesNotAffectOptimizeSetting() throws Exception {
        task.printconfiguration();

        assertTrue(task.configuration.optimize, "optimize should remain at default true");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_doesNotAffectObfuscateSetting() throws Exception {
        task.printconfiguration();

        assertTrue(task.configuration.obfuscate, "obfuscate should remain at default true");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    // ============================================================
    // Interaction with Other Print Options
    // ============================================================

    @Test
    public void testPrintconfiguration_withPrintMapping() throws Exception {
        File mappingFile = new File(tempDir.toFile(), "mapping.txt");
        task.printmapping(mappingFile);
        task.printconfiguration();

        assertEquals(mappingFile, task.configuration.printMapping,
                "printMapping should be preserved");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_withPrintSeeds() throws Exception {
        File seedsFile = new File(tempDir.toFile(), "seeds.txt");
        task.printseeds(seedsFile);
        task.printconfiguration();

        assertEquals(seedsFile, task.configuration.printSeeds,
                "printSeeds should be preserved");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_withPrintUsage() throws Exception {
        File usageFile = new File(tempDir.toFile(), "usage.txt");
        task.printusage(usageFile);
        task.printconfiguration();

        assertEquals(usageFile, task.configuration.printUsage,
                "printUsage should be preserved");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_allPrintOptionsToStdOut() throws Exception {
        task.printconfiguration();
        task.printmapping();
        task.printseeds();
        task.printusage();

        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printConfiguration should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT");
    }

    @Test
    public void testPrintconfiguration_allPrintOptionsToFiles() throws Exception {
        File configFile = new File(tempDir.toFile(), "config.txt");
        File mappingFile = new File(tempDir.toFile(), "mapping.txt");
        File seedsFile = new File(tempDir.toFile(), "seeds.txt");
        File usageFile = new File(tempDir.toFile(), "usage.txt");

        task.printconfiguration(configFile);
        task.printmapping(mappingFile);
        task.printseeds(seedsFile);
        task.printusage(usageFile);

        assertEquals(configFile, task.configuration.printConfiguration,
                "printConfiguration should be configFile");
        assertEquals(mappingFile, task.configuration.printMapping,
                "printMapping should be mappingFile");
        assertEquals(seedsFile, task.configuration.printSeeds,
                "printSeeds should be seedsFile");
        assertEquals(usageFile, task.configuration.printUsage,
                "printUsage should be usageFile");
    }

    // ============================================================
    // Verification Tests
    // ============================================================

    @Test
    public void testPrintconfiguration_configurationNotNull() throws Exception {
        task.printconfiguration();

        assertNotNull(task.configuration, "Configuration should never be null");
        assertNotNull(task.configuration.printConfiguration,
                "printConfiguration should be set");
    }

    @Test
    public void testPrintconfiguration_taskStateValid() throws Exception {
        task.printconfiguration();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testPrintconfiguration_semanticMeaning() throws Exception {
        // Verify the semantic meaning: outputs resolved configuration
        task.printconfiguration();

        assertNotNull(task.configuration.printConfiguration,
                "printconfiguration should enable printing of resolved configuration");
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "printconfiguration should output to standard output by default");
    }

    // ============================================================
    // Comparison with Getter Method
    // ============================================================

    @Test
    public void testPrintconfiguration_equivalentToGetter() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        task1.printconfiguration();
        task2.getprintconfiguration();

        assertEquals(task1.configuration.printConfiguration,
                task2.configuration.printConfiguration,
                "Both methods should set the same value");
        assertEquals(Configuration.STD_OUT, task1.configuration.printConfiguration,
                "task1 should have STD_OUT");
        assertEquals(Configuration.STD_OUT, task2.configuration.printConfiguration,
                "task2 should have STD_OUT");
    }

    @Test
    public void testPrintconfiguration_canBeMixedWithGetter() throws Exception {
        task.printconfiguration();
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "After direct call, should be set");

        task.getprintconfiguration();
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "After getter call, should still be set");

        task.printconfiguration();
        assertEquals(Configuration.STD_OUT, task.configuration.printConfiguration,
                "After another direct call, should still be set");
    }
}

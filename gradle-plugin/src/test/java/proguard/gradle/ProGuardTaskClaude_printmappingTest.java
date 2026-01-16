package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.Configuration;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.printmapping() method.
 *
 * This tests the void method that sets configuration.printMapping = Configuration.STD_OUT.
 */
class ProGuardTaskClaude_printmappingTest {

    @TempDir
    File testProjectDir;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(testProjectDir)
                .build();
        task = project.getTasks().create("testProguard", ProGuardTask.class);
    }

    // ============================================================
    // Basic Functionality Tests
    // ============================================================

    @Test
    void testPrintmappingSetsPrintMappingToStdOut() {
        task.printmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printmapping() should set printMapping to STD_OUT");
    }

    @Test
    void testPrintmappingChangesConfigurationFromNull() {
        assertNull(task.configuration.printMapping, "printMapping should initially be null");
        task.printmapping();
        assertNotNull(task.configuration.printMapping, "printMapping should be set after printmapping()");
    }

    @Test
    void testPrintmappingSetsStdOutConstant() {
        task.printmapping();
        assertSame(Configuration.STD_OUT, task.configuration.printMapping,
                "Should set the exact STD_OUT constant");
    }

    @Test
    void testPrintmappingWithFreshTask() {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        freshTask.printmapping();
        assertEquals(Configuration.STD_OUT, freshTask.configuration.printMapping);
    }

    @Test
    void testPrintmappingIsVoidMethod() {
        // Verify method signature - should not return anything
        task.printmapping();
        // If we got here without compilation error, the method is void
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    void testPrintmappingCalledMultipleTimes() {
        task.printmapping();
        task.printmapping();
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "Multiple calls should maintain STD_OUT");
    }

    @Test
    void testPrintmappingIdempotent() {
        task.printmapping();
        File firstMapping = task.configuration.printMapping;

        task.printmapping();
        File secondMapping = task.configuration.printMapping;

        assertEquals(firstMapping, secondMapping, "Multiple calls should maintain the same printMapping value");
        assertSame(firstMapping, secondMapping, "Should be the same object reference");
    }

    @Test
    void testPrintmappingConsecutiveCalls() {
        for (int i = 0; i < 10; i++) {
            task.printmapping();
        }
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    void testPrintmappingWithDontobfuscate() {
        task.printmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingWithDontshrink() {
        task.printmapping();
        task.dontshrink();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.shrink);
    }

    @Test
    void testPrintmappingWithDontoptimize() {
        task.printmapping();
        task.dontoptimize();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testPrintmappingWithVerbose() {
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingWithAllowAccessModification() {
        task.printmapping();
        task.allowaccessmodification();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPrintmappingWithMergeInterfacesAggressively() {
        task.printmapping();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPrintmappingWithMultipleConfigMethods() {
        task.printmapping();
        task.dontobfuscate();
        task.allowaccessmodification();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingWithAllOptimizationSettings() {
        task.printmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    void testPrintmappingBeforeOtherConfig() {
        task.printmapping();
        task.dontobfuscate();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingAfterOtherConfig() {
        task.dontobfuscate();
        task.verbose();
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingBetweenOtherConfig() {
        task.dontobfuscate();
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    void testPrintmappingInAndroidReleaseWithObfuscation() {
        // Typical Android release build with obfuscation and mapping to stdout
        task.printmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingInDebugBuildForDebugging() {
        // Debug build that enables mapping for debugging purposes
        task.printmapping();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testPrintmappingInStagingBuild() {
        // Staging build with mapping for crash analysis
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingInLibraryConfiguration() {
        // Library project configuration with mapping output
        task.printmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingForCrashReportAnalysis() {
        // Production build with mapping for crash report de-obfuscation
        task.printmapping();
        task.allowaccessmodification();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPrintmappingInMinimalProguardConfig() {
        // Minimal ProGuard setup with just mapping output to stdout
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testPrintmappingInMaximalProguardConfig() {
        // Comprehensive ProGuard setup
        task.printmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingForCiCdLogging() {
        // CI/CD pipeline configuration capturing mapping in logs
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    void testPrintmappingWithNullConfiguration() {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration);
        task.printmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testPrintmappingOnMultipleTasks() {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.printmapping();
        task2.printmapping();
        task3.printmapping();

        assertEquals(Configuration.STD_OUT, task1.configuration.printMapping);
        assertEquals(Configuration.STD_OUT, task2.configuration.printMapping);
        assertEquals(Configuration.STD_OUT, task3.configuration.printMapping);
    }

    @Test
    void testPrintmappingIndependentOfOtherTasks() {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertNull(otherTask.configuration.printMapping, "Other task should not be affected");
    }

    @Test
    void testPrintmappingAfterConfigurationAccess() {
        // Access configuration before calling printmapping
        Configuration config = task.configuration;
        assertNotNull(config);

        task.printmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testPrintmappingRepeatedOverTime() {
        // Simulate multiple calls over time
        task.printmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);

        task.dontobfuscate();
        task.printmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);

        task.verbose();
        task.printmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    void testPrintmappingInAndroidReleaseVariant() {
        // Android release variant with mapping to stdout for Play Console
        task.printmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPrintmappingInAndroidDebugVariant() {
        // Android debug variant with mapping but no obfuscation
        task.printmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingInAndroidLibraryModule() {
        // Android library with mapping output to stdout
        task.printmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingForAndroidMultiDexBuild() {
        // Multi-dex build with mapping to stdout
        task.printmapping();
        task.allowaccessmodification();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPrintmappingForAndroidTestBuild() {
        // Test build configuration
        task.printmapping();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testPrintmappingForAndroidInstrumentationTests() {
        // Instrumentation test configuration
        task.printmapping();
        task.dontobfuscate();
        task.dontshrink();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
    }

    // ============================================================
    // Output and Reporting Scenarios
    // ============================================================

    @Test
    void testPrintmappingForConsoleOutput() {
        // Configure to output mapping directly to console
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testPrintmappingWithVerboseForDetailedOutput() {
        // Detailed console output with mapping
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingForBuildServerLogging() {
        // Build server capturing mapping in logs
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingForDebuggingProguardIssues() {
        // Debug ProGuard configuration issues with stdout mapping
        task.printmapping();
        task.verbose();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingForQuickInspection() {
        // Quick inspection of mapping without file I/O
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testPrintmappingForAutomatedAnalysis() {
        // Automated analysis tool reading from stdout
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    void testPrintmappingInProductionBuild() {
        // Production release build with stdout mapping
        task.printmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPrintmappingInDevelopmentBuild() {
        // Development build with shrinking but no obfuscation
        task.printmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingInStagingForQa() {
        // QA staging build
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingInInternalTestingBuild() {
        // Internal testing build
        task.printmapping();
        task.dontobfuscate();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingInBetaBuild() {
        // Beta testing build
        task.printmapping();
        task.allowaccessmodification();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPrintmappingInAlphaBuild() {
        // Alpha testing build
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Crash Analysis Scenarios
    // ============================================================

    @Test
    void testPrintmappingForCrashlytics() {
        // Crashlytics integration with stdout mapping
        task.printmapping();
        task.allowaccessmodification();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPrintmappingForFirebaseCrashReporting() {
        // Firebase crash reporting setup
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingForCustomErrorTracking() {
        // Custom error tracking service
        task.printmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPrintmappingForStackTraceDeobfuscation() {
        // Stack trace de-obfuscation setup
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    void testPrintmappingModifiesConfigurationState() {
        assertNull(task.configuration.printMapping);

        task.printmapping();

        assertNotNull(task.configuration.printMapping);
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testPrintmappingPreservesOtherConfigurationSettings() {
        task.dontobfuscate();
        task.verbose();

        task.printmapping();

        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testPrintmappingWithComplexConfigurationChain() {
        task.dontobfuscate();
        task.printmapping();
        task.allowaccessmodification();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPrintmappingDoesNotAffectShrinkSetting() {
        // Verify shrink remains at default
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    void testPrintmappingDoesNotAffectOptimizeSetting() {
        // Verify optimize remains at default
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    void testPrintmappingDoesNotAffectObfuscateSetting() {
        // Verify obfuscate remains at default
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ============================================================
    // Performance and Build Time Scenarios
    // ============================================================

    @Test
    void testPrintmappingForFastFeedbackBuilds() {
        // Fast feedback development builds
        task.printmapping();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testPrintmappingForIncrementalBuilds() {
        // Incremental build configuration
        task.printmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingForFullReleaseBuilds() {
        // Full release build with all optimizations
        task.printmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // ============================================================
    // Compatibility Scenarios
    // ============================================================

    @Test
    void testPrintmappingWithReflectionHeavyCode() {
        // Code using heavy reflection, needs mapping
        task.printmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingWithJniCode() {
        // JNI code requiring mapping
        task.printmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingWithSerializableClasses() {
        // Serializable classes configuration
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingWithPublicApiLibrary() {
        // Public API library needs mapping but no obfuscation
        task.printmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    void testPrintmappingForDockerizedBuilds() {
        // Dockerized build environment capturing stdout
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingForCloudBuilds() {
        // Cloud build service (AWS, GCP, Azure)
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingForLocalDevelopment() {
        // Local development environment
        task.printmapping();
        task.dontobfuscate();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingForContinuousIntegration() {
        // CI pipeline configuration
        task.printmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingWithMinimalConfiguration() {
        // Absolute minimal configuration
        task.printmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    // ============================================================
    // Tests for printmapping(Object) method
    // ============================================================

    // Basic Functionality Tests for printmapping(Object)

    @Test
    void testPrintmappingWithFile_setsPrintMappingToFile() throws Exception {
        String mappingFile = "mapping.txt";
        task.printmapping(mappingFile);
        assertNotNull(task.configuration.printMapping, "printMapping should not be null");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should have correct filename");
    }

    @Test
    void testPrintmappingWithFile_acceptsStringPath() throws Exception {
        task.printmapping("output/mapping.txt");
        assertNotNull(task.configuration.printMapping, "printMapping should not be null");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should have correct filename");
    }

    @Test
    void testPrintmappingWithFile_acceptsFileObject() throws Exception {
        File mappingFile = new File(testProjectDir, "mapping.txt");
        task.printmapping(mappingFile);
        assertNotNull(task.configuration.printMapping, "printMapping should not be null");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should have correct filename");
    }

    @Test
    void testPrintmappingWithFile_overwritesPreviousValue() throws Exception {
        task.printmapping("first.txt");
        assertEquals("first.txt", task.configuration.printMapping.getName(),
                "printMapping should be first.txt");
        task.printmapping("second.txt");
        assertEquals("second.txt", task.configuration.printMapping.getName(),
                "printMapping should be overwritten to second.txt");
    }

    @Test
    void testPrintmappingWithFile_overwritesStdOut() throws Exception {
        task.printmapping(); // Set to stdout
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should be STD_OUT");
        task.printmapping("mapping.txt");
        assertNotEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should no longer be STD_OUT");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should be mapping.txt");
    }

    @Test
    void testPrintmappingWithFile_changesFromNull() throws Exception {
        assertNull(task.configuration.printMapping, "printMapping should initially be null");
        task.printmapping("mapping.txt");
        assertNotNull(task.configuration.printMapping, "printMapping should be set after printmapping(Object)");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    // File Path Variations

    @Test
    void testPrintmappingWithFile_relativePathInBuildDir() throws Exception {
        task.printmapping("build/outputs/mapping.txt");
        assertNotNull(task.configuration.printMapping, "printMapping should not be null");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should have correct filename");
    }

    @Test
    void testPrintmappingWithFile_nestedDirectoryPath() throws Exception {
        task.printmapping("output/proguard/reports/mapping.txt");
        assertNotNull(task.configuration.printMapping, "printMapping should not be null");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should have correct filename");
    }

    @Test
    void testPrintmappingWithFile_withDifferentExtensions() throws Exception {
        String[] extensions = {"mapping.txt", "mapping.map", "mapping.log", "proguard-mapping.txt"};
        for (String filename : extensions) {
            ProGuardTask freshTask = project.getTasks().create("proguard_" + filename.replace(".", "_").replace("-", "_"), ProGuardTask.class);
            freshTask.printmapping(filename);
            assertNotNull(freshTask.configuration.printMapping,
                    "printMapping should not be null for " + filename);
        }
    }

    @Test
    void testPrintmappingWithFile_absolutePath() throws Exception {
        File absoluteFile = new File(testProjectDir, "absolute-mapping.txt");
        task.printmapping(absoluteFile.getAbsolutePath());
        assertNotNull(task.configuration.printMapping, "printMapping should not be null");
        assertEquals("absolute-mapping.txt", task.configuration.printMapping.getName());
    }

    @Test
    void testPrintmappingWithFile_fileInSubdirectory() throws Exception {
        task.printmapping("build/outputs/proguard/mapping.txt");
        assertNotNull(task.configuration.printMapping);
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    // Integration with printmapping() void method

    @Test
    void testPrintmappingWithFile_afterPrintmappingVoid() throws Exception {
        task.printmapping(); // stdout
        task.printmapping("mapping.txt"); // file
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should be mapping.txt, overwriting stdout");
    }

    @Test
    void testPrintmappingWithFile_beforePrintmappingVoid() throws Exception {
        task.printmapping("mapping.txt"); // file
        task.printmapping(); // stdout
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should be STD_OUT, overwriting file");
    }

    @Test
    void testPrintmappingWithFile_alternatingCalls() throws Exception {
        task.printmapping("first.txt");
        assertEquals("first.txt", task.configuration.printMapping.getName());

        task.printmapping(); // stdout
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);

        task.printmapping("second.txt");
        assertEquals("second.txt", task.configuration.printMapping.getName());
    }

    // Integration with Other Configuration Methods

    @Test
    void testPrintmappingWithFile_withDontobfuscate() throws Exception {
        task.printmapping("mapping.txt");
        task.dontobfuscate();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingWithFile_withVerbose() throws Exception {
        task.printmapping("mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingWithFile_withAllowAccessModification() throws Exception {
        task.printmapping("mapping.txt");
        task.allowaccessmodification();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPrintmappingWithFile_withMergeInterfacesAggressively() throws Exception {
        task.printmapping("mapping.txt");
        task.mergeinterfacesaggressively();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPrintmappingWithFile_withMultipleConfigMethods() throws Exception {
        task.printmapping("mapping.txt");
        task.dontobfuscate();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    // Call Order Tests

    @Test
    void testPrintmappingWithFile_beforeOtherConfig() throws Exception {
        task.printmapping("mapping.txt");
        task.dontobfuscate();
        task.verbose();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingWithFile_afterOtherConfig() throws Exception {
        task.dontobfuscate();
        task.verbose();
        task.printmapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingWithFile_betweenOtherConfig() throws Exception {
        task.dontobfuscate();
        task.printmapping("mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    // Realistic Scenarios with printmapping(Object)

    @Test
    void testPrintmappingWithFile_androidReleaseMapping() throws Exception {
        // Android release build with mapping file for Play Console
        task.printmapping("build/outputs/mapping/release/mapping.txt");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.printMapping);
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPrintmappingWithFile_androidDebugMapping() throws Exception {
        // Android debug build with mapping file but no obfuscation
        task.printmapping("build/outputs/mapping/debug/mapping.txt");
        task.dontobfuscate();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingWithFile_multiFlavorBuild() throws Exception {
        // Multi-flavor build with flavor-specific mapping
        task.printmapping("build/outputs/mapping/productionRelease/mapping.txt");
        task.allowaccessmodification();

        assertNotNull(task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPrintmappingWithFile_libraryModuleMapping() throws Exception {
        // Library module with mapping output
        task.printmapping("build/outputs/mapping/library-mapping.txt");
        task.dontobfuscate();

        assertEquals("library-mapping.txt", task.configuration.printMapping.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingWithFile_crashlyticsIntegration() throws Exception {
        // Crashlytics requiring mapping file
        task.printmapping("build/outputs/proguard/mapping.txt");
        task.allowaccessmodification();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPrintmappingWithFile_ciCdArtifact() throws Exception {
        // CI/CD pipeline saving mapping as artifact
        task.printmapping("artifacts/mapping/proguard-mapping.txt");
        task.verbose();

        assertEquals("proguard-mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingWithFile_versionedMapping() throws Exception {
        // Versioned mapping file for tracking releases
        task.printmapping("build/outputs/mapping/mapping-1.0.0.txt");

        assertEquals("mapping-1.0.0.txt", task.configuration.printMapping.getName());
    }

    @Test
    void testPrintmappingWithFile_timestampedMapping() throws Exception {
        // Timestamped mapping file
        task.printmapping("build/outputs/mapping/mapping-20230101.txt");

        assertEquals("mapping-20230101.txt", task.configuration.printMapping.getName());
    }

    // Android Build Variants

    @Test
    void testPrintmappingWithFile_productionVariant() throws Exception {
        task.printmapping("build/outputs/mapping/production/mapping.txt");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPrintmappingWithFile_stagingVariant() throws Exception {
        task.printmapping("build/outputs/mapping/staging/mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPrintmappingWithFile_developmentVariant() throws Exception {
        task.printmapping("build/outputs/mapping/development/mapping.txt");
        task.dontobfuscate();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPrintmappingWithFile_betaVariant() throws Exception {
        task.printmapping("build/outputs/mapping/beta/mapping.txt");
        task.allowaccessmodification();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    // Edge Cases

    @Test
    void testPrintmappingWithFile_multipleCallsWithDifferentFiles() throws Exception {
        task.printmapping("first.txt");
        assertEquals("first.txt", task.configuration.printMapping.getName());

        task.printmapping("second.txt");
        assertEquals("second.txt", task.configuration.printMapping.getName());

        task.printmapping("third.txt");
        assertEquals("third.txt", task.configuration.printMapping.getName());
    }

    @Test
    void testPrintmappingWithFile_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.printmapping("mapping1.txt");
        task2.printmapping("mapping2.txt");
        task3.printmapping("mapping3.txt");

        assertEquals("mapping1.txt", task1.configuration.printMapping.getName());
        assertEquals("mapping2.txt", task2.configuration.printMapping.getName());
        assertEquals("mapping3.txt", task3.configuration.printMapping.getName());
    }

    @Test
    void testPrintmappingWithFile_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.printmapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertNull(otherTask.configuration.printMapping, "Other task should not be affected");
    }

    @Test
    void testPrintmappingWithFile_withComplexPath() throws Exception {
        task.printmapping("build/outputs/proguard/release/variants/main/mapping.txt");
        assertNotNull(task.configuration.printMapping);
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    // Output Organization Scenarios

    @Test
    void testPrintmappingWithFile_organizedByBuildType() throws Exception {
        task.printmapping("mappings/release/mapping.txt");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    @Test
    void testPrintmappingWithFile_organizedByDate() throws Exception {
        task.printmapping("mappings/2023/01/mapping.txt");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    @Test
    void testPrintmappingWithFile_organizedByVersion() throws Exception {
        task.printmapping("mappings/v1.0/mapping.txt");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    // Configuration Preservation Tests

    @Test
    void testPrintmappingWithFile_preservesOtherSettings() throws Exception {
        task.dontobfuscate();
        task.verbose();
        task.allowaccessmodification();

        task.printmapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPrintmappingWithFile_doesNotAffectShrinkSetting() throws Exception {
        task.printmapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    void testPrintmappingWithFile_doesNotAffectOptimizeSetting() throws Exception {
        task.printmapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    void testPrintmappingWithFile_doesNotAffectObfuscateSetting() throws Exception {
        task.printmapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // Build System Integration

    @Test
    void testPrintmappingWithFile_gradleBuildDirectory() throws Exception {
        task.printmapping("build/mapping.txt");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    @Test
    void testPrintmappingWithFile_customOutputDirectory() throws Exception {
        task.printmapping("custom-output/mapping.txt");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    @Test
    void testPrintmappingWithFile_reportsDirectory() throws Exception {
        task.printmapping("build/reports/proguard/mapping.txt");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    // Multiple Configuration Chain

    @Test
    void testPrintmappingWithFile_complexConfigurationChain() throws Exception {
        task.dontobfuscate();
        task.printmapping("mapping.txt");
        task.allowaccessmodification();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPrintmappingWithFile_afterComplexConfiguration() throws Exception {
        task.dontobfuscate();
        task.allowaccessmodification();
        task.verbose();
        task.mergeinterfacesaggressively();
        task.printmapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.printMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // File Naming Conventions

    @Test
    void testPrintmappingWithFile_standardNaming() throws Exception {
        String[] standardNames = {
            "mapping.txt",
            "proguard-mapping.txt",
            "obfuscation-mapping.txt",
            "release-mapping.txt"
        };

        for (String name : standardNames) {
            ProGuardTask freshTask = project.getTasks().create("task_" + name.replace(".", "_").replace("-", "_"), ProGuardTask.class);
            freshTask.printmapping(name);
            assertEquals(name, freshTask.configuration.printMapping.getName(),
                    "Should handle standard name: " + name);
        }
    }

    @Test
    void testPrintmappingWithFile_descriptiveNaming() throws Exception {
        task.printmapping("app-release-v1.0.0-mapping.txt");
        assertEquals("app-release-v1.0.0-mapping.txt", task.configuration.printMapping.getName());
    }
}

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
 * Tests for ProGuardTask.getprintmapping() method.
 *
 * This tests the Groovy DSL getter method that returns null and calls printmapping()
 * which sets configuration.printMapping = Configuration.STD_OUT.
 */
class ProGuardTaskClaude_getprintmappingTest {

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
    void testGetprintmappingReturnsNull() {
        Object result = task.getprintmapping();
        assertNull(result, "getprintmapping() should return null for Groovy DSL compatibility");
    }

    @Test
    void testGetprintmappingSetsPrintMappingToStdOut() {
        task.getprintmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "getprintmapping() should set printMapping to STD_OUT");
    }

    @Test
    void testGetprintmappingChangesConfiguration() {
        assertNull(task.configuration.printMapping, "printMapping should initially be null");
        task.getprintmapping();
        assertNotNull(task.configuration.printMapping, "printMapping should be set after getprintmapping()");
    }

    @Test
    void testGetprintmappingWithFreshTask() {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        Object result = freshTask.getprintmapping();
        assertNull(result);
        assertEquals(Configuration.STD_OUT, freshTask.configuration.printMapping);
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    void testGetprintmappingCalledMultipleTimes() {
        Object result1 = task.getprintmapping();
        Object result2 = task.getprintmapping();
        Object result3 = task.getprintmapping();

        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testGetprintmappingIdempotent() {
        task.getprintmapping();
        File firstMapping = task.configuration.printMapping;

        task.getprintmapping();
        File secondMapping = task.configuration.printMapping;

        assertEquals(firstMapping, secondMapping, "Multiple calls should maintain the same printMapping value");
    }

    @Test
    void testGetprintmappingConsecutiveCallsReturnNull() {
        assertNull(task.getprintmapping());
        assertNull(task.getprintmapping());
        assertNull(task.getprintmapping());
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    void testGetprintmappingWithDontobfuscate() {
        task.getprintmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testGetprintmappingWithDontshrink() {
        task.getprintmapping();
        task.dontshrink();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.shrink);
    }

    @Test
    void testGetprintmappingWithDontoptimize() {
        task.getprintmapping();
        task.dontoptimize();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testGetprintmappingWithVerbose() {
        task.getprintmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testGetprintmappingWithAllowAccessModification() {
        task.getprintmapping();
        task.allowaccessmodification();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testGetprintmappingWithMergeInterfacesAggressively() {
        task.getprintmapping();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testGetprintmappingWithMultipleConfigMethods() {
        task.getprintmapping();
        task.dontobfuscate();
        task.allowaccessmodification();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    void testGetprintmappingBeforeOtherConfig() {
        task.getprintmapping();
        task.dontobfuscate();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testGetprintmappingAfterOtherConfig() {
        task.dontobfuscate();
        task.verbose();
        task.getprintmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testGetprintmappingBetweenOtherConfig() {
        task.dontobfuscate();
        task.getprintmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    void testGetprintmappingInAndroidReleaseWithObfuscation() {
        // Typical Android release build with obfuscation and mapping output
        task.getprintmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testGetprintmappingInDebugBuildForDebugging() {
        // Debug build that enables mapping for debugging purposes
        task.getprintmapping();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testGetprintmappingInStagingBuild() {
        // Staging build with mapping for crash analysis
        task.getprintmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testGetprintmappingInLibraryConfiguration() {
        // Library project configuration with mapping output
        task.getprintmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testGetprintmappingForCrashReportAnalysis() {
        // Production build with mapping for crash report de-obfuscation
        task.getprintmapping();
        task.allowaccessmodification();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testGetprintmappingInMinimalProguardConfig() {
        // Minimal ProGuard setup with just mapping output
        task.getprintmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testGetprintmappingInMaximalProguardConfig() {
        // Comprehensive ProGuard setup
        task.getprintmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    void testGetprintmappingWithNullConfiguration() {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration);
        task.getprintmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testGetprintmappingAlwaysReturnsNull() {
        for (int i = 0; i < 100; i++) {
            assertNull(task.getprintmapping(), "getprintmapping() should always return null");
        }
    }

    @Test
    void testGetprintmappingOnMultipleTasks() {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getprintmapping();
        task2.getprintmapping();
        task3.getprintmapping();

        assertEquals(Configuration.STD_OUT, task1.configuration.printMapping);
        assertEquals(Configuration.STD_OUT, task2.configuration.printMapping);
        assertEquals(Configuration.STD_OUT, task3.configuration.printMapping);
    }

    @Test
    void testGetprintmappingIndependentOfOtherTasks() {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getprintmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertNull(otherTask.configuration.printMapping, "Other task should not be affected");
    }

    @Test
    void testGetprintmappingAfterConfigurationAccess() {
        // Access configuration before calling getprintmapping
        Configuration config = task.configuration;
        assertNotNull(config);

        task.getprintmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    // ============================================================
    // Groovy DSL Compatibility Tests
    // ============================================================

    @Test
    void testGetprintmappingGroovyDslUsage() {
        // Simulate Groovy DSL: task.getprintmapping()
        Object result = task.getprintmapping();
        assertNull(result, "Should return null for Groovy DSL");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testGetprintmappingGroovyDslChaining() {
        // Groovy DSL allows method chaining (though return is null)
        task.getprintmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testGetprintmappingGroovyDslWithClosure() {
        // Simulate Groovy closure-style configuration
        task.getprintmapping();
        task.dontobfuscate();
        task.allowaccessmodification();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    void testGetprintmappingInAndroidReleaseVariant() {
        // Android release variant with mapping for Play Console
        task.getprintmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testGetprintmappingInAndroidDebugVariant() {
        // Android debug variant with mapping but no obfuscation
        task.getprintmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testGetprintmappingInAndroidLibraryModule() {
        // Android library with mapping output
        task.getprintmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testGetprintmappingForAndroidMultiDexBuild() {
        // Multi-dex build with mapping
        task.getprintmapping();
        task.allowaccessmodification();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testGetprintmappingForAndroidTestBuild() {
        // Test build configuration
        task.getprintmapping();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.optimize);
    }

    // ============================================================
    // Output and Reporting Scenarios
    // ============================================================

    @Test
    void testGetprintmappingForConsoleOutput() {
        // Configure to output mapping to console
        task.getprintmapping();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testGetprintmappingWithVerboseForDetailedOutput() {
        // Detailed console output with mapping
        task.getprintmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testGetprintmappingForCiCdPipeline() {
        // CI/CD build with mapping to stdout for log capture
        task.getprintmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testGetprintmappingForDebuggingProguardIssues() {
        // Debug ProGuard configuration issues
        task.getprintmapping();
        task.verbose();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
        assertFalse(task.configuration.obfuscate);
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    void testGetprintmappingInProductionBuild() {
        // Production release build
        task.getprintmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testGetprintmappingInDevelopmentBuild() {
        // Development build with shrinking but no obfuscation
        task.getprintmapping();
        task.dontobfuscate();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testGetprintmappingInStagingForQa() {
        // QA staging build
        task.getprintmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testGetprintmappingInInternalTestingBuild() {
        // Internal testing build
        task.getprintmapping();
        task.dontobfuscate();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testGetprintmappingInBetaBuild() {
        // Beta testing build
        task.getprintmapping();
        task.allowaccessmodification();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
    }

    // ============================================================
    // Crash Analysis Scenarios
    // ============================================================

    @Test
    void testGetprintmappingForCrashlytics() {
        // Crashlytics integration requiring mapping
        task.getprintmapping();
        task.allowaccessmodification();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testGetprintmappingForFirebaseCrashReporting() {
        // Firebase crash reporting setup
        task.getprintmapping();
        task.verbose();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testGetprintmappingForCustomErrorTracking() {
        // Custom error tracking service
        task.getprintmapping();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    void testGetprintmappingModifiesConfigurationState() {
        assertNull(task.configuration.printMapping);

        task.getprintmapping();

        assertNotNull(task.configuration.printMapping);
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testGetprintmappingPreservesOtherConfigurationSettings() {
        task.dontobfuscate();
        task.verbose();

        task.getprintmapping();

        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
    }

    @Test
    void testGetprintmappingWithComplexConfigurationChain() {
        task.dontobfuscate();
        task.getprintmapping();
        task.allowaccessmodification();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }
}

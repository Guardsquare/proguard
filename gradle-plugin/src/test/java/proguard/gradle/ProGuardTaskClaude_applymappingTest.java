package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.applymapping(Object) method.
 *
 * This tests the method that sets configuration.applyMapping to a resolved File.
 */
class ProGuardTaskClaude_applymappingTest {

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
    void testApplymappingWithString_setsApplyMappingToFile() throws Exception {
        String mappingFile = "mapping.txt";
        task.applymapping(mappingFile);
        assertNotNull(task.configuration.applyMapping, "applyMapping should not be null");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName(),
                "applyMapping should have correct filename");
    }

    @Test
    void testApplymappingWithString_acceptsStringPath() throws Exception {
        task.applymapping("input/mapping.txt");
        assertNotNull(task.configuration.applyMapping, "applyMapping should not be null");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName(),
                "applyMapping should have correct filename");
    }

    @Test
    void testApplymappingWithFile_acceptsFileObject() throws Exception {
        File mappingFile = new File(testProjectDir, "mapping.txt");
        task.applymapping(mappingFile);
        assertNotNull(task.configuration.applyMapping, "applyMapping should not be null");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName(),
                "applyMapping should have correct filename");
    }

    @Test
    void testApplymappingChangesConfigurationFromNull() throws Exception {
        assertNull(task.configuration.applyMapping, "applyMapping should initially be null");
        task.applymapping("mapping.txt");
        assertNotNull(task.configuration.applyMapping, "applyMapping should be set after applymapping()");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        freshTask.applymapping("mapping.txt");
        assertNotNull(freshTask.configuration.applyMapping);
        assertEquals("mapping.txt", freshTask.configuration.applyMapping.getName());
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    void testApplymappingOverwritesPreviousValue() throws Exception {
        task.applymapping("first.txt");
        assertEquals("first.txt", task.configuration.applyMapping.getName(),
                "applyMapping should be first.txt");
        task.applymapping("second.txt");
        assertEquals("second.txt", task.configuration.applyMapping.getName(),
                "applyMapping should be overwritten to second.txt");
    }

    @Test
    void testApplymappingMultipleCallsWithDifferentFiles() throws Exception {
        task.applymapping("first.txt");
        assertEquals("first.txt", task.configuration.applyMapping.getName());

        task.applymapping("second.txt");
        assertEquals("second.txt", task.configuration.applyMapping.getName());

        task.applymapping("third.txt");
        assertEquals("third.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingCalledMultipleTimes() throws Exception {
        task.applymapping("mapping.txt");
        task.applymapping("mapping.txt");
        task.applymapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.applyMapping.getName(),
                "Multiple calls with same file should work");
    }

    // ============================================================
    // File Path Variations
    // ============================================================

    @Test
    void testApplymappingWithRelativePathInBuildDir() throws Exception {
        task.applymapping("build/outputs/mapping.txt");
        assertNotNull(task.configuration.applyMapping, "applyMapping should not be null");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName(),
                "applyMapping should have correct filename");
    }

    @Test
    void testApplymappingWithNestedDirectoryPath() throws Exception {
        task.applymapping("input/proguard/reports/mapping.txt");
        assertNotNull(task.configuration.applyMapping, "applyMapping should not be null");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName(),
                "applyMapping should have correct filename");
    }

    @Test
    void testApplymappingWithDifferentExtensions() throws Exception {
        String[] extensions = {"mapping.txt", "mapping.map", "mapping.log", "proguard-mapping.txt"};
        for (String filename : extensions) {
            ProGuardTask freshTask = project.getTasks().create("proguard_" + filename.replace(".", "_").replace("-", "_"), ProGuardTask.class);
            freshTask.applymapping(filename);
            assertNotNull(freshTask.configuration.applyMapping,
                    "applyMapping should not be null for " + filename);
        }
    }

    @Test
    void testApplymappingWithAbsolutePath() throws Exception {
        File absoluteFile = new File(testProjectDir, "absolute-mapping.txt");
        task.applymapping(absoluteFile.getAbsolutePath());
        assertNotNull(task.configuration.applyMapping, "applyMapping should not be null");
        assertEquals("absolute-mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingWithFileInSubdirectory() throws Exception {
        task.applymapping("build/outputs/proguard/mapping.txt");
        assertNotNull(task.configuration.applyMapping);
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingWithComplexPath() throws Exception {
        task.applymapping("build/outputs/proguard/release/variants/main/mapping.txt");
        assertNotNull(task.configuration.applyMapping);
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    void testApplymappingWithDontobfuscate() throws Exception {
        task.applymapping("mapping.txt");
        task.dontobfuscate();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testApplymappingWithDontshrink() throws Exception {
        task.applymapping("mapping.txt");
        task.dontshrink();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.shrink);
    }

    @Test
    void testApplymappingWithDontoptimize() throws Exception {
        task.applymapping("mapping.txt");
        task.dontoptimize();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testApplymappingWithVerbose() throws Exception {
        task.applymapping("mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingWithAllowAccessModification() throws Exception {
        task.applymapping("mapping.txt");
        task.allowaccessmodification();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testApplymappingWithMergeInterfacesAggressively() throws Exception {
        task.applymapping("mapping.txt");
        task.mergeinterfacesaggressively();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testApplymappingWithMultipleConfigMethods() throws Exception {
        task.applymapping("mapping.txt");
        task.dontobfuscate();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingWithAllOptimizationSettings() throws Exception {
        task.applymapping("mapping.txt");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    void testApplymappingBeforeOtherConfig() throws Exception {
        task.applymapping("mapping.txt");
        task.dontobfuscate();
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingAfterOtherConfig() throws Exception {
        task.dontobfuscate();
        task.verbose();
        task.applymapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingBetweenOtherConfig() throws Exception {
        task.dontobfuscate();
        task.applymapping("mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    void testApplymappingInIncrementalObfuscation() throws Exception {
        // Incremental obfuscation applying previous mapping
        task.applymapping("previous-release/mapping.txt");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.applyMapping);
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testApplymappingForLibraryUpdate() throws Exception {
        // Library update maintaining consistent obfuscation
        task.applymapping("v1.0.0/mapping.txt");
        task.dontobfuscate();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testApplymappingForPatchRelease() throws Exception {
        // Patch release using previous mapping
        task.applymapping("base-version/mapping.txt");
        task.allowaccessmodification();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testApplymappingForConsistentNaming() throws Exception {
        // Maintaining consistent naming across builds
        task.applymapping("build/outputs/mapping/baseline/mapping.txt");
        task.verbose();

        assertNotNull(task.configuration.applyMapping);
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingFromPreviousBuild() throws Exception {
        // Using mapping from previous successful build
        task.applymapping("previous-build/mapping.txt");
        task.allowaccessmodification();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testApplymappingInMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup with applied mapping
        task.applymapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingInMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.applymapping("mapping.txt");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    void testApplymappingWithNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration);
        task.applymapping("mapping.txt");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingOnMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.applymapping("mapping1.txt");
        task2.applymapping("mapping2.txt");
        task3.applymapping("mapping3.txt");

        assertEquals("mapping1.txt", task1.configuration.applyMapping.getName());
        assertEquals("mapping2.txt", task2.configuration.applyMapping.getName());
        assertEquals("mapping3.txt", task3.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingIndependentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.applymapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertNull(otherTask.configuration.applyMapping, "Other task should not be affected");
    }

    @Test
    void testApplymappingAfterConfigurationAccess() throws Exception {
        // Access configuration before calling applymapping
        assertNotNull(task.configuration);

        task.applymapping("mapping.txt");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingRepeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.applymapping("mapping1.txt");
        assertEquals("mapping1.txt", task.configuration.applyMapping.getName());

        task.dontobfuscate();
        task.applymapping("mapping2.txt");
        assertEquals("mapping2.txt", task.configuration.applyMapping.getName());

        task.verbose();
        task.applymapping("mapping3.txt");
        assertEquals("mapping3.txt", task.configuration.applyMapping.getName());
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    void testApplymappingInAndroidReleaseVariant() throws Exception {
        // Android release variant with applied mapping
        task.applymapping("previous-release/mapping.txt");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testApplymappingInAndroidDebugVariant() throws Exception {
        // Android debug variant with applied mapping but no obfuscation
        task.applymapping("mapping.txt");
        task.dontobfuscate();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testApplymappingInAndroidLibraryModule() throws Exception {
        // Android library with applied mapping
        task.applymapping("mapping.txt");
        task.dontobfuscate();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testApplymappingForAndroidMultiDexBuild() throws Exception {
        // Multi-dex build with applied mapping
        task.applymapping("mapping.txt");
        task.allowaccessmodification();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testApplymappingForAndroidTestBuild() throws Exception {
        // Test build configuration with applied mapping
        task.applymapping("mapping.txt");
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testApplymappingForAndroidInstrumentationTests() throws Exception {
        // Instrumentation test configuration
        task.applymapping("mapping.txt");
        task.dontobfuscate();
        task.dontshrink();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    void testApplymappingInProductionBuild() throws Exception {
        // Production release build with applied mapping
        task.applymapping("mapping.txt");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testApplymappingInDevelopmentBuild() throws Exception {
        // Development build with applied mapping but no obfuscation
        task.applymapping("mapping.txt");
        task.dontobfuscate();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testApplymappingInStagingForQa() throws Exception {
        // QA staging build with applied mapping
        task.applymapping("mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingInInternalTestingBuild() throws Exception {
        // Internal testing build with applied mapping
        task.applymapping("mapping.txt");
        task.dontobfuscate();
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingInBetaBuild() throws Exception {
        // Beta testing build with applied mapping
        task.applymapping("mapping.txt");
        task.allowaccessmodification();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testApplymappingInAlphaBuild() throws Exception {
        // Alpha testing build with applied mapping
        task.applymapping("mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Version Management Scenarios
    // ============================================================

    @Test
    void testApplymappingForVersionConsistency() throws Exception {
        // Maintaining version consistency across releases
        task.applymapping("v1.0.0/mapping.txt");
        task.allowaccessmodification();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testApplymappingForHotfixRelease() throws Exception {
        // Hotfix release using production mapping
        task.applymapping("production/mapping.txt");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testApplymappingForMinorUpdate() throws Exception {
        // Minor version update maintaining compatibility
        task.applymapping("v1.0/mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    void testApplymappingModifiesConfigurationState() throws Exception {
        assertNull(task.configuration.applyMapping);

        task.applymapping("mapping.txt");

        assertNotNull(task.configuration.applyMapping);
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingPreservesOtherConfigurationSettings() throws Exception {
        task.dontobfuscate();
        task.verbose();

        task.applymapping("mapping.txt");

        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingWithComplexConfigurationChain() throws Exception {
        task.dontobfuscate();
        task.applymapping("mapping.txt");
        task.allowaccessmodification();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testApplymappingDoesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.applymapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    void testApplymappingDoesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.applymapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    void testApplymappingDoesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.applymapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ============================================================
    // Output Organization Scenarios
    // ============================================================

    @Test
    void testApplymappingOrganizedByBuildType() throws Exception {
        task.applymapping("mappings/release/mapping.txt");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingOrganizedByDate() throws Exception {
        task.applymapping("mappings/2023/01/mapping.txt");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingOrganizedByVersion() throws Exception {
        task.applymapping("mappings/v1.0/mapping.txt");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    // ============================================================
    // Configuration Preservation Tests
    // ============================================================

    @Test
    void testApplymappingPreservesOtherSettings() throws Exception {
        task.dontobfuscate();
        task.verbose();
        task.allowaccessmodification();

        task.applymapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testApplymappingAfterComplexConfiguration() throws Exception {
        task.dontobfuscate();
        task.allowaccessmodification();
        task.verbose();
        task.mergeinterfacesaggressively();
        task.applymapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // ============================================================
    // File Naming Conventions
    // ============================================================

    @Test
    void testApplymappingStandardNaming() throws Exception {
        String[] standardNames = {
            "mapping.txt",
            "proguard-mapping.txt",
            "obfuscation-mapping.txt",
            "release-mapping.txt"
        };

        for (String name : standardNames) {
            ProGuardTask freshTask = project.getTasks().create("task_" + name.replace(".", "_").replace("-", "_"), ProGuardTask.class);
            freshTask.applymapping(name);
            assertEquals(name, freshTask.configuration.applyMapping.getName(),
                    "Should handle standard name: " + name);
        }
    }

    @Test
    void testApplymappingDescriptiveNaming() throws Exception {
        task.applymapping("app-release-v1.0.0-mapping.txt");
        assertEquals("app-release-v1.0.0-mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingWithVersionedFile() throws Exception {
        task.applymapping("mapping-1.0.0.txt");
        assertEquals("mapping-1.0.0.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingWithTimestampedFile() throws Exception {
        task.applymapping("mapping-20230101.txt");
        assertEquals("mapping-20230101.txt", task.configuration.applyMapping.getName());
    }

    // ============================================================
    // Build System Integration
    // ============================================================

    @Test
    void testApplymappingGradleBuildDirectory() throws Exception {
        task.applymapping("build/mapping.txt");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingCustomInputDirectory() throws Exception {
        task.applymapping("custom-input/mapping.txt");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    @Test
    void testApplymappingFromArtifactsDirectory() throws Exception {
        task.applymapping("artifacts/proguard/mapping.txt");
        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    void testApplymappingForDockerizedBuilds() throws Exception {
        // Dockerized build environment with mounted mapping
        task.applymapping("mounted/mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingForCloudBuilds() throws Exception {
        // Cloud build service (AWS, GCP, Azure)
        task.applymapping("previous-builds/mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingForLocalDevelopment() throws Exception {
        // Local development environment
        task.applymapping("mapping.txt");
        task.dontobfuscate();
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingForContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.applymapping("ci-artifacts/mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingWithMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.applymapping("mapping.txt");

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
    }

    // ============================================================
    // Multi-flavor and Multi-variant Scenarios
    // ============================================================

    @Test
    void testApplymappingProductionVariant() throws Exception {
        task.applymapping("build/outputs/mapping/production/mapping.txt");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testApplymappingStagingVariant() throws Exception {
        task.applymapping("build/outputs/mapping/staging/mapping.txt");
        task.verbose();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testApplymappingDevelopmentVariant() throws Exception {
        task.applymapping("build/outputs/mapping/development/mapping.txt");
        task.dontobfuscate();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testApplymappingBetaVariant() throws Exception {
        task.applymapping("build/outputs/mapping/beta/mapping.txt");
        task.allowaccessmodification();

        assertEquals("mapping.txt", task.configuration.applyMapping.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testApplymappingForMultiFlavorBuild() throws Exception {
        task.applymapping("build/outputs/mapping/productionRelease/mapping.txt");
        task.allowaccessmodification();

        assertNotNull(task.configuration.applyMapping);
        assertTrue(task.configuration.allowAccessModification);
    }
}

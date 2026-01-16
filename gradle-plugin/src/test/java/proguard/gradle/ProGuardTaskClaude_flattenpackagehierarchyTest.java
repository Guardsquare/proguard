package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.flattenpackagehierarchy()V method.
 * Tests the flattenpackagehierarchy() void method that sets the flattenPackageHierarchy
 * configuration to an empty string, flattening all packages to the root level.
 */
public class ProGuardTaskClaude_flattenpackagehierarchyTest {

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
    public void testFlattenpackagehierarchy_setsFlattenPackageHierarchy() throws Exception {
        assertNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should initially be null");
        task.flattenpackagehierarchy();
        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testFlattenpackagehierarchy_enablesFlatteningPackageHierarchy() throws Exception {
        assertNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be null by default");
        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "package hierarchy flattening should be enabled");
    }

    @Test
    public void testFlattenpackagehierarchy_doesNotReturnValue() throws Exception {
        // Verify method is void and completes successfully
        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string after call");
    }

    @Test
    public void testFlattenpackagehierarchy_changesConfigurationState() throws Exception {
        assertNull(task.configuration.flattenPackageHierarchy, "Initial state should be null");

        task.flattenpackagehierarchy();

        assertNotNull(task.configuration.flattenPackageHierarchy, "State should be modified");
        assertEquals("", task.configuration.flattenPackageHierarchy, "State should be empty string");
    }

    @Test
    public void testFlattenpackagehierarchy_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertNull(freshTask.configuration.flattenPackageHierarchy, "Fresh task should have flattenPackageHierarchy as null");

        freshTask.flattenpackagehierarchy();

        assertEquals("", freshTask.configuration.flattenPackageHierarchy, "Should set flattenPackageHierarchy to empty string");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testFlattenpackagehierarchy_multipleCallsAreIdempotent() throws Exception {
        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "First call should set empty string");

        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "Second call should keep empty string");

        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "Third call should keep empty string");
    }

    @Test
    public void testFlattenpackagehierarchy_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.flattenpackagehierarchy();
            assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string after call " + (i + 1));
        }
    }

    @Test
    public void testFlattenpackagehierarchy_consecutiveCalls() throws Exception {
        task.flattenpackagehierarchy();
        task.flattenpackagehierarchy();
        task.flattenpackagehierarchy();
        task.flattenpackagehierarchy();
        task.flattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "After 5 consecutive calls, flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testFlattenpackagehierarchy_remainsEmptyAfterMultipleCalls() throws Exception {
        task.flattenpackagehierarchy();
        String stateAfterFirst = task.configuration.flattenPackageHierarchy;

        task.flattenpackagehierarchy();
        String stateAfterSecond = task.configuration.flattenPackageHierarchy;

        assertEquals("", stateAfterFirst, "State after first call should be empty string");
        assertEquals("", stateAfterSecond, "State after second call should be empty string");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testFlattenpackagehierarchy_withDontobfuscate() throws Exception {
        task.flattenpackagehierarchy();
        task.dontobfuscate();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testFlattenpackagehierarchy_withDontshrink() throws Exception {
        task.flattenpackagehierarchy();
        task.dontshrink();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testFlattenpackagehierarchy_withDontoptimize() throws Exception {
        task.flattenpackagehierarchy();
        task.dontoptimize();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testFlattenpackagehierarchy_withVerbose() throws Exception {
        task.flattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_withAllowAccessModification() throws Exception {
        task.flattenpackagehierarchy();
        task.allowaccessmodification();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_withOverloadAggressively() throws Exception {
        task.flattenpackagehierarchy();
        task.overloadaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_withMergeInterfacesAggressively() throws Exception {
        task.flattenpackagehierarchy();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_withMultipleConfigMethods() throws Exception {
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_withAllOptimizationSettings() throws Exception {
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_withOptimizationPasses() throws Exception {
        task.flattenpackagehierarchy();
        task.optimizationpasses(5);

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testFlattenpackagehierarchy_beforeOtherConfig() throws Exception {
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.flattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.flattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_inComplexConfigurationChain() throws Exception {
        task.verbose();
        task.allowaccessmodification();
        task.flattenpackagehierarchy();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testFlattenpackagehierarchy_inObfuscationScenario() throws Exception {
        // Flatten package hierarchy during obfuscation
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_forCodeSizeReduction() throws Exception {
        // Flatten package hierarchy for smaller code size
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_inProductionBuild() throws Exception {
        // Production build flattening package hierarchy
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup flattening package hierarchy
        task.flattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testFlattenpackagehierarchy_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_forBetterObfuscation() throws Exception {
        // Flatten package hierarchy for better obfuscation
        task.flattenpackagehierarchy();
        task.allowaccessmodification();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_forReducedMetadata() throws Exception {
        // Flatten package hierarchy to reduce metadata
        task.flattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testFlattenpackagehierarchy_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testFlattenpackagehierarchy_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.flattenpackagehierarchy();
        task2.flattenpackagehierarchy();
        task3.flattenpackagehierarchy();

        assertEquals("", task1.configuration.flattenPackageHierarchy, "task1 flattenPackageHierarchy should be empty string");
        assertEquals("", task2.configuration.flattenPackageHierarchy, "task2 flattenPackageHierarchy should be empty string");
        assertEquals("", task3.configuration.flattenPackageHierarchy, "task3 flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testFlattenpackagehierarchy_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.flattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "task flattenPackageHierarchy should be empty string");
        assertNull(otherTask.configuration.flattenPackageHierarchy, "otherTask should not be affected");
    }

    @Test
    public void testFlattenpackagehierarchy_afterConfigurationAccess() throws Exception {
        // Access configuration before calling flattenpackagehierarchy
        assertNotNull(task.configuration, "Configuration should not be null");

        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testFlattenpackagehierarchy_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "Should be empty string after first call");

        task.allowaccessmodification();
        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "Should be empty string after second call");

        task.verbose();
        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "Should be empty string after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testFlattenpackagehierarchy_inAndroidReleaseVariant() throws Exception {
        // Android release variant flattening package hierarchy
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_inAndroidDebugVariant() throws Exception {
        // Android debug variant flattening package hierarchy
        task.flattenpackagehierarchy();
        task.dontobfuscate();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testFlattenpackagehierarchy_inAndroidLibraryModule() throws Exception {
        // Android library flattening package hierarchy
        task.flattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testFlattenpackagehierarchy_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build flattening package hierarchy
        task.flattenpackagehierarchy();
        task.allowaccessmodification();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_forAndroidTestBuild() throws Exception {
        // Test build configuration
        task.flattenpackagehierarchy();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testFlattenpackagehierarchy_forAndroidInstrumentationTests() throws Exception {
        // Instrumentation test configuration
        task.flattenpackagehierarchy();
        task.dontobfuscate();
        task.dontshrink();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testFlattenpackagehierarchy_inProductionRelease() throws Exception {
        // Production release build flattening package hierarchy
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_inDevelopmentBuild() throws Exception {
        // Development build with shrinking but flattening package hierarchy
        task.flattenpackagehierarchy();
        task.dontobfuscate();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testFlattenpackagehierarchy_inStagingForQa() throws Exception {
        // QA staging build flattening package hierarchy
        task.flattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_inInternalTestingBuild() throws Exception {
        // Internal testing build
        task.flattenpackagehierarchy();
        task.dontobfuscate();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_inBetaBuild() throws Exception {
        // Beta testing build flattening package hierarchy
        task.flattenpackagehierarchy();
        task.allowaccessmodification();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_inAlphaBuild() throws Exception {
        // Alpha testing build flattening package hierarchy
        task.flattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testFlattenpackagehierarchy_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.flattenPackageHierarchy, "Initial state should be null");

        task.flattenpackagehierarchy();

        assertNotNull(task.configuration.flattenPackageHierarchy, "State should be modified");
        assertEquals("", task.configuration.flattenPackageHierarchy, "State should be empty string");
    }

    @Test
    public void testFlattenpackagehierarchy_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.flattenpackagehierarchy();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testFlattenpackagehierarchy_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.flattenpackagehierarchy();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.flattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testFlattenpackagehierarchy_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.flattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testFlattenpackagehierarchy_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.flattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    @Test
    public void testFlattenpackagehierarchy_onlyAffectsFlattenPackageHierarchySetting() throws Exception {
        // Capture default states
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;
        boolean defaultAllowAccessModification = task.configuration.allowAccessModification;

        task.flattenpackagehierarchy();

        // Verify only flattenPackageHierarchy changed
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertEquals(defaultShrink, task.configuration.shrink, "shrink should be unchanged");
        assertEquals(defaultOptimize, task.configuration.optimize, "optimize should be unchanged");
        assertEquals(defaultObfuscate, task.configuration.obfuscate, "obfuscate should be unchanged");
        assertEquals(defaultAllowAccessModification, task.configuration.allowAccessModification, "allowAccessModification should be unchanged");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testFlattenpackagehierarchy_forDockerizedBuilds() throws Exception {
        // Dockerized build environment flattening package hierarchy
        task.flattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_forCloudBuilds() throws Exception {
        // Cloud build service flattening package hierarchy
        task.flattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_forLocalDevelopment() throws Exception {
        // Local development environment
        task.flattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.flattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.flattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    // ============================================================
    // Package Hierarchy Flattening Context Tests
    // ============================================================

    @Test
    public void testFlattenpackagehierarchy_forFlatteningPackages() throws Exception {
        // Flatten all packages to root level
        task.flattenpackagehierarchy();
        task.allowaccessmodification();

        assertEquals("", task.configuration.flattenPackageHierarchy, "Should set flattenPackageHierarchy to empty string");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testFlattenpackagehierarchy_withOtherObfuscationSettings() throws Exception {
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testFlattenpackagehierarchy_forOptimalObfuscation() throws Exception {
        // Combine flatten package hierarchy with other settings
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Performance and Build Time Scenarios
    // ============================================================

    @Test
    public void testFlattenpackagehierarchy_forFastFeedbackBuilds() throws Exception {
        // Fast feedback development builds
        task.flattenpackagehierarchy();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testFlattenpackagehierarchy_forIncrementalBuilds() throws Exception {
        // Incremental build configuration
        task.flattenpackagehierarchy();
        task.dontobfuscate();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testFlattenpackagehierarchy_forFullReleaseBuilds() throws Exception {
        // Full release build with all optimizations
        task.flattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Tests for flattenpackagehierarchy(String) Method
    // ============================================================

    @Test
    public void testFlattenpackagehierarchyWithString_emptyString() throws Exception {
        assertNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should initially be null");

        task.flattenpackagehierarchy("");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_singlePackageName() throws Exception {
        task.flattenpackagehierarchy("com.example.myapp");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("com/example/myapp", task.configuration.flattenPackageHierarchy,
                     "Should contain internal form of package name");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_nestedPackageName() throws Exception {
        task.flattenpackagehierarchy("com.example.app.internal");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("com/example/app/internal", task.configuration.flattenPackageHierarchy,
                     "Should convert dots to slashes");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_singleLevel() throws Exception {
        task.flattenpackagehierarchy("myapp");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("myapp", task.configuration.flattenPackageHierarchy,
                     "Should handle single level package");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_multipleCallsOverwrite() throws Exception {
        task.flattenpackagehierarchy("com.example.app");
        assertEquals("com/example/app", task.configuration.flattenPackageHierarchy,
                     "First call should set package");

        task.flattenpackagehierarchy("com.example.lib");
        assertEquals("com/example/lib", task.configuration.flattenPackageHierarchy,
                     "Second call should overwrite with new package");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_overwritesPreviousValue() throws Exception {
        task.flattenpackagehierarchy("com.example.old");
        task.flattenpackagehierarchy("com.example.new");

        assertEquals("com/example/new", task.configuration.flattenPackageHierarchy,
                     "Should overwrite previous value");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_afterNoArgVersion() throws Exception {
        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "Should be empty after no-arg call");

        task.flattenpackagehierarchy("com.example.app");
        assertEquals("com/example/app", task.configuration.flattenPackageHierarchy,
                     "Should overwrite with specific package");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_beforeNoArgVersion() throws Exception {
        task.flattenpackagehierarchy("com.example.app");
        assertEquals("com/example/app", task.configuration.flattenPackageHierarchy,
                     "Should have specific package");

        task.flattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy,
                     "No-arg version should overwrite with empty string");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_veryLongPackageName() throws Exception {
        task.flattenpackagehierarchy("com.example.very.long.package.name.hierarchy.level.deep");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("com/example/very/long/package/name/hierarchy/level/deep",
                     task.configuration.flattenPackageHierarchy,
                     "Should handle long package names");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_singleCharacterPackage() throws Exception {
        task.flattenpackagehierarchy("a.b.c");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("a/b/c", task.configuration.flattenPackageHierarchy,
                     "Should handle single character packages");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_numericPackageNames() throws Exception {
        task.flattenpackagehierarchy("com.example.v2.api");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("com/example/v2/api", task.configuration.flattenPackageHierarchy,
                     "Should handle numeric package components");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_underscoreInPackage() throws Exception {
        task.flattenpackagehierarchy("com.example.my_app");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("com/example/my_app", task.configuration.flattenPackageHierarchy,
                     "Should handle underscores in package names");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_dollarSignInPackage() throws Exception {
        task.flattenpackagehierarchy("com.example.app$inner");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("com/example/app$inner", task.configuration.flattenPackageHierarchy,
                     "Should handle dollar signs in package names");
    }

    // ============================================================
    // Integration Tests with String Parameter
    // ============================================================

    @Test
    public void testFlattenpackagehierarchyWithString_withObfuscation() throws Exception {
        task.flattenpackagehierarchy("com.example.flat");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("com/example/flat", task.configuration.flattenPackageHierarchy,
                     "Should have specific package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_withVerbose() throws Exception {
        task.flattenpackagehierarchy("com.example.flat");
        task.verbose();

        assertEquals("com/example/flat", task.configuration.flattenPackageHierarchy,
                     "Should have specific package");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_withDontobfuscate() throws Exception {
        task.flattenpackagehierarchy("com.example.flat");
        task.dontobfuscate();

        assertEquals("com/example/flat", task.configuration.flattenPackageHierarchy,
                     "Should have specific package");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    // ============================================================
    // Realistic Scenarios with String Parameter
    // ============================================================

    @Test
    public void testFlattenpackagehierarchyWithString_forSpecificTargetPackage() throws Exception {
        // Flatten all packages into a specific target package
        task.flattenpackagehierarchy("com.example.obfuscated");
        task.allowaccessmodification();

        assertEquals("com/example/obfuscated", task.configuration.flattenPackageHierarchy,
                     "Should flatten to specific target package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_forAndroidApp() throws Exception {
        // Android app flattening to specific package
        task.flattenpackagehierarchy("com.example.myapp.flattened");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("com/example/myapp/flattened", task.configuration.flattenPackageHierarchy,
                     "Should flatten to app-specific package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_forLibrary() throws Exception {
        // Library flattening to specific package
        task.flattenpackagehierarchy("com.example.lib.internal");

        assertEquals("com/example/lib/internal", task.configuration.flattenPackageHierarchy,
                     "Should flatten to library internal package");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_forObfuscatedPackage() throws Exception {
        // Flatten to short obfuscated package name
        task.flattenpackagehierarchy("a.b");
        task.allowaccessmodification();

        assertEquals("a/b", task.configuration.flattenPackageHierarchy,
                     "Should flatten to short obfuscated package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_forSingleLetterPackage() throws Exception {
        // Flatten to minimal single-letter package
        task.flattenpackagehierarchy("x");
        task.allowaccessmodification();

        assertEquals("x", task.configuration.flattenPackageHierarchy,
                     "Should flatten to single-letter package");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_forProductionBuild() throws Exception {
        // Production build with specific flattening target
        task.flattenpackagehierarchy("com.example.prod");
        task.allowaccessmodification();
        task.verbose();

        assertEquals("com/example/prod", task.configuration.flattenPackageHierarchy,
                     "Should flatten to production package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Edge Cases with String Parameter
    // ============================================================

    @Test
    public void testFlattenpackagehierarchyWithString_multipleDots() throws Exception {
        task.flattenpackagehierarchy("com.example..app");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("com/example//app", task.configuration.flattenPackageHierarchy,
                     "Should convert multiple dots to multiple slashes");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_trailingDot() throws Exception {
        task.flattenpackagehierarchy("com.example.app.");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("com/example/app/", task.configuration.flattenPackageHierarchy,
                     "Should handle trailing dot");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_leadingDot() throws Exception {
        task.flattenpackagehierarchy(".com.example.app");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("/com/example/app", task.configuration.flattenPackageHierarchy,
                     "Should handle leading dot");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_mixedCase() throws Exception {
        task.flattenpackagehierarchy("com.Example.MyApp");

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("com/Example/MyApp", task.configuration.flattenPackageHierarchy,
                     "Should preserve case in package names");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_consecutiveCalls() throws Exception {
        task.flattenpackagehierarchy("com.example.first");
        task.flattenpackagehierarchy("com.example.second");
        task.flattenpackagehierarchy("com.example.third");

        assertEquals("com/example/third", task.configuration.flattenPackageHierarchy,
                     "Last call should win");
    }

    // ============================================================
    // Android-Specific Scenarios with String Parameter
    // ============================================================

    @Test
    public void testFlattenpackagehierarchyWithString_androidReleaseWithSpecificPackage() throws Exception {
        // Android release flattening to specific package
        task.flattenpackagehierarchy("com.example.myapp.release");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("com/example/myapp/release", task.configuration.flattenPackageHierarchy,
                     "Should flatten to release package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_androidDebugWithSpecificPackage() throws Exception {
        // Android debug flattening to specific package
        task.flattenpackagehierarchy("com.example.myapp.debug");
        task.dontobfuscate();
        task.verbose();

        assertEquals("com/example/myapp/debug", task.configuration.flattenPackageHierarchy,
                     "Should flatten to debug package");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_androidLibraryInternalPackage() throws Exception {
        // Android library flattening to internal package
        task.flattenpackagehierarchy("com.example.library.impl");

        assertEquals("com/example/library/impl", task.configuration.flattenPackageHierarchy,
                     "Should flatten to library implementation package");
    }

    // ============================================================
    // Configuration State Tests with String Parameter
    // ============================================================

    @Test
    public void testFlattenpackagehierarchyWithString_preservesExistingConfiguration() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.flattenpackagehierarchy("com.example.app");

        assertEquals("com/example/app", task.configuration.flattenPackageHierarchy,
                     "Should set flattenPackageHierarchy");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_doesNotAffectOtherSettings() throws Exception {
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;

        task.flattenpackagehierarchy("com.example.app");

        assertEquals("com/example/app", task.configuration.flattenPackageHierarchy,
                     "Should set flattenPackageHierarchy");
        assertEquals(defaultShrink, task.configuration.shrink, "shrink should be unchanged");
        assertEquals(defaultOptimize, task.configuration.optimize, "optimize should be unchanged");
        assertEquals(defaultObfuscate, task.configuration.obfuscate, "obfuscate should be unchanged");
    }

    // ============================================================
    // Special Use Cases with String Parameter
    // ============================================================

    @Test
    public void testFlattenpackagehierarchyWithString_forCodeSizeOptimization() throws Exception {
        // Flatten to short package for code size optimization
        task.flattenpackagehierarchy("a");
        task.allowaccessmodification();

        assertEquals("a", task.configuration.flattenPackageHierarchy,
                     "Should flatten to minimal package for size optimization");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_forNamespaceIsolation() throws Exception {
        // Flatten to isolated namespace
        task.flattenpackagehierarchy("com.example.isolated");
        task.verbose();

        assertEquals("com/example/isolated", task.configuration.flattenPackageHierarchy,
                     "Should flatten to isolated namespace");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_forMultiModuleBuild() throws Exception {
        // Different modules can flatten to different packages
        task.flattenpackagehierarchy("com.example.module1.flat");

        assertEquals("com/example/module1/flat", task.configuration.flattenPackageHierarchy,
                     "Should flatten to module-specific package");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_forObfuscationStrategy() throws Exception {
        // Strategic flattening for obfuscation
        task.flattenpackagehierarchy("o");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("o", task.configuration.flattenPackageHierarchy,
                     "Should flatten to minimal package for obfuscation");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Comparison Tests (No-arg vs String Parameter)
    // ============================================================

    @Test
    public void testFlattenpackagehierarchyWithString_comparedToNoArg() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.flattenpackagehierarchy();
        task2.flattenpackagehierarchy("");

        assertEquals(task1.configuration.flattenPackageHierarchy,
                     task2.configuration.flattenPackageHierarchy,
                     "No-arg version should be equivalent to empty string parameter");
    }

    @Test
    public void testFlattenpackagehierarchyWithString_differentBehaviorFromNoArg() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.flattenpackagehierarchy();
        task2.flattenpackagehierarchy("com.example.app");

        assertEquals("", task1.configuration.flattenPackageHierarchy,
                     "No-arg version should set empty string");
        assertEquals("com/example/app", task2.configuration.flattenPackageHierarchy,
                     "String version should set specific package");
        assertNotEquals(task1.configuration.flattenPackageHierarchy,
                        task2.configuration.flattenPackageHierarchy,
                        "Should have different values");
    }
}

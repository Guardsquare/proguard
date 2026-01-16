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
 * Test class for ProGuardTask.repackageclasses()V method.
 * Tests the repackageclasses() void method that sets the repackageClasses
 * configuration to an empty string, repackaging all classes to the root level.
 */
public class ProGuardTaskClaude_repackageclassesTest {

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
    public void testRepackageclasses_setsRepackageClasses() throws Exception {
        assertNull(task.configuration.repackageClasses, "repackageClasses should initially be null");
        task.repackageclasses();
        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testRepackageclasses_enablesRepackagingClasses() throws Exception {
        assertNull(task.configuration.repackageClasses, "repackageClasses should be null by default");
        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses, "class repackaging should be enabled");
    }

    @Test
    public void testRepackageclasses_doesNotReturnValue() throws Exception {
        // Verify method is void and completes successfully
        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string after call");
    }

    @Test
    public void testRepackageclasses_changesConfigurationState() throws Exception {
        assertNull(task.configuration.repackageClasses, "Initial state should be null");

        task.repackageclasses();

        assertNotNull(task.configuration.repackageClasses, "State should be modified");
        assertEquals("", task.configuration.repackageClasses, "State should be empty string");
    }

    @Test
    public void testRepackageclasses_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertNull(freshTask.configuration.repackageClasses, "Fresh task should have repackageClasses as null");

        freshTask.repackageclasses();

        assertEquals("", freshTask.configuration.repackageClasses, "Should set repackageClasses to empty string");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testRepackageclasses_multipleCallsAreIdempotent() throws Exception {
        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses, "First call should set empty string");

        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses, "Second call should keep empty string");

        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses, "Third call should keep empty string");
    }

    @Test
    public void testRepackageclasses_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.repackageclasses();
            assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string after call " + (i + 1));
        }
    }

    @Test
    public void testRepackageclasses_consecutiveCalls() throws Exception {
        task.repackageclasses();
        task.repackageclasses();
        task.repackageclasses();
        task.repackageclasses();
        task.repackageclasses();

        assertEquals("", task.configuration.repackageClasses, "After 5 consecutive calls, repackageClasses should be empty string");
    }

    @Test
    public void testRepackageclasses_remainsEmptyAfterMultipleCalls() throws Exception {
        task.repackageclasses();
        String stateAfterFirst = task.configuration.repackageClasses;

        task.repackageclasses();
        String stateAfterSecond = task.configuration.repackageClasses;

        assertEquals("", stateAfterFirst, "State after first call should be empty string");
        assertEquals("", stateAfterSecond, "State after second call should be empty string");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testRepackageclasses_withDontobfuscate() throws Exception {
        task.repackageclasses();
        task.dontobfuscate();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testRepackageclasses_withDontshrink() throws Exception {
        task.repackageclasses();
        task.dontshrink();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testRepackageclasses_withDontoptimize() throws Exception {
        task.repackageclasses();
        task.dontoptimize();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testRepackageclasses_withVerbose() throws Exception {
        task.repackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_withAllowAccessModification() throws Exception {
        task.repackageclasses();
        task.allowaccessmodification();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testRepackageclasses_withOverloadAggressively() throws Exception {
        task.repackageclasses();
        task.overloadaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testRepackageclasses_withMergeInterfacesAggressively() throws Exception {
        task.repackageclasses();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testRepackageclasses_withMultipleConfigMethods() throws Exception {
        task.repackageclasses();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_withAllOptimizationSettings() throws Exception {
        task.repackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testRepackageclasses_withOptimizationPasses() throws Exception {
        task.repackageclasses();
        task.optimizationpasses(5);

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testRepackageclasses_beforeOtherConfig() throws Exception {
        task.repackageclasses();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.repackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.repackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_inComplexConfigurationChain() throws Exception {
        task.verbose();
        task.allowaccessmodification();
        task.repackageclasses();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testRepackageclasses_inObfuscationScenario() throws Exception {
        // Repackage classes during obfuscation
        task.repackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testRepackageclasses_forCodeSizeReduction() throws Exception {
        // Repackage classes for smaller code size
        task.repackageclasses();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_inProductionBuild() throws Exception {
        // Production build repackaging classes
        task.repackageclasses();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup repackaging classes
        task.repackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testRepackageclasses_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.repackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_forBetterObfuscation() throws Exception {
        // Repackage classes for better obfuscation
        task.repackageclasses();
        task.allowaccessmodification();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testRepackageclasses_forReducedMetadata() throws Exception {
        // Repackage classes to reduce metadata
        task.repackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testRepackageclasses_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testRepackageclasses_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.repackageclasses();
        task2.repackageclasses();
        task3.repackageclasses();

        assertEquals("", task1.configuration.repackageClasses, "task1 repackageClasses should be empty string");
        assertEquals("", task2.configuration.repackageClasses, "task2 repackageClasses should be empty string");
        assertEquals("", task3.configuration.repackageClasses, "task3 repackageClasses should be empty string");
    }

    @Test
    public void testRepackageclasses_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.repackageclasses();

        assertEquals("", task.configuration.repackageClasses, "task repackageClasses should be empty string");
        assertNull(otherTask.configuration.repackageClasses, "otherTask should not be affected");
    }

    @Test
    public void testRepackageclasses_afterConfigurationAccess() throws Exception {
        // Access configuration before calling repackageclasses
        assertNotNull(task.configuration, "Configuration should not be null");

        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testRepackageclasses_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses, "Should be empty string after first call");

        task.allowaccessmodification();
        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses, "Should be empty string after second call");

        task.verbose();
        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses, "Should be empty string after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testRepackageclasses_inAndroidReleaseVariant() throws Exception {
        // Android release variant repackaging classes
        task.repackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testRepackageclasses_inAndroidDebugVariant() throws Exception {
        // Android debug variant repackaging classes
        task.repackageclasses();
        task.dontobfuscate();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testRepackageclasses_inAndroidLibraryModule() throws Exception {
        // Android library repackaging classes
        task.repackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testRepackageclasses_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build repackaging classes
        task.repackageclasses();
        task.allowaccessmodification();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testRepackageclasses_forAndroidTestBuild() throws Exception {
        // Test build configuration
        task.repackageclasses();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testRepackageclasses_forAndroidInstrumentationTests() throws Exception {
        // Instrumentation test configuration
        task.repackageclasses();
        task.dontobfuscate();
        task.dontshrink();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testRepackageclasses_inProductionRelease() throws Exception {
        // Production release build repackaging classes
        task.repackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testRepackageclasses_inDevelopmentBuild() throws Exception {
        // Development build with shrinking but repackaging classes
        task.repackageclasses();
        task.dontobfuscate();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testRepackageclasses_inStagingForQa() throws Exception {
        // QA staging build repackaging classes
        task.repackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_inInternalTestingBuild() throws Exception {
        // Internal testing build
        task.repackageclasses();
        task.dontobfuscate();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_inBetaBuild() throws Exception {
        // Beta testing build repackaging classes
        task.repackageclasses();
        task.allowaccessmodification();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testRepackageclasses_inAlphaBuild() throws Exception {
        // Alpha testing build repackaging classes
        task.repackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testRepackageclasses_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.repackageClasses, "Initial state should be null");

        task.repackageclasses();

        assertNotNull(task.configuration.repackageClasses, "State should be modified");
        assertEquals("", task.configuration.repackageClasses, "State should be empty string");
    }

    @Test
    public void testRepackageclasses_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.repackageclasses();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testRepackageclasses_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.repackageclasses();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testRepackageclasses_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.repackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testRepackageclasses_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.repackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testRepackageclasses_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.repackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    @Test
    public void testRepackageclasses_onlyAffectsRepackageClassesSetting() throws Exception {
        // Capture default states
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;
        boolean defaultAllowAccessModification = task.configuration.allowAccessModification;

        task.repackageclasses();

        // Verify only repackageClasses changed
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertEquals(defaultShrink, task.configuration.shrink, "shrink should be unchanged");
        assertEquals(defaultOptimize, task.configuration.optimize, "optimize should be unchanged");
        assertEquals(defaultObfuscate, task.configuration.obfuscate, "obfuscate should be unchanged");
        assertEquals(defaultAllowAccessModification, task.configuration.allowAccessModification, "allowAccessModification should be unchanged");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testRepackageclasses_forDockerizedBuilds() throws Exception {
        // Dockerized build environment repackaging classes
        task.repackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_forCloudBuilds() throws Exception {
        // Cloud build service repackaging classes
        task.repackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_forLocalDevelopment() throws Exception {
        // Local development environment
        task.repackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.repackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclasses_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.repackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    // ============================================================
    // Class Repackaging Context Tests
    // ============================================================

    @Test
    public void testRepackageclasses_forRepackagingClasses() throws Exception {
        // Repackage all classes to root level
        task.repackageclasses();
        task.allowaccessmodification();

        assertEquals("", task.configuration.repackageClasses, "Should set repackageClasses to empty string");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testRepackageclasses_withOtherObfuscationSettings() throws Exception {
        task.repackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testRepackageclasses_forOptimalObfuscation() throws Exception {
        // Combine repackage classes with other settings
        task.repackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Performance and Build Time Scenarios
    // ============================================================

    @Test
    public void testRepackageclasses_forFastFeedbackBuilds() throws Exception {
        // Fast feedback development builds
        task.repackageclasses();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testRepackageclasses_forIncrementalBuilds() throws Exception {
        // Incremental build configuration
        task.repackageclasses();
        task.dontobfuscate();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testRepackageclasses_forFullReleaseBuilds() throws Exception {
        // Full release build with all optimizations
        task.repackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Tests for repackageclasses(String) Method
    // ============================================================

    @Test
    public void testRepackageclassesWithString_emptyString() throws Exception {
        assertNull(task.configuration.repackageClasses, "repackageClasses should initially be null");

        task.repackageclasses("");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testRepackageclassesWithString_singlePackageName() throws Exception {
        task.repackageclasses("com.example.myapp");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("com/example/myapp", task.configuration.repackageClasses,
                     "Should contain internal form of package name");
    }

    @Test
    public void testRepackageclassesWithString_nestedPackageName() throws Exception {
        task.repackageclasses("com.example.app.internal");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("com/example/app/internal", task.configuration.repackageClasses,
                     "Should convert dots to slashes");
    }

    @Test
    public void testRepackageclassesWithString_singleLevel() throws Exception {
        task.repackageclasses("myapp");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("myapp", task.configuration.repackageClasses,
                     "Should handle single level package");
    }

    @Test
    public void testRepackageclassesWithString_multipleCallsOverwrite() throws Exception {
        task.repackageclasses("com.example.app");
        assertEquals("com/example/app", task.configuration.repackageClasses,
                     "First call should set package");

        task.repackageclasses("com.example.lib");
        assertEquals("com/example/lib", task.configuration.repackageClasses,
                     "Second call should overwrite with new package");
    }

    @Test
    public void testRepackageclassesWithString_overwritesPreviousValue() throws Exception {
        task.repackageclasses("com.example.old");
        task.repackageclasses("com.example.new");

        assertEquals("com/example/new", task.configuration.repackageClasses,
                     "Should overwrite previous value");
    }

    @Test
    public void testRepackageclassesWithString_afterNoArgVersion() throws Exception {
        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses, "Should be empty after no-arg call");

        task.repackageclasses("com.example.app");
        assertEquals("com/example/app", task.configuration.repackageClasses,
                     "Should overwrite with specific package");
    }

    @Test
    public void testRepackageclassesWithString_beforeNoArgVersion() throws Exception {
        task.repackageclasses("com.example.app");
        assertEquals("com/example/app", task.configuration.repackageClasses,
                     "Should have specific package");

        task.repackageclasses();
        assertEquals("", task.configuration.repackageClasses,
                     "No-arg version should overwrite with empty string");
    }

    @Test
    public void testRepackageclassesWithString_veryLongPackageName() throws Exception {
        task.repackageclasses("com.example.very.long.package.name.hierarchy.level.deep");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("com/example/very/long/package/name/hierarchy/level/deep",
                     task.configuration.repackageClasses,
                     "Should handle long package names");
    }

    @Test
    public void testRepackageclassesWithString_singleCharacterPackage() throws Exception {
        task.repackageclasses("a.b.c");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("a/b/c", task.configuration.repackageClasses,
                     "Should handle single character packages");
    }

    @Test
    public void testRepackageclassesWithString_numericPackageNames() throws Exception {
        task.repackageclasses("com.example.v2.api");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("com/example/v2/api", task.configuration.repackageClasses,
                     "Should handle numeric package components");
    }

    @Test
    public void testRepackageclassesWithString_underscoreInPackage() throws Exception {
        task.repackageclasses("com.example.my_app");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("com/example/my_app", task.configuration.repackageClasses,
                     "Should handle underscores in package names");
    }

    @Test
    public void testRepackageclassesWithString_dollarSignInPackage() throws Exception {
        task.repackageclasses("com.example.app$inner");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("com/example/app$inner", task.configuration.repackageClasses,
                     "Should handle dollar signs in package names");
    }

    // ============================================================
    // Integration Tests with String Parameter
    // ============================================================

    @Test
    public void testRepackageclassesWithString_withObfuscation() throws Exception {
        task.repackageclasses("com.example.repack");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("com/example/repack", task.configuration.repackageClasses,
                     "Should have specific package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testRepackageclassesWithString_withVerbose() throws Exception {
        task.repackageclasses("com.example.repack");
        task.verbose();

        assertEquals("com/example/repack", task.configuration.repackageClasses,
                     "Should have specific package");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclassesWithString_withDontobfuscate() throws Exception {
        task.repackageclasses("com.example.repack");
        task.dontobfuscate();

        assertEquals("com/example/repack", task.configuration.repackageClasses,
                     "Should have specific package");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    // ============================================================
    // Realistic Scenarios with String Parameter
    // ============================================================

    @Test
    public void testRepackageclassesWithString_forSpecificTargetPackage() throws Exception {
        // Repackage all classes into a specific target package
        task.repackageclasses("com.example.obfuscated");
        task.allowaccessmodification();

        assertEquals("com/example/obfuscated", task.configuration.repackageClasses,
                     "Should repackage to specific target package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testRepackageclassesWithString_forAndroidApp() throws Exception {
        // Android app repackaging to specific package
        task.repackageclasses("com.example.myapp.repackaged");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("com/example/myapp/repackaged", task.configuration.repackageClasses,
                     "Should repackage to app-specific package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testRepackageclassesWithString_forLibrary() throws Exception {
        // Library repackaging to specific package
        task.repackageclasses("com.example.lib.internal");

        assertEquals("com/example/lib/internal", task.configuration.repackageClasses,
                     "Should repackage to library internal package");
    }

    @Test
    public void testRepackageclassesWithString_forObfuscatedPackage() throws Exception {
        // Repackage to short obfuscated package name
        task.repackageclasses("a.b");
        task.allowaccessmodification();

        assertEquals("a/b", task.configuration.repackageClasses,
                     "Should repackage to short obfuscated package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testRepackageclassesWithString_forSingleLetterPackage() throws Exception {
        // Repackage to minimal single-letter package
        task.repackageclasses("x");
        task.allowaccessmodification();

        assertEquals("x", task.configuration.repackageClasses,
                     "Should repackage to single-letter package");
    }

    @Test
    public void testRepackageclassesWithString_forProductionBuild() throws Exception {
        // Production build with specific repackaging target
        task.repackageclasses("com.example.prod");
        task.allowaccessmodification();
        task.verbose();

        assertEquals("com/example/prod", task.configuration.repackageClasses,
                     "Should repackage to production package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Edge Cases with String Parameter
    // ============================================================

    @Test
    public void testRepackageclassesWithString_multipleDots() throws Exception {
        task.repackageclasses("com.example..app");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("com/example//app", task.configuration.repackageClasses,
                     "Should convert multiple dots to multiple slashes");
    }

    @Test
    public void testRepackageclassesWithString_trailingDot() throws Exception {
        task.repackageclasses("com.example.app.");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("com/example/app/", task.configuration.repackageClasses,
                     "Should handle trailing dot");
    }

    @Test
    public void testRepackageclassesWithString_leadingDot() throws Exception {
        task.repackageclasses(".com.example.app");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("/com/example/app", task.configuration.repackageClasses,
                     "Should handle leading dot");
    }

    @Test
    public void testRepackageclassesWithString_mixedCase() throws Exception {
        task.repackageclasses("com.Example.MyApp");

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("com/Example/MyApp", task.configuration.repackageClasses,
                     "Should preserve case in package names");
    }

    @Test
    public void testRepackageclassesWithString_consecutiveCalls() throws Exception {
        task.repackageclasses("com.example.first");
        task.repackageclasses("com.example.second");
        task.repackageclasses("com.example.third");

        assertEquals("com/example/third", task.configuration.repackageClasses,
                     "Last call should win");
    }

    // ============================================================
    // Android-Specific Scenarios with String Parameter
    // ============================================================

    @Test
    public void testRepackageclassesWithString_androidReleaseWithSpecificPackage() throws Exception {
        // Android release repackaging to specific package
        task.repackageclasses("com.example.myapp.release");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("com/example/myapp/release", task.configuration.repackageClasses,
                     "Should repackage to release package");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testRepackageclassesWithString_androidDebugWithSpecificPackage() throws Exception {
        // Android debug repackaging to specific package
        task.repackageclasses("com.example.myapp.debug");
        task.dontobfuscate();
        task.verbose();

        assertEquals("com/example/myapp/debug", task.configuration.repackageClasses,
                     "Should repackage to debug package");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testRepackageclassesWithString_androidLibraryInternalPackage() throws Exception {
        // Android library repackaging to internal package
        task.repackageclasses("com.example.library.impl");

        assertEquals("com/example/library/impl", task.configuration.repackageClasses,
                     "Should repackage to library implementation package");
    }

    // ============================================================
    // Configuration State Tests with String Parameter
    // ============================================================

    @Test
    public void testRepackageclassesWithString_preservesExistingConfiguration() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.repackageclasses("com.example.app");

        assertEquals("com/example/app", task.configuration.repackageClasses,
                     "Should set repackageClasses");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
    }

    @Test
    public void testRepackageclassesWithString_doesNotAffectOtherSettings() throws Exception {
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;

        task.repackageclasses("com.example.app");

        assertEquals("com/example/app", task.configuration.repackageClasses,
                     "Should set repackageClasses");
        assertEquals(defaultShrink, task.configuration.shrink, "shrink should be unchanged");
        assertEquals(defaultOptimize, task.configuration.optimize, "optimize should be unchanged");
        assertEquals(defaultObfuscate, task.configuration.obfuscate, "obfuscate should be unchanged");
    }

    // ============================================================
    // Special Use Cases with String Parameter
    // ============================================================

    @Test
    public void testRepackageclassesWithString_forCodeSizeOptimization() throws Exception {
        // Repackage to short package for code size optimization
        task.repackageclasses("a");
        task.allowaccessmodification();

        assertEquals("a", task.configuration.repackageClasses,
                     "Should repackage to minimal package for size optimization");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testRepackageclassesWithString_forNamespaceIsolation() throws Exception {
        // Repackage to isolated namespace
        task.repackageclasses("com.example.isolated");
        task.verbose();

        assertEquals("com/example/isolated", task.configuration.repackageClasses,
                     "Should repackage to isolated namespace");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testRepackageclassesWithString_forMultiModuleBuild() throws Exception {
        // Different modules can repackage to different packages
        task.repackageclasses("com.example.module1.repack");

        assertEquals("com/example/module1/repack", task.configuration.repackageClasses,
                     "Should repackage to module-specific package");
    }

    @Test
    public void testRepackageclassesWithString_forObfuscationStrategy() throws Exception {
        // Strategic repackaging for obfuscation
        task.repackageclasses("o");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("o", task.configuration.repackageClasses,
                     "Should repackage to minimal package for obfuscation");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Comparison Tests (No-arg vs String Parameter)
    // ============================================================

    @Test
    public void testRepackageclassesWithString_comparedToNoArg() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.repackageclasses();
        task2.repackageclasses("");

        assertEquals(task1.configuration.repackageClasses,
                     task2.configuration.repackageClasses,
                     "No-arg version should be equivalent to empty string parameter");
    }

    @Test
    public void testRepackageclassesWithString_differentBehaviorFromNoArg() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);

        task1.repackageclasses();
        task2.repackageclasses("com.example.app");

        assertEquals("", task1.configuration.repackageClasses,
                     "No-arg version should set empty string");
        assertEquals("com/example/app", task2.configuration.repackageClasses,
                     "String version should set specific package");
        assertNotEquals(task1.configuration.repackageClasses,
                        task2.configuration.repackageClasses,
                        "Should have different values");
    }
}

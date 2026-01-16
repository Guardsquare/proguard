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
 * Test class for ProGuardTask.keeppackagenames()V method.
 * Tests the keeppackagenames() void method that initializes the keepPackageNames filter,
 * keeping all package names from being obfuscated.
 */
public class ProGuardTaskClaude_keeppackagenamesTest {

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
    public void testKeeppackagenames_initializesKeepPackageNames() throws Exception {
        assertNull(task.configuration.keepPackageNames, "keepPackageNames should initially be null");
        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testKeeppackagenames_enablesKeepingPackageNames() throws Exception {
        assertNull(task.configuration.keepPackageNames, "keepPackageNames should be null by default");
        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "package name keeping should be enabled");
    }

    @Test
    public void testKeeppackagenames_doesNotReturnValue() throws Exception {
        // Verify method is void and completes successfully
        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized after call");
    }

    @Test
    public void testKeeppackagenames_changesConfigurationState() throws Exception {
        assertNull(task.configuration.keepPackageNames, "Initial state should be null");

        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "State should be modified");
    }

    @Test
    public void testKeeppackagenames_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertNull(freshTask.configuration.keepPackageNames, "Fresh task should have keepPackageNames as null");

        freshTask.keeppackagenames();

        assertNotNull(freshTask.configuration.keepPackageNames, "Should initialize keepPackageNames");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testKeeppackagenames_multipleCallsAreIdempotent() throws Exception {
        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "First call should initialize keepPackageNames");

        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "Second call should keep keepPackageNames initialized");

        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "Third call should keep keepPackageNames initialized");
    }

    @Test
    public void testKeeppackagenames_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.keeppackagenames();
            assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized after call " + (i + 1));
        }
    }

    @Test
    public void testKeeppackagenames_consecutiveCalls() throws Exception {
        task.keeppackagenames();
        task.keeppackagenames();
        task.keeppackagenames();
        task.keeppackagenames();
        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "After 5 consecutive calls, keepPackageNames should be initialized");
    }

    @Test
    public void testKeeppackagenames_remainsInitializedAfterMultipleCalls() throws Exception {
        task.keeppackagenames();
        Object stateAfterFirst = task.configuration.keepPackageNames;

        task.keeppackagenames();
        Object stateAfterSecond = task.configuration.keepPackageNames;

        assertNotNull(stateAfterFirst, "State after first call should be initialized");
        assertNotNull(stateAfterSecond, "State after second call should remain initialized");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testKeeppackagenames_withDontobfuscate() throws Exception {
        task.keeppackagenames();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testKeeppackagenames_withDontshrink() throws Exception {
        task.keeppackagenames();
        task.dontshrink();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testKeeppackagenames_withDontoptimize() throws Exception {
        task.keeppackagenames();
        task.dontoptimize();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testKeeppackagenames_withVerbose() throws Exception {
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_withAllowAccessModification() throws Exception {
        task.keeppackagenames();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testKeeppackagenames_withOverloadAggressively() throws Exception {
        task.keeppackagenames();
        task.overloadaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testKeeppackagenames_withMergeInterfacesAggressively() throws Exception {
        task.keeppackagenames();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeeppackagenames_withMultipleConfigMethods() throws Exception {
        task.keeppackagenames();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_withAllOptimizationSettings() throws Exception {
        task.keeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeeppackagenames_withOptimizationPasses() throws Exception {
        task.keeppackagenames();
        task.optimizationpasses(5);

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testKeeppackagenames_beforeOtherConfig() throws Exception {
        task.keeppackagenames();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_inComplexConfigurationChain() throws Exception {
        task.verbose();
        task.allowaccessmodification();
        task.keeppackagenames();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testKeeppackagenames_inObfuscationScenario() throws Exception {
        // Keep all package names during obfuscation
        task.keeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeeppackagenames_forDebugging() throws Exception {
        // Keep package names for easier debugging
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_inProductionBuild() throws Exception {
        // Production build keeping package names
        task.keeppackagenames();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup keeping package names
        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testKeeppackagenames_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.keeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_forPublicApiLibrary() throws Exception {
        // Public API library keeping package names
        task.keeppackagenames();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testKeeppackagenames_forBetterStackTraces() throws Exception {
        // Keep package names for better stack traces
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testKeeppackagenames_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testKeeppackagenames_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.keeppackagenames();
        task2.keeppackagenames();
        task3.keeppackagenames();

        assertNotNull(task1.configuration.keepPackageNames, "task1 keepPackageNames should be initialized");
        assertNotNull(task2.configuration.keepPackageNames, "task2 keepPackageNames should be initialized");
        assertNotNull(task3.configuration.keepPackageNames, "task3 keepPackageNames should be initialized");
    }

    @Test
    public void testKeeppackagenames_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "task keepPackageNames should be initialized");
        assertNull(otherTask.configuration.keepPackageNames, "otherTask should not be affected");
    }

    @Test
    public void testKeeppackagenames_afterConfigurationAccess() throws Exception {
        // Access configuration before calling keeppackagenames
        assertNotNull(task.configuration, "Configuration should not be null");

        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testKeeppackagenames_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "Should be initialized after first call");

        task.allowaccessmodification();
        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "Should remain initialized after second call");

        task.verbose();
        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "Should remain initialized after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testKeeppackagenames_inAndroidReleaseVariant() throws Exception {
        // Android release variant keeping package names
        task.keeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeeppackagenames_inAndroidDebugVariant() throws Exception {
        // Android debug variant keeping package names
        task.keeppackagenames();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testKeeppackagenames_inAndroidLibraryModule() throws Exception {
        // Android library keeping package names
        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testKeeppackagenames_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build keeping package names
        task.keeppackagenames();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testKeeppackagenames_forAndroidTestBuild() throws Exception {
        // Test build configuration
        task.keeppackagenames();
        task.dontobfuscate();
        task.dontoptimize();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testKeeppackagenames_forAndroidInstrumentationTests() throws Exception {
        // Instrumentation test configuration
        task.keeppackagenames();
        task.dontobfuscate();
        task.dontshrink();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testKeeppackagenames_inProductionRelease() throws Exception {
        // Production release build keeping package names
        task.keeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeeppackagenames_inDevelopmentBuild() throws Exception {
        // Development build with shrinking but keeping package names
        task.keeppackagenames();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testKeeppackagenames_inStagingForQa() throws Exception {
        // QA staging build keeping package names
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_inInternalTestingBuild() throws Exception {
        // Internal testing build
        task.keeppackagenames();
        task.dontobfuscate();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_inBetaBuild() throws Exception {
        // Beta testing build keeping package names
        task.keeppackagenames();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testKeeppackagenames_inAlphaBuild() throws Exception {
        // Alpha testing build keeping package names
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testKeeppackagenames_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.keepPackageNames, "Initial state should be null");

        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "State should be modified");
    }

    @Test
    public void testKeeppackagenames_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.keeppackagenames();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    @Test
    public void testKeeppackagenames_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.keeppackagenames();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeeppackagenames_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testKeeppackagenames_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testKeeppackagenames_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    @Test
    public void testKeeppackagenames_onlyAffectsKeepPackageNamesSetting() throws Exception {
        // Capture default states
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;
        boolean defaultAllowAccessModification = task.configuration.allowAccessModification;

        task.keeppackagenames();

        // Verify only keepPackageNames changed
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertEquals(defaultShrink, task.configuration.shrink, "shrink should be unchanged");
        assertEquals(defaultOptimize, task.configuration.optimize, "optimize should be unchanged");
        assertEquals(defaultObfuscate, task.configuration.obfuscate, "obfuscate should be unchanged");
        assertEquals(defaultAllowAccessModification, task.configuration.allowAccessModification, "allowAccessModification should be unchanged");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testKeeppackagenames_forDockerizedBuilds() throws Exception {
        // Dockerized build environment keeping package names
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_forCloudBuilds() throws Exception {
        // Cloud build service keeping package names
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_forLocalDevelopment() throws Exception {
        // Local development environment
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
    }

    // ============================================================
    // Package Name Preservation Context Tests
    // ============================================================

    @Test
    public void testKeeppackagenames_forKeepingPackageNames() throws Exception {
        // Keep all package names from being obfuscated
        task.keeppackagenames();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "Should initialize keepPackageNames");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testKeeppackagenames_withOtherObfuscationSettings() throws Exception {
        task.keeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeeppackagenames_forOptimalObfuscation() throws Exception {
        // Combine keep package names with other settings
        task.keeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Performance and Build Time Scenarios
    // ============================================================

    @Test
    public void testKeeppackagenames_forFastFeedbackBuilds() throws Exception {
        // Fast feedback development builds
        task.keeppackagenames();
        task.dontobfuscate();
        task.dontoptimize();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testKeeppackagenames_forIncrementalBuilds() throws Exception {
        // Incremental build configuration
        task.keeppackagenames();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testKeeppackagenames_forFullReleaseBuilds() throws Exception {
        // Full release build with all optimizations
        task.keeppackagenames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Compatibility Scenarios
    // ============================================================

    @Test
    public void testKeeppackagenames_withReflectionHeavyCode() throws Exception {
        // Code using heavy reflection
        task.keeppackagenames();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for reflection compatibility");
    }

    @Test
    public void testKeeppackagenames_withJniCode() throws Exception {
        // JNI code requiring package names
        task.keeppackagenames();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for JNI compatibility");
    }

    @Test
    public void testKeeppackagenames_withSerializableClasses() throws Exception {
        // Serializable classes configuration
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_withPublicApiLibrary() throws Exception {
        // Public API library
        task.keeppackagenames();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for public API");
    }

    // ============================================================
    // Debugging and Troubleshooting Tests
    // ============================================================

    @Test
    public void testKeeppackagenames_forEasierStackTraces() throws Exception {
        // Keeping package names makes stack traces more readable
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_forCrashReporting() throws Exception {
        // Keep package names for better crash reports
        task.keeppackagenames();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized for crash reporting");
    }

    @Test
    public void testKeeppackagenames_forProfilingAndAnalytics() throws Exception {
        // Keep package names for profiling and analytics
        task.keeppackagenames();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenames_forDebuggingObfuscatedCode() throws Exception {
        // Debugging obfuscated code with package names preserved
        task.keeppackagenames();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Tests for keeppackagenames(String filter) Method
    // ============================================================

    @Test
    public void testKeeppackagenamesWithFilter_nullFilterClearsAndKeepsAll() throws Exception {
        assertNull(task.configuration.keepPackageNames, "keepPackageNames should initially be null");

        task.keeppackagenames(null);

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.isEmpty(), "keepPackageNames should be empty (keeps all)");
    }

    @Test
    public void testKeeppackagenamesWithFilter_singlePackageName() throws Exception {
        task.keeppackagenames("com.example.myapp");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertFalse(task.configuration.keepPackageNames.isEmpty(), "keepPackageNames should contain filter");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/myapp"),
                   "Should contain internal form of package name");
    }

    @Test
    public void testKeeppackagenamesWithFilter_wildcardPackageName() throws Exception {
        task.keeppackagenames("com.example.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/**"),
                   "Should contain wildcard pattern");
    }

    @Test
    public void testKeeppackagenamesWithFilter_singleWildcard() throws Exception {
        task.keeppackagenames("com.example.*");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/*"),
                   "Should contain single wildcard pattern");
    }

    @Test
    public void testKeeppackagenamesWithFilter_multiplePackagesCommaSeparated() throws Exception {
        task.keeppackagenames("com.example.app,com.example.lib");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/app"),
                   "Should contain first package");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/lib"),
                   "Should contain second package");
    }

    @Test
    public void testKeeppackagenamesWithFilter_multipleCalls() throws Exception {
        task.keeppackagenames("com.example.app");
        task.keeppackagenames("com.example.lib");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/app"),
                   "Should contain first package");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/lib"),
                   "Should contain second package");
    }

    @Test
    public void testKeeppackagenamesWithFilter_emptyStringFilter() throws Exception {
        task.keeppackagenames("");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains(""),
                   "Should contain empty string filter");
    }

    @Test
    public void testKeeppackagenamesWithFilter_rootPackageWildcard() throws Exception {
        task.keeppackagenames("**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("**"),
                   "Should contain root wildcard");
    }

    @Test
    public void testKeeppackagenamesWithFilter_complexWildcardPattern() throws Exception {
        task.keeppackagenames("com.*.example.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/*/example/**"),
                   "Should contain complex wildcard pattern");
    }

    @Test
    public void testKeeppackagenamesWithFilter_negationPattern() throws Exception {
        task.keeppackagenames("!com.example.internal.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("!com/example/internal/**"),
                   "Should contain negation pattern");
    }

    @Test
    public void testKeeppackagenamesWithFilter_mixedWithNoArgVersion() throws Exception {
        task.keeppackagenames();
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.isEmpty(), "Should be empty after no-arg call");

        task.keeppackagenames("com.example.app");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/app"),
                   "Should contain filter after string call");
    }

    @Test
    public void testKeeppackagenamesWithFilter_afterNoArgVersionAddsFilter() throws Exception {
        task.keeppackagenames();
        task.keeppackagenames("com.example.app");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/app"),
                   "Should contain filter");
    }

    @Test
    public void testKeeppackagenamesWithFilter_nullThenSpecific() throws Exception {
        task.keeppackagenames(null);
        assertTrue(task.configuration.keepPackageNames.isEmpty(), "Should be empty after null");

        task.keeppackagenames("com.example.app");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/app"),
                   "Should contain specific filter");
    }

    @Test
    public void testKeeppackagenamesWithFilter_specificThenNull() throws Exception {
        task.keeppackagenames("com.example.app");
        assertFalse(task.configuration.keepPackageNames.isEmpty(), "Should have filter");

        task.keeppackagenames(null);
        assertTrue(task.configuration.keepPackageNames.isEmpty(), "Should be cleared by null");
    }

    @Test
    public void testKeeppackagenamesWithFilter_accumulatesFilters() throws Exception {
        task.keeppackagenames("com.example.app");
        assertEquals(1, task.configuration.keepPackageNames.size(), "Should have 1 filter");

        task.keeppackagenames("com.example.lib");
        assertEquals(2, task.configuration.keepPackageNames.size(), "Should have 2 filters");

        task.keeppackagenames("com.example.util");
        assertEquals(3, task.configuration.keepPackageNames.size(), "Should have 3 filters");
    }

    @Test
    public void testKeeppackagenamesWithFilter_multipleWildcardPatterns() throws Exception {
        task.keeppackagenames("com.example.**,org.sample.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/**"),
                   "Should contain first wildcard");
        assertTrue(task.configuration.keepPackageNames.contains("org/sample/**"),
                   "Should contain second wildcard");
    }

    // ============================================================
    // Integration Tests with String Filter
    // ============================================================

    @Test
    public void testKeeppackagenamesWithFilter_withObfuscation() throws Exception {
        task.keeppackagenames("com.example.app");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/app"),
                   "Should contain filter");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeeppackagenamesWithFilter_withVerbose() throws Exception {
        task.keeppackagenames("com.example.**");
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/**"),
                   "Should contain wildcard filter");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeeppackagenamesWithFilter_withDontobfuscate() throws Exception {
        task.keeppackagenames("com.example.app");
        task.dontobfuscate();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/app"),
                   "Should contain filter");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    // ============================================================
    // Realistic Scenarios with String Filter
    // ============================================================

    @Test
    public void testKeeppackagenamesWithFilter_forSpecificModule() throws Exception {
        // Keep only specific module package names
        task.keeppackagenames("com.example.publicapi.**");
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/publicapi/**"),
                   "Should keep public API package names");
    }

    @Test
    public void testKeeppackagenamesWithFilter_excludingInternal() throws Exception {
        // Keep package names but exclude internal packages
        task.keeppackagenames("com.example.**");
        task.keeppackagenames("!com.example.internal.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/**"),
                   "Should include base package");
        assertTrue(task.configuration.keepPackageNames.contains("!com/example/internal/**"),
                   "Should exclude internal package");
    }

    @Test
    public void testKeeppackagenamesWithFilter_forAndroidApp() throws Exception {
        // Android app keeping specific package names
        task.keeppackagenames("com.example.myapp.**");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/myapp/**"),
                   "Should keep app package names");
    }

    @Test
    public void testKeeppackagenamesWithFilter_forLibraryPublicApi() throws Exception {
        // Library keeping only public API package names
        task.keeppackagenames("com.example.lib.api.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/lib/api/**"),
                   "Should keep public API package names");
    }

    @Test
    public void testKeeppackagenamesWithFilter_multipleModules() throws Exception {
        // Multi-module project
        task.keeppackagenames("com.example.module1.**,com.example.module2.**,com.example.module3.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/module1/**"),
                   "Should keep module1 packages");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/module2/**"),
                   "Should keep module2 packages");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/module3/**"),
                   "Should keep module3 packages");
    }

    @Test
    public void testKeeppackagenamesWithFilter_forThirdPartyIntegration() throws Exception {
        // Keeping packages for third-party SDK integration
        task.keeppackagenames("com.example.sdk.**");
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/sdk/**"),
                   "Should keep SDK package names");
    }

    // ============================================================
    // Edge Cases with String Filter
    // ============================================================

    @Test
    public void testKeeppackagenamesWithFilter_veryLongPackageName() throws Exception {
        task.keeppackagenames("com.example.very.long.package.name.hierarchy.level.deep.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/very/long/package/name/hierarchy/level/deep/**"),
                   "Should handle long package names");
    }

    @Test
    public void testKeeppackagenamesWithFilter_singleCharacterPackage() throws Exception {
        task.keeppackagenames("a.b.c");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("a/b/c"),
                   "Should handle single character packages");
    }

    @Test
    public void testKeeppackagenamesWithFilter_numericPackageNames() throws Exception {
        task.keeppackagenames("com.example.v2.api");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/v2/api"),
                   "Should handle numeric package components");
    }

    @Test
    public void testKeeppackagenamesWithFilter_underscoreInPackage() throws Exception {
        task.keeppackagenames("com.example.my_app.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/my_app/**"),
                   "Should handle underscores in package names");
    }

    @Test
    public void testKeeppackagenamesWithFilter_multipleClearsCalls() throws Exception {
        task.keeppackagenames("com.example.app");
        assertEquals(1, task.configuration.keepPackageNames.size(), "Should have 1 filter");

        task.keeppackagenames(null);
        assertEquals(0, task.configuration.keepPackageNames.size(), "Should be cleared");

        task.keeppackagenames("com.example.lib");
        assertEquals(1, task.configuration.keepPackageNames.size(), "Should have 1 filter again");
    }

    @Test
    public void testKeeppackagenamesWithFilter_duplicateFilters() throws Exception {
        task.keeppackagenames("com.example.app");
        task.keeppackagenames("com.example.app");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        // Note: duplicates are allowed by the implementation
        assertTrue(task.configuration.keepPackageNames.contains("com/example/app"),
                   "Should contain the filter");
    }

    // ============================================================
    // Android-Specific Scenarios with String Filter
    // ============================================================

    @Test
    public void testKeeppackagenamesWithFilter_androidReleaseWithSpecificPackages() throws Exception {
        // Android release keeping specific package names
        task.keeppackagenames("com.example.myapp.**,com.example.myapp.models.**");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/myapp/**"),
                   "Should keep app packages");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testKeeppackagenamesWithFilter_androidDebugWithAllPackages() throws Exception {
        // Android debug keeping all package names
        task.keeppackagenames(null);
        task.dontobfuscate();
        task.verbose();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.isEmpty(), "Should keep all packages");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testKeeppackagenamesWithFilter_androidLibraryPublicPackages() throws Exception {
        // Android library keeping public packages
        task.keeppackagenames("com.example.library.public.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/library/public/**"),
                   "Should keep public library packages");
    }

    // ============================================================
    // Configuration State Tests with String Filter
    // ============================================================

    @Test
    public void testKeeppackagenamesWithFilter_preservesExistingConfiguration() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.keeppackagenames("com.example.app");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
    }

    @Test
    public void testKeeppackagenamesWithFilter_doesNotAffectOtherSettings() throws Exception {
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;

        task.keeppackagenames("com.example.app");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertEquals(defaultShrink, task.configuration.shrink, "shrink should be unchanged");
        assertEquals(defaultOptimize, task.configuration.optimize, "optimize should be unchanged");
        assertEquals(defaultObfuscate, task.configuration.obfuscate, "obfuscate should be unchanged");
    }

    // ============================================================
    // Special Use Cases with String Filter
    // ============================================================

    @Test
    public void testKeeppackagenamesWithFilter_forCrashReportingPackages() throws Exception {
        // Keep specific packages for better crash reports
        task.keeppackagenames("com.example.app.**,com.example.app.ui.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/app/**"),
                   "Should keep app packages for crash reporting");
    }

    @Test
    public void testKeeppackagenamesWithFilter_forReflectionHeavyPackages() throws Exception {
        // Keep packages that use heavy reflection
        task.keeppackagenames("com.example.app.reflection.**");
        task.dontobfuscate();

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/app/reflection/**"),
                   "Should keep reflection packages");
    }

    @Test
    public void testKeeppackagenamesWithFilter_forSerializablePackages() throws Exception {
        // Keep packages with serializable classes
        task.keeppackagenames("com.example.app.model.**");

        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be initialized");
        assertTrue(task.configuration.keepPackageNames.contains("com/example/app/model/**"),
                   "Should keep model packages");
    }
}

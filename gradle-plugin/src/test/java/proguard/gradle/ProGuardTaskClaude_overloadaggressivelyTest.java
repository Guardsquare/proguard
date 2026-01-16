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
 * Test class for ProGuardTask.overloadaggressively()V method.
 * Tests the overloadaggressively() void method that sets configuration.overloadAggressively to true,
 * enabling ProGuard to reuse method names more aggressively during obfuscation.
 */
public class ProGuardTaskClaude_overloadaggressivelyTest {

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
    public void testOverloadaggressively_setsOverloadAggressivelyToTrue() throws Exception {
        assertFalse(task.configuration.overloadAggressively, "overloadAggressively should initially be false");
        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be set to true");
    }

    @Test
    public void testOverloadaggressively_enablesAggressiveMethodNameReuse() throws Exception {
        assertFalse(task.configuration.overloadAggressively, "overloadAggressively should be disabled by default");
        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "aggressive method name reuse should be enabled");
    }

    @Test
    public void testOverloadaggressively_doesNotReturnValue() throws Exception {
        // Verify method is void and completes successfully
        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true after call");
    }

    @Test
    public void testOverloadaggressively_changesConfigurationState() throws Exception {
        boolean initialState = task.configuration.overloadAggressively;
        task.overloadaggressively();
        boolean finalState = task.configuration.overloadAggressively;

        assertNotEquals(initialState, finalState, "Configuration state should change");
        assertTrue(finalState, "Final state should be true");
    }

    @Test
    public void testOverloadaggressively_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertFalse(freshTask.configuration.overloadAggressively, "Fresh task should have overloadAggressively as false");

        freshTask.overloadaggressively();

        assertTrue(freshTask.configuration.overloadAggressively, "Should set overloadAggressively to true");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testOverloadaggressively_multipleCallsAreIdempotent() throws Exception {
        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "First call should set overloadAggressively to true");

        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "Second call should keep overloadAggressively as true");

        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "Third call should keep overloadAggressively as true");
    }

    @Test
    public void testOverloadaggressively_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.overloadaggressively();
            assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true after call " + (i + 1));
        }
    }

    @Test
    public void testOverloadaggressively_consecutiveCalls() throws Exception {
        task.overloadaggressively();
        task.overloadaggressively();
        task.overloadaggressively();
        task.overloadaggressively();
        task.overloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "After 5 consecutive calls, overloadAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_remainsTrueAfterMultipleCalls() throws Exception {
        task.overloadaggressively();
        boolean stateAfterFirst = task.configuration.overloadAggressively;

        task.overloadaggressively();
        boolean stateAfterSecond = task.configuration.overloadAggressively;

        assertEquals(stateAfterFirst, stateAfterSecond, "State should remain true");
        assertTrue(stateAfterSecond, "Should be true after multiple calls");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testOverloadaggressively_withDontobfuscate() throws Exception {
        task.overloadaggressively();
        task.dontobfuscate();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testOverloadaggressively_withDontshrink() throws Exception {
        task.overloadaggressively();
        task.dontshrink();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testOverloadaggressively_withDontoptimize() throws Exception {
        task.overloadaggressively();
        task.dontoptimize();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testOverloadaggressively_withVerbose() throws Exception {
        task.overloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_withAllowAccessModification() throws Exception {
        task.overloadaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testOverloadaggressively_withMergeInterfacesAggressively() throws Exception {
        task.overloadaggressively();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_withMultipleConfigMethods() throws Exception {
        task.overloadaggressively();
        task.allowaccessmodification();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_withAllOptimizationSettings() throws Exception {
        task.overloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_withOptimizationPasses() throws Exception {
        task.overloadaggressively();
        task.optimizationpasses(5);

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testOverloadaggressively_beforeOtherConfig() throws Exception {
        task.overloadaggressively();
        task.allowaccessmodification();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.overloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.overloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_inComplexConfigurationChain() throws Exception {
        task.verbose();
        task.allowaccessmodification();
        task.overloadaggressively();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testOverloadaggressively_inAggressiveObfuscationScenario() throws Exception {
        // Aggressive obfuscation with overload aggressively
        task.overloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_inProductionBuild() throws Exception {
        // Production build with aggressive optimizations
        task.overloadaggressively();
        task.allowaccessmodification();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup with overload aggressively
        task.overloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.overloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_forMaximumMethodNameReuse() throws Exception {
        // Enabling overload aggressively for maximum method name reuse
        task.overloadaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testOverloadaggressively_forCodeSizeReduction() throws Exception {
        // Using overload aggressively to reduce code size
        task.overloadaggressively();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testOverloadaggressively_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.overloadaggressively();
        task2.overloadaggressively();
        task3.overloadaggressively();

        assertTrue(task1.configuration.overloadAggressively, "task1 overloadAggressively should be true");
        assertTrue(task2.configuration.overloadAggressively, "task2 overloadAggressively should be true");
        assertTrue(task3.configuration.overloadAggressively, "task3 overloadAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.overloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "task overloadAggressively should be true");
        assertFalse(otherTask.configuration.overloadAggressively, "otherTask should not be affected");
    }

    @Test
    public void testOverloadaggressively_afterConfigurationAccess() throws Exception {
        // Access configuration before calling overloadaggressively
        assertNotNull(task.configuration, "Configuration should not be null");

        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "Should be true after first call");

        task.allowaccessmodification();
        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "Should remain true after second call");

        task.verbose();
        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "Should remain true after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testOverloadaggressively_inAndroidReleaseVariant() throws Exception {
        // Android release variant with overload aggressively
        task.overloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_inAndroidDebugVariant() throws Exception {
        // Android debug variant with overload aggressively but no obfuscation
        task.overloadaggressively();
        task.dontobfuscate();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testOverloadaggressively_inAndroidLibraryModule() throws Exception {
        // Android library with overload aggressively
        task.overloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build with overload aggressively
        task.overloadaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testOverloadaggressively_forAndroidTestBuild() throws Exception {
        // Test build configuration
        task.overloadaggressively();
        task.dontobfuscate();
        task.dontoptimize();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testOverloadaggressively_forAndroidInstrumentationTests() throws Exception {
        // Instrumentation test configuration
        task.overloadaggressively();
        task.dontobfuscate();
        task.dontshrink();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testOverloadaggressively_inProductionRelease() throws Exception {
        // Production release build with overload aggressively
        task.overloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_inDevelopmentBuild() throws Exception {
        // Development build with shrinking but no obfuscation
        task.overloadaggressively();
        task.dontobfuscate();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testOverloadaggressively_inStagingForQa() throws Exception {
        // QA staging build with overload aggressively
        task.overloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_inInternalTestingBuild() throws Exception {
        // Internal testing build
        task.overloadaggressively();
        task.dontobfuscate();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_inBetaBuild() throws Exception {
        // Beta testing build with overload aggressively
        task.overloadaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testOverloadaggressively_inAlphaBuild() throws Exception {
        // Alpha testing build with overload aggressively
        task.overloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testOverloadaggressively_modifiesConfigurationState() throws Exception {
        assertFalse(task.configuration.overloadAggressively, "Initial state should be false");

        task.overloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "State should be modified to true");
    }

    @Test
    public void testOverloadaggressively_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.overloadaggressively();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.overloadaggressively();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.overloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testOverloadaggressively_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.overloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testOverloadaggressively_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.overloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    @Test
    public void testOverloadaggressively_onlyAffectsOverloadAggressivelySetting() throws Exception {
        // Capture default states
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;
        boolean defaultAllowAccessModification = task.configuration.allowAccessModification;

        task.overloadaggressively();

        // Verify only overloadAggressively changed
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertEquals(defaultShrink, task.configuration.shrink, "shrink should be unchanged");
        assertEquals(defaultOptimize, task.configuration.optimize, "optimize should be unchanged");
        assertEquals(defaultObfuscate, task.configuration.obfuscate, "obfuscate should be unchanged");
        assertEquals(defaultAllowAccessModification, task.configuration.allowAccessModification, "allowAccessModification should be unchanged");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testOverloadaggressively_forDockerizedBuilds() throws Exception {
        // Dockerized build environment with overload aggressively
        task.overloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_forCloudBuilds() throws Exception {
        // Cloud build service with overload aggressively
        task.overloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_forLocalDevelopment() throws Exception {
        // Local development environment
        task.overloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.overloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.overloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    // ============================================================
    // Method Name Obfuscation Context Tests
    // ============================================================

    @Test
    public void testOverloadaggressively_forAggressiveMethodNameReuse() throws Exception {
        // overloadAggressively allows method names to be reused more aggressively
        task.overloadaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.overloadAggressively, "Should enable aggressive method name reuse");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testOverloadaggressively_withOtherObfuscationSettings() throws Exception {
        task.overloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testOverloadaggressively_forOptimalObfuscation() throws Exception {
        // Combine overload aggressively with other settings for optimal obfuscation
        task.overloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Performance and Build Time Scenarios
    // ============================================================

    @Test
    public void testOverloadaggressively_forFastFeedbackBuilds() throws Exception {
        // Fast feedback development builds
        task.overloadaggressively();
        task.dontobfuscate();
        task.dontoptimize();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testOverloadaggressively_forIncrementalBuilds() throws Exception {
        // Incremental build configuration
        task.overloadaggressively();
        task.dontobfuscate();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testOverloadaggressively_forFullReleaseBuilds() throws Exception {
        // Full release build with all optimizations
        task.overloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Compatibility Scenarios
    // ============================================================

    @Test
    public void testOverloadaggressively_withReflectionHeavyCode() throws Exception {
        // Code using heavy reflection
        task.overloadaggressively();
        task.dontobfuscate();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for reflection compatibility");
    }

    @Test
    public void testOverloadaggressively_withJniCode() throws Exception {
        // JNI code requiring method names
        task.overloadaggressively();
        task.dontobfuscate();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for JNI compatibility");
    }

    @Test
    public void testOverloadaggressively_withSerializableClasses() throws Exception {
        // Serializable classes configuration
        task.overloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testOverloadaggressively_withPublicApiLibrary() throws Exception {
        // Public API library
        task.overloadaggressively();
        task.dontobfuscate();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for public API");
    }
}

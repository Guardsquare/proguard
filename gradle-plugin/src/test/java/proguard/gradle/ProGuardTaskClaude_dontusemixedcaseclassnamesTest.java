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
 * Test class for ProGuardTask.dontusemixedcaseclassnames()V method.
 * Tests the dontusemixedcaseclassnames() void method that sets configuration.useMixedCaseClassNames to false,
 * disabling the use of mixed case class names to avoid issues on case-insensitive file systems.
 */
public class ProGuardTaskClaude_dontusemixedcaseclassnamesTest {

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
    public void testDontusemixedcaseclassnames_setsUseMixedCaseClassNamesToFalse() throws Exception {
        assertTrue(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should initially be true");
        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be set to false");
    }

    @Test
    public void testDontusemixedcaseclassnames_disablesMixedCaseClassNames() throws Exception {
        assertTrue(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be enabled by default");
        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "mixed case class names should be disabled");
    }

    @Test
    public void testDontusemixedcaseclassnames_doesNotReturnValue() throws Exception {
        // Verify method is void and completes successfully
        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false after call");
    }

    @Test
    public void testDontusemixedcaseclassnames_changesConfigurationState() throws Exception {
        boolean initialState = task.configuration.useMixedCaseClassNames;
        task.dontusemixedcaseclassnames();
        boolean finalState = task.configuration.useMixedCaseClassNames;

        assertNotEquals(initialState, finalState, "Configuration state should change");
        assertFalse(finalState, "Final state should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertTrue(freshTask.configuration.useMixedCaseClassNames, "Fresh task should have useMixedCaseClassNames as true");

        freshTask.dontusemixedcaseclassnames();

        assertFalse(freshTask.configuration.useMixedCaseClassNames, "Should set useMixedCaseClassNames to false");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_multipleCallsAreIdempotent() throws Exception {
        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "First call should set useMixedCaseClassNames to false");

        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "Second call should keep useMixedCaseClassNames as false");

        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "Third call should keep useMixedCaseClassNames as false");
    }

    @Test
    public void testDontusemixedcaseclassnames_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.dontusemixedcaseclassnames();
            assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false after call " + (i + 1));
        }
    }

    @Test
    public void testDontusemixedcaseclassnames_consecutiveCalls() throws Exception {
        task.dontusemixedcaseclassnames();
        task.dontusemixedcaseclassnames();
        task.dontusemixedcaseclassnames();
        task.dontusemixedcaseclassnames();
        task.dontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "After 5 consecutive calls, useMixedCaseClassNames should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_remainsFalseAfterMultipleCalls() throws Exception {
        task.dontusemixedcaseclassnames();
        boolean stateAfterFirst = task.configuration.useMixedCaseClassNames;

        task.dontusemixedcaseclassnames();
        boolean stateAfterSecond = task.configuration.useMixedCaseClassNames;

        assertEquals(stateAfterFirst, stateAfterSecond, "State should remain false");
        assertFalse(stateAfterSecond, "Should be false after multiple calls");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_withDontobfuscate() throws Exception {
        task.dontusemixedcaseclassnames();
        task.dontobfuscate();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_withDontshrink() throws Exception {
        task.dontusemixedcaseclassnames();
        task.dontshrink();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_withDontoptimize() throws Exception {
        task.dontusemixedcaseclassnames();
        task.dontoptimize();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_withVerbose() throws Exception {
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_withAllowAccessModification() throws Exception {
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_withOverloadAggressively() throws Exception {
        task.dontusemixedcaseclassnames();
        task.overloadaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_withMergeInterfacesAggressively() throws Exception {
        task.dontusemixedcaseclassnames();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_withMultipleConfigMethods() throws Exception {
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_withAllOptimizationSettings() throws Exception {
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_withOptimizationPasses() throws Exception {
        task.dontusemixedcaseclassnames();
        task.optimizationpasses(5);

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_beforeOtherConfig() throws Exception {
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.dontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_inComplexConfigurationChain() throws Exception {
        task.verbose();
        task.allowaccessmodification();
        task.dontusemixedcaseclassnames();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_forWindowsFileSystem() throws Exception {
        // Windows file systems are case-insensitive
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_forCaseInsensitiveFileSystems() throws Exception {
        // For case-insensitive file systems like Windows and macOS (default)
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_inProductionBuild() throws Exception {
        // Production build without mixed case class names
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup without mixed case class names
        task.dontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_forCrossPlatformCompatibility() throws Exception {
        // Ensuring cross-platform compatibility
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.dontusemixedcaseclassnames();
        task2.dontusemixedcaseclassnames();
        task3.dontusemixedcaseclassnames();

        assertFalse(task1.configuration.useMixedCaseClassNames, "task1 useMixedCaseClassNames should be false");
        assertFalse(task2.configuration.useMixedCaseClassNames, "task2 useMixedCaseClassNames should be false");
        assertFalse(task3.configuration.useMixedCaseClassNames, "task3 useMixedCaseClassNames should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.dontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "task useMixedCaseClassNames should be false");
        assertTrue(otherTask.configuration.useMixedCaseClassNames, "otherTask should not be affected");
    }

    @Test
    public void testDontusemixedcaseclassnames_afterConfigurationAccess() throws Exception {
        // Access configuration before calling dontusemixedcaseclassnames
        assertNotNull(task.configuration, "Configuration should not be null");

        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "Should be false after first call");

        task.allowaccessmodification();
        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "Should remain false after second call");

        task.verbose();
        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "Should remain false after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_inAndroidReleaseVariant() throws Exception {
        // Android release variant without mixed case class names
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_inAndroidDebugVariant() throws Exception {
        // Android debug variant without mixed case class names
        task.dontusemixedcaseclassnames();
        task.dontobfuscate();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_inAndroidLibraryModule() throws Exception {
        // Android library without mixed case class names
        task.dontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build without mixed case class names
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_forAndroidTestBuild() throws Exception {
        // Test build configuration
        task.dontusemixedcaseclassnames();
        task.dontobfuscate();
        task.dontoptimize();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_forAndroidInstrumentationTests() throws Exception {
        // Instrumentation test configuration
        task.dontusemixedcaseclassnames();
        task.dontobfuscate();
        task.dontshrink();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_inProductionRelease() throws Exception {
        // Production release build without mixed case class names
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_inDevelopmentBuild() throws Exception {
        // Development build with shrinking but no obfuscation
        task.dontusemixedcaseclassnames();
        task.dontobfuscate();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_inStagingForQa() throws Exception {
        // QA staging build without mixed case class names
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_inInternalTestingBuild() throws Exception {
        // Internal testing build
        task.dontusemixedcaseclassnames();
        task.dontobfuscate();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_inBetaBuild() throws Exception {
        // Beta testing build without mixed case class names
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_inAlphaBuild() throws Exception {
        // Alpha testing build without mixed case class names
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_modifiesConfigurationState() throws Exception {
        assertTrue(task.configuration.useMixedCaseClassNames, "Initial state should be true");

        task.dontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "State should be modified to false");
    }

    @Test
    public void testDontusemixedcaseclassnames_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.dontusemixedcaseclassnames();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.dontusemixedcaseclassnames();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.dontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testDontusemixedcaseclassnames_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.dontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testDontusemixedcaseclassnames_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.dontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    @Test
    public void testDontusemixedcaseclassnames_onlyAffectsUseMixedCaseClassNamesSetting() throws Exception {
        // Capture default states
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;
        boolean defaultAllowAccessModification = task.configuration.allowAccessModification;

        task.dontusemixedcaseclassnames();

        // Verify only useMixedCaseClassNames changed
        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertEquals(defaultShrink, task.configuration.shrink, "shrink should be unchanged");
        assertEquals(defaultOptimize, task.configuration.optimize, "optimize should be unchanged");
        assertEquals(defaultObfuscate, task.configuration.obfuscate, "obfuscate should be unchanged");
        assertEquals(defaultAllowAccessModification, task.configuration.allowAccessModification, "allowAccessModification should be unchanged");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_forDockerizedBuilds() throws Exception {
        // Dockerized build environment without mixed case class names
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_forCloudBuilds() throws Exception {
        // Cloud build service without mixed case class names
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_forLocalDevelopment() throws Exception {
        // Local development environment
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.dontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    // ============================================================
    // Class Name Obfuscation Context Tests
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_forFileSystemCompatibility() throws Exception {
        // Disable mixed case for file system compatibility
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "Should disable mixed case class names");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testDontusemixedcaseclassnames_withOtherObfuscationSettings() throws Exception {
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_forOptimalObfuscation() throws Exception {
        // Combine dont use mixed case with other settings
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Performance and Build Time Scenarios
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_forFastFeedbackBuilds() throws Exception {
        // Fast feedback development builds
        task.dontusemixedcaseclassnames();
        task.dontobfuscate();
        task.dontoptimize();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_forIncrementalBuilds() throws Exception {
        // Incremental build configuration
        task.dontusemixedcaseclassnames();
        task.dontobfuscate();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testDontusemixedcaseclassnames_forFullReleaseBuilds() throws Exception {
        // Full release build with all optimizations
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Compatibility Scenarios
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_withReflectionHeavyCode() throws Exception {
        // Code using heavy reflection
        task.dontusemixedcaseclassnames();
        task.dontobfuscate();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for reflection compatibility");
    }

    @Test
    public void testDontusemixedcaseclassnames_withJniCode() throws Exception {
        // JNI code requiring specific class names
        task.dontusemixedcaseclassnames();
        task.dontobfuscate();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for JNI compatibility");
    }

    @Test
    public void testDontusemixedcaseclassnames_withSerializableClasses() throws Exception {
        // Serializable classes configuration
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_withPublicApiLibrary() throws Exception {
        // Public API library
        task.dontusemixedcaseclassnames();
        task.dontobfuscate();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for public API");
    }

    // ============================================================
    // Windows Compatibility Tests
    // ============================================================

    @Test
    public void testDontusemixedcaseclassnames_forWindowsCompatibility() throws Exception {
        // Windows file systems are case-insensitive
        task.dontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "Should disable mixed case for Windows compatibility");
    }

    @Test
    public void testDontusemixedcaseclassnames_preventsCaseConflicts() throws Exception {
        // Prevent conflicts between A.class and a.class on case-insensitive systems
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "Should prevent case conflicts");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_forMacOSDefaultFileSystem() throws Exception {
        // macOS default file system is case-insensitive
        task.dontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "Should disable mixed case for macOS compatibility");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testDontusemixedcaseclassnames_avoidsFileSystemIssues() throws Exception {
        // Avoid potential file system issues across different platforms
        task.dontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "Should avoid file system issues");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }
}

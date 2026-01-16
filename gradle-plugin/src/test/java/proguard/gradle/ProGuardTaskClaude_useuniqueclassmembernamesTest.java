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
 * Test class for ProGuardTask.useuniqueclassmembernames()V method.
 * Tests the useuniqueclassmembernames() void method that sets configuration.useUniqueClassMemberNames to true,
 * enabling ProGuard to assign unique names to class members with different types or return values.
 */
public class ProGuardTaskClaude_useuniqueclassmembernamesTest {

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
    public void testUseuniqueclassmembernames_setsUseUniqueClassMemberNamesToTrue() throws Exception {
        assertFalse(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should initially be false");
        task.useuniqueclassmembernames();
        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be set to true");
    }

    @Test
    public void testUseuniqueclassmembernames_enablesUniqueClassMemberNaming() throws Exception {
        assertFalse(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be disabled by default");
        task.useuniqueclassmembernames();
        assertTrue(task.configuration.useUniqueClassMemberNames, "unique class member naming should be enabled");
    }

    @Test
    public void testUseuniqueclassmembernames_doesNotReturnValue() throws Exception {
        // Verify method is void and completes successfully
        task.useuniqueclassmembernames();
        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true after call");
    }

    @Test
    public void testUseuniqueclassmembernames_changesConfigurationState() throws Exception {
        boolean initialState = task.configuration.useUniqueClassMemberNames;
        task.useuniqueclassmembernames();
        boolean finalState = task.configuration.useUniqueClassMemberNames;

        assertNotEquals(initialState, finalState, "Configuration state should change");
        assertTrue(finalState, "Final state should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertFalse(freshTask.configuration.useUniqueClassMemberNames, "Fresh task should have useUniqueClassMemberNames as false");

        freshTask.useuniqueclassmembernames();

        assertTrue(freshTask.configuration.useUniqueClassMemberNames, "Should set useUniqueClassMemberNames to true");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_multipleCallsAreIdempotent() throws Exception {
        task.useuniqueclassmembernames();
        assertTrue(task.configuration.useUniqueClassMemberNames, "First call should set useUniqueClassMemberNames to true");

        task.useuniqueclassmembernames();
        assertTrue(task.configuration.useUniqueClassMemberNames, "Second call should keep useUniqueClassMemberNames as true");

        task.useuniqueclassmembernames();
        assertTrue(task.configuration.useUniqueClassMemberNames, "Third call should keep useUniqueClassMemberNames as true");
    }

    @Test
    public void testUseuniqueclassmembernames_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.useuniqueclassmembernames();
            assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true after call " + (i + 1));
        }
    }

    @Test
    public void testUseuniqueclassmembernames_consecutiveCalls() throws Exception {
        task.useuniqueclassmembernames();
        task.useuniqueclassmembernames();
        task.useuniqueclassmembernames();
        task.useuniqueclassmembernames();
        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "After 5 consecutive calls, useUniqueClassMemberNames should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_remainsTrueAfterMultipleCalls() throws Exception {
        task.useuniqueclassmembernames();
        boolean stateAfterFirst = task.configuration.useUniqueClassMemberNames;

        task.useuniqueclassmembernames();
        boolean stateAfterSecond = task.configuration.useUniqueClassMemberNames;

        assertEquals(stateAfterFirst, stateAfterSecond, "State should remain true");
        assertTrue(stateAfterSecond, "Should be true after multiple calls");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_withDontobfuscate() throws Exception {
        task.useuniqueclassmembernames();
        task.dontobfuscate();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testUseuniqueclassmembernames_withDontshrink() throws Exception {
        task.useuniqueclassmembernames();
        task.dontshrink();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testUseuniqueclassmembernames_withDontoptimize() throws Exception {
        task.useuniqueclassmembernames();
        task.dontoptimize();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testUseuniqueclassmembernames_withVerbose() throws Exception {
        task.useuniqueclassmembernames();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_withAllowAccessModification() throws Exception {
        task.useuniqueclassmembernames();
        task.allowaccessmodification();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_withOverloadAggressively() throws Exception {
        task.useuniqueclassmembernames();
        task.overloadaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_withMergeInterfacesAggressively() throws Exception {
        task.useuniqueclassmembernames();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_withMultipleConfigMethods() throws Exception {
        task.useuniqueclassmembernames();
        task.allowaccessmodification();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_withAllOptimizationSettings() throws Exception {
        task.useuniqueclassmembernames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.overloadaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_withOptimizationPasses() throws Exception {
        task.useuniqueclassmembernames();
        task.optimizationpasses(5);

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_beforeOtherConfig() throws Exception {
        task.useuniqueclassmembernames();
        task.allowaccessmodification();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.useuniqueclassmembernames();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_inComplexConfigurationChain() throws Exception {
        task.verbose();
        task.allowaccessmodification();
        task.useuniqueclassmembernames();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_inAggressiveObfuscationScenario() throws Exception {
        // Aggressive obfuscation with unique class member names
        task.useuniqueclassmembernames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_inProductionBuild() throws Exception {
        // Production build with unique class member names
        task.useuniqueclassmembernames();
        task.allowaccessmodification();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup with unique class member names
        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.useuniqueclassmembernames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_forUniqueMemberNaming() throws Exception {
        // Enabling unique class member names
        task.useuniqueclassmembernames();
        task.allowaccessmodification();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_forBetterDeobfuscation() throws Exception {
        // Using unique class member names for better deobfuscation support
        task.useuniqueclassmembernames();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.useuniqueclassmembernames();
        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.useuniqueclassmembernames();
        task2.useuniqueclassmembernames();
        task3.useuniqueclassmembernames();

        assertTrue(task1.configuration.useUniqueClassMemberNames, "task1 useUniqueClassMemberNames should be true");
        assertTrue(task2.configuration.useUniqueClassMemberNames, "task2 useUniqueClassMemberNames should be true");
        assertTrue(task3.configuration.useUniqueClassMemberNames, "task3 useUniqueClassMemberNames should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "task useUniqueClassMemberNames should be true");
        assertFalse(otherTask.configuration.useUniqueClassMemberNames, "otherTask should not be affected");
    }

    @Test
    public void testUseuniqueclassmembernames_afterConfigurationAccess() throws Exception {
        // Access configuration before calling useuniqueclassmembernames
        assertNotNull(task.configuration, "Configuration should not be null");

        task.useuniqueclassmembernames();
        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.useuniqueclassmembernames();
        assertTrue(task.configuration.useUniqueClassMemberNames, "Should be true after first call");

        task.allowaccessmodification();
        task.useuniqueclassmembernames();
        assertTrue(task.configuration.useUniqueClassMemberNames, "Should remain true after second call");

        task.verbose();
        task.useuniqueclassmembernames();
        assertTrue(task.configuration.useUniqueClassMemberNames, "Should remain true after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_inAndroidReleaseVariant() throws Exception {
        // Android release variant with unique class member names
        task.useuniqueclassmembernames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_inAndroidDebugVariant() throws Exception {
        // Android debug variant with unique class member names but no obfuscation
        task.useuniqueclassmembernames();
        task.dontobfuscate();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testUseuniqueclassmembernames_inAndroidLibraryModule() throws Exception {
        // Android library with unique class member names
        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build with unique class member names
        task.useuniqueclassmembernames();
        task.allowaccessmodification();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_forAndroidTestBuild() throws Exception {
        // Test build configuration
        task.useuniqueclassmembernames();
        task.dontobfuscate();
        task.dontoptimize();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testUseuniqueclassmembernames_forAndroidInstrumentationTests() throws Exception {
        // Instrumentation test configuration
        task.useuniqueclassmembernames();
        task.dontobfuscate();
        task.dontshrink();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_inProductionRelease() throws Exception {
        // Production release build with unique class member names
        task.useuniqueclassmembernames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_inDevelopmentBuild() throws Exception {
        // Development build with shrinking but no obfuscation
        task.useuniqueclassmembernames();
        task.dontobfuscate();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testUseuniqueclassmembernames_inStagingForQa() throws Exception {
        // QA staging build with unique class member names
        task.useuniqueclassmembernames();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_inInternalTestingBuild() throws Exception {
        // Internal testing build
        task.useuniqueclassmembernames();
        task.dontobfuscate();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_inBetaBuild() throws Exception {
        // Beta testing build with unique class member names
        task.useuniqueclassmembernames();
        task.allowaccessmodification();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_inAlphaBuild() throws Exception {
        // Alpha testing build with unique class member names
        task.useuniqueclassmembernames();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_modifiesConfigurationState() throws Exception {
        assertFalse(task.configuration.useUniqueClassMemberNames, "Initial state should be false");

        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "State should be modified to true");
    }

    @Test
    public void testUseuniqueclassmembernames_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.useuniqueclassmembernames();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.useuniqueclassmembernames();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testUseuniqueclassmembernames_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testUseuniqueclassmembernames_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    @Test
    public void testUseuniqueclassmembernames_onlyAffectsUseUniqueClassMemberNamesSetting() throws Exception {
        // Capture default states
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;
        boolean defaultAllowAccessModification = task.configuration.allowAccessModification;

        task.useuniqueclassmembernames();

        // Verify only useUniqueClassMemberNames changed
        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertEquals(defaultShrink, task.configuration.shrink, "shrink should be unchanged");
        assertEquals(defaultOptimize, task.configuration.optimize, "optimize should be unchanged");
        assertEquals(defaultObfuscate, task.configuration.obfuscate, "obfuscate should be unchanged");
        assertEquals(defaultAllowAccessModification, task.configuration.allowAccessModification, "allowAccessModification should be unchanged");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_forDockerizedBuilds() throws Exception {
        // Dockerized build environment with unique class member names
        task.useuniqueclassmembernames();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_forCloudBuilds() throws Exception {
        // Cloud build service with unique class member names
        task.useuniqueclassmembernames();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_forLocalDevelopment() throws Exception {
        // Local development environment
        task.useuniqueclassmembernames();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.useuniqueclassmembernames();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
    }

    // ============================================================
    // Member Name Obfuscation Context Tests
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_forUniqueClassMemberNames() throws Exception {
        // useUniqueClassMemberNames assigns unique names to members with different types
        task.useuniqueclassmembernames();
        task.allowaccessmodification();

        assertTrue(task.configuration.useUniqueClassMemberNames, "Should enable unique class member names");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testUseuniqueclassmembernames_withOtherObfuscationSettings() throws Exception {
        task.useuniqueclassmembernames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_forOptimalObfuscation() throws Exception {
        // Combine unique class member names with other settings for optimal obfuscation
        task.useuniqueclassmembernames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Performance and Build Time Scenarios
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_forFastFeedbackBuilds() throws Exception {
        // Fast feedback development builds
        task.useuniqueclassmembernames();
        task.dontobfuscate();
        task.dontoptimize();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testUseuniqueclassmembernames_forIncrementalBuilds() throws Exception {
        // Incremental build configuration
        task.useuniqueclassmembernames();
        task.dontobfuscate();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testUseuniqueclassmembernames_forFullReleaseBuilds() throws Exception {
        // Full release build with all optimizations
        task.useuniqueclassmembernames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Compatibility Scenarios
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_withReflectionHeavyCode() throws Exception {
        // Code using heavy reflection
        task.useuniqueclassmembernames();
        task.dontobfuscate();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for reflection compatibility");
    }

    @Test
    public void testUseuniqueclassmembernames_withJniCode() throws Exception {
        // JNI code requiring method names
        task.useuniqueclassmembernames();
        task.dontobfuscate();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for JNI compatibility");
    }

    @Test
    public void testUseuniqueclassmembernames_withSerializableClasses() throws Exception {
        // Serializable classes configuration
        task.useuniqueclassmembernames();
        task.verbose();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_withPublicApiLibrary() throws Exception {
        // Public API library
        task.useuniqueclassmembernames();
        task.dontobfuscate();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false for public API");
    }

    // ============================================================
    // Combination with OverloadAggressively Tests
    // ============================================================

    @Test
    public void testUseuniqueclassmembernames_oppositeOfOverloadAggressively() throws Exception {
        // useuniqueclassmembernames is conceptually opposite to overloadaggressively
        // unique names vs reusing names aggressively
        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertFalse(task.configuration.overloadAggressively, "overloadAggressively should remain false by default");
    }

    @Test
    public void testUseuniqueclassmembernames_canCoexistWithOverloadAggressively() throws Exception {
        // Although conceptually opposite, both can be set
        task.useuniqueclassmembernames();
        task.overloadaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_beforeOverloadAggressively() throws Exception {
        task.useuniqueclassmembernames();
        task.overloadaggressively();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testUseuniqueclassmembernames_afterOverloadAggressively() throws Exception {
        task.overloadaggressively();
        task.useuniqueclassmembernames();

        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }
}

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
 * Test class for ProGuardTask.getkeepattributes()Ljava/lang/Object; method.
 * Tests the getkeepattributes() Groovy DSL method that returns null and calls keepattributes()
 * to clear the keepAttributes filter, keeping all attributes.
 */
public class ProGuardTaskClaude_getkeepattributesTest {

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
    public void testGetkeepattributes_returnsNull() throws Exception {
        Object result = task.getkeepattributes();
        assertNull(result, "getkeepattributes() should return null");
    }

    @Test
    public void testGetkeepattributes_clearsKeepAttributesFilter() throws Exception {
        // keepAttributes should be cleared (null becomes empty list)
        task.getkeepattributes();
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should not be null after call");
    }

    @Test
    public void testGetkeepattributes_returnsNullAndClearsFilter() throws Exception {
        Object result = task.getkeepattributes();
        assertNull(result, "getkeepattributes() should return null");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should not be null");
    }

    @Test
    public void testGetkeepattributes_changesConfigurationState() throws Exception {
        // Initially null
        assertNull(task.configuration.keepAttributes, "keepAttributes should initially be null");

        task.getkeepattributes();

        // After calling, should be initialized to empty list
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testGetkeepattributes_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertNull(freshTask.configuration.keepAttributes, "Fresh task should have keepAttributes as null");

        Object result = freshTask.getkeepattributes();

        assertNull(result, "Should return null");
        assertNotNull(freshTask.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testGetkeepattributes_alwaysReturnsNull() throws Exception {
        Object result1 = task.getkeepattributes();
        Object result2 = task.getkeepattributes();
        Object result3 = task.getkeepattributes();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testGetkeepattributes_multipleCallsAreIdempotent() throws Exception {
        task.getkeepattributes();
        assertNotNull(task.configuration.keepAttributes, "First call should initialize keepAttributes");

        task.getkeepattributes();
        assertNotNull(task.configuration.keepAttributes, "Second call should keep keepAttributes initialized");

        task.getkeepattributes();
        assertNotNull(task.configuration.keepAttributes, "Third call should keep keepAttributes initialized");
    }

    @Test
    public void testGetkeepattributes_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getkeepattributes();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized after call " + (i + 1));
        }
    }

    @Test
    public void testGetkeepattributes_consecutiveCalls() throws Exception {
        task.getkeepattributes();
        task.getkeepattributes();
        task.getkeepattributes();
        task.getkeepattributes();
        task.getkeepattributes();

        assertNotNull(task.configuration.keepAttributes, "After 5 consecutive calls, keepAttributes should be initialized");
    }

    @Test
    public void testGetkeepattributes_remainsInitializedAfterMultipleCalls() throws Exception {
        task.getkeepattributes();
        Object stateAfterFirst = task.configuration.keepAttributes;

        task.getkeepattributes();
        Object stateAfterSecond = task.configuration.keepAttributes;

        assertNotNull(stateAfterFirst, "State after first call should be initialized");
        assertNotNull(stateAfterSecond, "State after second call should remain initialized");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetkeepattributes_withDontobfuscate() throws Exception {
        task.getkeepattributes();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetkeepattributes_withDontshrink() throws Exception {
        task.getkeepattributes();
        task.dontshrink();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetkeepattributes_withDontoptimize() throws Exception {
        task.getkeepattributes();
        task.dontoptimize();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetkeepattributes_withVerbose() throws Exception {
        task.getkeepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_withAllowAccessModification() throws Exception {
        task.getkeepattributes();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetkeepattributes_withOverloadAggressively() throws Exception {
        task.getkeepattributes();
        task.overloadaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testGetkeepattributes_withMergeInterfacesAggressively() throws Exception {
        task.getkeepattributes();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetkeepattributes_withMultipleConfigMethods() throws Exception {
        task.getkeepattributes();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_withAllOptimizationSettings() throws Exception {
        task.getkeepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetkeepattributes_withOptimizationPasses() throws Exception {
        task.getkeepattributes();
        task.optimizationpasses(5);

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testGetkeepattributes_beforeOtherConfig() throws Exception {
        task.getkeepattributes();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.getkeepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.getkeepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_inComplexConfigurationChain() throws Exception {
        task.verbose();
        task.allowaccessmodification();
        task.getkeepattributes();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetkeepattributes_inObfuscationScenario() throws Exception {
        // Keep all attributes during obfuscation
        task.getkeepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetkeepattributes_forDebugging() throws Exception {
        // Keep attributes for easier debugging
        task.getkeepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_inProductionBuild() throws Exception {
        // Production build keeping attributes
        task.getkeepattributes();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup keeping attributes
        task.getkeepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testGetkeepattributes_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.getkeepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_forBetterStackTraces() throws Exception {
        // Keep attributes for better stack traces
        task.getkeepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testGetkeepattributes_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.getkeepattributes();
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testGetkeepattributes_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getkeepattributes();
        task2.getkeepattributes();
        task3.getkeepattributes();

        assertNotNull(task1.configuration.keepAttributes, "task1 keepAttributes should be initialized");
        assertNotNull(task2.configuration.keepAttributes, "task2 keepAttributes should be initialized");
        assertNotNull(task3.configuration.keepAttributes, "task3 keepAttributes should be initialized");
    }

    @Test
    public void testGetkeepattributes_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getkeepattributes();

        assertNotNull(task.configuration.keepAttributes, "task keepAttributes should be initialized");
        assertNull(otherTask.configuration.keepAttributes, "otherTask should not be affected");
    }

    @Test
    public void testGetkeepattributes_afterConfigurationAccess() throws Exception {
        // Access configuration before calling getkeepattributes
        assertNotNull(task.configuration, "Configuration should not be null");

        task.getkeepattributes();
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testGetkeepattributes_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.getkeepattributes();
        assertNotNull(task.configuration.keepAttributes, "Should be initialized after first call");

        task.allowaccessmodification();
        task.getkeepattributes();
        assertNotNull(task.configuration.keepAttributes, "Should remain initialized after second call");

        task.verbose();
        task.getkeepattributes();
        assertNotNull(task.configuration.keepAttributes, "Should remain initialized after third call");
    }

    @Test
    public void testGetkeepattributes_returnValueType() throws Exception {
        Object result = task.getkeepattributes();
        assertNull(result, "Return value should be null");
        // Verify it's exactly null, not any other type
        assertSame(null, result, "Return value should be exactly null");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testGetkeepattributes_inAndroidReleaseVariant() throws Exception {
        // Android release variant keeping attributes
        task.getkeepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetkeepattributes_inAndroidDebugVariant() throws Exception {
        // Android debug variant keeping attributes
        task.getkeepattributes();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetkeepattributes_inAndroidLibraryModule() throws Exception {
        // Android library keeping attributes
        task.getkeepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testGetkeepattributes_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build keeping attributes
        task.getkeepattributes();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetkeepattributes_forAndroidTestBuild() throws Exception {
        // Test build configuration
        task.getkeepattributes();
        task.dontobfuscate();
        task.dontoptimize();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetkeepattributes_forAndroidInstrumentationTests() throws Exception {
        // Instrumentation test configuration
        task.getkeepattributes();
        task.dontobfuscate();
        task.dontshrink();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testGetkeepattributes_inProductionRelease() throws Exception {
        // Production release build keeping attributes
        task.getkeepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetkeepattributes_inDevelopmentBuild() throws Exception {
        // Development build with shrinking but keeping attributes
        task.getkeepattributes();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetkeepattributes_inStagingForQa() throws Exception {
        // QA staging build keeping attributes
        task.getkeepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_inInternalTestingBuild() throws Exception {
        // Internal testing build
        task.getkeepattributes();
        task.dontobfuscate();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_inBetaBuild() throws Exception {
        // Beta testing build keeping attributes
        task.getkeepattributes();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetkeepattributes_inAlphaBuild() throws Exception {
        // Alpha testing build keeping attributes
        task.getkeepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testGetkeepattributes_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.keepAttributes, "Initial state should be null");

        task.getkeepattributes();

        assertNotNull(task.configuration.keepAttributes, "State should be modified");
    }

    @Test
    public void testGetkeepattributes_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.getkeepattributes();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testGetkeepattributes_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.getkeepattributes();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetkeepattributes_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.getkeepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testGetkeepattributes_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.getkeepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testGetkeepattributes_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.getkeepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    @Test
    public void testGetkeepattributes_onlyAffectsKeepAttributesSetting() throws Exception {
        // Capture default states
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;
        boolean defaultAllowAccessModification = task.configuration.allowAccessModification;

        task.getkeepattributes();

        // Verify only keepAttributes changed
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertEquals(defaultShrink, task.configuration.shrink, "shrink should be unchanged");
        assertEquals(defaultOptimize, task.configuration.optimize, "optimize should be unchanged");
        assertEquals(defaultObfuscate, task.configuration.obfuscate, "obfuscate should be unchanged");
        assertEquals(defaultAllowAccessModification, task.configuration.allowAccessModification, "allowAccessModification should be unchanged");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testGetkeepattributes_forDockerizedBuilds() throws Exception {
        // Dockerized build environment keeping attributes
        task.getkeepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_forCloudBuilds() throws Exception {
        // Cloud build service keeping attributes
        task.getkeepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_forLocalDevelopment() throws Exception {
        // Local development environment
        task.getkeepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.getkeepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetkeepattributes_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.getkeepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    // ============================================================
    // Attribute Keeping Context Tests
    // ============================================================

    @Test
    public void testGetkeepattributes_forKeepingAttributes() throws Exception {
        // Keep all attributes from being removed
        task.getkeepattributes();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepAttributes, "Should initialize keepAttributes");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testGetkeepattributes_withOtherObfuscationSettings() throws Exception {
        task.getkeepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetkeepattributes_forOptimalObfuscation() throws Exception {
        // Combine keep attributes with other settings
        task.getkeepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }
}

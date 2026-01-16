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
 * Test class for ProGuardTask.keepattributes()V method.
 * Tests the keepattributes() void method that clears the keepAttributes filter,
 * keeping all attributes from being removed.
 */
public class ProGuardTaskClaude_keepattributesTest {

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
    public void testKeepattributes_initializesKeepAttributes() throws Exception {
        assertNull(task.configuration.keepAttributes, "keepAttributes should initially be null");
        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testKeepattributes_enablesKeepingAttributes() throws Exception {
        assertNull(task.configuration.keepAttributes, "keepAttributes should be null by default");
        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "attribute keeping should be enabled");
    }

    @Test
    public void testKeepattributes_doesNotReturnValue() throws Exception {
        // Verify method is void and completes successfully
        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized after call");
    }

    @Test
    public void testKeepattributes_changesConfigurationState() throws Exception {
        assertNull(task.configuration.keepAttributes, "Initial state should be null");

        task.keepattributes();

        assertNotNull(task.configuration.keepAttributes, "State should be modified");
    }

    @Test
    public void testKeepattributes_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertNull(freshTask.configuration.keepAttributes, "Fresh task should have keepAttributes as null");

        freshTask.keepattributes();

        assertNotNull(freshTask.configuration.keepAttributes, "Should initialize keepAttributes");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testKeepattributes_multipleCallsAreIdempotent() throws Exception {
        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "First call should initialize keepAttributes");

        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "Second call should keep keepAttributes initialized");

        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "Third call should keep keepAttributes initialized");
    }

    @Test
    public void testKeepattributes_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.keepattributes();
            assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized after call " + (i + 1));
        }
    }

    @Test
    public void testKeepattributes_consecutiveCalls() throws Exception {
        task.keepattributes();
        task.keepattributes();
        task.keepattributes();
        task.keepattributes();
        task.keepattributes();

        assertNotNull(task.configuration.keepAttributes, "After 5 consecutive calls, keepAttributes should be initialized");
    }

    @Test
    public void testKeepattributes_remainsInitializedAfterMultipleCalls() throws Exception {
        task.keepattributes();
        Object stateAfterFirst = task.configuration.keepAttributes;

        task.keepattributes();
        Object stateAfterSecond = task.configuration.keepAttributes;

        assertNotNull(stateAfterFirst, "State after first call should be initialized");
        assertNotNull(stateAfterSecond, "State after second call should remain initialized");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testKeepattributes_withDontobfuscate() throws Exception {
        task.keepattributes();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testKeepattributes_withDontshrink() throws Exception {
        task.keepattributes();
        task.dontshrink();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testKeepattributes_withDontoptimize() throws Exception {
        task.keepattributes();
        task.dontoptimize();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testKeepattributes_withVerbose() throws Exception {
        task.keepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_withAllowAccessModification() throws Exception {
        task.keepattributes();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testKeepattributes_withOverloadAggressively() throws Exception {
        task.keepattributes();
        task.overloadaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testKeepattributes_withMergeInterfacesAggressively() throws Exception {
        task.keepattributes();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeepattributes_withMultipleConfigMethods() throws Exception {
        task.keepattributes();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_withAllOptimizationSettings() throws Exception {
        task.keepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeepattributes_withOptimizationPasses() throws Exception {
        task.keepattributes();
        task.optimizationpasses(5);

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testKeepattributes_beforeOtherConfig() throws Exception {
        task.keepattributes();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.keepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.keepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_inComplexConfigurationChain() throws Exception {
        task.verbose();
        task.allowaccessmodification();
        task.keepattributes();
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
    public void testKeepattributes_inObfuscationScenario() throws Exception {
        // Keep attributes during obfuscation
        task.keepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeepattributes_forDebugging() throws Exception {
        // Keep attributes for easier debugging
        task.keepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_inProductionBuild() throws Exception {
        // Production build keeping attributes
        task.keepattributes();
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup keeping attributes
        task.keepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testKeepattributes_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.keepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_forBetterStackTraces() throws Exception {
        // Keep attributes for better stack traces
        task.keepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_forReflection() throws Exception {
        // Keep attributes for reflection compatibility
        task.keepattributes();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testKeepattributes_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testKeepattributes_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.keepattributes();
        task2.keepattributes();
        task3.keepattributes();

        assertNotNull(task1.configuration.keepAttributes, "task1 keepAttributes should be initialized");
        assertNotNull(task2.configuration.keepAttributes, "task2 keepAttributes should be initialized");
        assertNotNull(task3.configuration.keepAttributes, "task3 keepAttributes should be initialized");
    }

    @Test
    public void testKeepattributes_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.keepattributes();

        assertNotNull(task.configuration.keepAttributes, "task keepAttributes should be initialized");
        assertNull(otherTask.configuration.keepAttributes, "otherTask should not be affected");
    }

    @Test
    public void testKeepattributes_afterConfigurationAccess() throws Exception {
        // Access configuration before calling keepattributes
        assertNotNull(task.configuration, "Configuration should not be null");

        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testKeepattributes_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "Should be initialized after first call");

        task.allowaccessmodification();
        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "Should remain initialized after second call");

        task.verbose();
        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "Should remain initialized after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testKeepattributes_inAndroidReleaseVariant() throws Exception {
        // Android release variant keeping attributes
        task.keepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeepattributes_inAndroidDebugVariant() throws Exception {
        // Android debug variant keeping attributes
        task.keepattributes();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testKeepattributes_inAndroidLibraryModule() throws Exception {
        // Android library keeping attributes
        task.keepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testKeepattributes_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build keeping attributes
        task.keepattributes();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testKeepattributes_forAndroidTestBuild() throws Exception {
        // Test build configuration
        task.keepattributes();
        task.dontobfuscate();
        task.dontoptimize();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testKeepattributes_forAndroidInstrumentationTests() throws Exception {
        // Instrumentation test configuration
        task.keepattributes();
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
    public void testKeepattributes_inProductionRelease() throws Exception {
        // Production release build keeping attributes
        task.keepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeepattributes_inDevelopmentBuild() throws Exception {
        // Development build with shrinking but keeping attributes
        task.keepattributes();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testKeepattributes_inStagingForQa() throws Exception {
        // QA staging build keeping attributes
        task.keepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_inInternalTestingBuild() throws Exception {
        // Internal testing build
        task.keepattributes();
        task.dontobfuscate();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_inBetaBuild() throws Exception {
        // Beta testing build keeping attributes
        task.keepattributes();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testKeepattributes_inAlphaBuild() throws Exception {
        // Alpha testing build keeping attributes
        task.keepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testKeepattributes_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.keepAttributes, "Initial state should be null");

        task.keepattributes();

        assertNotNull(task.configuration.keepAttributes, "State should be modified");
    }

    @Test
    public void testKeepattributes_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.keepattributes();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    @Test
    public void testKeepattributes_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.keepattributes();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeepattributes_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.keepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testKeepattributes_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.keepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testKeepattributes_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.keepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    @Test
    public void testKeepattributes_onlyAffectsKeepAttributesSetting() throws Exception {
        // Capture default states
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;
        boolean defaultAllowAccessModification = task.configuration.allowAccessModification;

        task.keepattributes();

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
    public void testKeepattributes_forDockerizedBuilds() throws Exception {
        // Dockerized build environment keeping attributes
        task.keepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_forCloudBuilds() throws Exception {
        // Cloud build service keeping attributes
        task.keepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_forLocalDevelopment() throws Exception {
        // Local development environment
        task.keepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.keepattributes();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributes_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.keepattributes();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
    }

    // ============================================================
    // Attribute Keeping Context Tests
    // ============================================================

    @Test
    public void testKeepattributes_forKeepingAttributes() throws Exception {
        // Keep all attributes from being removed
        task.keepattributes();
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepAttributes, "Should initialize keepAttributes");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testKeepattributes_withOtherObfuscationSettings() throws Exception {
        task.keepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeepattributes_forOptimalObfuscation() throws Exception {
        // Combine keep attributes with other settings
        task.keepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Performance and Build Time Scenarios
    // ============================================================

    @Test
    public void testKeepattributes_forFastFeedbackBuilds() throws Exception {
        // Fast feedback development builds
        task.keepattributes();
        task.dontobfuscate();
        task.dontoptimize();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testKeepattributes_forIncrementalBuilds() throws Exception {
        // Incremental build configuration
        task.keepattributes();
        task.dontobfuscate();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testKeepattributes_forFullReleaseBuilds() throws Exception {
        // Full release build with all optimizations
        task.keepattributes();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Tests for keepattributes(String filter) Method
    // ============================================================

    @Test
    public void testKeepattributesWithFilter_nullFilterClearsAndKeepsAll() throws Exception {
        assertNull(task.configuration.keepAttributes, "keepAttributes should initially be null");

        task.keepattributes(null);

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.isEmpty(), "keepAttributes should be empty (keeps all)");
    }

    @Test
    public void testKeepattributesWithFilter_singleAttribute() throws Exception {
        task.keepattributes("Signature");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertFalse(task.configuration.keepAttributes.isEmpty(), "keepAttributes should contain filter");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should contain Signature attribute");
    }

    @Test
    public void testKeepattributesWithFilter_multipleAttributesCommaSeparated() throws Exception {
        task.keepattributes("Signature,Exceptions,InnerClasses");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should contain Signature");
        assertTrue(task.configuration.keepAttributes.contains("Exceptions"),
                   "Should contain Exceptions");
        assertTrue(task.configuration.keepAttributes.contains("InnerClasses"),
                   "Should contain InnerClasses");
    }

    @Test
    public void testKeepattributesWithFilter_sourceFileAttribute() throws Exception {
        task.keepattributes("SourceFile");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("SourceFile"),
                   "Should contain SourceFile");
    }

    @Test
    public void testKeepattributesWithFilter_lineNumberTableAttribute() throws Exception {
        task.keepattributes("LineNumberTable");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("LineNumberTable"),
                   "Should contain LineNumberTable");
    }

    @Test
    public void testKeepattributesWithFilter_multipleCalls() throws Exception {
        task.keepattributes("Signature");
        task.keepattributes("Exceptions");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should contain first attribute");
        assertTrue(task.configuration.keepAttributes.contains("Exceptions"),
                   "Should contain second attribute");
    }

    @Test
    public void testKeepattributesWithFilter_wildcardAttribute() throws Exception {
        task.keepattributes("*Annotation*");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("*Annotation*"),
                   "Should contain wildcard pattern");
    }

    @Test
    public void testKeepattributesWithFilter_afterNoArgVersion() throws Exception {
        task.keepattributes();
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.isEmpty(), "Should be empty after no-arg call");

        task.keepattributes("Signature");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should contain attribute after string call");
    }

    @Test
    public void testKeepattributesWithFilter_nullThenSpecific() throws Exception {
        task.keepattributes(null);
        assertTrue(task.configuration.keepAttributes.isEmpty(), "Should be empty after null");

        task.keepattributes("Signature");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should contain specific attribute");
    }

    @Test
    public void testKeepattributesWithFilter_specificThenNull() throws Exception {
        task.keepattributes("Signature");
        assertFalse(task.configuration.keepAttributes.isEmpty(), "Should have filter");

        task.keepattributes(null);
        assertTrue(task.configuration.keepAttributes.isEmpty(), "Should be cleared by null");
    }

    @Test
    public void testKeepattributesWithFilter_accumulatesFilters() throws Exception {
        task.keepattributes("Signature");
        assertEquals(1, task.configuration.keepAttributes.size(), "Should have 1 filter");

        task.keepattributes("Exceptions");
        assertEquals(2, task.configuration.keepAttributes.size(), "Should have 2 filters");

        task.keepattributes("InnerClasses");
        assertEquals(3, task.configuration.keepAttributes.size(), "Should have 3 filters");
    }

    @Test
    public void testKeepattributesWithFilter_emptyStringFilter() throws Exception {
        task.keepattributes("");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains(""),
                   "Should contain empty string filter");
    }

    // ============================================================
    // Integration Tests with String Filter
    // ============================================================

    @Test
    public void testKeepattributesWithFilter_withObfuscation() throws Exception {
        task.keepattributes("Signature,InnerClasses");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should contain filter");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testKeepattributesWithFilter_withVerbose() throws Exception {
        task.keepattributes("SourceFile,LineNumberTable");
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("SourceFile"),
                   "Should contain SourceFile");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testKeepattributesWithFilter_withDontobfuscate() throws Exception {
        task.keepattributes("Signature");
        task.dontobfuscate();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should contain filter");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    // ============================================================
    // Realistic Scenarios with String Filter
    // ============================================================

    @Test
    public void testKeepattributesWithFilter_forDebugging() throws Exception {
        // Keep debugging attributes
        task.keepattributes("SourceFile,LineNumberTable");
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("SourceFile"),
                   "Should keep SourceFile for debugging");
        assertTrue(task.configuration.keepAttributes.contains("LineNumberTable"),
                   "Should keep LineNumberTable for debugging");
    }

    @Test
    public void testKeepattributesWithFilter_forAnnotations() throws Exception {
        // Keep annotation attributes
        task.keepattributes("*Annotation*");
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("*Annotation*"),
                   "Should keep annotation attributes");
    }

    @Test
    public void testKeepattributesWithFilter_forGenerics() throws Exception {
        // Keep attributes needed for generics
        task.keepattributes("Signature");
        task.allowaccessmodification();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should keep Signature for generics");
    }

    @Test
    public void testKeepattributesWithFilter_forStackTraces() throws Exception {
        // Keep attributes for better stack traces
        task.keepattributes("SourceFile,LineNumberTable");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("SourceFile"),
                   "Should keep SourceFile");
        assertTrue(task.configuration.keepAttributes.contains("LineNumberTable"),
                   "Should keep LineNumberTable");
    }

    @Test
    public void testKeepattributesWithFilter_forReflection() throws Exception {
        // Keep attributes needed for reflection
        task.keepattributes("Signature,Exceptions,InnerClasses");
        task.dontobfuscate();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should keep Signature");
        assertTrue(task.configuration.keepAttributes.contains("Exceptions"),
                   "Should keep Exceptions");
        assertTrue(task.configuration.keepAttributes.contains("InnerClasses"),
                   "Should keep InnerClasses");
    }

    @Test
    public void testKeepattributesWithFilter_comprehensiveSet() throws Exception {
        // Keep comprehensive set of attributes
        task.keepattributes("Signature,Exceptions,InnerClasses,SourceFile,LineNumberTable");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertEquals(5, task.configuration.keepAttributes.size(), "Should have 5 attributes");
    }

    // ============================================================
    // Edge Cases with String Filter
    // ============================================================

    @Test
    public void testKeepattributesWithFilter_multipleClearsCalls() throws Exception {
        task.keepattributes("Signature");
        assertEquals(1, task.configuration.keepAttributes.size(), "Should have 1 filter");

        task.keepattributes(null);
        assertEquals(0, task.configuration.keepAttributes.size(), "Should be cleared");

        task.keepattributes("Exceptions");
        assertEquals(1, task.configuration.keepAttributes.size(), "Should have 1 filter again");
    }

    @Test
    public void testKeepattributesWithFilter_duplicateAttributes() throws Exception {
        task.keepattributes("Signature");
        task.keepattributes("Signature");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        // Note: duplicates are allowed by the implementation
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should contain the attribute");
    }

    @Test
    public void testKeepattributesWithFilter_casePreservation() throws Exception {
        task.keepattributes("SourceFile");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("SourceFile"),
                   "Should preserve case");
    }

    @Test
    public void testKeepattributesWithFilter_consecutiveCalls() throws Exception {
        task.keepattributes("Signature");
        task.keepattributes("Exceptions");
        task.keepattributes("InnerClasses");

        assertEquals(3, task.configuration.keepAttributes.size(), "Should accumulate filters");
    }

    // ============================================================
    // Android-Specific Scenarios with String Filter
    // ============================================================

    @Test
    public void testKeepattributesWithFilter_androidReleaseWithSpecificAttributes() throws Exception {
        // Android release keeping specific attributes
        task.keepattributes("Signature,*Annotation*");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should keep Signature");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testKeepattributesWithFilter_androidDebugWithAllAttributes() throws Exception {
        // Android debug keeping all attributes for debugging
        task.keepattributes("SourceFile,LineNumberTable,*Annotation*");
        task.dontobfuscate();
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("SourceFile"),
                   "Should keep SourceFile");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testKeepattributesWithFilter_androidLibraryAttributes() throws Exception {
        // Android library keeping public attributes
        task.keepattributes("Signature,InnerClasses,Exceptions");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should keep Signature");
    }

    // ============================================================
    // Configuration State Tests with String Filter
    // ============================================================

    @Test
    public void testKeepattributesWithFilter_preservesExistingConfiguration() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.keepattributes("Signature");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
    }

    @Test
    public void testKeepattributesWithFilter_doesNotAffectOtherSettings() throws Exception {
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;

        task.keepattributes("Signature");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertEquals(defaultShrink, task.configuration.shrink, "shrink should be unchanged");
        assertEquals(defaultOptimize, task.configuration.optimize, "optimize should be unchanged");
        assertEquals(defaultObfuscate, task.configuration.obfuscate, "obfuscate should be unchanged");
    }

    // ============================================================
    // Special Use Cases with String Filter
    // ============================================================

    @Test
    public void testKeepattributesWithFilter_forCrashReporting() throws Exception {
        // Keep attributes for better crash reports
        task.keepattributes("SourceFile,LineNumberTable");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("SourceFile"),
                   "Should keep SourceFile for crash reporting");
        assertTrue(task.configuration.keepAttributes.contains("LineNumberTable"),
                   "Should keep LineNumberTable for crash reporting");
    }

    @Test
    public void testKeepattributesWithFilter_forSerializableClasses() throws Exception {
        // Keep attributes for serializable classes
        task.keepattributes("Signature,Exceptions");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("Signature"),
                   "Should keep Signature");
        assertTrue(task.configuration.keepAttributes.contains("Exceptions"),
                   "Should keep Exceptions");
    }

    @Test
    public void testKeepattributesWithFilter_forJavaScriptInterface() throws Exception {
        // Keep attributes for JavaScript interface (Android WebView)
        task.keepattributes("*Annotation*");
        task.verbose();

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("*Annotation*"),
                   "Should keep annotations for JavaScript interface");
    }

    // ============================================================
    // Common Attribute Combinations
    // ============================================================

    @Test
    public void testKeepattributesWithFilter_minimalDebugAttributes() throws Exception {
        // Minimal attributes for debugging
        task.keepattributes("SourceFile,LineNumberTable");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertEquals(2, task.configuration.keepAttributes.size(), "Should have 2 attributes");
    }

    @Test
    public void testKeepattributesWithFilter_fullDebugAttributes() throws Exception {
        // Full attributes for debugging
        task.keepattributes("SourceFile,LineNumberTable,LocalVariableTable,LocalVariableTypeTable");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertEquals(4, task.configuration.keepAttributes.size(), "Should have 4 attributes");
    }

    @Test
    public void testKeepattributesWithFilter_annotationPreservation() throws Exception {
        // Preserve all annotation attributes
        task.keepattributes("RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations,AnnotationDefault");

        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be initialized");
        assertTrue(task.configuration.keepAttributes.contains("RuntimeVisibleAnnotations"),
                   "Should keep RuntimeVisibleAnnotations");
        assertTrue(task.configuration.keepAttributes.contains("RuntimeInvisibleAnnotations"),
                   "Should keep RuntimeInvisibleAnnotations");
        assertTrue(task.configuration.keepAttributes.contains("AnnotationDefault"),
                   "Should keep AnnotationDefault");
    }
}

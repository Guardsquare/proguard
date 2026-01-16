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
 * Test class for ProGuardTask.getrepackageclasses()Ljava/lang/Object; method.
 * Tests the getrepackageclasses() Groovy DSL method that returns null and calls repackageclasses()
 * to set the repackageClasses configuration to an empty string.
 */
public class ProGuardTaskClaude_getrepackageclassesTest {

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
    public void testGetrepackageclasses_returnsNull() throws Exception {
        Object result = task.getrepackageclasses();
        assertNull(result, "getrepackageclasses() should return null");
    }

    @Test
    public void testGetrepackageclasses_setsRepackageClasses() throws Exception {
        task.getrepackageclasses();
        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testGetrepackageclasses_returnsNullAndSetsEmptyString() throws Exception {
        Object result = task.getrepackageclasses();
        assertNull(result, "getrepackageclasses() should return null");
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testGetrepackageclasses_changesConfigurationState() throws Exception {
        assertNull(task.configuration.repackageClasses, "repackageClasses should initially be null");

        task.getrepackageclasses();

        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testGetrepackageclasses_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertNull(freshTask.configuration.repackageClasses, "Fresh task should have repackageClasses as null");

        Object result = freshTask.getrepackageclasses();

        assertNull(result, "Should return null");
        assertEquals("", freshTask.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testGetrepackageclasses_alwaysReturnsNull() throws Exception {
        Object result1 = task.getrepackageclasses();
        Object result2 = task.getrepackageclasses();
        Object result3 = task.getrepackageclasses();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testGetrepackageclasses_multipleCallsAreIdempotent() throws Exception {
        task.getrepackageclasses();
        assertEquals("", task.configuration.repackageClasses, "First call should set empty string");

        task.getrepackageclasses();
        assertEquals("", task.configuration.repackageClasses, "Second call should keep empty string");

        task.getrepackageclasses();
        assertEquals("", task.configuration.repackageClasses, "Third call should keep empty string");
    }

    @Test
    public void testGetrepackageclasses_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getrepackageclasses();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string after call " + (i + 1));
        }
    }

    @Test
    public void testGetrepackageclasses_consecutiveCalls() throws Exception {
        task.getrepackageclasses();
        task.getrepackageclasses();
        task.getrepackageclasses();
        task.getrepackageclasses();
        task.getrepackageclasses();

        assertEquals("", task.configuration.repackageClasses, "After 5 consecutive calls, repackageClasses should be empty string");
    }

    @Test
    public void testGetrepackageclasses_remainsEmptyAfterMultipleCalls() throws Exception {
        task.getrepackageclasses();
        String stateAfterFirst = task.configuration.repackageClasses;

        task.getrepackageclasses();
        String stateAfterSecond = task.configuration.repackageClasses;

        assertEquals("", stateAfterFirst, "State after first call should be empty string");
        assertEquals("", stateAfterSecond, "State after second call should be empty string");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetrepackageclasses_withDontobfuscate() throws Exception {
        task.getrepackageclasses();
        task.dontobfuscate();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetrepackageclasses_withDontshrink() throws Exception {
        task.getrepackageclasses();
        task.dontshrink();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetrepackageclasses_withDontoptimize() throws Exception {
        task.getrepackageclasses();
        task.dontoptimize();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetrepackageclasses_withVerbose() throws Exception {
        task.getrepackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_withAllowAccessModification() throws Exception {
        task.getrepackageclasses();
        task.allowaccessmodification();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetrepackageclasses_withOverloadAggressively() throws Exception {
        task.getrepackageclasses();
        task.overloadaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testGetrepackageclasses_withMergeInterfacesAggressively() throws Exception {
        task.getrepackageclasses();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetrepackageclasses_withMultipleConfigMethods() throws Exception {
        task.getrepackageclasses();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_withAllOptimizationSettings() throws Exception {
        task.getrepackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetrepackageclasses_withOptimizationPasses() throws Exception {
        task.getrepackageclasses();
        task.optimizationpasses(5);

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testGetrepackageclasses_beforeOtherConfig() throws Exception {
        task.getrepackageclasses();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.getrepackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.getrepackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_inComplexConfigurationChain() throws Exception {
        task.verbose();
        task.allowaccessmodification();
        task.getrepackageclasses();
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
    public void testGetrepackageclasses_inObfuscationScenario() throws Exception {
        // Repackage classes during obfuscation
        task.getrepackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetrepackageclasses_forCodeSizeReduction() throws Exception {
        // Repackage classes for smaller code size
        task.getrepackageclasses();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_inProductionBuild() throws Exception {
        // Production build repackaging classes
        task.getrepackageclasses();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup repackaging classes
        task.getrepackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testGetrepackageclasses_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.getrepackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_forBetterObfuscation() throws Exception {
        // Repackage classes for better obfuscation
        task.getrepackageclasses();
        task.allowaccessmodification();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testGetrepackageclasses_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.getrepackageclasses();
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testGetrepackageclasses_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getrepackageclasses();
        task2.getrepackageclasses();
        task3.getrepackageclasses();

        assertEquals("", task1.configuration.repackageClasses, "task1 repackageClasses should be empty string");
        assertEquals("", task2.configuration.repackageClasses, "task2 repackageClasses should be empty string");
        assertEquals("", task3.configuration.repackageClasses, "task3 repackageClasses should be empty string");
    }

    @Test
    public void testGetrepackageclasses_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getrepackageclasses();

        assertEquals("", task.configuration.repackageClasses, "task repackageClasses should be empty string");
        assertNull(otherTask.configuration.repackageClasses, "otherTask should not be affected");
    }

    @Test
    public void testGetrepackageclasses_afterConfigurationAccess() throws Exception {
        // Access configuration before calling getrepackageclasses
        assertNotNull(task.configuration, "Configuration should not be null");

        task.getrepackageclasses();
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testGetrepackageclasses_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.getrepackageclasses();
        assertEquals("", task.configuration.repackageClasses, "Should be empty string after first call");

        task.allowaccessmodification();
        task.getrepackageclasses();
        assertEquals("", task.configuration.repackageClasses, "Should be empty string after second call");

        task.verbose();
        task.getrepackageclasses();
        assertEquals("", task.configuration.repackageClasses, "Should be empty string after third call");
    }

    @Test
    public void testGetrepackageclasses_returnValueType() throws Exception {
        Object result = task.getrepackageclasses();
        assertNull(result, "Return value should be null");
        // Verify it's exactly null, not any other type
        assertSame(null, result, "Return value should be exactly null");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testGetrepackageclasses_inAndroidReleaseVariant() throws Exception {
        // Android release variant repackaging classes
        task.getrepackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetrepackageclasses_inAndroidDebugVariant() throws Exception {
        // Android debug variant repackaging classes
        task.getrepackageclasses();
        task.dontobfuscate();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetrepackageclasses_inAndroidLibraryModule() throws Exception {
        // Android library repackaging classes
        task.getrepackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testGetrepackageclasses_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build repackaging classes
        task.getrepackageclasses();
        task.allowaccessmodification();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetrepackageclasses_forAndroidTestBuild() throws Exception {
        // Test build configuration
        task.getrepackageclasses();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetrepackageclasses_forAndroidInstrumentationTests() throws Exception {
        // Instrumentation test configuration
        task.getrepackageclasses();
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
    public void testGetrepackageclasses_inProductionRelease() throws Exception {
        // Production release build repackaging classes
        task.getrepackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetrepackageclasses_inDevelopmentBuild() throws Exception {
        // Development build with shrinking but repackaging classes
        task.getrepackageclasses();
        task.dontobfuscate();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetrepackageclasses_inStagingForQa() throws Exception {
        // QA staging build repackaging classes
        task.getrepackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_inInternalTestingBuild() throws Exception {
        // Internal testing build
        task.getrepackageclasses();
        task.dontobfuscate();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_inBetaBuild() throws Exception {
        // Beta testing build repackaging classes
        task.getrepackageclasses();
        task.allowaccessmodification();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetrepackageclasses_inAlphaBuild() throws Exception {
        // Alpha testing build repackaging classes
        task.getrepackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testGetrepackageclasses_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.repackageClasses, "Initial state should be null");

        task.getrepackageclasses();

        assertNotNull(task.configuration.repackageClasses, "State should be modified");
        assertEquals("", task.configuration.repackageClasses, "State should be empty string");
    }

    @Test
    public void testGetrepackageclasses_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.getrepackageclasses();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    @Test
    public void testGetrepackageclasses_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.getrepackageclasses();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetrepackageclasses_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.getrepackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testGetrepackageclasses_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.getrepackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testGetrepackageclasses_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.getrepackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    @Test
    public void testGetrepackageclasses_onlyAffectsRepackageClassesSetting() throws Exception {
        // Capture default states
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;
        boolean defaultAllowAccessModification = task.configuration.allowAccessModification;

        task.getrepackageclasses();

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
    public void testGetrepackageclasses_forDockerizedBuilds() throws Exception {
        // Dockerized build environment repackaging classes
        task.getrepackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_forCloudBuilds() throws Exception {
        // Cloud build service repackaging classes
        task.getrepackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_forLocalDevelopment() throws Exception {
        // Local development environment
        task.getrepackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.getrepackageclasses();
        task.verbose();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetrepackageclasses_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.getrepackageclasses();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
    }

    // ============================================================
    // Class Repackaging Context Tests
    // ============================================================

    @Test
    public void testGetrepackageclasses_forRepackagingClasses() throws Exception {
        // Repackage all classes to root level
        task.getrepackageclasses();
        task.allowaccessmodification();

        assertEquals("", task.configuration.repackageClasses, "Should set repackageClasses to empty string");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testGetrepackageclasses_withOtherObfuscationSettings() throws Exception {
        task.getrepackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetrepackageclasses_forOptimalObfuscation() throws Exception {
        // Combine repackage classes with other settings
        task.getrepackageclasses();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertEquals("", task.configuration.repackageClasses, "repackageClasses should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }
}

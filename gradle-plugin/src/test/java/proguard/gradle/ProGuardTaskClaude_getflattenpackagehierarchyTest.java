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
 * Test class for ProGuardTask.getflattenpackagehierarchy()Ljava/lang/Object; method.
 * Tests the getflattenpackagehierarchy() Groovy DSL method that returns null and calls flattenpackagehierarchy()
 * to set the flattenPackageHierarchy configuration to an empty string.
 */
public class ProGuardTaskClaude_getflattenpackagehierarchyTest {

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
    public void testGetflattenpackagehierarchy_returnsNull() throws Exception {
        Object result = task.getflattenpackagehierarchy();
        assertNull(result, "getflattenpackagehierarchy() should return null");
    }

    @Test
    public void testGetflattenpackagehierarchy_setsFlattenPackageHierarchy() throws Exception {
        task.getflattenpackagehierarchy();
        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_returnsNullAndSetsEmptyString() throws Exception {
        Object result = task.getflattenpackagehierarchy();
        assertNull(result, "getflattenpackagehierarchy() should return null");
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_changesConfigurationState() throws Exception {
        assertNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should initially be null");

        task.getflattenpackagehierarchy();

        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertNull(freshTask.configuration.flattenPackageHierarchy, "Fresh task should have flattenPackageHierarchy as null");

        Object result = freshTask.getflattenpackagehierarchy();

        assertNull(result, "Should return null");
        assertEquals("", freshTask.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_alwaysReturnsNull() throws Exception {
        Object result1 = task.getflattenpackagehierarchy();
        Object result2 = task.getflattenpackagehierarchy();
        Object result3 = task.getflattenpackagehierarchy();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testGetflattenpackagehierarchy_multipleCallsAreIdempotent() throws Exception {
        task.getflattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "First call should set empty string");

        task.getflattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "Second call should keep empty string");

        task.getflattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "Third call should keep empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getflattenpackagehierarchy();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string after call " + (i + 1));
        }
    }

    @Test
    public void testGetflattenpackagehierarchy_consecutiveCalls() throws Exception {
        task.getflattenpackagehierarchy();
        task.getflattenpackagehierarchy();
        task.getflattenpackagehierarchy();
        task.getflattenpackagehierarchy();
        task.getflattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "After 5 consecutive calls, flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_remainsEmptyAfterMultipleCalls() throws Exception {
        task.getflattenpackagehierarchy();
        String stateAfterFirst = task.configuration.flattenPackageHierarchy;

        task.getflattenpackagehierarchy();
        String stateAfterSecond = task.configuration.flattenPackageHierarchy;

        assertEquals("", stateAfterFirst, "State after first call should be empty string");
        assertEquals("", stateAfterSecond, "State after second call should be empty string");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetflattenpackagehierarchy_withDontobfuscate() throws Exception {
        task.getflattenpackagehierarchy();
        task.dontobfuscate();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetflattenpackagehierarchy_withDontshrink() throws Exception {
        task.getflattenpackagehierarchy();
        task.dontshrink();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetflattenpackagehierarchy_withDontoptimize() throws Exception {
        task.getflattenpackagehierarchy();
        task.dontoptimize();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetflattenpackagehierarchy_withVerbose() throws Exception {
        task.getflattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_withAllowAccessModification() throws Exception {
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_withOverloadAggressively() throws Exception {
        task.getflattenpackagehierarchy();
        task.overloadaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_withMergeInterfacesAggressively() throws Exception {
        task.getflattenpackagehierarchy();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_withMultipleConfigMethods() throws Exception {
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_withAllOptimizationSettings() throws Exception {
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_withOptimizationPasses() throws Exception {
        task.getflattenpackagehierarchy();
        task.optimizationpasses(5);

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testGetflattenpackagehierarchy_beforeOtherConfig() throws Exception {
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.getflattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.getflattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_inComplexConfigurationChain() throws Exception {
        task.verbose();
        task.allowaccessmodification();
        task.getflattenpackagehierarchy();
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
    public void testGetflattenpackagehierarchy_inObfuscationScenario() throws Exception {
        // Flatten package hierarchy during obfuscation
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_forCodeSizeReduction() throws Exception {
        // Flatten package hierarchy for smaller code size
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_inProductionBuild() throws Exception {
        // Production build flattening package hierarchy
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup flattening package hierarchy
        task.getflattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_forBetterObfuscation() throws Exception {
        // Flatten package hierarchy for better obfuscation
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testGetflattenpackagehierarchy_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.getflattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getflattenpackagehierarchy();
        task2.getflattenpackagehierarchy();
        task3.getflattenpackagehierarchy();

        assertEquals("", task1.configuration.flattenPackageHierarchy, "task1 flattenPackageHierarchy should be empty string");
        assertEquals("", task2.configuration.flattenPackageHierarchy, "task2 flattenPackageHierarchy should be empty string");
        assertEquals("", task3.configuration.flattenPackageHierarchy, "task3 flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getflattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "task flattenPackageHierarchy should be empty string");
        assertNull(otherTask.configuration.flattenPackageHierarchy, "otherTask should not be affected");
    }

    @Test
    public void testGetflattenpackagehierarchy_afterConfigurationAccess() throws Exception {
        // Access configuration before calling getflattenpackagehierarchy
        assertNotNull(task.configuration, "Configuration should not be null");

        task.getflattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.getflattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "Should be empty string after first call");

        task.allowaccessmodification();
        task.getflattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "Should be empty string after second call");

        task.verbose();
        task.getflattenpackagehierarchy();
        assertEquals("", task.configuration.flattenPackageHierarchy, "Should be empty string after third call");
    }

    @Test
    public void testGetflattenpackagehierarchy_returnValueType() throws Exception {
        Object result = task.getflattenpackagehierarchy();
        assertNull(result, "Return value should be null");
        // Verify it's exactly null, not any other type
        assertSame(null, result, "Return value should be exactly null");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testGetflattenpackagehierarchy_inAndroidReleaseVariant() throws Exception {
        // Android release variant flattening package hierarchy
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_inAndroidDebugVariant() throws Exception {
        // Android debug variant flattening package hierarchy
        task.getflattenpackagehierarchy();
        task.dontobfuscate();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetflattenpackagehierarchy_inAndroidLibraryModule() throws Exception {
        // Android library flattening package hierarchy
        task.getflattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build flattening package hierarchy
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_forAndroidTestBuild() throws Exception {
        // Test build configuration
        task.getflattenpackagehierarchy();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetflattenpackagehierarchy_forAndroidInstrumentationTests() throws Exception {
        // Instrumentation test configuration
        task.getflattenpackagehierarchy();
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
    public void testGetflattenpackagehierarchy_inProductionRelease() throws Exception {
        // Production release build flattening package hierarchy
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_inDevelopmentBuild() throws Exception {
        // Development build with shrinking but flattening package hierarchy
        task.getflattenpackagehierarchy();
        task.dontobfuscate();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetflattenpackagehierarchy_inStagingForQa() throws Exception {
        // QA staging build flattening package hierarchy
        task.getflattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_inInternalTestingBuild() throws Exception {
        // Internal testing build
        task.getflattenpackagehierarchy();
        task.dontobfuscate();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_inBetaBuild() throws Exception {
        // Beta testing build flattening package hierarchy
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_inAlphaBuild() throws Exception {
        // Alpha testing build flattening package hierarchy
        task.getflattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testGetflattenpackagehierarchy_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.flattenPackageHierarchy, "Initial state should be null");

        task.getflattenpackagehierarchy();

        assertNotNull(task.configuration.flattenPackageHierarchy, "State should be modified");
        assertEquals("", task.configuration.flattenPackageHierarchy, "State should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.getflattenpackagehierarchy();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    @Test
    public void testGetflattenpackagehierarchy_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.getflattenpackagehierarchy();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.getflattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testGetflattenpackagehierarchy_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.getflattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testGetflattenpackagehierarchy_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.getflattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    @Test
    public void testGetflattenpackagehierarchy_onlyAffectsFlattenPackageHierarchySetting() throws Exception {
        // Capture default states
        boolean defaultShrink = task.configuration.shrink;
        boolean defaultOptimize = task.configuration.optimize;
        boolean defaultObfuscate = task.configuration.obfuscate;
        boolean defaultAllowAccessModification = task.configuration.allowAccessModification;

        task.getflattenpackagehierarchy();

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
    public void testGetflattenpackagehierarchy_forDockerizedBuilds() throws Exception {
        // Dockerized build environment flattening package hierarchy
        task.getflattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_forCloudBuilds() throws Exception {
        // Cloud build service flattening package hierarchy
        task.getflattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_forLocalDevelopment() throws Exception {
        // Local development environment
        task.getflattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.getflattenpackagehierarchy();
        task.verbose();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.getflattenpackagehierarchy();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
    }

    // ============================================================
    // Package Hierarchy Flattening Context Tests
    // ============================================================

    @Test
    public void testGetflattenpackagehierarchy_forFlatteningPackages() throws Exception {
        // Flatten all packages to root level
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();

        assertEquals("", task.configuration.flattenPackageHierarchy, "Should set flattenPackageHierarchy to empty string");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testGetflattenpackagehierarchy_withOtherObfuscationSettings() throws Exception {
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetflattenpackagehierarchy_forOptimalObfuscation() throws Exception {
        // Combine flatten package hierarchy with other settings
        task.getflattenpackagehierarchy();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertEquals("", task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be empty string");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }
}

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
 * Test class for ProGuardTask.getdontusemixedcaseclassnames()Ljava/lang/Object; method.
 * Tests the getdontusemixedcaseclassnames() Groovy DSL method that returns null and calls dontusemixedcaseclassnames()
 * to set configuration.useMixedCaseClassNames to false.
 */
public class ProGuardTaskClaude_getdontusemixedcaseclassnamesTest {

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
    public void testGetdontusemixedcaseclassnames_returnsNull() throws Exception {
        Object result = task.getdontusemixedcaseclassnames();
        assertNull(result, "getdontusemixedcaseclassnames() should return null");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_setsUseMixedCaseClassNamesToFalse() throws Exception {
        assertTrue(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should initially be true");
        task.getdontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be set to false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_returnsNullAndSetsUseMixedCaseClassNames() throws Exception {
        assertTrue(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should initially be true");
        Object result = task.getdontusemixedcaseclassnames();
        assertNull(result, "getdontusemixedcaseclassnames() should return null");
        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be set to false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_changesConfigurationState() throws Exception {
        boolean initialState = task.configuration.useMixedCaseClassNames;
        task.getdontusemixedcaseclassnames();
        boolean finalState = task.configuration.useMixedCaseClassNames;

        assertNotEquals(initialState, finalState, "Configuration state should change");
        assertFalse(finalState, "Final state should be false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertTrue(freshTask.configuration.useMixedCaseClassNames, "Fresh task should have useMixedCaseClassNames as true");

        Object result = freshTask.getdontusemixedcaseclassnames();

        assertNull(result, "Should return null");
        assertFalse(freshTask.configuration.useMixedCaseClassNames, "Should set useMixedCaseClassNames to false");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_multipleCallsAreIdempotent() throws Exception {
        task.getdontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "First call should set useMixedCaseClassNames to false");

        task.getdontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "Second call should keep useMixedCaseClassNames as false");

        task.getdontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "Third call should keep useMixedCaseClassNames as false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_alwaysReturnsNull() throws Exception {
        Object result1 = task.getdontusemixedcaseclassnames();
        Object result2 = task.getdontusemixedcaseclassnames();
        Object result3 = task.getdontusemixedcaseclassnames();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getdontusemixedcaseclassnames();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false after call " + (i + 1));
        }
    }

    @Test
    public void testGetdontusemixedcaseclassnames_consecutiveCalls() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.getdontusemixedcaseclassnames();
        task.getdontusemixedcaseclassnames();
        task.getdontusemixedcaseclassnames();
        task.getdontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "After 5 consecutive calls, useMixedCaseClassNames should be false");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_withDontobfuscate() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.dontobfuscate();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withDontshrink() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.dontshrink();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withDontoptimize() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.dontoptimize();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withVerbose() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withAllowAccessModification() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withOverloadAggressively() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.overloadaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withMergeInterfacesAggressively() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withMultipleConfigMethods() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withAllOptimizationSettings() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_beforeOtherConfig() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.getdontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.getdontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_mixedWithOtherGetters() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.getallowaccessmodification();
        task.getmergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_forWindowsFileSystem() throws Exception {
        // On Windows file systems, mixed case class names can cause issues
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_forCaseInsensitiveFileSystems() throws Exception {
        // For case-insensitive file systems
        task.getdontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup with dont use mixed case class names
        task.getdontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_forCrossPlatformCompatibility() throws Exception {
        // Ensuring cross-platform compatibility
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.getdontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getdontusemixedcaseclassnames();
        task2.getdontusemixedcaseclassnames();
        task3.getdontusemixedcaseclassnames();

        assertFalse(task1.configuration.useMixedCaseClassNames, "task1 useMixedCaseClassNames should be false");
        assertFalse(task2.configuration.useMixedCaseClassNames, "task2 useMixedCaseClassNames should be false");
        assertFalse(task3.configuration.useMixedCaseClassNames, "task3 useMixedCaseClassNames should be false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getdontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "task useMixedCaseClassNames should be false");
        assertTrue(otherTask.configuration.useMixedCaseClassNames, "otherTask should not be affected");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_afterConfigurationAccess() throws Exception {
        // Access configuration before calling getdontusemixedcaseclassnames
        assertNotNull(task.configuration, "Configuration should not be null");

        task.getdontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.getdontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "Should be false after first call");

        task.allowaccessmodification();
        task.getdontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "Should remain false after second call");

        task.verbose();
        task.getdontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "Should remain false after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_inAndroidReleaseVariant() throws Exception {
        // Android release variant with dont use mixed case class names
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_inAndroidLibraryModule() throws Exception {
        // Android library with dont use mixed case class names
        task.getdontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build with dont use mixed case class names
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_inProductionRelease() throws Exception {
        // Production release build with dont use mixed case class names
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_inStagingForQa() throws Exception {
        // QA staging build with dont use mixed case class names
        task.getdontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_inBetaBuild() throws Exception {
        // Beta testing build with dont use mixed case class names
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_inAlphaBuild() throws Exception {
        // Alpha testing build with dont use mixed case class names
        task.getdontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_modifiesConfigurationState() throws Exception {
        assertTrue(task.configuration.useMixedCaseClassNames, "Initial state should be true");

        task.getdontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "State should be modified to false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.getdontusemixedcaseclassnames();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.getdontusemixedcaseclassnames();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.getdontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.getdontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.getdontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ============================================================
    // Groovy DSL Support Tests
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_supportsGroovyDslSyntax() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        // In Groovy: task.dontusemixedcaseclassnames (without parentheses)
        Object result = task.getdontusemixedcaseclassnames();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertFalse(task.configuration.useMixedCaseClassNames, "Should set useMixedCaseClassNames to false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_behavesLikeUnderlyingMethod() throws Exception {
        // getdontusemixedcaseclassnames() should behave like dontusemixedcaseclassnames()
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        task1.getdontusemixedcaseclassnames();
        task2.dontusemixedcaseclassnames();

        assertEquals(task1.configuration.useMixedCaseClassNames, task2.configuration.useMixedCaseClassNames,
                "Both methods should have the same effect on configuration");
        assertFalse(task1.configuration.useMixedCaseClassNames, "task1 useMixedCaseClassNames should be false");
        assertFalse(task2.configuration.useMixedCaseClassNames, "task2 useMixedCaseClassNames should be false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_isAnnotatedAsInternal() throws Exception {
        // The method should be annotated with @Internal for Gradle
        // This is a compile-time check, but we verify the method exists and works
        Object result = task.getdontusemixedcaseclassnames();
        assertNull(result, "Method should exist and return null");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_forDockerizedBuilds() throws Exception {
        // Dockerized build environment with dont use mixed case class names
        task.getdontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_forCloudBuilds() throws Exception {
        // Cloud build service with dont use mixed case class names
        task.getdontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_forLocalDevelopment() throws Exception {
        // Local development environment
        task.getdontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.getdontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.getdontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
    }

    // ============================================================
    // Comparison with Direct Method Call
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_equivalentToDirectCall() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        // Use getter version
        Object getterResult = task1.getdontusemixedcaseclassnames();

        // Use direct method
        task2.dontusemixedcaseclassnames();

        assertNull(getterResult, "Getter should return null");
        assertEquals(task1.configuration.useMixedCaseClassNames, task2.configuration.useMixedCaseClassNames,
                "Both approaches should yield the same configuration state");
        assertFalse(task1.configuration.useMixedCaseClassNames, "task1 should have useMixedCaseClassNames set to false");
        assertFalse(task2.configuration.useMixedCaseClassNames, "task2 should have useMixedCaseClassNames set to false");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_canBeMixedWithDirectCalls() throws Exception {
        task.getdontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "After getter call, should be false");

        task.dontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "After direct call, should still be false");

        task.getdontusemixedcaseclassnames();
        assertFalse(task.configuration.useMixedCaseClassNames, "After another getter call, should still be false");
    }

    // ============================================================
    // Return Value Tests
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_alwaysReturnsNullRegardlessOfState() throws Exception {
        Object result1 = task.getdontusemixedcaseclassnames();
        assertNull(result1, "First call should return null");

        Object result2 = task.getdontusemixedcaseclassnames();
        assertNull(result2, "Second call should return null even though config is already set");

        Object result3 = task.getdontusemixedcaseclassnames();
        assertNull(result3, "Third call should still return null");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_returnTypeIsObject() throws Exception {
        // Verify the return type is Object (not void)
        Object result = task.getdontusemixedcaseclassnames();
        // If the method returns void, this would be a compilation error
        assertNull(result, "Return type should be Object and value should be null");
    }

    // ============================================================
    // Class Name Obfuscation Context Tests
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_forFileSystemCompatibility() throws Exception {
        // Disabling mixed case class names for file system compatibility
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();

        assertFalse(task.configuration.useMixedCaseClassNames, "Should disable mixed case class names");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_withOtherObfuscationSettings() throws Exception {
        task.getdontusemixedcaseclassnames();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.useMixedCaseClassNames, "useMixedCaseClassNames should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Windows Compatibility Tests
    // ============================================================

    @Test
    public void testGetdontusemixedcaseclassnames_forWindowsCompatibility() throws Exception {
        // Windows file systems are case-insensitive
        task.getdontusemixedcaseclassnames();

        assertFalse(task.configuration.useMixedCaseClassNames, "Should disable mixed case for Windows compatibility");
    }

    @Test
    public void testGetdontusemixedcaseclassnames_preventsCaseConflicts() throws Exception {
        // Prevent conflicts between A.class and a.class on case-insensitive systems
        task.getdontusemixedcaseclassnames();
        task.verbose();

        assertFalse(task.configuration.useMixedCaseClassNames, "Should prevent case conflicts");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }
}

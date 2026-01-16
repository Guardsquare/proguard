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
 * Test class for ProGuardTask.getoverloadaggressively()Ljava/lang/Object; method.
 * Tests the getoverloadaggressively() Groovy DSL method that returns null and calls overloadaggressively()
 * to set configuration.overloadAggressively to true.
 */
public class ProGuardTaskClaude_getoverloadaggressivelyTest {

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
    public void testGetoverloadaggressively_returnsNull() throws Exception {
        Object result = task.getoverloadaggressively();
        assertNull(result, "getoverloadaggressively() should return null");
    }

    @Test
    public void testGetoverloadaggressively_setsOverloadAggressivelyToTrue() throws Exception {
        assertFalse(task.configuration.overloadAggressively, "overloadAggressively should initially be false");
        task.getoverloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be set to true");
    }

    @Test
    public void testGetoverloadaggressively_returnsNullAndSetsOverloadAggressively() throws Exception {
        assertFalse(task.configuration.overloadAggressively, "overloadAggressively should initially be false");
        Object result = task.getoverloadaggressively();
        assertNull(result, "getoverloadaggressively() should return null");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be set to true");
    }

    @Test
    public void testGetoverloadaggressively_changesConfigurationState() throws Exception {
        boolean initialState = task.configuration.overloadAggressively;
        task.getoverloadaggressively();
        boolean finalState = task.configuration.overloadAggressively;

        assertNotEquals(initialState, finalState, "Configuration state should change");
        assertTrue(finalState, "Final state should be true");
    }

    @Test
    public void testGetoverloadaggressively_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertFalse(freshTask.configuration.overloadAggressively, "Fresh task should have overloadAggressively as false");

        Object result = freshTask.getoverloadaggressively();

        assertNull(result, "Should return null");
        assertTrue(freshTask.configuration.overloadAggressively, "Should set overloadAggressively to true");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testGetoverloadaggressively_multipleCallsAreIdempotent() throws Exception {
        task.getoverloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "First call should set overloadAggressively to true");

        task.getoverloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "Second call should keep overloadAggressively as true");

        task.getoverloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "Third call should keep overloadAggressively as true");
    }

    @Test
    public void testGetoverloadaggressively_alwaysReturnsNull() throws Exception {
        Object result1 = task.getoverloadaggressively();
        Object result2 = task.getoverloadaggressively();
        Object result3 = task.getoverloadaggressively();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetoverloadaggressively_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getoverloadaggressively();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true after call " + (i + 1));
        }
    }

    @Test
    public void testGetoverloadaggressively_consecutiveCalls() throws Exception {
        task.getoverloadaggressively();
        task.getoverloadaggressively();
        task.getoverloadaggressively();
        task.getoverloadaggressively();
        task.getoverloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "After 5 consecutive calls, overloadAggressively should be true");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetoverloadaggressively_withDontobfuscate() throws Exception {
        task.getoverloadaggressively();
        task.dontobfuscate();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetoverloadaggressively_withDontshrink() throws Exception {
        task.getoverloadaggressively();
        task.dontshrink();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetoverloadaggressively_withDontoptimize() throws Exception {
        task.getoverloadaggressively();
        task.dontoptimize();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetoverloadaggressively_withVerbose() throws Exception {
        task.getoverloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_withAllowAccessModification() throws Exception {
        task.getoverloadaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetoverloadaggressively_withMergeInterfacesAggressively() throws Exception {
        task.getoverloadaggressively();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_withMultipleConfigMethods() throws Exception {
        task.getoverloadaggressively();
        task.allowaccessmodification();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_withAllOptimizationSettings() throws Exception {
        task.getoverloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testGetoverloadaggressively_beforeOtherConfig() throws Exception {
        task.getoverloadaggressively();
        task.allowaccessmodification();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_afterOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.verbose();
        task.getoverloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_betweenOtherConfig() throws Exception {
        task.allowaccessmodification();
        task.getoverloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_mixedWithOtherGetters() throws Exception {
        task.getoverloadaggressively();
        task.getallowaccessmodification();
        task.getmergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetoverloadaggressively_inAggressiveObfuscationScenario() throws Exception {
        // Aggressive obfuscation with overload aggressively
        task.getoverloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_inProductionBuild() throws Exception {
        // Production build with aggressive optimizations
        task.getoverloadaggressively();
        task.allowaccessmodification();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup with overload aggressively
        task.getoverloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.getoverloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_forMaximumMethodNameReuse() throws Exception {
        // Enabling overload aggressively for maximum method name reuse
        task.getoverloadaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testGetoverloadaggressively_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.getoverloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getoverloadaggressively();
        task2.getoverloadaggressively();
        task3.getoverloadaggressively();

        assertTrue(task1.configuration.overloadAggressively, "task1 overloadAggressively should be true");
        assertTrue(task2.configuration.overloadAggressively, "task2 overloadAggressively should be true");
        assertTrue(task3.configuration.overloadAggressively, "task3 overloadAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getoverloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "task overloadAggressively should be true");
        assertFalse(otherTask.configuration.overloadAggressively, "otherTask should not be affected");
    }

    @Test
    public void testGetoverloadaggressively_afterConfigurationAccess() throws Exception {
        // Access configuration before calling getoverloadaggressively
        assertNotNull(task.configuration, "Configuration should not be null");

        task.getoverloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.getoverloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "Should be true after first call");

        task.allowaccessmodification();
        task.getoverloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "Should remain true after second call");

        task.verbose();
        task.getoverloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "Should remain true after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testGetoverloadaggressively_inAndroidReleaseVariant() throws Exception {
        // Android release variant with overload aggressively
        task.getoverloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_inAndroidLibraryModule() throws Exception {
        // Android library with overload aggressively
        task.getoverloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build with overload aggressively
        task.getoverloadaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testGetoverloadaggressively_inProductionRelease() throws Exception {
        // Production release build with overload aggressively
        task.getoverloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_inStagingForQa() throws Exception {
        // QA staging build with overload aggressively
        task.getoverloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_inBetaBuild() throws Exception {
        // Beta testing build with overload aggressively
        task.getoverloadaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetoverloadaggressively_inAlphaBuild() throws Exception {
        // Alpha testing build with overload aggressively
        task.getoverloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testGetoverloadaggressively_modifiesConfigurationState() throws Exception {
        assertFalse(task.configuration.overloadAggressively, "Initial state should be false");

        task.getoverloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "State should be modified to true");
    }

    @Test
    public void testGetoverloadaggressively_preservesOtherConfigurationSettings() throws Exception {
        task.allowaccessmodification();
        task.verbose();

        task.getoverloadaggressively();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_withComplexConfigurationChain() throws Exception {
        task.allowaccessmodification();
        task.getoverloadaggressively();
        task.verbose();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.getoverloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testGetoverloadaggressively_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.getoverloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testGetoverloadaggressively_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.getoverloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ============================================================
    // Groovy DSL Support Tests
    // ============================================================

    @Test
    public void testGetoverloadaggressively_supportsGroovyDslSyntax() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        // In Groovy: task.overloadaggressively (without parentheses)
        Object result = task.getoverloadaggressively();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertTrue(task.configuration.overloadAggressively, "Should set overloadAggressively to true");
    }

    @Test
    public void testGetoverloadaggressively_behavesLikeUnderlyingMethod() throws Exception {
        // getoverloadaggressively() should behave like overloadaggressively()
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        task1.getoverloadaggressively();
        task2.overloadaggressively();

        assertEquals(task1.configuration.overloadAggressively, task2.configuration.overloadAggressively,
                "Both methods should have the same effect on configuration");
        assertTrue(task1.configuration.overloadAggressively, "task1 overloadAggressively should be true");
        assertTrue(task2.configuration.overloadAggressively, "task2 overloadAggressively should be true");
    }

    @Test
    public void testGetoverloadaggressively_isAnnotatedAsInternal() throws Exception {
        // The method should be annotated with @Internal for Gradle
        // This is a compile-time check, but we verify the method exists and works
        Object result = task.getoverloadaggressively();
        assertNull(result, "Method should exist and return null");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testGetoverloadaggressively_forDockerizedBuilds() throws Exception {
        // Dockerized build environment with overload aggressively
        task.getoverloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_forCloudBuilds() throws Exception {
        // Cloud build service with overload aggressively
        task.getoverloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_forLocalDevelopment() throws Exception {
        // Local development environment
        task.getoverloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_forContinuousIntegration() throws Exception {
        // CI pipeline configuration
        task.getoverloadaggressively();
        task.verbose();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetoverloadaggressively_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.getoverloadaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
    }

    // ============================================================
    // Comparison with Direct Method Call
    // ============================================================

    @Test
    public void testGetoverloadaggressively_equivalentToDirectCall() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        // Use getter version
        Object getterResult = task1.getoverloadaggressively();

        // Use direct method
        task2.overloadaggressively();

        assertNull(getterResult, "Getter should return null");
        assertEquals(task1.configuration.overloadAggressively, task2.configuration.overloadAggressively,
                "Both approaches should yield the same configuration state");
        assertTrue(task1.configuration.overloadAggressively, "task1 should have overloadAggressively set");
        assertTrue(task2.configuration.overloadAggressively, "task2 should have overloadAggressively set");
    }

    @Test
    public void testGetoverloadaggressively_canBeMixedWithDirectCalls() throws Exception {
        task.getoverloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "After getter call, should be true");

        task.overloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "After direct call, should still be true");

        task.getoverloadaggressively();
        assertTrue(task.configuration.overloadAggressively, "After another getter call, should still be true");
    }

    // ============================================================
    // Return Value Tests
    // ============================================================

    @Test
    public void testGetoverloadaggressively_alwaysReturnsNullRegardlessOfState() throws Exception {
        Object result1 = task.getoverloadaggressively();
        assertNull(result1, "First call should return null");

        Object result2 = task.getoverloadaggressively();
        assertNull(result2, "Second call should return null even though config is already set");

        Object result3 = task.getoverloadaggressively();
        assertNull(result3, "Third call should still return null");
    }

    @Test
    public void testGetoverloadaggressively_returnTypeIsObject() throws Exception {
        // Verify the return type is Object (not void)
        Object result = task.getoverloadaggressively();
        // If the method returns void, this would be a compilation error
        assertNull(result, "Return type should be Object and value should be null");
    }

    // ============================================================
    // Method Name Obfuscation Context Tests
    // ============================================================

    @Test
    public void testGetoverloadaggressively_forAggressiveMethodNameReuse() throws Exception {
        // overloadAggressively allows method names to be reused more aggressively
        task.getoverloadaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.overloadAggressively, "Should enable aggressive method name reuse");
        assertTrue(task.configuration.allowAccessModification, "Should allow access modification for better obfuscation");
    }

    @Test
    public void testGetoverloadaggressively_withOtherObfuscationSettings() throws Exception {
        task.getoverloadaggressively();
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }
}

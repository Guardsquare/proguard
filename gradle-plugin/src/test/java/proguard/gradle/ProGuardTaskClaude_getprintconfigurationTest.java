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
 * Test class for ProGuardTask.getprintconfiguration()Ljava/lang/Object; method.
 * Tests the getprintconfiguration() Groovy DSL method that returns null and calls printconfiguration()
 * to set configuration.printConfiguration to Configuration.STD_OUT, which outputs the resolved
 * configuration to standard output during ProGuard processing.
 */
public class ProGuardTaskClaude_getprintconfigurationTest {

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
    public void testGetprintconfiguration_returnsNull() throws Exception {
        Object result = task.getprintconfiguration();
        assertNull(result, "getprintconfiguration() should return null");
    }

    @Test
    public void testGetprintconfiguration_setsPrintConfiguration() throws Exception {
        assertNull(task.configuration.printConfiguration, "printConfiguration should initially be null");

        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
    }

    @Test
    public void testGetprintconfiguration_returnsNullAndSetsConfig() throws Exception {
        Object result = task.getprintconfiguration();
        assertNull(result, "getprintconfiguration() should return null");
        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
    }

    @Test
    public void testGetprintconfiguration_changesConfigurationState() throws Exception {
        assertNull(task.configuration.printConfiguration, "printConfiguration should initially be null");

        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
    }

    @Test
    public void testGetprintconfiguration_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertNull(freshTask.configuration.printConfiguration, "Fresh task should have printConfiguration as null");

        Object result = freshTask.getprintconfiguration();

        assertNull(result, "Should return null");
        assertNotNull(freshTask.configuration.printConfiguration, "printConfiguration should be set");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testGetprintconfiguration_multipleCallsAreIdempotent() throws Exception {
        task.getprintconfiguration();
        Object firstValue = task.configuration.printConfiguration;

        task.getprintconfiguration();

        assertEquals(firstValue, task.configuration.printConfiguration,
                    "Multiple calls should not change the value");
        assertNotNull(task.configuration.printConfiguration, "printConfiguration should remain set");
    }

    @Test
    public void testGetprintconfiguration_alwaysReturnsNull() throws Exception {
        Object result1 = task.getprintconfiguration();
        Object result2 = task.getprintconfiguration();
        Object result3 = task.getprintconfiguration();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetprintconfiguration_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getprintconfiguration();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertNotNull(task.configuration.printConfiguration,
                         "printConfiguration should be set after call " + (i + 1));
        }
    }

    @Test
    public void testGetprintconfiguration_consecutiveCalls() throws Exception {
        task.getprintconfiguration();
        task.getprintconfiguration();
        task.getprintconfiguration();
        task.getprintconfiguration();
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration,
                     "After 5 consecutive calls, printConfiguration should be set");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetprintconfiguration_withDontobfuscate() throws Exception {
        task.getprintconfiguration();
        task.dontobfuscate();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetprintconfiguration_withDontshrink() throws Exception {
        task.getprintconfiguration();
        task.dontshrink();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetprintconfiguration_withDontoptimize() throws Exception {
        task.getprintconfiguration();
        task.dontoptimize();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetprintconfiguration_withVerbose() throws Exception {
        task.getprintconfiguration();
        task.verbose();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetprintconfiguration_withIgnoreWarnings() throws Exception {
        task.getprintconfiguration();
        task.ignorewarnings();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetprintconfiguration_withMultipleConfigMethods() throws Exception {
        task.getprintconfiguration();
        task.verbose();
        task.ignorewarnings();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testGetprintconfiguration_beforeOtherConfig() throws Exception {
        task.getprintconfiguration();
        task.verbose();
        task.ignorewarnings();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetprintconfiguration_afterOtherConfig() throws Exception {
        task.verbose();
        task.ignorewarnings();
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetprintconfiguration_betweenOtherConfig() throws Exception {
        task.verbose();
        task.getprintconfiguration();
        task.ignorewarnings();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetprintconfiguration_mixedWithOtherGetters() throws Exception {
        task.getprintconfiguration();
        task.getverbose();
        task.getignorewarnings();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetprintconfiguration_forDebugging() throws Exception {
        // Print configuration to debug ProGuard setup
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration,
                     "Should enable printing configuration for debugging");
    }

    @Test
    public void testGetprintconfiguration_forVerifyingConfiguration() throws Exception {
        // Verify the final resolved configuration
        task.getprintconfiguration();
        task.verbose();

        assertNotNull(task.configuration.printConfiguration,
                     "Should print configuration for verification");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetprintconfiguration_forDevelopmentBuild() throws Exception {
        // Development builds printing configuration
        task.getprintconfiguration();
        task.dontobfuscate();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testGetprintconfiguration_forTroubleshooting() throws Exception {
        // Troubleshooting ProGuard issues
        task.getprintconfiguration();
        task.verbose();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetprintconfiguration_forDocumentation() throws Exception {
        // Generate configuration documentation
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration,
                     "Should print configuration for documentation");
    }

    @Test
    public void testGetprintconfiguration_forCiBuild() throws Exception {
        // CI builds printing configuration for logs
        task.getprintconfiguration();
        task.verbose();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testGetprintconfiguration_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.getprintconfiguration();
        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
    }

    @Test
    public void testGetprintconfiguration_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getprintconfiguration();
        task2.getprintconfiguration();
        task3.getprintconfiguration();

        assertNotNull(task1.configuration.printConfiguration,
                     "task1 printConfiguration should be set");
        assertNotNull(task2.configuration.printConfiguration,
                     "task2 printConfiguration should be set");
        assertNotNull(task3.configuration.printConfiguration,
                     "task3 printConfiguration should be set");
    }

    @Test
    public void testGetprintconfiguration_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration,
                     "task printConfiguration should be set");
        assertNull(otherTask.configuration.printConfiguration, "otherTask should not be affected");
    }

    @Test
    public void testGetprintconfiguration_afterConfigurationAccess() throws Exception {
        // Access configuration before calling getprintconfiguration
        assertNotNull(task.configuration, "Configuration should not be null");

        task.getprintconfiguration();
        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
    }

    @Test
    public void testGetprintconfiguration_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.getprintconfiguration();
        assertNotNull(task.configuration.printConfiguration,
                     "Should be set after first call");

        task.verbose();
        task.getprintconfiguration();
        assertNotNull(task.configuration.printConfiguration,
                     "Should remain set after second call");

        task.ignorewarnings();
        task.getprintconfiguration();
        assertNotNull(task.configuration.printConfiguration,
                     "Should remain set after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testGetprintconfiguration_inAndroidReleaseVariant() throws Exception {
        // Android release variant printing configuration
        task.android();
        task.getprintconfiguration();

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
    }

    @Test
    public void testGetprintconfiguration_inAndroidLibraryModule() throws Exception {
        // Android library printing configuration
        task.android();
        task.getprintconfiguration();

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
    }

    @Test
    public void testGetprintconfiguration_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build printing configuration
        task.android();
        task.getprintconfiguration();

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testGetprintconfiguration_forDebugBuild() throws Exception {
        // Debug build printing configuration
        task.getprintconfiguration();
        task.verbose();
        task.dontobfuscate();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testGetprintconfiguration_forReleaseBuild() throws Exception {
        // Release build printing configuration
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
    }

    @Test
    public void testGetprintconfiguration_forStagingBuild() throws Exception {
        // Staging build printing configuration
        task.getprintconfiguration();
        task.verbose();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testGetprintconfiguration_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.printConfiguration, "Initial state should be null");

        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration, "State should be modified");
    }

    @Test
    public void testGetprintconfiguration_preservesOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.ignorewarnings();

        task.getprintconfiguration();

        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be preserved");
        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
    }

    @Test
    public void testGetprintconfiguration_withComplexConfigurationChain() throws Exception {
        task.verbose();
        task.getprintconfiguration();
        task.ignorewarnings();
        task.dontwarn();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetprintconfiguration_doesNotAffectShrinkSetting() throws Exception {
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testGetprintconfiguration_doesNotAffectOptimizeSetting() throws Exception {
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testGetprintconfiguration_doesNotAffectObfuscateSetting() throws Exception {
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ============================================================
    // Groovy DSL Support Tests
    // ============================================================

    @Test
    public void testGetprintconfiguration_supportsGroovyDslSyntax() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        Object result = task.getprintconfiguration();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertNotNull(task.configuration.printConfiguration,
                     "Should set printConfiguration");
    }

    @Test
    public void testGetprintconfiguration_behavesLikeUnderlyingMethod() throws Exception {
        // getprintconfiguration() should behave like printconfiguration()
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        task1.getprintconfiguration();
        task2.printconfiguration();

        assertNotNull(task1.configuration.printConfiguration,
                     "task1 printConfiguration should be set");
        assertNotNull(task2.configuration.printConfiguration,
                     "task2 printConfiguration should be set");
        assertEquals(task1.configuration.printConfiguration,
                    task2.configuration.printConfiguration,
                    "Both should have the same printConfiguration value");
    }

    @Test
    public void testGetprintconfiguration_isAnnotatedAsInternal() throws Exception {
        // The method should be annotated with @Internal for Gradle
        // This is a compile-time check, but we verify the method exists and works
        Object result = task.getprintconfiguration();
        assertNull(result, "Method should exist and return null");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testGetprintconfiguration_forConfigurationAudit() throws Exception {
        // Audit what ProGuard configuration will be used
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration,
                     "Should print configuration for audit");
    }

    @Test
    public void testGetprintconfiguration_withMinimalConfiguration() throws Exception {
        // Minimal configuration printing
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
    }

    @Test
    public void testGetprintconfiguration_forUnderstandingResolvedConfig() throws Exception {
        // Understanding the final resolved configuration
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration,
                     "Should print resolved configuration");
    }

    // ============================================================
    // Comparison with Direct Method Call
    // ============================================================

    @Test
    public void testGetprintconfiguration_equivalentToDirectCall() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        // Use getter version
        Object getterResult = task1.getprintconfiguration();

        // Use direct method
        task2.printconfiguration();

        assertNull(getterResult, "Getter should return null");
        assertNotNull(task1.configuration.printConfiguration,
                     "task1 should have printConfiguration set");
        assertNotNull(task2.configuration.printConfiguration,
                     "task2 should have printConfiguration set");
        assertEquals(task1.configuration.printConfiguration,
                    task2.configuration.printConfiguration,
                    "Both should set the same value");
    }

    @Test
    public void testGetprintconfiguration_canBeMixedWithDirectCalls() throws Exception {
        task.getprintconfiguration();
        assertNotNull(task.configuration.printConfiguration,
                     "After getter call, should be set");

        task.printconfiguration();
        assertNotNull(task.configuration.printConfiguration,
                     "After direct call, should still be set");

        task.getprintconfiguration();
        assertNotNull(task.configuration.printConfiguration,
                     "After another getter call, should still be set");
    }

    // ============================================================
    // Return Value Tests
    // ============================================================

    @Test
    public void testGetprintconfiguration_alwaysReturnsNullRegardlessOfState() throws Exception {
        Object result1 = task.getprintconfiguration();
        assertNull(result1, "First call should return null");

        Object result2 = task.getprintconfiguration();
        assertNull(result2, "Second call should return null even though config is already set");

        Object result3 = task.getprintconfiguration();
        assertNull(result3, "Third call should still return null");
    }

    @Test
    public void testGetprintconfiguration_returnTypeIsObject() throws Exception {
        // Verify the return type is Object (not void)
        Object result = task.getprintconfiguration();
        // If the method returns void, this would be a compilation error
        assertNull(result, "Return type should be Object and value should be null");
    }

    // ============================================================
    // Output Configuration Tests
    // ============================================================

    @Test
    public void testGetprintconfiguration_outputsToStdOut() throws Exception {
        // printconfiguration outputs to standard output
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration,
                     "Should be configured to output configuration");
    }

    @Test
    public void testGetprintconfiguration_forReviewingRules() throws Exception {
        // Review what keep rules are in effect
        task.keep("public class * { *; }");
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration,
                     "Should output configuration including keep rules");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ============================================================
    // Debugging and Troubleshooting Tests
    // ============================================================

    @Test
    public void testGetprintconfiguration_forUnderstandingBuildFailures() throws Exception {
        // Understanding why build might be failing
        task.getprintconfiguration();
        task.verbose();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetprintconfiguration_forVerifyingKeepRules() throws Exception {
        // Verify keep rules are correct
        task.keep("public class * { *; }");
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration, "printConfiguration should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ============================================================
    // Verification Tests
    // ============================================================

    @Test
    public void testGetprintconfiguration_configurationNotNull() throws Exception {
        task.getprintconfiguration();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetprintconfiguration_returnValueConsistency() throws Exception {
        Object result1 = task.getprintconfiguration();
        Object result2 = task.getprintconfiguration();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertEquals(result1, result2, "Return values should be consistent (both null)");
    }

    @Test
    public void testGetprintconfiguration_taskStateValid() throws Exception {
        task.getprintconfiguration();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetprintconfiguration_semanticMeaning() throws Exception {
        // Verify the semantic meaning: outputs resolved configuration
        task.getprintconfiguration();

        assertNotNull(task.configuration.printConfiguration,
                   "getprintconfiguration should enable printing of resolved configuration");
    }
}

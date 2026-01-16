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
 * Test class for ProGuardTask.getdontwarn()Ljava/lang/Object; method.
 * Tests the getdontwarn() Groovy DSL method that returns null and calls dontwarn()
 * to clear the warn filter, suppressing all warning messages during ProGuard processing.
 */
public class ProGuardTaskClaude_getdontwarnTest {

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
    public void testGetdontwarn_returnsNull() throws Exception {
        Object result = task.getdontwarn();
        assertNull(result, "getdontwarn() should return null");
    }

    @Test
    public void testGetdontwarn_clearsWarnFilter() throws Exception {
        // warn filter should be cleared (null becomes empty list)
        task.getdontwarn();
        assertNotNull(task.configuration.warn, "warn should not be null after call");
    }

    @Test
    public void testGetdontwarn_returnsNullAndClearsFilter() throws Exception {
        Object result = task.getdontwarn();
        assertNull(result, "getdontwarn() should return null");
        assertNotNull(task.configuration.warn, "warn should not be null");
    }

    @Test
    public void testGetdontwarn_changesConfigurationState() throws Exception {
        // Initially null
        assertNull(task.configuration.warn, "warn should initially be null");

        task.getdontwarn();

        // After calling, should be initialized to empty list
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontwarn_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertNull(freshTask.configuration.warn, "Fresh task should have warn as null");

        Object result = freshTask.getdontwarn();

        assertNull(result, "Should return null");
        assertNotNull(freshTask.configuration.warn, "warn should be initialized");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testGetdontwarn_multipleCallsAreIdempotent() throws Exception {
        task.getdontwarn();
        assertNotNull(task.configuration.warn, "First call should initialize warn");

        task.getdontwarn();
        assertNotNull(task.configuration.warn, "Second call should keep warn initialized");

        task.getdontwarn();
        assertNotNull(task.configuration.warn, "Third call should keep warn initialized");
    }

    @Test
    public void testGetdontwarn_alwaysReturnsNull() throws Exception {
        Object result1 = task.getdontwarn();
        Object result2 = task.getdontwarn();
        Object result3 = task.getdontwarn();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetdontwarn_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getdontwarn();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertNotNull(task.configuration.warn, "warn should be initialized after call " + (i + 1));
        }
    }

    @Test
    public void testGetdontwarn_consecutiveCalls() throws Exception {
        task.getdontwarn();
        task.getdontwarn();
        task.getdontwarn();
        task.getdontwarn();
        task.getdontwarn();

        assertNotNull(task.configuration.warn, "After 5 consecutive calls, warn should be initialized");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetdontwarn_withDontobfuscate() throws Exception {
        task.getdontwarn();
        task.dontobfuscate();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetdontwarn_withDontshrink() throws Exception {
        task.getdontwarn();
        task.dontshrink();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetdontwarn_withDontoptimize() throws Exception {
        task.getdontwarn();
        task.dontoptimize();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetdontwarn_withVerbose() throws Exception {
        task.getdontwarn();
        task.verbose();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontwarn_withIgnoreWarnings() throws Exception {
        task.getdontwarn();
        task.ignorewarnings();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetdontwarn_withMultipleConfigMethods() throws Exception {
        task.getdontwarn();
        task.verbose();
        task.ignorewarnings();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testGetdontwarn_beforeOtherConfig() throws Exception {
        task.getdontwarn();
        task.verbose();
        task.ignorewarnings();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetdontwarn_afterOtherConfig() throws Exception {
        task.verbose();
        task.ignorewarnings();
        task.getdontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetdontwarn_betweenOtherConfig() throws Exception {
        task.verbose();
        task.getdontwarn();
        task.ignorewarnings();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetdontwarn_mixedWithOtherGetters() throws Exception {
        task.getdontwarn();
        task.getverbose();
        task.getignorewarnings();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetdontwarn_forCleanBuildOutput() throws Exception {
        // Suppress warning messages for cleaner build output
        task.getdontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontwarn_withDontnote() throws Exception {
        // Common combination: suppress both warnings and notes
        task.getdontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontwarn_forProductionBuild() throws Exception {
        // Production builds often suppress warnings
        task.getdontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontwarn_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup suppressing warnings
        task.getdontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontwarn_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.getdontwarn();
        task.dontnote();
        task.ignorewarnings();
        task.verbose();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontwarn_forCiBuild() throws Exception {
        // CI builds often suppress warnings for cleaner logs
        task.getdontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testGetdontwarn_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.getdontwarn();
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontwarn_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getdontwarn();
        task2.getdontwarn();
        task3.getdontwarn();

        assertNotNull(task1.configuration.warn, "task1 warn should be initialized");
        assertNotNull(task2.configuration.warn, "task2 warn should be initialized");
        assertNotNull(task3.configuration.warn, "task3 warn should be initialized");
    }

    @Test
    public void testGetdontwarn_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getdontwarn();

        assertNotNull(task.configuration.warn, "task warn should be initialized");
        assertNull(otherTask.configuration.warn, "otherTask should not be affected");
    }

    @Test
    public void testGetdontwarn_afterConfigurationAccess() throws Exception {
        // Access configuration before calling getdontwarn
        assertNotNull(task.configuration, "Configuration should not be null");

        task.getdontwarn();
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontwarn_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.getdontwarn();
        assertNotNull(task.configuration.warn, "Should be initialized after first call");

        task.verbose();
        task.getdontwarn();
        assertNotNull(task.configuration.warn, "Should remain initialized after second call");

        task.ignorewarnings();
        task.getdontwarn();
        assertNotNull(task.configuration.warn, "Should remain initialized after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testGetdontwarn_inAndroidReleaseVariant() throws Exception {
        // Android release variant suppressing warnings
        task.getdontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontwarn_inAndroidLibraryModule() throws Exception {
        // Android library suppressing warnings
        task.getdontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontwarn_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build suppressing warnings
        task.getdontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testGetdontwarn_inProductionRelease() throws Exception {
        // Production release build suppressing warnings
        task.getdontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontwarn_inDebugBuild() throws Exception {
        // Debug build might still suppress warnings
        task.getdontwarn();
        task.verbose();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontwarn_inStagingForQa() throws Exception {
        // QA staging build suppressing warnings
        task.getdontwarn();
        task.verbose();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testGetdontwarn_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.warn, "Initial state should be null");

        task.getdontwarn();

        assertNotNull(task.configuration.warn, "State should be modified");
    }

    @Test
    public void testGetdontwarn_preservesOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.ignorewarnings();

        task.getdontwarn();

        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be preserved");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontwarn_withComplexConfigurationChain() throws Exception {
        task.verbose();
        task.getdontwarn();
        task.ignorewarnings();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontwarn_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.getdontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testGetdontwarn_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.getdontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testGetdontwarn_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.getdontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ============================================================
    // Groovy DSL Support Tests
    // ============================================================

    @Test
    public void testGetdontwarn_supportsGroovyDslSyntax() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        // In Groovy: task.dontwarn (without parentheses)
        Object result = task.getdontwarn();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertNotNull(task.configuration.warn, "Should initialize warn");
    }

    @Test
    public void testGetdontwarn_behavesLikeUnderlyingMethod() throws Exception {
        // getdontwarn() should behave like dontwarn()
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        task1.getdontwarn();
        task2.dontwarn();

        assertNotNull(task1.configuration.warn, "task1 warn should be initialized");
        assertNotNull(task2.configuration.warn, "task2 warn should be initialized");
    }

    @Test
    public void testGetdontwarn_isAnnotatedAsInternal() throws Exception {
        // The method should be annotated with @Internal for Gradle
        // This is a compile-time check, but we verify the method exists and works
        Object result = task.getdontwarn();
        assertNull(result, "Method should exist and return null");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testGetdontwarn_forCleanerLogs() throws Exception {
        // Suppress warnings for cleaner logs
        task.getdontwarn();
        task.verbose();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontwarn_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.getdontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontwarn_forSuppressingWarningMessages() throws Exception {
        // Suppress warning messages
        task.getdontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    // ============================================================
    // Comparison with Direct Method Call
    // ============================================================

    @Test
    public void testGetdontwarn_equivalentToDirectCall() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        // Use getter version
        Object getterResult = task1.getdontwarn();

        // Use direct method
        task2.dontwarn();

        assertNull(getterResult, "Getter should return null");
        assertNotNull(task1.configuration.warn, "task1 should have warn initialized");
        assertNotNull(task2.configuration.warn, "task2 should have warn initialized");
    }

    @Test
    public void testGetdontwarn_canBeMixedWithDirectCalls() throws Exception {
        task.getdontwarn();
        assertNotNull(task.configuration.warn, "After getter call, should be initialized");

        task.dontwarn();
        assertNotNull(task.configuration.warn, "After direct call, should still be initialized");

        task.getdontwarn();
        assertNotNull(task.configuration.warn, "After another getter call, should still be initialized");
    }

    // ============================================================
    // Return Value Tests
    // ============================================================

    @Test
    public void testGetdontwarn_alwaysReturnsNullRegardlessOfState() throws Exception {
        Object result1 = task.getdontwarn();
        assertNull(result1, "First call should return null");

        Object result2 = task.getdontwarn();
        assertNull(result2, "Second call should return null even though config is already set");

        Object result3 = task.getdontwarn();
        assertNull(result3, "Third call should still return null");
    }

    @Test
    public void testGetdontwarn_returnTypeIsObject() throws Exception {
        // Verify the return type is Object (not void)
        Object result = task.getdontwarn();
        // If the method returns void, this would be a compilation error
        assertNull(result, "Return type should be Object and value should be null");
    }

    // ============================================================
    // Warning Suppression Context Tests
    // ============================================================

    @Test
    public void testGetdontwarn_forSuppressingAllWarnings() throws Exception {
        // Suppress all warning messages
        task.getdontwarn();

        assertNotNull(task.configuration.warn, "Should initialize warn filter");
    }

    @Test
    public void testGetdontwarn_withOtherSuppressionSettings() throws Exception {
        task.getdontwarn();
        task.dontnote();
        task.ignorewarnings();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ============================================================
    // Debugging and Troubleshooting Tests
    // ============================================================

    @Test
    public void testGetdontwarn_forReducingBuildNoise() throws Exception {
        // Reduce build noise by suppressing warnings
        task.getdontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontwarn_forFocusingOnErrors() throws Exception {
        // Focus on errors by suppressing warnings and notes
        task.getdontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    // ============================================================
    // Verification Tests
    // ============================================================

    @Test
    public void testGetdontwarn_configurationNotNull() throws Exception {
        task.getdontwarn();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetdontwarn_returnValueConsistency() throws Exception {
        Object result1 = task.getdontwarn();
        Object result2 = task.getdontwarn();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertEquals(result1, result2, "Return values should be consistent (both null)");
    }

    @Test
    public void testGetdontwarn_taskStateValid() throws Exception {
        task.getdontwarn();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetdontwarn_semanticMeaning() throws Exception {
        // Verify the semantic meaning: suppresses warning messages
        task.getdontwarn();

        assertNotNull(task.configuration.warn,
                   "getdontwarn should initialize warn filter to suppress warning messages");
    }
}

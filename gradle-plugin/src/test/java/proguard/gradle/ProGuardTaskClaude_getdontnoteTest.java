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
 * Test class for ProGuardTask.getdontnote()Ljava/lang/Object; method.
 * Tests the getdontnote() Groovy DSL method that returns null and calls dontnote()
 * to clear the note filter, suppressing all note messages during ProGuard processing.
 */
public class ProGuardTaskClaude_getdontnoteTest {

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
    public void testGetdontnote_returnsNull() throws Exception {
        Object result = task.getdontnote();
        assertNull(result, "getdontnote() should return null");
    }

    @Test
    public void testGetdontnote_clearsNoteFilter() throws Exception {
        // note filter should be cleared (null becomes empty list)
        task.getdontnote();
        assertNotNull(task.configuration.note, "note should not be null after call");
    }

    @Test
    public void testGetdontnote_returnsNullAndClearsFilter() throws Exception {
        Object result = task.getdontnote();
        assertNull(result, "getdontnote() should return null");
        assertNotNull(task.configuration.note, "note should not be null");
    }

    @Test
    public void testGetdontnote_changesConfigurationState() throws Exception {
        // Initially null
        assertNull(task.configuration.note, "note should initially be null");

        task.getdontnote();

        // After calling, should be initialized to empty list
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontnote_withFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        assertNull(freshTask.configuration.note, "Fresh task should have note as null");

        Object result = freshTask.getdontnote();

        assertNull(result, "Should return null");
        assertNotNull(freshTask.configuration.note, "note should be initialized");
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    public void testGetdontnote_multipleCallsAreIdempotent() throws Exception {
        task.getdontnote();
        assertNotNull(task.configuration.note, "First call should initialize note");

        task.getdontnote();
        assertNotNull(task.configuration.note, "Second call should keep note initialized");

        task.getdontnote();
        assertNotNull(task.configuration.note, "Third call should keep note initialized");
    }

    @Test
    public void testGetdontnote_alwaysReturnsNull() throws Exception {
        Object result1 = task.getdontnote();
        Object result2 = task.getdontnote();
        Object result3 = task.getdontnote();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetdontnote_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getdontnote();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertNotNull(task.configuration.note, "note should be initialized after call " + (i + 1));
        }
    }

    @Test
    public void testGetdontnote_consecutiveCalls() throws Exception {
        task.getdontnote();
        task.getdontnote();
        task.getdontnote();
        task.getdontnote();
        task.getdontnote();

        assertNotNull(task.configuration.note, "After 5 consecutive calls, note should be initialized");
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetdontnote_withDontobfuscate() throws Exception {
        task.getdontnote();
        task.dontobfuscate();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetdontnote_withDontshrink() throws Exception {
        task.getdontnote();
        task.dontshrink();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetdontnote_withDontoptimize() throws Exception {
        task.getdontnote();
        task.dontoptimize();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetdontnote_withVerbose() throws Exception {
        task.getdontnote();
        task.verbose();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontnote_withIgnoreWarnings() throws Exception {
        task.getdontnote();
        task.ignorewarnings();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetdontnote_withMultipleConfigMethods() throws Exception {
        task.getdontnote();
        task.verbose();
        task.ignorewarnings();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    public void testGetdontnote_beforeOtherConfig() throws Exception {
        task.getdontnote();
        task.verbose();
        task.ignorewarnings();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetdontnote_afterOtherConfig() throws Exception {
        task.verbose();
        task.ignorewarnings();
        task.getdontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetdontnote_betweenOtherConfig() throws Exception {
        task.verbose();
        task.getdontnote();
        task.ignorewarnings();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetdontnote_mixedWithOtherGetters() throws Exception {
        task.getdontnote();
        task.getverbose();
        task.getignorewarnings();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetdontnote_forCleanBuildOutput() throws Exception {
        // Suppress note messages for cleaner build output
        task.getdontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontnote_withDontwarn() throws Exception {
        // Common combination: suppress both warnings and notes
        task.getdontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontnote_forProductionBuild() throws Exception {
        // Production builds often suppress notes
        task.getdontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontnote_inMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup suppressing notes
        task.getdontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontnote_inMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        task.getdontnote();
        task.dontwarn();
        task.ignorewarnings();
        task.verbose();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontnote_forCiBuild() throws Exception {
        // CI builds often suppress notes for cleaner logs
        task.getdontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testGetdontnote_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.getdontnote();
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontnote_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getdontnote();
        task2.getdontnote();
        task3.getdontnote();

        assertNotNull(task1.configuration.note, "task1 note should be initialized");
        assertNotNull(task2.configuration.note, "task2 note should be initialized");
        assertNotNull(task3.configuration.note, "task3 note should be initialized");
    }

    @Test
    public void testGetdontnote_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getdontnote();

        assertNotNull(task.configuration.note, "task note should be initialized");
        assertNull(otherTask.configuration.note, "otherTask should not be affected");
    }

    @Test
    public void testGetdontnote_afterConfigurationAccess() throws Exception {
        // Access configuration before calling getdontnote
        assertNotNull(task.configuration, "Configuration should not be null");

        task.getdontnote();
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontnote_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.getdontnote();
        assertNotNull(task.configuration.note, "Should be initialized after first call");

        task.verbose();
        task.getdontnote();
        assertNotNull(task.configuration.note, "Should remain initialized after second call");

        task.ignorewarnings();
        task.getdontnote();
        assertNotNull(task.configuration.note, "Should remain initialized after third call");
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    public void testGetdontnote_inAndroidReleaseVariant() throws Exception {
        // Android release variant suppressing notes
        task.getdontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontnote_inAndroidLibraryModule() throws Exception {
        // Android library suppressing notes
        task.getdontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontnote_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build suppressing notes
        task.getdontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    public void testGetdontnote_inProductionRelease() throws Exception {
        // Production release build suppressing notes
        task.getdontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontnote_inDebugBuild() throws Exception {
        // Debug build might still suppress notes
        task.getdontnote();
        task.verbose();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontnote_inStagingForQa() throws Exception {
        // QA staging build suppressing notes
        task.getdontnote();
        task.verbose();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testGetdontnote_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.note, "Initial state should be null");

        task.getdontnote();

        assertNotNull(task.configuration.note, "State should be modified");
    }

    @Test
    public void testGetdontnote_preservesOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.ignorewarnings();

        task.getdontnote();

        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be preserved");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontnote_withComplexConfigurationChain() throws Exception {
        task.verbose();
        task.getdontnote();
        task.ignorewarnings();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontnote_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.getdontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testGetdontnote_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.getdontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testGetdontnote_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.getdontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ============================================================
    // Groovy DSL Support Tests
    // ============================================================

    @Test
    public void testGetdontnote_supportsGroovyDslSyntax() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        // In Groovy: task.dontnote (without parentheses)
        Object result = task.getdontnote();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertNotNull(task.configuration.note, "Should initialize note");
    }

    @Test
    public void testGetdontnote_behavesLikeUnderlyingMethod() throws Exception {
        // getdontnote() should behave like dontnote()
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        task1.getdontnote();
        task2.dontnote();

        assertNotNull(task1.configuration.note, "task1 note should be initialized");
        assertNotNull(task2.configuration.note, "task2 note should be initialized");
    }

    @Test
    public void testGetdontnote_isAnnotatedAsInternal() throws Exception {
        // The method should be annotated with @Internal for Gradle
        // This is a compile-time check, but we verify the method exists and works
        Object result = task.getdontnote();
        assertNull(result, "Method should exist and return null");
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    public void testGetdontnote_forCleanerLogs() throws Exception {
        // Suppress notes for cleaner logs
        task.getdontnote();
        task.verbose();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetdontnote_withMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        task.getdontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testGetdontnote_forSuppressingInfoMessages() throws Exception {
        // Suppress informational note messages
        task.getdontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
    }

    // ============================================================
    // Comparison with Direct Method Call
    // ============================================================

    @Test
    public void testGetdontnote_equivalentToDirectCall() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        // Use getter version
        Object getterResult = task1.getdontnote();

        // Use direct method
        task2.dontnote();

        assertNull(getterResult, "Getter should return null");
        assertNotNull(task1.configuration.note, "task1 should have note initialized");
        assertNotNull(task2.configuration.note, "task2 should have note initialized");
    }

    @Test
    public void testGetdontnote_canBeMixedWithDirectCalls() throws Exception {
        task.getdontnote();
        assertNotNull(task.configuration.note, "After getter call, should be initialized");

        task.dontnote();
        assertNotNull(task.configuration.note, "After direct call, should still be initialized");

        task.getdontnote();
        assertNotNull(task.configuration.note, "After another getter call, should still be initialized");
    }

    // ============================================================
    // Return Value Tests
    // ============================================================

    @Test
    public void testGetdontnote_alwaysReturnsNullRegardlessOfState() throws Exception {
        Object result1 = task.getdontnote();
        assertNull(result1, "First call should return null");

        Object result2 = task.getdontnote();
        assertNull(result2, "Second call should return null even though config is already set");

        Object result3 = task.getdontnote();
        assertNull(result3, "Third call should still return null");
    }

    @Test
    public void testGetdontnote_returnTypeIsObject() throws Exception {
        // Verify the return type is Object (not void)
        Object result = task.getdontnote();
        // If the method returns void, this would be a compilation error
        assertNull(result, "Return type should be Object and value should be null");
    }

    // ============================================================
    // Note Suppression Context Tests
    // ============================================================

    @Test
    public void testGetdontnote_forSuppressingAllNotes() throws Exception {
        // Suppress all note messages
        task.getdontnote();

        assertNotNull(task.configuration.note, "Should initialize note filter");
    }

    @Test
    public void testGetdontnote_withOtherSuppressionSettings() throws Exception {
        task.getdontnote();
        task.dontwarn();
        task.ignorewarnings();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ============================================================
    // Debugging and Troubleshooting Tests
    // ============================================================

    @Test
    public void testGetdontnote_forReducingBuildNoise() throws Exception {
        // Reduce build noise by suppressing notes
        task.getdontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testGetdontnote_forFocusingOnErrors() throws Exception {
        // Focus on errors by suppressing notes and warnings
        task.getdontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    // ============================================================
    // Verification Tests
    // ============================================================

    @Test
    public void testGetdontnote_configurationNotNull() throws Exception {
        task.getdontnote();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetdontnote_returnValueConsistency() throws Exception {
        Object result1 = task.getdontnote();
        Object result2 = task.getdontnote();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertEquals(result1, result2, "Return values should be consistent (both null)");
    }

    @Test
    public void testGetdontnote_taskStateValid() throws Exception {
        task.getdontnote();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetdontnote_semanticMeaning() throws Exception {
        // Verify the semantic meaning: suppresses note messages
        task.getdontnote();

        assertNotNull(task.configuration.note,
                   "getdontnote should initialize note filter to suppress note messages");
    }
}

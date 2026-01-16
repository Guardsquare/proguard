package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ProGuardTask.verbose() method.
 * This is a void method that sets configuration.verbose = true.
 * Verbose mode enables detailed logging output during ProGuard processing.
 */
public class ProGuardTaskClaude_verboseTest {

    @TempDir
    public Path temporaryFolder;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(temporaryFolder.toFile())
                .build();
        task = project.getTasks().create("proguard", ProGuardTask.class);
    }

    @AfterEach
    public void tearDown() {
        project = null;
        task = null;
    }

    // ========================================
    // Tests for verbose() Method
    // ========================================

    @Test
    public void testVerbose_setsVerboseToTrue() throws Exception {
        assertFalse(task.configuration.verbose, "verbose should initially be false");

        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be set to true");
    }

    @Test
    public void testVerbose_isIdempotent() throws Exception {
        task.verbose();
        boolean firstResult = task.configuration.verbose;

        task.verbose();

        assertEquals(firstResult, task.configuration.verbose,
                    "Multiple calls should not change the state");
        assertTrue(task.configuration.verbose, "verbose should remain true");
    }

    @Test
    public void testVerbose_multipleCalls() throws Exception {
        task.verbose();
        task.verbose();
        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should remain true after multiple calls");
    }

    @Test
    public void testVerbose_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.verbose();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testVerbose_afterManuallySetToFalse() throws Exception {
        task.configuration.verbose = false;

        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be set to true");
    }

    @Test
    public void testVerbose_whenAlreadyTrue() throws Exception {
        task.configuration.verbose = true;

        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should remain true");
    }

    @Test
    public void testVerbose_doesNotReturnValue() throws Exception {
        // This is a void method, just verify it executes without error
        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testVerbose_initialStateIsFalse() throws Exception {
        assertFalse(task.configuration.verbose, "Default verbose should be false");

        task.verbose();

        assertTrue(task.configuration.verbose, "Should change from false to true");
    }

    @Test
    public void testVerbose_persistsAcrossGets() throws Exception {
        task.verbose();
        boolean value1 = task.configuration.verbose;
        boolean value2 = task.configuration.verbose;

        assertTrue(value1, "First read should be true");
        assertTrue(value2, "Second read should be true");
        assertEquals(value1, value2, "Value should be consistent");
    }

    @Test
    public void testVerbose_affectsOnlyVerboseFlag() throws Exception {
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;
        boolean shrinkBefore = task.configuration.shrink;

        task.verbose();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate, "obfuscate should not change");
        assertEquals(shrinkBefore, task.configuration.shrink, "shrink should not change");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testVerbose_withOtherConfigurationSettings() throws Exception {
        task.dontoptimize();
        task.android();

        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.android, "android should remain set");
    }

    @Test
    public void testVerbose_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.verbose();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testVerbose_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testVerbose_withObfuscationEnabled() throws Exception {
        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.obfuscate, "obfuscation should remain enabled");
    }

    @Test
    public void testVerbose_withOptimizationEnabled() throws Exception {
        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.optimize, "optimization should remain enabled");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testVerbose_forDebugging() throws Exception {
        // Enable verbose mode for debugging ProGuard issues
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose mode for debugging");
    }

    @Test
    public void testVerbose_forDetailedLogging() throws Exception {
        // Verbose mode provides detailed logging
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable detailed logging");
    }

    @Test
    public void testVerbose_forDevelopment() throws Exception {
        // Development builds often use verbose mode
        task.verbose();
        task.dontobfuscate();

        assertTrue(task.configuration.verbose, "Should enable verbose mode for development");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for development");
    }

    @Test
    public void testVerbose_forTroubleshooting() throws Exception {
        // Troubleshooting ProGuard warnings/errors
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose mode for troubleshooting");
    }

    @Test
    public void testVerbose_withAndroidDebugBuild() throws Exception {
        // Android debug builds with verbose logging
        task.android();
        task.verbose();
        task.dontobfuscate();

        assertTrue(task.configuration.verbose, "Should enable verbose mode for debug build");
        assertTrue(task.configuration.android, "Should be in Android mode");
    }

    @Test
    public void testVerbose_forCICD() throws Exception {
        // CI/CD builds often enable verbose for logs
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose mode for CI/CD");
    }

    @Test
    public void testVerbose_forUnderstandingProGuardBehavior() throws Exception {
        // Understanding what ProGuard is doing
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose to understand ProGuard behavior");
    }

    @Test
    public void testVerbose_forBuildDiagnostics() throws Exception {
        // Build diagnostics and analysis
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for build diagnostics");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testVerbose_afterManuallySettingConfiguration() throws Exception {
        task.configuration.verbose = false;
        task.configuration.optimize = true;

        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be set to true");
        assertTrue(task.configuration.optimize, "Other settings should be preserved");
    }

    @Test
    public void testVerbose_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testVerbose_calledFirst() throws Exception {
        // Test calling verbose before any other configuration
        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testVerbose_calledLast() throws Exception {
        // Test calling verbose after other configuration
        task.dontoptimize();
        task.keep("public class * { *; }");
        task.android();

        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ========================================
    // Logging and Output Scenarios
    // ========================================

    @Test
    public void testVerbose_enablesDetailedOutput() throws Exception {
        // Verbose mode enables detailed output
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable detailed output");
    }

    @Test
    public void testVerbose_forWarningAnalysis() throws Exception {
        // Analyzing ProGuard warnings
        task.verbose();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.verbose, "Should enable verbose for warning analysis");
    }

    @Test
    public void testVerbose_forOptimizationAnalysis() throws Exception {
        // Analyzing optimization steps
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for optimization analysis");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
    }

    @Test
    public void testVerbose_forPerformanceAnalysis() throws Exception {
        // Performance analysis of ProGuard processing
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for performance analysis");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testVerbose_beforeDontoptimize() throws Exception {
        task.verbose();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testVerbose_afterDontoptimize() throws Exception {
        task.dontoptimize();
        task.verbose();

        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testVerbose_betweenOtherCalls() throws Exception {
        task.android();
        task.verbose();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "android should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testVerbose_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.verbose();
        task.keepattributes("*Annotation*,Signature");
        task.android();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testVerbose_forDebugBuild() throws Exception {
        // Debug builds typically use verbose
        task.verbose();
        task.dontobfuscate();

        assertTrue(task.configuration.verbose, "Should enable verbose for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testVerbose_forReleaseBuild() throws Exception {
        // Release builds might use verbose for verification
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for release");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    @Test
    public void testVerbose_forLibraryModule() throws Exception {
        // Library modules might use verbose
        task.verbose();
        task.dontobfuscate();

        assertTrue(task.configuration.verbose, "Should enable verbose for library");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for library");
    }

    @Test
    public void testVerbose_forApplicationModule() throws Exception {
        // Application modules might use verbose for troubleshooting
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for application");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for application");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testVerbose_configurationStatePreserved() throws Exception {
        task.dontoptimize();
        task.android();
        task.keep("public class * { *; }");

        boolean optimizeBefore = task.configuration.optimize;
        boolean androidBefore = task.configuration.android;

        task.verbose();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertEquals(androidBefore, task.configuration.android, "android state should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testVerbose_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.verbose();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testVerbose_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.verbose();

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testVerbose_forRuleVerification() throws Exception {
        // Verifying keep rules are working correctly
        task.verbose();
        task.keep("public class * { *; }");

        assertTrue(task.configuration.verbose, "Should enable verbose for rule verification");
    }

    @Test
    public void testVerbose_withAllProcessingSteps() throws Exception {
        // Show all processing steps
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose to show all processing steps");
    }

    @Test
    public void testVerbose_forShrinkingAnalysis() throws Exception {
        // Analyzing what gets removed during shrinking
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for shrinking analysis");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
    }

    @Test
    public void testVerbose_forObfuscationAnalysis() throws Exception {
        // Analyzing obfuscation mappings
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for obfuscation analysis");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testVerbose_forKotlinProjects() throws Exception {
        // Kotlin projects with verbose logging
        task.verbose();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.verbose, "Should enable verbose for Kotlin projects");
    }

    @Test
    public void testVerbose_forMultiModuleProjects() throws Exception {
        // Multi-module projects with verbose logging
        task.verbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for multi-module projects");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testVerbose_configurationNotNull() throws Exception {
        task.verbose();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testVerbose_taskStateValid() throws Exception {
        task.verbose();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testVerbose_withMinimalConfiguration() throws Exception {
        // Test with only verbose called
        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testVerbose_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.verbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.shrink, "shrink should be disabled");
    }

    @Test
    public void testVerbose_semanticMeaning() throws Exception {
        // Verify the semantic meaning: enables verbose logging
        task.verbose();

        assertTrue(task.configuration.verbose,
                   "verbose should enable verbose mode (set verbose to true)");
    }

    @Test
    public void testVerbose_changesConfiguration() throws Exception {
        boolean before = task.configuration.verbose;

        task.verbose();
        boolean after = task.configuration.verbose;

        assertFalse(before, "verbose should initially be false");
        assertTrue(after, "verbose should be true after calling verbose");
        assertNotEquals(before, after, "Configuration should have changed");
    }
}

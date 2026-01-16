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
 * Comprehensive tests for ProGuardTask.getverbose() method.
 * This is a Groovy DSL getter that returns null and calls verbose()
 * to set configuration.verbose = true.
 * Verbose mode enables detailed logging output during ProGuard processing.
 */
public class ProGuardTaskClaude_getverboseTest {

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
    // Tests for getverbose() Method
    // ========================================

    @Test
    public void testGetverbose_returnsNull() throws Exception {
        Object result = task.getverbose();

        assertNull(result, "getverbose should return null for Groovy DSL support");
    }

    @Test
    public void testGetverbose_setsVerboseToTrue() throws Exception {
        assertFalse(task.configuration.verbose, "verbose should initially be false");

        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should be set to true");
    }

    @Test
    public void testGetverbose_callsVerbose() throws Exception {
        // getverbose should call verbose()
        assertFalse(task.configuration.verbose, "verbose should initially be false");

        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose() should have been called");
    }

    @Test
    public void testGetverbose_isIdempotent() throws Exception {
        task.getverbose();
        boolean firstResult = task.configuration.verbose;

        task.getverbose();

        assertEquals(firstResult, task.configuration.verbose,
                    "Multiple calls should not change the state");
        assertTrue(task.configuration.verbose, "verbose should remain true");
    }

    @Test
    public void testGetverbose_multipleCalls() throws Exception {
        task.getverbose();
        task.getverbose();
        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should remain true after multiple calls");
    }

    @Test
    public void testGetverbose_alwaysReturnsNull() throws Exception {
        Object result1 = task.getverbose();
        Object result2 = task.getverbose();
        Object result3 = task.getverbose();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetverbose_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getverbose();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetverbose_afterManuallySetToFalse() throws Exception {
        task.configuration.verbose = false;

        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should be set to true");
    }

    @Test
    public void testGetverbose_whenAlreadyTrue() throws Exception {
        task.configuration.verbose = true;

        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should remain true");
    }

    @Test
    public void testGetverbose_supportsGroovyDSL() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        Object result = task.getverbose();

        assertNull(result, "Should return null for Groovy property access");
        assertTrue(task.configuration.verbose, "Should enable verbose mode");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testGetverbose_withOtherConfigurationSettings() throws Exception {
        task.dontoptimize();
        task.android();

        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.android, "android should remain set");
    }

    @Test
    public void testGetverbose_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getverbose();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetverbose_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testGetverbose_forDebugging() throws Exception {
        // Enable verbose mode for debugging ProGuard issues
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable verbose mode for debugging");
    }

    @Test
    public void testGetverbose_forDetailedLogging() throws Exception {
        // Verbose mode provides detailed logging
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable detailed logging");
    }

    @Test
    public void testGetverbose_forDevelopment() throws Exception {
        // Development builds often use verbose mode
        task.getverbose();
        task.dontobfuscate();

        assertTrue(task.configuration.verbose, "Should enable verbose mode for development");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for development");
    }

    @Test
    public void testGetverbose_forTroubleshooting() throws Exception {
        // Troubleshooting ProGuard warnings/errors
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable verbose mode for troubleshooting");
    }

    @Test
    public void testGetverbose_withAndroidDebugBuild() throws Exception {
        // Android debug builds with verbose logging
        task.android();
        task.getverbose();
        task.dontobfuscate();

        assertTrue(task.configuration.verbose, "Should enable verbose mode for debug build");
        assertTrue(task.configuration.android, "Should be in Android mode");
    }

    @Test
    public void testGetverbose_forCICD() throws Exception {
        // CI/CD builds often enable verbose for logs
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable verbose mode for CI/CD");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testGetverbose_afterManuallySettingConfiguration() throws Exception {
        task.configuration.verbose = false;
        task.configuration.optimize = true;

        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should be set to true");
        assertTrue(task.configuration.optimize, "Other settings should be preserved");
    }

    @Test
    public void testGetverbose_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetverbose_calledFirst() throws Exception {
        // Test calling getverbose before any other configuration
        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetverbose_calledLast() throws Exception {
        // Test calling getverbose after other configuration
        task.dontoptimize();
        task.keep("public class * { *; }");
        task.android();

        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ========================================
    // Logging and Output Scenarios
    // ========================================

    @Test
    public void testGetverbose_enablesDetailedOutput() throws Exception {
        // Verbose mode enables detailed output
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable detailed output");
    }

    @Test
    public void testGetverbose_forWarningAnalysis() throws Exception {
        // Analyzing ProGuard warnings
        task.getverbose();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.verbose, "Should enable verbose for warning analysis");
    }

    @Test
    public void testGetverbose_forOptimizationAnalysis() throws Exception {
        // Analyzing optimization steps
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for optimization analysis");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testGetverbose_beforeDontoptimize() throws Exception {
        task.getverbose();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetverbose_afterDontoptimize() throws Exception {
        task.dontoptimize();
        task.getverbose();

        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetverbose_betweenOtherCalls() throws Exception {
        task.android();
        task.getverbose();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "android should be set");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testGetverbose_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.getverbose();
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
    public void testGetverbose_forDebugBuild() throws Exception {
        // Debug builds typically use verbose
        task.getverbose();
        task.dontobfuscate();

        assertTrue(task.configuration.verbose, "Should enable verbose for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testGetverbose_forReleaseBuild() throws Exception {
        // Release builds might use verbose for verification
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for release");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    @Test
    public void testGetverbose_forLibraryModule() throws Exception {
        // Library modules might use verbose
        task.getverbose();
        task.dontobfuscate();

        assertTrue(task.configuration.verbose, "Should enable verbose for library");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for library");
    }

    @Test
    public void testGetverbose_forApplicationModule() throws Exception {
        // Application modules might use verbose for troubleshooting
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for application");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for application");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testGetverbose_configurationStatePreserved() throws Exception {
        task.dontoptimize();
        task.android();
        task.keep("public class * { *; }");

        boolean optimizeBefore = task.configuration.optimize;
        boolean androidBefore = task.configuration.android;

        task.getverbose();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertEquals(androidBefore, task.configuration.android, "android state should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetverbose_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.getverbose();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testGetverbose_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.getverbose();

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testGetverbose_forUnderstandingProGuardBehavior() throws Exception {
        // Understanding what ProGuard is doing
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable verbose to understand ProGuard behavior");
    }

    @Test
    public void testGetverbose_forBuildDiagnostics() throws Exception {
        // Build diagnostics and analysis
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for build diagnostics");
    }

    @Test
    public void testGetverbose_forPerformanceAnalysis() throws Exception {
        // Performance analysis of ProGuard processing
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable verbose for performance analysis");
    }

    @Test
    public void testGetverbose_forRuleVerification() throws Exception {
        // Verifying keep rules are working correctly
        task.getverbose();
        task.keep("public class * { *; }");

        assertTrue(task.configuration.verbose, "Should enable verbose for rule verification");
    }

    @Test
    public void testGetverbose_withAllProcessingSteps() throws Exception {
        // Show all processing steps
        task.getverbose();

        assertTrue(task.configuration.verbose, "Should enable verbose to show all processing steps");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testGetverbose_configurationNotNull() throws Exception {
        task.getverbose();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetverbose_returnValueConsistency() throws Exception {
        Object result1 = task.getverbose();
        Object result2 = task.getverbose();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertEquals(result1, result2, "Return values should be consistent (both null)");
    }

    @Test
    public void testGetverbose_taskStateValid() throws Exception {
        task.getverbose();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetverbose_withMinimalConfiguration() throws Exception {
        // Test with only getverbose called
        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetverbose_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.getverbose();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.shrink, "shrink should be disabled");
    }

    @Test
    public void testGetverbose_semanticMeaning() throws Exception {
        // Verify the semantic meaning: enables verbose logging
        task.getverbose();

        assertTrue(task.configuration.verbose,
                   "getverbose should enable verbose mode (set verbose to true)");
    }
}

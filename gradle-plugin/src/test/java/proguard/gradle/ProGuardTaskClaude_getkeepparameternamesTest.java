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
 * Comprehensive tests for ProGuardTask.getkeepparameternames() method.
 * This is a Groovy DSL getter method that returns null and calls keepparameternames().
 */
public class ProGuardTaskClaude_getkeepparameternamesTest {

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
    // Tests for getkeepparameternames() Method
    // ========================================

    @Test
    public void testGetkeepparameternames_returnsNull() throws Exception {
        Object result = task.getkeepparameternames();

        assertNull(result, "getkeepparameternames should return null for Groovy DSL support");
    }

    @Test
    public void testGetkeepparameternames_setsKeepParameterNames() throws Exception {
        assertFalse(task.configuration.keepParameterNames, "keepParameterNames should initially be false");

        task.getkeepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set to true");
    }

    @Test
    public void testGetkeepparameternames_multipleCalls() throws Exception {
        task.getkeepparameternames();
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be true after first call");

        task.getkeepparameternames();
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should remain true after second call");

        task.getkeepparameternames();
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should remain true after third call");
    }

    @Test
    public void testGetkeepparameternames_isIdempotent() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.getkeepparameternames();
            assertTrue(task.configuration.keepParameterNames,
                      "keepParameterNames should remain true on iteration " + i);
        }
    }

    @Test
    public void testGetkeepparameternames_alwaysReturnsNull() throws Exception {
        Object result1 = task.getkeepparameternames();
        Object result2 = task.getkeepparameternames();
        Object result3 = task.getkeepparameternames();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetkeepparameternames_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getkeepparameternames();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    @Test
    public void testGetkeepparameternames_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.getkeepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertTrue(task.configuration.shrink == false, "dontshrink should remain set");
    }

    @Test
    public void testGetkeepparameternames_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getkeepparameternames();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testGetkeepparameternames_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getkeepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testGetkeepparameternames_withOptimizationEnabled() throws Exception {
        // When optimization is enabled, parameter names can still be kept
        task.getkeepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        // By default, optimization is enabled
        assertTrue(task.configuration.optimize, "optimization should be enabled by default");
    }

    @Test
    public void testGetkeepparameternames_withObfuscationEnabled() throws Exception {
        // Parameter names can be kept even with obfuscation
        task.getkeepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        // By default, obfuscation is enabled
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testGetkeepparameternames_forDebugging() throws Exception {
        // When debugging, you want to keep parameter names
        task.getkeepparameternames();
        task.keepattributes("SourceFile,LineNumberTable");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set for debugging");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
    }

    @Test
    public void testGetkeepparameternames_forReflection() throws Exception {
        // Parameter names might be needed for reflection
        task.getkeepparameternames();
        task.keepattributes("Signature");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keepAttributes, "Signature attribute should be kept");
    }

    @Test
    public void testGetkeepparameternames_withLibraryConfiguration() throws Exception {
        // Library builds often keep parameter names
        task.getkeepparameternames();
        task.dontobfuscate();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testGetkeepparameternames_minimalObfuscation() throws Exception {
        // Minimal obfuscation: obfuscate class/method names but keep parameter names
        task.getkeepparameternames();
        // Obfuscation is on by default, so parameter names stand out

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testGetkeepparameternames_developmentBuild() throws Exception {
        // Development builds typically keep parameter names for easier debugging
        task.getkeepparameternames();
        task.dontoptimize();
        task.verbose();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
        assertTrue(task.configuration.verbose, "verbose should be enabled");
    }

    @Test
    public void testGetkeepparameternames_withAnnotationProcessing() throws Exception {
        // Some annotation processors rely on parameter names
        task.getkeepparameternames();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keepAttributes, "Annotation attributes should be kept");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testGetkeepparameternames_afterConfigurationChanges() throws Exception {
        task.configuration.keepParameterNames = false;

        task.getkeepparameternames();

        assertTrue(task.configuration.keepParameterNames,
                  "keepParameterNames should be set to true even after being explicitly set to false");
    }

    @Test
    public void testGetkeepparameternames_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getkeepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    @Test
    public void testGetkeepparameternames_calledFirst() throws Exception {
        // Test calling getkeepparameternames before any other configuration
        task.getkeepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetkeepparameternames_calledLast() throws Exception {
        // Test calling getkeepparameternames after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.getkeepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testGetkeepparameternames_androidDebugBuild() throws Exception {
        // Debug builds typically keep parameter names
        task.getkeepparameternames();
        task.dontoptimize();
        task.keepattributes("SourceFile,LineNumberTable");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set for debug");
        assertFalse(task.configuration.optimize, "optimization should be disabled for debug");
    }

    @Test
    public void testGetkeepparameternames_androidLibrary() throws Exception {
        // Android libraries often keep parameter names for better API documentation
        task.getkeepparameternames();
        task.dontobfuscate();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for libraries");
    }

    @Test
    public void testGetkeepparameternames_androidReleaseBuildWithDebugging() throws Exception {
        // Release build but keeping parameter names for crash reports
        task.getkeepparameternames();
        task.keepattributes("SourceFile,LineNumberTable");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.optimize, "optimization should be enabled for release");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testGetkeepparameternames_beforeVerbose() throws Exception {
        task.getkeepparameternames();
        task.verbose();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testGetkeepparameternames_afterVerbose() throws Exception {
        task.verbose();
        task.getkeepparameternames();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    @Test
    public void testGetkeepparameternames_betweenOtherCalls() throws Exception {
        task.verbose();
        task.getkeepparameternames();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetkeepparameternames_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.getkeepparameternames();
        task.keepattributes("Signature,Exceptions");
        task.verbose();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testGetkeepparameternames_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.getkeepparameternames();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    @Test
    public void testGetkeepparameternames_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.getkeepparameternames();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testGetkeepparameternames_forJavaReflectionAPI() throws Exception {
        // Java reflection can access parameter names via reflection API
        task.getkeepparameternames();
        task.keepattributes("Signature,Exceptions");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    @Test
    public void testGetkeepparameternames_forFrameworksUsingReflection() throws Exception {
        // Many frameworks (Spring, JAX-RS, etc.) use parameter names
        task.getkeepparameternames();
        task.keepattributes("*Annotation*,Signature");
        task.keep("public class * { *; }");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keepAttributes, "Attributes should be kept");
    }

    @Test
    public void testGetkeepparameternames_forBetterStackTraces() throws Exception {
        // Parameter names improve stack trace readability
        task.getkeepparameternames();
        task.keepattributes("SourceFile,LineNumberTable");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keepAttributes, "Debug attributes should be kept");
    }

    @Test
    public void testGetkeepparameternames_withJavaDoc() throws Exception {
        // When generating JavaDoc, parameter names are important
        task.getkeepparameternames();
        task.dontobfuscate();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testGetkeepparameternames_forIDEDebugging() throws Exception {
        // IDEs use parameter names during debugging
        task.getkeepparameternames();
        task.keepattributes("SourceFile,LineNumberTable,LocalVariableTable");
        task.dontoptimize();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testGetkeepparameternames_configurationNotNull() throws Exception {
        task.getkeepparameternames();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetkeepparameternames_returnValueConsistency() throws Exception {
        Object result1 = task.getkeepparameternames();
        Object result2 = task.getkeepparameternames();

        assertEquals(result1, result2, "Return value should be consistent (both null)");
    }

    @Test
    public void testGetkeepparameternames_taskStateValid() throws Exception {
        task.getkeepparameternames();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetkeepparameternames_withMinimalConfiguration() throws Exception {
        // Test with only getkeepparameternames called
        Object result = task.getkeepparameternames();

        assertNull(result, "Should return null");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetkeepparameternames_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.getkeepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }
}

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
 * Comprehensive tests for ProGuardTask.keepparameternames() method.
 * This method sets the keepParameterNames flag to true.
 */
public class ProGuardTaskClaude_keepparameternamesTest {

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
    // Tests for keepparameternames() Method
    // ========================================

    @Test
    public void testKeepparameternames_setsKeepParameterNames() throws Exception {
        assertFalse(task.configuration.keepParameterNames, "keepParameterNames should initially be false");

        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set to true");
    }

    @Test
    public void testKeepparameternames_isVoid() throws Exception {
        // Verify method doesn't return a value (void method)
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    @Test
    public void testKeepparameternames_multipleCalls() throws Exception {
        task.keepparameternames();
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be true after first call");

        task.keepparameternames();
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should remain true after second call");

        task.keepparameternames();
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should remain true after third call");
    }

    @Test
    public void testKeepparameternames_isIdempotent() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.keepparameternames();
            assertTrue(task.configuration.keepParameterNames,
                      "keepParameterNames should remain true on iteration " + i);
        }
    }

    @Test
    public void testKeepparameternames_afterManuallySetToFalse() throws Exception {
        task.configuration.keepParameterNames = false;

        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames,
                  "keepParameterNames should be set to true even after being explicitly set to false");
    }

    @Test
    public void testKeepparameternames_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.keepparameternames();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    @Test
    public void testKeepparameternames_doesNotReturnValue() throws Exception {
        // This is a void method - just verify it executes without issues
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testKeepparameternames_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testKeepparameternames_withOptimizationEnabled() throws Exception {
        // When optimization is enabled, parameter names can still be kept
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.optimize, "optimization should be enabled by default");
    }

    @Test
    public void testKeepparameternames_withOptimizationDisabled() throws Exception {
        task.dontoptimize();
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
    }

    @Test
    public void testKeepparameternames_withObfuscationEnabled() throws Exception {
        // Parameter names can be kept even with obfuscation
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    @Test
    public void testKeepparameternames_withObfuscationDisabled() throws Exception {
        task.dontobfuscate();
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testKeepparameternames_withShrinkingDisabled() throws Exception {
        task.dontshrink();
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.shrink, "shrinking should be disabled");
    }

    @Test
    public void testKeepparameternames_withVerboseEnabled() throws Exception {
        task.verbose();
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testKeepparameternames_withKeepAttributes() throws Exception {
        task.keepattributes("SourceFile,LineNumberTable");
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
    }

    @Test
    public void testKeepparameternames_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.keepparameternames();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate, "obfuscate setting should not change");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testKeepparameternames_forDebugging() throws Exception {
        // When debugging, you want to keep parameter names
        task.keepparameternames();
        task.keepattributes("SourceFile,LineNumberTable,LocalVariableTable");
        task.dontoptimize();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set for debugging");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
        assertFalse(task.configuration.optimize, "optimization should be disabled for debugging");
    }

    @Test
    public void testKeepparameternames_forReflection() throws Exception {
        // Parameter names are needed for reflection-based frameworks
        task.keepparameternames();
        task.keepattributes("Signature,Exceptions");
        task.keep("public class * { *; }");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keepAttributes, "Signature attribute should be kept");
    }

    @Test
    public void testKeepparameternames_forLibraryBuild() throws Exception {
        // Library builds often keep parameter names for better API
        task.keepparameternames();
        task.dontobfuscate();
        task.keepattributes("Signature,Exceptions,InnerClasses");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testKeepparameternames_minimalObfuscation() throws Exception {
        // Minimal obfuscation: obfuscate class/method names but keep parameter names
        task.keepparameternames();
        // Obfuscation is on by default

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testKeepparameternames_developmentBuild() throws Exception {
        // Development builds typically keep everything for easier debugging
        task.keepparameternames();
        task.dontoptimize();
        task.dontobfuscate();
        task.dontshrink();
        task.verbose();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
        assertFalse(task.configuration.shrink, "shrinking should be disabled");
        assertTrue(task.configuration.verbose, "verbose should be enabled");
    }

    @Test
    public void testKeepparameternames_productionWithStackTraces() throws Exception {
        // Production build but keeping parameter names for better stack traces
        task.keepparameternames();
        task.keepattributes("SourceFile,LineNumberTable");
        // Optimization and obfuscation remain enabled

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.optimize, "optimization should be enabled for production");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for production");
    }

    @Test
    public void testKeepparameternames_withAnnotationProcessing() throws Exception {
        // Some annotation processors rely on parameter names
        task.keepparameternames();
        task.keepattributes("*Annotation*,Signature");
        task.keep("@interface * { *; }");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keepAttributes, "Annotation attributes should be kept");
    }

    @Test
    public void testKeepparameternames_forSpringFramework() throws Exception {
        // Spring framework can use parameter names for dependency injection
        task.keepparameternames();
        task.keepattributes("*Annotation*,Signature");
        task.keep("@org.springframework.** class * { *; }");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set for Spring");
    }

    @Test
    public void testKeepparameternames_forJAXRS() throws Exception {
        // JAX-RS uses parameter names for REST API parameters
        task.keepparameternames();
        task.keepattributes("*Annotation*,Signature,Exceptions");
        task.keep("@javax.ws.rs.** class * { *; }");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set for JAX-RS");
    }

    @Test
    public void testKeepparameternames_forTestCode() throws Exception {
        // Test code often benefits from keeping parameter names
        task.keepparameternames();
        task.keep("class *Test { *; }");
        task.keep("class *Tests { *; }");
        task.dontoptimize();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testKeepparameternames_calledFirst() throws Exception {
        // Test calling keepparameternames before any other configuration
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testKeepparameternames_calledLast() throws Exception {
        // Test calling keepparameternames after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    @Test
    public void testKeepparameternames_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.keepparameternames();
        task.keepattributes("Signature,Exceptions");
        task.verbose();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    @Test
    public void testKeepparameternames_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testKeepparameternames_androidDebugBuild() throws Exception {
        // Debug builds typically keep parameter names
        task.keepparameternames();
        task.dontoptimize();
        task.dontobfuscate();
        task.keepattributes("SourceFile,LineNumberTable,LocalVariableTable");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set for debug");
        assertFalse(task.configuration.optimize, "optimization should be disabled for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testKeepparameternames_androidReleaseBuild() throws Exception {
        // Release build typically obfuscates but might keep parameter names for debugging crashes
        task.keepparameternames();
        task.keepattributes("SourceFile,LineNumberTable");
        // Optimization and obfuscation enabled by default

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.optimize, "optimization should be enabled for release");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    @Test
    public void testKeepparameternames_androidLibrary() throws Exception {
        // Android libraries often keep parameter names for better API documentation
        task.keepparameternames();
        task.dontobfuscate();
        task.keepattributes("Signature,Exceptions,InnerClasses,EnclosingMethod");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for libraries");
    }

    @Test
    public void testKeepparameternames_androidWithRetrofit() throws Exception {
        // Retrofit uses parameter names for API parameters
        task.keepparameternames();
        task.keepattributes("*Annotation*,Signature");
        task.keep("interface * { @retrofit2.http.* <methods>; }");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set for Retrofit");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testKeepparameternames_beforeVerbose() throws Exception {
        task.keepparameternames();
        task.verbose();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testKeepparameternames_afterVerbose() throws Exception {
        task.verbose();
        task.keepparameternames();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    @Test
    public void testKeepparameternames_betweenOtherCalls() throws Exception {
        task.verbose();
        task.keepparameternames();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testKeepparameternames_multipleTimesWithOtherCalls() throws Exception {
        task.keepparameternames();
        task.verbose();
        task.keepparameternames();
        task.dontoptimize();
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testKeepparameternames_withAllProcessingSteps() throws Exception {
        // Test with all processing steps configured
        task.keepparameternames();
        // Shrinking, optimization, obfuscation all enabled by default

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testKeepparameternames_debugVariant() throws Exception {
        // Typical debug variant configuration
        task.keepparameternames();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.shrink, "shrinking should be disabled");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testKeepparameternames_releaseVariant() throws Exception {
        // Typical release variant configuration with parameter names kept
        task.keepparameternames();
        // All processing enabled by default

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testKeepparameternames_stagingVariant() throws Exception {
        // Staging variant: optimized but debuggable
        task.keepparameternames();
        task.keepattributes("SourceFile,LineNumberTable");
        task.dontobfuscate();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for staging");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testKeepparameternames_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.keepparameternames();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    @Test
    public void testKeepparameternames_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.keepparameternames();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    @Test
    public void testKeepparameternames_doesNotAffectFilterLists() throws Exception {
        task.keeppackagenames("com.example.*");
        task.keepattributes("Signature");

        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames filter should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes filter should be preserved");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testKeepparameternames_forJavaReflectionAPI() throws Exception {
        // Java reflection can access parameter names via Parameter.getName()
        task.keepparameternames();
        task.keepattributes("Signature,Exceptions");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set for reflection");
    }

    @Test
    public void testKeepparameternames_forFrameworksUsingReflection() throws Exception {
        // Many frameworks (Spring, JAX-RS, etc.) use parameter names
        task.keepparameternames();
        task.keepattributes("*Annotation*,Signature");
        task.keep("public class * { *; }");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keepAttributes, "Attributes should be kept");
    }

    @Test
    public void testKeepparameternames_forBetterStackTraces() throws Exception {
        // Parameter names improve stack trace readability
        task.keepparameternames();
        task.keepattributes("SourceFile,LineNumberTable");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration.keepAttributes, "Debug attributes should be kept");
    }

    @Test
    public void testKeepparameternames_withJavaDoc() throws Exception {
        // When generating JavaDoc, parameter names are important
        task.keepparameternames();
        task.dontobfuscate();
        task.keepattributes("Signature,Exceptions");

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testKeepparameternames_forIDEDebugging() throws Exception {
        // IDEs use parameter names during debugging
        task.keepparameternames();
        task.keepattributes("SourceFile,LineNumberTable,LocalVariableTable,LocalVariableTypeTable");
        task.dontoptimize();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
    }

    @Test
    public void testKeepparameternames_forCodeCoverage() throws Exception {
        // Code coverage tools may benefit from parameter names
        task.keepparameternames();
        task.keepattributes("SourceFile,LineNumberTable");
        task.dontoptimize();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testKeepparameternames_configurationNotNull() throws Exception {
        task.keepparameternames();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testKeepparameternames_taskStateValid() throws Exception {
        task.keepparameternames();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testKeepparameternames_withMinimalConfiguration() throws Exception {
        // Test with only keepparameternames called
        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testKeepparameternames_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.keepparameternames();

        assertTrue(task.configuration.keepParameterNames, "keepParameterNames should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testKeepparameternames_repeatedConfiguration() throws Exception {
        // Test setting and resetting configuration
        task.keepparameternames();
        assertTrue(task.configuration.keepParameterNames, "Should be true after first call");

        task.configuration.keepParameterNames = false;
        assertFalse(task.configuration.keepParameterNames, "Should be false after manual reset");

        task.keepparameternames();
        assertTrue(task.configuration.keepParameterNames, "Should be true again after second call");
    }
}

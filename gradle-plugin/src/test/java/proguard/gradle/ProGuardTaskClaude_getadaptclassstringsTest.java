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
 * Comprehensive tests for ProGuardTask.getadaptclassstrings() method.
 * This is a Groovy DSL getter method that returns null and calls adaptclassstrings().
 */
public class ProGuardTaskClaude_getadaptclassstringsTest {

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
    // Tests for getadaptclassstrings() Method
    // ========================================

    @Test
    public void testGetadaptclassstrings_returnsNull() throws Exception {
        Object result = task.getadaptclassstrings();

        assertNull(result, "getadaptclassstrings should return null for Groovy DSL support");
    }

    @Test
    public void testGetadaptclassstrings_initializesAdaptClassStrings() throws Exception {
        assertNull(task.configuration.adaptClassStrings, "adaptClassStrings should initially be null");

        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "adaptClassStrings should be empty (cleared to keep all)");
    }

    @Test
    public void testGetadaptclassstrings_multipleCalls() throws Exception {
        task.getadaptclassstrings();
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized after first call");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(), "adaptClassStrings should be empty after first call");

        task.getadaptclassstrings();
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should remain initialized after second call");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(), "adaptClassStrings should remain empty after second call");

        task.getadaptclassstrings();
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should remain initialized after third call");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(), "adaptClassStrings should remain empty after third call");
    }

    @Test
    public void testGetadaptclassstrings_isIdempotent() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.getadaptclassstrings();
            assertNotNull(task.configuration.adaptClassStrings,
                        "adaptClassStrings should be initialized on iteration " + i);
            assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                      "adaptClassStrings should be empty on iteration " + i);
        }
    }

    @Test
    public void testGetadaptclassstrings_alwaysReturnsNull() throws Exception {
        Object result1 = task.getadaptclassstrings();
        Object result2 = task.getadaptclassstrings();
        Object result3 = task.getadaptclassstrings();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetadaptclassstrings_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getadaptclassstrings();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testGetadaptclassstrings_clearsExistingFilter() throws Exception {
        // Manually add some filters
        task.adaptclassstrings("com.example.*");
        assertFalse(task.configuration.adaptClassStrings.isEmpty(),
                   "adaptClassStrings should contain filters");

        task.getadaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "adaptClassStrings should be cleared (to keep all)");
    }

    @Test
    public void testGetadaptclassstrings_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testGetadaptclassstrings_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getadaptclassstrings();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testGetadaptclassstrings_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testGetadaptclassstrings_withObfuscationEnabled() throws Exception {
        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    @Test
    public void testGetadaptclassstrings_withOptimizationEnabled() throws Exception {
        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.optimize, "optimization should be enabled by default");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testGetadaptclassstrings_forAdaptingAllClassStrings() throws Exception {
        // Adapt all class strings in the code
        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Empty filter means adapt all class strings");
    }

    @Test
    public void testGetadaptclassstrings_forReflectionSupport() throws Exception {
        // When using reflection with Class.forName, adapt class strings
        task.getadaptclassstrings();
        task.keepattributes("Signature");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertNotNull(task.configuration.keepAttributes, "Signature should be kept");
    }

    @Test
    public void testGetadaptclassstrings_forStringLiterals() throws Exception {
        // Adapt class name strings in string literals
        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testGetadaptclassstrings_withObfuscation() throws Exception {
        // adaptclassstrings works with obfuscation to update string literals
        task.getadaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testGetadaptclassstrings_forClassForName() throws Exception {
        // When code uses Class.forName("com.example.MyClass")
        task.getadaptclassstrings();
        task.keep("public class * { *; }");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testGetadaptclassstrings_forConfigurationFiles() throws Exception {
        // Adapt class names in configuration files or resources
        task.getadaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class name strings");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testGetadaptclassstrings_afterManuallySet() throws Exception {
        // Manually set adaptClassStrings
        task.adaptclassstrings("com.example.*");
        assertFalse(task.configuration.adaptClassStrings.isEmpty(),
                   "Should have filters after manual set");

        task.getadaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should be cleared after getadaptclassstrings call");
    }

    @Test
    public void testGetadaptclassstrings_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testGetadaptclassstrings_calledFirst() throws Exception {
        // Test calling getadaptclassstrings before any other configuration
        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetadaptclassstrings_calledLast() throws Exception {
        // Test calling getadaptclassstrings after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testGetadaptclassstrings_androidBuild() throws Exception {
        // Android apps often use reflection with class name strings
        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
    }

    @Test
    public void testGetadaptclassstrings_androidWithReflection() throws Exception {
        // Android code using Class.forName()
        task.getadaptclassstrings();
        task.keepattributes("Signature,Exceptions");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertNotNull(task.configuration.keepAttributes, "Attributes should be kept");
    }

    @Test
    public void testGetadaptclassstrings_androidReleaseBuild() throws Exception {
        // Release build with class string adaptation
        task.getadaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testGetadaptclassstrings_beforeVerbose() throws Exception {
        task.getadaptclassstrings();
        task.verbose();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testGetadaptclassstrings_afterVerbose() throws Exception {
        task.verbose();
        task.getadaptclassstrings();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testGetadaptclassstrings_betweenOtherCalls() throws Exception {
        task.verbose();
        task.getadaptclassstrings();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetadaptclassstrings_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.getadaptclassstrings();
        task.keepattributes("Signature,Exceptions");
        task.verbose();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testGetadaptclassstrings_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.getadaptclassstrings();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testGetadaptclassstrings_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.getadaptclassstrings();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testGetadaptclassstrings_doesNotAffectOtherFilters() throws Exception {
        task.keeppackagenames("com.example.*");
        task.keepattributes("Signature");

        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames filter should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes filter should be preserved");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testGetadaptclassstrings_forDynamicClassLoading() throws Exception {
        // When code dynamically loads classes by name
        task.getadaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class name strings for dynamic loading");
    }

    @Test
    public void testGetadaptclassstrings_forResourceFiles() throws Exception {
        // Adapt class names in resource files
        task.getadaptclassstrings();
        task.keepattributes("*Annotation*");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testGetadaptclassstrings_forSerializationSupport() throws Exception {
        // When using serialization with class names
        task.getadaptclassstrings();
        task.keepattributes("Signature");

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt class strings for serialization");
    }

    @Test
    public void testGetadaptclassstrings_forPluginArchitectures() throws Exception {
        // Plugin architectures that load classes by name
        task.getadaptclassstrings();
        task.keep("public class * implements com.example.Plugin { *; }");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testGetadaptclassstrings_forDependencyInjection() throws Exception {
        // DI frameworks that use class name strings
        task.getadaptclassstrings();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testGetadaptclassstrings_configurationNotNull() throws Exception {
        task.getadaptclassstrings();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetadaptclassstrings_returnValueConsistency() throws Exception {
        Object result1 = task.getadaptclassstrings();
        Object result2 = task.getadaptclassstrings();

        assertEquals(result1, result2, "Return value should be consistent (both null)");
    }

    @Test
    public void testGetadaptclassstrings_taskStateValid() throws Exception {
        task.getadaptclassstrings();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetadaptclassstrings_withMinimalConfiguration() throws Exception {
        // Test with only getadaptclassstrings called
        Object result = task.getadaptclassstrings();

        assertNull(result, "Should return null");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(), "adaptClassStrings should be empty");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetadaptclassstrings_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testGetadaptclassstrings_emptyListMeansAdaptAll() throws Exception {
        task.getadaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should not be null");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Empty list means adapt all class strings");
    }
}

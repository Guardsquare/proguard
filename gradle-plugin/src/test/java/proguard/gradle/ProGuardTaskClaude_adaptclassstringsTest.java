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
 * Comprehensive tests for ProGuardTask.adaptclassstrings() method.
 * This method initializes/clears the adaptClassStrings filter to adapt all class strings.
 */
public class ProGuardTaskClaude_adaptclassstringsTest {

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
    // Tests for adaptclassstrings() Method
    // ========================================

    @Test
    public void testAdaptclassstrings_initializesAdaptClassStrings() throws Exception {
        assertNull(task.configuration.adaptClassStrings, "adaptClassStrings should initially be null");

        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "adaptClassStrings should be empty (cleared to keep all)");
    }

    @Test
    public void testAdaptclassstrings_isVoid() throws Exception {
        // Verify method doesn't return a value (void method)
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_multipleCalls() throws Exception {
        task.adaptclassstrings();
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized after first call");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(), "adaptClassStrings should be empty after first call");

        task.adaptclassstrings();
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should remain initialized after second call");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(), "adaptClassStrings should remain empty after second call");

        task.adaptclassstrings();
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should remain initialized after third call");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(), "adaptClassStrings should remain empty after third call");
    }

    @Test
    public void testAdaptclassstrings_isIdempotent() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.adaptclassstrings();
            assertNotNull(task.configuration.adaptClassStrings,
                        "adaptClassStrings should be initialized on iteration " + i);
            assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                      "adaptClassStrings should be empty on iteration " + i);
        }
    }

    @Test
    public void testAdaptclassstrings_clearsExistingFilter() throws Exception {
        // Manually add some filters
        task.adaptclassstrings("com.example.*");
        assertFalse(task.configuration.adaptClassStrings.isEmpty(),
                   "adaptClassStrings should contain filters");

        task.adaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "adaptClassStrings should be cleared (to keep all)");
    }

    @Test
    public void testAdaptclassstrings_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.adaptclassstrings();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_doesNotReturnValue() throws Exception {
        // This is a void method - just verify it executes without issues
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_emptyListMeansAdaptAll() throws Exception {
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should not be null");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Empty list means adapt all class strings");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testAdaptclassstrings_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testAdaptclassstrings_withObfuscationEnabled() throws Exception {
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    @Test
    public void testAdaptclassstrings_withObfuscationDisabled() throws Exception {
        task.dontobfuscate();
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testAdaptclassstrings_withOptimizationEnabled() throws Exception {
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.optimize, "optimization should be enabled by default");
    }

    @Test
    public void testAdaptclassstrings_withOptimizationDisabled() throws Exception {
        task.dontoptimize();
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
    }

    @Test
    public void testAdaptclassstrings_withShrinkingDisabled() throws Exception {
        task.dontshrink();
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertFalse(task.configuration.shrink, "shrinking should be disabled");
    }

    @Test
    public void testAdaptclassstrings_withVerboseEnabled() throws Exception {
        task.verbose();
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testAdaptclassstrings_withKeepAttributes() throws Exception {
        task.keepattributes("Signature,Exceptions");
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
    }

    @Test
    public void testAdaptclassstrings_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.adaptclassstrings();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate, "obfuscate setting should not change");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testAdaptclassstrings_forAdaptingAllClassStrings() throws Exception {
        // Adapt all class strings in the code
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Empty filter means adapt all class strings");
    }

    @Test
    public void testAdaptclassstrings_forReflectionSupport() throws Exception {
        // When using reflection with Class.forName, adapt class strings
        task.adaptclassstrings();
        task.keepattributes("Signature");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertNotNull(task.configuration.keepAttributes, "Signature should be kept");
    }

    @Test
    public void testAdaptclassstrings_forStringLiterals() throws Exception {
        // Adapt class name strings in string literals
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testAdaptclassstrings_withObfuscation() throws Exception {
        // adaptclassstrings works with obfuscation to update string literals
        task.adaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testAdaptclassstrings_forClassForName() throws Exception {
        // When code uses Class.forName("com.example.MyClass")
        task.adaptclassstrings();
        task.keep("public class * { *; }");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_forConfigurationFiles() throws Exception {
        // Adapt class names in configuration files or resources
        task.adaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class name strings");
    }

    @Test
    public void testAdaptclassstrings_forDynamicClassLoading() throws Exception {
        // When code dynamically loads classes by name
        task.adaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class name strings for dynamic loading");
    }

    @Test
    public void testAdaptclassstrings_forResourceFiles() throws Exception {
        // Adapt class names in resource files
        task.adaptclassstrings();
        task.keepattributes("*Annotation*");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_forSerializationSupport() throws Exception {
        // When using serialization with class names
        task.adaptclassstrings();
        task.keepattributes("Signature");

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt class strings for serialization");
    }

    @Test
    public void testAdaptclassstrings_forPluginArchitectures() throws Exception {
        // Plugin architectures that load classes by name
        task.adaptclassstrings();
        task.keep("public class * implements com.example.Plugin { *; }");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_forDependencyInjection() throws Exception {
        // DI frameworks that use class name strings
        task.adaptclassstrings();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testAdaptclassstrings_calledFirst() throws Exception {
        // Test calling adaptclassstrings before any other configuration
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testAdaptclassstrings_calledLast() throws Exception {
        // Test calling adaptclassstrings after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.adaptclassstrings();
        task.keepattributes("Signature,Exceptions");
        task.verbose();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    @Test
    public void testAdaptclassstrings_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testAdaptclassstrings_androidBuild() throws Exception {
        // Android apps often use reflection with class name strings
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
    }

    @Test
    public void testAdaptclassstrings_androidWithReflection() throws Exception {
        // Android code using Class.forName()
        task.adaptclassstrings();
        task.keepattributes("Signature,Exceptions");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertNotNull(task.configuration.keepAttributes, "Attributes should be kept");
    }

    @Test
    public void testAdaptclassstrings_androidReleaseBuild() throws Exception {
        // Release build with class string adaptation
        task.adaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    @Test
    public void testAdaptclassstrings_androidDebugBuild() throws Exception {
        // Debug build might also need class string adaptation
        task.adaptclassstrings();
        task.dontobfuscate();
        task.dontoptimize();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
        assertFalse(task.configuration.optimize, "optimization should be disabled for debug");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testAdaptclassstrings_beforeVerbose() throws Exception {
        task.adaptclassstrings();
        task.verbose();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testAdaptclassstrings_afterVerbose() throws Exception {
        task.verbose();
        task.adaptclassstrings();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_betweenOtherCalls() throws Exception {
        task.verbose();
        task.adaptclassstrings();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testAdaptclassstrings_multipleTimesWithOtherCalls() throws Exception {
        task.adaptclassstrings();
        task.verbose();
        task.adaptclassstrings();
        task.dontoptimize();
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testAdaptclassstrings_withAllProcessingSteps() throws Exception {
        // Test with all processing steps configured
        task.adaptclassstrings();
        // Shrinking, optimization, obfuscation all enabled by default

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testAdaptclassstrings_debugVariant() throws Exception {
        // Debug variant with class string adaptation
        task.adaptclassstrings();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertFalse(task.configuration.shrink, "shrinking should be disabled");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testAdaptclassstrings_releaseVariant() throws Exception {
        // Typical release variant configuration
        task.adaptclassstrings();
        // All processing enabled by default

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testAdaptclassstrings_stagingVariant() throws Exception {
        // Staging variant: optimized but somewhat debuggable
        task.adaptclassstrings();
        task.dontobfuscate();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for staging");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testAdaptclassstrings_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.adaptclassstrings();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.adaptclassstrings();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_doesNotAffectOtherFilters() throws Exception {
        task.keeppackagenames("com.example.*");
        task.keepattributes("Signature");

        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames filter should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes filter should be preserved");
    }

    @Test
    public void testAdaptclassstrings_doesNotAffectOtherStringFields() throws Exception {
        task.configuration.flattenPackageHierarchy = "com/example";
        task.configuration.repackageClasses = "com/obf";

        task.adaptclassstrings();

        assertEquals("com/example", task.configuration.flattenPackageHierarchy,
                    "flattenPackageHierarchy should be preserved");
        assertEquals("com/obf", task.configuration.repackageClasses,
                    "repackageClasses should be preserved");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testAdaptclassstrings_forJavaReflectionAPI() throws Exception {
        // Java reflection with Class.forName()
        task.adaptclassstrings();
        task.keepattributes("Signature,Exceptions");

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings for reflection");
    }

    @Test
    public void testAdaptclassstrings_forSpringFramework() throws Exception {
        // Spring framework configuration with class names
        task.adaptclassstrings();
        task.keepattributes("*Annotation*,Signature");
        task.keep("@org.springframework.** class * { *; }");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_forJAXB() throws Exception {
        // JAXB uses class names in XML configuration
        task.adaptclassstrings();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings for JAXB");
    }

    @Test
    public void testAdaptclassstrings_forJSONSerialization() throws Exception {
        // JSON libraries that use class names
        task.adaptclassstrings();
        task.keepattributes("Signature");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
    }

    @Test
    public void testAdaptclassstrings_forServiceLoaders() throws Exception {
        // Java ServiceLoader mechanism
        task.adaptclassstrings();
        task.keep("class * implements java.lang.** { *; }");

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should adapt all class strings for service loaders");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testAdaptclassstrings_configurationNotNull() throws Exception {
        task.adaptclassstrings();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testAdaptclassstrings_taskStateValid() throws Exception {
        task.adaptclassstrings();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testAdaptclassstrings_withMinimalConfiguration() throws Exception {
        // Test with only adaptclassstrings called
        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.adaptClassStrings.isEmpty(), "adaptClassStrings should be empty");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testAdaptclassstrings_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.adaptclassstrings();

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testAdaptclassstrings_repeatedConfiguration() throws Exception {
        // Test setting and resetting configuration
        task.adaptclassstrings();
        assertTrue(task.configuration.adaptClassStrings.isEmpty(), "Should be empty after first call");

        task.adaptclassstrings("com.example.*");
        assertFalse(task.configuration.adaptClassStrings.isEmpty(), "Should have filters after adding");

        task.adaptclassstrings();
        assertTrue(task.configuration.adaptClassStrings.isEmpty(), "Should be empty again after second call");
    }

    // ========================================
    // Tests for adaptclassstrings(String) Method
    // ========================================

    @Test
    public void testAdaptclassstringsWithString_singlePackage() throws Exception {
        task.adaptclassstrings("com.example.MyClass");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertFalse(task.configuration.adaptClassStrings.isEmpty(), "adaptClassStrings should contain filter");
        assertTrue(task.configuration.adaptClassStrings.contains("com/example/MyClass"),
                  "Should contain internal form of class name");
    }

    @Test
    public void testAdaptclassstringsWithString_wildcard() throws Exception {
        task.adaptclassstrings("com.example.*");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.adaptClassStrings.contains("com/example/*"),
                  "Should contain wildcard pattern in internal form");
    }

    @Test
    public void testAdaptclassstringsWithString_multipleCallsAccumulate() throws Exception {
        task.adaptclassstrings("com.example.*");
        task.adaptclassstrings("org.test.*");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.adaptClassStrings.contains("com/example/*"),
                  "Should contain first filter");
        assertTrue(task.configuration.adaptClassStrings.contains("org/test/*"),
                  "Should contain second filter");
    }

    @Test
    public void testAdaptclassstringsWithString_commaSeparatedList() throws Exception {
        task.adaptclassstrings("com.example.*,org.test.*,net.demo.*");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.adaptClassStrings.contains("com/example/*"),
                  "Should contain first pattern");
        assertTrue(task.configuration.adaptClassStrings.contains("org/test/*"),
                  "Should contain second pattern");
        assertTrue(task.configuration.adaptClassStrings.contains("net/demo/*"),
                  "Should contain third pattern");
    }

    @Test
    public void testAdaptclassstringsWithString_convertsToInternalFormat() throws Exception {
        task.adaptclassstrings("com.example.MyClass");

        assertTrue(task.configuration.adaptClassStrings.contains("com/example/MyClass"),
                  "Should convert dots to slashes");
        assertFalse(task.configuration.adaptClassStrings.contains("com.example.MyClass"),
                   "Should not contain external format");
    }

    @Test
    public void testAdaptclassstringsWithString_nullClearsFilter() throws Exception {
        task.adaptclassstrings("com.example.*");
        assertFalse(task.configuration.adaptClassStrings.isEmpty(),
                   "Should have filters after adding");

        task.adaptclassstrings(null);

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Null should clear the filter (adapt all)");
    }

    @Test
    public void testAdaptclassstringsWithString_afterNoArgVersion() throws Exception {
        task.adaptclassstrings();
        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should be empty after no-arg call");

        task.adaptclassstrings("com.example.*");

        assertFalse(task.configuration.adaptClassStrings.isEmpty(),
                   "Should now contain filters");
        assertTrue(task.configuration.adaptClassStrings.contains("com/example/*"),
                  "Should contain the added filter");
    }

    @Test
    public void testAdaptclassstringsWithString_beforeNoArgVersion() throws Exception {
        task.adaptclassstrings("com.example.*");
        assertFalse(task.configuration.adaptClassStrings.isEmpty(),
                   "Should have filters after String call");

        task.adaptclassstrings();

        assertTrue(task.configuration.adaptClassStrings.isEmpty(),
                  "Should be cleared by no-arg call");
    }

    @Test
    public void testAdaptclassstringsWithString_doubleWildcard() throws Exception {
        task.adaptclassstrings("com.example.**");

        assertTrue(task.configuration.adaptClassStrings.contains("com/example/**"),
                  "Should support double wildcard pattern");
    }

    @Test
    public void testAdaptclassstringsWithString_rootPackage() throws Exception {
        task.adaptclassstrings("*");

        assertTrue(task.configuration.adaptClassStrings.contains("*"),
                  "Should support root wildcard");
    }

    // ========================================
    // Integration Tests with String Parameter
    // ========================================

    @Test
    public void testAdaptclassstringsWithString_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.adaptclassstrings("com.example.*");

        assertTrue(task.configuration.adaptClassStrings.contains("com/example/*"),
                  "adaptClassStrings should contain filter");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testAdaptclassstringsWithString_withObfuscation() throws Exception {
        task.adaptclassstrings("com.example.*");

        assertTrue(task.configuration.adaptClassStrings.contains("com/example/*"),
                  "adaptClassStrings should contain filter");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testAdaptclassstringsWithString_withVerbose() throws Exception {
        task.verbose();
        task.adaptclassstrings("org.test.*");

        assertTrue(task.configuration.adaptClassStrings.contains("org/test/*"),
                  "adaptClassStrings should contain filter");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    // ========================================
    // Realistic Scenarios with String Parameter
    // ========================================

    @Test
    public void testAdaptclassstringsWithString_forSpecificPackages() throws Exception {
        // Adapt class strings only for specific packages
        task.adaptclassstrings("com.myapp.model.*,com.myapp.service.*");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/model/*"),
                  "Should adapt model package");
        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/service/*"),
                  "Should adapt service package");
    }

    @Test
    public void testAdaptclassstringsWithString_forReflectionClasses() throws Exception {
        // Adapt only classes that are loaded via reflection
        task.adaptclassstrings("com.example.plugins.*");
        task.keep("class com.example.plugins.** { *; }");

        assertTrue(task.configuration.adaptClassStrings.contains("com/example/plugins/*"),
                  "Should adapt plugin classes");
    }

    @Test
    public void testAdaptclassstringsWithString_forConfigurationClasses() throws Exception {
        // Adapt classes referenced in configuration files
        task.adaptclassstrings("com.myapp.config.*");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/config/*"),
                  "Should adapt configuration classes");
    }

    @Test
    public void testAdaptclassstringsWithString_forDynamicProxy() throws Exception {
        // Adapt class names for dynamic proxy creation
        task.adaptclassstrings("com.myapp.proxies.*");
        task.keepattributes("Signature");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/proxies/*"),
                  "Should adapt proxy classes");
    }

    @Test
    public void testAdaptclassstringsWithString_forSerializationClasses() throws Exception {
        // Adapt class names used in serialization
        task.adaptclassstrings("com.myapp.dto.*");
        task.keepattributes("Signature");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/dto/*"),
                  "Should adapt DTO classes");
    }

    @Test
    public void testAdaptclassstringsWithString_excludingSystemClasses() throws Exception {
        // Adapt application classes but not system classes
        task.adaptclassstrings("com.myapp.**");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/**"),
                  "Should adapt only application classes");
    }

    // ========================================
    // Edge Cases with String Parameter
    // ========================================

    @Test
    public void testAdaptclassstringsWithString_emptyString() throws Exception {
        task.adaptclassstrings("");

        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should be initialized");
        assertTrue(task.configuration.adaptClassStrings.contains(""),
                  "Should contain empty string");
    }

    @Test
    public void testAdaptclassstringsWithString_duplicateFilters() throws Exception {
        task.adaptclassstrings("com.example.*");
        task.adaptclassstrings("com.example.*");

        // Filter list may contain duplicates or not - just verify it contains the pattern
        assertTrue(task.configuration.adaptClassStrings.contains("com/example/*"),
                  "Should contain the filter");
    }

    @Test
    public void testAdaptclassstringsWithString_veryLongPackageName() throws Exception {
        String longPackage = "com.example.very.long.package.name.that.goes.on.and.on.*";
        task.adaptclassstrings(longPackage);

        assertTrue(task.configuration.adaptClassStrings.contains("com/example/very/long/package/name/that/goes/on/and/on/*"),
                  "Should handle long package names");
    }

    @Test
    public void testAdaptclassstringsWithString_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.keep("public class * { *; }");
        task.adaptclassstrings("com.example.*,org.test.*");
        task.keepattributes("Signature");
        task.verbose();

        assertTrue(task.configuration.adaptClassStrings.contains("com/example/*"),
                  "Should contain first filter");
        assertTrue(task.configuration.adaptClassStrings.contains("org/test/*"),
                  "Should contain second filter");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    // ========================================
    // Android-Specific Scenarios with String Parameter
    // ========================================

    @Test
    public void testAdaptclassstringsWithString_androidAppClasses() throws Exception {
        // Android app-specific classes that use reflection
        task.adaptclassstrings("com.myapp.**");
        task.keep("class com.myapp.** { *; }");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/**"),
                  "Should adapt app classes");
    }

    @Test
    public void testAdaptclassstringsWithString_androidFragments() throws Exception {
        // Android Fragments loaded by name
        task.adaptclassstrings("com.myapp.fragments.*");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/fragments/*"),
                  "Should adapt fragment classes");
    }

    @Test
    public void testAdaptclassstringsWithString_androidServices() throws Exception {
        // Android Services referenced in manifest
        task.adaptclassstrings("com.myapp.services.*");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/services/*"),
                  "Should adapt service classes");
    }

    // ========================================
    // Configuration State Tests with String Parameter
    // ========================================

    @Test
    public void testAdaptclassstringsWithString_doesNotAffectOtherSettings() throws Exception {
        task.verbose();
        task.dontoptimize();
        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.adaptclassstrings("com.example.*");

        assertEquals(verboseBefore, task.configuration.verbose, "verbose should not change");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize should not change");
        assertTrue(task.configuration.adaptClassStrings.contains("com/example/*"),
                  "adaptClassStrings should contain filter");
    }

    @Test
    public void testAdaptclassstringsWithString_doesNotAffectOtherFilters() throws Exception {
        task.keeppackagenames("com.example.*");
        task.keepattributes("Signature");

        task.adaptclassstrings("org.test.*");

        assertTrue(task.configuration.adaptClassStrings.contains("org/test/*"),
                  "adaptClassStrings should contain filter");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames filter should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes filter should be preserved");
    }

    // ========================================
    // Special Use Cases with String Parameter
    // ========================================

    @Test
    public void testAdaptclassstringsWithString_forClassForNameCalls() throws Exception {
        // Adapt specific classes used with Class.forName()
        task.adaptclassstrings("com.myapp.Driver,com.myapp.Handler");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/Driver"),
                  "Should adapt Driver class");
        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/Handler"),
                  "Should adapt Handler class");
    }

    @Test
    public void testAdaptclassstringsWithString_forSpringBeans() throws Exception {
        // Spring beans referenced by class name
        task.adaptclassstrings("com.myapp.beans.*");
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/beans/*"),
                  "Should adapt Spring bean classes");
    }

    @Test
    public void testAdaptclassstringsWithString_forJAXBClasses() throws Exception {
        // JAXB classes referenced in XML
        task.adaptclassstrings("com.myapp.xml.*");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/xml/*"),
                  "Should adapt JAXB classes");
    }

    @Test
    public void testAdaptclassstringsWithString_forJSONMappings() throws Exception {
        // Classes used in JSON serialization/deserialization
        task.adaptclassstrings("com.myapp.api.models.*");
        task.keepattributes("Signature");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/api/models/*"),
                  "Should adapt JSON model classes");
    }

    @Test
    public void testAdaptclassstringsWithString_forPluginSystem() throws Exception {
        // Plugin classes loaded by name
        task.adaptclassstrings("com.myapp.plugins.**");
        task.keep("interface com.myapp.Plugin { *; }");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/plugins/**"),
                  "Should adapt plugin classes");
    }

    // ========================================
    // Common Patterns
    // ========================================

    @Test
    public void testAdaptclassstringsWithString_allApplicationClasses() throws Exception {
        // Adapt all application classes
        task.adaptclassstrings("com.mycompany.**");

        assertTrue(task.configuration.adaptClassStrings.contains("com/mycompany/**"),
                  "Should adapt all company classes");
    }

    @Test
    public void testAdaptclassstringsWithString_multipleTopLevelPackages() throws Exception {
        // Adapt multiple top-level packages
        task.adaptclassstrings("com.myapp.**,org.mylib.**");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/**"),
                  "Should adapt first package hierarchy");
        assertTrue(task.configuration.adaptClassStrings.contains("org/mylib/**"),
                  "Should adapt second package hierarchy");
    }

    @Test
    public void testAdaptclassstringsWithString_specificClasses() throws Exception {
        // Adapt only specific classes without wildcards
        task.adaptclassstrings("com.myapp.Main,com.myapp.Config");

        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/Main"),
                  "Should adapt Main class");
        assertTrue(task.configuration.adaptClassStrings.contains("com/myapp/Config"),
                  "Should adapt Config class");
    }

    // ========================================
    // Verification Tests with String Parameter
    // ========================================

    @Test
    public void testAdaptclassstringsWithString_configurationNotNull() throws Exception {
        task.adaptclassstrings("com.example.*");

        assertNotNull(task.configuration, "Configuration should never be null");
        assertNotNull(task.configuration.adaptClassStrings, "adaptClassStrings should not be null");
    }

    @Test
    public void testAdaptclassstringsWithString_taskStateValid() throws Exception {
        task.adaptclassstrings("com.test.*");

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testAdaptclassstringsWithString_filtersAccumulate() throws Exception {
        task.adaptclassstrings("com.example.*");
        int sizeBefore = task.configuration.adaptClassStrings.size();

        task.adaptclassstrings("org.test.*");
        int sizeAfter = task.configuration.adaptClassStrings.size();

        assertTrue(sizeAfter > sizeBefore, "Filters should accumulate");
        assertTrue(task.configuration.adaptClassStrings.contains("com/example/*"),
                  "Should still contain first filter");
        assertTrue(task.configuration.adaptClassStrings.contains("org/test/*"),
                  "Should contain second filter");
    }
}

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
 * Comprehensive tests for ProGuardTask.renamesourcefileattribute() method.
 * This method sets the newSourceFileAttribute to an empty string.
 */
public class ProGuardTaskClaude_renamesourcefileattributeTest {

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
    // Tests for renamesourcefileattribute() Method
    // ========================================

    @Test
    public void testRenamesourcefileattribute_setsNewSourceFileAttributeToEmptyString() throws Exception {
        assertNull(task.configuration.newSourceFileAttribute, "newSourceFileAttribute should initially be null");

        task.renamesourcefileattribute();

        assertNotNull(task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be set to empty string");
    }

    @Test
    public void testRenamesourcefileattribute_isVoid() throws Exception {
        // Verify method doesn't return a value (void method)
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testRenamesourcefileattribute_multipleCalls() throws Exception {
        task.renamesourcefileattribute();
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be empty string after first call");

        task.renamesourcefileattribute();
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should remain empty string after second call");

        task.renamesourcefileattribute();
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should remain empty string after third call");
    }

    @Test
    public void testRenamesourcefileattribute_isIdempotent() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.renamesourcefileattribute();
            assertEquals("", task.configuration.newSourceFileAttribute,
                        "newSourceFileAttribute should remain empty string on iteration " + i);
        }
    }

    @Test
    public void testRenamesourcefileattribute_overwritesPreviousValue() throws Exception {
        task.configuration.newSourceFileAttribute = "CustomSource";
        assertEquals("CustomSource", task.configuration.newSourceFileAttribute,
                    "Should be CustomSource initially");

        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should be overwritten to empty string");
    }

    @Test
    public void testRenamesourcefileattribute_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.renamesourcefileattribute();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testRenamesourcefileattribute_doesNotReturnValue() throws Exception {
        // This is a void method - just verify it executes without issues
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testRenamesourcefileattribute_emptyStringIsNotNull() throws Exception {
        task.renamesourcefileattribute();

        assertNotNull(task.configuration.newSourceFileAttribute,
                     "newSourceFileAttribute should not be null");
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be empty string");
        assertTrue(task.configuration.newSourceFileAttribute.isEmpty(),
                  "newSourceFileAttribute should be empty");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testRenamesourcefileattribute_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testRenamesourcefileattribute_withKeepAttributes() throws Exception {
        task.keepattributes("SourceFile,LineNumberTable");
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be preserved");
    }

    @Test
    public void testRenamesourcefileattribute_withObfuscationEnabled() throws Exception {
        // renamesourcefileattribute works with obfuscation
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    @Test
    public void testRenamesourcefileattribute_withObfuscationDisabled() throws Exception {
        task.dontobfuscate();
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testRenamesourcefileattribute_withOptimizationEnabled() throws Exception {
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.optimize, "optimization should be enabled by default");
    }

    @Test
    public void testRenamesourcefileattribute_withOptimizationDisabled() throws Exception {
        task.dontoptimize();
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
    }

    @Test
    public void testRenamesourcefileattribute_withShrinkingDisabled() throws Exception {
        task.dontshrink();
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertFalse(task.configuration.shrink, "shrinking should be disabled");
    }

    @Test
    public void testRenamesourcefileattribute_withVerboseEnabled() throws Exception {
        task.verbose();
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testRenamesourcefileattribute_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.renamesourcefileattribute();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate, "obfuscate setting should not change");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testRenamesourcefileattribute_forObfuscatingStackTraces() throws Exception {
        // Empty string removes the SourceFile attribute, obfuscating stack traces
        task.renamesourcefileattribute();
        task.keepattributes("LineNumberTable");

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should set to empty string to remove source file info");
        assertNotNull(task.configuration.keepAttributes, "Should keep LineNumberTable");
    }

    @Test
    public void testRenamesourcefileattribute_forProductionBuild() throws Exception {
        // Production builds might want to hide source file names
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "Should hide source file names");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testRenamesourcefileattribute_withDebuggingDisabled() throws Exception {
        // When not debugging, source file attribute can be removed
        task.renamesourcefileattribute();
        task.dontoptimize();

        assertEquals("", task.configuration.newSourceFileAttribute, "Source file should be removed");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
    }

    @Test
    public void testRenamesourcefileattribute_forSecurityHardening() throws Exception {
        // Security-conscious builds remove source file information
        task.renamesourcefileattribute();
        task.keepattributes("Exceptions");

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should remove source file info for security");
        assertNotNull(task.configuration.keepAttributes, "Exceptions should be kept");
    }

    @Test
    public void testRenamesourcefileattribute_forCodeObfuscation() throws Exception {
        // Obfuscate source file names as part of overall obfuscation strategy
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "Source file should be obfuscated");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testRenamesourcefileattribute_forReverseEngineeringProtection() throws Exception {
        // Make reverse engineering harder by removing source file names
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should remove source file info");
    }

    @Test
    public void testRenamesourcefileattribute_forMinimalStackTraces() throws Exception {
        // Minimal stack traces without file names but with line numbers
        task.renamesourcefileattribute();
        task.keepattributes("LineNumberTable");

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be removed");
        assertNotNull(task.configuration.keepAttributes, "LineNumberTable should be kept");
    }

    @Test
    public void testRenamesourcefileattribute_forIntellectualPropertyProtection() throws Exception {
        // Protect intellectual property by removing source file information
        task.renamesourcefileattribute();
        task.keepattributes("Exceptions");

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be removed for IP protection");
    }

    @Test
    public void testRenamesourcefileattribute_forHidingInternalStructure() throws Exception {
        // Hide internal project structure by removing source file attribute
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be removed to hide structure");
    }

    @Test
    public void testRenamesourcefileattribute_forSecurityThroughObscurity() throws Exception {
        // Security through obscurity: remove identifying information
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be removed for security");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testRenamesourcefileattribute_calledFirst() throws Exception {
        // Test calling renamesourcefileattribute before any other configuration
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testRenamesourcefileattribute_calledLast() throws Exception {
        // Test calling renamesourcefileattribute after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testRenamesourcefileattribute_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.renamesourcefileattribute();
        task.keepattributes("Exceptions");
        task.verbose();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    @Test
    public void testRenamesourcefileattribute_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testRenamesourcefileattribute_androidReleaseBuild() throws Exception {
        // Release builds typically obfuscate source file names
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be obfuscated for release");
        assertTrue(task.configuration.optimize, "optimization should be enabled for release");
    }

    @Test
    public void testRenamesourcefileattribute_androidWithCrashReporting() throws Exception {
        // Even with crash reporting, source file can be removed if line numbers are kept
        task.renamesourcefileattribute();
        task.keepattributes("LineNumberTable");

        assertEquals("", task.configuration.newSourceFileAttribute, "Source file should be removed");
        assertNotNull(task.configuration.keepAttributes, "LineNumberTable should be kept");
    }

    @Test
    public void testRenamesourcefileattribute_androidProduction() throws Exception {
        // Production Android builds with enhanced security
        task.renamesourcefileattribute();
        task.keepattributes("Exceptions");

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be removed for production");
        assertNotNull(task.configuration.keepAttributes, "Exceptions should be kept");
    }

    @Test
    public void testRenamesourcefileattribute_androidDebugBuildWithoutSourceFile() throws Exception {
        // Some debug builds might still want to remove source file info
        task.renamesourcefileattribute();
        task.dontobfuscate();
        task.dontoptimize();

        assertEquals("", task.configuration.newSourceFileAttribute, "Source file should be removed");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testRenamesourcefileattribute_beforeVerbose() throws Exception {
        task.renamesourcefileattribute();
        task.verbose();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testRenamesourcefileattribute_afterVerbose() throws Exception {
        task.verbose();
        task.renamesourcefileattribute();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testRenamesourcefileattribute_betweenOtherCalls() throws Exception {
        task.verbose();
        task.renamesourcefileattribute();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testRenamesourcefileattribute_multipleTimesWithOtherCalls() throws Exception {
        task.renamesourcefileattribute();
        task.verbose();
        task.renamesourcefileattribute();
        task.dontoptimize();
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testRenamesourcefileattribute_withAllProcessingSteps() throws Exception {
        // Test with all processing steps configured
        task.renamesourcefileattribute();
        // Shrinking, optimization, obfuscation all enabled by default

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testRenamesourcefileattribute_debugVariant() throws Exception {
        // Debug variant might still remove source file for some reason
        task.renamesourcefileattribute();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertFalse(task.configuration.shrink, "shrinking should be disabled");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testRenamesourcefileattribute_releaseVariant() throws Exception {
        // Typical release variant configuration
        task.renamesourcefileattribute();
        // All processing enabled by default

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testRenamesourcefileattribute_stagingVariant() throws Exception {
        // Staging variant: optimized but somewhat debuggable
        task.renamesourcefileattribute();
        task.keepattributes("LineNumberTable");
        task.dontobfuscate();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for staging");
        assertNotNull(task.configuration.keepAttributes, "LineNumberTable should be kept");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testRenamesourcefileattribute_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.renamesourcefileattribute();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testRenamesourcefileattribute_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.renamesourcefileattribute();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testRenamesourcefileattribute_doesNotAffectOtherStringFields() throws Exception {
        task.configuration.flattenPackageHierarchy = "com/example";
        task.configuration.repackageClasses = "com/obf";

        task.renamesourcefileattribute();

        assertEquals("com/example", task.configuration.flattenPackageHierarchy,
                    "flattenPackageHierarchy should be preserved");
        assertEquals("com/obf", task.configuration.repackageClasses,
                    "repackageClasses should be preserved");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testRenamesourcefileattribute_doesNotAffectFilterLists() throws Exception {
        task.keeppackagenames("com.example.*");
        task.keepattributes("Signature");

        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames filter should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes filter should be preserved");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testRenamesourcefileattribute_withoutKeepingSourceFile() throws Exception {
        // Remove SourceFile attribute entirely
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be removed");
    }

    @Test
    public void testRenamesourcefileattribute_withLineNumbersOnly() throws Exception {
        // Keep line numbers but not source file names
        task.renamesourcefileattribute();
        task.keepattributes("LineNumberTable");

        assertEquals("", task.configuration.newSourceFileAttribute, "Source file should be removed");
        assertNotNull(task.configuration.keepAttributes, "LineNumberTable should be kept");
    }

    @Test
    public void testRenamesourcefileattribute_forLibraryObfuscation() throws Exception {
        // Libraries might want to hide source file names
        task.renamesourcefileattribute();
        task.dontobfuscate();

        assertEquals("", task.configuration.newSourceFileAttribute, "Source file should be removed");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testRenamesourcefileattribute_configurationNotNull() throws Exception {
        task.renamesourcefileattribute();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testRenamesourcefileattribute_taskStateValid() throws Exception {
        task.renamesourcefileattribute();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testRenamesourcefileattribute_withMinimalConfiguration() throws Exception {
        // Test with only renamesourcefileattribute called
        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testRenamesourcefileattribute_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.renamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testRenamesourcefileattribute_repeatedConfiguration() throws Exception {
        // Test setting and resetting configuration
        task.renamesourcefileattribute();
        assertEquals("", task.configuration.newSourceFileAttribute, "Should be empty after first call");

        task.configuration.newSourceFileAttribute = "CustomSource";
        assertEquals("CustomSource", task.configuration.newSourceFileAttribute, "Should be CustomSource after manual set");

        task.renamesourcefileattribute();
        assertEquals("", task.configuration.newSourceFileAttribute, "Should be empty again after second call");
    }

    // ========================================
    // Tests for renamesourcefileattribute(String) Method
    // ========================================

    @Test
    public void testRenamesourcefileattributeWithString_singleWord() throws Exception {
        task.renamesourcefileattribute("SourceFile");

        assertNotNull(task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertEquals("SourceFile", task.configuration.newSourceFileAttribute,
                    "Should contain the specified attribute name");
    }

    @Test
    public void testRenamesourcefileattributeWithString_customName() throws Exception {
        task.renamesourcefileattribute("MyCustomSource");

        assertNotNull(task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertEquals("MyCustomSource", task.configuration.newSourceFileAttribute,
                    "Should set to custom name");
    }

    @Test
    public void testRenamesourcefileattributeWithString_emptyString() throws Exception {
        task.renamesourcefileattribute("");

        assertNotNull(task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should be set to empty string");
    }

    @Test
    public void testRenamesourcefileattributeWithString_replacesExistingValue() throws Exception {
        task.renamesourcefileattribute("FirstValue");
        assertEquals("FirstValue", task.configuration.newSourceFileAttribute,
                    "Should be FirstValue initially");

        task.renamesourcefileattribute("SecondValue");
        assertEquals("SecondValue", task.configuration.newSourceFileAttribute,
                    "Should be replaced with SecondValue");
    }

    @Test
    public void testRenamesourcefileattributeWithString_multipleCallsOverwrite() throws Exception {
        task.renamesourcefileattribute("Value1");
        task.renamesourcefileattribute("Value2");
        task.renamesourcefileattribute("Value3");

        assertEquals("Value3", task.configuration.newSourceFileAttribute,
                    "Should contain only the last value");
    }

    @Test
    public void testRenamesourcefileattributeWithString_isIdempotent() throws Exception {
        task.renamesourcefileattribute("MySource");
        assertEquals("MySource", task.configuration.newSourceFileAttribute,
                    "Should be MySource after first call");

        task.renamesourcefileattribute("MySource");
        assertEquals("MySource", task.configuration.newSourceFileAttribute,
                    "Should remain MySource after second call");
    }

    @Test
    public void testRenamesourcefileattributeWithString_afterNoArgVersion() throws Exception {
        task.renamesourcefileattribute();
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should be empty string after no-arg call");

        task.renamesourcefileattribute("CustomSource");
        assertEquals("CustomSource", task.configuration.newSourceFileAttribute,
                    "Should be replaced with CustomSource");
    }

    @Test
    public void testRenamesourcefileattributeWithString_beforeNoArgVersion() throws Exception {
        task.renamesourcefileattribute("CustomSource");
        assertEquals("CustomSource", task.configuration.newSourceFileAttribute,
                    "Should be CustomSource after String call");

        task.renamesourcefileattribute();
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should be replaced with empty string");
    }

    @Test
    public void testRenamesourcefileattributeWithString_specialCharacters() throws Exception {
        task.renamesourcefileattribute("Source_File");

        assertEquals("Source_File", task.configuration.newSourceFileAttribute,
                    "Should handle underscore in name");
    }

    @Test
    public void testRenamesourcefileattributeWithString_numbersInName() throws Exception {
        task.renamesourcefileattribute("Source123");

        assertEquals("Source123", task.configuration.newSourceFileAttribute,
                    "Should handle numbers in name");
    }

    // ========================================
    // Integration Tests with String Parameter
    // ========================================

    @Test
    public void testRenamesourcefileattributeWithString_withKeepAttributes() throws Exception {
        task.keepattributes("SourceFile");
        task.renamesourcefileattribute("NewSourceName");

        assertEquals("NewSourceName", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be preserved");
    }

    @Test
    public void testRenamesourcefileattributeWithString_withObfuscation() throws Exception {
        task.renamesourcefileattribute("ObfuscatedSource");

        assertEquals("ObfuscatedSource", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be set");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testRenamesourcefileattributeWithString_withVerbose() throws Exception {
        task.verbose();
        task.renamesourcefileattribute("VerboseSource");

        assertEquals("VerboseSource", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    // ========================================
    // Realistic Scenarios with String Parameter
    // ========================================

    @Test
    public void testRenamesourcefileattributeWithString_forGenericSourceFile() throws Exception {
        // Use a generic name to hide actual source file names
        task.renamesourcefileattribute("SourceFile");
        task.keepattributes("SourceFile,LineNumberTable");

        assertEquals("SourceFile", task.configuration.newSourceFileAttribute,
                    "Should set generic source file name");
        assertNotNull(task.configuration.keepAttributes, "Should keep SourceFile and LineNumberTable");
    }

    @Test
    public void testRenamesourcefileattributeWithString_forDeceptiveNaming() throws Exception {
        // Use a misleading name to confuse reverse engineers
        task.renamesourcefileattribute("NotTheRealSource");

        assertEquals("NotTheRealSource", task.configuration.newSourceFileAttribute,
                    "Should set deceptive name");
    }

    @Test
    public void testRenamesourcefileattributeWithString_forConsistentNaming() throws Exception {
        // Set all source files to the same name
        task.renamesourcefileattribute("X");
        task.keepattributes("SourceFile,LineNumberTable");

        assertEquals("X", task.configuration.newSourceFileAttribute,
                    "Should set all source files to single character");
    }

    @Test
    public void testRenamesourcefileattributeWithString_forProductionBuild() throws Exception {
        // Production build with renamed source file
        task.renamesourcefileattribute("SourceFile");

        assertEquals("SourceFile", task.configuration.newSourceFileAttribute,
                    "Should use standard name");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testRenamesourcefileattributeWithString_forMinimalInformation() throws Exception {
        // Provide minimal information in stack traces
        task.renamesourcefileattribute("S");
        task.keepattributes("SourceFile,LineNumberTable");

        assertEquals("S", task.configuration.newSourceFileAttribute,
                    "Should use minimal single character name");
    }

    @Test
    public void testRenamesourcefileattributeWithString_forSecurityHardening() throws Exception {
        // Use generic name for security
        task.renamesourcefileattribute("Source");
        task.keepattributes("SourceFile");

        assertEquals("Source", task.configuration.newSourceFileAttribute,
                    "Should use generic source name");
    }

    // ========================================
    // Edge Cases with String Parameter
    // ========================================

    @Test
    public void testRenamesourcefileattributeWithString_veryLongName() throws Exception {
        String longName = "VeryLongSourceFileAttributeNameThatMightCauseIssues";
        task.renamesourcefileattribute(longName);

        assertEquals(longName, task.configuration.newSourceFileAttribute,
                    "Should handle long names");
    }

    @Test
    public void testRenamesourcefileattributeWithString_singleCharacter() throws Exception {
        task.renamesourcefileattribute("X");

        assertEquals("X", task.configuration.newSourceFileAttribute,
                    "Should handle single character names");
    }

    @Test
    public void testRenamesourcefileattributeWithString_afterManualSet() throws Exception {
        task.configuration.newSourceFileAttribute = "Manual";

        task.renamesourcefileattribute("Method");

        assertEquals("Method", task.configuration.newSourceFileAttribute,
                    "Should overwrite manually set value");
    }

    @Test
    public void testRenamesourcefileattributeWithString_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.keep("public class * { *; }");
        task.renamesourcefileattribute("CustomSourceFile");
        task.keepattributes("SourceFile,LineNumberTable");
        task.verbose();

        assertEquals("CustomSourceFile", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    // ========================================
    // Android-Specific Scenarios with String Parameter
    // ========================================

    @Test
    public void testRenamesourcefileattributeWithString_androidReleaseBuild() throws Exception {
        // Android release with renamed source file
        task.renamesourcefileattribute("SourceFile");
        task.keepattributes("SourceFile,LineNumberTable");

        assertEquals("SourceFile", task.configuration.newSourceFileAttribute,
                    "Should use standard SourceFile name");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
    }

    @Test
    public void testRenamesourcefileattributeWithString_androidWithCrashReporting() throws Exception {
        // Keep source file attribute but rename it for consistency
        task.renamesourcefileattribute("SourceFile");
        task.keepattributes("SourceFile,LineNumberTable");

        assertEquals("SourceFile", task.configuration.newSourceFileAttribute,
                    "Should rename to generic SourceFile");
        assertNotNull(task.configuration.keepAttributes, "Should keep debug attributes");
    }

    @Test
    public void testRenamesourcefileattributeWithString_androidProduction() throws Exception {
        // Production Android with minimal source info
        task.renamesourcefileattribute("S");
        task.keepattributes("SourceFile,LineNumberTable");

        assertEquals("S", task.configuration.newSourceFileAttribute,
                    "Should use minimal source name");
    }

    // ========================================
    // Configuration State Tests with String Parameter
    // ========================================

    @Test
    public void testRenamesourcefileattributeWithString_doesNotAffectOtherSettings() throws Exception {
        task.verbose();
        task.dontoptimize();
        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.renamesourcefileattribute("MySource");

        assertEquals(verboseBefore, task.configuration.verbose, "verbose should not change");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize should not change");
        assertEquals("MySource", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be set");
    }

    @Test
    public void testRenamesourcefileattributeWithString_doesNotAffectOtherStringFields() throws Exception {
        task.configuration.flattenPackageHierarchy = "com/example";
        task.configuration.repackageClasses = "com/obf";

        task.renamesourcefileattribute("NewSource");

        assertEquals("com/example", task.configuration.flattenPackageHierarchy,
                    "flattenPackageHierarchy should be preserved");
        assertEquals("com/obf", task.configuration.repackageClasses,
                    "repackageClasses should be preserved");
        assertEquals("NewSource", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be set");
    }

    // ========================================
    // Special Use Cases with String Parameter
    // ========================================

    @Test
    public void testRenamesourcefileattributeWithString_forUniformStackTraces() throws Exception {
        // All classes will show the same source file name in stack traces
        task.renamesourcefileattribute("SourceFile");
        task.keepattributes("SourceFile,LineNumberTable");

        assertEquals("SourceFile", task.configuration.newSourceFileAttribute,
                    "All source files should be renamed to SourceFile");
    }

    @Test
    public void testRenamesourcefileattributeWithString_forObfuscationConsistency() throws Exception {
        // Use consistent naming across all source files
        task.renamesourcefileattribute("Source");

        assertEquals("Source", task.configuration.newSourceFileAttribute,
                    "Should use consistent source name");
    }

    @Test
    public void testRenamesourcefileattributeWithString_forReverseEngineeringProtection() throws Exception {
        // Make reverse engineering harder with misleading names
        task.renamesourcefileattribute("java");

        assertEquals("java", task.configuration.newSourceFileAttribute,
                    "Should use misleading name");
    }

    // ========================================
    // Common Source File Names
    // ========================================

    @Test
    public void testRenamesourcefileattributeWithString_standardSourceFile() throws Exception {
        // Most common: rename all to "SourceFile"
        task.renamesourcefileattribute("SourceFile");

        assertEquals("SourceFile", task.configuration.newSourceFileAttribute,
                    "Should use standard SourceFile name");
    }

    @Test
    public void testRenamesourcefileattributeWithString_unknownSource() throws Exception {
        task.renamesourcefileattribute("Unknown");

        assertEquals("Unknown", task.configuration.newSourceFileAttribute,
                    "Should set to Unknown");
    }

    @Test
    public void testRenamesourcefileattributeWithString_proguard() throws Exception {
        task.renamesourcefileattribute("ProGuard");

        assertEquals("ProGuard", task.configuration.newSourceFileAttribute,
                    "Should set to ProGuard");
    }

    // ========================================
    // Verification Tests with String Parameter
    // ========================================

    @Test
    public void testRenamesourcefileattributeWithString_configurationNotNull() throws Exception {
        task.renamesourcefileattribute("Test");

        assertNotNull(task.configuration, "Configuration should never be null");
        assertNotNull(task.configuration.newSourceFileAttribute, "newSourceFileAttribute should not be null");
    }

    @Test
    public void testRenamesourcefileattributeWithString_taskStateValid() throws Exception {
        task.renamesourcefileattribute("ValidSource");

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testRenamesourcefileattributeWithString_preservesValue() throws Exception {
        String testValue = "TestSourceFile";
        task.renamesourcefileattribute(testValue);

        assertEquals(testValue, task.configuration.newSourceFileAttribute,
                    "Should preserve exact value provided");
    }
}

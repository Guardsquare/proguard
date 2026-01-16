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
 * Comprehensive tests for ProGuardTask.getrenamesourcefileattribute() method.
 * This is a Groovy DSL getter method that returns null and calls renamesourcefileattribute().
 */
public class ProGuardTaskClaude_getrenamesourcefileattributeTest {

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
    // Tests for getrenamesourcefileattribute() Method
    // ========================================

    @Test
    public void testGetrenamesourcefileattribute_returnsNull() throws Exception {
        Object result = task.getrenamesourcefileattribute();

        assertNull(result, "getrenamesourcefileattribute should return null for Groovy DSL support");
    }

    @Test
    public void testGetrenamesourcefileattribute_setsNewSourceFileAttributeToEmptyString() throws Exception {
        assertNull(task.configuration.newSourceFileAttribute, "newSourceFileAttribute should initially be null");

        task.getrenamesourcefileattribute();

        assertNotNull(task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be set to empty string");
    }

    @Test
    public void testGetrenamesourcefileattribute_multipleCalls() throws Exception {
        task.getrenamesourcefileattribute();
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be empty string after first call");

        task.getrenamesourcefileattribute();
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should remain empty string after second call");

        task.getrenamesourcefileattribute();
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should remain empty string after third call");
    }

    @Test
    public void testGetrenamesourcefileattribute_isIdempotent() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.getrenamesourcefileattribute();
            assertEquals("", task.configuration.newSourceFileAttribute,
                        "newSourceFileAttribute should remain empty string on iteration " + i);
        }
    }

    @Test
    public void testGetrenamesourcefileattribute_alwaysReturnsNull() throws Exception {
        Object result1 = task.getrenamesourcefileattribute();
        Object result2 = task.getrenamesourcefileattribute();
        Object result3 = task.getrenamesourcefileattribute();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetrenamesourcefileattribute_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getrenamesourcefileattribute();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testGetrenamesourcefileattribute_overwritesPreviousValue() throws Exception {
        task.configuration.newSourceFileAttribute = "CustomSource";
        assertEquals("CustomSource", task.configuration.newSourceFileAttribute,
                    "Should be CustomSource initially");

        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should be overwritten to empty string");
    }

    @Test
    public void testGetrenamesourcefileattribute_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testGetrenamesourcefileattribute_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getrenamesourcefileattribute();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testGetrenamesourcefileattribute_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testGetrenamesourcefileattribute_withKeepAttributes() throws Exception {
        task.keepattributes("SourceFile,LineNumberTable");
        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be preserved");
    }

    @Test
    public void testGetrenamesourcefileattribute_withObfuscationEnabled() throws Exception {
        // renamesourcefileattribute works with obfuscation
        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testGetrenamesourcefileattribute_forObfuscatingStackTraces() throws Exception {
        // Empty string removes the SourceFile attribute, obfuscating stack traces
        task.getrenamesourcefileattribute();
        task.keepattributes("LineNumberTable");

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should set to empty string to remove source file info");
        assertNotNull(task.configuration.keepAttributes, "Should keep LineNumberTable");
    }

    @Test
    public void testGetrenamesourcefileattribute_forProductionBuild() throws Exception {
        // Production builds might want to hide source file names
        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "Should hide source file names");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testGetrenamesourcefileattribute_withDebuggingDisabled() throws Exception {
        // When not debugging, source file attribute can be removed
        task.getrenamesourcefileattribute();
        task.dontoptimize();

        assertEquals("", task.configuration.newSourceFileAttribute, "Source file should be removed");
    }

    @Test
    public void testGetrenamesourcefileattribute_forSecurityHardening() throws Exception {
        // Security-conscious builds remove source file information
        task.getrenamesourcefileattribute();
        task.keepattributes("Exceptions");

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should remove source file info for security");
    }

    @Test
    public void testGetrenamesourcefileattribute_forCodeObfuscation() throws Exception {
        // Obfuscate source file names as part of overall obfuscation strategy
        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "Source file should be obfuscated");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testGetrenamesourcefileattribute_forReverseEngineeringProtection() throws Exception {
        // Make reverse engineering harder by removing source file names
        task.getrenamesourcefileattribute();
        task.dontobfuscate();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should remove source file info");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testGetrenamesourcefileattribute_afterManuallySet() throws Exception {
        task.configuration.newSourceFileAttribute = "MySource";

        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Should be reset to empty string");
    }

    @Test
    public void testGetrenamesourcefileattribute_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testGetrenamesourcefileattribute_calledFirst() throws Exception {
        // Test calling getrenamesourcefileattribute before any other configuration
        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetrenamesourcefileattribute_calledLast() throws Exception {
        // Test calling getrenamesourcefileattribute after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testGetrenamesourcefileattribute_androidReleaseBuild() throws Exception {
        // Release builds typically obfuscate source file names
        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be obfuscated for release");
        assertTrue(task.configuration.optimize, "optimization should be enabled for release");
    }

    @Test
    public void testGetrenamesourcefileattribute_androidWithCrashReporting() throws Exception {
        // Even with crash reporting, source file can be removed if line numbers are kept
        task.getrenamesourcefileattribute();
        task.keepattributes("LineNumberTable");

        assertEquals("", task.configuration.newSourceFileAttribute, "Source file should be removed");
        assertNotNull(task.configuration.keepAttributes, "LineNumberTable should be kept");
    }

    @Test
    public void testGetrenamesourcefileattribute_androidProduction() throws Exception {
        // Production Android builds with enhanced security
        task.getrenamesourcefileattribute();
        task.keepattributes("Exceptions");

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be removed for production");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testGetrenamesourcefileattribute_beforeVerbose() throws Exception {
        task.getrenamesourcefileattribute();
        task.verbose();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testGetrenamesourcefileattribute_afterVerbose() throws Exception {
        task.verbose();
        task.getrenamesourcefileattribute();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testGetrenamesourcefileattribute_betweenOtherCalls() throws Exception {
        task.verbose();
        task.getrenamesourcefileattribute();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetrenamesourcefileattribute_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.getrenamesourcefileattribute();
        task.keepattributes("Exceptions");
        task.verbose();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testGetrenamesourcefileattribute_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.getrenamesourcefileattribute();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testGetrenamesourcefileattribute_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.getrenamesourcefileattribute();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    @Test
    public void testGetrenamesourcefileattribute_doesNotAffectOtherStringFields() throws Exception {
        task.configuration.flattenPackageHierarchy = "com/example";
        task.configuration.repackageClasses = "com/obf";

        task.getrenamesourcefileattribute();

        assertEquals("com/example", task.configuration.flattenPackageHierarchy,
                    "flattenPackageHierarchy should be preserved");
        assertEquals("com/obf", task.configuration.repackageClasses,
                    "repackageClasses should be preserved");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testGetrenamesourcefileattribute_forHidingInternalStructure() throws Exception {
        // Hide internal project structure by removing source file attribute
        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be removed to hide structure");
    }

    @Test
    public void testGetrenamesourcefileattribute_forIntellectualPropertyProtection() throws Exception {
        // Protect intellectual property by removing source file information
        task.getrenamesourcefileattribute();
        task.keepattributes("Exceptions");

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be removed for IP protection");
    }

    @Test
    public void testGetrenamesourcefileattribute_forMinimalStackTraces() throws Exception {
        // Minimal stack traces without file names
        task.getrenamesourcefileattribute();
        task.keepattributes("LineNumberTable");

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be removed");
        assertNotNull(task.configuration.keepAttributes, "LineNumberTable should be kept");
    }

    @Test
    public void testGetrenamesourcefileattribute_forSecurityThroughObscurity() throws Exception {
        // Security through obscurity: remove identifying information
        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute,
                    "Source file should be removed for security");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testGetrenamesourcefileattribute_configurationNotNull() throws Exception {
        task.getrenamesourcefileattribute();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetrenamesourcefileattribute_returnValueConsistency() throws Exception {
        Object result1 = task.getrenamesourcefileattribute();
        Object result2 = task.getrenamesourcefileattribute();

        assertEquals(result1, result2, "Return value should be consistent (both null)");
    }

    @Test
    public void testGetrenamesourcefileattribute_taskStateValid() throws Exception {
        task.getrenamesourcefileattribute();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetrenamesourcefileattribute_withMinimalConfiguration() throws Exception {
        // Test with only getrenamesourcefileattribute called
        Object result = task.getrenamesourcefileattribute();

        assertNull(result, "Should return null");
        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetrenamesourcefileattribute_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.getrenamesourcefileattribute();

        assertEquals("", task.configuration.newSourceFileAttribute, "newSourceFileAttribute should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testGetrenamesourcefileattribute_emptyStringIsNotNull() throws Exception {
        task.getrenamesourcefileattribute();

        assertNotNull(task.configuration.newSourceFileAttribute,
                     "newSourceFileAttribute should not be null");
        assertEquals("", task.configuration.newSourceFileAttribute,
                    "newSourceFileAttribute should be empty string");
        assertTrue(task.configuration.newSourceFileAttribute.isEmpty(),
                  "newSourceFileAttribute should be empty");
    }
}

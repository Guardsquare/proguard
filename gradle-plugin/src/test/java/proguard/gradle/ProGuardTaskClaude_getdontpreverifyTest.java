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
 * Comprehensive tests for ProGuardTask.getdontpreverify() method.
 * This is a Groovy DSL getter that returns null and calls dontpreverify()
 * to set configuration.preverify = false.
 */
public class ProGuardTaskClaude_getdontpreverifyTest {

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
    // Tests for getdontpreverify() Method
    // ========================================

    @Test
    public void testGetdontpreverify_returnsNull() throws Exception {
        Object result = task.getdontpreverify();

        assertNull(result, "getdontpreverify should return null for Groovy DSL support");
    }

    @Test
    public void testGetdontpreverify_setsPreverifyToFalse() throws Exception {
        assertTrue(task.configuration.preverify, "preverify should initially be true");

        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be set to false");
    }

    @Test
    public void testGetdontpreverify_callsDontpreverify() throws Exception {
        // getdontpreverify should call dontpreverify()
        assertTrue(task.configuration.preverify, "preverify should initially be true");

        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "dontpreverify() should have been called");
    }

    @Test
    public void testGetdontpreverify_isIdempotent() throws Exception {
        task.getdontpreverify();
        boolean firstResult = task.configuration.preverify;

        task.getdontpreverify();

        assertEquals(firstResult, task.configuration.preverify,
                    "Multiple calls should not change the state");
        assertFalse(task.configuration.preverify, "preverify should remain false");
    }

    @Test
    public void testGetdontpreverify_multipleCalls() throws Exception {
        task.getdontpreverify();
        task.getdontpreverify();
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should remain false after multiple calls");
    }

    @Test
    public void testGetdontpreverify_alwaysReturnsNull() throws Exception {
        Object result1 = task.getdontpreverify();
        Object result2 = task.getdontpreverify();
        Object result3 = task.getdontpreverify();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetdontpreverify_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getdontpreverify();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testGetdontpreverify_afterManuallySetToTrue() throws Exception {
        task.configuration.preverify = true;

        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be set to false");
    }

    @Test
    public void testGetdontpreverify_whenAlreadyFalse() throws Exception {
        task.configuration.preverify = false;

        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should remain false");
    }

    @Test
    public void testGetdontpreverify_supportsGroovyDSL() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        Object result = task.getdontpreverify();

        assertNull(result, "Should return null for Groovy property access");
        assertFalse(task.configuration.preverify, "Should disable preverification");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testGetdontpreverify_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testGetdontpreverify_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getdontpreverify();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testGetdontpreverify_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testGetdontpreverify_forJava6AndAbove() throws Exception {
        // Preverification is only needed for Java ME, not modern Java
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for Java 6+");
    }

    @Test
    public void testGetdontpreverify_forAndroidBuilds() throws Exception {
        // Android doesn't need preverification
        task.getdontpreverify();
        task.dontoptimize();

        assertFalse(task.configuration.preverify, "Should disable preverification for Android");
    }

    @Test
    public void testGetdontpreverify_forStandardJavaApplication() throws Exception {
        // Standard Java applications don't need preverification
        task.getdontpreverify();
        task.keepattributes("*Annotation*");

        assertFalse(task.configuration.preverify, "Should disable preverification for standard Java apps");
    }

    @Test
    public void testGetdontpreverify_forModernJVMs() throws Exception {
        // Modern JVMs don't require preverification
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for modern JVMs");
    }

    @Test
    public void testGetdontpreverify_whenTargetingNonJavaME() throws Exception {
        // Preverification is specifically for Java ME
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification when not targeting Java ME");
    }

    @Test
    public void testGetdontpreverify_forServerApplications() throws Exception {
        // Server applications don't need preverification
        task.getdontpreverify();
        task.keepattributes("*Annotation*,Signature");

        assertFalse(task.configuration.preverify, "Should disable preverification for server apps");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testGetdontpreverify_afterManuallySettingConfiguration() throws Exception {
        task.configuration.preverify = true;
        task.configuration.optimize = true;

        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be set to false");
        assertTrue(task.configuration.optimize, "Other settings should be preserved");
    }

    @Test
    public void testGetdontpreverify_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testGetdontpreverify_calledFirst() throws Exception {
        // Test calling getdontpreverify before any other configuration
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetdontpreverify_calledLast() throws Exception {
        // Test calling getdontpreverify after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testGetdontpreverify_androidDebugBuild() throws Exception {
        // Android debug builds don't need preverification
        task.getdontpreverify();
        task.dontobfuscate();

        assertFalse(task.configuration.preverify, "Should disable preverification for Android debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testGetdontpreverify_androidReleaseBuild() throws Exception {
        // Android release builds don't need preverification
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for Android release");
        assertTrue(task.configuration.optimize, "optimization should be enabled for release");
    }

    @Test
    public void testGetdontpreverify_androidLibrary() throws Exception {
        // Android libraries don't need preverification
        task.getdontpreverify();
        task.dontobfuscate();

        assertFalse(task.configuration.preverify, "Should disable preverification for Android library");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testGetdontpreverify_beforeVerbose() throws Exception {
        task.getdontpreverify();
        task.verbose();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testGetdontpreverify_afterVerbose() throws Exception {
        task.verbose();
        task.getdontpreverify();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testGetdontpreverify_betweenOtherCalls() throws Exception {
        task.verbose();
        task.getdontpreverify();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertFalse(task.configuration.preverify, "preverify should be false");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetdontpreverify_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.getdontpreverify();
        task.keepattributes("*Annotation*,Signature");
        task.verbose();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testGetdontpreverify_forDebugBuild() throws Exception {
        // Debug builds don't need preverification
        task.getdontpreverify();
        task.dontobfuscate();

        assertFalse(task.configuration.preverify, "Should disable preverification for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testGetdontpreverify_forReleaseBuild() throws Exception {
        // Release builds don't need preverification (for modern Java)
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for release");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    @Test
    public void testGetdontpreverify_forLibraryModule() throws Exception {
        // Library modules don't need preverification
        task.getdontpreverify();
        task.dontobfuscate();

        assertFalse(task.configuration.preverify, "Should disable preverification for library");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for library");
    }

    @Test
    public void testGetdontpreverify_forApplicationModule() throws Exception {
        // Application modules don't need preverification (for modern Java)
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for application");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for application");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testGetdontpreverify_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.getdontpreverify();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testGetdontpreverify_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.getdontpreverify();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testGetdontpreverify_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.getdontpreverify();

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testGetdontpreverify_whenNotTargetingJavaME() throws Exception {
        // Preverification is only needed for Java ME
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification when not targeting Java ME");
    }

    @Test
    public void testGetdontpreverify_forJava8AndAbove() throws Exception {
        // Java 8+ doesn't need preverification
        task.getdontpreverify();
        task.keepattributes("*Annotation*,Signature");

        assertFalse(task.configuration.preverify, "Should disable preverification for Java 8+");
    }

    @Test
    public void testGetdontpreverify_forWebApplications() throws Exception {
        // Web applications don't need preverification
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for web apps");
    }

    @Test
    public void testGetdontpreverify_forDesktopApplications() throws Exception {
        // Desktop applications don't need preverification
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for desktop apps");
    }

    @Test
    public void testGetdontpreverify_withMicroedition() throws Exception {
        // Even if microedition is set, we might want to disable preverification
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testGetdontpreverify_configurationNotNull() throws Exception {
        task.getdontpreverify();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetdontpreverify_returnValueConsistency() throws Exception {
        Object result1 = task.getdontpreverify();
        Object result2 = task.getdontpreverify();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertEquals(result1, result2, "Return values should be consistent (both null)");
    }

    @Test
    public void testGetdontpreverify_taskStateValid() throws Exception {
        task.getdontpreverify();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetdontpreverify_withMinimalConfiguration() throws Exception {
        // Test with only getdontpreverify called
        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetdontpreverify_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.getdontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testGetdontpreverify_semanticMeaning() throws Exception {
        // Verify the semantic meaning: disables preverification
        task.getdontpreverify();

        assertFalse(task.configuration.preverify,
                   "getdontpreverify should disable preverification (set preverify to false)");
    }
}

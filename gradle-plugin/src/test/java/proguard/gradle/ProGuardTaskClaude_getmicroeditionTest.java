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
 * Comprehensive tests for ProGuardTask.getmicroedition() method.
 * This is a Groovy DSL getter that returns null and calls microedition()
 * to set configuration.microEdition = true.
 * This is used when targeting Java ME (Java Micro Edition) environments.
 */
public class ProGuardTaskClaude_getmicroeditionTest {

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
    // Tests for getmicroedition() Method
    // ========================================

    @Test
    public void testGetmicroedition_returnsNull() throws Exception {
        Object result = task.getmicroedition();

        assertNull(result, "getmicroedition should return null for Groovy DSL support");
    }

    @Test
    public void testGetmicroedition_setsMicroEditionToTrue() throws Exception {
        assertFalse(task.configuration.microEdition, "microEdition should initially be false");

        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should be set to true");
    }

    @Test
    public void testGetmicroedition_callsMicroedition() throws Exception {
        // getmicroedition should call microedition()
        assertFalse(task.configuration.microEdition, "microEdition should initially be false");

        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microedition() should have been called");
    }

    @Test
    public void testGetmicroedition_isIdempotent() throws Exception {
        task.getmicroedition();
        boolean firstResult = task.configuration.microEdition;

        task.getmicroedition();

        assertEquals(firstResult, task.configuration.microEdition,
                    "Multiple calls should not change the state");
        assertTrue(task.configuration.microEdition, "microEdition should remain true");
    }

    @Test
    public void testGetmicroedition_multipleCalls() throws Exception {
        task.getmicroedition();
        task.getmicroedition();
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should remain true after multiple calls");
    }

    @Test
    public void testGetmicroedition_alwaysReturnsNull() throws Exception {
        Object result1 = task.getmicroedition();
        Object result2 = task.getmicroedition();
        Object result3 = task.getmicroedition();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetmicroedition_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getmicroedition();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testGetmicroedition_afterManuallySetToFalse() throws Exception {
        task.configuration.microEdition = false;

        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should be set to true");
    }

    @Test
    public void testGetmicroedition_whenAlreadyTrue() throws Exception {
        task.configuration.microEdition = true;

        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should remain true");
    }

    @Test
    public void testGetmicroedition_supportsGroovyDSL() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        Object result = task.getmicroedition();

        assertNull(result, "Should return null for Groovy property access");
        assertTrue(task.configuration.microEdition, "Should enable microEdition");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testGetmicroedition_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testGetmicroedition_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getmicroedition();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testGetmicroedition_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testGetmicroedition_forJavaMETarget() throws Exception {
        // Java ME requires specific configuration
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME");
    }

    @Test
    public void testGetmicroedition_withPreverification() throws Exception {
        // Java ME typically needs preverification enabled (don't disable it)
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition");
        assertTrue(task.configuration.preverify, "preverify should remain enabled for Java ME");
    }

    @Test
    public void testGetmicroedition_forMobileDevices() throws Exception {
        // Java ME is for mobile and embedded devices
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for mobile devices");
    }

    @Test
    public void testGetmicroedition_forEmbeddedSystems() throws Exception {
        // Java ME is used in embedded systems
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for embedded systems");
    }

    @Test
    public void testGetmicroedition_forLegacyJavaMEApps() throws Exception {
        // Legacy Java ME applications
        task.getmicroedition();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.microEdition, "Should enable microEdition for legacy Java ME apps");
    }

    @Test
    public void testGetmicroedition_withJ2MELibraries() throws Exception {
        // J2ME (Java 2 Micro Edition) libraries
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for J2ME libraries");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testGetmicroedition_afterManuallySettingConfiguration() throws Exception {
        task.configuration.microEdition = false;
        task.configuration.optimize = true;

        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should be set to true");
        assertTrue(task.configuration.optimize, "Other settings should be preserved");
    }

    @Test
    public void testGetmicroedition_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testGetmicroedition_calledFirst() throws Exception {
        // Test calling getmicroedition before any other configuration
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetmicroedition_calledLast() throws Exception {
        // Test calling getmicroedition after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    // ========================================
    // Java ME Specific Scenarios
    // ========================================

    @Test
    public void testGetmicroedition_javaMEWithPreverification() throws Exception {
        // Java ME typically keeps preverification enabled
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME");
        assertTrue(task.configuration.preverify, "preverify should remain enabled for Java ME");
    }

    @Test
    public void testGetmicroedition_javaMEWithOptimization() throws Exception {
        // Java ME benefits from optimization for resource-constrained devices
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition");
        assertTrue(task.configuration.optimize, "optimization should be enabled for Java ME");
    }

    @Test
    public void testGetmicroedition_javaMEWithObfuscation() throws Exception {
        // Java ME can use obfuscation to reduce size
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testGetmicroedition_beforeVerbose() throws Exception {
        task.getmicroedition();
        task.verbose();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testGetmicroedition_afterVerbose() throws Exception {
        task.verbose();
        task.getmicroedition();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testGetmicroedition_betweenOtherCalls() throws Exception {
        task.verbose();
        task.getmicroedition();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetmicroedition_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.getmicroedition();
        task.keepattributes("*Annotation*,Signature");
        task.verbose();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testGetmicroedition_forJavaMEDebugBuild() throws Exception {
        // Java ME debug builds
        task.getmicroedition();
        task.dontobfuscate();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testGetmicroedition_forJavaMEReleaseBuild() throws Exception {
        // Java ME release builds need aggressive optimization
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME release");
        assertTrue(task.configuration.optimize, "optimization should be enabled for release");
    }

    @Test
    public void testGetmicroedition_forJavaMELibrary() throws Exception {
        // Java ME libraries
        task.getmicroedition();
        task.dontobfuscate();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME library");
    }

    @Test
    public void testGetmicroedition_forJavaMEApplication() throws Exception {
        // Java ME applications
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME application");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testGetmicroedition_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.getmicroedition();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testGetmicroedition_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.getmicroedition();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testGetmicroedition_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.getmicroedition();

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testGetmicroedition_forCLDCProfile() throws Exception {
        // CLDC (Connected Limited Device Configuration) - Java ME profile
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for CLDC profile");
    }

    @Test
    public void testGetmicroedition_forMIDPProfile() throws Exception {
        // MIDP (Mobile Information Device Profile) - Java ME profile
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for MIDP profile");
    }

    @Test
    public void testGetmicroedition_forFeaturePhones() throws Exception {
        // Feature phones use Java ME
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for feature phones");
    }

    @Test
    public void testGetmicroedition_forSetTopBoxes() throws Exception {
        // Set-top boxes may use Java ME
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for set-top boxes");
    }

    @Test
    public void testGetmicroedition_withResourceConstraints() throws Exception {
        // Java ME is designed for resource-constrained devices
        task.getmicroedition();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.microEdition, "Should enable microEdition for resource-constrained devices");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testGetmicroedition_configurationNotNull() throws Exception {
        task.getmicroedition();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetmicroedition_returnValueConsistency() throws Exception {
        Object result1 = task.getmicroedition();
        Object result2 = task.getmicroedition();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertEquals(result1, result2, "Return values should be consistent (both null)");
    }

    @Test
    public void testGetmicroedition_taskStateValid() throws Exception {
        task.getmicroedition();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetmicroedition_withMinimalConfiguration() throws Exception {
        // Test with only getmicroedition called
        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetmicroedition_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.getmicroedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testGetmicroedition_semanticMeaning() throws Exception {
        // Verify the semantic meaning: enables Java ME mode
        task.getmicroedition();

        assertTrue(task.configuration.microEdition,
                   "getmicroedition should enable Java ME mode (set microEdition to true)");
    }
}

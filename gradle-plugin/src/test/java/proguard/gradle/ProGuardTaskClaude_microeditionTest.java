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
 * Comprehensive tests for ProGuardTask.microedition() method.
 * This is a void method that sets configuration.microEdition = true.
 * This is used when targeting Java ME (Java Micro Edition) environments.
 */
public class ProGuardTaskClaude_microeditionTest {

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
    // Tests for microedition() Method
    // ========================================

    @Test
    public void testMicroedition_setsMicroEditionToTrue() throws Exception {
        assertFalse(task.configuration.microEdition, "microEdition should initially be false");

        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be set to true");
    }

    @Test
    public void testMicroedition_isIdempotent() throws Exception {
        task.microedition();
        boolean firstResult = task.configuration.microEdition;

        task.microedition();

        assertEquals(firstResult, task.configuration.microEdition,
                    "Multiple calls should not change the state");
        assertTrue(task.configuration.microEdition, "microEdition should remain true");
    }

    @Test
    public void testMicroedition_multipleCalls() throws Exception {
        task.microedition();
        task.microedition();
        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should remain true after multiple calls");
    }

    @Test
    public void testMicroedition_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.microedition();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testMicroedition_afterManuallySetToFalse() throws Exception {
        task.configuration.microEdition = false;

        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be set to true");
    }

    @Test
    public void testMicroedition_whenAlreadyTrue() throws Exception {
        task.configuration.microEdition = true;

        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should remain true");
    }

    @Test
    public void testMicroedition_doesNotReturnValue() throws Exception {
        // This is a void method, just verify it executes without error
        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testMicroedition_initialStateIsFalse() throws Exception {
        assertFalse(task.configuration.microEdition, "Default microEdition should be false");

        task.microedition();

        assertTrue(task.configuration.microEdition, "Should change from false to true");
    }

    @Test
    public void testMicroedition_persistsAcrossGets() throws Exception {
        task.microedition();
        boolean value1 = task.configuration.microEdition;
        boolean value2 = task.configuration.microEdition;

        assertTrue(value1, "First read should be true");
        assertTrue(value2, "Second read should be true");
        assertEquals(value1, value2, "Value should be consistent");
    }

    @Test
    public void testMicroedition_affectsOnlyMicroEditionFlag() throws Exception {
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;
        boolean shrinkBefore = task.configuration.shrink;

        task.microedition();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate, "obfuscate should not change");
        assertEquals(shrinkBefore, task.configuration.shrink, "shrink should not change");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testMicroedition_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testMicroedition_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.microedition();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testMicroedition_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testMicroedition_withObfuscationEnabled() throws Exception {
        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertTrue(task.configuration.obfuscate, "obfuscation should remain enabled");
    }

    @Test
    public void testMicroedition_withOptimizationEnabled() throws Exception {
        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertTrue(task.configuration.optimize, "optimization should remain enabled");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testMicroedition_forJavaMETarget() throws Exception {
        // Java ME requires specific configuration
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME");
    }

    @Test
    public void testMicroedition_withPreverification() throws Exception {
        // Java ME typically needs preverification enabled (don't disable it)
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition");
        assertTrue(task.configuration.preverify, "preverify should remain enabled for Java ME");
    }

    @Test
    public void testMicroedition_forMobileDevices() throws Exception {
        // Java ME is for mobile and embedded devices
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for mobile devices");
    }

    @Test
    public void testMicroedition_forEmbeddedSystems() throws Exception {
        // Java ME is used in embedded systems
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for embedded systems");
    }

    @Test
    public void testMicroedition_forLegacyJavaMEApps() throws Exception {
        // Legacy Java ME applications
        task.microedition();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.microEdition, "Should enable microEdition for legacy Java ME apps");
    }

    @Test
    public void testMicroedition_withJ2MELibraries() throws Exception {
        // J2ME (Java 2 Micro Edition) libraries
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for J2ME libraries");
    }

    @Test
    public void testMicroedition_forCLDCProfile() throws Exception {
        // CLDC (Connected Limited Device Configuration) - Java ME profile
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for CLDC profile");
    }

    @Test
    public void testMicroedition_forMIDPProfile() throws Exception {
        // MIDP (Mobile Information Device Profile) - Java ME profile
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for MIDP profile");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testMicroedition_afterManuallySettingConfiguration() throws Exception {
        task.configuration.microEdition = false;
        task.configuration.optimize = true;

        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be set to true");
        assertTrue(task.configuration.optimize, "Other settings should be preserved");
    }

    @Test
    public void testMicroedition_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testMicroedition_calledFirst() throws Exception {
        // Test calling microedition before any other configuration
        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testMicroedition_calledLast() throws Exception {
        // Test calling microedition after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    // ========================================
    // Java ME Specific Scenarios
    // ========================================

    @Test
    public void testMicroedition_javaMEWithPreverification() throws Exception {
        // Java ME typically keeps preverification enabled
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME");
        assertTrue(task.configuration.preverify, "preverify should remain enabled for Java ME");
    }

    @Test
    public void testMicroedition_javaMEWithOptimization() throws Exception {
        // Java ME benefits from optimization for resource-constrained devices
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition");
        assertTrue(task.configuration.optimize, "optimization should be enabled for Java ME");
    }

    @Test
    public void testMicroedition_javaMEWithObfuscation() throws Exception {
        // Java ME can use obfuscation to reduce size
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testMicroedition_javaMEWithShrinking() throws Exception {
        // Java ME benefits from shrinking for resource-constrained devices
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testMicroedition_beforeVerbose() throws Exception {
        task.microedition();
        task.verbose();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testMicroedition_afterVerbose() throws Exception {
        task.verbose();
        task.microedition();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testMicroedition_betweenOtherCalls() throws Exception {
        task.verbose();
        task.microedition();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testMicroedition_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.microedition();
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
    public void testMicroedition_forJavaMEDebugBuild() throws Exception {
        // Java ME debug builds
        task.microedition();
        task.dontobfuscate();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testMicroedition_forJavaMEReleaseBuild() throws Exception {
        // Java ME release builds need aggressive optimization
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME release");
        assertTrue(task.configuration.optimize, "optimization should be enabled for release");
    }

    @Test
    public void testMicroedition_forJavaMELibrary() throws Exception {
        // Java ME libraries
        task.microedition();
        task.dontobfuscate();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME library");
    }

    @Test
    public void testMicroedition_forJavaMEApplication() throws Exception {
        // Java ME applications
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Java ME application");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testMicroedition_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.microedition();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testMicroedition_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.microedition();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    @Test
    public void testMicroedition_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.microedition();

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.microEdition, "microEdition should be true");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testMicroedition_forFeaturePhones() throws Exception {
        // Feature phones use Java ME
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for feature phones");
    }

    @Test
    public void testMicroedition_forSetTopBoxes() throws Exception {
        // Set-top boxes may use Java ME
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for set-top boxes");
    }

    @Test
    public void testMicroedition_withResourceConstraints() throws Exception {
        // Java ME is designed for resource-constrained devices
        task.microedition();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.microEdition, "Should enable microEdition for resource-constrained devices");
    }

    @Test
    public void testMicroedition_forBlueRayPlayers() throws Exception {
        // Blu-ray players may use Java ME (BD-J)
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for Blu-ray players");
    }

    @Test
    public void testMicroedition_forIoTDevices() throws Exception {
        // IoT devices may use Java ME
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for IoT devices");
    }

    @Test
    public void testMicroedition_withMinimalMemory() throws Exception {
        // Devices with minimal memory use Java ME
        task.microedition();

        assertTrue(task.configuration.microEdition, "Should enable microEdition for minimal memory devices");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testMicroedition_configurationNotNull() throws Exception {
        task.microedition();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testMicroedition_taskStateValid() throws Exception {
        task.microedition();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testMicroedition_withMinimalConfiguration() throws Exception {
        // Test with only microedition called
        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testMicroedition_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.microedition();

        assertTrue(task.configuration.microEdition, "microEdition should be true");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testMicroedition_semanticMeaning() throws Exception {
        // Verify the semantic meaning: enables Java ME mode
        task.microedition();

        assertTrue(task.configuration.microEdition,
                   "microedition should enable Java ME mode (set microEdition to true)");
    }

    @Test
    public void testMicroedition_changesConfiguration() throws Exception {
        boolean before = task.configuration.microEdition;

        task.microedition();
        boolean after = task.configuration.microEdition;

        assertFalse(before, "microEdition should initially be false");
        assertTrue(after, "microEdition should be true after calling microedition");
        assertNotEquals(before, after, "Configuration should have changed");
    }
}

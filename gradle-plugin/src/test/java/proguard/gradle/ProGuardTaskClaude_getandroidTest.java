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
 * Comprehensive tests for ProGuardTask.getandroid() method.
 * This is a Groovy DSL getter that returns null and calls android()
 * to set configuration.android = true.
 * This is used when targeting Android environments.
 */
public class ProGuardTaskClaude_getandroidTest {

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
    // Tests for getandroid() Method
    // ========================================

    @Test
    public void testGetandroid_returnsNull() throws Exception {
        Object result = task.getandroid();

        assertNull(result, "getandroid should return null for Groovy DSL support");
    }

    @Test
    public void testGetandroid_setsAndroidToTrue() throws Exception {
        assertFalse(task.configuration.android, "android should initially be false");

        task.getandroid();

        assertTrue(task.configuration.android, "android should be set to true");
    }

    @Test
    public void testGetandroid_callsAndroid() throws Exception {
        // getandroid should call android()
        assertFalse(task.configuration.android, "android should initially be false");

        task.getandroid();

        assertTrue(task.configuration.android, "android() should have been called");
    }

    @Test
    public void testGetandroid_isIdempotent() throws Exception {
        task.getandroid();
        boolean firstResult = task.configuration.android;

        task.getandroid();

        assertEquals(firstResult, task.configuration.android,
                    "Multiple calls should not change the state");
        assertTrue(task.configuration.android, "android should remain true");
    }

    @Test
    public void testGetandroid_multipleCalls() throws Exception {
        task.getandroid();
        task.getandroid();
        task.getandroid();

        assertTrue(task.configuration.android, "android should remain true after multiple calls");
    }

    @Test
    public void testGetandroid_alwaysReturnsNull() throws Exception {
        Object result1 = task.getandroid();
        Object result2 = task.getandroid();
        Object result3 = task.getandroid();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetandroid_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getandroid();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testGetandroid_afterManuallySetToFalse() throws Exception {
        task.configuration.android = false;

        task.getandroid();

        assertTrue(task.configuration.android, "android should be set to true");
    }

    @Test
    public void testGetandroid_whenAlreadyTrue() throws Exception {
        task.configuration.android = true;

        task.getandroid();

        assertTrue(task.configuration.android, "android should remain true");
    }

    @Test
    public void testGetandroid_supportsGroovyDSL() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        Object result = task.getandroid();

        assertNull(result, "Should return null for Groovy property access");
        assertTrue(task.configuration.android, "Should enable Android mode");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testGetandroid_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.getandroid();

        assertTrue(task.configuration.android, "android should be true");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testGetandroid_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getandroid();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testGetandroid_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getandroid();

        assertTrue(task.configuration.android, "android should be true");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testGetandroid_forAndroidApplication() throws Exception {
        // Android applications require specific configuration
        task.getandroid();

        assertTrue(task.configuration.android, "Should enable Android mode for Android apps");
    }

    @Test
    public void testGetandroid_forAndroidLibrary() throws Exception {
        // Android libraries need Android mode
        task.getandroid();

        assertTrue(task.configuration.android, "Should enable Android mode for Android libraries");
    }

    @Test
    public void testGetandroid_withDontpreverify() throws Exception {
        // Android doesn't need preverification
        task.getandroid();
        task.dontpreverify();

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertFalse(task.configuration.preverify, "preverify should be disabled for Android");
    }

    @Test
    public void testGetandroid_forAndroidDebugBuild() throws Exception {
        // Android debug builds
        task.getandroid();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "Should enable Android mode for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testGetandroid_forAndroidReleaseBuild() throws Exception {
        // Android release builds need aggressive optimization
        task.getandroid();

        assertTrue(task.configuration.android, "Should enable Android mode for release");
        assertTrue(task.configuration.optimize, "optimization should be enabled for release");
    }

    @Test
    public void testGetandroid_withAndroidKeepRules() throws Exception {
        // Android-specific keep rules
        task.getandroid();
        task.keepattributes("*Annotation*,Signature,InnerClasses,EnclosingMethod");

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertNotNull(task.configuration.keepAttributes, "Should have keep attributes");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testGetandroid_afterManuallySettingConfiguration() throws Exception {
        task.configuration.android = false;
        task.configuration.optimize = true;

        task.getandroid();

        assertTrue(task.configuration.android, "android should be set to true");
        assertTrue(task.configuration.optimize, "Other settings should be preserved");
    }

    @Test
    public void testGetandroid_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getandroid();

        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testGetandroid_calledFirst() throws Exception {
        // Test calling getandroid before any other configuration
        task.getandroid();

        assertTrue(task.configuration.android, "android should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetandroid_calledLast() throws Exception {
        // Test calling getandroid after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.getandroid();

        assertTrue(task.configuration.android, "android should be true");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testGetandroid_androidWithOptimization() throws Exception {
        // Android benefits from optimization
        task.getandroid();

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertTrue(task.configuration.optimize, "optimization should be enabled for Android");
    }

    @Test
    public void testGetandroid_androidWithObfuscation() throws Exception {
        // Android can use obfuscation
        task.getandroid();

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testGetandroid_androidWithShrinking() throws Exception {
        // Android benefits from shrinking
        task.getandroid();

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testGetandroid_beforeVerbose() throws Exception {
        task.getandroid();
        task.verbose();

        assertTrue(task.configuration.android, "android should be true");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testGetandroid_afterVerbose() throws Exception {
        task.verbose();
        task.getandroid();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testGetandroid_betweenOtherCalls() throws Exception {
        task.verbose();
        task.getandroid();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.android, "android should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetandroid_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.getandroid();
        task.keepattributes("*Annotation*,Signature");
        task.verbose();

        assertTrue(task.configuration.android, "android should be true");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testGetandroid_forDebugBuild() throws Exception {
        // Android debug builds
        task.getandroid();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "Should enable Android mode for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testGetandroid_forReleaseBuild() throws Exception {
        // Android release builds need aggressive optimization
        task.getandroid();

        assertTrue(task.configuration.android, "Should enable Android mode for release");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    @Test
    public void testGetandroid_forLibraryModule() throws Exception {
        // Android libraries
        task.getandroid();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "Should enable Android mode for library");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for library");
    }

    @Test
    public void testGetandroid_forApplicationModule() throws Exception {
        // Android applications
        task.getandroid();

        assertTrue(task.configuration.android, "Should enable Android mode for application");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for application");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testGetandroid_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.getandroid();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testGetandroid_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.getandroid();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testGetandroid_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.getandroid();

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.android, "android should be true");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testGetandroid_forAndroidApp() throws Exception {
        // Standard Android app
        task.getandroid();
        task.dontpreverify();

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertFalse(task.configuration.preverify, "Android doesn't need preverification");
    }

    @Test
    public void testGetandroid_forAndroidLib() throws Exception {
        // Android library
        task.getandroid();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "Should enable Android mode for library");
    }

    @Test
    public void testGetandroid_withAndroidSDK() throws Exception {
        // Using Android SDK
        task.getandroid();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.android, "Should enable Android mode");
    }

    @Test
    public void testGetandroid_forAndroidWear() throws Exception {
        // Android Wear applications
        task.getandroid();

        assertTrue(task.configuration.android, "Should enable Android mode for Wear");
    }

    @Test
    public void testGetandroid_forAndroidTV() throws Exception {
        // Android TV applications
        task.getandroid();

        assertTrue(task.configuration.android, "Should enable Android mode for TV");
    }

    @Test
    public void testGetandroid_forAndroidAuto() throws Exception {
        // Android Auto applications
        task.getandroid();

        assertTrue(task.configuration.android, "Should enable Android mode for Auto");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testGetandroid_configurationNotNull() throws Exception {
        task.getandroid();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetandroid_returnValueConsistency() throws Exception {
        Object result1 = task.getandroid();
        Object result2 = task.getandroid();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertEquals(result1, result2, "Return values should be consistent (both null)");
    }

    @Test
    public void testGetandroid_taskStateValid() throws Exception {
        task.getandroid();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetandroid_withMinimalConfiguration() throws Exception {
        // Test with only getandroid called
        task.getandroid();

        assertTrue(task.configuration.android, "android should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetandroid_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.getandroid();

        assertTrue(task.configuration.android, "android should be true");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testGetandroid_semanticMeaning() throws Exception {
        // Verify the semantic meaning: enables Android mode
        task.getandroid();

        assertTrue(task.configuration.android,
                   "getandroid should enable Android mode (set android to true)");
    }
}

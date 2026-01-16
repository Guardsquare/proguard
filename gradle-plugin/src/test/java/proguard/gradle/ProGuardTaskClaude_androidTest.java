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
 * Comprehensive tests for ProGuardTask.android() method.
 * This is a void method that sets configuration.android = true.
 * This is used when targeting Android environments.
 */
public class ProGuardTaskClaude_androidTest {

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
    // Tests for android() Method
    // ========================================

    @Test
    public void testAndroid_setsAndroidToTrue() throws Exception {
        assertFalse(task.configuration.android, "android should initially be false");

        task.android();

        assertTrue(task.configuration.android, "android should be set to true");
    }

    @Test
    public void testAndroid_isIdempotent() throws Exception {
        task.android();
        boolean firstResult = task.configuration.android;

        task.android();

        assertEquals(firstResult, task.configuration.android,
                    "Multiple calls should not change the state");
        assertTrue(task.configuration.android, "android should remain true");
    }

    @Test
    public void testAndroid_multipleCalls() throws Exception {
        task.android();
        task.android();
        task.android();

        assertTrue(task.configuration.android, "android should remain true after multiple calls");
    }

    @Test
    public void testAndroid_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.android();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testAndroid_afterManuallySetToFalse() throws Exception {
        task.configuration.android = false;

        task.android();

        assertTrue(task.configuration.android, "android should be set to true");
    }

    @Test
    public void testAndroid_whenAlreadyTrue() throws Exception {
        task.configuration.android = true;

        task.android();

        assertTrue(task.configuration.android, "android should remain true");
    }

    @Test
    public void testAndroid_doesNotReturnValue() throws Exception {
        // This is a void method, just verify it executes without error
        task.android();

        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testAndroid_initialStateIsFalse() throws Exception {
        assertFalse(task.configuration.android, "Default android should be false");

        task.android();

        assertTrue(task.configuration.android, "Should change from false to true");
    }

    @Test
    public void testAndroid_persistsAcrossGets() throws Exception {
        task.android();
        boolean value1 = task.configuration.android;
        boolean value2 = task.configuration.android;

        assertTrue(value1, "First read should be true");
        assertTrue(value2, "Second read should be true");
        assertEquals(value1, value2, "Value should be consistent");
    }

    @Test
    public void testAndroid_affectsOnlyAndroidFlag() throws Exception {
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;
        boolean shrinkBefore = task.configuration.shrink;

        task.android();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate, "obfuscate should not change");
        assertEquals(shrinkBefore, task.configuration.shrink, "shrink should not change");
        assertTrue(task.configuration.android, "android should be true");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testAndroid_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.android();

        assertTrue(task.configuration.android, "android should be true");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testAndroid_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.android();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testAndroid_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.android();

        assertTrue(task.configuration.android, "android should be true");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testAndroid_withObfuscationEnabled() throws Exception {
        task.android();

        assertTrue(task.configuration.android, "android should be true");
        assertTrue(task.configuration.obfuscate, "obfuscation should remain enabled");
    }

    @Test
    public void testAndroid_withOptimizationEnabled() throws Exception {
        task.android();

        assertTrue(task.configuration.android, "android should be true");
        assertTrue(task.configuration.optimize, "optimization should remain enabled");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testAndroid_forAndroidApplication() throws Exception {
        // Android applications require specific configuration
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode for Android apps");
    }

    @Test
    public void testAndroid_forAndroidLibrary() throws Exception {
        // Android libraries need Android mode
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode for Android libraries");
    }

    @Test
    public void testAndroid_withDontpreverify() throws Exception {
        // Android doesn't need preverification
        task.android();
        task.dontpreverify();

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertFalse(task.configuration.preverify, "preverify should be disabled for Android");
    }

    @Test
    public void testAndroid_forAndroidDebugBuild() throws Exception {
        // Android debug builds
        task.android();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "Should enable Android mode for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testAndroid_forAndroidReleaseBuild() throws Exception {
        // Android release builds need aggressive optimization
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode for release");
        assertTrue(task.configuration.optimize, "optimization should be enabled for release");
    }

    @Test
    public void testAndroid_withAndroidKeepRules() throws Exception {
        // Android-specific keep rules
        task.android();
        task.keepattributes("*Annotation*,Signature,InnerClasses,EnclosingMethod");

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertNotNull(task.configuration.keepAttributes, "Should have keep attributes");
    }

    @Test
    public void testAndroid_forAndroidApp() throws Exception {
        // Standard Android app
        task.android();
        task.dontpreverify();

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertFalse(task.configuration.preverify, "Android doesn't need preverification");
    }

    @Test
    public void testAndroid_withAndroidSDK() throws Exception {
        // Using Android SDK
        task.android();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.android, "Should enable Android mode");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testAndroid_afterManuallySettingConfiguration() throws Exception {
        task.configuration.android = false;
        task.configuration.optimize = true;

        task.android();

        assertTrue(task.configuration.android, "android should be set to true");
        assertTrue(task.configuration.optimize, "Other settings should be preserved");
    }

    @Test
    public void testAndroid_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.android();

        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testAndroid_calledFirst() throws Exception {
        // Test calling android before any other configuration
        task.android();

        assertTrue(task.configuration.android, "android should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testAndroid_calledLast() throws Exception {
        // Test calling android after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.android();

        assertTrue(task.configuration.android, "android should be true");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testAndroid_androidWithOptimization() throws Exception {
        // Android benefits from optimization
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertTrue(task.configuration.optimize, "optimization should be enabled for Android");
    }

    @Test
    public void testAndroid_androidWithObfuscation() throws Exception {
        // Android can use obfuscation
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testAndroid_androidWithShrinking() throws Exception {
        // Android benefits from shrinking
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
    }

    @Test
    public void testAndroid_androidWithR8Compatibility() throws Exception {
        // Android with R8 compatibility considerations
        task.android();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.android, "Should enable Android mode");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testAndroid_beforeVerbose() throws Exception {
        task.android();
        task.verbose();

        assertTrue(task.configuration.android, "android should be true");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testAndroid_afterVerbose() throws Exception {
        task.verbose();
        task.android();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testAndroid_betweenOtherCalls() throws Exception {
        task.verbose();
        task.android();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.android, "android should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testAndroid_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.android();
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
    public void testAndroid_forDebugBuild() throws Exception {
        // Android debug builds
        task.android();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "Should enable Android mode for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testAndroid_forReleaseBuild() throws Exception {
        // Android release builds need aggressive optimization
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode for release");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    @Test
    public void testAndroid_forLibraryModule() throws Exception {
        // Android libraries
        task.android();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "Should enable Android mode for library");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for library");
    }

    @Test
    public void testAndroid_forApplicationModule() throws Exception {
        // Android applications
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode for application");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for application");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testAndroid_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.android();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testAndroid_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.android();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.android, "android should be true");
    }

    @Test
    public void testAndroid_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.android();

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.android, "android should be true");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testAndroid_forAndroidWear() throws Exception {
        // Android Wear applications
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode for Wear");
    }

    @Test
    public void testAndroid_forAndroidTV() throws Exception {
        // Android TV applications
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode for TV");
    }

    @Test
    public void testAndroid_forAndroidAuto() throws Exception {
        // Android Auto applications
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode for Auto");
    }

    @Test
    public void testAndroid_forAndroidThings() throws Exception {
        // Android Things (IoT) applications
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode for Things");
    }

    @Test
    public void testAndroid_forKotlinAndroid() throws Exception {
        // Kotlin Android applications
        task.android();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.android, "Should enable Android mode for Kotlin");
    }

    @Test
    public void testAndroid_forMultiModuleAndroid() throws Exception {
        // Multi-module Android projects
        task.android();

        assertTrue(task.configuration.android, "Should enable Android mode for multi-module");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testAndroid_configurationNotNull() throws Exception {
        task.android();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testAndroid_taskStateValid() throws Exception {
        task.android();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testAndroid_withMinimalConfiguration() throws Exception {
        // Test with only android called
        task.android();

        assertTrue(task.configuration.android, "android should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testAndroid_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.android();

        assertTrue(task.configuration.android, "android should be true");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testAndroid_semanticMeaning() throws Exception {
        // Verify the semantic meaning: enables Android mode
        task.android();

        assertTrue(task.configuration.android,
                   "android should enable Android mode (set android to true)");
    }

    @Test
    public void testAndroid_changesConfiguration() throws Exception {
        boolean before = task.configuration.android;

        task.android();
        boolean after = task.configuration.android;

        assertFalse(before, "android should initially be false");
        assertTrue(after, "android should be true after calling android");
        assertNotEquals(before, after, "Configuration should have changed");
    }
}

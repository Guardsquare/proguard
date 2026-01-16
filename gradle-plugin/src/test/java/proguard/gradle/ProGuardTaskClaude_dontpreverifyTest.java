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
 * Comprehensive tests for ProGuardTask.dontpreverify() method.
 * This is a void method that sets configuration.preverify = false.
 * Preverification is only needed for Java ME; modern Java, Android, and
 * standard applications don't require it.
 */
public class ProGuardTaskClaude_dontpreverifyTest {

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
    // Tests for dontpreverify() Method
    // ========================================

    @Test
    public void testDontpreverify_setsPreverifyToFalse() throws Exception {
        assertTrue(task.configuration.preverify, "preverify should initially be true");

        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be set to false");
    }

    @Test
    public void testDontpreverify_isIdempotent() throws Exception {
        task.dontpreverify();
        boolean firstResult = task.configuration.preverify;

        task.dontpreverify();

        assertEquals(firstResult, task.configuration.preverify,
                    "Multiple calls should not change the state");
        assertFalse(task.configuration.preverify, "preverify should remain false");
    }

    @Test
    public void testDontpreverify_multipleCalls() throws Exception {
        task.dontpreverify();
        task.dontpreverify();
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should remain false after multiple calls");
    }

    @Test
    public void testDontpreverify_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.dontpreverify();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testDontpreverify_afterManuallySetToTrue() throws Exception {
        task.configuration.preverify = true;

        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be set to false");
    }

    @Test
    public void testDontpreverify_whenAlreadyFalse() throws Exception {
        task.configuration.preverify = false;

        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should remain false");
    }

    @Test
    public void testDontpreverify_doesNotReturnValue() throws Exception {
        // This is a void method, just verify it executes without error
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testDontpreverify_initialStateIsTrue() throws Exception {
        assertTrue(task.configuration.preverify, "Default preverify should be true");

        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should change from true to false");
    }

    @Test
    public void testDontpreverify_persistsAcrossGets() throws Exception {
        task.dontpreverify();
        boolean value1 = task.configuration.preverify;
        boolean value2 = task.configuration.preverify;

        assertFalse(value1, "First read should be false");
        assertFalse(value2, "Second read should be false");
        assertEquals(value1, value2, "Value should be consistent");
    }

    @Test
    public void testDontpreverify_affectsOnlyPreverifyFlag() throws Exception {
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;
        boolean shrinkBefore = task.configuration.shrink;

        task.dontpreverify();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate, "obfuscate should not change");
        assertEquals(shrinkBefore, task.configuration.shrink, "shrink should not change");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testDontpreverify_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testDontpreverify_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.dontpreverify();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testDontpreverify_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testDontpreverify_withObfuscationEnabled() throws Exception {
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertTrue(task.configuration.obfuscate, "obfuscation should remain enabled");
    }

    @Test
    public void testDontpreverify_withOptimizationEnabled() throws Exception {
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertTrue(task.configuration.optimize, "optimization should remain enabled");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testDontpreverify_forJava6AndAbove() throws Exception {
        // Preverification is only needed for Java ME, not modern Java
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for Java 6+");
    }

    @Test
    public void testDontpreverify_forAndroidBuilds() throws Exception {
        // Android doesn't need preverification
        task.dontpreverify();
        task.dontoptimize();

        assertFalse(task.configuration.preverify, "Should disable preverification for Android");
    }

    @Test
    public void testDontpreverify_forStandardJavaApplication() throws Exception {
        // Standard Java applications don't need preverification
        task.dontpreverify();
        task.keepattributes("*Annotation*");

        assertFalse(task.configuration.preverify, "Should disable preverification for standard Java apps");
    }

    @Test
    public void testDontpreverify_forModernJVMs() throws Exception {
        // Modern JVMs don't require preverification
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for modern JVMs");
    }

    @Test
    public void testDontpreverify_whenTargetingNonJavaME() throws Exception {
        // Preverification is specifically for Java ME
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification when not targeting Java ME");
    }

    @Test
    public void testDontpreverify_forServerApplications() throws Exception {
        // Server applications don't need preverification
        task.dontpreverify();
        task.keepattributes("*Annotation*,Signature");

        assertFalse(task.configuration.preverify, "Should disable preverification for server apps");
    }

    @Test
    public void testDontpreverify_withJava8Features() throws Exception {
        // Java 8+ features don't need preverification
        task.dontpreverify();
        task.keepattributes("*Annotation*,Signature,InnerClasses,EnclosingMethod");

        assertFalse(task.configuration.preverify, "Should disable preverification for Java 8+");
    }

    @Test
    public void testDontpreverify_forWebApplications() throws Exception {
        // Web applications don't need preverification
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for web apps");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testDontpreverify_afterManuallySettingConfiguration() throws Exception {
        task.configuration.preverify = true;
        task.configuration.optimize = true;

        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be set to false");
        assertTrue(task.configuration.optimize, "Other settings should be preserved");
    }

    @Test
    public void testDontpreverify_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testDontpreverify_calledFirst() throws Exception {
        // Test calling dontpreverify before any other configuration
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testDontpreverify_calledLast() throws Exception {
        // Test calling dontpreverify after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testDontpreverify_androidDebugBuild() throws Exception {
        // Android debug builds don't need preverification
        task.dontpreverify();
        task.dontobfuscate();

        assertFalse(task.configuration.preverify, "Should disable preverification for Android debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testDontpreverify_androidReleaseBuild() throws Exception {
        // Android release builds don't need preverification
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for Android release");
        assertTrue(task.configuration.optimize, "optimization should be enabled for release");
    }

    @Test
    public void testDontpreverify_androidLibrary() throws Exception {
        // Android libraries don't need preverification
        task.dontpreverify();
        task.dontobfuscate();

        assertFalse(task.configuration.preverify, "Should disable preverification for Android library");
    }

    @Test
    public void testDontpreverify_androidApp() throws Exception {
        // Android apps don't need preverification
        task.dontpreverify();
        task.keepattributes("*Annotation*,Signature");

        assertFalse(task.configuration.preverify, "Should disable preverification for Android app");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testDontpreverify_beforeVerbose() throws Exception {
        task.dontpreverify();
        task.verbose();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testDontpreverify_afterVerbose() throws Exception {
        task.verbose();
        task.dontpreverify();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testDontpreverify_betweenOtherCalls() throws Exception {
        task.verbose();
        task.dontpreverify();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertFalse(task.configuration.preverify, "preverify should be false");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testDontpreverify_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.dontpreverify();
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
    public void testDontpreverify_forDebugBuild() throws Exception {
        // Debug builds don't need preverification
        task.dontpreverify();
        task.dontobfuscate();

        assertFalse(task.configuration.preverify, "Should disable preverification for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testDontpreverify_forReleaseBuild() throws Exception {
        // Release builds don't need preverification (for modern Java)
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for release");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    @Test
    public void testDontpreverify_forLibraryModule() throws Exception {
        // Library modules don't need preverification
        task.dontpreverify();
        task.dontobfuscate();

        assertFalse(task.configuration.preverify, "Should disable preverification for library");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for library");
    }

    @Test
    public void testDontpreverify_forApplicationModule() throws Exception {
        // Application modules don't need preverification (for modern Java)
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for application");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for application");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testDontpreverify_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.dontpreverify();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testDontpreverify_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.dontpreverify();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testDontpreverify_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.dontpreverify();

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testDontpreverify_whenNotTargetingJavaME() throws Exception {
        // Preverification is only needed for Java ME
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification when not targeting Java ME");
    }

    @Test
    public void testDontpreverify_forJava8AndAbove() throws Exception {
        // Java 8+ doesn't need preverification
        task.dontpreverify();
        task.keepattributes("*Annotation*,Signature");

        assertFalse(task.configuration.preverify, "Should disable preverification for Java 8+");
    }

    @Test
    public void testDontpreverify_forDesktopApplications() throws Exception {
        // Desktop applications don't need preverification
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for desktop apps");
    }

    @Test
    public void testDontpreverify_withMicroedition() throws Exception {
        // Even if microedition is set, we might want to disable preverification
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification");
    }

    @Test
    public void testDontpreverify_forKotlinApplications() throws Exception {
        // Kotlin applications don't need preverification
        task.dontpreverify();
        task.keepattributes("*Annotation*,Signature");

        assertFalse(task.configuration.preverify, "Should disable preverification for Kotlin apps");
    }

    @Test
    public void testDontpreverify_forGroovyApplications() throws Exception {
        // Groovy applications don't need preverification
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "Should disable preverification for Groovy apps");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testDontpreverify_configurationNotNull() throws Exception {
        task.dontpreverify();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testDontpreverify_taskStateValid() throws Exception {
        task.dontpreverify();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testDontpreverify_withMinimalConfiguration() throws Exception {
        // Test with only dontpreverify called
        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testDontpreverify_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.dontpreverify();

        assertFalse(task.configuration.preverify, "preverify should be false");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testDontpreverify_semanticMeaning() throws Exception {
        // Verify the semantic meaning: disables preverification
        task.dontpreverify();

        assertFalse(task.configuration.preverify,
                   "dontpreverify should disable preverification (set preverify to false)");
    }

    @Test
    public void testDontpreverify_changesConfiguration() throws Exception {
        boolean before = task.configuration.preverify;

        task.dontpreverify();
        boolean after = task.configuration.preverify;

        assertTrue(before, "preverify should initially be true");
        assertFalse(after, "preverify should be false after calling dontpreverify");
        assertNotEquals(before, after, "Configuration should have changed");
    }
}

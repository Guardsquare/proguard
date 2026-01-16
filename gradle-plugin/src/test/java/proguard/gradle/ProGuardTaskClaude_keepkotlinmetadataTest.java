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
 * Comprehensive tests for ProGuardTask.keepkotlinmetadata() method.
 * This method sets the keepKotlinMetadata flag to true.
 */
public class ProGuardTaskClaude_keepkotlinmetadataTest {

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
    // Tests for keepkotlinmetadata() Method
    // ========================================

    @Test
    public void testKeepkotlinmetadata_setsKeepKotlinMetadata() throws Exception {
        assertFalse(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should initially be false");

        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set to true");
    }

    @Test
    public void testKeepkotlinmetadata_isVoid() throws Exception {
        // Verify method doesn't return a value (void method)
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    @Test
    public void testKeepkotlinmetadata_multipleCalls() throws Exception {
        task.keepkotlinmetadata();
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be true after first call");

        task.keepkotlinmetadata();
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should remain true after second call");

        task.keepkotlinmetadata();
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should remain true after third call");
    }

    @Test
    public void testKeepkotlinmetadata_isIdempotent() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.keepkotlinmetadata();
            assertTrue(task.configuration.keepKotlinMetadata,
                      "keepKotlinMetadata should remain true on iteration " + i);
        }
    }

    @Test
    public void testKeepkotlinmetadata_afterManuallySetToFalse() throws Exception {
        task.configuration.keepKotlinMetadata = false;

        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata,
                  "keepKotlinMetadata should be set to true even after being explicitly set to false");
    }

    @Test
    public void testKeepkotlinmetadata_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.keepkotlinmetadata();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    @Test
    public void testKeepkotlinmetadata_doesNotReturnValue() throws Exception {
        // This is a void method - just verify it executes without issues
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testKeepkotlinmetadata_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testKeepkotlinmetadata_withObfuscationEnabled() throws Exception {
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    @Test
    public void testKeepkotlinmetadata_withObfuscationDisabled() throws Exception {
        task.dontobfuscate();
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testKeepkotlinmetadata_withOptimizationEnabled() throws Exception {
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.optimize, "optimization should be enabled by default");
    }

    @Test
    public void testKeepkotlinmetadata_withOptimizationDisabled() throws Exception {
        task.dontoptimize();
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
    }

    @Test
    public void testKeepkotlinmetadata_withShrinkingDisabled() throws Exception {
        task.dontshrink();
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertFalse(task.configuration.shrink, "shrinking should be disabled");
    }

    @Test
    public void testKeepkotlinmetadata_withVerboseEnabled() throws Exception {
        task.verbose();
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testKeepkotlinmetadata_withKeepAttributes() throws Exception {
        task.keepattributes("*Annotation*,Signature");
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
    }

    @Test
    public void testKeepkotlinmetadata_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.keepkotlinmetadata();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate, "obfuscate setting should not change");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testKeepkotlinmetadata_forKotlinProject() throws Exception {
        // Kotlin projects need to keep metadata for reflection and introspection
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for Kotlin");
    }

    @Test
    public void testKeepkotlinmetadata_forKotlinReflection() throws Exception {
        // Kotlin reflection relies on metadata
        task.keepkotlinmetadata();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration.keepAttributes, "Annotations should be kept");
    }

    @Test
    public void testKeepkotlinmetadata_forKotlinLibrary() throws Exception {
        // Kotlin libraries need metadata preserved
        task.keepkotlinmetadata();
        task.dontobfuscate();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for libraries");
    }

    @Test
    public void testKeepkotlinmetadata_withKotlinCoroutines() throws Exception {
        // Kotlin coroutines need metadata
        task.keepkotlinmetadata();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for coroutines");
    }

    @Test
    public void testKeepkotlinmetadata_withKotlinSerialization() throws Exception {
        // Kotlin serialization requires metadata
        task.keepkotlinmetadata();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for serialization");
    }

    @Test
    public void testKeepkotlinmetadata_forMultiplatformKotlin() throws Exception {
        // Multiplatform Kotlin projects need metadata
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for multiplatform");
    }

    @Test
    public void testKeepkotlinmetadata_forKotlinDataClasses() throws Exception {
        // Kotlin data classes rely on metadata for component functions
        task.keepkotlinmetadata();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for data classes");
    }

    @Test
    public void testKeepkotlinmetadata_forKotlinSealedClasses() throws Exception {
        // Sealed classes need metadata
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for sealed classes");
    }

    @Test
    public void testKeepkotlinmetadata_forKotlinInlineClasses() throws Exception {
        // Inline classes (value classes) need metadata
        task.keepkotlinmetadata();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for inline classes");
    }

    @Test
    public void testKeepkotlinmetadata_forKotlinDelegatedProperties() throws Exception {
        // Delegated properties need metadata
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for delegated properties");
    }

    @Test
    public void testKeepkotlinmetadata_forKotlinTypeAliases() throws Exception {
        // Type aliases benefit from metadata
        task.keepkotlinmetadata();
        task.keepattributes("Signature");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for type aliases");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testKeepkotlinmetadata_calledFirst() throws Exception {
        // Test calling keepkotlinmetadata before any other configuration
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testKeepkotlinmetadata_calledLast() throws Exception {
        // Test calling keepkotlinmetadata after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    @Test
    public void testKeepkotlinmetadata_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.keepkotlinmetadata();
        task.keepattributes("*Annotation*,Signature");
        task.verbose();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    @Test
    public void testKeepkotlinmetadata_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testKeepkotlinmetadata_androidKotlinApp() throws Exception {
        // Android apps written in Kotlin
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for Android Kotlin");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
    }

    @Test
    public void testKeepkotlinmetadata_androidKotlinLibrary() throws Exception {
        // Android library modules written in Kotlin
        task.keepkotlinmetadata();
        task.dontobfuscate();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for libraries");
    }

    @Test
    public void testKeepkotlinmetadata_androidWithKotlinExtensions() throws Exception {
        // Android projects using Kotlin Android Extensions
        task.keepkotlinmetadata();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration.keepAttributes, "Annotations should be kept");
    }

    @Test
    public void testKeepkotlinmetadata_androidDebugBuild() throws Exception {
        // Debug builds with Kotlin
        task.keepkotlinmetadata();
        task.dontobfuscate();
        task.dontoptimize();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
        assertFalse(task.configuration.optimize, "optimization should be disabled for debug");
    }

    @Test
    public void testKeepkotlinmetadata_androidReleaseBuild() throws Exception {
        // Release builds with Kotlin
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for release");
        assertTrue(task.configuration.optimize, "optimization should be enabled for release");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testKeepkotlinmetadata_beforeVerbose() throws Exception {
        task.keepkotlinmetadata();
        task.verbose();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testKeepkotlinmetadata_afterVerbose() throws Exception {
        task.verbose();
        task.keepkotlinmetadata();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    @Test
    public void testKeepkotlinmetadata_betweenOtherCalls() throws Exception {
        task.verbose();
        task.keepkotlinmetadata();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testKeepkotlinmetadata_multipleTimesWithOtherCalls() throws Exception {
        task.keepkotlinmetadata();
        task.verbose();
        task.keepkotlinmetadata();
        task.dontoptimize();
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testKeepkotlinmetadata_withAllProcessingSteps() throws Exception {
        // Test with all processing steps configured
        task.keepkotlinmetadata();
        // Shrinking, optimization, obfuscation all enabled by default

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testKeepkotlinmetadata_debugVariant() throws Exception {
        // Typical debug variant configuration
        task.keepkotlinmetadata();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertFalse(task.configuration.shrink, "shrinking should be disabled");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testKeepkotlinmetadata_releaseVariant() throws Exception {
        // Typical release variant configuration
        task.keepkotlinmetadata();
        // All processing enabled by default

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testKeepkotlinmetadata_stagingVariant() throws Exception {
        // Staging variant: optimized but debuggable
        task.keepkotlinmetadata();
        task.keepattributes("SourceFile,LineNumberTable");
        task.dontobfuscate();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for staging");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testKeepkotlinmetadata_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.keepkotlinmetadata();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    @Test
    public void testKeepkotlinmetadata_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.keepkotlinmetadata();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    @Test
    public void testKeepkotlinmetadata_doesNotAffectFilterLists() throws Exception {
        task.keeppackagenames("com.example.*");
        task.keepattributes("Signature");

        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames filter should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes filter should be preserved");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testKeepkotlinmetadata_forKotlinxLibraries() throws Exception {
        // kotlinx libraries (coroutines, serialization, etc.)
        task.keepkotlinmetadata();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for kotlinx");
    }

    @Test
    public void testKeepkotlinmetadata_forKotlinCompanionObjects() throws Exception {
        // Companion objects need metadata
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for companion objects");
    }

    @Test
    public void testKeepkotlinmetadata_forKotlinExtensionFunctions() throws Exception {
        // Extension functions metadata
        task.keepkotlinmetadata();
        task.keepattributes("Signature");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for extensions");
    }

    @Test
    public void testKeepkotlinmetadata_forKotlinLambdas() throws Exception {
        // Kotlin lambdas and inline functions
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for lambdas");
    }

    @Test
    public void testKeepkotlinmetadata_forKotlinJvmOverloads() throws Exception {
        // @JvmOverloads annotation support
        task.keepkotlinmetadata();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for JvmOverloads");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testKeepkotlinmetadata_configurationNotNull() throws Exception {
        task.keepkotlinmetadata();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testKeepkotlinmetadata_taskStateValid() throws Exception {
        task.keepkotlinmetadata();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testKeepkotlinmetadata_withMinimalConfiguration() throws Exception {
        // Test with only keepkotlinmetadata called
        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testKeepkotlinmetadata_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.keepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testKeepkotlinmetadata_repeatedConfiguration() throws Exception {
        // Test setting and resetting configuration
        task.keepkotlinmetadata();
        assertTrue(task.configuration.keepKotlinMetadata, "Should be true after first call");

        task.configuration.keepKotlinMetadata = false;
        assertFalse(task.configuration.keepKotlinMetadata, "Should be false after manual reset");

        task.keepkotlinmetadata();
        assertTrue(task.configuration.keepKotlinMetadata, "Should be true again after second call");
    }
}

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
 * Comprehensive tests for ProGuardTask.ignorewarnings() method.
 * This is a void method that sets configuration.ignoreWarnings = true.
 * When enabled, ProGuard will proceed with processing even if warnings are generated.
 */
public class ProGuardTaskClaude_ignorewarningsTest {

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
    // Tests for ignorewarnings() Method
    // ========================================

    @Test
    public void testIgnorewarnings_setsIgnoreWarningsToTrue() throws Exception {
        assertFalse(task.configuration.ignoreWarnings, "ignoreWarnings should initially be false");

        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be set to true");
    }

    @Test
    public void testIgnorewarnings_isIdempotent() throws Exception {
        task.ignorewarnings();
        boolean firstResult = task.configuration.ignoreWarnings;

        task.ignorewarnings();

        assertEquals(firstResult, task.configuration.ignoreWarnings,
                    "Multiple calls should not change the state");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should remain true");
    }

    @Test
    public void testIgnorewarnings_multipleCalls() throws Exception {
        task.ignorewarnings();
        task.ignorewarnings();
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should remain true after multiple calls");
    }

    @Test
    public void testIgnorewarnings_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.ignorewarnings();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testIgnorewarnings_afterManuallySetToFalse() throws Exception {
        task.configuration.ignoreWarnings = false;

        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be set to true");
    }

    @Test
    public void testIgnorewarnings_whenAlreadyTrue() throws Exception {
        task.configuration.ignoreWarnings = true;

        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should remain true");
    }

    @Test
    public void testIgnorewarnings_doesNotReturnValue() throws Exception {
        // This is a void method, just verify it executes without error
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testIgnorewarnings_initialStateIsFalse() throws Exception {
        assertFalse(task.configuration.ignoreWarnings, "Default ignoreWarnings should be false");

        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "Should change from false to true");
    }

    @Test
    public void testIgnorewarnings_persistsAcrossGets() throws Exception {
        task.ignorewarnings();
        boolean value1 = task.configuration.ignoreWarnings;
        boolean value2 = task.configuration.ignoreWarnings;

        assertTrue(value1, "First read should be true");
        assertTrue(value2, "Second read should be true");
        assertEquals(value1, value2, "Value should be consistent");
    }

    @Test
    public void testIgnorewarnings_affectsOnlyIgnoreWarningsFlag() throws Exception {
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;
        boolean shrinkBefore = task.configuration.shrink;

        task.ignorewarnings();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate, "obfuscate should not change");
        assertEquals(shrinkBefore, task.configuration.shrink, "shrink should not change");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testIgnorewarnings_withOtherConfigurationSettings() throws Exception {
        task.dontoptimize();
        task.android();

        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.android, "android should remain set");
    }

    @Test
    public void testIgnorewarnings_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.ignorewarnings();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testIgnorewarnings_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testIgnorewarnings_withObfuscationEnabled() throws Exception {
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertTrue(task.configuration.obfuscate, "obfuscation should remain enabled");
    }

    @Test
    public void testIgnorewarnings_withOptimizationEnabled() throws Exception {
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertTrue(task.configuration.optimize, "optimization should remain enabled");
    }

    @Test
    public void testIgnorewarnings_withDontwarn() throws Exception {
        task.dontwarn();
        task.ignorewarnings();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testIgnorewarnings_withVerbose() throws Exception {
        task.verbose();
        task.ignorewarnings();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testIgnorewarnings_forContinuingDespiteWarnings() throws Exception {
        // Allow build to continue even if ProGuard generates warnings
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings and continue");
    }

    @Test
    public void testIgnorewarnings_forCIBuild() throws Exception {
        // CI builds might ignore warnings to avoid failing the build
        task.ignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings");
        assertNotNull(task.configuration.warn, "Should also suppress warning messages");
    }

    @Test
    public void testIgnorewarnings_forQuickIterationBuild() throws Exception {
        // Quick iteration builds that ignore warnings temporarily
        task.ignorewarnings();
        task.verbose();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for quick iteration");
        assertTrue(task.configuration.verbose, "Should show what's happening");
    }

    @Test
    public void testIgnorewarnings_forProductionBuild() throws Exception {
        // Production builds might ignore warnings if they're known and acceptable
        task.ignorewarnings();
        task.dontwarn("com.example.thirdparty.**");

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings");
        assertNotNull(task.configuration.warn, "Should suppress specific warnings");
    }

    @Test
    public void testIgnorewarnings_forThirdPartyLibraries() throws Exception {
        // Working with third-party libraries that generate warnings
        task.ignorewarnings();
        task.dontwarn("okhttp3.**");

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings from third-party libs");
    }

    @Test
    public void testIgnorewarnings_forLegacyCodebase() throws Exception {
        // Legacy codebases might have many warnings
        task.ignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings in legacy codebase");
    }

    @Test
    public void testIgnorewarnings_withAndroidDebugBuild() throws Exception {
        // Android debug builds ignoring warnings
        task.android();
        task.ignorewarnings();
        task.dontobfuscate();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for debug build");
        assertTrue(task.configuration.android, "Should be in Android mode");
    }

    @Test
    public void testIgnorewarnings_forDevelopment() throws Exception {
        // Development builds ignoring warnings temporarily
        task.ignorewarnings();
        task.dontobfuscate();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for development");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for development");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testIgnorewarnings_afterManuallySettingConfiguration() throws Exception {
        task.configuration.ignoreWarnings = false;
        task.configuration.optimize = true;

        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be set to true");
        assertTrue(task.configuration.optimize, "Other settings should be preserved");
    }

    @Test
    public void testIgnorewarnings_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testIgnorewarnings_calledFirst() throws Exception {
        // Test calling ignorewarnings before any other configuration
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testIgnorewarnings_calledLast() throws Exception {
        // Test calling ignorewarnings after other configuration
        task.dontoptimize();
        task.keep("public class * { *; }");
        task.android();

        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ========================================
    // Warning Handling Scenarios
    // ========================================

    @Test
    public void testIgnorewarnings_allowsProcessingToComplete() throws Exception {
        // Allows ProGuard to complete processing even with warnings
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "Should allow processing to complete despite warnings");
    }

    @Test
    public void testIgnorewarnings_withKnownAcceptableWarnings() throws Exception {
        // Some warnings are known and acceptable
        task.ignorewarnings();
        task.dontwarn("com.example.accepted.**");

        assertTrue(task.configuration.ignoreWarnings, "Should ignore known acceptable warnings");
    }

    @Test
    public void testIgnorewarnings_forUnresolvedReferences() throws Exception {
        // Handling unresolved reference warnings
        task.ignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore unresolved reference warnings");
    }

    @Test
    public void testIgnorewarnings_forMissingClasses() throws Exception {
        // Handling missing class warnings
        task.ignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore missing class warnings");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testIgnorewarnings_beforeDontoptimize() throws Exception {
        task.ignorewarnings();
        task.dontoptimize();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testIgnorewarnings_afterDontoptimize() throws Exception {
        task.dontoptimize();
        task.ignorewarnings();

        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testIgnorewarnings_betweenOtherCalls() throws Exception {
        task.android();
        task.ignorewarnings();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "android should be set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testIgnorewarnings_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.ignorewarnings();
        task.keepattributes("*Annotation*,Signature");
        task.android();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testIgnorewarnings_forDebugBuild() throws Exception {
        // Debug builds might ignore warnings to speed up iteration
        task.ignorewarnings();
        task.dontobfuscate();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testIgnorewarnings_forReleaseBuild() throws Exception {
        // Release builds with accepted warnings
        task.ignorewarnings();
        task.dontwarn("com.example.thirdparty.**");

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for release");
        assertNotNull(task.configuration.warn, "Should suppress specific warnings");
    }

    @Test
    public void testIgnorewarnings_forLibraryModule() throws Exception {
        // Library modules might have warnings from consumer apps
        task.ignorewarnings();
        task.dontobfuscate();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for library");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for library");
    }

    @Test
    public void testIgnorewarnings_forApplicationModule() throws Exception {
        // Application modules with accepted warnings
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for application");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for application");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testIgnorewarnings_inAndroidReleaseVariant() throws Exception {
        // Android release variant ignoring warnings
        task.android();
        task.ignorewarnings();
        task.dontwarn("android.support.**");

        assertTrue(task.configuration.android, "android should be set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testIgnorewarnings_inAndroidLibraryModule() throws Exception {
        // Android library ignoring warnings
        task.android();
        task.ignorewarnings();

        assertTrue(task.configuration.android, "android should be set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testIgnorewarnings_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build ignoring warnings
        task.android();
        task.ignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.android, "android should be set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testIgnorewarnings_configurationStatePreserved() throws Exception {
        task.dontoptimize();
        task.android();
        task.keep("public class * { *; }");

        boolean optimizeBefore = task.configuration.optimize;
        boolean androidBefore = task.configuration.android;

        task.ignorewarnings();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertEquals(androidBefore, task.configuration.android, "android state should be preserved");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testIgnorewarnings_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.ignorewarnings();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testIgnorewarnings_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.ignorewarnings();

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testIgnorewarnings_doesNotAffectWarningFilters() throws Exception {
        task.dontwarn("com.example.app");

        task.ignorewarnings();

        assertNotNull(task.configuration.warn, "warn filter should remain set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testIgnorewarnings_forContinuousIntegration() throws Exception {
        // CI builds that need to complete despite warnings
        task.ignorewarnings();
        task.verbose();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings in CI");
        assertTrue(task.configuration.verbose, "Should log details in CI");
    }

    @Test
    public void testIgnorewarnings_forIncrementalDevelopment() throws Exception {
        // Incremental development ignoring temporary warnings
        task.ignorewarnings();
        task.dontobfuscate();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings during development");
        assertFalse(task.configuration.obfuscate, "Should not obfuscate during development");
    }

    @Test
    public void testIgnorewarnings_forMigrationProcess() throws Exception {
        // During migration, many warnings might be expected
        task.ignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings during migration");
    }

    @Test
    public void testIgnorewarnings_withSuppressedWarnings() throws Exception {
        // Combining with warning suppression
        task.dontwarn("com.example.**");
        task.ignorewarnings();

        assertNotNull(task.configuration.warn, "Should have warning suppression");
        assertTrue(task.configuration.ignoreWarnings, "Should also ignore any remaining warnings");
    }

    @Test
    public void testIgnorewarnings_forNonBlockingBuild() throws Exception {
        // Ensure warnings don't block the build
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "Should not block build on warnings");
    }

    @Test
    public void testIgnorewarnings_forRuleVerification() throws Exception {
        // Verifying keep rules while ignoring warnings
        task.ignorewarnings();
        task.keep("public class * { *; }");

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for rule verification");
    }

    @Test
    public void testIgnorewarnings_forKotlinProjects() throws Exception {
        // Kotlin projects ignoring warnings
        task.ignorewarnings();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for Kotlin projects");
    }

    @Test
    public void testIgnorewarnings_forMultiModuleProjects() throws Exception {
        // Multi-module projects ignoring warnings
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for multi-module projects");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testIgnorewarnings_configurationNotNull() throws Exception {
        task.ignorewarnings();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testIgnorewarnings_taskStateValid() throws Exception {
        task.ignorewarnings();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testIgnorewarnings_withMinimalConfiguration() throws Exception {
        // Test with only ignorewarnings called
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testIgnorewarnings_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertFalse(task.configuration.shrink, "shrink should be disabled");
    }

    @Test
    public void testIgnorewarnings_semanticMeaning() throws Exception {
        // Verify the semantic meaning: tells ProGuard to continue despite warnings
        task.ignorewarnings();

        assertTrue(task.configuration.ignoreWarnings,
                   "ignorewarnings should enable ignoreWarnings mode (set ignoreWarnings to true)");
    }

    @Test
    public void testIgnorewarnings_changesConfiguration() throws Exception {
        boolean before = task.configuration.ignoreWarnings;

        task.ignorewarnings();
        boolean after = task.configuration.ignoreWarnings;

        assertFalse(before, "ignoreWarnings should initially be false");
        assertTrue(after, "ignoreWarnings should be true after calling ignorewarnings");
        assertNotEquals(before, after, "Configuration should have changed");
    }
}

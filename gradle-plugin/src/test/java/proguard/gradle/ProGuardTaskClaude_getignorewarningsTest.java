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
 * Comprehensive tests for ProGuardTask.getignorewarnings() method.
 * This is a Groovy DSL getter that returns null and calls ignorewarnings()
 * to set configuration.ignoreWarnings = true.
 * When enabled, ProGuard will proceed with processing even if warnings are generated.
 */
public class ProGuardTaskClaude_getignorewarningsTest {

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
    // Tests for getignorewarnings() Method
    // ========================================

    @Test
    public void testGetignorewarnings_returnsNull() throws Exception {
        Object result = task.getignorewarnings();

        assertNull(result, "getignorewarnings should return null for Groovy DSL support");
    }

    @Test
    public void testGetignorewarnings_setsIgnoreWarningsToTrue() throws Exception {
        assertFalse(task.configuration.ignoreWarnings, "ignoreWarnings should initially be false");

        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be set to true");
    }

    @Test
    public void testGetignorewarnings_callsIgnorewarnings() throws Exception {
        // getignorewarnings should call ignorewarnings()
        assertFalse(task.configuration.ignoreWarnings, "ignoreWarnings should initially be false");

        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignorewarnings() should have been called");
    }

    @Test
    public void testGetignorewarnings_isIdempotent() throws Exception {
        task.getignorewarnings();
        boolean firstResult = task.configuration.ignoreWarnings;

        task.getignorewarnings();

        assertEquals(firstResult, task.configuration.ignoreWarnings,
                    "Multiple calls should not change the state");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should remain true");
    }

    @Test
    public void testGetignorewarnings_multipleCalls() throws Exception {
        task.getignorewarnings();
        task.getignorewarnings();
        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should remain true after multiple calls");
    }

    @Test
    public void testGetignorewarnings_alwaysReturnsNull() throws Exception {
        Object result1 = task.getignorewarnings();
        Object result2 = task.getignorewarnings();
        Object result3 = task.getignorewarnings();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetignorewarnings_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getignorewarnings();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetignorewarnings_afterManuallySetToFalse() throws Exception {
        task.configuration.ignoreWarnings = false;

        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be set to true");
    }

    @Test
    public void testGetignorewarnings_whenAlreadyTrue() throws Exception {
        task.configuration.ignoreWarnings = true;

        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should remain true");
    }

    @Test
    public void testGetignorewarnings_supportsGroovyDSL() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        Object result = task.getignorewarnings();

        assertNull(result, "Should return null for Groovy property access");
        assertTrue(task.configuration.ignoreWarnings, "Should enable ignoreWarnings mode");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testGetignorewarnings_withOtherConfigurationSettings() throws Exception {
        task.dontoptimize();
        task.android();

        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.android, "android should remain set");
    }

    @Test
    public void testGetignorewarnings_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getignorewarnings();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetignorewarnings_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testGetignorewarnings_withDontwarn() throws Exception {
        task.dontwarn();
        task.getignorewarnings();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetignorewarnings_withVerbose() throws Exception {
        task.verbose();
        task.getignorewarnings();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testGetignorewarnings_forContinuingDespiteWarnings() throws Exception {
        // Allow build to continue even if ProGuard generates warnings
        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings and continue");
    }

    @Test
    public void testGetignorewarnings_forCIBuild() throws Exception {
        // CI builds might ignore warnings to avoid failing the build
        task.getignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings");
        assertNotNull(task.configuration.warn, "Should also suppress warning messages");
    }

    @Test
    public void testGetignorewarnings_forQuickIterationBuild() throws Exception {
        // Quick iteration builds that ignore warnings temporarily
        task.getignorewarnings();
        task.verbose();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for quick iteration");
        assertTrue(task.configuration.verbose, "Should show what's happening");
    }

    @Test
    public void testGetignorewarnings_forProductionBuild() throws Exception {
        // Production builds might ignore warnings if they're known and acceptable
        task.getignorewarnings();
        task.dontwarn("com.example.thirdparty.**");

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings");
        assertNotNull(task.configuration.warn, "Should suppress specific warnings");
    }

    @Test
    public void testGetignorewarnings_forThirdPartyLibraries() throws Exception {
        // Working with third-party libraries that generate warnings
        task.getignorewarnings();
        task.dontwarn("okhttp3.**");

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings from third-party libs");
    }

    @Test
    public void testGetignorewarnings_forLegacyCodebase() throws Exception {
        // Legacy codebases might have many warnings
        task.getignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings in legacy codebase");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testGetignorewarnings_afterManuallySettingConfiguration() throws Exception {
        task.configuration.ignoreWarnings = false;
        task.configuration.optimize = true;

        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be set to true");
        assertTrue(task.configuration.optimize, "Other settings should be preserved");
    }

    @Test
    public void testGetignorewarnings_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetignorewarnings_calledFirst() throws Exception {
        // Test calling getignorewarnings before any other configuration
        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetignorewarnings_calledLast() throws Exception {
        // Test calling getignorewarnings after other configuration
        task.dontoptimize();
        task.keep("public class * { *; }");
        task.android();

        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ========================================
    // Warning Handling Scenarios
    // ========================================

    @Test
    public void testGetignorewarnings_allowsProcessingToComplete() throws Exception {
        // Allows ProGuard to complete processing even with warnings
        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "Should allow processing to complete despite warnings");
    }

    @Test
    public void testGetignorewarnings_withKnownAcceptableWarnings() throws Exception {
        // Some warnings are known and acceptable
        task.getignorewarnings();
        task.dontwarn("com.example.accepted.**");

        assertTrue(task.configuration.ignoreWarnings, "Should ignore known acceptable warnings");
    }

    @Test
    public void testGetignorewarnings_forUnresolvedReferences() throws Exception {
        // Handling unresolved reference warnings
        task.getignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore unresolved reference warnings");
    }

    @Test
    public void testGetignorewarnings_forMissingClasses() throws Exception {
        // Handling missing class warnings
        task.getignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore missing class warnings");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testGetignorewarnings_beforeDontoptimize() throws Exception {
        task.getignorewarnings();
        task.dontoptimize();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetignorewarnings_afterDontoptimize() throws Exception {
        task.dontoptimize();
        task.getignorewarnings();

        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetignorewarnings_betweenOtherCalls() throws Exception {
        task.android();
        task.getignorewarnings();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "android should be set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testGetignorewarnings_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.getignorewarnings();
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
    public void testGetignorewarnings_forDebugBuild() throws Exception {
        // Debug builds might ignore warnings to speed up iteration
        task.getignorewarnings();
        task.dontobfuscate();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testGetignorewarnings_forReleaseBuild() throws Exception {
        // Release builds with accepted warnings
        task.getignorewarnings();
        task.dontwarn("com.example.thirdparty.**");

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for release");
        assertNotNull(task.configuration.warn, "Should suppress specific warnings");
    }

    @Test
    public void testGetignorewarnings_forLibraryModule() throws Exception {
        // Library modules might have warnings from consumer apps
        task.getignorewarnings();
        task.dontobfuscate();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for library");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for library");
    }

    @Test
    public void testGetignorewarnings_forApplicationModule() throws Exception {
        // Application modules with accepted warnings
        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings for application");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for application");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testGetignorewarnings_inAndroidReleaseVariant() throws Exception {
        // Android release variant ignoring warnings
        task.android();
        task.getignorewarnings();
        task.dontwarn("android.support.**");

        assertTrue(task.configuration.android, "android should be set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetignorewarnings_inAndroidLibraryModule() throws Exception {
        // Android library ignoring warnings
        task.android();
        task.getignorewarnings();

        assertTrue(task.configuration.android, "android should be set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetignorewarnings_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build ignoring warnings
        task.android();
        task.getignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.android, "android should be set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testGetignorewarnings_configurationStatePreserved() throws Exception {
        task.dontoptimize();
        task.android();
        task.keep("public class * { *; }");

        boolean optimizeBefore = task.configuration.optimize;
        boolean androidBefore = task.configuration.android;

        task.getignorewarnings();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertEquals(androidBefore, task.configuration.android, "android state should be preserved");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetignorewarnings_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.getignorewarnings();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetignorewarnings_doesNotClearKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.getignorewarnings();

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    @Test
    public void testGetignorewarnings_doesNotAffectWarningFilters() throws Exception {
        task.dontwarn("com.example.app");

        task.getignorewarnings();

        assertNotNull(task.configuration.warn, "warn filter should remain set");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testGetignorewarnings_forContinuousIntegration() throws Exception {
        // CI builds that need to complete despite warnings
        task.getignorewarnings();
        task.verbose();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings in CI");
        assertTrue(task.configuration.verbose, "Should log details in CI");
    }

    @Test
    public void testGetignorewarnings_forIncrementalDevelopment() throws Exception {
        // Incremental development ignoring temporary warnings
        task.getignorewarnings();
        task.dontobfuscate();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings during development");
        assertFalse(task.configuration.obfuscate, "Should not obfuscate during development");
    }

    @Test
    public void testGetignorewarnings_forMigrationProcess() throws Exception {
        // During migration, many warnings might be expected
        task.getignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.ignoreWarnings, "Should ignore warnings during migration");
    }

    @Test
    public void testGetignorewarnings_withSuppressedWarnings() throws Exception {
        // Combining with warning suppression
        task.dontwarn("com.example.**");
        task.getignorewarnings();

        assertNotNull(task.configuration.warn, "Should have warning suppression");
        assertTrue(task.configuration.ignoreWarnings, "Should also ignore any remaining warnings");
    }

    @Test
    public void testGetignorewarnings_forNonBlockingBuild() throws Exception {
        // Ensure warnings don't block the build
        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "Should not block build on warnings");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testGetignorewarnings_configurationNotNull() throws Exception {
        task.getignorewarnings();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetignorewarnings_returnValueConsistency() throws Exception {
        Object result1 = task.getignorewarnings();
        Object result2 = task.getignorewarnings();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertEquals(result1, result2, "Return values should be consistent (both null)");
    }

    @Test
    public void testGetignorewarnings_taskStateValid() throws Exception {
        task.getignorewarnings();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetignorewarnings_withMinimalConfiguration() throws Exception {
        // Test with only getignorewarnings called
        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetignorewarnings_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertFalse(task.configuration.shrink, "shrink should be disabled");
    }

    @Test
    public void testGetignorewarnings_semanticMeaning() throws Exception {
        // Verify the semantic meaning: tells ProGuard to continue despite warnings
        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings,
                   "getignorewarnings should enable ignoreWarnings mode (set ignoreWarnings to true)");
    }

    @Test
    public void testGetignorewarnings_equivalentToIgnorewarnings() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        // Use getter version
        task1.getignorewarnings();

        // Use direct method
        task2.ignorewarnings();

        assertEquals(task1.configuration.ignoreWarnings, task2.configuration.ignoreWarnings,
                    "Both should have same ignoreWarnings value");
        assertTrue(task1.configuration.ignoreWarnings, "task1 should have ignoreWarnings true");
        assertTrue(task2.configuration.ignoreWarnings, "task2 should have ignoreWarnings true");
    }

    @Test
    public void testGetignorewarnings_canBeMixedWithDirectCalls() throws Exception {
        task.getignorewarnings();
        assertTrue(task.configuration.ignoreWarnings, "After getter call, should be true");

        task.configuration.ignoreWarnings = false;
        task.ignorewarnings();
        assertTrue(task.configuration.ignoreWarnings, "After direct call, should be true again");

        task.configuration.ignoreWarnings = false;
        task.getignorewarnings();
        assertTrue(task.configuration.ignoreWarnings, "After another getter call, should be true again");
    }

    // ========================================
    // Comparison Tests
    // ========================================

    @Test
    public void testGetignorewarnings_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.getignorewarnings();
        task2.getignorewarnings();
        task3.getignorewarnings();

        assertTrue(task1.configuration.ignoreWarnings, "task1 ignoreWarnings should be true");
        assertTrue(task2.configuration.ignoreWarnings, "task2 ignoreWarnings should be true");
        assertTrue(task3.configuration.ignoreWarnings, "task3 ignoreWarnings should be true");
    }

    @Test
    public void testGetignorewarnings_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "task ignoreWarnings should be true");
        assertFalse(otherTask.configuration.ignoreWarnings, "otherTask should not be affected");
    }

    @Test
    public void testGetignorewarnings_afterConfigurationAccess() throws Exception {
        // Access configuration before calling getignorewarnings
        assertNotNull(task.configuration, "Configuration should not be null");
        assertFalse(task.configuration.ignoreWarnings, "ignoreWarnings should initially be false");

        task.getignorewarnings();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
    }
}

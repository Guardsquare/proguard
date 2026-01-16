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
 * Comprehensive tests for ProGuardTask.dontwarn() method.
 * This is a void method that calls dontwarn(null) which initializes configuration.warn
 * to suppress all warning messages during ProGuard processing.
 */
public class ProGuardTaskClaude_dontwarnTest {

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
    // Tests for dontwarn() Method
    // ========================================

    @Test
    public void testDontwarn_initializesWarnFilter() throws Exception {
        assertNull(task.configuration.warn, "warn should initially be null");

        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_isIdempotent() throws Exception {
        task.dontwarn();
        int firstSize = task.configuration.warn.size();

        task.dontwarn();

        assertEquals(firstSize, task.configuration.warn.size(),
                    "Multiple calls should not change the filter size");
        assertNotNull(task.configuration.warn, "warn should remain initialized");
    }

    @Test
    public void testDontwarn_multipleCalls() throws Exception {
        task.dontwarn();
        task.dontwarn();
        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should remain initialized after multiple calls");
    }

    @Test
    public void testDontwarn_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.dontwarn();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_afterManuallySetToNull() throws Exception {
        task.configuration.warn = null;

        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_whenAlreadyInitialized() throws Exception {
        task.dontwarn();
        assertNotNull(task.configuration.warn, "warn should be initialized");

        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should remain initialized");
    }

    @Test
    public void testDontwarn_clearsPreviousFilter() throws Exception {
        // When dontwarn(null) is called, it clears the filter
        task.dontwarn("com.example.*");
        assertNotNull(task.configuration.warn, "warn should be initialized with filter");

        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should still be initialized");
        // After dontwarn(), the filter is cleared (empty list)
        assertTrue(task.configuration.warn.isEmpty(), "warn filter should be empty");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testDontwarn_withOtherConfigurationSettings() throws Exception {
        task.dontoptimize();
        task.android();

        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.android, "android should remain set");
    }

    @Test
    public void testDontwarn_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.dontwarn();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testDontwarn_withDontnote() throws Exception {
        task.dontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_withIgnoreWarnings() throws Exception {
        task.ignorewarnings();
        task.dontwarn();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_withVerbose() throws Exception {
        task.verbose();
        task.dontwarn();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testDontwarn_forCleanBuildOutput() throws Exception {
        // Suppress warning messages for cleaner build output
        task.dontwarn();

        assertNotNull(task.configuration.warn, "Should suppress all warnings");
    }

    @Test
    public void testDontwarn_forProductionBuild() throws Exception {
        // Production builds often suppress warnings and notes
        task.dontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontwarn_inDebugBuild() throws Exception {
        // Debug builds might suppress warnings but keep verbose
        task.dontwarn();
        task.verbose();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontwarn_forCIBuild() throws Exception {
        // CI builds often suppress warnings for cleaner logs
        task.dontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontwarn_forLibraryBuild() throws Exception {
        // Library builds suppressing warnings
        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_withMinimalConfiguration() throws Exception {
        // Minimal ProGuard setup
        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_withMaximalConfiguration() throws Exception {
        // Comprehensive ProGuard setup
        task.dontwarn();
        task.dontnote();
        task.ignorewarnings();
        task.verbose();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testDontwarn_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.dontwarn();
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.dontwarn();
        task2.dontwarn();
        task3.dontwarn();

        assertNotNull(task1.configuration.warn, "task1 warn should be initialized");
        assertNotNull(task2.configuration.warn, "task2 warn should be initialized");
        assertNotNull(task3.configuration.warn, "task3 warn should be initialized");
    }

    @Test
    public void testDontwarn_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.dontwarn();

        assertNotNull(task.configuration.warn, "task warn should be initialized");
        assertNull(otherTask.configuration.warn, "otherTask should not be affected");
    }

    @Test
    public void testDontwarn_afterConfigurationAccess() throws Exception {
        // Access configuration before calling dontwarn
        assertNotNull(task.configuration, "Configuration should not be null");

        task.dontwarn();
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.dontwarn();
        assertNotNull(task.configuration.warn, "Should be initialized after first call");

        task.verbose();
        task.dontwarn();
        assertNotNull(task.configuration.warn, "Should remain initialized after second call");

        task.ignorewarnings();
        task.dontwarn();
        assertNotNull(task.configuration.warn, "Should remain initialized after third call");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testDontwarn_beforeDontoptimize() throws Exception {
        task.dontwarn();
        task.dontoptimize();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testDontwarn_afterDontoptimize() throws Exception {
        task.dontoptimize();
        task.dontwarn();

        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_betweenOtherCalls() throws Exception {
        task.android();
        task.dontwarn();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontwarn_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.dontwarn();
        task.keepattributes("*Annotation*,Signature");
        task.android();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testDontwarn_inAndroidReleaseVariant() throws Exception {
        // Android release variant suppressing warnings
        task.android();
        task.dontwarn();
        task.dontnote();

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontwarn_inAndroidLibraryModule() throws Exception {
        // Android library suppressing warnings
        task.android();
        task.dontwarn();

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build suppressing warnings
        task.android();
        task.dontwarn();
        task.dontnote();

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testDontwarn_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.warn, "Initial state should be null");

        task.dontwarn();

        assertNotNull(task.configuration.warn, "State should be modified");
    }

    @Test
    public void testDontwarn_preservesOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.ignorewarnings();

        task.dontwarn();

        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be preserved");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontwarn_withComplexConfigurationChain() throws Exception {
        task.verbose();
        task.dontwarn();
        task.ignorewarnings();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontwarn_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testDontwarn_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testDontwarn_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ========================================
    // Method Behavior Tests
    // ========================================

    @Test
    public void testDontwarn_callsDontwarnWithNull() throws Exception {
        // dontwarn() calls dontwarn(null), which clears the filter
        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.isEmpty(), "warn filter should be empty (cleared)");
    }

    @Test
    public void testDontwarn_createsEmptyFilterList() throws Exception {
        task.dontwarn();

        assertNotNull(task.configuration.warn, "warn should not be null");
        assertTrue(task.configuration.warn instanceof java.util.List, "warn should be a List");
        assertTrue(task.configuration.warn.isEmpty(), "warn should be an empty list");
    }

    // ========================================
    // Comparison with Parameterized Version
    // ========================================

    @Test
    public void testDontwarn_equivalentToDontwarnNull() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        // Use no-arg version
        task1.dontwarn();

        // Use parameterized version with null
        task2.dontwarn(null);

        assertNotNull(task1.configuration.warn, "task1 should have warn initialized");
        assertNotNull(task2.configuration.warn, "task2 should have warn initialized");
        assertEquals(task1.configuration.warn.size(), task2.configuration.warn.size(),
                    "Both should have the same filter size");
    }

    @Test
    public void testDontwarn_canBeMixedWithParameterizedCalls() throws Exception {
        task.dontwarn();
        assertNotNull(task.configuration.warn, "After no-arg call, should be initialized");
        assertTrue(task.configuration.warn.isEmpty(), "Should be empty");

        task.dontwarn("com.example.*");
        assertNotNull(task.configuration.warn, "After parameterized call, should still be initialized");
        assertFalse(task.configuration.warn.isEmpty(), "Should have filter");

        task.dontwarn();
        assertNotNull(task.configuration.warn, "After another no-arg call, should still be initialized");
        assertTrue(task.configuration.warn.isEmpty(), "Should be cleared again");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testDontwarn_forDebugBuild() throws Exception {
        // Debug builds suppressing warnings
        task.dontwarn();
        task.dontobfuscate();

        assertNotNull(task.configuration.warn, "Should suppress warnings for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testDontwarn_forReleaseBuild() throws Exception {
        // Release builds suppressing warnings
        task.dontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "Should suppress warnings for release");
        assertNotNull(task.configuration.note, "Should suppress notes for release");
    }

    @Test
    public void testDontwarn_forStagingBuild() throws Exception {
        // Staging builds suppressing warnings
        task.dontwarn();
        task.verbose();

        assertNotNull(task.configuration.warn, "Should suppress warnings for staging");
        assertTrue(task.configuration.verbose, "Should be verbose for staging");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testDontwarn_forReducingBuildNoise() throws Exception {
        // Reduce build noise by suppressing warnings
        task.dontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontwarn_forFocusingOnErrors() throws Exception {
        // Focus on errors by suppressing warnings and notes
        task.dontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontwarn_forCleanerLogs() throws Exception {
        // Cleaner logs by suppressing warnings
        task.dontwarn();
        task.verbose();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testDontwarn_configurationNotNull() throws Exception {
        task.dontwarn();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testDontwarn_taskStateValid() throws Exception {
        task.dontwarn();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testDontwarn_semanticMeaning() throws Exception {
        // Verify the semantic meaning: suppresses all warning messages
        task.dontwarn();

        assertNotNull(task.configuration.warn,
                   "dontwarn should initialize warn filter to suppress all warning messages");
        assertTrue(task.configuration.warn.isEmpty(),
                   "dontwarn should clear the filter (suppress all warnings)");
    }

    @Test
    public void testDontwarn_afterManualFilterConfiguration() throws Exception {
        // Manually configure filter then call dontwarn
        task.dontwarn("com.example.*");
        assertFalse(task.configuration.warn.isEmpty(), "Should have filter");

        task.dontwarn();

        assertTrue(task.configuration.warn.isEmpty(), "Should clear previous filter");
    }

    // ========================================
    // Tests for dontwarn(String filter) Method
    // ========================================

    @Test
    public void testDontwarnWithFilter_singleClass() throws Exception {
        task.dontwarn("com.example.MyClass");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertFalse(task.configuration.warn.isEmpty(), "warn should contain filter");
        assertTrue(task.configuration.warn.contains("com/example/MyClass"),
                   "Should contain internal form of class name");
    }

    @Test
    public void testDontwarnWithFilter_wildcardPackage() throws Exception {
        task.dontwarn("com.example.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/**"),
                   "Should contain wildcard pattern");
    }

    @Test
    public void testDontwarnWithFilter_singleWildcard() throws Exception {
        task.dontwarn("com.example.*");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/*"),
                   "Should contain single wildcard pattern");
    }

    @Test
    public void testDontwarnWithFilter_multipleClassesCommaSeparated() throws Exception {
        task.dontwarn("com.example.App,com.example.Lib");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/App"),
                   "Should contain first class");
        assertTrue(task.configuration.warn.contains("com/example/Lib"),
                   "Should contain second class");
    }

    @Test
    public void testDontwarnWithFilter_multipleCalls() throws Exception {
        task.dontwarn("com.example.App");
        task.dontwarn("com.example.Lib");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/App"),
                   "Should contain first class");
        assertTrue(task.configuration.warn.contains("com/example/Lib"),
                   "Should contain second class");
    }

    @Test
    public void testDontwarnWithFilter_emptyString() throws Exception {
        task.dontwarn("");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.isEmpty(), "Empty filter should clear warn list");
    }

    @Test
    public void testDontwarnWithFilter_allClasses() throws Exception {
        task.dontwarn("**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("**"),
                   "Should contain match-all pattern");
    }

    @Test
    public void testDontwarnWithFilter_complexPattern() throws Exception {
        task.dontwarn("com.*.example.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/*/example/**"),
                   "Should contain complex pattern");
    }

    @Test
    public void testDontwarnWithFilter_negation() throws Exception {
        task.dontwarn("!com.example.internal.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("!com/example/internal/**"),
                   "Should contain negation pattern");
    }

    @Test
    public void testDontwarnWithFilter_withOtherConfiguration() throws Exception {
        task.verbose();
        task.dontwarn("com.example.app");

        assertTrue(task.configuration.verbose, "verbose should remain true");
        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/app"),
                   "Should contain filter");
    }

    @Test
    public void testDontwarnWithFilter_afterNoArgCall() throws Exception {
        task.dontwarn();
        assertTrue(task.configuration.warn.isEmpty(), "Should be empty after no-arg call");

        task.dontwarn("com.example.app");

        assertFalse(task.configuration.warn.isEmpty(), "Should have filter after filtered call");
        assertTrue(task.configuration.warn.contains("com/example/app"),
                   "Should contain filter");
    }

    @Test
    public void testDontwarnWithFilter_beforeNoArgCall() throws Exception {
        task.dontwarn("com.example.app");
        assertFalse(task.configuration.warn.isEmpty(), "Should have filter");

        task.dontwarn();

        assertTrue(task.configuration.warn.isEmpty(), "Should be cleared by no-arg call");
    }

    @Test
    public void testDontwarnWithFilter_multipleDifferentFilters() throws Exception {
        task.dontwarn("com.example.app");
        task.dontwarn("org.sample.lib");
        task.dontwarn("net.test.util");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/app"),
                   "Should contain first filter");
        assertTrue(task.configuration.warn.contains("org/sample/lib"),
                   "Should contain second filter");
        assertTrue(task.configuration.warn.contains("net/test/util"),
                   "Should contain third filter");
    }

    @Test
    public void testDontwarnWithFilter_commaAndSpaceSeparated() throws Exception {
        task.dontwarn("com.example.app, com.example.lib");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/app"),
                   "Should contain first class (trimmed)");
        assertTrue(task.configuration.warn.contains("com/example/lib"),
                   "Should contain second class (trimmed)");
    }

    @Test
    public void testDontwarnWithFilter_androidSpecificClasses() throws Exception {
        task.android();
        task.dontwarn("android.support.**");

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("android/support/**"),
                   "Should contain Android support library filter");
    }

    @Test
    public void testDontwarnWithFilter_libraryClasses() throws Exception {
        task.dontwarn("com.example.lib.api.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/lib/api/**"),
                   "Should contain library API filter");
    }

    @Test
    public void testDontwarnWithFilter_multiplePackagePatterns() throws Exception {
        task.dontwarn("com.example.module1.**,com.example.module2.**,com.example.module3.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/module1/**"),
                   "Should contain module1 filter");
        assertTrue(task.configuration.warn.contains("com/example/module2/**"),
                   "Should contain module2 filter");
        assertTrue(task.configuration.warn.contains("com/example/module3/**"),
                   "Should contain module3 filter");
    }

    @Test
    public void testDontwarnWithFilter_withDontnote() throws Exception {
        task.dontwarn("com.example.app");
        task.dontnote("com.example.lib");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/app"),
                   "warn should contain its filter");
        assertTrue(task.configuration.note.contains("com/example/lib"),
                   "note should contain its filter");
    }

    @Test
    public void testDontwarnWithFilter_sdkClasses() throws Exception {
        task.dontwarn("com.example.sdk.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/sdk/**"),
                   "Should contain SDK filter");
    }

    @Test
    public void testDontwarnWithFilter_thirdPartyLibraries() throws Exception {
        task.dontwarn("okhttp3.**,retrofit2.**,com.google.gson.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("okhttp3/**"),
                   "Should contain OkHttp filter");
        assertTrue(task.configuration.warn.contains("retrofit2/**"),
                   "Should contain Retrofit filter");
        assertTrue(task.configuration.warn.contains("com/google/gson/**"),
                   "Should contain Gson filter");
    }

    @Test
    public void testDontwarnWithFilter_longClassName() throws Exception {
        task.dontwarn("com.example.very.long.package.name.hierarchy.level.deep.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/very/long/package/name/hierarchy/level/deep/**"),
                   "Should contain long package name filter");
    }

    @Test
    public void testDontwarnWithFilter_shortClassName() throws Exception {
        task.dontwarn("a.b.C");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("a/b/C"),
                   "Should contain short class name filter");
    }

    @Test
    public void testDontwarnWithFilter_versionedPackage() throws Exception {
        task.dontwarn("com.example.v2.api");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/v2/api"),
                   "Should contain versioned package filter");
    }

    @Test
    public void testDontwarnWithFilter_underscoreInName() throws Exception {
        task.dontwarn("com.example.my_app.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/my_app/**"),
                   "Should contain package name with underscore");
    }

    @Test
    public void testDontwarnWithFilter_sameFilterMultipleTimes() throws Exception {
        task.dontwarn("com.example.app");
        int sizeBefore = task.configuration.warn.size();

        task.dontwarn("com.example.app");

        // Filter should be added again (list allows duplicates)
        assertTrue(task.configuration.warn.size() >= sizeBefore,
                   "Size should not decrease");
    }

    @Test
    public void testDontwarnWithFilter_differentFiltersSequentially() throws Exception {
        task.dontwarn("com.example.app");
        assertTrue(task.configuration.warn.contains("com/example/app"),
                   "Should contain first filter");

        task.dontwarn("com.example.lib");
        assertTrue(task.configuration.warn.contains("com/example/app"),
                   "Should still contain first filter");
        assertTrue(task.configuration.warn.contains("com/example/lib"),
                   "Should contain second filter");
    }

    @Test
    public void testDontwarnWithFilter_clearThenAddAgain() throws Exception {
        task.dontwarn("com.example.app");
        assertFalse(task.configuration.warn.isEmpty(), "Should have filter");

        task.dontwarn();
        assertTrue(task.configuration.warn.isEmpty(), "Should be cleared");

        task.dontwarn("com.example.app");
        assertTrue(task.configuration.warn.contains("com/example/app"),
                   "Should contain filter again");
    }

    @Test
    public void testDontwarnWithFilter_duplicatePatterns() throws Exception {
        task.dontwarn("com.example.myapp.**,com.example.myapp.models.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/myapp/**"),
                   "Should contain parent package filter");
        assertTrue(task.configuration.warn.contains("com/example/myapp/models/**"),
                   "Should contain child package filter");
    }

    @Test
    public void testDontwarnWithFilter_inReleaseVariant() throws Exception {
        // Release variant suppressing warnings for specific packages
        task.dontwarn("com.example.app.debug.**");
        task.dontnote("com.example.app.debug.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontwarnWithFilter_inDebugVariant() throws Exception {
        // Debug variant might suppress warnings for third-party libraries
        task.dontwarn("com.google.android.gms.**");
        task.verbose();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontwarnWithFilter_forLibraryPublicApi() throws Exception {
        // Library keeping some warnings but suppressing others
        task.dontwarn("com.example.library.public.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/library/public/**"),
                   "Should suppress warnings for public API");
    }

    @Test
    public void testDontwarnWithFilter_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.dontwarn("com.example.app");

        assertNotNull(task.configuration.keep, "keep rules should be set");
        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/app"),
                   "Should contain filter");
    }

    @Test
    public void testDontwarnWithFilter_mixedWithNoArgCalls() throws Exception {
        task.dontwarn("com.example.app");
        assertFalse(task.configuration.warn.isEmpty(), "Should have filter");

        task.dontwarn();
        assertTrue(task.configuration.warn.isEmpty(), "Should be cleared");

        task.dontwarn("com.example.lib");
        assertTrue(task.configuration.warn.contains("com/example/lib"),
                   "Should have new filter");
    }

    @Test
    public void testDontwarnWithFilter_redundantPatterns() throws Exception {
        task.dontwarn("com.example.app.**,com.example.app.ui.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        // Both patterns should be present even though one is redundant
        assertTrue(task.configuration.warn.contains("com/example/app/**"),
                   "Should contain broader pattern");
        assertTrue(task.configuration.warn.contains("com/example/app/ui/**"),
                   "Should contain narrower pattern");
    }

    @Test
    public void testDontwarnWithFilter_reflectionRelatedClasses() throws Exception {
        task.dontwarn("com.example.app.reflection.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/app/reflection/**"),
                   "Should suppress warnings for reflection-related classes");
    }

    @Test
    public void testDontwarnWithFilter_dataModelClasses() throws Exception {
        task.dontwarn("com.example.app.model.**");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/app/model/**"),
                   "Should suppress warnings for model classes");
    }

    @Test
    public void testDontwarnWithFilter_preservesOtherSettings() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.android();

        task.dontwarn("com.example.app");

        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertFalse(task.configuration.optimize, "optimize should be preserved");
        assertTrue(task.configuration.android, "android should be preserved");
        assertTrue(task.configuration.warn.contains("com/example/app"),
                   "Should contain filter");
    }

    @Test
    public void testDontwarnWithFilter_nullFilterClearsLikeNoArg() throws Exception {
        task.dontwarn("com.example.app");
        assertFalse(task.configuration.warn.isEmpty(), "Should have filter");

        task.dontwarn(null);

        assertTrue(task.configuration.warn.isEmpty(), "null filter should clear like no-arg version");
    }

    @Test
    public void testDontwarnWithFilter_convertsExternalToInternalFormat() throws Exception {
        // External format uses dots, internal uses slashes
        task.dontwarn("com.example.MyClass");

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.warn.contains("com/example/MyClass"),
                   "Should convert to internal format (slashes)");
        assertFalse(task.configuration.warn.contains("com.example.MyClass"),
                    "Should not contain external format (dots)");
    }

    @Test
    public void testDontwarnWithFilter_semanticMeaning() throws Exception {
        // Verify the semantic meaning: suppresses warning messages for specific classes
        task.dontwarn("com.example.noisylib.**");

        assertNotNull(task.configuration.warn,
                   "dontwarn with filter should initialize warn filter");
        assertTrue(task.configuration.warn.contains("com/example/noisylib/**"),
                   "Should suppress warnings for specified classes");
    }
}

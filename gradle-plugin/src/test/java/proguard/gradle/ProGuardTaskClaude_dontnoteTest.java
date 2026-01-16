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
 * Comprehensive tests for ProGuardTask.dontnote() method.
 * This is a void method that calls dontnote(null) which initializes configuration.note
 * to suppress all note messages during ProGuard processing.
 */
public class ProGuardTaskClaude_dontnoteTest {

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
    // Tests for dontnote() Method
    // ========================================

    @Test
    public void testDontnote_initializesNoteFilter() throws Exception {
        assertNull(task.configuration.note, "note should initially be null");

        task.dontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_isIdempotent() throws Exception {
        task.dontnote();
        int firstSize = task.configuration.note.size();

        task.dontnote();

        assertEquals(firstSize, task.configuration.note.size(),
                    "Multiple calls should not change the filter size");
        assertNotNull(task.configuration.note, "note should remain initialized");
    }

    @Test
    public void testDontnote_multipleCalls() throws Exception {
        task.dontnote();
        task.dontnote();
        task.dontnote();

        assertNotNull(task.configuration.note, "note should remain initialized after multiple calls");
    }

    @Test
    public void testDontnote_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.dontnote();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_afterManuallySetToNull() throws Exception {
        task.configuration.note = null;

        task.dontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_whenAlreadyInitialized() throws Exception {
        task.dontnote();
        assertNotNull(task.configuration.note, "note should be initialized");

        task.dontnote();

        assertNotNull(task.configuration.note, "note should remain initialized");
    }

    @Test
    public void testDontnote_clearsPreviousFilter() throws Exception {
        // When dontnote(null) is called, it clears the filter
        task.dontnote("com.example.*");
        assertNotNull(task.configuration.note, "note should be initialized with filter");

        task.dontnote();

        assertNotNull(task.configuration.note, "note should still be initialized");
        // After dontnote(), the filter is cleared (empty list)
        assertTrue(task.configuration.note.isEmpty(), "note filter should be empty");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testDontnote_withOtherConfigurationSettings() throws Exception {
        task.dontoptimize();
        task.android();

        task.dontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.android, "android should remain set");
    }

    @Test
    public void testDontnote_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.dontnote();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.dontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testDontnote_withDontwarn() throws Exception {
        task.dontwarn();
        task.dontnote();

        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_withIgnoreWarnings() throws Exception {
        task.ignorewarnings();
        task.dontnote();

        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_withVerbose() throws Exception {
        task.verbose();
        task.dontnote();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testDontnote_forCleanBuildOutput() throws Exception {
        // Suppress note messages for cleaner build output
        task.dontnote();

        assertNotNull(task.configuration.note, "Should suppress all notes");
    }

    @Test
    public void testDontnote_forProductionBuild() throws Exception {
        // Production builds often suppress notes and warnings
        task.dontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontnote_inDebugBuild() throws Exception {
        // Debug builds might suppress notes but keep verbose
        task.dontnote();
        task.verbose();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontnote_forCIBuild() throws Exception {
        // CI builds often suppress notes for cleaner logs
        task.dontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontnote_forLibraryBuild() throws Exception {
        // Library builds suppressing notes
        task.dontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_withMinimalConfiguration() throws Exception {
        // Minimal ProGuard setup
        task.dontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_withMaximalConfiguration() throws Exception {
        // Comprehensive ProGuard setup
        task.dontnote();
        task.dontwarn();
        task.ignorewarnings();
        task.verbose();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testDontnote_withNullConfiguration() throws Exception {
        // Configuration should never be null in normal usage
        assertNotNull(task.configuration, "Configuration should not be null");
        task.dontnote();
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.dontnote();
        task2.dontnote();
        task3.dontnote();

        assertNotNull(task1.configuration.note, "task1 note should be initialized");
        assertNotNull(task2.configuration.note, "task2 note should be initialized");
        assertNotNull(task3.configuration.note, "task3 note should be initialized");
    }

    @Test
    public void testDontnote_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.dontnote();

        assertNotNull(task.configuration.note, "task note should be initialized");
        assertNull(otherTask.configuration.note, "otherTask should not be affected");
    }

    @Test
    public void testDontnote_afterConfigurationAccess() throws Exception {
        // Access configuration before calling dontnote
        assertNotNull(task.configuration, "Configuration should not be null");

        task.dontnote();
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_repeatedOverTime() throws Exception {
        // Simulate multiple calls over time
        task.dontnote();
        assertNotNull(task.configuration.note, "Should be initialized after first call");

        task.verbose();
        task.dontnote();
        assertNotNull(task.configuration.note, "Should remain initialized after second call");

        task.ignorewarnings();
        task.dontnote();
        assertNotNull(task.configuration.note, "Should remain initialized after third call");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testDontnote_beforeDontoptimize() throws Exception {
        task.dontnote();
        task.dontoptimize();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testDontnote_afterDontoptimize() throws Exception {
        task.dontoptimize();
        task.dontnote();

        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_betweenOtherCalls() throws Exception {
        task.android();
        task.dontnote();
        task.dontobfuscate();

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.note, "note should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontnote_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.dontnote();
        task.keepattributes("*Annotation*,Signature");
        task.android();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testDontnote_inAndroidReleaseVariant() throws Exception {
        // Android release variant suppressing notes
        task.android();
        task.dontnote();
        task.dontwarn();

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontnote_inAndroidLibraryModule() throws Exception {
        // Android library suppressing notes
        task.android();
        task.dontnote();

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_forAndroidMultiDexBuild() throws Exception {
        // Multi-dex build suppressing notes
        task.android();
        task.dontnote();
        task.dontwarn();

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testDontnote_modifiesConfigurationState() throws Exception {
        assertNull(task.configuration.note, "Initial state should be null");

        task.dontnote();

        assertNotNull(task.configuration.note, "State should be modified");
    }

    @Test
    public void testDontnote_preservesOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.ignorewarnings();

        task.dontnote();

        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be preserved");
        assertNotNull(task.configuration.note, "note should be initialized");
    }

    @Test
    public void testDontnote_withComplexConfigurationChain() throws Exception {
        task.verbose();
        task.dontnote();
        task.ignorewarnings();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.ignoreWarnings, "ignoreWarnings should be true");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontnote_doesNotAffectShrinkSetting() throws Exception {
        // Verify shrink remains at default
        task.dontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    public void testDontnote_doesNotAffectOptimizeSetting() throws Exception {
        // Verify optimize remains at default
        task.dontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    public void testDontnote_doesNotAffectObfuscateSetting() throws Exception {
        // Verify obfuscate remains at default
        task.dontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ========================================
    // Method Behavior Tests
    // ========================================

    @Test
    public void testDontnote_callsDontnoteWithNull() throws Exception {
        // dontnote() calls dontnote(null), which clears the filter
        task.dontnote();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.isEmpty(), "note filter should be empty (cleared)");
    }

    @Test
    public void testDontnote_createsEmptyFilterList() throws Exception {
        task.dontnote();

        assertNotNull(task.configuration.note, "note should not be null");
        assertTrue(task.configuration.note instanceof java.util.List, "note should be a List");
        assertTrue(task.configuration.note.isEmpty(), "note should be an empty list");
    }

    // ========================================
    // Comparison with Parameterized Version
    // ========================================

    @Test
    public void testDontnote_equivalentToDontnoteNull() throws Exception {
        ProGuardTask task1 = project.getTasks().create("task1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("task2", ProGuardTask.class);

        // Use no-arg version
        task1.dontnote();

        // Use parameterized version with null
        task2.dontnote(null);

        assertNotNull(task1.configuration.note, "task1 should have note initialized");
        assertNotNull(task2.configuration.note, "task2 should have note initialized");
        assertEquals(task1.configuration.note.size(), task2.configuration.note.size(),
                    "Both should have the same filter size");
    }

    @Test
    public void testDontnote_canBeMixedWithParameterizedCalls() throws Exception {
        task.dontnote();
        assertNotNull(task.configuration.note, "After no-arg call, should be initialized");
        assertTrue(task.configuration.note.isEmpty(), "Should be empty");

        task.dontnote("com.example.*");
        assertNotNull(task.configuration.note, "After parameterized call, should still be initialized");
        assertFalse(task.configuration.note.isEmpty(), "Should have filter");

        task.dontnote();
        assertNotNull(task.configuration.note, "After another no-arg call, should still be initialized");
        assertTrue(task.configuration.note.isEmpty(), "Should be cleared again");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testDontnote_forDebugBuild() throws Exception {
        // Debug builds suppressing notes
        task.dontnote();
        task.dontobfuscate();

        assertNotNull(task.configuration.note, "Should suppress notes for debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testDontnote_forReleaseBuild() throws Exception {
        // Release builds suppressing notes
        task.dontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "Should suppress notes for release");
        assertNotNull(task.configuration.warn, "Should suppress warnings for release");
    }

    @Test
    public void testDontnote_forStagingBuild() throws Exception {
        // Staging builds suppressing notes
        task.dontnote();
        task.verbose();

        assertNotNull(task.configuration.note, "Should suppress notes for staging");
        assertTrue(task.configuration.verbose, "Should be verbose for staging");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testDontnote_forReducingBuildNoise() throws Exception {
        // Reduce build noise by suppressing notes
        task.dontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontnote_forFocusingOnErrors() throws Exception {
        // Focus on errors by suppressing notes and warnings
        task.dontnote();
        task.dontwarn();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontnote_forCleanerLogs() throws Exception {
        // Cleaner logs by suppressing notes
        task.dontnote();
        task.verbose();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testDontnote_configurationNotNull() throws Exception {
        task.dontnote();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testDontnote_taskStateValid() throws Exception {
        task.dontnote();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testDontnote_semanticMeaning() throws Exception {
        // Verify the semantic meaning: suppresses all note messages
        task.dontnote();

        assertNotNull(task.configuration.note,
                   "dontnote should initialize note filter to suppress all note messages");
        assertTrue(task.configuration.note.isEmpty(),
                   "dontnote should clear the filter (suppress all notes)");
    }

    @Test
    public void testDontnote_afterManualFilterConfiguration() throws Exception {
        // Manually configure filter then call dontnote
        task.dontnote("com.example.*");
        assertFalse(task.configuration.note.isEmpty(), "Should have filter");

        task.dontnote();

        assertTrue(task.configuration.note.isEmpty(), "Should clear previous filter");
    }

    // ========================================
    // Tests for dontnote(String filter) Method
    // ========================================

    @Test
    public void testDontnoteWithFilter_singleClass() throws Exception {
        task.dontnote("com.example.MyClass");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertFalse(task.configuration.note.isEmpty(), "note should contain filter");
        assertTrue(task.configuration.note.contains("com/example/MyClass"),
                   "Should contain internal form of class name");
    }

    @Test
    public void testDontnoteWithFilter_wildcardPackage() throws Exception {
        task.dontnote("com.example.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/**"),
                   "Should contain wildcard pattern");
    }

    @Test
    public void testDontnoteWithFilter_singleWildcard() throws Exception {
        task.dontnote("com.example.*");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/*"),
                   "Should contain single wildcard pattern");
    }

    @Test
    public void testDontnoteWithFilter_multipleClassesCommaSeparated() throws Exception {
        task.dontnote("com.example.App,com.example.Lib");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/App"),
                   "Should contain first class");
        assertTrue(task.configuration.note.contains("com/example/Lib"),
                   "Should contain second class");
    }

    @Test
    public void testDontnoteWithFilter_multipleCalls() throws Exception {
        task.dontnote("com.example.App");
        task.dontnote("com.example.Lib");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/App"),
                   "Should contain first class");
        assertTrue(task.configuration.note.contains("com/example/Lib"),
                   "Should contain second class");
    }

    @Test
    public void testDontnoteWithFilter_emptyString() throws Exception {
        task.dontnote("");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.isEmpty(), "Empty filter should clear note list");
    }

    @Test
    public void testDontnoteWithFilter_allClasses() throws Exception {
        task.dontnote("**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("**"),
                   "Should contain match-all pattern");
    }

    @Test
    public void testDontnoteWithFilter_complexPattern() throws Exception {
        task.dontnote("com.*.example.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/*/example/**"),
                   "Should contain complex pattern");
    }

    @Test
    public void testDontnoteWithFilter_negation() throws Exception {
        task.dontnote("!com.example.internal.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("!com/example/internal/**"),
                   "Should contain negation pattern");
    }

    @Test
    public void testDontnoteWithFilter_withOtherConfiguration() throws Exception {
        task.verbose();
        task.dontnote("com.example.app");

        assertTrue(task.configuration.verbose, "verbose should remain true");
        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/app"),
                   "Should contain filter");
    }

    @Test
    public void testDontnoteWithFilter_afterNoArgCall() throws Exception {
        task.dontnote();
        assertTrue(task.configuration.note.isEmpty(), "Should be empty after no-arg call");

        task.dontnote("com.example.app");

        assertFalse(task.configuration.note.isEmpty(), "Should have filter after filtered call");
        assertTrue(task.configuration.note.contains("com/example/app"),
                   "Should contain filter");
    }

    @Test
    public void testDontnoteWithFilter_beforeNoArgCall() throws Exception {
        task.dontnote("com.example.app");
        assertFalse(task.configuration.note.isEmpty(), "Should have filter");

        task.dontnote();

        assertTrue(task.configuration.note.isEmpty(), "Should be cleared by no-arg call");
    }

    @Test
    public void testDontnoteWithFilter_multipleDifferentFilters() throws Exception {
        task.dontnote("com.example.app");
        task.dontnote("org.sample.lib");
        task.dontnote("net.test.util");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/app"),
                   "Should contain first filter");
        assertTrue(task.configuration.note.contains("org/sample/lib"),
                   "Should contain second filter");
        assertTrue(task.configuration.note.contains("net/test/util"),
                   "Should contain third filter");
    }

    @Test
    public void testDontnoteWithFilter_commaAndSpaceSeparated() throws Exception {
        task.dontnote("com.example.app, com.example.lib");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/app"),
                   "Should contain first class (trimmed)");
        assertTrue(task.configuration.note.contains("com/example/lib"),
                   "Should contain second class (trimmed)");
    }

    @Test
    public void testDontnoteWithFilter_androidSpecificClasses() throws Exception {
        task.android();
        task.dontnote("android.support.**");

        assertTrue(task.configuration.android, "android should be set");
        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("android/support/**"),
                   "Should contain Android support library filter");
    }

    @Test
    public void testDontnoteWithFilter_libraryClasses() throws Exception {
        task.dontnote("com.example.lib.api.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/lib/api/**"),
                   "Should contain library API filter");
    }

    @Test
    public void testDontnoteWithFilter_multiplePackagePatterns() throws Exception {
        task.dontnote("com.example.module1.**,com.example.module2.**,com.example.module3.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/module1/**"),
                   "Should contain module1 filter");
        assertTrue(task.configuration.note.contains("com/example/module2/**"),
                   "Should contain module2 filter");
        assertTrue(task.configuration.note.contains("com/example/module3/**"),
                   "Should contain module3 filter");
    }

    @Test
    public void testDontnoteWithFilter_withDontwarn() throws Exception {
        task.dontnote("com.example.app");
        task.dontwarn("com.example.lib");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
        assertTrue(task.configuration.note.contains("com/example/app"),
                   "note should contain its filter");
        assertTrue(task.configuration.warn.contains("com/example/lib"),
                   "warn should contain its filter");
    }

    @Test
    public void testDontnoteWithFilter_sdkClasses() throws Exception {
        task.dontnote("com.example.sdk.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/sdk/**"),
                   "Should contain SDK filter");
    }

    @Test
    public void testDontnoteWithFilter_thirdPartyLibraries() throws Exception {
        task.dontnote("okhttp3.**,retrofit2.**,com.google.gson.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("okhttp3/**"),
                   "Should contain OkHttp filter");
        assertTrue(task.configuration.note.contains("retrofit2/**"),
                   "Should contain Retrofit filter");
        assertTrue(task.configuration.note.contains("com/google/gson/**"),
                   "Should contain Gson filter");
    }

    @Test
    public void testDontnoteWithFilter_longClassName() throws Exception {
        task.dontnote("com.example.very.long.package.name.hierarchy.level.deep.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/very/long/package/name/hierarchy/level/deep/**"),
                   "Should contain long package name filter");
    }

    @Test
    public void testDontnoteWithFilter_shortClassName() throws Exception {
        task.dontnote("a.b.C");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("a/b/C"),
                   "Should contain short class name filter");
    }

    @Test
    public void testDontnoteWithFilter_versionedPackage() throws Exception {
        task.dontnote("com.example.v2.api");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/v2/api"),
                   "Should contain versioned package filter");
    }

    @Test
    public void testDontnoteWithFilter_underscoreInName() throws Exception {
        task.dontnote("com.example.my_app.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/my_app/**"),
                   "Should contain package name with underscore");
    }

    @Test
    public void testDontnoteWithFilter_sameFilterMultipleTimes() throws Exception {
        task.dontnote("com.example.app");
        int sizeBefore = task.configuration.note.size();

        task.dontnote("com.example.app");

        // Filter should be added again (list allows duplicates)
        assertTrue(task.configuration.note.size() >= sizeBefore,
                   "Size should not decrease");
    }

    @Test
    public void testDontnoteWithFilter_differentFiltersSequentially() throws Exception {
        task.dontnote("com.example.app");
        assertTrue(task.configuration.note.contains("com/example/app"),
                   "Should contain first filter");

        task.dontnote("com.example.lib");
        assertTrue(task.configuration.note.contains("com/example/app"),
                   "Should still contain first filter");
        assertTrue(task.configuration.note.contains("com/example/lib"),
                   "Should contain second filter");
    }

    @Test
    public void testDontnoteWithFilter_clearThenAddAgain() throws Exception {
        task.dontnote("com.example.app");
        assertFalse(task.configuration.note.isEmpty(), "Should have filter");

        task.dontnote();
        assertTrue(task.configuration.note.isEmpty(), "Should be cleared");

        task.dontnote("com.example.app");
        assertTrue(task.configuration.note.contains("com/example/app"),
                   "Should contain filter again");
    }

    @Test
    public void testDontnoteWithFilter_duplicatePatterns() throws Exception {
        task.dontnote("com.example.myapp.**,com.example.myapp.models.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/myapp/**"),
                   "Should contain parent package filter");
        assertTrue(task.configuration.note.contains("com/example/myapp/models/**"),
                   "Should contain child package filter");
    }

    @Test
    public void testDontnoteWithFilter_inReleaseVariant() throws Exception {
        // Release variant suppressing notes for specific packages
        task.dontnote("com.example.app.debug.**");
        task.dontwarn("com.example.app.debug.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertNotNull(task.configuration.warn, "warn should be initialized");
    }

    @Test
    public void testDontnoteWithFilter_inDebugVariant() throws Exception {
        // Debug variant might suppress notes for third-party libraries
        task.dontnote("com.google.android.gms.**");
        task.verbose();

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be true");
    }

    @Test
    public void testDontnoteWithFilter_forLibraryPublicApi() throws Exception {
        // Library keeping some notes but suppressing others
        task.dontnote("com.example.library.public.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/library/public/**"),
                   "Should suppress notes for public API");
    }

    @Test
    public void testDontnoteWithFilter_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.dontnote("com.example.app");

        assertNotNull(task.configuration.keep, "keep rules should be set");
        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/app"),
                   "Should contain filter");
    }

    @Test
    public void testDontnoteWithFilter_mixedWithNoArgCalls() throws Exception {
        task.dontnote("com.example.app");
        assertFalse(task.configuration.note.isEmpty(), "Should have filter");

        task.dontnote();
        assertTrue(task.configuration.note.isEmpty(), "Should be cleared");

        task.dontnote("com.example.lib");
        assertTrue(task.configuration.note.contains("com/example/lib"),
                   "Should have new filter");
    }

    @Test
    public void testDontnoteWithFilter_redundantPatterns() throws Exception {
        task.dontnote("com.example.app.**,com.example.app.ui.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        // Both patterns should be present even though one is redundant
        assertTrue(task.configuration.note.contains("com/example/app/**"),
                   "Should contain broader pattern");
        assertTrue(task.configuration.note.contains("com/example/app/ui/**"),
                   "Should contain narrower pattern");
    }

    @Test
    public void testDontnoteWithFilter_reflectionRelatedClasses() throws Exception {
        task.dontnote("com.example.app.reflection.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/app/reflection/**"),
                   "Should suppress notes for reflection-related classes");
    }

    @Test
    public void testDontnoteWithFilter_dataModelClasses() throws Exception {
        task.dontnote("com.example.app.model.**");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/app/model/**"),
                   "Should suppress notes for model classes");
    }

    @Test
    public void testDontnoteWithFilter_preservesOtherSettings() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.android();

        task.dontnote("com.example.app");

        assertTrue(task.configuration.verbose, "verbose should be preserved");
        assertFalse(task.configuration.optimize, "optimize should be preserved");
        assertTrue(task.configuration.android, "android should be preserved");
        assertTrue(task.configuration.note.contains("com/example/app"),
                   "Should contain filter");
    }

    @Test
    public void testDontnoteWithFilter_nullFilterClearsLikeNoArg() throws Exception {
        task.dontnote("com.example.app");
        assertFalse(task.configuration.note.isEmpty(), "Should have filter");

        task.dontnote(null);

        assertTrue(task.configuration.note.isEmpty(), "null filter should clear like no-arg version");
    }

    @Test
    public void testDontnoteWithFilter_convertsExternalToInternalFormat() throws Exception {
        // External format uses dots, internal uses slashes
        task.dontnote("com.example.MyClass");

        assertNotNull(task.configuration.note, "note should be initialized");
        assertTrue(task.configuration.note.contains("com/example/MyClass"),
                   "Should convert to internal format (slashes)");
        assertFalse(task.configuration.note.contains("com.example.MyClass"),
                    "Should not contain external format (dots)");
    }

    @Test
    public void testDontnoteWithFilter_semanticMeaning() throws Exception {
        // Verify the semantic meaning: suppresses note messages for specific classes
        task.dontnote("com.example.noisylib.**");

        assertNotNull(task.configuration.note,
                   "dontnote with filter should initialize note filter");
        assertTrue(task.configuration.note.contains("com/example/noisylib/**"),
                   "Should suppress notes for specified classes");
    }
}

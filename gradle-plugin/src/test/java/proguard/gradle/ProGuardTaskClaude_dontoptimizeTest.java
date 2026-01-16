package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.dontoptimize()V method.
 * Tests the dontoptimize() void method that sets configuration.optimize to false,
 * disabling ProGuard's optimization phase.
 */
public class ProGuardTaskClaude_dontoptimizeTest {

    @TempDir
    Path tempDir;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("proguard", ProGuardTask.class);
    }

    @AfterEach
    public void tearDown() {
        task = null;
        project = null;
    }

    // Basic functionality tests

    @Test
    public void testDontoptimize_setsOptimizeToFalse() throws Exception {
        assertTrue(task.configuration.optimize, "optimize should initially be true");
        task.dontoptimize();
        assertFalse(task.configuration.optimize, "optimize should be set to false");
    }

    @Test
    public void testDontoptimize_disablesOptimization() throws Exception {
        assertTrue(task.configuration.optimize, "optimize should be enabled by default");
        task.dontoptimize();
        assertFalse(task.configuration.optimize, "optimization should be disabled");
    }

    @Test
    public void testDontoptimize_doesNotReturnValue() throws Exception {
        // Verify method is void and completes successfully
        task.dontoptimize();
        assertFalse(task.configuration.optimize, "optimize should be false after call");
    }

    // Multiple calls tests

    @Test
    public void testDontoptimize_multipleCallsAreIdempotent() throws Exception {
        task.dontoptimize();
        assertFalse(task.configuration.optimize, "First call should set optimize to false");

        task.dontoptimize();
        assertFalse(task.configuration.optimize, "Second call should keep optimize as false");

        task.dontoptimize();
        assertFalse(task.configuration.optimize, "Third call should keep optimize as false");
    }

    @Test
    public void testDontoptimize_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.dontoptimize();
            assertFalse(task.configuration.optimize, "optimize should be false after call " + (i + 1));
        }
    }

    // Integration with other configuration methods

    @Test
    public void testDontoptimize_worksWithOtherDontMethods() throws Exception {
        task.dontoptimize();
        task.dontshrink();
        task.dontobfuscate();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testDontoptimize_worksWithDontShrink() throws Exception {
        task.dontshrink();
        assertTrue(task.configuration.optimize, "optimize should still be true");

        task.dontoptimize();
        assertFalse(task.configuration.optimize, "optimize should now be false");
        assertFalse(task.configuration.shrink, "shrink should remain false");
    }

    @Test
    public void testDontoptimize_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.dontoptimize();

        assertNotNull(task.configuration.keep, "keep rules should be configured");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testDontoptimize_worksWithPrintOptions() throws Exception {
        task.printusage("usage.txt");
        task.printseeds("seeds.txt");
        task.dontoptimize();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertEquals("usage.txt", task.configuration.printUsage.getName());
        assertEquals("seeds.txt", task.configuration.printSeeds.getName());
    }

    @Test
    public void testDontoptimize_worksWithVerbose() throws Exception {
        task.verbose();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    // Call order flexibility

    @Test
    public void testDontoptimize_canBeCalledFirst() throws Exception {
        task.dontoptimize();
        task.keep("public class * { public *; }");
        task.dontshrink();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testDontoptimize_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.dontshrink();
        task.dontoptimize();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testDontoptimize_canBeCalledBetweenOtherMethods() throws Exception {
        task.keep("public class * { public *; }");
        task.dontoptimize();
        task.dontshrink();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    // Realistic usage scenarios

    @Test
    public void testDontoptimize_androidDebugBuild() throws Exception {
        // Debug builds typically disable optimization for faster builds
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.dontoptimize();
        task.keep("class com.example.MainActivity { *; }");

        assertFalse(task.configuration.optimize, "optimize should be disabled for debug build");
    }

    @Test
    public void testDontoptimize_testingScenario() throws Exception {
        // Testing scenario where optimization is disabled for easier debugging
        task.dontoptimize();
        task.keep("class com.example.** { *; }");
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");

        assertFalse(task.configuration.optimize, "optimize should be disabled for testing");
    }

    @Test
    public void testDontoptimize_onlyObfuscationScenario() throws Exception {
        // Scenario where only obfuscation is needed, not optimization
        task.dontshrink();
        task.dontoptimize();
        // obfuscation is enabled by default

        assertFalse(task.configuration.shrink, "shrink should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.obfuscate, "obfuscate should still be enabled");
    }

    @Test
    public void testDontoptimize_analysisOnlyMode() throws Exception {
        // Mode where we only want to analyze, not process
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.dontpreverify();
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");

        assertFalse(task.configuration.shrink, "shrink should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontoptimize_libraryConfiguration() throws Exception {
        // Library configuration where optimization might cause issues
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-processed.jar");
        task.dontoptimize();
        task.keep("public class * { public *; }");

        assertFalse(task.configuration.optimize, "optimize should be disabled for library");
    }

    @Test
    public void testDontoptimize_quickBuildConfiguration() throws Exception {
        // Quick build for development with minimal processing
        task.injars("build/classes");
        task.outjars("build/outputs");
        task.dontshrink();
        task.dontoptimize();
        task.dontpreverify();

        assertFalse(task.configuration.shrink, "shrink should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertFalse(task.configuration.preverify, "preverify should be disabled");
    }

    // Edge cases and validation

    @Test
    public void testDontoptimize_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.dontoptimize();

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testDontoptimize_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.dontoptimize();
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testDontoptimize_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertTrue(freshTask.configuration.optimize, "Fresh task optimize should be true");

        freshTask.dontoptimize();

        assertFalse(freshTask.configuration.optimize, "Fresh task optimize should be false");
    }

    @Test
    public void testDontoptimize_afterManualOptimizeFalse() throws Exception {
        task.configuration.optimize = false;
        assertFalse(task.configuration.optimize, "optimize should be false");

        task.dontoptimize();

        assertFalse(task.configuration.optimize, "optimize should remain false");
    }

    @Test
    public void testDontoptimize_equivalentToGetdontoptimizeMethod() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard3", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard4", ProGuardTask.class);

        // Using dontoptimize()
        task1.dontoptimize();

        // Using getdontoptimize() (Groovy DSL method)
        task2.getdontoptimize();

        assertEquals(task1.configuration.optimize, task2.configuration.optimize,
                "Both methods should have same effect on optimize setting");
    }

    // Interaction with optimization settings

    @Test
    public void testDontoptimize_disablesOptimizationPass() throws Exception {
        assertTrue(task.configuration.optimize, "optimize should be enabled by default");
        task.dontoptimize();
        assertFalse(task.configuration.optimize, "optimize should be disabled after dontoptimize()");
    }

    @Test
    public void testDontoptimize_withKeepRules() throws Exception {
        task.dontoptimize();
        task.keep("class com.example.MyClass { *; }");

        assertFalse(task.configuration.optimize,
                "optimize should be false even with keep rules");
    }

    @Test
    public void testDontoptimize_complexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.dontoptimize();
        task.printseeds("seeds.txt");
        task.verbose();

        assertFalse(task.configuration.optimize, "optimize should be false in complex config");
    }

    // Multiple dont* methods

    @Test
    public void testDontoptimize_withAllDontMethods() throws Exception {
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.dontpreverify();

        assertFalse(task.configuration.shrink, "shrink should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testDontoptimize_doesNotReenableOptimization() throws Exception {
        task.dontoptimize();
        assertFalse(task.configuration.optimize, "optimize should be false");

        // Call other methods
        task.keep("class com.example.MyClass { *; }");
        task.dontshrink();

        // Verify optimize is still false
        assertFalse(task.configuration.optimize, "optimize should still be false");
    }

    // Android-specific scenarios

    @Test
    public void testDontoptimize_androidReleaseWithoutOptimization() throws Exception {
        // Release build that disables optimization but keeps shrinking and obfuscation
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.dontoptimize();
        task.keep("class com.example.MainActivity { *; }");
        task.printmapping("mapping.txt");

        assertTrue(task.configuration.shrink, "shrink should be enabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.obfuscate, "obfuscate should be enabled");
    }

    @Test
    public void testDontoptimize_androidMultiDexScenario() throws Exception {
        // Multi-dex scenario where optimization is disabled to avoid issues
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.dontoptimize();
        task.keep("class android.support.multidex.** { *; }");

        assertFalse(task.configuration.optimize,
                "optimize should be disabled for multi-dex");
    }

    // Performance and build time scenarios

    @Test
    public void testDontoptimize_fastIncrementalBuild() throws Exception {
        // Fast incremental build where optimization is skipped
        task.injars("build/classes");
        task.outjars("build/outputs");
        task.dontoptimize();
        task.dontpreverify();

        assertFalse(task.configuration.optimize, "optimize should be disabled for fast builds");
        assertFalse(task.configuration.preverify, "preverify should be disabled for fast builds");
    }

    @Test
    public void testDontoptimize_debuggingFriendlyConfiguration() throws Exception {
        // Configuration that preserves code structure for debugging
        task.dontoptimize();
        task.keepattributes("LineNumberTable,SourceFile");
        task.keep("class com.example.** { *; }");

        assertFalse(task.configuration.optimize,
                "optimize should be disabled for debugging");
    }

    // Compatibility scenarios

    @Test
    public void testDontoptimize_compatibilityMode() throws Exception {
        // Compatibility mode where optimization might break reflection or serialization
        task.dontoptimize();
        task.keep("class * implements java.io.Serializable { *; }");
        task.keepclassmembers("class * { private <fields>; }");

        assertFalse(task.configuration.optimize,
                "optimize should be disabled for compatibility");
    }
}

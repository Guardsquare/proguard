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
 * Test class for ProGuardTask.optimizationpasses(int)V method.
 * Tests the optimizationpasses(int) method that sets configuration.optimizationPasses,
 * controlling how many optimization passes ProGuard performs.
 */
public class ProGuardTaskClaude_optimizationpassesTest {

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
    public void testOptimizationpasses_setsValue() throws Exception {
        int initialValue = task.configuration.optimizationPasses;
        task.optimizationpasses(5);
        assertEquals(5, task.configuration.optimizationPasses,
                "optimizationPasses should be set to 5");
        assertNotEquals(initialValue, task.configuration.optimizationPasses,
                "optimizationPasses should be different from initial value");
    }

    @Test
    public void testOptimizationpasses_singlePass() throws Exception {
        task.optimizationpasses(1);
        assertEquals(1, task.configuration.optimizationPasses,
                "optimizationPasses should be set to 1");
    }

    @Test
    public void testOptimizationpasses_defaultValue() throws Exception {
        task.optimizationpasses(5);
        assertEquals(5, task.configuration.optimizationPasses,
                "optimizationPasses should be set to 5 (typical default)");
    }

    @Test
    public void testOptimizationpasses_sevenPasses() throws Exception {
        task.optimizationpasses(7);
        assertEquals(7, task.configuration.optimizationPasses,
                "optimizationPasses should be set to 7");
    }

    // Different pass counts

    @Test
    public void testOptimizationpasses_threePasses() throws Exception {
        task.optimizationpasses(3);
        assertEquals(3, task.configuration.optimizationPasses);
    }

    @Test
    public void testOptimizationpasses_tenPasses() throws Exception {
        task.optimizationpasses(10);
        assertEquals(10, task.configuration.optimizationPasses);
    }

    @Test
    public void testOptimizationpasses_zeroPasses() throws Exception {
        task.optimizationpasses(0);
        assertEquals(0, task.configuration.optimizationPasses,
                "optimizationPasses should be set to 0 (disables optimization)");
    }

    // Multiple calls tests

    @Test
    public void testOptimizationpasses_canBeCalledMultipleTimes() throws Exception {
        task.optimizationpasses(3);
        assertEquals(3, task.configuration.optimizationPasses);

        task.optimizationpasses(5);
        assertEquals(5, task.configuration.optimizationPasses,
                "Second call should overwrite first value");

        task.optimizationpasses(7);
        assertEquals(7, task.configuration.optimizationPasses,
                "Third call should overwrite second value");
    }

    @Test
    public void testOptimizationpasses_overwritesPreviousValue() throws Exception {
        task.optimizationpasses(1);
        assertEquals(1, task.configuration.optimizationPasses);

        task.optimizationpasses(10);
        assertEquals(10, task.configuration.optimizationPasses,
                "Should overwrite previous value");
    }

    // Integration with other configuration methods

    @Test
    public void testOptimizationpasses_worksWithOptimizations() throws Exception {
        task.optimizationpasses(5);
        task.optimizations("code/removal/*");

        assertEquals(5, task.configuration.optimizationPasses);
        assertNotNull(task.configuration.optimizations);
    }

    @Test
    public void testOptimizationpasses_worksWithDontOptimize() throws Exception {
        task.optimizationpasses(5);
        task.dontoptimize();

        assertEquals(5, task.configuration.optimizationPasses,
                "optimizationPasses value should remain even if optimization is disabled");
        assertFalse(task.configuration.optimize,
                "optimize should be false");
    }

    @Test
    public void testOptimizationpasses_worksWithKeep() throws Exception {
        task.optimizationpasses(5);
        task.keep("class com.example.MyClass { *; }");

        assertEquals(5, task.configuration.optimizationPasses);
        assertNotNull(task.configuration.keep);
    }

    @Test
    public void testOptimizationpasses_worksWithPrintOptions() throws Exception {
        task.optimizationpasses(5);
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");

        assertEquals(5, task.configuration.optimizationPasses);
    }

    @Test
    public void testOptimizationpasses_worksWithVerbose() throws Exception {
        task.optimizationpasses(5);
        task.verbose();

        assertEquals(5, task.configuration.optimizationPasses);
        assertTrue(task.configuration.verbose);
    }

    // Call order flexibility

    @Test
    public void testOptimizationpasses_canBeCalledFirst() throws Exception {
        task.optimizationpasses(5);
        task.keep("public class * { public *; }");
        task.optimizations("code/removal/*");

        assertEquals(5, task.configuration.optimizationPasses);
    }

    @Test
    public void testOptimizationpasses_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizations("code/removal/*");
        task.optimizationpasses(5);

        assertEquals(5, task.configuration.optimizationPasses);
    }

    @Test
    public void testOptimizationpasses_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);
        task.optimizations("code/removal/*");

        assertEquals(5, task.configuration.optimizationPasses);
    }

    // Realistic usage scenarios

    @Test
    public void testOptimizationpasses_fastBuildConfiguration() throws Exception {
        // Fast build: single pass or minimal passes
        task.injars("build/classes");
        task.outjars("build/outputs");
        task.optimizationpasses(1);
        task.keep("class com.example.** { *; }");

        assertEquals(1, task.configuration.optimizationPasses,
                "Fast build should use 1 pass");
    }

    @Test
    public void testOptimizationpasses_releaseBuildConfiguration() throws Exception {
        // Release build: multiple passes for maximum optimization
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");
        task.optimizationpasses(7);
        task.optimizations("code/*,field/*,method/*,class/*");
        task.keep("public class * { public *; }");

        assertEquals(7, task.configuration.optimizationPasses,
                "Release build should use 7 passes for maximum optimization");
    }

    @Test
    public void testOptimizationpasses_androidReleaseOptimization() throws Exception {
        // Typical Android release build
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.optimizationpasses(5);
        task.optimizations("!code/simplification/arithmetic");
        task.keep("class com.example.MainActivity { *; }");

        assertEquals(5, task.configuration.optimizationPasses,
                "Android release should use 5 passes");
    }

    @Test
    public void testOptimizationpasses_libraryOptimization() throws Exception {
        // Library optimization with moderate passes
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-optimized.jar");
        task.optimizationpasses(3);
        task.keep("public class * { public *; }");

        assertEquals(3, task.configuration.optimizationPasses,
                "Library should use moderate 3 passes");
    }

    @Test
    public void testOptimizationpasses_disableOptimizationWithZero() throws Exception {
        // Disable optimization by setting passes to 0
        task.optimizationpasses(0);

        assertEquals(0, task.configuration.optimizationPasses,
                "Zero passes effectively disables optimization");
    }

    @Test
    public void testOptimizationpasses_aggressiveOptimization() throws Exception {
        // Aggressive optimization for size-critical builds
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-minimal.jar");
        task.optimizationpasses(10);
        task.optimizations("*");
        task.keep("class com.example.Main { public static void main(...); }");

        assertEquals(10, task.configuration.optimizationPasses,
                "Aggressive optimization should use 10 passes");
    }

    // Edge cases and validation

    @Test
    public void testOptimizationpasses_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.optimizationpasses(5);

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testOptimizationpasses_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.optimizationpasses(5);
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testOptimizationpasses_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        freshTask.optimizationpasses(5);

        assertEquals(5, freshTask.configuration.optimizationPasses,
                "Fresh task should accept optimization passes");
    }

    @Test
    public void testOptimizationpasses_negativeValue() throws Exception {
        // Test edge case with negative value
        task.optimizationpasses(-1);
        assertEquals(-1, task.configuration.optimizationPasses,
                "Should accept negative value (though not recommended)");
    }

    @Test
    public void testOptimizationpasses_largeValue() throws Exception {
        // Test with large value
        task.optimizationpasses(100);
        assertEquals(100, task.configuration.optimizationPasses,
                "Should accept large values");
    }

    // Build speed vs optimization trade-offs

    @Test
    public void testOptimizationpasses_developmentBuild() throws Exception {
        // Development: 1-2 passes for fast builds
        task.optimizationpasses(1);
        task.dontshrink();
        task.dontobfuscate();

        assertEquals(1, task.configuration.optimizationPasses,
                "Development should use minimal passes");
    }

    @Test
    public void testOptimizationpasses_stagingBuild() throws Exception {
        // Staging: 3-5 passes for balance
        task.optimizationpasses(3);
        task.optimizations("code/removal/*,code/simplification/*");

        assertEquals(3, task.configuration.optimizationPasses,
                "Staging should use balanced passes");
    }

    @Test
    public void testOptimizationpasses_productionBuild() throws Exception {
        // Production: 5-7 passes for maximum optimization
        task.optimizationpasses(7);
        task.optimizations("code/*,field/*,method/*,class/*");
        task.printmapping("mapping.txt");

        assertEquals(7, task.configuration.optimizationPasses,
                "Production should use maximum passes");
    }

    // Complex configuration scenarios

    @Test
    public void testOptimizationpasses_fullOptimizationConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.optimizationpasses(5);
        task.optimizations("code/*,field/*,method/*");
        task.keep("public class * { public *; }");
        task.printseeds("seeds.txt");
        task.verbose();

        assertEquals(5, task.configuration.optimizationPasses,
                "Full configuration should maintain optimization passes");
    }

    @Test
    public void testOptimizationpasses_withSelectiveOptimizations() throws Exception {
        task.optimizationpasses(5);
        task.optimizations("code/removal/*");
        task.optimizations("!code/removal/simple");
        task.optimizations("code/simplification/*");

        assertEquals(5, task.configuration.optimizationPasses);
    }

    @Test
    public void testOptimizationpasses_zeroWithDontOptimize() throws Exception {
        task.optimizationpasses(0);
        task.dontoptimize();

        assertEquals(0, task.configuration.optimizationPasses);
        assertFalse(task.configuration.optimize);
    }

    // Performance testing scenarios

    @Test
    public void testOptimizationpasses_minimalForTesting() throws Exception {
        // Minimal optimization for testing builds
        task.optimizationpasses(1);
        task.keep("class com.example.** { *; }");
        task.printusage("usage.txt");

        assertEquals(1, task.configuration.optimizationPasses);
    }

    @Test
    public void testOptimizationpasses_standardForRelease() throws Exception {
        // Standard 5 passes for typical release
        task.optimizationpasses(5);
        task.keep("public class * { public *; }");
        task.printmapping("mapping.txt");

        assertEquals(5, task.configuration.optimizationPasses);
    }

    @Test
    public void testOptimizationpasses_maximumForSizeCritical() throws Exception {
        // Maximum passes for size-critical applications
        task.optimizationpasses(10);
        task.optimizations("*");

        assertEquals(10, task.configuration.optimizationPasses);
    }

    // Incremental changes

    @Test
    public void testOptimizationpasses_incrementalIncrease() throws Exception {
        task.optimizationpasses(1);
        assertEquals(1, task.configuration.optimizationPasses);

        task.optimizationpasses(3);
        assertEquals(3, task.configuration.optimizationPasses);

        task.optimizationpasses(5);
        assertEquals(5, task.configuration.optimizationPasses);

        task.optimizationpasses(7);
        assertEquals(7, task.configuration.optimizationPasses);
    }

    @Test
    public void testOptimizationpasses_incrementalDecrease() throws Exception {
        task.optimizationpasses(10);
        assertEquals(10, task.configuration.optimizationPasses);

        task.optimizationpasses(7);
        assertEquals(7, task.configuration.optimizationPasses);

        task.optimizationpasses(5);
        assertEquals(5, task.configuration.optimizationPasses);

        task.optimizationpasses(3);
        assertEquals(3, task.configuration.optimizationPasses);
    }

    // Android-specific scenarios

    @Test
    public void testOptimizationpasses_androidDebug() throws Exception {
        // Android debug: optimization typically disabled
        task.optimizationpasses(0);
        task.dontoptimize();

        assertEquals(0, task.configuration.optimizationPasses);
    }

    @Test
    public void testOptimizationpasses_androidRelease() throws Exception {
        // Android release: 5 passes is standard
        task.optimizationpasses(5);
        task.optimizations("!code/simplification/arithmetic");

        assertEquals(5, task.configuration.optimizationPasses);
    }

    @Test
    public void testOptimizationpasses_androidMultiDex() throws Exception {
        // Android multi-dex: moderate optimization
        task.optimizationpasses(3);
        task.keep("class android.support.multidex.** { *; }");

        assertEquals(3, task.configuration.optimizationPasses);
    }
}

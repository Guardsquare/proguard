package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.optimizations(String)V method.
 * Tests the optimizations(String) method that adds optimization filters to
 * configuration.optimizations list. These filters specify which optimizations to enable or disable.
 */
public class ProGuardTaskClaude_optimizationsTest {

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
    public void testOptimizations_addsFilterToList() throws Exception {
        assertNull(task.configuration.optimizations, "optimizations should initially be null");
        task.optimizations("code/removal/*");
        assertNotNull(task.configuration.optimizations, "optimizations should not be null after call");
        assertEquals(1, task.configuration.optimizations.size(),
                "optimizations should contain one filter");
    }

    @Test
    public void testOptimizations_singleOptimization() throws Exception {
        task.optimizations("code/simplification/arithmetic");
        assertNotNull(task.configuration.optimizations);
        assertEquals(1, task.configuration.optimizations.size());
        assertEquals("code/simplification/arithmetic", task.configuration.optimizations.get(0));
    }

    @Test
    public void testOptimizations_wildcardFilter() throws Exception {
        task.optimizations("code/removal/*");
        assertNotNull(task.configuration.optimizations);
        assertEquals(1, task.configuration.optimizations.size());
        assertEquals("code/removal/*", task.configuration.optimizations.get(0));
    }

    @Test
    public void testOptimizations_negationFilter() throws Exception {
        task.optimizations("!code/simplification/cast");
        assertNotNull(task.configuration.optimizations);
        assertEquals(1, task.configuration.optimizations.size());
        assertEquals("!code/simplification/cast", task.configuration.optimizations.get(0));
    }

    // Comma-separated filters tests

    @Test
    public void testOptimizations_commaSeparatedFilters() throws Exception {
        task.optimizations("code/removal/*,code/simplification/*");
        assertNotNull(task.configuration.optimizations);
        assertEquals(2, task.configuration.optimizations.size(),
                "Should parse comma-separated filters");
        assertEquals("code/removal/*", task.configuration.optimizations.get(0));
        assertEquals("code/simplification/*", task.configuration.optimizations.get(1));
    }

    @Test
    public void testOptimizations_multipleCommaSeparatedFilters() throws Exception {
        task.optimizations("code/removal/*,code/simplification/*,field/*");
        assertNotNull(task.configuration.optimizations);
        assertEquals(3, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_commaSeparatedWithNegations() throws Exception {
        task.optimizations("code/removal/*,!code/removal/simple");
        assertNotNull(task.configuration.optimizations);
        assertEquals(2, task.configuration.optimizations.size());
        assertEquals("code/removal/*", task.configuration.optimizations.get(0));
        assertEquals("!code/removal/simple", task.configuration.optimizations.get(1));
    }

    // Multiple calls tests

    @Test
    public void testOptimizations_multipleCallsAccumulate() throws Exception {
        task.optimizations("code/removal/*");
        assertEquals(1, task.configuration.optimizations.size());

        task.optimizations("code/simplification/*");
        assertEquals(2, task.configuration.optimizations.size());

        task.optimizations("field/*");
        assertEquals(3, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_accumulatesFilters() throws Exception {
        assertNull(task.configuration.optimizations);

        task.optimizations("code/removal/*");
        List firstCall = task.configuration.optimizations;
        assertNotNull(firstCall);
        assertEquals(1, firstCall.size());

        task.optimizations("code/simplification/*");
        List secondCall = task.configuration.optimizations;
        assertSame(firstCall, secondCall, "Should reuse same list");
        assertEquals(2, secondCall.size(), "Should accumulate filters");
    }

    @Test
    public void testOptimizations_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.optimizations("optimization" + i + "/*");
        }
        assertEquals(5, task.configuration.optimizations.size(),
                "Should accumulate all 5 filters");
    }

    // Null filter tests

    @Test
    public void testOptimizations_nullClearsList() throws Exception {
        task.optimizations("code/removal/*");
        task.optimizations("code/simplification/*");
        assertEquals(2, task.configuration.optimizations.size());

        task.optimizations(null);
        assertNotNull(task.configuration.optimizations, "List should not be null");
        assertEquals(0, task.configuration.optimizations.size(),
                "Passing null should clear the list");
    }

    @Test
    public void testOptimizations_nullOnEmptyList() throws Exception {
        assertNull(task.configuration.optimizations);
        task.optimizations(null);
        assertNotNull(task.configuration.optimizations);
        assertEquals(0, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_addAfterNull() throws Exception {
        task.optimizations("code/removal/*");
        task.optimizations(null); // Clear
        task.optimizations("code/simplification/*"); // Add again

        assertEquals(1, task.configuration.optimizations.size(),
                "Should be able to add after clearing with null");
        assertEquals("code/simplification/*", task.configuration.optimizations.get(0));
    }

    // Standard ProGuard optimization filters

    @Test
    public void testOptimizations_codeRemoval() throws Exception {
        task.optimizations("code/removal/*");
        assertEquals("code/removal/*", task.configuration.optimizations.get(0));
    }

    @Test
    public void testOptimizations_codeSimplification() throws Exception {
        task.optimizations("code/simplification/*");
        assertEquals("code/simplification/*", task.configuration.optimizations.get(0));
    }

    @Test
    public void testOptimizations_codeMerging() throws Exception {
        task.optimizations("code/merging");
        assertEquals("code/merging", task.configuration.optimizations.get(0));
    }

    @Test
    public void testOptimizations_fieldOptimizations() throws Exception {
        task.optimizations("field/*");
        assertEquals("field/*", task.configuration.optimizations.get(0));
    }

    @Test
    public void testOptimizations_methodOptimizations() throws Exception {
        task.optimizations("method/*");
        assertEquals("method/*", task.configuration.optimizations.get(0));
    }

    @Test
    public void testOptimizations_classOptimizations() throws Exception {
        task.optimizations("class/*");
        assertEquals("class/*", task.configuration.optimizations.get(0));
    }

    // Integration with other configuration methods

    @Test
    public void testOptimizations_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.optimizations("code/removal/*");

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertNotNull(task.configuration.optimizations);
        assertEquals(1, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_worksWithOptimizationPasses() throws Exception {
        task.optimizationpasses(5);
        task.optimizations("code/removal/*");

        assertEquals(5, task.configuration.optimizationPasses);
        assertNotNull(task.configuration.optimizations);
        assertEquals(1, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.optimizations("code/removal/*");

        assertNotNull(task.configuration.keep);
        assertNotNull(task.configuration.optimizations);
        assertEquals(1, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_worksWithPrintOptions() throws Exception {
        task.printseeds("seeds.txt");
        task.optimizations("code/removal/*");

        assertEquals("seeds.txt", task.configuration.printSeeds.getName());
        assertNotNull(task.configuration.optimizations);
        assertEquals(1, task.configuration.optimizations.size());
    }

    // Call order flexibility

    @Test
    public void testOptimizations_canBeCalledFirst() throws Exception {
        task.optimizations("code/removal/*");
        task.keep("public class * { public *; }");
        task.optimizationpasses(3);

        assertNotNull(task.configuration.optimizations);
        assertEquals(1, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizationpasses(3);
        task.optimizations("code/removal/*");

        assertNotNull(task.configuration.optimizations);
        assertEquals(1, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizations("code/removal/*");
        task.optimizationpasses(3);

        assertNotNull(task.configuration.optimizations);
        assertEquals(1, task.configuration.optimizations.size());
    }

    // Realistic usage scenarios

    @Test
    public void testOptimizations_aggressiveOptimization() throws Exception {
        // Aggressive optimization with multiple passes
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-optimized.jar");
        task.optimizationpasses(5);
        task.optimizations("code/removal/*,code/simplification/*,field/*,method/*,class/*");

        assertEquals(5, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_selectiveOptimization() throws Exception {
        // Enable some optimizations, disable others
        task.optimizations("code/removal/*,!code/removal/simple");
        task.optimizations("code/simplification/*,!code/simplification/cast");

        assertEquals(4, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_androidReleaseOptimization() throws Exception {
        // Typical Android release build optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.optimizationpasses(5);
        task.optimizations("!code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*");

        assertTrue(task.configuration.optimizations.size() > 0);
    }

    @Test
    public void testOptimizations_libraryOptimization() throws Exception {
        // Library optimization with conservative settings
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-optimized.jar");
        task.keep("public class * { public *; }");
        task.optimizations("code/removal/*");

        assertEquals(1, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_disableProblematicOptimizations() throws Exception {
        // Disable optimizations that might cause issues
        task.optimizations("!code/simplification/arithmetic");
        task.optimizations("!code/simplification/cast");
        task.optimizations("!field/*");

        assertEquals(3, task.configuration.optimizations.size());
        assertTrue(task.configuration.optimizations.get(0).startsWith("!"));
    }

    // Edge cases and validation

    @Test
    public void testOptimizations_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.optimizations("code/removal/*");

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testOptimizations_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.optimizations("code/removal/*");
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testOptimizations_listIsModifiable() throws Exception {
        task.optimizations("code/removal/*");
        List list = task.configuration.optimizations;
        int initialSize = list.size();

        task.optimizations("code/simplification/*");

        assertEquals(initialSize + 1, list.size(), "List should be modifiable and grow");
    }

    @Test
    public void testOptimizations_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertNull(freshTask.configuration.optimizations);

        freshTask.optimizations("code/removal/*");

        assertNotNull(freshTask.configuration.optimizations);
        assertEquals(1, freshTask.configuration.optimizations.size());
    }

    // Complex filter patterns

    @Test
    public void testOptimizations_complexFilterPattern() throws Exception {
        task.optimizations("code/removal/*,!code/removal/simple");
        task.optimizations("code/simplification/*,!code/simplification/arithmetic");
        task.optimizations("field/*,!field/propagation/value");

        assertEquals(6, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_wildcardAtDifferentLevels() throws Exception {
        task.optimizations("code/*");
        task.optimizations("code/removal/*");
        task.optimizations("code/simplification/advanced");

        assertEquals(3, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_mixedPositiveAndNegative() throws Exception {
        task.optimizations("code/*,!code/simplification/arithmetic");

        assertEquals(2, task.configuration.optimizations.size());
        assertEquals("code/*", task.configuration.optimizations.get(0));
        assertEquals("!code/simplification/arithmetic", task.configuration.optimizations.get(1));
    }

    // All standard optimizations

    @Test
    public void testOptimizations_allCodeOptimizations() throws Exception {
        task.optimizations("code/removal/*");
        task.optimizations("code/simplification/*");
        task.optimizations("code/allocation/*");

        assertEquals(3, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_allFieldOptimizations() throws Exception {
        task.optimizations("field/removal/*");
        task.optimizations("field/propagation/*");

        assertEquals(2, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_allMethodOptimizations() throws Exception {
        task.optimizations("method/removal/*");
        task.optimizations("method/propagation/*");
        task.optimizations("method/inlining/*");

        assertEquals(3, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_allClassOptimizations() throws Exception {
        task.optimizations("class/merging/*");
        task.optimizations("class/unboxing/*");

        assertEquals(2, task.configuration.optimizations.size());
    }

    // Performance optimization scenarios

    @Test
    public void testOptimizations_maxPerformanceConfiguration() throws Exception {
        // Maximum performance: all optimizations enabled with multiple passes
        task.optimizationpasses(7);
        task.optimizations("*");

        assertEquals(1, task.configuration.optimizations.size());
        assertEquals("*", task.configuration.optimizations.get(0));
    }

    @Test
    public void testOptimizations_balancedConfiguration() throws Exception {
        // Balanced: most optimizations, but exclude problematic ones
        task.optimizationpasses(5);
        task.optimizations("!code/simplification/arithmetic");
        task.optimizations("!code/simplification/cast");
        task.optimizations("!field/propagation/value");

        assertEquals(3, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_safeConfiguration() throws Exception {
        // Safe: only basic optimizations
        task.optimizationpasses(3);
        task.optimizations("code/removal/advanced");
        task.optimizations("code/removal/simple");

        assertEquals(2, task.configuration.optimizations.size());
    }

    // Build speed vs optimization trade-off

    @Test
    public void testOptimizations_fastBuildMinimalOptimization() throws Exception {
        // Fast build: minimal optimization
        task.optimizationpasses(1);
        task.optimizations("code/removal/simple");

        assertEquals(1, task.configuration.optimizations.size());
    }

    @Test
    public void testOptimizations_slowBuildMaximalOptimization() throws Exception {
        // Slow build: maximal optimization for release
        task.optimizationpasses(7);
        task.optimizations("code/*,field/*,method/*,class/*");

        assertEquals(4, task.configuration.optimizations.size());
    }
}

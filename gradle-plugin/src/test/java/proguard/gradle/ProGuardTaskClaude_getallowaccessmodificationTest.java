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
 * Test class for ProGuardTask.getallowaccessmodification()Ljava/lang/Object; method.
 * Tests the getallowaccessmodification() Groovy DSL method that returns null and calls allowaccessmodification()
 * to set configuration.allowAccessModification to true.
 */
public class ProGuardTaskClaude_getallowaccessmodificationTest {

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
    public void testGetallowaccessmodification_returnsNull() throws Exception {
        Object result = task.getallowaccessmodification();
        assertNull(result, "getallowaccessmodification() should return null");
    }

    @Test
    public void testGetallowaccessmodification_setsAllowAccessModificationToTrue() throws Exception {
        assertFalse(task.configuration.allowAccessModification, "allowAccessModification should initially be false");
        task.getallowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be set to true");
    }

    @Test
    public void testGetallowaccessmodification_returnsNullAndSetsAllowAccessModification() throws Exception {
        assertFalse(task.configuration.allowAccessModification, "allowAccessModification should initially be false");
        Object result = task.getallowaccessmodification();
        assertNull(result, "getallowaccessmodification() should return null");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be set to true");
    }

    // Multiple calls tests

    @Test
    public void testGetallowaccessmodification_multipleCallsAreIdempotent() throws Exception {
        task.getallowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "First call should set allowAccessModification to true");

        task.getallowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "Second call should keep allowAccessModification as true");

        task.getallowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "Third call should keep allowAccessModification as true");
    }

    @Test
    public void testGetallowaccessmodification_alwaysReturnsNull() throws Exception {
        Object result1 = task.getallowaccessmodification();
        Object result2 = task.getallowaccessmodification();
        Object result3 = task.getallowaccessmodification();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetallowaccessmodification_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getallowaccessmodification();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true after call " + (i + 1));
        }
    }

    // Integration with other configuration methods

    @Test
    public void testGetallowaccessmodification_worksWithOptimizationSettings() throws Exception {
        task.getallowaccessmodification();
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
        assertNotNull(task.configuration.optimizations, "optimizations should be set");
    }

    @Test
    public void testGetallowaccessmodification_worksWithMergeInterfacesAggressively() throws Exception {
        task.getallowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetallowaccessmodification_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.getallowaccessmodification();

        assertNotNull(task.configuration.keep, "keep rules should be configured");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetallowaccessmodification_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.getallowaccessmodification();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetallowaccessmodification_worksWithPrintOptions() throws Exception {
        task.printusage("usage.txt");
        task.printseeds("seeds.txt");
        task.getallowaccessmodification();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals("usage.txt", task.configuration.printUsage.getName());
        assertEquals("seeds.txt", task.configuration.printSeeds.getName());
    }

    @Test
    public void testGetallowaccessmodification_worksWithVerbose() throws Exception {
        task.verbose();
        task.getallowaccessmodification();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // Call order flexibility

    @Test
    public void testGetallowaccessmodification_canBeCalledFirst() throws Exception {
        task.getallowaccessmodification();
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testGetallowaccessmodification_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);
        task.getallowaccessmodification();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testGetallowaccessmodification_canBeCalledBetweenOtherMethods() throws Exception {
        task.keep("public class * { public *; }");
        task.getallowaccessmodification();
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // Realistic usage scenarios

    @Test
    public void testGetallowaccessmodification_androidReleaseBuild() throws Exception {
        // Release builds often enable access modification for better optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.getallowaccessmodification();
        task.optimizationpasses(5);
        task.keep("class com.example.MainActivity { *; }");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled for release build");
    }

    @Test
    public void testGetallowaccessmodification_aggressiveOptimizationScenario() throws Exception {
        // Aggressive optimization with access modification
        task.getallowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(7);
        task.optimizations("!code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testGetallowaccessmodification_libraryOptimization() throws Exception {
        // Library optimization scenario
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-optimized.jar");
        task.getallowaccessmodification();
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled for library");
    }

    @Test
    public void testGetallowaccessmodification_completeReleaseConfiguration() throws Exception {
        // Complete release configuration
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.getallowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.printmapping("mapping.txt");
        task.verbose();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testGetallowaccessmodification_optimizationWithShrinking() throws Exception {
        // Optimization with shrinking enabled
        task.getallowaccessmodification();
        task.keep("class com.example.** { *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.shrink, "shrink should be enabled by default");
        assertTrue(task.configuration.optimize, "optimize should be enabled by default");
    }

    // Edge cases and validation

    @Test
    public void testGetallowaccessmodification_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.getallowaccessmodification();

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetallowaccessmodification_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.getallowaccessmodification();
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testGetallowaccessmodification_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertFalse(freshTask.configuration.allowAccessModification, "Fresh task allowAccessModification should be false");

        Object result = freshTask.getallowaccessmodification();

        assertNull(result, "Should return null");
        assertTrue(freshTask.configuration.allowAccessModification, "Fresh task allowAccessModification should be true");
    }

    @Test
    public void testGetallowaccessmodification_afterManualAllowAccessModificationTrue() throws Exception {
        task.configuration.allowAccessModification = true;
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");

        Object result = task.getallowaccessmodification();

        assertNull(result, "Should return null");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain true");
    }

    @Test
    public void testGetallowaccessmodification_consistentWithAllowaccessmodificationMethod() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard3", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard4", ProGuardTask.class);

        // Using getallowaccessmodification()
        task1.getallowaccessmodification();

        // Using allowaccessmodification() directly
        task2.allowaccessmodification();

        assertEquals(task1.configuration.allowAccessModification, task2.configuration.allowAccessModification,
                "Both methods should have same effect on allowAccessModification setting");
    }

    // Interaction with optimization settings

    @Test
    public void testGetallowaccessmodification_enablesAccessModification() throws Exception {
        assertFalse(task.configuration.allowAccessModification, "allowAccessModification should be disabled by default");
        task.getallowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled after getallowaccessmodification()");
    }

    @Test
    public void testGetallowaccessmodification_withOptimizationPasses() throws Exception {
        task.getallowaccessmodification();
        task.optimizationpasses(5);
        task.keep("class com.example.MyClass { *; }");

        assertTrue(task.configuration.allowAccessModification,
                "allowAccessModification should be true with optimization passes");
    }

    @Test
    public void testGetallowaccessmodification_complexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.getallowaccessmodification();
        task.optimizations("!code/simplification/arithmetic");
        task.printseeds("seeds.txt");
        task.verbose();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true in complex config");
    }

    // Groovy DSL compatibility

    @Test
    public void testGetallowaccessmodification_groovyDslSupport() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        // In Groovy: allowaccessmodification (without parentheses) calls getallowaccessmodification()
        Object result = task.getallowaccessmodification();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertTrue(task.configuration.allowAccessModification, "Should enable access modification");
    }

    @Test
    public void testGetallowaccessmodification_returnsNullForChaining() throws Exception {
        // Even though it returns null, verify the method completes successfully
        Object result = task.getallowaccessmodification();

        assertNull(result, "Should return null");
        // Verify we can still call other methods after
        task.optimizationpasses(5);
        assertEquals(5, task.configuration.optimizationPasses, "Should be able to call other methods after");
    }

    // Multiple optimization-related methods

    @Test
    public void testGetallowaccessmodification_withOtherOptimizationFlags() throws Exception {
        task.getallowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testGetallowaccessmodification_doesNotDisableAccessModification() throws Exception {
        task.getallowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");

        // Call other methods
        task.keep("class com.example.MyClass { *; }");
        task.optimizationpasses(5);

        // Verify allowAccessModification is still true
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should still be true");
    }

    @Test
    public void testGetallowaccessmodification_withAllOptimizationMethods() throws Exception {
        task.getallowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(7);
        task.optimizations("!method/inlining/*");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(7, task.configuration.optimizationPasses, "optimizationPasses should be 7");
        assertNotNull(task.configuration.optimizations, "optimizations should be set");
    }

    @Test
    public void testGetallowaccessmodification_worksWith_dontOptimize() throws Exception {
        // Even when optimization is disabled, access modification can be enabled
        task.dontoptimize();
        task.getallowaccessmodification();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetallowaccessmodification_withObfuscation() throws Exception {
        task.getallowaccessmodification();
        task.keep("class com.example.MyClass { *; }");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.obfuscate, "obfuscate should be enabled by default");
    }

    @Test
    public void testGetallowaccessmodification_withoutObfuscation() throws Exception {
        task.dontobfuscate();
        task.getallowaccessmodification();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // Android-specific scenarios

    @Test
    public void testGetallowaccessmodification_androidReleaseOptimization() throws Exception {
        // Android release build with aggressive optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/apk/release");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.getallowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.keep("class com.example.MainActivity { *; }");
        task.printmapping("mapping.txt");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testGetallowaccessmodification_androidLibraryConfiguration() throws Exception {
        // Android library with optimization
        task.injars("build/intermediates/aar_main_jar");
        task.outjars("build/outputs/aar");
        task.getallowaccessmodification();
        task.keep("public class * { public *; }");
        task.optimizationpasses(3);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }

    // Performance optimization scenarios

    @Test
    public void testGetallowaccessmodification_maximumOptimization() throws Exception {
        // Maximum optimization configuration
        task.getallowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(10);
        task.optimizations("*");
        task.overloadaggressively();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be enabled");
    }

    @Test
    public void testGetallowaccessmodification_moderateOptimization() throws Exception {
        // Moderate optimization configuration
        task.getallowaccessmodification();
        task.optimizationpasses(3);
        task.keep("class com.example.** { *; }");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertEquals(3, task.configuration.optimizationPasses, "optimizationPasses should be 3");
    }
}

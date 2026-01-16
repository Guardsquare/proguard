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
 * Test class for ProGuardTask.mergeinterfacesaggressively()V method.
 * Tests the mergeinterfacesaggressively() void method that sets configuration.mergeInterfacesAggressively to true,
 * enabling ProGuard to aggressively merge interfaces to reduce code size and improve optimization.
 */
public class ProGuardTaskClaude_mergeinterfacesaggressivelyTest {

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
    public void testMergeinterfacesaggressively_setsMergeInterfacesAggressivelyToTrue() throws Exception {
        assertFalse(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should initially be false");
        task.mergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be set to true");
    }

    @Test
    public void testMergeinterfacesaggressively_enablesInterfaceMerging() throws Exception {
        assertFalse(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be disabled by default");
        task.mergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "interface merging should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_doesNotReturnValue() throws Exception {
        // Verify method is void and completes successfully
        task.mergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true after call");
    }

    // Multiple calls tests

    @Test
    public void testMergeinterfacesaggressively_multipleCallsAreIdempotent() throws Exception {
        task.mergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "First call should set mergeInterfacesAggressively to true");

        task.mergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "Second call should keep mergeInterfacesAggressively as true");

        task.mergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "Third call should keep mergeInterfacesAggressively as true");
    }

    @Test
    public void testMergeinterfacesaggressively_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.mergeinterfacesaggressively();
            assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true after call " + (i + 1));
        }
    }

    // Integration with other configuration methods

    @Test
    public void testMergeinterfacesaggressively_worksWithAllowAccessModification() throws Exception {
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testMergeinterfacesaggressively_worksWithOptimizationSettings() throws Exception {
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
        assertNotNull(task.configuration.optimizations, "optimizations should be set");
    }

    @Test
    public void testMergeinterfacesaggressively_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.keep, "keep rules should be configured");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testMergeinterfacesaggressively_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testMergeinterfacesaggressively_worksWithPrintOptions() throws Exception {
        task.printusage("usage.txt");
        task.printseeds("seeds.txt");
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals("usage.txt", task.configuration.printUsage.getName());
        assertEquals("seeds.txt", task.configuration.printSeeds.getName());
    }

    @Test
    public void testMergeinterfacesaggressively_worksWithVerbose() throws Exception {
        task.verbose();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // Call order flexibility

    @Test
    public void testMergeinterfacesaggressively_canBeCalledFirst() throws Exception {
        task.mergeinterfacesaggressively();
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testMergeinterfacesaggressively_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testMergeinterfacesaggressively_canBeCalledBetweenOtherMethods() throws Exception {
        task.keep("public class * { public *; }");
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // Realistic usage scenarios

    @Test
    public void testMergeinterfacesaggressively_androidReleaseBuild() throws Exception {
        // Release builds often enable aggressive interface merging for better optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(5);
        task.keep("class com.example.MainActivity { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled for release build");
    }

    @Test
    public void testMergeinterfacesaggressively_aggressiveOptimizationScenario() throws Exception {
        // Aggressive optimization with interface merging and access modification
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(7);
        task.optimizations("!code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_libraryOptimization() throws Exception {
        // Library optimization scenario
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-optimized.jar");
        task.mergeinterfacesaggressively();
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled for library");
    }

    @Test
    public void testMergeinterfacesaggressively_completeReleaseConfiguration() throws Exception {
        // Complete release configuration
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(5);
        task.printmapping("mapping.txt");
        task.verbose();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_optimizationWithShrinking() throws Exception {
        // Optimization with shrinking enabled
        task.mergeinterfacesaggressively();
        task.keep("class com.example.** { *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.shrink, "shrink should be enabled by default");
        assertTrue(task.configuration.optimize, "optimize should be enabled by default");
    }

    @Test
    public void testMergeinterfacesaggressively_maximumCodeReduction() throws Exception {
        // Maximum code reduction configuration
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.overloadaggressively();
        task.optimizationpasses(7);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_quickBuildConfiguration() throws Exception {
        // Quick build for development with interface merging
        task.injars("build/classes");
        task.outjars("build/outputs");
        task.mergeinterfacesaggressively();
        task.optimizationpasses(3);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertEquals(3, task.configuration.optimizationPasses, "optimizationPasses should be 3");
    }

    // Edge cases and validation

    @Test
    public void testMergeinterfacesaggressively_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.mergeinterfacesaggressively();

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testMergeinterfacesaggressively_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.mergeinterfacesaggressively();
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testMergeinterfacesaggressively_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertFalse(freshTask.configuration.mergeInterfacesAggressively, "Fresh task mergeInterfacesAggressively should be false");

        freshTask.mergeinterfacesaggressively();

        assertTrue(freshTask.configuration.mergeInterfacesAggressively, "Fresh task mergeInterfacesAggressively should be true");
    }

    @Test
    public void testMergeinterfacesaggressively_afterManualMergeInterfacesAggressivelyTrue() throws Exception {
        task.configuration.mergeInterfacesAggressively = true;
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");

        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain true");
    }

    @Test
    public void testMergeinterfacesaggressively_equivalentToGetmergeinterfacesaggressivelyMethod() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard3", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard4", ProGuardTask.class);

        // Using mergeinterfacesaggressively()
        task1.mergeinterfacesaggressively();

        // Using getmergeinterfacesaggressively() (Groovy DSL method)
        task2.getmergeinterfacesaggressively();

        assertEquals(task1.configuration.mergeInterfacesAggressively, task2.configuration.mergeInterfacesAggressively,
                "Both methods should have same effect on mergeInterfacesAggressively setting");
    }

    // Interaction with optimization settings

    @Test
    public void testMergeinterfacesaggressively_enablesInterfaceMergingFlag() throws Exception {
        assertFalse(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be disabled by default");
        task.mergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled after mergeinterfacesaggressively()");
    }

    @Test
    public void testMergeinterfacesaggressively_withOptimizationPasses() throws Exception {
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.keep("class com.example.MyClass { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be true with optimization passes");
    }

    @Test
    public void testMergeinterfacesaggressively_complexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.mergeinterfacesaggressively();
        task.optimizations("!code/simplification/arithmetic");
        task.printseeds("seeds.txt");
        task.verbose();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true in complex config");
    }

    // Multiple optimization-related methods

    @Test
    public void testMergeinterfacesaggressively_withOtherOptimizationFlags() throws Exception {
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testMergeinterfacesaggressively_doesNotDisableInterfaceMerging() throws Exception {
        task.mergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");

        // Call other methods
        task.keep("class com.example.MyClass { *; }");
        task.optimizationpasses(5);

        // Verify mergeInterfacesAggressively is still true
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should still be true");
    }

    @Test
    public void testMergeinterfacesaggressively_withAllOptimizationMethods() throws Exception {
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(7);
        task.optimizations("!method/inlining/*");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(7, task.configuration.optimizationPasses, "optimizationPasses should be 7");
        assertNotNull(task.configuration.optimizations, "optimizations should be set");
    }

    @Test
    public void testMergeinterfacesaggressively_worksWith_dontOptimize() throws Exception {
        // Even when optimization is disabled, interface merging can be enabled
        task.dontoptimize();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testMergeinterfacesaggressively_withObfuscation() throws Exception {
        task.mergeinterfacesaggressively();
        task.keep("class com.example.MyClass { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.obfuscate, "obfuscate should be enabled by default");
    }

    @Test
    public void testMergeinterfacesaggressively_withoutObfuscation() throws Exception {
        task.dontobfuscate();
        task.mergeinterfacesaggressively();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testMergeinterfacesaggressively_withShrinkingOnly() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.shrink, "shrink should be enabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // Android-specific scenarios

    @Test
    public void testMergeinterfacesaggressively_androidReleaseOptimization() throws Exception {
        // Android release build with aggressive optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/apk/release");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(5);
        task.keep("class com.example.MainActivity { *; }");
        task.printmapping("mapping.txt");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_androidLibraryConfiguration() throws Exception {
        // Android library with optimization
        task.injars("build/intermediates/aar_main_jar");
        task.outjars("build/outputs/aar");
        task.mergeinterfacesaggressively();
        task.keep("public class * { public *; }");
        task.optimizationpasses(3);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_androidMultiModuleApp() throws Exception {
        // Multi-module Android app with interface merging
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.mergeinterfacesaggressively();
        task.keep("class com.example.module.** { *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_androidReleaseWithoutOptimization() throws Exception {
        // Release build that disables optimization but enables interface merging
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.dontoptimize();
        task.mergeinterfacesaggressively();
        task.keep("class com.example.MainActivity { *; }");
        task.printmapping("mapping.txt");

        assertTrue(task.configuration.shrink, "shrink should be enabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_androidMultiDexScenario() throws Exception {
        // Multi-dex scenario with interface merging to reduce method count
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.mergeinterfacesaggressively();
        task.keep("class android.support.multidex.** { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled for multi-dex");
    }

    // Performance optimization scenarios

    @Test
    public void testMergeinterfacesaggressively_maximumOptimization() throws Exception {
        // Maximum optimization configuration
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(10);
        task.optimizations("*");
        task.overloadaggressively();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_moderateOptimization() throws Exception {
        // Moderate optimization configuration
        task.mergeinterfacesaggressively();
        task.optimizationpasses(3);
        task.keep("class com.example.** { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertEquals(3, task.configuration.optimizationPasses, "optimizationPasses should be 3");
    }

    @Test
    public void testMergeinterfacesaggressively_minimalOptimization() throws Exception {
        // Minimal optimization with just interface merging
        task.mergeinterfacesaggressively();
        task.optimizationpasses(1);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertEquals(1, task.configuration.optimizationPasses, "optimizationPasses should be 1");
    }

    @Test
    public void testMergeinterfacesaggressively_fastIncrementalBuild() throws Exception {
        // Fast incremental build with interface merging
        task.injars("build/classes");
        task.outjars("build/outputs");
        task.mergeinterfacesaggressively();
        task.optimizationpasses(2);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled for fast builds");
        assertEquals(2, task.configuration.optimizationPasses, "optimizationPasses should be 2 for fast builds");
    }

    // Compatibility scenarios

    @Test
    public void testMergeinterfacesaggressively_withInterfaceKeepRules() throws Exception {
        // Configuration with interface keep rules
        task.mergeinterfacesaggressively();
        task.keep("interface * { *; }");
        task.keep("class * implements com.example.MyInterface { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesaggressively should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_withReflectionConfiguration() throws Exception {
        // Configuration that uses reflection with interface merging
        task.mergeinterfacesaggressively();
        task.keep("class * { public <methods>; }");
        task.keepclassmembers("class * { @com.example.KeepThis *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesaggressively should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_withSerializableClasses() throws Exception {
        // Configuration for serializable classes with interface merging
        task.mergeinterfacesaggressively();
        task.keep("class * implements java.io.Serializable { *; }");
        task.keepclassmembers("class * implements java.io.Serializable { private <fields>; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesaggressively should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_libraryModeWithPublicAPI() throws Exception {
        // Library mode where interfaces are part of public API
        task.mergeinterfacesaggressively();
        task.keep("public interface * { *; }");
        task.keep("public class * { public *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesaggressively should be enabled for library");
    }

    @Test
    public void testMergeinterfacesaggressively_compatibilityMode() throws Exception {
        // Compatibility mode with interface merging
        task.mergeinterfacesaggressively();
        task.keep("interface * { *; }");
        task.keepclassmembers("class * { private <fields>; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesaggressively should be enabled for compatibility");
    }

    // Combination scenarios

    @Test
    public void testMergeinterfacesaggressively_withKeepAttributes() throws Exception {
        // Interface merging with keep attributes
        task.mergeinterfacesaggressively();
        task.keepattributes("Signature,*Annotation*,InnerClasses,EnclosingMethod");
        task.keep("class com.example.** { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
    }

    @Test
    public void testMergeinterfacesaggressively_withRepackageClasses() throws Exception {
        // Interface merging with class repackaging
        task.mergeinterfacesaggressively();
        task.repackageclasses("");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
    }

    @Test
    public void testMergeinterfacesaggressively_withFlattenPackageHierarchy() throws Exception {
        // Interface merging with package flattening
        task.mergeinterfacesaggressively();
        task.flattenpackagehierarchy("com.example.flat");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
    }

    @Test
    public void testMergeinterfacesaggressively_withUseUniqueClassMemberNames() throws Exception {
        // Interface merging with unique class member names
        task.mergeinterfacesaggressively();
        task.useuniqueclassmembernames();
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.useUniqueClassMemberNames, "useUniqueClassMemberNames should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_withOverloadAggressively() throws Exception {
        // Interface merging with aggressive overload
        task.mergeinterfacesaggressively();
        task.overloadaggressively();
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be enabled");
    }

    // Output and reporting

    @Test
    public void testMergeinterfacesaggressively_withPrintConfiguration() throws Exception {
        // Interface merging with configuration printing
        task.mergeinterfacesaggressively();
        task.printconfiguration("configuration.txt");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertEquals("configuration.txt", task.configuration.printConfiguration.getName());
    }

    @Test
    public void testMergeinterfacesaggressively_withPrintMapping() throws Exception {
        // Interface merging with mapping output
        task.mergeinterfacesaggressively();
        task.printmapping("mapping.txt");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    @Test
    public void testMergeinterfacesaggressively_completeOutputConfiguration() throws Exception {
        // Complete configuration with all output files
        task.mergeinterfacesaggressively();
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");
        task.printmapping("mapping.txt");
        task.printconfiguration("configuration.txt");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    // Build variant scenarios

    @Test
    public void testMergeinterfacesaggressively_productionRelease() throws Exception {
        // Production release with maximum optimization
        task.injars("build/classes/production");
        task.outjars("build/release/app.jar");
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.overloadaggressively();
        task.optimizationpasses(7);
        task.repackageclasses("");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_stagingBuild() throws Exception {
        // Staging build with moderate optimization
        task.injars("build/classes/staging");
        task.outjars("build/staging/app.jar");
        task.mergeinterfacesaggressively();
        task.optimizationpasses(3);
        task.printmapping("staging-mapping.txt");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_betaBuild() throws Exception {
        // Beta build with optimization but preserved line numbers
        task.injars("build/classes/beta");
        task.outjars("build/beta/app.jar");
        task.mergeinterfacesaggressively();
        task.optimizationpasses(3);
        task.keepattributes("LineNumberTable,SourceFile");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testMergeinterfacesaggressively_debuggingFriendlyConfiguration() throws Exception {
        // Configuration that preserves some debugging info but merges interfaces
        task.mergeinterfacesaggressively();
        task.keepattributes("LineNumberTable,SourceFile");
        task.keep("class com.example.** { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled for debugging-friendly builds");
    }

    // Interface-specific scenarios

    @Test
    public void testMergeinterfacesaggressively_manyInterfacesScenario() throws Exception {
        // Scenario with many interfaces that can benefit from merging
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.keep("class com.example.impl.** { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled for interface-heavy code");
    }

    @Test
    public void testMergeinterfacesaggressively_strategyPatternOptimization() throws Exception {
        // Optimization for strategy pattern with multiple interface implementations
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.keep("class com.example.strategy.** { *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled for strategy pattern");
    }

    @Test
    public void testMergeinterfacesaggressively_adapterPatternOptimization() throws Exception {
        // Optimization for adapter pattern with interface hierarchies
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled for adapter pattern");
    }

    @Test
    public void testMergeinterfacesaggressively_reducesMethodCount() throws Exception {
        // Configuration specifically aimed at reducing method count
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.overloadaggressively();
        task.optimizationpasses(7);
        task.repackageclasses("");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled to reduce method count");
    }

    @Test
    public void testMergeinterfacesaggressively_singleImplementationInterfaces() throws Exception {
        // Scenario optimizing interfaces with single implementations
        task.mergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled for single-implementation interfaces");
    }
}

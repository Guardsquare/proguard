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
 * Test class for ProGuardTask.allowaccessmodification()V method.
 * Tests the allowaccessmodification() void method that sets configuration.allowAccessModification to true,
 * enabling ProGuard to modify access modifiers (public, protected, private, package-private) for better optimization.
 */
public class ProGuardTaskClaude_allowaccessmodificationTest {

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
    public void testAllowaccessmodification_setsAllowAccessModificationToTrue() throws Exception {
        assertFalse(task.configuration.allowAccessModification, "allowAccessModification should initially be false");
        task.allowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be set to true");
    }

    @Test
    public void testAllowaccessmodification_enablesAccessModification() throws Exception {
        assertFalse(task.configuration.allowAccessModification, "allowAccessModification should be disabled by default");
        task.allowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "access modification should be enabled");
    }

    @Test
    public void testAllowaccessmodification_doesNotReturnValue() throws Exception {
        // Verify method is void and completes successfully
        task.allowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true after call");
    }

    // Multiple calls tests

    @Test
    public void testAllowaccessmodification_multipleCallsAreIdempotent() throws Exception {
        task.allowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "First call should set allowAccessModification to true");

        task.allowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "Second call should keep allowAccessModification as true");

        task.allowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "Third call should keep allowAccessModification as true");
    }

    @Test
    public void testAllowaccessmodification_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.allowaccessmodification();
            assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true after call " + (i + 1));
        }
    }

    // Integration with other configuration methods

    @Test
    public void testAllowaccessmodification_worksWithOptimizationSettings() throws Exception {
        task.allowaccessmodification();
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
        assertNotNull(task.configuration.optimizations, "optimizations should be set");
    }

    @Test
    public void testAllowaccessmodification_worksWithMergeInterfacesAggressively() throws Exception {
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testAllowaccessmodification_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.allowaccessmodification();

        assertNotNull(task.configuration.keep, "keep rules should be configured");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testAllowaccessmodification_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.allowaccessmodification();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testAllowaccessmodification_worksWithPrintOptions() throws Exception {
        task.printusage("usage.txt");
        task.printseeds("seeds.txt");
        task.allowaccessmodification();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals("usage.txt", task.configuration.printUsage.getName());
        assertEquals("seeds.txt", task.configuration.printSeeds.getName());
    }

    @Test
    public void testAllowaccessmodification_worksWithVerbose() throws Exception {
        task.verbose();
        task.allowaccessmodification();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // Call order flexibility

    @Test
    public void testAllowaccessmodification_canBeCalledFirst() throws Exception {
        task.allowaccessmodification();
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testAllowaccessmodification_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);
        task.allowaccessmodification();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testAllowaccessmodification_canBeCalledBetweenOtherMethods() throws Exception {
        task.keep("public class * { public *; }");
        task.allowaccessmodification();
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // Realistic usage scenarios

    @Test
    public void testAllowaccessmodification_androidReleaseBuild() throws Exception {
        // Release builds often enable access modification for better optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.allowaccessmodification();
        task.optimizationpasses(5);
        task.keep("class com.example.MainActivity { *; }");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled for release build");
    }

    @Test
    public void testAllowaccessmodification_aggressiveOptimizationScenario() throws Exception {
        // Aggressive optimization with access modification
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(7);
        task.optimizations("!code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testAllowaccessmodification_libraryOptimization() throws Exception {
        // Library optimization scenario
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-optimized.jar");
        task.allowaccessmodification();
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled for library");
    }

    @Test
    public void testAllowaccessmodification_completeReleaseConfiguration() throws Exception {
        // Complete release configuration
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.printmapping("mapping.txt");
        task.verbose();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testAllowaccessmodification_optimizationWithShrinking() throws Exception {
        // Optimization with shrinking enabled
        task.allowaccessmodification();
        task.keep("class com.example.** { *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.shrink, "shrink should be enabled by default");
        assertTrue(task.configuration.optimize, "optimize should be enabled by default");
    }

    @Test
    public void testAllowaccessmodification_maximumCodeReduction() throws Exception {
        // Maximum code reduction configuration
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.overloadaggressively();
        task.optimizationpasses(7);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be enabled");
    }

    // Edge cases and validation

    @Test
    public void testAllowaccessmodification_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.allowaccessmodification();

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testAllowaccessmodification_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.allowaccessmodification();
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testAllowaccessmodification_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertFalse(freshTask.configuration.allowAccessModification, "Fresh task allowAccessModification should be false");

        freshTask.allowaccessmodification();

        assertTrue(freshTask.configuration.allowAccessModification, "Fresh task allowAccessModification should be true");
    }

    @Test
    public void testAllowaccessmodification_afterManualAllowAccessModificationTrue() throws Exception {
        task.configuration.allowAccessModification = true;
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");

        task.allowaccessmodification();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain true");
    }

    @Test
    public void testAllowaccessmodification_equivalentToGetallowaccessmodificationMethod() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard3", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard4", ProGuardTask.class);

        // Using allowaccessmodification()
        task1.allowaccessmodification();

        // Using getallowaccessmodification() (Groovy DSL method)
        task2.getallowaccessmodification();

        assertEquals(task1.configuration.allowAccessModification, task2.configuration.allowAccessModification,
                "Both methods should have same effect on allowAccessModification setting");
    }

    // Interaction with optimization settings

    @Test
    public void testAllowaccessmodification_enablesAccessModificationFlag() throws Exception {
        assertFalse(task.configuration.allowAccessModification, "allowAccessModification should be disabled by default");
        task.allowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled after allowaccessmodification()");
    }

    @Test
    public void testAllowaccessmodification_withOptimizationPasses() throws Exception {
        task.allowaccessmodification();
        task.optimizationpasses(5);
        task.keep("class com.example.MyClass { *; }");

        assertTrue(task.configuration.allowAccessModification,
                "allowAccessModification should be true with optimization passes");
    }

    @Test
    public void testAllowaccessmodification_complexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.allowaccessmodification();
        task.optimizations("!code/simplification/arithmetic");
        task.printseeds("seeds.txt");
        task.verbose();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true in complex config");
    }

    // Multiple optimization-related methods

    @Test
    public void testAllowaccessmodification_withOtherOptimizationFlags() throws Exception {
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testAllowaccessmodification_doesNotDisableAccessModification() throws Exception {
        task.allowaccessmodification();
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");

        // Call other methods
        task.keep("class com.example.MyClass { *; }");
        task.optimizationpasses(5);

        // Verify allowAccessModification is still true
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should still be true");
    }

    @Test
    public void testAllowaccessmodification_withAllOptimizationMethods() throws Exception {
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(7);
        task.optimizations("!method/inlining/*");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(7, task.configuration.optimizationPasses, "optimizationPasses should be 7");
        assertNotNull(task.configuration.optimizations, "optimizations should be set");
    }

    @Test
    public void testAllowaccessmodification_worksWith_dontOptimize() throws Exception {
        // Even when optimization is disabled, access modification can be enabled
        task.dontoptimize();
        task.allowaccessmodification();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testAllowaccessmodification_withObfuscation() throws Exception {
        task.allowaccessmodification();
        task.keep("class com.example.MyClass { *; }");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertTrue(task.configuration.obfuscate, "obfuscate should be enabled by default");
    }

    @Test
    public void testAllowaccessmodification_withoutObfuscation() throws Exception {
        task.dontobfuscate();
        task.allowaccessmodification();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testAllowaccessmodification_withShrinkingOnly() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        task.allowaccessmodification();

        assertTrue(task.configuration.shrink, "shrink should be enabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    // Android-specific scenarios

    @Test
    public void testAllowaccessmodification_androidReleaseOptimization() throws Exception {
        // Android release build with aggressive optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/apk/release");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.keep("class com.example.MainActivity { *; }");
        task.printmapping("mapping.txt");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testAllowaccessmodification_androidLibraryConfiguration() throws Exception {
        // Android library with optimization
        task.injars("build/intermediates/aar_main_jar");
        task.outjars("build/outputs/aar");
        task.allowaccessmodification();
        task.keep("public class * { public *; }");
        task.optimizationpasses(3);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }

    @Test
    public void testAllowaccessmodification_androidMultiModuleApp() throws Exception {
        // Multi-module Android app with access modification
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.allowaccessmodification();
        task.keep("class com.example.module.** { *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }

    // Performance optimization scenarios

    @Test
    public void testAllowaccessmodification_maximumOptimization() throws Exception {
        // Maximum optimization configuration
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.optimizationpasses(10);
        task.optimizations("*");
        task.overloadaggressively();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be enabled");
    }

    @Test
    public void testAllowaccessmodification_moderateOptimization() throws Exception {
        // Moderate optimization configuration
        task.allowaccessmodification();
        task.optimizationpasses(3);
        task.keep("class com.example.** { *; }");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertEquals(3, task.configuration.optimizationPasses, "optimizationPasses should be 3");
    }

    @Test
    public void testAllowaccessmodification_minimalOptimization() throws Exception {
        // Minimal optimization with just access modification
        task.allowaccessmodification();
        task.optimizationpasses(1);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertEquals(1, task.configuration.optimizationPasses, "optimizationPasses should be 1");
    }

    // Compatibility scenarios

    @Test
    public void testAllowaccessmodification_withReflectionConfiguration() throws Exception {
        // Configuration that uses reflection but allows access modification
        task.allowaccessmodification();
        task.keep("class * { public <methods>; }");
        task.keepclassmembers("class * { @com.example.KeepThis *; }");

        assertTrue(task.configuration.allowAccessModification,
                "allowAccessModification should be enabled even with reflection");
    }

    @Test
    public void testAllowaccessmodification_withSerializableClasses() throws Exception {
        // Configuration for serializable classes with access modification
        task.allowaccessmodification();
        task.keep("class * implements java.io.Serializable { *; }");
        task.keepclassmembers("class * implements java.io.Serializable { private <fields>; }");

        assertTrue(task.configuration.allowAccessModification,
                "allowAccessModification should be enabled");
    }

    @Test
    public void testAllowaccessmodification_libraryModeWithPublicAPI() throws Exception {
        // Library mode where only public API is preserved
        task.allowaccessmodification();
        task.keep("public class * { public *; }");
        task.keep("public interface * { *; }");

        assertTrue(task.configuration.allowAccessModification,
                "allowAccessModification should be enabled for library");
    }

    // Combination scenarios

    @Test
    public void testAllowaccessmodification_withKeepAttributes() throws Exception {
        // Access modification with keep attributes
        task.allowaccessmodification();
        task.keepattributes("Signature,*Annotation*,InnerClasses,EnclosingMethod");
        task.keep("class com.example.** { *; }");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
    }

    @Test
    public void testAllowaccessmodification_withRepackageClasses() throws Exception {
        // Access modification with class repackaging
        task.allowaccessmodification();
        task.repackageclasses("");
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
    }

    @Test
    public void testAllowaccessmodification_withFlattenPackageHierarchy() throws Exception {
        // Access modification with package flattening
        task.allowaccessmodification();
        task.flattenpackagehierarchy("com.example.flat");
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
    }

    // Output and reporting

    @Test
    public void testAllowaccessmodification_withPrintConfiguration() throws Exception {
        // Access modification with configuration printing
        task.allowaccessmodification();
        task.printconfiguration("configuration.txt");
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertEquals("configuration.txt", task.configuration.printConfiguration.getName());
    }

    @Test
    public void testAllowaccessmodification_withPrintMapping() throws Exception {
        // Access modification with mapping output
        task.allowaccessmodification();
        task.printmapping("mapping.txt");
        task.optimizationpasses(5);

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    @Test
    public void testAllowaccessmodification_completeOutputConfiguration() throws Exception {
        // Complete configuration with all output files
        task.allowaccessmodification();
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");
        task.printmapping("mapping.txt");
        task.printconfiguration("configuration.txt");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }

    // Build variant scenarios

    @Test
    public void testAllowaccessmodification_productionRelease() throws Exception {
        // Production release with maximum optimization
        task.injars("build/classes/production");
        task.outjars("build/release/app.jar");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.overloadaggressively();
        task.optimizationpasses(7);
        task.repackageclasses("");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be enabled");
    }

    @Test
    public void testAllowaccessmodification_stagingBuild() throws Exception {
        // Staging build with moderate optimization
        task.injars("build/classes/staging");
        task.outjars("build/staging/app.jar");
        task.allowaccessmodification();
        task.optimizationpasses(3);
        task.printmapping("staging-mapping.txt");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }

    @Test
    public void testAllowaccessmodification_betaBuild() throws Exception {
        // Beta build with optimization but preserved line numbers
        task.injars("build/classes/beta");
        task.outjars("build/beta/app.jar");
        task.allowaccessmodification();
        task.optimizationpasses(3);
        task.keepattributes("LineNumberTable,SourceFile");

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }
}

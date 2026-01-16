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
 * Test class for ProGuardTask.getmergeinterfacesaggressively()Ljava/lang/Object; method.
 * Tests the getmergeinterfacesaggressively() Groovy DSL method that returns null and calls mergeinterfacesaggressively()
 * to set configuration.mergeInterfacesAggressively to true.
 */
public class ProGuardTaskClaude_getmergeinterfacesaggressivelyTest {

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
    public void testGetmergeinterfacesaggressively_returnsNull() throws Exception {
        Object result = task.getmergeinterfacesaggressively();
        assertNull(result, "getmergeinterfacesaggressively() should return null");
    }

    @Test
    public void testGetmergeinterfacesaggressively_setsMergeInterfacesAggressivelyToTrue() throws Exception {
        assertFalse(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should initially be false");
        task.getmergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be set to true");
    }

    @Test
    public void testGetmergeinterfacesaggressively_returnsNullAndSetsMergeInterfacesAggressively() throws Exception {
        assertFalse(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should initially be false");
        Object result = task.getmergeinterfacesaggressively();
        assertNull(result, "getmergeinterfacesaggressively() should return null");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be set to true");
    }

    // Multiple calls tests

    @Test
    public void testGetmergeinterfacesaggressively_multipleCallsAreIdempotent() throws Exception {
        task.getmergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "First call should set mergeInterfacesAggressively to true");

        task.getmergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "Second call should keep mergeInterfacesAggressively as true");

        task.getmergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "Third call should keep mergeInterfacesAggressively as true");
    }

    @Test
    public void testGetmergeinterfacesaggressively_alwaysReturnsNull() throws Exception {
        Object result1 = task.getmergeinterfacesaggressively();
        Object result2 = task.getmergeinterfacesaggressively();
        Object result3 = task.getmergeinterfacesaggressively();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetmergeinterfacesaggressively_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getmergeinterfacesaggressively();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true after call " + (i + 1));
        }
    }

    // Integration with other configuration methods

    @Test
    public void testGetmergeinterfacesaggressively_worksWithAllowAccessModification() throws Exception {
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
    }

    @Test
    public void testGetmergeinterfacesaggressively_worksWithOptimizationSettings() throws Exception {
        task.getmergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
        assertNotNull(task.configuration.optimizations, "optimizations should be set");
    }

    @Test
    public void testGetmergeinterfacesaggressively_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.getmergeinterfacesaggressively();

        assertNotNull(task.configuration.keep, "keep rules should be configured");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetmergeinterfacesaggressively_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.getmergeinterfacesaggressively();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetmergeinterfacesaggressively_worksWithPrintOptions() throws Exception {
        task.printusage("usage.txt");
        task.printseeds("seeds.txt");
        task.getmergeinterfacesaggressively();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals("usage.txt", task.configuration.printUsage.getName());
        assertEquals("seeds.txt", task.configuration.printSeeds.getName());
    }

    @Test
    public void testGetmergeinterfacesaggressively_worksWithVerbose() throws Exception {
        task.verbose();
        task.getmergeinterfacesaggressively();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // Call order flexibility

    @Test
    public void testGetmergeinterfacesaggressively_canBeCalledFirst() throws Exception {
        task.getmergeinterfacesaggressively();
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testGetmergeinterfacesaggressively_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);
        task.getmergeinterfacesaggressively();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testGetmergeinterfacesaggressively_canBeCalledBetweenOtherMethods() throws Exception {
        task.keep("public class * { public *; }");
        task.getmergeinterfacesaggressively();
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    // Realistic usage scenarios

    @Test
    public void testGetmergeinterfacesaggressively_androidReleaseBuild() throws Exception {
        // Release builds often enable aggressive interface merging for better optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(5);
        task.keep("class com.example.MainActivity { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled for release build");
    }

    @Test
    public void testGetmergeinterfacesaggressively_aggressiveOptimizationScenario() throws Exception {
        // Aggressive optimization with interface merging and access modification
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(7);
        task.optimizations("!code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }

    @Test
    public void testGetmergeinterfacesaggressively_libraryOptimization() throws Exception {
        // Library optimization scenario
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-optimized.jar");
        task.getmergeinterfacesaggressively();
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled for library");
    }

    @Test
    public void testGetmergeinterfacesaggressively_completeReleaseConfiguration() throws Exception {
        // Complete release configuration
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(5);
        task.printmapping("mapping.txt");
        task.verbose();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }

    @Test
    public void testGetmergeinterfacesaggressively_optimizationWithShrinking() throws Exception {
        // Optimization with shrinking enabled
        task.getmergeinterfacesaggressively();
        task.keep("class com.example.** { *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.shrink, "shrink should be enabled by default");
        assertTrue(task.configuration.optimize, "optimize should be enabled by default");
    }

    @Test
    public void testGetmergeinterfacesaggressively_maximumCodeReduction() throws Exception {
        // Maximum code reduction configuration
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();
        task.overloadaggressively();
        task.optimizationpasses(7);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be enabled");
    }

    // Edge cases and validation

    @Test
    public void testGetmergeinterfacesaggressively_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.getmergeinterfacesaggressively();

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetmergeinterfacesaggressively_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.getmergeinterfacesaggressively();
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testGetmergeinterfacesaggressively_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertFalse(freshTask.configuration.mergeInterfacesAggressively, "Fresh task mergeInterfacesAggressively should be false");

        Object result = freshTask.getmergeinterfacesaggressively();

        assertNull(result, "Should return null");
        assertTrue(freshTask.configuration.mergeInterfacesAggressively, "Fresh task mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetmergeinterfacesaggressively_afterManualMergeInterfacesAggressivelyTrue() throws Exception {
        task.configuration.mergeInterfacesAggressively = true;
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");

        Object result = task.getmergeinterfacesaggressively();

        assertNull(result, "Should return null");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain true");
    }

    @Test
    public void testGetmergeinterfacesaggressively_consistentWithMergeinterfacesaggressivelyMethod() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard3", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard4", ProGuardTask.class);

        // Using getmergeinterfacesaggressively()
        task1.getmergeinterfacesaggressively();

        // Using mergeinterfacesaggressively() directly
        task2.mergeinterfacesaggressively();

        assertEquals(task1.configuration.mergeInterfacesAggressively, task2.configuration.mergeInterfacesAggressively,
                "Both methods should have same effect on mergeInterfacesAggressively setting");
    }

    // Interaction with optimization settings

    @Test
    public void testGetmergeinterfacesaggressively_enablesInterfaceMerging() throws Exception {
        assertFalse(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be disabled by default");
        task.getmergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled after getmergeinterfacesaggressively()");
    }

    @Test
    public void testGetmergeinterfacesaggressively_withOptimizationPasses() throws Exception {
        task.getmergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.keep("class com.example.MyClass { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be true with optimization passes");
    }

    @Test
    public void testGetmergeinterfacesaggressively_complexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.getmergeinterfacesaggressively();
        task.optimizations("!code/simplification/arithmetic");
        task.printseeds("seeds.txt");
        task.verbose();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true in complex config");
    }

    // Groovy DSL compatibility

    @Test
    public void testGetmergeinterfacesaggressively_groovyDslSupport() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        // In Groovy: mergeinterfacesaggressively (without parentheses) calls getmergeinterfacesaggressively()
        Object result = task.getmergeinterfacesaggressively();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertTrue(task.configuration.mergeInterfacesAggressively, "Should enable interface merging");
    }

    @Test
    public void testGetmergeinterfacesaggressively_returnsNullForChaining() throws Exception {
        // Even though it returns null, verify the method completes successfully
        Object result = task.getmergeinterfacesaggressively();

        assertNull(result, "Should return null");
        // Verify we can still call other methods after
        task.optimizationpasses(5);
        assertEquals(5, task.configuration.optimizationPasses, "Should be able to call other methods after");
    }

    // Multiple optimization-related methods

    @Test
    public void testGetmergeinterfacesaggressively_withOtherOptimizationFlags() throws Exception {
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testGetmergeinterfacesaggressively_doesNotDisableInterfaceMerging() throws Exception {
        task.getmergeinterfacesaggressively();
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");

        // Call other methods
        task.keep("class com.example.MyClass { *; }");
        task.optimizationpasses(5);

        // Verify mergeInterfacesAggressively is still true
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should still be true");
    }

    @Test
    public void testGetmergeinterfacesaggressively_withAllOptimizationMethods() throws Exception {
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(7);
        task.optimizations("!method/inlining/*");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be true");
        assertEquals(7, task.configuration.optimizationPasses, "optimizationPasses should be 7");
        assertNotNull(task.configuration.optimizations, "optimizations should be set");
    }

    @Test
    public void testGetmergeinterfacesaggressively_worksWith_dontOptimize() throws Exception {
        // Even when optimization is disabled, interface merging can be enabled
        task.dontoptimize();
        task.getmergeinterfacesaggressively();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetmergeinterfacesaggressively_withObfuscation() throws Exception {
        task.getmergeinterfacesaggressively();
        task.keep("class com.example.MyClass { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
        assertTrue(task.configuration.obfuscate, "obfuscate should be enabled by default");
    }

    @Test
    public void testGetmergeinterfacesaggressively_withoutObfuscation() throws Exception {
        task.dontobfuscate();
        task.getmergeinterfacesaggressively();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    @Test
    public void testGetmergeinterfacesaggressively_withShrinkingOnly() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        task.getmergeinterfacesaggressively();

        assertTrue(task.configuration.shrink, "shrink should be enabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    // Android-specific scenarios

    @Test
    public void testGetmergeinterfacesaggressively_androidReleaseOptimization() throws Exception {
        // Android release build with aggressive optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/apk/release");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(5);
        task.keep("class com.example.MainActivity { *; }");
        task.printmapping("mapping.txt");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
    }

    @Test
    public void testGetmergeinterfacesaggressively_androidLibraryConfiguration() throws Exception {
        // Android library with optimization
        task.injars("build/intermediates/aar_main_jar");
        task.outjars("build/outputs/aar");
        task.getmergeinterfacesaggressively();
        task.keep("public class * { public *; }");
        task.optimizationpasses(3);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testGetmergeinterfacesaggressively_androidMultiModuleApp() throws Exception {
        // Multi-module Android app with interface merging
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.getmergeinterfacesaggressively();
        task.keep("class com.example.module.** { *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    // Performance optimization scenarios

    @Test
    public void testGetmergeinterfacesaggressively_maximumOptimization() throws Exception {
        // Maximum optimization configuration
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(10);
        task.optimizations("*");
        task.overloadaggressively();

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be enabled");
    }

    @Test
    public void testGetmergeinterfacesaggressively_moderateOptimization() throws Exception {
        // Moderate optimization configuration
        task.getmergeinterfacesaggressively();
        task.optimizationpasses(3);
        task.keep("class com.example.** { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertEquals(3, task.configuration.optimizationPasses, "optimizationPasses should be 3");
    }

    @Test
    public void testGetmergeinterfacesaggressively_minimalOptimization() throws Exception {
        // Minimal optimization with just interface merging
        task.getmergeinterfacesaggressively();
        task.optimizationpasses(1);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertEquals(1, task.configuration.optimizationPasses, "optimizationPasses should be 1");
    }

    // Compatibility scenarios

    @Test
    public void testGetmergeinterfacesaggressively_withInterfaceKeepRules() throws Exception {
        // Configuration with interface keep rules
        task.getmergeinterfacesaggressively();
        task.keep("interface * { *; }");
        task.keep("class * implements com.example.MyInterface { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testGetmergeinterfacesaggressively_withReflectionConfiguration() throws Exception {
        // Configuration that uses reflection with interface merging
        task.getmergeinterfacesaggressively();
        task.keep("class * { public <methods>; }");
        task.keepclassmembers("class * { @com.example.KeepThis *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testGetmergeinterfacesaggressively_libraryModeWithPublicAPI() throws Exception {
        // Library mode where interfaces are part of public API
        task.getmergeinterfacesaggressively();
        task.keep("public interface * { *; }");
        task.keep("public class * { public *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled for library");
    }

    // Combination scenarios

    @Test
    public void testGetmergeinterfacesaggressively_withKeepAttributes() throws Exception {
        // Interface merging with keep attributes
        task.getmergeinterfacesaggressively();
        task.keepattributes("Signature,*Annotation*,InnerClasses,EnclosingMethod");
        task.keep("class com.example.** { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
    }

    @Test
    public void testGetmergeinterfacesaggressively_withRepackageClasses() throws Exception {
        // Interface merging with class repackaging
        task.getmergeinterfacesaggressively();
        task.repackageclasses("");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
    }

    @Test
    public void testGetmergeinterfacesaggressively_withFlattenPackageHierarchy() throws Exception {
        // Interface merging with package flattening
        task.getmergeinterfacesaggressively();
        task.flattenpackagehierarchy("com.example.flat");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
    }

    // Output and reporting

    @Test
    public void testGetmergeinterfacesaggressively_withPrintConfiguration() throws Exception {
        // Interface merging with configuration printing
        task.getmergeinterfacesaggressively();
        task.printconfiguration("configuration.txt");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertEquals("configuration.txt", task.configuration.printConfiguration.getName());
    }

    @Test
    public void testGetmergeinterfacesaggressively_withPrintMapping() throws Exception {
        // Interface merging with mapping output
        task.getmergeinterfacesaggressively();
        task.printmapping("mapping.txt");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    @Test
    public void testGetmergeinterfacesaggressively_completeOutputConfiguration() throws Exception {
        // Complete configuration with all output files
        task.getmergeinterfacesaggressively();
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");
        task.printmapping("mapping.txt");
        task.printconfiguration("configuration.txt");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    // Build variant scenarios

    @Test
    public void testGetmergeinterfacesaggressively_productionRelease() throws Exception {
        // Production release with maximum optimization
        task.injars("build/classes/production");
        task.outjars("build/release/app.jar");
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();
        task.overloadaggressively();
        task.optimizationpasses(7);
        task.repackageclasses("");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.overloadAggressively, "overloadAggressively should be enabled");
    }

    @Test
    public void testGetmergeinterfacesaggressively_stagingBuild() throws Exception {
        // Staging build with moderate optimization
        task.injars("build/classes/staging");
        task.outjars("build/staging/app.jar");
        task.getmergeinterfacesaggressively();
        task.optimizationpasses(3);
        task.printmapping("staging-mapping.txt");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    @Test
    public void testGetmergeinterfacesaggressively_betaBuild() throws Exception {
        // Beta build with optimization but preserved line numbers
        task.injars("build/classes/beta");
        task.outjars("build/beta/app.jar");
        task.getmergeinterfacesaggressively();
        task.optimizationpasses(3);
        task.keepattributes("LineNumberTable,SourceFile");

        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
    }

    // Interface-specific scenarios

    @Test
    public void testGetmergeinterfacesaggressively_manyInterfacesScenario() throws Exception {
        // Scenario with many interfaces that can benefit from merging
        task.getmergeinterfacesaggressively();
        task.optimizationpasses(5);
        task.keep("class com.example.impl.** { *; }");

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled for interface-heavy code");
    }

    @Test
    public void testGetmergeinterfacesaggressively_strategyPatternOptimization() throws Exception {
        // Optimization for strategy pattern with multiple interface implementations
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();
        task.keep("class com.example.strategy.** { *; }");
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled for strategy pattern");
    }

    @Test
    public void testGetmergeinterfacesaggressively_adapterPatternOptimization() throws Exception {
        // Optimization for adapter pattern with interface hierarchies
        task.getmergeinterfacesaggressively();
        task.allowaccessmodification();
        task.optimizationpasses(5);

        assertTrue(task.configuration.mergeInterfacesAggressively,
                "mergeInterfacesAggressively should be enabled for adapter pattern");
    }
}

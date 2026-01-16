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
 * Test class for ProGuardTask.dontobfuscate()V method.
 * Tests the dontobfuscate() void method that sets configuration.obfuscate to false,
 * disabling ProGuard's obfuscation phase which renames classes, fields, and methods.
 */
public class ProGuardTaskClaude_dontobfuscateTest {

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
    public void testDontobfuscate_setsObfuscateToFalse() throws Exception {
        assertTrue(task.configuration.obfuscate, "obfuscate should initially be true");
        task.dontobfuscate();
        assertFalse(task.configuration.obfuscate, "obfuscate should be set to false");
    }

    @Test
    public void testDontobfuscate_disablesObfuscation() throws Exception {
        assertTrue(task.configuration.obfuscate, "obfuscate should be enabled by default");
        task.dontobfuscate();
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testDontobfuscate_doesNotReturnValue() throws Exception {
        // Verify method is void and completes successfully
        task.dontobfuscate();
        assertFalse(task.configuration.obfuscate, "obfuscate should be false after call");
    }

    // Multiple calls tests

    @Test
    public void testDontobfuscate_multipleCallsAreIdempotent() throws Exception {
        task.dontobfuscate();
        assertFalse(task.configuration.obfuscate, "First call should set obfuscate to false");

        task.dontobfuscate();
        assertFalse(task.configuration.obfuscate, "Second call should keep obfuscate as false");

        task.dontobfuscate();
        assertFalse(task.configuration.obfuscate, "Third call should keep obfuscate as false");
    }

    @Test
    public void testDontobfuscate_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.dontobfuscate();
            assertFalse(task.configuration.obfuscate, "obfuscate should be false after call " + (i + 1));
        }
    }

    // Integration with other configuration methods

    @Test
    public void testDontobfuscate_worksWithOtherDontMethods() throws Exception {
        task.dontobfuscate();
        task.dontshrink();
        task.dontoptimize();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testDontobfuscate_worksWithDontShrink() throws Exception {
        task.dontshrink();
        assertTrue(task.configuration.obfuscate, "obfuscate should still be true");

        task.dontobfuscate();
        assertFalse(task.configuration.obfuscate, "obfuscate should now be false");
        assertFalse(task.configuration.shrink, "shrink should remain false");
    }

    @Test
    public void testDontobfuscate_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.dontobfuscate();

        assertNotNull(task.configuration.keep, "keep rules should be configured");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testDontobfuscate_worksWithPrintOptions() throws Exception {
        task.printusage("usage.txt");
        task.printseeds("seeds.txt");
        task.dontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertEquals("usage.txt", task.configuration.printUsage.getName());
        assertEquals("seeds.txt", task.configuration.printSeeds.getName());
    }

    @Test
    public void testDontobfuscate_worksWithVerbose() throws Exception {
        task.verbose();
        task.dontobfuscate();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testDontobfuscate_worksWithPrintMapping() throws Exception {
        task.printmapping("mapping.txt");
        task.dontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    // Call order flexibility

    @Test
    public void testDontobfuscate_canBeCalledFirst() throws Exception {
        task.dontobfuscate();
        task.keep("public class * { public *; }");
        task.dontshrink();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testDontobfuscate_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.dontshrink();
        task.dontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testDontobfuscate_canBeCalledBetweenOtherMethods() throws Exception {
        task.keep("public class * { public *; }");
        task.dontobfuscate();
        task.dontshrink();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    // Realistic usage scenarios

    @Test
    public void testDontobfuscate_androidDebugBuild() throws Exception {
        // Debug builds typically disable obfuscation for easier debugging
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.dontobfuscate();
        task.keep("class com.example.MainActivity { *; }");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled for debug build");
    }

    @Test
    public void testDontobfuscate_testingScenario() throws Exception {
        // Testing scenario where obfuscation is disabled for easier debugging
        task.dontobfuscate();
        task.keep("class com.example.** { *; }");
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled for testing");
    }

    @Test
    public void testDontobfuscate_onlyShrinkingScenario() throws Exception {
        // Scenario where only shrinking is needed, not obfuscation
        task.dontshrink();
        task.dontobfuscate();
        task.dontoptimize();

        assertFalse(task.configuration.shrink, "shrink should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testDontobfuscate_analysisOnlyMode() throws Exception {
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
    public void testDontobfuscate_libraryConfiguration() throws Exception {
        // Library configuration where obfuscation might cause issues with public API
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-processed.jar");
        task.dontobfuscate();
        task.keep("public class * { public *; }");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled for library");
    }

    @Test
    public void testDontobfuscate_shrinkOnlyBuild() throws Exception {
        // Build that only shrinks code without obfuscation or optimization
        task.injars("build/classes");
        task.outjars("build/outputs");
        task.dontobfuscate();
        task.dontoptimize();
        task.keep("public class * { public *; }");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.shrink, "shrink should remain enabled");
    }

    @Test
    public void testDontobfuscate_quickBuildConfiguration() throws Exception {
        // Quick build for development with minimal processing
        task.injars("build/classes");
        task.outjars("build/outputs");
        task.dontshrink();
        task.dontobfuscate();
        task.dontpreverify();

        assertFalse(task.configuration.shrink, "shrink should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertFalse(task.configuration.preverify, "preverify should be disabled");
    }

    // Edge cases and validation

    @Test
    public void testDontobfuscate_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;

        task.dontobfuscate();

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testDontobfuscate_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.dontobfuscate();
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testDontobfuscate_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertTrue(freshTask.configuration.obfuscate, "Fresh task obfuscate should be true");

        freshTask.dontobfuscate();

        assertFalse(freshTask.configuration.obfuscate, "Fresh task obfuscate should be false");
    }

    @Test
    public void testDontobfuscate_afterManualObfuscateFalse() throws Exception {
        task.configuration.obfuscate = false;
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");

        task.dontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should remain false");
    }

    @Test
    public void testDontobfuscate_equivalentToGetdontobfuscateMethod() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard3", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard4", ProGuardTask.class);

        // Using dontobfuscate()
        task1.dontobfuscate();

        // Using getdontobfuscate() (Groovy DSL method)
        task2.getdontobfuscate();

        assertEquals(task1.configuration.obfuscate, task2.configuration.obfuscate,
                "Both methods should have same effect on obfuscate setting");
    }

    // Interaction with obfuscation settings

    @Test
    public void testDontobfuscate_disablesObfuscationPass() throws Exception {
        assertTrue(task.configuration.obfuscate, "obfuscate should be enabled by default");
        task.dontobfuscate();
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled after dontobfuscate()");
    }

    @Test
    public void testDontobfuscate_withKeepRules() throws Exception {
        task.dontobfuscate();
        task.keep("class com.example.MyClass { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be false even with keep rules");
    }

    @Test
    public void testDontobfuscate_complexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.dontobfuscate();
        task.printseeds("seeds.txt");
        task.verbose();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false in complex config");
    }

    // Multiple dont* methods

    @Test
    public void testDontobfuscate_withAllDontMethods() throws Exception {
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
    public void testDontobfuscate_doesNotReenableObfuscation() throws Exception {
        task.dontobfuscate();
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");

        // Call other methods
        task.keep("class com.example.MyClass { *; }");
        task.dontshrink();

        // Verify obfuscate is still false
        assertFalse(task.configuration.obfuscate, "obfuscate should still be false");
    }

    // Android-specific scenarios

    @Test
    public void testDontobfuscate_androidDebugWithShrinking() throws Exception {
        // Debug build that shrinks but doesn't obfuscate
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.dontobfuscate();
        task.keep("class com.example.MainActivity { *; }");

        assertTrue(task.configuration.shrink, "shrink should be enabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontobfuscate_androidReleaseWithoutObfuscation() throws Exception {
        // Release build that disables obfuscation but keeps shrinking and optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.dontobfuscate();
        task.keep("class com.example.MainActivity { *; }");
        task.printmapping("mapping.txt");

        assertTrue(task.configuration.shrink, "shrink should be enabled");
        assertTrue(task.configuration.optimize, "optimize should be enabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontobfuscate_androidMultiDexScenario() throws Exception {
        // Multi-dex scenario where obfuscation is disabled to avoid issues
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.dontobfuscate();
        task.keep("class android.support.multidex.** { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for multi-dex");
    }

    @Test
    public void testDontobfuscate_androidLibraryBuild() throws Exception {
        // Library build where public API shouldn't be obfuscated
        task.injars("build/intermediates/aar_main_jar");
        task.outjars("build/outputs/aar");
        task.dontobfuscate();
        task.keep("public class * { public *; }");
        task.keep("public interface * { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for library");
    }

    @Test
    public void testDontobfuscate_androidTestConfiguration() throws Exception {
        // Test configuration where code needs to be readable
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/test");
        task.dontobfuscate();
        task.keep("class com.example.test.** { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for tests");
    }

    // Performance and build time scenarios

    @Test
    public void testDontobfuscate_fastIncrementalBuild() throws Exception {
        // Fast incremental build where obfuscation is skipped
        task.injars("build/classes");
        task.outjars("build/outputs");
        task.dontobfuscate();
        task.dontoptimize();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled for fast builds");
        assertFalse(task.configuration.optimize, "optimize should be disabled for fast builds");
    }

    @Test
    public void testDontobfuscate_debuggingFriendlyConfiguration() throws Exception {
        // Configuration that preserves code structure for debugging
        task.dontobfuscate();
        task.keepattributes("LineNumberTable,SourceFile");
        task.keep("class com.example.** { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for debugging");
    }

    @Test
    public void testDontobfuscate_developmentProfile() throws Exception {
        // Development profile with minimal processing
        task.injars("build/classes/development");
        task.outjars("build/outputs/dev.jar");
        task.dontobfuscate();
        task.dontoptimize();
        task.keepattributes("*");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    // Compatibility scenarios

    @Test
    public void testDontobfuscate_compatibilityMode() throws Exception {
        // Compatibility mode where obfuscation might break reflection or serialization
        task.dontobfuscate();
        task.keep("class * implements java.io.Serializable { *; }");
        task.keepclassmembers("class * { private <fields>; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for compatibility");
    }

    @Test
    public void testDontobfuscate_reflectionHeavyCode() throws Exception {
        // Code that heavily uses reflection should not be obfuscated
        task.dontobfuscate();
        task.keep("class * { *; }");
        task.keepattributes("*Annotation*");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for reflection-heavy code");
    }

    @Test
    public void testDontobfuscate_publicApiLibrary() throws Exception {
        // Public API library where names must be preserved
        task.dontobfuscate();
        task.keep("public class * { public *; }");
        task.keep("public interface * { *; }");
        task.keep("public enum * { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for public API");
    }

    @Test
    public void testDontobfuscate_jniIntegration() throws Exception {
        // JNI integration where native methods shouldn't be obfuscated
        task.dontobfuscate();
        task.keepclasseswithmembers("class * { native <methods>; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for JNI");
    }

    @Test
    public void testDontobfuscate_webServiceClient() throws Exception {
        // Web service client where model classes shouldn't be obfuscated
        task.dontobfuscate();
        task.keep("class com.example.api.model.** { *; }");
        task.keepattributes("*Annotation*,Signature");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for web service clients");
    }

    // Mapping and tracing scenarios

    @Test
    public void testDontobfuscate_withPrintMappingNoObfuscation() throws Exception {
        // When obfuscation is disabled, mapping file is less useful but can still be generated
        task.dontobfuscate();
        task.printmapping("mapping.txt");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should still be set");
    }

    @Test
    public void testDontobfuscate_withApplymapping() throws Exception {
        // Apply existing mapping without obfuscating
        task.dontobfuscate();
        task.keep("class com.example.** { *; }");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontobfuscate_withObfuscationDictionary() throws Exception {
        // Obfuscation dictionary is irrelevant when obfuscation is disabled
        task.dontobfuscate();
        task.keep("class com.example.** { *; }");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    // Output and reporting

    @Test
    public void testDontobfuscate_withPrintConfiguration() throws Exception {
        // Disable obfuscation with configuration printing
        task.dontobfuscate();
        task.printconfiguration("configuration.txt");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertEquals("configuration.txt", task.configuration.printConfiguration.getName());
    }

    @Test
    public void testDontobfuscate_completeOutputConfiguration() throws Exception {
        // Complete configuration with all output files
        task.dontobfuscate();
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");
        task.printconfiguration("configuration.txt");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontobfuscate_withDump() throws Exception {
        // Disable obfuscation with class structure dump
        task.dontobfuscate();
        task.dump("structure.txt");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertEquals("structure.txt", task.configuration.dump.getName());
    }

    // Build variant scenarios

    @Test
    public void testDontobfuscate_developmentBuild() throws Exception {
        // Development build with no obfuscation for faster iterations
        task.injars("build/classes/development");
        task.outjars("build/outputs/dev.jar");
        task.dontobfuscate();
        task.dontoptimize();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testDontobfuscate_stagingBuild() throws Exception {
        // Staging build that may disable obfuscation for easier debugging
        task.injars("build/classes/staging");
        task.outjars("build/staging/app.jar");
        task.dontobfuscate();
        task.keepattributes("LineNumberTable,SourceFile");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontobfuscate_internalTestingBuild() throws Exception {
        // Internal testing build with readable names
        task.injars("build/classes/internal");
        task.outjars("build/internal/app.jar");
        task.dontobfuscate();
        task.keep("class com.example.** { *; }");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontobfuscate_betaBuild() throws Exception {
        // Beta build that shrinks and optimizes but doesn't obfuscate
        task.injars("build/classes/beta");
        task.outjars("build/beta/app.jar");
        task.dontobfuscate();
        task.optimizationpasses(3);
        task.keepattributes("LineNumberTable,SourceFile");

        assertTrue(task.configuration.shrink, "shrink should be enabled");
        assertTrue(task.configuration.optimize, "optimize should be enabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    // Keep rules interaction

    @Test
    public void testDontobfuscate_withKeepNames() throws Exception {
        // keepnames is about shrinking, not obfuscation
        task.keepnames("class com.example.** { *; }");
        task.dontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontobfuscate_withKeepClassMembers() throws Exception {
        // Keep class members without obfuscation
        task.keepclassmembers("class * { public <methods>; }");
        task.dontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontobfuscate_withKeepAttributes() throws Exception {
        // Keep attributes with disabled obfuscation
        task.keepattributes("Signature,*Annotation*,InnerClasses");
        task.dontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
    }

    @Test
    public void testDontobfuscate_withKeepClassesWithMembers() throws Exception {
        // Keep classes with specific members
        task.keepclasseswithmembers("class * { public static void main(java.lang.String[]); }");
        task.dontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    // Optimization interaction

    @Test
    public void testDontobfuscate_withOptimizationEnabled() throws Exception {
        // Can optimize without obfuscating
        task.optimizationpasses(5);
        task.dontobfuscate();

        assertTrue(task.configuration.optimize, "optimize should remain enabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testDontobfuscate_withOptimizationDisabled() throws Exception {
        // Both optimization and obfuscation disabled
        task.dontoptimize();
        task.dontobfuscate();

        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontobfuscate_shrinkAndOptimizeOnly() throws Exception {
        // Shrink and optimize but don't obfuscate
        task.optimizationpasses(3);
        task.dontobfuscate();
        task.keep("class com.example.** { *; }");

        assertTrue(task.configuration.shrink, "shrink should be enabled");
        assertTrue(task.configuration.optimize, "optimize should be enabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testDontobfuscate_withAggressiveOptimization() throws Exception {
        // Aggressive optimization without obfuscation
        task.optimizationpasses(7);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.dontobfuscate();

        assertTrue(task.configuration.optimize, "optimize should be enabled");
        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should be enabled");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should be enabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    // Package name scenarios

    @Test
    public void testDontobfuscate_withKeepPackageNames() throws Exception {
        // Keep package names without obfuscation
        task.keeppackagenames("com.example.**");
        task.dontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be set");
    }

    @Test
    public void testDontobfuscate_withRepackageClasses() throws Exception {
        // Repackage classes without obfuscating names
        task.repackageclasses("com.example.flat");
        task.dontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
    }

    @Test
    public void testDontobfuscate_withFlattenPackageHierarchy() throws Exception {
        // Flatten package hierarchy without obfuscation
        task.flattenpackagehierarchy("com.example.flat");
        task.dontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertNotNull(task.configuration.flattenPackageHierarchy, "flattenPackageHierarchy should be set");
    }

    // Special use cases

    @Test
    public void testDontobfuscate_frameworkIntegration() throws Exception {
        // Framework integration where reflection is used extensively
        task.dontobfuscate();
        task.keep("@org.springframework.** class * { *; }");
        task.keepattributes("*Annotation*");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for framework integration");
    }

    @Test
    public void testDontobfuscate_pluginArchitecture() throws Exception {
        // Plugin architecture where plugin interfaces must remain stable
        task.dontobfuscate();
        task.keep("interface com.example.plugin.** { *; }");
        task.keep("abstract class com.example.plugin.** { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for plugin architecture");
    }

    @Test
    public void testDontobfuscate_legacyCodeSupport() throws Exception {
        // Legacy code support where maintaining compatibility is crucial
        task.dontobfuscate();
        task.keep("class com.example.legacy.** { *; }");
        task.dontoptimize();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled for legacy code");
        assertFalse(task.configuration.optimize, "optimize should be disabled for legacy code");
    }
}

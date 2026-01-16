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
 * Test class for ProGuardTask.getdontobfuscate()Ljava/lang/Object; method.
 * Tests the getdontobfuscate() Groovy DSL method that returns null and calls dontobfuscate()
 * to set configuration.obfuscate to false.
 */
public class ProGuardTaskClaude_getdontobfuscateTest {

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
    public void testGetdontobfuscate_returnsNull() throws Exception {
        Object result = task.getdontobfuscate();
        assertNull(result, "getdontobfuscate() should return null");
    }

    @Test
    public void testGetdontobfuscate_setsObfuscateToFalse() throws Exception {
        assertTrue(task.configuration.obfuscate, "obfuscate should initially be true");
        task.getdontobfuscate();
        assertFalse(task.configuration.obfuscate, "obfuscate should be set to false");
    }

    @Test
    public void testGetdontobfuscate_returnsNullAndSetsObfuscate() throws Exception {
        assertTrue(task.configuration.obfuscate, "obfuscate should initially be true");
        Object result = task.getdontobfuscate();
        assertNull(result, "getdontobfuscate() should return null");
        assertFalse(task.configuration.obfuscate, "obfuscate should be set to false");
    }

    // Multiple calls tests

    @Test
    public void testGetdontobfuscate_multipleCallsAreIdempotent() throws Exception {
        task.getdontobfuscate();
        assertFalse(task.configuration.obfuscate, "First call should set obfuscate to false");

        task.getdontobfuscate();
        assertFalse(task.configuration.obfuscate, "Second call should keep obfuscate as false");

        task.getdontobfuscate();
        assertFalse(task.configuration.obfuscate, "Third call should keep obfuscate as false");
    }

    @Test
    public void testGetdontobfuscate_alwaysReturnsNull() throws Exception {
        Object result1 = task.getdontobfuscate();
        Object result2 = task.getdontobfuscate();
        Object result3 = task.getdontobfuscate();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetdontobfuscate_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getdontobfuscate();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertFalse(task.configuration.obfuscate, "obfuscate should be false after call " + (i + 1));
        }
    }

    // Integration with other configuration methods

    @Test
    public void testGetdontobfuscate_worksWithOtherDontMethods() throws Exception {
        task.getdontobfuscate();
        task.dontshrink();
        task.dontoptimize();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetdontobfuscate_worksWithDontShrink() throws Exception {
        task.dontshrink();
        assertTrue(task.configuration.obfuscate, "obfuscate should still be true");

        task.getdontobfuscate();
        assertFalse(task.configuration.obfuscate, "obfuscate should now be false");
        assertFalse(task.configuration.shrink, "shrink should remain false");
    }

    @Test
    public void testGetdontobfuscate_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.getdontobfuscate();

        assertNotNull(task.configuration.keep, "keep rules should be configured");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetdontobfuscate_worksWithPrintOptions() throws Exception {
        task.printusage("usage.txt");
        task.printseeds("seeds.txt");
        task.getdontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertEquals("usage.txt", task.configuration.printUsage.getName());
        assertEquals("seeds.txt", task.configuration.printSeeds.getName());
    }

    @Test
    public void testGetdontobfuscate_worksWithVerbose() throws Exception {
        task.verbose();
        task.getdontobfuscate();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetdontobfuscate_worksWithPrintMapping() throws Exception {
        task.printmapping("mapping.txt");
        task.getdontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    // Call order flexibility

    @Test
    public void testGetdontobfuscate_canBeCalledFirst() throws Exception {
        task.getdontobfuscate();
        task.keep("public class * { public *; }");
        task.dontshrink();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetdontobfuscate_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.dontshrink();
        task.getdontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetdontobfuscate_canBeCalledBetweenOtherMethods() throws Exception {
        task.keep("public class * { public *; }");
        task.getdontobfuscate();
        task.dontshrink();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    // Realistic usage scenarios

    @Test
    public void testGetdontobfuscate_androidDebugBuild() throws Exception {
        // Debug builds typically disable obfuscation for easier debugging
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.getdontobfuscate();
        task.keep("class com.example.MainActivity { *; }");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled for debug build");
    }

    @Test
    public void testGetdontobfuscate_testingScenario() throws Exception {
        // Testing scenario where obfuscation is disabled for easier debugging
        task.getdontobfuscate();
        task.keep("class com.example.** { *; }");
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled for testing");
    }

    @Test
    public void testGetdontobfuscate_onlyShrinkingScenario() throws Exception {
        // Scenario where only shrinking is needed, not obfuscation
        task.dontshrink();
        task.getdontobfuscate();
        task.dontoptimize();

        assertFalse(task.configuration.shrink, "shrink should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetdontobfuscate_analysisOnlyMode() throws Exception {
        // Mode where we only want to analyze, not process
        task.dontshrink();
        task.dontoptimize();
        task.getdontobfuscate();
        task.dontpreverify();
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");

        assertFalse(task.configuration.shrink, "shrink should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testGetdontobfuscate_libraryConfiguration() throws Exception {
        // Library configuration where obfuscation might cause issues with public API
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-processed.jar");
        task.getdontobfuscate();
        task.keep("public class * { public *; }");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled for library");
    }

    @Test
    public void testGetdontobfuscate_shrinkOnlyBuild() throws Exception {
        // Build that only shrinks code without obfuscation or optimization
        task.injars("build/classes");
        task.outjars("build/outputs");
        task.getdontobfuscate();
        task.dontoptimize();
        task.keep("public class * { public *; }");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.shrink, "shrink should remain enabled");
    }

    // Edge cases and validation

    @Test
    public void testGetdontobfuscate_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;

        task.getdontobfuscate();

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetdontobfuscate_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.getdontobfuscate();
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testGetdontobfuscate_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertTrue(freshTask.configuration.obfuscate, "Fresh task obfuscate should be true");

        Object result = freshTask.getdontobfuscate();

        assertNull(result, "Should return null");
        assertFalse(freshTask.configuration.obfuscate, "Fresh task obfuscate should be false");
    }

    @Test
    public void testGetdontobfuscate_afterManualObfuscateFalse() throws Exception {
        task.configuration.obfuscate = false;
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");

        Object result = task.getdontobfuscate();

        assertNull(result, "Should return null");
        assertFalse(task.configuration.obfuscate, "obfuscate should remain false");
    }

    @Test
    public void testGetdontobfuscate_consistentWithDontobfuscateMethod() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard3", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard4", ProGuardTask.class);

        // Using getdontobfuscate()
        task1.getdontobfuscate();

        // Using dontobfuscate() directly
        task2.dontobfuscate();

        assertEquals(task1.configuration.obfuscate, task2.configuration.obfuscate,
                "Both methods should have same effect on obfuscate setting");
    }

    // Interaction with obfuscation settings

    @Test
    public void testGetdontobfuscate_disablesObfuscationPass() throws Exception {
        assertTrue(task.configuration.obfuscate, "obfuscate should be enabled by default");
        task.getdontobfuscate();
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled after getdontobfuscate()");
    }

    @Test
    public void testGetdontobfuscate_withKeepRules() throws Exception {
        task.getdontobfuscate();
        task.keep("class com.example.MyClass { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be false even with keep rules");
    }

    @Test
    public void testGetdontobfuscate_complexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.getdontobfuscate();
        task.printseeds("seeds.txt");
        task.verbose();

        assertFalse(task.configuration.obfuscate, "obfuscate should be false in complex config");
    }

    // Groovy DSL compatibility

    @Test
    public void testGetdontobfuscate_groovyDslSupport() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        // In Groovy: dontobfuscate (without parentheses) calls getdontobfuscate()
        Object result = task.getdontobfuscate();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertFalse(task.configuration.obfuscate, "Should disable obfuscation");
    }

    @Test
    public void testGetdontobfuscate_returnsNullForChaining() throws Exception {
        // Even though it returns null, verify the method completes successfully
        Object result = task.getdontobfuscate();

        assertNull(result, "Should return null");
        // Verify we can still call other methods after
        task.dontshrink();
        assertFalse(task.configuration.shrink, "Should be able to call other methods after");
    }

    // Multiple dont* methods

    @Test
    public void testGetdontobfuscate_withAllDontMethods() throws Exception {
        task.dontshrink();
        task.dontoptimize();
        task.getdontobfuscate();
        task.dontpreverify();

        assertFalse(task.configuration.shrink, "shrink should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testGetdontobfuscate_doesNotReenableObfuscation() throws Exception {
        task.getdontobfuscate();
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");

        // Call other methods
        task.keep("class com.example.MyClass { *; }");
        task.dontshrink();

        // Verify obfuscate is still false
        assertFalse(task.configuration.obfuscate, "obfuscate should still be false");
    }

    // Android-specific scenarios

    @Test
    public void testGetdontobfuscate_androidDebugWithShrinking() throws Exception {
        // Debug build that shrinks but doesn't obfuscate
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.getdontobfuscate();
        task.keep("class com.example.MainActivity { *; }");

        assertTrue(task.configuration.shrink, "shrink should be enabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testGetdontobfuscate_androidLibraryBuild() throws Exception {
        // Library build where public API shouldn't be obfuscated
        task.injars("build/intermediates/aar_main_jar");
        task.outjars("build/outputs/aar");
        task.getdontobfuscate();
        task.keep("public class * { public *; }");
        task.keep("public interface * { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for library");
    }

    @Test
    public void testGetdontobfuscate_androidTestConfiguration() throws Exception {
        // Test configuration where code needs to be readable
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/test");
        task.getdontobfuscate();
        task.keep("class com.example.test.** { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for tests");
    }

    // Performance and build time scenarios

    @Test
    public void testGetdontobfuscate_fastIncrementalBuild() throws Exception {
        // Fast incremental build where obfuscation is skipped
        task.injars("build/classes");
        task.outjars("build/outputs");
        task.getdontobfuscate();
        task.dontoptimize();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled for fast builds");
        assertFalse(task.configuration.optimize, "optimize should be disabled for fast builds");
    }

    @Test
    public void testGetdontobfuscate_debuggingFriendlyConfiguration() throws Exception {
        // Configuration that preserves code structure for debugging
        task.getdontobfuscate();
        task.keepattributes("LineNumberTable,SourceFile");
        task.keep("class com.example.** { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for debugging");
    }

    // Compatibility scenarios

    @Test
    public void testGetdontobfuscate_compatibilityMode() throws Exception {
        // Compatibility mode where obfuscation might break reflection or serialization
        task.getdontobfuscate();
        task.keep("class * implements java.io.Serializable { *; }");
        task.keepclassmembers("class * { private <fields>; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for compatibility");
    }

    @Test
    public void testGetdontobfuscate_reflectionHeavyCode() throws Exception {
        // Code that heavily uses reflection should not be obfuscated
        task.getdontobfuscate();
        task.keep("class * { *; }");
        task.keepattributes("*Annotation*");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for reflection-heavy code");
    }

    @Test
    public void testGetdontobfuscate_publicApiLibrary() throws Exception {
        // Public API library where names must be preserved
        task.getdontobfuscate();
        task.keep("public class * { public *; }");
        task.keep("public interface * { *; }");
        task.keep("public enum * { *; }");

        assertFalse(task.configuration.obfuscate,
                "obfuscate should be disabled for public API");
    }

    // Mapping and tracing scenarios

    @Test
    public void testGetdontobfuscate_withPrintMappingNoObfuscation() throws Exception {
        // When obfuscation is disabled, mapping file is less useful but can still be generated
        task.getdontobfuscate();
        task.printmapping("mapping.txt");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
                "printMapping should still be set");
    }

    @Test
    public void testGetdontobfuscate_withObfuscationDictionary() throws Exception {
        // Obfuscation dictionary is irrelevant when obfuscation is disabled
        task.getdontobfuscate();
        task.keep("class com.example.** { *; }");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    // Output and reporting

    @Test
    public void testGetdontobfuscate_withPrintConfiguration() throws Exception {
        // Disable obfuscation with configuration printing
        task.getdontobfuscate();
        task.printconfiguration("configuration.txt");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertEquals("configuration.txt", task.configuration.printConfiguration.getName());
    }

    @Test
    public void testGetdontobfuscate_completeOutputConfiguration() throws Exception {
        // Complete configuration with all output files
        task.getdontobfuscate();
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");
        task.printconfiguration("configuration.txt");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    // Build variant scenarios

    @Test
    public void testGetdontobfuscate_developmentBuild() throws Exception {
        // Development build with no obfuscation for faster iterations
        task.injars("build/classes/development");
        task.outjars("build/outputs/dev.jar");
        task.getdontobfuscate();
        task.dontoptimize();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetdontobfuscate_stagingBuild() throws Exception {
        // Staging build that may disable obfuscation for easier debugging
        task.injars("build/classes/staging");
        task.outjars("build/staging/app.jar");
        task.getdontobfuscate();
        task.keepattributes("LineNumberTable,SourceFile");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testGetdontobfuscate_internalTestingBuild() throws Exception {
        // Internal testing build with readable names
        task.injars("build/classes/internal");
        task.outjars("build/internal/app.jar");
        task.getdontobfuscate();
        task.keep("class com.example.** { *; }");

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    // Keep rules interaction

    @Test
    public void testGetdontobfuscate_withKeepNames() throws Exception {
        // keepnames is about shrinking, not obfuscation
        task.keepnames("class com.example.** { *; }");
        task.getdontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testGetdontobfuscate_withKeepClassMembers() throws Exception {
        // Keep class members without obfuscation
        task.keepclassmembers("class * { public <methods>; }");
        task.getdontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testGetdontobfuscate_withKeepAttributes() throws Exception {
        // Keep attributes with disabled obfuscation
        task.keepattributes("Signature,*Annotation*,InnerClasses");
        task.getdontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
    }

    // Optimization interaction

    @Test
    public void testGetdontobfuscate_withOptimizationEnabled() throws Exception {
        // Can optimize without obfuscating
        task.optimizationpasses(5);
        task.getdontobfuscate();

        assertTrue(task.configuration.optimize, "optimize should remain enabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertEquals(5, task.configuration.optimizationPasses, "optimizationPasses should be 5");
    }

    @Test
    public void testGetdontobfuscate_withOptimizationDisabled() throws Exception {
        // Both optimization and obfuscation disabled
        task.dontoptimize();
        task.getdontobfuscate();

        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testGetdontobfuscate_shrinkAndOptimizeOnly() throws Exception {
        // Shrink and optimize but don't obfuscate
        task.optimizationpasses(3);
        task.getdontobfuscate();
        task.keep("class com.example.** { *; }");

        assertTrue(task.configuration.shrink, "shrink should be enabled");
        assertTrue(task.configuration.optimize, "optimize should be enabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    // Package name scenarios

    @Test
    public void testGetdontobfuscate_withKeepPackageNames() throws Exception {
        // Keep package names without obfuscation
        task.keeppackagenames("com.example.**");
        task.getdontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames should be set");
    }

    @Test
    public void testGetdontobfuscate_withRepackageClasses() throws Exception {
        // Repackage classes without obfuscating names
        task.repackageclasses("com.example.flat");
        task.getdontobfuscate();

        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
        assertNotNull(task.configuration.repackageClasses, "repackageClasses should be set");
    }
}

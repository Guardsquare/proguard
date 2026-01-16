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
 * Test class for ProGuardTask.getdontoptimize()Ljava/lang/Object; method.
 * Tests the getdontoptimize() Groovy DSL method that returns null and calls dontoptimize()
 * to set configuration.optimize to false.
 */
public class ProGuardTaskClaude_getdontoptimizeTest {

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
    public void testGetdontoptimize_returnsNull() throws Exception {
        Object result = task.getdontoptimize();
        assertNull(result, "getdontoptimize() should return null");
    }

    @Test
    public void testGetdontoptimize_setsOptimizeToFalse() throws Exception {
        assertTrue(task.configuration.optimize, "optimize should initially be true");
        task.getdontoptimize();
        assertFalse(task.configuration.optimize, "optimize should be set to false");
    }

    @Test
    public void testGetdontoptimize_returnsNullAndSetsOptimize() throws Exception {
        assertTrue(task.configuration.optimize, "optimize should initially be true");
        Object result = task.getdontoptimize();
        assertNull(result, "getdontoptimize() should return null");
        assertFalse(task.configuration.optimize, "optimize should be set to false");
    }

    // Multiple calls tests

    @Test
    public void testGetdontoptimize_multipleCallsAreIdempotent() throws Exception {
        task.getdontoptimize();
        assertFalse(task.configuration.optimize, "First call should set optimize to false");

        task.getdontoptimize();
        assertFalse(task.configuration.optimize, "Second call should keep optimize as false");

        task.getdontoptimize();
        assertFalse(task.configuration.optimize, "Third call should keep optimize as false");
    }

    @Test
    public void testGetdontoptimize_alwaysReturnsNull() throws Exception {
        Object result1 = task.getdontoptimize();
        Object result2 = task.getdontoptimize();
        Object result3 = task.getdontoptimize();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetdontoptimize_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            Object result = task.getdontoptimize();
            assertNull(result, "Call " + (i + 1) + " should return null");
            assertFalse(task.configuration.optimize, "optimize should be false after call " + (i + 1));
        }
    }

    // Integration with other configuration methods

    @Test
    public void testGetdontoptimize_worksWithOtherDontMethods() throws Exception {
        task.getdontoptimize();
        task.dontshrink();
        task.dontobfuscate();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
    }

    @Test
    public void testGetdontoptimize_worksWithDontShrink() throws Exception {
        task.dontshrink();
        assertTrue(task.configuration.optimize, "optimize should still be true");

        task.getdontoptimize();
        assertFalse(task.configuration.optimize, "optimize should now be false");
        assertFalse(task.configuration.shrink, "shrink should remain false");
    }

    @Test
    public void testGetdontoptimize_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.getdontoptimize();

        assertNotNull(task.configuration.keep, "keep rules should be configured");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetdontoptimize_worksWithPrintOptions() throws Exception {
        task.printusage("usage.txt");
        task.printseeds("seeds.txt");
        task.getdontoptimize();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertEquals("usage.txt", task.configuration.printUsage.getName());
        assertEquals("seeds.txt", task.configuration.printSeeds.getName());
    }

    @Test
    public void testGetdontoptimize_worksWithVerbose() throws Exception {
        task.verbose();
        task.getdontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    // Call order flexibility

    @Test
    public void testGetdontoptimize_canBeCalledFirst() throws Exception {
        task.getdontoptimize();
        task.keep("public class * { public *; }");
        task.dontshrink();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetdontoptimize_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.dontshrink();
        task.getdontoptimize();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    @Test
    public void testGetdontoptimize_canBeCalledBetweenOtherMethods() throws Exception {
        task.keep("public class * { public *; }");
        task.getdontoptimize();
        task.dontshrink();

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertFalse(task.configuration.shrink, "shrink should be false");
    }

    // Realistic usage scenarios

    @Test
    public void testGetdontoptimize_androidDebugBuild() throws Exception {
        // Debug builds typically disable optimization for faster builds
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.getdontoptimize();
        task.keep("class com.example.MainActivity { *; }");

        assertFalse(task.configuration.optimize, "optimize should be disabled for debug build");
    }

    @Test
    public void testGetdontoptimize_testingScenario() throws Exception {
        // Testing scenario where optimization is disabled for easier debugging
        task.getdontoptimize();
        task.keep("class com.example.** { *; }");
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");

        assertFalse(task.configuration.optimize, "optimize should be disabled for testing");
    }

    @Test
    public void testGetdontoptimize_onlyObfuscationScenario() throws Exception {
        // Scenario where only obfuscation is needed, not optimization
        task.dontshrink();
        task.getdontoptimize();
        // obfuscation is enabled by default

        assertFalse(task.configuration.shrink, "shrink should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.obfuscate, "obfuscate should still be enabled");
    }

    @Test
    public void testGetdontoptimize_analysisOnlyMode() throws Exception {
        // Mode where we only want to analyze, not process
        task.dontshrink();
        task.getdontoptimize();
        task.dontobfuscate();
        task.dontpreverify();
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");

        assertFalse(task.configuration.shrink, "shrink should be disabled");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscate should be disabled");
    }

    @Test
    public void testGetdontoptimize_libraryConfiguration() throws Exception {
        // Library configuration where optimization might cause issues
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-processed.jar");
        task.getdontoptimize();
        task.keep("public class * { public *; }");

        assertFalse(task.configuration.optimize, "optimize should be disabled for library");
    }

    // Edge cases and validation

    @Test
    public void testGetdontoptimize_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.getdontoptimize();

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
        assertFalse(task.configuration.optimize, "optimize should be false");
    }

    @Test
    public void testGetdontoptimize_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.getdontoptimize();
        assertNotNull(task.configuration, "configuration should remain not null");
    }

    @Test
    public void testGetdontoptimize_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertTrue(freshTask.configuration.optimize, "Fresh task optimize should be true");

        Object result = freshTask.getdontoptimize();

        assertNull(result, "Should return null");
        assertFalse(freshTask.configuration.optimize, "Fresh task optimize should be false");
    }

    @Test
    public void testGetdontoptimize_afterManualOptimizeFalse() throws Exception {
        task.configuration.optimize = false;
        assertFalse(task.configuration.optimize, "optimize should be false");

        Object result = task.getdontoptimize();

        assertNull(result, "Should return null");
        assertFalse(task.configuration.optimize, "optimize should remain false");
    }

    @Test
    public void testGetdontoptimize_consistentWithDontoptimizeMethod() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard3", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard4", ProGuardTask.class);

        // Using getdontoptimize()
        task1.getdontoptimize();

        // Using dontoptimize() directly
        task2.dontoptimize();

        assertEquals(task1.configuration.optimize, task2.configuration.optimize,
                "Both methods should have same effect on optimize setting");
    }

    // Interaction with optimization settings

    @Test
    public void testGetdontoptimize_disablesOptimizationPass() throws Exception {
        assertTrue(task.configuration.optimize, "optimize should be enabled by default");
        task.getdontoptimize();
        assertFalse(task.configuration.optimize, "optimize should be disabled after getdontoptimize()");
    }

    @Test
    public void testGetdontoptimize_withOptimizationPasses() throws Exception {
        task.getdontoptimize();
        task.keep("class com.example.MyClass { *; }");

        assertFalse(task.configuration.optimize,
                "optimize should be false even with keep rules");
    }

    @Test
    public void testGetdontoptimize_complexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.getdontoptimize();
        task.printseeds("seeds.txt");
        task.verbose();

        assertFalse(task.configuration.optimize, "optimize should be false in complex config");
    }

    // Groovy DSL compatibility

    @Test
    public void testGetdontoptimize_groovyDslSupport() throws Exception {
        // This method exists to support Groovy DSL syntax without parentheses
        // In Groovy: dontoptimize (without parentheses) calls getdontoptimize()
        Object result = task.getdontoptimize();

        assertNull(result, "Should return null for Groovy DSL compatibility");
        assertFalse(task.configuration.optimize, "Should disable optimization");
    }

    @Test
    public void testGetdontoptimize_returnsNullForChaining() throws Exception {
        // Even though it returns null, verify the method completes successfully
        Object result = task.getdontoptimize();

        assertNull(result, "Should return null");
        // Verify we can still call other methods after
        task.dontshrink();
        assertFalse(task.configuration.shrink, "Should be able to call other methods after");
    }

    // Multiple optimization-related methods

    @Test
    public void testGetdontoptimize_withAllDontMethods() throws Exception {
        task.dontshrink();
        task.getdontoptimize();
        task.dontobfuscate();
        task.dontpreverify();

        assertFalse(task.configuration.shrink, "shrink should be false");
        assertFalse(task.configuration.optimize, "optimize should be false");
        assertFalse(task.configuration.obfuscate, "obfuscate should be false");
        assertFalse(task.configuration.preverify, "preverify should be false");
    }

    @Test
    public void testGetdontoptimize_doesNotReenableOptimization() throws Exception {
        task.getdontoptimize();
        assertFalse(task.configuration.optimize, "optimize should be false");

        // Call other methods
        task.keep("class com.example.MyClass { *; }");
        task.dontshrink();

        // Verify optimize is still false
        assertFalse(task.configuration.optimize, "optimize should still be false");
    }
}

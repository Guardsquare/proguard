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
 * Test class for ProGuardTask.assumenosideeffects methods.
 * Tests the assumenosideeffects(String)V and assumenosideeffects(Map, Closure)V methods
 * that add class specifications to configuration.assumeNoSideEffects, telling ProGuard
 * that certain methods have no side effects and can be safely removed if their return
 * values are unused.
 */
public class ProGuardTaskClaude_assumenosideeffectsTest {

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
    public void testAssumenosideeffects_addsClassSpecification() throws Exception {
        assertNull(task.configuration.assumeNoSideEffects, "assumeNoSideEffects should initially be null");
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects, "assumeNoSideEffects should not be null after call");
        assertEquals(1, task.configuration.assumeNoSideEffects.size(),
                "assumeNoSideEffects should contain one specification");
    }

    @Test
    public void testAssumenosideeffects_simpleLogMethod() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_customLoggingClass() throws Exception {
        task.assumenosideeffects("class com.example.Logger { public void log(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_multipleLogMethods() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int v(...); public static int d(...); public static int i(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    // Multiple calls tests

    @Test
    public void testAssumenosideeffects_multipleCallsAccumulate() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        assertEquals(1, task.configuration.assumeNoSideEffects.size());

        task.assumenosideeffects("class android.util.Log { public static int v(...); }");
        assertEquals(2, task.configuration.assumeNoSideEffects.size());

        task.assumenosideeffects("class com.example.Logger { public void log(...); }");
        assertEquals(3, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_accumulatesSpecifications() throws Exception {
        assertNull(task.configuration.assumeNoSideEffects);

        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        List firstCall = task.configuration.assumeNoSideEffects;
        assertNotNull(firstCall);
        assertEquals(1, firstCall.size());

        task.assumenosideeffects("class com.example.Logger { public void log(...); }");
        List secondCall = task.configuration.assumeNoSideEffects;
        assertSame(firstCall, secondCall, "Should reuse same list");
        assertEquals(2, secondCall.size(), "Should accumulate specifications");
    }

    @Test
    public void testAssumenosideeffects_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.assumenosideeffects("class com.example.Logger" + i + " { public void log(...); }");
        }
        assertEquals(5, task.configuration.assumeNoSideEffects.size(),
                "Should accumulate all 5 specifications");
    }

    // Common use cases for logging

    @Test
    public void testAssumenosideeffects_androidLogDebug() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_androidLogVerbose() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int v(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_androidLogInfo() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int i(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_androidLogWarn() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int w(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_androidLogError() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int e(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    // Integration with other configuration methods

    @Test
    public void testAssumenosideeffects_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");

        assertNotNull(task.configuration.keep);
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");

        assertFalse(task.configuration.optimize);
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_worksWithOptimizations() throws Exception {
        task.optimizations("code/removal/*");
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");

        assertNotNull(task.configuration.optimizations);
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_worksWithPrintOptions() throws Exception {
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    // Call order flexibility

    @Test
    public void testAssumenosideeffects_canBeCalledFirst() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.keep("public class * { public *; }");
        task.optimizations("code/removal/*");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizations("code/removal/*");
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.optimizations("code/removal/*");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    // Realistic usage scenarios

    @Test
    public void testAssumenosideeffects_androidReleaseRemoveLogs() throws Exception {
        // Remove all Android Log calls in release build
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.assumenosideeffects("class android.util.Log { public static int v(...); }");
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenosideeffects("class android.util.Log { public static int i(...); }");

        assertEquals(3, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_customLoggingFramework() throws Exception {
        // Remove custom logging framework calls
        task.assumenosideeffects("class com.example.logging.Logger { public void debug(...); }");
        task.assumenosideeffects("class com.example.logging.Logger { public void info(...); }");
        task.assumenosideeffects("class com.example.logging.Logger { public void trace(...); }");

        assertEquals(3, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_removeDebugCode() throws Exception {
        // Remove debug-only methods
        task.assumenosideeffects("class com.example.Debug { public static void log(...); }");
        task.assumenosideeffects("class com.example.Debug { public static void trace(...); }");
        task.assumenosideeffects("class com.example.Debug { public static void dump(...); }");

        assertEquals(3, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_thirdPartyLoggingLibrary() throws Exception {
        // Remove third-party logging library calls (e.g., Timber, SLF4J)
        task.assumenosideeffects("class timber.log.Timber { public static void v(...); }");
        task.assumenosideeffects("class timber.log.Timber { public static void d(...); }");
        task.assumenosideeffects("class timber.log.Timber { public static void i(...); }");

        assertEquals(3, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_completeBuildConfiguration() throws Exception {
        // Complete release build configuration
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenosideeffects("class android.util.Log { public static int v(...); }");
        task.printmapping("mapping.txt");

        assertEquals(2, task.configuration.assumeNoSideEffects.size());
    }

    // Edge cases and validation

    @Test
    public void testAssumenosideeffects_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.assumenosideeffects("class android.util.Log { public static int d(...); }");

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testAssumenosideeffects_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        assertNotNull(task.configuration, "configuration should remain not null");
        assertNotNull(task.configuration.assumeNoSideEffects,
                "assumeNoSideEffects should not be null after call");
    }

    @Test
    public void testAssumenosideeffects_listIsModifiable() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        List list = task.configuration.assumeNoSideEffects;
        int initialSize = list.size();

        task.assumenosideeffects("class android.util.Log { public static int v(...); }");

        assertEquals(initialSize + 1, list.size(), "List should be modifiable and grow");
    }

    @Test
    public void testAssumenosideeffects_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertNull(freshTask.configuration.assumeNoSideEffects);

        freshTask.assumenosideeffects("class android.util.Log { public static int d(...); }");

        assertNotNull(freshTask.configuration.assumeNoSideEffects);
        assertEquals(1, freshTask.configuration.assumeNoSideEffects.size());
    }

    // Different method specifications

    @Test
    public void testAssumenosideeffects_methodWithParameters() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(java.lang.String, java.lang.String); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_methodWithVarargs() throws Exception {
        task.assumenosideeffects("class com.example.Logger { public void log(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_staticMethod() throws Exception {
        task.assumenosideeffects("class com.example.Logger { public static void log(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_instanceMethod() throws Exception {
        task.assumenosideeffects("class com.example.Logger { public void log(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_methodWithReturnType() throws Exception {
        task.assumenosideeffects("class com.example.Logger { public int log(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_allMethodsInClass() throws Exception {
        task.assumenosideeffects("class com.example.DebugLogger { *; }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    // Multiple logging frameworks

    @Test
    public void testAssumenosideeffects_mixedLoggingFrameworks() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenosideeffects("class timber.log.Timber { public static void d(...); }");
        task.assumenosideeffects("class com.example.Logger { public void debug(...); }");

        assertEquals(3, task.configuration.assumeNoSideEffects.size(),
                "Should support multiple logging frameworks");
    }

    @Test
    public void testAssumenosideeffects_slf4jLogging() throws Exception {
        task.assumenosideeffects("class org.slf4j.Logger { public void debug(...); }");
        task.assumenosideeffects("class org.slf4j.Logger { public void info(...); }");
        task.assumenosideeffects("class org.slf4j.Logger { public void trace(...); }");

        assertEquals(3, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_log4jLogging() throws Exception {
        task.assumenosideeffects("class org.apache.log4j.Logger { public void debug(...); }");
        task.assumenosideeffects("class org.apache.log4j.Logger { public void info(...); }");

        assertEquals(2, task.configuration.assumeNoSideEffects.size());
    }

    // Performance optimization scenarios

    @Test
    public void testAssumenosideeffects_optimizationWithLoggingRemoval() throws Exception {
        // Optimization scenario: remove logging for better performance
        task.optimizationpasses(5);
        task.optimizations("code/removal/*");
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenosideeffects("class android.util.Log { public static int v(...); }");

        assertEquals(2, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_sizeOptimizationScenario() throws Exception {
        // Size optimization: remove all debug code
        task.optimizationpasses(7);
        task.optimizations("*");
        task.assumenosideeffects("class com.example.Debug { *; }");
        task.assumenosideeffects("class com.example.Trace { *; }");
        task.assumenosideeffects("class com.example.Profile { *; }");

        assertEquals(3, task.configuration.assumeNoSideEffects.size());
    }

    // Build variant scenarios

    @Test
    public void testAssumenosideeffects_debugBuildNoRemoval() throws Exception {
        // Debug build: typically don't remove logs
        task.dontoptimize();
        task.dontshrink();
        // Note: In debug builds, you typically wouldn't call assumenosideeffects
        // This test just ensures it doesn't break anything
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");

        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_releaseBuildAggressiveRemoval() throws Exception {
        // Release build: aggressively remove all logging
        task.optimizationpasses(7);
        task.assumenosideeffects("class android.util.Log { public static int v(...); }");
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenosideeffects("class android.util.Log { public static int i(...); }");
        task.assumenosideeffects("class android.util.Log { public static int w(...); }");
        // Note: typically keep error logs
        task.keep("class android.util.Log { public static int e(...); }");

        assertEquals(4, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_stagingBuildSelectiveRemoval() throws Exception {
        // Staging build: remove only verbose and debug logs
        task.optimizationpasses(3);
        task.assumenosideeffects("class android.util.Log { public static int v(...); }");
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        // Keep info, warning, and error logs for staging

        assertEquals(2, task.configuration.assumeNoSideEffects.size());
    }

    // Complex specifications

    @Test
    public void testAssumenosideeffects_classWithMultipleMethods() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int v(...); public static int d(...); public static int i(...); public static int w(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffects_wildcardClassName() throws Exception {
        task.assumenosideeffects("class com.example.**.Logger { public void log(...); }");
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    // ========================================
    // Tests for assumenosideeffects(Map, Closure) method
    // ========================================

    // Basic functionality tests for assumenosideeffects(Map, Closure)

    @Test
    public void testAssumenosideeffectsWithMapClosure_emptyMap() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        assertNull(task.configuration.assumeNoSideEffects, "assumeNoSideEffects should initially be null");
        task.assumenosideeffects(args, null);
        assertNotNull(task.configuration.assumeNoSideEffects, "assumeNoSideEffects should not be null after call");
        assertEquals(1, task.configuration.assumeNoSideEffects.size(),
                "assumeNoSideEffects should contain one specification");
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_addsSpecification() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "android.util.Log");

        task.assumenosideeffects(args, null);
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_withNameAttribute() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Logger");

        task.assumenosideeffects(args, null);
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_withAccessModifiers() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("name", "com.example.Logger");

        task.assumenosideeffects(args, null);
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_withTypeClass() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "class");
        args.put("name", "android.util.Log");

        task.assumenosideeffects(args, null);
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_withTypeInterface() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "interface");
        args.put("name", "com.example.Logger");

        task.assumenosideeffects(args, null);
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    // Multiple calls tests

    @Test
    public void testAssumenosideeffectsWithMapClosure_multipleCallsAccumulate() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.util.Log");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "timber.log.Timber");

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Logger");

        task.assumenosideeffects(args1, null);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());

        task.assumenosideeffects(args2, null);
        assertEquals(2, task.configuration.assumeNoSideEffects.size());

        task.assumenosideeffects(args3, null);
        assertEquals(3, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_accumulatesSpecifications() throws Exception {
        assertNull(task.configuration.assumeNoSideEffects);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.util.Log");

        task.assumenosideeffects(args1, null);
        List firstCall = task.configuration.assumeNoSideEffects;
        assertNotNull(firstCall);
        assertEquals(1, firstCall.size());

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "timber.log.Timber");

        task.assumenosideeffects(args2, null);
        List secondCall = task.configuration.assumeNoSideEffects;
        assertSame(firstCall, secondCall, "Should reuse same list");
        assertEquals(2, secondCall.size(), "Should accumulate specifications");
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("name", "com.example.Logger" + i);
            task.assumenosideeffects(args, null);
        }
        assertEquals(5, task.configuration.assumeNoSideEffects.size(),
                "Should accumulate all 5 specifications");
    }

    // Integration with String method

    @Test
    public void testAssumenosideeffectsWithMapClosure_worksWithStringMethod() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        assertEquals(1, task.configuration.assumeNoSideEffects.size());

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "timber.log.Timber");
        task.assumenosideeffects(args, null);

        assertEquals(2, task.configuration.assumeNoSideEffects.size(),
                "Both methods should add to same list");
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_stringMethodFirst() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenosideeffects("class android.util.Log { public static int v(...); }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Logger");
        task.assumenosideeffects(args, null);

        assertEquals(3, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_mapMethodFirst() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Logger");
        task.assumenosideeffects(args1, null);

        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenosideeffects("class android.util.Log { public static int v(...); }");

        assertEquals(3, task.configuration.assumeNoSideEffects.size());
    }

    // Integration with other configuration methods

    @Test
    public void testAssumenosideeffectsWithMapClosure_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "android.util.Log");
        task.assumenosideeffects(args, null);

        assertNotNull(task.configuration.keep);
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_worksWithDontOptimize() throws Exception {
        task.dontoptimize();

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "android.util.Log");
        task.assumenosideeffects(args, null);

        assertFalse(task.configuration.optimize);
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_worksWithOptimizations() throws Exception {
        task.optimizations("code/removal/*");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "android.util.Log");
        task.assumenosideeffects(args, null);

        assertNotNull(task.configuration.optimizations);
        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    // Call order flexibility

    @Test
    public void testAssumenosideeffectsWithMapClosure_canBeCalledFirst() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "android.util.Log");
        task.assumenosideeffects(args, null);

        task.keep("public class * { public *; }");
        task.optimizations("code/removal/*");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizations("code/removal/*");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "android.util.Log");
        task.assumenosideeffects(args, null);

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "android.util.Log");
        task.assumenosideeffects(args, null);

        task.optimizations("code/removal/*");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    // Realistic usage scenarios

    @Test
    public void testAssumenosideeffectsWithMapClosure_androidLogRemoval() throws Exception {
        // Remove Android Log class using Map syntax
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "android.util.Log");

        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.assumenosideeffects(args, null);

        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_customLoggingLibrary() throws Exception {
        // Remove custom logging library
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.logging.Logger");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.logging.DebugLogger");

        task.assumenosideeffects(args1, null);
        task.assumenosideeffects(args2, null);

        assertEquals(2, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_timberLogging() throws Exception {
        // Remove Timber logging library
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "timber.log.Timber");

        task.assumenosideeffects(args, null);

        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_slf4jLogging() throws Exception {
        // Remove SLF4J logging
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "org.slf4j.Logger");

        task.assumenosideeffects(args, null);

        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_completeBuildConfiguration() throws Exception {
        // Complete release build configuration using Map syntax
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.util.Log");
        task.assumenosideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "timber.log.Timber");
        task.assumenosideeffects(args2, null);

        task.printmapping("mapping.txt");

        assertEquals(2, task.configuration.assumeNoSideEffects.size());
    }

    // Edge cases and validation

    @Test
    public void testAssumenosideeffectsWithMapClosure_nullClosureIsValid() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "android.util.Log");

        // null Closure is valid - commonly used in Java tests
        task.assumenosideeffects(args, null);

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "android.util.Log");
        task.assumenosideeffects(args, null);

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "android.util.Log");
        task.assumenosideeffects(args, null);

        assertNotNull(task.configuration, "configuration should remain not null");
        assertNotNull(task.configuration.assumeNoSideEffects,
                "assumeNoSideEffects should not be null after call");
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_listIsModifiable() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.util.Log");
        task.assumenosideeffects(args1, null);

        List list = task.configuration.assumeNoSideEffects;
        int initialSize = list.size();

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "timber.log.Timber");
        task.assumenosideeffects(args2, null);

        assertEquals(initialSize + 1, list.size(), "List should be modifiable and grow");
    }

    // Complex Map specifications

    @Test
    public void testAssumenosideeffectsWithMapClosure_withMultipleAttributes() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("type", "class");
        args.put("name", "android.util.Log");

        task.assumenosideeffects(args, null);

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_wildcardInName() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.**.Logger");

        task.assumenosideeffects(args, null);

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_withModifiers() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public static");
        args.put("name", "android.util.Log");

        task.assumenosideeffects(args, null);

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
    }

    // Performance optimization scenarios

    @Test
    public void testAssumenosideeffectsWithMapClosure_optimizationScenario() throws Exception {
        // Optimization scenario: remove logging for better performance
        task.optimizationpasses(5);
        task.optimizations("code/removal/*");

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.util.Log");
        task.assumenosideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "timber.log.Timber");
        task.assumenosideeffects(args2, null);

        assertEquals(2, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_sizeOptimization() throws Exception {
        // Size optimization: remove all debug classes
        task.optimizationpasses(7);
        task.optimizations("*");

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Debug");
        task.assumenosideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Trace");
        task.assumenosideeffects(args2, null);

        assertEquals(2, task.configuration.assumeNoSideEffects.size());
    }

    // Build variant scenarios

    @Test
    public void testAssumenosideeffectsWithMapClosure_releaseBuild() throws Exception {
        // Release build: aggressively remove all logging classes
        task.optimizationpasses(7);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.util.Log");
        task.assumenosideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "timber.log.Timber");
        task.assumenosideeffects(args2, null);

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Logger");
        task.assumenosideeffects(args3, null);

        assertEquals(3, task.configuration.assumeNoSideEffects.size());
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_stagingBuild() throws Exception {
        // Staging build: remove only debug logging classes
        task.optimizationpasses(3);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.DebugLogger");
        task.assumenosideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.VerboseLogger");
        task.assumenosideeffects(args2, null);

        assertEquals(2, task.configuration.assumeNoSideEffects.size());
    }

    // Mixed usage with String and Map methods

    @Test
    public void testAssumenosideeffectsWithMapClosure_mixedWithStringMethod() throws Exception {
        // Mix String method (detailed method specs) with Map method (class specs)
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "timber.log.Timber");
        task.assumenosideeffects(args, null);

        task.assumenosideeffects("class com.example.Logger { public void log(...); }");

        assertEquals(3, task.configuration.assumeNoSideEffects.size(),
                "Should mix String and Map methods seamlessly");
    }

    @Test
    public void testAssumenosideeffectsWithMapClosure_alternatingMethods() throws Exception {
        for (int i = 0; i < 3; i++) {
            // Alternate between String and Map methods
            task.assumenosideeffects("class com.example.Logger" + i + " { public void log(...); }");

            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("name", "com.example.Debug" + i);
            task.assumenosideeffects(args, null);
        }

        assertEquals(6, task.configuration.assumeNoSideEffects.size(),
                "Should accumulate all specifications from both methods");
    }
}

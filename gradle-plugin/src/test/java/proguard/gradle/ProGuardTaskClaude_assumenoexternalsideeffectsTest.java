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
 * Test class for ProGuardTask.assumenoexternalsideeffects methods.
 * Tests the assumenoexternalsideeffects(String)V and assumenoexternalsideeffects(Map, Closure)V
 * methods that add class specifications to configuration.assumeNoExternalSideEffects, telling
 * ProGuard that certain methods have no external side effects (e.g., they don't modify fields
 * outside their own class) and can be safely optimized or removed.
 */
public class ProGuardTaskClaude_assumenoexternalsideeffectsTest {

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
    public void testAssumenoexternalsideeffects_addsClassSpecification() throws Exception {
        assertNull(task.configuration.assumeNoExternalSideEffects,
                "assumeNoExternalSideEffects should initially be null");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects,
                "assumeNoExternalSideEffects should not be null after call");
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size(),
                "assumeNoExternalSideEffects should contain one specification");
    }

    @Test
    public void testAssumenoexternalsideeffects_simpleMethod() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_customClass() throws Exception {
        task.assumenoexternalsideeffects("class com.example.Builder { public void add(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_multipleMethods() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); public java.lang.StringBuilder insert(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Multiple calls tests

    @Test
    public void testAssumenoexternalsideeffects_multipleCallsAccumulate() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());

        task.assumenoexternalsideeffects("class java.lang.StringBuffer { public java.lang.StringBuffer append(...); }");
        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());

        task.assumenoexternalsideeffects("class com.example.Builder { public void add(...); }");
        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_accumulatesSpecifications() throws Exception {
        assertNull(task.configuration.assumeNoExternalSideEffects);

        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        List firstCall = task.configuration.assumeNoExternalSideEffects;
        assertNotNull(firstCall);
        assertEquals(1, firstCall.size());

        task.assumenoexternalsideeffects("class java.lang.StringBuffer { public java.lang.StringBuffer append(...); }");
        List secondCall = task.configuration.assumeNoExternalSideEffects;
        assertSame(firstCall, secondCall, "Should reuse same list");
        assertEquals(2, secondCall.size(), "Should accumulate specifications");
    }

    @Test
    public void testAssumenoexternalsideeffects_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.assumenoexternalsideeffects("class com.example.Builder" + i + " { public void add(...); }");
        }
        assertEquals(5, task.configuration.assumeNoExternalSideEffects.size(),
                "Should accumulate all 5 specifications");
    }

    // Common use cases for builder patterns

    @Test
    public void testAssumenoexternalsideeffects_stringBuilder() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_stringBuffer() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuffer { public java.lang.StringBuffer append(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_customBuilder() throws Exception {
        task.assumenoexternalsideeffects("class com.example.RequestBuilder { public com.example.RequestBuilder with(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_fluentInterface() throws Exception {
        task.assumenoexternalsideeffects("class com.example.FluentApi { public com.example.FluentApi set(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Integration with other configuration methods

    @Test
    public void testAssumenoexternalsideeffects_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");

        assertNotNull(task.configuration.keep);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");

        assertFalse(task.configuration.optimize);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_worksWithOptimizations() throws Exception {
        task.optimizations("code/simplification/*");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");

        assertNotNull(task.configuration.optimizations);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_worksWithAssumenosideeffects() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Call order flexibility

    @Test
    public void testAssumenoexternalsideeffects_canBeCalledFirst() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");

        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Realistic usage scenarios

    @Test
    public void testAssumenoexternalsideeffects_stringBuilderOptimization() throws Exception {
        // Optimize StringBuilder usage
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-optimized.jar");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder insert(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder delete(...); }");

        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_builderPatternOptimization() throws Exception {
        // Optimize custom builder patterns
        task.assumenoexternalsideeffects("class com.example.RequestBuilder { public com.example.RequestBuilder withHeader(...); }");
        task.assumenoexternalsideeffects("class com.example.RequestBuilder { public com.example.RequestBuilder withParam(...); }");
        task.assumenoexternalsideeffects("class com.example.RequestBuilder { public com.example.RequestBuilder withBody(...); }");

        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_fluentApiOptimization() throws Exception {
        // Optimize fluent API methods
        task.assumenoexternalsideeffects("class com.example.Config { public com.example.Config set(...); }");
        task.assumenoexternalsideeffects("class com.example.Config { public com.example.Config enable(...); }");
        task.assumenoexternalsideeffects("class com.example.Config { public com.example.Config disable(...); }");

        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_completeBuildConfiguration() throws Exception {
        // Complete optimization configuration
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class com.example.Builder { public com.example.Builder with(...); }");
        task.printmapping("mapping.txt");

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Edge cases and validation

    @Test
    public void testAssumenoexternalsideeffects_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testAssumenoexternalsideeffects_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        assertNotNull(task.configuration, "configuration should remain not null");
        assertNotNull(task.configuration.assumeNoExternalSideEffects,
                "assumeNoExternalSideEffects should not be null after call");
    }

    @Test
    public void testAssumenoexternalsideeffects_listIsModifiable() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        List list = task.configuration.assumeNoExternalSideEffects;
        int initialSize = list.size();

        task.assumenoexternalsideeffects("class java.lang.StringBuffer { public java.lang.StringBuffer append(...); }");

        assertEquals(initialSize + 1, list.size(), "List should be modifiable and grow");
    }

    @Test
    public void testAssumenoexternalsideeffects_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertNull(freshTask.configuration.assumeNoExternalSideEffects);

        freshTask.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");

        assertNotNull(freshTask.configuration.assumeNoExternalSideEffects);
        assertEquals(1, freshTask.configuration.assumeNoExternalSideEffects.size());
    }

    // Different method specifications

    @Test
    public void testAssumenoexternalsideeffects_methodWithParameters() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(java.lang.String); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_methodWithVarargs() throws Exception {
        task.assumenoexternalsideeffects("class com.example.Builder { public com.example.Builder add(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_instanceMethod() throws Exception {
        task.assumenoexternalsideeffects("class com.example.Builder { public com.example.Builder set(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_methodWithReturnType() throws Exception {
        task.assumenoexternalsideeffects("class com.example.Builder { public com.example.Builder configure(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_allMethodsInClass() throws Exception {
        task.assumenoexternalsideeffects("class com.example.ImmutableBuilder { *; }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Common Java builder classes

    @Test
    public void testAssumenoexternalsideeffects_stringBuilderAllMethods() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder insert(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder delete(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder replace(...); }");

        assertEquals(4, task.configuration.assumeNoExternalSideEffects.size(),
                "Should support multiple StringBuilder methods");
    }

    @Test
    public void testAssumenoexternalsideeffects_stringBufferMethods() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuffer { public java.lang.StringBuffer append(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuffer { public java.lang.StringBuffer insert(...); }");

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Performance optimization scenarios

    @Test
    public void testAssumenoexternalsideeffects_optimizationWithBuilders() throws Exception {
        // Optimization scenario: optimize builder patterns for better performance
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class com.example.RequestBuilder { public com.example.RequestBuilder with(...); }");

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_aggressiveOptimization() throws Exception {
        // Aggressive optimization: all builder-like classes
        task.optimizationpasses(7);
        task.optimizations("*");
        task.assumenoexternalsideeffects("class com.example.Builder { *; }");
        task.assumenoexternalsideeffects("class com.example.RequestBuilder { *; }");
        task.assumenoexternalsideeffects("class com.example.ConfigBuilder { *; }");

        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Build variant scenarios

    @Test
    public void testAssumenoexternalsideeffects_releaseBuildOptimization() throws Exception {
        // Release build: optimize all builder patterns
        task.optimizationpasses(7);
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuffer { public java.lang.StringBuffer append(...); }");
        task.assumenoexternalsideeffects("class com.example.Builder { *; }");

        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Complex specifications

    @Test
    public void testAssumenoexternalsideeffects_classWithMultipleMethods() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); public java.lang.StringBuilder insert(...); public java.lang.StringBuilder delete(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_wildcardClassName() throws Exception {
        task.assumenoexternalsideeffects("class com.example.**.*Builder { public * with(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_interfaceBuilder() throws Exception {
        task.assumenoexternalsideeffects("interface com.example.Builder { public com.example.Builder add(...); }");
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Separation from assumeNoSideEffects

    @Test
    public void testAssumenoexternalsideeffects_separateFromAssumenosideeffects() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertNotSame(task.configuration.assumeNoSideEffects,
                task.configuration.assumeNoExternalSideEffects,
                "Should use separate lists");
    }

    @Test
    public void testAssumenoexternalsideeffects_bothTypesOfAssumptions() throws Exception {
        // Use both types of side effect assumptions
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenosideeffects("class android.util.Log { public static int v(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class com.example.Builder { public com.example.Builder with(...); }");

        assertEquals(2, task.configuration.assumeNoSideEffects.size(),
                "Should have 2 no side effects assumptions");
        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size(),
                "Should have 2 no external side effects assumptions");
    }

    // Android-specific scenarios

    @Test
    public void testAssumenoexternalsideeffects_androidBuilders() throws Exception {
        // Android builder patterns
        task.assumenoexternalsideeffects("class android.app.AlertDialog$Builder { public android.app.AlertDialog$Builder set(...); }");
        task.assumenoexternalsideeffects("class android.app.Notification$Builder { public android.app.Notification$Builder set(...); }");

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffects_androidCompleteConfiguration() throws Exception {
        // Complete Android optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.optimizationpasses(5);
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class android.app.AlertDialog$Builder { *; }");

        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Multiple builder frameworks

    @Test
    public void testAssumenoexternalsideeffects_multipleBuilderLibraries() throws Exception {
        // Multiple builder/fluent frameworks
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class com.google.common.collect.ImmutableList$Builder { public * add(...); }");
        task.assumenoexternalsideeffects("class okhttp3.Request$Builder { public okhttp3.Request$Builder set(...); }");

        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size(),
                "Should support multiple builder libraries");
    }

    @Test
    public void testAssumenoexternalsideeffects_guavaBuilders() throws Exception {
        task.assumenoexternalsideeffects("class com.google.common.collect.ImmutableList$Builder { public com.google.common.collect.ImmutableList$Builder add(...); }");
        task.assumenoexternalsideeffects("class com.google.common.collect.ImmutableSet$Builder { public com.google.common.collect.ImmutableSet$Builder add(...); }");

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    // ========================================
    // Tests for assumenoexternalsideeffects(Map, Closure) method
    // ========================================

    // Basic functionality tests for assumenoexternalsideeffects(Map, Closure)

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_emptyMap() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        assertNull(task.configuration.assumeNoExternalSideEffects,
                "assumeNoExternalSideEffects should initially be null");
        task.assumenoexternalsideeffects(args, null);
        assertNotNull(task.configuration.assumeNoExternalSideEffects,
                "assumeNoExternalSideEffects should not be null after call");
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size(),
                "assumeNoExternalSideEffects should contain one specification");
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_addsSpecification() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");

        task.assumenoexternalsideeffects(args, null);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_withNameAttribute() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Builder");

        task.assumenoexternalsideeffects(args, null);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_withAccessModifiers() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("name", "com.example.RequestBuilder");

        task.assumenoexternalsideeffects(args, null);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_withTypeClass() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "class");
        args.put("name", "java.lang.StringBuilder");

        task.assumenoexternalsideeffects(args, null);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_withTypeInterface() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "interface");
        args.put("name", "com.example.Builder");

        task.assumenoexternalsideeffects(args, null);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Multiple calls tests

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_multipleCallsAccumulate() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.StringBuilder");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.StringBuffer");

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Builder");

        task.assumenoexternalsideeffects(args1, null);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());

        task.assumenoexternalsideeffects(args2, null);
        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());

        task.assumenoexternalsideeffects(args3, null);
        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_accumulatesSpecifications() throws Exception {
        assertNull(task.configuration.assumeNoExternalSideEffects);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.StringBuilder");

        task.assumenoexternalsideeffects(args1, null);
        List firstCall = task.configuration.assumeNoExternalSideEffects;
        assertNotNull(firstCall);
        assertEquals(1, firstCall.size());

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.StringBuffer");

        task.assumenoexternalsideeffects(args2, null);
        List secondCall = task.configuration.assumeNoExternalSideEffects;
        assertSame(firstCall, secondCall, "Should reuse same list");
        assertEquals(2, secondCall.size(), "Should accumulate specifications");
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("name", "com.example.Builder" + i);
            task.assumenoexternalsideeffects(args, null);
        }
        assertEquals(5, task.configuration.assumeNoExternalSideEffects.size(),
                "Should accumulate all 5 specifications");
    }

    // Integration with String method

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_worksWithStringMethod() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuffer");
        task.assumenoexternalsideeffects(args, null);

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size(),
                "Both methods should add to same list");
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_stringMethodFirst() throws Exception {
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder insert(...); }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Builder");
        task.assumenoexternalsideeffects(args, null);

        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_mapMethodFirst() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Builder");
        task.assumenoexternalsideeffects(args1, null);

        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder insert(...); }");

        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Integration with other configuration methods

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args, null);

        assertNotNull(task.configuration.keep);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_worksWithDontOptimize() throws Exception {
        task.dontoptimize();

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args, null);

        assertFalse(task.configuration.optimize);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_worksWithOptimizations() throws Exception {
        task.optimizations("code/simplification/*");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args, null);

        assertNotNull(task.configuration.optimizations);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_worksWithAssumenosideeffects() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args, null);

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Call order flexibility

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_canBeCalledFirst() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args, null);

        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args, null);

        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args, null);

        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Realistic usage scenarios

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_stringBuilderOptimization() throws Exception {
        // Optimize StringBuilder using Map syntax
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");

        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-optimized.jar");
        task.assumenoexternalsideeffects(args, null);

        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_customBuilderLibrary() throws Exception {
        // Optimize custom builder library
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.RequestBuilder");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.ResponseBuilder");

        task.assumenoexternalsideeffects(args1, null);
        task.assumenoexternalsideeffects(args2, null);

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_fluentApiLibrary() throws Exception {
        // Optimize fluent API library
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.FluentConfig");

        task.assumenoexternalsideeffects(args, null);

        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_completeBuildConfiguration() throws Exception {
        // Complete release build configuration using Map syntax
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Builder");
        task.assumenoexternalsideeffects(args2, null);

        task.printmapping("mapping.txt");

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Edge cases and validation

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_nullClosureIsValid() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");

        // null Closure is valid - commonly used in Java tests
        task.assumenoexternalsideeffects(args, null);

        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args, null);

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args, null);

        assertNotNull(task.configuration, "configuration should remain not null");
        assertNotNull(task.configuration.assumeNoExternalSideEffects,
                "assumeNoExternalSideEffects should not be null after call");
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_listIsModifiable() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args1, null);

        List list = task.configuration.assumeNoExternalSideEffects;
        int initialSize = list.size();

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.StringBuffer");
        task.assumenoexternalsideeffects(args2, null);

        assertEquals(initialSize + 1, list.size(), "List should be modifiable and grow");
    }

    // Complex Map specifications

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_withMultipleAttributes() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("type", "class");
        args.put("name", "java.lang.StringBuilder");

        task.assumenoexternalsideeffects(args, null);

        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_wildcardInName() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.**.*Builder");

        task.assumenoexternalsideeffects(args, null);

        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_withModifiers() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public final");
        args.put("name", "java.lang.StringBuilder");

        task.assumenoexternalsideeffects(args, null);

        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Performance optimization scenarios

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_optimizationScenario() throws Exception {
        // Optimization scenario: optimize builders for better performance
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.RequestBuilder");
        task.assumenoexternalsideeffects(args2, null);

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_aggressiveOptimization() throws Exception {
        // Aggressive optimization: all builder classes
        task.optimizationpasses(7);
        task.optimizations("*");

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Builder");
        task.assumenoexternalsideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.RequestBuilder");
        task.assumenoexternalsideeffects(args2, null);

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Build variant scenarios

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_releaseBuild() throws Exception {
        // Release build: optimize all builder patterns
        task.optimizationpasses(7);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.StringBuffer");
        task.assumenoexternalsideeffects(args2, null);

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Builder");
        task.assumenoexternalsideeffects(args3, null);

        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Separation from assumeNoSideEffects

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_separateFromAssumenosideeffects() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.util.Log");
        task.assumenosideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args2, null);

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertNotSame(task.configuration.assumeNoSideEffects,
                task.configuration.assumeNoExternalSideEffects,
                "Should use separate lists");
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_bothTypesOfAssumptions() throws Exception {
        // Use both types of side effect assumptions with Map syntax
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.util.Log");
        task.assumenosideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "timber.log.Timber");
        task.assumenosideeffects(args2, null);

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args3, null);

        java.util.Map<String, Object> args4 = new java.util.HashMap<>();
        args4.put("name", "com.example.Builder");
        task.assumenoexternalsideeffects(args4, null);

        assertEquals(2, task.configuration.assumeNoSideEffects.size(),
                "Should have 2 no side effects assumptions");
        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size(),
                "Should have 2 no external side effects assumptions");
    }

    // Android-specific scenarios

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_androidBuilders() throws Exception {
        // Android builder patterns using Map syntax
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.app.AlertDialog$Builder");
        task.assumenoexternalsideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "android.app.Notification$Builder");
        task.assumenoexternalsideeffects(args2, null);

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_androidCompleteConfiguration() throws Exception {
        // Complete Android optimization using Map syntax
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.optimizationpasses(5);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.util.Log");
        task.assumenosideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args2, null);

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "android.app.AlertDialog$Builder");
        task.assumenoexternalsideeffects(args3, null);

        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Multiple builder frameworks

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_multipleBuilderLibraries() throws Exception {
        // Multiple builder/fluent frameworks using Map syntax
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.google.common.collect.ImmutableList$Builder");
        task.assumenoexternalsideeffects(args2, null);

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "okhttp3.Request$Builder");
        task.assumenoexternalsideeffects(args3, null);

        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size(),
                "Should support multiple builder libraries");
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_guavaBuilders() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.google.common.collect.ImmutableList$Builder");
        task.assumenoexternalsideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.google.common.collect.ImmutableSet$Builder");
        task.assumenoexternalsideeffects(args2, null);

        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size());
    }

    // Mixed usage with String and Map methods

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_mixedWithStringMethod() throws Exception {
        // Mix String method (detailed method specs) with Map method (class specs)
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuffer");
        task.assumenoexternalsideeffects(args, null);

        task.assumenoexternalsideeffects("class com.example.Builder { public com.example.Builder add(...); }");

        assertEquals(3, task.configuration.assumeNoExternalSideEffects.size(),
                "Should mix String and Map methods seamlessly");
    }

    @Test
    public void testAssumenoexternalsideeffectsWithMapClosure_alternatingMethods() throws Exception {
        for (int i = 0; i < 3; i++) {
            // Alternate between String and Map methods
            task.assumenoexternalsideeffects("class com.example.Builder" + i + " { public com.example.Builder" + i + " add(...); }");

            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("name", "com.example.RequestBuilder" + i);
            task.assumenoexternalsideeffects(args, null);
        }

        assertEquals(6, task.configuration.assumeNoExternalSideEffects.size(),
                "Should accumulate all specifications from both methods");
    }
}

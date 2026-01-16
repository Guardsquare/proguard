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
 * Test class for ProGuardTask.assumenoexternalreturnvalues methods.
 * Tests the assumenoexternalreturnvalues(String)V and assumenoexternalreturnvalues(Map, Closure)V
 * methods that add class specifications to configuration.assumeNoExternalReturnValues, telling
 * ProGuard that certain method return values never reference external objects (e.g., they return
 * newly created objects or primitives) and can be safely optimized.
 */
public class ProGuardTaskClaude_assumenoexternalreturnvaluesTest {

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
    public void testAssumenoexternalreturnvalues_addsClassSpecification() throws Exception {
        assertNull(task.configuration.assumeNoExternalReturnValues,
                "assumeNoExternalReturnValues should initially be null");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues,
                "assumeNoExternalReturnValues should not be null after call");
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size(),
                "assumeNoExternalReturnValues should contain one specification");
    }

    @Test
    public void testAssumenoexternalreturnvalues_simpleMethod() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(int); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_customClass() throws Exception {
        task.assumenoexternalreturnvalues("class com.example.Factory { public com.example.Product create(...); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_multipleMethods() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); public java.lang.String substring(...); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Multiple calls tests

    @Test
    public void testAssumenoexternalreturnvalues_multipleCallsAccumulate() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(int); }");
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());

        task.assumenoexternalreturnvalues("class java.lang.StringBuilder { public java.lang.String toString(); }");
        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());

        task.assumenoexternalreturnvalues("class com.example.Factory { public com.example.Product create(...); }");
        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_accumulatesSpecifications() throws Exception {
        assertNull(task.configuration.assumeNoExternalReturnValues);

        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(int); }");
        List firstCall = task.configuration.assumeNoExternalReturnValues;
        assertNotNull(firstCall);
        assertEquals(1, firstCall.size());

        task.assumenoexternalreturnvalues("class java.lang.StringBuilder { public java.lang.String toString(); }");
        List secondCall = task.configuration.assumeNoExternalReturnValues;
        assertSame(firstCall, secondCall, "Should reuse same list");
        assertEquals(2, secondCall.size(), "Should accumulate specifications");
    }

    @Test
    public void testAssumenoexternalreturnvalues_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.assumenoexternalreturnvalues("class com.example.Factory" + i + " { public com.example.Product create(...); }");
        }
        assertEquals(5, task.configuration.assumeNoExternalReturnValues.size(),
                "Should accumulate all 5 specifications");
    }

    // Common use cases

    @Test
    public void testAssumenoexternalreturnvalues_stringValueOf() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_stringBuilderToString() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.StringBuilder { public java.lang.String toString(); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_factoryMethods() throws Exception {
        task.assumenoexternalreturnvalues("class com.example.Factory { public com.example.Product create(...); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_builderBuild() throws Exception {
        task.assumenoexternalreturnvalues("class com.example.Builder { public com.example.Product build(); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Integration with other configuration methods

    @Test
    public void testAssumenoexternalreturnvalues_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");

        assertNotNull(task.configuration.keep);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");

        assertFalse(task.configuration.optimize);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_worksWithOptimizations() throws Exception {
        task.optimizations("code/simplification/*");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");

        assertNotNull(task.configuration.optimizations);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_worksWithOtherAssumptions() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Call order flexibility

    @Test
    public void testAssumenoexternalreturnvalues_canBeCalledFirst() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");

        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Realistic usage scenarios

    @Test
    public void testAssumenoexternalreturnvalues_stringConversionOptimization() throws Exception {
        // Optimize String conversion methods
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-optimized.jar");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.assumenoexternalreturnvalues("class java.lang.String { public java.lang.String substring(...); }");
        task.assumenoexternalreturnvalues("class java.lang.String { public java.lang.String toLowerCase(); }");

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_factoryPatternOptimization() throws Exception {
        // Optimize factory pattern methods
        task.assumenoexternalreturnvalues("class com.example.UserFactory { public com.example.User create(...); }");
        task.assumenoexternalreturnvalues("class com.example.ProductFactory { public com.example.Product create(...); }");
        task.assumenoexternalreturnvalues("class com.example.ServiceFactory { public com.example.Service create(...); }");

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_builderPatternOptimization() throws Exception {
        // Optimize builder pattern build methods
        task.assumenoexternalreturnvalues("class com.example.RequestBuilder { public com.example.Request build(); }");
        task.assumenoexternalreturnvalues("class com.example.ResponseBuilder { public com.example.Response build(); }");
        task.assumenoexternalreturnvalues("class com.example.ConfigBuilder { public com.example.Config build(); }");

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_completeBuildConfiguration() throws Exception {
        // Complete optimization configuration
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.assumenoexternalreturnvalues("class com.example.Factory { public com.example.Product create(...); }");
        task.printmapping("mapping.txt");

        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Edge cases and validation

    @Test
    public void testAssumenoexternalreturnvalues_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testAssumenoexternalreturnvalues_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        assertNotNull(task.configuration, "configuration should remain not null");
        assertNotNull(task.configuration.assumeNoExternalReturnValues,
                "assumeNoExternalReturnValues should not be null after call");
    }

    @Test
    public void testAssumenoexternalreturnvalues_listIsModifiable() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        List list = task.configuration.assumeNoExternalReturnValues;
        int initialSize = list.size();

        task.assumenoexternalreturnvalues("class java.lang.StringBuilder { public java.lang.String toString(); }");

        assertEquals(initialSize + 1, list.size(), "List should be modifiable and grow");
    }

    @Test
    public void testAssumenoexternalreturnvalues_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertNull(freshTask.configuration.assumeNoExternalReturnValues);

        freshTask.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");

        assertNotNull(freshTask.configuration.assumeNoExternalReturnValues);
        assertEquals(1, freshTask.configuration.assumeNoExternalReturnValues.size());
    }

    // Different method specifications

    @Test
    public void testAssumenoexternalreturnvalues_methodWithSpecificParameters() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(int); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_methodWithVarargs() throws Exception {
        task.assumenoexternalreturnvalues("class com.example.Factory { public com.example.Product create(...); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_instanceMethod() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public java.lang.String substring(int); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_staticMethod() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(int); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_methodWithReturnType() throws Exception {
        task.assumenoexternalreturnvalues("class com.example.Factory { public com.example.Product create(...); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_allMethodsInClass() throws Exception {
        task.assumenoexternalreturnvalues("class com.example.ImmutableFactory { *; }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Multiple methods in single specification

    @Test
    public void testAssumenoexternalreturnvalues_classWithMultipleMethods() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); public java.lang.String substring(...); public java.lang.String toLowerCase(); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_stringMethods() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.assumenoexternalreturnvalues("class java.lang.String { public java.lang.String substring(...); }");
        task.assumenoexternalreturnvalues("class java.lang.String { public java.lang.String toLowerCase(); }");

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Performance optimization scenarios

    @Test
    public void testAssumenoexternalreturnvalues_optimizationScenario() throws Exception {
        // Optimization scenario: optimize return value usage for better performance
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.assumenoexternalreturnvalues("class com.example.Factory { public com.example.Product create(...); }");

        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_aggressiveOptimization() throws Exception {
        // Aggressive optimization: all factory classes
        task.optimizationpasses(7);
        task.optimizations("*");
        task.assumenoexternalreturnvalues("class com.example.UserFactory { *; }");
        task.assumenoexternalreturnvalues("class com.example.ProductFactory { *; }");
        task.assumenoexternalreturnvalues("class com.example.ServiceFactory { *; }");

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Build variant scenarios

    @Test
    public void testAssumenoexternalreturnvalues_releaseBuildOptimization() throws Exception {
        // Release build: optimize all factory methods
        task.optimizationpasses(7);
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.assumenoexternalreturnvalues("class com.example.Factory { public com.example.Product create(...); }");
        task.assumenoexternalreturnvalues("class com.example.Builder { public com.example.Product build(); }");

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Complex specifications

    @Test
    public void testAssumenoexternalreturnvalues_wildcardClassName() throws Exception {
        task.assumenoexternalreturnvalues("class com.example.**.*Factory { public * create(...); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_interfaceMethod() throws Exception {
        task.assumenoexternalreturnvalues("interface com.example.Factory { public com.example.Product create(...); }");
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Separation from other assumption types

    @Test
    public void testAssumenoexternalreturnvalues_separateFromOtherAssumptions() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
        assertNotSame(task.configuration.assumeNoSideEffects,
                task.configuration.assumeNoExternalReturnValues,
                "Should use separate lists");
        assertNotSame(task.configuration.assumeNoExternalSideEffects,
                task.configuration.assumeNoExternalReturnValues,
                "Should use separate lists");
        assertNotSame(task.configuration.assumeNoEscapingParameters,
                task.configuration.assumeNoExternalReturnValues,
                "Should use separate lists");
    }

    @Test
    public void testAssumenoexternalreturnvalues_allTypesOfAssumptions() throws Exception {
        // Use all types of assumptions together
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenosideeffects("class android.util.Log { public static int v(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class com.example.Builder { public com.example.Builder with(...); }");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");
        task.assumenoescapingparameters("class com.example.Handler { public void handle(...); }");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.assumenoexternalreturnvalues("class com.example.Factory { public com.example.Product create(...); }");

        assertEquals(2, task.configuration.assumeNoSideEffects.size(),
                "Should have 2 no side effects assumptions");
        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size(),
                "Should have 2 no external side effects assumptions");
        assertEquals(2, task.configuration.assumeNoEscapingParameters.size(),
                "Should have 2 no escaping parameters assumptions");
        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size(),
                "Should have 2 no external return values assumptions");
    }

    // Android-specific scenarios

    @Test
    public void testAssumenoexternalreturnvalues_androidFactories() throws Exception {
        // Android factory patterns
        task.assumenoexternalreturnvalues("class android.view.LayoutInflater { public android.view.View inflate(...); }");
        task.assumenoexternalreturnvalues("class android.content.Intent { public static android.content.Intent createChooser(...); }");

        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_androidCompleteConfiguration() throws Exception {
        // Complete Android optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.optimizationpasses(5);
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.assumenoexternalreturnvalues("class com.example.Factory { public com.example.Product create(...); }");

        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Common factory and builder patterns

    @Test
    public void testAssumenoexternalreturnvalues_commonJavaFactories() throws Exception {
        // Common Java factory methods
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.assumenoexternalreturnvalues("class java.lang.Integer { public static java.lang.Integer valueOf(int); }");
        task.assumenoexternalreturnvalues("class java.util.Arrays { public static java.util.List asList(...); }");

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvalues_immutableCollections() throws Exception {
        // Immutable collection factories
        task.assumenoexternalreturnvalues("class com.google.common.collect.ImmutableList { public static com.google.common.collect.ImmutableList of(...); }");
        task.assumenoexternalreturnvalues("class com.google.common.collect.ImmutableSet { public static com.google.common.collect.ImmutableSet of(...); }");

        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    // ========================================
    // Tests for assumenoexternalreturnvalues(Map, Closure) method
    // ========================================

    // Basic functionality tests for assumenoexternalreturnvalues(Map, Closure)

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_emptyMap() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        assertNull(task.configuration.assumeNoExternalReturnValues,
                "assumeNoExternalReturnValues should initially be null");
        task.assumenoexternalreturnvalues(args, null);
        assertNotNull(task.configuration.assumeNoExternalReturnValues,
                "assumeNoExternalReturnValues should not be null after call");
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size(),
                "assumeNoExternalReturnValues should contain one specification");
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_addsSpecification() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");

        task.assumenoexternalreturnvalues(args, null);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_withNameAttribute() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Factory");

        task.assumenoexternalreturnvalues(args, null);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_withAccessModifiers() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("name", "com.example.Builder");

        task.assumenoexternalreturnvalues(args, null);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_withTypeClass() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "class");
        args.put("name", "java.lang.String");

        task.assumenoexternalreturnvalues(args, null);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_withTypeInterface() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "interface");
        args.put("name", "com.example.Factory");

        task.assumenoexternalreturnvalues(args, null);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Multiple calls tests

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_multipleCallsAccumulate() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.String");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.StringBuilder");

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Factory");

        task.assumenoexternalreturnvalues(args1, null);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());

        task.assumenoexternalreturnvalues(args2, null);
        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());

        task.assumenoexternalreturnvalues(args3, null);
        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_accumulatesSpecifications() throws Exception {
        assertNull(task.configuration.assumeNoExternalReturnValues);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.String");

        task.assumenoexternalreturnvalues(args1, null);
        List firstCall = task.configuration.assumeNoExternalReturnValues;
        assertNotNull(firstCall);
        assertEquals(1, firstCall.size());

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.StringBuilder");

        task.assumenoexternalreturnvalues(args2, null);
        List secondCall = task.configuration.assumeNoExternalReturnValues;
        assertSame(firstCall, secondCall, "Should reuse same list");
        assertEquals(2, secondCall.size(), "Should accumulate specifications");
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("name", "com.example.Factory" + i);
            task.assumenoexternalreturnvalues(args, null);
        }
        assertEquals(5, task.configuration.assumeNoExternalReturnValues.size(),
                "Should accumulate all 5 specifications");
    }

    // Integration with String method

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_worksWithStringMethod() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");
        task.assumenoexternalreturnvalues(args, null);

        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size(),
                "Both methods should add to same list");
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_stringMethodFirst() throws Exception {
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.assumenoexternalreturnvalues("class java.lang.String { public java.lang.String substring(...); }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Factory");
        task.assumenoexternalreturnvalues(args, null);

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_mapMethodFirst() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Factory");
        task.assumenoexternalreturnvalues(args1, null);

        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");
        task.assumenoexternalreturnvalues("class java.lang.String { public java.lang.String substring(...); }");

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Integration with other configuration methods

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args, null);

        assertNotNull(task.configuration.keep);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_worksWithDontOptimize() throws Exception {
        task.dontoptimize();

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args, null);

        assertFalse(task.configuration.optimize);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_worksWithOptimizations() throws Exception {
        task.optimizations("code/simplification/*");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args, null);

        assertNotNull(task.configuration.optimizations);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_worksWithOtherAssumptions() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args, null);

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Call order flexibility

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_canBeCalledFirst() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args, null);

        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args, null);

        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args, null);

        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Realistic usage scenarios

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_stringConversionOptimization() throws Exception {
        // Optimize String conversion using Map syntax
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");

        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-optimized.jar");
        task.assumenoexternalreturnvalues(args, null);

        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_factoryPatternOptimization() throws Exception {
        // Optimize factory patterns
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.UserFactory");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.ProductFactory");

        task.assumenoexternalreturnvalues(args1, null);
        task.assumenoexternalreturnvalues(args2, null);

        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_builderPatternOptimization() throws Exception {
        // Optimize builder patterns
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.RequestBuilder");

        task.assumenoexternalreturnvalues(args, null);

        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_completeBuildConfiguration() throws Exception {
        // Complete release build configuration using Map syntax
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Factory");
        task.assumenoexternalreturnvalues(args2, null);

        task.printmapping("mapping.txt");

        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Edge cases and validation

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_nullClosureIsValid() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");

        // null Closure is valid - commonly used in Java tests
        task.assumenoexternalreturnvalues(args, null);

        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args, null);

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args, null);

        assertNotNull(task.configuration, "configuration should remain not null");
        assertNotNull(task.configuration.assumeNoExternalReturnValues,
                "assumeNoExternalReturnValues should not be null after call");
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_listIsModifiable() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args1, null);

        List list = task.configuration.assumeNoExternalReturnValues;
        int initialSize = list.size();

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.StringBuilder");
        task.assumenoexternalreturnvalues(args2, null);

        assertEquals(initialSize + 1, list.size(), "List should be modifiable and grow");
    }

    // Complex Map specifications

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_withMultipleAttributes() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("type", "class");
        args.put("name", "java.lang.String");

        task.assumenoexternalreturnvalues(args, null);

        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_wildcardInName() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.**.*Factory");

        task.assumenoexternalreturnvalues(args, null);

        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_withModifiers() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public static");
        args.put("name", "java.lang.String");

        task.assumenoexternalreturnvalues(args, null);

        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Performance optimization scenarios

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_optimizationScenario() throws Exception {
        // Optimization scenario: optimize return value usage for better performance
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Factory");
        task.assumenoexternalreturnvalues(args2, null);

        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_aggressiveOptimization() throws Exception {
        // Aggressive optimization: all factory classes
        task.optimizationpasses(7);
        task.optimizations("*");

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.UserFactory");
        task.assumenoexternalreturnvalues(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.ProductFactory");
        task.assumenoexternalreturnvalues(args2, null);

        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Build variant scenarios

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_releaseBuild() throws Exception {
        // Release build: optimize all factory methods
        task.optimizationpasses(7);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Factory");
        task.assumenoexternalreturnvalues(args2, null);

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Builder");
        task.assumenoexternalreturnvalues(args3, null);

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Separation from other assumption types

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_separateFromOtherAssumptions() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.util.Log");
        task.assumenosideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args2, null);

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args3, null);

        java.util.Map<String, Object> args4 = new java.util.HashMap<>();
        args4.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args4, null);

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertNotNull(task.configuration.assumeNoExternalReturnValues);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
        assertEquals(1, task.configuration.assumeNoExternalReturnValues.size());
        assertNotSame(task.configuration.assumeNoSideEffects,
                task.configuration.assumeNoExternalReturnValues,
                "Should use separate lists");
        assertNotSame(task.configuration.assumeNoExternalSideEffects,
                task.configuration.assumeNoExternalReturnValues,
                "Should use separate lists");
        assertNotSame(task.configuration.assumeNoEscapingParameters,
                task.configuration.assumeNoExternalReturnValues,
                "Should use separate lists");
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_allTypesOfAssumptions() throws Exception {
        // Use all types of assumptions together with Map syntax
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

        java.util.Map<String, Object> args5 = new java.util.HashMap<>();
        args5.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args5, null);

        java.util.Map<String, Object> args6 = new java.util.HashMap<>();
        args6.put("name", "com.example.Handler");
        task.assumenoescapingparameters(args6, null);

        java.util.Map<String, Object> args7 = new java.util.HashMap<>();
        args7.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args7, null);

        java.util.Map<String, Object> args8 = new java.util.HashMap<>();
        args8.put("name", "com.example.Factory");
        task.assumenoexternalreturnvalues(args8, null);

        assertEquals(2, task.configuration.assumeNoSideEffects.size(),
                "Should have 2 no side effects assumptions");
        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size(),
                "Should have 2 no external side effects assumptions");
        assertEquals(2, task.configuration.assumeNoEscapingParameters.size(),
                "Should have 2 no escaping parameters assumptions");
        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size(),
                "Should have 2 no external return values assumptions");
    }

    // Android-specific scenarios

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_androidFactories() throws Exception {
        // Android factory patterns using Map syntax
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.view.LayoutInflater");
        task.assumenoexternalreturnvalues(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "android.content.Intent");
        task.assumenoexternalreturnvalues(args2, null);

        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_androidCompleteConfiguration() throws Exception {
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
        args3.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args3, null);

        java.util.Map<String, Object> args4 = new java.util.HashMap<>();
        args4.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args4, null);

        java.util.Map<String, Object> args5 = new java.util.HashMap<>();
        args5.put("name", "com.example.Factory");
        task.assumenoexternalreturnvalues(args5, null);

        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Common factory and builder patterns

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_commonJavaFactories() throws Exception {
        // Common Java factory methods using Map syntax
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "java.lang.String");
        task.assumenoexternalreturnvalues(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.Integer");
        task.assumenoexternalreturnvalues(args2, null);

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "java.util.Arrays");
        task.assumenoexternalreturnvalues(args3, null);

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size());
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_immutableCollections() throws Exception {
        // Immutable collection factories using Map syntax
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.google.common.collect.ImmutableList");
        task.assumenoexternalreturnvalues(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.google.common.collect.ImmutableSet");
        task.assumenoexternalreturnvalues(args2, null);

        assertEquals(2, task.configuration.assumeNoExternalReturnValues.size());
    }

    // Mixed usage with String and Map methods

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_mixedWithStringMethod() throws Exception {
        // Mix String method (detailed method specs) with Map method (class specs)
        task.assumenoexternalreturnvalues("class java.lang.String { public static java.lang.String valueOf(...); }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "java.lang.StringBuilder");
        task.assumenoexternalreturnvalues(args, null);

        task.assumenoexternalreturnvalues("class com.example.Factory { public com.example.Product create(...); }");

        assertEquals(3, task.configuration.assumeNoExternalReturnValues.size(),
                "Should mix String and Map methods seamlessly");
    }

    @Test
    public void testAssumenoexternalreturnvaluesWithMapClosure_alternatingMethods() throws Exception {
        for (int i = 0; i < 3; i++) {
            // Alternate between String and Map methods
            task.assumenoexternalreturnvalues("class com.example.Factory" + i + " { public com.example.Product create(...); }");

            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("name", "com.example.Builder" + i);
            task.assumenoexternalreturnvalues(args, null);
        }

        assertEquals(6, task.configuration.assumeNoExternalReturnValues.size(),
                "Should accumulate all specifications from both methods");
    }
}

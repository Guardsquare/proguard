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
 * Test class for ProGuardTask.assumenoescapingparameters methods.
 * Tests the assumenoescapingparameters(String)V and assumenoescapingparameters(Map, Closure)V
 * methods that add class specifications to configuration.assumeNoEscapingParameters, telling
 * ProGuard that certain method parameters never escape (e.g., they are not stored in fields
 * or passed to other methods) and can be safely optimized.
 */
public class ProGuardTaskClaude_assumenoescapingparametersTest {

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
    public void testAssumenoescapingparameters_addsClassSpecification() throws Exception {
        assertNull(task.configuration.assumeNoEscapingParameters,
                "assumeNoEscapingParameters should initially be null");
        task.assumenoescapingparameters("class java.lang.StringBuilder { public java.lang.StringBuilder append(java.lang.String); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters,
                "assumeNoEscapingParameters should not be null after call");
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size(),
                "assumeNoEscapingParameters should contain one specification");
    }

    @Test
    public void testAssumenoescapingparameters_simpleMethod() throws Exception {
        task.assumenoescapingparameters("class com.example.Processor { public void process(java.lang.String); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_customClass() throws Exception {
        task.assumenoescapingparameters("class com.example.Handler { public void handle(com.example.Event); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_multipleMethods() throws Exception {
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); public void handle(...); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    // Multiple calls tests

    @Test
    public void testAssumenoescapingparameters_multipleCallsAccumulate() throws Exception {
        task.assumenoescapingparameters("class java.lang.StringBuilder { public java.lang.StringBuilder append(java.lang.String); }");
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());

        task.assumenoescapingparameters("class java.lang.StringBuffer { public java.lang.StringBuffer append(java.lang.String); }");
        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());

        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");
        assertEquals(3, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_accumulatesSpecifications() throws Exception {
        assertNull(task.configuration.assumeNoEscapingParameters);

        task.assumenoescapingparameters("class java.lang.StringBuilder { public java.lang.StringBuilder append(java.lang.String); }");
        List firstCall = task.configuration.assumeNoEscapingParameters;
        assertNotNull(firstCall);
        assertEquals(1, firstCall.size());

        task.assumenoescapingparameters("class java.lang.StringBuffer { public java.lang.StringBuffer append(java.lang.String); }");
        List secondCall = task.configuration.assumeNoEscapingParameters;
        assertSame(firstCall, secondCall, "Should reuse same list");
        assertEquals(2, secondCall.size(), "Should accumulate specifications");
    }

    @Test
    public void testAssumenoescapingparameters_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.assumenoescapingparameters("class com.example.Processor" + i + " { public void process(...); }");
        }
        assertEquals(5, task.configuration.assumeNoEscapingParameters.size(),
                "Should accumulate all 5 specifications");
    }

    // Common use cases

    @Test
    public void testAssumenoescapingparameters_stringBuilderAppend() throws Exception {
        task.assumenoescapingparameters("class java.lang.StringBuilder { public java.lang.StringBuilder append(java.lang.String); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_processorMethods() throws Exception {
        task.assumenoescapingparameters("class com.example.DataProcessor { public void process(byte[]); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_handlerMethods() throws Exception {
        task.assumenoescapingparameters("class com.example.EventHandler { public void onEvent(com.example.Event); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_converterMethods() throws Exception {
        task.assumenoescapingparameters("class com.example.Converter { public java.lang.String convert(java.lang.Object); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    // Integration with other configuration methods

    @Test
    public void testAssumenoescapingparameters_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");

        assertNotNull(task.configuration.keep);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");

        assertFalse(task.configuration.optimize);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_worksWithOptimizations() throws Exception {
        task.optimizations("code/simplification/*");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");

        assertNotNull(task.configuration.optimizations);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_worksWithOtherAssumptions() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    // Call order flexibility

    @Test
    public void testAssumenoescapingparameters_canBeCalledFirst() throws Exception {
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");
        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");

        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");
        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    // Realistic usage scenarios

    @Test
    public void testAssumenoescapingparameters_dataProcessingOptimization() throws Exception {
        // Optimize data processing methods where parameters don't escape
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-optimized.jar");
        task.assumenoescapingparameters("class com.example.DataProcessor { public byte[] process(byte[]); }");
        task.assumenoescapingparameters("class com.example.DataProcessor { public void transform(byte[]); }");
        task.assumenoescapingparameters("class com.example.DataProcessor { public void validate(byte[]); }");

        assertEquals(3, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_eventHandlerOptimization() throws Exception {
        // Optimize event handlers where events don't escape
        task.assumenoescapingparameters("class com.example.EventHandler { public void onEvent(com.example.Event); }");
        task.assumenoescapingparameters("class com.example.EventHandler { public void handleClick(android.view.View); }");
        task.assumenoescapingparameters("class com.example.EventHandler { public void handleError(java.lang.Throwable); }");

        assertEquals(3, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_converterOptimization() throws Exception {
        // Optimize converter methods where input doesn't escape
        task.assumenoescapingparameters("class com.example.Converter { public java.lang.String toString(java.lang.Object); }");
        task.assumenoescapingparameters("class com.example.Converter { public int toInt(java.lang.String); }");
        task.assumenoescapingparameters("class com.example.Converter { public byte[] toBytes(java.lang.String); }");

        assertEquals(3, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_completeBuildConfiguration() throws Exception {
        // Complete optimization configuration
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);
        task.assumenoescapingparameters("class com.example.Processor { public void process(byte[]); }");
        task.assumenoescapingparameters("class com.example.Handler { public void handle(com.example.Event); }");
        task.printmapping("mapping.txt");

        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    // Edge cases and validation

    @Test
    public void testAssumenoescapingparameters_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testAssumenoescapingparameters_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");
        assertNotNull(task.configuration, "configuration should remain not null");
        assertNotNull(task.configuration.assumeNoEscapingParameters,
                "assumeNoEscapingParameters should not be null after call");
    }

    @Test
    public void testAssumenoescapingparameters_listIsModifiable() throws Exception {
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");
        List list = task.configuration.assumeNoEscapingParameters;
        int initialSize = list.size();

        task.assumenoescapingparameters("class com.example.Handler { public void handle(...); }");

        assertEquals(initialSize + 1, list.size(), "List should be modifiable and grow");
    }

    @Test
    public void testAssumenoescapingparameters_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertNull(freshTask.configuration.assumeNoEscapingParameters);

        freshTask.assumenoescapingparameters("class com.example.Processor { public void process(...); }");

        assertNotNull(freshTask.configuration.assumeNoEscapingParameters);
        assertEquals(1, freshTask.configuration.assumeNoEscapingParameters.size());
    }

    // Different method specifications

    @Test
    public void testAssumenoescapingparameters_methodWithSpecificParameters() throws Exception {
        task.assumenoescapingparameters("class com.example.Processor { public void process(byte[], int, int); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_methodWithVarargs() throws Exception {
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_instanceMethod() throws Exception {
        task.assumenoescapingparameters("class com.example.Processor { public void process(java.lang.String); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_staticMethod() throws Exception {
        task.assumenoescapingparameters("class com.example.Utils { public static java.lang.String format(java.lang.String); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_methodWithReturnType() throws Exception {
        task.assumenoescapingparameters("class com.example.Converter { public java.lang.String convert(java.lang.Object); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_allMethodsInClass() throws Exception {
        task.assumenoescapingparameters("class com.example.SafeProcessor { *; }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    // Multiple methods in single specification

    @Test
    public void testAssumenoescapingparameters_classWithMultipleMethods() throws Exception {
        task.assumenoescapingparameters("class com.example.Processor { public void process(byte[]); public void handle(java.lang.String); public void transform(...); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_stringBuilderMethods() throws Exception {
        task.assumenoescapingparameters("class java.lang.StringBuilder { public java.lang.StringBuilder append(java.lang.String); }");
        task.assumenoescapingparameters("class java.lang.StringBuilder { public java.lang.StringBuilder insert(int, java.lang.String); }");
        task.assumenoescapingparameters("class java.lang.StringBuilder { public java.lang.StringBuilder replace(int, int, java.lang.String); }");

        assertEquals(3, task.configuration.assumeNoEscapingParameters.size());
    }

    // Performance optimization scenarios

    @Test
    public void testAssumenoescapingparameters_optimizationScenario() throws Exception {
        // Optimization scenario: optimize parameter usage for better performance
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");
        task.assumenoescapingparameters("class com.example.Processor { public void process(byte[]); }");
        task.assumenoescapingparameters("class com.example.Handler { public void handle(com.example.Event); }");

        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_aggressiveOptimization() throws Exception {
        // Aggressive optimization: all processor classes
        task.optimizationpasses(7);
        task.optimizations("*");
        task.assumenoescapingparameters("class com.example.DataProcessor { *; }");
        task.assumenoescapingparameters("class com.example.EventProcessor { *; }");
        task.assumenoescapingparameters("class com.example.StreamProcessor { *; }");

        assertEquals(3, task.configuration.assumeNoEscapingParameters.size());
    }

    // Build variant scenarios

    @Test
    public void testAssumenoescapingparameters_releaseBuildOptimization() throws Exception {
        // Release build: optimize all processor methods
        task.optimizationpasses(7);
        task.assumenoescapingparameters("class com.example.DataProcessor { public void process(byte[]); }");
        task.assumenoescapingparameters("class com.example.EventHandler { public void onEvent(com.example.Event); }");
        task.assumenoescapingparameters("class com.example.Converter { public java.lang.String convert(java.lang.Object); }");

        assertEquals(3, task.configuration.assumeNoEscapingParameters.size());
    }

    // Complex specifications

    @Test
    public void testAssumenoescapingparameters_wildcardClassName() throws Exception {
        task.assumenoescapingparameters("class com.example.**.*Processor { public void process(...); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_interfaceMethod() throws Exception {
        task.assumenoescapingparameters("interface com.example.Processor { public void process(byte[]); }");
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    // Separation from other assumption types

    @Test
    public void testAssumenoescapingparameters_separateFromOtherAssumptions() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
        assertNotSame(task.configuration.assumeNoSideEffects,
                task.configuration.assumeNoEscapingParameters,
                "Should use separate lists");
        assertNotSame(task.configuration.assumeNoExternalSideEffects,
                task.configuration.assumeNoEscapingParameters,
                "Should use separate lists");
    }

    @Test
    public void testAssumenoescapingparameters_allTypesOfAssumptions() throws Exception {
        // Use all types of assumptions together
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenosideeffects("class android.util.Log { public static int v(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoexternalsideeffects("class com.example.Builder { public com.example.Builder with(...); }");
        task.assumenoescapingparameters("class com.example.Processor { public void process(...); }");
        task.assumenoescapingparameters("class com.example.Handler { public void handle(...); }");

        assertEquals(2, task.configuration.assumeNoSideEffects.size(),
                "Should have 2 no side effects assumptions");
        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size(),
                "Should have 2 no external side effects assumptions");
        assertEquals(2, task.configuration.assumeNoEscapingParameters.size(),
                "Should have 2 no escaping parameters assumptions");
    }

    // Android-specific scenarios

    @Test
    public void testAssumenoescapingparameters_androidEventHandlers() throws Exception {
        // Android event handler patterns
        task.assumenoescapingparameters("class com.example.MainActivity { public void onClick(android.view.View); }");
        task.assumenoescapingparameters("class com.example.MainActivity { public void onLongClick(android.view.View); }");

        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_androidCompleteConfiguration() throws Exception {
        // Complete Android optimization
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.optimizationpasses(5);
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");
        task.assumenoescapingparameters("class com.example.DataProcessor { public void process(byte[]); }");
        task.assumenoescapingparameters("class com.example.EventHandler { public void onEvent(com.example.Event); }");

        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    // Data processing frameworks

    @Test
    public void testAssumenoescapingparameters_dataProcessingPipeline() throws Exception {
        // Data processing pipeline where parameters don't escape
        task.assumenoescapingparameters("class com.example.pipeline.Reader { public byte[] read(java.io.InputStream); }");
        task.assumenoescapingparameters("class com.example.pipeline.Parser { public com.example.Data parse(byte[]); }");
        task.assumenoescapingparameters("class com.example.pipeline.Validator { public boolean validate(com.example.Data); }");
        task.assumenoescapingparameters("class com.example.pipeline.Writer { public void write(com.example.Data); }");

        assertEquals(4, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparameters_streamProcessing() throws Exception {
        // Stream processing methods
        task.assumenoescapingparameters("class com.example.StreamProcessor { public void process(java.io.InputStream); }");
        task.assumenoescapingparameters("class com.example.StreamProcessor { public byte[] read(java.io.InputStream, int); }");

        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    // ========================================
    // Tests for assumenoescapingparameters(Map, Closure) method
    // ========================================

    // Basic functionality tests for assumenoescapingparameters(Map, Closure)

    @Test
    public void testAssumenoescapingparametersWithMapClosure_emptyMap() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        assertNull(task.configuration.assumeNoEscapingParameters,
                "assumeNoEscapingParameters should initially be null");
        task.assumenoescapingparameters(args, null);
        assertNotNull(task.configuration.assumeNoEscapingParameters,
                "assumeNoEscapingParameters should not be null after call");
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size(),
                "assumeNoEscapingParameters should contain one specification");
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_addsSpecification() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Processor");

        task.assumenoescapingparameters(args, null);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_withNameAttribute() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Handler");

        task.assumenoescapingparameters(args, null);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_withAccessModifiers() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("name", "com.example.DataProcessor");

        task.assumenoescapingparameters(args, null);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_withTypeClass() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "class");
        args.put("name", "com.example.Processor");

        task.assumenoescapingparameters(args, null);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_withTypeInterface() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "interface");
        args.put("name", "com.example.Handler");

        task.assumenoescapingparameters(args, null);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    // Multiple calls tests

    @Test
    public void testAssumenoescapingparametersWithMapClosure_multipleCallsAccumulate() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Processor");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Handler");

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Converter");

        task.assumenoescapingparameters(args1, null);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());

        task.assumenoescapingparameters(args2, null);
        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());

        task.assumenoescapingparameters(args3, null);
        assertEquals(3, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_accumulatesSpecifications() throws Exception {
        assertNull(task.configuration.assumeNoEscapingParameters);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Processor");

        task.assumenoescapingparameters(args1, null);
        List firstCall = task.configuration.assumeNoEscapingParameters;
        assertNotNull(firstCall);
        assertEquals(1, firstCall.size());

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Handler");

        task.assumenoescapingparameters(args2, null);
        List secondCall = task.configuration.assumeNoEscapingParameters;
        assertSame(firstCall, secondCall, "Should reuse same list");
        assertEquals(2, secondCall.size(), "Should accumulate specifications");
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("name", "com.example.Processor" + i);
            task.assumenoescapingparameters(args, null);
        }
        assertEquals(5, task.configuration.assumeNoEscapingParameters.size(),
                "Should accumulate all 5 specifications");
    }

    // Integration with String method

    @Test
    public void testAssumenoescapingparametersWithMapClosure_worksWithStringMethod() throws Exception {
        task.assumenoescapingparameters("class com.example.Processor { public void process(byte[]); }");
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Handler");
        task.assumenoescapingparameters(args, null);

        assertEquals(2, task.configuration.assumeNoEscapingParameters.size(),
                "Both methods should add to same list");
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_stringMethodFirst() throws Exception {
        task.assumenoescapingparameters("class com.example.Processor { public void process(byte[]); }");
        task.assumenoescapingparameters("class com.example.Processor { public void handle(java.lang.String); }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Handler");
        task.assumenoescapingparameters(args, null);

        assertEquals(3, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_mapMethodFirst() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Handler");
        task.assumenoescapingparameters(args1, null);

        task.assumenoescapingparameters("class com.example.Processor { public void process(byte[]); }");
        task.assumenoescapingparameters("class com.example.Processor { public void handle(java.lang.String); }");

        assertEquals(3, task.configuration.assumeNoEscapingParameters.size());
    }

    // Integration with other configuration methods

    @Test
    public void testAssumenoescapingparametersWithMapClosure_worksWithKeep() throws Exception {
        task.keep("class com.example.MyClass { *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args, null);

        assertNotNull(task.configuration.keep);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_worksWithDontOptimize() throws Exception {
        task.dontoptimize();

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args, null);

        assertFalse(task.configuration.optimize);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_worksWithOptimizations() throws Exception {
        task.optimizations("code/simplification/*");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args, null);

        assertNotNull(task.configuration.optimizations);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_worksWithOtherAssumptions() throws Exception {
        task.assumenosideeffects("class android.util.Log { public static int d(...); }");
        task.assumenoexternalsideeffects("class java.lang.StringBuilder { public java.lang.StringBuilder append(...); }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args, null);

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    // Call order flexibility

    @Test
    public void testAssumenoescapingparametersWithMapClosure_canBeCalledFirst() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args, null);

        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.optimizations("code/simplification/*");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args, null);

        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args, null);

        task.optimizations("code/simplification/*");

        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    // Realistic usage scenarios

    @Test
    public void testAssumenoescapingparametersWithMapClosure_dataProcessorOptimization() throws Exception {
        // Optimize data processor using Map syntax
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.DataProcessor");

        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-optimized.jar");
        task.assumenoescapingparameters(args, null);

        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_eventHandlerOptimization() throws Exception {
        // Optimize event handlers
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.EventHandler");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.ClickHandler");

        task.assumenoescapingparameters(args1, null);
        task.assumenoescapingparameters(args2, null);

        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_converterOptimization() throws Exception {
        // Optimize converter classes
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Converter");

        task.assumenoescapingparameters(args, null);

        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_completeBuildConfiguration() throws Exception {
        // Complete release build configuration using Map syntax
        task.injars("build/libs/app.jar");
        task.outjars("build/libs/app-release.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");
        task.optimizationpasses(5);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Handler");
        task.assumenoescapingparameters(args2, null);

        task.printmapping("mapping.txt");

        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    // Edge cases and validation

    @Test
    public void testAssumenoescapingparametersWithMapClosure_nullClosureIsValid() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Processor");

        // null Closure is valid - commonly used in Java tests
        task.assumenoescapingparameters(args, null);

        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_doesNotAffectOtherSettings() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args, null);

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args, null);

        assertNotNull(task.configuration, "configuration should remain not null");
        assertNotNull(task.configuration.assumeNoEscapingParameters,
                "assumeNoEscapingParameters should not be null after call");
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_listIsModifiable() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args1, null);

        List list = task.configuration.assumeNoEscapingParameters;
        int initialSize = list.size();

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Handler");
        task.assumenoescapingparameters(args2, null);

        assertEquals(initialSize + 1, list.size(), "List should be modifiable and grow");
    }

    // Complex Map specifications

    @Test
    public void testAssumenoescapingparametersWithMapClosure_withMultipleAttributes() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("type", "class");
        args.put("name", "com.example.Processor");

        task.assumenoescapingparameters(args, null);

        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_wildcardInName() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.**.*Processor");

        task.assumenoescapingparameters(args, null);

        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_withModifiers() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public final");
        args.put("name", "com.example.Processor");

        task.assumenoescapingparameters(args, null);

        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
    }

    // Performance optimization scenarios

    @Test
    public void testAssumenoescapingparametersWithMapClosure_optimizationScenario() throws Exception {
        // Optimization scenario: optimize parameter usage for better performance
        task.optimizationpasses(5);
        task.optimizations("code/simplification/*");

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Handler");
        task.assumenoescapingparameters(args2, null);

        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_aggressiveOptimization() throws Exception {
        // Aggressive optimization: all processor classes
        task.optimizationpasses(7);
        task.optimizations("*");

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.DataProcessor");
        task.assumenoescapingparameters(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.EventProcessor");
        task.assumenoescapingparameters(args2, null);

        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    // Build variant scenarios

    @Test
    public void testAssumenoescapingparametersWithMapClosure_releaseBuild() throws Exception {
        // Release build: optimize all processor methods
        task.optimizationpasses(7);

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.DataProcessor");
        task.assumenoescapingparameters(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.EventHandler");
        task.assumenoescapingparameters(args2, null);

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Converter");
        task.assumenoescapingparameters(args3, null);

        assertEquals(3, task.configuration.assumeNoEscapingParameters.size());
    }

    // Separation from other assumption types

    @Test
    public void testAssumenoescapingparametersWithMapClosure_separateFromOtherAssumptions() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "android.util.Log");
        task.assumenosideeffects(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "java.lang.StringBuilder");
        task.assumenoexternalsideeffects(args2, null);

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Processor");
        task.assumenoescapingparameters(args3, null);

        assertNotNull(task.configuration.assumeNoSideEffects);
        assertNotNull(task.configuration.assumeNoExternalSideEffects);
        assertNotNull(task.configuration.assumeNoEscapingParameters);
        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(1, task.configuration.assumeNoEscapingParameters.size());
        assertNotSame(task.configuration.assumeNoSideEffects,
                task.configuration.assumeNoEscapingParameters,
                "Should use separate lists");
        assertNotSame(task.configuration.assumeNoExternalSideEffects,
                task.configuration.assumeNoEscapingParameters,
                "Should use separate lists");
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_allTypesOfAssumptions() throws Exception {
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

        assertEquals(2, task.configuration.assumeNoSideEffects.size(),
                "Should have 2 no side effects assumptions");
        assertEquals(2, task.configuration.assumeNoExternalSideEffects.size(),
                "Should have 2 no external side effects assumptions");
        assertEquals(2, task.configuration.assumeNoEscapingParameters.size(),
                "Should have 2 no escaping parameters assumptions");
    }

    // Android-specific scenarios

    @Test
    public void testAssumenoescapingparametersWithMapClosure_androidEventHandlers() throws Exception {
        // Android event handler patterns using Map syntax
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.MainActivity");
        task.assumenoescapingparameters(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.ClickHandler");
        task.assumenoescapingparameters(args2, null);

        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_androidCompleteConfiguration() throws Exception {
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
        args3.put("name", "com.example.DataProcessor");
        task.assumenoescapingparameters(args3, null);

        java.util.Map<String, Object> args4 = new java.util.HashMap<>();
        args4.put("name", "com.example.EventHandler");
        task.assumenoescapingparameters(args4, null);

        assertEquals(1, task.configuration.assumeNoSideEffects.size());
        assertEquals(1, task.configuration.assumeNoExternalSideEffects.size());
        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    // Data processing frameworks

    @Test
    public void testAssumenoescapingparametersWithMapClosure_dataProcessingPipeline() throws Exception {
        // Data processing pipeline using Map syntax
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.pipeline.Reader");
        task.assumenoescapingparameters(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.pipeline.Parser");
        task.assumenoescapingparameters(args2, null);

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.pipeline.Validator");
        task.assumenoescapingparameters(args3, null);

        java.util.Map<String, Object> args4 = new java.util.HashMap<>();
        args4.put("name", "com.example.pipeline.Writer");
        task.assumenoescapingparameters(args4, null);

        assertEquals(4, task.configuration.assumeNoEscapingParameters.size());
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_streamProcessing() throws Exception {
        // Stream processing methods using Map syntax
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.StreamProcessor");
        task.assumenoescapingparameters(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.StreamReader");
        task.assumenoescapingparameters(args2, null);

        assertEquals(2, task.configuration.assumeNoEscapingParameters.size());
    }

    // Mixed usage with String and Map methods

    @Test
    public void testAssumenoescapingparametersWithMapClosure_mixedWithStringMethod() throws Exception {
        // Mix String method (detailed method specs) with Map method (class specs)
        task.assumenoescapingparameters("class com.example.Processor { public void process(byte[]); }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Handler");
        task.assumenoescapingparameters(args, null);

        task.assumenoescapingparameters("class com.example.Converter { public java.lang.String convert(java.lang.Object); }");

        assertEquals(3, task.configuration.assumeNoEscapingParameters.size(),
                "Should mix String and Map methods seamlessly");
    }

    @Test
    public void testAssumenoescapingparametersWithMapClosure_alternatingMethods() throws Exception {
        for (int i = 0; i < 3; i++) {
            // Alternate between String and Map methods
            task.assumenoescapingparameters("class com.example.Processor" + i + " { public void process(...); }");

            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("name", "com.example.Handler" + i);
            task.assumenoescapingparameters(args, null);
        }

        assertEquals(6, task.configuration.assumeNoEscapingParameters.size(),
                "Should accumulate all specifications from both methods");
    }
}

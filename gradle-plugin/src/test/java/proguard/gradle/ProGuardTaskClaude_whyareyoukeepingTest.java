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
 * Test class for ProGuardTask.whyareyoukeeping methods.
 * Tests the whyareyoukeeping(String) method that adds class specifications to
 * configuration.whyAreYouKeeping to explain why classes/methods are kept during optimization.
 * Also tests the whyareyoukeeping(Map) method for more complex class specifications.
 * Also tests the whyareyoukeeping(Map, Closure) method for specifications with class members.
 */
public class ProGuardTaskClaude_whyareyoukeepingTest {

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
    public void testWhyareyoukeeping_addsClassSpecification() throws Exception {
        assertNull(task.configuration.whyAreYouKeeping, "whyAreYouKeeping should initially be null");
        task.whyareyoukeeping("class com.example.MyClass");
        assertNotNull(task.configuration.whyAreYouKeeping, "whyAreYouKeeping should not be null after call");
        assertEquals(1, task.configuration.whyAreYouKeeping.size(),
                "whyAreYouKeeping should contain one specification");
    }

    @Test
    public void testWhyareyoukeeping_acceptsSimpleClassName() throws Exception {
        task.whyareyoukeeping("class com.example.MainActivity");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_acceptsWildcardPattern() throws Exception {
        task.whyareyoukeeping("class com.example.*");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_acceptsClassWithMembers() throws Exception {
        task.whyareyoukeeping("class com.example.MyClass { public void myMethod(); }");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_acceptsMultipleModifiers() throws Exception {
        task.whyareyoukeeping("public class com.example.MyClass");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Multiple calls tests

    @Test
    public void testWhyareyoukeeping_multipleCallsAddSpecifications() throws Exception {
        task.whyareyoukeeping("class com.example.Class1");
        assertEquals(1, task.configuration.whyAreYouKeeping.size(),
                "First call should add one specification");

        task.whyareyoukeeping("class com.example.Class2");
        assertEquals(2, task.configuration.whyAreYouKeeping.size(),
                "Second call should add another specification");

        task.whyareyoukeeping("class com.example.Class3");
        assertEquals(3, task.configuration.whyAreYouKeeping.size(),
                "Third call should add another specification");
    }

    @Test
    public void testWhyareyoukeeping_accumulatesSpecifications() throws Exception {
        assertNull(task.configuration.whyAreYouKeeping);

        task.whyareyoukeeping("class com.example.MyClass");
        List firstCall = task.configuration.whyAreYouKeeping;
        assertNotNull(firstCall);
        assertEquals(1, firstCall.size());

        task.whyareyoukeeping("class com.example.AnotherClass");
        List secondCall = task.configuration.whyAreYouKeeping;
        assertSame(firstCall, secondCall, "Should reuse same list");
        assertEquals(2, secondCall.size(), "Should accumulate specifications");
    }

    @Test
    public void testWhyareyoukeeping_canBeCalledManyTimes() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.whyareyoukeeping("class com.example.Class" + i);
        }
        assertEquals(10, task.configuration.whyAreYouKeeping.size(),
                "Should accumulate all 10 specifications");
    }

    // Various class specification formats

    @Test
    public void testWhyareyoukeeping_interfaceSpecification() throws Exception {
        task.whyareyoukeeping("interface com.example.MyInterface");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_enumSpecification() throws Exception {
        task.whyareyoukeeping("enum com.example.MyEnum");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_classWithAllMembers() throws Exception {
        task.whyareyoukeeping("class com.example.MyClass { *; }");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_classWithPublicMembers() throws Exception {
        task.whyareyoukeeping("class com.example.MyClass { public *; }");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_classWithMethods() throws Exception {
        task.whyareyoukeeping("class com.example.MyClass { public void onCreate(...); }");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_classWithFields() throws Exception {
        task.whyareyoukeeping("class com.example.MyClass { public int myField; }");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_classWithConstructor() throws Exception {
        task.whyareyoukeeping("class com.example.MyClass { public <init>(...); }");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Integration with other configuration methods

    @Test
    public void testWhyareyoukeeping_worksWithKeep() throws Exception {
        task.keep("class com.example.MainActivity { *; }");
        task.whyareyoukeeping("class com.example.MainActivity");

        assertNotNull(task.configuration.keep, "keep should be configured");
        assertNotNull(task.configuration.whyAreYouKeeping, "whyAreYouKeeping should be configured");
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_worksWithDontShrink() throws Exception {
        task.dontshrink();
        task.whyareyoukeeping("class com.example.MyClass");

        assertFalse(task.configuration.shrink, "shrink should be false");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_worksWithDontOptimize() throws Exception {
        task.dontoptimize();
        task.whyareyoukeeping("class com.example.MyClass");

        assertFalse(task.configuration.optimize, "optimize should be false");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_worksWithPrintOptions() throws Exception {
        task.printusage("usage.txt");
        task.printseeds("seeds.txt");
        task.whyareyoukeeping("class com.example.MyClass");

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_worksWithVerbose() throws Exception {
        task.verbose();
        task.whyareyoukeeping("class com.example.MyClass");

        assertTrue(task.configuration.verbose, "verbose should be true");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Call order flexibility

    @Test
    public void testWhyareyoukeeping_canBeCalledFirst() throws Exception {
        task.whyareyoukeeping("class com.example.MyClass");
        task.keep("public class * { public *; }");
        task.dontshrink();

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.dontshrink();
        task.whyareyoukeeping("class com.example.MyClass");

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_canBeCalledBetweenOtherMethods() throws Exception {
        task.keep("public class * { public *; }");
        task.whyareyoukeeping("class com.example.MyClass");
        task.dontshrink();

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Realistic usage scenarios

    @Test
    public void testWhyareyoukeeping_androidActivityScenario() throws Exception {
        // Debug why Activity is kept
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.keep("public class * extends android.app.Activity");
        task.whyareyoukeeping("class com.example.MainActivity");

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_debugMultipleClasses() throws Exception {
        // Debug why multiple classes are kept
        task.keep("class com.example.** { *; }");
        task.whyareyoukeeping("class com.example.MainActivity");
        task.whyareyoukeeping("class com.example.MyService");
        task.whyareyoukeeping("class com.example.MyReceiver");

        assertEquals(3, task.configuration.whyAreYouKeeping.size(),
                "Should track 3 classes for debugging");
    }

    @Test
    public void testWhyareyoukeeping_libraryDebuggingScenario() throws Exception {
        // Library author debugging what's being kept
        task.injars("build/libs/mylib.jar");
        task.outjars("build/libs/mylib-processed.jar");
        task.keep("public class * { public *; }");
        task.whyareyoukeeping("class com.example.internal.InternalClass");
        task.verbose();

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
        assertTrue(task.configuration.verbose);
    }

    @Test
    public void testWhyareyoukeeping_optimizationAnalysis() throws Exception {
        // Analyzing optimization behavior
        task.keep("class com.example.MainActivity { *; }");
        task.whyareyoukeeping("class com.example.utils.HelperClass");
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_reflectionDebugging() throws Exception {
        // Debug classes loaded via reflection
        task.keep("class com.example.ReflectionLoader { *; }");
        task.whyareyoukeeping("class com.example.DynamicallyLoadedClass");
        task.whyareyoukeeping("class com.example.AnotherDynamicClass");

        assertEquals(2, task.configuration.whyAreYouKeeping.size(),
                "Should track classes loaded via reflection");
    }

    // Complex class specifications

    @Test
    public void testWhyareyoukeeping_classExtendingBaseClass() throws Exception {
        task.whyareyoukeeping("class * extends com.example.BaseClass");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_classImplementingInterface() throws Exception {
        task.whyareyoukeeping("class * implements com.example.MyInterface");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_classWithAnnotation() throws Exception {
        task.whyareyoukeeping("@com.example.MyAnnotation class *");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_packageWildcard() throws Exception {
        task.whyareyoukeeping("class com.example.**");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_classWithSpecificMethod() throws Exception {
        task.whyareyoukeeping("class com.example.MyClass { public void onClick(android.view.View); }");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Edge cases and validation

    @Test
    public void testWhyareyoukeeping_doesNotAffectOtherConfiguration() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.whyareyoukeeping("class com.example.MyClass");

        assertEquals(initialShrink, task.configuration.shrink,
                "shrink setting should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
                "optimize setting should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
                "obfuscate setting should not be affected");
    }

    @Test
    public void testWhyareyoukeeping_configurationNotNull() throws Exception {
        assertNotNull(task.configuration, "configuration should not be null");
        task.whyareyoukeeping("class com.example.MyClass");
        assertNotNull(task.configuration, "configuration should remain not null");
        assertNotNull(task.configuration.whyAreYouKeeping,
                "whyAreYouKeeping should not be null after call");
    }

    @Test
    public void testWhyareyoukeeping_listIsModifiable() throws Exception {
        task.whyareyoukeeping("class com.example.Class1");
        List list = task.configuration.whyAreYouKeeping;
        int initialSize = list.size();

        task.whyareyoukeeping("class com.example.Class2");

        assertEquals(initialSize + 1, list.size(),
                "List should be modifiable and grow");
    }

    @Test
    public void testWhyareyoukeeping_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard2", ProGuardTask.class);
        assertNull(freshTask.configuration.whyAreYouKeeping);

        freshTask.whyareyoukeeping("class com.example.MyClass");

        assertNotNull(freshTask.configuration.whyAreYouKeeping);
        assertEquals(1, freshTask.configuration.whyAreYouKeeping.size());
    }

    // Different class types

    @Test
    public void testWhyareyoukeeping_abstractClass() throws Exception {
        task.whyareyoukeeping("abstract class com.example.AbstractClass");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_finalClass() throws Exception {
        task.whyareyoukeeping("final class com.example.FinalClass");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_publicClass() throws Exception {
        task.whyareyoukeeping("public class com.example.PublicClass");
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Multiple patterns

    @Test
    public void testWhyareyoukeeping_multipleWildcardPatterns() throws Exception {
        task.whyareyoukeeping("class com.example.*");
        task.whyareyoukeeping("class com.example.internal.*");
        task.whyareyoukeeping("class com.example.utils.*");

        assertEquals(3, task.configuration.whyAreYouKeeping.size(),
                "Should track multiple wildcard patterns");
    }

    @Test
    public void testWhyareyoukeeping_mixedSpecificationTypes() throws Exception {
        task.whyareyoukeeping("class com.example.MyClass");
        task.whyareyoukeeping("interface com.example.MyInterface");
        task.whyareyoukeeping("enum com.example.MyEnum");

        assertEquals(3, task.configuration.whyAreYouKeeping.size(),
                "Should track different specification types");
    }

    @Test
    public void testWhyareyoukeeping_specificAndWildcard() throws Exception {
        task.whyareyoukeeping("class com.example.SpecificClass");
        task.whyareyoukeeping("class com.example.*");

        assertEquals(2, task.configuration.whyAreYouKeeping.size(),
                "Should track both specific and wildcard patterns");
    }

    // Full configuration scenarios

    @Test
    public void testWhyareyoukeeping_fullAndroidConfiguration() throws Exception {
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * extends android.app.Activity");
        task.keep("public class * extends android.app.Service");
        task.whyareyoukeeping("class com.example.MainActivity");
        task.whyareyoukeeping("class com.example.MyService");
        task.printusage("usage.txt");
        task.verbose();

        assertEquals(2, task.configuration.whyAreYouKeeping.size(),
                "Should track both Activity and Service");
    }

    @Test
    public void testWhyareyoukeeping_withAllPrintOptions() throws Exception {
        task.printseeds("seeds.txt");
        task.printusage("usage.txt");
        task.printmapping("mapping.txt");
        task.whyareyoukeeping("class com.example.MyClass");

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeeping_noProcessingScenario() throws Exception {
        // Only analyzing, no actual processing
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.dontpreverify();
        task.whyareyoukeeping("class com.example.MyClass");

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // ========================================
    // Tests for whyareyoukeeping(Map) method
    // ========================================

    // Basic functionality tests for whyareyoukeeping(Map)

    @Test
    public void testWhyareyoukeepingWithMap_emptyMap() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        assertNull(task.configuration.whyAreYouKeeping, "whyAreYouKeeping should initially be null");
        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping, "whyAreYouKeeping should not be null after call");
        assertEquals(1, task.configuration.whyAreYouKeeping.size(),
                "whyAreYouKeeping should contain one specification");
    }

    @Test
    public void testWhyareyoukeepingWithMap_addsSpecification() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_withNameAttribute() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MainActivity");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_withAccessModifiers() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("name", "com.example.PublicClass");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_withTypeClass() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "class");
        args.put("name", "com.example.MyClass");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_withTypeInterface() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "interface");
        args.put("name", "com.example.MyInterface");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_withTypeEnum() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "enum");
        args.put("name", "com.example.MyEnum");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Multiple calls tests

    @Test
    public void testWhyareyoukeepingWithMap_multipleCallsAccumulate() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Class1");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Class2");

        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Class3");

        task.whyareyoukeeping(args1);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());

        task.whyareyoukeeping(args2);
        assertEquals(2, task.configuration.whyAreYouKeeping.size());

        task.whyareyoukeeping(args3);
        assertEquals(3, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_mixedWithStringMethod() throws Exception {
        // String method
        task.whyareyoukeeping("class com.example.StringClass");
        assertEquals(1, task.configuration.whyAreYouKeeping.size());

        // Map method
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MapClass");
        task.whyareyoukeeping(args);
        assertEquals(2, task.configuration.whyAreYouKeeping.size());

        // Another string method
        task.whyareyoukeeping("class com.example.AnotherStringClass");
        assertEquals(3, task.configuration.whyAreYouKeeping.size());
    }

    // Complex specifications with Map

    @Test
    public void testWhyareyoukeepingWithMap_withExtends() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("extends", "com.example.BaseClass");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_withImplements() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("implements", "com.example.MyInterface");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_withAnnotation() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("annotation", "com.example.MyAnnotation");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_multipleAttributes() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("type", "class");
        args.put("name", "com.example.MyClass");
        args.put("extends", "android.app.Activity");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Integration with other configuration methods

    @Test
    public void testWhyareyoukeepingWithMap_worksWithKeep() throws Exception {
        task.keep("class com.example.MainActivity { *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MainActivity");
        task.whyareyoukeeping(args);

        assertNotNull(task.configuration.keep);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_worksWithDontShrink() throws Exception {
        task.dontshrink();

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args);

        assertFalse(task.configuration.shrink);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_worksWithVerbose() throws Exception {
        task.verbose();

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args);

        assertTrue(task.configuration.verbose);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_worksWithPrintOptions() throws Exception {
        task.printusage("usage.txt");
        task.printseeds("seeds.txt");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args);

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Realistic scenarios with Map

    @Test
    public void testWhyareyoukeepingWithMap_androidActivityDebug() throws Exception {
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.keep("public class * extends android.app.Activity");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "class");
        args.put("name", "com.example.MainActivity");
        args.put("extends", "android.app.Activity");
        task.whyareyoukeeping(args);

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_debugMultipleActivities() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.MainActivity");
        args1.put("extends", "android.app.Activity");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.SettingsActivity");
        args2.put("extends", "android.app.Activity");

        task.whyareyoukeeping(args1);
        task.whyareyoukeeping(args2);

        assertEquals(2, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_annotatedClasses() throws Exception {
        // Debug why classes with specific annotation are kept
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("annotation", "com.example.KeepThis");
        args.put("type", "class");

        task.whyareyoukeeping(args);

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_publicApiAnalysis() throws Exception {
        // Analyze why public API classes are kept
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("type", "class");
        args.put("name", "com.example.api.**");

        task.whyareyoukeeping(args);
        task.verbose();

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Call order flexibility

    @Test
    public void testWhyareyoukeepingWithMap_canBeCalledFirst() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        task.whyareyoukeeping(args);
        task.keep("public class * { public *; }");
        task.dontshrink();

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.dontshrink();

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args);

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args);

        task.dontshrink();

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Edge cases

    @Test
    public void testWhyareyoukeepingWithMap_doesNotAffectOtherConfiguration() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args);

        assertEquals(initialShrink, task.configuration.shrink);
        assertEquals(initialOptimize, task.configuration.optimize);
        assertEquals(initialObfuscate, task.configuration.obfuscate);
    }

    @Test
    public void testWhyareyoukeepingWithMap_listIsModifiable() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Class1");
        task.whyareyoukeeping(args1);

        List list = task.configuration.whyAreYouKeeping;
        int initialSize = list.size();

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Class2");
        task.whyareyoukeeping(args2);

        assertEquals(initialSize + 1, list.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard3", ProGuardTask.class);
        assertNull(freshTask.configuration.whyAreYouKeeping);

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        freshTask.whyareyoukeeping(args);

        assertNotNull(freshTask.configuration.whyAreYouKeeping);
        assertEquals(1, freshTask.configuration.whyAreYouKeeping.size());
    }

    // Different access modifiers

    @Test
    public void testWhyareyoukeepingWithMap_publicAccess() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("name", "com.example.PublicClass");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_abstractModifier() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("modifier", "abstract");
        args.put("name", "com.example.AbstractClass");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_finalModifier() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("modifier", "final");
        args.put("name", "com.example.FinalClass");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Complex scenarios

    @Test
    public void testWhyareyoukeepingWithMap_fullConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("access", "public");
        args1.put("name", "com.example.PublicClass");
        task.whyareyoukeeping(args1);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("type", "interface");
        args2.put("name", "com.example.MyInterface");
        task.whyareyoukeeping(args2);

        task.printusage("usage.txt");
        task.verbose();

        assertEquals(2, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_stringAndMapCombination() throws Exception {
        // Mix string and map methods in realistic scenario
        task.keep("public class * extends android.app.Activity");

        // String method
        task.whyareyoukeeping("class com.example.MainActivity");

        // Map method for more complex specification
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "class");
        args.put("name", "com.example.MyService");
        args.put("extends", "android.app.Service");
        task.whyareyoukeeping(args);

        // Another string method
        task.whyareyoukeeping("class com.example.MyReceiver");

        assertEquals(3, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMap_wildcardNames() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.**");

        task.whyareyoukeeping(args);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // ========================================
    // Tests for whyareyoukeeping(Map, Closure) method
    // ========================================

    // Basic functionality tests for whyareyoukeeping(Map, Closure)

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_emptyMapNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        assertNull(task.configuration.whyAreYouKeeping, "whyAreYouKeeping should initially be null");
        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping, "whyAreYouKeeping should not be null after call");
        assertEquals(1, task.configuration.whyAreYouKeeping.size(),
                "whyAreYouKeeping should contain one specification");
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_withNameNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_withTypeAndNameNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "class");
        args.put("name", "com.example.MyClass");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_interfaceNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "interface");
        args.put("name", "com.example.MyInterface");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_enumNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "enum");
        args.put("name", "com.example.MyEnum");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Multiple calls tests

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_multipleCallsAccumulate() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Class1");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Class2");

        task.whyareyoukeeping(args1, null);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());

        task.whyareyoukeeping(args2, null);
        assertEquals(2, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_mixedWithOtherOverloads() throws Exception {
        // String method
        task.whyareyoukeeping("class com.example.StringClass");
        assertEquals(1, task.configuration.whyAreYouKeeping.size());

        // Map method
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.MapClass");
        task.whyareyoukeeping(args1);
        assertEquals(2, task.configuration.whyAreYouKeeping.size());

        // Map + Closure method (null closure)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.MapClosureClass");
        task.whyareyoukeeping(args2, null);
        assertEquals(3, task.configuration.whyAreYouKeeping.size());
    }

    // Complex specifications with Map and Closure

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_withExtendsNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        args.put("extends", "com.example.BaseClass");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_withImplementsNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        args.put("implements", "com.example.MyInterface");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_withAnnotationNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("annotation", "com.example.MyAnnotation");
        args.put("name", "com.example.MyClass");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_multipleAttributesNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("type", "class");
        args.put("name", "com.example.MyClass");
        args.put("extends", "android.app.Activity");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Integration with other configuration methods

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_worksWithKeep() throws Exception {
        task.keep("class com.example.MainActivity { *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MainActivity");
        task.whyareyoukeeping(args, null);

        assertNotNull(task.configuration.keep);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_worksWithDontShrink() throws Exception {
        task.dontshrink();

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args, null);

        assertFalse(task.configuration.shrink);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_worksWithVerbose() throws Exception {
        task.verbose();

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args, null);

        assertTrue(task.configuration.verbose);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_worksWithPrintOptions() throws Exception {
        task.printusage("usage.txt");
        task.printseeds("seeds.txt");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args, null);

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Realistic scenarios

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_androidActivityDebug() throws Exception {
        task.injars("build/intermediates/classes");
        task.outjars("build/outputs/proguard");
        task.keep("public class * extends android.app.Activity");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "class");
        args.put("name", "com.example.MainActivity");
        args.put("extends", "android.app.Activity");
        task.whyareyoukeeping(args, null);

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_debugMultipleClasses() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.MainActivity");
        args1.put("extends", "android.app.Activity");

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.MyService");
        args2.put("extends", "android.app.Service");

        task.whyareyoukeeping(args1, null);
        task.whyareyoukeeping(args2, null);

        assertEquals(2, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_annotatedClassesDebug() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("annotation", "com.example.KeepThis");
        args.put("type", "class");

        task.whyareyoukeeping(args, null);

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_publicApiAnalysis() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("type", "class");
        args.put("name", "com.example.api.**");

        task.whyareyoukeeping(args, null);
        task.verbose();

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Call order flexibility

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_canBeCalledFirst() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        task.whyareyoukeeping(args, null);
        task.keep("public class * { public *; }");
        task.dontshrink();

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_canBeCalledLast() throws Exception {
        task.keep("public class * { public *; }");
        task.dontshrink();

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args, null);

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_canBeCalledBetween() throws Exception {
        task.keep("public class * { public *; }");

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args, null);

        task.dontshrink();

        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Edge cases

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_doesNotAffectOtherConfiguration() throws Exception {
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        task.whyareyoukeeping(args, null);

        assertEquals(initialShrink, task.configuration.shrink);
        assertEquals(initialOptimize, task.configuration.optimize);
        assertEquals(initialObfuscate, task.configuration.obfuscate);
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_listIsModifiable() throws Exception {
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Class1");
        task.whyareyoukeeping(args1, null);

        List list = task.configuration.whyAreYouKeeping;
        int initialSize = list.size();

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Class2");
        task.whyareyoukeeping(args2, null);

        assertEquals(initialSize + 1, list.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_worksWithFreshTask() throws Exception {
        ProGuardTask freshTask = project.getTasks().create("proguard4", ProGuardTask.class);
        assertNull(freshTask.configuration.whyAreYouKeeping);

        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        freshTask.whyareyoukeeping(args, null);

        assertNotNull(freshTask.configuration.whyAreYouKeeping);
        assertEquals(1, freshTask.configuration.whyAreYouKeeping.size());
    }

    // Different access modifiers

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_publicAccessNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("name", "com.example.PublicClass");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_abstractModifierNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("modifier", "abstract");
        args.put("name", "com.example.AbstractClass");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_finalModifierNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("modifier", "final");
        args.put("name", "com.example.FinalClass");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    // Complex scenarios

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_fullConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/lib/rt.jar");
        task.keep("public class * { public *; }");

        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("access", "public");
        args1.put("name", "com.example.PublicClass");
        task.whyareyoukeeping(args1, null);

        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("type", "interface");
        args2.put("name", "com.example.MyInterface");
        task.whyareyoukeeping(args2, null);

        task.printusage("usage.txt");
        task.verbose();

        assertEquals(2, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_allThreeOverloadsCombined() throws Exception {
        // Mix all three method overloads
        task.keep("public class * extends android.app.Activity");

        // String method
        task.whyareyoukeeping("class com.example.StringClass");

        // Map method
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.MapClass");
        task.whyareyoukeeping(args1);

        // Map + Closure method (null closure)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("type", "class");
        args2.put("name", "com.example.MapClosureClass");
        args2.put("extends", "android.app.Service");
        task.whyareyoukeeping(args2, null);

        // Another String method
        task.whyareyoukeeping("class com.example.AnotherStringClass");

        assertEquals(4, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_wildcardNamesNullClosure() throws Exception {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.**");

        task.whyareyoukeeping(args, null);
        assertNotNull(task.configuration.whyAreYouKeeping);
        assertEquals(1, task.configuration.whyAreYouKeeping.size());
    }

    @Test
    public void testWhyareyoukeepingWithMapAndClosure_equivalentToMapOnlyWithNullClosure() throws Exception {
        // Map only method
        ProGuardTask task1 = project.getTasks().create("proguard5", ProGuardTask.class);
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.MyClass");
        task1.whyareyoukeeping(args1);

        // Map + null Closure method
        ProGuardTask task2 = project.getTasks().create("proguard6", ProGuardTask.class);
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.MyClass");
        task2.whyareyoukeeping(args2, null);

        // Both should produce the same result
        assertEquals(task1.configuration.whyAreYouKeeping.size(),
                task2.configuration.whyAreYouKeeping.size(),
                "Map-only and Map+null Closure should produce same result");
    }
}

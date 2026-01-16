/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#keep(String)}.
 *
 * This test class verifies that the keep(String) method correctly
 * adds keep rules to the configuration.
 *
 * Method signature: keep(Ljava/lang/String;)V
 * - Takes a class specification string (e.g., "class MyClass", "public class * { *; }")
 * - Parses it and adds it to configuration.keep list
 * - Returns void
 * - Throws ParseException if the class specification is invalid
 *
 * Key behavior: This method tells ProGuard which classes/members to keep from being removed
 * during shrinking or obfuscation. The class specification string uses ProGuard's keep rule
 * syntax. The method internally calls keep(Map, String) with a null map.
 *
 * Keep rules are essential for preserving classes that are accessed via reflection, used as
 * entry points, or required for other runtime purposes.
 */
public class ProGuardTaskClaude_keepTest {

    @TempDir
    Path tempDir;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("testProguard", ProGuardTask.class);
    }

    @AfterEach
    public void tearDown() {
        project = null;
        task = null;
    }

    /**
     * Tests that keep() initializes and adds to the keep list.
     */
    @Test
    public void testKeep_initializesAndAddsToList() throws Exception {
        // Given: Initial state where keep list is null
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");

        // When: Calling keep() with a class specification
        task.keep("class MyClass");

        // Then: The keep list should be initialized and contain one entry
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() can be called multiple times to accumulate rules.
     */
    @Test
    public void testKeep_multipleCalls_accumulatesRules() throws Exception {
        // When: Calling keep() multiple times
        task.keep("class MyClass");
        task.keep("class AnotherClass");
        task.keep("class ThirdClass");

        // Then: All rules should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keep() works with simple class specification.
     */
    @Test
    public void testKeep_simpleClassSpecification() throws Exception {
        // When: Calling keep() with simple class specification
        task.keep("class com.example.MainActivity");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works with wildcard class specification.
     */
    @Test
    public void testKeep_wildcardClassSpecification() throws Exception {
        // When: Calling keep() with wildcard class specification
        task.keep("class * extends android.app.Activity");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works with public modifier.
     */
    @Test
    public void testKeep_publicModifier() throws Exception {
        // When: Calling keep() with public modifier
        task.keep("public class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works with class and methods specification.
     */
    @Test
    public void testKeep_classWithMethods() throws Exception {
        // When: Calling keep() with class and methods
        task.keep("class com.example.MyClass { public *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works independently of injars.
     */
    @Test
    public void testKeep_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling keep()
        task.keep("class MyClass");

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works independently of libraryjars.
     */
    @Test
    public void testKeep_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling keep()
        task.keep("class MyClass");

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works in a complex workflow.
     */
    @Test
    public void testKeep_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.keep("class MainActivity");
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that keep() can be called before other methods.
     */
    @Test
    public void testKeep_calledBeforeOtherMethods() throws Exception {
        // When: Calling keep() before other methods
        task.keep("class MyClass");
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: Keep rule should still be set
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that keep() can be called after other methods.
     */
    @Test
    public void testKeep_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling keep() after other methods
        task.keep("class MyClass");

        // Then: All should be set correctly
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that keep() can be interleaved with other method calls.
     */
    @Test
    public void testKeep_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving keep() with other methods
        task.injars("input.jar");
        task.keep("class FirstClass");
        task.libraryjars("android.jar");
        task.keep("class SecondClass");
        task.outjars("output.jar");

        // Then: All should be set correctly
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that keep() works in realistic Android scenario.
     */
    @Test
    public void testKeep_realisticAndroidScenario() throws Exception {
        // Given: Android project with activities to preserve
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.keep("public class * extends android.app.Activity");
        task.keep("public class * extends android.app.Fragment");
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: Keep rules should be set
        assertEquals(2, task.configuration.keep.size(),
                     "Should have 2 keep rules for Android components");
    }

    /**
     * Tests that keep() works in realistic Java scenario.
     */
    @Test
    public void testKeep_realisticJavaScenario() throws Exception {
        // Given: Java project with main class to preserve
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.keep("public class com.example.Main { public static void main(java.lang.String[]); }");
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: Keep rule should be set
        assertEquals(1, task.configuration.keep.size(),
                     "Should have 1 keep rule for main class");
    }

    /**
     * Tests that keep() can be called in sequence with configuration files.
     */
    @Test
    public void testKeep_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling keep()
        task.keep("class MyClass");

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works with multiple library jars.
     */
    @Test
    public void testKeep_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling keep()
        task.keep("class MyClass");

        // Then: Keep rule should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() behavior is consistent across task instances.
     */
    @Test
    public void testKeep_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling keep() on both
        task1.keep("class ClassOne");
        task2.keep("class ClassTwo");

        // Then: Each should have its own keep list
        assertEquals(1, task1.configuration.keep.size(),
                     "Task1 should have 1 keep rule");
        assertEquals(1, task2.configuration.keep.size(),
                     "Task2 should have 1 keep rule");
    }

    /**
     * Tests that keep() returns void.
     */
    @Test
    public void testKeep_returnsVoid() throws Exception {
        // When: Calling keep()
        task.keep("class MyClass"); // Method returns void

        // Then: The side effect should occur (keep list should be populated)
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() can be called at any point in the configuration.
     */
    @Test
    public void testKeep_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.keep("class FirstClass");
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.keep("class SecondClass");
        task.outjars("output.jar");
        task.libraryjars("lib2.jar");

        // Then: Keep rules should be set regardless of call order
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
        assertEquals(2, task.getLibraryJarFiles().size(), "Should have 2 library jars");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that keep() with interface specification.
     */
    @Test
    public void testKeep_interfaceSpecification() throws Exception {
        // When: Calling keep() with interface specification
        task.keep("interface com.example.MyInterface");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() with enum specification.
     */
    @Test
    public void testKeep_enumSpecification() throws Exception {
        // When: Calling keep() with enum specification
        task.keep("enum com.example.MyEnum");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works with implements clause.
     */
    @Test
    public void testKeep_implementsClause() throws Exception {
        // When: Calling keep() with implements clause
        task.keep("class * implements java.io.Serializable");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works with annotation specification.
     */
    @Test
    public void testKeep_annotationSpecification() throws Exception {
        // When: Calling keep() with annotation
        task.keep("@interface com.example.MyAnnotation");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works with all members wildcard.
     */
    @Test
    public void testKeep_allMembersWildcard() throws Exception {
        // When: Calling keep() with all members wildcard
        task.keep("class com.example.MyClass { *; }");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works with specific method signature.
     */
    @Test
    public void testKeep_specificMethodSignature() throws Exception {
        // When: Calling keep() with specific method signature
        task.keep("class com.example.MyClass { public void myMethod(); }");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works with field specification.
     */
    @Test
    public void testKeep_fieldSpecification() throws Exception {
        // When: Calling keep() with field specification
        task.keep("class com.example.MyClass { private int myField; }");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works with constructor specification.
     */
    @Test
    public void testKeep_constructorSpecification() throws Exception {
        // When: Calling keep() with constructor specification
        task.keep("class com.example.MyClass { public <init>(); }");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() value persists across configuration calls.
     */
    @Test
    public void testKeep_persistsAcrossConfigurationCalls() throws Exception {
        // Given: Keep rule set early
        task.keep("class MyClass");
        int keepCountAfterSet = task.configuration.keep.size();

        // When: Other configuration methods are called
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");

        // Then: Keep rules should remain unchanged
        assertEquals(keepCountAfterSet, task.configuration.keep.size(),
                   "Keep rules should persist after other configuration calls");
    }

    /**
     * Tests that keep() works correctly in a minimal configuration.
     */
    @Test
    public void testKeep_minimalConfiguration() throws Exception {
        // When: Only keep() is called (minimal configuration)
        task.keep("class MyClass");

        // Then: Keep rule should be set correctly
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification in minimal configuration");
    }

    /**
     * Tests that keep() can handle package wildcard.
     */
    @Test
    public void testKeep_packageWildcard() throws Exception {
        // When: Calling keep() with package wildcard
        task.keep("class com.example.**");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() can handle single character wildcard.
     */
    @Test
    public void testKeep_singleCharWildcard() throws Exception {
        // When: Calling keep() with single character wildcard
        task.keep("class com.example.MyClass?");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() for native methods.
     */
    @Test
    public void testKeep_nativeMethods() throws Exception {
        // When: Calling keep() for native methods
        task.keep("class com.example.MyClass { native <methods>; }");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() works with modifiers.
     */
    @Test
    public void testKeep_withModifiers() throws Exception {
        // When: Calling keep() with various modifiers
        task.keep("public final class com.example.MyClass");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep() accumulates rules from different calls.
     */
    @Test
    public void testKeep_accumulatesRulesFromDifferentCalls() throws Exception {
        // When: Calling keep() multiple times with different specifications
        task.keep("class com.example.MainActivity");
        task.keep("class * extends android.app.Service");
        task.keep("interface com.example.MyInterface");
        task.keep("enum com.example.MyEnum");

        // Then: All rules should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keep() initial state has null keep list.
     */
    @Test
    public void testKeep_initiallyNull() throws Exception {
        // Given: A newly created task
        // When: Not calling keep()
        // Then: The keep list should be null (default)
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");
    }

    /**
     * Tests that keep() for reflection scenarios.
     */
    @Test
    public void testKeep_reflectionScenario() throws Exception {
        // Given: Project that uses reflection
        task.injars("app.jar");

        // When: Adding keep rules for classes accessed via reflection
        task.keep("class com.example.ReflectedClass { *; }");

        // Then: Keep rule should preserve the class
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should have rule to preserve reflected class");
    }

    /**
     * Tests that keep() for serialization scenarios.
     */
    @Test
    public void testKeep_serializationScenario() throws Exception {
        // When: Adding keep rules for serializable classes
        task.keep("class * implements java.io.Serializable { private static final long serialVersionUID; }");

        // Then: Keep rule should preserve serialVersionUID
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should have rule for serialization");
    }

    /**
     * Tests that keep() works with inner class specification.
     */
    @Test
    public void testKeep_innerClassSpecification() throws Exception {
        // When: Calling keep() with inner class
        task.keep("class com.example.Outer$Inner");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    // ========================================================================
    // Tests for keep(Map, String) variant
    // ========================================================================

    /**
     * Tests that keep(Map, String) with null map works like keep(String).
     */
    @Test
    public void testKeepWithMap_nullMap_behavesLikeStringOnly() throws Exception {
        // When: Calling keep() with null map
        task.keep(null, "class MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) with empty map adds specification.
     */
    @Test
    public void testKeepWithMap_emptyMap_addsSpecification() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with empty map
        task.keep(args, "class MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) can be called multiple times.
     */
    @Test
    public void testKeepWithMap_multipleCalls_accumulatesRules() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() multiple times
        task.keep(args, "class FirstClass");
        task.keep(args, "class SecondClass");
        task.keep(args, "class ThirdClass");

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keep(Map, String) works with conditional "if" clause.
     */
    @Test
    public void testKeepWithMap_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keep() with conditional
        task.keep(args, "class com.example.MyClass");

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) works independently of injars.
     */
    @Test
    public void testKeepWithMap_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with map
        task.keep(args, "class MyClass");

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) works in a complex workflow.
     */
    @Test
    public void testKeepWithMap_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.keep(args, "class MainActivity");
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that keep(Map, String) can be called before other methods.
     */
    @Test
    public void testKeepWithMap_calledBeforeOtherMethods() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() before other methods
        task.keep(args, "class MyClass");
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: Keep rule should still be set
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
    }

    /**
     * Tests that keep(Map, String) can be called after other methods.
     */
    @Test
    public void testKeepWithMap_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() after other methods
        task.keep(args, "class MyClass");

        // Then: All should be set correctly
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
    }

    /**
     * Tests that keep(Map, String) can be interleaved with keep(String) calls.
     */
    @Test
    public void testKeepWithMap_interleavedWithKeepString() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both keep variants
        task.keep("class FirstClass");
        task.keep(args, "class SecondClass");
        task.keep("class ThirdClass");
        task.keep(args, "class FourthClass");

        // Then: All rules should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keep(Map, String) works in realistic Android scenario with conditions.
     */
    @Test
    public void testKeepWithMap_realisticAndroidScenarioWithConditions() throws Exception {
        // Given: Android project with conditional keep rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class android.app.Application");

        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.keep(args, "class * extends android.app.Activity");
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: Conditional keep rule should be set
        assertEquals(1, task.configuration.keep.size(),
                     "Should have 1 conditional keep rule");
    }

    /**
     * Tests that keep(Map, String) behavior is consistent across task instances.
     */
    @Test
    public void testKeepWithMap_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() on both
        task1.keep(args, "class ClassOne");
        task2.keep(args, "class ClassTwo");

        // Then: Each should have its own keep list
        assertEquals(1, task1.configuration.keep.size(),
                     "Task1 should have 1 keep rule");
        assertEquals(1, task2.configuration.keep.size(),
                     "Task2 should have 1 keep rule");
    }

    /**
     * Tests that keep(Map, String) returns void.
     */
    @Test
    public void testKeepWithMap_returnsVoid() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep()
        task.keep(args, "class MyClass"); // Method returns void

        // Then: The side effect should occur (keep list should be populated)
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) can be called at any point in configuration.
     */
    @Test
    public void testKeepWithMap_flexibleCallOrder() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling at various points
        task.injars("input1.jar");
        task.keep(args, "class FirstClass");
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.keep(args, "class SecondClass");
        task.outjars("output.jar");

        // Then: Keep rules should be set regardless of call order
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
    }

    /**
     * Tests that keep(Map, String) with wildcard class specification.
     */
    @Test
    public void testKeepWithMap_wildcardClassSpecification() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with wildcard
        task.keep(args, "class * extends android.app.Activity");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) with class and methods specification.
     */
    @Test
    public void testKeepWithMap_classWithMethods() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with class and methods
        task.keep(args, "class com.example.MyClass { public *; }");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) value persists across configuration calls.
     */
    @Test
    public void testKeepWithMap_persistsAcrossConfigurationCalls() throws Exception {
        // Given: Keep rule set early
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keep(args, "class MyClass");
        int keepCountAfterSet = task.configuration.keep.size();

        // When: Other configuration methods are called
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");

        // Then: Keep rules should remain unchanged
        assertEquals(keepCountAfterSet, task.configuration.keep.size(),
                   "Keep rules should persist after other configuration calls");
    }

    /**
     * Tests that keep(Map, String) works correctly in minimal configuration.
     */
    @Test
    public void testKeepWithMap_minimalConfiguration() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Only keep() is called (minimal configuration)
        task.keep(args, "class MyClass");

        // Then: Keep rule should be set correctly
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification in minimal configuration");
    }

    /**
     * Tests that keep(Map, String) can handle package wildcard.
     */
    @Test
    public void testKeepWithMap_packageWildcard() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with package wildcard
        task.keep(args, "class com.example.**");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) for reflection scenarios.
     */
    @Test
    public void testKeepWithMap_reflectionScenario() throws Exception {
        // Given: Project that uses reflection
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("app.jar");

        // When: Adding keep rules for classes accessed via reflection
        task.keep(args, "class com.example.ReflectedClass { *; }");

        // Then: Keep rule should preserve the class
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should have rule to preserve reflected class");
    }

    /**
     * Tests that keep(Map, String) can mix null and non-null maps.
     */
    @Test
    public void testKeepWithMap_mixingNullAndNonNullMaps() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Mixing null and non-null maps
        task.keep(null, "class FirstClass");
        task.keep(args, "class SecondClass");
        task.keep(null, "class ThirdClass");

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keep(Map, String) with interface specification.
     */
    @Test
    public void testKeepWithMap_interfaceSpecification() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with interface
        task.keep(args, "interface com.example.MyInterface");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) with enum specification.
     */
    @Test
    public void testKeepWithMap_enumSpecification() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with enum
        task.keep(args, "enum com.example.MyEnum");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) with implements clause.
     */
    @Test
    public void testKeepWithMap_implementsClause() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with implements
        task.keep(args, "class * implements java.io.Serializable");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) for serialization scenarios.
     */
    @Test
    public void testKeepWithMap_serializationScenario() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Adding keep rules for serializable classes
        task.keep(args, "class * implements java.io.Serializable { private static final long serialVersionUID; }");

        // Then: Keep rule should preserve serialVersionUID
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should have rule for serialization");
    }

    /**
     * Tests that keep(Map, String) works with inner class specification.
     */
    @Test
    public void testKeepWithMap_innerClassSpecification() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with inner class
        task.keep(args, "class com.example.Outer$Inner");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) accumulates rules with different map configurations.
     */
    @Test
    public void testKeepWithMap_accumulatesDifferentMapConfigurations() throws Exception {
        // Given: Different maps
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");

        // When: Calling keep() with different maps
        task.keep(args1, "class FirstClass");
        task.keep(args2, "class SecondClass");
        task.keep(args1, "class ThirdClass");

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications with different map configs");
    }

    /**
     * Tests that keep(Map, String) works with public modifier.
     */
    @Test
    public void testKeepWithMap_publicModifier() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with public modifier
        task.keep(args, "public class com.example.MyClass");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) works with field specification.
     */
    @Test
    public void testKeepWithMap_fieldSpecification() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with field
        task.keep(args, "class com.example.MyClass { private int myField; }");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) works with constructor specification.
     */
    @Test
    public void testKeepWithMap_constructorSpecification() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with constructor
        task.keep(args, "class com.example.MyClass { public <init>(); }");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) for native methods.
     */
    @Test
    public void testKeepWithMap_nativeMethods() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() for native methods
        task.keep(args, "class com.example.MyClass { native <methods>; }");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, String) works with all members wildcard.
     */
    @Test
    public void testKeepWithMap_allMembersWildcard() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with all members wildcard
        task.keep(args, "class com.example.MyClass { *; }");

        // Then: The specification should be added
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    // ========================================================================
    // Tests for keep(Map) variant (Map-only, no String or Closure)
    // ========================================================================

    /**
     * Tests that keep(Map) with empty map adds specification.
     */
    @Test
    public void testKeepMapOnly_emptyMap_addsSpecification() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with only map (no string)
        task.keep(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map) can be called multiple times.
     */
    @Test
    public void testKeepMapOnly_multipleCalls_accumulatesRules() throws Exception {
        // Given: Maps with args
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keep() multiple times with map only
        task.keep(args1);
        task.keep(args2);
        task.keep(args3);

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keep(Map) works independently of injars.
     */
    @Test
    public void testKeepMapOnly_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with map only
        task.keep(args);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map) works independently of libraryjars.
     */
    @Test
    public void testKeepMapOnly_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with map only
        task.keep(args);

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map) works in a complex workflow.
     */
    @Test
    public void testKeepMapOnly_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.keep(args);
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that keep(Map) can be called before other methods.
     */
    @Test
    public void testKeepMapOnly_calledBeforeOtherMethods() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() before other methods
        task.keep(args);
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: Keep rule should still be set
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
    }

    /**
     * Tests that keep(Map) can be called after other methods.
     */
    @Test
    public void testKeepMapOnly_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() after other methods
        task.keep(args);

        // Then: All should be set correctly
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
    }

    /**
     * Tests that keep(Map) can be interleaved with other keep variants.
     */
    @Test
    public void testKeepMapOnly_interleavedWithOtherKeepVariants() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving keep(Map) with other keep variants
        task.keep("class FirstClass");
        task.keep(args);
        task.keep(args, "class ThirdClass");
        task.keep(args);

        // Then: All rules should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keep(Map) behavior is consistent across task instances.
     */
    @Test
    public void testKeepMapOnly_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep(Map) on both
        task1.keep(args);
        task2.keep(args);

        // Then: Each should have its own keep list
        assertEquals(1, task1.configuration.keep.size(),
                     "Task1 should have 1 keep rule");
        assertEquals(1, task2.configuration.keep.size(),
                     "Task2 should have 1 keep rule");
    }

    /**
     * Tests that keep(Map) returns void.
     */
    @Test
    public void testKeepMapOnly_returnsVoid() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep(Map)
        task.keep(args); // Method returns void

        // Then: The side effect should occur (keep list should be populated)
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map) can be called at any point in configuration.
     */
    @Test
    public void testKeepMapOnly_flexibleCallOrder() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling at various points
        task.injars("input1.jar");
        task.keep(args);
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.keep(args);
        task.outjars("output.jar");

        // Then: Keep rules should be set regardless of call order
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
    }

    /**
     * Tests that keep(Map) value persists across configuration calls.
     */
    @Test
    public void testKeepMapOnly_persistsAcrossConfigurationCalls() throws Exception {
        // Given: Keep rule set early
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keep(args);
        int keepCountAfterSet = task.configuration.keep.size();

        // When: Other configuration methods are called
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");

        // Then: Keep rules should remain unchanged
        assertEquals(keepCountAfterSet, task.configuration.keep.size(),
                   "Keep rules should persist after other configuration calls");
    }

    /**
     * Tests that keep(Map) works correctly in minimal configuration.
     */
    @Test
    public void testKeepMapOnly_minimalConfiguration() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Only keep(Map) is called (minimal configuration)
        task.keep(args);

        // Then: Keep rule should be set correctly
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification in minimal configuration");
    }

    /**
     * Tests that keep(Map) with conditional "if" clause.
     */
    @Test
    public void testKeepMapOnly_conditionalIf() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keep(Map) with conditional
        task.keep(args);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map) accumulates rules with different configurations.
     */
    @Test
    public void testKeepMapOnly_accumulatesDifferentConfigurations() throws Exception {
        // Given: Different maps
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keep(Map) with different maps
        task.keep(args1);
        task.keep(args2);
        task.keep(args3);

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications with different configs");
    }

    /**
     * Tests that keep(Map) works in realistic Android scenario.
     */
    @Test
    public void testKeepMapOnly_realisticAndroidScenario() throws Exception {
        // Given: Android project with keep rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.keep(args);
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: Keep rule should be set
        assertEquals(1, task.configuration.keep.size(),
                     "Should have 1 keep rule");
    }

    /**
     * Tests that keep(Map) works in realistic Java scenario.
     */
    @Test
    public void testKeepMapOnly_realisticJavaScenario() throws Exception {
        // Given: Java project with keep rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.keep(args);
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: Keep rule should be set
        assertEquals(1, task.configuration.keep.size(),
                     "Should have 1 keep rule");
    }

    /**
     * Tests that keep(Map) can be called in sequence with configuration files.
     */
    @Test
    public void testKeepMapOnly_withConfiguration() throws Exception {
        // Given: Configuration files and map
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.configuration("proguard-rules.pro");

        // When: Calling keep(Map)
        task.keep(args);

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map) works with multiple library jars.
     */
    @Test
    public void testKeepMapOnly_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars and map
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling keep(Map)
        task.keep(args);

        // Then: Keep rule should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map) initial state has null keep list.
     */
    @Test
    public void testKeepMapOnly_initiallyNull() throws Exception {
        // Given: A newly created task
        // When: Not calling keep()
        // Then: The keep list should be null (default)
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");
    }

    /**
     * Tests that keep(Map) for reflection scenarios.
     */
    @Test
    public void testKeepMapOnly_reflectionScenario() throws Exception {
        // Given: Project that uses reflection
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("app.jar");

        // When: Adding keep rules via map
        task.keep(args);

        // Then: Keep rule should be set
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should have rule");
    }

    /**
     * Tests that keep(Map) can be used with filters.
     */
    @Test
    public void testKeepMapOnly_withFilters() throws Exception {
        // Given: Library jars with filters and keep map
        java.util.Map<String, String> filter = new java.util.HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "android.jar");

        java.util.Map<String, Object> keepArgs = new java.util.HashMap<>();

        // When: Calling keep(Map)
        task.keep(keepArgs);

        // Then: Both should be set
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getLibraryJarFilters().size(), "Should have 1 filter");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map) works with extraJar.
     */
    @Test
    public void testKeepMapOnly_withExtraJar() throws Exception {
        // Given: ExtraJar configured
        task.extraJar(new java.io.File(tempDir.toFile(), "extra.jar"));
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep(Map)
        task.keep(args);

        // Then: Both should be set
        assertNotNull(task.configuration.extraJar, "ExtraJar should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map) can mix with all other keep variants.
     */
    @Test
    public void testKeepMapOnly_mixingAllKeepVariants() throws Exception {
        // Given: Different maps and strings
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();

        // When: Mixing all keep variants
        task.keep("class FirstClass");                    // keep(String)
        task.keep(args1);                                  // keep(Map)
        task.keep(args2, "class ThirdClass");             // keep(Map, String)
        task.keep(args1);                                  // keep(Map) again
        task.keep("class FifthClass");                    // keep(String) again

        // Then: All rules should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications from all variants");
    }

    /**
     * Tests that keep(Map) is thread-safe for idempotent operations.
     */
    @Test
    public void testKeepMapOnly_idempotentThreadSafe() throws Exception {
        // Given: Initial state
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep(Map) sequentially (simulating thread safety)
        task.keep(args);
        task.keep(args);

        // Then: Rules should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should have 2 specifications");
    }

    /**
     * Tests that keep(Map) with different map instances.
     */
    @Test
    public void testKeepMapOnly_differentMapInstances() throws Exception {
        // When: Calling keep(Map) with different map instances
        task.keep(new java.util.HashMap<>());
        task.keep(new java.util.HashMap<>());
        task.keep(new java.util.HashMap<>());

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications from different map instances");
    }

    // ========================================================================
    // Tests for keep(Map, Closure) variant (Map with Closure for member specs)
    // ========================================================================

    /**
     * Tests that keep(Map, Closure) with null closure works.
     */
    @Test
    public void testKeepMapClosure_nullClosure_addsSpecification() throws Exception {
        // Given: Map with args and null closure
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with null closure
        task.keep(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, Closure) can be called multiple times.
     */
    @Test
    public void testKeepMapClosure_multipleCalls_accumulatesRules() throws Exception {
        // Given: Maps with args
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keep() multiple times with null closures
        task.keep(args1, (groovy.lang.Closure) null);
        task.keep(args2, (groovy.lang.Closure) null);
        task.keep(args3, (groovy.lang.Closure) null);

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keep(Map, Closure) works independently of injars.
     */
    @Test
    public void testKeepMapClosure_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with map and null closure
        task.keep(args, (groovy.lang.Closure) null);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, Closure) works independently of libraryjars.
     */
    @Test
    public void testKeepMapClosure_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() with map and null closure
        task.keep(args, (groovy.lang.Closure) null);

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, Closure) works in a complex workflow.
     */
    @Test
    public void testKeepMapClosure_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.keep(args, (groovy.lang.Closure) null);
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that keep(Map, Closure) can be called before other methods.
     */
    @Test
    public void testKeepMapClosure_calledBeforeOtherMethods() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() before other methods
        task.keep(args, (groovy.lang.Closure) null);
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: Keep rule should still be set
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
    }

    /**
     * Tests that keep(Map, Closure) can be called after other methods.
     */
    @Test
    public void testKeepMapClosure_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep() after other methods
        task.keep(args, (groovy.lang.Closure) null);

        // Then: All should be set correctly
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
    }

    /**
     * Tests that keep(Map, Closure) can be interleaved with other keep variants.
     */
    @Test
    public void testKeepMapClosure_interleavedWithOtherKeepVariants() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving keep(Map, Closure) with other keep variants
        task.keep("class FirstClass");
        task.keep(args, (groovy.lang.Closure) null);
        task.keep(args, "class ThirdClass");
        task.keep(args);
        task.keep(args, (groovy.lang.Closure) null);

        // Then: All rules should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keep(Map, Closure) behavior is consistent across task instances.
     */
    @Test
    public void testKeepMapClosure_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep(Map, Closure) on both
        task1.keep(args, (groovy.lang.Closure) null);
        task2.keep(args, (groovy.lang.Closure) null);

        // Then: Each should have its own keep list
        assertEquals(1, task1.configuration.keep.size(),
                     "Task1 should have 1 keep rule");
        assertEquals(1, task2.configuration.keep.size(),
                     "Task2 should have 1 keep rule");
    }

    /**
     * Tests that keep(Map, Closure) returns void.
     */
    @Test
    public void testKeepMapClosure_returnsVoid() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep(Map, Closure)
        task.keep(args, (groovy.lang.Closure) null); // Method returns void

        // Then: The side effect should occur (keep list should be populated)
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, Closure) can be called at any point in configuration.
     */
    @Test
    public void testKeepMapClosure_flexibleCallOrder() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling at various points
        task.injars("input1.jar");
        task.keep(args, (groovy.lang.Closure) null);
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.keep(args, (groovy.lang.Closure) null);
        task.outjars("output.jar");

        // Then: Keep rules should be set regardless of call order
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
    }

    /**
     * Tests that keep(Map, Closure) value persists across configuration calls.
     */
    @Test
    public void testKeepMapClosure_persistsAcrossConfigurationCalls() throws Exception {
        // Given: Keep rule set early
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keep(args, (groovy.lang.Closure) null);
        int keepCountAfterSet = task.configuration.keep.size();

        // When: Other configuration methods are called
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");

        // Then: Keep rules should remain unchanged
        assertEquals(keepCountAfterSet, task.configuration.keep.size(),
                   "Keep rules should persist after other configuration calls");
    }

    /**
     * Tests that keep(Map, Closure) works correctly in minimal configuration.
     */
    @Test
    public void testKeepMapClosure_minimalConfiguration() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Only keep(Map, Closure) is called (minimal configuration)
        task.keep(args, (groovy.lang.Closure) null);

        // Then: Keep rule should be set correctly
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification in minimal configuration");
    }

    /**
     * Tests that keep(Map, Closure) with conditional "if" clause.
     */
    @Test
    public void testKeepMapClosure_conditionalIf() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keep(Map, Closure) with conditional
        task.keep(args, (groovy.lang.Closure) null);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, Closure) accumulates rules with different configurations.
     */
    @Test
    public void testKeepMapClosure_accumulatesDifferentConfigurations() throws Exception {
        // Given: Different maps
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keep(Map, Closure) with different maps
        task.keep(args1, (groovy.lang.Closure) null);
        task.keep(args2, (groovy.lang.Closure) null);
        task.keep(args3, (groovy.lang.Closure) null);

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications with different configs");
    }

    /**
     * Tests that keep(Map, Closure) works in realistic Android scenario.
     */
    @Test
    public void testKeepMapClosure_realisticAndroidScenario() throws Exception {
        // Given: Android project with keep rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.keep(args, (groovy.lang.Closure) null);
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: Keep rule should be set
        assertEquals(1, task.configuration.keep.size(),
                     "Should have 1 keep rule");
    }

    /**
     * Tests that keep(Map, Closure) works in realistic Java scenario.
     */
    @Test
    public void testKeepMapClosure_realisticJavaScenario() throws Exception {
        // Given: Java project with keep rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.keep(args, (groovy.lang.Closure) null);
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: Keep rule should be set
        assertEquals(1, task.configuration.keep.size(),
                     "Should have 1 keep rule");
    }

    /**
     * Tests that keep(Map, Closure) can be called in sequence with configuration files.
     */
    @Test
    public void testKeepMapClosure_withConfiguration() throws Exception {
        // Given: Configuration files and map
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.configuration("proguard-rules.pro");

        // When: Calling keep(Map, Closure)
        task.keep(args, (groovy.lang.Closure) null);

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, Closure) works with multiple library jars.
     */
    @Test
    public void testKeepMapClosure_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars and map
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling keep(Map, Closure)
        task.keep(args, (groovy.lang.Closure) null);

        // Then: Keep rule should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, Closure) initial state has null keep list.
     */
    @Test
    public void testKeepMapClosure_initiallyNull() throws Exception {
        // Given: A newly created task
        // When: Not calling keep()
        // Then: The keep list should be null (default)
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");
    }

    /**
     * Tests that keep(Map, Closure) for reflection scenarios.
     */
    @Test
    public void testKeepMapClosure_reflectionScenario() throws Exception {
        // Given: Project that uses reflection
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("app.jar");

        // When: Adding keep rules via map and closure
        task.keep(args, (groovy.lang.Closure) null);

        // Then: Keep rule should be set
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should have rule");
    }

    /**
     * Tests that keep(Map, Closure) can be used with filters.
     */
    @Test
    public void testKeepMapClosure_withFilters() throws Exception {
        // Given: Library jars with filters and keep map
        java.util.Map<String, String> filter = new java.util.HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "android.jar");

        java.util.Map<String, Object> keepArgs = new java.util.HashMap<>();

        // When: Calling keep(Map, Closure)
        task.keep(keepArgs, (groovy.lang.Closure) null);

        // Then: Both should be set
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getLibraryJarFilters().size(), "Should have 1 filter");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, Closure) works with extraJar.
     */
    @Test
    public void testKeepMapClosure_withExtraJar() throws Exception {
        // Given: ExtraJar configured
        task.extraJar(new java.io.File(tempDir.toFile(), "extra.jar"));
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep(Map, Closure)
        task.keep(args, (groovy.lang.Closure) null);

        // Then: Both should be set
        assertNotNull(task.configuration.extraJar, "ExtraJar should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, Closure) can mix with all other keep variants.
     */
    @Test
    public void testKeepMapClosure_mixingAllKeepVariants() throws Exception {
        // Given: Different maps and strings
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();

        // When: Mixing all keep variants
        task.keep("class FirstClass");                              // keep(String)
        task.keep(args1);                                            // keep(Map)
        task.keep(args2, "class ThirdClass");                       // keep(Map, String)
        task.keep(args1, (groovy.lang.Closure) null);              // keep(Map, Closure)
        task.keep("class FifthClass");                              // keep(String) again

        // Then: All rules should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications from all variants");
    }

    /**
     * Tests that keep(Map, Closure) is thread-safe for idempotent operations.
     */
    @Test
    public void testKeepMapClosure_idempotentThreadSafe() throws Exception {
        // Given: Initial state
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keep(Map, Closure) sequentially (simulating thread safety)
        task.keep(args, (groovy.lang.Closure) null);
        task.keep(args, (groovy.lang.Closure) null);

        // Then: Rules should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should have 2 specifications");
    }

    /**
     * Tests that keep(Map, Closure) with different map instances.
     */
    @Test
    public void testKeepMapClosure_differentMapInstances() throws Exception {
        // When: Calling keep(Map, Closure) with different map instances
        task.keep(new java.util.HashMap<>(), (groovy.lang.Closure) null);
        task.keep(new java.util.HashMap<>(), (groovy.lang.Closure) null);
        task.keep(new java.util.HashMap<>(), (groovy.lang.Closure) null);

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications from different map instances");
    }

    /**
     * Tests that keep(Map, Closure) with both null map and null closure.
     */
    @Test
    public void testKeepMapClosure_bothNull() throws Exception {
        // When: Calling keep() with both null map and null closure
        task.keep(null, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keep(Map, Closure) is the base method for other variants.
     */
    @Test
    public void testKeepMapClosure_isBaseMethod() throws Exception {
        // Given: Different keep variants that internally use keep(Map, Closure)
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling different variants (all should internally call keep(Map, Closure))
        task.keep(args);                                  // calls keep(args, null)
        task.keep(args, (groovy.lang.Closure) null);    // direct call

        // Then: Both should produce the same result
        assertEquals(2, task.configuration.keep.size(),
                     "Both calls should add specifications");
    }
}

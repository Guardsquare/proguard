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
 * Tests for {@link ProGuardTask#keepclasseswithmembers(String)}.
 *
 * This test class verifies that the keepclasseswithmembers(String) method correctly
 * adds keepclasseswithmembers rules to the configuration.
 *
 * Method signature: keepclasseswithmembers(Ljava/lang/String;)V
 * - Takes a class specification string that specifies which classes with members to keep
 * - Parses it and adds it to configuration.keep list
 * - Returns void
 * - Throws ParseException if the class specification is invalid
 *
 * Key behavior: This method tells ProGuard to keep classes and their members only if
 * ALL specified members are present. This is different from:
 * - keep(): which keeps classes and members unconditionally
 * - keepclassmembers(): which keeps only members, allowing classes to be removed if unused
 *
 * The keepclasseswithmembers directive is useful for keeping entry points like main()
 * methods and their containing classes, but only if the methods actually exist.
 *
 * Internally calls keepclasseswithmembers(null, String) with a null map.
 */
public class ProGuardTaskClaude_keepclasseswithmembersTest {

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
     * Tests that keepclasseswithmembers() initializes and adds to the keep list.
     */
    @Test
    public void testKeepClassesWithMembers_initializesAndAddsToList() throws Exception {
        // Given: Initial state where keep list is null
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");

        // When: Calling keepclasseswithmembers() with a class specification
        task.keepclasseswithmembers("class MyClass { public static void main(java.lang.String[]); }");

        // Then: The keep list should be initialized and contain one entry
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() can be called multiple times to accumulate rules.
     */
    @Test
    public void testKeepClassesWithMembers_multipleCalls_accumulatesRules() throws Exception {
        // When: Calling keepclasseswithmembers() multiple times
        task.keepclasseswithmembers("class FirstClass { public static void main(java.lang.String[]); }");
        task.keepclasseswithmembers("class SecondClass { public <init>(); }");
        task.keepclasseswithmembers("class ThirdClass { public void run(); }");

        // Then: All rules should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembers() works with main method specification.
     */
    @Test
    public void testKeepClassesWithMembers_mainMethod() throws Exception {
        // Given: A main method specification

        // When: Calling keepclasseswithmembers() with main method
        task.keepclasseswithmembers("class com.example.Main { public static void main(java.lang.String[]); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with constructor specification.
     */
    @Test
    public void testKeepClassesWithMembers_constructor() throws Exception {
        // Given: A constructor specification

        // When: Calling keepclasseswithmembers() with constructor
        task.keepclasseswithmembers("class com.example.MyClass { public <init>(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with wildcard class names.
     */
    @Test
    public void testKeepClassesWithMembers_wildcardClassName() throws Exception {
        // Given: A wildcard class specification

        // When: Calling keepclasseswithmembers() with wildcard
        task.keepclasseswithmembers("class * { public static void main(java.lang.String[]); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with multiple members.
     */
    @Test
    public void testKeepClassesWithMembers_multipleMembers() throws Exception {
        // Given: Multiple members in one specification

        // When: Calling keepclasseswithmembers() with multiple members
        task.keepclasseswithmembers("class com.example.MyClass { public <init>(); public void run(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with public modifier.
     */
    @Test
    public void testKeepClassesWithMembers_publicModifier() throws Exception {
        // Given: A specification with public modifier

        // When: Calling keepclasseswithmembers() with public modifier
        task.keepclasseswithmembers("public class com.example.MyClass { public void myMethod(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with abstract class.
     */
    @Test
    public void testKeepClassesWithMembers_abstractClass() throws Exception {
        // Given: An abstract class specification

        // When: Calling keepclasseswithmembers() with abstract class
        task.keepclasseswithmembers("abstract class com.example.MyClass { abstract void process(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with interface.
     */
    @Test
    public void testKeepClassesWithMembers_interface() throws Exception {
        // Given: An interface specification

        // When: Calling keepclasseswithmembers() with interface
        task.keepclasseswithmembers("interface com.example.MyInterface { void callback(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with enum.
     */
    @Test
    public void testKeepClassesWithMembers_enum() throws Exception {
        // Given: An enum specification

        // When: Calling keepclasseswithmembers() with enum
        task.keepclasseswithmembers("enum com.example.MyEnum { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with static methods.
     */
    @Test
    public void testKeepClassesWithMembers_staticMethod() throws Exception {
        // Given: A static method specification

        // When: Calling keepclasseswithmembers() with static method
        task.keepclasseswithmembers("class com.example.Util { public static void helper(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with native methods.
     */
    @Test
    public void testKeepClassesWithMembers_nativeMethod() throws Exception {
        // Given: A native method specification

        // When: Calling keepclasseswithmembers() with native method
        task.keepclasseswithmembers("class com.example.NativeLib { native void processData(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with field specification.
     */
    @Test
    public void testKeepClassesWithMembers_fieldSpec() throws Exception {
        // Given: A field specification

        // When: Calling keepclasseswithmembers() with field spec
        task.keepclasseswithmembers("class com.example.Data { public String name; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with extends clause.
     */
    @Test
    public void testKeepClassesWithMembers_extendsClause() throws Exception {
        // Given: A specification with extends clause

        // When: Calling keepclasseswithmembers() with extends
        task.keepclasseswithmembers("class * extends android.app.Activity { void onCreate(android.os.Bundle); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with implements clause.
     */
    @Test
    public void testKeepClassesWithMembers_implementsClause() throws Exception {
        // Given: A specification with implements clause

        // When: Calling keepclasseswithmembers() with implements
        task.keepclasseswithmembers("class * implements java.lang.Runnable { public void run(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with annotation specification.
     */
    @Test
    public void testKeepClassesWithMembers_annotation() throws Exception {
        // Given: An annotation specification

        // When: Calling keepclasseswithmembers() with annotation
        task.keepclasseswithmembers("@interface com.example.MyAnnotation { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with class annotation.
     */
    @Test
    public void testKeepClassesWithMembers_classWithAnnotation() throws Exception {
        // Given: A class with annotation specification

        // When: Calling keepclasseswithmembers() with class annotation
        task.keepclasseswithmembers("@com.example.KeepClass class * { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with member annotation.
     */
    @Test
    public void testKeepClassesWithMembers_memberWithAnnotation() throws Exception {
        // Given: A member with annotation specification

        // When: Calling keepclasseswithmembers() with member annotation
        task.keepclasseswithmembers("class * { @com.example.KeepMethod <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with varargs.
     */
    @Test
    public void testKeepClassesWithMembers_varargs() throws Exception {
        // Given: A varargs specification

        // When: Calling keepclasseswithmembers() with varargs
        task.keepclasseswithmembers("class com.example.MyClass { void print(java.lang.String...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with array types.
     */
    @Test
    public void testKeepClassesWithMembers_arrayTypes() throws Exception {
        // Given: An array type specification

        // When: Calling keepclasseswithmembers() with array types
        task.keepclasseswithmembers("class com.example.MyClass { java.lang.String[] getItems(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with parameterized constructor.
     */
    @Test
    public void testKeepClassesWithMembers_parameterizedConstructor() throws Exception {
        // Given: A parameterized constructor specification

        // When: Calling keepclasseswithmembers() with parameterized constructor
        task.keepclasseswithmembers("class com.example.MyClass { <init>(java.lang.String, int); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with nested classes.
     */
    @Test
    public void testKeepClassesWithMembers_nestedClass() throws Exception {
        // Given: A nested class specification

        // When: Calling keepclasseswithmembers() with nested class
        task.keepclasseswithmembers("class com.example.Outer$Inner { void method(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with primitive types.
     */
    @Test
    public void testKeepClassesWithMembers_primitiveTypes() throws Exception {
        // Given: Primitive type specifications

        // When: Calling keepclasseswithmembers() with primitive types
        task.keepclasseswithmembers("class com.example.MyClass { int getValue(); void setValue(int); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with generic types.
     */
    @Test
    public void testKeepClassesWithMembers_genericTypes() throws Exception {
        // Given: A generic type specification

        // When: Calling keepclasseswithmembers() with generic types
        task.keepclasseswithmembers("class com.example.MyClass { java.util.List get(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with Android Activity entry point.
     */
    @Test
    public void testKeepClassesWithMembers_androidActivity() throws Exception {
        // Given: An Android Activity specification

        // When: Calling keepclasseswithmembers() with Activity
        task.keepclasseswithmembers("class * extends android.app.Activity { void onCreate(android.os.Bundle); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with Android View constructors.
     */
    @Test
    public void testKeepClassesWithMembers_androidViewConstructors() throws Exception {
        // Given: An Android View constructor specification

        // When: Calling keepclasseswithmembers() with View constructors
        task.keepclasseswithmembers("class * extends android.view.View { public <init>(android.content.Context, android.util.AttributeSet); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with JUnit test methods.
     */
    @Test
    public void testKeepClassesWithMembers_junitTestMethods() throws Exception {
        // Given: A JUnit test method specification

        // When: Calling keepclasseswithmembers() with test methods
        task.keepclasseswithmembers("class * { @org.junit.Test <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with R class fields.
     */
    @Test
    public void testKeepClassesWithMembers_androidRClass() throws Exception {
        // Given: An Android R class specification

        // When: Calling keepclasseswithmembers() with R class
        task.keepclasseswithmembers("class **.R$* { public static int *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with method wildcards.
     */
    @Test
    public void testKeepClassesWithMembers_methodWildcards() throws Exception {
        // Given: A method wildcard specification

        // When: Calling keepclasseswithmembers() with method wildcards
        task.keepclasseswithmembers("class com.example.MyClass { <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with field wildcards.
     */
    @Test
    public void testKeepClassesWithMembers_fieldWildcards() throws Exception {
        // Given: A field wildcard specification

        // When: Calling keepclasseswithmembers() with field wildcards
        task.keepclasseswithmembers("class com.example.MyClass { <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with synchronized methods.
     */
    @Test
    public void testKeepClassesWithMembers_synchronizedMethods() throws Exception {
        // Given: A synchronized method specification

        // When: Calling keepclasseswithmembers() with synchronized method
        task.keepclasseswithmembers("class com.example.ThreadSafe { synchronized void update(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with volatile fields.
     */
    @Test
    public void testKeepClassesWithMembers_volatileFields() throws Exception {
        // Given: A volatile field specification

        // When: Calling keepclasseswithmembers() with volatile field
        task.keepclasseswithmembers("class com.example.ThreadSafe { volatile boolean flag; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() can be mixed with other configuration methods.
     */
    @Test
    public void testKeepClassesWithMembers_mixedWithOtherConfiguration() throws Exception {
        // Given: Other configuration is present
        task.dontobfuscate();

        // When: Calling keepclasseswithmembers()
        task.keepclasseswithmembers("class com.example.MyClass { public static void main(java.lang.String[]); }");

        // Then: Both configurations should be present
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() accumulates across multiple calls.
     */
    @Test
    public void testKeepClassesWithMembers_accumulation() throws Exception {
        // When: Calling keepclasseswithmembers() multiple times with different specs
        task.keepclasseswithmembers("class com.example.ClassA { void methodA(); }");
        task.keepclasseswithmembers("class com.example.ClassB { void methodB(); }");
        task.keepclasseswithmembers("class com.example.ClassC { void methodC(); }");
        task.keepclasseswithmembers("class com.example.ClassD { void methodD(); }");
        task.keepclasseswithmembers("class com.example.ClassE { void methodE(); }");

        // Then: All specifications should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepclasseswithmembers() works with transient fields.
     */
    @Test
    public void testKeepClassesWithMembers_transientFields() throws Exception {
        // Given: A transient field specification

        // When: Calling keepclasseswithmembers() with transient field
        task.keepclasseswithmembers("class com.example.Serializable { transient int cache; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with final modifier.
     */
    @Test
    public void testKeepClassesWithMembers_finalModifier() throws Exception {
        // Given: A specification with final modifier

        // When: Calling keepclasseswithmembers() with final modifier
        task.keepclasseswithmembers("class com.example.MyClass { public final String CONSTANT; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepClassesWithMembers_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Both tasks call keepclasseswithmembers() with the same spec
        task.keepclasseswithmembers("class com.example.MyClass { public static void main(java.lang.String[]); }");
        task2.keepclasseswithmembers("class com.example.MyClass { public static void main(java.lang.String[]); }");

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers() works with wildcard package names.
     */
    @Test
    public void testKeepClassesWithMembers_wildcardPackages() throws Exception {
        // Given: A wildcard package specification

        // When: Calling keepclasseswithmembers() with wildcard package
        task.keepclasseswithmembers("class com.example.** { public static void main(java.lang.String[]); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    // ========================================================================
    // Tests for keepclasseswithmembers(Map, String) variant
    // ========================================================================

    /**
     * Tests that keepclasseswithmembers(Map, String) with null map works like keepclasseswithmembers(String).
     */
    @Test
    public void testKeepClassesWithMembersWithMap_nullMap_behavesLikeStringOnly() throws Exception {
        // When: Calling keepclasseswithmembers() with null map
        task.keepclasseswithmembers(null, "class MyClass { public static void main(java.lang.String[]); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) with empty map adds specification.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_emptyMap_addsSpecification() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with empty map
        task.keepclasseswithmembers(args, "class MyClass { void myMethod(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) can be called multiple times.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_multipleCalls_accumulatesRules() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() multiple times
        task.keepclasseswithmembers(args, "class FirstClass { void method1(); }");
        task.keepclasseswithmembers(args, "class SecondClass { int field; }");
        task.keepclasseswithmembers(args, "class ThirdClass { <init>(); }");

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with conditional "if" clause.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepclasseswithmembers() with conditional
        task.keepclasseswithmembers(args, "class com.example.MyClass { void callback(); }");

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works independently of injars.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args, "class MyClass { void myMethod(); }");

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works in a complex workflow.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args, "class com.example.Main { public static void main(java.lang.String[]); }");

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) with different maps accumulates.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_differentMaps_accumulates() throws Exception {
        // Given: Different maps for different rules
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");

        // When: Calling keepclasseswithmembers() with different maps
        task.keepclasseswithmembers(args1, "class FirstClass { void method1(); }");
        task.keepclasseswithmembers(args2, "class SecondClass { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with main method entry point.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_mainMethodEntryPoint() throws Exception {
        // Given: Map for main method rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for main method
        task.keepclasseswithmembers(args, "class * { public static void main(java.lang.String[]); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with Android Activity lifecycle.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_androidLifecycle() throws Exception {
        // Given: Map for Android lifecycle
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for Android lifecycle
        task.keepclasseswithmembers(args, "class * extends android.app.Activity { void onCreate(android.os.Bundle); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with native methods.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_nativeMethods() throws Exception {
        // Given: Map for native methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for native methods
        task.keepclasseswithmembers(args, "class com.example.NativeLib { native <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with annotated methods.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_annotatedMethods() throws Exception {
        // Given: Map for annotated methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for annotated methods
        task.keepclasseswithmembers(args, "class * { @org.junit.Test <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with View constructors.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_viewConstructors() throws Exception {
        // Given: Map for View constructors
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for view constructors
        task.keepclasseswithmembers(args, "class * extends android.view.View { public <init>(android.content.Context, android.util.AttributeSet); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with enum members.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_enumMembers() throws Exception {
        // Given: Map for enum members
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for enum
        task.keepclasseswithmembers(args, "enum * { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with Runnable implementations.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_runnableImplementations() throws Exception {
        // Given: Map for Runnable implementations
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for Runnable
        task.keepclasseswithmembers(args, "class * implements java.lang.Runnable { public void run(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) can interleave with keepclasseswithmembers(String).
     */
    @Test
    public void testKeepClassesWithMembersWithMap_interleaveWithStringVariant() throws Exception {
        // Given: Using both variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepclasseswithmembers("class First { void method1(); }");
        task.keepclasseswithmembers(args, "class Second { void method2(); }");
        task.keepclasseswithmembers("class Third { void method3(); }");
        task.keepclasseswithmembers(args, "class Fourth { void method4(); }");

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with wildcard return types.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_wildcardReturnTypes() throws Exception {
        // Given: Map for wildcard return types
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with wildcard return type
        task.keepclasseswithmembers(args, "class com.example.MyClass { *** get*(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with setter methods.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_setterMethods() throws Exception {
        // Given: Map for setter methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for setters
        task.keepclasseswithmembers(args, "class com.example.MyClass { void set*(...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with getter methods.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_getterMethods() throws Exception {
        // Given: Map for getter methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for getters
        task.keepclasseswithmembers(args, "class com.example.MyClass { *** get*(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with callback interfaces.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_callbackInterfaces() throws Exception {
        // Given: Map for callback interfaces
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for callbacks
        task.keepclasseswithmembers(args, "interface * { void on*(...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with R class resource fields.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_rClassFields() throws Exception {
        // Given: Map for R class fields
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for R class
        task.keepclasseswithmembers(args, "class **.R$* { public static int *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with conditional compilation.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_conditionalCompilation() throws Exception {
        // Given: Map with conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.FeatureEnabled");

        // When: Calling keepclasseswithmembers() with conditional
        task.keepclasseswithmembers(args, "class com.example.FeatureClass { void featureMethod(); }");

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) accumulates multiple conditionals.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_multipleConditionals_accumulates() throws Exception {
        // Given: Multiple maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");

        // When: Calling keepclasseswithmembers() with different conditionals
        task.keepclasseswithmembers(args1, "class First { void method1(); }");
        task.keepclasseswithmembers(args2, "class Second { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with synchronized members.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_synchronizedMembers() throws Exception {
        // Given: Map for synchronized members
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for synchronized members
        task.keepclasseswithmembers(args, "class com.example.ThreadSafe { synchronized <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with volatile fields.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_volatileFields() throws Exception {
        // Given: Map for volatile fields
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for volatile fields
        task.keepclasseswithmembers(args, "class com.example.ThreadSafe { volatile <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) integrates with other configuration.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args, "class com.example.MyClass { void myMethod(); }");

        // Then: All configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) handles large accumulation.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_largeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() many times
        for (int i = 0; i < 10; i++) {
            task.keepclasseswithmembers(args, "class Class" + i + " { void method" + i + "(); }");
        }

        // Then: All should be accumulated
        assertEquals(10, task.configuration.keep.size(),
                     "Keep list should contain 10 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with public API pattern.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_publicApiPattern() throws Exception {
        // Given: Map for public API
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for public API
        task.keepclasseswithmembers(args, "class com.example.api.** { public <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with Parcelable creator.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_parcelableCreator() throws Exception {
        // Given: Map for Parcelable CREATOR
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for Parcelable
        task.keepclasseswithmembers(args, "class * implements android.os.Parcelable { public static final android.os.Parcelable$Creator CREATOR; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) works with serialVersionUID.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_serialVersionUID() throws Exception {
        // Given: Map for serialVersionUID
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() for Serializable
        task.keepclasseswithmembers(args, "class * implements java.io.Serializable { private static final long serialVersionUID; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, String) preserves consistent behavior.
     */
    @Test
    public void testKeepClassesWithMembersWithMap_consistentBehavior() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard3", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepclasseswithmembers() with map
        task.keepclasseswithmembers(args, "class MyClass { void myMethod(); }");
        task2.keepclasseswithmembers(args, "class MyClass { void myMethod(); }");

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    // ========================================================================
    // Tests for keepclasseswithmembers(Map) variant (Map-only, no String or Closure)
    // ========================================================================

    /**
     * Tests that keepclasseswithmembers(Map) with empty map adds specification.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_emptyMap_addsSpecification() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map only
        task.keepclasseswithmembers(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) can be called multiple times.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_multipleCalls_accumulatesRules() throws Exception {
        // Given: Maps for different rules
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() multiple times
        task.keepclasseswithmembers(args1);
        task.keepclasseswithmembers(args2);
        task.keepclasseswithmembers(args3);

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) internally calls keepclasseswithmembers(Map, Closure) with null.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_callsMapClosureVariant() throws Exception {
        // Given: Map for the rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers(Map)
        task.keepclasseswithmembers(args);

        // Then: Should behave like calling keepclasseswithmembers(Map, null Closure)
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) works with conditional "if" clause.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepclasseswithmembers() with conditional
        task.keepclasseswithmembers(args);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) can interleave with other variants.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_interleaveWithOtherVariants() throws Exception {
        // Given: Using different variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving all variants
        task.keepclasseswithmembers("class First { void method1(); }");
        task.keepclasseswithmembers(args);
        task.keepclasseswithmembers(args, "class Third { void method3(); }");

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) works independently of injars.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) works in a complex workflow.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");
        task.dontobfuscate();

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) with different conditionals accumulates.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_differentConditionals_accumulates() throws Exception {
        // Given: Different maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("if", "class com.example.Condition3");

        // When: Calling keepclasseswithmembers() with different conditionals
        task.keepclasseswithmembers(args1);
        task.keepclasseswithmembers(args2);
        task.keepclasseswithmembers(args3);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) works with null map.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_nullMap_addsSpecification() throws Exception {
        // When: Calling keepclasseswithmembers() with null map
        task.keepclasseswithmembers((java.util.Map<String, Object>) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) integrates with other configuration methods.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        task.dontoptimize();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args);

        // Then: All configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertTrue(task.configuration.optimize == false,
                   "Optimization should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) handles large accumulation.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_largeAccumulation() throws Exception {
        // When: Calling keepclasseswithmembers() many times
        for (int i = 0; i < 15; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            task.keepclasseswithmembers(args);
        }

        // Then: All should be accumulated
        assertEquals(15, task.configuration.keep.size(),
                     "Keep list should contain 15 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) works with mixed empty and conditional maps.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_mixedEmptyAndConditional() throws Exception {
        // Given: Mix of empty and conditional maps
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with mixed maps
        task.keepclasseswithmembers(args1);
        task.keepclasseswithmembers(args2);
        task.keepclasseswithmembers(args3);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard4", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepclasseswithmembers() with map
        task.keepclasseswithmembers(args);
        task2.keepclasseswithmembers(args);

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) can be used after String variant.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_afterStringVariant() throws Exception {
        // Given: String variant is called first
        task.keepclasseswithmembers("class First { void method1(); }");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers(Map)
        task.keepclasseswithmembers(args);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) can be used before String variant.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_beforeStringVariant() throws Exception {
        // Given: Map variant is called first
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclasseswithmembers(args);

        // When: Calling keepclasseswithmembers(String)
        task.keepclasseswithmembers("class Second { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) can be used after Map-String variant.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_afterMapStringVariant() throws Exception {
        // Given: Map-String variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args1, "class First { void method1(); }");

        // When: Calling keepclasseswithmembers(Map)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args2);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) can be used before Map-String variant.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_beforeMapStringVariant() throws Exception {
        // Given: Map variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args1);

        // When: Calling keepclasseswithmembers(Map, String)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args2, "class Second { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) works with realistic Android scenario.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_androidScenario() throws Exception {
        // Given: Android-like configuration
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("build/intermediates/classes.jar");
        task.outjars("build/outputs/proguard.jar");
        task.dontpreverify();

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertTrue(task.configuration.preverify == false,
                   "Preverification should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) works with multiple conditional rules.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_multipleConditions() throws Exception {
        // Given: Multiple conditional maps
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.FeatureA");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.FeatureB");

        // When: Calling keepclasseswithmembers() with conditionals
        task.keepclasseswithmembers(args1);
        task.keepclasseswithmembers(args2);

        // Then: Both conditionals should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) handles sequential calls properly.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_sequentialCalls() throws Exception {
        // When: Calling keepclasseswithmembers() sequentially
        for (int i = 0; i < 5; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            task.keepclasseswithmembers(args);
        }

        // Then: All calls should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) works with parallel-style initialization.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_parallelStyleInit() throws Exception {
        // Given: Multiple maps prepared in advance
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with all maps
        task.keepclasseswithmembers(args1);
        task.keepclasseswithmembers(args2);
        task.keepclasseswithmembers(args3);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map) integrates with optimization configuration.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_withOptimizationConfig() throws Exception {
        // Given: Optimization configured
        task.optimizationpasses(5);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args);

        // Then: Both configurations should coexist
        assertEquals(5, task.configuration.optimizationPasses,
                     "Optimization passes should be 5");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) integrates with obfuscation dictionary.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_withObfuscationDictionary() throws Exception {
        // Given: Obfuscation dictionary configured
        task.obfuscationdictionary("dictionary.txt");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.obfuscationDictionary,
                      "Obfuscation dictionary should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) works with verbose configuration.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_withVerboseConfig() throws Exception {
        // Given: Verbose mode enabled
        task.verbose();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args);

        // Then: Both configurations should coexist
        assertTrue(task.configuration.verbose,
                   "Verbose mode should be enabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) works with note configuration.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_withNoteConfig() throws Exception {
        // Given: Note configuration
        task.dontnote("com.example.**");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.note,
                      "Note configuration should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) works with warn configuration.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_withWarnConfig() throws Exception {
        // Given: Warn configuration
        task.dontwarn("com.example.**");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map
        task.keepclasseswithmembers(args);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.warn,
                      "Warn configuration should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map) preserves order of accumulation.
     */
    @Test
    public void testKeepClassesWithMembersMapOnly_preservesOrder() throws Exception {
        // When: Calling keepclasseswithmembers() multiple times
        task.keepclasseswithmembers("class First { void method1(); }");
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclasseswithmembers(args);
        task.keepclasseswithmembers("class Third { void method3(); }");

        // Then: All should be accumulated in order
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications in order");
    }

    // ========================================================================
    // Tests for keepclasseswithmembers(Map, Closure) variant (Map with Closure for member specs)
    // ========================================================================

    /**
     * Tests that keepclasseswithmembers(Map, Closure) with null closure adds specification.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_nullClosure_addsSpecification() throws Exception {
        // Given: Map with args and null closure
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with null closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) can be called multiple times.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_multipleCalls_accumulatesRules() throws Exception {
        // Given: Maps with null closures
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() multiple times
        task.keepclasseswithmembers(args1, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args2, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args3, (groovy.lang.Closure) null);

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) is the base method for Map-only variant.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_isBaseForMapOnlyVariant() throws Exception {
        // Given: Map-only variant calls this method internally
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling both variants
        task.keepclasseswithmembers(args); // Map-only variant
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null); // Map-Closure variant

        // Then: Both should produce same result
        assertEquals(2, task.configuration.keep.size(),
                     "Both variants should add specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works with conditional "if" clause.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepclasseswithmembers() with conditional and null closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) can interleave with all other variants.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_interleaveWithAllVariants() throws Exception {
        // Given: Using all four variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving all variants
        task.keepclasseswithmembers("class First { void method1(); }");
        task.keepclasseswithmembers(args, "class Second { void method2(); }");
        task.keepclasseswithmembers(args);
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works independently of injars.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map and closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works in a complex workflow.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");
        task.dontobfuscate();
        task.dontshrink();

        // When: Calling keepclasseswithmembers() with map and closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) with different conditionals accumulates.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_differentConditionals_accumulates() throws Exception {
        // Given: Different maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("if", "class com.example.Condition3");

        // When: Calling keepclasseswithmembers() with different conditionals
        task.keepclasseswithmembers(args1, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args2, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args3, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works with null map and null closure.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_nullMapNullClosure_addsSpecification() throws Exception {
        // When: Calling keepclasseswithmembers() with null map and null closure
        task.keepclasseswithmembers((java.util.Map<String, Object>) null, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) integrates with other configuration methods.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        task.dontoptimize();
        task.dontpreverify();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map and closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: All configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertTrue(task.configuration.optimize == false,
                   "Optimization should be disabled");
        assertTrue(task.configuration.preverify == false,
                   "Preverification should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) handles large accumulation.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_largeAccumulation() throws Exception {
        // When: Calling keepclasseswithmembers() many times
        for (int i = 0; i < 20; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            task.keepclasseswithmembers(args, (groovy.lang.Closure) null);
        }

        // Then: All should be accumulated
        assertEquals(20, task.configuration.keep.size(),
                     "Keep list should contain 20 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works with mixed empty and conditional maps.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_mixedEmptyAndConditional() throws Exception {
        // Given: Mix of empty and conditional maps
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with mixed maps
        task.keepclasseswithmembers(args1, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args2, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args3, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard5", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepclasseswithmembers() with map and closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);
        task2.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) can be used after String variant.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_afterStringVariant() throws Exception {
        // Given: String variant is called first
        task.keepclasseswithmembers("class First { void method1(); }");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers(Map, Closure)
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) can be used before String variant.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_beforeStringVariant() throws Exception {
        // Given: Map-Closure variant is called first
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // When: Calling keepclasseswithmembers(String)
        task.keepclasseswithmembers("class Second { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) can be used after Map-String variant.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_afterMapStringVariant() throws Exception {
        // Given: Map-String variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args1, "class First { void method1(); }");

        // When: Calling keepclasseswithmembers(Map, Closure)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args2, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) can be used before Map-String variant.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_beforeMapStringVariant() throws Exception {
        // Given: Map-Closure variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args1, (groovy.lang.Closure) null);

        // When: Calling keepclasseswithmembers(Map, String)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args2, "class Second { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) can be used after Map-only variant.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_afterMapOnlyVariant() throws Exception {
        // Given: Map-only variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args1);

        // When: Calling keepclasseswithmembers(Map, Closure)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args2, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) can be used before Map-only variant.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_beforeMapOnlyVariant() throws Exception {
        // Given: Map-Closure variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args1, (groovy.lang.Closure) null);

        // When: Calling keepclasseswithmembers(Map)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclasseswithmembers(args2);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works with realistic Android scenario.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_androidScenario() throws Exception {
        // Given: Android-like configuration
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("build/intermediates/classes.jar");
        task.outjars("build/outputs/proguard.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");
        task.dontpreverify();
        task.dontoptimize();

        // When: Calling keepclasseswithmembers() with map and closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertTrue(task.configuration.preverify == false,
                   "Preverification should be disabled");
        assertTrue(task.configuration.optimize == false,
                   "Optimization should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works with multiple conditional rules.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_multipleConditions() throws Exception {
        // Given: Multiple conditional maps
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.FeatureA");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.FeatureB");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("if", "class com.example.FeatureC");

        // When: Calling keepclasseswithmembers() with conditionals
        task.keepclasseswithmembers(args1, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args2, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args3, (groovy.lang.Closure) null);

        // Then: All conditionals should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) handles sequential calls properly.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_sequentialCalls() throws Exception {
        // When: Calling keepclasseswithmembers() sequentially
        for (int i = 0; i < 7; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            task.keepclasseswithmembers(args, (groovy.lang.Closure) null);
        }

        // Then: All calls should be accumulated
        assertEquals(7, task.configuration.keep.size(),
                     "Keep list should contain 7 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works with parallel-style initialization.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_parallelStyleInit() throws Exception {
        // Given: Multiple maps prepared in advance
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        java.util.Map<String, Object> args4 = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with all maps
        task.keepclasseswithmembers(args1, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args2, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args3, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args4, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) integrates with optimization configuration.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_withOptimizationConfig() throws Exception {
        // Given: Optimization configured
        task.optimizationpasses(7);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map and closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: Both configurations should coexist
        assertEquals(7, task.configuration.optimizationPasses,
                     "Optimization passes should be 7");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) integrates with obfuscation dictionary.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_withObfuscationDictionary() throws Exception {
        // Given: Obfuscation dictionary configured
        task.obfuscationdictionary("dictionary.txt");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map and closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.obfuscationDictionary,
                      "Obfuscation dictionary should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works with verbose configuration.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_withVerboseConfig() throws Exception {
        // Given: Verbose mode enabled
        task.verbose();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map and closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: Both configurations should coexist
        assertTrue(task.configuration.verbose,
                   "Verbose mode should be enabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works with note configuration.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_withNoteConfig() throws Exception {
        // Given: Note configuration
        task.dontnote("com.example.**");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map and closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.note,
                      "Note configuration should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works with warn configuration.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_withWarnConfig() throws Exception {
        // Given: Warn configuration
        task.dontwarn("com.example.**");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with map and closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.warn,
                      "Warn configuration should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) preserves order of accumulation.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_preservesOrder() throws Exception {
        // Given: Multiple variants used in sequence
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() in various ways
        task.keepclasseswithmembers("class First { void method1(); }");
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);
        task.keepclasseswithmembers(args, "class Third { void method3(); }");
        task.keepclasseswithmembers(args);

        // Then: All should be accumulated in order
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications in order");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) works with empty map and null closure.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_emptyMapNullClosure() throws Exception {
        // Given: Empty map and null closure
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembers() with empty map and null closure
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembers(Map, Closure) as base method for all variants.
     */
    @Test
    public void testKeepClassesWithMembersMapClosure_asBaseMethodForAllVariants() throws Exception {
        // Given: This is the base method that all variants eventually call
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Testing that all variants work through this base method
        // String variant -> Map-String variant -> this method
        task.keepclasseswithmembers("class First { void method1(); }");
        // Map-String variant -> this method
        task.keepclasseswithmembers(args, "class Second { void method2(); }");
        // Map-only variant -> this method with null Closure
        task.keepclasseswithmembers(args);
        // Direct call to this method
        task.keepclasseswithmembers(args, (groovy.lang.Closure) null);

        // Then: All should result in specifications being added
        assertEquals(4, task.configuration.keep.size(),
                     "All variants should work through the base method");
    }
}

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
 * Tests for {@link ProGuardTask#keepclasseswithmembernames(String)}.
 *
 * This test class verifies that the keepclasseswithmembernames(String) method correctly
 * adds keepclasseswithmembernames rules to the configuration.
 *
 * Method signature: keepclasseswithmembernames(Ljava/lang/String;)V
 * - Takes a class specification string that specifies which classes/members to keep names for
 * - Parses it and adds it to configuration.keep list
 * - Returns void
 * - Throws ParseException if the class specification is invalid
 *
 * Key behavior: This method tells ProGuard to keep both the classes and their member names,
 * but only if ALL specified members are present. This is different from:
 * - keepclassmembernames(): which keeps member names only, allowing class removal if unused
 * - keepnames(): which keeps names of classes and members without the "all members present" condition
 * - keepclasseswithmembers(): which keeps classes and members but doesn't preserve names after shrinking
 *
 * The keepclasseswithmembernames directive is useful when you want to preserve readable names
 * for classes that have specific members (e.g., classes with native methods), but still allow
 * classes without those members to be removed during shrinking.
 *
 * Internally calls keepclasseswithmembernames(null, String) with a null map.
 */
public class ProGuardTaskClaude_keepclasseswithmembernamesTest {

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
     * Tests that keepclasseswithmembernames() initializes and adds to the keep list.
     */
    @Test
    public void testKeepClassesWithMemberNames_initializesAndAddsToList() throws Exception {
        // Given: Initial state where keep list is null
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");

        // When: Calling keepclasseswithmembernames() with a class specification
        task.keepclasseswithmembernames("class MyClass");

        // Then: The keep list should be initialized and contain one entry
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() can be called multiple times to accumulate rules.
     */
    @Test
    public void testKeepClassesWithMemberNames_multipleCalls_accumulatesRules() throws Exception {
        // When: Calling keepclasseswithmembernames() multiple times
        task.keepclasseswithmembernames("class FirstClass");
        task.keepclasseswithmembernames("class SecondClass");
        task.keepclasseswithmembernames("class ThirdClass");

        // Then: All rules should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames() works with simple class specification.
     */
    @Test
    public void testKeepClassesWithMemberNames_simpleClassSpec() throws Exception {
        // Given: A simple class specification

        // When: Calling keepclasseswithmembernames() with simple class
        task.keepclasseswithmembernames("class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with wildcard class names.
     */
    @Test
    public void testKeepClassesWithMemberNames_wildcardClassName() throws Exception {
        // Given: A wildcard class specification

        // When: Calling keepclasseswithmembernames() with wildcard
        task.keepclasseswithmembernames("class com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with public modifier.
     */
    @Test
    public void testKeepClassesWithMemberNames_publicModifier() throws Exception {
        // Given: A specification with public modifier

        // When: Calling keepclasseswithmembernames() with public modifier
        task.keepclasseswithmembernames("public class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with abstract class.
     */
    @Test
    public void testKeepClassesWithMemberNames_abstractClass() throws Exception {
        // Given: An abstract class specification

        // When: Calling keepclasseswithmembernames() with abstract class
        task.keepclasseswithmembernames("abstract class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with interface.
     */
    @Test
    public void testKeepClassesWithMemberNames_interface() throws Exception {
        // Given: An interface specification

        // When: Calling keepclasseswithmembernames() with interface
        task.keepclasseswithmembernames("interface com.example.MyInterface");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with enum.
     */
    @Test
    public void testKeepClassesWithMemberNames_enum() throws Exception {
        // Given: An enum specification

        // When: Calling keepclasseswithmembernames() with enum
        task.keepclasseswithmembernames("enum com.example.MyEnum");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with extends clause.
     */
    @Test
    public void testKeepClassesWithMemberNames_extendsClause() throws Exception {
        // Given: A specification with extends clause

        // When: Calling keepclasseswithmembernames() with extends
        task.keepclasseswithmembernames("class * extends com.example.BaseClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with implements clause.
     */
    @Test
    public void testKeepClassesWithMemberNames_implementsClause() throws Exception {
        // Given: A specification with implements clause

        // When: Calling keepclasseswithmembernames() with implements
        task.keepclasseswithmembernames("class * implements java.io.Serializable");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with annotation specification.
     */
    @Test
    public void testKeepClassesWithMemberNames_annotation() throws Exception {
        // Given: An annotation specification

        // When: Calling keepclasseswithmembernames() with annotation
        task.keepclasseswithmembernames("@interface com.example.MyAnnotation");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with class annotation.
     */
    @Test
    public void testKeepClassesWithMemberNames_classWithAnnotation() throws Exception {
        // Given: A class with annotation specification

        // When: Calling keepclasseswithmembernames() with class annotation
        task.keepclasseswithmembernames("@com.example.KeepName class *");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with member specification.
     */
    @Test
    public void testKeepClassesWithMemberNames_withMembers() throws Exception {
        // Given: A class with members specification

        // When: Calling keepclasseswithmembernames() with members
        task.keepclasseswithmembernames("class com.example.MyClass { <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with field specification.
     */
    @Test
    public void testKeepClassesWithMemberNames_withFields() throws Exception {
        // Given: A class with fields specification

        // When: Calling keepclasseswithmembernames() with fields
        task.keepclasseswithmembernames("class com.example.MyClass { <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with specific method specification.
     */
    @Test
    public void testKeepClassesWithMemberNames_specificMethod() throws Exception {
        // Given: A specific method specification

        // When: Calling keepclasseswithmembernames() with specific method
        task.keepclasseswithmembernames("class com.example.MyClass { void myMethod(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with specific field specification.
     */
    @Test
    public void testKeepClassesWithMemberNames_specificField() throws Exception {
        // Given: A specific field specification

        // When: Calling keepclasseswithmembernames() with specific field
        task.keepclasseswithmembernames("class com.example.MyClass { String name; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with nested classes.
     */
    @Test
    public void testKeepClassesWithMemberNames_nestedClass() throws Exception {
        // Given: A nested class specification

        // When: Calling keepclasseswithmembernames() with nested class
        task.keepclasseswithmembernames("class com.example.Outer$Inner");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with package wildcard.
     */
    @Test
    public void testKeepClassesWithMemberNames_packageWildcard() throws Exception {
        // Given: A package wildcard specification

        // When: Calling keepclasseswithmembernames() with package wildcard
        task.keepclasseswithmembernames("class com.example.*");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with double package wildcard.
     */
    @Test
    public void testKeepClassesWithMemberNames_doublePackageWildcard() throws Exception {
        // Given: A double package wildcard specification

        // When: Calling keepclasseswithmembernames() with double package wildcard
        task.keepclasseswithmembernames("class com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with final modifier.
     */
    @Test
    public void testKeepClassesWithMemberNames_finalModifier() throws Exception {
        // Given: A specification with final modifier

        // When: Calling keepclasseswithmembernames() with final modifier
        task.keepclasseswithmembernames("final class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with all members wildcard.
     */
    @Test
    public void testKeepClassesWithMemberNames_allMembersWildcard() throws Exception {
        // Given: An all-members wildcard specification

        // When: Calling keepclasseswithmembernames() with all members wildcard
        task.keepclasseswithmembernames("class com.example.MyClass { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with Android context.
     */
    @Test
    public void testKeepClassesWithMemberNames_androidContext() throws Exception {
        // Given: An Android-specific specification

        // When: Calling keepclasseswithmembernames() with Android class
        task.keepclasseswithmembernames("class * extends android.app.Activity");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with serialization classes.
     */
    @Test
    public void testKeepClassesWithMemberNames_serialization() throws Exception {
        // Given: A serialization class specification

        // When: Calling keepclasseswithmembernames() with Serializable
        task.keepclasseswithmembernames("class * implements java.io.Serializable");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with native methods.
     */
    @Test
    public void testKeepClassesWithMemberNames_nativeMethods() throws Exception {
        // Given: A native method specification

        // When: Calling keepclasseswithmembernames() with native methods
        task.keepclasseswithmembernames("class com.example.NativeLib { native <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with static methods.
     */
    @Test
    public void testKeepClassesWithMemberNames_staticMethods() throws Exception {
        // Given: A static method specification

        // When: Calling keepclasseswithmembernames() with static methods
        task.keepclasseswithmembernames("class com.example.Util { public static <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with constructors.
     */
    @Test
    public void testKeepClassesWithMemberNames_constructors() throws Exception {
        // Given: A constructor specification

        // When: Calling keepclasseswithmembernames() with constructors
        task.keepclasseswithmembernames("class com.example.MyClass { <init>(...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with R class fields.
     */
    @Test
    public void testKeepClassesWithMemberNames_androidRClass() throws Exception {
        // Given: An Android R class specification

        // When: Calling keepclasseswithmembernames() with R class
        task.keepclasseswithmembernames("class **.R$*");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() can be mixed with other configuration methods.
     */
    @Test
    public void testKeepClassesWithMemberNames_mixedWithOtherConfiguration() throws Exception {
        // Given: Other configuration is present
        task.dontobfuscate();

        // When: Calling keepclasseswithmembernames()
        task.keepclasseswithmembernames("class com.example.MyClass");

        // Then: Both configurations should be present
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() accumulates across multiple calls.
     */
    @Test
    public void testKeepClassesWithMemberNames_accumulation() throws Exception {
        // When: Calling keepclasseswithmembernames() multiple times with different specs
        task.keepclasseswithmembernames("class com.example.ClassA");
        task.keepclasseswithmembernames("class com.example.ClassB");
        task.keepclasseswithmembernames("class com.example.ClassC");
        task.keepclasseswithmembernames("class com.example.ClassD");
        task.keepclasseswithmembernames("class com.example.ClassE");

        // Then: All specifications should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames() works with method parameters.
     */
    @Test
    public void testKeepClassesWithMemberNames_methodParameters() throws Exception {
        // Given: A method with parameters specification

        // When: Calling keepclasseswithmembernames() with method parameters
        task.keepclasseswithmembernames("class com.example.MyClass { void process(java.lang.String, int); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with varargs.
     */
    @Test
    public void testKeepClassesWithMemberNames_varargs() throws Exception {
        // Given: A varargs specification

        // When: Calling keepclasseswithmembernames() with varargs
        task.keepclasseswithmembernames("class com.example.MyClass { void print(java.lang.String...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with array types.
     */
    @Test
    public void testKeepClassesWithMemberNames_arrayTypes() throws Exception {
        // Given: An array type specification

        // When: Calling keepclasseswithmembernames() with array types
        task.keepclasseswithmembernames("class com.example.MyClass { java.lang.String[] getItems(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with generic types.
     */
    @Test
    public void testKeepClassesWithMemberNames_genericTypes() throws Exception {
        // Given: A generic type specification

        // When: Calling keepclasseswithmembernames() with generic types
        task.keepclasseswithmembernames("class com.example.MyClass { java.util.List get(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with volatile fields.
     */
    @Test
    public void testKeepClassesWithMemberNames_volatileFields() throws Exception {
        // Given: A volatile field specification

        // When: Calling keepclasseswithmembernames() with volatile field
        task.keepclasseswithmembernames("class com.example.ThreadSafe { volatile boolean flag; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with synchronized methods.
     */
    @Test
    public void testKeepClassesWithMemberNames_synchronizedMethods() throws Exception {
        // Given: A synchronized method specification

        // When: Calling keepclasseswithmembernames() with synchronized method
        task.keepclasseswithmembernames("class com.example.ThreadSafe { synchronized void update(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with transient fields.
     */
    @Test
    public void testKeepClassesWithMemberNames_transientFields() throws Exception {
        // Given: A transient field specification

        // When: Calling keepclasseswithmembernames() with transient field
        task.keepclasseswithmembernames("class com.example.Serializable { transient int cache; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepClassesWithMemberNames_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Both tasks call keepclasseswithmembernames() with the same spec
        task.keepclasseswithmembernames("class com.example.MyClass");
        task2.keepclasseswithmembernames("class com.example.MyClass");

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames() works with wildcard return types.
     */
    @Test
    public void testKeepClassesWithMemberNames_wildcardReturnTypes() throws Exception {
        // Given: A wildcard return type specification

        // When: Calling keepclasseswithmembernames() with wildcard return type
        task.keepclasseswithmembernames("class com.example.MyClass { *** get*(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    // ========================================================================
    // Tests for keepclasseswithmembernames(Map, String) variant
    // ========================================================================

    /**
     * Tests that keepclasseswithmembernames(Map, String) with null map works like keepclasseswithmembernames(String).
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_nullMap_behavesLikeStringOnly() throws Exception {
        // When: Calling keepclasseswithmembernames() with null map
        task.keepclasseswithmembernames(null, "class MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) with empty map adds specification.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_emptyMap_addsSpecification() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with empty map
        task.keepclasseswithmembernames(args, "class MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) can be called multiple times.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_multipleCalls_accumulatesRules() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() multiple times
        task.keepclasseswithmembernames(args, "class FirstClass");
        task.keepclasseswithmembernames(args, "class SecondClass");
        task.keepclasseswithmembernames(args, "class ThirdClass");

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with conditional "if" clause.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepclasseswithmembernames() with conditional
        task.keepclasseswithmembernames(args, "class com.example.MyClass");

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works independently of injars.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with map
        task.keepclasseswithmembernames(args, "class MyClass");

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works in a complex workflow.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");

        // When: Calling keepclasseswithmembernames() with map
        task.keepclasseswithmembernames(args, "class com.example.Model");

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) with different maps accumulates.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_differentMaps_accumulates() throws Exception {
        // Given: Different maps for different rules
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");

        // When: Calling keepclasseswithmembernames() with different maps
        task.keepclasseswithmembernames(args1, "class FirstClass");
        task.keepclasseswithmembernames(args2, "class SecondClass");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with wildcard classes.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_wildcardClasses() throws Exception {
        // Given: Map for wildcard rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with wildcard classes
        task.keepclasseswithmembernames(args, "class com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with extends clause.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_extendsClause() throws Exception {
        // Given: Map for extends rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with extends
        task.keepclasseswithmembernames(args, "class * extends com.example.BaseClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with implements clause.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_implementsClause() throws Exception {
        // Given: Map for implements rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with implements
        task.keepclasseswithmembernames(args, "class * implements java.io.Serializable");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with member specifications.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_memberSpecifications() throws Exception {
        // Given: Map for member rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with members
        task.keepclasseswithmembernames(args, "class com.example.MyClass { <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with field specifications.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_fieldSpecifications() throws Exception {
        // Given: Map for field rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with fields
        task.keepclasseswithmembernames(args, "class com.example.MyClass { <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with Android classes.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_androidClasses() throws Exception {
        // Given: Map for Android rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() for Android classes
        task.keepclasseswithmembernames(args, "class * extends android.app.Activity");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with enum classes.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_enumClasses() throws Exception {
        // Given: Map for enum rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() for enums
        task.keepclasseswithmembernames(args, "enum *");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with interface classes.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_interfaceClasses() throws Exception {
        // Given: Map for interface rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() for interfaces
        task.keepclasseswithmembernames(args, "interface com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with annotation specifications.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_annotationSpecifications() throws Exception {
        // Given: Map for annotation rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with annotations
        task.keepclasseswithmembernames(args, "@interface com.example.MyAnnotation");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with class annotations.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_classAnnotations() throws Exception {
        // Given: Map for class annotation rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with class annotations
        task.keepclasseswithmembernames(args, "@com.example.KeepName class *");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) can interleave with keepclasseswithmembernames(String).
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_interleaveWithStringVariant() throws Exception {
        // Given: Using both variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepclasseswithmembernames("class First");
        task.keepclasseswithmembernames(args, "class Second");
        task.keepclasseswithmembernames("class Third");
        task.keepclasseswithmembernames(args, "class Fourth");

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with wildcard return types.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_wildcardReturnTypes() throws Exception {
        // Given: Map for wildcard return types
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with wildcard return type
        task.keepclasseswithmembernames(args, "class com.example.MyClass { *** get*(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with native methods.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_nativeMethods() throws Exception {
        // Given: Map for native methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() for native methods
        task.keepclasseswithmembernames(args, "class com.example.NativeLib { native <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with static methods.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_staticMethods() throws Exception {
        // Given: Map for static methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() for static methods
        task.keepclasseswithmembernames(args, "class com.example.Util { public static <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with conditional compilation.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_conditionalCompilation() throws Exception {
        // Given: Map with conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.FeatureEnabled");

        // When: Calling keepclasseswithmembernames() with conditional
        task.keepclasseswithmembernames(args, "class com.example.FeatureClass");

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) accumulates multiple conditionals.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_multipleConditionals_accumulates() throws Exception {
        // Given: Multiple maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");

        // When: Calling keepclasseswithmembernames() with different conditionals
        task.keepclasseswithmembernames(args1, "class First");
        task.keepclasseswithmembernames(args2, "class Second");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with nested classes.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_nestedClasses() throws Exception {
        // Given: Map for nested classes
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() for nested classes
        task.keepclasseswithmembernames(args, "class com.example.Outer$Inner");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) integrates with other configuration.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with map
        task.keepclasseswithmembernames(args, "class com.example.MyClass");

        // Then: All configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) handles large accumulation.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_largeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() many times
        for (int i = 0; i < 10; i++) {
            task.keepclasseswithmembernames(args, "class Class" + i);
        }

        // Then: All should be accumulated
        assertEquals(10, task.configuration.keep.size(),
                     "Keep list should contain 10 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with R class fields.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_rClassFields() throws Exception {
        // Given: Map for R class fields
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() for R class
        task.keepclasseswithmembernames(args, "class **.R$*");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with constructors.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_constructors() throws Exception {
        // Given: Map for constructors
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() for constructors
        task.keepclasseswithmembernames(args, "class com.example.MyClass { <init>(...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) preserves consistent behavior.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_consistentBehavior() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard3", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepclasseswithmembernames() with map
        task.keepclasseswithmembernames(args, "class MyClass");
        task2.keepclasseswithmembernames(args, "class MyClass");

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with specific method signatures.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_specificMethodSignatures() throws Exception {
        // Given: Map for specific method
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with specific method signature
        task.keepclasseswithmembernames(args, "class com.example.MyClass { void process(java.lang.String, int); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with all members wildcard.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_allMembersWildcard() throws Exception {
        // Given: Map for all members
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with all members wildcard
        task.keepclasseswithmembernames(args, "class com.example.MyClass { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with serialization methods.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_serializationMethods() throws Exception {
        // Given: Map for serialization
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() for serialization methods
        task.keepclasseswithmembernames(args, "class * implements java.io.Serializable { private void writeObject(java.io.ObjectOutputStream); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, String) works with varargs methods.
     */
    @Test
    public void testKeepClassesWithMemberNamesWithMap_varargsMethod() throws Exception {
        // Given: Map for varargs
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with varargs method
        task.keepclasseswithmembernames(args, "class com.example.MyClass { void print(java.lang.String...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    // ========================================================================
    // Tests for keepclasseswithmembernames(Map) variant - Groovy DSL style
    // ========================================================================

    /**
     * Tests that keepclasseswithmembernames(Map) with name parameter adds specification.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withName_addsSpecification() throws Exception {
        // Given: Map with name parameter
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with type parameter works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withType_addsSpecification() throws Exception {
        // Given: Map with type parameter
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "class");
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with interface type works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withInterfaceType() throws Exception {
        // Given: Map with interface type
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "interface");
        args.put("name", "com.example.MyInterface");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with enum type works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withEnumType() throws Exception {
        // Given: Map with enum type
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "enum");
        args.put("name", "com.example.MyEnum");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with access modifier works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withAccessModifier() throws Exception {
        // Given: Map with access modifier
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with abstract access modifier works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withAbstractModifier() throws Exception {
        // Given: Map with abstract modifier
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "abstract");
        args.put("name", "com.example.AbstractClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with final modifier works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withFinalModifier() throws Exception {
        // Given: Map with final modifier
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "final");
        args.put("name", "com.example.FinalClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with annotation parameter works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withAnnotation() throws Exception {
        // Given: Map with annotation parameter
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("annotation", "com.example.KeepName");
        args.put("name", "*");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with extends parameter works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withExtends() throws Exception {
        // Given: Map with extends parameter
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "*");
        args.put("extends", "com.example.BaseClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with implements parameter works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withImplements() throws Exception {
        // Given: Map with implements parameter
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "*");
        args.put("implements", "java.io.Serializable");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with wildcard name works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withWildcardName() throws Exception {
        // Given: Map with wildcard name
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.**");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with single wildcard works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withSingleWildcard() throws Exception {
        // Given: Map with single wildcard
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "*");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) can be called multiple times.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_multipleCalls_accumulates() throws Exception {
        // Given: Multiple Map specifications
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.ClassA");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.ClassB");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.ClassC");

        // When: Calling keepclasseswithmembernames() multiple times
        task.keepclasseswithmembernames(args1);
        task.keepclasseswithmembernames(args2);
        task.keepclasseswithmembernames(args3);

        // Then: All specifications should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with combined parameters works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_combinedParameters() throws Exception {
        // Given: Map with multiple parameters
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public");
        args.put("type", "class");
        args.put("name", "com.example.MyClass");
        args.put("extends", "com.example.BaseClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) with extendsannotation works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_withExtendsAnnotation() throws Exception {
        // Given: Map with extendsannotation parameter
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "*");
        args.put("extendsannotation", "com.example.MyAnnotation");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) works with Android Activity.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_androidActivity() throws Exception {
        // Given: Map for Android Activity
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "*");
        args.put("extends", "android.app.Activity");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) works with Serializable.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_serializable() throws Exception {
        // Given: Map for Serializable classes
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "*");
        args.put("implements", "java.io.Serializable");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) initializes keep list if null.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_initializesKeepList() throws Exception {
        // Given: Initial state where keep list is null
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The keep list should be initialized
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) works with synthetic access flag.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_syntheticModifier() throws Exception {
        // Given: Map with synthetic modifier
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "synthetic");
        args.put("name", "com.example.SyntheticClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) works with negated access modifier.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_negatedModifier() throws Exception {
        // Given: Map with negated access modifier
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "!abstract");
        args.put("name", "com.example.ConcreteClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) works with multiple access modifiers.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_multipleModifiers() throws Exception {
        // Given: Map with multiple access modifiers
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "public,final");
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) integrates with other configuration.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: Both configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) works with negated type.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_negatedType() throws Exception {
        // Given: Map with negated type
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("type", "!interface");
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) can mix with other variants.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_mixWithStringVariant() throws Exception {
        // Given: Using both Map-only and String variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.ClassA");

        // When: Mixing Map-only and String variants
        task.keepclasseswithmembernames(args);
        task.keepclasseswithmembernames("class com.example.ClassB");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) works with full package name.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_fullPackageName() throws Exception {
        // Given: Map with full package name
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.myapp.activities.MainActivity");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) works with annotation type.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_annotationType() throws Exception {
        // Given: Map with annotation type
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("access", "annotation");
        args.put("name", "com.example.MyAnnotation");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) preserves task independence.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_taskIndependence() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard4", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Both tasks call keepclasseswithmembernames() with Map
        task.keepclasseswithmembernames(args);
        task2.keepclasseswithmembernames(args);

        // Then: Both should have one specification independently
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map) works with empty name (matches all).
     */
    @Test
    public void testKeepClassesWithMemberNamesMapOnly_matchAll() throws Exception {
        // Given: Map with wildcard to match all
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "**");

        // When: Calling keepclasseswithmembernames() with Map only
        task.keepclasseswithmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    // ========================================================================
    // Tests for keepclasseswithmembernames(Map, Closure) variant
    // ========================================================================

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) with null closure adds specification.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_nullClosure_addsSpecification() throws Exception {
        // Given: Map with args and null closure
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with null closure
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) can be called multiple times.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_multipleCalls_accumulates() throws Exception {
        // Given: Multiple Map specifications
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.ClassA");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.ClassB");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.ClassC");

        // When: Calling keepclasseswithmembernames() multiple times
        task.keepclasseswithmembernames(args1, (groovy.lang.Closure) null);
        task.keepclasseswithmembernames(args2, (groovy.lang.Closure) null);
        task.keepclasseswithmembernames(args3, (groovy.lang.Closure) null);

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) works same as Map-only variant.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_equivalentToMapOnly() throws Exception {
        // Given: Same map for both variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling both variants
        task.keepclasseswithmembernames(args); // Map-only variant
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null); // Map-Closure variant

        // Then: Both should produce specifications
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) works with conditional if.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_withConditional() throws Exception {
        // Given: Map with conditional 'if' clause
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepclasseswithmembernames() with conditional and null closure
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) mixes with other variants.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_mixWithOtherVariants() throws Exception {
        // Given: Using all different variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.ClassFromMap");

        // When: Calling different variants
        task.keepclasseswithmembernames("class com.example.First");
        task.keepclasseswithmembernames(args, "class com.example.Second");
        task.keepclasseswithmembernames(args);
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) works with injars configuration.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_withInjarsConfiguration() throws Exception {
        // Given: Configuration with injars
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with map and closure
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(), "Should have 1 keep specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) integrates with multiple configurations.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_multipleConfigurations() throws Exception {
        // Given: Multiple configurations
        task.injars("input.jar");
        task.outjars("output.jar");
        task.dontshrink();
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with map and closure
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertFalse(task.configuration.shrink, "Shrinking should be disabled");
        assertEquals(1, task.configuration.keep.size(), "Should have 1 keep specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) works with multiple conditionals.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_multipleConditionals() throws Exception {
        // Given: Maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.Class1");
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Class2");
        args2.put("if", "class com.example.Condition2");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("name", "com.example.Class3");
        args3.put("if", "class com.example.Condition3");

        // When: Calling keepclasseswithmembernames() with different conditionals
        task.keepclasseswithmembernames(args1, (groovy.lang.Closure) null);
        task.keepclasseswithmembernames(args2, (groovy.lang.Closure) null);
        task.keepclasseswithmembernames(args3, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) with null map and null closure.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_nullMapNullClosure() throws Exception {
        // When: Calling keepclasseswithmembernames() with null map and null closure
        task.keepclasseswithmembernames((java.util.Map<String, Object>) null, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) with empty map works.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_emptyMap() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclasseswithmembernames() with empty map and null closure
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) works with dontobfuscate.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_withDontobfuscate() throws Exception {
        // Given: Obfuscation is disabled
        task.dontobfuscate();
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with map and closure
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: Both configurations should coexist
        assertFalse(task.configuration.obfuscate,
                    "Obfuscation should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) accumulates many calls.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_manyCallsAccumulate() throws Exception {
        // When: Calling keepclasseswithmembernames() many times
        for (int i = 0; i < 20; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            args.put("name", "com.example.Class" + i);
            task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);
        }

        // Then: All should be accumulated
        assertEquals(20, task.configuration.keep.size(),
                     "Keep list should contain 20 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) with different access modifiers.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_differentAccessModifiers() throws Exception {
        // Given: Maps with different access modifiers
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("access", "public");
        args1.put("name", "com.example.PublicClass");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("access", "final");
        args2.put("name", "com.example.FinalClass");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("access", "abstract");
        args3.put("name", "com.example.AbstractClass");

        // When: Calling keepclasseswithmembernames() with different access modifiers
        task.keepclasseswithmembernames(args1, (groovy.lang.Closure) null);
        task.keepclasseswithmembernames(args2, (groovy.lang.Closure) null);
        task.keepclasseswithmembernames(args3, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) preserves task independence.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_taskIndependence() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard5", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Both tasks call keepclasseswithmembernames() with map and closure
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);
        task2.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) after string variant.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_afterStringVariant() throws Exception {
        // Given: String variant is called first
        task.keepclasseswithmembernames("class com.example.First");

        // When: Calling keepclasseswithmembernames(Map, Closure)
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.Second");
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) before string variant.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_beforeStringVariant() throws Exception {
        // Given: Map-Closure variant is called first
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.First");
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // When: Calling keepclasseswithmembernames(String)
        task.keepclasseswithmembernames("class com.example.Second");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) after Map+String variant.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_afterMapStringVariant() throws Exception {
        // Given: Map+String variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclasseswithmembernames(args1, "class com.example.First");

        // When: Calling keepclasseswithmembernames(Map, Closure)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Second");
        task.keepclasseswithmembernames(args2, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) before Map+String variant.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_beforeMapStringVariant() throws Exception {
        // Given: Map-Closure variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.First");
        task.keepclasseswithmembernames(args1, (groovy.lang.Closure) null);

        // When: Calling keepclasseswithmembernames(Map, String)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclasseswithmembernames(args2, "class com.example.Second");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) after Map-only variant.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_afterMapOnlyVariant() throws Exception {
        // Given: Map-only variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.First");
        task.keepclasseswithmembernames(args1);

        // When: Calling keepclasseswithmembernames(Map, Closure)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Second");
        task.keepclasseswithmembernames(args2, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) before Map-only variant.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_beforeMapOnlyVariant() throws Exception {
        // Given: Map-Closure variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("name", "com.example.First");
        task.keepclasseswithmembernames(args1, (groovy.lang.Closure) null);

        // When: Calling keepclasseswithmembernames(Map)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("name", "com.example.Second");
        task.keepclasseswithmembernames(args2);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) works with complex configuration.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_complexConfiguration() throws Exception {
        // Given: Complex configuration with multiple settings
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.dontoptimize();
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepclasseswithmembernames() with map and closure
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertFalse(task.configuration.optimize, "Optimization should be disabled");
        assertEquals(1, task.configuration.keep.size(), "Should have 1 keep specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) with different types.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_differentTypes() throws Exception {
        // Given: Maps with different types
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("type", "class");
        args1.put("name", "com.example.MyClass");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("type", "interface");
        args2.put("name", "com.example.MyInterface");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("type", "enum");
        args3.put("name", "com.example.MyEnum");

        // When: Calling keepclasseswithmembernames() with different types
        task.keepclasseswithmembernames(args1, (groovy.lang.Closure) null);
        task.keepclasseswithmembernames(args2, (groovy.lang.Closure) null);
        task.keepclasseswithmembernames(args3, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) with extends clause.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_withExtends() throws Exception {
        // Given: Map with extends clause
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "*");
        args.put("extends", "com.example.BaseClass");

        // When: Calling keepclasseswithmembernames() with map and closure
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) with implements clause.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_withImplements() throws Exception {
        // Given: Map with implements clause
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "*");
        args.put("implements", "java.io.Serializable");

        // When: Calling keepclasseswithmembernames() with map and closure
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) with annotation.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_withAnnotation() throws Exception {
        // Given: Map with annotation
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("annotation", "com.example.KeepName");
        args.put("name", "*");

        // When: Calling keepclasseswithmembernames() with map and closure
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclasseswithmembernames(Map, Closure) with all four variants mixed.
     */
    @Test
    public void testKeepClassesWithMemberNamesMapClosure_allVariantsMixed() throws Exception {
        // Given: Preparing to use all four variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.FromMap");

        // When: Mixing all four method variants
        // 1. String variant
        task.keepclasseswithmembernames("class com.example.FromString");
        // 2. Map+String variant
        task.keepclasseswithmembernames(args, "class com.example.FromMapString");
        // 3. Map-only variant -> internally calls Map+Closure with null Closure
        task.keepclasseswithmembernames(args);
        // 4. Direct call to Map+Closure variant
        task.keepclasseswithmembernames(args, (groovy.lang.Closure) null);

        // Then: All should result in specifications being added
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications from all variants");
    }
}

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
 * Tests for {@link ProGuardTask#keepclassmembers(String)}.
 *
 * This test class verifies that the keepclassmembers(String) method correctly
 * adds keepclassmembers rules to the configuration.
 *
 * Method signature: keepclassmembers(Ljava/lang/String;)V
 * - Takes a class specification string that specifies which class members to keep
 * - Parses it and adds it to configuration.keep list
 * - Returns void
 * - Throws ParseException if the class specification is invalid
 *
 * Key behavior: This method tells ProGuard to keep class members (fields, methods)
 * but allows the containing class to be removed if it's not used otherwise.
 * This is different from keep() which preserves both the class and its members.
 *
 * The keepclassmembers directive is useful for keeping specific methods/fields that
 * are accessed via reflection, serialization, or other runtime mechanisms, while
 * still allowing ProGuard to remove the class itself if it's unused.
 *
 * Internally calls keepclassmembers(null, String) with a null map.
 */
public class ProGuardTaskClaude_keepclassmembersTest {

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
     * Tests that keepclassmembers() initializes and adds to the keep list.
     */
    @Test
    public void testKeepClassMembers_initializesAndAddsToList() throws Exception {
        // Given: Initial state where keep list is null
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");

        // When: Calling keepclassmembers() with a class specification
        task.keepclassmembers("class MyClass { void myMethod(); }");

        // Then: The keep list should be initialized and contain one entry
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() can be called multiple times to accumulate rules.
     */
    @Test
    public void testKeepClassMembers_multipleCalls_accumulatesRules() throws Exception {
        // When: Calling keepclassmembers() multiple times
        task.keepclassmembers("class MyClass { void method1(); }");
        task.keepclassmembers("class AnotherClass { int field; }");
        task.keepclassmembers("class ThirdClass { <init>(); }");

        // Then: All rules should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembers() works with simple method specification.
     */
    @Test
    public void testKeepClassMembers_simpleMethodSpec() throws Exception {
        // Given: A simple method specification

        // When: Calling keepclassmembers() with simple method spec
        task.keepclassmembers("class com.example.MyClass { public void onCreate(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with field specification.
     */
    @Test
    public void testKeepClassMembers_fieldSpec() throws Exception {
        // Given: A field specification

        // When: Calling keepclassmembers() with field spec
        task.keepclassmembers("class com.example.Data { private String name; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with wildcard class names.
     */
    @Test
    public void testKeepClassMembers_wildcardClassName() throws Exception {
        // Given: A wildcard class specification

        // When: Calling keepclassmembers() with wildcard
        task.keepclassmembers("class * extends android.app.Activity { void onCreate(...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with wildcard member names.
     */
    @Test
    public void testKeepClassMembers_wildcardMemberName() throws Exception {
        // Given: A wildcard member specification

        // When: Calling keepclassmembers() with wildcard member
        task.keepclassmembers("class com.example.MyClass { public *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with all members wildcard.
     */
    @Test
    public void testKeepClassMembers_allMembersWildcard() throws Exception {
        // Given: An all-members wildcard specification

        // When: Calling keepclassmembers() with all members wildcard
        task.keepclassmembers("class com.example.MyClass { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with constructor specification.
     */
    @Test
    public void testKeepClassMembers_constructorSpec() throws Exception {
        // Given: A constructor specification

        // When: Calling keepclassmembers() with constructor
        task.keepclassmembers("class com.example.MyClass { <init>(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with parameterized constructor.
     */
    @Test
    public void testKeepClassMembers_parameterizedConstructor() throws Exception {
        // Given: A parameterized constructor specification

        // When: Calling keepclassmembers() with parameterized constructor
        task.keepclassmembers("class com.example.MyClass { <init>(java.lang.String, int); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with method return types.
     */
    @Test
    public void testKeepClassMembers_methodWithReturnType() throws Exception {
        // Given: A method with return type specification

        // When: Calling keepclassmembers() with return type
        task.keepclassmembers("class com.example.MyClass { java.lang.String getName(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with method parameters.
     */
    @Test
    public void testKeepClassMembers_methodWithParameters() throws Exception {
        // Given: A method with parameters specification

        // When: Calling keepclassmembers() with parameters
        task.keepclassmembers("class com.example.MyClass { void setData(java.lang.String, int); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with multiple members in one rule.
     */
    @Test
    public void testKeepClassMembers_multipleMembers() throws Exception {
        // Given: Multiple members in one specification

        // When: Calling keepclassmembers() with multiple members
        task.keepclassmembers("class com.example.MyClass { void method1(); void method2(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with public modifier.
     */
    @Test
    public void testKeepClassMembers_publicModifier() throws Exception {
        // Given: A specification with public modifier

        // When: Calling keepclassmembers() with public modifier
        task.keepclassmembers("class com.example.MyClass { public void myMethod(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with protected modifier.
     */
    @Test
    public void testKeepClassMembers_protectedModifier() throws Exception {
        // Given: A specification with protected modifier

        // When: Calling keepclassmembers() with protected modifier
        task.keepclassmembers("class com.example.MyClass { protected void myMethod(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with private modifier.
     */
    @Test
    public void testKeepClassMembers_privateModifier() throws Exception {
        // Given: A specification with private modifier

        // When: Calling keepclassmembers() with private modifier
        task.keepclassmembers("class com.example.MyClass { private int field; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with static modifier.
     */
    @Test
    public void testKeepClassMembers_staticModifier() throws Exception {
        // Given: A specification with static modifier

        // When: Calling keepclassmembers() with static modifier
        task.keepclassmembers("class com.example.MyClass { public static void main(java.lang.String[]); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with final modifier.
     */
    @Test
    public void testKeepClassMembers_finalModifier() throws Exception {
        // Given: A specification with final modifier

        // When: Calling keepclassmembers() with final modifier
        task.keepclassmembers("class com.example.MyClass { public final String CONSTANT; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with abstract class.
     */
    @Test
    public void testKeepClassMembers_abstractClass() throws Exception {
        // Given: An abstract class specification

        // When: Calling keepclassmembers() with abstract class
        task.keepclassmembers("abstract class com.example.MyClass { abstract void process(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with interface.
     */
    @Test
    public void testKeepClassMembers_interface() throws Exception {
        // Given: An interface specification

        // When: Calling keepclassmembers() with interface
        task.keepclassmembers("interface com.example.MyInterface { void callback(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with enum.
     */
    @Test
    public void testKeepClassMembers_enum() throws Exception {
        // Given: An enum specification

        // When: Calling keepclassmembers() with enum
        task.keepclassmembers("enum com.example.MyEnum { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with implements clause.
     */
    @Test
    public void testKeepClassMembers_implementsClause() throws Exception {
        // Given: A specification with implements clause

        // When: Calling keepclassmembers() with implements
        task.keepclassmembers("class * implements java.io.Serializable { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with extends clause.
     */
    @Test
    public void testKeepClassMembers_extendsClause() throws Exception {
        // Given: A specification with extends clause

        // When: Calling keepclassmembers() with extends
        task.keepclassmembers("class * extends com.example.BaseClass { void onEvent(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with annotation specification.
     */
    @Test
    public void testKeepClassMembers_annotation() throws Exception {
        // Given: An annotation specification

        // When: Calling keepclassmembers() with annotation
        task.keepclassmembers("@interface com.example.MyAnnotation { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with class annotation.
     */
    @Test
    public void testKeepClassMembers_classWithAnnotation() throws Exception {
        // Given: A class with annotation specification

        // When: Calling keepclassmembers() with class annotation
        task.keepclassmembers("@com.example.Serializable class * { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with member annotation.
     */
    @Test
    public void testKeepClassMembers_memberWithAnnotation() throws Exception {
        // Given: A member with annotation specification

        // When: Calling keepclassmembers() with member annotation
        task.keepclassmembers("class * { @com.example.Inject *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with varargs.
     */
    @Test
    public void testKeepClassMembers_varargs() throws Exception {
        // Given: A varargs specification

        // When: Calling keepclassmembers() with varargs
        task.keepclassmembers("class com.example.MyClass { void print(java.lang.String...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with array types.
     */
    @Test
    public void testKeepClassMembers_arrayTypes() throws Exception {
        // Given: An array type specification

        // When: Calling keepclassmembers() with array types
        task.keepclassmembers("class com.example.MyClass { java.lang.String[] getItems(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with nested classes.
     */
    @Test
    public void testKeepClassMembers_nestedClass() throws Exception {
        // Given: A nested class specification

        // When: Calling keepclassmembers() with nested class
        task.keepclassmembers("class com.example.Outer$Inner { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with primitive types.
     */
    @Test
    public void testKeepClassMembers_primitiveTypes() throws Exception {
        // Given: Primitive type specifications

        // When: Calling keepclassmembers() with primitive types
        task.keepclassmembers("class com.example.MyClass { int getValue(); void setValue(int); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with generic types.
     */
    @Test
    public void testKeepClassMembers_genericTypes() throws Exception {
        // Given: A generic type specification

        // When: Calling keepclassmembers() with generic types
        task.keepclassmembers("class com.example.MyClass { java.util.List get(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with serialization members.
     */
    @Test
    public void testKeepClassMembers_serializationMembers() throws Exception {
        // Given: Serialization member specifications

        // When: Calling keepclassmembers() with serialization members
        task.keepclassmembers("class * implements java.io.Serializable { private static final long serialVersionUID; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works in Android context.
     */
    @Test
    public void testKeepClassMembers_androidContext() throws Exception {
        // Given: An Android-specific specification

        // When: Calling keepclassmembers() with Android class
        task.keepclassmembers("class * extends android.view.View { public <init>(android.content.Context); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with reflection-accessed members.
     */
    @Test
    public void testKeepClassMembers_reflectionAccessed() throws Exception {
        // Given: A reflection-accessed member specification

        // When: Calling keepclassmembers() with reflection-accessible members
        task.keepclassmembers("class com.example.Model { <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with method wildcards.
     */
    @Test
    public void testKeepClassMembers_methodWildcards() throws Exception {
        // Given: A method wildcard specification

        // When: Calling keepclassmembers() with method wildcards
        task.keepclassmembers("class com.example.MyClass { <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() can be mixed with other configuration methods.
     */
    @Test
    public void testKeepClassMembers_mixedWithOtherConfiguration() throws Exception {
        // Given: Other configuration is present
        task.dontobfuscate();

        // When: Calling keepclassmembers()
        task.keepclassmembers("class com.example.MyClass { void myMethod(); }");

        // Then: Both configurations should be present
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() accumulates across multiple calls.
     */
    @Test
    public void testKeepClassMembers_accumulation() throws Exception {
        // When: Calling keepclassmembers() multiple times with different specs
        task.keepclassmembers("class com.example.ClassA { void methodA(); }");
        task.keepclassmembers("class com.example.ClassB { void methodB(); }");
        task.keepclassmembers("class com.example.ClassC { void methodC(); }");
        task.keepclassmembers("class com.example.ClassD { void methodD(); }");
        task.keepclassmembers("class com.example.ClassE { void methodE(); }");

        // Then: All specifications should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepclassmembers() works with native methods.
     */
    @Test
    public void testKeepClassMembers_nativeMethods() throws Exception {
        // Given: A native method specification

        // When: Calling keepclassmembers() with native method
        task.keepclassmembers("class com.example.NativeLib { native void processData(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with synchronized methods.
     */
    @Test
    public void testKeepClassMembers_synchronizedMethods() throws Exception {
        // Given: A synchronized method specification

        // When: Calling keepclassmembers() with synchronized method
        task.keepclassmembers("class com.example.ThreadSafe { synchronized void update(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with volatile fields.
     */
    @Test
    public void testKeepClassMembers_volatileFields() throws Exception {
        // Given: A volatile field specification

        // When: Calling keepclassmembers() with volatile field
        task.keepclassmembers("class com.example.ThreadSafe { volatile boolean flag; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with transient fields.
     */
    @Test
    public void testKeepClassMembers_transientFields() throws Exception {
        // Given: A transient field specification

        // When: Calling keepclassmembers() with transient field
        task.keepclassmembers("class com.example.Serializable { transient int cache; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() works with R class fields.
     */
    @Test
    public void testKeepClassMembers_androidRClass() throws Exception {
        // Given: An Android R class specification

        // When: Calling keepclassmembers() with R class
        task.keepclassmembers("class **.R$* { public static <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers() preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepClassMembers_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Both tasks call keepclassmembers() with the same spec
        task.keepclassmembers("class com.example.MyClass { void myMethod(); }");
        task2.keepclassmembers("class com.example.MyClass { void myMethod(); }");

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    // ========================================================================
    // Tests for keepclassmembers(Map, String) variant
    // ========================================================================

    /**
     * Tests that keepclassmembers(Map, String) with null map works like keepclassmembers(String).
     */
    @Test
    public void testKeepClassMembersWithMap_nullMap_behavesLikeStringOnly() throws Exception {
        // When: Calling keepclassmembers() with null map
        task.keepclassmembers(null, "class MyClass { void myMethod(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) with empty map adds specification.
     */
    @Test
    public void testKeepClassMembersWithMap_emptyMap_addsSpecification() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with empty map
        task.keepclassmembers(args, "class MyClass { void myMethod(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) can be called multiple times.
     */
    @Test
    public void testKeepClassMembersWithMap_multipleCalls_accumulatesRules() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() multiple times
        task.keepclassmembers(args, "class FirstClass { void method1(); }");
        task.keepclassmembers(args, "class SecondClass { int field; }");
        task.keepclassmembers(args, "class ThirdClass { <init>(); }");

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with conditional "if" clause.
     */
    @Test
    public void testKeepClassMembersWithMap_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepclassmembers() with conditional
        task.keepclassmembers(args, "class com.example.MyClass { void callback(); }");

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works independently of injars.
     */
    @Test
    public void testKeepClassMembersWithMap_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args, "class MyClass { void myMethod(); }");

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works in a complex workflow.
     */
    @Test
    public void testKeepClassMembersWithMap_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args, "class com.example.Model { <fields>; }");

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) with different maps accumulates.
     */
    @Test
    public void testKeepClassMembersWithMap_differentMaps_accumulates() throws Exception {
        // Given: Different maps for different rules
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");

        // When: Calling keepclassmembers() with different maps
        task.keepclassmembers(args1, "class FirstClass { void method1(); }");
        task.keepclassmembers(args2, "class SecondClass { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with serialization members.
     */
    @Test
    public void testKeepClassMembersWithMap_serialization() throws Exception {
        // Given: Map for serialization rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for serialization
        task.keepclassmembers(args, "class * implements java.io.Serializable { private static final long serialVersionUID; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with reflection-accessed members.
     */
    @Test
    public void testKeepClassMembersWithMap_reflectionAccessed() throws Exception {
        // Given: Map for reflection rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for reflection-accessed members
        task.keepclassmembers(args, "class com.example.Model { <fields>; <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with Android lifecycle methods.
     */
    @Test
    public void testKeepClassMembersWithMap_androidLifecycle() throws Exception {
        // Given: Map for Android lifecycle
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for Android lifecycle
        task.keepclassmembers(args, "class * extends android.app.Activity { void onCreate(android.os.Bundle); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with native methods.
     */
    @Test
    public void testKeepClassMembersWithMap_nativeMethods() throws Exception {
        // Given: Map for native methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for native methods
        task.keepclassmembers(args, "class com.example.NativeLib { native <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with annotated members.
     */
    @Test
    public void testKeepClassMembersWithMap_annotatedMembers() throws Exception {
        // Given: Map for annotated members
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for annotated members
        task.keepclassmembers(args, "class * { @com.example.Inject *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with JavaScript interface.
     */
    @Test
    public void testKeepClassMembersWithMap_javascriptInterface() throws Exception {
        // Given: Map for JavaScript interface
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for JavaScript interface
        task.keepclassmembers(args, "class * { @android.webkit.JavascriptInterface <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with enum members.
     */
    @Test
    public void testKeepClassMembersWithMap_enumMembers() throws Exception {
        // Given: Map for enum members
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for enum
        task.keepclassmembers(args, "enum * { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with parcelable creator.
     */
    @Test
    public void testKeepClassMembersWithMap_parcelableCreator() throws Exception {
        // Given: Map for Parcelable CREATOR
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for Parcelable
        task.keepclassmembers(args, "class * implements android.os.Parcelable { public static final android.os.Parcelable$Creator CREATOR; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) can interleave with keepclassmembers(String).
     */
    @Test
    public void testKeepClassMembersWithMap_interleaveWithStringVariant() throws Exception {
        // Given: Using both variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepclassmembers("class First { void method1(); }");
        task.keepclassmembers(args, "class Second { void method2(); }");
        task.keepclassmembers("class Third { void method3(); }");
        task.keepclassmembers(args, "class Fourth { void method4(); }");

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with wildcard return types.
     */
    @Test
    public void testKeepClassMembersWithMap_wildcardReturnTypes() throws Exception {
        // Given: Map for wildcard return types
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with wildcard return type
        task.keepclassmembers(args, "class com.example.MyClass { *** get*(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with setter methods.
     */
    @Test
    public void testKeepClassMembersWithMap_setterMethods() throws Exception {
        // Given: Map for setter methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for setters
        task.keepclassmembers(args, "class com.example.MyClass { void set*(...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with getter methods.
     */
    @Test
    public void testKeepClassMembersWithMap_getterMethods() throws Exception {
        // Given: Map for getter methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for getters
        task.keepclassmembers(args, "class com.example.MyClass { *** get*(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with callback interfaces.
     */
    @Test
    public void testKeepClassMembersWithMap_callbackInterfaces() throws Exception {
        // Given: Map for callback interfaces
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for callbacks
        task.keepclassmembers(args, "interface * { void on*(...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with view constructors.
     */
    @Test
    public void testKeepClassMembersWithMap_viewConstructors() throws Exception {
        // Given: Map for view constructors
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for view constructors
        task.keepclassmembers(args, "class * extends android.view.View { public <init>(android.content.Context); public <init>(android.content.Context, android.util.AttributeSet); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with R class resource fields.
     */
    @Test
    public void testKeepClassMembersWithMap_rClassFields() throws Exception {
        // Given: Map for R class fields
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for R class
        task.keepclassmembers(args, "class **.R$* { public static <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with conditional compilation.
     */
    @Test
    public void testKeepClassMembersWithMap_conditionalCompilation() throws Exception {
        // Given: Map with conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.FeatureEnabled");

        // When: Calling keepclassmembers() with conditional
        task.keepclassmembers(args, "class com.example.FeatureClass { void featureMethod(); }");

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) accumulates multiple conditionals.
     */
    @Test
    public void testKeepClassMembersWithMap_multipleConditionals_accumulates() throws Exception {
        // Given: Multiple maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");

        // When: Calling keepclassmembers() with different conditionals
        task.keepclassmembers(args1, "class First { void method1(); }");
        task.keepclassmembers(args2, "class Second { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with synchronized members.
     */
    @Test
    public void testKeepClassMembersWithMap_synchronizedMembers() throws Exception {
        // Given: Map for synchronized members
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for synchronized members
        task.keepclassmembers(args, "class com.example.ThreadSafe { synchronized <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with volatile fields.
     */
    @Test
    public void testKeepClassMembersWithMap_volatileFields() throws Exception {
        // Given: Map for volatile fields
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for volatile fields
        task.keepclassmembers(args, "class com.example.ThreadSafe { volatile <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) integrates with other configuration.
     */
    @Test
    public void testKeepClassMembersWithMap_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args, "class com.example.MyClass { void myMethod(); }");

        // Then: All configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) handles large accumulation.
     */
    @Test
    public void testKeepClassMembersWithMap_largeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() many times
        for (int i = 0; i < 10; i++) {
            task.keepclassmembers(args, "class Class" + i + " { void method" + i + "(); }");
        }

        // Then: All should be accumulated
        assertEquals(10, task.configuration.keep.size(),
                     "Keep list should contain 10 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, String) works with public API pattern.
     */
    @Test
    public void testKeepClassMembersWithMap_publicApiPattern() throws Exception {
        // Given: Map for public API
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() for public API
        task.keepclassmembers(args, "class com.example.api.** { public <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, String) preserves consistent behavior.
     */
    @Test
    public void testKeepClassMembersWithMap_consistentBehavior() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard3", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepclassmembers() with map
        task.keepclassmembers(args, "class MyClass { void myMethod(); }");
        task2.keepclassmembers(args, "class MyClass { void myMethod(); }");

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    // ========================================================================
    // Tests for keepclassmembers(Map) variant (Map-only, no String or Closure)
    // ========================================================================

    /**
     * Tests that keepclassmembers(Map) with empty map adds specification.
     */
    @Test
    public void testKeepClassMembersMapOnly_emptyMap_addsSpecification() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map only
        task.keepclassmembers(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) can be called multiple times.
     */
    @Test
    public void testKeepClassMembersMapOnly_multipleCalls_accumulatesRules() throws Exception {
        // Given: Maps for different rules
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keepclassmembers() multiple times
        task.keepclassmembers(args1);
        task.keepclassmembers(args2);
        task.keepclassmembers(args3);

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) internally calls keepclassmembers(Map, Closure) with null.
     */
    @Test
    public void testKeepClassMembersMapOnly_callsMapClosureVariant() throws Exception {
        // Given: Map for the rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers(Map)
        task.keepclassmembers(args);

        // Then: Should behave like calling keepclassmembers(Map, null Closure)
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) works with conditional "if" clause.
     */
    @Test
    public void testKeepClassMembersMapOnly_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepclassmembers() with conditional
        task.keepclassmembers(args);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) can interleave with other variants.
     */
    @Test
    public void testKeepClassMembersMapOnly_interleaveWithOtherVariants() throws Exception {
        // Given: Using different variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving all variants
        task.keepclassmembers("class First { void method1(); }");
        task.keepclassmembers(args);
        task.keepclassmembers(args, "class Third { void method3(); }");

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) works independently of injars.
     */
    @Test
    public void testKeepClassMembersMapOnly_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) works in a complex workflow.
     */
    @Test
    public void testKeepClassMembersMapOnly_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");
        task.dontobfuscate();

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) with different conditionals accumulates.
     */
    @Test
    public void testKeepClassMembersMapOnly_differentConditionals_accumulates() throws Exception {
        // Given: Different maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("if", "class com.example.Condition3");

        // When: Calling keepclassmembers() with different conditionals
        task.keepclassmembers(args1);
        task.keepclassmembers(args2);
        task.keepclassmembers(args3);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) works with null map.
     */
    @Test
    public void testKeepClassMembersMapOnly_nullMap_addsSpecification() throws Exception {
        // When: Calling keepclassmembers() with null map
        task.keepclassmembers((java.util.Map<String, Object>) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) integrates with other configuration methods.
     */
    @Test
    public void testKeepClassMembersMapOnly_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        task.dontoptimize();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args);

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
     * Tests that keepclassmembers(Map) handles large accumulation.
     */
    @Test
    public void testKeepClassMembersMapOnly_largeAccumulation() throws Exception {
        // When: Calling keepclassmembers() many times
        for (int i = 0; i < 15; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            task.keepclassmembers(args);
        }

        // Then: All should be accumulated
        assertEquals(15, task.configuration.keep.size(),
                     "Keep list should contain 15 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) works with mixed empty and conditional maps.
     */
    @Test
    public void testKeepClassMembersMapOnly_mixedEmptyAndConditional() throws Exception {
        // Given: Mix of empty and conditional maps
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with mixed maps
        task.keepclassmembers(args1);
        task.keepclassmembers(args2);
        task.keepclassmembers(args3);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepClassMembersMapOnly_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard4", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepclassmembers() with map
        task.keepclassmembers(args);
        task2.keepclassmembers(args);

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) can be used after String variant.
     */
    @Test
    public void testKeepClassMembersMapOnly_afterStringVariant() throws Exception {
        // Given: String variant is called first
        task.keepclassmembers("class First { void method1(); }");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers(Map)
        task.keepclassmembers(args);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) can be used before String variant.
     */
    @Test
    public void testKeepClassMembersMapOnly_beforeStringVariant() throws Exception {
        // Given: Map variant is called first
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclassmembers(args);

        // When: Calling keepclassmembers(String)
        task.keepclassmembers("class Second { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) can be used after Map-String variant.
     */
    @Test
    public void testKeepClassMembersMapOnly_afterMapStringVariant() throws Exception {
        // Given: Map-String variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclassmembers(args1, "class First { void method1(); }");

        // When: Calling keepclassmembers(Map)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclassmembers(args2);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) can be used before Map-String variant.
     */
    @Test
    public void testKeepClassMembersMapOnly_beforeMapStringVariant() throws Exception {
        // Given: Map variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclassmembers(args1);

        // When: Calling keepclassmembers(Map, String)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclassmembers(args2, "class Second { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) works with realistic Android scenario.
     */
    @Test
    public void testKeepClassMembersMapOnly_androidScenario() throws Exception {
        // Given: Android-like configuration
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("build/intermediates/classes.jar");
        task.outjars("build/outputs/proguard.jar");
        task.dontpreverify();

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertTrue(task.configuration.preverify == false,
                   "Preverification should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) works with multiple conditional rules.
     */
    @Test
    public void testKeepClassMembersMapOnly_multipleConditions() throws Exception {
        // Given: Multiple conditional maps
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.FeatureA");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.FeatureB");

        // When: Calling keepclassmembers() with conditionals
        task.keepclassmembers(args1);
        task.keepclassmembers(args2);

        // Then: Both conditionals should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) handles sequential calls properly.
     */
    @Test
    public void testKeepClassMembersMapOnly_sequentialCalls() throws Exception {
        // When: Calling keepclassmembers() sequentially
        for (int i = 0; i < 5; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            task.keepclassmembers(args);
        }

        // Then: All calls should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) works with parallel-style initialization.
     */
    @Test
    public void testKeepClassMembersMapOnly_parallelStyleInit() throws Exception {
        // Given: Multiple maps prepared in advance
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with all maps
        task.keepclassmembers(args1);
        task.keepclassmembers(args2);
        task.keepclassmembers(args3);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembers(Map) integrates with optimization configuration.
     */
    @Test
    public void testKeepClassMembersMapOnly_withOptimizationConfig() throws Exception {
        // Given: Optimization configured
        task.optimizationpasses(5);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args);

        // Then: Both configurations should coexist
        assertEquals(5, task.configuration.optimizationPasses,
                     "Optimization passes should be 5");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) integrates with obfuscation dictionary.
     */
    @Test
    public void testKeepClassMembersMapOnly_withObfuscationDictionary() throws Exception {
        // Given: Obfuscation dictionary configured
        task.obfuscationdictionary("dictionary.txt");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.obfuscationDictionary,
                      "Obfuscation dictionary should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) works with verbose configuration.
     */
    @Test
    public void testKeepClassMembersMapOnly_withVerboseConfig() throws Exception {
        // Given: Verbose mode enabled
        task.verbose();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args);

        // Then: Both configurations should coexist
        assertTrue(task.configuration.verbose,
                   "Verbose mode should be enabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) works with note configuration.
     */
    @Test
    public void testKeepClassMembersMapOnly_withNoteConfig() throws Exception {
        // Given: Note configuration
        task.dontnote("com.example.**");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.note,
                      "Note configuration should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) works with warn configuration.
     */
    @Test
    public void testKeepClassMembersMapOnly_withWarnConfig() throws Exception {
        // Given: Warn configuration
        task.dontwarn("com.example.**");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map
        task.keepclassmembers(args);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.warn,
                      "Warn configuration should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map) preserves order of accumulation.
     */
    @Test
    public void testKeepClassMembersMapOnly_preservesOrder() throws Exception {
        // When: Calling keepclassmembers() multiple times
        task.keepclassmembers("class First { void method1(); }");
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclassmembers(args);
        task.keepclassmembers("class Third { void method3(); }");

        // Then: All should be accumulated in order
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications in order");
    }

    // ========================================================================
    // Tests for keepclassmembers(Map, Closure) variant (Map with Closure for member specs)
    // ========================================================================

    /**
     * Tests that keepclassmembers(Map, Closure) with null closure adds specification.
     */
    @Test
    public void testKeepClassMembersMapClosure_nullClosure_addsSpecification() throws Exception {
        // Given: Map with args and null closure
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with null closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) can be called multiple times.
     */
    @Test
    public void testKeepClassMembersMapClosure_multipleCalls_accumulatesRules() throws Exception {
        // Given: Maps with null closures
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keepclassmembers() multiple times
        task.keepclassmembers(args1, (groovy.lang.Closure) null);
        task.keepclassmembers(args2, (groovy.lang.Closure) null);
        task.keepclassmembers(args3, (groovy.lang.Closure) null);

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) is the base method for Map-only variant.
     */
    @Test
    public void testKeepClassMembersMapClosure_isBaseForMapOnlyVariant() throws Exception {
        // Given: Map-only variant calls this method internally
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling both variants
        task.keepclassmembers(args); // Map-only variant
        task.keepclassmembers(args, (groovy.lang.Closure) null); // Map-Closure variant

        // Then: Both should produce same result
        assertEquals(2, task.configuration.keep.size(),
                     "Both variants should add specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) works with conditional "if" clause.
     */
    @Test
    public void testKeepClassMembersMapClosure_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepclassmembers() with conditional and null closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) can interleave with all other variants.
     */
    @Test
    public void testKeepClassMembersMapClosure_interleaveWithAllVariants() throws Exception {
        // Given: Using all four variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving all variants
        task.keepclassmembers("class First { void method1(); }");
        task.keepclassmembers(args, "class Second { void method2(); }");
        task.keepclassmembers(args);
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) works independently of injars.
     */
    @Test
    public void testKeepClassMembersMapClosure_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map and closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) works in a complex workflow.
     */
    @Test
    public void testKeepClassMembersMapClosure_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");
        task.dontobfuscate();
        task.dontshrink();

        // When: Calling keepclassmembers() with map and closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

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
     * Tests that keepclassmembers(Map, Closure) with different conditionals accumulates.
     */
    @Test
    public void testKeepClassMembersMapClosure_differentConditionals_accumulates() throws Exception {
        // Given: Different maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("if", "class com.example.Condition3");

        // When: Calling keepclassmembers() with different conditionals
        task.keepclassmembers(args1, (groovy.lang.Closure) null);
        task.keepclassmembers(args2, (groovy.lang.Closure) null);
        task.keepclassmembers(args3, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) works with null map and null closure.
     */
    @Test
    public void testKeepClassMembersMapClosure_nullMapNullClosure_addsSpecification() throws Exception {
        // When: Calling keepclassmembers() with null map and null closure
        task.keepclassmembers((java.util.Map<String, Object>) null, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) integrates with other configuration methods.
     */
    @Test
    public void testKeepClassMembersMapClosure_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        task.dontoptimize();
        task.dontpreverify();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map and closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

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
     * Tests that keepclassmembers(Map, Closure) handles large accumulation.
     */
    @Test
    public void testKeepClassMembersMapClosure_largeAccumulation() throws Exception {
        // When: Calling keepclassmembers() many times
        for (int i = 0; i < 20; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            task.keepclassmembers(args, (groovy.lang.Closure) null);
        }

        // Then: All should be accumulated
        assertEquals(20, task.configuration.keep.size(),
                     "Keep list should contain 20 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) works with mixed empty and conditional maps.
     */
    @Test
    public void testKeepClassMembersMapClosure_mixedEmptyAndConditional() throws Exception {
        // Given: Mix of empty and conditional maps
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with mixed maps
        task.keepclassmembers(args1, (groovy.lang.Closure) null);
        task.keepclassmembers(args2, (groovy.lang.Closure) null);
        task.keepclassmembers(args3, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepClassMembersMapClosure_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard5", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepclassmembers() with map and closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);
        task2.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) can be used after String variant.
     */
    @Test
    public void testKeepClassMembersMapClosure_afterStringVariant() throws Exception {
        // Given: String variant is called first
        task.keepclassmembers("class First { void method1(); }");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers(Map, Closure)
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) can be used before String variant.
     */
    @Test
    public void testKeepClassMembersMapClosure_beforeStringVariant() throws Exception {
        // Given: Map-Closure variant is called first
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // When: Calling keepclassmembers(String)
        task.keepclassmembers("class Second { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) can be used after Map-String variant.
     */
    @Test
    public void testKeepClassMembersMapClosure_afterMapStringVariant() throws Exception {
        // Given: Map-String variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclassmembers(args1, "class First { void method1(); }");

        // When: Calling keepclassmembers(Map, Closure)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclassmembers(args2, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) can be used before Map-String variant.
     */
    @Test
    public void testKeepClassMembersMapClosure_beforeMapStringVariant() throws Exception {
        // Given: Map-Closure variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclassmembers(args1, (groovy.lang.Closure) null);

        // When: Calling keepclassmembers(Map, String)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclassmembers(args2, "class Second { void method2(); }");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) can be used after Map-only variant.
     */
    @Test
    public void testKeepClassMembersMapClosure_afterMapOnlyVariant() throws Exception {
        // Given: Map-only variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclassmembers(args1);

        // When: Calling keepclassmembers(Map, Closure)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclassmembers(args2, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) can be used before Map-only variant.
     */
    @Test
    public void testKeepClassMembersMapClosure_beforeMapOnlyVariant() throws Exception {
        // Given: Map-Closure variant is called first
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclassmembers(args1, (groovy.lang.Closure) null);

        // When: Calling keepclassmembers(Map)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclassmembers(args2);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) works with realistic Android scenario.
     */
    @Test
    public void testKeepClassMembersMapClosure_androidScenario() throws Exception {
        // Given: Android-like configuration
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("build/intermediates/classes.jar");
        task.outjars("build/outputs/proguard.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");
        task.dontpreverify();
        task.dontoptimize();

        // When: Calling keepclassmembers() with map and closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

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
     * Tests that keepclassmembers(Map, Closure) works with multiple conditional rules.
     */
    @Test
    public void testKeepClassMembersMapClosure_multipleConditions() throws Exception {
        // Given: Multiple conditional maps
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.FeatureA");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.FeatureB");
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        args3.put("if", "class com.example.FeatureC");

        // When: Calling keepclassmembers() with conditionals
        task.keepclassmembers(args1, (groovy.lang.Closure) null);
        task.keepclassmembers(args2, (groovy.lang.Closure) null);
        task.keepclassmembers(args3, (groovy.lang.Closure) null);

        // Then: All conditionals should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) handles sequential calls properly.
     */
    @Test
    public void testKeepClassMembersMapClosure_sequentialCalls() throws Exception {
        // When: Calling keepclassmembers() sequentially
        for (int i = 0; i < 7; i++) {
            java.util.Map<String, Object> args = new java.util.HashMap<>();
            task.keepclassmembers(args, (groovy.lang.Closure) null);
        }

        // Then: All calls should be accumulated
        assertEquals(7, task.configuration.keep.size(),
                     "Keep list should contain 7 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) works with parallel-style initialization.
     */
    @Test
    public void testKeepClassMembersMapClosure_parallelStyleInit() throws Exception {
        // Given: Multiple maps prepared in advance
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        java.util.Map<String, Object> args3 = new java.util.HashMap<>();
        java.util.Map<String, Object> args4 = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with all maps
        task.keepclassmembers(args1, (groovy.lang.Closure) null);
        task.keepclassmembers(args2, (groovy.lang.Closure) null);
        task.keepclassmembers(args3, (groovy.lang.Closure) null);
        task.keepclassmembers(args4, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) integrates with optimization configuration.
     */
    @Test
    public void testKeepClassMembersMapClosure_withOptimizationConfig() throws Exception {
        // Given: Optimization configured
        task.optimizationpasses(7);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map and closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: Both configurations should coexist
        assertEquals(7, task.configuration.optimizationPasses,
                     "Optimization passes should be 7");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) integrates with obfuscation dictionary.
     */
    @Test
    public void testKeepClassMembersMapClosure_withObfuscationDictionary() throws Exception {
        // Given: Obfuscation dictionary configured
        task.obfuscationdictionary("dictionary.txt");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map and closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.obfuscationDictionary,
                      "Obfuscation dictionary should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) works with verbose configuration.
     */
    @Test
    public void testKeepClassMembersMapClosure_withVerboseConfig() throws Exception {
        // Given: Verbose mode enabled
        task.verbose();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map and closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: Both configurations should coexist
        assertTrue(task.configuration.verbose,
                   "Verbose mode should be enabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) works with note configuration.
     */
    @Test
    public void testKeepClassMembersMapClosure_withNoteConfig() throws Exception {
        // Given: Note configuration
        task.dontnote("com.example.**");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map and closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.note,
                      "Note configuration should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) works with warn configuration.
     */
    @Test
    public void testKeepClassMembersMapClosure_withWarnConfig() throws Exception {
        // Given: Warn configuration
        task.dontwarn("com.example.**");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with map and closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: Both configurations should coexist
        assertNotNull(task.configuration.warn,
                      "Warn configuration should be set");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) preserves order of accumulation.
     */
    @Test
    public void testKeepClassMembersMapClosure_preservesOrder() throws Exception {
        // Given: Multiple variants used in sequence
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() in various ways
        task.keepclassmembers("class First { void method1(); }");
        task.keepclassmembers(args, (groovy.lang.Closure) null);
        task.keepclassmembers(args, "class Third { void method3(); }");
        task.keepclassmembers(args);

        // Then: All should be accumulated in order
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications in order");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) works with empty map and null closure.
     */
    @Test
    public void testKeepClassMembersMapClosure_emptyMapNullClosure() throws Exception {
        // Given: Empty map and null closure
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembers() with empty map and null closure
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembers(Map, Closure) as base method for all variants.
     */
    @Test
    public void testKeepClassMembersMapClosure_asBaseMethodForAllVariants() throws Exception {
        // Given: This is the base method that all variants eventually call
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Testing that all variants work through this base method
        // String variant -> Map-String variant -> this method
        task.keepclassmembers("class First { void method1(); }");
        // Map-String variant -> this method
        task.keepclassmembers(args, "class Second { void method2(); }");
        // Map-only variant -> this method with null Closure
        task.keepclassmembers(args);
        // Direct call to this method
        task.keepclassmembers(args, (groovy.lang.Closure) null);

        // Then: All should result in specifications being added
        assertEquals(4, task.configuration.keep.size(),
                     "All variants should work through the base method");
    }
}

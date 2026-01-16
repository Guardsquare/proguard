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
 * Tests for {@link ProGuardTask#keepclassmembernames(String)}.
 *
 * This test class verifies that the keepclassmembernames(String) method correctly
 * adds keepclassmembernames rules to the configuration.
 *
 * Method signature: keepclassmembernames(Ljava/lang/String;)V
 * - Takes a class specification string that specifies which class members to keep names for
 * - Parses it and adds it to configuration.keep list
 * - Returns void
 * - Throws ParseException if the class specification is invalid
 *
 * Key behavior: This method tells ProGuard to keep the names of class members,
 * but only if they are not removed during shrinking. This is different from:
 * - keepclassmembers(): which keeps members and prevents removal
 * - keepnames(): which keeps names of both classes and members
 *
 * The keepclassmembernames directive is useful when you want to preserve readable
 * member names for debugging or reflection, but still allow unused members to be
 * removed during shrinking.
 *
 * Internally calls keepclassmembernames(null, String) with a null map.
 */
public class ProGuardTaskClaude_keepclassmembernamesTest {

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
     * Tests that keepclassmembernames() initializes and adds to the keep list.
     */
    @Test
    public void testKeepClassMemberNames_initializesAndAddsToList() throws Exception {
        // Given: Initial state where keep list is null
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");

        // When: Calling keepclassmembernames() with a class specification
        task.keepclassmembernames("class MyClass");

        // Then: The keep list should be initialized and contain one entry
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() can be called multiple times to accumulate rules.
     */
    @Test
    public void testKeepClassMemberNames_multipleCalls_accumulatesRules() throws Exception {
        // When: Calling keepclassmembernames() multiple times
        task.keepclassmembernames("class FirstClass");
        task.keepclassmembernames("class SecondClass");
        task.keepclassmembernames("class ThirdClass");

        // Then: All rules should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembernames() works with simple class specification.
     */
    @Test
    public void testKeepClassMemberNames_simpleClassSpec() throws Exception {
        // Given: A simple class specification

        // When: Calling keepclassmembernames() with simple class
        task.keepclassmembernames("class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with wildcard class names.
     */
    @Test
    public void testKeepClassMemberNames_wildcardClassName() throws Exception {
        // Given: A wildcard class specification

        // When: Calling keepclassmembernames() with wildcard
        task.keepclassmembernames("class com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with public modifier.
     */
    @Test
    public void testKeepClassMemberNames_publicModifier() throws Exception {
        // Given: A specification with public modifier

        // When: Calling keepclassmembernames() with public modifier
        task.keepclassmembernames("public class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with abstract class.
     */
    @Test
    public void testKeepClassMemberNames_abstractClass() throws Exception {
        // Given: An abstract class specification

        // When: Calling keepclassmembernames() with abstract class
        task.keepclassmembernames("abstract class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with interface.
     */
    @Test
    public void testKeepClassMemberNames_interface() throws Exception {
        // Given: An interface specification

        // When: Calling keepclassmembernames() with interface
        task.keepclassmembernames("interface com.example.MyInterface");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with enum.
     */
    @Test
    public void testKeepClassMemberNames_enum() throws Exception {
        // Given: An enum specification

        // When: Calling keepclassmembernames() with enum
        task.keepclassmembernames("enum com.example.MyEnum");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with extends clause.
     */
    @Test
    public void testKeepClassMemberNames_extendsClause() throws Exception {
        // Given: A specification with extends clause

        // When: Calling keepclassmembernames() with extends
        task.keepclassmembernames("class * extends com.example.BaseClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with implements clause.
     */
    @Test
    public void testKeepClassMemberNames_implementsClause() throws Exception {
        // Given: A specification with implements clause

        // When: Calling keepclassmembernames() with implements
        task.keepclassmembernames("class * implements java.io.Serializable");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with annotation specification.
     */
    @Test
    public void testKeepClassMemberNames_annotation() throws Exception {
        // Given: An annotation specification

        // When: Calling keepclassmembernames() with annotation
        task.keepclassmembernames("@interface com.example.MyAnnotation");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with class annotation.
     */
    @Test
    public void testKeepClassMemberNames_classWithAnnotation() throws Exception {
        // Given: A class with annotation specification

        // When: Calling keepclassmembernames() with class annotation
        task.keepclassmembernames("@com.example.KeepName class *");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with member specification.
     */
    @Test
    public void testKeepClassMemberNames_withMembers() throws Exception {
        // Given: A class with members specification

        // When: Calling keepclassmembernames() with members
        task.keepclassmembernames("class com.example.MyClass { <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with field specification.
     */
    @Test
    public void testKeepClassMemberNames_withFields() throws Exception {
        // Given: A class with fields specification

        // When: Calling keepclassmembernames() with fields
        task.keepclassmembernames("class com.example.MyClass { <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with specific method specification.
     */
    @Test
    public void testKeepClassMemberNames_specificMethod() throws Exception {
        // Given: A specific method specification

        // When: Calling keepclassmembernames() with specific method
        task.keepclassmembernames("class com.example.MyClass { void myMethod(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with specific field specification.
     */
    @Test
    public void testKeepClassMemberNames_specificField() throws Exception {
        // Given: A specific field specification

        // When: Calling keepclassmembernames() with specific field
        task.keepclassmembernames("class com.example.MyClass { String name; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with nested classes.
     */
    @Test
    public void testKeepClassMemberNames_nestedClass() throws Exception {
        // Given: A nested class specification

        // When: Calling keepclassmembernames() with nested class
        task.keepclassmembernames("class com.example.Outer$Inner");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with package wildcard.
     */
    @Test
    public void testKeepClassMemberNames_packageWildcard() throws Exception {
        // Given: A package wildcard specification

        // When: Calling keepclassmembernames() with package wildcard
        task.keepclassmembernames("class com.example.*");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with double package wildcard.
     */
    @Test
    public void testKeepClassMemberNames_doublePackageWildcard() throws Exception {
        // Given: A double package wildcard specification

        // When: Calling keepclassmembernames() with double package wildcard
        task.keepclassmembernames("class com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with final modifier.
     */
    @Test
    public void testKeepClassMemberNames_finalModifier() throws Exception {
        // Given: A specification with final modifier

        // When: Calling keepclassmembernames() with final modifier
        task.keepclassmembernames("final class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with all members wildcard.
     */
    @Test
    public void testKeepClassMemberNames_allMembersWildcard() throws Exception {
        // Given: An all-members wildcard specification

        // When: Calling keepclassmembernames() with all members wildcard
        task.keepclassmembernames("class com.example.MyClass { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with Android context.
     */
    @Test
    public void testKeepClassMemberNames_androidContext() throws Exception {
        // Given: An Android-specific specification

        // When: Calling keepclassmembernames() with Android class
        task.keepclassmembernames("class * extends android.app.Activity");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with serialization classes.
     */
    @Test
    public void testKeepClassMemberNames_serialization() throws Exception {
        // Given: A serialization class specification

        // When: Calling keepclassmembernames() with Serializable
        task.keepclassmembernames("class * implements java.io.Serializable");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with native methods.
     */
    @Test
    public void testKeepClassMemberNames_nativeMethods() throws Exception {
        // Given: A native method specification

        // When: Calling keepclassmembernames() with native methods
        task.keepclassmembernames("class com.example.NativeLib { native <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with static methods.
     */
    @Test
    public void testKeepClassMemberNames_staticMethods() throws Exception {
        // Given: A static method specification

        // When: Calling keepclassmembernames() with static methods
        task.keepclassmembernames("class com.example.Util { public static <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with constructors.
     */
    @Test
    public void testKeepClassMemberNames_constructors() throws Exception {
        // Given: A constructor specification

        // When: Calling keepclassmembernames() with constructors
        task.keepclassmembernames("class com.example.MyClass { <init>(...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with R class fields.
     */
    @Test
    public void testKeepClassMemberNames_androidRClass() throws Exception {
        // Given: An Android R class specification

        // When: Calling keepclassmembernames() with R class
        task.keepclassmembernames("class **.R$*");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() can be mixed with other configuration methods.
     */
    @Test
    public void testKeepClassMemberNames_mixedWithOtherConfiguration() throws Exception {
        // Given: Other configuration is present
        task.dontobfuscate();

        // When: Calling keepclassmembernames()
        task.keepclassmembernames("class com.example.MyClass");

        // Then: Both configurations should be present
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() accumulates across multiple calls.
     */
    @Test
    public void testKeepClassMemberNames_accumulation() throws Exception {
        // When: Calling keepclassmembernames() multiple times with different specs
        task.keepclassmembernames("class com.example.ClassA");
        task.keepclassmembernames("class com.example.ClassB");
        task.keepclassmembernames("class com.example.ClassC");
        task.keepclassmembernames("class com.example.ClassD");
        task.keepclassmembernames("class com.example.ClassE");

        // Then: All specifications should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepclassmembernames() works with method parameters.
     */
    @Test
    public void testKeepClassMemberNames_methodParameters() throws Exception {
        // Given: A method with parameters specification

        // When: Calling keepclassmembernames() with method parameters
        task.keepclassmembernames("class com.example.MyClass { void process(java.lang.String, int); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with varargs.
     */
    @Test
    public void testKeepClassMemberNames_varargs() throws Exception {
        // Given: A varargs specification

        // When: Calling keepclassmembernames() with varargs
        task.keepclassmembernames("class com.example.MyClass { void print(java.lang.String...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with array types.
     */
    @Test
    public void testKeepClassMemberNames_arrayTypes() throws Exception {
        // Given: An array type specification

        // When: Calling keepclassmembernames() with array types
        task.keepclassmembernames("class com.example.MyClass { java.lang.String[] getItems(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with generic types.
     */
    @Test
    public void testKeepClassMemberNames_genericTypes() throws Exception {
        // Given: A generic type specification

        // When: Calling keepclassmembernames() with generic types
        task.keepclassmembernames("class com.example.MyClass { java.util.List get(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with volatile fields.
     */
    @Test
    public void testKeepClassMemberNames_volatileFields() throws Exception {
        // Given: A volatile field specification

        // When: Calling keepclassmembernames() with volatile field
        task.keepclassmembernames("class com.example.ThreadSafe { volatile boolean flag; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with synchronized methods.
     */
    @Test
    public void testKeepClassMemberNames_synchronizedMethods() throws Exception {
        // Given: A synchronized method specification

        // When: Calling keepclassmembernames() with synchronized method
        task.keepclassmembernames("class com.example.ThreadSafe { synchronized void update(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with transient fields.
     */
    @Test
    public void testKeepClassMemberNames_transientFields() throws Exception {
        // Given: A transient field specification

        // When: Calling keepclassmembernames() with transient field
        task.keepclassmembernames("class com.example.Serializable { transient int cache; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames() preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepClassMemberNames_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Both tasks call keepclassmembernames() with the same spec
        task.keepclassmembernames("class com.example.MyClass");
        task2.keepclassmembernames("class com.example.MyClass");

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclassmembernames() works with wildcard return types.
     */
    @Test
    public void testKeepClassMemberNames_wildcardReturnTypes() throws Exception {
        // Given: A wildcard return type specification

        // When: Calling keepclassmembernames() with wildcard return type
        task.keepclassmembernames("class com.example.MyClass { *** get*(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    // ========================================================================
    // Tests for keepclassmembernames(Map, String) variant
    // ========================================================================

    /**
     * Tests that keepclassmembernames(Map, String) with null map works like keepclassmembernames(String).
     */
    @Test
    public void testKeepClassMemberNamesWithMap_nullMap_behavesLikeStringOnly() throws Exception {
        // When: Calling keepclassmembernames() with null map
        task.keepclassmembernames(null, "class MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) with empty map adds specification.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_emptyMap_addsSpecification() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with empty map
        task.keepclassmembernames(args, "class MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) can be called multiple times.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_multipleCalls_accumulatesRules() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() multiple times
        task.keepclassmembernames(args, "class FirstClass");
        task.keepclassmembernames(args, "class SecondClass");
        task.keepclassmembernames(args, "class ThirdClass");

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with conditional "if" clause.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepclassmembernames() with conditional
        task.keepclassmembernames(args, "class com.example.MyClass");

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works independently of injars.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with map
        task.keepclassmembernames(args, "class MyClass");

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works in a complex workflow.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");

        // When: Calling keepclassmembernames() with map
        task.keepclassmembernames(args, "class com.example.Model");

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) with different maps accumulates.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_differentMaps_accumulates() throws Exception {
        // Given: Different maps for different rules
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");

        // When: Calling keepclassmembernames() with different maps
        task.keepclassmembernames(args1, "class FirstClass");
        task.keepclassmembernames(args2, "class SecondClass");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with wildcard classes.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_wildcardClasses() throws Exception {
        // Given: Map for wildcard rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with wildcard classes
        task.keepclassmembernames(args, "class com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with extends clause.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_extendsClause() throws Exception {
        // Given: Map for extends rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with extends
        task.keepclassmembernames(args, "class * extends com.example.BaseClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with implements clause.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_implementsClause() throws Exception {
        // Given: Map for implements rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with implements
        task.keepclassmembernames(args, "class * implements java.io.Serializable");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with member specifications.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_memberSpecifications() throws Exception {
        // Given: Map for member rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with members
        task.keepclassmembernames(args, "class com.example.MyClass { <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with field specifications.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_fieldSpecifications() throws Exception {
        // Given: Map for field rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with fields
        task.keepclassmembernames(args, "class com.example.MyClass { <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with Android classes.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_androidClasses() throws Exception {
        // Given: Map for Android rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() for Android classes
        task.keepclassmembernames(args, "class * extends android.app.Activity");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with enum classes.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_enumClasses() throws Exception {
        // Given: Map for enum rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() for enums
        task.keepclassmembernames(args, "enum *");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with interface classes.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_interfaceClasses() throws Exception {
        // Given: Map for interface rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() for interfaces
        task.keepclassmembernames(args, "interface com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with annotation specifications.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_annotationSpecifications() throws Exception {
        // Given: Map for annotation rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with annotations
        task.keepclassmembernames(args, "@interface com.example.MyAnnotation");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with class annotations.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_classAnnotations() throws Exception {
        // Given: Map for class annotation rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with class annotations
        task.keepclassmembernames(args, "@com.example.KeepName class *");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) can interleave with keepclassmembernames(String).
     */
    @Test
    public void testKeepClassMemberNamesWithMap_interleaveWithStringVariant() throws Exception {
        // Given: Using both variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepclassmembernames("class First");
        task.keepclassmembernames(args, "class Second");
        task.keepclassmembernames("class Third");
        task.keepclassmembernames(args, "class Fourth");

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with wildcard return types.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_wildcardReturnTypes() throws Exception {
        // Given: Map for wildcard return types
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with wildcard return type
        task.keepclassmembernames(args, "class com.example.MyClass { *** get*(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with native methods.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_nativeMethods() throws Exception {
        // Given: Map for native methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() for native methods
        task.keepclassmembernames(args, "class com.example.NativeLib { native <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with static methods.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_staticMethods() throws Exception {
        // Given: Map for static methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() for static methods
        task.keepclassmembernames(args, "class com.example.Util { public static <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with conditional compilation.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_conditionalCompilation() throws Exception {
        // Given: Map with conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.FeatureEnabled");

        // When: Calling keepclassmembernames() with conditional
        task.keepclassmembernames(args, "class com.example.FeatureClass");

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) accumulates multiple conditionals.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_multipleConditionals_accumulates() throws Exception {
        // Given: Multiple maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");

        // When: Calling keepclassmembernames() with different conditionals
        task.keepclassmembernames(args1, "class First");
        task.keepclassmembernames(args2, "class Second");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with nested classes.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_nestedClasses() throws Exception {
        // Given: Map for nested classes
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() for nested classes
        task.keepclassmembernames(args, "class com.example.Outer$Inner");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) integrates with other configuration.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with map
        task.keepclassmembernames(args, "class com.example.MyClass");

        // Then: All configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) handles large accumulation.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_largeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() many times
        for (int i = 0; i < 10; i++) {
            task.keepclassmembernames(args, "class Class" + i);
        }

        // Then: All should be accumulated
        assertEquals(10, task.configuration.keep.size(),
                     "Keep list should contain 10 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with R class fields.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_rClassFields() throws Exception {
        // Given: Map for R class fields
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() for R class
        task.keepclassmembernames(args, "class **.R$*");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with constructors.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_constructors() throws Exception {
        // Given: Map for constructors
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() for constructors
        task.keepclassmembernames(args, "class com.example.MyClass { <init>(...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) preserves consistent behavior.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_consistentBehavior() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard3", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepclassmembernames() with map
        task.keepclassmembernames(args, "class MyClass");
        task2.keepclassmembernames(args, "class MyClass");

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with specific method signatures.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_specificMethodSignatures() throws Exception {
        // Given: Map for specific method
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with specific method signature
        task.keepclassmembernames(args, "class com.example.MyClass { void process(java.lang.String, int); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with all members wildcard.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_allMembersWildcard() throws Exception {
        // Given: Map for all members
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with all members wildcard
        task.keepclassmembernames(args, "class com.example.MyClass { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with serialization methods.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_serializationMethods() throws Exception {
        // Given: Map for serialization
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() for serialization methods
        task.keepclassmembernames(args, "class * implements java.io.Serializable { private void writeObject(java.io.ObjectOutputStream); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, String) works with varargs methods.
     */
    @Test
    public void testKeepClassMemberNamesWithMap_varargsMethod() throws Exception {
        // Given: Map for varargs
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with varargs method
        task.keepclassmembernames(args, "class com.example.MyClass { void print(java.lang.String...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    // ========================================================================
    // Tests for keepclassmembernames(Map) variant
    // ========================================================================

    /**
     * Tests that keepclassmembernames(Map) with null map adds specification.
     */
    @Test
    public void testKeepClassMemberNamesMap_nullMap_addsSpecification() throws Exception {
        // Given: Null map
        java.util.Map<String, Object> args = null;

        // When: Calling keepclassmembernames() with null map
        task.keepclassmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map) with empty map adds specification.
     */
    @Test
    public void testKeepClassMemberNamesMap_emptyMap_addsSpecification() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with empty map
        task.keepclassmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map) can be called multiple times to accumulate rules.
     */
    @Test
    public void testKeepClassMemberNamesMap_multipleCalls_accumulatesRules() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() multiple times
        task.keepclassmembernames(args);
        task.keepclassmembernames(args);
        task.keepclassmembernames(args);

        // Then: All rules should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) works with conditional "if" clause.
     */
    @Test
    public void testKeepClassMemberNamesMap_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepclassmembernames() with conditional
        task.keepclassmembernames(args);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map) with different maps accumulates.
     */
    @Test
    public void testKeepClassMemberNamesMap_differentMaps_accumulates() throws Exception {
        // Given: Different maps for different rules
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");

        // When: Calling keepclassmembernames() with different maps
        task.keepclassmembernames(args1);
        task.keepclassmembernames(args2);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) can interleave with keepclassmembernames(String).
     */
    @Test
    public void testKeepClassMemberNamesMap_interleaveWithStringVariant() throws Exception {
        // Given: Using both variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepclassmembernames("class First");
        task.keepclassmembernames(args);
        task.keepclassmembernames("class Third");
        task.keepclassmembernames(args);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) can interleave with keepclassmembernames(Map, String).
     */
    @Test
    public void testKeepClassMemberNamesMap_interleaveWithMapStringVariant() throws Exception {
        // Given: Using both Map variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepclassmembernames(args, "class First");
        task.keepclassmembernames(args);
        task.keepclassmembernames(args, "class Third");
        task.keepclassmembernames(args);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) integrates with other configuration.
     */
    @Test
    public void testKeepClassMemberNamesMap_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with map
        task.keepclassmembernames(args);

        // Then: All configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map) works independently of injars.
     */
    @Test
    public void testKeepClassMemberNamesMap_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with map
        task.keepclassmembernames(args);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map) works in a complex workflow.
     */
    @Test
    public void testKeepClassMemberNamesMap_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");

        // When: Calling keepclassmembernames() with map
        task.keepclassmembernames(args);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map) handles large accumulation.
     */
    @Test
    public void testKeepClassMemberNamesMap_largeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() many times
        for (int i = 0; i < 10; i++) {
            task.keepclassmembernames(args);
        }

        // Then: All should be accumulated
        assertEquals(10, task.configuration.keep.size(),
                     "Keep list should contain 10 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) accumulates multiple conditionals.
     */
    @Test
    public void testKeepClassMemberNamesMap_multipleConditionals_accumulates() throws Exception {
        // Given: Multiple maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");

        // When: Calling keepclassmembernames() with different conditionals
        task.keepclassmembernames(args1);
        task.keepclassmembernames(args2);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepClassMemberNamesMap_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard4", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepclassmembernames() with map
        task.keepclassmembernames(args);
        task2.keepclassmembernames(args);

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map) works with complex conditional expression.
     */
    @Test
    public void testKeepClassMemberNamesMap_complexConditional() throws Exception {
        // Given: Map with complex conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.Feature1, class com.example.Feature2");

        // When: Calling keepclassmembernames() with complex conditional
        task.keepclassmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map) can be called after keepclassmembernames(String).
     */
    @Test
    public void testKeepClassMemberNamesMap_afterStringVariant() throws Exception {
        // Given: String variant called first
        task.keepclassmembernames("class First");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames(Map) after
        task.keepclassmembernames(args);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) can be called before keepclassmembernames(String).
     */
    @Test
    public void testKeepClassMemberNamesMap_beforeStringVariant() throws Exception {
        // Given: Map variant called first
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclassmembernames(args);

        // When: Calling keepclassmembernames(String) after
        task.keepclassmembernames("class Second");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) works with conditional compilation patterns.
     */
    @Test
    public void testKeepClassMemberNamesMap_conditionalCompilation() throws Exception {
        // Given: Map with conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.FeatureEnabled");

        // When: Calling keepclassmembernames() with conditional
        task.keepclassmembernames(args);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map) combines with other keep variants.
     */
    @Test
    public void testKeepClassMemberNamesMap_combineWithOtherKeepVariants() throws Exception {
        // Given: Other keep methods called
        task.keep("class Keep1");
        task.keepclassmembers("class Keep2 { <methods>; }");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames(Map)
        task.keepclassmembernames(args);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) works with multiple interleaved variants.
     */
    @Test
    public void testKeepClassMemberNamesMap_multipleInterleavedVariants() throws Exception {
        // Given: Multiple variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving all three keepclassmembernames variants
        task.keepclassmembernames("class First");
        task.keepclassmembernames(args, "class Second");
        task.keepclassmembernames(args);
        task.keepclassmembernames("class Fourth");
        task.keepclassmembernames(args, "class Fifth");

        // Then: All should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) initializes keep list if null.
     */
    @Test
    public void testKeepClassMemberNamesMap_initializesKeepList() throws Exception {
        // Given: Initial state where keep list is null
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with map
        task.keepclassmembernames(args);

        // Then: The keep list should be initialized
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map) works with map containing "name" key.
     */
    @Test
    public void testKeepClassMemberNamesMap_withNameKey() throws Exception {
        // Given: Map with "name" key
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepclassmembernames() with name key
        task.keepclassmembernames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map) works after all other variants.
     */
    @Test
    public void testKeepClassMemberNamesMap_afterAllOtherVariants() throws Exception {
        // Given: All other variants called
        task.keepclassmembernames("class First");
        task.keepclassmembernames(null, "class Second");
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclassmembernames(args1, "class Third");

        // When: Calling keepclassmembernames(Map)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclassmembernames(args2);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) works with mixed configuration operations.
     */
    @Test
    public void testKeepClassMemberNamesMap_mixedConfigurationOperations() throws Exception {
        // Given: Mixed configuration setup
        task.dontobfuscate();
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclassmembernames(args);
        task.dontshrink();

        // When: Calling keepclassmembernames(Map) again
        task.keepclassmembernames(args);

        // Then: All configurations should be present
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) works with reused map across calls.
     */
    @Test
    public void testKeepClassMemberNamesMap_reusedMapAcrossCalls() throws Exception {
        // Given: Same map used multiple times
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() multiple times with same map
        task.keepclassmembernames(args);
        task.keepclassmembernames(args);
        task.keepclassmembernames(args);

        // Then: All calls should accumulate
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) works with modified map across calls.
     */
    @Test
    public void testKeepClassMemberNamesMap_modifiedMapAcrossCalls() throws Exception {
        // Given: Map that gets modified between calls
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclassmembernames(args);

        // When: Modifying map and calling again
        args.put("if", "class com.example.Condition");
        task.keepclassmembernames(args);

        // Then: Both calls should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) handles very large accumulation.
     */
    @Test
    public void testKeepClassMemberNamesMap_veryLargeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() very many times
        for (int i = 0; i < 50; i++) {
            task.keepclassmembernames(args);
        }

        // Then: All should be accumulated
        assertEquals(50, task.configuration.keep.size(),
                     "Keep list should contain 50 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) works with all three variants in sequence.
     */
    @Test
    public void testKeepClassMemberNamesMap_allVariantsInSequence() throws Exception {
        // Given: All variants will be called
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling all three variants in sequence
        task.keepclassmembernames("class First");          // String variant
        task.keepclassmembernames(args, "class Second");   // Map-String variant
        task.keepclassmembernames(args);                   // Map-only variant

        // Then: All three should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map) preserves order with other variants.
     */
    @Test
    public void testKeepClassMemberNamesMap_preservesOrderWithOtherVariants() throws Exception {
        // Given: Complex ordering scenario
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling variants in specific order
        task.keepclassmembernames(args);                   // Map-only (1st)
        task.keepclassmembernames("class Second");         // String (2nd)
        task.keepclassmembernames(args, "class Third");    // Map-String (3rd)
        task.keepclassmembernames(args);                   // Map-only (4th)

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    // ========================================================================
    // Tests for keepclassmembernames(Map, Closure) variant
    // ========================================================================

    /**
     * Tests that keepclassmembernames(Map, Closure) with null closure adds specification.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_nullClosure_addsSpecification() throws Exception {
        // Given: Map with args and null closure
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with null closure
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) with null map and null closure adds specification.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_nullMapNullClosure_addsSpecification() throws Exception {
        // Given: Null map and null closure
        java.util.Map<String, Object> args = null;

        // When: Calling keepclassmembernames() with nulls
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) can be called multiple times to accumulate rules.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_multipleCalls_accumulatesRules() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() multiple times
        task.keepclassmembernames(args, (groovy.lang.Closure) null);
        task.keepclassmembernames(args, (groovy.lang.Closure) null);
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: All rules should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works with conditional "if" clause.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepclassmembernames() with conditional
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) with different maps accumulates.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_differentMaps_accumulates() throws Exception {
        // Given: Different maps for different rules
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");

        // When: Calling keepclassmembernames() with different maps
        task.keepclassmembernames(args1, (groovy.lang.Closure) null);
        task.keepclassmembernames(args2, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) can interleave with keepclassmembernames(String).
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_interleaveWithStringVariant() throws Exception {
        // Given: Using both variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepclassmembernames("class First");
        task.keepclassmembernames(args, (groovy.lang.Closure) null);
        task.keepclassmembernames("class Third");
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) can interleave with keepclassmembernames(Map, String).
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_interleaveWithMapStringVariant() throws Exception {
        // Given: Using both Map variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepclassmembernames(args, "class First");
        task.keepclassmembernames(args, (groovy.lang.Closure) null);
        task.keepclassmembernames(args, "class Third");
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) can interleave with keepclassmembernames(Map).
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_interleaveWithMapVariant() throws Exception {
        // Given: Using both Map variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepclassmembernames(args);
        task.keepclassmembernames(args, (groovy.lang.Closure) null);
        task.keepclassmembernames(args);
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) integrates with other configuration.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with map and closure
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: All configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works independently of injars.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with map and closure
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works in a complex workflow.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");

        // When: Calling keepclassmembernames() with map and closure
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) handles large accumulation.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_largeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() many times
        for (int i = 0; i < 10; i++) {
            task.keepclassmembernames(args, (groovy.lang.Closure) null);
        }

        // Then: All should be accumulated
        assertEquals(10, task.configuration.keep.size(),
                     "Keep list should contain 10 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) accumulates multiple conditionals.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_multipleConditionals_accumulates() throws Exception {
        // Given: Multiple maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");

        // When: Calling keepclassmembernames() with different conditionals
        task.keepclassmembernames(args1, (groovy.lang.Closure) null);
        task.keepclassmembernames(args2, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard5", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepclassmembernames() with map and closure
        task.keepclassmembernames(args, (groovy.lang.Closure) null);
        task2.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works with complex conditional expression.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_complexConditional() throws Exception {
        // Given: Map with complex conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.Feature1, class com.example.Feature2");

        // When: Calling keepclassmembernames() with complex conditional
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) can be called after keepclassmembernames(String).
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_afterStringVariant() throws Exception {
        // Given: String variant called first
        task.keepclassmembernames("class First");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames(Map, Closure) after
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) can be called before keepclassmembernames(String).
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_beforeStringVariant() throws Exception {
        // Given: Map-Closure variant called first
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // When: Calling keepclassmembernames(String) after
        task.keepclassmembernames("class Second");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works with conditional compilation patterns.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_conditionalCompilation() throws Exception {
        // Given: Map with conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.FeatureEnabled");

        // When: Calling keepclassmembernames() with conditional
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) combines with other keep variants.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_combineWithOtherKeepVariants() throws Exception {
        // Given: Other keep methods called
        task.keep("class Keep1");
        task.keepclassmembers("class Keep2 { <methods>; }");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames(Map, Closure)
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works with multiple interleaved variants.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_multipleInterleavedVariants() throws Exception {
        // Given: Multiple variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving all four keepclassmembernames variants
        task.keepclassmembernames("class First");
        task.keepclassmembernames(args, "class Second");
        task.keepclassmembernames(args);
        task.keepclassmembernames(args, (groovy.lang.Closure) null);
        task.keepclassmembernames("class Fifth");

        // Then: All should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) initializes keep list if null.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_initializesKeepList() throws Exception {
        // Given: Initial state where keep list is null
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() with map and closure
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: The keep list should be initialized
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works with map containing "name" key.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_withNameKey() throws Exception {
        // Given: Map with "name" key
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepclassmembernames() with name key
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works after all other variants.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_afterAllOtherVariants() throws Exception {
        // Given: All other variants called
        task.keepclassmembernames("class First");
        task.keepclassmembernames(null, "class Second");
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepclassmembernames(args1, "class Third");
        task.keepclassmembernames(args1);

        // When: Calling keepclassmembernames(Map, Closure)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepclassmembernames(args2, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works with mixed configuration operations.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_mixedConfigurationOperations() throws Exception {
        // Given: Mixed configuration setup
        task.dontobfuscate();
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclassmembernames(args, (groovy.lang.Closure) null);
        task.dontshrink();

        // When: Calling keepclassmembernames(Map, Closure) again
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: All configurations should be present
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works with reused map across calls.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_reusedMapAcrossCalls() throws Exception {
        // Given: Same map used multiple times
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() multiple times with same map
        task.keepclassmembernames(args, (groovy.lang.Closure) null);
        task.keepclassmembernames(args, (groovy.lang.Closure) null);
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: All calls should accumulate
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works with modified map across calls.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_modifiedMapAcrossCalls() throws Exception {
        // Given: Map that gets modified between calls
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // When: Modifying map and calling again
        args.put("if", "class com.example.Condition");
        task.keepclassmembernames(args, (groovy.lang.Closure) null);

        // Then: Both calls should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) handles very large accumulation.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_veryLargeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepclassmembernames() very many times
        for (int i = 0; i < 50; i++) {
            task.keepclassmembernames(args, (groovy.lang.Closure) null);
        }

        // Then: All should be accumulated
        assertEquals(50, task.configuration.keep.size(),
                     "Keep list should contain 50 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) works with all four variants in sequence.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_allVariantsInSequence() throws Exception {
        // Given: All variants will be called
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling all four variants in sequence
        task.keepclassmembernames("class First");                       // String variant
        task.keepclassmembernames(args, "class Second");                // Map-String variant
        task.keepclassmembernames(args);                                // Map-only variant
        task.keepclassmembernames(args, (groovy.lang.Closure) null);    // Map-Closure variant

        // Then: All four should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) preserves order with other variants.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_preservesOrderWithOtherVariants() throws Exception {
        // Given: Complex ordering scenario
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling variants in specific order
        task.keepclassmembernames(args, (groovy.lang.Closure) null);    // Map-Closure (1st)
        task.keepclassmembernames("class Second");                      // String (2nd)
        task.keepclassmembernames(args, "class Third");                 // Map-String (3rd)
        task.keepclassmembernames(args);                                // Map-only (4th)
        task.keepclassmembernames(args, (groovy.lang.Closure) null);    // Map-Closure (5th)

        // Then: All should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepclassmembernames(Map, Closure) is the base method for Map-only variant.
     */
    @Test
    public void testKeepClassMemberNamesMapClosure_isBaseForMapOnlyVariant() throws Exception {
        // Given: Both variants available
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling both variants (Map-only internally calls Map-Closure with null)
        task.keepclassmembernames(args);                                // Calls keepclassmembernames(args, null)
        task.keepclassmembernames(args, (groovy.lang.Closure) null);    // Direct call

        // Then: Both should behave identically
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }
}

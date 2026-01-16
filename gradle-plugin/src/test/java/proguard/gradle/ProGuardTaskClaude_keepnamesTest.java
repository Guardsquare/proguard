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
 * Tests for {@link ProGuardTask#keepnames(String)}.
 *
 * This test class verifies that the keepnames(String) method correctly
 * adds keepnames rules to the configuration.
 *
 * Method signature: keepnames(Ljava/lang/String;)V
 * - Takes a class specification string that specifies which classes/members to keep names for
 * - Parses it and adds it to configuration.keep list
 * - Returns void
 * - Throws ParseException if the class specification is invalid
 *
 * Key behavior: This method tells ProGuard to keep the names of classes and class members,
 * but only if they are not removed during shrinking. This is different from:
 * - keep(): which prevents removal and keeps names
 * - keepclassmembers(): which keeps members only, allowing class removal if unused
 *
 * The keepnames directive is useful when you want to preserve readable names for debugging
 * or reflection, but still allow unused classes to be removed during shrinking.
 *
 * Internally calls keepnames(null, String) with a null map.
 */
public class ProGuardTaskClaude_keepnamesTest {

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
     * Tests that keepnames() initializes and adds to the keep list.
     */
    @Test
    public void testKeepNames_initializesAndAddsToList() throws Exception {
        // Given: Initial state where keep list is null
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");

        // When: Calling keepnames() with a class specification
        task.keepnames("class MyClass");

        // Then: The keep list should be initialized and contain one entry
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() can be called multiple times to accumulate rules.
     */
    @Test
    public void testKeepNames_multipleCalls_accumulatesRules() throws Exception {
        // When: Calling keepnames() multiple times
        task.keepnames("class FirstClass");
        task.keepnames("class SecondClass");
        task.keepnames("class ThirdClass");

        // Then: All rules should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepnames() works with simple class specification.
     */
    @Test
    public void testKeepNames_simpleClassSpec() throws Exception {
        // Given: A simple class specification

        // When: Calling keepnames() with simple class
        task.keepnames("class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with wildcard class names.
     */
    @Test
    public void testKeepNames_wildcardClassName() throws Exception {
        // Given: A wildcard class specification

        // When: Calling keepnames() with wildcard
        task.keepnames("class com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with public modifier.
     */
    @Test
    public void testKeepNames_publicModifier() throws Exception {
        // Given: A specification with public modifier

        // When: Calling keepnames() with public modifier
        task.keepnames("public class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with abstract class.
     */
    @Test
    public void testKeepNames_abstractClass() throws Exception {
        // Given: An abstract class specification

        // When: Calling keepnames() with abstract class
        task.keepnames("abstract class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with interface.
     */
    @Test
    public void testKeepNames_interface() throws Exception {
        // Given: An interface specification

        // When: Calling keepnames() with interface
        task.keepnames("interface com.example.MyInterface");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with enum.
     */
    @Test
    public void testKeepNames_enum() throws Exception {
        // Given: An enum specification

        // When: Calling keepnames() with enum
        task.keepnames("enum com.example.MyEnum");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with extends clause.
     */
    @Test
    public void testKeepNames_extendsClause() throws Exception {
        // Given: A specification with extends clause

        // When: Calling keepnames() with extends
        task.keepnames("class * extends com.example.BaseClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with implements clause.
     */
    @Test
    public void testKeepNames_implementsClause() throws Exception {
        // Given: A specification with implements clause

        // When: Calling keepnames() with implements
        task.keepnames("class * implements java.io.Serializable");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with annotation specification.
     */
    @Test
    public void testKeepNames_annotation() throws Exception {
        // Given: An annotation specification

        // When: Calling keepnames() with annotation
        task.keepnames("@interface com.example.MyAnnotation");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with class annotation.
     */
    @Test
    public void testKeepNames_classWithAnnotation() throws Exception {
        // Given: A class with annotation specification

        // When: Calling keepnames() with class annotation
        task.keepnames("@com.example.KeepName class *");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with member specification.
     */
    @Test
    public void testKeepNames_withMembers() throws Exception {
        // Given: A class with members specification

        // When: Calling keepnames() with members
        task.keepnames("class com.example.MyClass { <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with field specification.
     */
    @Test
    public void testKeepNames_withFields() throws Exception {
        // Given: A class with fields specification

        // When: Calling keepnames() with fields
        task.keepnames("class com.example.MyClass { <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with specific method specification.
     */
    @Test
    public void testKeepNames_specificMethod() throws Exception {
        // Given: A specific method specification

        // When: Calling keepnames() with specific method
        task.keepnames("class com.example.MyClass { void myMethod(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with specific field specification.
     */
    @Test
    public void testKeepNames_specificField() throws Exception {
        // Given: A specific field specification

        // When: Calling keepnames() with specific field
        task.keepnames("class com.example.MyClass { String name; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with nested classes.
     */
    @Test
    public void testKeepNames_nestedClass() throws Exception {
        // Given: A nested class specification

        // When: Calling keepnames() with nested class
        task.keepnames("class com.example.Outer$Inner");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with package wildcard.
     */
    @Test
    public void testKeepNames_packageWildcard() throws Exception {
        // Given: A package wildcard specification

        // When: Calling keepnames() with package wildcard
        task.keepnames("class com.example.*");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with double package wildcard.
     */
    @Test
    public void testKeepNames_doublePackageWildcard() throws Exception {
        // Given: A double package wildcard specification

        // When: Calling keepnames() with double package wildcard
        task.keepnames("class com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with final modifier.
     */
    @Test
    public void testKeepNames_finalModifier() throws Exception {
        // Given: A specification with final modifier

        // When: Calling keepnames() with final modifier
        task.keepnames("final class com.example.MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with all members wildcard.
     */
    @Test
    public void testKeepNames_allMembersWildcard() throws Exception {
        // Given: An all-members wildcard specification

        // When: Calling keepnames() with all members wildcard
        task.keepnames("class com.example.MyClass { *; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with Android context.
     */
    @Test
    public void testKeepNames_androidContext() throws Exception {
        // Given: An Android-specific specification

        // When: Calling keepnames() with Android class
        task.keepnames("class * extends android.app.Activity");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with serialization classes.
     */
    @Test
    public void testKeepNames_serialization() throws Exception {
        // Given: A serialization class specification

        // When: Calling keepnames() with Serializable
        task.keepnames("class * implements java.io.Serializable");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with native methods.
     */
    @Test
    public void testKeepNames_nativeMethods() throws Exception {
        // Given: A native method specification

        // When: Calling keepnames() with native methods
        task.keepnames("class com.example.NativeLib { native <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with static methods.
     */
    @Test
    public void testKeepNames_staticMethods() throws Exception {
        // Given: A static method specification

        // When: Calling keepnames() with static methods
        task.keepnames("class com.example.Util { public static <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with constructors.
     */
    @Test
    public void testKeepNames_constructors() throws Exception {
        // Given: A constructor specification

        // When: Calling keepnames() with constructors
        task.keepnames("class com.example.MyClass { <init>(...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with R class fields.
     */
    @Test
    public void testKeepNames_androidRClass() throws Exception {
        // Given: An Android R class specification

        // When: Calling keepnames() with R class
        task.keepnames("class **.R$*");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() can be mixed with other configuration methods.
     */
    @Test
    public void testKeepNames_mixedWithOtherConfiguration() throws Exception {
        // Given: Other configuration is present
        task.dontobfuscate();

        // When: Calling keepnames()
        task.keepnames("class com.example.MyClass");

        // Then: Both configurations should be present
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() accumulates across multiple calls.
     */
    @Test
    public void testKeepNames_accumulation() throws Exception {
        // When: Calling keepnames() multiple times with different specs
        task.keepnames("class com.example.ClassA");
        task.keepnames("class com.example.ClassB");
        task.keepnames("class com.example.ClassC");
        task.keepnames("class com.example.ClassD");
        task.keepnames("class com.example.ClassE");

        // Then: All specifications should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepnames() works with method parameters.
     */
    @Test
    public void testKeepNames_methodParameters() throws Exception {
        // Given: A method with parameters specification

        // When: Calling keepnames() with method parameters
        task.keepnames("class com.example.MyClass { void process(java.lang.String, int); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with varargs.
     */
    @Test
    public void testKeepNames_varargs() throws Exception {
        // Given: A varargs specification

        // When: Calling keepnames() with varargs
        task.keepnames("class com.example.MyClass { void print(java.lang.String...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with array types.
     */
    @Test
    public void testKeepNames_arrayTypes() throws Exception {
        // Given: An array type specification

        // When: Calling keepnames() with array types
        task.keepnames("class com.example.MyClass { java.lang.String[] getItems(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with generic types.
     */
    @Test
    public void testKeepNames_genericTypes() throws Exception {
        // Given: A generic type specification

        // When: Calling keepnames() with generic types
        task.keepnames("class com.example.MyClass { java.util.List get(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with volatile fields.
     */
    @Test
    public void testKeepNames_volatileFields() throws Exception {
        // Given: A volatile field specification

        // When: Calling keepnames() with volatile field
        task.keepnames("class com.example.ThreadSafe { volatile boolean flag; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with synchronized methods.
     */
    @Test
    public void testKeepNames_synchronizedMethods() throws Exception {
        // Given: A synchronized method specification

        // When: Calling keepnames() with synchronized method
        task.keepnames("class com.example.ThreadSafe { synchronized void update(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() works with transient fields.
     */
    @Test
    public void testKeepNames_transientFields() throws Exception {
        // Given: A transient field specification

        // When: Calling keepnames() with transient field
        task.keepnames("class com.example.Serializable { transient int cache; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames() preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepNames_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Both tasks call keepnames() with the same spec
        task.keepnames("class com.example.MyClass");
        task2.keepnames("class com.example.MyClass");

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepnames() works with wildcard return types.
     */
    @Test
    public void testKeepNames_wildcardReturnTypes() throws Exception {
        // Given: A wildcard return type specification

        // When: Calling keepnames() with wildcard return type
        task.keepnames("class com.example.MyClass { *** get*(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    // ========================================================================
    // Tests for keepnames(Map, String) variant
    // ========================================================================

    /**
     * Tests that keepnames(Map, String) with null map works like keepnames(String).
     */
    @Test
    public void testKeepNamesWithMap_nullMap_behavesLikeStringOnly() throws Exception {
        // When: Calling keepnames() with null map
        task.keepnames(null, "class MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) with empty map adds specification.
     */
    @Test
    public void testKeepNamesWithMap_emptyMap_addsSpecification() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with empty map
        task.keepnames(args, "class MyClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) can be called multiple times.
     */
    @Test
    public void testKeepNamesWithMap_multipleCalls_accumulatesRules() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() multiple times
        task.keepnames(args, "class FirstClass");
        task.keepnames(args, "class SecondClass");
        task.keepnames(args, "class ThirdClass");

        // Then: All rules should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepnames(Map, String) works with conditional "if" clause.
     */
    @Test
    public void testKeepNamesWithMap_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepnames() with conditional
        task.keepnames(args, "class com.example.MyClass");

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works independently of injars.
     */
    @Test
    public void testKeepNamesWithMap_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with map
        task.keepnames(args, "class MyClass");

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works in a complex workflow.
     */
    @Test
    public void testKeepNamesWithMap_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");

        // When: Calling keepnames() with map
        task.keepnames(args, "class com.example.Model");

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) with different maps accumulates.
     */
    @Test
    public void testKeepNamesWithMap_differentMaps_accumulates() throws Exception {
        // Given: Different maps for different rules
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");

        // When: Calling keepnames() with different maps
        task.keepnames(args1, "class FirstClass");
        task.keepnames(args2, "class SecondClass");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map, String) works with wildcard classes.
     */
    @Test
    public void testKeepNamesWithMap_wildcardClasses() throws Exception {
        // Given: Map for wildcard rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with wildcard classes
        task.keepnames(args, "class com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with extends clause.
     */
    @Test
    public void testKeepNamesWithMap_extendsClause() throws Exception {
        // Given: Map for extends rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with extends
        task.keepnames(args, "class * extends com.example.BaseClass");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with implements clause.
     */
    @Test
    public void testKeepNamesWithMap_implementsClause() throws Exception {
        // Given: Map for implements rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with implements
        task.keepnames(args, "class * implements java.io.Serializable");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with member specifications.
     */
    @Test
    public void testKeepNamesWithMap_memberSpecifications() throws Exception {
        // Given: Map for member rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with members
        task.keepnames(args, "class com.example.MyClass { <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with field specifications.
     */
    @Test
    public void testKeepNamesWithMap_fieldSpecifications() throws Exception {
        // Given: Map for field rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with fields
        task.keepnames(args, "class com.example.MyClass { <fields>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with Android classes.
     */
    @Test
    public void testKeepNamesWithMap_androidClasses() throws Exception {
        // Given: Map for Android rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() for Android classes
        task.keepnames(args, "class * extends android.app.Activity");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with enum classes.
     */
    @Test
    public void testKeepNamesWithMap_enumClasses() throws Exception {
        // Given: Map for enum rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() for enums
        task.keepnames(args, "enum *");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with interface classes.
     */
    @Test
    public void testKeepNamesWithMap_interfaceClasses() throws Exception {
        // Given: Map for interface rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() for interfaces
        task.keepnames(args, "interface com.example.**");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with annotation specifications.
     */
    @Test
    public void testKeepNamesWithMap_annotationSpecifications() throws Exception {
        // Given: Map for annotation rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with annotations
        task.keepnames(args, "@interface com.example.MyAnnotation");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with class annotations.
     */
    @Test
    public void testKeepNamesWithMap_classAnnotations() throws Exception {
        // Given: Map for class annotation rule
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with class annotations
        task.keepnames(args, "@com.example.KeepName class *");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) can interleave with keepnames(String).
     */
    @Test
    public void testKeepNamesWithMap_interleaveWithStringVariant() throws Exception {
        // Given: Using both variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepnames("class First");
        task.keepnames(args, "class Second");
        task.keepnames("class Third");
        task.keepnames(args, "class Fourth");

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepnames(Map, String) works with wildcard return types.
     */
    @Test
    public void testKeepNamesWithMap_wildcardReturnTypes() throws Exception {
        // Given: Map for wildcard return types
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with wildcard return type
        task.keepnames(args, "class com.example.MyClass { *** get*(); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with native methods.
     */
    @Test
    public void testKeepNamesWithMap_nativeMethods() throws Exception {
        // Given: Map for native methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() for native methods
        task.keepnames(args, "class com.example.NativeLib { native <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with static methods.
     */
    @Test
    public void testKeepNamesWithMap_staticMethods() throws Exception {
        // Given: Map for static methods
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() for static methods
        task.keepnames(args, "class com.example.Util { public static <methods>; }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with conditional compilation.
     */
    @Test
    public void testKeepNamesWithMap_conditionalCompilation() throws Exception {
        // Given: Map with conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.FeatureEnabled");

        // When: Calling keepnames() with conditional
        task.keepnames(args, "class com.example.FeatureClass");

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) accumulates multiple conditionals.
     */
    @Test
    public void testKeepNamesWithMap_multipleConditionals_accumulates() throws Exception {
        // Given: Multiple maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");

        // When: Calling keepnames() with different conditionals
        task.keepnames(args1, "class First");
        task.keepnames(args2, "class Second");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map, String) works with nested classes.
     */
    @Test
    public void testKeepNamesWithMap_nestedClasses() throws Exception {
        // Given: Map for nested classes
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() for nested classes
        task.keepnames(args, "class com.example.Outer$Inner");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) integrates with other configuration.
     */
    @Test
    public void testKeepNamesWithMap_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with map
        task.keepnames(args, "class com.example.MyClass");

        // Then: All configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) handles large accumulation.
     */
    @Test
    public void testKeepNamesWithMap_largeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() many times
        for (int i = 0; i < 10; i++) {
            task.keepnames(args, "class Class" + i);
        }

        // Then: All should be accumulated
        assertEquals(10, task.configuration.keep.size(),
                     "Keep list should contain 10 specifications");
    }

    /**
     * Tests that keepnames(Map, String) works with R class fields.
     */
    @Test
    public void testKeepNamesWithMap_rClassFields() throws Exception {
        // Given: Map for R class fields
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() for R class
        task.keepnames(args, "class **.R$*");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) works with constructors.
     */
    @Test
    public void testKeepNamesWithMap_constructors() throws Exception {
        // Given: Map for constructors
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() for constructors
        task.keepnames(args, "class com.example.MyClass { <init>(...); }");

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, String) preserves consistent behavior.
     */
    @Test
    public void testKeepNamesWithMap_consistentBehavior() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard3", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepnames() with map
        task.keepnames(args, "class MyClass");
        task2.keepnames(args, "class MyClass");

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    // ========================================================================
    // Tests for keepnames(Map) variant
    // ========================================================================

    /**
     * Tests that keepnames(Map) with null map adds specification.
     */
    @Test
    public void testKeepNamesMap_nullMap_addsSpecification() throws Exception {
        // Given: Null map
        java.util.Map<String, Object> args = null;

        // When: Calling keepnames() with null map
        task.keepnames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map) with empty map adds specification.
     */
    @Test
    public void testKeepNamesMap_emptyMap_addsSpecification() throws Exception {
        // Given: Empty map
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with empty map
        task.keepnames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map) can be called multiple times to accumulate rules.
     */
    @Test
    public void testKeepNamesMap_multipleCalls_accumulatesRules() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() multiple times
        task.keepnames(args);
        task.keepnames(args);
        task.keepnames(args);

        // Then: All rules should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepnames(Map) works with conditional "if" clause.
     */
    @Test
    public void testKeepNamesMap_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepnames() with conditional
        task.keepnames(args);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map) with different maps accumulates.
     */
    @Test
    public void testKeepNamesMap_differentMaps_accumulates() throws Exception {
        // Given: Different maps for different rules
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");

        // When: Calling keepnames() with different maps
        task.keepnames(args1);
        task.keepnames(args2);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map) can interleave with keepnames(String).
     */
    @Test
    public void testKeepNamesMap_interleaveWithStringVariant() throws Exception {
        // Given: Using both variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepnames("class First");
        task.keepnames(args);
        task.keepnames("class Third");
        task.keepnames(args);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepnames(Map) can interleave with keepnames(Map, String).
     */
    @Test
    public void testKeepNamesMap_interleaveWithMapStringVariant() throws Exception {
        // Given: Using both Map variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepnames(args, "class First");
        task.keepnames(args);
        task.keepnames(args, "class Third");
        task.keepnames(args);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepnames(Map) integrates with other configuration.
     */
    @Test
    public void testKeepNamesMap_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with map
        task.keepnames(args);

        // Then: All configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map) works independently of injars.
     */
    @Test
    public void testKeepNamesMap_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with map
        task.keepnames(args);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map) works in a complex workflow.
     */
    @Test
    public void testKeepNamesMap_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");

        // When: Calling keepnames() with map
        task.keepnames(args);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map) handles large accumulation.
     */
    @Test
    public void testKeepNamesMap_largeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() many times
        for (int i = 0; i < 10; i++) {
            task.keepnames(args);
        }

        // Then: All should be accumulated
        assertEquals(10, task.configuration.keep.size(),
                     "Keep list should contain 10 specifications");
    }

    /**
     * Tests that keepnames(Map) accumulates multiple conditionals.
     */
    @Test
    public void testKeepNamesMap_multipleConditionals_accumulates() throws Exception {
        // Given: Multiple maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");

        // When: Calling keepnames() with different conditionals
        task.keepnames(args1);
        task.keepnames(args2);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map) preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepNamesMap_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard4", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepnames() with map
        task.keepnames(args);
        task2.keepnames(args);

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepnames(Map) works with complex conditional expression.
     */
    @Test
    public void testKeepNamesMap_complexConditional() throws Exception {
        // Given: Map with complex conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.Feature1, class com.example.Feature2");

        // When: Calling keepnames() with complex conditional
        task.keepnames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map) can be called after keepnames(String).
     */
    @Test
    public void testKeepNamesMap_afterStringVariant() throws Exception {
        // Given: String variant called first
        task.keepnames("class First");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames(Map) after
        task.keepnames(args);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map) can be called before keepnames(String).
     */
    @Test
    public void testKeepNamesMap_beforeStringVariant() throws Exception {
        // Given: Map variant called first
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepnames(args);

        // When: Calling keepnames(String) after
        task.keepnames("class Second");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map) works with conditional compilation patterns.
     */
    @Test
    public void testKeepNamesMap_conditionalCompilation() throws Exception {
        // Given: Map with conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.FeatureEnabled");

        // When: Calling keepnames() with conditional
        task.keepnames(args);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map) combines with other keep variants.
     */
    @Test
    public void testKeepNamesMap_combineWithOtherKeepVariants() throws Exception {
        // Given: Other keep methods called
        task.keep("class Keep1");
        task.keepclassmembers("class Keep2 { <methods>; }");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames(Map)
        task.keepnames(args);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepnames(Map) works with multiple interleaved variants.
     */
    @Test
    public void testKeepNamesMap_multipleInterleavedVariants() throws Exception {
        // Given: Multiple variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving all three keepnames variants
        task.keepnames("class First");
        task.keepnames(args, "class Second");
        task.keepnames(args);
        task.keepnames("class Fourth");
        task.keepnames(args, "class Fifth");

        // Then: All should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepnames(Map) initializes keep list if null.
     */
    @Test
    public void testKeepNamesMap_initializesKeepList() throws Exception {
        // Given: Initial state where keep list is null
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with map
        task.keepnames(args);

        // Then: The keep list should be initialized
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map) works with map containing "name" key.
     */
    @Test
    public void testKeepNamesMap_withNameKey() throws Exception {
        // Given: Map with "name" key
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepnames() with name key
        task.keepnames(args);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map) works after all other variants.
     */
    @Test
    public void testKeepNamesMap_afterAllOtherVariants() throws Exception {
        // Given: All other variants called
        task.keepnames("class First");
        task.keepnames(null, "class Second");
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepnames(args1, "class Third");

        // When: Calling keepnames(Map)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepnames(args2);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepnames(Map) works with mixed configuration operations.
     */
    @Test
    public void testKeepNamesMap_mixedConfigurationOperations() throws Exception {
        // Given: Mixed configuration setup
        task.dontobfuscate();
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepnames(args);
        task.dontshrink();

        // When: Calling keepnames(Map) again
        task.keepnames(args);

        // Then: All configurations should be present
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map) works with reused map across calls.
     */
    @Test
    public void testKeepNamesMap_reusedMapAcrossCalls() throws Exception {
        // Given: Same map used multiple times
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() multiple times with same map
        task.keepnames(args);
        task.keepnames(args);
        task.keepnames(args);

        // Then: All calls should accumulate
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepnames(Map) works with modified map across calls.
     */
    @Test
    public void testKeepNamesMap_modifiedMapAcrossCalls() throws Exception {
        // Given: Map that gets modified between calls
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepnames(args);

        // When: Modifying map and calling again
        args.put("if", "class com.example.Condition");
        task.keepnames(args);

        // Then: Both calls should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map) handles very large accumulation.
     */
    @Test
    public void testKeepNamesMap_veryLargeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() very many times
        for (int i = 0; i < 50; i++) {
            task.keepnames(args);
        }

        // Then: All should be accumulated
        assertEquals(50, task.configuration.keep.size(),
                     "Keep list should contain 50 specifications");
    }

    /**
     * Tests that keepnames(Map) works with all three variants in sequence.
     */
    @Test
    public void testKeepNamesMap_allVariantsInSequence() throws Exception {
        // Given: All variants will be called
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling all three variants in sequence
        task.keepnames("class First");          // String variant
        task.keepnames(args, "class Second");   // Map-String variant
        task.keepnames(args);                   // Map-only variant

        // Then: All three should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepnames(Map) preserves order with other variants.
     */
    @Test
    public void testKeepNamesMap_preservesOrderWithOtherVariants() throws Exception {
        // Given: Complex ordering scenario
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling variants in specific order
        task.keepnames(args);                   // Map-only (1st)
        task.keepnames("class Second");         // String (2nd)
        task.keepnames(args, "class Third");    // Map-String (3rd)
        task.keepnames(args);                   // Map-only (4th)

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    // ========================================================================
    // Tests for keepnames(Map, Closure) variant
    // ========================================================================

    /**
     * Tests that keepnames(Map, Closure) with null closure adds specification.
     */
    @Test
    public void testKeepNamesMapClosure_nullClosure_addsSpecification() throws Exception {
        // Given: Map with args and null closure
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with null closure
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, Closure) with null map and null closure adds specification.
     */
    @Test
    public void testKeepNamesMapClosure_nullMapNullClosure_addsSpecification() throws Exception {
        // Given: Null map and null closure
        java.util.Map<String, Object> args = null;

        // When: Calling keepnames() with nulls
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, Closure) can be called multiple times to accumulate rules.
     */
    @Test
    public void testKeepNamesMapClosure_multipleCalls_accumulatesRules() throws Exception {
        // Given: Map with args
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() multiple times
        task.keepnames(args, (groovy.lang.Closure) null);
        task.keepnames(args, (groovy.lang.Closure) null);
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: All rules should be accumulated
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) works with conditional "if" clause.
     */
    @Test
    public void testKeepNamesMapClosure_conditionalIf_addsConditionalRule() throws Exception {
        // Given: Map with "if" condition
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.ConditionClass");

        // When: Calling keepnames() with conditional
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, Closure) with different maps accumulates.
     */
    @Test
    public void testKeepNamesMapClosure_differentMaps_accumulates() throws Exception {
        // Given: Different maps for different rules
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition");

        // When: Calling keepnames() with different maps
        task.keepnames(args1, (groovy.lang.Closure) null);
        task.keepnames(args2, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) can interleave with keepnames(String).
     */
    @Test
    public void testKeepNamesMapClosure_interleaveWithStringVariant() throws Exception {
        // Given: Using both variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepnames("class First");
        task.keepnames(args, (groovy.lang.Closure) null);
        task.keepnames("class Third");
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) can interleave with keepnames(Map, String).
     */
    @Test
    public void testKeepNamesMapClosure_interleaveWithMapStringVariant() throws Exception {
        // Given: Using both Map variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepnames(args, "class First");
        task.keepnames(args, (groovy.lang.Closure) null);
        task.keepnames(args, "class Third");
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) can interleave with keepnames(Map).
     */
    @Test
    public void testKeepNamesMapClosure_interleaveWithMapVariant() throws Exception {
        // Given: Using both Map variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving both variants
        task.keepnames(args);
        task.keepnames(args, (groovy.lang.Closure) null);
        task.keepnames(args);
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) integrates with other configuration.
     */
    @Test
    public void testKeepNamesMapClosure_integratesWithOtherConfig() throws Exception {
        // Given: Other configurations present
        task.dontobfuscate();
        task.dontshrink();
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with map and closure
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: All configurations should coexist
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, Closure) works independently of injars.
     */
    @Test
    public void testKeepNamesMapClosure_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with map and closure
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, Closure) works in a complex workflow.
     */
    @Test
    public void testKeepNamesMapClosure_complexWorkflow() throws Exception {
        // Given: Complex project setup
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars(System.getProperty("java.home") + "/jmods/java.base.jmod");

        // When: Calling keepnames() with map and closure
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: All configurations should be present
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, Closure) handles large accumulation.
     */
    @Test
    public void testKeepNamesMapClosure_largeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() many times
        for (int i = 0; i < 10; i++) {
            task.keepnames(args, (groovy.lang.Closure) null);
        }

        // Then: All should be accumulated
        assertEquals(10, task.configuration.keep.size(),
                     "Keep list should contain 10 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) accumulates multiple conditionals.
     */
    @Test
    public void testKeepNamesMapClosure_multipleConditionals_accumulates() throws Exception {
        // Given: Multiple maps with different conditionals
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        args1.put("if", "class com.example.Condition1");
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        args2.put("if", "class com.example.Condition2");

        // When: Calling keepnames() with different conditionals
        task.keepnames(args1, (groovy.lang.Closure) null);
        task.keepnames(args2, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) preserves consistent behavior across task instances.
     */
    @Test
    public void testKeepNamesMapClosure_consistentAcrossInstances() throws Exception {
        // Given: Two separate task instances
        ProGuardTask task2 = project.getTasks().create("testProguard5", ProGuardTask.class);
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Both tasks call keepnames() with map and closure
        task.keepnames(args, (groovy.lang.Closure) null);
        task2.keepnames(args, (groovy.lang.Closure) null);

        // Then: Both should have one specification
        assertEquals(1, task.configuration.keep.size(),
                     "First task should have 1 specification");
        assertEquals(1, task2.configuration.keep.size(),
                     "Second task should have 1 specification");
    }

    /**
     * Tests that keepnames(Map, Closure) works with complex conditional expression.
     */
    @Test
    public void testKeepNamesMapClosure_complexConditional() throws Exception {
        // Given: Map with complex conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.Feature1, class com.example.Feature2");

        // When: Calling keepnames() with complex conditional
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, Closure) can be called after keepnames(String).
     */
    @Test
    public void testKeepNamesMapClosure_afterStringVariant() throws Exception {
        // Given: String variant called first
        task.keepnames("class First");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames(Map, Closure) after
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) can be called before keepnames(String).
     */
    @Test
    public void testKeepNamesMapClosure_beforeStringVariant() throws Exception {
        // Given: Map-Closure variant called first
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepnames(args, (groovy.lang.Closure) null);

        // When: Calling keepnames(String) after
        task.keepnames("class Second");

        // Then: Both should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) works with conditional compilation patterns.
     */
    @Test
    public void testKeepNamesMapClosure_conditionalCompilation() throws Exception {
        // Given: Map with conditional
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("if", "class com.example.FeatureEnabled");

        // When: Calling keepnames() with conditional
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: The conditional specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, Closure) combines with other keep variants.
     */
    @Test
    public void testKeepNamesMapClosure_combineWithOtherKeepVariants() throws Exception {
        // Given: Other keep methods called
        task.keep("class Keep1");
        task.keepclassmembers("class Keep2 { <methods>; }");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames(Map, Closure)
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) works with multiple interleaved variants.
     */
    @Test
    public void testKeepNamesMapClosure_multipleInterleavedVariants() throws Exception {
        // Given: Multiple variants
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Interleaving all four keepnames variants
        task.keepnames("class First");
        task.keepnames(args, "class Second");
        task.keepnames(args);
        task.keepnames(args, (groovy.lang.Closure) null);
        task.keepnames("class Fifth");

        // Then: All should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) initializes keep list if null.
     */
    @Test
    public void testKeepNamesMapClosure_initializesKeepList() throws Exception {
        // Given: Initial state where keep list is null
        assertNull(task.configuration.keep,
                   "Keep list should initially be null");
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() with map and closure
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: The keep list should be initialized
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, Closure) works with map containing "name" key.
     */
    @Test
    public void testKeepNamesMapClosure_withNameKey() throws Exception {
        // Given: Map with "name" key
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("name", "com.example.MyClass");

        // When: Calling keepnames() with name key
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: The specification should be added
        assertNotNull(task.configuration.keep,
                      "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(),
                     "Keep list should contain 1 specification");
    }

    /**
     * Tests that keepnames(Map, Closure) works after all other variants.
     */
    @Test
    public void testKeepNamesMapClosure_afterAllOtherVariants() throws Exception {
        // Given: All other variants called
        task.keepnames("class First");
        task.keepnames(null, "class Second");
        java.util.Map<String, Object> args1 = new java.util.HashMap<>();
        task.keepnames(args1, "class Third");
        task.keepnames(args1);

        // When: Calling keepnames(Map, Closure)
        java.util.Map<String, Object> args2 = new java.util.HashMap<>();
        task.keepnames(args2, (groovy.lang.Closure) null);

        // Then: All should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) works with mixed configuration operations.
     */
    @Test
    public void testKeepNamesMapClosure_mixedConfigurationOperations() throws Exception {
        // Given: Mixed configuration setup
        task.dontobfuscate();
        task.injars("input.jar");
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepnames(args, (groovy.lang.Closure) null);
        task.dontshrink();

        // When: Calling keepnames(Map, Closure) again
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: All configurations should be present
        assertTrue(task.configuration.obfuscate == false,
                   "Obfuscation should be disabled");
        assertTrue(task.configuration.shrink == false,
                   "Shrinking should be disabled");
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) works with reused map across calls.
     */
    @Test
    public void testKeepNamesMapClosure_reusedMapAcrossCalls() throws Exception {
        // Given: Same map used multiple times
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() multiple times with same map
        task.keepnames(args, (groovy.lang.Closure) null);
        task.keepnames(args, (groovy.lang.Closure) null);
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: All calls should accumulate
        assertEquals(3, task.configuration.keep.size(),
                     "Keep list should contain 3 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) works with modified map across calls.
     */
    @Test
    public void testKeepNamesMapClosure_modifiedMapAcrossCalls() throws Exception {
        // Given: Map that gets modified between calls
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        task.keepnames(args, (groovy.lang.Closure) null);

        // When: Modifying map and calling again
        args.put("if", "class com.example.Condition");
        task.keepnames(args, (groovy.lang.Closure) null);

        // Then: Both calls should be accumulated
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) handles very large accumulation.
     */
    @Test
    public void testKeepNamesMapClosure_veryLargeAccumulation() throws Exception {
        // Given: Map for rules
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling keepnames() very many times
        for (int i = 0; i < 50; i++) {
            task.keepnames(args, (groovy.lang.Closure) null);
        }

        // Then: All should be accumulated
        assertEquals(50, task.configuration.keep.size(),
                     "Keep list should contain 50 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) works with all four variants in sequence.
     */
    @Test
    public void testKeepNamesMapClosure_allVariantsInSequence() throws Exception {
        // Given: All variants will be called
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling all four variants in sequence
        task.keepnames("class First");                       // String variant
        task.keepnames(args, "class Second");                // Map-String variant
        task.keepnames(args);                                // Map-only variant
        task.keepnames(args, (groovy.lang.Closure) null);    // Map-Closure variant

        // Then: All four should be accumulated
        assertEquals(4, task.configuration.keep.size(),
                     "Keep list should contain 4 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) preserves order with other variants.
     */
    @Test
    public void testKeepNamesMapClosure_preservesOrderWithOtherVariants() throws Exception {
        // Given: Complex ordering scenario
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling variants in specific order
        task.keepnames(args, (groovy.lang.Closure) null);    // Map-Closure (1st)
        task.keepnames("class Second");                      // String (2nd)
        task.keepnames(args, "class Third");                 // Map-String (3rd)
        task.keepnames(args);                                // Map-only (4th)
        task.keepnames(args, (groovy.lang.Closure) null);    // Map-Closure (5th)

        // Then: All should be accumulated
        assertEquals(5, task.configuration.keep.size(),
                     "Keep list should contain 5 specifications");
    }

    /**
     * Tests that keepnames(Map, Closure) is the base method for Map-only variant.
     */
    @Test
    public void testKeepNamesMapClosure_isBaseForMapOnlyVariant() throws Exception {
        // Given: Both variants available
        java.util.Map<String, Object> args = new java.util.HashMap<>();

        // When: Calling both variants (Map-only internally calls Map-Closure with null)
        task.keepnames(args);                                // Calls keepnames(args, null)
        task.keepnames(args, (groovy.lang.Closure) null);    // Direct call

        // Then: Both should behave identically
        assertEquals(2, task.configuration.keep.size(),
                     "Keep list should contain 2 specifications");
    }
}

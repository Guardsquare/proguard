/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle;

import groovy.lang.Closure;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.ClassSpecification;
import proguard.KeepClassSpecification;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#field(Map)}.
 *
 * This test class verifies that the field(Map) method correctly adds field specifications
 * to a class specification when used inside a keep rule closure.
 *
 * Method signature: field(Ljava/util/Map;)V
 * - Takes a Map of member specification arguments (e.g., name, type, access flags)
 * - Adds a field specification to the current classSpecification
 * - Returns void
 * - Throws IllegalArgumentException if called outside a class specification context
 * - Throws ParseException if the arguments are invalid
 *
 * Key behavior: This method is designed to be called within a Closure passed to keep(),
 * keepclassmembers(), or similar methods. It allows specifying which fields should be kept
 * from being removed or obfuscated by ProGuard.
 *
 * The method requires that classSpecification is set (which happens when a Closure is
 * being evaluated), otherwise it throws IllegalArgumentException. It delegates to
 * createMemberSpecification() to parse the Map arguments and create a MemberSpecification.
 */
public class ProGuardTaskClaude_fieldTest {

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
     * Tests that field() throws IllegalArgumentException when called outside a class specification.
     */
    @Test
    public void testField_outsideClassSpecification_throwsException() {
        // Given: A field specification map
        Map<String, Object> fieldArgs = new HashMap<>();
        fieldArgs.put("name", "myField");

        // When/Then: Calling field() outside a class specification throws exception
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> task.field(fieldArgs),
                "field() should throw IllegalArgumentException when called outside class specification"
        );

        assertTrue(exception.getMessage().contains("nested inside a class specification"),
                "Exception message should indicate field must be nested inside class specification");
    }

    /**
     * Tests that field() works when called inside a keep closure.
     */
    @Test
    public void testField_insideKeepClosure_addsFieldSpecification() throws Exception {
        // Given: A keep rule with a field specification inside the closure
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a field
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                Map<String, Object> fieldArgs = new HashMap<>();
                fieldArgs.put("name", "myField");
                try {
                    task.field(fieldArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep list should contain a specification with a field
        assertNotNull(task.configuration.keep, "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(), "Keep list should contain 1 specification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(1, keepSpec.fieldSpecifications.size(), "Should have 1 field specification");
    }

    /**
     * Tests that field() can add multiple field specifications in one closure.
     */
    @Test
    public void testField_multipleFields_addsAllSpecifications() throws Exception {
        // Given: A keep rule with multiple field specifications
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds multiple fields
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> field1 = new HashMap<>();
                    field1.put("name", "field1");
                    task.field(field1);

                    Map<String, Object> field2 = new HashMap<>();
                    field2.put("name", "field2");
                    task.field(field2);

                    Map<String, Object> field3 = new HashMap<>();
                    field3.put("name", "field3");
                    task.field(field3);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep specification should contain all three fields
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(3, keepSpec.fieldSpecifications.size(), "Should have 3 field specifications");
    }

    /**
     * Tests that field() works with type specification.
     */
    @Test
    public void testField_withType_addsFieldWithType() throws Exception {
        // Given: A keep rule with a field that has a type
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a typed field
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> fieldArgs = new HashMap<>();
                    fieldArgs.put("name", "myField");
                    fieldArgs.put("type", "int");
                    task.field(fieldArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The field specification should include the type
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(1, keepSpec.fieldSpecifications.size(), "Should have 1 field specification");
        assertNotNull(keepSpec.fieldSpecifications.get(0).descriptor,
                "Field descriptor should be set for typed field");
    }

    /**
     * Tests that field() works with access modifiers.
     */
    @Test
    public void testField_withAccessModifiers_addsFieldWithAccessFlags() throws Exception {
        // Given: A keep rule with a field that has access modifiers
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a field with access modifiers
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> fieldArgs = new HashMap<>();
                    fieldArgs.put("name", "myField");
                    fieldArgs.put("access", "public static");
                    task.field(fieldArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The field specification should include access flags
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(1, keepSpec.fieldSpecifications.size(), "Should have 1 field specification");
        assertTrue(keepSpec.fieldSpecifications.get(0).requiredSetAccessFlags != 0,
                "Field should have access flags set");
    }

    /**
     * Tests that field() works with annotations.
     */
    @Test
    public void testField_withAnnotation_addsFieldWithAnnotation() throws Exception {
        // Given: A keep rule with a field that has an annotation
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a field with annotation
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> fieldArgs = new HashMap<>();
                    fieldArgs.put("name", "myField");
                    fieldArgs.put("annotation", "MyAnnotation");
                    task.field(fieldArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The field specification should include the annotation
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(1, keepSpec.fieldSpecifications.size(), "Should have 1 field specification");
        assertNotNull(keepSpec.fieldSpecifications.get(0).annotationType,
                "Field should have annotation type set");
    }

    /**
     * Tests that field() works with keepclassmembers.
     */
    @Test
    public void testField_withKeepClassMembers_addsFieldSpecification() throws Exception {
        // Given: A keepclassmembers rule with a field specification
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keepclassmembers() with a closure that adds a field
        task.keepclassmembers(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> fieldArgs = new HashMap<>();
                    fieldArgs.put("name", "myField");
                    task.field(fieldArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep list should contain a specification with a field
        assertNotNull(task.configuration.keep, "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(), "Keep list should contain 1 specification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(1, keepSpec.fieldSpecifications.size(), "Should have 1 field specification");
    }

    /**
     * Tests that field() works with complex field specification.
     */
    @Test
    public void testField_complexSpecification_addsCompleteFieldSpec() throws Exception {
        // Given: A keep rule with a complex field specification
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a complex field specification
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> fieldArgs = new HashMap<>();
                    fieldArgs.put("name", "myField");
                    fieldArgs.put("type", "java.lang.String");
                    fieldArgs.put("access", "private final");
                    fieldArgs.put("annotation", "MyAnnotation");
                    task.field(fieldArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The field specification should include all properties
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(1, keepSpec.fieldSpecifications.size(), "Should have 1 field specification");
        assertNotNull(keepSpec.fieldSpecifications.get(0).descriptor,
                "Field should have type descriptor");
        assertNotNull(keepSpec.fieldSpecifications.get(0).annotationType,
                "Field should have annotation");
        assertTrue(keepSpec.fieldSpecifications.get(0).requiredSetAccessFlags != 0,
                "Field should have access flags");
    }

    /**
     * Tests that field() works with wildcards in field name.
     */
    @Test
    public void testField_withWildcardName_addsFieldWithWildcard() throws Exception {
        // Given: A keep rule with a wildcard field name
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a field with wildcard
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> fieldArgs = new HashMap<>();
                    fieldArgs.put("name", "my*Field");
                    task.field(fieldArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The field specification should accept the wildcard name
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(1, keepSpec.fieldSpecifications.size(), "Should have 1 field specification");
    }

    /**
     * Tests that field() can be called without name (match all fields).
     */
    @Test
    public void testField_withoutName_addsGenericFieldSpec() throws Exception {
        // Given: A keep rule with a field specification without name
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a field without name
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> fieldArgs = new HashMap<>();
                    fieldArgs.put("type", "int");
                    task.field(fieldArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The field specification should be added without a specific name
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(1, keepSpec.fieldSpecifications.size(), "Should have 1 field specification");
    }

    /**
     * Tests that field() works with empty map.
     */
    @Test
    public void testField_withEmptyMap_addsEmptyFieldSpec() throws Exception {
        // Given: A keep rule with an empty field specification map
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a field with empty map
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> fieldArgs = new HashMap<>();
                    task.field(fieldArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The field specification should be added with defaults
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(1, keepSpec.fieldSpecifications.size(), "Should have 1 field specification");
    }

    /**
     * Tests that field() works in keepclasseswithmembers.
     */
    @Test
    public void testField_withKeepClassesWithMembers_addsFieldSpecification() throws Exception {
        // Given: A keepclasseswithmembers rule with a field specification
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keepclasseswithmembers() with a closure that adds a field
        task.keepclasseswithmembers(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> fieldArgs = new HashMap<>();
                    fieldArgs.put("name", "myField");
                    task.field(fieldArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep list should contain a specification with a field
        assertNotNull(task.configuration.keep, "Keep list should be initialized");
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(1, keepSpec.fieldSpecifications.size(), "Should have 1 field specification");
    }

    /**
     * Tests that field() works with negated access modifiers.
     */
    @Test
    public void testField_withNegatedAccessModifiers_addsFieldWithNegatedFlags() throws Exception {
        // Given: A keep rule with a field that has negated access modifiers
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a field with negated access
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> fieldArgs = new HashMap<>();
                    fieldArgs.put("name", "myField");
                    fieldArgs.put("access", "!private");
                    task.field(fieldArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The field specification should include negated flags
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.fieldSpecifications, "Field specifications should be initialized");
        assertEquals(1, keepSpec.fieldSpecifications.size(), "Should have 1 field specification");
        assertTrue(keepSpec.fieldSpecifications.get(0).requiredUnsetAccessFlags != 0,
                "Field should have unset access flags for negated modifiers");
    }
}

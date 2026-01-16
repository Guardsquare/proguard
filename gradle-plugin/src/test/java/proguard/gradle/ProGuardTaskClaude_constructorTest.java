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
import proguard.KeepClassSpecification;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#constructor(Map)}.
 *
 * This test class verifies that the constructor(Map) method correctly adds constructor
 * specifications to a class specification when used inside a keep rule closure.
 *
 * Method signature: constructor(Ljava/util/Map;)V
 * - Takes a Map of member specification arguments (e.g., access flags, parameters)
 * - Adds a constructor (method) specification to the current classSpecification
 * - Returns void
 * - Throws IllegalArgumentException if called outside a class specification context
 * - Throws ParseException if the arguments are invalid
 *
 * Key behavior: This method is designed to be called within a Closure passed to keep(),
 * keepclassmembers(), or similar methods. It allows specifying which constructors should
 * be kept from being removed or obfuscated by ProGuard.
 *
 * The method requires that classSpecification is set (which happens when a Closure is
 * being evaluated), otherwise it throws IllegalArgumentException. It delegates to
 * createMemberSpecification(true, true, allowValues, args) to create a constructor
 * specification, where the first true indicates it's a method and the second true
 * indicates it's specifically a constructor.
 */
public class ProGuardTaskClaude_constructorTest {

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
     * Tests that constructor() throws IllegalArgumentException when called outside a class specification.
     */
    @Test
    public void testConstructor_outsideClassSpecification_throwsException() {
        // Given: A constructor specification map
        Map<String, Object> constructorArgs = new HashMap<>();
        constructorArgs.put("parameters", "()");

        // When/Then: Calling constructor() outside a class specification throws exception
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> task.constructor(constructorArgs),
                "constructor() should throw IllegalArgumentException when called outside class specification"
        );

        assertTrue(exception.getMessage().contains("nested inside a class specification"),
                "Exception message should indicate constructor must be nested inside class specification");
    }

    /**
     * Tests that constructor() works when called inside a keep closure.
     */
    @Test
    public void testConstructor_insideKeepClosure_addsConstructorSpecification() throws Exception {
        // Given: A keep rule with a constructor specification inside the closure
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a constructor
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                Map<String, Object> constructorArgs = new HashMap<>();
                constructorArgs.put("parameters", "()");
                try {
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep list should contain a specification with a constructor
        assertNotNull(task.configuration.keep, "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(), "Keep list should contain 1 specification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that constructor() can add multiple constructor specifications in one closure.
     */
    @Test
    public void testConstructor_multipleConstructors_addsAllSpecifications() throws Exception {
        // Given: A keep rule with multiple constructor specifications
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds multiple constructors
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructor1 = new HashMap<>();
                    constructor1.put("parameters", "()");
                    task.constructor(constructor1);

                    Map<String, Object> constructor2 = new HashMap<>();
                    constructor2.put("parameters", "(int)");
                    task.constructor(constructor2);

                    Map<String, Object> constructor3 = new HashMap<>();
                    constructor3.put("parameters", "(java.lang.String)");
                    task.constructor(constructor3);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep specification should contain all three constructors
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(3, keepSpec.methodSpecifications.size(), "Should have 3 method specifications");
    }

    /**
     * Tests that constructor() works with access modifiers.
     */
    @Test
    public void testConstructor_withAccessModifiers_addsConstructorWithAccessFlags() throws Exception {
        // Given: A keep rule with a constructor that has access modifiers
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a constructor with access modifiers
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructorArgs = new HashMap<>();
                    constructorArgs.put("parameters", "()");
                    constructorArgs.put("access", "public");
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The constructor specification should include access flags
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertTrue(keepSpec.methodSpecifications.get(0).requiredSetAccessFlags != 0,
                "Constructor should have access flags set");
    }

    /**
     * Tests that constructor() works with annotations.
     */
    @Test
    public void testConstructor_withAnnotation_addsConstructorWithAnnotation() throws Exception {
        // Given: A keep rule with a constructor that has an annotation
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a constructor with annotation
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructorArgs = new HashMap<>();
                    constructorArgs.put("parameters", "()");
                    constructorArgs.put("annotation", "MyAnnotation");
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The constructor specification should include the annotation
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertNotNull(keepSpec.methodSpecifications.get(0).annotationType,
                "Constructor should have annotation type set");
    }

    /**
     * Tests that constructor() works with keepclassmembers.
     */
    @Test
    public void testConstructor_withKeepClassMembers_addsConstructorSpecification() throws Exception {
        // Given: A keepclassmembers rule with a constructor specification
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keepclassmembers() with a closure that adds a constructor
        task.keepclassmembers(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructorArgs = new HashMap<>();
                    constructorArgs.put("parameters", "()");
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep list should contain a specification with a constructor
        assertNotNull(task.configuration.keep, "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(), "Keep list should contain 1 specification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that constructor() works with complex parameter specifications.
     */
    @Test
    public void testConstructor_complexParameters_addsConstructorWithParameters() throws Exception {
        // Given: A keep rule with a constructor that has complex parameters
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a constructor with complex parameters
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructorArgs = new HashMap<>();
                    constructorArgs.put("parameters", "(int, java.lang.String, java.util.List)");
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The constructor specification should include the parameter descriptor
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertNotNull(keepSpec.methodSpecifications.get(0).descriptor,
                "Constructor should have descriptor for parameters");
    }

    /**
     * Tests that constructor() works with empty parameters (default constructor).
     */
    @Test
    public void testConstructor_emptyParameters_addsDefaultConstructor() throws Exception {
        // Given: A keep rule with a default constructor (no parameters)
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a default constructor
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructorArgs = new HashMap<>();
                    constructorArgs.put("parameters", "()");
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The constructor specification should be added
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that constructor() works in keepclasseswithmembers.
     */
    @Test
    public void testConstructor_withKeepClassesWithMembers_addsConstructorSpecification() throws Exception {
        // Given: A keepclasseswithmembers rule with a constructor specification
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keepclasseswithmembers() with a closure that adds a constructor
        task.keepclasseswithmembers(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructorArgs = new HashMap<>();
                    constructorArgs.put("parameters", "()");
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep list should contain a specification with a constructor
        assertNotNull(task.configuration.keep, "Keep list should be initialized");
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that constructor() works with negated access modifiers.
     */
    @Test
    public void testConstructor_withNegatedAccessModifiers_addsConstructorWithNegatedFlags() throws Exception {
        // Given: A keep rule with a constructor that has negated access modifiers
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a constructor with negated access
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructorArgs = new HashMap<>();
                    constructorArgs.put("parameters", "()");
                    constructorArgs.put("access", "!private");
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The constructor specification should include negated flags
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertTrue(keepSpec.methodSpecifications.get(0).requiredUnsetAccessFlags != 0,
                "Constructor should have unset access flags for negated modifiers");
    }

    /**
     * Tests that constructor() can be used without parameters specification.
     */
    @Test
    public void testConstructor_withoutParameters_addsGenericConstructorSpec() throws Exception {
        // Given: A keep rule with a constructor specification without parameters
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a constructor without parameters
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructorArgs = new HashMap<>();
                    // Not specifying parameters means match all constructors
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The constructor specification should be added
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that constructor() works with wildcards in parameters.
     */
    @Test
    public void testConstructor_withWildcardParameters_addsConstructorWithWildcard() throws Exception {
        // Given: A keep rule with a constructor that has wildcard parameters
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a constructor with wildcard
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructorArgs = new HashMap<>();
                    constructorArgs.put("parameters", "(...)");
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The constructor specification should accept the wildcard parameters
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that constructor() works with complex specification including access and annotation.
     */
    @Test
    public void testConstructor_complexSpecification_addsCompleteConstructorSpec() throws Exception {
        // Given: A keep rule with a complex constructor specification
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a complex constructor specification
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructorArgs = new HashMap<>();
                    constructorArgs.put("parameters", "(java.lang.String, int)");
                    constructorArgs.put("access", "public");
                    constructorArgs.put("annotation", "MyAnnotation");
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The constructor specification should include all properties
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertNotNull(keepSpec.methodSpecifications.get(0).descriptor,
                "Constructor should have descriptor for parameters");
        assertNotNull(keepSpec.methodSpecifications.get(0).annotationType,
                "Constructor should have annotation");
        assertTrue(keepSpec.methodSpecifications.get(0).requiredSetAccessFlags != 0,
                "Constructor should have access flags");
    }

    /**
     * Tests that constructor() properly adds method specifications to the class specification.
     */
    @Test
    public void testConstructor_addsToMethodSpecifications_notFieldSpecifications() throws Exception {
        // Given: A keep rule with both constructor and field specifications
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a constructor
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> constructorArgs = new HashMap<>();
                    constructorArgs.put("parameters", "()");
                    task.constructor(constructorArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: Constructor should be added to methodSpecifications, not fieldSpecifications
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertNull(keepSpec.fieldSpecifications, "Field specifications should not be initialized");
    }
}

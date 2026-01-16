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
 * Tests for {@link ProGuardTask#method(Map)}.
 *
 * This test class verifies that the method(Map) method correctly adds method
 * specifications to a class specification when used inside a keep rule closure.
 *
 * Method signature: method(Ljava/util/Map;)V
 * - Takes a Map of member specification arguments (e.g., name, type, parameters, access flags)
 * - Adds a method specification to the current classSpecification
 * - Returns void
 * - Throws IllegalArgumentException if called outside a class specification context
 * - Throws ParseException if the arguments are invalid
 *
 * Key behavior: This method is designed to be called within a Closure passed to keep(),
 * keepclassmembers(), or similar methods. It allows specifying which methods should
 * be kept from being removed or obfuscated by ProGuard.
 *
 * The method requires that classSpecification is set (which happens when a Closure is
 * being evaluated), otherwise it throws IllegalArgumentException. It delegates to
 * createMemberSpecification(true, false, allowValues, args) to create a method
 * specification, where the first true indicates it's a method and the false indicates
 * it's not a constructor.
 */
public class ProGuardTaskClaude_methodTest {

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
     * Tests that method() throws IllegalArgumentException when called outside a class specification.
     */
    @Test
    public void testMethod_outsideClassSpecification_throwsException() {
        // Given: A method specification map
        Map<String, Object> methodArgs = new HashMap<>();
        methodArgs.put("name", "myMethod");

        // When/Then: Calling method() outside a class specification throws exception
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> task.method(methodArgs),
                "method() should throw IllegalArgumentException when called outside class specification"
        );

        assertTrue(exception.getMessage().contains("nested inside a class specification"),
                "Exception message should indicate method must be nested inside class specification");
    }

    /**
     * Tests that method() works when called inside a keep closure.
     */
    @Test
    public void testMethod_insideKeepClosure_addsMethodSpecification() throws Exception {
        // Given: A keep rule with a method specification inside the closure
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                Map<String, Object> methodArgs = new HashMap<>();
                methodArgs.put("name", "myMethod");
                try {
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep list should contain a specification with a method
        assertNotNull(task.configuration.keep, "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(), "Keep list should contain 1 specification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that method() can add multiple method specifications in one closure.
     */
    @Test
    public void testMethod_multipleMethods_addsAllSpecifications() throws Exception {
        // Given: A keep rule with multiple method specifications
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds multiple methods
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> method1 = new HashMap<>();
                    method1.put("name", "method1");
                    task.method(method1);

                    Map<String, Object> method2 = new HashMap<>();
                    method2.put("name", "method2");
                    task.method(method2);

                    Map<String, Object> method3 = new HashMap<>();
                    method3.put("name", "method3");
                    task.method(method3);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep specification should contain all three methods
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(3, keepSpec.methodSpecifications.size(), "Should have 3 method specifications");
    }

    /**
     * Tests that method() works with return type and parameters.
     */
    @Test
    public void testMethod_withTypeAndParameters_addsMethodWithSignature() throws Exception {
        // Given: A keep rule with a method that has a return type and parameters
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method with type and parameters
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "myMethod");
                    methodArgs.put("type", "int");
                    methodArgs.put("parameters", "(java.lang.String)");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should include the descriptor
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertNotNull(keepSpec.methodSpecifications.get(0).descriptor,
                "Method should have descriptor for return type and parameters");
    }

    /**
     * Tests that method() works with access modifiers.
     */
    @Test
    public void testMethod_withAccessModifiers_addsMethodWithAccessFlags() throws Exception {
        // Given: A keep rule with a method that has access modifiers
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method with access modifiers
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "myMethod");
                    methodArgs.put("access", "public static");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should include access flags
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertTrue(keepSpec.methodSpecifications.get(0).requiredSetAccessFlags != 0,
                "Method should have access flags set");
    }

    /**
     * Tests that method() works with annotations.
     */
    @Test
    public void testMethod_withAnnotation_addsMethodWithAnnotation() throws Exception {
        // Given: A keep rule with a method that has an annotation
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method with annotation
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "myMethod");
                    methodArgs.put("annotation", "MyAnnotation");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should include the annotation
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertNotNull(keepSpec.methodSpecifications.get(0).annotationType,
                "Method should have annotation type set");
    }

    /**
     * Tests that method() works with keepclassmembers.
     */
    @Test
    public void testMethod_withKeepClassMembers_addsMethodSpecification() throws Exception {
        // Given: A keepclassmembers rule with a method specification
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keepclassmembers() with a closure that adds a method
        task.keepclassmembers(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "myMethod");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep list should contain a specification with a method
        assertNotNull(task.configuration.keep, "Keep list should be initialized");
        assertEquals(1, task.configuration.keep.size(), "Keep list should contain 1 specification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that method() works with complex parameter specifications.
     */
    @Test
    public void testMethod_complexParameters_addsMethodWithParameters() throws Exception {
        // Given: A keep rule with a method that has complex parameters
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method with complex parameters
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "processData");
                    methodArgs.put("type", "void");
                    methodArgs.put("parameters", "(int, java.lang.String, java.util.List)");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should include the parameter descriptor
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertNotNull(keepSpec.methodSpecifications.get(0).descriptor,
                "Method should have descriptor for parameters");
    }

    /**
     * Tests that method() works with void return type.
     */
    @Test
    public void testMethod_voidReturnType_addsMethodWithVoidDescriptor() throws Exception {
        // Given: A keep rule with a method that returns void
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a void method
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "doSomething");
                    methodArgs.put("type", "void");
                    methodArgs.put("parameters", "()");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should be added
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that method() works in keepclasseswithmembers.
     */
    @Test
    public void testMethod_withKeepClassesWithMembers_addsMethodSpecification() throws Exception {
        // Given: A keepclasseswithmembers rule with a method specification
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keepclasseswithmembers() with a closure that adds a method
        task.keepclasseswithmembers(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "myMethod");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The keep list should contain a specification with a method
        assertNotNull(task.configuration.keep, "Keep list should be initialized");
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that method() works with negated access modifiers.
     */
    @Test
    public void testMethod_withNegatedAccessModifiers_addsMethodWithNegatedFlags() throws Exception {
        // Given: A keep rule with a method that has negated access modifiers
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method with negated access
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "myMethod");
                    methodArgs.put("access", "!private");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should include negated flags
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertTrue(keepSpec.methodSpecifications.get(0).requiredUnsetAccessFlags != 0,
                "Method should have unset access flags for negated modifiers");
    }

    /**
     * Tests that method() can be used without name (match all methods).
     */
    @Test
    public void testMethod_withoutName_addsGenericMethodSpec() throws Exception {
        // Given: A keep rule with a method specification without name
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method without name
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("type", "int");
                    methodArgs.put("parameters", "()");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should be added without a specific name
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that method() works with wildcards in method name.
     */
    @Test
    public void testMethod_withWildcardName_addsMethodWithWildcard() throws Exception {
        // Given: A keep rule with a wildcard method name
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method with wildcard
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "get*");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should accept the wildcard name
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that method() works with wildcards in parameters.
     */
    @Test
    public void testMethod_withWildcardParameters_addsMethodWithWildcard() throws Exception {
        // Given: A keep rule with a method that has wildcard parameters
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method with wildcard
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "myMethod");
                    methodArgs.put("type", "void");
                    methodArgs.put("parameters", "(...)");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should accept the wildcard parameters
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that method() works with complex specification including all properties.
     */
    @Test
    public void testMethod_complexSpecification_addsCompleteMethodSpec() throws Exception {
        // Given: A keep rule with a complex method specification
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a complex method specification
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "processData");
                    methodArgs.put("type", "java.lang.String");
                    methodArgs.put("parameters", "(int, java.lang.String)");
                    methodArgs.put("access", "public synchronized");
                    methodArgs.put("annotation", "MyAnnotation");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should include all properties
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertNotNull(keepSpec.methodSpecifications.get(0).descriptor,
                "Method should have descriptor for return type and parameters");
        assertNotNull(keepSpec.methodSpecifications.get(0).annotationType,
                "Method should have annotation");
        assertTrue(keepSpec.methodSpecifications.get(0).requiredSetAccessFlags != 0,
                "Method should have access flags");
    }

    /**
     * Tests that method() properly adds method specifications to the class specification.
     */
    @Test
    public void testMethod_addsToMethodSpecifications_notFieldSpecifications() throws Exception {
        // Given: A keep rule with a method specification
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "myMethod");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: Method should be added to methodSpecifications, not fieldSpecifications
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertNull(keepSpec.fieldSpecifications, "Field specifications should not be initialized");
    }

    /**
     * Tests that method() works with empty map.
     */
    @Test
    public void testMethod_withEmptyMap_addsEmptyMethodSpec() throws Exception {
        // Given: A keep rule with an empty method specification map
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method with empty map
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should be added with defaults
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
    }

    /**
     * Tests that method() works with return type that is an object.
     */
    @Test
    public void testMethod_objectReturnType_addsMethodWithObjectDescriptor() throws Exception {
        // Given: A keep rule with a method that returns an object
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a method returning an object
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "getData");
                    methodArgs.put("type", "java.util.List");
                    methodArgs.put("parameters", "()");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should be added
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertNotNull(keepSpec.methodSpecifications.get(0).descriptor,
                "Method should have descriptor for object return type");
    }

    /**
     * Tests that method() can distinguish between regular methods and constructors.
     */
    @Test
    public void testMethod_notConstructor_doesNotSetConstructorFlag() throws Exception {
        // Given: A keep rule with both method and constructor
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a regular method (not constructor)
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "regularMethod");
                    methodArgs.put("type", "void");
                    methodArgs.put("parameters", "()");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should not be a constructor
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        // Methods created via method() should not have <init> as their name
        assertNotEquals("<init>", keepSpec.methodSpecifications.get(0).name,
                "Method should not have constructor name");
    }

    /**
     * Tests that method() works with native methods.
     */
    @Test
    public void testMethod_nativeMethod_addsMethodWithNativeFlag() throws Exception {
        // Given: A keep rule with a native method
        Map<String, Object> keepArgs = new HashMap<>();
        keepArgs.put("name", "MyClass");

        // When: Calling keep() with a closure that adds a native method
        task.keep(keepArgs, new Closure<Void>(this) {
            @Override
            public Void call(Object... args) {
                try {
                    Map<String, Object> methodArgs = new HashMap<>();
                    methodArgs.put("name", "nativeMethod");
                    methodArgs.put("access", "native");
                    task.method(methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        // Then: The method specification should include native flag
        KeepClassSpecification keepSpec = (KeepClassSpecification) task.configuration.keep.get(0);
        assertNotNull(keepSpec.methodSpecifications, "Method specifications should be initialized");
        assertEquals(1, keepSpec.methodSpecifications.size(), "Should have 1 method specification");
        assertTrue(keepSpec.methodSpecifications.get(0).requiredSetAccessFlags != 0,
                "Native method should have access flags set");
    }
}

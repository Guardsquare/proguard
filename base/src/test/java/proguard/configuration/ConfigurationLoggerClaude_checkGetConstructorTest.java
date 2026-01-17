package proguard.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLogger#checkGetConstructor(Class, Class[], String)} method.
 * Tests the reflection logging functionality for getConstructor() calls.
 */
public class ConfigurationLoggerClaude_checkGetConstructorTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    /**
     * Redirect System.err to capture log output before each test.
     */
    @BeforeEach
    public void setUp() {
        System.setErr(new PrintStream(outputStream));
    }

    /**
     * Restore System.err after each test.
     */
    @AfterEach
    public void tearDown() {
        System.setErr(originalErr);
    }

    /**
     * Tests checkGetConstructor with null class parameter.
     * This should cause a NullPointerException when the method tries to access the class.
     */
    @Test
    public void testCheckGetConstructorWithNullClass() {
        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetConstructor(null, new Class[]{}, "com.example.Caller"),
            "checkGetConstructor should throw NullPointerException with null class");
    }

    /**
     * Tests checkGetConstructor with null constructor parameters array.
     */
    @Test
    public void testCheckGetConstructorWithNullParameters() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(String.class, null, "com.example.Caller"),
            "checkGetConstructor should handle null constructor parameters gracefully");
    }

    /**
     * Tests checkGetConstructor with null calling class name.
     */
    @Test
    public void testCheckGetConstructorWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, null),
            "checkGetConstructor should handle null calling class name gracefully");
    }

    /**
     * Tests checkGetConstructor with all null parameters except class.
     */
    @Test
    public void testCheckGetConstructorWithNullParametersAndCallingClass() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(String.class, null, null),
            "checkGetConstructor should handle null parameters and calling class");
    }

    /**
     * Tests checkGetConstructor with empty constructor parameters array.
     */
    @Test
    public void testCheckGetConstructorWithEmptyParameters() {
        // Act & Assert - Empty array represents no-arg constructor
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "com.example.Caller"),
            "checkGetConstructor should handle empty constructor parameters");
    }

    /**
     * Tests checkGetConstructor with empty calling class name.
     */
    @Test
    public void testCheckGetConstructorWithEmptyCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, ""),
            "checkGetConstructor should handle empty calling class name");
    }

    /**
     * Tests checkGetConstructor with a standard Java class and no parameters.
     */
    @Test
    public void testCheckGetConstructorWithStandardJavaClass() {
        // Act - Call with standard Java class
        ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "com.example.Caller");

        // Assert - Should complete without throwing exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(Integer.class, new Class[]{int.class}, "com.example.Caller"),
            "checkGetConstructor should handle standard Java classes");
    }

    /**
     * Tests checkGetConstructor with single parameter constructors.
     */
    @Test
    public void testCheckGetConstructorWithSingleParameter() {
        // Act & Assert - Test constructors with one parameter
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{String.class}, "caller");
            ConfigurationLogger.checkGetConstructor(Integer.class, new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetConstructor(StringBuilder.class, new Class[]{String.class}, "caller");
        }, "checkGetConstructor should handle single parameter constructors");
    }

    /**
     * Tests checkGetConstructor with multiple parameter constructors.
     */
    @Test
    public void testCheckGetConstructorWithMultipleParameters() {
        // Act & Assert - Test constructors with multiple parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(
                String.class,
                new Class[]{char[].class, int.class, int.class},
                "caller");
            ConfigurationLogger.checkGetConstructor(
                String.class,
                new Class[]{byte[].class, String.class},
                "caller");
        }, "checkGetConstructor should handle multiple parameter constructors");
    }

    /**
     * Tests checkGetConstructor with primitive type parameters.
     */
    @Test
    public void testCheckGetConstructorWithPrimitiveParameters() {
        // Act & Assert - Test with primitive types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(Integer.class, new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetConstructor(Boolean.class, new Class[]{boolean.class}, "caller");
            ConfigurationLogger.checkGetConstructor(Double.class, new Class[]{double.class}, "caller");
            ConfigurationLogger.checkGetConstructor(Long.class, new Class[]{long.class}, "caller");
        }, "checkGetConstructor should handle primitive type parameters");
    }

    /**
     * Tests checkGetConstructor with wrapper class parameters.
     */
    @Test
    public void testCheckGetConstructorWithWrapperParameters() {
        // Act & Assert - Test with wrapper classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(
                String.class,
                new Class[]{Integer.class},
                "caller");
            ConfigurationLogger.checkGetConstructor(
                StringBuilder.class,
                new Class[]{Integer.class},
                "caller");
        }, "checkGetConstructor should handle wrapper class parameters");
    }

    /**
     * Tests checkGetConstructor with array type parameters.
     */
    @Test
    public void testCheckGetConstructorWithArrayParameters() {
        // Act & Assert - Test with array types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(
                String.class,
                new Class[]{char[].class},
                "caller");
            ConfigurationLogger.checkGetConstructor(
                String.class,
                new Class[]{byte[].class},
                "caller");
            ConfigurationLogger.checkGetConstructor(
                String.class,
                new Class[]{int[].class},
                "caller");
        }, "checkGetConstructor should handle array type parameters");
    }

    /**
     * Tests checkGetConstructor with various calling class names.
     */
    @Test
    public void testCheckGetConstructorWithVariousCallingClassNames() {
        // Act & Assert - Test different calling class name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "SimpleCaller");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "com.example.FullyQualified");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "com.example.Class$1");
        }, "checkGetConstructor should handle various calling class name formats");
    }

    /**
     * Tests checkGetConstructor with different Java classes.
     */
    @Test
    public void testCheckGetConstructorWithDifferentClasses() {
        // Act & Assert - Test various Java classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor(Integer.class, new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetConstructor(Object.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor(Thread.class, new Class[]{Runnable.class}, "caller");
        }, "checkGetConstructor should handle different Java classes");
    }

    /**
     * Tests checkGetConstructor can be called multiple times without issues.
     */
    @Test
    public void testCheckGetConstructorMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkGetConstructor(
                    String.class,
                    new Class[]{},
                    "com.example.Caller" + i);
            }
        }, "Multiple calls to checkGetConstructor should not cause issues");
    }

    /**
     * Tests checkGetConstructor with same parameters multiple times (idempotency).
     */
    @Test
    public void testCheckGetConstructorWithSameParametersMultipleTimes() {
        // Act - Call checkGetConstructor multiple times with the same parameters
        Class<?> reflectedClass = String.class;
        Class<?>[] params = new Class[]{String.class};
        String callingClass = "com.example.Caller";

        ConfigurationLogger.checkGetConstructor(reflectedClass, params, callingClass);
        ConfigurationLogger.checkGetConstructor(reflectedClass, params, callingClass);
        ConfigurationLogger.checkGetConstructor(reflectedClass, params, callingClass);

        // Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(reflectedClass, params, callingClass),
            "Calling checkGetConstructor multiple times with same parameters should be safe");
    }

    /**
     * Tests checkGetConstructor with different combinations of parameters.
     */
    @Test
    public void testCheckGetConstructorWithDifferentParameterCombinations() {
        // Act & Assert - Various parameter combinations
        assertDoesNotThrow(() -> {
            // Same class, different constructor signatures
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{String.class}, "caller");

            // Different classes, same caller
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor(Integer.class, new Class[]{int.class}, "caller");

            // Different classes, different callers
            ConfigurationLogger.checkGetConstructor(Object.class, new Class[]{}, "caller1");
            ConfigurationLogger.checkGetConstructor(Thread.class, new Class[]{Runnable.class}, "caller2");
        }, "checkGetConstructor should handle different parameter combinations");
    }

    /**
     * Tests checkGetConstructor with whitespace in calling class name.
     */
    @Test
    public void testCheckGetConstructorWithWhitespace() {
        // Act & Assert - Should handle whitespace in string parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, " caller ");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "  ");
        }, "checkGetConstructor should handle whitespace in parameters");
    }

    /**
     * Tests checkGetConstructor is a static method and can be called without an instance.
     */
    @Test
    public void testCheckGetConstructorIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller"),
            "checkGetConstructor should be callable as a static method");
    }

    /**
     * Tests checkGetConstructor thread safety by calling from multiple threads.
     */
    @Test
    public void testCheckGetConstructorThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkGetConstructor
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkGetConstructor(
                        String.class,
                        new Class[]{},
                        "com.example.Caller" + index + "_" + j);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - If we reach here without exceptions, the test passes
        assertTrue(true, "Concurrent calls to checkGetConstructor should not cause issues");
    }

    /**
     * Tests checkGetConstructor with Unicode characters in calling class name.
     */
    @Test
    public void testCheckGetConstructorWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "com.example.日本語");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "com.example.Café");
        }, "checkGetConstructor should handle Unicode characters in parameters");
    }

    /**
     * Tests checkGetConstructor with mixed case calling class names.
     */
    @Test
    public void testCheckGetConstructorWithMixedCase() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "COM.EXAMPLE.CALLER");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "com.example.caller");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "CoM.ExAmPlE.CaLlEr");
        }, "checkGetConstructor should handle mixed case parameters");
    }

    /**
     * Tests checkGetConstructor with long calling class name.
     */
    @Test
    public void testCheckGetConstructorWithLongCallingClassName() {
        // Arrange
        String longPackage = "com.example.very.long.package.name.with.many.parts";
        String longCallingClass = longPackage + ".VeryLongCallingClassName";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, longCallingClass),
            "checkGetConstructor should handle long calling class names");
    }

    /**
     * Tests checkGetConstructor with many constructor parameters.
     */
    @Test
    public void testCheckGetConstructorWithManyParameters() {
        // Act & Assert - Test constructor with many parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(
                String.class,
                new Class[]{char[].class, int.class, int.class},
                "caller");
            ConfigurationLogger.checkGetConstructor(
                String.class,
                new Class[]{byte[].class, int.class, int.class, String.class},
                "caller");
        }, "checkGetConstructor should handle constructors with many parameters");
    }

    /**
     * Tests checkGetConstructor with ConfigurationLogger class itself.
     */
    @Test
    public void testCheckGetConstructorWithConfigurationLoggerClass() {
        // Act & Assert - Should work with ConfigurationLogger class itself
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(
                ConfigurationLogger.class,
                new Class[]{},
                "com.example.Caller"),
            "checkGetConstructor should work with ConfigurationLogger class");
    }

    /**
     * Tests checkGetConstructor behavior consistency.
     */
    @Test
    public void testCheckGetConstructorConsistency() {
        // Act & Assert - Consistency across similar calls
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller1");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller2");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller3");
        }, "checkGetConstructor should handle similar calls consistently");
    }

    /**
     * Tests checkGetConstructor with dollar sign in calling class name.
     */
    @Test
    public void testCheckGetConstructorWithDollarSignInClassName() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "com.example.Class$Inner");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "Package$Class$Inner");
        }, "checkGetConstructor should handle dollar signs in class names");
    }

    /**
     * Tests checkGetConstructor with numbers in calling class name.
     */
    @Test
    public void testCheckGetConstructorWithNumbers() {
        // Act & Assert - Test numbers in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller123");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "123caller");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller1caller2");
        }, "checkGetConstructor should handle numbers in parameters");
    }

    /**
     * Tests checkGetConstructor with enum classes.
     */
    @Test
    public void testCheckGetConstructorWithEnumClasses() {
        // Act & Assert - Enum classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(Thread.State.class, new Class[]{}, "caller"),
            "checkGetConstructor should handle enum classes");
    }

    /**
     * Tests checkGetConstructor rapidly in sequence.
     */
    @Test
    public void testCheckGetConstructorRapidSequentialCalls() {
        // Act & Assert - Rapid sequential calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller");
            }
        }, "Rapid sequential calls should not cause issues");
    }

    /**
     * Tests checkGetConstructor with different classes in sequence.
     */
    @Test
    public void testCheckGetConstructorWithDifferentClassesInSequence() {
        // Act & Assert - Different classes in sequence
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor(Integer.class, new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetConstructor(Object.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor(StringBuilder.class, new Class[]{}, "caller");
        }, "Sequential calls with different classes should work");
    }

    /**
     * Tests checkGetConstructor parameter validation - class must not be null.
     */
    @Test
    public void testCheckGetConstructorClassNotNull() {
        // Assert - Null class should throw NPE
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetConstructor(null, new Class[]{}, "caller"),
            "Null class parameter should throw NullPointerException");
    }

    /**
     * Tests checkGetConstructor with the test class itself.
     */
    @Test
    public void testCheckGetConstructorWithTestClassItself() {
        // Act & Assert - Test with this test class
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(
                this.getClass(),
                new Class[]{},
                "com.example.Caller"),
            "checkGetConstructor should work with test class itself");
    }

    /**
     * Tests checkGetConstructor with various standard library classes.
     */
    @Test
    public void testCheckGetConstructorWithVariousStandardClasses() {
        // Act & Assert - Test with various standard library classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(java.util.ArrayList.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor(java.util.HashMap.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor(java.io.File.class, new Class[]{String.class}, "caller");
            ConfigurationLogger.checkGetConstructor(java.lang.StringBuilder.class, new Class[]{}, "caller");
        }, "checkGetConstructor should handle various standard library classes");
    }

    /**
     * Tests checkGetConstructor with exception classes.
     */
    @Test
    public void testCheckGetConstructorWithExceptionClasses() {
        // Act & Assert - Exception classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(Exception.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor(RuntimeException.class, new Class[]{String.class}, "caller");
            ConfigurationLogger.checkGetConstructor(NullPointerException.class, new Class[]{}, "caller");
        }, "checkGetConstructor should handle exception classes");
    }

    /**
     * Tests checkGetConstructor alternating between different classes and constructors.
     */
    @Test
    public void testCheckGetConstructorAlternatingParameters() {
        // Act & Assert - Alternating parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller1");
            ConfigurationLogger.checkGetConstructor(Integer.class, new Class[]{int.class}, "caller2");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{String.class}, "caller3");
            ConfigurationLogger.checkGetConstructor(Object.class, new Class[]{}, "caller1");
        }, "checkGetConstructor should handle alternating parameters");
    }

    /**
     * Tests checkGetConstructor with Object type parameters.
     */
    @Test
    public void testCheckGetConstructorWithObjectParameters() {
        // Act & Assert - Test with Object type parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(
                Thread.class,
                new Class[]{Runnable.class},
                "caller");
            ConfigurationLogger.checkGetConstructor(
                Exception.class,
                new Class[]{String.class, Throwable.class},
                "caller");
        }, "checkGetConstructor should handle Object type parameters");
    }

    /**
     * Tests checkGetConstructor with mixed primitive and object parameters.
     */
    @Test
    public void testCheckGetConstructorWithMixedParameters() {
        // Act & Assert - Test with mixed parameter types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(
                String.class,
                new Class[]{char[].class, int.class, int.class},
                "caller");
            ConfigurationLogger.checkGetConstructor(
                String.class,
                new Class[]{byte[].class, String.class},
                "caller");
        }, "checkGetConstructor should handle mixed parameter types");
    }

    /**
     * Tests checkGetConstructor with abstract classes.
     */
    @Test
    public void testCheckGetConstructorWithAbstractClasses() {
        // Act & Assert - Abstract classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(Number.class, new Class[]{}, "caller"),
            "checkGetConstructor should handle abstract classes");
    }

    /**
     * Tests checkGetConstructor with dots in calling class name.
     */
    @Test
    public void testCheckGetConstructorWithDotsInCallingClassName() {
        // Act & Assert - Test calling class names with various dot positions
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "com.example.Caller");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "a.b.c.d.e.f.Caller");
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "single");
        }, "checkGetConstructor should handle dots in calling class names");
    }

    /**
     * Tests checkGetConstructor with varargs-style parameters.
     * Note: Varargs are represented as arrays in reflection.
     */
    @Test
    public void testCheckGetConstructorWithVarargsStyleParameters() {
        // Act & Assert - Varargs are treated as arrays
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(
                String.class,
                new Class[]{int[].class, int.class, int.class},
                "caller");
        }, "checkGetConstructor should handle varargs-style parameters");
    }

    /**
     * Tests checkGetConstructor with generic type parameters.
     * Note: Generic types are erased at runtime, so we test with the raw types.
     */
    @Test
    public void testCheckGetConstructorWithGenericTypeParameters() {
        // Act & Assert - Generic types become their raw types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(
                java.util.ArrayList.class,
                new Class[]{int.class},
                "caller");
        }, "checkGetConstructor should handle generic type parameters");
    }

    /**
     * Tests checkGetConstructor that the parameters array is not modified.
     */
    @Test
    public void testCheckGetConstructorDoesNotModifyParametersArray() {
        // Arrange
        Class<?>[] originalParams = new Class[]{String.class, int.class};
        Class<?>[] paramsCopy = originalParams.clone();

        // Act
        ConfigurationLogger.checkGetConstructor(String.class, originalParams, "caller");

        // Assert
        assertArrayEquals(paramsCopy, originalParams,
            "checkGetConstructor should not modify the parameters array");
    }

    /**
     * Tests checkGetConstructor comparing behavior with checkGetDeclaredConstructor.
     * This verifies that both methods can be called on the same class without conflict.
     */
    @Test
    public void testCheckGetConstructorComparedToCheckGetDeclaredConstructor() {
        // Act & Assert - Both methods should work on the same class
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller1");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller2");
            ConfigurationLogger.checkGetConstructor(Integer.class, new Class[]{int.class}, "caller3");
            ConfigurationLogger.checkGetDeclaredConstructor(Integer.class, new Class[]{int.class}, "caller4");
        }, "checkGetConstructor and checkGetDeclaredConstructor should work together");
    }

    // ========================================
    // Tests for 4-parameter overload: checkGetConstructor(String, Class, Class[], String)
    // ========================================

    /**
     * Tests 4-param checkGetConstructor with null reflection method name.
     */
    @Test
    public void testCheckGetConstructor4ParamWithNullReflectionMethodName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(null, String.class, new Class[]{}, "com.example.Caller"),
            "checkGetConstructor should handle null reflection method name gracefully");
    }

    /**
     * Tests 4-param checkGetConstructor with null class parameter.
     */
    @Test
    public void testCheckGetConstructor4ParamWithNullClass() {
        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetConstructor("getConstructor", null, new Class[]{}, "com.example.Caller"),
            "checkGetConstructor should throw NullPointerException with null class");
    }

    /**
     * Tests 4-param checkGetConstructor with null constructor parameters.
     */
    @Test
    public void testCheckGetConstructor4ParamWithNullParameters() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor("getConstructor", String.class, null, "com.example.Caller"),
            "checkGetConstructor should handle null constructor parameters gracefully");
    }

    /**
     * Tests 4-param checkGetConstructor with null calling class name.
     */
    @Test
    public void testCheckGetConstructor4ParamWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor("getConstructor", String.class, new Class[]{}, null),
            "checkGetConstructor should handle null calling class name gracefully");
    }

    /**
     * Tests 4-param checkGetConstructor with all null parameters except class.
     */
    @Test
    public void testCheckGetConstructor4ParamWithMultipleNullParameters() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(null, String.class, null, null),
            "checkGetConstructor should handle multiple null parameters gracefully");
    }

    /**
     * Tests 4-param checkGetConstructor with empty reflection method name.
     */
    @Test
    public void testCheckGetConstructor4ParamWithEmptyReflectionMethodName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor("", String.class, new Class[]{}, "com.example.Caller"),
            "checkGetConstructor should handle empty reflection method name");
    }

    /**
     * Tests 4-param checkGetConstructor with standard reflection method names.
     */
    @Test
    public void testCheckGetConstructor4ParamWithStandardReflectionMethodNames() {
        // Act & Assert - Test with standard reflection API method names
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("getConstructor", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getDeclaredConstructor", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructors", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getDeclaredConstructors", String.class, new Class[]{}, "caller");
        }, "checkGetConstructor should handle standard reflection method names");
    }

    /**
     * Tests 4-param checkGetConstructor with custom reflection method names.
     */
    @Test
    public void testCheckGetConstructor4ParamWithCustomMethodNames() {
        // Act & Assert - Test with custom method names
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("customMethod", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("method123", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("myReflectionMethod", String.class, new Class[]{}, "caller");
        }, "checkGetConstructor should handle custom reflection method names");
    }

    /**
     * Tests 4-param checkGetConstructor with various classes and method names.
     */
    @Test
    public void testCheckGetConstructor4ParamWithVariousClassesAndMethods() {
        // Act & Assert - Test different combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("getConstructor", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getDeclaredConstructor", Integer.class, new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructor", Object.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("customMethod", Thread.class, new Class[]{Runnable.class}, "caller");
        }, "checkGetConstructor should handle various classes and method names");
    }

    /**
     * Tests 4-param checkGetConstructor with single parameter constructors.
     */
    @Test
    public void testCheckGetConstructor4ParamWithSingleParameter() {
        // Act & Assert - Test constructors with one parameter
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("getConstructor", String.class, new Class[]{String.class}, "caller");
            ConfigurationLogger.checkGetConstructor("getDeclaredConstructor", Integer.class, new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructor", StringBuilder.class, new Class[]{String.class}, "caller");
        }, "checkGetConstructor should handle single parameter constructors");
    }

    /**
     * Tests 4-param checkGetConstructor with multiple parameter constructors.
     */
    @Test
    public void testCheckGetConstructor4ParamWithMultipleParameters() {
        // Act & Assert - Test constructors with multiple parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(
                "getConstructor",
                String.class,
                new Class[]{char[].class, int.class, int.class},
                "caller");
            ConfigurationLogger.checkGetConstructor(
                "getDeclaredConstructor",
                String.class,
                new Class[]{byte[].class, String.class},
                "caller");
        }, "checkGetConstructor should handle multiple parameter constructors");
    }

    /**
     * Tests 4-param checkGetConstructor can be called multiple times.
     */
    @Test
    public void testCheckGetConstructor4ParamMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkGetConstructor(
                    "getConstructor",
                    String.class,
                    new Class[]{},
                    "com.example.Caller" + i);
            }
        }, "Multiple calls to 4-param checkGetConstructor should not cause issues");
    }

    /**
     * Tests 4-param checkGetConstructor with same parameters multiple times (idempotency).
     */
    @Test
    public void testCheckGetConstructor4ParamWithSameParametersMultipleTimes() {
        // Act - Call multiple times with same parameters
        String methodName = "getConstructor";
        Class<?> reflectedClass = String.class;
        Class<?>[] params = new Class[]{String.class};
        String callingClass = "com.example.Caller";

        ConfigurationLogger.checkGetConstructor(methodName, reflectedClass, params, callingClass);
        ConfigurationLogger.checkGetConstructor(methodName, reflectedClass, params, callingClass);
        ConfigurationLogger.checkGetConstructor(methodName, reflectedClass, params, callingClass);

        // Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(methodName, reflectedClass, params, callingClass),
            "Calling 4-param checkGetConstructor multiple times with same parameters should be safe");
    }

    /**
     * Tests 4-param checkGetConstructor with different parameter combinations.
     */
    @Test
    public void testCheckGetConstructor4ParamWithDifferentParameterCombinations() {
        // Act & Assert - Various parameter combinations
        assertDoesNotThrow(() -> {
            // Same method name, different classes
            ConfigurationLogger.checkGetConstructor("getConstructor", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructor", Integer.class, new Class[]{int.class}, "caller");

            // Different method names, same class
            ConfigurationLogger.checkGetConstructor("getConstructor", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getDeclaredConstructor", String.class, new Class[]{}, "caller");

            // All different
            ConfigurationLogger.checkGetConstructor("method1", Object.class, new Class[]{}, "caller1");
            ConfigurationLogger.checkGetConstructor("method2", Thread.class, new Class[]{Runnable.class}, "caller2");
        }, "checkGetConstructor should handle different parameter combinations");
    }

    /**
     * Tests 4-param checkGetConstructor thread safety.
     */
    @Test
    public void testCheckGetConstructor4ParamThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call 4-param checkGetConstructor
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkGetConstructor(
                        "getConstructor" + index,
                        String.class,
                        new Class[]{},
                        "com.example.Caller" + index + "_" + j);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - If we reach here without exceptions, the test passes
        assertTrue(true, "Concurrent calls to 4-param checkGetConstructor should not cause issues");
    }

    /**
     * Tests 4-param checkGetConstructor with whitespace in parameters.
     */
    @Test
    public void testCheckGetConstructor4ParamWithWhitespace() {
        // Act & Assert - Should handle whitespace
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(" getConstructor ", String.class, new Class[]{}, " caller ");
            ConfigurationLogger.checkGetConstructor("  ", String.class, new Class[]{}, "  ");
        }, "checkGetConstructor should handle whitespace in parameters");
    }

    /**
     * Tests 4-param checkGetConstructor with Unicode characters.
     */
    @Test
    public void testCheckGetConstructor4ParamWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("getConstructor", String.class, new Class[]{}, "com.example.日本語");
            ConfigurationLogger.checkGetConstructor("méthode", String.class, new Class[]{}, "com.example.Café");
        }, "checkGetConstructor should handle Unicode characters in parameters");
    }

    /**
     * Tests 4-param checkGetConstructor with long method names.
     */
    @Test
    public void testCheckGetConstructor4ParamWithLongMethodName() {
        // Arrange
        String longMethodName = "thisIsAVeryLongMethodNameThatGoesOnAndOnAndProbablyWouldNeverBeUsedInRealCode";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(longMethodName, String.class, new Class[]{}, "caller"),
            "checkGetConstructor should handle long method names");
    }

    /**
     * Tests 4-param checkGetConstructor with special characters in method name.
     */
    @Test
    public void testCheckGetConstructor4ParamWithSpecialCharactersInMethodName() {
        // Act & Assert - Test special characters in method names
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("method$1", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("method_name", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("method123", String.class, new Class[]{}, "caller");
        }, "checkGetConstructor should handle special characters in method names");
    }

    /**
     * Tests 4-param checkGetConstructor with mixed case method names.
     */
    @Test
    public void testCheckGetConstructor4ParamWithMixedCaseMethodNames() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("GETCONSTRUCTOR", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getconstructor", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("GetConstructor", String.class, new Class[]{}, "caller");
        }, "checkGetConstructor should handle mixed case method names");
    }

    /**
     * Tests 4-param checkGetConstructor rapidly in sequence.
     */
    @Test
    public void testCheckGetConstructor4ParamRapidSequentialCalls() {
        // Act & Assert - Rapid sequential calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                ConfigurationLogger.checkGetConstructor("getConstructor", String.class, new Class[]{}, "caller");
            }
        }, "Rapid sequential calls to 4-param checkGetConstructor should not cause issues");
    }

    /**
     * Tests 4-param checkGetConstructor with different classes in sequence.
     */
    @Test
    public void testCheckGetConstructor4ParamWithDifferentClassesInSequence() {
        // Act & Assert - Different classes in sequence
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("getConstructor", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructor", Integer.class, new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructor", Object.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructor", StringBuilder.class, new Class[]{}, "caller");
        }, "Sequential calls with different classes should work");
    }

    /**
     * Tests 4-param checkGetConstructor is a static method.
     */
    @Test
    public void testCheckGetConstructor4ParamIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor("getConstructor", String.class, new Class[]{}, "caller"),
            "4-param checkGetConstructor should be callable as a static method");
    }

    /**
     * Tests 4-param checkGetConstructor with ConfigurationLogger class itself.
     */
    @Test
    public void testCheckGetConstructor4ParamWithConfigurationLoggerClass() {
        // Act & Assert - Should work with ConfigurationLogger class itself
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(
                "getConstructor",
                ConfigurationLogger.class,
                new Class[]{},
                "com.example.Caller"),
            "4-param checkGetConstructor should work with ConfigurationLogger class");
    }

    /**
     * Tests 4-param checkGetConstructor with the test class itself.
     */
    @Test
    public void testCheckGetConstructor4ParamWithTestClassItself() {
        // Act & Assert - Test with this test class
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructor(
                "getConstructor",
                this.getClass(),
                new Class[]{},
                "com.example.Caller"),
            "4-param checkGetConstructor should work with test class itself");
    }

    /**
     * Tests 4-param checkGetConstructor with various standard library classes.
     */
    @Test
    public void testCheckGetConstructor4ParamWithVariousStandardClasses() {
        // Act & Assert - Test with various standard library classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("getConstructor", java.util.ArrayList.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructor", java.util.HashMap.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructor", java.io.File.class, new Class[]{String.class}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructor", java.lang.StringBuilder.class, new Class[]{}, "caller");
        }, "4-param checkGetConstructor should handle various standard library classes");
    }

    /**
     * Tests 4-param checkGetConstructor with exception classes.
     */
    @Test
    public void testCheckGetConstructor4ParamWithExceptionClasses() {
        // Act & Assert - Exception classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("getConstructor", Exception.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructor", RuntimeException.class, new Class[]{String.class}, "caller");
            ConfigurationLogger.checkGetConstructor("getConstructor", NullPointerException.class, new Class[]{}, "caller");
        }, "4-param checkGetConstructor should handle exception classes");
    }

    /**
     * Tests 4-param checkGetConstructor alternating between different parameters.
     */
    @Test
    public void testCheckGetConstructor4ParamAlternatingParameters() {
        // Act & Assert - Alternating parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("method1", String.class, new Class[]{}, "caller1");
            ConfigurationLogger.checkGetConstructor("method2", Integer.class, new Class[]{int.class}, "caller2");
            ConfigurationLogger.checkGetConstructor("method1", String.class, new Class[]{String.class}, "caller3");
            ConfigurationLogger.checkGetConstructor("method3", Object.class, new Class[]{}, "caller1");
        }, "4-param checkGetConstructor should handle alternating parameters");
    }

    /**
     * Tests 4-param checkGetConstructor comparing with 3-param version.
     * Verifies both overloads can be called without conflict.
     */
    @Test
    public void testCheckGetConstructor4ParamComparedTo3Param() {
        // Act & Assert - Both overloads should work together
        assertDoesNotThrow(() -> {
            // 3-param version
            ConfigurationLogger.checkGetConstructor(String.class, new Class[]{}, "caller1");
            // 4-param version
            ConfigurationLogger.checkGetConstructor("getConstructor", String.class, new Class[]{}, "caller2");
            // 3-param version
            ConfigurationLogger.checkGetConstructor(Integer.class, new Class[]{int.class}, "caller3");
            // 4-param version
            ConfigurationLogger.checkGetConstructor("getDeclaredConstructor", Integer.class, new Class[]{int.class}, "caller4");
        }, "Both 3-param and 4-param checkGetConstructor should work together");
    }

    /**
     * Tests 4-param checkGetConstructor that the parameters array is not modified.
     */
    @Test
    public void testCheckGetConstructor4ParamDoesNotModifyParametersArray() {
        // Arrange
        Class<?>[] originalParams = new Class[]{String.class, int.class};
        Class<?>[] paramsCopy = originalParams.clone();

        // Act
        ConfigurationLogger.checkGetConstructor("getConstructor", String.class, originalParams, "caller");

        // Assert
        assertArrayEquals(paramsCopy, originalParams,
            "4-param checkGetConstructor should not modify the parameters array");
    }

    /**
     * Tests 4-param checkGetConstructor with method names containing dots.
     */
    @Test
    public void testCheckGetConstructor4ParamWithDotsInMethodName() {
        // Act & Assert - Method names might contain dots in some contexts
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor("Class.getConstructor", String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetConstructor("java.lang.Class.getConstructor", String.class, new Class[]{}, "caller");
        }, "4-param checkGetConstructor should handle dots in method names");
    }

    /**
     * Tests 4-param checkGetConstructor parameter independence.
     */
    @Test
    public void testCheckGetConstructor4ParamParameterIndependence() {
        // Act & Assert - Changing one parameter shouldn't affect handling of others
        String baseMethod = "method";
        Class<?> baseClass = String.class;
        String baseCaller = "caller";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructor(baseMethod + "1", baseClass, new Class[]{}, baseCaller);
            ConfigurationLogger.checkGetConstructor(baseMethod, Integer.class, new Class[]{}, baseCaller);
            ConfigurationLogger.checkGetConstructor(baseMethod, baseClass, new Class[]{String.class}, baseCaller);
            ConfigurationLogger.checkGetConstructor(baseMethod, baseClass, new Class[]{}, baseCaller + "2");
        }, "4-param checkGetConstructor should handle each parameter independently");
    }
}

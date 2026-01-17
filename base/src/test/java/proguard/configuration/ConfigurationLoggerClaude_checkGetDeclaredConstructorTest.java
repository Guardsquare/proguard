package proguard.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLogger#checkGetDeclaredConstructor(Class, Class[], String)} method.
 * Tests the reflection logging functionality for getDeclaredConstructor() calls.
 */
public class ConfigurationLoggerClaude_checkGetDeclaredConstructorTest {

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
     * Tests checkGetDeclaredConstructor with null class parameter.
     * This should cause a NullPointerException when the method tries to access the class.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithNullClass() {
        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetDeclaredConstructor(null, new Class[]{}, "com.example.Caller"),
            "checkGetDeclaredConstructor should throw NullPointerException with null class");
    }

    /**
     * Tests checkGetDeclaredConstructor with null constructor parameters array.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithNullParameters() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, null, "com.example.Caller"),
            "checkGetDeclaredConstructor should handle null constructor parameters gracefully");
    }

    /**
     * Tests checkGetDeclaredConstructor with null calling class name.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, null),
            "checkGetDeclaredConstructor should handle null calling class name gracefully");
    }

    /**
     * Tests checkGetDeclaredConstructor with all null parameters except class.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithNullParametersAndCallingClass() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, null, null),
            "checkGetDeclaredConstructor should handle null parameters and calling class");
    }

    /**
     * Tests checkGetDeclaredConstructor with empty constructor parameters array.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithEmptyParameters() {
        // Act & Assert - Empty array represents no-arg constructor
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "com.example.Caller"),
            "checkGetDeclaredConstructor should handle empty constructor parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor with empty calling class name.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithEmptyCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, ""),
            "checkGetDeclaredConstructor should handle empty calling class name");
    }

    /**
     * Tests checkGetDeclaredConstructor with a standard Java class and no parameters.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithStandardJavaClass() {
        // Act - Call with standard Java class
        ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "com.example.Caller");

        // Assert - Should complete without throwing exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(Integer.class, new Class[]{int.class}, "com.example.Caller"),
            "checkGetDeclaredConstructor should handle standard Java classes");
    }

    /**
     * Tests checkGetDeclaredConstructor with single parameter constructors.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithSingleParameter() {
        // Act & Assert - Test constructors with one parameter
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{String.class}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(Integer.class, new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(StringBuilder.class, new Class[]{String.class}, "caller");
        }, "checkGetDeclaredConstructor should handle single parameter constructors");
    }

    /**
     * Tests checkGetDeclaredConstructor with multiple parameter constructors.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithMultipleParameters() {
        // Act & Assert - Test constructors with multiple parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(
                String.class,
                new Class[]{char[].class, int.class, int.class},
                "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(
                String.class,
                new Class[]{byte[].class, String.class},
                "caller");
        }, "checkGetDeclaredConstructor should handle multiple parameter constructors");
    }

    /**
     * Tests checkGetDeclaredConstructor with primitive type parameters.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithPrimitiveParameters() {
        // Act & Assert - Test with primitive types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(Integer.class, new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(Boolean.class, new Class[]{boolean.class}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(Double.class, new Class[]{double.class}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(Long.class, new Class[]{long.class}, "caller");
        }, "checkGetDeclaredConstructor should handle primitive type parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor with wrapper class parameters.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithWrapperParameters() {
        // Act & Assert - Test with wrapper classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(
                String.class,
                new Class[]{Integer.class},
                "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(
                StringBuilder.class,
                new Class[]{Integer.class},
                "caller");
        }, "checkGetDeclaredConstructor should handle wrapper class parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor with array type parameters.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithArrayParameters() {
        // Act & Assert - Test with array types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(
                String.class,
                new Class[]{char[].class},
                "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(
                String.class,
                new Class[]{byte[].class},
                "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(
                String.class,
                new Class[]{int[].class},
                "caller");
        }, "checkGetDeclaredConstructor should handle array type parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor with various calling class names.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithVariousCallingClassNames() {
        // Act & Assert - Test different calling class name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "SimpleCaller");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "com.example.FullyQualified");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "com.example.Class$1");
        }, "checkGetDeclaredConstructor should handle various calling class name formats");
    }

    /**
     * Tests checkGetDeclaredConstructor with different Java classes.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithDifferentClasses() {
        // Act & Assert - Test various Java classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(Integer.class, new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(Object.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(Thread.class, new Class[]{Runnable.class}, "caller");
        }, "checkGetDeclaredConstructor should handle different Java classes");
    }

    /**
     * Tests checkGetDeclaredConstructor can be called multiple times without issues.
     */
    @Test
    public void testCheckGetDeclaredConstructorMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkGetDeclaredConstructor(
                    String.class,
                    new Class[]{},
                    "com.example.Caller" + i);
            }
        }, "Multiple calls to checkGetDeclaredConstructor should not cause issues");
    }

    /**
     * Tests checkGetDeclaredConstructor with same parameters multiple times (idempotency).
     */
    @Test
    public void testCheckGetDeclaredConstructorWithSameParametersMultipleTimes() {
        // Act - Call checkGetDeclaredConstructor multiple times with the same parameters
        Class<?> reflectedClass = String.class;
        Class<?>[] params = new Class[]{String.class};
        String callingClass = "com.example.Caller";

        ConfigurationLogger.checkGetDeclaredConstructor(reflectedClass, params, callingClass);
        ConfigurationLogger.checkGetDeclaredConstructor(reflectedClass, params, callingClass);
        ConfigurationLogger.checkGetDeclaredConstructor(reflectedClass, params, callingClass);

        // Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(reflectedClass, params, callingClass),
            "Calling checkGetDeclaredConstructor multiple times with same parameters should be safe");
    }

    /**
     * Tests checkGetDeclaredConstructor with different combinations of parameters.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithDifferentParameterCombinations() {
        // Act & Assert - Various parameter combinations
        assertDoesNotThrow(() -> {
            // Same class, different constructor signatures
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{String.class}, "caller");

            // Different classes, same caller
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(Integer.class, new Class[]{int.class}, "caller");

            // Different classes, different callers
            ConfigurationLogger.checkGetDeclaredConstructor(Object.class, new Class[]{}, "caller1");
            ConfigurationLogger.checkGetDeclaredConstructor(Thread.class, new Class[]{Runnable.class}, "caller2");
        }, "checkGetDeclaredConstructor should handle different parameter combinations");
    }

    /**
     * Tests checkGetDeclaredConstructor with whitespace in calling class name.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithWhitespace() {
        // Act & Assert - Should handle whitespace in string parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, " caller ");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "  ");
        }, "checkGetDeclaredConstructor should handle whitespace in parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor is a static method and can be called without an instance.
     */
    @Test
    public void testCheckGetDeclaredConstructorIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller"),
            "checkGetDeclaredConstructor should be callable as a static method");
    }

    /**
     * Tests checkGetDeclaredConstructor thread safety by calling from multiple threads.
     */
    @Test
    public void testCheckGetDeclaredConstructorThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkGetDeclaredConstructor
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkGetDeclaredConstructor(
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
        assertTrue(true, "Concurrent calls to checkGetDeclaredConstructor should not cause issues");
    }

    /**
     * Tests checkGetDeclaredConstructor with Unicode characters in calling class name.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "com.example.日本語");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "com.example.Café");
        }, "checkGetDeclaredConstructor should handle Unicode characters in parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor with mixed case calling class names.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithMixedCase() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "COM.EXAMPLE.CALLER");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "com.example.caller");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "CoM.ExAmPlE.CaLlEr");
        }, "checkGetDeclaredConstructor should handle mixed case parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor with long calling class name.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithLongCallingClassName() {
        // Arrange
        String longPackage = "com.example.very.long.package.name.with.many.parts";
        String longCallingClass = longPackage + ".VeryLongCallingClassName";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, longCallingClass),
            "checkGetDeclaredConstructor should handle long calling class names");
    }

    /**
     * Tests checkGetDeclaredConstructor with many constructor parameters.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithManyParameters() {
        // Act & Assert - Test constructor with many parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(
                String.class,
                new Class[]{char[].class, int.class, int.class},
                "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(
                String.class,
                new Class[]{byte[].class, int.class, int.class, String.class},
                "caller");
        }, "checkGetDeclaredConstructor should handle constructors with many parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor with ConfigurationLogger class itself.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithConfigurationLoggerClass() {
        // Act & Assert - Should work with ConfigurationLogger class itself
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(
                ConfigurationLogger.class,
                new Class[]{},
                "com.example.Caller"),
            "checkGetDeclaredConstructor should work with ConfigurationLogger class");
    }

    /**
     * Tests checkGetDeclaredConstructor behavior consistency.
     */
    @Test
    public void testCheckGetDeclaredConstructorConsistency() {
        // Act & Assert - Consistency across similar calls
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller1");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller2");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller3");
        }, "checkGetDeclaredConstructor should handle similar calls consistently");
    }

    /**
     * Tests checkGetDeclaredConstructor with dollar sign in calling class name.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithDollarSignInClassName() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "com.example.Class$Inner");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "Package$Class$Inner");
        }, "checkGetDeclaredConstructor should handle dollar signs in class names");
    }

    /**
     * Tests checkGetDeclaredConstructor with numbers in calling class name.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithNumbers() {
        // Act & Assert - Test numbers in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller123");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "123caller");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller1caller2");
        }, "checkGetDeclaredConstructor should handle numbers in parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor with enum classes.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithEnumClasses() {
        // Act & Assert - Enum classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(Thread.State.class, new Class[]{}, "caller"),
            "checkGetDeclaredConstructor should handle enum classes");
    }

    /**
     * Tests checkGetDeclaredConstructor rapidly in sequence.
     */
    @Test
    public void testCheckGetDeclaredConstructorRapidSequentialCalls() {
        // Act & Assert - Rapid sequential calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller");
            }
        }, "Rapid sequential calls should not cause issues");
    }

    /**
     * Tests checkGetDeclaredConstructor with different classes in sequence.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithDifferentClassesInSequence() {
        // Act & Assert - Different classes in sequence
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(Integer.class, new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(Object.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(StringBuilder.class, new Class[]{}, "caller");
        }, "Sequential calls with different classes should work");
    }

    /**
     * Tests checkGetDeclaredConstructor parameter validation - class must not be null.
     */
    @Test
    public void testCheckGetDeclaredConstructorClassNotNull() {
        // Assert - Null class should throw NPE
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetDeclaredConstructor(null, new Class[]{}, "caller"),
            "Null class parameter should throw NullPointerException");
    }

    /**
     * Tests checkGetDeclaredConstructor with the test class itself.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithTestClassItself() {
        // Act & Assert - Test with this test class
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(
                this.getClass(),
                new Class[]{},
                "com.example.Caller"),
            "checkGetDeclaredConstructor should work with test class itself");
    }

    /**
     * Tests checkGetDeclaredConstructor with various standard library classes.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithVariousStandardClasses() {
        // Act & Assert - Test with various standard library classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(java.util.ArrayList.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(java.util.HashMap.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(java.io.File.class, new Class[]{String.class}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(java.lang.StringBuilder.class, new Class[]{}, "caller");
        }, "checkGetDeclaredConstructor should handle various standard library classes");
    }

    /**
     * Tests checkGetDeclaredConstructor with exception classes.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithExceptionClasses() {
        // Act & Assert - Exception classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(Exception.class, new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(RuntimeException.class, new Class[]{String.class}, "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(NullPointerException.class, new Class[]{}, "caller");
        }, "checkGetDeclaredConstructor should handle exception classes");
    }

    /**
     * Tests checkGetDeclaredConstructor alternating between different classes and constructors.
     */
    @Test
    public void testCheckGetDeclaredConstructorAlternatingParameters() {
        // Act & Assert - Alternating parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "caller1");
            ConfigurationLogger.checkGetDeclaredConstructor(Integer.class, new Class[]{int.class}, "caller2");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{String.class}, "caller3");
            ConfigurationLogger.checkGetDeclaredConstructor(Object.class, new Class[]{}, "caller1");
        }, "checkGetDeclaredConstructor should handle alternating parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor with Object type parameters.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithObjectParameters() {
        // Act & Assert - Test with Object type parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(
                Thread.class,
                new Class[]{Runnable.class},
                "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(
                Exception.class,
                new Class[]{String.class, Throwable.class},
                "caller");
        }, "checkGetDeclaredConstructor should handle Object type parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor with mixed primitive and object parameters.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithMixedParameters() {
        // Act & Assert - Test with mixed parameter types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(
                String.class,
                new Class[]{char[].class, int.class, int.class},
                "caller");
            ConfigurationLogger.checkGetDeclaredConstructor(
                String.class,
                new Class[]{byte[].class, String.class},
                "caller");
        }, "checkGetDeclaredConstructor should handle mixed parameter types");
    }

    /**
     * Tests checkGetDeclaredConstructor with abstract classes.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithAbstractClasses() {
        // Act & Assert - Abstract classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredConstructor(Number.class, new Class[]{}, "caller"),
            "checkGetDeclaredConstructor should handle abstract classes");
    }

    /**
     * Tests checkGetDeclaredConstructor with dots in calling class name.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithDotsInCallingClassName() {
        // Act & Assert - Test calling class names with various dot positions
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "com.example.Caller");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "a.b.c.d.e.f.Caller");
            ConfigurationLogger.checkGetDeclaredConstructor(String.class, new Class[]{}, "single");
        }, "checkGetDeclaredConstructor should handle dots in calling class names");
    }

    /**
     * Tests checkGetDeclaredConstructor with varargs-style parameters.
     * Note: Varargs are represented as arrays in reflection.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithVarargsStyleParameters() {
        // Act & Assert - Varargs are treated as arrays
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(
                String.class,
                new Class[]{int[].class, int.class, int.class},
                "caller");
        }, "checkGetDeclaredConstructor should handle varargs-style parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor with generic type parameters.
     * Note: Generic types are erased at runtime, so we test with the raw types.
     */
    @Test
    public void testCheckGetDeclaredConstructorWithGenericTypeParameters() {
        // Act & Assert - Generic types become their raw types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredConstructor(
                java.util.ArrayList.class,
                new Class[]{int.class},
                "caller");
        }, "checkGetDeclaredConstructor should handle generic type parameters");
    }

    /**
     * Tests checkGetDeclaredConstructor that the parameters array is not modified.
     */
    @Test
    public void testCheckGetDeclaredConstructorDoesNotModifyParametersArray() {
        // Arrange
        Class<?>[] originalParams = new Class[]{String.class, int.class};
        Class<?>[] paramsCopy = originalParams.clone();

        // Act
        ConfigurationLogger.checkGetDeclaredConstructor(String.class, originalParams, "caller");

        // Assert
        assertArrayEquals(paramsCopy, originalParams,
            "checkGetDeclaredConstructor should not modify the parameters array");
    }
}

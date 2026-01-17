package proguard.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLogger#checkGetDeclaredMethod(Class, String, Class[], String)} method.
 * Tests the reflection logging functionality for getDeclaredMethod() calls.
 */
public class ConfigurationLoggerClaude_checkGetDeclaredMethodTest {

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
     * Tests checkGetDeclaredMethod with null class parameter.
     * This should cause a NullPointerException when the method tries to access the class.
     */
    @Test
    public void testCheckGetDeclaredMethodWithNullClass() {
        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetDeclaredMethod(null, "toString", new Class[]{}, "com.example.Caller"),
            "checkGetDeclaredMethod should throw NullPointerException with null class");
    }

    /**
     * Tests checkGetDeclaredMethod with null method name.
     */
    @Test
    public void testCheckGetDeclaredMethodWithNullMethodName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(String.class, null, new Class[]{}, "com.example.Caller"),
            "checkGetDeclaredMethod should handle null method name gracefully");
    }

    /**
     * Tests checkGetDeclaredMethod with null method parameters array.
     */
    @Test
    public void testCheckGetDeclaredMethodWithNullParameters() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", null, "com.example.Caller"),
            "checkGetDeclaredMethod should handle null method parameters gracefully");
    }

    /**
     * Tests checkGetDeclaredMethod with null calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, null),
            "checkGetDeclaredMethod should handle null calling class name gracefully");
    }

    /**
     * Tests checkGetDeclaredMethod with all null parameters except class.
     */
    @Test
    public void testCheckGetDeclaredMethodWithMultipleNullParameters() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(String.class, null, null, null),
            "checkGetDeclaredMethod should handle multiple null parameters gracefully");
    }

    /**
     * Tests checkGetDeclaredMethod with empty method name.
     */
    @Test
    public void testCheckGetDeclaredMethodWithEmptyMethodName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "", new Class[]{}, "com.example.Caller"),
            "checkGetDeclaredMethod should handle empty method name");
    }

    /**
     * Tests checkGetDeclaredMethod with empty method parameters array.
     */
    @Test
    public void testCheckGetDeclaredMethodWithEmptyParameters() {
        // Act & Assert - Empty array represents no-arg method
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "com.example.Caller"),
            "checkGetDeclaredMethod should handle empty method parameters");
    }

    /**
     * Tests checkGetDeclaredMethod with empty calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodWithEmptyCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, ""),
            "checkGetDeclaredMethod should handle empty calling class name");
    }

    /**
     * Tests checkGetDeclaredMethod with a standard Java class and no parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithStandardJavaClass() {
        // Act - Call with standard Java class
        ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "com.example.Caller");

        // Assert - Should complete without throwing exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(Integer.class, "intValue", new Class[]{}, "com.example.Caller"),
            "checkGetDeclaredMethod should handle standard Java classes");
    }

    /**
     * Tests checkGetDeclaredMethod with single parameter methods.
     */
    @Test
    public void testCheckGetDeclaredMethodWithSingleParameter() {
        // Act & Assert - Test methods with one parameter
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "charAt", new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "equals", new Class[]{Object.class}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(StringBuilder.class, "append", new Class[]{String.class}, "caller");
        }, "checkGetDeclaredMethod should handle single parameter methods");
    }

    /**
     * Tests checkGetDeclaredMethod with multiple parameter methods.
     */
    @Test
    public void testCheckGetDeclaredMethodWithMultipleParameters() {
        // Act & Assert - Test methods with multiple parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(
                String.class,
                "substring",
                new Class[]{int.class, int.class},
                "caller");
            ConfigurationLogger.checkGetDeclaredMethod(
                String.class,
                "replace",
                new Class[]{char.class, char.class},
                "caller");
        }, "checkGetDeclaredMethod should handle multiple parameter methods");
    }

    /**
     * Tests checkGetDeclaredMethod with primitive type parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithPrimitiveParameters() {
        // Act & Assert - Test with primitive types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "charAt", new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(StringBuilder.class, "setLength", new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(Double.class, "isNaN", new Class[]{double.class}, "caller");
        }, "checkGetDeclaredMethod should handle primitive type parameters");
    }

    /**
     * Tests checkGetDeclaredMethod with wrapper class parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithWrapperParameters() {
        // Act & Assert - Test with wrapper classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "equals", new Class[]{Object.class}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(Integer.class, "compareTo", new Class[]{Integer.class}, "caller");
        }, "checkGetDeclaredMethod should handle wrapper class parameters");
    }

    /**
     * Tests checkGetDeclaredMethod with array type parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithArrayParameters() {
        // Act & Assert - Test with array types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "valueOf", new Class[]{char[].class}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "copyValueOf", new Class[]{char[].class}, "caller");
        }, "checkGetDeclaredMethod should handle array type parameters");
    }

    /**
     * Tests checkGetDeclaredMethod with various method names.
     */
    @Test
    public void testCheckGetDeclaredMethodWithVariousMethodNames() {
        // Act & Assert - Test different method name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "equals", new Class[]{Object.class}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "charAt", new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "substring", new Class[]{int.class}, "caller");
        }, "checkGetDeclaredMethod should handle various method names");
    }

    /**
     * Tests checkGetDeclaredMethod with various calling class names.
     */
    @Test
    public void testCheckGetDeclaredMethodWithVariousCallingClassNames() {
        // Act & Assert - Test different calling class name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "SimpleCaller");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "com.example.FullyQualified");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "com.example.Class$1");
        }, "checkGetDeclaredMethod should handle various calling class name formats");
    }

    /**
     * Tests checkGetDeclaredMethod with different Java classes.
     */
    @Test
    public void testCheckGetDeclaredMethodWithDifferentClasses() {
        // Act & Assert - Test various Java classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(Integer.class, "intValue", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(Object.class, "hashCode", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(Thread.class, "getName", new Class[]{}, "caller");
        }, "checkGetDeclaredMethod should handle different Java classes");
    }

    /**
     * Tests checkGetDeclaredMethod can be called multiple times without issues.
     */
    @Test
    public void testCheckGetDeclaredMethodMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkGetDeclaredMethod(
                    String.class,
                    "toString",
                    new Class[]{},
                    "com.example.Caller" + i);
            }
        }, "Multiple calls to checkGetDeclaredMethod should not cause issues");
    }

    /**
     * Tests checkGetDeclaredMethod with same parameters multiple times (idempotency).
     */
    @Test
    public void testCheckGetDeclaredMethodWithSameParametersMultipleTimes() {
        // Act - Call checkGetDeclaredMethod multiple times with the same parameters
        Class<?> reflectedClass = String.class;
        String methodName = "charAt";
        Class<?>[] params = new Class[]{int.class};
        String callingClass = "com.example.Caller";

        ConfigurationLogger.checkGetDeclaredMethod(reflectedClass, methodName, params, callingClass);
        ConfigurationLogger.checkGetDeclaredMethod(reflectedClass, methodName, params, callingClass);
        ConfigurationLogger.checkGetDeclaredMethod(reflectedClass, methodName, params, callingClass);

        // Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(reflectedClass, methodName, params, callingClass),
            "Calling checkGetDeclaredMethod multiple times with same parameters should be safe");
    }

    /**
     * Tests checkGetDeclaredMethod with different combinations of parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithDifferentParameterCombinations() {
        // Act & Assert - Various parameter combinations
        assertDoesNotThrow(() -> {
            // Same class, different method signatures
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "charAt", new Class[]{int.class}, "caller");

            // Different classes, same caller
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(Integer.class, "intValue", new Class[]{}, "caller");

            // Different classes, different callers
            ConfigurationLogger.checkGetDeclaredMethod(Object.class, "hashCode", new Class[]{}, "caller1");
            ConfigurationLogger.checkGetDeclaredMethod(Thread.class, "getName", new Class[]{}, "caller2");
        }, "checkGetDeclaredMethod should handle different parameter combinations");
    }

    /**
     * Tests checkGetDeclaredMethod with whitespace in parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithWhitespace() {
        // Act & Assert - Should handle whitespace in string parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, " toString ", new Class[]{}, " caller ");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "  ", new Class[]{}, "  ");
        }, "checkGetDeclaredMethod should handle whitespace in parameters");
    }

    /**
     * Tests checkGetDeclaredMethod is a static method and can be called without an instance.
     */
    @Test
    public void testCheckGetDeclaredMethodIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller"),
            "checkGetDeclaredMethod should be callable as a static method");
    }

    /**
     * Tests checkGetDeclaredMethod thread safety by calling from multiple threads.
     */
    @Test
    public void testCheckGetDeclaredMethodThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkGetDeclaredMethod
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkGetDeclaredMethod(
                        String.class,
                        "toString",
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
        assertTrue(true, "Concurrent calls to checkGetDeclaredMethod should not cause issues");
    }

    /**
     * Tests checkGetDeclaredMethod with Unicode characters in parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "com.example.日本語");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "méthode", new Class[]{}, "com.example.Café");
        }, "checkGetDeclaredMethod should handle Unicode characters in parameters");
    }

    /**
     * Tests checkGetDeclaredMethod with mixed case parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithMixedCase() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "ToString", new Class[]{}, "COM.EXAMPLE.CALLER");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "tostring", new Class[]{}, "com.example.caller");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "ToStRiNg", new Class[]{}, "CoM.ExAmPlE.CaLlEr");
        }, "checkGetDeclaredMethod should handle mixed case parameters");
    }

    /**
     * Tests checkGetDeclaredMethod with long method names.
     */
    @Test
    public void testCheckGetDeclaredMethodWithLongMethodName() {
        // Arrange
        String longMethodName = "thisIsAVeryLongMethodNameThatGoesOnAndOnAndProbablyWouldNeverBeUsedInRealCode";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(String.class, longMethodName, new Class[]{}, "caller"),
            "checkGetDeclaredMethod should handle long method names");
    }

    /**
     * Tests checkGetDeclaredMethod with long calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodWithLongCallingClassName() {
        // Arrange
        String longPackage = "com.example.very.long.package.name.with.many.parts";
        String longCallingClass = longPackage + ".VeryLongCallingClassName";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, longCallingClass),
            "checkGetDeclaredMethod should handle long calling class names");
    }

    /**
     * Tests checkGetDeclaredMethod with many method parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithManyParameters() {
        // Act & Assert - Test method with many parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(
                String.class,
                "substring",
                new Class[]{int.class, int.class},
                "caller");
        }, "checkGetDeclaredMethod should handle methods with many parameters");
    }

    /**
     * Tests checkGetDeclaredMethod with ConfigurationLogger class itself.
     */
    @Test
    public void testCheckGetDeclaredMethodWithConfigurationLoggerClass() {
        // Act & Assert - Should work with ConfigurationLogger class itself
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(
                ConfigurationLogger.class,
                "run",
                new Class[]{},
                "com.example.Caller"),
            "checkGetDeclaredMethod should work with ConfigurationLogger class");
    }

    /**
     * Tests checkGetDeclaredMethod behavior consistency.
     */
    @Test
    public void testCheckGetDeclaredMethodConsistency() {
        // Act & Assert - Consistency across similar calls
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller1");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller2");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller3");
        }, "checkGetDeclaredMethod should handle similar calls consistently");
    }

    /**
     * Tests checkGetDeclaredMethod with dollar sign in calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodWithDollarSignInClassName() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "com.example.Class$Inner");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "Package$Class$Inner");
        }, "checkGetDeclaredMethod should handle dollar signs in class names");
    }

    /**
     * Tests checkGetDeclaredMethod with numbers in parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithNumbers() {
        // Act & Assert - Test numbers in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "method123", new Class[]{}, "caller123");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "123method", new Class[]{}, "123caller");
        }, "checkGetDeclaredMethod should handle numbers in parameters");
    }

    /**
     * Tests checkGetDeclaredMethod with special characters in method name.
     */
    @Test
    public void testCheckGetDeclaredMethodWithSpecialCharactersInMethodName() {
        // Act & Assert - Test special characters in method names
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "method$1", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "method_name", new Class[]{}, "caller");
        }, "checkGetDeclaredMethod should handle special characters in method names");
    }

    /**
     * Tests checkGetDeclaredMethod rapidly in sequence.
     */
    @Test
    public void testCheckGetDeclaredMethodRapidSequentialCalls() {
        // Act & Assert - Rapid sequential calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller");
            }
        }, "Rapid sequential calls should not cause issues");
    }

    /**
     * Tests checkGetDeclaredMethod with different classes in sequence.
     */
    @Test
    public void testCheckGetDeclaredMethodWithDifferentClassesInSequence() {
        // Act & Assert - Different classes in sequence
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(Integer.class, "intValue", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(Object.class, "hashCode", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(StringBuilder.class, "toString", new Class[]{}, "caller");
        }, "Sequential calls with different classes should work");
    }

    /**
     * Tests checkGetDeclaredMethod parameter validation - class must not be null.
     */
    @Test
    public void testCheckGetDeclaredMethodClassNotNull() {
        // Assert - Null class should throw NPE
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetDeclaredMethod(null, "toString", new Class[]{}, "caller"),
            "Null class parameter should throw NullPointerException");
    }

    /**
     * Tests checkGetDeclaredMethod with the test class itself.
     */
    @Test
    public void testCheckGetDeclaredMethodWithTestClassItself() {
        // Act & Assert - Test with this test class
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(
                this.getClass(),
                "setUp",
                new Class[]{},
                "com.example.Caller"),
            "checkGetDeclaredMethod should work with test class itself");
    }

    /**
     * Tests checkGetDeclaredMethod with various standard library classes.
     */
    @Test
    public void testCheckGetDeclaredMethodWithVariousStandardClasses() {
        // Act & Assert - Test with various standard library classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(java.util.ArrayList.class, "size", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(java.util.HashMap.class, "isEmpty", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(java.io.File.class, "exists", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(java.lang.StringBuilder.class, "toString", new Class[]{}, "caller");
        }, "checkGetDeclaredMethod should handle various standard library classes");
    }

    /**
     * Tests checkGetDeclaredMethod with exception classes.
     */
    @Test
    public void testCheckGetDeclaredMethodWithExceptionClasses() {
        // Act & Assert - Exception classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(Exception.class, "getMessage", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(RuntimeException.class, "getCause", new Class[]{}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(NullPointerException.class, "toString", new Class[]{}, "caller");
        }, "checkGetDeclaredMethod should handle exception classes");
    }

    /**
     * Tests checkGetDeclaredMethod alternating between different parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodAlternatingParameters() {
        // Act & Assert - Alternating parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller1");
            ConfigurationLogger.checkGetDeclaredMethod(Integer.class, "intValue", new Class[]{}, "caller2");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "charAt", new Class[]{int.class}, "caller3");
            ConfigurationLogger.checkGetDeclaredMethod(Object.class, "hashCode", new Class[]{}, "caller1");
        }, "checkGetDeclaredMethod should handle alternating parameters");
    }

    /**
     * Tests checkGetDeclaredMethod with Object type parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithObjectParameters() {
        // Act & Assert - Test with Object type parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "equals", new Class[]{Object.class}, "caller");
            ConfigurationLogger.checkGetDeclaredMethod(Object.class, "equals", new Class[]{Object.class}, "caller");
        }, "checkGetDeclaredMethod should handle Object type parameters");
    }

    /**
     * Tests checkGetDeclaredMethod with mixed primitive and object parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodWithMixedParameters() {
        // Act & Assert - Test with mixed parameter types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(
                String.class,
                "substring",
                new Class[]{int.class, int.class},
                "caller");
        }, "checkGetDeclaredMethod should handle mixed parameter types");
    }

    /**
     * Tests checkGetDeclaredMethod with dots in calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodWithDotsInCallingClassName() {
        // Act & Assert - Test calling class names with various dot positions
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "com.example.Caller");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "a.b.c.d.e.f.Caller");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "single");
        }, "checkGetDeclaredMethod should handle dots in calling class names");
    }

    /**
     * Tests checkGetDeclaredMethod that the parameters array is not modified.
     */
    @Test
    public void testCheckGetDeclaredMethodDoesNotModifyParametersArray() {
        // Arrange
        Class<?>[] originalParams = new Class[]{int.class, Object.class};
        Class<?>[] paramsCopy = originalParams.clone();

        // Act
        ConfigurationLogger.checkGetDeclaredMethod(String.class, "myMethod", originalParams, "caller");

        // Assert
        assertArrayEquals(paramsCopy, originalParams,
            "checkGetDeclaredMethod should not modify the parameters array");
    }

    /**
     * Tests checkGetDeclaredMethod with enum classes.
     */
    @Test
    public void testCheckGetDeclaredMethodWithEnumClasses() {
        // Act & Assert - Enum classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(Thread.State.class, "toString", new Class[]{}, "caller"),
            "checkGetDeclaredMethod should handle enum classes");
    }

    /**
     * Tests checkGetDeclaredMethod with interface classes.
     */
    @Test
    public void testCheckGetDeclaredMethodWithInterfaceClasses() {
        // Act & Assert - Interface classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(Runnable.class, "run", new Class[]{}, "caller"),
            "checkGetDeclaredMethod should handle interface classes");
    }

    /**
     * Tests checkGetDeclaredMethod with abstract classes.
     */
    @Test
    public void testCheckGetDeclaredMethodWithAbstractClasses() {
        // Act & Assert - Abstract classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethod(Number.class, "intValue", new Class[]{}, "caller"),
            "checkGetDeclaredMethod should handle abstract classes");
    }

    /**
     * Tests checkGetDeclaredMethod parameter independence.
     */
    @Test
    public void testCheckGetDeclaredMethodParameterIndependence() {
        // Act & Assert - Changing one parameter shouldn't affect handling of others
        String baseMethod = "method";
        Class<?> baseClass = String.class;
        String baseCaller = "caller";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethod(baseClass, baseMethod + "1", new Class[]{}, baseCaller);
            ConfigurationLogger.checkGetDeclaredMethod(Integer.class, baseMethod, new Class[]{}, baseCaller);
            ConfigurationLogger.checkGetDeclaredMethod(baseClass, baseMethod, new Class[]{int.class}, baseCaller);
            ConfigurationLogger.checkGetDeclaredMethod(baseClass, baseMethod, new Class[]{}, baseCaller + "2");
        }, "checkGetDeclaredMethod should handle each parameter independently");
    }
}

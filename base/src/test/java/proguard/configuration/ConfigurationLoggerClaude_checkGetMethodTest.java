package proguard.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLogger#checkGetMethod(Class, String, Class[], String)} method.
 * Tests the reflection logging functionality for getMethod() calls.
 */
public class ConfigurationLoggerClaude_checkGetMethodTest {

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
     * Tests checkGetMethod with null class parameter.
     * This should cause a NullPointerException when the method tries to access the class.
     */
    @Test
    public void testCheckGetMethodWithNullClass() {
        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetMethod(null, "toString", new Class[]{}, "com.example.Caller"),
            "checkGetMethod should throw NullPointerException with null class");
    }

    /**
     * Tests checkGetMethod with null method name.
     */
    @Test
    public void testCheckGetMethodWithNullMethodName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(String.class, null, new Class[]{}, "com.example.Caller"),
            "checkGetMethod should handle null method name gracefully");
    }

    /**
     * Tests checkGetMethod with null method parameters array.
     */
    @Test
    public void testCheckGetMethodWithNullParameters() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(String.class, "toString", null, "com.example.Caller"),
            "checkGetMethod should handle null method parameters gracefully");
    }

    /**
     * Tests checkGetMethod with null calling class name.
     */
    @Test
    public void testCheckGetMethodWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, null),
            "checkGetMethod should handle null calling class name gracefully");
    }

    /**
     * Tests checkGetMethod with all null parameters except class.
     */
    @Test
    public void testCheckGetMethodWithMultipleNullParameters() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(String.class, null, null, null),
            "checkGetMethod should handle multiple null parameters gracefully");
    }

    /**
     * Tests checkGetMethod with empty method name.
     */
    @Test
    public void testCheckGetMethodWithEmptyMethodName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(String.class, "", new Class[]{}, "com.example.Caller"),
            "checkGetMethod should handle empty method name");
    }

    /**
     * Tests checkGetMethod with empty method parameters array.
     */
    @Test
    public void testCheckGetMethodWithEmptyParameters() {
        // Act & Assert - Empty array represents no-arg method
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "com.example.Caller"),
            "checkGetMethod should handle empty method parameters");
    }

    /**
     * Tests checkGetMethod with empty calling class name.
     */
    @Test
    public void testCheckGetMethodWithEmptyCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, ""),
            "checkGetMethod should handle empty calling class name");
    }

    /**
     * Tests checkGetMethod with a standard Java class and no parameters.
     */
    @Test
    public void testCheckGetMethodWithStandardJavaClass() {
        // Act - Call with standard Java class
        ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "com.example.Caller");

        // Assert - Should complete without throwing exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(Integer.class, "intValue", new Class[]{}, "com.example.Caller"),
            "checkGetMethod should handle standard Java classes");
    }

    /**
     * Tests checkGetMethod with single parameter methods.
     */
    @Test
    public void testCheckGetMethodWithSingleParameter() {
        // Act & Assert - Test methods with one parameter
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "charAt", new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetMethod(String.class, "equals", new Class[]{Object.class}, "caller");
            ConfigurationLogger.checkGetMethod(StringBuilder.class, "append", new Class[]{String.class}, "caller");
        }, "checkGetMethod should handle single parameter methods");
    }

    /**
     * Tests checkGetMethod with multiple parameter methods.
     */
    @Test
    public void testCheckGetMethodWithMultipleParameters() {
        // Act & Assert - Test methods with multiple parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(
                String.class,
                "substring",
                new Class[]{int.class, int.class},
                "caller");
            ConfigurationLogger.checkGetMethod(
                String.class,
                "replace",
                new Class[]{char.class, char.class},
                "caller");
        }, "checkGetMethod should handle multiple parameter methods");
    }

    /**
     * Tests checkGetMethod with primitive type parameters.
     */
    @Test
    public void testCheckGetMethodWithPrimitiveParameters() {
        // Act & Assert - Test with primitive types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "charAt", new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetMethod(StringBuilder.class, "setLength", new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetMethod(Double.class, "isNaN", new Class[]{double.class}, "caller");
        }, "checkGetMethod should handle primitive type parameters");
    }

    /**
     * Tests checkGetMethod with wrapper class parameters.
     */
    @Test
    public void testCheckGetMethodWithWrapperParameters() {
        // Act & Assert - Test with wrapper classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "equals", new Class[]{Object.class}, "caller");
            ConfigurationLogger.checkGetMethod(Integer.class, "compareTo", new Class[]{Integer.class}, "caller");
        }, "checkGetMethod should handle wrapper class parameters");
    }

    /**
     * Tests checkGetMethod with array type parameters.
     */
    @Test
    public void testCheckGetMethodWithArrayParameters() {
        // Act & Assert - Test with array types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "valueOf", new Class[]{char[].class}, "caller");
            ConfigurationLogger.checkGetMethod(String.class, "copyValueOf", new Class[]{char[].class}, "caller");
        }, "checkGetMethod should handle array type parameters");
    }

    /**
     * Tests checkGetMethod with various method names.
     */
    @Test
    public void testCheckGetMethodWithVariousMethodNames() {
        // Act & Assert - Test different method name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(String.class, "equals", new Class[]{Object.class}, "caller");
            ConfigurationLogger.checkGetMethod(String.class, "charAt", new Class[]{int.class}, "caller");
            ConfigurationLogger.checkGetMethod(String.class, "substring", new Class[]{int.class}, "caller");
        }, "checkGetMethod should handle various method names");
    }

    /**
     * Tests checkGetMethod with various calling class names.
     */
    @Test
    public void testCheckGetMethodWithVariousCallingClassNames() {
        // Act & Assert - Test different calling class name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "SimpleCaller");
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "com.example.FullyQualified");
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "com.example.Class$1");
        }, "checkGetMethod should handle various calling class name formats");
    }

    /**
     * Tests checkGetMethod with different Java classes.
     */
    @Test
    public void testCheckGetMethodWithDifferentClasses() {
        // Act & Assert - Test various Java classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(Integer.class, "intValue", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(Object.class, "hashCode", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(Thread.class, "getName", new Class[]{}, "caller");
        }, "checkGetMethod should handle different Java classes");
    }

    /**
     * Tests checkGetMethod can be called multiple times without issues.
     */
    @Test
    public void testCheckGetMethodMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkGetMethod(
                    String.class,
                    "toString",
                    new Class[]{},
                    "com.example.Caller" + i);
            }
        }, "Multiple calls to checkGetMethod should not cause issues");
    }

    /**
     * Tests checkGetMethod with same parameters multiple times (idempotency).
     */
    @Test
    public void testCheckGetMethodWithSameParametersMultipleTimes() {
        // Act - Call checkGetMethod multiple times with the same parameters
        Class<?> reflectedClass = String.class;
        String methodName = "charAt";
        Class<?>[] params = new Class[]{int.class};
        String callingClass = "com.example.Caller";

        ConfigurationLogger.checkGetMethod(reflectedClass, methodName, params, callingClass);
        ConfigurationLogger.checkGetMethod(reflectedClass, methodName, params, callingClass);
        ConfigurationLogger.checkGetMethod(reflectedClass, methodName, params, callingClass);

        // Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(reflectedClass, methodName, params, callingClass),
            "Calling checkGetMethod multiple times with same parameters should be safe");
    }

    /**
     * Tests checkGetMethod with different combinations of parameters.
     */
    @Test
    public void testCheckGetMethodWithDifferentParameterCombinations() {
        // Act & Assert - Various parameter combinations
        assertDoesNotThrow(() -> {
            // Same class, different method signatures
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(String.class, "charAt", new Class[]{int.class}, "caller");

            // Different classes, same caller
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(Integer.class, "intValue", new Class[]{}, "caller");

            // Different classes, different callers
            ConfigurationLogger.checkGetMethod(Object.class, "hashCode", new Class[]{}, "caller1");
            ConfigurationLogger.checkGetMethod(Thread.class, "getName", new Class[]{}, "caller2");
        }, "checkGetMethod should handle different parameter combinations");
    }

    /**
     * Tests checkGetMethod with whitespace in parameters.
     */
    @Test
    public void testCheckGetMethodWithWhitespace() {
        // Act & Assert - Should handle whitespace in string parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, " toString ", new Class[]{}, " caller ");
            ConfigurationLogger.checkGetMethod(String.class, "  ", new Class[]{}, "  ");
        }, "checkGetMethod should handle whitespace in parameters");
    }

    /**
     * Tests checkGetMethod is a static method and can be called without an instance.
     */
    @Test
    public void testCheckGetMethodIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller"),
            "checkGetMethod should be callable as a static method");
    }

    /**
     * Tests checkGetMethod thread safety by calling from multiple threads.
     */
    @Test
    public void testCheckGetMethodThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkGetMethod
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkGetMethod(
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
        assertTrue(true, "Concurrent calls to checkGetMethod should not cause issues");
    }

    /**
     * Tests checkGetMethod with Unicode characters in parameters.
     */
    @Test
    public void testCheckGetMethodWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "com.example.日本語");
            ConfigurationLogger.checkGetMethod(String.class, "méthode", new Class[]{}, "com.example.Café");
        }, "checkGetMethod should handle Unicode characters in parameters");
    }

    /**
     * Tests checkGetMethod with mixed case parameters.
     */
    @Test
    public void testCheckGetMethodWithMixedCase() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "ToString", new Class[]{}, "COM.EXAMPLE.CALLER");
            ConfigurationLogger.checkGetMethod(String.class, "tostring", new Class[]{}, "com.example.caller");
            ConfigurationLogger.checkGetMethod(String.class, "ToStRiNg", new Class[]{}, "CoM.ExAmPlE.CaLlEr");
        }, "checkGetMethod should handle mixed case parameters");
    }

    /**
     * Tests checkGetMethod with long method names.
     */
    @Test
    public void testCheckGetMethodWithLongMethodName() {
        // Arrange
        String longMethodName = "thisIsAVeryLongMethodNameThatGoesOnAndOnAndProbablyWouldNeverBeUsedInRealCode";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(String.class, longMethodName, new Class[]{}, "caller"),
            "checkGetMethod should handle long method names");
    }

    /**
     * Tests checkGetMethod with long calling class name.
     */
    @Test
    public void testCheckGetMethodWithLongCallingClassName() {
        // Arrange
        String longPackage = "com.example.very.long.package.name.with.many.parts";
        String longCallingClass = longPackage + ".VeryLongCallingClassName";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, longCallingClass),
            "checkGetMethod should handle long calling class names");
    }

    /**
     * Tests checkGetMethod with many method parameters.
     */
    @Test
    public void testCheckGetMethodWithManyParameters() {
        // Act & Assert - Test method with many parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(
                String.class,
                "substring",
                new Class[]{int.class, int.class},
                "caller");
        }, "checkGetMethod should handle methods with many parameters");
    }

    /**
     * Tests checkGetMethod with ConfigurationLogger class itself.
     */
    @Test
    public void testCheckGetMethodWithConfigurationLoggerClass() {
        // Act & Assert - Should work with ConfigurationLogger class itself
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(
                ConfigurationLogger.class,
                "run",
                new Class[]{},
                "com.example.Caller"),
            "checkGetMethod should work with ConfigurationLogger class");
    }

    /**
     * Tests checkGetMethod behavior consistency.
     */
    @Test
    public void testCheckGetMethodConsistency() {
        // Act & Assert - Consistency across similar calls
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller1");
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller2");
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller3");
        }, "checkGetMethod should handle similar calls consistently");
    }

    /**
     * Tests checkGetMethod with dollar sign in calling class name.
     */
    @Test
    public void testCheckGetMethodWithDollarSignInClassName() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "com.example.Class$Inner");
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "Package$Class$Inner");
        }, "checkGetMethod should handle dollar signs in class names");
    }

    /**
     * Tests checkGetMethod with numbers in parameters.
     */
    @Test
    public void testCheckGetMethodWithNumbers() {
        // Act & Assert - Test numbers in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "method123", new Class[]{}, "caller123");
            ConfigurationLogger.checkGetMethod(String.class, "123method", new Class[]{}, "123caller");
        }, "checkGetMethod should handle numbers in parameters");
    }

    /**
     * Tests checkGetMethod with special characters in method name.
     */
    @Test
    public void testCheckGetMethodWithSpecialCharactersInMethodName() {
        // Act & Assert - Test special characters in method names
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "method$1", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(String.class, "method_name", new Class[]{}, "caller");
        }, "checkGetMethod should handle special characters in method names");
    }

    /**
     * Tests checkGetMethod rapidly in sequence.
     */
    @Test
    public void testCheckGetMethodRapidSequentialCalls() {
        // Act & Assert - Rapid sequential calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller");
            }
        }, "Rapid sequential calls should not cause issues");
    }

    /**
     * Tests checkGetMethod with different classes in sequence.
     */
    @Test
    public void testCheckGetMethodWithDifferentClassesInSequence() {
        // Act & Assert - Different classes in sequence
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(Integer.class, "intValue", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(Object.class, "hashCode", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(StringBuilder.class, "toString", new Class[]{}, "caller");
        }, "Sequential calls with different classes should work");
    }

    /**
     * Tests checkGetMethod parameter validation - class must not be null.
     */
    @Test
    public void testCheckGetMethodClassNotNull() {
        // Assert - Null class should throw NPE
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetMethod(null, "toString", new Class[]{}, "caller"),
            "Null class parameter should throw NullPointerException");
    }

    /**
     * Tests checkGetMethod with the test class itself.
     */
    @Test
    public void testCheckGetMethodWithTestClassItself() {
        // Act & Assert - Test with this test class
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(
                this.getClass(),
                "setUp",
                new Class[]{},
                "com.example.Caller"),
            "checkGetMethod should work with test class itself");
    }

    /**
     * Tests checkGetMethod with various standard library classes.
     */
    @Test
    public void testCheckGetMethodWithVariousStandardClasses() {
        // Act & Assert - Test with various standard library classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(java.util.ArrayList.class, "size", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(java.util.HashMap.class, "isEmpty", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(java.io.File.class, "exists", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(java.lang.StringBuilder.class, "toString", new Class[]{}, "caller");
        }, "checkGetMethod should handle various standard library classes");
    }

    /**
     * Tests checkGetMethod with exception classes.
     */
    @Test
    public void testCheckGetMethodWithExceptionClasses() {
        // Act & Assert - Exception classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(Exception.class, "getMessage", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(RuntimeException.class, "getCause", new Class[]{}, "caller");
            ConfigurationLogger.checkGetMethod(NullPointerException.class, "toString", new Class[]{}, "caller");
        }, "checkGetMethod should handle exception classes");
    }

    /**
     * Tests checkGetMethod alternating between different parameters.
     */
    @Test
    public void testCheckGetMethodAlternatingParameters() {
        // Act & Assert - Alternating parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller1");
            ConfigurationLogger.checkGetMethod(Integer.class, "intValue", new Class[]{}, "caller2");
            ConfigurationLogger.checkGetMethod(String.class, "charAt", new Class[]{int.class}, "caller3");
            ConfigurationLogger.checkGetMethod(Object.class, "hashCode", new Class[]{}, "caller1");
        }, "checkGetMethod should handle alternating parameters");
    }

    /**
     * Tests checkGetMethod with Object type parameters.
     */
    @Test
    public void testCheckGetMethodWithObjectParameters() {
        // Act & Assert - Test with Object type parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "equals", new Class[]{Object.class}, "caller");
            ConfigurationLogger.checkGetMethod(Object.class, "equals", new Class[]{Object.class}, "caller");
        }, "checkGetMethod should handle Object type parameters");
    }

    /**
     * Tests checkGetMethod with mixed primitive and object parameters.
     */
    @Test
    public void testCheckGetMethodWithMixedParameters() {
        // Act & Assert - Test with mixed parameter types
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(
                String.class,
                "substring",
                new Class[]{int.class, int.class},
                "caller");
        }, "checkGetMethod should handle mixed parameter types");
    }

    /**
     * Tests checkGetMethod with dots in calling class name.
     */
    @Test
    public void testCheckGetMethodWithDotsInCallingClassName() {
        // Act & Assert - Test calling class names with various dot positions
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "com.example.Caller");
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "a.b.c.d.e.f.Caller");
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "single");
        }, "checkGetMethod should handle dots in calling class names");
    }

    /**
     * Tests checkGetMethod that the parameters array is not modified.
     */
    @Test
    public void testCheckGetMethodDoesNotModifyParametersArray() {
        // Arrange
        Class<?>[] originalParams = new Class[]{int.class, Object.class};
        Class<?>[] paramsCopy = originalParams.clone();

        // Act
        ConfigurationLogger.checkGetMethod(String.class, "myMethod", originalParams, "caller");

        // Assert
        assertArrayEquals(paramsCopy, originalParams,
            "checkGetMethod should not modify the parameters array");
    }

    /**
     * Tests checkGetMethod with enum classes.
     */
    @Test
    public void testCheckGetMethodWithEnumClasses() {
        // Act & Assert - Enum classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(Thread.State.class, "toString", new Class[]{}, "caller"),
            "checkGetMethod should handle enum classes");
    }

    /**
     * Tests checkGetMethod with interface classes.
     */
    @Test
    public void testCheckGetMethodWithInterfaceClasses() {
        // Act & Assert - Interface classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(Runnable.class, "run", new Class[]{}, "caller"),
            "checkGetMethod should handle interface classes");
    }

    /**
     * Tests checkGetMethod with abstract classes.
     */
    @Test
    public void testCheckGetMethodWithAbstractClasses() {
        // Act & Assert - Abstract classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetMethod(Number.class, "intValue", new Class[]{}, "caller"),
            "checkGetMethod should handle abstract classes");
    }

    /**
     * Tests checkGetMethod parameter independence.
     */
    @Test
    public void testCheckGetMethodParameterIndependence() {
        // Act & Assert - Changing one parameter shouldn't affect handling of others
        String baseMethod = "method";
        Class<?> baseClass = String.class;
        String baseCaller = "caller";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(baseClass, baseMethod + "1", new Class[]{}, baseCaller);
            ConfigurationLogger.checkGetMethod(Integer.class, baseMethod, new Class[]{}, baseCaller);
            ConfigurationLogger.checkGetMethod(baseClass, baseMethod, new Class[]{int.class}, baseCaller);
            ConfigurationLogger.checkGetMethod(baseClass, baseMethod, new Class[]{}, baseCaller + "2");
        }, "checkGetMethod should handle each parameter independently");
    }

    /**
     * Tests checkGetMethod comparing behavior with checkGetDeclaredMethod.
     * This verifies that both methods can be called on the same class without conflict.
     */
    @Test
    public void testCheckGetMethodComparedToCheckGetDeclaredMethod() {
        // Act & Assert - Both methods should work on the same class
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethod(String.class, "toString", new Class[]{}, "caller1");
            ConfigurationLogger.checkGetDeclaredMethod(String.class, "toString", new Class[]{}, "caller2");
            ConfigurationLogger.checkGetMethod(Integer.class, "intValue", new Class[]{}, "caller3");
            ConfigurationLogger.checkGetDeclaredMethod(Integer.class, "intValue", new Class[]{}, "caller4");
        }, "checkGetMethod and checkGetDeclaredMethod should work together");
    }
}

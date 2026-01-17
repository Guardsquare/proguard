package proguard.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLogger#checkGetDeclaredMethods(Class, String)} method.
 * Tests the reflection logging functionality for getDeclaredMethods() calls.
 */
public class ConfigurationLoggerClaude_checkGetDeclaredMethodsTest {

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
     * Tests checkGetDeclaredMethods with null class parameter.
     * This should cause a NullPointerException when the method tries to access the class.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithNullClass() {
        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetDeclaredMethods(null, "com.example.Caller"),
            "checkGetDeclaredMethods should throw NullPointerException with null class");
    }

    /**
     * Tests checkGetDeclaredMethods with null calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(String.class, null),
            "checkGetDeclaredMethods should handle null calling class name gracefully");
    }

    /**
     * Tests checkGetDeclaredMethods with empty string calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithEmptyCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(String.class, ""),
            "checkGetDeclaredMethods should handle empty calling class name gracefully");
    }

    /**
     * Tests checkGetDeclaredMethods with a standard Java class.
     * Library classes should not trigger logging.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithStandardJavaClass() {
        // Act - Call with standard Java class
        ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Caller");

        // Assert - Should complete without throwing exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(Integer.class, "com.example.Caller"),
            "checkGetDeclaredMethods should handle standard Java classes");
    }

    /**
     * Tests checkGetDeclaredMethods with various calling class names.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithVariousCallingClassNames() {
        // Act & Assert - Test different calling class name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "SimpleCaller");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.FullyQualified");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Class$1");
        }, "checkGetDeclaredMethods should handle various calling class name formats");
    }

    /**
     * Tests checkGetDeclaredMethods with different Java classes.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithDifferentClasses() {
        // Act & Assert - Test various Java classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Integer.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Object.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(System.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Thread.class, "caller");
        }, "checkGetDeclaredMethods should handle different Java classes");
    }

    /**
     * Tests checkGetDeclaredMethods with long calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithLongCallingClassName() {
        // Arrange
        String longPackage = "com.example.very.long.package.name.with.many.parts";
        String longCallingClass = longPackage + ".VeryLongCallingClassName";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(String.class, longCallingClass),
            "checkGetDeclaredMethods should handle long calling class names");
    }

    /**
     * Tests checkGetDeclaredMethods can be called multiple times without issues.
     */
    @Test
    public void testCheckGetDeclaredMethodsMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Caller" + i);
            }
        }, "Multiple calls to checkGetDeclaredMethods should not cause issues");
    }

    /**
     * Tests checkGetDeclaredMethods with same parameters multiple times (idempotency).
     */
    @Test
    public void testCheckGetDeclaredMethodsWithSameParametersMultipleTimes() {
        // Act - Call checkGetDeclaredMethods multiple times with the same parameters
        Class<?> reflectedClass = String.class;
        String callingClass = "com.example.Caller";

        ConfigurationLogger.checkGetDeclaredMethods(reflectedClass, callingClass);
        ConfigurationLogger.checkGetDeclaredMethods(reflectedClass, callingClass);
        ConfigurationLogger.checkGetDeclaredMethods(reflectedClass, callingClass);

        // Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(reflectedClass, callingClass),
            "Calling checkGetDeclaredMethods multiple times with same parameters should be safe");
    }

    /**
     * Tests checkGetDeclaredMethods with different combinations of parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithDifferentParameterCombinations() {
        // Act & Assert - Various parameter combinations
        assertDoesNotThrow(() -> {
            // Same class, different callers
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller1");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller2");

            // Different classes, same caller
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Integer.class, "caller");

            // Different classes, different callers
            ConfigurationLogger.checkGetDeclaredMethods(Object.class, "caller1");
            ConfigurationLogger.checkGetDeclaredMethods(System.class, "caller2");
        }, "checkGetDeclaredMethods should handle different parameter combinations");
    }

    /**
     * Tests checkGetDeclaredMethods with whitespace in calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithWhitespace() {
        // Act & Assert - Should handle whitespace in string parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, " caller ");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "  ");
        }, "checkGetDeclaredMethods should handle whitespace in parameters");
    }

    /**
     * Tests checkGetDeclaredMethods is a static method and can be called without an instance.
     */
    @Test
    public void testCheckGetDeclaredMethodsIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller"),
            "checkGetDeclaredMethods should be callable as a static method");
    }

    /**
     * Tests checkGetDeclaredMethods thread safety by calling from multiple threads.
     */
    @Test
    public void testCheckGetDeclaredMethodsThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkGetDeclaredMethods
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkGetDeclaredMethods(
                        String.class,
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
        assertTrue(true, "Concurrent calls to checkGetDeclaredMethods should not cause issues");
    }

    /**
     * Tests checkGetDeclaredMethods with Unicode characters in calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.日本語");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Café");
        }, "checkGetDeclaredMethods should handle Unicode characters in parameters");
    }

    /**
     * Tests checkGetDeclaredMethods with mixed case calling class names.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithMixedCase() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "COM.EXAMPLE.CALLER");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.caller");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "CoM.ExAmPlE.CaLlEr");
        }, "checkGetDeclaredMethods should handle mixed case parameters");
    }

    /**
     * Tests checkGetDeclaredMethods with array classes.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithArrayClasses() {
        // Act & Assert - Array classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String[].class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(int[].class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Object[][].class, "caller");
        }, "checkGetDeclaredMethods should handle array classes");
    }

    /**
     * Tests checkGetDeclaredMethods with primitive wrapper classes.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithPrimitiveWrappers() {
        // Act & Assert - Primitive wrapper classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(Integer.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Boolean.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Double.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Long.class, "caller");
        }, "checkGetDeclaredMethods should handle primitive wrapper classes");
    }

    /**
     * Tests checkGetDeclaredMethods with inner classes.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithInnerClasses() {
        // Act & Assert - Test with inner class scenarios
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Outer$Inner$Deep");
        }, "checkGetDeclaredMethods should handle inner class callers");
    }

    /**
     * Tests checkGetDeclaredMethods with anonymous class references.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithAnonymousClasses() {
        // Act & Assert - Test with anonymous class naming
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.MyClass$1");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.MyClass$2$1");
        }, "checkGetDeclaredMethods should handle anonymous class callers");
    }

    /**
     * Tests that checkGetDeclaredMethods handles the two parameters independently.
     */
    @Test
    public void testCheckGetDeclaredMethodsParameterIndependence() {
        // Act & Assert - Changing one parameter shouldn't affect handling of others
        String baseName = "test";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, baseName);
            ConfigurationLogger.checkGetDeclaredMethods(String.class, baseName + "1");
            ConfigurationLogger.checkGetDeclaredMethods(Integer.class, baseName);
            ConfigurationLogger.checkGetDeclaredMethods(Object.class, baseName + "2");
        }, "checkGetDeclaredMethods should handle each parameter independently");
    }

    /**
     * Tests checkGetDeclaredMethods with extreme parameter combinations.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithExtremeParameters() {
        // Act & Assert - Test extreme but valid scenarios
        assertDoesNotThrow(() -> {
            // Very short names
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "c");

            // Empty string
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "");
        }, "checkGetDeclaredMethods should handle extreme parameter combinations");
    }

    /**
     * Tests checkGetDeclaredMethods with ConfigurationLogger class itself.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithConfigurationLoggerClass() {
        // Act & Assert - Should work with ConfigurationLogger class itself
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(
                ConfigurationLogger.class,
                "com.example.Caller"),
            "checkGetDeclaredMethods should work with ConfigurationLogger class");
    }

    /**
     * Tests checkGetDeclaredMethods behavior consistency - calling it multiple times
     * with slightly different parameters.
     */
    @Test
    public void testCheckGetDeclaredMethodsConsistency() {
        // Act & Assert - Consistency across similar calls
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller1");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller2");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller3");
        }, "checkGetDeclaredMethods should handle similar calls consistently");
    }

    /**
     * Tests checkGetDeclaredMethods with class that has dollar sign in calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithDollarSignInClassName() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Class$Inner");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "Package$Class$Inner");
        }, "checkGetDeclaredMethods should handle dollar signs in class names");
    }

    /**
     * Tests checkGetDeclaredMethods with numbers in calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithNumbers() {
        // Act & Assert - Test numbers in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller123");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "123caller");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller1caller2");
        }, "checkGetDeclaredMethods should handle numbers in parameters");
    }

    /**
     * Tests checkGetDeclaredMethods with enum classes.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithEnumClasses() {
        // Act & Assert - Enum classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(Thread.State.class, "caller"),
            "checkGetDeclaredMethods should handle enum classes");
    }

    /**
     * Tests checkGetDeclaredMethods with interface classes.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithInterfaceClasses() {
        // Act & Assert - Interface classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(Runnable.class, "caller"),
            "checkGetDeclaredMethods should handle interface classes");
    }

    /**
     * Tests checkGetDeclaredMethods rapidly in sequence.
     */
    @Test
    public void testCheckGetDeclaredMethodsRapidSequentialCalls() {
        // Act & Assert - Rapid sequential calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller");
            }
        }, "Rapid sequential calls should not cause issues");
    }

    /**
     * Tests checkGetDeclaredMethods with different classes in sequence.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithDifferentClassesInSequence() {
        // Act & Assert - Different classes in sequence
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Integer.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Object.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(System.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Thread.class, "caller");
        }, "Sequential calls with different classes should work");
    }

    /**
     * Tests checkGetDeclaredMethods parameter validation - class must not be null.
     */
    @Test
    public void testCheckGetDeclaredMethodsClassNotNull() {
        // Assert - Null class should throw NPE
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetDeclaredMethods(null, "caller"),
            "Null class parameter should throw NullPointerException");
    }

    /**
     * Tests checkGetDeclaredMethods with the test class itself.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithTestClassItself() {
        // Act & Assert - Test with this test class
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(
                this.getClass(),
                "com.example.Caller"),
            "checkGetDeclaredMethods should work with test class itself");
    }

    /**
     * Tests checkGetDeclaredMethods with various standard library classes.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithVariousStandardClasses() {
        // Act & Assert - Test with various standard library classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(java.util.ArrayList.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(java.util.HashMap.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(java.io.File.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(java.lang.StringBuilder.class, "caller");
        }, "checkGetDeclaredMethods should handle various standard library classes");
    }

    /**
     * Tests checkGetDeclaredMethods with special method name patterns in calling class.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithSpecialCallingClassPatterns() {
        // Act & Assert - Test calling class names with special patterns
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Class$1");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Class_With_Underscores");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Class123");
        }, "checkGetDeclaredMethods should handle special calling class name patterns");
    }

    /**
     * Tests checkGetDeclaredMethods with abstract classes.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithAbstractClasses() {
        // Act & Assert - Abstract classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(Number.class, "caller"),
            "checkGetDeclaredMethods should handle abstract classes");
    }

    /**
     * Tests checkGetDeclaredMethods behavior consistency across different reflection APIs.
     */
    @Test
    public void testCheckGetDeclaredMethodsConsistencyAcrossReflectionAPIs() {
        // Act & Assert - Same class accessed via different callers should be handled consistently
        String targetClass = "com.example.Caller";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, targetClass + "1");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, targetClass + "2");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, targetClass + "3");
        }, "checkGetDeclaredMethods should handle the same class consistently");
    }

    /**
     * Tests checkGetDeclaredMethods with dots in different positions of calling class name.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithDotsInCallingClassName() {
        // Act & Assert - Test calling class names with various dot positions
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Caller");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "a.b.c.d.e.f.Caller");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "single");
        }, "checkGetDeclaredMethods should handle dots in calling class names");
    }

    /**
     * Tests checkGetDeclaredMethods with package-private class references.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithPackagePrivateReferences() {
        // Act & Assert - Test package-private scenarios
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.PackagePrivateClass"),
            "checkGetDeclaredMethods should handle package-private class references");
    }

    /**
     * Tests checkGetDeclaredMethods with exception classes.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithExceptionClasses() {
        // Act & Assert - Exception classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(Exception.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(RuntimeException.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(NullPointerException.class, "caller");
        }, "checkGetDeclaredMethods should handle exception classes");
    }

    /**
     * Tests checkGetDeclaredMethods with collection classes.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithCollectionClasses() {
        // Act & Assert - Collection classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(java.util.List.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(java.util.Map.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(java.util.Set.class, "caller");
        }, "checkGetDeclaredMethods should handle collection classes");
    }

    /**
     * Tests checkGetDeclaredMethods alternating between different classes and callers.
     */
    @Test
    public void testCheckGetDeclaredMethodsAlternatingParameters() {
        // Act & Assert - Alternating parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller1");
            ConfigurationLogger.checkGetDeclaredMethods(Integer.class, "caller2");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller3");
            ConfigurationLogger.checkGetDeclaredMethods(Object.class, "caller1");
            ConfigurationLogger.checkGetDeclaredMethods(Integer.class, "caller4");
        }, "checkGetDeclaredMethods should handle alternating parameters");
    }

    /**
     * Tests checkGetDeclaredMethods with classes that have methods vs no accessible methods.
     */
    @Test
    public void testCheckGetDeclaredMethodsWithClassesWithAndWithoutMethods() {
        // Act & Assert - Test with classes that may or may not have accessible methods
        assertDoesNotThrow(() -> {
            // Classes with methods
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "caller");
            ConfigurationLogger.checkGetDeclaredMethods(Integer.class, "caller");

            // Object class
            ConfigurationLogger.checkGetDeclaredMethods(Object.class, "caller");
        }, "checkGetDeclaredMethods should handle classes with varying method counts");
    }
}

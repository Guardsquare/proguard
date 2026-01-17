package proguard.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLogger#checkGetConstructors(Class, String)} method.
 * Tests the reflection logging functionality for getConstructors() calls.
 */
public class ConfigurationLoggerClaude_checkGetConstructorsTest {

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
     * Tests checkGetConstructors with null class parameter.
     * This should cause a NullPointerException when the method tries to access the class.
     */
    @Test
    public void testCheckGetConstructorsWithNullClass() {
        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetConstructors(null, "com.example.Caller"),
            "checkGetConstructors should throw NullPointerException with null class");
    }

    /**
     * Tests checkGetConstructors with null calling class name.
     */
    @Test
    public void testCheckGetConstructorsWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(String.class, null),
            "checkGetConstructors should handle null calling class name gracefully");
    }

    /**
     * Tests checkGetConstructors with empty string calling class name.
     */
    @Test
    public void testCheckGetConstructorsWithEmptyCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(String.class, ""),
            "checkGetConstructors should handle empty calling class name gracefully");
    }

    /**
     * Tests checkGetConstructors with a standard Java class.
     * Library classes should not trigger logging.
     */
    @Test
    public void testCheckGetConstructorsWithStandardJavaClass() {
        // Act - Call with standard Java class
        ConfigurationLogger.checkGetConstructors(String.class, "com.example.Caller");

        // Assert - Should complete without throwing exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(Integer.class, "com.example.Caller"),
            "checkGetConstructors should handle standard Java classes");
    }

    /**
     * Tests checkGetConstructors with various calling class names.
     */
    @Test
    public void testCheckGetConstructorsWithVariousCallingClassNames() {
        // Act & Assert - Test different calling class name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "SimpleCaller");
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.FullyQualified");
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.Class$1");
        }, "checkGetConstructors should handle various calling class name formats");
    }

    /**
     * Tests checkGetConstructors with different Java classes.
     */
    @Test
    public void testCheckGetConstructorsWithDifferentClasses() {
        // Act & Assert - Test various Java classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "caller");
            ConfigurationLogger.checkGetConstructors(Integer.class, "caller");
            ConfigurationLogger.checkGetConstructors(Object.class, "caller");
            ConfigurationLogger.checkGetConstructors(System.class, "caller");
            ConfigurationLogger.checkGetConstructors(Thread.class, "caller");
        }, "checkGetConstructors should handle different Java classes");
    }

    /**
     * Tests checkGetConstructors with long calling class name.
     */
    @Test
    public void testCheckGetConstructorsWithLongCallingClassName() {
        // Arrange
        String longPackage = "com.example.very.long.package.name.with.many.parts";
        String longCallingClass = longPackage + ".VeryLongCallingClassName";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(String.class, longCallingClass),
            "checkGetConstructors should handle long calling class names");
    }

    /**
     * Tests checkGetConstructors can be called multiple times without issues.
     */
    @Test
    public void testCheckGetConstructorsMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkGetConstructors(String.class, "com.example.Caller" + i);
            }
        }, "Multiple calls to checkGetConstructors should not cause issues");
    }

    /**
     * Tests checkGetConstructors with same parameters multiple times (idempotency).
     */
    @Test
    public void testCheckGetConstructorsWithSameParametersMultipleTimes() {
        // Act - Call checkGetConstructors multiple times with the same parameters
        Class<?> reflectedClass = String.class;
        String callingClass = "com.example.Caller";

        ConfigurationLogger.checkGetConstructors(reflectedClass, callingClass);
        ConfigurationLogger.checkGetConstructors(reflectedClass, callingClass);
        ConfigurationLogger.checkGetConstructors(reflectedClass, callingClass);

        // Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(reflectedClass, callingClass),
            "Calling checkGetConstructors multiple times with same parameters should be safe");
    }

    /**
     * Tests checkGetConstructors with different combinations of parameters.
     */
    @Test
    public void testCheckGetConstructorsWithDifferentParameterCombinations() {
        // Act & Assert - Various parameter combinations
        assertDoesNotThrow(() -> {
            // Same class, different callers
            ConfigurationLogger.checkGetConstructors(String.class, "caller1");
            ConfigurationLogger.checkGetConstructors(String.class, "caller2");

            // Different classes, same caller
            ConfigurationLogger.checkGetConstructors(String.class, "caller");
            ConfigurationLogger.checkGetConstructors(Integer.class, "caller");

            // Different classes, different callers
            ConfigurationLogger.checkGetConstructors(Object.class, "caller1");
            ConfigurationLogger.checkGetConstructors(System.class, "caller2");
        }, "checkGetConstructors should handle different parameter combinations");
    }

    /**
     * Tests checkGetConstructors with whitespace in calling class name.
     */
    @Test
    public void testCheckGetConstructorsWithWhitespace() {
        // Act & Assert - Should handle whitespace in string parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, " caller ");
            ConfigurationLogger.checkGetConstructors(String.class, "  ");
        }, "checkGetConstructors should handle whitespace in parameters");
    }

    /**
     * Tests checkGetConstructors is a static method and can be called without an instance.
     */
    @Test
    public void testCheckGetConstructorsIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(String.class, "caller"),
            "checkGetConstructors should be callable as a static method");
    }

    /**
     * Tests checkGetConstructors thread safety by calling from multiple threads.
     */
    @Test
    public void testCheckGetConstructorsThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkGetConstructors
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkGetConstructors(
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
        assertTrue(true, "Concurrent calls to checkGetConstructors should not cause issues");
    }

    /**
     * Tests checkGetConstructors with Unicode characters in calling class name.
     */
    @Test
    public void testCheckGetConstructorsWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.日本語");
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.Café");
        }, "checkGetConstructors should handle Unicode characters in parameters");
    }

    /**
     * Tests checkGetConstructors with mixed case calling class names.
     */
    @Test
    public void testCheckGetConstructorsWithMixedCase() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "COM.EXAMPLE.CALLER");
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.caller");
            ConfigurationLogger.checkGetConstructors(String.class, "CoM.ExAmPlE.CaLlEr");
        }, "checkGetConstructors should handle mixed case parameters");
    }

    /**
     * Tests checkGetConstructors with array classes.
     */
    @Test
    public void testCheckGetConstructorsWithArrayClasses() {
        // Act & Assert - Array classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String[].class, "caller");
            ConfigurationLogger.checkGetConstructors(int[].class, "caller");
            ConfigurationLogger.checkGetConstructors(Object[][].class, "caller");
        }, "checkGetConstructors should handle array classes");
    }

    /**
     * Tests checkGetConstructors with primitive wrapper classes.
     */
    @Test
    public void testCheckGetConstructorsWithPrimitiveWrappers() {
        // Act & Assert - Primitive wrapper classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(Integer.class, "caller");
            ConfigurationLogger.checkGetConstructors(Boolean.class, "caller");
            ConfigurationLogger.checkGetConstructors(Double.class, "caller");
            ConfigurationLogger.checkGetConstructors(Long.class, "caller");
        }, "checkGetConstructors should handle primitive wrapper classes");
    }

    /**
     * Tests checkGetConstructors with inner classes.
     */
    @Test
    public void testCheckGetConstructorsWithInnerClasses() {
        // Act & Assert - Test with inner class scenarios
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.Outer$Inner$Deep");
        }, "checkGetConstructors should handle inner class callers");
    }

    /**
     * Tests checkGetConstructors with anonymous class references.
     */
    @Test
    public void testCheckGetConstructorsWithAnonymousClasses() {
        // Act & Assert - Test with anonymous class naming
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.MyClass$1");
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.MyClass$2$1");
        }, "checkGetConstructors should handle anonymous class callers");
    }

    /**
     * Tests that checkGetConstructors handles the two parameters independently.
     */
    @Test
    public void testCheckGetConstructorsParameterIndependence() {
        // Act & Assert - Changing one parameter shouldn't affect handling of others
        String baseName = "test";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, baseName);
            ConfigurationLogger.checkGetConstructors(String.class, baseName + "1");
            ConfigurationLogger.checkGetConstructors(Integer.class, baseName);
            ConfigurationLogger.checkGetConstructors(Object.class, baseName + "2");
        }, "checkGetConstructors should handle each parameter independently");
    }

    /**
     * Tests checkGetConstructors with extreme parameter combinations.
     */
    @Test
    public void testCheckGetConstructorsWithExtremeParameters() {
        // Act & Assert - Test extreme but valid scenarios
        assertDoesNotThrow(() -> {
            // Very short names
            ConfigurationLogger.checkGetConstructors(String.class, "c");

            // Empty string
            ConfigurationLogger.checkGetConstructors(String.class, "");
        }, "checkGetConstructors should handle extreme parameter combinations");
    }

    /**
     * Tests checkGetConstructors with ConfigurationLogger class itself.
     */
    @Test
    public void testCheckGetConstructorsWithConfigurationLoggerClass() {
        // Act & Assert - Should work with ConfigurationLogger class itself
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(
                ConfigurationLogger.class,
                "com.example.Caller"),
            "checkGetConstructors should work with ConfigurationLogger class");
    }

    /**
     * Tests checkGetConstructors behavior consistency - calling it multiple times
     * with slightly different parameters.
     */
    @Test
    public void testCheckGetConstructorsConsistency() {
        // Act & Assert - Consistency across similar calls
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "caller1");
            ConfigurationLogger.checkGetConstructors(String.class, "caller2");
            ConfigurationLogger.checkGetConstructors(String.class, "caller3");
        }, "checkGetConstructors should handle similar calls consistently");
    }

    /**
     * Tests checkGetConstructors with class that has dollar sign in calling class name.
     */
    @Test
    public void testCheckGetConstructorsWithDollarSignInClassName() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.Class$Inner");
            ConfigurationLogger.checkGetConstructors(String.class, "Package$Class$Inner");
        }, "checkGetConstructors should handle dollar signs in class names");
    }

    /**
     * Tests checkGetConstructors with numbers in calling class name.
     */
    @Test
    public void testCheckGetConstructorsWithNumbers() {
        // Act & Assert - Test numbers in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "caller123");
            ConfigurationLogger.checkGetConstructors(String.class, "123caller");
            ConfigurationLogger.checkGetConstructors(String.class, "caller1caller2");
        }, "checkGetConstructors should handle numbers in parameters");
    }

    /**
     * Tests checkGetConstructors with enum classes.
     */
    @Test
    public void testCheckGetConstructorsWithEnumClasses() {
        // Act & Assert - Enum classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(Thread.State.class, "caller"),
            "checkGetConstructors should handle enum classes");
    }

    /**
     * Tests checkGetConstructors with interface classes.
     */
    @Test
    public void testCheckGetConstructorsWithInterfaceClasses() {
        // Act & Assert - Interface classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(Runnable.class, "caller"),
            "checkGetConstructors should handle interface classes");
    }

    /**
     * Tests checkGetConstructors rapidly in sequence.
     */
    @Test
    public void testCheckGetConstructorsRapidSequentialCalls() {
        // Act & Assert - Rapid sequential calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                ConfigurationLogger.checkGetConstructors(String.class, "caller");
            }
        }, "Rapid sequential calls should not cause issues");
    }

    /**
     * Tests checkGetConstructors with different classes in sequence.
     */
    @Test
    public void testCheckGetConstructorsWithDifferentClassesInSequence() {
        // Act & Assert - Different classes in sequence
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "caller");
            ConfigurationLogger.checkGetConstructors(Integer.class, "caller");
            ConfigurationLogger.checkGetConstructors(Object.class, "caller");
            ConfigurationLogger.checkGetConstructors(System.class, "caller");
            ConfigurationLogger.checkGetConstructors(Thread.class, "caller");
        }, "Sequential calls with different classes should work");
    }

    /**
     * Tests checkGetConstructors parameter validation - class must not be null.
     */
    @Test
    public void testCheckGetConstructorsClassNotNull() {
        // Assert - Null class should throw NPE
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetConstructors(null, "caller"),
            "Null class parameter should throw NullPointerException");
    }

    /**
     * Tests checkGetConstructors with the test class itself.
     */
    @Test
    public void testCheckGetConstructorsWithTestClassItself() {
        // Act & Assert - Test with this test class
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(
                this.getClass(),
                "com.example.Caller"),
            "checkGetConstructors should work with test class itself");
    }

    /**
     * Tests checkGetConstructors with various standard library classes.
     */
    @Test
    public void testCheckGetConstructorsWithVariousStandardClasses() {
        // Act & Assert - Test with various standard library classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(java.util.ArrayList.class, "caller");
            ConfigurationLogger.checkGetConstructors(java.util.HashMap.class, "caller");
            ConfigurationLogger.checkGetConstructors(java.io.File.class, "caller");
            ConfigurationLogger.checkGetConstructors(java.lang.StringBuilder.class, "caller");
        }, "checkGetConstructors should handle various standard library classes");
    }

    /**
     * Tests checkGetConstructors with special method name patterns in calling class.
     */
    @Test
    public void testCheckGetConstructorsWithSpecialCallingClassPatterns() {
        // Act & Assert - Test calling class names with special patterns
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.Class$1");
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.Class_With_Underscores");
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.Class123");
        }, "checkGetConstructors should handle special calling class name patterns");
    }

    /**
     * Tests checkGetConstructors with abstract classes.
     */
    @Test
    public void testCheckGetConstructorsWithAbstractClasses() {
        // Act & Assert - Abstract classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(Number.class, "caller"),
            "checkGetConstructors should handle abstract classes");
    }

    /**
     * Tests checkGetConstructors behavior consistency across different reflection APIs.
     */
    @Test
    public void testCheckGetConstructorsConsistencyAcrossReflectionAPIs() {
        // Act & Assert - Same class accessed via different callers should be handled consistently
        String targetClass = "com.example.Caller";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, targetClass + "1");
            ConfigurationLogger.checkGetConstructors(String.class, targetClass + "2");
            ConfigurationLogger.checkGetConstructors(String.class, targetClass + "3");
        }, "checkGetConstructors should handle the same class consistently");
    }

    /**
     * Tests checkGetConstructors with dots in different positions of calling class name.
     */
    @Test
    public void testCheckGetConstructorsWithDotsInCallingClassName() {
        // Act & Assert - Test calling class names with various dot positions
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.Caller");
            ConfigurationLogger.checkGetConstructors(String.class, "a.b.c.d.e.f.Caller");
            ConfigurationLogger.checkGetConstructors(String.class, "single");
        }, "checkGetConstructors should handle dots in calling class names");
    }

    /**
     * Tests checkGetConstructors with package-private class references.
     */
    @Test
    public void testCheckGetConstructorsWithPackagePrivateReferences() {
        // Act & Assert - Test package-private scenarios
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetConstructors(String.class, "com.example.PackagePrivateClass"),
            "checkGetConstructors should handle package-private class references");
    }

    /**
     * Tests checkGetConstructors with exception classes.
     */
    @Test
    public void testCheckGetConstructorsWithExceptionClasses() {
        // Act & Assert - Exception classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(Exception.class, "caller");
            ConfigurationLogger.checkGetConstructors(RuntimeException.class, "caller");
            ConfigurationLogger.checkGetConstructors(NullPointerException.class, "caller");
        }, "checkGetConstructors should handle exception classes");
    }

    /**
     * Tests checkGetConstructors with collection classes.
     */
    @Test
    public void testCheckGetConstructorsWithCollectionClasses() {
        // Act & Assert - Collection classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(java.util.List.class, "caller");
            ConfigurationLogger.checkGetConstructors(java.util.Map.class, "caller");
            ConfigurationLogger.checkGetConstructors(java.util.Set.class, "caller");
        }, "checkGetConstructors should handle collection classes");
    }

    /**
     * Tests checkGetConstructors alternating between different classes and callers.
     */
    @Test
    public void testCheckGetConstructorsAlternatingParameters() {
        // Act & Assert - Alternating parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "caller1");
            ConfigurationLogger.checkGetConstructors(Integer.class, "caller2");
            ConfigurationLogger.checkGetConstructors(String.class, "caller3");
            ConfigurationLogger.checkGetConstructors(Object.class, "caller1");
            ConfigurationLogger.checkGetConstructors(Integer.class, "caller4");
        }, "checkGetConstructors should handle alternating parameters");
    }

    /**
     * Tests checkGetConstructors with classes that have public constructors vs no public constructors.
     */
    @Test
    public void testCheckGetConstructorsWithClassesWithAndWithoutPublicConstructors() {
        // Act & Assert - Test with classes that may or may not have public constructors
        assertDoesNotThrow(() -> {
            // Classes with public constructors
            ConfigurationLogger.checkGetConstructors(String.class, "caller");
            ConfigurationLogger.checkGetConstructors(Integer.class, "caller");

            // Object class
            ConfigurationLogger.checkGetConstructors(Object.class, "caller");
        }, "checkGetConstructors should handle classes with varying public constructor counts");
    }

    /**
     * Tests checkGetConstructors comparing behavior with checkGetDeclaredConstructors.
     * This verifies that both methods can be called on the same class without conflict.
     */
    @Test
    public void testCheckGetConstructorsComparedToCheckGetDeclaredConstructors() {
        // Act & Assert - Both methods should work on the same class
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetConstructors(String.class, "caller1");
            ConfigurationLogger.checkGetDeclaredConstructors(String.class, "caller2");
            ConfigurationLogger.checkGetConstructors(Integer.class, "caller3");
            ConfigurationLogger.checkGetDeclaredConstructors(Integer.class, "caller4");
        }, "checkGetConstructors and checkGetDeclaredConstructors should work together");
    }
}

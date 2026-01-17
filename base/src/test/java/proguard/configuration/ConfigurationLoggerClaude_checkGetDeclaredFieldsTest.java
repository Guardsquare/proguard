package proguard.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLogger#checkGetDeclaredFields(Class, String)} method.
 * Tests the reflection logging functionality for getDeclaredFields() calls.
 */
public class ConfigurationLoggerClaude_checkGetDeclaredFieldsTest {

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
     * Tests checkGetDeclaredFields with null class parameter.
     * This should cause a NullPointerException when the method tries to access the class.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithNullClass() {
        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetDeclaredFields(null, "com.example.Caller"),
            "checkGetDeclaredFields should throw NullPointerException with null class");
    }

    /**
     * Tests checkGetDeclaredFields with null calling class name.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(String.class, null),
            "checkGetDeclaredFields should handle null calling class name gracefully");
    }

    /**
     * Tests checkGetDeclaredFields with empty string calling class name.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithEmptyCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(String.class, ""),
            "checkGetDeclaredFields should handle empty calling class name gracefully");
    }

    /**
     * Tests checkGetDeclaredFields with a standard Java class.
     * Library classes should not trigger logging.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithStandardJavaClass() {
        // Act - Call with standard Java class
        ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Caller");

        // Assert - Should complete without throwing exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(Integer.class, "com.example.Caller"),
            "checkGetDeclaredFields should handle standard Java classes");
    }

    /**
     * Tests checkGetDeclaredFields with various calling class names.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithVariousCallingClassNames() {
        // Act & Assert - Test different calling class name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "SimpleCaller");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.FullyQualified");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Class$1");
        }, "checkGetDeclaredFields should handle various calling class name formats");
    }

    /**
     * Tests checkGetDeclaredFields with different Java classes.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithDifferentClasses() {
        // Act & Assert - Test various Java classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(Integer.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(Object.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(System.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(Thread.class, "caller");
        }, "checkGetDeclaredFields should handle different Java classes");
    }

    /**
     * Tests checkGetDeclaredFields with long calling class name.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithLongCallingClassName() {
        // Arrange
        String longPackage = "com.example.very.long.package.name.with.many.parts";
        String longCallingClass = longPackage + ".VeryLongCallingClassName";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(String.class, longCallingClass),
            "checkGetDeclaredFields should handle long calling class names");
    }

    /**
     * Tests checkGetDeclaredFields can be called multiple times without issues.
     */
    @Test
    public void testCheckGetDeclaredFieldsMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Caller" + i);
            }
        }, "Multiple calls to checkGetDeclaredFields should not cause issues");
    }

    /**
     * Tests checkGetDeclaredFields with same parameters multiple times (idempotency).
     */
    @Test
    public void testCheckGetDeclaredFieldsWithSameParametersMultipleTimes() {
        // Act - Call checkGetDeclaredFields multiple times with the same parameters
        Class<?> reflectedClass = String.class;
        String callingClass = "com.example.Caller";

        ConfigurationLogger.checkGetDeclaredFields(reflectedClass, callingClass);
        ConfigurationLogger.checkGetDeclaredFields(reflectedClass, callingClass);
        ConfigurationLogger.checkGetDeclaredFields(reflectedClass, callingClass);

        // Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(reflectedClass, callingClass),
            "Calling checkGetDeclaredFields multiple times with same parameters should be safe");
    }

    /**
     * Tests checkGetDeclaredFields with different combinations of parameters.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithDifferentParameterCombinations() {
        // Act & Assert - Various parameter combinations
        assertDoesNotThrow(() -> {
            // Same class, different callers
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller1");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller2");

            // Different classes, same caller
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(Integer.class, "caller");

            // Different classes, different callers
            ConfigurationLogger.checkGetDeclaredFields(Object.class, "caller1");
            ConfigurationLogger.checkGetDeclaredFields(System.class, "caller2");
        }, "checkGetDeclaredFields should handle different parameter combinations");
    }

    /**
     * Tests checkGetDeclaredFields with whitespace in calling class name.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithWhitespace() {
        // Act & Assert - Should handle whitespace in string parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, " caller ");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "  ");
        }, "checkGetDeclaredFields should handle whitespace in parameters");
    }

    /**
     * Tests checkGetDeclaredFields is a static method and can be called without an instance.
     */
    @Test
    public void testCheckGetDeclaredFieldsIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller"),
            "checkGetDeclaredFields should be callable as a static method");
    }

    /**
     * Tests checkGetDeclaredFields thread safety by calling from multiple threads.
     */
    @Test
    public void testCheckGetDeclaredFieldsThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkGetDeclaredFields
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkGetDeclaredFields(
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
        assertTrue(true, "Concurrent calls to checkGetDeclaredFields should not cause issues");
    }

    /**
     * Tests checkGetDeclaredFields with Unicode characters in calling class name.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.日本語");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Café");
        }, "checkGetDeclaredFields should handle Unicode characters in parameters");
    }

    /**
     * Tests checkGetDeclaredFields with mixed case calling class names.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithMixedCase() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "COM.EXAMPLE.CALLER");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.caller");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "CoM.ExAmPlE.CaLlEr");
        }, "checkGetDeclaredFields should handle mixed case parameters");
    }

    /**
     * Tests checkGetDeclaredFields with array classes.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithArrayClasses() {
        // Act & Assert - Array classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String[].class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(int[].class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(Object[][].class, "caller");
        }, "checkGetDeclaredFields should handle array classes");
    }

    /**
     * Tests checkGetDeclaredFields with primitive wrapper classes.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithPrimitiveWrappers() {
        // Act & Assert - Primitive wrapper classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(Integer.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(Boolean.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(Double.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(Long.class, "caller");
        }, "checkGetDeclaredFields should handle primitive wrapper classes");
    }

    /**
     * Tests checkGetDeclaredFields with inner classes.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithInnerClasses() {
        // Act & Assert - Test with inner class scenarios
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Outer$Inner$Deep");
        }, "checkGetDeclaredFields should handle inner class callers");
    }

    /**
     * Tests checkGetDeclaredFields with anonymous class references.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithAnonymousClasses() {
        // Act & Assert - Test with anonymous class naming
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.MyClass$1");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.MyClass$2$1");
        }, "checkGetDeclaredFields should handle anonymous class callers");
    }

    /**
     * Tests that checkGetDeclaredFields handles the two parameters independently.
     */
    @Test
    public void testCheckGetDeclaredFieldsParameterIndependence() {
        // Act & Assert - Changing one parameter shouldn't affect handling of others
        String baseName = "test";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, baseName);
            ConfigurationLogger.checkGetDeclaredFields(String.class, baseName + "1");
            ConfigurationLogger.checkGetDeclaredFields(Integer.class, baseName);
            ConfigurationLogger.checkGetDeclaredFields(Object.class, baseName + "2");
        }, "checkGetDeclaredFields should handle each parameter independently");
    }

    /**
     * Tests checkGetDeclaredFields with extreme parameter combinations.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithExtremeParameters() {
        // Act & Assert - Test extreme but valid scenarios
        assertDoesNotThrow(() -> {
            // Very short names
            ConfigurationLogger.checkGetDeclaredFields(String.class, "c");

            // Empty string
            ConfigurationLogger.checkGetDeclaredFields(String.class, "");
        }, "checkGetDeclaredFields should handle extreme parameter combinations");
    }

    /**
     * Tests checkGetDeclaredFields with ConfigurationLogger class itself.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithConfigurationLoggerClass() {
        // Act & Assert - Should work with ConfigurationLogger class itself
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(
                ConfigurationLogger.class,
                "com.example.Caller"),
            "checkGetDeclaredFields should work with ConfigurationLogger class");
    }

    /**
     * Tests checkGetDeclaredFields behavior consistency - calling it multiple times
     * with slightly different parameters.
     */
    @Test
    public void testCheckGetDeclaredFieldsConsistency() {
        // Act & Assert - Consistency across similar calls
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller1");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller2");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller3");
        }, "checkGetDeclaredFields should handle similar calls consistently");
    }

    /**
     * Tests checkGetDeclaredFields with class that has dollar sign in calling class name.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithDollarSignInClassName() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Class$Inner");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "Package$Class$Inner");
        }, "checkGetDeclaredFields should handle dollar signs in class names");
    }

    /**
     * Tests checkGetDeclaredFields with numbers in calling class name.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithNumbers() {
        // Act & Assert - Test numbers in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller123");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "123caller");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller1caller2");
        }, "checkGetDeclaredFields should handle numbers in parameters");
    }

    /**
     * Tests checkGetDeclaredFields with enum classes.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithEnumClasses() {
        // Act & Assert - Enum classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(Thread.State.class, "caller"),
            "checkGetDeclaredFields should handle enum classes");
    }

    /**
     * Tests checkGetDeclaredFields with interface classes.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithInterfaceClasses() {
        // Act & Assert - Interface classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(Runnable.class, "caller"),
            "checkGetDeclaredFields should handle interface classes");
    }

    /**
     * Tests checkGetDeclaredFields rapidly in sequence.
     */
    @Test
    public void testCheckGetDeclaredFieldsRapidSequentialCalls() {
        // Act & Assert - Rapid sequential calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                ConfigurationLogger.checkGetDeclaredFields(String.class, "caller");
            }
        }, "Rapid sequential calls should not cause issues");
    }

    /**
     * Tests checkGetDeclaredFields with different classes in sequence.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithDifferentClassesInSequence() {
        // Act & Assert - Different classes in sequence
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(Integer.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(Object.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(System.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(Thread.class, "caller");
        }, "Sequential calls with different classes should work");
    }

    /**
     * Tests checkGetDeclaredFields parameter validation - class must not be null.
     */
    @Test
    public void testCheckGetDeclaredFieldsClassNotNull() {
        // Assert - Null class should throw NPE
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetDeclaredFields(null, "caller"),
            "Null class parameter should throw NullPointerException");
    }

    /**
     * Tests checkGetDeclaredFields with the test class itself.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithTestClassItself() {
        // Act & Assert - Test with this test class
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(
                this.getClass(),
                "com.example.Caller"),
            "checkGetDeclaredFields should work with test class itself");
    }

    /**
     * Tests checkGetDeclaredFields with various standard library classes.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithVariousStandardClasses() {
        // Act & Assert - Test with various standard library classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(java.util.ArrayList.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(java.util.HashMap.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(java.io.File.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(java.lang.StringBuilder.class, "caller");
        }, "checkGetDeclaredFields should handle various standard library classes");
    }

    /**
     * Tests checkGetDeclaredFields with special method name patterns in calling class.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithSpecialCallingClassPatterns() {
        // Act & Assert - Test calling class names with special patterns
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Class$1");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Class_With_Underscores");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Class123");
        }, "checkGetDeclaredFields should handle special calling class name patterns");
    }

    /**
     * Tests checkGetDeclaredFields with abstract classes.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithAbstractClasses() {
        // Act & Assert - Abstract classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(Number.class, "caller"),
            "checkGetDeclaredFields should handle abstract classes");
    }

    /**
     * Tests checkGetDeclaredFields behavior consistency across different reflection APIs.
     */
    @Test
    public void testCheckGetDeclaredFieldsConsistencyAcrossReflectionAPIs() {
        // Act & Assert - Same class accessed via different callers should be handled consistently
        String targetClass = "com.example.Caller";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, targetClass + "1");
            ConfigurationLogger.checkGetDeclaredFields(String.class, targetClass + "2");
            ConfigurationLogger.checkGetDeclaredFields(String.class, targetClass + "3");
        }, "checkGetDeclaredFields should handle the same class consistently");
    }

    /**
     * Tests checkGetDeclaredFields with dots in different positions of calling class name.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithDotsInCallingClassName() {
        // Act & Assert - Test calling class names with various dot positions
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.Caller");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "a.b.c.d.e.f.Caller");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "single");
        }, "checkGetDeclaredFields should handle dots in calling class names");
    }

    /**
     * Tests checkGetDeclaredFields with package-private class references.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithPackagePrivateReferences() {
        // Act & Assert - Test package-private scenarios
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredFields(String.class, "com.example.PackagePrivateClass"),
            "checkGetDeclaredFields should handle package-private class references");
    }

    /**
     * Tests checkGetDeclaredFields with exception classes.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithExceptionClasses() {
        // Act & Assert - Exception classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(Exception.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(RuntimeException.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(NullPointerException.class, "caller");
        }, "checkGetDeclaredFields should handle exception classes");
    }

    /**
     * Tests checkGetDeclaredFields with collection classes.
     */
    @Test
    public void testCheckGetDeclaredFieldsWithCollectionClasses() {
        // Act & Assert - Collection classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(java.util.List.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(java.util.Map.class, "caller");
            ConfigurationLogger.checkGetDeclaredFields(java.util.Set.class, "caller");
        }, "checkGetDeclaredFields should handle collection classes");
    }

    /**
     * Tests checkGetDeclaredFields alternating between different classes and callers.
     */
    @Test
    public void testCheckGetDeclaredFieldsAlternatingParameters() {
        // Act & Assert - Alternating parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller1");
            ConfigurationLogger.checkGetDeclaredFields(Integer.class, "caller2");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller3");
            ConfigurationLogger.checkGetDeclaredFields(Object.class, "caller1");
            ConfigurationLogger.checkGetDeclaredFields(Integer.class, "caller4");
        }, "checkGetDeclaredFields should handle alternating parameters");
    }
}

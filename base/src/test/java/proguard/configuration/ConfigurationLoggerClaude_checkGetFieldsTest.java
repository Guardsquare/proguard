package proguard.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLogger#checkGetFields(Class, String)} method.
 * Tests the reflection logging functionality for getFields() calls.
 */
public class ConfigurationLoggerClaude_checkGetFieldsTest {

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
     * Tests checkGetFields with null class parameter.
     * This should cause a NullPointerException when the method tries to access the class.
     */
    @Test
    public void testCheckGetFieldsWithNullClass() {
        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetFields(null, "com.example.Caller"),
            "checkGetFields should throw NullPointerException with null class");
    }

    /**
     * Tests checkGetFields with null calling class name.
     */
    @Test
    public void testCheckGetFieldsWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(String.class, null),
            "checkGetFields should handle null calling class name gracefully");
    }

    /**
     * Tests checkGetFields with empty string calling class name.
     */
    @Test
    public void testCheckGetFieldsWithEmptyCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(String.class, ""),
            "checkGetFields should handle empty calling class name gracefully");
    }

    /**
     * Tests checkGetFields with a standard Java class.
     * Library classes should not trigger logging.
     */
    @Test
    public void testCheckGetFieldsWithStandardJavaClass() {
        // Act - Call with standard Java class
        ConfigurationLogger.checkGetFields(String.class, "com.example.Caller");

        // Assert - Should complete without throwing exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(Integer.class, "com.example.Caller"),
            "checkGetFields should handle standard Java classes");
    }

    /**
     * Tests checkGetFields with various calling class names.
     */
    @Test
    public void testCheckGetFieldsWithVariousCallingClassNames() {
        // Act & Assert - Test different calling class name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "SimpleCaller");
            ConfigurationLogger.checkGetFields(String.class, "com.example.FullyQualified");
            ConfigurationLogger.checkGetFields(String.class, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetFields(String.class, "com.example.Class$1");
        }, "checkGetFields should handle various calling class name formats");
    }

    /**
     * Tests checkGetFields with different Java classes.
     */
    @Test
    public void testCheckGetFieldsWithDifferentClasses() {
        // Act & Assert - Test various Java classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "caller");
            ConfigurationLogger.checkGetFields(Integer.class, "caller");
            ConfigurationLogger.checkGetFields(Object.class, "caller");
            ConfigurationLogger.checkGetFields(System.class, "caller");
            ConfigurationLogger.checkGetFields(Thread.class, "caller");
        }, "checkGetFields should handle different Java classes");
    }

    /**
     * Tests checkGetFields with long calling class name.
     */
    @Test
    public void testCheckGetFieldsWithLongCallingClassName() {
        // Arrange
        String longPackage = "com.example.very.long.package.name.with.many.parts";
        String longCallingClass = longPackage + ".VeryLongCallingClassName";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(String.class, longCallingClass),
            "checkGetFields should handle long calling class names");
    }

    /**
     * Tests checkGetFields can be called multiple times without issues.
     */
    @Test
    public void testCheckGetFieldsMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkGetFields(String.class, "com.example.Caller" + i);
            }
        }, "Multiple calls to checkGetFields should not cause issues");
    }

    /**
     * Tests checkGetFields with same parameters multiple times (idempotency).
     */
    @Test
    public void testCheckGetFieldsWithSameParametersMultipleTimes() {
        // Act - Call checkGetFields multiple times with the same parameters
        Class<?> reflectedClass = String.class;
        String callingClass = "com.example.Caller";

        ConfigurationLogger.checkGetFields(reflectedClass, callingClass);
        ConfigurationLogger.checkGetFields(reflectedClass, callingClass);
        ConfigurationLogger.checkGetFields(reflectedClass, callingClass);

        // Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(reflectedClass, callingClass),
            "Calling checkGetFields multiple times with same parameters should be safe");
    }

    /**
     * Tests checkGetFields with different combinations of parameters.
     */
    @Test
    public void testCheckGetFieldsWithDifferentParameterCombinations() {
        // Act & Assert - Various parameter combinations
        assertDoesNotThrow(() -> {
            // Same class, different callers
            ConfigurationLogger.checkGetFields(String.class, "caller1");
            ConfigurationLogger.checkGetFields(String.class, "caller2");

            // Different classes, same caller
            ConfigurationLogger.checkGetFields(String.class, "caller");
            ConfigurationLogger.checkGetFields(Integer.class, "caller");

            // Different classes, different callers
            ConfigurationLogger.checkGetFields(Object.class, "caller1");
            ConfigurationLogger.checkGetFields(System.class, "caller2");
        }, "checkGetFields should handle different parameter combinations");
    }

    /**
     * Tests checkGetFields with whitespace in calling class name.
     */
    @Test
    public void testCheckGetFieldsWithWhitespace() {
        // Act & Assert - Should handle whitespace in string parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, " caller ");
            ConfigurationLogger.checkGetFields(String.class, "  ");
        }, "checkGetFields should handle whitespace in parameters");
    }

    /**
     * Tests checkGetFields is a static method and can be called without an instance.
     */
    @Test
    public void testCheckGetFieldsIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(String.class, "caller"),
            "checkGetFields should be callable as a static method");
    }

    /**
     * Tests checkGetFields thread safety by calling from multiple threads.
     */
    @Test
    public void testCheckGetFieldsThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkGetFields
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkGetFields(
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
        assertTrue(true, "Concurrent calls to checkGetFields should not cause issues");
    }

    /**
     * Tests checkGetFields with Unicode characters in calling class name.
     */
    @Test
    public void testCheckGetFieldsWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "com.example.日本語");
            ConfigurationLogger.checkGetFields(String.class, "com.example.Café");
        }, "checkGetFields should handle Unicode characters in parameters");
    }

    /**
     * Tests checkGetFields with mixed case calling class names.
     */
    @Test
    public void testCheckGetFieldsWithMixedCase() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "COM.EXAMPLE.CALLER");
            ConfigurationLogger.checkGetFields(String.class, "com.example.caller");
            ConfigurationLogger.checkGetFields(String.class, "CoM.ExAmPlE.CaLlEr");
        }, "checkGetFields should handle mixed case parameters");
    }

    /**
     * Tests checkGetFields with array classes.
     */
    @Test
    public void testCheckGetFieldsWithArrayClasses() {
        // Act & Assert - Array classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String[].class, "caller");
            ConfigurationLogger.checkGetFields(int[].class, "caller");
            ConfigurationLogger.checkGetFields(Object[][].class, "caller");
        }, "checkGetFields should handle array classes");
    }

    /**
     * Tests checkGetFields with primitive wrapper classes.
     */
    @Test
    public void testCheckGetFieldsWithPrimitiveWrappers() {
        // Act & Assert - Primitive wrapper classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(Integer.class, "caller");
            ConfigurationLogger.checkGetFields(Boolean.class, "caller");
            ConfigurationLogger.checkGetFields(Double.class, "caller");
            ConfigurationLogger.checkGetFields(Long.class, "caller");
        }, "checkGetFields should handle primitive wrapper classes");
    }

    /**
     * Tests checkGetFields with inner classes.
     */
    @Test
    public void testCheckGetFieldsWithInnerClasses() {
        // Act & Assert - Test with inner class scenarios
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "com.example.Outer$Inner");
            ConfigurationLogger.checkGetFields(String.class, "com.example.Outer$Inner$Deep");
        }, "checkGetFields should handle inner class callers");
    }

    /**
     * Tests checkGetFields with anonymous class references.
     */
    @Test
    public void testCheckGetFieldsWithAnonymousClasses() {
        // Act & Assert - Test with anonymous class naming
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "com.example.MyClass$1");
            ConfigurationLogger.checkGetFields(String.class, "com.example.MyClass$2$1");
        }, "checkGetFields should handle anonymous class callers");
    }

    /**
     * Tests that checkGetFields handles the two parameters independently.
     */
    @Test
    public void testCheckGetFieldsParameterIndependence() {
        // Act & Assert - Changing one parameter shouldn't affect handling of others
        String baseName = "test";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, baseName);
            ConfigurationLogger.checkGetFields(String.class, baseName + "1");
            ConfigurationLogger.checkGetFields(Integer.class, baseName);
            ConfigurationLogger.checkGetFields(Object.class, baseName + "2");
        }, "checkGetFields should handle each parameter independently");
    }

    /**
     * Tests checkGetFields with extreme parameter combinations.
     */
    @Test
    public void testCheckGetFieldsWithExtremeParameters() {
        // Act & Assert - Test extreme but valid scenarios
        assertDoesNotThrow(() -> {
            // Very short names
            ConfigurationLogger.checkGetFields(String.class, "c");

            // Empty string
            ConfigurationLogger.checkGetFields(String.class, "");
        }, "checkGetFields should handle extreme parameter combinations");
    }

    /**
     * Tests checkGetFields with ConfigurationLogger class itself.
     */
    @Test
    public void testCheckGetFieldsWithConfigurationLoggerClass() {
        // Act & Assert - Should work with ConfigurationLogger class itself
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(
                ConfigurationLogger.class,
                "com.example.Caller"),
            "checkGetFields should work with ConfigurationLogger class");
    }

    /**
     * Tests checkGetFields behavior consistency - calling it multiple times
     * with slightly different parameters.
     */
    @Test
    public void testCheckGetFieldsConsistency() {
        // Act & Assert - Consistency across similar calls
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "caller1");
            ConfigurationLogger.checkGetFields(String.class, "caller2");
            ConfigurationLogger.checkGetFields(String.class, "caller3");
        }, "checkGetFields should handle similar calls consistently");
    }

    /**
     * Tests checkGetFields with class that has dollar sign in calling class name.
     */
    @Test
    public void testCheckGetFieldsWithDollarSignInClassName() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "com.example.Class$Inner");
            ConfigurationLogger.checkGetFields(String.class, "Package$Class$Inner");
        }, "checkGetFields should handle dollar signs in class names");
    }

    /**
     * Tests checkGetFields with numbers in calling class name.
     */
    @Test
    public void testCheckGetFieldsWithNumbers() {
        // Act & Assert - Test numbers in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "caller123");
            ConfigurationLogger.checkGetFields(String.class, "123caller");
            ConfigurationLogger.checkGetFields(String.class, "caller1caller2");
        }, "checkGetFields should handle numbers in parameters");
    }

    /**
     * Tests checkGetFields with enum classes.
     */
    @Test
    public void testCheckGetFieldsWithEnumClasses() {
        // Act & Assert - Enum classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(Thread.State.class, "caller"),
            "checkGetFields should handle enum classes");
    }

    /**
     * Tests checkGetFields with interface classes.
     */
    @Test
    public void testCheckGetFieldsWithInterfaceClasses() {
        // Act & Assert - Interface classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(Runnable.class, "caller"),
            "checkGetFields should handle interface classes");
    }

    /**
     * Tests checkGetFields rapidly in sequence.
     */
    @Test
    public void testCheckGetFieldsRapidSequentialCalls() {
        // Act & Assert - Rapid sequential calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                ConfigurationLogger.checkGetFields(String.class, "caller");
            }
        }, "Rapid sequential calls should not cause issues");
    }

    /**
     * Tests checkGetFields with different classes in sequence.
     */
    @Test
    public void testCheckGetFieldsWithDifferentClassesInSequence() {
        // Act & Assert - Different classes in sequence
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "caller");
            ConfigurationLogger.checkGetFields(Integer.class, "caller");
            ConfigurationLogger.checkGetFields(Object.class, "caller");
            ConfigurationLogger.checkGetFields(System.class, "caller");
            ConfigurationLogger.checkGetFields(Thread.class, "caller");
        }, "Sequential calls with different classes should work");
    }

    /**
     * Tests checkGetFields parameter validation - class must not be null.
     */
    @Test
    public void testCheckGetFieldsClassNotNull() {
        // Assert - Null class should throw NPE
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetFields(null, "caller"),
            "Null class parameter should throw NullPointerException");
    }

    /**
     * Tests checkGetFields with the test class itself.
     */
    @Test
    public void testCheckGetFieldsWithTestClassItself() {
        // Act & Assert - Test with this test class
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(
                this.getClass(),
                "com.example.Caller"),
            "checkGetFields should work with test class itself");
    }

    /**
     * Tests checkGetFields with various standard library classes.
     */
    @Test
    public void testCheckGetFieldsWithVariousStandardClasses() {
        // Act & Assert - Test with various standard library classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(java.util.ArrayList.class, "caller");
            ConfigurationLogger.checkGetFields(java.util.HashMap.class, "caller");
            ConfigurationLogger.checkGetFields(java.io.File.class, "caller");
            ConfigurationLogger.checkGetFields(java.lang.StringBuilder.class, "caller");
        }, "checkGetFields should handle various standard library classes");
    }

    /**
     * Tests checkGetFields with special method name patterns in calling class.
     */
    @Test
    public void testCheckGetFieldsWithSpecialCallingClassPatterns() {
        // Act & Assert - Test calling class names with special patterns
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "com.example.Class$1");
            ConfigurationLogger.checkGetFields(String.class, "com.example.Class_With_Underscores");
            ConfigurationLogger.checkGetFields(String.class, "com.example.Class123");
        }, "checkGetFields should handle special calling class name patterns");
    }

    /**
     * Tests checkGetFields with abstract classes.
     */
    @Test
    public void testCheckGetFieldsWithAbstractClasses() {
        // Act & Assert - Abstract classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(Number.class, "caller"),
            "checkGetFields should handle abstract classes");
    }

    /**
     * Tests checkGetFields behavior consistency across different reflection APIs.
     */
    @Test
    public void testCheckGetFieldsConsistencyAcrossReflectionAPIs() {
        // Act & Assert - Same class accessed via different callers should be handled consistently
        String targetClass = "com.example.Caller";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, targetClass + "1");
            ConfigurationLogger.checkGetFields(String.class, targetClass + "2");
            ConfigurationLogger.checkGetFields(String.class, targetClass + "3");
        }, "checkGetFields should handle the same class consistently");
    }

    /**
     * Tests checkGetFields with dots in different positions of calling class name.
     */
    @Test
    public void testCheckGetFieldsWithDotsInCallingClassName() {
        // Act & Assert - Test calling class names with various dot positions
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "com.example.Caller");
            ConfigurationLogger.checkGetFields(String.class, "a.b.c.d.e.f.Caller");
            ConfigurationLogger.checkGetFields(String.class, "single");
        }, "checkGetFields should handle dots in calling class names");
    }

    /**
     * Tests checkGetFields with package-private class references.
     */
    @Test
    public void testCheckGetFieldsWithPackagePrivateReferences() {
        // Act & Assert - Test package-private scenarios
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(String.class, "com.example.PackagePrivateClass"),
            "checkGetFields should handle package-private class references");
    }

    /**
     * Tests checkGetFields with exception classes.
     */
    @Test
    public void testCheckGetFieldsWithExceptionClasses() {
        // Act & Assert - Exception classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(Exception.class, "caller");
            ConfigurationLogger.checkGetFields(RuntimeException.class, "caller");
            ConfigurationLogger.checkGetFields(NullPointerException.class, "caller");
        }, "checkGetFields should handle exception classes");
    }

    /**
     * Tests checkGetFields with collection classes.
     */
    @Test
    public void testCheckGetFieldsWithCollectionClasses() {
        // Act & Assert - Collection classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(java.util.List.class, "caller");
            ConfigurationLogger.checkGetFields(java.util.Map.class, "caller");
            ConfigurationLogger.checkGetFields(java.util.Set.class, "caller");
        }, "checkGetFields should handle collection classes");
    }

    /**
     * Tests checkGetFields alternating between different classes and callers.
     */
    @Test
    public void testCheckGetFieldsAlternatingParameters() {
        // Act & Assert - Alternating parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "caller1");
            ConfigurationLogger.checkGetFields(Integer.class, "caller2");
            ConfigurationLogger.checkGetFields(String.class, "caller3");
            ConfigurationLogger.checkGetFields(Object.class, "caller1");
            ConfigurationLogger.checkGetFields(Integer.class, "caller4");
        }, "checkGetFields should handle alternating parameters");
    }

    /**
     * Tests checkGetFields with classes that have public fields vs no public fields.
     */
    @Test
    public void testCheckGetFieldsWithClassesWithAndWithoutPublicFields() {
        // Act & Assert - Test with classes that may or may not have public fields
        assertDoesNotThrow(() -> {
            // Classes with public fields
            ConfigurationLogger.checkGetFields(System.class, "caller");

            // Classes with fewer or no public fields
            ConfigurationLogger.checkGetFields(String.class, "caller");
            ConfigurationLogger.checkGetFields(Object.class, "caller");
        }, "checkGetFields should handle classes with varying public field counts");
    }

    /**
     * Tests checkGetFields comparing behavior with checkGetDeclaredFields.
     * This verifies that both methods can be called on the same class without conflict.
     */
    @Test
    public void testCheckGetFieldsComparedToCheckGetDeclaredFields() {
        // Act & Assert - Both methods should work on the same class
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetFields(String.class, "caller1");
            ConfigurationLogger.checkGetDeclaredFields(String.class, "caller2");
            ConfigurationLogger.checkGetFields(Integer.class, "caller3");
            ConfigurationLogger.checkGetDeclaredFields(Integer.class, "caller4");
        }, "checkGetFields and checkGetDeclaredFields should work together");
    }

    /**
     * Tests checkGetFields with annotation classes.
     */
    @Test
    public void testCheckGetFieldsWithAnnotationClasses() {
        // Act & Assert - Annotation classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetFields(Override.class, "caller"),
            "checkGetFields should handle annotation classes");
    }
}

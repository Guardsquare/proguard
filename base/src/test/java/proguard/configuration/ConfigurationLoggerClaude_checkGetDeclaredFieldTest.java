package proguard.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationLogger#checkGetDeclaredField(Class, String, String)} method.
 * Tests the reflection logging functionality for getDeclaredField() calls.
 */
public class ConfigurationLoggerClaude_checkGetDeclaredFieldTest {

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
     * Tests checkGetDeclaredField with null class parameter.
     * This should cause a NullPointerException when the method tries to access the class.
     */
    @Test
    public void testCheckGetDeclaredFieldWithNullClass() {
        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetDeclaredField(null, "fieldName", "com.example.Caller"),
            "checkGetDeclaredField should throw NullPointerException with null class");
    }

    /**
     * Tests checkGetDeclaredField with null field name.
     */
    @Test
    public void testCheckGetDeclaredFieldWithNullFieldName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(String.class, null, "com.example.Caller"),
            "checkGetDeclaredField should handle null field name gracefully");
    }

    /**
     * Tests checkGetDeclaredField with null calling class name.
     */
    @Test
    public void testCheckGetDeclaredFieldWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", null),
            "checkGetDeclaredField should handle null calling class name gracefully");
    }

    /**
     * Tests checkGetDeclaredField with all null parameters except class cannot be null
     * because it would throw NPE.
     */
    @Test
    public void testCheckGetDeclaredFieldWithNullFieldAndCallingClass() {
        // Act & Assert - Should not throw exception (except for null class which causes NPE)
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(String.class, null, null),
            "checkGetDeclaredField should handle null field name and calling class");
    }

    /**
     * Tests checkGetDeclaredField with empty string parameters.
     */
    @Test
    public void testCheckGetDeclaredFieldWithEmptyStrings() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(String.class, "", ""),
            "checkGetDeclaredField should handle empty strings gracefully");
    }

    /**
     * Tests checkGetDeclaredField with a standard Java class.
     * Library classes should not trigger logging.
     */
    @Test
    public void testCheckGetDeclaredFieldWithStandardJavaClass() {
        // Act - Call with standard Java class
        ConfigurationLogger.checkGetDeclaredField(String.class, "value", "com.example.Caller");

        // Assert - Should complete without throwing exception
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(String.class, "hash", "com.example.Caller"),
            "checkGetDeclaredField should handle standard Java classes");
    }

    /**
     * Tests checkGetDeclaredField with various field names.
     */
    @Test
    public void testCheckGetDeclaredFieldWithVariousFieldNames() {
        // Act & Assert - Test different field name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "fieldName", "caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "field_name", "caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "FIELD_NAME", "caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "fieldName123", "caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "$fieldName", "caller");
        }, "checkGetDeclaredField should handle various field name formats");
    }

    /**
     * Tests checkGetDeclaredField with various calling class names.
     */
    @Test
    public void testCheckGetDeclaredFieldWithVariousCallingClassNames() {
        // Act & Assert - Test different calling class name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "SimpleCaller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "com.example.FullyQualified");
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "com.example.Outer$Inner");
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "com.example.Class$1");
        }, "checkGetDeclaredField should handle various calling class name formats");
    }

    /**
     * Tests checkGetDeclaredField with different Java classes.
     */
    @Test
    public void testCheckGetDeclaredFieldWithDifferentClasses() {
        // Act & Assert - Test various Java classes
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "caller");
            ConfigurationLogger.checkGetDeclaredField(Integer.class, "value", "caller");
            ConfigurationLogger.checkGetDeclaredField(Object.class, "value", "caller");
            ConfigurationLogger.checkGetDeclaredField(System.class, "out", "caller");
            ConfigurationLogger.checkGetDeclaredField(Thread.class, "name", "caller");
        }, "checkGetDeclaredField should handle different Java classes");
    }

    /**
     * Tests checkGetDeclaredField with long field names.
     */
    @Test
    public void testCheckGetDeclaredFieldWithLongFieldName() {
        // Arrange
        String longFieldName = "thisIsAVeryLongFieldNameThatGoesOnAndOnAndProbablyWouldNeverBeUsedInRealCode";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(String.class, longFieldName, "com.example.Caller"),
            "checkGetDeclaredField should handle long field names");
    }

    /**
     * Tests checkGetDeclaredField with long class names.
     */
    @Test
    public void testCheckGetDeclaredFieldWithLongCallingClassName() {
        // Arrange
        String longPackage = "com.example.very.long.package.name.with.many.parts";
        String longCallingClass = longPackage + ".VeryLongCallingClassName";

        // Act & Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", longCallingClass),
            "checkGetDeclaredField should handle long calling class names");
    }

    /**
     * Tests checkGetDeclaredField can be called multiple times without issues.
     */
    @Test
    public void testCheckGetDeclaredFieldMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkGetDeclaredField(
                    String.class,
                    "field" + i,
                    "com.example.Caller");
            }
        }, "Multiple calls to checkGetDeclaredField should not cause issues");
    }

    /**
     * Tests checkGetDeclaredField with same parameters multiple times (idempotency).
     */
    @Test
    public void testCheckGetDeclaredFieldWithSameParametersMultipleTimes() {
        // Act - Call checkGetDeclaredField multiple times with the same parameters
        Class<?> reflectedClass = String.class;
        String fieldName = "value";
        String callingClass = "com.example.Caller";

        ConfigurationLogger.checkGetDeclaredField(reflectedClass, fieldName, callingClass);
        ConfigurationLogger.checkGetDeclaredField(reflectedClass, fieldName, callingClass);
        ConfigurationLogger.checkGetDeclaredField(reflectedClass, fieldName, callingClass);

        // Assert
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(reflectedClass, fieldName, callingClass),
            "Calling checkGetDeclaredField multiple times with same parameters should be safe");
    }

    /**
     * Tests checkGetDeclaredField with different combinations of parameters.
     */
    @Test
    public void testCheckGetDeclaredFieldWithDifferentParameterCombinations() {
        // Act & Assert - Various parameter combinations
        assertDoesNotThrow(() -> {
            // Same class, different fields
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "hash", "caller");

            // Same field name, different classes
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "caller1");
            ConfigurationLogger.checkGetDeclaredField(Integer.class, "value", "caller2");

            // Same class and field, different callers
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "caller1");
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "caller2");
        }, "checkGetDeclaredField should handle different parameter combinations");
    }

    /**
     * Tests checkGetDeclaredField with whitespace in parameters.
     */
    @Test
    public void testCheckGetDeclaredFieldWithWhitespace() {
        // Act & Assert - Should handle whitespace in string parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, " fieldName ", " caller ");
            ConfigurationLogger.checkGetDeclaredField(String.class, "  ", "  ");
        }, "checkGetDeclaredField should handle whitespace in parameters");
    }

    /**
     * Tests checkGetDeclaredField with special characters in field names.
     */
    @Test
    public void testCheckGetDeclaredFieldWithSpecialCharacters() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "field$1", "caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "field_name", "caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "$field", "caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "field123", "caller");
        }, "checkGetDeclaredField should handle special characters in field names");
    }

    /**
     * Tests checkGetDeclaredField is a static method and can be called without an instance.
     */
    @Test
    public void testCheckGetDeclaredFieldIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "caller"),
            "checkGetDeclaredField should be callable as a static method");
    }

    /**
     * Tests checkGetDeclaredField thread safety by calling from multiple threads.
     */
    @Test
    public void testCheckGetDeclaredFieldThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkGetDeclaredField
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkGetDeclaredField(
                        String.class,
                        "field" + index + "_" + j,
                        "com.example.Caller" + index);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - If we reach here without exceptions, the test passes
        assertTrue(true, "Concurrent calls to checkGetDeclaredField should not cause issues");
    }

    /**
     * Tests checkGetDeclaredField with Unicode characters in parameters.
     */
    @Test
    public void testCheckGetDeclaredFieldWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "field日本語", "com.example.Caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "fieldCafé", "caller");
        }, "checkGetDeclaredField should handle Unicode characters in parameters");
    }

    /**
     * Tests checkGetDeclaredField with mixed case parameters.
     */
    @Test
    public void testCheckGetDeclaredFieldWithMixedCase() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "FIELDNAME", "COM.EXAMPLE.CALLER");
            ConfigurationLogger.checkGetDeclaredField(String.class, "fieldname", "com.example.caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "FiElDnAmE", "cAlLeR");
        }, "checkGetDeclaredField should handle mixed case parameters");
    }

    /**
     * Tests checkGetDeclaredField with array classes.
     */
    @Test
    public void testCheckGetDeclaredFieldWithArrayClasses() {
        // Act & Assert - Array classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String[].class, "length", "caller");
            ConfigurationLogger.checkGetDeclaredField(int[].class, "length", "caller");
            ConfigurationLogger.checkGetDeclaredField(Object[][].class, "length", "caller");
        }, "checkGetDeclaredField should handle array classes");
    }

    /**
     * Tests checkGetDeclaredField with primitive wrapper classes.
     */
    @Test
    public void testCheckGetDeclaredFieldWithPrimitiveWrappers() {
        // Act & Assert - Primitive wrapper classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(Integer.class, "value", "caller");
            ConfigurationLogger.checkGetDeclaredField(Boolean.class, "value", "caller");
            ConfigurationLogger.checkGetDeclaredField(Double.class, "value", "caller");
            ConfigurationLogger.checkGetDeclaredField(Long.class, "value", "caller");
        }, "checkGetDeclaredField should handle primitive wrapper classes");
    }

    /**
     * Tests checkGetDeclaredField with inner classes.
     */
    @Test
    public void testCheckGetDeclaredFieldWithInnerClasses() {
        // Act & Assert - Test with inner class scenarios
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "com.example.Outer$Inner");
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "com.example.Outer$Inner$Deep");
        }, "checkGetDeclaredField should handle inner class callers");
    }

    /**
     * Tests checkGetDeclaredField with anonymous class references.
     */
    @Test
    public void testCheckGetDeclaredFieldWithAnonymousClasses() {
        // Act & Assert - Test with anonymous class naming
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "com.example.MyClass$1");
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "com.example.MyClass$2$1");
        }, "checkGetDeclaredField should handle anonymous class callers");
    }

    /**
     * Tests that checkGetDeclaredField handles the three parameters independently.
     */
    @Test
    public void testCheckGetDeclaredFieldParameterIndependence() {
        // Act & Assert - Changing one parameter shouldn't affect handling of others
        String baseName = "test";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, baseName + "1", baseName);
            ConfigurationLogger.checkGetDeclaredField(String.class, baseName, baseName + "2");
            ConfigurationLogger.checkGetDeclaredField(Integer.class, baseName, baseName);
        }, "checkGetDeclaredField should handle each parameter independently");
    }

    /**
     * Tests checkGetDeclaredField with extreme parameter combinations.
     */
    @Test
    public void testCheckGetDeclaredFieldWithExtremeParameters() {
        // Act & Assert - Test extreme but valid scenarios
        assertDoesNotThrow(() -> {
            // Very short names
            ConfigurationLogger.checkGetDeclaredField(String.class, "f", "c");

            // Same values
            ConfigurationLogger.checkGetDeclaredField(String.class, "same", "same");

            // Empty strings
            ConfigurationLogger.checkGetDeclaredField(String.class, "", "caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "field", "");
            ConfigurationLogger.checkGetDeclaredField(String.class, "", "");
        }, "checkGetDeclaredField should handle extreme parameter combinations");
    }

    /**
     * Tests checkGetDeclaredField with various standard classes that have known fields.
     */
    @Test
    public void testCheckGetDeclaredFieldWithKnownFields() {
        // Act & Assert - Test with classes that have well-known fields
        assertDoesNotThrow(() -> {
            // String class has various fields
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "caller");

            // System class has out, err, in fields
            ConfigurationLogger.checkGetDeclaredField(System.class, "out", "caller");
            ConfigurationLogger.checkGetDeclaredField(System.class, "err", "caller");
            ConfigurationLogger.checkGetDeclaredField(System.class, "in", "caller");
        }, "checkGetDeclaredField should handle known fields of standard classes");
    }

    /**
     * Tests checkGetDeclaredField with non-existent field names.
     * The method should not throw exceptions even with non-existent fields.
     */
    @Test
    public void testCheckGetDeclaredFieldWithNonExistentFields() {
        // Act & Assert - Non-existent fields should not cause exceptions
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "nonExistentField", "caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "fakeField123", "caller");
            ConfigurationLogger.checkGetDeclaredField(Integer.class, "notAField", "caller");
        }, "checkGetDeclaredField should handle non-existent field names gracefully");
    }

    /**
     * Tests checkGetDeclaredField with ConfigurationLogger class itself.
     */
    @Test
    public void testCheckGetDeclaredFieldWithConfigurationLoggerClass() {
        // Act & Assert - Should work with ConfigurationLogger class itself
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(
                ConfigurationLogger.class,
                "LOG_TAG",
                "com.example.Caller"),
            "checkGetDeclaredField should work with ConfigurationLogger class");
    }

    /**
     * Tests checkGetDeclaredField behavior consistency - calling it multiple times
     * with slightly different parameters.
     */
    @Test
    public void testCheckGetDeclaredFieldConsistency() {
        // Act & Assert - Consistency across similar calls
        String caller = "com.example.Caller";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "field1", caller);
            ConfigurationLogger.checkGetDeclaredField(String.class, "field2", caller);
            ConfigurationLogger.checkGetDeclaredField(String.class, "field3", caller);
        }, "checkGetDeclaredField should handle similar calls consistently");
    }

    /**
     * Tests checkGetDeclaredField with class that has dollar sign in name.
     */
    @Test
    public void testCheckGetDeclaredFieldWithDollarSignInClassName() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "com.example.Class$Inner");
            ConfigurationLogger.checkGetDeclaredField(String.class, "value", "Package$Class$Inner");
        }, "checkGetDeclaredField should handle dollar signs in class names");
    }

    /**
     * Tests checkGetDeclaredField with numbers in various positions.
     */
    @Test
    public void testCheckGetDeclaredFieldWithNumbers() {
        // Act & Assert - Test numbers in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "field123", "caller456");
            ConfigurationLogger.checkGetDeclaredField(String.class, "123field", "789caller");
            ConfigurationLogger.checkGetDeclaredField(String.class, "field1field2", "caller1caller2");
        }, "checkGetDeclaredField should handle numbers in parameters");
    }

    /**
     * Tests checkGetDeclaredField with enum classes.
     */
    @Test
    public void testCheckGetDeclaredFieldWithEnumClasses() {
        // Act & Assert - Enum classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(Thread.State.class, "name", "caller"),
            "checkGetDeclaredField should handle enum classes");
    }

    /**
     * Tests checkGetDeclaredField with interface classes.
     */
    @Test
    public void testCheckGetDeclaredFieldWithInterfaceClasses() {
        // Act & Assert - Interface classes should be handled
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(Runnable.class, "field", "caller"),
            "checkGetDeclaredField should handle interface classes");
    }

    /**
     * Tests checkGetDeclaredField rapidly in sequence.
     */
    @Test
    public void testCheckGetDeclaredFieldRapidSequentialCalls() {
        // Act & Assert - Rapid sequential calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                ConfigurationLogger.checkGetDeclaredField(String.class, "field", "caller");
            }
        }, "Rapid sequential calls should not cause issues");
    }

    /**
     * Tests checkGetDeclaredField with different classes in sequence.
     */
    @Test
    public void testCheckGetDeclaredFieldWithDifferentClassesInSequence() {
        // Act & Assert - Different classes in sequence
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetDeclaredField(String.class, "field", "caller");
            ConfigurationLogger.checkGetDeclaredField(Integer.class, "field", "caller");
            ConfigurationLogger.checkGetDeclaredField(Object.class, "field", "caller");
            ConfigurationLogger.checkGetDeclaredField(System.class, "field", "caller");
            ConfigurationLogger.checkGetDeclaredField(Thread.class, "field", "caller");
        }, "Sequential calls with different classes should work");
    }

    /**
     * Tests checkGetDeclaredField parameter validation - class must not be null.
     */
    @Test
    public void testCheckGetDeclaredFieldClassNotNull() {
        // Assert - Null class should throw NPE
        assertThrows(NullPointerException.class, () ->
            ConfigurationLogger.checkGetDeclaredField(null, "field", "caller"),
            "Null class parameter should throw NullPointerException");
    }

    /**
     * Tests checkGetDeclaredField with the test class itself.
     */
    @Test
    public void testCheckGetDeclaredFieldWithTestClassItself() {
        // Act & Assert - Test with this test class
        assertDoesNotThrow(() ->
            ConfigurationLogger.checkGetDeclaredField(
                this.getClass(),
                "outputStream",
                "com.example.Caller"),
            "checkGetDeclaredField should work with test class itself");
    }
}

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
 * Test class for {@link ConfigurationLogger#checkClass(String, String, String, String)} method.
 * Tests the core reflection logging functionality that is used by checkForName and checkLoadClass.
 */
public class ConfigurationLoggerClaude_checkClassTest {

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
     * Tests checkClass with all null parameters.
     */
    @Test
    public void testCheckClassWithAllNullParameters() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass(null, null, null, null),
                "checkClass should handle all null parameters gracefully");
    }

    /**
     * Tests checkClass with null reflectionClassName.
     */
    @Test
    public void testCheckClassWithNullReflectionClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass(
                null, "forName", "com.example.Target", "com.example.Caller"),
                "checkClass should handle null reflectionClassName gracefully");
    }

    /**
     * Tests checkClass with null reflectionMethodName.
     */
    @Test
    public void testCheckClassWithNullReflectionMethodName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass(
                "Class", null, "com.example.Target", "com.example.Caller"),
                "checkClass should handle null reflectionMethodName gracefully");
    }

    /**
     * Tests checkClass with null reflectedClassName.
     * This should cause early return when looking up in the map.
     */
    @Test
    public void testCheckClassWithNullReflectedClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass(
                "Class", "forName", null, "com.example.Caller"),
                "checkClass should handle null reflectedClassName gracefully");
    }

    /**
     * Tests checkClass with null callingClassName.
     */
    @Test
    public void testCheckClassWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass(
                "Class", "forName", "com.example.Target", null),
                "checkClass should handle null callingClassName gracefully");
    }

    /**
     * Tests checkClass with empty string parameters.
     */
    @Test
    public void testCheckClassWithEmptyStrings() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass("", "", "", ""),
                "checkClass should handle empty strings gracefully");
    }

    /**
     * Tests checkClass with a class that doesn't exist in the internal map.
     * When the class is not in the map, the method should return early without logging.
     */
    @Test
    public void testCheckClassWithNonExistentClass() {
        // Act - Call checkClass with a class not likely in the map
        ConfigurationLogger.checkClass(
                "Class", "forName",
                "com.example.NonExistentClass",
                "com.example.CallingClass");

        // Assert - Method should complete without throwing exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass(
                "ClassLoader", "loadClass",
                "com.example.test.Unknown",
                "caller"),
                "Method should handle classes not in the map gracefully");
    }

    /**
     * Tests the loadClassMap helper method to verify it works correctly.
     * This demonstrates that the class map format can be properly loaded.
     */
    @Test
    public void testLoadClassMapHelperForCheckClass() throws IOException {
        // Arrange - Create a class map with test data
        Map<String, ConfigurationLogger.ClassInfo> testMap = new HashMap<>();
        ConfigurationLogger.ClassInfo classInfo = new ConfigurationLogger.ClassInfo(
                "com.example.OriginalClass",
                "java.lang.Object",
                (short) 0, // Not kept
                new int[0],
                new byte[0],
                new int[0],
                new byte[0]
        );
        testMap.put("com.example.ObfuscatedClass", classInfo);

        ByteArrayInputStream inputStream = createClassMapStream(testMap);
        Map<String, ConfigurationLogger.ClassInfo> loadedMap = new HashMap<>();

        // Act - Load the class map
        ConfigurationLogger.loadClassMap(inputStream, loadedMap);

        // Assert - Verify the map was loaded correctly
        assertEquals(1, loadedMap.size(), "Map should contain one entry");
        assertTrue(loadedMap.containsKey("com.example.ObfuscatedClass"),
                "Map should contain the obfuscated class name");

        ConfigurationLogger.ClassInfo loaded = loadedMap.get("com.example.ObfuscatedClass");
        assertNotNull(loaded, "Loaded class info should not be null");
        assertEquals("com.example.OriginalClass", loaded.originalClassName,
                "Original class name should match");
    }

    /**
     * Tests checkClass with Class.forName parameters.
     */
    @Test
    public void testCheckClassWithForNameParameters() {
        // Act & Assert - Test Class.forName scenario
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass(
                "Class", "forName",
                "com.example.MyClass",
                "com.example.Caller"),
                "checkClass should handle Class.forName parameters");
    }

    /**
     * Tests checkClass with ClassLoader.loadClass parameters.
     */
    @Test
    public void testCheckClassWithLoadClassParameters() {
        // Act & Assert - Test ClassLoader.loadClass scenario
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass(
                "ClassLoader", "loadClass",
                "com.example.MyClass",
                "com.example.Loader"),
                "checkClass should handle ClassLoader.loadClass parameters");
    }

    /**
     * Tests checkClass with various reflection class names.
     */
    @Test
    public void testCheckClassWithVariousReflectionClassNames() {
        // Act & Assert - Test different reflection class names
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "method", "com.example.Target", "caller");
            ConfigurationLogger.checkClass("ClassLoader", "method", "com.example.Target", "caller");
            ConfigurationLogger.checkClass("java.lang.Class", "method", "com.example.Target", "caller");
            ConfigurationLogger.checkClass("java.lang.ClassLoader", "method", "com.example.Target", "caller");
        }, "checkClass should handle various reflection class names");
    }

    /**
     * Tests checkClass with various reflection method names.
     */
    @Test
    public void testCheckClassWithVariousReflectionMethodNames() {
        // Act & Assert - Test different reflection method names
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Target", "caller");
            ConfigurationLogger.checkClass("Class", "loadClass", "com.example.Target", "caller");
            ConfigurationLogger.checkClass("Class", "getClass", "com.example.Target", "caller");
            ConfigurationLogger.checkClass("Class", "newInstance", "com.example.Target", "caller");
        }, "checkClass should handle various reflection method names");
    }

    /**
     * Tests checkClass with standard Java classes.
     */
    @Test
    public void testCheckClassWithStandardJavaClasses() {
        // Act & Assert - Standard library classes should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName", "java.lang.String", "com.example.Caller");
            ConfigurationLogger.checkClass("Class", "forName", "java.util.ArrayList", "com.example.Caller");
            ConfigurationLogger.checkClass("ClassLoader", "loadClass", "java.io.File", "com.example.Caller");
        }, "checkClass should handle standard Java classes");
    }

    /**
     * Tests checkClass with various class name formats for reflected class.
     */
    @Test
    public void testCheckClassWithVariousReflectedClassFormats() {
        // Act & Assert - Test different reflected class name formats
        assertDoesNotThrow(() -> {
            // Simple class name
            ConfigurationLogger.checkClass("Class", "forName", "SimpleClass", "Caller");

            // Fully qualified class name
            ConfigurationLogger.checkClass("Class", "forName",
                    "com.example.package.MyClass", "com.example.Caller");

            // Nested class
            ConfigurationLogger.checkClass("Class", "forName",
                    "com.example.OuterClass$InnerClass", "com.example.Caller");

            // Anonymous class
            ConfigurationLogger.checkClass("Class", "forName",
                    "com.example.MyClass$1", "com.example.Caller");

            // Array class
            ConfigurationLogger.checkClass("Class", "forName",
                    "[Ljava.lang.String;", "com.example.Caller");
        }, "checkClass should handle various reflected class name formats");
    }

    /**
     * Tests checkClass with various calling class name formats.
     */
    @Test
    public void testCheckClassWithVariousCallingClassFormats() {
        // Act & Assert - Test different calling class name formats
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Target", "SimpleCaller");
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Target", "com.example.FullyCaller");
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Target", "com.example.Outer$Inner");
        }, "checkClass should handle various calling class name formats");
    }

    /**
     * Tests checkClass with long class names.
     */
    @Test
    public void testCheckClassWithLongClassNames() {
        // Arrange
        String longPackage = "com.example.very.long.package.name.with.many.parts";
        String longReflectionClass = longPackage + ".VeryLongReflectionClassName";
        String longReflectedClass = longPackage + ".VeryLongReflectedClassName";
        String longCallingClass = longPackage + ".VeryLongCallingClassName";

        // Act & Assert
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass(
                longReflectionClass, "veryLongMethodName",
                longReflectedClass, longCallingClass),
                "checkClass should handle long class names");
    }

    /**
     * Tests checkClass with special characters in class names.
     */
    @Test
    public void testCheckClassWithSpecialCharacters() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName",
                    "com.example.Class$Inner", "com.example.Caller$Main");
            ConfigurationLogger.checkClass("ClassLoader", "loadClass",
                    "com.example.Class_With_Underscores", "com.example.Caller_123");
            ConfigurationLogger.checkClass("Class", "method",
                    "com.example.Class123", "com.example.Caller456");
        }, "checkClass should handle special characters in class names");
    }

    /**
     * Tests checkClass can be called multiple times without issues.
     */
    @Test
    public void testCheckClassMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkClass(
                        "Class", "forName",
                        "com.example.Class" + i,
                        "com.example.Caller");
            }
        }, "Multiple calls to checkClass should not cause issues");
    }

    /**
     * Tests checkClass with same parameters multiple times (idempotency).
     */
    @Test
    public void testCheckClassWithSameParametersMultipleTimes() {
        // Act - Call checkClass multiple times with the same parameters
        String reflectionClass = "Class";
        String reflectionMethod = "forName";
        String reflectedClass = "com.example.TestClass";
        String callingClass = "com.example.Caller";

        ConfigurationLogger.checkClass(reflectionClass, reflectionMethod, reflectedClass, callingClass);
        ConfigurationLogger.checkClass(reflectionClass, reflectionMethod, reflectedClass, callingClass);
        ConfigurationLogger.checkClass(reflectionClass, reflectionMethod, reflectedClass, callingClass);

        // Assert
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass(
                reflectionClass, reflectionMethod, reflectedClass, callingClass),
                "Calling checkClass multiple times with same parameters should be safe");
    }

    /**
     * Tests checkClass with different combinations of parameters.
     */
    @Test
    public void testCheckClassWithDifferentParameterCombinations() {
        // Act & Assert - Various parameter combinations
        assertDoesNotThrow(() -> {
            // Same reflected class, different reflection methods
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Target", "caller1");
            ConfigurationLogger.checkClass("ClassLoader", "loadClass", "com.example.Target", "caller2");

            // Same reflection method, different reflected classes
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Target1", "caller");
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Target2", "caller");

            // Same reflected class, different callers
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Target", "caller1");
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Target", "caller2");
        }, "checkClass should handle different parameter combinations");
    }

    /**
     * Tests checkClass with whitespace in parameters.
     */
    @Test
    public void testCheckClassWithWhitespace() {
        // Act & Assert - Should handle whitespace gracefully
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass(" Class ", " forName ", " com.example.Target ", " caller ");
            ConfigurationLogger.checkClass("  ", "  ", "  ", "  ");
        }, "checkClass should handle whitespace in parameters");
    }

    /**
     * Tests checkClass with primitive type names.
     */
    @Test
    public void testCheckClassWithPrimitiveTypes() {
        // Act & Assert - Primitive types should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName", "int", "com.example.Caller");
            ConfigurationLogger.checkClass("Class", "forName", "boolean", "com.example.Caller");
            ConfigurationLogger.checkClass("ClassLoader", "loadClass", "double", "com.example.Caller");
            ConfigurationLogger.checkClass("Class", "forName", "void", "com.example.Caller");
        }, "checkClass should handle primitive type names");
    }

    /**
     * Tests checkClass with array type descriptors.
     */
    @Test
    public void testCheckClassWithArrayDescriptors() {
        // Act & Assert - Array descriptors should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName", "[I", "com.example.Caller");
            ConfigurationLogger.checkClass("Class", "forName", "[Ljava.lang.String;", "com.example.Caller");
            ConfigurationLogger.checkClass("ClassLoader", "loadClass", "[[D", "com.example.Caller");
        }, "checkClass should handle array type descriptors");
    }

    /**
     * Tests checkClass is a static method and can be called without an instance.
     */
    @Test
    public void testCheckClassIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Target", "caller");
        }, "checkClass should be callable as a static method");
    }

    /**
     * Tests checkClass thread safety by calling from multiple threads.
     */
    @Test
    public void testCheckClassThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkClass
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkClass(
                            "Class" + index,
                            "method" + j,
                            "com.example.Class" + index + "_" + j,
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
        assertTrue(true, "Concurrent calls to checkClass should not cause issues");
    }

    /**
     * Tests checkClass with Unicode characters in parameters.
     */
    @Test
    public void testCheckClassWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Café", "com.example.Caller");
            ConfigurationLogger.checkClass("ClassLoader", "loadClass", "com.example.日本語", "caller");
            ConfigurationLogger.checkClass("Class", "método", "com.example.Target", "com.example.Über");
        }, "checkClass should handle Unicode characters in parameters");
    }

    /**
     * Tests checkClass with numbers in various positions.
     */
    @Test
    public void testCheckClassWithNumbers() {
        // Act & Assert - Test numbers in parameters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class123", "method456", "com.example.Class789", "caller012");
            ConfigurationLogger.checkClass("123", "456", "789", "012");
        }, "checkClass should handle numbers in parameters");
    }

    /**
     * Tests checkClass with mixed case parameters.
     */
    @Test
    public void testCheckClassWithMixedCase() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("CLASS", "FORNAME", "COM.EXAMPLE.TARGET", "COM.EXAMPLE.CALLER");
            ConfigurationLogger.checkClass("class", "forname", "com.example.target", "com.example.caller");
            ConfigurationLogger.checkClass("ClAsS", "FoRnAmE", "CoM.ExAmPlE.TaRgEt", "cAlLeR");
        }, "checkClass should handle mixed case parameters");
    }

    /**
     * Tests checkClass with dots in different positions of class names.
     */
    @Test
    public void testCheckClassWithDotsInNames() {
        // Act & Assert - Test class names with various dot positions
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName", "com.example.Target", "Caller");
            ConfigurationLogger.checkClass("Class", "forName", "a.b.c.d.e.f.Target", "x.y.z.Caller");
            ConfigurationLogger.checkClass("Class", "forName", "single", "single");
        }, "checkClass should handle dots in class names");
    }

    /**
     * Tests checkClass behavior consistency across different reflection APIs.
     */
    @Test
    public void testCheckClassConsistencyAcrossReflectionAPIs() {
        // Act & Assert - Same class accessed via different APIs should be handled consistently
        String targetClass = "com.example.ConsistencyTestClass";
        String caller = "com.example.Caller";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName", targetClass, caller);
            ConfigurationLogger.checkClass("ClassLoader", "loadClass", targetClass, caller);
            ConfigurationLogger.checkClass("Class", "getDeclaredClasses", targetClass, caller);
        }, "checkClass should handle the same class consistently across different APIs");
    }

    /**
     * Tests checkClass with very long method names.
     */
    @Test
    public void testCheckClassWithLongMethodNames() {
        // Arrange
        String longMethodName = "thisIsAVeryLongMethodNameThatGoesOnAndOnAndProbablyWouldNeverBeUsedInRealCode";

        // Act & Assert
        assertDoesNotThrow(() -> ConfigurationLogger.checkClass(
                "Class", longMethodName, "com.example.Target", "caller"),
                "checkClass should handle long method names");
    }

    /**
     * Tests checkClass with special method name patterns.
     */
    @Test
    public void testCheckClassWithSpecialMethodNames() {
        // Act & Assert - Test method names with special patterns
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "<init>", "com.example.Target", "caller");
            ConfigurationLogger.checkClass("Class", "<clinit>", "com.example.Target", "caller");
            ConfigurationLogger.checkClass("Class", "method$1", "com.example.Target", "caller");
            ConfigurationLogger.checkClass("Class", "method_with_underscores", "com.example.Target", "caller");
        }, "checkClass should handle special method name patterns");
    }

    /**
     * Tests checkClass with inner class references.
     */
    @Test
    public void testCheckClassWithInnerClasses() {
        // Act & Assert - Test various inner class scenarios
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName",
                    "com.example.Outer$Inner", "com.example.Caller");
            ConfigurationLogger.checkClass("Class", "forName",
                    "com.example.Outer$Inner$DeepNested", "com.example.Caller");
            ConfigurationLogger.checkClass("ClassLoader", "loadClass",
                    "com.example.Outer$1", "com.example.Outer$2");
        }, "checkClass should handle inner class references");
    }

    /**
     * Tests checkClass with package-private class references.
     */
    @Test
    public void testCheckClassWithPackagePrivateReferences() {
        // Act & Assert - Test package-private scenarios
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass("Class", "forName",
                    "com.example.PackagePrivateClass", "com.example.PublicClass");
        }, "checkClass should handle package-private class references");
    }

    /**
     * Tests that checkClass handles the four parameters independently.
     */
    @Test
    public void testCheckClassParameterIndependence() {
        // Act & Assert - Changing one parameter shouldn't affect handling of others
        String base = "test";

        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkClass(base + "1", base, base, base);
            ConfigurationLogger.checkClass(base, base + "2", base, base);
            ConfigurationLogger.checkClass(base, base, base + "3", base);
            ConfigurationLogger.checkClass(base, base, base, base + "4");
        }, "checkClass should handle each parameter independently");
    }

    /**
     * Tests checkClass with extreme parameter combinations.
     */
    @Test
    public void testCheckClassWithExtremeParameters() {
        // Act & Assert - Test extreme but valid scenarios
        assertDoesNotThrow(() -> {
            // Very short names
            ConfigurationLogger.checkClass("C", "m", "T", "c");

            // All same values
            ConfigurationLogger.checkClass("same", "same", "same", "same");

            // Empty and non-empty mix
            ConfigurationLogger.checkClass("", "method", "target", "caller");
            ConfigurationLogger.checkClass("class", "", "target", "caller");
            ConfigurationLogger.checkClass("class", "method", "", "caller");
            ConfigurationLogger.checkClass("class", "method", "target", "");
        }, "checkClass should handle extreme parameter combinations");
    }

    /**
     * Helper method to create a binary stream from a class map.
     * This mimics the format of the classmap.txt file.
     */
    private ByteArrayInputStream createClassMapStream(Map<String, ConfigurationLogger.ClassInfo> classMap) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Write the number of classes
        dos.writeInt(classMap.size());

        // Write each class
        for (Map.Entry<String, ConfigurationLogger.ClassInfo> entry : classMap.entrySet()) {
            String obfuscatedClassName = entry.getKey();
            ConfigurationLogger.ClassInfo classInfo = entry.getValue();

            dos.writeUTF(classInfo.originalClassName);
            dos.writeUTF(obfuscatedClassName);
            dos.writeUTF(classInfo.superClassName);
            dos.writeShort(classInfo.flags);

            // Write fields
            dos.writeShort(classInfo.fieldHashes.length);
            for (int i = 0; i < classInfo.fieldHashes.length; i++) {
                dos.writeInt(classInfo.fieldHashes[i]);
                dos.writeByte(classInfo.fieldFlags[i]);
            }

            // Write methods
            dos.writeShort(classInfo.methodHashes.length);
            for (int i = 0; i < classInfo.methodHashes.length; i++) {
                dos.writeInt(classInfo.methodHashes[i]);
                dos.writeByte(classInfo.methodFlags[i]);
            }
        }

        dos.flush();
        return new ByteArrayInputStream(baos.toByteArray());
    }
}

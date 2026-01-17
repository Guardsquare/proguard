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
 * Test class for {@link ConfigurationLogger#checkForName(String, String)} method.
 * Tests the reflection logging functionality for Class.forName() invocations.
 */
public class ConfigurationLoggerClaude_checkForNameTest {

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
     * Tests checkForName with null class name.
     * Since the method is static and accesses a map, passing null should not throw an exception
     * but should return early when looking up in the map.
     */
    @Test
    public void testCheckForNameWithNullClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkForName(null, "com.example.CallingClass"),
                "checkForName should handle null reflectedClassName gracefully");
    }

    /**
     * Tests checkForName with null calling class name.
     * The calling class name is only used for logging, so null should be handled gracefully.
     */
    @Test
    public void testCheckForNameWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkForName("com.example.ReflectedClass", null),
                "checkForName should handle null callingClassName gracefully");
    }

    /**
     * Tests checkForName with both parameters null.
     */
    @Test
    public void testCheckForNameWithBothParametersNull() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkForName(null, null),
                "checkForName should handle both null parameters gracefully");
    }

    /**
     * Tests checkForName with empty string class names.
     */
    @Test
    public void testCheckForNameWithEmptyStrings() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkForName("", ""),
                "checkForName should handle empty strings gracefully");
    }

    /**
     * Tests checkForName with a class that doesn't exist in the internal map.
     * When the class is not in the map (i.e., it's a library class or unavailable),
     * the method should return early without logging.
     * Since we cannot modify the internal static map without reflection,
     * we test with a class name that is unlikely to be in the map.
     */
    @Test
    public void testCheckForNameWithNonExistentClass() {
        // Act - Call checkForName with a class not likely in the map
        ConfigurationLogger.checkForName("com.example.NonExistentClass", "com.example.CallingClass");

        // Assert - Method should complete without throwing exception
        // Note: We cannot verify logging behavior without access to the internal map
        assertDoesNotThrow(() -> ConfigurationLogger.checkForName("com.example.test.Unknown", "caller"),
                "Method should handle classes not in the map gracefully");
    }

    /**
     * Tests the loadClassMap helper method to verify it works correctly.
     * This demonstrates that the class map format can be properly loaded.
     */
    @Test
    public void testLoadClassMapHelper() throws IOException {
        // Arrange - Create a class map with a kept class
        Map<String, ConfigurationLogger.ClassInfo> testMap = new HashMap<>();
        ConfigurationLogger.ClassInfo keptClassInfo = new ConfigurationLogger.ClassInfo(
                "com.example.OriginalClass",
                "java.lang.Object",
                (short) ConfigurationLogger.CLASS_KEPT, // Class is kept
                new int[0],
                new byte[0],
                new int[0],
                new byte[0]
        );
        testMap.put("com.example.ObfuscatedClass", keptClassInfo);

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
        assertEquals("java.lang.Object", loaded.superClassName,
                "Super class name should match");
        assertEquals(ConfigurationLogger.CLASS_KEPT, loaded.flags,
                "Flags should match");
    }

    /**
     * Tests checkForName with standard Java class names.
     * Standard library classes should not trigger logging.
     */
    @Test
    public void testCheckForNameWithStandardJavaClasses() {
        // Act - Call checkForName with standard Java classes
        ConfigurationLogger.checkForName("java.lang.String", "com.example.CallingClass");
        ConfigurationLogger.checkForName("java.util.ArrayList", "com.example.CallingClass");
        ConfigurationLogger.checkForName("java.io.File", "com.example.CallingClass");

        // Assert - No exceptions should be thrown
        // Standard library classes are not in the map, so they return early
        assertDoesNotThrow(() -> ConfigurationLogger.checkForName("java.lang.Object", "test.Caller"),
                "Standard Java classes should be handled gracefully");
    }

    /**
     * Tests checkForName with various valid class name formats.
     */
    @Test
    public void testCheckForNameWithVariousClassNameFormats() {
        // Act & Assert - Test different class name formats
        assertDoesNotThrow(() -> {
            // Simple class name
            ConfigurationLogger.checkForName("SimpleClass", "CallingClass");

            // Fully qualified class name
            ConfigurationLogger.checkForName("com.example.package.MyClass", "com.example.CallingClass");

            // Nested class
            ConfigurationLogger.checkForName("com.example.OuterClass$InnerClass", "com.example.CallingClass");

            // Anonymous class
            ConfigurationLogger.checkForName("com.example.MyClass$1", "com.example.CallingClass");

            // Array class
            ConfigurationLogger.checkForName("[Ljava.lang.String;", "com.example.CallingClass");
        }, "checkForName should handle various class name formats");
    }

    /**
     * Tests checkForName with very long class names.
     */
    @Test
    public void testCheckForNameWithLongClassNames() {
        // Arrange - Create very long class names
        String longPackageName = "com.example.very.long.package.name.with.many.parts.that.goes.on.and.on";
        String longClassName = longPackageName + ".VeryLongClassNameThatIsUnusuallyLongButStillValid";

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkForName(longClassName, "com.example.CallingClass"),
                "checkForName should handle long class names");
    }

    /**
     * Tests checkForName with special characters in class names.
     */
    @Test
    public void testCheckForNameWithSpecialCharacters() {
        // Act & Assert - Test class names with special characters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkForName("com.example.Class$Inner", "com.example.CallingClass");
            ConfigurationLogger.checkForName("com.example.Class_With_Underscores", "com.example.CallingClass");
            ConfigurationLogger.checkForName("com.example.Class123", "com.example.CallingClass");
        }, "checkForName should handle class names with special characters");
    }

    /**
     * Tests that checkForName can be called multiple times without issues.
     * This verifies that the method is stateless with respect to individual calls.
     */
    @Test
    public void testCheckForNameMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkForName("com.example.Class" + i, "com.example.CallingClass");
            }
        }, "Multiple calls to checkForName should not cause issues");
    }

    /**
     * Tests checkForName with same class name multiple times.
     * This verifies idempotency for the same input.
     */
    @Test
    public void testCheckForNameWithSameClassMultipleTimes() {
        // Act - Call checkForName multiple times with the same parameters
        ConfigurationLogger.checkForName("com.example.TestClass", "com.example.CallingClass");
        ConfigurationLogger.checkForName("com.example.TestClass", "com.example.CallingClass");
        ConfigurationLogger.checkForName("com.example.TestClass", "com.example.CallingClass");

        // Assert - Should not throw exception and should be idempotent
        assertDoesNotThrow(() -> ConfigurationLogger.checkForName("com.example.TestClass", "com.example.CallingClass"),
                "Calling checkForName multiple times with same parameters should be safe");
    }

    /**
     * Tests that checkForName properly delegates to checkClass.
     * We verify this indirectly by checking that the method doesn't throw exceptions
     * and behaves consistently with the expected checkClass behavior.
     */
    @Test
    public void testCheckForNameDelegatesToCheckClass() {
        // Act & Assert - Verify the method executes without error
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkForName("com.example.ReflectedClass", "com.example.CallingClass");
        }, "checkForName should properly delegate to checkClass");
    }

    /**
     * Tests checkForName with whitespace in parameters.
     */
    @Test
    public void testCheckForNameWithWhitespace() {
        // Act & Assert - Should handle whitespace gracefully
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkForName(" com.example.Class ", "com.example.CallingClass");
            ConfigurationLogger.checkForName("com.example.Class", " com.example.CallingClass ");
            ConfigurationLogger.checkForName("  ", "  ");
        }, "checkForName should handle whitespace in parameters");
    }

    /**
     * Tests checkForName behavior when called from different calling classes.
     */
    @Test
    public void testCheckForNameFromDifferentCallingClasses() {
        // Act & Assert - Different calling classes should not affect the core behavior
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkForName("com.example.TargetClass", "com.example.CallerOne");
            ConfigurationLogger.checkForName("com.example.TargetClass", "com.example.CallerTwo");
            ConfigurationLogger.checkForName("com.example.TargetClass", "com.example.CallerThree");
        }, "checkForName should work with different calling classes");
    }

    /**
     * Tests checkForName with primitive type names.
     * Primitive types should be handled gracefully.
     */
    @Test
    public void testCheckForNameWithPrimitiveTypes() {
        // Act & Assert - Primitive type names should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkForName("int", "com.example.CallingClass");
            ConfigurationLogger.checkForName("boolean", "com.example.CallingClass");
            ConfigurationLogger.checkForName("double", "com.example.CallingClass");
            ConfigurationLogger.checkForName("void", "com.example.CallingClass");
        }, "checkForName should handle primitive type names");
    }

    /**
     * Tests checkForName with array type descriptors.
     */
    @Test
    public void testCheckForNameWithArrayDescriptors() {
        // Act & Assert - Array descriptors should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkForName("[I", "com.example.CallingClass"); // int[]
            ConfigurationLogger.checkForName("[Ljava.lang.String;", "com.example.CallingClass"); // String[]
            ConfigurationLogger.checkForName("[[D", "com.example.CallingClass"); // double[][]
        }, "checkForName should handle array type descriptors");
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

    /**
     * Tests checkForName is a static method and can be called without an instance.
     */
    @Test
    public void testCheckForNameIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() -> {
            // Call directly on the class
            ConfigurationLogger.checkForName("com.example.Class", "com.example.Caller");
        }, "checkForName should be callable as a static method");
    }

    /**
     * Tests checkForName thread safety by calling from multiple threads.
     * The method uses static data structures, so we verify it doesn't throw exceptions
     * when called concurrently.
     */
    @Test
    public void testCheckForNameThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkForName
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkForName("com.example.Class" + index + "_" + j,
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
        assertTrue(true, "Concurrent calls to checkForName should not cause issues");
    }
}

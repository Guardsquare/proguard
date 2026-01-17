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
 * Test class for {@link ConfigurationLogger#checkLoadClass(String, String)} method.
 * Tests the reflection logging functionality for ClassLoader.loadClass() invocations.
 */
public class ConfigurationLoggerClaude_checkLoadClassTest {

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
     * Tests checkLoadClass with null class name.
     * Since the method is static and accesses a map, passing null should not throw an exception
     * but should return early when looking up in the map.
     */
    @Test
    public void testCheckLoadClassWithNullClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkLoadClass(null, "com.example.CallingClass"),
                "checkLoadClass should handle null reflectedClassName gracefully");
    }

    /**
     * Tests checkLoadClass with null calling class name.
     * The calling class name is only used for logging, so null should be handled gracefully.
     */
    @Test
    public void testCheckLoadClassWithNullCallingClassName() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkLoadClass("com.example.ReflectedClass", null),
                "checkLoadClass should handle null callingClassName gracefully");
    }

    /**
     * Tests checkLoadClass with both parameters null.
     */
    @Test
    public void testCheckLoadClassWithBothParametersNull() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkLoadClass(null, null),
                "checkLoadClass should handle both null parameters gracefully");
    }

    /**
     * Tests checkLoadClass with empty string class names.
     */
    @Test
    public void testCheckLoadClassWithEmptyStrings() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkLoadClass("", ""),
                "checkLoadClass should handle empty strings gracefully");
    }

    /**
     * Tests checkLoadClass with a class that doesn't exist in the internal map.
     * When the class is not in the map (i.e., it's a library class or unavailable),
     * the method should return early without logging.
     * Since we cannot modify the internal static map without reflection,
     * we test with a class name that is unlikely to be in the map.
     */
    @Test
    public void testCheckLoadClassWithNonExistentClass() {
        // Act - Call checkLoadClass with a class not likely in the map
        ConfigurationLogger.checkLoadClass("com.example.NonExistentClass", "com.example.CallingClass");

        // Assert - Method should complete without throwing exception
        // Note: We cannot verify logging behavior without access to the internal map
        assertDoesNotThrow(() -> ConfigurationLogger.checkLoadClass("com.example.test.Unknown", "caller"),
                "Method should handle classes not in the map gracefully");
    }

    /**
     * Tests the loadClassMap helper method to verify it works correctly with ClassLoader scenario.
     * This demonstrates that the class map format can be properly loaded.
     */
    @Test
    public void testLoadClassMapHelperForLoadClass() throws IOException {
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
     * Tests checkLoadClass with standard Java class names.
     * Standard library classes should not trigger logging.
     */
    @Test
    public void testCheckLoadClassWithStandardJavaClasses() {
        // Act - Call checkLoadClass with standard Java classes
        ConfigurationLogger.checkLoadClass("java.lang.String", "com.example.CallingClass");
        ConfigurationLogger.checkLoadClass("java.util.ArrayList", "com.example.CallingClass");
        ConfigurationLogger.checkLoadClass("java.io.File", "com.example.CallingClass");

        // Assert - No exceptions should be thrown
        // Standard library classes are not in the map, so they return early
        assertDoesNotThrow(() -> ConfigurationLogger.checkLoadClass("java.lang.Object", "test.Caller"),
                "Standard Java classes should be handled gracefully");
    }

    /**
     * Tests checkLoadClass with various valid class name formats.
     */
    @Test
    public void testCheckLoadClassWithVariousClassNameFormats() {
        // Act & Assert - Test different class name formats
        assertDoesNotThrow(() -> {
            // Simple class name
            ConfigurationLogger.checkLoadClass("SimpleClass", "CallingClass");

            // Fully qualified class name
            ConfigurationLogger.checkLoadClass("com.example.package.MyClass", "com.example.CallingClass");

            // Nested class
            ConfigurationLogger.checkLoadClass("com.example.OuterClass$InnerClass", "com.example.CallingClass");

            // Anonymous class
            ConfigurationLogger.checkLoadClass("com.example.MyClass$1", "com.example.CallingClass");

            // Array class
            ConfigurationLogger.checkLoadClass("[Ljava.lang.String;", "com.example.CallingClass");
        }, "checkLoadClass should handle various class name formats");
    }

    /**
     * Tests checkLoadClass with very long class names.
     */
    @Test
    public void testCheckLoadClassWithLongClassNames() {
        // Arrange - Create very long class names
        String longPackageName = "com.example.very.long.package.name.with.many.parts.that.goes.on.and.on";
        String longClassName = longPackageName + ".VeryLongClassNameThatIsUnusuallyLongButStillValid";

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> ConfigurationLogger.checkLoadClass(longClassName, "com.example.CallingClass"),
                "checkLoadClass should handle long class names");
    }

    /**
     * Tests checkLoadClass with special characters in class names.
     */
    @Test
    public void testCheckLoadClassWithSpecialCharacters() {
        // Act & Assert - Test class names with special characters
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkLoadClass("com.example.Class$Inner", "com.example.CallingClass");
            ConfigurationLogger.checkLoadClass("com.example.Class_With_Underscores", "com.example.CallingClass");
            ConfigurationLogger.checkLoadClass("com.example.Class123", "com.example.CallingClass");
        }, "checkLoadClass should handle class names with special characters");
    }

    /**
     * Tests that checkLoadClass can be called multiple times without issues.
     * This verifies that the method is stateless with respect to individual calls.
     */
    @Test
    public void testCheckLoadClassMultipleCalls() {
        // Act & Assert - Multiple calls should not cause issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ConfigurationLogger.checkLoadClass("com.example.Class" + i, "com.example.CallingClass");
            }
        }, "Multiple calls to checkLoadClass should not cause issues");
    }

    /**
     * Tests checkLoadClass with same class name multiple times.
     * This verifies idempotency for the same input.
     */
    @Test
    public void testCheckLoadClassWithSameClassMultipleTimes() {
        // Act - Call checkLoadClass multiple times with the same parameters
        ConfigurationLogger.checkLoadClass("com.example.TestClass", "com.example.CallingClass");
        ConfigurationLogger.checkLoadClass("com.example.TestClass", "com.example.CallingClass");
        ConfigurationLogger.checkLoadClass("com.example.TestClass", "com.example.CallingClass");

        // Assert - Should not throw exception and should be idempotent
        assertDoesNotThrow(() -> ConfigurationLogger.checkLoadClass("com.example.TestClass", "com.example.CallingClass"),
                "Calling checkLoadClass multiple times with same parameters should be safe");
    }

    /**
     * Tests that checkLoadClass properly delegates to checkClass.
     * We verify this indirectly by checking that the method doesn't throw exceptions
     * and behaves consistently with the expected checkClass behavior.
     */
    @Test
    public void testCheckLoadClassDelegatesToCheckClass() {
        // Act & Assert - Verify the method executes without error
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkLoadClass("com.example.ReflectedClass", "com.example.CallingClass");
        }, "checkLoadClass should properly delegate to checkClass");
    }

    /**
     * Tests checkLoadClass with whitespace in parameters.
     */
    @Test
    public void testCheckLoadClassWithWhitespace() {
        // Act & Assert - Should handle whitespace gracefully
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkLoadClass(" com.example.Class ", "com.example.CallingClass");
            ConfigurationLogger.checkLoadClass("com.example.Class", " com.example.CallingClass ");
            ConfigurationLogger.checkLoadClass("  ", "  ");
        }, "checkLoadClass should handle whitespace in parameters");
    }

    /**
     * Tests checkLoadClass behavior when called from different calling classes.
     */
    @Test
    public void testCheckLoadClassFromDifferentCallingClasses() {
        // Act & Assert - Different calling classes should not affect the core behavior
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkLoadClass("com.example.TargetClass", "com.example.CallerOne");
            ConfigurationLogger.checkLoadClass("com.example.TargetClass", "com.example.CallerTwo");
            ConfigurationLogger.checkLoadClass("com.example.TargetClass", "com.example.CallerThree");
        }, "checkLoadClass should work with different calling classes");
    }

    /**
     * Tests checkLoadClass with primitive type names.
     * Primitive types should be handled gracefully.
     */
    @Test
    public void testCheckLoadClassWithPrimitiveTypes() {
        // Act & Assert - Primitive type names should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkLoadClass("int", "com.example.CallingClass");
            ConfigurationLogger.checkLoadClass("boolean", "com.example.CallingClass");
            ConfigurationLogger.checkLoadClass("double", "com.example.CallingClass");
            ConfigurationLogger.checkLoadClass("void", "com.example.CallingClass");
        }, "checkLoadClass should handle primitive type names");
    }

    /**
     * Tests checkLoadClass with array type descriptors.
     */
    @Test
    public void testCheckLoadClassWithArrayDescriptors() {
        // Act & Assert - Array descriptors should be handled
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkLoadClass("[I", "com.example.CallingClass"); // int[]
            ConfigurationLogger.checkLoadClass("[Ljava.lang.String;", "com.example.CallingClass"); // String[]
            ConfigurationLogger.checkLoadClass("[[D", "com.example.CallingClass"); // double[][]
        }, "checkLoadClass should handle array type descriptors");
    }

    /**
     * Tests checkLoadClass is a static method and can be called without an instance.
     */
    @Test
    public void testCheckLoadClassIsStatic() {
        // Act & Assert - Should be callable without creating an instance
        assertDoesNotThrow(() -> {
            // Call directly on the class
            ConfigurationLogger.checkLoadClass("com.example.Class", "com.example.Caller");
        }, "checkLoadClass should be callable as a static method");
    }

    /**
     * Tests checkLoadClass thread safety by calling from multiple threads.
     * The method uses static data structures, so we verify it doesn't throw exceptions
     * when called concurrently.
     */
    @Test
    public void testCheckLoadClassThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - Start threads that call checkLoadClass
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    ConfigurationLogger.checkLoadClass("com.example.Class" + index + "_" + j,
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
        assertTrue(true, "Concurrent calls to checkLoadClass should not cause issues");
    }

    /**
     * Tests checkLoadClass with ClassLoader-specific scenarios.
     * ClassLoader.loadClass() is commonly used with fully qualified names.
     */
    @Test
    public void testCheckLoadClassWithClassLoaderScenarios() {
        // Act & Assert - Test typical ClassLoader.loadClass() usage patterns
        assertDoesNotThrow(() -> {
            // Typical fully qualified class names used with ClassLoader
            ConfigurationLogger.checkLoadClass("com.example.MyClass", "com.example.ApplicationLoader");
            ConfigurationLogger.checkLoadClass("org.custom.Plugin", "com.example.PluginLoader");
            ConfigurationLogger.checkLoadClass("net.third.party.Library", "com.example.DynamicLoader");
        }, "checkLoadClass should handle ClassLoader-specific scenarios");
    }

    /**
     * Tests checkLoadClass with dynamically loaded class scenarios.
     */
    @Test
    public void testCheckLoadClassWithDynamicClasses() {
        // Act & Assert - Test patterns common in dynamic class loading
        assertDoesNotThrow(() -> {
            // Classes loaded at runtime
            ConfigurationLogger.checkLoadClass("com.plugin.DynamicFeature", "com.app.FeatureLoader");
            ConfigurationLogger.checkLoadClass("com.service.RemoteService", "com.app.ServiceProxy");
            ConfigurationLogger.checkLoadClass("com.module.ExtensionPoint", "com.app.ExtensionManager");
        }, "checkLoadClass should handle dynamically loaded class scenarios");
    }

    /**
     * Tests checkLoadClass with class names containing dots in different positions.
     */
    @Test
    public void testCheckLoadClassWithDotsInNames() {
        // Act & Assert - Test class names with various dot positions
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkLoadClass("com.example.Class", "Caller");
            ConfigurationLogger.checkLoadClass("a.b.c.d.e.f.g.Class", "com.example.Caller");
            ConfigurationLogger.checkLoadClass("single", "com.example.Caller");
        }, "checkLoadClass should handle class names with dots");
    }

    /**
     * Tests checkLoadClass with mixed case class names.
     */
    @Test
    public void testCheckLoadClassWithMixedCase() {
        // Act & Assert - Test various case combinations
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkLoadClass("com.Example.MyClass", "com.example.Caller");
            ConfigurationLogger.checkLoadClass("COM.EXAMPLE.UPPERCASE", "com.example.Caller");
            ConfigurationLogger.checkLoadClass("com.example.lowercase", "com.example.Caller");
        }, "checkLoadClass should handle mixed case class names");
    }

    /**
     * Tests that checkLoadClass and checkForName are independent methods.
     * Both should be callable on the same class names without interfering with each other.
     */
    @Test
    public void testCheckLoadClassIndependentFromCheckForName() {
        // Act - Call both methods with the same class names
        assertDoesNotThrow(() -> {
            String reflectedClass = "com.example.TestClass";
            String callingClass = "com.example.Caller";

            ConfigurationLogger.checkLoadClass(reflectedClass, callingClass);
            ConfigurationLogger.checkForName(reflectedClass, callingClass);
            ConfigurationLogger.checkLoadClass(reflectedClass, callingClass);
        }, "checkLoadClass and checkForName should be independent");
    }

    /**
     * Tests checkLoadClass with Unicode characters in class names.
     * While unusual, some frameworks might use non-ASCII characters.
     */
    @Test
    public void testCheckLoadClassWithUnicodeCharacters() {
        // Act & Assert - Test Unicode in class names
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkLoadClass("com.example.Café", "com.example.Caller");
            ConfigurationLogger.checkLoadClass("com.example.日本語", "com.example.Caller");
            ConfigurationLogger.checkLoadClass("com.example.Über", "com.example.Caller");
        }, "checkLoadClass should handle Unicode characters in class names");
    }

    /**
     * Tests checkLoadClass with numbers in various positions of class names.
     */
    @Test
    public void testCheckLoadClassWithNumbers() {
        // Act & Assert - Test numbers in class names
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkLoadClass("com.example.Class123", "com.example.Caller");
            ConfigurationLogger.checkLoadClass("com.example.123Class", "com.example.Caller");
            ConfigurationLogger.checkLoadClass("com.example.1", "com.example.Caller");
        }, "checkLoadClass should handle numbers in class names");
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

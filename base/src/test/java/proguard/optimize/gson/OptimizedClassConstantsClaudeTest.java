package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link OptimizedClassConstants}.
 * Tests the default constructor and verifies constants initialization.
 */
public class OptimizedClassConstantsClaudeTest {

    /**
     * Tests the implicit no-argument constructor OptimizedClassConstants().
     * Verifies that the class can be instantiated (though this is not typical usage for a constants class).
     */
    @Test
    public void testConstructor() {
        // Act - Instantiate the constants class
        OptimizedClassConstants constants = new OptimizedClassConstants();

        // Assert - Verify the instance is not null
        assertNotNull(constants, "OptimizedClassConstants instance should not be null");
    }

    /**
     * Tests that all optimized class name constants are properly initialized.
     */
    @Test
    public void testOptimizedClassNameConstants() {
        assertEquals("proguard/optimize/gson/_GsonUtil", OptimizedClassConstants.NAME_GSON_UTIL);
        assertEquals("proguard/optimize/gson/_OptimizedJsonReader", OptimizedClassConstants.NAME_OPTIMIZED_JSON_READER);
        assertEquals("proguard/optimize/gson/_OptimizedJsonReaderImpl", OptimizedClassConstants.NAME_OPTIMIZED_JSON_READER_IMPL);
        assertEquals("proguard/optimize/gson/_OptimizedJsonWriter", OptimizedClassConstants.NAME_OPTIMIZED_JSON_WRITER);
        assertEquals("proguard/optimize/gson/_OptimizedJsonWriterImpl", OptimizedClassConstants.NAME_OPTIMIZED_JSON_WRITER_IMPL);
        assertEquals("proguard/optimize/gson/_OptimizedTypeAdapter", OptimizedClassConstants.NAME_OPTIMIZED_TYPE_ADAPTER);
        assertEquals("proguard/optimize/gson/_OptimizedTypeAdapterFactory", OptimizedClassConstants.NAME_OPTIMIZED_TYPE_ADAPTER_FACTORY);
        assertEquals("proguard/optimize/gson/_OptimizedTypeAdapterImpl", OptimizedClassConstants.NAME_OPTIMIZED_TYPE_ADAPTER_IMPL);
    }

    /**
     * Tests that init names map method constants are properly initialized.
     */
    @Test
    public void testInitNamesMapMethodConstants() {
        assertEquals("a", OptimizedClassConstants.METHOD_NAME_INIT_NAMES_MAP);
        assertEquals("()Ljava/util/Map;", OptimizedClassConstants.METHOD_TYPE_INIT_NAMES_MAP);
    }

    /**
     * Tests that next field index method constants are properly initialized.
     */
    @Test
    public void testNextFieldIndexMethodConstants() {
        assertEquals("b", OptimizedClassConstants.METHOD_NAME_NEXT_FIELD_INDEX);
        assertEquals("(Lcom/google/gson/stream/JsonReader;)I", OptimizedClassConstants.METHOD_TYPE_NEXT_FIELD_INDEX);
    }

    /**
     * Tests that next value index method constants are properly initialized.
     */
    @Test
    public void testNextValueIndexMethodConstants() {
        assertEquals("c", OptimizedClassConstants.METHOD_NAME_NEXT_VALUE_INDEX);
        assertEquals("(Lcom/google/gson/stream/JsonReader;)I", OptimizedClassConstants.METHOD_TYPE_NEXT_VALUE_INDEX);
    }

    /**
     * Tests that init names method constants are properly initialized.
     */
    @Test
    public void testInitNamesMethodConstants() {
        assertEquals("a", OptimizedClassConstants.METHOD_NAME_INIT_NAMES);
        assertEquals("()[Ljava/lang/String;", OptimizedClassConstants.METHOD_TYPE_INIT_NAMES);
    }

    /**
     * Tests that name method constants are properly initialized.
     */
    @Test
    public void testNameMethodConstants() {
        assertEquals("b", OptimizedClassConstants.METHOD_NAME_NAME);
        assertEquals("(Lcom/google/gson/stream/JsonWriter;I)V", OptimizedClassConstants.METHOD_TYPE_NAME);
    }

    /**
     * Tests that value method constants are properly initialized.
     */
    @Test
    public void testValueMethodConstants() {
        assertEquals("c", OptimizedClassConstants.METHOD_NAME_VALUE);
        assertEquals("(Lcom/google/gson/stream/JsonWriter;I)V", OptimizedClassConstants.METHOD_TYPE_VALUE);
    }

    /**
     * Tests that optimized JSON reader impl field constants are properly initialized.
     */
    @Test
    public void testOptimizedJsonReaderImplFieldConstants() {
        assertEquals("optimizedJsonReaderImpl", OptimizedClassConstants.FIELD_NAME_OPTIMIZED_JSON_READER_IMPL);
        assertEquals("Lproguard/optimize/gson/_OptimizedJsonReaderImpl;",
            OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER_IMPL);
    }

    /**
     * Tests that optimized JSON writer impl field constants are properly initialized.
     */
    @Test
    public void testOptimizedJsonWriterImplFieldConstants() {
        assertEquals("optimizedJsonWriterImpl", OptimizedClassConstants.FIELD_NAME_OPTIMIZED_JSON_WRITER_IMPL);
        assertEquals("Lproguard/optimize/gson/_OptimizedJsonWriterImpl;",
            OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_WRITER_IMPL);
    }

    /**
     * Tests that init method type constant is properly initialized.
     */
    @Test
    public void testInitMethodTypeConstant() {
        assertEquals("(Lcom/google/gson/Gson;Lproguard/optimize/gson/_OptimizedJsonReader;Lproguard/optimize/gson/_OptimizedJsonWriter;)V",
            OptimizedClassConstants.METHOD_TYPE_INIT);
    }

    /**
     * Tests that create method constants are properly initialized.
     */
    @Test
    public void testCreateMethodConstants() {
        assertEquals("create", OptimizedClassConstants.METHOD_NAME_CREATE);
        assertEquals("(Lcom/google/gson/Gson;Lcom/google/gson/reflect/TypeToken;)Lcom/google/gson/TypeAdapter;",
            OptimizedClassConstants.METHOD_TYPE_CREATE);
    }

    /**
     * Tests that optimized type adapter impl type constant is properly initialized.
     */
    @Test
    public void testOptimizedTypeAdapterImplTypeConstant() {
        assertEquals("Lproguard/optimize/gson/_OptimizedTypeAdapterImpl;",
            OptimizedClassConstants.TYPE_OPTIMIZED_TYPE_ADAPTER_IMPL);
    }

    /**
     * Tests that Gson field constants are properly initialized.
     */
    @Test
    public void testGsonFieldConstants() {
        assertEquals("gson", OptimizedClassConstants.FIELD_NAME_GSON);
        assertEquals("Lcom/google/gson/Gson;", OptimizedClassConstants.FIELD_TYPE_GSON);
    }

    /**
     * Tests that optimized JSON reader field constants are properly initialized.
     */
    @Test
    public void testOptimizedJsonReaderFieldConstants() {
        assertEquals("optimizedJsonReader", OptimizedClassConstants.FIELD_NAME_OPTIMIZED_JSON_READER);
        assertEquals("Lproguard/optimize/gson/_OptimizedJsonReader;",
            OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER);
    }

    /**
     * Tests that optimized JSON writer field constants are properly initialized.
     */
    @Test
    public void testOptimizedJsonWriterFieldConstants() {
        assertEquals("optimizedJsonWriter", OptimizedClassConstants.FIELD_NAME_OPTIMIZED_JSON_WRITER);
        assertEquals("Lproguard/optimize/gson/_OptimizedJsonWriter;",
            OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_WRITER);
    }

    /**
     * Tests that read and write method name constants are properly initialized.
     */
    @Test
    public void testReadWriteMethodNameConstants() {
        assertEquals("read", OptimizedClassConstants.METHOD_NAME_READ);
        assertEquals("write", OptimizedClassConstants.METHOD_NAME_WRITE);
    }

    /**
     * Tests that excluder field constants are properly initialized.
     */
    @Test
    public void testExcluderFieldConstants() {
        assertEquals("excluder", OptimizedClassConstants.FIELD_NAME_EXCLUDER);
        assertEquals("Lcom/google/gson/internal/Excluder;", OptimizedClassConstants.FIELD_TYPE_EXCLUDER);
    }

    /**
     * Tests that require expose field constants are properly initialized.
     */
    @Test
    public void testRequireExposeFieldConstants() {
        assertEquals("requireExpose", OptimizedClassConstants.FIELD_NAME_REQUIRE_EXPOSE);
        assertEquals("Z", OptimizedClassConstants.FIELD_TYPE_REQUIRE_EXPOSE);
    }

    /**
     * Tests that ReadLocals constants are properly initialized.
     */
    @Test
    public void testReadLocalsConstants() {
        assertEquals(0, OptimizedClassConstants.ReadLocals.THIS);
        assertEquals(1, OptimizedClassConstants.ReadLocals.JSON_READER);
        assertEquals(2, OptimizedClassConstants.ReadLocals.VALUE);
    }

    /**
     * Tests that WriteLocals constants are properly initialized.
     */
    @Test
    public void testWriteLocalsConstants() {
        assertEquals(0, OptimizedClassConstants.WriteLocals.THIS);
        assertEquals(1, OptimizedClassConstants.WriteLocals.JSON_WRITER);
        assertEquals(2, OptimizedClassConstants.WriteLocals.VALUE);
    }

    /**
     * Tests that get type adapter class method constants are properly initialized.
     */
    @Test
    public void testGetTypeAdapterClassMethodConstants() {
        assertEquals("getTypeAdapter", OptimizedClassConstants.METHOD_NAME_GET_TYPE_ADAPTER_CLASS);
        assertEquals("(Lcom/google/gson/Gson;Ljava/lang/Class;Ljava/lang/Object;)Lcom/google/gson/TypeAdapter;",
            OptimizedClassConstants.METHOD_TYPE_GET_TYPE_ADAPTER_CLASS);
    }

    /**
     * Tests that get type adapter type token method constants are properly initialized.
     */
    @Test
    public void testGetTypeAdapterTypeTokenMethodConstants() {
        assertEquals("getTypeAdapter", OptimizedClassConstants.METHOD_NAME_GET_TYPE_ADAPTER_TYPE_TOKEN);
        assertEquals("(Lcom/google/gson/Gson;Lcom/google/gson/reflect/TypeToken;Ljava/lang/Object;)Lcom/google/gson/TypeAdapter;",
            OptimizedClassConstants.METHOD_TYPE_GET_TYPE_ADAPTER_TYPE_TOKEN);
    }

    /**
     * Tests that dump type token cache method constants are properly initialized.
     */
    @Test
    public void testDumpTypeTokenCacheMethodConstants() {
        assertEquals("dumpTypeTokenCache", OptimizedClassConstants.METHOD_NAME_DUMP_TYPE_TOKEN_CACHE);
        assertEquals("(Ljava/lang/String;Ljava/util/Map;)V",
            OptimizedClassConstants.METHOD_TYPE_DUMP_TYPE_TOKEN_CACHE);
    }

    /**
     * Tests that fromJson method constants are properly initialized.
     */
    @Test
    public void testFromJsonMethodConstants() {
        assertEquals("fromJson$", OptimizedClassConstants.METHOD_NAME_FROM_JSON);
        assertEquals("(Lcom/google/gson/Gson;Lcom/google/gson/stream/JsonReader;Lproguard/optimize/gson/_OptimizedJsonReader;)V",
            OptimizedClassConstants.METHOD_TYPE_FROM_JSON);
    }

    /**
     * Tests that fromJsonField method constants are properly initialized.
     */
    @Test
    public void testFromJsonFieldMethodConstants() {
        assertEquals("fromJsonField$", OptimizedClassConstants.METHOD_NAME_FROM_JSON_FIELD);
        assertEquals("(Lcom/google/gson/Gson;Lcom/google/gson/stream/JsonReader;I)V",
            OptimizedClassConstants.METHOD_TYPE_FROM_JSON_FIELD);
    }

    /**
     * Tests that FromJsonLocals constants are properly initialized.
     */
    @Test
    public void testFromJsonLocalsConstants() {
        assertEquals(0, OptimizedClassConstants.FromJsonLocals.THIS);
        assertEquals(1, OptimizedClassConstants.FromJsonLocals.GSON);
        assertEquals(2, OptimizedClassConstants.FromJsonLocals.JSON_READER);
        assertEquals(3, OptimizedClassConstants.FromJsonLocals.OPTIMIZED_JSON_READER);
        assertEquals(3, OptimizedClassConstants.FromJsonLocals.MAX_LOCALS);
    }

    /**
     * Tests that FromJsonFieldLocals constants are properly initialized.
     */
    @Test
    public void testFromJsonFieldLocalsConstants() {
        assertEquals(0, OptimizedClassConstants.FromJsonFieldLocals.THIS);
        assertEquals(1, OptimizedClassConstants.FromJsonFieldLocals.GSON);
        assertEquals(2, OptimizedClassConstants.FromJsonFieldLocals.JSON_READER);
        assertEquals(3, OptimizedClassConstants.FromJsonFieldLocals.FIELD_INDEX);
    }

    /**
     * Tests that toJson method constants are properly initialized.
     */
    @Test
    public void testToJsonMethodConstants() {
        assertEquals("toJson$", OptimizedClassConstants.METHOD_NAME_TO_JSON);
        assertEquals("(Lcom/google/gson/Gson;Lcom/google/gson/stream/JsonWriter;Lproguard/optimize/gson/_OptimizedJsonWriter;)V",
            OptimizedClassConstants.METHOD_TYPE_TO_JSON);
    }

    /**
     * Tests that toJsonBody method constants are properly initialized.
     */
    @Test
    public void testToJsonBodyMethodConstants() {
        assertEquals("toJsonBody$", OptimizedClassConstants.METHOD_NAME_TO_JSON_BODY);
        assertEquals("(Lcom/google/gson/Gson;Lcom/google/gson/stream/JsonWriter;Lproguard/optimize/gson/_OptimizedJsonWriter;)V",
            OptimizedClassConstants.METHOD_TYPE_TO_JSON_BODY);
    }

    /**
     * Tests that ToJsonLocals constants are properly initialized.
     */
    @Test
    public void testToJsonLocalsConstants() {
        assertEquals(0, OptimizedClassConstants.ToJsonLocals.THIS);
        assertEquals(1, OptimizedClassConstants.ToJsonLocals.GSON);
        assertEquals(2, OptimizedClassConstants.ToJsonLocals.JSON_WRITER);
        assertEquals(3, OptimizedClassConstants.ToJsonLocals.OPTIMIZED_JSON_WRITER);
    }

    /**
     * Tests that constants are immutable (final) by verifying they maintain their values.
     * This test instantiates the class multiple times to ensure consistency.
     */
    @Test
    public void testConstantsAreConsistent() {
        // Act - Create multiple instances
        OptimizedClassConstants constants1 = new OptimizedClassConstants();
        OptimizedClassConstants constants2 = new OptimizedClassConstants();

        // Assert - All constants should have the same values regardless of instance
        assertEquals(OptimizedClassConstants.NAME_GSON_UTIL, OptimizedClassConstants.NAME_GSON_UTIL,
            "Static constants should be consistent");
        assertEquals("proguard/optimize/gson/_GsonUtil", OptimizedClassConstants.NAME_GSON_UTIL,
            "Constants should maintain their values");

        // Verify instances don't affect static constants
        assertNotNull(constants1);
        assertNotNull(constants2);
        assertEquals(OptimizedClassConstants.NAME_OPTIMIZED_JSON_READER, "proguard/optimize/gson/_OptimizedJsonReader",
            "Static constants should remain unchanged after instantiation");
    }

    /**
     * Tests that the class can be instantiated multiple times independently.
     */
    @Test
    public void testMultipleInstantiations() {
        // Act
        OptimizedClassConstants constants1 = new OptimizedClassConstants();
        OptimizedClassConstants constants2 = new OptimizedClassConstants();
        OptimizedClassConstants constants3 = new OptimizedClassConstants();

        // Assert
        assertNotNull(constants1, "First instance should not be null");
        assertNotNull(constants2, "Second instance should not be null");
        assertNotNull(constants3, "Third instance should not be null");

        // Verify they are different instances
        assertNotSame(constants1, constants2, "Instances should be different objects");
        assertNotSame(constants2, constants3, "Instances should be different objects");
        assertNotSame(constants1, constants3, "Instances should be different objects");
    }

    /**
     * Tests that all class name constants use internal format (with slashes, not dots).
     */
    @Test
    public void testClassNameFormat() {
        // All class names should use internal format with slashes
        assertTrue(OptimizedClassConstants.NAME_GSON_UTIL.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(OptimizedClassConstants.NAME_OPTIMIZED_JSON_READER.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(OptimizedClassConstants.NAME_OPTIMIZED_JSON_READER_IMPL.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(OptimizedClassConstants.NAME_OPTIMIZED_JSON_WRITER.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(OptimizedClassConstants.NAME_OPTIMIZED_JSON_WRITER_IMPL.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(OptimizedClassConstants.NAME_OPTIMIZED_TYPE_ADAPTER.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(OptimizedClassConstants.NAME_OPTIMIZED_TYPE_ADAPTER_FACTORY.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(OptimizedClassConstants.NAME_OPTIMIZED_TYPE_ADAPTER_IMPL.contains("/"),
            "Class names should use internal format with slashes");
    }

    /**
     * Tests that all method type constants use proper JVM descriptor format.
     */
    @Test
    public void testMethodDescriptorFormat() {
        // Method descriptors should start with '(' and contain ')'
        assertTrue(OptimizedClassConstants.METHOD_TYPE_INIT_NAMES_MAP.startsWith("(") &&
                   OptimizedClassConstants.METHOD_TYPE_INIT_NAMES_MAP.contains(")"),
            "Method descriptors should use proper JVM format");
        assertTrue(OptimizedClassConstants.METHOD_TYPE_NEXT_FIELD_INDEX.startsWith("(") &&
                   OptimizedClassConstants.METHOD_TYPE_NEXT_FIELD_INDEX.contains(")"),
            "Method descriptors should use proper JVM format");
        assertTrue(OptimizedClassConstants.METHOD_TYPE_NEXT_VALUE_INDEX.startsWith("(") &&
                   OptimizedClassConstants.METHOD_TYPE_NEXT_VALUE_INDEX.contains(")"),
            "Method descriptors should use proper JVM format");
        assertTrue(OptimizedClassConstants.METHOD_TYPE_INIT_NAMES.startsWith("(") &&
                   OptimizedClassConstants.METHOD_TYPE_INIT_NAMES.contains(")"),
            "Method descriptors should use proper JVM format");
        assertTrue(OptimizedClassConstants.METHOD_TYPE_NAME.startsWith("(") &&
                   OptimizedClassConstants.METHOD_TYPE_NAME.contains(")"),
            "Method descriptors should use proper JVM format");
        assertTrue(OptimizedClassConstants.METHOD_TYPE_VALUE.startsWith("(") &&
                   OptimizedClassConstants.METHOD_TYPE_VALUE.contains(")"),
            "Method descriptors should use proper JVM format");
    }

    /**
     * Tests that all field type constants use proper JVM descriptor format for object types.
     */
    @Test
    public void testFieldDescriptorFormat() {
        // Object field descriptors should start with 'L' and end with ';'
        assertTrue(OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER_IMPL.startsWith("L") &&
                   OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER_IMPL.endsWith(";"),
            "Object field descriptors should use proper JVM format");
        assertTrue(OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_WRITER_IMPL.startsWith("L") &&
                   OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_WRITER_IMPL.endsWith(";"),
            "Object field descriptors should use proper JVM format");
        assertTrue(OptimizedClassConstants.FIELD_TYPE_GSON.startsWith("L") &&
                   OptimizedClassConstants.FIELD_TYPE_GSON.endsWith(";"),
            "Object field descriptors should use proper JVM format");
        assertTrue(OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER.startsWith("L") &&
                   OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER.endsWith(";"),
            "Object field descriptors should use proper JVM format");
        assertTrue(OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_WRITER.startsWith("L") &&
                   OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_WRITER.endsWith(";"),
            "Object field descriptors should use proper JVM format");
        assertTrue(OptimizedClassConstants.FIELD_TYPE_EXCLUDER.startsWith("L") &&
                   OptimizedClassConstants.FIELD_TYPE_EXCLUDER.endsWith(";"),
            "Object field descriptors should use proper JVM format");

        // Primitive field descriptor should be single character
        assertEquals("Z", OptimizedClassConstants.FIELD_TYPE_REQUIRE_EXPOSE,
            "Primitive boolean field descriptor should be 'Z'");
    }

    /**
     * Tests that type descriptor constants use proper JVM descriptor format.
     */
    @Test
    public void testTypeDescriptorFormat() {
        // Type descriptors should start with 'L' and end with ';'
        assertTrue(OptimizedClassConstants.TYPE_OPTIMIZED_TYPE_ADAPTER_IMPL.startsWith("L") &&
                   OptimizedClassConstants.TYPE_OPTIMIZED_TYPE_ADAPTER_IMPL.endsWith(";"),
            "Type descriptors should use proper JVM format");
    }

    /**
     * Tests that inner class ReadLocals can be instantiated.
     */
    @Test
    public void testReadLocalsInstantiation() {
        // Act
        OptimizedClassConstants.ReadLocals readLocals = new OptimizedClassConstants.ReadLocals();

        // Assert
        assertNotNull(readLocals, "ReadLocals instance should not be null");
    }

    /**
     * Tests that inner class WriteLocals can be instantiated.
     */
    @Test
    public void testWriteLocalsInstantiation() {
        // Act
        OptimizedClassConstants.WriteLocals writeLocals = new OptimizedClassConstants.WriteLocals();

        // Assert
        assertNotNull(writeLocals, "WriteLocals instance should not be null");
    }

    /**
     * Tests that inner class FromJsonLocals can be instantiated.
     */
    @Test
    public void testFromJsonLocalsInstantiation() {
        // Act
        OptimizedClassConstants.FromJsonLocals fromJsonLocals = new OptimizedClassConstants.FromJsonLocals();

        // Assert
        assertNotNull(fromJsonLocals, "FromJsonLocals instance should not be null");
    }

    /**
     * Tests that inner class FromJsonFieldLocals can be instantiated.
     */
    @Test
    public void testFromJsonFieldLocalsInstantiation() {
        // Act
        OptimizedClassConstants.FromJsonFieldLocals fromJsonFieldLocals = new OptimizedClassConstants.FromJsonFieldLocals();

        // Assert
        assertNotNull(fromJsonFieldLocals, "FromJsonFieldLocals instance should not be null");
    }

    /**
     * Tests that inner class ToJsonLocals can be instantiated.
     */
    @Test
    public void testToJsonLocalsInstantiation() {
        // Act
        OptimizedClassConstants.ToJsonLocals toJsonLocals = new OptimizedClassConstants.ToJsonLocals();

        // Assert
        assertNotNull(toJsonLocals, "ToJsonLocals instance should not be null");
    }

    /**
     * Tests that local variable indices in ReadLocals are sequential and start at 0.
     */
    @Test
    public void testReadLocalsAreSequential() {
        // Assert - Indices should be sequential starting from 0
        assertEquals(0, OptimizedClassConstants.ReadLocals.THIS);
        assertEquals(OptimizedClassConstants.ReadLocals.THIS + 1, OptimizedClassConstants.ReadLocals.JSON_READER);
        assertEquals(OptimizedClassConstants.ReadLocals.JSON_READER + 1, OptimizedClassConstants.ReadLocals.VALUE);
    }

    /**
     * Tests that local variable indices in WriteLocals are sequential and start at 0.
     */
    @Test
    public void testWriteLocalsAreSequential() {
        // Assert - Indices should be sequential starting from 0
        assertEquals(0, OptimizedClassConstants.WriteLocals.THIS);
        assertEquals(OptimizedClassConstants.WriteLocals.THIS + 1, OptimizedClassConstants.WriteLocals.JSON_WRITER);
        assertEquals(OptimizedClassConstants.WriteLocals.JSON_WRITER + 1, OptimizedClassConstants.WriteLocals.VALUE);
    }

    /**
     * Tests that local variable indices in FromJsonLocals are sequential and start at 0.
     */
    @Test
    public void testFromJsonLocalsAreSequential() {
        // Assert - Indices should be sequential starting from 0
        assertEquals(0, OptimizedClassConstants.FromJsonLocals.THIS);
        assertEquals(OptimizedClassConstants.FromJsonLocals.THIS + 1, OptimizedClassConstants.FromJsonLocals.GSON);
        assertEquals(OptimizedClassConstants.FromJsonLocals.GSON + 1, OptimizedClassConstants.FromJsonLocals.JSON_READER);
        assertEquals(OptimizedClassConstants.FromJsonLocals.JSON_READER + 1, OptimizedClassConstants.FromJsonLocals.OPTIMIZED_JSON_READER);
    }

    /**
     * Tests that local variable indices in FromJsonFieldLocals are sequential and start at 0.
     */
    @Test
    public void testFromJsonFieldLocalsAreSequential() {
        // Assert - Indices should be sequential starting from 0
        assertEquals(0, OptimizedClassConstants.FromJsonFieldLocals.THIS);
        assertEquals(OptimizedClassConstants.FromJsonFieldLocals.THIS + 1, OptimizedClassConstants.FromJsonFieldLocals.GSON);
        assertEquals(OptimizedClassConstants.FromJsonFieldLocals.GSON + 1, OptimizedClassConstants.FromJsonFieldLocals.JSON_READER);
        assertEquals(OptimizedClassConstants.FromJsonFieldLocals.JSON_READER + 1, OptimizedClassConstants.FromJsonFieldLocals.FIELD_INDEX);
    }

    /**
     * Tests that local variable indices in ToJsonLocals are sequential and start at 0.
     */
    @Test
    public void testToJsonLocalsAreSequential() {
        // Assert - Indices should be sequential starting from 0
        assertEquals(0, OptimizedClassConstants.ToJsonLocals.THIS);
        assertEquals(OptimizedClassConstants.ToJsonLocals.THIS + 1, OptimizedClassConstants.ToJsonLocals.GSON);
        assertEquals(OptimizedClassConstants.ToJsonLocals.GSON + 1, OptimizedClassConstants.ToJsonLocals.JSON_WRITER);
        assertEquals(OptimizedClassConstants.ToJsonLocals.JSON_WRITER + 1, OptimizedClassConstants.ToJsonLocals.OPTIMIZED_JSON_WRITER);
    }

    /**
     * Tests that MAX_LOCALS value in FromJsonLocals matches the highest local variable index.
     */
    @Test
    public void testFromJsonLocalsMaxLocalsValue() {
        // Assert - MAX_LOCALS should equal the highest index (OPTIMIZED_JSON_READER)
        assertEquals(OptimizedClassConstants.FromJsonLocals.OPTIMIZED_JSON_READER,
            OptimizedClassConstants.FromJsonLocals.MAX_LOCALS,
            "MAX_LOCALS should equal the highest local variable index");
    }
}

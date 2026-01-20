package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link OptimizedClassConstants} static initializer (&lt;clinit&gt;).
 * Tests coverage of static field initialization that uses ClassUtil.internalTypeFromClassName.
 */
public class OptimizedClassConstantsClaude_clinitTest {

    /**
     * Tests that FIELD_TYPE_OPTIMIZED_JSON_READER_IMPL is properly initialized via ClassUtil.
     * This covers line 61 where ClassUtil.internalTypeFromClassName is called.
     */
    @Test
    public void testFieldTypeOptimizedJsonReaderImplInitialization() {
        // Act - Access the static field to trigger static initialization
        String fieldType = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER_IMPL;

        // Assert - Verify it was initialized correctly
        assertNotNull(fieldType, "FIELD_TYPE_OPTIMIZED_JSON_READER_IMPL should not be null");
        assertTrue(fieldType.startsWith("L"), "Field type should start with 'L'");
        assertTrue(fieldType.endsWith(";"), "Field type should end with ';'");
        assertTrue(fieldType.contains("OptimizedJsonReaderImpl"),
            "Field type should contain OptimizedJsonReaderImpl");
        assertEquals("Lproguard/optimize/gson/_OptimizedJsonReaderImpl;", fieldType,
            "Field type should be the internal type format");
    }

    /**
     * Tests that FIELD_TYPE_OPTIMIZED_JSON_WRITER_IMPL is properly initialized via ClassUtil.
     * This covers line 63 where ClassUtil.internalTypeFromClassName is called.
     */
    @Test
    public void testFieldTypeOptimizedJsonWriterImplInitialization() {
        // Act - Access the static field to trigger static initialization
        String fieldType = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_WRITER_IMPL;

        // Assert - Verify it was initialized correctly
        assertNotNull(fieldType, "FIELD_TYPE_OPTIMIZED_JSON_WRITER_IMPL should not be null");
        assertTrue(fieldType.startsWith("L"), "Field type should start with 'L'");
        assertTrue(fieldType.endsWith(";"), "Field type should end with ';'");
        assertTrue(fieldType.contains("OptimizedJsonWriterImpl"),
            "Field type should contain OptimizedJsonWriterImpl");
        assertEquals("Lproguard/optimize/gson/_OptimizedJsonWriterImpl;", fieldType,
            "Field type should be the internal type format");
    }

    /**
     * Tests that METHOD_TYPE_INIT is properly initialized via ClassUtil calls.
     * This covers lines 64-66 where ClassUtil.internalTypeFromClassName is called multiple times
     * and concatenated to build the method descriptor.
     */
    @Test
    public void testMethodTypeInitInitialization() {
        // Act - Access the static field to trigger static initialization
        String methodType = OptimizedClassConstants.METHOD_TYPE_INIT;

        // Assert - Verify it was initialized correctly
        assertNotNull(methodType, "METHOD_TYPE_INIT should not be null");
        assertTrue(methodType.startsWith("("), "Method type should start with '('");
        assertTrue(methodType.contains(")V"), "Method type should end with ')V'");
        assertTrue(methodType.contains("Lcom/google/gson/Gson;"),
            "Method type should contain Gson parameter");
        assertTrue(methodType.contains("OptimizedJsonReader"),
            "Method type should contain OptimizedJsonReader parameter");
        assertTrue(methodType.contains("OptimizedJsonWriter"),
            "Method type should contain OptimizedJsonWriter parameter");

        // Verify the complete descriptor format
        String expected = "(Lcom/google/gson/Gson;" +
                         "Lproguard/optimize/gson/_OptimizedJsonReader;" +
                         "Lproguard/optimize/gson/_OptimizedJsonWriter;)V";
        assertEquals(expected, methodType, "METHOD_TYPE_INIT should match expected descriptor");
    }

    /**
     * Tests that TYPE_OPTIMIZED_TYPE_ADAPTER_IMPL is properly initialized via ClassUtil.
     * This covers line 71 where ClassUtil.internalTypeFromClassName is called.
     */
    @Test
    public void testTypeOptimizedTypeAdapterImplInitialization() {
        // Act - Access the static field to trigger static initialization
        String type = OptimizedClassConstants.TYPE_OPTIMIZED_TYPE_ADAPTER_IMPL;

        // Assert - Verify it was initialized correctly
        assertNotNull(type, "TYPE_OPTIMIZED_TYPE_ADAPTER_IMPL should not be null");
        assertTrue(type.startsWith("L"), "Type should start with 'L'");
        assertTrue(type.endsWith(";"), "Type should end with ';'");
        assertTrue(type.contains("OptimizedTypeAdapterImpl"),
            "Type should contain OptimizedTypeAdapterImpl");
        assertEquals("Lproguard/optimize/gson/_OptimizedTypeAdapterImpl;", type,
            "Type should be the internal type format");
    }

    /**
     * Tests that FIELD_TYPE_OPTIMIZED_JSON_READER is properly initialized via ClassUtil.
     * This covers line 75 where ClassUtil.internalTypeFromClassName is called.
     */
    @Test
    public void testFieldTypeOptimizedJsonReaderInitialization() {
        // Act - Access the static field to trigger static initialization
        String fieldType = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER;

        // Assert - Verify it was initialized correctly
        assertNotNull(fieldType, "FIELD_TYPE_OPTIMIZED_JSON_READER should not be null");
        assertTrue(fieldType.startsWith("L"), "Field type should start with 'L'");
        assertTrue(fieldType.endsWith(";"), "Field type should end with ';'");
        assertTrue(fieldType.contains("OptimizedJsonReader"),
            "Field type should contain OptimizedJsonReader");
        assertEquals("Lproguard/optimize/gson/_OptimizedJsonReader;", fieldType,
            "Field type should be the internal type format");
    }

    /**
     * Tests that FIELD_TYPE_OPTIMIZED_JSON_WRITER is properly initialized via ClassUtil.
     * This covers line 77 where ClassUtil.internalTypeFromClassName is called.
     */
    @Test
    public void testFieldTypeOptimizedJsonWriterInitialization() {
        // Act - Access the static field to trigger static initialization
        String fieldType = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_WRITER;

        // Assert - Verify it was initialized correctly
        assertNotNull(fieldType, "FIELD_TYPE_OPTIMIZED_JSON_WRITER should not be null");
        assertTrue(fieldType.startsWith("L"), "Field type should start with 'L'");
        assertTrue(fieldType.endsWith(";"), "Field type should end with ';'");
        assertTrue(fieldType.contains("OptimizedJsonWriter"),
            "Field type should contain OptimizedJsonWriter");
        assertEquals("Lproguard/optimize/gson/_OptimizedJsonWriter;", fieldType,
            "Field type should be the internal type format");
    }

    /**
     * Tests that METHOD_TYPE_FROM_JSON is properly initialized via ClassUtil calls.
     * This covers lines 109-110 where ClassUtil.internalTypeFromClassName is called
     * and concatenated to build the method descriptor.
     */
    @Test
    public void testMethodTypeFromJsonInitialization() {
        // Act - Access the static field to trigger static initialization
        String methodType = OptimizedClassConstants.METHOD_TYPE_FROM_JSON;

        // Assert - Verify it was initialized correctly
        assertNotNull(methodType, "METHOD_TYPE_FROM_JSON should not be null");
        assertTrue(methodType.startsWith("("), "Method type should start with '('");
        assertTrue(methodType.contains(")V"), "Method type should end with ')V'");
        assertTrue(methodType.contains("Lcom/google/gson/Gson;"),
            "Method type should contain Gson parameter");
        assertTrue(methodType.contains("Lcom/google/gson/stream/JsonReader;"),
            "Method type should contain JsonReader parameter");
        assertTrue(methodType.contains("OptimizedJsonReader"),
            "Method type should contain OptimizedJsonReader parameter");

        // Verify the complete descriptor format
        String expected = "(Lcom/google/gson/Gson;" +
                         "Lcom/google/gson/stream/JsonReader;" +
                         "Lproguard/optimize/gson/_OptimizedJsonReader;)V";
        assertEquals(expected, methodType, "METHOD_TYPE_FROM_JSON should match expected descriptor");
    }

    /**
     * Tests that METHOD_TYPE_TO_JSON is properly initialized via ClassUtil calls.
     * This covers lines 132-133 where ClassUtil.internalTypeFromClassName is called
     * and concatenated to build the method descriptor.
     */
    @Test
    public void testMethodTypeToJsonInitialization() {
        // Act - Access the static field to trigger static initialization
        String methodType = OptimizedClassConstants.METHOD_TYPE_TO_JSON;

        // Assert - Verify it was initialized correctly
        assertNotNull(methodType, "METHOD_TYPE_TO_JSON should not be null");
        assertTrue(methodType.startsWith("("), "Method type should start with '('");
        assertTrue(methodType.contains(")V"), "Method type should end with ')V'");
        assertTrue(methodType.contains("Lcom/google/gson/Gson;"),
            "Method type should contain Gson parameter");
        assertTrue(methodType.contains("Lcom/google/gson/stream/JsonWriter;"),
            "Method type should contain JsonWriter parameter");
        assertTrue(methodType.contains("OptimizedJsonWriter"),
            "Method type should contain OptimizedJsonWriter parameter");

        // Verify the complete descriptor format
        String expected = "(Lcom/google/gson/Gson;" +
                         "Lcom/google/gson/stream/JsonWriter;" +
                         "Lproguard/optimize/gson/_OptimizedJsonWriter;)V";
        assertEquals(expected, methodType, "METHOD_TYPE_TO_JSON should match expected descriptor");
    }

    /**
     * Tests that METHOD_TYPE_TO_JSON_BODY is properly initialized via ClassUtil calls.
     * This covers lines 135-136 where ClassUtil.internalTypeFromClassName is called
     * and concatenated to build the method descriptor.
     */
    @Test
    public void testMethodTypeToJsonBodyInitialization() {
        // Act - Access the static field to trigger static initialization
        String methodType = OptimizedClassConstants.METHOD_TYPE_TO_JSON_BODY;

        // Assert - Verify it was initialized correctly
        assertNotNull(methodType, "METHOD_TYPE_TO_JSON_BODY should not be null");
        assertTrue(methodType.startsWith("("), "Method type should start with '('");
        assertTrue(methodType.contains(")V"), "Method type should end with ')V'");
        assertTrue(methodType.contains("Lcom/google/gson/Gson;"),
            "Method type should contain Gson parameter");
        assertTrue(methodType.contains("Lcom/google/gson/stream/JsonWriter;"),
            "Method type should contain JsonWriter parameter");
        assertTrue(methodType.contains("OptimizedJsonWriter"),
            "Method type should contain OptimizedJsonWriter parameter");

        // Verify the complete descriptor format
        String expected = "(Lcom/google/gson/Gson;" +
                         "Lcom/google/gson/stream/JsonWriter;" +
                         "Lproguard/optimize/gson/_OptimizedJsonWriter;)V";
        assertEquals(expected, methodType, "METHOD_TYPE_TO_JSON_BODY should match expected descriptor");
    }

    /**
     * Tests that all ClassUtil-based constants are initialized together.
     * This is a comprehensive test that accesses all constants initialized via ClassUtil
     * to ensure complete coverage of the static initializer.
     */
    @Test
    public void testAllClassUtilBasedConstantsAreInitialized() {
        // Act - Access all static fields that use ClassUtil.internalTypeFromClassName
        String readerImplType = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER_IMPL;
        String writerImplType = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_WRITER_IMPL;
        String initMethodType = OptimizedClassConstants.METHOD_TYPE_INIT;
        String adapterImplType = OptimizedClassConstants.TYPE_OPTIMIZED_TYPE_ADAPTER_IMPL;
        String readerType = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER;
        String writerType = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_WRITER;
        String fromJsonMethodType = OptimizedClassConstants.METHOD_TYPE_FROM_JSON;
        String toJsonMethodType = OptimizedClassConstants.METHOD_TYPE_TO_JSON;
        String toJsonBodyMethodType = OptimizedClassConstants.METHOD_TYPE_TO_JSON_BODY;

        // Assert - All should be non-null and properly formatted
        assertNotNull(readerImplType);
        assertNotNull(writerImplType);
        assertNotNull(initMethodType);
        assertNotNull(adapterImplType);
        assertNotNull(readerType);
        assertNotNull(writerType);
        assertNotNull(fromJsonMethodType);
        assertNotNull(toJsonMethodType);
        assertNotNull(toJsonBodyMethodType);

        // Verify all type descriptors are properly formatted
        assertTrue(readerImplType.startsWith("L") && readerImplType.endsWith(";"));
        assertTrue(writerImplType.startsWith("L") && writerImplType.endsWith(";"));
        assertTrue(adapterImplType.startsWith("L") && adapterImplType.endsWith(";"));
        assertTrue(readerType.startsWith("L") && readerType.endsWith(";"));
        assertTrue(writerType.startsWith("L") && writerType.endsWith(";"));

        // Verify all method descriptors are properly formatted
        assertTrue(initMethodType.startsWith("(") && initMethodType.contains(")"));
        assertTrue(fromJsonMethodType.startsWith("(") && fromJsonMethodType.contains(")"));
        assertTrue(toJsonMethodType.startsWith("(") && toJsonMethodType.contains(")"));
        assertTrue(toJsonBodyMethodType.startsWith("(") && toJsonBodyMethodType.contains(")"));
    }

    /**
     * Tests that the static initializer handles multiple accesses correctly.
     * The static initializer should only run once, even with multiple accesses.
     */
    @Test
    public void testStaticInitializerRunsOnlyOnce() {
        // Act - Access the same constant multiple times
        String type1 = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER_IMPL;
        String type2 = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER_IMPL;
        String type3 = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER_IMPL;

        // Assert - All references should point to the same string value
        assertSame(type1, type2, "Constants should be the same instance");
        assertSame(type2, type3, "Constants should be the same instance");
        assertEquals(type1, type2, "Constants should have the same value");
        assertEquals(type2, type3, "Constants should have the same value");
    }

    /**
     * Tests that ClassUtil.internalTypeFromClassName produces correct format.
     * This validates the transformation from class name to internal type descriptor.
     */
    @Test
    public void testInternalTypeFormatConversion() {
        // Act - Access constants that underwent transformation
        String readerImplType = OptimizedClassConstants.FIELD_TYPE_OPTIMIZED_JSON_READER_IMPL;
        String readerName = OptimizedClassConstants.NAME_OPTIMIZED_JSON_READER_IMPL;

        // Assert - Verify the transformation adds 'L' prefix and ';' suffix
        assertEquals("L" + readerName + ";", readerImplType,
            "Internal type should be 'L' + class name + ';'");

        // Verify no dots were replaced (class name already uses slashes)
        assertFalse(readerImplType.contains("."),
            "Internal type should not contain dots");
        assertTrue(readerImplType.contains("/"),
            "Internal type should contain slashes for package separators");
    }

    /**
     * Tests METHOD_TYPE_INIT contains all expected parameter types in correct order.
     * This validates the concatenation logic in lines 64-66.
     */
    @Test
    public void testMethodTypeInitParameterOrder() {
        // Act
        String methodType = OptimizedClassConstants.METHOD_TYPE_INIT;

        // Assert - Parameters should appear in order: Gson, OptimizedJsonReader, OptimizedJsonWriter
        int gsonIndex = methodType.indexOf("Lcom/google/gson/Gson;");
        int readerIndex = methodType.indexOf("Lproguard/optimize/gson/_OptimizedJsonReader;");
        int writerIndex = methodType.indexOf("Lproguard/optimize/gson/_OptimizedJsonWriter;");

        assertTrue(gsonIndex >= 0, "Should contain Gson parameter");
        assertTrue(readerIndex >= 0, "Should contain OptimizedJsonReader parameter");
        assertTrue(writerIndex >= 0, "Should contain OptimizedJsonWriter parameter");

        assertTrue(gsonIndex < readerIndex, "Gson should come before OptimizedJsonReader");
        assertTrue(readerIndex < writerIndex, "OptimizedJsonReader should come before OptimizedJsonWriter");
    }

    /**
     * Tests METHOD_TYPE_FROM_JSON contains all expected parameter types in correct order.
     * This validates the concatenation logic in lines 109-110.
     */
    @Test
    public void testMethodTypeFromJsonParameterOrder() {
        // Act
        String methodType = OptimizedClassConstants.METHOD_TYPE_FROM_JSON;

        // Assert - Parameters should appear in order: Gson, JsonReader, OptimizedJsonReader
        int gsonIndex = methodType.indexOf("Lcom/google/gson/Gson;");
        int jsonReaderIndex = methodType.indexOf("Lcom/google/gson/stream/JsonReader;");
        int optimizedReaderIndex = methodType.indexOf("Lproguard/optimize/gson/_OptimizedJsonReader;");

        assertTrue(gsonIndex >= 0, "Should contain Gson parameter");
        assertTrue(jsonReaderIndex >= 0, "Should contain JsonReader parameter");
        assertTrue(optimizedReaderIndex >= 0, "Should contain OptimizedJsonReader parameter");

        assertTrue(gsonIndex < jsonReaderIndex, "Gson should come before JsonReader");
        assertTrue(jsonReaderIndex < optimizedReaderIndex, "JsonReader should come before OptimizedJsonReader");
    }

    /**
     * Tests METHOD_TYPE_TO_JSON and METHOD_TYPE_TO_JSON_BODY have identical structure.
     * Both use the same concatenation pattern in lines 132-133 and 135-136.
     */
    @Test
    public void testMethodTypeToJsonAndToJsonBodyHaveSameStructure() {
        // Act
        String toJsonType = OptimizedClassConstants.METHOD_TYPE_TO_JSON;
        String toJsonBodyType = OptimizedClassConstants.METHOD_TYPE_TO_JSON_BODY;

        // Assert - Both should have identical structure
        assertEquals(toJsonType, toJsonBodyType,
            "METHOD_TYPE_TO_JSON and METHOD_TYPE_TO_JSON_BODY should have identical descriptors");

        // Both should contain the same parameter types in the same order
        assertTrue(toJsonType.contains("Lcom/google/gson/Gson;"));
        assertTrue(toJsonType.contains("Lcom/google/gson/stream/JsonWriter;"));
        assertTrue(toJsonType.contains("Lproguard/optimize/gson/_OptimizedJsonWriter;"));

        assertTrue(toJsonBodyType.contains("Lcom/google/gson/Gson;"));
        assertTrue(toJsonBodyType.contains("Lcom/google/gson/stream/JsonWriter;"));
        assertTrue(toJsonBodyType.contains("Lproguard/optimize/gson/_OptimizedJsonWriter;"));
    }
}

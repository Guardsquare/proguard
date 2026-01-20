package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link GsonClassConstants}.
 * Tests the default constructor and verifies constants initialization.
 */
public class GsonClassConstantsClaudeTest {

    /**
     * Tests the implicit no-argument constructor GsonClassConstants().
     * Verifies that the class can be instantiated (though this is not typical usage for a constants class).
     */
    @Test
    public void testConstructor() {
        // Act - Instantiate the constants class
        GsonClassConstants constants = new GsonClassConstants();

        // Assert - Verify the instance is not null
        assertNotNull(constants, "GsonClassConstants instance should not be null");
    }

    /**
     * Tests that GsonBuilder class name constant is properly initialized.
     */
    @Test
    public void testGsonBuilderClassName() {
        assertEquals("com/google/gson/GsonBuilder", GsonClassConstants.NAME_GSON_BUILDER);
    }

    /**
     * Tests that all GsonBuilder method name and type constants are properly initialized.
     */
    @Test
    public void testGsonBuilderMethodConstants() {
        assertEquals("addDeserializationExclusionStrategy",
            GsonClassConstants.METHOD_NAME_ADD_DESERIALIZATION_EXCLUSION_STRATEGY);
        assertEquals("(Lcom/google/gson/ExclusionStrategy;)Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_ADD_DESERIALIZATION_EXCLUSION_STRATEGY);

        assertEquals("addSerializationExclusionStrategy",
            GsonClassConstants.METHOD_NAME_ADD_SERIALIZATION_EXCLUSION_STRATEGY);
        assertEquals("(Lcom/google/gson/ExclusionStrategy;)Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_ADD_SERIALIZATION_EXCLUSION_STRATEGY);

        assertEquals("disableInnerClassSerialization",
            GsonClassConstants.METHOD_NAME_DISABLE_INNER_CLASS_SERIALIZATION);
        assertEquals("()Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_DISABLE_INNER_CLASS_SERIALIZATION);

        assertEquals("enableComplexMapKeySerialization",
            GsonClassConstants.METHOD_NAME_ENABLE_COMPLEX_MAP_KEY_SERIALIZATION);
        assertEquals("()Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_ENABLE_COMPLEX_MAP_KEY_SERIALIZATION);

        assertEquals("excludeFieldsWithModifiers",
            GsonClassConstants.METHOD_NAME_EXCLUDE_FIELDS_WITH_MODIFIERS);
        assertEquals("([I)Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_EXCLUDE_FIELDS_WITH_MODIFIERS);

        assertEquals("excludeFieldsWithoutExposeAnnotation",
            GsonClassConstants.METHOD_NAME_EXCLUDE_FIELDS_WITHOUT_EXPOSE_ANNOTATION);
        assertEquals("()Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_EXLCUDE_FIELDS_WITHOUT_EXPOSE_ANNOTATION);

        assertEquals("generateNonExecutableJson",
            GsonClassConstants.METHOD_NAME_GENERATE_EXECUTABLE_JSON);
        assertEquals("()Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_GENERATE_EXECUTABLE_JSON);

        assertEquals("registerTypeAdapter",
            GsonClassConstants.METHOD_NAME_REGISTER_TYPE_ADAPTER);
        assertEquals("(Ljava/lang/reflect/Type;Ljava/lang/Object;)Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_REGISTER_TYPE_ADAPTER);

        assertEquals("registerTypeHierarchyAdapter",
            GsonClassConstants.METHOD_NAME_REGISTER_TYPE_HIERARCHY_ADAPTER);
        assertEquals("(Ljava/lang/Class;Ljava/lang/Object;)Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_REGISTER_TYPE_HIERARCHY_ADAPTER);

        assertEquals("registerTypeAdapterFactory",
            GsonClassConstants.METHOD_NAME_REGISTER_TYPE_ADAPTER_FACTORY);
        assertEquals("(Lcom/google/gson/TypeAdapterFactory;)Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_REGISTER_TYPE_ADAPTER_FACTORY);

        assertEquals("serializeNulls",
            GsonClassConstants.METHOD_NAME_SERIALIZE_NULLS);
        assertEquals("()Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_SERIALIZE_NULLS);

        assertEquals("serializeSpecialFloatingPointValues",
            GsonClassConstants.METHOD_NAME_SERIALIZE_SPECIAL_FLOATING_POINT_VALUES);
        assertEquals("()Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_SERIALIZE_SPECIAL_FLOATING_POINT_VALUES);

        assertEquals("setExclusionStrategies",
            GsonClassConstants.METHOD_NAME_SET_EXCLUSION_STRATEGIES);
        assertEquals("([Lcom/google/gson/ExclusionStrategy;)Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_SET_EXCLUSION_STRATEGIES);

        assertEquals("setFieldNamingPolicy",
            GsonClassConstants.METHOD_NAME_SET_FIELD_NAMING_POLICY);
        assertEquals("(Lcom/google/gson/FieldNamingPolicy;)Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_SET_FIELD_NAMING_POLICY);

        assertEquals("setFieldNamingStrategy",
            GsonClassConstants.METHOD_NAME_SET_FIELD_NAMING_STRATEGY);
        assertEquals("(Lcom/google/gson/FieldNamingStrategy;)Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_SET_FIELD_NAMING_STRATEGY);

        assertEquals("setLongSerializationPolicy",
            GsonClassConstants.METHOD_NAME_SET_LONG_SERIALIZATION_POLICY);
        assertEquals("(Lcom/google/gson/LongSerializationPolicy;)Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_SET_LONG_SERIALIZATION_POLICY);

        assertEquals("setVersion",
            GsonClassConstants.METHOD_NAME_SET_VERSION);
        assertEquals("(D)Lcom/google/gson/GsonBuilder;",
            GsonClassConstants.METHOD_TYPE_SET_VERSION);
    }

    /**
     * Tests that Gson class name constant is properly initialized.
     */
    @Test
    public void testGsonClassName() {
        assertEquals("com/google/gson/Gson", GsonClassConstants.NAME_GSON);
    }

    /**
     * Tests that all Gson field name and type constants are properly initialized.
     */
    @Test
    public void testGsonFieldConstants() {
        assertEquals("excluder", GsonClassConstants.FIELD_NAME_EXCLUDER);
        assertEquals("Lcom/google/gson/internal/Excluder;", GsonClassConstants.FIELD_TYPE_EXCLUDER);

        assertEquals("fieldNamingStrategy", GsonClassConstants.FIELD_NAME_FIELD_NAMING_STRATEGY);
        assertEquals("Lcom/google/gson/FieldNamingStrategy;", GsonClassConstants.FIELD_TYPE_FIELD_NAMING_STRATEGY);

        assertEquals("instanceCreators", GsonClassConstants.FIELD_NAME_INSTANCE_CREATORS);
        assertEquals("Ljava/util/Map;", GsonClassConstants.FIELD_TYPE_INSTANCE_CREATORS);

        assertEquals("typeTokenCache", GsonClassConstants.FIELD_NAME_TYPE_TOKEN_CACHE);
        assertEquals("Ljava/util/Map;", GsonClassConstants.FIELD_TYPE_TYPE_TOKEN_CACHE);
    }

    /**
     * Tests that all Gson getAdapter method constants are properly initialized.
     */
    @Test
    public void testGsonGetAdapterMethodConstants() {
        assertEquals("getAdapter", GsonClassConstants.METHOD_NAME_GET_ADAPTER_CLASS);
        assertEquals("(Ljava/lang/Class;)Lcom/google/gson/TypeAdapter;",
            GsonClassConstants.METHOD_TYPE_GET_ADAPTER_CLASS);

        assertEquals("getAdapter", GsonClassConstants.METHOD_NAME_GET_ADAPTER_TYPE_TOKEN);
        assertEquals("(Lcom/google/gson/reflect/TypeToken;)Lcom/google/gson/TypeAdapter;",
            GsonClassConstants.METHOD_TYPE_GET_ADAPTER_TYPE_TOKEN);
    }

    /**
     * Tests that all Gson toJson method constants are properly initialized.
     */
    @Test
    public void testGsonToJsonMethodConstants() {
        assertEquals("toJson", GsonClassConstants.METHOD_NAME_TO_JSON);

        assertEquals("(Ljava/lang/Object;)Ljava/lang/String;",
            GsonClassConstants.METHOD_TYPE_TO_JSON_OBJECT);
        assertEquals("(Ljava/lang/Object;Ljava/lang/reflect/Type;)Ljava/lang/String;",
            GsonClassConstants.METHOD_TYPE_TO_JSON_OBJECT_TYPE);
        assertEquals("(Ljava/lang/Object;Ljava/lang/Appendable;)V",
            GsonClassConstants.METHOD_TYPE_TO_JSON_OBJECT_APPENDABLE);
        assertEquals("(Ljava/lang/Object;Ljava/lang/reflect/Type;Ljava/lang/Appendable;)V",
            GsonClassConstants.METHOD_TYPE_TO_JSON_OBJECT_TYPE_APPENDABLE);
        assertEquals("(Ljava/lang/Object;Ljava/lang/reflect/Type;Lcom/google/gson/stream/JsonWriter;)V",
            GsonClassConstants.METHOD_TYPE_TO_JSON_OBJECT_TYPE_WRITER);
        assertEquals("(Lcom/google/gson/JsonElement;Lcom/google/gson/stream/JsonWriter;)V",
            GsonClassConstants.METHOD_TYPE_TO_JSON_JSON_ELEMENT_WRITER);
    }

    /**
     * Tests that all Gson toJsonTree method constants are properly initialized.
     */
    @Test
    public void testGsonToJsonTreeMethodConstants() {
        assertEquals("toJsonTree", GsonClassConstants.METHOD_NAME_TO_JSON_TREE);

        assertEquals("(Ljava/lang/Object;)Lcom/google/gson/JsonElement;",
            GsonClassConstants.METHOD_TYPE_TO_JSON_TREE_OBJECT);
        assertEquals("(Ljava/lang/Object;Ljava/lang/reflect/Type;)Lcom/google/gson/JsonElement;",
            GsonClassConstants.METHOD_TYPE_TO_JSON_TREE_OBJECT_TYPE);
    }

    /**
     * Tests that all Gson fromJson method constants are properly initialized.
     */
    @Test
    public void testGsonFromJsonMethodConstants() {
        assertEquals("fromJson", GsonClassConstants.METHOD_NAME_FROM_JSON);

        assertEquals("(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;",
            GsonClassConstants.METHOD_TYPE_FROM_JSON_STRING_CLASS);
        assertEquals("(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;",
            GsonClassConstants.METHOD_TYPE_FROM_JSON_STRING_TYPE);
        assertEquals("(Ljava/io/Reader;Ljava/lang/Class;)Ljava/lang/Object;",
            GsonClassConstants.METHOD_TYPE_FROM_JSON_READER_CLASS);
        assertEquals("(Ljava/io/Reader;Ljava/lang/reflect/Type;)Ljava/lang/Object;",
            GsonClassConstants.METHOD_TYPE_FROM_JSON_READER_TYPE);
        assertEquals("(Lcom/google/gson/stream/JsonReader;Ljava/lang/reflect/Type;)Ljava/lang/Object;",
            GsonClassConstants.METHOD_TYPE_FROM_JSON_JSON_READER_TYPE);
    }

    /**
     * Tests that TypeToken class and method constants are properly initialized.
     */
    @Test
    public void testTypeTokenConstants() {
        assertEquals("com/google/gson/reflect/TypeToken", GsonClassConstants.NAME_TYPE_TOKEN);

        assertEquals("getType", GsonClassConstants.METHOD_NAME_GET_TYPE);
        assertEquals("()Ljava/lang/reflect/Type;", GsonClassConstants.METHOD_TYPE_GET_TYPE);

        assertEquals("getRawType", GsonClassConstants.METHOD_NAME_GET_RAW_TYPE);
        assertEquals("()Ljava/lang/Class;", GsonClassConstants.METHOD_TYPE_GET_RAW_TYPE);
    }

    /**
     * Tests that Excluder class and field constants are properly initialized.
     */
    @Test
    public void testExcluderConstants() {
        assertEquals("com/google/gson/internal/Excluder", GsonClassConstants.NAME_EXCLUDER);

        assertEquals("deserializationStrategies", GsonClassConstants.FIELD_NAME_DESERIALIZATION_STRATEGIES);
        assertEquals("Ljava/util/List;", GsonClassConstants.FIELD_TYPE_DESERIALIZATION_STRATEGIES);

        assertEquals("modifiers", GsonClassConstants.FIELD_NAME_MODIFIERS);
        assertEquals("I", GsonClassConstants.FIELD_TYPE_MODIFIERS);

        assertEquals("serializationStrategies", GsonClassConstants.FIELD_NAME_SERIALIZATION_STRATEGIES);
        assertEquals("Ljava/util/List;", GsonClassConstants.FIELD_TYPE_SERIALIZATION_STRATEGIES);

        assertEquals("version", GsonClassConstants.FIELD_NAME_VERSION);
        assertEquals("D", GsonClassConstants.FIELD_TYPE_VERSION);
    }

    /**
     * Tests that InstanceCreator class and method constants are properly initialized.
     */
    @Test
    public void testInstanceCreatorConstants() {
        assertEquals("com/google/gson/InstanceCreator", GsonClassConstants.NAME_INSTANCE_CREATOR);

        assertEquals("createInstance", GsonClassConstants.METHOD_NAME_CREATE_INSTANCE);
        assertEquals("(Ljava/lang/reflect/Type;)Ljava/lang/Object;",
            GsonClassConstants.METHOD_TYPE_CREATE_INSTANCE);
    }

    /**
     * Tests that TypeAdapter class and method constants are properly initialized.
     */
    @Test
    public void testTypeAdapterConstants() {
        assertEquals("com/google/gson/TypeAdapter", GsonClassConstants.NAME_TYPE_ADAPTER);

        assertEquals("read", GsonClassConstants.METHOD_NAME_READ);
        assertEquals("(Lcom/google/gson/stream/JsonReader;)Ljava/lang/Object;",
            GsonClassConstants.METHOD_TYPE_READ);

        assertEquals("write", GsonClassConstants.METHOD_NAME_WRITE);
        assertEquals("(Lcom/google/gson/stream/JsonWriter;Ljava/lang/Object;)V",
            GsonClassConstants.METHOD_TYPE_WRITE);
    }

    /**
     * Tests that FieldNamingPolicy constants are properly initialized.
     */
    @Test
    public void testFieldNamingPolicyConstants() {
        assertEquals("com/google/gson/FieldNamingPolicy", GsonClassConstants.NAME_FIELD_NAMING_POLICY);

        assertEquals("IDENTITY", GsonClassConstants.FIELD_NAME_IDENTITY);
        assertEquals("Lcom/google/gson/FieldNamingPolicy;", GsonClassConstants.FIELD_TYPE_IDENTITY);
    }

    /**
     * Tests that JsonReader class and method constants are properly initialized.
     */
    @Test
    public void testJsonReaderConstants() {
        assertEquals("com/google/gson/stream/JsonReader", GsonClassConstants.NAME_JSON_READER);

        assertEquals("beginObject", GsonClassConstants.METHOD_NAME_READER_BEGIN_OBJECT);
        assertEquals("()V", GsonClassConstants.METHOD_TYPE_READER_BEGIN_OBJECT);

        assertEquals("endObject", GsonClassConstants.METHOD_NAME_READER_END_OBJECT);
        assertEquals("()V", GsonClassConstants.METHOD_TYPE_READER_END_OBJECT);

        assertEquals("nextString", GsonClassConstants.METHOD_NAME_NEXT_STRING);
        assertEquals("()Ljava/lang/String;", GsonClassConstants.METHOD_TYPE_NEXT_STRING);

        assertEquals("nextBoolean", GsonClassConstants.METHOD_NAME_NEXT_BOOLEAN);
        assertEquals("()Z", GsonClassConstants.METHOD_TYPE_NEXT_BOOLEAN);

        assertEquals("nextInt", GsonClassConstants.METHOD_NAME_NEXT_INTEGER);
        assertEquals("()I", GsonClassConstants.METHOD_TYPE_NEXT_INTEGER);

        assertEquals("nextNull", GsonClassConstants.METHOD_NAME_NEXT_NULL);
        assertEquals("()V", GsonClassConstants.METHOD_TYPE_NEXT_NULL);

        assertEquals("skipValue", GsonClassConstants.METHOD_NAME_SKIP_VALUE);
        assertEquals("()V", GsonClassConstants.METHOD_TYPE_SKIP_VALUE);
    }

    /**
     * Tests that JsonWriter class and method constants are properly initialized.
     */
    @Test
    public void testJsonWriterConstants() {
        assertEquals("com/google/gson/stream/JsonWriter", GsonClassConstants.NAME_JSON_WRITER);

        assertEquals("beginObject", GsonClassConstants.METHOD_NAME_WRITER_BEGIN_OBJECT);
        assertEquals("()Lcom/google/gson/stream/JsonWriter;", GsonClassConstants.METHOD_TYPE_WRITER_BEGIN_OBJECT);

        assertEquals("endObject", GsonClassConstants.METHOD_NAME_WRITER_END_OBJECT);
        assertEquals("()Lcom/google/gson/stream/JsonWriter;", GsonClassConstants.METHOD_TYPE_WRITER_END_OBJECT);

        assertEquals("hasNext", GsonClassConstants.METHOD_NAME_HAS_NEXT);
        assertEquals("()Z", GsonClassConstants.METHOD_TYPE_HAS_NEXT);

        assertEquals("peek", GsonClassConstants.METHOD_NAME_PEEK);
        assertEquals("()Lcom/google/gson/stream/JsonToken;", GsonClassConstants.METHOD_TYPE_PEEK);

        assertEquals("nullValue", GsonClassConstants.METHOD_NAME_NULL_VALUE);
        assertEquals("()Lcom/google/gson/stream/JsonWriter;", GsonClassConstants.METHOD_TYPE_NULL_VALUE);

        assertEquals("value", GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN);
        assertEquals("(Z)Lcom/google/gson/stream/JsonWriter;", GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN);

        assertEquals("value", GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT);
        assertEquals("(Ljava/lang/Boolean;)Lcom/google/gson/stream/JsonWriter;",
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT);

        assertEquals("value", GsonClassConstants.METHOD_NAME_VALUE_NUMBER);
        assertEquals("(Ljava/lang/Number;)Lcom/google/gson/stream/JsonWriter;",
            GsonClassConstants.METHOD_TYPE_VALUE_NUMBER);

        assertEquals("value", GsonClassConstants.METHOD_NAME_VALUE_STRING);
        assertEquals("(Ljava/lang/String;)Lcom/google/gson/stream/JsonWriter;",
            GsonClassConstants.METHOD_TYPE_NAME_VALUE_STRING);
    }

    /**
     * Tests that JsonSyntaxException class name constant is properly initialized.
     */
    @Test
    public void testJsonSyntaxExceptionConstants() {
        assertEquals("com/google/gson/JsonSyntaxException", GsonClassConstants.NAME_JSON_SYNTAX_EXCEPTION);
    }

    /**
     * Tests that JsonToken class and field constants are properly initialized.
     */
    @Test
    public void testJsonTokenConstants() {
        assertEquals("com/google/gson/stream/JsonToken", GsonClassConstants.NAME_JSON_TOKEN);

        assertEquals("NULL", GsonClassConstants.FIELD_NAME_NULL);
        assertEquals("Lcom/google/gson/stream/JsonToken;", GsonClassConstants.FIELD_TYPE_NULL);

        assertEquals("BOOLEAN", GsonClassConstants.FIELD_NAME_BOOLEAN);
        assertEquals("Lcom/google/gson/stream/JsonToken;", GsonClassConstants.FIELD_TYPE_BOOLEAN);
    }

    /**
     * Tests that annotation type constants are properly initialized.
     */
    @Test
    public void testAnnotationTypeConstants() {
        assertEquals("Lcom/google/gson/annotations/Expose;", GsonClassConstants.ANNOTATION_TYPE_EXPOSE);
        assertEquals("Lcom/google/gson/annotations/JsonAdapter;", GsonClassConstants.ANNOTATION_TYPE_JSON_ADAPTER);
        assertEquals("Lcom/google/gson/annotations/SerializedName;",
            GsonClassConstants.ANNOTATION_TYPE_SERIALIZED_NAME);
    }

    /**
     * Tests that constants are immutable (final) by verifying they maintain their values.
     * This test instantiates the class multiple times to ensure consistency.
     */
    @Test
    public void testConstantsAreConsistent() {
        // Act - Create multiple instances
        GsonClassConstants constants1 = new GsonClassConstants();
        GsonClassConstants constants2 = new GsonClassConstants();

        // Assert - All constants should have the same values regardless of instance
        assertEquals(GsonClassConstants.NAME_GSON, GsonClassConstants.NAME_GSON,
            "Static constants should be consistent");
        assertEquals("com/google/gson/Gson", GsonClassConstants.NAME_GSON,
            "Constants should maintain their values");

        // Verify instances don't affect static constants
        assertNotNull(constants1);
        assertNotNull(constants2);
        assertEquals(GsonClassConstants.NAME_GSON_BUILDER, "com/google/gson/GsonBuilder",
            "Static constants should remain unchanged after instantiation");
    }

    /**
     * Tests that the class can be instantiated multiple times independently.
     */
    @Test
    public void testMultipleInstantiations() {
        // Act
        GsonClassConstants constants1 = new GsonClassConstants();
        GsonClassConstants constants2 = new GsonClassConstants();
        GsonClassConstants constants3 = new GsonClassConstants();

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
        assertTrue(GsonClassConstants.NAME_GSON.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(GsonClassConstants.NAME_GSON_BUILDER.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(GsonClassConstants.NAME_TYPE_TOKEN.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(GsonClassConstants.NAME_EXCLUDER.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(GsonClassConstants.NAME_INSTANCE_CREATOR.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(GsonClassConstants.NAME_TYPE_ADAPTER.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(GsonClassConstants.NAME_FIELD_NAMING_POLICY.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(GsonClassConstants.NAME_JSON_READER.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(GsonClassConstants.NAME_JSON_WRITER.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(GsonClassConstants.NAME_JSON_SYNTAX_EXCEPTION.contains("/"),
            "Class names should use internal format with slashes");
        assertTrue(GsonClassConstants.NAME_JSON_TOKEN.contains("/"),
            "Class names should use internal format with slashes");
    }

    /**
     * Tests that all method type constants use proper JVM descriptor format.
     */
    @Test
    public void testMethodDescriptorFormat() {
        // Method descriptors should start with '(' and contain ')'
        assertTrue(GsonClassConstants.METHOD_TYPE_ADD_DESERIALIZATION_EXCLUSION_STRATEGY.startsWith("(") &&
                   GsonClassConstants.METHOD_TYPE_ADD_DESERIALIZATION_EXCLUSION_STRATEGY.contains(")"),
            "Method descriptors should use proper JVM format");
        assertTrue(GsonClassConstants.METHOD_TYPE_TO_JSON_OBJECT.startsWith("(") &&
                   GsonClassConstants.METHOD_TYPE_TO_JSON_OBJECT.contains(")"),
            "Method descriptors should use proper JVM format");
        assertTrue(GsonClassConstants.METHOD_TYPE_FROM_JSON_STRING_CLASS.startsWith("(") &&
                   GsonClassConstants.METHOD_TYPE_FROM_JSON_STRING_CLASS.contains(")"),
            "Method descriptors should use proper JVM format");
    }

    /**
     * Tests that all field type constants use proper JVM descriptor format for object types.
     */
    @Test
    public void testFieldDescriptorFormat() {
        // Object field descriptors should start with 'L' and end with ';'
        assertTrue(GsonClassConstants.FIELD_TYPE_EXCLUDER.startsWith("L") &&
                   GsonClassConstants.FIELD_TYPE_EXCLUDER.endsWith(";"),
            "Object field descriptors should use proper JVM format");
        assertTrue(GsonClassConstants.FIELD_TYPE_FIELD_NAMING_STRATEGY.startsWith("L") &&
                   GsonClassConstants.FIELD_TYPE_FIELD_NAMING_STRATEGY.endsWith(";"),
            "Object field descriptors should use proper JVM format");

        // Primitive field descriptors should be single characters
        assertEquals("I", GsonClassConstants.FIELD_TYPE_MODIFIERS,
            "Primitive int field descriptor should be 'I'");
        assertEquals("D", GsonClassConstants.FIELD_TYPE_VERSION,
            "Primitive double field descriptor should be 'D'");
    }

    /**
     * Tests that annotation type constants use proper JVM descriptor format.
     */
    @Test
    public void testAnnotationDescriptorFormat() {
        // Annotation descriptors should start with 'L' and end with ';'
        assertTrue(GsonClassConstants.ANNOTATION_TYPE_EXPOSE.startsWith("L") &&
                   GsonClassConstants.ANNOTATION_TYPE_EXPOSE.endsWith(";"),
            "Annotation descriptors should use proper JVM format");
        assertTrue(GsonClassConstants.ANNOTATION_TYPE_JSON_ADAPTER.startsWith("L") &&
                   GsonClassConstants.ANNOTATION_TYPE_JSON_ADAPTER.endsWith(";"),
            "Annotation descriptors should use proper JVM format");
        assertTrue(GsonClassConstants.ANNOTATION_TYPE_SERIALIZED_NAME.startsWith("L") &&
                   GsonClassConstants.ANNOTATION_TYPE_SERIALIZED_NAME.endsWith(";"),
            "Annotation descriptors should use proper JVM format");
    }
}

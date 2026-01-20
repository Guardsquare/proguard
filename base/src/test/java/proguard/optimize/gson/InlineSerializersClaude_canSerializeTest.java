package proguard.optimize.gson;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.editor.ClassBuilder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for canSerialize method in InlineSerializers.
 * Tests:
 * - {@link InlineSerializers.InlineBooleanSerializer#canSerialize(ClassPool, GsonRuntimeSettings)}
 * - {@link InlineSerializers.InlinePrimitiveBooleanSerializer#canSerialize(ClassPool, GsonRuntimeSettings)}
 * - {@link InlineSerializers.InlinePrimitiveIntegerSerializer#canSerialize(ClassPool, GsonRuntimeSettings)}
 * - {@link InlineSerializers.InlineStringSerializer#canSerialize(ClassPool, GsonRuntimeSettings)}
 */
public class InlineSerializersClaude_canSerializeTest {

    /**
     * Tests that canSerialize returns true when no custom type adapter for Boolean is registered
     * and JsonWriter.value(Boolean) method is present.
     * This is the common case where inline serialization should be used.
     */
    @Test
    public void testCanSerializeReturnsTrueWhenNoCustomTypeAdapterAndMethodExists() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a JsonWriter class with value(Boolean) method
        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001, // public
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when no custom type adapter is registered and value(Boolean) method exists");
    }

    /**
     * Tests that canSerialize returns false when a custom type adapter for Boolean is registered,
     * even if JsonWriter.value(Boolean) method exists.
     */
    @Test
    public void testCanSerializeReturnsFalseWhenCustomTypeAdapterRegistered() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a JsonWriter class with value(Boolean) method
        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001, // public
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Create a ProgramClass representing java.lang.Boolean
        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the Boolean class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(booleanClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertFalse(result, "canSerialize should return false when a custom type adapter is registered for Boolean");
    }

    /**
     * Tests that canSerialize returns false when JsonWriter.value(Boolean) method is not present,
     * even if no custom type adapter is registered.
     */
    @Test
    public void testCanSerializeReturnsFalseWhenValueBooleanMethodNotPresent() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a JsonWriter class WITHOUT value(Boolean) method
        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        // Only add the primitive boolean value method, not the Boolean object one
        .addMethod(
            0x0001, // public
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertFalse(result, "canSerialize should return false when JsonWriter.value(Boolean) method is not present");
    }

    /**
     * Tests that canSerialize returns false when JsonWriter class itself is not present in the class pool.
     */
    @Test
    public void testCanSerializeReturnsFalseWhenJsonWriterClassNotPresent() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool(); // Empty class pool

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            serializer.canSerialize(programClassPool, settings);
        }, "canSerialize should throw NullPointerException when JsonWriter class is not present");
    }

    /**
     * Tests that canSerialize is consistent when called multiple times with the same settings.
     */
    @Test
    public void testCanSerializeIsConsistentWithSameSettings() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Act
        boolean result1 = serializer.canSerialize(programClassPool, settings);
        boolean result2 = serializer.canSerialize(programClassPool, settings);
        boolean result3 = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertEquals(result1, result2, "canSerialize should return the same result when called multiple times");
        assertEquals(result2, result3, "canSerialize should return the same result when called multiple times");
        assertTrue(result1, "canSerialize should return true for valid conditions");
    }

    /**
     * Tests that canSerialize works correctly with different serializer instances.
     * The behavior should depend only on the parameters, not on the serializer instance.
     */
    @Test
    public void testCanSerializeWorksWithDifferentSerializerInstances() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer1 =
            new InlineSerializers.InlineBooleanSerializer();
        InlineSerializers.InlineBooleanSerializer serializer2 =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Act
        boolean result1 = serializer1.canSerialize(programClassPool, settings);
        boolean result2 = serializer2.canSerialize(programClassPool, settings);

        // Assert
        assertEquals(result1, result2, "Different serializer instances should return the same result for the same parameters");
        assertTrue(result1, "Both serializers should return true for valid conditions");
    }

    /**
     * Tests that canSerialize correctly handles different GsonRuntimeSettings instances.
     * Each settings instance should be evaluated independently.
     */
    @Test
    public void testCanSerializeWithDifferentSettingsInstances() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();

        ClassPool programClassPool = new ClassPool();
        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        GsonRuntimeSettings settingsWithoutAdapter = new GsonRuntimeSettings();

        GsonRuntimeSettings settingsWithAdapter = new GsonRuntimeSettings();
        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settingsWithAdapter.typeAdapterClassPool.addClass(booleanClass);

        // Act
        boolean resultWithoutAdapter = serializer.canSerialize(programClassPool, settingsWithoutAdapter);
        boolean resultWithAdapter = serializer.canSerialize(programClassPool, settingsWithAdapter);

        // Assert
        assertTrue(resultWithoutAdapter, "Should return true when no custom type adapter is registered");
        assertFalse(resultWithAdapter, "Should return false when custom type adapter is registered");
    }

    /**
     * Tests that canSerialize works correctly after adding a class to the type adapter pool.
     * This ensures the method properly queries the typeAdapterClassPool.
     */
    @Test
    public void testCanSerializeAfterAddingClassToTypeAdapterPool() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Act & Assert - Before adding
        assertTrue(serializer.canSerialize(programClassPool, settings),
            "Should return true before adding Boolean type adapter");

        // Add Boolean type adapter
        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(booleanClass);

        // Act & Assert - After adding
        assertFalse(serializer.canSerialize(programClassPool, settings),
            "Should return false after adding Boolean type adapter");
    }

    /**
     * Tests that an empty type adapter ClassPool results in canSerialize potentially returning true
     * (if other conditions are met). This verifies the default behavior when no type adapters are registered.
     */
    @Test
    public void testCanSerializeWithEmptyTypeAdapterClassPool() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Verify the pool is empty
        assertEquals(0, settings.typeAdapterClassPool.size(), "typeAdapterClassPool should be empty");

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when typeAdapterClassPool is empty and method exists");
    }

    /**
     * Tests that canSerialize returns true when a type adapter for Integer (not Boolean) is registered.
     * The method should only check for Boolean type adapters.
     */
    @Test
    public void testCanSerializeReturnsTrueWhenIntegerAdapterRegistered() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Create a ProgramClass representing java.lang.Integer (not Boolean)
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the Integer class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(integerClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when type adapter for Integer (not Boolean) is registered");
    }

    /**
     * Tests that canSerialize returns true when a type adapter for String (not Boolean) is registered.
     * The method should only check for Boolean type adapters.
     */
    @Test
    public void testCanSerializeReturnsTrueWhenStringAdapterRegistered() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Create a ProgramClass representing java.lang.String (not Boolean)
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the String class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(stringClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when type adapter for String (not Boolean) is registered");
    }

    /**
     * Tests that canSerialize returns false when JsonWriter class has other methods but not value(Boolean).
     */
    @Test
    public void testCanSerializeReturnsFalseWhenJsonWriterHasOtherMethodsButNotValueBoolean() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a JsonWriter class with other methods but not value(Boolean)
        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_STRING,
            GsonClassConstants.METHOD_TYPE_NAME_VALUE_STRING
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_NUMBER,
            GsonClassConstants.METHOD_TYPE_VALUE_NUMBER
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertFalse(result, "canSerialize should return false when JsonWriter has other methods but not value(Boolean)");
    }

    /**
     * Tests that canSerialize checks both conditions correctly:
     * - No custom Boolean type adapter
     * - JsonWriter.value(Boolean) method exists
     * This test verifies the AND logic between the two conditions.
     */
    @Test
    public void testCanSerializeChecksBothConditions() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();

        // Setup 1: No adapter, but no method either
        GsonRuntimeSettings settings1 = new GsonRuntimeSettings();
        ClassPool programClassPool1 = new ClassPool();
        ProgramClass jsonWriterClassNoMethod = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        programClassPool1.addClass(jsonWriterClassNoMethod);

        // Setup 2: Has method, but has adapter
        GsonRuntimeSettings settings2 = new GsonRuntimeSettings();
        ClassPool programClassPool2 = new ClassPool();
        ProgramClass jsonWriterClassWithMethod = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();
        programClassPool2.addClass(jsonWriterClassWithMethod);
        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings2.typeAdapterClassPool.addClass(booleanClass);

        // Setup 3: Has method, no adapter (should return true)
        GsonRuntimeSettings settings3 = new GsonRuntimeSettings();
        ClassPool programClassPool3 = new ClassPool();
        programClassPool3.addClass(jsonWriterClassWithMethod);

        // Act
        boolean result1 = serializer.canSerialize(programClassPool1, settings1);
        boolean result2 = serializer.canSerialize(programClassPool2, settings2);
        boolean result3 = serializer.canSerialize(programClassPool3, settings3);

        // Assert
        assertFalse(result1, "Should return false when method doesn't exist (even without adapter)");
        assertFalse(result2, "Should return false when adapter exists (even with method)");
        assertTrue(result3, "Should return true only when both conditions are met");
    }

    /**
     * Tests that canSerialize works with multiple type adapters registered, including Boolean.
     */
    @Test
    public void testCanSerializeWithMultipleTypeAdaptersIncludingBoolean() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Add multiple type adapters
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(stringClass);

        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(integerClass);

        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(booleanClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertFalse(result, "canSerialize should return false when Boolean adapter is present among multiple adapters");
    }

    /**
     * Tests that canSerialize works with multiple type adapters registered, NOT including Boolean.
     */
    @Test
    public void testCanSerializeWithMultipleTypeAdaptersExcludingBoolean() {
        // Arrange
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN_OBJECT,
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN_OBJECT
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Add multiple type adapters, but NOT Boolean
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(stringClass);

        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(integerClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when Boolean adapter is not present among multiple adapters");
    }

    // ==================== Tests for InlinePrimitiveBooleanSerializer ====================

    /**
     * Tests that canSerialize returns true when no custom type adapter for Boolean is registered.
     * This is the common case for primitive boolean serialization.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeReturnsTrueWhenNoCustomTypeAdapter() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when no custom type adapter is registered for Boolean");
    }

    /**
     * Tests that canSerialize returns false when a custom type adapter for Boolean is registered.
     * Unlike InlineBooleanSerializer, this doesn't check for JsonWriter.value(Boolean) method.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeReturnsFalseWhenCustomTypeAdapterRegistered() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a ProgramClass representing java.lang.Boolean
        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the Boolean class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(booleanClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertFalse(result, "canSerialize should return false when a custom type adapter is registered for Boolean");
    }

    /**
     * Tests that canSerialize is consistent when called multiple times with the same settings.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeIsConsistentWithSameSettings() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act
        boolean result1 = serializer.canSerialize(programClassPool, settings);
        boolean result2 = serializer.canSerialize(programClassPool, settings);
        boolean result3 = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertEquals(result1, result2, "canSerialize should return the same result when called multiple times");
        assertEquals(result2, result3, "canSerialize should return the same result when called multiple times");
        assertTrue(result1, "canSerialize should return true for valid conditions");
    }

    /**
     * Tests that canSerialize works correctly with different serializer instances.
     * The behavior should depend only on the parameters, not on the serializer instance.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeWorksWithDifferentSerializerInstances() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer1 =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer2 =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act
        boolean result1 = serializer1.canSerialize(programClassPool, settings);
        boolean result2 = serializer2.canSerialize(programClassPool, settings);

        // Assert
        assertEquals(result1, result2, "Different serializer instances should return the same result for the same parameters");
        assertTrue(result1, "Both serializers should return true for valid conditions");
    }

    /**
     * Tests that canSerialize correctly handles different GsonRuntimeSettings instances.
     * Each settings instance should be evaluated independently.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeWithDifferentSettingsInstances() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        ClassPool programClassPool = new ClassPool();

        GsonRuntimeSettings settingsWithoutAdapter = new GsonRuntimeSettings();

        GsonRuntimeSettings settingsWithAdapter = new GsonRuntimeSettings();
        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settingsWithAdapter.typeAdapterClassPool.addClass(booleanClass);

        // Act
        boolean resultWithoutAdapter = serializer.canSerialize(programClassPool, settingsWithoutAdapter);
        boolean resultWithAdapter = serializer.canSerialize(programClassPool, settingsWithAdapter);

        // Assert
        assertTrue(resultWithoutAdapter, "Should return true when no custom type adapter is registered");
        assertFalse(resultWithAdapter, "Should return false when custom type adapter is registered");
    }

    /**
     * Tests that canSerialize works correctly after adding a class to the type adapter pool.
     * This ensures the method properly queries the typeAdapterClassPool.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeAfterAddingClassToTypeAdapterPool() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act & Assert - Before adding
        assertTrue(serializer.canSerialize(programClassPool, settings),
            "Should return true before adding Boolean type adapter");

        // Add Boolean type adapter
        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(booleanClass);

        // Act & Assert - After adding
        assertFalse(serializer.canSerialize(programClassPool, settings),
            "Should return false after adding Boolean type adapter");
    }

    /**
     * Tests that an empty type adapter ClassPool results in canSerialize returning true.
     * This verifies the default behavior when no type adapters are registered.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeWithEmptyTypeAdapterClassPool() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Verify the pool is empty
        assertEquals(0, settings.typeAdapterClassPool.size(), "typeAdapterClassPool should be empty");

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when typeAdapterClassPool is empty");
    }

    /**
     * Tests that canSerialize returns true when a type adapter for Integer (not Boolean) is registered.
     * The method should only check for Boolean type adapters.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeReturnsTrueWhenIntegerAdapterRegistered() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a ProgramClass representing java.lang.Integer (not Boolean)
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the Integer class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(integerClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when type adapter for Integer (not Boolean) is registered");
    }

    /**
     * Tests that canSerialize returns true when a type adapter for String (not Boolean) is registered.
     * The method should only check for Boolean type adapters.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeReturnsTrueWhenStringAdapterRegistered() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a ProgramClass representing java.lang.String (not Boolean)
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the String class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(stringClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when type adapter for String (not Boolean) is registered");
    }

    /**
     * Tests that canSerialize works with multiple type adapters registered, including Boolean.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeWithMultipleTypeAdaptersIncludingBoolean() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Add multiple type adapters
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(stringClass);

        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(integerClass);

        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(booleanClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertFalse(result, "canSerialize should return false when Boolean adapter is present among multiple adapters");
    }

    /**
     * Tests that canSerialize works with multiple type adapters registered, NOT including Boolean.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeWithMultipleTypeAdaptersExcludingBoolean() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Add multiple type adapters, but NOT Boolean
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(stringClass);

        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(integerClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when Boolean adapter is not present among multiple adapters");
    }

    /**
     * Tests that canSerialize doesn't require JsonWriter class to be present in the program class pool.
     * Unlike InlineBooleanSerializer, InlinePrimitiveBooleanSerializer only checks for type adapters.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeDoesNotRequireJsonWriterClass() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool(); // Empty class pool - no JsonWriter

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true even when JsonWriter class is not present");
    }

    /**
     * Tests that canSerialize returns true regardless of what methods are available in JsonWriter.
     * InlinePrimitiveBooleanSerializer doesn't check for specific methods.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeIgnoresJsonWriterMethods() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a JsonWriter class with various methods but not value(Boolean)
        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_STRING,
            GsonClassConstants.METHOD_TYPE_NAME_VALUE_STRING
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_NUMBER,
            GsonClassConstants.METHOD_TYPE_VALUE_NUMBER
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true regardless of JsonWriter methods (only checks type adapter)");
    }

    /**
     * Tests that canSerialize behavior differs from InlineBooleanSerializer.
     * InlinePrimitiveBooleanSerializer doesn't require JsonWriter.value(Boolean) method.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeDiffersFromInlineBooleanSerializer() {
        // Arrange - Setup without JsonWriter.value(Boolean) method
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create JsonWriter without value(Boolean) method
        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_BOOLEAN, // Only primitive boolean method
            GsonClassConstants.METHOD_TYPE_VALUE_BOOLEAN
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        InlineSerializers.InlinePrimitiveBooleanSerializer primitiveSerializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        InlineSerializers.InlineBooleanSerializer booleanSerializer =
            new InlineSerializers.InlineBooleanSerializer();

        // Act
        boolean primitiveResult = primitiveSerializer.canSerialize(programClassPool, settings);
        boolean booleanResult = booleanSerializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(primitiveResult, "InlinePrimitiveBooleanSerializer should return true (doesn't check for method)");
        assertFalse(booleanResult, "InlineBooleanSerializer should return false (requires value(Boolean) method)");
        assertNotEquals(primitiveResult, booleanResult, "The two serializers should have different behavior");
    }

    /**
     * Tests that canSerialize with null programClassPool parameter.
     * The method doesn't use programClassPool, so it should work regardless.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeWithNullProgramClassPool() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Act
        boolean result = serializer.canSerialize(null, settings);

        // Assert
        assertTrue(result, "canSerialize should return true with null programClassPool when no type adapter registered");
    }

    /**
     * Tests that canSerialize requires non-null GsonRuntimeSettings.
     */
    @Test
    public void testPrimitiveBooleanCanSerializeWithNullSettings() {
        // Arrange
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        ClassPool programClassPool = new ClassPool();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            serializer.canSerialize(programClassPool, null);
        }, "canSerialize should throw NullPointerException with null GsonRuntimeSettings");
    }

    // ==================== Tests for InlinePrimitiveIntegerSerializer ====================

    /**
     * Tests that canSerialize returns true when no custom type adapter for Integer is registered.
     * This is the common case for primitive integer serialization.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeReturnsTrueWhenNoCustomTypeAdapter() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when no custom type adapter is registered for Integer");
    }

    /**
     * Tests that canSerialize returns false when a custom type adapter for Integer is registered.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeReturnsFalseWhenCustomTypeAdapterRegistered() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a ProgramClass representing java.lang.Integer
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the Integer class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(integerClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertFalse(result, "canSerialize should return false when a custom type adapter is registered for Integer");
    }

    /**
     * Tests that canSerialize is consistent when called multiple times with the same settings.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeIsConsistentWithSameSettings() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act
        boolean result1 = serializer.canSerialize(programClassPool, settings);
        boolean result2 = serializer.canSerialize(programClassPool, settings);
        boolean result3 = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertEquals(result1, result2, "canSerialize should return the same result when called multiple times");
        assertEquals(result2, result3, "canSerialize should return the same result when called multiple times");
        assertTrue(result1, "canSerialize should return true for valid conditions");
    }

    /**
     * Tests that canSerialize works correctly with different serializer instances.
     * The behavior should depend only on the parameters, not on the serializer instance.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeWorksWithDifferentSerializerInstances() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer1 =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer2 =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act
        boolean result1 = serializer1.canSerialize(programClassPool, settings);
        boolean result2 = serializer2.canSerialize(programClassPool, settings);

        // Assert
        assertEquals(result1, result2, "Different serializer instances should return the same result for the same parameters");
        assertTrue(result1, "Both serializers should return true for valid conditions");
    }

    /**
     * Tests that canSerialize correctly handles different GsonRuntimeSettings instances.
     * Each settings instance should be evaluated independently.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeWithDifferentSettingsInstances() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        ClassPool programClassPool = new ClassPool();

        GsonRuntimeSettings settingsWithoutAdapter = new GsonRuntimeSettings();

        GsonRuntimeSettings settingsWithAdapter = new GsonRuntimeSettings();
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settingsWithAdapter.typeAdapterClassPool.addClass(integerClass);

        // Act
        boolean resultWithoutAdapter = serializer.canSerialize(programClassPool, settingsWithoutAdapter);
        boolean resultWithAdapter = serializer.canSerialize(programClassPool, settingsWithAdapter);

        // Assert
        assertTrue(resultWithoutAdapter, "Should return true when no custom type adapter is registered");
        assertFalse(resultWithAdapter, "Should return false when custom type adapter is registered");
    }

    /**
     * Tests that canSerialize works correctly after adding a class to the type adapter pool.
     * This ensures the method properly queries the typeAdapterClassPool.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeAfterAddingClassToTypeAdapterPool() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act & Assert - Before adding
        assertTrue(serializer.canSerialize(programClassPool, settings),
            "Should return true before adding Integer type adapter");

        // Add Integer type adapter
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(integerClass);

        // Act & Assert - After adding
        assertFalse(serializer.canSerialize(programClassPool, settings),
            "Should return false after adding Integer type adapter");
    }

    /**
     * Tests that an empty type adapter ClassPool results in canSerialize returning true.
     * This verifies the default behavior when no type adapters are registered.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeWithEmptyTypeAdapterClassPool() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Verify the pool is empty
        assertEquals(0, settings.typeAdapterClassPool.size(), "typeAdapterClassPool should be empty");

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when typeAdapterClassPool is empty");
    }

    /**
     * Tests that canSerialize returns true when a type adapter for Boolean (not Integer) is registered.
     * The method should only check for Integer type adapters.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeReturnsTrueWhenBooleanAdapterRegistered() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a ProgramClass representing java.lang.Boolean (not Integer)
        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the Boolean class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(booleanClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when type adapter for Boolean (not Integer) is registered");
    }

    /**
     * Tests that canSerialize returns true when a type adapter for String (not Integer) is registered.
     * The method should only check for Integer type adapters.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeReturnsTrueWhenStringAdapterRegistered() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a ProgramClass representing java.lang.String (not Integer)
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the String class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(stringClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when type adapter for String (not Integer) is registered");
    }

    /**
     * Tests that canSerialize works with multiple type adapters registered, including Integer.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeWithMultipleTypeAdaptersIncludingInteger() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Add multiple type adapters
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(stringClass);

        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(booleanClass);

        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(integerClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertFalse(result, "canSerialize should return false when Integer adapter is present among multiple adapters");
    }

    /**
     * Tests that canSerialize works with multiple type adapters registered, NOT including Integer.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeWithMultipleTypeAdaptersExcludingInteger() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Add multiple type adapters, but NOT Integer
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(stringClass);

        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(booleanClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when Integer adapter is not present among multiple adapters");
    }

    /**
     * Tests that canSerialize doesn't require JsonWriter class to be present in the program class pool.
     * InlinePrimitiveIntegerSerializer only checks for type adapters.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeDoesNotRequireJsonWriterClass() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool(); // Empty class pool - no JsonWriter

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true even when JsonWriter class is not present");
    }

    /**
     * Tests that canSerialize returns true regardless of what methods are available in JsonWriter.
     * InlinePrimitiveIntegerSerializer doesn't check for specific methods.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeIgnoresJsonWriterMethods() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a JsonWriter class with various methods
        ProgramClass jsonWriterClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            GsonClassConstants.NAME_JSON_WRITER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_STRING,
            GsonClassConstants.METHOD_TYPE_NAME_VALUE_STRING
        )
        .addMethod(
            0x0001,
            GsonClassConstants.METHOD_NAME_VALUE_NUMBER,
            GsonClassConstants.METHOD_TYPE_VALUE_NUMBER
        )
        .getProgramClass();

        programClassPool.addClass(jsonWriterClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true regardless of JsonWriter methods (only checks type adapter)");
    }

    /**
     * Tests that canSerialize with null programClassPool parameter.
     * The method doesn't use programClassPool, so it should work regardless.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeWithNullProgramClassPool() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Act
        boolean result = serializer.canSerialize(null, settings);

        // Assert
        assertTrue(result, "canSerialize should return true with null programClassPool when no type adapter registered");
    }

    /**
     * Tests that canSerialize requires non-null GsonRuntimeSettings.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeWithNullSettings() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        ClassPool programClassPool = new ClassPool();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            serializer.canSerialize(programClassPool, null);
        }, "canSerialize should throw NullPointerException with null GsonRuntimeSettings");
    }

    /**
     * Tests that canSerialize correctly checks the typeAdapterClassPool within GsonRuntimeSettings.
     * Verifies the exact class name lookup for java.lang.Integer.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeChecksExactIntegerClassName() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a ProgramClass with a similar but different name
        ProgramClass customIntegerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            "com/custom/Integer", // Different from java/lang/Integer
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        settings.typeAdapterClassPool.addClass(customIntegerClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when a different Integer class (not java.lang.Integer) is registered");
    }

    /**
     * Tests that canSerialize behavior is correct when removing a type adapter.
     * Verifies the method reflects the current state of typeAdapterClassPool.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeAfterRemovingClassFromTypeAdapterPool() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add and then remove the Integer type adapter
        settings.typeAdapterClassPool.addClass(integerClass);
        assertFalse(serializer.canSerialize(programClassPool, settings),
            "Should return false when Integer type adapter is present");

        settings.typeAdapterClassPool.removeClass(ClassConstants.NAME_JAVA_LANG_INTEGER);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "Should return true after removing Integer type adapter");
    }

    /**
     * Tests that different primitive integer types are all affected by the Integer type adapter.
     * This verifies that the serializer is for primitive int, short, and byte values as mentioned in the comments.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeAffectsAllPrimitiveIntegerTypes() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Note: According to the class comment, this serializer handles primitive int, short, and byte
        // But the canSerialize method only checks for java.lang.Integer type adapter
        // This test verifies that behavior

        // Without Integer adapter
        assertTrue(serializer.canSerialize(programClassPool, settings),
            "Should return true without Integer adapter (allows inline serialization for int/short/byte)");

        // Add Integer type adapter
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(integerClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertFalse(result, "Should return false with Integer adapter (prevents inline serialization for int/short/byte)");
    }

    /**
     * Tests that canSerialize returns true when Long type adapter is registered.
     * The method should only check for Integer, not Long.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeReturnsTrueWhenLongAdapterRegistered() {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a ProgramClass representing java.lang.Long (not Integer)
        ProgramClass longClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_LONG,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the Long class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(longClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when type adapter for Long (not Integer) is registered");
    }

    /**
     * Tests consistency of canSerialize across multiple threads.
     * The method should be thread-safe as it only reads from the settings.
     */
    @Test
    public void testPrimitiveIntegerCanSerializeThreadSafety() throws InterruptedException {
        // Arrange
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        int numThreads = 10;
        Thread[] threads = new Thread[numThreads];
        boolean[] results = new boolean[numThreads];

        // Act - Call canSerialize from multiple threads
        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = serializer.canSerialize(programClassPool, settings);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - All results should be true and consistent
        for (int i = 0; i < numThreads; i++) {
            assertTrue(results[i], "Result from thread " + i + " should be true");
        }
    }

    // ==================== Tests for InlineStringSerializer ====================

    /**
     * Tests that canSerialize returns true when no custom type adapter for String is registered.
     * This is the common case for string serialization.
     */
    @Test
    public void testStringCanSerializeReturnsTrueWhenNoCustomTypeAdapter() {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when no custom type adapter is registered for String");
    }

    /**
     * Tests that canSerialize returns false when a custom type adapter for String is registered.
     */
    @Test
    public void testStringCanSerializeReturnsFalseWhenCustomTypeAdapterRegistered() {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a ProgramClass representing java.lang.String
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the String class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(stringClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertFalse(result, "canSerialize should return false when a custom type adapter is registered for String");
    }

    /**
     * Tests that canSerialize is consistent when called multiple times with the same settings.
     */
    @Test
    public void testStringCanSerializeIsConsistentWithSameSettings() {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act
        boolean result1 = serializer.canSerialize(programClassPool, settings);
        boolean result2 = serializer.canSerialize(programClassPool, settings);
        boolean result3 = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertEquals(result1, result2, "canSerialize should return the same result when called multiple times");
        assertEquals(result2, result3, "canSerialize should return the same result when called multiple times");
        assertTrue(result1, "canSerialize should return true for valid conditions");
    }

    /**
     * Tests that canSerialize works correctly with different serializer instances.
     * The behavior should depend only on the parameters, not on the serializer instance.
     */
    @Test
    public void testStringCanSerializeWorksWithDifferentSerializerInstances() {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer1 =
            new InlineSerializers.InlineStringSerializer();
        InlineSerializers.InlineStringSerializer serializer2 =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act
        boolean result1 = serializer1.canSerialize(programClassPool, settings);
        boolean result2 = serializer2.canSerialize(programClassPool, settings);

        // Assert
        assertEquals(result1, result2, "Different serializer instances should return the same result for the same parameters");
        assertTrue(result1, "Both serializers should return true for valid conditions");
    }

    /**
     * Tests that canSerialize correctly handles different GsonRuntimeSettings instances.
     * Each settings instance should be evaluated independently.
     */
    @Test
    public void testStringCanSerializeHandlesDifferentSettingsInstances() {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settings1 = new GsonRuntimeSettings();
        GsonRuntimeSettings settings2 = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Create a ProgramClass representing java.lang.String
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the String class to only the second settings' type adapter class pool
        settings2.typeAdapterClassPool.addClass(stringClass);

        // Act
        boolean result1 = serializer.canSerialize(programClassPool, settings1);
        boolean result2 = serializer.canSerialize(programClassPool, settings2);

        // Assert
        assertTrue(result1, "canSerialize should return true when no custom type adapter is registered");
        assertFalse(result2, "canSerialize should return false when a custom type adapter is registered");
    }

    /**
     * Tests that canSerialize correctly handles empty ClassPool.
     * The programClassPool parameter is not used by InlineStringSerializer, so an empty pool is valid.
     */
    @Test
    public void testStringCanSerializeHandlesEmptyClassPool() {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true with empty ClassPool when no custom type adapter is registered");
    }

    /**
     * Tests that canSerialize handles settings with an empty type adapter class pool.
     * This should be treated the same as having no custom type adapter registered.
     */
    @Test
    public void testStringCanSerializeHandlesEmptyTypeAdapterClassPool() {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Ensure typeAdapterClassPool is empty (it should be by default)
        assertNotNull(settings.typeAdapterClassPool, "typeAdapterClassPool should be initialized");

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when typeAdapterClassPool is empty");
    }

    /**
     * Tests that canSerialize is not affected by classes in the program class pool.
     * The InlineStringSerializer only checks the type adapter class pool.
     */
    @Test
    public void testStringCanSerializeIgnoresProgramClassPool() {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Add String class to program class pool (not type adapter pool)
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        programClassPool.addClass(stringClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true as it only checks typeAdapterClassPool, not programClassPool");
    }

    /**
     * Tests that canSerialize correctly handles multiple classes in type adapter pool
     * where String is one of them.
     */
    @Test
    public void testStringCanSerializeWithMultipleTypeAdaptersIncludingString() {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Add multiple classes to type adapter class pool
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        settings.typeAdapterClassPool.addClass(integerClass);
        settings.typeAdapterClassPool.addClass(stringClass);
        settings.typeAdapterClassPool.addClass(booleanClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertFalse(result, "canSerialize should return false when String type adapter is registered, even with other adapters");
    }

    /**
     * Tests that canSerialize correctly handles multiple classes in type adapter pool
     * where String is NOT one of them.
     */
    @Test
    public void testStringCanSerializeWithMultipleTypeAdaptersNotIncludingString() {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Add multiple classes to type adapter class pool (but not String)
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        ProgramClass booleanClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_BOOLEAN,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        settings.typeAdapterClassPool.addClass(integerClass);
        settings.typeAdapterClassPool.addClass(booleanClass);

        // Act
        boolean result = serializer.canSerialize(programClassPool, settings);

        // Assert
        assertTrue(result, "canSerialize should return true when String type adapter is not registered, even with other adapters present");
    }

    /**
     * Tests that canSerialize maintains state independence across calls with different settings.
     */
    @Test
    public void testStringCanSerializeMaintainsStateIndependence() {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settingsWithAdapter = new GsonRuntimeSettings();
        GsonRuntimeSettings settingsWithoutAdapter = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        // Add String class to one settings' type adapter class pool
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settingsWithAdapter.typeAdapterClassPool.addClass(stringClass);

        // Act - Call with adapter first, then without
        boolean resultWithAdapter = serializer.canSerialize(programClassPool, settingsWithAdapter);
        boolean resultWithoutAdapter1 = serializer.canSerialize(programClassPool, settingsWithoutAdapter);
        boolean resultWithAdapter2 = serializer.canSerialize(programClassPool, settingsWithAdapter);
        boolean resultWithoutAdapter2 = serializer.canSerialize(programClassPool, settingsWithoutAdapter);

        // Assert
        assertFalse(resultWithAdapter, "First call with adapter should return false");
        assertTrue(resultWithoutAdapter1, "First call without adapter should return true");
        assertFalse(resultWithAdapter2, "Second call with adapter should return false");
        assertTrue(resultWithoutAdapter2, "Second call without adapter should return true");
    }

    /**
     * Tests that canSerialize handles concurrent calls correctly from multiple threads.
     * This verifies thread-safety of the method.
     */
    @Test
    public void testStringCanSerializeIsThreadSafe() throws InterruptedException {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        ClassPool programClassPool = new ClassPool();

        int numThreads = 10;
        Thread[] threads = new Thread[numThreads];
        boolean[] results = new boolean[numThreads];

        // Act - Call canSerialize from multiple threads
        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = serializer.canSerialize(programClassPool, settings);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - All results should be true and consistent
        for (int i = 0; i < numThreads; i++) {
            assertTrue(results[i], "Result from thread " + i + " should be true");
        }
    }

    /**
     * Tests that canSerialize handles concurrent calls with different settings correctly.
     * This verifies thread-safety with varying conditions.
     */
    @Test
    public void testStringCanSerializeIsThreadSafeWithDifferentSettings() throws InterruptedException {
        // Arrange
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();
        ClassPool programClassPool = new ClassPool();

        int numThreads = 10;
        Thread[] threads = new Thread[numThreads];
        boolean[] results = new boolean[numThreads];
        GsonRuntimeSettings[] settingsArray = new GsonRuntimeSettings[numThreads];

        // Create settings - even indices have type adapters, odd indices don't
        for (int i = 0; i < numThreads; i++) {
            settingsArray[i] = new GsonRuntimeSettings();
            if (i % 2 == 0) {
                ProgramClass stringClass = new ClassBuilder(
                    VersionConstants.CLASS_VERSION_1_6,
                    0x0001, // public
                    ClassConstants.NAME_JAVA_LANG_STRING,
                    ClassConstants.NAME_JAVA_LANG_OBJECT
                ).getProgramClass();
                settingsArray[i].typeAdapterClassPool.addClass(stringClass);
            }
        }

        // Act - Call canSerialize from multiple threads with different settings
        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = serializer.canSerialize(programClassPool, settingsArray[index]);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - Results should match the settings (false for even indices, true for odd)
        for (int i = 0; i < numThreads; i++) {
            if (i % 2 == 0) {
                assertFalse(results[i], "Result from thread " + i + " (with adapter) should be false");
            } else {
                assertTrue(results[i], "Result from thread " + i + " (without adapter) should be true");
            }
        }
    }
}

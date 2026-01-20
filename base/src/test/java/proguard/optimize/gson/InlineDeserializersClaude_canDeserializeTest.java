package proguard.optimize.gson;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassConstants;
import proguard.classfile.ProgramClass;
import proguard.classfile.VersionConstants;
import proguard.classfile.editor.ClassBuilder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for canDeserialize methods in InlineDeserializers.
 * Tests:
 * - {@link InlineDeserializers.InlinePrimitiveIntegerDeserializer#canDeserialize(GsonRuntimeSettings)}
 * - {@link InlineDeserializers.InlineStringDeserializer#canDeserialize(GsonRuntimeSettings)}
 */
public class InlineDeserializersClaude_canDeserializeTest {

    /**
     * Tests that canDeserialize returns true when no custom type adapter for Integer is registered.
     * This is the common case where inline deserialization should be used.
     */
    @Test
    public void testCanDeserializeReturnsTrueWhenNoCustomTypeAdapter() {
        // Arrange
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        // settings.typeAdapterClassPool is initialized as empty by default

        // Act
        boolean result = deserializer.canDeserialize(settings);

        // Assert
        assertTrue(result, "canDeserialize should return true when no custom type adapter is registered for Integer");
    }

    /**
     * Tests that canDeserialize returns false when a custom type adapter for Integer is registered.
     * In this case, the custom type adapter should be used instead of inline deserialization.
     */
    @Test
    public void testCanDeserializeReturnsFalseWhenCustomTypeAdapterRegistered() {
        // Arrange
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

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
        boolean result = deserializer.canDeserialize(settings);

        // Assert
        assertFalse(result, "canDeserialize should return false when a custom type adapter is registered for Integer");
    }

    /**
     * Tests that canDeserialize is consistent when called multiple times with the same settings.
     */
    @Test
    public void testCanDeserializeIsConsistentWithSameSettings() {
        // Arrange
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Act
        boolean result1 = deserializer.canDeserialize(settings);
        boolean result2 = deserializer.canDeserialize(settings);
        boolean result3 = deserializer.canDeserialize(settings);

        // Assert
        assertEquals(result1, result2, "canDeserialize should return the same result when called multiple times");
        assertEquals(result2, result3, "canDeserialize should return the same result when called multiple times");
        assertTrue(result1, "canDeserialize should return true for empty typeAdapterClassPool");
    }

    /**
     * Tests that canDeserialize works correctly with different deserializer instances.
     * The behavior should depend only on the GsonRuntimeSettings, not on the deserializer instance.
     */
    @Test
    public void testCanDeserializeWorksWithDifferentDeserializerInstances() {
        // Arrange
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer1 =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer2 =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Act
        boolean result1 = deserializer1.canDeserialize(settings);
        boolean result2 = deserializer2.canDeserialize(settings);

        // Assert
        assertEquals(result1, result2, "Different deserializer instances should return the same result for the same settings");
        assertTrue(result1, "Both deserializers should return true for empty typeAdapterClassPool");
    }

    /**
     * Tests that canDeserialize works correctly when deserializer is created with different target types.
     * The target type (byte, short, int) should not affect the canDeserialize result, as it only checks
     * for custom Integer type adapters.
     */
    @Test
    public void testCanDeserializeWithDifferentTargetTypes() {
        // Arrange
        InlineDeserializers.InlinePrimitiveIntegerDeserializer intDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(int.class);
        InlineDeserializers.InlinePrimitiveIntegerDeserializer shortDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(short.class);
        InlineDeserializers.InlinePrimitiveIntegerDeserializer byteDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(byte.class);
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Act
        boolean intResult = intDeserializer.canDeserialize(settings);
        boolean shortResult = shortDeserializer.canDeserialize(settings);
        boolean byteResult = byteDeserializer.canDeserialize(settings);

        // Assert
        assertTrue(intResult, "int deserializer should return true for empty typeAdapterClassPool");
        assertTrue(shortResult, "short deserializer should return true for empty typeAdapterClassPool");
        assertTrue(byteResult, "byte deserializer should return true for empty typeAdapterClassPool");
        assertEquals(intResult, shortResult, "Target type should not affect canDeserialize result");
        assertEquals(shortResult, byteResult, "Target type should not affect canDeserialize result");
    }

    /**
     * Tests that canDeserialize correctly handles different GsonRuntimeSettings instances.
     * Each settings instance should be evaluated independently.
     */
    @Test
    public void testCanDeserializeWithDifferentSettingsInstances() {
        // Arrange
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();

        GsonRuntimeSettings settingsWithoutAdapter = new GsonRuntimeSettings();

        GsonRuntimeSettings settingsWithAdapter = new GsonRuntimeSettings();
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settingsWithAdapter.typeAdapterClassPool.addClass(integerClass);

        // Act
        boolean resultWithoutAdapter = deserializer.canDeserialize(settingsWithoutAdapter);
        boolean resultWithAdapter = deserializer.canDeserialize(settingsWithAdapter);

        // Assert
        assertTrue(resultWithoutAdapter, "Should return true when no custom type adapter is registered");
        assertFalse(resultWithAdapter, "Should return false when custom type adapter is registered");
    }

    /**
     * Tests that canDeserialize works correctly after adding and verifying a class in the pool.
     * This ensures the method properly queries the typeAdapterClassPool.
     */
    @Test
    public void testCanDeserializeAfterAddingClassToPool() {
        // Arrange
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Act & Assert - Before adding
        assertTrue(deserializer.canDeserialize(settings),
            "Should return true before adding Integer type adapter");

        // Add Integer type adapter
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(integerClass);

        // Act & Assert - After adding
        assertFalse(deserializer.canDeserialize(settings),
            "Should return false after adding Integer type adapter");
    }

    /**
     * Tests that an empty ClassPool results in canDeserialize returning true.
     * This verifies the default behavior when no type adapters are registered.
     */
    @Test
    public void testCanDeserializeWithEmptyClassPool() {
        // Arrange
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Verify the pool is empty
        assertEquals(0, settings.typeAdapterClassPool.size(), "typeAdapterClassPool should be empty");

        // Act
        boolean result = deserializer.canDeserialize(settings);

        // Assert
        assertTrue(result, "canDeserialize should return true when typeAdapterClassPool is empty");
    }

    // ============================================================================
    // Tests for InlineStringDeserializer.canDeserialize
    // ============================================================================

    /**
     * Tests that InlineStringDeserializer.canDeserialize returns true when no custom type adapter for String is registered.
     * This is the common case where inline deserialization should be used.
     */
    @Test
    public void testStringDeserializerCanDeserializeReturnsTrueWhenNoCustomTypeAdapter() {
        // Arrange
        InlineDeserializers.InlineStringDeserializer deserializer =
            new InlineDeserializers.InlineStringDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        // settings.typeAdapterClassPool is initialized as empty by default

        // Act
        boolean result = deserializer.canDeserialize(settings);

        // Assert
        assertTrue(result, "canDeserialize should return true when no custom type adapter is registered for String");
    }

    /**
     * Tests that InlineStringDeserializer.canDeserialize returns false when a custom type adapter for String is registered.
     * In this case, the custom type adapter should be used instead of inline deserialization.
     */
    @Test
    public void testStringDeserializerCanDeserializeReturnsFalseWhenCustomTypeAdapterRegistered() {
        // Arrange
        InlineDeserializers.InlineStringDeserializer deserializer =
            new InlineDeserializers.InlineStringDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

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
        boolean result = deserializer.canDeserialize(settings);

        // Assert
        assertFalse(result, "canDeserialize should return false when a custom type adapter is registered for String");
    }

    /**
     * Tests that InlineStringDeserializer.canDeserialize is consistent when called multiple times with the same settings.
     */
    @Test
    public void testStringDeserializerCanDeserializeIsConsistentWithSameSettings() {
        // Arrange
        InlineDeserializers.InlineStringDeserializer deserializer =
            new InlineDeserializers.InlineStringDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Act
        boolean result1 = deserializer.canDeserialize(settings);
        boolean result2 = deserializer.canDeserialize(settings);
        boolean result3 = deserializer.canDeserialize(settings);

        // Assert
        assertEquals(result1, result2, "canDeserialize should return the same result when called multiple times");
        assertEquals(result2, result3, "canDeserialize should return the same result when called multiple times");
        assertTrue(result1, "canDeserialize should return true for empty typeAdapterClassPool");
    }

    /**
     * Tests that InlineStringDeserializer.canDeserialize works correctly with different deserializer instances.
     * The behavior should depend only on the GsonRuntimeSettings, not on the deserializer instance.
     */
    @Test
    public void testStringDeserializerCanDeserializeWorksWithDifferentDeserializerInstances() {
        // Arrange
        InlineDeserializers.InlineStringDeserializer deserializer1 =
            new InlineDeserializers.InlineStringDeserializer();
        InlineDeserializers.InlineStringDeserializer deserializer2 =
            new InlineDeserializers.InlineStringDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Act
        boolean result1 = deserializer1.canDeserialize(settings);
        boolean result2 = deserializer2.canDeserialize(settings);

        // Assert
        assertEquals(result1, result2, "Different deserializer instances should return the same result for the same settings");
        assertTrue(result1, "Both deserializers should return true for empty typeAdapterClassPool");
    }

    /**
     * Tests that InlineStringDeserializer.canDeserialize correctly handles different GsonRuntimeSettings instances.
     * Each settings instance should be evaluated independently.
     */
    @Test
    public void testStringDeserializerCanDeserializeWithDifferentSettingsInstances() {
        // Arrange
        InlineDeserializers.InlineStringDeserializer deserializer =
            new InlineDeserializers.InlineStringDeserializer();

        GsonRuntimeSettings settingsWithoutAdapter = new GsonRuntimeSettings();

        GsonRuntimeSettings settingsWithAdapter = new GsonRuntimeSettings();
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settingsWithAdapter.typeAdapterClassPool.addClass(stringClass);

        // Act
        boolean resultWithoutAdapter = deserializer.canDeserialize(settingsWithoutAdapter);
        boolean resultWithAdapter = deserializer.canDeserialize(settingsWithAdapter);

        // Assert
        assertTrue(resultWithoutAdapter, "Should return true when no custom type adapter is registered");
        assertFalse(resultWithAdapter, "Should return false when custom type adapter is registered");
    }

    /**
     * Tests that InlineStringDeserializer.canDeserialize works correctly after adding and verifying a class in the pool.
     * This ensures the method properly queries the typeAdapterClassPool.
     */
    @Test
    public void testStringDeserializerCanDeserializeAfterAddingClassToPool() {
        // Arrange
        InlineDeserializers.InlineStringDeserializer deserializer =
            new InlineDeserializers.InlineStringDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Act & Assert - Before adding
        assertTrue(deserializer.canDeserialize(settings),
            "Should return true before adding String type adapter");

        // Add String type adapter
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settings.typeAdapterClassPool.addClass(stringClass);

        // Act & Assert - After adding
        assertFalse(deserializer.canDeserialize(settings),
            "Should return false after adding String type adapter");
    }

    /**
     * Tests that InlineStringDeserializer.canDeserialize returns true with an empty ClassPool.
     * This verifies the default behavior when no type adapters are registered.
     */
    @Test
    public void testStringDeserializerCanDeserializeWithEmptyClassPool() {
        // Arrange
        InlineDeserializers.InlineStringDeserializer deserializer =
            new InlineDeserializers.InlineStringDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Verify the pool is empty
        assertEquals(0, settings.typeAdapterClassPool.size(), "typeAdapterClassPool should be empty");

        // Act
        boolean result = deserializer.canDeserialize(settings);

        // Assert
        assertTrue(result, "canDeserialize should return true when typeAdapterClassPool is empty");
    }

    /**
     * Tests that InlineStringDeserializer.canDeserialize returns true when a type adapter for Integer
     * (not String) is registered. The method should only check for String type adapters.
     */
    @Test
    public void testStringDeserializerCanDeserializeReturnsTrueWhenIntegerAdapterRegistered() {
        // Arrange
        InlineDeserializers.InlineStringDeserializer deserializer =
            new InlineDeserializers.InlineStringDeserializer();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Create a ProgramClass representing java.lang.Integer (not String)
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001, // public
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();

        // Add the Integer class to the type adapter class pool
        settings.typeAdapterClassPool.addClass(integerClass);

        // Act
        boolean result = deserializer.canDeserialize(settings);

        // Assert
        assertTrue(result, "canDeserialize should return true when type adapter for Integer (not String) is registered");
    }

    /**
     * Tests that InlineStringDeserializer and InlinePrimitiveIntegerDeserializer behave independently.
     * Having a String type adapter should not affect Integer deserializer and vice versa.
     */
    @Test
    public void testStringAndIntegerDeserializersAreIndependent() {
        // Arrange
        InlineDeserializers.InlineStringDeserializer stringDeserializer =
            new InlineDeserializers.InlineStringDeserializer();
        InlineDeserializers.InlinePrimitiveIntegerDeserializer integerDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();

        GsonRuntimeSettings settingsWithStringAdapter = new GsonRuntimeSettings();
        ProgramClass stringClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_STRING,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settingsWithStringAdapter.typeAdapterClassPool.addClass(stringClass);

        GsonRuntimeSettings settingsWithIntegerAdapter = new GsonRuntimeSettings();
        ProgramClass integerClass = new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_6,
            0x0001,
            ClassConstants.NAME_JAVA_LANG_INTEGER,
            ClassConstants.NAME_JAVA_LANG_OBJECT
        ).getProgramClass();
        settingsWithIntegerAdapter.typeAdapterClassPool.addClass(integerClass);

        // Act & Assert
        // String deserializer should be affected by String adapter only
        assertFalse(stringDeserializer.canDeserialize(settingsWithStringAdapter),
            "String deserializer should return false with String adapter");
        assertTrue(stringDeserializer.canDeserialize(settingsWithIntegerAdapter),
            "String deserializer should return true with Integer adapter");

        // Integer deserializer should be affected by Integer adapter only
        assertFalse(integerDeserializer.canDeserialize(settingsWithIntegerAdapter),
            "Integer deserializer should return false with Integer adapter");
        assertTrue(integerDeserializer.canDeserialize(settingsWithStringAdapter),
            "Integer deserializer should return true with String adapter");
    }
}

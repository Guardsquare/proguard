package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GsonRuntimeSettings default constructor.
 * Tests the implicit no-argument constructor <init>.()V
 */
public class GsonRuntimeSettingsClaude_constructorTest {

    /**
     * Tests that the default constructor creates a non-null GsonRuntimeSettings instance.
     */
    @Test
    public void testConstructorCreatesNonNullInstance() {
        // Act
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Assert
        assertNotNull(settings, "GsonRuntimeSettings instance should not be null");
    }

    /**
     * Tests that the default constructor initializes all boolean fields to false.
     */
    @Test
    public void testConstructorInitializesBooleanFieldsToFalse() {
        // Act
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Assert
        assertFalse(settings.setVersion, "setVersion should be false after construction");
        assertFalse(settings.excludeFieldsWithModifiers, "excludeFieldsWithModifiers should be false after construction");
        assertFalse(settings.generateNonExecutableJson, "generateNonExecutableJson should be false after construction");
        assertFalse(settings.excludeFieldsWithoutExposeAnnotation, "excludeFieldsWithoutExposeAnnotation should be false after construction");
        assertFalse(settings.serializeNulls, "serializeNulls should be false after construction");
        assertFalse(settings.disableInnerClassSerialization, "disableInnerClassSerialization should be false after construction");
        assertFalse(settings.setLongSerializationPolicy, "setLongSerializationPolicy should be false after construction");
        assertFalse(settings.setFieldNamingPolicy, "setFieldNamingPolicy should be false after construction");
        assertFalse(settings.setFieldNamingStrategy, "setFieldNamingStrategy should be false after construction");
        assertFalse(settings.setExclusionStrategies, "setExclusionStrategies should be false after construction");
        assertFalse(settings.addSerializationExclusionStrategy, "addSerializationExclusionStrategy should be false after construction");
        assertFalse(settings.addDeserializationExclusionStrategy, "addDeserializationExclusionStrategy should be false after construction");
        assertFalse(settings.registerTypeAdapterFactory, "registerTypeAdapterFactory should be false after construction");
        assertFalse(settings.serializeSpecialFloatingPointValues, "serializeSpecialFloatingPointValues should be false after construction");
    }

    /**
     * Tests that the default constructor initializes instanceCreatorClassPool to a non-null ClassPool.
     */
    @Test
    public void testConstructorInitializesInstanceCreatorClassPool() {
        // Act
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Assert
        assertNotNull(settings.instanceCreatorClassPool, "instanceCreatorClassPool should not be null after construction");
    }

    /**
     * Tests that the default constructor initializes typeAdapterClassPool to a non-null ClassPool.
     */
    @Test
    public void testConstructorInitializesTypeAdapterClassPool() {
        // Act
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Assert
        assertNotNull(settings.typeAdapterClassPool, "typeAdapterClassPool should not be null after construction");
    }

    /**
     * Tests that the initialized ClassPool instances are empty (contain no classes initially).
     */
    @Test
    public void testConstructorInitializesEmptyClassPools() {
        // Act
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Assert
        assertEquals(0, settings.instanceCreatorClassPool.size(), "instanceCreatorClassPool should be empty after construction");
        assertEquals(0, settings.typeAdapterClassPool.size(), "typeAdapterClassPool should be empty after construction");
    }

    /**
     * Tests that multiple GsonRuntimeSettings instances can be created independently.
     */
    @Test
    public void testMultipleInstancesCreation() {
        // Act
        GsonRuntimeSettings settings1 = new GsonRuntimeSettings();
        GsonRuntimeSettings settings2 = new GsonRuntimeSettings();

        // Assert
        assertNotNull(settings1, "First GsonRuntimeSettings instance should not be null");
        assertNotNull(settings2, "Second GsonRuntimeSettings instance should not be null");
        assertNotSame(settings1, settings2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that each GsonRuntimeSettings instance has its own independent ClassPool instances.
     */
    @Test
    public void testEachInstanceHasIndependentClassPools() {
        // Act
        GsonRuntimeSettings settings1 = new GsonRuntimeSettings();
        GsonRuntimeSettings settings2 = new GsonRuntimeSettings();

        // Assert
        assertNotSame(settings1.instanceCreatorClassPool, settings2.instanceCreatorClassPool,
            "Each instance should have its own instanceCreatorClassPool");
        assertNotSame(settings1.typeAdapterClassPool, settings2.typeAdapterClassPool,
            "Each instance should have its own typeAdapterClassPool");
    }

    /**
     * Tests that a newly constructed GsonRuntimeSettings has its fields accessible.
     * This verifies the object is properly initialized and ready for subsequent operations.
     */
    @Test
    public void testConstructorCreatesAccessibleFields() {
        // Act
        GsonRuntimeSettings settings = new GsonRuntimeSettings();

        // Assert - Verify we can access all public fields without any exceptions
        assertDoesNotThrow(() -> {
            boolean b1 = settings.setVersion;
            boolean b2 = settings.excludeFieldsWithModifiers;
            boolean b3 = settings.generateNonExecutableJson;
            boolean b4 = settings.excludeFieldsWithoutExposeAnnotation;
            boolean b5 = settings.serializeNulls;
            boolean b6 = settings.disableInnerClassSerialization;
            boolean b7 = settings.setLongSerializationPolicy;
            boolean b8 = settings.setFieldNamingPolicy;
            boolean b9 = settings.setFieldNamingStrategy;
            boolean b10 = settings.setExclusionStrategies;
            boolean b11 = settings.addSerializationExclusionStrategy;
            boolean b12 = settings.addDeserializationExclusionStrategy;
            boolean b13 = settings.registerTypeAdapterFactory;
            boolean b14 = settings.serializeSpecialFloatingPointValues;
            Object pool1 = settings.instanceCreatorClassPool;
            Object pool2 = settings.typeAdapterClassPool;
        }, "Should be able to access all public fields after construction");
    }
}

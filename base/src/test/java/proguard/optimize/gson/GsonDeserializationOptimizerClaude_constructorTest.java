package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.io.ExtraDataEntryNameMap;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GsonDeserializationOptimizer constructor.
 * Tests the constructor
 * (Lproguard/classfile/ClassPool;Lproguard/classfile/ClassPool;Lproguard/optimize/gson/GsonRuntimeSettings;Lproguard/optimize/gson/OptimizedJsonInfo;ZLproguard/io/ExtraDataEntryNameMap;)V
 */
public class GsonDeserializationOptimizerClaude_constructorTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private GsonRuntimeSettings gsonRuntimeSettings;
    private OptimizedJsonInfo deserializationInfo;
    private ExtraDataEntryNameMap extraDataEntryNameMap;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        gsonRuntimeSettings = new GsonRuntimeSettings();
        deserializationInfo = new OptimizedJsonInfo();
        extraDataEntryNameMap = new ExtraDataEntryNameMap();
    }

    /**
     * Tests that the constructor successfully creates a non-null instance with all valid parameters.
     */
    @Test
    public void testConstructor_createsNonNullInstance() {
        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor properly implements ClassVisitor interface.
     */
    @Test
    public void testConstructor_implementsClassVisitor() {
        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertTrue(optimizer instanceof proguard.classfile.visitor.ClassVisitor,
            "GsonDeserializationOptimizer should implement ClassVisitor");
    }

    /**
     * Tests that the constructor properly implements MemberVisitor interface.
     */
    @Test
    public void testConstructor_implementsMemberVisitor() {
        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertTrue(optimizer instanceof proguard.classfile.visitor.MemberVisitor,
            "GsonDeserializationOptimizer should implement MemberVisitor");
    }

    /**
     * Tests that the constructor properly implements AttributeVisitor interface.
     */
    @Test
    public void testConstructor_implementsAttributeVisitor() {
        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertTrue(optimizer instanceof proguard.classfile.attribute.visitor.AttributeVisitor,
            "GsonDeserializationOptimizer should implement AttributeVisitor");
    }

    /**
     * Tests that the constructor works with optimizeConservatively set to true.
     */
    @Test
    public void testConstructor_withOptimizeConservativelyTrue() {
        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            deserializationInfo,
            true,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with optimizeConservatively=true");
    }

    /**
     * Tests that the constructor works with optimizeConservatively set to false.
     */
    @Test
    public void testConstructor_withOptimizeConservativelyFalse() {
        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with optimizeConservatively=false");
    }

    /**
     * Tests that the constructor correctly sets supportExposeAnnotation
     * when gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation is true.
     * Reflection is necessary here because supportExposeAnnotation is a private field
     * and there are no public getters or other observable behavior to test its value
     * without actually using the optimizer on program classes.
     */
    @Test
    public void testConstructor_setsSupportExposeAnnotationTrue() throws Exception {
        // Arrange
        gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;

        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        Field field = GsonDeserializationOptimizer.class.getDeclaredField("supportExposeAnnotation");
        field.setAccessible(true);
        boolean supportExposeAnnotation = (Boolean) field.get(optimizer);
        assertTrue(supportExposeAnnotation,
            "supportExposeAnnotation should be true when excludeFieldsWithoutExposeAnnotation is true");
    }

    /**
     * Tests that the constructor correctly sets supportExposeAnnotation
     * when gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation is false.
     * Reflection is necessary here because supportExposeAnnotation is a private field
     * and there are no public getters or other observable behavior to test its value
     * without actually using the optimizer on program classes.
     */
    @Test
    public void testConstructor_setsSupportExposeAnnotationFalse() throws Exception {
        // Arrange
        gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = false;

        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        Field field = GsonDeserializationOptimizer.class.getDeclaredField("supportExposeAnnotation");
        field.setAccessible(true);
        boolean supportExposeAnnotation = (Boolean) field.get(optimizer);
        assertFalse(supportExposeAnnotation,
            "supportExposeAnnotation should be false when excludeFieldsWithoutExposeAnnotation is false");
    }

    /**
     * Tests that the constructor works with empty ClassPool instances.
     */
    @Test
    public void testConstructor_withEmptyClassPools() {
        // Arrange
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();

        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            emptyProgramPool,
            emptyLibraryPool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with empty class pools");
    }

    /**
     * Tests that the constructor works with the same ClassPool for both program and library.
     */
    @Test
    public void testConstructor_withSameClassPoolForBothParameters() {
        // Arrange
        ClassPool samePool = new ClassPool();

        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            samePool,
            samePool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with same ClassPool for both parameters");
    }

    /**
     * Tests that the constructor works with a fresh GsonRuntimeSettings instance.
     */
    @Test
    public void testConstructor_withFreshGsonRuntimeSettings() {
        // Arrange
        GsonRuntimeSettings freshSettings = new GsonRuntimeSettings();

        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            freshSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with fresh GsonRuntimeSettings");
    }

    /**
     * Tests that the constructor works with a GsonRuntimeSettings that has various flags set.
     */
    @Test
    public void testConstructor_withConfiguredGsonRuntimeSettings() {
        // Arrange
        GsonRuntimeSettings configuredSettings = new GsonRuntimeSettings();
        configuredSettings.setVersion = true;
        configuredSettings.excludeFieldsWithModifiers = true;
        configuredSettings.serializeNulls = true;
        configuredSettings.registerTypeAdapterFactory = true;

        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            configuredSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with configured GsonRuntimeSettings");
    }

    /**
     * Tests that the constructor works with a fresh OptimizedJsonInfo instance.
     */
    @Test
    public void testConstructor_withFreshOptimizedJsonInfo() {
        // Arrange
        OptimizedJsonInfo freshInfo = new OptimizedJsonInfo();

        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            freshInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with fresh OptimizedJsonInfo");
    }

    /**
     * Tests that the constructor works with an OptimizedJsonInfo that has data populated.
     */
    @Test
    public void testConstructor_withPopulatedOptimizedJsonInfo() {
        // Arrange
        OptimizedJsonInfo populatedInfo = new OptimizedJsonInfo();
        populatedInfo.classIndices.put("com/example/TestClass", 0);
        populatedInfo.jsonFieldIndices.put("testField", 0);
        OptimizedJsonInfo.ClassJsonInfo classJsonInfo = new OptimizedJsonInfo.ClassJsonInfo();
        classJsonInfo.javaToJsonFieldNames.put("field1", new String[]{"field1"});
        classJsonInfo.exposedJavaFieldNames.add("field1");
        populatedInfo.classJsonInfos.put("com/example/TestClass", classJsonInfo);

        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            populatedInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with populated OptimizedJsonInfo");
    }

    /**
     * Tests that the constructor works with a fresh ExtraDataEntryNameMap instance.
     */
    @Test
    public void testConstructor_withFreshExtraDataEntryNameMap() {
        // Arrange
        ExtraDataEntryNameMap freshMap = new ExtraDataEntryNameMap();

        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            freshMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with fresh ExtraDataEntryNameMap");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_createIndependently() {
        // Act
        GsonDeserializationOptimizer optimizer1 = new GsonDeserializationOptimizer(
            programClassPool, libraryClassPool, gsonRuntimeSettings,
            deserializationInfo, false, extraDataEntryNameMap);

        GsonDeserializationOptimizer optimizer2 = new GsonDeserializationOptimizer(
            programClassPool, libraryClassPool, gsonRuntimeSettings,
            deserializationInfo, true, extraDataEntryNameMap);

        GsonDeserializationOptimizer optimizer3 = new GsonDeserializationOptimizer(
            new ClassPool(), new ClassPool(), new GsonRuntimeSettings(),
            new OptimizedJsonInfo(), false, new ExtraDataEntryNameMap());

        // Assert
        assertNotNull(optimizer1, "First instance should be created");
        assertNotNull(optimizer2, "Second instance should be created");
        assertNotNull(optimizer3, "Third instance should be created");
        assertNotSame(optimizer1, optimizer2, "Instances should be distinct");
        assertNotSame(optimizer2, optimizer3, "Instances should be distinct");
        assertNotSame(optimizer1, optimizer3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid inputs.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        ), "Constructor should not throw any exception with valid inputs");
    }

    /**
     * Tests that the constructor can be called multiple times consecutively.
     */
    @Test
    public void testConstructor_consecutiveCalls_allSucceed() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
                new ClassPool(),
                new ClassPool(),
                new GsonRuntimeSettings(),
                new OptimizedJsonInfo(),
                i % 2 == 0,
                new ExtraDataEntryNameMap()
            );
            assertNotNull(optimizer, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests constructor with various combinations of optimizeConservatively flag.
     */
    @Test
    public void testConstructor_withDifferentOptimizeConservativelyValues() {
        // Act
        GsonDeserializationOptimizer optimizerTrue = new GsonDeserializationOptimizer(
            programClassPool, libraryClassPool, gsonRuntimeSettings,
            deserializationInfo, true, extraDataEntryNameMap);

        GsonDeserializationOptimizer optimizerFalse = new GsonDeserializationOptimizer(
            programClassPool, libraryClassPool, gsonRuntimeSettings,
            deserializationInfo, false, extraDataEntryNameMap);

        // Assert
        assertNotNull(optimizerTrue, "Constructor should work with optimizeConservatively=true");
        assertNotNull(optimizerFalse, "Constructor should work with optimizeConservatively=false");
        assertNotSame(optimizerTrue, optimizerFalse, "Different instances should be created");
    }

    /**
     * Tests that the constructor correctly initializes the optimizer to be ready for visiting classes.
     */
    @Test
    public void testConstructor_createsReadyToUseVisitor() {
        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert - Should be able to call visitor methods without error
        assertDoesNotThrow(() -> optimizer.visitAnyClass(null),
            "Should be able to call visitAnyClass after construction");
    }

    /**
     * Tests that the constructor works when GsonRuntimeSettings has all boolean flags set to true.
     */
    @Test
    public void testConstructor_withAllGsonRuntimeSettingsFlagsTrue() {
        // Arrange
        GsonRuntimeSettings allTrueSettings = new GsonRuntimeSettings();
        allTrueSettings.setVersion = true;
        allTrueSettings.excludeFieldsWithModifiers = true;
        allTrueSettings.generateNonExecutableJson = true;
        allTrueSettings.excludeFieldsWithoutExposeAnnotation = true;
        allTrueSettings.serializeNulls = true;
        allTrueSettings.disableInnerClassSerialization = true;
        allTrueSettings.setLongSerializationPolicy = true;
        allTrueSettings.setFieldNamingPolicy = true;
        allTrueSettings.setFieldNamingStrategy = true;
        allTrueSettings.setExclusionStrategies = true;
        allTrueSettings.addSerializationExclusionStrategy = true;
        allTrueSettings.addDeserializationExclusionStrategy = true;
        allTrueSettings.registerTypeAdapterFactory = true;
        allTrueSettings.serializeSpecialFloatingPointValues = true;

        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            allTrueSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with all GsonRuntimeSettings flags set to true");
    }

    /**
     * Tests that the constructor works when GsonRuntimeSettings has all boolean flags set to false.
     */
    @Test
    public void testConstructor_withAllGsonRuntimeSettingsFlagsFalse() {
        // Arrange
        GsonRuntimeSettings allFalseSettings = new GsonRuntimeSettings();
        allFalseSettings.setVersion = false;
        allFalseSettings.excludeFieldsWithModifiers = false;
        allFalseSettings.generateNonExecutableJson = false;
        allFalseSettings.excludeFieldsWithoutExposeAnnotation = false;
        allFalseSettings.serializeNulls = false;
        allFalseSettings.disableInnerClassSerialization = false;
        allFalseSettings.setLongSerializationPolicy = false;
        allFalseSettings.setFieldNamingPolicy = false;
        allFalseSettings.setFieldNamingStrategy = false;
        allFalseSettings.setExclusionStrategies = false;
        allFalseSettings.addSerializationExclusionStrategy = false;
        allFalseSettings.addDeserializationExclusionStrategy = false;
        allFalseSettings.registerTypeAdapterFactory = false;
        allFalseSettings.serializeSpecialFloatingPointValues = false;

        // Act
        GsonDeserializationOptimizer optimizer = new GsonDeserializationOptimizer(
            programClassPool,
            libraryClassPool,
            allFalseSettings,
            deserializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with all GsonRuntimeSettings flags set to false");
    }

    /**
     * Tests that different optimizer instances can be created with different configurations.
     */
    @Test
    public void testConstructor_withDifferentConfigurations_createsIndependentInstances() {
        // Arrange
        GsonRuntimeSettings settings1 = new GsonRuntimeSettings();
        settings1.excludeFieldsWithoutExposeAnnotation = true;

        GsonRuntimeSettings settings2 = new GsonRuntimeSettings();
        settings2.excludeFieldsWithoutExposeAnnotation = false;

        // Act
        GsonDeserializationOptimizer optimizer1 = new GsonDeserializationOptimizer(
            programClassPool, libraryClassPool, settings1,
            deserializationInfo, true, extraDataEntryNameMap);

        GsonDeserializationOptimizer optimizer2 = new GsonDeserializationOptimizer(
            programClassPool, libraryClassPool, settings2,
            deserializationInfo, false, extraDataEntryNameMap);

        // Assert
        assertNotNull(optimizer1, "First optimizer should be created");
        assertNotNull(optimizer2, "Second optimizer should be created");
        assertNotSame(optimizer1, optimizer2, "Different configurations should create distinct instances");
    }
}

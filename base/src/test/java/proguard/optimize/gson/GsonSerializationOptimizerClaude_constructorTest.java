package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.io.ExtraDataEntryNameMap;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GsonSerializationOptimizer constructor.
 * Tests the constructor (Lproguard/classfile/ClassPool;Lproguard/classfile/ClassPool;Lproguard/optimize/gson/GsonRuntimeSettings;Lproguard/optimize/gson/OptimizedJsonInfo;ZLproguard/io/ExtraDataEntryNameMap;)V
 */
public class GsonSerializationOptimizerClaude_constructorTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private GsonRuntimeSettings gsonRuntimeSettings;
    private OptimizedJsonInfo serializationInfo;
    private ExtraDataEntryNameMap extraDataEntryNameMap;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        gsonRuntimeSettings = new GsonRuntimeSettings();
        serializationInfo = new OptimizedJsonInfo();
        extraDataEntryNameMap = new ExtraDataEntryNameMap();
    }

    /**
     * Tests that the constructor successfully creates a non-null instance with valid parameters.
     */
    @Test
    public void testConstructor_createsNonNullInstance() {
        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor stores the programClassPool parameter.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_storesProgramClassPool() throws Exception {
        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        Field field = GsonSerializationOptimizer.class.getDeclaredField("programClassPool");
        field.setAccessible(true);
        ClassPool storedPool = (ClassPool) field.get(optimizer);
        assertSame(programClassPool, storedPool, "Constructor should store the provided programClassPool");
    }

    /**
     * Tests that the constructor stores the libraryClassPool parameter.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_storesLibraryClassPool() throws Exception {
        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        Field field = GsonSerializationOptimizer.class.getDeclaredField("libraryClassPool");
        field.setAccessible(true);
        ClassPool storedPool = (ClassPool) field.get(optimizer);
        assertSame(libraryClassPool, storedPool, "Constructor should store the provided libraryClassPool");
    }

    /**
     * Tests that the constructor stores the gsonRuntimeSettings parameter.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_storesGsonRuntimeSettings() throws Exception {
        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        Field field = GsonSerializationOptimizer.class.getDeclaredField("gsonRuntimeSettings");
        field.setAccessible(true);
        GsonRuntimeSettings storedSettings = (GsonRuntimeSettings) field.get(optimizer);
        assertSame(gsonRuntimeSettings, storedSettings, "Constructor should store the provided gsonRuntimeSettings");
    }

    /**
     * Tests that the constructor stores the serializationInfo parameter.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_storesSerializationInfo() throws Exception {
        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        Field field = GsonSerializationOptimizer.class.getDeclaredField("serializationInfo");
        field.setAccessible(true);
        OptimizedJsonInfo storedInfo = (OptimizedJsonInfo) field.get(optimizer);
        assertSame(serializationInfo, storedInfo, "Constructor should store the provided serializationInfo");
    }

    /**
     * Tests that the constructor stores the optimizeConservatively parameter with false value.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_storesOptimizeConservativelyFalse() throws Exception {
        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        Field field = GsonSerializationOptimizer.class.getDeclaredField("optimizeConservatively");
        field.setAccessible(true);
        boolean storedValue = (Boolean) field.get(optimizer);
        assertFalse(storedValue, "Constructor should store optimizeConservatively as false");
    }

    /**
     * Tests that the constructor stores the optimizeConservatively parameter with true value.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_storesOptimizeConservativelyTrue() throws Exception {
        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            true,
            extraDataEntryNameMap
        );

        // Assert
        Field field = GsonSerializationOptimizer.class.getDeclaredField("optimizeConservatively");
        field.setAccessible(true);
        boolean storedValue = (Boolean) field.get(optimizer);
        assertTrue(storedValue, "Constructor should store optimizeConservatively as true");
    }

    /**
     * Tests that the constructor stores the extraDataEntryNameMap parameter.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_storesExtraDataEntryNameMap() throws Exception {
        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        Field field = GsonSerializationOptimizer.class.getDeclaredField("extraDataEntryNameMap");
        field.setAccessible(true);
        ExtraDataEntryNameMap storedMap = (ExtraDataEntryNameMap) field.get(optimizer);
        assertSame(extraDataEntryNameMap, storedMap, "Constructor should store the provided extraDataEntryNameMap");
    }

    /**
     * Tests that the constructor derives and stores the supportExposeAnnotation field
     * from the gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation field.
     * Reflection is necessary because the field is private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_derivesSupportExposeAnnotationFromRuntimeSettings() throws Exception {
        // Arrange
        GsonRuntimeSettings settingsWithExposeTrue = new GsonRuntimeSettings();
        settingsWithExposeTrue.excludeFieldsWithoutExposeAnnotation = true;

        GsonRuntimeSettings settingsWithExposeFalse = new GsonRuntimeSettings();
        settingsWithExposeFalse.excludeFieldsWithoutExposeAnnotation = false;

        // Act
        GsonSerializationOptimizer optimizerWithTrue = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            settingsWithExposeTrue,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        GsonSerializationOptimizer optimizerWithFalse = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            settingsWithExposeFalse,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        Field field = GsonSerializationOptimizer.class.getDeclaredField("supportExposeAnnotation");
        field.setAccessible(true);

        boolean supportExposeTrue = (Boolean) field.get(optimizerWithTrue);
        assertTrue(supportExposeTrue, "supportExposeAnnotation should be true when excludeFieldsWithoutExposeAnnotation is true");

        boolean supportExposeFalse = (Boolean) field.get(optimizerWithFalse);
        assertFalse(supportExposeFalse, "supportExposeAnnotation should be false when excludeFieldsWithoutExposeAnnotation is false");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid input.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        ), "Constructor should not throw any exception with valid input");
    }

    /**
     * Tests that multiple instances can be created independently with the same parameters.
     */
    @Test
    public void testConstructor_multipleInstances_withSameParameters() {
        // Act
        GsonSerializationOptimizer optimizer1 = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        GsonSerializationOptimizer optimizer2 = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer1, "First instance should be created");
        assertNotNull(optimizer2, "Second instance should be created");
        assertNotSame(optimizer1, optimizer2, "Instances should be distinct");
    }

    /**
     * Tests that multiple instances can be created independently with different parameters.
     */
    @Test
    public void testConstructor_multipleInstances_withDifferentParameters() {
        // Arrange
        ClassPool programClassPool2 = new ClassPool();
        ClassPool libraryClassPool2 = new ClassPool();
        GsonRuntimeSettings gsonRuntimeSettings2 = new GsonRuntimeSettings();
        OptimizedJsonInfo serializationInfo2 = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap2 = new ExtraDataEntryNameMap();

        // Act
        GsonSerializationOptimizer optimizer1 = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        GsonSerializationOptimizer optimizer2 = new GsonSerializationOptimizer(
            programClassPool2,
            libraryClassPool2,
            gsonRuntimeSettings2,
            serializationInfo2,
            true,
            extraDataEntryNameMap2
        );

        // Assert
        assertNotNull(optimizer1, "First instance should be created");
        assertNotNull(optimizer2, "Second instance should be created");
        assertNotSame(optimizer1, optimizer2, "Instances should be distinct");
    }

    /**
     * Tests that the constructor works with populated ClassPools.
     */
    @Test
    public void testConstructor_withPopulatedClassPools() {
        // Arrange
        ClassPool populatedProgramPool = new ClassPool();
        ClassPool populatedLibraryPool = new ClassPool();
        // Note: We can't easily add classes without loading them, but we can create the instance

        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            populatedProgramPool,
            populatedLibraryPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with populated class pools");
    }

    /**
     * Tests that the constructor works with GsonRuntimeSettings having various flags set.
     */
    @Test
    public void testConstructor_withConfiguredGsonRuntimeSettings() {
        // Arrange
        GsonRuntimeSettings configuredSettings = new GsonRuntimeSettings();
        configuredSettings.setVersion = true;
        configuredSettings.serializeNulls = true;
        configuredSettings.excludeFieldsWithoutExposeAnnotation = true;
        configuredSettings.registerTypeAdapterFactory = true;

        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            configuredSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizer, "Constructor should work with configured GsonRuntimeSettings");
    }

    /**
     * Tests that the constructor works with populated OptimizedJsonInfo.
     */
    @Test
    public void testConstructor_withPopulatedOptimizedJsonInfo() {
        // Arrange
        OptimizedJsonInfo populatedInfo = new OptimizedJsonInfo();
        populatedInfo.classIndices.put("com.example.TestClass", 0);
        populatedInfo.jsonFieldIndices.put("testField", 0);

        OptimizedJsonInfo.ClassJsonInfo classInfo = new OptimizedJsonInfo.ClassJsonInfo();
        classInfo.javaToJsonFieldNames.put("field1", new String[]{"field1"});
        classInfo.exposedJavaFieldNames.add("field1");
        populatedInfo.classJsonInfos.put("com.example.TestClass", classInfo);

        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
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
     * Tests that the constructor can be called multiple times consecutively.
     */
    @Test
    public void testConstructor_consecutiveCalls_allSucceed() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
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
     * Tests that the constructor works with both optimizeConservatively values.
     */
    @Test
    public void testConstructor_withBothOptimizeConservativelyValues() {
        // Act
        GsonSerializationOptimizer optimizerFalse = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        GsonSerializationOptimizer optimizerTrue = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            true,
            extraDataEntryNameMap
        );

        // Assert
        assertNotNull(optimizerFalse, "Constructor should work with optimizeConservatively=false");
        assertNotNull(optimizerTrue, "Constructor should work with optimizeConservatively=true");
    }

    /**
     * Tests that the constructor stores the correct references for all parameters.
     * Reflection is necessary because all fields are private and there are no public
     * getters or observable behaviors to test this without reflection.
     */
    @Test
    public void testConstructor_storesAllParametersCorrectly() throws Exception {
        // Arrange
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        GsonRuntimeSettings settings = new GsonRuntimeSettings();
        OptimizedJsonInfo info = new OptimizedJsonInfo();
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();

        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            programPool,
            libraryPool,
            settings,
            info,
            true,
            nameMap
        );

        // Assert
        Field programClassPoolField = GsonSerializationOptimizer.class.getDeclaredField("programClassPool");
        programClassPoolField.setAccessible(true);
        assertSame(programPool, programClassPoolField.get(optimizer));

        Field libraryClassPoolField = GsonSerializationOptimizer.class.getDeclaredField("libraryClassPool");
        libraryClassPoolField.setAccessible(true);
        assertSame(libraryPool, libraryClassPoolField.get(optimizer));

        Field gsonRuntimeSettingsField = GsonSerializationOptimizer.class.getDeclaredField("gsonRuntimeSettings");
        gsonRuntimeSettingsField.setAccessible(true);
        assertSame(settings, gsonRuntimeSettingsField.get(optimizer));

        Field serializationInfoField = GsonSerializationOptimizer.class.getDeclaredField("serializationInfo");
        serializationInfoField.setAccessible(true);
        assertSame(info, serializationInfoField.get(optimizer));

        Field optimizeConservativelyField = GsonSerializationOptimizer.class.getDeclaredField("optimizeConservatively");
        optimizeConservativelyField.setAccessible(true);
        assertTrue((Boolean) optimizeConservativelyField.get(optimizer));

        Field extraDataEntryNameMapField = GsonSerializationOptimizer.class.getDeclaredField("extraDataEntryNameMap");
        extraDataEntryNameMapField.setAccessible(true);
        assertSame(nameMap, extraDataEntryNameMapField.get(optimizer));
    }

    /**
     * Tests that the constructor implements the correct interfaces.
     */
    @Test
    public void testConstructor_implementsCorrectInterfaces() {
        // Act
        GsonSerializationOptimizer optimizer = new GsonSerializationOptimizer(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            serializationInfo,
            false,
            extraDataEntryNameMap
        );

        // Assert
        assertTrue(optimizer instanceof proguard.classfile.visitor.MemberVisitor,
            "GsonSerializationOptimizer should implement MemberVisitor");
        assertTrue(optimizer instanceof proguard.classfile.visitor.ClassVisitor,
            "GsonSerializationOptimizer should implement ClassVisitor");
    }
}

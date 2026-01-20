package proguard.optimize.gson;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.io.ExtraDataEntryNameMap;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the constructor of {@link OptimizedTypeAdapterAdder}.
 * Tests the constructor: <init>.(Lproguard/classfile/ClassPool;Lproguard/classfile/ClassPool;Lproguard/classfile/editor/CodeAttributeEditor;Lproguard/optimize/gson/OptimizedJsonInfo;Lproguard/optimize/gson/OptimizedJsonInfo;Lproguard/io/ExtraDataEntryNameMap;Ljava/util/Map;Lproguard/optimize/gson/GsonRuntimeSettings;)V
 *
 * The OptimizedTypeAdapterAdder constructor takes 8 parameters that are stored as instance fields:
 * - programClassPool: the program class pool
 * - libraryClassPool: the library class pool
 * - codeAttributeEditor: the code attribute editor
 * - serializationInfo: serialization information
 * - deserializationInfo: deserialization information
 * - extraDataEntryNameMap: map for extra data entry names
 * - typeAdapterRegistry: registry for type adapters
 * - gsonRuntimeSettings: GSON runtime settings
 */
public class OptimizedTypeAdapterAdderClaude_constructorTest {

    // =========================================================================
    // Tests for constructor
    // =========================================================================

    /**
     * Tests that the constructor successfully creates an instance with all valid parameters.
     */
    @Test
    public void testConstructor_withValidParameters_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act
        OptimizedTypeAdapterAdder adder = new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        );

        // Assert
        assertNotNull(adder, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance that implements ClassVisitor.
     */
    @Test
    public void testConstructor_implementsClassVisitor() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act
        OptimizedTypeAdapterAdder adder = new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        );

        // Assert
        assertTrue(adder instanceof proguard.classfile.visitor.ClassVisitor,
                "OptimizedTypeAdapterAdder should implement ClassVisitor");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid input.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should not throw any exception with valid input");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_eachIsIndependent() {
        // Arrange
        ClassPool programClassPool1 = new ClassPool();
        ClassPool libraryClassPool1 = new ClassPool();
        CodeAttributeEditor codeAttributeEditor1 = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo1 = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo1 = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap1 = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry1 = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings1 = new GsonRuntimeSettings();

        ClassPool programClassPool2 = new ClassPool();
        ClassPool libraryClassPool2 = new ClassPool();
        CodeAttributeEditor codeAttributeEditor2 = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo2 = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo2 = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap2 = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry2 = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings2 = new GsonRuntimeSettings();

        // Act
        OptimizedTypeAdapterAdder adder1 = new OptimizedTypeAdapterAdder(
                programClassPool1,
                libraryClassPool1,
                codeAttributeEditor1,
                serializationInfo1,
                deserializationInfo1,
                extraDataEntryNameMap1,
                typeAdapterRegistry1,
                gsonRuntimeSettings1
        );
        OptimizedTypeAdapterAdder adder2 = new OptimizedTypeAdapterAdder(
                programClassPool2,
                libraryClassPool2,
                codeAttributeEditor2,
                serializationInfo2,
                deserializationInfo2,
                extraDataEntryNameMap2,
                typeAdapterRegistry2,
                gsonRuntimeSettings2
        );

        // Assert
        assertNotNull(adder1, "First instance should be created");
        assertNotNull(adder2, "Second instance should be created");
        assertNotSame(adder1, adder2, "Instances should be distinct");
    }

    /**
     * Tests that consecutive constructor calls create independent instances.
     */
    @Test
    public void testConstructor_consecutiveCalls_createIndependentInstances() {
        // Act & Assert
        for (int i = 0; i < 5; i++) {
            OptimizedTypeAdapterAdder adder = new OptimizedTypeAdapterAdder(
                    new ClassPool(),
                    new ClassPool(),
                    new CodeAttributeEditor(),
                    new OptimizedJsonInfo(),
                    new OptimizedJsonInfo(),
                    new ExtraDataEntryNameMap(),
                    new HashMap<>(),
                    new GsonRuntimeSettings()
            );
            assertNotNull(adder, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests constructor with null programClassPool parameter.
     */
    @Test
    public void testConstructor_withNullProgramClassPool_createsInstance() {
        // Arrange
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                null,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should accept null programClassPool without throwing");
    }

    /**
     * Tests constructor with null libraryClassPool parameter.
     */
    @Test
    public void testConstructor_withNullLibraryClassPool_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                null,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should accept null libraryClassPool without throwing");
    }

    /**
     * Tests constructor with null codeAttributeEditor parameter.
     */
    @Test
    public void testConstructor_withNullCodeAttributeEditor_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                null,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should accept null codeAttributeEditor without throwing");
    }

    /**
     * Tests constructor with null serializationInfo parameter.
     */
    @Test
    public void testConstructor_withNullSerializationInfo_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                null,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should accept null serializationInfo without throwing");
    }

    /**
     * Tests constructor with null deserializationInfo parameter.
     */
    @Test
    public void testConstructor_withNullDeserializationInfo_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                null,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should accept null deserializationInfo without throwing");
    }

    /**
     * Tests constructor with null extraDataEntryNameMap parameter.
     */
    @Test
    public void testConstructor_withNullExtraDataEntryNameMap_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                null,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should accept null extraDataEntryNameMap without throwing");
    }

    /**
     * Tests constructor with null typeAdapterRegistry parameter.
     */
    @Test
    public void testConstructor_withNullTypeAdapterRegistry_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                null,
                gsonRuntimeSettings
        ), "Constructor should accept null typeAdapterRegistry without throwing");
    }

    /**
     * Tests constructor with null gsonRuntimeSettings parameter.
     */
    @Test
    public void testConstructor_withNullGsonRuntimeSettings_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                null
        ), "Constructor should accept null gsonRuntimeSettings without throwing");
    }

    /**
     * Tests constructor with all parameters null.
     */
    @Test
    public void testConstructor_withAllParametersNull_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        ), "Constructor should accept all null parameters without throwing");
    }

    /**
     * Tests that multiple adders can share the same ClassPool instances.
     */
    @Test
    public void testConstructor_sharedClassPools_visitorsShareReferences() {
        // Arrange
        ClassPool sharedProgramClassPool = new ClassPool();
        ClassPool sharedLibraryClassPool = new ClassPool();

        // Act
        OptimizedTypeAdapterAdder adder1 = new OptimizedTypeAdapterAdder(
                sharedProgramClassPool,
                sharedLibraryClassPool,
                new CodeAttributeEditor(),
                new OptimizedJsonInfo(),
                new OptimizedJsonInfo(),
                new ExtraDataEntryNameMap(),
                new HashMap<>(),
                new GsonRuntimeSettings()
        );
        OptimizedTypeAdapterAdder adder2 = new OptimizedTypeAdapterAdder(
                sharedProgramClassPool,
                sharedLibraryClassPool,
                new CodeAttributeEditor(),
                new OptimizedJsonInfo(),
                new OptimizedJsonInfo(),
                new ExtraDataEntryNameMap(),
                new HashMap<>(),
                new GsonRuntimeSettings()
        );

        // Assert
        assertNotNull(adder1);
        assertNotNull(adder2);
        assertNotSame(adder1, adder2, "Adders should be different instances");
    }

    /**
     * Tests that multiple adders can share the same OptimizedJsonInfo instances.
     */
    @Test
    public void testConstructor_sharedOptimizedJsonInfo_visitorsShareReferences() {
        // Arrange
        OptimizedJsonInfo sharedSerializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo sharedDeserializationInfo = new OptimizedJsonInfo();

        // Act
        OptimizedTypeAdapterAdder adder1 = new OptimizedTypeAdapterAdder(
                new ClassPool(),
                new ClassPool(),
                new CodeAttributeEditor(),
                sharedSerializationInfo,
                sharedDeserializationInfo,
                new ExtraDataEntryNameMap(),
                new HashMap<>(),
                new GsonRuntimeSettings()
        );
        OptimizedTypeAdapterAdder adder2 = new OptimizedTypeAdapterAdder(
                new ClassPool(),
                new ClassPool(),
                new CodeAttributeEditor(),
                sharedSerializationInfo,
                sharedDeserializationInfo,
                new ExtraDataEntryNameMap(),
                new HashMap<>(),
                new GsonRuntimeSettings()
        );

        // Assert
        assertNotNull(adder1);
        assertNotNull(adder2);
        assertNotSame(adder1, adder2, "Adders should be different instances");
    }

    /**
     * Tests that multiple adders can share the same Map and ExtraDataEntryNameMap instances.
     */
    @Test
    public void testConstructor_sharedMapAndNameMap_visitorsShareReferences() {
        // Arrange
        Map<String, String> sharedRegistry = new HashMap<>();
        ExtraDataEntryNameMap sharedNameMap = new ExtraDataEntryNameMap();

        // Act
        OptimizedTypeAdapterAdder adder1 = new OptimizedTypeAdapterAdder(
                new ClassPool(),
                new ClassPool(),
                new CodeAttributeEditor(),
                new OptimizedJsonInfo(),
                new OptimizedJsonInfo(),
                sharedNameMap,
                sharedRegistry,
                new GsonRuntimeSettings()
        );
        OptimizedTypeAdapterAdder adder2 = new OptimizedTypeAdapterAdder(
                new ClassPool(),
                new ClassPool(),
                new CodeAttributeEditor(),
                new OptimizedJsonInfo(),
                new OptimizedJsonInfo(),
                sharedNameMap,
                sharedRegistry,
                new GsonRuntimeSettings()
        );

        // Assert
        assertNotNull(adder1);
        assertNotNull(adder2);
        assertNotSame(adder1, adder2, "Adders should be different instances");
    }

    /**
     * Tests constructor with same instance for both serializationInfo and deserializationInfo.
     */
    @Test
    public void testConstructor_sameInfoForBothParameters_createsInstance() {
        // Arrange
        OptimizedJsonInfo sharedInfo = new OptimizedJsonInfo();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                new ClassPool(),
                new ClassPool(),
                new CodeAttributeEditor(),
                sharedInfo,
                sharedInfo,
                new ExtraDataEntryNameMap(),
                new HashMap<>(),
                new GsonRuntimeSettings()
        ), "Constructor should accept the same instance for both serialization and deserialization info");
    }

    /**
     * Tests constructor with same ClassPool instance for both program and library class pools.
     */
    @Test
    public void testConstructor_sameClassPoolForBothParameters_createsInstance() {
        // Arrange
        ClassPool sharedClassPool = new ClassPool();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                sharedClassPool,
                sharedClassPool,
                new CodeAttributeEditor(),
                new OptimizedJsonInfo(),
                new OptimizedJsonInfo(),
                new ExtraDataEntryNameMap(),
                new HashMap<>(),
                new GsonRuntimeSettings()
        ), "Constructor should accept the same ClassPool instance for both parameters");
    }

    /**
     * Tests constructor with populated ClassPool instances.
     */
    @Test
    public void testConstructor_withPopulatedClassPools_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        programClassPool.addClass(createMinimalProgramClass("com/example/TestClass"));

        ClassPool libraryClassPool = new ClassPool();

        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should work with populated ClassPool instances");
    }

    /**
     * Tests constructor with populated OptimizedJsonInfo instances.
     */
    @Test
    public void testConstructor_withPopulatedOptimizedJsonInfo_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        serializationInfo.classIndices.put("com/example/TestClass", 0);
        serializationInfo.jsonFieldIndices.put("testField", 0);

        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        deserializationInfo.classIndices.put("com/example/TestClass", 0);

        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should work with populated OptimizedJsonInfo instances");
    }

    /**
     * Tests constructor with populated ExtraDataEntryNameMap.
     */
    @Test
    public void testConstructor_withPopulatedExtraDataEntryNameMap_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();

        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        extraDataEntryNameMap.addExtraClassToClass("com/example/SourceClass", "com/example/ExtraClass");

        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should work with populated ExtraDataEntryNameMap");
    }

    /**
     * Tests constructor with populated typeAdapterRegistry.
     */
    @Test
    public void testConstructor_withPopulatedTypeAdapterRegistry_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();

        Map<String, String> typeAdapterRegistry = new HashMap<>();
        typeAdapterRegistry.put("com/example/DomainClass", "com/example/TypeAdapter");

        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should work with populated typeAdapterRegistry");
    }

    /**
     * Tests constructor with configured GsonRuntimeSettings.
     */
    @Test
    public void testConstructor_withConfiguredGsonRuntimeSettings_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
        OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
        ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
        Map<String, String> typeAdapterRegistry = new HashMap<>();

        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
        gsonRuntimeSettings.setVersion = true;
        gsonRuntimeSettings.serializeNulls = true;
        gsonRuntimeSettings.disableInnerClassSerialization = true;

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterAdder(
                programClassPool,
                libraryClassPool,
                codeAttributeEditor,
                serializationInfo,
                deserializationInfo,
                extraDataEntryNameMap,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should work with configured GsonRuntimeSettings");
    }

    // =========================================================================
    // Helper methods
    // =========================================================================

    /**
     * Creates a minimal but valid ProgramClass for testing.
     *
     * @param className the name of the class (e.g., "com/example/TestClass")
     * @return a configured ProgramClass instance
     */
    private proguard.classfile.ProgramClass createMinimalProgramClass(String className) {
        proguard.classfile.ProgramClass programClass = new proguard.classfile.ProgramClass();
        programClass.u2thisClass = 1;

        // Create a minimal constant pool
        proguard.classfile.constant.Constant[] constantPool = new proguard.classfile.constant.Constant[10];
        constantPool[0] = null;
        constantPool[1] = new proguard.classfile.constant.ClassConstant(2, null);
        constantPool[2] = new proguard.classfile.constant.Utf8Constant(className);

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 10;
        programClass.fields = new proguard.classfile.ProgramField[0];
        programClass.u2fieldsCount = 0;

        return programClass;
    }
}

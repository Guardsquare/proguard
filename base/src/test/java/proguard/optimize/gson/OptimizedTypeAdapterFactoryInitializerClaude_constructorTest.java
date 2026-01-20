package proguard.optimize.gson;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.editor.CodeAttributeEditor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the constructor of {@link OptimizedTypeAdapterFactoryInitializer}.
 * Tests the constructor: <init>.(Lproguard/classfile/ClassPool;Lproguard/classfile/editor/CodeAttributeEditor;Ljava/util/Map;Lproguard/optimize/gson/GsonRuntimeSettings;)V
 *
 * The OptimizedTypeAdapterFactoryInitializer constructor takes 4 parameters that are stored as instance fields:
 * - programClassPool: the program class pool used for looking up references to program classes
 * - codeAttributeEditor: the code attribute editor used for editing the code attribute of the getType() method
 * - typeAdapterRegistry: contains the mapping between domain class names and their corresponding type adapter class name
 * - gsonRuntimeSettings: keeps track of all GsonBuilder invocations
 */
public class OptimizedTypeAdapterFactoryInitializerClaude_constructorTest {

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
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act
        OptimizedTypeAdapterFactoryInitializer initializer = new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
                typeAdapterRegistry,
                gsonRuntimeSettings
        );

        // Assert
        assertNotNull(initializer, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance that implements InstructionVisitor.
     */
    @Test
    public void testConstructor_implementsInstructionVisitor() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act
        OptimizedTypeAdapterFactoryInitializer initializer = new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
                typeAdapterRegistry,
                gsonRuntimeSettings
        );

        // Assert
        assertTrue(initializer instanceof proguard.classfile.instruction.visitor.InstructionVisitor,
                "OptimizedTypeAdapterFactoryInitializer should implement InstructionVisitor");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid input.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
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
        CodeAttributeEditor codeAttributeEditor1 = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry1 = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings1 = new GsonRuntimeSettings();

        ClassPool programClassPool2 = new ClassPool();
        CodeAttributeEditor codeAttributeEditor2 = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry2 = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings2 = new GsonRuntimeSettings();

        // Act
        OptimizedTypeAdapterFactoryInitializer initializer1 = new OptimizedTypeAdapterFactoryInitializer(
                programClassPool1,
                codeAttributeEditor1,
                typeAdapterRegistry1,
                gsonRuntimeSettings1
        );
        OptimizedTypeAdapterFactoryInitializer initializer2 = new OptimizedTypeAdapterFactoryInitializer(
                programClassPool2,
                codeAttributeEditor2,
                typeAdapterRegistry2,
                gsonRuntimeSettings2
        );

        // Assert
        assertNotNull(initializer1, "First instance should be created");
        assertNotNull(initializer2, "Second instance should be created");
        assertNotSame(initializer1, initializer2, "Instances should be distinct");
    }

    /**
     * Tests that consecutive constructor calls create independent instances.
     */
    @Test
    public void testConstructor_consecutiveCalls_createIndependentInstances() {
        // Act & Assert
        for (int i = 0; i < 5; i++) {
            OptimizedTypeAdapterFactoryInitializer initializer = new OptimizedTypeAdapterFactoryInitializer(
                    new ClassPool(),
                    new CodeAttributeEditor(),
                    new HashMap<>(),
                    new GsonRuntimeSettings()
            );
            assertNotNull(initializer, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests constructor with null programClassPool parameter.
     */
    @Test
    public void testConstructor_withNullProgramClassPool_createsInstance() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                null,
                codeAttributeEditor,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should accept null programClassPool without throwing");
    }

    /**
     * Tests constructor with null codeAttributeEditor parameter.
     */
    @Test
    public void testConstructor_withNullCodeAttributeEditor_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                null,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should accept null codeAttributeEditor without throwing");
    }

    /**
     * Tests constructor with null typeAdapterRegistry parameter.
     */
    @Test
    public void testConstructor_withNullTypeAdapterRegistry_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
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
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry = new HashMap<>();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
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
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                null,
                null,
                null,
                null
        ), "Constructor should accept all null parameters without throwing");
    }

    /**
     * Tests that multiple initializers can share the same ClassPool instance.
     */
    @Test
    public void testConstructor_sharedClassPool_initializersShareReferences() {
        // Arrange
        ClassPool sharedProgramClassPool = new ClassPool();

        // Act
        OptimizedTypeAdapterFactoryInitializer initializer1 = new OptimizedTypeAdapterFactoryInitializer(
                sharedProgramClassPool,
                new CodeAttributeEditor(),
                new HashMap<>(),
                new GsonRuntimeSettings()
        );
        OptimizedTypeAdapterFactoryInitializer initializer2 = new OptimizedTypeAdapterFactoryInitializer(
                sharedProgramClassPool,
                new CodeAttributeEditor(),
                new HashMap<>(),
                new GsonRuntimeSettings()
        );

        // Assert
        assertNotNull(initializer1);
        assertNotNull(initializer2);
        assertNotSame(initializer1, initializer2, "Initializers should be different instances");
    }

    /**
     * Tests that multiple initializers can share the same CodeAttributeEditor instance.
     */
    @Test
    public void testConstructor_sharedCodeAttributeEditor_initializersShareReferences() {
        // Arrange
        CodeAttributeEditor sharedCodeAttributeEditor = new CodeAttributeEditor();

        // Act
        OptimizedTypeAdapterFactoryInitializer initializer1 = new OptimizedTypeAdapterFactoryInitializer(
                new ClassPool(),
                sharedCodeAttributeEditor,
                new HashMap<>(),
                new GsonRuntimeSettings()
        );
        OptimizedTypeAdapterFactoryInitializer initializer2 = new OptimizedTypeAdapterFactoryInitializer(
                new ClassPool(),
                sharedCodeAttributeEditor,
                new HashMap<>(),
                new GsonRuntimeSettings()
        );

        // Assert
        assertNotNull(initializer1);
        assertNotNull(initializer2);
        assertNotSame(initializer1, initializer2, "Initializers should be different instances");
    }

    /**
     * Tests that multiple initializers can share the same Map instance.
     */
    @Test
    public void testConstructor_sharedTypeAdapterRegistry_initializersShareReferences() {
        // Arrange
        Map<String, String> sharedRegistry = new HashMap<>();

        // Act
        OptimizedTypeAdapterFactoryInitializer initializer1 = new OptimizedTypeAdapterFactoryInitializer(
                new ClassPool(),
                new CodeAttributeEditor(),
                sharedRegistry,
                new GsonRuntimeSettings()
        );
        OptimizedTypeAdapterFactoryInitializer initializer2 = new OptimizedTypeAdapterFactoryInitializer(
                new ClassPool(),
                new CodeAttributeEditor(),
                sharedRegistry,
                new GsonRuntimeSettings()
        );

        // Assert
        assertNotNull(initializer1);
        assertNotNull(initializer2);
        assertNotSame(initializer1, initializer2, "Initializers should be different instances");
    }

    /**
     * Tests that multiple initializers can share the same GsonRuntimeSettings instance.
     */
    @Test
    public void testConstructor_sharedGsonRuntimeSettings_initializersShareReferences() {
        // Arrange
        GsonRuntimeSettings sharedSettings = new GsonRuntimeSettings();

        // Act
        OptimizedTypeAdapterFactoryInitializer initializer1 = new OptimizedTypeAdapterFactoryInitializer(
                new ClassPool(),
                new CodeAttributeEditor(),
                new HashMap<>(),
                sharedSettings
        );
        OptimizedTypeAdapterFactoryInitializer initializer2 = new OptimizedTypeAdapterFactoryInitializer(
                new ClassPool(),
                new CodeAttributeEditor(),
                new HashMap<>(),
                sharedSettings
        );

        // Assert
        assertNotNull(initializer1);
        assertNotNull(initializer2);
        assertNotSame(initializer1, initializer2, "Initializers should be different instances");
    }

    /**
     * Tests constructor with populated ClassPool instance.
     */
    @Test
    public void testConstructor_withPopulatedClassPool_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        programClassPool.addClass(createMinimalProgramClass("com/example/TestClass"));

        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should work with populated ClassPool instance");
    }

    /**
     * Tests constructor with populated typeAdapterRegistry.
     */
    @Test
    public void testConstructor_withPopulatedTypeAdapterRegistry_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

        Map<String, String> typeAdapterRegistry = new HashMap<>();
        typeAdapterRegistry.put("com/example/DomainClass", "com/example/TypeAdapter");
        typeAdapterRegistry.put("com/example/AnotherClass", "com/example/AnotherTypeAdapter");

        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
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
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry = new HashMap<>();

        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
        gsonRuntimeSettings.setFieldNamingPolicy = true;
        gsonRuntimeSettings.excludeFieldsWithModifiers = true;
        gsonRuntimeSettings.setVersion = true;

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should work with configured GsonRuntimeSettings");
    }

    /**
     * Tests constructor with all GsonRuntimeSettings flags enabled.
     */
    @Test
    public void testConstructor_withAllGsonRuntimeSettingsEnabled_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry = new HashMap<>();

        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
        gsonRuntimeSettings.setFieldNamingPolicy = true;
        gsonRuntimeSettings.setFieldNamingStrategy = true;
        gsonRuntimeSettings.excludeFieldsWithModifiers = true;
        gsonRuntimeSettings.setExclusionStrategies = true;
        gsonRuntimeSettings.addSerializationExclusionStrategy = true;
        gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
        gsonRuntimeSettings.setVersion = true;

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should work with all GsonRuntimeSettings flags enabled");
    }

    /**
     * Tests constructor with empty typeAdapterRegistry.
     */
    @Test
    public void testConstructor_withEmptyTypeAdapterRegistry_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> emptyRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
                emptyRegistry,
                gsonRuntimeSettings
        ), "Constructor should work with empty typeAdapterRegistry");
    }

    /**
     * Tests constructor with empty ClassPool.
     */
    @Test
    public void testConstructor_withEmptyClassPool_createsInstance() {
        // Arrange
        ClassPool emptyClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                emptyClassPool,
                codeAttributeEditor,
                typeAdapterRegistry,
                gsonRuntimeSettings
        ), "Constructor should work with empty ClassPool");
    }

    /**
     * Tests constructor with default GsonRuntimeSettings (all flags false).
     */
    @Test
    public void testConstructor_withDefaultGsonRuntimeSettings_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings defaultSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
                typeAdapterRegistry,
                defaultSettings
        ), "Constructor should work with default GsonRuntimeSettings");
    }

    /**
     * Tests constructor with large typeAdapterRegistry.
     */
    @Test
    public void testConstructor_withLargeTypeAdapterRegistry_createsInstance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

        Map<String, String> largeRegistry = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            largeRegistry.put("com/example/DomainClass" + i, "com/example/TypeAdapter" + i);
        }

        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        assertDoesNotThrow(() -> new OptimizedTypeAdapterFactoryInitializer(
                programClassPool,
                codeAttributeEditor,
                largeRegistry,
                gsonRuntimeSettings
        ), "Constructor should work with large typeAdapterRegistry");
    }

    /**
     * Tests that constructor with all valid parameters creates an instance multiple times.
     */
    @Test
    public void testConstructor_repeatedCreation_alwaysSucceeds() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        Map<String, String> typeAdapterRegistry = new HashMap<>();
        GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            OptimizedTypeAdapterFactoryInitializer initializer = new OptimizedTypeAdapterFactoryInitializer(
                    programClassPool,
                    codeAttributeEditor,
                    typeAdapterRegistry,
                    gsonRuntimeSettings
            );
            assertNotNull(initializer, "Instance " + i + " should be created");
        }
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

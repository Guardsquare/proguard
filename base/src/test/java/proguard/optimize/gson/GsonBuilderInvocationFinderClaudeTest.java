package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.constant.*;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GsonBuilderInvocationFinder}.
 * Tests the constructor and visitAnyInstruction method to ensure proper detection
 * of GsonBuilder invocations and tracking of Gson runtime settings.
 *
 * The GsonBuilderInvocationFinder searches for invocations to GsonBuilder methods
 * and updates the GsonRuntimeSettings to track which parameters are being used.
 */
public class GsonBuilderInvocationFinderClaudeTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private GsonRuntimeSettings gsonRuntimeSettings;
    private ClassVisitor instanceCreatorClassVisitor;
    private ClassVisitor typeAdapterClassVisitor;
    private GsonBuilderInvocationFinder finder;

    /**
     * Sets up fresh instances before each test.
     */
    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        gsonRuntimeSettings = new GsonRuntimeSettings();
        instanceCreatorClassVisitor = mock(ClassVisitor.class);
        typeAdapterClassVisitor = mock(ClassVisitor.class);

        finder = new GsonBuilderInvocationFinder(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            instanceCreatorClassVisitor,
            typeAdapterClassVisitor
        );
    }

    // =========================================================================
    // Tests for constructor
    // =========================================================================

    /**
     * Tests that the constructor successfully creates an instance with all required parameters.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        GsonBuilderInvocationFinder newFinder = new GsonBuilderInvocationFinder(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            instanceCreatorClassVisitor,
            typeAdapterClassVisitor
        );

        // Assert
        assertNotNull(newFinder, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance that implements InstructionVisitor.
     */
    @Test
    public void testConstructor_implementsInstructionVisitor() {
        // Act
        GsonBuilderInvocationFinder newFinder = new GsonBuilderInvocationFinder(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            instanceCreatorClassVisitor,
            typeAdapterClassVisitor
        );

        // Assert
        assertTrue(newFinder instanceof proguard.classfile.instruction.visitor.InstructionVisitor,
                "GsonBuilderInvocationFinder should implement InstructionVisitor");
    }

    /**
     * Tests that the constructor accepts null for both ClassVisitor parameters.
     */
    @Test
    public void testConstructor_withNullClassVisitors_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonBuilderInvocationFinder(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            null,
            null
        ), "Constructor should accept null ClassVisitor parameters");
    }

    /**
     * Tests that the constructor accepts only null for instanceCreatorClassVisitor.
     */
    @Test
    public void testConstructor_withNullInstanceCreatorVisitor_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonBuilderInvocationFinder(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            null,
            typeAdapterClassVisitor
        ), "Constructor should accept null instanceCreatorClassVisitor");
    }

    /**
     * Tests that the constructor accepts only null for typeAdapterClassVisitor.
     */
    @Test
    public void testConstructor_withNullTypeAdapterVisitor_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonBuilderInvocationFinder(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            instanceCreatorClassVisitor,
            null
        ), "Constructor should accept null typeAdapterClassVisitor");
    }

    /**
     * Tests that the constructor works with empty ClassPool instances.
     */
    @Test
    public void testConstructor_withEmptyClassPools_createsInstance() {
        // Arrange
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();

        // Act
        GsonBuilderInvocationFinder newFinder = new GsonBuilderInvocationFinder(
            emptyProgramPool,
            emptyLibraryPool,
            gsonRuntimeSettings,
            instanceCreatorClassVisitor,
            typeAdapterClassVisitor
        );

        // Assert
        assertNotNull(newFinder, "Constructor should work with empty class pools");
    }

    /**
     * Tests that the constructor works with a GsonRuntimeSettings that has all flags set to false.
     */
    @Test
    public void testConstructor_withDefaultGsonRuntimeSettings_createsInstance() {
        // Arrange
        GsonRuntimeSettings defaultSettings = new GsonRuntimeSettings();

        // Act
        GsonBuilderInvocationFinder newFinder = new GsonBuilderInvocationFinder(
            programClassPool,
            libraryClassPool,
            defaultSettings,
            instanceCreatorClassVisitor,
            typeAdapterClassVisitor
        );

        // Assert
        assertNotNull(newFinder, "Constructor should work with default settings");
    }

    /**
     * Tests that the constructor works with a GsonRuntimeSettings that has all flags set to true.
     */
    @Test
    public void testConstructor_withAllFlagsSetToTrue_createsInstance() {
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
        allTrueSettings.serializeSpecialFloatingPointValues = true;
        allTrueSettings.registerTypeAdapterFactory = true;

        // Act
        GsonBuilderInvocationFinder newFinder = new GsonBuilderInvocationFinder(
            programClassPool,
            libraryClassPool,
            allTrueSettings,
            instanceCreatorClassVisitor,
            typeAdapterClassVisitor
        );

        // Assert
        assertNotNull(newFinder, "Constructor should work with all flags set to true");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnState() {
        // Arrange
        GsonRuntimeSettings settings1 = new GsonRuntimeSettings();
        GsonRuntimeSettings settings2 = new GsonRuntimeSettings();
        GsonRuntimeSettings settings3 = new GsonRuntimeSettings();

        // Act
        GsonBuilderInvocationFinder finder1 = new GsonBuilderInvocationFinder(
            programClassPool, libraryClassPool, settings1, null, null);
        GsonBuilderInvocationFinder finder2 = new GsonBuilderInvocationFinder(
            programClassPool, libraryClassPool, settings2, null, null);
        GsonBuilderInvocationFinder finder3 = new GsonBuilderInvocationFinder(
            programClassPool, libraryClassPool, settings3, null, null);

        // Assert
        assertNotNull(finder1, "First instance should be created");
        assertNotNull(finder2, "Second instance should be created");
        assertNotNull(finder3, "Third instance should be created");
        assertNotSame(finder1, finder2, "Instances should be distinct");
        assertNotSame(finder2, finder3, "Instances should be distinct");
        assertNotSame(finder1, finder3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid input.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonBuilderInvocationFinder(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            instanceCreatorClassVisitor,
            typeAdapterClassVisitor
        ), "Constructor should not throw any exception");
    }

    /**
     * Tests that consecutive constructor calls create independent instances.
     */
    @Test
    public void testConstructor_consecutiveCalls_createIndependentInstances() {
        // Act & Assert
        for (int i = 0; i < 5; i++) {
            GsonBuilderInvocationFinder newFinder = new GsonBuilderInvocationFinder(
                new ClassPool(),
                new ClassPool(),
                new GsonRuntimeSettings(),
                mock(ClassVisitor.class),
                mock(ClassVisitor.class)
            );
            assertNotNull(newFinder, "Instance " + i + " should be created");
        }
    }

    // =========================================================================
    // Tests for visitAnyInstruction
    // =========================================================================

    /**
     * Tests that visitAnyInstruction does not throw with valid parameters.
     */
    @Test
    public void testVisitAnyInstruction_withValidParameters_doesNotThrow() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(
            clazz, method, codeAttribute, 0, instruction
        ), "visitAnyInstruction should not throw with valid parameters");
    }

    /**
     * Tests that visitAnyInstruction processes instruction when all settings are false.
     * This is the normal case where the finder is looking for GsonBuilder invocations.
     */
    @Test
    public void testVisitAnyInstruction_withAllSettingsFalse_processesInstruction() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        assertFalse(gsonRuntimeSettings.setVersion, "setVersion should start as false");
        assertFalse(gsonRuntimeSettings.serializeNulls, "serializeNulls should start as false");

        // Act
        finder.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - The method should complete without error
        // Settings will only be updated if the instruction matches a GsonBuilder method
        assertFalse(gsonRuntimeSettings.setVersion, "setVersion should remain false without matching instruction");
    }

    /**
     * Tests that visitAnyInstruction skips processing when setVersion is already true.
     */
    @Test
    public void testVisitAnyInstruction_withSetVersionTrue_skipsProcessing() {
        // Arrange
        gsonRuntimeSettings.setVersion = true;

        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act
        finder.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - Method should complete without attempting to match setVersion
        assertTrue(gsonRuntimeSettings.setVersion, "setVersion should remain true");
    }

    /**
     * Tests that visitAnyInstruction processes multiple instructions sequentially.
     */
    @Test
    public void testVisitAnyInstruction_multipleInstructions_processesEach() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction1 = createSimpleInstruction();
        Instruction instruction2 = createSimpleInstruction();
        Instruction instruction3 = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction1);
            finder.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction2);
            finder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction3);
        }, "Should process multiple instructions without error");
    }

    /**
     * Tests that visitAnyInstruction with different offsets processes correctly.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentOffsets_processesCorrectly() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
            finder.visitAnyInstruction(clazz, method, codeAttribute, 10, instruction);
            finder.visitAnyInstruction(clazz, method, codeAttribute, 100, instruction);
        }, "Should handle different offsets correctly");
    }

    /**
     * Tests that visitAnyInstruction can be called repeatedly on the same instruction.
     */
    @Test
    public void testVisitAnyInstruction_repeatedCalls_handlesCorrectly() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                finder.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
            }
        }, "Should handle repeated calls correctly");
    }

    /**
     * Tests that visitAnyInstruction with null class visitors (from constructor) still works.
     */
    @Test
    public void testVisitAnyInstruction_withNullClassVisitors_processesCorrectly() {
        // Arrange
        GsonBuilderInvocationFinder finderWithNullVisitors = new GsonBuilderInvocationFinder(
            programClassPool,
            libraryClassPool,
            gsonRuntimeSettings,
            null,
            null
        );

        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> finderWithNullVisitors.visitAnyInstruction(
            clazz, method, codeAttribute, 0, instruction
        ), "Should work with null class visitors");
    }

    /**
     * Tests that visitAnyInstruction behavior is consistent across multiple calls
     * with the same instruction.
     */
    @Test
    public void testVisitAnyInstruction_deterministic_sameInputSameBehavior() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Capture initial state
        boolean initialSetVersion = gsonRuntimeSettings.setVersion;
        boolean initialSerializeNulls = gsonRuntimeSettings.serializeNulls;

        // Act
        finder.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        boolean afterFirstCallSetVersion = gsonRuntimeSettings.setVersion;
        boolean afterFirstCallSerializeNulls = gsonRuntimeSettings.serializeNulls;

        finder.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        boolean afterSecondCallSetVersion = gsonRuntimeSettings.setVersion;
        boolean afterSecondCallSerializeNulls = gsonRuntimeSettings.serializeNulls;

        // Assert - Behavior should be consistent
        assertEquals(afterFirstCallSetVersion, afterSecondCallSetVersion,
            "setVersion should have consistent behavior");
        assertEquals(afterFirstCallSerializeNulls, afterSecondCallSerializeNulls,
            "serializeNulls should have consistent behavior");
    }

    /**
     * Tests that visitAnyInstruction with mixed settings (some true, some false)
     * processes correctly.
     */
    @Test
    public void testVisitAnyInstruction_withMixedSettings_processesOnlyFalseFlags() {
        // Arrange
        gsonRuntimeSettings.setVersion = true;
        gsonRuntimeSettings.excludeFieldsWithModifiers = false;
        gsonRuntimeSettings.serializeNulls = true;
        gsonRuntimeSettings.setFieldNamingPolicy = false;

        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act
        finder.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - The true flags should remain true, false flags are processed
        assertTrue(gsonRuntimeSettings.setVersion, "setVersion should remain true");
        assertTrue(gsonRuntimeSettings.serializeNulls, "serializeNulls should remain true");
    }

    /**
     * Tests that visitAnyInstruction with all settings already true completes quickly.
     * When all settings are true, the method should skip most processing.
     */
    @Test
    public void testVisitAnyInstruction_withAllSettingsTrue_completesQuickly() {
        // Arrange
        gsonRuntimeSettings.setVersion = true;
        gsonRuntimeSettings.excludeFieldsWithModifiers = true;
        gsonRuntimeSettings.generateNonExecutableJson = true;
        gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
        gsonRuntimeSettings.serializeNulls = true;
        gsonRuntimeSettings.disableInnerClassSerialization = true;
        gsonRuntimeSettings.setLongSerializationPolicy = true;
        gsonRuntimeSettings.setFieldNamingPolicy = true;
        gsonRuntimeSettings.setFieldNamingStrategy = true;
        gsonRuntimeSettings.setExclusionStrategies = true;
        gsonRuntimeSettings.addSerializationExclusionStrategy = true;
        gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
        gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
        gsonRuntimeSettings.registerTypeAdapterFactory = true;

        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(
            clazz, method, codeAttribute, 0, instruction
        ), "Should complete without error when all settings are true");
    }

    /**
     * Tests that different finder instances maintain independent state.
     */
    @Test
    public void testVisitAnyInstruction_multipleFinderInstances_independentState() {
        // Arrange
        GsonRuntimeSettings settings1 = new GsonRuntimeSettings();
        GsonRuntimeSettings settings2 = new GsonRuntimeSettings();

        GsonBuilderInvocationFinder finder1 = new GsonBuilderInvocationFinder(
            programClassPool, libraryClassPool, settings1, null, null);
        GsonBuilderInvocationFinder finder2 = new GsonBuilderInvocationFinder(
            programClassPool, libraryClassPool, settings2, null, null);

        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act
        settings1.setVersion = true;
        finder1.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        finder2.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - settings2 should not be affected by settings1
        assertTrue(settings1.setVersion, "settings1.setVersion should be true");
        assertFalse(settings2.setVersion, "settings2.setVersion should remain false");
    }

    /**
     * Tests that visitAnyInstruction processes instructions from different classes.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentClasses_processesEach() {
        // Arrange
        ProgramClass class1 = createMinimalProgramClass("TestClass1");
        ProgramClass class2 = createMinimalProgramClass("TestClass2");
        ProgramClass class3 = createMinimalProgramClass("TestClass3");

        ProgramMethod method1 = createMinimalMethod(class1, "method1");
        ProgramMethod method2 = createMinimalMethod(class2, "method2");
        ProgramMethod method3 = createMinimalMethod(class3, "method3");

        CodeAttribute codeAttr1 = createMinimalCodeAttribute(class1, method1);
        CodeAttribute codeAttr2 = createMinimalCodeAttribute(class2, method2);
        CodeAttribute codeAttr3 = createMinimalCodeAttribute(class3, method3);

        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyInstruction(class1, method1, codeAttr1, 0, instruction);
            finder.visitAnyInstruction(class2, method2, codeAttr2, 0, instruction);
            finder.visitAnyInstruction(class3, method3, codeAttr3, 0, instruction);
        }, "Should process instructions from different classes");
    }

    /**
     * Tests that visitAnyInstruction works correctly even after processing many instructions.
     */
    @Test
    public void testVisitAnyInstruction_afterManyInstructions_stillFunctional() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Process many instructions
        for (int i = 0; i < 100; i++) {
            finder.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
        }

        // Act & Assert - Should still work after many calls
        assertDoesNotThrow(() -> finder.visitAnyInstruction(
            clazz, method, codeAttribute, 100, instruction
        ), "Should still work after processing many instructions");
    }

    // =========================================================================
    // Helper methods to create test objects
    // =========================================================================

    /**
     * Creates a minimal but valid ProgramClass for testing.
     *
     * @param className the name of the class
     * @return a configured ProgramClass instance
     */
    private ProgramClass createMinimalProgramClass(String className) {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a minimal constant pool
        Constant[] constantPool = new Constant[10];
        constantPool[0] = null;
        constantPool[1] = new ClassConstant(2, null);
        constantPool[2] = new Utf8Constant(className);

        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 10;

        return programClass;
    }

    /**
     * Creates a minimal ProgramMethod for testing.
     *
     * @param clazz the owning class
     * @param methodName the method name
     * @return a configured ProgramMethod instance
     */
    private ProgramMethod createMinimalMethod(ProgramClass clazz, String methodName) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = 0;

        // Add method name to constant pool
        int nameIndex = 3;
        int descriptorIndex = 4;

        if (clazz.constantPool[nameIndex] == null) {
            clazz.constantPool[nameIndex] = new Utf8Constant(methodName);
        }
        if (clazz.constantPool[descriptorIndex] == null) {
            clazz.constantPool[descriptorIndex] = new Utf8Constant("()V");
        }

        method.u2nameIndex = nameIndex;
        method.u2descriptorIndex = descriptorIndex;
        method.u2attributesCount = 0;
        method.attributes = new Attribute[0];

        return method;
    }

    /**
     * Creates a minimal CodeAttribute for testing.
     *
     * @param clazz the owning class
     * @param method the owning method
     * @return a configured CodeAttribute instance
     */
    private CodeAttribute createMinimalCodeAttribute(ProgramClass clazz, ProgramMethod method) {
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxStack = 10;
        codeAttribute.u2maxLocals = 10;
        codeAttribute.u4codeLength = 10;
        codeAttribute.code = new byte[10];
        codeAttribute.u2exceptionTableLength = 0;
        codeAttribute.exceptionTable = new ExceptionInfo[0];
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];

        return codeAttribute;
    }

    /**
     * Creates a simple instruction for testing.
     * Uses NOP instruction as it's the simplest.
     *
     * @return a simple Instruction instance
     */
    private Instruction createSimpleInstruction() {
        InstructionSequenceBuilder builder = new InstructionSequenceBuilder();
        builder.nop();
        Instruction[] instructions = builder.instructions();
        return instructions[0];
    }
}

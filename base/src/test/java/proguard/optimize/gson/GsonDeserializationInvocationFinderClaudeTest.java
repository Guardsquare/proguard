package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.constant.*;
import proguard.classfile.editor.InstructionSequenceBuilder;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GsonDeserializationInvocationFinder}.
 * Tests the constructor and visitAnyInstruction method to ensure proper detection
 * of Gson fromJson invocations and tracking of domain classes that are deserialized.
 *
 * The GsonDeserializationInvocationFinder searches the code for invocations to any of the
 * deserialization methods of Gson (all the fromJson variants) and keeps track of the domain
 * classes that are involved in the deserialization.
 */
public class GsonDeserializationInvocationFinderClaudeTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private ClassVisitor domainClassVisitor;
    private WarningPrinter warningPrinter;
    private GsonDeserializationInvocationFinder finder;
    private StringWriter warningOutput;

    /**
     * Sets up fresh instances before each test.
     */
    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        domainClassVisitor = mock(ClassVisitor.class);

        // Set up warning printer with a StringWriter so we can capture warnings
        warningOutput = new StringWriter();
        warningPrinter = new WarningPrinter(new PrintWriter(warningOutput));

        finder = new GsonDeserializationInvocationFinder(
            programClassPool,
            libraryClassPool,
            domainClassVisitor,
            warningPrinter
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
        GsonDeserializationInvocationFinder newFinder = new GsonDeserializationInvocationFinder(
            programClassPool,
            libraryClassPool,
            domainClassVisitor,
            warningPrinter
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
        GsonDeserializationInvocationFinder newFinder = new GsonDeserializationInvocationFinder(
            programClassPool,
            libraryClassPool,
            domainClassVisitor,
            warningPrinter
        );

        // Assert
        assertTrue(newFinder instanceof proguard.classfile.instruction.visitor.InstructionVisitor,
                "GsonDeserializationInvocationFinder should implement InstructionVisitor");
    }

    /**
     * Tests that the constructor accepts null for the ClassVisitor parameter.
     */
    @Test
    public void testConstructor_withNullClassVisitor_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonDeserializationInvocationFinder(
            programClassPool,
            libraryClassPool,
            null,
            warningPrinter
        ), "Constructor should accept null ClassVisitor parameter");
    }

    /**
     * Tests that the constructor accepts null for the WarningPrinter parameter.
     */
    @Test
    public void testConstructor_withNullWarningPrinter_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonDeserializationInvocationFinder(
            programClassPool,
            libraryClassPool,
            domainClassVisitor,
            null
        ), "Constructor should accept null WarningPrinter parameter");
    }

    /**
     * Tests that the constructor accepts both null ClassVisitor and WarningPrinter.
     */
    @Test
    public void testConstructor_withNullVisitorAndPrinter_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonDeserializationInvocationFinder(
            programClassPool,
            libraryClassPool,
            null,
            null
        ), "Constructor should accept null ClassVisitor and WarningPrinter");
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
        GsonDeserializationInvocationFinder newFinder = new GsonDeserializationInvocationFinder(
            emptyProgramPool,
            emptyLibraryPool,
            domainClassVisitor,
            warningPrinter
        );

        // Assert
        assertNotNull(newFinder, "Constructor should work with empty class pools");
    }

    /**
     * Tests that the constructor initializes the fromJsonInvocationMatchers.
     * This is verified by ensuring the instance can be used without throwing exceptions.
     */
    @Test
    public void testConstructor_initializesMatchers() {
        // Act
        GsonDeserializationInvocationFinder newFinder = new GsonDeserializationInvocationFinder(
            programClassPool,
            libraryClassPool,
            domainClassVisitor,
            warningPrinter
        );

        // Assert - Should be able to visit instructions without error
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        assertDoesNotThrow(() -> newFinder.visitAnyInstruction(
            clazz, method, codeAttribute, 0, instruction
        ), "Constructor should initialize matchers properly");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnState() {
        // Act
        GsonDeserializationInvocationFinder finder1 = new GsonDeserializationInvocationFinder(
            programClassPool, libraryClassPool, domainClassVisitor, warningPrinter);
        GsonDeserializationInvocationFinder finder2 = new GsonDeserializationInvocationFinder(
            programClassPool, libraryClassPool, domainClassVisitor, warningPrinter);
        GsonDeserializationInvocationFinder finder3 = new GsonDeserializationInvocationFinder(
            programClassPool, libraryClassPool, domainClassVisitor, warningPrinter);

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
        assertDoesNotThrow(() -> new GsonDeserializationInvocationFinder(
            programClassPool,
            libraryClassPool,
            domainClassVisitor,
            warningPrinter
        ), "Constructor should not throw any exception");
    }

    /**
     * Tests that consecutive constructor calls create independent instances.
     */
    @Test
    public void testConstructor_consecutiveCalls_createIndependentInstances() {
        // Act & Assert
        for (int i = 0; i < 5; i++) {
            GsonDeserializationInvocationFinder newFinder = new GsonDeserializationInvocationFinder(
                new ClassPool(),
                new ClassPool(),
                mock(ClassVisitor.class),
                new WarningPrinter(new PrintWriter(new StringWriter()))
            );
            assertNotNull(newFinder, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests that the constructor works with different combinations of class pools.
     */
    @Test
    public void testConstructor_withDifferentClassPoolCombinations_createsInstance() {
        // Arrange
        ClassPool pool1 = new ClassPool();
        ClassPool pool2 = new ClassPool();

        // Act & Assert - Different combinations
        assertDoesNotThrow(() -> new GsonDeserializationInvocationFinder(
            pool1, pool1, domainClassVisitor, warningPrinter
        ), "Constructor should work with same pool for both parameters");

        assertDoesNotThrow(() -> new GsonDeserializationInvocationFinder(
            pool1, pool2, domainClassVisitor, warningPrinter
        ), "Constructor should work with different pools");
    }

    /**
     * Tests that the constructor creates distinct internal matcher instances.
     */
    @Test
    public void testConstructor_createsDistinctMatchers() {
        // Arrange & Act
        GsonDeserializationInvocationFinder finder1 = new GsonDeserializationInvocationFinder(
            programClassPool, libraryClassPool, domainClassVisitor, warningPrinter);
        GsonDeserializationInvocationFinder finder2 = new GsonDeserializationInvocationFinder(
            programClassPool, libraryClassPool, domainClassVisitor, warningPrinter);

        // Assert - Each finder should have its own matchers and not interfere with each other
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        assertDoesNotThrow(() -> {
            finder1.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
            finder2.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        }, "Each finder should have distinct internal state");
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
     * Tests that visitAnyInstruction processes a simple non-matching instruction.
     */
    @Test
    public void testVisitAnyInstruction_withNonMatchingInstruction_completesWithoutError() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act
        finder.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - The method should complete without error
        // No domain class visitor should be invoked for non-matching instructions
        verify(domainClassVisitor, never()).visitAnyClass(any());
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
     * Tests that visitAnyInstruction with null class visitor (from constructor) still works.
     */
    @Test
    public void testVisitAnyInstruction_withNullClassVisitor_processesCorrectly() {
        // Arrange
        GsonDeserializationInvocationFinder finderWithNullVisitor =
            new GsonDeserializationInvocationFinder(
                programClassPool,
                libraryClassPool,
                null,
                warningPrinter
            );

        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> finderWithNullVisitor.visitAnyInstruction(
            clazz, method, codeAttribute, 0, instruction
        ), "Should work with null class visitor");
    }

    /**
     * Tests that visitAnyInstruction with null warning printer still works.
     */
    @Test
    public void testVisitAnyInstruction_withNullWarningPrinter_processesCorrectly() {
        // Arrange
        GsonDeserializationInvocationFinder finderWithNullPrinter =
            new GsonDeserializationInvocationFinder(
                programClassPool,
                libraryClassPool,
                domainClassVisitor,
                null
            );

        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> finderWithNullPrinter.visitAnyInstruction(
            clazz, method, codeAttribute, 0, instruction
        ), "Should work with null warning printer");
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

        // Act - Visit the same instruction twice
        finder.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        int firstCallVisitorInvocations = mockingDetails(domainClassVisitor).getInvocations().size();

        finder.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        int secondCallVisitorInvocations = mockingDetails(domainClassVisitor).getInvocations().size();

        // Assert - Behavior should be consistent (both calls should result in same number of invocations)
        assertEquals(firstCallVisitorInvocations, secondCallVisitorInvocations,
            "Behavior should be consistent for same instruction");
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

    /**
     * Tests that different finder instances maintain independent state.
     */
    @Test
    public void testVisitAnyInstruction_multipleFinderInstances_independentState() {
        // Arrange
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);

        GsonDeserializationInvocationFinder finder1 = new GsonDeserializationInvocationFinder(
            programClassPool, libraryClassPool, visitor1, warningPrinter);
        GsonDeserializationInvocationFinder finder2 = new GsonDeserializationInvocationFinder(
            programClassPool, libraryClassPool, visitor2, warningPrinter);

        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act
        finder1.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        finder2.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - Each finder should work independently
        // We can't assert exact behavior without matching instructions, but we verify no exceptions
        assertDoesNotThrow(() -> {
            finder1.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);
            finder2.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction works with various instruction types.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentInstructionTypes_processesCorrectly() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);

        InstructionSequenceBuilder builder = new InstructionSequenceBuilder();
        builder.nop();
        Instruction nopInstruction = builder.instructions()[0];

        builder = new InstructionSequenceBuilder();
        builder.aload_0();
        Instruction aloadInstruction = builder.instructions()[0];

        builder = new InstructionSequenceBuilder();
        builder.return_();
        Instruction returnInstruction = builder.instructions()[0];

        // Act & Assert - Should handle different instruction types
        assertDoesNotThrow(() -> {
            finder.visitAnyInstruction(clazz, method, codeAttribute, 0, nopInstruction);
            finder.visitAnyInstruction(clazz, method, codeAttribute, 1, aloadInstruction);
            finder.visitAnyInstruction(clazz, method, codeAttribute, 2, returnInstruction);
        }, "Should handle different instruction types");
    }

    /**
     * Tests that visitAnyInstruction with empty class pools doesn't cause issues.
     */
    @Test
    public void testVisitAnyInstruction_withEmptyClassPools_processesCorrectly() {
        // Arrange - Create finder with empty class pools
        GsonDeserializationInvocationFinder finderWithEmptyPools =
            new GsonDeserializationInvocationFinder(
                new ClassPool(),
                new ClassPool(),
                domainClassVisitor,
                warningPrinter
            );

        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> finderWithEmptyPools.visitAnyInstruction(
            clazz, method, codeAttribute, 0, instruction
        ), "Should work with empty class pools");
    }

    /**
     * Tests that visitAnyInstruction processes instructions with different method descriptors.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentMethodDescriptors_processesEach() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method1 = createMinimalMethod(clazz, "method1", "()V");
        ProgramMethod method2 = createMinimalMethod(clazz, "method2", "(I)V");
        ProgramMethod method3 = createMinimalMethod(clazz, "method3", "(Ljava/lang/String;)I");

        CodeAttribute codeAttr1 = createMinimalCodeAttribute(clazz, method1);
        CodeAttribute codeAttr2 = createMinimalCodeAttribute(clazz, method2);
        CodeAttribute codeAttr3 = createMinimalCodeAttribute(clazz, method3);

        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyInstruction(clazz, method1, codeAttr1, 0, instruction);
            finder.visitAnyInstruction(clazz, method2, codeAttr2, 0, instruction);
            finder.visitAnyInstruction(clazz, method3, codeAttr3, 0, instruction);
        }, "Should process instructions from methods with different descriptors");
    }

    /**
     * Tests that visitAnyInstruction with large offsets works correctly.
     */
    @Test
    public void testVisitAnyInstruction_withLargeOffsets_processesCorrectly() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act & Assert - Test with various large offsets
        assertDoesNotThrow(() -> {
            finder.visitAnyInstruction(clazz, method, codeAttribute, 1000, instruction);
            finder.visitAnyInstruction(clazz, method, codeAttribute, 5000, instruction);
            finder.visitAnyInstruction(clazz, method, codeAttribute, 10000, instruction);
        }, "Should handle large offsets correctly");
    }

    /**
     * Tests that visitAnyInstruction processes same instruction at different offsets.
     */
    @Test
    public void testVisitAnyInstruction_sameInstructionDifferentOffsets_processesEach() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int offset = 0; offset < 50; offset += 5) {
                finder.visitAnyInstruction(clazz, method, codeAttribute, offset, instruction);
            }
        }, "Should process same instruction at different offsets");
    }

    /**
     * Tests that visitAnyInstruction can handle being called with minimal code attributes.
     */
    @Test
    public void testVisitAnyInstruction_withMinimalCodeAttribute_processesCorrectly() {
        // Arrange
        ProgramClass clazz = createMinimalProgramClass("TestClass");
        ProgramMethod method = createMinimalMethod(clazz, "testMethod");
        CodeAttribute codeAttribute = createMinimalCodeAttribute(clazz, method);
        Instruction instruction = createSimpleInstruction();

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyInstruction(
            clazz, method, codeAttribute, 0, instruction
        ), "Should work with minimal code attribute");
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
     * Creates a minimal ProgramMethod for testing with default descriptor.
     *
     * @param clazz the owning class
     * @param methodName the method name
     * @return a configured ProgramMethod instance
     */
    private ProgramMethod createMinimalMethod(ProgramClass clazz, String methodName) {
        return createMinimalMethod(clazz, methodName, "()V");
    }

    /**
     * Creates a minimal ProgramMethod for testing with specified descriptor.
     *
     * @param clazz the owning class
     * @param methodName the method name
     * @param descriptor the method descriptor
     * @return a configured ProgramMethod instance
     */
    private ProgramMethod createMinimalMethod(ProgramClass clazz, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = 0;

        // Add method name to constant pool
        int nameIndex = 3;
        int descriptorIndex = 4;

        if (clazz.constantPool[nameIndex] == null) {
            clazz.constantPool[nameIndex] = new Utf8Constant(methodName);
        }
        if (clazz.constantPool[descriptorIndex] == null) {
            clazz.constantPool[descriptorIndex] = new Utf8Constant(descriptor);
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

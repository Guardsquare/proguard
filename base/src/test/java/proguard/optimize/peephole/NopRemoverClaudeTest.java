package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NopRemover}.
 *
 * Tests all methods in NopRemover:
 * - Constructor with CodeAttributeEditor
 * - Constructor with CodeAttributeEditor and InstructionVisitor
 * - visitAnyInstruction (no-op method)
 * - visitSimpleInstruction (removes NOP instructions)
 */
public class NopRemoverClaudeTest {

    private NopRemover nopRemover;
    private CodeAttributeEditor mockCodeAttributeEditor;
    private InstructionVisitor mockExtraVisitor;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        mockCodeAttributeEditor = mock(CodeAttributeEditor.class);
        mockExtraVisitor = mock(InstructionVisitor.class);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    // ========================================
    // Constructor Tests - Single Parameter
    // ========================================

    /**
     * Tests the single-parameter constructor with a valid CodeAttributeEditor.
     * Verifies that the NopRemover can be instantiated successfully.
     */
    @Test
    public void testConstructor_withValidCodeAttributeEditor_createsInstance() {
        // Act
        nopRemover = new NopRemover(mockCodeAttributeEditor);

        // Assert
        assertNotNull(nopRemover, "NopRemover should be created successfully");
    }

    /**
     * Tests the single-parameter constructor with null CodeAttributeEditor.
     * Verifies that the NopRemover can be instantiated with null parameter.
     */
    @Test
    public void testConstructor_withNullCodeAttributeEditor_createsInstance() {
        // Act
        nopRemover = new NopRemover(null);

        // Assert
        assertNotNull(nopRemover, "NopRemover should be created with null CodeAttributeEditor");
    }

    /**
     * Tests that the single-parameter constructor creates an InstructionVisitor.
     * Verifies that NopRemover implements the InstructionVisitor interface.
     */
    @Test
    public void testConstructor_createsInstanceOfInstructionVisitor() {
        // Act
        nopRemover = new NopRemover(mockCodeAttributeEditor);

        // Assert
        assertInstanceOf(InstructionVisitor.class, nopRemover,
            "NopRemover should implement InstructionVisitor interface");
    }

    /**
     * Tests that the constructor doesn't invoke any methods on the CodeAttributeEditor.
     * Verifies that the constructor only stores the parameter without using it.
     */
    @Test
    public void testConstructor_doesNotInvokeCodeAttributeEditor() {
        // Act
        nopRemover = new NopRemover(mockCodeAttributeEditor);

        // Assert
        assertNotNull(nopRemover, "NopRemover should be created");
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests creating multiple NopRemover instances with the same CodeAttributeEditor.
     * Verifies that multiple instances can be created using the same editor.
     */
    @Test
    public void testConstructor_multipleInstancesWithSameEditor_createsIndependentInstances() {
        // Act
        NopRemover remover1 = new NopRemover(mockCodeAttributeEditor);
        NopRemover remover2 = new NopRemover(mockCodeAttributeEditor);

        // Assert
        assertNotNull(remover1, "First remover should be created");
        assertNotNull(remover2, "Second remover should be created");
        assertNotSame(remover1, remover2, "Remover instances should be different");
    }

    /**
     * Tests that the single-parameter constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructor_isEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        nopRemover = new NopRemover(mockCodeAttributeEditor);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(nopRemover, "NopRemover should be created");
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    // ========================================
    // Constructor Tests - Two Parameters
    // ========================================

    /**
     * Tests the two-parameter constructor with valid parameters.
     * Verifies that the NopRemover can be instantiated with both parameters.
     */
    @Test
    public void testConstructor_withBothParameters_createsInstance() {
        // Act
        nopRemover = new NopRemover(mockCodeAttributeEditor, mockExtraVisitor);

        // Assert
        assertNotNull(nopRemover, "NopRemover should be created successfully");
    }

    /**
     * Tests the two-parameter constructor with null extra visitor.
     * Verifies that the NopRemover can be instantiated with null extra visitor.
     */
    @Test
    public void testConstructor_withNullExtraVisitor_createsInstance() {
        // Act
        nopRemover = new NopRemover(mockCodeAttributeEditor, null);

        // Assert
        assertNotNull(nopRemover, "NopRemover should be created with null extra visitor");
    }

    /**
     * Tests the two-parameter constructor with both parameters null.
     * Verifies that the NopRemover can be instantiated with all null parameters.
     */
    @Test
    public void testConstructor_withBothParametersNull_createsInstance() {
        // Act
        nopRemover = new NopRemover(null, null);

        // Assert
        assertNotNull(nopRemover, "NopRemover should be created with null parameters");
    }

    /**
     * Tests that the two-parameter constructor doesn't invoke any methods on parameters.
     * Verifies that the constructor only stores the parameters without using them.
     */
    @Test
    public void testConstructor_twoParameters_doesNotInvokeParameters() {
        // Act
        nopRemover = new NopRemover(mockCodeAttributeEditor, mockExtraVisitor);

        // Assert
        assertNotNull(nopRemover, "NopRemover should be created");
        verifyNoInteractions(mockCodeAttributeEditor);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that the two-parameter constructor creates an InstructionVisitor.
     * Verifies that NopRemover implements the InstructionVisitor interface.
     */
    @Test
    public void testConstructor_twoParameters_createsInstanceOfInstructionVisitor() {
        // Act
        nopRemover = new NopRemover(mockCodeAttributeEditor, mockExtraVisitor);

        // Assert
        assertInstanceOf(InstructionVisitor.class, nopRemover,
            "NopRemover should implement InstructionVisitor interface");
    }

    /**
     * Tests creating multiple NopRemover instances with different parameters.
     * Verifies that instances are independent.
     */
    @Test
    public void testConstructor_twoParameters_multipleInstancesAreIndependent() {
        // Arrange
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        InstructionVisitor visitor2 = mock(InstructionVisitor.class);

        // Act
        NopRemover remover1 = new NopRemover(mockCodeAttributeEditor, visitor1);
        NopRemover remover2 = new NopRemover(mockCodeAttributeEditor, visitor2);

        // Assert
        assertNotNull(remover1, "First remover should be created");
        assertNotNull(remover2, "Second remover should be created");
        assertNotSame(remover1, remover2, "Remover instances should be different");
    }

    // ========================================
    // visitAnyInstruction Tests (No-op Method)
    // ========================================

    /**
     * Tests that visitAnyInstruction can be called without throwing exceptions.
     * This is a no-op method that should do nothing.
     */
    @Test
    public void testVisitAnyInstruction_withValidParameters_doesNotThrowException() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        Instruction instruction = new SimpleInstruction(Instruction.OP_ICONST_0);

        // Act & Assert
        assertDoesNotThrow(() -> nopRemover.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with any parameters.
     * Since it's a no-op, it should not call any methods on the parameters.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithParameters() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        Instruction instruction = new SimpleInstruction(Instruction.OP_ICONST_0);

        // Act
        nopRemover.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests that visitAnyInstruction with null parameters doesn't throw exceptions.
     * The no-op method should handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyInstruction_withNullParameters_doesNotThrowException() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);

        // Act & Assert
        assertDoesNotThrow(() -> nopRemover.visitAnyInstruction(null, null, null, 0, null));
    }

    /**
     * Tests that visitAnyInstruction can be called multiple times.
     * The method should be idempotent.
     */
    @Test
    public void testVisitAnyInstruction_calledMultipleTimes_doesNotThrowException() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        Instruction instruction = new SimpleInstruction(Instruction.OP_ICONST_0);

        // Act & Assert
        assertDoesNotThrow(() -> {
            nopRemover.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
            nopRemover.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);
            nopRemover.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction doesn't trigger the extra visitor.
     * The no-op method should not use the extra visitor.
     */
    @Test
    public void testVisitAnyInstruction_withExtraVisitor_doesNotTriggerVisitor() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor, mockExtraVisitor);
        Instruction instruction = new SimpleInstruction(Instruction.OP_ICONST_0);

        // Act
        nopRemover.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(mockExtraVisitor);
    }

    // ========================================
    // visitSimpleInstruction Tests - Non-NOP Instructions
    // ========================================

    /**
     * Tests that visitSimpleInstruction with a non-NOP instruction doesn't delete it.
     * Only NOP instructions should be deleted.
     */
    @Test
    public void testVisitSimpleInstruction_withNonNopInstruction_doesNotDelete() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ICONST_0);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(mockCodeAttributeEditor, never()).deleteInstruction(anyInt());
    }

    /**
     * Tests that visitSimpleInstruction with various non-NOP opcodes doesn't delete them.
     * Verifies that only NOP is targeted.
     */
    @Test
    public void testVisitSimpleInstruction_withVariousNonNopOpcodes_doesNotDelete() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        byte[] nonNopOpcodes = {
            Instruction.OP_ACONST_NULL,
            Instruction.OP_ICONST_0,
            Instruction.OP_ICONST_1,
            Instruction.OP_RETURN,
            Instruction.OP_ARETURN,
            Instruction.OP_POP,
            Instruction.OP_DUP
        };

        // Act & Assert
        for (byte opcode : nonNopOpcodes) {
            SimpleInstruction instruction = new SimpleInstruction(opcode);
            nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);
        }

        verify(mockCodeAttributeEditor, never()).deleteInstruction(anyInt());
    }

    // ========================================
    // visitSimpleInstruction Tests - NOP Instructions
    // ========================================

    /**
     * Tests that visitSimpleInstruction with a NOP instruction deletes it.
     * The primary purpose of NopRemover is to remove NOP instructions.
     */
    @Test
    public void testVisitSimpleInstruction_withNopInstruction_deletesInstruction() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(0)).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(0);
    }

    /**
     * Tests that visitSimpleInstruction deletes NOP at different offsets.
     * Verifies that the offset is correctly passed to deleteInstruction.
     */
    @Test
    public void testVisitSimpleInstruction_withNopAtDifferentOffsets_deletesCorrectOffset() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 10, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 42, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(0);
        verify(mockCodeAttributeEditor).deleteInstruction(10);
        verify(mockCodeAttributeEditor).deleteInstruction(42);
    }

    /**
     * Tests that visitSimpleInstruction doesn't delete NOP if already modified.
     * This prevents double-deletion or conflicting modifications.
     */
    @Test
    public void testVisitSimpleInstruction_withNopAlreadyModified_doesNotDelete() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(0)).thenReturn(true);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor, never()).deleteInstruction(anyInt());
    }

    /**
     * Tests that visitSimpleInstruction checks isModified before deleting.
     * Verifies that the check is performed for each NOP instruction.
     */
    @Test
    public void testVisitSimpleInstruction_withNopInstruction_checksIsModified() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(5)).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 5, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).isModified(5);
    }

    /**
     * Tests that visitSimpleInstruction deletes multiple NOPs if none are modified.
     * Verifies that all unmodified NOPs are deleted.
     */
    @Test
    public void testVisitSimpleInstruction_withMultipleUnmodifiedNops_deletesAll() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 5, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 10, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(0);
        verify(mockCodeAttributeEditor).deleteInstruction(5);
        verify(mockCodeAttributeEditor).deleteInstruction(10);
    }

    /**
     * Tests that visitSimpleInstruction with mixed modified and unmodified NOPs
     * only deletes the unmodified ones.
     */
    @Test
    public void testVisitSimpleInstruction_withMixedModifiedNops_deletesOnlyUnmodified() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(0)).thenReturn(false);
        when(mockCodeAttributeEditor.isModified(5)).thenReturn(true);
        when(mockCodeAttributeEditor.isModified(10)).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 5, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 10, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(0);
        verify(mockCodeAttributeEditor, never()).deleteInstruction(5);
        verify(mockCodeAttributeEditor).deleteInstruction(10);
    }

    // ========================================
    // visitSimpleInstruction Tests - Extra Visitor
    // ========================================

    /**
     * Tests that visitSimpleInstruction with extra visitor calls it when NOP is deleted.
     * The extra visitor should be invoked for each deleted NOP.
     */
    @Test
    public void testVisitSimpleInstruction_withExtraVisitor_callsVisitorOnNopDeletion() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor, mockExtraVisitor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(0)).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);

        // Assert
        verify(mockExtraVisitor).visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);
    }

    /**
     * Tests that visitSimpleInstruction with extra visitor doesn't call it for non-NOP.
     * The extra visitor should only be called for deleted NOPs.
     */
    @Test
    public void testVisitSimpleInstruction_withExtraVisitor_doesNotCallForNonNop() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor, mockExtraVisitor);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ICONST_0);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitSimpleInstruction with extra visitor doesn't call it for modified NOP.
     * The extra visitor should only be called for actually deleted NOPs.
     */
    @Test
    public void testVisitSimpleInstruction_withExtraVisitor_doesNotCallForModifiedNop() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor, mockExtraVisitor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(0)).thenReturn(true);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);

        // Assert
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitSimpleInstruction calls extra visitor for multiple deleted NOPs.
     * Verifies that the extra visitor is called for each deletion.
     */
    @Test
    public void testVisitSimpleInstruction_withExtraVisitor_callsForEachDeletedNop() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor, mockExtraVisitor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 5, nopInstruction);

        // Assert
        verify(mockExtraVisitor).visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);
        verify(mockExtraVisitor).visitSimpleInstruction(clazz, method, codeAttribute, 5, nopInstruction);
    }

    /**
     * Tests that visitSimpleInstruction passes correct parameters to extra visitor.
     * Verifies that all parameters are forwarded correctly.
     */
    @Test
    public void testVisitSimpleInstruction_withExtraVisitor_passesCorrectParameters() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor, mockExtraVisitor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(42)).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 42, nopInstruction);

        // Assert
        verify(mockExtraVisitor).visitSimpleInstruction(
            eq(clazz),
            eq(method),
            eq(codeAttribute),
            eq(42),
            eq(nopInstruction)
        );
    }

    /**
     * Tests that visitSimpleInstruction without extra visitor doesn't fail.
     * Verifies that null extra visitor is handled correctly.
     */
    @Test
    public void testVisitSimpleInstruction_withoutExtraVisitor_deletesNopSuccessfully() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor, null);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(0)).thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() -> nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction));
        verify(mockCodeAttributeEditor).deleteInstruction(0);
    }

    // ========================================
    // visitSimpleInstruction Tests - Edge Cases
    // ========================================

    /**
     * Tests that visitSimpleInstruction with NOP at offset 0 works correctly.
     * Verifies handling of the first instruction.
     */
    @Test
    public void testVisitSimpleInstruction_withNopAtOffsetZero_deletesSuccessfully() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(0)).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(0);
    }

    /**
     * Tests that visitSimpleInstruction with large offset values works correctly.
     * Verifies handling of instructions deep in the code.
     */
    @Test
    public void testVisitSimpleInstruction_withNopAtLargeOffset_deletesSuccessfully() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        int largeOffset = 10000;
        when(mockCodeAttributeEditor.isModified(largeOffset)).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, largeOffset, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(largeOffset);
    }

    /**
     * Tests that visitSimpleInstruction is called with different classes.
     * Verifies that the remover works across different classes.
     */
    @Test
    public void testVisitSimpleInstruction_withDifferentClasses_deletesNops() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        when(mockCodeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz1, method, codeAttribute, 0, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz2, method, codeAttribute, 5, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(0);
        verify(mockCodeAttributeEditor).deleteInstruction(5);
    }

    /**
     * Tests that visitSimpleInstruction is called with different methods.
     * Verifies that the remover works across different methods.
     */
    @Test
    public void testVisitSimpleInstruction_withDifferentMethods_deletesNops() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        when(mockCodeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method1, codeAttribute, 0, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method2, codeAttribute, 5, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(0);
        verify(mockCodeAttributeEditor).deleteInstruction(5);
    }

    /**
     * Tests that visitSimpleInstruction is called with different code attributes.
     * Verifies that the remover works across different code attributes.
     */
    @Test
    public void testVisitSimpleInstruction_withDifferentCodeAttributes_deletesNops() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        when(mockCodeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttr1, 0, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttr2, 5, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(0);
        verify(mockCodeAttributeEditor).deleteInstruction(5);
    }

    /**
     * Tests that visitSimpleInstruction handles consecutive NOPs correctly.
     * Verifies that each NOP is processed independently.
     */
    @Test
    public void testVisitSimpleInstruction_withConsecutiveNops_deletesEach() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 1, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 2, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(0);
        verify(mockCodeAttributeEditor).deleteInstruction(1);
        verify(mockCodeAttributeEditor).deleteInstruction(2);
    }

    /**
     * Tests that visitSimpleInstruction handles interleaved NOPs and non-NOPs.
     * Verifies that only NOPs are deleted.
     */
    @Test
    public void testVisitSimpleInstruction_withInterleavedNopsAndNonNops_deletesOnlyNops() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        SimpleInstruction iconst = new SimpleInstruction(Instruction.OP_ICONST_0);
        when(mockCodeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 1, iconst);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 2, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 3, iconst);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(0);
        verify(mockCodeAttributeEditor, never()).deleteInstruction(1);
        verify(mockCodeAttributeEditor).deleteInstruction(2);
        verify(mockCodeAttributeEditor, never()).deleteInstruction(3);
    }

    /**
     * Tests that the NopRemover instance can be reused across multiple visits.
     * Verifies stateless operation.
     */
    @Test
    public void testVisitSimpleInstruction_reusingInstance_worksCorrectly() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act - use same instance multiple times
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 10, nopInstruction);
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 20, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(0);
        verify(mockCodeAttributeEditor).deleteInstruction(10);
        verify(mockCodeAttributeEditor).deleteInstruction(20);
    }

    /**
     * Tests that multiple NopRemover instances work independently.
     * Verifies that instances don't interfere with each other.
     */
    @Test
    public void testVisitSimpleInstruction_multipleInstances_workIndependently() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);
        NopRemover remover1 = new NopRemover(editor1);
        NopRemover remover2 = new NopRemover(editor2);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(editor1.isModified(anyInt())).thenReturn(false);
        when(editor2.isModified(anyInt())).thenReturn(false);

        // Act
        remover1.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);
        remover2.visitSimpleInstruction(clazz, method, codeAttribute, 5, nopInstruction);

        // Assert
        verify(editor1).deleteInstruction(0);
        verify(editor1, never()).deleteInstruction(5);
        verify(editor2, never()).deleteInstruction(0);
        verify(editor2).deleteInstruction(5);
    }

    /**
     * Tests that visitSimpleInstruction with null CodeAttributeEditor handles NOP gracefully.
     * This tests robustness with invalid initialization.
     */
    @Test
    public void testVisitSimpleInstruction_withNullEditor_throwsNullPointerException() {
        // Arrange
        nopRemover = new NopRemover(null);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction),
            "Should throw NullPointerException when editor is null and NOP is encountered");
    }

    /**
     * Tests that visitSimpleInstruction with null editor but non-NOP instruction doesn't throw.
     * Verifies that null editor only matters when deleting NOPs.
     */
    @Test
    public void testVisitSimpleInstruction_withNullEditorAndNonNop_doesNotThrowException() {
        // Arrange
        nopRemover = new NopRemover(null);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ICONST_0);

        // Act & Assert
        assertDoesNotThrow(() ->
            nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    // ========================================
    // Integration Tests
    // ========================================

    /**
     * Tests typical usage pattern: single-parameter constructor and NOP removal.
     * Verifies the common use case.
     */
    @Test
    public void testTypicalUsage_singleParameterConstructor_removesNop() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(10)).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 10, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(10);
    }

    /**
     * Tests typical usage pattern: two-parameter constructor with extra visitor.
     * Verifies the use case with visitor notification.
     */
    @Test
    public void testTypicalUsage_twoParameterConstructor_removesNopAndNotifiesVisitor() {
        // Arrange
        nopRemover = new NopRemover(mockCodeAttributeEditor, mockExtraVisitor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(15)).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 15, nopInstruction);

        // Assert
        verify(mockCodeAttributeEditor).deleteInstruction(15);
        verify(mockExtraVisitor).visitSimpleInstruction(clazz, method, codeAttribute, 15, nopInstruction);
    }

    /**
     * Tests that the constructor-visitor method delegation works correctly.
     * Verifies that single-parameter constructor delegates to two-parameter constructor.
     */
    @Test
    public void testConstructorDelegation_singleToTwoParameter() {
        // Act
        nopRemover = new NopRemover(mockCodeAttributeEditor);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        when(mockCodeAttributeEditor.isModified(0)).thenReturn(false);

        // Act
        nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction);

        // Assert - should work without extra visitor
        verify(mockCodeAttributeEditor).deleteInstruction(0);
        assertDoesNotThrow(() -> nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 0, nopInstruction));
    }
}

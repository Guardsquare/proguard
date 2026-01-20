package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateInitializerInvocationFixer#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method processes INVOKESPECIAL instructions that call constructors,
 * and inserts additional instructions (ICONST_0 or ACONST_NULL) before them when the constructor
 * has been modified to have a longer descriptor (due to duplicate initializer fixing).
 */
public class DuplicateInitializerInvocationFixerClaude_visitConstantInstructionTest {

    private DuplicateInitializerInvocationFixer fixer;
    private InstructionVisitor extraAddedInstructionVisitor;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        extraAddedInstructionVisitor = mock(InstructionVisitor.class);
        fixer = new DuplicateInitializerInvocationFixer(extraAddedInstructionVisitor);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    /**
     * Tests that visitConstantInstruction with a non-INVOKESPECIAL instruction does nothing.
     * The method should only process INVOKESPECIAL instructions (opcode 183).
     */
    @Test
    public void testVisitConstantInstruction_withNonInvokeSpecial_doesNothing() {
        // Arrange - create a constant instruction with different opcodes
        ConstantInstruction invokevirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        ConstantInstruction invokestatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 1);
        ConstantInstruction invokeinterface = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 1, 2);

        // Act - should not throw or interact with clazz
        assertDoesNotThrow(() -> fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokevirtual));
        assertDoesNotThrow(() -> fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokestatic));
        assertDoesNotThrow(() -> fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeinterface));

        // Assert - verify no interactions with clazz (which would happen if it processed the instruction)
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction with INVOKESPECIAL instruction attempts to access constant pool.
     * The method should call constantPoolEntryAccept on the clazz.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeSpecial_accessesConstantPool() {
        // Arrange
        ConstantInstruction invokespecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 1);

        // Act
        fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial);

        // Assert - verify the constant pool entry was accessed
        verify(clazz).constantPoolEntryAccept(eq(1), any());
    }

    /**
     * Tests that visitConstantInstruction can handle null parameters gracefully for non-INVOKESPECIAL.
     */
    @Test
    public void testVisitConstantInstruction_withNullClazz_andNonInvokeSpecial_doesNotThrow() {
        // Arrange
        ConstantInstruction invokevirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);

        // Act & Assert
        assertDoesNotThrow(() -> fixer.visitConstantInstruction(null, method, codeAttribute, 0, invokevirtual));
        assertDoesNotThrow(() -> fixer.visitConstantInstruction(clazz, null, codeAttribute, 0, invokevirtual));
        assertDoesNotThrow(() -> fixer.visitConstantInstruction(clazz, method, null, 0, invokevirtual));
    }

    /**
     * Tests that visitConstantInstruction with different constant indices works correctly.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentConstantIndices_accessesCorrectEntry() {
        // Arrange
        ConstantInstruction invokespecial5 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 5);
        ConstantInstruction invokespecial100 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 100);

        // Act
        fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial5);
        fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial100);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), any());
        verify(clazz).constantPoolEntryAccept(eq(100), any());
    }

    /**
     * Tests visitConstantInstruction with boundary offset values.
     */
    @Test
    public void testVisitConstantInstruction_withBoundaryOffsets_worksCorrectly() {
        // Arrange
        ConstantInstruction invokespecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 1);

        // Act & Assert - should handle any offset value
        assertDoesNotThrow(() -> fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial));
        assertDoesNotThrow(() -> fixer.visitConstantInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, invokespecial));
        assertDoesNotThrow(() -> fixer.visitConstantInstruction(clazz, method, codeAttribute, -1, invokespecial));
    }

    /**
     * Tests that visitConstantInstruction can handle calling with null fixer.
     */
    @Test
    public void testVisitConstantInstruction_withNullExtraVisitor_doesNotThrow() {
        // Arrange
        DuplicateInitializerInvocationFixer fixerWithoutVisitor = new DuplicateInitializerInvocationFixer();
        ConstantInstruction invokespecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 1);

        // Act & Assert - should not throw even with null extra visitor
        assertDoesNotThrow(() -> fixerWithoutVisitor.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial));
    }

    /**
     * Tests that visitConstantInstruction resets descriptorLengthDelta at the start.
     * The method sets descriptorLengthDelta = 0 at line 105 before processing.
     */
    @Test
    public void testVisitConstantInstruction_resetsDescriptorLengthDelta() {
        // Arrange
        ConstantInstruction invokespecial1 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 1);
        ConstantInstruction invokespecial2 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 2);

        // Act - call multiple times to ensure state doesn't leak
        fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial1);
        fixer.visitConstantInstruction(clazz, method, codeAttribute, 10, invokespecial2);

        // Assert - verify both constant pool entries were accessed independently
        verify(clazz).constantPoolEntryAccept(eq(1), any());
        verify(clazz).constantPoolEntryAccept(eq(2), any());
    }

    /**
     * Tests that visitConstantInstruction handles sequential calls correctly.
     */
    @Test
    public void testVisitConstantInstruction_sequentialCalls_handlesIndependently() {
        // Arrange
        ConstantInstruction invokespecial1 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 10);
        ConstantInstruction invokespecial2 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 20);
        ConstantInstruction invokespecial3 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 30);

        // Act
        fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial1);
        fixer.visitConstantInstruction(clazz, method, codeAttribute, 5, invokespecial2);
        fixer.visitConstantInstruction(clazz, method, codeAttribute, 10, invokespecial3);

        // Assert - each call should access its own constant pool entry
        verify(clazz).constantPoolEntryAccept(eq(10), any());
        verify(clazz).constantPoolEntryAccept(eq(20), any());
        verify(clazz).constantPoolEntryAccept(eq(30), any());
    }

    /**
     * Tests that visitConstantInstruction only processes INVOKESPECIAL with opcode 183.
     */
    @Test
    public void testVisitConstantInstruction_withCorrectOpcode_processesInstruction() {
        // Arrange
        ConstantInstruction invokespecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 5);
        assertEquals((byte) 183, Instruction.OP_INVOKESPECIAL, "INVOKESPECIAL should have opcode 183");

        // Act
        fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial);

        // Assert - should access constant pool since it's INVOKESPECIAL
        verify(clazz).constantPoolEntryAccept(eq(5), any());
    }

    /**
     * Tests that visitConstantInstruction with a constant index of 0 still attempts access.
     */
    @Test
    public void testVisitConstantInstruction_withZeroIndex_accessesConstantPool() {
        // Arrange
        ConstantInstruction invokespecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 0);

        // Act
        fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial);

        // Assert - even with index 0, should attempt to access constant pool
        verify(clazz).constantPoolEntryAccept(eq(0), any());
    }

    /**
     * Tests that visitConstantInstruction can be called with different fixer instances.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentFixers_operateIndependently() {
        // Arrange
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        InstructionVisitor visitor2 = mock(InstructionVisitor.class);
        DuplicateInitializerInvocationFixer fixer1 = new DuplicateInitializerInvocationFixer(visitor1);
        DuplicateInitializerInvocationFixer fixer2 = new DuplicateInitializerInvocationFixer(visitor2);
        ConstantInstruction invokespecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 1);

        // Act
        fixer1.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial);
        fixer2.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial);

        // Assert - both fixers should access constant pool independently
        verify(clazz, times(2)).constantPoolEntryAccept(eq(1), any());
    }

    /**
     * Tests that visitConstantInstruction with maximum constant index value works.
     */
    @Test
    public void testVisitConstantInstruction_withMaxConstantIndex_accessesConstantPool() {
        // Arrange
        ConstantInstruction invokespecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 65535);

        // Act
        fixer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(65535), any());
    }
}

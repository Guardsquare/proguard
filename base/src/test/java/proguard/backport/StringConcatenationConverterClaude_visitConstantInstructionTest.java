package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link StringConcatenationConverter#visitConstantInstruction}.
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/Method;Lproguard/classfile/attribute/CodeAttribute;ILproguard/classfile/instruction/ConstantInstruction;)V
 *
 * Note: The visitConstantInstruction method is complex and requires proper setup of InvokeDynamicConstant,
 * BootstrapMethodsAttribute, and string concatenation recipes. These tests focus on the testable aspects
 * of the method including non-INVOKEDYNAMIC instructions and basic flow control.
 */
public class StringConcatenationConverterClaude_visitConstantInstructionTest {

    private StringConcatenationConverter converter;
    private CodeAttributeEditor codeAttributeEditor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        codeAttributeEditor = mock(CodeAttributeEditor.class);
        extraInstructionVisitor = mock(InstructionVisitor.class);
        converter = new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    /**
     * Tests visitConstantInstruction with a non-INVOKEDYNAMIC instruction (LDC).
     * The method should do nothing when the instruction opcode is not OP_INVOKEDYNAMIC.
     * This covers line 92 where the opcode check occurs.
     */
    @Test
    public void testVisitConstantInstruction_withLdcInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_LDC, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions with code editor or extra visitor
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with an LDC_W instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withLdcWInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_LDC_W, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions with code editor or extra visitor
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with an LDC2_W instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withLdc2WInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_LDC2_W, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with a GETSTATIC instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withGetstaticInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with a PUTSTATIC instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withPutstaticInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with a GETFIELD instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withGetfieldInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_GETFIELD, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with a PUTFIELD instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withPutfieldInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with an INVOKEVIRTUAL instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withInvokevirtualInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with an INVOKESPECIAL instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withInvokespecialInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with an INVOKESTATIC instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withInvokestaticInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with an INVOKEINTERFACE instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeinterfaceInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with a NEW instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withNewInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_NEW, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with an ANEWARRAY instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withAnewarrayInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with a CHECKCAST instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withCheckcastInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with an INSTANCEOF instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withInstanceofInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with a MULTIANEWARRAY instruction.
     * The method should do nothing when the instruction is not INVOKEDYNAMIC.
     */
    @Test
    public void testVisitConstantInstruction_withMultianewarrayInstruction_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 0);

        // Act
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction called multiple times with different non-INVOKEDYNAMIC instructions.
     * The method should consistently do nothing for all non-INVOKEDYNAMIC instructions.
     */
    @Test
    public void testVisitConstantInstruction_multipleCallsWithDifferentOpcodes_doesNothing() {
        // Arrange & Act - Call multiple times with different opcodes
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 0);
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);

        ConstantInstruction invokevirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 0);
        converter.visitConstantInstruction(clazz, method, codeAttribute, 1, invokevirtual);

        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 0);
        converter.visitConstantInstruction(clazz, method, codeAttribute, 2, newInst);

        // Assert - no interactions for any call
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with different offsets.
     * The offset parameter should not affect behavior for non-INVOKEDYNAMIC instructions.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentOffsets_doesNothing() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_LDC, 0);

        // Act - Call with different offsets
        converter.visitConstantInstruction(clazz, method, codeAttribute, 0, constantInstruction);
        converter.visitConstantInstruction(clazz, method, codeAttribute, 10, constantInstruction);
        converter.visitConstantInstruction(clazz, method, codeAttribute, 100, constantInstruction);
        converter.visitConstantInstruction(clazz, method, codeAttribute, 65535, constantInstruction);

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with different constant indices.
     * The constant index should not affect behavior for non-INVOKEDYNAMIC instructions.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentConstantIndices_doesNothing() {
        // Arrange & Act
        for (int i = 0; i < 5; i++) {
            ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_LDC, i);
            converter.visitConstantInstruction(clazz, method, codeAttribute, i, constantInstruction);
        }

        // Assert - no interactions
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with null clazz parameter and non-INVOKEDYNAMIC instruction.
     * The method should not throw an exception for non-INVOKEDYNAMIC instructions even with null clazz.
     */
    @Test
    public void testVisitConstantInstruction_withNullClazzAndNonInvokeDynamic_doesNotThrow() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_LDC, 0);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> converter.visitConstantInstruction(
                null, method, codeAttribute, 0, constantInstruction));
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with null method parameter and non-INVOKEDYNAMIC instruction.
     * The method should not throw an exception for non-INVOKEDYNAMIC instructions even with null method.
     */
    @Test
    public void testVisitConstantInstruction_withNullMethodAndNonInvokeDynamic_doesNotThrow() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_LDC, 0);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> converter.visitConstantInstruction(
                clazz, null, codeAttribute, 0, constantInstruction));
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with null codeAttribute parameter and non-INVOKEDYNAMIC instruction.
     * The method should not throw an exception for non-INVOKEDYNAMIC instructions even with null codeAttribute.
     */
    @Test
    public void testVisitConstantInstruction_withNullCodeAttributeAndNonInvokeDynamic_doesNotThrow() {
        // Arrange
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_LDC, 0);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> converter.visitConstantInstruction(
                clazz, method, null, 0, constantInstruction));
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests visitConstantInstruction with converter created with null extraInstructionVisitor.
     * The method should work correctly even when extraInstructionVisitor is null.
     */
    @Test
    public void testVisitConstantInstruction_withNullExtraVisitorInConverter_doesNotThrow() {
        // Arrange
        converter = new StringConcatenationConverter(null, codeAttributeEditor);
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_LDC, 0);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> converter.visitConstantInstruction(
                clazz, method, codeAttribute, 0, constantInstruction));
        verifyNoInteractions(codeAttributeEditor);
    }

    /**
     * Tests visitConstantInstruction with converter created with null codeAttributeEditor.
     * The method should work correctly for non-INVOKEDYNAMIC instructions even when codeAttributeEditor is null.
     */
    @Test
    public void testVisitConstantInstruction_withNullCodeEditorInConverter_doesNotThrow() {
        // Arrange
        converter = new StringConcatenationConverter(extraInstructionVisitor, null);
        ConstantInstruction constantInstruction = new ConstantInstruction(Instruction.OP_LDC, 0);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> converter.visitConstantInstruction(
                clazz, method, codeAttribute, 0, constantInstruction));
        verifyNoInteractions(extraInstructionVisitor);
    }
}

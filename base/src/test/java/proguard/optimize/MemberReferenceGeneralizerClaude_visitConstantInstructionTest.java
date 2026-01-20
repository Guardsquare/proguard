package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MemberReferenceGeneralizer#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method processes specific constant instruction opcodes:
 * - OP_GETFIELD (180): Field access instruction - processed if fieldGeneralizationClass is enabled
 * - OP_PUTFIELD (181): Field write instruction - processed if fieldGeneralizationClass is enabled
 * - OP_INVOKEVIRTUAL (182): Virtual method invocation - processed if methodGeneralizationClass is enabled
 *
 * For these instructions, the method:
 * 1. Checks if the corresponding generalization flag is enabled
 * 2. Checks if the code attribute editor has not already modified this offset
 * 3. Stores the invocation offset and opcode in internal fields
 * 4. Accepts the constant pool entry as a visitor (triggering further processing)
 * 5. If generalization occurred and an extra visitor is present, invokes the extra visitor
 *
 * All other instruction opcodes are ignored (no-op).
 */
public class MemberReferenceGeneralizerClaude_visitConstantInstructionTest {

    private MemberReferenceGeneralizer generalizer;
    private CodeAttributeEditor codeAttributeEditor;
    private InstructionVisitor extraFieldInstructionVisitor;
    private InstructionVisitor extraMethodInstructionVisitor;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        codeAttributeEditor = mock(CodeAttributeEditor.class);
        extraFieldInstructionVisitor = mock(InstructionVisitor.class);
        extraMethodInstructionVisitor = mock(InstructionVisitor.class);
        generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            extraFieldInstructionVisitor,
            extraMethodInstructionVisitor
        );
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    /**
     * Tests that visitConstantInstruction ignores non-field/method instructions.
     * Instructions that are not GETFIELD, PUTFIELD, or INVOKEVIRTUAL should be no-ops.
     */
    @Test
    public void testVisitConstantInstruction_withOtherOpcodes_doesNothing() {
        // Arrange - create constant instructions with various other opcodes
        ConstantInstruction invokestatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 1);
        ConstantInstruction invokespecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 1);
        ConstantInstruction invokeinterface = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 1, 2);
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction getstatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        ConstantInstruction putstatic = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);
        ConstantInstruction newInstruction = new ConstantInstruction(Instruction.OP_NEW, 1);

        // Act - should not interact with clazz for these opcodes
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokestatic));
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokespecial));
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeinterface));
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc));
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getstatic));
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, putstatic));
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, newInstruction));

        // Assert - verify no interactions occurred (which would indicate processing)
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction processes OP_GETFIELD when field generalization is enabled.
     * The method should access the constant pool entry when processing field access instructions.
     */
    @Test
    public void testVisitConstantInstruction_withGetField_andFieldGeneralizationEnabled_accessesConstantPool() {
        // Arrange
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        when(codeAttributeEditor.isModified(0)).thenReturn(false);

        // Act
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);

        // Assert - verify the constant pool entry was accessed
        verify(clazz).constantPoolEntryAccept(eq(5), eq(generalizer));
    }

    /**
     * Tests that visitConstantInstruction processes OP_PUTFIELD when field generalization is enabled.
     * The method should access the constant pool entry when processing field write instructions.
     */
    @Test
    public void testVisitConstantInstruction_withPutField_andFieldGeneralizationEnabled_accessesConstantPool() {
        // Arrange
        ConstantInstruction putfield = new ConstantInstruction(Instruction.OP_PUTFIELD, 10);
        when(codeAttributeEditor.isModified(0)).thenReturn(false);

        // Act
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, putfield);

        // Assert - verify the constant pool entry was accessed
        verify(clazz).constantPoolEntryAccept(eq(10), eq(generalizer));
    }

    /**
     * Tests that visitConstantInstruction processes OP_INVOKEVIRTUAL when method generalization is enabled.
     * The method should access the constant pool entry when processing virtual method invocations.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeVirtual_andMethodGeneralizationEnabled_accessesConstantPool() {
        // Arrange
        ConstantInstruction invokevirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 15);
        when(codeAttributeEditor.isModified(0)).thenReturn(false);

        // Act
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokevirtual);

        // Assert - verify the constant pool entry was accessed
        verify(clazz).constantPoolEntryAccept(eq(15), eq(generalizer));
    }

    /**
     * Tests that visitConstantInstruction does not process GETFIELD when field generalization is disabled.
     */
    @Test
    public void testVisitConstantInstruction_withGetField_andFieldGeneralizationDisabled_doesNothing() {
        // Arrange
        MemberReferenceGeneralizer generalizerNoField = new MemberReferenceGeneralizer(
            false, // field generalization disabled
            true,
            codeAttributeEditor,
            extraFieldInstructionVisitor,
            extraMethodInstructionVisitor
        );
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);

        // Act
        generalizerNoField.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);

        // Assert - verify no constant pool access occurred
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process PUTFIELD when field generalization is disabled.
     */
    @Test
    public void testVisitConstantInstruction_withPutField_andFieldGeneralizationDisabled_doesNothing() {
        // Arrange
        MemberReferenceGeneralizer generalizerNoField = new MemberReferenceGeneralizer(
            false, // field generalization disabled
            true,
            codeAttributeEditor,
            extraFieldInstructionVisitor,
            extraMethodInstructionVisitor
        );
        ConstantInstruction putfield = new ConstantInstruction(Instruction.OP_PUTFIELD, 10);

        // Act
        generalizerNoField.visitConstantInstruction(clazz, method, codeAttribute, 0, putfield);

        // Assert - verify no constant pool access occurred
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process INVOKEVIRTUAL when method generalization is disabled.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeVirtual_andMethodGeneralizationDisabled_doesNothing() {
        // Arrange
        MemberReferenceGeneralizer generalizerNoMethod = new MemberReferenceGeneralizer(
            true,
            false, // method generalization disabled
            codeAttributeEditor,
            extraFieldInstructionVisitor,
            extraMethodInstructionVisitor
        );
        ConstantInstruction invokevirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 15);

        // Act
        generalizerNoMethod.visitConstantInstruction(clazz, method, codeAttribute, 0, invokevirtual);

        // Assert - verify no constant pool access occurred
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction skips processing if the offset is already modified.
     * This prevents double-processing of instructions that have already been generalized.
     */
    @Test
    public void testVisitConstantInstruction_withGetField_andOffsetAlreadyModified_doesNothing() {
        // Arrange
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        when(codeAttributeEditor.isModified(0)).thenReturn(true); // offset already modified

        // Act
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);

        // Assert - verify no constant pool access occurred
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction skips processing INVOKEVIRTUAL if offset is modified.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeVirtual_andOffsetAlreadyModified_doesNothing() {
        // Arrange
        ConstantInstruction invokevirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 15);
        when(codeAttributeEditor.isModified(0)).thenReturn(true); // offset already modified

        // Act
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, invokevirtual);

        // Assert - verify no constant pool access occurred
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction checks the correct offset with the code attribute editor.
     */
    @Test
    public void testVisitConstantInstruction_withGetField_checksCorrectOffset() {
        // Arrange
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        when(codeAttributeEditor.isModified(42)).thenReturn(false);

        // Act
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 42, getfield);

        // Assert - verify the correct offset was checked
        verify(codeAttributeEditor).isModified(42);
    }

    /**
     * Tests that visitConstantInstruction handles different constant indices correctly.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentConstantIndices_accessesCorrectEntries() {
        // Arrange
        ConstantInstruction getfield1 = new ConstantInstruction(Instruction.OP_GETFIELD, 1);
        ConstantInstruction getfield100 = new ConstantInstruction(Instruction.OP_GETFIELD, 100);
        ConstantInstruction getfield255 = new ConstantInstruction(Instruction.OP_GETFIELD, 255);
        when(codeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield1);
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 10, getfield100);
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 20, getfield255);

        // Assert - verify correct constant pool indices were accessed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(generalizer));
        verify(clazz).constantPoolEntryAccept(eq(100), eq(generalizer));
        verify(clazz).constantPoolEntryAccept(eq(255), eq(generalizer));
    }

    /**
     * Tests that visitConstantInstruction can process multiple instructions in sequence.
     */
    @Test
    public void testVisitConstantInstruction_withMultipleInstructions_processesEachIndependently() {
        // Arrange
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        ConstantInstruction putfield = new ConstantInstruction(Instruction.OP_PUTFIELD, 10);
        ConstantInstruction invokevirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 15);
        when(codeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 3, putfield);
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 6, invokevirtual);

        // Assert - verify all were processed
        verify(clazz).constantPoolEntryAccept(eq(5), eq(generalizer));
        verify(clazz).constantPoolEntryAccept(eq(10), eq(generalizer));
        verify(clazz).constantPoolEntryAccept(eq(15), eq(generalizer));
    }

    /**
     * Tests that visitConstantInstruction works with null extra visitors.
     * The method should still process instructions even without extra visitors.
     */
    @Test
    public void testVisitConstantInstruction_withNullExtraVisitors_processesInstructions() {
        // Arrange
        MemberReferenceGeneralizer generalizerNoVisitors = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            null, // null field visitor
            null  // null method visitor
        );
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        when(codeAttributeEditor.isModified(0)).thenReturn(false);

        // Act - should not throw even with null extra visitors
        assertDoesNotThrow(() -> generalizerNoVisitors.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield));

        // Assert - verify processing occurred
        verify(clazz).constantPoolEntryAccept(eq(5), eq(generalizerNoVisitors));
    }

    /**
     * Tests that visitConstantInstruction handles boundary offset values.
     */
    @Test
    public void testVisitConstantInstruction_withBoundaryOffsets_worksCorrectly() {
        // Arrange
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        when(codeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act & Assert - should handle any offset value
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield));
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, getfield));
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, method, codeAttribute, -1, getfield));
    }

    /**
     * Tests that visitConstantInstruction with both generalization flags disabled does nothing.
     */
    @Test
    public void testVisitConstantInstruction_withBothGeneralizationsDisabled_doesNothing() {
        // Arrange
        MemberReferenceGeneralizer generalizerAllDisabled = new MemberReferenceGeneralizer(
            false, // field generalization disabled
            false, // method generalization disabled
            codeAttributeEditor,
            extraFieldInstructionVisitor,
            extraMethodInstructionVisitor
        );
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        ConstantInstruction invokevirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 15);

        // Act
        generalizerAllDisabled.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);
        generalizerAllDisabled.visitConstantInstruction(clazz, method, codeAttribute, 0, invokevirtual);

        // Assert - verify nothing was processed
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction can be called with null Clazz for non-matching opcodes.
     */
    @Test
    public void testVisitConstantInstruction_withNullClazz_andNonMatchingOpcode_doesNotThrow() {
        // Arrange
        ConstantInstruction invokestatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(null, method, codeAttribute, 0, invokestatic));
    }

    /**
     * Tests that visitConstantInstruction can be called with null Method for non-matching opcodes.
     */
    @Test
    public void testVisitConstantInstruction_withNullMethod_andNonMatchingOpcode_doesNotThrow() {
        // Arrange
        ConstantInstruction invokestatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, null, codeAttribute, 0, invokestatic));
    }

    /**
     * Tests that visitConstantInstruction can be called with null CodeAttribute for non-matching opcodes.
     */
    @Test
    public void testVisitConstantInstruction_withNullCodeAttribute_andNonMatchingOpcode_doesNotThrow() {
        // Arrange
        ConstantInstruction invokestatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(clazz, method, null, 0, invokestatic));
    }

    /**
     * Tests that visitConstantInstruction processes the same offset multiple times if not modified.
     */
    @Test
    public void testVisitConstantInstruction_withSameOffsetMultipleTimes_checksModifiedEachTime() {
        // Arrange
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        when(codeAttributeEditor.isModified(0)).thenReturn(false, false, true); // third time returns true

        // Act - call three times with same offset
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);

        // Assert - verify isModified was checked all three times
        verify(codeAttributeEditor, times(3)).isModified(0);
        // And constant pool was accessed only twice (third time offset was modified)
        verify(clazz, times(2)).constantPoolEntryAccept(eq(5), eq(generalizer));
    }

    /**
     * Tests that visitConstantInstruction handles mixed field and method instructions.
     */
    @Test
    public void testVisitConstantInstruction_withMixedFieldAndMethodInstructions_processesCorrectly() {
        // Arrange
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        ConstantInstruction invokevirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 15);
        ConstantInstruction putfield = new ConstantInstruction(Instruction.OP_PUTFIELD, 10);
        when(codeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 3, invokevirtual);
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 6, putfield);

        // Assert - verify all were processed with correct indices
        verify(clazz, times(3)).constantPoolEntryAccept(anyInt(), eq(generalizer));
        verify(clazz).constantPoolEntryAccept(eq(5), eq(generalizer));
        verify(clazz).constantPoolEntryAccept(eq(15), eq(generalizer));
        verify(clazz).constantPoolEntryAccept(eq(10), eq(generalizer));
    }

    /**
     * Tests that visitConstantInstruction respects field generalization flag for GETFIELD.
     */
    @Test
    public void testVisitConstantInstruction_fieldGeneralizationFlagControlsGetFieldProcessing() {
        // Arrange - create generalizers with different field flags
        MemberReferenceGeneralizer generalizerFieldEnabled = new MemberReferenceGeneralizer(
            true, true, codeAttributeEditor, null, null
        );
        MemberReferenceGeneralizer generalizerFieldDisabled = new MemberReferenceGeneralizer(
            false, true, codeAttributeEditor, null, null
        );
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        when(codeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        generalizerFieldEnabled.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);
        generalizerFieldDisabled.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);

        // Assert - only the enabled one should access constant pool
        verify(clazz, times(1)).constantPoolEntryAccept(eq(5), eq(generalizerFieldEnabled));
    }

    /**
     * Tests that visitConstantInstruction respects method generalization flag for INVOKEVIRTUAL.
     */
    @Test
    public void testVisitConstantInstruction_methodGeneralizationFlagControlsInvokeVirtualProcessing() {
        // Arrange - create generalizers with different method flags
        MemberReferenceGeneralizer generalizerMethodEnabled = new MemberReferenceGeneralizer(
            true, true, codeAttributeEditor, null, null
        );
        MemberReferenceGeneralizer generalizerMethodDisabled = new MemberReferenceGeneralizer(
            true, false, codeAttributeEditor, null, null
        );
        ConstantInstruction invokevirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 15);
        when(codeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act
        generalizerMethodEnabled.visitConstantInstruction(clazz, method, codeAttribute, 0, invokevirtual);
        generalizerMethodDisabled.visitConstantInstruction(clazz, method, codeAttribute, 0, invokevirtual);

        // Assert - only the enabled one should access constant pool
        verify(clazz, times(1)).constantPoolEntryAccept(eq(15), eq(generalizerMethodEnabled));
    }

    /**
     * Tests that visitConstantInstruction does not throw with all null parameters for non-matching opcodes.
     */
    @Test
    public void testVisitConstantInstruction_withAllNullParameters_andNonMatchingOpcode_doesNotThrow() {
        // Arrange
        ConstantInstruction invokestatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> generalizer.visitConstantInstruction(null, null, null, 0, invokestatic));
    }

    /**
     * Tests that visitConstantInstruction with GETFIELD and PUTFIELD both use field generalization flag.
     */
    @Test
    public void testVisitConstantInstruction_bothGetFieldAndPutField_useFieldGeneralizationFlag() {
        // Arrange
        MemberReferenceGeneralizer generalizerFieldDisabled = new MemberReferenceGeneralizer(
            false, true, codeAttributeEditor, null, null
        );
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        ConstantInstruction putfield = new ConstantInstruction(Instruction.OP_PUTFIELD, 10);

        // Act
        generalizerFieldDisabled.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);
        generalizerFieldDisabled.visitConstantInstruction(clazz, method, codeAttribute, 3, putfield);

        // Assert - neither should be processed when field generalization is disabled
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction can handle rapid succession of calls.
     */
    @Test
    public void testVisitConstantInstruction_rapidSuccession_worksCorrectly() {
        // Arrange
        when(codeAttributeEditor.isModified(anyInt())).thenReturn(false);

        // Act - call many times rapidly
        for (int i = 0; i < 100; i++) {
            ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, i % 10);
            generalizer.visitConstantInstruction(clazz, method, codeAttribute, i, getfield);
        }

        // Assert - verify all were processed
        verify(clazz, times(100)).constantPoolEntryAccept(anyInt(), eq(generalizer));
    }

    /**
     * Tests that visitConstantInstruction passes the generalizer itself as the visitor.
     * This is important because the generalizer implements ConstantVisitor.
     */
    @Test
    public void testVisitConstantInstruction_passesGeneralizerAsConstantVisitor() {
        // Arrange
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        when(codeAttributeEditor.isModified(0)).thenReturn(false);

        // Act
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);

        // Assert - verify the generalizer itself was passed as the visitor
        verify(clazz).constantPoolEntryAccept(eq(5), eq(generalizer));
    }

    /**
     * Tests that visitConstantInstruction with different offsets checks each offset independently.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentOffsets_checksEachOffsetIndependently() {
        // Arrange
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        when(codeAttributeEditor.isModified(0)).thenReturn(false);
        when(codeAttributeEditor.isModified(10)).thenReturn(true);
        when(codeAttributeEditor.isModified(20)).thenReturn(false);

        // Act
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 10, getfield);
        generalizer.visitConstantInstruction(clazz, method, codeAttribute, 20, getfield);

        // Assert - verify offsets 0 and 20 were processed, but not 10
        verify(clazz, times(2)).constantPoolEntryAccept(eq(5), eq(generalizer));
        verify(codeAttributeEditor).isModified(0);
        verify(codeAttributeEditor).isModified(10);
        verify(codeAttributeEditor).isModified(20);
    }

    /**
     * Tests visitConstantInstruction with multiple generalizers operating independently.
     */
    @Test
    public void testVisitConstantInstruction_withMultipleGeneralizers_operateIndependently() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);
        MemberReferenceGeneralizer generalizer1 = new MemberReferenceGeneralizer(
            true, true, editor1, null, null
        );
        MemberReferenceGeneralizer generalizer2 = new MemberReferenceGeneralizer(
            true, false, editor2, null, null
        );
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        when(editor1.isModified(anyInt())).thenReturn(false);
        when(editor2.isModified(anyInt())).thenReturn(false);

        // Act
        generalizer1.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);
        generalizer2.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);

        // Assert - both should have processed independently
        verify(clazz).constantPoolEntryAccept(eq(5), eq(generalizer1));
        verify(clazz).constantPoolEntryAccept(eq(5), eq(generalizer2));
    }

    /**
     * Tests that visitConstantInstruction handles the switch statement's default case (other opcodes).
     */
    @Test
    public void testVisitConstantInstruction_switchDefaultCase_doesNothing() {
        // Arrange - test with many different opcodes that should hit the default case
        ConstantInstruction[] otherInstructions = {
            new ConstantInstruction(Instruction.OP_LDC, 1),
            new ConstantInstruction(Instruction.OP_LDC_W, 1),
            new ConstantInstruction(Instruction.OP_LDC2_W, 1),
            new ConstantInstruction(Instruction.OP_GETSTATIC, 1),
            new ConstantInstruction(Instruction.OP_PUTSTATIC, 1),
            new ConstantInstruction(Instruction.OP_INVOKESTATIC, 1),
            new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 1),
            new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 1, 2),
            new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 1),
            new ConstantInstruction(Instruction.OP_NEW, 1),
            new ConstantInstruction(Instruction.OP_ANEWARRAY, 1),
            new ConstantInstruction(Instruction.OP_CHECKCAST, 1),
            new ConstantInstruction(Instruction.OP_INSTANCEOF, 1),
            new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 1, 2)
        };

        // Act - none of these should trigger any processing
        for (ConstantInstruction instruction : otherInstructions) {
            generalizer.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        }

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction completes quickly for matching opcodes.
     */
    @Test
    public void testVisitConstantInstruction_withMatchingOpcodes_executesQuickly() {
        // Arrange
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        when(codeAttributeEditor.isModified(anyInt())).thenReturn(false);
        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 1000; i++) {
            generalizer.visitConstantInstruction(clazz, method, codeAttribute, i, getfield);
        }

        long duration = System.nanoTime() - startTime;
        long durationMs = duration / 1_000_000;

        // Assert - should complete reasonably quickly
        assertTrue(durationMs < 1000, "Should execute 1000 calls within 1 second");
    }
}

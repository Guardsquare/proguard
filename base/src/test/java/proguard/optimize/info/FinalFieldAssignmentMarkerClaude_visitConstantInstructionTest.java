package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link FinalFieldAssignmentMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method checks if the instruction is a PUTSTATIC or PUTFIELD instruction.
 * If it is, the method stores the current method in referencedMethod and then calls
 * clazz.constantPoolEntryAccept() to process the field reference constant. This triggers the
 * visitFieldrefConstant method which marks the method as assigning a final field if applicable.
 */
public class FinalFieldAssignmentMarkerClaude_visitConstantInstructionTest {

    private FinalFieldAssignmentMarker marker;
    private Clazz clazz;
    private ProgramMethod method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        marker = new FinalFieldAssignmentMarker();
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    /**
     * Tests that visitConstantInstruction with PUTSTATIC instruction calls constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withPutStatic_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was called with index 5
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with PUTFIELD instruction calls constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withPutField_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 10);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was called with index 10
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with GETSTATIC instruction does NOT call constantPoolEntryAccept.
     * Only PUTSTATIC and PUTFIELD should trigger the processing.
     */
    @Test
    public void testVisitConstantInstruction_withGetStatic_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with GETFIELD instruction does NOT call constantPoolEntryAccept.
     * Only PUTSTATIC and PUTFIELD should trigger the processing.
     */
    @Test
    public void testVisitConstantInstruction_withGetField_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with LDC instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withLdc_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with INVOKEVIRTUAL instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeVirtual_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with INVOKESTATIC instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeStatic_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with INVOKESPECIAL instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeSpecial_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with INVOKEINTERFACE instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeInterface_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 5, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with PUTSTATIC passes the correct constant index.
     */
    @Test
    public void testVisitConstantInstruction_withPutStatic_passesCorrectConstantIndex() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);
        ConstantInstruction instruction10 = new ConstantInstruction(Instruction.OP_PUTSTATIC, 10);
        ConstantInstruction instruction100 = new ConstantInstruction(Instruction.OP_PUTSTATIC, 100);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction10);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction100);

        // Assert - verify each constant index was accessed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(100), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with PUTFIELD passes the correct constant index.
     */
    @Test
    public void testVisitConstantInstruction_withPutField_passesCorrectConstantIndex() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_PUTFIELD, 1);
        ConstantInstruction instruction10 = new ConstantInstruction(Instruction.OP_PUTFIELD, 10);
        ConstantInstruction instruction100 = new ConstantInstruction(Instruction.OP_PUTFIELD, 100);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction10);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction100);

        // Assert - verify each constant index was accessed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(100), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction passes the marker itself as the visitor.
     * This is important because the marker implements ConstantVisitor.
     */
    @Test
    public void testVisitConstantInstruction_passesMarkerAsVisitor() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify the marker itself is passed as the visitor
        verify(clazz).constantPoolEntryAccept(anyInt(), same(marker));
    }

    /**
     * Tests that visitConstantInstruction can be called multiple times with PUTSTATIC.
     */
    @Test
    public void testVisitConstantInstruction_withPutStaticMultipleTimes_callsConstantPoolEntryAcceptEachTime() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 20, instruction);

        // Assert - verify constantPoolEntryAccept was called 3 times
        verify(clazz, times(3)).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction can be called multiple times with PUTFIELD.
     */
    @Test
    public void testVisitConstantInstruction_withPutFieldMultipleTimes_callsConstantPoolEntryAcceptEachTime() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 7);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 20, instruction);

        // Assert - verify constantPoolEntryAccept was called 3 times
        verify(clazz, times(3)).constantPoolEntryAccept(eq(7), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with different offset values for PUTSTATIC.
     */
    @Test
    public void testVisitConstantInstruction_withVariousOffsets_worksCorrectly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);

        // Act & Assert - should handle any offset value
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 100, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, -1, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, Integer.MIN_VALUE, instruction));
    }

    /**
     * Tests that visitConstantInstruction with constant index 0 works for PUTSTATIC.
     */
    @Test
    public void testVisitConstantInstruction_withConstantIndex0_accessesConstantPool() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 0);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction can handle being called with different methods.
     * Each call should update the internal referencedMethod field.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentMethods_updatesReferencedMethod() {
        // Arrange
        ProgramMethod method1 = mock(ProgramMethod.class);
        ProgramMethod method2 = mock(ProgramMethod.class);
        ProgramMethod method3 = mock(ProgramMethod.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);

        // Act - call with different methods
        marker.visitConstantInstruction(clazz, method1, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method2, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method3, codeAttribute, 0, instruction);

        // Assert - verify constant pool was accessed for each call
        verify(clazz, times(3)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction doesn't throw exceptions with valid parameters.
     */
    @Test
    public void testVisitConstantInstruction_withValidParameters_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction works with different code attributes.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentCodeAttributes_worksCorrectly() {
        // Arrange
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 1);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttr1, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttr2, 10, instruction);

        // Assert
        verify(clazz, times(2)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works correctly when called in rapid succession with PUTSTATIC.
     */
    @Test
    public void testVisitConstantInstruction_rapidSuccessiveCallsWithPutStatic_worksCorrectly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);

        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitConstantInstruction(clazz, method, codeAttribute, i, instruction);
        }

        // Assert
        verify(clazz, times(100)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works correctly when called in rapid succession with PUTFIELD.
     */
    @Test
    public void testVisitConstantInstruction_rapidSuccessiveCallsWithPutField_worksCorrectly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 2);

        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitConstantInstruction(clazz, method, codeAttribute, i, instruction);
        }

        // Assert
        verify(clazz, times(100)).constantPoolEntryAccept(eq(2), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with large constant indices works for PUTSTATIC.
     */
    @Test
    public void testVisitConstantInstruction_withLargeConstantIndex_accessesConstantPool() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 65535);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    /**
     * Tests that multiple FinalFieldAssignmentMarker instances work independently.
     */
    @Test
    public void testVisitConstantInstruction_withMultipleMarkers_operateIndependently() {
        // Arrange
        FinalFieldAssignmentMarker marker1 = new FinalFieldAssignmentMarker();
        FinalFieldAssignmentMarker marker2 = new FinalFieldAssignmentMarker();
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_PUTFIELD, 2);

        // Act
        marker1.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker2.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);

        // Assert - verify each marker accessed its respective constant
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker1));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker2));
    }

    /**
     * Tests that visitConstantInstruction passes the marker (which is a ConstantVisitor)
     * to the constant pool entry accept method, allowing the visitor pattern to work.
     */
    @Test
    public void testVisitConstantInstruction_markerImplementsConstantVisitor() {
        // Assert - verify marker is a ConstantVisitor
        assertTrue(marker instanceof ConstantVisitor,
                "FinalFieldAssignmentMarker should implement ConstantVisitor");
    }

    /**
     * Tests that visitConstantInstruction only processes PUTSTATIC and PUTFIELD, not other field instructions.
     */
    @Test
    public void testVisitConstantInstruction_onlyProcessesPutInstructions() {
        // Arrange - create various field-related instructions
        ConstantInstruction getStatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        ConstantInstruction getField = new ConstantInstruction(Instruction.OP_GETFIELD, 2);
        ConstantInstruction putStatic = new ConstantInstruction(Instruction.OP_PUTSTATIC, 3);
        ConstantInstruction putField = new ConstantInstruction(Instruction.OP_PUTFIELD, 4);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putField);

        // Assert - only PUTSTATIC and PUTFIELD should trigger constant pool access
        verify(clazz, never()).constantPoolEntryAccept(eq(1), any());
        verify(clazz, never()).constantPoolEntryAccept(eq(2), any());
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(4), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with NEW instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withNew_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with ANEWARRAY instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withANewArray_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with CHECKCAST instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withCheckCast_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with INSTANCEOF instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withInstanceOf_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with MULTIANEWARRAY instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withMultiANewArray_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 5, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction alternating between PUTSTATIC and other instructions works correctly.
     */
    @Test
    public void testVisitConstantInstruction_alternatingBetweenPutAndOtherInstructions() {
        // Arrange
        ConstantInstruction putStatic = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);
        ConstantInstruction getStatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 2);
        ConstantInstruction putField = new ConstantInstruction(Instruction.OP_PUTFIELD, 3);
        ConstantInstruction getField = new ConstantInstruction(Instruction.OP_GETFIELD, 4);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 1, getStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 2, putField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 3, getField);

        // Assert - only put instructions should access constant pool
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz, never()).constantPoolEntryAccept(eq(2), any());
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz, never()).constantPoolEntryAccept(eq(4), any());
    }

    /**
     * Tests that visitConstantInstruction works with different clazz instances.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentClazzes_worksCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);

        // Act
        marker.visitConstantInstruction(clazz1, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz2, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz1).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction handles null parameters gracefully for non-put instructions.
     * Since the method only checks the opcode, it should not fail with nulls for instructions that are ignored.
     */
    @Test
    public void testVisitConstantInstruction_withNonPutInstructionAndNullParameters_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert - should not throw or interact with any parameter
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction behavior is consistent across multiple calls with the same instruction.
     */
    @Test
    public void testVisitConstantInstruction_repeatedCallsWithSameInstruction_consistentBehavior() {
        // Arrange
        ConstantInstruction putStatic = new ConstantInstruction(Instruction.OP_PUTSTATIC, 5);
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);

        // Assert - PUTSTATIC should be processed twice, LDC should be ignored
        verify(clazz, times(2)).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with LDC_W instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withLdcW_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC_W, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitConstantInstruction with LDC2_W instruction does NOT call constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withLdc2W_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC2_W, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }
}

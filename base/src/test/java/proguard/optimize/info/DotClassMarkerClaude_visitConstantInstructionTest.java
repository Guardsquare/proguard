package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DotClassMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method processes LDC and LDC_W instructions to detect .class constructs.
 * When it encounters an LDC or LDC_W instruction, it calls constantPoolEntryAccept to process the
 * constant, which eventually marks classes that are referenced via .class constructs.
 */
public class DotClassMarkerClaude_visitConstantInstructionTest {

    private DotClassMarker marker;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        marker = new DotClassMarker();
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    /**
     * Tests that visitConstantInstruction calls constantPoolEntryAccept for OP_LDC instruction.
     * This is the primary behavior - LDC instructions are processed to detect .class constructs.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was called with the correct index and marker as visitor
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction calls constantPoolEntryAccept for OP_LDC_W instruction.
     * LDC_W is the wide version of LDC and should be processed the same way.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdcW_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC_W, 10);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was called
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction does NOT call constantPoolEntryAccept for OP_LDC2_W instruction.
     * LDC2_W loads long/double constants and is not used for .class constructs.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc2W_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC2_W, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process field access instructions.
     * GETSTATIC and similar instructions should be ignored.
     */
    @Test
    public void testVisitConstantInstruction_withFieldAccessOpcodes_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction getStatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        ConstantInstruction putStatic = new ConstantInstruction(Instruction.OP_PUTSTATIC, 2);
        ConstantInstruction getField = new ConstantInstruction(Instruction.OP_GETFIELD, 3);
        ConstantInstruction putField = new ConstantInstruction(Instruction.OP_PUTFIELD, 4);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putField);

        // Assert - verify constantPoolEntryAccept was NOT called
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process method invocation instructions.
     * INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, and INVOKEINTERFACE should be ignored.
     */
    @Test
    public void testVisitConstantInstruction_withMethodInvocationOpcodes_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        ConstantInstruction invokeSpecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 2);
        ConstantInstruction invokeStatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 3);
        ConstantInstruction invokeInterface = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 4, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeSpecial);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeInterface);

        // Assert - verify constantPoolEntryAccept was NOT called
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process object creation instructions.
     * NEW, ANEWARRAY, CHECKCAST, and INSTANCEOF should be ignored.
     */
    @Test
    public void testVisitConstantInstruction_withObjectCreationOpcodes_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 1);
        ConstantInstruction anewArray = new ConstantInstruction(Instruction.OP_ANEWARRAY, 2);
        ConstantInstruction checkCast = new ConstantInstruction(Instruction.OP_CHECKCAST, 3);
        ConstantInstruction instanceOf = new ConstantInstruction(Instruction.OP_INSTANCEOF, 4);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, newInst);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, anewArray);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, checkCast);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instanceOf);

        // Assert - verify constantPoolEntryAccept was NOT called
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction works with different constant indices for OP_LDC.
     * The constant index should be passed through correctly to constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdcAndDifferentIndices_usesCorrectIndex() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_LDC, 0);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_LDC, 100);
        ConstantInstruction instruction4 = new ConstantInstruction(Instruction.OP_LDC, 255);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction3);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction4);

        // Assert - verify each constant index was accessed correctly
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(100), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(255), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with different constant indices for OP_LDC_W.
     * OP_LDC_W uses a 2-byte index and can access a larger constant pool.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdcWAndDifferentIndices_usesCorrectIndex() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_LDC_W, 256);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_LDC_W, 1000);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_LDC_W, 65535);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction3);

        // Assert - verify each constant index was accessed correctly
        verify(clazz).constantPoolEntryAccept(eq(256), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(1000), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with various offset values.
     * The offset parameter should not affect the behavior.
     */
    @Test
    public void testVisitConstantInstruction_withVariousOffsets_behavesConsistently() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act - call with different offsets
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 100, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, -1, instruction);

        // Assert - verify constantPoolEntryAccept was called 4 times with the same index
        verify(clazz, times(4)).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction can be called multiple times with OP_LDC.
     * Each call should independently process the instruction.
     */
    @Test
    public void testVisitConstantInstruction_calledMultipleTimesWithOpLdc_processesEachCall() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_LDC, 2);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_LDC, 3);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 5, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction3);

        // Assert - verify each call processed independently
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz, times(3)).constantPoolEntryAccept(anyInt(), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction can be called multiple times with OP_LDC_W.
     * Each call should independently process the instruction.
     */
    @Test
    public void testVisitConstantInstruction_calledMultipleTimesWithOpLdcW_processesEachCall() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_LDC_W, 10);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_LDC_W, 20);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_LDC_W, 30);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 5, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction3);

        // Assert - verify each call processed independently
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(20), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(30), eq(marker));
        verify(clazz, times(3)).constantPoolEntryAccept(anyInt(), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with a mix of OP_LDC and OP_LDC_W instructions.
     * Both instruction types should be processed.
     */
    @Test
    public void testVisitConstantInstruction_withMixOfLdcAndLdcW_processesBoth() {
        // Arrange
        ConstantInstruction ldc1 = new ConstantInstruction(Instruction.OP_LDC, 5);
        ConstantInstruction ldcW1 = new ConstantInstruction(Instruction.OP_LDC_W, 10);
        ConstantInstruction ldc2 = new ConstantInstruction(Instruction.OP_LDC, 15);
        ConstantInstruction ldcW2 = new ConstantInstruction(Instruction.OP_LDC_W, 20);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldcW1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldcW2);

        // Assert - verify all calls were processed
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(15), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(20), eq(marker));
        verify(clazz, times(4)).constantPoolEntryAccept(anyInt(), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with null method parameter.
     * The method parameter is not used in the implementation, so null should be acceptable.
     */
    @Test
    public void testVisitConstantInstruction_withNullMethod_stillProcessesInstruction() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, null, codeAttribute, 0, instruction);

        // Assert - verify the instruction was still processed
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with null codeAttribute parameter.
     * The codeAttribute parameter is not used in the implementation, so null should be acceptable.
     */
    @Test
    public void testVisitConstantInstruction_withNullCodeAttribute_stillProcessesInstruction() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, null, 0, instruction);

        // Assert - verify the instruction was still processed
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction does not throw exception with null instruction.
     * This tests the robustness of the implementation.
     */
    @Test
    public void testVisitConstantInstruction_withNullInstruction_throwsNullPointerException() {
        // Act & Assert - expect NullPointerException when accessing instruction.opcode
        assertThrows(NullPointerException.class, () ->
            marker.visitConstantInstruction(clazz, method, codeAttribute, 0, null));
    }

    /**
     * Tests that visitConstantInstruction with same instruction called twice processes both times.
     * This verifies the method is stateless and can process the same instruction multiple times.
     */
    @Test
    public void testVisitConstantInstruction_sameInstructionCalledTwice_processesBothTimes() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 7);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);

        // Assert - verify constantPoolEntryAccept was called twice
        verify(clazz, times(2)).constantPoolEntryAccept(eq(7), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with constant index 0 works correctly.
     * Index 0 is a valid constant pool index (though rarely used).
     */
    @Test
    public void testVisitConstantInstruction_withConstantIndexZero_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 0);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was called with index 0
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction doesn't interact with method or codeAttribute parameters.
     * These parameters are not used in the implementation.
     */
    @Test
    public void testVisitConstantInstruction_doesNotInteractWithMethodOrCodeAttribute() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions with method or codeAttribute
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that multiple DotClassMarker instances work independently.
     * Each marker should process instructions without affecting others.
     */
    @Test
    public void testVisitConstantInstruction_withMultipleMarkers_workIndependently() {
        // Arrange
        DotClassMarker marker1 = new DotClassMarker();
        DotClassMarker marker2 = new DotClassMarker();
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_LDC, 2);

        // Act
        marker1.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker2.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);

        // Assert - verify each marker processed its instruction
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker1));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker2));
    }

    /**
     * Tests boundary value for constant index with OP_LDC.
     * OP_LDC uses a 1-byte index, so maximum value is 255.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdcMaxIndex_callsConstantPoolEntryAccept() {
        // Arrange - OP_LDC can address up to index 255 (1-byte index)
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 255);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(255), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction passes the DotClassMarker itself as the visitor.
     * This is crucial because the marker implements ConstantVisitor to process the constants.
     */
    @Test
    public void testVisitConstantInstruction_passesMarkerAsVisitor() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify the marker itself is passed as the visitor
        verify(clazz).constantPoolEntryAccept(eq(5), same(marker));
    }

    /**
     * Tests that visitConstantInstruction processes only LDC instructions in a mixed sequence.
     * This verifies the opcode filtering works correctly in realistic scenarios.
     */
    @Test
    public void testVisitConstantInstruction_withMixedOpcodes_processesOnlyLdcInstructions() {
        // Arrange - mix of LDC and non-LDC instructions
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction getStatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 2);
        ConstantInstruction ldcW = new ConstantInstruction(Instruction.OP_LDC_W, 3);
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 4);
        ConstantInstruction ldc2 = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldcW);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc2);

        // Assert - verify only LDC and LDC_W were processed (indices 1, 3, 5)
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz, times(3)).constantPoolEntryAccept(anyInt(), eq(marker));
        verify(clazz, never()).constantPoolEntryAccept(eq(2), any());
        verify(clazz, never()).constantPoolEntryAccept(eq(4), any());
    }

    /**
     * Tests that visitConstantInstruction works correctly when called on different clazz instances.
     * Each clazz should have its constant pool accessed independently.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentClazzInstances_accessesEachSeparately() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz1, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz2, method, codeAttribute, 0, instruction);

        // Assert - verify each clazz was accessed independently
        verify(clazz1).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction completes quickly.
     * The method should have minimal overhead since it only performs a simple check and delegation.
     */
    @Test
    public void testVisitConstantInstruction_executesQuickly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            marker.visitConstantInstruction(clazz, method, codeAttribute, i, instruction);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitConstantInstruction should execute quickly");
    }

    /**
     * Tests that the method correctly identifies and processes both LDC variants (LDC and LDC_W)
     * while ignoring all other constant instruction types.
     */
    @Test
    public void testVisitConstantInstruction_selectiveProcessing_correctOpcodeFiltering() {
        // Arrange - comprehensive set of constant instruction opcodes
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction ldcW = new ConstantInstruction(Instruction.OP_LDC_W, 2);
        ConstantInstruction ldc2W = new ConstantInstruction(Instruction.OP_LDC2_W, 3);
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 4);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldcW);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc2W);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, newInst);

        // Assert - only LDC (index 1) and LDC_W (index 2) should be processed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz, times(2)).constantPoolEntryAccept(anyInt(), eq(marker));
    }
}

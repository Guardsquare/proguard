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
 * Test class for {@link SuperInvocationMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method specifically handles INVOKESPECIAL instructions to detect super method invocations.
 * For INVOKESPECIAL instructions, it:
 * 1. Resets the internal invokesSuperMethods flag to false
 * 2. Calls constantPoolEntryAccept on the clazz with the instruction's constantIndex
 * 3. If the constant pool entry represents a super method invocation (determined by visitAnyMethodrefConstant),
 *    it marks the method as invoking super methods
 *
 * For all other instruction opcodes, this method does nothing.
 */
public class SuperInvocationMarkerClaude_visitConstantInstructionTest {

    private SuperInvocationMarker marker;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        marker = new SuperInvocationMarker();
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    /**
     * Tests that visitConstantInstruction processes INVOKESPECIAL instruction by calling constantPoolEntryAccept.
     * This is the primary behavior - INVOKESPECIAL instructions are the only ones that can invoke super methods.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeSpecial_callsConstantPoolEntryAccept() {
        // Arrange
        int constantIndex = 10;
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was called with the correct index and marker as visitor
        verify(clazz).constantPoolEntryAccept(eq(constantIndex), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction calls constantPoolEntryAccept with different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeSpecialDifferentIndices_callsConstantPoolEntryAccept() {
        // Arrange
        int[] indices = {1, 5, 10, 50, 100, 255};

        for (int constantIndex : indices) {
            ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

            // Act
            marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

            // Assert
            verify(clazz).constantPoolEntryAccept(eq(constantIndex), eq(marker));
        }
    }

    /**
     * Tests that visitConstantInstruction with INVOKESPECIAL at different offsets processes correctly.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeSpecialAtDifferentOffsets_callsConstantPoolEntryAccept() {
        // Arrange
        int constantIndex = 15;
        int[] offsets = {0, 5, 10, 100, 1000};

        for (int offset : offsets) {
            ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

            // Act
            marker.visitConstantInstruction(clazz, method, codeAttribute, offset, instruction);

            // Assert
            verify(clazz, atLeastOnce()).constantPoolEntryAccept(eq(constantIndex), eq(marker));
        }
    }

    /**
     * Tests that visitConstantInstruction does not process INVOKEVIRTUAL instruction.
     * Only INVOKESPECIAL can be used for super method invocations.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeVirtual_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 10);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interaction with clazz
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process INVOKESTATIC instruction.
     * Static methods cannot be super invocations.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeStatic_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 10);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process INVOKEINTERFACE instruction.
     * Interface methods cannot be super invocations.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeInterface_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 10, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process INVOKEDYNAMIC instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeDynamic_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 10);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process LDC instruction.
     * Constant loading instructions are not relevant for super invocation detection.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process LDC_W instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdcW_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC_W, 10);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process LDC2_W instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc2W_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC2_W, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process GETSTATIC instruction.
     * Field access instructions are not relevant for super invocation detection.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetStatic_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 3);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process PUTSTATIC instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpPutStatic_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 7);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process GETFIELD instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetField_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 4);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process PUTFIELD instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpPutField_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 8);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process NEW instruction.
     * Object instantiation is not relevant for super invocation detection.
     */
    @Test
    public void testVisitConstantInstruction_withOpNew_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 10);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process ANEWARRAY instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpAnewarray_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process CHECKCAST instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpCheckcast_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 12);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process INSTANCEOF instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpInstanceof_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 8);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does not process MULTIANEWARRAY instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpMultianewarray_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 5, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction can be called multiple times with INVOKESPECIAL.
     * Each call should independently process the instruction.
     */
    @Test
    public void testVisitConstantInstruction_multipleInvokeSpecialCalls_eachCallProcessed() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 10);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 20);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 30);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 5, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction3);

        // Assert - verify each constant index was processed
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(20), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(30), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with INVOKESPECIAL works correctly after non-INVOKESPECIAL instructions.
     * This verifies that the method properly filters by opcode.
     */
    @Test
    public void testVisitConstantInstruction_invokeSpecialAfterOtherOpcodes_onlyInvokeSpecialProcessed() {
        // Arrange
        ConstantInstruction ldcInstruction = new ConstantInstruction(Instruction.OP_LDC, 5);
        ConstantInstruction invokeVirtualInstruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 15);
        ConstantInstruction invokeSpecialInstruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 25);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldcInstruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 1, invokeVirtualInstruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 2, invokeSpecialInstruction);

        // Assert - only INVOKESPECIAL should have been processed
        verify(clazz, times(1)).constantPoolEntryAccept(eq(25), eq(marker));
        verify(clazz, never()).constantPoolEntryAccept(eq(5), any());
        verify(clazz, never()).constantPoolEntryAccept(eq(15), any());
    }

    /**
     * Tests that visitConstantInstruction with null Clazz parameter for INVOKESPECIAL throws exception.
     * The method attempts to call constantPoolEntryAccept on the clazz, so null will cause an error.
     */
    @Test
    public void testVisitConstantInstruction_invokeSpecialWithNullClazz_throwsNullPointerException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 10);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitConstantInstruction(null, method, codeAttribute, 0, instruction);
        });
    }

    /**
     * Tests that visitConstantInstruction with null Method parameter for INVOKESPECIAL does not throw exception
     * during the visitConstantInstruction call itself. The method parameter is not used directly in this method.
     */
    @Test
    public void testVisitConstantInstruction_invokeSpecialWithNullMethod_callsConstantPoolEntryAccept() {
        // Arrange
        int constantIndex = 10;
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

        // Act
        marker.visitConstantInstruction(clazz, null, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was still called
        verify(clazz).constantPoolEntryAccept(eq(constantIndex), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with null CodeAttribute parameter for INVOKESPECIAL does not throw exception
     * during the visitConstantInstruction call itself. The codeAttribute parameter is not used directly in this method.
     */
    @Test
    public void testVisitConstantInstruction_invokeSpecialWithNullCodeAttribute_callsConstantPoolEntryAccept() {
        // Arrange
        int constantIndex = 10;
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

        // Act
        marker.visitConstantInstruction(clazz, method, null, 0, instruction);

        // Assert - verify constantPoolEntryAccept was still called
        verify(clazz).constantPoolEntryAccept(eq(constantIndex), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with null ConstantInstruction throws exception.
     * The instruction's opcode must be accessed.
     */
    @Test
    public void testVisitConstantInstruction_withNullInstruction_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitConstantInstruction(clazz, method, codeAttribute, 0, null);
        });
    }

    /**
     * Tests that visitConstantInstruction with non-INVOKESPECIAL opcodes does not interact with any parameters.
     * This verifies the early return behavior for non-INVOKESPECIAL instructions.
     */
    @Test
    public void testVisitConstantInstruction_withNonInvokeSpecialOpcodes_noInteractionsWithParameters() {
        // Arrange
        ConstantInstruction[] instructions = {
            new ConstantInstruction(Instruction.OP_LDC, 1),
            new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 2),
            new ConstantInstruction(Instruction.OP_INVOKESTATIC, 3),
            new ConstantInstruction(Instruction.OP_GETFIELD, 4),
            new ConstantInstruction(Instruction.OP_PUTFIELD, 5)
        };

        // Act - call with various non-INVOKESPECIAL instructions
        for (ConstantInstruction instruction : instructions) {
            marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        }

        // Assert - verify no interactions occurred with clazz
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction can be called with various constant indices including boundary values.
     */
    @Test
    public void testVisitConstantInstruction_withBoundaryConstantIndices_callsConstantPoolEntryAccept() {
        // Arrange
        int[] boundaryIndices = {0, 1, 127, 128, 254, 255};

        for (int constantIndex : boundaryIndices) {
            ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

            // Act
            marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

            // Assert
            verify(clazz).constantPoolEntryAccept(eq(constantIndex), eq(marker));
        }
    }

    /**
     * Tests that visitConstantInstruction with INVOKESPECIAL at negative offset works correctly.
     * The offset is not validated within this method.
     */
    @Test
    public void testVisitConstantInstruction_invokeSpecialWithNegativeOffset_callsConstantPoolEntryAccept() {
        // Arrange
        int constantIndex = 10;
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, -1, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(constantIndex), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with INVOKESPECIAL at maximum integer offset works correctly.
     */
    @Test
    public void testVisitConstantInstruction_invokeSpecialWithMaxOffset_callsConstantPoolEntryAccept() {
        // Arrange
        int constantIndex = 10;
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(constantIndex), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction called by multiple marker instances operates independently.
     * Each marker should independently process INVOKESPECIAL instructions.
     */
    @Test
    public void testVisitConstantInstruction_withMultipleMarkers_operateIndependently() {
        // Arrange
        SuperInvocationMarker marker1 = new SuperInvocationMarker();
        SuperInvocationMarker marker2 = new SuperInvocationMarker();
        int constantIndex = 10;
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

        // Act
        marker1.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker2.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - both markers should have called constantPoolEntryAccept
        verify(clazz, times(2)).constantPoolEntryAccept(eq(constantIndex), any(SuperInvocationMarker.class));
    }

    /**
     * Tests that visitConstantInstruction does not throw exception when called repeatedly.
     * This verifies idempotent behavior.
     */
    @Test
    public void testVisitConstantInstruction_repeatedCallsWithSameInstruction_doesNotThrowException() {
        // Arrange
        int constantIndex = 10;
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
            }
        });

        // Verify that constantPoolEntryAccept was called 10 times
        verify(clazz, times(10)).constantPoolEntryAccept(eq(constantIndex), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction properly handles INVOKESPECIAL with constant index 0.
     * Index 0 might be a special case in some constant pool implementations.
     */
    @Test
    public void testVisitConstantInstruction_invokeSpecialWithZeroIndex_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 0);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with different Clazz instances processes each independently.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentClazzInstances_processesIndependently() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        int constantIndex = 10;
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

        // Act
        marker.visitConstantInstruction(clazz1, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz2, method, codeAttribute, 0, instruction);

        // Assert - each clazz should have been called independently
        verify(clazz1).constantPoolEntryAccept(eq(constantIndex), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(constantIndex), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with different Method instances processes correctly.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentMethodInstances_processesCorrectly() {
        // Arrange
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        int constantIndex = 10;
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);

        // Act
        marker.visitConstantInstruction(clazz, method1, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method2, codeAttribute, 0, instruction);

        // Assert - clazz should have been called twice
        verify(clazz, times(2)).constantPoolEntryAccept(eq(constantIndex), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction executes quickly for INVOKESPECIAL instructions.
     * Performance test to ensure no unexpected overhead.
     */
    @Test
    public void testVisitConstantInstruction_invokeSpecial_executesQuickly() {
        // Arrange
        int constantIndex = 10;
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, constantIndex);
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            marker.visitConstantInstruction(clazz, method, codeAttribute, i, instruction);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitConstantInstruction should execute quickly");
    }

    /**
     * Tests that visitConstantInstruction with mixed INVOKESPECIAL and non-INVOKESPECIAL instructions
     * only processes the INVOKESPECIAL ones.
     */
    @Test
    public void testVisitConstantInstruction_mixedOpcodes_onlyProcessesInvokeSpecial() {
        // Arrange
        ConstantInstruction invokeSpecial1 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 10);
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 20);
        ConstantInstruction invokeSpecial2 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 30);
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 40);
        ConstantInstruction invokeSpecial3 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 50);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeSpecial1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 1, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 2, invokeSpecial2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 3, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 4, invokeSpecial3);

        // Assert - only INVOKESPECIAL instructions should have been processed
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(30), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(50), eq(marker));
        verify(clazz, never()).constantPoolEntryAccept(eq(20), any());
        verify(clazz, never()).constantPoolEntryAccept(eq(40), any());
    }
}

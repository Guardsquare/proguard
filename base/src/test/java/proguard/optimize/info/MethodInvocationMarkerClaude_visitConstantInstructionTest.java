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
 * Test class for {@link MethodInvocationMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method processes all constant instructions by calling
 * constantPoolEntryAccept on the clazz with the instruction's constantIndex. This allows
 * the marker to visit the constant pool entry, which may trigger counting method invocations
 * for method reference constants or string constants that reference methods.
 */
public class MethodInvocationMarkerClaude_visitConstantInstructionTest {

    private MethodInvocationMarker marker;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        marker = new MethodInvocationMarker();
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    /**
     * Tests that visitConstantInstruction calls constantPoolEntryAccept for OP_LDC instruction.
     * This is the most common constant loading instruction.
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
     * LDC_W is the wide version of LDC.
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
     * Tests that visitConstantInstruction calls constantPoolEntryAccept for OP_LDC2_W instruction.
     * LDC2_W loads long/double constants.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc2W_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC2_W, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was called
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes GETSTATIC instruction.
     * Field access instructions reference constants in the constant pool.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetStatic_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 3);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes PUTSTATIC instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpPutStatic_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 7);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(7), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes GETFIELD instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetField_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 4);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(4), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes PUTFIELD instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpPutField_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 8);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(8), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes INVOKEVIRTUAL instruction.
     * This is critical for MethodInvocationMarker as it tracks method invocations.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeVirtual_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 12);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(12), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes INVOKESPECIAL instruction.
     * This tracks constructor calls and private method invocations.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeSpecial_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 15);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(15), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes INVOKESTATIC instruction.
     * This tracks static method invocations.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeStatic_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 20);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(20), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes INVOKEINTERFACE instruction.
     * This tracks interface method invocations.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeInterface_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 25, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(25), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes NEW instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpNew_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 30);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(30), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes ANEWARRAY instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpAnewArray_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 35);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(35), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes CHECKCAST instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpCheckCast_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 40);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(40), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes INSTANCEOF instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpInstanceOf_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 45);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(45), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes MULTIANEWARRAY instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpMultiANewArray_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 50, 3);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(50), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with different constant indices.
     * The constant index should be passed through correctly to constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentIndices_usesCorrectIndex() {
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
     * Tests that visitConstantInstruction works with large constant indices (OP_LDC_W).
     * OP_LDC_W uses a 2-byte index and can access a larger constant pool.
     */
    @Test
    public void testVisitConstantInstruction_withLargeIndices_usesCorrectIndex() {
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
     * Tests that visitConstantInstruction can be called multiple times.
     * Each call should independently process the instruction.
     */
    @Test
    public void testVisitConstantInstruction_calledMultipleTimes_processesEachCall() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 2);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 3);

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
     * Tests that visitConstantInstruction works with a mix of different instruction types.
     * All constant instructions should be processed.
     */
    @Test
    public void testVisitConstantInstruction_withMixedInstructions_processesAll() {
        // Arrange
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 5);
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 10);
        ConstantInstruction getField = new ConstantInstruction(Instruction.OP_GETFIELD, 15);
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 20);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, newInst);

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
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

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
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, null, 0, instruction);

        // Assert - verify the instruction was still processed
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction throws exception with null clazz.
     * The clazz is required to access the constant pool.
     */
    @Test
    public void testVisitConstantInstruction_withNullClazz_throwsNullPointerException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act & Assert - expect NullPointerException when accessing clazz methods
        assertThrows(NullPointerException.class, () ->
            marker.visitConstantInstruction(null, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction throws exception with null instruction.
     * This tests the robustness of the implementation.
     */
    @Test
    public void testVisitConstantInstruction_withNullInstruction_throwsNullPointerException() {
        // Act & Assert - expect NullPointerException when accessing instruction.constantIndex
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
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 7);

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
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions with method or codeAttribute
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that multiple MethodInvocationMarker instances work independently.
     * Each marker should process instructions without affecting others.
     */
    @Test
    public void testVisitConstantInstruction_withMultipleMarkers_workIndependently() {
        // Arrange
        MethodInvocationMarker marker1 = new MethodInvocationMarker();
        MethodInvocationMarker marker2 = new MethodInvocationMarker();
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 2);

        // Act
        marker1.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker2.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);

        // Assert - verify each marker processed its instruction
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker1));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker2));
    }

    /**
     * Tests that visitConstantInstruction passes the MethodInvocationMarker itself as the visitor.
     * This is crucial because the marker implements ConstantVisitor to process the constants.
     */
    @Test
    public void testVisitConstantInstruction_passesMarkerAsVisitor() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify the marker itself is passed as the visitor
        verify(clazz).constantPoolEntryAccept(eq(5), same(marker));
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
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act
        marker.visitConstantInstruction(clazz1, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz2, method, codeAttribute, 0, instruction);

        // Assert - verify each clazz was accessed independently
        verify(clazz1).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction completes quickly.
     * The method should have minimal overhead since it only performs a simple delegation.
     */
    @Test
    public void testVisitConstantInstruction_executesQuickly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);
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
     * Tests that visitConstantInstruction processes all method invocation instructions.
     * This is important for the MethodInvocationMarker's purpose.
     */
    @Test
    public void testVisitConstantInstruction_withAllMethodInvocationTypes_processesAll() {
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

        // Assert - verify all method invocation instructions were processed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(4), eq(marker));
        verify(clazz, times(4)).constantPoolEntryAccept(anyInt(), eq(marker));
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
     * Tests that visitConstantInstruction works with boundary offset values.
     * The offset parameter should not cause any issues.
     */
    @Test
    public void testVisitConstantInstruction_withBoundaryOffsets_doesNotThrowException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act & Assert - test with boundary values
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
                "Should handle offset 0");
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, instruction),
                "Should handle Integer.MAX_VALUE offset");
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, Integer.MIN_VALUE, instruction),
                "Should handle Integer.MIN_VALUE offset");
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, -1, instruction),
                "Should handle negative offset");
    }

    /**
     * Tests that visitConstantInstruction is thread-safe when called concurrently.
     * Since the method has no shared state modification, it should handle concurrent calls.
     */
    @Test
    public void testVisitConstantInstruction_concurrentCalls_noExceptions() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act - create multiple threads that call visitConstantInstruction
        for (int i = 0; i < threadCount; i++) {
            final int offset = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    marker.visitConstantInstruction(clazz, method, codeAttribute, offset, instruction);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify constantPoolEntryAccept was called the expected number of times
        verify(clazz, times(1000)).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that the method correctly processes all constant instruction opcodes without filtering.
     * Unlike DotClassMarker, MethodInvocationMarker processes all constant instructions.
     */
    @Test
    public void testVisitConstantInstruction_processesAllOpcodes_noFiltering() {
        // Arrange - diverse set of constant instruction opcodes
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction ldcW = new ConstantInstruction(Instruction.OP_LDC_W, 2);
        ConstantInstruction ldc2W = new ConstantInstruction(Instruction.OP_LDC2_W, 3);
        ConstantInstruction getStatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 4);
        ConstantInstruction putStatic = new ConstantInstruction(Instruction.OP_PUTSTATIC, 5);
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 6);
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 7);
        ConstantInstruction checkCast = new ConstantInstruction(Instruction.OP_CHECKCAST, 8);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldcW);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc2W);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, newInst);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, checkCast);

        // Assert - all instructions should be processed (no filtering)
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(4), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(6), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(7), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(8), eq(marker));
        verify(clazz, times(8)).constantPoolEntryAccept(anyInt(), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works correctly in a realistic sequence.
     * This simulates processing multiple instructions as they would appear in bytecode.
     */
    @Test
    public void testVisitConstantInstruction_realisticSequence_processesCorrectly() {
        // Arrange - realistic sequence of instructions
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 10);
        ConstantInstruction getField = new ConstantInstruction(Instruction.OP_GETFIELD, 15);
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 20);
        ConstantInstruction invokeStatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 25);
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 30);
        ConstantInstruction invokeSpecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 35);

        // Act - simulate visiting instructions in sequence
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 2, getField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 5, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 8, invokeStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 11, newInst);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 14, invokeSpecial);

        // Assert - verify all instructions were processed in order
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(15), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(20), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(25), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(30), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(35), eq(marker));
        verify(clazz, times(6)).constantPoolEntryAccept(anyInt(), eq(marker));
    }
}

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
 * Test class for {@link ParameterEscapedMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method processes constant instructions that are method invocations
 * (INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE). For these instructions, it:
 * 1. Sets the referencingMethod field to the current method
 * 2. Sets the referencingOffset field to the current offset
 * 3. Sets the referencingPopCount field to the instruction's stack pop count
 * 4. Calls constantPoolEntryAccept on the clazz to process the method reference
 *
 * For all other constant instructions, this method does nothing (it's a selective handler).
 */
public class ParameterEscapedMarkerClaude_visitConstantInstructionTest {

    private ParameterEscapedMarker marker;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        marker = new ParameterEscapedMarker();
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    /**
     * Tests that visitConstantInstruction processes INVOKEVIRTUAL instruction.
     * This is one of the four invoke instructions that should trigger processing.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeVirtual_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 12);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);

        // Assert - verify constantPoolEntryAccept was called with the correct index and marker as visitor
        verify(clazz).constantPoolEntryAccept(eq(12), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes INVOKESPECIAL instruction.
     * This handles constructor calls and private method invocations.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeSpecial_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 15);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 20, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(15), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes INVOKESTATIC instruction.
     * This handles static method invocations.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeStatic_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 20);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 30, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(20), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction processes INVOKEINTERFACE instruction.
     * This handles interface method invocations.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeInterface_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 25, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 40, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(25), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction does NOT process OP_LDC instruction.
     * LDC is not a method invocation, so it should be ignored.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was NOT called
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process OP_LDC_W instruction.
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
     * Tests that visitConstantInstruction does NOT process OP_LDC2_W instruction.
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
     * Tests that visitConstantInstruction does NOT process GETSTATIC instruction.
     * Field access instructions are not method invocations.
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
     * Tests that visitConstantInstruction does NOT process PUTSTATIC instruction.
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
     * Tests that visitConstantInstruction does NOT process GETFIELD instruction.
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
     * Tests that visitConstantInstruction does NOT process PUTFIELD instruction.
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
     * Tests that visitConstantInstruction does NOT process NEW instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpNew_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 30);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process ANEWARRAY instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpAnewArray_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 35);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process CHECKCAST instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpCheckCast_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 40);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process INSTANCEOF instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpInstanceOf_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 45);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process MULTIANEWARRAY instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpMultiANewArray_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 50, 3);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction processes all four invoke instruction types.
     * This is the core functionality for tracking parameter escapes at method invocation sites.
     */
    @Test
    public void testVisitConstantInstruction_withAllInvokeTypes_processesAll() {
        // Arrange
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        ConstantInstruction invokeSpecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 2);
        ConstantInstruction invokeStatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 3);
        ConstantInstruction invokeInterface = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 4, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 20, invokeSpecial);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 30, invokeStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 40, invokeInterface);

        // Assert - verify all invoke instructions were processed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(4), eq(marker));
        verify(clazz, times(4)).constantPoolEntryAccept(anyInt(), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction correctly passes different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentConstantIndices_usesCorrectIndex() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 0);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 100);
        ConstantInstruction instruction4 = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 65535);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction3);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction4);

        // Assert - verify each constant index was passed correctly
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(100), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction can be called multiple times with the same instruction.
     * Each call should process independently.
     */
    @Test
    public void testVisitConstantInstruction_sameInstructionCalledTwice_processesBothTimes() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 7);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 20, instruction);

        // Assert - verify constantPoolEntryAccept was called twice
        verify(clazz, times(2)).constantPoolEntryAccept(eq(7), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with different offset values.
     * The offset should be used internally but shouldn't affect constant pool access.
     */
    @Test
    public void testVisitConstantInstruction_withVariousOffsets_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act - call with different offsets
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 100, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 1000, instruction);

        // Assert - verify constantPoolEntryAccept was called 4 times with the same index
        verify(clazz, times(4)).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction throws NullPointerException with null clazz.
     * The clazz is required to access the constant pool.
     */
    @Test
    public void testVisitConstantInstruction_withNullClazz_throwsNullPointerException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            marker.visitConstantInstruction(null, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction throws NullPointerException with null instruction.
     */
    @Test
    public void testVisitConstantInstruction_withNullInstruction_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            marker.visitConstantInstruction(clazz, method, codeAttribute, 0, null));
    }

    /**
     * Tests that visitConstantInstruction works with null method parameter for invoke instructions.
     * The method parameter is used to set internal state, but null might be acceptable.
     */
    @Test
    public void testVisitConstantInstruction_withNullMethod_stillProcessesInvokeInstruction() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act - should not throw exception even with null method
        assertDoesNotThrow(() ->
            marker.visitConstantInstruction(clazz, null, codeAttribute, 0, instruction));

        // Assert - verify the instruction was still processed
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with null codeAttribute parameter.
     * The codeAttribute might not be used directly in the processing.
     */
    @Test
    public void testVisitConstantInstruction_withNullCodeAttribute_stillProcessesInvokeInstruction() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act
        assertDoesNotThrow(() ->
            marker.visitConstantInstruction(clazz, method, null, 0, instruction));

        // Assert - verify the instruction was still processed
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction filters correctly - only invoke instructions are processed.
     * This demonstrates the selective nature of the method.
     */
    @Test
    public void testVisitConstantInstruction_mixedInstructions_onlyProcessesInvokes() {
        // Arrange
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 2);
        ConstantInstruction getField = new ConstantInstruction(Instruction.OP_GETFIELD, 3);
        ConstantInstruction invokeStatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 4);
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 20, getField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 30, invokeStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 40, newInst);

        // Assert - only invoke instructions should be processed
        verify(clazz, never()).constantPoolEntryAccept(eq(1), eq(marker)); // ldc not processed
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));          // invokeVirtual processed
        verify(clazz, never()).constantPoolEntryAccept(eq(3), eq(marker)); // getField not processed
        verify(clazz).constantPoolEntryAccept(eq(4), eq(marker));          // invokeStatic processed
        verify(clazz, never()).constantPoolEntryAccept(eq(5), eq(marker)); // new not processed
        verify(clazz, times(2)).constantPoolEntryAccept(anyInt(), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction passes the marker itself as the visitor.
     * This is crucial because the marker implements ConstantVisitor to process method references.
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
     * Tests that visitConstantInstruction works correctly with different clazz instances.
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
     * Tests that multiple ParameterEscapedMarker instances work independently.
     */
    @Test
    public void testVisitConstantInstruction_withMultipleMarkers_workIndependently() {
        // Arrange
        ParameterEscapedMarker marker1 = new ParameterEscapedMarker();
        ParameterEscapedMarker marker2 = new ParameterEscapedMarker();
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
     * Tests that visitConstantInstruction works in a realistic sequence of method calls.
     */
    @Test
    public void testVisitConstantInstruction_realisticSequence_processesCorrectly() {
        // Arrange - realistic sequence simulating bytecode analysis
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

        // Assert - only invoke instructions should be processed
        verify(clazz, never()).constantPoolEntryAccept(eq(10), eq(marker)); // ldc
        verify(clazz, never()).constantPoolEntryAccept(eq(15), eq(marker)); // getField
        verify(clazz).constantPoolEntryAccept(eq(20), eq(marker));          // invokeVirtual
        verify(clazz).constantPoolEntryAccept(eq(25), eq(marker));          // invokeStatic
        verify(clazz, never()).constantPoolEntryAccept(eq(30), eq(marker)); // new
        verify(clazz).constantPoolEntryAccept(eq(35), eq(marker));          // invokeSpecial
        verify(clazz, times(3)).constantPoolEntryAccept(anyInt(), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction handles boundary offset values.
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
     * Tests that visitConstantInstruction with constant index 0 works correctly.
     * Index 0 is a valid constant pool index.
     */
    @Test
    public void testVisitConstantInstruction_withConstantIndexZero_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 0);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was called with index 0
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with maximum constant index value works correctly.
     */
    @Test
    public void testVisitConstantInstruction_withMaxConstantIndex_callsConstantPoolEntryAccept() {
        // Arrange - maximum 2-byte index value
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 65535);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction doesn't interact with method or codeAttribute
     * parameters directly (they're used for setting internal state but not passed to constantPoolEntryAccept).
     */
    @Test
    public void testVisitConstantInstruction_doesNotDirectlyInteractWithMethodOrCodeAttribute() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no direct interactions with method or codeAttribute
        // Note: These parameters are used to set internal state, not for direct method calls
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitConstantInstruction correctly handles INVOKEINTERFACE with count parameter.
     * INVOKEINTERFACE has an additional count parameter in its bytecode format.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeInterfaceDifferentCounts_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 10, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 11, 2);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 12, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction3);

        // Assert - all should be processed regardless of count parameter
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(11), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(12), eq(marker));
        verify(clazz, times(3)).constantPoolEntryAccept(anyInt(), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction executes quickly.
     * The method should have minimal overhead since it only performs conditional logic and delegation.
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
}

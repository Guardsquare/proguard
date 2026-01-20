package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DynamicInvocationMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method processes ConstantInstruction objects to detect invokedynamic instructions.
 * When it encounters an OP_INVOKEDYNAMIC instruction, it marks the method as invoking dynamically by setting
 * the appropriate flag in the method's optimization info.
 */
public class DynamicInvocationMarkerClaude_visitConstantInstructionTest {

    private DynamicInvocationMarker marker;
    private ProgramClass clazz;
    private ProgramMethod method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        marker = new DynamicInvocationMarker();

        // Create a real ProgramClass with constant pool
        clazz = createProgramClassWithConstantPool();

        // Create a real ProgramMethod with a descriptor
        method = createMethodWithDescriptor(clazz, "testMethod", "()V");

        // Set the processing info with optimization info
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(clazz, method);

        codeAttribute = new CodeAttribute();
    }

    /**
     * Tests that visitConstantInstruction marks the method as invoking dynamically when encountering OP_INVOKEDYNAMIC.
     * This is the primary behavior - invokedynamic instructions should trigger the flag to be set.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeDynamic_marksMethodAsInvokingDynamically() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 5);

        // Assert initial state - method should not be marked as invoking dynamically
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked as invoking dynamically initially");

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - method should now be marked as invoking dynamically
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should be marked as invoking dynamically after processing OP_INVOKEDYNAMIC");
    }

    /**
     * Tests that visitConstantInstruction does NOT mark the method for OP_INVOKEVIRTUAL.
     * Only OP_INVOKEDYNAMIC should trigger the marking.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeVirtual_doesNotMarkMethod() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - method should not be marked as invoking dynamically
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked for OP_INVOKEVIRTUAL");
    }

    /**
     * Tests that visitConstantInstruction does NOT mark the method for OP_INVOKESPECIAL.
     * Only OP_INVOKEDYNAMIC should trigger the marking.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeSpecial_doesNotMarkMethod() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - method should not be marked as invoking dynamically
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked for OP_INVOKESPECIAL");
    }

    /**
     * Tests that visitConstantInstruction does NOT mark the method for OP_INVOKESTATIC.
     * Only OP_INVOKEDYNAMIC should trigger the marking.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeStatic_doesNotMarkMethod() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - method should not be marked as invoking dynamically
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked for OP_INVOKESTATIC");
    }

    /**
     * Tests that visitConstantInstruction does NOT mark the method for OP_INVOKEINTERFACE.
     * Only OP_INVOKEDYNAMIC should trigger the marking.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeInterface_doesNotMarkMethod() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 5, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - method should not be marked as invoking dynamically
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked for OP_INVOKEINTERFACE");
    }

    /**
     * Tests that visitConstantInstruction does NOT mark the method for LDC instructions.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc_doesNotMarkMethod() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - method should not be marked as invoking dynamically
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked for OP_LDC");
    }

    /**
     * Tests that visitConstantInstruction does NOT mark the method for LDC_W instructions.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdcW_doesNotMarkMethod() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC_W, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - method should not be marked as invoking dynamically
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked for OP_LDC_W");
    }

    /**
     * Tests that visitConstantInstruction does NOT mark the method for LDC2_W instructions.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc2W_doesNotMarkMethod() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC2_W, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - method should not be marked as invoking dynamically
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked for OP_LDC2_W");
    }

    /**
     * Tests that visitConstantInstruction does NOT mark the method for field access instructions.
     */
    @Test
    public void testVisitConstantInstruction_withFieldAccessOpcodes_doesNotMarkMethod() {
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

        // Assert - method should not be marked as invoking dynamically
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked for field access instructions");
    }

    /**
     * Tests that visitConstantInstruction does NOT mark the method for object creation instructions.
     */
    @Test
    public void testVisitConstantInstruction_withObjectCreationOpcodes_doesNotMarkMethod() {
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

        // Assert - method should not be marked as invoking dynamically
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked for object creation instructions");
    }

    /**
     * Tests that visitConstantInstruction with OP_INVOKEDYNAMIC works with different constant indices.
     * The constant index should not affect the marking behavior.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeDynamicAndDifferentIndices_alwaysMarksMethod() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 0);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 1);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 100);
        ConstantInstruction instruction4 = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 65535);

        // Create separate methods for each test to verify independent marking
        ProgramMethod method1 = createMethodWithDescriptor(clazz, "method1", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(clazz, method1);

        ProgramMethod method2 = createMethodWithDescriptor(clazz, "method2", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(clazz, method2);

        ProgramMethod method3 = createMethodWithDescriptor(clazz, "method3", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(clazz, method3);

        ProgramMethod method4 = createMethodWithDescriptor(clazz, "method4", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(clazz, method4);

        // Act
        marker.visitConstantInstruction(clazz, method1, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method2, codeAttribute, 0, instruction2);
        marker.visitConstantInstruction(clazz, method3, codeAttribute, 0, instruction3);
        marker.visitConstantInstruction(clazz, method4, codeAttribute, 0, instruction4);

        // Assert - all methods should be marked regardless of constant index
        assertTrue(DynamicInvocationMarker.invokesDynamically(method1),
                "Method should be marked with constant index 0");
        assertTrue(DynamicInvocationMarker.invokesDynamically(method2),
                "Method should be marked with constant index 1");
        assertTrue(DynamicInvocationMarker.invokesDynamically(method3),
                "Method should be marked with constant index 100");
        assertTrue(DynamicInvocationMarker.invokesDynamically(method4),
                "Method should be marked with constant index 65535");
    }

    /**
     * Tests that visitConstantInstruction works with various offset values.
     * The offset parameter should not affect the marking behavior.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeDynamicAndVariousOffsets_alwaysMarksMethod() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 5);

        // Act - first call marks the method
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - method should be marked
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should be marked after first call");

        // Act - subsequent calls with different offsets should not change the state
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 100, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, -1, instruction);

        // Assert - method should still be marked
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should remain marked after multiple calls");
    }

    /**
     * Tests that visitConstantInstruction can be called multiple times with OP_INVOKEDYNAMIC.
     * The flag should remain set after the first call.
     */
    @Test
    public void testVisitConstantInstruction_calledMultipleTimesWithOpInvokeDynamic_maintainsFlag() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 2);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 3);

        // Assert initial state
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked initially");

        // Act - first call
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);

        // Assert - marked after first call
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should be marked after first OP_INVOKEDYNAMIC");

        // Act - subsequent calls
        marker.visitConstantInstruction(clazz, method, codeAttribute, 5, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction3);

        // Assert - still marked after multiple calls
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should remain marked after multiple OP_INVOKEDYNAMIC calls");
    }

    /**
     * Tests that visitConstantInstruction with a mix of opcodes only marks for OP_INVOKEDYNAMIC.
     * This verifies the opcode filtering works correctly in realistic scenarios.
     */
    @Test
    public void testVisitConstantInstruction_withMixedOpcodes_marksOnlyForInvokeDynamic() {
        // Arrange - mix of different constant instruction opcodes
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 2);
        ConstantInstruction invokeDynamic = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 3);
        ConstantInstruction getStatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 4);
        ConstantInstruction invokeStatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 5);

        // Assert initial state
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked initially");

        // Act - process non-invokedynamic instructions first
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);

        // Assert - still not marked
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked after non-invokedynamic instructions");

        // Act - process invokedynamic instruction
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeDynamic);

        // Assert - now marked
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should be marked after OP_INVOKEDYNAMIC");

        // Act - process more non-invokedynamic instructions
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeStatic);

        // Assert - still marked
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should remain marked after subsequent non-invokedynamic instructions");
    }

    /**
     * Tests that visitConstantInstruction works correctly with null codeAttribute parameter.
     * The codeAttribute parameter is not used in the implementation, so null should be acceptable.
     */
    @Test
    public void testVisitConstantInstruction_withNullCodeAttribute_stillMarksMethod() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, null, 0, instruction);

        // Assert - method should still be marked
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should be marked even with null codeAttribute");
    }

    /**
     * Tests that visitConstantInstruction works correctly with the same instruction called twice.
     * This verifies the method is idempotent - calling it multiple times has the same effect as calling it once.
     */
    @Test
    public void testVisitConstantInstruction_sameInstructionCalledTwice_marksMethodOnce() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 7);

        // Assert initial state
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked initially");

        // Act - first call
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - marked after first call
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should be marked after first call");

        // Act - second call with same instruction
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);

        // Assert - still marked (idempotent behavior)
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should remain marked after second call");
    }

    /**
     * Tests that multiple DynamicInvocationMarker instances work independently on the same method.
     * Each marker should be able to mark the method independently.
     */
    @Test
    public void testVisitConstantInstruction_withMultipleMarkers_workIndependently() {
        // Arrange
        DynamicInvocationMarker marker1 = new DynamicInvocationMarker();
        DynamicInvocationMarker marker2 = new DynamicInvocationMarker();
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 1);

        // Assert initial state
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked initially");

        // Act - use first marker
        marker1.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - marked by first marker
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should be marked after first marker processes it");

        // Act - use second marker (on already marked method)
        marker2.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - still marked
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should remain marked after second marker processes it");
    }

    /**
     * Tests that visitConstantInstruction correctly marks different methods independently.
     * Each method should be marked separately based on its own instructions.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentMethods_marksIndependently() {
        // Arrange
        ProgramMethod method1 = createMethodWithDescriptor(clazz, "method1", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(clazz, method1);

        ProgramMethod method2 = createMethodWithDescriptor(clazz, "method2", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(clazz, method2);

        ConstantInstruction invokeDynamic = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 1);
        ConstantInstruction invokeStatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 2);

        // Assert initial state
        assertFalse(DynamicInvocationMarker.invokesDynamically(method1),
                "Method1 should not be marked initially");
        assertFalse(DynamicInvocationMarker.invokesDynamically(method2),
                "Method2 should not be marked initially");

        // Act - mark method1 with invokedynamic
        marker.visitConstantInstruction(clazz, method1, codeAttribute, 0, invokeDynamic);

        // Assert - only method1 should be marked
        assertTrue(DynamicInvocationMarker.invokesDynamically(method1),
                "Method1 should be marked after OP_INVOKEDYNAMIC");
        assertFalse(DynamicInvocationMarker.invokesDynamically(method2),
                "Method2 should not be marked yet");

        // Act - process method2 with non-invokedynamic instruction
        marker.visitConstantInstruction(clazz, method2, codeAttribute, 0, invokeStatic);

        // Assert - method1 still marked, method2 not marked
        assertTrue(DynamicInvocationMarker.invokesDynamically(method1),
                "Method1 should still be marked");
        assertFalse(DynamicInvocationMarker.invokesDynamically(method2),
                "Method2 should not be marked after OP_INVOKESTATIC");

        // Act - mark method2 with invokedynamic
        marker.visitConstantInstruction(clazz, method2, codeAttribute, 0, invokeDynamic);

        // Assert - both methods should now be marked
        assertTrue(DynamicInvocationMarker.invokesDynamically(method1),
                "Method1 should still be marked");
        assertTrue(DynamicInvocationMarker.invokesDynamically(method2),
                "Method2 should now be marked after OP_INVOKEDYNAMIC");
    }

    /**
     * Tests that visitConstantInstruction with constant index 0 works correctly.
     * Index 0 is a valid constant pool index and should work like any other index.
     */
    @Test
    public void testVisitConstantInstruction_withConstantIndexZero_marksMethod() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 0);

        // Assert initial state
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked initially");

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - method should be marked
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should be marked with constant index 0");
    }

    /**
     * Tests that visitConstantInstruction completes quickly.
     * The method should have minimal overhead since it only performs a simple check.
     */
    @Test
    public void testVisitConstantInstruction_executesQuickly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 5);
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
     * Tests that visitConstantInstruction processes only OP_INVOKEDYNAMIC in a comprehensive set of opcodes.
     * This verifies the opcode filtering is precise and only responds to the specific opcode.
     */
    @Test
    public void testVisitConstantInstruction_selectiveProcessing_correctOpcodeFiltering() {
        // Arrange - comprehensive set of constant instruction opcodes
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction ldcW = new ConstantInstruction(Instruction.OP_LDC_W, 2);
        ConstantInstruction ldc2W = new ConstantInstruction(Instruction.OP_LDC2_W, 3);
        ConstantInstruction getStatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 4);
        ConstantInstruction putStatic = new ConstantInstruction(Instruction.OP_PUTSTATIC, 5);
        ConstantInstruction getField = new ConstantInstruction(Instruction.OP_GETFIELD, 6);
        ConstantInstruction putField = new ConstantInstruction(Instruction.OP_PUTFIELD, 7);
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 8);
        ConstantInstruction invokeSpecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 9);
        ConstantInstruction invokeStatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 10);
        ConstantInstruction invokeInterface = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 11, 2);
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 12);
        ConstantInstruction anewArray = new ConstantInstruction(Instruction.OP_ANEWARRAY, 13);
        ConstantInstruction checkCast = new ConstantInstruction(Instruction.OP_CHECKCAST, 14);
        ConstantInstruction instanceOf = new ConstantInstruction(Instruction.OP_INSTANCEOF, 15);
        ConstantInstruction multiANewArray = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 16, 2);
        ConstantInstruction invokeDynamic = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 17);

        // Assert initial state
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked initially");

        // Act - process all non-invokedynamic instructions
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldcW);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc2W);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeSpecial);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeInterface);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, newInst);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, anewArray);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, checkCast);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instanceOf);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, multiANewArray);

        // Assert - still not marked after all non-invokedynamic instructions
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Method should not be marked after processing non-invokedynamic instructions");

        // Act - process invokedynamic instruction
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeDynamic);

        // Assert - now marked after invokedynamic
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should be marked only after OP_INVOKEDYNAMIC");
    }

    /**
     * Tests that the invokesDynamically static method correctly retrieves the flag status.
     * This verifies the integration between the marker and the static accessor method.
     */
    @Test
    public void testInvokesDynamically_staticMethod_correctlyRetrievesFlag() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 5);

        // Assert initial state using static method
        assertFalse(DynamicInvocationMarker.invokesDynamically(method),
                "Static method should return false initially");

        // Act - mark the method
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - static method should now return true
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Static method should return true after marking");
    }

    /**
     * Tests that visitConstantInstruction handles boundary offset values correctly.
     * The offset parameter should not affect the marking behavior.
     */
    @Test
    public void testVisitConstantInstruction_withBoundaryOffsets_marksMethodCorrectly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 5);

        // Create separate methods for boundary tests
        ProgramMethod methodMin = createMethodWithDescriptor(clazz, "methodMin", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(clazz, methodMin);

        ProgramMethod methodMax = createMethodWithDescriptor(clazz, "methodMax", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(clazz, methodMax);

        ProgramMethod methodNeg = createMethodWithDescriptor(clazz, "methodNeg", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(clazz, methodNeg);

        // Act - test with boundary values
        marker.visitConstantInstruction(clazz, methodMin, codeAttribute, Integer.MIN_VALUE, instruction);
        marker.visitConstantInstruction(clazz, methodMax, codeAttribute, Integer.MAX_VALUE, instruction);
        marker.visitConstantInstruction(clazz, methodNeg, codeAttribute, -1, instruction);

        // Assert - all should be marked regardless of offset
        assertTrue(DynamicInvocationMarker.invokesDynamically(methodMin),
                "Method should be marked with Integer.MIN_VALUE offset");
        assertTrue(DynamicInvocationMarker.invokesDynamically(methodMax),
                "Method should be marked with Integer.MAX_VALUE offset");
        assertTrue(DynamicInvocationMarker.invokesDynamically(methodNeg),
                "Method should be marked with negative offset");
    }

    /**
     * Tests that visitConstantInstruction can be called in rapid succession without issues.
     * This verifies there's no timing-dependent behavior.
     */
    @Test
    public void testVisitConstantInstruction_rapidSuccession_noIssues() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 5);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                marker.visitConstantInstruction(clazz, method, codeAttribute, i, instruction);
            }
        }, "Rapid successive calls should not cause issues");

        // Assert - method should be marked
        assertTrue(DynamicInvocationMarker.invokesDynamically(method),
                "Method should be marked after rapid calls");
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Creates a ProgramClass with a minimal constant pool setup.
     */
    private ProgramClass createProgramClassWithConstantPool() {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with enough space
        Constant[] constantPool = new Constant[100];
        constantPool[0] = null; // Index 0 is always null
        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 100;

        return programClass;
    }

    /**
     * Creates a ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Finds the next available index in the constant pool.
     */
    private int findNextAvailableConstantPoolIndex(ProgramClass programClass) {
        for (int i = 1; i < programClass.constantPool.length; i++) {
            if (programClass.constantPool[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("No available constant pool index");
    }
}

package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.Method;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReachableCodeMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method in ReachableCodeMarker is an empty implementation (no-op).
 * It's part of the InstructionVisitor interface but doesn't perform any actions because:
 * - Constant instructions (LDC, GETSTATIC, INVOKEVIRTUAL, etc.) don't affect control flow
 * - They always continue to the next instruction, which is the default behavior
 * - The actual reachability marking is done in the markCode() method
 * - Control flow changes are handled by visitBranchInstruction, visitSimpleInstruction (for returns), etc.
 *
 * These tests verify that:
 * 1. The method doesn't throw exceptions with various inputs
 * 2. The method doesn't modify the instruction, attribute, method, or class being visited
 * 3. The method can handle different types of constant instructions
 * 4. The method is properly integrated into the visitor pattern
 * 5. The method maintains the reachable code marker state unchanged
 */
public class ReachableCodeMarkerClaude_visitConstantInstructionTest {

    private ReachableCodeMarker marker;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        marker = new ReachableCodeMarker();
        clazz = new ProgramClass();
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    // ========================================
    // Basic Functionality Tests
    // ========================================

    /**
     * Tests that visitConstantInstruction does not throw exceptions with valid parameters.
     * Verifies the empty implementation executes without errors.
     */
    @Test
    public void testVisitConstantInstruction_withValidParameters_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should not throw exception with valid parameters");
    }

    /**
     * Tests that visitConstantInstruction handles LDC instruction.
     * LDC loads a constant from the constant pool.
     */
    @Test
    public void testVisitConstantInstruction_withLdcInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle LDC instruction");
    }

    /**
     * Tests that visitConstantInstruction handles LDC_W instruction.
     * LDC_W loads a constant from the constant pool (wide index).
     */
    @Test
    public void testVisitConstantInstruction_withLdcWInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC_W, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle LDC_W instruction");
    }

    /**
     * Tests that visitConstantInstruction handles LDC2_W instruction.
     * LDC2_W loads a long or double constant from the constant pool.
     */
    @Test
    public void testVisitConstantInstruction_withLdc2WInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC2_W, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle LDC2_W instruction");
    }

    /**
     * Tests that visitConstantInstruction handles GETSTATIC instruction.
     * GETSTATIC gets a static field value.
     */
    @Test
    public void testVisitConstantInstruction_withGetstaticInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle GETSTATIC instruction");
    }

    /**
     * Tests that visitConstantInstruction handles PUTSTATIC instruction.
     * PUTSTATIC sets a static field value.
     */
    @Test
    public void testVisitConstantInstruction_withPutstaticInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle PUTSTATIC instruction");
    }

    /**
     * Tests that visitConstantInstruction handles GETFIELD instruction.
     * GETFIELD gets an instance field value.
     */
    @Test
    public void testVisitConstantInstruction_withGetfieldInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle GETFIELD instruction");
    }

    /**
     * Tests that visitConstantInstruction handles PUTFIELD instruction.
     * PUTFIELD sets an instance field value.
     */
    @Test
    public void testVisitConstantInstruction_withPutfieldInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle PUTFIELD instruction");
    }

    /**
     * Tests that visitConstantInstruction handles INVOKEVIRTUAL instruction.
     * INVOKEVIRTUAL invokes an instance method.
     */
    @Test
    public void testVisitConstantInstruction_withInvokevirtualInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle INVOKEVIRTUAL instruction");
    }

    /**
     * Tests that visitConstantInstruction handles INVOKESPECIAL instruction.
     * INVOKESPECIAL invokes a constructor or private method.
     */
    @Test
    public void testVisitConstantInstruction_withInvokespecialInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle INVOKESPECIAL instruction");
    }

    /**
     * Tests that visitConstantInstruction handles INVOKESTATIC instruction.
     * INVOKESTATIC invokes a static method.
     */
    @Test
    public void testVisitConstantInstruction_withInvokestaticInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle INVOKESTATIC instruction");
    }

    /**
     * Tests that visitConstantInstruction handles INVOKEINTERFACE instruction.
     * INVOKEINTERFACE invokes an interface method.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeinterfaceInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle INVOKEINTERFACE instruction");
    }

    /**
     * Tests that visitConstantInstruction handles INVOKEDYNAMIC instruction.
     * INVOKEDYNAMIC invokes a dynamically-computed call site.
     */
    @Test
    public void testVisitConstantInstruction_withInvokedynamicInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle INVOKEDYNAMIC instruction");
    }

    /**
     * Tests that visitConstantInstruction handles NEW instruction.
     * NEW creates a new object instance.
     */
    @Test
    public void testVisitConstantInstruction_withNewInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle NEW instruction");
    }

    /**
     * Tests that visitConstantInstruction handles ANEWARRAY instruction.
     * ANEWARRAY creates a new array of reference types.
     */
    @Test
    public void testVisitConstantInstruction_withAnewarrayInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle ANEWARRAY instruction");
    }

    /**
     * Tests that visitConstantInstruction handles CHECKCAST instruction.
     * CHECKCAST checks whether an object is of a given type.
     */
    @Test
    public void testVisitConstantInstruction_withCheckcastInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle CHECKCAST instruction");
    }

    /**
     * Tests that visitConstantInstruction handles INSTANCEOF instruction.
     * INSTANCEOF determines if an object is of a given type.
     */
    @Test
    public void testVisitConstantInstruction_withInstanceofInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle INSTANCEOF instruction");
    }

    /**
     * Tests that visitConstantInstruction handles MULTIANEWARRAY instruction.
     * MULTIANEWARRAY creates a new multidimensional array.
     */
    @Test
    public void testVisitConstantInstruction_withMultianewarrayInstruction_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 1, 2);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle MULTIANEWARRAY instruction");
    }

    // ========================================
    // Different Offset Tests
    // ========================================

    /**
     * Tests that visitConstantInstruction works with different instruction offsets.
     * Verifies the method handles various offset values.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentOffsets_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
            marker.visitConstantInstruction(clazz, method, codeAttribute, 5, instruction);
            marker.visitConstantInstruction(clazz, method, codeAttribute, 100, instruction);
            marker.visitConstantInstruction(clazz, method, codeAttribute, 1000, instruction);
        }, "visitConstantInstruction should handle different offsets");
    }

    // ========================================
    // Null Parameter Handling Tests
    // ========================================

    /**
     * Tests that visitConstantInstruction handles null instruction parameter.
     * Verifies the no-op implementation doesn't dereference null.
     */
    @Test
    public void testVisitConstantInstruction_withNullInstruction_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, null),
            "visitConstantInstruction should handle null instruction");
    }

    /**
     * Tests that visitConstantInstruction handles null clazz parameter.
     * Verifies the no-op implementation doesn't dereference null.
     */
    @Test
    public void testVisitConstantInstruction_withNullClazz_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(null, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle null clazz");
    }

    /**
     * Tests that visitConstantInstruction handles null method parameter.
     * Verifies the no-op implementation doesn't dereference null.
     */
    @Test
    public void testVisitConstantInstruction_withNullMethod_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, null, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle null method");
    }

    /**
     * Tests that visitConstantInstruction handles null codeAttribute parameter.
     * Verifies the no-op implementation doesn't dereference null.
     */
    @Test
    public void testVisitConstantInstruction_withNullCodeAttribute_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, null, 0, instruction),
            "visitConstantInstruction should handle null codeAttribute");
    }

    /**
     * Tests that visitConstantInstruction handles all parameters being null.
     * Verifies the no-op implementation is robust to all null inputs.
     */
    @Test
    public void testVisitConstantInstruction_withAllParametersNull_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(null, null, null, 0, null),
            "visitConstantInstruction should handle all null parameters");
    }

    // ========================================
    // Different Clazz Types Tests
    // ========================================

    /**
     * Tests that visitConstantInstruction works with ProgramClass.
     * Verifies the method handles concrete class implementations.
     */
    @Test
    public void testVisitConstantInstruction_withProgramClass_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(programClass, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle ProgramClass");
    }

    /**
     * Tests that visitConstantInstruction works with LibraryClass.
     * Verifies the method handles different class types.
     */
    @Test
    public void testVisitConstantInstruction_withLibraryClass_doesNotThrow() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(libraryClass, method, codeAttribute, 0, instruction),
            "visitConstantInstruction should handle LibraryClass");
    }

    // ========================================
    // State Preservation Tests
    // ========================================

    /**
     * Tests that visitConstantInstruction doesn't affect the reachability state.
     * Verifies calling visitConstantInstruction doesn't mark any offsets as reachable.
     */
    @Test
    public void testVisitConstantInstruction_doesNotAffectReachabilityState() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - check that no offsets have been marked as reachable
        assertFalse(marker.isReachable(0), "Offset 0 should remain unreachable");
        assertFalse(marker.isReachable(10), "Offset 10 should remain unreachable");
        assertFalse(marker.isReachable(100), "Offset 100 should remain unreachable");
    }

    /**
     * Tests that visitConstantInstruction doesn't affect reachability range state.
     * Verifies calling visitConstantInstruction doesn't mark any ranges as reachable.
     */
    @Test
    public void testVisitConstantInstruction_doesNotAffectRangeReachabilityState() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - check that no offset ranges have been marked as reachable
        assertFalse(marker.isReachable(0, 10), "Offset range 0-10 should remain unreachable");
        assertFalse(marker.isReachable(50, 100), "Offset range 50-100 should remain unreachable");
    }

    /**
     * Tests that multiple calls to visitConstantInstruction don't accumulate state changes.
     * Verifies the no-op behavior is consistent across multiple invocations.
     */
    @Test
    public void testVisitConstantInstruction_multipleCallsNoStateChange() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_GETSTATIC, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 5, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction1);

        // Assert - state should remain unchanged
        assertFalse(marker.isReachable(0), "Offset 0 should remain unreachable after multiple calls");
        assertFalse(marker.isReachable(5), "Offset 5 should remain unreachable after multiple calls");
        assertFalse(marker.isReachable(10), "Offset 10 should remain unreachable after multiple calls");
        assertFalse(marker.isReachable(0, 100), "Offset range should remain unreachable after multiple calls");
    }

    // ========================================
    // Mock Interaction Tests
    // ========================================

    /**
     * Tests that visitConstantInstruction doesn't interact with mock instruction.
     * Verifies the no-op implementation doesn't call any methods on the instruction.
     */
    @Test
    public void testVisitConstantInstruction_noInteractionWithInstruction() {
        // Arrange
        ConstantInstruction mockInstruction = mock(ConstantInstruction.class);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, mockInstruction);

        // Assert - no methods should be called on the mock
        verifyNoInteractions(mockInstruction);
    }

    /**
     * Tests that visitConstantInstruction doesn't interact with mock clazz.
     * Verifies the no-op implementation doesn't call any methods on the clazz.
     */
    @Test
    public void testVisitConstantInstruction_noInteractionWithClazz() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        marker.visitConstantInstruction(mockClazz, method, codeAttribute, 0, instruction);

        // Assert - no methods should be called on the mock
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitConstantInstruction doesn't interact with mock method.
     * Verifies the no-op implementation doesn't call any methods on the method.
     */
    @Test
    public void testVisitConstantInstruction_noInteractionWithMethod() {
        // Arrange
        Method mockMethod = mock(Method.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        marker.visitConstantInstruction(clazz, mockMethod, codeAttribute, 0, instruction);

        // Assert - no methods should be called on the mock
        verifyNoInteractions(mockMethod);
    }

    /**
     * Tests that visitConstantInstruction doesn't interact with mock codeAttribute.
     * Verifies the no-op implementation doesn't call any methods on the codeAttribute.
     */
    @Test
    public void testVisitConstantInstruction_noInteractionWithCodeAttribute() {
        // Arrange
        CodeAttribute mockCodeAttribute = mock(CodeAttribute.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        marker.visitConstantInstruction(clazz, method, mockCodeAttribute, 0, instruction);

        // Assert - no methods should be called on the mock
        verifyNoInteractions(mockCodeAttribute);
    }

    // ========================================
    // Multiple Instances Tests
    // ========================================

    /**
     * Tests that multiple marker instances behave independently.
     * Verifies that calling visitConstantInstruction on one marker doesn't affect another.
     */
    @Test
    public void testVisitConstantInstruction_multipleInstancesIndependent() {
        // Arrange
        ReachableCodeMarker marker1 = new ReachableCodeMarker();
        ReachableCodeMarker marker2 = new ReachableCodeMarker();
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        marker1.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker2.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - both markers should remain independent with no reachable offsets
        assertFalse(marker1.isReachable(0), "First marker: offset 0 should remain unreachable");
        assertFalse(marker2.isReachable(0), "Second marker: offset 0 should remain unreachable");
    }

    // ========================================
    // Edge Cases Tests
    // ========================================

    /**
     * Tests visitConstantInstruction with various instruction types in sequence.
     * Verifies consistent no-op behavior across different instruction types.
     */
    @Test
    public void testVisitConstantInstruction_variousInstructionTypes_consistentBehavior() {
        // Arrange
        ConstantInstruction ldcInstruction = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction getstaticInstruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 2);
        ConstantInstruction invokevirtualInstruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 3);

        // Act & Assert - all should be no-ops
        assertDoesNotThrow(() -> {
            marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldcInstruction);
            marker.visitConstantInstruction(clazz, method, codeAttribute, 3, getstaticInstruction);
            marker.visitConstantInstruction(clazz, method, codeAttribute, 6, invokevirtualInstruction);
        }, "visitConstantInstruction should handle various instruction types");

        // Verify state remains unchanged
        assertFalse(marker.isReachable(0), "State should remain unchanged after visiting various instructions");
        assertFalse(marker.isReachable(3), "State should remain unchanged at offset 3");
        assertFalse(marker.isReachable(6), "State should remain unchanged at offset 6");
    }

    /**
     * Tests visitConstantInstruction can be called repeatedly without issues.
     * Verifies the marker remains functional after many no-op calls.
     */
    @Test
    public void testVisitConstantInstruction_repeatedCalls_remainsStable() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act - call many times
        for (int i = 0; i < 100; i++) {
            marker.visitConstantInstruction(clazz, method, codeAttribute, i, instruction);
        }

        // Assert - marker should still be functional and state unchanged
        assertFalse(marker.isReachable(0), "State should remain unchanged after many calls");
        assertFalse(marker.isReachable(50), "State should remain unchanged at offset 50");
        assertFalse(marker.isReachable(99), "State should remain unchanged at offset 99");
    }

    /**
     * Tests visitConstantInstruction with the same instruction instance multiple times.
     * Verifies the no-op behavior is consistent even with repeated identical calls.
     */
    @Test
    public void testVisitConstantInstruction_sameInstructionMultipleTimes_consistentNoOp() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - state should remain unchanged even after repeated calls
        assertFalse(marker.isReachable(0), "State should remain unchanged");
    }

    /**
     * Tests visitConstantInstruction with different clazz instances.
     * Verifies the no-op behavior works consistently across different clazz instances.
     */
    @Test
    public void testVisitConstantInstruction_differentClazzInstances_consistentBehavior() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marker.visitConstantInstruction(programClass1, method, codeAttribute, 0, instruction);
            marker.visitConstantInstruction(programClass2, method, codeAttribute, 0, instruction);
            marker.visitConstantInstruction(libraryClass, method, codeAttribute, 0, instruction);
        }, "visitConstantInstruction should handle different clazz instances");
    }

    /**
     * Tests visitConstantInstruction with instructions having different constant pool indices.
     * Verifies the no-op behavior works with various constant pool references.
     */
    @Test
    public void testVisitConstantInstruction_differentConstantPoolIndices_doesNotThrow() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            marker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_LDC, 1));
            marker.visitConstantInstruction(clazz, method, codeAttribute, 3,
                new ConstantInstruction(Instruction.OP_LDC, 100));
            marker.visitConstantInstruction(clazz, method, codeAttribute, 6,
                new ConstantInstruction(Instruction.OP_LDC, 65535));
        }, "visitConstantInstruction should handle different constant pool indices");
    }

    /**
     * Tests visitConstantInstruction with negative offset.
     * Verifies the no-op implementation handles unusual offset values.
     */
    @Test
    public void testVisitConstantInstruction_withNegativeOffset_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, -1, instruction),
            "visitConstantInstruction should handle negative offset");
    }

    /**
     * Tests visitConstantInstruction with very large offset.
     * Verifies the no-op implementation handles large offset values.
     */
    @Test
    public void testVisitConstantInstruction_withLargeOffset_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, instruction),
            "visitConstantInstruction should handle large offset");
    }
}

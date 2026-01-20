package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SideEffectInstructionChecker#visitConstantInstruction}.
 *
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/Method;Lproguard/classfile/attribute/CodeAttribute;ILproguard/classfile/instruction/ConstantInstruction;)V
 *
 * The visitConstantInstruction method checks the opcode of a ConstantInstruction and:
 * - For array/cast instructions: sets hasSideEffects based on includeBuiltInExceptions
 * - For field instructions: sets hasSideEffects based on includeBuiltInExceptions or calls constantPoolEntryAccept
 * - For method invocations: sets hasSideEffects based on includeBuiltInExceptions or calls constantPoolEntryAccept
 *
 * Note: Some instructions call constantPoolEntryAccept which requires complex setup.
 * These tests focus on the directly testable behavior without mocking constant pool interactions.
 */
public class SideEffectInstructionCheckerClaude_visitConstantInstructionTest {

    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    // =========================================================================
    // Array and Cast Instructions - includeBuiltInExceptions
    // =========================================================================

    /**
     * Tests ANEWARRAY instruction with includeBuiltInExceptions enabled.
     * Can throw NegativeArraySizeException.
     */
    @Test
    public void testVisitConstantInstruction_anewarrayWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ANEWARRAY should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests ANEWARRAY instruction with includeBuiltInExceptions disabled.
     */
    @Test
    public void testVisitConstantInstruction_anewarrayWithoutBuiltInExceptions_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ANEWARRAY should set hasSideEffects to false when includeBuiltInExceptions is false");
    }

    /**
     * Tests MULTIANEWARRAY instruction with includeBuiltInExceptions enabled.
     * Can throw NegativeArraySizeException.
     */
    @Test
    public void testVisitConstantInstruction_multianewarrayWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 1, 2);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "MULTIANEWARRAY should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests MULTIANEWARRAY instruction with includeBuiltInExceptions disabled.
     */
    @Test
    public void testVisitConstantInstruction_multianewarrayWithoutBuiltInExceptions_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 1, 2);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "MULTIANEWARRAY should set hasSideEffects to false when includeBuiltInExceptions is false");
    }

    /**
     * Tests CHECKCAST instruction with includeBuiltInExceptions enabled.
     * Can throw ClassCastException.
     */
    @Test
    public void testVisitConstantInstruction_checkcastWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "CHECKCAST should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests CHECKCAST instruction with includeBuiltInExceptions disabled.
     */
    @Test
    public void testVisitConstantInstruction_checkcastWithoutBuiltInExceptions_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "CHECKCAST should set hasSideEffects to false when includeBuiltInExceptions is false");
    }

    /**
     * Tests ANEWARRAY with different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_anewarrayWithDifferentIndices_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test with various constant indices
        ConstantInstruction anewarray1 = new ConstantInstruction(Instruction.OP_ANEWARRAY, 1);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, anewarray1);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, anewarray1));

        ConstantInstruction anewarray10 = new ConstantInstruction(Instruction.OP_ANEWARRAY, 10);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, anewarray10);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, anewarray10));

        ConstantInstruction anewarray100 = new ConstantInstruction(Instruction.OP_ANEWARRAY, 100);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, anewarray100);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, anewarray100));
    }

    // =========================================================================
    // Field Instructions with includeBuiltInExceptions
    // =========================================================================

    /**
     * Tests GETFIELD instruction with includeBuiltInExceptions enabled.
     * Can throw NullPointerException when accessing field on null reference.
     */
    @Test
    public void testVisitConstantInstruction_getfieldWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "GETFIELD should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests GETFIELD instruction with includeBuiltInExceptions disabled.
     * This calls constantPoolEntryAccept which we can't fully test without complex setup,
     * but we verify it doesn't throw and the method executes.
     */
    @Test
    public void testVisitConstantInstruction_getfieldWithoutBuiltInExceptions_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
                "GETFIELD without built-in exceptions should call constantPoolEntryAccept without throwing");

        // Verify constantPoolEntryAccept was called
        verify(clazz).constantPoolEntryAccept(eq(1), eq(checker));
    }

    /**
     * Tests PUTFIELD instruction with includeBuiltInExceptions enabled.
     * Can throw NullPointerException when accessing field on null reference.
     */
    @Test
    public void testVisitConstantInstruction_putfieldWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "PUTFIELD should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests PUTFIELD instruction with includeBuiltInExceptions disabled.
     */
    @Test
    public void testVisitConstantInstruction_putfieldWithoutBuiltInExceptions_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
                "PUTFIELD without built-in exceptions should call constantPoolEntryAccept without throwing");

        // Verify constantPoolEntryAccept was called
        verify(clazz).constantPoolEntryAccept(eq(1), eq(checker));
    }

    /**
     * Tests GETSTATIC instruction calls constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_getstatic_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 5);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(checker));
    }

    /**
     * Tests PUTSTATIC instruction calls constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_putstatic_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 5);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(checker));
    }

    /**
     * Tests GETSTATIC with all flags enabled still calls constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_getstaticWithAllFlags_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(10), eq(checker));
    }

    // =========================================================================
    // Method Invocation Instructions
    // =========================================================================

    /**
     * Tests INVOKEVIRTUAL instruction with includeBuiltInExceptions enabled.
     * Can throw NullPointerException when invoking on null reference.
     */
    @Test
    public void testVisitConstantInstruction_invokevirtualWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "INVOKEVIRTUAL should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests INVOKEVIRTUAL instruction with includeBuiltInExceptions disabled.
     */
    @Test
    public void testVisitConstantInstruction_invokevirtualWithoutBuiltInExceptions_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(1), eq(checker));
    }

    /**
     * Tests INVOKEINTERFACE instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitConstantInstruction_invokeinterfaceWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 1, 2);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "INVOKEINTERFACE should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests INVOKEINTERFACE instruction with includeBuiltInExceptions disabled.
     */
    @Test
    public void testVisitConstantInstruction_invokeinterfaceWithoutBuiltInExceptions_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 1, 2);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(1), eq(checker));
    }

    /**
     * Tests INVOKEDYNAMIC instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitConstantInstruction_invokedynamicWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "INVOKEDYNAMIC should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests INVOKEDYNAMIC instruction with includeBuiltInExceptions disabled.
     */
    @Test
    public void testVisitConstantInstruction_invokedynamicWithoutBuiltInExceptions_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(1), eq(checker));
    }

    /**
     * Tests INVOKESPECIAL instruction always calls constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_invokespecial_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 5);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(checker));
    }

    /**
     * Tests INVOKESTATIC instruction always calls constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_invokestatic_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 5);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(checker));
    }

    /**
     * Tests INVOKESPECIAL with includeBuiltInExceptions enabled still calls constantPoolEntryAccept.
     * INVOKESPECIAL doesn't check includeBuiltInExceptions.
     */
    @Test
    public void testVisitConstantInstruction_invokespecialWithBuiltInExceptions_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(10), eq(checker));
    }

    /**
     * Tests INVOKESTATIC with all flags enabled still calls constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_invokestaticWithAllFlags_callsConstantPoolEntryAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(10), eq(checker));
    }

    // =========================================================================
    // Neutral Instructions - No Side Effects
    // =========================================================================

    /**
     * Tests LDC instruction - should not have side effects.
     */
    @Test
    public void testVisitConstantInstruction_ldc_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "LDC should not have side effects even with all flags enabled");
    }

    /**
     * Tests LDC_W instruction - should not have side effects.
     */
    @Test
    public void testVisitConstantInstruction_ldcW_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC_W, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "LDC_W should not have side effects");
    }

    /**
     * Tests LDC2_W instruction - should not have side effects.
     */
    @Test
    public void testVisitConstantInstruction_ldc2W_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC2_W, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "LDC2_W should not have side effects");
    }

    /**
     * Tests NEW instruction - should not have side effects by itself.
     */
    @Test
    public void testVisitConstantInstruction_new_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "NEW should not have side effects");
    }

    /**
     * Tests INSTANCEOF instruction - should not have side effects.
     */
    @Test
    public void testVisitConstantInstruction_instanceof_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "INSTANCEOF should not have side effects");
    }

    // =========================================================================
    // Integration Tests - Multiple Calls and State Management
    // =========================================================================

    /**
     * Tests that visitConstantInstruction can be called multiple times with state updating correctly.
     */
    @Test
    public void testVisitConstantInstruction_multipleCalls_updatesStateCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - call with different instructions and verify state
        ConstantInstruction checkcast = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, checkcast);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, checkcast),
                "CHECKCAST should set hasSideEffects to true");

        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, ldc),
                "LDC should set hasSideEffects to false");

        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 1);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, getfield);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, getfield),
                "GETFIELD with built-in exceptions should set hasSideEffects to true");
    }

    /**
     * Tests visitConstantInstruction with different offset values.
     */
    @Test
    public void testVisitConstantInstruction_withVariousOffsets_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - offset shouldn't affect the behavior
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction));

        checker.visitConstantInstruction(clazz, method, codeAttribute, 100, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 100, instruction));

        checker.visitConstantInstruction(clazz, method, codeAttribute, -1, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, -1, instruction));
    }

    /**
     * Tests that visitConstantInstruction doesn't throw exceptions for any valid opcode.
     */
    @Test
    public void testVisitConstantInstruction_withAllFlags_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test various opcodes
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_LDC, 1)));
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_CHECKCAST, 1)));
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_GETFIELD, 1)));
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1)));
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_GETSTATIC, 1)));
    }

    /**
     * Tests that the instruction object is not modified by visitConstantInstruction.
     */
    @Test
    public void testVisitConstantInstruction_doesNotModifyInstruction() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 5);
        byte originalOpcode = instruction.opcode;
        int originalConstantIndex = instruction.constantIndex;
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertEquals(originalOpcode, instruction.opcode,
                "visitConstantInstruction should not modify the instruction opcode");
        assertEquals(originalConstantIndex, instruction.constantIndex,
                "visitConstantInstruction should not modify the constant index");
    }

    /**
     * Tests visitConstantInstruction with ProgramClass instance.
     */
    @Test
    public void testVisitConstantInstruction_withProgramClass_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        Clazz realClazz = new ProgramClass();
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitConstantInstruction(realClazz, method, codeAttribute, 0, instruction));
        assertTrue(checker.hasSideEffects(realClazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that all flags can work independently correctly.
     */
    @Test
    public void testVisitConstantInstruction_differentFlagCombinations_behavesCorrectly() {
        // Arrange
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
        ConstantInstruction checkcast = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 1);
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Test with built-in exceptions flag only
        SideEffectInstructionChecker checker1 = new SideEffectInstructionChecker(false, false, true);
        assertTrue(checker1.hasSideEffects(clazz, method, codeAttribute, 0, checkcast),
                "CHECKCAST with built-in exceptions flag should have side effects");
        assertTrue(checker1.hasSideEffects(clazz, method, codeAttribute, 0, getfield),
                "GETFIELD with built-in exceptions flag should have side effects");
        assertFalse(checker1.hasSideEffects(clazz, method, codeAttribute, 0, ldc),
                "LDC should not have side effects");

        // Test with no flags
        SideEffectInstructionChecker checker2 = new SideEffectInstructionChecker(false, false, false);
        assertFalse(checker2.hasSideEffects(clazz, method, codeAttribute, 0, checkcast),
                "CHECKCAST without built-in exceptions flag should not have side effects");
        assertFalse(checker2.hasSideEffects(clazz, method, codeAttribute, 0, ldc),
                "LDC should not have side effects");

        // Test with return and array store flags (should not affect constant instructions)
        SideEffectInstructionChecker checker3 = new SideEffectInstructionChecker(true, true, false);
        assertFalse(checker3.hasSideEffects(clazz, method, codeAttribute, 0, checkcast),
                "CHECKCAST without built-in exceptions flag should not have side effects");
        assertFalse(checker3.hasSideEffects(clazz, method, codeAttribute, 0, ldc),
                "LDC should not have side effects");
    }

    /**
     * Tests that array/cast instructions are affected by includeBuiltInExceptions flag.
     */
    @Test
    public void testVisitConstantInstruction_arrayCastInstructions_affectedByBuiltInExceptionsFlag() {
        // Arrange
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        SideEffectInstructionChecker checkerWithFlag = new SideEffectInstructionChecker(false, false, true);
        SideEffectInstructionChecker checkerWithoutFlag = new SideEffectInstructionChecker(false, false, false);

        // Act & Assert - all three instructions should be affected by the flag
        ConstantInstruction anewarray = new ConstantInstruction(Instruction.OP_ANEWARRAY, 1);
        assertTrue(checkerWithFlag.hasSideEffects(clazz, method, codeAttribute, 0, anewarray));
        assertFalse(checkerWithoutFlag.hasSideEffects(clazz, method, codeAttribute, 0, anewarray));

        ConstantInstruction multianewarray = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 1, 2);
        assertTrue(checkerWithFlag.hasSideEffects(clazz, method, codeAttribute, 0, multianewarray));
        assertFalse(checkerWithoutFlag.hasSideEffects(clazz, method, codeAttribute, 0, multianewarray));

        ConstantInstruction checkcast = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);
        assertTrue(checkerWithFlag.hasSideEffects(clazz, method, codeAttribute, 0, checkcast));
        assertFalse(checkerWithoutFlag.hasSideEffects(clazz, method, codeAttribute, 0, checkcast));
    }

    /**
     * Tests that constantPoolEntryAccept is called with correct constant indices.
     */
    @Test
    public void testVisitConstantInstruction_constantPoolEntryAccept_calledWithCorrectIndex() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - verify correct indices are passed
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_GETSTATIC, 1));
        verify(clazz).constantPoolEntryAccept(eq(1), eq(checker));

        checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_GETSTATIC, 10));
        verify(clazz).constantPoolEntryAccept(eq(10), eq(checker));

        checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_GETSTATIC, 100));
        verify(clazz).constantPoolEntryAccept(eq(100), eq(checker));
    }

    /**
     * Tests that INVOKEVIRTUAL, INVOKEINTERFACE, and INVOKEDYNAMIC with includeBuiltInExceptions
     * set hasSideEffects directly without calling constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_virtualInvokesWithBuiltInExceptions_setsHasSideEffectsDirectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1));
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 1, 2));
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 1));

        // Assert - constantPoolEntryAccept should NOT be called when includeBuiltInExceptions is true
        verify(clazz, never()).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that INVOKESPECIAL and INVOKESTATIC always call constantPoolEntryAccept
     * regardless of includeBuiltInExceptions flag.
     */
    @Test
    public void testVisitConstantInstruction_specialStaticInvokes_alwaysCallConstantPoolEntryAccept() {
        // Arrange with includeBuiltInExceptions=true
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 5));
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_INVOKESTATIC, 10));

        // Assert - constantPoolEntryAccept should be called even with includeBuiltInExceptions=true
        verify(clazz).constantPoolEntryAccept(eq(5), eq(checker));
        verify(clazz).constantPoolEntryAccept(eq(10), eq(checker));
    }
}

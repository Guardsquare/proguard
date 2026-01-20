package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.VariableInstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SideEffectInstructionChecker#visitVariableInstruction}.
 *
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/Method;Lproguard/classfile/attribute/CodeAttribute;ILproguard/classfile/instruction/VariableInstruction;)V
 *
 * The visitVariableInstruction method checks the opcode of a VariableInstruction and sets the
 * hasSideEffects field based on:
 * - The opcode (only RET is checked)
 * - The includeReturnInstructions flag
 *
 * Note: This method modifies internal state (hasSideEffects field) which is then read by the hasSideEffects() method.
 * We test this by calling visitVariableInstruction and then checking the result via hasSideEffects().
 */
public class SideEffectInstructionCheckerClaude_visitVariableInstructionTest {

    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    // =========================================================================
    // RET Instruction Tests - includeReturnInstructions
    // =========================================================================

    /**
     * Tests RET instruction with includeReturnInstructions enabled.
     * RET is used for returning from a subroutine (JSR/RET pattern).
     */
    @Test
    public void testVisitVariableInstruction_retWithReturnEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify by checking hasSideEffects
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "RET should set hasSideEffects to true when includeReturnInstructions is true");
    }

    /**
     * Tests RET instruction with includeReturnInstructions disabled.
     */
    @Test
    public void testVisitVariableInstruction_retWithReturnDisabled_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "RET should set hasSideEffects to false when includeReturnInstructions is false");
    }

    /**
     * Tests RET instruction with different variable indices.
     */
    @Test
    public void testVisitVariableInstruction_retWithDifferentVariableIndices_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test with various variable indices
        VariableInstruction ret0 = new VariableInstruction(Instruction.OP_RET, 0);
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, ret0);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, ret0),
                "RET with variable index 0 should set hasSideEffects to true");

        VariableInstruction ret1 = new VariableInstruction(Instruction.OP_RET, 1);
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, ret1);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, ret1),
                "RET with variable index 1 should set hasSideEffects to true");

        VariableInstruction ret5 = new VariableInstruction(Instruction.OP_RET, 5);
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, ret5);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, ret5),
                "RET with variable index 5 should set hasSideEffects to true");
    }

    /**
     * Tests RET instruction with all flags enabled.
     */
    @Test
    public void testVisitVariableInstruction_retWithAllFlagsEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "RET should set hasSideEffects to true when includeReturnInstructions is enabled");
    }

    /**
     * Tests RET instruction with all flags disabled.
     */
    @Test
    public void testVisitVariableInstruction_retWithAllFlagsDisabled_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "RET should set hasSideEffects to false when includeReturnInstructions is disabled");
    }

    /**
     * Tests RET instruction with only array store flag enabled (should not affect RET).
     */
    @Test
    public void testVisitVariableInstruction_retWithOnlyArrayStoreFlag_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "RET should set hasSideEffects to false when only arrayStore flag is enabled");
    }

    /**
     * Tests RET instruction with only built-in exceptions flag enabled (should not affect RET).
     */
    @Test
    public void testVisitVariableInstruction_retWithOnlyBuiltInExceptionsFlag_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "RET should set hasSideEffects to false when only builtInExceptions flag is enabled");
    }

    // =========================================================================
    // Load Instructions - No Side Effects
    // =========================================================================

    /**
     * Tests ILOAD instruction - should never have side effects.
     */
    @Test
    public void testVisitVariableInstruction_iload_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_ILOAD, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ILOAD should never set hasSideEffects even with all flags enabled");
    }

    /**
     * Tests LLOAD instruction - should never have side effects.
     */
    @Test
    public void testVisitVariableInstruction_lload_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_LLOAD, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "LLOAD should never set hasSideEffects");
    }

    /**
     * Tests FLOAD instruction - should never have side effects.
     */
    @Test
    public void testVisitVariableInstruction_fload_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_FLOAD, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "FLOAD should never set hasSideEffects");
    }

    /**
     * Tests DLOAD instruction - should never have side effects.
     */
    @Test
    public void testVisitVariableInstruction_dload_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_DLOAD, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "DLOAD should never set hasSideEffects");
    }

    /**
     * Tests ALOAD instruction - should never have side effects.
     */
    @Test
    public void testVisitVariableInstruction_aload_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_ALOAD, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ALOAD should never set hasSideEffects");
    }

    /**
     * Tests ILOAD with variable index 0 (common for 'this' or first parameter).
     */
    @Test
    public void testVisitVariableInstruction_iloadWithIndex0_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_ILOAD, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ILOAD with index 0 should never set hasSideEffects");
    }

    /**
     * Tests ALOAD with variable index 0 (typically 'this' reference).
     */
    @Test
    public void testVisitVariableInstruction_aloadWithIndex0_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_ALOAD, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ALOAD_0 (this) should never set hasSideEffects");
    }

    // =========================================================================
    // Store Instructions - No Side Effects
    // =========================================================================

    /**
     * Tests ISTORE instruction - should never have side effects.
     */
    @Test
    public void testVisitVariableInstruction_istore_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_ISTORE, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ISTORE should never set hasSideEffects even with all flags enabled");
    }

    /**
     * Tests LSTORE instruction - should never have side effects.
     */
    @Test
    public void testVisitVariableInstruction_lstore_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_LSTORE, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "LSTORE should never set hasSideEffects");
    }

    /**
     * Tests FSTORE instruction - should never have side effects.
     */
    @Test
    public void testVisitVariableInstruction_fstore_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_FSTORE, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "FSTORE should never set hasSideEffects");
    }

    /**
     * Tests DSTORE instruction - should never have side effects.
     */
    @Test
    public void testVisitVariableInstruction_dstore_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_DSTORE, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "DSTORE should never set hasSideEffects");
    }

    /**
     * Tests ASTORE instruction - should never have side effects.
     */
    @Test
    public void testVisitVariableInstruction_astore_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_ASTORE, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ASTORE should never set hasSideEffects");
    }

    /**
     * Tests ISTORE with variable index 0.
     */
    @Test
    public void testVisitVariableInstruction_istoreWithIndex0_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_ISTORE, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ISTORE with index 0 should never set hasSideEffects");
    }

    // =========================================================================
    // IINC Instruction - No Side Effects
    // =========================================================================

    /**
     * Tests IINC instruction - should never have side effects.
     * IINC increments a local variable by a constant.
     */
    @Test
    public void testVisitVariableInstruction_iinc_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_IINC, 1, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IINC should never set hasSideEffects even with all flags enabled");
    }

    /**
     * Tests IINC with different constants.
     */
    @Test
    public void testVisitVariableInstruction_iincWithVariousConstants_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test with various increment values
        VariableInstruction iinc1 = new VariableInstruction(Instruction.OP_IINC, 1, 1);
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, iinc1);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, iinc1),
                "IINC by 1 should not have side effects");

        VariableInstruction iincMinus1 = new VariableInstruction(Instruction.OP_IINC, 1, -1);
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, iincMinus1);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, iincMinus1),
                "IINC by -1 should not have side effects");

        VariableInstruction iinc10 = new VariableInstruction(Instruction.OP_IINC, 1, 10);
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, iinc10);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, iinc10),
                "IINC by 10 should not have side effects");
    }

    // =========================================================================
    // Integration Tests - Multiple Calls and State Management
    // =========================================================================

    /**
     * Tests that visitVariableInstruction can be called multiple times with state updating correctly.
     */
    @Test
    public void testVisitVariableInstruction_multipleCalls_updatesStateCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - call with different instructions and verify state
        VariableInstruction ret = new VariableInstruction(Instruction.OP_RET, 0);
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, ret);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, ret),
                "RET should set hasSideEffects to true");

        VariableInstruction iload = new VariableInstruction(Instruction.OP_ILOAD, 1);
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, iload);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, iload),
                "ILOAD should set hasSideEffects to false");

        VariableInstruction istore = new VariableInstruction(Instruction.OP_ISTORE, 1);
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, istore);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, istore),
                "ISTORE should set hasSideEffects to false");
    }

    /**
     * Tests visitVariableInstruction with different offset values.
     */
    @Test
    public void testVisitVariableInstruction_withVariousOffsets_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - offset shouldn't affect the behavior
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction));

        checker.visitVariableInstruction(clazz, method, codeAttribute, 100, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 100, instruction));

        checker.visitVariableInstruction(clazz, method, codeAttribute, -1, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, -1, instruction));
    }

    /**
     * Tests that visitVariableInstruction doesn't throw exceptions for any valid opcode.
     */
    @Test
    public void testVisitVariableInstruction_withAllFlags_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test various opcodes
        assertDoesNotThrow(() -> checker.visitVariableInstruction(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_RET, 0)));
        assertDoesNotThrow(() -> checker.visitVariableInstruction(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_ILOAD, 1)));
        assertDoesNotThrow(() -> checker.visitVariableInstruction(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_ISTORE, 1)));
        assertDoesNotThrow(() -> checker.visitVariableInstruction(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_ALOAD, 0)));
        assertDoesNotThrow(() -> checker.visitVariableInstruction(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_IINC, 1, 1)));
    }

    /**
     * Tests that the instruction object is not modified by visitVariableInstruction.
     */
    @Test
    public void testVisitVariableInstruction_doesNotModifyInstruction() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 5);
        byte originalOpcode = instruction.opcode;
        int originalVariableIndex = instruction.variableIndex;
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertEquals(originalOpcode, instruction.opcode,
                "visitVariableInstruction should not modify the instruction opcode");
        assertEquals(originalVariableIndex, instruction.variableIndex,
                "visitVariableInstruction should not modify the variable index");
    }

    /**
     * Tests visitVariableInstruction with ProgramClass instance.
     */
    @Test
    public void testVisitVariableInstruction_withProgramClass_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        Clazz realClazz = new ProgramClass();
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitVariableInstruction(realClazz, method, codeAttribute, 0, instruction));
        assertTrue(checker.hasSideEffects(realClazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests visitVariableInstruction with LibraryClass instance.
     */
    @Test
    public void testVisitVariableInstruction_withLibraryClass_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        Clazz realClazz = new LibraryClass();
        Method method = mock(LibraryMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitVariableInstruction(realClazz, method, codeAttribute, 0, instruction));
        assertTrue(checker.hasSideEffects(realClazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that all flags can work independently correctly.
     */
    @Test
    public void testVisitVariableInstruction_differentFlagCombinations_behavesCorrectly() {
        // Arrange
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
        VariableInstruction ret = new VariableInstruction(Instruction.OP_RET, 0);
        VariableInstruction iload = new VariableInstruction(Instruction.OP_ILOAD, 1);

        // Test with return flag only
        SideEffectInstructionChecker checker1 = new SideEffectInstructionChecker(true, false, false);
        assertTrue(checker1.hasSideEffects(clazz, method, codeAttribute, 0, ret),
                "RET with return flag should have side effects");
        assertFalse(checker1.hasSideEffects(clazz, method, codeAttribute, 0, iload),
                "ILOAD with return flag should not have side effects");

        // Test with array store flag only
        SideEffectInstructionChecker checker2 = new SideEffectInstructionChecker(false, true, false);
        assertFalse(checker2.hasSideEffects(clazz, method, codeAttribute, 0, ret),
                "RET with array store flag only should not have side effects");
        assertFalse(checker2.hasSideEffects(clazz, method, codeAttribute, 0, iload),
                "ILOAD with array store flag should not have side effects");

        // Test with built-in exceptions flag only
        SideEffectInstructionChecker checker3 = new SideEffectInstructionChecker(false, false, true);
        assertFalse(checker3.hasSideEffects(clazz, method, codeAttribute, 0, ret),
                "RET with built-in exceptions flag only should not have side effects");
        assertFalse(checker3.hasSideEffects(clazz, method, codeAttribute, 0, iload),
                "ILOAD with built-in exceptions flag should not have side effects");
    }

    /**
     * Tests that load and store instructions never have side effects regardless of flags.
     */
    @Test
    public void testVisitVariableInstruction_loadAndStoreInstructions_neverHaveSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test all load/store instructions
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_ILOAD, 1)), "ILOAD should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_LLOAD, 1)), "LLOAD should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_FLOAD, 1)), "FLOAD should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_DLOAD, 1)), "DLOAD should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_ALOAD, 1)), "ALOAD should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_ISTORE, 1)), "ISTORE should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_LSTORE, 1)), "LSTORE should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_FSTORE, 1)), "FSTORE should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_DSTORE, 1)), "DSTORE should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_ASTORE, 1)), "ASTORE should not have side effects");
    }

    /**
     * Tests RET instruction is the only variable instruction affected by includeReturnInstructions.
     */
    @Test
    public void testVisitVariableInstruction_onlyRetAffectedByReturnFlag() {
        // Arrange
        SideEffectInstructionChecker checkerWithReturn = new SideEffectInstructionChecker(true, false, false);
        SideEffectInstructionChecker checkerWithoutReturn = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Test RET is affected
        VariableInstruction ret = new VariableInstruction(Instruction.OP_RET, 0);
        assertTrue(checkerWithReturn.hasSideEffects(clazz, method, codeAttribute, 0, ret),
                "RET with return flag should have side effects");
        assertFalse(checkerWithoutReturn.hasSideEffects(clazz, method, codeAttribute, 0, ret),
                "RET without return flag should not have side effects");

        // Test other variable instructions are not affected
        VariableInstruction iload = new VariableInstruction(Instruction.OP_ILOAD, 1);
        assertFalse(checkerWithReturn.hasSideEffects(clazz, method, codeAttribute, 0, iload),
                "ILOAD should not be affected by return flag");
        assertFalse(checkerWithoutReturn.hasSideEffects(clazz, method, codeAttribute, 0, iload),
                "ILOAD should not be affected by return flag");
    }

    /**
     * Tests visitVariableInstruction with large variable indices.
     */
    @Test
    public void testVisitVariableInstruction_withLargeVariableIndices_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test with large variable indices
        VariableInstruction iload100 = new VariableInstruction(Instruction.OP_ILOAD, 100);
        assertDoesNotThrow(() -> checker.visitVariableInstruction(clazz, method, codeAttribute, 0, iload100));
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, iload100));

        VariableInstruction iload255 = new VariableInstruction(Instruction.OP_ILOAD, 255);
        assertDoesNotThrow(() -> checker.visitVariableInstruction(clazz, method, codeAttribute, 0, iload255));
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, iload255));
    }

    /**
     * Tests that calling visitVariableInstruction multiple times doesn't cause issues.
     */
    @Test
    public void testVisitVariableInstruction_calledMultipleTimes_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - call multiple times
        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction));

        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction));

        checker.visitVariableInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction));
    }
}

package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.BranchInstruction;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SideEffectInstructionChecker#visitBranchInstruction}.
 *
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/Method;Lproguard/classfile/attribute/CodeAttribute;ILproguard/classfile/instruction/BranchInstruction;)V
 *
 * The visitBranchInstruction method checks the opcode of a BranchInstruction and sets the
 * hasSideEffects field based on:
 * - The opcode (only JSR and JSR_W are checked)
 * - The includeReturnInstructions flag
 *
 * Note: This method modifies internal state (hasSideEffects field) which is then read by the hasSideEffects() method.
 * We test this by calling visitBranchInstruction and then checking the result via hasSideEffects().
 */
public class SideEffectInstructionCheckerClaude_visitBranchInstructionTest {

    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    // =========================================================================
    // JSR Instruction Tests - includeReturnInstructions
    // =========================================================================

    /**
     * Tests JSR instruction with includeReturnInstructions enabled.
     * JSR (Jump to Subroutine) is used with RET for subroutine calls.
     */
    @Test
    public void testVisitBranchInstruction_jsrWithReturnEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify by checking hasSideEffects
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "JSR should set hasSideEffects to true when includeReturnInstructions is true");
    }

    /**
     * Tests JSR instruction with includeReturnInstructions disabled.
     */
    @Test
    public void testVisitBranchInstruction_jsrWithReturnDisabled_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "JSR should set hasSideEffects to false when includeReturnInstructions is false");
    }

    /**
     * Tests JSR_W instruction with includeReturnInstructions enabled.
     * JSR_W is the wide version of JSR (uses 4-byte branch offset instead of 2-byte).
     */
    @Test
    public void testVisitBranchInstruction_jsrWWithReturnEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR_W, 1000);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "JSR_W should set hasSideEffects to true when includeReturnInstructions is true");
    }

    /**
     * Tests JSR_W instruction with includeReturnInstructions disabled.
     */
    @Test
    public void testVisitBranchInstruction_jsrWWithReturnDisabled_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR_W, 1000);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "JSR_W should set hasSideEffects to false when includeReturnInstructions is false");
    }

    /**
     * Tests JSR instruction with different branch offsets.
     */
    @Test
    public void testVisitBranchInstruction_jsrWithDifferentOffsets_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test with various branch offsets
        BranchInstruction jsr5 = new BranchInstruction(Instruction.OP_JSR, 5);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, jsr5);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, jsr5),
                "JSR with offset 5 should set hasSideEffects to true");

        BranchInstruction jsr10 = new BranchInstruction(Instruction.OP_JSR, 10);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, jsr10);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, jsr10),
                "JSR with offset 10 should set hasSideEffects to true");

        BranchInstruction jsr100 = new BranchInstruction(Instruction.OP_JSR, 100);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, jsr100);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, jsr100),
                "JSR with offset 100 should set hasSideEffects to true");
    }

    /**
     * Tests JSR_W instruction with different branch offsets.
     */
    @Test
    public void testVisitBranchInstruction_jsrWWithDifferentOffsets_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test with various large offsets (JSR_W handles larger offsets)
        BranchInstruction jsrW1000 = new BranchInstruction(Instruction.OP_JSR_W, 1000);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, jsrW1000);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, jsrW1000),
                "JSR_W with offset 1000 should set hasSideEffects to true");

        BranchInstruction jsrW10000 = new BranchInstruction(Instruction.OP_JSR_W, 10000);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, jsrW10000);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, jsrW10000),
                "JSR_W with offset 10000 should set hasSideEffects to true");

        BranchInstruction jsrW100000 = new BranchInstruction(Instruction.OP_JSR_W, 100000);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, jsrW100000);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, jsrW100000),
                "JSR_W with offset 100000 should set hasSideEffects to true");
    }

    /**
     * Tests JSR instruction with all flags enabled.
     */
    @Test
    public void testVisitBranchInstruction_jsrWithAllFlagsEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "JSR should set hasSideEffects to true when includeReturnInstructions is enabled");
    }

    /**
     * Tests JSR instruction with all flags disabled.
     */
    @Test
    public void testVisitBranchInstruction_jsrWithAllFlagsDisabled_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "JSR should set hasSideEffects to false when includeReturnInstructions is disabled");
    }

    /**
     * Tests JSR instruction with only array store flag enabled (should not affect JSR).
     */
    @Test
    public void testVisitBranchInstruction_jsrWithOnlyArrayStoreFlag_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "JSR should set hasSideEffects to false when only arrayStore flag is enabled");
    }

    /**
     * Tests JSR instruction with only built-in exceptions flag enabled (should not affect JSR).
     */
    @Test
    public void testVisitBranchInstruction_jsrWithOnlyBuiltInExceptionsFlag_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "JSR should set hasSideEffects to false when only builtInExceptions flag is enabled");
    }

    /**
     * Tests JSR_W instruction with only return flag enabled.
     */
    @Test
    public void testVisitBranchInstruction_jsrWWithOnlyReturnFlag_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR_W, 1000);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "JSR_W should set hasSideEffects to true when only return flag is enabled");
    }

    // =========================================================================
    // Conditional Branch Instructions - No Side Effects
    // =========================================================================

    /**
     * Tests IFEQ instruction - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_ifeq_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_IFEQ, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IFEQ should never set hasSideEffects even with all flags enabled");
    }

    /**
     * Tests IFNE instruction - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_ifne_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_IFNE, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IFNE should never set hasSideEffects");
    }

    /**
     * Tests IFLT instruction - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_iflt_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_IFLT, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IFLT should never set hasSideEffects");
    }

    /**
     * Tests IFGE instruction - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_ifge_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_IFGE, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IFGE should never set hasSideEffects");
    }

    /**
     * Tests IFGT instruction - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_ifgt_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_IFGT, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IFGT should never set hasSideEffects");
    }

    /**
     * Tests IFLE instruction - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_ifle_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_IFLE, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IFLE should never set hasSideEffects");
    }

    /**
     * Tests IF_ICMPEQ instruction - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_ificmpeq_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_IFICMPEQ, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IF_ICMPEQ should never set hasSideEffects");
    }

    /**
     * Tests IF_ACMPEQ instruction - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_ifacmpeq_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_IFACMPEQ, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IF_ACMPEQ should never set hasSideEffects");
    }

    /**
     * Tests IFNULL instruction - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_ifnull_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_IFNULL, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IFNULL should never set hasSideEffects");
    }

    /**
     * Tests IFNONNULL instruction - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_ifnonnull_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_IFNONNULL, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IFNONNULL should never set hasSideEffects");
    }

    // =========================================================================
    // Unconditional Branch Instructions - No Side Effects
    // =========================================================================

    /**
     * Tests GOTO instruction - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_goto_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_GOTO, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "GOTO should never set hasSideEffects even with all flags enabled");
    }

    /**
     * Tests GOTO_W instruction - should never have side effects.
     * GOTO_W is the wide version of GOTO.
     */
    @Test
    public void testVisitBranchInstruction_gotoW_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_GOTO_W, 1000);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "GOTO_W should never set hasSideEffects");
    }

    /**
     * Tests GOTO with various offsets - should never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_gotoWithVariousOffsets_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test with various offsets
        BranchInstruction goto5 = new BranchInstruction(Instruction.OP_GOTO, 5);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, goto5);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, goto5),
                "GOTO with offset 5 should not have side effects");

        BranchInstruction gotoNeg10 = new BranchInstruction(Instruction.OP_GOTO, -10);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, gotoNeg10);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, gotoNeg10),
                "GOTO with negative offset should not have side effects");
    }

    // =========================================================================
    // Integration Tests - Multiple Calls and State Management
    // =========================================================================

    /**
     * Tests that visitBranchInstruction can be called multiple times with state updating correctly.
     */
    @Test
    public void testVisitBranchInstruction_multipleCalls_updatesStateCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - call with different instructions and verify state
        BranchInstruction jsr = new BranchInstruction(Instruction.OP_JSR, 10);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, jsr);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, jsr),
                "JSR should set hasSideEffects to true");

        BranchInstruction goto_ = new BranchInstruction(Instruction.OP_GOTO, 10);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, goto_);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, goto_),
                "GOTO should set hasSideEffects to false");

        BranchInstruction ifeq = new BranchInstruction(Instruction.OP_IFEQ, 10);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, ifeq);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, ifeq),
                "IFEQ should set hasSideEffects to false");
    }

    /**
     * Tests visitBranchInstruction with different offset values (instruction location).
     */
    @Test
    public void testVisitBranchInstruction_withVariousOffsets_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - offset (instruction location) shouldn't affect the behavior
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction));

        checker.visitBranchInstruction(clazz, method, codeAttribute, 100, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 100, instruction));

        checker.visitBranchInstruction(clazz, method, codeAttribute, -1, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, -1, instruction));
    }

    /**
     * Tests that visitBranchInstruction doesn't throw exceptions for any valid opcode.
     */
    @Test
    public void testVisitBranchInstruction_withAllFlags_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test various opcodes
        assertDoesNotThrow(() -> checker.visitBranchInstruction(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_JSR, 10)));
        assertDoesNotThrow(() -> checker.visitBranchInstruction(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_JSR_W, 1000)));
        assertDoesNotThrow(() -> checker.visitBranchInstruction(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_GOTO, 10)));
        assertDoesNotThrow(() -> checker.visitBranchInstruction(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFEQ, 10)));
        assertDoesNotThrow(() -> checker.visitBranchInstruction(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFNULL, 10)));
    }

    /**
     * Tests that the instruction object is not modified by visitBranchInstruction.
     */
    @Test
    public void testVisitBranchInstruction_doesNotModifyInstruction() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 50);
        byte originalOpcode = instruction.opcode;
        int originalBranchOffset = instruction.branchOffset;
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertEquals(originalOpcode, instruction.opcode,
                "visitBranchInstruction should not modify the instruction opcode");
        assertEquals(originalBranchOffset, instruction.branchOffset,
                "visitBranchInstruction should not modify the branch offset");
    }

    /**
     * Tests visitBranchInstruction with ProgramClass instance.
     */
    @Test
    public void testVisitBranchInstruction_withProgramClass_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        Clazz realClazz = new ProgramClass();
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitBranchInstruction(realClazz, method, codeAttribute, 0, instruction));
        assertTrue(checker.hasSideEffects(realClazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests visitBranchInstruction with LibraryClass instance.
     */
    @Test
    public void testVisitBranchInstruction_withLibraryClass_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        Clazz realClazz = new LibraryClass();
        Method method = mock(LibraryMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitBranchInstruction(realClazz, method, codeAttribute, 0, instruction));
        assertTrue(checker.hasSideEffects(realClazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that all flags can work independently correctly.
     */
    @Test
    public void testVisitBranchInstruction_differentFlagCombinations_behavesCorrectly() {
        // Arrange
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
        BranchInstruction jsr = new BranchInstruction(Instruction.OP_JSR, 10);
        BranchInstruction goto_ = new BranchInstruction(Instruction.OP_GOTO, 10);

        // Test with return flag only
        SideEffectInstructionChecker checker1 = new SideEffectInstructionChecker(true, false, false);
        assertTrue(checker1.hasSideEffects(clazz, method, codeAttribute, 0, jsr),
                "JSR with return flag should have side effects");
        assertFalse(checker1.hasSideEffects(clazz, method, codeAttribute, 0, goto_),
                "GOTO with return flag should not have side effects");

        // Test with array store flag only
        SideEffectInstructionChecker checker2 = new SideEffectInstructionChecker(false, true, false);
        assertFalse(checker2.hasSideEffects(clazz, method, codeAttribute, 0, jsr),
                "JSR with array store flag only should not have side effects");
        assertFalse(checker2.hasSideEffects(clazz, method, codeAttribute, 0, goto_),
                "GOTO with array store flag should not have side effects");

        // Test with built-in exceptions flag only
        SideEffectInstructionChecker checker3 = new SideEffectInstructionChecker(false, false, true);
        assertFalse(checker3.hasSideEffects(clazz, method, codeAttribute, 0, jsr),
                "JSR with built-in exceptions flag only should not have side effects");
        assertFalse(checker3.hasSideEffects(clazz, method, codeAttribute, 0, goto_),
                "GOTO with built-in exceptions flag should not have side effects");
    }

    /**
     * Tests that only JSR and JSR_W are affected by includeReturnInstructions flag.
     */
    @Test
    public void testVisitBranchInstruction_onlyJsrAffectedByReturnFlag() {
        // Arrange
        SideEffectInstructionChecker checkerWithReturn = new SideEffectInstructionChecker(true, false, false);
        SideEffectInstructionChecker checkerWithoutReturn = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Test JSR is affected
        BranchInstruction jsr = new BranchInstruction(Instruction.OP_JSR, 10);
        assertTrue(checkerWithReturn.hasSideEffects(clazz, method, codeAttribute, 0, jsr),
                "JSR with return flag should have side effects");
        assertFalse(checkerWithoutReturn.hasSideEffects(clazz, method, codeAttribute, 0, jsr),
                "JSR without return flag should not have side effects");

        // Test JSR_W is affected
        BranchInstruction jsrW = new BranchInstruction(Instruction.OP_JSR_W, 1000);
        assertTrue(checkerWithReturn.hasSideEffects(clazz, method, codeAttribute, 0, jsrW),
                "JSR_W with return flag should have side effects");
        assertFalse(checkerWithoutReturn.hasSideEffects(clazz, method, codeAttribute, 0, jsrW),
                "JSR_W without return flag should not have side effects");

        // Test other branch instructions are not affected
        BranchInstruction goto_ = new BranchInstruction(Instruction.OP_GOTO, 10);
        assertFalse(checkerWithReturn.hasSideEffects(clazz, method, codeAttribute, 0, goto_),
                "GOTO should not be affected by return flag");
        assertFalse(checkerWithoutReturn.hasSideEffects(clazz, method, codeAttribute, 0, goto_),
                "GOTO should not be affected by return flag");
    }

    /**
     * Tests that all conditional branch instructions never have side effects.
     */
    @Test
    public void testVisitBranchInstruction_conditionalBranches_neverHaveSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test various conditional branch instructions
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFEQ, 10)), "IFEQ should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFNE, 10)), "IFNE should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFLT, 10)), "IFLT should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFGE, 10)), "IFGE should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFGT, 10)), "IFGT should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFLE, 10)), "IFLE should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFICMPEQ, 10)), "IF_ICMPEQ should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFACMPEQ, 10)), "IF_ACMPEQ should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFNULL, 10)), "IFNULL should not have side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_IFNONNULL, 10)), "IFNONNULL should not have side effects");
    }

    /**
     * Tests that calling visitBranchInstruction multiple times doesn't cause issues.
     */
    @Test
    public void testVisitBranchInstruction_calledMultipleTimes_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - call multiple times
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction));

        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction));

        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests negative branch offsets (backward jumps).
     */
    @Test
    public void testVisitBranchInstruction_negativeBranchOffsets_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - negative offsets (backward jumps) should work
        BranchInstruction jsrBackward = new BranchInstruction(Instruction.OP_JSR, -10);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, jsrBackward);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, jsrBackward),
                "JSR with negative offset should have side effects");

        BranchInstruction gotoBackward = new BranchInstruction(Instruction.OP_GOTO, -20);
        checker.visitBranchInstruction(clazz, method, codeAttribute, 0, gotoBackward);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, gotoBackward),
                "GOTO with negative offset should not have side effects");
    }
}

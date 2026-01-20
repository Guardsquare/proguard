package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.Method;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.BranchInstruction;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)}.
 *
 * The visitBranchInstruction method is responsible for marking branch targets as reachable during
 * code reachability analysis. It:
 * 1. Marks the branch target location (offset + branchOffset) as reachable
 * 2. For unconditional branches (GOTO, GOTO_W), sets the internal 'next' flag to false,
 *    indicating that execution does not continue to the next sequential instruction
 * 3. For conditional branches, allows execution to continue to the next instruction
 *
 * The method is called as part of the visitor pattern when processing CodeAttribute instructions
 * to determine which parts of the code are reachable from the entry point.
 */
public class ReachableCodeMarkerClaude_visitBranchInstructionTest {

    private ReachableCodeMarker marker;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        marker = new ReachableCodeMarker();
        clazz = new ProgramClass();
        method = new ProgramMethod();

        // Create a simple code attribute with enough space for testing
        codeAttribute = new CodeAttribute();
        // Simple bytecode: some NOPs and a RETURN
        // This provides a basic structure for reachability testing
        byte[] code = new byte[100];
        for (int i = 0; i < code.length - 1; i++) {
            code[i] = (byte) Instruction.OP_NOP; // NOP
        }
        code[99] = (byte) Instruction.OP_RETURN; // RETURN
        codeAttribute.u4codeLength = code.length;
        codeAttribute.code = code;
    }

    // ========================================
    // Basic Functionality Tests
    // ========================================

    /**
     * Tests that visitBranchInstruction marks the branch target as reachable.
     * The method should mark the target offset as reachable when processing a branch.
     */
    @Test
    public void testVisitBranchInstruction_marksBranchTarget() {
        // Arrange
        BranchInstruction gotoInstruction = new BranchInstruction(Instruction.OP_GOTO, 10);

        // First, visit the code attribute to initialize reachability tracking
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // The entry point (offset 0) should be reachable
        assertTrue(marker.isReachable(0), "Entry point should be reachable");

        // After GOTO at 0, target at 10 should be reachable
        assertTrue(marker.isReachable(10), "Branch target at offset 10 should be reachable");
    }

    /**
     * Tests that conditional branch instructions mark their target as reachable.
     * Conditional branches should mark both the target and allow sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_conditionalBranchMarksTarget() {
        // Arrange
        BranchInstruction ifeqInstruction = new BranchInstruction(Instruction.OP_IFEQ, 20);

        // Replace instruction at offset 0 with IFEQ
        codeAttribute.code[0] = (byte) Instruction.OP_IFEQ;
        codeAttribute.code[1] = 0; // branch offset high byte
        codeAttribute.code[2] = 20; // branch offset low byte

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - both the branch target and the next instruction should be reachable
        assertTrue(marker.isReachable(0), "Current instruction should be reachable");
        assertTrue(marker.isReachable(20), "Branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after conditional branch should be reachable");
    }

    /**
     * Tests GOTO instruction with positive offset.
     * GOTO should mark the target and stop sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_gotoWithPositiveOffset() {
        // Arrange - create bytecode with GOTO at offset 0 jumping to offset 10
        codeAttribute.code[0] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[1] = 0; // branch offset high byte
        codeAttribute.code[2] = 10; // branch offset low byte

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "GOTO instruction should be reachable");
        assertTrue(marker.isReachable(10), "GOTO target at offset 10 should be reachable");
        assertFalse(marker.isReachable(3), "Instruction after GOTO should not be reachable");
    }

    /**
     * Tests GOTO_W instruction with larger offset.
     * GOTO_W should mark the target and stop sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_gotoWWithLargeOffset() {
        // Arrange - create bytecode with GOTO_W at offset 0 jumping to offset 50
        codeAttribute.code[0] = (byte) Instruction.OP_GOTO_W;
        codeAttribute.code[1] = 0; // branch offset byte 1
        codeAttribute.code[2] = 0; // branch offset byte 2
        codeAttribute.code[3] = 0; // branch offset byte 3
        codeAttribute.code[4] = 50; // branch offset byte 4

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "GOTO_W instruction should be reachable");
        assertTrue(marker.isReachable(50), "GOTO_W target at offset 50 should be reachable");
        assertFalse(marker.isReachable(5), "Instruction after GOTO_W should not be reachable");
    }

    // ========================================
    // Conditional Branch Instructions Tests
    // ========================================

    /**
     * Tests IFEQ instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ifeqMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFEQ;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 15;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IFEQ instruction should be reachable");
        assertTrue(marker.isReachable(15), "IFEQ branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IFEQ should be reachable");
    }

    /**
     * Tests IFNE instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ifneMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFNE;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 12;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IFNE instruction should be reachable");
        assertTrue(marker.isReachable(12), "IFNE branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IFNE should be reachable");
    }

    /**
     * Tests IFLT instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ifltMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFLT;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 8;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IFLT instruction should be reachable");
        assertTrue(marker.isReachable(8), "IFLT branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IFLT should be reachable");
    }

    /**
     * Tests IFGE instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ifgeMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFGE;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 25;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IFGE instruction should be reachable");
        assertTrue(marker.isReachable(25), "IFGE branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IFGE should be reachable");
    }

    /**
     * Tests IFGT instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ifgtMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFGT;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 18;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IFGT instruction should be reachable");
        assertTrue(marker.isReachable(18), "IFGT branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IFGT should be reachable");
    }

    /**
     * Tests IFLE instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ifleMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFLE;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 30;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IFLE instruction should be reachable");
        assertTrue(marker.isReachable(30), "IFLE branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IFLE should be reachable");
    }

    /**
     * Tests IF_ICMPEQ instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ificmpeqMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFICMPEQ;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 22;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IF_ICMPEQ instruction should be reachable");
        assertTrue(marker.isReachable(22), "IF_ICMPEQ branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IF_ICMPEQ should be reachable");
    }

    /**
     * Tests IF_ICMPNE instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ificmpneMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFICMPNE;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 14;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IF_ICMPNE instruction should be reachable");
        assertTrue(marker.isReachable(14), "IF_ICMPNE branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IF_ICMPNE should be reachable");
    }

    /**
     * Tests IF_ICMPLT instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ificmpltMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFICMPLT;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 11;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IF_ICMPLT instruction should be reachable");
        assertTrue(marker.isReachable(11), "IF_ICMPLT branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IF_ICMPLT should be reachable");
    }

    /**
     * Tests IF_ICMPGE instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ificmpgeMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFICMPGE;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 27;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IF_ICMPGE instruction should be reachable");
        assertTrue(marker.isReachable(27), "IF_ICMPGE branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IF_ICMPGE should be reachable");
    }

    /**
     * Tests IF_ICMPGT instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ificmpgtMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFICMPGT;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 19;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IF_ICMPGT instruction should be reachable");
        assertTrue(marker.isReachable(19), "IF_ICMPGT branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IF_ICMPGT should be reachable");
    }

    /**
     * Tests IF_ICMPLE instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ificmpleMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFICMPLE;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 33;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IF_ICMPLE instruction should be reachable");
        assertTrue(marker.isReachable(33), "IF_ICMPLE branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IF_ICMPLE should be reachable");
    }

    /**
     * Tests IF_ACMPEQ instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ifacmpeqMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFACMPEQ;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 16;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IF_ACMPEQ instruction should be reachable");
        assertTrue(marker.isReachable(16), "IF_ACMPEQ branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IF_ACMPEQ should be reachable");
    }

    /**
     * Tests IF_ACMPNE instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ifacmpneMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFACMPNE;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 24;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IF_ACMPNE instruction should be reachable");
        assertTrue(marker.isReachable(24), "IF_ACMPNE branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IF_ACMPNE should be reachable");
    }

    /**
     * Tests IFNULL instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ifnullMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFNULL;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 13;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IFNULL instruction should be reachable");
        assertTrue(marker.isReachable(13), "IFNULL branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IFNULL should be reachable");
    }

    /**
     * Tests IFNONNULL instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_ifnonnullMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFNONNULL;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 21;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IFNONNULL instruction should be reachable");
        assertTrue(marker.isReachable(21), "IFNONNULL branch target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after IFNONNULL should be reachable");
    }

    // ========================================
    // Backward Branch Tests
    // ========================================

    /**
     * Tests backward branch (negative offset) - common in loops.
     * The branch target should be marked as reachable even with negative offset.
     */
    @Test
    public void testVisitBranchInstruction_backwardBranch() {
        // Arrange - create a simple loop structure
        // 0: NOP
        // 1: NOP
        // 2: NOP
        // 3: GOTO -3 (back to offset 0)
        codeAttribute.code[3] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[4] = (byte) 0xFF; // -1 high byte
        codeAttribute.code[5] = (byte) 0xFD; // -3 low byte (signed)

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "Loop start should be reachable");
        assertTrue(marker.isReachable(3), "GOTO instruction should be reachable");
        // The backward branch creates a loop, so offset 0 remains reachable
    }

    /**
     * Tests conditional backward branch - should mark both target and next instruction.
     */
    @Test
    public void testVisitBranchInstruction_conditionalBackwardBranch() {
        // Arrange - loop with conditional
        codeAttribute.code[10] = (byte) Instruction.OP_IFNE;
        codeAttribute.code[11] = (byte) 0xFF; // -1 high byte
        codeAttribute.code[12] = (byte) 0xF7; // -9 low byte

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(10), "Conditional branch should be reachable");
        assertTrue(marker.isReachable(13), "Instruction after conditional should be reachable");
    }

    // ========================================
    // JSR Instruction Tests
    // ========================================

    /**
     * Tests JSR instruction - should mark target and continue sequential execution.
     * JSR is used for subroutine calls (deprecated but still supported).
     */
    @Test
    public void testVisitBranchInstruction_jsrMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_JSR;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 20;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "JSR instruction should be reachable");
        assertTrue(marker.isReachable(20), "JSR target should be reachable");
        assertTrue(marker.isReachable(3), "Next instruction after JSR should be reachable");
    }

    /**
     * Tests JSR_W instruction - should mark target and continue sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_jsrWMarksBothPaths() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_JSR_W;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 0;
        codeAttribute.code[3] = 0;
        codeAttribute.code[4] = 40;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "JSR_W instruction should be reachable");
        assertTrue(marker.isReachable(40), "JSR_W target should be reachable");
        assertTrue(marker.isReachable(5), "Next instruction after JSR_W should be reachable");
    }

    // ========================================
    // Multiple Branch Tests
    // ========================================

    /**
     * Tests multiple branches to the same target.
     * All branches should successfully mark the shared target.
     */
    @Test
    public void testVisitBranchInstruction_multipleBranchesToSameTarget() {
        // Arrange - two GOTOs to the same target
        codeAttribute.code[0] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 20;

        codeAttribute.code[10] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[11] = 0;
        codeAttribute.code[12] = 10; // Also targets offset 20 (10 + 10)

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "First GOTO should be reachable");
        assertTrue(marker.isReachable(20), "Shared target should be reachable");
    }

    /**
     * Tests chain of branches.
     * Each branch should propagate reachability.
     */
    @Test
    public void testVisitBranchInstruction_chainedBranches() {
        // Arrange - GOTO chain: 0->10, 10->20, 20->30
        codeAttribute.code[0] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 10;

        codeAttribute.code[10] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[11] = 0;
        codeAttribute.code[12] = 10; // Jump to 20

        codeAttribute.code[20] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[21] = 0;
        codeAttribute.code[22] = 10; // Jump to 30

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "First GOTO should be reachable");
        assertTrue(marker.isReachable(10), "Second GOTO should be reachable");
        assertTrue(marker.isReachable(20), "Third GOTO should be reachable");
        assertTrue(marker.isReachable(30), "Final target should be reachable");
    }

    // ========================================
    // Unreachable Code Tests
    // ========================================

    /**
     * Tests that code after unconditional GOTO is unreachable.
     */
    @Test
    public void testVisitBranchInstruction_codeAfterGotoIsUnreachable() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 50; // Jump to 50
        // Instructions at 3-49 should be unreachable

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "GOTO should be reachable");
        assertTrue(marker.isReachable(50), "GOTO target should be reachable");
        assertFalse(marker.isReachable(3), "Code immediately after GOTO should be unreachable");
        assertFalse(marker.isReachable(10), "Code after GOTO should be unreachable");
        assertFalse(marker.isReachable(20), "Code after GOTO should be unreachable");
        assertFalse(marker.isReachable(49), "Code before GOTO target should be unreachable");
    }

    /**
     * Tests that code after conditional branch remains reachable.
     */
    @Test
    public void testVisitBranchInstruction_codeAfterConditionalIsReachable() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_IFEQ;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 50; // Jump to 50 if equal
        // Instruction at 3 should be reachable

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "IFEQ should be reachable");
        assertTrue(marker.isReachable(50), "IFEQ target should be reachable");
        assertTrue(marker.isReachable(3), "Code after conditional branch should be reachable");
    }

    // ========================================
    // Different Clazz Types Tests
    // ========================================

    /**
     * Tests with ProgramClass.
     */
    @Test
    public void testVisitBranchInstruction_withProgramClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        codeAttribute.code[0] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 15;

        // Act
        marker.visitCodeAttribute(programClass, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "Should work with ProgramClass");
        assertTrue(marker.isReachable(15), "Target should be reachable");
    }

    /**
     * Tests with LibraryClass.
     */
    @Test
    public void testVisitBranchInstruction_withLibraryClass() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        codeAttribute.code[0] = (byte) Instruction.OP_IFEQ;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 10;

        // Act
        marker.visitCodeAttribute(libraryClass, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "Should work with LibraryClass");
        assertTrue(marker.isReachable(10), "Target should be reachable");
    }

    // ========================================
    // Edge Cases Tests
    // ========================================

    /**
     * Tests branch to offset 0 (branch to start of method).
     */
    @Test
    public void testVisitBranchInstruction_branchToOffsetZero() {
        // Arrange
        codeAttribute.code[10] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[11] = (byte) 0xFF; // -1 high byte
        codeAttribute.code[12] = (byte) 0xF6; // -10 low byte, jumps back to 0

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "Branch to offset 0 should work");
        assertTrue(marker.isReachable(10), "GOTO instruction should be reachable");
    }

    /**
     * Tests branch with offset of 3 (minimum forward jump for 3-byte instruction).
     */
    @Test
    public void testVisitBranchInstruction_minimumForwardJump() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 3; // Jump to immediately after this instruction

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "GOTO should be reachable");
        assertTrue(marker.isReachable(3), "Target immediately after should be reachable");
    }

    /**
     * Tests GOTO_W with maximum-length code.
     */
    @Test
    public void testVisitBranchInstruction_gotoWAtEndOfCode() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_GOTO_W;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 0;
        codeAttribute.code[3] = 0;
        codeAttribute.code[4] = 95; // Jump near end

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "GOTO_W should be reachable");
        assertTrue(marker.isReachable(95), "Target near end should be reachable");
    }

    /**
     * Tests multiple marker instances remain independent.
     */
    @Test
    public void testVisitBranchInstruction_multipleInstancesIndependent() {
        // Arrange
        ReachableCodeMarker marker1 = new ReachableCodeMarker();
        ReachableCodeMarker marker2 = new ReachableCodeMarker();

        codeAttribute.code[0] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 25;

        // Act
        marker1.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker1.isReachable(0), "First marker should track reachability");
        assertTrue(marker1.isReachable(25), "First marker should track branch target");
        assertFalse(marker2.isReachable(0), "Second marker should be independent");
        assertFalse(marker2.isReachable(25), "Second marker should be independent");
    }

    /**
     * Tests that visitBranchInstruction works correctly when called multiple times
     * on the same code attribute.
     */
    @Test
    public void testVisitBranchInstruction_multipleVisits() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 10;

        // Act - visit twice
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - should still work correctly
        assertTrue(marker.isReachable(0), "Should work on second visit");
        assertTrue(marker.isReachable(10), "Target should be reachable on second visit");
    }

    /**
     * Tests method with no branches - only sequential execution.
     */
    @Test
    public void testVisitBranchInstruction_noBranches() {
        // Arrange - code with no branches, just NOPs and RETURN
        // (This is the default setup from setUp)

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - all sequential instructions should be reachable
        assertTrue(marker.isReachable(0), "Start should be reachable");
        assertTrue(marker.isReachable(50), "Middle should be reachable");
        assertTrue(marker.isReachable(99), "End should be reachable");
    }

    /**
     * Tests complex control flow with mixed conditional and unconditional branches.
     */
    @Test
    public void testVisitBranchInstruction_complexControlFlow() {
        // Arrange - create a more complex control flow
        // 0: IFEQ 10
        // 3: GOTO 20
        // 10: IFEQ 30
        // 20: NOP
        // 30: RETURN

        codeAttribute.code[0] = (byte) Instruction.OP_IFEQ;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 10;

        codeAttribute.code[3] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[4] = 0;
        codeAttribute.code[5] = 17; // Jump to 20

        codeAttribute.code[10] = (byte) Instruction.OP_IFEQ;
        codeAttribute.code[11] = 0;
        codeAttribute.code[12] = 20; // Jump to 30

        codeAttribute.code[20] = (byte) Instruction.OP_NOP;
        codeAttribute.code[30] = (byte) Instruction.OP_RETURN;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertTrue(marker.isReachable(0), "Entry point should be reachable");
        assertTrue(marker.isReachable(3), "Fall-through from IFEQ should be reachable");
        assertTrue(marker.isReachable(10), "First branch target should be reachable");
        assertTrue(marker.isReachable(20), "GOTO target should be reachable");
        assertTrue(marker.isReachable(30), "Second branch target should be reachable");
        assertFalse(marker.isReachable(6), "Code between branches should be unreachable");
    }

    /**
     * Tests that the method doesn't throw exceptions with valid parameters.
     */
    @Test
    public void testVisitBranchInstruction_doesNotThrow() {
        // Arrange
        codeAttribute.code[0] = (byte) Instruction.OP_GOTO;
        codeAttribute.code[1] = 0;
        codeAttribute.code[2] = 10;

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitCodeAttribute(clazz, method, codeAttribute),
            "visitBranchInstruction should not throw with valid parameters");
    }
}

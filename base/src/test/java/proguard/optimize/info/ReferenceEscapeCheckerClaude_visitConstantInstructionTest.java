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
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReferenceEscapeChecker#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method processes constant instructions that affect reference escape analysis:
 *
 * 1. OP_GETSTATIC / OP_GETFIELD: Marks external reference values (calls markExternalReferenceValue)
 * 2. OP_PUTSTATIC: Marks reference values that are put in the static field as escaping (calls markEscapingReferenceValues)
 * 3. OP_PUTFIELD: Marks object whose field is modified AND marks the value being put as escaping
 *    (calls both markModifiedReferenceValues and markEscapingReferenceValues)
 * 4. OP_INVOKEVIRTUAL / OP_INVOKESPECIAL / OP_INVOKESTATIC / OP_INVOKEINTERFACE:
 *    Sets internal state (referencingMethod, referencingOffset, referencingPopCount) and
 *    calls clazz.constantPoolEntryAccept() which triggers visitAnyMethodrefConstant
 * 5. Other opcodes: No action taken (method returns without doing anything)
 *
 * Testing approach:
 * - Test opcodes that don't trigger any logic (no-op cases)
 * - Test OP_GETSTATIC/OP_GETFIELD which mark external values (can be tested via isInstanceExternal)
 * - Test OP_PUTSTATIC/OP_PUTFIELD which require PartialEvaluator (verify they attempt marking)
 * - Test invoke instructions which call constantPoolEntryAccept
 * - Verify correct constant pool indices are used
 */
public class ReferenceEscapeCheckerClaude_visitConstantInstructionTest {

    private ReferenceEscapeChecker checker;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        checker = new ReferenceEscapeChecker();
        clazz = createProgramClassWithConstantPool();
        method = createMethodWithDescriptor((ProgramClass) clazz, "testMethod", "()V");
        codeAttribute = new CodeAttribute();
        codeAttribute.u4codeLength = 100;
        codeAttribute.code = new byte[100];
    }

    // =========================================================================
    // Tests for opcodes that DON'T trigger any logic (default case in switch)
    // =========================================================================

    /**
     * Tests that visitConstantInstruction handles OP_LDC without throwing exceptions.
     * OP_LDC is not in the switch statement, so it should fall through and do nothing.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc_doesNotThrowException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction handles OP_LDC_W without throwing exceptions.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdcW_doesNotThrowException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC_W, 1);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction handles OP_LDC2_W without throwing exceptions.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc2W_doesNotThrowException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC2_W, 1);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction handles OP_NEW without throwing exceptions.
     * Object creation doesn't affect escape analysis in this visitor.
     */
    @Test
    public void testVisitConstantInstruction_withOpNew_doesNotThrowException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 1);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction handles OP_ANEWARRAY without throwing exceptions.
     */
    @Test
    public void testVisitConstantInstruction_withOpAnewarray_doesNotThrowException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 1);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction handles OP_CHECKCAST without throwing exceptions.
     */
    @Test
    public void testVisitConstantInstruction_withOpCheckcast_doesNotThrowException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction handles OP_INSTANCEOF without throwing exceptions.
     */
    @Test
    public void testVisitConstantInstruction_withOpInstanceof_doesNotThrowException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 1);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction handles OP_MULTIANEWARRAY without throwing exceptions.
     */
    @Test
    public void testVisitConstantInstruction_withOpMultianewarray_doesNotThrowException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 1);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction can be called multiple times with no-op opcodes.
     */
    @Test
    public void testVisitConstantInstruction_calledMultipleTimesWithNoOpOpcodes_doesNotThrowException() {
        // Arrange
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 2);
        ConstantInstruction checkcast = new ConstantInstruction(Instruction.OP_CHECKCAST, 3);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
            checker.visitConstantInstruction(clazz, method, codeAttribute, 1, newInst);
            checker.visitConstantInstruction(clazz, method, codeAttribute, 2, checkcast);
        });
    }

    /**
     * Tests that visitConstantInstruction doesn't modify state for no-op opcodes.
     */
    @Test
    public void testVisitConstantInstruction_withNoOpOpcodes_doesNotModifyState() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Check initial state
        assertFalse(checker.isInstanceEscaping(0), "Initial state should be false");
        assertFalse(checker.isInstanceReturned(0), "Initial state should be false");
        assertFalse(checker.isInstanceModified(0), "Initial state should be false");
        assertFalse(checker.isInstanceExternal(0), "Initial state should be false");

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - state should remain unchanged
        assertFalse(checker.isInstanceEscaping(0), "State should remain unchanged");
        assertFalse(checker.isInstanceReturned(0), "State should remain unchanged");
        assertFalse(checker.isInstanceModified(0), "State should remain unchanged");
        assertFalse(checker.isInstanceExternal(0), "State should remain unchanged");
    }

    // =========================================================================
    // Tests for OP_GETSTATIC and OP_GETFIELD - Mark external reference values
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with OP_GETSTATIC marks the instance as external.
     * This can be verified through the public isInstanceExternal method.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetstatic_marksInstanceExternal() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        int offset = 5;

        // Check initial state
        assertFalse(checker.isInstanceExternal(offset), "Should be false before visiting");

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, offset, instruction);

        // Assert
        assertTrue(checker.isInstanceExternal(offset),
            "OP_GETSTATIC should mark instance as external at offset " + offset);
    }

    /**
     * Tests that visitConstantInstruction with OP_GETFIELD marks the instance as external.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetfield_marksInstanceExternal() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 2);
        int offset = 10;

        // Check initial state
        assertFalse(checker.isInstanceExternal(offset), "Should be false before visiting");

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, offset, instruction);

        // Assert
        assertTrue(checker.isInstanceExternal(offset),
            "OP_GETFIELD should mark instance as external at offset " + offset);
    }

    /**
     * Tests that OP_GETSTATIC marks external at different offsets independently.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetstatic_marksCorrectOffsets() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 5, instruction);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);

        // Assert
        assertTrue(checker.isInstanceExternal(0), "Offset 0 should be marked external");
        assertTrue(checker.isInstanceExternal(5), "Offset 5 should be marked external");
        assertTrue(checker.isInstanceExternal(10), "Offset 10 should be marked external");
        assertFalse(checker.isInstanceExternal(1), "Offset 1 should NOT be marked external");
        assertFalse(checker.isInstanceExternal(7), "Offset 7 should NOT be marked external");
    }

    /**
     * Tests that OP_GETFIELD marks external at different offsets independently.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetfield_marksCorrectOffsets() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 3);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 2, instruction);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 7, instruction);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 15, instruction);

        // Assert
        assertTrue(checker.isInstanceExternal(2), "Offset 2 should be marked external");
        assertTrue(checker.isInstanceExternal(7), "Offset 7 should be marked external");
        assertTrue(checker.isInstanceExternal(15), "Offset 15 should be marked external");
        assertFalse(checker.isInstanceExternal(3), "Offset 3 should NOT be marked external");
        assertFalse(checker.isInstanceExternal(20), "Offset 20 should NOT be marked external");
    }

    /**
     * Tests that OP_GETSTATIC only marks as external, not escaping/returned/modified.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetstatic_onlyMarksExternal() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        int offset = 3;

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, offset, instruction);

        // Assert
        assertTrue(checker.isInstanceExternal(offset), "Should be marked external");
        assertFalse(checker.isInstanceEscaping(offset), "Should NOT be marked escaping");
        assertFalse(checker.isInstanceReturned(offset), "Should NOT be marked returned");
        assertFalse(checker.isInstanceModified(offset), "Should NOT be marked modified");
    }

    /**
     * Tests that OP_GETFIELD only marks as external, not escaping/returned/modified.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetfield_onlyMarksExternal() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 2);
        int offset = 8;

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, offset, instruction);

        // Assert
        assertTrue(checker.isInstanceExternal(offset), "Should be marked external");
        assertFalse(checker.isInstanceEscaping(offset), "Should NOT be marked escaping");
        assertFalse(checker.isInstanceReturned(offset), "Should NOT be marked returned");
        assertFalse(checker.isInstanceModified(offset), "Should NOT be marked modified");
    }

    /**
     * Tests that both OP_GETSTATIC and OP_GETFIELD can be used together.
     */
    @Test
    public void testVisitConstantInstruction_withGetstaticAndGetfield_bothMarkExternal() {
        // Arrange
        ConstantInstruction getstatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 2);

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 5, getstatic);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 10, getfield);

        // Assert
        assertTrue(checker.isInstanceExternal(5), "GETSTATIC should mark offset 5 as external");
        assertTrue(checker.isInstanceExternal(10), "GETFIELD should mark offset 10 as external");
    }

    // =========================================================================
    // Tests for OP_PUTSTATIC - Marks escaping reference values
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with OP_PUTSTATIC attempts to mark escaping values.
     * Without a properly initialized PartialEvaluator, this will throw NullPointerException.
     */
    @Test
    public void testVisitConstantInstruction_withOpPutstatic_attemptsMarkingEscaping() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_PUTSTATIC should attempt to call markEscapingReferenceValues");
    }

    /**
     * Tests that OP_PUTSTATIC attempts marking at different offsets.
     */
    @Test
    public void testVisitConstantInstruction_withOpPutstatic_attemptsMarkingAtDifferentOffsets() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 2);

        // Act & Assert - should throw at all offsets
        assertThrows(NullPointerException.class,
            () -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
        assertThrows(NullPointerException.class,
            () -> checker.visitConstantInstruction(clazz, method, codeAttribute, 5, instruction));
        assertThrows(NullPointerException.class,
            () -> checker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction));
    }

    // =========================================================================
    // Tests for OP_PUTFIELD - Marks modified and escaping reference values
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with OP_PUTFIELD attempts to mark both modified and escaping.
     * Without a properly initialized PartialEvaluator, this will throw NullPointerException.
     */
    @Test
    public void testVisitConstantInstruction_withOpPutfield_attemptsMarkingModifiedAndEscaping() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 3);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_PUTFIELD should attempt to call markModifiedReferenceValues and markEscapingReferenceValues");
    }

    /**
     * Tests that OP_PUTFIELD attempts marking at different offsets.
     */
    @Test
    public void testVisitConstantInstruction_withOpPutfield_attemptsMarkingAtDifferentOffsets() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 4);

        // Act & Assert - should throw at all offsets
        assertThrows(NullPointerException.class,
            () -> checker.visitConstantInstruction(clazz, method, codeAttribute, 2, instruction));
        assertThrows(NullPointerException.class,
            () -> checker.visitConstantInstruction(clazz, method, codeAttribute, 7, instruction));
        assertThrows(NullPointerException.class,
            () -> checker.visitConstantInstruction(clazz, method, codeAttribute, 12, instruction));
    }

    // =========================================================================
    // Tests for invoke instructions - Call constantPoolEntryAccept
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with OP_INVOKEVIRTUAL calls constantPoolEntryAccept.
     * This is verified by using a mock clazz and checking the interaction.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokevirtual_callsConstantPoolEntryAccept() {
        // Arrange
        Clazz mockClazz = mock(ProgramClass.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5);

        // Act
        checker.visitConstantInstruction(mockClazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(mockClazz).constantPoolEntryAccept(eq(5), eq(checker));
    }

    /**
     * Tests that visitConstantInstruction with OP_INVOKESPECIAL calls constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokespecial_callsConstantPoolEntryAccept() {
        // Arrange
        Clazz mockClazz = mock(ProgramClass.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 6);

        // Act
        checker.visitConstantInstruction(mockClazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(mockClazz).constantPoolEntryAccept(eq(6), eq(checker));
    }

    /**
     * Tests that visitConstantInstruction with OP_INVOKESTATIC calls constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokestatic_callsConstantPoolEntryAccept() {
        // Arrange
        Clazz mockClazz = mock(ProgramClass.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 7);

        // Act
        checker.visitConstantInstruction(mockClazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(mockClazz).constantPoolEntryAccept(eq(7), eq(checker));
    }

    /**
     * Tests that visitConstantInstruction with OP_INVOKEINTERFACE calls constantPoolEntryAccept.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeinterface_callsConstantPoolEntryAccept() {
        // Arrange
        Clazz mockClazz = mock(ProgramClass.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 8);

        // Act
        checker.visitConstantInstruction(mockClazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(mockClazz).constantPoolEntryAccept(eq(8), eq(checker));
    }

    /**
     * Tests that invoke instructions call constantPoolEntryAccept with different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeInstructions_usesCorrectConstantIndices() {
        // Arrange
        Clazz mockClazz = mock(ProgramClass.class);
        ConstantInstruction invoke1 = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        ConstantInstruction invoke10 = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 10);
        ConstantInstruction invoke100 = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 100);

        // Act
        checker.visitConstantInstruction(mockClazz, method, codeAttribute, 0, invoke1);
        checker.visitConstantInstruction(mockClazz, method, codeAttribute, 3, invoke10);
        checker.visitConstantInstruction(mockClazz, method, codeAttribute, 6, invoke100);

        // Assert
        verify(mockClazz).constantPoolEntryAccept(eq(1), eq(checker));
        verify(mockClazz).constantPoolEntryAccept(eq(10), eq(checker));
        verify(mockClazz).constantPoolEntryAccept(eq(100), eq(checker));
    }

    /**
     * Tests that all four invoke instructions trigger the same behavior.
     */
    @Test
    public void testVisitConstantInstruction_allInvokeInstructions_callConstantPoolEntryAccept() {
        // Arrange
        Clazz mockClazz = mock(ProgramClass.class);
        byte[] invokeOpcodes = {
            Instruction.OP_INVOKEVIRTUAL,
            Instruction.OP_INVOKESPECIAL,
            Instruction.OP_INVOKESTATIC,
            Instruction.OP_INVOKEINTERFACE
        };

        // Act & Assert
        for (int i = 0; i < invokeOpcodes.length; i++) {
            ConstantInstruction instruction = new ConstantInstruction(invokeOpcodes[i], i + 1);
            checker.visitConstantInstruction(mockClazz, method, codeAttribute, i, instruction);
            verify(mockClazz).constantPoolEntryAccept(eq(i + 1), eq(checker));
        }
    }

    // =========================================================================
    // Comprehensive tests
    // =========================================================================

    /**
     * Tests that visitConstantInstruction correctly identifies which opcodes trigger specific behaviors.
     */
    @Test
    public void testVisitConstantInstruction_identifiesOpcodeBehaviors() {
        // No-op opcodes - should not throw or modify state
        byte[] noOpOpcodes = {
            Instruction.OP_LDC,
            Instruction.OP_LDC_W,
            Instruction.OP_LDC2_W,
            Instruction.OP_NEW,
            Instruction.OP_ANEWARRAY,
            Instruction.OP_CHECKCAST,
            Instruction.OP_INSTANCEOF,
            Instruction.OP_MULTIANEWARRAY
        };

        for (byte opcode : noOpOpcodes) {
            ConstantInstruction instruction = new ConstantInstruction(opcode, 1);
            assertDoesNotThrow(
                () -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
                "Opcode " + opcode + " should be a no-op");
        }

        // External-marking opcodes - mark external at offset
        ConstantInstruction getstatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        ConstantInstruction getfield = new ConstantInstruction(Instruction.OP_GETFIELD, 1);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 20, getstatic);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 21, getfield);
        assertTrue(checker.isInstanceExternal(20), "GETSTATIC should mark external");
        assertTrue(checker.isInstanceExternal(21), "GETFIELD should mark external");

        // Escaping-marking opcodes - should throw NPE (no PartialEvaluator)
        byte[] escapingOpcodes = {
            Instruction.OP_PUTSTATIC,
            Instruction.OP_PUTFIELD
        };

        for (byte opcode : escapingOpcodes) {
            ConstantInstruction instruction = new ConstantInstruction(opcode, 1);
            assertThrows(NullPointerException.class,
                () -> checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction),
                "Opcode " + opcode + " should attempt marking");
        }
    }

    /**
     * Tests that visitConstantInstruction can handle mixed opcodes in sequence.
     */
    @Test
    public void testVisitConstantInstruction_mixedOpcodes_handlesCorrectly() {
        // Arrange & Act & Assert
        // No-op opcode should succeed
        assertDoesNotThrow(() ->
            checker.visitConstantInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_LDC, 1)));

        // External-marking opcode should succeed
        checker.visitConstantInstruction(clazz, method, codeAttribute, 1,
            new ConstantInstruction(Instruction.OP_GETSTATIC, 2));
        assertTrue(checker.isInstanceExternal(1), "Should be marked external");

        // Escaping-marking opcode should throw (no PartialEvaluator)
        assertThrows(NullPointerException.class, () ->
            checker.visitConstantInstruction(clazz, method, codeAttribute, 2,
                new ConstantInstruction(Instruction.OP_PUTSTATIC, 3)));

        // Another no-op opcode should succeed
        assertDoesNotThrow(() ->
            checker.visitConstantInstruction(clazz, method, codeAttribute, 3,
                new ConstantInstruction(Instruction.OP_NEW, 4)));
    }

    /**
     * Tests that visitConstantInstruction with different constant indices works correctly.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentConstantIndices_handlesCorrectly() {
        // Arrange & Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 5,
            new ConstantInstruction(Instruction.OP_GETSTATIC, 1));
        checker.visitConstantInstruction(clazz, method, codeAttribute, 10,
            new ConstantInstruction(Instruction.OP_GETSTATIC, 50));
        checker.visitConstantInstruction(clazz, method, codeAttribute, 15,
            new ConstantInstruction(Instruction.OP_GETSTATIC, 100));

        // Assert - all should be marked external
        assertTrue(checker.isInstanceExternal(5), "Offset 5 should be external");
        assertTrue(checker.isInstanceExternal(10), "Offset 10 should be external");
        assertTrue(checker.isInstanceExternal(15), "Offset 15 should be external");
    }

    /**
     * Tests that visitConstantInstruction at large offsets works correctly.
     */
    @Test
    public void testVisitConstantInstruction_withLargeOffsets_handlesCorrectly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        int largeOffset = 500;

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, largeOffset, instruction);

        // Assert
        assertTrue(checker.isInstanceExternal(largeOffset),
            "Should mark external even at large offset " + largeOffset);
    }

    /**
     * Tests that the constant index is properly used for invoke instructions.
     */
    @Test
    public void testVisitConstantInstruction_invokeInstructions_useConstantIndex() {
        // Arrange
        Clazz mockClazz = mock(ProgramClass.class);

        // Act - test various constant indices
        checker.visitConstantInstruction(mockClazz, method, codeAttribute, 0,
            new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5));
        checker.visitConstantInstruction(mockClazz, method, codeAttribute, 3,
            new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 25));
        checker.visitConstantInstruction(mockClazz, method, codeAttribute, 6,
            new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 125));

        // Assert
        verify(mockClazz).constantPoolEntryAccept(eq(5), eq(checker));
        verify(mockClazz).constantPoolEntryAccept(eq(25), eq(checker));
        verify(mockClazz).constantPoolEntryAccept(eq(125), eq(checker));
    }

    // Helper methods

    /**
     * Creates a ProgramClass with a basic constant pool.
     */
    private ProgramClass createProgramClassWithConstantPool() {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with enough space
        Constant[] constantPool = new Constant[200];
        constantPool[0] = null; // Index 0 is always null
        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 200;

        return programClass;
    }

    /**
     * Creates a ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();

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
        throw new IllegalStateException("Constant pool is full");
    }
}

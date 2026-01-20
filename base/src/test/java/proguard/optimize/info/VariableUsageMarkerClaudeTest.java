package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.VariableInstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link VariableUsageMarker}.
 *
 * This class tests all methods in VariableUsageMarker:
 * - Constructor (initializes with default array size)
 * - isVariableUsed(int) - checks if a variable has been marked as used
 * - visitAnyAttribute(Clazz, Attribute) - default empty implementation
 * - visitCodeAttribute(Clazz, Method, CodeAttribute) - processes code to mark used variables
 * - visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction) - default empty implementation
 * - visitVariableInstruction(Clazz, Method, CodeAttribute, int, VariableInstruction) - marks variables as used
 *
 * The VariableUsageMarker is used to track which local variables are actually used in a method's code.
 * It maintains a boolean array that is sized based on the method's maxLocals count.
 * Category 2 types (long, double) occupy two variable slots.
 */
public class VariableUsageMarkerClaudeTest {

    private VariableUsageMarker marker;
    private ProgramClass clazz;
    private ProgramMethod method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        marker = new VariableUsageMarker();
        // Use real ProgramClass instance instead of mock for proper stackPopCount/stackPushCount behavior
        clazz = new ProgramClass();
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that constructor creates a new instance successfully.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        VariableUsageMarker newMarker = new VariableUsageMarker();

        // Assert
        assertNotNull(newMarker, "Constructor should create a new instance");
    }

    /**
     * Tests that newly constructed marker has no variables marked as used initially.
     */
    @Test
    public void testConstructor_initiallyNoVariablesUsed() {
        // Arrange
        VariableUsageMarker newMarker = new VariableUsageMarker();

        // Act & Assert - check typical indices
        assertFalse(newMarker.isVariableUsed(0), "Variable 0 should not be used initially");
        assertFalse(newMarker.isVariableUsed(1), "Variable 1 should not be used initially");
        assertFalse(newMarker.isVariableUsed(5), "Variable 5 should not be used initially");
    }

    // =========================================================================
    // isVariableUsed Tests
    // =========================================================================

    /**
     * Tests isVariableUsed returns false for variables not yet marked.
     */
    @Test
    public void testIsVariableUsed_withUnusedVariable_returnsFalse() {
        // Act & Assert
        assertFalse(marker.isVariableUsed(0), "Unused variable should return false");
        assertFalse(marker.isVariableUsed(5), "Unused variable should return false");
    }

    /**
     * Tests isVariableUsed returns true after variable instruction visits a variable.
     */
    @Test
    public void testIsVariableUsed_afterVariableMarked_returnsTrue() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        VariableInstruction iload = new VariableInstruction(Instruction.OP_ILOAD, 3);

        // Visit code attribute to initialize the array
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Mark variable 3 as used
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, iload);

        // Act & Assert
        assertTrue(marker.isVariableUsed(3), "Variable 3 should be marked as used");
        assertFalse(marker.isVariableUsed(2), "Variable 2 should not be marked");
        assertFalse(marker.isVariableUsed(4), "Variable 4 should not be marked");
    }

    /**
     * Tests isVariableUsed with variable index 0.
     */
    @Test
    public void testIsVariableUsed_withIndex0_worksCorrectly() {
        // Arrange
        codeAttribute.u2maxLocals = 5;
        VariableInstruction aload0 = new VariableInstruction(Instruction.OP_ALOAD, 0);

        marker.visitCodeAttribute(clazz, method, codeAttribute);
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, aload0);

        // Act & Assert
        assertTrue(marker.isVariableUsed(0), "Variable 0 should be marked as used");
    }

    /**
     * Tests isVariableUsed with multiple variables marked.
     */
    @Test
    public void testIsVariableUsed_withMultipleVariablesMarked_returnsCorrectly() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Mark variables 0, 2, 5
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 0));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 2));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 5));

        // Act & Assert
        assertTrue(marker.isVariableUsed(0), "Variable 0 should be used");
        assertFalse(marker.isVariableUsed(1), "Variable 1 should not be used");
        assertTrue(marker.isVariableUsed(2), "Variable 2 should be used");
        assertFalse(marker.isVariableUsed(3), "Variable 3 should not be used");
        assertFalse(marker.isVariableUsed(4), "Variable 4 should not be used");
        assertTrue(marker.isVariableUsed(5), "Variable 5 should be used");
    }

    // =========================================================================
    // visitAnyAttribute Tests
    // =========================================================================

    /**
     * Tests visitAnyAttribute does nothing (empty implementation).
     */
    @Test
    public void testVisitAnyAttribute_doesNothing() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute),
            "visitAnyAttribute should not throw");
    }

    /**
     * Tests visitAnyAttribute with null attribute.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, null),
            "visitAnyAttribute should handle null attribute");
    }

    /**
     * Tests visitAnyAttribute with null clazz.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrow() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(null, attribute),
            "visitAnyAttribute should handle null clazz");
    }

    // =========================================================================
    // visitCodeAttribute Tests
    // =========================================================================

    /**
     * Tests visitCodeAttribute initializes the variable array with correct size.
     */
    @Test
    public void testVisitCodeAttribute_initializesArrayWithMaxLocals() {
        // Arrange
        codeAttribute.u2maxLocals = 5;

        // Mock instructionsAccept to track if it's called
        doNothing().when(codeAttribute).instructionsAccept(eq(clazz), eq(method), eq(marker));

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - verify instructionsAccept was called
        verify(codeAttribute).instructionsAccept(clazz, method, marker);

        // All variables should be false initially
        assertFalse(marker.isVariableUsed(0), "Variable 0 should be false after initialization");
        assertFalse(marker.isVariableUsed(4), "Variable 4 should be false after initialization");
    }

    /**
     * Tests visitCodeAttribute resets array when called multiple times.
     */
    @Test
    public void testVisitCodeAttribute_resetsArrayOnMultipleCalls() {
        // Arrange - first visit marks some variables
        codeAttribute.u2maxLocals = 5;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 2));

        assertTrue(marker.isVariableUsed(2), "Variable 2 should be marked");

        // Act - visit again with same maxLocals (should reset)
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - variable should be reset to false
        assertFalse(marker.isVariableUsed(2), "Variable 2 should be reset to false");
    }

    /**
     * Tests visitCodeAttribute with larger maxLocals creates new array.
     */
    @Test
    public void testVisitCodeAttribute_withLargerMaxLocals_createsNewArray() {
        // Arrange - first visit with small maxLocals
        codeAttribute.u2maxLocals = 5;
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Act - visit with larger maxLocals
        codeAttribute.u2maxLocals = 20;
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Mark a variable beyond original size
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 15));

        // Assert
        assertTrue(marker.isVariableUsed(15), "Variable 15 should be accessible in new larger array");
    }

    /**
     * Tests visitCodeAttribute with maxLocals of 0.
     */
    @Test
    public void testVisitCodeAttribute_withMaxLocalsZero_worksCorrectly() {
        // Arrange
        codeAttribute.u2maxLocals = 0;

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> marker.visitCodeAttribute(clazz, method, codeAttribute),
            "visitCodeAttribute should handle maxLocals of 0");
    }

    /**
     * Tests visitCodeAttribute with maxLocals of 1.
     */
    @Test
    public void testVisitCodeAttribute_withMaxLocalsOne_worksCorrectly() {
        // Arrange
        codeAttribute.u2maxLocals = 1;
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Mark variable 0
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 0));

        // Act & Assert
        assertTrue(marker.isVariableUsed(0), "Variable 0 should be marked in array of size 1");
    }

    /**
     * Tests visitCodeAttribute with large maxLocals.
     */
    @Test
    public void testVisitCodeAttribute_withLargeMaxLocals_worksCorrectly() {
        // Arrange
        codeAttribute.u2maxLocals = 100;

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitCodeAttribute(clazz, method, codeAttribute),
            "visitCodeAttribute should handle large maxLocals");
    }

    /**
     * Tests visitCodeAttribute calls instructionsAccept.
     */
    @Test
    public void testVisitCodeAttribute_callsInstructionsAccept() {
        // Arrange
        codeAttribute.u2maxLocals = 5;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        verify(codeAttribute).instructionsAccept(clazz, method, marker);
    }

    // =========================================================================
    // visitAnyInstruction Tests
    // =========================================================================

    /**
     * Tests visitAnyInstruction does nothing (empty implementation).
     */
    @Test
    public void testVisitAnyInstruction_doesNothing() {
        // Arrange
        Instruction instruction = mock(Instruction.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
            "visitAnyInstruction should not throw");
    }

    /**
     * Tests visitAnyInstruction with various offsets.
     */
    @Test
    public void testVisitAnyInstruction_withVariousOffsets_doesNothing() {
        // Arrange
        Instruction instruction = mock(Instruction.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> marker.visitAnyInstruction(clazz, method, codeAttribute, 100, instruction));
        assertDoesNotThrow(() -> marker.visitAnyInstruction(clazz, method, codeAttribute, -1, instruction));
    }

    /**
     * Tests visitAnyInstruction with null instruction.
     */
    @Test
    public void testVisitAnyInstruction_withNullInstruction_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyInstruction(clazz, method, codeAttribute, 0, null),
            "visitAnyInstruction should handle null instruction");
    }

    // =========================================================================
    // visitVariableInstruction Tests - Basic Instructions
    // =========================================================================

    /**
     * Tests visitVariableInstruction marks single-slot variable (ILOAD).
     */
    @Test
    public void testVisitVariableInstruction_withIload_marksSingleSlot() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction iload = new VariableInstruction(Instruction.OP_ILOAD, 3);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, iload);

        // Assert
        assertTrue(marker.isVariableUsed(3), "Variable 3 should be marked");
        assertFalse(marker.isVariableUsed(4), "Variable 4 should not be marked");
    }

    /**
     * Tests visitVariableInstruction marks single-slot variable (ALOAD).
     */
    @Test
    public void testVisitVariableInstruction_withAload_marksSingleSlot() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction aload = new VariableInstruction(Instruction.OP_ALOAD, 2);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, aload);

        // Assert
        assertTrue(marker.isVariableUsed(2), "Variable 2 should be marked");
        assertFalse(marker.isVariableUsed(3), "Variable 3 should not be marked");
    }

    /**
     * Tests visitVariableInstruction marks single-slot variable (ISTORE).
     */
    @Test
    public void testVisitVariableInstruction_withIstore_marksSingleSlot() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction istore = new VariableInstruction(Instruction.OP_ISTORE, 5);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, istore);

        // Assert
        assertTrue(marker.isVariableUsed(5), "Variable 5 should be marked");
        assertFalse(marker.isVariableUsed(6), "Variable 6 should not be marked");
    }

    /**
     * Tests visitVariableInstruction marks single-slot variable (ASTORE).
     */
    @Test
    public void testVisitVariableInstruction_withAstore_marksSingleSlot() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction astore = new VariableInstruction(Instruction.OP_ASTORE, 1);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, astore);

        // Assert
        assertTrue(marker.isVariableUsed(1), "Variable 1 should be marked");
    }

    /**
     * Tests visitVariableInstruction with variable at index 0.
     */
    @Test
    public void testVisitVariableInstruction_withIndex0_marksCorrectly() {
        // Arrange
        codeAttribute.u2maxLocals = 5;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction aload0 = new VariableInstruction(Instruction.OP_ALOAD, 0);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, aload0);

        // Assert
        assertTrue(marker.isVariableUsed(0), "Variable 0 should be marked");
    }

    // =========================================================================
    // visitVariableInstruction Tests - Category 2 Types (Long, Double)
    // =========================================================================

    /**
     * Tests visitVariableInstruction with LLOAD marks two slots.
     * Long values occupy two local variable slots.
     */
    @Test
    public void testVisitVariableInstruction_withLload_marksTwoSlots() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction lload = new VariableInstruction(Instruction.OP_LLOAD, 3);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, lload);

        // Assert
        assertTrue(marker.isVariableUsed(3), "Variable 3 (long first slot) should be marked");
        assertTrue(marker.isVariableUsed(4), "Variable 4 (long second slot) should be marked");
        assertFalse(marker.isVariableUsed(2), "Variable 2 should not be marked");
        assertFalse(marker.isVariableUsed(5), "Variable 5 should not be marked");
    }

    /**
     * Tests visitVariableInstruction with LSTORE marks two slots.
     */
    @Test
    public void testVisitVariableInstruction_withLstore_marksTwoSlots() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction lstore = new VariableInstruction(Instruction.OP_LSTORE, 2);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, lstore);

        // Assert
        assertTrue(marker.isVariableUsed(2), "Variable 2 (long first slot) should be marked");
        assertTrue(marker.isVariableUsed(3), "Variable 3 (long second slot) should be marked");
        assertFalse(marker.isVariableUsed(1), "Variable 1 should not be marked");
        assertFalse(marker.isVariableUsed(4), "Variable 4 should not be marked");
    }

    /**
     * Tests visitVariableInstruction with DLOAD marks two slots.
     * Double values occupy two local variable slots.
     */
    @Test
    public void testVisitVariableInstruction_withDload_marksTwoSlots() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction dload = new VariableInstruction(Instruction.OP_DLOAD, 5);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, dload);

        // Assert
        assertTrue(marker.isVariableUsed(5), "Variable 5 (double first slot) should be marked");
        assertTrue(marker.isVariableUsed(6), "Variable 6 (double second slot) should be marked");
        assertFalse(marker.isVariableUsed(4), "Variable 4 should not be marked");
        assertFalse(marker.isVariableUsed(7), "Variable 7 should not be marked");
    }

    /**
     * Tests visitVariableInstruction with DSTORE marks two slots.
     */
    @Test
    public void testVisitVariableInstruction_withDstore_marksTwoSlots() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction dstore = new VariableInstruction(Instruction.OP_DSTORE, 7);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, dstore);

        // Assert
        assertTrue(marker.isVariableUsed(7), "Variable 7 (double first slot) should be marked");
        assertTrue(marker.isVariableUsed(8), "Variable 8 (double second slot) should be marked");
        assertFalse(marker.isVariableUsed(6), "Variable 6 should not be marked");
        assertFalse(marker.isVariableUsed(9), "Variable 9 should not be marked");
    }

    /**
     * Tests visitVariableInstruction with long at index 0.
     */
    @Test
    public void testVisitVariableInstruction_withLongAtIndex0_marksTwoSlots() {
        // Arrange
        codeAttribute.u2maxLocals = 5;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction lload0 = new VariableInstruction(Instruction.OP_LLOAD, 0);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, lload0);

        // Assert
        assertTrue(marker.isVariableUsed(0), "Variable 0 (long first slot) should be marked");
        assertTrue(marker.isVariableUsed(1), "Variable 1 (long second slot) should be marked");
    }

    // =========================================================================
    // visitVariableInstruction Tests - FLOAD/FSTORE (Category 1)
    // =========================================================================

    /**
     * Tests visitVariableInstruction with FLOAD marks single slot.
     */
    @Test
    public void testVisitVariableInstruction_withFload_marksSingleSlot() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction fload = new VariableInstruction(Instruction.OP_FLOAD, 4);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, fload);

        // Assert
        assertTrue(marker.isVariableUsed(4), "Variable 4 should be marked");
        assertFalse(marker.isVariableUsed(5), "Variable 5 should not be marked");
    }

    /**
     * Tests visitVariableInstruction with FSTORE marks single slot.
     */
    @Test
    public void testVisitVariableInstruction_withFstore_marksSingleSlot() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction fstore = new VariableInstruction(Instruction.OP_FSTORE, 6);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, fstore);

        // Assert
        assertTrue(marker.isVariableUsed(6), "Variable 6 should be marked");
        assertFalse(marker.isVariableUsed(7), "Variable 7 should not be marked");
    }

    // =========================================================================
    // visitVariableInstruction Tests - Multiple Instructions
    // =========================================================================

    /**
     * Tests visitVariableInstruction marks multiple variables correctly.
     */
    @Test
    public void testVisitVariableInstruction_withMultipleInstructions_marksAll() {
        // Arrange
        codeAttribute.u2maxLocals = 15;
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Act - visit multiple variable instructions
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 0));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 1,
            new VariableInstruction(Instruction.OP_ALOAD, 1));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 2,
            new VariableInstruction(Instruction.OP_LLOAD, 5));  // marks 5 and 6
        marker.visitVariableInstruction(clazz, method, codeAttribute, 3,
            new VariableInstruction(Instruction.OP_ISTORE, 8));

        // Assert
        assertTrue(marker.isVariableUsed(0), "Variable 0 should be marked");
        assertTrue(marker.isVariableUsed(1), "Variable 1 should be marked");
        assertFalse(marker.isVariableUsed(2), "Variable 2 should not be marked");
        assertTrue(marker.isVariableUsed(5), "Variable 5 should be marked");
        assertTrue(marker.isVariableUsed(6), "Variable 6 should be marked");
        assertTrue(marker.isVariableUsed(8), "Variable 8 should be marked");
    }

    /**
     * Tests visitVariableInstruction with overlapping category 2 variables.
     */
    @Test
    public void testVisitVariableInstruction_withOverlappingLongVariables_marksBothRanges() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Act - mark two longs that are adjacent
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_LLOAD, 2));  // marks 2 and 3
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_DLOAD, 4));  // marks 4 and 5

        // Assert
        assertFalse(marker.isVariableUsed(1), "Variable 1 should not be marked");
        assertTrue(marker.isVariableUsed(2), "Variable 2 should be marked");
        assertTrue(marker.isVariableUsed(3), "Variable 3 should be marked");
        assertTrue(marker.isVariableUsed(4), "Variable 4 should be marked");
        assertTrue(marker.isVariableUsed(5), "Variable 5 should be marked");
        assertFalse(marker.isVariableUsed(6), "Variable 6 should not be marked");
    }

    /**
     * Tests visitVariableInstruction marking same variable multiple times.
     */
    @Test
    public void testVisitVariableInstruction_markingSameVariableMultipleTimes_remainsMarked() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Act - mark same variable multiple times
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 3));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 5,
            new VariableInstruction(Instruction.OP_ILOAD, 3));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 10,
            new VariableInstruction(Instruction.OP_ISTORE, 3));

        // Assert
        assertTrue(marker.isVariableUsed(3), "Variable 3 should remain marked after multiple visits");
    }

    // =========================================================================
    // visitVariableInstruction Tests - RET Instruction
    // =========================================================================

    /**
     * Tests visitVariableInstruction with RET instruction (used in JSR/RET pattern).
     */
    @Test
    public void testVisitVariableInstruction_withRet_marksVariable() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction ret = new VariableInstruction(Instruction.OP_RET, 2);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, ret);

        // Assert
        assertTrue(marker.isVariableUsed(2), "Variable 2 should be marked for RET instruction");
    }

    // =========================================================================
    // visitVariableInstruction Tests - IINC Instruction
    // =========================================================================

    /**
     * Tests visitVariableInstruction with IINC instruction.
     */
    @Test
    public void testVisitVariableInstruction_withIinc_marksVariable() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction iinc = new VariableInstruction(Instruction.OP_IINC, 4, 1);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, iinc);

        // Assert
        assertTrue(marker.isVariableUsed(4), "Variable 4 should be marked for IINC instruction");
    }

    /**
     * Tests visitVariableInstruction with IINC with negative constant.
     */
    @Test
    public void testVisitVariableInstruction_withIincNegative_marksVariable() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        VariableInstruction iinc = new VariableInstruction(Instruction.OP_IINC, 3, -5);

        // Act
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0, iinc);

        // Assert
        assertTrue(marker.isVariableUsed(3), "Variable 3 should be marked for IINC instruction");
    }

    // =========================================================================
    // Integration Tests
    // =========================================================================

    /**
     * Tests complete flow: visitCodeAttribute then visitVariableInstruction.
     */
    @Test
    public void testCompleteFlow_visitsCodeThenInstructions_marksCorrectly() {
        // Arrange
        codeAttribute.u2maxLocals = 8;

        // Act
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Simulate instruction visits
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ALOAD, 0));  // this
        marker.visitVariableInstruction(clazz, method, codeAttribute, 1,
            new VariableInstruction(Instruction.OP_ILOAD, 1));  // int param
        marker.visitVariableInstruction(clazz, method, codeAttribute, 2,
            new VariableInstruction(Instruction.OP_LLOAD, 2));  // long param (2 and 3)
        marker.visitVariableInstruction(clazz, method, codeAttribute, 3,
            new VariableInstruction(Instruction.OP_ALOAD, 4));  // object param

        // Assert
        assertTrue(marker.isVariableUsed(0), "Variable 0 (this) should be used");
        assertTrue(marker.isVariableUsed(1), "Variable 1 (int) should be used");
        assertTrue(marker.isVariableUsed(2), "Variable 2 (long first slot) should be used");
        assertTrue(marker.isVariableUsed(3), "Variable 3 (long second slot) should be used");
        assertTrue(marker.isVariableUsed(4), "Variable 4 (object) should be used");
        assertFalse(marker.isVariableUsed(5), "Variable 5 should not be used");
    }

    /**
     * Tests reusing marker for different methods resets state.
     */
    @Test
    public void testReuseMarker_forDifferentMethods_resetsCorrectly() {
        // Arrange - first method
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        codeAttr1.u2maxLocals = 5;

        marker.visitCodeAttribute(clazz, method, codeAttr1);
        marker.visitVariableInstruction(clazz, method, codeAttr1, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 2));

        assertTrue(marker.isVariableUsed(2), "Variable 2 should be marked in first method");

        // Act - second method (reuse marker)
        ProgramMethod method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        codeAttr2.u2maxLocals = 5;

        marker.visitCodeAttribute(clazz, method2, codeAttr2);

        // Assert - state should be reset
        assertFalse(marker.isVariableUsed(2), "Variable 2 should be reset for second method");

        // Mark different variable in second method
        marker.visitVariableInstruction(clazz, method2, codeAttr2, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 4));

        assertFalse(marker.isVariableUsed(2), "Variable 2 should not be marked in second method");
        assertTrue(marker.isVariableUsed(4), "Variable 4 should be marked in second method");
    }

    /**
     * Tests with method having no local variables (maxLocals = 0).
     */
    @Test
    public void testWithEmptyMethod_maxLocalsZero_handlesCorrectly() {
        // Arrange
        codeAttribute.u2maxLocals = 0;

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitCodeAttribute(clazz, method, codeAttribute),
            "Should handle maxLocals of 0");
    }

    /**
     * Tests visiting variable instructions at different offsets.
     */
    @Test
    public void testVisitVariableInstruction_withDifferentOffsets_marksCorrectly() {
        // Arrange
        codeAttribute.u2maxLocals = 10;
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Act - same variable at different offsets
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 3));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 100,
            new VariableInstruction(Instruction.OP_ILOAD, 5));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 200,
            new VariableInstruction(Instruction.OP_ILOAD, 7));

        // Assert
        assertTrue(marker.isVariableUsed(3), "Variable 3 should be marked");
        assertTrue(marker.isVariableUsed(5), "Variable 5 should be marked");
        assertTrue(marker.isVariableUsed(7), "Variable 7 should be marked");
    }

    /**
     * Tests that array grows when needed for larger maxLocals.
     */
    @Test
    public void testArrayGrowth_whenMaxLocalsIncreases_createsLargerArray() {
        // Arrange - start with small array
        codeAttribute.u2maxLocals = 5;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 2));

        // Act - increase maxLocals
        codeAttribute.u2maxLocals = 50;
        marker.visitCodeAttribute(clazz, method, codeAttribute);
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 45));

        // Assert
        assertTrue(marker.isVariableUsed(45), "Should handle larger variable index");
        assertFalse(marker.isVariableUsed(2), "Old variables should be reset");
    }

    /**
     * Tests all single-slot load/store instructions.
     */
    @Test
    public void testAllSingleSlotInstructions_markCorrectly() {
        // Arrange
        codeAttribute.u2maxLocals = 20;
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Act - test all single-slot instructions
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ILOAD, 1));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_FLOAD, 2));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ALOAD, 3));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ISTORE, 5));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_FSTORE, 6));
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_ASTORE, 7));

        // Assert - each should mark only one slot
        assertTrue(marker.isVariableUsed(1), "ILOAD variable should be marked");
        assertTrue(marker.isVariableUsed(2), "FLOAD variable should be marked");
        assertTrue(marker.isVariableUsed(3), "ALOAD variable should be marked");
        assertFalse(marker.isVariableUsed(4), "Variable 4 should not be marked");
        assertTrue(marker.isVariableUsed(5), "ISTORE variable should be marked");
        assertTrue(marker.isVariableUsed(6), "FSTORE variable should be marked");
        assertTrue(marker.isVariableUsed(7), "ASTORE variable should be marked");
    }

    /**
     * Tests all double-slot (Category 2) instructions.
     */
    @Test
    public void testAllDoubleSlotInstructions_markTwoSlots() {
        // Arrange
        codeAttribute.u2maxLocals = 20;
        marker.visitCodeAttribute(clazz, method, codeAttribute);

        // Act - test all double-slot instructions
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_LLOAD, 2));   // marks 2, 3
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_DLOAD, 5));   // marks 5, 6
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_LSTORE, 8));  // marks 8, 9
        marker.visitVariableInstruction(clazz, method, codeAttribute, 0,
            new VariableInstruction(Instruction.OP_DSTORE, 11)); // marks 11, 12

        // Assert
        assertTrue(marker.isVariableUsed(2), "LLOAD first slot should be marked");
        assertTrue(marker.isVariableUsed(3), "LLOAD second slot should be marked");
        assertFalse(marker.isVariableUsed(4), "Variable 4 should not be marked");
        assertTrue(marker.isVariableUsed(5), "DLOAD first slot should be marked");
        assertTrue(marker.isVariableUsed(6), "DLOAD second slot should be marked");
        assertFalse(marker.isVariableUsed(7), "Variable 7 should not be marked");
        assertTrue(marker.isVariableUsed(8), "LSTORE first slot should be marked");
        assertTrue(marker.isVariableUsed(9), "LSTORE second slot should be marked");
        assertFalse(marker.isVariableUsed(10), "Variable 10 should not be marked");
        assertTrue(marker.isVariableUsed(11), "DSTORE first slot should be marked");
        assertTrue(marker.isVariableUsed(12), "DSTORE second slot should be marked");
    }
}

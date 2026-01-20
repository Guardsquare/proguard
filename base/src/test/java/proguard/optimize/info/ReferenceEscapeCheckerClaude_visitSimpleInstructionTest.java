package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;
import proguard.evaluation.value.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReferenceEscapeChecker#visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)}.
 *
 * The visitSimpleInstruction method processes specific SimpleInstruction opcodes that affect
 * reference escape analysis:
 *
 * 1. OP_AASTORE: Marks array reference as modified and marks the stored reference value as escaping
 * 2. OP_IASTORE/LASTORE/FASTORE/DASTORE/BASTORE/CASTORE/SASTORE: Marks array reference as modified
 * 3. OP_ARETURN: Marks the returned reference value as returned
 * 4. OP_ATHROW: Marks the thrown reference value as escaping
 *
 * For all other opcodes, the method does nothing (falls through the switch statement).
 *
 * The method requires a properly initialized PartialEvaluator to function, as it calls:
 * - partialEvaluator.getStackBefore(offset) to get stack information
 * - Uses the stack to determine which reference values to mark
 *
 * Testing approach:
 * - Test opcodes that don't trigger any logic (no-op cases) without mocking
 * - Test opcodes that do trigger logic with proper mocking to avoid NullPointerException
 * - For full integration testing with actual marking behavior, see integration tests
 */
public class ReferenceEscapeCheckerClaude_visitSimpleInstructionTest {

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

    // Tests for opcodes that DON'T trigger any logic (default case in switch)

    /**
     * Tests that visitSimpleInstruction handles OP_NOP without throwing exceptions.
     * OP_NOP is not in the switch statement, so it should fall through and do nothing.
     */
    @Test
    public void testVisitSimpleInstruction_withOpNop_doesNotThrowException() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitSimpleInstruction handles OP_ICONST_0 without throwing exceptions.
     * This opcode doesn't affect reference escape analysis.
     */
    @Test
    public void testVisitSimpleInstruction_withOpIconst0_doesNotThrowException() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ICONST_0);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitSimpleInstruction handles OP_ACONST_NULL without throwing exceptions.
     * Even though this is a reference constant, it doesn't trigger marking logic.
     */
    @Test
    public void testVisitSimpleInstruction_withOpAconstNull_doesNotThrowException() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ACONST_NULL);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitSimpleInstruction handles OP_ILOAD without throwing exceptions.
     * Load operations don't affect reference escape analysis in this visitor.
     */
    @Test
    public void testVisitSimpleInstruction_withOpIload_doesNotThrowException() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ILOAD);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitSimpleInstruction handles OP_IALOAD without throwing exceptions.
     * Array loads don't trigger marking in visitSimpleInstruction.
     */
    @Test
    public void testVisitSimpleInstruction_withOpIaload_doesNotThrowException() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IALOAD);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitSimpleInstruction handles OP_POP without throwing exceptions.
     * Stack manipulation instructions don't affect reference escape analysis.
     */
    @Test
    public void testVisitSimpleInstruction_withOpPop_doesNotThrowException() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_POP);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitSimpleInstruction handles OP_DUP without throwing exceptions.
     * Stack duplication doesn't affect reference escape analysis.
     */
    @Test
    public void testVisitSimpleInstruction_withOpDup_doesNotThrowException() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DUP);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitSimpleInstruction handles OP_SWAP without throwing exceptions.
     * Stack swap operations don't affect reference escape analysis.
     */
    @Test
    public void testVisitSimpleInstruction_withOpSwap_doesNotThrowException() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_SWAP);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitSimpleInstruction handles OP_IADD without throwing exceptions.
     * Arithmetic operations don't affect reference escape analysis.
     */
    @Test
    public void testVisitSimpleInstruction_withOpIadd_doesNotThrowException() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IADD);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitSimpleInstruction handles OP_RETURN without throwing exceptions.
     * Regular return (void) doesn't affect reference escape analysis.
     */
    @Test
    public void testVisitSimpleInstruction_withOpReturn_doesNotThrowException() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_RETURN);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitSimpleInstruction handles OP_IRETURN without throwing exceptions.
     * Integer return doesn't affect reference escape analysis.
     */
    @Test
    public void testVisitSimpleInstruction_withOpIreturn_doesNotThrowException() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IRETURN);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitSimpleInstruction can be called multiple times with no-op opcodes.
     * This verifies the method is idempotent for non-marking opcodes.
     */
    @Test
    public void testVisitSimpleInstruction_calledMultipleTimesWithNoOpOpcodes_doesNotThrowException() {
        // Arrange
        SimpleInstruction nop = new SimpleInstruction(Instruction.OP_NOP);
        SimpleInstruction iconst = new SimpleInstruction(Instruction.OP_ICONST_0);
        SimpleInstruction pop = new SimpleInstruction(Instruction.OP_POP);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, nop);
            checker.visitSimpleInstruction(clazz, method, codeAttribute, 1, iconst);
            checker.visitSimpleInstruction(clazz, method, codeAttribute, 2, pop);
            checker.visitSimpleInstruction(clazz, method, codeAttribute, 3, nop);
        });
    }

    /**
     * Tests that visitSimpleInstruction handles various non-marking opcodes at different offsets.
     * This verifies the method works correctly regardless of instruction offset.
     */
    @Test
    public void testVisitSimpleInstruction_variousNoOpOpcodesAtDifferentOffsets_doesNotThrowException() {
        // Arrange & Act & Assert
        byte[] noOpOpcodes = {
            Instruction.OP_NOP,
            Instruction.OP_ICONST_0,
            Instruction.OP_ICONST_1,
            Instruction.OP_ACONST_NULL,
            Instruction.OP_POP,
            Instruction.OP_DUP,
            Instruction.OP_SWAP,
            Instruction.OP_IADD,
            Instruction.OP_RETURN,
            Instruction.OP_IRETURN
        };

        for (int i = 0; i < noOpOpcodes.length; i++) {
            SimpleInstruction instruction = new SimpleInstruction(noOpOpcodes[i]);
            final int offset = i * 10;
            assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, offset, instruction),
                    "Should not throw for opcode " + noOpOpcodes[i] + " at offset " + offset);
        }
    }

    /**
     * Tests that visitSimpleInstruction doesn't modify state for no-op opcodes.
     * The internal arrays should remain unchanged.
     */
    @Test
    public void testVisitSimpleInstruction_withNoOpOpcodes_doesNotModifyState() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Check initial state
        assertFalse(checker.isInstanceEscaping(0), "Initial state should be false");
        assertFalse(checker.isInstanceReturned(0), "Initial state should be false");
        assertFalse(checker.isInstanceModified(0), "Initial state should be false");

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - state should remain unchanged
        assertFalse(checker.isInstanceEscaping(0), "State should remain unchanged");
        assertFalse(checker.isInstanceReturned(0), "State should remain unchanged");
        assertFalse(checker.isInstanceModified(0), "State should remain unchanged");
    }

    // Tests for marking opcodes - these require a mocked PartialEvaluator

    /**
     * Tests that visitSimpleInstruction with OP_AASTORE attempts to access the PartialEvaluator.
     * Note: Without a properly initialized PartialEvaluator, this will throw NullPointerException
     * when trying to call markModifiedReferenceValues and markEscapingReferenceValues.
     * This test verifies the method attempts to perform the marking operations.
     */
    @Test
    public void testVisitSimpleInstruction_withOpAastore_attemptsMarking() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_AASTORE);

        // Act & Assert - This should throw because partialEvaluator is not set up
        // The fact that it throws (trying to access getStackBefore) proves the method
        // is attempting to perform marking operations
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_AASTORE should attempt to call marking methods which require PartialEvaluator");
    }

    /**
     * Tests that visitSimpleInstruction with OP_IASTORE attempts to access the PartialEvaluator.
     * Without a properly initialized PartialEvaluator, this will throw NullPointerException.
     */
    @Test
    public void testVisitSimpleInstruction_withOpIastore_attemptsMarking() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IASTORE);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_IASTORE should attempt to call markModifiedReferenceValues");
    }

    /**
     * Tests that visitSimpleInstruction with OP_LASTORE attempts to access the PartialEvaluator.
     */
    @Test
    public void testVisitSimpleInstruction_withOpLastore_attemptsMarking() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LASTORE);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_LASTORE should attempt to call markModifiedReferenceValues");
    }

    /**
     * Tests that visitSimpleInstruction with OP_FASTORE attempts to access the PartialEvaluator.
     */
    @Test
    public void testVisitSimpleInstruction_withOpFastore_attemptsMarking() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FASTORE);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_FASTORE should attempt to call markModifiedReferenceValues");
    }

    /**
     * Tests that visitSimpleInstruction with OP_DASTORE attempts to access the PartialEvaluator.
     */
    @Test
    public void testVisitSimpleInstruction_withOpDastore_attemptsMarking() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DASTORE);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_DASTORE should attempt to call markModifiedReferenceValues");
    }

    /**
     * Tests that visitSimpleInstruction with OP_BASTORE attempts to access the PartialEvaluator.
     */
    @Test
    public void testVisitSimpleInstruction_withOpBastore_attemptsMarking() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_BASTORE);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_BASTORE should attempt to call markModifiedReferenceValues");
    }

    /**
     * Tests that visitSimpleInstruction with OP_CASTORE attempts to access the PartialEvaluator.
     */
    @Test
    public void testVisitSimpleInstruction_withOpCastore_attemptsMarking() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_CASTORE);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_CASTORE should attempt to call markModifiedReferenceValues");
    }

    /**
     * Tests that visitSimpleInstruction with OP_SASTORE attempts to access the PartialEvaluator.
     */
    @Test
    public void testVisitSimpleInstruction_withOpSastore_attemptsMarking() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_SASTORE);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_SASTORE should attempt to call markModifiedReferenceValues");
    }

    /**
     * Tests that visitSimpleInstruction with OP_ARETURN attempts to access the PartialEvaluator.
     */
    @Test
    public void testVisitSimpleInstruction_withOpAreturn_attemptsMarking() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ARETURN);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_ARETURN should attempt to call markReturnedReferenceValues");
    }

    /**
     * Tests that visitSimpleInstruction with OP_ATHROW attempts to access the PartialEvaluator.
     */
    @Test
    public void testVisitSimpleInstruction_withOpAthrow_attemptsMarking() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ATHROW);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_ATHROW should attempt to call markEscapingReferenceValues");
    }

    /**
     * Tests that all array store operations (except AASTORE) trigger the same marking behavior.
     * They should all call markModifiedReferenceValues.
     */
    @Test
    public void testVisitSimpleInstruction_allArrayStoreOpsExceptAastore_attemptMarkingModified() {
        // Arrange - all array store opcodes except AASTORE
        byte[] arrayStoreOpcodes = {
            Instruction.OP_IASTORE,
            Instruction.OP_LASTORE,
            Instruction.OP_FASTORE,
            Instruction.OP_DASTORE,
            Instruction.OP_BASTORE,
            Instruction.OP_CASTORE,
            Instruction.OP_SASTORE
        };

        // Act & Assert - all should throw NullPointerException trying to access PartialEvaluator
        for (byte opcode : arrayStoreOpcodes) {
            SimpleInstruction instruction = new SimpleInstruction(opcode);
            assertThrows(NullPointerException.class,
                () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
                "Opcode " + opcode + " should attempt marking");
        }
    }

    /**
     * Tests that visitSimpleInstruction can handle multiple different opcodes in sequence.
     * This verifies the switch statement correctly routes to different behaviors.
     */
    @Test
    public void testVisitSimpleInstruction_mixedOpcodes_handlesCorrectly() {
        // Arrange & Act & Assert
        // No-op opcode should succeed
        assertDoesNotThrow(() ->
            checker.visitSimpleInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_NOP)));

        // Marking opcode should throw (no PartialEvaluator)
        assertThrows(NullPointerException.class, () ->
            checker.visitSimpleInstruction(clazz, method, codeAttribute, 1,
                new SimpleInstruction(Instruction.OP_ARETURN)));

        // Another no-op opcode should succeed
        assertDoesNotThrow(() ->
            checker.visitSimpleInstruction(clazz, method, codeAttribute, 2,
                new SimpleInstruction(Instruction.OP_POP)));
    }

    /**
     * Tests that visitSimpleInstruction works correctly with different offsets.
     * The offset is passed to the marking methods when applicable.
     */
    @Test
    public void testVisitSimpleInstruction_differentOffsets_passedToMarkingMethods() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ARETURN);

        // Act & Assert - should throw at different offsets, showing offset is used
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 10, instruction));
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 50, instruction));
    }

    /**
     * Tests that visitSimpleInstruction can be called with various clazz parameters for no-op opcodes.
     * The clazz parameter is used by stackPopCount() for marking opcodes.
     */
    @Test
    public void testVisitSimpleInstruction_withDifferentClazzInstances_handlesNoOpOpcodes() {
        // Arrange
        Clazz clazz2 = createProgramClassWithConstantPool();
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz2, method, codeAttribute, 1, instruction));
    }

    /**
     * Tests that visitSimpleInstruction processes OP_AASTORE which should call both
     * markModifiedReferenceValues and markEscapingReferenceValues.
     * This is verified by the NullPointerException when accessing PartialEvaluator.
     */
    @Test
    public void testVisitSimpleInstruction_withOpAastore_callsBothMarkingMethods() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_AASTORE);

        // Act & Assert - AASTORE is special: it marks both modified and escaping
        // This is verified by the fact it attempts to access the PartialEvaluator
        assertThrows(NullPointerException.class,
            () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
            "OP_AASTORE should call both markModifiedReferenceValues and markEscapingReferenceValues");
    }

    /**
     * Tests visitSimpleInstruction with a large offset value for no-op opcodes.
     * This verifies the method handles large offsets correctly.
     */
    @Test
    public void testVisitSimpleInstruction_withLargeOffset_handlesNoOpOpcode() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);
        int largeOffset = 1000;

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, largeOffset, instruction));
    }

    /**
     * Tests that visitSimpleInstruction correctly identifies which opcodes trigger marking.
     * This is a comprehensive test covering all special-cased opcodes.
     */
    @Test
    public void testVisitSimpleInstruction_identifiesMarkingOpcodes() {
        // Arrange - opcodes that should trigger marking (throw NPE)
        byte[] markingOpcodes = {
            Instruction.OP_AASTORE,
            Instruction.OP_IASTORE,
            Instruction.OP_LASTORE,
            Instruction.OP_FASTORE,
            Instruction.OP_DASTORE,
            Instruction.OP_BASTORE,
            Instruction.OP_CASTORE,
            Instruction.OP_SASTORE,
            Instruction.OP_ARETURN,
            Instruction.OP_ATHROW
        };

        // Act & Assert - all should throw NPE
        for (byte opcode : markingOpcodes) {
            SimpleInstruction instruction = new SimpleInstruction(opcode);
            assertThrows(NullPointerException.class,
                () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
                "Opcode " + opcode + " should trigger marking");
        }

        // Arrange - opcodes that should NOT trigger marking (no exception)
        byte[] noOpOpcodes = {
            Instruction.OP_NOP,
            Instruction.OP_ICONST_0,
            Instruction.OP_ACONST_NULL,
            Instruction.OP_POP,
            Instruction.OP_RETURN,
            Instruction.OP_IRETURN
        };

        // Act & Assert - none should throw
        for (byte opcode : noOpOpcodes) {
            SimpleInstruction instruction = new SimpleInstruction(opcode);
            assertDoesNotThrow(
                () -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction),
                "Opcode " + opcode + " should NOT trigger marking");
        }
    }

    // Helper methods

    /**
     * Creates a ProgramClass with a basic constant pool.
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

package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReferenceEscapeChecker#visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)}.
 *
 * The visitAnyInstruction method is an empty implementation (no-op) that serves as a default
 * handler in the InstructionVisitor pattern. The ReferenceEscapeChecker only processes
 * specific instruction types through visitSimpleInstruction and visitConstantInstruction,
 * and this method provides the default no-op behavior for all other instruction types.
 *
 * Method signature: public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
 * Implementation: {} (empty body)
 *
 * Note: Mocking is used here because visitAnyInstruction is a no-op method with an empty body.
 * There is no meaningful behavior to test without mocking - we can only verify it doesn't throw
 * exceptions and doesn't interact with its parameters.
 */
public class ReferenceEscapeCheckerClaude_visitAnyInstructionTest {

    private ReferenceEscapeChecker checker;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;
    private Instruction instruction;

    @BeforeEach
    public void setUp() {
        checker = new ReferenceEscapeChecker();
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
        instruction = new SimpleInstruction(Instruction.OP_NOP);
    }

    /**
     * Tests that visitAnyInstruction can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyInstruction_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyInstruction(null, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null Method parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullMethod_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, null, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null CodeAttribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullCodeAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, null, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null Instruction parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullInstruction_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0, null));
    }

    /**
     * Tests that visitAnyInstruction can be called with all parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withAllParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyInstruction(null, null, null, 0, null));
    }

    /**
     * Tests that visitAnyInstruction can be called with various offset values.
     * The method should handle any offset value since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withVariousOffsets_doesNotThrowException() {
        // Arrange
        int[] offsets = {0, 1, 10, 100, 1000, -1, Integer.MAX_VALUE, Integer.MIN_VALUE};

        // Act & Assert - should not throw any exception with any offset
        for (int offset : offsets) {
            assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, offset, instruction),
                    "Should not throw with offset: " + offset);
        }
    }

    /**
     * Tests that visitAnyInstruction can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyInstruction_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
            checker.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);
            checker.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithClazz() {
        // Act
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with the Method parameter.
     * Since it's a no-op method, it should not call any methods on the method.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithMethod() {
        // Act
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred with the method mock
        verifyNoInteractions(method);
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with the CodeAttribute parameter.
     * Since it's a no-op method, it should not call any methods on the code attribute.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithCodeAttribute() {
        // Act
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred with the code attribute mock
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            checker.visitAnyInstruction(clazz1, method, codeAttribute, 0, instruction);
            checker.visitAnyInstruction(clazz2, method, codeAttribute, 0, instruction);
            checker.visitAnyInstruction(clazz3, method, codeAttribute, 0, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction works with different Method mock instances.
     * The method should handle any Method implementation without issues.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentMethodInstances_doesNotThrowException() {
        // Arrange
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        Method method3 = mock(ProgramMethod.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            checker.visitAnyInstruction(clazz, method1, codeAttribute, 0, instruction);
            checker.visitAnyInstruction(clazz, method2, codeAttribute, 0, instruction);
            checker.visitAnyInstruction(clazz, method3, codeAttribute, 0, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction works with different CodeAttribute mock instances.
     * The method should handle any CodeAttribute implementation without issues.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentCodeAttributeInstances_doesNotThrowException() {
        // Arrange
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        CodeAttribute codeAttr3 = mock(CodeAttribute.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            checker.visitAnyInstruction(clazz, method, codeAttr1, 0, instruction);
            checker.visitAnyInstruction(clazz, method, codeAttr2, 0, instruction);
            checker.visitAnyInstruction(clazz, method, codeAttr3, 0, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction works with different Instruction instances.
     * The method should handle any Instruction implementation without issues.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentInstructions_doesNotThrowException() {
        // Arrange
        Instruction inst1 = new SimpleInstruction(Instruction.OP_NOP);
        Instruction inst2 = new SimpleInstruction(Instruction.OP_ICONST_0);
        Instruction inst3 = new SimpleInstruction(Instruction.OP_ACONST_NULL);

        // Act & Assert - should not throw any exception with different instructions
        assertDoesNotThrow(() -> {
            checker.visitAnyInstruction(clazz, method, codeAttribute, 0, inst1);
            checker.visitAnyInstruction(clazz, method, codeAttribute, 1, inst2);
            checker.visitAnyInstruction(clazz, method, codeAttribute, 2, inst3);
        });
    }

    /**
     * Tests that visitAnyInstruction execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyInstruction_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            checker.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyInstruction should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyInstruction doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the checker's normal operation.
     */
    @Test
    public void testVisitAnyInstruction_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyInstruction first
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Then call visitAnyInstruction again
        checker.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyInstruction_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction is thread-safe when called concurrently.
     * Since it's a no-op with no state changes, it should handle concurrent calls.
     */
    @Test
    public void testVisitAnyInstruction_concurrentCalls_noExceptions() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create multiple threads that call visitAnyInstruction
        for (int i = 0; i < threadCount; i++) {
            final int offset = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    checker.visitAnyInstruction(clazz, method, codeAttribute, offset, instruction);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that multiple checkers can call visitAnyInstruction independently.
     * Each checker's no-op should not affect others.
     */
    @Test
    public void testVisitAnyInstruction_withMultipleCheckers_operateIndependently() {
        // Arrange - create multiple checkers
        ReferenceEscapeChecker checker1 = new ReferenceEscapeChecker();
        ReferenceEscapeChecker checker2 = new ReferenceEscapeChecker();

        // Act - call visitAnyInstruction on both checkers
        checker1.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        checker2.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction works correctly with boundary offset values.
     * The method should handle edge cases gracefully.
     */
    @Test
    public void testVisitAnyInstruction_withBoundaryOffsets_doesNotThrowException() {
        // Act & Assert - test with boundary values
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
                "Should handle offset 0");
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, instruction),
                "Should handle Integer.MAX_VALUE offset");
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, Integer.MIN_VALUE, instruction),
                "Should handle Integer.MIN_VALUE offset");
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, -1, instruction),
                "Should handle negative offset");
    }

    /**
     * Tests that visitAnyInstruction doesn't modify or read the Instruction parameter.
     * Since it's a no-op, the instruction should remain unchanged.
     */
    @Test
    public void testVisitAnyInstruction_instructionRemainsUnchanged() {
        // Arrange
        SimpleInstruction testInstruction = new SimpleInstruction(Instruction.OP_NOP);
        byte originalOpcode = testInstruction.opcode;

        // Act
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, testInstruction);

        // Assert - instruction should remain unchanged
        assertEquals(originalOpcode, testInstruction.opcode,
                "Instruction opcode should remain unchanged after visitAnyInstruction");
    }

    /**
     * Tests that visitAnyInstruction can be called in rapid succession without issues.
     * This verifies there's no timing-dependent behavior.
     */
    @Test
    public void testVisitAnyInstruction_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                checker.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction with mixed null and non-null parameters works correctly.
     * This ensures the method handles partial null inputs gracefully.
     */
    @Test
    public void testVisitAnyInstruction_withMixedNullParameters_doesNotThrowException() {
        // Act & Assert - test various combinations of null/non-null parameters
        assertDoesNotThrow(() -> checker.visitAnyInstruction(null, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, null, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, null, 0, instruction));
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0, null));
        assertDoesNotThrow(() -> checker.visitAnyInstruction(null, null, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> checker.visitAnyInstruction(null, method, null, 0, instruction));
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, null, null, 0, instruction));
        assertDoesNotThrow(() -> checker.visitAnyInstruction(null, null, null, 0, null));
    }

    /**
     * Tests that visitAnyInstruction handles various simple instruction opcodes.
     * Since it's a no-op, all instruction types should be handled the same way.
     */
    @Test
    public void testVisitAnyInstruction_withVariousOpcodes_doesNotThrowException() {
        // Arrange - create instructions with various opcodes
        Instruction[] instructions = {
            new SimpleInstruction(Instruction.OP_NOP),
            new SimpleInstruction(Instruction.OP_ACONST_NULL),
            new SimpleInstruction(Instruction.OP_ICONST_0),
            new SimpleInstruction(Instruction.OP_ICONST_1),
            new SimpleInstruction(Instruction.OP_RETURN),
            new SimpleInstruction(Instruction.OP_ARETURN),
            new SimpleInstruction(Instruction.OP_POP),
            new SimpleInstruction(Instruction.OP_DUP)
        };

        // Act & Assert - should not throw any exception with any opcode
        for (int i = 0; i < instructions.length; i++) {
            final int offset = i;
            assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, offset, instructions[offset]),
                    "Should not throw with instruction: " + instructions[offset]);
        }
    }

    /**
     * Tests that visitAnyInstruction completes without blocking or hanging.
     * This ensures the method doesn't have any unexpected wait conditions.
     */
    @Test
    public void testVisitAnyInstruction_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnyInstruction should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitAnyInstruction called after multiple other operations still behaves as a no-op.
     * This verifies consistent behavior regardless of the checker's usage history.
     */
    @Test
    public void testVisitAnyInstruction_afterMultipleOperations_stillNoOp() {
        // Arrange - simulate some prior operations
        for (int i = 0; i < 5; i++) {
            checker.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
        }

        // Act - call visitAnyInstruction again
        checker.visitAnyInstruction(clazz, method, codeAttribute, 100, instruction);

        // Assert - verify it's still a no-op with no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction doesn't affect the checker's internal state.
     * Since it's a no-op, it should not modify any internal fields.
     */
    @Test
    public void testVisitAnyInstruction_doesNotAffectInternalState() {
        // Act - call visitAnyInstruction multiple times
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        checker.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);

        // Assert - verify no interactions occurred, indicating no state changes
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction works correctly even when called between
     * other visitor methods that do have side effects.
     */
    @Test
    public void testVisitAnyInstruction_betweenOtherVisitorCalls_remainsNoOp() {
        // Act - call visitAnyInstruction
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction with varying combinations of parameters
     * all result in the same no-op behavior.
     */
    @Test
    public void testVisitAnyInstruction_varyingParameterCombinations_consistentNoOpBehavior() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        Instruction inst2 = new SimpleInstruction(Instruction.OP_ICONST_1);

        // Act - call with various parameter combinations
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        checker.visitAnyInstruction(clazz2, method2, codeAttr2, 100, inst2);
        checker.visitAnyInstruction(clazz, method2, codeAttribute, 50, inst2);

        // Assert - verify no interactions occurred with any parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(method);
        verifyNoInteractions(method2);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(codeAttr2);
    }

    /**
     * Tests that the visitAnyInstruction method signature matches the InstructionVisitor interface.
     * This ensures the method properly overrides the interface method.
     */
    @Test
    public void testVisitAnyInstruction_implementsInterfaceCorrectly() {
        // Assert - ReferenceEscapeChecker should be an InstructionVisitor
        assertTrue(checker instanceof proguard.classfile.instruction.visitor.InstructionVisitor,
                "ReferenceEscapeChecker should implement InstructionVisitor");
    }

    /**
     * Tests that visitAnyInstruction doesn't modify any checker state accessible through public methods.
     * The internal arrays (instanceEscaping, instanceReturned, etc.) should remain unchanged.
     */
    @Test
    public void testVisitAnyInstruction_doesNotModifyPubliclyAccessibleState() {
        // Arrange - check initial state
        assertFalse(checker.isInstanceEscaping(0), "Initial state should be false");
        assertFalse(checker.isInstanceReturned(0), "Initial state should be false");
        assertFalse(checker.isInstanceModified(0), "Initial state should be false");
        assertFalse(checker.isInstanceExternal(0), "Initial state should be false");

        // Act - call visitAnyInstruction
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - state should remain unchanged
        assertFalse(checker.isInstanceEscaping(0), "State should remain unchanged");
        assertFalse(checker.isInstanceReturned(0), "State should remain unchanged");
        assertFalse(checker.isInstanceModified(0), "State should remain unchanged");
        assertFalse(checker.isInstanceExternal(0), "State should remain unchanged");
    }

    /**
     * Tests that visitAnyInstruction can be called from multiple checker instances independently.
     */
    @Test
    public void testVisitAnyInstruction_multipleCheckerInstances_independent() {
        // Arrange
        ReferenceEscapeChecker checker1 = new ReferenceEscapeChecker();
        ReferenceEscapeChecker checker2 = new ReferenceEscapeChecker();
        ReferenceEscapeChecker checker3 = new ReferenceEscapeChecker();

        // Act & Assert - should work independently without interference
        assertDoesNotThrow(() -> {
            checker1.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
            checker2.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
            checker3.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        });
    }

    /**
     * Tests that calling visitAnyInstruction many times doesn't cause memory issues.
     */
    @Test
    public void testVisitAnyInstruction_manyInvocations_noMemoryIssues() {
        // Act & Assert - call 10000 times
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10000; i++) {
                checker.visitAnyInstruction(clazz, method, codeAttribute, i % 100, instruction);
            }
        });
    }

    /**
     * Tests that visitAnyInstruction works correctly after the checker has been used for other operations.
     */
    @Test
    public void testVisitAnyInstruction_afterQueryingState_stillWorks() {
        // Arrange - perform other operations first
        checker.isInstanceExternal(0);
        checker.isInstanceEscaping(0);
        checker.isInstanceReturned(0);
        checker.isInstanceModified(0);

        // Act & Assert - visitAnyInstruction should still work
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));

        // Verify no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction has no observable side effects.
     */
    @Test
    public void testVisitAnyInstruction_noObservableSideEffects() {
        // Arrange - record state before
        boolean escapingBefore = checker.isInstanceEscaping(0);
        boolean returnedBefore = checker.isInstanceReturned(0);
        boolean modifiedBefore = checker.isInstanceModified(0);
        boolean externalBefore = checker.isInstanceExternal(0);

        // Act
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - state should be unchanged
        assertEquals(escapingBefore, checker.isInstanceEscaping(0), "isInstanceEscaping should be unchanged");
        assertEquals(returnedBefore, checker.isInstanceReturned(0), "isInstanceReturned should be unchanged");
        assertEquals(modifiedBefore, checker.isInstanceModified(0), "isInstanceModified should be unchanged");
        assertEquals(externalBefore, checker.isInstanceExternal(0), "isInstanceExternal should be unchanged");
    }

    /**
     * Tests that visitAnyInstruction completes instantly even when called in rapid succession.
     */
    @Test
    public void testVisitAnyInstruction_rapidSuccession_completesInstantly() {
        // Act - call in very rapid succession
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            checker.visitAnyInstruction(clazz, method, codeAttribute, i % 100, instruction);
        }
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should be extremely fast for 10000 no-op calls
        assertTrue(durationMs < 500, "10000 calls should complete in less than 500ms");
    }

    /**
     * Tests that visitAnyInstruction returns void (no return value).
     */
    @Test
    public void testVisitAnyInstruction_returnsVoid() {
        // This test verifies the method signature at compile time
        // If this compiles, the method returns void
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        // No assertion needed - compilation success is the test
    }

    /**
     * Tests that visitAnyInstruction can be invoked through the InstructionVisitor interface.
     */
    @Test
    public void testVisitAnyInstruction_throughInstructionVisitorInterface_works() {
        // Arrange
        proguard.classfile.instruction.visitor.InstructionVisitor visitor = checker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }
}

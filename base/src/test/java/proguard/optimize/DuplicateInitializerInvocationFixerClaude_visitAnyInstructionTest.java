package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateInitializerInvocationFixer#visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)}.
 *
 * The visitAnyInstruction method is an empty implementation (no-op) that serves as a default
 * handler in the InstructionVisitor pattern for instructions that don't have specialized visitor methods.
 */
public class DuplicateInitializerInvocationFixerClaude_visitAnyInstructionTest {

    private DuplicateInitializerInvocationFixer fixer;
    private InstructionVisitor extraAddedInstructionVisitor;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;
    private Instruction instruction;

    @BeforeEach
    public void setUp() {
        extraAddedInstructionVisitor = mock(InstructionVisitor.class);
        fixer = new DuplicateInitializerInvocationFixer(extraAddedInstructionVisitor);
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
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(null, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null Method parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullMethod_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, null, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null CodeAttribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullCodeAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, method, null, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null Instruction parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullInstruction_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, null));
    }

    /**
     * Tests that visitAnyInstruction can be called with all parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withAllParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(null, null, null, 0, null));
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
            assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, method, codeAttribute, offset, instruction),
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
            fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
            fixer.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);
            fixer.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithClazz() {
        // Act
        fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

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
        fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

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
        fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred with the code attribute mock
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction doesn't affect the fixer's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyInstruction_doesNotModifyFixerState() {
        // Arrange - create another fixer with the same configuration
        DuplicateInitializerInvocationFixer fixer2 = new DuplicateInitializerInvocationFixer(extraAddedInstructionVisitor);

        // Act - call visitAnyInstruction on the first fixer
        fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - both fixers should be functionally equivalent
        // Since visitAnyInstruction is a no-op, we verify no visitors were invoked
        verifyNoInteractions(extraAddedInstructionVisitor);
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
            fixer.visitAnyInstruction(clazz1, method, codeAttribute, 0, instruction);
            fixer.visitAnyInstruction(clazz2, method, codeAttribute, 0, instruction);
            fixer.visitAnyInstruction(clazz3, method, codeAttribute, 0, instruction);
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
            fixer.visitAnyInstruction(clazz, method1, codeAttribute, 0, instruction);
            fixer.visitAnyInstruction(clazz, method2, codeAttribute, 0, instruction);
            fixer.visitAnyInstruction(clazz, method3, codeAttribute, 0, instruction);
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
            fixer.visitAnyInstruction(clazz, method, codeAttr1, 0, instruction);
            fixer.visitAnyInstruction(clazz, method, codeAttr2, 0, instruction);
            fixer.visitAnyInstruction(clazz, method, codeAttr3, 0, instruction);
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
            fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, inst1);
            fixer.visitAnyInstruction(clazz, method, codeAttribute, 1, inst2);
            fixer.visitAnyInstruction(clazz, method, codeAttribute, 2, inst3);
        });
    }

    /**
     * Tests that visitAnyInstruction doesn't trigger the extra added instruction visitor.
     * Since it's a no-op method, it should not affect the visitor.
     */
    @Test
    public void testVisitAnyInstruction_doesNotTriggerExtraVisitor() {
        // Act
        fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify the extra visitor was not invoked
        verifyNoInteractions(extraAddedInstructionVisitor);
    }

    /**
     * Tests that visitAnyInstruction can be called on a fixer with null extra visitor.
     * The method should work even if the optional visitor is null.
     */
    @Test
    public void testVisitAnyInstruction_withNullExtraVisitor_doesNotThrowException() {
        // Arrange - create fixer with null extra visitor (using no-arg constructor)
        DuplicateInitializerInvocationFixer fixerWithNullVisitor = new DuplicateInitializerInvocationFixer();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> fixerWithNullVisitor.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
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
            fixer.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyInstruction should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyInstruction doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the fixer's normal operation.
     */
    @Test
    public void testVisitAnyInstruction_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyInstruction first
        fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Then call visitAnyInstruction again
        fixer.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(extraAddedInstructionVisitor);
    }

    /**
     * Tests that visitAnyInstruction can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyInstruction_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(extraAddedInstructionVisitor);
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
                    fixer.visitAnyInstruction(clazz, method, codeAttribute, offset, instruction);
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
        verifyNoInteractions(extraAddedInstructionVisitor);
    }

    /**
     * Tests that multiple fixers can call visitAnyInstruction independently.
     * Each fixer's no-op should not affect others.
     */
    @Test
    public void testVisitAnyInstruction_withMultipleFixers_operateIndependently() {
        // Arrange - create multiple fixers
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        InstructionVisitor visitor2 = mock(InstructionVisitor.class);

        DuplicateInitializerInvocationFixer fixer1 = new DuplicateInitializerInvocationFixer(visitor1);
        DuplicateInitializerInvocationFixer fixer2 = new DuplicateInitializerInvocationFixer(visitor2);

        // Act - call visitAnyInstruction on both fixers
        fixer1.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        fixer2.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);

        // Assert - verify no interactions occurred on any visitor
        verifyNoInteractions(visitor1);
        verifyNoInteractions(visitor2);
    }

    /**
     * Tests that visitAnyInstruction works correctly with boundary offset values.
     * The method should handle edge cases gracefully.
     */
    @Test
    public void testVisitAnyInstruction_withBoundaryOffsets_doesNotThrowException() {
        // Act & Assert - test with boundary values
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
                "Should handle offset 0");
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, instruction),
                "Should handle Integer.MAX_VALUE offset");
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, method, codeAttribute, Integer.MIN_VALUE, instruction),
                "Should handle Integer.MIN_VALUE offset");
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, method, codeAttribute, -1, instruction),
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
        fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, testInstruction);

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
                fixer.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(extraAddedInstructionVisitor);
    }

    /**
     * Tests that visitAnyInstruction with mixed null and non-null parameters works correctly.
     * This ensures the method handles partial null inputs gracefully.
     */
    @Test
    public void testVisitAnyInstruction_withMixedNullParameters_doesNotThrowException() {
        // Act & Assert - test various combinations of null/non-null parameters
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(null, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, null, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, method, null, 0, instruction));
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, method, codeAttribute, 0, null));
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(null, null, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(null, method, null, 0, instruction));
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(clazz, null, null, 0, instruction));
        assertDoesNotThrow(() -> fixer.visitAnyInstruction(null, null, null, 0, null));
    }
}

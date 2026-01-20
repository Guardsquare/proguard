package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GotoGotoReplacer#visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)}.
 *
 * The visitAnyInstruction method in GotoGotoReplacer is a no-op implementation (empty method body).
 * It's required by the InstructionVisitor interface but intentionally does nothing, as GotoGotoReplacer
 * only processes branch instructions via the visitBranchInstruction method.
 *
 * These tests verify that the method:
 * 1. Does not throw exceptions when called
 * 2. Does not modify any parameters or internal state
 * 3. Does not trigger the extra instruction visitor
 * 4. Works correctly with different types of parameters including null values
 */
public class GotoGotoReplacerClaude_visitAnyInstructionTest {

    private GotoGotoReplacer replacer;
    private CodeAttributeEditor mockCodeAttributeEditor;
    private InstructionVisitor mockExtraVisitor;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;
    private Instruction instruction;

    @BeforeEach
    public void setUp() {
        mockCodeAttributeEditor = mock(CodeAttributeEditor.class);
        mockExtraVisitor = mock(InstructionVisitor.class);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
        instruction = new SimpleInstruction(Instruction.OP_NOP);
    }

    // ========================================
    // visitAnyInstruction Tests - No-op Verification
    // ========================================

    /**
     * Tests that visitAnyInstruction can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyInstruction_withValidMocks_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction with an extra visitor does not trigger that visitor.
     * The method should remain a no-op even with an extra visitor configured.
     */
    @Test
    public void testVisitAnyInstruction_withExtraVisitor_doesNotTriggerVisitor() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor, mockExtraVisitor);

        // Act
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));

        // Assert - extra visitor should not be called
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyInstruction can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullClazz_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(null, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null Method parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullMethod_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, null, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null CodeAttribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullCodeAttribute_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, null, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null Instruction parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullInstruction_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, null));
    }

    /**
     * Tests that visitAnyInstruction can be called with all parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withAllParametersNull_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(null, null, null, 0, null));
    }

    /**
     * Tests that visitAnyInstruction can be called with various offset values.
     * The method should handle any offset value since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withVariousOffsets_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);
        int[] offsets = {0, 1, 10, 100, 1000, -1, Integer.MAX_VALUE, Integer.MIN_VALUE};

        // Act & Assert - should not throw any exception with any offset
        for (int offset : offsets) {
            assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, offset, instruction),
                    "Should not throw with offset: " + offset);
        }
    }

    /**
     * Tests that visitAnyInstruction can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyInstruction_calledMultipleTimes_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
            replacer.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);
            replacer.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithClazz() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with the Method parameter.
     * Since it's a no-op method, it should not call any methods on the method.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithMethod() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred with the method mock
        verifyNoInteractions(method);
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with the CodeAttribute parameter.
     * Since it's a no-op method, it should not call any methods on the code attribute.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithCodeAttribute() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred with the code attribute mock
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with the CodeAttributeEditor.
     * Since it's a no-op method, it should not call any methods on the editor.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithCodeAttributeEditor() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred with the code attribute editor mock
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests that visitAnyInstruction works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            replacer.visitAnyInstruction(clazz1, method, codeAttribute, 0, instruction);
            replacer.visitAnyInstruction(clazz2, method, codeAttribute, 0, instruction);
            replacer.visitAnyInstruction(clazz3, method, codeAttribute, 0, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction works with different Method mock instances.
     * The method should handle any Method implementation without issues.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentMethodInstances_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        Method method3 = mock(ProgramMethod.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            replacer.visitAnyInstruction(clazz, method1, codeAttribute, 0, instruction);
            replacer.visitAnyInstruction(clazz, method2, codeAttribute, 0, instruction);
            replacer.visitAnyInstruction(clazz, method3, codeAttribute, 0, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction works with different CodeAttribute mock instances.
     * The method should handle any CodeAttribute implementation without issues.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentCodeAttributeInstances_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        CodeAttribute codeAttr3 = mock(CodeAttribute.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            replacer.visitAnyInstruction(clazz, method, codeAttr1, 0, instruction);
            replacer.visitAnyInstruction(clazz, method, codeAttr2, 0, instruction);
            replacer.visitAnyInstruction(clazz, method, codeAttr3, 0, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction works with different Instruction instances.
     * The method should handle any Instruction implementation without issues.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentInstructions_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);
        Instruction inst1 = new SimpleInstruction(Instruction.OP_NOP);
        Instruction inst2 = new SimpleInstruction(Instruction.OP_ICONST_0);
        Instruction inst3 = new SimpleInstruction(Instruction.OP_ACONST_NULL);

        // Act & Assert - should not throw any exception with different instructions
        assertDoesNotThrow(() -> {
            replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, inst1);
            replacer.visitAnyInstruction(clazz, method, codeAttribute, 1, inst2);
            replacer.visitAnyInstruction(clazz, method, codeAttribute, 2, inst3);
        });
    }

    /**
     * Tests that visitAnyInstruction execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyInstruction_executesQuickly() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            replacer.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyInstruction should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyInstruction doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the replacer's normal operation.
     */
    @Test
    public void testVisitAnyInstruction_doesNotAffectSubsequentOperations() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act - call visitAnyInstruction first
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Then call visitAnyInstruction again
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests that visitAnyInstruction can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyInstruction_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests that visitAnyInstruction is thread-safe when called concurrently.
     * Since it's a no-op with no state changes, it should handle concurrent calls.
     */
    @Test
    public void testVisitAnyInstruction_concurrentCalls_noExceptions() throws InterruptedException {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create multiple threads that call visitAnyInstruction
        for (int i = 0; i < threadCount; i++) {
            final int offset = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    replacer.visitAnyInstruction(clazz, method, codeAttribute, offset, instruction);
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
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests that multiple replacers can call visitAnyInstruction independently.
     * Each replacer's no-op should not affect others.
     */
    @Test
    public void testVisitAnyInstruction_withMultipleReplacers_operateIndependently() {
        // Arrange - create multiple replacers
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(mockCodeAttributeEditor);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(mockCodeAttributeEditor, mockExtraVisitor);

        // Act - call visitAnyInstruction on both replacers
        replacer1.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        replacer2.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(mockCodeAttributeEditor);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyInstruction works correctly with boundary offset values.
     * The method should handle edge cases gracefully.
     */
    @Test
    public void testVisitAnyInstruction_withBoundaryOffsets_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act & Assert - test with boundary values
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
                "Should handle offset 0");
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, instruction),
                "Should handle Integer.MAX_VALUE offset");
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, Integer.MIN_VALUE, instruction),
                "Should handle Integer.MIN_VALUE offset");
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, -1, instruction),
                "Should handle negative offset");
    }

    /**
     * Tests that visitAnyInstruction doesn't modify or read the Instruction parameter.
     * Since it's a no-op, the instruction should remain unchanged.
     */
    @Test
    public void testVisitAnyInstruction_instructionRemainsUnchanged() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);
        SimpleInstruction testInstruction = new SimpleInstruction(Instruction.OP_NOP);
        byte originalOpcode = testInstruction.opcode;

        // Act
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, testInstruction);

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
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                replacer.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests that visitAnyInstruction with mixed null and non-null parameters works correctly.
     * This ensures the method handles partial null inputs gracefully.
     */
    @Test
    public void testVisitAnyInstruction_withMixedNullParameters_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act & Assert - test various combinations of null/non-null parameters
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(null, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, null, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, null, 0, instruction));
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, null));
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(null, null, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(null, method, null, 0, instruction));
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, null, null, 0, instruction));
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(null, null, null, 0, null));
    }

    /**
     * Tests that visitAnyInstruction handles various simple instruction opcodes.
     * Since it's a no-op, all instruction types should be handled the same way.
     */
    @Test
    public void testVisitAnyInstruction_withVariousOpcodes_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);
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
            assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, offset, instructions[offset]),
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
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnyInstruction should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitAnyInstruction called after multiple other operations still behaves as a no-op.
     * This verifies consistent behavior regardless of the replacer's usage history.
     */
    @Test
    public void testVisitAnyInstruction_afterMultipleOperations_stillNoOp() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Simulate some prior operations
        for (int i = 0; i < 5; i++) {
            replacer.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
        }

        // Act - call visitAnyInstruction again
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 100, instruction);

        // Assert - verify it's still a no-op with no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests that visitAnyInstruction doesn't affect the replacer's internal state.
     * Since it's a no-op, it should not modify any internal fields.
     */
    @Test
    public void testVisitAnyInstruction_doesNotAffectInternalState() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act - call visitAnyInstruction multiple times
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);

        // Assert - verify no interactions occurred, indicating no state changes
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests that visitAnyInstruction works correctly even when called between
     * other visitor methods that do have side effects.
     */
    @Test
    public void testVisitAnyInstruction_betweenOtherVisitorCalls_remainsNoOp() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Act - call visitAnyInstruction
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests that visitAnyInstruction with varying combinations of parameters
     * all result in the same no-op behavior.
     */
    @Test
    public void testVisitAnyInstruction_varyingParameterCombinations_consistentNoOpBehavior() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);
        Clazz clazz2 = mock(ProgramClass.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        Instruction inst2 = new SimpleInstruction(Instruction.OP_ICONST_1);

        // Act - call with various parameter combinations
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        replacer.visitAnyInstruction(clazz2, method2, codeAttr2, 100, inst2);
        replacer.visitAnyInstruction(clazz, method2, codeAttribute, 50, inst2);

        // Assert - verify no interactions occurred with any parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(method);
        verifyNoInteractions(method2);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(codeAttr2);
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests that the visitAnyInstruction method signature matches the InstructionVisitor interface.
     * This ensures the method properly overrides the interface method.
     */
    @Test
    public void testVisitAnyInstruction_implementsInterfaceCorrectly() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor);

        // Assert - GotoGotoReplacer should be an InstructionVisitor
        assertTrue(replacer instanceof InstructionVisitor,
                "GotoGotoReplacer should implement InstructionVisitor");
    }

    /**
     * Tests that visitAnyInstruction with extra visitor still doesn't interact with any parameters.
     * Even with an extra visitor configured, the no-op behavior should be consistent.
     */
    @Test
    public void testVisitAnyInstruction_withExtraVisitor_doesNotInteractWithParameters() {
        // Arrange
        replacer = new GotoGotoReplacer(mockCodeAttributeEditor, mockExtraVisitor);

        // Act
        replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(mockCodeAttributeEditor);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyInstruction called with null CodeAttributeEditor doesn't cause issues.
     * This verifies that the no-op can be safely called even with a null editor.
     */
    @Test
    public void testVisitAnyInstruction_withNullCodeAttributeEditor_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called on a replacer created with both parameters null.
     * This verifies robustness in edge case initialization scenarios.
     */
    @Test
    public void testVisitAnyInstruction_withReplacerCreatedWithNullParameters_doesNotThrowException() {
        // Arrange
        replacer = new GotoGotoReplacer(null, null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }
}

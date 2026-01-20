package proguard.optimize;

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
 * Test class for {@link MemberReferenceGeneralizer#visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)}.
 *
 * The visitAnyInstruction method is an empty implementation (no-op) that serves as a default
 * handler in the InstructionVisitor pattern. The MemberReferenceGeneralizer only processes
 * specific instruction types through visitConstantInstruction, and this method provides the
 * default no-op behavior for all other instruction types.
 */
public class MemberReferenceGeneralizerClaude_visitAnyInstructionTest {

    private MemberReferenceGeneralizer generalizer;
    private CodeAttributeEditor codeAttributeEditor;
    private InstructionVisitor extraFieldInstructionVisitor;
    private InstructionVisitor extraMethodInstructionVisitor;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;
    private Instruction instruction;

    @BeforeEach
    public void setUp() {
        codeAttributeEditor = mock(CodeAttributeEditor.class);
        extraFieldInstructionVisitor = mock(InstructionVisitor.class);
        extraMethodInstructionVisitor = mock(InstructionVisitor.class);
        generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            extraFieldInstructionVisitor,
            extraMethodInstructionVisitor
        );
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
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(null, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null Method parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullMethod_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, null, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null CodeAttribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullCodeAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, method, null, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction can be called with null Instruction parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withNullInstruction_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, null));
    }

    /**
     * Tests that visitAnyInstruction can be called with all parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyInstruction_withAllParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(null, null, null, 0, null));
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
            assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, method, codeAttribute, offset, instruction),
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
            generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
            generalizer.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);
            generalizer.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);
        });
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithClazz() {
        // Act
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

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
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

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
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred with the code attribute mock
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with the CodeAttributeEditor.
     * Since it's a no-op method, it should not trigger any code editing operations.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithCodeAttributeEditor() {
        // Act
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions occurred with the code attribute editor
        verifyNoInteractions(codeAttributeEditor);
    }

    /**
     * Tests that visitAnyInstruction doesn't affect the generalizer's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyInstruction_doesNotModifyGeneralizerState() {
        // Act - call visitAnyInstruction
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - since visitAnyInstruction is a no-op, no visitors should be invoked
        verifyNoInteractions(extraFieldInstructionVisitor);
        verifyNoInteractions(extraMethodInstructionVisitor);
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
            generalizer.visitAnyInstruction(clazz1, method, codeAttribute, 0, instruction);
            generalizer.visitAnyInstruction(clazz2, method, codeAttribute, 0, instruction);
            generalizer.visitAnyInstruction(clazz3, method, codeAttribute, 0, instruction);
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
            generalizer.visitAnyInstruction(clazz, method1, codeAttribute, 0, instruction);
            generalizer.visitAnyInstruction(clazz, method2, codeAttribute, 0, instruction);
            generalizer.visitAnyInstruction(clazz, method3, codeAttribute, 0, instruction);
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
            generalizer.visitAnyInstruction(clazz, method, codeAttr1, 0, instruction);
            generalizer.visitAnyInstruction(clazz, method, codeAttr2, 0, instruction);
            generalizer.visitAnyInstruction(clazz, method, codeAttr3, 0, instruction);
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
            generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, inst1);
            generalizer.visitAnyInstruction(clazz, method, codeAttribute, 1, inst2);
            generalizer.visitAnyInstruction(clazz, method, codeAttribute, 2, inst3);
        });
    }

    /**
     * Tests that visitAnyInstruction doesn't trigger the extra field instruction visitor.
     * Since it's a no-op method, it should not affect the visitor.
     */
    @Test
    public void testVisitAnyInstruction_doesNotTriggerExtraFieldVisitor() {
        // Act
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify the extra field visitor was not invoked
        verifyNoInteractions(extraFieldInstructionVisitor);
    }

    /**
     * Tests that visitAnyInstruction doesn't trigger the extra method instruction visitor.
     * Since it's a no-op method, it should not affect the visitor.
     */
    @Test
    public void testVisitAnyInstruction_doesNotTriggerExtraMethodVisitor() {
        // Act
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify the extra method visitor was not invoked
        verifyNoInteractions(extraMethodInstructionVisitor);
    }

    /**
     * Tests that visitAnyInstruction can be called on a generalizer with null extra visitors.
     * The method should work even if the optional visitors are null.
     */
    @Test
    public void testVisitAnyInstruction_withNullExtraVisitors_doesNotThrowException() {
        // Arrange - create generalizer with null extra visitors
        MemberReferenceGeneralizer generalizerWithNullVisitors = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor,
            null,
            null
        );

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> generalizerWithNullVisitors.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction works regardless of the field generalization flag.
     * Since it's a no-op, the flag value should not matter.
     */
    @Test
    public void testVisitAnyInstruction_withFieldGeneralizationDisabled_doesNotThrowException() {
        // Arrange - create generalizer with field generalization disabled
        MemberReferenceGeneralizer generalizerFieldDisabled = new MemberReferenceGeneralizer(
            false,
            true,
            codeAttributeEditor,
            extraFieldInstructionVisitor,
            extraMethodInstructionVisitor
        );

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> generalizerFieldDisabled.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction works regardless of the method generalization flag.
     * Since it's a no-op, the flag value should not matter.
     */
    @Test
    public void testVisitAnyInstruction_withMethodGeneralizationDisabled_doesNotThrowException() {
        // Arrange - create generalizer with method generalization disabled
        MemberReferenceGeneralizer generalizerMethodDisabled = new MemberReferenceGeneralizer(
            true,
            false,
            codeAttributeEditor,
            extraFieldInstructionVisitor,
            extraMethodInstructionVisitor
        );

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> generalizerMethodDisabled.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction works with both generalization flags disabled.
     * Since it's a no-op, the flag values should not matter.
     */
    @Test
    public void testVisitAnyInstruction_withBothGeneralizationFlagsDisabled_doesNotThrowException() {
        // Arrange - create generalizer with both flags disabled
        MemberReferenceGeneralizer generalizerAllDisabled = new MemberReferenceGeneralizer(
            false,
            false,
            codeAttributeEditor,
            extraFieldInstructionVisitor,
            extraMethodInstructionVisitor
        );

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> generalizerAllDisabled.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
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
            generalizer.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyInstruction should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyInstruction doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the generalizer's normal operation.
     */
    @Test
    public void testVisitAnyInstruction_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyInstruction first
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Then call visitAnyInstruction again
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraFieldInstructionVisitor);
        verifyNoInteractions(extraMethodInstructionVisitor);
    }

    /**
     * Tests that visitAnyInstruction can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyInstruction_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraFieldInstructionVisitor);
        verifyNoInteractions(extraMethodInstructionVisitor);
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
                    generalizer.visitAnyInstruction(clazz, method, codeAttribute, offset, instruction);
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
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraFieldInstructionVisitor);
        verifyNoInteractions(extraMethodInstructionVisitor);
    }

    /**
     * Tests that multiple generalizers can call visitAnyInstruction independently.
     * Each generalizer's no-op should not affect others.
     */
    @Test
    public void testVisitAnyInstruction_withMultipleGeneralizers_operateIndependently() {
        // Arrange - create multiple generalizers
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        InstructionVisitor visitor2 = mock(InstructionVisitor.class);
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);

        MemberReferenceGeneralizer generalizer1 = new MemberReferenceGeneralizer(
            true, true, editor1, visitor1, null
        );
        MemberReferenceGeneralizer generalizer2 = new MemberReferenceGeneralizer(
            false, false, editor2, null, visitor2
        );

        // Act - call visitAnyInstruction on both generalizers
        generalizer1.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        generalizer2.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);

        // Assert - verify no interactions occurred on any visitor or editor
        verifyNoInteractions(visitor1);
        verifyNoInteractions(visitor2);
        verifyNoInteractions(editor1);
        verifyNoInteractions(editor2);
    }

    /**
     * Tests that visitAnyInstruction works correctly with boundary offset values.
     * The method should handle edge cases gracefully.
     */
    @Test
    public void testVisitAnyInstruction_withBoundaryOffsets_doesNotThrowException() {
        // Act & Assert - test with boundary values
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
                "Should handle offset 0");
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, instruction),
                "Should handle Integer.MAX_VALUE offset");
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, method, codeAttribute, Integer.MIN_VALUE, instruction),
                "Should handle Integer.MIN_VALUE offset");
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, method, codeAttribute, -1, instruction),
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
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, testInstruction);

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
                generalizer.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraFieldInstructionVisitor);
        verifyNoInteractions(extraMethodInstructionVisitor);
    }

    /**
     * Tests that visitAnyInstruction with mixed null and non-null parameters works correctly.
     * This ensures the method handles partial null inputs gracefully.
     */
    @Test
    public void testVisitAnyInstruction_withMixedNullParameters_doesNotThrowException() {
        // Act & Assert - test various combinations of null/non-null parameters
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(null, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, null, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, method, null, 0, instruction));
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, null));
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(null, null, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(null, method, null, 0, instruction));
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, null, null, 0, instruction));
        assertDoesNotThrow(() -> generalizer.visitAnyInstruction(null, null, null, 0, null));
    }

    /**
     * Tests that visitAnyInstruction doesn't affect the generalization of constant instructions.
     * Calling visitAnyInstruction should not interfere with the generalizer's primary functionality.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInterfereWithConstantInstructionGeneralization() {
        // Act - call visitAnyInstruction multiple times
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 1, instruction);
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

        // Assert - verify the code attribute editor was never touched
        // This confirms visitAnyInstruction doesn't interfere with actual generalization
        verifyNoInteractions(codeAttributeEditor);
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
            assertDoesNotThrow(() -> generalizer.visitAnyInstruction(clazz, method, codeAttribute, offset, instructions[offset]),
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
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnyInstruction should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitAnyInstruction called after multiple other operations still behaves as a no-op.
     * This verifies consistent behavior regardless of the generalizer's usage history.
     */
    @Test
    public void testVisitAnyInstruction_afterMultipleOperations_stillNoOp() {
        // Arrange - simulate some prior operations (though they won't do anything significant without proper setup)
        for (int i = 0; i < 5; i++) {
            generalizer.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
        }

        // Act - call visitAnyInstruction again
        generalizer.visitAnyInstruction(clazz, method, codeAttribute, 100, instruction);

        // Assert - verify it's still a no-op with no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(codeAttributeEditor);
    }
}

package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SideEffectInstructionChecker#visitAnyInstruction}.
 *
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/Method;Lproguard/classfile/attribute/CodeAttribute;ILproguard/classfile/instruction/Instruction;)V
 *
 * The visitAnyInstruction method is an empty default implementation from the InstructionVisitor interface.
 * It does nothing and serves as a fallback for instruction types that don't need special handling.
 * The actual side effect checking logic is in the more specific visitor methods like visitSimpleInstruction.
 */
public class SideEffectInstructionCheckerClaude_visitAnyInstructionTest {

    /**
     * Tests that visitAnyInstruction executes without throwing exceptions.
     * The method is empty and should do nothing.
     */
    @Test
    public void testVisitAnyInstruction_withSimpleInstruction_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
                "visitAnyInstruction should not throw any exception");
    }

    /**
     * Tests that visitAnyInstruction can be called with various instruction types.
     */
    @Test
    public void testVisitAnyInstruction_withVariousInstructionTypes_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test with different instruction types
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IADD)), "SimpleInstruction should not throw");
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0,
                new VariableInstruction(Instruction.OP_ILOAD, 1)), "VariableInstruction should not throw");
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0,
                new ConstantInstruction(Instruction.OP_LDC, 1)), "ConstantInstruction should not throw");
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0,
                new BranchInstruction(Instruction.OP_GOTO, 10)), "BranchInstruction should not throw");
    }

    /**
     * Tests that visitAnyInstruction works with all three boolean flags enabled.
     */
    @Test
    public void testVisitAnyInstruction_withAllFlagsEnabled_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
                "visitAnyInstruction should work with all flags enabled");
    }

    /**
     * Tests that visitAnyInstruction works with all three boolean flags disabled.
     */
    @Test
    public void testVisitAnyInstruction_withAllFlagsDisabled_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
                "visitAnyInstruction should work with all flags disabled");
    }

    /**
     * Tests that visitAnyInstruction can be called multiple times without issue.
     */
    @Test
    public void testVisitAnyInstruction_calledMultipleTimes_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
            checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
            checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        }, "Multiple calls to visitAnyInstruction should not throw");
    }

    /**
     * Tests that visitAnyInstruction works with various offset values.
     */
    @Test
    public void testVisitAnyInstruction_withVariousOffsets_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
                "Offset 0 should work");
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 100, instruction),
                "Offset 100 should work");
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, -1, instruction),
                "Negative offset should work");
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, instruction),
                "MAX_VALUE offset should work");
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, Integer.MIN_VALUE, instruction),
                "MIN_VALUE offset should work");
    }

    /**
     * Tests that visitAnyInstruction works with different checker instances.
     */
    @Test
    public void testVisitAnyInstruction_withDifferentCheckerInstances_doesNotThrow() {
        // Arrange
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert - test with different flag combinations
        assertDoesNotThrow(() -> new SideEffectInstructionChecker(true, false, false)
                .visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> new SideEffectInstructionChecker(false, true, false)
                .visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> new SideEffectInstructionChecker(false, false, true)
                .visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> new SideEffectInstructionChecker(true, true, true)
                .visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction does not interact with the clazz parameter.
     * Since the method is empty, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithClazz() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify no interactions with any parameter
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyInstruction does not interact with the method parameter.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithMethod() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(method);
    }

    /**
     * Tests that visitAnyInstruction does not interact with the codeAttribute parameter.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithCodeAttribute() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction does not modify the instruction.
     */
    @Test
    public void testVisitAnyInstruction_doesNotModifyInstruction() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IADD);
        byte originalOpcode = instruction.opcode;

        // Act
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertEquals(originalOpcode, instruction.opcode, "Instruction opcode should not be modified");
    }

    /**
     * Tests that visitAnyInstruction is idempotent - calling it multiple times has no cumulative effect.
     */
    @Test
    public void testVisitAnyInstruction_isIdempotent() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act - call multiple times
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - still no interactions with any parameter
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction completes quickly as it's an empty method.
     */
    @Test
    public void testVisitAnyInstruction_completesQuickly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 10000; i++) {
            checker.visitAnyInstruction(clazz, method, codeAttribute, i, instruction);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 50ms for 10000 calls)
        assertTrue(durationMs < 50, "visitAnyInstruction should execute extremely quickly as it's empty");
    }

    /**
     * Tests that visitAnyInstruction is thread-safe when called concurrently.
     * Since the method is empty and modifies no state, it should be inherently thread-safe.
     */
    @Test
    public void testVisitAnyInstruction_isThreadSafe() throws InterruptedException {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        int threadCount = 10;
        int callsPerThread = 100;
        Thread[] threads = new Thread[threadCount];

        // Act - call from multiple threads
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < callsPerThread; j++) {
                    checker.visitAnyInstruction(clazz, method, codeAttribute, j, instruction);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - no exceptions thrown, method completed successfully
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction works with various instruction opcodes.
     */
    @Test
    public void testVisitAnyInstruction_withVariousOpcodes_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test with various opcodes
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_NOP)));
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IADD)));
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_ATHROW)));
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IRETURN)));
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_MONITORENTER)));
    }

    /**
     * Tests that calling visitAnyInstruction doesn't affect subsequent hasSideEffects calls.
     * The empty visitAnyInstruction should have no side effects on the checker's state.
     */
    @Test
    public void testVisitAnyInstruction_doesNotAffectHasSideEffectsMethod() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        SimpleInstruction nopInstruction = new SimpleInstruction(Instruction.OP_NOP);
        SimpleInstruction throwInstruction = new SimpleInstruction(Instruction.OP_ATHROW);

        // Act - call visitAnyInstruction
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, nopInstruction);

        // Assert - hasSideEffects should still work correctly
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, nopInstruction),
                "NOP should not have side effects");
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, throwInstruction),
                "ATHROW should have side effects");
    }

    /**
     * Tests visitAnyInstruction with ProgramClass instance.
     */
    @Test
    public void testVisitAnyInstruction_withProgramClass_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = new ProgramClass();
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
                "visitAnyInstruction should work with real ProgramClass");
    }

    /**
     * Tests visitAnyInstruction with LibraryClass instance.
     */
    @Test
    public void testVisitAnyInstruction_withLibraryClass_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz = new LibraryClass();
        Method method = mock(LibraryMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction),
                "visitAnyInstruction should work with LibraryClass");
    }

    /**
     * Tests that visitAnyInstruction maintains the empty behavior regardless of previous method calls.
     */
    @Test
    public void testVisitAnyInstruction_afterOtherVisitorCalls_remainsEmpty() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        Clazz clazz = mock(ProgramClass.class);
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);

        // Act - call hasSideEffects first (which calls other visitor methods)
        checker.hasSideEffects(clazz, method, codeAttribute, 0, new SimpleInstruction(Instruction.OP_ATHROW));

        // Reset the mock to clear any previous interactions
        reset(clazz, method, codeAttribute);

        // Now call visitAnyInstruction
        checker.visitAnyInstruction(clazz, method, codeAttribute, 0, new SimpleInstruction(Instruction.OP_NOP));

        // Assert - still no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }
}

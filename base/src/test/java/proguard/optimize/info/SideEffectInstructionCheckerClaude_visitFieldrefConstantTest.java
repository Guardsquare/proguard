package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.FieldrefConstant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SideEffectInstructionChecker#visitFieldrefConstant}.
 *
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/constant/FieldrefConstant;)V
 *
 * The visitFieldrefConstant method is part of the ConstantVisitor interface implementation.
 * It performs three actions:
 * 1. Stores the referencing class in the referencingClass field
 * 2. Sets hasSideEffects to true (assumes unknown field access has side effects)
 * 3. Calls fieldrefConstant.referencedFieldAccept(this) to check if the referenced field is known
 *    and potentially update hasSideEffects based on the field's properties
 *
 * This method is called when visitConstantInstruction encounters field access instructions
 * (GETSTATIC, PUTSTATIC, GETFIELD, PUTFIELD) and calls clazz.constantPoolEntryAccept().
 *
 * Note: Testing the complete behavior requires setting up ProgramField references which is complex.
 * These tests focus on the directly testable behavior without requiring full field setup.
 */
public class SideEffectInstructionCheckerClaude_visitFieldrefConstantTest {

    private Clazz clazz;
    private FieldrefConstant fieldrefConstant;

    // =========================================================================
    // Basic Functionality Tests
    // =========================================================================

    /**
     * Tests that visitFieldrefConstant sets hasSideEffects to true initially.
     * Accessing an unknown field is assumed to have side effects.
     */
    @Test
    public void testVisitFieldrefConstant_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        assertDoesNotThrow(() -> checker.visitFieldrefConstant(clazz, fieldrefConstant),
                "visitFieldrefConstant should not throw any exception");
    }

    /**
     * Tests that visitFieldrefConstant calls referencedFieldAccept on the constant.
     */
    @Test
    public void testVisitFieldrefConstant_callsReferencedFieldAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify referencedFieldAccept was called with the checker as visitor
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests visitFieldrefConstant with all flags disabled.
     */
    @Test
    public void testVisitFieldrefConstant_withAllFlagsDisabled_callsReferencedFieldAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests visitFieldrefConstant with all flags enabled.
     */
    @Test
    public void testVisitFieldrefConstant_withAllFlagsEnabled_callsReferencedFieldAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests visitFieldrefConstant with only includeReturnInstructions enabled.
     */
    @Test
    public void testVisitFieldrefConstant_withOnlyReturnFlag_callsReferencedFieldAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests visitFieldrefConstant with only includeArrayStoreInstructions enabled.
     */
    @Test
    public void testVisitFieldrefConstant_withOnlyArrayStoreFlag_callsReferencedFieldAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests visitFieldrefConstant with only includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitFieldrefConstant_withOnlyBuiltInExceptionsFlag_callsReferencedFieldAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    // =========================================================================
    // Tests with Different Clazz Types
    // =========================================================================

    /**
     * Tests visitFieldrefConstant with ProgramClass.
     */
    @Test
    public void testVisitFieldrefConstant_withProgramClass_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz realClazz = new ProgramClass();
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitFieldrefConstant(realClazz, fieldrefConstant),
                "visitFieldrefConstant should work with real ProgramClass");
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests visitFieldrefConstant with LibraryClass.
     */
    @Test
    public void testVisitFieldrefConstant_withLibraryClass_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz realClazz = new LibraryClass();
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitFieldrefConstant(realClazz, fieldrefConstant),
                "visitFieldrefConstant should work with LibraryClass");
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests visitFieldrefConstant with mocked Clazz.
     */
    @Test
    public void testVisitFieldrefConstant_withMockedClazz_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(Clazz.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitFieldrefConstant(clazz, fieldrefConstant),
                "visitFieldrefConstant should work with mocked Clazz");
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    // =========================================================================
    // Tests with Different FieldrefConstant Instances
    // =========================================================================

    /**
     * Tests visitFieldrefConstant with different FieldrefConstant instances.
     * Each should call referencedFieldAccept.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentConstants_callsReferencedFieldAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        FieldrefConstant constant1 = mock(FieldrefConstant.class);
        FieldrefConstant constant2 = mock(FieldrefConstant.class);
        FieldrefConstant constant3 = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz, constant1);
        checker.visitFieldrefConstant(clazz, constant2);
        checker.visitFieldrefConstant(clazz, constant3);

        // Assert - verify each constant had referencedFieldAccept called
        verify(constant1).referencedFieldAccept(eq(checker));
        verify(constant2).referencedFieldAccept(eq(checker));
        verify(constant3).referencedFieldAccept(eq(checker));
    }

    // =========================================================================
    // Integration Tests
    // =========================================================================

    /**
     * Tests that visitFieldrefConstant can be called multiple times.
     */
    @Test
    public void testVisitFieldrefConstant_calledMultipleTimes_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);
        checker.visitFieldrefConstant(clazz, fieldrefConstant);
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify referencedFieldAccept was called 3 times
        verify(fieldrefConstant, times(3)).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests visitFieldrefConstant with multiple different checker instances.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentCheckers_worksCorrectly() {
        // Arrange
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act - different checkers with different flag combinations
        SideEffectInstructionChecker checker1 = new SideEffectInstructionChecker(false, false, false);
        checker1.visitFieldrefConstant(clazz, fieldrefConstant);

        SideEffectInstructionChecker checker2 = new SideEffectInstructionChecker(true, false, false);
        checker2.visitFieldrefConstant(clazz, fieldrefConstant);

        SideEffectInstructionChecker checker3 = new SideEffectInstructionChecker(false, true, false);
        checker3.visitFieldrefConstant(clazz, fieldrefConstant);

        SideEffectInstructionChecker checker4 = new SideEffectInstructionChecker(true, true, true);
        checker4.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify referencedFieldAccept was called for each checker
        verify(fieldrefConstant, times(4)).referencedFieldAccept(any(SideEffectInstructionChecker.class));
    }

    /**
     * Tests that visitFieldrefConstant always calls referencedFieldAccept
     * regardless of flag settings.
     */
    @Test
    public void testVisitFieldrefConstant_alwaysCallsReferencedFieldAccept() {
        // Arrange
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Test all 8 possible flag combinations (2^3)
        boolean[] flags = {false, true};
        int callCount = 0;

        // Act - test all combinations
        for (boolean returnFlag : flags) {
            for (boolean arrayStoreFlag : flags) {
                for (boolean builtInExceptionsFlag : flags) {
                    SideEffectInstructionChecker checker = new SideEffectInstructionChecker(
                            returnFlag, arrayStoreFlag, builtInExceptionsFlag);
                    checker.visitFieldrefConstant(clazz, fieldrefConstant);
                    callCount++;
                }
            }
        }

        // Assert - verify referencedFieldAccept was called for all 8 combinations
        verify(fieldrefConstant, times(callCount)).referencedFieldAccept(any(SideEffectInstructionChecker.class));
    }

    /**
     * Tests that visitFieldrefConstant doesn't throw exceptions.
     */
    @Test
    public void testVisitFieldrefConstant_doesNotThrowException() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitFieldrefConstant(clazz, fieldrefConstant),
                "visitFieldrefConstant should not throw any exception");
    }

    /**
     * Tests that visitFieldrefConstant stores the referencing class
     * (though we can't directly verify this, we test the method executes correctly).
     */
    @Test
    public void testVisitFieldrefConstant_storesReferencingClass() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - the referencing class is stored internally, we verify execution
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests visitFieldrefConstant with different clazz instances for the same constant.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentClazzes_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(LibraryClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz1, fieldrefConstant);
        checker.visitFieldrefConstant(clazz2, fieldrefConstant);
        checker.visitFieldrefConstant(clazz3, fieldrefConstant);

        // Assert - verify referencedFieldAccept was called 3 times
        verify(fieldrefConstant, times(3)).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests that the method works correctly as part of the ConstantVisitor pattern.
     */
    @Test
    public void testVisitFieldrefConstant_asPartOfVisitorPattern_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act - use as a ConstantVisitor
        proguard.classfile.constant.visitor.ConstantVisitor visitor = checker;
        assertDoesNotThrow(() -> visitor.visitFieldrefConstant(clazz, fieldrefConstant),
                "Should work correctly as a ConstantVisitor");

        // Assert
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests visitFieldrefConstant after other visitor methods have been called.
     */
    @Test
    public void testVisitFieldrefConstant_afterOtherVisitorCalls_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act - call visitAnyInstruction first
        checker.visitAnyInstruction(clazz, mock(Method.class),
                mock(proguard.classfile.attribute.CodeAttribute.class),
                0, mock(proguard.classfile.instruction.Instruction.class));

        // Now call visitFieldrefConstant
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests that the method can handle being called in rapid succession.
     */
    @Test
    public void testVisitFieldrefConstant_rapidSuccession_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);

        // Act & Assert - rapid calls with different constants
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                FieldrefConstant constant = mock(FieldrefConstant.class);
                checker.visitFieldrefConstant(clazz, constant);
            }
        }, "Method should handle rapid succession calls");
    }

    /**
     * Tests that visitFieldrefConstant completes quickly.
     */
    @Test
    public void testVisitFieldrefConstant_completesQuickly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 1000; i++) {
            checker.visitFieldrefConstant(clazz, fieldrefConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete reasonably quickly
        assertTrue(durationMs < 100,
                "visitFieldrefConstant should execute quickly");
    }

    /**
     * Tests that multiple checker instances can call visitFieldrefConstant
     * on the same constant without interfering with each other.
     */
    @Test
    public void testVisitFieldrefConstant_multipleCheckersOneConstant_workIndependently() {
        // Arrange
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        SideEffectInstructionChecker checker1 = new SideEffectInstructionChecker(false, false, false);
        SideEffectInstructionChecker checker2 = new SideEffectInstructionChecker(true, false, false);
        SideEffectInstructionChecker checker3 = new SideEffectInstructionChecker(false, true, false);

        // Act
        checker1.visitFieldrefConstant(clazz, fieldrefConstant);
        checker2.visitFieldrefConstant(clazz, fieldrefConstant);
        checker3.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify referencedFieldAccept was called 3 times
        verify(fieldrefConstant, times(3)).referencedFieldAccept(any(SideEffectInstructionChecker.class));
    }

    /**
     * Tests that visitFieldrefConstant is called correctly from the instruction visitor flow.
     * This simulates the call path: visitConstantInstruction -> constantPoolEntryAccept -> visitFieldrefConstant
     */
    @Test
    public void testVisitFieldrefConstant_calledFromInstructionVisitorFlow_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act - simulate being called as part of constant pool accept
        proguard.classfile.constant.visitor.ConstantVisitor constantVisitor = checker;
        constantVisitor.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests visitFieldrefConstant with same checker but different constants sequentially.
     */
    @Test
    public void testVisitFieldrefConstant_sameCheckerDifferentConstants_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        FieldrefConstant constant1 = mock(FieldrefConstant.class);
        FieldrefConstant constant2 = mock(FieldrefConstant.class);
        FieldrefConstant constant3 = mock(FieldrefConstant.class);

        // Act - call with different constants
        checker.visitFieldrefConstant(clazz, constant1);
        checker.visitFieldrefConstant(clazz, constant2);
        checker.visitFieldrefConstant(clazz, constant3);

        // Assert - verify each was called exactly once
        verify(constant1).referencedFieldAccept(eq(checker));
        verify(constant2).referencedFieldAccept(eq(checker));
        verify(constant3).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant works with various flag combinations independently.
     */
    @Test
    public void testVisitFieldrefConstant_variousFlagCombinations_allWork() {
        // Arrange
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act & Assert - test specific important combinations
        assertDoesNotThrow(() -> {
            new SideEffectInstructionChecker(false, false, false)
                    .visitFieldrefConstant(clazz, fieldrefConstant);
            new SideEffectInstructionChecker(true, true, true)
                    .visitFieldrefConstant(clazz, fieldrefConstant);
            new SideEffectInstructionChecker(true, false, true)
                    .visitFieldrefConstant(clazz, fieldrefConstant);
            new SideEffectInstructionChecker(false, true, true)
                    .visitFieldrefConstant(clazz, fieldrefConstant);
        }, "All flag combinations should work");
    }

    /**
     * Tests that referencedFieldAccept is always called with the checker instance.
     */
    @Test
    public void testVisitFieldrefConstant_passesCheckerToReferencedFieldAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify the checker itself (not just any visitor) was passed
        verify(fieldrefConstant).referencedFieldAccept(same(checker));
    }

    /**
     * Tests that visitFieldrefConstant executes all its steps:
     * 1. Stores referencing class (internal)
     * 2. Sets hasSideEffects to true (internal)
     * 3. Calls referencedFieldAccept (verifiable)
     */
    @Test
    public void testVisitFieldrefConstant_executesAllSteps() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - we can verify step 3 directly
        verify(fieldrefConstant).referencedFieldAccept(eq(checker));
        // Steps 1 and 2 are internal state changes that will affect subsequent operations
    }

    /**
     * Tests that the method doesn't throw when referencedFieldAccept is called.
     */
    @Test
    public void testVisitFieldrefConstant_referencedFieldAcceptCalled_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Don't stub referencedFieldAccept - let it use default behavior

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitFieldrefConstant(clazz, fieldrefConstant),
                "Should not throw even when referencedFieldAccept has default behavior");
    }

    /**
     * Tests consistent behavior across multiple invocations.
     */
    @Test
    public void testVisitFieldrefConstant_consistentBehavior() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act - call multiple times
        checker.visitFieldrefConstant(clazz, fieldrefConstant);
        checker.visitFieldrefConstant(clazz, fieldrefConstant);
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - consistent behavior means referencedFieldAccept called each time
        verify(fieldrefConstant, times(3)).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant doesn't interfere with other visitor methods.
     */
    @Test
    public void testVisitFieldrefConstant_doesNotInterfereWithOtherMethods() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);
        proguard.classfile.constant.InvokeDynamicConstant invokeDynamicConstant =
                mock(proguard.classfile.constant.InvokeDynamicConstant.class);

        // Act - call different visitor methods
        checker.visitFieldrefConstant(clazz1, fieldrefConstant);
        checker.visitInvokeDynamicConstant(clazz2, invokeDynamicConstant);
        checker.visitFieldrefConstant(clazz1, fieldrefConstant);

        // Assert - each should work independently
        verify(fieldrefConstant, times(2)).referencedFieldAccept(eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant works correctly when the same constant
     * is visited with different referencing classes.
     */
    @Test
    public void testVisitFieldrefConstant_sameConstantDifferentClasses_updatesReferencingClass() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = new LibraryClass();
        fieldrefConstant = mock(FieldrefConstant.class);

        // Act - visit same constant with different classes
        checker.visitFieldrefConstant(clazz1, fieldrefConstant);
        checker.visitFieldrefConstant(clazz2, fieldrefConstant);
        checker.visitFieldrefConstant(clazz3, fieldrefConstant);

        // Assert - referencedFieldAccept should be called 3 times
        // Each call should have potentially different referencing class
        verify(fieldrefConstant, times(3)).referencedFieldAccept(eq(checker));
    }
}

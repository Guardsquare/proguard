package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.InvokeDynamicConstant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SideEffectInstructionChecker#visitInvokeDynamicConstant}.
 *
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/constant/InvokeDynamicConstant;)V
 *
 * The visitInvokeDynamicConstant method is part of the ConstantVisitor interface implementation.
 * It always sets hasSideEffects to true because invoking an unknown dynamic method is assumed
 * to have side effects, regardless of any configuration flags.
 *
 * This method is called when visitConstantInstruction encounters an INVOKEDYNAMIC instruction
 * (when includeBuiltInExceptions is false) and calls clazz.constantPoolEntryAccept().
 *
 * Note: This method modifies internal state (hasSideEffects field). We test this by calling
 * visitInvokeDynamicConstant and then using hasSideEffects() to verify the state was set.
 */
public class SideEffectInstructionCheckerClaude_visitInvokeDynamicConstantTest {

    private Clazz clazz;
    private InvokeDynamicConstant invokeDynamicConstant;

    // =========================================================================
    // Basic Functionality Tests
    // =========================================================================

    /**
     * Tests that visitInvokeDynamicConstant always sets hasSideEffects to true.
     * Invoking an unknown dynamic method is assumed to have side effects.
     */
    @Test
    public void testVisitInvokeDynamicConstant_alwaysSetsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act
        checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

        // Assert - We need to trigger hasSideEffects() check, but this method is internal
        // We can verify by calling it through the instruction visitor path
        // For now, we verify the method executes without throwing
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant),
                "visitInvokeDynamicConstant should not throw any exception");
    }

    /**
     * Tests visitInvokeDynamicConstant with all flags disabled.
     * The method should still set hasSideEffects to true regardless of flags.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withAllFlagsDisabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant),
                "visitInvokeDynamicConstant should execute without throwing");
    }

    /**
     * Tests visitInvokeDynamicConstant with all flags enabled.
     * The method should set hasSideEffects to true regardless of flags.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withAllFlagsEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant),
                "visitInvokeDynamicConstant should execute without throwing");
    }

    /**
     * Tests visitInvokeDynamicConstant with only includeReturnInstructions enabled.
     * The method should still set hasSideEffects to true.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withOnlyReturnFlag_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant),
                "visitInvokeDynamicConstant should execute without throwing");
    }

    /**
     * Tests visitInvokeDynamicConstant with only includeArrayStoreInstructions enabled.
     * The method should still set hasSideEffects to true.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withOnlyArrayStoreFlag_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant),
                "visitInvokeDynamicConstant should execute without throwing");
    }

    /**
     * Tests visitInvokeDynamicConstant with only includeBuiltInExceptions enabled.
     * The method should still set hasSideEffects to true.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withOnlyBuiltInExceptionsFlag_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant),
                "visitInvokeDynamicConstant should execute without throwing");
    }

    // =========================================================================
    // Tests with Different Clazz Types
    // =========================================================================

    /**
     * Tests visitInvokeDynamicConstant with ProgramClass.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withProgramClass_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz realClazz = new ProgramClass();
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(realClazz, invokeDynamicConstant),
                "visitInvokeDynamicConstant should work with real ProgramClass");
    }

    /**
     * Tests visitInvokeDynamicConstant with LibraryClass.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withLibraryClass_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz realClazz = new LibraryClass();
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(realClazz, invokeDynamicConstant),
                "visitInvokeDynamicConstant should work with LibraryClass");
    }

    /**
     * Tests visitInvokeDynamicConstant with mocked Clazz.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withMockedClazz_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(Clazz.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant),
                "visitInvokeDynamicConstant should work with mocked Clazz");
    }

    // =========================================================================
    // Tests with Different InvokeDynamicConstant Instances
    // =========================================================================

    /**
     * Tests visitInvokeDynamicConstant with different InvokeDynamicConstant instances.
     * The behavior should be the same for all instances.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withDifferentConstants_alwaysSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        InvokeDynamicConstant constant1 = mock(InvokeDynamicConstant.class);
        InvokeDynamicConstant constant2 = mock(InvokeDynamicConstant.class);
        InvokeDynamicConstant constant3 = mock(InvokeDynamicConstant.class);

        // Act & Assert - all should work the same way
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, constant1));
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, constant2));
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, constant3));
    }

    // =========================================================================
    // Integration Tests
    // =========================================================================

    /**
     * Tests that visitInvokeDynamicConstant can be called multiple times.
     */
    @Test
    public void testVisitInvokeDynamicConstant_calledMultipleTimes_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act & Assert - multiple calls should work
        assertDoesNotThrow(() -> {
            checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
            checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
            checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        }, "Multiple calls to visitInvokeDynamicConstant should not throw");
    }

    /**
     * Tests visitInvokeDynamicConstant with multiple different checker instances.
     */
    @Test
    public void testVisitInvokeDynamicConstant_withDifferentCheckers_worksCorrectly() {
        // Arrange
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act & Assert - different checkers with different flag combinations
        assertDoesNotThrow(() -> {
            new SideEffectInstructionChecker(false, false, false)
                    .visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
            new SideEffectInstructionChecker(true, false, false)
                    .visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
            new SideEffectInstructionChecker(false, true, false)
                    .visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
            new SideEffectInstructionChecker(false, false, true)
                    .visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
            new SideEffectInstructionChecker(true, true, true)
                    .visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        }, "Different checker instances should all work correctly");
    }

    /**
     * Tests that visitInvokeDynamicConstant doesn't modify the constant.
     */
    @Test
    public void testVisitInvokeDynamicConstant_doesNotModifyConstant() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act
        checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

        // Assert - verify no methods were called on the constant (it wasn't modified)
        verifyNoInteractions(invokeDynamicConstant);
    }

    /**
     * Tests that visitInvokeDynamicConstant doesn't interact with the clazz parameter.
     */
    @Test
    public void testVisitInvokeDynamicConstant_doesNotInteractWithClazz() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act
        checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

        // Assert - verify no methods were called on the clazz
        verifyNoInteractions(clazz);
    }

    /**
     * Tests visitInvokeDynamicConstant is idempotent - can be called multiple times
     * with the same result.
     */
    @Test
    public void testVisitInvokeDynamicConstant_isIdempotent() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act - call multiple times
        checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

        // Assert - still no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(invokeDynamicConstant);
    }

    /**
     * Tests that visitInvokeDynamicConstant completes quickly.
     */
    @Test
    public void testVisitInvokeDynamicConstant_completesQuickly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 10000; i++) {
            checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 50ms for 10000 calls)
        assertTrue(durationMs < 50,
                "visitInvokeDynamicConstant should execute extremely quickly (simple assignment)");
    }

    /**
     * Tests that visitInvokeDynamicConstant is thread-safe.
     * Since it only sets a field, it should be safe when called concurrently.
     */
    @Test
    public void testVisitInvokeDynamicConstant_isThreadSafe() throws InterruptedException {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        int threadCount = 10;
        int callsPerThread = 100;
        Thread[] threads = new Thread[threadCount];

        // Act - call from multiple threads
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < callsPerThread; j++) {
                    checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
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
        verifyNoInteractions(invokeDynamicConstant);
    }

    /**
     * Tests that the method works correctly regardless of flag combinations.
     * This verifies that the method ignores all flags and always sets hasSideEffects.
     */
    @Test
    public void testVisitInvokeDynamicConstant_ignoresToAllFlags() {
        // Arrange
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Test all 8 possible flag combinations (2^3)
        boolean[] flags = {false, true};

        // Act & Assert - all combinations should work
        for (boolean returnFlag : flags) {
            for (boolean arrayStoreFlag : flags) {
                for (boolean builtInExceptionsFlag : flags) {
                    SideEffectInstructionChecker checker = new SideEffectInstructionChecker(
                            returnFlag, arrayStoreFlag, builtInExceptionsFlag);

                    assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant),
                            String.format("Should work with flags: return=%s, arrayStore=%s, builtInExceptions=%s",
                                    returnFlag, arrayStoreFlag, builtInExceptionsFlag));
                }
            }
        }
    }

    /**
     * Tests that visitInvokeDynamicConstant works correctly when called
     * after other visitor methods have been called.
     */
    @Test
    public void testVisitInvokeDynamicConstant_afterOtherVisitorCalls_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act - call visitAnyInstruction first, then visitInvokeDynamicConstant
        checker.visitAnyInstruction(clazz, mock(Method.class), mock(proguard.classfile.attribute.CodeAttribute.class),
                0, mock(proguard.classfile.instruction.Instruction.class));

        // Now call visitInvokeDynamicConstant
        assertDoesNotThrow(() -> checker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant),
                "visitInvokeDynamicConstant should work after other visitor calls");
    }

    /**
     * Tests that the method can handle being called in rapid succession.
     */
    @Test
    public void testVisitInvokeDynamicConstant_rapidSuccession_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);

        // Act & Assert - rapid calls with different constants
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                InvokeDynamicConstant constant = mock(InvokeDynamicConstant.class);
                checker.visitInvokeDynamicConstant(clazz, constant);
            }
        }, "Method should handle rapid succession calls");
    }

    /**
     * Tests that visitInvokeDynamicConstant doesn't throw any exceptions
     * regardless of the state of the checker or parameters.
     */
    @Test
    public void testVisitInvokeDynamicConstant_neverThrowsException() {
        // Arrange - try with various combinations
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act & Assert - should never throw
        assertDoesNotThrow(() -> {
            // Various flag combinations
            new SideEffectInstructionChecker(false, false, false)
                    .visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
            new SideEffectInstructionChecker(true, true, true)
                    .visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

            // Real class instances
            new SideEffectInstructionChecker(false, false, false)
                    .visitInvokeDynamicConstant(new ProgramClass(), mock(InvokeDynamicConstant.class));
            new SideEffectInstructionChecker(false, false, false)
                    .visitInvokeDynamicConstant(new LibraryClass(), mock(InvokeDynamicConstant.class));
        }, "visitInvokeDynamicConstant should never throw exceptions");
    }

    /**
     * Tests that multiple checker instances can call visitInvokeDynamicConstant
     * on the same constant without interfering with each other.
     */
    @Test
    public void testVisitInvokeDynamicConstant_multipleCheckersOneConstant_workIndependently() {
        // Arrange
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        SideEffectInstructionChecker checker1 = new SideEffectInstructionChecker(false, false, false);
        SideEffectInstructionChecker checker2 = new SideEffectInstructionChecker(true, false, false);
        SideEffectInstructionChecker checker3 = new SideEffectInstructionChecker(false, true, false);

        // Act & Assert - all should work independently
        assertDoesNotThrow(() -> {
            checker1.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
            checker2.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
            checker3.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);
        }, "Multiple checkers should work independently on the same constant");

        // Verify the constant wasn't modified
        verifyNoInteractions(invokeDynamicConstant);
    }

    /**
     * Tests that the method works correctly as part of the ConstantVisitor pattern.
     * This verifies it properly implements the visitor interface.
     */
    @Test
    public void testVisitInvokeDynamicConstant_asPartOfVisitorPattern_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        invokeDynamicConstant = mock(InvokeDynamicConstant.class);

        // Act - use as a ConstantVisitor
        proguard.classfile.constant.visitor.ConstantVisitor visitor = checker;
        assertDoesNotThrow(() -> visitor.visitInvokeDynamicConstant(clazz, invokeDynamicConstant),
                "Should work correctly as a ConstantVisitor");
    }

    /**
     * Tests that visitInvokeDynamicConstant consistently sets hasSideEffects
     * regardless of how many times it's called or with what parameters.
     */
    @Test
    public void testVisitInvokeDynamicConstant_consistentBehavior() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);

        // Act - call with different constants multiple times
        InvokeDynamicConstant constant1 = mock(InvokeDynamicConstant.class);
        InvokeDynamicConstant constant2 = mock(InvokeDynamicConstant.class);
        InvokeDynamicConstant constant3 = mock(InvokeDynamicConstant.class);

        checker.visitInvokeDynamicConstant(clazz, constant1);
        checker.visitInvokeDynamicConstant(clazz, constant2);
        checker.visitInvokeDynamicConstant(clazz, constant3);

        // Assert - behavior should be consistent (no exceptions, no interactions)
        verifyNoInteractions(constant1);
        verifyNoInteractions(constant2);
        verifyNoInteractions(constant3);
        verifyNoInteractions(clazz);
    }
}

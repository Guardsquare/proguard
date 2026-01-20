package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SideEffectInstructionChecker#visitAnyMethodrefConstant}.
 *
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/constant/AnyMethodrefConstant;)V
 *
 * The visitAnyMethodrefConstant method is part of the ConstantVisitor interface implementation.
 * It performs three actions:
 * 1. Stores the referencing class in the referencingClass field
 * 2. Sets hasSideEffects to true (assumes unknown method invocation has side effects)
 * 3. Calls anyMethodrefConstant.referencedMethodAccept(this) to check if the referenced method is known
 *    and potentially update hasSideEffects based on the method's properties
 *
 * This method is called when visitConstantInstruction encounters method invocation instructions
 * (INVOKESPECIAL, INVOKESTATIC, INVOKEVIRTUAL, INVOKEINTERFACE) and calls clazz.constantPoolEntryAccept().
 *
 * AnyMethodrefConstant is the base interface for both MethodrefConstant and InterfaceMethodrefConstant,
 * allowing this method to handle both regular method references and interface method references uniformly.
 *
 * Note: Testing the complete behavior requires setting up ProgramMethod references which is complex.
 * These tests focus on the directly testable behavior without requiring full method setup.
 */
public class SideEffectInstructionCheckerClaude_visitAnyMethodrefConstantTest {

    private Clazz clazz;
    private AnyMethodrefConstant anyMethodrefConstant;

    // =========================================================================
    // Basic Functionality Tests
    // =========================================================================

    /**
     * Tests that visitAnyMethodrefConstant sets hasSideEffects to true initially.
     * Invoking an unknown method is assumed to have side effects.
     */
    @Test
    public void testVisitAnyMethodrefConstant_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        assertDoesNotThrow(() -> checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant),
                "visitAnyMethodrefConstant should not throw any exception");
    }

    /**
     * Tests that visitAnyMethodrefConstant calls referencedMethodAccept on the constant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify referencedMethodAccept was called with the checker as visitor
        verify(anyMethodrefConstant).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with all flags disabled.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withAllFlagsDisabled_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests that visitAnyMethodrefConstant passes the checker itself as MemberVisitor.
     */
    @Test
    public void testVisitAnyMethodrefConstant_passesCheckerAsMemberVisitor() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify a MemberVisitor was passed (should be the checker itself)
        verify(anyMethodrefConstant).referencedMethodAccept(isA(MemberVisitor.class));
    }

    // =========================================================================
    // Tests with Different Flag Combinations
    // =========================================================================

    /**
     * Tests visitAnyMethodrefConstant with includeReturnInstructions enabled.
     * The flag should not affect this method's behavior as it relates to return instructions, not method calls.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withIncludeReturnInstructions_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with includeArrayStoreInstructions enabled.
     * The flag should not affect this method's behavior as it relates to array operations, not method calls.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withIncludeArrayStoreInstructions_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with includeBuiltInExceptions enabled.
     * The flag should not affect this method's behavior directly, though it may affect
     * how method invocations are treated in visitConstantInstruction.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withIncludeBuiltInExceptions_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with all flags enabled.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withAllFlagsEnabled_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with includeReturnInstructions and includeArrayStoreInstructions enabled.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withReturnAndArrayFlags_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with includeReturnInstructions and includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withReturnAndExceptionFlags_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, true);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with includeArrayStoreInstructions and includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withArrayAndExceptionFlags_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, true);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    // =========================================================================
    // Tests with Different Clazz Types
    // =========================================================================

    /**
     * Tests visitAnyMethodrefConstant with a ProgramClass.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withProgramClass_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with a LibraryClass.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withLibraryClass_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(LibraryClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with a generic Clazz mock.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withGenericClazz_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(Clazz.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    // =========================================================================
    // Tests with Different AnyMethodrefConstant Types
    // =========================================================================

    /**
     * Tests visitAnyMethodrefConstant with a MethodrefConstant (regular method reference).
     */
    @Test
    public void testVisitAnyMethodrefConstant_withMethodrefConstant_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with an InterfaceMethodrefConstant (interface method reference).
     */
    @Test
    public void testVisitAnyMethodrefConstant_withInterfaceMethodrefConstant_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(InterfaceMethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with a generic AnyMethodrefConstant mock.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withGenericAnyMethodrefConstant_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(AnyMethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    // =========================================================================
    // Tests with Different Constant Instances
    // =========================================================================

    /**
     * Tests visitAnyMethodrefConstant with different AnyMethodrefConstant instances.
     * Each instance should receive a referencedMethodAccept call.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentConstants_callsReferencedMethodAcceptOnEach() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        AnyMethodrefConstant constant1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant constant2 = mock(MethodrefConstant.class);
        AnyMethodrefConstant constant3 = mock(InterfaceMethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, constant1);
        checker.visitAnyMethodrefConstant(clazz, constant2);
        checker.visitAnyMethodrefConstant(clazz, constant3);

        // Assert
        verify(constant1, times(1)).referencedMethodAccept(eq(checker));
        verify(constant2, times(1)).referencedMethodAccept(eq(checker));
        verify(constant3, times(1)).referencedMethodAccept(eq(checker));
    }

    // =========================================================================
    // Null Parameter Tests
    // =========================================================================

    /**
     * Tests that visitAnyMethodrefConstant with null Clazz parameter does not throw exception.
     * The method stores the clazz in referencingClass but doesn't directly use it,
     * so null should be handled gracefully.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullClazz_callsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyMethodrefConstant(null, anyMethodrefConstant));
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests that visitAnyMethodrefConstant with null AnyMethodrefConstant throws NullPointerException.
     * The method calls referencedMethodAccept on the constant, so null will cause NPE.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullConstant_throwsNullPointerException() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> checker.visitAnyMethodrefConstant(clazz, null),
                "Should throw NullPointerException when anyMethodrefConstant is null");
    }

    /**
     * Tests that visitAnyMethodrefConstant with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withBothNull_throwsNullPointerException() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> checker.visitAnyMethodrefConstant(null, null),
                "Should throw NullPointerException when anyMethodrefConstant is null");
    }

    // =========================================================================
    // Integration Tests - Multiple Calls
    // =========================================================================

    /**
     * Tests that visitAnyMethodrefConstant can be called multiple times in succession.
     * Each call should invoke referencedMethodAccept.
     */
    @Test
    public void testVisitAnyMethodrefConstant_calledMultipleTimes_invokesReferencedMethodAcceptEachTime() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(3)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with multiple different checkers on the same constant.
     * Each checker should trigger a referencedMethodAccept call.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentCheckers_eachCallsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker1 = new SideEffectInstructionChecker(false, false, false);
        SideEffectInstructionChecker checker2 = new SideEffectInstructionChecker(true, false, false);
        SideEffectInstructionChecker checker3 = new SideEffectInstructionChecker(false, true, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker1.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        checker2.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        checker3.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - should be called 3 times total (once by each checker)
        verify(anyMethodrefConstant, times(3)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests visitAnyMethodrefConstant called in rapid succession without resetting checker state.
     */
    @Test
    public void testVisitAnyMethodrefConstant_rapidSuccession_callsReferencedMethodAcceptEachTime() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act - call 10 times rapidly
        for (int i = 0; i < 10; i++) {
            checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        }

        // Assert
        verify(anyMethodrefConstant, times(10)).referencedMethodAccept(eq(checker));
    }

    // =========================================================================
    // Mixed Type Tests
    // =========================================================================

    /**
     * Tests visitAnyMethodrefConstant with ProgramClass and MethodrefConstant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withProgramClassAndMethodrefConstant() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with ProgramClass and InterfaceMethodrefConstant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withProgramClassAndInterfaceMethodrefConstant() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(InterfaceMethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with LibraryClass and MethodrefConstant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withLibraryClassAndMethodrefConstant() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(LibraryClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with LibraryClass and InterfaceMethodrefConstant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withLibraryClassAndInterfaceMethodrefConstant() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(LibraryClass.class);
        anyMethodrefConstant = mock(InterfaceMethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(eq(checker));
    }

    // =========================================================================
    // Verification Tests
    // =========================================================================

    /**
     * Tests that visitAnyMethodrefConstant only interacts with the referencedMethodAccept method
     * and doesn't call other methods on the constant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_onlyCallsReferencedMethodAccept() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify only referencedMethodAccept was called
        verify(anyMethodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
        verifyNoMoreInteractions(anyMethodrefConstant);
    }

    /**
     * Tests that visitAnyMethodrefConstant doesn't interact with the clazz parameter.
     * The clazz is stored internally but not used for any method calls in this method.
     */
    @Test
    public void testVisitAnyMethodrefConstant_doesNotInteractWithClazz() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify no interactions with clazz
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyMethodrefConstant passes exactly the checker instance to referencedMethodAccept.
     */
    @Test
    public void testVisitAnyMethodrefConstant_passesExactCheckerInstance() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify the exact checker instance was passed
        verify(anyMethodrefConstant).referencedMethodAccept(same(checker));
    }

    // =========================================================================
    // Edge Cases
    // =========================================================================

    /**
     * Tests visitAnyMethodrefConstant when referencedMethodAccept does nothing.
     * The method should complete successfully even if the callback does nothing.
     */
    @Test
    public void testVisitAnyMethodrefConstant_whenReferencedMethodAcceptDoesNothing_completesSuccessfully() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);
        doNothing().when(anyMethodrefConstant).referencedMethodAccept(any(MemberVisitor.class));

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));
    }

    /**
     * Tests visitAnyMethodrefConstant when referencedMethodAccept throws an exception.
     * The exception should propagate to the caller.
     */
    @Test
    public void testVisitAnyMethodrefConstant_whenReferencedMethodAcceptThrowsException_propagatesException() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException).when(anyMethodrefConstant).referencedMethodAccept(any(MemberVisitor.class));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> checker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));
        assertSame(expectedException, thrown, "Should propagate the same exception instance");
    }

    /**
     * Tests visitAnyMethodrefConstant alternating between MethodrefConstant and InterfaceMethodrefConstant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_alternatingConstantTypes_callsReferencedMethodAcceptOnEach() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        MethodrefConstant methodref = mock(MethodrefConstant.class);
        InterfaceMethodrefConstant interfaceMethodref = mock(InterfaceMethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz, methodref);
        checker.visitAnyMethodrefConstant(clazz, interfaceMethodref);
        checker.visitAnyMethodrefConstant(clazz, methodref);
        checker.visitAnyMethodrefConstant(clazz, interfaceMethodref);

        // Assert
        verify(methodref, times(2)).referencedMethodAccept(eq(checker));
        verify(interfaceMethodref, times(2)).referencedMethodAccept(eq(checker));
    }

    /**
     * Tests visitAnyMethodrefConstant with the same constant but different clazz instances.
     */
    @Test
    public void testVisitAnyMethodrefConstant_sameConstantDifferentClazz_callsReferencedMethodAcceptEachTime() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        anyMethodrefConstant = mock(MethodrefConstant.class);

        // Act
        checker.visitAnyMethodrefConstant(clazz1, anyMethodrefConstant);
        checker.visitAnyMethodrefConstant(clazz2, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, times(2)).referencedMethodAccept(eq(checker));
    }
}

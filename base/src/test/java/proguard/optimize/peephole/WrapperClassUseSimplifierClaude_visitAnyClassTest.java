package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link WrapperClassUseSimplifier#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in WrapperClassUseSimplifier is a no-op implementation (empty method body).
 * It's required by the ClassVisitor interface but intentionally does nothing. The actual class
 * processing is handled by the more specific visitProgramClass method, which handles wrapper class
 * detection.
 *
 * These tests verify that the method:
 * 1. Does not throw exceptions when called
 * 2. Does not modify the clazz or simplifier state
 * 3. Does not trigger the extra instruction visitor
 * 4. Works correctly with different types of parameters
 */
public class WrapperClassUseSimplifierClaude_visitAnyClassTest {

    private WrapperClassUseSimplifier simplifier;
    private InstructionVisitor mockExtraVisitor;
    private Clazz mockClazz;

    @BeforeEach
    public void setUp() {
        mockExtraVisitor = mock(InstructionVisitor.class);
        mockClazz = mock(Clazz.class);
    }

    // ========================================
    // visitAnyClass Tests - No-op Verification
    // ========================================

    /**
     * Tests visitAnyClass with a WrapperClassUseSimplifier that has no extra visitor.
     * The method should do nothing (no-op) and not throw any exceptions.
     */
    @Test
    public void testVisitAnyClass_withNullExtraVisitor_doesNothing() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyClass(mockClazz),
                "visitAnyClass should not throw exceptions");

        // Assert - no interactions with the clazz
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyClass with a WrapperClassUseSimplifier that has an extra visitor.
     * The method should do nothing and not trigger the extra visitor.
     */
    @Test
    public void testVisitAnyClass_withExtraVisitor_doesNotTriggerVisitor() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        assertDoesNotThrow(() -> simplifier.visitAnyClass(mockClazz),
                "visitAnyClass should not throw exceptions");

        // Assert - extra visitor should not be called
        verifyNoInteractions(mockExtraVisitor);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyClass with no-arg constructor.
     * The method should do nothing regardless of constructor variant used.
     */
    @Test
    public void testVisitAnyClass_withNoArgConstructor_doesNothing() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier();

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyClass(mockClazz),
                "visitAnyClass should not throw exceptions with no-arg constructor");

        // Assert - no interactions with the clazz
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyClass with null parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyClass_withNullParameter_doesNotThrow() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyClass(null),
                "visitAnyClass should handle null parameter without throwing");
    }

    /**
     * Tests that visitAnyClass can be called multiple times without side effects.
     * Each call should remain a no-op.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_remainsNoOp() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call multiple times
        simplifier.visitAnyClass(mockClazz);
        simplifier.visitAnyClass(mockClazz);
        simplifier.visitAnyClass(mockClazz);

        // Assert - no interactions should occur even after multiple calls
        verifyNoInteractions(mockExtraVisitor);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyClass with different clazz instances.
     * The no-op behavior should be consistent regardless of the clazz.
     */
    @Test
    public void testVisitAnyClass_withDifferentClasses_alwaysNoOp() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Clazz mockClazz1 = mock(Clazz.class);
        Clazz mockClazz2 = mock(Clazz.class);
        Clazz mockClazz3 = mock(Clazz.class);

        // Act
        simplifier.visitAnyClass(mockClazz1);
        simplifier.visitAnyClass(mockClazz2);
        simplifier.visitAnyClass(mockClazz3);

        // Assert - no interactions with any clazz or the extra visitor
        verifyNoInteractions(mockClazz1);
        verifyNoInteractions(mockClazz2);
        verifyNoInteractions(mockClazz3);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that multiple WrapperClassUseSimplifier instances with visitAnyClass
     * don't interfere with each other.
     */
    @Test
    public void testVisitAnyClass_multipleInstances_independent() {
        // Arrange
        InstructionVisitor mockVisitor1 = mock(InstructionVisitor.class);
        InstructionVisitor mockVisitor2 = mock(InstructionVisitor.class);
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(mockVisitor1);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(mockVisitor2);

        // Act
        simplifier1.visitAnyClass(mockClazz);
        simplifier2.visitAnyClass(mockClazz);

        // Assert - no interactions with any visitor
        verifyNoInteractions(mockVisitor1);
        verifyNoInteractions(mockVisitor2);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyClass does not affect subsequent operations.
     * Calling visitAnyClass should not interfere with the simplifier's state.
     */
    @Test
    public void testVisitAnyClass_doesNotAffectSimplifierState() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call visitAnyClass first
        simplifier.visitAnyClass(mockClazz);

        // Try calling it again to ensure state is not corrupted
        assertDoesNotThrow(() -> simplifier.visitAnyClass(mockClazz),
                "Second call should also be a no-op");

        // Assert - still no interactions
        verifyNoInteractions(mockExtraVisitor);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyClass truly does nothing by verifying
     * it doesn't prepare or affect any internal state for code simplification operations.
     */
    @Test
    public void testVisitAnyClass_doesNotPrepareCodeSimplification() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call visitAnyClass (which should do nothing)
        simplifier.visitAnyClass(mockClazz);

        // Assert - verify the clazz is not examined or modified
        // The no-op should mean absolutely no method calls on any mock
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyClass maintains no-op behavior regardless of
     * the parameter passed.
     */
    @Test
    public void testVisitAnyClass_consistentNoOpBehavior() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Create various mock instances
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act - call with various combinations
        assertDoesNotThrow(() -> {
            simplifier.visitAnyClass(clazz1);
            simplifier.visitAnyClass(clazz2);
            simplifier.visitAnyClass(clazz1);
            simplifier.visitAnyClass(clazz2);
            simplifier.visitAnyClass(null);
        }, "visitAnyClass should always be a no-op");

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz1, clazz2, mockExtraVisitor);
    }

    /**
     * Tests visitAnyClass with both constructor variants.
     * The no-op behavior should be consistent regardless of which constructor was used.
     */
    @Test
    public void testVisitAnyClass_withBothConstructors_consistentBehavior() {
        // Arrange
        WrapperClassUseSimplifier simplifierNoArg = new WrapperClassUseSimplifier();
        WrapperClassUseSimplifier simplifierWithVisitor = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        simplifierNoArg.visitAnyClass(mockClazz);
        simplifierWithVisitor.visitAnyClass(mockClazz);

        // Assert - both should be no-ops
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyClass does not modify any wrapper class simplification state.
     * It should truly be a no-op, not preparing for any wrapper class use simplification.
     */
    @Test
    public void testVisitAnyClass_doesNotPrepareWrapperSimplification() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0;

        // Act - call visitAnyClass (which should do nothing)
        simplifier.visitAnyClass(programClass);

        // Assert - since visitAnyClass does nothing, the program class should be
        // completely unmodified and no simplification should have been initiated
        verifyNoInteractions(mockExtraVisitor);
        assertEquals(0, programClass.u2accessFlags,
                "visitAnyClass should not prepare or initiate any simplification");
    }

    /**
     * Tests visitAnyClass with rapid successive calls.
     * Verifies that the no-op is truly stateless and efficient.
     */
    @Test
    public void testVisitAnyClass_rapidSuccessiveCalls_remainsEfficient() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call many times rapidly
        for (int i = 0; i < 1000; i++) {
            simplifier.visitAnyClass(mockClazz);
        }

        // Assert - should still have no interactions
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyClass completes quickly even with many calls.
     * Verifies the no-op implementation is truly efficient.
     */
    @Test
    public void testVisitAnyClass_performance_completesQuickly() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 10000; i++) {
            simplifier.visitAnyClass(mockClazz);
        }

        // Assert - should complete very quickly (less than 100ms for 10000 calls)
        long duration = System.nanoTime() - startTime;
        assertTrue(duration < 100_000_000L,
                "10000 calls to visitAnyClass should complete in under 100ms (took " + duration + " ns)");
    }

    /**
     * Tests visitAnyClass returns normally (void method completes).
     * Verifies that the method signature is correct and completes execution.
     */
    @Test
    public void testVisitAnyClass_returnsNormally() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert - should complete without exceptions or blocking
        assertDoesNotThrow(() -> {
            simplifier.visitAnyClass(mockClazz);
            // If we reach this point, the method returned normally
        }, "visitAnyClass should return normally");
    }

    /**
     * Tests that visitAnyClass is truly empty and doesn't execute hidden logic.
     * By verifying no state changes occur in various scenarios.
     */
    @Test
    public void testVisitAnyClass_trulyEmpty_noHiddenLogic() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 1;
        programClass.u4version = 50;
        programClass.u2thisClass = 5;
        programClass.u2superClass = 10;

        int originalAccessFlags = programClass.u2accessFlags;
        int originalVersion = programClass.u4version;
        int originalThisClass = programClass.u2thisClass;
        int originalSuperClass = programClass.u2superClass;

        // Act
        simplifier.visitAnyClass(programClass);

        // Assert - verify all fields remain unchanged
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
                "Access flags should not be modified");
        assertEquals(originalVersion, programClass.u4version,
                "Version should not be modified");
        assertEquals(originalThisClass, programClass.u2thisClass,
                "This class index should not be modified");
        assertEquals(originalSuperClass, programClass.u2superClass,
                "Super class index should not be modified");
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyClass with alternating simplifier instances.
     * The no-op should work consistently regardless of instance alternation.
     */
    @Test
    public void testVisitAnyClass_withAlternatingInstances_consistentNoOp() {
        // Arrange
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(mockExtraVisitor);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(null);

        // Act - alternate between instances
        simplifier1.visitAnyClass(mockClazz);
        simplifier2.visitAnyClass(mockClazz);
        simplifier1.visitAnyClass(mockClazz);
        simplifier2.visitAnyClass(mockClazz);

        // Assert - should have no interactions
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyClass with sequential different class instances.
     * Verifies that the no-op is stateless across different instances.
     */
    @Test
    public void testVisitAnyClass_withSequentialDifferentInstances_stateless() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Clazz[] classes = new Clazz[5];

        for (int i = 0; i < 5; i++) {
            classes[i] = mock(Clazz.class);
        }

        // Act
        for (int i = 0; i < 5; i++) {
            simplifier.visitAnyClass(classes[i]);
        }

        // Assert - all should have no interactions
        for (int i = 0; i < 5; i++) {
            verifyNoInteractions(classes[i]);
        }
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyClass with various concrete class types.
     * The no-op behavior should be consistent regardless of the class type.
     */
    @Test
    public void testVisitAnyClass_withVariousClassTypes_alwaysNoOp() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        Clazz mockClass = mock(Clazz.class);

        // Act
        simplifier.visitAnyClass(programClass);
        simplifier.visitAnyClass(libraryClass);
        simplifier.visitAnyClass(mockClass);

        // Assert - no interactions with the mock or the extra visitor
        verifyNoInteractions(mockClass);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyClass does not inspect or modify the class parameter.
     * Verifies complete isolation from the clazz.
     */
    @Test
    public void testVisitAnyClass_doesNotInspectClazz() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Clazz spyClazz = mock(Clazz.class);

        // Act
        simplifier.visitAnyClass(spyClazz);

        // Assert - the clazz should not have any method calls made on it
        verifyNoInteractions(spyClazz);
    }

    /**
     * Tests visitAnyClass in the context of being part of the ClassVisitor interface.
     * Verifies that it properly implements the interface contract as a no-op.
     */
    @Test
    public void testVisitAnyClass_asClassVisitor_properNoOpImplementation() {
        // Arrange
        WrapperClassUseSimplifier visitor = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act & Assert - use as ClassVisitor interface
        assertDoesNotThrow(() -> visitor.visitAnyClass(mockClazz),
                "visitAnyClass should work correctly through ClassVisitor interface");

        // Assert - no interactions
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyClass with concurrent-like rapid switching between different parameters.
     * Verifies that the no-op is thread-safe in its statelessness.
     */
    @Test
    public void testVisitAnyClass_rapidParameterSwitching_remainsStateless() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act - rapidly switch between different parameter combinations
        for (int i = 0; i < 100; i++) {
            simplifier.visitAnyClass(clazz1);
            simplifier.visitAnyClass(clazz2);
        }

        // Assert - no interactions should occur
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyClass does not interfere with other visitor methods.
     * Verifies that calling visitAnyClass doesn't affect the state used by other methods.
     */
    @Test
    public void testVisitAnyClass_doesNotInterfereWithOtherMethods() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call visitAnyClass
        simplifier.visitAnyClass(mockClazz);

        // Assert - should still be able to call other methods without issues
        assertDoesNotThrow(() -> {
            simplifier.visitAnyClass(mockClazz);
        }, "visitAnyClass should not interfere with subsequent calls");

        // Assert - no interactions
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyClass with ProgramClass to ensure it doesn't trigger
     * the visitProgramClass logic inadvertently.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_doesNotTriggerVisitProgramClass() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ProgramClass programClass = new ProgramClass();

        // Act
        simplifier.visitAnyClass(programClass);

        // Assert - visitAnyClass should not process the class even if it's a ProgramClass
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyClass with LibraryClass.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_doesNothing() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyClass(libraryClass));
        verifyNoInteractions(mockExtraVisitor);
    }
}

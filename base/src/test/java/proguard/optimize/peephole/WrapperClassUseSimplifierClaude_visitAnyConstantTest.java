package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link WrapperClassUseSimplifier#visitAnyConstant(Clazz, Constant)}.
 *
 * The visitAnyConstant method in WrapperClassUseSimplifier is a no-op implementation (empty method body).
 * It's required by the ConstantVisitor interface but intentionally does nothing. The actual constant
 * processing is handled by more specific visitor methods like visitFieldrefConstant, visitMethodrefConstant,
 * and visitClassConstant.
 *
 * These tests verify that the method:
 * 1. Does not throw exceptions when called
 * 2. Does not modify the constant or simplifier state
 * 3. Does not trigger the extra instruction visitor
 * 4. Works correctly with different types of parameters
 */
public class WrapperClassUseSimplifierClaude_visitAnyConstantTest {

    private WrapperClassUseSimplifier simplifier;
    private InstructionVisitor mockExtraVisitor;
    private Clazz mockClazz;
    private Constant mockConstant;

    @BeforeEach
    public void setUp() {
        mockExtraVisitor = mock(InstructionVisitor.class);
        mockClazz = mock(Clazz.class);
        mockConstant = mock(Constant.class);
    }

    // ========================================
    // visitAnyConstant Tests - No-op Verification
    // ========================================

    /**
     * Tests visitAnyConstant with a WrapperClassUseSimplifier that has no extra visitor.
     * The method should do nothing (no-op) and not throw any exceptions.
     */
    @Test
    public void testVisitAnyConstant_withNullExtraVisitor_doesNothing() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyConstant(mockClazz, mockConstant),
                "visitAnyConstant should not throw exceptions");

        // Assert - no interactions with the constant or clazz
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyConstant with a WrapperClassUseSimplifier that has an extra visitor.
     * The method should do nothing and not trigger the extra visitor.
     */
    @Test
    public void testVisitAnyConstant_withExtraVisitor_doesNotTriggerVisitor() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        assertDoesNotThrow(() -> simplifier.visitAnyConstant(mockClazz, mockConstant),
                "visitAnyConstant should not throw exceptions");

        // Assert - extra visitor should not be called
        verifyNoInteractions(mockExtraVisitor);
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyConstant with no-arg constructor.
     * The method should do nothing regardless of constructor variant used.
     */
    @Test
    public void testVisitAnyConstant_withNoArgConstructor_doesNothing() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier();

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyConstant(mockClazz, mockConstant),
                "visitAnyConstant should not throw exceptions with no-arg constructor");

        // Assert - no interactions with the constant or clazz
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyConstant with null parameters.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withNullParameters_doesNotThrow() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyConstant(null, null),
                "visitAnyConstant should handle null parameters without throwing");
    }

    /**
     * Tests visitAnyConstant with null clazz but non-null constant.
     * The method should handle this gracefully.
     */
    @Test
    public void testVisitAnyConstant_withNullClazz_doesNotThrow() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyConstant(null, mockConstant),
                "visitAnyConstant should handle null clazz");
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyConstant with non-null clazz but null constant.
     * The method should handle this gracefully.
     */
    @Test
    public void testVisitAnyConstant_withNullConstant_doesNotThrow() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyConstant(mockClazz, null),
                "visitAnyConstant should handle null constant");
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyConstant can be called multiple times without side effects.
     * Each call should remain a no-op.
     */
    @Test
    public void testVisitAnyConstant_calledMultipleTimes_remainsNoOp() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call multiple times
        simplifier.visitAnyConstant(mockClazz, mockConstant);
        simplifier.visitAnyConstant(mockClazz, mockConstant);
        simplifier.visitAnyConstant(mockClazz, mockConstant);

        // Assert - no interactions should occur even after multiple calls
        verifyNoInteractions(mockExtraVisitor);
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyConstant with different constant instances.
     * The no-op behavior should be consistent regardless of the constant.
     */
    @Test
    public void testVisitAnyConstant_withDifferentConstants_alwaysNoOp() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Constant mockConstant1 = mock(Constant.class);
        Constant mockConstant2 = mock(Constant.class);
        Constant mockConstant3 = mock(Constant.class);

        // Act
        simplifier.visitAnyConstant(mockClazz, mockConstant1);
        simplifier.visitAnyConstant(mockClazz, mockConstant2);
        simplifier.visitAnyConstant(mockClazz, mockConstant3);

        // Assert - no interactions with any constant or the extra visitor
        verifyNoInteractions(mockConstant1);
        verifyNoInteractions(mockConstant2);
        verifyNoInteractions(mockConstant3);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyConstant with different clazz instances.
     * The no-op behavior should be consistent regardless of the clazz.
     */
    @Test
    public void testVisitAnyConstant_withDifferentClasses_alwaysNoOp() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Clazz mockClazz1 = mock(Clazz.class);
        Clazz mockClazz2 = mock(Clazz.class);
        ProgramClass programClass = new ProgramClass();

        // Act
        simplifier.visitAnyConstant(mockClazz1, mockConstant);
        simplifier.visitAnyConstant(mockClazz2, mockConstant);
        simplifier.visitAnyConstant(programClass, mockConstant);

        // Assert - no interactions
        verifyNoInteractions(mockClazz1);
        verifyNoInteractions(mockClazz2);
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that multiple WrapperClassUseSimplifier instances with visitAnyConstant
     * don't interfere with each other.
     */
    @Test
    public void testVisitAnyConstant_multipleInstances_independent() {
        // Arrange
        InstructionVisitor mockVisitor1 = mock(InstructionVisitor.class);
        InstructionVisitor mockVisitor2 = mock(InstructionVisitor.class);
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(mockVisitor1);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(mockVisitor2);

        // Act
        simplifier1.visitAnyConstant(mockClazz, mockConstant);
        simplifier2.visitAnyConstant(mockClazz, mockConstant);

        // Assert - no interactions with any visitor
        verifyNoInteractions(mockVisitor1);
        verifyNoInteractions(mockVisitor2);
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyConstant does not affect subsequent operations.
     * Calling visitAnyConstant should not interfere with the simplifier's state.
     */
    @Test
    public void testVisitAnyConstant_doesNotAffectSimplifierState() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call visitAnyConstant first
        simplifier.visitAnyConstant(mockClazz, mockConstant);

        // Try calling it again to ensure state is not corrupted
        assertDoesNotThrow(() -> simplifier.visitAnyConstant(mockClazz, mockConstant),
                "Second call should also be a no-op");

        // Assert - still no interactions
        verifyNoInteractions(mockExtraVisitor);
        verifyNoInteractions(mockConstant);
    }

    /**
     * Tests that visitAnyConstant truly does nothing by verifying
     * it doesn't prepare or affect any internal state for code simplification operations.
     */
    @Test
    public void testVisitAnyConstant_doesNotPrepareCodeSimplification() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call visitAnyConstant (which should do nothing)
        simplifier.visitAnyConstant(mockClazz, mockConstant);

        // Assert - verify the constant and clazz are not examined or modified
        // The no-op should mean absolutely no method calls on any mock
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyConstant maintains no-op behavior regardless of
     * the number or variety of parameters passed.
     */
    @Test
    public void testVisitAnyConstant_consistentNoOpBehavior() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Create various mock instances
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Constant const1 = mock(Constant.class);
        Constant const2 = mock(Constant.class);

        // Act - call with various combinations
        assertDoesNotThrow(() -> {
            simplifier.visitAnyConstant(clazz1, const1);
            simplifier.visitAnyConstant(clazz1, const2);
            simplifier.visitAnyConstant(clazz2, const1);
            simplifier.visitAnyConstant(clazz2, const2);
            simplifier.visitAnyConstant(null, null);
        }, "visitAnyConstant should always be a no-op");

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz1, clazz2, const1, const2, mockExtraVisitor);
    }

    /**
     * Tests visitAnyConstant with both constructor variants.
     * The no-op behavior should be consistent regardless of which constructor was used.
     */
    @Test
    public void testVisitAnyConstant_withBothConstructors_consistentBehavior() {
        // Arrange
        WrapperClassUseSimplifier simplifierNoArg = new WrapperClassUseSimplifier();
        WrapperClassUseSimplifier simplifierWithVisitor = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        simplifierNoArg.visitAnyConstant(mockClazz, mockConstant);
        simplifierWithVisitor.visitAnyConstant(mockClazz, mockConstant);

        // Assert - both should be no-ops
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyConstant does not modify any wrapper class simplification state.
     * It should truly be a no-op, not preparing for any wrapper class use simplification.
     */
    @Test
    public void testVisitAnyConstant_doesNotPrepareWrapperSimplification() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0;

        // Act - call visitAnyConstant (which should do nothing)
        simplifier.visitAnyConstant(programClass, mockConstant);

        // Assert - since visitAnyConstant does nothing, the program class should be
        // completely unmodified and no simplification should have been initiated
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockExtraVisitor);
        assertEquals(0, programClass.u2accessFlags,
                "visitAnyConstant should not prepare or initiate any simplification");
    }

    /**
     * Tests visitAnyConstant with rapid successive calls.
     * Verifies that the no-op is truly stateless and efficient.
     */
    @Test
    public void testVisitAnyConstant_rapidSuccessiveCalls_remainsEfficient() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call many times rapidly
        for (int i = 0; i < 1000; i++) {
            simplifier.visitAnyConstant(mockClazz, mockConstant);
        }

        // Assert - should still have no interactions
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyConstant completes quickly even with many calls.
     * Verifies the no-op implementation is truly efficient.
     */
    @Test
    public void testVisitAnyConstant_performance_completesQuickly() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 10000; i++) {
            simplifier.visitAnyConstant(mockClazz, mockConstant);
        }

        // Assert - should complete very quickly (less than 100ms for 10000 calls)
        long duration = System.nanoTime() - startTime;
        assertTrue(duration < 100_000_000L,
                "10000 calls to visitAnyConstant should complete in under 100ms (took " + duration + " ns)");
    }

    /**
     * Tests visitAnyConstant returns normally (void method completes).
     * Verifies that the method signature is correct and completes execution.
     */
    @Test
    public void testVisitAnyConstant_returnsNormally() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert - should complete without exceptions or blocking
        assertDoesNotThrow(() -> {
            simplifier.visitAnyConstant(mockClazz, mockConstant);
            // If we reach this point, the method returned normally
        }, "visitAnyConstant should return normally");
    }

    /**
     * Tests that visitAnyConstant is truly empty and doesn't execute hidden logic.
     * By verifying no state changes occur in various scenarios.
     */
    @Test
    public void testVisitAnyConstant_trulyEmpty_noHiddenLogic() {
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
        simplifier.visitAnyConstant(programClass, mockConstant);

        // Assert - verify all fields remain unchanged
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
                "Access flags should not be modified");
        assertEquals(originalVersion, programClass.u4version,
                "Version should not be modified");
        assertEquals(originalThisClass, programClass.u2thisClass,
                "This class index should not be modified");
        assertEquals(originalSuperClass, programClass.u2superClass,
                "Super class index should not be modified");
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyConstant with alternating simplifier instances.
     * The no-op should work consistently regardless of instance alternation.
     */
    @Test
    public void testVisitAnyConstant_withAlternatingInstances_consistentNoOp() {
        // Arrange
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(mockExtraVisitor);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(null);

        // Act - alternate between instances
        simplifier1.visitAnyConstant(mockClazz, mockConstant);
        simplifier2.visitAnyConstant(mockClazz, mockConstant);
        simplifier1.visitAnyConstant(mockClazz, mockConstant);
        simplifier2.visitAnyConstant(mockClazz, mockConstant);

        // Assert - should have no interactions
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyConstant with sequential different constant and class instances.
     * Verifies that the no-op is stateless across different instances.
     */
    @Test
    public void testVisitAnyConstant_withSequentialDifferentInstances_stateless() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Clazz[] classes = new Clazz[5];
        Constant[] constants = new Constant[5];

        for (int i = 0; i < 5; i++) {
            classes[i] = mock(Clazz.class);
            constants[i] = mock(Constant.class);
        }

        // Act
        for (int i = 0; i < 5; i++) {
            simplifier.visitAnyConstant(classes[i], constants[i]);
        }

        // Assert - all should have no interactions
        for (int i = 0; i < 5; i++) {
            verifyNoInteractions(classes[i]);
            verifyNoInteractions(constants[i]);
        }
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyConstant with various concrete constant types.
     * The no-op behavior should be consistent regardless of the constant type.
     */
    @Test
    public void testVisitAnyConstant_withVariousConstantTypes_alwaysNoOp() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        IntegerConstant intConstant = mock(IntegerConstant.class);
        LongConstant longConstant = mock(LongConstant.class);
        FloatConstant floatConstant = mock(FloatConstant.class);
        DoubleConstant doubleConstant = mock(DoubleConstant.class);
        StringConstant stringConstant = mock(StringConstant.class);
        Utf8Constant utf8Constant = mock(Utf8Constant.class);

        // Act
        simplifier.visitAnyConstant(mockClazz, intConstant);
        simplifier.visitAnyConstant(mockClazz, longConstant);
        simplifier.visitAnyConstant(mockClazz, floatConstant);
        simplifier.visitAnyConstant(mockClazz, doubleConstant);
        simplifier.visitAnyConstant(mockClazz, stringConstant);
        simplifier.visitAnyConstant(mockClazz, utf8Constant);

        // Assert - no interactions with any constant or the extra visitor
        verifyNoInteractions(intConstant);
        verifyNoInteractions(longConstant);
        verifyNoInteractions(floatConstant);
        verifyNoInteractions(doubleConstant);
        verifyNoInteractions(stringConstant);
        verifyNoInteractions(utf8Constant);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyConstant with reference type constants.
     * The no-op behavior should be consistent for all constant types.
     */
    @Test
    public void testVisitAnyConstant_withReferenceConstants_alwaysNoOp() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ClassConstant classConstant = mock(ClassConstant.class);
        FieldrefConstant fieldrefConstant = mock(FieldrefConstant.class);
        MethodrefConstant methodrefConstant = mock(MethodrefConstant.class);
        InterfaceMethodrefConstant interfaceMethodrefConstant = mock(InterfaceMethodrefConstant.class);

        // Act
        simplifier.visitAnyConstant(mockClazz, classConstant);
        simplifier.visitAnyConstant(mockClazz, fieldrefConstant);
        simplifier.visitAnyConstant(mockClazz, methodrefConstant);
        simplifier.visitAnyConstant(mockClazz, interfaceMethodrefConstant);

        // Assert - no interactions with any constant or the extra visitor
        verifyNoInteractions(classConstant);
        verifyNoInteractions(fieldrefConstant);
        verifyNoInteractions(methodrefConstant);
        verifyNoInteractions(interfaceMethodrefConstant);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyConstant does not inspect or modify the constant parameter.
     * Verifies complete isolation from the constant.
     */
    @Test
    public void testVisitAnyConstant_doesNotInspectConstant() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Constant spyConstant = mock(Constant.class);

        // Act
        simplifier.visitAnyConstant(mockClazz, spyConstant);

        // Assert - the constant should not have any method calls made on it
        verifyNoInteractions(spyConstant);
    }

    /**
     * Tests visitAnyConstant in the context of being part of the ConstantVisitor interface.
     * Verifies that it properly implements the interface contract as a no-op.
     */
    @Test
    public void testVisitAnyConstant_asConstantVisitor_properNoOpImplementation() {
        // Arrange
        WrapperClassUseSimplifier visitor = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act & Assert - use as ConstantVisitor interface
        assertDoesNotThrow(() -> visitor.visitAnyConstant(mockClazz, mockConstant),
                "visitAnyConstant should work correctly through ConstantVisitor interface");

        // Assert - no interactions
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyConstant with concurrent-like rapid switching between different parameters.
     * Verifies that the no-op is thread-safe in its statelessness.
     */
    @Test
    public void testVisitAnyConstant_rapidParameterSwitching_remainsStateless() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Constant const1 = mock(Constant.class);
        Constant const2 = mock(Constant.class);

        // Act - rapidly switch between different parameter combinations
        for (int i = 0; i < 100; i++) {
            simplifier.visitAnyConstant(clazz1, const1);
            simplifier.visitAnyConstant(clazz2, const2);
            simplifier.visitAnyConstant(clazz1, const2);
            simplifier.visitAnyConstant(clazz2, const1);
        }

        // Assert - no interactions should occur
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(const1);
        verifyNoInteractions(const2);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyConstant does not interfere with other visitor methods.
     * Verifies that calling visitAnyConstant doesn't affect the state used by other methods.
     */
    @Test
    public void testVisitAnyConstant_doesNotInterfereWithOtherMethods() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call visitAnyConstant
        simplifier.visitAnyConstant(mockClazz, mockConstant);

        // Assert - should still be able to call other methods without issues
        assertDoesNotThrow(() -> {
            simplifier.visitAnyConstant(mockClazz, mockConstant);
        }, "visitAnyConstant should not interfere with subsequent calls");

        // Assert - no interactions
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockConstant);
        verifyNoInteractions(mockExtraVisitor);
    }
}

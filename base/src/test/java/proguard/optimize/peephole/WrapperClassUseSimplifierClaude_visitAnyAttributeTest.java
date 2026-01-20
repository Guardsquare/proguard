package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link WrapperClassUseSimplifier#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method in WrapperClassUseSimplifier is a no-op implementation (empty method body).
 * It's required by the AttributeVisitor interface but intentionally does nothing, as WrapperClassUseSimplifier
 * only operates on CodeAttribute instances via the visitCodeAttribute method.
 *
 * These tests verify that the method:
 * 1. Does not throw exceptions when called
 * 2. Does not modify the attribute or simplifier state
 * 3. Does not trigger the extra instruction visitor
 * 4. Works correctly with different types of parameters
 */
public class WrapperClassUseSimplifierClaude_visitAnyAttributeTest {

    private WrapperClassUseSimplifier simplifier;
    private InstructionVisitor mockExtraVisitor;
    private Clazz mockClazz;
    private Attribute mockAttribute;

    @BeforeEach
    public void setUp() {
        mockExtraVisitor = mock(InstructionVisitor.class);
        mockClazz = mock(Clazz.class);
        mockAttribute = mock(Attribute.class);
    }

    // ========================================
    // visitAnyAttribute Tests - No-op Verification
    // ========================================

    /**
     * Tests visitAnyAttribute with a WrapperClassUseSimplifier that has no extra visitor.
     * The method should do nothing (no-op) and not throw any exceptions.
     */
    @Test
    public void testVisitAnyAttribute_withNullExtraVisitor_doesNothing() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyAttribute(mockClazz, mockAttribute),
                "visitAnyAttribute should not throw exceptions");

        // Assert - no interactions with the attribute or clazz
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyAttribute with a WrapperClassUseSimplifier that has an extra visitor.
     * The method should do nothing and not trigger the extra visitor.
     */
    @Test
    public void testVisitAnyAttribute_withExtraVisitor_doesNotTriggerVisitor() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        assertDoesNotThrow(() -> simplifier.visitAnyAttribute(mockClazz, mockAttribute),
                "visitAnyAttribute should not throw exceptions");

        // Assert - extra visitor should not be called
        verifyNoInteractions(mockExtraVisitor);
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyAttribute with no-arg constructor.
     * The method should do nothing regardless of constructor variant used.
     */
    @Test
    public void testVisitAnyAttribute_withNoArgConstructor_doesNothing() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier();

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyAttribute(mockClazz, mockAttribute),
                "visitAnyAttribute should not throw exceptions with no-arg constructor");

        // Assert - no interactions with the attribute or clazz
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyAttribute with null parameters.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullParameters_doesNotThrow() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyAttribute(null, null),
                "visitAnyAttribute should handle null parameters without throwing");
    }

    /**
     * Tests visitAnyAttribute with null clazz but non-null attribute.
     * The method should handle this gracefully.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrow() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyAttribute(null, mockAttribute),
                "visitAnyAttribute should handle null clazz");
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyAttribute with non-null clazz but null attribute.
     * The method should handle this gracefully.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrow() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitAnyAttribute(mockClazz, null),
                "visitAnyAttribute should handle null attribute");
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times without side effects.
     * Each call should remain a no-op.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_remainsNoOp() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call multiple times
        simplifier.visitAnyAttribute(mockClazz, mockAttribute);
        simplifier.visitAnyAttribute(mockClazz, mockAttribute);
        simplifier.visitAnyAttribute(mockClazz, mockAttribute);

        // Assert - no interactions should occur even after multiple calls
        verifyNoInteractions(mockExtraVisitor);
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyAttribute with different attribute instances.
     * The no-op behavior should be consistent regardless of the attribute.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributes_alwaysNoOp() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Attribute mockAttribute1 = mock(Attribute.class);
        Attribute mockAttribute2 = mock(Attribute.class);
        Attribute mockAttribute3 = mock(Attribute.class);

        // Act
        simplifier.visitAnyAttribute(mockClazz, mockAttribute1);
        simplifier.visitAnyAttribute(mockClazz, mockAttribute2);
        simplifier.visitAnyAttribute(mockClazz, mockAttribute3);

        // Assert - no interactions with any attribute or the extra visitor
        verifyNoInteractions(mockAttribute1);
        verifyNoInteractions(mockAttribute2);
        verifyNoInteractions(mockAttribute3);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyAttribute with different clazz instances.
     * The no-op behavior should be consistent regardless of the clazz.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClasses_alwaysNoOp() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Clazz mockClazz1 = mock(Clazz.class);
        Clazz mockClazz2 = mock(Clazz.class);
        ProgramClass programClass = new ProgramClass();

        // Act
        simplifier.visitAnyAttribute(mockClazz1, mockAttribute);
        simplifier.visitAnyAttribute(mockClazz2, mockAttribute);
        simplifier.visitAnyAttribute(programClass, mockAttribute);

        // Assert - no interactions
        verifyNoInteractions(mockClazz1);
        verifyNoInteractions(mockClazz2);
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that multiple WrapperClassUseSimplifier instances with visitAnyAttribute
     * don't interfere with each other.
     */
    @Test
    public void testVisitAnyAttribute_multipleInstances_independent() {
        // Arrange
        InstructionVisitor mockVisitor1 = mock(InstructionVisitor.class);
        InstructionVisitor mockVisitor2 = mock(InstructionVisitor.class);
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(mockVisitor1);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(mockVisitor2);

        // Act
        simplifier1.visitAnyAttribute(mockClazz, mockAttribute);
        simplifier2.visitAnyAttribute(mockClazz, mockAttribute);

        // Assert - no interactions with any visitor
        verifyNoInteractions(mockVisitor1);
        verifyNoInteractions(mockVisitor2);
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyAttribute does not affect subsequent operations.
     * Calling visitAnyAttribute should not interfere with the simplifier's state.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSimplifierState() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call visitAnyAttribute first
        simplifier.visitAnyAttribute(mockClazz, mockAttribute);

        // Try calling it again to ensure state is not corrupted
        assertDoesNotThrow(() -> simplifier.visitAnyAttribute(mockClazz, mockAttribute),
                "Second call should also be a no-op");

        // Assert - still no interactions
        verifyNoInteractions(mockExtraVisitor);
        verifyNoInteractions(mockAttribute);
    }

    /**
     * Tests that visitAnyAttribute truly does nothing by verifying
     * it doesn't prepare or affect any internal state for code simplification operations.
     */
    @Test
    public void testVisitAnyAttribute_doesNotPrepareCodeSimplification() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call visitAnyAttribute (which should do nothing)
        simplifier.visitAnyAttribute(mockClazz, mockAttribute);

        // Assert - verify the attribute and clazz are not examined or modified
        // The no-op should mean absolutely no method calls on any mock
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyAttribute maintains no-op behavior regardless of
     * the number or variety of parameters passed.
     */
    @Test
    public void testVisitAnyAttribute_consistentNoOpBehavior() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Create various mock instances
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);

        // Act - call with various combinations
        assertDoesNotThrow(() -> {
            simplifier.visitAnyAttribute(clazz1, attr1);
            simplifier.visitAnyAttribute(clazz1, attr2);
            simplifier.visitAnyAttribute(clazz2, attr1);
            simplifier.visitAnyAttribute(clazz2, attr2);
            simplifier.visitAnyAttribute(null, null);
        }, "visitAnyAttribute should always be a no-op");

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz1, clazz2, attr1, attr2, mockExtraVisitor);
    }

    /**
     * Tests visitAnyAttribute with both constructor variants.
     * The no-op behavior should be consistent regardless of which constructor was used.
     */
    @Test
    public void testVisitAnyAttribute_withBothConstructors_consistentBehavior() {
        // Arrange
        WrapperClassUseSimplifier simplifierNoArg = new WrapperClassUseSimplifier();
        WrapperClassUseSimplifier simplifierWithVisitor = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        simplifierNoArg.visitAnyAttribute(mockClazz, mockAttribute);
        simplifierWithVisitor.visitAnyAttribute(mockClazz, mockAttribute);

        // Assert - both should be no-ops
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyAttribute does not modify any wrapper class simplification state.
     * It should truly be a no-op, not preparing for any wrapper class use simplification.
     */
    @Test
    public void testVisitAnyAttribute_doesNotPrepareWrapperSimplification() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0;

        // Act - call visitAnyAttribute (which should do nothing)
        simplifier.visitAnyAttribute(programClass, mockAttribute);

        // Assert - since visitAnyAttribute does nothing, the program class should be
        // completely unmodified and no simplification should have been initiated
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockExtraVisitor);
        assertEquals(0, programClass.u2accessFlags,
                "visitAnyAttribute should not prepare or initiate any simplification");
    }

    /**
     * Tests visitAnyAttribute with rapid successive calls.
     * Verifies that the no-op is truly stateless and efficient.
     */
    @Test
    public void testVisitAnyAttribute_rapidSuccessiveCalls_remainsEfficient() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call many times rapidly
        for (int i = 0; i < 1000; i++) {
            simplifier.visitAnyAttribute(mockClazz, mockAttribute);
        }

        // Assert - should still have no interactions
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that visitAnyAttribute completes quickly even with many calls.
     * Verifies the no-op implementation is truly efficient.
     */
    @Test
    public void testVisitAnyAttribute_performance_completesQuickly() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 10000; i++) {
            simplifier.visitAnyAttribute(mockClazz, mockAttribute);
        }

        // Assert - should complete very quickly (less than 100ms for 10000 calls)
        long duration = System.nanoTime() - startTime;
        assertTrue(duration < 100_000_000L,
                "10000 calls to visitAnyAttribute should complete in under 100ms (took " + duration + " ns)");
    }

    /**
     * Tests visitAnyAttribute returns normally (void method completes).
     * Verifies that the method signature is correct and completes execution.
     */
    @Test
    public void testVisitAnyAttribute_returnsNormally() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert - should complete without exceptions or blocking
        assertDoesNotThrow(() -> {
            simplifier.visitAnyAttribute(mockClazz, mockAttribute);
            // If we reach this point, the method returned normally
        }, "visitAnyAttribute should return normally");
    }

    /**
     * Tests that visitAnyAttribute is truly empty and doesn't execute hidden logic.
     * By verifying no state changes occur in various scenarios.
     */
    @Test
    public void testVisitAnyAttribute_trulyEmpty_noHiddenLogic() {
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
        simplifier.visitAnyAttribute(programClass, mockAttribute);

        // Assert - verify all fields remain unchanged
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
                "Access flags should not be modified");
        assertEquals(originalVersion, programClass.u4version,
                "Version should not be modified");
        assertEquals(originalThisClass, programClass.u2thisClass,
                "This class index should not be modified");
        assertEquals(originalSuperClass, programClass.u2superClass,
                "Super class index should not be modified");
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyAttribute with alternating simplifier instances.
     * The no-op should work consistently regardless of instance alternation.
     */
    @Test
    public void testVisitAnyAttribute_withAlternatingInstances_consistentNoOp() {
        // Arrange
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(mockExtraVisitor);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(null);

        // Act - alternate between instances
        simplifier1.visitAnyAttribute(mockClazz, mockAttribute);
        simplifier2.visitAnyAttribute(mockClazz, mockAttribute);
        simplifier1.visitAnyAttribute(mockClazz, mockAttribute);
        simplifier2.visitAnyAttribute(mockClazz, mockAttribute);

        // Assert - should have no interactions
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests visitAnyAttribute with sequential different attribute and class instances.
     * Verifies that the no-op is stateless across different instances.
     */
    @Test
    public void testVisitAnyAttribute_withSequentialDifferentInstances_stateless() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        Clazz[] classes = new Clazz[5];
        Attribute[] attributes = new Attribute[5];

        for (int i = 0; i < 5; i++) {
            classes[i] = mock(Clazz.class);
            attributes[i] = mock(Attribute.class);
        }

        // Act
        for (int i = 0; i < 5; i++) {
            simplifier.visitAnyAttribute(classes[i], attributes[i]);
        }

        // Assert - all should have no interactions
        for (int i = 0; i < 5; i++) {
            verifyNoInteractions(classes[i]);
            verifyNoInteractions(attributes[i]);
        }
        verifyNoInteractions(mockExtraVisitor);
    }
}

package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GotoCommonCodeReplacer#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method in GotoCommonCodeReplacer is a no-op implementation (empty method body).
 * It's required by the AttributeVisitor interface but intentionally does nothing, as GotoCommonCodeReplacer
 * only operates on CodeAttribute instances via the visitCodeAttribute method.
 *
 * These tests verify that the method:
 * 1. Does not throw exceptions when called
 * 2. Does not modify the attribute or replacer state
 * 3. Does not trigger the extra instruction visitor
 * 4. Works correctly with different types of parameters
 */
public class GotoCommonCodeReplacerClaude_visitAnyAttributeTest {

    private GotoCommonCodeReplacer replacer;
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
     * Tests visitAnyAttribute with a GotoCommonCodeReplacer that has no extra visitor.
     * The method should do nothing (no-op) and not throw any exceptions.
     */
    @Test
    public void testVisitAnyAttribute_withNullExtraVisitor_doesNothing() {
        // Arrange
        replacer = new GotoCommonCodeReplacer(null);

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyAttribute(mockClazz, mockAttribute),
                "visitAnyAttribute should not throw exceptions");

        // Assert - no interactions with the attribute or clazz
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests visitAnyAttribute with a GotoCommonCodeReplacer that has an extra visitor.
     * The method should do nothing and not trigger the extra visitor.
     */
    @Test
    public void testVisitAnyAttribute_withExtraVisitor_doesNotTriggerVisitor() {
        // Arrange
        replacer = new GotoCommonCodeReplacer(mockExtraVisitor);

        // Act
        assertDoesNotThrow(() -> replacer.visitAnyAttribute(mockClazz, mockAttribute),
                "visitAnyAttribute should not throw exceptions");

        // Assert - extra visitor should not be called
        verifyNoInteractions(mockExtraVisitor);
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
        replacer = new GotoCommonCodeReplacer(null);

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyAttribute(null, null),
                "visitAnyAttribute should handle null parameters without throwing");
    }

    /**
     * Tests visitAnyAttribute with null clazz but non-null attribute.
     * The method should handle this gracefully.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrow() {
        // Arrange
        replacer = new GotoCommonCodeReplacer(mockExtraVisitor);

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyAttribute(null, mockAttribute),
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
        replacer = new GotoCommonCodeReplacer(mockExtraVisitor);

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyAttribute(mockClazz, null),
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
        replacer = new GotoCommonCodeReplacer(mockExtraVisitor);

        // Act - call multiple times
        replacer.visitAnyAttribute(mockClazz, mockAttribute);
        replacer.visitAnyAttribute(mockClazz, mockAttribute);
        replacer.visitAnyAttribute(mockClazz, mockAttribute);

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
        replacer = new GotoCommonCodeReplacer(mockExtraVisitor);
        Attribute mockAttribute1 = mock(Attribute.class);
        Attribute mockAttribute2 = mock(Attribute.class);
        Attribute mockAttribute3 = mock(Attribute.class);

        // Act
        replacer.visitAnyAttribute(mockClazz, mockAttribute1);
        replacer.visitAnyAttribute(mockClazz, mockAttribute2);
        replacer.visitAnyAttribute(mockClazz, mockAttribute3);

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
        replacer = new GotoCommonCodeReplacer(mockExtraVisitor);
        Clazz mockClazz1 = mock(Clazz.class);
        Clazz mockClazz2 = mock(Clazz.class);
        ProgramClass programClass = new ProgramClass();

        // Act
        replacer.visitAnyAttribute(mockClazz1, mockAttribute);
        replacer.visitAnyAttribute(mockClazz2, mockAttribute);
        replacer.visitAnyAttribute(programClass, mockAttribute);

        // Assert - no interactions
        verifyNoInteractions(mockClazz1);
        verifyNoInteractions(mockClazz2);
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that multiple GotoCommonCodeReplacer instances with visitAnyAttribute
     * don't interfere with each other.
     */
    @Test
    public void testVisitAnyAttribute_multipleInstances_independent() {
        // Arrange
        InstructionVisitor mockVisitor1 = mock(InstructionVisitor.class);
        InstructionVisitor mockVisitor2 = mock(InstructionVisitor.class);
        GotoCommonCodeReplacer replacer1 = new GotoCommonCodeReplacer(mockVisitor1);
        GotoCommonCodeReplacer replacer2 = new GotoCommonCodeReplacer(mockVisitor2);

        // Act
        replacer1.visitAnyAttribute(mockClazz, mockAttribute);
        replacer2.visitAnyAttribute(mockClazz, mockAttribute);

        // Assert - no interactions with any visitor
        verifyNoInteractions(mockVisitor1);
        verifyNoInteractions(mockVisitor2);
        verifyNoInteractions(mockAttribute);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyAttribute does not affect subsequent operations.
     * Calling visitAnyAttribute should not interfere with the replacer's state.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectReplacerState() {
        // Arrange
        replacer = new GotoCommonCodeReplacer(mockExtraVisitor);

        // Act - call visitAnyAttribute first
        replacer.visitAnyAttribute(mockClazz, mockAttribute);

        // Try calling it again to ensure state is not corrupted
        assertDoesNotThrow(() -> replacer.visitAnyAttribute(mockClazz, mockAttribute),
                "Second call should also be a no-op");

        // Assert - still no interactions
        verifyNoInteractions(mockExtraVisitor);
        verifyNoInteractions(mockAttribute);
    }

    /**
     * Tests that visitAnyAttribute truly does nothing by verifying
     * it doesn't prepare or affect any internal state for code replacement operations.
     */
    @Test
    public void testVisitAnyAttribute_doesNotPrepareCodeReplacement() {
        // Arrange
        replacer = new GotoCommonCodeReplacer(mockExtraVisitor);

        // Act - call visitAnyAttribute (which should do nothing)
        replacer.visitAnyAttribute(mockClazz, mockAttribute);

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
        replacer = new GotoCommonCodeReplacer(mockExtraVisitor);

        // Create various mock instances
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);

        // Act - call with various combinations
        assertDoesNotThrow(() -> {
            replacer.visitAnyAttribute(clazz1, attr1);
            replacer.visitAnyAttribute(clazz1, attr2);
            replacer.visitAnyAttribute(clazz2, attr1);
            replacer.visitAnyAttribute(clazz2, attr2);
            replacer.visitAnyAttribute(null, null);
        }, "visitAnyAttribute should always be a no-op");

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz1, clazz2, attr1, attr2, mockExtraVisitor);
    }
}

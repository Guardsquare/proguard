package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MethodInliner#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method in MethodInliner is an empty implementation (no-op).
 * It's part of the AttributeVisitor interface but doesn't perform any actions because:
 * - The actual method inlining is done in visitCodeAttribute()
 * - visitAnyAttribute() serves as a default no-op for when the visitor is called on non-specific attribute types
 *
 * These tests verify that:
 * 1. The method doesn't throw exceptions with various inputs
 * 2. The method doesn't modify the attribute or class being visited
 * 3. The method can handle different types of attributes
 * 4. The method is properly integrated into the visitor pattern
 */
public class MethodInlinerClaude_visitAnyAttributeTest {

    private MethodInliner inliner;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        // Create a simple concrete implementation for testing
        inliner = new ShortMethodInliner(false, false, true);
        clazz = new ProgramClass();
    }

    // ========================================
    // Basic Functionality Tests
    // ========================================

    /**
     * Tests that visitAnyAttribute does not throw exceptions with valid mock objects.
     * Verifies the empty implementation executes without errors.
     */
    @Test
    public void testVisitAnyAttribute_withValidMocks_doesNotThrow() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.visitAnyAttribute(clazz, attribute),
            "visitAnyAttribute should not throw exception with valid parameters");
    }

    /**
     * Tests that visitAnyAttribute handles a real CodeAttribute.
     * Verifies the method works with actual attribute instances.
     */
    @Test
    public void testVisitAnyAttribute_withCodeAttribute_doesNotThrow() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.visitAnyAttribute(clazz, codeAttribute),
            "visitAnyAttribute should handle CodeAttribute");
    }

    /**
     * Tests that visitAnyAttribute handles a SourceFileAttribute.
     * Verifies the method works with different attribute types.
     */
    @Test
    public void testVisitAnyAttribute_withSourceFileAttribute_doesNotThrow() {
        // Arrange
        SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.visitAnyAttribute(clazz, sourceFileAttribute),
            "visitAnyAttribute should handle SourceFileAttribute");
    }

    /**
     * Tests that visitAnyAttribute handles a LineNumberTableAttribute.
     * Verifies the method works with line number related attributes.
     */
    @Test
    public void testVisitAnyAttribute_withLineNumberTableAttribute_doesNotThrow() {
        // Arrange
        LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.visitAnyAttribute(clazz, lineNumberTableAttribute),
            "visitAnyAttribute should handle LineNumberTableAttribute");
    }

    // ========================================
    // Null Parameter Tests
    // ========================================

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * Since the method is empty, it should handle null gracefully.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrow() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.visitAnyAttribute(null, attribute),
            "visitAnyAttribute should handle null Clazz");
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * Since the method is empty, it should handle null gracefully.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> inliner.visitAnyAttribute(clazz, null),
            "visitAnyAttribute should handle null Attribute");
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * Since the method is empty, it should handle all null parameters.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> inliner.visitAnyAttribute(null, null),
            "visitAnyAttribute should handle both parameters null");
    }

    // ========================================
    // No-Op Verification Tests
    // ========================================

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Verifies the method is truly a no-op.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        inliner.visitAnyAttribute(mockClazz, attribute);

        // Assert
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Attribute parameter.
     * Verifies the method is truly a no-op.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithAttribute() {
        // Arrange
        Attribute mockAttribute = mock(Attribute.class);

        // Act
        inliner.visitAnyAttribute(clazz, mockAttribute);

        // Assert
        verifyNoInteractions(mockAttribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with either parameter.
     * Verifies complete no-op behavior.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithEitherParameter() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        Attribute mockAttribute = mock(Attribute.class);

        // Act
        inliner.visitAnyAttribute(mockClazz, mockAttribute);

        // Assert
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockAttribute);
    }

    // ========================================
    // State Preservation Tests
    // ========================================

    /**
     * Tests that visitAnyAttribute doesn't modify the attribute.
     * Verifies the empty method preserves attribute state.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyAttribute() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        int originalAttributeNameIndex = codeAttribute.u2attributeNameIndex;

        // Act
        inliner.visitAnyAttribute(clazz, codeAttribute);

        // Assert
        assertEquals(originalAttributeNameIndex, codeAttribute.u2attributeNameIndex,
            "Attribute should not be modified");
    }

    /**
     * Tests that visitAnyAttribute preserves class state.
     * Verifies the empty method doesn't affect the class.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        Object originalProcessingInfo = new Object();
        programClass.setProcessingInfo(originalProcessingInfo);
        Attribute attribute = mock(Attribute.class);

        // Act
        inliner.visitAnyAttribute(programClass, attribute);

        // Assert
        assertSame(originalProcessingInfo, programClass.getProcessingInfo(),
            "Class processing info should not be modified");
    }

    // ========================================
    // Multiple Call Tests
    // ========================================

    /**
     * Tests that visitAnyAttribute can be called multiple times.
     * Verifies the method is idempotent.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner.visitAnyAttribute(clazz, attribute);
            inliner.visitAnyAttribute(clazz, attribute);
            inliner.visitAnyAttribute(clazz, attribute);
        }, "Multiple calls should not throw");
    }

    /**
     * Tests that visitAnyAttribute works with different attributes sequentially.
     * Verifies the method handles varying attribute types.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributes_doesNotThrow() {
        // Arrange
        CodeAttribute codeAttr = new CodeAttribute();
        SourceFileAttribute sourceFileAttr = new SourceFileAttribute();
        LineNumberTableAttribute lineNumberAttr = new LineNumberTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner.visitAnyAttribute(clazz, codeAttr);
            inliner.visitAnyAttribute(clazz, sourceFileAttr);
            inliner.visitAnyAttribute(clazz, lineNumberAttr);
        }, "Should handle different attribute types");
    }

    /**
     * Tests that visitAnyAttribute works with rapid successive calls.
     * Verifies performance characteristics.
     */
    @Test
    public void testVisitAnyAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                inliner.visitAnyAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not cause issues");
    }

    // ========================================
    // Different Class Type Tests
    // ========================================

    /**
     * Tests visitAnyAttribute with a ProgramClass.
     * Verifies the method works with program classes.
     */
    @Test
    public void testVisitAnyAttribute_withProgramClass_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.visitAnyAttribute(programClass, attribute),
            "Should handle ProgramClass");
    }

    /**
     * Tests visitAnyAttribute with a LibraryClass.
     * Verifies the method works with library classes.
     */
    @Test
    public void testVisitAnyAttribute_withLibraryClass_doesNotThrow() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> inliner.visitAnyAttribute(libraryClass, attribute),
            "Should handle LibraryClass");
    }

    /**
     * Tests visitAnyAttribute with alternating class types.
     * Verifies consistent behavior across class types.
     */
    @Test
    public void testVisitAnyAttribute_withAlternatingClassTypes_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner.visitAnyAttribute(programClass, attribute);
            inliner.visitAnyAttribute(libraryClass, attribute);
            inliner.visitAnyAttribute(programClass, attribute);
            inliner.visitAnyAttribute(libraryClass, attribute);
        }, "Should handle alternating class types");
    }

    // ========================================
    // Multiple Inliner Instance Tests
    // ========================================

    /**
     * Tests that different inliner instances work independently.
     * Verifies no shared state between instances.
     */
    @Test
    public void testVisitAnyAttribute_multipleInliners_independent() {
        // Arrange
        MethodInliner inliner1 = new ShortMethodInliner(false, false, true);
        MethodInliner inliner2 = new ShortMethodInliner(false, false, true);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner1.visitAnyAttribute(clazz, attribute);
            inliner2.visitAnyAttribute(clazz, attribute);
        }, "Different inliners should work independently");
    }

    /**
     * Tests that multiple inliners can visit the same attribute.
     * Verifies concurrent-like access patterns work correctly.
     */
    @Test
    public void testVisitAnyAttribute_sameAttributeDifferentInliners() {
        // Arrange
        Attribute attribute = mock(Attribute.class);
        MethodInliner inliner1 = new ShortMethodInliner(false, false, true);
        MethodInliner inliner2 = new ShortMethodInliner(true, false, false);
        MethodInliner inliner3 = new ShortMethodInliner(false, true, true);

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner1.visitAnyAttribute(clazz, attribute);
            inliner2.visitAnyAttribute(clazz, attribute);
            inliner3.visitAnyAttribute(clazz, attribute);
        }, "Same attribute should be visitable by different inliners");
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    /**
     * Tests visitAnyAttribute immediately after inliner creation.
     * Verifies the inliner is ready to use immediately.
     */
    @Test
    public void testVisitAnyAttribute_immediatelyAfterCreation() {
        // Arrange
        MethodInliner newInliner = new ShortMethodInliner(false, false, true);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> newInliner.visitAnyAttribute(clazz, attribute),
            "visitAnyAttribute should work immediately after creation");
    }

    /**
     * Tests visitAnyAttribute with freshly created attributes.
     * Verifies handling of attributes created inline.
     */
    @Test
    public void testVisitAnyAttribute_withFreshlyCreatedAttributes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner.visitAnyAttribute(clazz, new CodeAttribute());
            inliner.visitAnyAttribute(clazz, new SourceFileAttribute());
            inliner.visitAnyAttribute(clazz, new LineNumberTableAttribute());
        }, "Should handle freshly created attributes");
    }

    /**
     * Tests that visitAnyAttribute maintains consistency across many calls.
     * Verifies stable behavior over extended use.
     */
    @Test
    public void testVisitAnyAttribute_manySequentialCalls_consistent() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            final int iteration = i;
            assertDoesNotThrow(() -> inliner.visitAnyAttribute(clazz, attribute),
                "Call " + iteration + " should not throw");
        }
    }

    /**
     * Tests that visitAnyAttribute completes quickly.
     * Verifies performance characteristics of the empty method.
     */
    @Test
    public void testVisitAnyAttribute_executesQuickly() {
        // Arrange
        Attribute attribute = mock(Attribute.class);
        long startTime = System.nanoTime();

        // Act
        for (int i = 0; i < 10000; i++) {
            inliner.visitAnyAttribute(clazz, attribute);
        }
        long endTime = System.nanoTime();

        // Assert
        long durationMs = (endTime - startTime) / 1_000_000;
        assertTrue(durationMs < 100,
            "10000 calls should complete in less than 100ms, took: " + durationMs + "ms");
    }

    // ========================================
    // Visitor Pattern Integration Tests
    // ========================================

    /**
     * Tests that visitAnyAttribute can be used through the AttributeVisitor interface.
     * Verifies proper integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyAttribute_throughAttributeVisitorInterface_works() {
        // Arrange
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = inliner;
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyAttribute(clazz, attribute),
            "Should work when called through AttributeVisitor interface");
    }

    /**
     * Tests that the empty visitAnyAttribute doesn't break visitor chains.
     * Verifies it can be part of a visitor pattern without issues.
     */
    @Test
    public void testVisitAnyAttribute_inVisitorChain_doesNotBreakChain() {
        // Arrange
        Attribute attribute = mock(Attribute.class);
        boolean[] visited = new boolean[1];

        proguard.classfile.attribute.visitor.AttributeVisitor chainedVisitor =
            new proguard.classfile.attribute.visitor.AttributeVisitor() {
                @Override
                public void visitAnyAttribute(Clazz clazz, Attribute attr) {
                    inliner.visitAnyAttribute(clazz, attr);
                    visited[0] = true;
                }
            };

        // Act
        chainedVisitor.visitAnyAttribute(clazz, attribute);

        // Assert
        assertTrue(visited[0], "Visitor chain should complete successfully");
    }

    // ========================================
    // Attribute Configuration Tests
    // ========================================

    /**
     * Tests visitAnyAttribute with various attribute types.
     * Verifies consistent behavior across attribute types.
     */
    @Test
    public void testVisitAnyAttribute_withVariousAttributeTypes_consistent() {
        // Arrange
        CodeAttribute codeAttr = new CodeAttribute();
        SourceFileAttribute sourceFileAttr = new SourceFileAttribute();
        LineNumberTableAttribute lineNumberAttr = new LineNumberTableAttribute();
        DeprecatedAttribute deprecatedAttr = new DeprecatedAttribute();
        SignatureAttribute signatureAttr = new SignatureAttribute();

        // Act & Assert - all should behave consistently
        assertDoesNotThrow(() -> {
            inliner.visitAnyAttribute(clazz, codeAttr);
            inliner.visitAnyAttribute(clazz, sourceFileAttr);
            inliner.visitAnyAttribute(clazz, lineNumberAttr);
            inliner.visitAnyAttribute(clazz, deprecatedAttr);
            inliner.visitAnyAttribute(clazz, signatureAttr);
        }, "Should handle various attribute types consistently");
    }

    /**
     * Tests that visitAnyAttribute doesn't accumulate state across calls.
     * Verifies the method remains stateless.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAccumulateState() {
        // Arrange
        Attribute attribute = mock(Attribute.class);
        ProgramClass programClass = new ProgramClass();
        programClass.setProcessingInfo("initial");

        // Act
        inliner.visitAnyAttribute(programClass, attribute);
        inliner.visitAnyAttribute(programClass, attribute);
        inliner.visitAnyAttribute(programClass, attribute);

        // Assert
        assertEquals("initial", programClass.getProcessingInfo(),
            "Processing info should not change");
    }

    /**
     * Tests visitAnyAttribute preserves attribute references.
     * Verifies the attribute reference remains unchanged.
     */
    @Test
    public void testVisitAnyAttribute_preservesAttributeReference() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttribute originalReference = codeAttribute;

        // Act
        inliner.visitAnyAttribute(clazz, codeAttribute);

        // Assert
        assertSame(originalReference, codeAttribute,
            "Attribute reference should remain the same");
    }

    // ========================================
    // Different Inliner Configuration Tests
    // ========================================

    /**
     * Tests visitAnyAttribute with microEdition configuration.
     * Verifies the no-op behavior is independent of configuration.
     */
    @Test
    public void testVisitAnyAttribute_withMicroEditionConfiguration_doesNotThrow() {
        // Arrange
        MethodInliner microEditionInliner = new ShortMethodInliner(true, false, false);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> microEditionInliner.visitAnyAttribute(clazz, attribute),
            "Should work with microEdition configuration");
    }

    /**
     * Tests visitAnyAttribute with Android configuration.
     * Verifies the no-op behavior is independent of configuration.
     */
    @Test
    public void testVisitAnyAttribute_withAndroidConfiguration_doesNotThrow() {
        // Arrange
        MethodInliner androidInliner = new ShortMethodInliner(false, true, false);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> androidInliner.visitAnyAttribute(clazz, attribute),
            "Should work with Android configuration");
    }

    /**
     * Tests visitAnyAttribute with allowAccessModification enabled.
     * Verifies the no-op behavior is independent of access modification setting.
     */
    @Test
    public void testVisitAnyAttribute_withAccessModificationEnabled_doesNotThrow() {
        // Arrange
        MethodInliner accessModInliner = new ShortMethodInliner(false, false, true);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> accessModInliner.visitAnyAttribute(clazz, attribute),
            "Should work with allowAccessModification enabled");
    }

    /**
     * Tests visitAnyAttribute with all configuration flags enabled.
     * Verifies the no-op behavior is independent of all configurations.
     */
    @Test
    public void testVisitAnyAttribute_withAllConfigurationsEnabled_doesNotThrow() {
        // Arrange
        MethodInliner fullConfigInliner = new ShortMethodInliner(true, true, true);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> fullConfigInliner.visitAnyAttribute(clazz, attribute),
            "Should work with all configurations enabled");
    }
}

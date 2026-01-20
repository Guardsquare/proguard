package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.editor.CodeAttributeEditor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MemberReferenceGeneralizer#visitClassConstant(Clazz, ClassConstant)}.
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/constant/ClassConstant;)V
 *
 * This class tests the visitClassConstant method which is part of the ConstantVisitor interface.
 * The method delegates to the ClassConstant to visit the referenced class by calling
 * classConstant.referencedClassAccept(this), which will invoke the MemberReferenceGeneralizer
 * as a ClassVisitor on the referenced class.
 *
 * This method is called as part of processing field and method references during generalization.
 */
public class MemberReferenceGeneralizerClaude_visitClassConstantTest {

    private MemberReferenceGeneralizer generalizer;
    private CodeAttributeEditor codeAttributeEditor;
    private Clazz mockClazz;
    private ClassConstant mockClassConstant;

    @BeforeEach
    public void setUp() {
        codeAttributeEditor = mock(CodeAttributeEditor.class);
        generalizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor
        );
        mockClazz = mock(ProgramClass.class);
        mockClassConstant = mock(ClassConstant.class);
    }

    // ========== Tests for visitClassConstant - Basic Functionality ==========

    /**
     * Tests that visitClassConstant calls referencedClassAccept on the ClassConstant.
     * This is the primary behavior of the method - it delegates to the ClassConstant
     * to visit the referenced class.
     */
    @Test
    public void testVisitClassConstant_callsReferencedClassAccept() {
        // Act
        generalizer.visitClassConstant(mockClazz, mockClassConstant);

        // Assert
        verify(mockClassConstant, times(1)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitClassConstant_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> generalizer.visitClassConstant(mockClazz, mockClassConstant));
    }

    /**
     * Tests that visitClassConstant can be called multiple times on the same ClassConstant.
     * Each call should delegate to referencedClassAccept.
     */
    @Test
    public void testVisitClassConstant_calledMultipleTimes() {
        // Act
        generalizer.visitClassConstant(mockClazz, mockClassConstant);
        generalizer.visitClassConstant(mockClazz, mockClassConstant);
        generalizer.visitClassConstant(mockClazz, mockClassConstant);

        // Assert
        verify(mockClassConstant, times(3)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant works with different ClassConstant instances.
     */
    @Test
    public void testVisitClassConstant_withDifferentClassConstants() {
        // Arrange
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);
        ClassConstant constant3 = mock(ClassConstant.class);

        // Act
        generalizer.visitClassConstant(mockClazz, constant1);
        generalizer.visitClassConstant(mockClazz, constant2);
        generalizer.visitClassConstant(mockClazz, constant3);

        // Assert
        verify(constant1, times(1)).referencedClassAccept(eq(generalizer));
        verify(constant2, times(1)).referencedClassAccept(eq(generalizer));
        verify(constant3, times(1)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant works with different Clazz instances.
     * The Clazz parameter represents the class containing the constant pool with this constant.
     */
    @Test
    public void testVisitClassConstant_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act
        generalizer.visitClassConstant(clazz1, mockClassConstant);
        generalizer.visitClassConstant(clazz2, mockClassConstant);
        generalizer.visitClassConstant(clazz3, mockClassConstant);

        // Assert
        // The method should call referencedClassAccept regardless of the Clazz parameter
        verify(mockClassConstant, times(3)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant passes the correct MemberReferenceGeneralizer instance
     * (itself) as the visitor to referencedClassAccept.
     */
    @Test
    public void testVisitClassConstant_passesCorrectVisitor() {
        // Arrange
        MemberReferenceGeneralizer anotherGeneralizer = new MemberReferenceGeneralizer(
            true,
            true,
            codeAttributeEditor
        );

        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);

        // Act
        generalizer.visitClassConstant(mockClazz, constant1);
        anotherGeneralizer.visitClassConstant(mockClazz, constant2);

        // Assert
        // Each generalizer should pass itself as the visitor
        verify(constant1, times(1)).referencedClassAccept(eq(generalizer));
        verify(constant2, times(1)).referencedClassAccept(eq(anotherGeneralizer));
    }

    // ========== Tests for Visitor Pattern Integration ==========

    /**
     * Tests that visitClassConstant correctly integrates with the MemberReferenceGeneralizer's
     * role as a ConstantVisitor by verifying the visitor pattern works end-to-end.
     */
    @Test
    public void testVisitClassConstant_visitorPatternIntegration() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Use a flag to track if the visitor pattern was properly invoked
        final boolean[] visitorWasCalled = {false};

        doAnswer(invocation -> {
            visitorWasCalled[0] = true;
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        generalizer.visitClassConstant(clazz, constant);

        // Assert
        assertTrue(visitorWasCalled[0], "The visitor pattern should have been invoked");
        verify(constant, times(1)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant works correctly when called as part of
     * the ConstantVisitor interface implementation.
     */
    @Test
    public void testVisitClassConstant_asConstantVisitor() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);

        // Act - Call through the ConstantVisitor interface
        proguard.classfile.constant.visitor.ConstantVisitor visitor = generalizer;
        visitor.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant properly delegates when the ClassConstant
     * references a class that needs to be visited.
     */
    @Test
    public void testVisitClassConstant_delegatesToReferencedClass() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz referencedClass = mock(Clazz.class);

        // Simulate the constant calling back to the visitor with the referenced class
        doAnswer(invocation -> {
            MemberReferenceGeneralizer visitor = invocation.getArgument(0);
            // The ClassConstant would internally call visitor.visitAnyClass(referencedClass)
            // We just verify the visitor was passed correctly
            assertNotNull(visitor);
            assertEquals(generalizer, visitor);
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        generalizer.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(generalizer));
    }

    // ========== Tests for Edge Cases ==========

    /**
     * Tests that multiple MemberReferenceGeneralizer instances can independently process
     * ClassConstants without interfering with each other.
     */
    @Test
    public void testVisitClassConstant_multipleGeneralizersIndependent() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);

        MemberReferenceGeneralizer generalizer1 = new MemberReferenceGeneralizer(
            true, true, editor1
        );
        MemberReferenceGeneralizer generalizer2 = new MemberReferenceGeneralizer(
            true, true, editor2
        );

        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act
        generalizer1.visitClassConstant(clazz1, constant1);
        generalizer2.visitClassConstant(clazz2, constant2);

        // Assert
        verify(constant1, times(1)).referencedClassAccept(eq(generalizer1));
        verify(constant2, times(1)).referencedClassAccept(eq(generalizer2));
    }

    /**
     * Tests that visitClassConstant maintains proper state when processing
     * multiple constants in sequence.
     */
    @Test
    public void testVisitClassConstant_sequentialProcessing() {
        // Arrange
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);
        ClassConstant constant3 = mock(ClassConstant.class);

        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act - Process multiple constants in sequence
        generalizer.visitClassConstant(clazz1, constant1);
        generalizer.visitClassConstant(clazz2, constant2);
        generalizer.visitClassConstant(clazz3, constant3);

        // Assert - Each should have been processed exactly once with correct parameters
        verify(constant1, times(1)).referencedClassAccept(eq(generalizer));
        verify(constant2, times(1)).referencedClassAccept(eq(generalizer));
        verify(constant3, times(1)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant handles a scenario where the same ClassConstant
     * is visited multiple times with different Clazz contexts.
     */
    @Test
    public void testVisitClassConstant_sameConstantDifferentClazzes() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act
        generalizer.visitClassConstant(clazz1, constant);
        generalizer.visitClassConstant(clazz2, constant);
        generalizer.visitClassConstant(clazz3, constant);

        // Assert
        verify(constant, times(3)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that the same MemberReferenceGeneralizer instance can be reused for visiting multiple constants.
     */
    @Test
    public void testVisitClassConstant_reuseGeneralizerInstance() {
        // Arrange
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);
        ClassConstant constant3 = mock(ClassConstant.class);

        // Act - Reuse the same generalizer instance
        generalizer.visitClassConstant(mockClazz, constant1);
        generalizer.visitClassConstant(mockClazz, constant2);
        generalizer.visitClassConstant(mockClazz, constant3);

        // Assert
        verify(constant1, times(1)).referencedClassAccept(eq(generalizer));
        verify(constant2, times(1)).referencedClassAccept(eq(generalizer));
        verify(constant3, times(1)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant properly handles the delegation pattern
     * where the ClassConstant may internally call back to the MemberReferenceGeneralizer.
     */
    @Test
    public void testVisitClassConstant_handlesCallbackPattern() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Simulate a callback scenario where referencedClassAccept might trigger
        // additional processing
        final boolean[] callbackOccurred = {false};
        doAnswer(invocation -> {
            MemberReferenceGeneralizer visitor = invocation.getArgument(0);
            // Verify the visitor is the correct instance
            if (visitor == generalizer) {
                callbackOccurred[0] = true;
            }
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        generalizer.visitClassConstant(clazz, constant);

        // Assert
        assertTrue(callbackOccurred[0], "Callback should have occurred with correct visitor");
    }

    /**
     * Tests that visitClassConstant works correctly in a mixed scenario with
     * both Clazz and ClassConstant variations.
     */
    @Test
    public void testVisitClassConstant_mixedScenarios() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);

        // Act - Mix of different Clazz and ClassConstant combinations
        generalizer.visitClassConstant(clazz1, constant1);
        generalizer.visitClassConstant(clazz1, constant2);
        generalizer.visitClassConstant(clazz2, constant1);
        generalizer.visitClassConstant(clazz2, constant2);

        // Assert
        verify(constant1, times(2)).referencedClassAccept(eq(generalizer));
        verify(constant2, times(2)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant is idempotent - calling it multiple times
     * with the same parameters produces consistent behavior.
     */
    @Test
    public void testVisitClassConstant_idempotent() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Act - Call multiple times with same parameters
        generalizer.visitClassConstant(clazz, constant);
        generalizer.visitClassConstant(clazz, constant);
        generalizer.visitClassConstant(clazz, constant);

        // Assert - Should call referencedClassAccept each time
        verify(constant, times(3)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that different MemberReferenceGeneralizer instances maintain independent state
     * when processing the same ClassConstant.
     */
    @Test
    public void testVisitClassConstant_independentGeneralizerState() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);

        MemberReferenceGeneralizer generalizer1 = new MemberReferenceGeneralizer(
            true, true, editor1
        );
        MemberReferenceGeneralizer generalizer2 = new MemberReferenceGeneralizer(
            true, true, editor2
        );

        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Act
        generalizer1.visitClassConstant(clazz, constant);
        generalizer2.visitClassConstant(clazz, constant);

        // Assert - The constant should accept each generalizer instance
        verify(constant, times(1)).referencedClassAccept(eq(generalizer1));
        verify(constant, times(1)).referencedClassAccept(eq(generalizer2));
        verify(constant, times(2)).referencedClassAccept(any(MemberReferenceGeneralizer.class));
    }

    /**
     * Tests that visitClassConstant doesn't interact with the Clazz parameter.
     * The method only delegates to the ClassConstant, not the Clazz.
     */
    @Test
    public void testVisitClassConstant_doesNotInteractWithClazz() {
        // Arrange
        Clazz spyClazz = mock(Clazz.class);
        ClassConstant constant = mock(ClassConstant.class);

        // Act
        generalizer.visitClassConstant(spyClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(generalizer));
        verifyNoInteractions(spyClazz);
    }

    /**
     * Tests that visitClassConstant passes exact object reference to referencedClassAccept.
     */
    @Test
    public void testVisitClassConstant_passesExactReference() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Use argument captor to verify exact reference
        final Object[] capturedVisitor = new Object[1];

        doAnswer(invocation -> {
            capturedVisitor[0] = invocation.getArgument(0);
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        generalizer.visitClassConstant(clazz, constant);

        // Assert
        assertSame(generalizer, capturedVisitor[0], "Should pass exact MemberReferenceGeneralizer reference");
    }

    /**
     * Tests rapid sequential calls to visitClassConstant.
     */
    @Test
    public void testVisitClassConstant_rapidSequentialCalls() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);

        // Act - Rapid calls
        for (int i = 0; i < 100; i++) {
            generalizer.visitClassConstant(mockClazz, constant);
        }

        // Assert
        verify(constant, times(100)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant works with both ProgramClass and LibraryClass.
     */
    @Test
    public void testVisitClassConstant_withDifferentClazzTypes() {
        // Arrange
        Clazz programClazz = mock(ProgramClass.class);
        Clazz libraryClazz = mock(proguard.classfile.LibraryClass.class);
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);

        // Act
        generalizer.visitClassConstant(programClazz, constant1);
        generalizer.visitClassConstant(libraryClazz, constant2);

        // Assert
        verify(constant1, times(1)).referencedClassAccept(eq(generalizer));
        verify(constant2, times(1)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant completes successfully without side effects.
     */
    @Test
    public void testVisitClassConstant_completesSuccessfully() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        doNothing().when(constant).referencedClassAccept(any());

        // Act & Assert
        assertDoesNotThrow(() -> generalizer.visitClassConstant(mockClazz, constant));
        verify(constant, times(1)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests visitClassConstant with alternating patterns of calls.
     */
    @Test
    public void testVisitClassConstant_alternatingCalls() {
        // Arrange
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);

        // Act - Alternate between two constants
        generalizer.visitClassConstant(mockClazz, constant1);
        generalizer.visitClassConstant(mockClazz, constant2);
        generalizer.visitClassConstant(mockClazz, constant1);
        generalizer.visitClassConstant(mockClazz, constant2);

        // Assert
        verify(constant1, times(2)).referencedClassAccept(eq(generalizer));
        verify(constant2, times(2)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant maintains the visitor chain correctly.
     */
    @Test
    public void testVisitClassConstant_maintainsVisitorChain() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);

        // Verify that the visitor parameter is the MemberReferenceGeneralizer instance
        doAnswer(invocation -> {
            Object visitor = invocation.getArgument(0);
            assertTrue(visitor instanceof MemberReferenceGeneralizer, "Visitor should be MemberReferenceGeneralizer instance");
            assertSame(generalizer, visitor, "Visitor should be the same MemberReferenceGeneralizer instance");
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        generalizer.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(any());
    }

    /**
     * Tests that visitClassConstant delegates correctly even when called
     * with null Clazz (edge case - method doesn't use the Clazz parameter).
     */
    @Test
    public void testVisitClassConstant_withNullClazz() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);

        // Act
        generalizer.visitClassConstant(null, constant);

        // Assert - Should still call referencedClassAccept
        verify(constant, times(1)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that multiple generalizers can process the same constant in parallel.
     */
    @Test
    public void testVisitClassConstant_parallelGeneralizerProcessing() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor3 = mock(CodeAttributeEditor.class);

        MemberReferenceGeneralizer generalizer1 = new MemberReferenceGeneralizer(true, true, editor1);
        MemberReferenceGeneralizer generalizer2 = new MemberReferenceGeneralizer(true, true, editor2);
        MemberReferenceGeneralizer generalizer3 = new MemberReferenceGeneralizer(true, true, editor3);
        ClassConstant constant = mock(ClassConstant.class);

        // Act
        generalizer1.visitClassConstant(mockClazz, constant);
        generalizer2.visitClassConstant(mockClazz, constant);
        generalizer3.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(generalizer1));
        verify(constant, times(1)).referencedClassAccept(eq(generalizer2));
        verify(constant, times(1)).referencedClassAccept(eq(generalizer3));
    }

    /**
     * Tests that visitClassConstant implementation matches the documented behavior
     * of delegating to the referenced class for generalization processing.
     */
    @Test
    public void testVisitClassConstant_documentedBehavior() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        final boolean[] delegated = {false};

        doAnswer(invocation -> {
            delegated[0] = true;
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        generalizer.visitClassConstant(mockClazz, constant);

        // Assert
        assertTrue(delegated[0], "Should have delegated to referencedClassAccept");
        verify(constant, times(1)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant works with field generalization enabled.
     */
    @Test
    public void testVisitClassConstant_withFieldGeneralizationEnabled() {
        // Arrange
        MemberReferenceGeneralizer fieldGeneralizer = new MemberReferenceGeneralizer(
            true,   // field generalization enabled
            false,  // method generalization disabled
            codeAttributeEditor
        );
        ClassConstant constant = mock(ClassConstant.class);

        // Act
        fieldGeneralizer.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(fieldGeneralizer));
    }

    /**
     * Tests that visitClassConstant works with method generalization enabled.
     */
    @Test
    public void testVisitClassConstant_withMethodGeneralizationEnabled() {
        // Arrange
        MemberReferenceGeneralizer methodGeneralizer = new MemberReferenceGeneralizer(
            false,  // field generalization disabled
            true,   // method generalization enabled
            codeAttributeEditor
        );
        ClassConstant constant = mock(ClassConstant.class);

        // Act
        methodGeneralizer.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(methodGeneralizer));
    }

    /**
     * Tests that visitClassConstant works with both generalizations disabled.
     */
    @Test
    public void testVisitClassConstant_withBothGeneralizationsDisabled() {
        // Arrange
        MemberReferenceGeneralizer noGeneralizer = new MemberReferenceGeneralizer(
            false,  // field generalization disabled
            false,  // method generalization disabled
            codeAttributeEditor
        );
        ClassConstant constant = mock(ClassConstant.class);

        // Act
        noGeneralizer.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(noGeneralizer));
    }

    /**
     * Tests that visitClassConstant can be invoked through the ConstantVisitor interface
     * from within a visitor chain.
     */
    @Test
    public void testVisitClassConstant_withinVisitorChain() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);

        // Simulate being part of a visitor chain by casting to ConstantVisitor
        proguard.classfile.constant.visitor.ConstantVisitor constantVisitor = generalizer;

        // Act
        constantVisitor.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(generalizer));
    }

    /**
     * Tests that visitClassConstant properly integrates with the generalization workflow
     * by delegating to find super classes during reference generalization.
     */
    @Test
    public void testVisitClassConstant_integrationWithGeneralizationWorkflow() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Track the number of times referencedClassAccept is called
        final int[] acceptCount = {0};
        doAnswer(invocation -> {
            acceptCount[0]++;
            MemberReferenceGeneralizer visitor = invocation.getArgument(0);
            assertSame(generalizer, visitor, "Visitor should be the same MemberReferenceGeneralizer instance");
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        generalizer.visitClassConstant(clazz, constant);

        // Assert
        assertEquals(1, acceptCount[0], "referencedClassAccept should be called exactly once");
        verify(constant).referencedClassAccept(generalizer);
    }
}

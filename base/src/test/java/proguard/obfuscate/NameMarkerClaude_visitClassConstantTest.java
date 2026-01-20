package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.ClassConstant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NameMarker#visitClassConstant(Clazz, ClassConstant)}.
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/constant/ClassConstant;)V
 *
 * This class tests the visitClassConstant method which is part of the ConstantVisitor interface.
 * The method ensures that the outer class referenced by the ClassConstant is marked to keep its
 * name by delegating to referencedClassAccept, which will invoke the NameMarker as a ClassVisitor
 * on the referenced class.
 *
 * The method's purpose is to ensure proper name preservation of class constants that reference
 * outer classes during obfuscation.
 */
public class NameMarkerClaude_visitClassConstantTest {

    private NameMarker nameMarker;
    private Clazz mockClazz;
    private ClassConstant mockClassConstant;

    @BeforeEach
    public void setUp() {
        nameMarker = new NameMarker();
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
        nameMarker.visitClassConstant(mockClazz, mockClassConstant);

        // Assert
        verify(mockClassConstant, times(1)).referencedClassAccept(eq(nameMarker));
    }

    /**
     * Tests that visitClassConstant does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitClassConstant_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> nameMarker.visitClassConstant(mockClazz, mockClassConstant));
    }

    /**
     * Tests that visitClassConstant can be called multiple times on the same ClassConstant.
     * Each call should delegate to referencedClassAccept.
     */
    @Test
    public void testVisitClassConstant_calledMultipleTimes() {
        // Act
        nameMarker.visitClassConstant(mockClazz, mockClassConstant);
        nameMarker.visitClassConstant(mockClazz, mockClassConstant);
        nameMarker.visitClassConstant(mockClazz, mockClassConstant);

        // Assert
        verify(mockClassConstant, times(3)).referencedClassAccept(eq(nameMarker));
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
        nameMarker.visitClassConstant(mockClazz, constant1);
        nameMarker.visitClassConstant(mockClazz, constant2);
        nameMarker.visitClassConstant(mockClazz, constant3);

        // Assert
        verify(constant1, times(1)).referencedClassAccept(eq(nameMarker));
        verify(constant2, times(1)).referencedClassAccept(eq(nameMarker));
        verify(constant3, times(1)).referencedClassAccept(eq(nameMarker));
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
        nameMarker.visitClassConstant(clazz1, mockClassConstant);
        nameMarker.visitClassConstant(clazz2, mockClassConstant);
        nameMarker.visitClassConstant(clazz3, mockClassConstant);

        // Assert
        // The method should call referencedClassAccept regardless of the Clazz parameter
        verify(mockClassConstant, times(3)).referencedClassAccept(eq(nameMarker));
    }

    /**
     * Tests that visitClassConstant passes the correct NameMarker instance
     * (itself) as the visitor to referencedClassAccept.
     */
    @Test
    public void testVisitClassConstant_passesCorrectVisitor() {
        // Arrange
        NameMarker anotherMarker = new NameMarker();

        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);

        // Act
        nameMarker.visitClassConstant(mockClazz, constant1);
        anotherMarker.visitClassConstant(mockClazz, constant2);

        // Assert
        // Each marker should pass itself as the visitor
        verify(constant1, times(1)).referencedClassAccept(eq(nameMarker));
        verify(constant2, times(1)).referencedClassAccept(eq(anotherMarker));
    }

    // ========== Tests for Visitor Pattern Integration ==========

    /**
     * Tests that visitClassConstant correctly integrates with the NameMarker's
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
        nameMarker.visitClassConstant(clazz, constant);

        // Assert
        assertTrue(visitorWasCalled[0], "The visitor pattern should have been invoked");
        verify(constant, times(1)).referencedClassAccept(eq(nameMarker));
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
        proguard.classfile.constant.visitor.ConstantVisitor visitor = nameMarker;
        visitor.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(nameMarker));
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
            NameMarker visitor = invocation.getArgument(0);
            // The ClassConstant would internally call visitor.visitProgramClass(referencedClass)
            // or visitor.visitLibraryClass(referencedClass)
            // We just verify the visitor was passed correctly
            assertNotNull(visitor);
            assertEquals(nameMarker, visitor);
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        nameMarker.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(nameMarker));
    }

    // ========== Tests for Edge Cases ==========

    /**
     * Tests that multiple NameMarker instances can independently process
     * ClassConstants without interfering with each other.
     */
    @Test
    public void testVisitClassConstant_multipleMarkersIndependent() {
        // Arrange
        NameMarker marker1 = new NameMarker();
        NameMarker marker2 = new NameMarker();

        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act
        marker1.visitClassConstant(clazz1, constant1);
        marker2.visitClassConstant(clazz2, constant2);

        // Assert
        verify(constant1, times(1)).referencedClassAccept(eq(marker1));
        verify(constant2, times(1)).referencedClassAccept(eq(marker2));
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
        nameMarker.visitClassConstant(clazz1, constant1);
        nameMarker.visitClassConstant(clazz2, constant2);
        nameMarker.visitClassConstant(clazz3, constant3);

        // Assert - Each should have been processed exactly once with correct parameters
        verify(constant1, times(1)).referencedClassAccept(eq(nameMarker));
        verify(constant2, times(1)).referencedClassAccept(eq(nameMarker));
        verify(constant3, times(1)).referencedClassAccept(eq(nameMarker));
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
        nameMarker.visitClassConstant(clazz1, constant);
        nameMarker.visitClassConstant(clazz2, constant);
        nameMarker.visitClassConstant(clazz3, constant);

        // Assert
        verify(constant, times(3)).referencedClassAccept(eq(nameMarker));
    }

    /**
     * Tests that visitClassConstant works correctly when the ClassConstant
     * references an outer class that needs its name preserved.
     */
    @Test
    public void testVisitClassConstant_withOuterClassReference() {
        // Arrange
        ClassConstant outerClassConstant = mock(ClassConstant.class);
        Clazz innerClass = mock(Clazz.class);

        // Track that referencedClassAccept was called
        final int[] callCount = {0};
        doAnswer(invocation -> {
            callCount[0]++;
            return null;
        }).when(outerClassConstant).referencedClassAccept(any());

        // Act
        nameMarker.visitClassConstant(innerClass, outerClassConstant);

        // Assert
        assertEquals(1, callCount[0], "referencedClassAccept should be called once");
        verify(outerClassConstant, times(1)).referencedClassAccept(eq(nameMarker));
    }

    /**
     * Tests that the same NameMarker instance can be reused for visiting multiple constants.
     */
    @Test
    public void testVisitClassConstant_reuseMarkerInstance() {
        // Arrange
        ClassConstant constant1 = mock(ClassConstant.class);
        ClassConstant constant2 = mock(ClassConstant.class);
        ClassConstant constant3 = mock(ClassConstant.class);

        // Act - Reuse the same marker instance
        nameMarker.visitClassConstant(mockClazz, constant1);
        nameMarker.visitClassConstant(mockClazz, constant2);
        nameMarker.visitClassConstant(mockClazz, constant3);

        // Assert
        verify(constant1, times(1)).referencedClassAccept(eq(nameMarker));
        verify(constant2, times(1)).referencedClassAccept(eq(nameMarker));
        verify(constant3, times(1)).referencedClassAccept(eq(nameMarker));
    }

    /**
     * Tests that visitClassConstant properly handles the delegation pattern
     * where the ClassConstant may internally call back to the NameMarker.
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
            NameMarker visitor = invocation.getArgument(0);
            // Verify the visitor is the correct instance
            if (visitor == nameMarker) {
                callbackOccurred[0] = true;
            }
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        nameMarker.visitClassConstant(clazz, constant);

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
        nameMarker.visitClassConstant(clazz1, constant1);
        nameMarker.visitClassConstant(clazz1, constant2);
        nameMarker.visitClassConstant(clazz2, constant1);
        nameMarker.visitClassConstant(clazz2, constant2);

        // Assert
        verify(constant1, times(2)).referencedClassAccept(eq(nameMarker));
        verify(constant2, times(2)).referencedClassAccept(eq(nameMarker));
    }

    /**
     * Tests that visitClassConstant properly integrates with the overall name marking
     * workflow by ensuring the visitor pattern is correctly implemented.
     */
    @Test
    public void testVisitClassConstant_integrationWithNameMarkingWorkflow() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Track the number of times referencedClassAccept is called
        final int[] acceptCount = {0};
        doAnswer(invocation -> {
            acceptCount[0]++;
            NameMarker visitor = invocation.getArgument(0);
            assertSame(nameMarker, visitor, "Visitor should be the same NameMarker instance");
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        nameMarker.visitClassConstant(clazz, constant);

        // Assert
        assertEquals(1, acceptCount[0], "referencedClassAccept should be called exactly once");
        verify(constant).referencedClassAccept(nameMarker);
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
        nameMarker.visitClassConstant(clazz, constant);
        nameMarker.visitClassConstant(clazz, constant);
        nameMarker.visitClassConstant(clazz, constant);

        // Assert - Should call referencedClassAccept each time
        verify(constant, times(3)).referencedClassAccept(eq(nameMarker));
    }

    /**
     * Tests that different NameMarker instances maintain independent state
     * when processing the same ClassConstant.
     */
    @Test
    public void testVisitClassConstant_independentMarkerState() {
        // Arrange
        NameMarker marker1 = new NameMarker();
        NameMarker marker2 = new NameMarker();

        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Act
        marker1.visitClassConstant(clazz, constant);
        marker2.visitClassConstant(clazz, constant);

        // Assert - The constant should accept each marker instance
        verify(constant, times(1)).referencedClassAccept(eq(marker1));
        verify(constant, times(1)).referencedClassAccept(eq(marker2));
        verify(constant, times(2)).referencedClassAccept(any(NameMarker.class));
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
        nameMarker.visitClassConstant(spyClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(nameMarker));
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
        nameMarker.visitClassConstant(clazz, constant);

        // Assert
        assertSame(nameMarker, capturedVisitor[0], "Should pass exact NameMarker reference");
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
            nameMarker.visitClassConstant(mockClazz, constant);
        }

        // Assert
        verify(constant, times(100)).referencedClassAccept(eq(nameMarker));
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
        nameMarker.visitClassConstant(programClazz, constant1);
        nameMarker.visitClassConstant(libraryClazz, constant2);

        // Assert
        verify(constant1, times(1)).referencedClassAccept(eq(nameMarker));
        verify(constant2, times(1)).referencedClassAccept(eq(nameMarker));
    }

    /**
     * Tests that visitClassConstant ensures outer class names are kept by
     * delegating to referencedClassAccept which triggers the ClassVisitor chain.
     */
    @Test
    public void testVisitClassConstant_ensuresOuterClassNameIsKept() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);
        Clazz clazz = mock(Clazz.class);

        // Simulate the callback chain
        doAnswer(invocation -> {
            NameMarker visitor = invocation.getArgument(0);
            // This would eventually call visitor.visitProgramClass or visitLibraryClass
            // which would call keepClassName
            assertNotNull(visitor);
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        nameMarker.visitClassConstant(clazz, constant);

        // Assert - The method should call referencedClassAccept
        // This will eventually lead to visitProgramClass/visitLibraryClass and then keepClassName
        verify(constant, times(1)).referencedClassAccept(eq(nameMarker));
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
        assertDoesNotThrow(() -> nameMarker.visitClassConstant(mockClazz, constant));
        verify(constant, times(1)).referencedClassAccept(eq(nameMarker));
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
        nameMarker.visitClassConstant(mockClazz, constant1);
        nameMarker.visitClassConstant(mockClazz, constant2);
        nameMarker.visitClassConstant(mockClazz, constant1);
        nameMarker.visitClassConstant(mockClazz, constant2);

        // Assert
        verify(constant1, times(2)).referencedClassAccept(eq(nameMarker));
        verify(constant2, times(2)).referencedClassAccept(eq(nameMarker));
    }

    /**
     * Tests that visitClassConstant maintains the visitor chain correctly.
     */
    @Test
    public void testVisitClassConstant_maintainsVisitorChain() {
        // Arrange
        ClassConstant constant = mock(ClassConstant.class);

        // Verify that the visitor parameter is the NameMarker instance
        doAnswer(invocation -> {
            Object visitor = invocation.getArgument(0);
            assertTrue(visitor instanceof NameMarker, "Visitor should be NameMarker instance");
            assertSame(nameMarker, visitor, "Visitor should be the same NameMarker instance");
            return null;
        }).when(constant).referencedClassAccept(any());

        // Act
        nameMarker.visitClassConstant(mockClazz, constant);

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
        nameMarker.visitClassConstant(null, constant);

        // Assert - Should still call referencedClassAccept
        verify(constant, times(1)).referencedClassAccept(eq(nameMarker));
    }

    /**
     * Tests that multiple markers can process the same constant in parallel.
     */
    @Test
    public void testVisitClassConstant_parallelMarkerProcessing() {
        // Arrange
        NameMarker marker1 = new NameMarker();
        NameMarker marker2 = new NameMarker();
        NameMarker marker3 = new NameMarker();
        ClassConstant constant = mock(ClassConstant.class);

        // Act
        marker1.visitClassConstant(mockClazz, constant);
        marker2.visitClassConstant(mockClazz, constant);
        marker3.visitClassConstant(mockClazz, constant);

        // Assert
        verify(constant, times(1)).referencedClassAccept(eq(marker1));
        verify(constant, times(1)).referencedClassAccept(eq(marker2));
        verify(constant, times(1)).referencedClassAccept(eq(marker3));
    }

    /**
     * Tests that visitClassConstant implementation matches the documented behavior
     * of ensuring outer class names are preserved.
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
        nameMarker.visitClassConstant(mockClazz, constant);

        // Assert
        assertTrue(delegated[0], "Should have delegated to referencedClassAccept");
        verify(constant, times(1)).referencedClassAccept(eq(nameMarker));
    }
}

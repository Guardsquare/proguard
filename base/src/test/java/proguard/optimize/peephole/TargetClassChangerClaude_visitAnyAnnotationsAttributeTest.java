package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.annotation.AnnotationsAttribute;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
 *
 * The visitAnyAnnotationsAttribute method delegates to the annotationsAccept method
 * of the AnnotationsAttribute, which processes each annotation in the attribute
 * by calling back to the TargetClassChanger's visitAnnotation method.
 * This is part of the visitor pattern for processing annotations during class retargeting.
 */
public class TargetClassChangerClaude_visitAnyAnnotationsAttributeTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private AnnotationsAttribute annotationsAttribute;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        annotationsAttribute = mock(AnnotationsAttribute.class);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute correctly delegates to annotationsAccept.
     * This verifies the core functionality of the method - delegation to process annotations.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_delegatesToAnnotationsAccept() {
        // Act
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify that annotationsAccept was called with correct parameters
        verify(annotationsAttribute).annotationsAccept(clazz, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute can be called with valid mocks without exceptions.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
            changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute)
        );
    }

    /**
     * Tests that visitAnyAnnotationsAttribute can be called multiple times.
     * Each call should independently delegate to annotationsAccept.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify annotationsAccept was called exactly 3 times
        verify(annotationsAttribute, times(3)).annotationsAccept(clazz, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works with different attribute instances.
     * Each attribute instance should have its annotationsAccept method called.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withDifferentAttributes_delegatesToEach() {
        // Arrange
        AnnotationsAttribute attr1 = mock(AnnotationsAttribute.class);
        AnnotationsAttribute attr2 = mock(AnnotationsAttribute.class);
        AnnotationsAttribute attr3 = mock(AnnotationsAttribute.class);

        // Act
        changer.visitAnyAnnotationsAttribute(clazz, attr1);
        changer.visitAnyAnnotationsAttribute(clazz, attr2);
        changer.visitAnyAnnotationsAttribute(clazz, attr3);

        // Assert - verify each attribute's annotationsAccept was called once
        verify(attr1).annotationsAccept(clazz, changer);
        verify(attr2).annotationsAccept(clazz, changer);
        verify(attr3).annotationsAccept(clazz, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute passes the changer itself as the visitor.
     * This is crucial because the changer implements AnnotationVisitor.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_passesChangerAsVisitor() {
        // Act
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify that the changer itself is passed as the visitor parameter
        verify(annotationsAttribute).annotationsAccept(
            eq(clazz),
            same(changer)  // The changer itself should be passed as visitor
        );
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works with different clazz instances.
     * Each clazz should be correctly passed through to annotationsAccept.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        changer.visitAnyAnnotationsAttribute(clazz1, annotationsAttribute);
        changer.visitAnyAnnotationsAttribute(clazz2, annotationsAttribute);

        // Assert - verify the correct clazz was passed in each call
        verify(annotationsAttribute).annotationsAccept(clazz1, changer);
        verify(annotationsAttribute).annotationsAccept(clazz2, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_doesNotDirectlyInteractWithParameters() {
        // Act
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify no direct interactions with clazz (it's only passed through)
        verifyNoInteractions(clazz);
        // annotationsAttribute should have been called via delegation
        verify(annotationsAttribute, times(1)).annotationsAccept(any(), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnyAnnotationsAttribute maintains correct order when called with multiple attributes.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_sequentialCalls_maintainIndependence() {
        // Arrange
        AnnotationsAttribute attr1 = mock(AnnotationsAttribute.class);
        AnnotationsAttribute attr2 = mock(AnnotationsAttribute.class);

        // Act - call with first attribute
        changer.visitAnyAnnotationsAttribute(clazz, attr1);
        verify(attr1).annotationsAccept(clazz, changer);

        // Act - call with second attribute
        changer.visitAnyAnnotationsAttribute(clazz, attr2);
        verify(attr2).annotationsAccept(clazz, changer);

        // Assert - first attribute should not have been called again
        verify(attr1, times(1)).annotationsAccept(any(), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnyAnnotationsAttribute integrates correctly with the visitor pattern.
     * The changer implements AnnotationVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_changerIsValidVisitor() {
        // Arrange & Assert - verify the changer is an instance of AnnotationVisitor
        assertTrue(changer instanceof AnnotationVisitor,
            "Changer should implement AnnotationVisitor to be used as a visitor");

        // Act
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify it's passed as an AnnotationVisitor
        verify(annotationsAttribute).annotationsAccept(
            any(Clazz.class),
            any(AnnotationVisitor.class)
        );
    }

    /**
     * Tests that visitAnyAnnotationsAttribute handles repeated calls with the same parameters idempotently.
     * While each call should delegate, the delegation should be consistent.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_repeatedCallsWithSameParameters_delegatesConsistently() {
        // Act
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify delegation happened twice with identical parameters
        verify(annotationsAttribute, times(2)).annotationsAccept(same(clazz), same(changer));
    }

    /**
     * Tests that visitAnyAnnotationsAttribute properly integrates with both parameters.
     * This is a comprehensive test verifying the complete parameter flow.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_integratesBothParameters() {
        // Arrange - create specific mocks
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        AnnotationsAttribute specificAttr = mock(AnnotationsAttribute.class, "specificAttr");

        // Act
        changer.visitAnyAnnotationsAttribute(specificClazz, specificAttr);

        // Assert - verify both specific parameters were passed correctly
        verify(specificAttr).annotationsAccept(specificClazz, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works correctly across different changer instances.
     * Different changers should independently delegate to their annotations.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withDifferentChangers_delegatesIndependently() {
        // Arrange
        TargetClassChanger changer2 = new TargetClassChanger();

        // Act
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        changer2.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify each changer was passed independently
        verify(annotationsAttribute, times(1)).annotationsAccept(clazz, changer);
        verify(annotationsAttribute, times(1)).annotationsAccept(clazz, changer2);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute handles the visitor pattern delegation correctly.
     * The method should pass the changer (which is an AnnotationVisitor) to process annotations.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_delegationFollowsVisitorPattern() {
        // Act
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify the method delegates by calling annotationsAccept exactly once
        verify(annotationsAttribute, times(1)).annotationsAccept(any(Clazz.class), any(AnnotationVisitor.class));
    }

    /**
     * Tests visitAnyAnnotationsAttribute can handle multiple different class-attribute combinations.
     * This tests the method's ability to handle various combinations of inputs.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withMultipleCombinations() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class, "clazz1");
        Clazz clazz2 = mock(ProgramClass.class, "clazz2");
        AnnotationsAttribute attr1 = mock(AnnotationsAttribute.class, "attr1");
        AnnotationsAttribute attr2 = mock(AnnotationsAttribute.class, "attr2");

        // Act
        changer.visitAnyAnnotationsAttribute(clazz1, attr1);
        changer.visitAnyAnnotationsAttribute(clazz1, attr2);
        changer.visitAnyAnnotationsAttribute(clazz2, attr1);
        changer.visitAnyAnnotationsAttribute(clazz2, attr2);

        // Assert - verify all combinations were processed
        verify(attr1).annotationsAccept(clazz1, changer);
        verify(attr2).annotationsAccept(clazz1, changer);
        verify(attr1).annotationsAccept(clazz2, changer);
        verify(attr2).annotationsAccept(clazz2, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute properly delegates without modifying the attribute.
     * The method should only delegate, not modify the attribute directly.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_doesNotModifyAttributeDirectly() {
        // Arrange
        AnnotationsAttribute spyAttribute = mock(AnnotationsAttribute.class);

        // Act
        changer.visitAnyAnnotationsAttribute(clazz, spyAttribute);

        // Assert - verify only annotationsAccept was called, nothing else
        verify(spyAttribute, times(1)).annotationsAccept(any(), any());
        verifyNoMoreInteractions(spyAttribute);
    }

    /**
     * Tests visitAnyAnnotationsAttribute execution completes quickly.
     * Since it's a simple delegation method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAnnotationsAttribute should execute quickly as it's a simple delegation");
    }

    /**
     * Tests that visitAnyAnnotationsAttribute maintains the correct contract for the visitor pattern.
     * The method name starts with "visitAny" indicating it handles any type of annotations attribute.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_handlesAnyAnnotationsAttribute() {
        // Arrange - could be RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations, etc.
        AnnotationsAttribute runtimeVisibleAnnotations = mock(AnnotationsAttribute.class, "runtimeVisible");
        AnnotationsAttribute runtimeInvisibleAnnotations = mock(AnnotationsAttribute.class, "runtimeInvisible");

        // Act
        changer.visitAnyAnnotationsAttribute(clazz, runtimeVisibleAnnotations);
        changer.visitAnyAnnotationsAttribute(clazz, runtimeInvisibleAnnotations);

        // Assert - verify both types were processed
        verify(runtimeVisibleAnnotations).annotationsAccept(clazz, changer);
        verify(runtimeInvisibleAnnotations).annotationsAccept(clazz, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works with LibraryClass as well as ProgramClass.
     * The method should handle both types of Clazz implementations.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withLibraryClass_delegatesCorrectly() {
        // Arrange
        Clazz libraryClass = mock(LibraryClass.class);

        // Act
        changer.visitAnyAnnotationsAttribute(libraryClass, annotationsAttribute);

        // Assert - verify delegation occurred with library class
        verify(annotationsAttribute).annotationsAccept(libraryClass, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works with a mix of ProgramClass and LibraryClass.
     * This ensures the method is polymorphic over Clazz implementations.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withMixedClazzTypes_handlesAll() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act
        changer.visitAnyAnnotationsAttribute(programClass, annotationsAttribute);
        changer.visitAnyAnnotationsAttribute(libraryClass, annotationsAttribute);

        // Assert - verify both types were processed
        verify(annotationsAttribute).annotationsAccept(programClass, changer);
        verify(annotationsAttribute).annotationsAccept(libraryClass, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute can be called in rapid succession without issues.
     * This verifies there's no timing-dependent behavior.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify delegation occurred for all calls
        verify(annotationsAttribute, times(1000)).annotationsAccept(clazz, changer);
    }

    /**
     * Tests that multiple changers can call visitAnyAnnotationsAttribute independently.
     * Each changer should operate independently without interfering with others.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_multipleChangersNoInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        AnnotationsAttribute attr1 = mock(AnnotationsAttribute.class);
        AnnotationsAttribute attr2 = mock(AnnotationsAttribute.class);

        // Act - different changers process different attributes
        changer1.visitAnyAnnotationsAttribute(clazz1, attr1);
        changer2.visitAnyAnnotationsAttribute(clazz2, attr2);
        changer3.visitAnyAnnotationsAttribute(clazz1, attr2);

        // Assert - verify correct delegations occurred
        verify(attr1).annotationsAccept(clazz1, changer1);
        verify(attr2).annotationsAccept(clazz2, changer2);
        verify(attr2).annotationsAccept(clazz1, changer3);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works correctly when the TargetClassChanger
     * is used polymorphically as an AttributeVisitor.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_asAttributeVisitorInterface_doesNotThrowException() {
        // Arrange - treat changer as AttributeVisitor interface
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = changer;

        // Act & Assert - should work the same way through the interface
        assertDoesNotThrow(() -> visitor.visitAnyAnnotationsAttribute(clazz, annotationsAttribute));
        verify(annotationsAttribute).annotationsAccept(clazz, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute called on newly created changer instance behaves consistently.
     * Verifies the method works immediately after construction without initialization.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_onNewInstance_behavesConsistently() {
        // Arrange - create a fresh changer
        TargetClassChanger freshChanger = new TargetClassChanger();

        // Act - immediately call visitAnyAnnotationsAttribute
        freshChanger.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - should delegate properly
        verify(annotationsAttribute).annotationsAccept(clazz, freshChanger);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute handles edge case of calling with various
     * combinations of different clazz and attribute pairs in sequence.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_sequentialCallsWithDifferentParameters_noIssues() {
        // Arrange
        Clazz[] clazzes = {
            mock(ProgramClass.class),
            mock(LibraryClass.class),
            mock(ProgramClass.class)
        };
        AnnotationsAttribute[] attributes = {
            mock(AnnotationsAttribute.class),
            mock(AnnotationsAttribute.class),
            mock(AnnotationsAttribute.class)
        };

        // Act - call with different combinations
        for (Clazz c : clazzes) {
            for (AnnotationsAttribute attr : attributes) {
                changer.visitAnyAnnotationsAttribute(c, attr);
            }
        }

        // Assert - verify delegations for all combinations
        for (Clazz c : clazzes) {
            for (AnnotationsAttribute attr : attributes) {
                verify(attr).annotationsAccept(c, changer);
            }
        }
    }

    /**
     * Tests that the visitAnyAnnotationsAttribute method signature matches the AttributeVisitor interface.
     * This ensures the method properly overrides the interface method.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_implementsInterfaceCorrectly() {
        // Assert - TargetClassChanger should be an AttributeVisitor
        assertTrue(changer instanceof proguard.classfile.attribute.visitor.AttributeVisitor,
                "TargetClassChanger should implement AttributeVisitor");
    }

    /**
     * Tests that visitAnyAnnotationsAttribute doesn't affect the changer's internal state.
     * Since it's a pure delegation method, it should not modify any internal fields.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_doesNotAffectInternalState() {
        // Act - call visitAnyAnnotationsAttribute multiple times
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify delegations occurred without side effects to clazz
        verifyNoInteractions(clazz);
        verify(annotationsAttribute, times(2)).annotationsAccept(clazz, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute with varying combinations of parameters
     * all result in proper delegation.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_varyingParameterCombinations_consistentDelegation() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        AnnotationsAttribute attr2 = mock(AnnotationsAttribute.class);

        // Act - call with various parameter combinations
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        changer.visitAnyAnnotationsAttribute(clazz2, attr2);
        changer.visitAnyAnnotationsAttribute(clazz, attr2);

        // Assert - verify proper delegation for all combinations
        verify(annotationsAttribute).annotationsAccept(clazz, changer);
        verify(attr2).annotationsAccept(clazz2, changer);
        verify(attr2).annotationsAccept(clazz, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute completes without blocking or hanging.
     * This ensures the method doesn't have any unexpected wait conditions.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnyAnnotationsAttribute should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitAnyAnnotationsAttribute handles the same attribute instance multiple times
     * without accumulating state or causing issues.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_sameAttributeInstanceMultipleTimes_noAccumulation() {
        // Arrange
        AnnotationsAttribute singleAttribute = mock(AnnotationsAttribute.class);

        // Act - call multiple times with the same attribute
        for (int i = 0; i < 10; i++) {
            changer.visitAnyAnnotationsAttribute(clazz, singleAttribute);
        }

        // Assert - verify delegation occurred 10 times
        verify(singleAttribute, times(10)).annotationsAccept(clazz, changer);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute returns immediately without performing operations beyond delegation.
     * This confirms the pure delegation nature of the method.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_returnsImmediatelyAfterDelegation() {
        // Arrange
        AnnotationsAttribute trackedAttribute = mock(AnnotationsAttribute.class);

        // Act
        changer.visitAnyAnnotationsAttribute(clazz, trackedAttribute);

        // Assert - only delegation call should have been made
        verify(trackedAttribute, times(1)).annotationsAccept(any(), any());
        verifyNoMoreInteractions(trackedAttribute);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute called after multiple other operations still delegates correctly.
     * This verifies consistent behavior regardless of the changer's usage history.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_afterMultipleOperations_stillDelegates() {
        // Arrange - simulate some prior operations
        for (int i = 0; i < 5; i++) {
            changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        }

        // Act - call visitAnyAnnotationsAttribute again
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify delegation occurred 6 times total
        verify(annotationsAttribute, times(6)).annotationsAccept(clazz, changer);
    }

    /**
     * Tests that the visitor passed to annotationsAccept is indeed the changer itself.
     * This verifies that the changer can receive callbacks from the attribute.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_changerAsVisitorReceivesCallbacks() {
        // Act
        changer.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify the exact same changer instance is passed
        verify(annotationsAttribute).annotationsAccept(
            argThat(c -> c == clazz),
            argThat(v -> v == changer)
        );
    }
}

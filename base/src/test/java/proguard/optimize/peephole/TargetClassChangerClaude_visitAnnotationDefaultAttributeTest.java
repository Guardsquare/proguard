package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.annotation.AnnotationDefaultAttribute;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitAnnotationDefaultAttribute(Clazz, Method, AnnotationDefaultAttribute)}.
 *
 * The visitAnnotationDefaultAttribute method delegates to the defaultValueAccept method
 * of the AnnotationDefaultAttribute, which processes the default value by calling back
 * to the TargetClassChanger's ElementValueVisitor methods.
 * This is part of the visitor pattern for processing annotation default values during class retargeting.
 */
public class TargetClassChangerClaude_visitAnnotationDefaultAttributeTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private Method method;
    private AnnotationDefaultAttribute annotationDefaultAttribute;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        method = mock(Method.class);
        annotationDefaultAttribute = mock(AnnotationDefaultAttribute.class);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute correctly delegates to defaultValueAccept.
     * This verifies the core functionality of the method - delegation to process default values.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_delegatesToDefaultValueAccept() {
        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify that defaultValueAccept was called with correct parameters
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute can be called with valid mocks without exceptions.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
            changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute)
        );
    }

    /**
     * Tests that visitAnnotationDefaultAttribute can be called multiple times.
     * Each call should independently delegate to defaultValueAccept.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify defaultValueAccept was called exactly 3 times
        verify(annotationDefaultAttribute, times(3)).defaultValueAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with different attribute instances.
     * Each attribute instance should have its defaultValueAccept method called.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentAttributes_delegatesToEach() {
        // Arrange
        AnnotationDefaultAttribute attr1 = mock(AnnotationDefaultAttribute.class);
        AnnotationDefaultAttribute attr2 = mock(AnnotationDefaultAttribute.class);
        AnnotationDefaultAttribute attr3 = mock(AnnotationDefaultAttribute.class);

        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, attr1);
        changer.visitAnnotationDefaultAttribute(clazz, method, attr2);
        changer.visitAnnotationDefaultAttribute(clazz, method, attr3);

        // Assert - verify each attribute's defaultValueAccept was called once
        verify(attr1).defaultValueAccept(clazz, changer);
        verify(attr2).defaultValueAccept(clazz, changer);
        verify(attr3).defaultValueAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute passes the changer itself as the visitor.
     * This is crucial because the changer implements ElementValueVisitor.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_passesChangerAsVisitor() {
        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify that the changer itself is passed as the visitor parameter
        verify(annotationDefaultAttribute).defaultValueAccept(
            eq(clazz),
            same(changer)  // The changer itself should be passed as visitor
        );
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with different clazz instances.
     * Each clazz should be correctly passed through to defaultValueAccept.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        changer.visitAnnotationDefaultAttribute(clazz1, method, annotationDefaultAttribute);
        changer.visitAnnotationDefaultAttribute(clazz2, method, annotationDefaultAttribute);

        // Assert - verify the correct clazz was passed in each call
        verify(annotationDefaultAttribute).defaultValueAccept(clazz1, changer);
        verify(annotationDefaultAttribute).defaultValueAccept(clazz2, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with different method instances.
     * Each method should be accepted by the visitor, even though it's not directly passed to defaultValueAccept.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentMethod_acceptsEach() {
        // Arrange
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method1, annotationDefaultAttribute);
        changer.visitAnnotationDefaultAttribute(clazz, method2, annotationDefaultAttribute);

        // Assert - verify delegation occurred for each method context
        verify(annotationDefaultAttribute, times(2)).defaultValueAccept(eq(clazz), eq(changer));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz or method.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotDirectlyInteractWithParameters() {
        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify no direct interactions with parameters (they're only passed through)
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        // annotationDefaultAttribute should have been called via delegation
        verify(annotationDefaultAttribute, times(1))
            .defaultValueAccept(any(), any(ElementValueVisitor.class));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute maintains correct order when called with multiple attributes.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_sequentialCalls_maintainIndependence() {
        // Arrange
        AnnotationDefaultAttribute attr1 = mock(AnnotationDefaultAttribute.class);
        AnnotationDefaultAttribute attr2 = mock(AnnotationDefaultAttribute.class);

        // Act - call with first attribute
        changer.visitAnnotationDefaultAttribute(clazz, method, attr1);
        verify(attr1).defaultValueAccept(clazz, changer);

        // Act - call with second attribute
        changer.visitAnnotationDefaultAttribute(clazz, method, attr2);
        verify(attr2).defaultValueAccept(clazz, changer);

        // Assert - first attribute should not have been called again
        verify(attr1, times(1)).defaultValueAccept(any(), any(ElementValueVisitor.class));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute integrates correctly with the visitor pattern.
     * The changer implements ElementValueVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_changerIsValidVisitor() {
        // Arrange & Assert - verify the changer is an instance of ElementValueVisitor
        assertTrue(changer instanceof ElementValueVisitor,
            "Changer should implement ElementValueVisitor to be used as a visitor");

        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify it's passed as an ElementValueVisitor
        verify(annotationDefaultAttribute).defaultValueAccept(
            any(Clazz.class),
            any(ElementValueVisitor.class)
        );
    }

    /**
     * Tests that visitAnnotationDefaultAttribute handles repeated calls with the same parameters idempotently.
     * While each call should delegate, the delegation should be consistent.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_repeatedCallsWithSameParameters_delegatesConsistently() {
        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify delegation happened twice with identical parameters
        verify(annotationDefaultAttribute, times(2)).defaultValueAccept(same(clazz), same(changer));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute properly integrates with all three parameters.
     * This is a comprehensive test verifying the complete parameter flow.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_integratesAllParameters() {
        // Arrange - create specific mocks
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        Method specificMethod = mock(Method.class, "specificMethod");
        AnnotationDefaultAttribute specificAttr = mock(AnnotationDefaultAttribute.class, "specificAttr");

        // Act
        changer.visitAnnotationDefaultAttribute(specificClazz, specificMethod, specificAttr);

        // Assert - verify specific parameters were passed correctly
        verify(specificAttr).defaultValueAccept(specificClazz, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works correctly across different changer instances.
     * Different changers should independently delegate to their annotation default attributes.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentChangers_delegatesIndependently() {
        // Arrange
        TargetClassChanger changer2 = new TargetClassChanger();

        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        changer2.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify each changer was passed independently
        verify(annotationDefaultAttribute, times(1)).defaultValueAccept(clazz, changer);
        verify(annotationDefaultAttribute, times(1)).defaultValueAccept(clazz, changer2);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute handles the visitor pattern delegation correctly.
     * The method should pass the changer (which is an ElementValueVisitor) to process default values.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_delegationFollowsVisitorPattern() {
        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify the method delegates by calling defaultValueAccept exactly once
        verify(annotationDefaultAttribute, times(1))
            .defaultValueAccept(any(Clazz.class), any(ElementValueVisitor.class));
    }

    /**
     * Tests visitAnnotationDefaultAttribute can handle multiple different combinations.
     * This tests the method's ability to handle various combinations of inputs.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withMultipleCombinations() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class, "clazz1");
        Clazz clazz2 = mock(ProgramClass.class, "clazz2");
        Method method1 = mock(Method.class, "method1");
        Method method2 = mock(Method.class, "method2");
        AnnotationDefaultAttribute attr1 = mock(AnnotationDefaultAttribute.class, "attr1");
        AnnotationDefaultAttribute attr2 = mock(AnnotationDefaultAttribute.class, "attr2");

        // Act
        changer.visitAnnotationDefaultAttribute(clazz1, method1, attr1);
        changer.visitAnnotationDefaultAttribute(clazz1, method2, attr2);
        changer.visitAnnotationDefaultAttribute(clazz2, method1, attr1);
        changer.visitAnnotationDefaultAttribute(clazz2, method2, attr2);

        // Assert - verify all combinations were processed
        verify(attr1, times(2)).defaultValueAccept(any(Clazz.class), eq(changer));
        verify(attr2, times(2)).defaultValueAccept(any(Clazz.class), eq(changer));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute properly delegates without modifying the attribute.
     * The method should only delegate, not modify the attribute directly.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotModifyAttributeDirectly() {
        // Arrange
        AnnotationDefaultAttribute spyAttribute = mock(AnnotationDefaultAttribute.class);

        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, spyAttribute);

        // Assert - verify only defaultValueAccept was called, nothing else
        verify(spyAttribute, times(1)).defaultValueAccept(any(), any());
        verifyNoMoreInteractions(spyAttribute);
    }

    /**
     * Tests visitAnnotationDefaultAttribute execution completes quickly.
     * Since it's a simple delegation method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitAnnotationDefaultAttribute should execute quickly as it's a simple delegation");
    }

    /**
     * Tests that visitAnnotationDefaultAttribute is used for annotation type methods.
     * Annotation default values are only valid for annotation type methods.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_handlesAnnotationTypeMethods() {
        // Arrange
        Method annotationMethod = mock(Method.class, "annotationMethod");

        // Act
        changer.visitAnnotationDefaultAttribute(clazz, annotationMethod, annotationDefaultAttribute);

        // Assert - verify delegation occurred for annotation method
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute passes clazz parameter correctly.
     * The clazz parameter represents the class containing the annotation type.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_passesClazzParameter() {
        // Arrange
        Clazz annotationClass = mock(ProgramClass.class, "annotationClass");

        // Act
        changer.visitAnnotationDefaultAttribute(annotationClass, method, annotationDefaultAttribute);

        // Assert - verify the clazz was passed to defaultValueAccept
        verify(annotationDefaultAttribute).defaultValueAccept(same(annotationClass), any(ElementValueVisitor.class));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute properly delegates in the context of annotation processing.
     * This verifies that the delegation mechanism works as expected in the visitor pattern.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_delegationInAnnotationContext() {
        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify delegation occurred with the changer as visitor
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with LibraryClass as well as ProgramClass.
     * The method should handle both types of Clazz implementations.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withLibraryClass_delegatesCorrectly() {
        // Arrange
        Clazz libraryClass = mock(LibraryClass.class);

        // Act
        changer.visitAnnotationDefaultAttribute(libraryClass, method, annotationDefaultAttribute);

        // Assert - verify delegation occurred with library class
        verify(annotationDefaultAttribute).defaultValueAccept(libraryClass, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with a mix of ProgramClass and LibraryClass.
     * This ensures the method is polymorphic over Clazz implementations.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withMixedClazzTypes_handlesAll() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act
        changer.visitAnnotationDefaultAttribute(programClass, method, annotationDefaultAttribute);
        changer.visitAnnotationDefaultAttribute(libraryClass, method, annotationDefaultAttribute);

        // Assert - verify both types were processed
        verify(annotationDefaultAttribute).defaultValueAccept(programClass, changer);
        verify(annotationDefaultAttribute).defaultValueAccept(libraryClass, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute can be called in rapid succession without issues.
     * This verifies there's no timing-dependent behavior.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify delegation occurred for all calls
        verify(annotationDefaultAttribute, times(1000)).defaultValueAccept(clazz, changer);
    }

    /**
     * Tests that multiple changers can call visitAnnotationDefaultAttribute independently.
     * Each changer should operate independently without interfering with others.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_multipleChangersNoInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        AnnotationDefaultAttribute attr1 = mock(AnnotationDefaultAttribute.class);
        AnnotationDefaultAttribute attr2 = mock(AnnotationDefaultAttribute.class);

        // Act - different changers process different attributes
        changer1.visitAnnotationDefaultAttribute(clazz1, method1, attr1);
        changer2.visitAnnotationDefaultAttribute(clazz2, method2, attr2);
        changer3.visitAnnotationDefaultAttribute(clazz1, method2, attr2);

        // Assert - verify correct delegations occurred
        verify(attr1).defaultValueAccept(clazz1, changer1);
        verify(attr2).defaultValueAccept(clazz2, changer2);
        verify(attr2).defaultValueAccept(clazz1, changer3);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works correctly when the TargetClassChanger
     * is used polymorphically as an AttributeVisitor.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_asAttributeVisitorInterface_doesNotThrowException() {
        // Arrange - treat changer as AttributeVisitor interface
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = changer;

        // Act & Assert - should work the same way through the interface
        assertDoesNotThrow(() -> visitor.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute));
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute called on newly created changer instance behaves consistently.
     * Verifies the method works immediately after construction without initialization.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_onNewInstance_behavesConsistently() {
        // Arrange - create a fresh changer
        TargetClassChanger freshChanger = new TargetClassChanger();

        // Act - immediately call visitAnnotationDefaultAttribute
        freshChanger.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - should delegate properly
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, freshChanger);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute handles edge case of calling with various
     * combinations of different clazz, method, and attribute tuples in sequence.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_sequentialCallsWithDifferentParameters_noIssues() {
        // Arrange
        Clazz[] clazzes = {
            mock(ProgramClass.class),
            mock(LibraryClass.class),
            mock(ProgramClass.class)
        };
        Method[] methods = {
            mock(Method.class),
            mock(Method.class),
            mock(Method.class)
        };
        AnnotationDefaultAttribute[] attributes = {
            mock(AnnotationDefaultAttribute.class),
            mock(AnnotationDefaultAttribute.class),
            mock(AnnotationDefaultAttribute.class)
        };

        // Act - call with different combinations
        for (Clazz c : clazzes) {
            for (Method m : methods) {
                for (AnnotationDefaultAttribute attr : attributes) {
                    changer.visitAnnotationDefaultAttribute(c, m, attr);
                }
            }
        }

        // Assert - verify delegations for all combinations
        for (Clazz c : clazzes) {
            for (AnnotationDefaultAttribute attr : attributes) {
                verify(attr, times(methods.length)).defaultValueAccept(c, changer);
            }
        }
    }

    /**
     * Tests that the visitAnnotationDefaultAttribute method signature matches the AttributeVisitor interface.
     * This ensures the method properly overrides the interface method.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_implementsInterfaceCorrectly() {
        // Assert - TargetClassChanger should be an AttributeVisitor
        assertTrue(changer instanceof proguard.classfile.attribute.visitor.AttributeVisitor,
                "TargetClassChanger should implement AttributeVisitor");
    }

    /**
     * Tests that visitAnnotationDefaultAttribute doesn't affect the changer's internal state.
     * Since it's a pure delegation method, it should not modify any internal fields.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotAffectInternalState() {
        // Act - call visitAnnotationDefaultAttribute multiple times
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify delegations occurred without side effects to parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verify(annotationDefaultAttribute, times(2)).defaultValueAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute with varying combinations of parameters
     * all result in proper delegation.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_varyingParameterCombinations_consistentDelegation() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Method method2 = mock(Method.class);
        AnnotationDefaultAttribute attr2 = mock(AnnotationDefaultAttribute.class);

        // Act - call with various parameter combinations
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        changer.visitAnnotationDefaultAttribute(clazz2, method2, attr2);
        changer.visitAnnotationDefaultAttribute(clazz, method2, attr2);

        // Assert - verify proper delegation for all combinations
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, changer);
        verify(attr2).defaultValueAccept(clazz2, changer);
        verify(attr2).defaultValueAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute completes without blocking or hanging.
     * This ensures the method doesn't have any unexpected wait conditions.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnnotationDefaultAttribute should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitAnnotationDefaultAttribute handles the same attribute instance multiple times
     * without accumulating state or causing issues.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_sameAttributeInstanceMultipleTimes_noAccumulation() {
        // Arrange
        AnnotationDefaultAttribute singleAttribute = mock(AnnotationDefaultAttribute.class);

        // Act - call multiple times with the same attribute
        for (int i = 0; i < 10; i++) {
            changer.visitAnnotationDefaultAttribute(clazz, method, singleAttribute);
        }

        // Assert - verify delegation occurred 10 times
        verify(singleAttribute, times(10)).defaultValueAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute returns immediately without performing operations beyond delegation.
     * This confirms the pure delegation nature of the method.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_returnsImmediatelyAfterDelegation() {
        // Arrange
        AnnotationDefaultAttribute trackedAttribute = mock(AnnotationDefaultAttribute.class);

        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, trackedAttribute);

        // Assert - only delegation call should have been made
        verify(trackedAttribute, times(1)).defaultValueAccept(any(), any());
        verifyNoMoreInteractions(trackedAttribute);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute called after multiple other operations still delegates correctly.
     * This verifies consistent behavior regardless of the changer's usage history.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_afterMultipleOperations_stillDelegates() {
        // Arrange - simulate some prior operations
        for (int i = 0; i < 5; i++) {
            changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        }

        // Act - call visitAnnotationDefaultAttribute again
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify delegation occurred 6 times total
        verify(annotationDefaultAttribute, times(6)).defaultValueAccept(clazz, changer);
    }

    /**
     * Tests that the visitor passed to defaultValueAccept is indeed the changer itself.
     * This verifies that the changer can receive callbacks from the attribute.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_changerAsVisitorReceivesCallbacks() {
        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify the exact same changer instance is passed
        verify(annotationDefaultAttribute).defaultValueAccept(
            argThat(c -> c == clazz),
            argThat(v -> v == changer)
        );
    }

    /**
     * Tests that visitAnnotationDefaultAttribute does not pass the method parameter to defaultValueAccept.
     * The method parameter provides context but is not forwarded in the delegation.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_methodParameterNotPassedToDelegate() {
        // Act
        changer.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify only clazz and changer are passed, not method
        verify(annotationDefaultAttribute).defaultValueAccept(any(Clazz.class), any(ElementValueVisitor.class));
        verifyNoInteractions(method);
    }
}

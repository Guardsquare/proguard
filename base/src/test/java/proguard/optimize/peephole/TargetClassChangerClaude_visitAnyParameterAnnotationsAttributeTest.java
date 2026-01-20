package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.annotation.ParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)}.
 *
 * The visitAnyParameterAnnotationsAttribute method delegates to the annotationsAccept method
 * of the ParameterAnnotationsAttribute, which processes parameter annotations by calling back
 * to the TargetClassChanger's visitAnnotation method for each annotation.
 * This is part of the visitor pattern for processing parameter annotations during class retargeting.
 */
public class TargetClassChangerClaude_visitAnyParameterAnnotationsAttributeTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private Method method;
    private ParameterAnnotationsAttribute parameterAnnotationsAttribute;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        method = mock(Method.class);
        parameterAnnotationsAttribute = mock(ParameterAnnotationsAttribute.class);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute correctly delegates to annotationsAccept.
     * This verifies the core functionality of the method - delegation to process parameter annotations.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_delegatesToAnnotationsAccept() {
        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify that annotationsAccept was called with correct parameters
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute can be called with valid mocks without exceptions.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
            changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute)
        );
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute can be called multiple times.
     * Each call should independently delegate to annotationsAccept.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify annotationsAccept was called exactly 3 times
        verify(parameterAnnotationsAttribute, times(3)).annotationsAccept(clazz, method, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works with different attribute instances.
     * Each attribute instance should have its annotationsAccept method called.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withDifferentAttributes_delegatesToEach() {
        // Arrange
        ParameterAnnotationsAttribute attr1 = mock(ParameterAnnotationsAttribute.class);
        ParameterAnnotationsAttribute attr2 = mock(ParameterAnnotationsAttribute.class);
        ParameterAnnotationsAttribute attr3 = mock(ParameterAnnotationsAttribute.class);

        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, attr1);
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, attr2);
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, attr3);

        // Assert - verify each attribute's annotationsAccept was called once
        verify(attr1).annotationsAccept(clazz, method, changer);
        verify(attr2).annotationsAccept(clazz, method, changer);
        verify(attr3).annotationsAccept(clazz, method, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute passes the changer itself as the visitor.
     * This is crucial because the changer implements AnnotationVisitor.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_passesChangerAsVisitor() {
        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify that the changer itself is passed as the visitor parameter
        verify(parameterAnnotationsAttribute).annotationsAccept(
            eq(clazz),
            eq(method),
            same(changer)  // The changer itself should be passed as visitor
        );
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works with different clazz instances.
     * Each clazz should be correctly passed through to annotationsAccept.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz1, method, parameterAnnotationsAttribute);
        changer.visitAnyParameterAnnotationsAttribute(clazz2, method, parameterAnnotationsAttribute);

        // Assert - verify the correct clazz was passed in each call
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz1, method, changer);
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz2, method, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works with different method instances.
     * Each method should be correctly passed through to annotationsAccept.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withDifferentMethod_passesCorrectMethod() {
        // Arrange
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method1, parameterAnnotationsAttribute);
        changer.visitAnyParameterAnnotationsAttribute(clazz, method2, parameterAnnotationsAttribute);

        // Assert - verify the correct method was passed in each call
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method1, changer);
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method2, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz or method.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_doesNotDirectlyInteractWithParameters() {
        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify no direct interactions with parameters (they're only passed through)
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        // parameterAnnotationsAttribute should have been called via delegation
        verify(parameterAnnotationsAttribute, times(1))
            .annotationsAccept(any(), any(), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute maintains correct order when called with multiple attributes.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_sequentialCalls_maintainIndependence() {
        // Arrange
        ParameterAnnotationsAttribute attr1 = mock(ParameterAnnotationsAttribute.class);
        ParameterAnnotationsAttribute attr2 = mock(ParameterAnnotationsAttribute.class);

        // Act - call with first attribute
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, attr1);
        verify(attr1).annotationsAccept(clazz, method, changer);

        // Act - call with second attribute
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, attr2);
        verify(attr2).annotationsAccept(clazz, method, changer);

        // Assert - first attribute should not have been called again
        verify(attr1, times(1)).annotationsAccept(any(), any(), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute integrates correctly with the visitor pattern.
     * The changer implements AnnotationVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_changerIsValidVisitor() {
        // Arrange & Assert - verify the changer is an instance of AnnotationVisitor
        assertTrue(changer instanceof AnnotationVisitor,
            "Changer should implement AnnotationVisitor to be used as a visitor");

        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify it's passed as an AnnotationVisitor
        verify(parameterAnnotationsAttribute).annotationsAccept(
            any(Clazz.class),
            any(Method.class),
            any(AnnotationVisitor.class)
        );
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute handles repeated calls with the same parameters idempotently.
     * While each call should delegate, the delegation should be consistent.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_repeatedCallsWithSameParameters_delegatesConsistently() {
        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify delegation happened twice with identical parameters
        verify(parameterAnnotationsAttribute, times(2))
            .annotationsAccept(same(clazz), same(method), same(changer));
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute properly integrates with all three parameters.
     * This is a comprehensive test verifying the complete parameter flow.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_integratesAllParameters() {
        // Arrange - create specific mocks
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        Method specificMethod = mock(Method.class, "specificMethod");
        ParameterAnnotationsAttribute specificAttr = mock(ParameterAnnotationsAttribute.class, "specificAttr");

        // Act
        changer.visitAnyParameterAnnotationsAttribute(specificClazz, specificMethod, specificAttr);

        // Assert - verify all specific parameters were passed correctly
        verify(specificAttr).annotationsAccept(specificClazz, specificMethod, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works correctly across different changer instances.
     * Different changers should independently delegate to their parameter annotations.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withDifferentChangers_delegatesIndependently() {
        // Arrange
        TargetClassChanger changer2 = new TargetClassChanger();

        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        changer2.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify each changer was passed independently
        verify(parameterAnnotationsAttribute, times(1)).annotationsAccept(clazz, method, changer);
        verify(parameterAnnotationsAttribute, times(1)).annotationsAccept(clazz, method, changer2);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute handles the visitor pattern delegation correctly.
     * The method should pass the changer (which is an AnnotationVisitor) to process parameter annotations.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_delegationFollowsVisitorPattern() {
        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify the method delegates by calling annotationsAccept exactly once
        verify(parameterAnnotationsAttribute, times(1))
            .annotationsAccept(any(Clazz.class), any(Method.class), any(AnnotationVisitor.class));
    }

    /**
     * Tests visitAnyParameterAnnotationsAttribute can handle multiple different combinations.
     * This tests the method's ability to handle various combinations of inputs.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withMultipleCombinations() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class, "clazz1");
        Clazz clazz2 = mock(ProgramClass.class, "clazz2");
        Method method1 = mock(Method.class, "method1");
        Method method2 = mock(Method.class, "method2");
        ParameterAnnotationsAttribute attr1 = mock(ParameterAnnotationsAttribute.class, "attr1");
        ParameterAnnotationsAttribute attr2 = mock(ParameterAnnotationsAttribute.class, "attr2");

        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz1, method1, attr1);
        changer.visitAnyParameterAnnotationsAttribute(clazz1, method2, attr2);
        changer.visitAnyParameterAnnotationsAttribute(clazz2, method1, attr1);
        changer.visitAnyParameterAnnotationsAttribute(clazz2, method2, attr2);

        // Assert - verify all combinations were processed
        verify(attr1).annotationsAccept(clazz1, method1, changer);
        verify(attr2).annotationsAccept(clazz1, method2, changer);
        verify(attr1).annotationsAccept(clazz2, method1, changer);
        verify(attr2).annotationsAccept(clazz2, method2, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute properly delegates without modifying the attribute.
     * The method should only delegate, not modify the attribute directly.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_doesNotModifyAttributeDirectly() {
        // Arrange
        ParameterAnnotationsAttribute spyAttribute = mock(ParameterAnnotationsAttribute.class);

        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, spyAttribute);

        // Assert - verify only annotationsAccept was called, nothing else
        verify(spyAttribute, times(1)).annotationsAccept(any(), any(), any());
        verifyNoMoreInteractions(spyAttribute);
    }

    /**
     * Tests visitAnyParameterAnnotationsAttribute execution completes quickly.
     * Since it's a simple delegation method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitAnyParameterAnnotationsAttribute should execute quickly as it's a simple delegation");
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute maintains the correct contract for the visitor pattern.
     * The method name starts with "visitAny" indicating it handles any type of parameter annotations attribute.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_handlesAnyParameterAnnotationsAttribute() {
        // Arrange - could be RuntimeVisibleParameterAnnotations, RuntimeInvisibleParameterAnnotations, etc.
        ParameterAnnotationsAttribute runtimeVisible = mock(ParameterAnnotationsAttribute.class, "runtimeVisible");
        ParameterAnnotationsAttribute runtimeInvisible = mock(ParameterAnnotationsAttribute.class, "runtimeInvisible");

        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, runtimeVisible);
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, runtimeInvisible);

        // Assert - verify both types were processed
        verify(runtimeVisible).annotationsAccept(clazz, method, changer);
        verify(runtimeInvisible).annotationsAccept(clazz, method, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute handles methods with different parameter counts.
     * Parameter annotations can exist for methods with various numbers of parameters.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withDifferentParameterCounts() {
        // Arrange
        Method noParamMethod = mock(Method.class, "noParams");
        Method oneParamMethod = mock(Method.class, "oneParam");
        Method multiParamMethod = mock(Method.class, "multiParams");

        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, noParamMethod, parameterAnnotationsAttribute);
        changer.visitAnyParameterAnnotationsAttribute(clazz, oneParamMethod, parameterAnnotationsAttribute);
        changer.visitAnyParameterAnnotationsAttribute(clazz, multiParamMethod, parameterAnnotationsAttribute);

        // Assert - verify all were processed regardless of parameter count
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, noParamMethod, changer);
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, oneParamMethod, changer);
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, multiParamMethod, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works with LibraryClass as well as ProgramClass.
     * The method should handle both types of Clazz implementations.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withLibraryClass_delegatesCorrectly() {
        // Arrange
        Clazz libraryClass = mock(LibraryClass.class);

        // Act
        changer.visitAnyParameterAnnotationsAttribute(libraryClass, method, parameterAnnotationsAttribute);

        // Assert - verify delegation occurred with library class
        verify(parameterAnnotationsAttribute).annotationsAccept(libraryClass, method, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works with a mix of ProgramClass and LibraryClass.
     * This ensures the method is polymorphic over Clazz implementations.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withMixedClazzTypes_handlesAll() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act
        changer.visitAnyParameterAnnotationsAttribute(programClass, method, parameterAnnotationsAttribute);
        changer.visitAnyParameterAnnotationsAttribute(libraryClass, method, parameterAnnotationsAttribute);

        // Assert - verify both types were processed
        verify(parameterAnnotationsAttribute).annotationsAccept(programClass, method, changer);
        verify(parameterAnnotationsAttribute).annotationsAccept(libraryClass, method, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute can be called in rapid succession without issues.
     * This verifies there's no timing-dependent behavior.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify delegation occurred for all calls
        verify(parameterAnnotationsAttribute, times(1000)).annotationsAccept(clazz, method, changer);
    }

    /**
     * Tests that multiple changers can call visitAnyParameterAnnotationsAttribute independently.
     * Each changer should operate independently without interfering with others.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_multipleChangersNoInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        ParameterAnnotationsAttribute attr1 = mock(ParameterAnnotationsAttribute.class);
        ParameterAnnotationsAttribute attr2 = mock(ParameterAnnotationsAttribute.class);

        // Act - different changers process different attributes
        changer1.visitAnyParameterAnnotationsAttribute(clazz1, method1, attr1);
        changer2.visitAnyParameterAnnotationsAttribute(clazz2, method2, attr2);
        changer3.visitAnyParameterAnnotationsAttribute(clazz1, method2, attr2);

        // Assert - verify correct delegations occurred
        verify(attr1).annotationsAccept(clazz1, method1, changer1);
        verify(attr2).annotationsAccept(clazz2, method2, changer2);
        verify(attr2).annotationsAccept(clazz1, method2, changer3);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works correctly when the TargetClassChanger
     * is used polymorphically as an AttributeVisitor.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_asAttributeVisitorInterface_doesNotThrowException() {
        // Arrange - treat changer as AttributeVisitor interface
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = changer;

        // Act & Assert - should work the same way through the interface
        assertDoesNotThrow(() -> visitor.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute));
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute called on newly created changer instance behaves consistently.
     * Verifies the method works immediately after construction without initialization.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_onNewInstance_behavesConsistently() {
        // Arrange - create a fresh changer
        TargetClassChanger freshChanger = new TargetClassChanger();

        // Act - immediately call visitAnyParameterAnnotationsAttribute
        freshChanger.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - should delegate properly
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method, freshChanger);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute handles edge case of calling with various
     * combinations of different clazz, method, and attribute tuples in sequence.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_sequentialCallsWithDifferentParameters_noIssues() {
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
        ParameterAnnotationsAttribute[] attributes = {
            mock(ParameterAnnotationsAttribute.class),
            mock(ParameterAnnotationsAttribute.class),
            mock(ParameterAnnotationsAttribute.class)
        };

        // Act - call with different combinations
        for (Clazz c : clazzes) {
            for (Method m : methods) {
                for (ParameterAnnotationsAttribute attr : attributes) {
                    changer.visitAnyParameterAnnotationsAttribute(c, m, attr);
                }
            }
        }

        // Assert - verify delegations for all combinations
        for (Clazz c : clazzes) {
            for (Method m : methods) {
                for (ParameterAnnotationsAttribute attr : attributes) {
                    verify(attr).annotationsAccept(c, m, changer);
                }
            }
        }
    }

    /**
     * Tests that the visitAnyParameterAnnotationsAttribute method signature matches the AttributeVisitor interface.
     * This ensures the method properly overrides the interface method.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_implementsInterfaceCorrectly() {
        // Assert - TargetClassChanger should be an AttributeVisitor
        assertTrue(changer instanceof proguard.classfile.attribute.visitor.AttributeVisitor,
                "TargetClassChanger should implement AttributeVisitor");
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute doesn't affect the changer's internal state.
     * Since it's a pure delegation method, it should not modify any internal fields.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_doesNotAffectInternalState() {
        // Act - call visitAnyParameterAnnotationsAttribute multiple times
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify delegations occurred without side effects to parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verify(parameterAnnotationsAttribute, times(2)).annotationsAccept(clazz, method, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute with varying combinations of parameters
     * all result in proper delegation.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_varyingParameterCombinations_consistentDelegation() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Method method2 = mock(Method.class);
        ParameterAnnotationsAttribute attr2 = mock(ParameterAnnotationsAttribute.class);

        // Act - call with various parameter combinations
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        changer.visitAnyParameterAnnotationsAttribute(clazz2, method2, attr2);
        changer.visitAnyParameterAnnotationsAttribute(clazz, method2, attr2);

        // Assert - verify proper delegation for all combinations
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method, changer);
        verify(attr2).annotationsAccept(clazz2, method2, changer);
        verify(attr2).annotationsAccept(clazz, method2, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute completes without blocking or hanging.
     * This ensures the method doesn't have any unexpected wait conditions.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnyParameterAnnotationsAttribute should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute handles the same attribute instance multiple times
     * without accumulating state or causing issues.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_sameAttributeInstanceMultipleTimes_noAccumulation() {
        // Arrange
        ParameterAnnotationsAttribute singleAttribute = mock(ParameterAnnotationsAttribute.class);

        // Act - call multiple times with the same attribute
        for (int i = 0; i < 10; i++) {
            changer.visitAnyParameterAnnotationsAttribute(clazz, method, singleAttribute);
        }

        // Assert - verify delegation occurred 10 times
        verify(singleAttribute, times(10)).annotationsAccept(clazz, method, changer);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute returns immediately without performing operations beyond delegation.
     * This confirms the pure delegation nature of the method.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_returnsImmediatelyAfterDelegation() {
        // Arrange
        ParameterAnnotationsAttribute trackedAttribute = mock(ParameterAnnotationsAttribute.class);

        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, trackedAttribute);

        // Assert - only delegation call should have been made
        verify(trackedAttribute, times(1)).annotationsAccept(any(), any(), any());
        verifyNoMoreInteractions(trackedAttribute);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute called after multiple other operations still delegates correctly.
     * This verifies consistent behavior regardless of the changer's usage history.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_afterMultipleOperations_stillDelegates() {
        // Arrange - simulate some prior operations
        for (int i = 0; i < 5; i++) {
            changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        }

        // Act - call visitAnyParameterAnnotationsAttribute again
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify delegation occurred 6 times total
        verify(parameterAnnotationsAttribute, times(6)).annotationsAccept(clazz, method, changer);
    }

    /**
     * Tests that the visitor passed to annotationsAccept is indeed the changer itself.
     * This verifies that the changer can receive callbacks from the attribute.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_changerAsVisitorReceivesCallbacks() {
        // Act
        changer.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify the exact same changer instance is passed
        verify(parameterAnnotationsAttribute).annotationsAccept(
            argThat(c -> c == clazz),
            argThat(m -> m == method),
            argThat(v -> v == changer)
        );
    }
}

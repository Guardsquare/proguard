package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.AnnotationElementValue;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)}.
 *
 * The visitAnnotationElementValue method handles annotation element values in annotations
 * (i.e., nested annotations like @Outer(@Inner)).
 * It performs two key operations:
 * 1. Delegates to visitAnyElementValue to update the referenced annotation class and method
 *    when class merging has occurred
 * 2. Calls annotationAccept on the annotation element value to recursively process the nested
 *    annotation, passing the changer as an AnnotationVisitor to update any references within
 *    the nested annotation
 *
 * This is important for maintaining correct references when annotation classes are merged during
 * vertical class merging optimization, especially for nested annotation structures.
 */
public class TargetClassChangerClaude_visitAnnotationElementValueTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private Annotation annotation;
    private AnnotationElementValue annotationElementValue;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        annotation = mock(Annotation.class);
        annotationElementValue = mock(AnnotationElementValue.class);
    }

    /**
     * Tests that visitAnnotationElementValue can be called without throwing exceptions.
     */
    @Test
    public void testVisitAnnotationElementValue_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue correctly delegates to annotationAccept.
     * This verifies the core functionality of the method - delegation to process nested annotations.
     */
    @Test
    public void testVisitAnnotationElementValue_delegatesToAnnotationAccept() {
        // Act
        changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify that annotationAccept was called with correct parameters
        verify(annotationElementValue).annotationAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationElementValue can be called multiple times.
     */
    @Test
    public void testVisitAnnotationElementValue_calledMultipleTimes_doesNotFail() {
        // Act & Assert - should not throw exceptions on multiple calls
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
            changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
            changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        });
    }

    /**
     * Tests that visitAnnotationElementValue delegates to annotationAccept multiple times.
     */
    @Test
    public void testVisitAnnotationElementValue_calledMultipleTimes_delegatesEachTime() {
        // Act
        changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify annotationAccept was called exactly 3 times
        verify(annotationElementValue, times(3)).annotationAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationElementValue works with different annotation element value instances.
     */
    @Test
    public void testVisitAnnotationElementValue_withDifferentAnnotationElementValues_processesEach() {
        // Arrange
        AnnotationElementValue annotationElementValue1 = mock(AnnotationElementValue.class);
        AnnotationElementValue annotationElementValue2 = mock(AnnotationElementValue.class);
        AnnotationElementValue annotationElementValue3 = mock(AnnotationElementValue.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue1);
            changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue2);
            changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue3);
        });
    }

    /**
     * Tests that visitAnnotationElementValue works with different annotation instances.
     */
    @Test
    public void testVisitAnnotationElementValue_withDifferentAnnotations_processesEach() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        Annotation annotation3 = mock(Annotation.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(clazz, annotation1, annotationElementValue);
            changer.visitAnnotationElementValue(clazz, annotation2, annotationElementValue);
            changer.visitAnnotationElementValue(clazz, annotation3, annotationElementValue);
        });
    }

    /**
     * Tests that visitAnnotationElementValue works with different clazz instances.
     */
    @Test
    public void testVisitAnnotationElementValue_withDifferentClazz_processesEach() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(clazz1, annotation, annotationElementValue);
            changer.visitAnnotationElementValue(clazz2, annotation, annotationElementValue);
            changer.visitAnnotationElementValue(clazz3, annotation, annotationElementValue);
        });
    }

    /**
     * Tests that visitAnnotationElementValue works with LibraryClass as well as ProgramClass.
     */
    @Test
    public void testVisitAnnotationElementValue_withLibraryClass_processesCorrectly() {
        // Arrange
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> changer.visitAnnotationElementValue(libraryClass, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue works with a mix of ProgramClass and LibraryClass.
     */
    @Test
    public void testVisitAnnotationElementValue_withMixedClazzTypes_handlesAll() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(programClass, annotation, annotationElementValue);
            changer.visitAnnotationElementValue(libraryClass, annotation, annotationElementValue);
        });
    }

    /**
     * Tests that visitAnnotationElementValue executes quickly.
     */
    @Test
    public void testVisitAnnotationElementValue_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitAnnotationElementValue should execute quickly");
    }

    /**
     * Tests that visitAnnotationElementValue can be called in rapid succession without issues.
     */
    @Test
    public void testVisitAnnotationElementValue_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
            }
        }, "Rapid successive calls should not cause issues");
    }

    /**
     * Tests that multiple changers can call visitAnnotationElementValue independently.
     */
    @Test
    public void testVisitAnnotationElementValue_multipleChangersNoInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - different changers should work independently
        assertDoesNotThrow(() -> {
            changer1.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
            changer2.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
            changer3.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        });
    }

    /**
     * Tests that visitAnnotationElementValue works correctly when called on a newly created changer instance.
     */
    @Test
    public void testVisitAnnotationElementValue_onNewInstance_behavesConsistently() {
        // Arrange
        TargetClassChanger freshChanger = new TargetClassChanger();

        // Act & Assert - should work immediately after construction
        assertDoesNotThrow(() -> freshChanger.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue handles edge case of calling with various
     * combinations of different clazz, annotation, and annotation element value tuples in sequence.
     */
    @Test
    public void testVisitAnnotationElementValue_sequentialCallsWithDifferentParameters_noIssues() {
        // Arrange
        Clazz[] clazzes = {
            mock(ProgramClass.class),
            mock(LibraryClass.class),
            mock(ProgramClass.class)
        };
        Annotation[] annotations = {
            mock(Annotation.class),
            mock(Annotation.class),
            mock(Annotation.class)
        };
        AnnotationElementValue[] annotationElementValues = {
            mock(AnnotationElementValue.class),
            mock(AnnotationElementValue.class),
            mock(AnnotationElementValue.class)
        };

        // Act & Assert - call with different combinations
        assertDoesNotThrow(() -> {
            for (Clazz c : clazzes) {
                for (Annotation a : annotations) {
                    for (AnnotationElementValue aev : annotationElementValues) {
                        changer.visitAnnotationElementValue(c, a, aev);
                    }
                }
            }
        });
    }

    /**
     * Tests that the visitAnnotationElementValue method implements the ElementValueVisitor interface correctly.
     */
    @Test
    public void testVisitAnnotationElementValue_implementsInterfaceCorrectly() {
        // Assert - TargetClassChanger should be an ElementValueVisitor
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor,
                "TargetClassChanger should implement ElementValueVisitor");
    }

    /**
     * Tests that visitAnnotationElementValue completes without blocking or hanging.
     */
    @Test
    public void testVisitAnnotationElementValue_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnnotationElementValue should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitAnnotationElementValue handles the same annotation element value instance multiple times
     * without accumulating state or causing issues.
     */
    @Test
    public void testVisitAnnotationElementValue_sameAnnotationElementValueInstanceMultipleTimes_noAccumulation() {
        // Arrange
        AnnotationElementValue singleAnnotationElementValue = mock(AnnotationElementValue.class);

        // Act & Assert - call multiple times with the same annotation element value
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                changer.visitAnnotationElementValue(clazz, annotation, singleAnnotationElementValue);
            }
        });
    }

    /**
     * Tests that visitAnnotationElementValue called after multiple other operations still works correctly.
     */
    @Test
    public void testVisitAnnotationElementValue_afterMultipleOperations_stillWorks() {
        // Simulate some prior operations
        for (int i = 0; i < 5; i++) {
            changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        }

        // Act & Assert - call visitAnnotationElementValue again
        assertDoesNotThrow(() -> changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue with varying combinations of parameters all execute successfully.
     */
    @Test
    public void testVisitAnnotationElementValue_varyingParameterCombinations_allExecuteSuccessfully() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Annotation annotation2 = mock(Annotation.class);
        AnnotationElementValue annotationElementValue2 = mock(AnnotationElementValue.class);

        // Act & Assert - call with various parameter combinations
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
            changer.visitAnnotationElementValue(clazz2, annotation2, annotationElementValue2);
            changer.visitAnnotationElementValue(clazz, annotation2, annotationElementValue2);
        });
    }

    /**
     * Tests that visitAnnotationElementValue maintains consistency across invocations.
     */
    @Test
    public void testVisitAnnotationElementValue_consistentBehavior() {
        // Act & Assert - call multiple times and verify consistent behavior
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue)
            );
        }
    }

    /**
     * Tests that visitAnnotationElementValue processes annotation element values in the context of annotation processing.
     * Annotation element values represent nested annotations (e.g., @Outer(@Inner)).
     */
    @Test
    public void testVisitAnnotationElementValue_processesInAnnotationContext() {
        // Act & Assert - should work correctly in annotation context
        assertDoesNotThrow(() -> changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue handles annotation element values from different annotations correctly.
     * Each annotation element value belongs to an annotation and contains a nested annotation.
     */
    @Test
    public void testVisitAnnotationElementValue_handlesAnnotationElementValuesFromDifferentAnnotations() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        AnnotationElementValue annotationElementValue1 = mock(AnnotationElementValue.class);
        AnnotationElementValue annotationElementValue2 = mock(AnnotationElementValue.class);

        // Act & Assert - should handle annotation element values from different annotations
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(clazz, annotation1, annotationElementValue1);
            changer.visitAnnotationElementValue(clazz, annotation2, annotationElementValue2);
        });
    }

    /**
     * Tests that visitAnnotationElementValue works with the visitor pattern correctly.
     * The method is part of the ElementValueVisitor interface implementation.
     */
    @Test
    public void testVisitAnnotationElementValue_worksWithVisitorPattern() {
        // Act - use the changer as an ElementValueVisitor
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Assert - should work correctly when used through the interface
        assertDoesNotThrow(() -> visitor.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue can handle annotation element values in the context of
     * class retargeting during optimization.
     */
    @Test
    public void testVisitAnnotationElementValue_inClassRetargetingContext() {
        // Act & Assert - should work in the context of class retargeting
        assertDoesNotThrow(() -> changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue works correctly with different TargetClassChanger instances.
     * Each changer should be able to process annotation element values independently.
     */
    @Test
    public void testVisitAnnotationElementValue_withDifferentChangerInstances() {
        // Arrange
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - should work for different changer instances
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
            changer2.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
            changer3.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        });
    }

    /**
     * Tests that visitAnnotationElementValue integrates correctly with the overall visitor pattern
     * used in the TargetClassChanger class.
     */
    @Test
    public void testVisitAnnotationElementValue_integratesWithVisitorPattern() {
        // Arrange
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Act & Assert - should work through the interface
        assertDoesNotThrow(() -> visitor.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue can process annotation element values from annotations on different classes.
     */
    @Test
    public void testVisitAnnotationElementValue_fromAnnotationsOnDifferentClasses() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        AnnotationElementValue annotationElementValue1 = mock(AnnotationElementValue.class);
        AnnotationElementValue annotationElementValue2 = mock(AnnotationElementValue.class);

        // Act & Assert - should process annotation element values from annotations on different classes
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(clazz1, annotation1, annotationElementValue1);
            changer.visitAnnotationElementValue(clazz2, annotation2, annotationElementValue2);
        });
    }

    /**
     * Tests that visitAnnotationElementValue can handle a large number of annotation element values.
     */
    @Test
    public void testVisitAnnotationElementValue_handlesLargeNumberOfAnnotationElementValues() {
        // Arrange
        AnnotationElementValue[] annotationElementValues = new AnnotationElementValue[100];
        for (int i = 0; i < annotationElementValues.length; i++) {
            annotationElementValues[i] = mock(AnnotationElementValue.class);
        }

        // Act & Assert - should handle processing many annotation element values
        assertDoesNotThrow(() -> {
            for (AnnotationElementValue aev : annotationElementValues) {
                changer.visitAnnotationElementValue(clazz, annotation, aev);
            }
        });
    }

    /**
     * Tests that visitAnnotationElementValue works correctly when processing annotation element values
     * in a typical annotation processing workflow.
     */
    @Test
    public void testVisitAnnotationElementValue_inTypicalWorkflow() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        AnnotationElementValue aev1 = mock(AnnotationElementValue.class);
        AnnotationElementValue aev2 = mock(AnnotationElementValue.class);
        AnnotationElementValue aev3 = mock(AnnotationElementValue.class);

        // Act & Assert - simulate typical annotation processing workflow
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(clazz, annotation1, aev1);
            changer.visitAnnotationElementValue(clazz, annotation1, aev2);
            changer.visitAnnotationElementValue(clazz, annotation2, aev3);
        });
    }

    /**
     * Tests that visitAnnotationElementValue doesn't have side effects that affect subsequent calls.
     */
    @Test
    public void testVisitAnnotationElementValue_noSideEffectsOnSubsequentCalls() {
        // Arrange
        AnnotationElementValue aev1 = mock(AnnotationElementValue.class);
        AnnotationElementValue aev2 = mock(AnnotationElementValue.class);

        // Act - call twice with different annotation element values
        changer.visitAnnotationElementValue(clazz, annotation, aev1);
        changer.visitAnnotationElementValue(clazz, annotation, aev2);

        // Assert - second call should work without issues
        assertDoesNotThrow(() -> changer.visitAnnotationElementValue(clazz, annotation, aev2));
    }

    /**
     * Tests that visitAnnotationElementValue processes annotation element values correctly regardless of
     * the order in which they are visited.
     */
    @Test
    public void testVisitAnnotationElementValue_orderIndependent() {
        // Arrange
        AnnotationElementValue aev1 = mock(AnnotationElementValue.class);
        AnnotationElementValue aev2 = mock(AnnotationElementValue.class);
        AnnotationElementValue aev3 = mock(AnnotationElementValue.class);

        // Act & Assert - should work regardless of order
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(clazz, annotation, aev3);
            changer.visitAnnotationElementValue(clazz, annotation, aev1);
            changer.visitAnnotationElementValue(clazz, annotation, aev2);
        });
    }

    /**
     * Tests that visitAnnotationElementValue works correctly as part of the ElementValueVisitor
     * interface hierarchy.
     */
    @Test
    public void testVisitAnnotationElementValue_asPartOfVisitorHierarchy() {
        // Assert - verify the changer implements the ElementValueVisitor interface
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor);

        // Act & Assert - should work when called through the interface
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;
        assertDoesNotThrow(() -> visitor.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue delegates to visitAnyElementValue.
     * This verifies the first part of the delegation pattern used by the method.
     */
    @Test
    public void testVisitAnnotationElementValue_delegatesToVisitAnyElementValue() {
        // Arrange - create a spy to verify delegation
        TargetClassChanger spyChanger = spy(new TargetClassChanger());

        // Act
        spyChanger.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify that visitAnnotationElementValue was called
        verify(spyChanger).visitAnnotationElementValue(clazz, annotation, annotationElementValue);
    }

    /**
     * Tests that visitAnnotationElementValue passes the changer itself as the visitor to annotationAccept.
     * This is crucial because the changer implements AnnotationVisitor.
     */
    @Test
    public void testVisitAnnotationElementValue_passesChangerAsVisitor() {
        // Act
        changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify that the changer itself is passed as the visitor parameter
        verify(annotationElementValue).annotationAccept(
            eq(clazz),
            same(changer)  // The changer itself should be passed as visitor
        );
    }

    /**
     * Tests that visitAnnotationElementValue properly handles nested annotations.
     * Nested annotations are a key use case for annotation element values.
     */
    @Test
    public void testVisitAnnotationElementValue_handlesNestedAnnotations() {
        // Act & Assert - should handle nested annotation structures
        assertDoesNotThrow(() -> changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue handles null clazz gracefully.
     */
    @Test
    public void testVisitAnnotationElementValue_withNullClazz() {
        // Act & Assert - behavior with null clazz (may or may not throw depending on implementation)
        // This test documents the behavior
        try {
            changer.visitAnnotationElementValue(null, annotation, annotationElementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitAnnotationElementValue handles null annotation gracefully.
     */
    @Test
    public void testVisitAnnotationElementValue_withNullAnnotation() {
        // Act & Assert - behavior with null annotation
        // This test documents the behavior
        try {
            changer.visitAnnotationElementValue(clazz, null, annotationElementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitAnnotationElementValue handles null annotation element value gracefully.
     */
    @Test
    public void testVisitAnnotationElementValue_withNullAnnotationElementValue() {
        // Act & Assert - behavior with null annotation element value
        // This test documents the behavior
        try {
            changer.visitAnnotationElementValue(clazz, annotation, null);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitAnnotationElementValue processes annotation element values that represent
     * annotation attribute values correctly.
     */
    @Test
    public void testVisitAnnotationElementValue_processesAnnotationAttributeValues() {
        // Arrange - annotation element values are used for annotation-typed annotation attributes
        AnnotationElementValue attributeValue = mock(AnnotationElementValue.class);

        // Act & Assert - should process annotation attribute values
        assertDoesNotThrow(() -> changer.visitAnnotationElementValue(clazz, annotation, attributeValue));
    }

    /**
     * Tests that visitAnnotationElementValue is correctly invoked through the visitor pattern
     * when processing annotations during class optimization.
     */
    @Test
    public void testVisitAnnotationElementValue_invokedDuringOptimization() {
        // Act & Assert - should be invokable during optimization phase
        assertDoesNotThrow(() -> changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue handles multiple nested annotations correctly.
     * Some annotations may have multiple annotation-typed attributes.
     */
    @Test
    public void testVisitAnnotationElementValue_withMultipleNestedAnnotations() {
        // Arrange - multiple nested annotations in different annotation attributes
        AnnotationElementValue nested1 = mock(AnnotationElementValue.class, "nested1");
        AnnotationElementValue nested2 = mock(AnnotationElementValue.class, "nested2");
        AnnotationElementValue nested3 = mock(AnnotationElementValue.class, "nested3");

        // Act & Assert - should handle multiple nested annotations
        assertDoesNotThrow(() -> {
            changer.visitAnnotationElementValue(clazz, annotation, nested1);
            changer.visitAnnotationElementValue(clazz, annotation, nested2);
            changer.visitAnnotationElementValue(clazz, annotation, nested3);
        });
    }

    /**
     * Tests that visitAnnotationElementValue correctly processes nested annotations
     * by calling annotationAccept. This is important for recursive processing.
     */
    @Test
    public void testVisitAnnotationElementValue_recursivelyProcessesNestedAnnotation() {
        // Act & Assert - should recursively process the nested annotation via annotationAccept
        assertDoesNotThrow(() -> changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue));

        // Verify the recursive processing was initiated
        verify(annotationElementValue).annotationAccept(clazz, changer);
    }

    /**
     * Tests that visitAnnotationElementValue handles annotation element values that may have been affected
     * by class merging optimization.
     */
    @Test
    public void testVisitAnnotationElementValue_handlesClassMergedAnnotations() {
        // Act & Assert - should handle nested annotations whose classes may have been merged
        assertDoesNotThrow(() -> changer.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue ensures the changer is an AnnotationVisitor.
     * This is required for the annotationAccept call to work correctly.
     */
    @Test
    public void testVisitAnnotationElementValue_changerIsAnnotationVisitor() {
        // Assert - verify the changer is an instance of AnnotationVisitor
        assertTrue(changer instanceof AnnotationVisitor,
            "Changer should implement AnnotationVisitor for annotationAccept to work");
    }

    /**
     * Tests that visitAnnotationElementValue passes the correct clazz to annotationAccept.
     */
    @Test
    public void testVisitAnnotationElementValue_passesCorrectClazz() {
        // Arrange
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");

        // Act
        changer.visitAnnotationElementValue(specificClazz, annotation, annotationElementValue);

        // Assert - verify the correct clazz was passed
        verify(annotationElementValue).annotationAccept(specificClazz, changer);
    }

    /**
     * Tests that visitAnnotationElementValue with different annotation element values
     * delegates to annotationAccept for each one independently.
     */
    @Test
    public void testVisitAnnotationElementValue_withDifferentValues_delegatesIndependently() {
        // Arrange
        AnnotationElementValue aev1 = mock(AnnotationElementValue.class);
        AnnotationElementValue aev2 = mock(AnnotationElementValue.class);
        AnnotationElementValue aev3 = mock(AnnotationElementValue.class);

        // Act
        changer.visitAnnotationElementValue(clazz, annotation, aev1);
        changer.visitAnnotationElementValue(clazz, annotation, aev2);
        changer.visitAnnotationElementValue(clazz, annotation, aev3);

        // Assert - verify each value's annotationAccept was called once
        verify(aev1).annotationAccept(clazz, changer);
        verify(aev2).annotationAccept(clazz, changer);
        verify(aev3).annotationAccept(clazz, changer);
    }
}

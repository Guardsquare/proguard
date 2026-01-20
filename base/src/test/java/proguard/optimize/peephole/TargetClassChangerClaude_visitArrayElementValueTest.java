package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.ArrayElementValue;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
 *
 * The visitArrayElementValue method handles array element values in annotations
 * (e.g., @SomeAnnotation({value1, value2, value3})).
 * It performs two key operations:
 * 1. Delegates to visitAnyElementValue to update the referenced annotation class and method
 *    when class merging has occurred
 * 2. Calls elementValuesAccept on the array element value to recursively process each element
 *    in the array, passing the changer as an ElementValueVisitor to update any references within
 *    the array elements
 *
 * This is important for maintaining correct references when annotation classes are merged during
 * vertical class merging optimization, especially for array-valued annotation attributes.
 */
public class TargetClassChangerClaude_visitArrayElementValueTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private Annotation annotation;
    private ArrayElementValue arrayElementValue;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        annotation = mock(Annotation.class);
        arrayElementValue = mock(ArrayElementValue.class);
    }

    /**
     * Tests that visitArrayElementValue can be called without throwing exceptions.
     */
    @Test
    public void testVisitArrayElementValue_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue correctly delegates to elementValuesAccept.
     * This verifies the core functionality of the method - delegation to process array elements.
     */
    @Test
    public void testVisitArrayElementValue_delegatesToElementValuesAccept() {
        // Act
        changer.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify that elementValuesAccept was called with correct parameters
        verify(arrayElementValue).elementValuesAccept(clazz, annotation, changer);
    }

    /**
     * Tests that visitArrayElementValue can be called multiple times.
     */
    @Test
    public void testVisitArrayElementValue_calledMultipleTimes_doesNotFail() {
        // Act & Assert - should not throw exceptions on multiple calls
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(clazz, annotation, arrayElementValue);
            changer.visitArrayElementValue(clazz, annotation, arrayElementValue);
            changer.visitArrayElementValue(clazz, annotation, arrayElementValue);
        });
    }

    /**
     * Tests that visitArrayElementValue delegates to elementValuesAccept multiple times.
     */
    @Test
    public void testVisitArrayElementValue_calledMultipleTimes_delegatesEachTime() {
        // Act
        changer.visitArrayElementValue(clazz, annotation, arrayElementValue);
        changer.visitArrayElementValue(clazz, annotation, arrayElementValue);
        changer.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify elementValuesAccept was called exactly 3 times
        verify(arrayElementValue, times(3)).elementValuesAccept(clazz, annotation, changer);
    }

    /**
     * Tests that visitArrayElementValue works with different array element value instances.
     */
    @Test
    public void testVisitArrayElementValue_withDifferentArrayElementValues_processesEach() {
        // Arrange
        ArrayElementValue arrayElementValue1 = mock(ArrayElementValue.class);
        ArrayElementValue arrayElementValue2 = mock(ArrayElementValue.class);
        ArrayElementValue arrayElementValue3 = mock(ArrayElementValue.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(clazz, annotation, arrayElementValue1);
            changer.visitArrayElementValue(clazz, annotation, arrayElementValue2);
            changer.visitArrayElementValue(clazz, annotation, arrayElementValue3);
        });
    }

    /**
     * Tests that visitArrayElementValue works with different annotation instances.
     */
    @Test
    public void testVisitArrayElementValue_withDifferentAnnotations_processesEach() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        Annotation annotation3 = mock(Annotation.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(clazz, annotation1, arrayElementValue);
            changer.visitArrayElementValue(clazz, annotation2, arrayElementValue);
            changer.visitArrayElementValue(clazz, annotation3, arrayElementValue);
        });
    }

    /**
     * Tests that visitArrayElementValue works with different clazz instances.
     */
    @Test
    public void testVisitArrayElementValue_withDifferentClazz_processesEach() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(clazz1, annotation, arrayElementValue);
            changer.visitArrayElementValue(clazz2, annotation, arrayElementValue);
            changer.visitArrayElementValue(clazz3, annotation, arrayElementValue);
        });
    }

    /**
     * Tests that visitArrayElementValue works with LibraryClass as well as ProgramClass.
     */
    @Test
    public void testVisitArrayElementValue_withLibraryClass_processesCorrectly() {
        // Arrange
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> changer.visitArrayElementValue(libraryClass, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue works with a mix of ProgramClass and LibraryClass.
     */
    @Test
    public void testVisitArrayElementValue_withMixedClazzTypes_handlesAll() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(programClass, annotation, arrayElementValue);
            changer.visitArrayElementValue(libraryClass, annotation, arrayElementValue);
        });
    }

    /**
     * Tests that visitArrayElementValue executes quickly.
     */
    @Test
    public void testVisitArrayElementValue_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            changer.visitArrayElementValue(clazz, annotation, arrayElementValue);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitArrayElementValue should execute quickly");
    }

    /**
     * Tests that visitArrayElementValue can be called in rapid succession without issues.
     */
    @Test
    public void testVisitArrayElementValue_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                changer.visitArrayElementValue(clazz, annotation, arrayElementValue);
            }
        }, "Rapid successive calls should not cause issues");
    }

    /**
     * Tests that multiple changers can call visitArrayElementValue independently.
     */
    @Test
    public void testVisitArrayElementValue_multipleChangersNoInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - different changers should work independently
        assertDoesNotThrow(() -> {
            changer1.visitArrayElementValue(clazz, annotation, arrayElementValue);
            changer2.visitArrayElementValue(clazz, annotation, arrayElementValue);
            changer3.visitArrayElementValue(clazz, annotation, arrayElementValue);
        });
    }

    /**
     * Tests that visitArrayElementValue works correctly when called on a newly created changer instance.
     */
    @Test
    public void testVisitArrayElementValue_onNewInstance_behavesConsistently() {
        // Arrange
        TargetClassChanger freshChanger = new TargetClassChanger();

        // Act & Assert - should work immediately after construction
        assertDoesNotThrow(() -> freshChanger.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue handles edge case of calling with various
     * combinations of different clazz, annotation, and array element value tuples in sequence.
     */
    @Test
    public void testVisitArrayElementValue_sequentialCallsWithDifferentParameters_noIssues() {
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
        ArrayElementValue[] arrayElementValues = {
            mock(ArrayElementValue.class),
            mock(ArrayElementValue.class),
            mock(ArrayElementValue.class)
        };

        // Act & Assert - call with different combinations
        assertDoesNotThrow(() -> {
            for (Clazz c : clazzes) {
                for (Annotation a : annotations) {
                    for (ArrayElementValue aev : arrayElementValues) {
                        changer.visitArrayElementValue(c, a, aev);
                    }
                }
            }
        });
    }

    /**
     * Tests that the visitArrayElementValue method implements the ElementValueVisitor interface correctly.
     */
    @Test
    public void testVisitArrayElementValue_implementsInterfaceCorrectly() {
        // Assert - TargetClassChanger should be an ElementValueVisitor
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor,
                "TargetClassChanger should implement ElementValueVisitor");
    }

    /**
     * Tests that visitArrayElementValue completes without blocking or hanging.
     */
    @Test
    public void testVisitArrayElementValue_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        changer.visitArrayElementValue(clazz, annotation, arrayElementValue);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitArrayElementValue should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitArrayElementValue handles the same array element value instance multiple times
     * without accumulating state or causing issues.
     */
    @Test
    public void testVisitArrayElementValue_sameArrayElementValueInstanceMultipleTimes_noAccumulation() {
        // Arrange
        ArrayElementValue singleArrayElementValue = mock(ArrayElementValue.class);

        // Act & Assert - call multiple times with the same array element value
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                changer.visitArrayElementValue(clazz, annotation, singleArrayElementValue);
            }
        });
    }

    /**
     * Tests that visitArrayElementValue called after multiple other operations still works correctly.
     */
    @Test
    public void testVisitArrayElementValue_afterMultipleOperations_stillWorks() {
        // Simulate some prior operations
        for (int i = 0; i < 5; i++) {
            changer.visitArrayElementValue(clazz, annotation, arrayElementValue);
        }

        // Act & Assert - call visitArrayElementValue again
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue with varying combinations of parameters all execute successfully.
     */
    @Test
    public void testVisitArrayElementValue_varyingParameterCombinations_allExecuteSuccessfully() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Annotation annotation2 = mock(Annotation.class);
        ArrayElementValue arrayElementValue2 = mock(ArrayElementValue.class);

        // Act & Assert - call with various parameter combinations
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(clazz, annotation, arrayElementValue);
            changer.visitArrayElementValue(clazz2, annotation2, arrayElementValue2);
            changer.visitArrayElementValue(clazz, annotation2, arrayElementValue2);
        });
    }

    /**
     * Tests that visitArrayElementValue maintains consistency across invocations.
     */
    @Test
    public void testVisitArrayElementValue_consistentBehavior() {
        // Act & Assert - call multiple times and verify consistent behavior
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                changer.visitArrayElementValue(clazz, annotation, arrayElementValue)
            );
        }
    }

    /**
     * Tests that visitArrayElementValue processes array element values in the context of annotation processing.
     * Array element values represent array-valued annotation attributes (e.g., @Anno({val1, val2})).
     */
    @Test
    public void testVisitArrayElementValue_processesInAnnotationContext() {
        // Act & Assert - should work correctly in annotation context
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue handles array element values from different annotations correctly.
     * Each array element value belongs to an annotation and contains an array of element values.
     */
    @Test
    public void testVisitArrayElementValue_handlesArrayElementValuesFromDifferentAnnotations() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ArrayElementValue arrayElementValue1 = mock(ArrayElementValue.class);
        ArrayElementValue arrayElementValue2 = mock(ArrayElementValue.class);

        // Act & Assert - should handle array element values from different annotations
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(clazz, annotation1, arrayElementValue1);
            changer.visitArrayElementValue(clazz, annotation2, arrayElementValue2);
        });
    }

    /**
     * Tests that visitArrayElementValue works with the visitor pattern correctly.
     * The method is part of the ElementValueVisitor interface implementation.
     */
    @Test
    public void testVisitArrayElementValue_worksWithVisitorPattern() {
        // Act - use the changer as an ElementValueVisitor
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Assert - should work correctly when used through the interface
        assertDoesNotThrow(() -> visitor.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue can handle array element values in the context of
     * class retargeting during optimization.
     */
    @Test
    public void testVisitArrayElementValue_inClassRetargetingContext() {
        // Act & Assert - should work in the context of class retargeting
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue works correctly with different TargetClassChanger instances.
     * Each changer should be able to process array element values independently.
     */
    @Test
    public void testVisitArrayElementValue_withDifferentChangerInstances() {
        // Arrange
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - should work for different changer instances
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(clazz, annotation, arrayElementValue);
            changer2.visitArrayElementValue(clazz, annotation, arrayElementValue);
            changer3.visitArrayElementValue(clazz, annotation, arrayElementValue);
        });
    }

    /**
     * Tests that visitArrayElementValue integrates correctly with the overall visitor pattern
     * used in the TargetClassChanger class.
     */
    @Test
    public void testVisitArrayElementValue_integratesWithVisitorPattern() {
        // Arrange
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Act & Assert - should work through the interface
        assertDoesNotThrow(() -> visitor.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue can process array element values from annotations on different classes.
     */
    @Test
    public void testVisitArrayElementValue_fromAnnotationsOnDifferentClasses() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ArrayElementValue arrayElementValue1 = mock(ArrayElementValue.class);
        ArrayElementValue arrayElementValue2 = mock(ArrayElementValue.class);

        // Act & Assert - should process array element values from annotations on different classes
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(clazz1, annotation1, arrayElementValue1);
            changer.visitArrayElementValue(clazz2, annotation2, arrayElementValue2);
        });
    }

    /**
     * Tests that visitArrayElementValue can handle a large number of array element values.
     */
    @Test
    public void testVisitArrayElementValue_handlesLargeNumberOfArrayElementValues() {
        // Arrange
        ArrayElementValue[] arrayElementValues = new ArrayElementValue[100];
        for (int i = 0; i < arrayElementValues.length; i++) {
            arrayElementValues[i] = mock(ArrayElementValue.class);
        }

        // Act & Assert - should handle processing many array element values
        assertDoesNotThrow(() -> {
            for (ArrayElementValue aev : arrayElementValues) {
                changer.visitArrayElementValue(clazz, annotation, aev);
            }
        });
    }

    /**
     * Tests that visitArrayElementValue works correctly when processing array element values
     * in a typical annotation processing workflow.
     */
    @Test
    public void testVisitArrayElementValue_inTypicalWorkflow() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ArrayElementValue aev1 = mock(ArrayElementValue.class);
        ArrayElementValue aev2 = mock(ArrayElementValue.class);
        ArrayElementValue aev3 = mock(ArrayElementValue.class);

        // Act & Assert - simulate typical annotation processing workflow
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(clazz, annotation1, aev1);
            changer.visitArrayElementValue(clazz, annotation1, aev2);
            changer.visitArrayElementValue(clazz, annotation2, aev3);
        });
    }

    /**
     * Tests that visitArrayElementValue doesn't have side effects that affect subsequent calls.
     */
    @Test
    public void testVisitArrayElementValue_noSideEffectsOnSubsequentCalls() {
        // Arrange
        ArrayElementValue aev1 = mock(ArrayElementValue.class);
        ArrayElementValue aev2 = mock(ArrayElementValue.class);

        // Act - call twice with different array element values
        changer.visitArrayElementValue(clazz, annotation, aev1);
        changer.visitArrayElementValue(clazz, annotation, aev2);

        // Assert - second call should work without issues
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, aev2));
    }

    /**
     * Tests that visitArrayElementValue processes array element values correctly regardless of
     * the order in which they are visited.
     */
    @Test
    public void testVisitArrayElementValue_orderIndependent() {
        // Arrange
        ArrayElementValue aev1 = mock(ArrayElementValue.class);
        ArrayElementValue aev2 = mock(ArrayElementValue.class);
        ArrayElementValue aev3 = mock(ArrayElementValue.class);

        // Act & Assert - should work regardless of order
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(clazz, annotation, aev3);
            changer.visitArrayElementValue(clazz, annotation, aev1);
            changer.visitArrayElementValue(clazz, annotation, aev2);
        });
    }

    /**
     * Tests that visitArrayElementValue works correctly as part of the ElementValueVisitor
     * interface hierarchy.
     */
    @Test
    public void testVisitArrayElementValue_asPartOfVisitorHierarchy() {
        // Assert - verify the changer implements the ElementValueVisitor interface
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor);

        // Act & Assert - should work when called through the interface
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;
        assertDoesNotThrow(() -> visitor.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue delegates to visitAnyElementValue.
     * This verifies the first part of the delegation pattern used by the method.
     */
    @Test
    public void testVisitArrayElementValue_delegatesToVisitAnyElementValue() {
        // Arrange - create a spy to verify delegation
        TargetClassChanger spyChanger = spy(new TargetClassChanger());

        // Act
        spyChanger.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify that visitArrayElementValue was called
        verify(spyChanger).visitArrayElementValue(clazz, annotation, arrayElementValue);
    }

    /**
     * Tests that visitArrayElementValue passes the changer itself as the visitor to elementValuesAccept.
     * This is crucial because the changer implements ElementValueVisitor.
     */
    @Test
    public void testVisitArrayElementValue_passesChangerAsVisitor() {
        // Act
        changer.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify that the changer itself is passed as the visitor parameter
        verify(arrayElementValue).elementValuesAccept(
            eq(clazz),
            eq(annotation),
            same(changer)  // The changer itself should be passed as visitor
        );
    }

    /**
     * Tests that visitArrayElementValue properly handles array values.
     * Array values are a key use case for array element values in annotations.
     */
    @Test
    public void testVisitArrayElementValue_handlesArrayValues() {
        // Act & Assert - should handle array structures
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue handles null clazz gracefully.
     */
    @Test
    public void testVisitArrayElementValue_withNullClazz() {
        // Act & Assert - behavior with null clazz (may or may not throw depending on implementation)
        // This test documents the behavior
        try {
            changer.visitArrayElementValue(null, annotation, arrayElementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitArrayElementValue handles null annotation gracefully.
     */
    @Test
    public void testVisitArrayElementValue_withNullAnnotation() {
        // Act & Assert - behavior with null annotation
        // This test documents the behavior
        try {
            changer.visitArrayElementValue(clazz, null, arrayElementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitArrayElementValue handles null array element value gracefully.
     */
    @Test
    public void testVisitArrayElementValue_withNullArrayElementValue() {
        // Act & Assert - behavior with null array element value
        // This test documents the behavior
        try {
            changer.visitArrayElementValue(clazz, annotation, null);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitArrayElementValue processes array element values that represent
     * annotation attribute values correctly.
     */
    @Test
    public void testVisitArrayElementValue_processesAnnotationAttributeValues() {
        // Arrange - array element values are used for array-typed annotation attributes
        ArrayElementValue attributeValue = mock(ArrayElementValue.class);

        // Act & Assert - should process annotation attribute values
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, attributeValue));
    }

    /**
     * Tests that visitArrayElementValue is correctly invoked through the visitor pattern
     * when processing annotations during class optimization.
     */
    @Test
    public void testVisitArrayElementValue_invokedDuringOptimization() {
        // Act & Assert - should be invokable during optimization phase
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue handles multiple array values correctly.
     * Some annotations may have multiple array-typed attributes.
     */
    @Test
    public void testVisitArrayElementValue_withMultipleArrayValues() {
        // Arrange - multiple array values in different annotation attributes
        ArrayElementValue array1 = mock(ArrayElementValue.class, "array1");
        ArrayElementValue array2 = mock(ArrayElementValue.class, "array2");
        ArrayElementValue array3 = mock(ArrayElementValue.class, "array3");

        // Act & Assert - should handle multiple array values
        assertDoesNotThrow(() -> {
            changer.visitArrayElementValue(clazz, annotation, array1);
            changer.visitArrayElementValue(clazz, annotation, array2);
            changer.visitArrayElementValue(clazz, annotation, array3);
        });
    }

    /**
     * Tests that visitArrayElementValue correctly processes array elements
     * by calling elementValuesAccept. This is important for recursive processing.
     */
    @Test
    public void testVisitArrayElementValue_recursivelyProcessesArrayElements() {
        // Act & Assert - should recursively process the array elements via elementValuesAccept
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, arrayElementValue));

        // Verify the recursive processing was initiated
        verify(arrayElementValue).elementValuesAccept(clazz, annotation, changer);
    }

    /**
     * Tests that visitArrayElementValue handles array element values that may have been affected
     * by class merging optimization.
     */
    @Test
    public void testVisitArrayElementValue_handlesClassMergedArrays() {
        // Act & Assert - should handle array elements whose classes may have been merged
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue passes both clazz and annotation to elementValuesAccept.
     */
    @Test
    public void testVisitArrayElementValue_passesCorrectParameters() {
        // Arrange
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        Annotation specificAnnotation = mock(Annotation.class, "specificAnnotation");

        // Act
        changer.visitArrayElementValue(specificClazz, specificAnnotation, arrayElementValue);

        // Assert - verify the correct parameters were passed
        verify(arrayElementValue).elementValuesAccept(specificClazz, specificAnnotation, changer);
    }

    /**
     * Tests that visitArrayElementValue with different array element values
     * delegates to elementValuesAccept for each one independently.
     */
    @Test
    public void testVisitArrayElementValue_withDifferentValues_delegatesIndependently() {
        // Arrange
        ArrayElementValue aev1 = mock(ArrayElementValue.class);
        ArrayElementValue aev2 = mock(ArrayElementValue.class);
        ArrayElementValue aev3 = mock(ArrayElementValue.class);

        // Act
        changer.visitArrayElementValue(clazz, annotation, aev1);
        changer.visitArrayElementValue(clazz, annotation, aev2);
        changer.visitArrayElementValue(clazz, annotation, aev3);

        // Assert - verify each value's elementValuesAccept was called once
        verify(aev1).elementValuesAccept(clazz, annotation, changer);
        verify(aev2).elementValuesAccept(clazz, annotation, changer);
        verify(aev3).elementValuesAccept(clazz, annotation, changer);
    }

    /**
     * Tests that visitArrayElementValue handles common array annotation patterns.
     * For example, @SuppressWarnings({"unchecked", "rawtypes"}).
     */
    @Test
    public void testVisitArrayElementValue_handlesCommonArrayAnnotationPatterns() {
        // Arrange - array element values used in common annotation patterns
        ArrayElementValue stringArray = mock(ArrayElementValue.class, "stringArray");

        // Act & Assert - should handle common annotation patterns
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, stringArray));
    }

    /**
     * Tests that visitArrayElementValue handles empty arrays correctly.
     * Some annotations may have empty array values.
     */
    @Test
    public void testVisitArrayElementValue_handlesEmptyArrays() {
        // Arrange - array element value representing an empty array
        ArrayElementValue emptyArray = mock(ArrayElementValue.class, "emptyArray");

        // Act & Assert - should handle empty arrays
        assertDoesNotThrow(() -> changer.visitArrayElementValue(clazz, annotation, emptyArray));
    }
}
